package com.example.springboot.service;

//收藏service层
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.CollectDao;
import com.example.springboot.dao.CourseResourceDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.CourseResource;
import com.example.springboot.exception.CustomException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CollectService {

    @Resource
    private CollectDao collectDao;

    @Resource
    private CourseResourceDao courseResourceDao;

    /**
     * 保存收藏记录
     */
    @Transactional
    public void save(Collect collect) {
        // 获取当前登录用户
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }

        // 如果没有传入用户ID，则使用当前登录用户ID
        if (collect.getAdminId() == null) {
            collect.setAdminId(user.getId());
        }

        // 检查课程资源是否存在
        CourseResource resource = courseResourceDao.findById(collect.getCourseresourceId());
        if (resource == null) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "课程资源不存在");
        }

        // 检查是否已经收藏过
        if (checkCollectionStatus(collect.getCourseresourceId(), collect.getAdminId())) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "该资源已收藏，请勿重复操作");
        }

        // 设置创建时间
        if (ObjectUtil.isEmpty(collect.getCreatetime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            collect.setCreatetime(LocalDateTime.now().format(formatter));
        }

        // 如果没有指定章节，从资源的category中提取
        if (collect.getChapterId() == null) {
            try {
                Integer category = Integer.parseInt(resource.getCategory());
                collect.setChapterId(category);
            } catch (NumberFormatException e) {
                collect.setChapterId(0); // 默认为0
            }
        }

        // 保存收藏记录
        collectDao.insert(collect);

        // 更新资源收藏数
        updateResourceCollectionCount(collect.getCourseresourceId(), 1);
    }

    /**
     * 根据资源ID和用户ID删除收藏记录
     */
    @Transactional
    public void deleteByResourceAndUser(Integer courseresourceId, Integer adminId) {
        // 获取当前登录用户
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }

        // 如果没有传入用户ID，则使用当前登录用户ID
        if (adminId == null) {
            adminId = user.getId();
        }

        // 验证操作权限（普通用户只能删除自己的收藏）
        if (!user.getRole().equals("管理员") && !user.getId().equals(adminId)) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "无权操作其他用户的收藏");
        }

        // 先检查是否存在
        if (checkCollectionStatus(courseresourceId, adminId)) {
            collectDao.deleteByResourceAndUser(courseresourceId, adminId);

            // 减少资源收藏数
            updateResourceCollectionCount(courseresourceId, -1);
        } else {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "未找到该收藏记录");
        }
    }

    /**
     * 根据ID删除收藏记录
     */
    public void deleteById(Integer id) {
        // 获取当前登录用户
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }

        // 查询收藏记录以获取资源ID
        Collect collect = collectDao.selectByPrimaryKey(id);
        if (collect == null) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "未找到该收藏记录");
        }

        // 验证操作权限（普通用户只能删除自己的收藏）
        if (!user.getRole().equals("管理员") && !user.getId().equals(collect.getAdminId())) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "无权操作其他用户的收藏");
        }

        // 删除收藏记录
        collectDao.deleteByPrimaryKey(id);

        // 减少资源收藏数
        updateResourceCollectionCount(collect.getCourseresourceId(), -1);
    }

    /**
     * 根据用户ID查询所有收藏
     */
    public List<Collect> findByUserId(Integer adminId) {
        // 获取当前登录用户
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }

        // 如果没有传入用户ID，则使用当前登录用户ID
        if (adminId == null) {
            adminId = user.getId();
        }

        // 验证操作权限（普通用户只能查看自己的收藏）
        if (!user.getRole().equals("管理员") && !user.getId().equals(adminId)) {
            throw new CustomException(ResultCode.PARAM_ERROR.msg, "无权查看其他用户的收藏");
        }

        return collectDao.findByUserId(adminId);
    }

    /**
     * 检查用户是否已收藏某资源
     */
    public boolean checkCollectionStatus(Integer courseresourceId, Integer adminId) {
        if (courseresourceId == null || adminId == null) {
            return false;
        }
        return collectDao.countByResourceAndUser(courseresourceId, adminId) > 0;
    }

    /**
     * 更新资源的收藏计数
     */
    private void updateResourceCollectionCount(Integer resourceId, int delta) {
        CourseResource resource = courseResourceDao.findById(resourceId);
        if (resource != null) {
            Integer currentCount = resource.getCollectcount();
            if (currentCount == null) {
                currentCount = 0;
            }

            // 确保计数不会小于0
            int newCount = Math.max(0, currentCount + delta);

            CourseResource update = new CourseResource();
            update.setId(resourceId);
            update.setCollectcount(newCount);
            courseResourceDao.updateByPrimaryKeySelective(update);
        }
    }
}