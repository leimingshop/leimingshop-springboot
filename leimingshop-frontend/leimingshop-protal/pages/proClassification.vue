<template>
    <div class="pro-classification">
        <!-- 顶部搜索部分 -->
        <searchPart :borderFullWidth="true"></searchPart>

        <div class="inner">
            <!-- 内容 -->
            <div class="p-content">
                <!-- 全部分类 -->
                <all-categories
                    ref="allCategories"
                    :categoryLoading="categoryLoading"
                    :breadcrumbList="customBreadcrumb"
                    :goodsClassVOList="goodsClassVOList"
                    :brandVOList="brandVOList"
                    :goodsLabelVOList="goodsLabelVOList"
                    :goodsAttrVOList="goodsAttrVOList"
                    @handleClassifyList="handleClassifyList"
                    @handleCategoryIds="handleCategoryIds"
                    @handleAttrData="handleAttrData"
                ></all-categories>

                <div class="product-wrap">
                    <!-- 商品筛选过滤工具栏 -->
                    <product-filtration
                        v-if="hasSearchList"
                        ref="filtration"
                        @handleFilterData="handleFilterData"
                        @handlePriceData="handlePriceData"
                        @hanldeChangeStyle="hanldeChangeStyle"
                    ></product-filtration>

                    <!-- 商品列表 -->
                    <div class="goods-wrap" v-loading="dataLoading">
                        <goods-comp
                            v-if="goodsVOList.length != 0"
                            v-for="item in goodsVOList"
                            :key="item.id"
                            :sortMethod="goodsCard.sortMethod"
                            :thumbnailVisible="goodsCard.thumbnailVisible"
                            :otherInfoVisible="goodsCard.otherInfoVisible"
                            :goodsId="item.id"
                            :specId="item.specId"
                            :specSellPrice="item.specSellPrice"
                            :goodsLabels="item.goodsLabels"
                            :goodsMainPicture="item.goodsMainPicture"
                            :goodsName="item.goodsName"
                            :goodsSubTitle="item.goodsSubTitle"
                        ></goods-comp>

                        <!-- 筛选 - 无数据 -->
                        <filtration-no-data
                            v-if="!hasSearchList"
                            :noDataText="filterNoDataText"
                        ></filtration-no-data>

                        <!-- 搜索 - 无数据 -->
                        <!-- <search-no-data></search-no-data> -->
                    </div>

                    <!-- 分页 -->
                    <paging
                        v-if="goodsVOList.length != 0 && totalCount > pageSize"
                        class="paging"
                        :totalCount="totalCount"
                        :pageSize="pageSize"
                        v-model="pageNo"
                    />
                    <div v-else style="height: 60px"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import mixinGoodsList from "@/mixins/goodsList";
    import { mapState, mapActions } from "vuex";

    export default {
        name: "proClassification", // 商品分类列表页
        mixins: [mixinGoodsList],
        head() {
            return {
                title: "商品分类",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        data() {
            return {
                goodsCard: {
                    thumbnailVisible: false,
                    otherInfoVisible: false,
                    sortMethod: "column",
                },
                customBreadcrumb: [], // 重定义面包屑
            };
        },

        computed: {
            ...mapState("goodsClassification", ["breadcrumbList"]),
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    if (newVal) {
                        // 查询子集分类
                        this.handleClassifyList(newVal.query.classId);

                        // 查询商品大类
                        let gcIds = newVal.query.classId
                            ? [newVal.query.classId]
                            : undefined;
                        let params = {
                            gcIds,
                            ...this.searchDataForm,
                        };
                        this.handleSearchGoods(params);
                    }
                },
            },

            breadcrumbList: {
                immediate: true,
                async handler(newVal, oldVal) {
                    await this.$nextTick();
                    this.handleCustomBreadcrumbList(this.$route, newVal);
                },
            },
        },

        created() {},

        mounted() {},

        methods: {
            ...mapActions("goodsClassification", ["handleClassifyList"]),

            // 本页点击分类面包屑
            handleCustomBreadcrumbList(route, breadcrumb) {
                let routeQuery = this.$route.query;

                // 如果查询数据来源于当前页，处理面包屑
                if (routeQuery.dataSource == "currentPage") {
                    // 状态: 首页 / 一级分类
                    if (breadcrumb.length == 2) {
                        this.customBreadcrumb = breadcrumb;
                    } else {
                        let tempList = [];
                        for (let item of breadcrumb) {
                            if (
                                item.classId == routeQuery.classId &&
                                this.$refs["allCategories"]
                            ) {
                                this.$refs[
                                    "allCategories"
                                ].category[0].selectTags = [item.title];
                            } else {
                                tempList.push(item);
                            }
                        }

                        this.customBreadcrumb = tempList;
                    }
                } else {
                    this.customBreadcrumb = breadcrumb;
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/goods-list-comp.scss";
</style>

<style lang="scss" scoped>
    .pro-classification {
        background: #f6f6f6;
        width: 100%;
    }
</style>
