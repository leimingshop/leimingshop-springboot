<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataForm" class="addOrEdit" :rules="dataRule" ref="dataForm"
                 v-loading="flashSaleLoading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="活动名称：" prop="activityName">
                <el-input v-model="dataForm.activityName" placeholder="请输入活动名称" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <div class="hrTips">活动名称最长不超过20个汉字。</div>
            
            <el-form-item label="生效时间：" prop="activityName">
                <!-- <el-input v-model="dataForm.activityName" placeholder="请输入活动名称" maxlength="20" show-word-limit></el-input> -->
                <el-date-picker
                        v-model="valuetime"
                        type="datetimerange"
                        align="right"
                        unlink-panels
                        :picker-options="pickerOptions"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        @change='acttime'>
                </el-date-picker>
            </el-form-item>
            <div class="hrTips">活动到期后将自动失效，失效后不可延长</div>
            <el-form-item label="限购数量：" prop="restrictionQuantity" style="height: 42px">
                <el-input-number v-model="dataForm.restrictionQuantity" placeholder="请输入限时抢购商品限购数量" :min="0"
                                 :max="99999"></el-input-number>
            </el-form-item>
            <div class="hrTips">活动价限购数量为该活动下的商品每个用户可以限时购价购物的商品数量，输入0或不填为不限购</div>
            <!-- <el-form-item label="会员等级限制：">
                <el-select v-model="dataForm.memberGradeId"
                           filterable
                           placeholder="请选择会员等级">
                    <el-option label="无限制" value></el-option>
                    <el-option
                            v-for="item in memberGradeList"
                            :key="item.id"
                            :label="item.gradeName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item> -->
             <el-form-item label="会员等级限制：">
                <el-select v-model="dataForm.memberGradeId"
                           filterable
                           placeholder="请选择会员等级"
                >
                    <el-option label="无限制" value></el-option>
                    <el-option
                            v-for="item in memberGradeList"
                            :key="item.id"
                            :label="item.gradeName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <div class="hrTips">非必填项，默认选择为无等级限制，可选择新用户或其他等级，选择限制条件后只有满足该会员等级条件用户可参加该限时抢购活动</div>
        
            <el-form-item label="活动订单支付有效期：" prop="payValidTime">
                <el-input-number v-model="dataForm.payValidTime" :min="1" :max="1440"></el-input-number>&nbsp;分钟
            </el-form-item>
            <div class="hrTips">默认为运营平台统一设置的时间，如无特殊要求无需修改（设置范围1-1440）</div>

            <el-form-item label="下单可用抵扣：" prop="deduction">
                <el-checkbox-group v-model="deduction"  >
                    <el-checkbox :label="1" >余额</el-checkbox>
                    <!-- <el-checkbox :label="2" >积分</el-checkbox> -->
                </el-checkbox-group>
            </el-form-item>
            <div class="hrTips">仅勾选下单抵扣方式，可同时勾选</div>

            <el-form-item label="下单可用优惠：" prop="discount">
                <el-checkbox-group v-model="discount"  >
                    <el-checkbox :label="1" >优惠券</el-checkbox>
                    <!-- <el-checkbox :label="2" >满减活动</el-checkbox> -->
                </el-checkbox-group>
                    <!-- <el-radio v-model="discount" @change="setPointFlag" label=1>优惠券</el-radio>
                    <el-radio v-model="discount" @change="setReduceFlag" label=2>满减活动</el-radio> -->
            </el-form-item>
            <div class="hrTips">仅勾选下单可用优惠，可同时勾选</div>


            <el-form-item class="title-2">

                <el-button class="btn" type="primary" @click="dataFormSubmit('dataForm')" :loading="saveLoading">
                    {{saveLoading?"提交中...":"确认"}}
                </el-button>
                <el-button class="btn" type="primary" @click="savaAndSub('dataForm')" :loading="saveAddLoading">
                    {{saveAddLoading?"提交中...":"保存并添加商品"}}
                </el-button>
                <el-button class="btn" type="primary" plain @click="changePage()">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {
        saveFlashSaleActivity,
        editFlashSaleActivity,
        flashSaleActivityDetail,
        memberGradeList,
        getOrderCancelTime,
        getSekillSetting
    } from '@/api/api'

    export default {
        name: "model-add-or-edit",
        data() {
            return {
                //只能选择今天和今天之后的时间且不超过90天
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
                    }
                },
                breaddata: [],
                // 会员等级集合
                memberGradeList: [],
                valuetime:[],
                discount:[],
                deduction:[],
                cancelTime:0, //默认订单取消时间
                // 新增限时抢购表单
                dataForm: {
                   	activityEndDate: "",
                    activityName: "",//活动名称
                    activityStartDate: "",
                    activityState: "",//活动状态(默认:0未开始，1进行中，2已结束)
                    balanceFlag: 0,//余额是否可用(默认：0不可用，1可用)
                    couponsFlag: 0,//积分是否可用(默认：0不可用，1可用)
                    memberGradeId: '',
                    memberGradeIntegration: 0,
                    payValidTime: 0,//订单支付有效时间(单位：分钟)
                    pointFlag: 0, //优惠券是否可用 (默认：0不可用，1可用) 
                    reduceFlag: 0,//满减是否可用(默认：0不可用，1可用)
                    restrictionQuantity: 0,//活动限购数量
                },
                row: '',
                //radio--可用范围
                // 指定商品
                page: 1,
                limit: 10,
                total: 0,
                type: '',
                // 数据加载loadng
                flashSaleLoading: false,
                // 提交
                saveLoading: false,
                saveAddLoading:false,
                // 校验规则
                dataRule: {
                    activityName: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'}
                    ],
                    valuetime:[
                        {required: true,message:'必填项不能为空',trigger:'blur'}
                    ],
                },
            }
        },
        components: {
            Bread,
        },
        methods: {
            setDeduction(){
                this.dataForm.balanceFlag= 0
                this.dataForm.pointFlag= 0
                this.deduction.forEach(item => {
                    if(item == 1){
                        //设置余额
                        this.dataForm.balanceFlag= 1
                    }
                    if(item == 2){
                        //设置积分
                        this.dataForm.pointFlag= 1
                    }
                });
            },
            setDiscount(){
                this.dataForm.couponsFlag= 0
                this.dataForm.reduceFlag= 0
                this.discount.forEach(item => {
                    if(item == 1){
                    //设置余额
                        this.dataForm.couponsFlag= 1
                    }
                    if(item == 2){
                        this.dataForm.reduceFlag= 1
                    }
                });
            },
            
            acttime() {
                let d = new Date();
                let month = d.getMonth() > 8 ? d.getMonth() + 1 : '0' + (d.getMonth() + 1)
                let day = d.getDate()<10?'0'+d.getDate():d.getDate();
                let date = d.getFullYear() + '-' + month + '-' + day
                let hour = d.getHours() > 9 ? d.getHours() : '0' + d.getHours()
                let minute = d.getMinutes() > 9 ? d.getMinutes() : '0' + d.getMinutes()
                let time = date + ' ' + hour + ':' + minute + ':00'
                if (this.valuetime && this.valuetime.length != 0) {
                    if(this.valuetime[0]<time){
                        this.$message.error("起始时间不能小于当时时间")
                         this.valuetime=[]
                    }else{
                        this.dataForm.activityStartDate = this.valuetime[0];
                        this.dataForm.activityEndDate = this.valuetime[1];
                    }
                } else {
                    this.dataForm.activityStartDate = "";
                    this.dataForm.activityEndDate = ""
                }
            },
            init(type,row) {
                this.type = type;
                 if (this.type == 2) {
                        this.breaddata = ["营销系统", "限时抢购活动列表", "限时抢购活动编辑"];
                        this.row = row;
                        // 回显活动
                        this.backScan(this.row,this.type);
                    } else if (this.type == 1) {
                        this.breaddata = ["营销系统", "限时抢购活动列表", "限时抢购活动新增"];
                        this.orderCancelTime()
                    } else if (this.type == 6) {
                        this.breaddata = ["营销系统", "限时抢购活动列表", "限时抢购活动新增"];
                        this.row = row;
                        // 回显活动
                        this.backScan(this.row,this.type);
                    }
                // // 获取会员等级列表
                this.getMemberGradeList();
                // // 获取限时抢购设置

                // this.flashSaleLoading = false
                
            },
            orderCancelTime(){
                getOrderCancelTime().then((res)=>{
                    if(res.code==200){
                        this.dataForm.payValidTime = res.data;
                        this.cancelTime = res.data;
                    }
                })
            },
            // 编辑回显
            backScan(row,type) {
                var obj = {
                    id: row.id
                }
                flashSaleActivityDetail(obj).then((res => {
                           

                    if (res.code == 200) {
                        this.flashSaleLoading = false;
                        if (type == 6) {
                            this.dataForm = res.data;
                            this.row = '';
                            this.dataForm.activityId = '';
                        } else if (type == 2) {
                            this.dataForm = res.data;
                            // 数据回显格式转换一下
                            this.valuetime = [res.data.activityStartDate,res.data.activityEndDate]
                            this.dataForm.activityStartDate = this.valuetime[0];
                            this.dataForm.activityEndDate = this.valuetime[1];
                            
                            if(this.dataForm.balanceFlag==1){
                                this.deduction.push(1) 
                            }
                            if(this.dataForm.pointFlag==1){
                                this.deduction.push(2) 
                            }
                            if(this.dataForm.couponsFlag==1){
                                this.discount.push(1)
                            }
                            if(this.dataForm.reduceFlag==1){
                                this.discount.push(2)
                            }
                        }
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500
                        })
                        // 跳回上一页 刷新列表
                        this.$emit('changePage')
                    }
                }))
            },
            // 会员等级列表
            getMemberGradeList() {
                memberGradeList().then((res) => {
                    if (res.code == 200) {
                        this.memberGradeList = res.data;
                    }
                })
            },
            // 表单提交
            dataFormSubmit(dataForm) {
                this.setDeduction()
                this.setDiscount()
                console.log("asazxvvzvxsf",this.dataForm)
                this.$refs[dataForm].validate((valid) => {
                    if (valid) {
                        this.saveLoading = true;
                        var fn = this.type == 2 ? editFlashSaleActivity : saveFlashSaleActivity;
                        fn(this.dataForm).then((res => {
                            this.saveLoading = false;
                            let status = null;
                            if (res.code == "200") {
                                status = "success";
                                this.changePage()
                            } else {
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500
                            })
                        }))
                    } else {
                        return false;
                    }
                })
            },
            // 保存并添加商品
            savaAndSub(dataForm) {
                this.$refs[dataForm].validate((valid) => {
                    if (valid) {
                        this.setDeduction()
                        this.setDiscount()
                        this.saveAddLoading = true;
                        // console.log(this.type,67)
                        // console.log(this.dataForm,7777)
                        // var fn = this.type == 2 ? editFlashSaleActivity : saveFlashSaleActivity;
                        if(this.type!==2){
                        saveFlashSaleActivity(this.dataForm).then((res => {
                            this.saveAddLoading = false;
                            let status = null;
                            if (res.code == 200) {
                                status = "success";
                                console.log(res.data,77777788888)
                                this.$emit('changeCompent', 4, res.data)
                            } else {
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500
                            })
                        }))
                        }else{
                        editFlashSaleActivity(this.dataForm).then((res=>{
                            this.saveAddLoading = false;
                            let status = null;
                            if(res.code == 200){
                                status = "success";
                                this.$emit('changeCompent',4,this.dataForm)
                            }else{
                                status = "error";
                            }
                            this.$message({
                                message:res.msg,
                                type:status,
                                duration:1500
                            })
                        }))
                        }
                    } else {
                        return false;
                    }
                })
            },
            // 返回上一页
            changePage() {
                this.$emit('changePage')
            }
        }
    }
