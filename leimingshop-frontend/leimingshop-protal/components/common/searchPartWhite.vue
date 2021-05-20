<template>
    <div class="search-part">
        <searchFirstLevel :isNeedCupping="isNeedCupping"></searchFirstLevel>

        <div :class="{ border: borderFullWidth }">
            <div class="Secondarynav-part warp-con backgroundcolorFFF">
                <Dropdown
                    class="Secondarynav-left"
                    trigger="click"
                    placement="bottom-start"
                >
                    <a href="javascript:void(0)" class="red-title">
                        所有商品分类
                        <Icon type="ios-arrow-down"></Icon>
                    </a>
                    <DropdownMenu slot="list">
                        <DropdownItem
                            v-for="(level1,
                            indexlevel1) in IndexCustomClassData"
                            :key="indexlevel1"
                        >
                            <div class="all-pro-class">
                                <div
                                    class="all-pro-class-title"
                                    @click="
                                        handleGoGoodsClass(
                                            level1.id,
                                            level1.classId
                                        )
                                    "
                                >
                                    {{ level1.gcName }}
                                </div>

                                <div
                                    class="goods-type-level2"
                                    v-show="
                                        judgeDisplay(
                                            level1.gcName,
                                            level1.children,
                                            indexlevel2
                                        )
                                    "
                                    v-for="(level2,
                                    indexlevel2) in level1.children"
                                    :key="indexlevel2"
                                >
                                    <a
                                        v-show="
                                            judgeDisplayLevel2Slash(
                                                level1.gcName,
                                                level1.children,
                                                indexlevel2
                                            )
                                        "
                                        >/</a
                                    >
                                    <a
                                        class="goods-type-level2-a"
                                        @click="
                                            handleGoGoodsClass(
                                                level2.id,
                                                level2.classId
                                            )
                                        "
                                        >{{ level2.gcName }}</a
                                    >
                                    <div class="right-display">
                                        <div
                                            v-for="(level2,
                                            indexlevel2) in level1.children"
                                            :key="indexlevel2"
                                        >
                                            <span
                                                @click="
                                                    handleGoGoodsClass(
                                                        level2.id,
                                                        level2.classId
                                                    )
                                                "
                                                >{{ level2.gcName }}</span
                                            >
                                            <Icon
                                                type="ios-arrow-forward"
                                            ></Icon>
                                            <p>
                                                <span
                                                    v-for="(level3,
                                                    indexlevel3) in level2.children"
                                                    :key="indexlevel3"
                                                    class="goods-type-level3-a"
                                                    @click="
                                                        handleGoGoodsClass(
                                                            level3.id,
                                                            level3.classId
                                                        )
                                                    "
                                                    >{{ level3.gcName }}</span
                                                >
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <Icon
                                type="ios-arrow-forward"
                                class="second-arrow-icon"
                            ></Icon>
                        </DropdownItem>
                    </DropdownMenu>
                </Dropdown>
                <Row type="flex">
                    <i-col>
                        <a @click="$router.push('/')">首页</a>
                    </i-col>
                    <i-col v-for="(item, index) of IndexNavList" :key="index">
                        <a @click="goNavPage(item)">{{ item.title }}</a>
                    </i-col>
                </Row>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapState, mapActions } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import { IndexNav } from "@/api/api_01main.js";
    import searchFirstLevel from "@/components/common/searchFirstLevel.vue";
    export default {
        props: {
            isNeedCupping: {
                type: Boolean,
                default: true,
                required: false,
            },

            borderFullWidth: {
                type: Boolean,
                default: false,
            },
        },
        components: {
            searchFirstLevel,
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "IndexCustomClassData",
            ]),
        },
        data() {
            return {
                cityList: [
                    {
                        value: "0",
                        label: "店铺",
                    },
                    {
                        value: "1",
                        label: "商品",
                    },
                ],
                value: "",
                model1: "0",
                IndexNavList: [],
            };
        },
        mounted() {
            this.actIndexCustomClass();
            this.getIndexNav();
        },
        methods: {
            ...mapActions("main", ["actIndexCustomClass"]),
            //获取首页导航栏
            getIndexNav() {
                IndexNav("")
                    .then((res) => {
                        // console.log(res.data)
                        if (res.code == 200) {
                            this.IndexNavList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //首页导航栏跳转
            goNavPage(item) {
                console.log(item);
                if (item.relationType == 1) {
                    window.open(item.relationParams, "_blank");
                } else if (item.relationType == 2) {
                    let routeUrl = this.$router.resolve({
                        path: "/proClassification",
                        query: {
                            classId: item.relationParams,
                        },
                    });

                    window.open(routeUrl.href, "_blank");
                }
            },
            judgeDisplay(level1gcName, level1children, indexlevel2) {
                if (indexlevel2 == 0) {
                    return true;
                } else if (indexlevel2 == 1) {
                    if (
                        level1children[indexlevel2].gcName.length +
                            level1children[indexlevel2 - 1].gcName.length <
                        7
                    ) {
                        return true;
                    }
                }
            },
            judgeDisplayLevel2Slash(level1gcName, level1children, indexlevel2) {
                if (indexlevel2 == 0) {
                    return false;
                } else if (indexlevel2 == 1) {
                    if (
                        level1children[indexlevel2].gcName.length +
                            level1children[indexlevel2 - 1].gcName.length <
                        7
                    ) {
                        return true;
                    }
                }
            },
            // 跳转至分类列表页
            handleGoGoodsClass(id, classId) {
                let routeUrl = this.$router.resolve({
                    path: "/proClassification",
                    params: {
                        id,
                    },
                    query: {
                        classId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },

            // 跳转至目标页
            handleRouterGo(path) {
                let routeUrl = this.$router.resolve({
                    path,
                });

                window.open(routeUrl.href, "_blank");
            },
        },
    };
</script>

<style lang='scss' scoped>
    $primary-color: #dd2619;

    .warp-con {
        width: 1200px;
        margin: 0 auto;
    }

    .backgroundcolorFFF {
        background-color: #fff;
    }

    .con-left {
        width: 232px;
    }

    .con-right {
        flex-grow: 1;
    }

    //顶部搜索部分
    .search-part {
        width: 100%;
        background-color: #fff;

        .search-wrap {
            height: 147px;
            padding-top: 47px;
            display: flex;

            .search-left {
                height: 100px;
                margin-top: -15px;

                img {
                    width: 232px;
                    height: 80px;
                }
            }

            .search-con {
                flex-grow: 1;
                display: flex;
                justify-content: center;
            }

            .search-con-cart {
                width: 132px;
                height: 46px;
                line-height: 46px;
                border-radius: 4px;
                margin-left: 16px;
                border: 1px solid #999999;
                background-color: #fff;
                font-size: 16px;
                // padding-right: 15px;
                display: flex;
                justify-content: center;
                align-items: center;

                .ivu-icon-ios-cart-outline {
                    margin-right: 6px;
                    font-size: 17px;
                }

                .ivu-badge {
                    width: 16px;
                    height: 16px;

                    /deep/ .ivu-badge-count {
                        top: -1px;
                        border-radius: 50%;
                        width: 15px;
                        height: 17px;
                        font-size: 10px;
                    }
                }
            }
        }
    }

    .search-con-search {
        .search-con-search-top {
            width: 560px;
            height: 46px;
            background: rgba(255, 255, 255, 1);
            border-radius: 4px;
            border: 2px solid #dd2619;
            display: flex;

            .ivu-select {
                width: 80px;
                height: 40px;
                margin-top: 1px;
                margin-left: 1px;

                /deep/.ivu-select-small.ivu-select-single .ivu-select-selection {
                    width: 80px;
                    height: 40px;
                    border: 0;
                    font-size: 16px;
                }

                /deep/ .ivu-select-selected-value,
                /deep/ .ivu-select-placeholder {
                    height: 40px;
                    line-height: 40px;
                    padding: 0 21px 0 20px;
                    font-size: 16px;
                }
            }
            /deep/ .ivu-icon {
                &:before {
                    content: "";
                    display: block;
                    width: 0;
                    height: 0;
                    border-left: 5px solid transparent;
                    border-right: 5px solid transparent;
                    border-top: 7px solid #333;
                }
            }
            .ivu-input-wrapper {
                flex-grow: 1;

                /deep/ .ivu-input {
                    width: 100%;
                    height: 40px;
                    margin-top: 1px;
                    border: 0;
                    outline: none;
                    font-size: 16px;
                    box-shadow: 0 0 0 0px transparent;
                    border-bottom: 0 solid gainsboro;
                }
            }

            button {
                margin-right: -2px;
                width: 80px;
                height: 42px;
                background: #dd2619;
                border: 0;
                border-radius: 0;
                font-size: 20px;
            }
        }

        .search-con-search-bottom {
            margin-top: 6px;
            color: #bebebe;
            font-size: 13px;

            span {
                display: inline-block;
                margin: 0 6px 0 0;
            }
        }
    }

    .border {
        border-bottom: 2px solid #dd2619;
        .Secondarynav-part {
            border-bottom: 0;
        }
    }

    .Secondarynav-part {
        border-bottom: 2px solid #dd2619;
        display: flex;
        position: relative;

        .ivu-row-flex {
            background-color: #fff;
            padding-left: 22px;
            display: flex;
            flex-grow: 1;
            flex-wrap: nowrap;
            text-align: center;
            line-height: 46px;
            font-size: 15px;

            .ivu-col {
                cursor: pointer;
                max-width: 100px;
                padding: 0 20px 0 20px;
                p {
                    overflow: hidden;
                    white-space: nowrap;
                    word-break: break-all;
                    text-overflow: ellipsis;
                }

                a {
                    color: #4c4c4c;

                    &:hover {
                        color: $primary-color;
                    }
                }
            }
        }

        .Secondarynav-left {
            width: 232px;
            height: 46px;
            line-height: 46px;
            color: #fff;
            padding: 0 20px 0 0px;
            background-color: #dd2619;
            font-size: 15px;

            .red-title {
                padding: 0 0 0 30px;
                color: #ffffff;
                display: flex;
                justify-content: space-between;
                width: 100%;
                align-items: center;
            }

            /deep/ .ivu-dropdown-menu {
                height: 470px;
            }

            /deep/ .ivu-select-dropdown {
                width: 232px;
                padding: 10px 0;
                top: 43px !important;

                .all-pro-class {
                    width: 100%;
                    display: flex;
                    align-items: center;

                    .all-pro-class-title {
                        margin-right: 10px;
                        font-size: 14px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: #222222;
                        line-height: 14px;
                    }

                    .all-pro-class-title:hover {
                        color: #dd2619;
                    }

                    .goods-type-level2 {
                        margin-left: 4px;
                        a:first-of-type {
                            color: #666;
                            margin-right: 4px;
                        }
                        .goods-type-level2-a {
                            margin: 0 0 0 0;
                            font-size: 14px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #666666;
                            line-height: 14px;
                        }

                        .goods-type-level2-a:hover {
                            color: #dd2619;
                        }

                        .right-display {
                            width: 970px;
                            height: 490px;
                            background-color: #fff;
                            padding: 24px 20px 32px 20px;
                            display: flex;
                            flex-wrap: wrap;
                            flex-grow: 1;
                            position: absolute;
                            display: none;
                            top: 0px;
                            left: 232px;
                            box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.12);

                            p > span {
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: #666666;
                                line-height: 12px;
                                margin-right: 7px;
                                margin-left: 3px;
                                margin-bottom: 10px;
                            }
                            p {
                                display: flex;
                                flex-wrap: wrap;
                            }
                            div > span:first-child {
                                min-width: 48px;
                                margin-right: 3px;
                                margin-left: 0px;
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 600;
                                color: rgba(34, 34, 34, 1);
                                line-height: 12px;
                                text-align: right;
                            }

                            div {
                                display: flex;
                                margin-bottom: 10px;
                            }

                            span:hover {
                                color: #dd2619;
                            }
                        }

                        .right-display:hover {
                            display: block;
                        }
                    }
                }
                /deep/.ivu-dropdown-item {
                    height: 30px;
                    padding: 0 10px 0 20px;
                    display: flex;
                    align-items: center;
                }
                /deep/.ivu-dropdown-item:hover {
                    .all-pro-class {
                        .goods-type-level2 {
                            .right-display {
                                display: block;
                                position: absolute;
                                top: 0px;
                                left: 233px;
                            }
                        }
                    }
                }
            }
        }
    }
</style>
