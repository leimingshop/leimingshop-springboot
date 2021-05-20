<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <importAndExport
                style="right: 20px; top: 12px; position: absolute"
                :importAndExportOptions="importAndExportOptions"
                :dataForm="dataForm"
            ></importAndExport>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                ref="dataForm"
                maxlength="20"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="会员账号：">
                    <el-input
                        v-model="dataForm.account"
                        placeholder="会员账号"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="联系人电话：">
                    <el-input
                        v-model="dataForm.phone"
                        placeholder="联系人电话"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="投诉类型：">
                    <el-select
                        v-model="dataForm.type"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option label="全部" value=""> </el-option>
                        <el-option
                            v-for="item in type"
                            :key="item.dictValue"
                            :label="item.dictName"
                            :value="item.dictValue"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="投诉来源：">
                    <el-select
                        v-model="dataForm.source"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option
                            v-for="item in source"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="投诉判定：">
                    <el-select
                        v-model="dataForm.verdict"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option
                            v-for="item in verdict"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="处理人：">
                    <el-input
                        v-model="dataForm.disposePerson"
                        placeholder="处理人"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="处理时间：">
                    <el-date-picker
                        v-model="disposeTime"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        @blur="acttime"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="投诉时间：">
                    <el-date-picker
                        v-model="valuetime"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        @blur="acttime"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button
                        class="btn"
                        @click="reset('dataForm')"
                        type="primary"
                        plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="dataForm.status"
                    @change="handleClick(dataForm.status)"
                    type="primary"
                    style="margin-bottom: 0px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="10" width="170"
                        >待处理</el-radio-button
                    >
                    <el-radio-button label="20" width="170"
                        >已处理</el-radio-button
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
                        1、投诉处理用于处理并记录用户在商城投诉的商城问题。
                    </div>
                    <div class="iconSize">
                        2、投诉主要针对的是用户产生交易之后对具体订单产生的投诉
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            ref="dataList"
            border=""
            style="width: 100%"
            v-loading="dataListLoading"
            @selection-change="dataListSelectionChangeHandle"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <!-- <el-table-column
	      type="selection"
				align="center"
	      width="70">
	  </el-table-column> -->
            <el-table-column
                type="index"
                prop="$index"
                align="center"
                label="序号"
                width="70"
            >
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>

            <el-table-column prop="account" label="会员账号" align="center">
            </el-table-column>
            <el-table-column
                prop="phone"
                align="center"
                width="130"
                label="联系人电话"
            >
            </el-table-column>
            <el-table-column
                prop="type"
                align="center"
                label="投诉类型"
                width="120"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.type == 1">服务相关</span>
                    <span v-if="scope.row.type == 2">物流相关</span>
                    <span v-if="scope.row.type == 3">售后相关</span>
                    <span v-if="scope.row.type == 4">商品相关</span>
                    <span v-if="scope.row.type == 5">其他</span>
                </template>
            </el-table-column>

            <el-table-column
                prop="createDate"
                label="投诉时间"
                align="center"
                width="155"
            >
            </el-table-column>

            <el-table-column prop="source" align="center" label="投诉来源">
                <template slot-scope="scope">
                    <span v-if="scope.row.source == 0">pc</span>
                    <span v-if="scope.row.source == 1">H5</span>
                    <span v-if="scope.row.source == 2">android</span>
                    <span v-if="scope.row.source == 3">ios</span>
                    <span v-if="scope.row.source == 4">微信</span>
                    <span v-if="scope.row.source == 5">小程序</span>
                </template>
            </el-table-column>
            <el-table-column prop="verdict" align="center" label="投诉判定">
                <template slot-scope="scope">
                    <span v-if="!scope.row.verdict"></span>
                    <span v-if="scope.row.verdict == 1">有效投诉</span>
                    <span v-if="scope.row.verdict == 2">重点问题</span>
                    <span v-if="scope.row.verdict == 3">无效投诉</span>
                </template>
            </el-table-column>

            <el-table-column
                prop="customerSatisfaction"
                align="center"
                label="满意度"
            >
                <template slot-scope="scope">
                    <span v-if="!scope.row.customerSatisfaction"></span>
                    <span
                        v-if="
                            scope.row.customerSatisfaction == 1 ||
                            scope.row.customerSatisfaction == 2
                        "
                        >差</span
                    >
                    <span
                        v-if="
                            scope.row.customerSatisfaction == 3 ||
                            scope.row.customerSatisfaction == 4
                        "
                        >一般</span
                    >
                    <span v-if="scope.row.customerSatisfaction == 5">满意</span>
                </template>
            </el-table-column>

            <el-table-column prop="disposePerson" align="center" label="处理人">
            </el-table-column>

            <el-table-column
                prop="disposeDate"
                align="center"
                label="处理时间"
                width="155"
            >
            </el-table-column>

            <el-table-column prop="status" align="center" label="处理状态">
                <template slot-scope="scope">
                    <span style="color: red" v-if="scope.row.status == 10"
                        >待处理</span
                    >
                    <span v-if="scope.row.status == 20">已处理</span>
                </template>
            </el-table-column>

            <el-table-column
                prop="address"
                align="center"
                width="140"
                label="操作"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="showDetail(scope.row)"
                    >
                        <span v-show="scope.row.status == 20">查看</span>
                        <span v-show="scope.row.status == 10">处理</span>
                    </el-button>
                    <!-- <el-button type="text" size="small" v-show="scope.row.status==10" @click="showDetail(scope.row.id)" >处理</el-button> -->
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="params.currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="params.currentPageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        >
        </el-pagination>
    </div>
