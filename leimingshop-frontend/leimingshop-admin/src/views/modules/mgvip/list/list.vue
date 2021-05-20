<template>
    <div v-if="isGradelist" class="gradeListCustomClass">
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <!--导出按钮-->
            <!-- <importAndExport style="right: 20px;top: 12px;position: absolute"
            :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
            v-if="$hasPermission('sys:member:export')"></importAndExport> -->
            <el-button
                style="
                    right: 20px;
                    top: 12px;
                    position: absolute;
                    padding: 9px 20px;
                "
                v-if="
                    importAndExportOptions && importAndExportOptions.exportUrl
                "
                class="btn"
                @click="exportExcel"
            >
                <img
                    :src="importAndExportOptions.exportSrc"
                    style="width: 20px; height: 20px"
                    alt=""
            /></el-button>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                ref="dataForm"
                @keyup.enter.native="getData()"
            >
                <!-- <el-form-item label="会员ID：" prop="id">
        <el-input v-model="dataForm.id" placeholder="会员ID" clearable></el-input>
      </el-form-item> -->
                <el-form-item label="会员名称：" prop="memberName">
                    <el-input
                        v-model="dataForm.memberName"
                        placeholder="会员名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="会员等级：" prop="gradeId">
                    <el-select
                        v-model="dataForm.gradeId"
                        placeholder="会员等级"
                        clearable
                    >
                        <el-option label="全部" value=""></el-option>
                        <el-option
                            v-for="(item, index) in gradeName"
                            :key="index"
                            :label="item.gradeName"
                            :value="item.id"
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
            </el-form>
            <el-form>
                <!-- batchChangeBalance() batchInnerVisible=true-->
                <div class="formControlArea">
                    <el-form-item
                        style="display: block; margin-bottom: 0px !important"
                    >
                        <el-button type="primary" @click="batchChangeBalance"
                            >批量调整余额</el-button
                        >
                    </el-form-item>
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
                            1、点击编辑可重置指定会员的登录密码，密码回已短信的形式发送到注册账号所用的手机号
                        </div>
                        <div class="iconSize">
                            2、异常或违规的会员可通过禁用使该会员无法登录账号进行操作
                        </div>
                        <div class="iconSize">
                            3、登录日志可查看指定会员的登录时间、登录设备及登录状态等信息
                        </div>
                        <div class="iconSize">
                            4、勾选复选框的会员可批量进行余额调整
                        </div>
                    </template>
                </el-alert>
            </el-form>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="mixinViewModuleOptions.dataListLoading"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="selection"
                label="全选"
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
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>
            <el-table-column
                prop="memberName"
                label="会员名称"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="memberMobile"
                label="手机号"
                align="center"
            ></el-table-column>
            <el-table-column prop="memberAvatar" label="头像" align="center">
                <template slot-scope="scope">
                    <img
                        :src="$imgDomain + scope.row.memberAvatar"
                        alt=""
                        style="width: 50px; height: 50px; border-radius: 100%"
                    />
                </template>
            </el-table-column>
            <el-table-column
                prop="availableBalance"
                label="余额"
                align="center"
            >
                <template slot-scope="scope">
                    <!--<sapn :memberSourceType=memberSourceType>{{scope.row.memberSource}}</sapn>-->
                    <span>{{ "￥" + scope.row.availableBalance }}</span>
                </template>
            </el-table-column>
            <!--<el-table-column prop="memberMobile" align="center" label="会员手机号" width="140"></el-table-column>-->
            <el-table-column
                prop="gradeName"
                align="center"
                label="会员等级"
                width="140"
            ></el-table-column>
            <el-table-column
                prop="memberSource"
                align="center"
                width="140"
                label="会员来源"
            >
                <template slot-scope="scope">
                    <!--<sapn :memberSourceType=memberSourceType>{{scope.row.memberSource}}</sapn>-->
                    <!-- 用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序） -->
                    <span v-if="scope.row.memberSource == 0">网站注册</span>
                    <span v-if="scope.row.memberSource == 1">手机注册</span>
                    <span v-if="scope.row.memberSource == 2">Android</span>
                    <span v-if="scope.row.memberSource == 3">IOS</span>
                    <span v-if="scope.row.memberSource == 4">微信</span>
                    <span v-if="scope.row.memberSource == 5">小程序</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="createDate"
                align="center"
                label="注册时间"
                width="170"
            ></el-table-column>
            <el-table-column
                prop="memberState"
                align="center"
                label="状态"
                width="80"
                :formatter="stateRules"
            >
                <!-- <template slot-scope="scope">
          <span v-if="scope.row.groupStatus == 0">启用</span>
          <span v-else>禁止</span>
        </template>-->
            </el-table-column>
            <el-table-column label="操作" width="300" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="goDetail(scope.row)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:member:view')"
                        >查看</el-button
                    >
                    <el-button
                        @click.native.prevent="addHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:member:update')"
                        >编辑</el-button
                    >
                    <el-button
                        @click.native.prevent="forbitHandle(scope.row)"
                        v-if="
                            scope.row.memberState == 0 &&
                            $hasPermission('sys:member:status')
                        "
                        :style="{ color: '#e6a23c' }"
                        type="text"
                        size="mini"
                        >禁用</el-button
                    >
                    <el-button
                        @click.native.prevent="forbitHandle(scope.row)"
                        type="text"
                        class="artstart"
                        size="mini"
                        v-if="
                            scope.row.memberState == 1 &&
                            $hasPermission('sys:member:status')
                        "
                        >启用</el-button
                    >
                    <el-button
                        @click.native.prevent="loginLogHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:member:log')"
                        >登录日志</el-button
                    >
                    <!-- @click.native.prevent="changeBalance(scope.row.id)" -->
                    <el-button
                        @click="changeBalance(scope.row)"
                        type="text"
                        size="mini"
                        >调整余额</el-button
                    >
                </template>
                <!-- v-if="$hasPermission('sys:member:log')" -->
            </el-table-column>
        </el-table>
        <el-dialog
            width="600px"
            title="会员余额设置"
            :before-close="closeInner"
            :visible.sync="innerVisible"
            append-to-body
        >
            <el-form label-width="150px" ref="form" :model="balanceDataForm">
                <el-form-item label="会员名称：">
                    <span>{{ memberBalanceInfo.memberName }}</span>
                </el-form-item>
                <el-form-item label="当前余额：">
                    <span>{{ memberBalanceInfo.availableBalance }}</span>
                </el-form-item>
                <el-form-item label="增加或减少余额：">
                    <el-input-number
                        :max="1000000.0"
                        :min="-1000000.0"
                        :precision="2"
                        v-model="balanceDataForm.money"
                        :step="1"
                    ></el-input-number>
                    <br />
                    <div
                        style="
                            font-size: 12px;
                            white-space: nowrap;
                            color: rgb(96 98 102 / 64%);
                            margin-top: -8px;
                        "
                    >
                        增加仅需输入余额，减少需要在数字前加符号（-），仅可输入数字
                    </div>
                </el-form-item>
                <el-form-item label="备注：">
                    <el-input
                        style="width: 70%"
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="balanceDataForm.content"
                        maxlength="20"
                        show-word-limit
                    >
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="singlefooter">
                <el-button plain @click="closeInner">取 消</el-button>
                <el-button type="primary" @click="submitBalance(1)"
                    >确认</el-button
                >
            </div>
        </el-dialog>

        <el-dialog
            width="600px"
            title="批量余额设置"
            :before-close="closeInner"
            :visible.sync="batchInnerVisible"
            append-to-body
        >
            <el-form
                label-width="150px"
                ref="form"
                :model="batchChangeBalanceData"
            >
                <el-form-item>
                    <el-radio
                        v-model="batchChangeBalanceData.changeType"
                        :label="0"
                        class="addOrReduce"
                    >
                        增加或减少余额：
                        <el-input-number
                            :max="1000000.0"
                            :min="-1000000.0"
                            :precision="2"
                            v-model="addOrReduce"
                            :step="1"
                        >
                        </el-input-number>
                    </el-radio>
                    <!-- style="" -->
                    <div
                        style="
                            width: 60%;
                            white-space: nowrap;
                            white-space: nowrap;
                            color: rgb(96 98 102 / 64%);
                            margin-top: -8px;
                        "
                    >
                        {{
                            "增加仅需输入余额，减少需要在数字前加符号（-），仅可输入数字"
                        }}
                    </div>
                    <el-radio
                        v-model="batchChangeBalanceData.changeType"
                        :label="1"
                        class="uniteChange"
                    >
                        统一调整到：
                        <el-input-number
                            :max="1000000.0"
                            :min="0"
                            :precision="2"
                            v-model="uniteChange"
                            :step="1"
                        >
                        </el-input-number>
                    </el-radio>
                    <div
                        style="
                            font-size: 12px;
                            white-space: nowrap;
                            color: rgb(96 98 102 / 64%);
                            margin-top: -8px;
                        "
                    >
                        {{ "设置统一调整后，增加或减少余额设置将无效" }}
                    </div>
                </el-form-item>
                <!-- <el-form-item label=""> -->
                <div style="display: flex">
                    <div
                        style="
                            color: #606266 !important;
                            margin-top: 15px;
                            margin-left: 104px;
                        "
                    >
                        备注：
                    </div>
                    <el-input
                        style="width: 40%"
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="batchChangeBalanceData.remark"
                        maxlength="20"
                        show-word-limit
                    >
                    </el-input>
                </div>
                <!-- </el-form-item> -->
            </el-form>
            <div slot="footer" class="batchFooter">
                <el-button plain @click="closeInner">取 消</el-button>
                <el-button type="primary" @click="submitBalance(0)"
                    >确认</el-button
                >
            </div>
        </el-dialog>
        <el-pagination
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
        ></el-pagination>
        <!-- 编辑 -->
        <editHandle
            ref="memberEdit"
            v-if="editMember"
            @changeWindow="changeWindow"
            :memberInfo="memberInfo"
            :memberInfoSourse="memberInfoSourse"
        ></editHandle>

        <!-- 批量更换等级 -->
        <!-- <el-dialog title="批量更换会员等级" :visible.sync="dialogVisible">
      <el-form :model="form">
        <p>将所选中会员批量转移到下面的会员等级下</p>
        <el-form-item label="会员类别：" :label-width="formLabelWidth">
          <el-select v-model="form.region" placeholder="请选择活动区域">
            <el-option label="普通会员" value="0"></el-option>
            <el-option label="黄金会员" value="1"></el-option>
            <el-option label="铂金会员" value="2"></el-option>
            <el-option label="白银会员" value="3"></el-option>
            <el-option label="钻石会员" value="4"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>-->
    </div>
    <gradeDet
        v-else
        ref="membershow"
        :memberInfo="memberInfo"
        :memberId="memberId"
        :memberOrders="memberOrders"
        @changeState="changeState"
    ></gradeDet>
