<template>
  <div>
    <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
    <div class="top-title">开票</div>

    <!--    <div>-->
    <!--      <el-button size="small" @click="init(rowData)" type="primary">刷新</el-button>-->
    <!--    </div>-->

    <div style="margin-left: 120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="area-box">
            <div class="fullTitle title-1">订单信息</div>
            <div class="grid-content bg-purple">
              <div class="label-box">会员名称：</div>
              <div class="text-box">{{dataForm.memberName}}</div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">订单编号：</div>
              <div class="text-box">{{dataForm.orderSn}}
                <i  v-if="dataForm.orderSn"   :class="['el-icon-document','orderId1']" @click="copyText(dataForm.orderSn,'.orderId1')"></i></div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">支付金额：</div>
              <div class="text-box">{{dataForm.payAmount}}
                <i  v-if="dataForm.payAmount" :class="['el-icon-document','payAmount1']" @click="copyText(dataForm.payAmount,'.payAmount1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">下单时间：</div>
              <div class="text-box">{{dataForm.createOrderDate}}
                <i  v-if="dataForm.createOrderDate" :class="['el-icon-document','createOrderDate1']" @click="copyText(dataForm.createOrderDate,'.createOrderDate1')"></i>
              </div>
            </div>
          </div>
          <div class="area-box">
            <div class="fullTitle title-1">收件信息</div>
            <div class="grid-content bg-purple" v-if="dataForm.personalName && dataForm.personalName!=''" >
              <div class="label-box">收件人：</div>
              <div class="text-box">{{dataForm.personalName}}</div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">用户手机：</div>
              <div class="text-box">{{dataForm.addresseePhone}}
                <i v-if="dataForm.addresseePhone" :class="['el-icon-document','addresseePhone1']" @click="copyText(dataForm.addresseePhone,'.addresseePhone1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple" v-if="dataForm.memberInvoiceType==1">
              <div class="label-box">用户邮箱：</div>
              <div class="text-box">{{dataForm.addresseeMail}}
                <i  v-if="dataForm.addresseeMail" :class="['el-icon-document','addresseeMail1']" @click="copyText(dataForm.addresseeMail,'.addresseeMail1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple" v-if="dataForm.memberInvoiceType!=null && dataForm.memberInvoiceType!=1">
              <div class="label-box">用户地址：</div>
              <div class="text-box">{{dataForm.addresseeAddress}}
                <i  v-if="dataForm.addresseeAddress" :class="['el-icon-document','detailedAddress1']" @click="copyText(dataForm.addresseeAddress,'.detailedAddress1')"></i>
              </div>
            </div>
             <!-- <div class="grid-content bg-purple" v-if="dataForm.memberInvoiceType!=null && dataForm.memberInvoiceType!=1">
              <div class="label-box">用户详细地址：</div>
              <div class="text-box">{{dataForm.detailedAddress}}
                <i  v-if="dataForm.detailedAddress" :class="['el-icon-document','detailedAddress1']" @click="copyText(dataForm.detailedAddress,'.detailedAddress1')"></i>
              </div>
            </div> -->
          </div>
        </el-col>
        <el-col :span="12">
          <div class="area-box">
            <div class="fullTitle title-1">开票信息</div>
            <div class="grid-content bg-purple">
              <div class="label-box">发票类型：</div>
              <div class="text-box" v-if="dataForm.memberInvoiceType==1">电子发票</div>
              <div class="text-box" v-if="dataForm.memberInvoiceType==2">纸质发票</div>
              <div class="text-box" v-if="dataForm.memberInvoiceType==3">增值税专项发票</div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">发票内容：</div>
              <div class="text-box" v-if="dataForm.memberInvoiceContent==1">商品明细</div>
              <div class="text-box" v-if="dataForm.memberInvoiceContent==2">商品类别</div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">抬头类型：</div>
              <div class="text-box" v-if="dataForm.companyType==0">不开票</div>
              <div class="text-box" v-if="dataForm.companyType==1">个人</div>
              <div class="text-box" v-if="dataForm.companyType==2">单位</div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">单位名称：</div>
              <div class="text-box"  :title="dataForm.company">{{companySub}} 
                <i  v-if="dataForm.company" :class="['el-icon-document','company1']" @click="copyText(dataForm.company,'.company1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">公司税号：</div>
              <div class="text-box">{{dataForm.dutyParagraph}}
                <i  v-if="dataForm.dutyParagraph" :class="['el-icon-document','dutyParagraph1']" @click="copyText(dataForm.dutyParagraph,'.dutyParagraph1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">注册地址：</div>
              <div class="text-box">{{dataForm.registeredAddress}}
                <i  v-if="dataForm.registeredAddress" :class="['el-icon-document','registeredAddress1']" @click="copyText(dataForm.registeredAddress,'.registeredAddress1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">注册电话：</div>
              <div class="text-box">{{dataForm.officePhone}}
                <i  v-if="dataForm.officePhone" :class="['el-icon-document','officePhone1']" @click="copyText(dataForm.officePhone,'.officePhone1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">开户银行：</div>
              <div class="text-box">{{dataForm.bankName}}
                <i  v-if="dataForm.bankName" :class="['el-icon-document','bankName1']" @click="copyText(dataForm.bankName,'.bankName1')"></i>
              </div>
            </div>
            <div class="grid-content bg-purple">
              <div class="label-box">银行账号：</div>
              <div class="text-box">{{dataForm.bankNumber}}
                <i  v-if="dataForm.bankNumber" :class="['el-icon-document','bankNumber1']" @click="copyText(dataForm.bankNumber,'.bankNumber1')"></i>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <div class="table-box">
        <div class="fullTitle title-1">订单商品</div>
        <el-table
            width="95%"
            :data="dataForm.invoiceInfoDTOS"
            border
            v-loading="dataListLoading"
            style="width: 95%;margin-left:42px;margin-top:10px">
          <el-table-column label="序号" width="70" align="center">
            <template slot-scope="scope">
              {{scope.$index+1}}
            </template>
          </el-table-column>
          <el-table-column prop="goodsId" label="商品编号" align="center" width="170"></el-table-column>
          <el-table-column prop="goodsName" label="商品名称" align="left">
            <template slot-scope="scope">
              <div :id="'goodsName' + scope.$index ">
                {{scope.row.goodsName}}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="specInfo" label="规格型号" align="center">
            <template slot-scope="scope">
              <div>
                {{scope.row.specInfo}}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="goodsNum" label="商品数量" align="center" width="100"></el-table-column>
          <el-table-column prop="gcName" label="商品单价（元）" width="130" align="center">
            <template slot-scope="scope">
              <div>
                ￥{{scope.row.specPayPrice}}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="orderAmount" label="金额（元）" align="center" width="130">
            <template slot-scope="scope">
              <div :id="'price' + scope.$index ">
                ￥{{scope.row.specTotalPrice}}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="scope">
              <el-button @click="copyText(scope.row.goodsName,'.copygoodsName'+ scope.$index)"
                         :class="'copygoodsName'+ scope.$index"
                         type="text" size="mini">复制商品名称</el-button>
              <el-button @click="copyText(scope.row.specTotalPrice,'.copyprice'+ scope.$index)"
                         :class="'copyprice'+ scope.$index"
                         type="text" size="mini">复制金额</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-if="orderStatus==0&&(dataForm.invoiceEvolve == 1 || dataForm.invoiceEvolve == 3)"  style="text-align: center;">
          <el-button class="btn-item" type="primary" plain @click="changePage()" >返回</el-button>
      </div>

      <div v-else>
      <div class="fullTitle title-1">发票工具</div>
      <div v-if="dataForm.invoiceEvolve == 1 || dataForm.invoiceEvolve == 3">
        <el-form :inline="true" :model="dataFormTemp" class="form-box" :rules="dataRule" ref="dataFormTemp"
                 v-loading="couponLoading">
          <el-form-item label="开票类型：" prop="storeInvoiceType">
            <el-radio-group v-model="dataFormTemp.storeInvoiceType" @change="updateContent(dataFormTemp.storeInvoiceType)">
              <el-radio label="1">电子普通发票</el-radio>
              <el-radio label="2" >纸质普通发票</el-radio>
              <el-radio label="3" >增值税专用发票</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="开票内容：" prop="storeInvoiceContent">
            <el-radio-group v-model="dataFormTemp.storeInvoiceContent">
              <el-radio label="1" :disabled='contentDisabled'>商品明细</el-radio>
              <el-radio label="2" :disabled='contentDisabled'>商品类别</el-radio>
            </el-radio-group>
          </el-form-item>

          <div v-if="dataFormTemp.storeInvoiceType == 1">
            <el-form-item label="上传发票：" class="specSize" prop="fileUrl">
              <el-upload
                  class="upload-demo"
                  action=""
                  :http-request="httpRequest"
                  :on-remove="handleRemove"
                  :before-upload="beforeUpload"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :file-list="fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip ">只能上传 jpg、png、gif、pdf 格式文件</div>
              </el-upload>
            </el-form-item>
          </div>
          <div v-else-if="dataFormTemp.storeInvoiceType == 2 || dataFormTemp.storeInvoiceType == 3">
            <el-form-item label="发票代码："   prop="invoiceCode">
              <el-input v-model="dataFormTemp.invoiceCode" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="发票号码："  prop="invoiceNumber">
              <el-input v-model="dataFormTemp.invoiceNumber" maxlength="20" ></el-input>
            </el-form-item>
            <el-form-item label="物流商："   prop="logisticsCompanies">
              <!-- <el-input v-model="dataFormTemp.logisticsCompanies"></el-input> -->
              <el-select v-model="dataFormTemp.logisticsCompanies" placeholder="请选择物流公司" >
                <el-option
                  :label="item.companyName"
                  :value="item.companyName"
                  v-for="(item,index) in logList"
                  :key="index"
                >{{item.companyName}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="快递单号："   prop="logisticsNumber">
              <el-input v-model="dataFormTemp.logisticsNumber" maxlength="20"></el-input>
            </el-form-item>
          </div>
          <el-form-item>
       
          </el-form-item>
               <div  style="text-align: center;">
              <el-button class="btn-item" type="primary" @click="dataFormSubmit('dataFormTemp')" :loading="saveLoading">
                {{saveLoading?"提交中...":"确认"}}
              </el-button>
              <el-button class="btn-item" type="primary" plain @click="changePage()" >取消</el-button>
            </div>
        </el-form>
      </div>
      <div v-else-if="dataForm.storeInvoiceType == 1">
        <div class="area-box">
          <div class="grid-content bg-purple">
            <div class="label-box">发票类型：</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==1">电子发票</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==2">纸质发票</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==3">增值税专项发票</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">发票内容：</div>
            <div class="text-box" v-if="dataForm.storeInvoiceContent==1">商品明细</div>
            <div class="text-box" v-if="dataForm.storeInvoiceContent==2">商品类别</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">上传发票：</div>
            <div class="text-box">
              <a :href="dataForm.fileUrl | filterImgUrl" :download="getFileName()" target="_blank">{{"电子发票"}}</a>
            </div>
          </div>
        </div>
        <div style="text-align: center;">
          <el-button class="btn-item" type="primary" plain @click="changePage()" >返回</el-button>
          <el-button class="btn-item" type="primary" @click="sendMail" >发送邮件</el-button>
        </div>
      </div>
      <div v-else>
        <div class="area-box">
          <div class="grid-content bg-purple">
            <div class="label-box">发票类型：</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==1">电子发票</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==2">纸质发票</div>
            <div class="text-box" v-if="dataForm.storeInvoiceType==3">增值税专项发票</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">发票内容：</div>
            <div class="text-box" v-if="dataForm.storeInvoiceContent==1">商品明细</div>
            <div class="text-box" v-if="dataForm.storeInvoiceContent==2">商品类别</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">发票代码：</div>
            <div class="text-box">{{dataForm.invoiceCode}}</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">发票号码：</div>
            <div class="text-box">{{dataForm.invoiceNumber}}</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">物流商：</div>
            <div class="text-box">{{dataForm.logisticsCompanies}}</div>
          </div>
          <div class="grid-content bg-purple">
            <div class="label-box">快递单号：</div>
            <div class="text-box">{{dataForm.logisticsNumber}} </div>
          </div>
        </div>
        <div  style="text-align: center;">
          <el-button class="btn-item" type="primary" plain @click="changePage()">返回</el-button>
        </div>
      </div>
      </div>

    </div>
  </div>

</template>

<script>
	import Bread from "@/components/bread";
	import {fileInvoiceUpload} from '@/api/url'
	import {getinvoiceDetail,getinvoiceinfo, putMakeInvoice, fileInvoiceUploadApi,sendEmail,logCompany} from '@/api/api'
	import Clipboard from 'clipboard'

	export default {
		name: "details",
		data() {
			return {
        contentDisabled:false,
        vuexImgUrl:this.$store.state.imgUrl,
				breaddata: ["财务管理", "发票管理", "开票"],
        fileUploadUrl: fileInvoiceUpload,
        buttonType:2,
        orderStatus:0,
        companySub:'',
				dataForm: {
          id:'',
          orderId:'',
          orderSn:'',
          memberName:'',
          storeId:'',
          payAmount:'',
          createOrderDate:'',
          applyDate:'',
          companyType:'',
          memberInvoiceType:'',
          memberInvoiceContent:'',
          storeInvoiceType:'',
          storeInvoiceContent:'',
          personalName:'',
          addresseeName:'',
          addresseePhone:'',
          addresseeMail:'',
          addresseeAddress:'',
          detailedAddress:'',
          provinceId:'',
          cityId:'',
          districtId:'',
          streetId:'',
          company:'',
          dutyParagraph:'',
          registeredAddress:'',
          officePhone:'',
          bankName:'',
          bankNumber:'',
          invoiceEvolve:'',
          orderStatus:'',
          changeFlag:'',
          invoiceCode:'',
          invoiceNumber:'',
          invoiceDate:'',
          logisticsCompanies:'',
          logisticsNumber:'',
          fileUrl:'',
          invoiceInfoDTOS:[],
        },
        orderList: [],
        logList:"",
				dataFormTemp: {
					id: '',
					storeInvoiceType: '1',
					storeInvoiceContent: '',
					invoiceCode: '',
					invoiceNumber: '',
					logisticsCompanies: '',
					logisticsNumber: '',
					fileUrl: '',

				},
				fileList: [],
				cancleOrderVisible: false,
				loading: true,
				dataListLoading: false,
				listItem: {},
				groupRecordId: '',
        type:-1,
				// 提交
				saveLoading: false,
				couponLoading: false,
				rowData: {},
				// 校验规则
				dataRule: {
					storeInvoiceType: [
						{required: true, message: '必填项不能为空', trigger: 'change'},
					],
					storeInvoiceContent: [
						{required: true, message: '必填项不能为空', trigger: 'change'},
					],
					fileUrl: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
					],
					logisticsCompanies: [
						{required: true, message: '必填项不能为空', trigger: 'change'},
						// {validator: validTimechange, trigger: 'blur'},
					],
					logisticsNumber: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
					],
				},
			}
		},
		watch: {
			"dataFormTemp.storeInvoiceType"(newValue, oldValue) {
				this.$refs['dataFormTemp'].clearValidate();
				this.dataFormTemp.invoiceCode = '';
				this.dataFormTemp.invoiceNumber = '';
				this.dataFormTemp.logisticsCompanies = '';
				this.dataFormTemp.logisticsNumber = '';
				this.fileList = [];
				this.dataFormTemp.fileUrl = '';
			}
		},
		components: {
			Bread,
		},
		methods: {
			 init(row,type) {
         var row=row||this.$route.query.row;
        //  if(type==1){
          //  var row=row
        //  }else{
        //    var row=this.$route.query.row
        //  }
        console.log(row,8888)
         this.buttonType = type;
        // console.log(row)
        this.orderStatus=row.orderStatus;
				let param = {
					id: row.id,
				}
        getinvoiceDetail(param).then((res)=>{
          if(res.code==200){
            this.dataForm = res.data;
            if(res.data.company&&res.data.company.length>12){
              this.companySub = res.data.company.substr(0,12)+"..."
              if(orderStatus==0&&(dataForm.invoiceEvolve == 1 || dataForm.invoiceEvolve == 3)){
                
              }
            }else{
              this.companySub = res.data.company
            }
          }
          
        })
      },
      //获取物流公司
      getLogCompany() {
        console.log("进来了")
        logCompany().then(res => {
          if (res.code == 200) {
            this.logList = res.data;
          } else {
            console.log("获取物流公司失败");
          }
        });
      },
      updateContent(flag){
					if(flag=="3"){
						this.dataFormTemp.storeInvoiceContent="1";
            this.contentDisabled=true
            this.getLogCompany()
				  }else{
            this.contentDisabled=false
          }
          if(flag == "2"){
            this.getLogCompany()
          }

			},
      getFileName(){
          let arr = this.dataForm.fileUrl.split("\/")
          if(arr.length>0){
            return arr[arr.length-1];
          }else{
            return "下载出错"
          }
         
      },
			copyText(data,className) {
				let clipboard = new Clipboard(className, {
					text: function () {
						return data
					}
				})
				clipboard.on('success', e => {
					this.$message({
						message: '复制成功',
						type: 'success',
						duration: 1000
					})
					clipboard.destroy()
				})

				clipboard.on('error', e => {
					this.$message({
						message: '复制失败，请重新复制',
						type: 'error',
						duration: 1000
					})
					clipboard.destroy()
				})
			},
			dataFormSubmit(dataFormTemp) {
				if (this.dataFormTemp.storeInvoiceType == 1) {
          this.dataFormTemp.id=this.dataForm.id;
					this.dataFormTemp.invoiceCode = '';
					this.dataFormTemp.invoiceNumber = '';
					this.dataFormTemp.logisticsCompanies = '';
					this.dataFormTemp.logisticsNumber = '';
				} else {
          this.dataFormTemp.id=this.dataForm.id;
					this.fileList = [];
					this.dataFormTemp.fileUrl = '';
				}
				console.log(this.dataFormTemp)
				this.$refs[dataFormTemp].validate((valid) => {
					let param = Object.assign({}, this.dataFormTemp)
					if (valid) {
						putMakeInvoice(param).then((res => {
							this.saveLoading = false;
							let status = null;
							if (res.code == "200") {
								status = "success";
                // this.changePage()
			          	this.$emit('changePage')
                
							} else {
								status = "error";
							}
							this.$message({
								message: res.msg,
								type: status,
								duration: 1500
							})
						}))
					} else {
						return false;
					}
				})
			},

			handleRemove(file, fileList) {
				this.dataFormTemp.fileUrl = ''
			},
			handleExceed(files, fileList) {
				this.$message.warning(`当前限制选择上传${files.length} 个文件`);
			},
			beforeUpload(file) {
        let isUpload = false
        const isLt5M = file.size / 1024 / 1024 > 5;
        if (isLt5M) {
          this.$message.error('图片大小不能超过 5MB!');
			    return isUpload;
			  } 
				console.log(file.type)
				if (file.type == 'image/jpeg' || file.type == 'image/png' || file.type == 'application/pdf' || file.type == 'image/gif') {
					isUpload = true;
				} else {
					this.$message({
						message: '只能上传jpg/png/gif/pdf格式的文件',
						type: 'error',
						duration: 1500,
					})
				}
				return isUpload
			},
			httpRequest(options) {
				let config = {
					headers: {"Content-Type": "multipart/form-data"},
				};
				let param = new FormData();
				param.append("file", options.file);
				this.$http.post(this.fileUploadUrl, param, config).then(({data: res}) => {
					if (res.code == 200) {
						this.dataFormTemp.fileUrl = res.data.url;
						this.$message({
							message: res.msg,
							type: 'success',
							duration: 1500,
						})
					} else {
						this.$message({
							message: res.msg,
							type: 'error',
							duration: 1500,
						})
          }
				})
			},
			viewDetails() {

			},
			remarks(row) {

      },
      sendMail() {
        this.$prompt('请输入邮箱', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
          inputErrorMessage: '邮箱格式不正确'
        }).then(({ value }) => {
          var obj = {
                  email:value,
                  id:this.dataForm.id
          }
          this.sendEmailLoaing = true;
          sendEmail(obj).then((res)=>{
              // this.sendEmailLoaing =false;
              // this.showToast(res.msg)
              if(res.code==200){
                  this.$message({
                    type: 'success',
                    message: res.msg
                  });
                  // setTimeout(()=>{ this.closePopup()},1000)
              }else{
                this.$message({
                  type: 'error',
                  message: '发送失败，请稍后重试！'
                });    
              }
          })
        });
      },
			changePage() {
				this.$emit('changePage')
			}
		}
	}
