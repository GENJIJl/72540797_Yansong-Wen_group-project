package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.CourseResource;
import com.example.springboot.service.CourseResourceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/courseResource")
public class CourseResourceController {

    @Resource
    private CourseResourceService courseResourceService;

    // 获取所有课程资源数据
    @GetMapping("/alldata")
    public Result getAllData() {
        return Result.success(courseResourceService.GetAll());
    }

    // 新增课程资源接口
    @PostMapping
    public Result addResource(@RequestBody CourseResource courseResource) {
        if (courseResource.getId() == null) {
            courseResourceService.add(courseResource);
        }else{
            courseResourceService.update(courseResource);
        }
        return Result.success();
    }




    // 根据ID删除课程资源
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseResourceService.delete(id);
        return Result.success();
    }

    // 分页查询课程资源
    @PostMapping("/page")
    public Result findPage(@RequestBody CourseResource search,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(courseResourceService.findPage(search, pageNum, pageSize));
    }

    // 编辑课程资源接口
    @PostMapping("/edit")
    public Result editResource(@RequestBody CourseResource courseResource) {
        return Result.success(courseResourceService.courseResourceEdit(courseResource));
    }


    @PostMapping("/download/{id}")
    public Result recordDownload(@PathVariable Integer id) {
        try {
            courseResourceService.increaseDownloadCount(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error( "下载记录失败：" + e.getMessage());
        }
    }

    @PostMapping("/like/{id}")
    public Result recordLike(@PathVariable Integer id) {
        try {
            courseResourceService.increaseLikeCount(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("点赞失败：" + e.getMessage());
        }
    }

}