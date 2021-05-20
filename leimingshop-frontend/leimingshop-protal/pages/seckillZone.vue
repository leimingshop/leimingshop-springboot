<template>
    <div class="container">
        <!-- 顶部搜索部分 -->
        <search-second-level :isNeedCupping="false"></search-second-level>

        <!-- 面包屑-->
        <breadcrumb1 :list="brandCrumbList" />

        <!-- 中间的banner广告 -->
        <div
            class="banner-part warp-con"
            v-lazy:background-image="'/img/01.index/seckillZone-benner.png'"
        ></div>

        <!-- {{activitySeckillAreaTimeData}}  -->
        <!-- 秒杀时间  -->
        <cupping :SwiperHeight="500" :seizeaseatHeight="80">
            <div class="warp-con">
                <div
                    v-swiper:mySwiper="swiperOption"
                    class="swiper-container tabs-con"
                >
                    <div class="swiper-wrapper tabs">
                        <li
                            class="li-tab swiper-slide"
                            @click="
                                toggleTabs(
                                    0,
                                    activitySeckillAreaTimeData.sessionId
                                )
                            "
                            :class="{
                                artActive: 0 === nowIndex,
                                artCursor: true,
                            }"
                        >
                            <p class="artTime">
                                {{ activitySeckillAreaTimeData.currentDateStr }}
                            </p>
                            <p class="artText">
                                正在秒杀
                                <span
                                    >距离结束时间{{ timeCount.hours }}:{{
                                        timeCount.minutes
                                    }}:{{ timeCount.seconds }}</span
                                >
                            </p>
                        </li>
                        <li
                            class="li-tab swiper-slide"
                            v-for="(item,
                            index) in activitySeckillAreaTimeData.soonTimeSoltList"
                            @click="toggleTabs(index + 1, item.sessionId)"
                            :class="{
                                artActive: index + 1 === nowIndex,
                                artCursor: true,
                            }"
                            :key="index"
                        >
                            <p class="artTime">
                                {{ item.dayTime
                                }}{{
                                    item.activityStartDate &&
                                    item.activityStartDate.substring(11, 16)
                                }}
                            </p>
                            <p class="artText">即将开始</p>
                        </li>
                    </div>
                </div>
            </div>
        </cupping>
        <!-- 秒杀切换 -->
        <div class="seckillZone-part warp-con">
            <p
                class="artRemind backgroundcolorFFF"
                v-if="ActivitySeckillCurrentGoodsData.length"
            >
                <img src="/img/01.index/icon-watch.png" />设置提醒 好物不错过
            </p>
            <load-more
                :completed="iscompleted"
                @changeData="changPages"
                v-if="ActivitySeckillCurrentGoodsData.length"
            >
                <div class="artCouponcenter">
                    <seckill-zone-item
                        :sessionId="
                            sessionId
                                ? sessionId
                                : activitySeckillAreaTimeData.sessionId
                        "
                        @toggleShow="isShowModal = true"
                        v-for="(item, index) in ActivitySeckillCurrentGoodsData"
                        :isRemind="nowIndex"
                        :key="index"
                        :data="item"
                    ></seckill-zone-item>
                </div>
            </load-more>
        </div>

        <!-- 无秒杀商品时 -->
        <div
            class="seckillNull"
            v-if="ActivitySeckillCurrentGoodsData.length == 0"
        >
            <div class="no-data-main-content">
                <img :src="'/img/06.personalCenter/noshop.png'" alt />
                <div class="null">暂无秒杀商品</div>
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
    // import Swiper from "swiper";
    // import "swiper/css/swiper.css";
    import $ from "jquery";
    import cupping from "@/components/01.index/cupping.vue"; //吸顶效果
    import seckillZoneItem from "@/components/01.index/seckillZoneItem.vue"; //秒杀专区
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import loginPopup from "@/components/common/login";
    import { mapState, mapActions, mapMutations } from "vuex";
    export default {
        name: "seckillZone",
        data() {
            return {
                dataHH: 4,
                seckillGoodsData: {
                    img: "/img/01.index/合并形状.png",
                    title: "小米雷铭官方官方旗舰店",
                    info: "匠心裁剪 垂感质地",
                    money: "579.00",
                    originalPrice: "759.00",
                    num: "100",
                },
                cropImg: "/01.index/3.jpg",
                nowIndex: 0,
                sessionId: "",
                isShowModal: false,
                swiperOption: {
                    slidesPerView: "auto",
                    spaceBetween: 5,
                    freeMode: true,
                    pagination: {
                        el: ".swiper-container",
                        dynamicBullets: true,
                    },
                },
                brandCrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "秒杀专区",
                        toPath: "",
                    },
                ]),
            };
        },
        components: {
            cupping,
            seckillZoneItem,
            loginPopup,
            breadcrumb1,
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "iscompleted",
                "activitySeckillAreaTimeData",
                "timeCount",
                "ActivitySeckillCurrentGoodsData",
                "total",
            ]),
        },
        mounted() {
            this.activitySeckillAreaTime(() => {
                // this.$nextTick(() => {
                //   new Swiper(".swiper-container", {
                //     slidesPerView: "auto",
                //     spaceBetween: 5,
                //     freeMode: true,
                //   });
                // });
            });
        },
        methods: {
            ...mapActions("main", [
                "activitySeckillAreaTime",
                "activitySeckillCurrentGoods",
                "activitySeckillSoonGoods",
            ]),
            //下滑加载更多
            changPages(page) {
                if (!this.iscompleted) {
                    let obj = {
                        page: page,
                        sessionId: this.sessionId,
                    };
                    this.activitySeckillCurrentGoods(obj);
                }
            },
            //切换tab项
            toggleTabs(index, sessionId) {
                this.nowIndex = index;
                this.sessionId = sessionId;
                let obj = {
                    page: 1,
                    sessionId: sessionId,
                };
                if (index == 0) this.activitySeckillCurrentGoods(obj);
                //秒杀专区-正在秒杀商品列表
                else this.activitySeckillSoonGoods(obj); //秒杀专区-即将秒杀商品列表
                $(window).resize();
            },
        },
    };
