<template>
  <div>
      <el-table
        width="100%"
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        >
        <el-table-column
          type="selection"
          width="70">
        </el-table-column>

        <el-table-column
          label="序号"
          width="70"
          align="center">
            <template slot-scope="scope">
              {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
            </template>
        </el-table-column>

		    <!-- <el-table-column
		      prop="specSerial"
		      label="SKU"
          width="180"
		      align="center">
		    </el-table-column>

        <el-table-column
		      label="图片"
		      align="center"
          width="120">
            <template slot-scope="scope">
                  <img :src="scope.row.specMainPicture | filterImgUrl" alt="" style="width:60px;height:60px;object-fit: contain;">
            </template>
		    </el-table-column>

        <el-table-column
		      prop="goodsName"
		      label="商品名称"
          width="180"
		      align="center">
          <template slot-scope="scope">
            <div class="towEllipsis" @click="previewH5Fn(scope.row)" style="margin-left: 8px;" >
              <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                <span  style="color: #4e80db;text-decoration: none; cursor: pointer;" >{{scope.row.goodsName}}</span>
              </el-tooltip>
            </div>
           </template>
		    </el-table-column>

        <el-table-column
		      prop="brandName"
		      label="品牌"
		      align="center" >
		    </el-table-column> -->
         <el-table-column label="商品信息" align="center" width="380">
            <template slot-scope="scope">
            <div class="goodsPropsWrap">
                <div class="goodsImg">
                <img :src="scope.row.specMainPicture | filterImgUrl" alt=""/>
                </div>
                <div class="goodsProps">
                    <span class="goodsName">
                        <label class="goodsNameTitle">名称:</label>
                        <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" v-if="scope.row.goodsName.length>20"
                                    placement="top-start">

                            <span class="goodsNameContent" @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                        </el-tooltip>
                        <span v-else class="goodsNameContent" @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                    </span>
                <span class="goodsBrand">
                    <label class="goodsNameTitle">品牌:</label>
                    <span class="goodsBrandName">{{scope.row.brandName}}</span>
                </span>
                <span class="goodsNum">
                    <label class="goodsNameTitle">sku:</label>
                    <span class="goodsBrandName">{{scope.row.id}}</span>
                </span>
                </div>
            </div>
            </template>
      </el-table-column>

       <el-table-column
		      prop="price"
		      label="销售价/成本价"
		      align="right"
          min-width="120">
            <template slot-scope="scope">
              <div class="priceWrap">  
                  <div class="price1">￥{{scope.row.specSellPrice}}</div>
                  <div class="price2">￥{{scope.row.specCostPrice}}</div>
              </div>
            </template>
		    </el-table-column>

         <el-table-column
          prop="gcName"
		      label="后台分类"
		      align="center"
         >
            <template slot-scope="scope">
              <div>
                 {{scope.row.gcName}}
              </div>
            </template>
		    </el-table-column>

        <el-table-column
		      prop="specAttrName"
		      label="规格"
		      align="center">
		    </el-table-column>

		    <el-table-column
		      label="操作"
		      align="center"
          width="140"
		      >
            <template slot-scope="scope">
			        <el-button @click="showSkuModel(scope.row)" type="text" size="mini">编辑</el-button>
              <el-button  @click="cotrolSkuShow('singe',scope.row)" type="text" size="mini">
                  <span  v-if="scope.row.specShow==0 || scope.row.specShow==2" class="artstart">上架</span>
                  <span  v-if="scope.row.specShow==1" class="artclose">下架</span>
                  <!-- <span  v-if="scope.row.specShow==2"   style="color:#FF0000">未上架</span> -->

              </el-button>
              <!-- <div class="btnWrap">
                <div  class="editWrap" >
                    <svg class="icon-svg btsvg" aria-hidden="true"><use xlink:href="#icon-edit-square"></use></svg>
                    <span>编辑</span>
                </div>

                <div class="upDownWrap" @click="cotrolSkuShow('singe',scope.row)">
                    <span v-if="scope.row.specShow==1" style="color:#0B9D27;" >
                        <svg class="icon-svg btsvg" aria-hidden="true"><use xlink:href="#icon-vertical-align-top"></use></svg>
                        <span>下架</span>
                    </span>
                    <span v-else style="color:#FF0000" >
                        <svg class="icon-svg btsvg" aria-hidden="true"><use xlink:href="#icon-vertical-align-botto"></use></svg>
                        <span>上架</span>
                    </span>
                </div>
              </div> -->
			      </template>
		    </el-table-column>
	  </el-table>
    <div  class="bottomFun"> 
        <div class="bottomFunLeft">
            <!-- <el-checkbox v-model="checked" @change="change">全选</el-checkbox> -->
           <div class="grayBtnWarp">
              <el-button type="primary" plain @click="cotrolSkuShow('batch',1)">上架</el-button>
              <el-button type="info" plain @click="cotrolSkuShow('batch',0)">下架</el-button>
              <!-- <el-button type="danger" plain @click="deleteHandle()">删除</el-button> -->
           </div>
        </div>
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

    <skuData v-if="skuDataVisible" ref="skuDataCompon" @searchDataList="getDataList" ></skuData>

    
    
    <skuOnOff v-if="skuOnOffVisible" ref="skuOnOffCompon" @searchDataList="getDataList" ></skuOnOff>

  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'

import skuData from './model-sku-data'
 import skuOnOff from './modules/model-sku-on-off.vue'
import {goodsUpdateCheckActivity} from "@/api/api.js";
import { goodsSpecPage } from '@/api/url'

export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
          getDataListURL: goodsSpecPage,
          activatedIsNeed:false,
          getDataListIsPage: true,
          // exportURL: '/admin-api/log/login/export',
          // deleteURL: deleteGoodsUrl,
          // deleteIsBatch: true,
          // deleteIsBatchKey: 'id'
      },
      checked:false,
      skuDataVisible:false,
      multipleSelection:[],
      dataForm:{},
       skuOnOffVisible:false,
    }
  }, 
  components: {
      skuData,
      skuOnOff
	},
  created () {
  },
  methods: {
    init(dataForm){
        Object.assign(this.dataForm,dataForm)
        this.page=1;
        this.limit=10;
        this.getDataList();
    },
    reset () {

    },
    editRow(){
      
    },
    showSkuModel(row){
    // 查询商品是否参加活动
        let obj =  [row.goodsId];
        goodsUpdateCheckActivity(obj).then(res => {
            if (res.code == 200) {
                if (res.data.activityFlag == 0) {
                    // 未参加促销
                    this.skuDataVisible = true;
                    row.goodsName = this.dataForm.goodsName
                    this.$nextTick(()=>{
                        this.$refs.skuDataCompon.init(row);
                    })
                } else if (res.data.activityFlag == 1) {
                    // 促销未开始
                    this.$confirm(res.data.operationMsg, '提示', {
                        cancelButtonText: '取消',
                        confirmButtonText: '确定',
                        type: 'warning'
                    }).then(() => {
                        this.skuDataVisible = true;
                        row.goodsName = this.dataForm.goodsName
                        this.$nextTick(()=>{
                            this.$refs.skuDataCompon.init(row);
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    });
                } else if (res.data.activityFlag == 2) {
                    // 促销进行中
                    this.$message({
                        type: 'warning',
                        message: res.data.operationMsg
                    });
                } else {
                    this.$message({
                        message: '服务器异常',
                        type: 'error',
                        duration: 1500,
                    })
                }
            } else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    duration: 1500,
                })
            }
        })
    },
    change(tab){
        console.log(tab);
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
       this.dataListSelectionChangeHandle(val);
    },
    getIds(){
      var ids= [];
      console.log(this.multipleSelection);
      this.multipleSelection.forEach((item,index)=>{
          if("object" == typeof(item)){
            ids.push(item.id);
          }else{
            ids.push(id);
          }
      })
      return ids;
    },
      previewH5Fn(row){
      window.open(window.SITE_CONFIG['pcURL']+'/goodsDetails?goodsId='+row.id+'&specId='+row.id);
    },
    // 控制上下架
    cotrolSkuShow(type,rowOrstatus){
        var ids= [];
        var specShow = 0;
        var goodsId = '';
        if(type=="batch"){ //批量
          if(this.multipleSelection.length==0){
            this.$message({
              message: "请选择商品",
              type:"warning",
              duration: 1500,
            })
            return
          }
          ids = this.getIds();
          goodsId=this.multipleSelection[0].goodsId;
          specShow = rowOrstatus
        }else{ //单个
          ids = [rowOrstatus.id]
          goodsId=rowOrstatus.goodsId;
          specShow = rowOrstatus.specShow==1?0:1;
        }

        this.skuOnOffVisible = true;
        this.$nextTick(()=>{
          this.visible = false;
          this.$refs.skuOnOffCompon.init(rowOrstatus,ids,goodsId,specShow);
        })
    },
      
  }
}
</script>
<style lang="scss"  scoped>
@import "@/element-ui/theme-variables.scss";
// 表头背景和字体颜色
/deep/ .el-table__header  th{
  background:#f5f7fa;
}
// 勾选表格复选框居中
/deep/ tr .cell{
    display: flex;
    justify-content: center
}
// 表内字体默认颜色
/deep/ .el-table__row{
 color: #999999FF;
 font-size: 14px;
}

