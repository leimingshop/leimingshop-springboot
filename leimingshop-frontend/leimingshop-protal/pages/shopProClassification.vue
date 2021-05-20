<template>
    <div class="shop-classification">
        <!-- 店铺公共头部 -->
        <common-shop-head
            :StoreInfoStoreIdData="StoreInfoStoreIdData"
        ></common-shop-head>

        <!-- 分割线 & 记录位置 不可移除 -->
        <div class="split-line"></div>

        <!-- 全部分类 -->
        <all-categories
            ref="allCategories"
            :categoryLoading="categoryLoading"
            :isMultipleChoice="false"
            :breadcrumbVisible="false"
            :categoryAlwaysVisible="true"
            :goodsClassVOList="goodsClassVOList"
            :shopClassifyList="shopClassifyList"
            :brandVOList="brandVOList"
            :goodsLabelVOList="goodsLabelVOList"
            :goodsAttrVOList="goodsAttrVOList"
            @handleCategoryIds="handleCategoryIds"
            @handleResetSecondClassify="handleResetSecondClassify"
            @handleAttrData="handleAttrData"
        ></all-categories>

        <div
            v-if="
                !(
                    shopClassifyList &&
                    shopClassifyList.length == 0 &&
                    !hasSearchList
                )
            "
            class="product-wrap"
        >
            <!-- 宝贝分类 -->
            <product-categories
                ref="productCategories"
                :shopClassifyList="shopClassifyList"
                @handleSelectFirstClass="handleSelectFirstClass"
            ></product-categories>

            <!-- 商品列表 -->
            <div class="product-inner">
                <!-- 商品筛选过滤工具栏 -->
                <product-filtration
                    ref="filtration"
                    @handleFilterData="handleFilterData"
                    @handlePriceData="handlePriceData"
                    @hanldeChangeStyle="hanldeChangeStyle"
                ></product-filtration>

                <!-- 商品 -->
                <div class="goods-wrap" v-loading="dataLoading">
                    <Row :gutter="goodsCard.gutter">
                        <Col
                            v-for="(item, index) in goodsVOList"
                            :key="index"
                            :span="goodsCard.span"
                            style="padding: 0px"
                        >
                            <goods-comp
                                :sortMethod="goodsCard.sortMethod"
                                :thumbnailVisible="goodsCard.thumbnailVisible"
                                :otherInfoVisible="goodsCard.otherInfoVisible"
                                :thumbnailWidth="goodsCard.thumbnailWidth"
                                :goodsId="item.id"
                                :specId="item.specId"
                                :specSellPrice="item.specSellPrice"
                                :goodsLabels="item.goodsLabels"
                                :goodsMainPicture="item.goodsMainPicture"
                                :goodsName="item.goodsName"
                                :goodsSubTitle="item.goodsSubTitle"
                                :goodsPicList="item.goodsPicList"
                            ></goods-comp>
                        </Col>
                    </Row>

                    <!-- 分页 -->
                    <paging
                        v-if="goodsVOList.length !== 0 && totalCount > pageSize"
                        class="paging"
                        :totalCount="totalCount"
                        :pageSize="pageSize"
                        v-model="pageNo"
                        @handlePageChange="handlePageChange"
                    />
                    <div v-else style="height: 60px"></div>

                    <!-- 筛选 - 无数据 -->
                    <filtration-no-data
                        v-if="goodsVOList.length == 0 && !dataLoading"
                        :noDataText="filterNoDataText"
                    ></filtration-no-data>
                </div>
            </div>
        </div>

        <!-- 搜索 - 无数据 -->
        <search-no-data v-else></search-no-data>
        <!-- 店铺推荐 -->
        <product-recommend
            v-if="recommendList && recommendList.length != 0"
            v-loading="recommendLoading"
            :recommendList="recommendList"
            :carouselItemNum="carouselItemNum"
        ></product-recommend>
    </div>
</template>

