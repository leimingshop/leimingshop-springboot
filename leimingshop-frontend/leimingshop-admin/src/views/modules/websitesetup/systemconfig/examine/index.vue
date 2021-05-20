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
                    1、商品审核开启后商品添加的商品需要平台进行审核，反之则不需要平台审核
                </div>
                <div class="iconSize">
                    2、退货审核开启后商家订单售后退货流程需要平台进行审核，反之则不需要平台审核
                </div>
                <div class="iconSize">
                    3、换货审核开启后商家订单售后换货流程需要平台进行审核，反之则不需要平台审核
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
            <el-form-item label="商品审核设置：" prop="goodsAuditOpen">
                <el-switch
                    style="display: block; margin-top: 10px"
                    v-model="ruleForm.goodsAuditOpen"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    inactive-value="0"
                    @change="submitForm()"
                    v-if="$hasPermission('sys:audit:setting')"
                >
                </el-switch>
            </el-form-item>
            <el-form-item label="退货审核设置：" prop="goodsReturnOpen">
                <el-switch
                    style="display: block; margin-top: 10px"
                    v-model="ruleForm.goodsReturnOpen"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    inactive-value="0"
                    @change="submitForm"
                    v-if="$hasPermission('sys:audit:setting')"
                >
                </el-switch>
            </el-form-item>
            <el-form-item label="换货审核设置：" prop="goodsExchangeOpen">
                <el-switch
                    style="display: block; margin-top: 10px"
                    v-model="ruleForm.goodsExchangeOpen"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    inactive-value="0"
                    @change="submitForm()"
                    v-if="$hasPermission('sys:audit:setting')"
                >
                </el-switch>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { sysWebExamineCon, sysWebExamineSave } from "@/api/api";
    export default {
        data() {
            return {
                breaddata: ["网站管理", "系统设置", "审核设置"],
                ruleForm: {
                    goodsExchangeOpen: 1,
                    goodsReturnOpen: 1,
                    goodsAuditOpen: 0,
                },
                //   goodsReturnOpen: "1",
                //   goodsExchangeOpen: "0",//
                //   goodsAuditOpen: "1",
                rules: {},
            };
        },
        components: { Bread },
        methods: {
            getExtaStatus() {
                //获取审核状态
                sysWebExamineCon().then((res) => {
                    if (res.code == 200) {
                        console.log(res);
                        this.ruleForm = res.data;
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "warning",
                            duration: 1500,
                        });
                    }
                });
            },
            //保存提交
            submitForm() {
                const obj = {
                    // goodsAuditOpen: {
                    goodsExchangeOpen: this.ruleForm.goodsExchangeOpen,
                    goodsReturnOpen: this.ruleForm.goodsReturnOpen,
                    goodsAuditOpen: this.ruleForm.goodsAuditOpen,
                    // }
                };
                sysWebExamineSave(obj).then((res) => {
                    if (res.code == 200) {
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 2000,
                        });
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "warning",
                            duration: 2000,
                        });
                    }
                });
            },
        },
        created() {
            this.getExtaStatus();
        },
    };
</script>
<style scoped>
</style>
