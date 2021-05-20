<template>
    <div v-loading="loading">
        <Bread :breaddata="orderDiscount" @changePage="changePageDet()" :index="'2'"></Bread>
        <div class="orderDisDe">
            <!-- <div class="orderDelf"> -->
            <div style="display: flex; align-items: center;width:43.3%">
                <label class="weightedFont">订单编号：</label>
                <input class="weightedFont" type="text" v-model="orderSn" id="bar"/>
            </div>
            <div style="width:33.3%;text-align: center;">
                <label class="weightedFont">下单时间：</label>
                <input class="weightedFont" type="text" v-model="createDate" id="bar1"/>
            </div>
        </div>
        <div class="title" style="margin-top:20px; margin-bottom: 0px">订单促销</div>
        <div class="discountTable">
            <el-table :data="activityList" border="" style="width: 100%; text-align: center">
                <el-table-column prop="activityType" label="促销类型" width="180">
                    <template slot-scope="scope">
                        {{scope.row.activityType==1 ? '优惠券':'满减活动'}}
                    </template>
                </el-table-column>
                <el-table-column prop="reduceAmount" label="优惠金额" width="180"></el-table-column>
<!--                <el-table-column prop="id" label="促销ID"></el-table-column>-->
                <el-table-column prop="activityName" label="活动名称"></el-table-column>
                <el-table-column prop="amount3" label="活动时间">
                    <template slot-scope="scope">
                        {{scope.row.startDate}}-{{scope.row.endDate}}
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="title" style="margin-bottom: 0px">订单优惠券</div>
        <div class="couponTable">
            <el-table :data="tableData" border="" style="width: 100%; text-align: center">
                <el-table-column prop="activityScope" label="优惠券类型" width="180">
                    <template slot-scope="scope">
                        {{scope.row.activityScope==0 ? '平台':'店铺'}}
                    </template>
                </el-table-column>
                <el-table-column prop="faceValue" label="优惠金额" width="180"></el-table-column>
<!--                <el-table-column prop="id" label="促销ID"></el-table-column>-->
                <el-table-column prop="activityName" label="活动名称"></el-table-column>
                <el-table-column prop="totalNum" label="发行量"></el-table-column>
            </el-table>
        </div>
        <div class="title">订单金额明细拆分（SKU维度)</div>
        <div class="order">
            <el-table :data="detailDetail" border="" style="width: 100%; text-align: center">
                <el-table-column label="商品信息">
                    <el-table-column prop="goodsName" label="商品名称" width="180"></el-table-column>
                    <el-table-column prop="specSerial" label="商品编号"></el-table-column>
                    <el-table-column prop="specPrice" label="单品金额" width="80"></el-table-column>
                </el-table-column>
<!--                <el-table-column prop="goodsName" label="商品名称" width="180"></el-table-column>-->
<!--                <el-table-column prop="specPrice" label="单品金额" width="80"></el-table-column>-->
                <el-table-column label="促销优惠相关">
                    <el-table-column
                            prop="reduceAmount"
                            label="基础优惠">
                    </el-table-column>
                    <el-table-column prop="reduceAmount" label="满减"></el-table-column>
                </el-table-column>
                <el-table-column label="运费相关">
                    <!--                    <el-table-column prop="amount3" label="商家运费"></el-table-column>-->
                    <el-table-column prop="amount3" label="运费金额"></el-table-column>
                    <!--                    <el-table-column prop="amount3" label="基础运费"></el-table-column>-->
                    <!--                    <el-table-column prop="amount3" label="偏远运费"></el-table-column>-->
                </el-table-column>
                <el-table-column label="支付优惠相关">
                    <el-table-column prop="couponAmount" label="优惠券"></el-table-column>
                    <el-table-column prop="amount3" label="积分"></el-table-column>
                    <el-table-column prop="amount3" label="余额"></el-table-column>
<!--                    <el-table-column prop="amount3" label="超级红包"></el-table-column>-->
                </el-table-column>
                <el-table-column label="其他">
                    <!--                    <el-table-column prop="amount3" label="退换货无忧"></el-table-column>-->
                    <!--                    <el-table-column prop="amount3" label="全球购税费"></el-table-column>-->
                    <!--                    <el-table-column prop="amount3" label="落地配服务"></el-table-column>-->
                    <el-table-column prop="goodsNum" label="数量"></el-table-column>
                    <el-table-column prop="goodsTotalPrice" label="商品总价"></el-table-column>
                    <el-table-column prop="goodsTotalPayPrice" label="应付金额"></el-table-column>
                </el-table-column>
                <!--                <el-table-column prop="amount3" label="应付金额"></el-table-column>-->
                <!--                <el-table-column prop="specSerial" label="商品编号"></el-table-column>-->
            </el-table>
            <div class="intrudts">
                <div> 退换货无忧：用户在结算页购买，逆向运费理赔保险</div>
                <div>
                    <span class="redStart">*</span>
                    <span> 订单中赠品，按照商家维护的赠品金额进行明细拆分</span>
                </div>

            </div>
            <div style="line-height: 30px">全球购税费：全球购订单独有</div>
            <div>落地配服务：大件商品送货安装费，开通落地配服务商家独有</div>
        </div>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { toOrderSnDetail} from '@/api/api'
    export default {
        data() {
            return {
                orderDiscount: ["订单管理", "订单管理", "订单详情", "订单优惠明细"],
                detailDetail: [],
                tableData: [],
                activityList: [],
                orderSn:'',
                createDate:'',
                loading:true,
            };
        },
        components: { Bread },
        created(){
        },
        methods: {
            init(orderSn){
                this.orderSn= orderSn
                this.getDetailData(orderSn)
            },
            getDetailData(orderSn){
                let params={
                    orderSn:orderSn
                }
                toOrderSnDetail(params).then(res=>{
                    if(res.code==200){
                        this.loading = false
                        this.activityList = res.data.adminOrderActivityList //订单促销集合
                        this.tableData = res.data.adminOrderCouponsList //订单优惠券集合
                        this.detailDetail = res.data.orderGoodsList //拆分明细
                        this.createDate = res.data.createDate //下单时间
                    }else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                })
            },
            //页面跳转
            changePageDet() {
                this.$emit("hiddenDiscountDetFn");
            }
        }

    };
</script>
<style lang="scss" scoped>
    .searchInput{
        /deep/ .el-input{
            width: 200px !important;
        }
    }
    .order{
        margin-bottom: 20px;
    }
    .discountTable,.couponTable{
        display: flex;
        margin-bottom: 20px;
    }
    .preferentialList{
        width: 28px;
        border: 1px solid #d1d1d1;
        text-align: center;
        padding: 12px 5px;
    }
    .intrudts{
        margin-top: 20px;
        display: flex;
        justify-content: space-between;
        .redStart{
            color: red;
        }
    }
    .title{
        font-size: 18px;
        font-weight: 700;
    }

    .orderDisDe {
        height: 60px;
        border: 1px solid #ecedf1;
        background-color: #fdfafe;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 30px;
        padding-right: 30px;
        font-size:18px;
    }
</style>
