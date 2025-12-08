package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.CourseResourceDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.CourseResource;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseResourceService {

    @Resource
    private CourseResourceDao courseResourceDao;
    @Resource
    private ClassifierService classifierService;
    @Resource
    private FileRelocationService fileRelocationService; // 新增

    public List<CourseResource> GetAll() {
        return courseResourceDao.selectAll();
    }

    public CourseResource add(CourseResource courseResource) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        if (currentUser.getRole() == 1) {
            throw new CustomException("403", "No permission to modify resources");
        }

        String title = courseResource.getTitle();
        if (ObjectUtil.isEmpty(title)) {
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }

        // 如果没有设置分类，则使用分类服务自动分配
        if (ObjectUtil.isEmpty(courseResource.getCategory())) {
            String category = classifierService.classifyByTitle(title);
            courseResource.setCategory(category);
        }

        // 设置默认封面图片
        if (ObjectUtil.isEmpty(courseResource.getImg())) {
            courseResource.setImg("http://localhost:9090/files/defaultresource.jpg");
        }

        // 初始化计数器
        if (courseResource.getLikecount() == null) {
            courseResource.setLikecount(0);
        }
        if (courseResource.getCollectcount() == null) {
            courseResource.setCollectcount(0);
        }

        // 新增：确保文件存储在正确的章节文件夹中
        if (!ObjectUtil.isEmpty(courseResource.getPath()) && !ObjectUtil.isEmpty(courseResource.getCategory())) {
            String updatedPath = fileRelocationService.ensureFileInCorrectChapter(
                    courseResource.getPath(), courseResource.getCategory());
            courseResource.setPath(updatedPath);
        }

        courseResourceDao.insertSelective(courseResource);
        return courseResource;
    }

    public CourseResource findById(Integer id) {
        return courseResourceDao.findById(id);
    }

    public void update(CourseResource courseResource) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        if (currentUser.getRole() == 1 && courseResource.getLikecount() == null) {
            throw new CustomException("403", "No permission to modify resources");
        }
        // 如果是点赞操作，获取当前点赞数并加1
        if (courseResource.getId() != null && courseResource.getLikecount() != null) {
            CourseResource existingResource = courseResourceDao.selectByPrimaryKey(courseResource.getId());
            if (existingResource != null) {
                courseResource.setLikecount(existingResource.getLikecount() + 1);
            }
        }
        courseResourceDao.updateByPrimaryKeySelective(courseResource);
    }


    public void increaseDownloadCount(Integer id) {
        CourseResource existingResource = courseResourceDao.selectByPrimaryKey(id);
        if (existingResource != null) {
            CourseResource updateResource = new CourseResource();
            updateResource.setId(id);
            updateResource.setDownloadcount(existingResource.getDownloadcount() + 1);
            courseResourceDao.updateByPrimaryKeySelective(updateResource);
        }
    }

    public void increaseLikeCount(Integer id) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        CourseResource existingResource = courseResourceDao.selectByPrimaryKey(id);
        if (existingResource != null) {
            CourseResource updateResource = new CourseResource();
            updateResource.setId(id);
            updateResource.setLikecount(existingResource.getLikecount() + 1);
            courseResourceDao.updateByPrimaryKeySelective(updateResource);
        }
    }

    public void delete(Integer id) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        if (currentUser.getRole() == 1) {
            throw new CustomException("403", "No permission to modify resources");
        }
        courseResourceDao.deleteByPrimaryKey(id);
    }

    public PageInfo<CourseResource> findPage(CourseResource search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CourseResource> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<CourseResource> findByCondition(CourseResource search) {
        return courseResourceDao.findBySearch(search);
    }

    public CourseResource courseResourceEdit(CourseResource courseResource) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        if (currentUser.getRole() == 1) {
            throw new CustomException("403", "No permission to modify resources");
        }
        String courseResourceName = courseResource.getTitle();
        // 先校验标题是不是为空
        if (ObjectUtil.isEmpty(courseResourceName)) {
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }

        // 获取当前资源信息
        CourseResource existingResource = courseResourceDao.selectByPrimaryKey(courseResource.getId());
        if (existingResource != null) {
            // 检查分类是否变更
            String oldCategory = existingResource.getCategory();
            String newCategory = courseResource.getCategory();

            //如果分类变更了，重新定位文件
            if (!ObjectUtil.isEmpty(newCategory) && !ObjectUtil.isEmpty(oldCategory) &&
                    !newCategory.equals(oldCategory) &&
                    !ObjectUtil.isEmpty(courseResource.getPath())) {

                // 使用文件重定位服务移动文件到正确的章节文件夹
                String updatedPath = fileRelocationService.ensureFileInCorrectChapter(
                        courseResource.getPath(), newCategory);
                courseResource.setPath(updatedPath);
            }
        }

        courseResourceDao.updateByPrimaryKeySelective(courseResource);
        return courseResource;
    }
}