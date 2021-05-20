<template>
    <div class="orderDetailInfo">
        <div class="logistics-info">
            <div class="logistics-top">物流信息</div>
            <div class="logistics-content">
                <div class="logistics-left">
                    <ul>
                        <li>
                            <img src="/img/06.personalCenter/psfs.png" alt />
                            <div>
                                配送方式：
                                <span v-if="detailData.devlierType">{{
                                    detailData.devlierType | logistics
                                }}</span>
                                <span v-else>暂无</span>
                            </div>
                        </li>
                        <li>
                            <img src="/img/06.personalCenter/kddh.png" alt />
                            <div>
                                快递单号：
                                <!-- <span v-if="detailData.orderLogisticsDTO&&detailData.orderLogisticsDTO.nu">{{detailData.orderLogisticsDTO.nu}}</span>-->
                                <span v-if="detailData.transportCode">{{
                                    detailData.transportCode
                                }}</span>
                                <span v-else>暂无</span>
                            </div>
                        </li>
                        <li>
                            <img src="/img/06.personalCenter/iphone.png" alt />
                            <div>
                                联系电话：
                                <span
                                    v-if="
                                        detailData.orderLogisticsDTO &&
                                        detailData.orderLogisticsDTO
                                            .companyPhone
                                    "
                                    >{{
                                        detailData.orderLogisticsDTO
                                            .companyPhone
                                    }}</span
                                >
                                <span v-else>暂无</span>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="logistics-right" v-show="logisticsStatus == 1">
                    <ul class="logistics-process">
                        <li
                            v-for="(val, key, index) in logisticsMap"
                            :key="index"
                        >
                            <div class="line"></div>
                            <p class="process-top">{{ key }}</p>
                            <div class="process-bottom">
                                <div
                                    v-for="(item, index) in val"
                                    class="process-row"
                                >
                                    <span class="process-time">{{
                                        item.time | filterTime
                                    }}</span>
                                    <span class="circle"></span>
                                    <span class="process-descriction">{{
                                        item.context
                                    }}</span>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="nologistics-right" v-show="logisticsStatus == 0">
                    暂无物流信息
                </div>
            </div>
        </div>
        <div style="display: flex; justify-content: space-between">
            <div class="receive-info">
                <div class="receive1">收货信息</div>
                <div class="receive2">
                    <img src="/img/06.personalCenter/shr.png" alt />
                    <span style="margin-left: 10px">收货人：</span>
                    <span v-if="orderAddressDTO.trueName">{{
                        orderAddressDTO.trueName
                    }}</span>
                    <span v-else>暂无</span>
                </div>
                <div class="receive3">
                    <img src="/img/06.personalCenter/lxfs.png" alt />
                    <span style="margin-left: 10px">联系方式：</span>
                    <span v-if="orderAddressDTO.mobPhone">{{
                        orderAddressDTO.mobPhone
                    }}</span>
                    <span v-else>暂无</span>
                </div>
                <div class="receive4">
                    <img src="/img/06.personalCenter/shdz.png" alt />
                    <span style="margin-left: 10px; width: 62px"
                        >收货地址：</span
                    >
                    <span
                        v-if="
                            orderAddressDTO.areaInfo || orderAddressDTO.address
                        "
                        >{{ orderAddressDTO.areaInfo }}
                        {{ orderAddressDTO.address }}</span
                    >
                    <span v-else>暂无</span>
                </div>
            </div>
            <div class="invoice-info">
                <div class="receive1">发票信息</div>
                <div class="receive2">
                    <img src="/img/06.personalCenter/fplx.png" alt />
                    <span style="margin-left: 10px">发票类型：</span>
                    <span v-if="detailData.invoiceType"
                        >{{
                            detailData.invoiceType | filterInvoices("type1")
                        }}发票</span
                    >
                    <span v-else>暂无</span>
                </div>
                <div class="receive3">
                    <img src="/img/06.personalCenter/fpnr.png" alt />
                    <span style="margin-left: 10px">发票内容：</span>
                    <span v-if="detailData.invoiceContent">{{
                        detailData.invoiceContent | filterInvoices("type2")
                    }}</span>
                    <span v-else>暂无</span>
                </div>
                <div class="receive4">
                    <img src="/img/06.personalCenter/fptt.png" alt />
                    <span style="margin-left: 10px">发票抬头：</span>
                    <span v-if="detailData.invoiceFlag">{{
                        detailData.invoiceFlag | filterInvoices("type3")
                    }}</span>
                    <span v-else>暂无</span>
                </div>
            </div>
        </div>
        <div style="display: flex; justify-content: space-between">
            <div class="pay-method">
                <div class="pay-method1">支付方式及送货时间</div>
                <div class="pay-method2">
                    <img src="/img/06.personalCenter/money.png" alt />
                    <span style="margin-left: 10px">支付方式：</span>
                    <span v-if="detailData.paymentName">{{
                        detailData.paymentName
                    }}</span>
                    <span v-else>暂无</span>
                </div>
                <div class="pay-method3">
                    <img src="/img/06.personalCenter/shsj.png" alt />
                    <span style="margin-left: 10px">送货时间：</span>
                    <span v-if="detailData.transportTime">{{
                        detailData.transportTime
                    }}</span>
                    <span v-else>暂无</span>
                </div>
            </div>
            <div class="leave-message" v-if="detailData.orderMessage">
                <div class="leave-message1">买家留言</div>
                <!-- <div class="leave-message2">{{ detailData.orderMessage }}</div> -->
                <div class="leave-message2" v-for="(item,index) in detailData.orderDetailGoodsListDTOList" :key="index">
                        <p><span v-if="detailData.orderDetailGoodsListDTOList.length!=1">{{item.storeName}}：</span><span>{{item.orderMessage}}</span></p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        components: {},
        props: {
            detailData: {
                type: Object,
                required: true,
                default: function () {
                    return {};
                },
            },
        },
        data() {
            return {
                orderAddressDTO: {}, //收货人的相关信息
                logisticsStatus: 0, //物流状态判断 0是无物流信息 1是有物流信息
                logisticsMap: {
                //     "2020-05-05 周二": [
                //         {
                //             context:
                //                 "客户签收人: 本人签收 已签收 感谢使用圆通速递，期待再次为您服务 如有疑问请联系：13569812521，投诉电话：0373-8968113",
                //             time: "2020-05-05 15:31:45",
                //             ftime: "2020-05-05 15:31:45",
                //         },
                //         {
                //             context:
                //                 "丁栾镇308省道路西圆通速递妈妈驿站已发出自提短信请上门自提联系电话0373-8968113",
                //             time: "2020-05-05 10:31:46",
                //             ftime: "2020-05-05 10:31:46",
                //         },
                //         {
                //             context:
                //                 "快件已到达丁栾镇308省道路西圆通速递妈妈驿站联系电话0373-8968113",
                //             time: "2020-05-05 10:31:45",
                //             ftime: "2020-05-05 10:31:45",
                //         },
                //         {
                //             context:
                //                 "【河南省新乡市长垣县丁栾镇公司】 派件中 派件人: 薛康 电话 13569812521 如有疑问，请联系：0373-8968113",
                //             time: "2020-05-05 10:30:00",
                //             ftime: "2020-05-05 10:30:00",
                //         },
                //         {
                //             context: "【河南省新乡市长垣县公司】 已收入",
                //             time: "2020-05-05 08:23:45",
                //             ftime: "2020-05-05 08:23:45",
                //         },
                //     ],
                //     "2020-05-04 周一": [
                //         {
                //             context:
                //                 "【郑州转运中心】 已发出 下一站 【河南省新乡市长垣县公司】",
                //             time: "2020-05-04 12:10:42",
                //             ftime: "2020-05-04 12:10:42",
                //         },
                //         {
                //             context: "【郑州转运中心公司】 已收入",
                //             time: "2020-05-04 11:46:06",
                //             ftime: "2020-05-04 11:46:06",
                //         },
                //     ],
                //     "2020-05-03 周日": [
                //         {
                //             context:
                //                 "【河北省石家庄市鹿泉市】 已发出 下一站 【石家庄转运中心公司】",
                //             time: "2020-05-03 21:28:27",
                //             ftime: "2020-05-03 21:28:27",
                //         },
                //         {
                //             context: "【河北省石家庄市鹿泉市公司】 已打包",
                //             time: "2020-05-03 21:25:07",
                //             ftime: "2020-05-03 21:25:07",
                //         },
                //         {
                //             context:
                //                 "【河北省石家庄市鹿泉市公司】 已收件 取件人: 陈智炜 (15383116922)",
                //             time: "2020-05-03 20:07:22",
                //             ftime: "2020-05-03 20:07:22",
                //         },
                //     ],
                },
            };
        },
        watch: {},
        computed: {},
        methods: {},
        created() {},
        mounted() {
            setTimeout(() => {
                this.orderAddressDTO = this.detailData.orderAddressDTO;
                this.logisticsMap = this.detailData.logisticsMap
            }, 300);
            console.log(this.detailData);
            if (this.detailData.logisticsMap != null) {
                console.log();
                this.logisticsStatus = 1;
            }
        },
        filters: {
            filterTime(val) {
                var arr1 = [];
                arr1 = val.split(" ");
                return arr1[1];
            },
            //过滤发票内容，发票类型，抬头类型
            filterInvoices(val, type) {
                //发票类型
                if (type == "type1") {
                    switch (val) {
                        case 1:
                            return "电子";
                            break;
                        case 2:
                            return "纸质";
                            break;
                        case 3:
                            return "增值税";
                            break;
                        default:
                            return "无值";
                    }
                }
                //发票内容
                if (type == "type2") {
                    switch (val) {
                        case 1:
                            return "商品明细";
                            break;
                        case 2:
                            return "商品类别";
                            break;
                        default:
                            return "无值";
                    }
                }
                //发票抬头类型
                if (type == "type3") {
                    switch (val) {
                        case 0:
                            return "不开票";
                            break;
                        case 1:
                            return "个人";
                            break;
                        case 2:
                            return "单位";
                            break;
                        default:
                            return "无值";
                    }
                }
            },
            //物流配送方式
            logistics(val) {
                switch (val) {
                    case 1:
                        return "快递";
                        break;
                    case 2:
                        return "自提";
                        break;
                    case 3:
                        return "无需物流";
                        break;
                    case 4:
                        return "电子提货";
                        break;
                }
            },
        },
    };
