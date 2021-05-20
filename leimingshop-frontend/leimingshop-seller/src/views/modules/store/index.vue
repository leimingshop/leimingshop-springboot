<template>
  <div>
    <el-breadcrumb separator="/">
        <el-breadcrumb-item >店铺管理</el-breadcrumb-item>
        <el-breadcrumb-item>店铺设置</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form :inline="true" class="topGapPadding" :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="" label-width="102px">
      <div class="formMode">
        <div style="margin-bottom:20px">商家信息</div>
        <div class="itemMode">
          <el-form-item label="商家Id：">
              <div style="width:250px">{{dataForm.storeId}}</div>
          </el-form-item>
          <el-form-item label="商家名称：" prop="storeName">
              <el-input v-model="dataForm.storeName" maxlength="20" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="logo：" prop="storeLogo">
              <div style="width:250px">
                <img-cropper
                    ref="storeLogo"
                    :index="'storeLogo'"
                    :imgWidth='"60px"'
                    :imgHeight='"60px"'
                    @GiftUrlHandle="GiftUrlHandle"
                ></img-cropper>
              </div>
          </el-form-item>
        </div>
      </div>
      <div class="formMode">
        <div style="margin-bottom:20px">公司信息</div>
        <div class="itemMode">
          <el-form-item label="公司名称：">
              <el-input v-model="dataForm.companyName" maxlength="50" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="法人姓名：">
              <el-input v-model="dataForm.legalPersonName" maxlength="10" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="办公电话：">
              <el-input v-model="dataForm.companyPhone" maxlength="11" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="注册地址：">
              <el-input v-model="dataForm.companyAddress" maxlength="50" show-word-limit placeholder="与注册证书地址一致" clearable></el-input>
          </el-form-item>
          <el-form-item label="办公地址：">
              <el-input v-model="dataForm.companyAddressDetail" maxlength="50" show-word-limit placeholder="可以收发信件的有效地址" clearable></el-input>
          </el-form-item>
          <el-form-item label="注册资金：" prop="companyRegisteredCapital">
              <el-input v-model="dataForm.companyRegisteredCapital " type="text" placeholder="" clearable>
                <template slot="append">万元</template>
              </el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
            <el-form-item label="公司注册证书：">
                <div style="width:250px">
                    <img-cropper
                        ref="cropperImg2"
                        :index="'cropperImg2'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        @GiftUrlHandle="GiftUrlHandle">
                    </img-cropper>
                </div>
            </el-form-item>
            <el-form-item label="税号：">
                <el-input v-model="dataForm.taxpayerId" maxlength="50" show-word-limit placeholder="" clearable></el-input>
            </el-form-item>
            <!-- <el-form-item label="税号：">
                <el-input v-model="dataForm.taxpayerId" placeholder="" clearable></el-input>
            </el-form-item> -->
            <div style="width:392px"></div>
        </div>
        <div class="itemMode">
            <el-form-item label="公司简介：">
                <el-input type="textarea" v-model="dataForm.storeIntro" maxlength="50" placeholder="" clearable></el-input>
            </el-form-item>
        </div>
      </div>
      <div class="formMode">
        <div style="margin-bottom:20px">结算信息</div>
        <div class="itemMode">
          <el-form-item label="银行开户名称：">
              <el-input v-model="dataForm.bankAccountName " maxlength="20" show-word-limit placeholder="开户行账号" clearable></el-input>
          </el-form-item>
          <el-form-item label="开户行名称：">
              <el-input v-model="dataForm.bankName " maxlength="20" show-word-limit placeholder="开户行账号" clearable></el-input>
          </el-form-item>
          <el-form-item label="开户行账号：">
              <el-input v-model="dataForm.bankAccountNumber" maxlength="20" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="支付宝姓名：">
              <el-input v-model="dataForm.alipayName " maxlength="10" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="支付宝账号：">
              <el-input v-model="dataForm.alipayAccountNumber" maxlength="20" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <div style="width:392px"></div>
        </div>
        <div class="itemMode">
          <el-form-item label="微信姓名：">
              <el-input v-model="dataForm.wechatName " maxlength="10" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="微信账号：">
              <el-input v-model="dataForm.wechatAccountNumber" maxlength="20" show-word-limit placeholder="" clearable></el-input>
          </el-form-item>
          <div style="width:392px"></div>
        </div>
      </div>
    </el-form>
    <div style="display: flex;justify-content: center;margin-top:20px">
      <el-button @click="getStoreNews">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="submitStore"  v-if="$hasPermission('sys:setting:echo')">{{ $t('confirm') }}</el-button>
    </div>

  </div>
</template>

<script>
import imgCropper from "@/components/model-photo-cropper";
import { uploadPicBase64,storeNews,updateStore } from '@/api/api'
import { isMobile,isPhone } from '@/utils/validate'
import { isPrice } from '@/utils/validate'

