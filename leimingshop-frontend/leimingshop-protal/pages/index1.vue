<template>
    <div class="container">
        <!-- 顶部搜索部分 -->
        <div class="search-part">
            <search-first-level></search-first-level>
            <div class="Secondarynav-part warp-con backgroundcolorFFF">
                <div class="Secondarynav-left">
                    所有商品分类<Icon type="ios-arrow-forward"></Icon>
                </div>
                <Row type="flex">
                    <i-col class="artCurrent">
                        <p @click="$router.push('/')">首页</p>
                    </i-col>
                    <i-col v-for="(item, index) of IndexNavList" :key="index">
                        <p @click="goNavPage(item)">{{ item.title }}</p>
                    </i-col>

                    <!-- <i-col>
                        <p @click="$router.push('/couponCenter')">领优惠券</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/seckillZone')">秒杀专区</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">拼团专区</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">热卖商品</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">大牌汇聚</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">好物优享</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">便捷服务</p>
                    </i-col>
                    <i-col>
                        <p @click="$router.push('/')">生活服务</p>
                    </i-col> -->
                </Row>
            </div>
        </div>

        <!-- 中间轮播图部分 -->
        <div class="Threelevelnav-part warp-con">
            <ul class="Threelevelnav-left con-left">
                <li
                    v-for="(level1, indexlevel1) in IndexCustomClassData"
                    :key="indexlevel1"
                >
                    <span
                        class="goods-type"
                        @click="goClassSearch(level1.classId)"
                        >{{ level1.gcName }}</span
                    >
                    <div
                        class="goods-type-level2"
                        v-show="
                            judgeDisplayLevel2(
                                level1.gcName,
                                level1.children,
                                indexlevel2
                            )
                        "
                        v-for="(level2, indexlevel2) in level1.children"
                        :key="indexlevel2"
                    >
                        <a
                            v-show="
                                judgeDisplayLevel2Slash(
                                    level1.gcName,
                                    level1.children,
                                    indexlevel2
                                )
                            "
                            >/</a
                        >
                        <a
                            class="goods-type-level2-a"
                            @click="goClassSearch(level2.classId)"
                            >{{ level2.gcName }}</a
                        >
                    </div>
                    <div class="artp">
                        <!-- <span class="goods-type-level2-span"></span> -->
                        <!-- 右侧显现 -->
                        <div class="Threelevelnav-left-con">
                            <div class="Threelevelnav-left-con-l">
                                <div
                                    class="leve3-box"
                                    v-for="(level2,
                                    indexlevel2) in level1.children"
                                    :key="indexlevel2"
                                >
                                    <span
                                        @click="goClassSearch(level2.classId)"
                                        >{{ level2.gcName }}</span
                                    >
                                    <Icon type="ios-arrow-forward"></Icon>
                                    <p>
                                        <span
                                            v-for="(level3,
                                            indexlevel3) in level2.children"
                                            :key="indexlevel3"
                                            class="goods-type-level3-a"
                                            @click="
                                                goClassSearch(level3.classId)
                                            "
                                            >{{ level3.gcName }}</span
                                        >
                                    </p>
                                </div>
                            </div>
                            <div class="Threelevelnav-left-con-r">
                                <img
                                    v-for="(itemAdv, indexAdv) in level1 &&
                                    level1.advLinkConfigDTOList"
                                    :key="indexAdv"
                                    v-lazy="$imgURL + itemAdv.imgUrl"
                                    @click="goAD(itemAdv)"
                                />
                            </div>
                        </div>
                    </div>
                    <Icon type="ios-arrow-forward"></Icon>
                </li>
            </ul>

            <div class="rotationchart-part">
                <Carousel
                    :autoplay-speed="3000"
                    easing="none"
                    :autoplay="IndexAdvInPage.length > 1"
                    @on-click="actCarousel"
                    v-model="carouselVal"
                    loop
                    :radius-dot="true"
                >
                    <CarouselItem
                        v-for="(item, index) in IndexAdvInPage"
                        :key="index"
                    >
                        <div class="demo-carousel">
                            <img
                                style="height: 470px; width: auto"
                                :src="$imgURL + item.imageUrl"
                            />
                        </div>
                    </CarouselItem>
                </Carousel>
                <!-- <div class="location swiper-rotationchart" >
                    <div class="swiper-wrapper">
                        <div v-for='(item,index) in IndexAdvData' class="swiper-slide" :key="index">
                            <img :src="$imgURL + item.imageUrl">
                        </div>
                    </div>
                    <div class="swiper-button-next" v-if="IndexAdvData.length > 1"></div>
                    <div class="swiper-button-prev" v-if="IndexAdvData.length > 1"></div>
                    <div class="swiper-pagination"></div>
                </div> -->
            </div>
        </div>

        <!-- 秒杀专区 -->
        <div
            class="seckill-part warp-con backgroundcolorFFF"
            v-if="
                ActivitySeckillHomeData.seckillGoodsList &&
                ActivitySeckillHomeData.seckillGoodsList.length > 0
            "
        >
            <div class="seckill-left con-left">
                <img v-lazy="'/img/01.index/seckillZone.png'" alt="" />
                <p>{{ ActivitySeckillHomeData.currentDateStr }}点场 倒计时</p>
                <p>
                    <span>{{ getTimeCount.hours }}</span
                    >:<span>{{ getTimeCount.minutes }}</span
                    >:<span>{{ getTimeCount.seconds }}</span>
                </p>
                <p>下一场{{ ActivitySeckillHomeData.nextDateStr }}开始</p>
                <p @click="$router.push('/seckillZone')">查看全部</p>
            </div>
            <div class="seckill-right con-right">
                <seckill-goods
                    v-for="(item,
                    index) in ActivitySeckillHomeData.seckillGoodsList"
                    :key="index"
                    :data="item"
                >
                </seckill-goods>
            </div>
        </div>
        <component
            :is="actIndexFloorItem.floorStyleName"
            v-for="(actIndexFloorItem, actIndexFloorIndex) of actIndexFloorData"
            :key="actIndexFloorIndex"
            :data="actIndexFloorItem"
        ></component>

        <!-- 为你推荐 -->
        <div v-if="IndexRecommendPageData.length > 0">
            <div class="recommended-title">为你推荐</div>
            <load-more :completed="iscompleted" @changeData="changPages">
                <!-- <Scroll :on-reach-bottom="handleReachBottom"> -->
                <div class="recommended-part warp-con">
                    <recommended-item
                        v-for="(item, index) in IndexRecommendPageData"
                        :key="index"
                        :data="item"
                    >
                    </recommended-item>
                </div>
                <!-- </Scroll> -->
            </load-more>
        </div>
        <div style="height: 30px"></div>
    </div>
