<template>
  <div id="main">
    <div style="width: 97%;height:100%;padding:40px 40px; background-color: white;border-radius: 4px">
      <div style="margin-bottom: 40px;font-size: 18px;font-weight: 1000;">Class Management</div>
      <div style="margin-bottom: 20px; display: flex">
        <div style="flex: 10;text-align: left">
          <el-input placeholder="Please enter search content" size="small"  v-model="search.gradeName" style="width: 20%; margin-right: 20px;"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
          <el-button type="success" size="small" style="border-radius: 5px;width: 100px;text-align: center;background-color: rgb(26, 161, 74)" @click="load">Search</el-button>
        </div>
        <div style="flex:2;text-align: right">
          <el-button v-if="user.role == 2" type="success" size="small" style="border-radius: 5px;width: 100px;text-align: center;background-color:rgb(26, 161, 74)"  @click="add">Add</el-button>
        </div>
      </div>
      <el-table :data="tableData" border  stripe style="width: 100%"  :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }" >

        <el-table-column prop="gradeName" label="Class Name" ></el-table-column>
        <el-table-column prop="brief" label="Brief" ></el-table-column>




        <el-table-column fixed="right" label="Actions" >
          <template slot-scope="scope">
            <el-button v-if="user.role == 2" type="text" size="small" @click="edit(scope.row)">Edit</el-button>
            <el-popconfirm title="Are you sure to delete?" @confirm="del(scope.row.id)" style="margin-left: 10px">
              <el-button v-if="user.role == 2" size="small" type="text" slot="reference">Delete</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>

      <div class="block" style="text-align: right;margin-top: 20px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="5"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>

    </div>

<!--Modal-->
    <el-dialog  title="Please fill in the information" :visible.sync="dialogVisible" width="30%" >

      <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">

        <el-form-item label="Class Name">
          <el-input size="small" v-model="form.gradeName" placeholder="Please enter class name"></el-input>
        </el-form-item>
        <el-form-item label="Brief">
          <el-input size="small" v-model="form.brief" placeholder="Please enter class brief"></el-input>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">Cancel</el-button>
        <el-button style="background-color: rgb(26, 161, 74)" size="small" type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>



  </div>
</template>





<script>
import request from "@/utils/request";
export default {
  created() {
    this.load();
  },

  methods: {

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },



    load() {
      request.post("/grade/page?pageNum=" + this.pageNum, this.search).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$notify.error(res.msg);
        }
      });
    },


    add(){
      this.form = {};
      this.dialogVisible = true;
    },

    save(){
      if (!this.form.id){
        request.post("/grade",this.form).then(res =>{
          if (res.code === '0'){
            this.$notify.success("Add successful");
            this.dialogVisible = false;
            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      }else {
        request.post("/grade/edit",this.form).then(res =>{
          if (res.code === '0'){
            this.$notify.success("Update successful");
            this.dialogVisible = false;
            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      }
    },

    del(id) {
      request.delete("/grade/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success('Delete successful');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },




    edit(row){
      this.dialogVisible = true;
      this.form = row;
    },
  },


  data() {
    return {

      user: JSON.parse(localStorage.getItem("user") || {}),
      form:{},
      dialogVisible:false,
      input:'',
      pageNum:1,
      total:0,
      search:{},
      tableData: []
    }
  },
}
</script>



<style>
  .el-main{
    width: 99.3%;
    height:1000px;
    border-style: solid;
    border-color: gray;
    align-self: center;
    border-radius: 10px;
    border-color: rgb(217, 217, 217,80%);
    border-width:2px;
  }

  #id{
    width:95%;
    margin-right: 5%;
  }

  .block>ul:hover{
    color: rgb(26, 161, 74);
  }

  .el-pagination.is-background .el-pager li:not(.disabled).active{
    background-color: rgb(26, 161, 74);
    color: white;
  }
  .el-pagination.is-background .el-pager li:not(.disabled):hover{
    color: rgb(26, 161, 74);
  }

</style>