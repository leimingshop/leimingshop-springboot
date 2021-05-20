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
                    1、商户入驻之后，可以进行店铺信息的修改，修改完后需要运营端审核，审核通过才能生效
                </div>
            </template>
        </el-alert>
        <el-form
            :model="ruleForm"
            :rules="rules"
            ref="ruleForm"
            label-width="140px"
            class="demo-ruleForm artinputcontent"
        >
            <el-radio-group v-model="ruleForm.type" @change="change()">
                <el-radio label="3">按月对账： </el-radio>
                <el-form-item label="每：" prop="month" class="inlineShow">
                    <el-input
                        style="width: 160px"
                        placeholder=""
                        :min="0"
                        type="number"
                        :max="1440"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.month"
                    >
                    </el-input
                    >&#12288;月对账一次&#12288;首次对账开始时间：

                    <el-date-picker
                        v-model="valueTime1"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间"
                    >
                    </el-date-picker>
                </el-form-item>

                <br />
                <el-radio label="2">按周对账： </el-radio>
                <el-form-item label="每：" prop="week" class="inlineShow">
                    <el-input
                        style="width: 160px"
                        placeholder=""
                        :min="0"
                        type="number"
                        :max="1440"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.week"
                    >
                    </el-input
                    >&#12288;周对账一次&#12288;首次对账开始时间：
                    <el-date-picker
                        v-model="valueTime2"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间"
                    >
                    </el-date-picker>
                </el-form-item>

                <br />
                <el-radio label="1">按天对账： </el-radio>
                <el-form-item label="" prop="day" class="inlineShow">
                    首次对账开始时间：
                    <el-date-picker
                        popper-class="timeClass"
                        type="dates"
                        v-model="valueTime3"
                        value-format="d"
                        format="dd日"
                        :default-value="newTime"
                        placeholder="选择一个或多个日期"
                        @blur="acttime"
                    >
                    </el-date-picker>
                </el-form-item>
            </el-radio-group>

            <el-form-item>
                <!-- <el-button @click="resetForm('ruleForm')" style="margin-top: 20px">取消</el-button> -->
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                    v-if="$hasPermission('sys:bill:setting')"
                    >保存</el-button
                >
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { getBilSetting, saveBillSetting } from "@/api/api";
    export default {
        data() {
            return {
                newTime: new Date().getFullYear() + " 01-01",
                valueTime1: "",
                valueTime2: "",
                valueTime3: "",
                breaddata: ["网站管理", "系统设置", "对账设置"],
                ruleForm: {
                    statisticalCycle: "",
                    type: "",
                    month: "",
                    week: "",
                    day: "",
                },
                rules: {
                    statisticalCycle: [
                        {
                            required: true,
                            message: "请选择结算周期",
                            trigger: "blur",
                        },
                    ],
                    type: [
                        { required: true, message: "请选择状态", trigger: "blur" },
                    ],
                },
            };
        },
        components: { Bread },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var params = {};
                        if (this.ruleForm.type == 3) {
                            params.days = this.ruleForm.month;
                            params.startTime = this.valueTime1;
                        } else if (this.ruleForm.type == 2) {
                            params.days = this.ruleForm.week;
                            params.startTime = this.valueTime2;
                        } else if (this.ruleForm.type == 1) {
                            params.days = this.ruleForm.day;
                        }
                        params.type = this.ruleForm.type;
                        console.log(params);

                        saveBillSetting(params).then((res) => {
                            if (res.code == 200 && res) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1500,
                                });
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "error",
                                    duration: 1500,
                                });
                            }
                        });
                    } else {
                        console.log("error submit!!");
                        return false;
                    }
                });
            },
            resetForm() {
                this.getSettlData();
            },
            //开始结束时间
            acttime() {
                if (this.ruleForm.type == 3) {
                    this.ruleForm.startTime = this.valueTime1[0];
                    this.ruleForm.endTime = this.valueTime1[1];
                } else if (this.ruleForm.type == 2) {
                    this.ruleForm.startTime = this.valueTime2[0];
                    this.ruleForm.endTime = this.valueTime2[1];
                } else if (this.ruleForm.type == 1) {
                    this.ruleForm.startTime = "";
                    this.ruleForm.endTime = "";
                    console.log(this.valueTime3);
                    var b = this.valueTime3.join(",");
                    this.ruleForm.day = b;
                }
            },
            //改变选项，变空值
            change() {
                this.valueTime1 = "";
                this.valueTime2 = "";
                this.valueTime3 = "";
                this.ruleForm.month = "";
                this.ruleForm.week = "";
                this.ruleForm.day = "";
            },
            //获取结算设置数据
            getSettlData() {
                getBilSetting().then((res) => {
                    if (res.code == 200 && res) {
                        if (res.data.type == 3) {
                            this.ruleForm.type = "3";
                            this.ruleForm.month = res.data.days;
                            this.valueTime1 = res.data.startTime;
                        } else if (res.data.type == 2) {
                            this.ruleForm.type = "2";
                            this.ruleForm.week = res.data.days;

                            this.valueTime2 = res.data.startTime;
                        } else if (res.data.type == 1) {
                            this.ruleForm.type = "1";
                            var str = res.data.days;
                            var ss = str.split(",");
                            this.valueTime3 = ss;
                        }
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "warning",
                            duration: 1500,
                        });
                    }
                });
            },
        },
        created() {
            this.getSettlData();
        },
    };
</script>
<style lang="scss" scoped>
    .artinputcontent {
        .inlineShow {
            display: inline-block;
        }
        /deep/ .el-form-item__label {
            width: 40px !important;
        }
        /deep/ .el-form-item__content {
            margin-left: 40px !important;
        }
    }
    .timeClass {
        /deep/ .el-date-picker__header {
            display: none;
        }
    }
</style>