</template>

<script>
    //import list from "./list.vue"
    import mixinViewModule from "@/mixins/view-module";
    import { complainType } from "@/api/api";
    import Bread from "@/components/bread";
    import { ordercomplainEx } from "@/api/io";
    import importAndExport from "@/components/import-and-export";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                importAndExportOptions: {
                    exportUrl: ordercomplainEx, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/ordercomplain/page",
                    getDataListIsPage: true,
                    dataListLoading: false,
                },
                breaddata: ["运营管理", "客服管理", "投诉列表"],

                dataForm: {
                    type: "",
                    source: "",
                    verdict: "",
                    account: "",
                    phone: "",
                    disposePerson: "",
                    status: "",
                    valuetime: [],
                    disposeTime: [],
                },
                // type: [{ id: '', name: "全部" },{ id: 1, name: "相关服务" },{ id: 2, name: "物流相关" },{ id: 3, name: "售后相关" },{ id: 4, name: "商品相关" },{ id: 5, name: "其他" }],
                type: [],
                source: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: 0,
                        name: "PC",
                    },
                    {
                        id: 1,
                        name: "H5",
                    },
                    {
                        id: 2,
                        name: "android",
                    },
                    {
                        id: 3,
                        name: "ios",
                    },
                    {
                        id: 4,
                        name: "微信",
                    },
                    {
                        id: 5,
                        name: "小程序",
                    },
                ],
                verdict: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: 1,
                        name: "有效投诉",
                    },
                    {
                        id: 2,
                        name: "重点问题",
                    },
                    {
                        id: 3,
                        name: "无效投诉",
                    },
                ],
                valuetime: "",
                disposeTime: "",
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        created() {
            this.complainType();
        },
        components: {
            Bread,
            importAndExport,
        },
        watch: {
            valuetime(val) {
                if (!val) {
                    this.dataForm.strTime = "";
                    this.dataForm.endTime = "";
                }
            },
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
            init() {
                this.getData();
            },
            // 获取数据,
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 重置
            reset(dataForm) {
                this.valuetime = "";
                this.disposeTime = "";
                this.dataForm = {
                    type: "",
                    source: "",
                    verdict: "",
                    account: "",
                    phone: "",
                    disposePerson: "",
                    status: "",
                    valuetime: [],
                    disposeTime: [],
                };
                this.getData();
            },
            // 获取投诉类型
            complainType() {
                complainType().then((res) => {
                    if ((res.code = 200)) {
                        this.type = res.data;
                    }
                });
            },

            getGoodKind2() {},
            //切换tab搜索
            handleClick(value) {
                this.dataForm.status = value;
                this.getData();
            },
            // 详情
            showDetail(row) {
                this.$emit("showDetail", row);
            },
            // 每页数
            sizeChangeHandle(val) {
                this.page = 1;
                this.limit = val;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle(val) {
                this.params.currentPage = val;
                this.page = val;
                this.getDataList();
            },
            //开始结束时间
            acttime() {
                this.dataForm.createStartDate = this.valuetime[0];
                this.dataForm.createEndDate = this.valuetime[1];
                this.dataForm.disposeStartDate = this.disposeTime[0];
                this.dataForm.disposeEndDate = this.disposeTime[1];
            },

            artaddmassage() {
                this.$router.push({
                    name: "content-informationmanage-informationupdata",
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";

    // 表头背景和字体颜色
    /deep/ .el-table__header th {
        background: #f5f7fa;
    }

    .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
        white-space: nowrap;
    }

    *::-webkit-scrollbar {
        width: 7px;
        height: 1px;
        background-color: transparent;
    }

    /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    *::-webkit-scrollbar-track {
        background-color: #f0f6ff;
    }

    /*定义滚动条轨道 内阴影+圆角*/
    *::-webkit-scrollbar-thumb {
        background-color: #e3e4e4;
        border-radius: 6px;
    }

    /*定义滑块 内阴影+圆角*/
    .scrollbarHide::-webkit-scrollbar {
        display: none;
    }

    .scrollbarShow::-webkit-scrollbar {
        display: block;
    }

    .el-input {
        width: 170px;
        height: 40px;
    }

    /deep/ .el-tabs__nav-wrap {
        border-bottom: 2px #efefef dotted;

        &::after {
            content: unset;
        }
    }

    /deep/ .el-tabs__nav-scroll {
        height: 60px;
        line-height: 60px;

        /deep/ .el-tabs__item {
            width: 120px;
            // box-sizing: border-box;
            text-align: center;
        }
    }

    /deep/ .el-tabs__active-bar {
        // width: 120px !important;
    }

    .grayBtn {
    }

    //
    .btn {
        width: auto;
    }
</style>