<template>
    <div>
        <div  v-show="!discountDetVisible">
            <Bread :breaddata="orderDetBreaddata" @changePage="changePage" :index="index"></Bread>
            <div v-loading="backScanLoading">
                <el-steps :active="activeStep" align-center>
                    <el-step
                            v-for="(item,index) in data.orderTimeDTOList"
                            :key="index"
                            :title="item.orderOperation"
                            :description="item.operationDate"
                    ></el-step>
                </el-steps>
                <div class="orderUerInfo">
                    <div class="orderDe">
                        <div class="orderDelf">
                            <div>
                                订单编号：
                                <input type="text" v-model="data.orderSn" id="bar"/>
                                <el-button
                                        class="btn"
                                        type="info"
                                        size="mini"
                                        id="copy"
                                        icon="el-icon-message"
                                        plain
                                        @click="copyOrder()"
                                        data-clipboard-action="copy"
                                        data-clipboard-target="#bar"
                                >复制
                                </el-button>
                            </div>
                            <div>
                                订单状态：{{data.orderStatus==0?'已取消':data.orderStatus==10?'待付款':data.orderStatus==20?'待发货':data.orderStatus==30?'待收货':'交易完成'
                                }}
                            </div>
                        </div>
                        <div class="orderDerg">
                            <div>
                                操作:
                                <el-button type="primary" size="mini" plain @click="goDiscountDetFn(3)">查看订单优惠明细</el-button>
                            </div>
                            <el-button
                                    type="primary"
                                    :disabled="data.disable==1"
                                    plain
                                    size="mini"
                                    @click="addRemarks()"
                                    style="margin-right:10px;"
                            >增加备注
                            </el-button>
                        </div>
                    </div>
                    <div class="buyerInfo"
                        style="display: flex;justify-content: space-between;align-items: center;adding: 20px;    padding: 20px;margin: 20px 0;">
                        <!--左边购买用户,用户等级-->
                        <div style="width: 50%">
                            <div>
                                <span style="font-size:15px;color:#333;font-weight: bold;">购买用户：</span>
                                <span>{{data.buyerName}}</span>
                            </div>
                            <div style="margin-top:20px;">
                                <span style="font-size:15px;color:#333;font-weight: bold;">用户等级：</span>
                                <span>{{data.buyerGraderName}}</span>
                            </div>
                        </div>
                        <!--右边备注等级,商家备注-->
                        <div style="width: 50%">
                            <div>
                                <span style="font-size:15px;color:#333;font-weight: bold;">备注等级：</span>
                                <span v-if="data.sellerRemarkGrade==1">一级</span>
                                <span v-if="data.sellerRemarkGrade==2">二级</span>
                                <span v-if="data.sellerRemarkGrade==3">三级</span>
                                <span v-if="data.sellerRemarkGrade==4">四级</span>
                            </div>
                            <div style="margin-top:20px;">
                                <span style="font-size:15px;color:#333;font-weight: bold;">商家备注：</span>
                                <span>{{data.sellerRemark}}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="orderRecord">
                    <div class="orderInfo">
                        <div class="title">包裹</div>
                        <p>
                            <span class="inforTit">承运单位：</span>
                            {{data.transportCompanyName }}
                        </p>
                        <p>
                            <span class="inforTit">承运单号：</span>
                            {{data.transportCode }}
                        </p>
                    </div>
                    <div class="orderLogisticsText">

                        <div v-if="orderLog">
                            <div v-for="item in orderLog" class="order LogTextDiv">
                                <span class="orderLogTimeText">{{item.time}}</span>
                                <span class="orderLogText">{{item.context}}</span>
                            </div>
                        </div>
                        <span class="orderLogText" v-if="orderLog==''">暂无物流信息   </span>
                    </div>
                </div>
                <div class="orderConfig">
                    <div class="orderConList">
                        <div class="title">收货人信息</div>
                        <p>
                            <span class="inforTit">会员名称：</span>
                            {{data.buyerName}}
                        </p>
                        <p>
                            <span class="inforTit">收货人：</span>
                            {{addressInfo.trueName}}
                        </p>
                        <p>
                            <span class="inforTit">收货地址：</span>
                            <span>
                    {{addressInfo.areaInfo + addressInfo.address}}
                </span>
                        </p>
                        <p style="align-items: center;">
                            <span class="inforTit">手机号：</span>
                            {{addressInfo.mobPhone}}
                            <el-button type="primary" style="margin-left:4px" size="mini" plain @click="lookPhoneNum()">点击查看</el-button>
                        </p>
                        <p>
                            <span class="inforTit">买家留言：</span>
                            <span>{{data.orderMessage}}</span>
                        </p>
                    </div>
                    <div class="orderConList">
                        <div class="title">配送信息</div>
                        <p>
                            <span class="inforTit">配送方式：</span>
                            {{data.devlierType==1?'快递':data.devlierType==0?'无需物流':'--'}}
                        </p>
                        <p>
                            <span class="inforTit">运费：</span>
                            {{data.shippingFee!=null?'￥'+data.shippingFee:''}}
                        </p>
                        <p>
                            <span class="inforTit">配送日期：</span>
                            {{data.transportTime}}
                        </p>
                    </div>
                    <div class="orderConList">
                        <div class="title">付款信息</div>
                        <p>
                            <span class="inforTit">付款方式：</span>
                            {{data.paymentName}}
                        </p>
                        <p>
                            <span class="inforTit">付款时间：</span>
                            {{data.paymentTime}}
                        </p>
                        <p>
                            <span class="inforTit">商品总额：</span>
                            {{data.goodsAmount!=null?'￥'+data.goodsAmount:''}}
                        </p>
                        <p>
                            <span class="inforTit">运费金额：</span>
                            {{data.shippingFee!=null?'￥'+data.shippingFee:''}}
                        </p>
                        <p>
                            <span class="inforTit">促销价格：</span>
                            {{(data.couponAmount!=null && data.preferentialPrice != null)?'￥'+(data.preferentialPrice - data.couponAmount).toFixed(2):'￥0.00'}}
                        </p>
                        <p>
                            <span class="inforTit">优惠券：</span>
                            {{data.couponAmount!=null?'￥'+data.couponAmount:'￥0.00'}}
                        </p>
                        <p>
                            <span class="inforTit">积分：</span>￥0.00
                        </p>
                        <p>
                            <span class="inforTit">应支付金额：</span>
                            {{data.orderAmount!=null?'￥'+data.orderAmount:''}}
                        </p>
                    </div>
                    <div class="orderConList">
                        <div class="title">发票信息</div>
                        <p>
                            <span class="inforTit">发票类型：</span>--
                        </p>
                        <p>
                            <span class="inforTit">发票抬头：</span>--
                        </p>
                        <p>
                            <span class="inforTit">纳税人识别号：</span>--
                        </p>
                        <p>
                            <span class="inforTit">发票内容：</span>--
                        </p>
                    </div>
                </div>
                <div>
                    <el-table :data="orderData" style="width: 100%" border>
                        <el-table-column
                                prop="goodsName"
                                label="商品名称"
                                width="240"
                                :show-overflow-tooltip="true"
                                align="left"
                        >
                            <template slot-scope="scope">
                                <div style="text-align: left;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;" :title="scope.row.goodsName">
                                    <img :src="scope.row.goodsImage | filterImgUrl" width="40px" height="40px"/>
                                    <!-- <label>{{scope.row.goodsName}} </label> -->
                                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;margin-left:6px;"  @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="specSerial" label="商品货号" width="180" align="center"></el-table-column>
                        <el-table-column prop="specPrice" label="单价" align="center">
                            <template slot-scope="scope">{{scope.row.specPrice!=''?'￥'+scope.row.specPrice:''}}</template>
                        </el-table-column>
                        <el-table-column prop="rudePri" label="优惠金额" align="center">
                            <template slot-scope="scope">
                                {{(data.couponAmount!=null && data.preferentialPrice != null)?'￥'+(data.preferentialPrice - data.couponAmount).toFixed(2):'￥0.00'}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="num" label="赠送积分" align="center"></el-table-column>
                        <el-table-column prop="score" label="积分优惠购" align="center"></el-table-column>
                        <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
                        <el-table-column prop="goodsTotalPayPrice" label="应付金额" align="center">
                            <template
                                    slot-scope="scope"
                            >{{data.orderAmount!=null?'￥'+data.orderAmount:''}}
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="summary">
                        <ul>
                            <li>
                                <span class="inforTit">商品总价：</span>
                                {{data.goodsAmount!=null?'￥'+data.goodsAmount:''}}
                            </li>
                            <li>
                                <span class="inforTit">运费：</span>
                                {{data.shippingFee!=null?'￥'+data.shippingFee:''}}
                            </li>
                            <li>
                                <span class="inforTit">促销金额：</span>
                                {{(data.couponAmount!=null && data.preferentialPrice != null)?'￥'+(data.preferentialPrice - data.couponAmount).toFixed(2):'￥0.00'}}
                            </li>
                            <li>
                                <span class="inforTit">优惠券：</span>
                                {{data.couponAmount!=null?'￥'+data.couponAmount:'￥0.00'}}
                            </li>
                            <li>
                                <span class="inforTit">积分抵扣：</span>￥0.00
                            </li>
                            <li>
                                <span class="inforTit">应付金额：</span>
                                {{data.orderAmount!=null?'￥'+data.orderAmount:''}}
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="operationRecord">
                    <p class="title">操作日志</p>
                    <el-table :data="packageInfo" style="width: 100%" border>
                        <el-table-column prop="creator" label="操作人" width="180" align="center"></el-table-column>
                        <el-table-column prop="statusInfo" label="操作描述" align="center"></el-table-column>
                        <el-table-column prop="createDate" label="操作时间" align="center"></el-table-column>
                    </el-table>
                </div>

                <!--返回按钮-->
                <div style="margin-top: 40px;margin-bottom: 40px">
                    <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>

                </div>
            </div>
        </div>
        <modelRemark v-if="remarkVisible" ref="remarkCompon"></modelRemark>

         <!-- 查看订单优惠明细 -->
        <discountDet ref="discountDet" v-if="discountDetVisible" @hiddenDiscountDetFn="hiddenDiscountDetFn"></discountDet>
         <!-- 预览h5 -->
         <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
    </div>
</template>
<script>
  import Bread from '@/components/bread'
  import Clipboard from 'clipboard'
  import modelPreviewH5 from '../../mggoods/modules/model-preview-h5.vue'
  import modelRemark from './modules/model-remark'
 import {orderDetail, orderDetailBybh} from "@/api/api";
    import discountDet from "./discountDet.vue";
  export default {
    data () {
      return {
        previewH5Visible:false,
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
        addressInfo: '',
        backScanLoading:false, //回显数据loading
        discountDetVisible:false,  //折扣弹框变量
      }
    },
    components: {
      Bread,
      modelRemark,
      discountDet,modelPreviewH5
    },
    props: ['index', "orderDetBreaddata"],
    mounted () {

    },
    methods: {
       init(row) {
           console.log(row)
            this.row = row;
            this.$nextTick(()=>{
                // console.log(this.orderDetBreaddata)
                    this.backScan()
            })
        },
        // 预览h5
        previewH5Fn(row){
             window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
        },
        backScan(){
             var fn = this.row.id ? orderDetail : orderDetailBybh
                if (this.row.id) {
                    var obj = {id: this.row.id};
                } else {
                    var obj = {orderSn: this.row.aftersaleSn};
                }
                this.backScanLoading = true
                fn(obj).then(res => {
                    this.backScanLoading = false
                    if (res.code == 200) {
                        this.data = res.data;
                        this.addressInfo = res.data.orderAddressDTO; //收货人信息
                        this.handelePhone();
                        this.orderData = res.data.orderGoodsDTOList;
                        if (res.data.orderLogisticsDTO) {
                            this.orderLog = res.data.orderLogisticsDTO.data; //物流
                        } else {
                            this.orderLog = [];
                        }
                        this.packageInfo = res.data.orderLogDTOList; //订单状态
                        // this.detailOrList = 2;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
        },
        //处理手机号
        handelePhone() {
            this.allmobPhone = this.addressInfo.mobPhone
            this.addressInfo.mobPhone = this.allmobPhone.replace(
                this.allmobPhone.substring(3, 7),
                '****'
            )
            if (this.data.orderStatus == 10) {
                this.activeStep = 1 //提交订单
            } else if (this.data.orderStatus == 20) {
                this.activeStep = 2 //付款成功
            } else if (this.data.orderStatus == 30 || this.data.orderStatus == 0) {
                this.activeStep = 3 //商品出库 或者 取消订单
            } else {
                this.activeStep = 5
            }
        },
        changeCompent(){

        },
      //页面跳转 1-列表页
      changePage (nums) {
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
        //查看订单折扣详情
        goDiscountDetFn() {
            // this.$emit("showDiscountDetFn");
            this.discountDetVisible = true;
            this.$nextTick(()=>{
                this.$refs.discountDet.init(this.row)
            })
        },
        hiddenDiscountDetFn(){
            this.discountDetVisible = false;
        },
      //添加备注
      addRemarks () {
        if (this.data.disable == 1) return
        this.remarkVisible = true
        this.$nextTick(() => {
          this.$refs.remarkCompon.init(this.data)
        })
      },
      copyOrder () {
        var clipboard = new Clipboard('.btn')
        let that = this
        clipboard.on('success', e => {
          that.$message({
            message: '订单号复制成功',
            type: 'success',
            duration: 1000
          })
          clipboard.destroy()
        })

        clipboard.on('error', e => {
          that.$message({
            message: '订单号复制失败，请重新复制',
            type: 'error',
            duration: 1000
          })
          clipboard.destroy()
        })
      },
      //查看手机号
      lookPhoneNum () {
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
    .creater {
        display: inline-block;
        width: 80px;
        margin: 0 15px;
    }

    .el-textarea {
        width: 30%;
    }

    #bar {
        border: none;
        margin-right: 10px;
        min-width: 180px;
        display: inline-block;
    }

    #bar1 {
        border: none;
        display: inline-block;
    }

    .orderLogText {
        font-size: 15px;
        margin-left: 15px;
    }

    .orderUerInfo {
        width: 100%;
        height: auto;
        margin-top: 20px;
        border: 1px solid #d1d1d1;
    }

    .orderLogTimeText {
        font-size: 15px;
        font-weight: 600;
    }

    .orderLogisticsText {
        overflow: auto;
        width: 80%;
        height: 100%;
        padding: 10px;
        justify-content: flex-start;
    }

    .inforTit {
        width: 100px;
        font-weight: 600;
        text-align: left;
        color: #333;
        display: inline-block;
    }

    .goodsNameContent {
        width: 150px;
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        color: #4e80db;
        text-decoration: none;
        cursor: pointer;
    }

    .title {
        font-size: 16px;
        font-weight: 700;
        margin-bottom: 8px;
    }

    .orderDe {
        /* border: 1px solid #333; */
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .orderDelf,
    .orderDerg {
        /* display: flex; */
        display: flex;
        align-items: center;
    }

    .orderDelf div {
        margin-left: 20px;
        font-size: 18px;
    }

    .orderDerg div {
        margin-right: 20px;
    }

    .buyerInfo {
        box-sizing: border-box;
        border-radius: 5px;
        padding: 0px 8px;
        height: auto;
        /* margin: 5px 10px; */
        border: 1px solid #d1d1d1;
        display: flex;
    }

    .buyerInfo ul {
        width: 50%;
        padding: inherit;
    }

    .buyerInfo ul li {
        list-style-type: none;
        padding: 3px;
    }

    .orderRecord {
        widows: 100%;
        height: auto;
        height: 200px;
        display: flex;
        margin-top: 10px;
        border: 1px solid #d1d1d1;
    }

    .orderText {
        width: 80%;
        height: 100%;
        display: flex;
        padding: 10px;

        justify-content: flex-start;
    }

    .orderInfo {
        width: 26.5%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .el-textarea {
        width: 100%;
    }

    .el-textarea__inner {
        height: 100%;
        resize: none;
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
        width: 25%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: auto;
        display: flex;
        flex-direction: column;
    }

    .summary {
        width: 100%;
        display: flex;
        margin-top: 20px;
        justify-content: flex-end;
    }

    .summary ul li {
        list-style: none;
        width: 200px;
        line-height: 30px;
    }

    .operationRecord {
        width: 100%;
        padding-left: 20px;
        margin: 20px 0;
        height: auto;
        border: 1px solid #d1d1d1;
    }

    .operationRecord p {
        line-height: 30px;
    }

    .el-table {
        margin-top: 20px;
    }
</style>
