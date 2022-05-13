package com.gua.localdevelop.service;

import com.gua.localdevelop.vo.ResultVO;

import java.net.UnknownHostException;

public interface CommandService {
    ResultVO executeCmd(String cmd, String... args);
    ResultVO executeServer(String shell, String serverUserName, String serverUserPassWord, String localHost, String pwd, String cmd, String targetHost) throws UnknownHostException;
    ResultVO executePing(String shell, String serverHost, String targetServer);
}
