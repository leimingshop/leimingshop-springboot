<template>
    <div>
        <div
            style="
                width: 100%;
                float: flex;
                height: 60px;
                justify-content: space-between;
                align-items: center;
                display: flex;
                background-color: whitesmoke;
            "
        >
            <span style="margin: 27px">会员增长统计</span>
            <span style="margin-right: 30px">
                <statisticTimePick @getData="getData"></statisticTimePick>
            </span>
        </div>
        <div class="formControlArea">
            <div></div>
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
                    1、会员增长数据统计图展示了指定时间段内会员的注册趋势
                </div>
                <div class="iconSize">
                    2、会员来源数据统计图展示了指定时间段内各端注册的占比
                </div>
            </template>
        </el-alert>
        <div v-loading="loading">
            <div v-if="chartData.rows && chartData.rows.length != 0">
                <!-- :settings="chartSettings" -->
                <ve-line :data="chartData" :extend="extend"></ve-line>
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
    import { statis } from "@/api/api";
    import { type } from "os";
    import statisticTimePick from "@/components/statisticTimePick";
    export default {
        data() {
            this.extend = {
                series: {
                    smooth: false,
                },
            };
            return {
                loading: false,
                value: [],
                timeType: "1",
                dateObj: {
                    startDate: "",
                    endDate: "",
                },
                chartData: {
                    columns: ["时间", "人数"],
                    rows: [],
                },
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                        // return time.getTime() > Date.now() - 8.64e7;
                    },
                },
            };
        },
        components: {
            statisticTimePick,
        },
        methods: {
            getData(objArgu) {
                this.objArgu = objArgu;
                this.getGrowStatis();
            },
            //会员增长统计展示
            getGrowStatis() {
                // var currentDateStamp = new Date().getTime();

                // this.getTime();

                if (!this.objArgu.startDate || !this.objArgu.endDate) {
                    this.$message.error("请选择时间");
                    return;
                }
                var obj = {
                    params: {
                        timeType: this.objArgu.timeType,
                        // startDate:this.dateObj.startDate,
                        // endDate:this.dateObj.endDate,
                        startDate: this.objArgu.startDate,
                        endDate: this.objArgu.endDate,
                    },
                };
                this.loading = true;
                statis(obj).then((res) => {
                    this.loading = false;
                    if (res.code == 200) {
                        // this.chartData.rows['人数']=res.data.memberIncreaseNumber;
                        this.chartData.rows = [];
                        res.data.forEach((item, index) => {
                            if (this.objArgu.timeType == 1) {
                                item["时间"] = item.createHourTime.substring(
                                    11,
                                    13
                                );
                                item["时间"] = item["时间"] + ":00";
                            } else if (this.objArgu.timeType == 2) {
                                item["时间"] = item.createDayTime.substring(5, 10);
                            } else if (this.objArgu.timeType == 3) {
                                item["时间"] = item.createMonthTime.substring(0, 7);
                            }
                            item["人数"] = item.memberIncreaseNumber;
                            //   console.log(item);
                            //   this.chartData.rows.push({
                            //      "时间":item.createMonthTime,
                            //      "人数":item.memberIncreaseNumber,
                            //   })
                        });
                        this.chartData.rows = res.data;
                        console.log("this.chartData");
                        console.log(this.chartData);
                    } else {
                        this.$message.error(res.msg);
                        this.value = "";
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
</style>
