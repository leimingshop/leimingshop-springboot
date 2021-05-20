<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <div class="mod-sys__role">
                <el-form
                    :inline="true"
                    :model="dataForm"
                    class="grayLine"
                    @keyup.enter.native="getData()"
                >
                    <el-form-item label="名称：">
                        <el-input
                            v-model="dataForm.roleName"
                            :placeholder="$t('role.name')"
                            clearable
                        ></el-input>
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
                    <div>
                        <el-form>
                            <el-form-item>
                                <el-button
                                    type="primary"
                                    @click="addOrUpdateHandle()"
                                    v-if="$hasPermission('sys:role:save')"
                                    >{{ $t("add") }}</el-button
                                >
                                <el-button
                                    type="danger"
                                    plain
                                    @click="deleteHandle()"
                                    v-if="$hasPermission('sys:role:delete')"
                                    >批量删除</el-button
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
                            1、默认的超级管理员角色不可操作和删除，该角色为商户端最大权限角色
                        </div>
                        <div class="iconSize">
                            2、新增角色的功能权限配置可配置到操作按钮
                        </div>
                    </template>
                </el-alert>
            </div>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="dataListSelectionChangeHandle"
            @sort-change="dataListSortChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="selection"
                header-align="center"
                align="center"
                width="50"
            ></el-table-column>
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
                prop="roleName"
                :label="$t('role.name')"
                header-align="center"
                align="center"
                width="320"
            ></el-table-column>
            <el-table-column
                prop="roleRemark"
                :label="$t('role.remark')"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="createDate"
                :label="$t('role.createDate')"
                sortable="custom"
                header-align="center"
                align="center"
                width="180"
            ></el-table-column>
            <el-table-column
                :label="$t('handle')"
                header-align="center"
                align="center"
                width="150"
            >
                <template slot-scope="scope" v-if="scope.row.roleMark !== 1">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrUpdateHandle(scope.row.id)"
                        v-if="$hasPermission('sys:role:update')"
                        >{{ $t("update") }}</el-button
                    >
                    <el-button
                        type="text"
                        class="artdanger"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:role:delete')"
                        >{{ $t("delete") }}</el-button
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
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update
            v-if="addOrUpdateVisible"
            ref="addOrUpdate"
            @refreshDataList="getDataList"
        ></add-or-update>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./role-add-or-update";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/role/page",
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/role",
                    deleteIsBatch: true,
                },
                breaddata: ["店铺管理", "角色管理"],
                dataForm: {},
                tableheight: document.body.offsetHeight - 400,
                tableheightBig: 300,
            };
        },
        components: {
            AddOrUpdate,
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
<style scoped>
    .formControlArea {
        height: 70px;
    }
</style>