<script>
    import mixinGoodsList from "@/mixins/goodsList";
    import commonShopHead from "@/components/02.shopIndex/commonShopHead";
    import productCategories from "@/components/02.shopIndex/02-01.shopProClassification/productCategories";
    import productRecommend from "@/components/02.shopIndex/02-01.shopProClassification/productRecommend";
    import { mapState, mapActions } from "vuex";
    import $ from "jquery";

    export default {
        name: "shopClassification", // 店铺分类列表页
        mixins: [mixinGoodsList],

        head() {
            return {
                title: "店铺 - 商品分类",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        props: {
            StoreInfoStoreIdData: {
                type: Object,
                required: false,
                default: {},
            },
        },

        data() {
            return {
                goodsCard: {
                    thumbnailVisible: true,
                    otherInfoVisible: false,
                    sortMethod: "column",
                    span: 6, // 栅格占位
                    gutter: 10, // 栅格间距
                    thumbnailWidth: 34, // 单个缩略图宽度
                },
                scrollY: 0, //当前滚动条位置
            };
        },

        components: {
            commonShopHead,
            productCategories,
            productRecommend,
        },

        computed: {
            ...mapState("shopProClassification", [
                "shopClassifyList",
                "recommendList",
                "recommendLoading",
                "carouselItemNum",
            ]),
        },

        watch: {
            $route: {
                immediate: true,
                async handler(newVal, olaVal) {
                    let storeId = this.$route.query.storeId;
                    if (!storeId) return;

                    await this.$nextTick();

                    // 自动定位页面位置
                    this.handleScroll();

                    // 店铺商品分类
                    this.handleStoreClass(storeId);

                    // 获取商品大类，店铺列表页面不带关键字搜索
                    let searchDataForm = this.searchDataForm;
                    // searchDataForm.keyword = ''
                    this.handleSearchGoods(searchDataForm);
                },
            },

            shopClassifyList: {
                immediate: true,
                async handler(newVal, olaVal) {
                    console.log(2233445566);
                    let autoFixed = this.$route.query.autoFixed == undefined;
                    if (newVal && newVal.length != 0 && autoFixed) {
                        this.handleScroll();
                    }
                },
            },
        },

        created() {
            let storeId = this.$route.query.storeId;
            if (!storeId) return;

            // 店铺推荐商品接口
            this.handleGoodsRecommend(storeId);
        },

        mounted() {
            var that = this;
            // 点击分类，路由变动会触发滚动条回到顶部，所以需要记录位置
            $(window).on("scroll", function () {
                that.scrollY = $(window).scrollTop();
            });

            this.$once("hook:destroyed", () => $(window).off("scroll"));
        },

        methods: {
            ...mapActions("shopProClassification", [
                "handleStoreClass",
                "handleGoodsRecommend",
            ]),

            // 切换商品列表样式
            hanldeChangeStyle(mode) {
                let span; // 栅格占位
                let gutter; // 栅格间距
                let thumbnailWidth; // 单个缩略图宽度

                switch (mode) {
                    case "row":
                        span = 24;
                        gutter = 0;
                        thumbnailWidth = 20;
                        break;

                    case "column":
                        span = 6;
                        gutter = 10;
                        thumbnailWidth = 50;
                        break;
                }

                this.goodsCard = {
                    sortMethod: mode,
                    span,
                    gutter,
                    thumbnailWidth,
                };
            },

            // 定位
            async handleScroll() {
                // 不自动定位
                if (this.$route.query.autoFixed == "false") {
                    window.scroll(0, this.scrollY);
                    return;
                }

                // 自动定位
                if ($(".split-line").length != 0) {
                    if ($(".artCupping").css("position") == "fixed") {
                        var tempTop =
                            $(".split-line").offset().top -
                            $(".mall-header").height();
                    } else {
                        var tempTop =
                            $(".split-line").offset().top -
                            $(".header").height() -
                            $(".nav-part").height();
                    }

                    $("html, body").animate(
                        {
                            scrollTop: tempTop,
                        },
                        350
                    );
                }
            },

            // 切换分页回调
            handlePageChange(val) {
                var tempTop =
                    $(".split-line").offset().top - $(".mall-header").height();
                $("html, body").animate(
                    {
                        scrollTop: tempTop,
                    },
                    350
                );
            },

            // 选中侧边栏二级分类
            handleSelectFirstClass(val) {
                // 默认选中上方一级分类
                var tempArr = val ? [val] : [];
                this.$refs.allCategories.category[0].selectTags = tempArr;
                // 为防止错乱，清空一级分类，只需二级分类就可查出商品
                this.handleResetOtherCategories();
            },

            // 选中上方一级分类
            handleResetSecondClassify(val) {
                // 清空选中的侧边栏二级分类
                this.$refs.productCategories.selectVal = "";
                // 展开对应的子集分类 （手风琴模式）
                // let tempArr = this.shopClassifyList
                // tempArr.forEach((ITEM, INDEX) => {
                //     if (ITEM.id == val) {
                //         this.$refs.productCategories.openNames = [`${INDEX}`]
                //     }
                // })

                this.$router.replace({
                    path: "/shopProClassification",
                    query: {
                        storeId: this.$route.query.storeId,
                        storeFirstGcIds: val,
                        autoFixed: "false",
                        t: Date.now(),
                    },
                });
                this.handleResetOtherCategories();
            },

            // 点击分类清空其他已选类别
            handleResetOtherCategories() {
                // 其他类别已选属性名称
                let categoriesClone = this.$refs.allCategories.$options.data();
                this.$set(
                    this.$refs.allCategories.category,
                    1,
                    categoriesClone.category[1]
                );
                this.$set(
                    this.$refs.allCategories.category,
                    2,
                    categoriesClone.category[2]
                );
                this.$refs.allCategories.filtrate = categoriesClone.filtrate;
            },
        },
    };
</script>

<style lang="scss" scoped>
    .shop-classification {
        padding-bottom: 80px;
        /deep/ .breadcrumb {
            margin: 0 auto;
            .ivu-tag {
                margin: 20px 0 20px 15px;
            }
        }

        /deep/ .product-filtration {
            margin: 0 0 10px 0;
        }

        /deep/ .filtration-no-data {
            width: 100%;
            float: right;
        }

        /deep/ .search-no-data {
            width: 1200px;
            margin: 0 auto;
        }
    }

    .product-wrap {
        width: 1200px;
        margin: 10px auto 0;
        display: flex;
        justify-content: space-between;
    }

    /deep/ .product-categories {
        margin-right: 10px;
    }

    .product-inner {
        flex: 1;
        width: 1030px;
    }

    .goods-wrap {
        width: 100%;
        min-height: 586px;
        margin: 10px 0 0 0;
        padding: 0 0 30px;
        background: #ffffff;
        box-sizing: border-box;
        /deep/ .goods-comp {
            border: 0;
            margin-bottom: 10px;
            &:hover {
                box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.1);
            }
        }
        .ivu-row {
            margin-left: 0px !important;
            margin-right: 0px !important;
        }
    }
    /deep/ .p-text {
        margin-top: 0px !important;
    }
    /deep/ .p-text .p-desc {
        margin-top: 8px;
    }
    /deep/ .p-content {
        padding: 0px !important;
    }
    .split-line {
        width: 100%;
        height: 1px;
        background: #efefef;
    }
    /deep/ .title {
        height: 65px !important;
        line-height: 65px !important;
        h2 {
            font-size: 26px !important;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600 !important;
            color: rgba(58, 58, 58, 1) !important;
        }
    }
    /deep/ .ivu-input-number-focused {
        box-shadow: none !important;
    }

    /deep/ .ivu-input-number {
        &:hover {
            border-color: #b7b7b7 !important;
        }
    }
    /deep/ .ivu-col-span-6 {
        width: 24% !important;
        margin: 0 5px !important;
    }
    /deep/ .ivu-card-body .p-img {
        width: 220px;
        height: 220px;
    }
    /deep/ .filtrate button {
        color: #666666;
    }
</style>
