package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.ResourceDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Resource;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    public List<Resource> GetAll(){

        return resourceDao.selectAll();
    }
    public Resource add(Resource resource) {
        String title = resource.getTitle();
        if (ObjectUtil.isEmpty(title)){
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }
        if (ObjectUtil.isEmpty(resource.getImg())) {
            resource.setImg("http://localhost:9090/files/defaultresource.jpg");
        }
        resourceDao.insertSelective(resource);
        return resource;
    }


    public Resource findById(Integer id) {
        return resourceDao.findById(id);
    }




    public void delete(Integer id) {
        resourceDao.deleteByPrimaryKey(id);
    }


    public PageInfo<Resource> findPage(Resource search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Resource> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<Resource> findByCondition(Resource search) {
        return resourceDao.findBySearch(search);
    }



    public Resource resourceEdit(Resource resource) {

        String resourceName = resource.getTitle();
        //     1.先校验标题是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(resourceName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        resourceDao.updateByPrimaryKeySelective(resource);

        return resource;
    }

}