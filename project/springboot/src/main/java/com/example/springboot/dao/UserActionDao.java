package com.example.springboot.dao;

import com.example.springboot.entity.UserAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserActionDao {

    List<UserAction> selectAll();

    List<UserAction> selectByUserId(@Param("userId") Integer userId);

    List<UserAction> selectByResourceId(@Param("resourceId") Integer resourceId);

    UserAction selectByUserIdAndResourceId(@Param("userId") Integer userId, @Param("resourceId") Integer resourceId);

    int insert(UserAction userAction);

    int update(UserAction userAction);

    int deleteById(@Param("id") Integer id);
}