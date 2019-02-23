package com.ihuntto.aop_simplelogger;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LogcatAspect {
    private static final String POINTCUT_METHOD =
            "execution(@com.ihuntto.aop_simplelogger.Logcat * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void pointcut() {
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Log.d(className, methodName);
        joinPoint.proceed();
    }
}
