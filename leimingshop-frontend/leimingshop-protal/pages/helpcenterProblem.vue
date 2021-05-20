<template>
    <div style="width: 100%; background: #eee">
        <div class="personl-top" style="background: #ffffff">
            <search-part-white :borderFullWidth="true"></search-part-white>
        </div>
        <div class="bgc">
            <div class="personal-center-base">
                <!-- 面包屑 -->
                <breadcrumb1 :list="breadCrumbList" />
                <div class="person-center-content">
                    <div class="banner">
                        <img v-lazy="'/img/04.shoppingCar/banner.png'" alt />
                    </div>
                    <div class="problemClassification">
                        <div class="classification">问题分类</div>
                        <div class="always">
                            <ul>
                                <li
                                    v-for="(item, index) in MyFirstClassList"
                                    :key="index"
                                    @click="gohelpCenter(index)"
                                >
                                    <span class="problem-icon">
                                        <img
                                            :src="'/img/06.personalCenter/06-14.helpCenter/user_nav_2.png'"
                                            alt
                                        />
                                    </span>
                                    <div class="description">
                                        <p>{{ item.acName }}</p>
                                        <p></p>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { getMyFirstClassList } from "@/api/api_06-14.helpCenter.js";
    import searchPartWhite from "@/components/common/searchPartWhite.vue";
    // import helpCenter from "@/views/06.personalCenter/06-14-02.helpCenter.vue";
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";

    export default {
        name: "helpcenterProblem",
        head() {
            return {
                title: "帮助中心",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        components: {
            searchPartWhite,
            // helpCenter,
            breadcrumb1,
        },
        data() {
            return {
                //首页一级分类集合
                MyFirstClassList: [
                    {
                        acName: "",
                        children: null,
                        id: "",
                    },
                ],
                breadCrumbList: Object.freeze([
                    {
                        title: "首页",
                        toPath: "/",
                    },
                    {
                        title: "帮助中心",
                        toPath: "",
                    },
                ]),
            };
        },
        created() {},
        computed: {},
        mounted() {
            this.getMyFirstClassListData();
        },
        methods: {
            // 获取首页分类数据
            getMyFirstClassListData() {
                // 这个getMyFirstClassList()方法名,要与接口数据名一致，否则调取不到接口
                getMyFirstClassList()
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.MyFirstClassList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },

            // 跳转帮助中心子集分类页
            gohelpCenter(id) {
                this.$router.push({
                    path: "/helpCenter",
                    query: {
                        parentId: id,
                    },
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    /deep/ .search-left {
        margin-top: 0 !important;
    }

    /deep/ .border {
        border-bottom: 2px solid #dd2619;
        .Secondarynav-part {
            border-bottom: 0;
        }
    }

    .bgc {
        width: 100%;
        background: #f6f6f6;
        .personal-center-base {
            width: 1200px;
            margin: 0 auto;
            padding-bottom: 80px;

            .personal-center-nav {
                padding: 15px 0;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                .home {
                    cursor: pointer;
                }
                .home:hover {
                    color: rgba(226, 39, 11, 1);
                }
                .helpcenter {
                    cursor: pointer;
                }
                .helpcenter:hover {
                    color: rgba(226, 39, 11, 1);
                }
            }

            .person-center-content {
                width: 100%;
                .banner {
                    width: 100%;
                    height: 130px;
                    img {
                        width: 100%;
                        height: 100%;
                        display: block;
                    }
                }

                .problemClassification {
                    background: rgba(255, 255, 255, 1);
                    padding: 0 33px 42px 29px;
                    margin-top: 28px;
                    .classification {
                        height: 64px;
                        line-height: 64px;
                        font-size: 22px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 600;
                        color: rgba(51, 51, 51, 1);
                    }
                    .always {
                        width: 100%;

                        ul {
                            width: 100%;

                            li {
                                display: inline-block;
                                width: 273px;
                                height: 108px;
                                margin-right: 11px;
                                margin-bottom: 13px;
                                // border: 1px solid #cccccc;
                                padding: 24px 23px 24px 21px;
                                background: #ffffff;
                                cursor: pointer;

                                .problem-icon {
                                    display: inline-block;
                                    width: 60px;
                                    height: 60px;

                                    img {
                                        width: 100%;
                                        height: 100%;
                                    }
                                }

                                .description {
                                    display: inline-block;
                                    width: 148px;
                                    height: 60px;
                                    font-size: 14px;
                                    font-weight: 400;
                                    line-height: 24px;
                                    margin-left: 15px;

                                    p:first-child {
                                        height: 32px;
                                        font-size: 22px;
                                        font-weight: 500;
                                        color: rgba(85, 85, 85, 1);
                                        line-height: 8px;
                                    }

                                    p:last-child {
                                        font-size: 14px;
                                        font-weight: 400;
                                        color: rgba(51, 51, 51, 1);
                                        // height: 20px;
                                        // line-height: 20px;
                                        overflow: hidden;
                                        text-overflow: ellipsis;
                                        white-space: nowrap;
                                    }
                                }
                            }

                            li:hover {
                                box-shadow: 0px 0px 5px 2px rgba(0, 0, 0, 0.07);
                            }

                            li:nth-last-of-type(-n + 4) {
                                margin-bottom: 0;
                            }

                            li:nth-child(4n) {
                                margin-right: 0;
                            }
                        }
                    }
                }
            }
        }
    }
</style>
