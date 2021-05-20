<template>
    <div class="recommendedArea-part">
        <div
            class="recommendedArea-item"
            @click="goGoodsList()"
            v-lazy-container="{
                selector: 'img',
                error: '/img/common/loading/200_200.png',
                loading: '/img/common/loading/200_200.png',
            }"
        >
            <p>特惠推荐</p>
            <p>我猜得到你的需要</p>
            <img
                :data-src="
                    $imgURL +
                    '/group1/M00/00/8C/wKgAD17oxKiAR1voAAHFkSBoi_8843.png'
                "
                alt=""
            />
        </div>

        <div
            class="recommendedArea-item"
            @click="goGoodsList()"
            v-lazy-container="{
                selector: 'img',
                error: '/img/common/loading/200_200.png',
                loading: '/img/common/loading/200_200.png',
            }"
        >
            <p>人气推荐</p>
            <p>人气好物榜</p>
            <img
                :data-src="
                    $imgURL +
                    '/group1/M00/00/8C/wKgAD17oxIGAakiOAALaCASm-dM405.png'
                "
                alt=""
            />
        </div>

        <div
            class="recommendedArea-item"
            @click="goGoodsList()"
            v-lazy-container="{
                selector: 'img',
                error: '/img/common/loading/200_200.png',
                loading: '/img/common/loading/200_200.png',
            }"
        >
            <p>编辑精心整理推荐</p>
            <p>我猜得到你的需要</p>
            <img
                :data-src="
                    $imgURL +
                    '/group1/M00/00/8C/wKgAD17oxEiAPsF5AAL0VrW50YE153.png'
                "
                alt=""
            />
        </div>

        <div
            class="recommendedArea-item"
            @click="goCoupons()"
            v-lazy-container="{
                selector: 'img',
                error: '/img/common/loading/200_200.png',
                loading: '/img/common/loading/200_200.png',
            }"
        >
            <p>领券中心</p>
            <p>发现更多超值优惠券</p>
            <img
                :data-src="
                    $imgURL +
                    '/group1/M00/00/8C/wKgAD17ow82AZ4ZoAAFcEDbt9xc091.png'
                "
                alt=""
            />
        </div>
    </div>
</template>

<script>
    export default {
        name: "recommendedArea",
        props: {
            data: {
                type: Array,
                required: false,
                default: [],
            },
        },
        data() {
            return {};
        },

        methods: {
            // 跳转优惠券列表
            goCoupons() {
                this.$router.push({
                    path: "/couponCenter",
                    query: {},
                });
            },
            //商品列表
            goGoodsList() {
                let routeUrl = this.$router.resolve({
                    path: "/shopProClassification",
                    query: { storeId: this.$route.query.storeId },
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang="scss" scoped>
    .recommendedArea-part {
        display: flex;
        text-align: center;
        .recommendedArea-item {
            cursor: pointer;
            margin-right: 11px;
            margin-bottom: 11px;
            width: 292px;
            height: 340px;
            background-color: #ffffff;
            &:nth-of-type(4n) {
                margin-right: 0;
            }
            img {
                width: 180px;
                height: 180px;
                transition: all 0.6s;
            }
            p:nth-of-type(1) {
                margin: 40px 0 10px 0;
                font-size: 20px;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #333333;
                line-height: 20px;
            }
            p:nth-of-type(2) {
                margin-bottom: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(153, 153, 153, 1);
                line-height: 14px;
            }
            &:hover {
                img {
                    transform: scale(1.05);
                }
            }
        }
    }
</style>
