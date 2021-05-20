<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-form
            :inline="true"
            class="grayLine topGapPadding"
            :model="dataForm"
            @keyup.enter.native="getData()"
        >
            <el-form-item label="原因内容：">
                <el-input
                    v-model="dataForm.content"
                    placeholder="原因内容"
                    clearable
                ></el-input>
            </el-form-item>
            <el-form-item label="类型：">
                <el-select v-model="dataForm.type" placeholder="请选择">
                    <el-option
                        v-for="item in typeList"
                        :key="item.id"
                        :label="item.lable"
                        :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="适用角色：">
                <el-select v-model="dataForm.role" placeholder="请选择">
                    <el-option
                        v-for="item in roleList"
                        :key="item.id"
                        :label="item.lable"
                        :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="getData()">查询</el-button>
                <el-button @click="reset()" type="primary" plain
                    >重置</el-button
                >
            </el-form-item>
            <br />
        </el-form>

        <el-form>
            <el-form-item>
                <div class="formControlArea">
                    <el-button
                        type="primary"
                        @click="addOrAdit()"
                        v-if="$hasPermission('sys:apply:reason:save')"
                        >添加原因</el-button
                    >
                    <div></div>
                    <div style="display: flex">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>
            </el-form-item>
            <el-alert
                title="操作提示"
                type="warning"
                @close="$store.commit('showListMessage')"
                v-show="$store.state.listMessageFlag"
            >
                <template slot="title">
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">
                        1、申请类型为：退货、换货、申请退款、取消订单
                    </div>
                    <div class="iconSize">2、角色类型为：会员、商品、平台</div>
                    <div class="iconSize">3、原因内容长度为1-15个字</div>
                    <div class="iconSize">
                        4、勾选申请原因类型及适用角色复选框可统一配置申请原型
                    </div>
                </template>
            </el-alert>
        </el-form>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
        >
            <!-- <el-table-column
	      type="selection"
	      width="70">
	    </el-table-column> -->
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
            <el-table-column prop="content" align="left" label="原因内容">
            </el-table-column>
            <el-table-column prop="type" align="center" label="类型">
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.type.split(',')">{{
                        item == "0"
                            ? "退货 "
                            : item == "1"
                            ? "换货 "
                            : item == "2"
                            ? "申请退款 "
                            : "取消订单 "
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="role"
                align="center"
                width="180"
                label="适用角色"
            >
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.role.split(',')">{{
                        item == "0" ? "会员 " : item == "1" ? "商家 " : "平台 "
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template
                    slot-scope="scope"
                    v-if="scope.row.forbidOperation == 0"
                >
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrAdit(scope.row.id, scope.row)"
                        v-if="$hasPermission('sys:apply:reason:edit')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:apply:reason:delete')"
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

        <!-- 图片弹框 -->
        <el-dialog
            :title="titleName"
            :visible.sync="photoVisible"
            :close-on-click-modal="false"
            :show-close="false"
            width="44%"
        >
            <el-form
                :model="dataForm1"
                :rules="dataRuleOther"
                ref="dataRuleOther"
                label-width="120px"
            >
                <el-form-item label="原因内容：" prop="content">
                    <el-input
                        type="textarea"
                        v-model="dataForm1.content"
                        maxlength="15"
                    ></el-input>
                </el-form-item>
                <el-form-item label="类型：" prop="type">
                    <el-checkbox-group v-model="dataForm1.type">
                        <el-checkbox label="3">取消订单</el-checkbox>
                        <el-checkbox label="2">申请退款</el-checkbox>
                        <el-checkbox label="1">换货</el-checkbox>
                        <el-checkbox label="0">退货</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="适用角色：" prop="role">
                    <el-checkbox-group v-model="dataForm1.role">
                        <el-checkbox label="0">会员</el-checkbox>
                        <el-checkbox label="1">商家</el-checkbox>
                        <el-checkbox label="2">平台</el-checkbox>
                    </el-checkbox-group>
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
    import { addReason, editReason } from "@/api/api";
    import { constants } from "fs";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/reasondescription/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/reasondescription",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                photoVisible: false,
                titleName: "",
                breaddata: ["网站管理", "高级设置", "申请售后原因"],
                typeList: [
                    { id: "", lable: "全部" },
                    { id: "0", lable: "退货" },
                    { id: "1", lable: "换货" },
                    { id: "2", lable: "申请退款" },
                    { id: "3", lable: "取消订单" },
                ],
                reasonId: "",
                roleList: [
                    { id: "", lable: "全部" },
                    { id: "0", lable: "会员" },
                    { id: "1", lable: "商家" },
                    { id: "2", lable: "平台" },
                ],
                dataForm: {
                    type: "",
                    role: "",
                },
                dataForm1: {
                    content: "",
                    type: [],
                    role: [],
                },
                tableheight: document.body.offsetHeight - 440,
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
                    content: [
                        {
                            required: true,
                            message: "原因不能为空",
                            trigger: "blur",
                        },
                        { validator: validateComfirmPassword2, trigger: "blur" },
                    ],
                    type: [
                        {
                            required: true,
                            message: "类型选择不能为空",
                            trigger: "blur",
                        },
                    ],
                    role: [
                        {
                            required: true,
                            message: "适用角色选择不能为空",
                            trigger: "blur",
                        },
                    ],
                };
            },
        },
        methods: {
            addOrAdit(id, obj) {
                console.log("id", id);
                this.photoVisible = true;
                if (id) {
                    this.reasonId = id;
                    this.titleName = "编辑申请原因";
                    // console.log(obj.role.split(','))
                    this.dataForm1.id = obj.id;
                    this.dataForm1.type = obj.type.split(",");
                    this.dataForm1.role = obj.role.split(",");
                    this.dataForm1.content = obj.content;
                } else {
                    this.reasonId = "";
                    this.dataForm1 = {
                        content: "",
                        type: [],
                        role: [],
                    };
                    this.titleName = "申请原因新增";
                }
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm.content = "";
                this.dataForm.type = "";
                this.dataForm.role = "";
                this.getData();
            },
            handleClose() {
                this.photoVisible = false;
                this.$refs["dataRuleOther"].resetFields(); //校验隐藏
            },
            startCheck() {
                this.$refs["dataRuleOther"].validate((valid) => {
                    if (valid) {
                        this.dataForm1.type = this.dataForm1.type.join(",");
                        this.dataForm1.role = this.dataForm1.role.join(",");
                        if (this.reasonId) {
                            editReason(this.dataForm1).then((res) => {
                                if (res.code == 200) {
                                    this.photoVisible = false;
                                    this.reset();
                                    this.$refs["dataRuleOther"].resetFields(); //校验隐藏
                                }
                            });
                        } else {
                            addReason(this.dataForm1).then((res) => {
                                if (res.code == 200) {
                                    this.photoVisible = false;
                                    this.reset();
                                    this.$refs["dataRuleOther"].resetFields(); //校验隐藏
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
    .el-input {
        width: 170px;
        height: 40px;
    }
    /deep/.el-table td {
        padding: 5px 0;
    }
</style>