</script>

<style lang="scss" scoped>
    .container {
        background-color: #f6f6f6;
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

    .layout-breadcrumb {
        height: 52px;
        line-height: 52px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(51, 51, 51, 1);
        .home {
            cursor: pointer;
        }
        .home:hover {
            color: #e2270b;
        }
    }

    //中间banner部分
    .banner-part {
        height: 120px;
        background-repeat: no-repeat;
        background-size: cover;
        background-position: center;
    }

    .tabs-con {
        background-color: #414141;
    }

    /deep/ .tabs {
        text-align: center;
        height: 80px;
        line-height: 80px;

        .li-tab {
            width: 240px;
            margin-right: 0;
            padding: 14px 19px 14px 19px;
            border: 0;
            background-color: #414141;
            border-radius: 0;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;

            &.artActive {
                background-color: #dd2619;
            }

            p {
                line-height: 24px;
                text-align: left;
            }

            .artTime {
                font-size: 20px;
                margin-right: 10px;
            }

            &:nth-of-type(1) {
                .artText {
                    flex-grow: 1;
                }
            }

            &:nth-of-type(n + 2) {
                padding-right: 50px;
                padding-left: 50px;
            }

            .artText {
                font-size: 14px;

                span {
                    display: block;
                }
            }
        }
    }

    .seckillZone-part {
        .artRemind {
            height: 46px;
            margin: 15px 0 15px 0;
            font-size: 18px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222222;
            line-height: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            img {
                margin-right: 8px;
            }
        }

        p {
            text-align: center;
            // margin: 39px 0 33px 0;
        }

        .artCouponcenter {
            display: flex;
            // justify-content: space-between;
            flex-wrap: wrap;
        }
    }

    // 无秒杀商品
    .seckillNull {
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 200px 0;
        .no-data-main-content {
            .null {
                font-size: 16px;
                color: rgba(102, 102, 102, 1);
                text-align: center;
                margin-top: 5px;
            }
        }
    }
</style>
