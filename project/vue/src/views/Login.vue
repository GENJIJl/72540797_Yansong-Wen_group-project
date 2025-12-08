<template>
  <div class="login-container">
    <div style="display: flex">

      <div class="decoration">
        <img src="@/assets/logo2.png" style="border-radius: 10px ;opacity:0.85;width: 300px ;height: 300px;margin-left: 40px">
        <div id="title">Programming Platform User Login Page</div>

      </div>

      <el-form>
        <el-form-item class="form-box" style="padding-top: 40px;border-radius: 10px;">
          <el-form-item>
            <el-input style="margin-bottom: 15px;" size="large" prefix-icon="el-icon-s-custom" placeholder="Please enter username" v-model="form.userName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="margin-bottom: 15px" size="large" show-password prefix-icon="el-icon-key" placeholder="Please enter password" v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 100%;margin-bottom: 15px;background-color: rgba(255,255,255,0.1) !important;" size="large"  placeholder="Please select your role" v-model="form.role">
              <el-option value="1" label="Student"></el-option>
              <el-option value="2" label="Teacher"></el-option>
              <el-option value="3" label="Administrator"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style=" border-radius:10px  ;width: 100%;height: 50px;background-color: rgb(239, 157, 72);border-color: rgb(239, 157, 72);color:white"size="large" @click="login">Login</el-button>
          </el-form-item>
        </el-form-item>

        <el-form-item class="zhuce" style="background-color: rgb(255, 255, 255,0.1); border-radius: 10px; backdrop-filter: blur(10px);
  box-shadow: 0 10px 25px rgb(0,0,0,0.1);display: flex; justify-content: center;align-items: center">
          <el-form-item class="regBox">
            <a href="javascript:void(0)" @click="navRegister"  style="text-indent: 12px;font-size:large;color:white !important;">Register</a><span class="el-icon-right" style="color:white !important;"></span>
          </el-form-item>

        </el-form-item>

      </el-form>
    </div>
  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: {}
    }
  },



  methods: {

    login() {
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
      if (!this.form.role) {
        this.$notify.error({
          title: "Error",
          message: "Please select role",
        });
        return;
      }



        request.post('/account/login', this.form).then(res => {
          if(res.code === '0') {
            let user = res.data.user;
            let token = res.data.token;
            localStorage.setItem("token",token);
            localStorage.setItem("user",JSON.stringify(user))
            this.$notify.success("Login successful!");
            const role = Number(user.role || this.form.role);
            if (role === 1) {
              this.$router.push("/student/resources")
            } else {
            this.$router.push("/manager/home")
            }
          } else {
            this.$notify.error(res.msg);
          }
        });
    },
    navRegister() {
      this.$router.push("/register")
    }
  }
}

</script>


<style scoped>


.login-container {
  height: 100vh;
  overflow: hidden;
  background-color: orange;
  background-image:
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(241, 113, 51,0.8),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(255, 255, 255,0.5),rgba(26, 161, 74,0)),
      radial-gradient(closest-side,rgba(26, 161, 74,1),rgba(26, 161, 74,0));
  background-size:
      90vmax 90vmax,
      120vmax 80vmax,
      90vmax 90vmax,
      180vmax 100vmax,
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
  animation: 13s movement linear infinite both;
  -webkit-animation: 13s movement linear infinite both;
}

@keyframes movement {
  0%, 100%{
    background-size:
        130vmax 130vmax,
        200vmax 200vmax,
        90vmax 90vmax,
        80vmax 80vmax,
        90vmax 90vmax;

    background-position:
        -80vmax -80vmax,
        160vmax -30vmax,
        10vmax 10vmax,
        -30vmax -10vmax,
        50vmax 50vmax;
  }
  25%{
    background-size:
        100vmax 100vmax,
        300vmax 300vmax,
        100vmax 100vmax,
        90vmax 90vmax,
        60vmax 60vmax;

    background-position:
        -60vmax -120vmax,
        10vmax -100vmax,
        10vmax 40vmax,
        -40vmax -20vmax,
        40vmax 60vmax;
  }
}

.form-box {
  width: 300px;
  padding: 20px 40px;
  border-radius: 10px;
  background-color: rgb(255, 255, 255,0.1);
  backdrop-filter: blur(10px);
  box-shadow: 0 10px 25px rgb(0,0,0,0.1);
  opacity: 1;

}

 .decoration{

  margin-right:15px;
}

/deep/ .el-input__inner {
  border-color:  white!important;
  border-radius: 10px;
  background-color: rgb(0,0,0,0) !important;
  color: white;
  box-shadow: 0 0 10px rgba(255,255,255,0.6) !important;
}

/deep/ .el-input__icon {
  color: white !important;
}

/deep/ .el-form-item {
  margin-bottom: 5px;
}

/deep/ .el-input__inner::placeholder {
  color: white;
  opacity: 1;
  font-size: large;

}

/deep/ .el-button {
  font-size: 18px !important;
  font-weight: bolder;
}
#title{
  margin-top: 10px;
  margin-bottom: 55px;
  font-size: 30px;
  color: #ffffff;
  text-shadow:-2px 0 rgb(255,255,223,0.1),		/* 向左阴影 */
  0 -2px rgb(255,255,223,0.1),				/* 向上阴影 */
  2px 0 rgb(255,255,223,0.1),					/* 向右阴影 */
  0 2px rgb(255,255,223,0.1);
}

.el-form-item__content{
  font-size:50px !important;
}

.el-form-item {
  margin-bottom: 5px;
}



 .el-input__icon {
  color: #666;
}

a {
  text-decoration: none;
  color: rgb(239, 157, 72);
  font-weight: bold;

}
.regBox {
  color: rgb(239, 157, 72);
  font-size: 28px;
  margin-bottom: 10px;
  margin-top: 10px;
  text-align: right;

}
</style>