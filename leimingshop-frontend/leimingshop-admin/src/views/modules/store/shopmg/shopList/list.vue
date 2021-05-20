<template>
    <div>
        <el-dialog class="model-sku-data" title="店铺管理员" :close-on-click-modal="false" :visible.sync="visible"
            :before-close="closeDialog">

            <el-table width="100%" :data="this.userPage" border v-loading="dataListLoading" style="width: 100%">
                <el-table-column label="序号" width="70" align="center">
                    <template slot-scope="scope">
                        <!-- {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }} -->
                        {{scope.$index+1}}
                    </template>
                </el-table-column>

                <el-table-column label="登录名" prop="account" align="center">
                </el-table-column>

                <el-table-column label="手机号" prop="mobilePhone" align="center">
                </el-table-column>

                <el-table-column label="邮箱" prop="email" align="center">
                </el-table-column>

                <el-table-column label="角色" prop="roleName" align="center">
                </el-table-column>

            </el-table>
            <!-- 分页 -->
            <el-pagination :current-page="page1" :page-sizes="[10, 20, 50, 100]" :page-size="limit1" :total="total1"
                layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle1"
                @current-change="pageCurrentChangeHandle1">
            </el-pagination>
        </el-dialog>

        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <importAndExport style="right: 20px;top: 12px;position: absolute"
                :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
                v-if="$hasPermission('sys:store:export')"></importAndExport>

            <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getData()">
                <!-- <el-form-item label="商家ID：">
            <el-input v-model="dataForm.storeId" placeholder="商品ID" clearable></el-input>
        </el-form-item> -->
                <el-form-item label="店铺名称：">
                    <el-input v-model="dataForm.storeName" placeholder="商户名称" clearable></el-input>
                </el-form-item>
                <el-form-item label="商家账号：">
                    <el-input v-model="dataForm.userName" placeholder="商家账号" clearable></el-input>
                </el-form-item>
                <el-form-item label="店铺等级：">
                    <el-select v-model="dataForm.gradeId" placeholder="请选择">
                        <el-option v-for="item in storeGradeList" :key="item.id" :label="item.sgName" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否自营：">
                    <el-select v-model="dataForm.storeType" placeholder="请选择">
                        <el-option v-for="item in storeTypes" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="入驻时间：">
                    <!-- <el-date-picker
                v-model="value4"
                type="datetimerange"
                range-separator="-"
                start-placeholder="开始日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                end-placeholder="结束日期">
            </el-date-picker> -->
                    <el-date-picker v-model="value4" type="daterange" range-separator="至" value-format="yyyy-MM-dd"
                        start-placeholder="开始日期" end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getData()">查询</el-button>
                    <el-button @click="reset()" type="primary" plain>重置</el-button>
                </el-form-item>
                <br />

            </el-form>
            <el-form>
                <div class="formControlArea">
                    <el-form-item style="disply:block;margin-bottom:0px !important;">
                        <el-button type="primary" @click="addOrAdit()" v-if="$hasPermission('sys:store:save')">创建店铺
                        </el-button>
                        <!-- <el-button type="primary" plain @click="exportHandle()">导出</el-button> -->

                    </el-form-item>
                    <div style="display:flex;">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>
            </el-form>
            <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                <template slot='title'>
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">1、店铺列表包含已经入驻商城的店铺。</div>
                    <div class="iconSize">2、只包括商户入驻审核通过和运营人员创建的店铺，未通过入驻审核的店铺不在此列表展示</div>
                </template>
            </el-alert>
        </div>
        <el-table :data="dataList" v-loading="dataListLoading" border style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">
                    {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                </template>
            </el-table-column>
            <!-- <el-table-column
	      type="selection"
	      width="70">
	    </el-table-column> -->

            <el-table-column prop="storeName" width="200" label="店铺名称">
                <template slot-scope="scope">
                    <div style="display:flex;">
                        <span style="width: 40px; height: 40px;margin-right:20px;">
                            <img v-if="scope.row.storeLogo" :src="$imgDomain + scope.row.storeLogo" alt="img"
                                style=" object-fit: contain;width: 40px;height:40px;border-radius:50%;">
                        </span>
                        <div class="wordswarp" style="display:flex;align-items: center">{{scope.row.storeName}}</div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="account" align="center" width="180" label="商户账号">
            </el-table-column>
            <el-table-column prop="gradeName" align="center" label="店铺等级">
            </el-table-column>
            <el-table-column prop="isEnable" align="center" label="店铺状态">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.isEnable ==0">启用</el-tag>
                    <el-tag type="danger" v-else>禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createDate" align="center" width="180" label="入驻时间">
            </el-table-column>
            <el-table-column prop="creator" align="center" label="是否自营">
                <template slot-scope="scope">
                    <span>{{scope.row.storeType==2?'非自营':'自营'}}</span>
                </template>
            </el-table-column>
            <el-table-column width="240" align="center" label="操作">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="showDetail(scope.row)"
                        v-if="$hasPermission('sys:store:view')">查看</el-button>
                    <el-button type="text" size="small" @click="addOrAdit(scope.row.id)"
                        v-if="$hasPermission('sys:store:edit')">编辑</el-button>
                    <el-button type="text" size="small" @click="updateIsEnable(scope.row)"
                        v-if="$hasPermission('sys:store:status')">
                        <span>{{scope.row.isEnable==0?'禁用':'启用'}}</span>
                    </el-button>
                    <el-button class="artdanger" type="text" size="small" @click="deleteStore(scope.row.id)"
                        v-if="$hasPermission('sys:store:delete')">删除</el-button>
                    <el-button type="text" size="small" @click="userList(scope.row.id)"
                        v-if="$hasPermission('sys:store:pwd')">管理员</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination @size-change="pageSizeChangeHandle" @current-change="pageCurrentChangeHandle"
            :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

        <!-- 修改密码 -->
        <el-dialog title="修改密码" :visible.sync="centerDialogVisible" :close-on-click-modal="false" :show-close="false"
            class="storedialog" width="30%">
            <el-form :model="numberDataForm" :rules="dataRule" ref="numberDataForm"
                @keyup.enter.native="numberSubmitHandle()" label-width="120px">
                <el-form-item label="新密码：" prop="newPassword">
                    <el-input v-model="numberDataForm.newPassword" type="password" maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="确认密码：" prop="confirmPassword">
                    <el-input v-model="numberDataForm.confirmPassword" type="password" maxlength="20"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="noCheck">取 消</el-button>
                <el-button type="primary" @click="numberSubmitHandle" :loading="buttonStatus">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import Bread from "@/components/bread";
    import importAndExport from "@/components/import-and-export"
    import {
        storeExport
    } from '@/api/io'
    import {
        businessPageUrl
    } from '@/api/url'
    import {
        storeGrade,
        updateEnable,
        storeUserPage
    } from '@/api/api'
    import JsEncrypt from 'jsencrypt'
    import security from '@/utils/security.js'

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: businessPageUrl,
                    getDataListIsPage: true,
                    exportURL: '/admin-api/store/export',
                    deleteURL: '/admin-api/store',
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                importAndExportOptions: {
                    // importUrl:shopBrandImport,//导入接口
                    // importWord:"导入信息",
                    exportUrl: storeExport, //导出接口
                    exportWord: "导出",
                },
                buttonStatus: false,
                userPage: {

                },
                visible: false,
                page1: 1,
                limit1: 10,
                total1: 0,
                storeId: '',
                centerDialogVisible: false,
                value4: [],
                breaddata: ["商家系统", "商家管理", "商家列表"],
                dataForm: {
                    storeType: '',
                    gradeId: ''
                },
                numberDataForm: {
                    newPassword: '',
                    confirmPassword: '',
                    username: ''
                },
                storeTypes: [{
                        value: '',
                        label: '全部'
                    },
                    {
                        value: '1',
                        label: '自营'
                    },
                    {
                        value: '2',
                        label: '非自营'
                    }
                ],
                storeGradeList: [{
                    id: '',
                    sgName: '全部'
                }], //商家等级列表
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 0
            }
        },
        components: {
            Bread,
            importAndExport
        },
        computed: {
            dataRule() {
                var validateComfirmPassword = (rule, value, callback) => {
                    console.log(this.numberDataForm.newPassword, value)
                    if (this.numberDataForm.newPassword !== value) {
                        return callback(new Error(this.$t('updatePassword.validate.comfirmPassword')))
                    }
                    callback()
                }
                return {
                    newPassword: [{
                        required: true,
                        message: this.$t('validate.required'),
                        trigger: 'blur'
                    }],
                    confirmPassword: [{
                            required: true,
                            message: this.$t('validate.required'),
                            trigger: 'blur'
                        },
                        {
                            validator: validateComfirmPassword,
                            trigger: 'blur'
                        }
                    ]
                }
            }
        },
        watch: {
            value4(newval, oldval) {
                console.log(newval, oldval)
                if (newval) {
                    this.dataForm.startTime = newval[0];
                    this.dataForm.endTime = newval[1];
                } else {
                    this.dataForm.startTime = '';
                    this.dataForm.endTime = '';
                }
            },
            '$store.state.mainSwitch'() { //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100)
            }
        },
        created() {
            let obj = {
                params: {
                    page: 1,
                    limit: 100,
                }
            }
            storeGrade(obj).then((res) => {
                console.log('商家等级', res)
                if (res.code == 200 && res.data.list) {
                    this.storeGradeList = [...this.storeGradeList, ...res.data.list]
                }
            })
        },
        methods: {
            showDetail(row) {
                this.$emit("showDetail", row);
            },
            addOrAdit(id) {
                this.$emit("addOrAdit", id);
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.value4 = [];
                this.dataForm = {
                    storeType: '',
                    gradeId: ''
                };
                this.getData();
            },
            // 删除店铺哦
            deleteStore(id) {
                var obj = { //request.js
                    data: []
                }
                obj.data.push(id)
                this.$confirm('店铺删除后，商户账号需要重新申请开店且所有店铺的子账号将无法登录系统，请谨慎操作！', '确认删除该店铺', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.delete('/admin-api/store', obj).then(({
                        data: res
                    }) => {
                        if (res.code == 200) {
                            this.$message({
                                type: 'success',
                                message: '删除成功'
                            });
                            this.getData();
                        }
                    })
                })
            },
            changeNumber(account) {
                this.centerDialogVisible = true;
                this.numberDataForm.username = account;
            },
            numberSubmitHandle() {
                this.$refs['numberDataForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }

                    // 增加用户名与密码 RSA加密
                    let publicKey = security.publicKey;
                    var encrypt = new JSEncrypt();
                    encrypt.setPublicKey(publicKey);
                    var obj = {};
                    Object.assign(obj, this.numberDataForm)

                    obj.newPassword = encrypt.encrypt(this.numberDataForm.newPassword);
                    obj.confirmPassword = encrypt.encrypt(this.numberDataForm.confirmPassword);

                    this.$http.put('/admin-api/store/update/password', obj).then(({
                        data: res
                    }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg)
                        }
                        this.$message({
                            message: this.$t('prompt.success'),
                            type: 'success',
                            duration: 500,
                            onClose: () => {
                                this.centerDialogVisible = false
                            }
                        })
                    })
                })
            },
            updateIsEnable(row) {
                var obj = {
                    isEnable: row.isEnable == 0 ? 1 : 0,
                    storeId: row.id
                };
                updateEnable(obj).then((res) => {
                    if (res.code !== 200) {
                        return this.$message.error(res.msg)
                    }
                    this.$message({
                        message: this.$t('prompt.success'),
                        type: 'success',
                        duration: 500,
                        onClose: () => {
                            this.centerDialogVisible = false
                        }
                    })
                    this.getData();
                })
            },
            noCheck() {
                this.centerDialogVisible = false
                this.$refs['numberDataForm'].resetFields(); //校验隐藏
            },
            closeDialog() {
                this.visible = false;
            },
            userList(id, page, limit) {

                this.storeId = id
                var obj = {
                    params: {
                        storeId: id,
                        page: page,
                        limit: limit
                    }

                }
                storeUserPage(obj).then(res => {
                    if (res.code == 200) {
                        this.userPage = res.data.list
                        this.total1 = res.data.total
                        console.log(this.userPage)
                        this.visible = true
                    }
                })

            },
            // 分页, 每页条数
            pageSizeChangeHandle1(val) {
                this.page1 = 1
                this.limit1 = val
                this.userList(this.storeId, this.page1, this.limit1)
            },
            // 分页, 当前页
            pageCurrentChangeHandle1(val) {
                this.page1 = val
                this.userList(this.storeId, this.page1, this.limit1)
            },
        }
    };
</script>
<style lang="scss">
    .storedialog {
        .el-input {
            // width: 280px;
            // align-items: center

            // justify-content: center
        }
    }

    .wordswarp {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        line-clamp: 1;
        -webkit-box-orient: vertical;
    }
</style>