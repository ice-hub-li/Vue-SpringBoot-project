<template>
  <div>

    <div style="margin: 10px 0" >
      <el-input style="width: 200px" placeholder="请输入用户名搜索"  prefix-icon="el-icon-search"
                v-model:type="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱搜索"  prefix-icon="el-icon-message"
                v-model:type="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入电话搜索"  prefix-icon="el-icon-phone"
                v-model:type="phone"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd" >新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-info"
          icon-color="red"
          title="这是一段内容确定批量删除吗？"
          @confirm="delBatch">
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload :action="'http://' +serverIp+ ':9090/customer/import'" :show-file-list="false" accept=".xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入<i class="el-icon-download"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-upload2"></i></el-button>
    </div>


    <el-table :data="tableData" style="width: 100%" height="360" stripe :header-cell-class-name="tableHeaderBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column  prop="id" label="ID" width="180"></el-table-column>
      <el-table-column  prop="name" label="姓名" width="180"></el-table-column>
      <el-table-column prop="phone" label="电话" width="180"></el-table-column>
<!--      <el-table-column prop="level" label="会员等级"></el-table-column>-->
      <el-table-column prop="email" label="邮箱" width="150"></el-table-column>
      <el-table-column prop="balance" label="余额(￥)" width="150"></el-table-column>
      <el-table-column label="操作" fixed="right" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="handleDelete(scope.row.id)">
            <el-button type="danger" slot="reference">删除<i class="el-icon-delete"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <div class="block">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

    </div>

    <el-dialog title="客户信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form  label-width="60px" size="small">
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" :label-width="formLabelWidth">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>

<!--        <el-form-item label="会员等级" :label-width="formLabelWidth">-->
<!--          <el-select clearable v-model="form.role" placeholder="请选择" style="width: 100%">-->
<!--            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.name"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item >
          <el-input v-model="form.pid"  :value="1" disabled="True" ></el-input>
        </el-form-item>


        <el-form-item label="余额/￥" :label-width="formLabelWidth">
          <el-input v-model="form.balance" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Customer",
  data(){
    return{
      serverIp:serverIp,
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      name:'',
      level:'',
      email:'',
      phone:'',
      balance:'',
      form:{},
      dialogFormVisible:false,
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse:false,
      sideWith:200,
      logoTextShow:true,
      tableHeaderBg:'tableHeaderBg',
      multipleSelection:[],
      roles:[],
      currentUserId: 0,
      pid:0,

    }
  },
  // 钩子函数，在创建页面的时候最先加载
  created() {
    this.load()
  },
  methods:{

    load(){
      //请求分页查询数据
      let currentUserName = ''
      let currentUserEmail = ''
      var currentUserId = 0



      currentUserName =JSON.parse(localStorage.getItem("user")).username
      currentUserEmail =JSON.parse(localStorage.getItem("user")).email



      this.request.get("/user/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: currentUserName,
          email: currentUserEmail,

        }
      }).then(res => {
        console.log(555555555555)
        res.currentUserId = res.data.records[0].id
        localStorage.setItem("CurrentUserId",JSON.stringify(res))
      })

      //从localStorage中获取
      currentUserId =JSON.parse(localStorage.getItem("CurrentUserId")).currentUserId


      this.request.get("/customer/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.name,
          email: this.email,
          pid: currentUserId,
    }
      }).then(res =>{
        console.log(1111111111111)
        console.log(currentUserId)
        // this.tableData =  res.data.records
        //去掉data就可以显示出数据；
        this.tableData =  res.records
        this.total = res.data.total
      })





      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize + "&username=" + this.username)
      //     .then(res => res.json()).then(res => {
      //   console.log(res)
      //   this.tableData = res.data
      //   this.total = res.total
      // })
    },
    save(){
      // let pid = 0
      // pid = JSON.parse(localStorage.getItem("CurrentUserId")).currentUserId
      // this.form.set()
      // form["pid"] = pid
      this.request.post("/customer",this.form).then(res =>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.load()
          this.dialogFormVisible=false
        }else {
          this.$message.error("保存失败")
          this.load()
        }
        this.dialogFormVisible=false
      })
    },
    reset(){
      this.username = ''
      this.email = ''
      this.phone = ''
      this.load()
    },
    handleAdd(){
      this.dialogFormVisible=true
      this.form = {}

    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
      this.load()
    },
    handleDelete(id){
      this.request.delete("/customer/"+id).then(res => {
        if (res.code === '200'){
          this.$message.success("删除成功")
          this.load()
          this.dialogFormVisible = false
        } else {
          this.$message.error("删除失败")
          this.load()
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val

    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id) //[{},{},{}]=>[1,2,3]
      this.request.post("/customer/del/batch",ids).then(res => {
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
          this.load()
        }
      })
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()

    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()

    },
    exp(){
      window.open(`http://${serverIp}:9090/customer/export`)
    },
    handleExcelImportSuccess(){
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>

<style scoped>
/*.tableHeaderBg{*/
/*  background: #ffffff !important;*/
/*}*/
</style>
