<template>
    <div class>
        <Row>
            <!-- <i-col :span="4">占位</i-col> -->
            <i-col :span="20">
                <Card dis-hover style="height: 120px; margin-top: 0">
                    <!-- <p slot="title"> -->
                    <div class="headerContent">
                        <!-- <div class="header" style="height:76px;display:flex;"> -->
                        <div class="whiteBorder">
                            <img
                                :src="dataForm.memberAvatar | filterImgUrl"
                                style="
                                    border-radius: 50%;
                                    width: 56px;
                                    height: 56px;
                                "
                                alt
                            />
                        </div>
                        <div class="head-content">
                            <div class="header-left">
                                <!-- memberName -->
                                <div class="header-left-name">
                                    {{ dataForm.nickName }}
                                </div>
                                <!-- <el-button round>黄金会员</el-button> -->
                                <div class="header-left-mermber">
                                    {{ dataForm.memberGraderName }}
                                </div>
                            </div>
                            <div class="header-left-option">
                                <!-- <span class="fontStyle">
                                    <router-link to="">会员中心</router-link>
                                </span>
                                <span class="fontStyle">
                                    <router-link to="">我的主页</router-link>
                </span>-->
                                <span class="fontStyle">
                                    <router-link
                                        to="/personalCenterBase/securitySetting"
                                        >安全设置</router-link
                                    >
                                </span>
                                <span class="fontStyle">
                                    <router-link
                                        to="/personalCenterBase/addressManagement"
                                        >地址管理</router-link
                                    >
                                </span>
                            </div>
                        </div>
                        <!-- </div> -->
                        <div class="header-right-box">
                            <div class="header-right">
                                <div
                                    class="header-right-div"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/coupon'
                                        )
                                    "
                                >
                                    <p
                                        class="header-right-number"
                                        style="color: #dd2619"
                                    >
                                        {{
                                            dataForm.couponNum
                                                ? dataForm.couponNum
                                                : 0
                                        }}
                                    </p>
                                    <p class="header-right-text">优惠券</p>
                                </div>
                                <div
                                    class="header-right-div"
                                    @click="
                                        $router.push(
                                            '/personalCenterbase/favoritesList'
                                        )
                                    "
                                >
                                    <p class="header-right-number">
                                        {{
                                            dataForm.collectNum
                                                ? dataForm.collectNum
                                                : 0
                                        }}
                                    </p>
                                    <p class="header-right-text">商品收藏</p>
                                </div>
                                <div
                                    class="header-right-div"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/myPoints'
                                        )
                                    "
                                >
                                    <p class="header-right-number">
                                        {{
                                            dataForm.pointValue
                                                ? dataForm.pointValue
                                                : 0
                                        }}
                                    </p>
                                    <p class="header-right-text">积分</p>
                                </div>
                                <div
                                    class="header-right-div"
                                    @click="$router.push('/')"
                                >
                                    <p class="header-right-number">
                                        {{
                                            dataForm.growthValue
                                                ? dataForm.growthValue
                                                : 0
                                        }}
                                    </p>
                                    <p class="header-right-text">成长值</p>
                                </div>
                                <div
                                    class="header-right-div"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/myTracks'
                                        )
                                    "
                                >
                                    <p class="header-right-number">
                                        {{
                                            dataForm.browseNum
                                                ? dataForm.browseNum
                                                : 0
                                        }}
                                    </p>
                                    <p class="header-right-text">足迹</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- </p> -->
                </Card>
                <!-- 个人中心头部内容 -->

                <!-- 个人中心我的订单 -->
                <Card dis-hover style="padding: 0 40px 20px">
                    <div v-if="dataList.length == 0" class="null">
                        <div class="record">我的订单</div>
                        <div style="margin: 20px auto 20px auto">
                            <img
                                src="/img/03.goodsClass/03-01.goodsClassification/no-data.png"
                                alt
                                style="width: 160px; height: 160px"
                            />
                            <p
                                style="
                                    font-size: 16px;
                                    color: #3a3a3a;
                                    text-align: center;
                                "
                            >
                                暂无订单
                            </p>
                        </div>
                    </div>

                    <div v-else-if="dataList.length > 0">
                        <div class="orderTop">
                            <div class="record">我的订单</div>
                            <div @mouseout="mouseOut" @mouseover="mouseOver">
                                <div
                                    class="allOrder"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/myOrders'
                                        )
                                    "
                                >
                                    全部订单
                                    <img
                                        :src="arrow"
                                        alt
                                        style="height: 10px"
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="order-list">
                            <template v-for="(item, index) in dataList">
                                <!-- data循环里的appStatus-{{item.appStatus}} -->
                                <!-- 订单中商品在不同的库房或商家，故拆单  只有groupStatus!= 0/10  并且 orderChildrenList有值   子组件展示的数据也取orderChildrenList里的-->
                                <separate
                                    :thead="table.head"
                                    v-if="
                                        (item.orderStatus == 20 ||
                                            item.orderStatus == 30 ||
                                            item.orderStatus == 40 ||
                                            (item.orderStatus == 0 &&
                                                item.paymentName)) &&
                                        item.orderChildrenList != null
                                    "
                                    :data="item"
                                    :key="index"
                                ></separate>

                                <!-- 待付款 子组件展示的数据取orderGoodsDtoList里的-->
                                <wait-pay
                                    style="width: 864px"
                                    :thead="table.head"
                                    v-if="item.appStatus == 10"
                                    :data="item"
                                    :key="index"
                                ></wait-pay>

                                <!-- 待收货 子组件展示的数据取orderGoodsDtoList里的-->
                                <wait-receive
                                    :thead="table.head"
                                    v-if="
                                        (item.appStatus == 20 ||
                                            item.appStatus == 30) &&
                                        item.orderChildrenList == null
                                    "
                                    @childFn="parentFn"
                                    :data="item"
                                    :key="index"
                                ></wait-receive>

                                <!-- 已完成 子组件展示的数据取orderGoodsDtoList里的-->
                                <completed
                                    :thead="table.head"
                                    v-if="
                                        item.appStatus == 40 &&
                                        item.orderChildrenList == null
                                    "
                                    :data="item"
                                    @getDataList="personalOrder"
                                    :key="index"
                                ></completed>

                                <!-- 已取消 子组件展示的数据取orderGoodsDtoList里的-->
                                <canceled
                                    :thead="table.head"
                                    v-if="
                                        item.appStatus == 0 &&
                                        item.paymentName == null
                                    "
                                    @getDataList="personalOrder"
                                    :data="item"
                                    :key="index"
                                ></canceled>
                            </template>
                        </div>
                    </div>
                </Card>

                <!-- 个人中心收藏商品 -->
                <Card dis-hover>
                    <div v-if="favoriteListNum == 0" class="null">
                        <div style="margin: 20px auto 40px auto">
                            <div
                                class="record"
                                style="margin: 20px 0px 20px 40px"
                            >
                                我的收藏
                            </div>
                            <img
                                src="/img/03.goodsClass/03-01.goodsClassification/no-data.png"
                                alt
                                style="width: 160px; height: 160px"
                            />
                            <p
                                style="
                                    font-size: 16px;
                                    color: #3a3a3a;
                                    text-align: center;
                                "
                            >
                                暂无收藏
                            </p>
                        </div>
                    </div>

                    <div class="collect" v-else-if="favoriteListNum > 0">
                        <div class="collect-title">
                            <span class="collect-title-left">我的收藏</span>
                            <div
                                class="left"
                                @mouseout="mouseOut"
                                @mouseover="mouseOver"
                            >
                                <span
                                    class="collect-title-right"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/favoritesList'
                                        )
                                    "
                                >
                                    查看全部
                                    <img
                                        :src="arrow"
                                        alt
                                        style="height: 10px"
                                    />
                                </span>
                            </div>
                        </div>
                        <div class="switch">
                            <ul style="display: flex">
                                <li
                                    :class="collection == 0 ? 'on' : 'off'"
                                    @click="collectionChange(0)"
                                >
                                    1
                                </li>
                                <li
                                    :class="collection == 1 ? 'on' : 'off'"
                                    @click="collectionChange(1)"
                                    style="margin: 0 4px"
                                >
                                    2
                                </li>
                                <li
                                    :class="collection == 2 ? 'on' : 'off'"
                                    @click="collectionChange(2)"
                                >
                                    3
                                </li>
                            </ul>
                        </div>
                        <div
                            v-model="value"
                            loop
                            :radius-dot="false"
                            :arrow="favoriteList.length < 5 ? 'never' : 'hover'"
                            style="display: flex; overflow: hidden"
                        >
                            <div
                                style="display: flex"
                                class="collect-li"
                                v-if="favoriteListNum < 5"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index) in favoriteList"
                                    :key="index"
                                    @click="
                                        goDetailPage(item.specId, item.goodsId)
                                    "
                                >
                                    <img
                                        :src="item.pictureUrl | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <!-- <span style="font-size:14px;font-weight:400;margin-right:-6px">￥</span>
                      {{item.favPrice}} -->
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.favPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </div>
                            <div
                                style="display: flex"
                                class="collect-li"
                                v-for="(favoriteListItem,
                                index1) in favoriteList"
                                :key="index1"
                                v-else-if="favoriteListNum > 4"
                                v-show="index1 == collection"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index2) in favoriteListItem"
                                    :key="index2"
                                    @click="
                                        goDetailPage(item.specId, item.goodsId)
                                    "
                                >
                                    <img
                                        :src="item.pictureUrl | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <!-- <span style="font-size:14px;font-weight:400;margin-right: -6px;">￥</span>
                      {{item.favPrice}} -->
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.favPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- <span class="swiper_num">
                            <span>{this.favoriteList.length}\4</span>
            </span>-->
                        <removeOrder
                            v-if="$store.state.myOrders.reasonList.length > 0"
                            @childFn="parentFn"
                        ></removeOrder>
                    </div>
                </Card>
                <!-- 我的足迹 -->
                <!-- <Card dis-hover style="padding: 3px 0">
                    <div v-if="browseListNum == 0" class="null">
                        <div style="margin: 20px auto 40px auto">
                            <div
                                class="record"
                                style="margin: 20px 0px 20px 40px"
                            >
                                我的足迹
                            </div>
                            <img
                                src="/img/03.goodsClass/03-01.goodsClassification/no-data.png"
                                alt
                                style="width: 160px; height: 160px"
                            />
                            <p
                                style="
                                    font-size: 16px;
                                    color: #3a3a3a;
                                    text-align: center;
                                "
                            >
                                暂无足迹
                            </p>
                        </div>
                    </div>

                    <div class="collect" v-else-if="browseListNum > 0">
                        <div class="collect-title">
                            <span class="collect-title-left">我的足迹</span>
                            <div
                                class="left"
                                @mouseout="mouseOut"
                                @mouseover="mouseOver"
                            >
                                <span
                                    class="collect-title-right"
                                    @click="$router.push('/myTracks')"
                                >
                                    查看全部
                                    <img
                                        :src="arrow"
                                        alt
                                        style="height: 10px"
                                    />
                                </span>
                            </div>
                        </div>
                        <Carousel
                            v-model="value1"
                            loop
                            :radius-dot="false"
                            :arrow="favoriteList.length < 5 ? 'never' : 'hover'"
                        >
                            <CarouselItem
                                style="display: flex"
                                class="collect-li"
                                v-if="browseListNum < 5"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index) in browseList"
                                    :key="index"
                                    @click="
                                        goDetailPage(
                                            item.goodsSpecId,
                                            item.goodsId
                                        )
                                    "
                                >
                                    <img
                                        :src="item.goodsImage | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.goodsPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </CarouselItem>
                            <CarouselItem
                                style="display: flex"
                                class="collect-li"
                                v-for="(browseListItem, index1) in browseList"
                                :key="index1"
                                v-else-if="browseListNum > 4"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index) in browseListItem"
                                    :key="index"
                                    @click="
                                        goDetailPage(
                                            item.goodsSpecId,
                                            item.goodsId
                                        )
                                    "
                                >
                                    <img
                                        :src="item.goodsImage | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.goodsPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </CarouselItem>
                        </Carousel>
                    </div>
                </Card> -->
                <Card dis-hover style="padding: 3px 0">
                    <div v-if="browseListNum == 0" class="null">
                        <div style="margin: 20px auto 40px auto">
                            <div
                                class="record"
                                style="margin: 20px 0px 20px 40px"
                            >
                                我的足迹
                            </div>
                            <img
                                src="/img/03.goodsClass/03-01.goodsClassification/no-data.png"
                                alt
                                style="width: 160px; height: 160px"
                            />
                            <p
                                style="
                                    font-size: 16px;
                                    color: #3a3a3a;
                                    text-align: center;
                                "
                            >
                                暂无足迹
                            </p>
                        </div>
                    </div>

                    <div class="collect" v-if="browseListNum > 0">
                        <div class="collect-title">
                            <span class="collect-title-left">我的足迹</span>

                            <div
                                class="left"
                                @mouseout="mouseOut"
                                @mouseover="mouseOver"
                            >
                                <span
                                    class="collect-title-right"
                                    @click="
                                        $router.push(
                                            '/personalCenterBase/myTracks'
                                        )
                                    "
                                >
                                    查看全部
                                    <img
                                        :src="arrow"
                                        alt
                                        style="height: 10px"
                                    />
                                </span>
                            </div>
                        </div>
                        <div class="switch">
                            <ul style="display: flex">
                                <li
                                    :class="number == 0 ? 'on' : 'off'"
                                    @click="tabChange(0)"
                                >
                                    1
                                </li>
                                <li
                                    :class="number == 1 ? 'on' : 'off'"
                                    @click="tabChange(1)"
                                    style="margin: 0 4px"
                                >
                                    2
                                </li>
                                <li
                                    :class="number == 2 ? 'on' : 'off'"
                                    @click="tabChange(2)"
                                >
                                    3
                                </li>
                            </ul>
                        </div>

                        <div
                            v-model="value1"
                            loop
                            :radius-dot="false"
                            :arrow="favoriteList.length < 5 ? 'never' : 'hover'"
                            style="display: flex; overflow: hidden"
                        >
                            <div
                                style="display: flex"
                                class="collect-li"
                                v-if="browseListNum < 5"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index) in browseList"
                                    :key="index"
                                    @click="
                                        goDetailPage(
                                            item.goodsSpecId,
                                            item.goodsId
                                        )
                                    "
                                >
                                    <img
                                        :src="item.goodsImage | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <!-- <span style="font-size:14px;font-weight:400;margin-right: -6px;">￥</span> -->
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.goodsPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </div>
                            <div
                                style="display: flex"
                                class="collect-li"
                                v-for="(browseListItem, index1) in browseList"
                                :key="index1"
                                v-else-if="browseListNum > 4"
                                v-show="index1 == number"
                            >
                                <div
                                    class="cellect-li-box"
                                    v-for="(item, index) in browseListItem"
                                    :key="index"
                                    @click="
                                        goDetailPage(
                                            item.goodsSpecId,
                                            item.goodsId
                                        )
                                    "
                                >
                                    <img
                                        :src="item.goodsImage | filterImgUrl"
                                        class="cellect-li-img"
                                        alt
                                    />
                                    <div class="cellect-li-font">
                                        <p class="font-14">
                                            {{ item.goodsName }}
                                        </p>
                                        <p class="font-12">
                                            {{ item.goodsSubTitle }}
                                        </p>
                                        <p class="font-10">
                                            <!-- <span style="font-size:14px;font-weight:400;margin-right: -6px;">￥</span> -->
                                            <template>
                                                <p
                                                    class="price"
                                                    v-html="
                                                        $options.filters.filterPrice(
                                                            item.goodsPrice
                                                        )
                                                    "
                                                ></p>
                                            </template>
                                        </p>
                                        <div class="content-spaceBetween"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Card>
            </i-col>
        </Row>
    </div>
