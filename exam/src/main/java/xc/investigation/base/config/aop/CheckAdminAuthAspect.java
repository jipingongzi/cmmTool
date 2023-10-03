package xc.investigation.base.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.utils.WebUtil;

/**
 * 检查token
 * @author ibm
 */
@Component
@Aspect
public class CheckAdminAuthAspect {

    private final WebUtil webUtil;

    public CheckAdminAuthAspect(WebUtil webUtil) {
        this.webUtil = webUtil;
    }

    @Pointcut("@annotation(xc.investigation.base.config.aop.AdminAuth)")
    public void checkAdminAuth(){}

    @Around("checkAdminAuth()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if(!SysAdminRoleType.ADMIN.equals(webUtil.getCurrentAdmin().getRoleType())){
            throw new BizException("无权访问");
        }
        return joinPoint.proceed();
    }
}
