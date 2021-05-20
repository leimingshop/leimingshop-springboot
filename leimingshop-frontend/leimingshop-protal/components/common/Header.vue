<template>
    <div class="box">
        <div class="nav">
            <Dropdown class="location-drop">
                <a class="location">
                    <img src="/img/common/header/ico_position.png" alt="" />
                    <span>{{ city }}</span>
                </a>
                <DropdownMenu slot="list">
                    <div class="location-box">
                        <a class="location">
                            <img src="/img/common/header/ico_position.png" alt="" />
                            <span>{{ city }}</span>
                        </a>
                        <!-- <div class="gray-mask"></div> -->
                    </div>
                    <div class="location-hover">
                        <div class="wait-choose" :class="{ 'already-choose': cityItem == city }" v-for="(cityItem, cityIndex) of citiesArr" :key="cityIndex">
                            {{ cityItem }}
                        </div>
                    </div>
                </DropdownMenu>
            </Dropdown>

            <ul class="detail">
                <!-- memberName -->
                <li class="first" v-show="!userInfo.memberName">
                    <router-link to="/login">请先登录 <Icon type="person"></Icon>
                    </router-link>
                </li>
                <li v-show="!userInfo.memberName">
                    <router-link to="/register">免费注册 <Icon type="person-add"></Icon>
                    </router-link>
                </li>
                <li class="nickName-drop" v-show="!!userInfo.memberName">
                    <Dropdown>
                        <p class="username-p">
                            <img class="person-icon" :src="$imgURL + userInfo.memberAvatar" />
                            <span class="username">{{
                                userInfo.memberName
                            }}</span>
                        </p>
                        <DropdownMenu slot="list">
                            <div class="my-page">
                                <a @click="
                                        goPage(
                                            '/personalCenterBase/personalCenter'
                                        )
                                    ">个人中心</a>
                                <a @click="
                                        goPage(
                                            '/personalCenterBase/evaluationSheet'
                                        )
                                    ">评价晒单</a>
                                <a @click="
                                        goPage(
                                            '/personalCenterBase/favoritesList'
                                        )
                                    ">收藏商品</a>
                                <a @click="
                                        goPage(
                                            '/personalCenterBase/securitySetting'
                                        )
                                    ">安全设置</a>
                                <a @click="signOutFun">退出登录</a>
                            </div>
                        </DropdownMenu>
                    </Dropdown>
                </li>
                <li>
                    <router-link to="/personalCenterBase/myOrders">我的订单</router-link>
                </li>
                <li>
                    <router-link to="/helpcenterProblem">帮助中心</router-link>
                </li>
                <li>
                    <router-link to="/">在线客服</router-link>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    // import store from "@/vuex/store"; //引用vuex ssr注释
    import { mapState, mapActions } from "vuex";
    export default {
        name: "M-Header",
        //  store: store, //加载vuex ssr注释
        created() {
            this.isLogin();
        },
        data() {
            return {
                city: "珠海",
                citiesArr: [
                    "北京",
                    "上海",
                    "天津",
                    "重庆",
                    "广州",
                    "深圳",
                    "河南",
                    "辽宁",
                    "吉林",
                    "江苏",
                    "江西",
                    "四川",
                    "海南",
                    "贵州",
                    "云南",
                    "西藏",
                    "陕西",
                    "甘肃",
                    "青海",
                    "珠海",
                    "西藏",
                    "陕西",
                    "甘肃",
                    "青海",
                    "珠海",
                ],
            };
        },
        computed: {
            ...mapState("commonHeader", [
                //确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "userInfo",
                "shoppingCart",
            ]),
        },
        mounted() {
            var s = window.returnCitySN["cname"];
            this.city = s.substring(0, s.length - 1);
        },
        methods: {
            ...mapActions("commonHeader", ["logOut", "isLogin"]),

            changeCity(city) {
                this.city = city;
            },
            goToPay() {
                this.$router.push("/order");
            },
            goPage(data) {
                let newpage = this.$router.resolve({
                    path: data,
                });
                window.open(newpage.href, "_blank");
            },
            signOutFun() {
                this.logOut();
            },
        },
    };
