package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Teacher;
import com.example.springboot.entity.Manager;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ManagerService;
import com.example.springboot.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    AdminService adminService;

    @Resource
    TeacherService teacherService;

    @Resource
    ManagerService managerService;




    @PostMapping("/register")
    public Result accountRegister(@RequestBody Account account){
        Integer role = account.getRole();
        Account login = new Account(); // 定义一个Account类的login,用于返回给前端
        if ( 1 == role){ //学生注册
            Admin admin = new Admin();
            BeanUtils.copyProperties(account,admin);
            login = adminService.adminRegister(admin);
        }
        if ( 2 == role){ //教师注册
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(account,teacher);
            login = teacherService.teacherRegister(teacher);
        }
        return Result.success(login);
    }


    /**
     * 登录接口
     */

    @PostMapping("/login")
    public Result AcountLogin(@RequestBody Account account){
        Integer role = account.getRole();
        Account login = new Account(); // 定义一个Account类的login,用于返回给前端
        if ( 1 == role){
            Admin admin = new Admin(); //创建一个学生类admin
            BeanUtils.copyProperties(account,admin); //把父类的属性拷贝到admin
            login = adminService.adminLogin(admin); //调用服务层的登录逻辑

        }
        if ( 2 == role){
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(account,teacher);
            login = teacherService.teacherLogin(teacher);
        }

        if ( 3 == role){
            Manager manager = new Manager();
            BeanUtils.copyProperties(account,manager);
            login = managerService.managerLogin(manager);
        }


        String token = JwtTokenUtils.genToken(login.getId() + "-" + login.getRole(), login.getPassword());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", login);
        return Result.success(map);

    }


}
