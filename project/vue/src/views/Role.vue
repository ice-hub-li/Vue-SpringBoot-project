<template>
  <div>

    <div style="margin: 10px 0" >
      <el-input style="width: 200px" placeholder="请输入名称搜索"  prefix-icon="el-icon-search"
                v-model:type="name"></el-input>
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
<!--      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept=".xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">导入<i class="el-icon-download"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-upload2"></i></el-button>-->
    </div>


    <el-table :data="tableData" style="width: 100%" height="360" stripe :header-cell-class-name="tableHeaderBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column  prop="id" label="ID" ></el-table-column>
      <el-table-column  prop="name" label="名称" ></el-table-column>

      <el-table-column  prop="flag" label="唯一标识" ></el-table-column>


      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column label="操作" fixed="right" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row.id)">分配菜单<i class="el-icon-menu"></i></el-button>
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

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form  label-width="60px" size="small">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识" :label-width="formLabelWidth">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%" >
      <el-tree
          :data="menuData"
          node-key="id"
          ref="tree"
          :props="props"
          :default-expanded-keys="expends"
          :default-checked-keys="checks"
          show-checkbox
          @check-change="handleCheckChange">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"/> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>



  </div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      name:'',
      form:{},
      dialogFormVisible:false,
      menuDialogVis:false,
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse:false,
      sideWith:200,
      logoTextShow:true,
      tableHeaderBg:'tableHeaderBg',
      multipleSelection:[],
      menuData:[],
      props:{
        label: 'name',
      },
      expends:[],
      checks:[],
      roleId:0,
    }
  },
  // 钩子函数，在创建页面的时候最先加载
  created() {
    this.load()
  },
  methods:{
    load(){
      //请求分页查询数据
      this.request.get("/role/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res =>{
        console.log(res)
        this.tableData =  res.data.records
        this.total = res.data.total
      })

      //



      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize + "&username=" + this.username)
      //     .then(res => res.json()).then(res => {
      //   console.log(res)
      //   this.tableData = res.data
      //   this.total = res.total
      // })
    },
    save(){
      this.request.post("/role",this.form).then(res =>{
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
    saveRoleMenu(){
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        console.log(this.$refs.tree.getCheckedKeys());
        console.log(res)
        if (res.code === '200') {
          this.$message.success("绑定成功")
          this.menuDialogVis = false

          // 操作管理员角色后需要重新登录
          // if (this.roleFlag === 'ROLE_ADMIN') {
          //   this.$store.commit("logout")
          // }

        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset(){
      this.name = ''
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
      this.request.delete("/role/"+id).then(res => {
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
      this.request.post("/role/del/batch",ids).then(res => {
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
    selectMenu(roleId){
      this.menuDialogVis = true
      this.roleId = roleId

      //请求菜单数据
      this.request.get("/menu").then(res =>{
        this.menuData =  res.data

        //把后台返回的菜单处理为id数组；
        this.expends = this.menuData.map(v => v.id)
      })

      // 请求接口将数据返回
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data

      })



    },
    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
  }
}
</script>

<style scoped>
/*.tableHeaderBg{*/
/*  background: #ffffff !important;*/
/*}*/
</style>
