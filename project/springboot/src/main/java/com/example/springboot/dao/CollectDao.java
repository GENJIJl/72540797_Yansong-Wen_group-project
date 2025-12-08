package com.example.springboot.dao;

//收藏dao层

import com.example.springboot.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectDao {

    /**
     * 插入收藏记录
     */
    int insert(Collect collect);

    /**
     * 根据主键查询收藏记录
     */
    Collect selectByPrimaryKey(Integer id);
    /**
     * 根据主键删除收藏记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据资源ID和用户ID删除收藏记录
     */
    int deleteByResourceAndUser(@Param("courseresourceId") Integer courseresourceId,
                                @Param("adminId") Integer adminId);

    /**
     * 根据用户ID查询所有收藏
     */
    List<Collect> findByUserId(Integer adminId);

    /**
     * 统计用户对特定资源的收藏数量
     */
    int countByResourceAndUser(@Param("courseresourceId") Integer courseresourceId,
                               @Param("adminId") Integer adminId);
}