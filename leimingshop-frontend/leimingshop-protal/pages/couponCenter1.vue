<template>
    <div class="container">
        <Modal
            v-model="modal1"
            width="426px"
            height="440px"
            class="success-modal"
        >
            <successfully-received
                :modal1="modal1"
                @closeSuccessShow="closeSuccessShow"
            ></successfully-received>
        </Modal>
        <!-- 顶部搜索部分 -->
        <search-second-level></search-second-level>

        <!-- 面包屑 -->
        <breadcrumb1 :list="breadcrumbList" />
        <!-- 中间的banner广告 -->
        <div
            class="banner-part warp-con"
            v-lazy:background-image="'/img/01.index/banner.png'"
        ></div>

        <!-- 优惠券 -->
        <div class="couponcenter-part warp-con">
            <p>
                <img
                    style="height: 20px; width: auto"
                    v-lazy="'/img/01.index/img-coupon.png'"
                    :key="'/img/01.index/img-coupon.png'"
                />
            </p>
            <!-- <load-more :completed="iscompleted"   @changeData="changPages">    目前不写分页-->
            <div class="artCouponcenter">
                <coupon-item
                    class="artCon"
                    @successShow="successShow()"
                    v-for="(item, index) in ActivityCouponCenterData"
                    @toggleShow="isShowModal = true"
                    :key="index"
                    :index="index"
                    :data="item"
                ></coupon-item>

                <!-- {{ActivityCouponCenterData}} -->

                <div
                    class="no-artCouponcenter"
                    v-if="ActivityCouponCenterData.length == 0"
                >
                    <div class="no-data-main-content">
                        <img :src="'/img/04.shoppingCar/Nullcoupon.png'" alt />
                    </div>
                </div>
            </div>
        </div>

        <Modal
            v-model="isShowModal"
            title="您尚未登录，请登录后领取"
            width="406px"
            :footer-hide="true"
            class="login-modal"
        >
            <loginPopup></loginPopup>
        </Modal>
    </div>
</template>

<script>
    import couponItem from "@/components/01.index/couponItem.vue"; //秒杀专区
    import successfullyReceived from "@/components/01.index/successfullyReceived.vue"; //领取成功弹窗
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import loginPopup from "@/components/common/login";
    import { mapState, mapActions, mapMutations } from "vuex";
    export default {
        name: "Index",
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
                modal1: false,
                isShowModal: false,
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "领券中心",
                        toPath: "",
                    },
                ]),
            };
        },
        components: {
            couponItem,
            successfullyReceived,
            loginPopup,
            breadcrumb1,
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "ActivityCouponCenterData",
                "iscompleted",
            ]),
        },
        async fetch({ store }) {
            await store.dispatch("main/activityCouponCenter");
        },
        mounted() {
            // this.activityCouponCenter();
        },
        methods: {
            ...mapActions("main", ["activityCouponCenter"]),
            changPages(page) {
                if (!this.iscompleted) {
                    this.activityCouponCenter(page);
                }
            },
            successShow() {
                this.modal1 = true;
            },
            closeSuccessShow() {
                this.modal1 = false;
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
        padding-bottom: 40px;
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

    .success-modal {
        /deep/ .ivu-modal {
            border-radius: 12px;
            border: 4px solid rgba(51, 51, 51, 0.08);
            margin-top: 12%;

            /deep/ .ivu-modal-header {
                padding-top: 33px;
                font-size: 24px;
                font-weight: 600;
                border-bottom: 0px;
                font-family: PingFangSC-Regular, PingFang SC;
                color: rgba(0, 0, 0, 1);
                line-height: 24px;
                letter-spacing: 3px;
                text-align: center;

                .ivu-modal-header p,
                .ivu-modal-header-inner {
                    font-size: 24px;
                    height: 24px;
                }
            }
            .ivu-modal-body {
                text-align: center;
                padding: 20px;
            }
            /deep/ .ivu-modal-content {
                width: 440px;
                height: 280px;
                background: #ffffff;

                /deep/ .ivu-modal-close {
                    right: 5px;
                    top: 1px;
                    .ivu-icon-ios-close {
                        font-size: 40px !important;
                    }
                }
            }
            .ivu-modal-footer {
                display: none;
            }
        }
    }
    .login-modal {
        /deep/ .ivu-modal {
            top: 25%;
        }
        /deep/ .ivu-modal-content {
            padding-bottom: 10px;
            border-radius: 10px;
            position: relative;
            /deep/ .ivu-modal-close {
                position: absolute;
                top: 0;
            }
            /deep/ .ivu-modal-header {
                height: 34px;
                background: #f3f3f3;
                padding: 0 20px;
                border-top-left-radius: 10px;
                border-top-right-radius: 10px;
                .ivu-modal-header-inner {
                    height: 34px;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #666666;
                    line-height: 34px;
                }
            }
            .ivu-modal-body {
                padding: 0;
                /deep/ .content-box {
                    padding-top: 20px;
                }
            }
        }
    }

    //中间banner部分
    .banner-part {
        height: 120px;
        background-size: cover;
        -webkit-background-size: cover;
        -o-background-size: cover;
        background-position: center 0;
    }

    .couponcenter-part {
        p {
            text-align: center;
            margin: 20px 0 20px 0;
        }

        .artCouponcenter {
            .artCon {
                margin-right: 10px;
            }
            .artCon:nth-child(3n + 3) {
                margin-right: 0;
            }
            display: flex;
            flex-wrap: wrap;
        }

        .no-artCouponcenter {
            margin: 122px auto;
        }
    }
</style>
