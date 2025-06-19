package com.qingjin.aspect;

import com.qingjin.annotation.autofill.AutoFill;
import com.qingjin.constant.AutoFillConstant;
import com.qingjin.context.BaseContext;
import com.qingjin.enumeration.OperationType;
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
 * 自定义的切面类，用于实现公共字段自动填充的方法
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.qingjin.mapper.*.*(..)) &&  @annotation(com.qingjin.annotation.autofill.*)")
    public void autoFillPointCut(){}


    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始公共字段的自动填充");

        //获取被拦截的数据库类型  -获取注解上的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value(); //获取数据库操作类型

        //获取当前被拦截方法的参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = (Long) BaseContext.getContext();

        //根据不同的操作类，为对应属性通过反射来赋值
        if(operationType == OperationType.USER_INSERT){
            try {
                // 获取反射方法
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.USER_UPDATE){
            try {

                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //根据不同的操作类，为对应属性通过反射来赋值
        if(operationType == OperationType.TIME_INSERT){
            try {
                // 获取反射方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,currentId);
                setUpdateTime.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.TIME_UPDATE){
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class);
                setUpdateTime.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //根据不同的操作类，为对应属性通过反射来赋值
        if(operationType == OperationType.USERTIME_INSERT){
            try {
                // 获取反射方法
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class);

                //通过反射为对象属性赋值
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
                setCreateTime.invoke(entity,currentId);
                setUpdateTime.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.USER_UPDATE){
            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class);
                setUpdateTime.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