</script>
<style scoped lang="scss">
    .orderDetailInfo {
        width: 100%;
        margin-top: 10px;
        .logistics-info {
            width: 100%;
            background: #ffffff;
            padding: 20px 40px;
            .logistics-top {
                font-size: 18px;
                color: #333333;
                font-weight: 500;
            }
        }
    }
    .logistics-content {
        display: flex;
        justify-content: space-between;
        .logistics-left {
            li {
                margin-top: 20px;
                display: flex;
                img {
                    width: 14px;
                    height: 14px;
                    font-size: 14px;
                    color: #666666;
                    margin-top: 1px;
                }
            }
        }
        .logistics-right {
            width: 590px;
            height: 305px;
            box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);
            margin-top: 20px;
            padding: 20px 30px;
            .process-top {
                font-size: 14px;
                color: #3a3a3a;
                font-weight: 400;
            }
            .process-bottom {
                padding-left: 52px;
                padding-bottom: 20px;
                .process-time {
                    font-size: 13px;
                    color: #7d7d7d;
                }
                .circle {
                    width: 6px;
                    height: 6px;
                    background: #eeeeee;
                    border-radius: 50%;
                    display: block;
                    margin-left: 8px;
                    margin-right: 8px;
                }
            }
        }
    }
    .process-row {
        display: flex;
        align-items: center;
        margin-top: 15px;
    }
    .logistics-process {
        height: 265px;
        overflow-y: auto;
        li {
            position: relative;
            .line {
                height: 100%;
                border-right: 1px solid #eeeeee;
                width: 1px;
                position: absolute;
                top: 0;
                left: 112px;
            }
        }
    }
    ::-webkit-scrollbar-track {
        background: rgba(0, 0, 0, 0.1);
        border-radius: 0;
    }
    ::-webkit-scrollbar {
        -webkit-appearance: none;
        width: 10px;
        height: 10px;
    }
    ::-webkit-scrollbar-thumb {
        cursor: pointer;
        border-radius: 5px;
        background: rgba(0, 0, 0, 0.25);
        //    transition：color 0.2s ease
    }
    .receive-info {
        width: 49%;
        background: #ffffff;
        margin-top: 10px;
        padding: 20px 0 40px 40px;
        .receive1 {
            font-size: 18px;
            color: #333333;
            font-weight: 500px;
        }
        .receive2,
        .receive3,
        .receive4 {
            font-size: 12px;
            color: #666666;
            font-weight: 300px;
            margin-top: 20px;
            display: flex;
            height: 12px;
            line-height: 12px;
            img {
                height: 12px;
                width: 12px;
            }
        }
    }
    .invoice-info {
        width: 49%;
        background: #ffffff;
        margin-top: 10px;
        padding: 20px 0 40px 40px;
        .receive1 {
            font-size: 18px;
            color: #333333;
            font-weight: 500px;
        }
        .receive2,
        .receive3,
        .receive4 {
            font-size: 14px;
            color: #666666;
            font-weight: 300px;
            margin-top: 20px;
            display: flex;
            height: 14px;
            line-height: 14px;
        }
    }
    .pay-method {
        width: 49%;
        background: #ffffff;
        margin-top: 10px;
        padding: 20px 0 40px 40px;
        .pay-method1 {
            font-size: 18px;
            color: #333333;
            font-weight: 500;
        }
        .pay-method2 {
            font-size: 14px;
            color: #666666;
            margin-top: 19px;
            display: flex;
            height: 14px;
            line-height: 14px;
        }
        .pay-method3 {
            font-size: 14px;
            color: #666666;
            margin-top: 19px;
            display: flex;
            height: 14px;
            line-height: 14px;
        }
    }
    .nologistics-right {
        width: 590px;
        height: 105px;
        box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.16);
        margin-top: 20px;
        padding: 20px 30px;
        font-size: 13px;
        color: #a3a3a3;
    }
    .leave-message {
        width: 49%;
        background: #ffffff;
        margin-top: 10px;
        padding: 20px 0 40px 40px;
        .leave-message1 {
            font-size: 18px;
            color: #333333;
            font-weight: 500;
        }
        .leave-message2 {
            font-size: 14px;
            color: #666666;
            margin-top: 19px;
        }
    }
</style>
