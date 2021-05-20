<template>
    <div>
        <div id="control-area">
            <!-- <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">商品</el-breadcrumb-item>
              <el-breadcrumb-item><a href="/">商品管理</a></el-breadcrumb-item>
              <el-breadcrumb-item>商品列表</el-breadcrumb-item>
            </el-breadcrumb> -->
            <div style="display: flex; justify-content: space-between">
                <Bread :breaddata="breaddata"></Bread>
                <el-button
                    style="right: 20px; top: 12px; position: absolute"
                    type="primary"
                    @click="showLogListFn()"
                    >修改记录</el-button
                >
            </div>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.goodsName"
                        placeholder="商品名称/商品编号"
                    ></el-input>
                </el-form-item>
                <el-form-item label="所属店铺：">
                    <el-input
                        v-model="dataForm.storeName"
                        placeholder="店铺名称"
                    ></el-input>
                </el-form-item>
                <el-form-item label="商品品牌：">
                    <!-- <el-autocomplete
                    v-model="state4"
                    :fetch-suggestions="getBrandListPull"
                    placeholder="请输入品牌"
                    @blur="handleSelect"
                    @select="handleSelect"
                ></el-autocomplete> -->
                    <el-select
                        v-model="dataForm.brandName"
                        filterable
                        clearable
                        placeholder="请选择品牌"
                    >
                        <el-option
                            v-for="item in brandlist"
                            :key="item.value"
                            :label="item.value"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="商品分类：">
                    <el-cascader
                        clearable=""
                        v-model="gcIds"
                        :options="goodscalssOption"
                        :props="props"
                        @change="finishCange()"
                    ></el-cascader>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="handleClick()"
                        >查询</el-button
                    >
                    <el-button class="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>

            <div class="formControlArea">
                <el-radio-group
                    v-model="activeName"
                    @change="handleClick"
                    style="margin-top: 0px; margin-bottom: 0px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="upper">上架</el-radio-button>
                    <el-radio-button label="lower">下架</el-radio-button>
                    <el-radio-button label="not">未上架</el-radio-button>
                </el-radio-group>
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
                        1、审核通过的商品修改规格售价和成本价
                    </div>
                    <div class="iconSize">
                        2、可通过修改流水查看商品价格修改前后变化及修改人修改时间
                    </div>
                </template>
            </el-alert>
        </div>
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

            <!-- <el-table-column
                    prop="goodsSerial"
                    label="商品编号"
                    width="180"
                    align="center">
            </el-table-column> -->
            <!-- 
            <el-table-column
                    label="图片"
                    align="center">
                <template slot-scope="scope">
                    <div class="goodsImg">
                        <img :src="scope.row.pictureUrl | filterImgUrl"
                             style="width:60px;height:60px;object-fit: contain;" alt=""/>
                    </div>
                </template>
            </el-table-column> -->

            <!-- <el-table-column
                    prop="goodsName"
                    label="商品名称"
                    width="180"
                    align="center">
                <template slot-scope="scope">
                    <div class="towEllipsis">
                         <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                            <span style="color: #4e80db;text-decoration: none; cursor: pointer;"  @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                         </el-tooltip>
                    </div>
                </template>
            </el-table-column>  -->

            <!-- <el-table-column
                    prop="brandName"
                    label="品牌"
                    align="center">
            </el-table-column> -->
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
                                    v-if="scope.row.goodsName.length > 20"
                                    placement="top-start"
                                >
                                    <span
                                        class="goodsNameContent"
                                        @click="previewH5Fn(scope.row)"
                                        >{{ scope.row.goodsName }}</span
                                    >
                                </el-tooltip>
                                <span
                                    v-else
                                    class="goodsNameContent"
                                    @click="previewH5Fn(scope.row)"
                                    >{{ scope.row.goodsName }}</span
                                >
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

            <el-table-column prop="gcName" label="后台分类" align="center">
            </el-table-column>

            <el-table-column prop="storeName" label="店铺名称" align="center">
            </el-table-column>

            <el-table-column label="操作" align="center" width="180">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            addOrEditHandle(scope.$index, scope.row)
                        "
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:price:update')"
                    >
                        修改价格
                    </el-button>
                    <el-button
                        @click.native.prevent="showLogListFn(scope.row.id)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:price:log:view')"
                        >修改记录
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
    import Bread from "@/components/bread";
    import listItem from "./listItem.vue";
    import { setTimeout } from "timers";
    import mixinViewModule from "@/mixins/view-module";
    import { goodsPageUrl } from "@/api/url";
    // import { deletepriceLogUrl } from '@/api/url'
    import addEditData from "./model-add-edit-data";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";
    import {
        goodsUpdateCheckActivity,
        allTreeGoodsclass,
        brandListPull,
    } from "@/api/api.js";

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
                brandlist: [],
                state4: "",
                gcIds: [],
                goodscalssOption: [],
                props: {
                    value: "id",
                    label: "gcName",
                },
                addEditDataVisible: false,
                breaddata: ["商品", "商品管理", "价格管理"],
                dataForm: {
                    goodsShow: "", //上下架状态:0下架;1上架
                },
                activeName: "",
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
            listItem,
            addEditData,
            modelPreviewH5,
        },
        created() {
            // 第一次请求数据
            this.handleClick();
            // 获取分类的所有树形结构
            this.getallTreeGoodsclassFn();
            this.getBrandListPull();
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            // 获取品牌下拉列表
            getBrandListPull(brandName, cb) {
                var obj = {
                    params: {
                        // page:1,
                        // limit:50,
                        brandName: brandName,
                    },
                };
                brandListPull(obj).then((res) => {
                    if (res.code == 200) {
                        // this.brandListOption =  [{"id":"",brandName:"全部"}].concat(res.data);
                        var showData = [];
                        res.data.forEach((item, index) => {
                            item.value = item.brandName;
                            showData.push(item);
                        });
                        // cb(showData)
                        this.brandlist = showData;
                    }
                });
            },
            // // 获取品牌下拉列表
            handleSelect(item) {
                if (item.id) {
                    // this.dataForm.brandId = item.id
                    this.dataForm.brandName = item.brandName;
                } else {
                    this.dataForm.brandName = this.state4;
                }
                // this.dataForm.brandName= item.brandName
                // this.dataForm.brandId= item.id
            },
            // 获取分类的所有树形结构
            getallTreeGoodsclassFn() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOption = res.data;
                    }
                });
            },
            finishCange() {
                console.log(this.gcIds);
                let len = this.gcIds.length;
                if (len != -1) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
            },
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                if (tab == "") {
                    this.dataForm.goodsShow = "";
                } else if (tab == "upper") {
                    this.dataForm.goodsShow = "1";
                } else if (tab == "lower") {
                    this.dataForm.goodsShow = "0";
                } else if (tab == "not") {
                    this.dataForm.goodsShow = "2";
                }
                this.getDataList();
            },
            // reset() {
            //   this.dataForm.goodsShow = "";//商品名称/商品货号
            // },
            showLogListFn(id) {
                this.$emit("showLogListFn", id);
            },
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
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcURL"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
            // 重置
            reset() {
                this.dataForm.goodsName = "";
                this.dataForm.storeName = "";
                this.dataForm.brandName = "";
                this.dataForm.gcId = ""; //分类ID
                this.gcIds = []; //分类ID
                this.state4 = "";
                this.getDataList();
            },
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";

    /* .el-scrollbar__wrap {
              overflow-x: hidden;
            } */
    .el-popper {
        left: 292px !important;
    }

    // .el-table--border {
    //     margin-top: 20px;
    // }

    .el-radio-group {
        margin-top: 20px;
    }

    .orderUerInfo {
        width: 100%;
        height: auto;
        margin-top: 20px;
        border: 1px solid #d1d1d1;
    }

    .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
        white-space: nowrap;
    }

    .el-form--inline .el-form-item {
        margin-right: 76px;
    }

    *::-webkit-scrollbar {
        width: 7px;
        height: 1px;
        background-color: transparent;
    }

    /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    *::-webkit-scrollbar-track {
        background-color: #f0f6ff;
    }

    /*定义滚动条轨道 内阴影+圆角*/
    *::-webkit-scrollbar-thumb {
        background-color: #e3e4e4;
        border-radius: 6px;
    }

    /*定义滑块 内阴影+圆角*/
    .scrollbarHide::-webkit-scrollbar {
        display: none;
    }

    .scrollbarShow::-webkit-scrollbar {
        display: block;
    }

    .el-button {
    }

    .el-input {
        width: 170px;
        height: 40px;
    }

    /deep/ .el-tabs__nav-wrap {
        border-bottom: 2px #efefef dotted;

        &::after {
            content: unset;
        }
    }

    /deep/ .el-tabs__nav-scroll {
        height: 60px;
        line-height: 60px;

        /deep/ .el-tabs__item {
            width: 120px;
            // box-sizing: border-box;
            text-align: center;
        }
    }

    /deep/ .el-tabs__active-bar {
        // width: 120px !important;
    }

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

    .grayBtn {
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

    /deep/ .el-cascader {
        width: 260px;

        /deep/.el-input {
            width: 100% !important;
        }
    }
</style>