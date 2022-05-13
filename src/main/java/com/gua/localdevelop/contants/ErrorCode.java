package com.gua.localdevelop.contants;


public enum ErrorCode {
    SUCCESS(0, "成功"),
    ARGS_EXCEPTION(10001, "参数异常"),
    FILE_EXCEPTION(10002, "上传文件失败"),
    SHELL_EXCEPTION(10003, "执行脚本失败"),
    USER_NOT_LOGIN_EXCEPTION(100000, "用户未登陆"),
    ;

    int code;
    String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
