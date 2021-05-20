<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormData" class="addOrEdit" ref="dataFormData" v-loading="loading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="限时抢购活动名称:">
                {{dataFormData.activityName}}
            </el-form-item>
            <el-form-item label="生效时间:">
                {{dataFormData.activityStartDate}} ~ {{dataFormData.activityEndDate}}
            </el-form-item>
            <el-form-item label="限时抢购限购数量:">
                {{ dataFormData.restrictionQuantity==0 ? '不限购' : dataFormData.restrictionQuantity}}
            </el-form-item>
            <el-form-item label="会员等级限制:">
                {{ dataFormData.memberGradeName ? dataFormData.memberGradeName : '无等级限制'}}
            </el-form-item>

            <!-- <div class="fullTitle title-1">
                优惠规则
            </div> -->
            <el-form-item label="订单支付有效时间:">
                {{ dataFormData.payValidTime }}&nbsp;&nbsp;分钟
            </el-form-item>
            <el-form-item label="下单可用抵扣:">
                {{dataFormData.pointFlag == 1 ? "积分" : ""}}
                <span v-if="dataFormData.pointFlag == 1">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                {{dataFormData.balanceFlag == 1 ? "余额" : ""}}
   
            </el-form-item>
            <el-form-item label="下单可用优惠:">
                {{dataFormData.couponsFlag == 1 ? "优惠券" : ""}}
                <span v-if="dataFormData.couponsFlag == 1">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                {{dataFormData.reduceFlag == 1 ? "满减" : ""}}
            </el-form-item>
            <div style="margin-left: 180px">
                <el-button class="btn" plain @click="changePage">返回</el-button>
                <el-button class="btn" type="primary" @click="changeCompent(4, row)">查看限时抢购商品</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {flashSaleActivityDetail} from '@/api/api'

    export default {
        name: "detail",
        data() {
            return {
                breaddata: ["营销系统", "限时抢购活动列表", "查看"],
                dataFormData: {},
                row: '',
                loading: true
            }
        },
        components: {
            Bread,
        },
        methods: {
            init(row) {
                this.row = row;
                var obj = {
                    id: row.id
                }
                flashSaleActivityDetail(obj).then((res => {
                    if (res.code == 200) {
                        this.dataFormData = res.data
                        this.loading = false
                    } else {

                    }
                }))
            },
            changePage() {
                this.$emit('changePage')
            },
            changeCompent(type, row) {
                this.$emit('changeCompent', type, row);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit {
        .fullTitle {
            font-weight: 700;
            font-size: 25px;
        }
        .title-1 {
            margin-left: -60px;
        }
        margin-left: 10%;
        .el-form-item {
            display: flex;
        }
        .el-input {
            width: 400px !important;
        }
        /deep/ .el-form-item__label {
            margin-right: 20px;
            min-width: 130px;
            text-align: right;
        }
        .displayName {
            display: inline-block;
        }
    }
</style>
