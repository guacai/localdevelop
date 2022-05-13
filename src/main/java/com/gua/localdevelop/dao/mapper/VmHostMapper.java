package com.gua.localdevelop.dao.mapper;

import com.gua.localdevelop.dao.model.VmHost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VmHostMapper {

    @Select(value = "select * from vm_host")
    List<VmHost> queryAll();

    @Select(value = "select * from vm_host where id = #{id}")
    VmHost selectId(int id);

    @Insert(value = "INSERT INTO vm_host (description, groupId, groupName, environment, host, shell, status) VALUES ( #{description},#{groupId},  #{groupName}, #{environment}, #{host}, #{shell}, #{status})")
    void insert(VmHost vmHost);

    @Update(value = "UPDATE vm_host SET description = #{description}, groupId = #{groupId}, groupName = #{groupName}, environment = #{environment}, host = #{host}, shell = #{shell}, status = #{status} where id = #{id}")
    void update(VmHost vmHost);
}
