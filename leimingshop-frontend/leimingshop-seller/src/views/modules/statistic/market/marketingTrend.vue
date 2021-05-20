<template>
    <div>
    <div>
        <div style="display:flex;justify-content:space-between;width:100%;margin-top: 15px; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
          <span style="line-height:40px;margin:27px">营销趋势</span>
          <el-button style="    margin-right: 33px;" type="text" @click="open">指标选择</el-button>
        </div>
        <!-- :settings="chartSettings"  -->
        <ve-line :data="charData"  style="margin-top:20px"  :settings='chartSettings'></ve-line>
    </div>
    <div>
          <div style="width:100%; float:flex; height:60px;justify-content: space-between;display: flex; background-color:whitesmoke;align-items: center;">
           <span style="margin:27px">营销明细</span>
        </div>
        <div style="margin-top:8px">
            <el-table
            :data="tableData"
            border
            width="100%"
            >
            <el-table-column label="活动商品名称" align="center" width="330">
              <template slot-scope="scope">
                <!-- <div class="goodsPropsWrap">
                    <img style="width: 60px;height: 60px;" :src="scope.row.goodsImage | filterImgUrl" alt=""/>
                  <div class="goodsProps">
                            <span class="goodsName">
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                                    <span class="goodsNameContent"  @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                                </el-tooltip>
                            </span>
                        </div>
                </div> -->
              <div  style="display: flex;cursor: pointer;">
              <img :src="scope.row.goodsImage | filterImgUrl" width="40px" height="40px"/>
              <div class="towEllipsis" style="margin-left:8px" >
                  <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                      <span style="color: #4e80db;text-decoration: none; cursor: pointer;"   @click="previewH5Fn(scope.row)">
                          {{scope.row.goodsName}}
                      </span>
                  </el-tooltip>
              </div>
           </div>
              </template>
            </el-table-column>
            
            <el-table-column
            prop="downOrderPerson"
            label="下单人数"
            align="center">
            </el-table-column>

            <el-table-column
            prop="payPerson"
            label="支付人数"
            align="center"
            >
            </el-table-column>

            <el-table-column
            prop="orderGoodsNum"
            label="下单件数"
            align="center"
            >
            </el-table-column>

            <el-table-column
            prop="payOrderGoodsNum"
            label="支付件数"
            align="center"
            >
            </el-table-column>

            <el-table-column
            prop="payAmount"
            label="应收金额"
            align="center"
            >
            </el-table-column>

            <el-table-column
            prop="realityAmount"
            label="下单金额"
            align="center"
            >
            </el-table-column>

            <el-table-column
            prop="activityReduceAmount"
            label="减免金额"
            align="center"
            >
            </el-table-column>
            </el-table>
        </div>
    </div>
    <el-pagination
      @size-change="pageSizeChangeHandle"
      @current-change="pageCurrentChangeHandle"
      :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="limit"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <marketSelect ref="selectionMore" @choosedAfter="choosedAfter"></marketSelect>
    </div>
</template>
<script>
import marketSelect from "./marketSelect"
import {activityDetail,activity} from "@/api/api"
export default {
    data(){
        this.extend={
          series:{
            smooth:false
          }
        }
        this.chartSettings={
          'axisSite':{right:['转化率']},
          'yAxisType':['KMB','percent'],
          'yAxisName':['数值','比率']
        }
        return{
            activityStatisGoodsPage:{
              detailStatisDTOList:[],
            },
            charData:{
                columns:['日期','领取量','下单使用量','支付使用量','转化率'],
                rows:[
                  // {'日期':'2020-01-02','浏览量':500,'访客量':500,'下单订单数':600,'转化率':0.9},
                ],
            },
            tableData:[],
            activityStatisGoodsPage:{},
            map:[],
            page:1,
            total:0,
            limit:10,
        }
    },
    components:{
        marketSelect
    },
    created(){
      // this.getTableData();
      // this.getMarketingTrend();
        
    },
    methods:{
        init(arguObj,data){
          // 请求接口参数
          this.arguObj = arguObj;
          this.activityStatisGoodsPage = data.activityStatisGoodsPage; // 表格数据
          this.map = data.map;    // 折线图数据
          this.handleTableTrend();
          this.handleMarketingTrend();
        },
        
        previewH5Fn(row) {
				 window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
			},
        open() {
              this.$nextTick(()=>{
                this.$refs.selectionMore.init();
            })
        },

        // 表格模块---------------------------------------------------------------
          // 处理折线数据
        handleTableTrend(){
          this.tableData = this.activityStatisGoodsPage.detailStatisDTOList
          this.total = this.activityStatisGoodsPage.total
        },
        // 掉接口获取表格数据
        getTableData(){
            let obj={
                  params:{
                    ...this.arguObj,
                    page:this.page,
                    limit:this.limit,
                  }
              }
              activityDetail(obj).then(res=>{
                  if(res.code==200){
                    this.activityStatisGoodsPage = res.data
                    this.handleTableTrend();
                  }else{
                    this.$message.error(res.msg);
                  }
              })
        },
        // 折线图---------------------------------------------------------------
        // 处理折线图数据
        handleMarketingTrend(){
            //  this.map 
            // this.charData.rows=[];
            // this.charData.columns=[];
            var keys=Object.keys(this.map)
            var values=Object.values(this.map)
            let rows = []
            keys.forEach((item,index)=>{
                    rows.push({
                      '日期':item,
                      // 曝光
                      '浏览量':values[index].pageView,
                      '访客量':values[index].visitorsNumber,
                      '领取量':values[index].visitorsNumber,
                      // 下单
                      "下单订单数":values[index].downOrderNum,
                      "下单人数":values[index].downOrderPerson,
                      "下单使用量":values[index].userOrderNum,
                      "下单金额":values[index].orderAmount,
                      // 支付
                      "支付订单数":values[index].payOrderNum,
                      "支付人数":values[index].payPerson,
                      "支付使用量":values[index].payUseNum,
                      "应收金额":values[index].realityAmount,
                      "实收金额":values[index].payAmount,
                      "减免金额":values[index].activityReduceAmount,
                      // 曝光
                      "转化率":values[index].conversion,
                    })
            })
            // 备份数据
            this.charData.rows2 = rows
            // 默认数据
            let checkList = ['领取量','下单使用量','支付使用量','转化率'];
            this.choosedAfter(checkList);
            this.$refs.selectionMore.checkList = checkList
        },
        // 选择规格之后
        choosedAfter(checkList){
              // console.log(checkList)
            // this.handleMarketingTrend();
            this.charData.columns =  ["日期"].concat(checkList);
            this.charData.rows = [];
            this.charData.rows2.forEach((item,index)=>{
              let rowitem = {"日期":item["日期"] };
              checkList.forEach((item2,index2)=>{
                  rowitem[item2] = item[item2];
              })
              this.charData.rows.push(rowitem)
            })
          //  表格数据
           console.log(this.charData);
        },
        //  每页数
        pageSizeChangeHandle (val) {
          this.page = 1
          this.limit = val
          this.getTableData()
        },
        // 当前页
        pageCurrentChangeHandle (val) {
          this.page = val
          this.getTableData()
        },
      }
    }
</script>
<style lang="scss" scoped>
// 商品
    .goodsPropsWrap {
        margin: auto;
        width: 330px;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .goodsImg {
            width: 50px;
            height: 50px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsProps {
            width: 210px;
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
.towEllipsis{
  text-align: left;
  text-overflow: -o-ellipsis-lastline;
  text-overflow: ellipsis;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>