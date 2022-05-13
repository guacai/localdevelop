package com.gua.localdevelop.controller;

import com.alibaba.fastjson.JSON;
import com.gua.localdevelop.annotation.WithoutLogin;
import com.gua.localdevelop.contants.Contants;
import com.gua.localdevelop.contants.ErrorCode;
import com.gua.localdevelop.dao.mapper.UserMapper;
import com.gua.localdevelop.dao.model.User;
import com.gua.localdevelop.utils.AESUtil;
import com.gua.localdevelop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @WithoutLogin
    @RequestMapping("user/login")
    public ResultVO login(HttpServletResponse response, @RequestBody User user){
        User user1 = userMapper.queryOne(user);
        if(Objects.isNull(user1)){
            return ResultVO.error(ErrorCode.USER_NOT_LOGIN_EXCEPTION);
        }
        String encodeUser = AESUtil.encrypt(JSON.toJSONString(user1));
        Cookie cookie = new Cookie(Contants.USER_COOKIE_TOKEN, encodeUser);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResultVO.success();
    }
}
