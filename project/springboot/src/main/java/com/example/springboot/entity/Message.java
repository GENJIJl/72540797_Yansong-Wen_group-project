package com.example.springboot.entity;

import javax.persistence.*;
import java.util.List;


/**
 * 留言
 */
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 留言内容 */
    @Column(name = "content")
    private String content;
    /** 留言时间 */
    @Column(name = "time")
    private String time;
    /** 留言人id */
    @Column(name = "userId")
    private Integer userId;
    /** 留言人角色 */
    @Column(name = "role")
    private Integer role;
    /** 父ID */
    @Column(name = "parentId")
    private Integer parentId;
    @Transient
    private String userName;
    @Transient
    private List<Message> children;
    @Transient
    private String avatar;
    @Transient
    private String reply;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Message> getChildren() {
        return children;
    }

    public void setChildren(List<Message> children) {
        this.children = children;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}