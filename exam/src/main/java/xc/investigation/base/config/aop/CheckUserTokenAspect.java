package xc.investigation.base.config.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xc.investigation.base.config.exception.SystemException;
import xc.investigation.base.constant.XcRequestHeader;
import xc.investigation.base.repo.entity.user.UserTokenEntity;
import xc.investigation.base.repo.jpa.user.UserTokenJpaRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 检查token
 * @author ibm
 */
@Component
@Aspect
public class CheckUserTokenAspect {

    private final UserTokenJpaRepo userTokenJpaRepo;

    public CheckUserTokenAspect(UserTokenJpaRepo userTokenJpaRepo) {
        this.userTokenJpaRepo = userTokenJpaRepo;
    }

    @Pointcut("@annotation(xc.investigation.base.config.aop.CheckUserToken)")
    public void checkToken(){}

    @Around("checkToken()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null){
            throw new SystemException("token request miss");
        }
        HttpServletRequest req = servletRequestAttributes.getRequest();
        String token = req.getHeader(XcRequestHeader.XC_USER_TOKEN.name());
        if(StringUtils.isBlank(token)){
            throw new SystemException("token miss");
        }
        Optional<UserTokenEntity> userTokenOptional = userTokenJpaRepo.findByToken(token);
        if(!userTokenOptional.isPresent()){
            throw new SystemException("token invalid");
        }
        return joinPoint.proceed();
    }
}
