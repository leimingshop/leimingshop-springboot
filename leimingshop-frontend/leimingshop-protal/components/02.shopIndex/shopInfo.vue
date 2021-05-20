<template>
    <div class="artshopinfo">
        <Poptip placement="bottom" trigger="hover">
            <div class="shop-info" type="parimary">
                <div class="shop-name-box">
                    <div class="shop-name">
                        {{ StoreInfoStoreIdData.storeName }}
                    </div>
                    <div class="shop-four" style="display: flex">
                        <div class="shop-info-other-box">
                            <div class="shop-info-other-word">评价</div>
                            <div class="shop-serve-level">
                                {{
                                    StoreInfoStoreIdData.findEvaluateStoreDTO &&
                                    StoreInfoStoreIdData.findEvaluateStoreDTO
                                        .goodsEvaluate
                                }}<img
                                    src="/img/02.shopIndex/up.png"
                                    alt=""
                                /><img
                                    v-show="false"
                                    src="/img/02.shopIndex/down.png"
                                    alt=""
                                />
                            </div>
                        </div>
                        <div class="shop-info-other-box">
                            <div class="shop-info-other-word">物流</div>
                            <div class="shop-serve-level">
                                {{
                                    StoreInfoStoreIdData.findEvaluateStoreDTO &&
                                    StoreInfoStoreIdData.findEvaluateStoreDTO
                                        .logisticsEvaluate
                                }}<img
                                    src="/img/02.shopIndex/up.png"
                                    alt=""
                                /><img
                                    v-show="false"
                                    src="/img/02.shopIndex/down.png"
                                    alt=""
                                />
                            </div>
                        </div>
                        <div
                            class="shop-info-other-box"
                            style="margin-right: 10px"
                        >
                            <div class="shop-info-other-word">售后</div>
                            <div class="shop-serve-level">
                                {{
                                    StoreInfoStoreIdData.findEvaluateStoreDTO &&
                                    StoreInfoStoreIdData.findEvaluateStoreDTO
                                        .afterSaleEvaluate
                                }}<img
                                    v-show="false"
                                    src="/img/02.shopIndex/up.png"
                                    alt=""
                                /><img
                                    src="/img/02.shopIndex/down.png"
                                    alt=""
                                />
                            </div>
                        </div>
                    </div>
                    <div class="shop-stars">
                        店铺星级
                        <!-- <Rate v-model="valueText" /> -->
                        <div
                            class="art-stars"
                            v-if="StoreInfoStoreIdData.findEvaluateStoreDTO"
                            v-for="(item, index) in 5"
                            :key="index"
                        >
                            <img
                                v-if="
                                    index <
                                    StoreInfoStoreIdData.findEvaluateStoreDTO
                                        .storeLevel
                                "
                                src="/img/02.shopIndex/star1.png"
                                alt=""
                            />
                            <img
                                v-else
                                src="/img/02.shopIndex/star2.png"
                                alt=""
                            />
                        </div>
                    </div>
                </div>

                <!-- <div class="shop-info-other-box">
                    <div class="shop-info-other-word">评价</div>
                    <div class="shop-serve-level">
                        {{StoreInfoStoreIdData.findEvaluateStoreDTO && StoreInfoStoreIdData.findEvaluateStoreDTO.goodsEvaluate }}<img
                            src="/img/02.shopIndex/up.png" alt=""><img v-show="false"
                            src="/img/02.shopIndex/down.png" alt=""></div>
                </div>
                <div class="shop-info-other-box">
                    <div class="shop-info-other-word">物流</div>
                    <div class="shop-serve-level">
                        {{StoreInfoStoreIdData.findEvaluateStoreDTO && StoreInfoStoreIdData.findEvaluateStoreDTO.logisticsEvaluate}}<img
                            src="/img/02.shopIndex/up.png" alt=""><img v-show="false"
                            src="/img/02.shopIndex/down.png" alt=""></div>
                </div>
                <div class="shop-info-other-box" style="margin-right:10px;">
                    <div class="shop-info-other-word">售后</div>
                    <div class="shop-serve-level">
                        {{StoreInfoStoreIdData.findEvaluateStoreDTO && StoreInfoStoreIdData.findEvaluateStoreDTO.afterSaleEvaluate }}<img
                            v-show="false" src="/img/02.shopIndex/up.png" alt=""><img
                            src="/img/02.shopIndex/down.png" alt=""></div>
                </div> -->
            </div>
            <div slot="content">
                <!-- <div style="width:11px;height:11px;background:#ffffff"></div> -->
                <!-- <div class="artUpperTriangle"></div> -->
                <div class="shopping-cart-list">
                    <p class="art-shopping">店铺评分</p>
                    <p class="art-score">
                        评分：<span>{{
                            StoreInfoStoreIdData.findEvaluateStoreDTO &&
                            StoreInfoStoreIdData.findEvaluateStoreDTO
                                .goodsEvaluate
                        }}</span
                        >物流：<span>{{
                            StoreInfoStoreIdData.findEvaluateStoreDTO &&
                            StoreInfoStoreIdData.findEvaluateStoreDTO
                                .logisticsEvaluate
                        }}</span>
                        售后：<span>{{
                            StoreInfoStoreIdData.findEvaluateStoreDTO &&
                            StoreInfoStoreIdData.findEvaluateStoreDTO
                                .afterSaleEvaluate
                        }}</span>
                    </p>
                    <Divider />
                    <p class="art-shopping">店铺介绍</p>
                    <div class="art-con">
                        <span>店铺名称：</span>
                        <p
                            style="font-size：12px;font-family:PingFangSC-Regular,PingFang SC;font-weight:400;color:rgba(102,102,102,1);"
                        >
                            {{ StoreInfoStoreIdData.storeName }}
                        </p>
                    </div>
                    <div class="art-con">
                        <span>开店时间：</span>
                        <p
                            style="
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PindFang SC;
                                font-weight: 400;
                                color: rgba(102, 102, 102, 1);
                            "
                        >
                            {{ StoreInfoStoreIdData.createDate }}
                        </p>
                    </div>
                    <div class="art-con shop-con">
                        <span>店铺简介：</span>
                        <p
                            style="
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PindFang SC;
                                font-weight: 400;
                                color: rgba(102, 102, 102, 1);
                            "
                        >
                            {{ StoreInfoStoreIdData.storeIntro }}
                        </p>
                    </div>
                    <div class="art-con" v-viewer>
                        <span>证件信息：</span>
                        <p class="artImg">
                            <img
                                v-lazy="
                                    $imgURL +
                                    StoreInfoStoreIdData.electronicBusinessLicense
                                "
                                alt=""
                            />
                        </p>
                    </div>
                    <!-- <Divider /> -->
                    <div
                        class="art-collection"
                        v-if="StoreInfoStoreIdData.isFavorite == 0"
                        @click="collectShop(StoreInfoStoreIdData)"
                    >
                        <img
                            v-if="!StoreInfoStoreIdData.isFavorite"
                            src="/img/02.shopIndex/star3.png"
                            alt=""
                        />
                        <img
                            v-else
                            src="/img/02.shopIndex/star1.png"
                            alt=""
                        />收藏店铺
                    </div>
                    <div
                        class="art-collection"
                        v-else-if="StoreInfoStoreIdData.isFavorite == 1"
                        @click="collectShop(StoreInfoStoreIdData)"
                    >
                        <img
                            v-if="!StoreInfoStoreIdData.isFavorite"
                            src="/img/02.shopIndex/star3.png"
                            alt=""
                        />
                        <img
                            v-else
                            src="/img/02.shopIndex/solid-star3.png"
                            alt=""
                        />
                        取消收藏
                    </div>
                </div>
            </div>
        </Poptip>
        <!-- <div class="shop-info-other-box art-Down">
            <div class="shop-info-other-word" style="margin-left:20px">手机逛</div>
            <div class="shop-serve-level" style="margin-left:20px;"><img src="/img/02.shopIndex/Fill Phone.png" alt=""></div>
            <div class="artImg" style="margin-left:20px;"><img v-lazy="'/static/img/01.index/MobileQRCode.png'"></div>
        </div> -->
        <Poptip trigger="hover" placement="bottom">
            <div class="shop-info-other-box art-Down" type="parimary">
                <div class="shop-info-other-word">手机逛</div>
                <div class="shop-serve-level" style="margin-left: 10px">
                    <img src="/img/02.shopIndex/Fill Phone.png" alt="" />
                </div>
            </div>
            <div class="artImg" slot="content" style="height: 110px">
                <img v-lazy="'/img/01.index/MobileQRCode.png'" />
            </div>
        </Poptip>
    </div>
