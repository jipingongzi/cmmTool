package xc.investigation.base.config.mvc.interceptor;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xc.investigation.base.config.exception.SystemException;
import xc.investigation.base.constant.XcRequestHeader;
import xc.investigation.base.constant.XcRequestSlat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * check request illegal
 * @author ibm
 */
@Component
@Slf4j
public class XcBadRequestInterceptor implements HandlerInterceptor {

    private static final MD5 MD5_CODER = MD5.create();
    private static final String ADMIN_PATH = "/admin";
    private static final String USER_PATH = "/user";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        String uri = request.getRequestURI();
        String sign = request.getHeader(XcRequestHeader.XC_USER_SIGN.name());
        if(uri.contains(ADMIN_PATH)){
            sign = request.getHeader(XcRequestHeader.XC_ADMIN_SIGN.name());
        }
        String salt = XcRequestSlat.XC_USER_SLAT.getValue();
        String plaintext = uri + salt;
        String ciphertext = new String(MD5_CODER.digest(plaintext, StandardCharsets.UTF_8));

        if(!StringUtils.equals(ciphertext,sign)){
            throw new SystemException("非法访问");
        }
        return true;
    }
}
