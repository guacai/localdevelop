create table vm_host
(
    id          INTEGER
        constraint vm_host_pk
            primary key autoincrement,
    description varchar(64) default '',
    groupId    int,
    groupName  varchar(64)  default '',
    environment varchar(64) default '',
    host        varchar(64) default '',
    shell       varchar(256) default '',
    status      int
);


create table vm_server
(
    id          INTEGER
        constraint vm_host_pk
            primary key autoincrement,
    description varchar(64) default '',
    serverUserName varchar(64) default '',
    serverUserPassWord varchar(64) default '',
    groupId    int,
    groupName  varchar(64)  default '',
    environment varchar(64) default '',
    localhost        varchar(64) default '',
    host        varchar(64) default '',
    pwd         varchar(64) default '',
    exac        varchar(256) default '',
    shell       varchar(256) default '',
    status      int
);

create table user
(
    id          INTEGER
        constraint vm_host_pk
            primary key autoincrement,
    userName varchar(64) default '',
    passWord   varchar(64) default ''
);