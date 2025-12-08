package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.CourseResource;
import com.example.springboot.service.CollaborativeFilteringService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Resource
    private CollaborativeFilteringService collaborativeFilteringService;

    /**
     * 获取个性化推荐列表
     * @param limit 推荐数量
     * @return 推荐列表
     */
    @GetMapping("/personalized")
    public Result getPersonalizedRecommendations(@RequestParam(defaultValue = "4") Integer limit) {
        // 获取当前登录用户
        Account currentUser = JwtTokenUtils.getCurrentUser();
        Integer userId = currentUser != null ? currentUser.getId() : null;

        // 获取推荐列表
        List<CourseResource> recommendations = collaborativeFilteringService.getRecommendations(userId, limit);

        return Result.success(recommendations);
    }

    /**
     * 获取全局热门推荐
     * @param limit 推荐数量
     * @return 热门推荐列表
     */
    @GetMapping("/popular")
    public Result getPopularRecommendations(@RequestParam(defaultValue = "4") Integer limit) {
        List<CourseResource> recommendations = collaborativeFilteringService.getGlobalRecommendations(limit);
        return Result.success(recommendations);
    }
}