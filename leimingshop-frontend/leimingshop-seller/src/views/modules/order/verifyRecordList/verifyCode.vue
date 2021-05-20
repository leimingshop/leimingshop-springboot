<template>
    <div>
        <Bread :breaddata="orderDetBreaddata" @changePage="changePage" :index="index"></Bread>
        <div style="display: flex; align-items: center;">
            <p style="font-weight: 700;">电子提货码：</p>
            <el-input style="width: 200px" v-model="fetchcode" placeholder="请输入电子提货码"></el-input>
            <el-button style="margin-left: 10px" type="primary" @click="verifyCodeClick(fetchcode)">查询</el-button>
            <el-button style="margin-left: 10px" type="primary" @click="changePage()">返回</el-button>
        </div>
        <el-dialog
                title="核销确认"
                :visible.sync="checkDlg"
                width="30%">
            <div style="display: flex;flex-direction: column;">
                <p>
                    <span class="inforTit">商品名称：</span>
                    {{goodsData.spuName}}
                </p>
                <p>
                    <span class="inforTit">规格型号：</span>
                    {{goodsData.specAttrValueName}}
                </p>
                <p>
                    <span class="inforTit">商品数量：</span>
                    {{data.goodsNum }}
                    <span class="inforTitDia">未核销商品数量：</span>
                    {{data.goodsNum - data.exchangeNum}}
                </p>
                <p>
                    <span class="inforTit">用户名称：</span>
                    {{orderInfoData.orderAddressDTO&&orderInfoData.orderAddressDTO.trueName}}
                </p>
                <p>
                    <span class="inforTit">用户手机：</span>
                    {{orderInfoData.orderAddressDTO&&orderInfoData.orderAddressDTO.mobPhone}}
                </p>
                <p style="margin-left: 65px;color: red;">请认真核对以上信息，并确认用户已到店核销商品。</p>
                <el-form style="margin-left: 65px" :model="numberValidateForm" ref="dataFormTemp" label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item
                            label="核销商品数量："
                            prop="age"
                            :rules="[
                               { type: 'number', message: '数量必须为数字值'}
                             ]"
                    >
<!--                        <el-input type="number" style="width: 80px" v-model.number="numberValidateForm.amount"></el-input>-->
                        <el-input-number v-model="numberValidateForm.amount" :precision="0" :step="1" :min="0"
                                          :max="999999"
                                          controls-position="right"
                        >
                        </el-input-number>
                        个
                    </el-form-item>
                        <el-button class="btn" type="primary" @click="verifySubmit()" :loading="saveLoading">
                            {{saveLoading?"提交中...":"确认"}}
                        </el-button>
                        <el-button class="btn" type="primary" plain @click="checkDlg = false" plain>取消</el-button>
                </el-form>
            </div>
        </el-dialog>
        <div v-show="backScanLoading">
            <div class="orderRecord">
                <div class="orderInfo">
                    <div class="title">订单信息</div>
                    <p>
                        <span class="inforTit">订单编号：</span>
                        {{orderInfoData.orderSn }}
                    </p>
                    <p>
                        <span class="inforTit">会员名称：</span>
                        {{orderInfoData.buyerName }}
                    </p>
                    <p>
                        <span class="inforTit">下单时间：</span>
                        {{orderInfoData.createDate }}
                    </p>
                    <p>
                        <span class="inforTit">订单金额：</span>
                        ￥{{orderInfoData.orderAmount }}
                    </p>
                    <p>
                        <span class="inforTit">支付方式：</span>
                        {{orderInfoData.paymentName }}
                    </p>
                    <p>
                        <span class="inforTit">支付时间：</span>
                        {{orderInfoData.paymentTime }}
                    </p>
                    <p>
                        <span class="inforTit">收件人：</span>
                        {{orderInfoData.orderAddressDTO&&orderInfoData.orderAddressDTO.trueName}}
                    </p>
                    <p>
                        <span class="inforTit">手机号码：</span>
                        {{orderInfoData.orderAddressDTO&&orderInfoData.orderAddressDTO.mobPhone}}
                    </p>
