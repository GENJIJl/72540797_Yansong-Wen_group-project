package com.example.springboot.dao;

import com.example.springboot.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ResourceDao extends Mapper<Resource> {


    @Select("select * from resource where `id` = #{id}")
    Resource findById(@Param("id")Integer id);

    List<Resource> findBySearch(@Param("search") Resource search);

}