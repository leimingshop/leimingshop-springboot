<template>
    <div class="personalBase">
        <!-- <search-part-white></search-part-white> -->
        <div class="personl-top" style="background: #ffffff">
            <personal-top></personal-top>
        </div>
        <div class="personal-center-base">
            <!-- 面包屑 -->
            <breadcrumb1 :list="breadcrumbList" />
            <div class="person-center-content">
                <div class="person-center-left">
                    <ul>
                        <li v-for="(item1, index1) in navList" :key="index1">
                            <div class="list-head">
                                <!-- {{navList}} -->
                                <img
                                    :src="item1.img"
                                    alt
                                    class="list-head-img"
                                />
                                {{ item1.title }}
                            </div>
                            <ul class="detail-list">
                                <li
                                    v-for="(item2, index2) in item1.list"
                                    :key="index2"
                                    @click="
                                        goDetail(
                                            item2.name,
                                            item2.routename,
                                            index1,
                                            index2,
                                            item2.routerPath
                                        )
                                    "
                                    :class="{
                                        on:
                                            $store.state.personalCenterBase
                                                .index1Flag === index1 &&
                                            $store.state.personalCenterBase
                                                .index2Flag === index2,
                                    }"
                                >
                                    <span
                                        :class="{
                                            on:
                                                $store.state.personalCenterBase
                                                    .index1Flag === index1 &&
                                                $store.state.personalCenterBase
                                                    .index2Flag === index2,
                                        }"
                                        style="height: 12px; line-height: 12px"
                                        >{{ item2.name }}</span
                                    >
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <div class="person-center-right">
                    <!-- <keep-alive> -->
                    <router-view></router-view>
                    <!-- </keep-alive> -->
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    // import searchPartWhite from "@/components/common/searchPartWhite.vue";
    import personalTop from "@/components/06.personalCenter/common/personalTop.vue";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        components: {
            // searchPartWhite
            personalTop,
            breadcrumb1,
        },
        props: {},
        data() {
            return {
                targetPathName: "", //选中模块的路由名字
                personCenterTitle: "个人中心",
                navList: [
                    //routename是路由的name，根据自己需要进行设置，用于跳转

                    {
                        title: "交易管理",
                        img: require("~/static/img/06.personalCenter/transaction.png"),
                        list: [
                            {
                                name: "我的订单",
                                routename: "myOrders",
                                routerPath: "/personalCenterBase/myOrders",
                            },
                            {
                                name: "售后服务",
                                routename: "afterSale",
                                routerPath: "/personalCenterBase/afterSale",
                            },
                            {
                                name: "优惠券",
                                routename: "coupon",
                                routerPath: "/personalCenterBase/coupon",
                            },
                            {
                                name: "评价晒单",
                                routename: "evaluationSheet",
                                routerPath: "/personalCenterBase/evaluationSheet",
                            },
                            // {
                            //     name: "售后服务列表",
                            //     routename: "afterSalesExchange"
                            // }
                        ],
                    },
                    {
                        title: "我的收藏",
                        img: require("~/static/img/06.personalCenter/collection.png"),
                        list: [
                            {
                                name: "收藏的商品",
                                routename: "favoritesList",
                                routerPath: "/personalCenterBase/favoritesList",
                            },
                            {
                                name: "收藏的店铺",
                                routename: "favoriteStoreList",
                                routerPath: "/personalCenterBase/favoriteStoreList",
                            },
                        ],
                    },
                    {
                        title: "我的账户",
                        img: require("~/static/img/06.personalCenter/account.png"),
                        list: [
                            {
                                name: "个人中心",
                                routename: "personalCenter",
                                routerPath: "/personalCenterBase/personalCenter",
                            },
                            {
                                name: "消息通知",
                                routename: "messageNotification",
                                routerPath:
                                    "/personalCenterBase/messageNotification",
                            },
                            {
                                name: "个人信息",
                                routename: "personalData",
                                routerPath: "/personalCenterBase/personalData",
                            },
                            {
                                name: "安全设置",
                                routename: "securitySetting",
                                routerPath: "/personalCenterBase/securitySetting",
                            },
                            {
                                name: "地址管理",
                                routename: "addressManagement",
                                routerPath: "/personalCenterBase/addressManagement",
                            },
                            {
                                name: "我的积分",
                                routename: "myPoints",
                                routerPath: "/personalCenterBase/myPoints",
                            },
                            {
                                name: "我的足迹",
                                routename: "myTracks",
                                routerPath: "/personalCenterBase/myTracks",
                            },
                            {
                                name: "我的发票",
                                routename: "useInvoice",
                                routerPath:
                                    "/personalCenterBase/invoiceBase/useInvoice",
                            },
                        ],
                    },
                    {
                        title: "帮助中心",
                        img: require("~/static/img/06.personalCenter/helpcenter.png"),
                        list: [
                            {
                                name: "帮助中心",
                                routename: "helpcenterProblem",
                                routerPath: "/helpcenterProblem",
                            },
                        ],
                    },
                ],
                lastRoute: {
                    meta: {
                        title: "去你打野的",
                    },
                    path: "/",
                }, //上个路由信息
            };
        },
        watch: {},
        computed: {
            // ...mapState("personalCenterBase", ["barList"]),
            breadcrumbList: {
                get() {
                    const arr =
                        this.$store.state.personalCenterBase.barList &&
                        this.$store.state.personalCenterBase.barList.length
                            ? this.$store.state.personalCenterBase.barList.map(
                                  (item) => {
                                      item.toPath = item.path;
                                      item.title = item.meta.title;
                                      return item;
                                  }
                              )
                            : [];
                    return arr;
                },
                set() {},
            },
        },
        async asyncData() {},
        methods: {
            judgeRouterCopy() {
                //左边导航选中状态以及面包屑处理
                let routerPath = this.$route.path;
                this.navList.forEach((item1, index1) => {
                    item1.list.forEach((item2, index2) => {
                        if (routerPath == item2.routerPath) {
                            const arr = [];
                            arr.push({
                                meta: {
                                    title: "首页",
                                },
                                path: "/",
                            });
                            arr.push({
                                meta: {
                                    title: item2.name,
                                },
                                path: "",
                            });
                            var SET_DISPLAY_STYLE_DATA = {
                                index1Flag: index1,
                                index2Flag: index2,
                                personCenterTitle: item2.name,
                                barList: arr,
                            };
                            this.$store.commit(
                                "personalCenterBase/SET_DISPLAY_STYLE",
                                SET_DISPLAY_STYLE_DATA
                            );
                        }
                    });
                });
            },
            goDetail(name, routename, index1Flag, index2Flag, routerPath) {
                this.personCenterTitle = name;
                this.$router.push({
                    path: routerPath,
                });

                var SET_DISPLAY_STYLE_DATA = {
                    index1Flag: index1Flag,
                    index2Flag: index2Flag,
                    personCenterTitle: name,
                };

                this.$store.commit(
                    "personalCenterBase/SET_DISPLAY_STYLE",
                    SET_DISPLAY_STYLE_DATA
                );
            },
        },
        created() {
            //首次进我的中心，初始化面包屑
            this.barList = [
                {
                    meta: {
                        title: "首页",
                    },
                    path: "/",
                },
                {
                    meta: {
                        title: this.$store.state.personalCenterBase
                            .personCenterTitle,
                    },
                },
            ];
        },
        mounted() {
            this.judgeRouterCopy();
        },
        beforeUpdate() {
            this.judgeRouterCopy();
        },
        updated() {
            //改变了barList，不能在此用judgeRouterCopy方法，陷入死循环
            //  this.judgeRouterCopy();
        }, // 监听,当路由发生变化的时候执行
        watch: {
            $route(to, from) {
                this.lastRoute = {
                    path: from.path,
                    meta: {
                        title: from.meta.title,
                    },
                };
            },
        },
    };
