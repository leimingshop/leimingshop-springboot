<template>
    <div class="user-evaluation" v-if="commentTotalCount != 0">
        <!-- 评价相关数量 -->
        <div class="e-num-about">
            <div class="e-good-rate">
                <span class="good-rate-text">{{ eGoodRate.text }}：</span>
                <p class="good-rate-num">{{ eGoodRate.num }}</p>
            </div>
        </div>

        <!-- 评价列表  -->
        <div class="e-list">
            <div class="e-list-head">
                <ButtonGroup class="e-base-list">
                    <Button
                        type="default"
                        v-for="(item, index) in eBaseList"
                        :key="index"
                        :class="[
                            { active: index + 1 == commentSearchData.type },
                        ]"
                        @click="handleCheckCommentType(index)"
                        >{{ handleEItem(item) }}</Button
                    >
                </ButtonGroup>

                <Checkbox
                    class="currentProduct"
                    v-model="currentProduct.value"
                    @on-change="handleChangeComment"
                    >{{ currentProduct.text }}</Checkbox
                >
            </div>

            <div class="e-comment-list" v-loading="goodsCommentLoading">
                <div v-if="goodsCommentList.length != 0">
                    <!-- 评论列表 -->
                    <comment-comp
                        v-for="item in goodsCommentList"
                        :key="item.id"
                        :comment="item"
                    ></comment-comp>

                    <!-- 分页 -->
                    <paging
                        class="paging"
                        :totalCount="currentTotalCount"
                        :pageSize="commentSearchData.limit"
                        v-model="commentSearchData.page"
                        @handlePageChange="handlePageChange"
                        v-if="
                            goodsCommentList.length !== 0 &&
                            currentTotalCount > commentSearchData.limit
                        "
                    />
                    <div v-else style="height: 60px"></div>
                </div>
                <filtration-no-data
                    :noDataText="noDataText"
                    v-else
                ></filtration-no-data>
            </div>
        </div>
    </div>

    <div v-else v-text="noDataText"></div>
</template>

<script>
    import commentComp from "@/components/03.goodsClass/03-03-04.userEvaluation/commentComp.vue";
    import filtrationNoData from "@/components/03.goodsClass/03-01.goodsClassification/filtrationNoData";
    import paging from "@/components/common/paging";

    import $ from "jquery";
    export default {
        name: "userEvaluation", //用户评价列表页

        data() {
            return {
                currentProduct: {
                    value: false,
                    text: "仅看当前商品",
                },

                commentSearchData: {
                    page: 1, // 当前页码，从1开始
                    limit: 10, // 每页显示记录数
                    // orderField: '', // 排序字段（现无，先不传）
                    specId: void 0, // 商品SKUID 默认不传，加载全部
                    goodsId: void 0, //this.$route.query.goodsId, // 商品SPUID
                    type: 1, // 评价类型 1 全部 2 带图评价 3 好评 4 中评 5 差评
                    // order: '', // 排序方式，可选值(asc、desc) （现无，先不传）
                },

                noDataText: "暂无评价",
            };
        },

        props: {
            goodsCommentLoading: {
                type: Boolean,
                default: false,
            },

            goodsCommentList: {
                type: Array,
                default: () => [],
            },

            commentTotalCount: {
                type: Number,
                default: 0,
            },

            commentAboutNum: {
                type: Object,
                default: null,
            },
        },

        created() {},

        computed: {
            eGoodRate() {
                let commentAboutNum = this.commentAboutNum;
                return {
                    num: `${commentAboutNum.reputably}%`,
                    text: "好评率",
                };
            },

            eBaseList() {
                let commentAboutNum = this.commentAboutNum;
                return [
                    { title: "商品评价", num: commentAboutNum.totalCount },
                    { title: "带图评价", num: commentAboutNum.imgEvaCount },
                    { title: "好评", num: commentAboutNum.goodEvaluate },
                    { title: "中评", num: commentAboutNum.mediumEvaluate },
                    { title: "差评", num: commentAboutNum.negativeEvaluate },
                ];
            },

            currentTotalCount() {
                let index = this.commentSearchData.type - 1;
                return this.eBaseList[index].num;
            },
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    Object.assign(this.$data, this.$options.data());
                    this.$set(
                        this.commentSearchData,
                        "goodsId",
                        newVal.query.goodsId
                    );
                },
            },

            commentSearchData: {
                immediate: true,
                deep: true,
                handler(newVal, oldVal) {
                    this.$emit("handleCommentList", this.commentSearchData);
                },
            },
        },

        components: {
            commentComp,
            filtrationNoData,
            paging,
        },

        methods: {
            // 评论列表 tab-nav-bar
            handleEItem(item) {
                return `${item.title}(${item.num})`;
            },

            // 切换分页回调
            handlePageChange(currentPage) {
                $("html, body").animate(
                    {
                        scrollTop: $(".main-part-content").offset().top,
                    },
                    350
                );
            },

            // 评论列表 tab-nav-bar 切换回调
            handleCheckCommentType(index) {
                this.handlePageReset();
                this.$set(this.commentSearchData, "type", ++index);
            },

            // 是否只显示当前商品评论列表
            handleChangeComment(val) {
                let specId = val ? this.$route.query.specId : void 0;
                let goodsId = this.$route.query.goodsId;

                this.handlePageReset();
                this.$set(this.commentSearchData, "specId", specId);
                this.$set(this.commentSearchData, "goodsId", goodsId);
            },

            // 初始化分页
            handlePageReset() {
                this.handlePageChange(1);
                this.$set(this.commentSearchData, "page", 1);
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .user-evaluation {
        width: 100%;
        background: #ffffff;
    }

    .e-num-about {
        width: 100%;
        overflow: hidden;
        margin-top: -10px;
        .e-good-rate {
            height: 30px;
            line-height: 30px;
            text-align: left;
            display: flex;
        }

        .good-rate-num {
            display: block;
            font-size: 30px;
            font-weight: 600;
            color: #e72900;
        }

        .good-rate-text {
            display: block;
            font-size: 14px;
            color: #717171;
        }
    }

    .e-list {
        margin-top: 30px;
    }

    .e-list-head {
        width: 100%;
        height: 46px;
        padding: 0 20px 0 15px;
        background: #f6f6f6;
    }

    .e-base-list {
        height: 100%;
        float: left;
        button {
            height: 100%;
            border: none;
            background: transparent;
            font-size: 13px;
        }
    }

    .currentProduct {
        float: right;
        margin-top: 12px;
        /deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
            background-color: #dd2619;
            border-color: #dd2619;
        }
    }

    .e-comment-list {
        min-height: 500px;
    }
</style>
