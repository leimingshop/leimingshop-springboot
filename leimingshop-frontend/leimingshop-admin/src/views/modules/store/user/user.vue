<template>
    <div>
        <div id="control-area">
            <el-dialog width="30%" title="密码重置确认" :visible.sync="innerVisible" class="restPass" append-to-body>
                <el-form label-width="100px" :rules="rules" ref="form" :model="form">
                    <h3 style="text-align: center;">您确定重置用户[{{form.username}}]的密码吗？</h3>

                    <p style="width:100%;text-align: center;">提示：确认后，新密码将会以短信形式发送到商户手机【{{form.phone}}】</p>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button plain @click="closeInner">取 消</el-button>
                    <el-button type="primary" @click="sendMsg">确认</el-button>
                </div>
            </el-dialog>
            <Bread :breaddata="breaddata"></Bread>

            <div class="mod-sys__user">
                <el-form :inline="true" :model="dataForm" class="grayLine" @keyup.enter.native="getData()">
                    <el-form-item label="登录账号：">
                        <el-input v-model="dataForm.username" placeholder="登录账号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="手机号：">
                        <el-input v-model="dataForm.mobilePhone" placeholder="手机号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱：">
                        <el-input v-model="dataForm.email" placeholder="邮箱" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="状态：">
                        <el-select v-model="dataForm.isEnable" placeholder="请选择">
                            <el-option label="启用" value="0"></el-option>
                            <el-option label="禁用" value="1"></el-option>
                        </el-select>
                    </el-form-item>


                    <el-form-item>
                        <el-button @click="getData()" type="primary">{{ $t('query') }}</el-button>
                        <el-button @click="reset()" type="primary" plain>重置</el-button>
                    </el-form-item>
                    <br />
                </el-form>
                <el-form>
                    <div class="formControlArea">
                        <el-form-item style="disply:block;margin-bottom:0px !important;">
                            <el-button type="primary" @click="addOrUpdateHandle()"
                                v-if="$hasPermission('sys:user:save')">
                                {{ $t('add') }}</el-button>
                            <!-- <el-button type="danger" plain @click="deleteHandle()" v-if="$hasPermission('sys:user:delete')">批量{{ $t('deleteBatch') }}</el-button> -->
                            <!-- <el-button type="primary" plain @click="exportHandle()">{{ $t('export') }}</el-button> -->
                        </el-form-item>
                        <div style="display:flex;">
                            <mainSwitch></mainSwitch>
                            <mainTipsMessage></mainTipsMessage>
                        </div>
                    </div>
                    <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                        <template slot='title'>
                            <div class="iconSize">操作提示：</div>
                            <div class="iconSize">1、管理员列表包含已经入驻店铺的商户管理员。</div>
                            <div class="iconSize">2、只包括商户入驻和申请时初始的管理员账号，不包含商户创建的店铺子账号。</div>
                        </template>
                    </el-alert>

                </el-form>
            </div>
            <el-table v-loading="dataListLoading" :data="dataList" border
                @selection-change="dataListSelectionChangeHandle" @sort-change="dataListSortChangeHandle"
                style="width: 100%;" :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
                <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
                <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                    <template slot-scope="scope">
                        {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                    </template>
                </el-table-column>
                <el-table-column prop="account" :label="$t('user.username')" header-align="center" align="center"
                    width="130px"></el-table-column>
                <el-table-column prop="nickname" label="姓名" header-align="center" align="center" width="130px">
                </el-table-column>
                <el-table-column prop="mobilePhone" :label="$t('user.mobile')" header-align="center" width="180px"
                    align="center"></el-table-column>
                <el-table-column prop="email" :label="$t('user.email')" header-align="center" align="center">
                </el-table-column>
                <el-table-column prop="isEnable" label="状态" header-align="center" align="center" width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" v-if="scope.row.isEnable ==0">启用</el-tag>
                        <el-tag type="danger" v-else>禁用</el-tag>
                    </template>
                </el-table-column>

                <el-table-column :label="$t('handle')" header-align="center" align="center">
                    <template slot-scope="scope" v-if="scope.row.roleMark!==1">
                        <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)"
                            v-if="$hasPermission('sys:user:update')">编辑</el-button>
                        <el-button type="text" size="small" @click="updateIsEnable(scope.row)">
                            <span style="color:limegreen" size="small" v-if="scope.row.isEnable ==1">启用</span>
                            <span type="text" class="artdanger" size="small" v-else>禁用</span>
                        </el-button>
                        <el-button type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)"
                            v-if="$hasPermission('sys:user:delete')">{{ $t('delete') }}</el-button>
                        <el-button type="text" class="artdanger" size="small"
                            @click="resetPassword(scope.row.id,scope.row.account,scope.row.mobilePhone)">重置密码
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
                layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle">
            </el-pagination>
            <!-- 弹窗, 新增 / 修改 -->
            <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
        </div>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import AddOrUpdate from './user-add-or-update'
    import {
        isEnable,
        storeResetPassword
    } from '@/api/api'
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            var checkPhone = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("手机号不能为空"));
                } else {
                    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
                    console.log(reg.test(value));
                    if (reg.test(value)) {
                        callback();
                    } else {
                        return callback(new Error("请输入正确的手机号"));
                    }
                }
            };
            return {
                mixinViewModuleOptions: {
                    getDataListURL: '/admin-api/store/user/page',
                    getDataListIsPage: true,
                    deleteURL: '/admin-api/store/user',
                    deleteIsBatch: true,
                    exportURL: '/admin-api/user/export'
                },
                roleList: [],
                innerVisible: false,
                form: {},
                rules: {
                    phone: [{
                        validator: checkPhone,
                        trigger: "blur"
                    }]
                },
                storeList: [],
                breaddata: ["商户管理", "管理员管理"],
                dataForm: {
                    roleId: "",
                    storeId: ""
                },
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 0
            }
        },
        components: {
            AddOrUpdate,
            Bread
        },
        created() {


        },
        watch: {
            '$store.state.mainSwitch'() { //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100)
            }
        },
        methods: {
            resetPassword(id, username, mobilePhone) {
                this.innerVisible = true
                this.form.username = username
                this.form.id = id
                this.form.phone = mobilePhone.replace(mobilePhone.substring(3, 7), "****")
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            closeInner() {
                this.innerVisible = false;
                this.form.phone = "";
            },
            sendMsg() {
                this.forbitLoading = true;
                var obj = {
                    id: this.form.id
                }
                storeResetPassword(obj).then((res) => {
                    this.forbitLoading = false;
                    let status = "error"
                    if (res.code == 200) {
                        this.innerVisible = false;
                        this.getDataList();
                        status = "success"

                    }
                    this.$message({
                        message: res.msg,
                        type: status,
                        duration: 1500,
                    })
                })

            },
            reset() {
                this.dataForm = {};
                this.getData();
            },
            //启用禁用
            updateIsEnable(row) {
                var params = {
                    id: row.id,
                }

                if (row.isEnable == 1) {
                    params.type = 0
                } else {
                    params.type = 1
                }
                isEnable(params).then((res) => {

                    if (res.code == 200) {

                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500
                        })

                        this.getDataList();
                    }
                })

            }
        }
    }
</script>
<style lang="scss" scoped>
    /deep/ .el-table td {
        padding: 7px 0
    }
</style>