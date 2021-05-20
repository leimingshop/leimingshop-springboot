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
                        v-model="dataForm.memberName"
                        placeholder="会员账号"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="联系人电话：">
                    <el-input
                        v-model="dataForm.mobileNumber"
                        placeholder="联系人电话"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="反馈类型：">
                    <el-select
                        v-model="dataForm.feedbackType"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option label="全部" value=""> </el-option>
                        <el-option
                            v-for="item in feedbackType"
                            :key="item.dictValue"
                            :label="item.dictName"
                            :value="item.dictValue"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="反馈来源：">
                    <el-select
                        v-model="dataForm.feedbackSource"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option
                            v-for="item in feedbackSource"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="反馈判定：">
                    <el-select
                        v-model="dataForm.decideType"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option
                            v-for="item in decideType"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="处理人：">
                    <el-input
                        v-model="dataForm.operator"
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

                <el-form-item label="反馈时间：">
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
                    v-model="dataForm.disposeStatus"
                    @change="handleClick(dataForm.disposeStatus)"
                    type="primary"
                    style="margin-bottom: 0px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="0" width="170"
                        >待处理</el-radio-button
                    >
                    <el-radio-button label="1" width="170"
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
                        1、反馈处理用于处理并记录用户在商城反馈的商城问题
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

            <el-table-column prop="memberName" label="会员账号" align="center">
            </el-table-column>
            <el-table-column
                prop="mobileNumber"
                align="center"
                width="130"
                label="联系人电话"
            >
            </el-table-column>
            <el-table-column
                prop="feedbackType"
                align="center"
                label="反馈类型"
                width="120"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.feedbackType == 1">功能异常</span>
                    <span v-if="scope.row.feedbackType == 2">优化建议</span>
                    <span v-if="scope.row.feedbackType == 3">其他反馈</span>
                </template>
            </el-table-column>

            <el-table-column
                prop="createDate"
                label="反馈时间"
                align="center"
                width="155"
            >
            </el-table-column>

            <el-table-column
                prop="feedbackSource"
                align="center"
                label="反馈来源"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.feedbackSource == 0">pc</span>
                    <span v-if="scope.row.feedbackSource == 1">H5</span>
                    <span v-if="scope.row.feedbackSource == 2">android</span>
                    <span v-if="scope.row.feedbackSource == 3">ios</span>
                    <span v-if="scope.row.feedbackSource == 4">微信</span>
                    <span v-if="scope.row.feedbackSource == 5">小程序</span>
                </template>
            </el-table-column>
            <el-table-column prop="decideType" align="center" label="反馈判定">
                <template slot-scope="scope">
                    <span v-if="!scope.row.decideType"></span>
                    <span v-if="scope.row.decideType == 0">无效反馈</span>
                    <span v-if="scope.row.decideType == 1">有效反馈</span>
                    <span v-if="scope.row.decideType == 2">重点问题</span>
                </template>
            </el-table-column>

            <el-table-column prop="operator" align="center" label="处理人">
            </el-table-column>

            <el-table-column
                prop="disposeDate"
                align="center"
                label="处理时间"
                width="155"
            >
            </el-table-column>

            <el-table-column
                prop="disposeStatus"
                align="center"
                label="处理状态"
            >
                <template slot-scope="scope">
                    <span style="color: red" v-if="scope.row.disposeStatus == 0"
                        >待处理</span
                    >
                    <span v-if="scope.row.disposeStatus == 1">已处理</span>
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
                        <span v-show="scope.row.disposeStatus == 1">查看</span>
                        <span v-show="scope.row.disposeStatus == 0">处理</span>
                    </el-button>
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
    import { feedbackType } from "@/api/api";
    import { feedbackEx } from "@/api/io";
    import importAndExport from "@/components/import-and-export";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                importAndExportOptions: {
                    exportUrl: feedbackEx, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/feedback/page",
                    getDataListIsPage: true,
                    dataListLoading: false,
                },
                breaddata: ["运营管理", "客服管理", "反馈列表"],

                dataForm: {
                    feedbackType: "",
                    feedbackSource: "",
                    decideType: "",
                    memberName: "",
                    mobileNumber: "",
                    operator: "",
                    disposeStatus: "",
                    valuetime: [],
                    disposeTime: [],
                },
                feedbackType: [],
                feedbackSource: [
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
                decideType: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: 0,
                        name: "无效反馈",
                    },
                    {
                        id: 1,
                        name: "有效反馈",
                    },
                    {
                        id: 2,
                        name: "重点问题",
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
            this.feedback();
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
                        90;
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
                    feedbackType: "",
                    feedbackSource: "",
                    decideType: "",
                    memberName: "",
                    mobileNumber: "",
                    operator: "",
                    disposeStatus: "",
                    valuetime: [],
                    disposeTime: [],
                };
                this.getData();
            },
            // 获取反馈类型
            feedback() {
                feedbackType().then((res) => {
                    if ((res.code = 200)) {
                        this.feedbackType = res.data;
                    }
                });
            },

            getGoodKind2() {},
            //切换tab搜索
            handleClick(value) {
                this.dataForm.disposeStatus = value;
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