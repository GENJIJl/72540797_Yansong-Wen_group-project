package com.example.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "teacher")
public class Teacher extends Account {
    @Column(name = "gradeId")
    private Integer gradeId;


    /**
     * 班级名
     */
    @Transient
    private String gradeName;

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
//@Table(name = "teacher")
//public class Teacher {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "userName")
//    private String userName;
//
//    @Column(name = "idnum")
//    private String idnum;
//
//
//    @Column(name = "gender")
//    private String gender;
//
//    @Column(name = "age")
//    private String age;
//
//    @Column(name = "classId")
//    private String classId;
//
//    @Column(name = "birth")
//    private String birth;
//
//    @Column(name = "contactInfo")
//    private String contactInfo;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "password")
//    private String password;
//
//    @Column(name = "role")
//    private String role;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getidnum() {
//        return idnum;
//    }
//
//    public void setidnum(String idnum) {
//        this.idnum = idnum;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getAge() {
//        return age;
//    }
//
//    public void setAge(String age) {
//        this.age = age;
//    }
//
//    public String getClassId() {
//        return classId;
//    }
//
//    public void setClassId(String classId) {
//        this.classId = classId;
//    }
//
//    public String getBirth() {
//        return birth;
//    }
//
//    public void setBirth(String birth) {
//        this.birth = birth;
//    }
//
//    public String getContactInfo() {
//        return contactInfo;
//    }
//
//    public void setContactInfo(String contactInfo) {
//        this.contactInfo = contactInfo;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//}
