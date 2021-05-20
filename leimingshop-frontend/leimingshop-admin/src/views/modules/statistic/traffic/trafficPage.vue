<template>
    <div>
        <div
            style="
                width: 100%;
                height: 60px;
                justify-content: space-between;
                align-items: center;
                display: flex;
                background-color: whitesmoke;
            "
        >
            <span style="margin: 27px">页面流量</span>
            <span style="margin-right: 30px">
                <statisticTimePick @getData="getData"></statisticTimePick>
            </span>
        </div>
        <div class="formControlArea">
            <div style="width: 500px">
                <div style="width: 90%; height: 50px; margin: 20px auto">
                    <p style="float: left">统计页面：</p>
                    <div style="float: left">
                        <el-select
                            @change="getPagePv"
                            v-model="statisticType"
                            placeholder="请选择"
                        >
                            <el-option
                                v-for="item in statisticTypeoptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
            </div>
            <div style="display: flex">
                <mainSwitch></mainSwitch>
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert
            title="操作提示"
            type="warning"
            @close="$store.commit('showListMessage')"
            v-show="$store.state.listMessageFlag"
        >
            <template slot="title">
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">
                    1、统计图展示了指定时间段内的页面点击流量及单位人数的时间波动变化
                </div>
                <div class="iconSize">
                    2、统计图展示了指定时间段内的流量来源PV及UV占比
                </div>
            </template>
        </el-alert>

        <div v-loading="loading">
            <div v-if="chartData.rows && chartData.rows.length != 0">
                <!-- :settings="chartSettings" -->
                <ve-line
                    :data="chartData"
                    :extend="extend2"
                    :settings="chartSettings"
                ></ve-line>
            </div>
            <div
                v-else
                style="
                    font-size: 14px;
                    text-align: center;
                    color: #909399;
                    margin-top: 20px;
                    margin-bottom: 90px;
                "
            >
                暂无数据
            </div>
        </div>
    </div>
</template>
<script>
    import { pagepv } from "@/api/api";
    import statisticTimePick from "@/components/statisticTimePick";
    export default {
        data() {
            this.extend2 = {
                //'xAxis.0.axisLabel.rotate':45,
                series: {
                    smooth: false,
                },
            };
            this.chartSettings = {
                axisSite: { left: ["浏览次数(pv)"], right: ["独立访客(uv)"] },
                yAxisName: ["单位:次", "单位:人"],
            };
            return {
                value: [],
                loading: false,
                timeType: "1",
                statisticType: "1",
                chartData: {
                    columns: ["时间", "独立访客(uv)", "浏览次数(pv)"],
                    rows: [
                        { 时间: "", "独立访客(uv)": "123", "浏览次数(pv)": "123" }, //初始化数据不能删除
                        // {'时间':'','独立访客(uv)':"123321",'浏览次数(pv)':"312"},
                    ],
                },
                dateObj: {
                    startTime: "",
                    endTime: "",
                },
                statisticTypeoptions: [
                    { id: "0", name: "全部" },
                    { id: "1", name: "首页" },
                    { id: "2", name: "商品分类页" },
                    { id: "3", name: "购物车" },
                    { id: "4", name: "商品详情页" },
                ],
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                        // return time.getTime() > Date.now() - 8.64e7;
                    },
                },
                objArgu: "",
            };
        },
        components: {
            statisticTimePick,
        },
        methods: {
            getData(objArgu) {
                this.objArgu = objArgu;
                this.getPagePv();
            },
            //  查询pv,uv数据
            getPagePv() {
                if (this.value[0] == "" || this.value[1] == "") {
                    this.$message.error("请选择时间");
                    return;
                }
                var obj = {
                    params: {
                        timeType: this.objArgu.timeType, //type,//时间类型(1:时,2:日,3:月)
                        statisticType: this.statisticType, //	统计页面(1:全站、2:首页、3:商品分类页、4:购物车、5:商品详情页)
                        startDate: this.objArgu.startDate,
                        endDate: this.objArgu.endDate,
                    },
                };
                this.loading = true;
                var that = this;
                pagepv(obj).then((res) => {
                    this.loading = false;

                    if (res.code == 200) {
                        this.chartData.rows = [];
                        var keys = Object.keys(res.data);
                        var values = Object.values(res.data);

                        values.forEach((item, index) => {
                            if (this.objArgu.timeType == 1) {
                                item["时间"] = keys[index].substring(11, 13);
                                item["时间"] = item["时间"] + ":00";
                            } else if (this.objArgu.timeType == 2) {
                                item["时间"] = keys[index].substring(5, 10);
                            } else if (this.objArgu.timeType == 3) {
                                item["时间"] = keys[index];
                            }

                            item["独立访客(uv)"] = item.uv;
                            item["浏览次数(pv)"] = item.pv;
                        });
                        this.chartData.rows = values;
                    } else if (res.code != 200) {
                        this.$message.error(res.msg);
                        this.value = "";
                    }
                });
            },
        },
    };
</script>
