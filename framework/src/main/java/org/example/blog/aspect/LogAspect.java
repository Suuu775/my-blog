package org.example.blog.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.blog.annotation.SystemLog;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(org.example.blog.annotation.SystemLog)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object printLog(ProceedingJoinPoint joinPoint){
        try {
            handleBefore(joinPoint);
            Object proceed = joinPoint.proceed();
            handleAfter(proceed);
            return proceed;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            log.info("=======End=======" + System.lineSeparator());
        }
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();


        log.info("=======Start=======");
// 打印请求 URL
        log.info("URL : {}",request.getRequestURL().toString());
// 打印描述信息
        log.info("BusinessName : {}",signature.getMethod().getAnnotation(SystemLog.class).businessName());
// 打印 Http method
        log.info("HTTP Method : {}",  request.getMethod());
// 打印调用 controller 的全路径以及执行方法
        log.info("Class Method : {}.{}", signature.getDeclaringTypeName(),signature.getName());
// 打印请求的 IP
        log.info("IP : {}",request.getRemoteAddr());
// 打印请求入参
        log.info("Request Args : {}",joinPoint.getArgs());
    }

    private void handleAfter(Object proceed) {
        // 打印出参
        log.info("Response : {}", JSON.toJSONString(proceed));
    }
}
