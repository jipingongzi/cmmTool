package xc.investigation.base.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
public class LoggingAspect {
    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* xc.investigation.base.api.*Controller.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        // 打印请求路径、请求参数和 header
        logger.info("Request path: " + request.getRequestURL().toString());
        logger.info("Request params: " + request.getQueryString());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("Header name: " + headerName + ", value: " + request.getHeader(headerName));
        }
    }

    @AfterReturning(value = "controllerPointcut()", returning = "result")
    public void logAfterReturning(Object result) {
        // 打印返回值
        logger.info("Response body: " + result);
    }
}
