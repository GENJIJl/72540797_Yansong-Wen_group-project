<template>
  <div class="student-resources">
    <div class="header">
      <h2>Course Resources</h2>
      <div class="actions">
        <el-input
            v-model="search.title"
            size="small"
            placeholder="Search by title"
            style="width: 240px; margin-right: 10px"/>
        <el-select v-model="search.category" size="small" placeholder="Chapter" style="width: 140px; margin-right: 10px">
          <el-option v-for="n in 10" :key="n" :label="`Chapter ${n}`" :value="n.toString()"/>
        </el-select>
        <el-button type="primary" size="small" @click="loadResources">Search</el-button>
        <el-button type="danger" size="small" plain style="margin-left: 10px" @click="logout">Logout</el-button>
      </div>
    </div>

    <el-table
        :data="resources"
        border
        stripe
        style="width: 100%; margin-top: 10px"
        :cell-style="{ textAlign: 'center' }"
        :header-cell-style="{ textAlign: 'center' }">
      <el-table-column prop="title" label="Title" min-width="160"/>
      <el-table-column prop="category" label="Chapter" min-width="90">
        <template slot-scope="scope">
          Chapter {{ scope.row.category }}
        </template>
      </el-table-column>
      <el-table-column prop="type" label="Type" min-width="90"/>
      <el-table-column prop="downloadcount" label="Downloads" min-width="100"/>
      <el-table-column prop="likecount" label="Likes" min-width="120">
        <template slot-scope="scope">
          <span style="margin-right:6px;">{{ scope.row.likecount || 0 }}</span>
          <el-button type="text" size="mini" @click="like(scope.row)">Like</el-button>
        </template>
      </el-table-column>
      <el-table-column label="Actions" min-width="120">
        <template slot-scope="scope">
          <el-button type="text" @click="download(scope.row)">Download</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="block" style="text-align: right; margin-top: 10px">
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <div class="message-area">
      <h3>Leave a message</h3>
      <el-input
          type="textarea"
          :rows="3"
          v-model="newMessage"
          placeholder="Share your feedback or questions"/>
      <div style="text-align: right; margin-top: 8px;">
        <el-button type="primary" size="small" @click="submitMessage">Submit</el-button>
      </div>

      <div class="message-list">
        <div class="message-item" v-for="msg in messages" :key="msg.id">
          <div class="message-header">
            <span class="author">{{ msg.userName || 'User' }}</span>
            <span class="time">{{ msg.time }}</span>
          </div>
          <div class="content">{{ msg.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'StudentResource',
  data() {
    return {
      resources: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      search: {
        title: '',
        category: ''
      },
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      newMessage: '',
      messages: []
    }
  },
  created() {
    this.loadResources()
    this.loadMessages()
  },
  methods: {
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.loadResources()
    },
    async loadResources() {
      try {
        const res = await request.post(`/courseResource/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}`, this.search)
        if (res.code === '0') {
          this.resources = res.data.list
          this.total = res.data.total
        } else {
          this.$notify.error(res.msg)
        }
      } catch (e) {
        this.$notify.error('Failed to load resources')
      }
    },
    async download(resource) {
      if (!resource || !resource.id) return
      try {
        await request.post(`/courseResource/download/${resource.id}`)
      } catch (e) {
        // ignore counter failure
      }
      if (resource.path) {
        window.open(resource.path, '_blank')
      } else {
        this.$notify.error('No download path')
      }
    },
    async loadMessages() {
      try {
        const res = await request.get('/message')
        if (res.code === '0') {
          this.messages = res.data || []
        } else {
          this.$notify.error(res.msg)
        }
      } catch (e) {
        this.$notify.error('Failed to load messages')
      }
    },
    async submitMessage() {
      if (!this.newMessage) {
        this.$message.error('Please enter message content')
        return
      }
      if (!this.user || !this.user.id) {
        this.$message.error('Please log in first')
        return
      }
      const payload = {
        content: this.newMessage,
        userId: this.user.id,
        role: this.user.role,
        parentId: 0
      }
      try {
        const res = await request.post('/message', payload)
        if (res.code === '0') {
          this.$notify.success('Message submitted')
          this.newMessage = ''
          this.loadMessages()
        } else {
          this.$notify.error(res.msg)
        }
      } catch (e) {
        this.$notify.error('Failed to submit message')
      }
    },
    async like(resource) {
      if (!resource || !resource.id) return
      try {
        const res = await request.post(`/courseResource/like/${resource.id}`)
        if (res.code === '0') {
          const idx = this.resources.findIndex(r => r.id === resource.id)
          if (idx !== -1) {
            const current = this.resources[idx].likecount || 0
            this.$set(this.resources[idx], 'likecount', current + 1)
          }
        } else {
          this.$notify.error(res.msg)
        }
      } catch (e) {
        this.$notify.error('Failed to like resource')
      }
    },
    logout() {
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.student-resources {
  padding: 20px;
  background: #f6f8fb;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.actions {
  display: flex;
  align-items: center;
}
.res-card {
  margin-bottom: 16px;
}
.cover {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}
.res-title {
  font-weight: 600;
  margin-bottom: 4px;
}
.res-meta {
  margin-bottom: 6px;
}
.res-counts {
  display: flex;
  justify-content: space-between;
  color: #888;
  font-size: 12px;
  margin-bottom: 6px;
}
.message-area {
  margin-top: 20px;
  background: #fff;
  padding: 16px;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}
.message-list {
  margin-top: 12px;
}
.message-item {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}
.message-header {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
}
.content {
  margin-top: 4px;
  font-size: 14px;
}

/* Light green search inputs */
/deep/ .el-input__inner {
  background-color: #e6f6ec !important;
}
</style>

