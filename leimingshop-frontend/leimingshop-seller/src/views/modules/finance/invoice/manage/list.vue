<template>
    <!--优惠券列表-->
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-form
            class="grayLine topGapPadding"
            :inline="true"
            ref="dataForm"
            :model="dataForm"
        >
            <el-form-item label="订单编号：">
                <el-input
                    v-model="dataForm.orderSn"
                    placeholder="请输入"
                    maxlength="20"
                    clearable
                ></el-input>
            </el-form-item>
            <el-form-item label="会员名称：">
                <el-input
                    v-model="dataForm.memberName"
                    placeholder="请输入"
                    maxlength="20"
                    clearable
                ></el-input>
            </el-form-item>
            <el-form-item label="发票类型：" prop="memberInvoiceType">
                <!--        用户提交发票类型(1：电子、2：纸质、3：增值税)-->
                <el-select
                    v-model="dataForm.memberInvoiceType"
                    placeholder="请选择"
                >
                    <el-option label="全部状态" value=""></el-option>
                    <el-option label="电子" value="1">电子</el-option>
                    <el-option label="纸质" value="2">纸质</el-option>
                    <el-option label="增值税" value="3">增值税</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="开票状态：" prop="invoiceEvolve">
                <!--        1：待开票、2：已开票、3：待换开、4：已换开）-->
                <el-select
                    v-model="dataForm.invoiceEvolve"
                    placeholder="请选择"
                >
                    <el-option label="全部状态" value=""></el-option>
                    <el-option label="待开票" value="1">待开票</el-option>
                    <el-option label="已开票" value="2">已开票</el-option>
                    <el-option label="待换开" value="3">待换开</el-option>
                    <el-option label="已换开" value="4">已换开</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="订单状态：" prop="orderStatus">
                <!--        0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成-->
                <el-select v-model="dataForm.orderStatus" placeholder="请选择">
                    <el-option label="全部状态" value=""></el-option>
                    <el-option label="已取消" value="0">已取消</el-option>
                    <el-option label="待付款" value="10">待付款</el-option>
                    <el-option label="待发货" value="20">待发货</el-option>
                    <el-option label="待收货" value="30">待收货</el-option>
                    <el-option label="已完成" value="40">已完成</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="开票时间：">
                <el-date-picker
                    v-model="dateArr"
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
        </el-form>
        <!--新增按钮-->
        <div class="line"></div>
        <div class="formControlArea">
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
                    1、待支付的订单在发票管理列表不显示，只有支付完的才显示。
                </div>
                <div class="iconSize">
                    2、已取消的订单，即使还为开票也不能再开票。
                </div>
                <div class="iconSize">
                    3、订单为已开票状态后，用户申请换开，不在原有记录上更新开票状态，需要新增一条数据，状态为待换开。
                    即一个订单在发票管理最多有两条数据，一条开票的，一条换开的。
                </div>
                <div class="iconSize">
                    4、列表显示的订单金额为实际支付金额。
                </div>
            </template>
        </el-alert>
        <!--表格-->
        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="dataListLoading"
            style="width: 100%; maigin-top: 20px"
        >
            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column
                prop="orderSn"
                label="订单编号"
                align="center"
                min-width="200"
            ></el-table-column>
            <el-table-column
                prop="memberName"
                label="会员名称"
                align="center"
                min-width="120"
            ></el-table-column>
            <el-table-column
                prop="memberInvoiceType"
                label="申请发票类型"
                align="center"
                min-width="120"
            >
                <template slot-scope="scope">
                    <!--        1：待开票、2：已开票、3：待换开、4：已换开）-->
                    <div v-if="scope.row.memberInvoiceType == 1">电子发票</div>
                    <div v-if="scope.row.memberInvoiceType == 2">纸质发票</div>
                    <div v-if="scope.row.memberInvoiceType == 3">
                        增值税专项发票
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="payAmount"
                label="订单金额 (元)"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="applyDate"
                label="申请时间"
                align="center"
                width="180"
            >
                <template slot-scope="scope">
                    <div>{{ scope.row.applyDate }}</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="invoiceDate"
                label="开票时间"
                align="center"
                width="180"
            >
                <template slot-scope="scope">
                    <div>{{ scope.row.invoiceDate }}</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="activityState"
                label="订单状态"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <!--        0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成-->
                    <div v-if="scope.row.orderStatus == 0">已取消</div>
                    <div v-if="scope.row.orderStatus == 10">待付款</div>
                    <div v-if="scope.row.orderStatus == 20">待发货</div>
                    <div v-if="scope.row.orderStatus == 30">待收货</div>
                    <div v-if="scope.row.orderStatus == 40">已完成</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="invoiceEvolve"
                label="开票状态"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.invoiceEvolve == 1" style="color: red">
                        待开票
                    </div>
                    <div
                        v-if="scope.row.invoiceEvolve == 2"
                        style="color: #1fe81f"
                    >
                        已开票
                    </div>
                    <div v-if="scope.row.invoiceEvolve == 3" style="color: red">
                        待换开
                    </div>
                    <div
                        v-if="scope.row.invoiceEvolve == 4"
                        style="color: #1fe81f"
                    >
                        已换开
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
                <template slot-scope="scope">
                    <!-- v-if="(scope.row.invoiceEvolve == 1 || scope.row.invoiceEvolve == 3)&&(scope.row.orderStatus!=10&&scope.row.orderStatus!=0)" -->
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(1, scope.row)"
                        v-if="
                            (scope.row.invoiceEvolve == 1 ||
                                scope.row.invoiceEvolve == 3) &&
                            scope.row.orderStatus != 10 &&
                            scope.row.orderStatus != 0
                        "
                        >开票
                    </el-button>
                    <el-button
                        v-else
                        size="mini"
                        type="text"
                        @click="changeCompent(2, scope.row)"
                        >查看</el-button
                    >
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
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
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import { getorderinvoicepage } from "@/api/url";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["财务管理", "发票管理"],
                mixinViewModuleOptions: {
                    getDataListURL: getorderinvoicepage,
                    getDataListIsPage: true,
                    // deleteURL: '/seller-api/group',
                    // deleteIsBatch: true
                },
                // 查询表单
                dateArr: [],
                dataForm: {
                    activityName: "",
                    startTime: "",
                    endTime: "",
                    activityStatus: "",
                },
                // 优惠券列表表格
                dataList: [],
                dataListLoading: false,
            };
        },
        components: {
            Bread,
        },
        methods: {
            // 查询表格数据
            getData() {
                this.page = 1;
                this.dataForm.startInvoiceDate = this.dateArr[0];
                this.dataForm.endInvoiceDate = this.dateArr[1];
                this.getDataList();
            },
            // 重置
            reset() {
                this.dateArr = [];
                this.dataForm = {
                    activityName: "",
                    startTime: "",
                    endTime: "",
                    activityStatus: "",
                };
                this.getDataList();
            },
            // 点击新增、编辑、查看
            changeCompent(type, row) {
                this.$emit("changeCompent", type, row);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .couponList {
        /deep/ .el-input__icon {
            height: unset !important;
        }

        .line {
            height: 3px;
            background: #ecedf1;
            margin: 0 -20px;
        }

        .addButton {
            margin: 10px 30px;
        }

        .el-table--border {
            margin-top: 20px;
        }
    }
</style>
