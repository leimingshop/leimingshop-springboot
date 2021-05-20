<template>
    <Poptip placement="bottom-start" v-model="datas.couponVisible" width="260">
        <div
            style="cursor: pointer; width: 65px"
            class="content-good-select"
            @click="haha1(datas, list)"
        >
            优惠券<img src="/img/04.shoppingCar/open.png" alt="" />
        </div>
        <div
            class="api oo"
            slot="content"
            :class="{
                other:
                    couponList.canRecList.length +
                        couponList.recedList.length <=
                    5,
            }"
        >
            <div
                v-for="(smallcouponListItem1,
                smallcouponIndex1) in couponList.canRecList"
                :key="'coupons' + smallcouponIndex1"
                class="contentItem"
            >
                <div class="itemLeft">
                    <div class="content-good-coupon">
                        ￥{{
                            parseFloat(smallcouponListItem1.faceValue).toFixed(
                                2
                            )
                        }}
                    </div>
                    <div
                        class="font-12"
                        style="
                            width: 110px;
                            margin: 0 0 0 8px;
                            font-size: 12px;
                            color: rgba(34, 34, 34, 1);
                            font-weight: 400;
                        "
                    >
                        满{{ smallcouponListItem1.limitAmount }}减{{
                            smallcouponListItem1.faceValue
                        }}
                    </div>
                </div>
                <div
                    style="cursor: pointer"
                    class="content-good-receive"
                    @click="getcoupons(smallcouponListItem1)"
                >
                    领取
                </div>
            </div>
            <div
                v-for="(smallcouponListItem2,
                smallcouponIndex2) in couponList.recedList"
                :key="'coupon' + smallcouponIndex2"
                class="contentItem"
            >
                <template v-if="couponList.recedList.length != 0">
                    <div class="itemLeft">
                        <div class="content-good-coupon">
                            ￥{{ smallcouponListItem2.faceValue }}
                        </div>
                        <div
                            style="
                                width: 110px;
                                margin: 0 0 0 8px;
                                font-size: 12px;
                            "
                        >
                            满{{ smallcouponListItem2.limitAmount }}减{{
                                smallcouponListItem2.faceValue
                            }}
                        </div>
                    </div>

                    <div class="content-good-received">已领取</div>
                </template>
            </div>
        </div>
    </Poptip>
</template>

<script>
    import { GoodscouponList } from "@/api/api_04.shoppingCar.js";
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部

    export default {
        props: ["id", "list", "datas"],
        data() {
            return {
                couponList: { canRecList: [], recedList: [] },
            };
        },
        methods: {
            ...mapActions("shoppingCarIndex", [
                "getcoupon", // 领取优惠券
            ]),
            haha1(item, list) {
                this.datas.couponVisible = true;
                for (var i = 0; i < list.length; i++) {
                    list[i].couponVisible = false;
                }
                this.getListData();
            },
            getListData() {
                GoodscouponList({ goodsId: this.id, type: 1 })
                    .then((res) => {
                        if (res.code == 200) {
                            this.couponList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            getcoupons(item) {
                let obj = { couponId: item.id };
                this.getcoupon(obj);
                item.couponVisible = false;
            },
        },
    };
</script>

<style lang="scss" scoped>
    .contentItem {
        width: 100%;
        display: flex;
        align-items: center;
        margin: 10px 0 0 0;
        justify-content: space-between;
        padding-right: 10px;
        .itemLeft {
            width: 76%;
            display: flex;
            align-items: center;
        }
    }
    .content-good-coupon {
        width: 60px;
        padding: 0 3px;
        height: 20px;
        // line-height: 16px;
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        // text-align: center;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .content-good-received {
        width: 34px;
        height: 16px;
        font-size: 8px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(153, 153, 153, 1);
        line-height: 16px;
        text-align: center;
        letter-spacing: 1px;
    }
    .content-good-receive {
        width: 38px;
        height: 20px;
        background: rgba(221, 38, 25, 1);
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(255, 255, 255, 1);
        line-height: 20px;
        text-align: center;
        letter-spacing: 1px;
        cursor: pointer;
    }
    .content-good-select {
        width: 52px;
        height: 18px;
        // line-height: 18px;
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        // text-align: center;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(221, 38, 25, 1);
        display: flex;
        justify-content: center;
        align-items: center;
        // line-height: 12px;
        img {
            width: 9px;
            height: 6px;
            margin-left: 4px;
        }
    }
    /deep/.ivu-select-small {
        color: red;
    }
    .ivu-select-small.ivu-select-single
        .ivu-select-selection
        .ivu-select-placeholder {
        color: red;
    }
    .api {
        max-height: 150px;
    }
    /deep/ .ivu-poptip-body {
        padding: 10px;
        padding-right: 0;
    }
    .other {
        padding-right: 10px;
    }
</style>
