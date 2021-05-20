<template>
    <div class="similarRecommendation-con">
        <div style="width: 1208px; margin: 0 auto; position: relative">
            <div class="title">
                <div>商品推荐</div>
            </div>

            <div>
                <Carousel
                    v-model="value1"
                    :loop="false"
                    :arrow="arrowShow"
                    dots="none"
                    @on-change="changeList"
                >
                    <CarouselItem
                        v-for="itemNumber in recommendTotalPage"
                        :key="itemNumber"
                    >
                        <div class="demo-carousel" style="height: 330px">
                            <div
                                class="good"
                                @click.stop="
                                    toDetail(item.goodsId, item.specId)
                                "
                                v-for="(item, index) in recommendList"
                                :key="index"
                            >
                                <div class="img">
                                    <img
                                        :src="$imgURL + item.specMainPicture"
                                        width="161"
                                        alt
                                    />
                                </div>
                                <div class="good-name">
                                    {{ item.goodsName }}
                                </div>
                                <div class="good-info">
                                    {{ item.goodsSubTitle }}
                                </div>
                                <div class="good-price" style="font-size: 0px">
                                    <!-- <span class="good-price-icon">￥</span> -->

                                    <span>
                                        <span
                                            class="good-price-icon"
                                            style="font-size: 14px"
                                            >￥</span
                                        >
                                        <span style="font-size: 20px">{{
                                            parseFloat(item.specSellPrice)
                                                .toFixed(2)
                                                .split(".")[0]
                                        }}</span>
                                    </span>
                                    <span style="font-size: 16px">.</span>
                                    <span style="font-size: 12px">{{
                                        parseFloat(item.specSellPrice)
                                            .toFixed(2)
                                            .split(".")[1]
                                    }}</span>

                                    <!-- <span class="good-price-icon">.00</span> -->
                                </div>
                            </div>
                        </div>
                    </CarouselItem>
                </Carousel>
            </div>
        </div>
    </div>
</template>

<script>
    import { CartRecommend } from "@/api/api_04.shoppingCar.js";

    // import { mapState, mapActions, mapMutations } from 'vuex';//1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        data() {
            return {
                value1: 0,
                recommendList: [],
                recommendTotalPage: "",
                page: 1,
            };
        },
        computed: {
            arrowShow: function () {
                return this.recommendList.length ? "always" : "never";
            },
        },
        created() {},
        mounted() {
            this.getRecommend(this.page);
        },
        methods: {
            getRecommend(page) {
                let obj = {
                    page: page,
                    limit: 5,
                };
                CartRecommend(obj)
                    .then((res) => {
                        console.log(res);
                        this.recommendList =
                            res.data.list === null ? [] : res.data.list;
                        this.recommendTotalPage = Math.ceil(
                            res.data.total / obj.limit
                        );
                        // this.recommendTotalPage = 5;
                    })
                    .catch((err) => console.log(err));
            },
            //切换幻灯片
            changeList(e, index) {
                // if(index>this.recommendTotalPage){
                //     index = 1
                // }
                index = index + 1;
                console.log("当前下标", e);
                this.getRecommend(index);
            },
            //去商品详情
            toDetail(id, specId) {
                this.$router.push({
                    path: "/goodsDetails",
                    query: { goodsId: id, specId: specId },
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .similarRecommendation-con {
        background: rgba(246, 246, 246, 1);
    }
    .title {
        height: 28px;
        font-size: 36px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: bold;
        color: rgba(51, 51, 51, 1);
        line-height: 28px;
        padding: 43px 0 34px 0px;
        margin: 0 0 20px 0;
        display: flex;
        justify-content: space-between;
        padding-left: 8px;
    }
    .good {
        width: 232px;
        height: 322px;
        padding: 0 16px;
        background: rgba(255, 255, 255, 1);
        border: 1px solid #f5f5f5;
        margin: 0 10px 0 0;
        cursor: pointer;
        position: relative;
        top: 0px;
        left: 0px;
        // border-radius: 4px;
        &:last-child {
            margin-right: 0;
        }
        &:first-child {
            margin-left: 4px;
        }
        &:hover {
            // border: none;
            border-color: transparent;
            box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
            .good-name {
                color: rgb(221, 38, 25);
            }
        }
        /deep/ .ivu-carousel {
            height: 400px !important;
        }
        // &:first-child:hover {
        //     margin-left: 0;
        // }
        // &:hover{
        //     box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
        //     border-color: #DD2619;
        // }
    }
    .img {
        width: 200px;
        height: 200px;
        margin: 20px 0 17px 0;
        text-align: center;
    }
    .good-name {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1; //（行数）
        -webkit-box-orient: vertical;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: #333333;
        line-height: 14px;
    }
    .good-info {
        margin: 8px 0 10px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1; //（行数）
        -webkit-box-orient: vertical;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        line-height: 12px;
    }
    .good-price {
        font-size: 20px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        line-height: 20px;
    }
    .good-price-icon {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(221, 38, 25, 1);
    }
    .swiper-slide,
    .demo-carousel {
        display: flex;
        // justify-content: center;
        border-right: none;
        padding: 0 8px;
        img {
            width: 100%;
            height: 100%;
        }
    }
    .demo-carousel {
        padding: 0px;
    }
    /deep/ .ivu-carousel-list {
        top: -26px;
        left: -4px;
    }
    /deep/ .ivu-carousel-arrow.left {
        /* width: 20px; */
        /* right: 16px; */
        width: 26px;
        height: 26px;
        background: rgba(255, 255, 255, 1);
        color: #666666;
        border-radius: 4px;
        position: relative;
        top: -20px;
        left: 1142px;
        font-size: 16px;
        border: 1px solid #ccc;
        &:hover {
            box-shadow: 0 1px 4px rgba(54, 54, 54, 0.2);
            background-color: rgba(221, 38, 25, 1);
            color: #fff;
            border-color: transparent;
        }
    }
    /deep/ .ivu-carousel-arrow.right {
        /* width: 20px; */
        /* right: 16px; */
        width: 26px;
        height: 26px;
        color: #666666;
        background: rgba(255, 255, 255, 1);
        border-radius: 4px;
        position: relative;
        top: -386px;
        left: 1175px;
        font-size: 16px;
        border: 1px solid #ccc;
        // display: flex;
        // justify-content: flex-end;
        &:hover {
            box-shadow: 0 1px 4px rgba(54, 54, 54, 0.2);
            background-color: rgba(221, 38, 25, 1);
            color: #fff;
            border-color: transparent;
        }
    }
    .ivu-carousel-item {
        padding-top: 10px;
    }
</style>
