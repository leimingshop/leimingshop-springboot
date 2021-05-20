<template>
    <div class="container">
        <!-- 店铺公共头部 -->
        <common-shop-head
            :StoreInfoStoreIdData="StoreInfoStoreIdData"
        ></common-shop-head>

        <!-- 中间的轮播图 -->

        <!-- 推荐专区 -->
        <div class="word">
            <img
                src="/img/02.shopIndex/title1.png"
                v-if="StoreRecommendData.length"
                alt
            />
        </div>
        <!-- <p class="artTitle warp-con" v-if="StoreRecommendData.length">推荐专区 <span></span></p> -->
        <recommended-area
            class="warp-con"
            :data="StoreRecommendData"
        ></recommended-area>

        <component
            :is="actIndexFloorItem.floorStyleName"
            v-for="(actIndexFloorItem, actIndexFloorIndex) of actIndexFloorData"
            :key="actIndexFloorIndex"
            :data="actIndexFloorItem"
        ></component>

        <!-- 专题  先隐藏 -->

        <p v-if="false" class="artTitle warp-con">专题 <span></span></p>
        <special-area
            v-if="false"
            :seckillGoodsData="seckillGoodsData"
            class="warp-con"
        ></special-area>

        <div class="word">
            <img src="/img/02.shopIndex/title3.png" v-if="false" alt />
        </div>
        <!-- 品牌故事   先隐藏-->
        <!-- <p v-if="false" class="artTitle warp-con">品牌故事 <span></span></p> -->
        <brandstory-area
            v-if="false"
            :seckillGoodsData="seckillGoodsData"
            class="warp-con"
        ></brandstory-area>
    </div>
</template>

