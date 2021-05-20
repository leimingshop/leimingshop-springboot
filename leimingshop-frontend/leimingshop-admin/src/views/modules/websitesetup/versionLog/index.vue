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
                <el-form-item label="日志标题：">
                    <el-input
                        v-model="dataForm.logTitle"
                        placeholder="请输入日志标题"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="日志类型：">
                    <el-select v-model="dataForm.logType" placeholder="请选择">
                        <el-option
                            v-for="item in typeList"
                            :key="item.id"
                            :label="item.lable"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发版日期：">
                    <el-date-picker
                        v-model="updateDateArr"
                        type="datetimerange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        align="left"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                    ></el-date-picker>
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
        </div>
        <div class="formControlArea">
            <el-form>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="addOrAdit()"
                        v-if="$hasPermission('sys:updatalog:reason:save')"
                        >新增
                    </el-button>
                    <el-button
                        v-if="$hasPermission('sys:updatalog:delete')"
                        type="danger"
                        plain
                        @click="deleteHandle()"
                        >{{ $t("deleteBatch") }}
                    </el-button>
                </el-form-item>
            </el-form>
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
                <div class="iconSize">1、版本日志为平台功能更新说明</div>
            </template>
        </el-alert>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" width="70"></el-table-column>
            <!-- <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
        <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
      </el-table-column>-->
            <el-table-column
                prop="logTitle"
                align="left"
                width="260"
                label="日志标题"
            ></el-table-column>
            <el-table-column prop="logDec" align="left" label="日志内容">
                <template slot-scope="scope">
                    <el-tooltip
                        class="item"
                        effect="dark"
                        :disabled="scope.row.logDec.length <= 68"
                        :content="scope.row.logDec"
                        placement="top-start"
                    >
                        <div slot="content" class="content-text">
                            <div>
                                {{ scope.row.logDec }}
                            </div>
                        </div>
                        <div class="towEllipsis">
                            {{ scope.row.logDec }}
                        </div>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column
                prop="logVersion"
                align="center"
                width="240"
                label="版本号"
            ></el-table-column>
            <el-table-column
                prop="type"
                align="center"
                width="120"
                label="类型"
            >
                <template slot-scope="scope">
                    <span>{{
                        scope.row.logType == "0" ? "优化迭代 " : "新增功能"
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="saveDate"
                align="center"
                width="180"
                label="发版日期"
            ></el-table-column>
            <el-table-column align="center" width="160" label="操作">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrAdit(scope.row.id, scope.row)"
                        v-if="$hasPermission('sys:updatalog:reason:edit')"
                        >编辑
                    </el-button>
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:updatalog:delete')"
                        >删除
                    </el-button>
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
        ></el-pagination>

        <!-- 图片弹框 -->
        <el-dialog
            :title="titleName"
            :visible.sync="photoVisible"
            :close-on-click-modal="false"
            :show-close="false"
            width="600px"
        >
            <el-form
                class="model-box"
                :model="modelForm"
                :rules="dataRuleOther"
                ref="dataRuleOther"
                label-width="120px"
            >
                <el-form-item label="日志标题：" prop="logTitle">
                    <el-input
                        :maxlength="maxlength20"
                        v-model="modelForm.logTitle"
                    ></el-input>
                </el-form-item>
                <el-form-item label="版本号：" prop="logVersion">
                    <el-input
                        :maxlength="maxlength20"
                        v-model="modelForm.logVersion"
                    ></el-input>
                </el-form-item>
                <el-form-item label="日志类型：" prop="logType">
                    <el-select v-model="modelForm.logType" placeholder="请选择">
                        <el-option
                            v-for="item in modeltypeList"
                            :key="item.id"
                            :label="item.lable"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发版日期：" prop="saveDate">
                    <el-date-picker
                        v-model="modelForm.saveDate"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="更新日期："
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="日志更新内容：" prop="logDec">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="modelForm.logDec"
                        :maxlength="maxlength"
                    ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="startCheck">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    // import { deleteReason } from '@/api/url'
    import { addupdatelog, editupdatelog } from "@/api/api";
    import { constants } from "fs";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                maxlength: 500,
                maxlength20: 20,
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/updatelog/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/updatelog",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                photoVisible: false,
                titleName: "",
                breaddata: ["网站管理", "高级设置", "版本日志"],
                typeList: [
                    { id: "", lable: "全部" },
                    { id: "0", lable: "迭代优化" },
                    { id: "1", lable: "新增功能" },
                ],
                modeltypeList: [
                    { id: "0", lable: "迭代优化" },
                    { id: "1", lable: "新增功能" },
                ],
                updatelogId: "",
                dataForm: {
                    logTitle: "",
                    logType: "",
                    startCreateDate: "",
                    endCreateDate: "",
                },
                updateDateArr: [],
                modelForm: {
                    logTitle: "",
                    logVersion: "",
                    logType: "",
                    logDec: "",
                    saveDate: "",
                },
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 300,
            };
        },
        components: { Bread },
        computed: {
            dataRuleOther() {
                var validateComfirmPassword2 = (rule, value, callback) => {
                    console.log(value.length);
                    if (value.length < 2) {
                        return callback(new Error("原因字数不能低于2个字"));
                    } else {
                        callback();
                    }
                };
                return {
                    logTitle: [
                        {
                            required: true,
                            message: "更新日志标题不能为空",
                            trigger: "blur",
                        },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    logVersion: [
                        {
                            required: true,
                            message: "版本号不能为空",
                            trigger: "blur",
                        },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    logType: [
                        {
                            required: true,
                            message: "更新日志类型不能为空",
                            trigger: "change",
                        },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    saveDate: [
                        {
                            required: true,
                            message: "发版日期不能为空",
                            trigger: "blur",
                        },
                    ],
                    logDec: [
                        {
                            required: true,
                            message: "日志更新内容不能为空",
                            trigger: "blur",
                        },
                    ],
                };
            },
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        180;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            addOrAdit(id, obj) {
                this.photoVisible = true;
                if (id) {
                    this.titleName = "编辑日志";
                    this.updatelogId = id;
                    this.modelForm.logTitle = obj.logTitle;
                    this.modelForm.logVersion = obj.logVersion;
                    this.modelForm.logType = obj.logType.toString();
                    this.modelForm.logDec = obj.logDec;
                    this.modelForm.saveDate = obj.saveDate;
                } else {
                    this.titleName = "新增日志";
                    this.updatelogId = "";
                    this.modelForm = {
                        logTitle: "",
                        logVersion: "",
                        logType: "",
                        logDec: "",
                        saveDate: "",
                    };
                }
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.dataForm.startCreateDate = this.updateDateArr[0];
                this.dataForm.endCreateDate = this.updateDateArr[1];
                this.getDataList();
            },
            reset() {
                this.dataForm.logTitle = "";
                this.dataForm.logType = "";
                this.dataForm.startCreateDate = "";
                this.dataForm.endCreateDate = "";
                this.updateDateArr = [];
                this.getData();
            },
            handleClose() {
                this.photoVisible = false;
                this.$refs["dataRuleOther"].resetFields(); //校验隐藏
            },
            startCheck() {
                this.$refs["dataRuleOther"].validate((valid) => {
                    if (valid) {
                        if (this.updatelogId) {
                            let prama = {};
                            Object.assign(prama, this.modelForm);
                            prama.id = this.updatelogId;
                            editupdatelog(prama).then((res) => {
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: "success",
                                    });
                                    this.photoVisible = false;
                                    this.reset();
                                    this.$refs["dataRuleOther"].resetFields(); //校验隐藏
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "warning",
                                    });
                                }
                            });
                        } else {
                            addupdatelog(this.modelForm).then((res) => {
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: "success",
                                    });
                                    this.photoVisible = false;
                                    this.reset();
                                    this.$refs["dataRuleOther"].resetFields(); //校验隐藏
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "warning",
                                    });
                                }
                            });
                        }
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .model-box {
        .el-input {
            width: 300px;
            height: 40px;
        }

        .el-select {
            width: 300px;
        }
    }

    .content-text {
        max-width: 600px;
    }
    .towEllipsis {
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .el-textarea {
        /*width: 400px;*/
    }

    .el-form-item {
        margin-bottom: 20px !important;
    }
    .formControlArea {
        height: 70px;
    }
</style>
