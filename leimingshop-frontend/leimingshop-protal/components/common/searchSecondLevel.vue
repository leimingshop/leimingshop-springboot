<template>
    <div>
        <!-- 二级导航 -->
        <div :class="{ artCupping: xiding && isNeedCupping }">
            <div class="mall-header">
                <div class="mall-header-contain">
                    <img
                        @click="$router.push('/')"
                        class="artCursor"
                        :src="$imgURL + IndexSiteData.logoNow"
                    />
                    <div v-if="!isShop" class="artclassification">
                        <Select v-model="model1" placeholder="全部商品">
                            <Option
                                v-for="item in searchType1"
                                :value="item.value"
                                :key="item.value"
                            >
                                {{ item.label }}</Option
                            >
                        </Select>
                    </div>
                    <shopInfo
                        v-else
                        :StoreInfoStoreIdData="StoreInfoStoreIdData"
                        @handleCollectShop="handleCollectShop"
                    ></shopInfo>
                    <div class="search-box" v-on:keyup.13="actToDetail">
                        <Select
                            v-model="searchTypeVal"
                            size="small"
                            placeholder=""
                        >
                            <Option
                                v-for="item in searchType"
                                :value="item.value"
                                :key="item.value"
                                >{{ item.label }}
                            </Option>
                        </Select>

                        <div class="line"></div>
                        <AutoComplete
                            v-model="searchVal"
                            :data="IndexSelectOptions"
                            @on-search="handleSearch1"
                            placeholder="请输入商品名"
                        ></AutoComplete>
                        <input type="text" class="hidden" />
                        <input type="password" class="hidden" />
                        <!-- 解决Chrome浏览器自动填充input的问题 -->
                        <input
                            v-model="searchVal"
                            autocomplete="off"
                            style="width: 0; opacity: 0"
                        />
                        <i-button
                            @click="actToDetail"
                            type="error"

                        ><img src="/img/01.index/search.png" alt="" style="width:20px;height:20px" /></i-button>
                    </div>
                    <index-cart></index-cart>
                    <div
                        class="search-left-discount"
                        v-if="myCoupon"
                        @click="goMyCoupon()"
                    >
                        <img
                            class="mycoupon"
                            v-lazy="'/img/01.index/icon-mycoupon.png'"
                        />
                        <img
                            class="mycouponHover"
                            v-lazy="'/img/01.index/icon-mycoupon-hover.png'"
                        />
                        <span>我的优惠券</span>
                    </div>
                </div>
            </div>
        </div>
        <div
            v-show="xiding && isNeedCupping"
            class="artseizeaseat"
            :style="'width: 100%;height:' + seizeaseatHeight + 'px;'"
        ></div>

        <!-- 回到顶部 -->
        <div v-show="isShow" class="artbackToTop">
            <div @click="actBackToTop">
                <img src="/img/01.index/backToTop/icon-top.png" />
                <img src="/img/01.index/backToTop/icon-top-on.png" />
                返回顶部
            </div>
            <div class="artApp">
                <img src="/img/01.index/backToTop/icon-phone.png" />
                <img src="/img/01.index/backToTop/icon-phone-on.png" />
                下载APP
                <div class="artAppDown">
                    <img src="/img/01.index/MobileQRCode.png" />
                </div>
            </div>
            <div style="display: none">
                <img src="/img/01.index/backToTop/icon-chart.png" />
                <img src="/img/01.index/backToTop/icon-chart-on.png" />
                在线客服
            </div>
        </div>
        <Modal
            v-model="isShowModal"
            width="426px"
            :footer-hide="true"
            height="440px"
        >
            <loginPopup></loginPopup>
        </Modal>
    </div>
