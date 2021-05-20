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
                <el-form-item label="帖子标题/内容：">
                    <el-input
                        v-model="dataForm.articleTitle"
                        placeholder="帖子标题/内容"
                        clearable
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="评论内容：">
                    <el-input
                        v-model="dataForm.commentContent"
                        placeholder="评论内容"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="评论人：">
                    <el-input
                        v-model="dataForm.creator"
                        placeholder="评论人"
                        clearable
                    ></el-input>
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
                <el-form-item label="评论时间：">
                    <el-date-picker
                        v-model="dataForm.startTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        clearable
                        placeholder="选择开始时间"
                    >
                    </el-date-picker>
                    -
                    <el-date-picker
                        v-model="dataForm.endTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        clearable
                        placeholder="选择结束时间"
                    >
                    </el-date-picker>
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
                        <el-button type="danger" plain @click="deleteHandle()"
                            >批量删除</el-button
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
                    <div class="iconSize">1、帖子评论可隐藏或者删除</div>
                </template>
            </el-alert>
        </div>
        <el-table
            v-loading="dataListLoading"
            :data="dataList"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            row-style="height:40px"
            header-row-style="height:40px"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
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
                prop="articleTitle"
                label="帖子标题/内容"
                align="center"
            >
                <template slot-scope="scope">
                    <el-tooltip
                        class="item"
                        effect="dark"
                        :disabled="scope.row.articleTitle.length <= 68"
                        :content="scope.row.logDec"
                        placement="top-start"
                    >
                        <div slot="content" class="content-text">
                            <div>
                                {{ scope.row.articleTitle }}
                            </div>
                        </div>
                        <div class="towEllipsis">
                            {{ scope.row.articleTitle }}
                        </div>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column
                prop="articleCreator"
                label="发布人"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="commentContent"
                label="评论内容"
                header-align="center"
                align="center"
            ></el-table-column>
            <el-table-column
                prop="creator"
                label="评论人"
                header-align="center"
                align="center"
                width="120"
            >
            </el-table-column>
            <el-table-column
                prop="createDate"
                label="评论时间"
                header-align="center"
                align="center"
                width="160"
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
                        @click="
                            statusUpdate(
                                scope.row.id,
                                scope.row.status == 0 ? 1 : 0
                            )
                        "
                    >
                        {{ scope.row.status == 0 ? "显示" : "隐藏" }}</el-button
                    >
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
        ></el-pagination>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import {
        circleCommentPageUrl,
        deleteCircleCommentUrl,
        statusUpdateCircleCommentUrl,
    } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                addRecommendVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: circleCommentPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteCircleCommentUrl,
                    deleteIsBatch: true,
                },
                dataForm: {
                    status: "",
                    commentParentId: "0",
                    creator: "",
                    startTime: "",
                    endTime: "",
                    commentContent: "",
                    articleTitle: "",
                },
                breaddata: ["运营管理", "圈子管理", "帖子评论"],
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
                tableheight: "auto",
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
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm.status = "";
                this.dataForm.creator = "";
                this.dataForm.startTime = "";
                this.dataForm.endTime = "";
                this.dataForm.commentContent = "";
                this.dataForm.articleTitle = "";
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //评论状态修改
            statusUpdate(id, code) {
                this.$confirm(`确定进行此次操作？`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    center: true,
                }).then(() => {
                    var obj = {};
                    obj.id = id;
                    obj.code = code;
                    this.$http
                        .put(statusUpdateCircleCommentUrl, obj)
                        .then((res) => {
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
        },
    };
</script>
<style lang="scss" scoped>
    .model-box {
        .el-input {
            width: 300px;
            height: 40px;
        }

        .el-select {
            width: 300px;
        }
    }

    .content-text {
        max-width: 600px;
    }

    .towEllipsis {
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .el-textarea {
        /*width: 400px;*/
    }

    /deep/ .el-date-editor--datetime {
        width: 200px !important;
    }

    .el-form-item {
        margin-bottom: 20px !important;
    }
</style>