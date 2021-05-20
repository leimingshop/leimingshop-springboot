<template>
  <div>
    <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">商家系统</el-breadcrumb-item>
        <el-breadcrumb-item><a href="/">商户管理</a></el-breadcrumb-item>
        <el-breadcrumb-item>商户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()" >
         <!-- <el-scrollbar style="height:90px;margin-right: 30px;"> -->
        <el-form-item label="商家ID：">
            <el-input v-model="dataForm.storeId" placeholder="商品ID" clearable></el-input>
        </el-form-item>
        <el-form-item  label="商家名称：">
            <el-input v-model="dataForm.storeName" placeholder="商家名称" clearable></el-input>
        </el-form-item>
        <el-form-item  label="商家等级：">
            <el-select v-model="dataForm.gradeId" clearable  placeholder="请选择">
                <el-option
                    v-for="item in storeGradeList"
                    :key="item.id"
                    :label="item.sgName"
                    :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item  label="商家类别：">
            <el-select v-model="dataForm.storeType" clearable  placeholder="请选择">
                <el-option
                    v-for="item in storeTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="操作时间：">
                <el-date-picker
                        v-model="dataForm.startTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        clearable
                        placeholder="选择开始时间">
                </el-date-picker>
                -
                <el-date-picker
                        v-model="dataForm.endTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        clearable
                        placeholder="选择结束时间">
                </el-date-picker>
        </el-form-item>
        <!-- </el-scrollbar> -->
        <el-form-item>
            <el-button  class="btn" type="primary" @click="getDataList()">查询</el-button>
            <el-button class="btn"  type="primary" plain @click="reset()" plain>重置</el-button>
        </el-form-item>
        <br />
        <el-form-item>
            <el-button type="primary"   @click="addOrAdit()">新增商户</el-button>
            <el-button type="primary"  plain @click="exportHandle()">导出</el-button>
        </el-form-item>
    </el-form>
    <el-table
	  :data="dataList"
      v-loading="dataListLoading"
      border
	  style="width: 100%">
	    <!-- <el-table-column
	    	type="index"
		    prop="$index"
		    label="序号"
		    width="70">
		</el-table-column>
	    <el-table-column
	      type="selection"
	      width="70">
	    </el-table-column> -->
		<el-table-column
		    prop="id"
		    label="商户ID"
		    width="180">
		</el-table-column>
		<el-table-column
		    prop="storeName"
		    label="商户">
            <template slot-scope="scope">
                <div style="float:left">
                    <span style="width: 40px; height: 40px;margin-right:20px;" v-if="scope.row.storeLogo">
                        <img :src="scope.row.storeLogo" alt="img" style=" object-fit: contain;width: 40px;border-radius:50%;">
                    </span>
                    <span>{{scope.row.storeName}}</span>
                </div>
		    </template>
		</el-table-column>
		<el-table-column
		    prop="account"
		    label="商户管理账号">
		</el-table-column>
		<el-table-column
		    prop="gradeName"
		    label="商家等级">
		</el-table-column>
		<el-table-column
		    prop="createDate"
		    label="创建时间"
             width="180">
		</el-table-column>
        <el-table-column
		    prop="creator"
		    label="商户类别">
            <template slot-scope="scope">
		    	<span>{{scope.row.storeType==2?'普通商户':'自营商户'}}</span>
		    </template>
		</el-table-column>
	    <el-table-column
	   		prop="address"
	    	label="操作">
		    <template slot-scope="scope">
		    	<el-button type="text" size="small" @click="showDetail(scope.row.id)">查看</el-button>
		    	<el-button type="text" size="small" @click="addOrAdit(scope.row.id)">编辑</el-button>
		    	<el-button class="artdanger" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
		    </template>
	  	</el-table-column>
	</el-table>
	<!-- 分页 -->
    <el-pagination
	    @size-change="pageSizeChangeHandle"
	    @current-change="pageCurrentChangeHandle"
	    :current-page="page"
	    :page-sizes="[10, 20, 50, 100]"
	    :page-size="limit"
	    :total="total"
	    layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import { businessPageUrl } from '@/api/url'
import { storeGrade } from '@/api/api'
  
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
          getDataListURL: businessPageUrl,
          getDataListIsPage: true,
          exportURL: '/admin-api/store/export',
          deleteURL: '/admin-api/store',
          deleteIsBatch: true,
          // deleteIsBatchKey: 'id'
      },
      dataForm: {},
      storeTypes:[
          {value: '1',label: '自营商户'},
          {value: '2',label: '普通商户'}
      ],
      storeGradeList:[],//商家等级列表
    }
  },
  created(){
      let obj = {
            params:{
                page:1,
                limit:100,
            }
        }
      storeGrade(obj).then((res)=>{
          console.log('商家等级',res)
            if(res.code == 200 && res.data.list){
                this.storeGradeList = res.data.list
            }
      })
      this.demo();
  },
  methods: {
        showDetail(id){
	    	this.$emit("showDetail",id);
        },
        addOrAdit(id){
            this.$emit("addOrAdit",id);
        },
        reset() {
            this.dataForm = {};
            this.getDataList();
        },
        demo(){
        	function placeholderPic(){
						var w = document.documentElement.offsetWidth;
						document.documentElement.style.fontSize=w/20+'px';
					}
						placeholderPic();
					window.onresize=function(){
						placeholderPic();
					}
        }
        
  }
};
</script>
<style lang="scss" scoped>
.el-input {
  width: 170px;
  height: 40px;
}
</style>
