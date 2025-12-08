package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.URLEncoder;

/**
 *  文件上传接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/file/";
    // 章节目录路径
    private static final String chapterPath = filePath + "chapter/";
    private static final String http = "http://localhost:9090/files/";

    /**
     * 初始化文件存储目录结构
     */
    private void initDirectories() {
        // 确保主文件夹存在
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }

        // 确保章节文件夹存在
        if (!FileUtil.isDirectory(chapterPath)) {
            FileUtil.mkdir(chapterPath);
        }

        // 创建1-10章节文件夹（如果不存在）
        for (int i = 1; i <= 10; i++) {
            String chapterDir = chapterPath + i + "/";
            if (!FileUtil.isDirectory(chapterDir)) {
                FileUtil.mkdir(chapterDir);
            }
        }
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file, @RequestParam(required = false) String category) {
        synchronized (FileController.class) {
            initDirectories(); // 确保目录结构存在

            String flag = System.currentTimeMillis() + "";// 时间戳
            String fileName = file.getOriginalFilename();//上传的文件名
            String savePath;
            String returnUrl;

            // 检查是否指定了有效的章节分类
            if (StrUtil.isNotEmpty(category) && category.matches("\\d+")) {
                // 文件存储到对应章节文件夹
                savePath = chapterPath + category + "/";
                returnUrl = http + "chapter/" + category + "/" + flag + "-" + fileName;
            } else {
                // 如果没有指定章节或指定无效，存储到默认文件夹
                savePath = filePath;
                returnUrl = http + flag + "-" + fileName;
            }

            try {
                // 文件存储形式：时间戳-文件名
                FileUtil.writeBytes(file.getBytes(), savePath + flag + "-" + fileName);
                System.out.println(fileName + "--上传成功，保存到：" + savePath);
                Thread.sleep(1L);
                return Result.success(returnUrl);
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败: " + e.getMessage());
                return Result.error("文件上传失败：" + e.getMessage());
            }
        }
    }

    /**
     * 获取章节文件
     */
    @GetMapping("/chapter/{chapter}/{filename}")
    public void getChapterFile(@PathVariable String chapter, @PathVariable String filename, HttpServletResponse response) {
        String targetPath = chapterPath + chapter + "/";
        try {
            if (FileUtil.exist(targetPath + filename)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(targetPath + filename);
                OutputStream os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            } else {
                System.out.println("章节文件未找到: " + targetPath + filename);
                response.setStatus(404);
            }
        } catch (Exception e) {
            System.out.println("章节文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 获取默认路径文件
     */
    @GetMapping("/{filename}")
    public void getFile(@PathVariable String filename, HttpServletResponse response) {
        try {
            if (FileUtil.exist(filePath + filename)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + filename);
                OutputStream os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            } else {
                System.out.println("文件未找到: " + filePath + filename);
                response.setStatus(404);
            }
        } catch (Exception e) {
            System.out.println("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 删除章节文件
     */
    @DeleteMapping("/chapter/{chapter}/{filename}")
    public Result deleteChapterFile(@PathVariable String chapter, @PathVariable String filename) {
        String filePath = chapterPath + chapter + "/" + filename;
        if (FileUtil.exist(filePath)) {
            FileUtil.del(filePath);
            return Result.success("文件删除成功");
        }
        return Result.error("文件不存在");
    }

    /**
     * 删除默认路径文件
     */
    @DeleteMapping("/{filename}")
    public Result deleteFile(@PathVariable String filename) {
        String targetPath = filePath + filename;
        if (FileUtil.exist(targetPath)) {
            FileUtil.del(targetPath);
            return Result.success("文件删除成功");
        }
        return Result.error("文件不存在");
    }


}