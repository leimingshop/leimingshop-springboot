<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormData" class="addOrEdit" ref="dataFormData" v-loading="loading">
            <div class="fullTitle title-1">
                优惠券基本信息
            </div>
            <el-form-item label="优惠券名称 ">
                {{dataFormData.activityName}}
            </el-form-item>
            <el-form-item label="优惠券类型 ">
                满减券
            </el-form-item>
            <el-form-item label="领券时间 ">
                {{dataFormData.getStartDate}} ~ {{dataFormData.getEndDate}}
            </el-form-item>
            <el-form-item label="优惠券规则 ">
                单笔订单满
                {{dataFormData.limitAmount}}【门槛值】
                元，立减现金 {{dataFormData.faceValue}}【面额】元
            </el-form-item>
            <el-form-item label="优惠券有效期 ">
                <span v-if="dataFormData.validityType == 0">
                      {{dataFormData.useStartDate}}~{{dataFormData.useEndDate}}
                    固定时间</span>
                <span v-if="dataFormData.validityType == 1">
                    领取后{{dataFormData.validityDays}}天有效</span>
            </el-form-item>
            <!--useStartDate  useEndDate -->
<!--            <el-form-item label="起止时间 " v-if="dataFormData.validityType == 0">-->
<!--                {{dataFormData.useStartDate}}-->
<!--                <span style="margin: 0px 5px;">~</span>-->
<!--                {{dataFormData.useEndDate}}-->
<!--            </el-form-item>-->
<!--            <el-form-item label="有效期天数 " v-if="dataFormData.validityType == 1">-->
<!--                {{dataFormData.validityDays}}天-->
<!--            </el-form-item>-->
            <el-form-item label="每人限领券数 ">
                {{ dataFormData.personLimit==0 ? '不限量' : dataFormData.personLimit}}
            </el-form-item>
            <el-form-item label="可用范围 " style="display: flex">
                <span v-if="dataFormData.activityGoodsScope == 0">全场通用</span>
                <span v-if="dataFormData.activityGoodsScope == 1">指定店铺分类：
                     <span v-for="(item,index) in dataFormData.couponsGoodsDTOList" :key="index" class="displayName">
                           {{item.relationName}}
                           <span style="margin: 0 10px" v-if="index<dataFormData.couponsGoodsDTOList.length-1">|</span>
                       </span></span>
                <span v-if="dataFormData.activityGoodsScope == 2">指定商品：
                       <span v-for="(item,index) in dataFormData.couponsGoodsDTOList" :key="index" class="displayName">
                           {{item.relationName}}
                           <span style="margin: 0 10px" v-if="index<dataFormData.couponsGoodsDTOList.length-1">|</span>
                       </span>
                   </span>
                <span v-if="dataFormData.activityGoodsScope == 3">指定品牌：
                     <div v-for="(item,index) in dataFormData.couponsGoodsDTOList" :key="index" class="displayName">
                           {{item.relationName}}
                         <span style="margin: 0 10px" v-if="index<dataFormData.couponsGoodsDTOList.length-1">|</span>
                       </div>
                   </span>
            </el-form-item>
            <div class="fullTitle title-1">优惠劵领取信息</div>
            <el-form-item label="发券数量 ">
                {{ dataFormData.totalNum==0 ? '不限量' : dataFormData.totalNum+ '张'}}
            </el-form-item>
            <div style="margin-left: 180px">
                <el-button class="btn"  type="primary" plain @click="changePage">返回</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {editCoupons} from '@/api/api'

    export default {
        name: "details",
        data() {
            return {
                breaddata: ["营销系统", "优惠券管理", "查看"],
                dataFormData: {},
                loading: true
            }
        },
        components: {
            Bread,
        },
        methods: {
            init(row) {
                var obj = {
                    id: row.id
                }
                editCoupons(obj).then((res => {
                    if (res.code == 200) {
                        this.dataFormData = res.data
                        this.loading = false
                    } else {

                    }
                }))
            },
            changePage() {
                this.$emit('changePage')
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit {
        margin-left: 10%;

        .el-form-item {
            display: flex;
        }

        .el-form-item__content > div {
            color: #999999;
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

        .fullTitle{
            font-weight: 700;
        }
        .title-1{
            margin-left: -60px;
        }
    }
</style>
