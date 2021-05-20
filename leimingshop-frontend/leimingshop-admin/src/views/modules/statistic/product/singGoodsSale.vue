<template>
   <div>
        <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
           <span style="margin:27px">单个商品销售情况</span>
           <span style="margin-right:30px;">
              <statisticDatePick  @getData='getData'></statisticDatePick>
           </span>
        </div>
        <div style="margin-top:30px;">
          <ve-histogram v-if="chartData2.rows.length>0"
            :data="chartData2" 
            :extend="chartExtend" 
            :settings="chartSettings"
            :title="chartTitle"
            :legend="chartLegend"
            :data-empty="dataEmpty">
            </ve-histogram>
            <ve-histogram v-else
            :data-empty="dataEmpty">
            
            </ve-histogram>
        </div>
        <div style="width:98%; float:left; height:50px;justify-content: space-between;display: flex;">
          单品销售明细
        </div>
        <div>
          <el-table
            width="100%"
            :data="dataList"
            border
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
		        prop="goodsId"
		        label="商品编号(spu)"
                min-width="100"
		        align="center">
		      </el-table-column>

		    <el-table-column
        prop="goodsMainPicture"
		      label="图片"
		      align="center">
            <template slot-scope="scope">
                  <div class="goodsImg">
                    <img  :src="scope.row.goodsMainPicture | filterImgUrl" style="width:60px;height:60px;object-fit: contain;" alt=""/>
                  </div>
            </template>
		    </el-table-column>

        <el-table-column
		    prop="goodsName"
		    label="商品名称"
            width="200"
		    align="center">
            <template slot-scope="scope">
                <div class="towEllipsis">
                  <!-- <a :href="scope.row | filterhrefUrl"  target="view_window">{{scope.row.goodsName}}</a> -->
                  <span style="color: #4e80db;text-decoration: none; cursor: pointer;"  @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                </div>
             </template>
		    </el-table-column>

        <el-table-column
		      prop="pageView"
		      label="浏览量"
		      align="center">
		    </el-table-column>

        <el-table-column
		      prop="visitorsNumber"
		      label="浏览人数"
		      align="center">
		    </el-table-column>

         <el-table-column
		      prop="paymentNumber"
		      label="付款人数"
		      align="center"
         >
		    </el-table-column>

        <el-table-column
		      prop="singleConversion"
		      label="单品转化率"
		      align="center"
          min-width="130"
         >
          <template slot-scope="scope">
                        <div>
                           {{(parseFloat(scope.row.singleConversion*100).toFixed(2))+"%"}}
                        </div>
                      </template>
		    </el-table-column>

		    <el-table-column
          prop="salesNumber"
		      label="销售数量"
		      align="center"
          width="140px"
		      >
		    </el-table-column>

        <el-table-column
          prop="salesAmount"
		      label="销售金额"
		      align="center"
          width="140px"
		      >
		    </el-table-column>
          </el-table>
        </div>
              <!-- 分页 -->
      <!-- <el-pagination
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="currentPageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->
   </div>
</template>

<script>

import { salestatis} from "@/api/api";
import statisticDatePick from "@/components/statisticDatePick"
import 'v-charts/lib/style.css'
export default {
    data(){
      this.chartExtend={
        barWidth:60,
          'xAxis.0': {
              data: [],
              axisLabel: {
                  interval: 0,
                  formatter: function (value) {
                      var str = "";
                      var num = 12; //每行显示字数
                      var valLength = value.length; //该项x轴字数
                    if(valLength>24){
                        value = value.substring(0,21)
                        value = value + '...'
                    }
                      var rowNum = Math.ceil(valLength / num); // 行数
                      if (rowNum > 1) {
                          for (var i = 0; i < rowNum; i++) {
                              var temp = "";
                              var start = i * num;
                              var end = start + num;

                              temp = value.substring(start, end) + "\n";
                              str += temp;
                          }
                          return str;
                      } else {
                          return value;
                      }
                  }
              }
          }
      }
      this.chartSettings={
        yAxisType:['KMB'],
        yAxisName:['单位：件']

      }
      this.chartTitle={
        left: 'center',
        text: '单品销量排行',
      }
      this.chartLegend={
       data:[]
      }
        return{
            dataEmpty: false,
            dataList:[],
            multipleSelection:[],
           // dataListLoading:false,
            valueTime:'',
            queryType:'1',
            currentPage:1,
            currentPageSize:10,
            limit:'10',
            total:0,
            // dataxx:[],
            chartData2:{
                columns:['商品名称','商品销售'],
                rows:[]
            },
            pickerOptions:{
          disabledDate(time){
            return time.getTime()>Date.now();
          }
        },
            dateObj:{
              startDate:'',
              endDate:'',
            },
        }
    },
    components:{
      statisticDatePick
    },
    mounted(){
        // this.getData();
    },
    methods: {
      getData(objArgu){
        this.objArgu = objArgu
        this.queryData();
      },
      previewH5Fn(row){
       window.open(window.SITE_CONFIG['pcURL']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
    },
      queryData(){
        let obj={
          params:{
            queryType:this.objArgu.queryType,
            startDate:this.objArgu.startDate,
            endDate:this.objArgu.endDate,
          }
        }
        var that = this
        salestatis(obj).then(res=>{
          that.chartExtend['xAxis.0'].data=[]
              if(res.code==200){
                res.data.forEach((item,index)=>{
                    item['商品名称'] = item.goodsName;
                    item['商品销售'] = item.salesNumber
                })
                  that.chartData2.rows= res.data
                  if(that.chartData2.rows && that.chartData2.rows.length!=0){
                    that.dataEmpty = false;
                  }else{
                    that.dataEmpty = true;
                  }
                  for(let i=0;i<that.chartData2.rows.length;i++){
                      that.chartExtend['xAxis.0'].data[i]=that.chartData2.rows[i]['商品名称']
                  }
                  that.dataList = res.data;
            } else{
              this.$message({
                  message:res.msg,
                  type: 'error',
                  duration: 1500,
              })
            }
        })
     
    },
  handleSelectionChange(val){
            this.multipleSelection=val;
        },

      //  每页数
		// pageSizeChangeHandle (val) {
		// 	this.currentPageSize = val;
		// 	this.currentPage = 1;
		// 	this.limit = val;
		// 	this.getDataList().then((res)=>{
    //       this.backScanHook();
    //   });
		// },
		// 当前页
		// handleCurrentChange (val) {
		// 	this.currentPage = val;
		// 	this.page = val;
		// 	this.getDataList().then((res)=>{
    //       this.backScanHook();
    //   });
		// },
    // }
    }
}
</script>
<style lang="scss" scoped>
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
</style>
