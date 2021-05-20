<template>
    <Modal
        v-model="adressStatus"
        :mask-closable="false"
        width="480"
        class="addAdress"
        @on-cancel="handleReset('adressForm')"
    >
        <div slot="header">
            <span class="h-title">{{ adressType }}收货地址</span>
        </div>
        <div class="content">
            <Form
                ref="adressForm"
                :model="adressForm"
                :label-colon="true"
                :rules="adressRule"
                :label-width="85"
            >
                <FormItem label="收货人" prop="trueName">
                    <Input
                        class="customerized"
                        v-model="adressForm.trueName"
                        clearable
                        maxlength="7"
                        placeholder="收货人姓名"
                    />
                </FormItem>
                <FormItem label="手机" prop="mobPhone">
                    <Input
                        class="customerized"
                        type="number"
                        v-model="adressForm.mobPhone"
                        maxlength="11"
                        placeholder="请填写正确的11位手机号码"
                    />
                </FormItem>
                <FormItem label="所在地区" prop="region">
                    <Cascader
                        class="customerized"
                        style="width: 210px"
                        :data="areaListOne"
                        :load-data="loadData"
                        @on-change="regionChange"
                        v-model="adressForm.region"
                        placeholder="请选择省/市/区"
                    ></Cascader>
                </FormItem>
                <FormItem label="详细地址" prop="address">
                    <Input
                        class="customerized"
                        style="width: 297px"
                        v-model="adressForm.address"
                        maxlength="20"
                        placeholder="完善详细地址"
                    />
                </FormItem>
                <FormItem label="邮政编码" v-if="!staticData">
                    <Input
                        class="customerized"
                        v-model="adressForm.zipCode"
                        maxlength="10"
                        placeholder="请输入邮编"
                    />
                </FormItem>
                <!-- <FormItem label="地址标签"> -->
                <div class="tiptype" v-if="!staticData">
                    <!-- <div class="tipList">
                            <span v-for="(item,index) in adressTips" :key="index">{{item}}</span>
                        </div>
          <Input v-model="addTips" placeholder="填写其它标签" maxlength="4" @on-enter="toEnter" clearable style="width: 175px" />-->

                    <div class="adressSet">
                        <Checkbox
                            class="customerized customerized-checkbox"
                            v-model="adressForm.defaultFlag"
                            @on-change="adressSet($event)"
                            >设为默认收货地址</Checkbox
                        >
                        <span
                            style="
                                font-size: 12px;
                                font-family: PingFangSC-Regular, PingFang SC;
                                font-weight: 400;
                                color: rgba(34, 34, 34, 1);
                            "
                            >提示：每次下单会默认使用该地址</span
                        >
                    </div>
                </div>
                <!-- </FormItem> -->
            </Form>
        </div>
        <div slot="footer" class="footerBtn">
            <Button
                style="border: none"
                type="default"
                class="cancelBtn 111"
                @click="handleReset('adressForm')"
                >取消</Button
            >

            <Button
                type="primary"
                class="confirmBtn"
                :loading="modal_loading"
                @click="handleSubmit('adressForm')"
                >保存</Button
            >
        </div>
    </Modal>
</template>

