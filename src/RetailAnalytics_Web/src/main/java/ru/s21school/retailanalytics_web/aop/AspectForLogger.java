package ru.s21school.retailanalytics_web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AspectForLogger {
    @Around("allServiceMethods()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Method {}.{} called with argument(s) = {}",
                proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName(),
                Arrays.toString(proceedingJoinPoint.getArgs()));
        try {
            Object result = proceedingJoinPoint.proceed();
            log.info("Method {}.{} executed",
                    proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName());
            return result;
        } catch (Throwable ex) {
            log.warn("Method {}.{} throw exception with message {}", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(), ex.getMessage());
            throw ex;
        }
    }

    @Pointcut("execution(* ru.s21school.retailanalytics_web.services.*.*.*(..))")
    public void allServiceMethods() {
    }
}
