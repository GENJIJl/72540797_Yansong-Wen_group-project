<template>
  <div class="tabble_ground">
    <div class="moudle_name" STYLE="font-weight: 1000;font-size: 18px;margin-top: 40px;margin-left: 10px">Message Management</div>
    <div class="input_button">
      <div class="input_box">
        <el-input placeholder="Please enter message content to search" size="small"  v-model="search.content" class="input_left" style="margin-top: 40px;margin-left: 10px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
        <el-button type="success" size="small" style="border-radius: 5px;width: 100px;text-align: center;background-color: rgb(26, 161, 74);margin-top: 20px;margin-left: 10px" @click="load">Search</el-button>
      </div>
    </div>
    <el-table :data="tableData" border style="width: 100%;margin-top: 30px;margin-left: 10px" :fit="true" stripe :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">
      <el-table-column label="Message Content" prop="content"></el-table-column>
      <el-table-column label="User" prop="userName"></el-table-column>
      <el-table-column label="Time" prop="time"></el-table-column>
      <el-table-column fixed="right" label="Actions">
        <template slot-scope="scope">
          <el-popconfirm title="Are you sure to delete?" @confirm="del(scope.row.id)" style="margin-left: 10px">
            <el-button size="small" type="text" slot="reference" >Delete</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right;margin-top: 50px">
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
      request.post("/message/page?pageNum=" + this.pageNum, this.search).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

    del(id) {
      request.delete("/message/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success('Delete successful');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },

  },

  data() {
    return {
      user: JSON.parse(localStorage.getItem("user") || {}),
      input: '',
      pageNum: 1,
      total: 0,
      search: {},
      tableData: [],
      form: {}
    }
  }
}
</script>

<style>

</style>