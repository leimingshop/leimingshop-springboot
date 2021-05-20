<template>
    <div class="fillOrder">
        <!-- 顶部搜索部分 -->
        <search-first-level></search-first-level>

        <!-- 面包屑 -->
        <div style="width: 100%; background: rgb(246, 246, 246)">
            <breadcrumb1 :list="breadcrumbList" />
        </div>

        <!-- 主要内容 -->
        <div class="main">
            <div class="container">
                <!-- 选择地址 -->
                <div v-show="settlementForm.virtualFlag !== 1" class="adress">
                    <div class="itemTitle">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                                height: 14px;
                            "
                        >
                            收货人信息
                        </div>
                        <div
                            v-if="adressList.length < 20"
                            class="changeWords"
                            style="
                                font-size: 13px;
                                font-weight: bold;
                                color: rgba(0, 122, 255, 1);
                                line-height: 13px;
                                cursor: pointer;
                            "
                            @click="addOrUpdata('')"
                        >
                            ＋添加收货地址
                        </div>
                    </div>
                    <div
                        class="defaultAdress"
                        v-if="settlementForm.memberAddress"
                    >
                        <div
                            class="cellWords adress_name"
                            :title="settlementForm.memberAddress.trueName"
                            style="justify-content: center"
                        >
                            {{ settlementForm.memberAddress.trueName }}
                            <img :src="'/img/04.shoppingCar/coupon3.svg'" />
                        </div>
                        <div
                            class="cellWords adress_name1"
                            :title="settlementForm.memberAddress.trueName"
                        >
                            {{ settlementForm.memberAddress.trueName }}
                        </div>
                        <div class="cellWords adress_news">
                            {{ settlementForm.memberAddress.areaInfo
                            }}{{ settlementForm.memberAddress.address }}
                        </div>
                        <div class="cellWords adress_phone">
                            {{ settlementForm.memberAddress.mobPhone }}
                        </div>
                        <div
                            class="cellWords btnCell"
                            style="margin: 0 20px; justify-content: center"
                            v-if="settlementForm.memberAddress.defaultFlag == 1"
                        >
                            默认地址
                        </div>
                        <div
                            style="
                                color: #dd2619;
                                margin: 0 20px;
                                cursor: pointer;
                            "
                            class="cellWords"
                            v-else
                            @click="
                                setDefultAdress(
                                    settlementForm.memberAddress,
                                    'set'
                                )
                            "
                        >
                            设为默认地址
                        </div>
                        <div
                            style="
                                color: #dd2619;
                                cursor: pointer;
                                margin: 0 0 0 20px;
                            "
                            class="cellWords"
                            @click="
                                (zanshiStatus = true),
                                    addOrUpdata(settlementForm.memberAddress)
                            "
                        >
                            修改地址
                        </div>
                    </div>
                    <template v-if="adressDefaultFlag">
                        <div
                            v-if="addressListShow"
                            slot="content"
                            style="max-height: 195px"
                            :class="{ otherScroll: adressList.length > 6 }"
                        >
                            <div
                                style="cursor: pointer"
                                v-for="(item, index) in adressList"
                                :key="index"
                                @click="checkAdress(item, index)"
                                @mouseenter="enter(index)"
                                @mouseleave="leave(index)"
                            >
                                <template
                                    v-if="
                                        !settlementForm.memberAddress ||
                                        item.id !=
                                            settlementForm.memberAddress.id
                                    "
                                >
                                    <div
                                        class="defaultAdress defaultAdressItem"
                                    >
                                        <div
                                            class="cellWords adress_name"
                                            :title="item.trueName"
                                            style="
                                                justify-content: center;
                                                border: 1px solid
                                                    rgba(204, 204, 204, 1);
                                            "
                                        >
                                            {{ item.trueName }}
                                        </div>
                                        <div class="adressMain">
                                            <div
                                                class="adressMain_div addressMainInfo"
                                            >
                                                <div
                                                    class="cellWords adress_name1"
                                                    :title="item.trueName"
                                                >
                                                    {{ item.trueName }}
                                                </div>
                                                <div
                                                    class="cellWords adress_news"
                                                >
                                                    {{ item.areaInfo
                                                    }}{{ item.address }}
                                                </div>
                                                <div
                                                    class="cellWords adress_phone"
                                                >
                                                    {{ item.mobPhone }}
                                                </div>
                                            </div>
                                            <div class="adressMain_div">
                                                <div
                                                    class="cellWords btnCell"
                                                    style="
                                                        margin: 0 33px;
                                                        justify-content: center;
                                                    "
                                                    v-if="item.defaultFlag == 1"
                                                >
                                                    默认地址
                                                </div>
                                                <div
                                                    style="
                                                        color: #dd2619;
                                                        margin: 0 20px 0 28px;
                                                        cursor: pointer;
                                                    "
                                                    class="cellWords"
                                                    @click.stop="
                                                        setDefultAdress(item)
                                                    "
                                                    v-if="
                                                        item.showStatus &&
                                                        item.defaultFlag != 1
                                                    "
                                                >
                                                    设为默认地址
                                                </div>
                                                <!-- v-if="item.showStatus&&item.defaultFlag!=1" -->
                                                <div
                                                    style="
                                                        color: #dd2619;
                                                        cursor: pointer;
                                                        margin-left: 20px;
                                                    "
                                                    class="cellWords"
                                                    @click.stop="
                                                        addOrUpdata(item)
                                                    "
                                                    v-if="item.showStatus"
                                                >
                                                    修改地址
                                                </div>
                                                <!-- v-if="item.showStatus" -->
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </div>
                        </div>
                        <div
                            class="changeAddressList"
                            v-if="!addressListShow"
                            @click.stop="offAddressList"
                        >
                            切换地址
                            <img
                                :src="'/img/04.shoppingCar/openAddressList.png'"
                            />
                        </div>
                        <div
                            class="changeAddressList"
                            v-if="addressListShow"
                            @click.stop="offAddressList"
                        >
                            收起地址
                            <img
                                :src="'/img/04.shoppingCar/offAddressList.png'"
                            />
                        </div>
                    </template>
                </div>
                <div
                    v-show="settlementForm.virtualFlag === 1"
                    class="virtualAddress"
                >
                    <div class="itemTitle">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                            "
                        >
                            虚拟商品收货信息
                        </div>
                    </div>
                    <div>
                        <Form
                            ref="virtualAddressData"
                            :model="virtualAddressData"
                            :rules="virtualAddressRule"
                            :label-width="120"
                        >
                            <FormItem
                                label="收货人姓名"
                                class="ivu-form-item-required"
                                prop="memberName"
                            >
                                <Input
                                    v-model="virtualAddressData.memberName"
                                    placeholder="请填写收货人姓名"
                                    style="width: 300px"
                                />
                            </FormItem>
                            <FormItem
                                label="收货人手机号"
                                class="ivu-form-item-required"
                                prop="memberMobile"
                            >
                                <Input
                                    v-model="virtualAddressData.memberMobile"
                                    placeholder="请填写收货人手机号"
                                    style="width: 300px"
                                />
                            </FormItem>
                        </Form>
                    </div>
                </div>
                <!-- 配送时间 -->
                <!-- 暂无该功能进行注释 -->
                <!-- <div style="padding:26px 0 40px 0;border-bottom: 1px solid #dcdee2;">
                    <div class="itemTitle">
                        <div style="font-size:16px;font-weight:bold;color:rgba(51,51,51,1);line-height: 24px;">配送时间
                        </div>
                    </div>

                    <div class="giveTimeList">
                        <div v-for="(item,index) in giveTimeChose" :key="index" @click="changeChoseIndex('giveTimeActiveIndex',index)" class="giveTimeItem" :class="{active: index === giveTimeActiveIndex}">{{item}}
                            <img :src="'/static/img/04.shoppingCar/coupon3.png'">
                        </div>
                    </div>

        </div>-->
                <!-- 支付方式 -->
                <div class="payCon">
                    <div class="itemTitle">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                            "
                        >
                            支付方式
                        </div>
                    </div>

                    <div class="payList">
                        <!-- 暂无该功能，删除现在使用代码，注释代码生效即可 -->
                        <div class="payItem active">
                            在线支付
                            <img :src="'/img/04.shoppingCar/coupon3.svg'" />
                        </div>
                        <!-- <div class="payItem" v-for="(item,index) in payChose" :key="index" @click="changeChoseIndex('payActiveIndex',index)" :class="{active: index === payActiveIndex}">{{item}}
                            <img :src="'/static/img/04.shoppingCar/coupon3.png'">
                        </div>
            <span class="payItemRemarks">货到付款需付5元手续费，仅支持2000元以下订单</span>-->
                    </div>
                </div>
                <!-- 多店铺列表 -->
                <div
                    style="padding: 26px 0 0 0"
                    v-for="(storeItem,
                    storeIndex) in settlementForm.cartToOrderList"
                    :key="storeIndex"
                >
                    <div class="itemTitle carttoOrderTitle">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(34, 34, 34, 1);
                                line-height: 14px;
                            "
                        >
                            店铺：{{ storeItem.storeName }}
                        </div>
                    </div>

                    <div>
                        <Table
                            class="cartGoodsListTable"
                            :columns="columns1"
                            :data="storeItem.cartGoodsDTOList"
                        >
                            <template slot-scope="{ row }" slot="specName">
                                <div class="goodsNew">
                                    <img
                                        style="cursor: pointer"
                                        @click.stop="
                                            toDetail(row.goodsId, row.specId)
                                        "
                                        v-lazy="$imgURL + row.specMainPicture"
                                    />
                                    <div
                                        class="goodsNew_main"
                                        @click.stop="
                                            toDetail(row.goodsId, row.specId)
                                        "
                                    >
                                        <div
                                            style="cursor: pointer"
                                            class="goodsNew_words"
                                        >
                                            {{ row.specName }}
                                        </div>
                                        <div class="norms">
                                            <div
                                                style="
                                                    margin: 0 18px 0 0;
                                                    cursor: pointer;
                                                    text-align: left;
                                                    overflow: hidden;
                                                    white-space: nowrap;
                                                    text-overflow: ellipsis;
                                                "
                                            >
                                                <span
                                                    style="padding-right: 18px"
                                                    v-for="item in stringToArray(
                                                        row.specInfo
                                                    )"
                                                    :key="item"
                                                    >{{ item }}</span
                                                >
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </template>
                            <template slot-scope="{ row }" slot="specPrice">
                                <p class="goodsWords">
                                    ￥{{
                                        parseFloat(row.specSellPrice).toFixed(2)
                                    }}
                                </p>
                            </template>
                            <template slot-scope="{ row }" slot="goodsNum">
                                <p class="goodsWords">x{{ row.goodsNum }}</p>
                            </template>
                            <template slot-scope="{ row }" slot="smallMoney">
                                <p class="goodsWords">
                                    ￥{{
                                        (
                                            parseFloat(row.specSellPrice) *
                                            row.goodsNum
                                        ).toFixed(2)
                                    }}
                                </p>
                            </template>
                            <!-- <template slot-scope="{ row }" slot="allmoney">
                                <p style="color:rgba(221,38,25,1);" class="goodsWords">￥{{ parseFloat(row.specPrice)*row.goodsNum }}</p>
              </template>-->
                        </Table>
                    </div>

                    <div
                        class="itemTitle"
                        style="margin: 26px 0 0 0"
                        v-if="
                            storeItem.canList && storeItem.canList.length != 0
                        "
                    >
                        <div
                            style="
                                font-size: 16px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 24px;
                            "
                        >
                            优惠券
                        </div>
                    </div>
                    <div
                        class="coupon"
                        style="
                            padding: 26px 0 40px 0;
                            border-bottom: 1px solid rgba(230, 230, 230, 1);
                        "
                        v-if="
                            storeItem.canList && storeItem.canList.length != 0
                        "
                    >
                        <div
                            v-if="!storeItem.selectItem"
                            class="couponBtn"
                            @click="
                                (couponStatus = true),
                                    (couponIndex = storeIndex)
                            "
                        >
                            选择优惠劵
                        </div>
                        <div
                            v-else
                            style="color: #dd2619"
                            class="couponBtn"
                            @click="
                                (couponStatus = true),
                                    (couponIndex = storeIndex)
                            "
                        >
                            满{{ storeItem.selectItem.limitAmount }}减{{
                                storeItem.selectItem.faceValue
                            }}
                        </div>

                        <div>
                            共
                            <span
                                style="
                                    font-size: 16px;
                                    font-weight: bold;
                                    color: rgba(221, 38, 25, 1);
                                "
                                >{{
                                    storeItem.canList.length +
                                    storeItem.canntList.length
                                }}</span
                            >张优惠劵，其中
                            <span
                                style="
                                    font-size: 16px;
                                    font-weight: bold;
                                    color: rgba(221, 38, 25, 1);
                                "
                                >{{ storeItem.canList.length }}</span
                            >张可用
                            <!-- <span @click="couponStatus = true,couponIndex = storeIndex" style="font-size:14px;font-weight:400;color:rgba(221,38,25,1);cursor: pointer;margin-left:20px;">兑换优惠劵></span> -->
                        </div>
                    </div>

                    <div class="itemTitle" style="margin: 26px 0 0 0">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                            "
                        >
                            发票信息
                        </div>
                    </div>
                    <div
                        style="
                            padding: 20px 0 17px 0;
                            border-bottom: 1px solid rgba(230, 230, 230, 1);
                            color: rgba(51, 51, 51, 1);
                        "
                        class="newinvoiceDiv-con"
                    >
                        <div class="newinvoiceDiv ww">
                            <Checkbox
                                v-if="
                                    invoiceStatusList[storeIndex]
                                        .invoiceShowStatus
                                "
                                v-model="
                                    invoiceStatusList[storeIndex].checkStatus
                                "
                                class="invoiceCheckBox"
                                @on-change="
                                    receiptSet($event, storeIndex, storeItem)
                                "
                                >我需要开发票</Checkbox
                            >
                            <div
                                style="width: 965px"
                                class="newinvoiceDiv aa"
                                v-for="(i, d) in otherlist"
                                :key="d"
                            >
                                <template
                                    v-if="
                                        i.storeId == storeItem.storeId &&
                                        otherlist[storeIndex].invoiceShowStatus
                                    "
                                >
                                    <div class="maindiv">
                                        <div>
                                            发票类型：
                                            <span
                                                style="
                                                    color: #333333;
                                                    margin-right: 58px;
                                                "
                                            >
                                                {{
                                                    i.memberInvoiceType === 1
                                                        ? "电子发票"
                                                        : i.memberInvoiceType ===
                                                          2
                                                        ? "普通发票"
                                                        : "增值税专项发票"
                                                }}
                                            </span>
                                        </div>
                                        <div class="companyTypeCell">
                                            发票抬头：
                                            <span
                                                style="
                                                    color: #333333;
                                                    margin-right: 58px;
                                                "
                                                :title="
                                                    i.companyType == 2
                                                        ? i.company
                                                        : ''
                                                "
                                                >{{
                                                    i.companyType == 2
                                                        ? i.company
                                                        : "个人"
                                                }}</span
                                            >
                                        </div>
                                        <div>
                                            内容：
                                            <span
                                                style="
                                                    color: #333333;
                                                    margin-right: 58px;
                                                "
                                                >{{
                                                    i.memberInvoiceContent == 1
                                                        ? "商品明细"
                                                        : "商品类别"
                                                }}</span
                                            >
                                        </div>
                                        <div>
                                            发票金额：
                                            <span
                                                style="
                                                    color: #333333;
                                                    margin-right: 58px;
                                                "
                                                >￥{{
                                                    storeItem.orderPrice
                                                }}</span
                                            >
                                        </div>
                                    </div>
                                    <div
                                        class="updataInvoice"
                                        @click="
                                            toUpdataInvoice(
                                                i,
                                                storeIndex,
                                                storeItem
                                            )
                                        "
                                    >
                                        修改信息
                                    </div>
                                </template>
                            </div>
                        </div>

                        <Checkbox
                            disabled
                            v-if="
                                !invoiceStatusList[storeIndex].invoiceShowStatus
                            "
                            v-model="invoiceStatusList[storeIndex].checkStatus"
                            >我需要开发票</Checkbox
                        >
                        <span
                            style="color: #dd2619"
                            v-if="
                                !invoiceStatusList[storeIndex]
                                    .invoiceShowStatus &&
                                !invoiceStatusList[storeIndex].wordsState
                            "
                            >该订单存在无法开票商品</span
                        >
                        <span
                            style="color: #dd2619"
                            v-if="
                                !invoiceStatusList[storeIndex]
                                    .invoiceShowStatus &&
                                invoiceStatusList[storeIndex].wordsState
                            "
                            >该订单商品可选发票类型不同，如需开票，请分别下单</span
                        >

                        <!-- <div v-if="!invoiceStatusList[storeIndex].invoiceShowStatus" style="display:flex;align-items:center;cursor: pointer;" @click="$Message.info('不能开发票')"><span style="width:16px;height:16px;border: 1px solid #dcdee2;margin-right:4px;"></span> <span>我需要开发票</span></div> -->
                    </div>

                    <div class="itemTitle" style="margin: 26px 0 0 0">
                        <div
                            style="
                                font-size: 14px;
                                font-weight: 600;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                            "
                        >
                            留言
                        </div>
                    </div>
                    <div style="padding: 18px 0 0 0">
                        <Input
                            v-model="messageList[storeIndex].message"
                            @on-blur="inputBlue(storeItem.storeId, storeIndex)"
                            maxlength="20"
                            show-word-limit
                            type="textarea"
                            placeholder="请输入留言"
                            style="width: 440px; font-size: 12px"
                            class="textarea-14 focusShodowNone text-area-color"
                        />
                    </div>
                </div>

                <!-- 金额明细 -->
                <div style="padding: 37px 0 0 0">
                    <div
                        class="itemTitle"
                        style="
                            margin: 0;
                            border-bottom: 1px solid #dcdee2;
                            padding: 0 0 25px 0;
                            height: 30px;
                        "
                    >
                        <div
                            style="
                                font-size: 14px;
                                font-weight: bold;
                                color: rgba(51, 51, 51, 1);
                                line-height: 14px;
                            "
                        >
                            金额明细
                        </div>
                    </div>

                    <div class="amount">
                        <div class="divFloat">
                            <span>商品件数：</span>
                            <div class="keyWord">
                                {{ settlementForm.totalNum }}件
                            </div>
                        </div>
                        <div class="divFloat">
                            <span>商品总价：</span>
                            <div class="keyWord">
                                ￥{{
                                    (
                                        parseFloat(
                                            settlementForm.orderPrice
                                        ).toFixed(2) -
                                        parseFloat(
                                            settlementForm.goodsTotalFreight
                                        ).toFixed(2)
                                    ).toFixed(2)
                                }}
                            </div>
                        </div>
                        <div class="divFloat">
                            <span>活动优惠：</span>
                            <div class="keyWord">
                                -￥{{
                                    settlementForm.activityPrice
                                        ? settlementForm.activityPrice
                                        : 0
                                }}
                            </div>
                        </div>
                        <div class="divFloat">
                            <span>优惠劵抵扣：</span>
                            <div class="keyWord">
                                -￥{{
                                    settlementForm.couponsPrice
                                        ? settlementForm.couponsPrice
                                        : 0
                                }}
                            </div>
                        </div>
                        <div class="divFloat">
                            <span>运费：</span>
                            <div class="keyWord">
                                ￥{{
                                    settlementForm.goodsTotalFreight
                                        ? settlementForm.goodsTotalFreight
                                        : 0
                                }}
                            </div>
                        </div>
                        <div class="divFloat">
                            <span>积分抵扣金额：</span>
                            <div class="keyWord">￥0.00</div>
                        </div>
                        <div class="divFloat">
                            <span style="font-weight: bold; fontsize: 18px"
                                >应付总额：</span
                            >
                            <div
                                class="keyWord"
                                style="font-weight: bold; font-size: 0px"
                            >
                                <span
                                    style="
                                        font-size: 14px;
                                        color: rgba(221, 38, 25, 1);
                                    "
                                    >￥</span
                                >
                                <span
                                    style="
                                        font-size: 22px;
                                        font-weight: bold;
                                        color: rgba(221, 38, 25, 1);
                                    "
                                    >{{
                                        settlementForm.payPrice
                                            ? settlementForm.payPrice
                                            : 0
                                    }}</span
                                >
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="subFn">
                <div class="subFn_left" v-if="settlementForm.memberAddress">
                    <div
                        style="
                            height: 14px;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 600;
                            line-height: 14px;
                            color: rgba(51, 51, 51, 1);
                            margin: 12px 0 8px 0;
                        "
                    >
                        <span style="margin: 0 20px 0 0">{{
                            settlementForm.memberAddress.trueName
                        }}</span>
                        <span>{{ settlementForm.memberAddress.mobPhone }}</span>
                    </div>
                    <div
                        style="
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            line-height: 14px;
                            color: rgba(51, 51, 51, 1);
                        "
                    >
                        {{ settlementForm.memberAddress.areaInfo
                        }}{{ settlementForm.memberAddress.address }}
                    </div>
                </div>
                <div class="subFn_left" v-else></div>
                <Button
                    class="subFn_btn"
                    v-if="!modal_loading"
                    :loading="modal_loading"
                    @click="handleSubmit('virtualAddressData')"
                    >提交订单</Button
                >
                <Button class="subFn_btn" v-else>提交订单</Button>
            </div>
        </div>

        <!-- 新增修改地址弹框 -->
        <adressModal
            ref="adressModalStatus"
            @getAction="callbackAction"
        ></adressModal>

        <!-- 优惠券弹框 -->
        <div v-if="couponStatus">
            <Modal
                v-model="couponStatus"
                width="670"
                class="couponModal"
                scrollable
                @on-cancel="couponReset"
            >
                <p
                    slot="header"
                    style="color: #f60; text-align: center; height: 31px"
                >
                    <span
                        style="
                            font-size: 24px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: bold;
                            color: rgba(51, 51, 51, 1);
                        "
                        >选择优惠券</span
                    >
                </p>
                <div class="coupon">
                    <div class="useful">
                        <div
                            class="listItem"
                            v-for="(item, index) in settlementForm
                                .cartToOrderList[couponIndex].canList"
                            :key="index"
                            @click="checkCoupon(index)"
                        >
                            <div class="listItem_left">
                                <div class="listItem_left_top">
                                    <div
                                        style="
                                            font-weight: 400;
                                            font-size: 13px;
                                            line-height: 13px;
                                            margin-bottom: 2px;
                                        "
                                    >
                                        ￥
                                    </div>
                                    <div
                                        style="
                                            font-weight: bold;
                                            line-height: 24px;
                                        "
                                    >
                                        {{ item.faceValue }}
                                    </div>
                                </div>
                                <div class="listItem_left_bottom">
                                    满{{ item.limitAmount }}元可用
                                </div>
                            </div>
                            <div class="listItem_right">
                                <div
                                    class="storeNameWords"
                                    style="
                                        color: rgba(51, 51, 51, 1);
                                        font-size: 14px;
                                        font-weight: bold;
                                    "
                                    :title="item.storeName"
                                >
                                    {{ item.storeName }}
                                </div>
                                <div>
                                    适用范围：{{
                                        item.activityGoodsScope == 0
                                            ? "全场通用"
                                            : item.activityGoodsScope == 1
                                            ? "指定店铺分类"
                                            : item.activityGoodsScope == 2
                                            ? "指定商品"
                                            : "指定品牌"
                                    }}
                                </div>
                                <div>有效期至：{{ item.endDate }}</div>
                                <img
                                    v-if="item.selectFlag == 1 ? true : false"
                                    class="check"
                                    :src="'/img/04.shoppingCar/coupon3.svg'"
                                />
                            </div>
                        </div>
                    </div>
                    <div
                        class="useful"
                        v-if="
                            settlementForm.cartToOrderList[couponIndex]
                                .canntList.length != 0
                        "
                    >
                        <div
                            style="
                                width: 100%;
                                padding: 10px 0;
                                font-size: 18px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 500;
                                color: rgba(102, 102, 102, 1);
                                border-bottom: 1px solid #ebebeb;
                            "
                        >
                            以下优惠券不满足使用条件
                        </div>
                        <div class="useful" style="margin: 20px 0 0 0">
                            <div
                                class="listItem"
                                v-for="(item, index) in settlementForm
                                    .cartToOrderList[couponIndex].canntList"
                                :key="index"
                            >
                                <div class="listItem_left other">
                                    <div class="listItem_left_top">
                                        <div
                                            style="
                                                font-weight: 400;
                                                font-size: 13px;
                                                line-height: 13px;
                                                margin-bottom: 2px;
                                            "
                                        >
                                            ￥
                                        </div>
                                        <div
                                            style="
                                                font-weight: bold;
                                                line-height: 24px;
                                            "
                                        >
                                            {{ item.faceValue }}
                                        </div>
                                    </div>
                                    <div class="listItem_left_bottom">
                                        满{{ item.limitAmount }}元可用
                                    </div>
                                </div>
                                <div class="listItem_right">
                                    <div
                                        class="storeNameWords"
                                        style="
                                            color: #666666;
                                            font-size: 14px;
                                            font-weight: bold;
                                        "
                                        :title="item.storeName"
                                    >
                                        {{ item.storeName }}
                                    </div>
                                    <div style="color: #666666">
                                        适用范围：{{
                                            item.activityGoodsScope == 0
                                                ? "全场通用"
                                                : item.activityGoodsScope == 1
                                                ? "指定店铺分类"
                                                : item.activityGoodsScope == 2
                                                ? "指定商品"
                                                : "指定品牌"
                                        }}
                                    </div>
                                    <div style="color: #666666">
                                        有效期至：{{ item.endDate }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div slot="footer" class="footerBtn">
                    <Button
                        size="default"
                        style="width: auto"
                        @click="couponReset('不用优惠券')"
                        >不用优惠券</Button
                    >
                    <Button
                        style="
                            margin: 0 0 0 65px;
                            background: rgba(221, 38, 25, 1);
                            color: rgba(255, 255, 255, 1);
                            width: auto;
                        "
                        size="default"
                        :loading="modal_loading"
                        @click="couponSubmit"
                        >确认使用</Button
                    >
                </div>
            </Modal>
        </div>

        <!-- 发票弹框 -->
        <Modal
            v-show="!receiptNewStatus"
            :mask-closable="false"
            v-model="receiptStatus"
            width="480"
            class="receipt"
            @on-cancel="receiptReset('receiptForm')"
        >
            <p
                slot="header"
                style="color: #f60; text-align: center; height: 38px"
            >
                <span
                    style="
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: #333333;
                        line-height: 16px;
                    "
                    >发票信息</span
                >
            </p>
            <div style="width: 100%">
                <div class="receipt_mold">
                    <div class="commenTitle">发票类型</div>
                    <div
                        class="mold_item"
                        @click="receiptMoldFn(item.key, index)"
                        v-for="(item, index) in moldList"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.memberInvoiceType,
                        }"
                        :key="index"
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.memberInvoiceType"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
                <div class="receipt_type" style="margin: 0 0 10px 0">
                    <div class="commenTitle">发票抬头</div>
                    <div
                        class="commentDiv"
                        @click="receiptTypeFn(index)"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.companyType,
                        }"
                        v-for="(item, index) in typeList"
                        :key="index"
                        v-if="
                            receiptForm.memberInvoiceType === 3 && item.key == 1
                                ? false
                                : true
                        "
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.companyType"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
                <div class="receipt_type" v-if="contentTypeList.length != 0">
                    <div class="commenTitle">发票内容</div>
                    <div
                        class="commentDiv"
                        @click="invoceType(index)"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.memberInvoiceContent,
                        }"
                        v-for="(item, index) in contentTypeList"
                        :key="index"
                        v-if="
                            receiptForm.memberInvoiceType === 3 && item.key == 2
                                ? false
                                : true
                        "
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.memberInvoiceContent"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
                <div class="commenBox-title" v-if="!seconedStatus">
                    {{
                        (receiptForm.memberInvoiceType == 1 ||
                            receiptForm.memberInvoiceType === 2) &&
                        receiptForm.companyType === 1
                            ? "个人"
                            : (receiptForm.memberInvoiceType === 1 ||
                                  receiptForm.memberInvoiceType === 2) &&
                              receiptForm.companyType === 2
                            ? "单位"
                            : "发票"
                    }}信息
                </div>
                <div class="commenBox-title" v-if="seconedStatus">
                    {{
                        receiptForm.memberInvoiceType == 1 &&
                        receiptForm.companyType === 2
                            ? "收件人"
                            : receiptForm.memberInvoiceType === 2 &&
                              receiptForm.companyType === 2
                            ? "收件"
                            : "发票"
                    }}信息
                </div>
                <Form
                    class="receiptForm1"
                    ref="receiptForm"
                    :model="receiptForm"
                    :label-colon="true"
                    :rules="receiptRule"
                    :label-width="115"
                >
                    <div class="commenBox">
                        <!-- 电子个人 -->
                        <div v-show="electronPerson">
                            <FormItem
                                label="个人名称"
                                class="ivu-form-item-required"
                                prop="personalName"
                            >
                                <Input
                                    v-model="receiptForm.personalName"
                                    clearable
                                    maxlength="12"
                                    placeholder="请输入个人名称"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="收票人手机"
                                class="ivu-form-item-required"
                                prop="addresseePhone"
                            >
                                <Input
                                    v-model="receiptForm.addresseePhone"
                                    clearable
                                    maxlength="11"
                                    placeholder="请填写收票人手机"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="收票人邮箱"
                                class="ivu-form-item-required"
                                prop="addresseeMail"
                            >
                                <Input
                                    v-model="receiptForm.addresseeMail"
                                    clearable
                                    maxlength="64"
                                    placeholder="请填写收票人邮箱"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                        </div>
                        <!-- 所有单位类型的发票 -->
                        <div v-show="allCompany">
                            <FormItem
                                label="单位名称"
                                class="ivu-form-item-required"
                                prop="company"
                            >
                                <Input
                                    v-if="invoiceList.length == 0"
                                    v-model="receiptForm.company"
                                    clearable
                                    :maxlength="40"
                                    placeholder="请输入单位名称"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                                <Select
                                    v-else
                                    v-model="receiptForm.id"
                                    filterable
                                    allow-create
                                    @on-create="handleCreate1"
                                    @on-change="changeInvoice($event)"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                >
                                    <Option
                                        v-for="(invoiceitem,
                                        invoiceindex) in invoiceList"
                                        :value="invoiceitem.id"
                                        :key="invoiceindex"
                                        >{{ invoiceitem.company }}</Option
                                    >
                                </Select>
                            </FormItem>
                            <FormItem
                                label="税号"
                                class="ivu-form-item-required"
                                prop="dutyParagraph"
                            >
                                <Input
                                    v-model="receiptForm.dutyParagraph"
                                    clearable
                                    maxlength="20"
                                    placeholder="请填写单位税号"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem label="注册地址">
                                <Input
                                    v-model="receiptForm.registeredAddress"
                                    clearable
                                    :maxlength="25"
                                    placeholder="请填写单位注册地址"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem label="办公电话">
                                <Input
                                    v-model="receiptForm.officePhone"
                                    type="tel"
                                    clearable
                                    :maxlength="20"
                                    placeholder="请填写单位办公电话"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem label="开户银行">
                                <Input
                                    v-model="receiptForm.bankName"
                                    clearable
                                    maxlength="25"
                                    placeholder="请填写单位开户银行"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="开户银行账号"
                                class="ivu-form-item-required"
                                prop="bankNumber"
                            >
                                <Input
                                    v-model="receiptForm.bankNumber"
                                    clearable
                                    maxlength="19"
                                    placeholder="请填写单位开户银行账号"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                        </div>

                        <!-- 电子单位下一步 -->
                        <div v-show="electronCompany">
                            <FormItem
                                label="收票人手机"
                                class="ivu-form-item-required"
                                prop="addresseePhone"
                            >
                                <Input
                                    v-model="receiptForm.addresseePhone"
                                    clearable
                                    maxlength="11"
                                    placeholder="请填写收票人手机"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="收票人邮箱"
                                class="ivu-form-item-required"
                                prop="addresseeMail"
                            >
                                <Input
                                    v-model="receiptForm.addresseeMail"
                                    clearable
                                    maxlength="64"
                                    placeholder="请填写收票人邮箱"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                        </div>

                        <!-- 普通个人 -->
                        <div v-show="generalPerson">
                            <FormItem
                                label="个人名称"
                                class="ivu-form-item-required"
                                prop="personalName"
                            >
                                <Input
                                    v-model="receiptForm.personalName"
                                    clearable
                                    maxlength="12"
                                    placeholder="请输入个人名称"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="收票人手机"
                                class="ivu-form-item-required"
                                prop="addresseePhone"
                            >
                                <Input
                                    v-model="receiptForm.addresseePhone"
                                    clearable
                                    maxlength="11"
                                    placeholder="请填写收票人手机"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="所在地区"
                                class="ivu-form-item-required"
                                prop="regions"
                            >
                                <Cascader
                                    :data="areaListOne"
                                    :load-data="loadData"
                                    @on-change="regionChangeOtner"
                                    v-model="receiptForm.regions"
                                    placeholder="请选择省/市/区"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                    :key="defaultKey + 6"
                                ></Cascader>
                            </FormItem>
                            <FormItem
                                label="详细地址"
                                class="ivu-form-item-required"
                                prop="detailedAddress"
                            >
                                <Input
                                    v-model="receiptForm.detailedAddress"
                                    placeholder="请输入详细地址"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                        </div>
                        <!-- 普通单位下一步 -->
                        <div v-show="generalCompany">
                            <FormItem
                                label="收票人名称"
                                class="ivu-form-item-required"
                                prop="addresseeName"
                            >
                                <Input
                                    v-model="receiptForm.addresseeName"
                                    clearable
                                    maxlength="11"
                                    placeholder="请填写收票人名称"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="收票人手机"
                                class="ivu-form-item-required"
                                prop="addresseePhone"
                            >
                                <Input
                                    v-model="receiptForm.addresseePhone"
                                    clearable
                                    maxlength="11"
                                    placeholder="请填写收票人手机"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                            <FormItem
                                label="所在地区"
                                class="ivu-form-item-required"
                                prop="regions"
                            >
                                <Cascader
                                    :data="areaListOne"
                                    :load-data="loadData"
                                    @on-change="regionChangeOtner"
                                    v-model="receiptForm.regions"
                                    placeholder="请选择省/市/区"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                ></Cascader>
                            </FormItem>
                            <FormItem
                                label="详细地址"
                                class="ivu-form-item-required"
                                prop="detailedAddress"
                            >
                                <Input
                                    v-model="receiptForm.detailedAddress"
                                    placeholder="请输入详细地址"
                                    style="width: 240px"
                                    class="focusShodowNone"
                                />
                            </FormItem>
                        </div>
                    </div>
                </Form>
                <div
                    v-if="seconedStatus && receiptForm.companyType != 1"
                    style="
                        width: 100%;
                        display: flex;
                        justify-content: space-between;
                        margin: 18px 0 0 0;
                    "
                    class="receiptCon"
                >
                    <Checkbox
                        v-model="setStatus"
                        @on-change="receiptSetDefult($event)"
                        >设为默认发票抬头</Checkbox
                    >
                    <span
                        style="
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            cursor: pointer;
                            color: rgba(102, 102, 102, 1);
                        "
                        @click="receiptNewStatus = true"
                        >发票须知></span
                    >
                </div>
            </div>

            <div
                v-if="receiptForm.companyType != 2 || seconedStatus"
                slot="footer"
                class="footerBtn"
            >
                <Button
                    size="default"
                    class="newBtn"
                    style="
                        background: #f5f5f5;
                        width: 60px;
                        height: 30px;
                        font-size: 14px;
                    "
                    v-if="seconedStatus"
                    @click="oldFn"
                    >上一步</Button
                >
                <Button
                    size="default"
                    class="newBtn"
                    style="
                        background: #f5f5f5;
                        width: 60px;
                        height: 30px;
                        font-size: 14px;
                    "
                    v-else
                    @click="receiptReset('receiptForm')"
                    >取消</Button
                >
                <Button
                    class="newBtn preserveButton"
                    size="default"
                    :loading="modal_loading"
                    @click="receiptSubmit('receiptForm')"
                    >保存</Button
                >
            </div>
            <div v-else slot="footer" class="footerBtn">
                <Button
                    class="newBtn"
                    style="background: #f5f5f5"
                    size="default"
                    @click="nextStep('receiptForm')"
                    >下一步</Button
                >
            </div>
        </Modal>

        <!-- 发票须知弹框 -->
        <Modal
            :mask-closable="false"
            v-model="receiptNewStatus"
            width="680"
            class="receipt"
        >
            <p
                slot="header"
                style="color: #f60; text-align: center; height: 31px"
            >
                <span
                    style="
                        font-size: 24px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: bold;
                        color: rgba(51, 51, 51, 1);
                    "
                    >发票信息</span
                >
            </p>
            <div style="width: 100%">
                <div
                    style="
                        width: 100%;
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: bold;
                        margin: 0 0 20px 0;
                        color: rgba(51, 51, 51, 1);
                    "
                >
                    发票须知
                </div>
                <div class="receiptNew">
                    <div class="receiptNew_title">一、发票介绍</div>
                    <div>
                        雷铭商城一共可以提供三种发票可供用户选择：电子普通发票、纸质普通发票、增值税专项发票具体商品可开具发票已商家可提供发票为准。
                    </div>
                </div>
                <div class="receiptNew">
                    <div class="receiptNew_title">二、开具时间范围</div>
                    <div>
                        自订单完成之日起，会员可在一个月内进行发票申请，已申请的发票，在订单完成之日一年内，可以进行换开，已过期的无法申请。
                    </div>
                </div>
                <div class="receiptNew">
                    <div class="receiptNew_title">三、作废方式</div>
                    <div>
                        对于已开具发票的订单，会员予以退货处理的，纸质发票应与货物一同邮寄到商家。电子发票无需处理，自动作废。如发票缺失，则按照税点扣除会申请退款的金额
                    </div>
                </div>
                <div class="receiptNew">
                    <div class="receiptNew_title">四、发票相关</div>
                    <div>
                        雷铭商城一切发票均遵守国家相关税法规定，具体规定是啥我也不知道，为了显着整齐，再打几个字。反正就是不能违法就对了。
                    </div>
                </div>
            </div>
            <div slot="footer" class="footerBtn"></div>
        </Modal>

        <Modal
            :mask-closable="false"
            v-model="againShoppingStatus"
            width="600"
            class="againShopping"
        >
            <p
                slot="header"
                style="color: #f60; text-align: left; height: 31px"
            >
                <span
                    style="
                        font-size: 18px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: bold;
                        color: rgba(51, 51, 51, 1);
                    "
                    >请重新下单</span
                >
            </p>
            <div
                style="width: 100%; border: 1px solid #f0f0f0"
                v-if="exceedTenMillionFlag == false"
            >
                <div
                    class="problemItem"
                    v-for="(problemItem, problemIndex) in problemList"
                    :key="problemIndex"
                >
                    <div class="title">{{ problemItem.errorMsgTitle }}</div>
                    <div
                        class="item"
                        v-for="(items,
                        indexs) in problemItem.errorGoodsMsgDTOList"
                        :key="indexs"
                    >
                        <div class="item_left">
                            <img
                                style="
                                    width: 84px;
                                    height: 84px;
                                    margin-right: 20px;
                                "
                                v-lazy="$imgURL + items.goodsImage"
                            />
                            <div class="item_content">
                                <div class="words">{{ items.specName }}</div>
                                <div class="guige">
                                    <div class="guige_left">
                                        {{ items.specAttrValueName }}
                                    </div>
                                    <div class="guige_right">
                                        ￥{{ items.price }}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div
                            style="
                                margin-right: 40px;
                                font-weight: bold;
                                color: rgba(51, 51, 51, 1);
                                font-size: 15px;
                            "
                        >
                            x{{ items.goodsNum }}
                        </div>
                    </div>
                </div>
            </div>
            <div
                style="width: 100%; border: 1px solid #f0f0f0"
                v-if="exceedTenMillionFlag == true"
            >
                <div class="problemItem">
                    <div class="title">{{ problemList }}</div>
                </div>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn"
                    style="
                        width: 104px;
                        background: #dd2619;
                        color: #ffffff;
                        border-radius: 4px;
                    "
                    size="default"
                    @click="goBack"
                    >返回重新下单</Button
                >
            </div>
        </Modal>
    </div>
