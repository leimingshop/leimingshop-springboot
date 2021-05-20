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
                <el-form-item label="文章名称：">
                    <el-input
                        v-model="dataForm.docTitle"
                        placeholder="文章名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="文章识别码：">
                    <el-input
                        v-model="dataForm.docCode"
                        placeholder="文章识别码"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
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
                            @click="addOrArticle()"
                            v-if="$hasPermission('sys:document:save')"
                            >新增
                        </el-button>
                        <el-button
                            type="danger"
                            plain
                            @click="deleteHandle()"
                            v-if="$hasPermission('sys:document:delete')"
                            >批量删除
                        </el-button>
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
                        1、系统文章为电商平台需要说明的隐私政策、注册协议、用户协议等说明
                    </div>
                    <div class="iconSize">
                        2、请谨慎操作，避免操作不当而删除
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
                        <el-table-column
                type="selection"
                align="center"
                width="46"
            >
                            </el-table-column
            >
                       <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="64"
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
                prop="docTitle"
                align="center"
                width="514"
                label="文章标题"
            >
                            </el-table-column
            >
                        <el-table-column
                prop="docCode"
                align="center"
                width="514"
                label="调用标识码"
            >
                            </el-table-column
            >
                        <el-table-column
                prop="address"
                align="center"
                width="514"
                label="操作"
            >
                            <template slot-scope="scope">
                                <el-button
                        type="text"
                        size="small"
                        @click="addOrArticle(scope.row.id)"
                        v-if="$hasPermission('sys:document:edit')"
                        >编辑</el-button
                    >
                                <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:document:delete')"
                        >删除
                    </el-button>
                                </template
                >
                            </el-table-column
            >
                    </el-table
        >
                
        <!-- 分页 -->
            <el-pagination
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
            :current-page="page.currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="params.currentPageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        >
                </el-pagination
        >
    </div>
</template>
<script>
    import mixinViewModule from "@/mixins/view-module";
    // import {document,documentDelete} from '@/api/api';
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/document/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/document",
                    deleteIsBatch: true,
                },
                breaddata: ["运营管理", "帮助中心", "系统文章"],
                dataForm: {
                    isShow: "",
                },
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
        },
        created() {},
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
            //新建和编辑
            addOrArticle(id) {
                this.$emit("addArticleFn", id);
            },

            //编辑
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.page = 1;
                this.dataForm = {
                    isShow: "",
                };
                this.getData();
            },
            //每页数
            // sizeChangeHandle(val){
            //     this.params.currentPageSize=val;
            //     this.params.currentPage=1;
            //     this.limit=val;
            //     this.getDataList();

            // },
            //当前页
            // currentChangeHandle(val){
            //     this.params.currentPage=val;
            //     this.page=val;
            //     this.getData();
            // },
        },
    };
</script>
<style lang="scss" scoped>
    .el-input {
        width: 170px;
        height: 40px;
    }
</style>