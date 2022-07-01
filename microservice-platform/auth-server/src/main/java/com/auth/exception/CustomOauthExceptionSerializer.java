package com.auth.exception;

import com.base.Result;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @Description 用户认证失败序列化
 * @Author linyf
 * @Date 2022-06-24 15:34
 */
public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException> {
    private static final long serialVersionUID = 2652127645704345563L;

    public CustomOauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

    @Override
    public void serialize(CustomOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(Result.err(value.getMessage()));
    }
}
