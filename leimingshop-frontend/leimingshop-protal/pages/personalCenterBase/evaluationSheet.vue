<template>
    <div class="integration-center">
        <div class="integra-center-top">
            <ul class="intecenter-top-left" id="tags">
                <li
                    class="base"
                    :class="{ current: flag == 1 }"
                    @click="change(1)"
                >
                    待评价
                </li>
                <li
                    class="base"
                    :class="{ current: flag == 2 }"
                    @click="change(2)"
                >
                    已评价
                </li>
            </ul>
            <ul
                class="intecenter-top-right"
                id="tags"
                style="cursor: pointer"
                @mouseout="mouseOut"
                @mouseover="mouseOver"
            >
                <img :src="wenhao" alt />
                <span @click="$router.push('/helpcenterProblem')"
                    >商品评价说明</span
                >
            </ul>
        </div>

        <div class="integra-center-bottom" id="tagsContent">
            <!-- 待评价 -->
            <noEvaluated
                v-if="flag == 1"
                @childNo="childNo"
                :noevaluatedLoading="noevaluatedLoading"
            ></noEvaluated>
            <!-- 已评价 -->
            <Evaluated
                v-else-if="flag == 2"
                @childOk="childOk"
                :evaluatedLoading="evaluatedLoading"
            ></Evaluated>
            <!-- 待评价分页器 -->
            <div
                class="evaluate"
                v-if="
                    flag == 1 &&
                    !noevaluatedLoading &&
                    pagingFlag1 &&
                    this.evaluateListObject.total > 1
                "
            >
                <paging
                    class="paging"
                    v-if="total > 5"
                    :totalCount="total"
                    :pageSize="pageSize"
                    @handlePageChange="handlePageChange"
                />
            </div>
            <!-- 已评价分页器 -->
            <div
                class="evaluate"
                v-if="
                    flag == 2 &&
                    !evaluatedLoading &&
                    pagingFlag2 &&
                    this.evaluateListObj.total > 1
                "
            >
                <paging
                    class="paging"
                    v-if="totalCount > 5"
                    :totalCount="total"
                    :pageSize="pageSize"
                    @handlePageChange="handlePageChange"
                />
            </div>
        </div>
    </div>
</template>

