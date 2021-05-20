<template>
    <div v-loading="dataLoding">
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
            <div style="display:flex;">
                <!-- <mainSwitch></mainSwitch> -->
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
            <template slot='title'>
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">1、计算周期：成长值的计算周期，超过该周期的成长值将会被清零，选择永久代表不清零；可选择永久、1月、3月、6月、12月、18月、24月</div>
                <div class="iconSize">2、计算时间：每个月计算一次，计算时间可选1-28其中任意一天，计算后会保留一个月，直到下次计算才会被更改 （例如：周期选择12个月，计算时间1号；2019.12.01号，会计算2018.12.01-2019.12.01之间的成长值，2018.12.01号之前的成长值会清零）</div>
            </template>
        </el-alert>
        <div class="topMoudel">
            <h3>成长值规则设置</h3>
            <el-form
                    :model="dataForm"
                    ref="dataForm"
                    label-width="150px"
                    class="demo-ruleForm artinputcontent"
            >
                <el-form-item label="名称：" prop="cancelOrder">
                    <div>
                        <el-input
                                placeholder="成长值"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.name"
                        >
                        </el-input>
                    </div>
                </el-form-item>
                <el-form-item label="购物消费：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.shoppingConsumption"
                        >
                            <template slot="append">元</template>
                        </el-input> 奖励1分成长值（不含运费）
                    </div>
                </el-form-item>
                <el-form-item label="订单实际支付金额低于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.minOrderPay"
                        >
                            <template slot="append">元</template>
                        </el-input> 不送成长值
                    </div>
                </el-form-item>
                <el-form-item label="单件商品最高可获得：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.maxPiecesGoods"
                        >
                            <template slot="append">分</template>
                        </el-input> 成长值
                    </div>
                </el-form-item>
                <el-form-item label="评价大于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.evaluationWordCount"
                        >
                            <template slot="append">字</template>
                        </el-input> 奖励
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.evaluationWordGrowth"
                        >
                            <template slot="append">分</template>
                        </el-input> 成长值
                    </div>
                </el-form-item>
                <el-form-item label="评价图片大于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.evaluationPicCount"
                        >
                            <template slot="append">张</template>
                        </el-input> 奖励
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.evaluationPicGrowth"
                        >
                            <template slot="append">分</template>
                        </el-input> 成长值
                    </div>
                </el-form-item>
                <el-form-item label="商品单价低于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.goodsPriceMinGrowth"
                        >
                            <template slot="append">元</template>
                        </el-input> 评价不送成长值
                    </div>
                </el-form-item>
                <el-form-item label="计算周期：" prop="cancelOrder">
                    <div>
                        <el-select v-model="dataForm.growthRule.calculationCycle" placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select> 超过该周期的成长值将会被清零，选择永久代表不清零
                    </div>
                </el-form-item>
                <el-form-item label="计算时间：" prop="cancelOrder">
                    <div>
                        <el-input
                                placeholder="1-28"
                                :min="1"
                                type="number"
                                :max="28"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.growthRule.calculationTime"
                        >
                            <template slot="append">号</template>
                        </el-input> 每月
                    </div>
                </el-form-item>
            </el-form>
        </div>

        <div class="topMoudel">
            <h3>积分规则设置</h3>
            <el-form
                    :model="dataForm"
                    ref="dataForm"
                    label-width="150px"
                    class="demo-ruleForm artinputcontent"
            >
                <el-form-item label="名称：" prop="cancelOrder">
                    <div>
                        <el-input
                                placeholder="积分"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.name"
                        >
                        </el-input>
                    </div>
                </el-form-item>
                <el-form-item label="购物消费：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.shoppingConsumption"
                        >
                            <template slot="append">元</template>
                        </el-input> 奖励1个积分（不含运费）
                    </div>
                </el-form-item>
                <el-form-item label="订单实际支付金额低于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.minOrderPay"
                        >
                            <template slot="append">元</template>
                        </el-input> 不送积分
                    </div>
                </el-form-item>
                <el-form-item label="单件商品最高可获得：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.maxPiecesGoods"
                        >
                            <template slot="append">个</template>
                        </el-input> 积分
                    </div>
                </el-form-item>
                <el-form-item label="评价大于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.evaluationWordCount"
                        >
                            <template slot="append">字</template>
                        </el-input> 奖励
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.evaluationWordGrowth"
                        >
                            <template slot="append">个</template>
                        </el-input> 积分
                    </div>
                </el-form-item>
                <el-form-item label="评价图片大于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.evaluationPicCount"
                        >
                            <template slot="append">张</template>
                        </el-input> 奖励
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.evaluationPicGrowth"
                        >
                            <template slot="append">个</template>
                        </el-input> 积分
                    </div>
                </el-form-item>
                <el-form-item label="商品单价低于：" prop="cancelOrder">
                    <div>
                        <el-input
                                :min="0"
                                type="number"
                                :max="1440"
                                onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                                autocomplete="off"
                                v-model="dataForm.pointRule.goodsPriceMinGrowth"
                        >
                            <template slot="append">元</template>
                        </el-input> 评价不送积分
                    </div>
                </el-form-item>
                <el-form-item class="regule" label="清零规则：" prop="cancelOrder">
                    <div>
                        <el-radio v-model="dataForm.pointRule.clearRule" :label="number1">不清零</el-radio>
                        <el-radio v-model="dataForm.pointRule.clearRule" :label="number2">自然年清零上一年获得的积分 每年12月31日清空上一年获得的积分</el-radio>
                        <el-radio v-model="dataForm.pointRule.clearRule" :label="number3">自然年清零所有积分 每年12月31日清空所有积分</el-radio>
                    </div>
                </el-form-item>
            </el-form>
        </div>
        <div  class="bottombtWarp">
            <el-button type="primary" @click="dataFormSubmit()" v-if="$hasPermission('sys:rule:save')">{{saveLoading?"保存中":"保存"}}</el-button>
        </div>
    </div>
