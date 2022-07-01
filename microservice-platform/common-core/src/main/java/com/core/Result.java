package com.core;

import com.core.constants.BasicConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

@ApiModel(value = "RESTful API 标准返回结果", description = "用于统一标准的响应格式")
@Getter
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "响应码，0：成功；-1：失败", example = "0")
    private Integer code;

    @ApiModelProperty(value = "响应消息，当 code 等于 -1 时的提示消息")
    private String msg = "";

    @ApiModelProperty
    private T data;

    public Result(){}

    private Result(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok(){
        return new Result(BasicConstants.SUCCESS, "success", null);
    }

    public static <T> Result<T> ok(T data){
        return new Result<T>(BasicConstants.SUCCESS, "success", data);
    }

    public static Result err(String msg){
        return new Result(BasicConstants.FAILURE, msg, null);
    }

    public static <T> Result<T> err(String msg, T data){
        return new Result<T>(BasicConstants.FAILURE, msg, data);
    }
}
