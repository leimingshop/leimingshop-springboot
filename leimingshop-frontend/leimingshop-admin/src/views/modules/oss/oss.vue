<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
    <div class="mod-oss__oss">
      <el-form :inline="true" :model="dataForm">
        <el-form-item>
          <el-button type="primary" plain @click="configHandle()">{{ $t('oss.config') }}</el-button>
          <el-button type="primary" @click="uploadHandle()">{{ $t('oss.upload') }}</el-button>
          <el-button type="danger" plain @click="deleteHandle()">{{ '批量' + $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
        style="width: 100%;"
        :height="tableheight" >
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="url" :label="$t('oss.url')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="createDate" :label="$t('oss.createDate')" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
      <!-- 弹窗, 云存储配置 -->
      <config v-if="configVisible" ref="config"></config>
      <!-- 弹窗, 上传文件 -->
      <upload v-if="uploadVisible" ref="upload" @refreshDataList="getDataList"></upload>
    </div>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import Config from './oss-config'
import Upload from './oss-upload'
import Bread from "@/components/bread";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        activatedIsNeed:false,
        getDataListURL: '/admin-api/oss/file/page',
        getDataListIsPage: true,
        deleteURL: '/admin-api/oss/file',
        deleteIsBatch: true
      },
      breaddata: ["系统管理", "文件上传"],
      dataForm: {},
      configVisible: false,
      uploadVisible: false,
      tableheight:document.body.offsetHeight-350,
    }
  },
  components: {
    Config,
    Upload,
    Bread
  },
  methods: {
    // 云存储配置
    configHandle () {
      this.configVisible = true
      this.$nextTick(() => {
        this.$refs.config.init()
      })
    },
    // 上传文件
    uploadHandle () {
      this.uploadVisible = true
      this.$nextTick(() => {
        this.$refs.upload.init()
      })
    }
  }
}
</script>
