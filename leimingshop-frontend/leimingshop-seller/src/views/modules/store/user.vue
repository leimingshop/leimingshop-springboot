<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <div class="mod-sys__user">
                <el-form
                    :inline="true"
                    :model="dataForm"
                    class="grayLine"
                    @keyup.enter.native="getData()"
                >
                    <el-form-item label="用户名：">
                        <el-input
                            v-model="dataForm.username"
                            placeholder="用户名"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="所属角色：">
                        <el-select
                            v-model="dataForm.roleId"
                            placeholder="请选择"
                        >
                            <el-option
                                v-for="item in roleList"
                                :key="item.id"
                                :label="item.roleName"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="所属店铺：">
                        <el-select
                            v-model="dataForm.storeId"
                            placeholder="请选择"
                        >
                            <el-option
                                v-for="item in storeList"
                                :key="item.id"
                                :label="item.storeName"
                                :value="item.id"
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
                    <div>
                        <el-form>
                            <el-form-item>
                                <el-button
                                    type="primary"
                                    @click="addOrUpdateHandle()"
                                    v-if="$hasPermission('sys:user:save')"
                                    >{{ $t("add") }}</el-button
                                >
                                <el-button
                                    type="danger"
                                    plain
                                    @click="deleteHandle()"
                                    v-if="$hasPermission('sys:user:delete')"
                                    >批量{{ $t("deleteBatch") }}</el-button
                                >
                                <!-- <el-button type="primary" plain @click="exportHandle()">{{ $t('export') }}</el-button> -->
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
                            1、默认的超级管理员账户不可操作和删除，该账户为商户端最大权限账户
                        </div>
                        <div class="iconSize">
                            2、如新增管理员需先配置角色管理的功能权限
                        </div>
                        <div class="iconSize">
                            3、新增的用户账户可进行启禁用、修改信息、删除账户、重置密码操作
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
                width="40"
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
                prop="account"
                :label="$t('user.username')"
                header-align="center"
                align="center"
                width="130px"
            ></el-table-column>
            <el-table-column
                prop="nickname"
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
                prop="storeName"
                label="所属店铺"
                header-align="center"
                align="center"
            ></el-table-column>
            <!-- <el-table-column prop="email" :label="$t('user.email')" header-align="center" align="center" width="180px"></el-table-column> -->
            <el-table-column
                prop="mobilePhone"
                :label="$t('user.mobile')"
                width="120px"
                header-align="center"
                align="center"
            ></el-table-column>
            <!-- <el-table-column prop="isEnable" :label="$t('user.status')" header-align="center" align="center" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isEnable === 1" size="small" type="danger">{{ $t('user.status0') }}</el-tag>
            <el-tag v-else size="small" type="success">{{ $t('user.status1') }}</el-tag>
          </template>
        </el-table-column> -->
            <!-- <el-table-column prop="createDate" :label="$t('user.createDate')" header-align="center" align="center" width="180">
        </el-table-column> -->
            <el-table-column
                prop="createDate"
                label="创建时间"
                header-align="center"
                width="160px"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="isEnable"
                label="状态"
                header-align="center"
                align="center"
                width="80"
            >
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.isEnable == 0"
                        >启用</el-tag
                    >
                    <el-tag type="danger" v-else>禁用</el-tag>
                </template>
            </el-table-column>

            <el-table-column
                :label="$t('handle')"
                width="220px"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope" v-if="scope.row.roleMark !== 1">
                    <el-button
                        type="text"
                        size="small"
                        @click="updateIsEnable(scope.row)"
                    >
                        <span
                            style="color: limegreen"
                            size="small"
                            v-if="scope.row.isEnable == 1"
                            >启用</span
                        >
                        <span type="text" class="artdanger" size="small" v-else
                            >禁用</span
                        >
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrUpdateHandle(scope.row.id)"
                        v-if="$hasPermission('sys:user:update')"
                        >{{ $t("update") }}</el-button
                    >
                    <el-button
                        type="text"
                        class="artdanger"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:user:delete')"
                        >{{ $t("delete") }}</el-button
                    >
                    <el-button
                        type="text"
                        class="artdanger"
                        size="small"
                        @click="resetPassword(scope.row.id, scope.row.account)"
                        v-if="$hasPermission('sys:user:reset:password')"
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
    import AddOrUpdate from "./user-add-or-update";
    import { resetPassword, isEnable } from "@/api/api";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/user/page",
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/user",
                    deleteIsBatch: true,
                    exportURL: "/seller-api/user/export",
                },
                roleList: [],
                storeList: [],
                breaddata: ["店铺管理", "管理员管理"],
                dataForm: {
                    roleId: "",
                    storeId: "",
                },
                tableheight: document.body.offsetHeight - 400,
                tableheightBig: 300,
            };
        },
        components: {
            AddOrUpdate,
            Bread,
        },
        created() {
            this.getRoleList();
            this.getStoreList();
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    console.log(
                        "12345",
                        document.body.clientHeight,
                        document.getElementById("control-area").clientHeight
                    );
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },

        methods: {
            // 获取角色列表
            getRoleList() {
                let obj = {
                    params: {
                        page: 1,
                        limit: 1000,
                        roleMark: 0,
                    },
                };
                return this.$http
                    .get("/seller-api/role/page", obj)
                    .then(({ data: res }) => {
                        if (res.code == 200) {
                            this.roleList = res.data.list;
                        } else {
                            return this.$message.error(res.msg);
                        }
                    })
                    .catch(() => {});
            },
            // 获取店铺列表
            getStoreList() {
                return this.$http
                    .get("/seller-api/setting/user/all")
                    .then(({ data: res }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        this.storeList = res.data;
                    })
                    .catch(() => {});
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
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm = {};
                this.getData();
            },
            //启用禁用
            updateIsEnable(row) {
                var params = {
                    id: row.id,
                };

                if (row.isEnable == 1) {
                    params.type = 0;
                } else {
                    params.type = 1;
                }
                isEnable(params).then((res) => {
                    if (res.code == 200) {
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500,
                        });

                        this.getDataList();
                    }
                });
            },
        },
    };
</script>
<style scoped>
    .formControlArea {
        height: 70px;
    }
</style>