<script>
    import commonShopHead from "@/components/02.shopIndex/commonShopHead"; //店铺通用头部
    import enjoyGoodThings1 from "@/components/01.index/enjoyGoodThingsStyle1.vue"; //优享好物1
    import enjoyGoodThings2 from "@/components/01.index/enjoyGoodThingsStyle2.vue"; //优享好物2
    import enjoyGoodThings3 from "@/components/01.index/enjoyGoodThingsStyle3.vue"; //优享好物3
    import recommendedArea from "@/components/02.shopIndex/recommendedArea.vue"; //推荐专区
    import specialArea from "@/components/02.shopIndex/specialArea.vue"; //专题
    import brandstoryArea from "@/components/02.shopIndex/brandstoryArea.vue"; //品牌故事
    import { IndexAdv, IndexNav, IndexFloor } from "@/api/api_01main.js";
    import { mapState, mapActions, mapMutations } from "vuex";
    export default {
        props: {
            StoreInfoStoreIdData: {
                type: Object,
                required: false,
                default: {},
            },
        },
        data() {
            return {
                carouselVal: 0,
                currentShow: false,
                advKey: "pcStoreIndex", //pcIndex pc首页 storeIndex 店铺首页
                storeId: this.$route.query.storeId, //店铺id
                seckillGoodsData: {
                    img: "/img/01.index/3.jpg",
                    title: "Apple iPhone SE(A2298)",
                    info: "匠心裁剪 垂感质地",
                    money: "579.00",
                    originalPrice: "759.00",
                    num: "100",
                },
                IndexAdvInPage: [],
                IndexNavList: null,
                actIndexFloorData: [], //楼层数据
            };
        },
        components: {
            enjoyGoodThings2,
            enjoyGoodThings3,
            enjoyGoodThings1,
            recommendedArea,
            specialArea,
            brandstoryArea,
            commonShopHead,
        },
        computed: {
            ...mapState("main", [
                "IndexAdvData",
                "IndexFloorData",
                "StoreRecommendData",
                "GoodsStoreClassAllData",
            ]),
        },
        mounted() {
            let obj = {
                advKey: this.advKey, //pcIndex pc首页 storeIndex 店铺首页
            };
            if (this.storeId) {
                obj.storeId = this.storeId; //店铺Id
            }
            // this.actIndexAdv(obj);
            IndexAdv(obj)
                .then((res) => {
                    this.IndexAdvInPage = res.data;
                })
                .catch((err) => console.log(err));
            this.actIndexFloor({
                storeId: this.storeId,
            });
            this.actStoreRecommend({
                storeId: this.storeId,
            });
            console.log(this.storeId);
            this.actGoodsStoreClassAll({
                storeId: this.storeId,
            });
            this.getIndexNav();
        },
        methods: {
            ...mapActions("main", [
                "actIndexAdv",
                // 'actIndexFloor',
                "actStoreRecommend",
                "actGoodsStoreClassAll",
            ]),
            actIndexFloor(params) {
                IndexFloor(params)
                    .then((res) => {
                        if (res.code == 200) {
                            this.actIndexFloorData = res.data;
                            for (
                                var i = 0;
                                i < this.actIndexFloorData.length;
                                i++
                            ) {
                                this.actIndexFloorData[i].floorStyleName =
                                    "enjoy-good-things" +
                                    this.actIndexFloorData[i].floorStyle;
                                this.actIndexFloorData[
                                    i
                                ].linkType = this.actIndexFloorData[i].actionType;
                                this.actIndexFloorData[
                                    i
                                ].typeKeyWord = this.actIndexFloorData[
                                    i
                                ].actionParams;
                                for (
                                    var j = 0;
                                    j <
                                    this.actIndexFloorData[i]
                                        .webFloorLinkConfigDTOList.length;
                                    j++
                                ) {
                                    if (
                                        this.actIndexFloorData[i]
                                            .webFloorLinkConfigDTOList[j]
                                            .relationType &&
                                        this.actIndexFloorData[i]
                                            .webFloorLinkConfigDTOList[j]
                                            .relationTarget
                                    ) {
                                        this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].linkType = this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[j].relationType;
                                        this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].typeKeyWord = this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].relationTarget;
                                    }
                                    if (
                                        this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].typeKeyWord.indexOf(",") !== -1
                                    ) {
                                        var a = this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].typeKeyWord.split(",")[2];
                                        var specIntegerPrize = a.substring(
                                            0,
                                            a.indexOf(".")
                                        );
                                        var specFloatPrize = a.substring(
                                            a.indexOf(".")
                                        );
                                        this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].specIntegerPrize = specIntegerPrize;
                                        this.actIndexFloorData[
                                            i
                                        ].webFloorLinkConfigDTOList[
                                            j
                                        ].specFloatPrize = specFloatPrize;
                                    }
                                }
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            artHandleOpen(e) {
                this.currentShow = e;
            },
            goClassPage(storeGcIds) {
                this.$router.push({
                    path: "/shopProClassification",
                    query: {
                        storeId: this.storeId,
                        storeGcIds: storeGcIds,
                    },
                });
            },
            //获取首页导航栏
            getIndexNav() {
                IndexNav(`storeId=${this.storeId}`)
                    .then((res) => {
                        console.log(res.data);
                        if (res.code == 200) {
                            this.IndexNavList = res.data;
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            //首页导航栏跳转
            goNavPage(item) {
                console.log(item);
                if (item.relationType == 1) {
                    window.open(item.relationParams, "_blank");
                } else if (item.relationType == 2) {
                    let routeUrl = this.$router.resolve({
                        path: "/proClassification",
                        query: {
                            classId: item.relationParams,
                        },
                    });

                    window.open(routeUrl.href, "_blank");
                }
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

    .container {
        background-color: #f6f6f6;
        padding-bottom: 80px;
    }
    .word {
        width: 1200px;
        height: 36px;
        margin: 14px auto 20px;
        img {
            width: 141px;
            height: 100%;
        }
    }
    .warp-con {
        width: 1200px;
        margin: 0 auto;

        .ivu-row-flex {
            padding-left: 22px;
            display: flex;
            flex-grow: 1;
            flex-wrap: nowrap;
            text-align: center;
            line-height: 46px;
            font-size: 15px;

            .ivu-col {
                padding: 0 20px 0 20px;

                a {
                    color: #ffffff;

                    &:hover {
                        color: rgba(221, 38, 25, 1);
                    }
                }
            }
        }
    }

    .nav-warp {
        display: flex;
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

    .advertisement-part {
        .advertisement-warp {
            height: 110px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            img {
                width: 230px;
                height: 97px;
            }

            .advertisement-right {
                display: flex;

                img {
                    margin-right: 20px;
                    height: 97px;
                    width: 30px;
                }

                .advertisement-right-con {
                    margin-right: 30px;
                    display: flex;

                    p:nth-of-type(1) {
                        margin-top: 20px;
                        margin-bottom: 8px;
                        font-size: 16px;
                        font-weight: 500;
                    }

                    p:nth-of-type(2) {
                        font-size: 16px;
                        font-weight: 600;
                    }
                }
            }
        }
    }

    //头部导航
    .nav-part {
        position: relative;
        z-index: 18;
        height: 46px;
        background-color: #2c2c2c;

        .ivu-menu,
        .ivu-menu-horizontal {
            position: static;
            height: 46px;
            line-height: 46px;
            background-color: #2c2c2c;

            .ivu-menu-item {
                padding: 0 25px;

                .ivu-dropdown-menu > div {
                    margin-bottom: 20px;
                }
            }

            //查看所有商品
            .artdropdown {
                /deep/ .ivu-select-dropdown {
                    width: 1200px;
                    height: 550px;
                    overflow: hidden;
                    top: 100% !important;
                    left: 0 !important;
                    margin: 0 0;
                    z-index: 9999;
                    padding: 30px 30px;
                    border-radius: 0;
                }

                .artdropdown-con {
                    font-size: 14px;
                    font-family: PingFangSC-Medium, PingFang SC;
                    font-weight: 600;
                    color: rgba(48, 48, 48, 1);
                    line-height: 14px;
                    margin-bottom: 10px;

                    span {
                        margin-right: 6px;
                        display: inline-block;
                        width: 3px;
                        height: 15px;
                        background: rgba(221, 38, 25, 1);
                        vertical-align: bottom;
                    }
                }

                .artdropdown-con-text {
                    font-size: 13px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(136, 136, 136, 1);
                    line-height: 18px;

                    span {
                        margin-right: 10px;
                    }
                }
            }
        }

        .ivu-menu-item-active,
        .ivn-menu-opened {
            background-color: #dd2619;
        }
    }

    // 轮播图
    /deep/ .ivu-carousel {
        height: 550px;

        img {
            width: 100%;
            height: 550px;
        }

        /deep/ .ivu-carousel-arrow.left {
            left: 16%;
        }

        /deep/ .ivu-carousel-arrow.right {
            right: 16%;
        }
    }

    .artTitle {
        height: 36px;
        line-height: 36px;
        font-size: 36px;
        font-family: Helvetica;
        color: #222222;
        font-weight: 600;
        margin-top: 14px;
        margin-bottom: 20px;
        // line-height: 36px;
        // margin: 30px auto;

        span {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(153, 153, 153, 1);
            line-height: 14px;
            margin-left: 20px;
        }
    }
</style>
