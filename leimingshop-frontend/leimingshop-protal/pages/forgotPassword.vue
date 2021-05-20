<template>
    <div>
        <div class="home">
            <div class="home-head">
                <logoImg></logoImg>
                <router-link to="/login" class="home-head-right">返回登录></router-link>
            </div>
        </div>
        <div class="main">
            <!-- <div class="red"></div> -->
            <div class="content">
                <div class="formStyle">
                    <component :is="tab" @childFn="parentFn" :mobile="mobile" :username="username"></component>
                </div>
            </div>
        </div>
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
</template>

<script>
    import logoImg from "@/components/common/logoImg.vue";
    import forgotCurrent01 from "@/components/11.forgotMessage/forgotCurrent01.vue";
    import forgotCurrent02 from "@/components/11.forgotMessage/forgotCurrent02.vue";
    import forgotCurrent03 from "@/components/11.forgotMessage/forgotCurrent03.vue";
    import forgotCurrent04 from "@/components/11.forgotMessage/forgotCurrent04.vue";
    export default {
        head() {
            return {
                title: "找回密码",
                meta: [
                {
                    hid: "description",
                    name: "description",
                    content: "My custom description",
                }, ],
            };
        },
        components: {
            logoImg,
            forgotCurrent01,
            forgotCurrent02,
            forgotCurrent03,
            forgotCurrent04,
        },
        data() {
            return {
                tab: "forgotCurrent01",
                mobile: "",
                username: "",
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
            };
        },
        methods: {
            parentFn(data) {
                // this.tab=data;
                // if(mobile){
                //     this.mobile = mobile
                // }
                if (typeof data === "object") {
                    this.mobile = data.mobile;
                    this.username = data.username;
                    this.tab = data.componentName;
                } else {
                    this.tab = data;
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    //顶部部分
    .home {
        width: 100%;
        background: #fff;
        box-shadow: 0px 2px 6px 0px rgba(221, 217, 217, 0.31);

        .home-head {
            width: 1200px;
            height: 110px;
            line-height: 110px;
            margin: 0 auto;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            overflow: hidden;

            .home-head-left {
                width: 219px;
                height: 59px;
            }

            .home-head-right {
                color: #dd2619;
                font-size: 17px;
                margin: 62px 0 24px 0;
            }
        }
    }

    .red {
        width: 100%;
        height: 1px;
        background: #dd2619;
    }

    .main {
        padding: 82px 0;
        border-bottom: 1px solid #cccccc;

        .content {
            width: 979px;
            height: 461px;
            margin: 0 auto;
            // padding: 92px 0 0 0;
            // text-align: center;

            .formStyle {
                width: 60%;
                margin: 25px auto;

                .formBtn {
                    width: 137px;
                    height: 100%;
                    background: #3e3d46;
                    color: #fff;
                }
            }
        }
    }

    .friend-link {
        display: flex;
        align-items: center;
        width: 908px;
        height: 30px;
        margin: 21px auto 4px;
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
        padding: 5px 0px;
        float: left;
    }

    .link-item {
        padding: 0px 8px;
        margin: 0 9px;
        cursor: pointer;
        // border-right: 1px solid #ccc;
    }

    .link-last-item {
        border: none;
    }

    .copyright {
        width: 100%;
        color: #666;
        line-height: 30px;
        text-align: center;
        margin: 0 0 24px;
    }
    .copyright a {
            color: #232323;
    }

    /deep/ .ivu-steps .ivu-steps-head-inner {
        width: 30px;
        height: 30px;
        line-height: 30px;
        background: rgba(241, 243, 241, 1);
        border: none;
        font-size: 16px;
        color: #999999 !important;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner {
        border-color: #f1f3f1;
        background-color: #f1f3f1;
    }

    /deep/ .ivu-steps .ivu-steps-title {
        background: rgba(255, 252, 252, 1);
    }

    /deep/ .ivu-steps .ivu-steps-head {
        background: rgba(255, 252, 252, 1);
        margin-bottom: 6px;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner span,
    .ivu-steps-item.ivu-steps-status-process .ivu-steps-head-inner>.ivu-steps-icon {
        color: #999999;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner span,
    .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner>.ivu-steps-icon {
        color: #999999;
    }

    /deep/ .ivu-form-item {
        display: flex;
        margin: 25px auto;
    }

    /deep/ .ivu-input {
        width: 348px;
        height: 40px;
        font-size: 14px;
        line-height: 40px;

        &:focus {
            border-color: #dcdee2;
            box-shadow: none;
        }

        &:hover {
            border-color: #b7b7b7;
        }
    }

    /deep/ .ivu-form .ivu-form-item-label {
        font-size: 14px;
        color: #222;
        margin: 0 12px;
        line-height: 40px;
        padding: 0;
    }

    /deep/ .ivu-btn-primary {
        width: 348px;
        height: 46px;
        margin: 0 133px;
        color: white;
        background-color: #dd2619;
    }

    /deep/ .ivu-steps .ivu-steps-head-inner>.ivu-steps-icon.ivu-icon {
        background: #13ae27;
        border-radius: 50%;
        font-size: 30px;
        color: #fff;
    }

    /deep/ .ivu-steps-item.ivu-steps-status-finish .ivu-steps-tail>i:after {
        background: #82d38d;
    }

    /deep/ .ivu-steps.ivu-steps-small .ivu-steps-content {
        font-size: 18px;
        padding: 0;
    }

    /deep/ .ivu-steps-horizontal .ivu-steps-content {
        padding: 0;
    }

    /deep/ .ivu-input-wrapper:hover {
        cursor: pointer;

        .ivu-input-icon-clear {
            line-height: 40px;
            color: #ed4014;
        }
    }

    /deep/ .ivu-steps-content {
        font-size: 14px !important;
    }

    /deep/ .ivu-steps-status-finish .ivu-steps-content {
        color: #13ae27 !important;
    }

    /deep/ .ivu-steps {
        .ivu-steps-item {
            padding-left: 20px;
        }

        .ivu-steps-title {
            height: 30px;

            &:after {
                border-top: 1px dashed #13ae27;
                background: white;
            }
        }

        .ivu-steps-content {
            position: relative;
            left: -60px;
            text-align: center;
        }

        .ivu-steps-status-wait:nth-last-child(1) {
            .ivu-steps-content {
                left: -10px;
            }
        }
    }
</style>
