package com.example.springboot.entity;

import javax.persistence.*;

//收藏
@Table(name = "collect")
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "courseresourceId", nullable = false)
    private Integer courseresourceId;

    @Column(name = "adminId", nullable = false)
    private Integer adminId;

    @Column(name = "createtime", nullable = false)
    private String createtime;

    @Column(name = "chapterId", nullable = false)
    private Integer chapterId;

    @Transient
    private Integer userName;

    @Transient
    private Integer courseName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseresourceId() {
        return courseresourceId;
    }

    public void setCourseresourceId(Integer courseresourceId) {
        this.courseresourceId = courseresourceId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getUserName() {
        return userName;
    }

    public void setUserName(Integer userName) {
        this.userName = userName;
    }

    public Integer getCourseName() {
        return courseName;
    }

    public void setCourseName(Integer courseName) {
        this.courseName = courseName;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
}



