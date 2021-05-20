<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <!--导出按钮-->
            <!-- <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList" v-if="$hasPermission('sys:order:export')"></importAndExport> -->
            <el-button
                style="
                    right: 20px;
                    top: 12px;
                    position: absolute;
                    padding: 9px 20px;
                "
                v-if="
                    importAndExportOptions && importAndExportOptions.exportUrl
                "
                class="btn"
                @click="exportExcel"
            >
                <img
                    :src="importAndExportOptions.exportSrc"
                    style="width: 20px; height: 20px"
                    alt=""
            /></el-button>

            <el-form
                :inline="true"
                ref="dataForm"
                class="grayLine topGapPadding"
                :model="dataForm"
                :rules="dataRule"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="订单编号：" prop="orderSn">
                    <el-input
                        v-model="dataForm.orderSn"
                        maxlength="20"
                        placeholder="订单编号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="会员名称：" prop="buyerName">
                    <el-input
                        v-model="dataForm.buyerName"
                        maxlength="20"
                        placeholder="会员名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="商户名称：" prop="storeIdOrName">
                    <el-input
                        v-model="dataForm.storeIdOrName"
                        maxlength="60"
                        placeholder="商户名称"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="支付方式：" prop="paymentCode">
                    <el-select
                        v-model="dataForm.paymentCode"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option
                            v-for="(item, index) in paymentList"
                            :label="item.paymentName"
                            :value="item.paymentCode"
                            :key="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="支付状态：" prop="paymentStatus">
                    <el-select
                        v-model="dataForm.paymentStatus"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未付款" value="0"></el-option>
                        <el-option label="已付款" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="下单时间：">
                    <el-date-picker
                        v-model="timeArr"
                        type="datetimerange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        align="left"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                    >
                    </el-date-picker>
                </el-form-item>
                <!-- <el-form-item  label="交易渠道:">
                        <el-input v-model="dataForm.paramCode" placeholder="请选择" clearable></el-input>
                </el-form-item>-->
                <el-form-item label="交易时间：">
                    <el-date-picker
                        v-model="timeArr2"
                        type="datetimerange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        align="left"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="订单类型：" prop="virtualFlag">
                    <el-select
                        v-model="dataForm.virtualFlag"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value></el-option>
                        <el-option label="实体订单" value="0"></el-option>
                        <el-option label="虚拟订单" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button
                        class="btn"
                        type="primary"
                        plain
                        @click="reset('dataForm')"
                        >重置</el-button
                    >
                </el-form-item>
                <br />
                <!-- <el-form-item>
                <el-button calss="btn" @click="reset()" style="color:#999999FF;">导出</el-button>
                </el-form-item>-->
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="radio1"
                    @change="agreeChange"
                    v-if="isShow"
                    style="margin-top: 0px; margin-bottom: 0px"
                >
                    <el-radio-button label="">所有订单</el-radio-button>
                    <el-radio-button label="10">待支付</el-radio-button>
                    <el-radio-button label="20">待发货</el-radio-button>
                    <el-radio-button label="30">待收货</el-radio-button>
                    <el-radio-button label="40">已完成</el-radio-button>
                    <el-radio-button label="0">已取消</el-radio-button>
                </el-radio-group>
                <div v-if="!isShow"></div>
                <div style="display: flex">
                    <mainSwitch></mainSwitch>
                    <mainTipsMessage></mainTipsMessage>
                </div>
            </div>
            <el-alert
                title="操作提示"
                type="warning"
                @close="$store.commit('showListMessage')"
                v-show="$store.state.listMessageFlag"
            >
                <template slot="title">
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">
                        1、点击查看订单操作将显示该订单（包括订单优惠明细）的详细信息
                    </div>
                    <div class="iconSize">
                        2、订单状态为：待支付、待发货、待收货、已完成、已取消
                    </div>
                </template>
            </el-alert>
        </div>

        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="dataListLoading"
            style="width: 100%; maigin-top: 10px"
            slot="empty"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">{{
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>
            <el-table-column
                prop="orderSn"
                label="订单编号"
                align="center"
                width="180"
            ></el-table-column>
            <el-table-column
                prop="buyerName"
                label="会员名称"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="storeName"
                label="商户名称"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="createDate"
                label="下单时间"
                align="center"
                width="160"
            ></el-table-column>
            <el-table-column
                prop="paymentTime"
                label="交易时间"
                align="center"
                width="160"
            ></el-table-column>
            <el-table-column
                prop="paymentName"
                label="支付方式"
                align="center"
                width="80"
            ></el-table-column>
            <el-table-column
                prop="orderAmount"
                label="订单金额"
                align="right"
                width="100"
            >
                <template slot-scope="scope"
                    >￥{{ scope.row.orderAmount }}</template
                >
            </el-table-column>
            <el-table-column
                prop="orderStatus"
                label="订单状态"
                align="center"
                width="100"
                :formatter="orderState"
            >
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <!-- <el-button type="primary" @click="submitStore()">{{ $t('confirm') }}</el-button> -->
                    <el-button
                        size="mini"
                        type="text"
                        @click="goOrderDetail(scope.row)"
                        v-if="$hasPermission('sys:order:view')"
                        >查看订单</el-button
                    >
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import importAndExport from "@/components/import-and-export";

    import { orderExport } from "@/api/io";
    import { orderlists } from "@/api/url";
    import { paymentList } from "@/api/api";
    import discountDet from "./discountDet";
    import orderDet from "./orderDet";
    import mixinViewModule from "@/mixins/view-module";
    export default {
        mixins: [mixinViewModule],
        data() {
            var validateOrderSn = (rule, value, callback) => {
                if (value) {
                    var myReg = /[\u4E00-\u9FA5]/;
                    if (myReg.test(value)) {
                        return callback(new Error("订单编号不能含有中文"));
                    }
                }
                callback();
            };
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: orderExport, //导出接口
                    exportSrc: require("@/assets/img/export.png"),
                },
                mixinViewModuleOptions: {
                    getDataListURL: orderlists,
                    getDataListIsPage: true,
                    exportURL: "",
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                dataRule: {
                    orderSn: [
                        {
                            validator: validateOrderSn,
                            trigger: "blur",
                        },
                    ],
                },
                paymentList: "", //支付方式
                formInline: {}, //订单优惠明细
                textarea: "",
                // breaddataHome: ["订单系统", "订单管理", "订单列表"],
                // orderDetBreaddata:["订单管理", "订单管理", "订单列表", "订单详情"],
                dataListLoading: false,
                detailOrList: 1,
                radio1: "",
                tableData: [],
                // orderData: [],
                dataForm: {
                    orderSn: "",
                    storeIdOrName: "",
                    buyerName: "",
                    paymentCode: "",
                    paymentStatus: "",
                    virtualFlag: "",
                    startCreateDate: "",
                    endCreateDate: "",
                    endPaymentTime: "",
                    startPaymentTime: "",
                },
                tableData: [],
                timeArr: "", //下单时间数据
                timeArr2: "", //交易时间数据
                // addressInfo: [], //地址数据
                // packageInfo: [], //包裹数据
                // orderLog: [], //操作日志
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                orderState: function (row, column) {
                    return row.orderStatus == 0 ? (
                        <el-tag type="info"> 已取消 </el-tag>
                    ) : row.orderStatus == 10 ? (
                        <el-tag type="warning"> 待付款 </el-tag>
                    ) : row.orderStatus == 20 ? (
                        <el-tag type="warning"> 待发货 </el-tag>
                    ) : row.orderStatus == 30 ? (
                        <el-tag type="warning"> 待收货 </el-tag>
                    ) : (
                        <el-tag type="success"> 已完成 </el-tag>
                    );
                },
                tableheight: document.body.offsetHeight - 470,
                tableheightBig: 300,
            };
        },
        props: ["status", "breaddata"],
        created() {
            //处理不同状态
            this.isShow = this.status !== undefined ? false : true;
            this.radio1 = this.status == undefined ? "" : this.status;
            this.dataForm.orderStatus = this.status == undefined ? "" : this.status;
            this.getPaymentList();
        },

        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        115;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            getData() {
                if (this.timeArr && this.timeArr.length == 2) {
                    this.dataForm.startCreateDate = this.timeArr[0];
                    this.dataForm.endCreateDate = this.timeArr[1];
                } else {
                    this.dataForm.startCreateDate = "";
                    this.dataForm.endCreateDate = "";
                }
                if (this.timeArr2 && this.timeArr2.length == 2) {
                    this.dataForm.startPaymentTime = this.timeArr2[0];
                    this.dataForm.endPaymentTime = this.timeArr2[1];
                } else {
                    this.dataForm.startPaymentTime = "";
                    this.dataForm.endPaymentTime = "";
                }
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //订单支付方式
            getPaymentList() {
                paymentList().then((res) => {
                    if (res.code == 200) {
                        this.paymentList = res.data;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            },
            //订单状态筛选
            agreeChange(val) {
                this.dataForm.page = 1;
                this.dataForm.limit = 10;
                this.params = {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                };
                this.dataForm.orderStatus = val;
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset(dataForm) {
                this.dataForm.orderSn = "";
                this.dataForm.buyerName = "";
                this.dataForm.paymentCode = "";
                this.dataForm.paymentStatus = "";
                this.dataForm.virtualFlag = "";
                this.dataForm.storeIdOrName = "";
                this.timeArr = [];
                this.timeArr2 = [];
                this.dataForm.startCreateDate = "";
                this.dataForm.endtime = "";
                this.dataForm.startPaymentTime = "";
                this.dataForm.endPaymentTime = "";
                this.$refs["dataForm"].resetFields();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //订单导出
            exportExcel() {
                let that = this;
                var obj = {
                    params: {
                        ...this.dataForm,
                    },
                };
                this.$http
                    .get(`${this.importAndExportOptions.exportUrl}`, obj)
                    .then((res) => {
                        that.$message({
                            message: "请到系统管理-下载管理中下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
            //订单详情
            goOrderDetail(row) {
                //   this.showOrderDetailVisible = true;
                //   this.discountDetVisible = false
                //   this.$nextTick(()=>{
                //     this.$refs.orderDetailCompon.init(row);
                //   })
                this.$emit("hiddenList", row);
            },
        },
        components: {
            Bread,
            orderDet,
            discountDet,
            importAndExport,
        },
    };
</script>
<style scoped>
    .el-popper {
        left: 292px !important;
    }

    /* .el-table--border {
                                                    margin-top: 20px;
                                                } */

    .el-radio-group {
        margin-top: 20px;
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
        height: 70px;
        margin: 5px 10px;
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
        justify-content: center;
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

    .order {
        display: flex;
    }

    .order p {
        margin-left: 20px;
    }

    .ssss {
        display: flex;
        height: auto;
        align-items: center;
    }

    /deep/ .el-form-item {
        margin-top: 0px !important;
    }
</style>