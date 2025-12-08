<template>


  <div class="login-container">
    <div style="display: flex;flex-direction: column">
      <div class="decoration">
        <div id="title">Programming Course User Registration Page</div>
      </div>
      <el-form>
        <el-form-item class="form-box" style="padding-top: 40px;border-radius: 10px">
          <el-form-item >
            <el-input  style="margin-bottom: 15px; " size="large" prefix-icon="el-icon-s-custom" placeholder="Username" v-model="form.userName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-key" placeholder="Password" v-model="form.password"></el-input>
          </el-form-item>

          <el-form-item>
            <el-input style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-key" placeholder="Confirm Password" v-model="form.nmm"></el-input>
          </el-form-item>

            <el-form-item>
              <el-input  style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-aim" placeholder="Student ID/Teacher ID" v-model="form.idnum"></el-input>
            </el-form-item>

          <el-form-item>
            <el-input  style="margin-bottom: 15px " size="large" show-password prefix-icon="el-icon-aim" placeholder="Class" v-model="form.classId"></el-input>
          </el-form-item>

          <el-form-item>
            <el-select  style="width: 100%;margin-bottom: 15px" size="large"  placeholder="Gender" v-model="form.gender">
              <el-option value="1" label="Male"></el-option>
              <el-option value="2" label="Female"></el-option>
            </el-select>
          </el-form-item>


        <el-form-item>
          <el-date-picker  style="width: 100%;margin-bottom: 15px" size="large"  placeholder="Date of Birth" v-model="form.birth"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>

          <el-form-item >
            <el-input  style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-phone" placeholder="Contact Information" v-model="form.contactInfo  " ></el-input>
          </el-form-item>

          <el-form-item>
            <el-input  style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-message" placeholder="Email" v-model="form.email"></el-input>
          </el-form-item>

          <el-form-item>
            <el-select style="width: 100%;margin-bottom: 15px" size="large"  placeholder="Select Role" v-model="form.role">
              <el-option value="1" label="Student"></el-option>
              <el-option value="2" label="Teacher"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item >
            <el-button  style="width: 100%;border-radius:10px;height: 50px;background-color:rgb(239, 157, 72);border-color:rgb(239, 157, 72);color:rgba(255,255,255)" font-size="large" @click="register">Register</el-button>
          </el-form-item>
        </el-form-item>
        <el-form-item style="background-color:rgb(255, 255, 255,0.1);backdrop-filter: blur(10px);box-shadow: 0 10px 25px rgb(0,0,0,0.1); border-radius: 10px; display: flex; justify-content: center;align-items: center">
          <el-form-item class="regBox">
            <a href="javascript:void(0)" @click="navLogin" style="text-indent: 12px;">Login</a><span class="el-icon-right"></span>
          </el-form-item>

        </el-form-item>

      </el-form>
    </div>
  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {}
    }
  },
  created() {
  },

  methods: {


    register(){
      // Form data validation
      if (!this.form.userName) {
        this.$notify.error({
          title: "Error",
          message: "Please enter username",
        });
        return;
      }
      if (!this.form.password) {
        this.$notify.error({
          title: "Error",
          message: "Please enter password",
        });
        return;
      }
      if (this.form.password !== this.form.nmm) {
        this.$notify.error({
          title: "Error",
          message: "Passwords do not match",
        });
        return;
      }
      if (!this.form.idnum) {
        this.$notify.error({
          title: "Error",
          message: "Please enter Student ID/Teacher ID",
        });
        return;
      }
      if (!this.form.classId) {
        this.$notify.error({
          title: "Error",
          message: "Please enter class",
        });
        return;
      }
      if (!this.form.gender) {
        this.$notify.error({
          title: "Error",
          message: "Please select gender",
        });
        return;
      }
      if (!this.form.birth) {
        this.$notify.error({
          title: "Error",
          message: "Please enter date of birth",
        });
        return;
      }
      if (!this.form.contactInfo) {
        this.$notify.error({
          title: "Error",
          message: "Please enter contact information",
        });
        return;
      }
      if (!this.form.email) {
        this.$notify.error({
          title: "Error",
          message: "Please enter email",
        });
        return;
      }
      if (!this.form.role) {
        this.$notify.error({
          title: "Error",
          message: "Please select role",
        });
        return;
      }


        request.post('account/register',this.form).then(res =>{
          if(res.code === '0'){
            this.$notify.success({
              title: "Success",
              message: "Registration successful",
            });
            this.$router.push("/manager/admin")
          }else{
            this.$notify.error({
              title: "Error!!",
              message: res.msg || "Unknown error",
            });
          }
        })

      // if(this.form.role==1){
      //   request.post('account/register',this.form).then(res =>{
      //     if(res.code === '0'){
      //       this.$notify.success({
      //         title: "成功",
      //         message: "注册成功",
      //       });
      //       this.$router.push("/manager/admin")
      //     }else{
      //       this.$notify.error({
      //         title: "错误！！",
      //         message: res.msg || "未知错误",
      //       });
      //     }
      //   })
      // } else if(this.form.role==2){
      //   request.post('account/register',this.form).then(res =>{
      //     if(res.code === '0'){
      //       this.$notify.success({
      //         title: "成功",
      //         message: "注册成功",
      //       });
      //       this.$router.push("/manager/teacher")
      //     }else{
      //       this.$notify.error({
      //         title: "错误！！",
      //         message: res.msg || "未知错误",
      //       });
      //     }
      //   })
      // }


    },


    navLogin() {
      this.$router.push("/login")
    }

  }
}


