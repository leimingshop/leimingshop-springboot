<template>
    <div>
        <div
            style="
                width: 100%;
                float: flex;
                height: 60px;
                justify-content: space-between;
                display: flex;
                background-color: whitesmoke;
                align-items: center;
            "
        >
            <span style="margin: 27px">商品分类销售分析</span>
            <span style="margin-right: 30px">
                <statisticDatePick @getData="getData"></statisticDatePick>
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
                    1、商品分类销售分析数据统计图展示了指定时间段内一级分类商品的销售占比
                </div>
                <div class="iconSize">
                    2、单个商品销售情况数据统计图展示了指定时间段内销量前十的商品销售数量
                </div>
                <div class="iconSize">
                    3、单品销售明细数据统计图展示了指定时间段内前十的商品销售情况明细
                </div>
            </template>
        </el-alert>
        <div style="width:100% height:450px;margin-top:30px;">
            <div class="aaaabbb" style="display: flex; justify-content: center">
                <!-- <ve-ring :data="chartData" :settings="chartSettings"  width="400px" ></ve-ring>
            <ve-ring :data="chartData1" :settings="chartSettings" width="400px" ></ve-ring> -->

                <ve-pie
                    width="100%"
                    height="600px"
                    :tooltip="tooltip"
                    :legend="legend"
                    :toolbox="toolbox"
                    :series="series"
                ></ve-pie>
            </div>
            <div style="clear: both"></div>
        </div>
        <div style="margin-bottom: 80px; margin-top: 30px">
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="className" label="分类名称" width="180">
                </el-table-column>

                <el-table-column
                    prop="salesNumber"
                    label="销售数量"
                    width="180"
                >
                </el-table-column>

                <el-table-column prop="salesNumberProportion" label="数量比例">
                    <template slot-scope="scope">
                        <div>
                            {{
                                parseFloat(
                                    scope.row.salesNumberProportion * 100
                                ).toFixed(2) + "%"
                            }}
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="salesAmount" label="销售金额">
                </el-table-column>
                <el-table-column prop="salesAmountProportion" label="金额比例">
                    <template slot-scope="scope">
                        <div>
                            {{
                                parseFloat(
                                    scope.row.salesAmountProportion * 100
                                ).toFixed(2) + "%"
                            }}
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>
<script>
    import { goodstatis } from "@/api/api";
    import statisticDatePick from "@/components/statisticDatePick";
    export default {
        data() {
            this.chartSettings = {
                offsetY: 400,
            };
            return {
                tooltip: {
                    trigger: "item",
                    formatter: "{a} <br/>{b} : {c} ({d}%)",
                },
                legend: {
                    left: "center",
                    top: "bottom",
                    data: [],
                },
                toolbox: {
                    show: true,
                    top: "top",
                    feature: {
                        mark: { show: true },
                        // dataView: {show: true, readOnly: false},
                        magicType: {
                            show: true,
                            type: ["pie", "funnel"],
                        },
                        restore: { show: true },
                        saveAsImage: { show: true },
                    },
                },
                series: [
                    {
                        name: "半径模式",
                        type: "pie",
                        radius: [20, 110],
                        center: ["25%", "60%"],
                        roseType: false,
                        label: {
                            show: true,
                        },
                        emphasis: {
                            label: {
                                show: true,
                            },
                        },
                        data: [
                            { value: 50, name: "rose1" },
                            { value: 50, name: "rose2" },
                            { value: 15, name: "rose3" },
                            { value: 25, name: "rose4" },
                            { value: 20, name: "rose5" },
                            { value: 35, name: "rose6" },
                            { value: 30, name: "rose7" },
                            { value: 40, name: "rose8" },
                            { value: 30, name: "rose9" },
                            { value: 50, name: "rose10" },
                            { value: 15, name: "rose11" },
                            { value: 25, name: "rose12" },
                            { value: 20, name: "rose13" },
                            { value: 35, name: "rose14" },
                            { value: 30, name: "rose15" },
                        ],
                    },
                    {
                        name: "面积模式",
                        type: "pie",
                        radius: [20, 110],
                        center: ["75%", "60%"],
                        roseType: false,
                        data: [
                            // {value: '' ,name: ''},
                            { value: 50, name: "rose1" },
                            { value: 50, name: "rose2" },
                            { value: 15, name: "rose3" },
                            { value: 25, name: "rose4" },
                            { value: 20, name: "rose5" },
                            { value: 35, name: "rose6" },
                            { value: 30, name: "rose7" },
                            { value: 40, name: "rose8" },
                            { value: 30, name: "rose9" },
                            { value: 50, name: "rose10" },
                            { value: 15, name: "rose11" },
                            { value: 25, name: "rose12" },
                            { value: 20, name: "rose13" },
                            { value: 35, name: "rose14" },
                            { value: 30, name: "rose15" },
                        ],
                    },
                ],
                tableData: [],
                queryType: "1",
                valueTime: [],
                // chartData:{
                // columns:['分类名称','销售数量'],
                // rows:[
                //     // {'分类名称':'彩妆','销售数量':''},
                //     // {'分类名称':'食品','销售数量':''},
                //     // {'分类名称':'家电','销售数量':''},
                //     // {'分类名称':'厨具','销售数量':''},
                //     // {'分类名称':'男装','销售数量':''}

                //     ]
                // },
                // chartData1:{
                //   columns:['分类名称','销售金额'],
                //   rows:[
                //   // {'分类名称':'彩妆','销售金额':''},
                //   // {'分类名称':'食品','销售金额':''},
                //   // {'分类名称':'家电','销售金额':''},
                //   // {'分类名称':'厨具','销售金额':''},
                //   // {'分类名称':'男装','销售金额':''}
                //   ]
                // },
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                    },
                },
                dateObj: {
                    startDate: "",
                    endDate: "",
                },
                objArgu: "",
            };
        },
        components: {
            statisticDatePick,
        },
        mounted() {
            // this.getData();
        },
        methods: {
            getData(objArgu) {
                this.objArgu = objArgu;
                console.log("qwertytdsasdfgfdsa");
                console.log(this.objArgu);
                this.queryData();
            },
            queryData() {
                let obj = {
                    params: {
                        queryType: this.objArgu.queryType,
                        startDate: this.objArgu.startDate,
                        endDate: this.objArgu.endDate,
                    },
                };
                var that = this;
                goodstatis(obj).then((res) => {
                    if (res.code == 200) {
                        that.legend.data = [];
                        that.series[0].data = [];
                        that.series[1].data = [];
                        res.data.forEach((item, index) => {
                            // item['分类名称'] = item.className;
                            // item['销售数量'] = item.salesNumber;
                            // item['销售金额'] = item.salesAmount;

                            that.legend.data[index] = item.className;

                            // that.series[0].data[index]['name'] = item.className
                            // that.series[1].data[index]['name'] = item.className

                            that.legend.data.push(item.className);
                            that.series[0].data.push({
                                value: item.salesNumber,
                                name: item.className,
                            });
                            that.series[1].data.push({
                                value: item.salesAmount,
                                name: item.className,
                            });
                            console.log(that);
                        });

                        // this.chartData.rows=  res.data
                        // this.chartData1.rows= res.data
                        this.tableData = res.data;
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                        });
                    }
                });
            },
        },
    };
</script>  
<style lang="scss" scoped>
    /deep/ .ve-ring {
        div {
            div {
                width: 400px !important;
            }
        }
    }
    .ringLeft .ringRight {
        width: 50%;
        float: left;
    }
</style>