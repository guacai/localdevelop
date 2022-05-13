package com.gua.localdevelop.dao.model;

import lombok.Data;

@Data
public class VmServer {
    private int id;
    private String description;
    private String serverUserName;
    private String serverUserPassWord;
    private int groupId;
    private String groupName;
    private String environment;
    private String localhost;
    private String host;
    private String exac;
    private String pwd;
    private String shell;
    private int status;

}
