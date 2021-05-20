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
        <div class="mod-sys__log-login">
            <div id="control-area">
                <el-form
                    :inline="true"
                    :model="dataForm"
                    class="grayLine"
                    @keyup.enter.native="getData()"
                >
                    <el-form-item label="用户名：">
                        <el-input
                            v-model="dataForm.creator"
                            :placeholder="$t('logLogin.creatorName')"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="状态：">
                        <el-select
                            v-model="dataForm.status"
                            :placeholder="$t('logLogin.status')"
                            clearable
                        >
                            <el-option
                                :label="$t('logLogin.status0')"
                                :value="0"
                            ></el-option>
                            <el-option
                                :label="$t('logLogin.status1')"
                                :value="1"
                            ></el-option>
                            <el-option
                                :label="$t('logLogin.status2')"
                                :value="2"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="getData()">{{
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
                            1、查看管理员的登录日志，系统对管理员的关键操作进行了记录
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
                    :label="$t('logLogin.creatorName')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="operation"
                    :label="$t('logLogin.operation')"
                    header-align="center"
                    align="center"
                    width="100"
                >
                    <template slot-scope="scope">
                        {{
                            scope.row.operation === 0
                                ? $t("logLogin.operation0")
                                : $t("logLogin.operation1")
                        }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="status"
                    :label="$t('logLogin.status')"
                    sortable="custom"
                    header-align="center"
                    align="center"
                    width="100"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.status === 0"
                            size="small"
                            type="danger"
                            >{{ $t("logLogin.status0") }}</el-tag
                        >
                        <el-tag
                            v-else-if="scope.row.status === 1"
                            size="small"
                            type="success"
                            >{{ $t("logLogin.status1") }}</el-tag
                        >
                        <el-tag v-else size="small" type="warning">{{
                            $t("logLogin.status2")
                        }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ip"
                    :label="$t('logLogin.ip')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="userAgent"
                    :label="$t('logLogin.userAgent')"
                    header-align="center"
                    align="center"
                    width="400"
                    :show-overflow-tooltip="true"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    :label="$t('logLogin.createDate')"
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

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/log/login/page",
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export'
                },
                breaddata: ["网站管理", "日志", "登录日志"],
                dataForm: {
                    creator: "",
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
                        160;
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
                    creator: "",
                    status: "",
                };
                this.getData();
            },
        },
    };
</script>
