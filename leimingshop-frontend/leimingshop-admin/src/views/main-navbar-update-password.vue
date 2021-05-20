<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('updatePassword.title')"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :append-to-body="true">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item :label="$t('updatePassword.username')  + '：'">
        <span>{{ $store.state.user.name }}</span>
      </el-form-item>
      <el-form-item prop="password" :label="$t('updatePassword.password')  + '：'">
        <el-input v-model="dataForm.password" type="password" :placeholder="$t('updatePassword.password')" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item prop="newPassword" :label="$t('updatePassword.newPassword')  + '：'">
        <el-input v-model="dataForm.newPassword" type="password" :placeholder="$t('updatePassword.newPassword')" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item prop="comfirmPassword" :label="$t('updatePassword.comfirmPassword')  + '：'">
        <el-input v-model="dataForm.comfirmPassword" type="password" :placeholder="$t('updatePassword.comfirmPassword')" maxlength="20"></el-input>
      </el-form-item>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
import { clearLoginInfo } from '@/utils'
import JsEncrypt from 'jsencrypt'
import security from '@/utils/security.js'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        password: '',
        newPassword: '',
        comfirmPassword: ''
      }
    }
  },
  computed: {
    dataRule () {
      var validateComfirmPassword = (rule, value, callback) => {
        if (this.dataForm.newPassword !== value) {
          return callback(new Error(this.$t('updatePassword.validate.comfirmPassword')))
        }
        callback()
      }
      return {
        password: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        comfirmPassword: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateComfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
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
  
        obj.password = encrypt.encrypt(this.dataForm.password);
        obj.newPassword = encrypt.encrypt(this.dataForm.newPassword);
        obj.comfirmPassword = encrypt.encrypt(this.dataForm.comfirmPassword);

        this.$http.put('/admin-api/user/password', obj).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              clearLoginInfo()
              this.$router.replace({ name: 'login' })
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