<!--                    <p>-->
<!--                        <span class="inforTit">店铺名称：</span>-->
<!--                        {{orderInfoData.storeName}}-->
<!--                    </p>-->
                </div>
                <div class="orderInfo">
                    <div class="title">商品信息</div>
                    <p>
                        <span class="inforTit">商品货号：</span>
                        {{goodsData.spuSerial}}
                    </p>
                    <p>
                        <span class="inforTit">商品名称：</span>
                        {{goodsData.spuName}}
                    </p>
                    <p>
                        <span class="inforTit">商品价格：</span>
                        ￥{{data.goodsPrice }}
                    </p>
                    <p>
                        <span class="inforTit">商品数量：</span>
                        {{data.goodsNum }}
                    </p>
                    <p>
                        <span class="inforTit">sku编号：</span>
                        {{data.goodsSerial}}
                    </p>
                    <p>
                        <span class="inforTit">规格型号：</span>
                        {{goodsData.specAttrValueName}}
                    </p>
                </div>
            </div>
            <div class="orderConfig">
                <div class="orderConList">
                    <div class="title">订单核销</div>
                    <p>
                        <span class="inforTitW">核销状态：</span>
                        <span style="color: #54B99E;font-weight: 700;">{{data.codeStatus == 0 ? '待核销' : data.codeStatus == 1 ? '部分核销' : data.codeStatus == 2 ? '已核销' : '已过期'}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">未核销商品数量：</span>
                        <span class="checkWeight">{{data.goodsNum - data.exchangeNum}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">有效期：</span>
                        <span class="checkWeight">领取后{{data.validDay == -1 ? '永久有效' : data.validDay+'天有效'}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">截止日期：</span>
                        <span class="checkWeight">{{data.expiryDate}}</span>
                    </p>
                </div>
            </div>
            <div style="margin-top: 40px;margin-bottom: 40px">
                <el-button v-if="data.codeStatus == 0 || data.codeStatus == 1" type="primary"
                           style="display:block;margin:0 auto" @click="upDialog()">确认核销
                </el-button>
            </div>

        </div>

    </div>
</template>
<script>
    import Bread from '@/components/bread'
    import {fetchCodeDetail, orderVerify} from "@/api/api";

    export default {
        data() {
            //物流单号
            var transport = (rule, value, callback) => {
                if (value == "") {
                    callback(new Error("物流单号不得为空"));
                } else if (value.length > 15) {
                    callback(new Error("物流单号最长限制15位"));
                } else {
                    callback();
                }
            };
            return {
                previewH5Visible: false,
                textarea: '',
                // form: {
                //   desc: '',
                //   resource: 0
                // },
                allmobPhone: '',
                remarkVisible: false,
                activeStep: 1,
                row: "",
                data: [],
                addressInfo: [], //地址数据
                packageInfo: [], //包裹数据
                orderLog: [], //操作日志
                orderData: [],
                orderInfoData: [],
                backScanLoading: false, //回显数据loading
                fetchcode: '',
                checkDlg: false, // 确认核销
                checkNumber: '', // 确认核销数量
                numberValidateForm: {
                    amount: ''
                },
                // 提交
                saveLoading: false,
                goodsData: [] // 商品数组
            }
        },
        components: {
            Bread,
        },
        props: ['index', "orderDetBreaddata"],
        mounted() {

        },
        methods: {
            init(row) {
                console.log(row)
                this.row = row;
                // this.$nextTick(() => {
                //     // console.log(this.orderDetBreaddata)
                //     this.backScan()
                // })
            },

            verifySubmit() {
                this.$refs["dataFormTemp"].validate(valid => {
                    if (valid) {
                        if (this.numberValidateForm.amount == '') {
                            this.$message({
                                type: "warning",
                                message: "核销数量不能为空！"
                            });
                            return false;
                        }
                        if (this.numberValidateForm.amount < 1) {
                            this.$message({
                                type: "warning",
                                message: "核销数量必须大于1！"
                            });
                            return false;
                        }
                        if (this.numberValidateForm.amount > (this.data.goodsNum - this.data.exchangeNum)) {
                            this.$message({
                                type: "warning",
                                message: "核销数量不能大于剩余核销数量！"
                            });
                            return false;
                        }
                        const obj = {
                            id: this.data.id,
                            verifyNum: this.numberValidateForm.amount
                        };
                        orderVerify(obj).then(res => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg
                                });
                                this.checkDlg = false;
                                this.verifyCodeClick(this.fetchcode)
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            changeCompent() {

            },
            upDialog() {
                this.checkDlg = true;
                this.numberValidateForm.amount = '';
            },
            //页面跳转 1-列表页
            changePage(nums) {
                this.$emit('changePage', nums)
                // if(this.$route.query.tabIndex == 2){
                //     //关闭当前页面
                //     let arr = this.$store.state.contentTabs.filter(v => {
                //         return v.name != this.$route.name
                //     })
                //     this.$store.state.contentTabs = arr
                //     //返回原来页面
                //     this.$router.go(-1)
                // }else {
                //
                // }
            },
            verifyCodeClick(fetchCode) {
                var that = this;
                var fn = fetchCodeDetail;
                var obj = {
                    params: {
                        fetchCode: fetchCode
                    }
                };

                fn(obj).then(res => {

                    if (res.code == 200) {
                        if (res.data == null) {
                            this.backScanLoading = false;
                            this.$message({
                                type: "warning",
                                message: '电子提货码不存在！'
                            });
                            return;
                        }
                        this.data = res.data;
                        this.backScanLoading = true;
                        this.orderInfoData = res.data.adminOrderDetailDTO;
                        var orderGoodsDTOList = res.data.adminOrderDetailDTO.orderGoodsDTOList;
                        orderGoodsDTOList.forEach(item => {
                            if (item.id == this.data.orderGoodsId ) {
                                this.goodsData = item;
                            }
                        })
                            this.addressInfo = res.data.orderAddressDTO; //收货人信息
                        this.orderData = res.data.orderGoodsDTOList;
                        // this.detailOrList = 2;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });

            },

            hiddenDiscountDetFn() {
                this.discountDetVisible = false;
            },
            //查看手机号
            lookPhoneNum() {
                if (/\*\*\*\*/.test(this.addressInfo.mobPhone)) {
                    this.addressInfo.mobPhone = this.allmobPhone
                } else {
                    this.addressInfo.mobPhone = this.allmobPhone.replace(
                        this.allmobPhone.substring(3, 7),
                        '****'
                    )
                }
            }
        }
    }
</script>
<style scoped>


    .inforTit {
        width: 100px;
        text-align: left;
        color: #333;
        display: inline-block;
        margin: 0 0 0 65px;
    }

    .inforTitW {
        width: 120px;
        font-weight: 700;
        text-align: left;
        color: #333;
        display: inline-block;
        margin: 0 0 0 65px;
    }

    .inforTitDia {
        text-align: left;
        color: #333;
        display: inline-block;
        margin: 0 0 0 65px;
    }

    .checkWeight {
        font-weight: 700;
    }

    .title {
        font-size: 16px;
        font-weight: 700;
        margin-bottom: 8px;
    }

    .orderRecord {
        widows: 100%;
        height: auto;
        display: flex;
        margin-top: 10px;
        border: 1px solid #d1d1d1;
    }

    .orderInfo {
        width: 50%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .orderConfig {
        width: 100%;
        border: 1px solid #d1d1d1;
        height: auto;
        display: flex;
        justify-content: flex-start;
        margin-top: 10px;
    }

    .orderConList {
        width: 50%;
        padding: 20px 0 0 30px;
        text-align: left;
        height: auto;
        display: flex;
        flex-direction: column;
    }

</style>
