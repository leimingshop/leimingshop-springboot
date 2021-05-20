<template>
    <div>
      <Bread  :breaddata="breaddata" @changePage="changePage()" :index="'1'"></Bread>

  <div>
     <el-card style="    margin-left: -20px;">
       <div style="display: flex;">
          <div style="margin-top: 10px;font-size:18px;color:#333;font-weight: bold;">
          <span>账单编号：</span>
          <span>{{ this.detailData.billInfo.id}}</span>
        </div>

        <div  style="margin-left: 45px;margin-top: 10px;font-size:18px;color:#333;font-weight: bold;">
          <span style="font-size:18px;color:#333;font-weight: bold;">结算状态：</span>
          <span>{{this.detailData.billInfo.billState==0?"未结算":"已结算"}}</span>
        </div>

        <div  style="margin-left: 45px;margin-top: 10px;font-size:18px;color:#333;font-weight: bold;">
          <span style="font-size:18px;color:#333;font-weight: bold;">出账时间：</span>
          <span>{{this.detailData.billInfo.billTime}}</span>
        </div>

        <div style="margin-left: 45px;margin-top: 10px;font-size:18px;color:#333;font-weight: bold;">
          <span>应结金额：</span>
          <span >￥{{this.detailData.billInfo.resultTotals}}</span>
        </div>

        <div style="margin-left: 45px;">
          <el-button v-if="this.detailData.billInfo.confirmStatus==0"  :disabled="this.vable"  type="primary" size="big" style="with:500px;margin-left: 45px;" @click="confirm()">确认</el-button>
          <el-button v-if="!this.detailData.billInfo.storeRemark"  type="primary" size="big" style="with:500px;margin-left: 45px;" @click="remark()">添加备注</el-button>
          <importAndExport  style="right: 330px;position: absolute;border-color: #0ea55d;margin-left: 45px;" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList" ></importAndExport>
        </div>
       </div>
      </el-card>
  
    <br />
    <div style="font-size:18px;color:#333;font-weight: bold;">
      <span >出账周期：</span>
      <span>{{this.detailData.billInfo.obtStartTime}}至{{this.detailData.billInfo.obtEndTime}}</span>
    </div>
    <br />
    <div style="font-size:18px;color:#333;font-weight: bold;margin-top:15px">
      <span >应结金额：</span>
      <span>订单金额-平台分佣-退单金额+退还佣金-店铺活动费用+积分抵现金额</span>
    </div>
      
    <div style="font-size:18px;color:red;margin-left: 87px;margin-top:20px">
        <span>￥{{this.detailData.billInfo.orderTotalAmount}}-￥{{this.detailData.billInfo.commisTotal}}-￥{{this.detailData.billInfo.returnAmount}}+￥{{this.detailData.billInfo.returnCommisTotal}}-￥{{this.detailData.billInfo.storeCostTotal}}+￥{{this.detailData.billInfo.pointTotal}}</span>
    </div>

  </div>


  <div>
    <div>

       

    </div>
    <br />

    <!-- <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">操作记录：</span>
    </div>
    <br /> -->

    <!-- <div v-for="(item,index) in detailData.logFrom">{{item.createDate}}&#12288;{{item.storeName}}&#12288;{{item.operator}}&#12288;{{item.operatorType}}</div> -->

  </div>
<div style="clear:both"></div>

    <el-radio-group v-model="activeName" @change="handleClick(activeName,1,10)"  type="primary">
            <el-radio-button label="order" width="170">订单列表</el-radio-button>
            <el-radio-button label="return" width="170">退货列表</el-radio-button>
    </el-radio-group>

        <el-table v-if="ordersible" v-loading="dataListLoading1" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
          <el-table-column prop="orderSn" label="订单编号" header-align="center" align="center" width="180"></el-table-column>
          <el-table-column prop="payTime" label="交易时间" header-align="center" align="center"></el-table-column>
          <el-table-column prop="orderTotals" label="订单金额" header-align="center" align="center"></el-table-column>
          <el-table-column prop="commisTotals" label="平台分佣" header-align="center" align="center"></el-table-column>
          <el-table-column prop="storeCostTotals" label="店铺活动费" header-align="center" align="center"></el-table-column>
          <el-table-column prop="pointAmount" label="积分抵现" header-align="center" align="center"></el-table-column>
          <el-table-column prop="paymentName" label="支付方式" header-align="center" align="center"></el-table-column>
                      <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="orderDetail(scope.row.orderId)">订单详情</el-button>
          </template>
        </el-table-column>
      </el-table>
        <el-pagination
                v-if="ordersible"
                :current-page="page1"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit1"
                :total="total1"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="pageSizeChangeHandle1"
                @current-change="pageCurrentChangeHandle1">
        </el-pagination>
      <el-table v-if="returnsible" v-loading="dataListLoading2" :data="detailData.returnFrom" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
          <el-table-column prop="returnSn" label="退货编号" header-align="center" align="center" width="180"></el-table-column>
          <el-table-column prop="returnTime" label="申请时间" header-align="center" align="center"></el-table-column>
          <el-table-column prop="specSerial" label="商品货号" header-align="center" align="center"></el-table-column>
          <el-table-column prop="goodsName" label="商品名称" header-align="center" align="center"></el-table-column>
          <el-table-column prop="returnAmount" label="退款金额" header-align="center" align="center"></el-table-column>
          <el-table-column prop="paymentName" label="支付方式" header-align="center" align="center"></el-table-column>
          <el-table-column prop="manageTime" label="平台处理时间" header-align="center" align="center"></el-table-column>
          <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="afterDetail(scope.row)">售后详情</el-button>
          </template>
        </el-table-column>
      </el-table>
        <el-pagination
                v-if="returnsible"
                :current-page="page2"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit2"
                :total="total2"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="pageSizeChangeHandle2"
                @current-change="pageCurrentChangeHandle2">
        </el-pagination>

    </div>
