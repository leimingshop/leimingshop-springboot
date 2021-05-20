<template>
    <!-- 商品详情页主要区域组件 -->
    <div class="container1">
        <!-- 面包屑 -->
        <!-- {{
            this.goodsDetails.frontSeckillActivityPageDTO.restrictionQuantity
        }}activityType{{ activityType }} -->
        <breadcrumb1 :list="breadcrumb.list" />
        <div class="prodcut-main-body">
            <div class="main-image">
                <!-- 商品图片 -->
                <goods-image
                    v-if="goodsDetails.specAttrValuePicList"
                    :goodsPicList="goodsDetails.goodsPicList"
                    :specAttrValuePicList="goodsDetails.specAttrValuePicList"
                ></goods-image>

                <!-- 商品详情操作相关 -->
                <div class="magnifier-bottom">
                    <div
                        :class="['collect', { collected: goodsIsCollect == 1 }]"
                    >
                        <img
                            src="/img/03.goodsClass/03-03.goodsDetail/collect.png"
                            alt
                            v-if="goodsIsCollect == 0"
                        />
                        <img
                            src="/img/03.goodsClass/03-03.goodsDetail/collected.png"
                            alt
                            v-else
                        />
                        <div class="collect-num" @click="handleCollectGoods">
                            收藏商品({{ goodsFavNum }})
                        </div>
                    </div>
                    <div class="sales-volume">
                        <img
                            src="/img/03.goodsClass/03-03.goodsDetail/sales_volume.png"
                            alt
                        />
                        <div class="sales-volume-num">
                            销量&nbsp;
                            <span v-text="goodsDetails.specSaleNum"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="main-area">
                <div class="goods-name">
                    <Tag
                        class="goods-label"
                        v-if="
                            goodsDetails.goodsLabels &&
                            goodsDetails.goodsLabels.length != 0
                        "
                    >
                        {{ goodsDetails.goodsLabels[0].labelName }}</Tag
                    >
                    <span v-text="goodsDetails.specName"></span>
                </div>

                <!-- 商品上架状态 -->
                <div v-if="goodsDetails.goodsShow == 1">
                    <!--秒杀-->
                    <div
                        class="goods-sec-name"
                        v-text="goodsDetails.goodsSubTitle"
                    ></div>

                    <!-- 活动类型 - 秒杀  -->
                    <div v-if="goodsDetails.seckillFlag == 1">
                        <!-- 秒杀预告 -->
                        <div
                            class="seckill-notice"
                            v-if="
                                goodsDetails.frontSeckillActivityPageDTO
                                    .activityState == 0
                            "
                        >
                            <p>「秒杀预告」</p>
                            <p
                                v-text="
                                    goodsDetails.frontSeckillActivityPageDTO
                                        .activityStartDate
                                "
                            ></p>
                            <p>秒杀价</p>
                            <p class="prize-num">
                                ￥
                                <span
                                    v-text="
                                        goodsDetails.frontSeckillActivityPageDTO
                                            .activityPrice
                                    "
                                ></span>
                            </p>
                        </div>

                        <!--秒杀已开始 -->
                        <div
                            class="flash-sale"
                            v-if="
                                goodsDetails.frontSeckillActivityPageDTO
                                    .activityState == 1
                            "
                        >
                            <span>
                                <span style="color: #dd2619">「秒杀商品」</span>
                                <span>距离结束剩余：</span>
                                <span
                                    class="countDown"
                                    style="color: #dd2619"
                                    v-text="handleCountDown()"
                                ></span>
                            </span>
                            <a class="more" @click="handleMoreSeckill">
                                <img :src="collectIcon" alt />
                                更多秒杀
                                <img :src="moreIcon" alt />
                            </a>
                        </div>

                        <div class="prize">
                            <div class="main-area-title">秒杀价</div>
                            <div
                                class="prize-num"
                                v-html="
                                    $options.filters.filterPrice(
                                        goodsDetails.frontSeckillActivityPageDTO
                                            .activityPrice
                                    )
                                "
                            ></div>
                            <div
                                class="prize-change-num"
                                v-text="goodsDetails.specSellPrice"
                            ></div>
                        </div>
                    </div>
                    <!--限时抢购-->
                    <div
                        v-else-if="
                            goodsDetails.seckillFlag == 2 &&
                            goodsDetails.frontSeckillActivityPageDTO
                                .activityState == 1
                        "
                    >
                        <!--限时抢购已开始 -->
                        <div class="flash-sale">
                            <span>
                                <span style="color: #dd2619">「限时特惠」</span>
                                <span>距离结束剩余：</span>
                                <span
                                    class="countDown"
                                    style="color: #dd2619"
                                    v-text="handleCountDown()"
                                ></span>
                            </span>
                        </div>
                        <div class="prize">
                            <div class="main-area-title">限时特惠价</div>
                            <div
                                class="prize-num"
                                v-html="
                                    $options.filters.filterPrice(
                                        goodsDetails.frontSeckillActivityPageDTO
                                            .activityPrice
                                    )
                                "
                            ></div>
                            <div
                                class="prize-change-num"
                                v-text="goodsDetails.specSellPrice"
                            ></div>
                        </div>
                    </div>
                    <!-- 活动类型 - 无活动  -->
                    <div class="prize basic-style-30" v-else>
                        <div class="main-area-title">价格</div>
                        <div
                            class="prize-num"
                            v-html="
                                $options.filters.filterPrice(
                                    goodsDetails.specSellPrice
                                )
                            "
                        ></div>
                    </div>

                    <!-- 活动类型 - 优惠券 -->
                    <div
                        class="basic-style-20 couponList"
                        v-if="
                            goodsDetails.couponsFlag == 1 &&
                            couponList &&
                            couponList.length != 0
                        "
                    >
                        <div class="main-area-title" style="line-height: 22px">
                            领劵
                        </div>
                        <div
                            class="coupon-content"
                            v-for="item in couponList.slice(0, 3)"
                            :key="item.id"
                            v-show="item.couponsType == 0"
                        >
                            满{{ item.limitAmount }}减{{ item.faceValue }}
                        </div>

                        <div class="ml10 recive-btn" @click="handleReceive">
                            立即领取
                        </div>
                    </div>

                    <!-- 活动类型 - 满减 -->
                    <div
                        class="basic-style-20 goodsReduceList"
                        v-if="
                            goodsDetails.reduceFlag == 1 &&
                            reduceList &&
                            reduceList.length != 0
                        "
                    >
                        <div class="main-area-title">满减</div>
                        <div class="price-reduction-content">
                            <template v-for="item in reduceList.slice(0, 4)">
                                <p
                                    v-text="item.activityDescription"
                                    :class="{
                                        highlight:
                                            item.id ==
                                            goodsDetails
                                                .frontReduceActivityPageDTO.id,
                                    }"
                                ></p>
                            </template>
                            <div
                                :class="[
                                    'dropDown-list',
                                    {
                                        hoverActive: reduceActivityDropdownVisible,
                                    },
                                ]"
                            >
                                <Dropdown
                                    v-if="reduceList.length > 2"
                                    trigger="click"
                                    :transfer="true"
                                    transfer-class-name="reduce-activity-dropdown"
                                    placement="bottom"
                                    @on-visible-change="
                                        (visible) =>
                                            (reduceActivityDropdownVisible = visible)
                                    "
                                >
                                    <div
                                        style="
                                            width: 70px;
                                            height: 30px;
                                            padding-left: 10px;
                                            line-height: 32px;
                                        "
                                        class="reduce-activity-btn"
                                        type="text"
                                    >
                                        <span>全部</span>
                                        <Icon type="ios-arrow-down" size="16" />
                                    </div>

                                    <DropdownMenu slot="list">
                                        <li
                                            v-for="(item, index) in reduceList"
                                            :class="[
                                                {
                                                    active:
                                                        item.id ==
                                                        goodsDetails
                                                            .frontReduceActivityPageDTO
                                                            .id,
                                                },
                                            ]"
                                            v-text="item.activityDescription"
                                        ></li>
                                    </DropdownMenu>
                                </Dropdown>
                            </div>
                        </div>
                    </div>

                    <!-- <div class="basic-style">
                        <div class="main-area-title">运费</div>
                        <div class="coupon-word">免运费</div>
                        <div class="ml10">了解详情</div>
          </div>-->
                    <!-- 隐藏，先不做 -->
                    <!-- 有任何活动都显示一个线，没有活动用一个div进行占位 -->
                    <div
                        class="line"
                        v-if="
                            goodsDetails.seckillFlag ||
                            (goodsDetails.couponsFlag == 1 &&
                                couponList &&
                                couponList.length != 0) ||
                            (goodsDetails.reduceFlag == 1 &&
                                reduceList &&
                                reduceList.length != 0)
                        "
                    ></div>
                    <div style="width: 100%; height: 15px" v-else></div>
                    <!-- 属性 -->
                    <div
                        class="basic-style-5"
                        v-for="(ITEM, INDEX) in goodsSpecNameValue"
                        :key="ITEM.specAttrName"
                    >
                        <div
                            class="main-area-title"
                            v-text="ITEM.specAttrName"
                            style="margin-bottom: 10px"
                        ></div>
                        <RadioGroup
                            type="button"
                            @on-change="handleChangeAttr($event, INDEX)"
                            v-model="specAttrCheckList[INDEX]"
                        >
                            <Radio
                                v-for="item in ITEM.specAttrValueVOList"
                                :disabled="item.isDisabled"
                                :key="item.attrValueId"
                                :label="item.attrValueId"
                                >{{ item.attrValueName }}</Radio
                            >
                        </RadioGroup>
                    </div>

                    <!-- 数量 -->
                    <div class="basic-style-5">
                        <div
                            class="main-area-title"
                            style="height：30px;line-height:30px;"
                        >
                            数量
                        </div>
                        <!-- max:{{ max }}min:{{ min }}goodsNum{{ goodsNum }} -->
                        <counter
                            :min="min"
                            :max="max"
                            v-model="goodsNum"
                        ></counter>

                        <div class="ml10">
                            库存：
                            <span
                                style="line-height: 30px"
                                v-text="specStorage"
                            ></span>
                        </div>

                        <div
                            style="line-height: 30px"
                            class="ml10 hintBuyGoodsNum"
                            v-html="hintBuyGoodsNum"
                        ></div>
                    </div>

                    <!-- 活动类型 - 拼团  -->
                    <div class="group-buying-word" v-if="activityType == 4">
                        该商品为拼团商品，用手机端
                        <span>扫描二维码</span>即可专享团购价
                    </div>

                    <div class="operation-box">
                        <Button
                            :class="[
                                'join-cart',
                                {
                                    noStorage:
                                        specStorage == 0 || specShow != 1,
                                },
                            ]"
                            :loading="addCartLoading"
                            @click="handleAddToCart"
                            >加入购物车</Button
                        >
                        <Button
                            :class="[
                                'buy-it',
                                {
                                    noStorage:
                                        specStorage == 0 || specShow != 1,
                                },
                            ]"
                            :loading="cartBuyNowLoading"
                            @click="handleBuyItNow"
                            >立即购买</Button
                        >
                    </div>
                </div>

                <!-- 商品下架状态 -->
                <div v-else v-text="outOfStockContent" class="outOfStock"></div>
            </div>
        </div>

        <!-- 领取优惠券 -->
        <receive-coupon
            ref="receiveCoupon"
            class="receiveCoupon"
            :couponList="couponList"
        ></receive-coupon>
    </div>
