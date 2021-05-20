<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-message__sms">
      <el-form :inline="true" :model="dataForm"  class="grayLine" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.mobile" :placeholder="$t('sms.mobile')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="dataForm.status" :placeholder="$t('sms.status')" clearable>
            <el-option :label="$t('sms.status1')" :value="1"></el-option>
            <el-option :label="$t('sms.status0')" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
      </el-form>
      
      <el-form>
      	<el-form-item>
          <el-button type="primary" plain @click="configHandle()">{{ $t('sms.config') }}</el-button>
          <el-button type="primary" plain @click="sendHandle()">{{ $t('sms.send') }}</el-button>
          <el-button type="danger" plain @click="deleteHandle()">{{ '批量' + $t('deleteBatch') }}</el-button>
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
        <el-table-column prop="platform" :label="$t('sms.platform')" sortable="custom" header-align="center" align="center">
          <template slot-scope="scope">
            {{ scope.row.platform === 1 ? $t('sms.platform1') : scope.row.platform === 2 ? $t('sms.platform2') : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="mobile" :label="$t('sms.mobile')" header-align="center" align="center" width="120"></el-table-column>
        <el-table-column prop="params1" :label="$t('sms.params1')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="params2" :label="$t('sms.params2')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="params3" :label="$t('sms.params3')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="params4" :label="$t('sms.params4')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="status" :label="$t('sms.status')" sortable="custom" header-align="center" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1" size="small">{{ $t('sms.status1') }}</el-tag>
            <el-tag v-else size="small" type="danger">{{ $t('sms.status0') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" :label="$t('sms.createDate')" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" class="artdanger" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
      <!-- 弹窗, 短信配置 -->
      <config v-if="configVisible" ref="config"></config>
      <!-- 弹窗, 发送短信 -->
      <send v-if="sendVisible" ref="send" @refreshDataList="getDataList"></send>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import Config from './sms-config'
import Send from './sms-send'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/message/sms/page',
        getDataListIsPage: true,
        deleteURL: '/message/sms',
        deleteIsBatch: true
      },
      dataForm: {
        mobile: '',
        status: null
      },
      configVisible: false,
      sendVisible: false
    }
  },
  components: {
    Config,
    Send
  },
  methods: {
    // 短信配置
    configHandle () {
      this.configVisible = true
      this.$nextTick(() => {
        this.$refs.config.init()
      })
    },
    // 发送短信
    sendHandle () {
      this.sendVisible = true
      this.$nextTick(() => {
        this.$refs.send.init()
      })
    }
  }
}
</script>
