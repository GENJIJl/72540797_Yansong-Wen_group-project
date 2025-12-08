<template>
  <div>
    <div style="width: 100%;height:1000px;padding:40px 10px; background-color: white;border-radius: 5px">
      <!-- Buttons and search box -->
      <div style="margin-bottom: 40px;font-size: 18px;font-weight: 1000;">Course Resource Management</div>
      <div style="margin-bottom: 20px; display: flex">
        <div style="flex: 9;text-align: left">
          <el-input
              placeholder="Please enter course resource title to search"
              size="small"
              v-model="search.title"
              style="width: 20%; margin-right: 10px;">
            <i slot="suffix" class="el-input__icon el-icon-search"></i>
          </el-input>


          <el-button
              type="success"
              size="small"
              style="width: 100px;text-align: center;background-color: rgb(26, 161, 74);border-radius: 5px"
              @click="load">
            Search
          </el-button>

          <!-- View mode toggle buttons -->
          <el-radio-group v-model="viewMode" style="margin-left: 20px;" size="small">
            <el-radio-button label="table">Table View</el-radio-button>
            <el-radio-button label="card">Card View</el-radio-button>
          </el-radio-group>
        </div>
        <div style="flex:5;text-align: right">
          <el-button
              type="success"
              size="small"
              style="width: 100px;text-align: center;margin-right: 10px;background-color: rgb(26, 161, 74);border-radius: 5px"
              @click="add">
            Add
          </el-button>
        </div>
      </div>

      <!-- Table view -->
      <el-table
          v-if="viewMode === 'table'"
          :data="tableData"
          border
          stripe
          style="width: 100%"
          :cell-style="{ textAlign: 'center' }"
          :header-cell-style="{ textAlign: 'center' }">

        <el-table-column label="Cover">
          <template v-slot="scope">
            <el-image
                :src="scope.row.img"
                :preview-src-list="[scope.row.img]"
                style="width: 50px; height: 50px; border-radius: 5px">
            </el-image>
          </template>
        </el-table-column>

        <el-table-column prop="title" label="Title"></el-table-column>
        <el-table-column prop="category" label="Chapter">
          <template slot-scope="scope">
            Chapter {{ scope.row.category }}
          </template>
        </el-table-column>

        <el-table-column prop="time" label="Publish Time"></el-table-column>
        <el-table-column prop="likecount" label="Likes"></el-table-column>
        <el-table-column prop="collectcount" label="Collections"></el-table-column>
        <el-table-column prop="downloadcount" label="Downloads"></el-table-column>

        <el-table-column fixed="right" label="Actions">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="download(scope.row)">Download</el-button>
            <el-button type="text" size="small" @click="edit(scope.row)">Edit</el-button>
            <el-popconfirm
                title="Are you sure to delete?"
                @confirm="del(scope.row.id)"
                style="margin-left: 10px">
              <el-button size="small" type="text" slot="reference">Delete</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Chapter grouped card view -->
      <div v-if="viewMode === 'card'">
        <div v-for="chapter in chaptersWithResources" :key="chapter.id" style="margin-bottom: 20px;">
          <div style="display: flex; align-items: center; margin-bottom: 10px;">
            <h3 style="margin: 0; color: #1aa14a; flex: 1;">Chapter {{ chapter.id }} Resources</h3>
            <el-button
                size="mini"
                type="success"
                plain
                @click="expandChapter(chapter.id)"
                style="margin-right: 10px;">
              {{ chapterExpanded[chapter.id] ? 'Collapse' : 'Expand' }}
            </el-button>
          </div>

          <el-collapse-transition>
            <div v-show="chapterExpanded[chapter.id]">
              <el-row :gutter="16">

                <el-col :span="4" v-for="item in chapter.resources" :key="item.id">
                  <el-card shadow="hover" style="margin-bottom: 15px;" class="resource-card">
                    <div style="display: flex; flex-direction: column; height: 100px;">
                      <div style="flex: 1;">
                        <div style="font-weight: bold; margin-bottom: 3px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ item.title }}</div>
                        <div style="font-size: 11px; color: #999; margin-bottom: 3px;">
                          <el-tag size="mini" :type="getFileTypeTag(item.type)">{{ getFileTypeName(item.type) }}</el-tag>
                        </div>
                        <div style="font-size: 11px; color: #999; margin-bottom: 3px;">
                          {{ formatDate(item.time) }}
                        </div>
                        <div style="font-size: 11px; color: #999;">
                          <i class="el-icon-star-on" style="color: #ffcc00;"></i> {{ item.likecount }}
                          <i class="el-icon-collection-tag" style="color: #409EFF; margin-left: 8px;"></i> {{ item.collectcount }}
                          <i class="el-icon-download" style="color: #67C23A; margin-left: 8px;"></i> {{ item.downloadcount }}
                        </div>
                      </div>
                      <div style="display: flex; justify-content: space-between; margin-top: 5px; font-size: 11px;">
                        <el-button type="text" size="mini" @click="download(item)">Download</el-button>
                        <el-button type="text" size="mini" @click="edit(item)">Edit</el-button>
                        <el-button type="text" size="mini" @click="confirmDelete(item.id)">Delete</el-button>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-collapse-transition>
        </div>
      </div>

      <!-- Pagination -->
      <div class="block" style="text-align: right;margin-top: 20px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- Add/Edit dialog -->
    <el-dialog title="Please fill in the information" :visible.sync="dialogVisible" width="40%">
      <el-form
          :model="form"
          label-position="right"
          label-width="100px"
          style="padding-right: 40px">

        <el-form-item label="Cover">
          <el-upload
              class="avatar-uploader"
              :action="getUploadUrl()"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :data="getUploadData()">

            <img v-if="form.img" :src="form.img" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="Title">
          <el-input
              size="small"
              v-model="form.title"
              placeholder="Please enter title">
          </el-input>
        </el-form-item>

        <el-form-item label="Chapter">
          <el-select
              style="width: 100%"
              size="small"
              v-model="form.category"
              placeholder="Please select chapter">
            <el-option v-for="n in 10" :key="n" :value="n.toString()" :label="`Chapter ${n}`"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Type">
          <el-select style="width: 100%;margin-bottom: 15px;background-color: rgba(255,255,255,0.1) !important;" size="small"  placeholder="Please select file type" v-model="form.type">
            <el-option value="document" label="Document"></el-option>
            <el-option value="video" label="Video"></el-option>
            <el-option value="code" label="Code"></el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <el-upload style="padding-left:100px; "
                 class="upload-demo"
                 drag
                 :action="getUploadUrl()"
                 :on-success="successUpload"
                 :data="getUploadData()"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">Drag file here, or <em>click to upload</em></div>
        <div class="el-upload__tip" slot="tip"></div><!--Tip information-->
      </el-upload>

      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">Cancel</el-button>
        <el-button size="small" type="primary" @click="save" style="background-color: #1aa14a; border-color: #1aa14a">Save</el-button>
      </div>
      <i style="margin-left: 100px;color: red;">warning: Upload file cannot be empty!</i>
    </el-dialog>

    <!-- Download notification popup -->
    <el-dialog
        title="Download Successful"
        :visible.sync="downloadNoticeVisible"
        width="30%"
        :show-close="false"
        center>
      <div style="text-align: center;">
        <i class="el-icon-success" style="color: #1aa14a; font-size: 50px;"></i>
        <p>File "{{ currentDownloadItem.title }}" download started</p>
        <p style="margin-top: 10px; font-size: 16px;">
          <i class="el-icon-download" style="color: #67C23A;"></i>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="downloadNoticeVisible = false" style="background-color: #1aa14a; border-color: #1aa14a">OK</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      form: {},
      dialogVisible: false,
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      search: {
        category: ''
      },
      token: localStorage.getItem("token"),
      viewMode: 'table',
      chapterExpanded: {},
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择文件类型', trigger: 'change' }
        ],
        path: [
          { required: true, message: '请上传文件', trigger: 'change' }
        ]
      },
      downloadNoticeVisible: false,
      currentDownloadItem: {}
    }
  },

  computed: {

    chaptersWithResources() {
      const chapters = [];
      for (let i = 1; i <= 10; i++) {
        chapters.push({
          id: i.toString(),
          resources: []
        });
      }


      this.tableData.forEach(resource => {
        const chapterIndex = chapters.findIndex(chapter => chapter.id === resource.category);
        if (chapterIndex !== -1) {
          chapters[chapterIndex].resources.push(resource);
        }
      });

      return chapters.filter(chapter => chapter.resources.length > 0);
    }
  },

  created() {
    this.load();

    for (let i = 1; i <= 10; i++) {
      this.$set(this.chapterExpanded, i.toString(), true);
    }
  },

  methods: {
    getUploadUrl() {
      return "http://localhost:9090/files/upload";
    },


    getUploadData() {
      return {
        category: this.form.category || ''
      };
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },


    expandChapter(chapterId) {
      this.$set(this.chapterExpanded, chapterId, !this.chapterExpanded[chapterId]);
    },


    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },


    getFileTypeName(type) {
      const typeMap = {
        'document': 'Document',
        'image': 'Image',
        'video': 'Video',
        'code': 'Code',
        'software': 'Software'
      };
      return typeMap[type] || type;
    },


    getFileTypeTag(type) {
      const tagMap = {
        'document': 'primary',
        'image': 'success',
        'video': 'warning',
        'code': 'info',
        'software': 'info'
      };
      return tagMap[type] || '';
    },


    confirmDelete(id) {
      this.$confirm('Are you sure to delete this resource?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.del(id);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Delete cancelled'
        });
      });
    },


    async del(id) {
      try {

        const resourceToDelete = this.tableData.find(item => item.id === id);

        const res = await request.delete(`/courseResource/${id}`)

        if (res.code === '0') {
          // 如果数据库记录删除成功，再删除物理文件
          if (resourceToDelete && resourceToDelete.path) {
            const fullPath = resourceToDelete.path;

            // 检查文件是否存储在章节文件夹中
            if (fullPath.includes('/chapter/')) {
              const chapterRegex = /\/chapter\/(\d+)\/([^/]+)$/;
              const matches = fullPath.match(chapterRegex);

              if (matches && matches.length === 3) {
                const chapter = matches[1];
                const fileFlag = matches[2];

                // 调用章节文件删除API
                try {
                  await request.delete(`/files/chapter/${chapter}/${fileFlag}`);

                } catch (fileError) {

                }
              }
            } else {

              const fileFlag = fullPath.substring(fullPath.lastIndexOf('/') + 1);
              try {
                await request.delete(`/files/${fileFlag}`);

              } catch (fileError) {

              }
            }
          }

          this.$notify.success('Delete successful');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      } catch (error) {
        console.error('Delete failed', error);
        this.$notify.error('Delete failed');
      }
    },

    // Load data
    async load() {
      try {
        const res = await request.post(`/courseResource/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}`, this.search)
        if (res.code === '0') {
          this.tableData = res.data.list
          this.total = res.data.total
        } else {
          this.$notify.error(res.msg)
        }
      } catch (error) {
        this.$notify.error('Data loading failed')
      }
    },

    // Edit
    edit(row) {
      this.form = {...row}  // Use object spread operator to simplify copy
      this.dialogVisible = true
    },

    // Add
    add() {
      this.form = {}
      this.dialogVisible = true
    },

    successUpload(res) {
      console.log('File upload successful:', res)
      // Need to save the file path returned from upload to the form
      if (res) {
        this.form.path = res.data
      }
      this.form.time = new Date().toISOString()
    },

    // Save
    async save() {
      try {
        if (!this.form.path) {
          this.$notify.error({
            title: "Error",
            message: "Please upload file",
          });
          return;
        }

        const url = this.form.id ? "/courseResource/edit" : "/courseResource"
        const res = await request.post(url, this.form)

        if (res.code === '0') {
          this.$notify.success(this.form.id ? "Update successful" : "Add successful")
          this.dialogVisible = false
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      } catch (error) {
        this.$notify.error('Save failed')
      }
    },

    handleAvatarSuccess(res) {
      this.form.img = res.data
    },

    async download(resourceItem) {
      if (!resourceItem || !resourceItem.id || !resourceItem.path) {
        this.$notify.error('Resource information incomplete, cannot download');
        return;
      }

      try {

        this.currentDownloadItem = { ...resourceItem };


        const res = await request.post(`/courseResource/download/${resourceItem.id}`);

        if (res.code === '0') {

          if (res.data && typeof res.data.downloadcount !== 'undefined') {
            this.currentDownloadItem.downloadcount = res.data.downloadcount;
            const index = this.tableData.findIndex(item => item.id === resourceItem.id);
            if (index !== -1) {
              this.$set(this.tableData[index], 'downloadcount', res.data.downloadcount);
            }
          } else {
            this.currentDownloadItem.downloadcount = (resourceItem.downloadcount || 0) + 1;
            const index = this.tableData.findIndex(item => item.id === resourceItem.id);
            if (index !== -1) {
              this.$set(this.tableData[index], 'downloadcount', this.currentDownloadItem.downloadcount);
            }
          }


          this.downloadNoticeVisible = true;

          window.location.href = resourceItem.path;

        } else {
          this.$notify.error(res.msg || 'Download failed');
        }
      } catch (error) {
        console.error('Download processing failed:', error);
        this.$notify.error('Download processing failed');


        window.location.href = resourceItem.path;
      }
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: rgb(26, 161, 74);
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color:rgb(26, 161, 74) !important;
  color: #fff !important;
}


.chapter-title {
  font-size: 18px;
  font-weight: bold;
  margin: 15px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
  color: #1aa14a;
}


.resource-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.resource-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}


.download-notice {
  text-align: center;
}
.download-notice .count {
  font-size: 18px;
  font-weight: bold;
  color: #1aa14a;
  margin: 15px 0;
}
</style>