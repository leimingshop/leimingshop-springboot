<template>
    <div>
        <Bread :breaddata="breaddata"  :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormData" class="addOrEdit" ref="dataFormData" v-loading="loading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="满减活动名称:">
                {{dataFormData.activityName}}
            </el-form-item>
            <el-form-item label="生效时间:">
                {{dataFormData.startDate}} ~ {{dataFormData.endDate}}
            </el-form-item>
            <el-form-item label="活动类型:">
                <span v-if="dataFormData.ruleType == 0">普通满减</span>
                <span v-if="dataFormData.ruleType == 1">每满减</span>
                <span v-if="dataFormData.ruleType == 2">阶梯满减</span>
            </el-form-item>
            <el-form-item  label="满减规则:">
                <div v-for="(item,index) in dataFormData.reduceRuleSaveDTOList" :key="index">
                     条件-满
                {{item.limitAmount}}
                元  优惠-减{{item.reduceAmount}}元
                </div>
            </el-form-item>
            <div class="fullTitle title-1">活动规则</div>
            <el-form-item label="适用商品:">
                <span v-if="dataFormData.activityGoodsScope == 0" class="displayName">全场通用</span>

                <span v-if="dataFormData.activityGoodsScope == 1">指定分类：
                    <div v-for="(item,index) in dataFormData.reduceGoodsDTOList" :key="index" class="displayName">
                        {{item.relationName}}
                        <span style="margin: 0 10px" v-if="index<dataFormData.reduceGoodsDTOList.length-1">|</span>
                    </div>
                </span>

                <span v-if="dataFormData.activityGoodsScope == 2">指定商品：
                    <span v-for="(item,index) in dataFormData.reduceGoodsDTOList" :key="index" class="displayName">
                        {{item.relationName}}
                        <span style="margin: 0 10px" v-if="index<dataFormData.reduceGoodsDTOList.length-1">|</span>
                    </span>
                </span>

                <span v-if="dataFormData.activityGoodsScope == 3">指定品牌：
                    <div v-for="(item,index) in dataFormData.reduceGoodsDTOList" :key="index" class="displayName">
                        {{item.relationName}}
                        <span style="margin: 0 10px" v-if="index<dataFormData.reduceGoodsDTOList.length-1">|</span>
                    </div>
                </span>
            </el-form-item>
          <div style="margin-left: 180px">
              <el-button class="btn"  type="primary" plain @click="changePage">返回</el-button>
          </div>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import { editFullReduction } from '@/api/api'
    export default {
        name: "details",
        data(){
            return{
                breaddata: ["营销系统", "满减活动列表", "查看"],
                dataFormData:{
                },
                loading:true
            }
        },
        components:{
            Bread,
        },
        methods:{
            init(row){
                var obj  = {
                    id:row.id
                }
                editFullReduction(obj).then((res=>{
                    if(res.code == 200){
                        this.dataFormData = res.data
                        this.loading = false
                    }else{

                    }
                }))
            },
            changePage(){
                this.$emit('changePage')
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit{
        .fullTitle{
            font-weight: 700;
        }
        .title-1{
            margin-left: -60px;
        }
        margin-left: 10%;
        .el-form-item{
            display: flex;
        }
        .el-input{
            width: 400px !important;
        }
        /deep/ .el-form-item__label{
            margin-right: 20px;
            min-width: 130px;
            text-align: right;
        }
        .displayName{
            display: inline-block;
        }
    }
</style>