</template>

<script>
import importAndExport from "@/components/import-and-export"
//对账详情导出 listType 1订单列表 2退货列表
import { billExport } from '@/api/io'
import mixinViewModule from '@/mixins/view-module'
import {getbillInfo,getOrderBillPage,getReturnBillPage,getBillLog,bill} from "@/api/api";
import Bread from "@/components/bread"
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      /*导出*/
      importAndExportOptions:{
        exportUrl:billExport,//导出接口
        exportWord:"导出",
      },
      ordersible:true,
      vable:false,
      returnsible:false,
      activeName:"order",
      mixinViewModuleOptions: {
        getDataListURL: 'seller-api/orderbill/order/page',
        getDataListIsPage: true,
        deleteURL: '/settle/orderbill',
        deleteIsBatch: true,
      },
        dataList:[],
        dataListLoading1:true,
        dataListLoading2:true,
        // 分页初始值
        page1:1,
        limit1:10,
        total1:0,
        page2:1,
        limit2:10,
        total2:0,
      dataForm: {
        billTotalId:'',
      },
      detailData: {
        id: '',
        billTotalId:'',
        billInfo:{

        },
        orderFrom:[],
        returnFrom:[

        ],
        logFrom:{

        }
      },
      breaddata: ["财务管理", "对账列表", "对账详情"]
    }
  },
  // created() {

            // 第一次请求数据
            // this.handleClick("order");
            // this.init(this.detailData.billTotalId);
        // },

  components: {
    Bread,
    importAndExport
  },
  methods:{

        init(row){
          this.vable=false
            this.dataForm.billTotalId=row;
            this.detailData.billInfo.id=row
            this.activeName = "order"
	          this.$nextTick(()=>{
              if(row){
              // this.getDataList();
              this.info(row);
              this.handleClick("order",1,10)
              }
            })
      },
      //切换tab
      handleClick(tab,page,limit) {
          this.dataListLoading1 = true
          this.dataListLoading2 = true
          this.activeName = tab
          var obj={
                params:{
                    billTotalId:this.detailData.billInfo.id,
                    page:page,
                    limit:limit
                }
          }

          if(tab== "order"){
            this.dataForm.listType = 1;
            this.ordersible=true;
            this.returnsible=false;
            this.detailData.orderFrom = []
            getOrderBillPage(obj).then((res)=>{
              if(res.code == 200){
                  this.dataListLoading1 = false
                  this.dataList=res.data.list
                  this.total1 = res.data.total
              }
            })

          }else if(tab== "return"){
            this.dataForm.listType = 2;
            this.ordersible=false;
            this.returnsible=true;
            this.detailData.returnFrom = []
            getReturnBillPage(obj).then((res)=>{
                if(res.code == 200){
                    this.dataListLoading2 = false
                this.detailData.returnFrom=res.data.list
                    this.total2 = res.data.total
              }
            })
          }
        },
        //详情
        info(row){
            var obj = {
                id:row
            }
            getbillInfo(obj).then((res)=>{
                if(res.code == 200){
                  this.detailData.billInfo=res.data

                }
            })

        },
        // 面包屑
        changePage(){
          this.$emit("changePage")
        },
        // 订单详情
        orderDetail(id){
          var row ={
            id:id
          }
        this.$emit("orderDetail",row)
        },
        //售后详情
        afterDetail(row){

        this.$emit("afterDetail",row)
        },
        //添加备注
        remark(){
          this.$prompt('','添加备注', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPattern: ''

          }).then(({ value }) => {
            var params={
              id:this.detailData.billInfo.id,
              storeRemark:value
            }
            bill(params).then((res)=>{
              if(res.code==200){
                  this.$message({
                    type: 'success',
                    message: res.msg
                });
               this.init(params.id);
              }

            })
        }).catch(() => {
        });
        },
        //确认信息
        confirm(){
          this.vable=true;
           var params={
              id:this.detailData.billInfo.id,
              confirmStatus:1
            }
           bill(params).then((res)=>{
              if(res.code==200){
                  this.$message({
                    type: 'success',
                    message: res.msg
                });
                this.init(params.id);
              }

            })
        },

// 分页, 每页条数
      pageSizeChangeHandle1(val) {
          this.page1 = 1
          this.limit1 = val
          this.handleClick(this.activeName,this.page1,this.limit1)
      },
      // 分页, 当前页
      pageCurrentChangeHandle1(val) {
          this.page1 = val
          this.handleClick(this.activeName,this.page1,this.limit1)
      },
      pageSizeChangeHandle2(val) {
          this.page2 = 1
          this.limit2 = val
          this.handleClick(this.activeName,this.page2,this.limit2)
      },
      // 分页, 当前页
      pageCurrentChangeHandle2(val) {
          this.page2 = val
          this.handleClick(this.activeName,this.page2,this.limit2)
      },

  }
}
</script>
