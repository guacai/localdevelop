package com.gua.localdevelop.dao.mapper;

import com.gua.localdevelop.dao.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Select(value = "select * from user where userName = #{userName} and passWord = #{passWord}")
    User queryOne(User user);
}
