<template>
  <div>

      <el-form style="margin-top:0px;">
        <el-form-item>
           <el-button type="primary"  @click="addOrEditHandle()" >售后模板</el-button>
           <!-- <el-button type="danger" plain @click="deleteHandle()" >批量删除</el-button> -->
        </el-form-item>
      </el-form>
      
      <el-table
        width="100%"
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%"
        >  
          <el-table-column
          label="序号"
          width="70"
          align="center">
            <template slot-scope="scope">
              {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
            </template>
        </el-table-column>
        
        <el-table-column
		      prop="name"
		      label="模板名称"
		      align="center">
		    </el-table-column>

         <el-table-column
		      prop="createDate"
		      label="创建时间"
		      align="center">
		    </el-table-column>

         <el-table-column
		      prop="updateDate"
		      label="修改时间"
		      align="center">
		    </el-table-column>

        <el-table-column
		      label="操作"
		      align="center">
            <template slot-scope="scope">
			         <el-button @click.native.prevent="addOrEditHandle(scope.$index, scope.row)"type="text"size="mini">编辑</el-button>
              <!-- <el-button class="artdanger" @click.native.prevent="deleteHandle(scope.row.id)"type="text"size="mini">删除</el-button> -->
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

       	<!-- 弹窗, 新建 -->
	    	<addEditData  v-if="addEditDataVisible" ref="addEditData" @searchDataList="getDataList"></addEditData>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import { aftertemplatePageUrl,deleteAftertemplateUrl } from '@/api/url'
  
import addEditData from './model-add-edit-data'


export default {
  mixins: [mixinViewModule],
  data () {
    return {
    mixinViewModuleOptions: {
      getDataListURL: aftertemplatePageUrl,
      getDataListIsPage: true,
      // exportURL: '/admin-api/log/login/export',
      // deleteURL: deleteAftertemplateUrl ,
      // deleteIsBatch: true,
      // deleteIsBatchKey: 'id'
    },
    addEditDataVisible:false,
    dataForm:{},
    }
  },
  components: {
    addEditData,
  },
  created () {
  },
  mounted(){
    // this.getDataList();
  },
  methods: {
    // 新建和编辑
    addOrEditHandle(index=-1,row=""){
       this.setAddEditDataVisible(true);
        this.$nextTick(() => {
          this.$refs.addEditData.init(row)
        })
    },
    setAddEditDataVisible(boolargu){
      this.addEditDataVisible =  boolargu;
    },
  }
}
</script>
