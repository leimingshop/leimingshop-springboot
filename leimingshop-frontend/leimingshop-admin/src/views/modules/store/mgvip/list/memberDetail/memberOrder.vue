<template>
    <div>
        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="mixinViewModuleOptions.dataListLoading"
            style="width: 100%"
            @selection-change="dataListSelectionChangeHandle"
        >
            <el-table-column prop="id" label="订单编号" align="center"></el-table-column>
            <el-table-column prop="storeId" label="商户ID" align="center"></el-table-column>
            <el-table-column prop="createDate" label="下单时间" align="center" width="180"></el-table-column>
            <el-table-column prop="paymentTime" align="center" label="交易时间" width="180"></el-table-column>
            <el-table-column prop="paymentName" align="center" label="支付方式"></el-table-column>
            <el-table-column prop="orderAmount" align="center" label="订单金额" width="100"></el-table-column>
            <el-table-column prop="orderStatus" align="center" :formatter="orderRules" label="订单状态"></el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="goDetail(scope.row)"
                        type="primary"
                        size="mini"
                    >查看订单</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
        ></el-pagination>
    </div>
</template>
<script>
import mixinViewModule from "@/mixins/view-module";
import { orderlists } from "@/api/url";
import { orderDetail } from "@/api/api";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: orderlists,
        getDataListIsPage: true,
        deleteURL: "",
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "id"
      },
      orderRules:function(row) {
        return row.orderStatus == 0
          ? <el-tag type="info">已取消</el-tag>
          : row.orderStatus == 10
            ? <el-tag type="warning">待付款</el-tag>
            : row.orderStatus == 20
              ? <el-tag type="warning">待发货</el-tag>
              : row.orderStatus == 30
                ? <el-tag type="warning">待收货</el-tag>
                : <el-tag type="danger">交易完成</el-tag>;
      }
    };
  },
  props: ["memberId"],
  created() {
    this.dataForm.buyerId = this.memberId;
    this.getDataList();
  },
  methods: {
    //订单详情
    goDetail(index) {
      const obj = { id: index.id };
      orderDetail(obj).then(res => {
        if (res.code == 200) {
          this.addressInfo = res.data.orderAddressDTO; //收货人信息
          this.orderData = res.data.orderGoodsDTOList;
          this.orderLog = res.data.orderLogisticsLogDTOList; //物流
          this.packageInfo = res.data.orderLogDTOList; //订单状态
          this.data = res.data;
          this.$emit('goBack',this.data)
        } else {
          this.$message({
            type: "warning",
            message: res.msg
          });
        }
      });
    }
  }
};
</script>
<style scoped>
</style>


