<template>
    <div>
      <Bread  :breaddata="breaddata" @changePage="changePage()" :index="'2'"></Bread>
        <importAndExport  style="right: 12px;top: 12px;position: absolute;border-color: #0ea55d" :importAndExportOptions="importAndExportOptions" :dataForm="exFrom" @getDataList="getDataList" ></importAndExport>

  <div style="float:left;width:50%;margin-top: 60px">
    <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">商家类型：</span>
      <span>{{ this.dataForm1.billInfo.storeType==1?"自营店铺":"普通店铺"}}</span>
    </div>
    <br />
    <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">起止时间：</span>
      <span>{{this.dataForm1.billInfo.obtStartTime}}至{{this.dataForm1.billInfo.obtEndTime}}</span>
    </div>
    <br />
    <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">出账时间：</span>
      <span>{{this.dataForm1.billInfo.billTime}}</span>
    </div>
    <br />
    <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">应结金额：</span>
      <span style="font-size:5px;color:#333;">￥{{this.dataForm1.billInfo.resultTotals}}=￥{{this.dataForm1.billInfo.orderTotalAmount}}(订单金额)-￥{{this.dataForm1.billInfo.commisTotal}}(平台分佣)-￥{{this.dataForm1.billInfo.returnAmount}}(退单金额)+￥{{this.dataForm1.billInfo.returnCommisTotal}}(退还佣金)-￥{{this.dataForm1.billInfo.storeCostTotal}}(店铺活动费用)+￥{{this.dataForm1.billInfo.pointTotal}}(积分抵现金额)</span>
    </div>
    <br />
      <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">结算状态：</span>
      <span>{{this.dataForm1.billInfo.billState==0?"未结算":"已结算"}}</span>
    </div>
    <br />
  </div>


        <div>
    <br />

    <div>
      <span style="font-size:15px;color:#333;font-weight: bold;">操作记录：</span>
    </div>
    <br />
        <el-table v-loading="dataListLoading1" :data="dataForm1.logFrom" border style="width:30%;" :show-header="false" class="tableClass">
            <el-table-column prop="createDate"  header-align="center" align="center"></el-table-column>
            <el-table-column prop="storeName"  header-align="center" align="center"></el-table-column>
            <el-table-column prop="operator"  header-align="center" align="center"></el-table-column>
            <el-table-column prop="operatorType"  header-align="center" align="center"></el-table-column>
        </el-table>
  </div>
