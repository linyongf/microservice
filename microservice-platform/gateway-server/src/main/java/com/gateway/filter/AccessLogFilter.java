package com.gateway.filter;

import com.base.Result;
import com.base.constants.BasicConstants;
import com.base.utils.JsonUtil;
import com.gateway.log.AccessLogRepository;
import com.gateway.log.GatewayLog;
import com.gateway.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description ????????????????????????????????????
 * @Author linyf
 * @Date 2022-06-23 16:55
 */
@Slf4j
@Component
public class AccessLogFilter implements GlobalFilter, Ordered {

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Resource
    private AccessLogRepository accessLogRepository;

    /**
     * JWT???????????????
     */
    @Resource
    private TokenStore tokenStore;

    /**
     * ???????????????<-1?????????????????? NettyWriteResponseFilter ???????????????????????????????????????????????????????????????
     * ??????????????????????????? -1 ?????????????????????????????????????????????
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // ????????????
        String requestPath = request.getPath().pathWithinApplication().value();
        Route route = getGatewayRoute(exchange);
        String ipAddress = request.getRemoteAddress().getAddress().getHostAddress();

        GatewayLog gatewayLog = new GatewayLog();
        gatewayLog.setSchema(request.getURI().getScheme());
        gatewayLog.setRequestMethod(request.getMethodValue());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getId());
        gatewayLog.setRequestTime(LocalDateTime.now());
        gatewayLog.setIp(ipAddress);

        String token = Utils.getToken(request);
        if(!StringUtils.isEmpty(token)){
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            gatewayLog.setRequestUser(additionalInformation.get(BasicConstants.USER_NAME).toString());
        }

        MediaType mediaType = request.getHeaders().getContentType();
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)
                || MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)
                || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            return writeBodyLog(exchange, chain, gatewayLog);
        } else {
            return writeBasicLog(exchange, chain, gatewayLog);
        }
    }

    private Mono<Void> writeBasicLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog accessLog) {
        StringBuilder builder = new StringBuilder();
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            builder.append(entry.getKey()).append("=").append(StringUtils.collectionToCommaDelimitedString(entry.getValue()));
        }
        accessLog.setRequestBody(builder.toString());

        //???????????????
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // ????????????
                    writeAccessLog(accessLog);
                }));
    }

    /**
     * ?????? request body ???????????????????????????
     * ??????: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono writeBodyLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    gatewayLog.setRequestBody(body);
                    return Mono.just(body);
                });

        // ?????? BodyInserter ?????? body(????????????body), ?????? request body ??????????????????
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // ??????????????????
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);
                    // ??????????????????
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
                    // ???????????????
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // ????????????
                                writeAccessLog(gatewayLog);
                            }));
                }));
    }

    /**
     * ????????????
     *
     * @param gatewayLog ????????????
     * @date
     */
    private void writeAccessLog(GatewayLog gatewayLog) {
        if(StringUtils.isEmpty(gatewayLog.getRequestUser()) && gatewayLog.getResponseData()!=null){
            Result<String> result = JsonUtil.deserialize(gatewayLog.getResponseData(), Result.class);
            if(result.getCode()==BasicConstants.SUCCESS){
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(result.getData());
                Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
                gatewayLog.setRequestUser(additionalInformation.get(BasicConstants.USER_NAME).toString());
            }
        }
        log.info(gatewayLog.toString());
        if(!StringUtils.isEmpty(gatewayLog.getRequestUser()) && gatewayLog.getResponseData()!=null){
            accessLogRepository.insert(gatewayLog).subscribe();
        }
    }

    private Route getGatewayRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }

    /**
     * ?????????????????????????????? headers
     *
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    /**
     * ??????????????????
     * ?????? DataBufferFactory ????????????????????????????????????
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, GatewayLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    LocalDateTime respTime = LocalDateTime.now();
                    gatewayLog.setResponseTime(respTime);
                    // ?????????????????? ????????????
                    long executeTime = Duration.between(gatewayLog.getRequestTime(), respTime).getNano();
                    BigDecimal bigDecimal = new BigDecimal(executeTime);
                    BigDecimal divide = bigDecimal.divide(new BigDecimal(1_000_000_000), 3, RoundingMode.DOWN);
                    gatewayLog.setExecuteTime(divide.floatValue());

                    // ?????????????????????????????? json ?????????
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);

                    if (HttpStatus.OK.equals(this.getStatusCode())
                            && !StringUtils.isEmpty(originalResponseContentType)
                            && originalResponseContentType.contains(MediaType.APPLICATION_JSON_VALUE)) {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // ???????????????????????????????????????????????????
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);

                            // ???????????????
                            DataBufferUtils.release(join);
                            String responseResult = new String(content, StandardCharsets.UTF_8);

                            gatewayLog.setResponseData(responseResult);

                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }
}
