package com.example.springboot.dao;

import com.example.springboot.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface MessageDao extends Mapper<Message> {

    List<Message> findBySearch(@Param("search") Message search, @Param("userId") Integer userId, @Param("role") Integer role);

    @Select("select * from message where parentId = #{parentId} order by id desc")
    List<Message> findByParentId(@Param("parentId") Integer parentId);
}

