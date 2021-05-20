<template>
    <div class="search-no-data">
        <img :src="noDataImg" alt="" />
        <div class="noDataText" v-html="noDataText"></div>

        <div class="other-search-info">
            <p v-text="noDataTitle"></p>
            <ul v-loading="recommendLoading">
                <li v-for="(item, index) in recommendList" :key="index">
                    <router-link
                        :to="`/searchGoodsResult?keyWord=${encodeURIComponent(
                            item.hotWord
                        )}`"
                        replace
                        >{{ item.hotWord }}
                    </router-link>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    import { mapState, mapActions } from "vuex";

    export default {
        name: "searchNoData", // 筛选 - 空数据
        data() {
            return {
                noDataImg: require("~/static/img/03.goodsClass/03-01.goodsClassification/no-data.png"),
                noDataTitle: "您还可以尝试以下搜索：",
            };
        },

        props: {},

        computed: {
            noDataText() {
                if (this.$route.query.keyWord) {
                    return `
                            <p>抱歉，没有搜索到与&ensp;<span>${this.$route.query.keyWord}</span>&ensp;相关的商品</p>
                            <p>请检查您的输入是否有误</p>
                        `;
                } else {
                    return "<p>抱歉，没有搜索到相关的商品</p>";
                }
            },

            ...mapState("searchGoodsResult", ["recommendList", "recommendLoading"]),
        },

        mounted() {
            this.hanldeRecommendList();
        },

        methods: {
            ...mapActions("searchGoodsResult", ["hanldeRecommendList"]),
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .search-no-data {
        width: 100%;
        padding-bottom: 70px;
        background: #ffffff;
        text-align: center;

        img {
            width: 100px;
            height: auto;
            margin-top: 163px;
        }

        .noDataText {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #999999;
            line-height: 14px;

            /deep/ p {
                &:nth-of-type(1) {
                    margin: 30px 0 10px 0;
                }
            }

            /deep/ span {
                color: $primary-color;
            }
        }
    }

    .other-search-info {
        width: 100%;
        padding: 110px 260px 0;
        text-align: left;

        p {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #999999;
            line-height: 14px;
        }

        ul {
            margin: 20px 0 0 -10px;
            line-height: 28px;
            li {
                height: 14px;
                line-height: 14px;
                display: inline-block;
                height: 100%;
                padding: 0 10px;
                border-right: 1px solid $primary-color;

                &:last-of-type {
                    border: none;
                }
            }

            a {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #dd2619;
                line-height: 14px;
                color: $primary-color;
            }
        }
    }
</style>
