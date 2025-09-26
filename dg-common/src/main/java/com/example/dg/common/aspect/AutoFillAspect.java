package com.example.dg.common.aspect;


import com.example.dg.common.annotation.AutoFill;
import com.example.dg.common.constant.AutoFillConstant;
import com.example.dg.common.context.BaseContext;
import com.example.dg.common.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.example.dg.mapper.*.*(..)) && @annotation(com.example.dg.common.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     * @param joinPoint
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");

        // 获取到被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取到被拦截的方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取到被拦截的方法上的注解对象
        OperationType operationType = autoFill.value();//获取到数据库操作类型

        // 获取到被拦截方法的参数-->实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0)
            return;

        Object entity = args[0];

        // 准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据数据库操作类型，对对应属性通过反射赋值
        if (operationType == OperationType.INSERT) {
            // 对4个公共字段赋值
            try {
                Method setCreateAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_AT, LocalDateTime.class);
                Method setUpdateAt= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_AT, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateAt.invoke(entity, now);
                setUpdateAt.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            // 对2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_AT, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
