<template>
    <div class="common-shop-head">
        <!-- 头部广告 -->
        <div class="advertisement-part backgroundcolorFFF">
            <div
                class="warp-con advertisement-warp"
                v-lazy-container="{
                    selector: 'img',
                    error: '/img/common/loading/200_200.png',
                    loading: '/img/common/loading/200_200.png',
                }"
            >
                <!-- <img
                    class="artCursor"
                    v-if="storeIndesLogo"
                    :data-src="$imgURL + storeIndesLogo.imageUrl"
                    @click="toLink(storeIndesLogo)"
                /> -->
                <div class="advertisement-right" v-if="false">
                    <div class="advertisement-right-con">
                        <img
                            @click="$router.push('/')"
                            class="artCursor"
                            v-lazy="'/img/01.index/logo.png'"
                        />
                        <div class>
                            <p>HUAWEI p40 系列 | 5G</p>
                            <p>超感知影响</p>
                        </div>
                    </div>
                    <div class="advertisement-right-con">
                        <img
                            @click="$router.push('/')"
                            class="artCursor"
                            v-lazy="'/img/01.index/logo.png'"
                        />
                        <div class>
                            <p>HUAWEI p40 系列 | 5G</p>
                            <p>超感知影响</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 头部nav   trigger="click" -->
        <div class="nav-part backgroundcolorFFF">
            <div class="nav-warp warp-con">
                <Menu mode="horizontal" theme="primary" active-name="2">
                    <!-- <MenuItem v-for="(item,index) in GoodsStoreClassAllData" :key="index" :name="index" class="artdropdown">
                        <Dropdown   @on-visible-change="artHandleOpen">
                            <div href="javascript:void(0)">
                                {{item.gcName}}
                                <Icon type="ios-arrow-down"></Icon>
                                <Icon v-show="currentShow" type="ios-arrow-up"></Icon>
                            </div>
                            <DropdownMenu slot="list">
                                <div v-for="(childrenItem,indexItem) in item.children">
                                    <p class="artdropdown-con"><span></span> {{childrenItem.gcName}}</p>
                                    <p class="artdropdown-con-text"><span>手机</span>   <span>游戏手机</span>   <span>5G手机</span>   <span>拍照手机</span>   <span>全面屏手机</span>   </p>
                                </div>
                            </DropdownMenu>
                        </Dropdown>
          </MenuItem>-->

                    <MenuItem name="1" class="artdropdown">
                        <Dropdown @on-visible-change="artHandleOpen">
                            <div @click="handleResetPage()">
                                查看全部商品
                                <Icon type="ios-arrow-down"></Icon>
                            </div>
                            <DropdownMenu slot="list">
                                <vue-scroll :ops="ops">
                                    <div
                                        v-for="(item,
                                        index) in GoodsStoreClassAllData"
                                        :key="index"
                                    >
                                        <p class="artdropdown-con" @click="go1ClassPage(item.id)">
                                            <span></span>
                                            {{ item.gcName }}
                                        </p>
                                        <p class="artdropdown-con-text">
                                            <span
                                                v-for="(childrenItem,
                                                indexItem) in item.children"
                                                :key="indexItem"
                                                :class="{
                                                    active:
                                                        childrenItem.id ==
                                                        $route.query.storeGcIds,
                                                }"
                                                @click="
                                                    go2ClassPage(
                                                        childrenItem.id
                                                    )
                                                "
                                                >{{ childrenItem.gcName }}</span
                                            >
                                        </p>
                                    </div>
                                </vue-scroll>
                            </DropdownMenu>
                        </Dropdown>
                    </MenuItem>
                </Menu>
                <Row type="flex">
                    <i-col>
                        <a @click="goShopIndex()">首页</a>
                    </i-col>
                    <i-col v-for="(item, index) of IndexNavList" :key="index">
                        <a @click="goNavPage(item)">{{ item.title }}</a>
                    </i-col>
                    <!-- <i-col><a @click="handleRouterGo('/')">首页</a></i-col>
                <i-col><a @click="handleRouterGo('/couponCenter')">领优惠券</a></i-col>
                <i-col><a @click="handleRouterGo('/seckillZone')">秒杀专区</a></i-col>
                <i-col><a @click="handleRouterGo('/')">拼团专区</a></i-col>
                <i-col><a @click="handleRouterGo('/')">热卖商品</a></i-col>
                <i-col><a @click="handleRouterGo('/')">大牌汇聚</a></i-col>
                <i-col><a @click="handleRouterGo('/')">好物优享</a></i-col>
                <i-col><a @click="handleRouterGo('/')">便捷服务</a></i-col>
          <i-col><a @click="handleRouterGo('/')">生活服务</a></i-col>-->
                </Row>
            </div>
        </div>

        <!-- 中间的轮播图 -->
        <Carousel
            :autoplay="IndexAdvInPage.length > 1"
            loop
            @on-click="actCarousel"
            v-if="IndexAdvInPage && IndexAdvInPage.length > 0"
            :autoplay-speed="3000"
        >
            <CarouselItem
                v-if="IndexAdvInPage.length > 0"
                v-model="carouselVal"
                v-for="(item, index) in IndexAdvInPage"
                :key="index"
            >
                <div class="demo-carousel" @click="goADV(item.relationTarget)">
                    <img :src="$imgURL + item.imageUrl" />
                </div>
            </CarouselItem>
        </Carousel>
    </div>
