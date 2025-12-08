<template>
  <div id="app">
    <el-container style="height: 500px;background-color: white !important; ">
    <el-aside width="200px">
      <div style="height:240px; width:90%;justify-content: center;align-items: center ">
        <img src="../assets/cau_logo.jpg" alt="" style="height: 180px;width:90%;margin-left: 10%;margin-bottom: 5%;margin-top: 5%;border-radius: 10px;border-color: black" index="/manager/home">

      </div>
      <el-menu class="el-menu" :default-active="$route.path" router>

        <el-submenu index="1">
          <template slot="title"><i class="el-icon-s-home" style="color: rgb(26, 161, 74)"></i>System Home</template>
          <el-menu-item-group>
            <el-menu-item index="/manager/home">Recommendations</el-menu-item>
          </el-menu-item-group>
        </el-submenu>


        <el-submenu  index="2">
          <template slot="title"><i class="el-icon-user-solid" style="color: rgb(26, 161, 74)"></i>User Management</template>
          <el-menu-item-group>
            <el-menu-item v-if="user.role == 2 || this.user.role === 3" index="/manager/admin">Student Management</el-menu-item>
            <el-menu-item v-if="user.role == 3" index="/manager/teacher" @click="checkRole">Teacher Management</el-menu-item>
            <el-menu-item v-if="user.role == 3" index="/manager/grade">Class Management</el-menu-item>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu index="3">
          <template slot="title"><i class="el-icon-s-management" style="color: rgb(26, 161, 74)"></i>Website Data</template>
          <el-menu-item-group>
            <el-menu-item v-if="user.role == 2 || this.user.role === 3" index="/manager/courseResource">Course Resource Management</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <el-menu-item v-if="user.role === 3" index="/manager/message">Message Management</el-menu-item>
          </el-menu-item-group>
        </el-submenu>

        <el-submenu v-if="user.role == 2" index="4">
          <template slot="title"><i class="el-icon-s-tools" style="color: rgb(26, 161, 74)"></i>Data Analysis</template>
          <el-menu-item-group>
            <el-menu-item index="/manager/analysis">Course Usage Analysis Report</el-menu-item>
          </el-menu-item-group>
        </el-submenu>

      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <span style="color: rgb(26, 161, 74);text-align: center;margin-left: 15px;font-size:x-large;font-weight:1000">Programming Course Resource Intelligent Management System</span>
        <div id="right">
          <img :src="user.avatar" id="photo">
          <span id="userid">{{ user.userName }}</span>
          <el-dropdown>
            <i class="el-icon-more" style="margin-right: 15px;margin-top:18px;font-size:30px !important;color:  rgb(26, 161, 74);width: 30px;height: 30px"></i>
            <el-dropdown-menu slot="dropdown">

              <div @click="logout()"><el-dropdown-item>Logout</el-dropdown-item></div>

            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </el-header>

      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
  </div>
</template>


<script>
import request from "@/utils/request";


export default {
  name: "ManagerLayout",

  data () {
    return {
      // 定义User对象，从缓存里取值
      user: JSON.parse(localStorage.getItem("user") || {}),
    }
  },


  methods: {
// Logout method
    logout() {
      localStorage.removeItem("user");// Clear cache
      this.$router.push("/login");// Navigate to login page
    },
    checkRole() {
      if (this.user.role == 1 ) {
        this.$message.error('Insufficient permissions');
        throw new Error('Insufficient permissions');
      }
    }
  },




}



</script>

<style>

</style>