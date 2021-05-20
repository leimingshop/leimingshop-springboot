<template>
    <div>
        <div v-if="isList">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>

                <!--导出按钮-->
                <importAndExport
                    style="right: 20px; top: 12px; position: absolute"
                    :importAndExportOptions="importAndExportOptions"
                    :dataForm="dataForm"
                    @getDataList="getDataList"
                    v-if="$hasPermission('sys:evaluate:export')"
                ></importAndExport>

                <el-form
                    :inline="true"
                    class="grayLine topGapPadding"
                    :model="dataForm"
                    ref="dataForm"
                >
                    <el-form-item label="评价时间：">
                        <el-date-picker
                            v-model="timeArr"
                            type="datetimerange"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            align="left"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']"
                        ></el-date-picker>
                    </el-form-item>
                    <el-form-item label="评价状态：" prop="evaluateState">
                        <el-select
                            v-model="dataForm.evaluateState"
                            placeholder="请选择活动区域"
                        >
                            <el-option label="全部" value=""></el-option>
                            <el-option label="显示" value="0"></el-option>
                            <el-option label="隐藏" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="回复状态：" prop="replyState">
                        <el-select
                            v-model="dataForm.replyState"
                            placeholder="请选择活动区域"
                        >
                            <el-option label="全部" value=""></el-option>
                            <el-option label="未回复" value="0"></el-option>
                            <el-option label="已回复" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" @click="getData()"
                            >查询</el-button
                        >
                        <el-button
                            class="btn"
                            type="primary"
                            plain
                            @click="reset()"
                            >重置</el-button
                        >
                    </el-form-item>
                </el-form>

                <div class="formControlArea">
                    <div>
                        <el-form>
                            <el-form-item style="display: block">
                                <el-button
                                    class="btn"
                                    @click="changeShow()"
                                    type="primary"
                                    plain
                                    v-if="$hasPermission('sys:evaluate:status')"
                                    >批量显示
                                </el-button>
                                <!-- <el-button class="btn" @click="deleteHandle()" type="danger" plain
                               v-if="$hasPermission('sys:evaluate:delete')">批量删除
                    </el-button> -->
                                <el-button
                                    class="btn"
                                    @click="changeHide()"
                                    type="info"
                                    plain
                                    v-if="$hasPermission('sys:evaluate:status')"
                                    >批量隐藏
                                </el-button>
                                <el-button
                                    class="btn"
                                    type="primary"
                                    plain
                                    @click="batchreply()"
                                    v-if="$hasPermission('sys:evaluate:read')"
                                    >批量回复</el-button
                                >
                            </el-form-item>
                        </el-form>
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
                            1、商品评价可单独回复也可批量回复
                        </div>
                        <div class="iconSize">
                            2、不想显示的评价信息可隐藏显示
                        </div>
                        <div class="iconSize">
                            3、误操作的商品评价可点击显示再次前台显示
                        </div>
                    </template>
                </el-alert>
            </div>
            <el-table
                width="100%"
                :data="dataList"
                border=""
                @selection-change="dataListSelectionChangeHandle"
                v-loading="dataListLoading"
                style="width: 100%; maigin-top: 20px"
                :height="
                    !$store.state.mainSwitch ? tableheight : tableheightBig
                "
            >
                <el-table-column
                    type="selection"
                    width="50"
                    align="center"
                ></el-table-column>
                <el-table-column
                    type="index"
                    prop="$index"
                    label="序号"
                    align="center"
                    width="50"
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
                    prop="goodsName"
                    label="商品信息"
                    align="center"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        <el-tooltip
                            class="item"
                            effect="dark"
                            :content="scope.row.goodsName"
                            placement="top-start"
                        >
                            <div
                                style="
                                    text-align: left;
                                    text-overflow: ellipsis;
                                    overflow: hidden;
                                    white-space: nowrap;
                                "
                            >
                                <img
                                    :src="
                                        scope.row.specMainPicture | filterImgUrl
                                    "
                                    width="40px"
                                    height="40px"
                                />
                                <span
                                    style="
                                        color: #4e80db;
                                        text-decoration: none;
                                        cursor: pointer;
                                    "
                                    @click="previewH5Fn(scope.row)"
                                    >{{ scope.row.goodsName }}</span
                                >
                            </div>
                        </el-tooltip>
                    </template>
                </el-table-column>

                <el-table-column label="评价" align="left" width="330">
                    <template slot-scope="scope">
                        <div class="evaluatePropsWrap">
                            <div class="evaluateProps" style="width: 93%">
                                <span>
                                    <label style="font-weight: 550; float: left"
                                        >评价星级:</label
                                    >
                                    <el-rate
                                        v-model="scope.row.evaluateScores"
                                        disabled
                                        text-color="#ff9900"
                                        style="float: left; margin-left: 8px"
                                    >
                                    </el-rate>
                                </span>
                                <br />
                                <span>
                                    <label style="font-weight: 550"
                                        >评价时间:</label
                                    >
                                    <span style="margin-left: 8px">{{
                                        scope.row.createDate
                                    }}</span>
                                </span>
                                <br />
                                <span
                                    v-if="scope.row.evaluateContent"
                                    style="
                                        display: -webkit-box;
                                        -webkit-box-orient: vertical;
                                        -webkit-line-clamp: 2;
                                        overflow: hidden;
                                    "
                                >
                                    <label style="font-weight: 550"
                                        >评价内容:</label
                                    >
                                    <el-tooltip
                                        class="item"
                                        popper-class="evaluationContent"
                                        effect="dark"
                                        style="width: 20%"
                                        :content="scope.row.evaluateContent"
                                        placement="top-start"
                                    >
                                        <span style="margin-left: 8px">{{
                                            scope.row.evaluateContent
                                        }}</span>
                                    </el-tooltip>
                                </span>

                                <span
                                    v-if="scope.row.replyContent"
                                    style="
                                        display: -webkit-box;
                                        -webkit-box-orient: vertical;
                                        -webkit-line-clamp: 2;
                                        overflow: hidden;
                                    "
                                >
                                    <label style="font-weight: 550"
                                        >回复内容:</label
                                    >
                                    <el-tooltip
                                        class="item"
                                        effect="dark"
                                        popper-class="evaluationContent"
                                        :content="scope.row.replyContent"
                                        placement="bottom"
                                    >
                                        <span style="margin-left: 8px">{{
                                            scope.row.replyContent
                                        }}</span>
                                    </el-tooltip>
                                </span>
                            </div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="晒单图片" align="center" width="190px">
                    <template slot-scope="scope">
                        <div align="center">
                            <span
                                v-if="
                                    !scope.row.evaluateImage ||
                                    scope.row.evaluateImage == null
                                "
                            ></span>

                            <img
                                v-else
                                v-for="(itemimg,
                                index) in scope.row.evaluateImage.split(',')"
                                :src="itemimg | filterImgUrl"
                                width="40px"
                                height="40px"
                                style="margin-left: 1px; margin-top: 1px"
                                @click="
                                    previewBigImg(
                                        scope.row.evaluateImage,
                                        index
                                    )
                                "
                            />
                        </div>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="replyState"
                    label="回复状态"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.replyContent"
                            type="success"
                            plain
                            >已回复</el-tag
                        >
                        <el-tag v-if="!scope.row.replyContent" type="info"
                            >未回复</el-tag
                        >
                    </template>
                </el-table-column>

                <el-table-column
                    prop="memberName"
                    label="用户信息"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    label="评价时间"
                    align="center"
                    min-width="120"
                ></el-table-column>
                <el-table-column
                    prop="evaluateState "
                    label="显示状态"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.evaluateState == 0"
                            type="success"
                            plain
                            >显示</el-tag
                        >
                        <el-tag v-if="scope.row.evaluateState == 1" type="info"
                            >隐藏</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column label="操作" min-width="100" align="center">
                    <template slot-scope="scope">
                        <!--<el-button size="mini" type="text" @click="handleEdit(scope.row)"
                                   v-if="$hasPermission('sys:evaluate:view')">详情
                        </el-button>-->
                        <el-button
                            size="mini"
                            @click="changeHide(scope.row.id)"
                            v-if="
                                scope.row.evaluateState == 0 &&
                                $hasPermission('sys:evaluate:status')
                            "
                            type="text"
                            ><span class="artdisable">隐藏</span></el-button
                        >
                        <el-button
                            size="mini"
                            @click="changeShow(scope.row)"
                            type="text"
                            v-if="
                                scope.row.evaluateState == 1 &&
                                $hasPermission('sys:evaluate:status')
                            "
                            ><span class="artstart">显示</span></el-button
                        >
                        <el-button
                            size="mini"
                            type="text"
                            @click="reply(scope.row)"
                            v-if="$hasPermission('sys:evaluate:reply')"
                            >回复
                        </el-button>
                        <!-- <el-button class="artdanger" size="mini" type="text" @click="deleteHandle( scope.row.id)"
                                   v-if="$hasPermission('sys:evaluate:delete')">删除
                        </el-button> -->
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
            ></el-pagination>
            <el-dialog title="回复" :visible.sync="dialogFormVisible">
                <el-form :model="form" :rules="dataRule" ref="repet">
                    <el-form-item label="回复内容" prop="content">
                        <el-input
                            v-model="form.content"
                            :placeholder="$t('model.name')"
                            autocomplete="off"
                            maxlength="50"
                        ></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false"
                        >取 消</el-button
                    >
                    <el-button
                        type="primary"
                        @click="submitForm()"
                        :loading="submitLoading"
                        >确 定</el-button
                    >
                </div>
            </el-dialog>
            <!-- 预览h5 -->
            <modelPreviewH5
                v-if="previewH5Visible"
                ref="previewH5Compon"
            ></modelPreviewH5>
        </div>
        <evaDet
            v-else
            @changeState="changeModel"
            :evaDetails="evaDetails"
        ></evaDet>
    </div>