</template>

<script>
    import {getMoresettings,updateMoresettings} from "@/api/api.js"
    import Bread from "@/components/bread";
    export default {
        data() {
            return {
                number1:0,
                number2:1,
                number3:2,
                options: [{
                    value: 0,
                    label: '永久'
                }, {
                    value: 1,
                    label: '1月'
                }, {
                    value: 3,
                    label: '3月'
                }, {
                    value: 6,
                    label: '6月'
                }, {
                    value: 12,
                    label: '12月'
                }, {
                    value: 18,
                    label: '18月'
                }, {
                    value: 24,
                    label: '24月'
                }],
                dataLoding:false,
                saveLoading:false,
                breaddata: ["会员管理", "积分及成长值","更多规则设置"],
                dataForm:{
                    "growthRule": {
                        "calculationCycle": 0,
                        "calculationTime": 0,
                        "evaluationPicCount": 0,
                        "evaluationPicGrowth": 0,
                        "evaluationWordCount": 0,
                        "evaluationWordGrowth": 0,
                        "goodsPriceMinGrowth": 0,
                        "maxPiecesGoods": 0,
                        "minOrderPay": 0,
                        "name": "string",
                        "shoppingConsumption": 0
                    },
                    "pointRule": {
                        "clearRule": 0,
                        "evaluationPicCount": 0,
                        "evaluationPicGrowth": 0,
                        "evaluationWordCount": 0,
                        "evaluationWordGrowth": 0,
                        "goodsPriceMinGrowth": 0,
                        "maxPiecesGoods": 0,
                        "minOrderPay": 0,
                        "name": "string",
                        "shoppingConsumption": 0
                    }
                }
            };
        },
        components: {
            Bread
        },
        created() {
            this.backScan();
        },
        methods: {
            backScan(){
                this.dataLoding=true
                getMoresettings().then((res)=>{
                    this.dataLoding=false
                    if(res.code==200){
                        this.dataForm = res.data;
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            },
            dataFormSubmit(){
                var obj = this.dataForm;
                this.saveLoading = true;
                updateMoresettings(obj).then((res)=>{
                    this.saveLoading = false;
                    if(res.code==200){
                        this.$message.success(res.msg);
                        this.backScan();
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            }
        }
    };
</script>
<style  lang="scss" scoped>

    .el-dialog {
        width: 40%;
    }
    h3{
        margin-top: 40px;
        span{
            font-weight: 400;
            font-size: 14px;
            color: #CCCCCC;
        }
    }
    // 顶部
    .topMoudel{
        .topItem{
            display: flex;
            .topItemLeft{
                display: inline-block;
                width: 246px;
                height: 50px;
                line-height: 50px;
                text-align: right;
                padding-right: 10px;
                background-color: #f0f2f5;
                border:1px solid #ebebeb;
            }
            .topItemRight{
                width: 994px;
                height: 50px;
                line-height: 50px;
                text-align: left;
                display: flex;
                align-items: center;
                padding-left: 10px;
                border:1px solid #ebebeb;
                justify-content: flex-start;
            }
        }
    }
    .bottombtWarp{
        margin: 40px auto 40px;
        text-align: center;

    }
    .el-input-group {
        width: 200px;
    }
    .el-input {
        width: 200px;
    }
    /deep/ .el-form-item__label {
        width: 160px !important;
    }
    .el-radio+.el-radio {
        margin-left: 0px;
    }
     .el-radio {
         width: 100%;
     }
    .regule{
        display: flex;
        /deep/ .el-form-item__content{
            margin-left: 0px !important;
        }
    }


</style>
