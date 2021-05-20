<template>
    <div>
        <!-- 一级公共头部  -->
        <div :class="{ artCupping: xiding && isNeedCupping }">
            <div class="search-wrap warp-con backgroundcolorFFF">
                <img
                    @click="$router.push('/')"
                    class="search-left con-left artCursor"
                    :src="$imgURL + IndexSiteData.logoNow"
                    alt
                />
                <!-- wt-start -->
                <div class="personal-title1">
                    <span
                        class="pertitle1"
                        @click="
                            $router.push('/personalCenterBase/personalCenter')
                        "
                        >个人中心</span
                    >
                    <!-- <span class="pertitle2">账户设置</span> -->
                    <Dropdown>
                        <a href="javascript:void(0)" class="personalSize"
                            >账户设置</a
                        >
                        <DropdownMenu slot="list">
                            <DropdownItem>
                                <router-link
                                    to="/personalCenterBase/personalData"
                                    >个人信息</router-link
                                >
                            </DropdownItem>
                            <DropdownItem>
                                <router-link
                                    to="/personalCenterBase/securitySetting"
                                    >安全设置</router-link
                                >
                            </DropdownItem>
                            <DropdownItem>
                                <router-link
                                    to="/personalCenterBase/addressManagement"
                                    >地址管理</router-link
                                >
                            </DropdownItem>
                            <DropdownItem>
                                <router-link to="/personalCenterBase/myPoints"
                                    >我的积分</router-link
                                >
                            </DropdownItem>
                            <DropdownItem>
                                <router-link to="/personalCenterBase/myTracks"
                                    >我的足迹</router-link
                                >
                            </DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                    <!-- <span class="pertitle3">会员中心</span> -->
                    <div class="perstitle">
                        <span
                            class="pertitle4"
                            @click="
                                $router.push(
                                    '/personalCenterBase/messageNotification'
                                )
                            "
                        >
                            消息
                            <!-- （<span class="pertitle4-number">{{messageCountData}}</span>） -->
                        </span>
                        (<span class="pertitle4-number">{{
                            messageCountData
                        }}</span
                        >)
                    </div>
                </div>
                <!-- wt-end -->
                <div class="search-con">
                    <div class="search-con-search">
                        <div
                            class="search-con-search-top"
                            v-on:keyup.13="actToDetail"
                        >
                            <Select
                                v-model="searchTypeVal"
                                size="small"
                                placeholder
                            >
                                <Option
                                    v-for="item in searchType"
                                    :value="item.value"
                                    :key="item.value"
                                >
                                    {{ item.label }}</Option
                                >
                            </Select>
                            <AutoComplete
                                v-model="searchVal"
                                :data="IndexSelectOptions"
                                @on-search="handleSearch1"
                                placeholder="请输入关键词"
                                style="width: 150px"
                            ></AutoComplete>
                            <!-- <i-input v-model="searchVal" placeholder="请输入"></i-input> -->
                            <Button
                                @click="actToDetail"
                                type="error"
                                class="search-btn"
                            >
                                <img
                                    src="/img/common/search/ico-search.png"
                                    alt=""
                                />
                            </Button>
                        </div>
                        <!-- <div class="search-con-search-bottom">
                            <span class="artCursor" v-for="(item,index) in IndexHotWordData" :key="index" :class="{artCurrent:item.sort==1}">{{item.hotWord}}</span>
            </div>-->
                    </div>
                    <index-cart></index-cart>
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
    </div>
