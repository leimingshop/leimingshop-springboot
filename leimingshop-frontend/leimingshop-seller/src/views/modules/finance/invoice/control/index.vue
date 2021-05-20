<template>
    <div>
        <Bread :breaddata="breaddata" @changePage="changePage"></Bread>
        <div class="formControlArea">
            <div>
                <div class="top-title">开票控制</div>
            </div>
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
                    1.发票管理的开关，是给店铺所有商品设置发票默认值。不影响已在架商品
                </div>
                <div class="iconSize">
                    2、对于新发布商品，商品添加页面默认值与当前功能保持一致
                </div>
                <div class="iconSize">
                    3、当前功能默认值优先级低于商品添加页面。
                </div>
            </template>
        </el-alert>
        <el-form
            :inline="true"
            :model="dataFormTemp"
            class="addOrEdit"
            :rules="dataRule"
            ref="dataFormTemp"
            v-loading="couponLoading"
        >
            <div class="fullTitle title-1">基本信息</div>
            <el-form-item label="是否开具发票：" prop="invoiceFlag">
                <el-radio-group
                    v-model="dataFormTemp.invoiceFlag"
                    @change="updateInvoice(dataFormTemp.invoiceFlag)"
                >
                    <el-radio label="1">是</el-radio>
                    <el-radio label="0">否</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item
                label="发票类型选择："
                :prop="dataFormTemp.invoiceFlag ? 'invoiceType' : ''"
            >
                <el-checkbox-group
                    v-model="dataFormTemp.invoiceType"
                    @change="updateContent(dataFormTemp.invoiceType)"
                >
                    <!--          <el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>-->
                    <el-checkbox label="1" :disabled="isDisabled"
                        >电子发票</el-checkbox
                    >
                    <el-checkbox label="2" :disabled="isDisabled"
                        >纸质普票</el-checkbox
                    >
                    <el-checkbox label="3" :disabled="isDisabled"
                        >增值税专项发票</el-checkbox
                    >
                </el-checkbox-group>
            </el-form-item>
            <div class="hrTips">控制用户购买商品时，可以选择的发票种类。</div>

            <el-form-item
                label="发票内容："
                :prop="dataFormTemp.invoiceFlag ? 'invoiceContent' : ''"
            >
                <el-checkbox-group v-model="dataFormTemp.invoiceContent">
                    <el-checkbox
                        label="1"
                        :disabled="isDisabled || contentDisabled"
                        >商品明细</el-checkbox
                    >
                    <el-checkbox
                        label="2"
                        :disabled="isDisabled || contentDisabled"
                        >商品类别</el-checkbox
                    >
                </el-checkbox-group>
            </el-form-item>
            <div class="hrTips">控制用户购买商品时，可以选择的发票内容。</div>

            <div class="fullTitle title-1">邮寄信息管理</div>
            <el-form-item label="发票收件人：" prop="addressee">
                <el-input
                    maxlength="20"
                    v-model="dataFormTemp.addressee"
                ></el-input>
            </el-form-item>
            <div class="hrTips">
                用户申请发票换开时，原纸质发票寄回时的收件人
            </div>

            <el-form-item label="收件人电话：" prop="addresseePhone">
                <el-input
                    maxlength="20"
                    v-model="dataFormTemp.addresseePhone"
                ></el-input>
            </el-form-item>
            <div class="hrTips">用户申请发票换开时，原纸质发票收件人电话</div>
            <el-form-item
                label="邮寄地址："
                clearable
                class="specSize"
                prop="provinceId"
            >
                <div>
                    <div style="margin-bottom: 10px">
                        <el-cascader
                            clearable=""
                            v-model="addressIds"
                            :options="address"
                            :props="props"
                            @change="finishCange"
                        ></el-cascader>
                    </div>
                    <el-input
                        maxlength="50"
                        type="textarea"
                        :rows="2"
                        placeholder="请输入内容"
                        prop="detailedAddress"
                        v-model="dataFormTemp.detailedAddress"
                    >
                    </el-input>
                </div>
            </el-form-item>
            <div class="hrTips">用户申请发票换开时，纸质发票寄回的地址</div>
            <el-form-item>
                <el-button
                    class="btn-item"
                    type="primary"
                    @click="dataFormSubmit('dataFormTemp')"
                    :loading="saveLoading"
                >
                    {{ saveLoading ? "提交中..." : "确认" }}
                </el-button>
                <!--        <el-button class="btn-item" type="primary" plain @click="getDetail()" plain>取消</el-button>-->
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import { getStoreinvoiceDetail, savaAndEditInvoice } from "@/api/api";
    import addressJson from "@/assets/addr2";

    export default {
        name: "model-add-or-edit",
        data() {
            return {
                data: {},
                address: addressJson.data,
                props: {
                    value: "i",
                    label: "n",
                    children: "c",
                },
                isDisabled: false,
                contentDisabled: false,
                addressIds: [],
                breaddata: ["财务管理", "开票控制"],
                //回显数据的时候loading
                couponLoading: true,
                // 发票设置信息
                dataFormTemp: {
                    invoiceFlag: "0",
                    invoiceType: [],
                    invoiceContent: [],
                    addressee: "",
                    addresseePhone: "",
                    provinceId: "",
                    cityId: "",
                    districtId: "",
                    streetId: "",
                    mailingAddress: "",
                    detailedAddress: "",
                },
                row: "",
                // 提交
                saveLoading: false,
                // 校验规则
            };
        },
        computed: {
            dataRule() {
                return {
                    invoiceFlag: [
                        {
                            required: true,
                            message: "必填项不能为空",
                            trigger: "blur",
                        },
                    ],
                    // invoiceType: [
                    // 	{required: true, message: '必填项不能为空',  trigger: 'blur'},
                    // ],
                    // invoiceContent: [
                    // 	{required: true, message: '必填项不能为空', trigger: 'blur'},
                    // ],
                    addressee: [
                        {
                            required: true,
                            message: "必填项不能为空",
                            trigger: "blur",
                        },
                    ],
                    addresseePhone: [
                        {
                            required: true,
                            message: "必填项不能为空",
                            trigger: "blur",
                        },
                    ],
                    provinceId: [
                        {
                            required: true,
                            message: "必填项不能为空",
                            trigger: "blur",
                        },
                        // {validator: validTimechange, trigger: 'blur'},
                    ],
                    detailedAddress: [
                        {
                            required: true,
                            message: "必填项不能为空",
                            trigger: "blur",
                        },
                    ],
                };
            },
        },
        components: {
            Bread,
        },
        watch: {
            // 收票人
            "dataFormTemp.addressee": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    // 删除输入的汉字
                    if (!/^[\u4E00-\u9FA5a-zA-Z0-9]*$/.test(newV[i])) {
                        this.dataFormTemp.addressee = newV.replace(newV[i], "");
                    }
                }
            },
            //电话号
            "dataFormTemp.addresseePhone": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    // 删除输入的汉字
                    if (!/^[a-zA-Z0-9]*$/.test(newV[i])) {
                        this.dataFormTemp.addresseePhone = newV.replace(
                            newV[i],
                            ""
                        );
                    }
                }
            },
            //详细地址
            "dataFormTemp.detailedAddress": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    // 删除输入的汉字
                    if (!/^[\u4E00-\u9FA5a-zA-Z0-9]*$/.test(newV[i])) {
                        this.dataFormTemp.detailedAddress = newV.replace(
                            newV[i],
                            ""
                        );
                    }
                }
            },
        },
        created() {
            this.getDetail();
        },
        methods: {
            init(row) {
                // 商品列表数据
                this.couponLoading = false;
            },
            addressfun(e) {
                let arr = "";
                e.forEach((v, i) => {
                    if ((v.i = e[i])) {
                        arr = arr + v.n;
                    }
                });
            },
            finishCange(e) {
                let mailingAddress = "";
                var addressfun = (arr, all) => {
                    arr.forEach((v, i) => {
                        var fun = all.forEach((vv, ii) => {
                            if (v == vv.i) {
                                if (mailingAddress == "") {
                                    mailingAddress = vv.n;
                                } else {
                                    mailingAddress = mailingAddress + " " + vv.n;
                                }
                                if (arr.length > 1) {
                                    addressfun(arr.slice(1), vv.c);
                                }
                            }
                        });
                    });
                };
                addressfun(e, this.address);
                this.dataFormTemp.mailingAddress = mailingAddress;
                let len = this.addressIds.length;
                if (len == 4) {
                    this.dataFormTemp.provinceId = this.addressIds[0];
                    this.dataFormTemp.cityId = this.addressIds[1];
                    this.dataFormTemp.districtId = this.addressIds[2];
                    this.dataFormTemp.streetId = this.addressIds[3];
                } else if (len == 3) {
                    this.dataFormTemp.provinceId = this.addressIds[0];
                    this.dataFormTemp.cityId = this.addressIds[1];
                    this.dataFormTemp.districtId = this.addressIds[2];
                } else if (len == 2) {
                    this.dataFormTemp.provinceId = this.addressIds[0];
                    this.dataFormTemp.cityId = this.addressIds[1];
                    // this.dataFormTemp.districtId = this.addressIds[2]
                } else if (len == 1) {
                    this.dataFormTemp.provinceId = this.addressIds[0];
                    // this.dataFormTemp.cityId = this.addressIds[1]
                    // this.dataFormTemp.districtId = this.addressIds[2]
                }
            },
            updateInvoice(flag) {
                if (flag == 1) {
                    this.isDisabled = false;
                } else {
                    this.isDisabled = true;
                }
            },
            updateContent(flag) {
                console.log(this.dataFormTemp.invoiceType);
                this.contentDisabled = false;
                for (var i = 0; i < flag.length; i++) {
                    if (flag[i] == "3") {
                        this.dataFormTemp.invoiceContent = [];
                        this.dataFormTemp.invoiceContent.push("1");
                        this.contentDisabled = true;
                        // }else{
                    }
                }
            },
            invoiceFlagStatus() {
                if (this.dataFormTemp.invoiceFlag == 1) {
                    return true;
                } else {
                    return false;
                }
            },
            // 回显
            getDetail() {
                this.couponLoading = false;
                getStoreinvoiceDetail().then((res) => {
                    if (res.code == 200) {
                        // invoiceFlag: '1',
                        // 	invoiceType:['1'],
                        // 	invoiceContent:['1'],
                        // 	addressee:'2',
                        // 	addresseePhone:'2',
                        // 	provinceId:'1',
                        // 	cityId:'',
                        // 	districtId:'',
                        // 	streetId:'',
                        // 	mailingAddress:'',
                        // 	detailedAddress:''
                        let arr = [];
                        if (res.data.provinceId) {
                            arr.push(res.data.provinceId);
                        }
                        if (res.data.cityId) {
                            arr.push(res.data.cityId);
                        }
                        if (res.data.districtId) {
                            arr.push(res.data.districtId);
                        }
                        if (res.data.streetId) {
                            arr.push(res.data.streetId);
                        }
                        this.addressIds = arr;
                        if (
                            res.data.invoiceFlag != null &&
                            res.data.invoiceFlag === 0
                        ) {
                            this.dataFormTemp.invoiceFlag = "0";
                        } else {
                            this.dataFormTemp.invoiceFlag = "1";
                        }
                        this.dataFormTemp.invoiceType = res.data.invoiceType.split(
                            ","
                        );
                        this.dataFormTemp.invoiceContent = res.data.invoiceContent.split(
                            ","
                        );
                        this.dataFormTemp.addressee = res.data.addressee;
                        this.dataFormTemp.addresseePhone = res.data.addresseePhone;
                        this.dataFormTemp.provinceId = res.data.provinceId;
                        this.dataFormTemp.cityId = res.data.cityId;
                        this.dataFormTemp.districtId = res.data.districtId;
                        this.dataFormTemp.streetId = res.data.streetId;
                        this.dataFormTemp.mailingAddress = res.data.mailingAddress;
                        this.dataFormTemp.detailedAddress =
                            res.data.detailedAddress;
                        this.updateInvoice(this.dataFormTemp.invoiceFlag);
                        this.updateContent(this.dataFormTemp.invoiceType);
                    }
                });
            },
            // 表单提交
            dataFormSubmit(dataFormTemp) {
                console.log(this.$refs[dataFormTemp]);

                this.$refs[dataFormTemp].validate((valid) => {
                    this.dataFormTemp.mailingAddress = this.dataFormTemp.mailingAddress;
                    let param = Object.assign({}, this.dataFormTemp);
                    if (param.invoiceFlag == "1" && param.invoiceContent == "") {
                        this.$message({
                            message: "发票内容不能为空",
                            type: "error",
                            duration: 1500,
                        });
                        return;
                    }
                    if (param.invoiceFlag == "1" && param.invoiceType == "") {
                        this.$message({
                            message: "发票类型不能为空",
                            type: "error",
                            duration: 1500,
                        });
                        return;
                    }
                    if (
                        param.detailedAddress == null ||
                        param.detailedAddress == ""
                    ) {
                        this.$message({
                            message: "详细地址不能为空",
                            type: "error",
                            duration: 1500,
                        });
                        return;
                    }
                    if (
                        param.mailingAddress == null ||
                        param.mailingAddress == ""
                    ) {
                        this.$message({
                            message: "邮寄地址不能为空",
                            type: "error",
                            duration: 1500,
                        });
                        return;
                    }
                    if (
                        param.invoiceContent &&
                        param.invoiceContent != "" &&
                        param.invoiceContent.length > 0
                    ) {
                        var newInvoiceContent = [];
                        for (var i = 0; i < param.invoiceContent.length; i++) {
                            if (param.invoiceContent[i] !== "") {
                                newInvoiceContent.push(param.invoiceContent[i]);
                            }
                        }
                        param.invoiceContent = newInvoiceContent.join();
                    } else {
                        param.invoiceContent = "";
                    }
                    if (
                        param.invoiceType &&
                        param.invoiceType != "" &&
                        param.invoiceType.length > 0
                    ) {
                        var newinvoiceType = [];
                        for (var i = 0; i < param.invoiceType.length; i++) {
                            if (param.invoiceType[i] !== "") {
                                newinvoiceType.push(param.invoiceType[i]);
                            }
                        }
                        param.invoiceType = newinvoiceType.join();
                    } else {
                        param.invoiceType = "";
                    }
                    if (valid) {
                        // this.saveLoading = true;
                        savaAndEditInvoice(param).then((res) => {
                            this.saveLoading = false;
                            let status = null;
                            if (res.code == "200") {
                                status = "success";
                                // this.changePage()
                            } else {
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500,
                            });
                        });
                    } else {
                        return false;
                    }
                });
            },

            // 返回上一页
            changePage() {
                this.$emit("changePage");
            },
        },
    };
</script>

<style lang="scss" scoped>
    .top-title {
        font-weight: 700;
        font-size: 18px;
        height: 40px;
        line-height: 40px;
    }
    .addOrEdit {
        margin-left: 10%;

        .btn-item {
            margin: 30px 150px;
        }
        .fullTitle {
            font-weight: 700;
            height: 40px;
            line-height: 40px;
        }

        .title-1 {
            margin-left: -60px;
        }

        .hrTips {
            color: #999999;
            margin-left: 150px;
            margin-bottom: 10px;
        }

        .el-form-item {
            display: block;
        }

        /deep/ .el-checkbox-group {
            display: inline-block;
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

        /deep/ .el-input {
            width: 400px !important;
        }

        .specSize {
            .el-input {
                width: 200px !important;
            }
        }
    }
</style>
