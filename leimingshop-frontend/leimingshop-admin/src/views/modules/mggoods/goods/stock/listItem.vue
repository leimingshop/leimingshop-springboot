<template>
  <div>
    
      <el-table
        width="100%"
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%"
        @selection-change="dataListSelectionChangeHandle"
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
        
        <el-table-column
		      prop="id"
		      label="spu"
          width="180"
		      align="center">
		    </el-table-column>

        <el-table-column
		      label="图片"
		      align="center">
              <template slot-scope="scope">
                  <div class="goodsImg">
                    <img  :src="scope.row.pictureUrl | filterImgUrl" style="width:60px;height:60px;object-fit: contain;" alt=""/>
                  </div>
              </template>
		    </el-table-column>

       <el-table-column
		      prop="goodsName"
		      label="商品名称"
          width="180"
		      align="center">
              <template slot-scope="scope">
                <div class="towEllipsis">
                  <a :href="scope.row | filterhrefUrl"  target="view_window">{{scope.row.goodsName}}</a>
                </div>
             </template>
		    </el-table-column>

        <el-table-column
		      prop="brandName"
		      label="品牌"
		      align="center">
		    </el-table-column>

        <el-table-column
		      label="价格"
		      align="center">
          <template slot-scope="scope">
              <div class="priceWrap">  
                  <div class="price1">￥{{scope.row.specSellPrice}}</div>
                  <div class="price2">￥{{scope.row.specCostPrice}}</div>
              </div>
            </template>
		    </el-table-column>

        <el-table-column
		      prop="gcName"
		      label="分类"
		      align="center">
		    </el-table-column>

        <el-table-column
		      prop="storeName"
		      label="店铺名称"
		      align="center">
		    </el-table-column>

        <el-table-column
		      label="操作"
		      align="center"
		      >
            <template slot-scope="scope">
			         <el-button @click.native.prevent="addOrEditHandle(scope.$index, scope.row)"type="text"size="mini">改库存</el-button>
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
import { goodsPageUrl } from '@/api/url'
  // import { deletepriceLogUrl } from '@/api/url'
import addEditData from './model-add-edit-data'
import {goodsUpdateCheckActivity} from "@/api/api.js";

export default {
  mixins: [mixinViewModule],
  data () {
    return {
    mixinViewModuleOptions: {
      getDataListURL: goodsPageUrl,
      activatedIsNeed:false,
      getDataListIsPage: true,
      // exportURL: '/admin-api/log/login/export',
      // deleteURL: deletepriceLogUrl,
      // deleteIsBatch: true,
      // deleteIsBatchKey: 'id'
    },
    addEditDataVisible:false,
    dataForm:{
        goodsShow:'',
    },
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
    init(dataForm){
         Object.assign(this.dataForm,dataForm)
        this.getDataList();
    },
    // 新建和编辑
    addOrEditHandle(index=-1,row=""){
      // 查询商品是否参加活动
        let obj =  [row.id];
        goodsUpdateCheckActivity(obj).then(res => {
            if (res.code == 200) {
                if (res.data.activityFlag == 0) {
                    // 未参加促销
                    this.setAddEditDataVisible(true);
                    this.$nextTick(() => {
                        this.$refs.addEditData.init(row)
                    })
                } else if (res.data.activityFlag == 1) {
                    // 促销未开始
                    this.$confirm(res.data.operationMsg, '提示', {
                        cancelButtonText: '取消',
                        confirmButtonText: '确定',
                        type: 'warning'
                    }).then(() => {
                        this.setAddEditDataVisible(true);
                        this.$nextTick(() => {
                          this.$refs.addEditData.init(row)
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
    setAddEditDataVisible(boolargu){
      this.addEditDataVisible =  boolargu;
    },
    // 重置
    reset(){
        this.dataForm.attrName = "";
    },
  }
}
</script>
<style  lang="scss"  scoped>
@import "@/element-ui/theme-variables.scss";

 .goodsImg{
      width: 70px;
      height: 70px;
      margin: auto;
      img{
        width:100%;
        height: 100%;
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

.towEllipsis{
  text-align: left;
   text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
