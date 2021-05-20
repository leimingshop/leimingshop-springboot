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
                        v-model="dataForm.labelName"
                        placeholder="标签名称"
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
                                v-if="$hasPermission('sys:label:save')"
                                >新增标签
                            </el-button>
                            <el-button
                                type="danger"
                                plain
                                @click="deleteHandle()"
                                v-if="$hasPermission('sys:label:delete')"
                            >
                                批量删除
                            </el-button>
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
                                1、新增标签可添加一个新的商品标签，编辑该数据可修改其标签名称、排序、以及标签值；商品标签可与商品进行绑定，例如：新品\爆款\特惠...
                            </div>
                            <div class="iconSize">
                                2、商品标签值可由平台进行建立，再由商家对已建标签值进行选择
                            </div>
                            <div class="iconSize">
                                3、进行删除标签及标签值操作时，因其有可能已被商家选择，且与商品进行了关联，删除既有的标签将直接影响前台商品的正常显示，请谨慎操作。
                            </div>
                        </template>
                    </el-alert>
                </el-form-item>
            </el-form>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            @selection-change="dataListSelectionChangeHandle"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" minwidth="70"> </el-table-column>

            <el-table-column label="序号" min-width="70" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                </template>
            </el-table-column>

            <el-table-column
                prop="labelName"
                label="标签名称"
                align="center"
                min-width="140"
            >
            </el-table-column>

            <el-table-column
                prop="goodsNum"
                label="商品数量"
                align="center"
                min-width="80"
            >
            </el-table-column>

            <el-table-column
                prop="storeNum"
                label="店铺数量"
                align="center"
                min-width="80"
            >
            </el-table-column>

            <el-table-column
                prop="groupName"
                label="分组名称"
                align="center"
                min-width="140"
            >
            </el-table-column>

            <el-table-column label="操作" align="center" min-width="120">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:label:edit')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:label:delete')"
                        >删除</el-button
                    >
                    <el-button
                        class="btn"
                        @click="copyLink()"
                        data-clipboard-action="copy"
                        :data-clipboard-text="scope.row | filterlabelfUrl"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:label:copy')"
                        >复制链接</el-button
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
    import { tagPageUrl, deleteTagUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";
    import Clipboard from "clipboard";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["商品管理", "标签管理", "标签列表"],
                mixinViewModuleOptions: {
                    getDataListURL: tagPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteTagUrl,
                    deleteIsBatch: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                addEditDataVisible: false,
                dataForm: {
                    labelName: "",
                },
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
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
        mounted() {},
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        115;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            //复制链接
            copyLink() {
                var clipboard = new Clipboard(".btn");
                let that = this;
                clipboard.on("success", (e) => {
                    that.$message({
                        message: "链接复制成功",
                        type: "success",
                        duration: 1000,
                    });
                    clipboard.destroy();
                });

                clipboard.on("error", (e) => {
                    that.$message({
                        message: "链接复制失败，请重新复制",
                        type: "error",
                        duration: 1000,
                    });
                    clipboard.destroy();
                });
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
                this.groupStatus = 1;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm = {};
                this.getData();
            },
        },
    };
</script>
<style lang="scss" scoped>
    /deep/.el-table td {
        padding: 7px 0;
    }
</style>