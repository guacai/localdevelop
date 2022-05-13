package com.gua.localdevelop.dao.mapper;

import com.gua.localdevelop.dao.model.VmServer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VmServerMapper {

    @Select(value = "select * from vm_server")
    List<VmServer> queryAll();

    @Select(value = "select * from vm_server where id = #{id}")
    VmServer selectId(int id);

    @Insert(value = "INSERT INTO vm_server (description, serverUserName, serverUserPassWord, groupId, groupName, environment, localhost, host, exac, pwd, shell, status) VALUES ( #{description}, #{serverUserName}, #{serverUserPassWord} ,#{groupId},  #{groupName}, #{environment}, #{localhost}, #{host}, #{exac}, #{pwd}, #{shell},#{status})")
    void insert(VmServer vmServer);

    @Update(value = "UPDATE vm_server SET description = #{description}, serverUserName = #{serverUserName},  serverUserPassWord = #{serverUserPassWord}, groupId = #{groupId}, groupName = #{groupName}, environment = #{environment}, localhost = #{localhost}, host = #{host},  exac = #{exac}, pwd = #{pwd}, shell = #{shell}, status = #{status}  where id = #{id}")
    void update(VmServer vmServer);
}