// 复选框颜色改正
/deep/ .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner{
    // background-color: #666666 !important;
    // border-color: #666666 !important;
}
// 表格内部纵向分割线颜色
.el-table--border td, .el-table--border th, .el-table__body-wrapper .el-table--border.is-scrolling-left~.el-table__fixed{
      border-right: 1px solid white;
}
// 商品 
.goodsPropsWrap{
  margin: auto;
  height: 110px;
  width: 320px;
    display: flex;
  justify-content: space-around;
  align-items: center;
  .goodsImg{
      width: 70px;
      height: 70px;
      img{
        width:100%;
        height: 100%;
      }
  }
  .goodsProps{
        width: 200px;
        height: 70px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        color: #999999;
      .goodsName{
        color: #666666FF;
      }
  }
}

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

// 分类


// 店铺名称

// 操作
.btnWrap{
    display: flex;
    justify-content: space-around;
    align-items: center;
    .editWrap{
       cursor: pointer;
    }
  .skuWrap{
    cursor: pointer;
  }
  .btsvg{
    margin-right: 3px;
  }
}
.el-table__row:hover {
  .editWrap,.skuWrap{
    color: #2260D2; 
  }
}
.bottomFun{
    display: flex;
    justify-content: space-between;
    align-items: center;
  .bottomFunLeft{
    width: 450px;
    display: flex;
    justify-content: space-between;
    align-items: center;
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
