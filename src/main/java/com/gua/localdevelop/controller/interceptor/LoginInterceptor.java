package com.gua.localdevelop.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gua.localdevelop.annotation.WithoutLogin;
import com.gua.localdevelop.contants.Contants;
import com.gua.localdevelop.contants.ErrorCode;
import com.gua.localdevelop.dao.mapper.UserMapper;
import com.gua.localdevelop.dao.model.User;
import com.gua.localdevelop.utils.AESUtil;
import com.gua.localdevelop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod action = (HandlerMethod) handler;
            WithoutLogin withoutLogin = action.getMethodAnnotation(WithoutLogin.class);
            if (Objects.nonNull(withoutLogin)) {
                return true;
            }
        }
        boolean needLogin = false;
        String userCacheId = AESUtil.decrypt(getCookieValue(request, Contants.USER_COOKIE_TOKEN));
        if (ObjectUtils.isEmpty(userCacheId)) {
            needLogin = true;
        } else {
            User parse = JSON.parseObject(userCacheId, User.class);
            User user = userMapper.queryOne(parse);
            if (Objects.isNull(user)) {
                needLogin = true;
            }
        }
        if (needLogin) {
            generateNotLoginResponse(response);
            return false;
        }
        return true;
    }

    private void generateNotLoginResponse(HttpServletResponse response) throws IOException {
        ResultVO<Map<String, String>> resultVO = ResultVO.error(ErrorCode.ARGS_EXCEPTION);
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().print(OBJECT_MAPPER.writeValueAsString(resultVO));
    }


    private String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            log.debug("cookie is null");
            return null;
        }
        for (Cookie cookie : cookies) {
            log.debug("cookie name {} value {} {} {}", cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath());
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
