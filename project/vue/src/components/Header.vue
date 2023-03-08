<template>
  <div style="font-size: 12px;line-height: 60px; display: flex">
    <div style="flex: 1">
<!--      <span :class="collapseBtnClass" style="cursor: pointer;font-size: 18px"  @click="collapse"></span>-->


      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
<!--        <el-breadcrumb-item :to="'/'" >首页</el-breadcrumb-item>-->
        <el-breadcrumb-item style="font-size: 15px;margin-left: 1px">{{this.$route.name}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 150px; cursor:pointer; text-align: right" >
      <div style="display: inline-block">
        <img :src="user.avatarUrl" alt="" style="width: 35px;height: 35px; border-radius: 50%; position: relative; top: 10px;right: 5px">
        <span>{{user.username}}</span><i class="el-icon-setting" style="margin-left: 5px"></i>

      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">

        <el-dropdown-item style="font-size:14px; padding: 5px 0">
          <span style="text-decoration: none" @click="personinformation">个人信息</span>
<!--          <router-link to="/person" >个人信息</router-link>-->
        </el-dropdown-item>

        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
<!--          <router-link to="/password">修改密码</router-link>-->
          <span style="text-decoration: none" @click="changepassword">密码修改</span>
        </el-dropdown-item>

        <el-dropdown-item style="font-size:14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass: String,
    collapse: false,
    user:Object,
  },
  // 计算属性
  computed:{
    currentPathName(){
      return this.$store.state.currentPathName();//需要监听的数据
    }
  },
  data(){
    return{
      // 直接从外面接受
      // user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  methods:{
    collapse(){
      this.$emit("asideCollapse")
    },
    logout(){
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    },
    personinformation(){
      this.$router.push("/person")
      this.$message.success("进入个人信息页面成功")

    },
    changepassword(){
      this.$router.push("/password")
      this.$message.success("进入密码修改页面成功")

    }


  },
  // watch:{
  //   currentPathName(newVal,oldVal){
  //     console.log(newVal)
  //
  //   }
  // },
}
</script>

<style scoped>

</style>
