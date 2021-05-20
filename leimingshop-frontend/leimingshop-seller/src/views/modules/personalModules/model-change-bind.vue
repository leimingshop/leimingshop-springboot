<template>
		<el-dialog
			:visible.sync="visible"
			:title='title'
			:append-to-body="true"
			:before-close='close'
			:close-on-click-modal="false"
  			:close-on-press-escape="false"
            width="30%"
		>
            <!-- 第一步 -->
            <div v-if="step==1" class="radioPhoneEmailWarp">
                <el-form
                        :model="dataForm"
                        :rules="dataRule"
                        ref="dataForm1"
                        label-width="130px">
                        <el-form-item label="选择验证方式：" prop="radio">
                            <el-radio-group v-model="radio">
                                <el-radio :label="1">手机</el-radio>
                                <el-radio :label="2">邮箱</el-radio>
                            </el-radio-group>
                        </el-form-item>

                </el-form>
                <div class="bottomBts">
                    <el-button @click="close()">取消</el-button>
                    <el-button type="primary" @click="firstStep">下一步</el-button>
                </div>
            </div>

            <!-- 第二步 -->
            <div  v-if="step==2">
                 <el-form
                    :model="dataForm"
                    :rules="dataRule"
                    ref="dataForm2"
                    label-width="130px">
                    <el-form-item label="当前手机：" v-if="radio==1">
                        <span>{{this.dataForm.encryptionPhone}}</span>
                    </el-form-item>

                     <el-form-item label="当前邮箱：" v-if="radio==2">
                         <span>{{this.dataForm.encryptionEmail}}</span>
                     </el-form-item>

                    <el-form-item label="图形验证码：" prop="captcha">
                         <div style="display:flex">
                            <el-input v-model="dataForm.captcha"   class="imgCodeInputClass" placeholder="请输入"></el-input>
                            <img :src="imgCode" class="imgCodeClass" @click="getImgCode" >
                        </div>
                    </el-form-item>

                    <el-form-item v-if="radio==1" label="手机验证码：" prop="phoneCaptcha">
                         <div style="display:flex">
                            <el-input v-model="dataForm.phoneCaptcha"   class="imgCodeInputClass" placeholder="请输入"></el-input>
                            <!-- 获取验证码 -->
                            <el-button class="getCode"
                                type="primary"
                                size="mini"
                                @click="getVerifyCode()"
                                :loading="sendVerifyCodeLoading"
                                :disabled="msgBtn.waitingCode">
                                {{msgBtn.text}}
                            </el-button>
                        </div>
                    </el-form-item>

                    <el-form-item v-if="radio==2" label="邮箱验证码：" prop="emailCaptcha">
                         <div style="display:flex">
                            <el-input v-model="dataForm.emailCaptcha"   class="imgCodeInputClass" placeholder="请输入"></el-input>
                            <!-- 获取验证码 -->
                            <el-button class="getCode"
                                type="primary"
                                size="mini"
                                @click="getVerifyCode()"
                                :loading="sendVerifyCodeLoading"
                                :disabled="msgBtn.waitingCode">
                                {{msgBtn.text}}
                            </el-button>
                        </div>
                    </el-form-item>

                </el-form>
                <div class="bottomBts">
                    <el-button @click="secondPreStep">上一步</el-button>
                    <el-button type="primary" @click="secondStep">下一步</el-button>
                </div>
            </div>

            <!-- 第三步 -->
            <div  v-if="step==3">
                <el-form
                    :model="dataForm"
                    :rules="dataRule"
                    ref="dataForm3"
                    label-width="130px">

                    <!-- 手机 -->
                    <div v-if="changeWay==1">
                         <el-form-item   label="手机号：" prop="newMobilePhone">
                            <el-input  maxlength="11" v-model="dataForm.newMobilePhone"    placeholder="请输入"></el-input>
                        </el-form-item>

                        <el-form-item   v-if="changeWay==1"  label="手机验证码：" prop="newPhoneCaptcha">
                            <div style="display:flex">
                                <el-input v-model="dataForm.newPhoneCaptcha"    placeholder="请输入"></el-input>
                                 <!-- 获取验证码 -->
                                <el-button class="getCode"
                                    type="primary"
                                    size="mini"
                                    @click="getVerifyCodeNew"
                                    :loading="sendVerifyCodeNewLoading"
                                    :disabled="msgBtn2.waitingCode">
                                    {{msgBtn2.text}}
                                </el-button>
                            </div>
                        </el-form-item>
                    </div>

                    <!-- 邮箱 -->
                    <div v-if="changeWay==2">
                        <el-form-item v-if="changeWay==2" label="新邮箱地址：" prop="newEmail">
                            <el-input v-model="dataForm.newEmail"    placeholder="请输入"></el-input>
                        </el-form-item>

                        <el-form-item  v-if="changeWay==2"  label="邮箱验证码：" prop="newEmailCaptcha">
                            <div style="display:flex">
                                <el-input v-model="dataForm.newEmailCaptcha"    placeholder="请输入"></el-input>
                                <!-- 获取验证码 -->
                                <el-button class="getCode"
                                    type="primary"
                                    size="mini"
                                    @click="getVerifyCodeNew"
                                    :loading="sendVerifyCodeNewLoading"
                                    :disabled="msgBtn2.waitingCode">
                                    {{msgBtn2.text}}
                                </el-button>
                            </div>
                        </el-form-item>
                    </div>

                </el-form>
                <div class="bottomBts">
                    <el-button type="primary" @click="dataFormSubmit()" :loading="saveLoading">确定</el-button>
                </div>
            </div>

		</el-dialog>
