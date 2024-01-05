package com.sean.cmm.cli;

public interface ICmdConnector {

    Boolean connect(String url, String username, String pwd);
}
