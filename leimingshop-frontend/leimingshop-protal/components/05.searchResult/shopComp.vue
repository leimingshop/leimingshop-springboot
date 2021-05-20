<template>
    <Card :bordered="bordered" :class="['shop-comp']">
        <div
            class="p-img"
            v-lazy-container="{
                selector: 'img',
                error: '/img/common/loading/200_200.png',
                loading: '/img/common/loading/200_200.png',
            }"
        >
            <img :data-src="shopLogo" class="anim" alt="" />
        </div>

        <div class="s-content">
            <div class="s-name" :title="storeName">
                <div class="s-type" v-if="storeType == 1">自营</div>
                <span v-text="storeName"></span>
            </div>
            <div class="s-grade-title">{{ storeGradeName }}</div>
            <div class="s-grade-icon">
                <em>{{ storeGradeTitle }}</em>
                <Rate v-model="storeStar" custom-icon="rate-icon" disabled />
            </div>
        </div>
        <div class="button-box">
            <a
                class="openShopBtn"
                shape="circle"
                type="default"
                @click="handleShopDetail()"
                >进入店铺</a
            >
        </div>
    </Card>
</template>

<script>
    export default {
        name: "shopComp",

        data() {
            return {
                storeGradeTitle: "店铺星级",
            };
        },

        props: {
            // 是否显示card边框
            bordered: {
                type: Boolean,
                default: false,
            },

            // 店铺id
            storeId: {
                type: String,
                default: "",
            },

            // 店铺名称
            storeName: {
                type: String,
                default: "",
            },

            // 店铺类型 1:自营商户，2:普通商户
            storeType: {
                type: String | Number,
                default: "",
            },

            // 店铺logo
            storeLogo: {
                type: String,
                default: "",
            },

            // 店铺评级名称
            storeGradeName: {
                type: String,
                default: "",
            },

            // 店铺评级名称
            storeStar: {
                type: Number,
                default: "",
            },
        },

        computed: {
            shopLogo() {
                return `${this.$imgURL}${this.storeLogo}`;
            },
        },

        watch: {},

        methods: {
            handleShopDetail() {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: this.storeId },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    @keyframes mymove {
        from {
            transform: scale(0);
        }
        to {
            transform: scale(1);
        }
    }

    .shop-comp {
        position: relative;
        width: 100%;
        border: 2px solid #ffffff;
        border-radius: 2px;
        transition: border 0.2s, box-shadow 0.2s;

        &:hover {
            cursor: pointer;
            box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
            .button-box {
                display: block;
            }
        }
        .button-box {
            display: none;
            position: absolute;
            bottom: 0;
            width: 196px;
            height: 55px;
            background: url("/img/01.index/gradients.png") no-repeat center/cover;
            padding: 5px 0 0 0;
        }
        /deep/ .ivu-card-body {
            width: 100%;
            height: 100%;
            padding: 20px 16px 10px 16px;
            position: relative;
        }
    }

    .p-img {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 200px;
        height: 200px;
        margin: 0 auto;

        img {
            width: 100%;
            height: 100%;
            &.anim {
                animation: mymove 0.3s 1;
            }
        }
    }

    /deep/ .rate-icon {
        width: 14px;
        height: 14px;
        background: url("/img/03.goodsClass/03-03.goodsDetail/collectAll.png") top
            left/cover;
    }

    /deep/ .ivu-rate-star-full {
        .rate-icon {
            background: url("/img/03.goodsClass/03-03.goodsDetail/collected.png")
                top left/cover;
        }
    }

    .s-content {
        padding: 0;

        .s-name {
            margin-top: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: #222222;
            line-height: 14px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            display: flex;
        }

        .s-type {
            width: 26px;
            height: 14px;
            background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
            padding: 0;
            border-radius: 2px;
            margin-right: 5px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 14px;
            text-align: center;
        }

        .s-grade-title {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 500;
            color: #666666;
            line-height: 12px;
            margin: 10px 0 6px 0;
        }

        .s-grade-icon {
            em {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #999999;
                line-height: 12px;
                font-style: normal;
            }

            .ivu-rate {
                margin-top: -10px;
            }
        }
    }

    .ivu-divider {
        margin: 15px 0 18px;
        background: #f5f5f5;
    }

    .openShopBtn {
        width: 110px;
        height: 30px;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
        border-radius: 23px;
        margin: 5px auto;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
        line-height: 30px;
        text-align: center;
        display: block;
    }
</style>
