package com.gua.localdevelop.contants;

public enum CmdEnum {
    RESTART(1, "restart"),
    START(2, "start"),
    END(3, "stop"),
    ;


    private int cmdId;
    private String cmd;

    CmdEnum(int cmdId, String cmd) {
        this.cmdId = cmdId;
        this.cmd = cmd;
    }

    public static String getCmd(int cmdId) {
        for (CmdEnum value : CmdEnum.values()) {
            if(cmdId == value.cmdId){
                return value.cmd;
            }
        }
        return "restart";
    }
}
