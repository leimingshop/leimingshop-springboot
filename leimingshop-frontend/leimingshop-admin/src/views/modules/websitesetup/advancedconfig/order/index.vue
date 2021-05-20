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
                    1、正常订单超过x分未付款，订单自动取消——默认为30分钟，时间单位为分钟，输入范围为1-1440分钟
                </div>
                <div class="iconSize">
                    2、发货超过X天未收货，订单自动完成——默认为7天，时间单位为天，输入范围为1-60天
                </div>
                <div class="iconSize">
                    3、订单完成超过X天自动结束交易，不能申请退货售后——默认为14天，时间单位为天，输入范围为1-60天
                </div>
                <div class="iconSize">
                    4、订单完成超过X天自动结束交易，不能申请换货售后——默认为14天，时间单位为天，输入范围为1-60天
                </div>
                <div class="iconSize">
                    5、订单完成超过X天自动结好评——默认为7天，时间单位为天，输入范围为1-60天
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
            <el-form-item label="正常订单超过：" prop="cancelOrder">
                <div>
                    <el-input
                        placeholder="请输入分钟"
                        :min="0"
                        type="number"
                        :max="1440"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.cancelOrder"
                    >
                        <template slot="append">分</template> </el-input
                    >未付款，订单自动关闭
                </div>
            </el-form-item>
            <el-form-item label="发货超过：" prop="autoCompleteOrder">
                <div>
                    <el-input
                        placeholder="请输入天数"
                        type="number"
                        min="0"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.autoCompleteOrder"
                    >
                        <template slot="append">天</template> </el-input
                    >未收货，订单自动完成
                </div>
            </el-form-item>
            <el-form-item label="订单完成超过：" prop="cannotReturn">
                <div>
                    <el-input
                        placeholder="请输入天数"
                        type="number"
                        min="0"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.cannotReturn"
                    >
                        <template slot="append">天</template> </el-input
                    >自动结束交易,不能申请退货售后
                </div>
            </el-form-item>
            <el-form-item label="订单完成超过：" prop="cannotBarter">
                <div>
                    <el-input
                        placeholder="请输入天数"
                        type="number"
                        min="0"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.cannotBarter"
                    >
                        <template slot="append">天</template> </el-input
                    >自动结束交易,不能申请换货售后
                </div>
            </el-form-item>
            <el-form-item label="订单完成超过：" prop="autoComment">
                <div>
                    <el-input
                        placeholder="请输入天数"
                        type="number"
                        min="0"
                        onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                        autocomplete="off"
                        v-model="ruleForm.autoComment"
                    >
                        <template slot="append">天</template> </el-input
                    >自动好评
                </div>
            </el-form-item>
            <el-form-item>
                <!-- <el-button @click="ret()" style="margin-top: 20px">取消</el-button> -->
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
    import { orderAdvCon, orderAdvSave } from "@/api/api";
    export default {
        data() {
            var checkOrder = (rule, value, callback) => {
                if (value > 1440 && value != "") {
                    return callback(new Error("输入项不得超过1440"));
                } else {
                    callback();
                }
            };
            return {
                breaddata: ["网站管理", "高级设置", "订单设置"],
                ruleForm: {
                    autoComment: "", //好评
                    cannotBarter: "", //换货
                    cannotReturn: "", //退货
                    autoCompleteOrder: "", //未收货，自动完成
                    cancelOrder: "", //关闭
                },
                rules: {
                    autoComment: [
                        { required: true, message: "请输入天数", trigger: "blur" },
                    ],
                    cannotBarter: [
                        { required: true, message: "请输入天数", trigger: "blur" },
                    ],
                    cannotReturn: [
                        { required: true, message: "请输入天数", trigger: "blur" },
                    ],
                    autoCompleteOrder: [
                        { required: true, message: "请输入天数", trigger: "blur" },
                    ],
                    cancelOrder: [
                        {
                            required: true,
                            message: "请输入分钟数",
                            trigger: "blur",
                        },
                        { validator: checkOrder, trigger: "blur" },
                    ],
                },
            };
        },
        methods: {
            //获取订单设置数据
            getOrderCon() {
                orderAdvCon().then((res) => {
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
                        orderAdvSave(this.ruleForm).then((res) => {
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
            //取消修改
            ret() {
                this.getOrderCon();
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
