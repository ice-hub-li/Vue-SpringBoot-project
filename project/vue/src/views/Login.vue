<template>
  <div class="wrapper">
    <div style="height: 60px; line-height: 60px;font-size: 20px;padding-left: 50px;color: white;
       background-color: rgba(0,0,0,0.2)">
      客多多
    </div>

    <div style="margin: 50px auto; background-color: white;width: 500px;border-radius: 10px;overflow: hidden">
      <el-tabs v-model="activeName" >

        <el-tab-pane  label="账户密码登录" name="first">
          <div style="margin: 20px auto; background-color: #fff; width: 300px; height:250px; padding: 20px; border-radius: 10px">

            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="username">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input></el-form-item>
              <el-form-item prop="password">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
              </el-form-item>

              <el-form-item prop="code"  >
<!--                <el-input type="text" v-model="formLogin.code" >-->
<!--                  <template slot="prepend">验证码</template>-->
<!--                  <template slot="append">-->
<!--                    <div class="login-code"  @click="refreshCode">-->
<!--                      <SIdentify :identifyCode="identifyCode"></SIdentify>-->
<!--                    </div>-->
<!--                  </template>-->
<!--                </el-input>-->
                <div style="display: flex" >
<!--                  前台VUE登录的显示的验证码-->
                  <el-input placeholder="请输入验证码" prefix-icon="el-icon-warning-outline" size="mid" v-model="user.code" @blur="blur" style="width: 200px"></el-input>
                  <span @click="refreshCode" style="cursor: pointer;flex: 1">
                        <SIdentify :identifyCode="identifyCode"></SIdentify>
                  </span>
                </div>
              </el-form-item>

              <el-form-item style="margin: 10px 0; text-align: right">
                <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
                <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
<!--                <el-button type="primary" size="small" autocomplete="off" @click="$router.push('/resetpassword')">忘记密码</el-button>-->
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane  label="邮箱登录" name="second">
          <div style="margin: 20px auto; background-color: #fff; width: 300px; height:250px; padding: 20px; border-radius: 10px">
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="email">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-message" v-model="user.email"></el-input></el-form-item>
              <el-form-item prop="code">
                <el-input size="medium" style="width: 160px" prefix-icon="el-icon-lock"  v-model="user.code"></el-input>
                <el-button type="primary" size="small" class="ml-5" @click="getEmailcode">获取验证码</el-button>
              </el-form-item>

<!--              <el-form-item prop="code"  >-->
<!--                <div style="display: flex" >-->
<!--                  <el-input placeholder="请输入验证码" prefix-icon="el-icon-warning-outline" size="mid" v-model="user.code" @blur="blur" style="width: 200px"></el-input>-->
<!--                  <span @click="refreshCode" style="cursor: pointer;flex: 1">-->
<!--                    <SIdentify :identifyCode="identifyCode"></SIdentify>-->
<!--&lt;!&ndash;                        <SIdentify :identifyCodesecond="identifyCodesecond"></SIdentify>&ndash;&gt;-->
<!--                  </span>-->
<!--                </div>-->
<!--              </el-form-item>-->

              <el-form-item style="margin: 10px 0; text-align: right">
                <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
                <el-button type="primary" size="small" autocomplete="off" @click="loginEmail">登录</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

      </el-tabs>
    </div>


<!--    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">-->
<!--      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>-->
<!--      <el-form :model="user" :rules="rules" ref="userForm">-->
<!--        <el-form-item prop="username">-->
<!--          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input></el-form-item>-->
<!--        <el-form-item prop="password">-->
<!--          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item style="margin: 10px 0; text-align: right">-->
<!--          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>-->
<!--          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--    </div>-->
  </div>
</template>

<script>


import {setRouter} from "@/router";
import SIdentify from "../common/SIdentify";

export default {
  // name: "SIdentify",
  components: { SIdentify },
  name: "Login",
  data() {
    return {
      code:"",
      user:{},
      activeName: 'first',
      identifyCodes: '1234567890abcdefjhijklinopqrsduvwxyz',
      identifyCode: '',
      isSidentify:'',
      identifyCodeseconds: '1234567890abcdefjhijklinopqrsduvwxyz',
      identifyCodesecond: '',
      rules:{
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        // email: [
        //   { required: true, message: '请输入邮箱', trigger: 'blur' },

        // ],
        // code: [
        //     { required: true, message: "请输入验证码", trigger: "blur" }
        // ]
      }
    }

    },
  mounted () {
    // 初始化验证码
    this.identifyCode = ''
    this.makeCode(this.identifyCodes, 4)

  },
  methods:{
    getEmailcode(){
      if (!this.user.email){
        this.$message.warning("请输入邮箱账号")
      }
      //正则表达式
      if(!/^[a-zA-Z0-9][a-zA-Z0-9_]+\@[a-zA-Z0-9]+\.(com|cn|net|com.cn)$/i
          .test(this.user.email)){
        this.$message.warning("请输入正确的邮箱账号")
      }
      //methods函数方法中为JS的语法
      //前端页面跳转使用$router.push('/register')
      //向后台发送
      this.request.get("/user/email/"+this.user.email).then(res =>{
        if (res.code == '200'){
          this.$message.success("发送成功")
        }else {
          this.$message.error(res.msg)
        }
      })


    },
    blur(){
      if (this.user.code.toLowerCase() == this.identifyCode.toLowerCase()){
        this.$message.success("验证码输入正确")
      }else {
        this.$message.success("验证码输入错误,请刷新重新输入")
      }
      },
    createCode() {
      let code = "";
      const codeLength = 4; //验证码的长度
      const random = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
          'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); //随机数
      for(let i = 0; i < codeLength; i++) { //循环操作
        let index = Math.floor(Math.random() * 36); //取得随机数的索引（0~35）
        code += random[index]; //根据索引取得随机数加到code上
      }
      this.checkCode = code; //把code值赋给验证码
    },
    refreshCode () {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode (o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[this.randomNum(0, this.identifyCodes.length)]
      }
    },
    randomNum (min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },

    login(){
      this.$refs["userForm"].validate((valid) => {
        if(this.user.code.toLowerCase() == this.identifyCode.toLowerCase()){
          if (valid) {//表单合法
            this.request.post("/user/login", this.user).then(res => {
              if (res.code === '200'){
                localStorage.setItem("user",JSON.stringify(res.data)) //储存信息到用户浏览器
                localStorage.setItem("menus",JSON.stringify(res.data.menus)) //储存信息到用户浏览器
                //登录中就要去热恶人动态设置当前用户的路由
                setRouter()
                this.$router.push("/")
                this.$message.success("登录成功")
              }else {
                this.$message.error(res.msg)
              }
            })
          }else {
            this.$message.success("验证码输入错误,请刷新重新输入")
          }
        }

      });
    },
    loginEmail(){
      this.$refs["userForm"].validate((valid) => {
        if (valid) {//表单合法
          this.request.post("/user/loginEmail", this.user).then(res => {
            if (res.code === '200'){
              localStorage.setItem("user",JSON.stringify(res.data)) //储存信息到用户浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus)) //储存信息到用户浏览器
              //登录中就要去热恶人动态设置当前用户的路由
              setRouter()
              this.$router.push("/")
              this.$message.success("登录成功")
            }else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },


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