<script>
    import {
        AddressUpdata,
        AddAddress,
        AreaList,
        AddressDetail,
    } from "@/api/api_04.shoppingCar.js";
    export default {
        props: {
            /**
             * @author huowei
             * @param {staticData} 确定以后, 是否调接口保存地址信息
             * @value { true }  不调接口, 将数据返回到页面
             * @value { false } 调接口, 保存数据
             */
            staticData: {
                type: Boolean,
                default: false,
            },
        },

        data() {
            return {
                adressType: "新增",
                adressStatus: false,
                modal_loading: false,
                adressForm: {
                    address: "", //地址 ,
                    areaId: "", //地区ID ,
                    areaInfo: "", //地址内容 ,
                    cityId: "", //市级ID ,
                    defaultFlag: false, //是否默认（ 默认为0:非默认，1:已默认） ,
                    id: "", //主键 ,
                    memberId: "", //会员ID ,
                    mobPhone: "", //收件人电话 ,
                    provinceId: "", //省级id ,
                    stressId: "", //街道ID ,
                    trueName: "", //收件人名称 ,
                    zipCode: "", //邮编
                    region: [],
                },
                adressRule: {
                    trueName: [
                        {
                            required: true,
                            message: "收货人姓名不能为空",
                            trigger: "blur",
                        },
                    ],
                    mobPhone: [
                        {
                            required: true,
                            message: "手机号不能为空",
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                console.log("122", value);
                                let reg = /^1[3456789]\d{9}$/;
                                if (!reg.test(value)) {
                                    callback(new Error("手机号格式错误"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    region: [
                        {
                            required: true,
                            validator: (rule, value, callback) => {
                                console.log("122", value);
                                if (value.length == 0 || !value[0]) {
                                    callback(new Error("所在地区不能为空"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    address: [
                        {
                            required: true,
                            message: "详细地址不能为空",
                            trigger: "blur",
                        },
                    ],
                },
                areaListOne: [],
                childrenList: [],
            };
        },
        methods: {
            adressFn(param) {
                this.adressStatus = true;
                this.getArea(0);

                if (param) {
                    this.adressType = "修改";
                    //  `author huowei`
                    if (this.staticData) {
                        this.adressForm = param;
                        this.adressForm.region = [
                            param.provinceId,
                            param.cityId,
                            param.areaId,
                            param.stressId,
                        ];
                    } else {
                        this.getInfo(param.id);
                    }
                } else {
                    this.adressType = "添加";
                }
            },

            regionChange(e) {
                console.log("修改id", e);
                this.adressForm.region = e;
                this.adressForm.provinceId = e[0];
                this.adressForm.cityId = e[1];
                this.adressForm.areaId = e[2];
                if (e.length == 3) {
                    this.adressForm.stressId = "";
                } else {
                    this.adressForm.stressId = e[3];
                }
            },
            // 新增修改时地址设置默认
            adressSet(e) {
                console.log(e);
                if (e) {
                    this.adressForm.defaultFlag = true;
                } else {
                    this.adressForm.defaultFlag = false;
                }
            },

            // 第一级的调地区接口  以后可能有用
            getArea(id) {
                AreaList({ id: id })
                    .then((res) => {
                        if (res.code == 200) {
                            this.areaListOne = res.data.map((item) => {
                                return {
                                    value: item.id,
                                    label: item.areaName,
                                    children: [],
                                    loading: false,
                                    flag: item.count,
                                };
                            });
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            // 一级一级的调地区接口  以后可能有用
            loadData(item, callback) {
                console.log("返回数据", item);
                item.loading = true;
                AreaList({ id: item.value })
                    .then((res) => {
                        if (res.code == 200) {
                            item.children = res.data.map((item) => {
                                if (item.count != 0) {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                        children: [],
                                        loading: false,
                                    };
                                } else {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                    };
                                }
                            });
                            item.loading = false;
                            callback();
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            // 新增标签回车事件
            toEnter() {
                if (!this.addTips) {
                    this.$Message.warning("新增不能为空");
                } else {
                    this.adressTips.push(this.addTips);
                    this.addTips = "";
                }
            },
            // 修改时回显
            getInfo(id) {
                AddressDetail({ id: id })
                    .then((res) => {
                        if (res && res.code == 200 && res.data) {
                            res.data.defaultFlag = res.data.defaultFlag
                                ? true
                                : false;
                            this.adressForm = res.data;
                            this.adressForm.region = [
                                this.adressForm.provinceId,
                                this.adressForm.cityId,
                                this.adressForm.areaId,
                                this.adressForm.stressId,
                            ];
                        } else {
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },

            //通过省市县街道ID获取对应的省市县街道lable
            demoFn(arr, data, city = []) {
                if (typeof data === "object") {
                    for (let i = 0; arr[i] !== undefined; i++) {
                        for (let j = 0; data[j] !== undefined; j++) {
                            if (arr[i] === data[j].value) {
                                city.push(data[j].label);
                            }
                        }
                    }
                    for (let i = 0; data[i] !== undefined; i++) {
                        this.demoFn(arr, data[i].children, city);
                    }
                }
                this.adressForm.areaInfo = city.join(" ");
            },

            // 新增修改地址表单重置
            handleReset(name) {
                this.$refs[name].resetFields();
                this.adressStatus = false;
            },

            // 新增修改地址表单提交
            handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.demoFn(this.adressForm.region, this.areaListOne);
                        this.modal_loading = true;
                        let newObj = JSON.parse(JSON.stringify(this.adressForm));
                        newObj.defaultFlag = this.adressForm.defaultFlag ? 1 : 0;
                        newObj.region = JSON.stringify(this.adressForm.region);
                        //获取对应的省市县街道lable
                        if (this.adressType == "修改") {
                            // 判断是否页面所需回调数据 `author huowei`
                            if (this.staticData) {
                                this.handleEditAfterSaleAddress(newObj);
                                this.modal_loading = false;
                                this.adressStatus = false;
                                return;
                            }

                            AddressUpdata(newObj)
                                .then((res) => {
                                    // console.log('修改结果',res)
                                    if (res.code == 200) {
                                        // 成功之后的回调
                                        this.$emit("getAction", newObj);
                                        this.modal_loading = false;
                                        this.adressStatus = false;
                                        this.adressForm = {
                                            address: "", //地址 ,
                                            areaId: "", //地区ID ,
                                            areaInfo: "", //地址内容 ,
                                            cityId: "", //市级ID ,
                                            defaultFlag: false, //是否默认（ 默认为0:非默认，1:已默认） ,
                                            id: "", //主键 ,
                                            memberId: "", //会员ID ,
                                            mobPhone: "", //收件人电话 ,
                                            provinceId: "", //省级id ,
                                            stressId: "", //街道ID ,
                                            trueName: "", //收件人名称 ,
                                            zipCode: "", //邮编
                                            region: [],
                                        };
                                    }
                                })
                                .catch((err) => {
                                    console.log(err);
                                });
                        } else {
                            delete newObj["id"];
                            console.log(newObj);
                            AddAddress(newObj)
                                .then((res) => {
                                    // console.log('新增结果',res)
                                    if (res.code == 200) {
                                        // 成功之后的回调
                                        this.$emit("getAction", newObj);
                                        // this.$Message.success(res.msg);
                                        this.modal_loading = false;
                                        this.adressStatus = false;
                                        this.adressForm = {
                                            address: "", //地址 ,
                                            areaId: "", //地区ID ,
                                            areaInfo: "", //地址内容 ,
                                            cityId: "", //市级ID ,
                                            defaultFlag: false, //是否默认（ 默认为0:非默认，1:已默认） ,
                                            id: "", //主键 ,
                                            memberId: "", //会员ID ,
                                            mobPhone: "", //收件人电话 ,
                                            provinceId: "", //省级id ,
                                            stressId: "", //街道ID ,
                                            trueName: "", //收件人名称 ,
                                            zipCode: "", //邮编
                                            region: [],
                                        };
                                    }
                                })
                                .catch((err) => {
                                    console.log(err);
                                });
                        }
                    }
                });
            },

            /*
             * @author: huowei
             * @method 修改商品订单售后地址所需方法
             *
             */
            handleEditAfterSaleAddress(address) {
                this.$emit("handleEditAfterSaleAddress", address);
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";
    .h-title {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        height: 16px;
        color: #333333;
        line-height: 16px;
    }
    .adressSet {
        width: 100%;
        display: flex;
        /deep/ .ivu-checkbox-default {
            font-size: 12px;
            color: rgba(34, 34, 34, 1);
        }
    }
    /deep/ .ivu-modal-close {
        top: 10px;
        right: 15px;
    }
    /deep/ .ivu-modal-header {
        padding: 30px 16px 20px 16px !important;
    }
    /deep/ .ivu-icon-ios-close:hover {
        color: #dd2619;
    }

    /deep/ .ivu-modal-body {
        padding-top: 0 !important;
        .content {
            margin: 0px 4px !important;
            /deep/ .ivu-form .ivu-form-item-label {
                color: #222;
            }
        }
    }
    /deep/ .ivu-modal-footer {
        height: 90px !important;
    }
    .ivu-modal-footer .footerBtn {
        /deep/ button {
            width: 60px;
            height: 30px;
            font-size: 14px;
        }
    }
    .confirmBtn {
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
    }
    .tiptype {
        margin-left: 85px;
    }
    .customerized {
        /deep/ .ivu-input-default {
            height: 34px !important;
            font-size: 12px;
            box-shadow: 0 0 0 0;
        }
        /deep/ .ivu-input:focus {
            border: 1px solid #b7b7b7;
        }
        /deep/ .ivu-checkbox-focus {
            border: 1px solid #b7b7b7;
            box-shadow: 0 0 0 0;
        }
    }
    .customerized-checkbox {
        /deep/ .ivu-checkbox-inner {
            width: 12px;
            height: 12px;
            &:after {
                top: 1px;
                left: 3px;
            }
        }
    }
    /deep/ .ivu-form-item-error .customerized .ivu-input {
        border: 1px solid #dd2619;
    }
    /deep/ .ivu-form-item-error-tip {
        font-size: 12px;
    }
</style>
