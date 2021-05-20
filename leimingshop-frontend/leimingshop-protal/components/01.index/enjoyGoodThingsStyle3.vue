<template>
    <div style="font-size: 0px">
        <Carousel
            v-show="data.advDTOList.length > 1"
            autoplay
            loop
            @on-click="actCarousel"
            style="width: 1200px; height: 120px; margin: 0 auto"
        >
            <CarouselItem
                v-for="(item, index) in data.advDTOList"
                :key="index"
                class="advertisement-part artCursor warp-con backgroundcolorFFF"
            >
                <img
                    :src="$imgURL + item.imageUrl"
                />
            </CarouselItem>
        </Carousel>
        <div
            style="margin-top: 20px"
            v-show="data.advDTOList.length == 1"
            v-for="(item, index) in data.advDTOList"
            :key="index"
            class="advertisement-part artCursor warp-con backgroundcolorFFF"
        >
            <img
                :src="$imgURL + item.imageUrl"
                @click="actCarousel(index)"
            />
        </div>

        <div class="enjoygoodthings3 warp-con backgroundcolorFFF">
            <p
                class="arttitle artCursor"
                v-if="titleIsShow"
                @click="goAD(data)"
            >
                <i></i><span>{{ data.floorName }}</span>
            </p>
            <div class="enjoygoodthings3-con">
                <div
                    class="enjoygoodthings-con-left artCursorbor"
                    @click="goAD(data.webFloorLinkConfigDTOList[0])"
                    v-if="data.webFloorLinkConfigDTOList.length > 0"
                    v-lazy-container="{
                        selector: 'img',
                        error: '/img/common/loading/330_530.png',
                        loading: '/img/common/loading/330_530.png',
                    }"
                >
                    <div
                        class="floor_imgStyle_1_bg"
                        v-show="data.webFloorLinkConfigDTOList[0].title !== ''"
                    >
                        <p>{{ data.webFloorLinkConfigDTOList[0].title }}</p>
                        <p>{{ data.webFloorLinkConfigDTOList[0].subTitle }}</p>
                    </div>
                    <img
                        :data-src="
                            $imgURL + data.webFloorLinkConfigDTOList[0].imgUrl
                        "
                        alt=""
                    />
                </div>
                <div class="enjoygoodthings-con-right">
                    <div class="enjoygoodthings-con-right-r">
                        <template
                            v-for="(goodsItem,
                            goodsIndex) in data.webFloorLinkConfigDTOList"
                        >
                            <div
                                class="artCon artCursorbor"
                                :key="goodsIndex"
                                @click="goAD(goodsItem)"
                                v-if="goodsIndex > 0 && goodsIndex < 3"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <template v-if="goodsItem.configStyle == 2">
                                    <img
                                        class="goodsImg"
                                        :data-src="
                                            $imgURL +
                                            goodsItem.imgUrl +
                                            '_200x200.png'
                                        "
                                        alt=""
                                    />
                                    <p class="artitle arttextoverflow">
                                        {{ goodsItem.goodsName }}
                                    </p>
                                    <!-- <p class="artinfos arttextoverflow">{{goodsItem.info}}</p> -->
                                    <p class="artinfos arttextoverflow">
                                        {{ goodsItem.subTitle }}
                                    </p>
                                    <p class="artmoney">
                                        <span
                                            ><span style="font-size: 14px"
                                                >￥</span
                                            >{{ goodsItem.specIntegerPrize
                                            }}<span style="font-size: 14px">{{
                                                goodsItem.specFloatPrize
                                            }}</span>
                                        </span>
                                    </p>
                                </template>
                                <template v-if="goodsItem.configStyle == 1">
                                    <img
                                        style="width: 150px; height: 224px"
                                        class="goodsImg"
                                        :data-src="$imgURL + goodsItem.imgUrl"
                                        alt=""
                                    />
                                </template>
                            </div>
                        </template>
                    </div>
                    <div class="enjoygoodthings-con-right-l">
                        <template
                            v-for="(goodsItem,
                            goodsIndex) in data.webFloorLinkConfigDTOList"
                        >
                            <div
                                class="artCon artCursorbor"
                                :key="goodsIndex"
                                @click="goAD(goodsItem)"
                                v-if="goodsIndex > 2"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <template v-if="goodsItem.configStyle == 2">
                                    <img
                                        class="goodsImg"
                                        :data-src="
                                            $imgURL +
                                            goodsItem.imgUrl +
                                            '_200x200.png'
                                        "
                                        alt=""
                                    />
                                    <p class="artitle arttextoverflow">
                                        {{ goodsItem.goodsName }}
                                    </p>
                                    <p class="artinfos arttextoverflow">
                                        {{ goodsItem.subTitle }}
                                    </p>
                                    <p class="artmoney">
                                        <span
                                            ><span style="font-size: 14px"
                                                >￥</span
                                            >{{ goodsItem.specIntegerPrize
                                            }}<span style="font-size: 14px">{{
                                                goodsItem.specFloatPrize
                                            }}</span>
                                        </span>
                                    </p>
                                </template>
                                <template v-if="goodsItem.configStyle == 1">
                                    <img
                                        style="width: 150px; height: 224px"
                                        class="goodsImg"
                                        :data-src="$imgURL + goodsItem.imgUrl"
                                        alt=""
                                    />
                                </template>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        name: "enjoygoodthings3",
        props: {
            data: {
                type: Object,
                required: true,
                default: {},
            },
            titleIsShow: {
                type: Boolean,
                required: false,
                default: true,
            },
        },
        data() {
            return {};
        },
        watch: {},
        computed: {},
        mounted() {
            console.log(this.data);
        },
        methods: {
            goGoodsDetails(goodsItem) {
                let newpage = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: goodsItem.id,
                        specId: goodsItem.specId,
                    },
                });
                window.open(newpage.href, "_blank");
            },
            actCarousel(index){
                    console.log(this.data.advDTOList[index])
                    if(this.data.advDTOList[index] && this.data.advDTOList[index].relationTarget && this.data.advDTOList[index].relationType){
                        if(this.data.advDTOList[index].relationType=='url'){
                                    window.open(this.data.advDTOList[index].relationTarget);
                            }else if(this.data.advDTOList[index].relationType=='searchGoodsByClass'){
                                var id = this.data.advDTOList[index].relationTarget
                                let newpage = this.$router.resolve({
                                        path: "/proClassification",
                                        query: {
                                                keyWord: "",
                                                classId: id,
                                },
                                });
                                window.open(newpage.href, "_blank");
                            }else if(this.data.advDTOList[index].relationType=='topic'){
                                var id = this.data.advDTOList[index].relationTarget
                                let newpage = this.$router.resolve({
                                        path: "/subject",
                                        query: {
                                                id: id,
                                        },
                                });
                                window.open(newpage.href, "_blank");
                            }
                    }
            },
            goADV(data) {
        //        if (data && data.relationTarget !==null && data.relationTarget !=='') {
        //                 if(data.relationType == 'url'){
        //                         window.open(data.relationTarget);
        //                 }else if(data.relationType == 'searchGoodsByClass'){
        //                         this.$router.push({
        //                                 path: '/proClassification',
        //                                 query:{
        //                                         classId:data.relationTarget
        //                                 }
        //                         })
        //                 }
        //         }
            },
            goAD(data) {
                    console.log('3',data)
                console.log(this.$route.name);
                if (data.linkType == "link") {
                    window.open(data.typeKeyWord);
                } else if (data.linkType == "searchGoodsByClass") {
                    var goPath = "";
                    var ClassifiQuery = {};
                    if (this.$route.name == "shopIndex") {
                        goPath = "/shopProClassification";
                        ClassifiQuery = {
                            storeId: this.$route.query.storeId,
                            storeGcIds: data.typeKeyWord,
                        };
                    } else {
                        goPath = "/proClassification";
                        ClassifiQuery = {
                            keyWord: "",
                            classId: data.typeKeyWord,
                        };
                    }
                    let newpage = this.$router.resolve({
                        path: goPath,
                        query: ClassifiQuery,
                    });
                    window.open(newpage.href, "_blank");
                } else if (data.linkType == "searchByKeyWord") {
                    let newpage = this.$router.resolve({
                        path: "/searchGoodsResult",
                        query: {
                            keyWord: data.typeKeyWord,
                            classId: "",
                        },
                    });
                    window.open(newpage.href, "_blank");
                } else if (data.linkType == "goodsDetail") {
                    var specId = data.typeKeyWord.split(",")[0];
                    var goodsId = data.typeKeyWord.split(",")[1];
                    let newpage = this.$router.resolve({
                        path: "/goodsDetails",
                        query: {
                            goodsId: goodsId,
                            specId: specId,
                        },
                    });
                    window.open(newpage.href, "_blank");
                } else if (data.linkType == "topic") {
                        var id = data.typeKeyWord;
                        let newpage = this.$router.resolve({
                                path: "/subject",
                                query: {
                                        id: id,
                                },
                        });
                        window.open(newpage.href, "_blank");
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    /deep/ .ivu-carousel {
        margin-top: 10px !important;
    }

    .arttextoverflow {
        white-space: nowrap;
        overflow: hidden;
        word-break: break-all;
        word-wrap: break-word;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
    }

    .arttextoverflow2 {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
    }

    img {
        display: inline-block;
    }

    .warp-con {
        width: 1200px;
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

    .artCursorbor {
        cursor: pointer;
        border: 1px solid #f5f5f5;

        .goodsImg {
            transition: all 0.6s;
            object-fit: cover;
        }

        &:hover {
            // border: 1px solid #ed4014;
            // box-shadow: 0 1px 4px rgba(54, 54, 54, 0.2);

            // border-color: #DD2619;
            //border:2px solid #efefef;
            .goodsImg {
                transform: scale(1.05);
                object-fit: cover;
            }

            .artitle {
                color: #dd2619;
            }
        }
    }

    .enjoygoodthings3 {
        padding: 30px;
        margin-top: 20px;

        .arttitle {
            margin-bottom: 20px;
            display: flex;
            align-items: center;

            i {
                width: 4px;
                height: 20px;
                background: #f3b73d;
                margin-right: 10px;
            }

            span {
                font-size: 24px;
                font-family: PingFang SC;
                font-weight: 600;
                color: #222222;
                line-height: 24px;
            }
        }
    }

    .enjoygoodthings3-con {
        display: flex;

        .enjoygoodthings-con-left {
            position: relative;
            width: 204px;
            height: 600px;
            border: 1px solid #f5f5f5;
            overflow: hidden;

            .floor_imgStyle_1_bg {
                width: 204px;
                height: 600px;
                background: linear-gradient(
                    180deg,
                    rgba(0, 0, 0, 0.1) 0%,
                    rgba(0, 0, 0, 0.2) 0%,
                    rgba(0, 0, 0, 0.1) 100%
                );
                position: absolute;
                top: 0;
            }

            p {
                position: absolute;

                &:nth-of-type(1) {
                    top: 31px;
                    left: 30px;
                    font-size: 20px;
                    font-family: PingFang SC;
                    font-weight: 600;
                    color: rgba(255, 255, 255, 1);
                    line-height: 20px;
                }

                &:nth-of-type(2) {
                    top: 59px;
                    left: 30px;
                    font-size: 14px;
                    font-family: PingFang SC;
                    font-weight: 600;
                    color: rgba(255, 255, 255, 1);
                    line-height: 20px;
                }
            }

            img {
                width: 204px;
                height: 600px;
                object-fit: cover;
            }
        }

        .enjoygoodthings-con-right {
            .artCursorbor {
                border-left: 0px;
            }

            .enjoygoodthings-con-right-r {
                float: left;
                display: flex;
                flex-direction: column;

                .artCursorbor:nth-child(1) {
                    border-bottom: 0px;
                }
            }

            .enjoygoodthings-con-right-l {
                float: right;
                width: 700px;
                display: flex;
                flex-wrap: wrap;

                .artCursorbor:nth-child(1),
                .artCursorbor:nth-child(2),
                .artCursorbor:nth-child(3) {
                    border-bottom: 0px;
                }
            }

            .enjoygoodthings-con-right-r > div {
                width: 232px;
                height: 300px;

                img {
                    width: 160px;
                    height: 160px;
                }

                p:nth-of-type(1) {
                    margin: 18px 0 0 0;
                    font-size: 14px;
                    font-family: PingFang SC;
                    font-weight: 600;
                    color: rgba(34, 34, 34, 1);
                    line-height: 14px;
                }

                p:nth-of-type(2) {
                    margin: 8px 0 10px 0;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    line-height: 12px;
                }

                &:hover {
                    p:nth-of-type(1) {
                        color: #dd2619;
                    }
                }
            }
        }
    }

    //  中间广告
    .advertisement-part {
        height: 120px !important;
        overflow: hidden;

        img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

    .artCon {
        display: inline-block;
        height: 300px;
        width: 232px;
        padding: 30px 37px 0px 37px;

        img {
            display: block;
            width: 152px;
            height: 152px;
            margin: 0 auto;
        }

        .artitle {
            margin-top: 18px;
            font-size: 14px;
            font-family: PingFang SC;
            font-weight: 600;
            color: rgba(34, 34, 34, 1);
            line-height: 14px;
        }

        .artitle:hover {
            color: #dd2619;
        }

        .artinfos {
            margin: 8px 0 10px 0;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(153, 153, 153, 1);
            line-height: 12px;
        }

        .artmoney {
            span {
                color: #dd2619;
                height: 20px;
                font-size: 20px;
                font-weight: 600;
                line-height: 20px;
            }
        }
    }
</style>
