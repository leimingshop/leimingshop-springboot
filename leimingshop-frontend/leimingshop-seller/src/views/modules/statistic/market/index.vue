<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
            <div style="display: flex">
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
                    1、展示满减和优惠券的浏览量、访问量、领取量等数据的分析
                </div>
                <div class="iconSize">
                    2、营销趋势可根据指标选择进行趋势分析
                </div>
            </template>
        </el-alert>

        <div>
            <div
                style="
                    width: 98%;
                    float: flex;
                    height: 76px;
                    justify-content: space-between;
                    display: flex;
                    align-content: center;
                    line-height: 76px;
                "
            >
                <div style="margin-right: 20px">
                    <el-form :inline="true">
                        <el-form-item>
                            <el-select
                                v-model="activityType"
                                @change="changeActivityType"
                            >
                                <el-option label="优惠券" value="1"></el-option>
                                <el-option label="满减活动" value="2">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-select
                                v-model="activityId"
                                @change="changeActivityId"
                            >
                                <el-option
                                    v-for="item in activityNameOpction"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                >
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="margin-top: -17px">
                    <statisticDatePick @getData="getData"></statisticDatePick>
                </div>
            </div>
            <el-card style="margin-top: -11px">
                <span style="color: rgb(230, 162, 60)"
                    >数据说明：该数据为您显示：营销活动数据效果，为活动复盘提供依据</span
                >
            </el-card>
        </div>
        <div>
            <div
                style="
                    width: 100%;
                    margin-top: 15px;
                    float: flex;
                    height: 60px;
                    justify-content: space-between;
                    display: flex;
                    background-color: whitesmoke;
                    align-items: center;
                "
            >
                <span style="margin: 27px">营销概况</span>
            </div>
            <el-card
                style="padding-bottom: 17px; margin-top: 8px"
                v-if="activityStatisDTO"
            >
                <div class="marketCard" style="margin-top: -12px">
                    <div class="goodsInfoClass">
                        <span style="font-size: 17px">活动曝光</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>浏览量</label>
                        <br />
                        <span>{{ activityStatisDTO.pageView }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>访客量</label>
                        <br />
                        <span>{{ activityStatisDTO.visitorsNumber }}</span>
                    </div>
                    <div class="goodsInfoClass" v-if="this.activityType == 1">
                        <label>领取量</label>
                        <br />
                        <span>{{ activityStatisDTO.personNum }}</span>
                    </div>
                </div>
                <div class="marketCard">
                    <div class="goodsInfoClass">
                        <span style="font-size: 17px">活动下单</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>下单订单数</label>
                        <br />
                        <span>{{ activityStatisDTO.downOrderNum }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>下单人数</label>
                        <br />
                        <span>{{ activityStatisDTO.downOrderPerson }}</span>
                    </div>
                    <div class="goodsInfoClass" v-if="this.activityType == 1">
                        <label>下单使用量</label>
                        <br />
                        <span>{{ activityStatisDTO.userOrderNum }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>下单金额</label>
                        <br />
                        <span>{{ activityStatisDTO.orderAmount }}</span>
                    </div>
                </div>
                <div class="marketCard">
                    <div class="goodsInfoClass">
                        <span style="font-size: 17px">支付转化</span>
                    </div>

                    <div class="goodsInfoClass">
                        <label>支付订单数</label>
                        <br />
                        <span>{{ activityStatisDTO.payOrderNum }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>支付人数</label>
                        <br />
                        <span>{{ activityStatisDTO.payPerson }}</span>
                    </div>
                    <div class="goodsInfoClass" v-if="this.activityType == 1">
                        <label>支付使用量</label>
                        <br />
                        <span>{{ activityStatisDTO.payUseNum }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>应收金额</label>
                        <br />
                        <span>{{ activityStatisDTO.realityAmount }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>实收金额</label>
                        <br />
                        <span>{{ activityStatisDTO.payAmount }}</span>
                    </div>
                    <div class="goodsInfoClass">
                        <label>该活动减免金额</label>
                        <br />
                        <span>{{
                            activityStatisDTO.activityReduceAmount
                        }}</span>
                    </div>
                </div>
            </el-card>
        </div>

        <!-- <marketing></marketing> -->
        <marketingTrend ref="marketingTrendCompon"></marketingTrend>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { activity } from "@/api/api";
    import statisticDatePick from "@/components/statisticDatePick";
    import marketingTrend from "./marketingTrend";
    export default {
        data() {
            return {
                breaddata: ["统计", "商品分析", "营销统计"],
                // dateObj:{
                //     startDate:'',
                //     endDate:'',
                //     queryType:'1',
                //     activityId:"",
                // },

                activityNameOpctionInit: [
                    {
                        value: "",
                        label: "全部",
                    },
                ],
                activityNameOpction: [],
                activityStatisDTO: "",
                activity: "",

                activityType: "1", //  活动类型1 优惠券 2 满减
                activityId: "", // 活动ID
                activityName: "", //活动名称
                objArgu: {
                    queryType: "",
                    startDate: "",
                    endDate: "",
                },
            };
        },
        components: {
            Bread,
            statisticDatePick,
            marketingTrend,
        },
        activated() {
            this.handleUrlData();

            // if(this.$route.query.activityName){
            //     this.value='2'
            //     this.activityName=[{
            //         value:'1',
            //         label:'全部'
            //     },{
            //         value:'2',
            //         label:this.$route.query.activityName
            //     }]
            // }

            // if(this.$route.query.activityType){
            //     this.activityType=this.$route.query.activityType
            //     this.dateObj.activityId=this.$route.query.activityId
            //     this.activityId=this.$route.query.activityId
            //     this.queryData();
            // }
        },
        mounted() {
            // this.getData();
        },
        methods: {
            handleUrlData() {
                this.activityNameOpction = [].concat(this.activityNameOpctionInit);
                if (!this.$route.query.activityId) {
                    return;
                }
                this.activityType = this.$route.query.activityType;
                this.activityId = this.$route.query.activityId;
                this.activityName = this.$route.query.activityName;
                this.objArgu.queryType = 1;
                if (this.activityId) {
                    this.activityNameOpction.push({
                        value: this.activityId,
                        label: this.activityName,
                    });
                    this.queryData();
                }
            },
            getData(objArgu) {
                this.objArgu = objArgu;
                setTimeout(() => {
                    this.queryData();
                }, 100);
            },
            // 切换活动类型
            changeActivityType() {
                this.activityId = "";
                this.activityNameOpction = [].concat(this.activityNameOpctionInit);
                this.queryData();
            },
            // 切换活动
            changeActivityId(value) {
                this.queryData();
            },

            queryData() {
                let obj = {
                    params: {
                        queryType: this.objArgu.queryType, // 查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)
                        startDate: this.objArgu.startDate,
                        endDate: this.objArgu.endDate,
                        activityType: this.activityType, //活动类型1 优惠券 2 满减
                        activityId: this.activityId, //  活动ID
                    },
                };
                activity(obj).then((res) => {
                    this.activityStatisDTO = [];
                    if (res.code == 200) {
                        this.activityStatisDTO = res.data.activityStatisDTO;
                        this.$nextTick(() => {
                            this.$refs.marketingTrendCompon.init(
                                obj.params,
                                res.data
                            );
                        });
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .marketCard {
        margin-top: 30px;
        display: flex;
        height: 90px;
    }
    .goodsInfoClass {
        margin-left: -41px;
        display: flex;
        flex-direction: column;
        font-size: 15px;
        color: #333;
        font-weight: bold;
        text-align-last: center;
        width: 13%;
        span {
            font-size: 15px;
            color: black;
            font-weight: bold;
            margin-top: 20px;
        }
        label {
            font-size: 15px;
            color: #333;
            font-weight: bold;
            margin-top: 20px;
        }
    }
    .el-form--inline {
        /deep/.el-input {
            height: 40px !important;
        }
    }
</style>
