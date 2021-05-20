<template>
  <div class="aui-wrapper aui-page__login">
    <canvasbg></canvasbg>
    <div class="aui-content__wrapper" stype=" pointer-events:auto;">
      <main class="aui-content logincontent">
        <div class="login-header">
          <!-- <h2 class="login-brand"></h2> -->
            <img class="login-header-img" src="~@/assets/img/logintitle1.png" alt="">
        </div>
        <div class="login-body">
          <h3 class="login-title">{{ $t('login.title') }}</h3>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" status-icon>
            <!-- <el-form-item>
              <el-select v-model="$i18n.locale" class="w-percent-100">
                <el-option v-for="(val, key) in i18nMessages" :key="key" :label="val._lang" :value="key"></el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item prop="username">
              <el-input v-model="dataForm.username" :placeholder="$t('login.username')" maxlength="20">
                <span slot="prefix" class="el-input__icon">
                  <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
                </span>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" :placeholder="$t('login.password')" maxlength="20">
                <span slot="prefix" class="el-input__icon">
                  <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-lock"></use></svg>
                </span>
              </el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" :placeholder="$t('login.captcha')">
                    <span slot="prefix" class="el-input__icon">
                      <svg class="icon-svg" aria-hidden="true"><use xlink:href="#icon-safetycertificate"></use></svg>
                    </span>
                  </el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmitHandle()" class="w-percent-100">{{ $t('login.title') }}</el-button>
            </el-form-item>
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
      },
    }
  },
  components: {
    canvasbg
  },
  computed: {
    dataRule () {
      var validateComfirmPassword3 = (rule, value, callback) => {
        // console.log(this.captchaStatus)
        if (this.captchaStatus) {
          callback()
        }else{
          return callback(new Error('验证码不正确'))
        }
      }
      var validateComfirmPassword2 = (rule, value, callback) => {
        // console.log(this.captchaStatus)
        if (this.passwordStatus) {
          callback()
        }else{
          return callback(new Error('账号或密码错误'))
        }
      }
      return {
        username: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword2, trigger: 'blur' }
        ],
        password: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword2, trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword3, trigger: 'blur' }
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
      this.captchaPath = `${window.SITE_CONFIG['apiURL']}/admin-api/captcha?uuid=${this.dataForm.uuid}`
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

        this.$http.post('/admin-api/login', obj).then(({ data: res }) => {
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
            return this.$message.error(res.msg);
          }
          Cookies.set('ADMIN-TOKEN', res.data.token)
          this.$router.replace({ name: 'home' })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
<style lang="scss">
.aui-page__login{
      overflow: hidden;
}
.logincontent .el-form-item{
	margin-bottom: 20px !important;
}
</style>

