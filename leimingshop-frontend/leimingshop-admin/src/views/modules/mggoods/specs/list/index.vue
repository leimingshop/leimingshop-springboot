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
                        v-model="dataForm.specName"
                        placeholder="请输入规格名称"
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
                                v-if="$hasPermission('sys:spec:save')"
                            >
                                新增规格</el-button
                            >
                            <el-button
                                type="danger"
                                plain
                                @click="deleteHandle()"
                                v-if="$hasPermission('sys:batch:spec:delete')"
                                >批量删除</el-button
                            >
                        </div>
                        <div style="display: flex">
                            <mainSwitch></mainSwitch>
                            <mainTipsMessage></mainTipsMessage>
                        </div>
                    </div>
                </el-form-item>
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
                        1、新增规格可添加一个新的商品规格，编辑该数据可修改其规格名称、排序、以及规格值；商品规格可与商品分类进行绑定，例如：颜色\尺寸\材质...
                    </div>
                    <div class="iconSize">
                        2、商品规格值为该规格的具体规格参数，例如：规格颜色的规格值为-红色\黄色\黑色\白色...
                    </div>
                    <div class="iconSize">
                        3、商品规格值可由平台进行建立，再由商家对已建规格值进行选择；每项规格的规格值总数不会超过25个
                    </div>
                    <div class="iconSize">
                        4、进行删除规格及规格值操作时，因其有可能已被商家选择，且与商品进行了关联，删除既有的规格及规格值将直接影响前台商品的正常显示，请谨慎操作。
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :row-style="iRowStyle"
            :cell-style="iCellStyle"
            :header-row-style="iHeaderRowStyle"
            :header-cell-style="iHeaderCellStyle"
            width="100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            @selection-change="dataListSelectionChangeHandle"
        >
            <el-table-column type="selection" width="70"> </el-table-column>

            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                    <!-- {{scope.$index+1+(parseInt(params.currentPage)-1)* parseInt(params.currentPageSize) }} -->
                </template>
            </el-table-column>

            <el-table-column prop="specName" label="规格" align="center">
            </el-table-column>

            <el-table-column
                prop="specValue"
                label="规格值"
                align="center"
                min-width="200"
            >
                <template slot-scope="scope">
                    <el-tooltip
                        class="specValueClass"
                        effect="dark"
                        :content="scope.row.specValue"
                        placement="top-start"
                    >
                        <span>
                            {{ scope.row.specValue }}
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>

            <el-table-column
                prop="specGroupValue"
                label="分组"
                align="center"
                min-width="200"
            >
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:spec:update')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:spec:delete')"
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
    import { specePageUrl, deleteSpecUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["商品管理", "规格管理", "规格列表"],
                mixinViewModuleOptions: {
                    getDataListURL: specePageUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteSpecUrl,
                    deleteIsBatch: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                addEditDataVisible: false,
                dataForm: {
                    specName: "",
                },
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                    // "roleType" : 2 //角色(1-老师2-学生)
                },
                totalPage: 0,
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
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            // 新建和编辑
            addOrEditHandle(index = -1, row = "") {
                this.setAddEditDataVisible(true);
                this.dataForm.specName = "";
                this.dataForm.groupStatus = "";
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
                // this.dataForm.groupStatus=1;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm = {};
                this.getData();
            },
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
        },
    };
</script>
<style lang="scss" scoped>
    .specValueClass {
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    /deep/.el-table td {
        padding: 7px 0;
    }
</style>