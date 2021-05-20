<template>
<div>
  <list  v-show="showPageNum==1" @changePageNum="changePageNum"></list>
  <detail v-show="showPageNum==2" ref="detailConmpon"  @changePageNum="changePageNum"></detail>
  <orderDet
          ref="orderDetailCompon"
          v-if="showPageNum==3"
          @changePage="changePage"
          :index="'3'"
          :orderDetBreaddata="orderDetBreaddata"
  ></orderDet>

</div>
</template>
<script>
  import  list from  "./list.vue"
  import  detail from  "./detail.vue"
  import orderDet from "../order/list/orderDet";

// import { exchangegoods } from "@/api/url";
// import { exchDetail } from "@/api/api";
// import mixinViewModule from "@/mixins/view-module";

// import List from "../../store/storeList/list";
export default {

  data() {
    return {
      orderDetBreaddata:["订单管理", "售后管理", "换货管理", "换货详情", "订单详情"],

      // tableData: [],
      // dataForm: {
      //   specSerial: "",
      //   aftersaleSn: "",
      //   storeName: "",
      //   goodsName: "",
      //   memberName: "",
      //   endTime: "",
      //   startTime: ""
      // },
      totalPage: 0,
      goodsData: [], //售后商品table
      saleGoods: [], //售后申请数据
      saleAuditLog: [], //售后理由数据-平台审核数据
      data: {}, //总数据
      piclist: [], //凭证图片
      total: 0,


      data: [],
      params: {
        currentPage: 1, //当前页数
        currentPageSize: 10 //每页显示的条数
      },
      // *****************订单详情页
      addressInfo: [], //地址数据
      packageInfo: [], //包裹数据
      orderLog: [], //操作日志
      orderData: [],
      data2: [],
      showPageNum:1,
    };
  },
  methods: {
    changePageNum(num,row){
      this.showPageNum = num;
      this.$nextTick(()=>{
         if(this.showPageNum==2){
            this.$refs.detailConmpon.init(row);
         }else if(this.showPageNum==3){
           row.id = row.orderId
           this.$refs.orderDetailCompon.init(row);
         }
      })
    },
    // changePage(){
    //   this.$route.push({
    //     name:'mgorder-order-list'
    //   })
    // },
    //返回上一级
    changePage() {
      this.showPageNum = 2;
    },

  },
  created() {
    //   this.getDataListURL
  },
  components: {
    list,
    detail,
    orderDet,
    // List, Bread,
  }
};
</script>
<style scoped>
.creater {
  display: inline-block;
  width: 80px;
  margin: 0 15px;
}
.el-table--border {
  margin-top: 20px;
}
.el-radio-group {
  margin-top: 20px;
}

.buyerInfo {
  flex-direction: column;
}
.buyerInfo,
.sellerInfo,
.buyerInfo,
.goodsLog,
.goods,
.operationList {
  border: 1px solid #e1e1e1;
  margin-top: 10px;
  padding: 10px 10px 20px 10px;
}
.inforTit {
  width: 100px;
  font-weight: 600;
  text-align: right;
  display: inline-block;
}
.imglist {
  width: 100px;
  height: 100px;
}
.inforRight {
  margin-left: 40px;
  display: inline-block;
}
.right {
  margin-right: 15px;
}
.buyerInfo span,
.sellerInfo span,
.buyerInfo span,
.goodsLog span {
  margin-top: 20px;
}

/deep/ .el-form-item {
  margin-top: 0px !important;
}

</style>