</script>


<style >
.login-container {
  height: 100vh;
  overflow: hidden;
  background-color: orange;
  background-image:
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(255, 255, 255,0.4),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0));
  background-size:
      90vmax 90vmax,
      100vmax 80vmax,
      90vmax 90vmax,
      100vmax 100vmax,
      90vmax 90vmax;

  background-position:
      -80vmax -80vmax,
      60vmax -30vmax,
      10vmax 10vmax,
      -30vmax -10vmax,
      50vmax 50vmax;
  background-repeat:no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: 10s movement linear infinite;
}

@keyframes movement {
  0%, 100%{
    background-size:
        130vmax 130vmax,
        80vmax 80vmax,
        90vmax 90vmax,
        80vmax 80vmax,
        90vmax 90vmax;

    background-position:
        -80vmax -80vmax,
        1vmax -30vmax,
        10vmax 10vmax,
        -30vmax -10vmax,
        50vmax 50vmax;
  }
  25%{
    background-size:
        100vmax 100vmax,
        90vmax 90vmax,
        100vmax 100vmax,
        90vmax 90vmax,
        60vmax 60vmax;

    background-position:
        -60vmax -90vmax,
        50vmax -40vmax,
        10vmax 20vmax,
        -40vmax -20vmax,
        40vmax 60vmax;
  }
}


.decoration{

}


#title{
  margin-top: 10px;
  margin-bottom: 20px;
  font-size: 40px;
  color: #ffffff;
  text-shadow:-2px 0 rgb(255,255,223,0.1),		/* 向左阴影 */
  0 -2px rgb(255,255,223,0.1),				/* 向上阴影 */
  2px 0 rgb(255,255,223,0.1),					/* 向右阴影 */
  0 2px rgb(255,255,223,0.1);
}


.form-box {
  width: 400px;
  padding: 20px 40px;
  border-radius: 2px;
  background-color: rgb(255, 255, 255,0.1);
  backdrop-filter: blur(10px);
  box-shadow: 0 10px 25px rgb(0,0,0,0.1);
  opacity: 1;

}
.el-form-item {
  margin-bottom: 5px;
}
.el-input__inner {
  border-color:  white!important;
  border-radius: 10px;
  background-color: rgb(0,0,0,0) !important;
  color: white;
  box-shadow: 0 0 10px rgba(255,255,255,0.6) !important;
}

.el-input__icon {
  color: white !important;
}

a {
  text-decoration: none;
  color: rgb(255, 182, 93);
  font-weight: bold;
  font-size: large;
}
.regBox {
  color: rgb(255, 182, 93);
  font-size: large;
  margin-bottom: 10px;
  margin-top: 10px;
  text-align: right;
}

.el-input__inner::placeholder {
  color: white;
  opacity: 1;
  font-size: large;

}



</style>