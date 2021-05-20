<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>

        <div class="mod-sys__user">
            <el-form
                :inline="true"
                :model="dataForm"
                class="grayLine"
                ref="seachDataFrom"
                :rules="dataRule"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="登录账号：" prop="username">
                    <el-input
                        v-model="dataForm.username"
                        placeholder="登录账号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="所属角色：">
                    <el-select v-model="dataForm.roleId" placeholder="请选择">
                        <el-option
                            v-for="item in roleList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        ></el-option>
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
                <div>
                    <el-form>
                        <el-form-item>
                            <el-button
                                v-if="$hasPermission('sys:user:save')"
                                type="primary"
                                @click="addOrUpdateHandle()"
                                >{{ $t("add") }}</el-button
                            >
                            <el-button
                                v-if="$hasPermission('sys:user:delete')"
                                type="danger"
                                plain
                                @click="deleteHandle()"
                                >{{ $t("deleteBatch") }}</el-button
                            >
                            <!-- <el-button v-if="$hasPermission('sys:user:export')" type="primary" plain @click="exportHandle()">{{ $t('export') }}</el-button> -->
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
                    <div class="iconSize">1、可新增或修改已有管理员信息</div>
                    <div class="iconSize">2、可删除管理员或修改管理员密码</div>
                </template>
            </el-alert>
            <el-table
                v-loading="dataListLoading"
                :data="dataList"
                border
                @selection-change="dataListSelectionChangeHandle"
                @sort-change="dataListSortChangeHandle"
                style="width: 100%"
            >
                <el-table-column
                    type="selection"
                    header-align="center"
                    align="center"
                    :selectable="selectable"
                    width="50"
                ></el-table-column>
                <el-table-column
                    type="index"
                    prop="$index"
                    label="序号"
                    align="center"
                    width="70"
                >
                    <template slot-scope="scope">{{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}</template>
                </el-table-column>
                <el-table-column
                    prop="username"
                    label="登录账号"
                    header-align="center"
                    align="center"
                    width="130px"
                ></el-table-column>
                <el-table-column
                    prop="realName"
                    label="姓名"
                    header-align="center"
                    align="center"
                    width="130px"
                ></el-table-column>
                <el-table-column
                    prop="roleName"
                    label="所属角色"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="email"
                    :label="$t('user.email')"
                    header-align="center"
                    align="center"
                    width="180px"
                ></el-table-column>
                <el-table-column
                    prop="mobile"
                    :label="$t('user.mobile')"
                    header-align="center"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="status"
                    :label="$t('user.status')"
                    header-align="center"
                    align="center"
                    width="100"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.status === 0"
                            size="small"
                            type="danger"
                            >{{ $t("user.status0") }}</el-tag
                        >
                        <el-tag v-else size="small" type="success">{{
                            $t("user.status1")
                        }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createDate"
                    :label="$t('user.createDate')"
                    header-align="center"
                    align="center"
                    width="180"
                ></el-table-column>
                <el-table-column
                    :label="$t('handle')"
                    header-align="center"
                    align="center"
                    width="180"
                >
                    <template
                        slot-scope="scope"
                        v-if="scope.row.superAdmin !== 1"
                    >
                        <el-button
                            v-if="$hasPermission('sys:user:update')"
                            type="text"
                            size="small"
                            @click="addOrUpdateHandle(scope.row.id)"
                            >{{ $t("update") }}</el-button
                        >
                        <el-button
                            v-if="$hasPermission('sys:user:delete')"
                            type="text"
                            class="artdanger"
                            size="small"
                            @click="deleteHandle(scope.row.id)"
                            >{{ $t("delete") }}</el-button
                        >
                        <el-button
                            v-if="$hasPermission('sys:user:update')"
                            type="text"
                            class="artdanger"
                            size="small"
                            @click="
                                resetPassword(scope.row.id, scope.row.username)
                            "
                            >重置密码</el-button
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
            ></el-pagination>
            <!-- 弹窗, 新增 / 修改 -->
            <add-or-update
                v-if="addOrUpdateVisible"
                ref="addOrUpdate"
                @refreshDataList="getDataList"
            ></add-or-update>
        </div>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./user-add-or-update";
    import Bread from "@/components/bread";
    import { resetPassword } from "@/api/api";

    export default {
        mixins: [mixinViewModule],
        data() {
            var validateUserName = (rule, value, callback) => {
                if (value) {
                    var myReg = /[\u4E00-\u9FA5]/;
                    if (myReg.test(value)) {
                        return callback(new Error("登录账号不能含有中文"));
                    }
                }
                callback();
            };

            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/user/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/user",
                    deleteIsBatch: true,
                    exportURL: "/admin-api/user/export",
                },
                roleList: [],
                breaddata: ["网站管理", "权限", "管理员管理"],
                dataForm: {
                    roleId: "",
                },
                dataRule: {
                    username: [{ validator: validateUserName, trigger: "blur" }],
                },
                tableheight: document.body.offsetHeight - 420,
            };
        },
        components: {
            AddOrUpdate,
            Bread,
        },
        created() {
            this.getRoleList();
        },
        methods: {
            selectable(row, index) {
                if (row.superAdmin == 1) {
                    return false;
                } else {
                    return true;
                }
            },

            // 获取角色列表
            getRoleList() {
                return this.$http
                    .get("/admin-api/role/list")
                    .then(({ data: res }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        this.roleList = res.data;
                    })
                    .catch(() => {});
            },

            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm.roleId = "";
                this.dataForm.username = "";
                this.getData();
                this.$refs["seachDataFrom"].validate((valid) => {
                    if (valid) {
                    }
                });
            },
            resetPassword(id, username) {
                this.$confirm("是否重置用户 " + username + " 的密码?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.forbitLoading = true;
                        var obj = {
                            id: id,
                        };
                        resetPassword(obj).then((res) => {
                            this.forbitLoading = false;
                            let status = "error";
                            if (res.code == 200) {
                                this.getDataList();
                                status = "success";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500,
                            });
                        });
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
        },
    };
</script>
<style lang="scss" scoped>
    /deep/.el-table td {
        padding: 7px 0;
    }
    .formControlArea {
        height: 70px;
    }
</style>