package com.example.springboot.common.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.Account;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ManagerService;
import com.example.springboot.service.TeacherService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class JwtTokenUtils {

    private static AdminService staticAdminService;
    private static TeacherService staticTeacherService;
    private static ManagerService staticManagerService;  // 新增

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private ManagerService managerService;

    @PostConstruct
    public void setServices() {  // 重命名方法
        staticAdminService = adminService;
        staticTeacherService = teacherService;
        staticManagerService = managerService;
    }

    /**
     * 生成token
     */
    public static String genToken(String userRole, String password) {
        return JWT.create().withAudience(userRole) // 将 userId-role 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(password)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                log.error("获取当前登录的token失败， token: {}", token);
                return null;
            }
            // 解析token，获取用户的id
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];
            Account user = null;
            if ("1".equals(role)) {
                user = staticAdminService.findById(Integer.valueOf(userId));
            }
            if ("2".equals(role)) {
                user = staticTeacherService.findById(Integer.valueOf(userId));
            }
            if ("3".equals(role)) {
                user = staticManagerService.findById(Integer.valueOf(userId));
            }

            return user;
        } catch (Exception e) {
            log.error("获取当前登录的管理员信息失败, token={}", token,  e);
            return null;
        }
    }
}