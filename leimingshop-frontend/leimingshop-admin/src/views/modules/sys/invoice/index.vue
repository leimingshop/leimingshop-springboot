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
                    1、开具发票时间从订单完成后开始计算，超过开具发票时间不可开票
                </div>
                <div class="incoSize">
                    2、时间计算为自然日，开具发票时间限制为1-365天
                </div>
                <div class="incoSize">
                    3、换开发票时间从订单完成后开始计算，超过换开发票时间不可换开
                </div>
                <div class="incoSize">
                    4、时间计算为自然月，换开发票时间限制为1-24月
                </div>
            </template>
        </el-alert>
        <el-form
            :model="ruleForm"
            :rules="rules"
            ref="ruleForm"
            label-width="150px"
            class="demo-ruleForm artinputcontent"
        >
            <el-form-item label="开具发票时间：" prop="openInvoice">
                <div>
                    <el-input
                        placeholder="请输入天数"
                        :min="0"
                        type="number"
                        :max="1440"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.openInvoice"
                    >
                        <template slot="append">天</template>
                    </el-input>
                </div>
            </el-form-item>
            <el-form-item label="换开发票时间：" prop="exchangeInvoice">
                <div>
                    <el-input
                        placeholder="请输入月数"
                        type="number"
                        min="0"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.exchangeInvoice"
                    >
                        <template slot="append">月</template> </el-input
                    >建议不超过12个月
                </div>
            </el-form-item>

            <el-form-item>
                <el-button
                    type="primary"
                    style="margin-top：50px"
                    @click="submitForm()"
                    v-if="$hasPermission('sys:order:setting')"
                    >保存</el-button
                >
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { getInvoice, UpdateInvoice } from "@/api/api";
    export default {
        data() {
            return {
                breaddata: ["网站管理", "高级设置", "发票设置"],
                ruleForm: {
                    openInvoice: 35,
                    exchangeInvoice: 12,
                },
                rules: {
                    openInvoice: [
                        { required: true, message: "请输入天数", trigger: "blur" },
                    ],
                    exchangeInvoice: [
                        { required: true, message: "请输入月数", trigger: "blur" },
                    ],
                },
            };
        },
        methods: {
            //获取发票设置数据
            getOrderCon() {
                getInvoice().then((res) => {
                    if (res.code == 200) {
                        this.ruleForm = res.data;
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                        });
                    }
                });
            },
            //保存修改
            submitForm() {
                this.$refs["ruleForm"].validate((valid) => {
                    if (valid) {
                        if (
                            this.ruleForm.openInvoice > 365 ||
                            this.ruleForm.openInvoice == 0
                        ) {
                            ////开具发票时间限制为1-365天
                            this.$message({
                                message: "开具发票时间限制为1-365天",
                                type: "error",
                                duration: 1500,
                            });
                            return;
                        }

                        if (
                            this.ruleForm.exchangeInvoice > 24 ||
                            this.ruleForm.exchangeInvoice == 0
                        ) {
                            ////开具发票时间限制为1-365天
                            this.$message({
                                message: "换开发票时间限制为1-24月",
                                type: "error",
                                duration: 1500,
                            });
                            return;
                        }

                        UpdateInvoice(this.ruleForm).then((res) => {
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
        },
        components: { Bread },
        created() {
            this.getOrderCon();
        },
    };
</script>
<style scoped>
    .el-form-item_label {
        margin-right: 30px;
    }
    .el-input-group {
        width: 280px;
        margin-right: 16px;
    }
</style>
