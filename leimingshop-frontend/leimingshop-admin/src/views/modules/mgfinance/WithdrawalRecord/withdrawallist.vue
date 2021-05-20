<template>
    <div v-if="start">
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
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
                    <el-button calss="btn" type="primary" @click="getData()">
                        查询
                    </el-button>
                    <el-button
                        calss="btn"
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
                    v-model="activeName"
                    @change="handleClick(activeName)"
                    style="margin-bottom: 0px"
                    type="primary"
                >
                    <el-radio-button
                        v-for="(item, index) in stateArray"
                        :key="index"
                        :label="item.id"
                        >{{ item.name }}
                    </el-radio-button>
                </el-radio-group>
            </div>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="handleSelectionChange"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection"
                             :selectable="selectable"
                             label="全选" width="70">
            </el-table-column>
            <el-table-column
                prop="memberId"
                label="会员ID"
                header-align="center"
                align="center"
                width="170"
            >
            </el-table-column>
            <el-table-column
                prop="buyerName"
                label="会员名称"
                header-align="center"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="createDate"
                label="申请时间"
                header-align="center"
                align="center"
                width="160"
            >
            </el-table-column>
            <el-table-column
                prop="withdrawalAmount"
                label="申请金额"
                header-align="center"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="bankAccount"
                label="收款账号\开户行账号"
                header-align="center"
                align="center"
                width="160"
            >
            </el-table-column>
            <el-table-column
                prop="bankPeople"
                label="收款人"
                header-align="bankPeople"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="bankName"
                label="收款银行"
                header-align="center"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="auditStatus"
                label="提现进度"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.auditStatus == 1">申请中</span>
                    <span v-else-if="scope.row.auditStatus == 2">申请取消</span>
                    <span v-else-if="scope.row.auditStatus == 3">申请驳回</span>
                    <span v-else-if="scope.row.auditStatus == 4">审核通过</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="id"
                label="申请编号"
                header-align="center"
                align="center"
            >
            </el-table-column>
            <el-table-column
                :label="$t('handle')"
                fixed="right"
                header-align="center"
                align="center"
                width="150"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        v-show="scope.row.auditStatus == 1"
                        @click="showDetail(scope.row)"
                    >
                        审核
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        v-show="scope.row.auditStatus == 1"
                        @click="reject(scope.row)"
                    >
                        驳回
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-row style="display: inline-flex">
            <el-button
                style="margin-top: 10px; height: 40px; background-color: white"
                v-show="dataForm.auditStatus==1 || dataForm.auditStatus==''"
                @click="batchAudit()"
            >
                批量审核
            </el-button>
            <el-pagination
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                style="display: flex; position: fixed; flex: 1; right: 30px"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
            >
            </el-pagination>
        </el-row>
        <modelAudit
            v-if="showFlag"
            ref="modelAudit"
            :textStr="'批量审核'"
            @determineSubmit="determineSubmit"
        >
        </modelAudit>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import modelAudit from "../modules/model-audit.vue";
    import { withdrawalapplicationAudit } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                valueTime: [],
                activeName: "",
                mixinViewModuleOptions: {
                    getDataListURL: "admin-api/withdrawalapplication/page",
                    getDataListIsPage: true,
                    deleteIsBatch: true,
                },
                dataForm: {
                    startDate: "",
                    endDate: "",
                    auditStatus: "1",
                    bankPeople: "",
                    order: "",
                    memberName: "",
                    orderField: "",
                },
                breaddata: ["财务管理", "提现管理", "提现申请审核"],
                stateArray: [
                    { name: "全部", id: "" },
                    { name: "审核中", id: "1" },
                    { name: "已驳回", id: "3" },
                    { name: "已取消", id: "2" },
                    { name: "审核通过", id: "4" },
                ],
                start: true,
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 300,
                multipleSelection: [],
                showFlag: false,
            };
        },
        created() {
            // 第一次请求数据
            this.handleClick("");
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
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            //重置
            reset() {
                this.page = 1;
                this.valueTime = [];
                this.dataForm = {};
                this.activeName = "";
                this.getDataList();
            },

            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
                if (this.dataForm.auditStatus != null) {
                    this.activeName = this.dataForm.auditStatus;
                } else {
                    this.activeName = "";
                }
            },

            selectable(row, index) {
                console.log(row);
                if (row.auditStatus == 1) {
                    return true;
                } else {
                    return false;
                }
            },

            showDetail(item) {
                this.$msgbox({
                    title: "提现审核",
                    message:
                        "&nbsp&nbsp&nbsp&nbsp申请时间：" +
                        item.createDate +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp申请金额：￥" +
                        item.withdrawalAmount +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp收款账号：" +
                        item.bankAccount +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp收款人：" +
                        item.bankPeople +
                        "<br/>" +
                        "&nbsp&nbsp&nbsp&nbsp手机号码：" +
                        item.bankPhone,
                    showCancelButton: true,
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    beforeClose: (action, instance, done) => {
                        if (action === "confirm") {
                            var idsArray = [];
                            idsArray.push(item.id);
                            var params = {
                                ids: idsArray,
                                auditStatus: 4,
                                auditReason: "",
                            };
                            withdrawalapplicationAudit(params).then((res) => {
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
                }).then((action) => {});
            },

            reject(item) {
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
                        idsArray.push(item.id);
                        var params = {
                            ids: idsArray,
                            auditStatus: 3,
                            auditReason: value,
                        };
                        withdrawalapplicationAudit(params).then((res) => {
                            this.$message({ type: "success", message: res.msg });
                            this.getDataList();
                        });
                    })
                    .catch(() => {});
            },

            //批量审核方法
            batchAudit() {
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
                var params = {
                    ids: this.getIds(),
                    auditStatus: auditType,
                    auditReason: auditTextStr,
                };
                withdrawalapplicationAudit(params).then((res) => {
                    console.log("resresresresres", res);
                    this.$message({ type: "success", message: res.msg });
                    this.getDataList();
                    this.showFlag = false;
                });
            },

            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                this.dataForm.auditStatus = tab;
                this.getDataList();
            },

            //开始结束时间
            acttime() {
                this.dataForm.startDate = this.valueTime[0];
                this.dataForm.endDate = this.valueTime[1];
            },
        },
    };
</script>
