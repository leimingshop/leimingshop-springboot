<template>
    <div class="to-pay">
        <!-- 面包屑 -->
        <breadcrumb1 :list="breadcrumbList" />
        <div class="to-pay-body">
            <div class="head-word">
                <img
                    src="/img/04.shoppingCar/pay_icon.png"
                    class="head-left-icon"
                />
                <div>{{ msg }}</div>
            </div>
            <p class="serve-word">
                页面将在{{ count }}秒之内跳转，如未跳转请点击下方按钮
            </p>
            <!-- <div class="serve-message">温馨提示：雷铭商城不会以订单异常、系统升级为由要求您点击任何网址链接进行退款操作。</div> -->
            <div class="bottom-button">
                <a class="red-button" @click="$router.push({ path: '/' })"
                    >进入首页</a
                >
            </div>
        </div>
    </div>
</template>

<script>
    import { memberBindEmail } from "@/api/api_09login.js";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    const TIME_COUNT = 10;
    export default {
        components: {
            breadcrumb1,
        },
        head() {
            return {
                title: "绑定邮箱",
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
                msg: "",
                count: "",
                timer: null,
                breadcrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "绑定成功",
                        toPath: "",
                    },
                ]),
            };
        },
        mounted() {
            this.bindEmail();
        },
        methods: {
            bindEmail() {
                memberBindEmail({
                    code: this.$route.query.code,
                    memberId: this.$route.query.memberId,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.msg = "绑定成功";
                        } else {
                            this.msg = res.msg;
                        }
                        this.timeClock(); //计时器
                    })
                    .catch((err) => console.log(err));
            },
            timeClock() {
                this.count = TIME_COUNT;
                this.showCode = false;
                this.timer = setInterval(() => {
                    if (this.count > 0 && this.count <= TIME_COUNT) {
                        this.count--;
                    } else {
                        clearInterval(this.timer);
                        this.timer = null;
                        this.$router.push({
                            path: "/",
                        });
                    }
                }, 1000);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .to-pay {
        width: 100%;
        height: 842px;
        background-color: #f6f6f6;
        margin: 0 auto;

        .ivu-breadcrumb {
            width: 1200px;
            margin: 0 auto;
            line-height: 52px;
            span {
                font-size: 13px;
            }
        }

        .to-pay-body {
            width: 1200px;
            height: 710px;
            background-color: #fff;
            margin: 0 auto;
            overflow: hidden;

            .head-word {
                width: 660px;
                margin: 120px auto 0 auto;
                font-size: 38px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(19, 174, 39, 1);
                line-height: 38px;
                display: flex;

                img {
                    margin: 0 13px 0 0;
                }
            }

            .serve-word {
                width: 660px;
                margin: 18px auto 32px auto;
                font-size: 28px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #333;
                line-height: 28px;
            }

            .order-info {
                width: 660px;
                margin: 12px auto 0 auto;
                display: flex;
                font-size: 22px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba#666;
                line-height: 30px;
            }

            .order-info span:last-child {
                margin: 0 0 0 40px;
                color: #333;
            }

            .serve-message {
                width: 660px;
                margin: 12px auto 0 auto;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                line-height: 22px;
            }

            .bottom-button {
                width: 536px;
                margin: 108px auto 0 auto;
                display: flex;
                justify-content: space-between;

                .red-button {
                    width: 210px;
                    height: 60px;
                    background-color: #dd2619;
                    font-size: 24px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(255, 255, 255, 1);
                    line-height: 60px;
                    text-align: center;
                }

                .red-button:hover {
                    background-color: #e45147;
                }

                .gray-button {
                    width: 210px;
                    height: 60px;
                    background-color: #f5f5f5;
                    font-size: 24px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(102, 102, 102, 1);
                    line-height: 60px;
                    text-align: center;
                }

                .gray-button:hover {
                    background-color: #fafafa;
                }
            }
        }
    }
</style>
