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
                <el-form-item label="申请时间：">
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
                    <el-button class="btn" type="primary" @click="searchData()">
                        查询
                    </el-button>
                    <el-button
                        class="btn"
                        @click="reset()"
                        type="primary"
                        plain
                    >
                        重置
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="dataForm.issueStatus"
                    @change="handleClick"
                    style="margin-top: 5px; margin-bottom: 5px"
                >
                    <el-radio-button label=""> 全部 </el-radio-button>
                    <el-radio-button label="4"> 审核中 </el-radio-button>
                    <el-radio-button label="5"> 已驳回 </el-radio-button>
                    <el-radio-button label="6"> 已发放 </el-radio-button>
                </el-radio-group>
            </div>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            @selection-change="handleSelectionChange"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" label="全选"
                             width="70"
                             :selectable="selectable"
            >
            </el-table-column>
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
            <el-table-column label="收款银行" prop="bankName" align="center">
            </el-table-column>
            <el-table-column label="提现进度" prop="auditStatus" align="center">
                <template slot-scope="scope">
                    <el-tag type="warning" v-if="scope.row.auditStatus == 4"
                        >发放申请中</el-tag
                    >
                    <el-tag type="danger" v-if="scope.row.auditStatus == 5"
                        >发放驳回</el-tag
                    >
                    <el-tag type="success" v-if="scope.row.auditStatus == 6"
                        >发放通过</el-tag
                    >
                </template>
            </el-table-column>
            <!-- <el-table-column label="交易流水"
                             prop="storeLinkman"
                             align="center">
            </el-table-column> -->
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="mini"
                        v-show="scope.row.auditStatus == 4"
                        @click="showExamineFn(scope.row)"
                    >
                        发放
                    </el-button>
                    <el-button
                        type="text"
                        size="mini"
                        v-show="scope.row.auditStatus == 4"
                        @click="reject(scope.row)"
                    >
                        驳回
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-row style="display: inline-flex">
            <el-button
                style="margin-top: 10px; height: 40px; background-color: white"
                @click="batchDistribution()"
                v-show="dataForm.issueStatus==4 || dataForm.issueStatus==''"
            >
                批量发放
            </el-button>
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
        <modelAudit
            v-if="showFlag"
            ref="modelAudit"
            :textStr="'批量发放'"
            @determineSubmit="determineSubmit"
        >
        </modelAudit>
    </div>
</template>
<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import modelAudit from "../modules/model-audit.vue";
    import { withdrawalapplicationIssue } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "admin-api/withdrawalapplication/issue/page",
                    activatedIsNeed: true,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteGoodsUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["财务管理", "提现管理", "提现发放审核"],
                dataForm: {
                    bankPeople: "", //收款人
                    memberName: "", //会员名称或ID
                    orderField: "", //
                    order: "", //
                    startDate: "", //开始时间
                    endDate: "", // 结束时间
                    issueStatus: "", // 提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）
                },
                valueTime: [],
                multipleSelection: [],
                operationFlowVisible: false, // 审核流水弹框是否展示
                tableheight: document.body.offsetHeight - 410,
                tableheightBig: 300,
                showFlag: false,
            };
        },
        components: {
            Bread,
            modelAudit,
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
                (this.valueTime = []), this.handleValueTime();
                this.getDataList();
            },

            selectable(row, index) {
                console.log(row);
                if (row.auditStatus == 4) {
                    return true;
                } else {
                    return false;
                }
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
                this.$msgbox({
                    title: "提现审核",
                    message:
                        "&nbsp&nbsp&nbsp&nbsp申请时间：" +
                        row.createDate +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp申请金额：￥" +
                        row.withdrawalAmount +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp收款账号：" +
                        row.bankAccount +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp收款人：" +
                        row.bankPeople +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp手机号码：" +
                        row.bankPhone,
                    showCancelButton: true,
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    beforeClose: (action, instance, done) => {
                        if (action === "confirm") {
                            var idsArray = [];
                            idsArray.push(row.id);
                            var params = {
                                ids: idsArray,
                                auditStatus: 6,
                                auditReason: "",
                            };
                            withdrawalapplicationIssue(params).then((res) => {
                                done();
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.getDataList();
                            });
                        } else {
                            done();
                        }
                    },
                }).then((action) => {
                    this.$message({
                        type: "info",
                        message: "action: " + action,
                    });
                });
            },

            // 查询
            reject(row) {
                // 拒绝理由：
                this.$prompt("", "申请驳回", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    inputPlaceholder: "请输入拒绝理由",
                    inputValidator: function (value) {
                        if (!value || value == "" || value.length == 0) {
                            return "拒绝理由不能为空";
                        }
                    },
                })
                    .then(({ value }) => {
                        if (!value || value == "" || value.length == 0) {
                            this.$message({
                                type: "warning",
                                message: "请输入拒绝理由",
                            });
                            return;
                        }
                        var idsArray = [];
                        idsArray.push(row.id);
                        var params = {
                            ids: idsArray,
                            auditStatus: 5,
                            auditReason: value,
                        };
                        withdrawalapplicationIssue(params).then((res) => {
                            this.$message({ type: "success", message: res.msg });
                            this.getDataList();
                        });
                    })
                    .catch(() => {});
            },

            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                this.dataForm.issueStatus = tab;
                this.searchData();
            },

            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.dataListSelectionChangeHandle(val);
            },

            getIds() {
                var ids = [];
                this.multipleSelection.forEach((item, index) => {
                    if ("object" == typeof item) {
                        ids.push(item.id);
                    } else {
                        ids.push(id);
                    }
                });
                return ids;
            },

            determineSubmit(auditType, auditTextStr) {
                if (parseInt(auditType) == 4) {
                    auditType = 6;
                } else {
                    auditType = 5;
                }
                var params = {
                    ids: this.getIds(),
                    auditStatus: auditType,
                    auditReason: auditTextStr,
                };
                withdrawalapplicationIssue(params).then((res) => {
                    console.log("resresresresres", res);
                    this.$message({ type: "success", message: res.msg });
                    this.getDataList();
                    this.showFlag = false;
                });
            },

            batchDistribution() {
                if ( this.getIds().length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择需要审核的申请",
                    });
                    return;
                };
                this.showFlag = true;
                this.$nextTick(() => {
                    this.$refs.modelAudit.init();
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
