<template>
    <div class="carIndex">
        <!-- 顶部搜索部分 -->
        <search-first-level></search-first-level>

        <!-- 面包屑 -->
        <div
            class="carBreadcrumb"
            id="carBreadcrumb"
            :class="{
                carBreadcrumb: !cartData || cartData.findCartDTO.length == 0,
            }"
        >
            <breadcrumb1 :list="breadcrumbList" />
        </div>

        <div style="width: 100%">
            <div
                style="width: 1200px; margin: auto"
                :class="{
                    carBreadcrumbs:
                        !cartData || cartData.findCartDTO.length == 0,
                }"
            >
                <div
                    v-if="
                        cartData &&
                        (cartData.findCartDTO.length != 0 ||
                            (cartData.cartVOList &&
                                cartData.cartVOList.length != 0))
                    "
                >
                    <!-- 购物车列表头部-->
                    <div
                        class="header carListTop font-12 color-333"
                        style="margin-bottom: 20px"
                    >
                        <div
                            class="width-155"
                            style="width: 144px; margin: 0 0 0 10px"
                        >
                            <Checkbox
                                :value="allChecked"
                                @on-change="
                                    handleCheckAll($event, 'chenkAll', '')
                                "
                            >
                                <span style="margin: 0 0 0 11px">全选</span>
                            </Checkbox>
                        </div>
                        <div style="width: 420px" class="width-413">
                            商品信息
                        </div>
                        <div style="width: 145px" class="width-162">单价</div>
                        <div style="width: 166px" class="width-162">数量</div>
                        <div style="width: 147px" class="width-155">小计</div>
                        <div class="width-152">操作</div>
                    </div>
                    <!-- 有效店铺列表-->
                    <div
                        class="indexInputBox"
                        v-for="(storeItem, index) in cartData.findCartDTO"
                        :key="index"
                    >
                        <template
                            v-if="
                                cartData.findCartDTO &&
                                cartData.findCartDTO.length != 0
                            "
                        >
                            <div class="content-shop">
                                <Checkbox
                                    :value="storeItem.isSelectAll"
                                    @on-change="
                                        handleCheckAll(
                                            $event,
                                            'store',
                                            storeItem.storeId
                                        )
                                    "
                                    style="margin: 0 0 0 10px"
                                ></Checkbox>
                                <!-- <span style="margin:0 0 0 6px;">店铺：</span> -->
                                <span
                                    v-if="storeItem.storeType"
                                    class="content-selfSupport"
                                    >{{ storeItem.storeType }}</span
                                >
                                <span
                                    class="content-shopName"
                                    @click.stop="toStore(storeItem.storeId)"
                                    >{{ storeItem.storeName }}</span
                                >
                            </div>
                            <!-- 商品列表-->
                            <div class="content-goodList">
                                <div
                                    style="
                                        height: 1px;
                                        background-color: #c9c9c9;
                                    "
                                ></div>
                                <div
                                    class="content-good"
                                    v-for="(activityItem,
                                    activityIndex) in storeItem.activityCartGroupDTOList"
                                    :key="activityIndex"
                                >
                                    <div
                                        class="content-hotSale"
                                        style="
                                            display: flex;
                                            align-items: center;
                                        "
                                        v-if="activityItem.activityDescription"
                                    >
                                        <div
                                            class="commenCeil"
                                            style="
                                                width: 25px;
                                                height: 16px;
                                                background: rgba(250, 72, 5, 1);
                                                border-radius: 2px;
                                                line-height: 16px;
                                            "
                                        >
                                            {{
                                                activityList[
                                                    activityItem.activityType
                                                ]
                                            }}
                                        </div>
                                        <span class="font-grey">{{
                                            activityItem.activityDescription
                                        }}</span>
                                        <!-- <span class="font-red">已减￥200.00</span>
                    <span>)</span>-->
                                        <span
                                            class="font-333;"
                                            style="
                                                cursor: pointer;
                                                color: rgba(51, 51, 51, 1);
                                            "
                                            @click.stop="
                                                strollAround(
                                                    activityItem.activityId,
                                                    activityItem.activityType
                                                )
                                            "
                                        >
                                            &nbsp;&nbsp;{{
                                                activityItem.activityOperation ===
                                                0
                                                    ? "去逛逛"
                                                    : activityItem.activityOperation ===
                                                      1
                                                    ? "已满足部分规则，去凑单"
                                                    : activityItem.activityOperation ===
                                                      2
                                                    ? "不满足任何规则，去凑单 "
                                                    : "无满减商品，去逛逛 "
                                            }}>
                                        </span>
                                    </div>

                                    <div
                                        class="goodsItem"
                                        style="
                                            width: 100%;
                                            display: flex;
                                            align-items: flex-start;
                                            padding: 18px 0 15px;
                                        "
                                        v-for="(goodsItem,
                                        goodsIndex) in activityItem.cartDTOList"
                                        :key="goodsIndex"
                                        :class="{
                                            goodsItemSelected:
                                                goodsItem.isSelect !== 0,
                                        }"
                                    >
                                        <div
                                            style="
                                                width: 28px;
                                                height: 100%;
                                                display: flex;
                                                align-items: flex-start;
                                                justify-content: center;
                                                margin-right: 8px;
                                            "
                                        >
                                            <Checkbox
                                                :value="
                                                    goodsItem.isSelect === 0
                                                        ? false
                                                        : true
                                                "
                                                @on-change="
                                                    handleCheckAll(
                                                        $event,
                                                        'goodsItem',
                                                        goodsItem.id
                                                    )
                                                "
                                                style="margin: 0 0 0 0"
                                            ></Checkbox>
                                        </div>
                                        <div
                                            @click.stop="
                                                toDetail(
                                                    goodsItem.goodsId,
                                                    goodsItem.specId
                                                )
                                            "
                                        >
                                            <img
                                                v-lazy="
                                                    $imgURL +
                                                    goodsItem.specMainPicture
                                                "
                                                style="
                                                    width: 84px;
                                                    height: 84px;
                                                    cursor: pointer;
                                                "
                                                alt
                                            />
                                        </div>
                                        <div
                                            style="
                                                margin: 0 52px 0 13px;
                                                min-height: 84px;
                                                width: 390px;
                                                display: flex;
                                                flex-direction: column;
                                            "
                                        >
                                            <div
                                                class="font-goodInfo width-257"
                                                style="
                                                    cursor: pointer;
                                                    height: 40px;
                                                "
                                                :title="goodsItem.specName"
                                                @click.stop="
                                                    toDetail(
                                                        goodsItem.goodsId,
                                                        goodsItem.specId
                                                    )
                                                "
                                            >
                                                <div
                                                    style="
                                                        width: 30px;
                                                        height: 16px;
                                                        background: #fa4805;
                                                        border-radius: 2px;
                                                        font-size: 10px;
                                                        font-weight: 400;
                                                        color: #fff;
                                                        text-align: center;
                                                        margin: 0 4px 0 0;
                                                        display: inline-block;
                                                        line-height: 16px;
                                                    "
                                                    v-if="goodsItem.labelName"
                                                >
                                                    {{ goodsItem.labelName }}
                                                </div>
                                                {{ goodsItem.specName }}
                                            </div>
                                            <div class="content-good-prop">
                                                <div
                                                    class="marginRight-18"
                                                    :title="goodsItem.specInfo"
                                                    style="height: 15px"
                                                >
                                                    <span
                                                        style="
                                                            padding-right: 18px;
                                                        "
                                                        v-for="item in stringToArray(
                                                            goodsItem.specInfo
                                                        )"
                                                        :key="item"
                                                        >{{ item }}</span
                                                    >
                                                </div>
                                                <!-- <div>{{ "容量" + ":" + "128G" }}</div> -->
                                            </div>
                                            <!-- &&(goodsItem.hour!=='00'&&goodsItem.min!=='00'&&goodsItem.sec!=='00') -->
                                            <div
                                                class="font-red marginTop-8"
                                                v-if="
                                                    (goodsItem.activityType ==
                                                        3 ||
                                                        goodsItem.activityType ==
                                                            5) &&
                                                    goodsItem.timeFlag
                                                "
                                            >
                                                <span
                                                    style="
                                                        width: 34px;
                                                        height: 20px;
                                                    "
                                                    v-if="
                                                        goodsItem.activityType ==
                                                        3
                                                    "
                                                    >【秒杀】</span
                                                >
                                                <span
                                                    style="
                                                        width: 34px;
                                                        height: 20px;
                                                    "
                                                    v-if="
                                                        goodsItem.activityType ==
                                                        5
                                                    "
                                                    >【限购】</span
                                                >
                                                <!-- <span class="commenCeil" style="width:34px;height:20px;">1212</span> -->
                                                <!--  -->
                                                还剩
                                                <span
                                                    v-show="goodsItem.day !== 0"
                                                    style="line-height: 16px"
                                                    >{{ goodsItem.days }}</span
                                                >天
                                                <span
                                                    style="line-height: 16px"
                                                    >{{ goodsItem.hour }}</span
                                                >:
                                                <span
                                                    style="line-height: 16px"
                                                    >{{ goodsItem.min }}</span
                                                >:
                                                <span
                                                    style="line-height: 16px"
                                                    >{{ goodsItem.sec }}</span
                                                >恢复原价
                                            </div>
                                        </div>
                                        <div
                                            class="content-good-price width-143"
                                        >
                                            <p
                                                style="
                                                    font-weight: 400;
                                                    color: #222222;
                                                "
                                            >
                                                ￥{{
                                                    parseFloat(
                                                        goodsItem.specSellPrice
                                                    ).toFixed(2)
                                                }}
                                            </p>
                                            <!-- v-if="goodsItem.activityNum>0" -->
                                            <div
                                                v-if="
                                                    goodsItem.activityType == 2
                                                "
                                            >
                                                <!-- <Poptip placement="bottom-start" v-model="goodsItem.visible">
                                                    <div style="cursor: pointer;" class="content-good-select" @click.stop="haha(goodsItem,activityItem.cartDTOList)">促销 ^</div>
                                                    <div class="api" slot="content">
                                                        <RadioGroup v-model="goodsItem.activityId" vertical @on-change="changeRadio">
                                                            <Radio :label="smallActivityItem.id" v-for="(smallActivityItem,smallActivityIndex) in goodsItem.frontReduceActivityPageDTOS" :key="smallActivityIndex">
                                                                <span>{{smallActivityItem.activityDescription}}</span>
                                                            </Radio>
                                                        </RadioGroup>
                                                        <div style="display:flex;justify-content:space-around;padding:5px 10px;">
                                                            <Button size="small" @click.stop="cancel(goodsItem)">取消</Button>
                                                            <Button type="error" @click.stop="ok(goodsItem)" style="" size="small">确定</Button>
                                                        </div>
                                                    </div>
                        </Poptip>-->

                                                <goodsvisible
                                                    :id="goodsItem.goodsId"
                                                    :list="
                                                        activityItem.cartDTOList
                                                    "
                                                    :datas="goodsItem"
                                                ></goodsvisible>
                                            </div>
                                            <div
                                                v-if="
                                                    goodsItem.couponsFlag == 1
                                                "
                                            >
                                                <!-- <Poptip placement="bottom-start" v-model="goodsItem.couponVisible" width="230">
                                                    <div style="cursor: pointer;width: 65px;" class="content-good-select" @click.stop="haha1(goodsItem,activityItem.cartDTOList)">优惠券 ^</div>
                                                    <div class="api" slot="content">
                                                        <div v-for="(smallcouponListItem1,smallcouponIndex1) in goodsItem.goodsDetailCouponsListDTO.canRecList" :key="'coupons'+smallcouponIndex1"
                                                            style="display:flex;align-items:center; margin:10px 0 0 0;">
                                                            <div class="content-good-coupon">￥{{smallcouponListItem1.faceValue}}</div>
                                                            <div class="font-12 " style="width:110px;margin:0 0 0 8px;">满{{smallcouponListItem1.limitAmount}}减{{smallcouponListItem1.faceValue}}</div>
                                                            <div style="cursor: pointer;" class="content-good-receive" @click="getcoupons(smallcouponListItem1)">领取</div>
                                                        </div>
                                                        <div v-for="(smallcouponListItem2,smallcouponIndex2) in goodsItem.goodsDetailCouponsListDTO.recedList" :key="'coupon'+smallcouponIndex2"
                                                            v-if="goodsItem.goodsDetailCouponsListDTO.recedList.length!=0"
                                                            style="display:flex;align-items:center;margin:10px 0 0 0;">
                                                            <div class="content-good-coupon">￥{{smallcouponListItem2.faceValue}}</div>
                                                            <div style="width:110px;margin:0 0 0 8px;">满{{smallcouponListItem2.faceValue}}减{{smallcouponListItem2.faceValue}}</div>
                                                            <div class="content-good-received">已领取</div>
                                                        </div>
                                                    </div>
                        </Poptip>-->

                                                <goodscoupon
                                                    style="margin-top: 4px"
                                                    :id="goodsItem.goodsId"
                                                    :list="
                                                        activityItem.cartDTOList
                                                    "
                                                    :datas="goodsItem"
                                                ></goodscoupon>
                                            </div>
                                        </div>
                                        <div
                                            class="width-162"
                                            style="
                                                width: 163px;
                                                position: relative;
                                                height: 50px;
                                            "
                                        >
                                            <div class="contnent-good-num">
                                                <Button
                                                    type="text"
                                                    @click="
                                                        changeGoodsNum(
                                                            goodsItem.goodsNum,
                                                            goodsItem,
                                                            'down'
                                                        )
                                                    "
                                                    class="numBtn leftBtn"
                                                    :disabled="
                                                        goodsItem.goodsNum <= 1
                                                    "
                                                >
                                                    <img :src="reduceImg" alt />
                                                </Button>
                                                <!-- 判断数额限制 -->
                                                <!-- :disabled="numGoodsId==goodsItem.goodsId" -->
                                                <InputNumber
                                                    :active-change="false"
                                                    @on-change="
                                                        changeGoodsNum(
                                                            $event,
                                                            goodsItem
                                                        )
                                                    "
                                                    :max="goodsItem.endNumber"
                                                    :min="1"
                                                    v-model="goodsItem.goodsNum"
                                                ></InputNumber>

                                                <!-- <input type="number" @on-blur="changInput(goodsItem.goodsNum,goodsItem.endNumber)" v-model="goodsItem.goodsNum" class="numInput inputs"/> -->
                                                <Button
                                                    type="text"
                                                    @click="
                                                        changeGoodsNum(
                                                            goodsItem.goodsNum,
                                                            goodsItem,
                                                            'up'
                                                        )
                                                    "
                                                    class="numBtn rightBtn"
                                                    :disabled="
                                                        goodsItem.goodsNum >=
                                                        goodsItem.endNumber
                                                    "
                                                >
                                                    <img
                                                        :src="increaseImg"
                                                        alt
                                                    />
                                                </Button>
                                            </div>
                                        </div>
                                        <div
                                            class="content-good-price width-147"
                                        >
                                            <p
                                                style="
                                                    font-weight: 600;
                                                    color: #222222;
                                                "
                                            >
                                                ￥{{
                                                    (
                                                        goodsItem.specSellPrice *
                                                        goodsItem.goodsNum
                                                    ).toFixed(2)
                                                }}
                                            </p>
                                            <!-- <p class="font-red marginTop-8">减 ￥200.00</p> -->
                                        </div>
                                        <div class="content-good-operation">
                                            <!-- <p v-if="good.isInvalid" class="font-red" style="cursor: pointer;">相似宝贝</p> -->
                                            <p
                                                style="
                                                    cursor: pointer;
                                                    color: #666;
                                                "
                                                @click="toCollect(goodsItem.id)"
                                            >
                                                移入收藏夹
                                            </p>
                                            <p
                                                style="
                                                    cursor: pointer;
                                                    color: #666;
                                                "
                                                class="marginTop-8"
                                                @click="
                                                    (deleteStatus = true),
                                                        (deleteId =
                                                            goodsItem.specId),
                                                        (deleteCartId =
                                                            goodsItem.id)
                                                "
                                            >
                                                删除
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                    <!-- 失效店铺列表-->
                    <div
                        class="indexInputBox"
                        v-if="
                            cartData.cartVOList &&
                            cartData.cartVOList.length != 0
                        "
                    >
                        <div
                            style="width: 100"
                            v-for="(invalidItem,
                            invalidIndex) in cartData.cartVOList"
                            :key="invalidIndex"
                        >
                            <div class="content-shop">
                                <!-- <span style="margin:0 0 0 18px;">店铺：</span> -->
                                <span
                                    class="content-shopName"
                                    @click.stop="toStore(invalidItem.storeId)"
                                    >{{ invalidItem.storeName }}</span
                                >
                            </div>
                            <!-- 商品列表-->
                            <div class="content-goodList">
                                <div
                                    style="
                                        height: 1px;
                                        background-color: #c9c9c9;
                                    "
                                ></div>
                                <div
                                    class="content-good"
                                    v-for="(items, indexs) in invalidItem.list"
                                    :key="indexs"
                                    style="
                                        display: flex;
                                        padding: 18px 0 15px 0;
                                    "
                                >
                                    <div
                                        style="
                                            width: 36px;
                                            height: 100%;
                                            display: flex;
                                            align-items: flex-start;
                                            justify-content: center;
                                        "
                                    >
                                        <div
                                            style="
                                                font-size: 13px;
                                                color: rgba(102, 102, 102, 1);
                                            "
                                        >
                                            失效
                                        </div>
                                    </div>
                                    <div
                                        style="cursor: pointer"
                                        @click.stop="
                                            toDetail(
                                                items.goodsId,
                                                items.specId
                                            )
                                        "
                                    >
                                        <img
                                            v-lazy="
                                                $imgURL + items.specMainPicture
                                            "
                                            style="
                                                width: 84px;
                                                height: 84px;
                                                opacity: 0.69;
                                            "
                                            alt
                                        />
                                    </div>
                                    <div
                                        style="
                                            margin: 0 52px 0 13px;
                                            width: 390px;
                                        "
                                    >
                                        <div
                                            style="cursor: pointer; color: #999"
                                            :title="items.specName"
                                            class="font-goodInfo width-257"
                                            @click.stop="
                                                toDetail(
                                                    items.goodsId,
                                                    items.specId
                                                )
                                            "
                                        >
                                            {{ items.specName }}
                                        </div>
                                        <div
                                            class="content-good-prop"
                                            style="margin-top: 5px"
                                        >
                                            <div
                                                class="marginRight-18"
                                                style="
                                                    height: 15px;
                                                    width: 260px;
                                                    color: #999999;
                                                "
                                                :title="items.specInfo"
                                            >
                                                <span
                                                    style="padding-right: 18px"
                                                    v-for="item in stringToArray(
                                                        items.specInfo
                                                    )"
                                                    :key="item"
                                                    >{{ item }}</span
                                                >
                                            </div>
                                        </div>
                                        <div
                                            style="
                                                margin-top: 5px;
                                                font-size: 12px;
                                                color: #dd2619;
                                            "
                                        >
                                            无货
                                        </div>
                                    </div>
                                    <div
                                        class="content-good-price width-143"
                                        style="color: #999999"
                                    >
                                        <p
                                            style="
                                                font-weight: 400;
                                                color: #999999;
                                            "
                                        >
                                            ￥{{
                                                parseFloat(
                                                    items.specSellPrice
                                                ).toFixed(2)
                                            }}
                                        </p>
                                    </div>
                                    <div
                                        class="width-143"
                                        style="
                                            width: 163px;
                                            position: relative;
                                            height: 50px;
                                        "
                                    >
                                        <div class="contnent-good-num">
                                            <Button
                                                type="text"
                                                class="numBtn leftBtn"
                                                style="background: #f5f5f5"
                                                :disabled="true"
                                            >
                                                <img :src="reduceImg" alt />
                                            </Button>
                                            <!-- 判断数额限制 -->
                                            <InputNumber
                                                :max="10"
                                                :min="1"
                                                :disabled="true"
                                                v-model="items.goodsNum"
                                            ></InputNumber>
                                            <!-- <input type="number" @on-blur="changInput(goodsItem.goodsNum,goodsItem.endNumber)" v-model="goodsItem.goodsNum" class="numInput inputs"/> -->
                                            <Button
                                                type="text"
                                                class="numBtn rightBtn"
                                                style="background: #f5f5f5"
                                                :disabled="true"
                                            >
                                                <img :src="increaseImg" alt />
                                            </Button>
                                        </div>
                                    </div>
                                    <div class="content-good-price width-147">
                                        <p
                                            style="
                                                font-weight: 600;
                                                color: #999999;
                                            "
                                        >
                                            ￥{{
                                                (
                                                    items.specSellPrice *
                                                    items.goodsNum
                                                ).toFixed(2)
                                            }}
                                        </p>
                                    </div>
                                    <div class="content-good-operation">
                                        <!-- <p class="font-red" style="cursor: pointer;">相似宝贝</p> -->
                                        <p
                                            style="
                                                cursor: pointer;
                                                color: #666666;
                                            "
                                            @click.stop="
                                                (deleteStatus = true),
                                                    (deleteId = items.specId),
                                                    (deleteCartId = items.id)
                                            "
                                        >
                                            删除
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 用来定位结算栏距离底部距离 -->
                    <div id="height" style="width: 100%; height: 1px"></div>

                    <!-- 结算栏-->
                    <div
                        style="margin: 30px 0 0 0; background-color: white"
                        :class="{ carCupping: carShow }"
                    >
                        <div
                            class="header font-14 color-333"
                            style="display: flex"
                        >
                            <div
                                style="
                                    display: flex;
                                    width: 850px;
                                    flex-warp: wrap;
                                "
                            >
                                <div
                                    class="width-155"
                                    style="
                                        width: 119px;
                                        margin: 0 14px 0 9px;
                                        display: flex;
                                        align-items: center;
                                        flex-warp: wrap;
                                    "
                                >
                                    <Checkbox
                                        :value="allChecked"
                                        @on-change="
                                            handleCheckAll(
                                                $event,
                                                'chenkAll',
                                                ''
                                            )
                                        "
                                        >全选</Checkbox
                                    >
                                </div>
                                <div
                                    class="width-162"
                                    style="
                                        width: 128px;
                                        cursor: pointer;
                                        display: flex;
                                        align-items: center;
                                    "
                                    @click.stop="
                                        selArr.length > 0
                                            ? ((deleteStatus = true),
                                              (deleteId = ''),
                                              (deleteCartId = ''))
                                            : $Message.warning(
                                                  '您还未选择商品！'
                                              )
                                    "
                                >
                                    <span class="hoverColor">
                                        删除选中商品
                                    </span>
                                </div>
                                <div
                                    class="width-162"
                                    style="
                                        width: 393px;
                                        cursor: pointer;
                                        display: flex;
                                        align-items: center;
                                    "
                                    @click.stop="toCollect('')"
                                >
                                    <span class="hoverColor"> 移入收藏夹 </span>
                                </div>
                                <div
                                    class="width-257 bottomInput"
                                    style="width: 220px"
                                >
                                    总共
                                    <span class="font-red">{{
                                        cartData.totalNum
                                    }}</span
                                    >件商品，已选商品
                                    <InputNumber
                                        :class="
                                            cartData.selectNum > 9 ? '' : 'aa'
                                        "
                                        v-model="cartData.selectNum"
                                        readonly
                                    ></InputNumber>
                                    <!-- <span class="font-red">{{ cartData.selectNum }}</span>  -->
                                    件
                                </div>
                            </div>

                            <div
                                style="
                                    display: flex;
                                    width: 590px;
                                    justify-content: space-between;
                                "
                            >
                                <!-- <div class="bottomInput" style="display: flex;align-items: center;">商品合计： -->
                                <!-- v-if="parseFloat(cartData.totalPrice).toFixed(2)>10000000" -->
                                <!-- ￥<span >{{parseFloat(cartData.totalPrice).toFixed(2)}}</span> -->
                                <!-- <InputNumber v-else v-model="cartData.totalPrice" readonly></InputNumber> -->
                                <!-- </div> -->

                                <div
                                    style="
                                        display: flex;
                                        flex-direction: column;
                                        justify-content: center;
                                    "
                                >
                                    <div
                                        style="
                                            display: flex;
                                            align-items: center;
                                            min-width: 200px;
                                            justify-content: flex-end;
                                        "
                                    >
                                        <p class="font-red content-totalPrice">
                                            应付总额：
                                        </p>
                                        <p class="font-red content-totalPrice">
                                            ￥{{
                                                parseFloat(
                                                    cartData.payPrice
                                                ).toFixed(2)
                                            }}
                                        </p>
                                    </div>
                                    <div
                                        v-if="
                                            parseFloat(
                                                cartData.discountAmount
                                            ) != 0
                                        "
                                        style="
                                            text-align: right;
                                            font-size: 12px;
                                            color: rgba(51, 51, 51, 1);
                                            margin: 5px 0 0 0;
                                        "
                                    >
                                        优惠：-￥{{ cartData.discountAmount }}
                                    </div>
                                </div>
                                <div
                                    :class="{
                                        haveMoney: cartData.payPrice != 0,
                                    }"
                                    class="width-152 content-Settlement"
                                    @click.stop="settlement"
                                >
                                    结算
                                </div>
                            </div>
                        </div>
                    </div>
                    <div
                        v-show="carShow"
                        style="'width: 100%;height:68px;"
                    ></div>
                </div>

                <!-- 购物车为空 -->
                <div
                    v-if="
                        !cartData ||
                        (cartData.findCartDTO.length == 0 &&
                            (!cartData.cartVOList ||
                                cartData.cartVOList.length == 0))
                    "
                    class="empty"
                    style="
                        width: 1200px;
                        margin: auto;
                        height: 300px;
                        background: #ffffff;
                    "
                >
                    <img v-lazy="'/img/04.shoppingCar/caricon.png'" />
                    <div class="empty_right">
                        <p>您的购物车还是空的~ 赶紧行动吧！</p>
                        <div class="btnGo" @click.stop="goShopping">
                            去购物>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--同类推荐-->
        <similarRecommendation></similarRecommendation>

        <!-- 收藏弹框 -->

        <Modal
            v-model="collectStatus"
            width="188"
            class="collect collectSuccess"
        >
            <div style="text-align: center">
                <img
                    src="/img/04.shoppingCar/aixin.png"
                    style="width: 36px; height: 30px"
                />
                <p style="font-size: 16px; color: #333; margin: 20px 0 0 0">
                    商品已移入收藏夹
                </p>
            </div>
            <div slot="footer" class="footerBtn"></div>
        </Modal>

        <!-- 收藏确认弹框 -->
        <Modal
            v-model="collectStatrtStatus"
            width="342"
            class="delete collectStatrtStatus"
        >
            <div
                style="
                    text-align: center;
                    width: 218px;
                    margin: 0 auto;
                    font-weight: 400;
                    color: #222;
                "
            >
                <p style="font-size: 16px; color: #333">
                    确认将商品移入收藏夹？移动后购物车将不显示该商品
                </p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn newBtn1"
                    style="
                        width: 54px;
                        height: 24px;
                        background: #f5f5f5;
                        font-size: 14px;
                        letter-spacing: 1px;
                    "
                    size="default"
                    @click.stop="
                        (collectStatrtStatus = false), (collectId = '')
                    "
                    >取消</Button
                >
                <div
                    class="newBtn newBtn2"
                    style="
                        width: 54px;
                        height: 24px;
                        margin: 0 0 0 10px;
                        background: linear-gradient(
                            90deg,
                            #dd291c 0%,
                            #ff4e02 100%
                        );
                        color: rgba(255, 255, 255, 1);
                        font-size: 14px;
                        letter-spacing: 1px;
                        consor: pointer;
                    "
                    size="default"
                    @click.stop="collectSure()"
                >
                    确定
                </div>
            </div>
        </Modal>

        <!-- 删除弹框 -->
        <Modal v-model="deleteStatus" width="342" class="delete">
            <div
                style="
                    text-align: center;
                    display: flex;
                    align-items: center;
                    flex-wrap: wrap;
                "
            >
                <img
                    src="/img/04.shoppingCar/jinggao.png"
                    style="width: 22px; height: 19px; line-height: 16px"
                />
                <p style="padding-left: 7px; font-size: 16px; color: #333">
                    确定要删除所选商品？
                </p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn newBtn1"
                    style="
                        width: 54px;
                        height: 24px;
                        background: #f5f5f5;
                        margin-right: 10px;
                    "
                    size="default"
                    @click.stop="deleteStatus = false"
                    >取消</Button
                >
                <div
                    class="newBtn"
                    style="
                        width: 54px;
                        height: 24px;
                        margin: 0 0 0 10px;
                        background: linear-gradient(
                            90deg,
                            #dd291c 0%,
                            #ff4e02 100%
                        );
                        color: rgba(255, 255, 255, 1);
                    "
                    size="default"
                    :loading="modal_loading"
                    @click.stop="delFn()"
                >
                    确定
                </div>
            </div>
        </Modal>

        <!-- 登录 -->
        <login ref="login" class="login"></login>
    </div>