</template>

<script>
    import { IndexAdv, IndexNav } from "@/api/api_01main.js";

    import { mapState, mapActions, mapMutations } from "vuex";

    export default {
        name: "commonShopHead",

        props: {
            StoreInfoStoreIdData: {
                type: Object,
                required: false,
                default: {},
            },
            // advKey:{
            //     type: Object,
            //     required: false,
            //     default: {},
            // }
        },

        data() {
            return {
                carouselVal: 0,
                currentShow: false,
                advKey: "pcStoreIndex", //pcIndex pc首页 storeIndex 店铺首页
                storeId: this.$route.query.storeId, //店铺id
                IndexAdvInPage: [],
                storeIndesLogo: [],
                IndexNavList: null,
                imageUrl: "",
                relationTarget: "",
                ops: {
                    vuescroll: {},
                    scrollPanel: {},
                    rail: {
                        background: "#ccc",
                        opacity: 0,
                        size: "5px",
                        specifyBorderRadius: false,
                        gutterOfEnds: null, //轨道距 x 和 y 轴两端的距离
                        gutterOfSide: "0", //距离容器的距离
                        keepShow: false, //是否即使 bar 不存在的情况下也保持显示
                        border: "none", //边框
                    },
                    bar: {
                        hoverStyle: true,
                        onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
                        background: "red", //颜色
                    },
                },
            };
        },
        components: {},
        created() {},
        computed: {
            ...mapState("main", ["GoodsStoreClassAllData"]),
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
                .catch((err) => {
                    console.log(err);
                });
            let params = {
                advKey: "pcStoreLogo", //pcIndex pc首页 storeIndex 店铺首页
                storeId: this.storeId,
            };
            IndexAdv(params)
                .then((res) => {
                    console.log("我请求IndexAdv数据了");
                    this.storeIndesLogo = res.data[0];
                })
                .catch((err) => {
                    console.log(err);
                });

            this.actGoodsStoreClassAll({
                storeId: this.storeId,
            });
            this.getIndexNav();
        },
        methods: {
            ...mapActions("main", ["actIndexAdv", "actGoodsStoreClassAll"]),

            artHandleOpen(e) {
                this.currentShow = e;
            },
            //店铺的轮播图
            actCarousel(index){
                    console.log(this.IndexAdvInPage[index])
                     if(this.IndexAdvInPage[index].relationTarget &&this.IndexAdvInPage[index].relationType){
                            if(this.IndexAdvInPage[index].relationType =='url'){
                                    window.open(this.IndexAdvInPage[index].relationTarget);
                            }
                    }
            },
            goADV(url) {
                //     if(url!=null && url!=''){
                //        window.open(url, "_blank");
                //     }
            },
            go1ClassPage(storeFirstGcIds) {
                this.$router.replace({
                    path: "/shopProClassification",
                    query: {
                        storeId: this.storeId,
                        storeFirstGcIds: storeFirstGcIds,
                    },
                });
            },
            go2ClassPage(storeGcIds) {
                this.$router.replace({
                    path: "/shopProClassification",
                    query: {
                        storeId: this.storeId,
                        storeGcIds: storeGcIds,
                    },
                });
            },

            // 点击查看全部商品，重置页面参数
            handleResetPage() {
                this.$router.replace({
                    path: "/shopProClassification",
                    query: {
                        storeId: this.storeId,
                        querySearchType: "resetStore",
                        t: new Date().getTime(),
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
                    .catch((err) => {
                        console.log(err);
                    });
            },
            toLink(storeIndesLogo) {
                if (
                    this.storeIndesLogo.relationTarget != "" &&
                    this.storeIndesLogo.relationTarget != null
                ) {
                    console.log(5534, storeIndesLogo.relationTarget);
                    window.open(storeIndesLogo.relationTarget);
                }
            },
            //首页导航栏跳转
            goNavPage(item) {
                if (item.isOpen == 1) {
                    if (item.relationType == 1) {
                        window.open(item.relationParams, "_blank");
                    } else if (item.relationType == 2) {
                        let routeUrl = this.$router.resolve({
                            path: "/shopProClassification",
                            query: {
                                storeId: item.storeId,
                                storeGcIds: item.relationParams,
                            },
                        });

                        window.open(routeUrl.href, "_blank");
                    }
                } else if (item.isOpen == 0) {
                    if (item.relationType == 1) {
                        window.location.href = item.relationParams;
                    } else if (item.relationType == 2) {
                        this.$router.push({
                            path: "/shopProClassification",
                            query: {
                                storeId: item.storeId,
                                storeGcIds: item.relationParams,
                            },
                        });
                    }
                }
            },
            //跳转店铺首页
            goShopIndex() {
                this.$router.push({
                    path: "/shopIndex",
                    query: {
                        storeId: this.storeId,
                    },
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .common-shop-head {
        .container {
            background-color: #eee;
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
            width: auto;
            height: 97px;
            cursor: pointer;
        }

        .advertisement-part {
            .advertisement-warp {
                height: 110px;
                display: flex;
                justify-content: space-between;
                align-items: center;

                img {
                    // width: 230px;
                    height: 97px;
                    width: 1200px;
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
                background-color: #dd2619;

                .ivu-icon {
                    font-size: 16px;
                    margin-left: 4px;
                }

                .ivu-menu-item {
                    padding: 0 25px;

                    .ivu-dropdown-menu > div {
                        margin-bottom: 20px;
                    }
                }

                /* 查看所有商品 */
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
                        color: #222222;
                        line-height: 15px;
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
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #666666;
                        line-height: 18px;
                        display: flex;
                        flex-wrap: wrap;

                        span {
                            margin-right: 10px;
                            &.active {
                                color: $primary-color;
                            }
                        }
                    }
                }
            }

            .ivu-menu-item-active,
            .ivn-menu-opened {
                background-color: #dd2619;
            }
        }

        /deep/ .__rail-is-vertical {
            background: #ccc !important;
            width: 3px !important;
        }

        /deep/ .ivu-dropdown-menu {
            height: 100%;
        }

        .artdropdown-con-text {
            margin-bottom: 20px !important;
        }

        /* 轮播图 */
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
    }
</style>