</script>

<style lang="scss" scoped>
    // 新增或编辑表单
    .addOrEdit {
        margin-left: 10%;
        .hrTips {
            color: #999999;
            margin-left: 150px;
            margin-bottom: 10px;
        }
        .fullTitle {
            font-weight: 700;
            font-size: 25px;
        }
        .title-1 {
            margin-left: -60px;
        }
        .title-2 {
            margin-left: 150px;
        }
        .title-3 {
            margin-left: 60px;
        }
        .el-form-item {
            display: block;
        }
        /deep/ .el-form-item__label {
            width: 150px !important;
            text-align: right;
        }
        /deep/ .el-form-item__error {
            position: relative !important;
            display: inline-block;
            margin-left: 10px;
        }
        /deep/ .el-input__icon {
            height: unset !important;
        }
        /deep/ .el-input-number {
            line-height: 34px !important;
            width: unset !important;
        }
        .el-input {
            width: 400px !important;
        }
        .specSize {
            .el-input {
                width: 200px !important;
            }
        }
        .fullTis {
            color: #999999;
            margin-left: 5px;
            margin-left: 170px
        }
        .addBtn {
            margin-top: 15px;
            margin-left: 170px;
        }
        .delBtn {
            margin-left: 20px;
            margin-top: 15px;
        }
    }
    /deep/ .el-form--inline .el-input .el-input__inner{
        height: 36px;
    }
</style>
