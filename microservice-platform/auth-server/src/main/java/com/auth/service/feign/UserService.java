package com.auth.service.feign;

import com.api.entity.User;
import com.base.Result;
import com.base.constants.SystemServerConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = SystemServerConstants.FOUNDATION)
public interface UserService {

    @GetMapping("/user/{username}")
    Result<User> getUserByUsername(@PathVariable("username") String username);

}
