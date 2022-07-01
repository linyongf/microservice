package com.core.config;

import com.core.Result;
import com.core.constants.BasicConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @Description 统一异常处理
 * @Author linyf
 * @Date 2022-06-24 16:12
 */
@RestControllerAdvice
@Slf4j
public class UnifiedExceptionControllerAdvice {
    @ExceptionHandler
    public Result handler(Exception e) {
        log.error("系统内部错误，message = " + e.getLocalizedMessage(), e);
        return Result.err(e.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder errMsg = new StringBuilder();

        BindingResult bindingResult = e.getBindingResult();
        if (Objects.nonNull(bindingResult)) {
            List<ObjectError> objectErrors = bindingResult.getAllErrors();
            for (ObjectError oe : objectErrors) {
                errMsg.append(oe.getDefaultMessage()).append(BasicConstants.NEW_LINE);
            }
        }

        return Result.err(errMsg.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolatedException(ConstraintViolationException e) {
        StringBuilder errMsg = new StringBuilder();

        Optional<Set<ConstraintViolation<?>>> optional = Optional.ofNullable(e.getConstraintViolations());
        Set<ConstraintViolation<?>> constraintViolations = optional.orElse(new HashSet<>());

        for (ConstraintViolation cv : constraintViolations) {
            errMsg.append(cv.getMessage()).append(BasicConstants.NEW_LINE);
        }

        return Result.err(errMsg.toString());
    }
}
