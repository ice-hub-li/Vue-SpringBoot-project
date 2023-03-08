<template>
  <div style="overflow: hidden; height: 100vh">

    <h2 class="m-0">抱歉，页面丢失请返回
      <a href="javascript:;" @click="login">登录页面或主页</a>
    </h2>
    <img src="../assets/407.png" alt="" style="width: 100%; height: 100%">
  </div>

<!--  <div  class="wrapper">-->

<!--      <el-button type="warning" size="small" autocomplete="off" >注册</el-button>-->
<!--      <el-button type="primary" size="small" autocomplete="off" >登录</el-button>-->
<!--&lt;!&ndash;      <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>&ndash;&gt;-->

<!--  </div>-->
</template>

<script>
export default {
  name: "NotFound",
  methods: {
    login() {
      const storeUser = localStorage.getItem("user");
      console.log(5555555555555555555555555)
      console.log(storeUser)
      if (storeUser){
        this.$router.push('/home')
      }
      if (!storeUser){
        this.$router.push('/login')
      }
    }
  },
}
</script>

<style>
.bgImg {
  background: url("../assets/404.png") no-repeat;
  background-size: 100% 100vh;
}
</style>
