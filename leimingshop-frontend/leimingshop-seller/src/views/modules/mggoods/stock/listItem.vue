<template>
    <div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            @selection-change="dataListSelectionChangeHandle"
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

            <el-table-column label="商品信息" align="center" width="380">
                <template slot-scope="scope">
                    <div class="goodsPropsWrap">
                        <div class="goodsImg">
                            <img
                                :src="scope.row.pictureUrl | filterImgUrl"
                                alt=""
                            />
                        </div>
                        <div class="goodsProps">
                            <span class="goodsName">
                                <label class="goodsNameTitle">名称:</label>
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    :content="scope.row.goodsName"
                                    placement="top-start"
                                >
                                    <span
                                        class="goodsNameContent"
                                        @click="previewH5Fn(scope.row)"
                                        >{{ scope.row.goodsName }}</span
                                    >
                                </el-tooltip>
                            </span>
                            <span class="goodsBrand">
                                <label class="goodsNameTitle">品牌:</label>
                                <span class="goodsBrandName">{{
                                    scope.row.brandName
                                }}</span>
                            </span>
                            <span class="goodsNum">
                                <label class="goodsNameTitle">货号:</label>
                                <span class="goodsBrandName">{{
                                    scope.row.goodsSerial
                                }}</span>
                            </span>
                        </div>
                    </div>
                </template>
            </el-table-column>

            <el-table-column prop="gcName" label="后台分类" align="center">
            </el-table-column>

            <el-table-column label="销售价/成本价" align="center">
                <template slot-scope="scope">
                    <div class="priceWrap">
                        <div class="price1">
                            ￥{{ scope.row.specSellPrice }}
                        </div>
                        <div class="price2">
                            ￥{{ scope.row.specCostPrice }}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="商品库存" align="center">
                <template slot-scope="scope">
                    <div class="stockWrap">
                        <div class="stock">{{ scope.row.goodsStock }}</div>
                    </div>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:stock:update')"
                        >修改库存</el-button
                    >
                    <el-button
                        @click.native.prevent="showLogListFn(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:stock:log:view')"
                        >修改记录</el-button
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
        <!-- 预览h5 -->
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import { goodsPageUrl } from "@/api/url";
    // import { deletepriceLogUrl } from '@/api/url'
    import addEditData from "./model-add-edit-data";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";
    import { goodsUpdateCheckActivity } from "@/api/api.js";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: goodsPageUrl,
                    activatedIsNeed: false,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deletepriceLogUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                addEditDataVisible: false,
                dataForm: {
                    goodsShow: "",
                },
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 420,
            };
        },
        props: {
            tableheightBig: {
                type: Number,
                default: 0,
            },
        },
        components: {
            addEditData,
            modelPreviewH5,
        },
        created() {},
        mounted() {
            // this.getDataList();
        },
        methods: {
            init(dataForm) {
                Object.assign(this.dataForm, dataForm);
                this.getData();
            },
            // 新建和编辑
            addOrEditHandle(index = -1, row = "") {
                // 查询商品是否参加活动
                let obj = [row.id];
                goodsUpdateCheckActivity(obj).then((res) => {
                    if (res.code == 200) {
                        if (res.data.activityFlag == 0) {
                            // 未参加促销
                            this.setAddEditDataVisible(true);
                            this.$nextTick(() => {
                                this.$refs.addEditData.init(row);
                            });
                        } else if (res.data.activityFlag == 1) {
                            // 促销未开始
                            this.$confirm(res.data.operationMsg, "提示", {
                                cancelButtonText: "取消",
                                confirmButtonText: "确定",
                                type: "warning",
                            })
                                .then(() => {
                                    this.setAddEditDataVisible(true);
                                    this.$nextTick(() => {
                                        this.$refs.addEditData.init(row);
                                    });
                                })
                                .catch(() => {
                                    this.$message({
                                        type: "info",
                                        message: "已取消操作",
                                    });
                                });
                        } else if (res.data.activityFlag == 2) {
                            // 促销进行中
                            this.$message({
                                type: "warning",
                                message: res.data.operationMsg,
                            });
                        } else {
                            this.$message({
                                message: "服务器异常",
                                type: "error",
                                duration: 1500,
                            });
                        }
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                        });
                    }
                });
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
            // 重置
            reset() {
                this.dataForm.attrName = "";
                this.getData();
            },
            showLogListFn(id) {
                this.$emit("showLogListFn", id);
            },
        },
    };
</script>
<style  lang="scss"  scoped>
    @import "@/element-ui/theme-variables.scss";

    .goodsImg {
        width: 70px;
        height: 70px;
        margin: auto;
        img {
            width: 100%;
            height: 100%;
        }
    }
    // 价格
    .priceWrap {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 100%;
        .price1 {
            color: $--color-primary;
        }
        div {
            width: 100%;
            text-align: right;
        }
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
    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 330px;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .goodsImg {
            width: 70px;
            height: 70px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsProps {
            width: 210px;
            height: auto;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            color: #999999;

            .goodsNameTitle {
                display: inline-block;
                width: 50px;
                color: #666666ff;
            }

            .goodsBrandName {
                text-align: left;
            }

            .goodsName {
                display: flex;
                height: 41px;

                .goodsNameContent {
                    width: 150px;
                    text-align: left;
                    text-overflow: -o-ellipsis-lastline;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    line-clamp: 2;
                    -webkit-box-orient: vertical;
                    color: #4e80db;
                    text-decoration: none;
                    cursor: pointer;
                }
            }
        }
    }
</style>
