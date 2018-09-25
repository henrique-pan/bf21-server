package com.bf21.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.bf21.service..*.*(..))")
    public void service() {}

    @Pointcut("execution(* com.bf21.rs..*.*(..))")
    public void rs() {}

    @Pointcut("execution(* com.bf21.repository..*.*(..))")
    public void repository() {}

    @Pointcut("execution(* com.bf21.main..*.*(..))")
    public void main() {}

    @Pointcut("within(com.bf21.interceptor.RestExceptionHandler)")
    public void exceptionHandler() {}

    @Before("main() || service() || rs() || repository()")
    public void before(JoinPoint joinPoint) throws Throwable {
        StringBuffer logMessage = new StringBuffer();

        setClass(joinPoint, logMessage);
        setMethod(joinPoint, logMessage);

        log.info(logMessage.toString());
    }

    @AfterReturning(pointcut = "main() || service() || rs() || repository() || exceptionHandler()", returning = "result")
    public void after(JoinPoint joinPoint, Object result) throws Throwable {
        StringBuffer logMessage = new StringBuffer();

        setClass(joinPoint, logMessage);
        setMethod(joinPoint, logMessage);
        setReturnValue(result, logMessage);

        log.info(logMessage.toString());
    }

    @AfterThrowing(pointcut = "main() || service() || rs() || repository() || exceptionHandler()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        StringBuffer logMessage = new StringBuffer();

        setClass(joinPoint, logMessage);
        setMethod(joinPoint, logMessage);
        setException(exception, logMessage);

        log.error(logMessage.toString());
    }

    private static void setClass(JoinPoint joinPoint, StringBuffer logMessage) {
        logMessage.append("[");
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append("]");
    }

    private static void setMethod(JoinPoint joinPoint, StringBuffer logMessage) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // MethodName & Return Type
        logMessage.append("[");
        logMessage.append(methodSignature.getName());
        logMessage.append(":").append(methodSignature.getReturnType().getCanonicalName());
        logMessage.append("]");

        // ArgsName:Value
        Object[] params = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        if (args.length > 0) {
            logMessage.append("[");
            for (int i = 0; i < args.length; i++) {
                logMessage.append(params[i]).append(":");
                logMessage.append("[").append(args[i]).append("],");
            }

            logMessage.deleteCharAt(logMessage.length() - 1);
            logMessage.append("]");
        }
    }

    private static void setReturnValue(Object result, StringBuffer logMessage) {
        logMessage.append("[");
        logMessage.append(result);
        logMessage.append("]");
    }

    private static void setExecutionTime(long executionTime, StringBuffer logMessage) {
        logMessage.append("[@");
        logMessage.append(executionTime);
        logMessage.append("ms]");
    }

    private static void setException(Throwable exception, StringBuffer logMessage) {
        logMessage.append("[");
        logMessage.append(exception.getClass().getName());
        if(exception.getCause() != null) logMessage.append(":").append(exception.getCause());
        if(exception.getMessage() != null) logMessage.append(":").append(exception.getMessage());
        logMessage.append("]");
    }
}
