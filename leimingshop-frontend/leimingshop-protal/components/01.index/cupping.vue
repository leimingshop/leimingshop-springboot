<template>
    <div>
        <!-- 吸顶 -->
        <div :class="{ artCupping: xiding }">
            <slot></slot>
        </div>
        <div
            v-show="xiding"
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
        <!-- <slot name="container"></slot> -->
    </div>
</template>
<script>
    import $ from "jquery";
    export default {
        name: "cuppingTop",
        props: {
            SwiperHeight: {
                // 滚动高度
                type: Number,
                default: 200,
                required: false,
            },
            seizeaseatHeight: {
                // 占位高度
                type: Number,
                default: 194,
                required: false,
            },
            topHeight: {
                //顶部显示高度
                type: Number,
                default: 100,
                required: false,
            },
        },
        data() {
            return {
                //是否吸顶
                xiding: false,
                //是否显示
                isShow: false,
            };
        },
        mounted() {
            // 监听滑动事件
            window.onscroll = this.handleScroll;
        },
        destroyed() {
            window.onscroll = null; //清除滑动事件绑定
        },
        methods: {
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
    .artCupping {
        width: 100%;
        background-color: #fff;
        position: fixed;
        top: 0;
        z-index: 99; //不要设置太大了，避免影响弹框的层级

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
