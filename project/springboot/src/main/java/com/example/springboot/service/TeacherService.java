package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.TeacherDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Teacher;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Resource
    private TeacherDao teacherDao;

    public List<Teacher> getAll(){

        return teacherDao.getAll();
    }

    //用户注册
    public Teacher teacherRegister(Teacher teacher) {
        //1.获取用户输入的用户名，在数据库中查询该用户名是否存在，如果存在则抛出异常
        String teacherName = teacher.getUserName();//用户输入的用户名
        // 先拿到用户输入的用户名
        Teacher dbteacher = teacherDao.findByName(teacherName);
        if (ObjectUtil.isNotEmpty(dbteacher)){  //如果找到该用户抛出异常即使已存在
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(teacher.getAvatar())) {
            teacher.setAvatar("http://localhost:9090/files/default.jpg");
        }

        if (ObjectUtil.isEmpty(teacher.getPassword())) {
            teacher.setPassword("123456");
        }

        //2.新增用户注册信息到数据表
        teacherDao.insertSelective(teacher);


        return teacher;
    }

    //教师登录
    public Teacher teacherLogin(Teacher teacher) {

//        1.拿到用户传来的用户名，先到数据库查看这个用户名是否存在,如果不存在抛出异常：“用户不存在”
        Teacher dbTeacher = teacherDao.findByName(teacher.getUserName());
        if (ObjectUtil.isEmpty(dbTeacher)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
//        2.如果用户存在，则那用户输入的密码跟数据库找到的用户的密码比对，密码输入正确，允许登录
        String password = teacher.getPassword(); //用户输入的密码
        String dbPassword = dbTeacher.getPassword(); // 数据库找到用户的密码

        if (!password.equalsIgnoreCase(dbPassword)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }

        return dbTeacher;

    }



    public Teacher findById(Integer id) {
        return teacherDao.findById(id);
    }

    public void delete(Integer id) {
        teacherDao.deleteByPrimaryKey(id);
    }


    public Teacher teacherEdit(Teacher teacher) {

        String userName = teacher.getUserName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(userName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        teacherDao.updateByPrimaryKeySelective(teacher);

        return teacher;
    }

    // 分页查询和模糊查询的方法
    public PageInfo<Teacher> findPage(Teacher search, Integer pageNum, Integer pageSize) {
        //1.jwt方法获取当前用户，判断当前用户是否为登录状态
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> all = findByCondition(search);

        return PageInfo.of(all);
    }

    // 根据条件查询的方法
    public List<Teacher> findByCondition(Teacher search) {
        return teacherDao.findBySearch(search);
    }

}