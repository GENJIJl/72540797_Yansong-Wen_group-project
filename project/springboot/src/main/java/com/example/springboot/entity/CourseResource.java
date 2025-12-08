package com.example.springboot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courseResource")
public class CourseResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源标题
     */
    @Column(name = "title", length = 255)
    private String title;

    /**
     * 文件存储路径
     */
    @Column(name = "path", length = 255)
    private String path;

    /**
     * 文件类型
     */
    @Column(name = "type", length = 255)
    private String type;

    /**
     * 发布时间
     */
    @Column(name = "time")
    private LocalDateTime time;

    /**
     * 上传者id
     */
    @Column(name = "uploaderId", nullable = false)
    private Integer uploaderId;

    @Column(name = "likecount", nullable = false)
    private Integer likecount;

    @Column(name = "collectcount", nullable = false)
    private Integer collectcount;

    public Integer getDownloadcount() {
        return downloadcount;
    }

    public void setDownloadcount(Integer downloadcount) {
        this.downloadcount = downloadcount;
    }

    @Column(name = "downloadcount", nullable = false)
    private Integer downloadcount;

    /**
     * 封面图
     */
    @Column(name = "img")
    private String img;

    @Column(name = "category")
    private String category;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public Integer getUploaderId() { return uploaderId; }
    public void setUploaderId(Integer uploaderId) { this.uploaderId = uploaderId; }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(Integer collectcount) {
        this.collectcount = collectcount;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }
}