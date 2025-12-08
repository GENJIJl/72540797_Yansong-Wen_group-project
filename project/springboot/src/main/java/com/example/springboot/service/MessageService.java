package com.example.springboot.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.AdminDao;
import com.example.springboot.dao.ManagerDao;
import com.example.springboot.dao.MessageDao;
import com.example.springboot.dao.TeacherDao;
import com.example.springboot.entity.*;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageDao messageDao;
    @Resource
    private AdminDao adminDao;
    @Resource
    private TeacherDao teacherDao;
    @Resource
    private ManagerDao managerDao;

    public Message add(Message message) {
        message.setTime(DateUtil.now());
        messageDao.insertSelective(message);
        return message;
    }

    public void delete(Integer id) {
        messageDao.deleteByPrimaryKey(id);
    }

    public Message findById(Integer id) {
        return messageDao.selectByPrimaryKey(id);
    }

    public List<Message> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        List<Message> list = messageDao.findByParentId(0);
        for (Message message : list) {
            setData(message);
            List<Message> children = messageDao.findByParentId(message.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                for (Message child : children) {
                    setData(child);
                }
                message.setChildren(children);
            }
        }
        return list;
    }

    private void setData(Message message) {
        if (1 == message.getRole()) {
            Admin admin = adminDao.selectByPrimaryKey(message.getUserId());
            message.setUserName(admin.getUserName());
            message.setAvatar(admin.getAvatar());
        }
        if (2 == message.getRole()) {
            Teacher teacher =teacherDao.selectByPrimaryKey(message.getUserId());
            message.setUserName(teacher.getUserName());
            message.setAvatar(teacher.getAvatar());
        }
        if (3 == message.getRole()) {
            Manager manager =managerDao.selectByPrimaryKey(message.getUserId());
            message.setUserName(manager.getUserName());
            message.setAvatar(manager.getAvatar());
        }
    }

    public PageInfo<Message> findPage(Message search, Integer pageNum, Integer pageSize) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Message> all = findByCondition(search, currentUser);
        for (Message message : all) {
            Integer role = message.getRole();
            if (1 == role) {
                Admin admin = adminDao.selectByPrimaryKey(message.getUserId());
                message.setUserName(admin.getUserName());
            } else if(2 == role){
                Teacher teacher =teacherDao.selectByPrimaryKey(message.getUserId());
                message.setUserName(teacher.getUserName());
            } else if(3 == role){
                Manager manager =managerDao.selectByPrimaryKey(message.getUserId());
                message.setUserName(manager.getUserName());
            }
        }
        return PageInfo.of(all);
    }

    public List<Message> findByCondition(Message search, Account currentUser) {
    // 管理员可以查看所有留言
        if (3 == currentUser.getRole()) {
            return messageDao.findBySearch(search, null, null);
        } else {
            // 非管理员只能查看自己的留言
            return messageDao.findBySearch(search, currentUser.getId(), currentUser.getRole());
        }
    }
}
