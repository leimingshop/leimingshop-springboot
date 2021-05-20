<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="话题名称：" prop="topicName">
                    <el-input
                        v-model="dataForm.topicName"
                        placeholder="话题名称"
                    ></el-input>
                </el-form-item>
                <el-form-item label="圈子分类：">
                    <el-select
                        v-model="dataForm.acId"
                        placeholder="请选择"
                        clearable
                    >
                        <!-- <el-option>-请选择-</el-option> -->
                        <el-option
                            v-for="(item, index) in classList"
                            :key="item.id"
                            :label="item.acName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否启用：">
                    <el-select v-model="dataForm.status" placeholder="请选择">
                        <el-option
                            v-for="item in statusList"
                            :key="item.id"
                            :label="item.sgName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="话题类型：">
                    <el-select
                        v-model="dataForm.superTopicFlag"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in topicList"
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
                        1、圈子话题的分类选择需先在圈子分类进行创建
                    </div>
                    <div class="iconSize">2、话题可选择普通话题或超级话题</div>
                </template>
            </el-alert>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="tableHeight"
        >
            <el-table-column
                type="selection"
                header-align="center"
                align="center"
                width="50"
            ></el-table-column>
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
                prop="sort"
                label="排序"
                header-align="center"
                align="center"
                width="70"
            ></el-table-column>
            <el-table-column
                prop="topicName"
                label="话题"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="acName"
                label="圈子分类"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <!--<el-table-column prop="imageUrl" label="话题logo" header-align="center" align="center" width="150">-->
            <!--<template slot-scope="scope">-->
            <!--<img-->
            <!--style="width: 100px; height: 100px"-->
            <!--:src="$imgDomain + scope.row.imageUrl " />-->
            <!--</template>-->
            <!--</el-table-column>-->
            <el-table-column
                prop="creator"
                label="创建人"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="articleNum"
                label="帖子数"
                header-align="center"
                align="center"
                width="90"
            >
            </el-table-column>
            <el-table-column
                prop="pvNum"
                label="阅读量"
                header-align="center"
                align="center"
                width="90"
            ></el-table-column>
            <el-table-column
                prop="updateDate"
                label="更新时间"
                header-align="center"
                align="center"
                width="160"
            >
            </el-table-column>
            <!--          状态标识（0：停用 1：启用（默认））-->
            <el-table-column
                prop="status"
                label="是否启用"
                header-align="center"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <el-tag type="error" v-if="scope.row.status == 0"
                        >停用</el-tag
                    >
                    <el-tag type="success" v-else>启用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="superTopicFlag"
                label="超话推荐"
                header-align="center"
                align="center"
                width="80"
            >
                <template slot-scope="scope">
                    <el-button
                        v-if="scope.row.superTopicFlag == 1"
                        @click="
                            updateFlag(scope.row.superTopicFlag, scope.row.id)
                        "
                        type="success"
                        icon="el-icon-check"
                        circle
                    ></el-button>
                    <el-button
                        v-else="scope.row.superTopicFlag == 0"
                        @click="
                            updateFlag(scope.row.superTopicFlag, scope.row.id)
                        "
                        type="info"
                        icon="el-icon-check"
                        circle
                    ></el-button>
                </template>
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
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./cmscircle-add-or-update";
    import Bread from "@/components/bread";
    import {
        circleTopicPageUrl,
        updateCircleTopicUrl,
        deleteCircleTopicUrl,
        circleClassListUrl,
    } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: circleTopicPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteCircleTopicUrl,
                    deleteIsBatch: true,
                },
                imageUrl: "",
                classList: "",
                dataForm: {
                    id: "",
                    acId: "",
                    topicName: "",
                },
                dataForm1: {
                    id: "",
                    superTopicFlag: "",
                },
                breaddata: ["运营管理", "圈子管理", "圈子话题"],
                statusList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "停用",
                    },
                    {
                        id: 1,
                        sgName: "启用",
                    },
                ],
                topicList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "普通话题",
                    },
                    {
                        id: 1,
                        sgName: "超级话题",
                    },
                ],
                // tableheight: document.body.offsetHeight - 380,
                tableHeight:null,
                tableheightBig: 300,
            };
        },
        created() {
            this.init();
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
        mounted(){
            window.onresize = function(){
                this.tableHeight="";
            }.bind(this)
        },
        methods: {
            init() {
                this.visible = true;
                this.getClassListInfo();
            },
            updateFlag(val1, val2) {
                val1 == 1
                    ? (this.dataForm1.superTopicFlag = 0)
                    : (this.dataForm1.superTopicFlag = 1);
                this.dataForm1.id = val2;
                this.$http
                    .put(updateCircleTopicUrl, this.dataForm1)
                    .then(({ data: res }) => {
                        if (res.code == 200) {
                            this.$message({
                                type: "success",
                                message: "操作成功",
                            });
                            this.getDataList();
                        }
                    });
            },
            //获取分类列表信息
            getClassListInfo() {
                this.$http.get(circleClassListUrl).then(({ data: res }) => {
                    if (res.code == 200) {
                        this.classList = res.data;
                        //增加“无”分类
                        this.$set(this.classList, this.classList.length, {
                            acName: "无",
                            id: "0",
                        });
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            },
            //重置按钮
            reset() {
                this.dataForm.acId = "";
                this.dataForm.topicName = "";
                this.dataForm.status = "";
                this.dataForm.superTopicFlag = "";
                this.getData();
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
        },
        components: {
            AddOrUpdate,
            Bread,
        },
    };
</script>
<style lang="scss" scoped>
    .cell > .el-button {
        border-radius: 50% !important;
        padding: 12px !important;
    }
</style>