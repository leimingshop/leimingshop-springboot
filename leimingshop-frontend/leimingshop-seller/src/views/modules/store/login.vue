<template>
    <div class="storeLogin">
        <div id="control-area">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>店铺管理</el-breadcrumb-item>
                <el-breadcrumb-item>登陆日志</el-breadcrumb-item>
            </el-breadcrumb>

            <!--导出按钮-->
            <importAndExport
                style="right: 20px; top: 12px; position: absolute"
                :importAndExportOptions="importAndExportOptions"
                :dataForm="dataForm"
                @getDataList="getDataList"
                v-if="$hasPermission('sys:storeloginlog:export')"
            ></importAndExport>

            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="用户名：">
                    <el-input
                        v-model="dataForm.creator"
                        placeholder="商家名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="登录状态：">
                    <el-select v-model="dataForm.status" placeholder="请选择">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户操作：">
                    <el-select
                        v-model="dataForm.operation"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in options1"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <el-form>
                <el-form-item>
                    <!-- <el-button @click="exportHandle()" type="primary" plain>导出</el-button> -->
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
                    <div class="iconSize">
                        1、登录日志为商户登录账号登录的记录
                    </div>
                    <div class="iconSize">
                        2、查看登录日志可查看登录的用户端操作及登录状态
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
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
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column prop="creator" label="用户名" align="center">
            </el-table-column>
            <el-table-column
                prop="ip"
                label="操作IP"
                align="center"
                width="180"
            >
            </el-table-column>
            <el-table-column
                prop="operation"
                width="140"
                align="center"
                label="用户操作"
            >
                <template slot-scope="scope">
                    <span>{{
                        scope.row.operation == 0 ? "用户登录" : "用户退出"
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="status"
                align="center"
                width="140"
                label="登录状态"
            >
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.status == 1"
                        >成功</el-tag
                    >
                    <el-tag type="danger" v-else-if="scope.row.status == 0"
                        >失败</el-tag
                    >
                    <el-tag type="warning" v-else>锁定</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="createDate"
                align="center"
                width="180"
                label="操作日期"
            >
            </el-table-column>
            <el-table-column prop="userAgent" align="left" label="用户代理">
                <template slot-scope="scope">
                    <div class="words" :title="scope.row.userAgent">
                        {{ scope.row.userAgent }}
                    </div>
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
    import mixinViewModule from "@/mixins/view-module";
    import importAndExport from "@/components/import-and-export";
    import { storeloginlogExport } from "@/api/io";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: storeloginlogExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/storeloginlog/page",
                    getDataListIsPage: true,
                    exportURL: "/seller-api/storeloginlog/export",
                },
                dataForm: {
                    operation: "",
                    status: "",
                },
                options: [
                    {
                        value: "",
                        label: "全部",
                    },
                    {
                        value: 0,
                        label: "失败",
                    },
                    {
                        value: 1,
                        label: "成功",
                    },
                    {
                        value: 2,
                        label: "锁定",
                    },
                ],
                options1: [
                    {
                        value: "",
                        label: "全部",
                    },
                    {
                        value: 0,
                        label: "用户登录",
                    },
                    {
                        value: 1,
                        label: "用户退出",
                    },
                ],
                tableheight: document.body.offsetHeight - 360,
                tableheightBig: 300,
            };
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
        components: {
            importAndExport,
        },
        methods: {
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.dataForm = {
                    operation: "",
                    status: "",
                };
                this.getData();
            },
        },
    };
</script>
<style lang="scss">
    // @import "@/element-ui/theme-variables.scss";
    .storeLogin {
        .words {
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            line-clamp: 2;
            -webkit-box-orient: vertical;
        }
    }
</style>
