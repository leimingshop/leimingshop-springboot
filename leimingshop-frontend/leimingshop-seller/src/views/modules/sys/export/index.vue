<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata" class="bread"></Bread>
            <!-- <el-button class="relaod-button" type="primary" @click="getData">刷新</el-button> -->

            <el-form
                :inline="true"
                :model="dataForm"
                class="grayLine"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="任务名称：">
                    <el-input
                        v-model="dataForm.assignmentName"
                        :placeholder="$t('role.name')"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="状态：">
                    <el-select
                        v-model="dataForm.operationStatus"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button @click="getData()" type="primary">{{
                        $t("query")
                    }}</el-button>
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
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
                    <div class="iconSize">1、商家导出的相关数据在此下载</div>
                    <div class="iconSize">2、如下载状态为失败则需重新下载</div>
                    <div class="iconSize">3、进行中的需等待完成后可下载</div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            style="width: 100%; margin-top: 10px"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                prop="assignmentName"
                label="任务名称"
                width="180"
                align="center"
            >
                <template slot-scope="scope">
                    <div :title="scope.row.assignmentName">
                        {{ scope.row.assignmentName }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="createDate"
                label="操作时间"
                width="280"
                align="center"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.createDate }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="finishTime"
                label="完成时间"
                width="280"
                align="center"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.operationStatus == 2">
                        {{ scope.row.finishTime }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="creator" label="操作人" align="center">
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.creator }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="operationStatus "
                label="状态"
                align="center"
            >
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.operationStatus == 0" type="warning"
                        >失败</el-tag
                    >
                    <el-tag v-if="scope.row.operationStatus == 1" type="info"
                        >进行中</el-tag
                    >
                    <el-tag v-if="scope.row.operationStatus == 2" type="success"
                        >已完成</el-tag
                    >
                </template>
            </el-table-column>
            <el-table-column
                prop="address"
                label="操作"
                align="center"
                width="250"
            >
                <template slot-scope="scope">
                    <a
                        v-if="
                            scope.row.operationStatus == 2 &&
                            scope.row.operatingLogo == 1
                        "
                        :href="scope.row.downloadLink | filterImgUrl"
                    >
                        <el-button type="text" size="mini">下载</el-button>
                    </a>
                    <el-button
                        class="artdanger"
                        @click="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        style="margin-left: 8px"
                        v-if="$hasPermission('sys:export:delete')"
                        >删除</el-button
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
    import mixinViewModule from "@/mixins/view-module";
    import { getsysexportmanagement, delSysExportManagement } from "@/api/url";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: getsysexportmanagement,
                    getDataListIsPage: true,
                    deleteURL: delSysExportManagement,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
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
                        label: "进行中",
                    },
                    {
                        value: 2,
                        label: "已完成",
                    },
                ],
                breaddata: ["系统管理", "导出管理"],
                downloadLink: "",
                tableheight: document.body.offsetHeight - 330,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
        },
        created() {},
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        120;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm = {};
                this.getData();
            },
        },
    };
</script>
<style lang="scss" scoped>
    .cell div {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
    }
    .relaod-button {
        float: right;
        margin-bottom: 5px;
    }
    .bread {
        display: inline-block;
    }
</style>
