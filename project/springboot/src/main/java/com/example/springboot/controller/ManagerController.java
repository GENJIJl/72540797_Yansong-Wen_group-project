package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Manager;
import com.example.springboot.service.ManagerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Resource
    ManagerService managerService;

    @GetMapping("/alldata")
    public Result getData(){
        return Result.success(managerService.getAll());
    }



    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        managerService.delete(id);
        return Result.success();
    }

    /**
     * 编辑管理员信息接口
     */
    @PostMapping("/edit")
    public Result managerEdit(@RequestBody Manager manager){
        return Result.success(managerService.managerEdit(manager));
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Manager search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(managerService.findPage(search, pageNum, pageSize));
    }
}