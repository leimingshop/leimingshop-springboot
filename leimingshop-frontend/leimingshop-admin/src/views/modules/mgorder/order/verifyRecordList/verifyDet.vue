<template>
    <div>
        <Bread :breaddata="orderDetBreaddata" @changePage="changePage" :index="index"></Bread>
        <div v-loading="backScanLoading">
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
                        {{orderInfoData.orderAmount }}
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
                    <p>
                        <span class="inforTit">店铺名称：</span>
                        {{orderInfoData.storeName}}
                    </p>
                </div>
                <div class="orderInfo">
                    <div class="title">商品信息</div>
                    <p>
                        <span class="inforTit">商品货号：</span>
                        {{goodsData.spuSerial}}
                    </p>
                    <p>
                        <span class="inforTit">商品名称：</span>
                        {{goodsData.spuName }}
                    </p>
                    <p>
                        <span class="inforTit">商品价格：</span>
                        {{data.goodsPrice }}
                    </p>
                    <p>
                        <span class="inforTit">商品数量：</span>
                        {{data.goodsNum }}
                    </p>
                    <p>
                        <span class="inforTit">sku编号：</span>
                        {{goodsData.specSerial}}
                    </p>
                    <p>
                        <span class="inforTit">规格型号：</span>
                        {{goodsData.specInfo}}
                    </p>
                </div>
            </div>
            <div class="orderConfig">
                <div class="orderConList">
                    <div class="title">核销记录</div>
                    <p>
                        <span class="inforTitW">核销人员：</span>
                        <span class="checkWeight">{{data.creator}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">核销数量：</span>
                        <span class="checkWeight">{{data.exchangeNum}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">核销时间：</span>
                        <span class="checkWeight">{{data.createDate}}</span>
                    </p>
                    <p>
                        <span class="inforTitW">电子提货码：</span>
                        <span class="checkWeight">{{data.fetchCode}}</span>
                    </p>
                </div>
            </div>

            <!--返回按钮-->
            <div style="margin-top: 40px;margin-bottom: 40px">
                <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>
            </div>
        </div>
    </div>
</template>
<script>
    import Bread from '@/components/bread'
    import {verifyCodeDetail} from "@/api/api";

    export default {
        data() {
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
                allmobPhone: '',
                orderData: '',
                orderInfoData: [],
                backScanLoading: false, //回显数据loading
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
                this.$nextTick(() => {
                    // console.log(this.orderDetBreaddata)
                    this.backScan()
                })
            },

            backScan() {
                var fn = verifyCodeDetail
                var obj = {id: this.row.id};
                this.backScanLoading = true
                fn(obj).then(res => {
                    this.backScanLoading = false
                    if (res.code == 200) {
                        this.data = res.data;
                        this.orderInfoData = res.data.adminOrderDetailDTO;
                        var orderGoodsDTOList = res.data.adminOrderDetailDTO.orderGoodsDTOList;
                        this.goodsData = orderGoodsDTOList[0];
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
            changeCompent() {

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
