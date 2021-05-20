<template>
  <div>
    <Bread :breaddata="orderDetData" @changePage="getList(1)" :index="'2'"></Bread>
    <el-steps
      :active="data.orderStatus==10?1:data.orderStatus==20?2:data.orderStatus==30?3:5"
      align-center
    >
      <el-step title="提交订单" icon="el-icon-document" :description="data.createDate"></el-step>
      <el-step title="付款成功" icon="el-icon-mobile-phone" description=""></el-step>
      <el-step title="商品出库" icon="el-icon-goods" description=""></el-step>
      <el-step title="待收货" icon="el-icon-time" description=""></el-step>
      <el-step title="完成" icon="el-icon-circle-check" description=""></el-step>
    </el-steps>
    <div class="orderUerInfo">
      <div class="orderDe">
        <div class="orderDelf">
          <div>订单编号：
            <!-- <span>{{data.orderSn}}</span> -->
            <input type="text" v-model="data.orderSn" id="bar">
            <el-button
              class="btn"
              type="info"
              size="mini"
              id="copy"
              icon="el-icon-message"
              plain
              @click="copyOrder()"
              data-clipboard-action="copy"
              data-clipboard-target="#bar"
            >复制</el-button>
          </div>
          <div>订单状态：{{data.orderStatus==0?'已取消':data.orderStatus==10?'待付款':data.orderStatus==20?'待发货':data.orderStatus==30?'待收货':'交易完成' }}</div>
        </div>
        <div class="orderDerg">
          <div>操作:
            <el-button type="primary" size="mini" plain @click="getList(3)">查看订单优惠明细</el-button>
            <!-- <span @click="getList(3)">查看订单优惠明细</span> -->
          </div>
          <el-button
            type="primary"
            plain
            size="mini"
            @click="addRemarks()"
            style="margin-right:10px;"
          >增加备注</el-button>
          <!-- <div @click="addRemarks()">增加备注</div> -->
        </div>
      </div>
      <div class="buyerInfo">
        <ul>
          <li>购买用户：{{addressInfo.mobPhone}}</li>
          <li>用户等级：--</li>
        </ul>
        <ul>
          <li>备注等级：--</li>
          <li>商家备注：--</li>
        </ul>
      </div>
    </div>
    <div class="orderRecord">
      <div class="orderInfo">
        <div>包裹</div>
        <p>送货方式:{{data.transportCompanyName }}</p>
        <p>承运单电话:--</p>
      </div>
      <div class="orderText">暂无物流信息
        <!-- <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="textarea"></el-input> -->
      </div>
    </div>
    <div class="orderConfig">
      <div class="orderConList">
        <div>收货人信息</div>
        <p>联系买家:{{addressInfo.trueName}}</p>
        <p>收货人:--</p>
        <p>收货地址:{{addressInfo.address}}</p>
        <p>手机号:{{addressInfo.mobPhone}}</p>
        <p>
          买家留言:
          <span>{{data.orderMessage}}</span>
          <!-- <input type="textarea" :value="data.orderMessage"> -->
        </p>
      </div>
      <div class="orderConList">
        <div>配送信息</div>
        <p>配送方式:{{data.transportCompanyName }}</p>
        <p>运费:{{data.shippingFee!=null?'￥'+data.shippingFee:''}}</p>
        <p>配送日期:--</p>
      </div>
      <div class="orderConList">
        <div>付款信息</div>
        <p>付款方式:{{data.paymentName}}</p>
        <p>付款时间:{{data.paymentTime}}</p>
        <p>商品总额:{{data.goodsAmount!=null?'￥'+data.goodsAmount:''}}</p>
        <p>运费金额:{{data.shippingFee!=null?'￥'+data.shippingFee:''}}</p>
        <p>促销价格:{{(data.couponAmount!=null && data.preferentialPrice != null)?'￥'+(data.preferentialPrice - data.couponAmount).toFixed(2):'￥0.00'}}</p>
        <p>优惠券:{{data.couponAmount!=null?'￥'+data.couponAmount:'￥0.00'}}</p>
        <p>积分:--</p>
        <p>应支付金额:{{data.orderAmount!=null?'￥'+data.orderAmount:''}}</p>
      </div>
      <div class="orderConList">
        <div>发票信息</div>
        <p>发票类型:--</p>
        <p>发票抬头:--</p>
        <p>纳税人识别号:--</p>
        <p>发票内容:--</p>
      </div>
    </div>
    <div>
      <el-table :data="orderData" style="width: 100%" border="">
        <el-table-column prop="goodsName" label="商品名称" width="180" align="center"></el-table-column>
        <el-table-column prop="specId" label="商品货号" width="180" align="center"></el-table-column>
        <el-table-column prop="specCostPrice" label="单价" align="center">
          <template
            slot-scope="scope"
          >{{scope.row.specPrice!=''?'￥'+scope.row.specPrice:''}}</template>
        </el-table-column>
        <el-table-column prop="rudePri" label="优惠金额" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.activityType == 3">
                  {{((scope.row.specPrice - scope.row.specPayPrice) * scope.row.goodsNum).toFixed(2)}}
              </span>
              <span v-if="scope.row.activityType != 3">
                  {{scope.row.discountActivityAmount != ''?'￥'+scope.row.discountActivityAmount:'￥0.00'}}
              </span>
          </template>
        </el-table-column>
        <el-table-column prop="num" label="赠送积分" align="center"></el-table-column>
        <el-table-column prop="score" label="积分优惠购" align="center"></el-table-column>
        <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
        <el-table-column prop="goodsTotalPayPrice" label="应付金额" align="center">
          <template slot-scope="scope">
            {{scope.row.goodsTotalPayPrice!=''?'￥'+scope.row.goodsTotalPayPrice:''}}
          </template>
        </el-table-column>
      </el-table>
      <div class="summary">
        <ul>
          <li>商品总价:{{data.goodsAmount!=null?'￥'+data.goodsAmount:''}}</li>
          <li>运费:{{data.shippingFee!=null?'￥'+data.shippingFee:''}}</li>
          <li>促销金额:{{data.reduceAmount!=null?'￥'+data.reduceAmount:'￥0.00'}}</li>
          <li>优惠券:{{data.couponAmount!=null?'￥'+data.couponAmount:'￥0.00'}}</li>
          <li>积分抵扣:--</li>
          <li>应付金额:{{data.orderAmount!=null?'￥'+data.orderAmount:''}}</li>
        </ul>
      </div>
    </div>
    <div class="operationRecord">
      <p>操作日志</p>
      <p v-for="item in orderLog">
        {{item.updateDate}}
        <span class="creater">{{item.updater}}</span>
        {{item.message}}
      </p>
    </div>
    <el-dialog title="填写备注" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="备注信息：">
          <el-input type="textarea" v-model="form.desc"></el-input>
        </el-form-item>
        <el-form-item label="等级：">
          <el-radio-group v-model="form.resource">
            <el-radio label="一级" value="0"></el-radio>
            <el-radio label="二级" value="1"></el-radio>
            <el-radio label="三级" value="2"></el-radio>
            <el-radio label="四级" value="3"></el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" plain @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Bread from "@/components/bread";