</template>

<script>
    // import store from "@/vuex/store";
    // import { mapState, mapMutations } from "vuex";
    import waitPay from "@/components/06.personalCenter/06-07.myOrders/waitPay";
    import waitReceive from "@/components/06.personalCenter/06-07.myOrders/waitReceive";
    import completed from "@/components/06.personalCenter/06-07.myOrders/completed";
    import canceled from "@/components/06.personalCenter/06-07.myOrders/canceled";
    import separate from "@/components/06.personalCenter/06-07.myOrders/separate";
    import reasonPopup from "@/components/06.personalCenter/06-07.myOrders/reasonPopup.vue"; //取消订单的理由
    import removeOrder from "@/components/06.personalCenter/06-07.myOrders/removeOrder.vue"; //取消订单的理由

    import {
        OrderConfirmReceipt,
        CartAgain,
    } from "@/api/api_06-07-01personalMyOrders.js";
    import { mapActions } from "vuex";
    import {
        homeMember,
        orderPage,
        favoritesPage,
        browseList,
    } from "@/api/api_06-08-01personalData.js";
    export default {
        name: "personalCenter",
        components: {
            waitPay,
            waitReceive,
            completed,
            canceled,
            separate,
            reasonPopup,
            removeOrder,
        },
        head() {
            return {
                title: "个人中心",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        data() {
            return {
                arrow: "/img/06.personalCenter/arrow.png",
                //wt -start
                table: {
                    head: [
                        {
                            title: "订单详情",
                            width: "336px",
                        },
                        {
                            title: "收货人",
                            width: "154px",
                        },
                        {
                            title: "金额",
                            width: "90px",
                        },
                        {
                            title: "订单状态",
                            width: "154px",
                        },
                        {
                            title: "操作",
                            width: "130px",
                        },
                    ],
                },
                //wt -end
                page: 1,
                limit: 2,
                limit1: 12,
                index: 0,
                value: 0,
                value1: 0,
                number: 0,
                collection: 0,
                memberAvatar: "",
                // appStatus:'',
                items: {
                    appStatus: "",
                    paymentName: "",
                },
                dataForm: {
                    memberGraderName: "",
                    memberName: "",
                    memberAvatar: "",
                    appStatus: "",
                    paymentName: "",
                    nickName: "",
                },
                dataList: {
                    id: "",
                    createDate: "",
                    orderSn: "",
                    storeName: "",
                    cancalDate: "",
                    goodsImage: "",
                    goodsName: "",
                    appStatus: "",
                    orderList: {},
                    paymentName: "",
                },
                favoriteList: {
                    pictureUrl: "",
                    goodsName: "",
                    goodsSubTitle: "",
                    favPrice: "",
                    appStatus: "",
                },
                favoriteListNum: "",
                browseList: {
                    goodsImage: "",
                    goodsName: "",
                    goodsPrice: "",
                    goodsSubTitle: "",
                    appStatus: "",
                },
                browseListNum: "",
                order: "",
            };
        },
        //   computed: {
        //     ...mapState(["signUpStep"])
        //   },
        //   methods: {
        //     ...mapMutations(["SET_SIGN_UP_SETP"])
        //   },
        //   store,
        //   mounted() {
        //     this.SET_SIGN_UP_SETP(0);
        //   }
        created() {
            this.homeMember();
            this.personalOrder();
            this.getfavority();
            this.getbrowseList();
        },
        mounted() {
            // this.actTabs(this.$store.state.myOrders.currentLabelnum);
            if (this.$route.name == "personalCenter") {
                this.personCenterTitle = "个人中心";
                var SET_DISPLAY_STYLE_DATA = {
                    index1Flag: 2,
                    index2Flag: 0,
                    personCenterTitle: "个人中心",
                };
                this.$store.commit(
                    "personalCenterBase/SET_DISPLAY_STYLE",
                    SET_DISPLAY_STYLE_DATA
                );
            }
        },
        methods: {
            ...mapActions("main", ["actCartList"]),
            //查看物流
            handleLogistics() {},
            //立即付款
            toPay(id) {
                this.$router.push({
                    path: "/toPay",
                    query: {
                        orderId: this.orderGoodsDTOList.orderId,
                    },
                });
            },
            // goADV(data) {
            //     if (data !== null && data !== '') {
            //         window.open(data)
            //     }
            // },
            //确认收货
            async handleConfirm(id) {
                try {
                    let res = await OrderConfirmReceipt(this.dataList.id);
                    if (res && res.code == 200) {
                        this.$Message.success("确认收货成功");
                    }
                } catch (e) {}
            },
            //查看详情
            handleDetail(id, appStatus) {
                this.$router.push({
                    path: "/personalCenterBase/myOrdersDetail",
                    query: {
                        id: this.data.id,
                        appStatus: this.data.appStatus,
                    },
                });
            },
            //再次购买
            async handleAgain(specId, goodsNum) {
                let obj = {
                    saveCartDTO: [],
                };
                obj.saveCartDTO.push({
                    goodsNum: goodsNum,
                    specId: specId,
                });
                try {
                    let res = await CartAgain(obj);
                    if (res && res.code == 200) {
                        this.actCartList();
                        this.$Message.success("添加购物车成功");
                    }
                } catch (e) {}
            },
            //鼠标悬浮图片变化
            mouseOut: function () {
                this.arrow = "/img/06.personalCenter/arrow.png";
            },
            mouseOver: function () {
                this.arrow = "/img/06.personalCenter/redarrow.png";
            },
            //获取个人信息的数据
            homeMember(id) {
                var obj = {
                    id: id,
                };
                homeMember(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataForm = res.data;
                        } else {
                            this.$api.msg(res.msg);
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            //接收父类的信号，刷新页面
            parentFn(order) {
                console.log("rtyuio");
                console.log("执行了父类方法");
                this.order = order;
                this.personalOrder();
            },
            //获取订单的数据
            personalOrder() {
                let obj = {
                    page: this.page,
                    limit: this.limit,
                };
                orderPage(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataList = res.data.list;
                            console.log(this.dataList);
                            this.dataList.orderList =
                                res.data.list.orderGoodsDTOList;
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            tabChange(number) {
                this.number = number;
                console.log(888, number);
            },
            collectionChange(collection) {
                this.collection = collection;
            },
            //获取我的收藏数据
            getfavority() {
                let obj = {
                    page: this.page,
                    limit: this.limit1,
                };
                (this.favoriteList = []),
                    favoritesPage(obj)
                        .then((res) => {
                            if (res.code == 200) {
                                this.favoriteListNum = res.data.list.length;
                                if (res.data.list.length < 5) {
                                    this.favoriteList = res.data.list;
                                } else {
                                    this.favoriteList = [];
                                    for (
                                        var i = 0;
                                        i < res.data.list.length;
                                        i += 4
                                    ) {
                                        this.favoriteList.push(
                                            res.data.list.slice(i, i + 4)
                                        );
                                    }
                                }
                            }
                        })
                        .catch((error) => {
                            console.log(error);
                        });
            },
            goDetailPage(specId, goodsId) {
                console.log("1111111");
                console.log(specId + "   !!!!!   " + goodsId);
                //跳转到商品详情页页面
                let newpage = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId: goodsId,
                        specId: specId,
                    },
                });
                window.open(newpage.href, "_blank");
            },

            //获取我的足迹的数据
            getbrowseList() {
                browseList()
                    .then((res) => {
                        if (res.code == 200) {
                            this.browseListNum = res.data.length;
                            if (res.data.length < 5) {
                                this.browseList = res.data;
                            } else {
                                this.browseList = [];
                                for (var i = 0; i < res.data.length; i += 4) {
                                    this.browseList.push(res.data.slice(i, i + 4));
                                }
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
        },
    };
</script>

<style lang="scss" scoped>
    //  $primary-color: #DD2619;
    .font {
        font-family: "微软雅黑";
        font-weight: 400;
        color: #999999;
    }
    .switch {
        position: absolute;
        left: 800px;
        bottom: 385px;
        .on {
            width: 12px;
            height: 12px;
            border-radius: 8px;
            background-color: #dd2619;
            color: #fff;
            font-size: 10px;
            line-height: 12px;
            text-align: center;
            cursor: pointer;
        }
        .off {
            width: 12px;
            height: 12px;
            font-size: 10px;
            border-radius: 8px;
            background-color: #ebebeb;
            color: #666666;
            line-height: 12px;
            text-align: center;
            cursor: pointer;
        }
    }
    .order-list {
        width: 868px;
    }

    .font-14 {
        font-size: 14px;
        font-weight: 600;
        color: #222222 !important;
        font-family: PingFangSC-Medium, PingFang SC;
    }

    .font-12 {
        font-size: 12px;
    }

    .content-spaceBetween {
        display: flex;
        justify-content: space-between;
    }

    .text-align-center {
        text-align: center;
    }

    .color-666666 {
        color: #666666;
    }

    .color-999999 {
        color: #999999;
    }

    .fontStyle {
        width: 58px;
        height: 20px;
        font-size: 12px;
        margin: 0 0 10px 6px;
        color: #999999;
        border: 1px solid #999999;
        text-align: center;
        border-radius: 10px;

        a {
            color: #999999;

            &:hover {
                color: #dd2619;
            }
        }

        &:hover {
            border-color: #dd2619;
        }
    }

    .null {
        text-align: center;
    }

    .card-title {
        font-family: 微软雅黑;
        font-weight: 400;
        font-style: normal;
        font-size: 16px;
        color: rgb(153, 153, 153);
    }

    .headerContent {
        padding: 0 0 0 32px;
        display: flex;
        // height: 120px;
        align-items: center;

        .img {
            width: 56px;
            height: 56px;
            border-radius: 50%;
        }
    }

    .head-content {
        margin: 0 0 0 12px;
        display: flex;
        flex-flow: column;
        width: 275px;
    }

    .header-left {
        // height: 20px;
        margin: 0 0 10px 5px;
        text-align: left;
        align-items: center;
        display: flex;
    }

    .header-left-name {
        // height: 20px;
        // min-width: 180px;
        font-size: 18px;
        margin-right: 3px;
        line-height: 18px;
        color: #222222;
        // font-family: "微软雅黑 Bold", "微软雅黑 Regular", "微软雅黑";
        font-weight: 600;
        white-space: nowrap;
        overflow: hidden;
        flex: 1;
        padding-right: 10px;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .header-left-mermber {
        width: 60px;
        height: 20px;
        font-size: 12px;
        line-height: 20px;
        border-radius: 4px;
        // margin: 0 auto;
        color: #e4bb00;
        background: linear-gradient(
            rgba(69, 69, 69, 1) 0%,
            rgba(28, 28, 28, 1) 100%
        );
        text-align: center;
    }

    .header-left-option {
        width: 230px;
        height: 20px;
        font-size: 12px;
        line-height: 20px;
        display: flex;
        // flex-wrap: wrap;
    }

    .header-right-box {
        width: 527px;
        border-left: 1px solid #ebebeb;
        margin: 0 0 0 60px;
    }

    .header-right {
        display: flex;
        margin: 0 50px 0 59px;
    }

    .header-right-div {
        height: 60px;
        width: 60px;
        // height: 76px;
        margin: 0 30px 0 0;
        text-align: center;
        align-items: center;
        cursor: pointer;
    }

    .header-right-text {
        font-size: 12px;
        width: 60px;
        font-weight: 400;
        margin: 10px 0 8px;
        color: #666666;
        font-family: PingFangSC-Regular, PingFang SC;
    }

    .header-right-number {
        height: 26px;
        font-size: 26px;
        line-height: 26px;
        color: #555555;
        margin: 0;
        font-weight: 600;
        font-family: PingFangSC-Semibold, PingFang SC;
    }

    .orderTop {
        width: 868px;
        height: 18px;
        line-height: 18px;
        display: flex;
        justify-content: space-between;
        font-size: 18px;
        margin: 20px 0px;
    }

    .orderCenter {
        width: 868px;
        // height: 172px;
        margin: 19px 40px 0 40px;
        // border:1px solid #999999;
        box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);

        .orderMain {
            height: 44px;
            line-height: 44px;
            display: flex;
            background: #f6f6f6;
            color: #666666;
            justify-content: space-around;
        }
    }

    .orderRight1 {
        width: 154px;
        // margin: 30px 68px 0 0;
        display: flex;
        flex-direction: column;

        // text-align: center;
        .number {
            font-size: 20px;
            font-weight: 500;
            color: #333333;

            .small {
                font-size: 14px;
                font-weight: 400;
            }
        }

        .text {
            margin: 2px;
            font-size: 13px;
            display: flex;
            color: #999999;
        }

        .r-text1 {
            font-size: 16px;
            color: #dd2619;
            cursor: pointer;
        }

        .r-text2 {
            margin: 5px;
            font-size: 13px;
            color: #333333;
            cursor: pointer;

            &:hover {
                color: #dd2619;
            }
        }
    }

    .orderRight2 {
        width: 130px;
        display: flex;
        flex-flow: column;

        .number {
            font-size: 20px;
            font-weight: 500;
            color: #333333;

            .small {
                font-size: 14px;
                font-weight: 400;
            }
        }

        .text {
            margin: 2px;
            font-size: 13px;
            color: #999999;
        }

        .r-text1 {
            font-size: 16px;
            color: #dd2619;
            cursor: pointer;
        }

        .r-text2 {
            margin: 5px;
            font-size: 13px;
            color: #333333;
            cursor: pointer;
        }
    }

    .collect {
        padding: 0px 40px 40px;
        position: relative;
        .collect-title {
            // margin: 0 0 20px 0;
            display: flex;
            justify-content: space-between;

            .number {
                display: flex;
                line-height: 54px;
                margin-left: 629px;
                z-index: 999;
                font-size: 10px;
            }
            .collect-title-left {
                font-size: 14px;
                font-weight: 600;
                color: #333333;
                line-height: 54px;
            }

            .collect-title-right {
                font-size: 12px;
                font-weight: 400;
                color: #999999;
                line-height: 54px;
                padding-right: 15px;

                &:hover {
                    color: #dd2619;
                }
            }

            .collect-title-right {
                cursor: pointer;
            }
        }

        .cellect-li-box {
            width: 217px;
            // height: 300px;
            padding: 20px 20px;
            margin: 2px;

            &:hover {
                // border: 1px solid #ffffff;
                box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
                .font-14 {
                    color: #dd2619;
                }
            }
        }

        .cellect-li-img {
            width: 177px;
            height: 177px;
            // margin: 50px 28px 0;
        }

        .cellect-li-font {
            // margin: 17px 20px 0;
            .font-10 {
                font-size: 18px;
                color: #dd2619;
                font-weight: 600;
                // margin-top: 10px;
            }
            .font-14 {
                font-size: 14px;
                color: #333333;
                margin-top: 20px;
                text-overflow: -o-ellipsis-lastline;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 1;
                line-clamp: 1;
                -webkit-box-orient: vertical;
            }

            .font-12 {
                font-size: 12px;
                color: #999999;
                font-family: PingFangSC-Regular, PingFang SC;
                margin-top: 8px;
                margin-bottom: 4px;
                text-overflow: -o-ellipsis-lastline;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 1;
                line-clamp: 1;
                -webkit-box-orient: vertical;
            }
        }
    }

    .orderCenter-left {
        width: 400px;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
        flex-direction: column;
        text-align: left;
        padding: 10px 0 10px 30px;
        box-sizing: border-box;
    }

    .hotSale {
        width: 31px;
        height: 16px;
        font-size: 12px;
        border: 1px solid #dd2619;
        color: #ffffff;
        background: #dd2619;
        border-radius: 4px;
    }

    .goods {
        height: 48px;
        margin: 0 0 4px 0;
        font-size: 16px;
        color: #333333;
        font-weight: 500;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        cursor: pointer;
    }

    .orderCenter-leftPhoto {
        width: 100%;
        padding: 20px 0;
        border-bottom: 1px solid #e8e8e8;
        display: flex;
        -webkit-box-align: center;
        align-items: center;

        &:last-child {
            border-bottom: 0;
        }
    }

    .orderSecond {
        width: 60px;
        min-height: 122px;
        box-sizing: content-box;
        margin: 0 50px;
        display: flex;
        -webkit-box-align: center;
        align-items: center;
        -webkit-box-pack: center;
        justify-content: center;
        text-align: center;
    }

    .allOrder {
        font-size: 12px;
        font-weight: 400;
        color: #999999;
        cursor: pointer;

        &:hover {
            color: #dd2619;
        }
    }

    .orderRight1 {
        // width: 154px;
        min-height: 122px;
        box-sizing: content-box;
        margin-right: -1px;
        display: flex;
        -webkit-box-align: center;
        align-items: center;
        -webkit-box-pack: center;
        justify-content: center;
        text-align: center;
    }
    .whiteBorder {
        width: 58px;
        height: 58px;
        border-radius: 50%;
        background: #ffffff;
        text-align: center;
        padding: 1px 1px;
        box-shadow: 0px 0px 4px 4px rgba(0, 0, 0, 0.1);
    }
    .record {
        font-size: 14px;
        font-weight: 600;
        color: #333333;
    }
    .price {
        font-size: 18px;
        color: #dd2619;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        /deep/ span:nth-of-type(2) {
            margin: 0 -4px !important;
        }
    }
    /deep/ .ivu-col-span-20 {
        width: 963px;
    }

    /deep/ .ivu-card-body {
        // width: 948px;
        height: 100%;
        padding: 0;
        display: flex;
        flex-direction: column;
        align-content: center;
        justify-content: center;
        // height: 120px;
        // padding: 32px;
    }

    /deep/ .personal-center-base {
        width: 1215px !important;
    }
    /deep/ .person-center-right {
        width: 963px !important;
    }
    /deep/ .ivu-carousel-dots-inside {
        width: 71px;
        bottom: 339px;
        left: 738px;
    }

    /deep/ .ivu-card {
        border-radius: 0px !important;
    }

    /deep/ .person-center-right {
        width: 950px;
    }

    /deep/ .ivu-carousel-dots li.ivu-carousel-active > button {
        width: 12px;
        height: 12px;
        background: #dd2619;
        color: #ffffff;
        border-radius: 50%;
        font-size: 10px;
    }

    /deep/ .ivu-carousel-dots li button {
        width: 12px;
        height: 12px;
        background: #ebebeb;
        color: #666666;
        font-size: 10px;
        border-radius: 50%;
    }

    /deep/ .ivu-card-bordered {
        margin-top: 10px;
        border: 1px solid #f6f6f6;
    }

    /deep/ .o-freight {
        width: 210px !important;
    }

    /deep/ .o-status {
        width: 130px !important;
    }

    /deep/ .order-info .g-name {
        font-weight: 600;
    }

    /deep/ .order-info .o-content > div.o-freight span {
        font-weight: 600;
    }
    /deep/ .order-info .g-image {
        width: 68px !important;
        max-height: 68px !important;
    }
    /deep/ .order-info .o-head {
        height: 44px !important;
        line-height: 44px !important;
    }
    /deep/ .order-info .o-goods-desc .g-attribute {
        font-size: 12px !important;
    }
    /deep/ .order-info .o-head {
        p {
            font-size: 14px !important;
        }
    }
    /deep/ .order-info .o-content > div.o-freight {
        span {
            margin-bottom: 0px !important;
        }
    }
    /deep/ .ivu-btn-text {
        font-size: 12px !important;
        color: #666666 !important;
        font-family: PingFangSC-Regular, PingFang SC !important;
        &:hover {
            color: #dd2619 !important;
        }
    }
    /deep/ .order-info .o-content > div.o-status {
        font-size: 12px !important;
        font-family: PingFangSC-Regular, PingFang SC !important;
        margin-bottom: 8px;
    }
    /deep/ .ivu-carousel-dots li {
        padding: 0px !important;
    }
    /deep/ .order-info .o-content > div.o-freight em {
        margin: 0px 0 18px !important;
    }
    // 我的收藏and我的足迹数字显示
    // /deep/ .ivu-carousel-dots-inside {
    //   counter-reset: section1;
    // }
    // /deep/ .ivu-carousel-dots-inside li {
    //   font-size: 6px;
    // }
    // /deep/ .ivu-carousel-dots-inside li:after {
    //   position: relative;
    //   top: -14px;
    //   counter-increment: section1;
    //   content: counter(section1);
    // }
    /deep/ .order-info .o-content > div.o-freight i {
        margin-right: -4px;
    }
</style>
