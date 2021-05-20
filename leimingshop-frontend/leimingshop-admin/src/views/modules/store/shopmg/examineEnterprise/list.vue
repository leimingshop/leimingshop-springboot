<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                :model="dataForm"
                class="grayLine topGapPadding"
            >
                <el-form-item label="店铺名称：">
                    <el-input
                        v-model="dataForm.storeName"
                        placeholder="请输入"
                    ></el-input>
                </el-form-item>

                <el-form-item label="商户账号：">
                    <el-input
                        v-model="dataForm.userName"
                        placeholder="请输入"
                    ></el-input>
                </el-form-item>

                <el-form-item label="申请日期：">
                    <el-date-picker
                        v-model="valueTime"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item>
                    <el-button class="btn" type="primary" @click="searchData()"
                        >查询</el-button
                    >
                    <el-button class="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="dataForm.audit"
                    @change="handleClick"
                    style="margin-top: 5px; margin-bottom: 5px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="10">待审核</el-radio-button>
                    <el-radio-button label="20">审核通过</el-radio-button>
                    <el-radio-button label="30">审核拒绝</el-radio-button>
                </el-radio-group>
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
                        1、商户入驻之后，可以进行企业资质信息的修改，修改完后需要运营端审核，审核通过才能生效
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
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

            <el-table-column label="店铺名称" prop="storeName" align="center">
            </el-table-column>

            <el-table-column label="商户账号" prop="account" align="center">
            </el-table-column>

            <el-table-column label="公司名称" prop="companyName" align="center">
            </el-table-column>

            <el-table-column label="申请时间" prop="createDate" align="center">
            </el-table-column>

            <el-table-column
                label="办公电话"
                prop="companyPhone"
                align="center"
            >
            </el-table-column>

            <el-table-column
                label="审核状态"
                prop="registerAuditStatus"
                align="center"
            >
                <template slot-scope="scope">
                    <el-tag
                        type="warning"
                        v-if="scope.row.authAuditStatus == 10"
                        >待审核</el-tag
                    >
                    <el-tag
                        type="success"
                        v-if="scope.row.authAuditStatus == 20"
                        >审核通过</el-tag
                    >
                    <el-tag
                        type="warning"
                        v-if="scope.row.authAuditStatus == 30"
                        >审核拒绝</el-tag
                    >
                    <el-tag
                        type="warning"
                        v-if="scope.row.authAuditStatus == 40"
                        >待提交</el-tag
                    >
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="mini"
                        @click="showExamineFn(scope.row)"
                        v-if="scope.row.authAuditStatus == 10"
                        >审核</el-button
                    >
                    <el-button
                        type="text"
                        size="mini"
                        @click="showExamineFn2(scope.row)"
                        v-else
                        v-if="$hasPermission('sys:store:company:view')"
                        >查看</el-button
                    >
                    <el-button
                        type="text"
                        size="mini"
                        @click="showOperationFlowFn(scope.row)"
                        v-if="$hasPermission('sys:store:company:tally')"
                        >操作流水</el-button
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

        <!-- 审核流水弹框 -->
        <modelOperationFlow
            v-if="operationFlowVisible"
            ref="operationFlowCompon"
        ></modelOperationFlow>
    </div>
</template>
<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import modelOperationFlow from "./model-operation-flow";
    import { auditCompanyPgaeUrl } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: auditCompanyPgaeUrl,
                    activatedIsNeed: true,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteGoodsUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["店铺管理", "店铺列表"],
                dataForm: {
                    storeName: "", //店铺名称
                    userName: "", //店铺账号
                    storeId: "", //店铺ID
                    audit: "", //审核状态（10 待审核 20 审核通过 30 审核拒绝 ）
                    startTime: "", //开始时间
                    endTime: "", // 结束时间
                },
                auditOptions: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: "10",
                        name: "待审核",
                    },
                    {
                        id: "20",
                        name: "审核通过",
                    },
                    {
                        id: "30",
                        name: "审核拒绝",
                    },
                ],
                valueTime: [],
                operationFlowVisible: false, // 审核流水弹框是否展示
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
            modelOperationFlow,
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                this.dataForm.audit = tab;
                this.searchData();
            },
            // 点击按钮查询
            searchData() {
                this.page = 1;
                this.limit = 10;
                this.handleValueTime();
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.storeName = "";
                this.dataForm.userName = "";
                this.dataForm.storeId = "";
                this.dataForm.audit = "";

                (this.valueTime = []), this.handleValueTime();
                this.getDataList();
            },
            // 处理选择的时间
            handleValueTime() {
                if (this.valueTime && this.valueTime.lenth != 0) {
                    this.dataForm.startTime = this.valueTime[0];
                    this.dataForm.endTime = this.valueTime[1];
                } else {
                    this.dataForm.startTime = "";
                    this.dataForm.endTime = "";
                }
            },
            // 点击审核按钮
            showExamineFn(row) {
                row.operationType = "examine";
                this.$emit("showExamineFn", row);
            },
            // 查询
            showExamineFn2(row) {
                row.operationType = "look";
                this.$emit("showExamineFn", row);
            },
            // 展示审核流水弹框
            showOperationFlowFn(row) {
                this.operationFlowVisible = true;
                this.$nextTick(() => {
                    this.$refs.operationFlowCompon.init(row);
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    /deep/ .el-table td {
        padding: 7px 0;
    }
</style>