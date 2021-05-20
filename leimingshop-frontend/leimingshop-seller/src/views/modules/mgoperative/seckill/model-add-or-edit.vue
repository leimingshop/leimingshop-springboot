<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataForm" class="addOrEdit" :rules="dataRule" ref="dataForm"
                 v-loading="seckillLoading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="秒杀活动名称：" prop="activityName">
                <el-input v-model="dataForm.activityName" placeholder="请输入活动名称" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <div class="hrTips">必填项，可输入20个汉字</div>
            <el-form-item label="秒杀时间：" prop="date">
                <el-select
                        v-model="dataForm.date"
                           placeholder="请选择活动日期"
                           @change="changeSelect"
                >
                    <el-option
                            v-for="item in sessionDate"
                            :key="item.name"
                            :label="item.name"
                            :value="item.value">
                    </el-option>
                </el-select>--
                <el-select
                        v-model="dataForm.sessionId"
                        placeholder="请选择秒杀时间"
                        @change="changeSelect2"
                >
                    <el-option
                            v-for="item in sessionTime"
                            :key="item.id"
                            :label="item.value"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <div class="hrTips">活动到期后将自动失效，失效后不可延长</div>
            <el-form-item label="秒杀限购数量：" prop="restrictionQuantity" style="height: 42px">
                <el-input-number v-model="dataForm.restrictionQuantity" placeholder="请输入秒杀商品限购数量" :min="0"
                                 :max="99999"></el-input-number>
            </el-form-item>
            <div class="hrTips">秒杀活动价限购数量为该秒杀活动下的商品每个用户可以秒杀价购物的商品数量，输入0或不填为不限购</div>
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
            <div class="hrTips">非必填项，默认选择为无等级限制，可选择新用户或其他等级，选择限制条件后只有满足该会员等级条件用户可参加该秒杀活动</div>

            <div class="fullTitle title-1">
                优惠规则
            </div>
            <el-form-item label="秒杀活动有效期：" prop="activityEffectiveTime">
                <el-input-number v-model="dataForm.activityEffectiveTime" :min="1" :max="48"></el-input-number>&nbsp;小时
            </el-form-item>
            <div class="hrTips">秒杀活动有效期时间范围为1-48小时</div>

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
        saveSeckillActivity,
        editSeckillActivity,
        seckillActivityDetail,
        seckillSessionList,
        memberGradeList,
        getSekillSetting
    } from '@/api/api'

    export default {
        name: "model-add-or-edit",
        data() {
            return {
                breaddata: [],
                sessionDate: [],//秒杀日期
                sessionTime: [],//秒杀时间
                // 会员等级集合
                memberGradeList: [],
                // 新增秒杀表单
                dataForm: {
                    date:'',
                    activityName: '',   //秒杀活动名称
                    sessionId: '',  //秒杀场次id
                    restrictionQuantity: '',    //限购数量
                    memberGradeId: '',  //会员等级限制
                    activityEffectiveTime: '',  //秒杀活动有效期
                },
                row: '',
                //radio--可用范围
                // 指定商品
                page: 1,
                limit: 10,
                total: 0,
                type: '',
                // 数据加载loadng
                seckillLoading: false,
                // 提交
                saveLoading: false,
                saveAddLoading:false,
                // 校验规则
                dataRule: {
                    activityName: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'}
                    ],
                    date:[
                        {required: true,message:'必填项不能为空',trigger:'blur'}
                    ],
                    sessionId: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'}
                    ],

                },
            }
        },
        components: {
            Bread,
        },
        methods: {
            init(row, type) {
                this.type = type;
                this.seckillLoading = true;
                // 获取场次列表 先求出时间 否则回显的时候会出错 识别不到sessionDate和sessionTime
                this.getSeckillSessionList().then(()=>{
                    if (this.type == 2) {
                        this.breaddata = ["营销系统", "秒杀活动列表", "秒杀活动编辑"];
                        this.row = row;
                        // 回显活动
                        this.backScan(row);
                    } else if (this.type == 1) {
                        this.breaddata = ["营销系统", "秒杀活动列表", "秒杀活动新增"];
                    } else if (this.type == 6) {
                        this.breaddata = ["营销系统", "秒杀活动列表", "秒杀活动新增"];
                        this.row = row;
                        // 回显活动
                        this.backScan(row);
                    }
                })
                // 获取会员等级列表
                this.getMemberGradeList();
                // 获取秒杀设置
                this.getSekillSetting();

                this.seckillLoading = false
            },
            // 编辑回显
            backScan(row) {
                var obj = {
                    id: row.id
                }
                seckillActivityDetail(obj).then((res => {
                    if (res.code == 200) {
                        this.seckillLoading = false;
                        if (this.type == 6) {
                            this.dataForm = res.data;
                            this.row = '';
                            this.dataForm.activityId = '';
                            this.dataForm.sessionId = '';
                        } else if (this.type == 2) {

                            console.log(111)
                            this.dataForm = res.data;
                            let dataAndTime = res.data.activityStartDate
                                let backDate = dataAndTime.split(" ")[0] // 日期
                                let backTime = dataAndTime.split(" ")[1] // 时间
                            this.sessionDate.forEach((item, index) => {
                                if(backDate==item.name){
                                    this.sessionTime = item.children
                                    this.dataForm.date = item.name
                                }
                            });
                            this.sessionTime.forEach((item, index) => {
                                if(backTime==item.value){
                                    this.dataForm.sessionId = item.id
                                }
                            });
                            // this.dataForm.sessionId = res.data.sessionId
                            // this.dataForm.date = dataAndTime.split(" ")[0]

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

            getSekillSetting() {
                getSekillSetting().then((res) => {
                    if (res.code == 200) {
                        this.dataForm.activityEffectiveTime = res.data.activityEffectiveTime;
                    }
                })
            },

            // 场次列表
            getSeckillSessionList() {
                var that =this
               return seckillSessionList().then((res) => {
                    if (res.code == 200) {
                        let sessionDate = [];
                        // 过滤重复数据
                        res.data.forEach((item,index)=>{
                            var _index =sessionDate.findIndex((item2,index2)=>{
                                   return  item2.value == item.activityStartDateStr.split(" ")[0];
                            })
                            let obj1 = {
                                value:item.activityStartDateStr.split(" ")[0],
                                name:item.activityStartDateStr.split(" ")[0],
                            }
                            let obj2 = {
                                value:item.activityStartDateStr.split(" ")[1],
                                id:item.id,
                                activityEffectiveTime:item.activityEffectiveTime
                            }
                            if(_index==-1){
                                sessionDate.push({
                                       ...obj1,
                                        children:[obj2]
                                });
                            }else{
                                sessionDate[_index].children.push(obj2)
                            }
                        })

                        this.sessionDate = sessionDate;

                    }
                })
            },
            changeSelect2(val){
                // this.dataForm.activityEffectiveTime = 1 // 默认最小值为1
                this.sessionTime.forEach((item, index) => {
                    if(item.id == val){
                        this.dataForm.activityEffectiveTime = item.activityEffectiveTime
                    }
                });
            },
            // 切换场次 清空之前的状态
            changeSelect(val) {
                // this.dataForm.activityEffectiveTime = 1 // 默认最小值为1
                this.sessionTime =[]
                this.dataForm.sessionId =''
                this.sessionDate.forEach((item, index) => {
                    if(val==item.name){
                        this.sessionTime = item.children
                    }
                });
            },
            // 表单提交
            dataFormSubmit(dataForm) {
                this.$refs[dataForm].validate((valid) => {
                    if (valid) {
                        this.saveLoading = true;
                        var fn = this.type == 2 ? editSeckillActivity : saveSeckillActivity;
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
                        this.saveAddLoading = true;
                        var fn = this.type == 2 ? editSeckillActivity : saveSeckillActivity;
                        if(this.type !== 2){
                            saveSeckillActivity(this.dataForm).then((res => {
                            this.saveAddLoading = false;
                            let status = null;
                            if (res.code == "200") {
                                status = "success";
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
                          editSeckillActivity(this.dataForm).then((res => {
                            this.saveAddLoading = false;
                            let status = null;
                            if (res.code == "200") {
                                status = "success";
                                this.$emit('changeCompent', 4, this.row)
                            } else {
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500
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
