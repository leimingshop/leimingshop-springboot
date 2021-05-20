<template>
    <Card
        :bordered="bordered"
        :class="['goods-comp', goodsStyle, thumbnailStyle]"
    >
        <Tag
            class="p-tag"
            size="medium"
            color="#DD2619"
            v-if="goodsLabels && goodsLabels.length != 0"
            >{{
                goodsLabels instanceof Array
                    ? goodsLabels[0].labelName
                    : goodsLabels
            }}</Tag
        >

        <div class="p-img" @click="handleGoodsDetail">
            <img v-lazy="goodsMainPic" :key="goodsMainPic" class="anim" alt v-if="picture==true"/>
            <img v-lazy="$imgURL+goodsMainPic" :key="goodsMainPic" class="anim" alt v-else/>
        </div>

        <div class="p-scroll" v-if="thumbnailVisible">
            <div class="container">
                <ul>
                    <li v-for="(item, index) in goodsPicList" :key="index">
                        <img
                            :class="[{ active: hoverIndex == index }]"
                            v-lazy="hanldeGoodsPic(item.pictureUrl)"
                            alt
                            @mouseover="handleMouseOver(index, item.pictureUrl)"
                        />
                    </li>
                </ul>
            </div>
            <div class="arrow">
                <div class="prev" @click="prev">
                    <img
                        src="/img/03.goodsClass/03-03.goodsDetail/arrow_left.png"
                        alt
                    />
                </div>
                <div class="next" @click="next">
                    <img
                        src="/img/03.goodsClass/03-03.goodsDetail/arrow_right.png"
                        alt
                    />
                </div>
            </div>
        </div>
        <!-- <div class="p-scroll" v-if="thumbnailVisible">
            <Carousel
                ref="thumbnail"
                :value="thumbnailVal"
                dots="none"
                arrow="always"
            >
                <template v-if="goodsPicList && goodsPicList.length">
                    <CarouselItem
                        v-for="(item, index) in goodsPicList"
                        :key="index"
                    >
                        <img
                            :class="[{ active: hoverIndex == index }]"
                            v-lazy="hanldeGoodsPic(item.pictureUrl)"
                            alt
                            @mouseover="handleMouseOver(index, item.pictureUrl)"
                        />-->
        <!-- 生产环境图片不能动态切换 要加个 key，遂加之则图片就可以动态切换了 -->
        <!-- </CarouselItem>
                </template>
            </Carousel>
        </div>  -->

        <div class="p-content" @click="handleGoodsDetail">
            <div
                class="p-price"
                v-html="$options.filters.filterPrice(specSellPrice)"
            ></div>

            <div class="p-text">
                <div
                    class="p-name"
                    v-html="goodsName"
                    :title="goodsName.replace(/<[^>]*>/g, '')"
                ></div>
                <div class="p-desc" v-text="goodsSubTitle"></div>
            </div>
        </div>

        <div class="p-other-about" v-if="otherInfoVisible">
            <Divider />

            <div class="shop-name" @click="handleShopDetail">
                <Tag class="storeType" v-if="storeType == 1">自营</Tag>
                <span v-text="storeName" :title="storeName"></span>
            </div>

            <div class="p-info">
                <p class="p-comment-num" @click="handleGoodsDetail">
                    <span v-text="evaluateCount"></span>条评价
                </p>
                <p class="p-sales-volume">
                    销量
                    <span v-text="goodsSaleNum"></span>件
                </p>
            </div>
        </div>
    </Card>
</template>

