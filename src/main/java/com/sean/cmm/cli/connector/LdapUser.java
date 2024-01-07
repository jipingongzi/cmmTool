package com.sean.cmm.cli.connector;

import lombok.Data;

import java.util.List;

@Data
public class LdapUser {
    private String userName;
    private String userType;
    private List<String> permissions;
    private Boolean active;
}
