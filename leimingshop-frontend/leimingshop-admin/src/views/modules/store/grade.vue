<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="等级名称：">
                    <el-input
                        v-model="dataForm.sgName"
                        placeholder="等级名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>

            <el-form>
                <div class="formControlArea">
                    <el-form-item
                        style="disply: block; margin-bottom: 0px !important"
                    >
                        <el-button
                            type="primary"
                            @click="toAdd('新增商家等级')"
                            v-if="$hasPermission('sys:storegrade:save')"
                        >
                            新增等级
                        </el-button>
                        <el-button
                            type="danger"
                            plain
                            @click="deleteHandle()"
                            v-if="$hasPermission('sys:store:grade:delete')"
                        >
                            批量删除</el-button
                        >
                    </el-form-item>
                    <div style="display: flex">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>
            </el-form>
            <el-alert
                title="操作提示"
                type="warning"
                @close="$store.commit('showListMessage')"
                v-show="$store.state.listMessageFlag"
            >
                <template slot="title">
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">
                        1、商户等级用以控制不同级别商户的可发布商品数量、收费标准、佣金比例等内容。不同等级的商户，需要按照收费标准，缴纳一定的费用，以求达到相应等级的权益
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" width="70"> </el-table-column>
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
                prop="sgName"
                align="center"
                label="等级名称"
                width="180"
            >
            </el-table-column>
            <el-table-column prop="sgGoodsLimit" align="center" label="商品数">
            </el-table-column>
            <el-table-column prop="sgPrice" align="right" label="收费标准">
            </el-table-column>
            <el-table-column
                prop="brokerageScale"
                align="right"
                label="佣金比例"
            >
                <template slot-scope="scope">
                    <span
                        >{{
                            scope.row.brokerageScale
                                ? scope.row.brokerageScale
                                : "0"
                        }}%</span
                    >
                </template>
            </el-table-column>
            <el-table-column prop="showFlag" align="center" label="状态">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.showFlag == 1"
                        >启用</el-tag
                    >
                    <el-tag type="danger" v-else>禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="toAdd('编辑商家等级', scope.row.id)"
                        v-if="$hasPermission('sys:store:grade:update')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:store:grade:delete')"
                        >删除</el-button
                    >
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        >
        </el-pagination>

        <!-- 新增等级 -->
        <el-dialog
            :title="gradeTitle"
            :visible.sync="centerDialogVisible"
            :close-on-click-modal="false"
            :show-close="false"
            class="gradeDialog"
            width="32%"
        >
            <el-form
                :model="gradeDataForm"
                :rules="dataRule"
                ref="gradeDataForm"
                @keyup.enter.native="gradeSubmitHandle()"
                label-width="120px"
            >
                <el-form-item label="等级名称：" prop="sgName">
                    <el-input v-model="gradeDataForm.sgName"></el-input>
                </el-form-item>
                <el-form-item label="商品数量：" prop="sgGoodsLimit">
                    <el-input
                        v-model="gradeDataForm.sgGoodsLimit"
                        maxlength="6"
                    ></el-input>
                </el-form-item>
                <el-form-item label="收费标准：" prop="sgPrice">
                    <el-input v-model="gradeDataForm.sgPrice">
                        <template slot="append">人民币</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="佣金比例：" prop="brokerageScale">
                    <el-input
                        v-model="gradeDataForm.brokerageScale"
                        maxlength="4"
                    >
                        <template slot="append">%</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="是否启用：" prop="showFlag">
                    <el-radio-group v-model="gradeDataForm.showFlag">
                        <el-radio label="1">启用</el-radio>
                        <el-radio label="0">禁用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="noCheck">取 消</el-button>
                <el-button
                    type="primary"
                    @click="gradeSubmitHandle"
                    :loading="buttonStatus"
                    >确 定</el-button
                >
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import { storeGradePageUrl } from "@/api/url";
    import { addGrade, storeGradeNews, updatestoreGrade } from "@/api/api";
    import { showGrade } from "@/api/api";
    import { constants } from "fs";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: storeGradePageUrl,
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/store/grade",
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                breaddata: ["商家系统", "商家管理", "商家等级"],
                buttonStatus: false,
                gradeTitle: "",
                dataForm: {},
                gradeDataForm: {
                    brokerageScale: "",
                    sgGoodsLimit: "",
                    // sgGradeScore: '',
                    sgName: "",
                    sgPrice: "",
                    showFlag: "1",
                },
                gradeId: "",
                centerDialogVisible: false,
                dataRule: {
                    brokerageScale: [
                        {
                            required: true,
                            message: "佣金比例不能为空！",
                            trigger: "blur",
                        },
                        {
                            validator: function (rule, value, callback) {
                                if (!Number(value)) {
                                    callback(new Error("请输入正确的佣金比例"));
                                } else if (
                                    Number(value) > 100 ||
                                    Number(value) <= 0
                                ) {
                                    callback(new Error("佣金比例应处于0-100"));
                                } else if (
                                    JSON.stringify(value).indexOf(".") != -1
                                ) {
                                    if (
                                        JSON.stringify(value).split(".")[1] &&
                                        JSON.stringify(value).split(".")[1].length >
                                            2
                                    ) {
                                        callback(new Error("只能保留一位小数"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    sgGoodsLimit: [
                        {
                            required: true,
                            message: "商品数量不能为空！",
                            trigger: "blur",
                        },
                        {
                            validator: function (rule, value, callback) {
                                let reg = /^[0-9]+$/;
                                if (!Number(value)) {
                                    callback(new Error("请输入正确的商品数量"));
                                } else if (
                                    Number(value) > 100000 ||
                                    Number(value) < 0
                                ) {
                                    callback(new Error("商品数量应处于0-100000"));
                                } else if (!reg.test(Number(value))) {
                                    callback(new Error("商品数量应为整数"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    //   sgGradeScore: [
                    //     { required: true, message: '佣金比例不能为空！', trigger: 'blur' }
                    //   ],
                    sgName: [
                        {
                            required: true,
                            message: "等级名称不能为空！",
                            trigger: "blur",
                        },
                    ],
                    sgPrice: [
                        {
                            required: true,
                            message: "收费标准不能为空！",
                            trigger: "blur",
                        },
                        {
                            validator: function (rule, value, callback) {
                                if (!Number(value)) {
                                    callback(new Error("请输入正确的收费标准"));
                                } else if (
                                    Number(value) > 1000000 ||
                                    Number(value) < 0
                                ) {
                                    callback(new Error("收费标准应处于0-10000000"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    showFlag: [
                        {
                            required: true,
                            message: "是否启用不能为空！",
                            trigger: "blur",
                        },
                    ],
                },
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        components: {
            Bread,
        },

        methods: {
            noCheck() {
                this.centerDialogVisible = false;
                this.$refs["gradeDataForm"].resetFields(); //校验隐藏
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.page = 1;
                this.dataForm = {};
                this.getData();
            },
            //判断新增还是修改
            toAdd(title, id) {
                this.gradeTitle = title;
                this.gradeId = "";
                this.centerDialogVisible = true;
                if (id) {
                    this.gradeId = id;
                    var obj = {
                        id: id,
                    };
                    storeGradeNews(obj).then((res) => {
                        console.log(
                            "等级回显",
                            res.data.showFlag,
                            JSON.stringify(res.data.showFlag)
                        );
                        if (res.code == 200 && res.data) {
                            Object.assign(this.gradeDataForm, res.data);
                            this.gradeDataForm.showFlag = JSON.stringify(
                                res.data.showFlag
                            );
                        }
                    });
                } else {
                    this.gradeDataForm = {
                        brokerageScale: "",
                        sgGoodsLimit: "",
                        sgName: "",
                        sgPrice: "",
                        showFlag: "1",
                    };
                }
            },
            // 新增修改提交
            gradeSubmitHandle() {
                this.$refs["gradeDataForm"].validate((valid) => {
                    if (valid) {
                        this.buttonStatus = true;
                        if (this.gradeId) {
                            this.gradeDataForm.id = this.gradeId;
                            updatestoreGrade(this.gradeDataForm).then((res) => {
                                console.log(res);
                                if (res.code == 200) {
                                    this.centerDialogVisible = false;
                                    this.getDataList();
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
                                this.buttonStatus = false;
                            });
                        } else {
                            addGrade(this.gradeDataForm).then((res) => {
                                console.log(res);
                                if (res.code == 200) {
                                    this.centerDialogVisible = false;
                                    this.getDataList();
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
                                    this.getDataList();
                                }
                                this.buttonStatus = false;
                            });
                        }
                    }
                });
            },
            changeStatus(showFlag, id) {
                console.log("状态值", showFlag);
                let obj = {
                    showFlag: showFlag ? 0 : 1,
                    id: id,
                };
                showGrade(obj).then((res) => {
                    console.log(res);
                    if (res.code == 200) {
                        this.getDataList();
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
        },
    };
</script>
<style lang="scss" scoped>
    // @import "@/element-ui/theme-variables.scss";

    // .el-input {
    //   width: 170px;
    //   height: 40px;
    // }
    .gradeDialog {
        .el-input {
            width: 250px !important;
        }
    }

    /deep/ .el-table td {
        padding: 7px 0;
    }
</style>