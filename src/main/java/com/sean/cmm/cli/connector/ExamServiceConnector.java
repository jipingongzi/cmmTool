package com.sean.cmm.cli.connector;

import com.sean.cmm.cli.ICmdConnector;
import com.sean.cmm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamServiceConnector implements ICmdConnector {

    @Autowired
    private CertificateService certificateService;
}
