<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-button
            style="right: 20px; top: 12px; position: absolute"
            class="btn"
            plain
            @click="exportHandle()"
            >{{ $t("export") }}</el-button
        >

        <div class="mod-sys__log-error">
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
                            :placeholder="$t('logError.module')"
                            clearable
                        ></el-input>
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
                            1、查看管理员的异常日志，系统对管理员的关键操作进行了记录
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
                    prop="module"
                    :label="$t('logError.module')"
                    header-align="center"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="requestUri"
                    :label="$t('logError.requestUri')"
                    header-align="center"
                    align="center"
                    width="240"
                ></el-table-column>
                <el-table-column
                    prop="requestMethod"
                    :label="$t('logError.requestMethod')"
                    header-align="center"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="requestParams"
                    :label="$t('logError.requestParams')"
                    header-align="center"
                    align="center"
                    width="450"
                    :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                    prop="ip"
                    :label="$t('logError.ip')"
                    header-align="center"
                    align="center"
                    width="140"
                ></el-table-column>
                <el-table-column
                    prop="userAgent"
                    :label="$t('logError.userAgent')"
                    header-align="center"
                    align="center"
                    width="150"
                    :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    :label="$t('logError.createDate')"
                    sortable="custom"
                    header-align="center"
                    align="center"
                    width="180"
                ></el-table-column>
                <el-table-column
                    :label="$t('handle')"
                    fixed="right"
                    header-align="center"
                    align="center"
                    width="120"
                >
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            size="small"
                            @click="infoHandle(scope.row.errorInfo)"
                            >{{ $t("logError.errorInfo") }}</el-button
                        >
                    </template>
                </el-table-column>
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

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/log/error/page",
                    getDataListIsPage: true,
                    exportURL: "/admin-api/log/error/export",
                },
                dataForm: {
                    module: "",
                },
                breaddata: ["网站管理", "日志", "异常日志"],
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
            // 异常信息
            infoHandle(info) {
                this.$alert(info, this.$t("logError.errorInfo"), {
                    customClass: "mod-sys__log-error-view-info",
                });
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm = {
                    module: "",
                };
                this.getData();
            },
        },
    };
</script>

<style lang="scss">
    .mod-sys__log-error {
        &-view-info {
            width: 80%;
        }
    }
</style>
