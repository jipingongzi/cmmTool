package xc.investigation.base.config.mvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xc.investigation.base.constant.XcRequestHeader;
import xc.investigation.base.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * check request illegal
 * @author ibm
 */
@Component
@Slf4j
public class XcTokenHolderInterceptor implements HandlerInterceptor {

    private static final String ADMIN_PATH = "/admin";
    private static final String USER_PATH = "/user";

    private final WebUtil webUtil;

    public XcTokenHolderInterceptor(WebUtil webUtil) {
        this.webUtil = webUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        final String errorUri = "/error";
        String uri = request.getRequestURI();
        if (freeToken(uri) || uri.endsWith(errorUri)) {
            return true;
        }
        if(uri.contains(ADMIN_PATH)){
            String token = request.getHeader(XcRequestHeader.XC_ADMIN_TOKEN.name());
            webUtil.setCurrentAdmin(token);
        }else {
            String token = request.getHeader(XcRequestHeader.XC_USER_TOKEN.name());
            webUtil.setCurrentUser(token);
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                         Exception ex) throws Exception {
        String uri = request.getRequestURI();
        if(uri.contains(ADMIN_PATH)){
            webUtil.removeCurrentAdmin();
        }else {
            webUtil.removeCurrentUser();
        }
    }

    private Boolean freeToken(String uri){
        if(webUtil.isStaticResource(uri)){
            return true;
        }
        if (uri.contains(ADMIN_PATH)){
            return adminUriFreeTokenList.contains(uri);
        }else if(uri.contains(USER_PATH)){
            return userUriFreeTokenList.contains(uri);
        }else {
            return false;
        }
    }

    private final List<String> userUriFreeTokenList = Arrays.asList("/user/sys/login");
    private final List<String> adminUriFreeTokenList = Arrays.asList("/admin/sys/manager/login");

}
