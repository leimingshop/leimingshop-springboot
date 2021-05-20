<template>
    <!-- v-if="detailOrList==1" -->
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata ? breaddata : breaddataHome"></Bread>

            <el-form
                :inline="true"
                ref="dataForm"
                class="grayLine topGapPadding"
                :model="dataForm"
                :rules="dataRule"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="会员名称：" prop="buyerName">
                    <el-input
                        v-model="dataForm.buyerName"
                        maxlength="20"
                        placeholder="会员名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="商品编号：" prop="goodsSerial">
                    <el-input
                        v-model="dataForm.goodsSerial"
                        maxlength="20"
                        placeholder="商品编号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="订单编号：" prop="orderSn">
                    <el-input
                        v-model="dataForm.orderSn"
                        maxlength="20"
                        placeholder="订单编号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="商品名称：" prop="goodsName">
                    <el-input
                        v-model="dataForm.goodsName"
                        maxlength="60"
                        placeholder="商品名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="店铺名称：" prop="storeName">
                    <el-input
                        v-model="dataForm.storeName"
                        maxlength="60"
                        placeholder="店铺名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="核销状态：" prop="codeStatus">
                    <el-select
                        v-model="dataForm.codeStatus"
                        placeholder="请选择核销状态"
                    >
                        <el-option label="全部" value></el-option>
                        <el-option label="待核销" value="0"></el-option>
                        <el-option label="部分核销" value="1"></el-option>
                        <el-option label="已核销" value="2"></el-option>
                        <el-option label="已过期" value="3"></el-option>
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
            </el-form>

            <div
                style="
                    height: 40px;
                    margin-top: 5px;
                    margin-bottom: -7px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    over-flow: hidden;
                "
            >
                <div></div>
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
                        1、点击查看详情操作将显示该商品的核销详细信息
                    </div>
                    <div class="iconSize">
                        2、核销状态为：待核销、部分核销、已核销、已过期
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%; maigin-top: 10px"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="50"
            >
                <template slot-scope="scope">{{
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>
            <el-table-column
                prop="memberName"
                label="会员名称"
                align="center"
                width="120"
            ></el-table-column>
            <el-table-column
                prop="goodsSerial"
                label="商品编号"
                align="center"
                width="120"
            ></el-table-column>
            <el-table-column
                prop="goodsName"
                label="商品名称"
                align="center"
                width="200"
            >
                <template slot-scope="scope">
                    <div
                        style="display: flex; cursor: pointer"
                        @click="previewH5Fn(scope.row)"
                    >
                        <img
                            :src="scope.row.goodsImage | filterImgUrl"
                            width="40px"
                            height="40px"
                        />
                        <div class="towEllipsis" style="margin-left: 8px">
                            <el-tooltip
                                class="item"
                                effect="dark"
                                :content="scope.row.goodsName"
                                v-if="scope.row.goodsName.length > 20"
                                placement="top-start"
                            >
                                <span
                                    style="
                                        color: #4e80db;
                                        text-decoration: none;
                                        cursor: pointer;
                                    "
                                    >{{ scope.row.goodsName }}</span
                                >
                            </el-tooltip>
                            <span
                                v-else
                                style="
                                    color: #4e80db;
                                    text-decoration: none;
                                    cursor: pointer;
                                "
                                >{{ scope.row.goodsName }}</span
                            >
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="storeName"
                label="店铺名称"
                align="center"
                width="120"
            ></el-table-column>
            <el-table-column
                prop="orderSn"
                label="订单编号"
                align="center"
                width="200"
            ></el-table-column>
            <el-table-column
                prop="goodsNum"
                label="商品数量"
                align="center"
                width="90"
            ></el-table-column>
            <el-table-column
                prop="goodsPrice"
                label="商品单价"
                align="center"
                width="100"
            ></el-table-column>
            <el-table-column
                prop="expiryDate"
                label="有效期至"
                align="center"
                width="180"
            ></el-table-column>
            <el-table-column
                prop="codeStatus"
                label="状态"
                align="center"
                width="120"
                :formatter="codeStatus"
            >
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="center">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(scope.row, 2)"
                        v-if="$hasPermission('sys:order:view')"
                        >查看详情</el-button
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
    import { getFetchCodePage } from "@/api/url";

    import mixinViewModule from "@/mixins/view-module";
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
                mixinViewModuleOptions: {
                    getDataListURL: getFetchCodePage,
                    getDataListIsPage: true,
                    exportURL: "",
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },

                breaddataHome: ["订单系统", "订单管理", "电子提货码列表"],
                dataListLoading: false,
                radio1: "",
                tableData: [],
                orderData: [],
                dataForm: {
                    orderSn: "",
                    // storeIdAndName: "",
                    buyerName: "",
                    paymentId: "",
                    codeStatus: "",
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
                    orderSn: [
                        {
                            validator: validateOrderSn,
                            trigger: "blur",
                        },
                    ],
                },
                codeStatus: function (row, column) {
                    return row.codeStatus == 0 ? (
                        <el-tag type="info"> 待核销 </el-tag>
                    ) : row.codeStatus == 1 ? (
                        <el-tag type="warning"> 部分核销 </el-tag>
                    ) : row.codeStatus == 2 ? (
                        <el-tag type="success"> 已核销 </el-tag>
                    ) : row.codeStatus == 3 ? (
                        <el-tag type="warning"> 已过期 </el-tag>
                    ) : (
                        <el-tag type="success"> 已完成 </el-tag>
                    );
                },
                tableheight: document.body.offsetHeight - 470,
                tableheightBig: 300,
            };
        },
        computed: {},
        components: {
            Bread,
        },
        props: ["status", "breaddata"],
        created() {
            //处理不同状态
            this.isShow = this.status !== undefined ? false : true;
            this.radio1 = this.status == undefined ? "" : this.status;
            this.dataForm.orderStatus = this.status == undefined ? "" : this.status;
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
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcURL"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
            //重置
            reset(dataForm) {
                this.dataForm.orderSn = "";
                this.dataForm.buyerName = "";
                this.dataForm.paymentId = "";
                this.dataForm.codeStatus = "";
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

    .towEllipsis {
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }
</style>