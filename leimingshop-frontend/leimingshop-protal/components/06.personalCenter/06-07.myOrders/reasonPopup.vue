<template>
    <div class="reason-wrapper">
        <div class="reason-popup">
            <div class="inner">
                <p class="p1">取消订单</p>
                <p class="p2">
                    取消订单后，本单享有的优惠可能会一并取消，是否继续？
                </p>
                <p class="p3">请选择取消订单的原因（必选）：</p>
                <div
                    class="error"
                    @click="cancel"
                    @mouseout="mouseout"
                    @mouseover="mouseover"
                >
                    <img :src="error" alt />
                </div>
                <ul class="radio-group">
                    <li
                        class="radio-li"
                        v-for="(item, index) in reasonList"
                        @click="selectLi(index)"
                        name="radio-li"
                    >
                        {{ item.content }}
                    </li>
                </ul>
                <div class="button-group">
                    <div class="btn1" @click="cancel">取消</div>
                    <div @click="confirm" class="btn2">确定</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { OrderCancel } from "@/api/api_06-07-01personalMyOrders.js";
    export default {
        components: {},
        props: {},
        data() {
            return {
                border: "",
                error: "/img/06.personalCenter/error.png",
                reasonList: [],
                id: "",
                currentRadioId: "",
                radioList: [],
            };
        },
        watch: {},
        computed: {},
        methods: {
            selectLi(index) {
                this.radioList.forEach((item, sort) => {
                    item.style.color = "#333333";
                    item.style.border = "1px solid rgba(204,204,204,1)";
                });
                this.reasonList.forEach((item, index1) => {
                    if (index == index1) {
                        this.radioList[index1].style.color = "#dd2619";
                        this.radioList[index1].style.border = "1px solid #dd2619";
                        this.currentRadioId = this.reasonList[index1].id;
                    }
                });
            },
            mouseout() {
                this.error = "/img/06.personalCenter/error.png";
            },
            mouseover() {
                this.error = "/img/06.personalCenter/errorred.png";
            },
            confirm() {
                if (this.currentRadioId == "") {
                    this.$Message.warning("请您选择取消订单的理由！");
                    return;
                }
                OrderCancel({ id: this.id, reasonId: this.currentRadioId })
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.$Message.success("取消订单成功！");
                            this.$store.state.myOrders.reasonList = [];
                            this.$emit("getDataList");
                            if (this.$route.name == "personalCenterBase-myOrdersDetail") {
                                this.$router.push({
                                    path: "/personalCenterBase/myOrders",
                                });
                            }
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //取消订单原因弹层
            cancel() {
                this.$store.state.myOrders.reasonList = [];
            },
        },
        created() {},
        mounted() {
            console.log(this.$route.name);
            this.reasonList = this.$store.state.myOrders.reasonList;
            this.id = this.$store.state.myOrders.id;
            //得到取消订单的理由li
            this.radioList = document.getElementsByName("radio-li");
        },
    };
</script>
<style scoped lang="scss">
    .reason-wrapper /deep/ {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.1);
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 1000;
        .ivu-radio {
            // display: none;
        }
        .p1 {
            font-size: 16px;
            color: #333333;
            font-weight: 500;
            text-align: center;
            font-family: PingFangSC-Medium, PingFang SC;
        }
        .p2 {
            height: 14px;
            line-height: 14px;
            font-size: 14px;
            color: #333333;
            font-weight: 400;
            margin-top: 20px;
            font-family: PingFangSC-Regular, PingFang SC;
        }
        .p3 {
            height: 14px;
            line-height: 14px;
            font-size: 14px;
            color: #666666;
            font-weight: 400;
            margin-top: 10px;
            font-family: PingFangSC-Regular, PingFang SC;
        }
        .reason-popup {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: #ffffff;
            border: 10px solid rgba(0, 0, 0, 0.2);
            border-radius: 10px;
        }
    }
    .inner {
        border-radius: 10px;
        // padding: 20px 75px 40px 75px;
        padding: 30px;
        position: relative;
    }
    .error {
        position: absolute;
        right: 10px;
        top: 10px;
    }
    .button-group {
        margin-top: 20px;
        text-align: center;
        display: flex;
        justify-content: center;
        .btn1 {
            width: 60px;
            height: 30px;
            border-radius: 2px;
            text-align: center;
            line-height: 30px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            color: #333333;
            border-radius: 1px;
            background: #f5f5f5;
            cursor: pointer;
            &:hover {
                background: #dd2619;
                color: #ffffff;
            }
        }
        .btn2 {
            width: 60px;
            height: 30px;
            border-radius: 2px;
            text-align: center;
            line-height: 30px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            color: #ffffff;
            border-radius: 1px;
            background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
            margin-left: 26px;
            cursor: pointer;
        }
    }
    .radio-group {
        width: 410px;
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        margin-top: 20px;
        li {
            width: 195px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #333333;
            border-radius: 1px;
            border: 1px solid rgba(204, 204, 204, 1);
            text-align: center;
            margin: 10px auto 0;
            cursor: pointer;
        }
    }
</style>