</template>
<script>
    import importAndExport from "@/components/import-and-export";
    import { goodsEvaluateExport } from "@/api/io";
    import Bread from "@/components/bread";
    import evaDet from "./evaDet.vue";
    import { deleva, goodseva } from "@/api/url";
    import { changeStatus, evaDets, msgReply, changeReadStatus } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";
    import cloneDeep from "lodash/cloneDeep";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                previewH5Visible: false,
                /*导出*/
                importAndExportOptions: {
                    exportUrl: goodsEvaluateExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: goodseva,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleva,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                form: {
                    content: "",
                },
                cartainId: "",
                breaddata: ["商品", "商品管理", "商品评价"],
                dataForm: {
                    evaluateState: "",
                    readState: "",
                    replyState: "",
                },
                evaDetails: "", //评价详情
                dataListLoading: false,
                totalPage: 0,
                isList: true, //列表页 false-详情页
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },

                timeArr: "", //下单时间
                dataList: [],
                dialogFormVisible: false,
                submitLoading: false,
                tableheight: document.body.offsetHeight - 400,
                tableheightBig: 300,
            };
        },
        created() {},
        components: { Bread, evaDet, modelPreviewH5, importAndExport },
        computed: {
            dataRule() {
                return {
                    content: [
                        {
                            required: true,
                            message: "回复内容不能为空",
                            trigger: "blur",
                        },
                    ],
                };
            },
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        105;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?goodsId=" +
                        row.goodsId +
                        "&specId=" +
                        row.goodsSpecId
                );
            },
            // 预览图片
            //大图预览带分页
            previewBigImg(images, index) {
                //string转数组
                var imagesArr = images ? images.split(",") : [];
                if (imagesArr.length == 0) {
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2, index2) => {
                    if (/http/.test(item2) || /data:image/.test(item2)) {
                    } else {
                        imagesArr[index2] =
                            window.SITE_CONFIG["imgURL"] + "" + item2;
                    }
                });
                this.$imagePreview({
                    images: imagesArr,
                    index: index,
                });
            },

            //详情
            handleEdit(index) {
                const obj = { id: index.id };
                evaDets(obj).then((res) => {
                    if (res.code == 200) {
                        this.evaDetails = res.data;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
                this.isList = false;
            },
            //返回上一级
            changeModel() {
                this.isList = !this.isList;
            },
            //条件查询
            getData() {
                this.dataForm.startDate = this.timeArr[0];
                this.dataForm.endDate = this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.timeArr = [];
                this.dataForm = {};
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //评价回复
            reply(row) {
                this.dialogFormVisible = true;
                this.submitLoading = false;
                this.form.content = "";
                this.submitFormId = row.id;
            },
            batchreply() {
                this.dialogFormVisible = true;
                this.submitLoading = false;
                this.form.content = "";
            },
            submitForm() {
                if (this.submitLoading) {
                    return;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });

                if (!this.submitFormId && arrId.length == 0) {
                    this.$message({
                        type: "error",
                        message: "请选择回复数据",
                    });
                    return;
                }
                this.$refs.repet.validate((valid) => {
                    if (valid) {
                        const obj = {
                            ids:
                                this.submitFormId == null
                                    ? arrId
                                    : [this.submitFormId],
                            replyCount: this.form.content,
                        };
                        this.submitLoading = true;
                        changeStatus(obj).then((res) => {
                            this.submitLoading = false;
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.dialogFormVisible = false;
                                this.submitFormId = null;
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "error",
                                    message: res.msg,
                                });
                            }
                        });
                    } else {
                    }
                });
            },
            //批量已读
            changeRead(id) {
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择批量处理的数据",
                    });
                    return false;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });
                this.$confirm("您确认读取所有选中评价吗？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        const obj = {
                            ids: id == null ? arrId : [id],
                            state: 0,
                        };
                        changeReadStatus(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: "修改成功!",
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: "修改失败!",
                                });
                                this.getDataList();
                            }
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消",
                        });
                    });
            },
            //批量显示/显示
            changeShow(row) {
                var flag = true;
                var id = "";
                if (row) {
                    id = row.id;
                    if (row.illegalDel == 1) {
                        this.$message({
                            type: "warning",
                            message: "当前评价已被平台端违规删除,禁止显示",
                        });
                        return false;
                    }
                }
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择批量处理的数据",
                    });
                    return false;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    if (val.illegalDel == 1) {
                        that.$message({
                            type: "warning",
                            message: "选中的评价中已被平台端违规删除,禁止显示",
                        });
                        flag = false;
                    }
                    arrId.push(val.id);
                });
                if (flag) {
                    this.$confirm("您确认显示所有选中评价吗？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            const obj = {
                                ids: id == null ? arrId : [id],
                                state: 0,
                            };
                            changeStatus(obj).then((res) => {
                                if (res.code == 200) {
                                    this.$message({
                                        type: "success",
                                        message: "修改成功!",
                                    });
                                    this.getDataList();
                                } else {
                                    this.$message({
                                        type: "warning",
                                        message: "修改失败!",
                                    });
                                    this.getDataList();
                                }
                            });
                        })
                        .catch(() => {
                            this.$message({
                                type: "info",
                                message: "已取消",
                            });
                        });
                }
            },
            //批量隐藏/隐藏
            changeHide(id) {
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择批量处理的数据",
                    });
                    return false;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });
                this.$confirm("您确认隐藏所有选中评价吗？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        const obj = {
                            ids: id == null ? arrId : [id],
                            state: 1,
                        };
                        changeStatus(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: "修改成功!",
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: "修改失败!",
                                });
                                this.getDataList();
                            }
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消",
                        });
                    });
            },
        },
    };
</script>
<style lang="scss">
    /*评价*/
    .evaluatePropsWrap {
        margin: auto;
        width: 330px;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .evaluatePropsWrap.evaluateProps {
        width: 210px;
        height: 70px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        color: #999999;
    }
    /*评价内容/回复内容背景框大小修改*/
    .evaluationContent {
        max-width: 40%;
    }
    .formControlArea {
        height: 70px;
    }
</style>
