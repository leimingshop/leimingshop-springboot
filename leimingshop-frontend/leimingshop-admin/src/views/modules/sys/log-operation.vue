<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-button
            class="btn"
            style="right: 20px; top: 12px; position: absolute"
            plain
            @click="exportHandle()"
            >{{ $t("export") }}</el-button
        >
        <div class="mod-sys__log-operation">
            <div id="control-area">
                <el-form
                    :inline="true"
                    :model="dataForm"
                    class="grayLine"
                    @keyup.enter.native="getData()"
                >
                    <el-form-item label="模块名称：">
                        <el-input
                            v-model="dataForm.module"
                            :placeholder="$t('logOperation.module')"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="状态：">
                        <el-select
                            v-model="dataForm.status"
                            :placeholder="$t('logOperation.status')"
                            clearable
                        >
                            <el-option
                                :label="$t('logOperation.status0')"
                                :value="0"
                            ></el-option>
                            <el-option
                                :label="$t('logOperation.status1')"
                                :value="1"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="getData()" type="primary">{{
                            $t("query")
                        }}</el-button>
                        <el-button type="primary" plain @click="reset()"
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
                        <div class="iconSize">
                            1、查看管理员的操作日志，系统对管理员的关键操作进行了记录
                        </div>
                    </template>
                </el-alert>
            </div>
            <el-table
                v-loading="dataListLoading"
                :data="dataList"
                border
                @sort-change="dataListSortChangeHandle"
                style="width: 100%"
                :height="
                    !$store.state.mainSwitch ? tableheight : tableheightBig
                "
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
                <el-table-column
                    prop="creator"
                    :label="$t('logOperation.creatorName')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="operation"
                    :label="$t('logOperation.operation')"
                    header-align="center"
                    align="center"
                    width="140"
                ></el-table-column>
                <el-table-column
                    prop="module"
                    :label="$t('logOperation.module')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="requestUri"
                    :label="$t('logOperation.requestUri')"
                    header-align="center"
                    align="center"
                    width="180"
                ></el-table-column>
                <el-table-column
                    prop="requestMethod"
                    :label="$t('logOperation.requestMethod')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="requestParams"
                    :label="$t('logOperation.requestParams')"
                    header-align="center"
                    align="center"
                    width="150"
                    :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                    prop="requestTime"
                    :label="$t('logOperation.requestTime')"
                    sortable="custom"
                    header-align="center"
                    align="center"
                    width="120"
                >
                    <template slot-scope="scope">
                        {{ `${scope.row.requestTime}ms` }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="status"
                    :label="$t('logOperation.status')"
                    sortable="custom"
                    header-align="center"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.status === 0"
                            size="small"
                            type="danger"
                            >{{ $t("logOperation.status0") }}</el-tag
                        >
                        <el-tag v-else size="small" type="success">{{
                            $t("logOperation.status1")
                        }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ip"
                    :label="$t('logOperation.ip')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="userAgent"
                    :label="$t('logOperation.userAgent')"
                    header-align="center"
                    align="center"
                    width="150"
                    :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    :label="$t('logOperation.createDate')"
                    sortable="custom"
                    header-align="center"
                    align="center"
                    width="180"
                ></el-table-column>
            </el-table>
            <el-pagination
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
            >
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import qs from "qs";
    import Cookies from "js-cookie";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/log/operation/page",
                    getDataListIsPage: true,
                    exportURL: "/admin-api/log/operation/export",
                },
                breaddata: ["网站管理", "日志", "操作日志"],
                dataForm: {
                    module: "",
                    status: "",
                },
                tableheight: document.body.offsetHeight - 420,
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
                        140;
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
                this.dataForm = {
                    module: "",
                    status: "",
                };
                this.getData();
            },
            exportHandle() {
                let that = this;
                var params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                    ...this.dataForm,
                });
                this.$http
                    .get(
                        `${window.SITE_CONFIG["apiURL"]}${this.mixinViewModuleOptions.exportURL}?${params}`
                    )
                    .then((res) => {
                        that.$message({
                            message: "请到系统管理-导出管理下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
        },
    };
</script>
