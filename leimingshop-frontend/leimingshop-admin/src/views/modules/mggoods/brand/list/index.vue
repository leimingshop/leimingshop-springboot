<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <!--导出按钮-->
            <importAndExport
                style="right: 20px; top: 12px; position: absolute"
                :importAndExportOptions="importAndExportOptions"
                :dataForm="dataForm"
                @getDataList="getDataList"
                v-if="$hasPermission('sys:brand:export')"
            ></importAndExport>

            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="品牌首字母：">
                    <el-select
                        v-model="dataForm.brandInitials"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in brandInitialList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="品牌名称：">
                    <el-input
                        v-model="dataForm.brandName"
                        placeholder="请输入品牌名称"
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
                <div class="formControlArea">
                    <div>
                        <el-form-item style="margin-bottom: 0px !important">
                            <el-button
                                type="primary"
                                @click="addOrEditHandle()"
                                v-if="$hasPermission('sys:brand:save')"
                                >新增品牌</el-button
                            >
                            <el-button
                                type="danger"
                                plain
                                @click="deleteHandle()"
                                v-if="$hasPermission('sys:brand:delete')"
                                >批量删除</el-button
                            >
                        </el-form-item>
                    </div>
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
                        1、商品品牌建立后可与商品分类进行绑定，新增一个品牌则需要重新与商品分类建立所属关系
                    </div>
                    <div class="iconSize">
                        2、品牌绑定商品分类后，商家发布商品时，可根据发布的商品所在分类找到对应的所属品牌并选择
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
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
                </template>
            </el-table-column>

            <el-table-column
                prop="brandPic"
                label="品牌LOGO"
                align="center"
                min-width="200"
            >
                <template slot-scope="scope">
                    <img
                        style="width: 50px; height: 50px"
                        :src="scope.row.brandPic | filterImgUrl"
                        alt=""
                    />
                </template>
            </el-table-column>

            <el-table-column
                prop="brandName"
                label="品牌名称"
                align="center"
                min-width="200"
            >
            </el-table-column>

            <el-table-column
                prop="brandNameEn"
                label="英文名称"
                align="center"
                min-width="200"
            >
            </el-table-column>

            <el-table-column
                prop="brandInitials"
                label="品牌首字母"
                align="center"
                min-width="200"
            >
            </el-table-column>

            <!-- <el-table-column
                  prop="brandApply"
                  label="审核状态"
                  align="center"
              min-width="200">
                <template slot-scope="scope">
                   <span v-if="scope.row.brandApply==0">待审核</span>
                  <span v-else-if="scope.row.brandApply==1">通过</span>
                  <span v-else-if="scope.row.brandApply==2">未通过</span>
                  <span v-else>--</span>
               </template>
                </el-table-column> -->

            <el-table-column label="操作" align="center" min-width="220">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:brand:update')"
                    >
                        编辑
                    </el-button>
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:brand:delete')"
                        >删除
                    </el-button>
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
    import importAndExport from "@/components/import-and-export";
    import { brandExport } from "@/api/io";
    import mixinViewModule from "@/mixins/view-module";
    import { brandPageUrl, deleteBrandUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["商品", "品牌管理", "品牌列表"],
                mixinViewModuleOptions: {
                    getDataListURL: brandPageUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteBrandUrl,
                    deleteIsBatch: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                /*导出*/
                importAndExportOptions: {
                    exportUrl: brandExport, //导出接口
                    exportWord: "导出",
                },
                addEditDataVisible: false,
                dataForm: {
                    brandInitials: "", //品牌首字母
                    brandName: "", // 英文名称
                    brandNameEn: "", //品牌名称
                    brandApply: "", //0为申请中，1为通过，默认为1
                },
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                    // "roleType" : 2 //角色(1-老师2-学生)
                },
                brandApplyList: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: "0",
                        name: "待审核",
                    },
                    {
                        id: "1",
                        name: "通过",
                    },
                    {
                        id: "2",
                        name: "未通过",
                    },
                ],
                totalPage: 0,
                // 商品审核状态
                brandInitialList: [
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: "A",
                        name: "A",
                    },
                    {
                        id: "B",
                        name: "B",
                    },
                    {
                        id: "C",
                        name: "C",
                    },
                    {
                        id: "D",
                        name: "D",
                    },
                    {
                        id: "E",
                        name: "E",
                    },
                    {
                        id: "F",
                        name: "F",
                    },
                    {
                        id: "G",
                        name: "G",
                    },
                    {
                        id: "H",
                        name: "H",
                    },
                    {
                        id: "I",
                        name: "I",
                    },
                    {
                        id: "J",
                        name: "J",
                    },
                    {
                        id: "K",
                        name: "K",
                    },
                    {
                        id: "L",
                        name: "L",
                    },
                    {
                        id: "M",
                        name: "M",
                    },
                    {
                        id: "N",
                        name: "N",
                    },
                    {
                        id: "O",
                        name: "O",
                    },
                    {
                        id: "P",
                        name: "P",
                    },
                    {
                        id: "Q",
                        name: "Q",
                    },
                    {
                        id: "R",
                        name: "R",
                    },
                    {
                        id: "S",
                        name: "S",
                    },
                    {
                        id: "T",
                        name: "T",
                    },
                    {
                        id: "U",
                        name: "U",
                    },
                    {
                        id: "V",
                        name: "V",
                    },
                    {
                        id: "W",
                        name: "W",
                    },
                    {
                        id: "X",
                        name: "X",
                    },
                    {
                        id: "Y",
                        name: "Y",
                    },
                    {
                        id: "Z",
                        name: "Z",
                    },
                ],
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            addEditData,
            Bread,
            importAndExport,
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
            // 重置
            reset() {
                this.dataForm.brandInitials = "";
                this.dataForm.brandName = "";
                this.dataForm.brandNameEn = "";
                this.dataForm.brandApply = "";
                this.getData();
            },
        },
    };
</script>