<div style="clear:both"></div>

    <el-radio-group v-model="activeName" @change="handleClick(activeName,1,10)" style="margin-bottom: 15px" type="primary">
            <el-radio-button label="order" width="170">订单列表</el-radio-button>
            <el-radio-button label="return" width="170">退货列表</el-radio-button>
    </el-radio-group>

        <el-table v-if="ordersible" v-loading="dataListLoading1" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
          <el-table-column prop="orderSn" label="订单编号" header-align="center" align="center"></el-table-column>
          <el-table-column prop="payTime" label="交易时间" header-align="center" align="center"></el-table-column>
          <el-table-column prop="orderTotals" label="订单金额" header-align="center" align="center"></el-table-column>
          <el-table-column prop="commisTotals" label="平台分佣" header-align="center" align="center"></el-table-column>
          <el-table-column prop="storeCostTotals" label="店铺活动费" header-align="center" align="center"></el-table-column>
          <el-table-column prop="pointAmount" label="积分抵现" header-align="center" align="center"></el-table-column>
          <el-table-column prop="paymentName" label="支付方式" header-align="center" align="center"></el-table-column>
                      <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="orderDetail(scope.row.orderId)">查看</el-button>
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
      <el-table v-if="returnsible" v-loading="dataListLoading2" :data="dataForm1.returnFrom" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
          <el-table-column prop="returnSn" label="退货编号" header-align="center" align="center"></el-table-column>
          <el-table-column prop="returnTime" label="申请时间" header-align="center" align="center"></el-table-column>
          <el-table-column prop="specSerial" label="商品货号" header-align="center" align="center"></el-table-column>
          <el-table-column prop="goodsName" label="商品名称" header-align="center" align="center"></el-table-column>
          <el-table-column prop="returnAmount" label="退款金额" header-align="center" align="center"></el-table-column>
          <el-table-column prop="paymentName" label="支付方式" header-align="center" align="center"></el-table-column>
          <el-table-column prop="manageTime" label="平台处理时间" header-align="center" align="center"></el-table-column>
                      <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="afterDetail(scope.row)">查看</el-button>

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
import {getbillInfo,getOrderBillPage,getReturnBillPage,getBillLog} from "@/api/api";
import Bread from "@/components/bread"
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      ordersible:true,
      returnsible:false,
      activeName:"order",
      mixinViewModuleOptions: {
        getDataListURL: 'admin-api/orderbill/order/page',
        getDataListIsPage: true,
        deleteURL: '/settle/orderbill',
        deleteIsBatch: true,
      },
        /*导出*/
        importAndExportOptions:{
            exportUrl:billExport,//导出
            exportWord:"导出",
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
        exFrom:{
           billTotalId:'',
           listType:'1'
        },
      dataForm1: {
        id: '',
        billTotalId:'',
        billInfo:{

        },
        orderFrom:[],
        returnFrom:[

        ],
        logFrom:{

        },
      },
      breaddata: ["财务管理", "对账管理", "对账列表", "对账详情"]
    }
  },
  // created() {

            // 第一次请求数据
            // this.handleClick("order",1,10);
            // this.init(this.dataForm1.billTotalId);
        // },

  components: {
    Bread,
    importAndExport
  },
  methods:{

        init(row){
            this.dataForm1.billTotalId=row;
            this.dataForm1.billInfo.id=row;
            this.dataForm.billTotalId=row;
            this.exFrom.billTotalId=row;
	          this.$nextTick(()=>{
              if(row){
               this.getDataList();
               this.info(row);
               this.getBillLog(row);
                  this.handleClick("order",1,10)
              }
            })
      },
      handleClick(tab,page,limit) {
          this.dataListLoading1 = true
          this.dataListLoading2 = true
            this.activeName = tab
          var that = this
          var obj={
                params:{
                    billTotalId:this.dataForm1.billInfo.id,
                    page:page,
                    limit:limit
                }
          }

          if(tab== "order"){
              this.exFrom.listType = 1;
              that.ordersible=true;
              that.returnsible=false;
              that.dataForm1.orderFrom = []
            getOrderBillPage(obj).then((res)=>{
              if(res.code == 200){
                  that.dataListLoading1 = false
                  that.dataList=res.data.list
                  that.total1 = res.data.total
              }
            })

          }else if(tab== "return"){
            this.exFrom.listType =2;
           this.ordersible=false;
            this.returnsible=true;
              this.dataForm1.returnFrom = []
            getReturnBillPage(obj).then((res)=>{
                if(res.code == 200){
                    this.dataListLoading2 = false
                this.dataForm1.returnFrom=res.data.list
                    this.total2 = res.data.total
              }
            })
          }
        },
        info(row){
            var obj = {
                id:row
            }
            getbillInfo(obj).then((res)=>{
                if(res.code == 200){
                  this.dataForm1.billInfo=res.data

                }
            })

        },
        changePage(){
          this.$emit("changePage")
        },

        getBillLog(row){
           var params = {
                id:row
            }
            getBillLog(params).then((res)=>{
                if(res.code == 200){
                  this.dataForm1.logFrom=res.data

                }
            })
        },
        // 订单详情
        orderDetail(id){
          var row ={
            id:id
          }
        this.$emit("orderDetail",row)
        },
        //售后详情
          //售后详情
        afterDetail(row){
           this.$emit("afterDetail",row)
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
<style lang="scss">
    .tableClass{
        th{
            padding: 0!important;
            height: 15px;
            line-height: 15px;
        }
        td{
            padding: 0!important;
            height: 15px;
            line-height: 15px;
  
        }
    }
</style>
