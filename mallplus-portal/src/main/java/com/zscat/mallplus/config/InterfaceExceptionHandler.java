package com.zscat.mallplus.config;

import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.exception.JwtTokenExpiredException;
import com.zscat.mallplus.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器
 *
 * @author ieflex
 */
@RestControllerAdvice
@Slf4j
public class InterfaceExceptionHandler {

    /**
     * 接口 业务异常
     */
    @ResponseBody
    @ExceptionHandler(ClassCastException.class)
    public Object businessInterfaceException(ClassCastException e) {
        log.error(e.getMessage(), e);
     //   e.printStackTrace();
        return new CommonResult().fail(100);
    }
    /**
     * 接口 业务异常
     */
    @ResponseBody
    @ExceptionHandler(JwtTokenExpiredException.class)
    public Object JwtTokenExpiredException(JwtTokenExpiredException e) {
        log.error(e.getMessage(), e);
        //   e.printStackTrace();
        return new CommonResult().fail(300);
    }
    /**
     * 拦截所有运行时的全局异常   
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object runtimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        // 返回 JOSN
        return new CommonResult().failed(e.getMessage());
    }

    /**
     * 系统异常捕获处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exception(Exception e) {
        log.error(e.getMessage(), e);
        return new CommonResult().fail(500);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleBusinessException(MethodArgumentNotValidException exception){
        log.error(exception.getMessage(), exception);
        return new CommonResult().failed(exception.getBindingResult().getFieldError().getDefaultMessage());
    }
}
