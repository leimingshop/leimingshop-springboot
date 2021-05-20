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
            <span style="margin: 27px">交易数据</span>
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
                    1、交易数据统计图展示了指定时间段内交易维度的数据统计
                </div>
                <div class="iconSize">
                    2、近七天交易数据为昨天之前7日内的付款金额、退款金额、下单转化率等数据的趋势变化
                </div>
                <div class="iconSize">
                    3、人均消费区间对比数据统计图展示了指定时间段内各交易区间的人数占比
                </div>
                <div class="iconSize">
                    4、订单来源数据统计图展示了指定时间段内下单的来源
                </div>
            </template>
        </el-alert>
        <div class="tradeDatawarp">
            <div class="tradeDataLeft">
                <div
                    class="tradeDataTableItem"
                    v-for="(item, index) in leftTableData"
                    :key="index"
                    style=""
                >
                    <div class="tradeDataTableItemTop">{{ item.title }}</div>
                    <div class="tradeDataTableItemBottom">{{ item.value }}</div>
                </div>
            </div>
            <div class="tradeDataRight">
                <!-- :settings="chartSettings"  -->
                <ve-funnel :data="chartData1" :extend="extend"></ve-funnel>
                <div class="react react1">
                    <span class="text"
                        >成交转换率{{
                            parseInt(chartData1.lineData[2] * 100) + "%"
                        }}</span
                    >
                </div>

                <div class="react react2">
                    <span class="text"
                        >下单转换率{{
                            parseInt(chartData1.lineData[1] * 100) + "%"
                        }}</span
                    >
                </div>

                <div class="react react3">
                    <span class="text"
                        >付款转换率{{
                            parseInt(chartData1.lineData[0] * 100) + "%"
                        }}</span
                    >
                </div>
            </div>
            <div style="clear: both"></div>
        </div>
    </div>
</template>
<script>
    import { transaction } from "@/api/api";
    import statisticDatePick from "@/components/statisticDatePick";
    export default {
        data() {
            this.chartSettings = {
                dataType: "percent",
            };
            this.extend = {
                series: {
                    label: {
                        show: true,
                        position: "inside",
                    },
                },
            };
            return {
                leftTableData: [
                    { title: "浏览量", value: "" },
                    { title: "访客数", value: "" },
                    { title: "下单人数", value: "" },
                    { title: "订单数", value: "" },
                    { title: "下单件数", value: "" },
                    { title: "下单金额", value: "" },

                    { title: "退款金额", value: "" },
                    { title: "付款人数", value: "" },
                    { title: "付款订单数", value: "" },
                    { title: "付款件数", value: "" },
                    { title: "付款金额", value: "" },
                    { title: "客单价", value: "" },
                ],
                chartData1: {
                    columns: ["状态", "数值"],
                    rows: [
                        { 状态: "浏览", 数值: "" },
                        { 状态: "下单", 数值: "" },
                        { 状态: "付款", 数值: "" },
                    ],
                    lineData: [],
                },
                queryType: "1",
                valueTime: [],
                range: [],
                dateObj: {
                    startDate: "",
                    endDate: "",
                },
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                    },
                },
            };
        },
        mounted() {
            this.getData();
        },
        components: {
            statisticDatePick,
        },
        methods: {
            getData(objArgu) {
                this.objArgu = objArgu;
                this.queryData();
            },
            queryData() {
                console.log(this.objArgu);
                var obj = {
                    params: {
                        queryType: this.objArgu.queryType,
                        startDate: this.objArgu.startDate,
                        endDate: this.objArgu.endDate,
                    },
                };
                transaction(obj).then((res) => {
                    if (res.code == 200) {
                        // 表格数据
                        this.leftTableData[0].value = res.data.pageView
                            ? res.data.pageView
                            : 0; //浏览量
                        this.leftTableData[1].value = res.data.visitorsNumber; //访客数
                        this.leftTableData[2].value = res.data.submitOrderNumber; //下单人数
                        this.leftTableData[3].value = res.data.orderNumber; //订单数
                        this.leftTableData[4].value = res.data.orderGoodsNumber; //下单件数
                        this.leftTableData[5].value = res.data.submitOrderAmount; //下单金额
                        this.leftTableData[6].value = res.data.refundAmount; //退款金额
                        this.leftTableData[7].value = res.data.paymentNumber; //付款人数
                        this.leftTableData[8].value = res.data.paymentOrderNumber; //付款订单数
                        this.leftTableData[9].value = res.data.paymentGoodsNumber; //付款件数
                        this.leftTableData[10].value = res.data.paymentAmount; //付款金额
                        this.leftTableData[11].value = res.data.perTicketSales
                            ? res.data.perTicketSales
                            : 0; //客单价
                        // 右边锥形图表数据
                        //  this.tableData   = res.data;
                        this.chartData1.rows[0]["数值"] = res.data.visitorsNumber; //访客数
                        this.chartData1.rows[1]["数值"] =
                            res.data.submitOrderNumber; // res.data.submitConversion ;下单人数// "123.1" //
                        this.chartData1.rows[2]["数值"] = res.data.paymentNumber; // res.data.closingConversion ;付款人数// "123.1"//
                        this.chartData1.lineData[0] = res.data.paymentConversion; //;///"123.1"付款转化率//
                        this.chartData1.lineData[1] = res.data.submitConversion; // res.data.submitConversion ;下单转化率// "123.1" //
                        this.chartData1.lineData[2] = res.data.closingConversion; // res.data.closingConversion ;成交转化率// "123.1"//
                    } else if (res.code != 200) {
                        this.$message.error(res.msg);
                    }
                });
            },
        },
    };
