package com.example.sbjconvert.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LogExecutionResultAspect {
    @Pointcut("@annotation(com.example.sbjconvert.annotation.LogExecutionResult)")
    public void logResultPointcut() {
    }

    @AfterReturning(pointcut = "logResultPointcut()", returning = "result")
    public void logExecutionResult(JoinPoint joinPoint, Object result) {
        logDetails();
    }

    @AfterThrowing(pointcut = "logResultPointcut()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        logDetails();
    }

    private void logDetails() {
        var requestAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var request = requestAttrs.getRequest();
        var geoLocationDetails = request.getAttribute("geoLocationDetails");
        log.info("GeoLocation Details: {}", geoLocationDetails);
        // ....
    }
}
