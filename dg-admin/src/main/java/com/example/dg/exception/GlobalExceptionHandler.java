package com.example.dg.exception;

import com.example.dg.common.constant.HttpStatus;
import com.example.dg.common.exception.ServiceException;
import com.example.dg.common.exception.user.UserPasswordNotMatchException;
import com.example.dg.result.Result;
import com.example.dg.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 
 * @version 1.0
 * @ControllerAdvice: 使用它可以标识一个全局异常处理器/对象
 * 会注入到spring容器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserPasswordNotMatchException.class)
    public Result handleAccessDeniedException(UserPasswordNotMatchException e, HttpServletRequest request, HandlerMethod handlerMethod)
    {
        String requestURI = request.getRequestURI();
        log.info("请求地址'{}',异常发生的方法是={}", requestURI, handlerMethod.getMethod());
        return Result.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? Result.error(code, e.getMessage()) : Result.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.error(e.getMessage());
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return Result.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    //1、编写方法,处理指定异常, 比如我们处理算术异常和空指针异常, 可以指定多个异常
    //2. 这里要处理的异常，由程序员来指定
    //3. Exception e : 表示异常发生后，传递的异常对象
    //4. Model model: 可以将我们的异常信息，放入model,并传递给显示页面
    //提出一个问题，如何获取到异常发生的方法

//    @ExceptionHandler({ArithmeticException.class, NullPointerException.class})
//    public Result handleAritException(Exception e, Model model, HandlerMethod handlerMethod) {
//
//        log.info("异常信息={}", e.getMessage());
//        //得到异常发生的方法是哪个
//        log.info("异常发生的方法是={}", handlerMethod.getMethod());
//        Result result = Result.error(e.getMessage());
//        return result;
//    }


}
