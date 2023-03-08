<template >
  <el-container style="height: 100%"  >
    <el-aside :width="sideWith + 'px'" style="background-color: rgb(238, 241, 246);box-shadow: 2px 0 6px rgb(0 21 41 / 35%); "  >
<!--      组件传值得方法-->
      <Aside :is-collapse="isCollapse" :logo-text-show="logoTextShow"/>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #131212">
<!--        将manage的值传递给header-->
        <Header :collapse-btn-class="collapseBtnClass" :collapse="collapse" :user="user"/>
      </el-header>


      <el-main style="height: 100px">
<!--        表示当前页面的子路由会在router-view里边展示-->
        <router-view @refreshUser="getUser" />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";


export default {
  name: 'Manage',
  data(){
    return {
      // 给出默认值
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse:false,
      sideWith:200,
      logoTextShow:true,
      user:{}
    }
  },
  created() {
    //从后台获得最新的数据
    this.getUser()

  },
  components:{
    Aside,
    Header
  },
  methods:{
    collapse(){//点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse){//收缩
        this.sideWith = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow =false
      }else {//展开
        this.sideWith = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow=true

      }
    },
    getUser(){
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      //从后台获取得到数据
      return this.request.get("/user/username/"+username).then(res => {
        //重新赋值后台最新的user数据
        this.user = res.data

      })
    }

  }
}
</script>

<style>
.tableHeaderBg{
  background: #eee !important;
}
</style>