<script>
    import noEvaluated from "@/components/06.personalCenter/06-02.evaluationSheet/06-02-01.noEvaluated.vue";
    import Evaluated from "@/components/06.personalCenter/06-02.evaluationSheet/06-02-02.evaluated.vue";
    import { mapState, mapMutations, mapActions } from "vuex";
    import paging from "@/components/common/paging";

    export default {
        name: "evaluationSheet", //个人中心评价晒单
        components: {
            paging,
            noEvaluated,
            Evaluated,
        },
        props: {},
        data() {
            return {
                page: 1, //当前页
                pageSize: 5, //每页显示多少条数据
                totalCount: 1, //总共页数，默认为1
                total: 1, //总共条数
                flag: 1, //默认显示待评价页面
                noevaluatedLoading: false,
                evaluatedLoading: false,
                pagingFlag1: true,
                pagingFlag2: true,
                wenhao: "/img/06.personalCenter/wenhao.png",
            };
        },
        mounted() {
            this.getNoevaluatedData();
        },
        computed: {
            ...mapState("evaluationSheet", [
                "evaluateListObject",
                "evaluateListObj",
            ]),
        },
        methods: {
            ...mapActions("evaluationSheet", [
                "getNoevaluatedList",
                "getEvaluatedList",
            ]),
            // 待评价/已评价无数据分页不显示
            childNo() {
                this.pagingFlag1 = false;
            },
            childOk() {
                this.pagingFlag2 = false;
            },
            /**
             * 获取待评价数据封装
             */
            getNoevaluatedData() {
                this.noevaluatedLoading = true;
                this.pagingFlag1 = false;
                this.getNoevaluatedList({
                    page: 1,
                    limit: this.pageSize,
                })
                    .then((res) => {
                        this.noevaluatedLoading = false;
                        if (this.evaluateListObject.list.length > 0) {
                            this.total = this.evaluateListObject.total;

                            this.pagingFlag1 = true;
                        } else {
                            this.pagingFlag1 = false;
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            mouseOut: function () {
                this.wenhao = "/img/06.personalCenter/wenhao.png";
            },
            mouseOver: function () {
                this.wenhao = "/img/06.personalCenter/wenhaored.png";
            },
            /**
             *  获取已评价数据封装
             */
            getEvaluatedData() {
                this.evaluatedLoading = true;
                this.pagingFlag2 = false;
                this.getEvaluatedList({
                    page: 1,
                    limit: this.pageSize,
                })
                    .then((res) => {
                        this.evaluatedLoading = false;
                        if (this.evaluateListObj.list.length > 0) {
                            this.total = this.evaluateListObj.total;
                            this.pagingFlag2 = true;
                        } else {
                            this.pagingFlag2 = false;
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            // 展示待评价/已评价当前页数据切换
            change(data) {
                this.flag = data;
                if (this.flag == 1) {
                    this.getNoevaluatedData();
                } else if (this.flag == 2) {
                    this.getEvaluatedData();
                } else {
                    console.log("获取总页数失败");
                }
            },
            // 分页
            handlePageChange(val) {
                // 待评价分页、已评价分页
                if (this.flag == 1) {
                    this.getNoevaluatedList({
                        page: val,
                        limit: this.pageSize,
                    });
                } else if (this.flag == 2) {
                    this.getEvaluatedList({
                        page: val,
                        limit: this.pageSize,
                    });
                } else {
                    console.log("分页失败");
                }
            },

            goshopevaluate() {
                this.$router.push({
                    path: "/shopEvaluationmore",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    a {
        text-decoration: none;
        color: #333;
    }

    .integration-center {
        background: #fff;
        height: 100%;
    }

    .intecenter-top-left {
        display: flex;
        justify-content: flex-start;
    }

    .integra-center-top {
        background: #eee;
        border-bottom: 0.9px solid rgba(221, 38, 25, 1);
        display: flex;
        justify-content: space-between;
        background: rgba(255, 255, 255, 1);
    }

    .intecenter-top-right {
        display: flex;
        align-items: center;
        margin-right: 20px;
    }

    .base {
        width: 140px;
        text-align: center;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 500;
        color: #666666;
        border-bottom: 0;
        cursor: pointer;
    }

    .current {
        background: rgba(221, 38, 25, 1);
        color: rgba(255, 255, 255, 1);
    }

    .intecenter-top-right span {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(153, 153, 153, 1);
        margin-left: 5px;
    }

    .intecenter-top-right span:hover {
        color: #e2270b;
    }

    // 卡片区域样式
    .indentcard {
        margin: 40px;

        .ivu-card-head {
            padding: 0;
            background: #ccc;

            .title {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                margin-left: 15px;
                line-height: 25px;

                span {
                    margin-right: 20px;
                }
            }
        }
    }

    // 卡片下面部分样式
    .details {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 60px 0 30px;

        .details-left {
            // width: 50%;
            width: 318px;

            .details-top {
                display: flex;
                padding-bottom: 20px;
                margin-bottom: 20px;
                border-bottom: 1px solid #ebebeb;

                .details-img {
                    width: 68px;
                    height: 68px;
                    margin-right: 20px;

                    img {
                        height: 100%;
                    }
                }

                .youth {
                    // 多行文本两端对其方式
                    text-indent: justify;
                    text-align-last: justify;
                    text-justify: inter-ideograph;
                    line-height: 22px;

                    img {
                        vertical-align: middle;
                    }

                    .xiaomi {
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(51, 51, 51, 1);
                    }

                    .youth-phone {
                        width: 223px;
                        font-size: 16px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(51, 51, 51, 1);
                        // 文字最多显示2行
                        // 超出部分隐藏显示省略号
                        text-overflow: -o-ellipsis-lastline;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                    }

                    .details-bottom-block {
                        display: flex;
                        justify-content: space-between;
                        height: 13px;
                        font-size: 13px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(153, 153, 153, 1);
                        line-height: 13px;
                        margin-top: 12px;
                    }
                }
            }

            .details-top:last-child {
                border: 0;
            }

            .line {
                width: 312px;
                height: 1px;
                background: rgba(235, 235, 235, 1);
                margin-left: 20px;
            }

            .details-bottom {
                display: flex;
                padding: 20px;

                .details-img {
                    width: 68px;
                    height: 68px;
                    margin-right: 18px;

                    img {
                        height: 100%;
                    }
                }
            }

            .youth {
                // 多行文本两端对其方式
                text-indent: justify;
                text-align-last: justify;
                text-justify: inter-ideograph;
                line-height: 22px;

                img {
                    vertical-align: middle;
                    margin-right: 10px;
                }

                .xiaomi {
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(51, 51, 51, 1);
                }

                .youth-phone {
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(51, 51, 51, 1);
                }

                .details-bottom-color {
                    height: 13px;
                    font-size: 13px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    line-height: 13px;
                    margin-top: 12px;
                }
            }
        }

        .details-right {
            // width: 50%;
            display: flex;
            align-items: center;
            justify-content: space-around;

            .name {
                width: 120px;
                text-align: center;
            }

            .money {
                font-size: 20px;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-weight: 600;
                color: rgba(51, 51, 51, 1);
                width: 180px;
                text-align: center;
            }

            .right {
                width: 100px;
                text-align: center;
            }
        }

        .right .right-p1 span {
            height: 16px;
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(231, 41, 0, 1);
            line-height: 16px;
            cursor: pointer;
        }

        .right .right-p2 span {
            height: 12px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(102, 102, 102, 1);
            line-height: 12px;
            cursor: pointer;
        }
    }

    .evaluate {
        text-align: center;
        // margin-top: 20px;

        .ivu-page {
            position: relative;
        }
    }
    /deep/ .ivu-card-bordered {
        border: 1px solid #f6f6f6 !important;
        box-shadow: none !important;
        &:hover {
            border: 1px solid #e8e8e8 !important;
            .o-head {
                background: #e8e8e8;
            }
            .g-name {
                color: #dd2619 !important;
            }
        }
    }
    /deep/ .o-head {
        height: 44px !important;
        line-height: 44px !important;
        background: #f6f6f6;
    }
    /deep/ .ivu-card {
        border-radius: 0px !important;
    }
    /deep/ .g-name {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #222222 !important;
        &:hover {
            color: #dd2619 !important;
        }
    }
    /deep/ .g-attribute {
        font-size: 12px !important;
    }
    /deep/ .o-content > div.o-consignee-name {
        margin-left: 0px !important;
        display: flex !important;
    }
    /deep/ .base {
        height: 44px !important;
        line-height: 44px !important;
    }
    /deep/ .page-item-jump-prev,
    .ivu-page-item-jump-next {
        border-radius: 0px !important;
    }
    /deep/ .o-content > div.o-operate .right-p1 {
        height: 14px;
        line-height: 14px;
        font-size: 14px !important;
        font-family: PingFangSC-Medium, pingFang SC;
        color: #dd2619 !important;
    }
    /deep/ .o-content > div.o-operate .right-p2 {
        font-size: 12px !important;
        height: 12px;
        line-height: 12px;
        &:hover {
            color: #dd2619;
        }
    }
</style>
