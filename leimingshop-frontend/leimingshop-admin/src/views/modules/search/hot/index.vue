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
                        v-model.trim="dataFormShow.hotWord"
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
                        v-if="$hasPermission('sys:hotword:add')"
                        >添加搜索词</el-button
                    >
                    <el-button
                        type="danger"
                        plain
                        @click="deleteHandle()"
                        v-if="$hasPermission('sys:hotword:delete')"
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
                        1、热门搜索为PC及移动端搜索框下方配置的热门搜索
                    </div>
                    <div class="iconSize">
                        2、搜索词建议2-6个字，搜索词个数建议8个以内
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
            @selection-change="dataListSelectionChangeHandle"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
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
                prop="hotWord"
                label="搜索词"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="sort"
                label="排序"
                align="center"
                width="250"
            ></el-table-column>
            <el-table-column label="操作" align="center" width="250">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        @click="addHandle(scope.$index, scope.row)"
                        size="mini"
                        v-if="$hasPermission('sys:hotword:edit')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:hotword:delete')"
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
    import { keywordpageUrl, deleteShophotkeywordUrl } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: keywordpageUrl,
                    getDataListIsPage: true,
                    exportURL: "/admin-api/shopStore",
                    deleteURL: deleteShophotkeywordUrl,
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["搜索管理", "热门搜索"],
                dataFormShow: {
                    hotWord: "", //关键字搜索
                },
                dataList: [],
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
            // 关键字搜索
            "dataFormShow.hotWord": function (newV, oldV) {
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
                        this.dataFormShow.hotWord = newV.substr(
                            0,
                            chineseCount / 2 + characterCount - 1
                        );
                    }
                }
            },
        },
        methods: {
            getData() {
                this.dataForm = {};
                for (let key in this.dataFormShow) {
                    this.$set(this.dataForm, `${key}`, this.dataFormShow[key]);
                }
                console.log(this.dataForm);
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataFormShow.hotWord = ""; //关键字搜索
                this.dataForm.hotWord = ""; //关键字搜索
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
        },
    };
</script>

<style scoped>
</style>
