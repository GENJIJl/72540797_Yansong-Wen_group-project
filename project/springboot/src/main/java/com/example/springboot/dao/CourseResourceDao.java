package com.example.springboot.dao;

import com.example.springboot.entity.CourseResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CourseResourceDao extends Mapper<CourseResource> {


    @Select("select * from courseResource where `id` = #{id}")
    CourseResource findById(@Param("id")Integer id);

    List<CourseResource> findBySearch(@Param("search") CourseResource search);

}