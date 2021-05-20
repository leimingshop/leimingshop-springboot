<template>
    <div class="search-no-data">
        <img :src="noDataImg" alt="" />
        <p v-text="noDataText"></p>
        <Button size="large" type="primary" shape="circle" @click="handleRouter"
            >马上去购物</Button
        >
    </div>
</template>

<script>
    export default {
        name: "searchNoData",
        data() {
            return {
                noDataImg: require("~/static/img/06.personalCenter/06-03.afterSale/afterSaleEmpty.png"),
                noDataText: "您还没有相关服务单",
            };
        },
        components: {},
        created() {},
        computed: {},
        watch: {},
        mounted() {},
        methods: {
            handleRouter() {
                this.$router.push({
                    path: "/HomeIndex",
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .search-no-data {
        padding-top: 120px;
        text-align: center;
        img {
            margin: 0 auto;
        }

        p {
            font-size: 18px;
        }

        button {
            width: 182px;
            letter-spacing: 0.5px;
            margin: 30px 0 100px;
        }
    }
</style>
