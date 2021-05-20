<template>
    <!-- v-if="detailOrList==1" -->
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata ? breaddata : breaddataHome"></Bread>

            <!--导出按钮-->
            <!--    <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList" v-if="$hasPermission('sys:order:export')"></importAndExport>-->
            <!--    <el-button style="right: 20px;top: 12px;position: absolute" v-if="importAndExportOptions && importAndExportOptions.exportUrl" class="btn" @click="exportExcel">{{importAndExportOptions.exportWord}}</el-button>-->

            <el-form
                :inline="true"
                ref="dataForm"
                class="grayLine topGapPadding"
                :model="dataForm"
                :rules="dataRule"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="订单编号：">
                    <el-input
                        v-model="dataForm.orderSn"
                        maxlength="20"
                        placeholder="订单编号"
                        clearable
                    ></el-input>
                </el-form-item>
                <!-- <el-form-item label="商户ID/名称：" prop="storeIdAndName">
        <el-input v-model="dataForm.storeIdAndName" placeholder="商户ID/名称" clearable></el-input>
      </el-form-item>-->
                <el-form-item label="会员名称：" prop="buyerName">
                    <el-input
                        v-model="dataForm.buyerName"
                        maxlength="20"
                        placeholder="会员名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="支付方式：" prop="paymentId">
                    <el-select
                        v-model="dataForm.paymentId"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value></el-option>
                        <el-option
                            v-for="(item, index) in paymentList"
                            :label="item.paymentName"
                            :value="item.id"
                            :key="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="支付状态：" prop="paymentStatus">
                    <el-select
                        v-model="dataForm.paymentStatus"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value></el-option>
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
                    ></el-date-picker>
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
                    ></el-date-picker>
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
                <div>
                    <el-radio-group
                        v-model="radio1"
                        @change="agreeChange"
                        v-if="isShow"
                        style="margin-top: 5px; margin-bottom: 5px"
                    >
                        <el-radio-button label>所有订单</el-radio-button>
                        <el-radio-button label="10">待支付</el-radio-button>
                        <el-radio-button label="999">待拼团</el-radio-button>
                        <el-radio-button label="20">待发货</el-radio-button>
                        <el-radio-button label="30">待收货</el-radio-button>
                        <el-radio-button label="40">已完成</el-radio-button>
                        <el-radio-button label="0">已取消</el-radio-button>
                    </el-radio-group>
                </div>
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
                        2、订单状态为：待支付、待拼团、待发货、待收货、已完成、已取消
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%; margin-top: 8px"
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
                width="130"
            ></el-table-column>
            <!-- <el-table-column prop="storeId" label="商户ID" align="center" width="200"></el-table-column> -->
            <el-table-column
                prop="createDate"
                label="下单时间"
                align="center"
                width="170"
            ></el-table-column>
            <el-table-column
                prop="paymentTime"
                label="交易时间"
                align="center"
                width="170"
            ></el-table-column>
            <el-table-column
                prop="paymentName"
                label="支付方式"
                align="center"
            ></el-table-column>
            <el-table-column prop="orderAmount" label="订单金额" align="right">
                <template slot-scope="scope"
                    >￥{{ scope.row.orderAmount }}</template
                >
            </el-table-column>
            <el-table-column
                prop="orderStatus"
                label="订单状态"
                align="center"
                :formatter="orderState"
            ></el-table-column>
            <el-table-column label="操作" width="290" align="center">
                <div slot-scope="scope" style="display: inline-flex">
                    <el-button
                        size="mini"
                        type="text"
                        @click="delGoods(scope.row)"
                        v-if="
                            scope.row.orderStatus == 20 &&
                            scope.row.groupStatus == 1 &&
                            $hasPermission('sys:order:shippment')
                        "
                        >发货</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        @click="seeGroupDetail(scope.row)"
                        v-if="
                            scope.row.orderStatus != 10 &&
                            scope.row.paymentStatus != 0 &&
                            $hasPermission('sys:order:view')
                        "
                        >拼团详情</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(scope.row, 2)"
                        v-if="$hasPermission('sys:order:view')"
                        >查看订单</el-button
                    >
                    <el-button
                        v-if="
                            scope.row.orderStatus == 10 ||
                            (scope.row.orderStatus == 20 &&
                                $hasPermission('sys:order:cancel'))
                        "
                        size="mini"
                        type="text"
                        @click="cancleOrderFn(scope.row)"
                        >取消订单</el-button
                    >
                </div>
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
        <el-dialog
            title="标记发货"
            :visible.sync="dialogGoods"
            v-if="dialogGoods"
            width="470px"
        >
            <el-form
                :model="goodsform"
                ref="goodsform"
                :rules="dataRule"
                class="sendModal"
            >
                <!-- <el-form-item label="分单发货：" :label-width="formLabelWidth" prop="isSplit">
          <el-radio-group v-model="goodsform.isSplit" style="margin-top:0px;margin-left:10px;">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1" disabled>是</el-radio>
          </el-radio-group>
        </el-form-item> -->
                <el-form-item
                    label="物流方式："
                    :label-width="formLabelWidth"
                    prop="devlierType"
                >
                    <el-radio-group
                        v-model="goodsform.devlierType"
                        style="margin-top: 0px; margin-left: 10px"
                    >
                        <el-radio :label="1">快递</el-radio>
                        <!-- <el-radio :label="2">自提</el-radio> -->
                        <el-radio :label="0">无需物流</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item
                    label="物流公司："
                    :label-width="formLabelWidth"
                    prop="name"
                    v-if="goodsform.devlierType == 1"
                >
                    <el-select
                        v-model="goodsform.name"
                        placeholder="请选择物流公司"
                        style="width: 220px"
                    >
                        <el-option
                            :label="item.companyName"
                            :value="`${item.companyName}|${item.companyId}|${item.companyPhone}`"
                            v-for="(item, index) in logList"
                            :key="index"
                            >{{ item.companyName }}</el-option
                        >
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="物流单号："
                    :label-width="formLabelWidth"
                    prop="transportCode"
                    v-if="goodsform.devlierType == 1"
                >
                    <el-input
                        v-model="goodsform.transportCode"
                        maxlength="20"
                        autocomplete="off"
                        style="width: 220px"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center">
                <el-button @click="dialogGoods = false">取 消</el-button>
                <el-button type="primary" @click="goodsLog">确 定</el-button>
            </div>
        </el-dialog>
        <modelCancleOrder
            v-if="cancleOrderVisible"
            ref="cancleOrderCompon"
            @searchDataList="getDataList"
        ></modelCancleOrder>
    </div>
    <!-- <orderDet
    v-else-if="detailOrList==2"
    @changePage="changePage"
    :data="data"
    :addressInfo="addressInfo"
    :orderLog="orderLog"
    :packageInfo="packageInfo"
    :orderData="orderData"
    :orderDetBreadData="orderDetBreadData"
  ></orderDet>
  <discountDet v-else @changeState="changeState"></discountDet> -->
