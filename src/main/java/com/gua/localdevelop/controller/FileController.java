package com.gua.localdevelop.controller;


import com.gua.localdevelop.dao.mapper.VmServerMapper;
import com.gua.localdevelop.dao.model.VmServer;
import com.gua.localdevelop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static com.gua.localdevelop.contants.ErrorCode.FILE_EXCEPTION;

@RestController
public class FileController {

    @Autowired
    private VmServerMapper vmServerMapper;

    @PostMapping("/server/upfile")
    public ResultVO upfile(@RequestParam("file") MultipartFile file, @RequestParam int id) {
        // 获取文件名
        VmServer vmServer = vmServerMapper.selectId(id);
        File dest = new File(vmServer.getPwd());
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            OutputStream outStream = new FileOutputStream(dest);
            outStream.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error(FILE_EXCEPTION);
        }
        return ResultVO.success();
    }

}
