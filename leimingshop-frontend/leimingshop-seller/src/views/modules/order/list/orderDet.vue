<template>
    <div class="orderDetClass">
        <div  v-show="!discountDetVisible">
            <!-- <Bread :breaddata="orderDetBreadData" @changePage="changePage" :index="index ? index : '2'"></Bread> -->
            <Bread :breaddata="orderDetBreaddata" @changePage="changePage" :index="index"></Bread>
            <div v-loading="backScanLoading">
                <el-steps :active="activeStep" align-center>
                    <el-step
                            style="margin-bottom: 50px; padding-top:50px; border-top: 1px solid #ECEDF1;"
                            v-for="(item,index) in data.orderTimeDTOList"
                            :key="index"
                            :title="item.orderOperation"
                            :description="item.operationDate"
                    ></el-step>
                </el-steps>
                <div class="orderUerInfo">
                    <div class="orderDe">
                        <!-- <div class="orderDelf"> -->
                            <div  style="display: flex; align-items: center;width:36%">
                                <label  class="weightedFont">订单编号：</label>
                                <input  class="weightedFont orderNumber" type="text" v-model="data.orderSn"/>
                                <el-button
                                        class="btn"
                                        type="info"
                                        size="mini"
                                        id="copy_text"
                                        icon="el-icon-message"
                                        plain
                                        @click="copyOrder"
                                        :data-clipboard-text="data.orderSn"


                                >复制
                                </el-button>
                            </div>
                        <div  v-if="data.virtualFlag == 1" style="width:20%;text-align: center;">
                            <label class="weightedFont">订单类型：虚拟订单</label>
                        </div>
                            <div  style="width:31%;text-align: center;">
                                <label class="weightedFont">订单状态：</label>
                                 <span style="color:#0092FE">
                                     {{data.orderStatus==0?'已取消':data.orderStatus==10?'待付款':data.orderStatus==20?'待发货':data.orderStatus==30?'待收货':'交易完成'
                                }}
                                 </span>
            </div>
            <!-- </div> -->
            <div class="orderDerg" style="width:33%;justify-content: flex-end;">
              <div>
                <label class="weightedFont">操作：</label>
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
          <div class="buyerInfo">
            <!--左边购买用户,用户等级-->
            <div style="width: 50%">
              <div class="contentFont">
                <span>购买用户：</span>
                <span>{{data.buyerName}}</span>
              </div>
              <div class="contentFont" style="margin-top:20px;">
                <span>用户等级：</span>
                <span>{{data.buyerGraderName}}</span>
              </div>
            </div>
            <!--右边备注等级,商家备注-->
            <div style="width: 50%">
              <div class="contentFont">
                <span>备注等级：</span>
                <span v-if="data.sellerRemarkGrade==1">一级</span>
                <span v-if="data.sellerRemarkGrade==2">二级</span>
                <span v-if="data.sellerRemarkGrade==3">三级</span>
                <span v-if="data.sellerRemarkGrade==4">四级</span>
              </div>
              <div class="contentFont" style="margin-top:20px;">
                <span>商家备注：</span>
                <span>{{data.sellerRemark}}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="orderRecord">
          <div class="orderInfo">
            <div class="title weightedFont">包裹</div>
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
            <div class="title weightedFont">收货人信息</div>
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
                    {{addressInfo.areaInfo == null ? '' : addressInfo.areaInfo + addressInfo.address}}
                </span>
            </p>
            <p style="align-items: center;">
              <span class="inforTit">手机号：</span>
              {{addressInfo.mobPhone}}
              <el-button type="primary" style="margin-left:4px;padding:7px 6px" size="mini" plain @click="lookPhoneNum()">点击查看
              </el-button>
            </p>
            <p>
              <span class="inforTit">买家留言：</span>
              <span>{{data.orderMessage}}</span>
            </p>
          </div>
          <div class="orderConList">
            <div class="title weightedFont">配送信息</div>
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
            <div class="title weightedFont ">付款信息</div>
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
            <div class="title weightedFont">发票信息</div>
            <template v-if="orderInvoiceData.invoiceEvolve == 2 || orderInvoiceData.invoiceEvolve == 4">
              <p><span class="inforTit">发票类型：</span>
                <span v-if="orderInvoiceData.storeInvoiceType==1">电子发票</span>
                <span v-if="orderInvoiceData.storeInvoiceType==2">纸质发票</span>
                <span v-if="orderInvoiceData.storeInvoiceType==3">增值税专项发票</span></p>
              <p><span class="inforTit">发票内容：</span>
                <span v-if="orderInvoiceData.storeInvoiceContent==1">商品明细</span>
                <span v-if="orderInvoiceData.storeInvoiceContent==2">商品类别</span></p>
              <p><span class="inforTit">抬头类型：</span>
                <span v-if="orderInvoiceData.companyType==0">不开票</span>
                <span v-if="orderInvoiceData.companyType==1">个人</span>
                <span v-if="orderInvoiceData.companyType==2">单位</span></p>
            </template>
            <template v-if="orderInvoiceData.invoiceEvolve == 1 || orderInvoiceData.invoiceEvolve == 3">
              <p><span class="inforTit">发票类型：</span>
                <span v-if="orderInvoiceData.memberInvoiceType==1">电子发票</span>
                <span v-if="orderInvoiceData.memberInvoiceType==2">纸质发票</span>
                <span v-if="orderInvoiceData.memberInvoiceType==3">增值税专项发票</span></p>
              <p><span class="inforTit">发票内容：</span>
                <span v-if="orderInvoiceData.memberInvoiceContent==1">商品明细</span>
                <span v-if="orderInvoiceData.memberInvoiceContent==2">商品类别</span></p>
              <p><span class="inforTit">抬头类型：</span>
                <span v-if="orderInvoiceData.companyType==0">不开票</span>
                <span v-if="orderInvoiceData.companyType==1">个人</span>
                <span v-if="orderInvoiceData.companyType==2">单位</span></p>
            </template>
              <p v-if="orderInvoiceData.companyType==1"><span  class="inforTit">个人名称：</span>{{orderInvoiceData.personalName}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">单位名称：</span>{{orderInvoiceData.company}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">公司税号：</span>{{orderInvoiceData.dutyParagraph}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">注册地址：</span>{{orderInvoiceData.registeredAddress}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">注册电话：</span>{{orderInvoiceData.officePhone}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">开户银行：</span>{{orderInvoiceData.bankName}}</p>
              <p v-if="orderInvoiceData.companyType==2"><span  class="inforTit">银行账号：</span>{{orderInvoiceData.bankNumber}}</p>
            <template v-if="data.invoiceFlag == 1 || data.invoiceFlag == 2">
              <div class="btn-box" style="margin: 20px 0">
                <el-button v-if="(orderInvoiceData.invoiceEvolve == 1 || orderInvoiceData.invoiceEvolve == 3)
                &&(data.orderStatus==40)"
                type="primary" @click="gotoInvoice(orderInvoiceData,2)">开票</el-button>
                <el-button v-if="orderInvoiceData.invoiceEvolve == 2 || orderInvoiceData.invoiceEvolve == 4" class="btn-item" type="primary" plain @click="gotoInvoice(orderInvoiceData,2)" >查看</el-button>
              </div>
            </template>
          </div>
        </div>
        <div>

          <div class="title" style="margin-top: 50px">
            <div>商品信息</div>
          </div>
          <!-- <div style="margin: 20px 0 10px 0;">
              <span style=";font-size: 18px;">{{data.storeName}}</span>
              <span style="margin-left: 15px;font-size: 18px;">店铺ID: {{data.storeId}}</span>
          </div> -->

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
                                <span v-if="scope.row.activityType == 3">
                                    {{((scope.row.specPrice - scope.row.specPayPrice) * scope.row.goodsNum).toFixed(2)}}
                                </span>
                                <span v-if="scope.row.activityType != 3">
                                    {{scope.row.discountActivityAmount != ''?'￥'+scope.row.discountActivityAmount:'￥0.00'}}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="num" label="赠送积分" align="center"></el-table-column>
                        <el-table-column prop="score" label="积分优惠购" align="center"></el-table-column>
                        <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
                        <el-table-column prop="goodsTotalPayPrice" label="应付金额" align="center">
                            <template
                                    slot-scope="scope"
                            >{{scope.row.goodsTotalPayPrice!=''?'￥'+scope.row.goodsTotalPayPrice:''}}
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="summary">
                        <span style="font-weight:500;">总计：</span>
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
                                {{(data.couponAmount!=null && data.preferentialPrice != null)?'￥'+(data.preferentialPrice - data.couponAmount).toFixed(2):''}}
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
import { Message } from 'element-ui'

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
				orderInvoiceData: {},
				addressInfo: [], //地址数据
				packageInfo: [], //包裹数据
				orderLog: [], //操作日志
				orderData: [],
				allmobPhone: '',
				addressInfo: '',
				backScanLoading: false, //回显数据loading
				discountDetVisible: false,  //折扣弹框变量
			}
		},
		components: {
			Bread,
			modelRemark,
			discountDet, modelPreviewH5
		},
		props: ['index', "orderDetBreaddata"],
		mounted() {

		},
		methods: {
			init(row) {
				this.row = row;
				this.$nextTick(() => {
					// console.log(this.orderDetBreaddata)
					this.backScan()
				})
			},
			// 预览h5
			previewH5Fn(row) {
				 window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
			},
			backScan() {
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
						if (res.data.orderInvoiceDTO) {
							this.orderInvoiceData = res.data.orderInvoiceDTO;  //发票
						}

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
			//页面跳转 1-列表页
			changePage(nums) {
				this.$emit('changePage', nums)
			},
			gotoInvoice(row,type) {
       
				this.$router.push({
          path: '/finance-invoice-manage',
        
					query: {
            row:row,
						tabIndex: type,
					}
        })
        console.log(this.$router,56789)
			},
			//查看订单折扣详情
			goDiscountDetFn() {
				// this.$emit("showDiscountDetFn");
				this.discountDetVisible = true;
				this.$nextTick(() => {
					this.$refs.discountDet.init(this.data.orderSn)
				})
			},
			hiddenDiscountDetFn() {
				this.discountDetVisible = false;
			},
			//添加备注
			addRemarks() {
				if (this.data.disable == 1) return
				this.remarkVisible = true
				this.$nextTick(() => {
					this.$refs.remarkCompon.init(this.data)
				})
      },
      copyOrder:function(){
        var _this = this;
        var clipboard = new Clipboard('#copy_text');
        clipboard.on('success',e => {
          Message({
            message:'订单号复制成功',
            type:'success'
          })
          //释放内存
          clipboard.destroy()
        })
        clipboard.on('error',e => {
          Message({
            message:'该浏览器不支持自动复制',
            type:'warning'
          });
          clipboard.destroy()
        })
      },
			// copyOrder() {
			// 	var clipboard = new Clipboard('.btn')
			// 	let that = this
			// 	clipboard.on('success', e => {
			// 		that.$message({
			// 			message: '订单号复制成功',
			// 			type: 'success',
			// 			duration: 1000
      //     })
      //     console.log(323242,clipboard)
			// 		clipboard.destroy()
			// 	})

			// 	clipboard.on('error', e => {
			// 		that.$message({
			// 			message: '订单号复制失败，请重新复制',
			// 			type: 'error',
			// 			duration: 1000
			// 		})
			// 		clipboard.destroy()
			// 	})
			// },
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
<style lang="scss">
  .orderDetClass {

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
      border: 1px solid #ECEDF1;
      background-color: #FDFAFE;
    }

    .orderDe {
        /* border: 1px solid #333; */
        height: 85px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 30px;
        padding-right: 30px;
        font-size:18px;
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
      max-width: 120px;
      min-width: 75px;
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
        font-size: 18px;
        font-weight: 700;
        margin-bottom: 8px;
    }

    .orderDe {
        /* border: 1px solid #333; */
        height: 85px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 30px;
        padding-right: 30px;
        font-size:18px;
    }

    .orderDelf,
    .orderDerg {
      /* display: flex; */
      display: flex;
      align-items: center;
    }

    .orderDelf div {
      margin-left: 20px;
    }

    .orderDerg div {
      margin-right: 20px;
    }

    .buyerInfo {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      box-sizing: border-box;
      height: 110px;
      /* margin: 5px 10px; */
      border-top: 1px solid #ECEDF1;
      display: flex;
      padding-left: 30px;
      padding-right: 30px;
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
      width: 100%;
      height: 150px;
      display: flex;
      margin-top: 10px;
      border: 1px solid #ECEDF1;
      background-color: #FDFAFE;
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
      background-color: #FDFAFE;
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
      background-color: #FDFAFE;
      padding-left: 20px;
      width: 100%;
      display: flex;
      padding-top: 20px;
      font-size: 18px;

      ul {
        margin-top: -4px;
        padding-left: 0;

        li {
          list-style: none;
          width: 200px;
          line-height: 30px;
        }
      }
    }

    .operationRecord {
      width: 100%;
      padding-left: 20px;
      margin: 50px 0;
      height: auto;
      // border: 1px solid #ECEDF1;
    }

    .operationRecord p {
      line-height: 30px;
    }

    .el-table {
      margin-top: 20px;
    }

    /*/deep/ th {*/
    /*  color: #2260D2 !important;*/
    /*  background-color: #D2E0F7 !important;*/
    /*}*/

    .el-step__head.is-success {
      color: rgba(51, 51, 51, 1) !important;
      border-color: #D6D3D7 !important;

      .el-step__icon {
        background: #D7E2F7 !important;
      }

      .el-icon-check:before {
        color: #2062D2;
      }
    }

    .el-step__main {
      .is-success {
        font-size: 18px !important;
        color: rgba(51, 51, 51, 1) !important;

      }
    }
    .weightedFont{
        font-weight:600;
        color:rgba(0,0,0,1);

    }
    .orderNumber{
      display:block;
      white-space:nowrap;
      overflow:hidden;
      // text-overflow:ellipsis;
      border: none;
      max-width: 220px;
    }
}
</style>
