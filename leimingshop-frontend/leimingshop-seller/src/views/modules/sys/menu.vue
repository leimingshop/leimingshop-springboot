<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-sys__menu">
         <Bread :breaddata="breaddata"></Bread>
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-button  type="primary" @click="addOrUpdateHandle()"> 新增</el-button>
        </el-form-item>
      </el-form>
      <el-table class="sysMenuClass" v-loading="dataListLoading" :data="dataList" border style="width: 100%;" row-key="id">
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
        <table-tree-column prop="name" :label="$t('menu.name')" header-align="center" width="250"></table-tree-column>
        <el-table-column prop="icon" :label="$t('menu.icon')" header-align="center" align="center">
          <template slot-scope="scope">
            <svg class="icon-svg" aria-hidden="true"><use :xlink:href="`#${scope.row.icon}`"></use></svg>
          </template>
        </el-table-column>
        <el-table-column prop="type" :label="$t('menu.type')" header-align="center" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 0" size="small">{{ $t('menu.type0') }}</el-tag>
            <el-tag v-else size="small" type="info">{{ $t('menu.type1') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" :label="$t('menu.sort')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="url" :label="$t('menu.url')" header-align="center" align="center" width="150" :show-overflow-tooltip="true" ></el-table-column>
        <el-table-column prop="permission" :label="$t('menu.permission')" header-align="center" align="center" width="150" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column  :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button  type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import TableTreeColumn from '@/components/table-tree-column'
import AddOrUpdate from './menu-add-or-update'
import { delMenu } from '@/api/url'
import Bread from "@/components/bread";
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/seller-api/menu/find/all/menu',
        deleteURL: delMenu,
        deleteIsBatch:true,
      },
        breaddata: ["店铺管理", "菜单管理"],
    }
  },
  components: {
    TableTreeColumn,
    AddOrUpdate,
    Bread
  },
    methods: {
  }
}
</script>

<style lang="scss">
 .mod-sys__menu {
  .resource-list {
    .el-select .el-input__inner {
      min-width: 110px;
      text-align: center;
    }
  }
 }
.sysMenuClass{
   .el-table__expand-icon {
    display: inline-block;
    margin-right: 8px;
    height: 13px;

  }
  .el-icon-caret-right {
        display: none;
  }
}

</style>