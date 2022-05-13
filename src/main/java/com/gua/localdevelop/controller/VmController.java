package com.gua.localdevelop.controller;

import com.gua.localdevelop.contants.CmdEnum;
import com.gua.localdevelop.contants.ErrorCode;
import com.gua.localdevelop.dao.mapper.VmHostMapper;
import com.gua.localdevelop.dao.mapper.VmServerMapper;
import com.gua.localdevelop.dao.model.VmHost;
import com.gua.localdevelop.dao.model.VmServer;
import com.gua.localdevelop.service.CommandService;
import com.gua.localdevelop.vo.CmdVO;
import com.gua.localdevelop.vo.PingVO;
import com.gua.localdevelop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

@RestController
public class VmController {

    @Autowired
    private VmHostMapper vmHostMapper;

    @Autowired
    private VmServerMapper vmServerMapper;

    @Autowired
    private CommandService commandService;

    @RequestMapping("machine/list")
    public ResultVO queryAll(){
        List<VmHost> vmHosts = vmHostMapper.queryAll();
        return ResultVO.success(vmHosts);
    }

    @RequestMapping("machine/ping")
    public ResultVO machinePing(@RequestBody PingVO pingVO){
        VmHost vmHost = vmHostMapper.selectId(pingVO.getId());
        return commandService.executePing(vmHost.getShell(), vmHost.getHost(), pingVO.getHosts());
    }

    @RequestMapping("machine/add")
    public ResultVO machineAdd(@RequestBody VmHost vmHost){
        vmHostMapper.insert(vmHost);
        return ResultVO.success();
    }

    @RequestMapping("machine/update")
    public ResultVO machineUpdate(@RequestBody VmHost vmHost){
        vmHostMapper.update(vmHost);
        return ResultVO.success();
    }


    @RequestMapping("server/list")
    public ResultVO queryAllVmHosts(){
        List<VmServer> vmServers = vmServerMapper.queryAll();
        return ResultVO.success(vmServers);
    }

    @RequestMapping("server/add")
    public ResultVO vmHostsAdd(@RequestBody VmServer vmServer){
        vmServerMapper.insert(vmServer);
        return ResultVO.success();
    }

    @RequestMapping("server/update")
    public ResultVO vmHostsUpdate(@RequestBody VmServer vmServer){
        vmServerMapper.update(vmServer);
        return ResultVO.success();
    }

    @RequestMapping("server/run")
    public ResultVO executeServer(@RequestBody CmdVO cmdVO) throws UnknownHostException {
        String cmd = CmdEnum.getCmd(cmdVO.getCmd());
        VmServer vmServer = vmServerMapper.selectId(cmdVO.getId());
        if (Objects.isNull(vmServer)){
            return ResultVO.error(ErrorCode.ARGS_EXCEPTION);
        }
        ResultVO result = commandService.executeServer(vmServer.getShell(), vmServer.getServerUserName(), vmServer.getServerUserPassWord(), vmServer.getLocalhost(), vmServer.getPwd(), cmd, vmServer.getHost());
        if(result.getErrno() == ErrorCode.SUCCESS.getCode()){
            vmServer.setStatus(cmdVO.getCmd());
        }else {
            vmServer.setStatus(-1);
        }
        vmServerMapper.update(vmServer);
        return result;
    }

}
