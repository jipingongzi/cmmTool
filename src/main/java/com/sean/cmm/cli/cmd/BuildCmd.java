package com.sean.cmm.cli.cmd;

import com.sean.cmm.cli.BaseCmd;
import com.sean.cmm.dto.CertificateResultDto;
import com.sean.cmm.service.CertificateService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildCmd extends BaseCmd {

    private final CertificateService certificateService;

    public BuildCmd(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public Object[] parseArgs(String userInput) {
        List<CertificateResultDto> ars = new ArrayList<>();
        return new List[]{ars};
    }
}