export default {
  data () {
      var validateGtZero = (rule, value, callback) => {
          if (value <0 || value==0) {
              return callback(new Error('注册资金须大于0'))
          }
          callback()
      }
    return {
      dataForm: {
          storeIntro:'',
        alipayAccountNumber: "",
        alipayName: "",
        bankAccountName: "",
        bankAccountNumber: "",
        "bankName": "",
        "companyAddress": "",
        "companyAddressDetail": "",
        "companyAreaId": '',
        "companyCityId": '',
        "companyName": "",
        "companyPhone": "",
        "companyProvinceId": '',
        "companyRegisteredCapital": '',
        "electronicBusinessLicense": "",
        "id": '',
        "legalPersonName": "",
        "storeId": '',
        "storeLogo": "",
        "storeName": "",
        "taxpayerId": "",
        "wechatAccountNumber": "",
        "wechatName": ""
      },
      uploading:false,
      dataRule:{
          storeName: [
            { required: true, message: '商家名称不能为空！', trigger: 'blur' }
          ],
          storeLogo: [
            { required: true, message: '商家logo不能为空！', trigger: 'blur' }
          ] ,
          companyRegisteredCapital: [
            { required: false, trigger: 'blur' },
              { validator: validateGtZero, trigger: 'blur' }
          ]
        }
    }
  },
  components:{
		imgCropper,
	},
    watch:{
      'dataForm.taxpayerId':function (newV,oldV) {
          for(let i=0;i<newV.length;i++){
              // 删除输入的汉字
              if(/^[\u4e00-\u9fa5]*$/.test(newV[i])){
                  this.dataForm.taxpayerId = newV.replace(newV[i],"")
              }
          }
      },
        'dataForm.bankAccountNumber':function (newV,oldV) {
          for(let i=0;i<newV.length;i++){
              // 删除输入的汉字
              if(/^[\u4e00-\u9fa5]*$/.test(newV[i])){
                  this.dataForm.bankAccountNumber = newV.replace(newV[i],"")
              }
          }
      },
        'dataForm.alipayAccountNumber':function (newV,oldV) {
          for(let i=0;i<newV.length;i++){
              // 删除输入的汉字
              if(/^[\u4e00-\u9fa5]*$/.test(newV[i])){
                  this.dataForm.alipayAccountNumber = newV.replace(newV[i],"")
              }
          }
      },
        'dataForm.wechatAccountNumber':function (newV,oldV) {
          for(let i=0;i<newV.length;i++){
              // 删除输入的汉字
              if(/^[\u4e00-\u9fa5]*$/.test(newV[i])){
                  this.dataForm.wechatAccountNumber = newV.replace(newV[i],"")
              }
          }
      },
    },
  created(){
      this.getStoreNews();
  },
  methods: {
        getStoreNews(){
            storeNews().then(res=>{
                if(res.data&&res.code == 200){
                    console.log('回显信息',res.data.storeName)
                    Object.assign(this.dataForm,res.data);
                    if(res.data.storeLogo){
                        this.$nextTick(()=>{
                            this.$refs.storeLogo.cropper.imgShow  = true
                            this.$refs.storeLogo.cropper.cropImg  = this.$imgDomain + res.data.storeLogo;
                        })
                    }
                    if(res.data.electronicBusinessLicense){
                        this.$nextTick(()=>{
                            this.$refs.cropperImg2.cropper.imgShow  = true
                            this.$refs.cropperImg2.cropper.cropImg  = this.$imgDomain + res.data.electronicBusinessLicense;
                        })
                    }
                    // this.dataForm.storeName = res.data.storeName
                }
            })
        },

        submitStore(){
          if(!isMobile(this.dataForm.companyPhone)&&this.dataForm.companyPhone){
                this.$message({
                    message:'电话格式不正确',
                    type: 'error',
                    duration: 1500,
                })
                return
            }
              // 注册资金
              if(!isPrice(this.dataForm.companyRegisteredCapital)&&this.dataForm.companyRegisteredCapital){
                this.$message({
                    message:'注册资金只能输入6位正整数',
                    type: 'error',
                    duration: 1500,
                })
                return
            }
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    updateStore(this.dataForm).then(res=>{
                        console.log('666666',res)
                        if(res.code == 200){
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                    })
                }


            })
        },
        GiftUrlHandle(val,index){
            this.uploadPic(val,index);
        },
        uploadPic(base64,index){
            let that = this;
            const params = { "imgStr": base64 };
             params.type=1
            return new Promise(function(resolve){
                uploadPicBase64(params).then(res =>{
                    console.log('21212121212',res)
                    that.uploading = true;
                    if(res && res.code == "200"){
                        var url = res.data.url
                        // if(!/http/.test(url)){
                        //     url = "http://192.168.1.108:8888/"+url;
                        // }
                        if(index == 'storeLogo'){
                            that.dataForm.storeLogo = url;
                        }else{
                            that.dataForm.electronicBusinessLicense = url;
                        }
                        resolve("true")
                    }else {
                        that.$message({
                            message:res.msg,
                            type: 'error',
                            duration: 1500,
                        })
                        resolve("false")
                    }
                })
            });
        },
  }
};
</script>
<style lang="scss">
  .formMode{
    padding-top: 30px;
    border-bottom: 20px solid #ECEDF1;
    .el-input{
      width: 250px !important;
    }
    .el-textarea__inner{
        width: 250px !important;
    }
    &:first-child{
      padding-top: 0;
    }
    &:last-child{
      border: none;
    }
    .itemMode{
      padding-left: 20px;
      display: flex;
      justify-content: space-between;
    }
  }
</style>
