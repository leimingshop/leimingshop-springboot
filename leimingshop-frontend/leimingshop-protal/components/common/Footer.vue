<template>
    <footer class="footer">
        <div class="service-slogon-body">
            <div class="service-slogon">
                <div class="circle-word">保</div>
                <div>
                    <div class="right-title">雷铭品质保证</div>
                    <div class="right-content">正品保证 假一赔十</div>
                </div>
            </div>
            <div class="service-slogon">
                <div class="circle-word">正</div>
                <div>
                    <div class="right-title">正品行货 精致服务</div>
                    <div class="right-content">正品保证 假一赔十</div>
                </div>
            </div>
            <div class="service-slogon">
                <div class="circle-word">全</div>
                <div>
                    <div class="right-title">品类齐全 轻松购</div>
                    <div class="right-content">正品保证 假一赔十</div>
                </div>
            </div>
            <div class="service-slogon">
                <div class="circle-word">退</div>
                <div>
                    <div class="right-title">15天无忧退换货</div>
                    <div class="right-content">正品保证 假一赔十</div>
                </div>
            </div>
        </div>

        <div class="service-hotline">
            <!-- <div class="clearfix"></div> -->
            <div class="follow">
                <div class="shopping">
                    <!-- <img v-lazy="'/static/img/01.index/leiming.png'" alt=""> -->
                    <logoImg></logoImg>
                </div>
                <div class="follow-ours">
                    <div class="ours">关注我们</div>
                    <div class="icons">
                        <div class="icons1">
                            <div class="artAppDown1">
                                <img
                                    v-lazy="'/img/01.index/MobileQRCode.png'"
                                />
                            </div>
                        </div>
                        <div class="icons2">
                            <div class="artAppDown2">
                                <img
                                    v-lazy="'/img/01.index/MobileQRCode.png'"
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="customer">
                <ul>
                    <li>客户服务</li>
                    <li>
                        <a href="#">在线客服</a>
                    </li>
                    <li>
                        <a href="#">问题反馈</a>
                    </li>
                </ul>
            </div>

            <div class="time">
                <ul>
                    <li>服务热线</li>
                    <li>400-0000-000</li>
                    <li>周一至周日 8:00-18:00</li>
                    <li>(仅收市话费)</li>
                </ul>
            </div>

            <div class="download">
                <div class="download-app">下载APP</div>
                <div class="app">
                    <div class="app-img">
                        <img src="/img/01.index/appone.png" alt="" />
                    </div>
                    <div class="app-right">
                        <p>扫描二维码查看</p>
                        <p>移动版原型效果</p>
                        <p>下载页面</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="service-intro">
            <div class="clearfix"></div>
            <div class="friend-link">
                <div class="friend-link-item">
                    <ul>
                        <li v-for="(link, index) in moreLink" :key="index">
                            <span
                                :class="[
                                    linkItemClass,
                                    { 'link-last-item': index === 9 },
                                ]"
                                >{{ link }}</span
                            >
                        </li>
                    </ul>
                </div>
            </div>
            <div class="copyright">
                <p>Copyright&nbsp;&nbsp;©2018&nbsp;&nbsp;北京雷铭智信科技有限公司 &nbsp; 本公司保留一切版权所有 &nbsp;|&nbsp;<a href="http://beian.miit.gov.cn/" target="_blank">京ICP备12000912号-1</a></p>
            </div>
        </div>
    </footer>
</template>

