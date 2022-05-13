package com.gua.localdevelop.dao.model;

import lombok.Data;

@Data
public class VmHost {
    private int id;
    private String description;
    private int groupId;
    private String groupName;
    private String environment;
    private String host;
    private String shell;
    private int status;

}
