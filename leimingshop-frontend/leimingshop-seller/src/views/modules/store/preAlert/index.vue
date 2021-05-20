<template>
    <div>
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
                    1、库存预警设置开启后，当商户商家出售的商品单个规格库存小于该预警值时会站内信提示添加商品库存
                </div>
                <div class="iconSize">
                    2、如需短信提醒可开启短信提醒，短信提醒可能产生费用
                </div>
            </template>
        </el-alert>

        <div
            class="preAlert"
            v-loading="loading"
            style="width: 400px; margin: auto; margin-top: 40px"
        >
            <el-form
                label-width="100px"
                :rules="dataRule"
                ref="dataForm"
                :model="dataForm"
            >
                <el-form-item label="库存预警：" prop="phone">
                    <el-input-number
                        v-model="dataForm.storage"
                        controls-position="right"
                        :min="1"
                    ></el-input-number>
                </el-form-item>
                <div style="display: flex">
                    <el-form-item label="短信提醒：" prop="phone">
                        <!-- <el-radio-group v-model="dataForm.isSendSms">
                    <el-radio :label="0">开启</el-radio>
                    <el-radio :label="1">关闭</el-radio>
                </el-radio-group> -->

                        <el-switch
                            v-model="dataForm.isSendSms"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            :active-value="0"
                            :inactive-value="1"
                        >
                        </el-switch>
                    </el-form-item>

                    <el-form-item label="是否启用：" prop="phone">
                        <!-- <el-radio-group v-model="dataForm.isEnable">
                    <el-radio :label="0">启用</el-radio>
                    <el-radio :label="1">禁用</el-radio>
                </el-radio-group> -->

                        <el-switch
                            v-model="dataForm.isEnable"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            :active-value="0"
                            :inactive-value="1"
                        >
                        </el-switch>
                    </el-form-item>
                </div>
            </el-form>
            <div
                slot="footer"
                class="dialog-footer"
                style="width: 300px; text-align: center"
            >
                <el-button plain @click="cancle()">取 消</el-button>
                <el-button
                    type="primary"
                    @click="saveData()"
                    :loading="saveLoading"
                    :disabled="saveLoading"
                    v-if="$hasPermission('sys:storagewarning:setting')"
                    >{{ saveLoading ? "保存中..." : "保存" }}</el-button
                >
            </div>
        </div>
    </div>
</template>

<script>
    import {
        storagewarningInfo,
        storagewarningSave,
        messageInfoEdit,
    } from "@/api/api.js";
    import cloneDeep from "lodash/cloneDeep";
    export default {
        data() {
            return {
                loading: false,
                saveLoading: false,
                dataRule: {
                    storeName: [
                        {
                            required: true,
                            message: "商家名称不能为空！",
                            trigger: "blur",
                        },
                    ],
                },
                tempDataForm: {},
                dataForm: {
                    goodsId: "", // 商品id ,
                    isEnable: 1, //: 是否启用（0 启用，1禁用） ,
                    isSendSms: 1, //  是否发送短信（0 发送，1 不发送）
                    storage: "", //  库存预警值 ,
                    storeId: "", // 店铺Id
                },
            };
        },
        components: {},
        activated() {
            this.backScan();
        },
        created() {},
        methods: {
            //   回显数据
            backScan() {
                this.loading = true;
                storagewarningInfo().then((res) => {
                    this.loading = false;
                    if (res.code == 200) {
                        this.tempDataForm = cloneDeep(res.data);
                        Object.assign(this.dataForm, res.data);
                    }
                });
            },
            //   取消
            cancle() {
                let that = this;
                this.$confirm("是否确定取消?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        that.dataForm = cloneDeep(this.tempDataForm);
                    })
                    .catch(() => {});
            },
            //  保存
            saveData() {
                if (this.saveLoading) {
                    return;
                }
                var fn = this.dataForm.id ? messageInfoEdit : storagewarningSave;
                var obj = {};
                var obj = {
                    goodsId: this.dataForm.goodsId, // 商品id ,
                    isEnable: +this.dataForm.isEnable, //: 是否启用（0 启用，1禁用） ,
                    isSendSms: +this.dataForm.isSendSms, //  是否发送短信（0 发送，1 不发送）
                    storage: +this.dataForm.storage, //  库存预警值 ,
                    storeId: this.dataForm.storeId, // 店铺Id
                };
                this.dataForm.id ? (obj.id = this.dataForm.id) : "";
                this.saveLoading = true;
                fn(obj).then((res) => {
                    this.saveLoading = false;
                    if (res.code == 200) {
                        this.$message.success(res.msg);
                        this.backScan();
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .preAlert {
        .formMode {
            padding-top: 30px;
            border-bottom: 20px solid #ecedf1;
            .el-input {
                width: 250px !important;
            }
            .el-textarea__inner {
                width: 250px !important;
            }
            &:first-child {
                padding-top: 0;
            }
            &:last-child {
                border: none;
            }
            .itemMode {
                padding-left: 20px;
                display: flex;
                justify-content: space-between;
            }
        }

        /deep/ .el-form-item__content {
            display: block;
        }
    }
</style>
