<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormData" class="addOrEdit" ref="dataFormData" v-loading="loading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="秒杀活动名称:">
                {{dataFormData.activityName}}
            </el-form-item>
            <el-form-item label="生效时间:">
                {{dataFormData.activityStartDate}} ~ {{dataFormData.activityEndDate}}
            </el-form-item>
            <el-form-item label="秒杀限购数量:">
                {{ dataFormData.restrictionQuantity==0 ? '不限购' : dataFormData.restrictionQuantity}}
            </el-form-item>
            <el-form-item label="会员等级限制:">
                {{ dataFormData.memberGradeName ? dataFormData.memberGradeName : '无等级限制'}}
            </el-form-item>

            <div class="fullTitle title-1">
                优惠规则
            </div>
            <el-form-item label="秒杀活动有效期:">
                {{ dataFormData.activityEffectiveTime }}小时
            </el-form-item>
            <div style="margin-left: 180px">
                <el-button class="btn" plain @click="changePage">返回</el-button>
                <el-button class="btn" type="primary" @click="changeCompent(4, row)">查看秒杀商品</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {seckillActivityDetail} from '@/api/api'

    export default {
        name: "detail",
        data() {
            return {
                breaddata: ["营销系统", "秒杀活动列表", "查看"],
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
                seckillActivityDetail(obj).then((res => {
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
