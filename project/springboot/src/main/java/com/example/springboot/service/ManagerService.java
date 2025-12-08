package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.ManagerDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Manager;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Resource
    private ManagerDao managerDao;

    public List<Manager> getAll(){

        return managerDao.getAll();
    }



    //管理员登录
    public Manager managerLogin(Manager manager) {

//        1.拿到用户传来的用户名，先到数据库查看这个用户名是否存在,如果不存在抛出异常：“用户不存在”
        Manager dbManager = managerDao.findByName(manager.getUserName());
        if (ObjectUtil.isEmpty(dbManager)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
//        2.如果用户存在，则那用户输入的密码跟数据库找到的用户的密码比对，密码输入正确，允许登录
        String password = manager.getPassword(); //用户输入的密码
        String dbPassword = dbManager.getPassword(); // 数据库找到用户的密码

        if (!password.equalsIgnoreCase(dbPassword)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }

        return dbManager;

    }



    public Manager findById(Integer id) {
        return managerDao.findById(id);
    }

    public void delete(Integer id) {
        managerDao.deleteByPrimaryKey(id);
    }


    public Manager managerEdit(Manager manager) {

        String userName = manager.getUserName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(userName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        managerDao.updateByPrimaryKeySelective(manager);

        return manager;
    }


    public PageInfo<Manager> findPage(Manager search, Integer pageNum, Integer pageSize) {

        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Manager> all = findByCondition(search);

        return PageInfo.of(all);
    }


    public List<Manager> findByCondition(Manager search) {
        return managerDao.findBySearch(search);
    }

}