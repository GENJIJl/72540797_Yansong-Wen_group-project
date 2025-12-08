package com.example.springboot.service;

import com.example.springboot.dao.CourseResourceDao;
import com.example.springboot.dao.UserActionDao; // 假设有这个DAO来获取用户行为数据
import com.example.springboot.entity.CourseResource;
import com.example.springboot.entity.UserAction;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CollaborativeFilteringService {

    @Resource
    private CourseResourceDao courseResourceDao;

    @Resource
    private UserActionDao userActionDao; // 用户行为DAO

    // 权重配置：点赞和收藏的权重
    private static final double LIKE_WEIGHT = 1.0;
    private static final double COLLECT_WEIGHT = 1.5;

    /**
     * 基于物品的协同过滤算法，为指定用户生成推荐列表
     * @param userId 用户ID，如果为null则生成全局热门推荐
     * @param limit 推荐数量
     * @return 推荐的资源列表
     */
    public List<CourseResource> getRecommendations(Integer userId, int limit) {
        // 如果用户ID为空，返回全局热门推荐
        if (userId == null) {
            return getGlobalRecommendations(limit);
        }

        // 1. 获取所有课程资源
        List<CourseResource> allResources = courseResourceDao.selectAll();

        // 2. 构建用户-资源评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix = buildUserItemMatrix();

        // 3. 计算物品相似度矩阵
        Map<Integer, Map<Integer, Double>> itemSimilarityMatrix = calculateItemSimilarity(userItemMatrix);

        // 4. 获取用户已交互的资源
        Set<Integer> userInteractedItems = getUserInteractedItems(userId);

        // 5. 生成推荐
        Map<Integer, Double> recommendationScores = new HashMap<>();

        // 对于用户交互过的每个资源
        for (Integer interactedItemId : userInteractedItems) {
            // 获取用户对该资源的评分
            double userRating = getUserRatingForItem(userId, interactedItemId, userItemMatrix);

            // 找出与该资源相似的其他资源
            Map<Integer, Double> similarItems = itemSimilarityMatrix.getOrDefault(interactedItemId, new HashMap<>());

            // 计算推荐分数
            for (Map.Entry<Integer, Double> entry : similarItems.entrySet()) {
                Integer similarItemId = entry.getKey();
                Double similarity = entry.getValue();

                // 如果用户已经交互过这个资源，跳过
                if (userInteractedItems.contains(similarItemId)) {
                    continue;
                }

                // 计算推荐分数：用户评分 * 物品相似度
                double score = userRating * similarity;
                recommendationScores.put(similarItemId, recommendationScores.getOrDefault(similarItemId, 0.0) + score);
            }
        }

        // 如果推荐列表为空，返回全局热门推荐
        if (recommendationScores.isEmpty()) {
            return getGlobalRecommendations(limit);
        }

        // 6. 排序并返回前N个推荐
        List<Map.Entry<Integer, Double>> sortedRecommendations = new ArrayList<>(recommendationScores.entrySet());
        sortedRecommendations.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

        // 7. 构建推荐资源列表
        List<CourseResource> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, sortedRecommendations.size()); i++) {
            Integer resourceId = sortedRecommendations.get(i).getKey();
            CourseResource resource = findResourceById(allResources, resourceId);
            if (resource != null) {
                recommendations.add(resource);
            }
        }

        // 如果推荐数量不足，添加一些全局热门资源
        if (recommendations.size() < limit) {
            List<CourseResource> globalRecommendations = getGlobalRecommendations(limit - recommendations.size());
            // 过滤掉已经在推荐列表中的资源
            Set<Integer> recommendedIds = new HashSet<>();
            for (CourseResource resource : recommendations) {
                recommendedIds.add(resource.getId());
            }
            for (CourseResource resource : globalRecommendations) {
                if (!recommendedIds.contains(resource.getId())) {
                    recommendations.add(resource);
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return recommendations;
    }

    /**
     * 获取全局热门推荐
     * @param limit 推荐数量
     * @return 热门资源列表
     */
    public List<CourseResource> getGlobalRecommendations(int limit) {
        // 获取所有资源
        List<CourseResource> allResources = courseResourceDao.selectAll();

        // 根据点赞数和收藏数计算热度分数
        allResources.sort((a, b) -> {
            int likeA = a.getLikecount() != null ? a.getLikecount() : 0;
            int collectA = a.getCollectcount() != null ? a.getCollectcount() : 0;
            double scoreA = likeA * LIKE_WEIGHT + collectA * COLLECT_WEIGHT;

            int likeB = b.getLikecount() != null ? b.getLikecount() : 0;
            int collectB = b.getCollectcount() != null ? b.getCollectcount() : 0;
            double scoreB = likeB * LIKE_WEIGHT + collectB * COLLECT_WEIGHT;

            return Double.compare(scoreB, scoreA); // 降序排列
        });

        // 返回前limit个热门资源
        return allResources.subList(0, Math.min(limit, allResources.size()));
    }

    /**
     * 构建用户-资源评分矩阵
     * @return 用户-资源评分矩阵
     */
    private Map<Integer, Map<Integer, Double>> buildUserItemMatrix() {
        // 用户-资源评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix = new HashMap<>();

        // 获取所有用户行为数据
        List<UserAction> userActions = userActionDao.selectAll();

        // 构建矩阵
        for (UserAction action : userActions) {
            Integer userId = action.getUserId();
            Integer resourceId = action.getResourceId();
            Double rating = calculateUserRating(action);

            // 更新用户-资源评分矩阵
            userItemMatrix.putIfAbsent(userId, new HashMap<>());
            userItemMatrix.get(userId).put(resourceId, rating);
        }

        return userItemMatrix;
    }

    /**
     * 计算用户对资源的评分
     * @param action 用户行为
     * @return 评分
     */
    private Double calculateUserRating(UserAction action) {
        double rating = 0.0;

        // 如果用户点赞了，增加评分
        if (action.getLiked() != null && action.getLiked()) {
            rating += LIKE_WEIGHT;
        }

        // 如果用户收藏了，增加评分
        if (action.getCollected() != null && action.getCollected()) {
            rating += COLLECT_WEIGHT;
        }

        return rating;
    }

    /**
     * 计算物品相似度矩阵（基于余弦相似度）
     * @param userItemMatrix 用户-资源评分矩阵
     * @return 物品相似度矩阵
     */
    private Map<Integer, Map<Integer, Double>> calculateItemSimilarity(Map<Integer, Map<Integer, Double>> userItemMatrix) {
        // 物品相似度矩阵
        Map<Integer, Map<Integer, Double>> itemSimilarityMatrix = new HashMap<>();

        // 物品评分向量
        Map<Integer, Map<Integer, Double>> itemUserMatrix = new HashMap<>();

        // 转置用户-资源矩阵，得到资源-用户矩阵
        for (Map.Entry<Integer, Map<Integer, Double>> userEntry : userItemMatrix.entrySet()) {
            Integer userId = userEntry.getKey();
            Map<Integer, Double> itemRatings = userEntry.getValue();

            for (Map.Entry<Integer, Double> itemEntry : itemRatings.entrySet()) {
                Integer itemId = itemEntry.getKey();
                Double rating = itemEntry.getValue();

                itemUserMatrix.putIfAbsent(itemId, new HashMap<>());
                itemUserMatrix.get(itemId).put(userId, rating);
            }
        }

        // 计算物品之间的余弦相似度
        // 获取所有物品ID
        List<Integer> itemIds = new ArrayList<>(itemUserMatrix.keySet());

        // 计算每对物品之间的相似度
        for (int i = 0; i < itemIds.size(); i++) {
            Integer itemI = itemIds.get(i);
            Map<Integer, Double> usersI = itemUserMatrix.getOrDefault(itemI, new HashMap<>());

            // 初始化物品I的相似度向量
            itemSimilarityMatrix.putIfAbsent(itemI, new HashMap<>());

            for (int j = i + 1; j < itemIds.size(); j++) {
                Integer itemJ = itemIds.get(j);
                Map<Integer, Double> usersJ = itemUserMatrix.getOrDefault(itemJ, new HashMap<>());

                // 计算两个物品的余弦相似度
                double similarity = calculateCosineSimilarity(usersI, usersJ);

                // 相似度是对称的
                itemSimilarityMatrix.get(itemI).put(itemJ, similarity);

                // 为itemJ初始化相似度向量
                itemSimilarityMatrix.putIfAbsent(itemJ, new HashMap<>());
                itemSimilarityMatrix.get(itemJ).put(itemI, similarity);
            }
        }

        return itemSimilarityMatrix;
    }

    /**
     * 计算余弦相似度
     * @param vectorA 向量A
     * @param vectorB 向量B
     * @return 余弦相似度
     */
    private double calculateCosineSimilarity(Map<Integer, Double> vectorA, Map<Integer, Double> vectorB) {
        // 共同用户集合
        Set<Integer> commonUsers = new HashSet<>(vectorA.keySet());
        commonUsers.retainAll(vectorB.keySet());

        // 如果没有共同用户，相似度为0
        if (commonUsers.isEmpty()) {
            return 0.0;
        }

        // 计算点积
        double dotProduct = 0.0;
        for (Integer userId : commonUsers) {
            dotProduct += vectorA.get(userId) * vectorB.get(userId);
        }

        // 计算向量A的模
        double normA = 0.0;
        for (Double value : vectorA.values()) {
            normA += value * value;
        }
        normA = Math.sqrt(normA);

        // 计算向量B的模
        double normB = 0.0;
        for (Double value : vectorB.values()) {
            normB += value * value;
        }
        normB = Math.sqrt(normB);

        // 计算余弦相似度
        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (normA * normB);
    }

    /**
     * 获取用户已交互的资源ID集合
     * @param userId 用户ID
     * @return 资源ID集合
     */
    private Set<Integer> getUserInteractedItems(Integer userId) {
        // 获取用户的所有行为
        List<UserAction> userActions = userActionDao.selectByUserId(userId);

        // 提取交互过的资源ID
        Set<Integer> interactedItems = new HashSet<>();
        for (UserAction action : userActions) {
            interactedItems.add(action.getResourceId());
        }

        return interactedItems;
    }

    /**
     * 获取用户对某个资源的评分
     * @param userId 用户ID
     * @param itemId 资源ID
     * @param userItemMatrix 用户-资源评分矩阵
     * @return 评分
     */
    private double getUserRatingForItem(Integer userId, Integer itemId, Map<Integer, Map<Integer, Double>> userItemMatrix) {
        Map<Integer, Double> userRatings = userItemMatrix.getOrDefault(userId, new HashMap<>());
        return userRatings.getOrDefault(itemId, 0.0);
    }

    /**
     * 根据ID查找资源
     * @param resources 资源列表
     * @param resourceId 资源ID
     * @return 资源对象，如果不存在则返回null
     */
    private CourseResource findResourceById(List<CourseResource> resources, Integer resourceId) {
        for (CourseResource resource : resources) {
            if (resource.getId().equals(resourceId)) {
                return resource;
            }
        }
        return null;
    }
}