</script>

<style lang="scss" scoped>

  // .text-box{
  //   text-overflow: ellipsis;
  //   overflow: hidden;
  //   white-space: nowrap;
  //   width: 240px;
  // }
  // .company1{
    
  // }
  .el-icon-document{
    font-size: 18px;
    cursor: pointer;
    color: deepskyblue;
  }

  .el-upload__tip {
    font-size: 14px;
  }

  .table-box {
    margin: 30px 0;
  }

  .top-title {
    font-weight: 700;
    font-size: 18px;
    height: 40px;
    line-height: 40px;
  }

  .bg-purple {
    display: flex;
    line-height: 40px;
    height: 40px;

    .label-box {
      width: 120px;
      text-align: right;
    }
  }

  .fullTitle {
    font-weight: 700;
    height: 40px;
    line-height: 40px;
  }

  .btn-box {
    margin-top: 30px;
    /*text-align: center;*/
    .btn-item {
      margin: 30px 50px 30px 150px
    }

    .btn-item:nth-child(2) {
      margin: 30px 0;
    }
  }


  .form-box {
    .fullTitle {
      font-weight: 700;
      height: 40px;
      line-height: 40px;
    }

    .hrTips {
      color: #999999;
      margin-left: 150px;
      margin-bottom: 10px;
    }

    .el-form-item {
      display: block;
    }

    /deep/
    .el-checkbox-group {
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

    /deep/
    .el-input {
      width: 400px !important;
    }

    .specSize {
      .el-input {
        width: 200px !important;
      }

      /deep/
      .el-form-item__error {

      }

      /deep/
      .upload-demo {
        display: inline-block;
      }

      .el-form-item__content > div:nth-child(1) {

      }
    }
  }


  // 操作
  .btnWrap {
    display: flex;
    justify-content: space-around;
    align-items: center;

    .editWrap {
      cursor: pointer;
    }

    .skuWrap {
      cursor: pointer;
    }

    .btsvg {
      margin-right: 3px;
    }
  }
</style>
