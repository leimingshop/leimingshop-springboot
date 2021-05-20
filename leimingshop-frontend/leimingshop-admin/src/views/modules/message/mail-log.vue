<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
    <div class="mod-message__mail-log">
      <el-form :inline="true" :model="dataForm" class="grayLine" @keyup.enter.native="getData()">
        <el-form-item label="模板：">
          <el-input v-model="dataForm.templateId" :placeholder="$t('mail.templateId')" clearable></el-input>
        </el-form-item>
        <el-form-item label="收件人：">
          <el-input v-model="dataForm.mailTo" :placeholder="$t('mail.mailTo')" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态：">
          <el-select v-model="dataForm.status" :placeholder="$t('mail.status')" clearable>
            <el-option :label="$t('mail.status1')" :value="1"></el-option>
            <el-option :label="$t('mail.status0')" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData()">{{ $t('query') }}</el-button>
        </el-form-item>
        <br />
      </el-form>
      <el-form>
      	<el-form-item>
          <el-button type="danger" plain @click="deleteHandle()" v-if="$hasPermission('sys:maillog:delete')">{{$t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
        style="width: 100%;"
        :height="tableheight"
        >
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="templateId" :label="$t('mail.templateId')" sortable="custom" header-align="center" align="center"></el-table-column>
        <el-table-column prop="mailFrom" :label="$t('mail.mailFrom')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="mailTo" :label="$t('mail.mailTo')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="mailCc" :label="$t('mail.mailCc')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="subject" :label="$t('mail.subject')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="status" :label="$t('mail.status')" sortable="custom" header-align="center" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1" size="small">{{ $t('mail.status1') }}</el-tag>
            <el-tag v-else size="small" type="danger">{{ $t('mail.status0') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" :label="$t('mail.createDate')" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)"  v-if="$hasPermission('sys:maillog:delete')">{{ $t('delete') }}</el-button>
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
    </div>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import Bread from "@/components/bread";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/admin-api/maillog/page',
        getDataListIsPage: true,
        deleteURL: '/admin-api/maillog',
        deleteIsBatch: true
      },
      breaddata: ["系统管理", "消息管理",'邮件发送记录'],
      dataForm: {
        templateId: '',
        mailTo: '',
        status: null
      },
      tableheight:document.body.offsetHeight-420,
    }
  },
  components: {
    Bread
  },
  methods: {
     getData(){
      this.page = 1;
      this.limit = 10;
      this.getDataList();
    },
  }
}
</script>
