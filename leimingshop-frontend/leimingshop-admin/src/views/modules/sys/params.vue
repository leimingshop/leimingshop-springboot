<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
    <div class="mod-sys__params">
      <el-form :inline="true" :model="dataForm" class="grayLine" @keyup.enter.native="getData()">
        <el-form-item label="编码：">
          <el-input  v-model="dataForm.paramCode" :placeholder="$t('params.paramCode')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData()">{{ $t('query') }}</el-button>
        </el-form-item>
        <br />
      </el-form>
      <el-form>
      	<el-form-item>
          <el-button v-if="$hasPermission('sys:params:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
          <el-button v-if="$hasPermission('sys:params:delete')" type="danger" plain @click="deleteHandle()">{{'批量' + $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle"
         style="width: 100%;"
         :height="tableheight" >
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column
            type="index"
            prop="$index"
            label="序号"
            align="center"
            width="70">
            <template slot-scope="scope">
            {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
            </template>
        </el-table-column>
        <el-table-column prop="paramCode" :label="$t('params.paramCode')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="paramValue" :label="$t('params.paramValue')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('params.remark')" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('sys:params:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('sys:params:delete')" type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './params-add-or-update'
import Bread from "@/components/bread";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/admin-api/params/page',
        getDataListIsPage: true,
        deleteURL: '/admin-api/params',
        deleteIsBatch: true
      },
      breaddata: ["系统管理", "参数管理"],
      dataForm: {
        paramCode: ''
      },
      tableheight:document.body.offsetHeight-420,
    }
  },
  components: {
    AddOrUpdate,
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