</template>
<script>
import { getallimages } from "@/api/url"
import { isEmail, isMobile } from '@/utils/validate'
import { getUUID } from '@/utils'

import { editPhone,editEmail } from "@/api/api.js"
import { phoneCaptcha,verifyPhoneCaptcha,verifyCaptcha } from "@/api/api.js"
import security from '@/utils/security.js'
export default {
    data(){
        return {
            step:1,// 第几步
			visible:false,
            title:"",
            radio:1, //  1手机验证；2邮箱验证 
            changeWay:1,// 1换绑手机；2换绑邮箱
            dataListLoading:false,
            imgCode:'',
            dataForm:{
                uuid:'',
                mobilePhone:'',
                email:'',
                // account:'',
                captcha:'',// 图形验证码
                phoneCaptcha:'', // 手机验证码
                emailCaptcha:'', //  邮箱验证码
                newMobilePhone:'',//修改后要绑定的手机
                newEmail:'', // 修改后要绑定的邮箱
                newPhoneCaptcha:'', // 手机验证码
                newEmailCaptcha:'', //  邮箱验证码

              encryptionPhone:'',
              encryptionEmail:'',
            },
            msgBtn: {
                text: "发送验证码",
                waitingCode: false,
                count: 60
            },
            msgBtn2: {
                text: "发送验证码",
                waitingCode: false,
                count: 60
            },
            sendVerifyCodeLoading:false,
            sendVerifyCodeNewLoading:false,
            saveLoading:false,
            checkPhoneOrEmain3:false,
        }
	},
	props:{

    },
    computed: {
        dataRule () {
          let that = this;
          var validateMobile = (rule, value, callback) => {
            if (!isMobile(value)) {
                  return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.mobile') })))
            }
            callback()
          }
          // 验证邮箱
          var validateEmail = (rule, value, callback) => {
            if (!isEmail(value)) {
              return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.email') })))
            }
            callback()
          }
            return {
                radio: [
                    { required: true, message: "必填项不能为空", trigger: 'blur' }
                ],
                captcha: [
                    { required: true, message: "必填项不能为空", trigger: 'blur' }
                ],
                phoneCaptcha:[
                    { required: true, message: "必填项不能为空", trigger: 'blur' }
                ],
                emailCaptcha:[
                    { required: true, message: "必填项不能为空", trigger: 'blur' }
                ],
                // 第三步手机验证
                newMobilePhone:[
                    { required: true, message: "必填项不能为空", trigger: 'blur' },
                  { validator: validateMobile, trigger: 'blur' }
                ],
                newPhoneCaptcha:[
                    { required: this.checkPhoneOrEmain3?true:false, message: "必填项不能为空", trigger: 'blur' }
                ],
                // 第三步邮箱验证
                newEmail:[
                    { required: true, message: "必填项不能为空", trigger: 'blur' },
                  { validator: validateEmail, trigger: 'blur' }
                ],
                newEmailCaptcha:[
                    { required: this.checkPhoneOrEmain3?true:false, message: "必填项不能为空", trigger: 'blur' }
                ],
            }
        }
    },
	components:{
    },
    created() {
	},
	mounted(){
    },
    methods:{
        init(obj){
			// console.log("弹框出来后会走这里");
			this.visible = true;
            this.title = obj.title
            this.changeWay = obj.changeWay //  1换绑手机；2换绑邮箱
            // this.dataForm.mobilePhone = obj.mobilePhone?obj.mobilePhone:13243169999
            this.dataForm.mobilePhone = obj.mobilePhone
            this.dataForm.email = obj.email
            this.dataForm.account = obj.account
            this.dataForm.encryptionPhone = obj.encryptionPhone
            this.dataForm.encryptionEmail = obj.encryptionEmail

            this.step=1;
            this.sendVerifyCodeLoading = false;
            this.sendVerifyCodeNewLoading = false;
            this.msgBtn={
                text: "发送验证码",
                waitingCode: false,
                count: 60
            };
            this.msgBtn2= {
                text: "发送验证码",
                waitingCode: false,
                count: 60
            };
           if(window.countdown) clearInterval(window.countdown);
           if(window.countdown2) clearInterval(window.countdown2);

		},

        // 第一步----------------------------------------------------------------------------------------------
        firstStep(){
            this.step = 2;
            this.getImgCode();
        },

        // 第二步----------------------------------------------------------------------------------------------
        // 获取图形验证码
        getImgCode(){
            this.dataForm.uuid = getUUID()
            this.imgCode = `${window.SITE_CONFIG['apiURL']}/seller-api/generate/image?uuid=${this.dataForm.uuid}`
        },
        // 在第二步中 上一步
        secondPreStep(){
            this.dataForm.captcha = "";
            this.dataForm.phoneCaptcha="";
            this.dataForm.emailCaptcha ="";
            this.step = 1;
        },
         // 第二步中获取邮箱或者手机验证码
        getVerifyCode(type){
            if(this.sendVerifyCodeLoading || this.msgBtn.waitingCode){
                return
            }
            if(!this.dataForm.captcha){
               this.$message.error("验证码不能为空");
               return;
            }
            // 推行验证码通过才能 发送验证码
            this.verifyCaptchaFn().then((res)=>{
                if(res.code!=200){
                    this.$message.error(res.msg);
                    return
                }
                // 获取手机验证码
                var obj = {
                    params:{
                        verifyType:this.radio,
                        linkman:this.radio==1?this.dataForm.mobilePhone:this.dataForm.email
                    }
                }
                this.sendVerifyCodeLoading = true;
                phoneCaptcha(obj).then((res)=>{
                    this.sendVerifyCodeLoading = false;
                    if(res.code==200){
                        this.$message({
                            message:"发送成功",
                            type: 'success',
                            duration: 1500,
                        })
                        this.msgBtn.waitingCode=true;
                        window.countdown = setInterval(() => {
                            this.msgBtn.count--;
                            this.msgBtn.text = this.msgBtn.count + "s";
                            if (this.msgBtn.count < 0) {
                                clearInterval(window.countdown);
                                this.msgBtn.text = "重新发送";
                                this.msgBtn.waitingCode = false;
                                this.msgBtn.count = 60;
                            }
                        }, 1000);
                    }else{
                    this.$message.error(res.msg);
                    }
                })
            })

        },

         // 验证图形验证码是否正确
        verifyCaptchaFn(){
            var obj2 = {
              params:{
                uuid:this.dataForm.uuid,
                captcha:this.dataForm.captcha
              }
            }
            // 验证验证码对不
            return new  Promise((resolve,reject)=>{
                verifyCaptcha(obj2).then((res)=>{
                    if(res.code!=200){
                      this.getImgCode();
                    }
                    resolve(res)

                })
             })
        },
        //  校验手机号邮箱验证码是否正确
        verifyPhoneCaptchaFn(type){
            let mobilePhone = type=="old"?this.dataForm.mobilePhone:this.dataForm.newMobilePhone
            let email = type=="old"?this.dataForm.email:this.dataForm.newEmail
            let phoneCaptcha = type=="old"?this.dataForm.phoneCaptcha:this.dataForm.newPhoneCaptcha
            let emailCaptcha = type=="old"?this.dataForm.emailCaptcha:this.dataForm.newEmailCaptcha

            // 第二步中的验证
            let  linkman = this.radio==1?mobilePhone:email
            let  captcha = this.radio==1?phoneCaptcha:emailCaptcha
            let  verifyType = this.radio
            // 第三步中的验证
            if(type=="new"){
                linkman = this.changeWay==1?mobilePhone:email
                captcha = this.changeWay==1?phoneCaptcha:emailCaptcha
                verifyType = this.changeWay
            }
           var obj = {
             params:{
                verifyType:verifyType,
                linkman:linkman,
                captcha:captcha
             }
            }
            return new  Promise((resolve,reject)=>{
                verifyPhoneCaptcha(obj).then((res)=>{
                    resolve(res)
                })
            })
        },
        // 第二步跳转到第三步
        secondStep(){
            // this.step = 3;
            // return;
            this.$refs['dataForm2'].validate((valid) => {
                if (!valid) {
                    return false
                }
                Promise.all([
                //   this.verifyCaptchaFn(),//  验证图形验证码是否正确
                  this.verifyPhoneCaptchaFn("old")//  校验手机号邮箱验证码是否正确
                ]).then((res) => {
                    console.log(res);
                    if(res[0].code!=200){
                        this.$message.error(res[0].msg);
                        return
                    }
                    this.step = 3;
                })
            })
        },

        // 第三步----------------------------------------------------------------------------------------------
         // 第三步中获取邮箱或者手机验证码
        getVerifyCodeNew(type){
             if(this.sendVerifyCodeNewLoading || this.msgBtn2.waitingCode){
                return
            }

          this.checkPhoneOrEmain3 = false;
          this.$refs['dataForm3'].validate((valid) => {
              if (!valid) {
                return false
              }
            // 获取手机验证码
            var obj = {
              params:{
                verifyType:this.changeWay,
                linkman:this.changeWay==1?this.dataForm.newMobilePhone:this.dataForm.newEmail
              }
            }
            this.sendVerifyCodeNewLoading = true;
            phoneCaptcha(obj).then((res)=>{
              this.sendVerifyCodeNewLoading = false;
              if(res.code==200){
                this.$message({
                  message:"发送成功",
                  type: 'success',
                  duration: 1500,
                })

                this.msgBtn2.waitingCode=true;
                window.countdown2 = setInterval(() => {
                  this.msgBtn2.count--;
                  this.msgBtn2.text = this.msgBtn2.count + "s";
                  if (this.msgBtn2.count < 0) {
                    clearInterval(window.countdown2);
                    this.msgBtn2.text = "重新发送";
                    this.msgBtn2.waitingCode = false;
                    this.msgBtn2.count = 60;
                  }
                }, 1000);
              }else{
                this.$message.error(res.msg);
              }
            })
          })

        },
        // 完成 提交修改
        dataFormSubmit(){
            this.editPhoneOrEmain();
        },
        // 修改商户手机号或者邮箱
        editPhoneOrEmain(){

            this.checkPhoneOrEmain3 = true;
            this.$refs['dataForm3'].validate((valid) => {
                if (!valid) {
                    return false
                }
              if(this.changeWay==1 && !this.dataForm.newPhoneCaptcha) {
                return
              }else if(this.changeWay==2 && !this.dataForm.newEmailCaptcha){
                return;
              }
              Promise.all([
                  this.verifyPhoneCaptchaFn("new")//  校验手机号邮箱验证码是否正确
                ]).then((res) => {
                    console.log(res);
                    if(res[0].code!=200){
                        this.$message.error(res[0].msg);
                        return
                    }
                    var obj  = {}
                   // 增加用户名与密码 RSA加密
                   let publicKey = security.publicKey;
                   var encrypt = new JSEncrypt();
                   encrypt.setPublicKey(publicKey);
                    if(this.changeWay==1){
                        obj.mobilePhone=encrypt.encrypt(this.dataForm.newMobilePhone)  // 修改商户手机号
                    }else{
                        obj.email=encrypt.encrypt(this.dataForm.newEmail)  // 修改商户邮箱
                    }
                    var fn = this.changeWay==1?editPhone:editEmail
                    this.saveLoading = true;
                    fn(obj).then((res)=>{
                        this.saveLoading = false
                        if(res.code==200){
                            this.$message.success(res.msg);
                            this.$emit("refreshData")
                            this.close();
                        }else{
                            this.$message.error(res.msg);
                        }

                    })
                })
            })
        },
		// 关闭前走的函数
        close(){
			this.visible=false;
			this.$parent.showChangeBindVisible = false;
        }
    },
}
</script>
<style lang="scss" scoped>
.imageWarp{
  min-height: 100px;
  display: flex;
  flex-wrap: wrap;
  .imageItem{
      width:100px;
      height:100px;
      line-height: 100px;
      display: inline-block;
      border: 1px solid #979797;
      margin: 10px 10px;
      overflow: hidden;
      img{
        width:98%;
        height:auto;
      }
  }
}

.getCode{
      margin-left: 4px;
      width: 111.8px;
}

.bottomBts{
    text-align: center;
    margin-top:40px;
}
.imgCodeInputClass{
    width: 120px;
}
.imgCodeClass{
    display:inline-block;
    width: 120px;
    height: 40px;
    margin-left: 4px;
}

.radioPhoneEmailWarp{
   /deep/ .el-form-item__content{
        display: inherit;
    }
}
</style>
