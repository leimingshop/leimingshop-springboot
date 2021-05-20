<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                ref="dataForm"
                maxlength="20"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="标题：">
                    <el-input
                        v-model="dataForm.messageTitle"
                        placeholder="信息标题名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="发件人：">
                    <el-input
                        v-model="dataForm.sendName"
                        placeholder="发件人"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="读取状态：">
                    <el-select v-model="dataForm.status">
                        <el-option label="全部" value=""> </el-option>
                        <el-option label="未读" value="0"> </el-option>
                        <el-option label="已读" value="1"> </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="信息类型：">
                    <el-select
                        @change="changeSelect(dataForm.messageType)"
                        v-model="dataForm.messageType"
                        placeholder="请选择"
                        loading-text="加载中···"
                        @visible-change="getGoodKind2"
                    >
                        <el-option key="0" label="私信" value="0"> </el-option>
                        <el-option key="1" label="系统消息" value="1">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发送时间：">
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

            <el-form>
                <el-form-item>
                    <el-button
                        class="btn"
                        @click="batchRead()"
                        type="primary"
                        plain
                        >批量已读
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <div>
                    <el-radio-group
                        v-model="activeName"
                        @change="handleClick(activeName)"
                        type="primary"
                        style="margin-top: 5px; margin-bottom: 5px"
                    >
                        <el-radio-button label="">全部</el-radio-button>
                        <el-radio-button label="0" width="170"
                            >平台私信</el-radio-button
                        >
                        <el-radio-button label="1" width="170"
                            >系统消息</el-radio-button
                        >
                    </el-radio-group>
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
                        1、站内信分为系统消息和私信，系统消息为平台给全体商户发送的通知，平台私信为平台为特定商户发送的消息通知
                    </div>
                    <div class="iconSize">
                        2、未读的站内信可勾选复选框进行批量已读操作
                    </div>
                    <div class="iconSize">
                        3、未读的站内信点击查看该条站内信状态变为已读
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            ref="dataList"
            border=""
            style="width: 100%; margin-top: 8px"
            v-loading="dataListLoading"
            @selection-change="dataListSelectionChangeHandle"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" align="center" width="70">
            </el-table-column>
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
            <el-table-column prop="sendTime" align="center" label="发送日期">
            </el-table-column>
            <el-table-column
                prop="sendName"
                align="center"
                width="180"
                label="发送人"
            >
            </el-table-column>
            <el-table-column
                prop="status"
                align="center"
                label="状态"
                width="120"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.status == 0">未读</span>
                    <span v-else-if="scope.row.status == 1">已读</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="messageType"
                align="center"
                label="信息类型"
                width="120"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.messageType == 0">平台私信</span>
                    <span v-else-if="scope.row.messageType == 1">系统消息</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="messageTitle"
                label="信息标题"
                align="center"
            >
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
                        @click="showDetail(scope.row.id)"
                        >查看</el-button
                    >
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        >删除</el-button
                    >
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
    import { messagePage, deletemessage, batchRead } from "@/api/api";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "seller-api/message/text/page",
                    getDataListIsPage: true,
                    activatedIsNeed: false,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: "seller-api/message/text",
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                breaddata: ["内容管理", "站内信列表"],
                goodKind2loading: false,
                goodKind3loading: false,
                dataForm: {
                    status: "",
                    messageTitle: "",
                    messageType: "",
                    strTime: "",
                    endTime: "",
                },
                tableData: [],
                goodKindList1: [
                    { id: "", name: "全部" },
                    { id: 1, name: "系统信息" },
                    { id: 0, name: "私信" },
                ],
                activeName: "",
                valuetime: "",
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 300,
            };
        },
        activated() {
            if (
                this.$route.query.messageType ||
                this.$route.query.messageType == 0
            ) {
                // 0退货，1换货，1仅退款，
                this.dataForm.messageType = +this.$route.query.messageType;
            }
            if (this.$route.query.status || this.$route.query.status == 0) {
                //
                this.dataForm.status = this.$route.query.status;
                this.activeName = 1;
            }
            // 消息通知列表跳到站内信-平台私信
            if (
                this.$route.query.activeName2 ||
                this.$route.query.activeName2 == 0
            ) {
                this.activeName = 0;
            }
            // 消息通知列表跳到站内信-系统消息
            if (this.$route.query.activeName2 == 1) {
                this.activeName = 1;
            }
            if (this.$route.query.index || this.$route.query.index == 9) {
                this.$emit("showDetail", this.$route.query.id);
            }
            this.getDataList();
        },
        created() {
            this.dataForm.messageType = this.goodKindList1[0].id;
            this.getDataList();
        },
        components: {
            Bread,
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
            //  getDataList() {},
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
                this.activeName = this.dataForm.messageType;
            },
            reset(dataForm) {
                this.$refs[dataForm].resetFields();
                this.dataForm.messageTitle = "";
                this.dataForm.status = "";
                this.dataForm.messageType = this.goodKindList1[0].id;
                this.dataForm.strTime = "";
                this.dataForm.endTime = "";
                this.dataForm.sendName = "";
                this.valuetime = "";
                this.activeName = "";
                this.getData();
            },
            // 批量已读
            batchRead() {
                var flag = true;
                const arrId = [];

                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });
                if (arrId.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择消息!",
                    });
                    return;
                }
                if (flag) {
                    this.$confirm("您确认已读所有选中消息吗？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            const obj = {
                                ids: arrId,
                            };
                            batchRead(arrId).then((res) => {
                                if (res.code == 200) {
                                    this.$message({
                                        type: "success",
                                        message: "已读成功!",
                                    });
                                    this.getDataList();
                                } else {
                                    this.$message({
                                        type: "warning",
                                        message: "已读失败!",
                                    });
                                    this.getDataList();
                                }
                            });
                        })
                        .catch(() => {
                            this.$message({
                                type: "info",
                                message: "已取消",
                            });
                        });
                }
            },
            getGoodKind2() {},
            getGoodKind3() {},
            handleClick(tab, event) {
                console.log(tab.name);
            },
            showDetail(row) {
                this.$emit("showDetail", row);
            },
            //信息类型
            formatmessageType(row, column) {
                return row.messageType == 1 ? "系统信息" : "私信";
            },
            //发送的方式
            formatsendMode(row, column) {
                if (row.sendMode) {
                    let sendModeresult = "";
                    let sendModeArr = row.sendMode.split(",");
                    if (sendModeArr.indexOf("0") > -1) {
                        sendModeresult = sendModeresult + "站内信 ";
                    }
                    if (sendModeArr.indexOf("1") > -1) {
                        sendModeresult = sendModeresult + "短信 ";
                    }
                    if (sendModeArr.indexOf("2") > -1) {
                        sendModeresult = sendModeresult + "邮件 ";
                    }
                    if (sendModeresult.length == 0) {
                        return "";
                    } else {
                        return sendModeresult.substring(
                            0,
                            sendModeresult.length - 1
                        );
                    }
                } else {
                    return "";
                }
            },
            //切换tab
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                if (tab == "") {
                    this.dataForm.messageType = "";
                    this.dataForm.status = "";
                } else if (tab == "0") {
                    this.dataForm.messageType = this.goodKindList1[2].id;
                } else if (tab == "1") {
                    this.dataForm.messageType = this.goodKindList1[1].id;
                }
                this.dataForm.messageType = tab;
                this.getDataList();
            },
            // 每页数
            sizeChangeHandle(val) {
                this.params.currentPageSize = val;
                this.params.currentPage = 1;
                this.limit = val;
                this.getDataList();
            },
            changeSelect(value) {
                // this.activeName=value;
                // this.handleClick(value)
            },
            // 当前页
            currentChangeHandle(val) {
                this.params.currentPage = val;
                this.page = val;
                this.getDataList();
            },
            //开始结束时间
            acttime() {
                this.dataForm.strTime = this.valuetime[0];
                this.dataForm.endTime = this.valuetime[1];
            },
            //跳转站内信
            artaddmassage() {
                this.$router.push({
                    name: "content-informationmanage-informationupdata",
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    /* .el-scrollbar__wrap {
                                                                              overflow-x: hidden;
                                                                            } */
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
    } /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    *::-webkit-scrollbar-track {
        background-color: #f0f6ff;
    } /*定义滚动条轨道 内阴影+圆角*/
    *::-webkit-scrollbar-thumb {
        background-color: #e3e4e4;
        border-radius: 6px;
    } /*定义滑块 内阴影+圆角*/
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

