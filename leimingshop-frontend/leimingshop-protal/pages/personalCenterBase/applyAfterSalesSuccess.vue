<template>
    <div class="applySuccess">
        <div class="a-header">
            <h2 v-text="applyTitle"></h2>
        </div>

        <div class="a-content">
            <i class="hint-icon"></i>
            <h4 v-text="title"></h4>
            <h6 v-text="description"></h6>
            <Button type="default" @click="handleBack">{{ btnText }}</Button>
        </div>

        <div class="a-footer">
            <h4 v-text="aFooter.title"></h4>
            <ul>
                <li
                    v-for="(item, index) in aFooter.list"
                    :key="item.slice(0, 3)"
                    v-text="item"
                ></li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        name: "applySuccess",
        head() {
            return {
                title: "申请售后成功",
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
                applyTitle: this.$route.query.title,
                title: "售后申请提交成功",
                description: "售后专员可能与您电话沟通，请保持手机畅通！",
                btnText: "返回列表",
                aFooter: {
                    title: "温馨提示：",
                    list: [
                        "• 商品寄回地址将在审核通过后以短信形式告知，或在申请记录中查询。",
                        "• 提交服务单后，售后专员可能与您电话沟通，请保持手机畅通。",
                        "• 退货处理成功后退款金额将原路返回到您的支付账户中。",
                    ],
                },
            };
        },

        components: {},
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            handleBack() {
                this.$router.replace({
                    path: "/personalCenterBase/afterSale",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .applySuccess {
        background: #ffffff;
    }

    .a-header {
        height: 56px;
        line-height: 56px;
        border-bottom: 1px solid #ebebeb;
        padding: 0 0 0 40px;
        h2 {
            color: #333333;
            font-size: 14px;
            font-weight: bold;
        }
    }

    .a-content {
        text-align: center;
        margin: 142px 0 372px;
        .hint-icon {
            width: 60px;
            height: 60px;
            display: inline-block;
            background: url("/img/06.personalCenter/success@2x.png") center/cover;
        }

        h4 {
            font-size: 14px;
            font-weight: bold;
            color: #222222;
            margin: 20px 0 10px;
        }

        h6 {
            font-size: 12px;
            color: #999999;
        }

        button {
            width: 80px;
            height: 30px;
            border-radius: 22px;
            border: 1px solid #cccccc;
            color: #666666;
            font-size: 12px;
            margin-top: 20px;
            &:hover {
                background: $primary-color;
                color: #ffffff;
                border-color: $primary-color;
            }
        }
    }

    .a-footer {
        margin: 0 40px;
        padding: 40px 0;
        border-top: 1px solid #ebebeb;
        h4 {
            color: #333333;
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 12px;
        }
        ul {
            color: #666666;
            font-size: 13px;
        }
    }
</style>