</script>
<style lang="scss"  scoped>
    // 交易数据
    .tradeDatawarp {
        width: 100%;
        height: 400px;
        margin-top: 10px;
        // display:flex;
        .tradeDataLeft {
            float: left;
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            width: 982px;
            height: 198px;
            border-bottom: 1px solid #bebcbc;
            border-right: 1px solid #bebcbc;
            margin: 30px;
            .tradeDataTableItem {
                display: flex;
                justify-content: space-between;
                flex-direction: column;
                .tradeDataTableItemTop {
                    width: 163px;
                    height: 50px;
                    line-height: 50px;
                    text-align: center;
                    border-top: 1px solid #bebcbc;
                    border-left: 1px solid #bebcbc;
                    background: #f3f3f3;
                }
                .tradeDataTableItemBottom {
                    width: 163px;
                    height: 50px;
                    line-height: 50px;
                    text-align: center;
                    border-top: 1px solid #bebcbc;
                    border-left: 1px solid #bebcbc;
                }
            }
        }
        .tradeDataRight {
            position: relative;
            width: 400px;
            float: left;
            .react {
                position: absolute;
                text-align: left;
                border: 1px solid #ddd;
                border-left: 0;
                transform: skewX(-23deg);
            }
            .text {
                display: inline-block;
                transform: skewX(30deg);
                position: relative;
                width: 220px;
            }

            .react1 {
                top: 100px;
                left: 160px;
                margin-left: 100px;
                width: 100px;
                line-height: 200px;
                .text {
                    left: 80px;
                }
            }
            .react2 {
                top: 120px;
                left: 205px;
                margin-left: 70px;
                width: 50px;
                height: 80px;
                .text {
                    left: 10px;
                    top: 36px;
                }
            }
            .react3 {
                top: 210px;
                left: 185px;
                margin-left: 50px;
                width: 50px;
                height: 80px;
                .text {
                    left: 10px;
                    top: 36px;
                }
            }
        }
    }
    .sevenDayDataRight {
        .sevenDayDataRightItem {
            .sevenDayDataItemName {
                display: inline-block;
                width: 80px;
                height: 30px;
            }
            .sevenDayDataItemBt {
                margin-left: 20px;
                cursor: pointer;
            }
        }
    }
</style>