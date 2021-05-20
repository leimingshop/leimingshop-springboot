<template>

  <div>
      <Bread  :breaddata="breaddata"  @changePage="changePage"  :index="'2'" ></Bread>

      <!--导出按钮-->
      <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"></importAndExport>
       <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="输入搜索：">
        <el-input v-model="dataForm.goodsName" placeholder="商品名称/商品编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="btn" type="primary" @click="getDataList()">查询</el-button>
        <el-button class="btn" @click="reset()" type="primary" plain>重置</el-button>
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
         <el-table-column label="商品信息" align="center" width="380">
            <template slot-scope="scope">
            <div class="goodsPropsWrap">
                <div class="goodsImg">
                <img :src="scope.row.specMainPicture | filterImgUrl" alt=""/>
                </div>
                <div class="goodsProps">
                    <span class="goodsName">
                        <label class="goodsNameTitle">名称:</label>
                        <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                    placement="top-start">

                            <span class="goodsNameContent" @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                        </el-tooltip>
                    </span>
                <span class="goodsBrand">
                    <label class="goodsNameTitle">品牌:</label>
                    <span class="goodsBrandName">{{scope.row.brandName}}</span>
                </span>
                <span class="goodsNum">
                    <label class="goodsNameTitle">sku:</label>
                    <span class="goodsBrandName">{{scope.row.sku}}</span>
                </span>
                </div>
            </div>
            </template>
      </el-table-column>

        <el-table-column
                    prop="specName"
                    label="规格"
                    align="center">
                     <template slot-scope="scope">
                    <div v-if="scope.row.specName">{{scope.row.specName}}</div>
                    <div v-else>无规格</div>
                </template>
            </el-table-column>

        <el-table-column
		      label="修改前（销售价\成本价）"
          width="220"
		      align="center">
              <template slot-scope="scope">
                <div class="priceWrap">
                    <div class="price1">￥{{scope.row.beforeMarketPrice}}</div>
                    <div class="price2">￥{{scope.row.beforeCostPrice}}</div>
                </div>
              </template>
		    </el-table-column>

        <el-table-column
		      label="修改后（销售价\成本价）"
          width="220"
		      align="center">
             <template slot-scope="scope">
                <div class="priceWrap">
                    <div class="price1">￥{{scope.row.afterMarketPrice}}</div>
                    <div class="price2">￥{{scope.row.afterCostPrice}}</div>
                </div>
              </template>
		    </el-table-column>

          <el-table-column
                  prop="updater"
                  label="修改人"
                  align="center">
          </el-table-column>

        <el-table-column
		      prop="updateDate"
		      label="修改时间"
          width="180"
		      align="center">
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
import importAndExport from "@/components/import-and-export"
import { priceLogExport } from '@/api/io'

import addEditData from './model-add-edit-data'
import { priceLogPageUrl } from '@/api/url'
  // import { deletepriceLogUrl } from '@/api/url'
import Bread from "@/components/bread";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
       breaddata: ["商品", "商品管理", "价格管理","修改记录"],
      /*导出*/
      importAndExportOptions:{
        exportUrl:priceLogExport,//导出接口
        exportWord:"导出",
      },
    mixinViewModuleOptions: {
      activatedIsNeed: false,
      getDataListURL: priceLogPageUrl,
      getDataListIsPage: true,
      // exportURL: '/admin-api/log/login/export',
      // deleteURL: deletepriceLogUrl,
      // deleteIsBatch: true,
      // deleteIsBatchKey: 'id'
    },
    addEditDataVisible:false,
    dataForm:{
      goodsName:''
    },
    }
  },
  components: {
    addEditData,
    Bread,
    importAndExport
  },
  created () {
  },
  mounted(){
  },
  methods: {
    init(id){
      this.dataForm.goodsId = id;
      this.getDataList();
    },
    previewH5Fn(row){
      window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.id+'&specId='+row.sku);
    },
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
    changePage(){
      this.$emit("showListFn");
    },
    // 重置
    reset(){
        this.dataForm.goodsName = "";
        this.getDataList()
    },
  }
}
</script>
<style lang="scss"  scoped>
@import "@/element-ui/theme-variables.scss";
// 价格
.priceWrap{
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  .price1{
    color: $--color-primary;
  }
  div{
    width: 100%;
    text-align: right;
  }

}
     .towEllipsis {
    text-align: left;
    text-overflow: -o-ellipsis-lastline;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .goodsPropsWrap {
    margin: auto;
    height: 80px;
    width: 330px;
    display: flex;
    justify-content: space-around;
    align-items: center;

    .goodsImg {
      width: 70px;
      height: 70px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .goodsProps {
      width: 210px;
      height: auto;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      color: #999999;

      .goodsNameTitle {
        display: inline-block;
        width: 50px;
        color: #666666FF;
      }

      .goodsBrandName {
        text-align: left;
      }

      .goodsName {
        display: flex;
            height: 41px;

        .goodsNameContent {
          width: 150px;
          text-align: left;
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          -webkit-box-orient: vertical;
          color: #4e80db;
          text-decoration: none;
          cursor: pointer;
        }

      }
    }
  }
</style>
