<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-activiti__process">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.processName" :placeholder="$t('process.name')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="dataForm.key" :placeholder="$t('process.key')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataList()">{{ $t('query') }}</el-button>
          <el-button type="primary" @click="deployHandle()">{{ $t('process.deployFile') }}</el-button>
          <el-button type="danger" plain @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
        style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" :label="$t('process.id')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="deploymentId" :label="$t('process.deploymentId')" header-align="center" align="center" width="80"></el-table-column>
        <el-table-column prop="name" :label="$t('process.name')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="key" :label="$t('process.key')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="version" :label="$t('process.version')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="resourceName" :label="$t('process.resourceName')" header-align="center" align="center" :show-overflow-tooltip="true" width="100">
          <template slot-scope="scope">
            <a :href="getResourceURL(scope.row.deploymentId, scope.row.resourceName)" target="_blank">{{ scope.row.resourceName }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="diagramResourceName" :label="$t('process.diagramResourceName')" header-align="center" align="center" :show-overflow-tooltip="true" width="100">
          <template slot-scope="scope">
            <a :href="getResourceURL(scope.row.deploymentId, scope.row.diagramResourceName)" target="_blank">{{ scope.row.diagramResourceName }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="deploymentTime" :label="$t('process.deploymentTime')" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="scope.row.suspended" type="text" size="small" @click="activeHandle(scope.row.id)">{{ $t('process.active') }}</el-button>
            <el-button v-else type="text" size="small" @click="suspendHandle(scope.row.id)">{{ $t('process.suspend') }}</el-button>
            <el-button type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.deploymentId)">{{ $t('delete') }}</el-button>
            <el-button type="text" size="small" @click="convertToModelHandle(scope.row.id)">{{ $t('process.convertToModel') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle">
      </el-pagination>
      <!-- 弹窗, 部署流程文件 -->
      <deploy v-if="deployVisible" ref="deploy" @refreshDataList="getDataList"></deploy>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import Deploy from './process-deploy'
import Cookies from 'js-cookie'
import qs from 'qs'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/activiti/process/page',
        getDataListIsPage: true,
        deleteURL: '/activiti/process',
        deleteIsBatch: true,
        deleteIsBatchKey: 'deploymentId'
      },
      dataForm: {
        processName: '',
        key: ''
      },
      deployVisible: false
    }
  },
  components: {
    Deploy
  },
  methods: {
    // 获取流程(xml/image)url地址
    getResourceURL (id, name) {
      var params = qs.stringify({
        'token': Cookies.get('ADMIN-TOKEN'),
        'deploymentId': id,
        'resourceName': name
      })
      return `${window.SITE_CONFIG['apiURL']}/activiti/process/resource?${params}`
    },
    // 部署流程文件
    deployHandle () {
      this.deployVisible = true
      this.$nextTick(() => {
        this.$refs.deploy.init()
      })
    },
    // 激活
    activeHandle (id) {
      this.$confirm(this.$t('prompt.info', { 'handle': this.$t('process.active') }), this.$t('prompt.title'), {
        confirmButtonText: this.$t('confirm'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$http.put(`/activiti/process/active/${id}`).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.getDataList()
            }
          })
        }).catch(() => {})
      }).catch(() => {})
    },
    // 挂起
    suspendHandle (id) {
      this.$confirm(this.$t('prompt.info', { 'handle': this.$t('process.suspend') }), this.$t('prompt.title'), {
        confirmButtonText: this.$t('confirm'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$http.put(`/activiti/process/suspend/${id}`).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.getDataList()
            }
          })
        }).catch(() => {})
      }).catch(() => {})
    },
    // 转换为模型
    convertToModelHandle (id) {
      this.$confirm(this.$t('prompt.info', { 'handle': this.$t('process.convertToModel') }), this.$t('prompt.title'), {
        confirmButtonText: this.$t('confirm'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$http.post(`/activiti/process/convertToModel/${id}`).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.getDataList()
            }
          })
        }).catch(() => {})
      }).catch(() => {})
    }
  }
}
</script>
