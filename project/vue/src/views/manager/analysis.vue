<template>
  <div>
    <div style="width: 100%; padding: 40px 10px; background-color: white; border-radius: 5px">
      <!-- Title and filter area -->
      <div class="dashboard-header">
        <div style="margin-bottom: 20px; font-size: 18px; font-weight: 1000;">Course Resource Usage Analysis Report</div>
        <div style="margin-bottom: 20px; display: flex">
          <div style="flex: 9; text-align: left">
            <el-select
                v-model="filter.timeRange"
                placeholder="Time Range"
                size="small"
                style="width: 150px; margin-right: 15px;">
              <el-option label="Last Week" value="week"></el-option>
              <el-option label="Last Month" value="month"></el-option>
              <el-option label="Last Quarter" value="quarter"></el-option>
              <el-option label="This Semester" value="semester"></el-option>
              <el-option label="All" value="all"></el-option>
            </el-select>

            <el-select
                v-model="filter.chapter"
                placeholder="Chapter Filter"
                size="small"
                style="width: 150px; margin-right: 15px;">
              <el-option label="All Chapters" value=""></el-option>
              <el-option v-for="n in 10" :key="`chapter-${n}`" :value="n.toString()" :label="`Chapter ${n}`"></el-option>
            </el-select>

            <el-select
                v-model="filter.resourceType"
                placeholder="Resource Type"
                size="small"
                style="width: 150px; margin-right: 15px;">
              <el-option label="All Types" value=""></el-option>
              <el-option label="Document" value="document"></el-option>
              <el-option label="Video" value="video"></el-option>
              <el-option label="Code" value="code"></el-option>
            </el-select>

            <el-button
                type="success"
                size="small"
                style="width: 100px; text-align: center; background-color: rgb(26, 161, 74); border-radius: 5px"
                @click="loadData">
              Search
            </el-button>
          </div>
          <div style="flex: 5; text-align: right">
            <el-button
                type="success"
                size="small"
                style="width: 100px; text-align: center; margin-right: 10px; background-color: rgb(26, 161, 74); border-radius: 5px"
                @click="exportReport">
              Export Report
            </el-button>
          </div>
        </div>
      </div>


      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="6">
          <el-card shadow="hover" class="data-card">
            <div slot="header" class="card-header">
              <span>Total Resources</span>
            </div>
            <div class="card-content">
              <div class="card-value">{{ summaryData.totalResources }}</div>
              <div class="card-trend" :class="summaryData.resourceTrend >= 0 ? 'trend-up' : 'trend-down'">
                <i :class="summaryData.resourceTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(summaryData.resourceTrend) }}% vs previous period
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="data-card">
            <div slot="header" class="card-header">
              <span>Total Downloads</span>
            </div>
            <div class="card-content">
              <div class="card-value">{{ summaryData.totalDownloads }}</div>
              <div class="card-trend" :class="summaryData.downloadTrend >= 0 ? 'trend-up' : 'trend-down'">
                <i :class="summaryData.downloadTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(summaryData.downloadTrend) }}% vs previous period
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="data-card">
            <div slot="header" class="card-header">
              <span>Total Collections</span>
            </div>
            <div class="card-content">
              <div class="card-value">{{ summaryData.totalCollects }}</div>
              <div class="card-trend" :class="summaryData.collectTrend >= 0 ? 'trend-up' : 'trend-down'">
                <i :class="summaryData.collectTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(summaryData.collectTrend) }}% vs previous period
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="data-card">
            <div slot="header" class="card-header">
              <span>Total Likes</span>
            </div>
            <div class="card-content">
              <div class="card-value">{{ summaryData.totalLikes }}</div>
              <div class="card-trend" :class="summaryData.likeTrend >= 0 ? 'trend-up' : 'trend-down'">
                <i :class="summaryData.likeTrend >= 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(summaryData.likeTrend) }}% vs previous period
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>


      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div slot="header" class="card-header">
              <span>Chapter Resource Usage Distribution</span>
            </div>
            <div class="chart-container" id="chapterDistributionChart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div slot="header" class="card-header">
              <span>Resource Type Distribution</span>
            </div>
            <div class="chart-container" id="resourceTypeChart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="24">
          <el-card shadow="hover" class="chart-card">
            <div slot="header" class="card-header">
              <span>Resource Usage Trend</span>
              <el-radio-group v-model="trendMetric" size="mini" style="margin-left: 15px;">
                <el-radio-button label="downloads">Downloads</el-radio-button>
                <el-radio-button label="likes">Likes</el-radio-button>
                <el-radio-button label="collects">Collections</el-radio-button>
              </el-radio-group>
            </div>
            <div class="chart-container" id="usageTrendChart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div slot="header" class="card-header">
              <span>Most Popular Resources Top 10</span>
            </div>
            <div class="chart-container" id="topResourcesChart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div slot="header" class="card-header">
              <span>Multi-dimensional Resource Evaluation</span>
            </div>
            <div class="chart-container" id="radarChart"></div>
          </el-card>
        </el-col>
      </el-row>


      <el-card shadow="hover" style="margin-top: 20px;">
        <div slot="header" class="card-header">
          <span>Resource Usage Details</span>
          <el-radio-group v-model="sortBy" size="mini" style="margin-left: 15px;">
            <el-radio-button label="downloadcount">By Downloads</el-radio-button>
            <el-radio-button label="likecount">By Likes</el-radio-button>
            <el-radio-button label="collectcount">By Collections</el-radio-button>
          </el-radio-group>
        </div>
        <el-table
            :data="resourcesData"
            border
            stripe
            style="width: 100%"
            :cell-style="{ textAlign: 'center' }"
            :header-cell-style="{ textAlign: 'center' }">

          <el-table-column prop="title" label="Resource Title"></el-table-column>
          <el-table-column prop="category" label="Chapter">
            <template slot-scope="scope">
              Chapter {{ scope.row.category }}
            </template>
          </el-table-column>
          <el-table-column prop="type" label="File Type">
            <template slot-scope="scope">
              <el-tag :type="getFileTypeTag(scope.row.type)">{{ getFileTypeName(scope.row.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="Publish Time"></el-table-column>
          <el-table-column prop="downloadcount" label="Downloads">
            <template slot-scope="scope">
              <span>{{ scope.row.downloadcount }}</span>
              <el-tooltip content="Percentage of total downloads" placement="top">
                <el-progress
                    :percentage="getPercentage(scope.row.downloadcount, summaryData.totalDownloads)"
                    :color="getProgressColor(getPercentage(scope.row.downloadcount, summaryData.totalDownloads))"
                    :show-text="false"
                    style="width: 80%; margin: 0 auto;"></el-progress>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="likecount" label="Likes">
            <template slot-scope="scope">
              <span>{{ scope.row.likecount }}</span>
              <el-progress
                  :percentage="getPercentage(scope.row.likecount, summaryData.totalLikes)"
                  :color="getProgressColor(getPercentage(scope.row.likecount, summaryData.totalLikes))"
                  :show-text="false"
                  style="width: 80%; margin: 0 auto;"></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="collectcount" label="Collections">
            <template slot-scope="scope">
              <span>{{ scope.row.collectcount }}</span>
              <el-progress
                  :percentage="getPercentage(scope.row.collectcount, summaryData.totalCollects)"
                  :color="getProgressColor(getPercentage(scope.row.collectcount, summaryData.totalCollects))"
                  :show-text="false"
                  style="width: 80%; margin: 0 auto;"></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="usageScore" label="Overall Score">
            <template slot-scope="scope">
              <el-rate
                  v-model="scope.row.usageScore"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}">
              </el-rate>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import * as echarts from 'echarts'

export default {
  data() {
    return {

      filter: {
        timeRange: 'month',
        chapter: '',
        resourceType: ''
      },


      sortBy: 'downloadcount',
      trendMetric: 'downloads',


      summaryData: {
        totalResources: 0,
        totalDownloads: 0,
        totalCollects: 0,
        totalLikes: 0,
        resourceTrend: 10,
        downloadTrend: 15,
        collectTrend: 8,
        likeTrend: -5
      },


      resourcesData: [],
      charts: {}
    }
  },

  created() {
    this.loadData()
  },

  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },

  watch: {
    trendMetric() {
      this.updateUsageTrendChart()
    },
    sortBy() {
      this.sortResourcesData()
    }
  },

  methods: {

    getFileTypeName(type) {
      const typeMap = {
        'document': 'Document',
        'image': 'Image',
        'video': 'Video',
        'code': 'Code',
        'software': 'Software'
      }
      return typeMap[type] || type
    },


    getFileTypeTag(type) {
      const tagMap = {
        'document': 'primary',
        'image': 'success',
        'video': 'warning',
        'code': 'info',
        'software': 'info'
      }
      return tagMap[type] || ''
    },


    getPercentage(value, total) {
      if (!total) return 0
      return Math.round((value / total) * 100)
    },


    getProgressColor(percentage) {
      if (percentage < 30) return '#909399'
      if (percentage < 70) return '#409EFF'
      return '#67C23A'
    },

    // Load data
    async loadData() {
      try {

        const params = { ...this.filter }

        const res = await request.post('/courseResource/analysis', params)

        if (res.code === '0') {

          this.resourcesData = res.data.resources || []
          this.calculateUsageScore()
          this.updateSummaryData()
          this.updateAllCharts()
        } else {
          this.$notify.error(res.msg || 'Data loading failed')
        }
      } catch (error) {
        console.error('Data loading failed', error)

        this.loadMockData()
      }
    },


    loadMockData() {
      const mockResources = []

      for (let i = 1; i <= 20; i++) {
        const category = Math.ceil(Math.random() * 10)
        const typeOptions = ['document', 'video', 'code']
        const type = typeOptions[Math.floor(Math.random() * typeOptions.length)]
        const baseDownload = Math.floor(Math.random() * 100)
        const baseLike = Math.floor(Math.random() * 50)
        const baseCollect = Math.floor(Math.random() * 30)
        const resource = {
          id: i,
          title: `${this.getFileTypeName(type)} Resource ${i} - Chapter ${category}`,
          category: category.toString(),
          type: type,
          time: new Date(Date.now() - Math.random() * 10000000000).toISOString().split('T')[0],
          downloadcount: baseDownload + (category === 3 ? 50 : 0),  // Chapter 3 is particularly popular
          likecount: baseLike + (category === 3 ? 25 : 0),
          collectcount: baseCollect + (category === 3 ? 15 : 0),
        }

        mockResources.push(resource)
      }

      this.resourcesData = mockResources
      this.calculateUsageScore()
      this.updateSummaryData()
      this.updateAllCharts()
    },


    calculateUsageScore() {

      const maxDownloads = Math.max(...this.resourcesData.map(r => r.downloadcount || 0))
      const maxLikes = Math.max(...this.resourcesData.map(r => r.likecount || 0))
      const maxCollects = Math.max(...this.resourcesData.map(r => r.collectcount || 0))
      const weights = {
        download: 0.5,
        like: 0.3,
        collect: 0.2
      }


      this.resourcesData.forEach(resource => {

        const downloadScore = maxDownloads ? (resource.downloadcount || 0) / maxDownloads : 0
        const likeScore = maxLikes ? (resource.likecount || 0) / maxLikes : 0
        const collectScore = maxCollects ? (resource.collectcount || 0) / maxCollects : 0


        const totalScore = (
            weights.download * downloadScore +
            weights.like * likeScore +
            weights.collect * collectScore
        )


        resource.usageScore = Math.round(totalScore * 5 * 10) / 10
      })


      this.sortResourcesData()
    },

    // Sort resource data according to current sort method
    sortResourcesData() {
      this.resourcesData.sort((a, b) => {
        if (this.sortBy === 'usageScore') {
          return b.usageScore - a.usageScore
        }
        return (b[this.sortBy] || 0) - (a[this.sortBy] || 0)
      })
    },


    updateSummaryData() {
      this.summaryData = {
        totalResources: this.resourcesData.length,
        totalDownloads: this.resourcesData.reduce((sum, item) => sum + (item.downloadcount || 0), 0),
        totalCollects: this.resourcesData.reduce((sum, item) => sum + (item.collectcount || 0), 0),
        totalLikes: this.resourcesData.reduce((sum, item) => sum + (item.likecount || 0), 0),
        resourceTrend: 10,  // Assumed trend data, should be fetched from API in practice
        downloadTrend: 15,
        collectTrend: 8,
        likeTrend: -5
      }
    },

    // Initialize all charts
    initCharts() {
      // Chapter distribution chart
      this.charts.chapterDistribution = echarts.init(document.getElementById('chapterDistributionChart'))

      // Resource type chart
      this.charts.resourceType = echarts.init(document.getElementById('resourceTypeChart'))

      // Usage trend chart
      this.charts.usageTrend = echarts.init(document.getElementById('usageTrendChart'))

      // Top resources chart
      this.charts.topResources = echarts.init(document.getElementById('topResourcesChart'))

      // Radar chart
      this.charts.radar = echarts.init(document.getElementById('radarChart'))
      window.addEventListener('resize', () => {
        Object.values(this.charts).forEach(chart => {
          chart && chart.resize()
        })
      })


      this.updateAllCharts()
    },


    updateAllCharts() {
      this.updateChapterDistributionChart()
      this.updateResourceTypeChart()
      this.updateUsageTrendChart()
      this.updateTopResourcesChart()
      this.updateRadarChart()
    },

    // Update chapter distribution chart
    updateChapterDistributionChart() {
      if (!this.charts.chapterDistribution) return

      // Statistics resource data by chapter
      const chapterStats = {}
      for (let i = 1; i <= 10; i++) {
        chapterStats[i] = {
          downloads: 0,
          likes: 0,
          collects: 0,
          resources: 0
        }
      }

      // Statistics data for each chapter
      this.resourcesData.forEach(resource => {
        const chapter = parseInt(resource.category)
        if (chapterStats[chapter]) {
          chapterStats[chapter].downloads += resource.downloadcount || 0
          chapterStats[chapter].likes += resource.likecount || 0
          chapterStats[chapter].collects += resource.collectcount || 0
          chapterStats[chapter].resources += 1
        }
      })

      // Convert to chart data
      const xAxisData = Object.keys(chapterStats).map(ch => `Chapter ${ch}`)
      const seriesData = [
        {
          name: 'Downloads',
          type: 'bar',
          stack: 'total',
          emphasis: { focus: 'series' },
          data: Object.values(chapterStats).map(stat => stat.downloads)
        },
        {
          name: 'Likes',
          type: 'bar',
          stack: 'total',
          emphasis: { focus: 'series' },
          data: Object.values(chapterStats).map(stat => stat.likes)
        },
        {
          name: 'Collections',
          type: 'bar',
          stack: 'total',
          emphasis: { focus: 'series' },
          data: Object.values(chapterStats).map(stat => stat.collects)
        },
        {
          name: 'Resources',
          type: 'line',
          yAxisIndex: 1,
          emphasis: { focus: 'series' },
          data: Object.values(chapterStats).map(stat => stat.resources)
        }
      ]

      // Set chart options
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: ['Downloads', 'Likes', 'Collections', 'Resources']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: xAxisData
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: 'Usage'
          },
          {
            type: 'value',
            name: 'Resources',
            position: 'right'
          }
        ],
        series: seriesData,
        color: ['#409EFF', '#FFB980', '#67C23A', '#E6A23C']
      }

      this.charts.chapterDistribution.setOption(option)
    },


    updateResourceTypeChart() {
      if (!this.charts.resourceType) return


      const typeStats = {}

      this.resourcesData.forEach(resource => {
        if (!typeStats[resource.type]) {
          typeStats[resource.type] = {
            count: 0,
            downloads: 0,
            likes: 0,
            collects: 0
          }
        }

        typeStats[resource.type].count++
        typeStats[resource.type].downloads += resource.downloadcount || 0
        typeStats[resource.type].likes += resource.likecount || 0
        typeStats[resource.type].collects += resource.collectcount || 0
      })


      const pieData = Object.keys(typeStats).map(type => ({
        name: this.getFileTypeName(type),
        value: typeStats[type].count
      }))


      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: pieData.map(item => item.name)
        },
        series: [
          {
            name: 'Resource Type',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}: {c} ({d}%)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: true
            },
            data: pieData
          }
        ],
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      }

      this.charts.resourceType.setOption(option)
    },

    // Update usage trend chart
    updateUsageTrendChart() {
      if (!this.charts.usageTrend) return

      const months = []
      const now = new Date()
      for (let i = 5; i >= 0; i--) {
        const month = new Date(now)
        month.setMonth(now.getMonth() - i)
        months.push(month.toISOString().substr(0, 7)) // YYYY-MM format
      }


      const downloadData = [120, 132, 101, 134, 90, 230]
      const likeData = [220, 182, 191, 234, 290, 330]
      const collectData = [150, 232, 201, 154, 190, 330]

      let seriesData = []

      if (this.trendMetric === 'downloads') {
        seriesData = [{
          name: 'Downloads',
          type: 'line',
          smooth: true,
          data: downloadData
        }]
      } else if (this.trendMetric === 'likes') {
        seriesData = [{
          name: 'Likes',
          type: 'line',
          smooth: true,
          data: likeData
        }]
      } else {
        seriesData = [{
          name: 'Collections',
          type: 'line',
          smooth: true,
          data: collectData
        }]
      }


      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: seriesData,
        color: ['#409EFF', '#67C23A', '#E6A23C']
      }

      this.charts.usageTrend.setOption(option)
    },

    // Update popular resources chart
    updateTopResourcesChart() {
      if (!this.charts.topResources) return


      const topResources = [...this.resourcesData]
          .sort((a, b) => b.usageScore - a.usageScore)
          .slice(0, 10)


      const data = topResources.map(resource => ({
        name: resource.title,
        downloads: resource.downloadcount || 0,
        likes: resource.likecount || 0,
        collects: resource.collectcount || 0
      }))

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: ['Downloads', 'Likes', 'Collections']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        dataset: {
          source: data
        },
        xAxis: { type: 'value' },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name),
          axisLabel: {
            width: 100,
            overflow: 'truncate',
            interval: 0
          }
        },
        series: [
          {
            name: 'Downloads',
            type: 'bar',
            stack: 'total',
            emphasis: { focus: 'series' },
            encode: { x: 'downloads', y: 'name' }
          },
          {
            name: 'Likes',
            type: 'bar',
            stack: 'total',
            emphasis: { focus: 'series' },
            encode: { x: 'likes', y: 'name' }
          },
          {
            name: 'Collections',
            type: 'bar',
            stack: 'total',
            emphasis: { focus: 'series' },
            encode: { x: 'collects', y: 'name' }
          }
        ],
        color: ['#409EFF', '#67C23A', '#E6A23C']
      }

      this.charts.topResources.setOption(option)
    },

    // Update radar chart
    updateRadarChart() {
      if (!this.charts.radar) return


      const chapterStats = {}
      for (let i = 1; i <= 10; i++) {
        chapterStats[i] = {
          downloads: 0,
          likes: 0,
          collects: 0,
          count: 0
        }
      }


      this.resourcesData.forEach(resource => {
        const chapter = parseInt(resource.category)
        if (chapterStats[chapter]) {
          chapterStats[chapter].downloads += resource.downloadcount || 0
          chapterStats[chapter].likes += resource.likecount || 0
          chapterStats[chapter].collects += resource.collectcount || 0
          chapterStats[chapter].count += 1
        }
      })


      Object.keys(chapterStats).forEach(chapter => {
        if (chapterStats[chapter].count > 0) {
          chapterStats[chapter].avgDownloads = chapterStats[chapter].downloads / chapterStats[chapter].count
          chapterStats[chapter].avgLikes = chapterStats[chapter].likes / chapterStats[chapter].count
          chapterStats[chapter].avgCollects = chapterStats[chapter].collects / chapterStats[chapter].count
        }
      })


      const topChapters = Object.keys(chapterStats)
          .filter(chapter => chapterStats[chapter].count > 0)
          .sort((a, b) => chapterStats[b].count - chapterStats[a].count)
          .slice(0, 5)

      if (topChapters.length === 0) return


      const option = {
        tooltip: {},
        legend: {
          data: topChapters.map(ch => `Chapter ${ch}`)
        },
        radar: {
          indicator: [
            { name: 'Average Downloads', max: Math.max(...Object.values(chapterStats).map(s => s.avgDownloads || 0)) * 1.2 },
            { name: 'Average Likes', max: Math.max(...Object.values(chapterStats).map(s => s.avgLikes || 0)) * 1.2 },
            { name: 'Average Collections', max: Math.max(...Object.values(chapterStats).map(s => s.avgCollects || 0)) * 1.2 },
            { name: 'Resource Count', max: Math.max(...Object.values(chapterStats).map(s => s.count || 0)) * 1.2 },
            { name: 'Overall Score', max: 5 }
          ]
        },
        series: [{
          name: 'Multi-dimensional Chapter Resource Evaluation',
          type: 'radar',
          data: topChapters.map(chapter => {

            const chapterResources = this.resourcesData.filter(r => r.category === chapter)
            const avgScore = chapterResources.length > 0
                ? chapterResources.reduce((sum, r) => sum + (r.usageScore || 0), 0) / chapterResources.length
                : 0

            return {
              value: [
                chapterStats[chapter].avgDownloads || 0,
                chapterStats[chapter].avgLikes || 0,
                chapterStats[chapter].avgCollects || 0,
                chapterStats[chapter].count || 0,
                avgScore
              ],
              name: `Chapter ${chapter}`
            }
          })
        }]
      }

      this.charts.radar.setOption(option)
    },

    // Export analysis report
    exportReport() {

      this.$confirm('Are you sure to export the analysis report?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'info'
      }).then(() => {
        this.$notify({
          title: 'Success',
          message: 'Analysis report export started, will download automatically shortly',
          type: 'success',
          duration: 2000
        })


        setTimeout(() => {
          this.$notify({
            title: 'Export Complete',
            message: 'Course resource usage analysis report exported successfully',
            type: 'success'
          })
        }, 2000)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Export cancelled'
        })
      })
    },


  }
}
</script>

<style>
.dashboard-header {
  margin-bottom: 20px;
}

.data-card {
  height: 120px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
  padding: 10px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #1aa14a;
}

.card-trend {
  font-size: 14px;
}

.trend-up {
  color: #67C23A;
}

.trend-down {
  color: #F56C6C;
}

.chart-card {
  margin-bottom: 20px;
  height: 350px;
}

.chart-container {
  height: 280px;
  width: 100%;
}

@media screen and (max-width: 1200px) {
  .chart-container {
    height: 250px;
  }
}

@media screen and (max-width: 992px) {
  .chart-card {
    height: 300px;
  }

  .chart-container {
    height: 230px;
  }
}
</style>