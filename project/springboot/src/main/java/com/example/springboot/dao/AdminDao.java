//DAO 层（Data Access Object 层） 是一种专注于 数据持久化操作的设计模式，通常用于抽象和封装对数据库的访问逻辑。
// 在实际开发中，DAO 层在 MyBatis 等框架中对应于 Mapper 层。

package com.example.springboot.dao;

import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AdminDao extends Mapper<Admin> {

    List<Admin> getAll();

    @Select("select * from admin where `userName` = #{adminName}")
    Admin findByName(@Param("adminName") String adminName);//如果存在返回该adminname

    @Select("select * from admin where `id` = #{id}")
    Admin findById(@Param("id")Integer id);

    List<Admin> findBySearch(@Param("search") Admin search);
}