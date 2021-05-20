<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-sys__dict">
      <el-form :inline="true" class="grayLine" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.dictName" :placeholder="$t('dict.dictName')" clearable></el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.pid === '0'">
          <el-input v-model="dataForm.dictType" :placeholder="$t('dict.dictType')" clearable></el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.pid !== '0'">
          <el-input v-model="dataForm.dictValue" :placeholder="$t('dict.dictValue')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        
      </el-form>
      <el-form>
      	<el-form-item>
          <el-button v-if="$hasPermission('sys:dict:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
          <el-button v-if="$hasPermission('sys:dict:delete')" type="danger" plain @click="deleteHandle()">{{ '批量' + $t('deleteBatch') }}</el-button>
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
        <el-table-column prop="dictName" :label="$t('dict.dictName')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="dictType" :label="$t('dict.dictType')" header-align="center" align="center">
          <template slot-scope="scope">
            <span v-if="dataForm.pid !== '0'">{{ scope.row.dictType }}</span>
            <el-button v-else type="text" @click="childHandle(scope.row)">{{ scope.row.dictType }}</el-button>
          </template>
        </el-table-column>
        <el-table-column v-if="dataForm.pid !== '0'" prop="dictValue" label="字典值" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" :label="$t('dict.sort')" sortable="custom" header-align="center" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('dict.remark')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="createDate" :label="$t('dict.createDate')" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
        <el-table-column  :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('sys:dict:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('sys:dict:delete')" type="text" size="small" class="artdanger" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-if="dataForm.pid === '0'"
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
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './dict-add-or-update'
import { moduleRoutes } from '@/router'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        activatedIsNeed: false,
        getDataListURL: '/admin-api/dict/page',
        getDataListIsPage: true,
        deleteURL: '/admin-api/dict',
        deleteIsBatch: true
      },
      dataForm: {
        pid: '0',
        dictName: '',
        dictType: '',
        dictValue: ''
      }
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    // 通过路由参数pid, 控制列表请求操作
    this.dataForm.pid = this.$route.params.pid || '0'
    if (this.dataForm.pid !== '0') {
      this.mixinViewModuleOptions.getDataListURL = '/admin-api/dict/list'
      this.mixinViewModuleOptions.getDataListIsPage = false
      this.dataForm.dictType = this.$route.params.type || ''
    }
    this.getDataList()
  },
  methods: {
    // 子级
    childHandle (row) {
      // 组装路由名称, 并判断是否已添加, 如是: 则直接跳转
      var routeName = `${this.$route.name}__${row.id}`
      var route = window.SITE_CONFIG['dynamicRoutes'].filter(item => item.name === routeName)[0]
      if (route) {
        return this.$router.push({ name: routeName, params: { 'pid': row.id } })
      }
      // 否则: 添加并全局变量保存, 再跳转
      route = {
        path: routeName,
        component: () => import(`@/views/modules/${this.$route.name.replace(/-/g, '/')}`),
        name: routeName,
        meta: {
          ...window.SITE_CONFIG['contentTabDefault'],
          menuId: this.$route.meta.menuId,
          title: `${this.$route.meta.title} - ${row.dictType}`
        }
      }
      this.$router.addRoutes([
        {
          ...moduleRoutes,
          name: `main-dynamic__${route.name}`,
          children: [route]
        }
      ])
      window.SITE_CONFIG['dynamicRoutes'].push(route)
      this.$router.push({ name: route.name, params: { 'pid': row.id, 'type': row.dictType } })
    },
    // 新增 / 修改
    addOrUpdateHandle (row = {}) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.dataForm.id = row.id
        this.$refs.addOrUpdate.dataForm.pid = this.dataForm.pid
        this.$refs.addOrUpdate.dataForm.dictType = row.dictType || this.dataForm.dictType || ''
        this.$refs.addOrUpdate.init()
      })
    }
  }
}
</script>
