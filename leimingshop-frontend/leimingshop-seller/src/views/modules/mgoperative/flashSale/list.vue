<template>
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
                        placeholder="请输入活动名称"
                        clearable
                    ></el-input>
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
                <el-form-item label="活动时间：" prop="valuetime">
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
                                >新增
                            </el-button>
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
                        1、未开始状态——点击商品跳转到添加商品列表，可添加或删除商品，可修改商品限时购库存\进行中状态——点击商品跳转到添加商品列表，可删除商品，不可修改商品限时购库存
                    </div>
                    <div class="iconSize">
                        2、未开始状态——添加活动商品时弹窗活动库存默认为空，为空项的sku不参加限时购活动，添加的商品sku限时购库存至少有一个不为空
                    </div>
                    <div class="iconSize">
                        3、锁定商品库存规则——添加限时购活动时添加的商品设置好限时购库存后即锁定库存
                    </div>
                    <div class="iconSize">
                        4、用户在限时购活动时间范围内，选择限时购库存下单后取消订单，该订单限时购库存退还到普通库存。
                    </div>
                    <div class="iconSize">
                        5、限时购活动商品在限时购时间范围内商品不可进行编辑，编辑提示该商品为限时购活动商品，请在限时购活动移除该商品
                    </div>
                </template>
            </el-alert>
        </div>
        <!--秒杀列表-->
        <el-table
            width="100%"
            :data="dataList"
            :border="true"
            v-loading="dataListLoading"
            style="width: 100%; maigin-top: 20px"
        >
            <!-- :height="!$store.state.mainSwitch ? tableheight : tableheightBig" -->

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
                prop="activityName"
                label="限时抢购活动名称"
                align="center"
                min-width="200"
            ></el-table-column>
            <el-table-column
                prop="goodsNum"
                label="活动商品（件）"
                align="center"
                min-width="120"
            ></el-table-column>
            <el-table-column
                prop="browseNum"
                label="限时抢购商品浏览量"
                align="center"
                min-width="120"
            ></el-table-column>
            <el-table-column
                prop="orderNum"
                label="下单数量"
                align="center"
                min-width="100"
            ></el-table-column>
            <el-table-column
                prop="getEndDate"
                label="活动时间"
                align="center"
                min-width="300"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.activityStartDate }}-{{
                            scope.row.activityEndDate
                        }}
                    </div>
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
                        v-if="
                            scope.row.activityState != 0 &&
                            $hasPermission('sys:seckill:view')
                        "
                        >查看
                    </el-button>
                    <!--<el-button size="mini" type="text"-->
                    <!--v-if="scope.row.activityState == 2 && $hasPermission('sys:seckill:edit')"-->
                    <!--@click="changeCompent(2,scope.row)">复制-->
                    <!--</el-button>-->
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 1 ||
                            (scope.row.activityState == 2 &&
                                $hasPermission('sys:seckill:view'))
                        "
                        @click="changeCompent(6, scope.row)"
                        >查看商品
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 0 &&
                            $hasPermission('sys:seckill:view')
                        "
                        @click="changeCompent(4, scope.row)"
                        >管理商品
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 0 &&
                            $hasPermission('sys:seckill:edit')
                        "
                        @click="changeCompent(2, scope.row)"
                        >编辑
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState != 1 &&
                            $hasPermission('sys:seckill:edit')
                        "
                        @click="deleteHandle(scope.row.id)"
                        >删除
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 1 &&
                            $hasPermission('sys:seckill:edit')
                        "
                        @click="stopFlashSaleActivity(scope.row)"
                        >停止
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
    import { getFlashSalePage } from "@/api/url";
    import { stopFlashSaleActivity } from "@/api/api";
    // import {deleteSeckillActivity} from "@/api/api";

    export default {
        name: "list",
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["营销系统", "限时抢购", "限时抢购列表"],
                mixinViewModuleOptions: {
                    activatedIsNeed: false,
                    getDataListURL: getFlashSalePage,
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/flash/sale",
                    deleteIsBatch: true,
                },
                // 活动时间
                valuetime: [],
                // 查询表单
                dataForm: {
                    activityName: "", //活动名称
                    activityState: "", //活动状态
                },
                // 秒杀列表
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
        activated() {
            this.startControlTips = false;
            this.getDataList().then(() => {
                setTimeout(() => {
                    this.startControlTips = true;
                }, 0);
            });
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
        created() {},
        methods: {
            // 活动时间查询
            acttime() {
                if (this.valuetime && this.valuetime.length != 0) {
                    this.dataForm.activityStartDate = this.valuetime[0];
                    this.dataForm.activityEndDate = this.valuetime[1];
                } else {
                    this.dataForm.activityStartDate = "";
                    this.dataForm.activityEndDate = "";
                }
            },
            // 查询秒杀列表
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
                this.dataForm.activityState = "";
                this.dataForm.activityStartDate = "";
                this.dataForm.activityEndDate = "";
                this.valuetime = [];
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
            // 停止秒杀活动
            stopFlashSaleActivity(row) {
                this.$confirm("您确认停止该活动吗?", "提示", {
                    cancelButtonText: "取消",
                    confirmButtonText: "确定",
                }).then(() => {
                    var obj = {
                        id: row.id,
                    };
                    stopFlashSaleActivity(obj).then((res) => {
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
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .seckill {
        /deep/ .el-input__icon {
            height: unset !important;
        }
        .line {
            height: 10px;
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
