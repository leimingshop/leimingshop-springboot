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
                    1、结算周期分为日结周结跟月结，修改统计周期后，新的统计周期在本周期统计完成后生效
                </div>
                <div class="iconSize">
                    2、自动打款设置默认为开启状态，开启后到达结算日期会自动打款
                </div>
                <div class="iconSize">
                    3、设置完成修改后点击保存按钮生效，提示修改成功，修改后的设置仅对之后的数据产生影响，之前的数据不会产生影响
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
            <el-form-item label="结算周期：" prop="statisticalCycle">
                <el-select
                    v-model="ruleForm.statisticalCycle"
                    placeholder="请选择结算周期"
                >
                    <el-option label="日结" value="0"></el-option>
                    <el-option label="周结" value="1"></el-option>
                    <el-option label="月结" value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="自动打款设置：" prop="automaticMoney">
                <el-switch
                    style="display: block; margin-top: 10px"
                    v-model="ruleForm.automaticMoney"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                >
                </el-switch>
            </el-form-item>
            <el-form-item>
                <!-- <el-button @click="resetForm('ruleForm')" style="margin-top: 20px">取消</el-button> -->
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                    v-if="$hasPermission('sys:settlement:setting')"
                    >保存</el-button
                >
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { settlAdvCon, settlAdvSave } from "@/api/api";
    export default {
        data() {
            return {
                breaddata: ["网站管理", "系统设置", "结算设置"],
                ruleForm: { statisticalCycle: "", automaticMoney: "" },
                rules: {
                    statisticalCycle: [
                        {
                            required: true,
                            message: "请选择结算周期",
                            trigger: "blur",
                        },
                    ],
                    automaticMoney: [
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
                        settlAdvSave(this.ruleForm).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1500,
                                });
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "warning",
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
            //获取结算设置数据
            getSettlData() {
                settlAdvCon().then((res) => {
                    if (res.code == 200 && res) {
                        this.ruleForm = res.data;
                    } else {
                        this.$message({
                            message: res.msg + "哈哈",
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
<style scoped>
</style>
