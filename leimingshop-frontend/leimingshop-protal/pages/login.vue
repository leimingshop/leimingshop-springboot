<template>
    <div class="login">
        <div class="home">
            <div class="home-head">
                <logoImg></logoImg>
                <div @mouseout="mouseOut" @mouseover="mouseOver" class="homeRight">
                    <router-link to="/" class="home-head-right">进入网站首页
                    </router-link>
                    <img :src="page" alt="" style="width:17px;height:19px;" />
                </div>
            </div>
        </div>
        <div class="content-body">
            <div class="content">
                <img class="content-bg-img" src="/img/09.login/bg.png" alt="" />
                <loginCom class="content-list"></loginCom>
            </div>
        </div>

        <div class="content-footer">
            <div class="friend-link">
                <div class="friend-link-item">
                    <ul>
                        <li v-for="(link, index) in moreLink" :key="index">
                            <span :class="[
                                    linkItemClass,
                                    { 'link-last-item': index === 9 },
                                ]">{{ link }}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="copyright">
                <p>Copyright&nbsp;&nbsp;©2018&nbsp;&nbsp;北京雷铭智信科技有限公司 &nbsp; 本公司保留一切版权所有 &nbsp;|&nbsp;<a href="http://beian.miit.gov.cn/" target="_blank">京ICP备12000912号-1</a></p>
            </div>
        </div>
        <!-- 登录页 -->
        <!-- <img v-lazy="'/static/img/' + cropImg" :key="'/static/img/' + cropImg"/> -->
        <!-- <img :src="cropImg | filterImgUrl" alt=""> -->
    </div>
</template>

<script>
    import loginCom from "@/components/common/login.vue";
    import logoImg from "@/components/common/logoImg.vue";
    export default {
        components: {
            loginCom,
            logoImg,
        },
        head() {
            return {
                title: "登录",
                meta: [
                {
                    hid: "description",
                    name: "description",
                    content: "My custom description",
                }, ],
            };
        },
        data() {
            return {
                page: "/img/01.index/grey.png",
                cropImg: "",
                dataFrom: {
                    phone: "",
                    password: "",
                },
                linkItemClass: "link-item",
                moreLink: [
                    "关于我们",
                    "联系我们",
                    "联系客服",
                    "合作招商",
                    "商家帮助",
                    "营销中心",
                    "销售联盟",
                    "校园社区",
                    "风险监测",
                    "隐私政策",
                ],
                ruleInline: {
                    phone: [
                    {
                        required: true,
                        message: "请输入手机号",
                        trigger: "blur",
                    }, ],
                    password: [
                    {
                        required: true,
                        message: "请输入登录密码.",
                        trigger: "blur",
                    },
                    {
                        type: "string",
                        min: 6,
                        message: "最少可以输入六位数密码",
                        trigger: "blur",
                    }, ],
                },
            };
        },
        methods: {
            //跳注册页面
            jumpRegister() {
                this.$router.replace({
                    path: "register",
                });
            },
            //鼠标悬浮图片变化
            mouseOut: function() {
                this.page = "/img/01.index/grey.png";
            },
            mouseOver: function() {
                this.page = "/img/01.index/redRow.png";
            },
        },
    };
</script>

<style lang="scss" scoped>
    .login {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
    }

    //顶部部分
    .home {
        width: 100%;
        background: #fff;

        .home-head {
            width: 1200px;
            height: 100px;
            line-height: 100px;
            margin: 0 auto;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            overflow: hidden;

            .home-head-right {
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 16px;
                text-shadow: 0px 2px 6px rgba(221, 217, 217, 0.31);

                &:hover {
                    color: #dd2619;
                }

                img {
                    width: 9px;
                    height: 9px;
                }
            }

            img {
                width: 219px;
                height: auto;
            }
        }
    }

    .homeRight {
        width: 115px;
        height: 21px;
        line-height: 21px;
        display: flex;
        &:hover{
            .home-head-right{
                color: #dd2619;
            }
        }
    }

    .content-body {
        width: 100%;
        height: 100%;
        max-height: 525px;
        background: linear-gradient(180deg,
                rgba(177, 57, 61, 1) 0%,
                rgba(221, 55, 59, 1) 13%,
                rgba(237, 57, 60, 1) 18%,
                rgba(255, 66, 71, 1) 47%,
                rgba(200, 51, 54, 1) 73%);
        overflow: hidden;
        display: flex;

        .content {
            width: 1120px;
            height: 424px;
            margin: 54px auto 0 auto;
            display: flex;
            justify-content: space-between;

            .content-bg-img {
                width: 606px;
                height: 440px;
            }

            .content-list {
                width: 406px;
                height: 424px;
                background: rgba(255, 255, 255, 1);
                border-radius: 14px;
            }
        }
    }

    .content-footer {
        width: 100%;
        height: 98px;
        background-color: #ffffff;
        border-top: 1px solid #cccccc;

        .friend-link {
            display: flex;
            align-items: flex-end;
            width: 908px;
            height: 43px;
            margin: 0px auto;
            color: #666;
            /* //   border-top: 1px solid #b2dfdb; */
        }

        .friend-link-item {
            margin: 0px auto;
        }

        .friend-link-item ul {
            list-style: none;
        }

        .friend-link-item li {
            float: left;
        }

        .link-item {
            padding: 0px 15px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222222;
            line-height: 13px;
            cursor: pointer;

            &:hover {
                color: #dd2619;
            }
        }

        .link-last-item {
            border: none;
        }

        .copyright {
            width: 100%;
            margin-top: 20px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #222222;
            line-height: 13px;
            text-align: center;
        }
        .copyright a {
            color: #222222;
         }
    }

    /deep/ .ivu-input {
        height: 50px;
        background: #fff;
        border: 1px solid #cccccc;
    }

    /deep/ .ivu-form-item-error-tip {
        margin: 0 48px;
    }

    /deep/ .a {
        color: #666666;
    }
</style>
