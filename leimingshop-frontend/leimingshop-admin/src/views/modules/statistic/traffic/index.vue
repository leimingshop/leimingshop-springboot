<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <!--页面流量统计-->
        <trafficPage></trafficPage>
        <trafficSource></trafficSource>
    </div>
</template>

<script>
    import { pagepv } from "@/api/api";
    import trafficPage from "./trafficPage";
    import Bread from "@/components/bread";
    import trafficSource from "./trafficSource";

    export default {
        data() {
            return {
                breaddata: ["统计", "流量分析", "流量统计"],
                chartData: {
                    columns: ["时间", "独立访客", "浏览次数"],
                    rows: [
                        { 时间: "1", 独立访客: "0", 浏览次数: "" },
                        { 时间: "2", 独立访客: "0", 浏览次数: "" },
                        { 时间: "3", 独立访客: "0", 浏览次数: "" },
                        { 时间: "4", 独立访客: "0", 浏览次数: "" },
                        { 时间: "5", 独立访客: "0", 浏览次数: "" },
                        { 时间: "6", 独立访客: "0", 浏览次数: "" },
                        { 时间: "7", 独立访客: "0", 浏览次数: "" },
                        { 时间: "8", 独立访客: "0", 浏览次数: "" },
                        { 时间: "9", 独立访客: "0", 浏览次数: "" },
                        { 时间: "0", 独立访客: "0", 浏览次数: "" },
                        { 时间: "1", 独立访客: "0", 浏览次数: "" },
                        { 时间: "0", 独立访客: "0", 浏览次数: "" },
                    ],
                },
            };
        },
        components: {
            Bread,
            trafficPage,
            trafficSource,
        },
        mounted() {},
        methods: {},
    };
</script>
