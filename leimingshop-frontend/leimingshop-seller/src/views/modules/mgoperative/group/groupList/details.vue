<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormData" class="addOrEdit" ref="dataFormData" v-loading="loading">
            <div class="fullTitle title-1">
                拼团基本信息
            </div>
            <el-form-item label="活动名称 ">
                {{dataFormData.groupName}}
            </el-form-item>
            <el-form-item label="活动时间 ">
                {{dataFormData.startTime}} ~ {{dataFormData.endTime}}
            </el-form-item>
            <el-form-item label="参团用户条件 ">
                <span v-if="dataFormData.joinFlag == 0">无限制</span>
                <span v-else-if="dataFormData.joinFlag == 1">新用户</span>
            </el-form-item>
            <el-form-item label="活动预热 ">
                活动开始前 {{dataFormData.groupPreheat}}小时，商品详情页显示活动信息
            </el-form-item>
            <el-form-item label="推荐拼团 ">
                <span v-if="dataFormData.recommendFlag == 0">开启</span>
                <span v-else-if="dataFormData.recommendFlag == 1">关闭</span>
            </el-form-item>
            <el-form-item label="模拟成团 ">
                <span v-if="dataFormData.simulateFlag == 0">开启</span>
                <span v-else-if="dataFormData.simulateFlag == 1">关闭</span>
            </el-form-item>
            <el-form-item label="成团有效时间 ">
                {{dataFormData.validTime}}小时
            </el-form-item>
            <el-form-item label="订单支付有效期 ">
                {{dataFormData.payEndTime}}分钟
            </el-form-item>

<!--            <el-form-item label="下单使用优惠 "></el-form-item>-->
<!--            <el-form-item label="下单可用抵扣 "></el-form-item>-->

            <div style="margin-left: 180px">
                <el-button class="btn"  type="primary" plain @click="changePage">返回</el-button>
                <el-button class="btn"  type="primary" plain @click="gotoGoods">查看拼团商品</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {groupDetails} from '@/api/api'

    export default {
        name: "details",
        data() {
            return {
                breaddata: ["营销系统", "拼团管理", "拼团查看"],
                dataFormData: {},
                loading: true,
                row:{}
            }
        },
        components: {
            Bread,
        },
        computed:{
            dataRuleOther(){
                return {
                    logTitle: [
                        { required: true, message: '更新日志标题不能为空', trigger: 'blur' },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    logVersion: [
                        { required: true, message: '版本号不能为空', trigger: 'blur' },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    logType: [
                        { required: true, message: '更新日志类型不能为空', trigger: 'change' },
                        // { validator: validateComfirmPassword2, trigger: 'blur' }
                    ],
                    saveDate: [
                        { required: true, message: '发版日期不能为空', trigger: 'blur' }
                    ],
                    logDec: [
                        { required: true, message: '日志更新内容不能为空', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            init(row) {
                console.log(row)
                this.row = row
                var obj = {
                    id: row.id
                }
                console.log(obj)
                groupDetails(obj).then((res => {
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
            gotoGoods(){
                this.$emit('changeCompent', 7, this.row)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit {
        margin-left: 10%;
        .fullTitle{
            font-weight: 700;
            height: 40px;
            line-height: 40px;
        }
        .title-1{
            margin-left: -60px;
        }
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
