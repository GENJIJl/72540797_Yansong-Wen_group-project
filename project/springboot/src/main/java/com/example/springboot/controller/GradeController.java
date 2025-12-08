package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Grade;
import com.example.springboot.service.GradeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/grade")
public class GradeController {
    @Resource
    GradeService gradeService;



    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(gradeService.GetAll());

    }
    /**
     * 新增班级接口
     */
    @PostMapping
    public Result gradeRegister(@RequestBody Grade grade){

        return Result.success(gradeService.gradeRegister(grade));

    }


    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        gradeService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Grade search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(gradeService.findPage(search, pageNum, pageSize));
    }


    /**
     * 编辑班级信息接口
     */
    @PostMapping("/edit")
    public Result gradeEdit(@RequestBody Grade grade){

        return Result.success(gradeService.gradeEdit(grade));

    }



}
