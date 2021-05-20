<!-- 常规搜索结果页 不显示大类‘分类集合’ -->
<!-- 活动搜索结果页 不显示大类‘所有集合’ -->

<template>
    <div :class="['search-goods-result', { activity: activity }]">
        <!-- 顶部搜索部分 -->
        <searchPart :borderFullWidth="true"></searchPart>

        <div class="inner">
            <!-- 内容 -->
            <div class="p-content">
                <!-- 全部分类 -->
                <all-categories
                    ref="allCategories"
                    :categoryLoading="categoryLoading"
                    :categoriesVisible="categoriesVisible"
                    :breadcrumbList="breadcrumbList"
                    :brandVOList="brandVOList"
                    :goodsLabelVOList="goodsLabelVOList"
                    :goodsAttrVOList="goodsAttrVOList"
                    @handleCategoryIds="handleCategoryIds"
                    @handleAttrData="handleAttrData"
                ></all-categories>

                <div class="product-wrap">
                    <!-- 商品筛选过滤工具栏 -->
                    <product-filtration
                        ref="filtration"
                        :sortBtnVisible="false"
                        @handleFilterData="handleFilterData"
                        @handlePriceData="handlePriceData"
                    ></product-filtration>

                    <!-- 商品列表 -->
                    <div class="goods-wrap" v-loading="dataLoading">
                        <template v-for="item in goodsVOList">
                            <goods-comp
                                v-if="goodsVOList.length != 0"
                                :key="item.id"
                                :thumbnailVisible="goodsCard.thumbnailVisible"
                                :otherInfoVisible="goodsCard.otherInfoVisible"
                                :goodsId="item.id"
                                :specId="item.specId"
                                :specSellPrice="item.specSellPrice"
                                :goodsLabels="item.goodsLabels"
                                :goodsMainPicture="item.goodsMainPicture"
                                :goodsName="item.goodsName"
                                :goodsSubTitle="item.goodsSubTitle"
                                :storeType="item.storeType"
                                :storeName="item.storeName"
                                :storeId="item.storeId"
                                :evaluateCount="item.evaluateCount"
                                :goodsSaleNum="item.goodsSaleNum"
                            ></goods-comp>
                        </template>
                        <!-- 筛选 - 无数据 -->
                        <filtration-no-data
                            v-if="hasSearchList && goodsVOList.length == 0"
                            :noDataText="filterNoDataText"
                        ></filtration-no-data>
                        <!-- 搜索 - 无数据 -->
                        <search-no-data v-if="!hasSearchList"></search-no-data>
                    </div>

                    <!-- 分页 -->
                    <paging
                        v-if="goodsVOList.length != 0 && totalCount > pageSize"
                        class="paging"
                        :totalCount="totalCount"
                        :pageSize="pageSize"
                        v-model="pageNo"
                    />
                    <div v-else style="height: 50px"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import mixinGoodsList from "~/mixins/goodsList";
    import { mapState, mapMutations, mapActions, store } from "vuex";

    export default {
        name: "searchGoodsResult", // 搜索商品结果
        mixins: [mixinGoodsList],
        data() {
            return {
                goodsCard: {
                    thumbnailVisible: false,
                    otherInfoVisible: true,
                },

                activity: null, // 活动

                breadcrumbList: [], // 重定义面包屑

                categoriesVisible: true, // 是否显示大类
            };
        },

        computed: {
            activitySearchForm() {
                return {
                    ...this.activity, // 活动
                    ...this.searchDataForm, // 搜索字段
                    ...this.filterData, // 过滤字段
                    ...this.priceData, // 价格字段
                };
            },
        },

        watch: {
            $route: {
                immediate: true,
                async handler(newVal, oldVal) {
                    // 活动列表页
                    if (newVal.query.activityType) {
                        this.activity = {
                            activityId: newVal.query.activityId,
                            collectBillType: newVal.query.collectBillType,
                        };
                    }

                    // 搜索关键字
                    if (newVal.query.keyWord !== undefined) {
                        // 重置查询数据
                        // Object.assign(this.$data, this.$options.data())
                        // this.$set(this.categoryData, 'brandIds', [])
                        // 获取商品大类categories
                        await this.handleSearchGoods(this.searchDataForm);
                    }

                    // 面包屑
                    this.handleSearchBreadcrumb(newVal.query);
                },
            },

            activitySearchForm: {
                immediate: true,
                deep: true,
                handler(newVal, oldVal) {
                    let activityType = this.$route.query.activityType;
                    if (activityType) {
                        this.categoriesVisible = false;
                        this.handleDataList(activityType);
                    }
                },
            },
        },

        methods: {
            ...mapActions("searchGoodsResult", ["handleCollectBillsReduce"]),

            // 列表查询判断
            handleDataList(activityType) {
                if (activityType == "manjian") {
                    this.handlefullListOfReduction();
                }

                if (activityType == "coupon") {
                    this.handleCollectBills();
                }
            },

            //满减活动去凑单商品列表页面
            handlefullListOfReduction(query) {
                let params = this.activitySearchForm;
                this.handleCollectBillsReduce(params);
            },

            // 优惠券去凑单商品列表页面
            handleCollectBills() {
                let params = this.activitySearchForm;
                // this.collectBills(params)
            },

            // 搜索结果页面包屑
            handleSearchBreadcrumb(query) {
                let breadcrumbList;

                switch (query.activityType) {
                    case "manjian":
                        breadcrumbList = [
                            { title: "全部结果" },
                            { title: "活动凑单" },
                        ];
                        break;

                    case "coupon":
                        breadcrumbList = [
                            { title: "全部结果" },
                            { title: "优惠券凑单" },
                        ];
                        break;

                    default:
                        breadcrumbList = [
                            { title: "首页", toPath: "/" },
                            { title: "搜索结果", toPath: "" },
                            { title: query.keyWord || "全部", toPath: "" },
                        ];
                }
                this.breadcrumbList = breadcrumbList;
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "~/assets/scss/modules/goods-list-comp.scss";
</style>

<style lang="scss" scoped>
    .search-goods-result {
        background: #f6f6f6;
        width: 100%;
    }

    /deep/ .product-filtration {
        margin-bottom: 10px;
        .ivu-checkbox-wrapper {
            vertical-align: -8px !important;
        }
    }
    /deep/ .basic-style h6 {
        font-size: 13px !important;
    }
    /deep/ .ivu-checkbox {
        margin-left: 6px !important;
    }
    /deep/ .basic-style .handle-btn {
        top: 10px !important;
    }
    /deep/ .goods-comp.column-style {
        margin-right: 10px;
    }
    /deep/ .goods-comp.column-style .ivu-card-body {
        padding: 20px 16px 18px !important;
    }
    /deep/ .goods-wrap[data-v-691257c8] .goods-comp .p-tag {
        top: 20px;
        left: 16px;
    }
    /deep/ .ivu-checkbox-focus {
        box-shadow: none !important;
    }
    /deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
        background-color: #ffffff !important;
    }
    /deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
        &::after {
            border: 1px solid #dd2619 !important;
            border-top: 0 !important;
            border-left: 0 !important;
        }
    }
    // /deep/ .ivu-page .ivu-page-disabled .ivu-icon {
    //   color: #3a3a3a !important;
    // }

    // /deep/ .ivu-page-options-elevator input {
    //   border-radius: 0px !important;
    // }
    // /deep/ .ivu-btn {
    //   border-radius: 0px !important;
    // }
    // /deep/ .ivu-page-item-active {
    //   background: #dd2619 !important;
    //   a {
    //     color: #ffffff !important;
    //   }
    // }
    // /deep/ .paging-comp {
    //   padding: 0px !important;
    // }
    /deep/ .confirm-btn {
        margin-bottom: 2px !important;
    }
    // /deep/ .ivu-page[data-v-ffdbe760] .ivu-page-item {
    //   a {
    //     line-height: 15px;
    //     margin: 4px 0 !important;
    //   }
    // }
    // /deep/ .confirm-btn {
    //   margin-bottom: 10px !important;
    // }
    // /deep/ .ivu-page[data-v-ffdbe760] .ivu-page-item {
    //   a {
    //     line-height: 15px;
    //     margin: 4px 0 !important;
    //   }
    // }
</style>
