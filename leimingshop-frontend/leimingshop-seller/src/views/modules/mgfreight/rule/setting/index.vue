<template>
    <div v-loading="dataLoading">
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
                    1、运费设置针对一笔订单中，存在使用不同运费模板的商品，的运费计算方式
                </div>
                <div class="iconSize">
                    2、叠加运费：当一个订单中的商品使用多个运费模板时，将每个商品的运费之和作为订单总运费
                </div>
                <div class="iconSize">
                    3、最高运费：当一个订单中的商品使用多个运费模板时，取订单中运费最多的商品的运费作为订单总运费
                </div>
                <div class="iconSize">
                    4、最低运费：当一个订单中的商品使用多个运费模板时，取订单中运费最低的商品的运费作为订单总运费
                </div>
            </template>
        </el-alert>

        <el-row>
            <el-col :span="22" :offset="1">
                <div class="grid-content bg-purple-dark">
                    <p
                        style="
                            font-size: 20px;
                            font-family: 'Arial Negreta', 'Arial Normal',
                                'Arial';
                        "
                    >
                        运费计费方式
                    </p>
                    <el-radio-group v-model="ruleType">
                        <div>
                            <el-radio class="label" :label="0"
                                >叠加运费计算</el-radio
                            >
                            <span class="labelValue"
                                >订单中的商品有多个运费模板时，将每个商品的运费之和订为订单总运费</span
                            >
                        </div>
                        <div>
                            <el-radio class="label" :label="1"
                                >最高运费计算</el-radio
                            >
                            <span class="labelValue"
                                >订单中的商品有多个运费模板时，取订单中运费最多的商品的运费计为订单总运费</span
                            >
                        </div>
                        <div>
                            <el-radio class="label" :label="2"
                                >最低运费计算</el-radio
                            >
                            <span class="labelValue"
                                >订单中的商品有多个运费模板时，取订单中运费最少的商品的运费计为订单总运费</span
                            >
                        </div>
                        <!-- <div>
                            <el-radio class="label" :label="3" >智能组合计算</el-radio>
                            <span class="labelValue">
                            1）取最大首重价格的模版首重作为订单首重价格和首重门槛，然后你分别计算商品独自的续重价格。<br/>
                            2）当有最大首重价格相同，首重单位价格相同，续重价格不同的模版时，选取最小续重的模版作为订单首重。<br/>
                            3）当有最大首重价格相同，单位价格不同时，取单位价格最大的模版作为订单的首重。<br/>
                            4）包邮商品不参与运费计算
                        </span>
                        </div> -->
                    </el-radio-group>
                </div>
            </el-col>
        </el-row>

        <div style="display: flex; justify-content: center; padding-top: 50px">
            <el-button
                v-loading="submitLoading"
                type="primary"
                @click="editFreightRuleSetting()"
                v-if="$hasPermission('sys:freight:rule:edit')"
            >
                保存
            </el-button>
        </div>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { getFreightRuleSetting, editFreightRuleSetting } from "@/api/api";

    export default {
        data() {
            return {
                breaddata: ["物流管理", "运费设置"],
                ruleType: "",
                dataLoading: false,
                submitLoading: false,
            };
        },
        components: {
            Bread,
        },
        created() {
            // 获取运费规则设置
            this.selectFreightRuleSetting();
        },
        methods: {
            // 查询运费规则设置
            selectFreightRuleSetting() {
                this.dataLoading = true;
                getFreightRuleSetting().then((res) => {
                    this.dataLoading = false;
                    if (res.code == 200) {
                        this.ruleType = res.data.ruleType;
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            // 修改运费规则
            editFreightRuleSetting() {
                let obj = {
                    ruleType: this.ruleType,
                };
                this.submitLoading = true;
                editFreightRuleSetting(obj).then((res) => {
                    this.submitLoading = false;
                    if (res.code == 200) {
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500,
                        });
                        this.selectFreightRuleSetting();
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .label {
        padding-left: 50px;
        font-size: 15px;
        padding-top: 25px;
        display: inline-block;
        vertical-align: top;
    }

    .labelValue {
        padding-top: 25px;
        padding-left: 100px;
        font-size: 15px;
        width: 1000px;
        display: inline-block;
        vertical-align: top;
        line-height: 2;
        color: gray;
    }
</style>