</template>

<script>
    import similarRecommendation from "@/components/04.shoppingCar/similarRecommendation.vue"; //同类推荐
    import goodscoupon from "@/components/04.shoppingCar/goodscoupon.vue"; //优惠券
    import goodsvisible from "@/components/04.shoppingCar/goodsvisible.vue"; //促销活动
    import login from "@/components/common/login 2.vue";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import { CartPage, AddCar } from "@/api/api_04.shoppingCar.js";
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import Cookies from "js-cookie";
    export default {
        head() {
            return {
                title: "我的购物车",
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
                reduceImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_reduce.png"), // '+'图片
                increaseImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_increase.png"), // '-'图片
                modal_loading: false, //加载状态
                carShow: false, //结算是否吸底
                carHeight: 200, // 滚动高度
                deleteStatus: false, //是否删除
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "我的购物车",
                        toPath: "",
                    },
                ]),
                vertical: "",
                visible: false, //活动气泡
                deleteId: "", //要单个删除商品的specId
                loginFlag: false, //登录状态
                deleteCartId: "", //要单个删除商品的购物车Id
                numGoodsId: "",
                collectStatrtStatus: false,
                collectId: "",
                clientHeight: "",
                offsetTop: "",
                activityList: Object.freeze([
                    "无活动",
                    "优惠券",
                    "满减",
                    "秒杀",
                    "拼团",
                    "限购",
                ]),
            };
        },
        components: {
            similarRecommendation,
            goodscoupon,
            goodsvisible,
            login,
            breadcrumb1,
        },
        computed: {
            ...mapState("shoppingCarIndex", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "cartData",
                "allChecked", // 是否全选
                "recommendList", //商品推荐列表
                "selArr", //批量删除的specId
                "collectStatus", //收藏成功与否判断
                "numDisabled",
            ]),
        },
        async fetch({ store }) {
            await store.dispatch("shoppingCarIndex/cartPage");
        },
        updated() {
            // console.log("我更新完了");
            this.offsetTop =
                document.getElementById("height") &&
                document.getElementById("height").offsetTop;
            this.clientHeight = document.documentElement.clientHeight;
            this.carScroll();
        },
        mounted() {
            this.loginFlag = Cookies.get("auth") ? true : false;
            // 监听滑动事件
            // window.onscroll = this.carScroll;
            window.addEventListener("scroll", this.carScroll);
            this.cartPage();

            clearInterval(this.residueInteval);
            this.residueInteval = setInterval(() => {
                //计时器每秒钟执行一次
                this.handleCountDown();
            }, 1000);
        },
        destroyed() {
            window.removeEventListener("scroll", this.carScroll);
            clearInterval(this.residueInteval);
        },
        methods: {
            stringToArray(str) {
                if (str) {
                    const a = str.split(" ");
                    a.push("测试用户:最激昂和三的手机");
                    return a;
                }
                return [];
            },
            ...mapActions("shoppingCarIndex", [
                "cartPage", //购物车列表
                "addCar", //加入购物车
                "checkAall", //全选全消
                "checkItem", //单个全选全消
                "nologinDelete", //未登录删除
                "loginCartDeletes", //登录删除
                "loginCartDeletes1",
                "cartFavorites", //收藏
                "getcoupon", // 领取优惠券
            ]),

            changeRadio(e) {
                this.radioId = e;
            },
            //秒杀倒计时
            handleCountDown() {
                if (
                    this.cartData &&
                    this.cartData.findCartDTO &&
                    this.cartData.findCartDTO.length != 0
                ) {
                    this.cartData.findCartDTO.forEach((item, index) => {
                        item.activityCartGroupDTOList.forEach(
                            (activityGroup, index2) => {
                                activityGroup.cartDTOList.forEach((ii, index3) => {
                                    if (
                                        (ii.activityType == 3 ||
                                            ii.activityType == 5) &&
                                        ii.activityEndDate
                                    ) {
                                        var gapStamp =
                                            new Date(
                                                ii.activityEndDate.replace(
                                                    /-/g,
                                                    "/"
                                                )
                                            ).getTime() - new Date().getTime();
                                        if (gapStamp > 1000) {
                                            ii.days = this.computeCountDown4(
                                                gapStamp
                                            );
                                            ii.hour = this.computeCountDown1(
                                                gapStamp
                                            );
                                            ii.min = this.computeCountDown2(
                                                gapStamp
                                            );
                                            ii.sec = this.computeCountDown3(
                                                gapStamp
                                            );
                                            ii.timeFlag = true;
                                        } else {
                                            ii.timeFlag = false;
                                        }
                                    }
                                });
                            }
                        );
                    });
                    this.cartData.findCartDTO = [].concat(
                        this.cartData.findCartDTO
                    );
                }
            },
            computeCountDown1(gapTimetamp) {
                let hour = Math.floor((gapTimetamp / 1000 / 60 / 60) % 24); //时
                if (hour == 0) {
                    return "00";
                } else if (hour < 10) {
                    return "0" + hour;
                } else {
                    return hour;
                }
            },
            computeCountDown2(gapTimetamp) {
                let min = Math.floor((gapTimetamp / 1000 / 60) % 60); //分
                if (min == 0) {
                    return "00";
                } else if (min < 10) {
                    return "0" + min;
                } else {
                    return min;
                }
            },
            computeCountDown3(gapTimetamp) {
                let sec = Math.floor((gapTimetamp / 1000) % 60); //秒
                if (sec == 0) {
                    return "00";
                } else if (sec < 10) {
                    return "0" + sec;
                } else {
                    return sec;
                }
            },
            computeCountDown4(gapTimetamp) {
                let sec = Math.floor(gapTimetamp / 1000 / 60 / 60 / 24); //天
                return sec;
            },
            //更换活动
            ok(item) {
                let obj = {
                    cartId: item.id,
                    goodsNum: item.goodsNum,
                    specId: item.specId,
                    activityId: this.radioId,
                    activityType: item.activityType,
                };
                this.addCar(obj);
                this.radioId = "";
                item.visible = false;
            },
            cancel(item) {
                console.log("=======", item);
                item.visible = false;
                this.radioId = "";
                this.cartPage();
            },
            haha(item, list) {
                for (var i = 0; i < list.length; i++) {
                    list[i].visible = false;
                }
                item.visible = true;
            },
            haha1(item, list) {
                for (var i = 0; i < list.length; i++) {
                    list[i].couponVisible = false;
                }
                item.couponVisible = true;
            },
            getcoupons(item) {
                let obj = { couponId: item.id };
                this.getcoupon(obj);
                item.couponVisible = false;
            },

            // 滑动方法
            carScroll() {
                // 获取屏幕滑动的高度
                // 滑动距离底部高度 > 设定高度  吸顶
                // console.log(
                //   document.getElementById("height").offsetTop,
                //   window.pageYOffset,
                //   document.documentElement.clientHeight
                // );
                //   // "-----",
                //   document.getElementById("height").offsetTop,
                //   "window.pageYOffset + document.documentElement.clientWidth:",
                //   window.pageYOffset + document.documentElement.clientWidth
                //   // document.documentElement.clientWidth,
                //   // "==========",
                //   // document.getElementById("height") &&
                //   //   document.getElementById("height").getBoundingClientRect().top
                // );

                if (
                    // document.getElementById("height") &&
                    // document.getElementById("height").getBoundingClientRect().top > 680
                    this.offsetTop &&
                    this.offsetTop > window.pageYOffset + this.clientHeight
                ) {
                    this.carShow = true;
                } else {
                    this.carShow = false;
                }
            },
            //更改选中状态
            handleCheckAll(data, type, id) {
                //选中函数 type有三种值：chenkAll（全选），goodsItem（选中商品），store（选中商铺）
                console.log("???", data, type);
                let dataStatus = data ? 1 : 0;
                if (type != "goodsItem") {
                    let obj = {
                        state: dataStatus,
                        storeId: id,
                    };
                    // console.log('=======',obj)
                    this.checkAall(obj);
                } else {
                    let obj = {
                        state: dataStatus,
                        cartId: id,
                    };
                    this.checkItem(obj);
                }
                //通过H5的功能知道   判断是否选中是根据接口里字段判断知否选中，列表接口中的判断字段在下方
                //isSelectAll: true, //是否全选该店铺下的所有商品
                //isSelect:true,     //该商品是否选中
            },
            // 删除商品
            delFn() {
                console.log(this.loginFlag);
                this.modal_loading = true;
                setTimeout(() => {
                    this.modal_loading = false;
                    this.deleteStatus = false;
                    if (!this.loginFlag) {
                        // 未登录时的删除
                        if (this.deleteId) {
                            //单个删除
                            let obj = [this.deleteId];
                            this.nologinDelete(obj);
                        } else {
                            //批量删除
                            let obj = this.selArr;
                            this.nologinDelete(obj);
                        }
                    } else {
                        // 登录时的删除
                        if (this.deleteCartId) {
                            //单个删除
                            let obj = {
                                cartId: this.deleteCartId,
                                delType: 1,
                            };
                            this.loginCartDeletes(obj);
                        } else {
                            //批量删除
                            let obj = {
                                delType: 2,
                            };
                            this.loginCartDeletes1(obj);
                        }
                    }
                }, 2000);
            },

            collectSure() {
                if (this.collectId) {
                    this.collectStatrtStatus = false;
                    let obj = { cartId: this.collectId };
                    this.cartFavorites(obj);
                } else {
                    this.collectStatrtStatus = false;
                    let obj = { cartId: "" };
                    this.cartFavorites(obj);
                }
            },
            //收藏
            toCollect(id) {
                if (id) {
                    this.collectId = id;
                    this.collectStatrtStatus = true;
                } else {
                    if (this.selArr.length > 0) {
                        this.collectId = id;
                        this.collectStatrtStatus = true;
                    } else {
                        this.$Message.warning("您还未选择商品！");
                    }
                }
            },
            //改变数值($event,goodsItem.specId,goodsItem.id)
            changeGoodsNum(e, item, type) {
                console.log("-----", e);
                if (!this.numDisabled) {
                    let num;
                    if (type) {
                        if (type == "down") {
                            num = e - 1;
                        } else {
                            num = e + 1;
                        }
                    } else {
                        num = e ? e : 1;
                    }
                    this.numGoodsId = item.goodsId;
                    let obj = {
                        cartId: item.id,
                        goodsNum: num,
                        specId: item.specId,
                        activityId: item.activityId,
                        activityType: item.activityType,
                    };
                    // AddCar(obj).then(res=>{
                    //     console.log('加入结果',res)
                    // })
                    this.addCar(obj);
                }
            },
            changInput(num, maxNum) {
                if (num > maxNum) {
                    num = maxNum;
                }
                if (num < 1) {
                    num = maxNum;
                }
            },

            // 去逛逛
            strollAround(id, type) {
                let val;
                if (type == 0) {
                    val = "wu";
                } else if (type == 1) {
                    val = "coupon";
                } else if (type == 2) {
                    val = "manjian";
                } else if (type == 3) {
                    val = "miaosha";
                } else {
                    val = "pingtuan";
                }
                this.$router.push({
                    path: "/searchGoodsResult",
                    query: {
                        activityId: id,
                        collectBillType: 3,
                        activityType: val,
                    },
                });
            },
            //去商品详情
            toDetail(id, specId) {
                this.$router.push({
                    path: "/goodsDetails",
                    query: { goodsId: id, specId: specId },
                });
            },
            // 去店铺
            toStore(id) {
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: { storeId: id },
                });
                window.open(routeUrl.href, "_blank");
            },
            goShopping() {
                this.$router.push({
                    path: "/searchGoodsResult",
                    query: { keyWord: "" },
                });
            },
            settlement() {
                if (!Cookies.get("auth")) {
                    // this.$Message.warning('您还还未登录！')
                    this.$refs.login.modal1 = true;
                    return false;
                }
                if (this.selArr.length == 0) {
                    this.$Message.warning("您还未选择商品！");
                    return false;
                }
                this.$router.push({
                    path: "/fillOrder",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";
    .pp {
        position: fixed;
        width: 100%;
        height: 68px;
        top: 200px;
        left: 0;
        z-index: 20;
        color: red;
        background: fuchsia;
    }
    /deep/ .ivu-input-number-handler-wrap {
        display: none;
    }
    .contnent-good-num {
        position: relative;
        border: 1px solid rgba(240, 240, 240, 1);
        height: 24px;
        width: 84px;
        border-radius: 4px;
        overflow: hidden;
    }
    .indexInputBox {
        /deep/ .ivu-input-number {
            position: absolute;
            left: 26px;
            border: none;
            border-radius: 0;
            width: 31px;
            height: 22px;
            line-height: 22px;
            &:hover {
                border-color: #dcdee2;
            }
            .ivu-input-number-input-wrap {
                height: 100%;
                line-height: 22px;
                .ivu-input-number-input {
                    height: 100%;
                    text-align: center;
                    background-color: #fff;
                    color: #222222;
                    font-weight: 400;
                    font-family: PingFangSC-Regular, PingFang SC;
                }
            }
        }
    }
    .bottomInput {
        display: flex;
        align-items: center;
        /deep/ .ivu-input-number {
            border: none;
            width: 16px;
            .ivu-input-number-input {
                padding: 0;
                color: #333;
                background: #fafafa;
                font-size: 12px;
                color: #dd2619;
            }
        }
        /deep/ .aa {
            width: 8px;
        }
        /deep/ .ivu-input-number-focused {
            box-shadow: none;
        }
    }

    /deep/ .checkCell {
        span:last-child {
            display: none;
        }
    }
    /deep/ .ivu-checkbox-focus {
        box-shadow: none;
    }
    .goodsItem {
        border-bottom: 1px solid #c9c9c9;
        &:last-child {
            border: none;
        }
    }
    .numBtn {
        width: 26px;
        height: 22px;
        padding: 0;
        border-radius: 0;
        background: #f5f5f5;
        &:hover {
            background: #f5f5f5;
        }
        /deep/ span {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            img {
                width: 15px;
            }
        }
        &.leftBtn {
            position: absolute;
            top: 0;
            left: 0px;
        }
        &.rightBtn {
            position: absolute;
            top: 0;
            left: 56px;
        }
    }
    .numInput {
        width: 50px;
        border: none;
        text-align: center;
    }
    .inputs {
        border-bottom: 1px solid #f0f0f0;
        border-top: 1px solid #f0f0f0;
    }
    .newBtn {
        width: 69px;
        height: 34px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 2px;
        &:hover {
            color: #333333;
        }
    }
    .ivu-modal-footer .footerBtn .newBtn1 {
        margin-right: 10px;
    }

    .newBtn1:hover {
        border-color: #dcdee2;
    }
    .newBtn2:hover {
        background-color: blue;
    }

    // 更改顶部搜索样式
    /deep/ .search-wrap {
        justify-content: space-between;
        .search-con {
            width: 560px;
            flex-grow: inherit !important;
            .search-con-search-bottom {
                display: none;
            }
        }
        .artCart {
            display: none;
        }
    }

    // 结算浮动样式
    .carCupping {
        position: fixed;
        width: 100%;
        height: 50px;
        left: 0;
        bottom: 0;
        z-index: 20;
        display: flex;
        justify-content: center;
        box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.6);
        /deep/ .ivu-checkbox {
            margin-right: 10px;
        }
        .haveMoney {
            height: 50px !important;
            line-height: 50px !important;
        }
        .content-Settlement {
            height: 50px !important;
        }
    }
    .carIndex .carCupping .header {
        border: none;
        height: 50px;
    }
    /deep/ .ivu-modal-footer {
        border: none;
        .footerBtn {
            display: flex;
            justify-content: center;
            padding: 0 0 30px 0;
        }
    }
    /deep/ .ivu-modal {
        border: 4px solid rgba(0, 0, 0, 0.15);
        border-radius: 6px;
    }
    .delete {
        /deep/ .ivu-modal-content {
            border-radius: 0;
        }
        /deep/ .ivu-modal-body {
            padding: 49px 73px 0px 70px;
        }
        /deep/ .ivu-modal-footer {
            border: none;
            height: 90px;
            padding: 32px 0 31px 0;
            .footerBtn {
                display: flex;
                justify-content: center;
                padding: 0;
                button {
                    font-size: 14px;
                }
                div {
                    cursor: pointer;
                }
            }
        }
        /deep/ .ivu-modal-close {
            right: 10px;
            top: 10px;
        }
    }
    .collect {
        /deep/ .ivu-modal-close {
            display: none;
        }
        /deep/ .ivu-modal-body {
            padding-top: 30px;
        }
        /deep/ .ivu-modal-footer {
            padding: 0;
        }
        /deep/ .ivu-modal {
            height: 132px;
        }
        /deep/ .ivu-modal-content {
            border-radius: 6px;
        }
    }

    .empty {
        display: flex;
        align-items: center;
        justify-content: center;
        vertical-align: bottom;
        img {
            width: 80px;
        }
        .empty_right {
            margin-left: 24px;
            height: 80px;
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            p {
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #222222;
                line-height: 16px;
            }
            .btnGo {
                width: 66px;
                height: 28px;
                background: #dd2619;
                border-radius: 4px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 28px;
                text-align: center;
                cursor: pointer;
            }
        }
    }
    #carBreadcrumb {
        width: 100% !important;
        background: #f6f6f6;
    }
    .carBreadcrumbs {
        width: 100% !important;
        display: flex;
        justify-content: center;
    }
    .commenCeil {
        font-size: 10px;
        font-weight: 400;
        color: rgba(255, 255, 255, 1);
        text-align: center;
        margin: 0 10px 0 0;
        line-height: 20px;
    }

    .carIndex {
        .font-family {
            font-family: PingFangSC-Regular, PingFang SC;
        }
        .font-weight {
            font-weight: 400;
        }
        .font-12 {
            font-size: 12px;
            /deep/ .ivu-checkbox-wrapper {
                font-size: 12px !important;
            }
        }
        .font-13 {
            font-size: 13px;
        }
        .font-14 {
            font-size: 14px;
        }
        .color-333 {
            color: #333;
        }
        .width-155 {
            width: 155px;
        }
        .width-413 {
            width: 413px;
        }
        .width-143 {
            width: 143px;
        }
        .width-147 {
            width: 147px;
        }
        .width-162 {
            width: 162px;
        }
        .width-152 {
            width: 152px;
        }
        .width-257 {
            width: 257px;
        }
        .marginRight-18 {
            margin: 0 18px 0 0;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
        .marginTop-8 {
            margin: 8px 0 0 0;
        }
        .font-grey {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #bebebe;
            line-height: 12px;
        }
        .font-goodInfo {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(34, 34, 34, 1);
            line-height: 20px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2; //（行数）
            -webkit-box-orient: vertical;
        }
        .font-red {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(221, 38, 25, 1);
            line-height: 12px;
        }
        .header {
            width: 1199px;
            height: 50px;
            background: #fff;
            border: 1px solid rgba(204, 204, 204, 1);
            display: flex;
            align-items: center;
            .content-Settlement {
                height: 50px;
                line-height: 50px;
                font-size: 20px;
            }
            .hoverColor:hover {
                color: #dd2619;
            }
        }
        .carListTop {
            background-color: rgba(238, 238, 238, 1);
            border: 0px;
        }
        .content-goodList {
            width: 1199px;
            border: 1px solid rgba(246, 246, 246, 1);
            border-bottom: none;
            //  border-top: 1px solid rgba(201, 201, 201, 1);
        }
        .content-hotSale {
            height: 41px;
            line-height: 41px;
            padding: 0 0 0 20px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            border-bottom: 1px solid rgba(246, 246, 246, 1);
            // border-top: 1px solid rgba(201, 201, 201, 1);
            // color: red;
        }
        .content-shopName {
            margin: 0 0 0 6px;
            cursor: pointer;
            color: #222;
            font-weight: 600;
            font-family: PingFangSC-Regular, PingFang SC;
        }
        .content-shopName:hover {
            color: #dd2619;
        }
        .content-totalPrice {
            font-size: 14px;
            font-weight: bold;
            color: rgba(223, 57, 45, 1);
        }
        .content-shop {
            margin: 0 0 0 24px;
            width: 1199px;
            height: 38px;
            background: rgba(255, 255, 255, 1);
            margin: 20px 0 0 0;
            line-height: 38px;
            display: flex;
            align-items: center;
        }
        .content-selfSupport {
            width: 30px;
            height: 17px;
            line-height: 17px;
            text-align: center;
            background: linear-gradient(
                90deg,
                rgba(221, 41, 28, 1) 0%,
                rgba(255, 78, 2, 1) 100%
            );
            border-radius: 2px;
            color: #ffffff;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(255, 255, 255, 1);
            margin: 0 0 2px 5px;
        }
        .content-good {
            width: 1197px;
            // padding: 24px 0 0 0;
            // background: #f9f9f9;
            // display: flex;
            // align-items: flex-start;
            border: 1px solid rgba(246, 246, 246, 1);
        }
        .content-Settlement {
            width: 120px;
            height: 68px;
            line-height: 68px;
            text-align: center;
            background: rgba(176, 176, 176, 1);
            font-size: 22px;
            font-weight: 500;
            color: rgba(255, 255, 255, 1);
        }
        .content-Settlement:hover {
            cursor: pointer;
        }
        .haveMoney {
            background: #dd2619;
            color: rgba(255, 255, 255, 1);
        }
        .content-good-price {
            font-size: 15px;
            font-family: PingFangSC-Regular, PingFang SC;
            color: rgba(51, 51, 51, 1);
            line-height: 21px;
        }
        .content-good-prop {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            margin: 10px 0 0 0;
            line-height: 12px;
            display: flex;
            flex-wrap: wrap;
        }
        .content-good-operation {
            height: 18px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);
            line-height: 18px;
            p:hover {
                color: rgba(221, 38, 25, 1);
            }
        }
        .content-good-coupon {
            width: 38px;
            height: 16px;
            // line-height: 16px;
            border-radius: 2px;
            border: 1px solid rgba(221, 38, 25, 1);
            // text-align: center;
            font-size: 10px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(221, 38, 25, 1);
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .content-good-received {
            width: 29px;
            height: 11px;
            font-size: 8px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            line-height: 11px;
            letter-spacing: 1px;
        }
        .content-good-receive {
            width: 26px;
            height: 14px;
            background: rgba(221, 38, 25, 1);
            border-radius: 2px;
            border: 1px solid rgba(221, 38, 25, 1);
            font-size: 8px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(255, 255, 255, 1);
            line-height: 14px;
            text-align: center;
            letter-spacing: 1px;
            cursor: pointer;
        }
        .content-good-select {
            width: 52px;
            height: 18px;
            // line-height: 18px;
            border-radius: 2px;
            border: 1px solid rgba(221, 38, 25, 1);
            // text-align: center;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(221, 38, 25, 1);
            display: flex;
            justify-content: center;
            align-items: center;
            // line-height: 12px;
        }
        /deep/.ivu-select-small {
            color: red;
        }
        .ivu-select-small.ivu-select-single
            .ivu-select-selection
            .ivu-select-placeholder {
            color: red;
        }
        /deep/ .similarRecommendation-con {
            margin: 50px 0 0 0;
            background-color: white;
            .title {
                font-size: 26px;
                height: 26px;
                line-height: 0;
                padding: 38px 0 1px 0;
            }
        }
        /deep/ #carBreadcrumb {
            background-color: #fff;
        }
        /deep/ .ivu-checkbox-inner {
            width: 12px;
            height: 12px;
            border: 1px solid #cccccc;
            &:after {
                top: 1px;
                left: 4px;
            }
        }
        /deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
            border: none;
        }
    }
    // /deep/ footer .service-slogon-body {
    //     // border-top: 1px solid #E9E7E7;
    // }
    /deep/ .ivu-modal-wrap {
        z-index: 9999 !important;
    }
    /deep/ .ivu-modal-mask {
        z-index: 9999 !important;
    }
    .goodsItemSelected {
        background: #fef9f9;
    }
    // /deep/ .ivu-carousel-item {
    //     padding-bottom: 10px;
    //     .good {
    //         &:hover {
    //             border: none;
    //             box-shadow: 0px 0px 4px 0px rgba(0,0,0,0.1);
    //         }
    //     }
    // }
    // .similarRecommendation-con {
    //     background-color:red !important;
    //     height:470px !important;
    //     overflow: hidden !important;
    // }
    .newBtn {
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
    }
</style>
