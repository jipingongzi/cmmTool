package com.sean.cmm.cli.connector;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LdapService {
    public void authenticate(String url, String userName, String pwd){

    }

    public List<String> checkPermission(String userName){
        return Collections.emptyList();
    }

    public LdapUser loadUser(String userName){
        return new LdapUser();
    }
}
