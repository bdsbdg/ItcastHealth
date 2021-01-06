package com.itheima.controller;

import com.itheima.entity.Result;
import com.itheima.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 校验统一处理异常
 * 如果校验不通过，就在异常中处理，统一输出格式
 * created by chl on 2020/6/11
 **/
@Slf4j
@RestControllerAdvice
public class RequestValidateExceptionHandle {

    /**
     * 校验错误拦截处理
     * @return 错误信息
     */

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Result tesseractExceptionExceptionHandler(MethodArgumentNotValidException e) {
//        e.printStackTrace();
//        return new Result(false,"参数错误");
//    }
//


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result validationBodyException(MethodArgumentNotValidException exception) {
//        log.error(exception.getCause().getLocalizedMessage());
        BindingResult result = exception.getBindingResult();
        String message = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                });
                if (errors.size() > 0) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    message = fieldError.getDefaultMessage();
                }
            }
        }
        return new Result(false, message);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public Result parameterTypeException(HttpMessageConversionException exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return new Result(false,"参数类型转换错误");
    }

    @ExceptionHandler(ServiceException.class)
    public Result serviceHandlerException(ServiceException exception){
        log.error(exception.getMessage());
        return new Result(false,exception.getMessage());
    }


        @ExceptionHandler(Exception.class)
    public Result commonExceptionHandler(Exception exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return new Result(false,"未知异常");
    }

}