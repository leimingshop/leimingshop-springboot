<template>
    <div
        class="artCon"
        @click="goDetails()"
        v-lazy-container="{
            selector: 'img',
            error: '/img/common/loading/200_200.png',
            loading: '/img/common/loading/200_200.png',
        }"
    >
        <img :data-src="$imgURL + data.goodsMainPicture" alt="" />
        <p class="artitle arttextoverflow">{{ data.goodsName }}</p>
        <p class="artinfos arttextoverflow">{{ data.goodsSubTitle }}</p>
        <p class="artmoney">
            <span>
                <span style="font-size: 14px; line-height: 18px">￥</span>
                <span style="line-height: 20px">{{
                    data.specIntegerPrize
                }}</span>
                <span style="font-size: 12px; line-height: 16px">{{
                    data.specFloatPrize
                }}</span>
            </span>
            <span class="line-price">￥{{ data.specSellPrice }}</span>
        </p>
        <p class="artnum">
            <span class="last-num">仅剩{{ activitySurplusStorage }}件</span
            ><Progress
                :percent="Number(data.salePercentage)"
                status="active"
            ></Progress>
        </p>
    </div>
</template>
<script>
    export default {
        name: "seckillGoods",
        props: {
            data: {
                type: Object,
                required: true,
                default: {},
            },
        },
        head() {
            return {
                title: "秒杀中心",
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
                activitySurplusStorage:
                    (this.data.activitySurplusStorage + "").length > 4
                        ? "1万+"
                        : this.data.activitySurplusStorage,
            };
        },
        watch: {},
        computed: {},
        methods: {
            goDetails() {
                this.$router.push({
                    path: "/goodsDetails",
                    query: {
                        goodsId: this.data.id,
                        specId: this.data.specId,
                    },
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .arttextoverflow {
        white-space: nowrap;
        overflow: hidden;
        word-break: break-all;
        word-wrap: break-word;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
    }
    .artCon {
        height: 100%;
        width: 240px;
        padding: 30px 30px 30px 30px;
        cursor: pointer;
        img {
            display: block;
            width: 180px;
            height: 180px;
        }
        .artitle {
            line-height: 14px;
            margin-top: 12px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: #222222;
        }
        .artinfos {
            margin-top: 8px;
            margin-bottom: 12px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #999999;
            line-height: 12px;
        }
        .artmoney {
            display: flex;
            align-items: flex-end;
            span:nth-of-type(1) {
                display: flex;
                align-items: flex-end;
                color: #dd2619;
                height: 20px;
                font-size: 20px;
                font-weight: 600;
            }
            .line-price {
                height: 14px;
                margin-left: 8px;
                font-size: 12px;
                font-weight: 400;
                line-height: 12px;
                text-decoration: line-through;
                color: #999999;
            }
        }
        .artnum {
            margin-top: 10px;
            line-height: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .last-num {
                width: 68px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 12px;
            }
            .ivu-progress-show-info {
                width: 120px;
                display: flex;
                justify-content: flex-end;
                align-items: center;
                /deep/ .ivu-progress-inner {
                    height: 8px;
                    background: #ffecea;
                    /deep/.ivu-progress-bg {
                        height: 8px !important;
                        background: linear-gradient(
                            270deg,
                            #fe5b15 0%,
                            #df3022 100%
                        );
                        border-radius: 4px !important;
                    }
                }
                /deep/ .ivu-progress-outer {
                    width: 81px;
                    padding-right: 0;
                    margin-right: 0;
                }
                /deep/ .ivu-progress-text {
                    font-size: 12x;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #dd2619;
                    line-height: 12px;
                }
            }
        }
    }
</style>

