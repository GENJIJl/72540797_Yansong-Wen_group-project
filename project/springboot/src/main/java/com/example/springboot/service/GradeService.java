package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.GradeDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Grade;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Resource
    private GradeDao gradeDao;

    public List<Grade> GetAll(){

        return gradeDao.selectAll();
    }
    public Grade gradeRegister(Grade grade) {
        // 插入数据库前做一下用户名唯一性校验，
        String gradeName = grade.getGradeName();
        Grade dbGrade = gradeDao.findByGradename(gradeName);
        if (ObjectUtil.isNotEmpty(dbGrade)){
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }

        gradeDao.insertSelective(grade);
        return grade;
    }


    public Grade findById(Integer id) {
        return gradeDao.findById(id);
    }




    public void delete(Integer id) {
        gradeDao.deleteByPrimaryKey(id);
    }


    public PageInfo<Grade> findPage(Grade search, Integer pageNum, Integer pageSize) {
        Account grade = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(grade)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Grade> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<Grade> findByCondition(Grade search) {
        return gradeDao.findBySearch(search);
    }



    public Grade gradeEdit(Grade grade) {

        String gradeName = grade.getGradeName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(gradeName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        gradeDao.updateByPrimaryKeySelective(grade);

        return grade;
    }

}
