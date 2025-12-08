package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Teacher;
import com.example.springboot.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    TeacherService teacherService;

    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(teacherService.getAll());

    }


//    //教师注册接口
//    @PostMapping("/register")
//    public Result teacherRegister(@RequestBody Teacher teacher){
//        return Result.success(teacherService.teacherRegister(teacher));
//    }
//
//
//    //教师登录接口
//
//
//    @PostMapping("/login")
//    public Result teacherLogin(@RequestBody Teacher teacher){
//
//        return Result.success(teacherService.teacherLogin(teacher));
//
//    }

    /**
     * 新增教师接口
     */
    @PostMapping
    public Result teacherRegister(@RequestBody Teacher teacher){

        return Result.success(teacherService.teacherRegister(teacher));


    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        teacherService.delete(id);
        return Result.success();
    }


    /**
     * 编辑教师信息接口
     */
    @PostMapping("/edit")
    public Result teacherEdit(@RequestBody Teacher teacher){

        return Result.success(teacherService.teacherEdit(teacher));

    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Teacher search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(teacherService.findPage(search, pageNum, pageSize));
    }
}