<template>
    <div>
        <div id="control-area">
            <Bread
                :breaddata="breaddata"
                @changePage="changePage"
                :index="'2'"
            ></Bread>

            <!--导出按钮-->
            <importAndExport
                style="right: 20px; top: 12px; position: absolute"
                :importAndExportOptions="importAndExportOptions"
                :dataForm="dataForm"
                @getDataList="getDataList"
                v-if="$hasPermission('sys:memberlog:export')"
            ></importAndExport>

            <el-form
                :inline="true"
                :model="dataFormTemp"
                class="demo-form-inline grayLine"
                ref="dataForm"
            >
                <el-form-item label="输入搜索：" prop="memberId" clearable>
                    <el-input
                        v-model="dataFormTemp.memberId"
                        placeholder="会员名称/手机号"
                    ></el-input>
                </el-form-item>
                <el-form-item label="操作时间：">
                    <el-date-picker
                        v-model="valuetime"
                        type="datetimerange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        align="left"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="登录端：" prop="source" clearable>
                    <el-select
                        v-model="dataFormTemp.source"
                        placeholder="活动区域"
                    >
                        <el-option label="全部" value></el-option>
                        <el-option label="手机" value="1"></el-option>
                        <el-option label="PC" value="0"></el-option>
                        <el-option label="其他" value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getData">查询</el-button>
                    <el-button type="primary" plain @click="rest"
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <div></div>
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
                    <div class="iconSize">1、登录日志可导出到本地excel文件</div>
                    <div class="iconSize">
                        2、登录结果分为登录成功和登录失败
                    </div>
                </template>
            </el-alert>
        </div>

        <el-table
            :data="dataList"
            border
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">{{
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>
            <el-table-column
                prop="loginName"
                label="会员名称"
                width="150"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="phoneNumber"
                label="手机号"
                width="150"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="createDate"
                label="操作时间"
                width="250"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="source"
                label="登录端"
                width="80"
                :formatter="sourceRule"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="ip"
                label="操作人IP"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="operation"
                label="操作内容"
                :formatter="stateRule"
                align="center"
            ></el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
    </div>
</template>
<script>
    import importAndExport from "@/components/import-and-export";
    import { memberloginlogExport } from "@/api/io";
    import mixinViewModule from "@/mixins/view-module";
    import cloneDeep from "lodash/cloneDeep";
    import Bread from "@/components/bread";
    import { loginLog } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: memberloginlogExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: loginLog,
                    activatedIsNeed: false,
                    getDataListIsPage: true,
                    // deleteURL: deleteMemberUrl,
                    deleteIsBatch: false,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                dataFormTemp: {
                    starTime: "",
                    endTime: "",
                    source: "",
                    memberId: "",
                    memberId1: "",
                },
                dataForm: {
                    starTime: "",
                    endTime: "",
                    source: "",
                    memberId: "",
                },
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                valuetime: "", //开始结束时间
                breaddata: ["会员系统", "会员管理", "会员列表", "登录日志"],
                tableheight: document.body.offsetHeight - 380,
                tableheightBig: 300,
                stateRule(row, column) {
                    return row.status == 0 ? "登录成功" : "登录失败";
                },
                sourceRule(row, column) {
                    return row.source == 0
                        ? "PC"
                        : row.source == 1
                        ? "手机"
                        : "其他";
                },
            };
        },
        activated() {
            console.log(111);
            this.dataFormTemp.memberId1 = this.$route.query.memberId;
            this.dataForm.memberId = this.$route.query.memberId;
            this.getData();
        },
        watch: {
            valuetime(val) {
                if (!val) {
                    this.dataFormTemp.starTime = "";
                    this.dataFormTemp.endTime = "";
                }
            },
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        40;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            //选择开始结束时间
            changeTime() {
                if (this.valuetime && this.valuetime.length > 1) {
                    this.dataFormTemp.starTime = this.valuetime[0];
                    this.dataFormTemp.endTime = this.valuetime[1];
                } else {
                    this.dataFormTemp.starTime = "";
                    this.dataFormTemp.endTime = "";
                }
            },
            getData() {
                this.changeTime();
                this.page = 1;
                this.limit = 10;

                this.dataForm = cloneDeep(this.dataFormTemp);
                if (this.dataFormTemp.memberId1) {
                    this.dataForm.memberId = this.dataFormTemp.memberId1;
                }
                this.getDataList();
            },
            //返回上一级
            changePage() {
                this.$router.push({
                    name: "mgvip-list-list",
                });
            },
            //重置
            rest() {
                this.page = 1;
                this.$refs["dataForm"].resetFields();
                this.valuetime = [];
                this.dataFormTemp.starTime = "";
                this.dataFormTemp.endTime = "";
                this.dataFormTemp.memberId = "";
                this.getData();
            },
            // 每页数
            sizeChangeHandle(val) {
                this.params.currentPageSize = val;
                this.params.currentPage = 1;
                this.limit = val;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle(val) {
                this.params.currentPage = val;
                this.page = val;
                this.getDataList();
            },
        },
        components: {
            Bread,
            importAndExport,
        },
    };
</script>