</template>
<script>
    // 搜索头部
    import searchPart from "@/components/common/searchPartWhite.vue";
    import {
        getAddressThreelist, //请求地址数据
    } from "@/api/api_09login.js";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    import {
        Settlement,
        AddressPage,
        AddressUpdata,
        setDefaultAddress,
        AddAddress,
        FillOrderFlush,
        DefaultInvoice,
        UpdataInvoice,
        SaveOrder,
        AreaList,
        BuyNowSettlement,
        BuynowFlush,
        OrderBuynow,
        InvoiceList,
    } from "@/api/api_04.shoppingCar.js";
    import adressModal from "@/components/04.shoppingCar/adressModal.vue";

    export default {
        head() {
            return {
                title: "填写订单",
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
                adressDefaultFlag: false,
                againShoppingStatus: false,
                exceedTenMillionFlag: false, //订单超过1千万的标记
                invoiceStatus: "",
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "填写订单信息",
                        toPath: "",
                    },
                ]),
                zanshiStatus: false,
                showStatus: false,
                adressList: [], //地址列表
                columns1: [
                    {
                        title: "商品信息",
                        slot: "specName",
                        width: 578,
                    },
                    {
                        title: "单价",
                        slot: "specPrice",
                    },
                    {
                        title: "数量",
                        slot: "goodsNum",
                    },
                    {
                        title: "小计",
                        slot: "smallMoney",
                    },
                    // {
                    //     title: '实付',
                    //     slot: 'allmoney'
                    // }
                ],
                modal_loading: false, //按钮缓加载
                adressTips: ["家", "公司", "学校"], //地址标签
                addTips: "", //新增的标签名
                regionList: [],
                couponStatus: false, //优惠券弹框
                couponIndex: "", //选择的是哪个店铺的优惠券,是下标
                couponForm: [], //选择的优惠券
                momentCouponId: "", //暂时勾选的优惠券Id
                receiptStatus: false, //发票弹框
                receipIndex: "", //选择的是哪个店铺的发票,是下标
                receiptNewStatus: false,
                moldList: [
                    {
                        name: "电子发票",
                        key: 1,
                    },
                    {
                        name: "普通发票（纸质）",
                        key: 2,
                    },
                    {
                        name: "增值税专项发票",
                        key: 3,
                    },
                ],
                typeList: [
                    // {name:'不开票',key:0},
                    {
                        name: "个人",
                        key: 1,
                    },
                    {
                        name: "单位",
                        key: 2,
                    },
                ],
                contentTypeList: [
                    {
                        name: "商品明细",
                        key: 1,
                    },
                    {
                        name: "商品类别",
                        key: 2,
                    },
                ],
                setFlag: 0, //设置发票默认
                setStatus: false, //设置发票默认
                seconedStatus: false,
                receiptForm: {
                    memberInvoiceType: 1, //发票分类 1：电子发票，2：普通发票（纸质），3：增值税专项发票
                    companyType: 1, //发票类型 1：个人，2：单位
                    memberInvoiceContent: 1, //发票内容类型 1：商品明细，2：商品类别

                    addresseeAddress: "", //收票人地址（空格分隔） ,
                    addresseeMail: "", //收票人邮箱 ,
                    addresseeName: "", //收票人名称 ,
                    addresseePhone: "", //收票人手机 ,
                    bankName: "", //开户银行 ,
                    bankNumber: "", //银行账号 ,
                    cityId: "", //市ID ,
                    company: "", //单位名称（公司抬头） ,
                    detailedAddress: "", //收票人详细地址 ,
                    districtId: "", //区ID ,
                    dutyParagraph: "", //企业税号 ,
                    officePhone: "", //办公电话 ,
                    personalName: "", //个人名称（个人发票使用） ,
                    provinceId: "", //省ID ,
                    registeredAddress: "", //注册地址 ,
                    storeId: "", //店铺ID ,
                    streetId: "", //街道ID
                    regions: [],
                    id: "",
                },
                receiptRule: {
                    personalName: [
                        // {
                        //   required: true,
                        //   message: "个人名称不能为空",
                        //   trigger: "blur",
                        // },
                        {
                            validator: (rule, value, callback) => {
                                if (this.electronPerson || this.generalPerson) {
                                    if (!value) {
                                        callback(new Error("个人名称不能为空"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    addresseeName: [
                        {
                            validator: (rule, value, callback) => {
                                if (this.generalCompany) {
                                    if (value) {
                                        callback();
                                    } else {
                                        callback(new Error("售票人名称不能为空"));
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    addresseePhone: [
                        // {
                        //   required: true,
                        //   message: "收票人手机号不能为空",
                        //   trigger: "blur",
                        // },
                        {
                            validator: (rule, value, callback) => {
                                if (
                                    this.electronPerson ||
                                    this.generalPerson ||
                                    this.electronCompany ||
                                    this.generalCompany
                                ) {
                                    if (!!value) {
                                        let reg = /^1[3456789]\d{9}$/;
                                        if (!reg.test(value)) {
                                            callback(new Error("手机号格式错误"));
                                        } else {
                                            callback();
                                        }
                                    } else {
                                        callback(new Error("手机号不能为空"));
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    // 收票人邮箱
                    addresseeMail: [
                        // {
                        //   required: true,
                        //   message: "收票人邮箱不能为空",
                        //   trigger: "blur",
                        // },
                        {
                            validator: (rule, value, callback) => {
                                if (this.electronPerson || this.electronCompany) {
                                    if (value) {
                                        callback();
                                    } else {
                                        callback(new Error("收票人邮箱不能为空"));
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                        {
                            type: "email",
                            message: "邮箱格式不正确",
                            trigger: "blur",
                        },
                    ],
                    company: [
                        // 暂时屏蔽
                        {
                            validator: (rule, value, callback) => {
                                console.log(value, this.allCompany, value.length);
                                if (this.allCompany) {
                                    if (value) {
                                        if (value.length <= 40) {
                                            callback();
                                        } else {
                                            callback(
                                                new Error("公司名称不能超过40字")
                                            );
                                        }
                                    } else {
                                        callback(new Error("公司名称不能为空"));
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: ["blur", "change"],
                        },

                        //   {
                        //     type: "string",
                        //     max: 40,
                        //     message: "最多不能超过40个字",
                        //     trigger: "blur",
                        //   },
                    ],
                    // 税号
                    dutyParagraph: [
                        // {
                        //   required: true,
                        //   message: "单位税号不能为空",
                        //   trigger: "blur",
                        // },
                        {
                            validator: (rule, value, callback) => {
                                if (this.allCompany) {
                                    if (!!value) {
                                        let reg = /^[A-Z0-9]{15}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$/;
                                        if (!reg.test(value)) {
                                            callback(new Error("税号格式错误"));
                                        } else {
                                            callback();
                                        }
                                    } else {
                                        callback(new Error("税号不能为空"));
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    // 地区
                    regions: [
                        {
                            validator: (rule, value, callback) => {
                                if (this.generalPerson || this.generalCompany) {
                                    if (value.length == 0 || !value[0]) {
                                        callback(new Error("所在地区不能为空"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    detailedAddress: [
                        // {
                        //   required: true,
                        //   message: "详细地址不能为空",
                        //   trigger: "blur",
                        // },
                        {
                            validator: (rule, value, callback) => {
                                if (this.generalCompany || this.generalPerson) {
                                    if (!value) {
                                        callback(new Error("详细地址不能为空"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                if (this.generalCompany || this.generalPerson) {
                                    if (value.length > 50) {
                                        callback(
                                            new Error(
                                                "详细地址过长,不能超过50个字符"
                                            )
                                        );
                                    } else {
                                        callback();
                                    }
                                }
                            },
                            trigger: ["change", "blur"],
                        },
                    ],
                    // officePhone:[
                    //     { validator: (rule, value, callback) => {
                    //             let reg = /([0-9]{3,4}-)?[0-9]{7,8}/;
                    //             console.log('122',value)
                    //             if (!reg.test(value)) {
                    //                 callback(new Error('办公电话格式错误'));
                    //             } else {
                    //                 callback();
                    //             }
                    //         }, trigger: 'blur'
                    //     }
                    // ],
                    // 银行卡号
                    bankNumber: [
                        {
                            validator: (rule, value, callback) => {
                                if (this.allCompany) {
                                    let reg = /[0-9]{16,19}/;
                                    if (!value) {
                                        callback(new Error("银行卡号不能为空"));
                                    } else if (!reg.test(value)) {
                                        callback(new Error("银行卡号格式错误"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                },
                areaListOne: [],
                childrenList: [],
                receiptFormList: [], //最终要提交的发票集合
                settlementForm: {},
                couponObj: "", //最终要提交的优惠券集合
                messageList: [],
                messageForm: "", //最终要提交的留言集合
                oldsettlementForm: "", //深度克隆的老数据
                invoiceStatusList: [],
                defaultNew: "", //默认发票信息
                orderTypeStatus: false,
                buyNowCouponId: "",
                invoiceList: [], //发票抬头列表
                problemList: [], //提交失败商品
                otherlist: [],
                addressListShow: false,
                giveTimeChose: [
                    "不限送货时间：周一至周日",
                    "工作日送货：周一至周五",
                    "双休日、假日送货：周六至周日",
                ],
                giveTimeActiveIndex: 0,
                payChose: ["在线支付", "货到付款"],
                payActiveIndex: 0,
                defaultKey: new Date().valueOf(),
                virtualAddressData: {
                    memberMobile: "", //虚拟商品接收电话
                    memberName: "", // 虚拟商品接收人姓名
                },
                virtualAddressRule: {
                    memberMobile: [
                        {
                            validator: (rule, value, callback) => {
                                if (this.settlementForm.virtualFlag === 1) {
                                    if (!value) {
                                        callback(new Error("手机号不能为空"));
                                    } else {
                                        let reg = /^1[3456789]\d{9}$/;
                                        if (!reg.test(value)) {
                                            callback(new Error("手机号格式错误"));
                                        } else {
                                            callback();
                                        }
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    memberName: [
                        {
                            validator: (rule, value, callback) => {
                                if (this.settlementForm.virtualFlag === 1) {
                                    if (!value) {
                                        callback(new Error("收货人姓名不能为空"));
                                    } else {
                                        callback();
                                    }
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        components: {
            searchPart,
            adressModal,
            breadcrumb1,
        },
        created() {
            //获取地址列表
            this.getAdressList();
            //获取用户默认发票
            this.getDefultInvoice();
            this.getArea(0);
            this.getInvoiceList();
        },
        mounted() {
            let query = this.$route.query;
            //获取结算页面的初始数据
            if (query.specId && query.number) {
                console.log("tttttttttt", localStorage.getItem("backStatus"));
                if (localStorage.getItem("backStatus")) {
                    localStorage.setItem("backStatus", "");
                    history.back();
                    return false;
                }
                this.orderTypeStatus = true;
                let obj = {
                    specId: query.specId,
                    number: query.number,
                };
                BuyNowSettlement(obj)
                    .then((res) => {
                        console.log("结算数据9999999999999999999999", res);
                        if (res.code == 200) {
                            res.data.cartToOrderList.map((item) => {
                                this.messageList.push({
                                    message: "",
                                    storeId: "",
                                });
                                item.canList.map((items) => {
                                    if (items.selectFlag == 1) {
                                        item.selectItem = items;
                                        this.buyNowCouponId = items.id;
                                    }
                                });
                            });
                            this.settlementForm = {
                                ...this.settlementForm,
                                ...res.data,
                            };
                            this.invoiceJudge(this.settlementForm.cartToOrderList);
                            this.oldsettlementForm = JSON.parse(
                                JSON.stringify(this.settlementForm)
                            );
                        } else {
                            setTimeout(() => {
                                history.back();
                            }, 200);
                        }
                        console.log("=========", this.settlementForm);
                    })
                    .catch((err) => console.log(err));
            } else {
                Settlement()
                    .then((res) => {
                        console.log("结算数据", res);
                        if (res.code == 200) {
                            res.data.cartToOrderList.map((item, index) => {
                                this.messageList.push({
                                    message: "",
                                    storeId: "",
                                });
                                item.canList.map((items) => {
                                    if (items.selectFlag == 1) {
                                        item.selectItem = items;
                                        this.couponForm.push(
                                            '"' +
                                                item.storeId +
                                                '"' +
                                                ":" +
                                                '"' +
                                                items.id +
                                                '"'
                                        );
                                        this.couponForm.reverse();
                                        let i,
                                            j,
                                            allArr = [];
                                        for (
                                            i = 0;
                                            i < this.couponForm.length;
                                            i++
                                        ) {
                                            let flag = true;
                                            for (j = 0; j < allArr.length; j++) {
                                                if (
                                                    this.couponForm[i].split(
                                                        ":"
                                                    )[0] == allArr[j].split(":")[0]
                                                ) {
                                                    flag = false;
                                                }
                                            }
                                            if (flag) {
                                                allArr.push(this.couponForm[i]);
                                            }
                                        }
                                        allArr.reverse();
                                        this.couponObj =
                                            "{" + allArr.join(",") + "}";
                                    }
                                });
                            });
                            this.settlementForm = {
                                ...this.settlementForm,
                                ...res.data,
                            };
                            this.invoiceJudge(this.settlementForm.cartToOrderList);
                            this.oldsettlementForm = JSON.parse(
                                JSON.stringify(this.settlementForm)
                            );
                        } else {
                            setTimeout(() => {
                                history.back();
                            }, 200);
                        }
                        console.log("=========", this.settlementForm);
                    })
                    .catch((err) => console.log(err));
            }
        },
        computed: {
            //电子个人
            electronPerson() {
                return (
                    this.receiptForm.memberInvoiceType === 1 &&
                    this.receiptForm.companyType === 1
                );
            },
            // 普通个人
            generalPerson() {
                return (
                    this.receiptForm.memberInvoiceType === 2 &&
                    this.receiptForm.companyType === 1
                );
            },
            // 普通单位
            generalCompany() {
                return (
                    (this.receiptForm.memberInvoiceType === 2 ||
                        this.receiptForm.memberInvoiceType === 3) &&
                    this.receiptForm.companyType === 2 &&
                    this.seconedStatus
                );
            },
            // 电子单位
            electronCompany() {
                return (
                    this.receiptForm.memberInvoiceType === 1 &&
                    this.receiptForm.companyType === 2 &&
                    this.seconedStatus
                );
            },
            // 所有单位
            allCompany() {
                return this.receiptForm.companyType === 2 && !this.seconedStatus;
            },
        },
        methods: {
            stringToArray(str) {
                if (str) {
                    return str.split(" ");
                }
                return [];
            },
            addOrUpdata(item) {
                if (item) {
                    let obj = JSON.parse(JSON.stringify(item));
                    this.$refs.adressModalStatus.adressFn(obj);
                } else {
                    this.$refs.adressModalStatus.adressFn();
                }
            },
            getInvoiceList() {
                InvoiceList()
                    .then((res) => {
                        console.log("发票列表", res);
                        if (res.code == 200) {
                            this.invoiceList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            changeInvoice(e) {
                this.invoiceList.map((item, index) => {
                    if (e) {
                        if (item.id == e || item.company == e) {
                            this.defaultNew = item;
                            this.receiptForm = {
                                ...this.receiptForm,
                                ...item,
                            };
                        }
                    }
                    // else {
                    //   console.log(1);
                    //   this.invoiceList = this.invoiceList.filter((item) => !!item.id);
                    // }
                });
            },
            handleCreate1(val) {
                if (val.length > 40) {
                    this.$Message["error"]({
                        background: true,
                        content: "单位名称不得超过40字",
                    });
                } else {
                    this.invoiceList.map((item, index) => {
                        if (item.id == this.receiptForm.id) {
                            this.invoiceList[index].company = val;
                            this.receiptForm.company = val;
                        }
                    });
                }
            },
            //去商品详情
            toDetail(id, specId) {
                let routeUrl = this.$router.resolve({
                    name: "goodsDetails",
                    query: {
                        goodsId: id,
                        specId: specId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
            // 第一级的调地区接口  以后可能有用
            getArea(id) {
                AreaList({
                    id: id,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.areaListOne = res.data.map((item) => {
                                return {
                                    value: item.id,
                                    label: item.areaName,
                                    children: [],
                                    loading: false,
                                    flag: item.count,
                                };
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 一级一级的调地区接口  以后可能有用
            loadData(item, callback) {
                console.log("返回数据", item);
                item.loading = true;
                AreaList({
                    id: item.value,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            item.children = res.data.map((item) => {
                                if (item.count != 0) {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                        children: [],
                                        loading: false,
                                    };
                                } else {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                    };
                                }
                            });
                            item.loading = false;
                            callback();
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 判断是否可以开发票
            invoiceJudge(list) {
                list.map((item) => {
                    this.invoiceStatusList.push({
                        checkStatus: false,
                        invoiceShowStatus: true,
                        wordsState: true,
                    });
                    this.otherlist.push({
                        invoiceShowStatus: false,
                        memberInvoiceType: "",
                        companyType: "",
                        memberInvoiceContent: "",
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //街道ID
                        regions: [],
                    });
                });
                list.forEach((goodsItem, goodsIndex) => {
                    let invoiceFlag = 1;
                    let invoiceTypeArr1 = [];
                    let invoiceTypeArr2 = [];
                    let invoiceTypeArr3 = [];
                    let invoiceContentArr1 = [];
                    let invoiceContentArr2 = [];

                    goodsItem.cartGoodsDTOList.forEach((item, index) => {
                        if (item.invoiceType) {
                            if (/1/.test(item.invoiceType)) {
                                invoiceTypeArr1.push("none");
                            }
                            if (/2/.test(item.invoiceType)) {
                                invoiceTypeArr2.push("none");
                            }
                            if (/3/.test(item.invoiceType)) {
                                invoiceTypeArr3.push("none");
                            }
                        } else {
                            this.invoiceStatusList[
                                goodsIndex
                            ].invoiceShowStatus = false;
                            this.invoiceStatusList[goodsIndex].wordsState = false;
                        }
                        if (item.invoiceFlag == 0) {
                            this.invoiceStatusList[
                                goodsIndex
                            ].invoiceShowStatus = false;
                            this.invoiceStatusList[goodsIndex].wordsState = false;
                        }
                        if (/1/.test(item.invoiceContent)) {
                            invoiceContentArr1.push("none");
                        }
                        if (/2/.test(item.invoiceContent)) {
                            invoiceContentArr2.push("none");
                        }
                    });

                    let invoiceTypeArr = [];
                    let invoiceContentArr = [];
                    if (
                        goodsItem.cartGoodsDTOList.length == invoiceTypeArr1.length
                    ) {
                        console.log("能开电子");
                        invoiceTypeArr.push(1);
                    }
                    if (
                        goodsItem.cartGoodsDTOList.length == invoiceTypeArr2.length
                    ) {
                        console.log("能开纸质");
                        invoiceTypeArr.push(2);
                    }

                    if (
                        goodsItem.cartGoodsDTOList.length == invoiceTypeArr3.length
                    ) {
                        console.log("能开增值税");
                        invoiceTypeArr.push(3);
                    }

                    if (
                        goodsItem.cartGoodsDTOList.length ==
                        invoiceContentArr1.length
                    ) {
                        console.log("能开商品明细");
                        invoiceContentArr.push(1);
                    }

                    if (
                        goodsItem.cartGoodsDTOList.length ==
                        invoiceContentArr2.length
                    ) {
                        console.log("能开商品类别");
                        invoiceContentArr.push(2);
                    }
                    console.log(
                        "发票类型：",
                        invoiceTypeArr,
                        "发票内容：",
                        invoiceContentArr
                    );

                    if (
                        invoiceTypeArr.length > 0 &&
                        invoiceContentArr.length > 0 &&
                        invoiceContentArr.length > 0
                    ) {
                        this.invoiceStatusList[goodsIndex].invoiceShowStatus = true;
                    } else {
                        this.invoiceStatusList[
                            goodsIndex
                        ].invoiceShowStatus = false;
                        if (goodsItem.cartGoodsDTOList.length > 1) {
                            this.invoiceStatusList[goodsIndex].wordsState = true;
                        } else {
                            this.invoiceStatusList[goodsIndex].wordsState = false;
                        }
                    }
                });
            },
            //获取用户默认发票
            getDefultInvoice() {
                DefaultInvoice()
                    .then((res) => {
                        console.log("获取默认发票", res);
                        if (res.code == 200 && res.data) {
                            this.defaultNew = res.data;
                            this.receiptForm = {
                                ...this.receiptForm,
                                ...this.defaultNew,
                            };
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //获取地址列表
            getAdressList() {
                AddressPage({
                    page: 1,
                    limit: 100,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            res.data.list.map((item, index) => {
                                item.showStatus = false;
                            });
                            const hasDefaultAddress = res.data.list.some((item) => {
                                return item.defaultFlag === 1;
                            });
                            if (hasDefaultAddress) {
                                this.addressListShow = false;
                            } else {
                                this.addressListShow = true;
                            }
                            if (res.data.list.length === 1 && hasDefaultAddress) {
                                this.adressDefaultFlag = false;
                            } else if (res.data.list.length === 0) {
                                this.adressDefaultFlag = false;
                            } else {
                                this.adressDefaultFlag = true;
                            }
                            this.adressList = res.data.list;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 修改支付方式、配送时间
            changeChoseIndex(value, index) {
                this[value] = index;
            },
            callbackAction(newObj) {
                console.log("----------", this.zanshiStatus);
                this.getAdressList();
                if (this.orderTypeStatus) {
                    this.buyNowFillOrderFlush(newObj.id);
                } else {
                    this.changeFillOrderFlush(
                        newObj.id,
                        this.couponObj ? this.couponObj : ""
                    );
                }
                this.zanshiStatus = false;
            },
            enter(index) {
                this.adressList[index].showStatus = true;
            },
            leave(index) {
                this.adressList[index].showStatus = false;
            },
            //切换地址
            checkAdress(item, index) {
                this.settlementForm.memberAddress = item;
                if (this.orderTypeStatus) {
                    this.buyNowFillOrderFlush(item.id);
                } else {
                    this.changeFillOrderFlush(
                        item.id,
                        this.couponObj ? this.couponObj : ""
                    );
                }
            },
            //设置默认地址
            setDefultAdress(item, val) {
                item.defaultFlag = 1;
                let obj = {
                    id: item.id,
                };
                setDefaultAddress(item)
                    .then((res) => {
                        console.log("设置默认地址结果", res);
                        if (res.data.code == 200) {
                            this.getAdressList();
                            this.$Message.success("设置成功");
                            if (this.orderTypeStatus) {
                                this.buyNowFillOrderFlush(item.id);
                            } else {
                                this.changeFillOrderFlush(
                                    item.id,
                                    this.couponObj ? this.couponObj : ""
                                );
                            }
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 收起地址
            offAddressList() {
                this.addressListShow = !this.addressListShow;
            },
            //购物车更换修改地址和选择优惠券后要掉的接口
            changeFillOrderFlush(addressId, memberCouponsId) {
                let obj = {
                    addressId: addressId,
                    memberCouponsId: memberCouponsId,
                };
                FillOrderFlush(obj)
                    .then((res) => {
                        console.log("更换结果", res);
                        if (res.code == 200) {
                            res.data.cartToOrderList.map((item) => {
                                item.canList.map((items) => {
                                    if (items.selectFlag == 1) {
                                        item.selectItem = items;
                                        this.couponForm.push(
                                            '"' +
                                                item.storeId +
                                                '"' +
                                                ":" +
                                                '"' +
                                                items.id +
                                                '"'
                                        );
                                        this.couponForm.reverse();
                                        let i,
                                            j,
                                            allArr = [];
                                        for (
                                            i = 0;
                                            i < this.couponForm.length;
                                            i++
                                        ) {
                                            let flag = true;
                                            for (j = 0; j < allArr.length; j++) {
                                                if (
                                                    this.couponForm[i].split(
                                                        ":"
                                                    )[0] == allArr[j].split(":")[0]
                                                ) {
                                                    flag = false;
                                                }
                                            }
                                            if (flag) {
                                                allArr.push(this.couponForm[i]);
                                            }
                                        }
                                        allArr.reverse();
                                        this.couponObj =
                                            "{" + allArr.join(",") + "}";
                                    }
                                });
                            });
                            this.settlementForm = {
                                ...this.settlementForm,
                                ...res.data,
                            };
                            this.oldsettlementForm = JSON.parse(
                                JSON.stringify(this.settlementForm)
                            );
                            console.log("=====>>>>", this.settlementForm);
                        } else {
                            // setTimeout(()=>{
                            //     history.back()
                            // },1000)
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //立即购买切换优惠活动/地址
            buyNowFillOrderFlush(addressId) {
                let obj = {
                    activityType: this.buyNowCouponId ? 1 : 0,
                    addressId: addressId,
                    id: this.buyNowCouponId,
                    number: this.$route.query.number,
                    specId: this.$route.query.specId,
                };
                BuynowFlush(obj)
                    .then((res) => {
                        console.log("更换结果", res);
                        if (res.code == 200) {
                            res.data.cartToOrderList.map((item) => {
                                item.canList.map((items) => {
                                    if (items.selectFlag == 1) {
                                        item.selectItem = items;
                                        this.buyNowCouponId = items.id;
                                    }
                                });
                            });
                            this.settlementForm = {
                                ...this.settlementForm,
                                ...res.data,
                            };
                            this.oldsettlementForm = JSON.parse(
                                JSON.stringify(this.settlementForm)
                            );
                            console.log("=====>>>>", this.settlementForm);
                        } else {
                            setTimeout(() => {
                                history.back();
                            }, 1000);
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //选择优惠券
            checkCoupon(index) {
                this.settlementForm.cartToOrderList[this.couponIndex].canList.map(
                    (item, index) => {
                        item.selectFlag = false;
                    }
                );
                this.settlementForm.cartToOrderList[this.couponIndex].canList[
                    index
                ].selectFlag = true;
                this.momentCouponId = this.settlementForm.cartToOrderList[
                    this.couponIndex
                ].canList[index].id;
            },
            // 关闭优惠券弹框
            couponReset(val) {
                console.log("关闭优惠券弹框");
                this.settlementForm.cartToOrderList[
                    this.couponIndex
                ].canList = this.oldsettlementForm.cartToOrderList[
                    this.couponIndex
                ].canList;
                if (val) {
                    let stId = this.settlementForm.cartToOrderList[this.couponIndex]
                        .storeId;
                    let obj = this.couponObj.substring(
                        1,
                        this.couponObj.length - 1
                    );
                    let arr = obj.split(",");
                    arr.map((item, index) => {
                        if (item.indexOf(stId) != -1) {
                            arr.splice(index, 1);
                        }
                    });
                    if (arr.length != 0) {
                        this.couponObj = "{" + arr.join(",") + "}";
                    } else {
                        this.couponObj = "";
                    }
                    if (this.orderTypeStatus) {
                        this.buyNowCouponId = "";
                        this.buyNowFillOrderFlush(
                            this.settlementForm.memberAddress &&
                                this.settlementForm.memberAddress.id
                        );
                    } else {
                        this.changeFillOrderFlush(
                            this.settlementForm.memberAddress &&
                                this.settlementForm.memberAddress.id,
                            this.couponObj
                        );
                    }
                }
                this.momentCouponId = "";
                setTimeout(() => {
                    this.couponStatus = false;
                }, 200);
            },
            couponSubmit() {
                this.modal_loading = true;
                // 把使用的优惠券存储
                let nowId = "";
                if (this.momentCouponId) {
                    nowId = this.momentCouponId;
                    this.buyNowCouponId = this.momentCouponId;
                } else {
                    this.settlementForm.cartToOrderList[
                        this.couponIndex
                    ].canList.map((item) => {
                        if (item.selectFlag == 1) {
                            nowId = item.id;
                            this.buyNowCouponId = item.id;
                        }
                    });
                }
                if (!nowId) {
                    this.$Message.warning("请选择优惠券");
                    this.modal_loading = false;
                    return false;
                }
                this.couponForm.push(
                    '"' +
                        this.settlementForm.cartToOrderList[this.couponIndex]
                            .storeId +
                        '"' +
                        ":" +
                        '"' +
                        nowId +
                        '"'
                );
                this.couponForm.reverse();
                let i,
                    j,
                    allArr = [];
                for (i = 0; i < this.couponForm.length; i++) {
                    let flag = true;
                    for (j = 0; j < allArr.length; j++) {
                        if (
                            this.couponForm[i].split(":")[0] ==
                            allArr[j].split(":")[0]
                        ) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        allArr.push(this.couponForm[i]);
                    }
                }
                allArr.reverse();
                this.couponObj = "{" + allArr.join(",") + "}";
                console.log("=111===", this.couponObj, this.settlementForm);
                if (this.orderTypeStatus) {
                    this.buyNowFillOrderFlush(
                        this.settlementForm.memberAddress &&
                            this.settlementForm.memberAddress.id
                    );
                } else {
                    this.changeFillOrderFlush(
                        this.settlementForm.memberAddress &&
                            this.settlementForm.memberAddress.id,
                        this.couponObj
                    );
                }
                this.momentCouponId = "";
                setTimeout(() => {
                    this.modal_loading = false;
                    this.couponStatus = false;
                }, 200);
            },
            //留言失焦
            inputBlue(storeId, index) {
                this.messageList[index].storeId = storeId;
                console.log(
                    storeId,
                    this.messageList[index].message,
                    "留言列表",
                    this.messageList
                );
                let obj = "";
                this.messageList.map((item, index) => {
                    if (item.storeId && item.message) {
                        obj +=
                            "{" +
                            '"' +
                            item.storeId +
                            '"' +
                            ":" +
                            '"' +
                            item.message +
                            '"' +
                            "}" +
                            ",";
                    }
                });
                console.log("----", obj.slice(0, obj.length - 1));
                this.messageForm = "[" + obj.slice(0, obj.length - 1) + "]";
                console.log("===", this.messageForm);
            },

            //打开发票弹框
            receiptSet(e, index, goods) {
                this.receipIndex = index;
                if (e) {
                    let invoiceFlag = 1;
                    let invoiceTypeArr1 = [];
                    let invoiceTypeArr2 = [];
                    let invoiceTypeArr3 = [];
                    let invoiceContentArr1 = [];
                    let invoiceContentArr2 = [];

                    goods.cartGoodsDTOList.forEach((item, indexitem) => {
                        if (item.invoiceFlag == 0) {
                            invoiceFlag = 0;
                        }
                        if (/1/.test(item.invoiceType)) {
                            invoiceTypeArr1.push("none");
                        }
                        if (/2/.test(item.invoiceType)) {
                            invoiceTypeArr2.push("none");
                        }
                        if (/3/.test(item.invoiceType)) {
                            invoiceTypeArr3.push("none");
                        }

                        if (/1/.test(item.invoiceContent)) {
                            invoiceContentArr1.push("none");
                        }
                        if (/2/.test(item.invoiceContent)) {
                            invoiceContentArr2.push("none");
                        }
                    });
                    if (invoiceFlag == 0) {
                        console.log("不能开发票!");
                        this.$Message.warning("选择的商品不能开发票!");
                        setTimeout(() => {
                            this.invoiceStatusList[
                                this.receipIndex
                            ].checkStatus = false;
                        }, 500);
                        return;
                    }
                    let invoiceTypeArr = [];
                    let invoiceContentArr = [];
                    if (goods.cartGoodsDTOList.length == invoiceTypeArr1.length) {
                        console.log("能开电子");
                        invoiceTypeArr.push({
                            name: "电子发票",
                            key: 1,
                        });
                    }
                    if (goods.cartGoodsDTOList.length == invoiceTypeArr2.length) {
                        console.log("能开纸质");
                        invoiceTypeArr.push({
                            name: "普通发票（纸质）",
                            key: 2,
                        });
                    }

                    if (goods.cartGoodsDTOList.length == invoiceTypeArr3.length) {
                        console.log("能开增值税");
                        invoiceTypeArr.push({
                            name: "增值税专项发票",
                            key: 3,
                        });
                    }

                    if (
                        goods.cartGoodsDTOList.length == invoiceContentArr1.length
                    ) {
                        console.log("能开商品明细");
                        invoiceContentArr.push({
                            name: "商品明细",
                            key: 1,
                        });
                    }

                    if (
                        goods.cartGoodsDTOList.length == invoiceContentArr2.length
                    ) {
                        console.log("能开商品类别");
                        invoiceContentArr.push({
                            name: "商品类别",
                            key: 2,
                        });
                    }
                    this.moldList = invoiceTypeArr;
                    console.log("市场的创伤大城市的城市", this.moldList);

                    if (this.moldList.length != 0) {
                        this.receiptForm.memberInvoiceType = this.moldList[0].key;
                    }

                    this.contentTypeList = invoiceContentArr;
                    if (
                        this.contentTypeList.length != 0 &&
                        this.receiptForm.memberInvoiceType != 3
                    ) {
                        this.receiptForm.companyType = this.contentTypeList[0].key;
                    } else {
                        this.receiptForm.companyType = 2;
                    }
                    this.receiptForm.memberInvoiceContent = this.contentTypeList[0].key;

                    this.receiptStatus = true;
                } else {
                    console.log("222222222", index, this.otherlist);
                    this.receiptFormList.splice(index, 1);
                    this.otherlist[index].invoiceShowStatus = false;
                    console.log("市场的创伤大城市的城市", this.otherlist);
                }
            },
            //设置判断发票默认的变量
            receiptSetDefult(e) {
                if (e) {
                    this.setFlag = 1;
                } else {
                    this.setFlag = 0;
                }
            },
            //回显发票信息
            toUpdataInvoice(item, index, goods) {
                console.log("发票回显id", item.id);
                this.receipIndex = index;
                let invoiceFlag = 1;
                let invoiceTypeArr1 = [];
                let invoiceTypeArr2 = [];
                let invoiceTypeArr3 = [];
                let invoiceContentArr1 = [];
                let invoiceContentArr2 = [];

                goods.cartGoodsDTOList.forEach((item, index) => {
                    if (item.invoiceFlag == 0) {
                        invoiceFlag = 0;
                    }
                    if (/1/.test(item.invoiceType)) {
                        invoiceTypeArr1.push("none");
                    }
                    if (/2/.test(item.invoiceType)) {
                        invoiceTypeArr2.push("none");
                    }
                    if (/3/.test(item.invoiceType)) {
                        invoiceTypeArr3.push("none");
                    }

                    if (/1/.test(item.invoiceContent)) {
                        invoiceContentArr1.push("none");
                    }
                    if (/2/.test(item.invoiceContent)) {
                        invoiceContentArr2.push("none");
                    }
                });
                if (invoiceFlag == 0) {
                    console.log("不能开发票");
                    this.$Message.warning("选择的商品121不能开发票!");
                    setTimeout(() => {
                        this.invoiceStatusList[
                            this.receipIndex
                        ].checkStatus = false;
                    }, 500);
                    return;
                }
                let invoiceTypeArr = [];
                let invoiceContentArr = [];
                if (goods.cartGoodsDTOList.length == invoiceTypeArr1.length) {
                    console.log("能开电子");
                    invoiceTypeArr.push({
                        name: "电子发票",
                        key: 1,
                    });
                }
                if (goods.cartGoodsDTOList.length == invoiceTypeArr2.length) {
                    console.log("能开纸质");
                    invoiceTypeArr.push({
                        name: "普通发票（纸质）",
                        key: 2,
                    });
                }

                if (goods.cartGoodsDTOList.length == invoiceTypeArr3.length) {
                    console.log("能开增值税");
                    invoiceTypeArr.push({
                        name: "增值税专项发票",
                        key: 3,
                    });
                }

                if (goods.cartGoodsDTOList.length == invoiceContentArr1.length) {
                    console.log("能开商品明细");
                    invoiceContentArr.push({
                        name: "商品明细",
                        key: 1,
                    });
                }

                if (goods.cartGoodsDTOList.length == invoiceContentArr2.length) {
                    console.log("能开商品类别");
                    invoiceContentArr.push({
                        name: "商品类别",
                        key: 2,
                    });
                }
                console.log(invoiceTypeArr, invoiceContentArr);
                this.moldList = invoiceTypeArr;
                this.contentTypeList = invoiceContentArr;
                this.receiptStatus = true;
                this.receiptForm = {
                    ...this.receiptForm,
                    ...item,
                };
            },
            // 设置默认发票
            toSetDefult(obj) {
                let zanshiId =
                    this.defaultNew && this.defaultNew.id ? this.defaultNew.id : "";
                let a = this.receiptForm.memberInvoiceType == 3 ? 1 : 0;
                let item = {
                    bankName: obj.bankName,
                    bankNumber: obj.bankNumber,
                    company: obj.company,
                    defaultFlag: 1,
                    dutyParagraph: obj.dutyParagraph,
                    id: zanshiId,
                    officePhone: obj.officePhone,
                    registeredAddress: obj.registeredAddress,
                    vatFlag: a,
                };
                UpdataInvoice(item)
                    .then((res) => {
                        console.log("设置默认发票", res);
                        this.getDefultInvoice();
                    })
                    .catch((err) => console.log(err));
            },
            //重构发票的省市县街道id
            regionChangeOtner(e) {
                console.log("修改id", e);
                this.receiptForm.provinceId = e[0];
                this.receiptForm.cityId = e[1];
                this.receiptForm.districtId = e[2];
                if (e.length == 3) {
                    this.receiptForm.streetId = "";
                } else {
                    this.receiptForm.streetId = e[3];
                }
            },
            //通过省市县街道ID获取对应的省市县街道lable
            receiptDemoFn(arr, data, city = []) {
                if (typeof data === "object") {
                    for (let i = 0; arr[i] !== undefined; i++) {
                        for (let j = 0; data[j] !== undefined; j++) {
                            if (arr[i] === data[j].value) {
                                city.push(data[j].label);
                            }
                        }
                    }
                    for (let i = 0; data[i] !== undefined; i++) {
                        this.receiptDemoFn(arr, data[i].children, city);
                    }
                }
                this.receiptForm.addresseeAddress = city.join(" ");
            },
            // 发票表单储存
            receiptSubmit(name) {
                console.log(this.receiptForm, "提交的表单");
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if (this.receiptForm.regions.length != 0) {
                            this.receiptDemoFn(
                                this.receiptForm.regions,
                                this.areaListOne
                            );
                        }
                        this.modal_loading = true;
                        console.log(
                            "发票表单",
                            this.settlementForm.cartToOrderList[this.receipIndex]
                                .storeId
                        );
                        let obj = {
                            storeId: this.settlementForm.cartToOrderList[
                                this.receipIndex
                            ].storeId,
                        };
                        let objs = {
                            ...this.receiptForm,
                            ...obj,
                        };
                        this.otherlist[this.receipIndex] = objs;
                        console.log("12122", this.receiptForm.addresseeAddress);
                        if (!objs.id) {
                            delete objs["id"];
                        }
                        this.receiptFormList.push(objs);
                        this.receiptFormList.reverse();
                        let i,
                            j,
                            allArr = [];
                        for (i = 0; i < this.receiptFormList.length; i++) {
                            let flag = true;
                            for (j = 0; j < allArr.length; j++) {
                                if (
                                    this.receiptFormList[i].storeId ==
                                    allArr[j].storeId
                                ) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                allArr.push(this.receiptFormList[i]);
                            }
                        }
                        this.receiptFormList = allArr;
                        // 设置默认发票
                        if (
                            this.setStatus &&
                            this.seconedStatus &&
                            this.receiptForm.companyType != 1
                        ) {
                            this.toSetDefult(objs);
                        }
                        console.log("yyyyyyyyy");

                        this.otherlist[this.receipIndex].invoiceShowStatus = true;
                        console.log("pppppppppp", this.otherlist[this.receipIndex]);
                        console.log("====", this.receiptFormList);

                        this.modal_loading = false;
                        this.receiptStatus = false;
                        // this.getInvoiceList();
                        this.receiptForm = {
                            memberInvoiceType: 1, //发票分类 1：电子发票，2：普通发票（纸质），3：增值税专项发票
                            companyType: 1, //发票类型 1：个人，2：单位
                            memberInvoiceContent: 1, //发票内容类型 1：商品明细，2：商品类别
                            addresseeAddress: "", //收票人地址（空格分隔） ,
                            addresseeMail: "", //收票人邮箱 ,
                            addresseeName: "", //收票人名称 ,
                            addresseePhone: "", //收票人手机 ,
                            bankName: "", //开户银行 ,
                            bankNumber: "", //银行账号 ,
                            cityId: "", //市ID ,
                            company: "", //单位名称（公司抬头） ,
                            detailedAddress: "", //收票人详细地址 ,
                            districtId: "", //区ID ,
                            dutyParagraph: "", //企业税号 ,
                            officePhone: "", //办公电话 ,
                            personalName: "", //个人名称（个人发票使用） ,
                            provinceId: "", //省ID ,
                            registeredAddress: "", //注册地址 ,
                            storeId: "", //店铺ID ,
                            streetId: "", //街道ID
                            regions: [],
                        };
                        if (this.receiptForm.companyType == 2) {
                            this.receiptForm = {
                                ...this.receiptForm,
                                ...this.defaultNew,
                            };
                        }
                    }
                });
            },
            // 发票下一步
            nextStep(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        console.log(this.receiptForm);
                        this.seconedStatus = true;
                    }
                });
            },
            // 清空发票校验
            receiptResetData(name) {
                this.$refs[name].resetFields();
                // if (this.receiptForm.companyType == 2) {
                //     this.receiptForm = {
                //         ...this.receiptForm,
                //         ...this.defaultNew,
                //     };
                // }
            },
            invoceType(index) {
                if (this.contentTypeList.length != 1) {
                    this.receiptForm.memberInvoiceContent = index + 1;
                }
                console.log(this.receiptForm.memberInvoiceContent);
            },
            receiptTypeFn(index) {
                this.defaultKey = new Date().valueOf();
                let that = this;
                that.receiptForm.companyType = index + 1;
                if (that.contentTypeList.length != 1) {
                    that.receiptForm.memberInvoiceContent = 1;
                }
                that.seconedStatus = false;
                // if (that.seconedStatus) {
                //     that.seconedStatus = false;
                // }
                // try {
                //     that.receiptResetData("receiptForm");
                //     setTimeout(() => {
                //         that.receiptForm.companyType = index + 1;
                //         if (that.contentTypeList.length != 1) {
                //             that.receiptForm.memberInvoiceContent = 1;
                //         }
                //         that.seconedStatus = false;
                //         let obj = {
                //             addresseeAddress: "", //收票人地址（空格分隔） ,
                //             addresseeMail: "", //收票人邮箱 ,
                //             addresseeName: "", //收票人名称 ,
                //             addresseePhone: "", //收票人手机 ,
                //             bankName: "", //开户银行 ,
                //             bankNumber: "", //银行账号 ,
                //             cityId: "", //市ID ,
                //             company: "", //单位名称（公司抬头） ,
                //             detailedAddress: "", //收票人详细地址 ,
                //             districtId: "", //区ID ,
                //             dutyParagraph: "", //企业税号 ,
                //             officePhone: "", //办公电话 ,
                //             personalName: "", //个人名称（个人发票使用） ,
                //             provinceId: "", //省ID ,
                //             registeredAddress: "", //注册地址 ,
                //             storeId: "", //店铺ID ,
                //             streetId: "", //街道ID
                //             regions: [],
                //         };
                //         that.receiptForm = {
                //             ...that.receiptForm,
                //             ...obj,
                //         };
                //         if (that.receiptForm.companyType == 2) {
                //             that.receiptForm = {
                //                 ...that.receiptForm,
                //                 ...that.defaultNew,
                //             };
                //             this.setStatus = true;
                //         }
                //     }, 200);
                // } catch (e) {
                //     that.receiptForm.companyType = index + 1;
                //     if (that.contentTypeList.length != 1) {
                //         that.receiptForm.memberInvoiceContent = 1;
                //     }
                //     that.seconedStatus = false;
                //     let obj = {
                //         addresseeAddress: "", //收票人地址（空格分隔） ,
                //         addresseeMail: "", //收票人邮箱 ,
                //         addresseeName: "", //收票人名称 ,
                //         addresseePhone: "", //收票人手机 ,
                //         bankName: "", //开户银行 ,
                //         bankNumber: "", //银行账号 ,
                //         cityId: "", //市ID ,
                //         company: "", //单位名称（公司抬头） ,
                //         detailedAddress: "", //收票人详细地址 ,
                //         districtId: "", //区ID ,
                //         dutyParagraph: "", //企业税号 ,
                //         officePhone: "", //办公电话 ,
                //         personalName: "", //个人名称（个人发票使用） ,
                //         provinceId: "", //省ID ,
                //         registeredAddress: "", //注册地址 ,
                //         storeId: "", //店铺ID ,
                //         streetId: "", //街道ID
                //         regions: [],
                //     };
                //     that.receiptForm = {
                //         ...that.receiptForm,
                //         ...obj,
                //     };
                //     if (that.receiptForm.companyType == 2) {
                //         that.receiptForm = {
                //             ...that.receiptForm,
                //             ...that.defaultNew,
                //         };
                //         this.setStatus = true;
                //     }
                // }
            },
            // 发票上一步按钮
            oldFn() {
                let that = this;
                try {
                    if (!that.otherlist[that.receipIndex].invoiceShowStatus) {
                        that.receiptResetData("receiptForm");
                    }
                    that.seconedStatus = false;
                    console.log(this.receiptForm);
                } catch (e) {
                    that.seconedStatus = false;
                    console.log(this.receiptForm);
                }
            },
            // 发票表单重置
            receiptReset(name) {
                // if(this.receiptFormList.length!=0){
                //     this.receiptFormList.map(item=>{
                //         if(item.storeId == this.settlementForm.cartToOrderList[this.receipIndex].storeId){
                //             this.invoiceStatusList[this.receipIndex].checkStatus = false;
                //         }
                //     })
                // }else{
                if (this.otherlist[this.receipIndex].invoiceShowStatus) {
                    this.invoiceStatusList[this.receipIndex].checkStatus = true;
                } else {
                    this.invoiceStatusList[this.receipIndex].checkStatus = false;
                }
                // }
                console.log(
                    "mmmmmmmm",
                    this.invoiceStatusList[this.receipIndex].checkStatus
                );
                this.receiptStatus = false;
                try {
                    this.$refs[name].resetFields();
                } catch (e) {
                    this.receiptForm = {
                        memberInvoiceType: 1, //发票分类 1：电子发票，2：普通发票（纸质），3：增值税专项发票
                        companyType: 1, //发票类型 1：个人，2：单位
                        memberInvoiceContent: 1, //发票内容类型 1：商品明细，2：商品类别
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //街道ID
                        regions: [],
                    };
                    if (this.receiptForm.companyType == 2) {
                        this.receiptForm = {
                            ...this.receiptForm,
                            ...this.defaultNew,
                        };
                    }
                }
            },
            // 改变发票类型
            receiptMoldFn(key, index) {
                this.defaultKey = new Date().valueOf();
                let that = this;
                if (that.seconedStatus) {
                    that.seconedStatus = false;
                }
                if (that.moldList.length != 1) {
                    that.receiptForm.memberInvoiceType = index + 1;
                }
                if (that.contentTypeList.length != 1) {
                    that.receiptForm.memberInvoiceContent = 1;
                }
                key == 3
                    ? (that.receiptForm.companyType = 2)
                    : (that.receiptForm.companyType = 1);
                if (that.receiptForm.companyType == 2) {
                    that.receiptForm = {
                        ...that.receiptForm,
                        ...that.defaultNew,
                    };
                    this.setStatus = true;
                }
                // try {
                //     that.receiptResetData("receiptForm");
                //     setTimeout(() => {
                //         if (that.moldList.length != 1) {
                //             that.receiptForm.memberInvoiceType = index + 1;
                //         }
                //         if (that.contentTypeList.length != 1) {
                //             that.receiptForm.memberInvoiceContent = 1;
                //         }
                //         key == 3
                //             ? (that.receiptForm.companyType = 2)
                //             : (that.receiptForm.companyType = 1);
                //         let obj = {
                //             addresseeAddress: "", //收票人地址（空格分隔） ,
                //             addresseeMail: "", //收票人邮箱 ,
                //             addresseeName: "", //收票人名称 ,
                //             addresseePhone: "", //收票人手机 ,
                //             bankName: "", //开户银行 ,
                //             bankNumber: "", //银行账号 ,
                //             cityId: "", //市ID ,
                //             company: "", //单位名称（公司抬头） ,
                //             detailedAddress: "", //收票人详细地址 ,
                //             districtId: "", //区ID ,
                //             dutyParagraph: "", //企业税号 ,
                //             officePhone: "", //办公电话 ,
                //             personalName: "", //个人名称（个人发票使用） ,
                //             provinceId: "", //省ID ,
                //             registeredAddress: "", //注册地址 ,
                //             storeId: "", //店铺ID ,
                //             streetId: "", //街道ID
                //             regions: [],
                //         };
                //         that.receiptForm = {
                //             ...that.receiptForm,
                //             ...obj,
                //         };
                //         if (that.receiptForm.companyType == 2) {
                //             that.receiptForm = {
                //                 ...that.receiptForm,
                //                 ...that.defaultNew,
                //             };
                //             this.setStatus = true;
                //         }
                //     }, 200);
                // } catch (e) {
                //     // console.log('ggggggggggggggggggggggggggggggggggggggg')
                //     if (that.moldList.length != 1) {
                //         that.receiptForm.memberInvoiceType = index + 1;
                //     }
                //     if (that.contentTypeList.length != 1) {
                //         that.receiptForm.memberInvoiceContent = 1;
                //     }
                //     key == 3
                //         ? (that.receiptForm.companyType = 2)
                //         : (that.receiptForm.companyType = 1);
                //     let obj = {
                //         addresseeAddress: "", //收票人地址（空格分隔） ,
                //         addresseeMail: "", //收票人邮箱 ,
                //         addresseeName: "", //收票人名称 ,
                //         addresseePhone: "", //收票人手机 ,
                //         bankName: "", //开户银行 ,
                //         bankNumber: "", //银行账号 ,
                //         cityId: "", //市ID ,
                //         company: "", //单位名称（公司抬头） ,
                //         detailedAddress: "", //收票人详细地址 ,
                //         districtId: "", //区ID ,
                //         dutyParagraph: "", //企业税号 ,
                //         officePhone: "", //办公电话 ,
                //         personalName: "", //个人名称（个人发票使用） ,
                //         provinceId: "", //省ID ,
                //         registeredAddress: "", //注册地址 ,
                //         storeId: "", //店铺ID ,
                //         streetId: "", //街道ID
                //         regions: [],
                //     };
                //     that.receiptForm = {
                //         ...that.receiptForm,
                //         ...obj,
                //     };
                //     if (that.receiptForm.companyType == 2) {
                //         that.receiptForm = {
                //             ...that.receiptForm,
                //             ...that.defaultNew,
                //         };
                //         this.setStatus = true;
                //     }
                // }
                // console.log("2222", that.receiptForm.memberInvoiceContent);
            },
            // 重新下单
            goBack() {
                this.againShoppingStatus = false;
                this.$router.go(-1);
            },
            // 验证虚拟订单的信息提交
            handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.orderSub();
                    } else {
                        this.$Message.error("请确认虚拟订单收货信息正确!");
                    }
                });
            },
            // 提交订单
            orderSub() {
                console.log("最终要提交的优惠券集合", this.couponObj);
                console.log("最终要提交的发票集合", this.receiptFormList);
                console.log("最终要提交的留言集合", this.messageForm);
                console.log("======", this.settlementForm.memberAddress);
                if (
                    !this.settlementForm.memberAddress ||
                    !this.settlementForm.memberAddress.id
                ) {
                    this.$Message.warning("您还未选择地址");
                    return false;
                }
                this.modal_loading = true;
                if (this.orderTypeStatus) {
                    //orderTypeStatus为true时是立即购买进来的，反之就是购物车进来的
                    let obj0, obj1;
                    let id1 = this.settlementForm.cartToOrderList[0]
                        .cartGoodsDTOList[0].activityId
                        ? this.settlementForm.cartToOrderList[0].cartGoodsDTOList[0]
                              .activityId
                        : "";
                    let type1 = this.settlementForm.cartToOrderList[0]
                        .cartGoodsDTOList[0].activityType;
                    let memberInvoiceDTO = {};
                    obj0 = {
                        addressId: this.settlementForm.memberAddress.id, //地址id
                        invoiceType: 0,
                        goodsNum: this.$route.query.number,
                        memberCouponsId: this.buyNowCouponId,
                        orderMessage: this.messageList[0].message,
                        orderPlatform: 0,
                        specId: this.$route.query.specId,
                        specSellPrice: this.settlementForm.cartToOrderList[0]
                            .cartGoodsDTOList[0].specSellPrice,
                        // specSellPrice: 999.11,
                        activityId: id1,
                        activityType: type1,
                        memberMobile: this.virtualAddressData.memberMobile, //只有虚拟订单才有该字段   用户手机号(虚拟订单必传),
                        memberName: this.virtualAddressData.memberName, //只有虚拟订单才有该字段      收件人(虚拟订单必传),
                    };
                    if (this.receiptFormList.length != 0) {
                        obj1 = {
                            addresseeMail: this.receiptFormList[0].addresseeMail,
                            addresseeName: this.receiptFormList[0].addresseeName,
                            addresseePhone: this.receiptFormList[0].addresseePhone,
                            companyType: this.receiptFormList[0].companyType,
                            invoiceContent: this.receiptFormList[0]
                                .memberInvoiceContent,
                            invoiceType: this.receiptFormList[0].memberInvoiceType,
                            memberInvoiceDTO: memberInvoiceDTO,
                            personalName: this.receiptFormList[0].personalName,
                            addresseeAddress: this.receiptFormList[0]
                                .addresseeAddress,
                            cityId: this.receiptFormList[0].cityId,
                            detailedAddress: this.receiptFormList[0]
                                .detailedAddress,
                            districtId: this.receiptFormList[0].districtId,
                            provinceId: this.receiptFormList[0].provinceId,
                            streetId: this.receiptFormList[0].streetId,

                            // "groupRecordId": 0, //拼团记录id ,
                        };
                        if (this.receiptFormList[0].companyType == 2) {
                            obj1.memberInvoiceDTO = {
                                bankName: this.receiptFormList[0].bankName,
                                bankNumber: this.receiptFormList[0].bankNumber,
                                company: this.receiptFormList[0].company,
                                defaultFlag: this.receiptFormList[0].defaultFlag,
                                dutyParagraph: this.receiptFormList[0]
                                    .dutyParagraph,
                                id: this.receiptFormList[0].id,
                                officePhone: this.receiptFormList[0].officePhone,
                                registeredAddress: this.receiptFormList[0]
                                    .registeredAddress,
                                vatFlag:
                                    this.receiptFormList[0].memberInvoiceType != 3
                                        ? 0
                                        : 1,
                            };
                        }
                    } else {
                        obj1 = {};
                    }
                    let obj = {
                        ...obj0,
                        ...obj1,
                    };
                    (obj.activityId = this.settlementForm.cartToOrderList[0].cartGoodsDTOList[0].activityId),
                        (obj.activityType = this.settlementForm.cartToOrderList[0].cartGoodsDTOList[0].activityType),
                        console.log(">>>>>>>>>", obj, JSON.stringify(obj1));
                    OrderBuynow(obj)
                        .then((res) => {
                            console.log("提交订单结果", res);
                            if (res.code == 200) {
                                console.log("返回的orderId", res.data.orderId);
                                if (res.data.checkStatus == 10) {
                                    this.modal_loading = false;
                                    localStorage.setItem("backStatus", "true");
                                    this.$router.push({
                                        path: "/toPay",
                                        query: {
                                            orderId: res.data.orderId,
                                        },
                                    });
                                } else if (res.data.checkStatus == 50) {
                                    this.modal_loading = false;
                                    console.log(123);
                                } else {
                                    this.modal_loading = false;
                                    this.problemList = res.data.errorGoodsList;
                                    this.againShoppingStatus = true;
                                }
                            } else {
                                this.modal_loading = false;
                            }
                        })
                        .catch((err) => console.log(err));
                } else {
                        console.log('到这里了')
                    let obj = {
                        addressId: this.settlementForm.memberAddress.id, //地址id
                        memberCouponsId: this.couponObj, //最终要提交的优惠券集合
                        orderInvoiceSaveDTOS: this.receiptFormList, //最终要提交的发票集合
                        orderMessage: this.messageForm, //最终要提交的留言集合
                        orderPlatform: 0, //订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
                        memberMobile: this.virtualAddressData.memberMobile, //只有虚拟订单才有该字段   用户手机号(虚拟订单必传)
                        memberName: this.virtualAddressData.memberName, //只有虚拟订单才有该字段      收件人(虚拟订单必传)
                    }
                    if(obj.orderInvoiceSaveDTOS.length!=0){
                            if(obj.orderInvoiceSaveDTOS[0].companyType=='1'){
                            obj.orderInvoiceSaveDTOS[0].company = ''
                            obj.orderInvoiceSaveDTOS[0].dutyParagraph = ''
                            obj.orderInvoiceSaveDTOS[0].registeredAddress = ''
                            obj.orderInvoiceSaveDTOS[0].officePhone = ''
                            obj.orderInvoiceSaveDTOS[0].bankName = ''
                            obj.orderInvoiceSaveDTOS[0].bankNumber = ''
                        }
                    }

                    SaveOrder(obj)
                        .then((res) => {
                            console.log("提交订单结果", res);
                            if (res.code == 200) {
                                console.log("返回的orderId", res.data.orderId);
                                if (res.data.checkStatus == 10) {
                                    this.modal_loading = false;
                                    this.$router.push({
                                        path: "/toPay",
                                        query: {
                                            orderId: res.data.orderId,
                                        },
                                    });
                                } else if ([30,40,50].includes(res.data.checkStatus)) {
                                    this.modal_loading = false;
                                    this.problemList = res.data.msg;
                                    this.againShoppingStatus = true;
                                    this.exceedTenMillionFlag = true;
                                } else {
                                    this.modal_loading = false;
                                    this.problemList = res.data.errorGoodsList;
                                    this.againShoppingStatus = true;
                                    this.exceedTenMillionFlag = false;
                                }
                            } else {
                                this.modal_loading = false;
                                this.$Message.error("系统发生错误，请稍后重试");
                            }
                        })
                        .catch((err) => {
                            this.$Message.error("系统发生错误，请稍后重试");
                            console.log(err);
                        });
                }
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";

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

    /deep/ .ivu-collapse-header {
        padding-bottom: 10px;
        height: 50px !important;

        // .ivu-icon-ios-arrow-forward:before{
        //     content: ">>" !important;
        // }
        .ivu-icon-ios-arrow-forward {
            top: 11.5px !important;
        }
    }

    /deep/ .ivu-input {
        border-radius: 0;
        font-size: 12px;
        color: #222;
    }
    /deep/ .ivu-select-input {
        font-size: 12px;
    }

    .storeNameWords {
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }

    .newBtn {
        width: 69px;
        height: 34px;
        border: none;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 0;

        &:hover {
            color: #333333;
        }
    }

    .changeWords:hover {
        color: #dd2619 !important;
    }

    .companyTypeCell {
        max-width: 300px;
        text-overflow: ellipsis;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        margin-right: 30px;
    }

    .otherScroll {
        overflow-y: scroll;
    }
    .newinvoiceDiv-con {
        /deep/ .ivu-checkbox-wrapper {
            width: 142px;
            font-size: 12px;
            .ivu-checkbox-inner {
                width: 12px;
                height: 12px;
                margin-right: 3px;
                &:after {
                    top: 1px;
                    left: 3px;
                }
            }
            .ivu-checkbox-focus {
                box-shadow: none !important;
            }
        }
    }
    .newinvoiceDiv {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        .maindiv {
            // margin-left: 90px;
            font-size: 12px;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            display: flex;
            span {
                font-weight: 400;
            }
        }

        .updataInvoice {
            margin-left: 70px;
            width: 74px;
            height: 30px;
            background: rgba(221, 38, 25, 0.03);
            border-radius: 15px;
            cursor: pointer;
            font-size: 13px;
            font-weight: bold;
            color: rgba(221, 38, 25, 1);
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }

    .fillOrder {
        .adress {
            padding: 22px 0 25px 0;
            border-bottom: 1px solid rgba(230, 230, 230, 1);
            /deep/ .ivu-collapse {
                border-top: none;

                .ivu-collapse-content {
                    padding-left: 0;
                }
            }

            /deep/ .ivu-collapse-header {
                padding-left: 0;

                .ivu-icon-ios-arrow-forward {
                    position: absolute;
                    left: 60px;
                    top: 13px;
                }
            }
        }

        .itemTitle {
            width: 100%;
            height: 14px;
            display: flex;
            justify-content: space-between;
            margin-bottom: 18px;
        }
        .carttoOrderTitle {
            margin-bottom: 12px;
        }
        .cellWords {
            font-size: 13px;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);
            text-align: left;
            line-height: 24px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            // display: flex;
            // align-items: center;
            // text-align: center;
        }

        .changeAddressList {
            width: 100px;
            font-size: 13px;
            height: 13px;
            cursor: pointer;
            img {
                width: 14px;
                height: 14px;
                margin-left: 5px;
                vertical-align: middle;
            }
        }

        .defaultAdress {
            width: 100%;
            height: 100%;
            display: flex;
            margin: 0 0 18px 0;
            align-items: center;

            // &:last-child {
            //     margin-bottom: 0;
            // }

            .btnCell {
                width: 60px;
                height: 20px;
                background: rgba(153, 153, 153, 1);
                border-radius: 2px;
                text-align: center;
                line-height: 20px;
                color: rgba(255, 255, 255, 1);
            }

            .adress_name {
                padding: 0 3px;
                width: 94px;
                height: 30px;
                line-height: 30px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                border: 1px solid rgba(221, 38, 25, 1);
                margin-right: 9px;
                position: relative;
                display: block;
                text-align: center;
                font-size: 12px;
                color: rgba(34, 34, 34, 1);

                img {
                    position: absolute;
                    width: 14px;
                    height: 14px;
                    right: -1px;
                    bottom: -1px;
                }
            }

            .adress_name1 {
                max-width: 89px;
                height: 18px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                font-family: PingFangSC-Regular, PingFang SC;
                line-height: 18px;
                margin-right: 17px;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                font-size: 12px;
                padding: 0 0 0 13px;
                color: rgba(34, 34, 34, 1);
            }

            .adress_news {
                height: 18px;
                font-family: PingFangSC-Regular, PingFang SC;
                color: rgba(34, 34, 34, 1);
                line-height: 18px;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                margin-right: 40px;
                font-size: 12px;
            }

            .adress_phone {
                width: 84px;
                height: 18px;
                font-family: PingFangSC-Regular, PingFang SC;
                line-height: 18px;
                margin-right: 14px;
                font-size: 12px;
                color: rgba(34, 34, 34, 1);
            }

            .adressMain {
                height: 30px;
                padding: 0 10px 0;
                padding-left: 0;
                display: flex;
                // justify-content: space-between;
                align-items: center;
                .adressMain_div {
                    display: flex;
                }
                .addressMainInfo {
                    width: 847px;
                }
            }

            .adressMain:hover {
                background: rgba(255, 246, 246, 1);
            }
        }
        // .defaultAdressItem{

        //     // .adress_phone{
        //     //     width: 520px;
        //     // }

        // }
        .giveTimeList {
            width: 100%;
            display: flex;

            .giveTimeItem {
                margin: 0 20px;
                width: 204px;
                height: 40px;
                background: rgba(255, 255, 255, 1);
                border: 1px solid #ccc;
                text-align: center;
                line-height: 40px;
                font-size: 12px;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                position: relative;
                img {
                    position: absolute;
                    width: 14px;
                    height: 14px;
                    bottom: 0;
                    right: 0;
                    visibility: hidden;
                }
                &:first-child {
                    margin: 0 20px 0 0;
                }
                &:hover {
                    cursor: pointer;
                }
            }
            .active {
                border: 1px solid rgba(221, 38, 25, 1);
                img {
                    visibility: visible;
                }
            }
        }
        .payList {
            width: 100%;
            display: flex;

            .payItem {
                margin: 0 20px;
                width: 122px;
                height: 40px;
                background: rgba(255, 255, 255, 1);
                border: 1px solid #ccc;
                text-align: center;
                line-height: 40px;
                font-size: 12px;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                position: relative;

                img {
                    position: absolute;
                    width: 14px;
                    height: 14px;
                    bottom: -1px;
                    right: -1px;
                    visibility: hidden;
                }
                &:first-child {
                    margin: 0 20px 0 0;
                }
                &:hover {
                    cursor: pointer;
                }
            }
            .active {
                border: 1px solid rgba(221, 38, 25, 1);
                img {
                    visibility: visible;
                }
            }
            .payItemRemarks {
                display: block;
                height: 12px;
                padding-top: 28px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #dd2619;
                line-height: 12px;
            }
        }

        .goodsWords {
            height: 103px;
            font-size: 15px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: bold;
            color: rgba(51, 51, 51, 1);
            margin-top: 20px;
        }

        .goodsNew {
            width: 100%;
            display: flex;
            margin: 20px 0 20px 0;
            height: 84px;

            img {
                width: 84px;
                height: 84px;
                margin-right: 13px;
            }

            .goodsNew_main {
                min-width: 200px;
                max-width: 260px;

                .goodsNew_words {
                    width: 100%;
                    height: 36px;
                    text-overflow: -o-ellipsis-lastline;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    line-clamp: 2;
                    -webkit-box-orient: vertical;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    text-align: left;
                    font-size: 12px;
                    font-weight: 400;
                    color: #222222;
                    line-height: 18px;
                }

                .norms {
                    width: 100%;
                    display: flex;
                    margin: 10px 0 0 0;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #666666;
                    line-height: 12px;
                }
            }
        }

        .coupon {
            width: 100%;
            display: flex;
            align-items: flex-end;

            .couponBtn {
                width: 168px;
                height: 40px;
                background: rgba(221, 38, 25, 0.2);
                border-radius: 1px;
                border: 1px solid rgba(221, 38, 25, 1);
                text-align: center;
                line-height: 40px;
                margin-right: 40px;
                cursor: pointer;
            }
        }

        .amount {
            width: 100%;
            padding: 20px 40px;

            .divFloat {
                display: flex;
                justify-content: flex-end;
                align-items: center;
                margin-bottom: 14px;

                .keyWord {
                    width: 114px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(221, 38, 25, 1);
                    line-height: 22px;
                    text-align: right;
                }

                span {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(51, 51, 51, 1);
                }
            }
        }

        .subFn {
            width: 1200px;
            margin: auto;
            padding: 0 0 0 28px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            // background:rgba(204,204,204,1);
            background: #ededed;

            .subFn_left {
                display: flex;
                height: 60px;
                flex-direction: column;
            }

            .subFn_btn {
                width: 106px;
                height: 60px;
                background: rgba(221, 38, 25, 1);
                border-radius: 0px;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 600;
                color: rgba(255, 255, 255, 1);
                text-align: center;
                line-height: 60px;
                cursor: pointer;
            }
        }
    }

    /deep/ .ivu-modal-footer {
        border: none;

        .footerBtn {
            display: flex;
            justify-content: center;
            padding: 0 0 20px 0;
        }
    }

    /deep/ .ivu-modal-header {
        border: none;
        padding: 30px 16px 0 16px;
    }

    .commenBox-title {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        line-height: 12px;
        color: #222222;
        margin-top: 15px;
    }
    .commenBox {
        width: 100%;
        padding: 24px 9px;
        border: 1px dashed rgba(204, 204, 204, 1);
        margin-top: 10px;

        /deep/ .ivu-form .ivu-form-item-label {
            font-size: 12px;
        }
        /deep/ .ivu-form-item-error-tip {
            font-size: 12px;
        }

        /deep/ .ivu-form-item {
            margin-bottom: 20px;
            &:last-child {
                margin-bottom: 0;
            }
        }
    }

    .receipt {
        /deep/ .ivu-modal-body {
            padding: 0 40px 15px 40px;
        }
        /deep/ .ivu-modal-footer {
            height: 74px;
            padding: 7px 18px 12px 18px;
        }
        .receipt_mold {
            width: 100%;
            display: flex;
            flex-wrap: wrap;

            .mold_item {
                height: 30px;
                border-radius: 1px;
                text-align: center;
                line-height: 30px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                border: 1px solid rgba(204, 204, 204, 1);
                position: relative;
                border-radius: 1px;
                margin: 0 10px 10px 0;
                padding: 0 12px;
                cursor: pointer;

                img {
                    width: 12px;
                    height: 12px;
                    position: absolute;
                    bottom: -1px;
                    right: -1px;
                }
            }

            .mold_itemActive {
                color: rgba(51, 51, 51, 1);
                border: 1px solid rgba(221, 38, 25, 1);
            }

            .mold_item:nth-child(4) {
                margin-right: 0;
            }
        }

        .receipt_type {
            width: 100%;
            display: flex;
        }

        .commenTitle {
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222;
            line-height: 30px;
            margin-right: 18px;
        }

        .commentDiv {
            width: 74px;
            height: 30px;
            margin-right: 10px;
            border-radius: 1px;
            border: 1px solid rgba(204, 204, 204, 1);
            text-align: center;
            line-height: 30px;
            color: rgba(102, 102, 102, 1);
            font-size: 12px;
            position: relative;
            cursor: pointer;

            img {
                width: 12px;
                height: 12px;
                position: absolute;
                bottom: -1px;
                right: -1px;
            }
        }

        .mold_itemActive {
            color: rgba(51, 51, 51, 1);
            border: 1px solid rgba(221, 38, 25, 1);
        }
    }
    .focusShodowNone {
        /deep/ input:focus {
            box-shadow: 0 0 0 0;
            border-color: #b7b7b7;
        }
        /deep/ textarea:focus {
            box-shadow: 0 0 0 0;
            border-color: #e8e8e8;
        }
    }
    .addAdress {
        /deep/ .ivu-modal-body {
            padding: 30px 26px 20px 26px;
        }

        /deep/ .ivu-form-item-content {
            width: 210px;
        }

        /deep/ .ivu-form-item:nth-child(3) {
            .ivu-form-item-content {
                width: 405px;
            }
        }

        /deep/ .ivu-form-item:nth-child(6) {
            .ivu-form-item-content {
                width: 480px;
            }
        }

        .tiptype {
            width: 100%;
            display: flex;
            flex-direction: column;

            .tipList {
                width: 100%;
                display: flex;

                span {
                    width: 80px;
                    height: 34px;
                    border-radius: 1px;
                    border: 1px solid rgba(204, 204, 204, 1);
                    text-align: center;
                    line-height: 34px;
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(51, 51, 51, 1);
                    margin-right: 22px;
                    display: inline-block;
                    cursor: pointer;
                    margin-bottom: 14px;
                }
            }

            .adressSet {
                width: 100%;
                display: flex;
                // margin-top: 24px;
            }
        }
    }

    .couponModal {
        .coupon {
            width: 100%;

            .useful {
                width: 100%;
                display: flex;
                flex-wrap: wrap;

                .listItem {
                    width: 280px;
                    height: 70px;
                    display: flex;
                    margin-right: 29px;
                    margin-bottom: 28px;
                    cursor: pointer;

                    .listItem_left {
                        width: 100px;
                        height: 70px;
                        padding: 5px 0;
                        background: url("/img/04.shoppingCar/coupon1.png") no-repeat
                            center/cover;
                        display: flex;
                        justify-content: center;
                        flex-direction: column;

                        .listItem_left_top {
                            width: 100%;
                            display: flex;
                            justify-content: center;
                            align-items: flex-end;
                            color: #fff;
                            font-size: 26px;
                            margin: 0 0 0 -1px;
                        }

                        .listItem_left_bottom {
                            width: 100%;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            color: #fff;
                            font-size: 12px;
                            margin: 2px 0 0 1px;
                        }
                    }

                    .other {
                        background: url("/img/04.shoppingCar/coupon2.png") no-repeat
                            center/cover;
                    }

                    .listItem_right {
                        max-width: 180px;
                        flex: 1;
                        display: flex;
                        flex-direction: column;
                        padding: 5px 10px;
                        position: relative;
                        border: 1px solid rgba(235, 235, 235, 1);
                        border-left: none;

                        .check {
                            position: absolute;
                            width: 22px;
                            height: 22px;
                            bottom: 0;
                            right: 0;
                        }

                        div {
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(51, 51, 51, 1);
                        }
                    }
                }

                .listItem:nth-child(2n + 2) {
                    margin-right: 0;
                }
            }
        }

        /deep/ .ivu-modal-body {
            padding: 0 20px;
        }
    }

    .receiptNew {
        width: 100%;
        margin: 0 0 35px 0;

        .receiptNew_title {
            width: 100%;
            font-size: 13px;
            font-weight: bold;
            line-height: 22px;
        }

        .receiptNew_content {
            width: 100%;
            font-size: 13px;
            font-weight: 400;
            line-height: 22px;
        }
    }

    .receiptForm1 {
        /deep/ .ivu-form-item-content {
            width: 327px;
        }
    }

    .receiptForm2 {
        /deep/ .ivu-form-item:nth-child(3) {
            .ivu-form-item-label::before {
                content: "";
            }
        }

        /deep/ .ivu-form-item:nth-child(4) {
            .ivu-form-item-label::before {
                content: "";
            }
        }
    }

    .againShopping {
        .problemItem {
            width: 100%;
            padding-top: 20px;

            .title {
                width: 100%;
                font-size: 14px;
                font-weight: 400;
                color: rgba(221, 38, 25, 1);
                padding: 0 11px;
                padding-bottom: 10px;
            }

            .item {
                padding: 11px;
                width: 100%;
                border-bottom: 1px dashed #f0f0f0;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .item_left {
                    width: 90%;
                    display: flex;

                    .item_content {
                        width: 60%;
                        display: flex;
                        flex-direction: column;
                        justify-content: space-around;

                        .words {
                            width: 100%;
                            text-overflow: -o-ellipsis-lastline;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            display: -webkit-box;
                            -webkit-line-clamp: 2;
                            line-clamp: 2;
                            -webkit-box-orient: vertical;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(51, 51, 51, 1);
                        }

                        .guige {
                            width: 100%;
                            display: flex;
                            //     justify-content: space-between;

                            .guige_left {
                                font-size: 13px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: rgba(102, 102, 102, 1);
                            }

                            .guige_right {
                                font-size: 15px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 600;
                                color: rgba(221, 38, 25, 1);
                            }
                        }
                    }
                }

                &:last-child {
                    border: none;
                }
            }
        }
    }
    /deep/ .ivu-table-tbody tr:hover td {
        background-color: white;
    }
    /deep/ .cartGoodsListTable thead th {
        height: 40px;
        background-color: #eeeeee;
        span {
            color: rgba(34, 34, 34, 1);
        }
    }
    /deep/ .ivu-modal-footer {
        button:nth-of-type(1) {
            margin-right: 20px;
        }
    }
    .receiptCon {
        /deep/ .ivu-checkbox-wrapper {
            font-size: 12px;
            .ivu-checkbox-inner {
                width: 12px;
                height: 12px;
                &:after {
                    top: 0;
                    left: 3px;
                }
            }
        }
    }
    .textarea-14 /deep/ .ivu-input {
        font-size: 12px;
    }
    // /deep/ .ivu-modal-close{
    //     width: 16px;
    //     height: 16px;
    //     background-color: red;
    //     .ivu-icon {
    //         color: blue;
    //     }
    // }
    /deep/ .ivu-input-icon-clear {
        cursor: pointer;
    }
    /deep/ .ivu-input:hover {
        border-color: #dcdee2;
    }
    /deep/ .ivu-modal-close {
        right: 12px;
        top: 9px;
        .ivu-icon-ios-close {
            width: 30px !important;
            height: 30px !important;
            font-size: 36px !important;
            &:hover {
                color: #444 !important;
            }
        }
    }
    .text-area-color {
        /deep/ .ivu-input {
            color: #222 !important;
        }
    }
    .commenBox .testRequired {
        .ivu-form-item-label:after {
            content: "*" !important;
            display: inline-block;
            margin-right: 4px;
            line-height: 1;
            font-family: SimSun;
            font-size: 14px;
            color: #ed4014;
        }
    }
    .main {
        background: rgba(246, 246, 246, 1);
        width: 100%;
        padding-bottom: 80px;
    }
    .container {
        width: 1200px;
        margin: auto;
        padding: 0 21px;
        background: rgba(255, 255, 255, 1);
    }
    .payCon {
        padding: 26px 0 40px 0;
        border-bottom: 1px solid rgba(230, 230, 230, 1);
    }
    /deep/ .ivu-modal-footer .footerBtn .preserveButton {
        color: white;
        width: 60px;
        height: 30px;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
        font-size: 14px;
    }
    .virtualAddress {
        border-bottom: 1px solid #e6e6e6;
        padding: 22px 0 25px 0;
        /deep/ .ivu-input {
            border-radius: 0;
            font-size: 12px;
            color: #222;
            &:focus {
                box-shadow: 0 0 0 0;
                border-color: #b7b7b7;
            }
        }
        /deep/ .ivu-form-item-error-tip {
            font-size: 12px;
        }
    }
</style>
