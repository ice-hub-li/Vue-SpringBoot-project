import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store"
import {includes} from "core-js/internals/array-includes";


Vue.use(VueRouter)


const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/resetpassword',
    name: 'ResetPassword',
    component: () => import('../views/ResetPassword.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//刷新页面会重置路由
export const setRouter = ()=>{
  const storeMenus = localStorage.getItem("menus");
  const storeUser = localStorage.getItem("user");
  if(storeUser){
    if (storeMenus){
      //拼装动态路由
      const manageRouter = {path:'/',name:'Manage',component:()=>import('../views/Manage.vue'),redirect:"/home",children:[
          {path:'person',name:'个人信息',component:()=>import('../views/Person.vue')},
          {path:'password',name:'修改密码',component:()=>import('../views/Password.vue')}
        ]}

      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if (item.path){
          let itemMenu = {path: item.path.replace("/",""),name:item.name,component:()=>import('../views/'+item.pagePath+'.vue')}
          manageRouter.children.push(itemMenu)
        }else if (item.children.length){
          item.children.forEach(item =>{
            if (item.path){
              let itemMenu = {path: item.path.replace("/",""),name:item.name,component:()=>import('../views/'+item.pagePath+'.vue')}
              manageRouter.children.push(itemMenu)
            }

          })
        }

      })
      const currentRouteNames = router.getRoutes().map(v => v.name)
      if (!currentRouteNames.includes('Manage')){
        router.addRoute(manageRouter)
      }
    }
  }
}

//再次设置一下路由
setRouter()
//
// router.addRoutes({
//   path: '/',
//       name: 'Manage',
//     component: ()=>import('../views/Manage.vue'),
//     redirect: "/home",
//     children:[
//   {path: 'home', name: '', component: ()=>import('../views/Home.vue')},
//   {path: 'user', name: '用户管理', component: ()=>import('../views/User.vue')},
//   {path: 'person', name: '个人信息', component: ()=>import('../views/Person.vue')},
//   {path: 'file', name: '文件管理', component: ()=>import('../views/File.vue')},
//   {path: 'role', name: '角色管理', component: ()=>import('../views/Role.vue')},
//   {path: 'menu', name: '菜单管理', component: ()=>import('../views/Menu.vue')},
//   ]
// })

//路由守卫
// router.beforeEach((to, from, next) => {
//   localStorage.setItem("currentPathName",to.name)  //设置当前路由名称
//   store.commit("setPath")  //触发store的数据更新,
//   next()  //放行路由，
// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  const  storeMenu = localStorage.getItem("menus")
  if (!to.matched.length){
    if (storeMenu){
      next("/404")
    }else {
      //跳回登录界面
      next("/login")
    }
  }
  next()
})

export default router
