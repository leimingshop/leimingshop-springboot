<template>
    <div class="product-recommend">
        <div class="title">
            <h2>店铺推荐</h2>
            <ButtonGroup>
                <Button
                    :disabled="tabIndex == 0"
                    @click.native="handleChange(-1)"
                ></Button>
                <Button
                    :disabled="tabIndex == carouselItemNum - 1"
                    @click.native="handleChange(1)"
                ></Button>
            </ButtonGroup>
        </div>

        <!-- 商品 -->
        <div class="goods-wrap" v-if="recommendList.length != 0">
            <Carousel
                ref="carousel"
                v-model="tabIndex"
                dots="none"
                arrow="never"
            >
                <CarouselItem
                    v-for="(ITEM, INDEX) in carouselItemNum"
                    :key="INDEX"
                >
                    <goods-comp
                        v-for="(item, index) in recommendList[INDEX]"
                        :key="item.id"
                        :thumbnailVisible="goodsCard.thumbnailVisible"
                        :otherInfoVisible="goodsCard.otherInfoVisible"
                        :goodsId="item.id"
                        :specId="item.specId"
                        :specSellPrice="item.specSellPrice"
                        :goodsLabels="item.labelName"
                        :goodsMainPicture="item.goodsMainPicture"
                        :goodsName="item.goodsName"
                        :goodsSubTitle="item.goodsSubTitle"
                        @click="handleGoodsTab(item.id, item.specId)"
                    ></goods-comp>
                </CarouselItem>
            </Carousel>
        </div>
    </div>
</template>

<script>
    import goodsComp from "./goodsComp";

    export default {
        name: "productRecommend", // 店铺推荐

        data() {
            return {
                tabIndex: 0,
                goodsCard: {
                    thumbnailVisible: false,
                    otherInfoVisible: false,
                },
            };
        },

        props: {
            recommendList: {
                type: Array,
                default: () => [],
            },
            carouselItemNum: {
                type: Number,
                default: 0,
            },
        },

        watch: {},

        components: {
            goodsComp,
        },

        mounted() {},

        methods: {
            // 切换轮播图回调
            handleChange(type) {
                if (type == -1 && this.tabIndex == 0) return;
                if (type == 1 && this.tabIndex == this.carouselItemNum - 1) return;
                this.$refs.carousel.arrowEvent(type);
            },

            // 点击商品回调
            handleGoodsTab(goodsId, specId) {
                this.$router.push({
                    path: "/goodsDetails",
                    query: { goodsId, specId },
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .product-recommend {
        width: 1200px;
        min-height: 300px;
        margin: 0 auto;
        .ivu-row {
            white-space: nowrap;
        }
        .ivu-col {
            display: inline-block;
        }
    }

    .title {
        height: 96px;
        line-height: 96px;
        h2 {
            font-size: 36px;
            color: #3a3a3a;
            float: left;
        }
        .ivu-btn-group {
            margin: 18px 0;
            float: right;
            button {
                width: 26px;
                height: 26px;
                padding: 0;
                border-radius: 4px !important;
                font-size: 16px;
                background: url("/img/02.shopIndex/02-01.shopProClassification/arrow-left.png")
                    no-repeat center;
                background-size: 40% auto;
                background-color: #ffffff;
                transition: background 0.3s;
                &:last-child {
                    transform: rotate(180deg);
                    margin-left: 5px;
                }
                &:not([disabled]):hover {
                    background: url("/img/02.shopIndex/02-01.shopProClassification/arrow-left-hover.png")
                        no-repeat center;
                    background-size: 40% auto;
                    background-color: #dd2619;
                }
                &[disabled] {
                    background-color: #e8e8e8;
                    border-color: #e8e8e8;
                    opacity: 0.5;
                }
            }
        }
    }

    .ivu-carousel {
        width: 100%;
        /deep/ .goods-comp {
            width: 232px;
            float: left;
            margin-right: 10px;
            .p-tag {
                top: 20px;
                left: 16px;
            }
            .p-content {
                flex-direction: column-reverse;
                padding: 0px !important;
            }
            &:nth-of-type(5n) {
                margin-right: 0;
            }
            &:hover {
                border-color: #dd2619;
            }
        }
    }
    /deep/.goods-comp.column-style .ivu-card-body {
        padding: 20px 16px 18px !important;
    }
    /deep/ .p-text .p-name {
        margin-top: 20px !important;
    }
    /deep/ .p-price {
        margin-top: 10px !important;
    }
</style>