</template>

<script>
    import importAndExport from "@/components/import-and-export";
    import { memberExport } from "@/api/io";
    import mixinViewModule from "@/mixins/view-module";
    import { memberPageUrl, deleteMemberUrl, memberGrade } from "@/api/url";
    import {
        updateMembergrade,
        memberDet,
        memberOrders,
        memberList,
        memberDetTable,
        memberChangeBalance,
        memberBatchChangeBalance,
    } from "@/api/api";
    import cloneDeep from "lodash/cloneDeep";
    import Bread from "@/components/bread";
    // import changeGrade from "./model-change-grade";
    import editHandle from "./model-edit";
    import gradeDet from "./gradeDet";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                addOrReduce: 0,
                uniteChange: 0,
                batchInnerVisible: false,
                innerVisible: false,
                /*导出*/
                importAndExportOptions: {
                    exportUrl: memberExport, //导出接口
                    exportSrc: require("@/assets/img/export.png"),
                },
                mixinViewModuleOptions: {
                    getDataListURL: memberPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteMemberUrl,
                    deleteIsBatch: false,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                //   addEditDataVisible: false,
                editMember: false,
                isGradelist: true,
                isEdit: false, //编辑
                memberInfo: {}, //会员信息
                forbitLoading: false, //当前row
                currentIndex: -1, //当前index
                dataForm: {
                    // nickName: "",
                    memberName: "",
                    memberMobile: "",
                    gradeId: "",
                },
                memberBalanceInfo: {},
                balanceDataForm: {
                    money: 0,
                    content: "",
                    memberId: "",
                },
                memberInfoSourse: "", //保存源数据
                breaddata: ["会员系统", "会员管理", "会员列表"],
                gradeName: [],
                multipleSelection: [],
                batchChangeBalanceData: {
                    remark: "",
                    changeBalance: 0,
                    changeType: 0,
                    memberNameDTOList: [],
                },
                form: {
                    region: "0",
                },
                stateRules: function (row, column) {
                    return row.memberState == 1 ? (
                        <el-tag type="danger"> 禁用 </el-tag>
                    ) : (
                        <el-tag type="success">启用</el-tag>
                    );
                },
                memberOrders: {},
                formLabelWidth: "120px",
                tableheight: document.body.offsetHeight - 380,
                tableheightBig: 300,
            };
        },
        components: {
            editHandle,
            Bread,
            gradeDet,
            importAndExport,
        },
        watch: {
            "dataForm.id": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    // 删除输入的汉字
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) {
                        this.dataForm.id = newV.replace(newV[i], "");
                    }
                }
            },
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
        created() {
            this.memberGra();
        },
        methods: {
            //会员导出
            exportExcel() {
                let that = this;
                var obj = {
                    params: {
                        ...this.dataForm,
                    },
                };
                this.$http
                    .get(`${this.importAndExportOptions.exportUrl}`, obj)
                    .then((res) => {
                        that.$message({
                            message: "请到系统管理-下载管理中下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
            //会员等级
            memberGra() {
                memberList().then((res) => {
                    if (res.code == 200) {
                        this.gradeName = res.data;
                    }
                });
            },
            // 更改权限
            addOrEditHandle() {
                this.addEditDataVisible = true;
            },
            // 编辑
            addHandle(id) {
                this.editMember = true;
                this.memberId = id;
                const obj = {
                    id: id,
                };
                memberDet(obj).then((res) => {
                    if (res.code == 200 && res.data.id != "") {
                        this.memberInfo = res.data;
                        this.memberInfoSourse = cloneDeep(res.data);
                        this.memberInfo.memberPasswd = "******";
                        this.isEdit = true;
                        // this.$nextTick(() => {
                        //   this.$refs.memberEdit.init();
                        // });
                    }
                });
            },
            changeBalance(row) {
                this.innerVisible = true;
                this.memberBalanceInfo = cloneDeep(row);
            },
            closeInner() {
                this.innerVisible = false;
                this.batchInnerVisible = false;
                this.balanceDataForm.money = 0;
                this.balanceDataForm.content = "";
                this.batchChangeBalanceData.remark = "";
                this.batchChangeBalanceData.changeBalance = 0;
                this.addOrReduce = 0;
                this.uniteChange = 0;
                this.batchChangeBalanceData.changeType = 0;
            },
            //修改用户余额
            submitBalance(type) {
                let method = memberChangeBalance;
                let obj = {};
                if (type == 1) {
                    if (
                        null == this.balanceDataForm.money ||
                        this.balanceDataForm.money == 0
                    ) {
                        return this.$message({
                            message: "变更余额不能为0",
                            type: "warning",
                            duration: 1500,
                        });
                    }
                    obj = {
                        memberId: this.memberBalanceInfo.id,
                        balance: this.balanceDataForm.money,
                        remark: this.balanceDataForm.content,
                        memberName: this.memberBalanceInfo.memberName,
                    };
                } else {
                    if (
                        this.batchChangeBalanceData.changeType == 0 &&
                        this.addOrReduce == 0
                    ) {
                        return this.$message({
                            message: "变更余额不能为0",
                            type: "warning",
                            duration: 1500,
                        });
                    }
                    if (
                        this.batchChangeBalanceData.changeType == 1 &&
                        this.uniteChange < 0
                    ) {
                        return this.$message({
                            message: "变更余额不能小于0",
                            type: "warning",
                            duration: 1500,
                        });
                    }
                    obj = {
                        changeBalance:
                            this.batchChangeBalanceData.changeType == 0
                                ? this.addOrReduce
                                : this.uniteChange,
                        changeType: this.batchChangeBalanceData.changeType,
                        balanceType: 1,
                        remark: this.batchChangeBalanceData.remark,
                        memberNameDTOList: [],
                    };
                    this.multipleSelection.forEach((item, index) => {
                        var member = {
                            memberId: item.id,
                            memberName: item.memberName,
                        };
                        obj.memberNameDTOList.push(member);
                    });
                    console.log(obj);
                    method = memberBatchChangeBalance;
                }
                method(obj).then((res) => {
                    this.closeInner();
                    this.getData();
                    if (res.code == 200) {
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500,
                        });
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                        });
                    }
                });
            },
            batchChangeBalance() {
                if (this.multipleSelection.length > 0) {
                    this.batchInnerVisible = true;
                } else {
                    this.$message({
                        message: "选择用户后操作",
                        type: "error",
                        duration: 1500,
                    });
                }
            },
            //查看会员详情
            goDetail(row) {
                var id = row.id;
                const obj = {
                    id: id,
                };
                this.memberId = id;
                memberDetTable(obj).then((res) => {
                    if (res.code == 200) {
                        this.memberInfo = res.data;
                        this.isGradelist = false;
                        this.$nextTick(() => {
                            this.$refs.membershow.init(row);
                        });
                    }
                });
                memberOrders(obj).then((res) => {
                    if (res.code == 200) {
                        this.memberOrders = res.data;
                        this.isGradelist = false;
                    }
                });
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 重置
            reset() {
                this.$refs["dataForm"].resetFields();
                this.dataForm.gradeId = "";
                this.dataForm.memberName = "";
                this.dataForm.id = "";
                this.getData();
            },
            // 启用&禁用
            forbitHandle(index) {
                var msg = "";
                var title = "";
                // 默认0:正常、1:锁定
                if (index.memberState === 0) {
                    title = "禁用提示";
                    msg = "是否禁用该会员";
                } else {
                    title = "启用提示";
                    msg = "是否启用该会员";
                }
                this.$confirm(msg, title, {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        let obj = {
                            id: index.id,
                        };
                        return updateMembergrade(obj).then((res) => {
                            console.log(res);
                            if (res.code == 200) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1000,
                                });
                                setTimeout(() => {
                                    this.getDataList();
                                }, 500);
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "error",
                                    duration: 1000,
                                });
                            }
                        });

                        // this.$message({
                        //   type: 'success',
                        //   message: '删除成功!'
                        // });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消禁用",
                        });
                    });
            },
            //编辑页面触发
            changeWindow() {
                this.editMember = false;
            },
            //页面跳转
            changeState() {
                this.isGradelist = true;
                // this.$emit("showListFn");
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            //登录日志
            loginLogHandle(id) {
                this.$router.push({
                    name: "mgvip-loginlog-list",
                    query: {
                        memberId: id,
                    },
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    // .gradeListCustomClass {
    // .restPass{
    //   border-radius: 10px;
    // }
    /deep/ .el-dialog {
        border-radius: 10px !important;

        .el-form-item /deep/ .el-form-item__label {
            color: #606266 !important;
        }
    }

    .singlefooter {
        padding-bottom: 20px;
        padding-right: 45px;
        margin-top: -20px;
    }

    .batchFooter {
        margin-top: -20px;
        margin-bottom: 20px;
        margin-right: 30px;
    }

    .uniteChange {
        padding-top: 20px;
        margin-left: -28%;
    }

    .addOrReduce {
        margin-left: -35%;
    }

    /deep/ .el-textarea {
        position: relative;

        .el-input__count {
            position: absolute;
            bottom: 2px;
            right: 10px;
        }
    }

    // }
</style>