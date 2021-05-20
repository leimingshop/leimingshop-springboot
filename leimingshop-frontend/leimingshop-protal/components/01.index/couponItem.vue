<template>
    <div class="artCon">
        <div class="artCon-left">
            <p class="artMoney1">
                <span style="font-size: 20px; line-height: 20px">￥</span>
                <span>{{ data.faceValue }}</span>
            </p>
            <p class="artMoney2">满{{ data.limitAmount }}元可用</p>
        </div>
        <div class="artcontainer">
            <div>
                <p class="artitle arttextoverflow">{{ data.storeName }}</p>
                <p class="artinfos arttextoverflow">
                    适用范围：{{
                        data.activityGoodsScope == 0
                            ? "全场通用"
                            : data.activityGoodsScope == 1
                            ? "指定店铺分类"
                            : data.activityGoodsScope == 2
                            ? "指定商品"
                            : "指定品牌"
                    }}
                </p>
                <p class="artdate">
                    <span
                        >有效期至：{{
                            data.validityType == 0
                                ? data.useEndDate
                                : "领取后" + data.validityDays + "天"
                        }}</span
                    >
                </p>
            </div>
            <div class="artCursor">
                <p
                    v-show="isReceived"
                    class="artbtn"
                    @click="goCouponsGoods(data.id)"
                >
                    去使用
                </p>
                <p class="artbtn artbtn-receive" @click="actReceive(data.id)">
                    去领取
                </p>
            </div>
        </div>
        <div v-show="isReceived" class="artreceive">
            <img src="/img/01.index/receive-coupon.png" />
        </div>
    </div>
</template>
<script>
    import { mapState, mapActions, mapMutations } from "vuex";
    import Cookies from "js-cookie";
    export default {
        name: "couponItem",
        props: {
            data: {
                type: Object,
                required: true,
                default: {},
            },
            index: {
                type: Number,
                required: true,
                default: 0,
            },
        },
        data() {
            return {
                loginFlag: false, //登录状态
                isReceived:false
            };
        },
        watch: {},

        components: {},
        computed: {},
        mounted() {
            this.loginFlag = Cookies.get("auth") ? true : false;
        },
        created(){
            this.isReceived = this.data.isReceived
        },
        methods: {
            ...mapActions("main", ["activityCouponReceive"]),
            actReceive(id) {
                //点击领取
                if(this.data.isReceived == true){
                        this.$Message.warning('请勿重复领取!')
                        this.isReceived = true
                        return false
                }
                if (!this.loginFlag) {
                    // 未登录时的删除
                    this.$emit("toggleShow");
                } else {
                    let obj = {
                        id: id,
                        index: this.index,
                    };
                    this.activityCouponReceive(obj);
                    setTimeout(() => {
                        if (this.data.isReceived == true) {
                            this.isReceived = true
                            this.$emit("successShow");
                        }
                    }, 1000);
                }
            },
            goCouponsGoods(data) {
                this.$router.push({
                    path: "/searchCouponsGoods",
                    query: {
                        activityId: data,
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

    .artCursor {
        cursor: pointer;
    }

    .artCon {
        height: 100px;
        width: 393px;
        margin-bottom: 20px;
        overflow: hidden;
        position: relative;
        display: flex;

        .artCon-left {
            float: left;
            width: 140px;
            color: #fff;
            text-align: center;
            background: url("/img/01.index/bgImg.png") no-repeat center/cover;
            display: flex;
            flex-direction: column;
            justify-content: center;

            .artMoney1 {
                width: 140px;
                font-size: 30px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #ffffff;
                line-height: 30px;
                text-align: center;
                display: flex;
                justify-content: center;
                align-items: flex-end;
            }

            .artMoney2 {
                margin-top: 10px;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 16px;
            }
        }

        &:nth-of-type(2n) {
            margin-right: 0;
        }

        img {
            display: inline-block;
            width: 175px;
            height: 100px;
        }

        .artcontainer {
            width: 253px;
            background-color: #ffffff;
            display: flex;
            justify-content: space-between;

            .artbtn {
                width: 70px;
                height: 30px;
                border: 1px solid #dd2619;
                border-radius: 22px;
                margin-top: 50px;
                margin-right: 10px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(221, 38, 25, 1);
                line-height: 30px;
                text-align: center;
            }

            .artbtn-receive {
                background: linear-gradient(90deg, #df2b1c 0%, #ff4e03 100%);
                color: #fff;
            }
        }

        .artitle {
            width: 160px;
            margin-top: 20px;
            margin-left: 10px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: #222222;
            line-height: 14px;
        }

        .artinfos {
            margin-top: 15px;
            margin-left: 10px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 12px;
        }

        .artdate {
            margin-top: 10px;
            margin-left: 10px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 12px;
        }

        .artreceive {
            position: absolute;
            z-index: 0;
            top: 0;
            right: 0;

            img {
                width: 50px;
                height: 45px;
            }
        }
    }
</style>
