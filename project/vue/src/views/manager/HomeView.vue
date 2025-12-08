<template>
  <div class="home-container">
    <!-- Header Banner -->
    <div class="banner">
      <div class="banner-content">
        <h1>Welcome to Programming Course Resource Intelligent Management System</h1>
        <p>Providing intelligent teaching resource management platform for teachers and students</p>
        <el-button type="success" size="medium" @click="navigateToLogin" style="background-color: rgb(26, 161, 74); border: none; margin-top: 20px;">Get Started</el-button>
      </div>
    </div>


    <div class="features-section">
      <h2>Backend System Features</h2>
      <div class="features-container">
        <div class="feature-card" @click="navigateTo('/manager/admin')">
          <i class="el-icon-user"></i>
          <h3>Student Management</h3>
          <p>Manage student information, class assignments and student records</p>
        </div>
        <div class="feature-card" @click="navigateTo('/manager/courseresource')">
          <i class="el-icon-reading"></i>
          <h3>Course Resources</h3>
          <p>Provide course materials, teaching videos and learning resources</p>
        </div>
        <div class="feature-card" @click="navigateTo('/manager/grade')">
          <i class="el-icon-office-building"></i>
          <h3>Class Management</h3>
          <p>Manage class information, class overview and student lists</p>
        </div>
        <div class="feature-card" @click="navigateTo('/manager/message')">
          <i class="el-icon-chat-line-square"></i>
          <h3>Messages</h3>
          <p>Teacher-student communication platform, feedback and notifications</p>
        </div>
      </div>
    </div>
    



    <div class="footer">
      <p>© 2025 Course Resource Management System </p>
      <div class="footer-links">
        <a href="#">About Us</a>
        <a href="#">Help</a>
        <a href="#">Contact</a>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'HomePage',
  data() {
    return {
      stats: {
        studentCount: 0,
        courseCount: 0,
        gradeCount: 0,
        messageCount: 0
      },

    }
  },
  created() {
    this.loadStatistics();
  },
  methods: {
    navigateToLogin() {
      this.$router.push('/front/index');
    },
    navigateTo(path) {
      this.$router.push(path);
    },

    loadStatistics() {


      this.stats = {
        studentCount: 256,
        courseCount: 42,
        gradeCount: 8,
        messageCount: 128
      };


    }
  }
}
</script>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.banner {
  height: 400px;
  background: linear-gradient(135deg, #1aa14a 0%, #0d6b31 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  padding: 0 20px;
  margin-bottom: 40px;
}

.banner-content {
  text-align: center;
  max-width: 800px;
}

.banner-content h1 {
  font-size: 36px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.features-section, .stats-section, .announcements-section {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.features-section h2, .stats-section h2, .announcements-section h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
  color: #333;
}

.features-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.feature-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 240px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.feature-card i {
  font-size: 48px;
  color: #1aa14a;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}

.stats-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-top: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 200px;
  margin: 10px;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #1aa14a;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: #666;
}

.announcements-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.announcement-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.announcement-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.announcement-date {
  color: #999;
  font-size: 14px;
}

.announcement-card p {
  margin: 0;
  color: #666;
  line-height: 1.5;
}

.footer {
  margin-top: 40px;
  padding: 20px;
  background-color: #333;
  color: white;
  text-align: center;
}

.footer p {
  margin-bottom: 10px;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.footer-links a {
  color: #ccc;
  text-decoration: none;
}

.footer-links a:hover {
  color: white;
  text-decoration: underline;
}

.home-link {
  position: fixed;
  top: 20px;
  right: 20px;
  background-color: #1aa14a;
  color: white;
  padding: 10px 15px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s;
  z-index: 1000;
}

.home-link:hover {
  background-color: #0d8638;
}
</style>