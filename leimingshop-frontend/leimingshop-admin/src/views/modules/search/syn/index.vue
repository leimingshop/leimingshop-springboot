<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataFormShow"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="关键字搜索：">
                    <el-input
                        v-model.trim="dataFormShow.name"
                        placeholder="请输入关键词搜索"
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >搜索</el-button
                    >
                    <el-button class="btn" type="primary" plain @click="reset()"
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <div>
                    <el-button
                        @click="addHandle()"
                        class="btn"
                        type="primary"
                        v-if="$hasPermission('sys:synonym:add')"
                        >添加同义词</el-button
                    >
                    <el-button
                        type="danger"
                        plain
                        @click="deleteHandle()"
                        v-if="$hasPermission('sys:synonym:delete')"
                        >批量删除</el-button
                    >
                    <el-button type="warning" plain @click="disableBatch()"
                        >批量停用</el-button
                    >
                    <el-button type="primary" plain @click="enableBatch()"
                        >批量启用</el-button
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
                        1、同义词的设置有利于提升用户搜索体验
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%; margin-top: 10px"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
            @selection-change="dataListSelectionChangeHandle"
        >
            <el-table-column
                type="selection"
                header-align="center"
                align="center"
                width="50"
            ></el-table-column>
            <el-table-column label="序号" width="140" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                    <!-- {{scope.$index+1+(parseInt(params.currentPage)-1)* parseInt(params.currentPageSize) }} -->
                </template>
            </el-table-column>
            <el-table-column
                prop="name"
                label="同义词库"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="state"
                label="状态"
                align="center"
                width="250"
            >
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.state == 0" type="info"
                        >停用</el-tag
                    >
                    <el-tag v-if="scope.row.state == 1" type="success"
                        >启用</el-tag
                    >
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        @click="addHandle(scope.$index, scope.row)"
                        size="mini"
                        v-if="$hasPermission('sys:synonym:edit')"
                        >编辑</el-button
                    >
                    <el-button
                        @click="forbitHandle('singe', scope.row)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:synonym:status')"
                    >
                        <span v-if="scope.row.state == 0">启用</span>
                        <span v-if="scope.row.state == 1" class="artclose"
                            >停用</span
                        >
                    </el-button>
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:synonym:delete')"
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
        <!-- 弹窗, 编辑 -->
        <addEditData
            v-if="addDataVisible"
            ref="addEditData"
            @searchDataList="getDataList"
        ></addEditData>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import addEditData from "./model-add-edit-data";
    import { shopsynonymUrl, deleteShopsynonymUrl } from "@/api/url";
    import { editShopsynonym } from "@/api/api";
    import { editShopsynonymBatch } from "@/api/api";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: shopsynonymUrl,
                    getDataListIsPage: true,
                    exportURL: "/admin-api/shopStore",
                    deleteURL: deleteShopsynonymUrl,
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["搜索管理", "同义词管理"],
                dataFormShow: {
                    name: "", //关键字搜索
                },
                dataList: [],
                dataListBatch: [],
                dataListLoading: false,
                addDataVisible: false,
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
            addEditData,
        },
        watch: {
            "dataFormShow.name": function (newV, oldV) {
                var chineseCount = 0,
                    characterCount = 0;
                for (let i = 0; i < newV.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) {
                        //汉字
                        chineseCount = chineseCount + 2;
                    } else {
                        //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 300) {
                        //输入字符大于300的时候过滤
                        this.dataFormShow.name = newV.substr(
                            0,
                            chineseCount / 2 + characterCount - 1
                        );
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
        methods: {
            getData() {
                this.dataForm = {};
                for (let key in this.dataFormShow) {
                    this.$set(this.dataForm, `${key}`, this.dataFormShow[key]);
                }
                this.dataList = [];
                console.log(this.dataForm);
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataFormShow.name = ""; //关键字搜索
                this.dataForm.name = ""; //关键字搜索
                this.getDataList();
            },
            // 新建
            addHandle(index = -1, row = "") {
                this.setAddDataVisible(true);
                this.$nextTick(() => {
                    this.$refs.addEditData.init(row);
                });
            },
            setAddDataVisible(boolargu) {
                this.addDataVisible = boolargu;
            },
            forbitHandle(index, row) {
                this.currentIndex = index;
                var obj = {
                    id: row.id,
                    state: row.state == 1 ? 0 : 1, //
                    name: row.name,
                };
                var msg = "";
                row.state == 1 ? (msg = "禁用") : (msg = "启用");
                this.$confirm("是否" + msg + "该分组?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.forbitLoading = true;
                        editShopsynonym(obj).then((res) => {
                            this.forbitLoading = false;
                            // console.log(res);
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
                    })
                    .catch(() => {});
            },
            disableBatch() {
                this.dataListBatch = [];
                this.dataListSelections.forEach((item, index) => {
                    var obj = {
                        id: item.id,
                        state: item.state == 1 ? 0 : 1, //
                        name: item.name,
                    };
                    this.dataListBatch.push(obj);
                });
                var msg = "停用";
                if (this.dataListSelections.length < 1) {
                    this.$message({
                        message: "请选择操作项",
                        type: "warning",
                        duration: 1500,
                    });
                    return false;
                }
                this.$confirm("是否" + msg + "该分组?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.forbitLoading = true;
                        editShopsynonymBatch(this.dataListBatch).then((res) => {
                            this.forbitLoading = false;
                            // console.log(res);
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
                    })
                    .catch(() => {});
            },
            enableBatch() {
                this.dataListBatch = [];
                this.dataListSelections.forEach((item, index) => {
                    var obj = {
                        id: item.id,
                        state: item.state == 1 ? 0 : 1, //
                        name: item.name,
                    };
                    this.dataListBatch.push(obj);
                });
                var msg = "启用";
                if (this.dataListSelections.length < 1) {
                    this.$message({
                        message: "请选择操作项",
                        type: "warning",
                        duration: 1500,
                    });
                    return false;
                }
                this.$confirm("是否" + msg + "该分组?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.forbitLoading = true;
                        editShopsynonymBatch(this.dataListBatch).then((res) => {
                            this.forbitLoading = false;
                            // console.log(res);
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
                    })
                    .catch(() => {});
            },
        },
    };
</script>

<style scoped>
</style>
