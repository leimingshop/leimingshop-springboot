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
                <el-form-item label="文章标题：">
                    <el-input
                        v-model="dataForm.articleTitle"
                        placeholder="文章标题"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="文章分类：">
                    <el-cascader
                        v-model="acId"
                        :options="acOption"
                        @change="caeChange"
                        :props="defaultParams"
                        change-on-select
                        :show-all-levels="false"
                        clearable
                    ></el-cascader>
                </el-form-item>
                <el-form-item label="是否显示：">
                    <el-select v-model="dataForm.status" placeholder="请选择">
                        <el-option
                            v-for="item in showFlagList"
                            :key="item.id"
                            :label="item.sgName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button calss="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button calss="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>

            <el-form>
                <div class="formControlArea">
                    <el-form-item
                        style="disply: block; margin-bottom: 0px !important"
                    >
                        <el-button
                            type="primary"
                            @click="addOrUpdateHandle()"
                            >{{ $t("add") }}</el-button
                        >
                        <el-button
                            type="danger"
                            plain
                            @click="deleteHandle()"
                            >{{ $t("deleteBatch") }}</el-button
                        >
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
                        1、新增服务指南文章选择的文章分类需先创建服务指南分类否则无法创建文章
                    </div>
                    <div class="iconSize">
                        2、新增的服务指南文章默认为显示，如不想立即显示可选择隐藏
                    </div>
                    <div class="iconSize">
                        3、前台现在的排序通过优先级排序控制
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">{{
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>
            <el-table-column
                type="selection"
                header-align="center"
                align="center"
                width="50"
            ></el-table-column>
            <el-table-column
                prop="sort"
                label="排序"
                header-align="center"
                align="center"
                width="70"
            ></el-table-column>
            <el-table-column
                prop="articleTitle"
                label="文章标题"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="acName"
                label="文章分类"
                header-align="center"
                align="center"
                width="150"
            >
            </el-table-column>
            <el-table-column
                prop="status"
                label="是否显示"
                header-align="center"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.status == 0"
                        >隐藏</el-tag
                    >
                    <el-tag type="success" v-else>显示</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="updateDate"
                label="更新时间"
                header-align="center"
                align="center"
                width="180"
            >
            </el-table-column>
            <el-table-column
                :label="$t('handle')"
                fixed="right"
                header-align="center"
                align="center"
                width="150"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrUpdateHandle(scope.row.id)"
                        >{{ $t("update") }}
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        >{{ $t("delete") }}
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
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update
            v-if="addOrUpdateVisible"
            ref="addOrUpdate"
            @refreshDataList="getDataList"
        ></add-or-update>
    </div>
</template>

<script>
    import {
        serviceGuidePageUrl,
        deleteServiceGuideUrl,
        serviceGuideClassTreeListUrl,
    } from "@/api/url";
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./serviceguide-add-or-update";
    import Bread from "@/components/bread";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: serviceGuidePageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteServiceGuideUrl,
                    deleteIsBatch: true,
                },
                acId: [],
                acOption: [],
                defaultParams: {
                    label: "acName",
                    value: "id",
                    children: "children",
                },
                dataForm: {
                    articleTitle: "", //文章标题
                    acId: [], //分类ID
                    status: "",
                },
                breaddata: ["运营", "服务指南管理", "服务指南文章"],
                showFlagList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "隐藏",
                    },
                    {
                        id: 1,
                        sgName: "显示",
                    },
                ],
                tableheight: "auto",
                tableheightBig: 300,
            };
        },
        components: {
            AddOrUpdate,
            Bread,
        },
        created() {
            // 第一次请求数据
            this.classList();
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
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.dataForm = {};
                this.acId = []; //分类ID（显示）
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //改变acId参数格式
            caeChange(item) {
                this.dataForm.acId = item[item.length - 1];
                this.dataForm.acId = this.dataForm.acId.toString();
            },
            //文章分类集合
            classList() {
                this.$http.get(serviceGuideClassTreeListUrl).then((res) => {
                    if (res.data.code == 200) {
                        // 将获得到的数据赋值给options
                        this.acOption = res.data.data;
                    }
                });
            },
        },
    };
</script>