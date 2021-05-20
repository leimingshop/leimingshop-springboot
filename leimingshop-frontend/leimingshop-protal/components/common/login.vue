<template>
    <div class="content-box">
        <div class="content-tab">
            <a
                v-for="(tab, index) in tabs"
                :class="{ cur: iscur == index }"
                class="tab-a"
                @click="(iscur = index), tabChange('select' + (index + 1))"
                :key="index"
            >
                <div class="word">{{ tab.name }}</div>
                <div class="line"></div>
            </a>
        </div>
        <component :is="tabView"></component>
    </div>
</template>

<script>
    import select1 from "@/components/09.login/select01.vue";
    import select2 from "@/components/09.login/select02.vue";
    import Cookies from "js-cookie";
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        data() {
            return {
                tabView: "select1",
                iscur: 0,
                tabs: [
                    {
                        name: "密码登录",
                    },
                    {
                        name: "短信登录",
                    },
                ],
            };
        },
        components: {
            select1,
            select2,
        },
        computed: {
            wechatState() {
                return this.$store.getters["login/wechatState"];
            },
        },
        mounted() {
            if (this.$route.query.code) {
                this.getOpenID({
                    code: this.$route.query.code,
                });
            }
        },
        methods: {
            ...mapState("login", ["wechat_state"]),
            ...mapActions("login", ["getOpenID"]),

            tabChange(tab) {
                this.tabView = tab;
            },
        },
        watch: {
            wechatState(val, oldval) {
                if (val.code == 505) {
                    this.$Message.info(val.msg);
                } else if (val.code == 1017) {
                    this.$router.push({
                        path: "/bindPhone",
                    });
                } else if (val.code == 200) {
                    Cookies.set("auth", val.data.token);
                    this.$router.push({
                        path: "/",
                    });
                } else {
                    this.$Message.info("服务器出错");
                }
            },
        },
    };
</script>


<style lang="scss" scoped>
    .content-box {
        width: 100%;
        height: 100%;
        padding: 30px 0 0 0;
    }

    .content-tab {
        width: 100%;
        display: flex;
        justify-content: space-between;
        padding: 0 18%;
    }

    .tab-a {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #222222;
        line-height: 16px;
    }

    .cur {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: #dd2619;
        line-height: 16px;

        .line {
            width: 22px;
            height: 3px;
            background-color: red;
            border-radius: 3px;
            margin: 11px auto 5px auto;
        }
    }
    /deep/ .ivu-input-wrapper:hover .ivu-input-icon-clear {
        line-height: 46px;
    }
</style>
