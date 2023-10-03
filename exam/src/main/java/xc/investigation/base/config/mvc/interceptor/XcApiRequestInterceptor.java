package xc.investigation.base.config.mvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xc.investigation.base.constant.XcRequestHeader;
import xc.investigation.base.utils.WebUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * record all api info
 * @author ibm
 */
@Component
@Slf4j
public class XcApiRequestInterceptor implements HandlerInterceptor {

    private final WebUtil webUtil;

    public XcApiRequestInterceptor(WebUtil webUtil) {
        this.webUtil = webUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {

        String userToken = request.getHeader(XcRequestHeader.XC_USER_TOKEN.name());
        String adminToken = request.getHeader(XcRequestHeader.XC_ADMIN_TOKEN.name());
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = request.getRemoteAddr();
        String host = request.getRemoteHost();

        ServletInputStream inputStream = request.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String body = new String(bytes);
        if(!webUtil.isStaticResource(uri)) {
            log.info(String.format("\n--------------------------------------------------------------\n" +
                            "user : %s,\n" +
                            "admin : %s,\n" +
                            "method : %s,\n" +
                            "uri : %s,\n" +
                            "queryString : %s,\n" +
                            "remoteAddr : %s,\n" +
                            "host : %s ,\n" +
                            "body : %s",
                    userToken, adminToken, method, uri, queryString, remoteAddr, host, body));
        }
        return true;
    }
}
