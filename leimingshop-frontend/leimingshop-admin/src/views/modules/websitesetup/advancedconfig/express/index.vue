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
                    1、此设置由平台技术人员维护，请谨慎操作，如操作不当会影响商城正常使用
                </div>
            </template>
        </el-alert>
        <el-form
            :model="numberValidateForm"
            :rules="rules"
            ref="numberValidateForm"
            label-width="150px"
            class="demo-ruleForm artinputcontent"
        >
            <el-form-item label="快递100 Key：" prop="expressKey" class="input">
                <el-col :span="24">
                    <el-input
                        style="width: 300px"
                        type="text"
                        v-model="numberValidateForm.expressKey"
                        autocomplete="off"
                    ></el-input>
                </el-col>
            </el-form-item>
            <el-form-item
                label="快递100 Customer："
                prop="expressCustomer"
                class="input"
            >
                <el-input
                    type="text"
                    style="width: 300px"
                    v-model="numberValidateForm.expressCustomer"
                    autocomplete="off"
                ></el-input>
            </el-form-item>
            <el-form-item>
                <!-- <el-button @click="resetForm()" style="margin-top: 20px">取消</el-button> -->
                <el-button
                    type="primary"
                    @click="submitForm('numberValidateForm')"
                    v-if="$hasPermission('sys:express:setting')"
                    >保存</el-button
                >
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { expressAdvCon, expressAdvSave } from "@/api/api";
    export default {
        data() {
            return {
                breaddata: ["网站管理", "系统设置", "快递设置"],
                numberValidateForm: { expressKey: "", expressCustomer: "" },
                rules: {
                    expressKey: [
                        {
                            required: true,
                            message: "请输入快递100 Key",
                            trigger: "blur",
                        },
                    ],
                    expressCustomer: [
                        {
                            required: true,
                            message: "请输入快递100 Customer",
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        expressAdvSave(this.numberValidateForm).then((res) => {
                            if (res.code == 200 && res) {
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
                        return false;
                    }
                });
            },
            //取消修改
            resetForm() {
                this.getExpressData();
            },
            //获取快递设置数据
            getExpressData() {
                expressAdvCon().then((res) => {
                    if (res.code == 200 && res) {
                        this.numberValidateForm = res.data;
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
        components: { Bread },
        created() {
            this.getExpressData();
        },
    };
</script>
<style scoped>
    .el-input {
        width: 20%;
    }
</style>
