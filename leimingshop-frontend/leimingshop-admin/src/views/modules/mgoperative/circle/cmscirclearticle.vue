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
                <el-form-item label="帖子标题/内容：">
                    <el-input
                        v-model="dataForm.articleTitle"
                        placeholder="帖子标题/内容"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="圈子话题：">
                    <el-input
                        v-model="dataForm.topicName"
                        placeholder="圈子话题"
                        clearable
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
                <el-form-item label="发布类型：">
                    <el-select
                        v-model="dataForm.articleFlag"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in articleFlagList"
                            :key="item.id"
                            :label="item.sgName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否显示：">
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
                <el-form-item label="审核状态：">
                    <el-select v-model="dataForm.audit" placeholder="请选择">
                        <el-option
                            v-for="item in auditList"
                            :key="item.id"
                            :label="item.sgName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="帖子类型：">
                    <el-select
                        v-model="dataForm.essenceFlag"
                        placeholder="请选择"
                    >
                        <el-option
                            v-for="item in essenceFlagList"
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
                    <div class="iconSize">1、可设置高质量的帖子为精华帖</div>
                    <div class="iconSize">2、如帖子内容不合规可隐藏该帖子</div>
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
            <!-- :height="!$store.state.mainSwitch ? tableheight : tableheightBig" -->

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
                prop="articleTitle"
                label="帖子标题/内容"
                header-align="center"
                align="center"
            >
                <template slot-scope="scope">
                    <div class="contentValue" :title="scope.row.articleTitle">
                        {{ scope.row.articleTitle }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="topicName"
                label="圈子话题"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="acName"
                label="圈子分类"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="articleFlag"
                label="发布类型"
                header-align="center"
                align="center"
                width="120"
            >
                <template slot-scope="scope">
                    <span v-if="scope.row.articleFlag == 1">文字帖</span>
                    <span v-if="scope.row.articleFlag == 2">图文帖</span>
                    <span v-if="scope.row.articleFlag == 3">视频帖</span>
                    <span v-if="scope.row.articleFlag == 4">文章帖</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="creator"
                label="创建人"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="updateDate"
                label="更新时间"
                header-align="center"
                align="center"
                width="160"
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
                prop="articlePraiseNum"
                label="点赞数"
                header-align="center"
                align="center"
                width="90"
            >
            </el-table-column>
            <el-table-column
                prop="articleCommentNum"
                label="回复数"
                header-align="center"
                align="center"
                width="90"
            >
            </el-table-column>
            <el-table-column
                prop="shareNum"
                label="分享数"
                header-align="center"
                align="center"
                width="90"
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
                prop="audit"
                label="审核状态"
                header-align="center"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.audit == 0"
                        >未审核</el-tag
                    >
                    <el-tag type="success" v-else-if="scope.row.audit == 1"
                        >审核通过</el-tag
                    >
                    <el-tag type="danger" v-else>审核驳回</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="essenceFlag"
                label="精华帖"
                header-align="center"
                align="center"
                width="80"
            >
                <template slot-scope="scope">
                    <el-button
                        v-if="scope.row.essenceFlag == 1"
                        type="success"
                        @click="statusUpdate(scope.row.id, 5, 0)"
                        icon="el-icon-check"
                        circle
                    ></el-button>
                    <el-button
                        v-else="scope.row.essenceFlag == 0"
                        type="info"
                        @click="statusUpdate(scope.row.id, 5, 1)"
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
                width="170"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        v-if="scope.row.articleFlag == 4"
                        @click="
                            articleAudit(
                                scope.row.id,
                                scope.row.audit == 0 ? 0 : 1
                            )
                        "
                    >
                        {{
                            scope.row.audit == 0 ? "审核" : "审核结果"
                        }}</el-button
                    >
                    <el-button
                        type="text"
                        size="small"
                        @click="
                            statusUpdate(
                                scope.row.id,
                                3,
                                scope.row.status == 0 ? 1 : 0
                            )
                        "
                    >
                        {{ scope.row.status == 0 ? "显示" : "隐藏" }}</el-button
                    >
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrUpdateHandle(scope.row.id)"
                        >查看详情</el-button
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
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update
            v-if="addOrUpdateVisible"
            ref="addOrUpdate"
            @refreshDataList="getDataList"
        ></add-or-update>
        <!-- 弹窗, 审核 / 审核结果 -->
        <circleAudit
            v-if="circleAuditVisible"
            ref="circleAudit"
            @refreshDataList="getDataList"
        ></circleAudit>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./cmscirclearticle-add-or-update";
    import circleAudit from "./circleAudit";
    import Bread from "@/components/bread";
    import {
        circleArticlePageUrl,
        deleteCircleArticleUrl,
        updateCircleArticleUrl,
        statusUpdateCircleUrl,
        circleClassListUrl,
    } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                circleAuditVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: circleArticlePageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteCircleArticleUrl,
                    deleteIsBatch: true,
                },
                classList: "",
                dataForm: {
                    articleTitle: "",
                    articleFlag: "",
                    acId: "",
                    topicName: "",
                    status: "",
                    audit: "",
                    essenceFlag: "",
                },
                breaddata: ["运营管理", "圈子管理", "帖子管理 "],
                articleFlagList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 1,
                        sgName: "文字帖",
                    },
                    {
                        id: 2,
                        sgName: "图文帖",
                    },
                    {
                        id: 3,
                        sgName: "视频帖",
                    },
                    {
                        id: 4,
                        sgName: "文章帖",
                    },
                ],
                statusList: [
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
                auditList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "未审核",
                    },
                    {
                        id: 1,
                        sgName: "审核通过",
                    },
                    {
                        id: 2,
                        sgName: "审核驳回",
                    },
                ],
                essenceFlagList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "普通帖",
                    },
                    {
                        id: 1,
                        sgName: "精华帖",
                    },
                ],
                // tableheight: "auto",
                tableHeight:null,
                tableheightBig: 300,
            };
        },
        components: {
            AddOrUpdate,
            circleAudit,
            Bread,
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
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getClassListInfo();
                this.getDataList();
            },
            //重置按钮
            reset() {
                this.page = 1;
                this.limit = 10;
                this.dataForm = {};
                this.getDataList();
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
            //文章状态修改
            statusUpdate(id, type, code) {
                this.$confirm(`确定进行此次操作？`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    center: true,
                }).then(() => {
                    var obj = {};
                    obj.id = id;
                    obj.type = type;
                    obj.code = code;
                    this.$http.put(statusUpdateCircleUrl, obj).then((res) => {
                        if (res.data.code == 200) {
                            this.$message.success({
                                message: "操作成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => {
                                    this.getData();
                                },
                            });
                        } else {
                            this.$message.info(res.data.msg);
                        }
                    });
                });
            },

            //审核
            articleAudit(id, auditCode) {
                this.circleAuditVisible = true;
                this.$nextTick(() => {
                    this.$refs.circleAudit.dataForm.id = id;
                    this.$refs.circleAudit.auditDataForm.auditCode = auditCode;
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .cell > .el-button {
        border-radius: 50% !important;
        padding: 12px !important;
    }

    .contentValue {
        width: 100%;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }
</style>