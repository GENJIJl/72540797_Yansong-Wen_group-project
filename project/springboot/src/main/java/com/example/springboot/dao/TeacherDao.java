package com.example.springboot.dao;

import com.example.springboot.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TeacherDao extends Mapper<Teacher> {

    List<Teacher> getAll();

    @Select("select * from teacher where `userName` = #{teacherName}")
    Teacher findByName(@Param("teacherName") String teacherName);//如果存在返回该teachername

    @Select("select * from teacher where `id` = #{id}")
    Teacher findById(@Param("id")Integer id);

    List<Teacher> findBySearch(@Param("search") Teacher search);
}