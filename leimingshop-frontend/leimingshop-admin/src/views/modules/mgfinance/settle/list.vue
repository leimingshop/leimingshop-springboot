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
                <el-form-item label="账单编号：">
                    <el-input
                        v-model="dataForm.billTotalSn"
                        placeholder="账单编号"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="店铺ID：">
                    <el-input
                        v-model="dataForm.storeId"
                        placeholder="店铺ID"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="店铺名称：">
                    <el-input
                        v-model="dataForm.storeName"
                        placeholder="店铺名称"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="店铺类型：">
                    <el-select
                        v-model="dataForm.storeType"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="自营平台" value="1"></el-option>
                        <el-option label="普通商户" value="2"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="商家确认状态：">
                    <el-select
                        v-model="dataForm.confirmStatus"
                        @change="changeSelect(dataForm.confirmStatus)"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未确认" value="0"></el-option>
                        <el-option label="已确认" value="1"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="结算状态：">
                    <el-select
                        v-model="dataForm.billState"
                        placeholder="请选择"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未结算" value="0"></el-option>
                        <el-option label="已结算" value="1"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="出账日期：">
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
                    <el-button calss="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button calss="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="activeName"
                    @change="handleClick(activeName)"
                    style="margin-bottom: 0px"
                    type="primary"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="1" width="170"
                        >已确认</el-radio-button
                    >
                    <el-radio-button label="0" width="170"
                        >未确认</el-radio-button
                    >
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
                        1、对账单根据平台网站管理-高级设置-对账设置的对账周期进行对账
                    </div>
                    <div class="inconSize">
                        2、对账状态分为已确认和未确认，商户确认过的账单为已确认账单
                    </div>
                    <div class="inconSize">
                        3、已确认账单可进行详情查看和结算操作
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                prop="billTotalSn"
                label="账单编号"
                header-align="center"
                align="center"
                width="170"
            >
            </el-table-column>
            <el-table-column
                prop="storeName"
                label="店铺名称"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="storeId"
                label="店铺ID"
                header-align="center"
                align="center"
                width="170"
            >
            </el-table-column>
            <el-table-column
                prop="billTime"
                label="出账日期"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="resultTotals"
                label="本期应结"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="confirmStatus"
                label="商家确认状态"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.confirmStatus == 0">未确认</span>
                    <span v-else-if="scope.row.confirmStatus == 1">已确认</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="billState"
                label="结算状态"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.billState == 0">未结算</span>
                    <span v-else-if="scope.row.billState == 1">已结算</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="storeType"
                label="商户类别"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.storeType == 1">自营平台</span>
                    <span v-else-if="scope.row.storeType == 2">普通商户</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="obtStartTime"
                label="开始日期"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="obtEndTime"
                label="结束日期"
                header-align="center"
                align="center"
            ></el-table-column>
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
                        @click="showDetail(scope.row.id)"
                        v-if="$hasPermission('sys:bill:view')"
                        >查看</el-button
                    >
                    <el-button
                        type="text"
                        size="small"
                        @click="bill(scope.row.id)"
                        v-if="
                            $hasPermission('sys:bill:order') &&
                            scope.row.confirmStatus == 1 &&
                            scope.row.billState == 0
                        "
                    >
                        结算
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        @click="remark(scope.row)"
                        v-if="
                            $hasPermission('sys:bill:remark') &&
                            scope.row.confirmStatus == 1
                        "
                    >
                        备注
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
        >
        </el-pagination>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import { bill } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                valueTime: [],
                activeName: "",
                mixinViewModuleOptions: {
                    getDataListURL: "admin-api/orderbill/page",
                    getDataListIsPage: true,
                    deleteIsBatch: true,
                },
                dataForm: {
                    id: "",
                    startTime: "",
                    endTime: "",
                    confirmStatus: "",
                },
                breaddata: ["财务管理", "对账管理", "对账列表"],
                start: true,
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 0,
            };
        },
        created() {
            // 第一次请求数据
            this.handleClick("");
        },
        components: {
            Bread,
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    this.tableheightBig =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
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
                if (this.dataForm.confirmStatus != null) {
                    this.activeName = this.dataForm.confirmStatus;
                } else {
                    this.activeName = "";
                }
            },
            showDetail(id) {
                this.$emit("showDetail", id);
            },
            changeSelect(status) {
                if (status == "1") {
                    this.dataForm.confirmStatus = "1";
                    this.activeName = "1";
                } else if (status == "0") {
                    this.dataForm.confirmStatus = "0";
                    this.activeName = "0";
                } else {
                    this.dataForm.confirmStatus = "";
                    this.activeName = "";
                }
            },
            bill(id) {
                this.$prompt("请输入结算备注", "确定通过结算？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",

                    inputPattern: "",
                })
                    .then(({ value }) => {
                        var params = {
                            id: id,
                            billRemark: value,
                        };
                        bill(params).then((res) => {
                            this.$message({
                                type: "success",
                                message: res.msg,
                            });
                            this.getData();
                        });
                    })
                    .catch(() => {});
            },
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                if (tab == "") {
                    this.dataForm.confirmStatus = "";
                } else if (tab == "1") {
                    this.dataForm.confirmStatus = "1";
                } else if (tab == "0") {
                    this.dataForm.confirmStatus = "0";
                }
                this.getDataList();
            },
            remark(row) {
                if (!row.storeRemark) {
                    row.storeRemark = "暂无备注";
                }
                this.$alert(row.storeRemark, row.storeName + "的备注", {
                    confirmButtonText: "确定",
                    callback: (action) => {},
                });
            },
            //开始结束时间
            acttime() {
                this.dataForm.startTime = this.valueTime[0];
                this.dataForm.endTime = this.valueTime[1];
            },
        },
    };
</script>