</template>

<script>
    import Swiper from "swiper";
    import axios from "axios";
    import {
        IndexCustomClass, //首页商品分类
        IndexAdv,
    } from "@/api/api_01main.js";
    // import "swiper/css/swiper.css";
    import searchFirstLevel from "@/components/common/searchFirstLevel.vue";
    import seckillGoods from "@/components/01.index/seckillGoods.vue"; //秒杀专区
    import enjoyGoodThings1 from "@/components/01.index/enjoyGoodThingsStyle1.vue"; //优享好物1
    import enjoyGoodThings2 from "@/components/01.index/enjoyGoodThingsStyle2.vue"; //优享好物2
    import enjoyGoodThings3 from "@/components/01.index/enjoyGoodThingsStyle3.vue"; //优享好物3
    import recommendedItem from "@/components/01.index/recommendedItem.vue"; //为你推荐
    import { IndexNav, IndexFloor } from "@/api/api_01main.js";
    import { mapState, mapActions, mapMutations, mapGetters } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        name: "Index",
        data() {
            return {
                carouselVal: 0,
                cityList: [
                    {
                        value: "0",
                        label: "店铺",
                    },
                    {
                        value: "1",
                        label: "商品",
                    },
                ],
                value: "",
                advKey: "pcIndex", //pcIndex pc首页 storeIndex 店铺首页
                storeId: "", //店铺id
                seckillGoodsData: {
                    img: "../../../img/01.index/3.jpg",
                    title: "Apple iPhone SE(A2298)",
                    info: "匠心裁剪 垂感质地",
                    money: "579.00",
                    originalPrice: "759.00",
                    num: "100",
                },
                // IndexAdvInPage: [],
                // IndexNavList: [
                //   {
                //     classIds: "",
                //     id: "1270992485745635330",
                //     isOpen: 1,
                //     relationParams: "http://b2b2c.leimingtech.com/seckillZone",
                //     relationType: 1,
                //     sort: 1,
                //     storeId: null,
                //     title: "",
                //   },
                //],
                // actIndexFloorData: [], //楼层数据
                // IndexRecommendPageData:[],
            };
        },
        async fetch({ store, params }) {
            // store.commit("article/SET_CURRENT_PAGE", 1);
            // await store.dispatch("main/actIndexFloor");
            // store.dispatch("main/actIndexCustomClass");
            // store.dispatch("main/actIndexRecommendPage", 1);
            const arr = [
                store.dispatch("main/actIndexCustomClass"), // 商品分类
                store.dispatch("main/actIndexRecommendPage", 1), // 商品推荐
                store.dispatch("main/actIndexFloor"), //楼层信息
            ];
            return Promise.all(arr);
        },
        async asyncData({ params, store }) {
            return Promise.allSettled([
                // store.dispatch("main/actIndexCustomClass"),
                IndexAdv({ advKey: "pcIndex" }), // 首页轮播图
                IndexNav(), // 首页导航栏
            ])
                .then((data) => {
                    console.log(data);
                    return {
                        // IndexCustomClassData: store.state.main.IndexCustomClassData,
                        IndexAdvInPage: data[0].value.data,
                        IndexNavList: data[1].value.data,
                    };
                })
                .catch((err) => console.log(err));
            // return {
            //   // IndexCustomClassData: store.state.main.IndexCustomClassData,
            //   IndexAdvInPage: [],
            //   IndexNavList: [],
            // };
        },
        components: {
            seckillGoods,
            enjoyGoodThings1,
            enjoyGoodThings2,
            enjoyGoodThings3,
            recommendedItem,
            searchFirstLevel,
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "iscompleted",
                "artIndexData",
                "curFloor",
                "IndexFloorData",
                "IndexRecommendPageData",
                "page",
                "limit",
                "total",
                "actIndexFloorData",
                // "IndexAdvInPage",
                "IndexCustomClassData",
                "IndexSiteData",
                "ActivitySeckillHomeData",
                "timeCount",
            ]),
            ...mapGetters("main", ["getTimeCount"]),
        },
        watch: {
            IndexAdvData: {
                handler(newVal, oldVal) {
                    this.$forceUpdate();
                },
                immediate: true,
            },
        },
        mounted() {
            this.$forceUpdate();
            //banner轮播图
            new Swiper(".swiper-rotationchart", {
                autoplay: {
                    //可选选项，自动滑动
                    delay: 3000,
                    disableOnInteraction: false,
                },
                loop: true, // 循环模式选项
                // 如果需要分页器
                pagination: {
                    el: ".swiper-pagination",
                },
                //左右切换按钮
                navigation: {
                    nextEl: ".swiper-button-next",
                    prevEl: ".swiper-button-prev",
                },
            });
            let obj = {
                advKey: this.advKey, //pcIndex pc首页 storeIndex 店铺首页
            };
            if (this.storeId) {
                obj.storeId = this.storeId; //店铺Id
            }

            // this.actIndexAdv(obj);
            // IndexAdv(obj).then((res) => {
            //   this.IndexAdvInPage = res.data;
            // });
            // this.actIndexFloor();
            // this.actIndexRecommendPage(this.page);

            //  this.actIndexCustomClass();
            this.actActivitySeckillHome();
            // this.getIndexNav();

            // this.$store.dispatch('main/actIndexAdv')//3.使用dispatch调用引进的方法
        },
        methods: {
            ...mapActions("main", [
                "actIndexAdv",
                // 'actIndexFloor',
                "actIndexRecommendPage",
                "actIndexCustomClass",
                "actActivitySeckillHome",
            ]),
            actCarousel(index) {
                console.log(index);
            },
            goAD(data) {
                if (data.linkType == "link") {
                    window.open(data.typeKeyWord);
                } else if (data.linkType == "searchGoodsByClass") {
                    let newpage = this.$router.resolve({
                        path: "/proClassification",
                        query: {
                            keyWord: "",
                            classId: data.typeKeyWord,
                        },
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
                }
            },
            //获取首页导航栏
            // getIndexNav() {
            //   IndexNav("")
            //     .then((res) => {
            //       // console.log(res.data)
            //       if (res.code == 200) {
            //         this.IndexNavList = res.data;
            //       }
            //     })
            //     .catch((error) => {
            //       console.log(error);
            //     });
            // },
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
            //下滑加载更多
            changPages(page) {
                if (!this.iscompleted) {
                    this.actIndexRecommendPage(this.page + 1);
                }
            },
            actIndexFloor() {
                IndexFloor({})
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
                                        this.actIndexFloorData[i]
                                            .webFloorLinkConfigDTOList[j]
                                            .typeKeyWord !== null
                                    ) {
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
                        }
                    })
                    .catch((error) => console.log(error));
            },
            goClassSearch(classId) {
                let routeUrl = this.$router.resolve({
                    path: "/proClassification",
                    query: {
                        classId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },
            judgeDisplayLevel2(level1gcName, level1children, indexlevel2) {
                if (indexlevel2 == 0) {
                    return true;
                } else if (indexlevel2 == 1) {
                    if (
                        level1children[indexlevel2].gcName.length +
                            level1children[indexlevel2 - 1].gcName.length <
                        7
                    ) {
                        return true;
                    }
                }
            },
            judgeDisplayLevel2Slash(level1gcName, level1children, indexlevel2) {
                if (indexlevel2 == 0) {
                    return false;
                } else if (indexlevel2 == 1) {
                    if (
                        level1children[indexlevel2].gcName.length +
                            level1children[indexlevel2 - 1].gcName.length <
                        7
                    ) {
                        return true;
                    }
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    .container {
        background-color: #f6f6f6;
        font-size: 0px;
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

    //顶部搜索部分
    .search-part {
        width: 100%;
        background-color: #fff;

        .Secondarynav-part {
            display: flex;

            .Secondarynav-left {
                width: 232px;
                height: 46px;
                line-height: 46px;
                color: #fff;
                padding: 0 20px 0 30px;
                background-color: #dd2619;
                font-size: 15px;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .ivu-icon {
                    line-height: 30px;
                    color: #ffffff;
                    transform: rotate(90deg);
                    -ms-transform: rotate(90deg);
                    /* IE 9 */
                    -moz-transform: rotate(90deg);
                    /* Firefox */
                    -webkit-transform: rotate(90deg);
                    /* Safari 和 Chrome */
                    -o-transform: rotate(90deg);
                    /* Opera */
                }
            }

            .ivu-row-flex {
                padding-left: 22px;
                flex-grow: 1;
                flex-wrap: nowrap;
                text-align: center;
                line-height: 46px;
                font-size: 15px;

                .ivu-col {
                    cursor: pointer;
                    max-width: 100px;
                    padding: 0 20px 0 20px;
                    color: #333333;

                    p {
                        font-size: 15px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(34, 34, 34, 1);
                        overflow: hidden;
                        white-space: nowrap;
                        word-break: break-all;
                        text-overflow: ellipsis;
                    }

                    &.artCurrent p {
                        color: #e2270b;
                        font-weight: 500;
                    }

                    &:hover {
                        color: #dd2619;
                    }
                }
            }
        }
    }

    //中间轮播图部分
    .Threelevelnav-part {
        // display: flex;
        position: relative;

        .Threelevelnav-left {
            left: 0;
            height: 470px;
            position: absolute;
            padding: 10px 0 10px 0;
            z-index: 98;
            background: #ffffff;

            li {
                width: 232px;
                height: 30px;
                padding: 0 10px 0 20px;
                line-height: 30px;
                font-size: 15px;
                cursor: pointer;
                display: flex;

                .goods-type {
                    margin-right: 11px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #222222;
                    line-height: 30px;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    word-break: break-all;
                }

                .goods-type-level2 {
                    margin-left: 4px;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    word-break: break-all;
                    display: flex;
                    align-items: center;

                    a:first-of-type {
                        color: #666;
                        margin-right: 4px;
                    }

                    .goods-type-level2-a {
                        color: #666;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #666666;
                        line-height: 14px;
                    }
                    .goods-type-level2-a:hover {
                        color: #dd2619;
                    }
                }

                & > .artp {
                    line-height: 31px;
                    font-size: 13px;
                    flex-grow: 1;
                    color: #717171;
                }

                .ivu-icon {
                    line-height: 30px;
                    color: #666666;
                }

                .Threelevelnav-left-con {
                    display: none;
                    width: 968px;
                    min-height: 470px;
                    background: rgba(255, 255, 255, 1);
                    position: absolute;
                    top: -16px;
                    left: 100%;
                    z-index: 999;
                    color: #222;
                    box-sizing: border-box;
                    margin-top: 16px;
                    padding: 24px 20px 32px 20px;
                    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.12);
                }

                .Threelevelnav-left-con-l {
                    flex-grow: 1;

                    .leve3-box {
                        margin-bottom: 10px;
                        display: flex;
                    }

                    div {
                        display: flex;
                        flex-wrap: wrap;

                        & > span {
                            min-width: 48px;
                            margin-right: 3px;
                            margin-left: 0px;
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 600;
                            color: rgba(34, 34, 34, 1);
                            line-height: 12px;
                            text-align: right;
                        }

                        .ivu-icon {
                            color: #222222;
                            line-height: 12px;
                        }

                        p {
                            max-width: 550px;
                            flex-grow: 1;
                            display: flex;
                            flex-wrap: wrap;
                        }

                        p span {
                            display: inline-block;
                            margin: 0px 6px 10px 6px;
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #666666;
                            line-height: 12px;
                        }
                    }
                }

                .Threelevelnav-left-con-r {
                    width: 248px;
                    height: 400px;
                    overflow: hidden;
                    // display: flex;
                    // flex-wrap: wrap;

                    img {
                        // display: block;
                        width: 238px;
                        height: 146px;
                        margin-bottom: 10px;

                        &:nth-of-type(n + 2) {
                            width: 114px;
                            height: 114px;
                            margin-right: 10px;
                        }
                    }
                }

                &:hover .Threelevelnav-left-con {
                    display: flex;
                }

                &:hover {
                    background: #eeeeee;

                    .goods-type,
                    p {
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        overflow: hidden;
                        word-break: break-all;
                    }

                    .goods-type-level3-a:hover {
                        color: #dd2619;
                    }

                    .Threelevelnav-left-con-l div {
                        color: #303030;

                        p span {
                            color: #939393;
                        }

                        span {
                            color: #303030;
                        }
                    }
                }
            }
        }

        .rotationchart-part {
            width: 100%;
            position: relative;
            height: 470px;
            flex-grow: 1;
            overflow: hidden;

            //iview
            /deep/ .ivu-carousel,
            /deep/ .ivu-carousel-list {
                height: 470px;
            }

            /deep/ .ivu-carousel-arrow.right {
                right: 0;
                height: 60px;
                width: 50px;
                background: rgba(0, 0, 0, 0.4);
                border-radius: 100px 0px 0px 100px;

                // border-radius: 0 50% 50% 0;
                /deep/ .ivu-icon-ios-arrow-forward:before {
                    color: #fff;
                    opacity: 0.7;
                    font-size: 33px;
                    padding-left: 10px;
                }
            }

            /deep/ .radius {
                width: 9px;
                height: 9px;
            }

            img {
                display: block;
                width: 100%;
            }

            /deep/ .swiper-pagination.swiper-pagination-bullets,
            /deep/ .ivu-carousel-dots-inside {
                padding: 50px;
                text-align: right;
            }

            .swiper-button-prev {
                display: none; //左边的不显示
                left: 0;
                height: 60px;
                width: 50px;
                background: rgba(0, 0, 0, 1);
                border-radius: 0px 100px 100px 0px;
                opacity: 0.3;

                // border-radius: 0 50% 50% 0;
                &:after {
                    color: #fff;
                    opacity: 0.7;
                    font-size: 33px;
                    padding-right: 10px;
                }
            }

            .swiper-button-next {
                right: 0;
                height: 60px;
                width: 50px;
                background: rgba(0, 0, 0, 1);
                border-radius: 100px 0px 0px 100px;
                opacity: 0.3;

                // border-radius: 0 50% 50% 0;
                &:after {
                    color: #fff;
                    opacity: 0.7;
                    font-size: 33px;
                    padding-left: 10px;
                }
            }

            /deep/ .swiper-pagination-bullet-active {
                background: rgba(41, 2, 5, 1);
                opacity: 0.76;
            }
        }
    }

    //秒杀专区
    .seckill-part {
        display: flex;
        margin-top: 20px;
        margin-bottom: 20px;
        height: 338px;

        .seckill-left {
            text-align: center;
            color: #fff;
            height: 100%;
            background: url("/img/01.index/index-seckillZone-bgimg.png") no-repeat
                center/cover;

            img {
                width: 165px;
                height: 42px;
                margin-top: 50px;
                margin-bottom: 20px;
            }

            p:nth-of-type(1) {
                font-size: 18px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #ffffff;
                line-height: 18px;
                margin-bottom: 31px;
            }

            p:nth-of-type(2) {
                line-height: 44px;
                font-size: 28px;
                margin-bottom: 30px;

                span {
                    display: inline-block;
                    margin: 0 1px;
                    width: 38px;
                    height: 38px;
                    background-color: #000;
                    border-radius: 8px;
                    font-size: 20px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    line-height: 38px;
                }
            }

            p:nth-of-type(3) {
                margin-bottom: 30px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(255, 255, 255, 1);
                line-height: 14px;
            }

            p:nth-of-type(4) {
                width: 90px;
                height: 34px;
                border-radius: 20px;
                border: 1px solid rgba(255, 255, 255, 0.62);
                margin: 0 auto;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(255, 255, 255, 1);
                line-height: 32px;
                cursor: pointer;
            }

            p:nth-of-type(4):hover {
                width: 90px;
                height: 34px;
                background-color: #000000;
                border: 1px solid rgba(0, 0, 0, 1);
                border-radius: 20px;
                margin: 0 auto;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                cursor: pointer;
            }
        }

        .seckill-right {
            width: 968px;
            padding-left: 8px;
            overflow: hidden;
            display: flex;
            background-color: #fff;
        }
    }

    //为你推荐
    .recommended-title {
        width: 250px;
        height: 19px;
        margin: 24px auto 18px auto;
        text-align: center;
        height: 40px;
        font-size: 28px;
        font-weight: 600;
        line-height: 40px;
        color: #333333;
        display: flex;
        justify-content: center;
        align-items: center;
        background: url("/img/01.index/index-title.png") no-repeat center center;
    }

    .recommended-part {
        display: flex;
        flex-wrap: wrap;
        min-height: 500px;
    }
    /deep/ .footer-text {
        color: #666666;
    }
    /deep/
        .search-con-search
        .search-con-search-top
        .ivu-select
        .ivu-select-dropdown {
        top: 44px !important;
    }
    /deep/ .ivu-select-dropdown {
        margin: 0px !important;
        padding: 0px !important;
    }
    // /deep/ .ivu-select-item-selected, .ivu-select-item-selected{
    //     :hover{
    //         color: #dd2619 !important;
    //         background: rgba(221, 38, 25, 0.03) !important;
    //     }
    // }
    // /deep/ .ivu-select-item{
    //     &:hover{
    //        background: rgba(221, 38, 25, 0.03) !important;
    //        color: #dd2619 !important;
    //     }
    // }
</style>
