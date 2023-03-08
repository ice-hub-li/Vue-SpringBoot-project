<template>
  <el-menu :default-openeds="opens" style="min-height: 100vh; overflow-x:hidden"
           background-color="rgb(18,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/荷包蛋.png" alt="" style="width: 45px;position: relative;top: 15px;margin-right: 5px">
      <b style="color: #42b983" v-show="logoTextShow">客多多</b>

    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{item.name}}</span>
        </el-menu-item>

      </div>
      <div v-else>
        <el-submenu :index="item.id + '' ">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{item.name}}</span>
          </template>
          <div v-for="subitem in item.children" :key="subitem.id">
            <el-menu-item :index="subitem.path">
              <i :class="subitem.icon"></i>
              <span slot="title">{{subitem.name}}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>




<script>
export default {
  name: "Aside",
  props:{
    // 传入的值需要进行定义
    isCollapse:Boolean,
    logoTextShow:Boolean,

  },
  data(){
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id+'') : [],


    }
  }
}
</script>

<style scoped>

</style>
