<template>
    <!--运费模板列表-->
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-form
            class="grayLine topGapPadding"
            :inline="true"
            ref="dataForm"
            :model="dataForm"
        >
            <el-form-item label="运费模板名称：">
                <el-input
                    v-model="dataForm.templateName"
                    placeholder="运费模板名称"
                    clearable
                ></el-input>
            </el-form-item>

            <el-form-item>
                <el-button class="btn" type="primary" @click="getData()"
                    >查询</el-button
                >
                <el-button
                    class="btn"
                    type="primary"
                    plain
                    @click="reset('dataForm')"
                    >重置</el-button
                >
            </el-form-item>
        </el-form>
        <!--新增按钮-->
        <div class="formControlArea">
            <div>
                <div class="addButton">
                    <el-button
                        class="btn"
                        type="primary"
                        @click="changeCompent(1, '')"
                        >新增运费模板</el-button
                    >
                </div>
            </div>
            <div style="display: flex">
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
                <div class="iconSize">1、运费模板名称不可重复</div>
                <div class="iconSize">
                    2、仅可一个模板设为默认，默认模板在商品新增时为默认选项
                </div>
                <div class="iconSize">
                    3、使用复制该运费模板，除名称不一致外，其他均一致
                </div>
                <div class="iconSize">
                    4、删除运费模板时，如删除的该模板有商品使用，则需修改运费模板，反之则可直接确认删除
                </div>
            </template>
        </el-alert>

        <!--表格-->
        <div v-loading="dataListLoading">
            <div
                style="margin-top: 10px"
                v-if="dataList && dataList.length > 0"
            >
                <div v-for="(item, index) in dataList" :key="index">
                    <el-row
                        style="
                            display: flex;
                            justify-content: center;
                            background-color: #f2f2f2;
                            margin-top: 10px;
                            border: 0;
                            height: 50px;
                        "
                    >
                        <el-col
                            :span="22"
                            :offset="1"
                            style="line-height: 50px"
                        >
                            <span style="margin-left: -70px">{{
                                item.templateName
                            }}</span>
                            <span style="margin-right: 50px; float: right"
                                >最后编辑时间：{{ item.updateDate }}</span
                            >
                            <span style="margin-right: 50px; float: right">
                                <span
                                    style="position: relative; float: right"
                                    @click="setDefaultTemplate(item)"
                                >
                                    <el-radio
                                        class="label"
                                        v-model="item.defaultFlag"
                                        :label="1"
                                        >设为默认模板
                                    </el-radio>
                                    <span
                                        style="
                                            position: absolute;
                                            top: 0;
                                            left: 0;
                                            width: 25px;
                                            height: 100%;
                                            z-index: 2;
                                            cursor: pointer;
                                        "
                                    ></span>
                                </span>
                                <span style="margin-right: 100px; float: right"
                                    >使用该模板的商品：{{ item.goodsNum }}</span
                                >
                            </span>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-table
                            width="100%"
                            :data="item.freightTemplateRuleDTOList"
                            border=""
                            :span-method="objectSpanMethod"
                            style="width: 100%; margin-top: unset"
                        >
                            <el-table-column
                                prop="areaDescription"
                                label="可配送区域"
                                align="center"
                                min-width="600"
                            ></el-table-column>
                            <el-table-column
                                prop="firstFee"
                                :label='item.templateType==0 ?"首件（个）" :"首重（kg）"'
                                align="center"
                                min-width="100"
                            ></el-table-column>
                            <el-table-column
                                prop="firstAmount"
                                label="运费（元）"
                                align="center"
                                min-width="100"
                            ></el-table-column>
                            <el-table-column
                                prop="additionalFee"
                                :label='item.templateType==0 ?"续件（个）" :"续重（kg）"'
                                align="center"
                                min-width="100"
                            ></el-table-column>
                            <el-table-column
                                prop="additionalAmount"
                                label="续费（元）"
                                align="center"
                                min-width="100"
                            ></el-table-column>
                            <el-table-column
                                prop="additionalFee"
                                label="操作"
                                align="center"
                                min-width="150"
                            >
                                <el-button
                                    size="mini"
                                    type="text"
                                    @click="changeCompent(2, item)"
                                    >编辑
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="text"
                                    @click="changeCompent(3, item)"
                                    >复制
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="text"
                                    @click="deleteFreightRuleSetting(item)"
                                    >删除
                                </el-button>
                            </el-table-column>
                        </el-table>
                    </el-row>
                </div>
            </div>
            <div style="margin-top: 30px" v-else>
                <el-row
                    style="
                        display: flex;
                        justify-content: center;
                        margin-top: 10px;
                        border: 1px solid #ecedf1;
                        height: 50px;
                    "
                >
                    <span style="margin-left: -70px; margin-top: 15px"
                        >暂无数据</span
                    >
                </el-row>
            </div>
        </div>

        <!--删除弹框-->
        <updateGoodsTemplate
            v-if="editVisible"
            ref="updateGoodsTemplateCompent"
            @flushData="flushData"
        ></updateGoodsTemplate>

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
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import updateGoodsTemplate from "./updateGoodsTemplate.vue";
    import mixinViewModule from "@/mixins/view-module";
    import {
        freightTemplatePage,
        setDefaultFreightTemplate,
        deleteFreightTemplate,
        countFreightTemplateGoods,
    } from "@/api/api";

    export default {
        name: "list",
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["物流管理", "运费模板列表"],
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/freight/template/admin/page",
                    getDataListIsPage: true,
                },
                // 查询表单
                dataForm: {
                    templateName: "",
                },
                // 运费模板列表
                dataList: [],
                dataListLoading: false,
                editVisible: "",
            };
        },
        components: {
            Bread,
            updateGoodsTemplate,
        },
        methods: {
            // 查询表格数据
            getData() {
                this.page = 1;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.templateName = "";
                this.getDataList();
            },
            flushData() {
                this.getDataList();
            },
            // 点击新增、编辑、查看
            changeCompent(type, row) {
                this.$emit("changeCompent", type, row);
            },
            // 修改默认运费模板
            setDefaultTemplate(item) {
                let obj;
                if (item.defaultFlag == 1) {
                    obj = {
                        id: item.id,
                        defaultFlag: 0,
                    };
                } else {
                    obj = {
                        id: item.id,
                        defaultFlag: 1,
                    };
                }
                setDefaultFreightTemplate(obj).then((res) => {
                    if (res.code == 200) {
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500,
                        });
                        this.getData();
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            // 删除运费模板
            deleteFreightRuleSetting(item) {
                let obj = {
                    id: item.id,
                };
                // 查询关联商品数量
                countFreightTemplateGoods(obj).then((res) => {
                    if (res.code == 200) {
                        if (res.data > 0) {
                            // 未关联商品
                            item.goodsNum = res.data;
                            this.editVisible = true;
                            this.$nextTick(() => {
                                this.$refs.updateGoodsTemplateCompent.init(item);
                            });
                        } else {
                            // 未关联商品
                            this.$confirm(
                                "确定删除运费模板【" + item.templateName + "】吗?",
                                "确认删除",
                                {
                                    cancelButtonText: "取消",
                                    confirmButtonText: "确定",
                                    type: "warning",
                                }
                            )
                                .then(() => {
                                    let obj = {
                                        data: [item.id],
                                    };
                                    deleteFreightTemplate(obj).then((res) => {
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
                                        this.getData(); // 刷新列表数据
                                    });
                                })
                                .catch(() => {
                                    this.$message({
                                        type: "info",
                                        message: "已取消操作",
                                    });
                                });
                        }
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            // 合并操作列
            objectSpanMethod({ row, column, rowIndex, columnIndex }) {
                if (columnIndex === 5) {
                    return {
                        rowspan: 10000,
                        colspan: 1,
                    };
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    .freight {
        /deep/ .el-input__icon {
            height: unset !important;
        }
        .line {
            height: 10px;
            background: #ecedf1;
            margin: 0 -20px;
        }
        .addButton {
            margin: 10px 30px;
        }
        .el-table--border {
            margin-top: 20px;
        }
    }
</style>