</template>

<script>
    import { mapState } from "vuex";
    import { handleArrRemove } from "@/utils/array-handle";
    import goodsImage from "@/components/03.goodsClass/03-03.goodsDetails/goodsImage.vue";
    import receiveCoupon from "@/components/03.goodsClass/03-03.goodsDetails/receiveCoupon.vue";
    import counter from "@/components/common/counter";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    // import countdownVue from './countdown.vue'

    export default {
        components: {
            receiveCoupon,
            goodsImage,
            counter,
            breadcrumb1,
        },

        props: {
            goodsIsCollect: {
                type: Number,
                default: 0,
            },

            loginFlag: {
                type: Number,
                default: 0,
            },

            goodsDetails: {
                type: Object,
                default: null,
            },

            reduceList: {
                type: Array,
                default: null,
            },

            couponList: {
                type: Array,
                default: () => [],
            },

            goodsSpecNameValue: {
                type: Array,
                default: () => [],
            },

            goodsSpecList: {
                type: Array,
                default: () => [],
            },

            addCartLoading: {
                type: Boolean,
                default: false,
            },

            cartBuyNowLoading: {
                type: Boolean,
                default: false,
            },

            goodsFavNum: {
                type: Number,
                default: 0,
            },
        },
        data() {
            return {
                // 面包屑
                breadcrumb: {
                    separator: ">",
                    list: [],
                },
                reduceImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_reduce.png"), // '+'图片
                increaseImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_increase.png"), // '-'图片
                collectIcon: require("~/static/img/03.goodsClass/03-03.goodsDetail/seckill_list_icon.png"),
                moreIcon: require("~/static/img/03.goodsClass/03-03.goodsDetail/more.png"),
                specAttrCheckList: [], // 规格选中列表
                addCartSpecId: this.$route.query.specId, // 待加入购物车的商品规格id, 规格id会变动，所以路由参数只用一次
                specStorage: null, // 库存
                specShow: 1, // 商品规格是否上架，1上架，0下架
                goodsNum: 1, // 商品数量
                activityType: null, //当前规格商品活动类型 活动类型 0无 1优惠券 2满减 3秒杀 4拼团 ,
                attrDisabledArr: [], //禁用属性集合 未上架的商品 和库存为0的商品，属性禁用，切换属性触发
                outOfStockContent: "该商品已下柜，欢迎挑选其他商品！",
                reduceActivityDropdownVisible: false, // 满减活动列表
                activityStart: "",
                activityStatus: false, //活动状态
            };
        },

        computed: {
            ...mapState("goodsDetails", ["breadcrumbList"]),

            min() {
                console.log("触发了min");
                let number = this.specShow == 0 || this.specStorage == 0 ? 0 : 1;
                this.goodsNum = number;
                return number;
            },

            max() {
                // 秒杀活动限购数量
                console.log('this.goodsDetails',this.goodsDetails)
                if (this.goodsDetails.seckillFlag && this.goodsDetails
                        .frontSeckillActivityPageDTO.activityState == '1') {
                    let seckillLimitNum = this.goodsDetails
                        .frontSeckillActivityPageDTO.restrictionQuantity;
                    if (seckillLimitNum !== null && seckillLimitNum != 0)
                        return seckillLimitNum;
                }
                return this.specStorage > 200 ? 200 : this.specStorage;
            },

            hintBuyGoodsNum() {
                if (this.goodsDetails.seckillFlag) {
                    let seckillLimitNum = this.goodsDetails
                        .frontSeckillActivityPageDTO.restrictionQuantity;
                    if (seckillLimitNum !== null && seckillLimitNum != 0)
                        return `（&nbsp;活动限购数量&nbsp;${seckillLimitNum}&nbsp;件&nbsp;）`;
                }
                // return `（&nbsp;商品限购数量&nbsp;${this.max}&nbsp;件&nbsp;）`
            },
        },

        watch: {
            breadcrumbList: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.$set(this.breadcrumb, "list", newVal);
                },
            },
            activityStatus: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.handleDefaultGoodsParam();
                    this.handleDefaultAttrSelect();
                },
            },
        },

        created() {
            // 默认展示商品参数
            this.handleDefaultGoodsParam();
            // 默认选中规格属性列表
            this.handleDefaultAttrSelect();
            this.previewCountdown();
        },

        mounted() {},

        methods: {
            // 默认展示商品参数
            handleDefaultGoodsParam() {
                // 会有没有商品规格列表（goodsSpecList = []）的情况，所以库存从详情获取
                this.specStorage = this.goodsDetails.specStorage;
                console.log("默认展示商品参数");
                // 默认输入框商品数量
                this.goodsNum = this.specStorage == 0 ? 0 : 1;
            },

            // 默认选中规格属性列表
            handleDefaultAttrSelect() {
                // 没有规格的商品，直接返回
                console.log("默认选中规格属性列表");
                if (!this.goodsSpecList) return;
                console.log(
                    this.goodsSpecList,
                    this.addCartSpecId,
                    "addCartSpecId"
                );
                // 根据商品规格 默认选中的属性列表
                let result = this.goodsSpecList.filter((item, index) => {
                    return this.addCartSpecId == item.specId;
                })[0];

                if (!result) {
                    //     setTimeout(() => {
                    //         this.handleDefaultAttrSelect();
                    //     }, 200);
                    return;
                }

                this.activityType = result.activityType;

                // 默认选中规格属性集合
                this.specAttrCheckList = result.specAttrAndAttrValue.map(
                    (item, index) => {
                        return item.specAttrValueId;
                    }
                );

                // 商品规格是否上架
                this.specShow = result.specShow;

                // 禁用库存为0 或者 未上架的规格属性
                this.handleAttrDisabled();
            },

            // 加入购物车时修改商品属性
            async handleChangeAttr(attr, Index) {
                // 重置选中属性集合
                this.$set(this.specAttrCheckList, Index, attr);

                // 重置选中规格
                let result = this.goodsSpecList.filter((ITEM, INDEX) => {
                    let tempArr = ITEM.specAttrAndAttrValue.map((item, index) => {
                        return item.specAttrValueId;
                    });
                    return this.specAttrCheckList.toString() == tempArr.toString();
                })[0];

                // 重置商品详情页 参数
                this.addCartSpecId = result.specId;
                // 重置路由
                let routeQuery = this.$route.query;
                let query = {
                    goodsId: routeQuery.goodsId,
                    specId: this.addCartSpecId,
                };
                this.$router.replace({
                    path: "/goodsDetails",
                    query,
                });
                // 重置商品详情 (切换规格不传goodsId，否则参数刷新显示不正常)
                await this.$parent.handleGoodsDetails({
                    specId: this.addCartSpecId,
                });
                // 商品规格是否上架
                this.specShow = result.specShow;
                // 重置 默认展示商品参数
                this.handleDefaultGoodsParam();
                // 重置 禁用库存为0 或者 未上架的规格属性
                this.handleAttrDisabled();
            },

            // 商品下架或未上架，商品库存为0，禁用选项
            handleAttrDisabled(attr) {
                let goodsSpecList = this.goodsSpecList;

                // in array not in others
                const difference = (array, others) => {
                    return array.filter((item) => !others.includes(item));
                };

                // every in array in others
                const isSubset = (array, others) => {
                    return array.every((item) => others.includes(item));
                };

                // 遍历规格列表
                this.goodsSpecNameValue.forEach((ITEM, INDEX) => {
                    let tempAttrArr = ITEM.specAttrValueVOList.map(
                        (item, index) => {
                            return item.attrValueId;
                        }
                    );

                    // 移除当前遍历规格下的已选属性
                    const tempSelected = difference(
                        this.specAttrCheckList,
                        tempAttrArr
                    );

                    // 遍历规格属性
                    tempAttrArr.forEach((item, index) => {
                        const willSelected = [...tempSelected, item];

                        // 默认可选
                        let tempObj = ITEM.specAttrValueVOList[index];
                        tempObj["isDisabled"] = false;
                        this.$set(
                            this.goodsSpecNameValue[INDEX].specAttrValueVOList,
                            index,
                            tempObj
                        );

                        // 在库存列表寻找匹配库存
                        for (let i = 0, len = goodsSpecList.length; i < len; i++) {
                            const skuGroup = goodsSpecList[
                                i
                            ].specAttrAndAttrValue.map((item, index) => {
                                return item.specAttrValueId;
                            });
                            let specStorage;
                            if (
                                this.goodsDetails.seckillFlag &&
                                this.goodsDetails.frontSeckillActivityPageDTO
                                    .activityState == 1
                            ) {
                                specStorage = goodsSpecList[i].activityStorage;
                            } else {
                                specStorage = goodsSpecList[i].specStorage;
                            }
                            if (
                                isSubset(willSelected, skuGroup) &&
                                (specStorage == 0 || goodsSpecList[i].specShow != 1)
                            ) {
                                tempObj["isDisabled"] = true;
                                this.$set(
                                    this.goodsSpecNameValue[INDEX]
                                        .specAttrValueVOList,
                                    index,
                                    tempObj
                                );
                                break;
                            }
                        }
                    });
                });
            },

            // 加入购物车（未登录也可加入购物车）
            handleAddToCart() {
                if (this.specStorage == 0 || this.specShow != 1) return;

                if (this.addCartLoading) return;

                // 传递数据调接口
                let params = {
                    goodsNum: this.goodsNum,
                    specId: this.addCartSpecId,
                };

                this.$emit("handleAddCart", params);
            },

            // 立即购买
            handleBuyItNow() {
                if (this.specStorage == 0 || this.specShow != 1) return;

                if (this.loginFlag == 0) {
                    this.$emit("handleShowLoginModal");
                    return;
                }

                this.$router.push({
                    path: "/fillOrder",
                    query: {
                        number: this.goodsNum,
                        specId: this.$route.query.specId,
                    },
                });
            },

            // 收藏商品
            handleCollectGoods() {
                if (this.loginFlag == 0) {
                    this.$emit("handleShowLoginModal");
                    return;
                }

                if (this.goodsIsCollect == 1) {
                    let params = [this.addCartSpecId];
                    this.$emit("handleGoodsCancelCollection", params);
                } else {
                    let params = {
                        specId: this.addCartSpecId,
                        goodsId: this.$route.query.goodsId,
                        favPrice: this.goodsDetails.specSellPrice,
                    };
                    this.$emit("handleGoodsCollection", params);
                }
            },

            // 领取优惠券
            handleReceive() {
                if (this.loginFlag == 0) this.$emit("handleShowLoginModal");
                else this.$refs.receiveCoupon.visible = true;
            },

            // 计算预告开始倒计时
            previewCountdown() {
                // await this.$nextTick()
                if (
                    this.goodsDetails.frontSeckillActivityPageDTO &&
                    this.goodsDetails.frontSeckillActivityPageDTO.activityStart !==
                        null
                ) {
                    setInterval(() => {
                        var date = new Date(
                            this.goodsDetails.frontSeckillActivityPageDTO.activityStartDate
                        );
                        // console.log(11111,date)
                        var timestamp = new Date();
                        if (timestamp >= date) {
                            this.goodsDetails.frontSeckillActivityPageDTO.activityState = 1;
                            this.activityStatus = true;
                        } else {
                            this.goodsDetails.frontSeckillActivityPageDTO.activityState = 0;
                            this.activityStatus = false;
                        }
                    }, 1000);
                }
            },

            // 秒杀倒计时
            async handleCountDown() {
                console.log("计算倒计时");
                await this.$nextTick();
                let endDateTime = new Date(
                    this.goodsDetails.frontSeckillActivityPageDTO.activityEndDate
                );
                this.$options.filters.filterCountDown(
                    endDateTime,
                    ".flash-sale .countDown"
                );
                // console.log(333,"filterCountDown")
            },

            // 更多秒杀
            handleMoreSeckill() {
                this.$router.push({
                    name: "seckillZone",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    /deep/ .ivu-radio-group-item {
        position: relative !important;
    }

    .container1 {
        width: 1200px;
        background-color: #fff;
        margin: 0 auto;

        .prodcut-main-body {
            display: flex;
            // justify-content: space-between;

            .main-image {
                width: 480px;
                // height: 663px;
                margin-right: 20px;
            }

            .magnifier-bottom {
                display: flex;
                margin: 20px 0 30px 0;
                .collect {
                    display: flex;
                    align-items: center;

                    &.collected {
                        & > .collect-num {
                            color: $primary-color;
                            font-weight: 600;
                        }
                    }
                }

                .collect > .collect-num {
                    margin: 0 0 0 6px;
                    color: #333333;
                    // line-height: 50px;
                    cursor: pointer;
                }

                .sales-volume {
                    margin: 0 0 0 30px;
                    display: flex;
                    align-items: center;
                }

                .sales-volume > .sales-volume-num {
                    margin: 0 0 0 6px;
                    color: #333333;
                    // line-height: 50px;
                    cursor: pointer;
                }
            }

            .main-area {
                width: 640px;

                .goods-name {
                    font-size: 16px;
                    font-weight: 600;
                    font-family: PingFangSC-Regular, PingFang SC;
                    color: #222;
                    line-height: 22px;
                    letter-spacing: 1px;
                }

                .goods-label {
                    width: 32px;
                    height: 18px;
                    line-height: 16px;
                    text-align: center;
                    padding: 0 0 0 1px;
                    background: linear-gradient(
                        90deg,
                        rgba(221, 41, 28, 1) 0%,
                        rgba(255, 78, 2, 1) 100%
                    );
                    border: 0;
                    border-radius: 2px;
                    vertical-align: -5px;

                    /deep/ .ivu-tag-text {
                        font-size: 12px;
                        font-weight: 400;
                        color: rgba(255, 255, 255, 1);
                        text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.21);
                        line-height: 18px;
                        text-align: center;
                    }
                }

                .goods-sec-name {
                    margin: 4px 0 20px 0;
                    font-size: 12px;
                    font-weight: 400;
                    font-family: PingFangSC-Regular, PingFang SC;
                    color: #999;
                    line-height: 15px;
                    letter-spacing: 0.5px;
                }

                .seckill-notice {
                    width: 640px;
                    height: 40px;
                    padding: 0 10px;
                    background: rgba(221, 38, 25, 0.06);
                    display: flex;
                    align-items: center;
                    color: $primary-color;

                    p {
                        margin-right: 15px;

                        &:nth-of-type(3) {
                            color: #000000;
                            margin-right: 5px;
                        }
                    }
                }

                .flash-sale {
                    width: 640px;
                    height: 40px;
                    background: rgba(221, 38, 25, 0.06);
                    padding: 0 10px;
                    margin: 20px 0 20px 0;
                    line-height: 40px;
                    display: flex;
                    justify-content: space-between;

                    a {
                        color: #222;
                        display: flex;
                        align-items: center;
                        transition: color 0.3s;

                        &:hover {
                            color: $primary-color;
                        }

                        img {
                            margin: 0 6px;
                        }
                    }
                }

                .main-area-title {
                    min-width: 42px;
                    color: #999;
                    margin: 0 10px 0 0;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                }

                .basic-style-5 {
                    margin: 0 0 5px 0;

                    .main-area-title {
                        line-height: 30px;
                    }
                }

                .basic-style-20 {
                    margin: 20px 0 0 0;

                    align-items: center;
                }

                .basic-style-30 {
                    margin: 30px 0 0 0;

                    align-items: center;
                }

                .prize,
                .basic-style-30,
                .basic-style-20,
                .basic-style-5 {
                    display: flex;
                    color: #dd2619;
                    line-height: 20px;

                    /* 价格 */
                    .prize-num {
                        display: flex;
                        align-items: baseline;
                        margin: 0 10px 0 0;
                        /deep/span:nth-child(2){
                        margin: 0 !important;
                        }
                    }

                    /* 价格减价 */
                    .prize-change-num {
                        margin-top: 2px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #999999;
                        line-height: 19px;
                        text-decoration: line-through;
                    }

                    /* 优惠券 */
                    &.couponList {
                        margin-bottom: 4px;
                    }

                    .coupon-content {
                        height: 22px;
                        background: url("/img/03.goodsClass/03-03.goodsDetail/coupon_bg.png")
                            no-repeat center/cover;
                        padding: 0 8px;
                        margin: 0 10px 0 0;
                        color: #fff;
                        line-height: 22px;
                    }

                    /* 运费 */
                    .coupon-word {
                        color: #333;
                        display: flex;
                        align-items: center;
                    }

                    .ml10 {
                        margin: 0 0 0 10px;
                    }

                    .hintBuyGoodsNum {
                        color: #999;
                    }

                    .recive-btn {
                        cursor: pointer;
                        font-weight: normal;
                        opacity: 1;
                        transition: opacity 0.8s;

                        &:hover,
                        &:active {
                            opacity: 0.6;
                        }
                    }

                    /*满减*/
                    .price-reduction-content {
                        color: #666;
                        display: flex;
                        align-items: center;
                        p {
                            margin-right: 10px;
                            max-width: 140px;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;

                            &.highlight {
                                font-size: 14px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: #333333;
                            }
                        }

                        .dropDown-list {
                            position: relative;
                            /deep/ .ivu-dropdown {
                                margin-top: -1px;
                            }
                            &::before {
                                content: "";
                                display: block;
                                width: 100%;
                                height: 100%;
                                box-shadow: 0 0 2px 1px rgba(0, 0, 0, 0.1);
                                border-bottom: none;
                                border-top-left-radius: 4px;
                                border-top-right-radius: 4px;
                                position: absolute;
                                left: 0;
                                top: 0;
                                opacity: 0;
                                transition: opacity 0.8s;
                            }

                            &::after {
                                content: "";
                                display: block;
                                width: 100%;
                                height: 5px;
                                background: #ffffff;
                                position: absolute;
                                z-index: 3;
                                left: 0;
                                bottom: 3px;
                                box-shadow: 0 0 4px rgba(255, 255, 255, 1);
                                opacity: 0;
                                transition: opacity 1s;
                            }

                            button .ivu-icon {
                                transition: transform 0.3s;
                            }

                            &:hover,
                            &.hoverActive {
                                &::before,
                                &::after {
                                    opacity: 1 !important;
                                }

                                button {
                                    color: $primary-color;
                                }
                            }

                            &:hover,
                            &.hoverActive {
                                button {
                                    color: $primary-color;

                                    .ivu-icon {
                                        transform: rotate(-180deg);
                                        color: $primary-color;
                                    }
                                }
                            }

                            &.hoverActive:after {
                                top: 27px;
                            }

                            &.hoverActive:before {
                                height: 100%;
                                top: 0;
                            }
                        }
                    }

                    /deep/ .ivu-radio-group-button .ivu-radio-wrapper {
                        height: 28px;
                        line-height: 28px;
                        box-sizing: content-box;
                        border-radius: 0px;
                        margin: 0 10px 10px 0;
                        color: #999999;
                        background-color: #ffffff;
                        outline: none;
                        border-left: 1px solid #ebebeb;
                        border-color: #ebebeb;

                        &:before,
                        &:after {
                            display: none;
                        }

                        &:hover {
                            color: #dd2619;
                            border-color: #dd2619;
                            opacity: 0.8;
                        }
                    }

                    /deep/ .ivu-radio-group-button .ivu-radio-wrapper-checked {
                        border-color: #dd2619;
                        box-shadow: none;
                        color: #dd2619;
                        opacity: 1 !important;
                        position: static;
                    }

                    /deep/ .ivu-radio-group-button .ivu-radio-wrapper-disabled {
                        color: #999999 !important;
                        border-color: #dcdee2 !important;
                        background: #efefef !important;
                        font-weight: 400 !important;
                    }
                }

                .line {
                    width: 100%;
                    height: 1px;
                    background-color: #f1f1f1;
                    margin: 20px 0 20px 0;
                }

                .group-buying-word {
                    margin: 15px 0 0 40px;
                    font-size: 14px;
                    color: #999;
                    line-height: 14px;

                    span {
                        color: #4a9cff;
                        font-weight: bold;
                        cursor: pointer;
                    }
                }

                .operation-box {
                    width: 280px;
                    margin: 40px 0 30px 51px;
                    display: flex;
                    justify-content: space-between;

                    .join-cart,
                    .buy-it {
                        width: 120px;
                        height: 40px;
                        background: linear-gradient(
                            270deg,
                            rgba(255, 178, 0, 1) 0%,
                            rgba(246, 136, 14, 1) 100%
                        );
                        border-radius: 4px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: #ffffff;
                        line-height: 40px;
                        text-align: center;
                        cursor: pointer;
                        transition: all 0.3s;

                        &:hover {
                            opacity: 0.8;
                            border-color: #ffffff;
                        }

                        &:active {
                            opacity: 1;
                        }
                    }

                    .buy-it {
                        width: 120px;
                        height: 40px;
                        background: linear-gradient(
                            90deg,
                            rgba(222, 42, 28, 1) 0%,
                            rgba(255, 78, 3, 1) 100%
                        );
                        border-radius: 4px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: #ffffff;
                        line-height: 40px;
                        text-align: center;
                        cursor: pointer;
                        transition: all 0.3s;

                        &:hover {
                            opacity: 0.8;
                        }

                        &:active {
                            opacity: 1;
                        }
                    }

                    .noStorage {
                        cursor: not-allowed;
                        background: #cecece;
                        border-color: #cecece;
                    }
                }
            }
        }

        .outOfStock {
            margin-top: 10px;
            color: #999999;
        }
    }
</style>


<style lang="scss">
    $primary-color: #dd2619;

    .v-transfer-dom {
        .artCon {
            &:nth-of-type(2n) {
                margin-right: 30px !important;
            }
        }
    }

    .reduce-activity-dropdown.ivu-select-dropdown {
        width: 656px;
        min-height: 70px;
        left: 575px !important;
        right: 0;
        margin: 0px auto 0;
        z-index: 2 !important;

        .ivu-dropdown-menu {
            text-align: left;
            padding: 20px;
            display: inline-block;

            li {
                color: #565656;
                display: inline-block;
                padding: 5px 20px;

                &.active {
                    color: $primary-color;
                }
            }
        }
    }

    /deep/ .custom-carousel-arrow {
        width: 20px !important;
        height: 26px !important;
    }

    /deep/ .ivu-btn {
        font-size: 10px;
    }

    .ivu-btn {
        height: 20px;
        border-color: #ffffff;
        line-height: 19px;
    }
</style>