</template>

<script>
    export default {
        name: "shopInfo",
        props: {
            valueText: {
                type: Number,
                required: false,
                default: 3,
            },
            StoreInfoStoreIdData: {
                type: Object,
                required: false,
                default: {},
            },
        },
        data() {
            return {};
        },
        methods: {
            artHandleOpen(e) {
                this.currentShow = e;
            },
            collectShop(data) {
                if (data.isFavorite == 0) {
                    this.$emit("handleCollectShop", "collect");
                //     this.$Message.success("收藏成功");
                } else {
                    this.$emit("handleCollectShop", "cancelCollect");
                //     this.$Message.success("取消收藏成功");
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    .arttextoverflow {
        width: 100%;
        white-space: nowrap;
        overflow: hidden;
        word-break: break-all;
        word-wrap: break-word;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
    }

    .artshopinfo {
        width: 480px;
        height: 40px;
        padding: 0 0px 0 30px;
        border-left: 1px solid #ebebeb;
        display: flex;
        align-items: center;

        /deep/ .ivu-dropdown {
            width: 295px;
            position: relative;
        }

        /deep/ .ivu-dropdown .ivu-select-dropdown {
            // transform: translateX(-25%);
            width: 363px;
            position: absolute;
            box-shadow: 1px -1px 10px #ccc;
            margin: 15px 0;
            padding: 0;
            top: 50px !important;

            .ivu-dropdown-menu {
                width: 360px;
                position: relative;

                .artUpperTriangle {
                    position: absolute;
                    top: -10px;
                    left: 50%;
                    margin-left: -8px;
                    width: 0;
                    height: 0;
                    border-right: 10px solid transparent;
                    border-left: 10px solid transparent;
                    border-bottom: 10px solid #fff;
                    box-shadow: 1px -1px 10px #ccc;
                    background: #e3e3e3;
                }

                /deep/ .shopping-cart-list {
                    width: 360px;
                    min-height: 300px;
                    max-height: 365px;
                    // padding: 20px 30px 20px 29px;
                    padding: 5px !important;

                    .ivu-divider-horizontal {
                        margin: 21px 0 20px;
                    }

                    .art-shopping {
                        margin-bottom: 12px;
                        font-size: 15px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(51, 51, 51, 1);
                        line-height: 15px;
                    }

                    .art-score {
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(76, 76, 76, 1);
                        line-height: 14px;

                        span {
                            margin-right: 20px;
                            font-size: 16px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 600;
                            color: rgba(231, 41, 0, 1);
                            line-height: 13px;
                        }
                    }

                    .art-con {
                        margin-top: 8px;
                        font-size: 12px !important;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(102, 102, 102, 1);
                        line-height: 18px;
                        overflow: hidden;

                        .p {
                            font-size: 12px;
                        }
                        .artImg img {
                            width: 30px;
                            height: 30px;
                        }

                        span {
                            float: left;
                        }

                        p {
                            width: 240px;
                            float: left;
                        }
                    }

                    .shop-con p {
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 2;
                        overflow: hidden;
                    }

                    .art-collection {
                        margin: 0 auto;
                        width: 100px !important;
                        height: 30px !important;
                        background: linear-gradient(
                            90deg,
                            rgba(222, 42, 28, 1) 0%,
                            rgba(255, 78, 3, 1) 100%
                        );
                        border-radius: 40px;
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(255, 255, 255, 1);
                        line-height: 30px !important;
                        text-align: center;
                        cursor: pointer;

                        img {
                            vertical-align: middle;
                            margin-right: 8px;
                            margin-bottom: 5px !important;
                        }
                    }
                }
            }
        }

        .art-Down {
            position: relative;

            .artImg {
                position: absolute;
                top: 110%;
                left: -30px;
                display: none;
                width: 115px;
                height: 115px;
                z-index: 99;
                box-shadow: 1px -1px 10px #ccc;
                border-radius: 3px;

                img {
                    width: 110px;
                    height: 110px;
                    margin: 2px 0 0 1px;
                }
            }

            &:hover > .artImg {
                display: block;
            }
        }
    }

    .artImg img {
        width: 110px;
        height: 110px;
    }

    .shop-info {
        // width: 430px;
        display: flex;
        align-items: center;

        /deep/ .shop-name-box {
            // height: 40px;

            .shop-name {
                // width: 150px;
                word-break: break-all;

                // display: -webkit-box;
                -webkit-line-clamp: 1;
                -webkit-box-orient: vertical;
                overflow: hidden;
                font-size: 15px;
                font-weight: 500;
                color: #222222;
                line-height: 17px;
                flex-flow: nowrap;
                width: 300px;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .shop-four {
                display: flex;
                margin: 10px 0;
            }
            .shop-stars {
                display: flex;
                align-items: center;
                flex-wrap: nowrap;
                // margin: 8px 0 0 0;
                font-size: 14px;
                font-weight: 400;
                color: #999999;
                line-height: 15px;

                .art-stars {
                    margin-left: 3px;

                    img {
                        margin-right: 2px;
                    }
                }

                /deep/ .ivu-rate-star {
                    width: 14px;
                    height: 14px;
                    margin-right: 2px;
                }

                /deep/ .ivu-rate-star-full:before,
                /deep/ .ivu-rate-star:before {
                    font-size: 16px;
                    color: #f5a623;
                }
            }
        }
    }

    .shop-info-other-box {
        height: 13px;
        line-height: 13px;
        margin: 0 0 0 20px;
        display: flex;
        text-align: center;
        &:nth-child(1) {
            margin: 0px;
        }

        .shop-info-other-word {
            font-size: 14px;
            font-weight: 400;
            color: #999999;
            line-height: 14px;
        }

        .shop-serve-level {
            // margin: 10px 0 0 0;
            font-size: 15px;
            font-weight: 500;
            color: #dd2619;
            line-height: 14px;
            margin-left: 9px;
        }

        img {
            margin-left: 3px;
        }
    }

    /deep/ .ivu-poptip-inner {
        // width: 120px;
        // height: 120px;

        .ivu-poptip-body {
            padding: 5px;

            .ivu-poptip-body-content {
                // width: 110px;
                // height: 110px;
                overflow: hidden;
            }
        }
    }

    /deep/ .ivu-poptip-popper {
        min-width: 110px !important;
    }
    /deep/ .ivu-poptip-content {
        // width: 360px;
        .ivu-poptip-inner {
            // width: 360px;
            // height: 300px;
            .ivu-poptip-body {
                // padding: 20px;
                .ivu-divider-horizontal {
                    margin: 9px 0;
                }
                /deep/ .shopping-cart-list {
                    // width: 360px;
                    max-height: 365px;
                    // padding: 20px 30px 20px 29px;

                    // .ivu-divider-horizontal {
                    //     margin: 21px 0 20px;
                    // }
                    padding: 15px 15px 7px !important;
                    .art-shopping {
                        margin-bottom: 12px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(51, 51, 51, 1);
                        line-height: 14px;
                    }

                    .art-score {
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(76, 76, 76, 1);
                        line-height: 14px;

                        span {
                            margin-right: 20px;
                            font-size: 16px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 600;
                            color: rgba(231, 41, 0, 1);
                            line-height: 13px;
                        }
                    }

                    .art-con {
                        margin-top: 8px;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(86, 86, 86, 1);
                        line-height: 18px;
                        overflow: hidden;

                        .artImg {
                            // height: 110px !important;
                        }
                        img {
                            width: 30px;
                            height: 30px;
                        }

                        span {
                            float: left;
                        }

                        p {
                            width: 240px;
                            float: left;
                        }
                    }

                    .shop-con p {
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 2;
                        overflow: hidden;
                    }

                    .art-collection {
                        margin: 23px auto 0;
                        width: 100px;
                        height: 30px;
                        background: linear-gradient(
                            90deg,
                            rgba(222, 42, 28, 1) 0%,
                            rgba(255, 78, 3, 1) 100%
                        );
                        border-radius: 40px;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(255, 255, 255, 1);
                        line-height: 30px;
                        text-align: center;
                        cursor: pointer;

                        img {
                            vertical-align: middle;
                            margin-right: 8px;
                            margin-bottom: 5px;
                        }
                    }
                }
            }
        }
    }
    /deep/ .ivu-poptip {
        cursor: pointer;
    }
</style>