import Clipboard from "clipboard";

export default {
  data() {
    return {
      textarea: "",
      dialogFormVisible: false,
      orderDetData: ["订单管理", "订单管理", "会员详情", "订单详情"],
      form: {
        desc: "",
        resource: 0
      }
    };
  },
  components: {
    Bread
  },
  props: ["data", "addressInfo", "orderLog", "orderData", "packageInfo"],
  methods: {
    //页面跳转 1-列表页
    getList(nums) {
      this.$emit("goList");
    },

    //添加备注
    addRemarks() {
      this.dialogFormVisible = !this.dialogFormVisible;
    },
    copyOrder() {
      var clipboard = new Clipboard(".btn");
      let that = this;
      clipboard.on("success", e => {
        that.$message({
          message: "订单号复制成功",
          type: "success",
          duration: 1000
        });
        clipboard.destroy();
      });

      clipboard.on("error", e => {
        that.$message({
          message: "订单号复制失败，请重新复制",
          type: "error",
          duration: 1000
        });
        clipboard.destroy();
      });
    }
  }
};
</script>
<style scoped>
.creater {
  display: inline-block;
  width: 80px;
  margin: 0 15px;
}
.el-textarea {
  width: 30%;
}
#bar {
  border: none;
  margin-right: 10px;
  min-width: 180px;
  display: inline-block;
}
.orderUerInfo {
  width: 100%;
  height: auto;
  margin-top: 20px;
  border: 1px solid #d1d1d1;
}
.orderDe {
  /* border: 1px solid #333; */
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.orderDelf,
.orderDerg {
  /* display: flex; */
  display: flex;
  align-items: center;
}
.orderDelf div {
  margin-left: 20px;
}
.orderDerg div {
  margin-right: 20px;
}
.buyerInfo {
  box-sizing: border-box;
  border-radius: 5px;
  padding: 0px 8px;
  height: auto;
  /* margin: 5px 10px; */
  border: 1px solid #d1d1d1;
  display: flex;
}
.buyerInfo ul {
  width: 50%;
  padding: inherit;
}
.buyerInfo ul li {
  list-style-type: none;
  padding: 3px;
}
.orderRecord {
  widows: 100%;
  height: auto;
  height: 200px;
  display: flex;
  margin-top: 10px;
  border: 1px solid #d1d1d1;
}
.orderText {
  width: 80%;
  height: 100%;
  display: flex;
  padding: 10px;
  justify-content: flex-start;
}
.orderInfo {
  width: 20%;
  padding: 20px 0 0 30px;
  border-right: 1px solid #d1d1d1;
  text-align: left;
  height: 100%;
}
.el-textarea {
  width: 100%;
}
.el-textarea__inner {
  height: 100%;
  resize: none;
}
.orderConfig {
  width: 100%;
  border: 1px solid #d1d1d1;
  height: auto;
  display: flex;
  justify-content: flex-start;
  margin-top: 10px;
}
.orderConList {
  width: 25%;
  padding: 20px 0 0 30px;
  border-right: 1px solid #d1d1d1;
  text-align: left;
  height: auto;
}
.summary {
  width: 100%;
  display: flex;
  margin-top: 20px;
  justify-content: flex-end;
}
.summary ul li {
  list-style: none;
  width: 200px;
  line-height: 30px;
}
.operationRecord {
  width: 100%;
  padding-left: 20px;
  margin: 20px 0;
  height: auto;
  border: 1px solid #d1d1d1;
}
.operationRecord p {
  line-height: 30px;
}
.el-table {
  margin-top: 20px;
}
</style>