</template>

<script>
    import importAndExport from "@/components/import-and-export";
    import { orderExport } from "@/api/io";
    import Bread from "@/components/bread";
    import modelCancleOrder from "../modules/model-cancle-order";
    import { ordergrouppage } from "@/api/url";
    import { orderDetail, paymentList, delGoodsLog, logCompany } from "@/api/api";
    // import discountDet from "./discountDet";
    // import orderDet from "./orderDet";
    import mixinViewModule from "@/mixins/view-module";
    import { types } from "util";
    export default {
        mixins: [mixinViewModule],
        data() {
            //订单编号校验
            var validateOrderSn = (rule, value, callback) => {
                if (value) {
                    var myReg = /[\u4E00-\u9FA5]/;
                    if (myReg.test(value)) {
                        return callback(new Error("订单编号不能含有中文"));
                    }
                }
                callback();
            };
            //物流单号
            var transport = (rule, value, callback) => {
                if (value == "") {
                    callback(new Error("物流单号不得为空"));
                } else if (value.length > 15) {
                    callback(new Error("物流单号最长限制15位"));
                } else {
                    callback();
                }
            };
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: orderExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: ordergrouppage,
                    getDataListIsPage: true,
                    exportURL: "",
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                cancleOrderVisible: false, //取消订单弹框
                logIndex: "", //物流公司下标
                logList: "", //物流公司
                formLabelWidth: "120px",
                paymentList: "", //支付方式
                dialogGoods: false, //发货
                goodsform: {
                    devlierType: 1,
                    isSplit: 0,
                    transportCode: "",
                    name: "",
                }, //发货表单
                formInline: {}, //订单优惠明细
                textarea: "",
                breaddataHome: ["订单系统", "订单管理", "拼团列表"],
                //   orderDetBreadData: ["订单系统", "订单管理", "订单列表", "订单详情"],
                dataListLoading: false,
                //   detailOrList: 1,
                radio1: "",
                tableData: [],
                orderData: [],
                dataForm: {
                    orderSn: "",
                    // storeIdAndName: "",
                    buyerName: "",
                    paymentId: "",
                    paymentStatus: "",
                    startCreateDate: "",
                    endCreateDate: "",
                    endPaymentTime: "",
                    startPaymentTime: "",
                },
                tableData: [],
                timeArr: "", //下单时间数据
                timeArr2: "", //交易时间数据
                addressInfo: [], //地址数据
                packageInfo: [], //包裹数据
                orderLog: [], //操作日志
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                dataRule: {
                    isSplit: [
                        {
                            required: true,
                            message: "是否拆单不得为空",
                            trigger: "blur",
                        },
                    ],
                    devlierType: [
                        {
                            required: true,
                            message: "配送方式不得为空",
                            trigger: "blur",
                        },
                    ],
                    name: [
                        {
                            required: true,
                            message: "请输入物流公司",
                            trigger: "blur",
                        },
                    ],
                    transportCode: [
                        { required: true, validator: transport, trigger: "blur" },
                    ],
                    orderSn: [{ validator: validateOrderSn, trigger: "blur" }],
                },
                orderState: function (row, column) {
                    return row.orderStatus == 0 ? (
                        <el-tag type="info">已取消</el-tag>
                    ) : row.orderStatus == 10 ? (
                        <el-tag type="warning">待付款</el-tag>
                    ) : row.orderStatus == 20 && row.groupStatus == 1 ? (
                        <el-tag type="warning">待发货</el-tag>
                    ) : row.orderStatus == 20 && row.groupStatus == 0 ? (
                        <el-tag type="warning">待拼团</el-tag>
                    ) : row.orderStatus == 30 ? (
                        <el-tag type="warning">待收货</el-tag>
                    ) : (
                        <el-tag type="success">已完成</el-tag>
                    );
                },
                tableheight: document.body.offsetHeight - 470,
                tableheightBig: 300,
            };
        },
        watch: {
            // 订单编号
            "dataForm.orderSn": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    // 删除输入的汉字
                    if (/^[\u4E00-\u9FA5]*$/.test(newV[i])) {
                        this.dataForm.orderSn = newV.replace(newV[i], "");
                    }
                }
            },
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        computed: {},
        components: {
            Bread,
            // orderDet,
            // discountDet,
            modelCancleOrder,
            importAndExport,
        },
        props: ["status", "breaddata"],
        created() {
            //处理不同状态
            this.isShow = this.status !== undefined ? false : true;
            this.radio1 = this.status == undefined ? "" : this.status;
            this.dataForm.orderStatus = this.status == undefined ? "" : this.status;
            this.getPaymentList();
        },
        methods: {
            // 单独的导出按钮
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
                            message: "请到店铺管理-导出管理中下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
            //确定发货
            goodsLog() {
                this.$refs["goodsform"].validate((valid) => {
                    if (valid) {
                        let [
                            companyName,
                            companyId,
                            companyPhone,
                        ] = this.goodsform.name.split("|");
                        const obj = {
                            devlierType: this.goodsform.devlierType,
                            id: this.goodsform.id,
                            isSplit: this.goodsform.isSplit,
                            transportCode: this.goodsform.transportCode,
                            transportCompanyName: companyName,
                            transportCompanyId: companyId,
                            transportCompanyPhone:
                                companyPhone == "null" ? "" : companyPhone,
                        };
                        delGoodsLog(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.dialogGoods = false;
                                this.goodsform = {
                                    devlierType: 1,
                                    isSplit: 0,
                                    transportCode: "",
                                    name: "",
                                };
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg,
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取物流公司
            getLogCom() {
                logCompany().then((res) => {
                    if (res.code == 200) {
                        this.logList = res.data;
                    } else {
                        console.log("获取物流公司失败");
                    }
                });
            },
            //发货
            delGoods(item) {
                this.goodsform.id = item.id;
                this.dialogGoods = true;
                this.getLogCom();
            },
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
                // 状态码  999  == 拼团中
                if (val == 999) {
                    this.dataForm.orderStatus = "";
                    this.dataForm.groupStatus = 0;
                } else if (val == 20) {
                    this.dataForm.orderStatus = 20;
                    this.dataForm.groupStatus = 1;
                } else {
                    this.dataForm.orderStatus = val;
                    this.dataForm.groupStatus = "";
                }
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset(dataForm) {
                this.dataForm.orderSn = "";
                this.dataForm.buyerName = "";
                this.dataForm.paymentId = "";
                this.dataForm.paymentStatus = "";
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
            seeGroupDetail(row) {
                // this.$router.push({
                // 	path:'/mgoperative-group-record',
                // 	query: {
                // 		tabIndex:2,
                // 		groupRecordId:row.groupRecordId
                // 	}
                // })
                this.$emit("showGroupDetail", row);
            },
            changeCompent(row, index) {
                this.$emit("changeCompent", row, index);
            },
            // 取消订单
            cancleOrderFn(row) {
                console.log(row);
                this.cancleOrderVisible = true;
                this.$nextTick(() => {
                    this.$refs.cancleOrderCompon.init(row);
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .sendModal {
        /deep/ .el-form-item__content {
            display: block !important;
        }
    }
    .el-popper {
        left: 292px !important;
    }
    .el-table--border {
        margin-top: 20px;
    }
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
