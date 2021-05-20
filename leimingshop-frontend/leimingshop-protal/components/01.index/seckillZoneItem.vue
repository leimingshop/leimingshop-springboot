<template>
    <div
        class="artCon"
        v-lazy-container="{
            selector: '.main-pic-img',
            error: '/img/common/loading/200_200.png',
            loading: '/img/common/loading/200_200.png',
        }"
    >
        <img
            class="main-pic-img"
            @click="goDetails(data)"
            :data-src="$imgURL + data.goodsMainPicture"
            alt=""
        />
        <p class="artitle arttextoverflow" @click="goDetails(data)">
            {{ data.goodsName }}
        </p>
        <p class="artinfos arttextoverflow">{{ data.goodsSubTitle }}</p>
        <p class="artnum" v-if="isRemind == 0">
            <span>仅剩{{ data.activitySurplusStorage }}件</span
            ><Progress :percent="Number(data.salePercentage)"></Progress>
        </p>
        <div class="artBottom">
            <div class="artmoney">
                <div><span>￥</span>{{ data.activityPrice }}</div>
                <div>
                    <span style="transform: scale(0.67)">￥</span
                    >{{ data.specSellPrice }}
                </div>
            </div>
            <div
                class="artBtn artCursor"
                @click="
                    actBtn(
                        data.activityId,
                        data.activityType,
                        data.id,
                        data.specId
                    )"
                v-show="isRemind == 0"
            >
                立即购买
            </div>
            <div
                class="artBtn artCursor gray"
                v-show="isRemind !== 0 && remindFlag == 1"
            >
                已提醒
            </div>
            <div
                class="artBtn artCursor"
                @click="actBtn(data.activityId, data.activityType, data.id)"
                v-show="isRemind !== 0 && remindFlag == 0"
            >
                提醒我
            </div>
        </div>
        <Modal
            v-model="modal"
            width="240"
            footer-hide
            transfer
            class="success-body"
        >
            <div class="success-box">
                <img src="/img/01.index/success.png" />
                <span>设置提醒成功</span>
            </div>
        </Modal>
    </div>
</template>
<script>
    import {
        ActivitySeckillRemind, //秒杀专区-设置提醒
    } from "@/api/api_01main.js";
    import { mapState, mapActions, mapMutations } from "vuex";
    import Cookies from "js-cookie";
    export default {
        name: "seckillZoneItem",
        props: {
            data: {
                type: Object,
                required: true,
                default: {},
            },
            isRemind: {
                type: Number,
                required: true,
                default: 0,
            },
            sessionId: {
                type: String,
                required: true,
                default: "",
            },
        },
        data() {
            return {
                loginFlag: false, //登录状态
                remindFlag: 0,
                modal: false,
            };
        },
        watch: {},
        computed: {},
        mounted() {
            this.loginFlag = Cookies.get("auth") ? true : false;
            this.remindFlag = this.data.remindFlag;
            if(!this.loginFlag){
                    this.remindFlag = 0
            }
        },
        methods: {
            ...mapActions("main", ["actActivitySeckillRemind"]),
            actBtn(id, activityType, goodsId, specId) {
                if (this.isRemind !== 0 && this.remindFlag == 0) {
                    //即将开始的时段
                    if (!this.loginFlag) {
                        // 未登录时
                        this.$emit("toggleShow"); //登录弹窗
                    } else {
                        let obj = {
                            activityId: id,
                            sessionId: this.sessionId,
                            activityType: 3,
                            goodsId: goodsId,
                        }; //提醒我接口

                        ActivitySeckillRemind(obj)
                            .then((res) => {
                                if (res.code == 200) {
                                    this.remindFlag = 1;
                                    // return this.$Message.success('设置提醒成功');
                                    this.modal = true;
                                    setTimeout(() => {
                                        this.modal = false;
                                    }, 2000);
                                }
                            })
                            .catch((error) => {
                                console.log(error);
                            });
                    }
                } else if (this.isRemind == 0) {
                    //跳转页面
                    let newpage = this.$router.resolve({
                        path: "/goodsDetails",
                        query: {
                            goodsId: goodsId,
                            specId: specId,
                        },
                    });
                    window.open(newpage.href, "_blank");
                }
            },
            goDetails(data) {
                this.$router.push({
                    path: "/goodsDetails",
                    query: {
                        goodsId: data.id,
                        specId: data.specId,
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

    .gray {
        background-color: #f7f7f7;
        color: #cccccc;
    }

    .artCon {
        width: 290px;
        max-height: 410px;
        padding: 20px 30px 20px 30px;
        background-color: #fff;
        margin-bottom: 12px;
        margin-right: 13px;

        &:nth-of-type(4n) {
            margin-right: 0;
        }

        img {
            margin: 0 auto;
            display: block;
            width: 230px;
            height: 230px;
            cursor: pointer;
        }

        .artitle {
            width: 233px;
            height: 14px;
            margin-top: 18px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: #222222;
            line-height: 14px;
            cursor: pointer;
        }

        .artinfos {
            margin-top: 10px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 12px;
        }

        .artBottom {
            margin-top: 10px;
            display: flex;
        }

        .artmoney {
            flex-grow: 1;

            div:nth-of-type(1) {
                color: #dd2619;
                height: 20px;
                font-size: 20px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #dd2619;
                line-height: 20px;

                span {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #dd2619;
                    line-height: 14px;
                }
            }

            div:nth-of-type(2) {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #cccccc;
                line-height: 12px;
                text-decoration: line-through;
            }
        }

        .artBtn {
            width: 70px;
            height: 30px;
            background: linear-gradient(90deg, #df2b1c 0%, #ff4e03 100%);
            border-radius: 20px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 30px;
            text-align: center;
        }

        .artnum {
            line-height: 15px;
            font-size: 13px;
            display: flex;
            justify-content: space-around;
            margin: 10px 0;

            span {
                height: 12px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 12px;
                flex-grow: 1;
            }

            .ivu-progress-show-info {
                width: 165px;

                /deep/ .ivu-progress-inner {
                    background-color: #eee;
                }

                /deep/ .ivu-progress-outer {
                    width: 165px;
                    padding-right: 0;
                    margin-right: 0;
                }

                /deep/ .ivu-progress-text {
                    color: #dd2619;
                    display: none;
                }
            }
        }
    }
    .success-body {
        /deep/ .ivu-modal-mask {
            background-color: rgba(0, 0, 0, 0.13) !important;
        }
        /deep/ .ivu-modal {
            top: 40%;
        }
        /deep/ .ivu-modal-body {
            padding: 30px;
            .success-box {
                display: flex;
                flex-direction: column;
                align-items: center;
                img {
                    width: 60px;
                    height: 60px;
                }
                span {
                    margin: 20px 0 0 0;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: #222222;
                    line-height: 14px;
                }
            }
        }
        /deep/ .ivu-modal-close {
            display: none !important;
        }
    }
</style>
