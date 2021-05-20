<template>
    <div class="artCart">
        <Dropdown
            placement="bottom-start"
            @on-visible-change="artHandleOpen"
            trigger="hover"
        >
            <div class="search-con-cart" @click="toCart()">
                <img src="/img/common/search/ico-shopcar-on.png" alt="" />
                <img src="/img/common/search/ico-shopcar.png" alt="" />
                <span class="search-con-cart-word">购物车</span>
                <Badge
                    v-if="CartListData && CartListData.num"
                    :count="CartListData && CartListData.num"
                    :class="
                        CartListData.num.toString().length <= 2
                            ? 'oneStyle'
                            : 'twoStyle'
                    "
                >
                    <a href="#" class="demo-badge"></a>
                </Badge>
            </div>
            <!-- v-if="CartListData" -->
            <DropdownMenu slot="list">
                <div class="artUpperTriangle"></div>
                <div
                    class="shopping-cart-null"
                    v-show="CartListData && CartListData.num == 0"
                >
                    <img src="/img/common/search/empty-cart.png" alt="" />
                    <span>购物车中还没有商品，快去选购吧</span>
                </div>
                <!-- 此处代码用于判断显示自适应窗口还是滚动条窗口 -->
                <div
                    class="shopping-cart-list"
                    style="max-height: 315px; height: auto"
                    v-if="
                        visible &&
                        CartListData.list &&
                        CartListData.list.length < 6
                    "
                >
                    <div
                        class="shopping-cart-box"
                        v-for="(item, index) in CartListData.list"
                        :key="index"
                    >
                        <div
                            class="shopping-cart-img"
                            @click.stop="toDetail(item.goodsId, item.specId)"
                            v-lazy-container="{
                                selector: 'img',
                                error: '/img/common/loading/200_200.png',
                                loading: '/img/common/loading/200_200.png',
                            }"
                        >
                            <img :data-src="$imgURL + item.specMainPicture" />
                        </div>
                        <div class="shopping-cart-info">
                            <div
                                style="
                                    width: 242px;
                                    display: flex;
                                    justify-content: space-between;
                                "
                            >
                                <div
                                    class="shopping-cart-title"
                                    @click.stop="
                                        toDetail(item.goodsId, item.specId)
                                    "
                                >
                                    <p class="arttextoverflow wordsName">
                                        {{ item.specName }}
                                    </p>
                                </div>
                                <p class="artmoney arttextoverflow">
                                    <span>￥{{ item.specSavePrice }}</span>
                                    <span>×{{ item.goodsNum }}</span>
                                </p>
                            </div>
                            <div
                                style="
                                    width: 242px;
                                    display: flex;
                                    justify-content: space-between;
                                    margin-top: 8px;
                                "
                            >
                                <div
                                    class="shopping-cart-detail-no arttextoverflow"
                                    v-if="item.specInfo !== null"
                                >
                                    <span class="shopping-cart-text-no">
                                        {{ item.specInfo }}
                                    </span>
                                </div>
                                <div
                                    class="shopping-cart-detail arttextoverflow"
                                    v-else
                                >
                                    <span class="shopping-cart-text">
                                        {{ item.specInfo }}
                                    </span>
                                </div>
                                <div
                                    class="go-to-delete artCursor"
                                    @click="actDetele(item)"
                                >
                                    删除
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 此处代码用于判断显示滚动条窗口还是自适应窗口 -->
                <div
                    class="shopping-cart-list"
                    v-if="
                        visible &&
                        CartListData.list &&
                        CartListData.list.length > 5
                    "
                >
                    <vue-scroll :ops="ops">
                        <div
                            class="shopping-cart-box"
                            v-for="(item, index) in CartListData.list"
                            :key="index"
                        >
                            <div
                                class="shopping-cart-img"
                                @click.stop="
                                    toDetail(item.goodsId, item.specId)
                                "
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    :data-src="$imgURL + item.specMainPicture"
                                />
                            </div>
                            <div class="shopping-cart-info">
                                <div
                                    style="
                                        width: 242px;
                                        display: flex;
                                        justify-content: space-between;
                                    "
                                >
                                    <div
                                        class="shopping-cart-title"
                                        @click.stop="
                                            toDetail(item.goodsId, item.specId)
                                        "
                                    >
                                        <p class="arttextoverflow wordsName">
                                            {{ item.specName }}
                                        </p>
                                    </div>
                                    <p class="artmoney arttextoverflow">
                                        <span>￥{{ item.specSavePrice }}</span>
                                        <span>×{{ item.goodsNum }}</span>
                                    </p>
                                </div>
                                <div
                                    style="
                                        width: 242px;
                                        display: flex;
                                        justify-content: space-between;
                                        margin-top: 8px;
                                    "
                                >
                                    <div
                                        class="shopping-cart-detail-no arttextoverflow"
                                        v-if="item.specInfo !== null"
                                    >
                                        <span class="shopping-cart-text-no">
                                            {{ item.specInfo }}
                                        </span>
                                    </div>
                                    <div
                                        class="shopping-cart-detail arttextoverflow"
                                        v-else
                                    >
                                        <span class="shopping-cart-text">
                                            {{ item.specInfo }}
                                        </span>
                                    </div>
                                    <div
                                        class="go-to-delete artCursor"
                                        @click="actDetele(item)"
                                    >
                                        删除
                                    </div>
                                </div>
                            </div>
                        </div>
                    </vue-scroll>
                </div>
                <!-- <div class="shopping-go-cart" v-show="CartListData && CartListData.num>0"> -->
                <div
                    class="shopping-go-cart"
                    v-if="
                        visible && CartListData.list && CartListData.list.length
                    "
                >
                    <div>
                        <P
                            >共
                            <span style="font-weight: 600">{{
                                CartListData && CartListData.num
                            }}</span>
                            件商品</P
                        >
                    </div>

                    <div class="artCursor">
                        <router-link to="/shoppingCar" style="color: #fff">
                            去购物车
                        </router-link>
                    </div>
                </div>
            </DropdownMenu>
        </Dropdown>
    </div>
