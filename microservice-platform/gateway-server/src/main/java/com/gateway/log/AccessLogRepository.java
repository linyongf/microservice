package com.gateway.log;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends ReactiveMongoRepository<GatewayLog,String> {}
