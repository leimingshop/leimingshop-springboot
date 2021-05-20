<template>
    <div class="integration-center">
        <div class="integra-center-top">
            <ul class="intecenter-top-left" id="tag">
                <li
                    v-for="(item, index) in tabArray"
                    :key="index"
                    class="base"
                    :class="{ current: currentIndex == index }"
                    @click="toggleLabel(index)"
                >
                    {{ item.name }}
                </li>
            </ul>
            <ul
                class="intecenter-top-right"
                id="tag"
                @mouseout="mouseOut"
                @mouseover="mouseOver"
            >
                <img :src="wenhao" alt />
                <span @click="normalQuestion" style="cursor: pointer"
                    >积分使用常见问题</span
                >
            </ul>
        </div>
        <component :is="moduler" @seeAllType="seeAll"></component>
    </div>
</template>
<script>
    import myPoints from "@/components/06.personalCenter/06-05.myPoints/myPoints.vue";
    import pointsDetail from "@/components/06.personalCenter/06-05.myPoints/pointsDetail.vue";
    import { mapState, mapMutations, mapActions } from "vuex";
    export default {
        components: {
            myPoints,
            pointsDetail,
        },
        props: {},
        head() {
            return {
                title: "我的积分",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        data() {
            return {
                wenhao: "/img/06.personalCenter/wenhao.png",
                moduler: "myPoints",
                currentIndex: 0,
                tabArray: [
                    { name: "我的积分", component: myPoints },
                    { name: "积分明细", component: pointsDetail },
                ],
            };
        },
        watch: {},
        computed: {
            ...mapState("myPoints", ["page", "pageSize"]),
        },
        methods: {
            ...mapActions("myPoints", [
                "getPointsDetailsData",
                "getMyPointsDetailData",
                "getMyPointsWeekData",
            ]),
            toggleLabel(index) {
                this.currentIndex = index;
                this.moduler = this.tabArray[index].component;
            },
            //跳转到帮助中心
            normalQuestion() {
                this.$router.push({
                    path: "/helpcenterProblem",
                });
            },
            seeAll() {
                this.currentIndex = 1;
                this.moduler = this.tabArray[1].component;
            },
            mouseOut: function () {
                this.wenhao = "/img/06.personalCenter/wenhao.png";
            },
            mouseOver: function () {
                this.wenhao = "/img/06.personalCenter/wenhaored.png";
            },
        },
        created() {},

        mounted() {
            this.getMyPointsDetailData();
            this.getMyPointsWeekData();
            this.getPointsDetailsData({
                page: 1,
                limit: 10,
            })
                .then((res) => {
                    console.log(res);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
    };
</script>
<style lang="scss" scoped>
    .integration-center {
        width: 1000px;
    }
    .intecenter-top-left {
        display: flex;
        justify-content: flex-start;
    }
    .integra-center-top {
        background: #fff;
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid #dd2619;
    }
    .intecenter-top-right {
        display: flex;
        align-items: center;
        margin-right: 55px;
        span {
            color: #999999;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            margin-left: 5px;
        }
        &:hover {
            span {
                color: #dd2619 !important;
            }
        }
    }
    .base {
        width: 120px;
        text-align: center;
        height: 44px;
        line-height: 44px;
        cursor: pointer;
        font-size: 14px;
        color: #666666;
        font-weight: 400;
        font-family: PingFangSC-Regular, PingFang SC;
    }
    .current {
        font-size: 14px;
        font-weight: 500;
        font-family: PingFangSc-Medium, PingFang SC;
        color: #ffffff;
        width: 120px;
        text-align: center;
        height: 44px;
        line-height: 44px;
        background: #dd2619;
    }
    .ivu-icon {
        color: #dd2619;
        vertical-align: baseline;
    }
    .ivu-date-picker-rel {
        color: #515a6e;
        a {
            color: #515a6e;
        }
    }
    .my-points {
        display: none;
    }
    .currentpoints {
        display: flex;
        justify-content: space-between;
        border: 2px solid rgba(228, 228, 228, 1);
        background: #ffffff;
        padding: 30px 0;
    }
    .currentpoints-left {
        width: 457px;
        height: 60px;
        border-right: 1px solid #ebebeb;
        text-align: center;
        background: #ffffff;
        display: flex;
        justify-content: left;
        span:first-child {
            margin-top: 10px;
            display: block;
        }
        .span2 {
            margin-top: 10px;
            display: block;
            font-size: 24px;
            color: red;
        }
        i {
            display: block;
            margin-top: 10px;
            margin-right: 0;
        }
        .current-left {
            margin-left: 60px;
        }
    }
    .currentpoints-right {
        display: flex;
        // justify-content: space-around;
        width: 100%;
        align-items: center;
        margin-left: 70px;
    }
    .plist {
        text-align: center;
        font-size: 26px;
        color: #262626;
        font-weight: 600;
    }
    .plist-another {
        text-align: center;
        font-size: 26px;
        color: #dd2619;
        font-weight: 600;
        margin: 5px 0 10px;
    }
    .cumulative-savings {
        text-align: center;
        div {
            icon {
                color: #555555;
                vertical-align: baseline;
            }
            span {
                text-align: center;
                font-size: 26px;
                color: #555555;
                font-weight: 600;
            }
        }
    }
    .recentdetails {
        margin-top: 10px;
        background: #ffffff;
        padding: 20px 40px 0 20px;
    }
    .recentdetails1 {
        padding-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }
    .recentdetails2 li {
        display: flex;
        justify-content: space-between;
        padding-top: 30px;
        border-bottom: 1px solid rgba(228, 228, 228, 1);
        padding-bottom: 30px;
        align-items: center;
    }
    .daylogin {
        margin-right: 20px;
        color: #dd2619;
        font-size: 20px;
        span {
            margin-right: -5px;
        }
    }
    .usage {
        background: #ffffff;
        padding: 20px 40px 0px 40px;
    }
    .usage1 {
        display: flex;
        justify-content: space-between;
        padding-bottom: 10px;
        font-size: 18px;
        font-weight: 400px;
        color: #2b2b2b;
        padding-bottom: 20px;
    }
    .usage2 {
        margin-top: 10px;
        li {
            display: flex;
            justify-content: space-between;
            margin-top: 10px;
            border-bottom: 1px solid rgba(228, 228, 228, 1);
            padding-bottom: 10px;
            align-items: center;
        }
    }
    .usage2-div {
        margin-right: 20px;
        color: #dd2619;
        font-size: 20px;
    }
    .usage3 {
        text-align: center;
        // margin-top: 30px;
    }
    .current-right {
        margin-left: 12px;
        text-align: left;
        width: 198px;
        margin-right: 29px;
        p {
            font-size: 18px;
            font-family: PingFangSC-Medium, PingFang SC;
            color: #262626;
            font-weight: 500;
            text-align: left;
        }
        span {
            color: #dd2619;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            border: 1px solid #dd2619;
            border-radius: 10px;
            padding: 2px;
        }
    }
    .after {
        font-size: 14px;
        color: #717171;
    }
    .cumulative-savings div span.money {
        font-size: 12px;
    }
    .recentdetails1-left {
        font-size: 18px;
        color: #424242;
        font-weight: 500;
    }
    .recentdetails1-right {
        cursor: pointer;
        font-size: 14px;
        color: #666666;
        .ivu-icon {
            color: #666666;
            margin-left: -5px;
        }
        &:hover {
            color: #dd2619;
            .ivu-icon {
                color: #dd2619;
            }
        }
    }
    .recent1 {
        font-size: 14px;
        color: #222222;
        font-weight: 500;
        font-family: PingFangSC-Medium, PingFang SC;
    }
    .recent2 {
        height: 12px;
        line-height: 12px;
        font-size: 12px;
        font-family: PingFangSC-Reular, PingFang SC;
        color: #999999;
        font-weight: 400;
        margin-top: 10px;
    }
    .current-span2 {
        color: #dd2619;
    }
    .usage1-span1 {
        margin-right: 20px;
    }
</style>