</template>
<script>
    import $ from "jquery";
    import Cookies from "js-cookie";
    import shopInfo from "@/components/02.shopIndex/shopInfo.vue";
    import loginPopup from "@/components/common/login";
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import {
        storeCollection,
        storeCancelCollection,
    } from "@/api/api_03.goodsClass";
    export default {
        name: "searchSecondLevel",
        props: {
            isNeedCupping: {
                type: Boolean,
                default: true,
                required: false,
            },
            myCoupon: {
                type: Boolean,
                default: true,
                required: false,
            },
            isShop: {
                type: Boolean,
                default: false,
                required: false,
            },
        },
        data() {
            return {
                //是否吸顶
                xiding: false,
                //是否显示
                isShow: false,
                artShow: false,
                model1: "",
                SwiperHeight: 200, // 滚动高度
                seizeaseatHeight: 194, // 占位高度
                IndexSelectOptions: [],
                storeId: this.$route.query.storeId, //店铺id
                isShowModal: false,

                searchType1: [
                    {
                        value: "0",
                        label: "全部商品",
                    },
                    {
                        value: "1",
                        label: "店铺",
                    },
                ],
                searchVal: "",
                searchTypeVal: "0",
                cropImg: "/01.index/3.jpg",
            };
        },
        components: {
            shopInfo,
            loginPopup,
        },
        computed: {
            ...mapState("main", [
                "IndexSiteData",
                "StoreInfoStoreIdData",
                "IndexSearchkeyword",
            ]),

            searchType() {
                if (this.isShop && this.storeId) {
                    return [
                        {
                            value: "0",
                            label: "全站",
                        },
                        {
                            value: "1",
                            label: "本店",
                        },
                    ];
                } else {
                    return [
                        {
                            value: "0",
                            label: "商品",
                        },
                        {
                            value: "1",
                            label: "店铺",
                        },
                    ];
                }
            },
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    let keyWord = newVal.query.keyWord;
                    this.searchVal = keyWord ? decodeURIComponent(keyWord) : "";
                },
            },
        },

        mounted() {
            // 监听滑动事件
            if (this.isNeedCupping) {
                window.onscroll = this.handleScroll;
            }

            if (this.isShop && this.storeId) {
                this.actStoreInfoStoreId({
                    storeId: this.storeId,
                });
            }
        },
        destroyed() {
            window.onscroll = null; //清除滑动事件绑定
        },
        methods: {
            ...mapActions("main", ["actStoreInfoStoreId", "actIndexTips"]),
            ...mapMutations("main", ["change_IndexSearchkeyword"]),

            actToDetail() {
                let t = new Date().getTime(); // 解决同一个链接页面不刷新问题

                if (this.searchTypeVal == 0) {
                    this.$router.push(
                        `/searchGoodsResult?keyWord=${encodeURIComponent(
                            this.searchVal
                        )}&t=${t}`
                    );
                } else {
                    if (this.isShop && this.storeId) {
                        this.$router.replace({
                            path: "/shopProClassification",
                            query: {
                                storeId: this.$route.query.storeId,
                                keyWord: encodeURIComponent(this.searchVal),
                                querySearchType: "resetStore",
                                t: t,
                            },
                        });
                    } else {
                        this.$router.push(
                            `/searchShopResult?keyWord=${encodeURIComponent(
                                this.searchVal
                            )}`
                        );
                    }
                }
            },

            //远程搜索
            handleSearch1(query) {
                if (query !== "") {
                    this.change_IndexSearchkeyword(query);
                    this.actIndexTips((arrVal) => {
                        console.log(arrVal);
                        this.IndexSelectOptions = arrVal;
                    });
                } else {
                    this.IndexSelectOptions = [];
                }
            },
            handleScroll() {
                // 获取屏幕滑动的高度
                // 滑动高度 > 轮播dom高度  吸顶
                if (document.documentElement.scrollTop > this.SwiperHeight) {
                    // 吸顶
                    this.xiding = true;
                } else {
                    // 取消吸
                    this.xiding = false;
                }

                if (document.documentElement.scrollTop > 100) {
                    this.isShow = true;
                } else {
                    this.isShow = false;
                }
            },
            actBackToTop() {
                $("html,body").animate(
                    {
                        scrollTop: "0px",
                    },
                    350
                );
            },
            goMyCoupon() {
                if (Cookies.get("auth") && Cookies.get("auth") !== "") {
                    this.$router.push({
                        path: "/personalCenterBase/coupon",
                    });
                } else {
                    this.isShowModal = true;
                }
            },
            // 关注店铺
            handleCollectShop(param) {
                console.log(param);
                let storeId = this.$route.query.storeId;
                if (param == "collect") {
                    storeCollection({
                        storeId: storeId,
                    })
                        .then((res) => {
                            if (res.code == 200) {
                                this.$Message.success("收藏成功");
                                this.actStoreInfoStoreId({
                                    storeId: this.storeId,
                                });
                            }
                        })
                        .catch((err) => console.log(err));
                } else {
                    storeCancelCollection([storeId])
                        .then((res) => {
                            if (res.code == 200) {
                                this.$Message.success("取消收藏成功");
                                this.actStoreInfoStoreId({
                                    storeId: this.storeId,
                                });
                            }
                        })
                        .catch((err) => console.log(err));
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    .hidden {
        height: 0;
        width: 0;
        border-width: 0px !important;
    }

    .container {
        background-color: #eee;
    }

    .warp-con {
        width: 1200px;
        margin: 0 auto;
    }

    .backgroundcolorFFF {
        background-color: #fff;
    }

    .con-left {
        width: 232px;
    }

    .con-right {
        flex-grow: 1;
    }

    .artCursor {
        cursor: pointer;
    }

    .artCupping {
        width: 100%;
        min-height: 100px;
        background-color: #fff;
        position: fixed;
        top: 0;
        z-index: 99;
        /* 不要设置太大了，避免影响弹框的层级 */
        border-bottom: 2px solid #dd2619;

        /* 首页吸顶样式开始 */
        .search-wrap {
            height: 99%;

            .search-left {
                margin-top: -4px;
            }
        }

        .search-con-search-bottom {
            display: none;
        }

        /* 首页吸顶样式结束 */
    }

    /* 顶部搜索部分 */
    .mall-header {
        width: 100%;
        background-color: #fff;

        .mall-header-contain {
            width: 1200px;
            height: 140px;
            margin: 0 auto;
            display: flex;
            align-items: center;

            // overflow: hidden;
            & > img {
                width: 219px;
                height: auto;
                margin-right: 30px;
            }

            .artclassification {
                flex-grow: 1;

                p {
                    width: 104px;
                    margin: 14px 30px 13px 0;
                    // padding: 8px 11px 8px 11px;
                    border: 1px solid rgba(204, 204, 204, 1);
                    text-align: center;

                    /deep/ .ivu-select-single .ivu-select-selection {
                        width: 102px;
                        height: 32px;
                        border: 0;
                        outline: none;
                        font-size: 14px;
                        box-shadow: none;

                        span {
                            font-size: 16px;
                            font-weight: 400;
                            color: rgba(102, 102, 102, 1);
                        }

                        /deep/ .ivu-icon {
                            width: 13px;
                            height: 13px;
                            color: rgba(102, 102, 102, 1);
                        }

                        /deep/ .ivu-select-selected-value {
                            padding: 0 0 0 0;
                        }
                    }

                    /deep/
                        .mall-header
                        .mall-header-contain
                        .artclassification
                        p
                        .ivu-select-selected-value {
                        padding: 0 0 0 0;
                    }
                    &:hover{
                        border-color: #cccccc !important ;
                    }
                }

                .ivu-select {
                    width: 104px;
                    height: 32px;
                    // margin-top: 1px;
                    // margin-left: 1px;

                    /deep/ .ivu-select-selected-value,
                    /deep/ .ivu-select-placeholder {
                        height: 32px;
                        line-height: 32px;
                        // padding: 0 21px 0 20px;
                        font-size: 16px;
                        padding-left: 11px;

                    }
                }

            }

            .search-box {
                width: 380px;
                height: 46px;
                background: rgba(255, 255, 255, 1);
                border-radius: 4px;
                border: 2px solid #dd2619;
                display: flex;

                /deep/.ivu-input {
                    padding-left: 0 !important;
                }

                .line {
                    height: 16px;
                    width: 1px;
                    background: rgba(204, 204, 204, 0.5);
                    margin: 0 11px;
                    margin-top: 13px;
                }

                .ivu-select {
                    width: 80px;
                    height: 40px;
                    margin-top: 1px;
                    margin-left: 1px;

                    /deep/ .ivu-select-selection {
                        width: 80px;
                        height: 40px;
                        border: 0;
                        font-size: 16px;
                        outline: none;
                        border: 0;
                    }

                    /deep/ .ivu-select-selected-value,
                    /deep/ .ivu-select-placeholder {
                        height: 40px;
                        line-height: 40px;
                        padding: 0 21px 0 20px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(58, 58, 58, 1);
                    }

                    /deep/ .ivu-icon {
                        &:before {
                            content: "";
                            display: block;
                            width: 0;
                            height: 0;
                            border-left: 5px solid transparent;
                            border-right: 5px solid transparent;
                            border-top: 7px solid #333;
                        }
                    }

                    /deep/ .ivu-auto-complete {
                        width: 378px;
                        left: 677px !important;
                        top: 125px !important;
                        border-top-right-radius: 0px !important;
                        border-top-left-radius: 0px !important;
                        box-shadow: 0 2px 3px rgba(0, 0, 0, 0.2);
                        padding: 0;
                        margin: 0;
                    }
                    /deep/ .ivu-select-selection {
                        border-radius: 0px !important;
                        box-shadow: 0 0px 0px rgba(0, 0, 0, 0.2) !important;
                    }

                    /deep/ .ivu-select-selection-focused {
                        border-radius: 0px !important;
                        box-shadow: 0 0px 3px rgba(0, 0, 0, 0.2) !important;
                    }

                    /deep/ .ivu-select-item {
                        // text-align: center;
                        padding: 2px 16px;
                        font-size: 12px !important;
                        height: 24px;
                        line-height: 20px;
                        color: #666;
                        &:hover {
                            background-color: #e5e5e5;
                        }
                    }
                }

                .ivu-input-wrapper,
                .ivu-auto-complete {
                    flex-grow: 1;
                    height: 40px;

                    /deep/ .ivu-input,
                    /deep/ .ivu-select-input,
                    /deep/ .ivu-select-selection {
                        width: 100%;
                        height: 40px;
                        // margin-top: 1px;
                        border: 0;
                        outline: none;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222222;
                        box-shadow: 0 0 0 0px transparent;
                        border-bottom: 0 solid gainsboro;
                        background: transparent;
                    }
                }

                button {
                    margin-right: -2px;
                    width: 80px;
                    height: 42px;
                    background: #dd2619;
                    border: 0;
                    border-radius: 0;
                    font-size: 20px;

                    /deep/ .ivu-icon-ios-search:before {
                        font-size: 25px;
                        height: 40px;
                        line-height: 40px;
                    }
                }
            }

            .search-left-discount {
                width: 132px;
                height: 46px;
                border-radius: 4px;
                margin-left: 20px;
                background-color: #fff;
                text-align: center;
                cursor: pointer;
                border: 1px solid #bebebe;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                line-height: 46px;

                img {
                    vertical-align: text-top;
                    width: 16px;
                    height: 16px;
                }
                .mycoupon {
                    display: inline-block;
                }
                .mycouponHover {
                    display: none;
                }
                &:hover {
                    color: #dd2619;
                    .mycoupon {
                        display: none;
                    }
                    .mycouponHover {
                        display: inline-block;
                    }
                }
            }

            .search-left-cart {
                width: 132px;
                height: 46px;
                line-height: 46px;
                border-radius: 4px;
                margin-left: 20px;
                border: 1px solid #999999;
                background-color: #fff;
                font-size: 16px;
                // padding-right: 15px;
                display: flex;
                justify-content: center;
                align-items: center;

                .ivu-icon-ios-cart-outline {
                    margin-right: 6px;
                    font-size: 17px;
                }

                .ivu-badge {
                    width: 16px;
                    height: 16px;

                    /deep/ .ivu-badge-count {
                        top: -1px;
                        border-radius: 8px;
                        height: 17px;
                        font-size: 10px;
                        line-height: 16px;
                    }
                }
            }
        }
    }

    /* 回到顶部 */
    .artbackToTop {
        width: 70px;
        position: fixed;
        z-index: 999;
        bottom: 30px;
        right: 10px;

        div {
            cursor: pointer;
            height: 70px;
            margin-bottom: 5px;
            background-color: #ffffff;
            color: #666666;
            padding-top: 13px;
            font-size: 13px;
            font-weight: 400;
            text-align: center;

            img:first-child {
                display: block;
                margin: 0 auto 6px;
            }

            img:nth-child(2) {
                display: none;
                margin: 0 auto 6px;
            }
        }

        div:hover {
            cursor: pointer;
            height: 70px;
            margin-bottom: 5px;
            background-color: #dd2619;
            color: #fff;
            padding-top: 13px;
            font-size: 13px;
            font-weight: 400;
            text-align: center;

            img:first-child {
                display: none;
                margin: 0 auto 6px;
            }

            img:nth-child(2) {
                display: block;
                margin: 0 auto 6px;
            }
        }

        .artApp {
            position: relative;

            .artAppDown {
                padding-top: 0;
                position: absolute;
                top: 0;
                left: -145%;
                display: none;
                width: 100px;
                height: 100px;
                z-index: 99;

                img {
                    width: 100px !important;
                    height: 100px !important;
                    display: block !important;
                }
            }

            &:hover > .artAppDown {
                display: block;
            }
        }
    }
    /deep/ .ivu-select-single .ivu-select-selection .ivu-select-placeholder{
        color: #666666 !important;
    }
    /deep/ .ivu-select-visible .ivu-select-selection{
        border-color: #cccccc !important;
        box-shadow: none !important;
    }
    /deep/ .ivu-select-selection-focused, .ivu-select-selection:hover{
        border-color: #cccccc !important;
    }
    /deep/ .ivu-select-selection-focused .ivu-select-selection{
        &:hover{
            border-color: #cccccc !important;
        }
    }
    /deep/ .ivu-select-selection-focused, .ivu-select-selection:hover{
        border-color: #cccccc !important;
    }

</style>
