<template>
  <div class="findpwd">
     <!--头部-->
        <div class="findpwdHead">
            <img src="@/assets/img/sellerlogo.png" />
            <div class="Rtitle">找回密码</div>
        </div>
        <div class="findpwdBody">
            <div class="steps">
              <el-steps :active="active">
                  <el-step title="账号信息" description="">
                  </el-step>
                  <el-step title="验证身份" description=""></el-step>
                  <el-step title="重置密码" description=""></el-step>
              </el-steps>
            </div>
             <!-- 第一步 -->
            <div v-if="active==1" class="infoWarp">
              <el-form
                        :inline="true"
                        ref="dataForm"
                        :model="dataForm"
                        class="dataForm"
                        :rules="dataRule"
                         label-width="100px"
                >
                    <el-form-item label="商户账号：" prop="userName" class="phoneInput" style="margin-top:30px">
                        <el-input v-model="dataForm.userName" placeholder="用户名/手机号/邮箱" clearable @blur="isRegisteredMobile()"></el-input>
                    </el-form-item>
                    <el-form-item label="图形验证码：" prop="captcha">
                       <div class="captchaWarp" >
                          <el-input step="width:200px !important" v-model="dataForm.captcha" placeholder="请输入" clearable></el-input>
                          <img :src="captchaPath" @click="getCaptcha()" style="">
                       </div>
                    </el-form-item>
                </el-form>
                <div style="margin-left:150px;">
                  <el-button class="btn nextStep" type="primary" @click="goVerify()">下一步</el-button>
                </div>
            </div>
             <!-- 第二步 -->
            <div v-if="active==2"  class="infoWarp">
                <el-form
                        :inline="true"
                        ref="dataForm"
                        :model="dataForm"
                        class="dataForm"
                        :rules="dataRule"
                         label-width="100px"
                >
                    <el-form-item label="验证方式：" prop="mobilePhone" class="phoneInput">
                        <div class="mobilePhoneClass">
                          <el-select v-model="dataForm.verifyType" placeholder="请选择">
                              <el-option key="1" label="手机" value="1"></el-option>
                              <el-option key="2" label="邮箱" value="2"></el-option>
                          </el-select>
                          <span v-if="dataForm.verifyType==1" style="font-size:14px">
                              将向您的手机：{{dataForm.mobilePhone.toString().replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')}}发送验证码
                          </span>
                          <span v-if="dataForm.verifyType==2" style="font-size:14px">
                              将向您的邮箱：{{dataForm.email}}发送验证码
                          </span>
                        </div>
                    </el-form-item>
                    <el-form-item label="验证码：" prop="captcha2">
                      <div class="captchaWarp">
                          <el-input v-model="dataForm.captcha2" placeholder="请输入" clearable></el-input>
                          <el-button class="btn getCode" type="primary" size="mini" style="margin-left:4px;" @click="getPhoneCaptcha()" :disabled="msgBtn.waitingCode">{{msgBtn.text}}</el-button>
                      </div>
                    </el-form-item>
                </el-form>
                <div style="margin-left:100px;">
                  <el-button class="btn nextStep" type="primary" @click="goResetPwd()">下一步</el-button>
                   <el-button class="btn nextStep" type="primary" @click="preStep()">上一步</el-button>
                </div>
            </div>
            <!-- 第三步 -->
            <div v-if="active==3"  class="infoWarp">
                    <el-form
                        :inline="true"
                        ref="dataForm"
                        :model="dataForm"
                        class="dataForm"
                        :rules="dataRule"
                         label-width="100px"
                >
                    <el-form-item label="重置密码：" prop="newPassword" class="phoneInput">
                        <el-input v-model="dataForm.newPassword" placeholder="请输入" show-password clearable @blur="isRegisteredMobile()"></el-input>
                    </el-form-item>
                    <el-form-item label="再输一次：" prop="confirmPassword" class="phoneInput">
                        <el-input v-model="dataForm.confirmPassword" placeholder="请输入" show-password clearable @blur="isRegisteredMobile()"></el-input>
                    </el-form-item>
                </el-form>
                <div style="margin-left:150px;">
                   <el-button class="btn nextStep" type="primary" @click="finishFn()">完成</el-button>
                </div>
            </div>
        </div>

        <div class="findpwdFoot">
            雷铭科技2019 © leimingtech.com
        </div>

  </div>
</template>

<script>
  import { getUUID } from '@/utils'
  import JsEncrypt from 'jsencrypt'
  import security from '@/utils/security.js'
  import { verifyUsername,verifyCaptcha,phoneCaptcha,verifyPhoneCaptcha,forgetPassword} from "@/api/api.js"
import { reject } from 'q';
  export default {
    data(){
      // 验证账号是否存在
      var validateUserName = (rule, value, callback) => {
        if(this.userNameStatus){
             callback()
        }else{
            return callback(new Error('账号不存在'))
        }
      };
      // 验证验证码
      var validatecaptcha= (rule, value, callback) => {
        if(this.captchaStatus){
            callback()
        }else{
            return callback(new Error('验证码不正确'))
        }
      };

       var validatecaptcha2= (rule, value, callback) => {
        if(this.captcha2Status){
            callback()
        }else{
            return callback(new Error('验证码不正确'))
        }
      };
      // 两次密码是否一致
      var  validateConfirmPassword= (rule, value, callback) => {
        if(value!= this.dataForm.newPassword){
          return callback("两次密码不一致");
        }
        
        return callback();
      };
      return{
          active:1,
          isHref:false,
          captchaPath:'',
          msgBtn: {
                    text: "发送验证码",
                    waitingCode: false,
                    count: 60
          },
          dataForm:{
              uuid:'',
              userName:'',
              captcha:'',
              verifyType:'1',// 验证类型
              linkman:"",
              captcha2:'',// 手机或者邮箱验证码
              newPassword:'',//新密码
              confirmPassword:'',//确认密码
              // oldPassword:"",//原密码
              mobilePhone:'',
              email:'',
          },
          userNameStatus:true,
          captchaStatus:true,
          captcha2Status:true,
           // 校验规则
          dataRule: {
              userName: [
                  {required: true, message: '必填项不能为空', trigger: 'blur'},
                  { validator: validateUserName, trigger: 'blur' }
              ],
              captcha: [
                  {required: true, message: '必填项不能为空', trigger: 'blur'},
                  { validator: validatecaptcha, trigger: 'blur' }
              ],
              captcha2: [
                  {required: true, message: '必填项不能为空', trigger: 'blur'},
                  { validator: validatecaptcha2, trigger: 'blur' }
              ],
              newPassword: [
                  {required: true, message: '必填项不能为空', trigger: 'blur'},
              ],
              confirmPassword: [
                  {required: true, message: '必填项不能为空', trigger: 'blur'},
                  { validator: validateConfirmPassword, trigger: 'blur' }
              ],
          },
      }
    },
    created () {
      this.getCaptcha()
    },
    methods:{
        // 下一步
        nextStep(){

        },
        // 获取图形验证嘛
        getCaptcha(){
          this.dataForm.uuid = getUUID();
          this.captchaPath = `${window.SITE_CONFIG['apiURL']}/seller-api/generate/image?uuid=${this.dataForm.uuid}`
        },
        isRegisteredMobile(){

        },
        // 验证手机号是否存在
        verifyUserNameFn(){
            var obj = {
              params:{
                userName:this.dataForm.userName,
              }
            }
            return new  Promise((resolve,reject)=>{
                //  校验账号是否存在
                  verifyUsername(obj).then((res)=>{
                      if(res.code==200){
                        resolve("ok")
                          this.dataForm.mobilePhone = res.data.mobilePhone
                          this.dataForm.email =  res.data.email
                      }else{
                        resolve("error")
                        // this.$message.error(res.msg);
                        this.userNameStatus = false
                        this.$refs['dataForm'].validate((valid) => {
                            this.userNameStatus = true;
                        });
                      }
                  })
            })
        },
        // 验证验证码是否正确
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
                    if(res.code==200){
                       resolve("ok")
                       
                    }else{
                       resolve("error")
                      this.getCaptcha();
                      this.captchaStatus = false;
                      this.$refs['dataForm'].validate((valid) => {
                          this.captchaStatus = true;
                      });
                    }
                })
             })
        },
        // 下一步验证身份
        goVerify(){
           this.$refs['dataForm'].validate((valid) => {
                if (!valid) {
                  return false
                }
                //  验证手机号是否存在
                // this.verifyUserNameFn();
                //  // 验证验证码是否正确
                // this.verifyCaptchaFn();
                Promise.all([
                  this.verifyUserNameFn(),
                  this.verifyCaptchaFn()
                ]).then((res) => {
                    console.log(res);
                    if(res[0]=="ok" && res[1]=="ok"){
                       this.active=2;
                    }
                })
           })
        },
        // 获取手机或者邮箱验证码
        getPhoneCaptcha(){
          var obj = {
            params:{
              verifyType:this.dataForm.verifyType,
              linkman:this.dataForm.verifyType==1?this.dataForm.mobilePhone:this.dataForm.email
            }
          }
          phoneCaptcha(obj).then((res)=>{
                if(res.code==200){
                  this.$message({
                            message:"发送成功",
                            type: 'success',
                            duration: 1500,
                  })

                this.msgBtn.waitingCode=true;
                  let countdown = setInterval(() => {
                    this.msgBtn.count--;
                    this.msgBtn.text = this.msgBtn.count + "s";
                    if (this.msgBtn.count < 0) {
                        clearInterval(countdown);
                        this.msgBtn.text = "重新发送";
                        this.msgBtn.waitingCode = false;
                        this.msgBtn.count = 60;
                    }
                }, 1000);
                }else{
                  this.$message.error(res.msg);
                }
          })
        },
        // 上一步
        preStep(){
            this.active = 1;
        },
        // 重置密码
        goResetPwd(){
            var obj = {
             params:{
                verifyType:this.dataForm.verifyType,
                linkman:this.dataForm.verifyType==1?this.dataForm.mobilePhone:this.dataForm.email,
                captcha:this.dataForm.captcha2
             }
            }
            verifyPhoneCaptcha(obj).then((res)=>{
                if(res.code==200){
                  this.active = 3;
                }else{
                   this.captcha2Status = false;
                    this.$refs['dataForm'].validate((valid) => {
                          this.captcha2Status = true;
                    });
                }
            })
        },
        // 确定修改密码
        finishFn(){
             this.$refs['dataForm'].validate((valid) => {
                if (!valid) {
                  return false
                }
                  // 增加用户名与密码 RSA加密
                    let publicKey = security.publicKey;
                    var encrypt = new JSEncrypt();
                    encrypt.setPublicKey(publicKey);

                var obj = {
                    username:encrypt.encrypt(this.dataForm.userName),
                    newPassword:encrypt.encrypt(this.dataForm.newPassword),
                    confirmPassword:encrypt.encrypt(this.dataForm.confirmPassword),
                    // oldPassword:this.dataForm.oldPassword
                }
                forgetPassword(obj).then((res)=>{
                    if(res.code==200){
                      this.$router.replace({ name: 'login' })
                    }else{
                      this.$message.error(res.msg);
                    }
                })
             })
        
        }
        
    }
   
  }
