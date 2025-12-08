package com.example.springboot.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

@Service
public class FileRelocationService {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/file/";
    private static final String chapterPath = filePath + "chapter/";
    private static final String http = "http://localhost:9090/files/";

    /**
     * 确保文件存储在与分类匹配的章节文件夹中
     *
     * @param fileUrl 当前文件URL
     * @param category 分类（章节）
     * @return 更新后的文件URL
     */
    public String ensureFileInCorrectChapter(String fileUrl, String category) {
        // 如果URL或分类为空，直接返回原始URL
        if (ObjectUtil.isEmpty(fileUrl) || ObjectUtil.isEmpty(category) || !category.matches("\\d+")) {
            return fileUrl;
        }

        // 检查文件是否已在正确的章节文件夹中
        String expectedPathPattern = "/files/chapter/" + category + "/";
        if (fileUrl.contains(expectedPathPattern)) {
            return fileUrl;
        }

        try {

            String originalFileName;

            if (fileUrl.contains("/files/")) {
                originalFileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            } else {
                System.out.println("文件URL格式不正确: " + fileUrl);
                return fileUrl;
            }

            // 确定源文件物理路径
            String sourceFilePath;
            if (fileUrl.contains("/chapter/")) {

                String[] parts = fileUrl.split("/chapter/");
                if (parts.length > 1) {
                    String remainingPath = parts[1]; // 例如: 1/123456-file.pdf
                    String[] chapterParts = remainingPath.split("/", 2);
                    if (chapterParts.length > 1) {
                        String oldCategory = chapterParts[0]; // 例如: 1
                        sourceFilePath = chapterPath + oldCategory + "/" + originalFileName;
                    } else {
                        return fileUrl;
                    }
                } else {
                    return fileUrl;
                }
            } else {

                sourceFilePath = filePath + originalFileName;
            }


            String targetChapterDir = chapterPath + category + "/";
            String targetFilePath = targetChapterDir + originalFileName;


            if (!FileUtil.exist(targetChapterDir)) {
                FileUtil.mkdir(targetChapterDir);
            }


            if (FileUtil.exist(sourceFilePath)) {

                FileUtil.move(FileUtil.file(sourceFilePath), FileUtil.file(targetFilePath), true);
                System.out.println("文件已从 " + sourceFilePath + " 移动到 " + targetFilePath);


                String newFileUrl = http + "chapter/" + category + "/" + originalFileName;
                return newFileUrl;
            } else {
                System.out.println("源文件不存在: " + sourceFilePath);
                return fileUrl;
            }
        } catch (Exception e) {
            System.err.println("移动文件失败: " + e.getMessage());
            e.printStackTrace();
            return fileUrl;
        }
    }
}