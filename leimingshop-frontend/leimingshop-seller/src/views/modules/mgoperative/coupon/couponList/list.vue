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
                <el-form-item label="优惠券名称：">
                    <el-input
                        v-model="dataForm.activityName"
                        placeholder="优惠券名称"
                        clearable
                    ></el-input>
                </el-form-item>

                <!--                <el-form-item label="优惠券类型：" prop="couponsType">-->
                <!--                    <el-select v-model="dataForm.couponsType" placeholder="售后类型">-->
                <!--                        <el-option label="全部" value=""></el-option>-->
                <!--                        <el-option label="满减" value="0">满减</el-option>-->
                <!--                        <el-option label="满折" value="1">满折</el-option>-->
                <!--                    </el-select>-->
                <!--                </el-form-item>-->
                <!--                <el-form-item label="优惠券门槛：" prop="aftersaleType">-->
                <!--                    <el-select v-model="dataForm.aftersaleType" placeholder="售后类型">-->
                <!--                        <el-option label="全部" value=""></el-option>-->
                <!--                        <el-option label="有门槛" value="0">有门槛</el-option>-->
                <!--                        <el-option label="无门槛" value="1">无门槛</el-option>-->
                <!--                    </el-select>-->
                <!--                </el-form-item>-->
                <el-form-item label="活动状态：" prop="activityState">
                    <el-select
                        v-model="dataForm.activityState"
                        placeholder="售后类型"
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未开始" value="0">未开始</el-option>
                        <el-option label="进行中" value="1">进行中</el-option>
                        <el-option label="已结束" value="2">已结束</el-option>
                    </el-select>
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
                        1、手动停止进行中的优惠券后,用户将不能领取该券,但是已经领取的店铺优惠券在有效期的仍然可以使用。
                    </div>
                    <div class="iconSize">
                        2、新增优惠券的可用范围默认为全场通用，可选中指定分类（店铺）、商品、品牌
                    </div>
                    <div class="iconSize">
                        3、优惠券活动未开始状态可以进行编辑或删除操作，进行中和已结束的优惠券活动不可删除
                    </div>
                    <div class="iconSize">
                        4、进行中和已结束的优惠券活动可通过操作-统计进行活动营销效果的
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
            <el-table-column label="序号" width="60" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <!-- <el-table-column prop="id" label="优惠券ID" align="center" width="180"></el-table-column> -->
            <el-table-column
                prop="activityName"
                label="优惠券名称"
                align="center"
                min-width="180"
            >
                <!--                    <template slot-scope="scope">-->
                <!--                        <el-tooltip class="item" effect="dark" :content="scope.row.activityName" placement="top-start">-->
                <!--                            <span>{{scope.row.activityName}}</span>-->
                <!--                        </el-tooltip>-->
                <!--                    </template>-->
            </el-table-column>
            <!--                <el-table-column prop="couponsType" label="优惠券类型" align="center" min-width="80">-->
            <!--                    <template slot-scope="scope">-->
            <!--                        <div v-if="scope.row.couponsType==0">满减券</div>-->
            <!--                        <div v-if="scope.row.couponsType==1">满折券</div>-->
            <!--                    </template>-->
            <!--                </el-table-column>-->
            <el-table-column
                prop="faceValue"
                label="面值"
                align="center"
                min-width="80"
            ></el-table-column>
            <el-table-column
                prop="validityDays"
                label="有效期"
                align="center"
                min-width="170"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.validityType == 0">
                        {{ scope.row.useStartDate }}-{{ scope.row.useEndDate }}
                    </div>
                    <div v-else>领取{{ scope.row.validityDays }}天有效</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="usedNum"
                label="使用数量/领券数量/发券数量"
                align="center"
                width="200"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.usedNum }}/{{ scope.row.receivedNum }}/{{
                            scope.row.totalNum == 0
                                ? "不限量"
                                : scope.row.totalNum
                        }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="getEndDate"
                label="活动时间"
                align="center"
                width="290"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.getStartDate }}-{{ scope.row.getEndDate }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="activityState"
                label="活动状态"
                align="center"
                width="80"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.activityState == 0">未开始</div>
                    <div v-if="scope.row.activityState == 1">进行中</div>
                    <div v-if="scope.row.activityState == 2">已结束</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="190" align="center">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(3, scope.row)"
                        v-if="$hasPermission('sys:coupon:view')"
                        >查看</el-button
                    >
                    <!--明细 v-if="scope.row.activityState !==0"-->
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState !== 0 &&
                            $hasPermission('sys:coupons:deatil')
                        "
                        @click="changeCompent(4, scope.row)"
                        >明细</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 0 &&
                            $hasPermission('sys:coupons:edit')
                        "
                        @click="changeCompent(2, scope.row)"
                        >编辑</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 0 &&
                            $hasPermission('sys:coupons:delete')
                        "
                        @click="deleteHandle(scope.row.id)"
                        >删除</el-button
                    >
                    <el-button
                        size="mini"
                        type="text"
                        v-if="
                            scope.row.activityState == 1 &&
                            $hasPermission('sys:coupons:status')
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
    import { getCouponPage } from "@/api/url";
    import { stopCoupons } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["营销系统", "优惠券管理", "优惠券列表"],
                mixinViewModuleOptions: {
                    getDataListURL: getCouponPage,
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/con/coupons/activity",
                },
                // 查询表单
                dataForm: {
                    activityName: "",
                    couponsType: "",
                    aftersaleType: "",
                    activityState: "",
                },
                // 优惠券列表表格
                dataList: [],
                dataListLoading: false,
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
        methods: {
            // 查询表格数据
            getData() {
                this.page = 1;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.activityName = "";
                this.dataForm.couponsType = "";
                this.dataForm.aftersaleType = "";
                this.dataForm.activityState = "";
                this.getDataList();
            },
            // 点击新增、编辑、查看
            changeCompent(type, row) {
                this.$emit("changeCompent", type, row);
            },
            //统计路由跳转
            statis(row) {
                var queryObj1 = [
                    { key: "activityId", value: row.id },
                    { key: "activityType", value: "1" },
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
            stopCoupon(row) {
                this.$confirm("您确认停止该优惠券活动吗?", "提示", {
                    cancelButtonText: "取消",
                    confirmButtonText: "确定",
                    type: "warning",
                })
                    .then(() => {
                        var obj = {
                            id: row.id,
                        };
                        stopCoupons(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1500,
                                });
                                this.getDataList(); // 刷新列表数据
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "error",
                                    duration: 1500,
                                });
                            }
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