</template>
<script>
    import $ from "jquery";
    import { messageCount } from "@/api/api_06-09messageNotification.js";
    import indexCart from "@/components/01.index/indexCart.vue"; //购物车
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        name: "searchFirstLevel",
        props: {
            isNeedCupping: {
                type: Boolean,
                default: true,
                required: false,
            },
        },
        data() {
            return {
                //是否吸顶
                xiding: false,
                //是否显示
                isShow: false,
                SwiperHeight: 200, // 滚动高度
                seizeaseatHeight: 194, // 占位高度
                IndexSelectOptions: [],
                count: "",
                searchType: [
                    {
                        value: "0",
                        label: "商品",
                    },
                    {
                        value: "1",
                        label: "店铺",
                    },
                ],
                searchVal: "",
                searchTypeVal: "0",
            };
        },
        components: {
            indexCart,
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "IndexHotWordData",
                "IndexSiteData",
                "IndexSearchkeyword",
                "messageCountData",
            ]),
        },
        created() {
            // this.getNotifyCount();
        },
        mounted() {
            // 监听滑动事件
            if (this.isNeedCupping) {
                window.onscroll = this.handleScroll;
            }
            this.actIndexHotWord();
            this.actGetMessageCount();
        },
        destroyed() {
            window.onscroll = null; //清除滑动事件绑定
        },
        methods: {
            ...mapActions("main", [
                "actIndexHotWord",
                "actIndexTips",
                "actGetMessageCount",
            ]),
            ...mapMutations("main", ["change_IndexSearchkeyword"]),
            actToDetail() {
                if (this.searchTypeVal == 0) {
                    this.$router.push(
                        `/searchGoodsResult?keyWord=${encodeURIComponent(
                            this.searchVal
                        )}`
                    );
                } else {
                    this.$router.push(
                        `/searchShopResult?keyWord=${encodeURIComponent(
                            this.searchVal
                        )}`
                    );
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
        },
    };
</script>
<style lang="scss" scoped>
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
        height: 100px;
        background-color: #fff;
        position: fixed;
        top: 0;
        z-index: 99; //不要设置太大了，避免影响弹框的层级
        border-bottom: 2px solid #dd2619;

        //首页吸顶样式开始
        .search-wrap {
            height: 99%;

            .search-left {
                margin-top: -4px;
            }
        }

        .search-con-search-bottom {
            display: none;
        }

        //首页吸顶样式结束
    }

    .search-wrap {
        height: 120px;
        // padding-top: 47px;
        display: flex;
        align-items: center;

        .search-left {
            // margin-top: -24px;
            width: 188px;
            // height: 52px;
        }

        .search-con {
            flex-grow: 1;
            display: flex;
            justify-content: center;
            margin-left: 40px;
        }
    }
    /deep/.oneStyle.ivu-badge-count {
        width: 16px !important;
        height: 16px !important;
        line-height: 15px !important;
    }
    .search-con-search {
        width: 330px;
        .search-con-search-top {
            // width:560px;
            height: 46px;
            background: rgba(255, 255, 255, 1);
            border-radius: 4px;
            border: 2px solid #dd2619;
            display: flex;
            .search-btn {
                height: 43px;
                padding-top: 6px;
                display: flex;
                justify-content: center;
                align-items: center;
                img {
                    width: 20px;
                    height: 20px;
                }
            }
            .ivu-select {
                width: 85px;
                height: 42px;
                // margin-top: 1px;
                margin-left: 1px;
                position: relative;
                overflow: visible;

                /deep/ .ivu-select-selection {
                    width: 85px;
                    height: 42px;
                    border: 0;
                    font-size: 16px;
                }

                /deep/ .ivu-select-selected-value,
                /deep/ .ivu-select-placeholder {
                    height: 42px;
                    line-height: 42px;
                    padding: 0 17px;
                    font-size: 14px;
                    color: #222222;
                    font-family: PingFangSc-Regular, PingFang SC;
                    font-weight: 400;
                }

                /deep/ .ivu-select-dropdown {
                    top: 42px !important;
                    border-top-right-radius: 0px !important;
                    border-top-left-radius: 0px !important;
                    box-shadow: 0 2px 3px rgba(0, 0, 0, 0.2);
                    margin: 0px !important;
                    padding: 0px !important;
                    position: relative;
                    /deep/ .ivu-select-dropdown-list {
                        // position:absolute;
                        top: 10px;
                    }
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
                    text-align: center;
                }
            }

            .ivu-input-wrapper,
            .ivu-auto-complete {
                flex-grow: 1;
                height: 42px;

                /deep/ .ivu-input,
                /deep/ .ivu-select-input,
                /deep/ .ivu-select-selection {
                    width: 100%;
                    height: 40px;
                    margin-top: 1px;
                    border: 0;
                    outline: none;
                    font-size: 14px;
                    box-shadow: 0 0 0 0px transparent;
                    border-bottom: 0 solid gainsboro;
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
                    font-size: 26px;
                }
            }
        }

        .search-con-search-bottom {
            margin-top: 6px;
            color: #bebebe;
            font-size: 13px;

            span {
                display: inline-block;
                margin: 0 6px 0 0;

                &.artCurrent {
                    color: #e2270b;
                    font-weight: 500;
                }
            }
        }
    }
    //回到顶部
    .artbackToTop {
        width: 70px;
        // background-color: #fff;
        position: fixed;
        bottom: 30px;
        right: 10px;
        z-index: 1000;

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

    .personal-title1 {
        display: flex;
        // justify-content: space-between;
        align-items: center;
        margin-left: 20px;
        width: 440px;
        height: 30px;
        border-left: 1px solid #ebebeb;

        .pertitle1 {
            font-size: 26px;
            color: #333333;
            margin-left: 20px;
            cursor: pointer;
        }

        .pertitle2 {
            font-size: 15px;
            color: #666666;
            margin-left: 20px;
        }

        .pertitle3 {
            font-size: 15px;
            color: #666666;
            margin-left: 20px;
        }

        .pertitle4 {
            font-size: 14px;
            color: #666666;
            margin-left: 20px;
            cursor: pointer;
            font-family: PingFangSC-Regular, PingFang SC;
        }
    }
    .perstitle {
        &:hover {
            .pertitle4 {
                color: #dd2619;
            }
            color: #dd2619;
        }
    }
    .pertitle4-number {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #dd2619;
    }
    a {
        color: #666666;
    }
    .personalSize {
        font-size: 14px;
        color: #666666;
        margin-left: 20px;
        font-family: PingFangSC-Regular, PingFang SC;
        &:hover {
            color: #dd2619;
        }
    }
    /deep/.search-con-search
        .search-con-search-top
        .ivu-auto-complete[data-v-298cbf13]
        .ivu-input {
        height: 20px;
        margin-top: 12px;
        padding: 3px 19px;
        border-left: 1px solid #ebebeb;
    }
    /deep/ .ivu-icon-ios-arrow-down {
        &::before {
            content: "";
            display: block;
            width: 0;
            height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-top: 7px solid #333;
        }
    }
    /deep/ .artCart .search-con-cart {
        margin-left: 14px !important;
    }
    /deep/ .artCart .search-con-cart .ivu-badge {
        margin-left: 0px !important;
    }
    /deep/ .ivu-select-arrow {
        margin-right: 10px;
    }
    // /deep/ .ivu-select-item-selected, .ivu-select-item-selected{
    //     &:hover{
    //      background: rgba(221, 38, 25, 0.03) !important;
    //     }
    // }
    // /deep/ .ivu-dropdown-rel{
    //     &:hover{
    //         color: #dd2619;
    //     }
    // }
</style>
