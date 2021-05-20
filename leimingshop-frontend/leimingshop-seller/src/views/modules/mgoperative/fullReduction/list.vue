<template>
    <!--页面还未对接，接口还没出来-->
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                class="grayLine topGapPadding"
                :inline="true"
                ref="dataForm"
                :model="dataForm"
            >
                <el-form-item label="满减活动名称：">
                    <el-input
                        v-model="dataForm.activityName"
                        placeholder="请输入活动名称"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="活动类型：" prop="ruleType">
                    <el-select
                        v-model="dataForm.ruleType"
                        placeholder="活动类型"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="普通满减" value="0"
                            >普通满减</el-option
                        >
                        <el-option label="每满减" value="1">每满减</el-option>
                        <el-option label="阶梯满减" value="2"
                            >阶梯满减</el-option
                        >
                    </el-select>
                </el-form-item>
                <el-form-item label="活动状态：" prop="activityState">
                    <el-select
                        v-model="dataForm.activityState"
                        placeholder="活动状态"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未开始" value="0">未开始</el-option>
                        <el-option label="进行中" value="1">进行中</el-option>
                        <el-option label="已结束" value="2">已结束</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="生效时间：" prop="valuetime">
                    <el-date-picker
                        v-model="valuetime"
                        type="datetimerange"
                        align="right"
                        unlink-panels
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        @change="acttime"
                    >
                    </el-date-picker>
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
                                @click="changeCompent(1)"
                                v-if="$hasPermission('sys:reduce:save')"
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
                        1、活动未开始可进行查看、编辑、删除操作；活动进行中可进行查看、停止、统计操作；活动结束可进行查看、删除、统计操作
                    </div>
                    <div class="iconSize">
                        2、新增的活动可用范围默认为全场通用，可选中指定商品、品牌
                    </div>
                    <div class="iconSize">
                        3、满减活动类型分为普通满减、每满减、阶梯满减
                    </div>
                    <div class="iconSize">
                        4、进行中和已结束的活动可通过操作-统计进行活动营销效果的
                    </div>
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
            <!-- <el-table-column prop="id" label="满减活动ID" align="center" width="180"></el-table-column> -->
            <el-table-column
                prop="activityName"
                label="满减活动名称"
                align="center"
                min-width="200"
            >
                <!--                <template slot-scope="scope">-->
                <!--                    <el-tooltip class="item" effect="dark" :content="scope.row.activityName" placement="top-start">-->
                <!--                        <span>{{scope.row.activityName}}</span>-->
                <!--                    </el-tooltip>-->
                <!--                </template>-->
            </el-table-column>
            <el-table-column
                prop="ruleType"
                label="满减活动类型"
                align="center"
                min-width="120"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.ruleType == 0">普通满减</div>
                    <div v-if="scope.row.ruleType == 1">每满减</div>
                    <div v-if="scope.row.ruleType == 2">阶梯满减</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="content"
                label="活动内容"
                align="center"
                width="170"
            >
                <template slot-scope="scope">
                    <div style="position: relative">
                        <span
                            class="myCenterTips"
                            style="
                                z-index: -1;
                                position: absolute;
                                color: transparent;
                                width: 100%;
                            "
                            >{{ scope.row.content }}</span
                        >
                        <div v-if="startControlTips">
                            <el-tooltip
                                class="item"
                                effect="dark"
                                :content="scope.row.content"
                                placement="top-start"
                                v-show="showTips(scope.$index)"
                            >
                                <span>{{ scope.row.content }}</span>
                            </el-tooltip>
                            <span v-show="!showTips(scope.$index)">{{
                                scope.row.content
                            }}</span>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="orderNum"
                label="下单数量"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="getEndDate"
                label="活动时间"
                align="center"
                min-width="300"
            >
                <template slot-scope="scope">
                    <div>{{ scope.row.startDate }}-{{ scope.row.endDate }}</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="activityState"
                label="活动状态"
                align="center"
                min-width="80"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.activityState == 0">未开始</div>
                    <div v-if="scope.row.activityState == 1">进行中</div>
                    <div v-if="scope.row.activityState == 2">已结束</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(3, scope.row)"
                        v-if="$hasPermission('sys:reduce:view')"
                        >查看</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 0 &&
                            $hasPermission('sys:reduce:edit')
                        "
                        @click="changeCompent(2, scope.row)"
                        >编辑</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState != 1 &&
                            $hasPermission('sys:reduce:delete')
                        "
                        @click="deleteHandle(scope.row.id)"
                        >删除</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 1 &&
                            $hasPermission('sys:reduce:stop')
                        "
                        @click="stopCoupon(scope.row)"
                        >停止</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="scope.row.activityState != 0"
                        @click="statis(scope.row)"
                        >统计</el-button
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
    import { getFullReductionPage } from "@/api/url";
    import { stopFullReduction } from "@/api/api";
    export default {
        name: "list",
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["营销系统", "满减活动", "满减活动列表"],
                mixinViewModuleOptions: {
                    activatedIsNeed: false,
                    getDataListURL: getFullReductionPage,
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/reduce/activity",
                },
                // 生效时间
                valuetime: [],
                // 查询表单
                dataForm: {
                    activityName: "",
                    ruleType: "",
                    activityState: "",
                    startDate: "", //活动开始时间 ,
                    endDate: "", //活动结束时间 ,
                },
                // 优惠券列表表格
                dataList: [],
                dataListLoading: false,
                startControlTips: false,
                tableheight: document.body.offsetHeight - 440,
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
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        activated() {
            this.startControlTips = false;
            this.getDataList().then(() => {
                setTimeout(() => {
                    this.startControlTips = true;
                }, 0);
            });
        },
        created() {},
        methods: {
            showTips(index) {
                // setTimeout(()=>{
                if (
                    document.getElementsByClassName("myCenterTips") &&
                    document.getElementsByClassName("myCenterTips")[index]
                ) {
                    if (
                        document.getElementsByClassName("myCenterTips")[index]
                            .offsetHeight > 50
                    ) {
                        console.log([
                            index,
                            document.getElementsByClassName("myCenterTips")[index]
                                .offsetHeight,
                        ]);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
                // },0)
            },
            //统计路由跳转
            statis(row) {
                var queryObj1 = [
                    { key: "activityId", value: row.id },
                    { key: "activityType", value: "2" },
                    { key: "activityName", value: row.activityName },
                ];
                this.goRouter("statistic-market-index", queryObj1);
            },
            // 路由跳转
            goRouter(argu, queryObj) {
                if (!queryObj) {
                    this.$router.push({
                        path: argu,
                    });
                } else {
                    var obj = {};
                    queryObj.forEach((item, index) => {
                        obj[item.key] = item.value;
                    });
                    this.$router.push({
                        path: argu,
                        query: obj,
                    });
                }
            },
            //领券时间
            acttime() {
                if (this.valuetime && this.valuetime.length != 0) {
                    this.dataForm.startDate = this.valuetime[0];
                    this.dataForm.endDate = this.valuetime[1];
                } else {
                    this.dataForm.startDate = "";
                    this.dataForm.endDate = "";
                }
            },
            // 查询表格数据
            getData() {
                this.page = 1;
                this.startControlTips = false;
                this.getDataList().then(() => {
                    setTimeout(() => {
                        this.startControlTips = true;
                    }, 0);
                });
            },
            // 重置
            reset() {
                this.dataForm.activityName = "";
                this.dataForm.ruleType = "";
                this.dataForm.activityState = "";
                this.dataForm.startDate = "";
                this.dataForm.endDate = "";
                this.valuetime = "";
                this.startControlTips = false;
                this.getDataList().then(() => {
                    setTimeout(() => {
                        this.startControlTips = true;
                    }, 0);
                });
            },
            // 点击新增、编辑、查看
            changeCompent(type, row) {
                this.$emit("changeCompent", type, row);
            },
            stopCoupon(row) {
                this.$confirm("您确认停止该满减活动吗?", "提示", {
                    cancelButtonText: "取消",
                    confirmButtonText: "确定",
                    type: "warning",
                })
                    .then(() => {
                        var obj = {
                            id: row.id,
                        };
                        stopFullReduction(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1500,
                                });
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "error",
                                    duration: 1500,
                                });
                            }
                            this.getData(); // 刷新列表数据
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消操作",
                        });
                    });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .fullReduction {
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
