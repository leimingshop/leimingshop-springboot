<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                :model="dataForm"
                class="grayLine topGapPadding"
            >
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.memberName"
                        placeholder="会员ID\会员名称"
                        clearable
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="收款人：">
                    <el-input
                        v-model="dataForm.bankPeople"
                        placeholder="收款人姓名"
                        clearable
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="账号：">
                    <el-input
                        v-model="dataForm.bankAccount"
                        placeholder="收款账号\开户行账号"
                        clearable
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="日期选择：">
                    <el-date-picker
                        v-model="valueTime"
                        type="daterange"
                        range-separator="至"
                        value-format="yyyy-MM-dd"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        @blur="acttime"
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
                    v-model="dataForm.issueStatus"
                    @change="handleClick"
                    style="margin-top: 5px; margin-bottom: 5px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="6">提现成功</el-radio-button>
                    <el-radio-button label="3">驳回记录</el-radio-button>
                </el-radio-group>
            </div>
            <!-- <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                <template slot='title'>
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">1、商户入驻审核用于审核商户端申请入驻的商家。包含商家的账号信息、店铺信息、经营资质等信息。审核通过之后，商家可以拥有一个商城店铺，并可以进行商品发布和店铺管理等操作</div>
                    <div class="iconSize">2、商户入驻如果没有审核通过，商户可以修改后，重新提交。重新提交的在原有记录上更新状态，不生成新的入驻记录。提交和审核记录在操作流水中同步记录</div>
                </template>
            </el-alert> -->
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column label="会员ID" prop="memberId" align="center">
            </el-table-column>
            <el-table-column label="会员名称" prop="buyerName" align="center">
            </el-table-column>
            <el-table-column
                label="申请时间"
                prop="createDate"
                align="center"
                width="160"
            >
            </el-table-column>
            <el-table-column
                label="申请金额"
                prop="withdrawalAmount"
                align="center"
            >
            </el-table-column>
            <el-table-column
                label="收款账号\开户行账号"
                prop="bankAccount"
                align="center"
                width="160"
            >
            </el-table-column>
            <el-table-column label="收款人" prop="bankPeople" align="center">
            </el-table-column>
            <el-table-column label="开户银行" prop="bankName" align="center">
            </el-table-column>
            <el-table-column label="提现结果" prop="auditStatus" align="center">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.auditStatus == 6"
                        >提现成功</el-tag
                    >
                    <el-tag type="danger" v-if="scope.row.auditStatus == 3"
                        >已驳回</el-tag
                    >
                </template>
            </el-table-column>
            <el-table-column
                label="支付时间"
                prop="payTime"
                align="center"
                width="160"
            >
            </el-table-column>
            <el-table-column label="驳回原因" prop="auditReason" align="center">
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-row style="display: inline-flex">
            <el-pagination
                style="display: flex; position: fixed; flex: 1; right: 30px"
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
            >
            </el-pagination>
        </el-row>
    </div>
</template>
<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "admin-api/withdrawalapplication/record/page",
                    activatedIsNeed: true,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteGoodsUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["财务管理", "提现管理", "提现记录"],
                dataForm: {
                    bankPeople: "", //收款人
                    memberName: "", //会员名称或ID
                    orderField: "", //
                    order: "", //
                    startDate: "", //开始时间
                    endDate: "", // 结束时间
                    bankAccount: "",
                    issueStatus: "", // 提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）
                },
                valueTime: [],
                operationFlowVisible: false, // 审核流水弹框是否展示
                tableheight: document.body.offsetHeight - 410,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
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
            // 点击按钮查询
            searchData() {
                this.page = 1;
                this.limit = 10;
                this.handleValueTime();
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.bankPeople = ""; //店铺名称                    this.dataForm.memberName = ''; //店铺账号
                this.dataForm.orderField = ""; //店铺类型（1:自营商户，2:普通商户）
                this.dataForm.order = ""; //审核状态（10 待审核 20 审核通过 30 审核拒绝 ）
                this.dataForm.issueStatus = ""; // 结束时间
                this.dataForm.memberName = ""; //店铺账号
                this.dataForm.bankAccount="";
                (this.valueTime = []), this.handleValueTime();
                this.getDataList();
            },
            // 处理选择的时间
            handleValueTime() {
                if (this.valueTime && this.valueTime.lenth != 0) {
                    this.dataForm.startDate = this.valueTime[0];
                    this.dataForm.endDate = this.valueTime[1];
                } else {
                    this.dataForm.startDate = "";
                    this.dataForm.endDate = "";
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

            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                this.dataForm.issueStatus = tab;
                this.searchData();
            },
            // 展示审核流水弹框
            showOperationFlowFn(row) {
                this.operationFlowVisible = true;
                this.$nextTick(() => {
                    this.$refs.operationFlowCompon.init(row);
                });
            },
            //开始结束时间
            acttime() {
                this.dataForm.startDate = this.valueTime[0];
                this.dataForm.endDate = this.valueTime[1];
            },
        },
    };
</script>
<style>
</style>
