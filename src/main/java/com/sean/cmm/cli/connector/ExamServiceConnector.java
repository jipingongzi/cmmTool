package com.sean.cmm.cli.connector;

import com.sean.cmm.cli.ICmdConnector;
import com.sean.cmm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamServiceConnector implements ICmdConnector {

    @Autowired
    private CertificateService certificateService;

    private final String localUrl = "127.0.0.1";
    private final String localUserName = "root";
    private final String localPwd = "123456";

    @Override
    public Boolean connect(String url, String username, String pwd) {
        if(localUrl.equals(url)){
            return localUserName.equals(username) && localPwd.equals(pwd);
        }
        // todo remote LDAP check
        return false;

    }
}
