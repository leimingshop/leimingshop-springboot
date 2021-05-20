<template>
    <!--优惠券列表-->
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                class="grayLine topGapPadding"
                :inline="true"
                ref="dataForm"
                :model="dataForm"
            >
                <el-form-item label="活动名称：">
                    <el-input
                        v-model="dataForm.activityName"
                        placeholder="活动名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="状态：" prop="activityState">
                    <el-select
                        v-model="dataForm.activityState"
                        placeholder="活动状态"
                    >
                        <el-option label="全部状态" value=""></el-option>
                        <el-option label="未开始" value="0">未开始</el-option>
                        <el-option label="进行中" value="1">活动中</el-option>
                        <el-option label="已结束" value="2">活动结束</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="活动时间：">
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
            <div class="formControlArea">
                <div>
                    <el-form>
                        <el-form-item>
                            <el-button
                                class="btn"
                                type="primary"
                                @click="changeCompent(1, '')"
                                >新增</el-button
                            >
                        </el-form-item>
                    </el-form>
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
                        1、活动未开始可进行编辑拼团、管理商品、删除操作；活动进行中可进行查看拼团\商品、停止操作；活动结束可进行查看拼团\商品、删除操作
                    </div>
                    <div class="iconSize">
                        2、新增的拼团活动可设置参团条件，默认为无限制
                    </div>
                    <div class="iconSize">3、拼团和设置推进拼团和虚拟成团</div>
                </template>
            </el-alert>
        </div>
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
                prop="groupName"
                label="活动名称"
                align="center"
                min-width="200"
            ></el-table-column>
            <el-table-column
                prop="goodsNum"
                label="商品个数"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="groupNum"
                label="成团数"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="paymentNum"
                label="支付订单数"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="getEndDate"
                label="活动时间"
                align="center"
                width="320"
            >
                <template slot-scope="scope">
                    <div>{{ scope.row.startTime }}-{{ scope.row.endTime }}</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="activityState"
                label="状态"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.activityStatus == 0">未开始</div>
                    <div v-if="scope.row.activityStatus == 1">活动中</div>
                    <div v-if="scope.row.activityStatus == 2">活动结束</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(3, scope.row)"
                        v-if="
                            scope.row.activityStatus == 1 ||
                            scope.row.activityStatus == 2
                        "
                        >查看拼团
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(7, scope.row)"
                        v-if="
                            scope.row.activityStatus == 1 ||
                            scope.row.activityStatus == 2
                        "
                        >查看商品
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="scope.row.activityStatus == 0"
                        @click="changeCompent(2, scope.row)"
                        >编辑拼团
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="scope.row.activityStatus == 0"
                        @click="changeCompent(6, scope.row)"
                        >管理商品
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="scope.row.activityStatus == 1"
                        @click="stopGroup(scope.row)"
                        >停止
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityStatus == 0 ||
                            scope.row.activityStatus == 2
                        "
                        @click="deleteHandle(scope.row.id)"
                        >删除
                    </el-button>
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
    import { getGroupPage } from "@/api/url";
    import { stopGroupActivity } from "@/api/api";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["营销系统", "拼团管理", "拼团活动列表"],
                mixinViewModuleOptions: {
                    getDataListURL: getGroupPage,
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/group",
                    deleteIsBatch: true,
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
                this.dataForm.startTime = this.dateArr[0];
                this.dataForm.endTime = this.dateArr[1];
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
            stopGroup(row) {
                this.$confirm("您确认停止该活动吗？", "提示", {
                    cancelButtonText: "取消",
                    confirmButtonText: "确定",
                    type: "warning",
                })
                    .then(() => {
                        let obj = {
                            id: row.id,
                        };
                        stopGroupActivity(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.getData();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg,
                                });
                                // this.getData()
                            }
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消",
                        });
                    });
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
            height: 2px;
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
    .formControlArea {
        height: 70px;
    }
</style>
