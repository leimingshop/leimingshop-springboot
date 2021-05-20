<template>
    <div v-loading="loading">
        <Bread
            :breaddata="orderDetData"
            @changePage="changePage(1)"
            :index="'2'"
        ></Bread>
        <el-steps :active="activeStep" align-center>
            <el-step
                v-for="(item, index) in data.orderTimeDTOList"
                :key="index"
                :title="item.orderOperation"
                :description="item.operationDate"
            >
            </el-step>
        </el-steps>
        <div class="orderUerInfo">
            <div class="orderDe">
                <div class="orderDelf">
                    <div>
                        订单编号：
                        <!-- <span>{{data.orderSn}}</span> -->
                        <input type="text" v-model="data.orderSn" id="bar" />
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
                            >复制
                        </el-button>
                    </div>
                    <div>
                        订单状态：{{
                            data.orderStatus == 0
                                ? "已取消"
                                : data.orderStatus == 10
                                ? "待付款"
                                : data.orderStatus == 20
                                ? "待发货"
                                : data.orderStatus == 30
                                ? "待收货"
                                : "交易完成"
                        }}
                    </div>
                </div>
                <div class="orderDerg">
                    <div>
                        操作:
                        <el-button type="primary" plain @click="getDiscount()"
                            >查看订单优惠明细</el-button
                        >
                        <!-- <span @click="getDiscount()">查看订单优惠明细</span> -->
                    </div>
                </div>
            </div>
            <div class="buyerInfo">
                <ul class="disPlayColumn">
                    <li class="disPlayRow">
                        <span class="inforTit marginTop0">购买用户：</span>
                        {{ data.buyerName }}
                    </li>
                    <li>
                        <span class="inforTit">用户等级：</span>
                        {{ data.buyerGraderName }}
                    </li>
                </ul>
                <ul class="disPlayColumn">
                    <li>
                        <span class="inforTit">备注等级：</span>
                        <span v-if="data.sellerRemarkGrade == 1">一级</span>
                        <span v-if="data.sellerRemarkGrade == 2">二级</span>
                        <span v-if="data.sellerRemarkGrade == 3">三级</span>
                        <span v-if="data.sellerRemarkGrade == 4">四级</span>
                    </li>
                    <li>
                        <span class="inforTit">商家备注：</span>
                        {{ data.sellerRemark }}
                    </li>
                </ul>
            </div>
        </div>
        <div class="orderRecord">
            <div class="orderInfo">
                <div class="title">包裹</div>
                <p>
                    <span class="inforTit">承运单位:：</span>
                    {{ data.transportCompanyName }}
                </p>
                <p>
                    <span class="inforTit">承运单号：</span>
                    {{ data.transportCode }}
                </p>
            </div>
            <div class="orderLogisticsText">
                <div v-for="item in packageInfo" class="orderLogTextDiv">
                    <span class="orderLogTimeText">{{ item.time }}</span>
                    <span class="orderLogText">{{ item.context }}</span>
                </div>
                <span class="orderLogText" v-if="packageInfo == ''"
                    >暂无物流信息</span
                >
            </div>
        </div>
        <div class="orderConfig">
            <div class="orderConList">
                <div>
                    <span class="title">收货人信息</span>
                </div>
                <p>
                    <span class="inforTit">会员名称:</span>
                    {{ data.buyerName }}
                </p>
                <p>
                    <span class="inforTit">收货人:</span>
                    {{ addressInfo.trueName }}
                </p>
                <p>
                    <span class="inforTit">收货地址:</span>
                    {{ addressInfo.address }}
                </p>
                <p>
                    <span class="inforTit">手机号:</span>
                    {{ addressInfo.mobPhone }}
                    <el-button
                        type="primary"
                        size="mini"
                        plain
                        @click="lookPhoneNum()"
                        >点击查看</el-button
                    >
                </p>
                <p>
                    <span class="inforTit">买家留言：</span>
                    <span>{{ data.orderMessage }}</span>
                </p>
            </div>
            <div class="orderConList">
                <div>
                    <span class="title">配送信息</span>
                </div>
                <p>
                    <span class="inforTit">配送方式：</span>
                    {{
                        data.devlierType == 1
                            ? "快递"
                            : data.devlierType == 0
                            ? "无需物流"
                            : "--"
                    }}
                </p>
                <p>
                    <span class="inforTit">运费:</span>
                    {{ data.shippingFee != null ? data.shippingFee : "" }}
                </p>
                <p>
                    <span class="inforTit">配送日期:</span>
                    {{ data.transportTime }}
                </p>
            </div>
            <div class="orderConList">
                <div class="title">付款信息</div>
                <p>
                    <span class="inforTit">付款方式:</span>
                    {{ data.paymentName }}
                </p>
                <p>
                    <span class="inforTit">付款时间:</span>
                    {{ data.paymentTime }}
                </p>
                <p>
                    <span class="inforTit">商品总额:</span>
                    {{ data.goodsAmount != null ? data.goodsAmount : "" }}
                </p>
                <p>
                    <span class="inforTit">运费金额:</span>
                    {{ data.shippingFee != null ? data.shippingFee : "" }}
                </p>
                <p>
                    <span class="inforTit">促销价格:</span>
                    {{
                        data.couponAmount != null &&
                        data.preferentialPrice != null
                            ? (
                                  data.preferentialPrice - data.couponAmount
                              ).toFixed(2)
                            : ""
                    }}
                </p>
                <p>
                    <span class="inforTit">优惠券:</span>
                    {{ data.couponAmount != null ? data.couponAmount : "" }}
                </p>
                <p><span class="inforTit">积分:</span>--</p>
                <p>
                    <span class="inforTit">应支付金额:</span>
                    {{ data.orderAmount != null ? data.orderAmount : "" }}
                </p>
            </div>
            <div class="orderConList">
                <div>
                    <span class="title">发票信息</span>
                </div>
                <p><span class="inforTit">发票类型:</span>--</p>
                <p><span class="inforTit">发票抬头:</span>--</p>
                <p><span class="inforTit">纳税人识别号:</span>--</p>
                <p><span class="inforTit">发票内容:</span>--</p>
            </div>
        </div>
        <div>
            <div class="title" style="margin-top: 15px">
                商品信息
                <span style="margin-left: 15px; font-size: 13px">{{
                    data.storeName
                }}</span>
                <span style="margin-left: 15px; font-size: 13px"
                    >店铺ID: {{ data.storeId }}</span
                >
            </div>
            <el-table :data="orderData" border style="width: 100%">
                <el-table-column
                    prop="goodsName"
                    label="商品名称"
                    width="240"
                    :show-overflow-tooltip="true"
                    align="left"
                >
                    <template slot-scope="scope">
                        <div
                            style="
                                text-align: left;
                                text-overflow: ellipsis;
                                overflow: hidden;
                                white-space: nowrap;
                            "
                            :title="scope.row.goodsName"
                        >
                            <img
                                :src="scope.row.goodsImage | filterImgUrl"
                                width="40px"
                                height="40px"
                            />
                            <label>{{ scope.row.goodsName }} </label>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="specSerial"
                    label="商品货号"
                    width="180"
                    align="center"
                ></el-table-column>
                <el-table-column prop="specPrice" label="单价" align="center">
                    <template slot-scope="scope">{{
                        scope.row.specPrice != ""
                            ? "￥" + scope.row.specPrice
                            : ""
                    }}</template>
                </el-table-column>
                <el-table-column prop="rudePri" label="优惠金额">
                    <template slot-scope="scope">{{ "￥" + "0.00" }}</template>
                </el-table-column>
                <el-table-column prop="num" label="赠送积分"></el-table-column>
                <el-table-column
                    prop="score"
                    label="积分优惠购"
                ></el-table-column>
                <el-table-column prop="goodsNum" label="数量"></el-table-column>
                <el-table-column prop="goodsTotalPayPrice" label="应付金额">
                    <template slot-scope="scope">
                        {{
                            scope.row.goodsTotalPayPrice != ""
                                ? "￥" + scope.row.goodsTotalPayPrice
                                : ""
                        }}
                    </template>
                </el-table-column>
            </el-table>
            <div class="summary">
                <ul>
                    <li>
                        <span class="inforTit">商品总价:</span>
                        {{
                            data.goodsAmount != null
                                ? "￥" + data.goodsAmount
                                : ""
                        }}
                    </li>
                    <li>
                        <span class="inforTit">运费:</span>
                        {{
                            data.shippingFee != null
                                ? "￥" + data.shippingFee
                                : ""
                        }}
                    </li>
                    <li>
                        <span class="inforTit">促销金额:</span>
                        {{
                            data.couponAmount != null &&
                            data.preferentialPrice != null
                                ? "￥" +
                                  (
                                      data.preferentialPrice - data.couponAmount
                                  ).toFixed(2)
                                : ""
                        }}
                    </li>
                    <li>
                        <span class="inforTit">优惠券:</span>
                        {{
                            data.couponAmount != null
                                ? "￥" + data.couponAmount
                                : ""
                        }}
                    </li>
                    <li><span class="inforTit">积分抵扣:</span>--</li>
                    <li>
                        <span class="inforTit">应付金额:</span>
                        {{
                            data.orderAmount != null
                                ? "￥" + data.orderAmount
                                : ""
                        }}
                    </li>
                </ul>
            </div>
        </div>

        <div class="operationRecord">
            <p>
                <span class="title">操作日志</span>
            </p>
            <el-table :data="orderLog" style="width: 100%" border>
                <el-table-column
                    prop="creator"
                    label="操作人"
                    width="180"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="statusInfo"
                    label="操作描述"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    label="操作时间"
                    align="center"
                ></el-table-column>
            </el-table>
        </div>

        <!--返回按钮-->
        <div style="margin-top: 40px; margin-bottom: 40px">
            <el-button
                type="primary"
                style="display: block; margin: 0 auto"
                @click="changePage(1)"
                >返回</el-button
            >
        </div>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import Clipboard from "clipboard";
    import { paymentOrder } from "@/api/api";

    export default {
        data() {
            return {
                orderDetData: ["财务管理", "支付管理", "交易记录", "订单详情"],
                activeStep: 1,
                loading: false,
                addressInfo: "",
                orderData: [],
                packageInfo: [], //物流
                orderLog: [], //日志
                data: "",
                row: "",
                textarea: "",
            };
        },
        components: {
            Bread,
        },
        methods: {
            init(row) {
                this.row = row;
                this.backScan();
            },
            copyOrder() {
                var clipboard = new Clipboard(".btn");
                let that = this;
                clipboard.on("success", (e) => {
                    that.$message({
                        message: "订单号复制成功",
                        type: "success",
                        duration: 1000,
                    });
                    clipboard.destroy();
                });

                clipboard.on("error", (e) => {
                    that.$message({
                        message: "订单号复制失败，请重新复制",
                        type: "error",
                        duration: 1000,
                    });
                    clipboard.destroy();
                });
            },
            //查看手机号
            lookPhoneNum() {
                if (/\*\*\*\*/.test(this.addressInfo.mobPhone)) {
                    this.addressInfo.mobPhone = this.allmobPhone;
                } else {
                    this.addressInfo.mobPhone = this.allmobPhone.replace(
                        this.allmobPhone.substring(3, 7),
                        "****"
                    );
                }
            },
            backScan() {
                const obj = { orderSn: this.row.orderSn };
                this.loading = true;
                paymentOrder(obj).then((res) => {
                    this.loading = false;
                    if (res.code == 200) {
                        console.log(res, "333s");
                        this.addressInfo = res.data.orderAddressDTO; //收货人信息
                        this.orderData = res.data.orderGoodsDTOList;
                        this.orderLog = res.data.orderLogDTOList;
                        this.data = res.data;
                        if (res.data.orderLogisticsDTO) {
                            this.packageInfo = res.data.orderLogisticsDTO.data; //物流
                        } else {
                            this.packageInfo = [];
                        }
                        this.allmobPhone = this.addressInfo.mobPhone;
                        this.addressInfo.mobPhone = this.allmobPhone.replace(
                            this.allmobPhone.substring(3, 7),
                            "****"
                        );
                        if (this.data.orderStatus == 10) {
                            this.activeStep = 1; //提交订单
                        } else if (this.data.orderStatus == 20) {
                            this.activeStep = 2; //付款成功
                        } else if (
                            this.data.orderStatus == 30 ||
                            this.data.orderStatus == 0
                        ) {
                            this.activeStep = 3; //商品出库 或者 取消订单
                        } else {
                            this.activeStep = 5;
                        }
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            },
            //返回上一级
            changePage(nums) {
                this.$emit("chagePageNum", nums);
            },
        },
    };
</script>
<style lang="scss" scoped>
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

    .orderLogText {
        font-size: 15px;
        margin-left: 15px;
    }

    .orderLogTimeText {
        font-size: 15px;
        font-weight: 600;
    }

    .orderLogisticsText {
        overflow: auto;
        width: 80%;
        height: 100%;
        padding: 10px;
        justify-content: flex-start;
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
    .disPlayRow {
        display: flex;
    }
    .disPlayColumn {
        display: flex;
        flex-direction: column;
    }
    .marginTop0 {
        margin-top: 0 !important;
    }

    .orderText {
        width: 80%;
        height: 100%;
        display: flex;
        padding: 10px;
        justify-content: center;
    }

    .orderInfo {
        width: 26.5%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: 100%;
        display: flex;
        flex-direction: column;
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
        display: flex;
        flex-direction: column;
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

    .title {
        font-size: 16px;
        font-weight: 700;
        margin-bottom: 8px;
    }

    .inforTit {
        font-size: 15px;
        width: 100px;
        font-weight: 600;
        text-align: left;
        color: #333;
        display: inline-block;
    }
</style>