</script>
<style lang="scss" scoped>
    .personalBase /deep/ {
        width: 100%;
        background: #f6f6f6;
    }

    .personal-center-base {
        width: 1200px;
        margin: 0 auto;
        padding-bottom: 60px;

        .personal-center-nav {
            font-size: 13px;
            padding: 15px 0;
            color: #333333;
            cursor: pointer;
        }

        .person-center-content {
            display: flex;
            justify-content: space-between;
        }
    }

    .person-center-left {
        width: 232px;
        background: rgba(255, 255, 255, 1);
        //   padding:20px 61px 20px 19px
        li {
            border-bottom: 1px solid #ebebeb;
            .detail-list {
                li {
                    border-bottom: none;
                }
            }
            &:last-child {
                border-bottom: none;
            }
        }
    }

    .person-center-right {
        width: 948px;
    }

    .list-head {
        height: 42px;
        font-size: 14px;
        color: #222222;
        font-weight: 600;
        // padding: 30px 0 27px 42px;
        line-height: 42px;
        display: flex;
        margin: 10px 0 0px 42px;
        font-family: PingFangSC-Medium, PingFang SC;
        .list-head-img {
            width: 14px;
            height: 14px;
            margin-right: 10px;
            margin-top: 13px;
        }
    }

    .detail-list {
        li {
            height: 40px;
            cursor: pointer;
            line-height: 40px;
            font-size: 12px;
            color: #666666;
            &:hover {
                color: #dd2619;
            }
            span {
                width: 100%;
                display: inline-block;
                padding-left: 67px;
            }

            span.on {
                border-left: 3px solid #dd2619;
                padding-left: 64px;
            }
            &:last-child {
                margin-bottom: 10px;
                border-bottom: none !important;
            }
        }

        li.on {
            color: #dd2619;
            background: #fef8f8;
            font-weight: 600;
        }
    }
    /deep/ .ivu-breadcrumb-item-separator {
        margin: 0 4px 0 0 !important;
    }
</style>
