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
                            <div class="line"></div>
                            <AutoComplete
                                v-model="searchVal"
                                :data="IndexSelectOptions"
                                @on-search="handleSearch1"
                                @on-select="changeSearchInput"
                                placeholder="秒杀商品限时抢购"
                            ></AutoComplete>

                            <!-- 解决Chrome浏览器自动填充input的问题 -->
                            <input
                                v-model="searchVal"
                                autocomplete="off"
                                style="width: 0; opacity: 0; border: 0"
                            />

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
                        <div class="search-con-search-bottom">
                            <span
                                class="artCursor"
                                @click="
                                    searchVal = item.hotWord;
                                    actToDetail();
                                "
                                v-for="(item, index) in IndexHotWordData"
                                :key="index"
                                >{{ item.hotWord }}</span
                            >
                        </div>
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
                <img v-lazy="'/img/01.index/backToTop/icon-top.png'" />
                <img v-lazy="'/img/01.index/backToTop/icon-top-on.png'" />
                返回顶部
            </div>
            <div class="artApp">
                <img v-lazy="'/img/01.index/backToTop/icon-phone.png'" />
                <img v-lazy="'/img/01.index/backToTop/icon-phone-on.png'" />
                下载APP
                <div class="artAppDown">
                    <img v-lazy="'/img/01.index/MobileQRCode.png'" />
                </div>
            </div>
            <div style="display: none">
                <img v-lazy="'/img/01.index/backToTop/icon-chart.png'" />
                <img
                    v-lazy="'/img/01.index/backToTop/icon-chart-on.png'"
                />在线客服
            </div>
        </div>
    </div>
</template>
<script>
    import $ from "jquery";
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
            ]),
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    // 这里是跳转以后的空数据和热词显示方式
                    this.searchVal = newVal.query.keyWord || "";
                },
            },
        },

        mounted() {
            // 监听滑动事件
            if (this.isNeedCupping) {
                window.onscroll = this.handleScroll;
            }
            this.actIndexHotWord();

            // 这里是店铺回显方式
            this.$nextTick(() => {
                if (this.$route.name == "searchGoodsResult") {
                    this.searchTypeVal = "0";
                } else if (this.$route.name == "searchShopResult") {
                    this.searchTypeVal = "1";
                }
            });
        },
        destroyed() {
            window.onscroll = null; //清除滑动事件绑定
        },
        methods: {
            ...mapActions("main", ["actIndexHotWord", "actIndexTips"]),
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
                    this.$router.push(
                        `/searchShopResult?keyWord=${encodeURIComponent(
                            this.searchVal
                        )}&t=${t}`
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
            changeSearchInput(query) {
                this.searchVal = query;
                this.actToDetail();
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

    .artCursor:hover {
        color: #dd2619;
    }

    .artCupping {
        width: 100%;
        height: 80px;
        background-color: #fff;
        position: fixed;
        top: 0;
        z-index: 99; //不要设置太大了，避免影响弹框的层级
        border-bottom: 1px solid #dd2619;
        box-shadow: 0px 2px 12px 0px rgba(0, 0, 0, 0.18);

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
        height: 137px;
        // padding-top: 47px;
        display: flex;
        align-items: center;

        .search-left {
            // height: 60px; //修改这个属性的时候请通知天津本部前端 刘伯琛
        }

        .search-con {
            flex-grow: 1;
            display: flex;
            justify-content: center;
        }
    }

    .search-con-search {
        .search-con-search-top {
            width: 560px;
            height: 46px;
            background: rgba(255, 255, 255, 1);
            border-radius: 4px;
            border: 2px solid #dd2619;
            display: flex;
            align-items: center;
            position: relative;
            .ivu-select {
                width: 80px;
                height: 40px;

                /deep/ .ivu-select-selection {
                    width: 80px;
                    height: 40px;
                    border: 0;
                    font-size: 14px;
                }

                /deep/ .ivu-select-selected-value,
                /deep/ .ivu-select-placeholder {
                    height: 40px;
                    line-height: 40px;
                    padding: 0 21px 0 20px;
                    font-size: 14px;
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
                    padding: 0px;
                    width: 560px;
                    top: 40px !important;
                    border-radius: 0px !important;
                    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.2);
                    border: 1px solid #e5e5e5;
                    left: -2px !important;
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
                    padding: 2px 20px;
                    font-size: 12px !important;
                    height: 24px;
                    line-height: 20px;
                    color: #666;
                    &:hover {
                        background-color: #e5e5e5;
                    }
                }
            }

            .line {
                height: 16px;
                width: 1px;
                background-color: #cccccc;
                margin: 0 11px;
            }
            .search-btn {
                img {
                    width: 20px;
                    height: 20px;
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
                    margin-top: 1px;
                    border: 0;
                    outline: none;
                    font-size: 14px;
                    box-shadow: 0 0 0 0px transparent;
                    border-bottom: 0 solid gainsboro;
                }
            }

            /deep/ .ivu-input::-webkit-input-placeholder {
                color: #999999;
            }

            button {
                width: 80px;
                height: 43px;
                background: #dd2619;
                padding-top: 6px;
                border: 0;
                border-radius: 0;
            }
        }

        .search-con-search-bottom {
            width: 563px;
            margin-top: 6px;
            color: #bebebe;
            font-size: 13px;
            overflow: hidden;
            white-space: nowrap;
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
                background-color: #ffffff;
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
</style>