</script>

<style lang="scss" scoped>
    .box {
        width: 100%;
        height: 30px;
        background-color: #eeeeee;
        font-size: 13px;

        .nav {
            margin: 0% auto;
            width: 1200px;
            display: flex;
            flex-direction: row;
            justify-content: space-between;

            ul {
                list-style: none;

                li {
                    float: left;
                    font-size: 12px;
                    line-height: 30px;
                    margin-right: 15px;
                    font-weight: 400;
                }

                li:last-child {
                    margin-right: 0;
                }
            }

            a {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                text-decoration: none;
                color: #666666;
                padding-left: 15px;
                cursor: pointer;
            }

            a:hover {
                color: #dd2619;
            }

            .location {
                height: 30px;
                padding: 0 10px;
                line-height: 30px;
                display: flex;
                align-items: center;
                position: relative;

                img {
                    height: 12px;
                    width: auto;
                    margin-right: 5px;
                }
            }

            .location-drop {
                height: 30px;
                position: relative;

                /deep/ .ivu-scroll-content-loading {
                    opacity: 1;
                }

                /deep/ .ivu-select-dropdown {
                    width: 320px;
                    height: 260px;
                    background-color: #ffffff;
                    padding: 0 0 20px 10px !important;
                    border: 1px solid #eeeeee;
                    border-top: 0px;
                    border-top-left-radius: 0px;
                    border-top-right-radius: 0px;
                    margin: 0 auto;
                    position: absolute;
                    top: 0px !important;
                    left: -1px !important;
                }

                /deep/ .ivu-dropdown-menu {
                    .location-box {
                        width: 400px;
                        background-color: #f6f6f6;
                        margin-bottom: 20px;
                        display: flex;

                        .location {
                            background-color: #fff;
                            padding: 0px;

                            span {
                                padding-right: 10px;
                                display: flex;
                                flex-wrap: wrap;
                            }
                        }

                        // .gray-mask{
                        //     width:320px;
                        //     height:30px;
                        //     background-color:#F6F6F6
                        // }
                    }
                }

                .location-hover {
                    display: flex;
                    flex-wrap: wrap;

                    .wait-choose {
                        height: 24px;
                        padding: 0 8px;
                        margin-right: 24px;
                        margin-bottom: 17px;
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #666666;
                        line-height: 24px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .already-choose,
                    .wait-choose:hover {
                        color: #ffffff;
                        background-color: #dd2619;
                    }

                    .wait-choose:nth-child(5n + 5) {
                        margin-right: 0px;
                    }
                }
            }
        }
    }

    .first {
        color: #999999;
    }

    .first a:first-child {
        padding-left: 3px;
        border-left: none;
    }

    .city {
        padding: 10px 15px;
    }

    .city-item {
        font-weight: bold;
        cursor: pointer;
        padding: 5px;
    }

    .city-item:hover {
        color: #d9534f;
    }

    .person-icon {
        width: 20px;
        height: 20px;
        border-radius: 10px;
        margin: 0 5px 0 0;
    }

    .cart-null-icon {
        font-size: 38px;
        margin-bottom: 15px;
    }

    .username-p {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #dd2619;
        line-height: 30px;
        cursor: pointer;
        display: flex;
        align-items: center;

        .username {
            width: 80px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }
    }

    .my-page {
        // padding: 3px 5px;
        width: 100px;
        height: 100%;

        a {
            padding: 0 !important;
            border: 0 !important;
            margin: 0 !important;
            display: block;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 32px;
            text-align: center;
        }

        a:hover {
            background: rgba(221, 38, 25, 0.03);
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #dd2619;
        }
    }

    .nickName-drop {
        /deep/ .ivu-select-dropdown {
            padding: 0px;
            border-top-left-radius: 0px;
            border-top-right-radius: 0px;
            margin: 0px;
            top: 30px !important;
        }
    }
</style>