<script>
    export default {
        name: "goodsComp",

        data() {
            return {
                thumbnailVal: 0,
                hoverIndex: 0,
                goodsStyle: "column-style", // 商品列表样式
                thumbnailStyle: "thumbnail-show", // 缩略图显示时
                goodsMainPic: "",
                picture:true,
                pictureUrl: "",
            };
        },
        props: {
            // 商品排序方式
            sortMethod: {
                type: String,
                default: "column",
            },

            // 是否显示card边框
            bordered: {
                type: Boolean,
                default: false,
            },

            // 是否显示商品缩略图列表
            thumbnailVisible: {
                type: Boolean,
                default: true,
            },

            // 单个缩略图宽度
            thumbnailWidth: {
                type: Number,
                default: 34,
            },

            // 商品其它相关信息
            otherInfoVisible: {
                type: Boolean,
                default: false,
            },

            // 商品标签
            goodsLabels: {
                type: Array | String,
                default: () => [],
            },

            // 商品id
            goodsId: {
                type: String,
                default: "",
            },

            // 商品规格id, 用于获取商品详情
            specId: {
                type: String,
                default: "",
            },

            // 商品价格
            specSellPrice: {
                type: String,
                default: "",
            },

            // 商品图片
            goodsMainPicture: {
                type: String,
                default: "",
            },

            // 商品名称
            goodsName: {
                type: String,
                default: "",
            },

            // 商品副标题
            goodsSubTitle: {
                type: String,
                default: "",
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

            // 评价数量
            evaluateCount: {
                type: Number,
                default: 0,
            },

            // 销量
            goodsSaleNum: {
                type: Number,
                default: 0,
            },

            goodsPicList: {
                type: Array,
                required: false,
                default: () => [],
            },
        },

        computed: {},

        watch: {
            goodsMainPicture: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.goodsMainPic = `${this.$imgURL}${this.goodsMainPicture}`; //修改SITE_CONMFIG['imgURL']
                },
            },

            sortMethod: {
                immediate: true,
                async handler(newVal, oldVal) {
                    await this.$nextTick();

                    // 恢复默认
                    if (this.thumbnailVisible) {
                        this.$refs.thumbnail.listWidth = this.thumbnailWidth;
                        this.thumbnailVal = 0;
                        this.goodsMainPic = `${this.$imgURL}${this.goodsMainPicture}`; // 修改SITE_CONFIG['imgURL']
                    }

                    // 切换排版样式
                    this.goodsStyle = `${newVal}-style`;
                },
            },

            thumbnailVisible: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.thumbnailStyle = newVal
                        ? "thumbnail-show"
                        : "thumbnail-hide";
                },
            },
        },

        methods: {
            hanldeGoodsPic(img) {
                return `${this.$imgURL}${img}`;
            },

            handleMouseOver(index, pic) {
                this.picture=true;
                this.hoverIndex = index;
                this.goodsMainPic = this.hanldeGoodsPic(pic);
                console.log(555, pic);
            },
            next() {
                this.picture=false;
                this.hoverIndex++;
                if (this.hoverIndex > this.goodsPicList.length - 1) {
                    this.hoverIndex = 0;
                    // this.goodsMainPic = this.goodsPicList[
                    //     this.hoverIndex
                    // ].pictureUrl;
                    console.log(444, this.goodsMainPic);
                }
                this.goodsMainPic=this.goodsPicList[this.hoverIndex].pictureUrl;
                console.log(7878,this.goodsMainPic)
            },
            prev() {
                this.picture=false;
                this.hoverIndex--;
                if (this.hoverIndex < 0) {
                    this.hoverIndex = this.goodsPicList.length - 1;
                    // this.goodsMainPic = this.goodsPicList[
                    //     this.hoverIndex
                    // ].pictureUrl;
                    console.log(6666, this.goodsMainPic);
                }
                 this.goodsMainPic=this.goodsPicList[this.hoverIndex].pictureUrl;
                 console.log(6868,this.goodsMainPic)
            },
            handleGoodsDetail() {
                let routeQuery = this.$route.query;

                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: { goodsId: this.goodsId, specId: this.specId },
                });

                window.open(routeUrl.href, "_blank");
            },

            // 评价跳转逻辑，暂无
            handlEvaluateDetail() {},

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
    // @keyframes mymove {
    //   from {
    //     transform: scale(0);
    //   }
    //   to {
    //     transform: scale(1);
    //   }
    // }
    .container {
        position: absolute;
        left: 27px;
        ul {
            display: flex;
            li {
                width: 34px;
                height: 34px;
                img {
                    width: 28px;
                    height: 28px;
                    border: 1px solid #e5e5e5;
                    cursor: pointer;
                    &:hover,
                    &.active {
                        border-color: #dd2619;
                    }
                }
            }
        }
    }
    .arrow {
        display: flex;
        justify-content: space-between;
    }
    .prev {
        width: 10px;
        line-height: 34px;
        img {
            width: 10px;
            height: 16px;
        }
    }
    .next {
        width: 10px;
        line-height: 34px;
        img {
            width: 10px;
            height: 16px;
        }
    }
    .goods-comp {
        width: 100%;
        position: relative;
        border: 2px solid #ffffff;
        transition: border 0.2s, box-shadow 0.2s;
        &:hover {
            border-color: #f84539;
        }
        &.ivu-card {
            border-radius: 0;
            &:hover {
                border-color: #ffffff;
                box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
                .p-name {
                    color: #dd2619;
                }
            }
        }
        &.column-style {
            margin-right: 10px;
            /deep/ .ivu-card-body {
                padding: 20px 12px !important;
            }
        }
        /deep/ .ivu-card-body {
            width: 100%;
            height: 100%;
        }
    }

    .p-tag {
        height: 20px;
        line-height: 20px;
        position: absolute;
        margin: 0;
        padding: 0 8px;
        top: 20px;
        left: 25px;
        background: linear-gradient(
            90deg,
            rgba(221, 41, 28, 1) 0%,
            rgba(255, 78, 2, 1) 100%
        ) !important;
        border-color: #ffffff !important;
        border-width: 0px !important;
    }

    .p-img {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 200px;
        height: 200px;
        margin: 0 auto;
        cursor: pointer;
        img {
            max-width: 100%;
            max-height: 100%;
            &.anim {
                animation: mymove 0.3s 1;
            }
        }
    }

    .p-scroll {
        position: relative;
        padding: 0 6px;
        margin-top: 15px;
        width: 100%;
        overflow: hidden;
    }

    .ivu-carousel /deep/ {
        width: 34px;
        height: 35px;
        box-sizing: border-box;
        position: static;

        .ivu-carousel-list {
            height: 100%;
            position: absolute;
            left: 28px;
            right: 33px;
        }

        .ivu-carousel-item {
            float: left;
            box-sizing: content-box;
            img {
                width: 28px;
                height: 28px;
                cursor: pointer;
                border: 1px solid #e5e5e5;
                box-sizing: border-box;
                transition: border 0.3s;
                &:hover,
                &.active {
                    border-color: #dd2619;
                }
            }
        }

        .ivu-carousel-arrow {
            width: 14px;
            height: 14px;
            background: transparent;
            transition: none;
            &.left {
                left: 3px;
                top: 13px;
                .ivu-icon-ios-arrow-back:before {
                    content: "";
                    width: 10px;
                    height: 16px;
                    display: block;
                    background: url("/img/02.shopIndex/prev.png") no-repeat;
                    background-size: 100% 100%;
                }
            }
            &.right {
                right: 5px;
                top: 13px;
                .ivu-icon-ios-arrow-forward:before {
                    content: "";
                    width: 10px;
                    display: block;
                    height: 16px;
                    background: url("/img/02.shopIndex/next.png") no-repeat;
                    background-size: 100% 100%;
                }
            }
            .ivu-icon {
                color: #666666;
                font-size: 30px;
                &:hover {
                    color: #dd2619;
                }
            }
        }
    }

    .p-content {
        // padding: 0 12px;
        cursor: pointer;
        display: flex;
        flex-direction: column;
    }

    .p-price {
        height: 20px;
        line-height: 20px;
        color: #dd2619;
        margin-top: 4px;
        font-family: PingFangSC-Semibold, PingFang SC;
        em {
            font-size: 14px;
            font-style: normal;
        }
        i {
            font-size: 20px;
            font-weight: bold;
            font-style: normal;
        }
    }

    .p-text {
        // margin-top: 6px;
        .p-name {
            height: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            color: #222222;
            font-weight: 600;
            font-size: 14px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-top: 10px;
            line-height: 14px;
            &:hover {
                color: #dd2619;
            }
        }

        .p-desc {
            height: 12px;
            font-size: 12px;
            color: #999999;
            margin-top: 7px;
            line-height: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
</style>

<style lang="scss" scoped>
    /*    商品列表行样式    */
    .row-style {
        /*        width: 100%;*/
        height: 142px;
        .p-img {
            max-width: 100px;
            max-height: 100px;
            float: left;
            margin-top: 5px;
            margin-right: 20px;
        }
        .p-scroll {
            height: 18px;
            width: 98px;
            transform: rotate(90deg);
            float: left;
            margin: 48px -25px 0 -35px;
        }
        .ivu-carousel /deep/ {
            width: 20px;
            height: 18px;
            .ivu-carousel-list {
                padding-left: 0;
                margin-left: -2px;
            }

            .ivu-carousel-item {
                width: 20px !important;
                img {
                    width: 18px;
                    height: 18px;
                    transform: rotate(-90deg);
                }
            }

            .ivu-carousel-arrow {
                transition: none;
                &.left {
                    left: -5px;
                    top: 8px;
                }
                &.right {
                    right: 1px;
                    top: 8px;
                }
            }
        }

        .p-content {
            flex-direction: column-reverse;
        }

        .p-text {
            width: 825px;
            .p-name {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: #222222;
                line-height: 14px;
            }
            .p-desc {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #a3a3a3;
                line-height: 12px;
            }
        }
    }
</style>

<style lang="scss" scoped>
    /* 搜索商品页 展示 商品附带信息 */
    .p-other-about {
        /deep/ .ivu-divider-horizontal {
            margin: 7px auto;
        }

        .shop-name {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 14px;
            cursor: pointer;
            overflow: hidden;
            white-space: nowrap;
            word-break: break-all;
            text-overflow: ellipsis;
            & > span {
                font-size: 13px;
                &:hover {
                    color: #dd2619;
                }
            }
            .storeType /deep/ {
                width: 30px;
                height: 16px;
                line-height: 16px;
                text-align: center;
                padding: 0;
                background: linear-gradient(
                    90deg,
                    rgba(221, 41, 28, 1) 0%,
                    rgba(255, 78, 2, 1) 100%
                );
                border: 0;
                border-radius: 4px;
                vertical-align: -5px;
                .ivu-tag-text {
                    font-size: 12px;
                    color: rgba(255, 255, 255, 1);
                    text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.21);
                }
            }
        }

        .p-info {
            margin-top: 10px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222222;
            line-height: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .p-comment-num /deep/ {
            color: #999999;
            cursor: pointer;
            span {
                color: #666;
                padding: 0 2px 0 0;
            }
        }

        .p-sales-volume {
            color: #999999;
        }
    }

    /deep/ .ivu-tag {
        border-radius: 0px !important;
    }
    /deep/ .ivu-carousel-track {
    }
</style>
