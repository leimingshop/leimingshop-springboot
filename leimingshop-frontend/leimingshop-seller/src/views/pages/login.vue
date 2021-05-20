<template>
  <div class="aui-wrapper aui-page__login">
    <canvasbg></canvasbg>
    <div class="aui-content__wrapper" stype=" pointer-events:auto;">
      <main class="aui-content">
        <div class="login-header">
          <!-- <h2 class="login-brand">{{ $t('brand.lg') }}</h2> -->
          <!-- <img src="~@/assets/img/sellerlogo.png" alt="" class="login-brand"> -->
          <img class="login-header-img" src="~@/assets/img/bgk.png" alt="">
        </div>
        <div class="login-body">
          <img src="~@/assets/img/sellerlogo.png" alt="" style="margin-bottom:20px">
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" status-icon>
            <!-- <el-form-item>
              <el-select v-model="$i18n.locale" class="w-percent-100">
                <el-option v-for="(val, key) in i18nMessages" :key="key" :label="val._lang" :value="key"></el-option>
              </el-select>
            </el-form-item> -->
            <h3 class="login-title">商家中心登录</h3>
            <el-form-item prop="username">
              <el-input v-model="dataForm.username" placeholder="请输入账号" maxlength="20">
                <span slot="prefix" class="el-input__icon">
                  <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
                </span>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="请输入密码" maxlength="20">
                <span slot="prefix" class="el-input__icon">
                  <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-lock"></use></svg>
                </span>
              </el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="11">
                  <el-input v-model="dataForm.captcha" :placeholder="$t('login.captcha')">
                    <span slot="prefix" class="el-input__icon">
                      <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-safetycertificate"></use></svg>
                    </span>
                  </el-input>
                </el-col>
                <el-col :span="9" class="login-captcha" style="padding:0;">
                  <img :src="captchaPath" @click="getCaptcha()" style="height:38px;border: 1px solid #b4bccc;">
                </el-col>
                <!-- <el-col :span="4" class="login-captcha"> -->
                  <a @click="getCaptcha()" style="font-size:10px;cursor: pointer;">换一张</a>
                <!-- </el-col> -->
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmitHandle()" class="w-percent-100">{{ $t('login.title') }}</el-button>
            </el-form-item>
            <div style="display: flex;justify-content: space-between;">
              <el-button type="text" @click="jumpRegistered">注册商户</el-button>
              <el-button type="text" @click="goFindpwd">忘记密码？</el-button>
            </div>
          </el-form>
        </div>
        <div class="login-footer">
          <p>
          </p>
          <p><a href="https://www.leimingtech.com/" target="_blank">雷铭科技</a>2019 © leimingtech.com</p>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import debounce from 'lodash/debounce'
import { messages } from '@/i18n'
import { getUUID } from '@/utils'
import canvasbg from '@/components/canvasbg'
import JsEncrypt from 'jsencrypt'
import security from '@/utils/security.js'

export default {
  data () {
    return {
      i18nMessages: messages,
      captchaPath: '',
      captchaStatus:true,
      passwordStatus:true,
      dataForm: {
        username: '',
        password: '',
        uuid: '',
        captcha: ''
      }
    }
  },
  components: {
    canvasbg
  },
  computed: {
    dataRule () {
      var validateComfirmPassword = (rule, value, callback) => {
        if (this.captchaStatus) {
          callback()
        }else{
          return callback(new Error('验证码错误'))
        }
      }
      var validateComfirmPassword1 = (rule, value, callback) => {
        if (this.passwordStatus) {
          callback()
        }else{
          return callback(new Error('账号或密码错误'))
        }

      }
      return {
        username: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword1, trigger: 'blur' }
        ],
        password: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword1, trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.getCaptcha()
  },
  methods: {
    // 获取验证码
    getCaptcha () {
      this.dataForm.uuid = getUUID()
      this.captchaPath = `${window.SITE_CONFIG['apiURL']}/seller-api/generate/image?uuid=${this.dataForm.uuid}`
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }

        // 增加用户名与密码 RSA加密
        let publicKey = security.publicKey;
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var obj = {};
        Object.assign(obj,this.dataForm)
        obj.username = encrypt.encrypt(this.dataForm.username);
        obj.password = encrypt.encrypt(this.dataForm.password);

        this.$http.post('/seller-api/login', obj).then(({ data: res }) => {
          console.log('登录信息',res)
          if (res.code != 200) {
            this.getCaptcha();
            if(res.code == 10007){
              this.captchaStatus = false;
              this.$refs['dataForm'].validate((valid) => {
                this.captchaStatus = true;
              })
            }
            if(res.code == 10004){
              this.passwordStatus = false;
              this.$refs['dataForm'].validate((valid) => {
                this.passwordStatus = true;
              })
            }
            return this.$message.error(res.msg)
          }
          Cookies.set('SELLER-TOKEN', res.data.token)
          // 区分登录进去页面还是刷新进去
          window.isloaingEntry = true;
          this.$router.replace({ name: 'home' })
        })
      })
    }, 1000, { 'leading': true, 'trailing': false }),
    //跳到注册页面
    jumpRegistered(){
      this.$router.replace({ path: 'registered' })
    },
    goFindpwd(){
      this.$router.push({ path: 'findpwd' })
    },
  }
}
</script>
<style lang="scss">
.aui-page__login{
      overflow: hidden;
}
</style>