<script>
    import logoImg from "@/components/common/logoImg.vue";
    export default {
        name: "Footer",
        components: {
            logoImg,
        },
        data() {
            return {
                linkItemClass: "link-item",
                guideArr: [
                    [
                        "购物指南",
                        "购物流程",
                        "会员介绍",
                        "生活旅行",
                        "常见问题",
                        "大家电",
                        "联系客服",
                    ],
                    [
                        "配送方式",
                        "上门自提",
                        "211限时达",
                        "配送服务查询",
                        "收取标准",
                        "海外配送",
                    ],
                    [
                        "支付方式",
                        "货到付款",
                        "在线支付",
                        "分期付款",
                        "邮局汇款",
                        "公司转账",
                    ],
                    [
                        "售后服务",
                        "售后政策",
                        "价格保护",
                        "退款说明",
                        "返修/退换货",
                        "取消订单",
                    ],
                ],
                moreLink: [
                    "关于我们",
                    "帮助中心",
                    "售后服务",
                    "配送与验收",
                    "商务合作",
                    "企业采购",
                    "开放平台",
                    "搜索推荐",
                    "友情链接",
                ],
            };
        },
    };
</script>

<style lang="scss" scoped>
    /*****************************底 部 开 始*****************************/

    a {
        text-decoration: none;
        color: rgba(51, 51, 51, 1);
    }
    ul li {
        list-style: none;
    }

    .footer {
        width: 100%;

        .service-slogon-body {
            width: 100%;
            background-color: #ffffff;
            // border-top:  1px solid #E9E7E7;
            border-bottom: 1px solid #e9e7e7;
            display: flex;
            justify-content: center;

            .service-slogon {
                width: 300px;
                height: 108px;
                display: flex;
                justify-content: center;
                align-items: center;

                .circle-word {
                    width: 48px;
                    height: 48px;
                    background: rgba(255, 255, 255, 1);
                    border: 1px solid rgba(214, 214, 214, 1);
                    border-radius: 50%;
                    margin: 0 14px 0 0;
                    font-size: 20px;
                    font-family: PingFangSC-Semibold, PingFang SC;
                    font-weight: 600;
                    color: rgba(68, 68, 68, 1);
                    line-height: 45px;
                    text-align: center;
                }

                .right-title {
                    margin: 0 0 6px 0;
                    font-size: 18px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: #222222;
                    line-height: 18px;
                }

                .right-content {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #666;
                    line-height: 14px;
                }
            }
        }

        .service-hotline {
            width: 100%;
            height: 212px;
            box-sizing: border-box;
            display: flex;
            justify-content: center;

            .follow {
                width: 300px;

                .shopping {
                    max-width: 100%;
                    height: auto;
                    margin: 40px 11px 0 60px;
                    img {
                        width: 100%;
                        height: 100%;
                    }
                }

                .follow-ours {
                    width: 229px;
                    height: 34px;
                    line-height: 34px;
                    margin: 39px 0 0 62px;

                    .ours {
                        display: inline-block;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(68, 68, 68, 1);
                        margin-right: 29px;
                    }

                    .icons {
                        height: 34px;
                        display: inline-block;

                        .icons1 {
                            display: inline-block;
                            width: 34px;
                            height: 34px;
                            background: url("/img/01.index/weibobig.png") no-repeat
                                center;
                            background-size: 100% 100%;
                            vertical-align: middle;
                            margin-right: 21px;
                        }

                        .icons2 {
                            display: inline-block;
                            width: 34px;
                            height: 34px;
                            background: url("/img/01.index/weixinbig.png") no-repeat
                                center;
                            background-size: 100% 100%;
                            vertical-align: middle;
                        }
                        .icons1:hover {
                            cursor: pointer;
                            background: url("/img/01.index/xuanweibo.png") no-repeat;
                            background-size: 100% 100%;
                            vertical-align: middle;
                        }
                        .icons2:hover {
                            cursor: pointer;
                            background: url("/img/01.index/xuanweixin.png")
                                no-repeat;
                            background-size: 100% 100%;
                            vertical-align: middle;
                        }
                    }
                }
            }
            .customer {
                width: 300px;
                ul {
                    margin-top: 45px;
                    margin-left: 93px;
                    li:first-child {
                        height: 25px;
                        font-size: 18px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222222;
                        line-height: 25px;
                        margin-bottom: 15px;
                    }
                    li:nth-child(2),
                    li:last-child {
                        a {
                            height: 20px;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #222;
                            line-height: 20px;
                            &:hover {
                                color: #dd2619;
                            }
                        }
                    }
                    li:nth-child(2) {
                        margin-bottom: 10px;
                    }
                }
            }
            .time {
                width: 300px;
                ul {
                    margin-top: 45px;
                    li:first-child {
                        height: 25px;
                        font-size: 18px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222;
                        line-height: 25px;
                        margin-bottom: 15px;
                    }
                    li:nth-child(2) {
                        height: 18px;
                        font-size: 18px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: #222;
                        line-height: 18px;
                        margin-bottom: 12px;
                    }
                    li:nth-child(3) {
                        height: 14px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222;
                        line-height: 14px;
                        margin-bottom: 13px;
                    }
                    li:last-child {
                        height: 14px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222;
                        line-height: 14px;
                    }
                }
            }
            .download {
                width: 300px;
                .download-app {
                    margin-top: 46px;
                    height: 25px;
                    font-size: 18px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #222;
                    line-height: 25px;
                    margin-bottom: 5px;
                }
                .app {
                    height: 98px;
                    .app-img {
                        display: inline-block;
                        width: 98px;
                        height: 98px;
                        border: 1px solid rgba(234, 234, 234, 1);
                        padding: 3px 2px 2px 3px;
                        vertical-align: top;
                        img {
                            width: 100%;
                            height: 100%;
                        }
                    }
                    .app-right {
                        display: inline-block;
                        height: 98px;
                        margin-left: 7px;
                        p:first-child,
                        p:nth-child(2) {
                            height: 17px;
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #222;
                            line-height: 17px;
                        }
                        p:first-child {
                            margin-bottom: 6px;
                        }
                        p:last-child {
                            width: 82px;
                            height: 30px;
                            border-radius: 4px;
                            margin-top: 20px;
                            border: 1px solid rgba(234, 234, 234, 1);
                            text-align: center;
                            line-height: 30px;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: rgba(224, 63, 53, 1);
                            cursor: pointer;
                        }
                        p:last-child:hover {
                            width: 82px;
                            height: 30px;
                            background-color: rgba(224, 63, 53, 1);
                            border-radius: 4px;
                            margin-top: 20px;
                            border: 1px solid rgba(234, 234, 234, 1);
                            text-align: center;
                            line-height: 30px;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #ffffff;
                            cursor: pointer;
                        }
                    }
                }
            }
        }

        .icons1 {
            position: relative;
            .artAppDown1 {
                width: 100px;
                height: 100px;
                position: absolute;
                top: 42px;
                left: -30px;
                display: none;
                z-index: 100;
                img {
                    width: 100%;
                    height: 100%;
                }
            }
            &:hover > .artAppDown1 {
                display: block;
            }
        }

        .icons2 {
            position: relative;
            .artAppDown2 {
                width: 100px;
                height: 100px;
                position: absolute;
                top: 42px;
                left: -30px;
                display: none;
                z-index: 100;
                img {
                    width: 100%;
                    height: 100%;
                }
            }
            &:hover > .artAppDown2 {
                display: block;
            }
        }

        .service-intro {
            width: 100%;

            .friend-link {
                width: 100%;
                background-color: #333;
                padding: 20px 0 0 0;
                margin: 0px auto;
                color: #919191;
                display: flex;
                align-items: center;

                .friend-link-item {
                    width: 100%;
                }

                .friend-link-item ul {
                    width: 745px;
                    margin: 0 auto;
                    list-style: none;
                    display: flex;
                    justify-content: space-between;
                }

                .friend-link-item li {
                    padding: 5px 0px;
                    &:hover {
                        color: #fff;
                    }
                }

                .link-item {
                    cursor: pointer;
                }

                .link-last-item {
                    border: none;
                }
            }

            .copyright {
                width: 100%;
                background-color: #333;
                padding: 0 0 20px 0;
                color: #919191;
                line-height: 30px;
                text-align: center;

                a {
                    color: #919191;
                }
            }
        }
    }
</style>
