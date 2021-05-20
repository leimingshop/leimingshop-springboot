<template>
  <div >
    <Bread :breaddata="paymentData" @changePage="goOrderList" :index="'2'"></Bread>
    <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="dataListLoading"
            style="width: 100%; maigin-top:10px;"
    >
      <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
        <template slot-scope="scope">{{scope.$index+1 }}</template>
      </el-table-column>
      <el-table-column prop="orderSn" label="订单编号" align="center" width="180"></el-table-column>
      <el-table-column prop="buyerName" label="会员名称" align="center"></el-table-column>
      <el-table-column prop="storeName" label="商户名称" align="center"></el-table-column>
      <el-table-column prop="createDate" label="下单时间" align="center" width="160"></el-table-column>
      <el-table-column prop="paymentTime" label="交易时间" align="center" width="160"></el-table-column>
      <el-table-column prop="paymentName" label="支付方式" align="center" width="80"></el-table-column>
      <el-table-column prop="orderAmount" label="订单金额" align="right" width="100">
        <template slot-scope="scope">￥{{scope.row.orderAmount}}</template>
      </el-table-column>
      <el-table-column
              prop="orderStatus"
              label="订单状态"
              align="center"
              width="100"
      >
        <template slot-scope="scope">
           <span v-if="scope.row.orderStatus==0">已取消</span>
            <span v-else-if="scope.row.orderStatus==10">待付款</span>
            <span v-else-if="scope.row.orderStatus==20">待发货</span>
            <span v-else-if="scope.row.orderStatus==30">待收货</span>
            <span v-else-if="scope.row.orderStatus==40">已完成</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <!-- <el-button type="primary" @click="submitStore()">{{ $t('confirm') }}</el-button> -->
          <el-button size="mini" type="text" @click="goOrderDetail( scope.row)">查看订单</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import Bread from "@/components/bread";
  import { paymentOrderList } from "@/api/api";
  export default {
    data () {
      return {
        orderParentSn:"", //订单号
        dataList:[],  // 订单列表
        paymentData: ["财务管理", "支付管理", "交易记录", "订单列表"],
        dataListLoading:false,
        row:'',
      }
    },
    components: {
      Bread
    },
    methods: {
      init(row){
        this.row = row;
        this.getData();
      },
      getData(){
        const params = {orderParentSn: this.row.orderSn}
        paymentOrderList(params).then(res => {
          if (res.code == 200) {
            this.dataList = res.data;
          } else {
            this.$message({
              type: "warning",
              message: res.msg
            });
          }
        });
      },
      goOrderList(){
        this.$emit("chagePageNum",1);
      },
      goOrderDetail(row){
        this.$emit("chagePageNum",3,row);
      }
    }
  }
</script>
