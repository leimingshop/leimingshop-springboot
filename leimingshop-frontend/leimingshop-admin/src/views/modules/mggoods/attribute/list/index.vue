<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <el-form
                :inline="true"
                class="grayLine noMargin topGapPadding"
                :model="dataForm"
            >
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.attrName"
                        placeholder="请输入属性名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button calss="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button calss="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <el-form>
                <el-form-item style="margin-bottom: 0px !important">
                    <div class="formControlArea">
                        <div>
                            <el-button
                                type="primary"
                                @click="addOrEditHandle()"
                                v-if="$hasPermission('sys:attribute:save')"
                            >
                                新增属性</el-button
                            >
                            <el-button
                                type="danger"
                                plain
                                @click="deleteHandleLocal()"
                                v-if="
                                    $hasPermission('sys:attribute:batch:delete')
                                "
                                >批量删除</el-button
                            >
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
                                1、分组用于对属性类型进行划分区别使用，例如属性分组名称：电脑属性分组、手机属性分组、服装属性分组等此类表述方式
                            </div>
                            <div class="iconSize">
                                2、商品属性值为该属性的具体属性参数，例如：属性内存的属性值为-1G\4G\8G\16G\32G...
                            </div>
                            <div class="iconSize">
                                3、商品属性值可由平台进行建立，再由商家对已建属性值进行选择
                            </div>
                            <div class="iconSize">
                                4、进行删除属性及属性值操作时，因其有可能已被商家选择，且与商品进行了关联，删除既有的属性及属性值将直接影响前台商品的正常显示，请谨慎操作。
                            </div>
                        </template>
                    </el-alert>
                </el-form-item>
            </el-form>
        </div>
        <el-table
            :row-style="iRowStyle"
            :cell-style="iCellStyle"
            :header-row-style="iHeaderRowStyle"
            :header-cell-style="iHeaderCellStyle"
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
            @selection-change="dataListSelectionChangeHandle"
        >
            <el-table-column type="selection" width="70"> </el-table-column>

            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                    <!-- {{scope.$index+1+(parseInt(params.currentPage)-1)* parseInt(params.currentPageSize) }} -->
                </template>
            </el-table-column>

            <el-table-column prop="attrName" label="属性名称" align="center">
            </el-table-column>

            <el-table-column
                prop="attrValue"
                label="属性值"
                align="center"
                min-width="200"
            >
            </el-table-column>
            <el-table-column
                prop="attrGroupValue"
                label="属性分组"
                align="center"
                min-width="200"
            >
            </el-table-column>
            <el-table-column label="操作" align="center" width="120">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:attribute:update')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandleLocal(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:attribute:delete')"
                        >删除</el-button
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

        <!-- 弹窗, 新建 -->
        <addEditData
            v-if="addEditDataVisible"
            ref="addEditData"
            @searchDataList="getDataList"
        ></addEditData>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import { attributePageUrl, deleteAttributeUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";

    import { attributeCheckClasss } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["商品管理", "属性管理", "属性列表"],
                mixinViewModuleOptions: {
                    getDataListURL: attributePageUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                deleteMsg: "",
                addEditDataVisible: false,
                dataForm: {
                    attrName: "",
                    // groupName:''
                },
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            addEditData,
            Bread,
        },
        created() {},
        mounted() {
            // this.getDataList();
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        120;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            iRowStyle: function ({ row, rowIndex }) {
                return "height:47px";
            },
            iHeaderRowStyle: function ({ row, rowIndex }) {
                return "height:45px";
            },
            iCellStyle: function ({ row, column, rowIndex, columnIndex }) {
                return "padding:1px";
            },
            iHeaderCellStyle: function ({ row, column, rowIndex, columnIndex }) {
                return "padding:10px";
            },
            // 新建和编辑
            addOrEditHandle(index = -1, row = "") {
                this.setAddEditDataVisible(true);
                this.$nextTick(() => {
                    this.$refs.addEditData.init(row);
                });
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            deleteHandleLocal(id) {
                if (id) {
                    var ids = [];
                    ids.push(id);
                    attributeCheckClasss(ids).then((res) => {
                        console.log(res);
                        if (res.code == 200 && res.data) {
                            this.deleteMsg = res.msg;
                        } else {
                            this.deleteMsg = res.msg;
                        }
                        //      // 单个删除
                        this.deleteHandle(id);
                    });
                } else {
                    // 批量删除
                    this.deleteHandle();
                }
            },
            // 重置
            reset() {
                this.dataForm.attrName = "";
                this.getData();
            },
        },
    };
</script>