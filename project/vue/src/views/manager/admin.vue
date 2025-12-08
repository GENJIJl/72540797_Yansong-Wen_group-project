<template>
  <div id="main">
    <div style="width: 97%;height:100%;padding:40px 40px; background-color: white;border-radius: 4px">
      <div style="margin-bottom: 40px;font-size: 18px;font-weight: 1000;">Student Management</div>
      <div style="margin-bottom: 20px; display: flex">
        <div style="flex: 10;text-align: left">
          <el-input placeholder="Please enter search content" size="small"  v-model="search.userName" style="width: 20%; margin-right: 20px;"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
          <el-button type="success" size="small" style="border-radius: 5px;width: 100px;text-align: center;background-color: rgb(26, 161, 74)" @click="load">Search</el-button>
        </div>
        <div style="flex:2;text-align: right">
          <el-button type="success" size="small" style="border-radius: 5px;width: 100px;text-align: center;background-color:rgb(26, 161, 74)"  @click="add">Add</el-button>
        </div>
      </div>
      <el-table :data="tableData" border  stripe style="width: 100%"  :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }" >
        <el-table-column label="Avatar">
          <template v-slot="scope">
            <el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 50px; height: 50px; border-radius: 10px"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="Name" ></el-table-column>
        <el-table-column prop="idnum" label="Student ID" ></el-table-column>
        <el-table-column prop="gradeName" label="Class"></el-table-column>
        <el-table-column prop="gender" label="Gender" ></el-table-column>
        <el-table-column prop="birth" label="Date of Birth" ></el-table-column>
        <el-table-column prop="contactInfo" label="Contact" ></el-table-column>
        <el-table-column prop="email" label="Email" ></el-table-column>
        <el-table-column prop="password" label="Password" ></el-table-column>


        <el-table-column fixed="right" label="Actions" >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">Edit</el-button>
            <el-popconfirm title="Are you sure to delete?" @confirm="del(scope.row.id)" style="margin-left: 10px">
              <el-button size="small" type="text" slot="reference">Delete</el-button>
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
        <el-form-item label="Avatar">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="form.avatar" :src="form.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="Username">
          <el-input size="small" v-model="form.userName" placeholder="Please enter username"></el-input>
        </el-form-item>
        <el-form-item label="Student ID">
          <el-input size="small" v-model="form.idnum" placeholder="Please enter student ID"></el-input>
        </el-form-item>
        <el-form-item label="Gender">
          <el-radio-group size="small" v-model="form.gender">
            <el-radio label="Male"></el-radio>
            <el-radio label="Female"></el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Date of Birth">
          <el-date-picker  style="width: 100%;margin-bottom: 15px" size="large"  placeholder="Date of Birth" v-model="form.birth"
                           type="date"
                           format="yyyy-MM-dd"
                           value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="Contact">
          <el-input size="small" v-model="form.contactInfo" placeholder="Please enter contact information"></el-input>
        </el-form-item>

        <el-form-item label="Email">
          <el-input size="small" v-model="form.email" placeholder="Please enter email"></el-input>
        </el-form-item>

        <el-form-item label="Password">
          <el-input size="small" v-model="form.password" placeholder="Please enter password"></el-input>
        </el-form-item>

        <el-form-item label="Class">
          <el-select size="small" style="width: 100%" v-model="form.gradeId">
            <el-option v-for="item in gradeData" :value="item.id" :label="item.gradeName" :key="item.id"></el-option>
          </el-select>
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
    this.loadGrade();
  },

  methods: {
    handleClick(row) {
      console.log(row);
    },
    handleSizeChange(val) {
      console.log(`${val} items per page`);
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },


    loadGrade(){
      request.get("grade/alldata").then(res =>{
        if (res.code === '0'){
          this.gradeData = res.data;
        } else {
          this.$notify.error(res.msg)
        }

      });
    },



    load() {
      request.post("/admin/page?pageNum=" + this.pageNum, this.search).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

    // Click add button, open modal and clear data
    add(){
      this.form = {};
      this.dialogVisible = true;
    },
    // Both add and update use save method, need to determine if it's add or update based on id
    save(){
      if (!this.form.id){ // If no id, use add interface
        this.form.role = 1;// Automatically assign role 1
        request.post("/admin",this.form).then(res =>{ // POST request sends form object to backend
          if (res.code === '0'){ // If interface call succeeds, close modal and reload data
            this.$notify.success("Add successful");
            this.dialogVisible = false;
            this.load();
          } else {
            this.$notify.error(res.msg); // If unsuccessful, return error message
          }
        })
      }else {
        request.post("/admin/edit",this.form).then(res =>{ // POST request sends form object to backend
          if (res.code === '0'){ // If interface call succeeds, close modal and reload data
            this.$notify.success("Update successful");
            this.dialogVisible = false;
            this.load();
          } else {
            this.$notify.error(res.msg); // If unsuccessful, return error message
          }
        })
      }
    },
    // Delete data by ID
    del(id) {
      request.delete("/admin/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success('Delete successful');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

    handleAvatarSuccess(res) {
      this.form.avatar = res.data;
    },

    // Click edit, open modal, display current object information
    edit(row){
      this.form = JSON.parse(JSON.stringify(row));
      // JSON.parse() converts a JSON string to a JavaScript object or value,
      // JSON.stringify() converts a JavaScript value (object or array) to a JSON string.
      this.$nextTick(() => {
        this.$refs.tx.clearFiles()
      });
      this.dialogVisible = true;
    },
  },


  data() {
    return {
      form:{},
      dialogVisible:false,
      input:'',
      pageNum:1,
      total:0,
      search:{},
      tableData: [],
      gradeData: {},
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