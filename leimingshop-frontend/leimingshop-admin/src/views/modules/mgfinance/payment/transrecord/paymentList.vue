<template>
    <div v-if="isList">
        <div id="control-area">
            <bread :breaddata="breaddata"></bread>

            <!--导出按钮-->
            <importAndExport
                style="right: 20px; top: 12px; position: absolute"
                :importAndExportOptions="importAndExportOptions"
                :dataForm="dataForm"
                @getDataList="getDataList"
                v-if="$hasPermission('sys:payment:tally:export')"
            ></importAndExport>

            <el-form
                :inline="true"
                ref="dataForm"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <!-- <el-scrollbar style="height:90px;margin-right: 30px;"> -->
                <el-form-item label="交易流水号：" prop="paymentSn">
                    <el-input
                        v-model="dataForm.paymentSn"
                        placeholder="交易流水号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="交易单号：" prop="tradeSn">
                    <el-input
                        v-model="dataForm.tradeSn"
                        placeholder="交易单号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="交易渠道：" prop="paymentCode">
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
                <el-form-item label="用户名称：" prop="buyerName">
                    <el-input
                        v-model="dataForm.buyerName"
                        placeholder="用户名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="收支状态：" prop="transactionStatus">
                    <el-select
                        v-model="dataForm.transactionStatus"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="收" value="1"></el-option>
                        <el-option label="支" value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="交易时间：">
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
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button
                        class="btn"
                        @click="reset('dataForm')"
                        type="primary"
                        plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <div class="formControlArea">
                <div></div>
                <div style="display: flex">
                    <mainSwitch></mainSwitch>
                    <mainTipsMessage></mainTipsMessage>
                </div>
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
                <div class="iconSize">1、交易流水号为本平台生成</div>
                <div class="inconSize">2、交易单号根据交易渠道规则生成</div>
                <div class="inconSize">
                    3、收支用颜色进行区分，红色为支出，绿色为收入
                </div>
            </template>
        </el-alert>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column
                label="交易流水号"
                align="center"
                prop="paymentSn"
                min-width="190"
            ></el-table-column>
            <el-table-column
                prop="orderSn"
                label="订单编号"
                align="center"
                min-width="190"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.orderSn && scope.row.orderSn != null">
                  {{scope.row.orderSn}}
                </span>
                <span v-else>
                  {{"用户充值"}}
                </span>
              </template>
            </el-table-column>
            <el-table-column
                prop="buyerName"
                label="用户名"
                align="center"
                min-width="140"
            ></el-table-column>
            <el-table-column
                prop="tradeSn"
                label="交易单号"
                align="center"
                min-width="250"
            ></el-table-column>
            <el-table-column
                prop="createDate"
                label="交易时间"
                align="center"
                min-width="190"
            ></el-table-column>
            <el-table-column
                prop="paymentName"
                label="交易渠道"
                align="center"
                min-width="100"
            ></el-table-column>
            <el-table-column
                prop="paymentAmount"
                label="交易金额"
                align="right"
                min-width="120"
            >
                <template slot-scope="scope">
                    <span
                        :class="
                            scope.row.transactionStatus == 1
                                ? 'green'
                                : 'normalHeight'
                        "
                        >￥{{ scope.row.paymentAmount }}</span
                    >
                </template>
            </el-table-column>
            <el-table-column
                prop="transactionStatus"
                label="收/支"
                align="center"
                min-width="70"
                :formatter="statusRules"
            >
                <template slot-scope="scope">
                    <span
                        :class="
                            scope.row.transactionStatus == 1
                                ? 'green'
                                : 'normalHeight'
                        "
                        >{{
                            scope.row.transactionStatus == 1 ? "收" : "支"
                        }}</span
                    >
                </template>
            </el-table-column>

            <!--   <el-table-column prop="paymentRemarks" label="备注" align="center"></el-table-column>-->
            <el-table-column label="操作" min-width="120" align="center">
                <template slot-scope="scope">
                  <div v-if="scope.row.orderSn && scope.row.orderSn != null">
                    <span v-if="scope.row.isSplit == 0">
                        <el-button
                            size="mini"
                            type="text"
                            @click="goOrderDetail(scope.row.orderSn)"
                            v-if="$hasPermission('sys:order:view')"
                        >订单详情</el-button
                        >
                    </span>
                    <span v-if="scope.row.isSplit == 1">
                        <el-button
                            size="mini"
                            type="text"
                            @click="goOrderList(scope.row)"
                        >订单列表</el-button
                        >
                    </span>
                  </div>
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
        >
        </el-pagination>
    </div>
    <div v-else>
        <!-- 1231231321312312123123 -->
    </div>
</template>
<script>
    import importAndExport from "@/components/import-and-export";
    import { paymentTallyExport } from "@/api/io";
    import Bread from "@/components/bread";
    import { dealListUrl, dealListExportUrl } from "@/api/url";
    import { paymentList } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: paymentTallyExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: dealListUrl,
                    getDataListIsPage: true,
                    exportURL: "dealListExportUrl",
                    // deleteURL: deleteAttributeUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: "id"
                },
                tableData: [],
                textarea: "",
                isList: true,
                breaddata: ["财务管理", "支付管理", "交易记录"],
                dataListLoading: false,
                dataForm: {
                    tradeSn: "",
                    paymentSn: "",
                    paymentCode: "",
                    buyerName: "",
                    transactionStatus: "",
                    startDate: "",
                    endDate: "",
                },
                paymentList: "", //交易渠道
                timeArr: "", //活动时间
                addressInfo: [], //地址数据
                packageInfo: [], //包裹数据
                orderLog: [], //操作日志
                goodKind2loading: false,
                goodKind3loading: false,
                statusRules(row, column) {
                    return row.transactionStatus == 1 ? "收" : "支";
                },
                tableheight: document.body.offsetHeight - 358,
                tableheightBig: 300,
            };
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            //条件查询
            getData() {
                this.dataForm.startDate = this.timeArr && this.timeArr[0];
                this.dataForm.endDate = this.timeArr && this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //交易渠道
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
            //重置
            reset(formName) {
                this.timeArr = [];
                this.dataForm.startDate = "";
                this.dataForm.endDate = "";
                this.$refs[formName].resetFields();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //查看订单
            goOrderDetail(orderSn) {
                var row = {
                    paymentOrderSn: orderSn,
                };
                this.$emit("chagePageNum", 3, row);
            },
            //查看订单列表
            goOrderList(row) {
                this.$emit("chagePageNum", 2, row);
            },
        },
        components: {
            Bread,
            importAndExport,
        },
        created() {
            this.getPaymentList();
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

    // .el-table {
    //     margin-top: 20px;
    // }

    .normalHeight {
        color: red;
    }

    .green {
        color: #04a864;
    }

    .yellow {
        color: #d69d00;
    }
</style>