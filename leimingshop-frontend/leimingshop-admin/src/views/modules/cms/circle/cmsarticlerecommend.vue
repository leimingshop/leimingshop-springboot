<template>
    <div>
      <Bread  :breaddata="breaddata"></Bread>
      <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()">
  
      <el-form-item label="ID：">
          <el-input v-model="dataForm.id" placeholder="ID" clearable></el-input>
      </el-form-item>
    
      <el-form-item label="主文章ID：">
          <el-input v-model="dataForm.mainArticleId" placeholder="主文章ID" clearable></el-input>
      </el-form-item>
    
      <el-form-item label="被推荐文章ID：">
          <el-input v-model="dataForm.recommendArticleId" placeholder="被推荐文章ID" clearable></el-input>
      </el-form-item>
              <el-form-item>
            <el-button  calss="btn" type="primary" @click="getDataList()">查询</el-button>
            <el-button  calss="btn"  @click="reset()" type="primary" plain>重置</el-button>
        </el-form-item>

      </el-form>

        <el-form>
          <el-form-item>
              <el-button  type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
              <el-button  type="danger" plain @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
          </el-form-item>
        </el-form>

      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="ID" header-align="center" align="center"></el-table-column>
          <el-table-column prop="mainArticleId" label="主文章ID" header-align="center" align="center"></el-table-column>
          <el-table-column prop="recommendArticleId" label="被推荐文章ID" header-align="center" align="center"></el-table-column>
              <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button  type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
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
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './cmsarticlerecommend-add-or-update'
import Bread from "@/components/bread"
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/cart/cmsarticlerecommend/page',
        getDataListIsPage: true,
        deleteURL: '/cart/cmsarticlerecommend',
        deleteIsBatch: true
      },
      dataForm: {
        id: ''
      },
      breaddata: ["一级菜单", "二级菜单", "三级菜单"]
    }
  },
  components: {
    AddOrUpdate,
    Bread
  }
}
</script>
