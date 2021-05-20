<template>
    <div
        class="artCon artCursor"
        @click="goShop(data)"
        v-lazy-container="{
            selector: 'img',
            error: '/img/common/loading/200_200.png',
            loading: '/img/common/loading/200_200.png',
        }"
    >
        <p class="artFlog">
            <span v-if="data && data.labelName">{{
                data && data.labelName
            }}</span>
        </p>
        <img
            :data-src="$imgURL + data.specMainPicture + '_200x200.png'"
            alt=""
        />
        <p class="artitle arttextoverflow">{{ data && data.goodsName }}</p>
        <p class="artinfos arttextoverflow">{{ data && data.goodsSubTitle }}</p>
        <p class="artmoney">
            <a>￥</a
            ><span
                >{{ data && data.specIntegerPrize
                }}<span style="font-size: 14px">{{
                    data && data.specFloatPrize
                }}</span></span
            >
        </p>
    </div>
</template>
<script>
    export default {
        name: "recommendedItem",
        props: {
            data: {
                type: Object,
                required: true,
                default: {},
            },
        },
        watch: {},
        computed: {},
        methods: {
            // actToDetail(){
            //     this.$router.push('/goodsDetails',{'goods_id':data.id})
            // },
            goShop(data) {
                let newpage = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: data.goodsId,
                        specId: data.specId,
                    },
                });
                window.open(newpage.href, "_blank");
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
        border-radius: 0px;
        border: 1px solid #f5f5f5;
        &:hover {
            box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
            .artitle {
                color: #dd2619;
            }
        }
    }

    .artCon {
        height: 322px;
        width: 232px;
        padding: 20px 16px 0px 16px;
        margin-right: 10px;
        margin-bottom: 10px;
        background-color: #fff;

        &:nth-of-type(5n) {
            margin-right: 0;
        }

        & > img {
            margin: 0 auto;
            display: block;
            width: 200px;
            height: 200px;
        }

        .artitle {
            margin-top: 19px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(51, 51, 51, 1);
            line-height: 14px;
        }

        .artinfos {
            margin-top: 8px;
            margin-bottom: 10px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(153, 153, 153, 1);
            line-height: 12px;
        }

        .artmoney {
            a {
                color: #dd2619;
                font-size: 14px;
            }
            span:nth-of-type(1) {
                color: #dd2619;
                height: 20px;
                font-size: 20px;
                font-weight: 600;
                line-height: 20px;
            }

            span:nth-of-type(2) {
                height: 14px;
                margin-left: 8px;
                font-size: 14px;
                font-weight: 400;
                line-height: 14px;
                text-decoration: line-through;
                color: #999999;
            }
        }

        .artFlog {
            position: absolute;
            height: 20px;

            span {
                display: inline-block;
                width: 40px;
                height: 20px;
                line-height: 20px;
                text-align: center;
                background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(255, 255, 255, 1);
                text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.21);
            }
        }
    }
</style>
