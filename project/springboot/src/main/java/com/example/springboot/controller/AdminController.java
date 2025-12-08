//接收来自前端的请求，调用 Service 层处理业务逻辑，并返回结果。
//是 Web 应用程序中的入口。

package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.service.AdminService;
import com.example.springboot.entity.Admin;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(adminService.getAll());

    }

//    //学生注册接口
//    @PostMapping("/register")
//    public Result adminRegister(@RequestBody Admin admin){
//        return Result.success(adminService.adminRegister(admin));
//    }
//
//
//    //学生登录接口
//
//
//    @PostMapping("/login")
//    public Result adminLogin(@RequestBody Admin admin){
//
//        return Result.success(adminService.adminLogin(admin));
//
//    }
    /**
     * 新增学生接口
     */
    @PostMapping
    public Result adminRegister(@RequestBody Admin admin){

        return Result.success(adminService.adminRegister(admin));


    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        adminService.delete(id);
        return Result.success();
    }


    /**
     * 编辑学生信息接口
     */
    @PostMapping("/edit")
    public Result adminEdit(@RequestBody Admin admin){

        return Result.success(adminService.adminEdit(admin));

    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Admin search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(adminService.findPage(search, pageNum, pageSize));
    }
}


