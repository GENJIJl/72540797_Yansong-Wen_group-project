<template>
  <el-card class="resource-card">
    <img :src="item.img" @error="handleImgError" class="resource-image">
    <div class="card-content">
      <div class="title">{{ item.title }}</div>
      <div class="meta-info">
        <time class="time">{{ formatTime(item.time) }}</time>
        <el-button type="text" @click="$emit('download', item.path)" class="download-btn">
          <i class="el-icon-download"></i>
        </el-button>
      </div>
      <el-tag size="mini" :type="getTagType(item.type)" effect="plain">
        {{ item.type?.toUpperCase() || 'UNKNOWN' }}
      </el-tag>
    </div>
  </el-card>
</template>

<script>
export default {
  props: ['item'],
  methods: {
    handleImgError(e) {
      e.target.src = require('@/assets/defaultresource.jpg')
    },
    formatTime(time) {
      return new Date(time).toLocaleDateString()
    },
    getTagType(type) {
      const typeMap = {
        document: 'success',
        image: 'warning',
        video: 'danger',
        code: 'info',
        assignment: 'primary'
      }
      return typeMap[type] || 'info'
    }
  }
}
</script>

<style scoped>
/* 卡片样式 */
</style>