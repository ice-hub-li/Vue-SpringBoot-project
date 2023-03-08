<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 450px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="输入用户名" @blur="verifyUserNameIsSole" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input></el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="确认密码" @blur="verifyPasswordEqualsConfirmpassword" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item prop="email">
        <el-input size="medium" placeholder="请输入邮箱"  style="margin: 10px 0" prefix-icon="el-icon-message" v-model="user.email"></el-input></el-form-item>
        <el-form-item prop="code">
          <el-input size="medium" placeholder="确输入验证码" style="width: 160px" prefix-icon="el-icon-lock"  v-model="user.code"></el-input>
          <el-button type="primary" size="small" class="ml-5" @click="getEmailcode">获取邮箱验证码</el-button>
        </el-form-item>

        <el-form-item  style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="register">点击注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>


export default {
  name: "Login",
  data() {
    return {
      // 花括号表示User对象
      user: {},
      rules:{
        username: [
          { required: true, message: '输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '输入密码', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认输入密码', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '输入邮箱', trigger: 'blur' },
        ],
        code: [
          { required: true, message: '输入验证码', trigger: 'blur' },
        ],
      }
    }
  },
  methods:{
    verifyPasswordEqualsConfirmpassword(){
      if (this.user.password != this.user.confirmPassword){
        this.$message.warning("两次输入的密码不同，请确认密码！！！")
      }
    },

    verifyUserNameIsSole(){
      this.request.get("/user/verifyUserName/"+this.user.username).then(res =>{
        if (res.code == '200'){
          // this.$message.warning("用户名已存在，请重新设置新的用户名！！！")
        }else {
          this.$message.error(res.msg)
        }
      })
    },

    getEmailcode(){
      if (!this.user.email){
        this.$message.warning("请输入邮箱账号")
      }
      //正则表达式
      if(!/^[a-zA-Z0-9][a-zA-Z0-9_]+\@[a-zA-Z0-9]+\.(com|cn|net|com.cn)$/i
          .test(this.user.email)){
        this.$message.warning("请输入正确的邮箱账号！！！")
      }
      //methods函数方法中为JS的语法
      //前端页面跳转使用$router.push('/register')
      //向后台发送
      this.request.get("/user/resetpassword/"+this.user.email).then(res =>{
        if (res.code == '200'){
          this.$message.success("发送成功,请注意查收！！！")
        }else {
          this.$message.error(res.msg)
        }
      })


    },


    register(){
      this.$refs["userForm"].validate((valid) => {
        if (valid) {//表单合法
          if (this.user.password != this.user.confirmPassword){
            this.$message.error("两次输入的密码不同")
            return false;
          }


          //查询输入的验证码是否是正确的
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200'){
              this.$message.success("注册成功，请返回登录")
            }else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>
