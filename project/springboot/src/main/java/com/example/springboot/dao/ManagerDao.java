package com.example.springboot.dao;

import com.example.springboot.entity.Manager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ManagerDao extends Mapper<Manager> {

    List<Manager> getAll();

    @Select("select * from manager where `userName` = #{managerName}")
    Manager findByName(@Param("managerName") String managerName);

    @Select("select * from manager where `id` = #{id}")
    Manager findById(@Param("id")Integer id);

    List<Manager> findBySearch(@Param("search") Manager search);
}