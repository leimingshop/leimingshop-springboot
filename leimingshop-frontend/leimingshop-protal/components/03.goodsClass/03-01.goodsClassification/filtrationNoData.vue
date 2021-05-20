<template>
    <div class="filtration-no-data">
        <img :src="noDataImg" alt="" />
        <p v-text="noDataText"></p>
    </div>
</template>

<script>
    export default {
        name: "filtrationNoData", // 筛选 - 空数据
        data() {
            return {
                noDataImg: require("~/static/img/03.goodsClass/03-01.goodsClassification/no-data.png"),
            };
        },

        props: {
            noDataText: {
                type: String,
                default: "搜索无结果",
            },
        },
    };
</script>

<style lang="scss" scoped>
    .filtration-no-data {
        width: 100%;
        height: 530px;
        background: #ffffff;
        text-align: center;
        img {
            width: 160px;
            height: 160px;
            margin-top: 163px;
        }
        p {
            font-size: 16px;
            color: #3a3a3a;
            margin-top: 30px;
        }
    }
</style>
