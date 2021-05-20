<template>
  <div class="menuPage">
    <Bread :breaddata="breaddata"></Bread>

    <div class="mod-sys__menu">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-button v-if="$hasPermission('sys:menu:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
      </el-form>
      <!-- row-key="id" -->
      <el-table row-key="id" v-loading="dataListLoading" :data="dataList" border 
        style="width: 100%;"
        :height="tableheight" >
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
        <el-table-column prop="name" :label="$t('menu.name')" header-align="center" width="250"></el-table-column>
        <el-table-column prop="icon" :label="$t('menu.icon')" header-align="center" align="center">
          <template slot-scope="scope">
            <svg class="icon-svg" aria-hidden="true"><use :xlink:href="`#${scope.row.icon}`"></use></svg>
          </template>
        </el-table-column>
        <!-- <el-table-column
            type="selection"
            width="70">
        </el-table-column> -->
        <el-table-column prop="type" :label="$t('menu.type')" header-align="center" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 0" size="small">{{ $t('menu.type0') }}</el-tag>
            <el-tag v-else size="small" type="info">{{ $t('menu.type1') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" :label="$t('menu.sort')" header-align="center" align="center"></el-table-column>
        <el-table-column prop="url" :label="$t('menu.url')" header-align="center" align="center" width="150" :show-overflow-tooltip="true" ></el-table-column>
        <el-table-column prop="permissions" :label="$t('menu.permissions')" header-align="center" align="center" width="150" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column  :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('sys:menu:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('sys:menu:delete')" type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import TableTreeColumn from '@/components/table-tree-column'
import AddOrUpdate from './menu-add-or-update'
import Bread from "@/components/bread";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/admin-api/menu/list?type=',
        deleteURL: '/admin-api/menu'
      },
      breaddata: ["系统管理", "菜单管理"],
      tableheight:document.body.offsetHeight-300,
    }
  },
  components: {
    TableTreeColumn,
    AddOrUpdate,
    Bread
  }
}
</script>

<style lang="scss">
  .menuPage{
    /deep/.el-table__row{
      td:nth-child(2){
        .cell{
          display: flex;
          .el-table__expand-icon{
            margin-right: 10px;
          }
        }
      }
    }
  }

</style>
