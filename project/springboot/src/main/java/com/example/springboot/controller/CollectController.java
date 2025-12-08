package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Collect;
import com.example.springboot.service.CollectService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    /**
     * 添加收藏
     */
    @PostMapping
    public Result add(@RequestBody Collect collect) {
        try {
            collectService.save(collect);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping
    public Result delete(@RequestParam Integer courseresourceId, @RequestParam(required = false) Integer adminId) {
        try {
            collectService.deleteByResourceAndUser(courseresourceId, adminId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询用户收藏列表
     */
    @GetMapping("/user")
    public Result findByUser(@RequestParam(required = false) Integer adminId) {
        try {
            return Result.success(collectService.findByUserId(adminId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查收藏状态
     */
    @GetMapping("/check")
    public Result checkStatus(@RequestParam Integer courseresourceId, @RequestParam(required = false) Integer adminId) {
        try {
            boolean isCollected = collectService.checkCollectionStatus(courseresourceId, adminId);
            return Result.success(isCollected);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID删除收藏
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        try {
            // 这里假设你的service层有一个deleteById方法
            // 如果没有，你需要在CollectService中添加此方法
            collectService.deleteById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}