</template>
<script>
    import { mapState, mapActions, mapMutations } from "vuex";
    import Cookies from "js-cookie";
    export default {
        name: "indexCart",
        data() {
            return {
                visible: false,
                loginFlag: false, //登录状态
                ops: {
                    vuescroll: {},
                    scrollPanel: {},
                    rail: {
                        background: "#ccc",
                        opacity: 0,
                        size: "10px",
                        specifyBorderRadius: false,
                        gutterOfEnds: null, //轨道距 x 和 y 轴两端的距离
                        gutterOfSide: "0", //距离容器的距离
                        keepShow: false, //是否即使 bar 不存在的情况下也保持显示
                        border: "none", //边框
                    },
                    bar: {
                        hoverStyle: true,
                        onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
                        background: "red", //颜色
                    },
                },
            };
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "CartListData",
            ]),
        },
        mounted() {
            this.loginFlag = Cookies.get("auth") ? true : false;
            this.actCartList();
        },
        methods: {
            ...mapActions("main", [
                "actCartList",
                "actDeleteCart",
                "actLoginCartDelete",
            ]),
            artHandleOpen(e) {
                this.visible = e;
            },
            actDetele(item) {
                //删除按钮
                if (!this.loginFlag) {
                    // 未登录时的删除
                    this.actDeleteCart([item.specId]);
                } else {
                    let obj = {
                        cartId: item.id,
                        delType: 1,
                    };
                    this.actLoginCartDelete(obj);
                }
            },
            toCart() {
                this.$router.push({
                    path: "/shoppingCar",
                });
            },
            //去商品详情
            toDetail(id, specId) {
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: id,
                        specId: specId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
        destroyed() {},
    };
</script>
<style lang="scss" scoped>
    .artCursor {
        cursor: pointer;
    }

    .arttextoverflow {
        white-space: nowrap;
        overflow: hidden;
        word-break: break-all;
        word-wrap: break-word;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
    }

    /deep/ .__rail-is-vertical {
        width: 3px !important;
        background-color: #ccc !important;
    }

    .artCart {
        // margin-left: 10px;
        .search-con-cart {
            width: 132px;
            height: 46px;
            line-height: 46px;
            border-radius: 4px;
            // border: 1px solid #999999;
            border: 1px solid #bcbcbc;
            background-color: #fff;
            font-size: 16px;
            margin-left: 20px;
            // padding-right: 15px;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;

            .ivu-icon-ios-cart-outline {
                margin-right: 6px;
                font-size: 17px;
            }

            .search-con-cart-word {
                margin-left: 8px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #dd2619;
                line-height: 14px;
            }

            img:nth-child(1) {
                display: block;
                width: 14px;
                height: 14px;
            }

            img:nth-child(2) {
                display: none;
                width: 14px;
                height: 14px;
            }

            .ivu-badge {
                width: 16px;
                height: 16px;
                margin-left: 8px;

                /deep/ .ivu-badge-count {
                    top: -1px;
                    border-radius: 8px;
                    height: 17px;
                    line-height: 16px;
                    background: #dd2619;
                    color: #ffffff;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC !important;
                    font-weight: 400;
                }
            }
        }

        /deep/ .search-con-cart:hover {
            // background: rgba(221, 38, 25, 1);
            border: 1px solid rgba(221, 38, 25, 1);
            // color: rgba(255, 255, 255, 1);

            // img:nth-child(1) {
            //     display: none;
            // }

            // img:nth-child(2) {
            //     display: block;
            // }

            // /deep/ .ivu-badge-count {
            //     background: rgba(255, 255, 255, 1);
            //     color: #DD2619;
            //     font-size: 12px;
            //     font-family: PingFangSC-Regular, PingFang SC !important;
            //     font-weight: 400;

            // }

            // .search-con-cart-word {
            //     color: rgba(255, 255, 255, 1);
            // }
        }

        /deep/ .ivu-dropdown {
            position: relative;
        }
        /deep/ .ivu-select-dropdown {
            margin: 0px !important;
            padding: 0px !important;
        }
        /deep/ .ivu-dropdown .ivu-select-dropdown {
            // transform: translateX(-25%);
            position: absolute;
            top: 60px !important;
            left: -52% !important;
            box-shadow: 1px -1px 10px #ccc;
            padding-bottom: 0 !important;

            .ivu-dropdown-menu {
                width: 320px;
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
                }

                .shopping-cart-list {
                    width: 320px;
                    height: 315px;
                    overflow: hidden;
                    padding: 0 5px 0 10px;

                    .shopping-cart-box {
                        display: flex;
                        padding: 10px 10px 10px 0;
                        border-bottom: 1px solid rgba(235, 235, 235, 1);

                        .wordsName {
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #222222;
                            line-height: 14px;
                        }

                        &:hover {
                            .wordsName {
                                color: #dd2619;
                            }
                        }

                        .shopping-cart-img {
                            width: 42px;
                            height: 42px;
                            margin-right: 10px;
                            cursor: pointer;
                        }

                        .shopping-cart-img img {
                            display: block;
                            width: 42px;
                            height: 42px;
                        }

                        .go-to-delete {
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #666666;
                            line-height: 12px;
                        }

                        .go-to-delete:hover {
                            color: #e2270b;
                        }

                        .shopping-cart-title {
                            width: 150px;
                            font-size: 15px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(51, 51, 51, 1);
                            // margin-bottom: 5px;
                            cursor: pointer;
                        }

                        .shopping-cart-detail {
                            margin-top: 17px;
                        }

                        .shopping-cart-detail span {
                            font-size: 13px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(182, 182, 182, 1);
                            margin-top: 6px;
                        }

                        .shopping-cart-detail-no {
                            margin-bottom: 6px;
                        }

                        .shopping-cart-detail-no span {
                            font-size: 12px;
                            font-family: PingFangSC-Light, PingFang SC;
                            font-weight: 300;
                            color: #999999;
                            line-height: 12px;
                        }

                        .artmoney {
                            span:nth-of-type(1) {
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 600;
                                color: #222222;
                                line-height: 12px;
                            }

                            span:nth-of-type(2) {
                                font-size: 13px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: rgba(58, 58, 58, 1);
                                height: 14px;
                                margin-left: 8px;
                                line-height: 13px;
                            }
                        }
                    }
                }

                .shopping-go-cart {
                    height: 45px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 0 10px;

                    div:nth-of-type(1) {
                        width: 200px;

                        p {
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #333333;
                            line-height: 14px;
                        }
                    }

                    div:nth-of-type(2) {
                        width: 64px;
                        height: 30px;
                        text-align: center;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #ffffff;
                        line-height: 30px;
                        background: linear-gradient(
                            90deg,
                            rgba(222, 42, 28, 1) 0%,
                            rgba(255, 78, 3, 1) 100%
                        );
                        border-radius: 15px;
                    }
                }
            }
        }

        .shopping-cart-null {
            padding: 15px;
            display: flex;
            align-items: center;

            span {
                margin-left: 20px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 17px;
            }
        }

        .cart-null-icon {
            font-size: 38px;
            margin-bottom: 15px;
        }
    }

    .oneStyle {
        /deep/ .ivu-badge-count {
            border-radius: 50% !important;
            width: 19px !important;
            padding: 0;
            height: 19px !important;
            min-width: 16px;
            text-align: center;
            line-height: 19px !important;
            top: -2px !important;
        }
    }

    .twoStyle {
        /deep/ .ivu-badge-count {
            border-radius: 8px !important;
            width: 32px !important;
            padding: 0;
            min-width: 16px;
            text-align: center;
        }
    }
</style>
