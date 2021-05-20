<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" ref="dataForm"
                @keyup.enter.native="getData()">
                <el-form-item label="等级名称：" class="aa" prop="gradeName">
                    <el-input v-model="dataForm.gradeName" placeholder="等级名称" show-word-limit maxlength=20 clearable>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getData()">查询</el-button>
                    <el-button @click="reset()" type="primary" plain>重置</el-button>
                </el-form-item>
                <br>
            </el-form>
            <el-form>
                <div class="formControlArea">
                    <el-form-item style="display:block;margin-bottom:0px !important;">
                        <el-button type="primary" @click="deal" v-if="$hasPermission('sys:membergrade:save')">新增等级
                        </el-button>
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
                    <div class="iconSize">1、最低级会员不可编辑等级分值，不可删除</div>
                </template>
            </el-alert>
        </div>
        <el-table width="100%" :data="dataList" border="" :height="!$store.state.mainSwitch ? tableheight:tableheightBig"
            :row-style="iRowStyle" :cell-style="iCellStyle" :header-row-style="iHeaderRowStyle"
            :header-cell-style="iHeaderCellStyle">
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
            </el-table-column>
            <el-table-column prop="gradeName" align="center" label="等级名称"></el-table-column>
            <el-table-column prop="integration" align="center" label="等级分值">
                <template slot-scope="scope">{{scope.row.integration +'分值以上'}}</template>
            </el-table-column>
            <el-table-column prop="memeberNum" align="center" label="人数"></el-table-column>
            <el-table-column label="操作" width="400" align="center">
                <template slot-scope="scope">
                    <!-- <el-button @click="goDetail(scope.row.id,false)" type="text" size="mini">查看</el-button> -->
                    <el-button @click="goDetail(scope.row,true)" type="text" size="mini"
                        v-if="$hasPermission('sys:membergrade:update')">编辑</el-button>
                    <el-button class="artdanger"
                        v-if="scope.row.integration!=0 && $hasPermission('sys:membergrade:delete')"
                        @click="deleteHandle(scope.row.id)" type="text" size="mini">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"></el-pagination>
        <!-- 查看 编辑  新增-->
        <el-dialog :title="changeTit" width="35%" :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="form">
                <el-form-item label="等级名称：" :label-width="formLabelWidth" v-if="isChange">
                    <el-input v-model="form.gradeName" autocomplete="off" style="width:75%" readonly></el-input>
                </el-form-item>
                <el-form-item label="等级名称：" :label-width="formLabelWidth" v-else prop="gradeName">
                    <el-input v-model="form.gradeName" onkeyup autocomplete="off" style="width:75%" show-word-limit
                        maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="等级分值：" :label-width="formLabelWidth" v-if="isChange">
                    <el-input v-model="form.integration" autocomplete="off" style="width:30%" readonly></el-input>--
                    <el-input v-model="form.maxIntegration" autocomplete="off" style="width:30%" readonly></el-input>
                </el-form-item>
                <el-form-item label="等级分值：" :label-width="formLabelWidth" v-else prop="integration">
                    <el-input-number v-model="form.integration" :step="1" :min="0" :max="999999"
                        :disabled="disableStatus"></el-input-number>
                    <span class="tip">不小于该等级分值!</span>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" v-if="isChange">
                <el-button plain @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            </div>
            <div slot="footer" class="dialog-footer" v-else>
                <el-button plain @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="changeScore">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import {
        memberGrade,
        gradeDel
    } from "@/api/url";
    import {
        greadDet,
        changeGrade,
        addSavaGrade
    } from "@/api/api";
    import Bread from "@/components/bread";
    export default {
        mixins: [mixinViewModule],
        data() {
            let nameRule = (rule, value, callback) => {
                if (!value.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")) {
                    callback(new Error("不得输入特殊符号"));
                } else {
                    callback();
                }
            };
            return {
                mixinViewModuleOptions: {
                    getDataListURL: memberGrade,
                    getDataListIsPage: true,
                    deleteURL: gradeDel,
                    deleteIsBatch: false,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "ids"
                },
                formLabelWidth: "120px",
                isChange: false,
                changeTit: "", //弹窗标题
                dialogFormVisible: false,
                breaddata: ["会员系统", "会员管理", "会员等级"],
                dataForm: {},
                form: {
                    gradeName: "",
                    integration: 0
                },
                rules: {
                    gradeName: [{
                            required: true,
                            message: "请输入会员名称",
                            trigger: "blur"
                        },
                        {
                            validator: nameRule,
                            trigger: "blur"
                        }
                    ],
                    integration: [{
                        required: true,
                        message: "请选择等级分值",
                        trigger: "change"
                    }]
                },
                disableStatus: false, //是否禁用等级分值
                tableheight: document.body.offsetHeight - 420,
                tableheightBig:0
            };
        },
        mounted() {},
        created() {},
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        95;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
        },
        methods: {
            iRowStyle: function ({
                row,
                rowIndex
            }) {
                return 'height:47px';
            },
            iHeaderRowStyle: function ({
                row,
                rowIndex
            }) {
                return 'height:45px';
            },
            iCellStyle: function ({
                row,
                column,
                rowIndex,
                columnIndex
            }) {
                return 'padding:1px'
            },
            iHeaderCellStyle: function ({
                row,
                column,
                rowIndex,
                columnIndex
            }) {
                return 'padding:10px'
            },
            goDetail(row, states) {
                if (row.integration === 0) { // 不可删除的会员等级不可修改等级分值
                    this.disableStatus = true
                } else {
                    this.disableStatus = false
                }
                this.form.id = row.id;
                if (states) {
                    this.isChange = false;
                    this.changeTit = "会员编辑";
                } else {
                    this.isChange = true;
                    this.changeTit = "会员详情";
                }
                const obj = {
                    id: row.id
                };
                greadDet(obj).then(res => {
                    if (res.code == 200) {
                        this.dialogFormVisible = !this.dialogFormVisible;
                        this.form = res.data;
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1000
                        });
                    }
                });
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.dataForm.gradeName = "";
                this.getData();
            },
            //修改等级
            changeScore() {
                const obj = {
                    id: this.form.id,
                    gradeName: this.form.gradeName,
                    integration: this.form.integration
                };
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.form.id == "") {
                            console.log("kong");
                            addSavaGrade(obj).then(res => {
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: "success",
                                        duration: 1000
                                    });
                                    this.dialogFormVisible = false;
                                    this.getDataList();
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "error",
                                        duration: 1000
                                    });
                                    this.dialogFormVisible = false;
                                }
                            });
                        } else {
                            changeGrade(obj).then(res => {
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: "success",
                                        duration: 1000
                                    });
                                    this.dialogFormVisible = false;
                                    this.getDataList();
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "error",
                                        duration: 1000
                                    });
                                    this.dialogFormVisible = false;
                                }
                            });
                        }
                    } else {
                        console.log("error submit!!");
                        return false;
                    }
                });
            },
            //新增等级
            deal() {
                this.isChange = false;
                this.form.id = "";
                this.form.id = "";
                this.form.gradeName = "";
                this.form.integration = 0;
                this.disableStatus = false;
                this.changeTit = "新增会员";
                this.dialogFormVisible = true;
            }
        },
        components: {
            Bread
        }
    };
</script>
<style lang="scss" scoped>
    .tip {
        margin-left: 15px;
        display: inline-block;
    }

    /deep/.el-input__inner {
        padding-right: 64px;
    }

    .aa {
        /deep/.el-input {
            width: 207px !important;
        }
    }
</style>