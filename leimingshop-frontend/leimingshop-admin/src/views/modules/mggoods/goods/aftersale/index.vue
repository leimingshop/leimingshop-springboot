<template>
    <div>
        <div id="control-area">
            <div class="formControlArea">
                <el-form style="margin-top: 0px">
                    <el-form-item style="margin-bottom: 0px !important">
                        <el-button
                            type="primary"
                            @click="addOrEditHandle()"
                            v-if="$hasPermission('sys:aftersale:model:save')"
                        >
                            新增售后模板</el-button
                        >
                        <!-- <el-button type="danger" plain @click="deleteHandle()" >批量删除</el-button> -->
                    </el-form-item>
                </el-form>
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
                        1、商家添加商品时可选择已添加的售后模板
                    </div>
                    <div class="iconSize">2、排序规则按照列表排序规则显示</div>
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
        >
            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>

            <el-table-column
                prop="name"
                label="模板名称"
                width="600px"
                align="center"
            >
            </el-table-column>

            <el-table-column prop="createDate" label="创建时间" align="center">
            </el-table-column>

            <el-table-column prop="updateDate" label="修改时间" align="center">
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:aftertemplate:update')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        @click.native.prevent="deleteHandle(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:aftertemplate:delete')"
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
    import { aftertemplatePageUrl, deleteAftertemplateUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: aftertemplatePageUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteAftertemplateUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                addEditDataVisible: false,
                dataForm: {},
                tableheight: document.body.offsetHeight - 340,
                tableheightBig: 300,
            };
        },
        components: {
            addEditData,
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
                        90;
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
        },
    };
</script>