</script>
<style lang="scss" scoped>
.findpwd{
    .findpwdHead{
        height: 120px;
        background-color: rgba(51, 102, 204, 1);
        img{
            border-width: 0px;
            position: absolute;
            left: 49px;
            top: 26px;
            width: 257px;
            height: 60px;
        }
        .Rtitle{
            border-width: 0px;
            position: absolute;
            left: 368px;
            top: 59px;
            width: 113px;
            height: 33px;
            font-size: 28px;
            color: #FFFFFF;
        }
    }
    .findpwdBody{
        // margin-left: 10%;
        width:40%;
        margin: auto;
        margin-top: 137px;
        overflow: hidden;
        .steps{
         
        }
    }
    .findpwdFoot{
        margin-top: 100px;
        text-align: center;
        color: rgba(102, 102, 102, 0.847058823529412);
    }
    /deep/ .el-form--inline .el-input{
          width: 240px !important;
    }
    .captchaWarp{
        display:flex;
        width: 240px;
        /deep/ .el-form--inline .el-input{
            width: 140px !important;
        }
        img{
          height:34px;
          width:120px;
          border: 1px solid #b4bccc;
          margin-top:2px;
          margin-left:4px;
        }
      }
    .infoWarp{
      // width: 380px;
      //  margin: auto;
      margin-left:20%;
    }
    .mobilePhoneClass{
        display: flex;
        flex-direction: column;
    }
    .getCode{
      margin-left: 28px;
      width: 111.8px;
    }
}
</style>
