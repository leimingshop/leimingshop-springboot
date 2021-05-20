<template>
  <el-dialog :visible.sync="visible" :title="!pageId ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="account" :label="$t('user.username') + '：'">
        <el-input v-model="dataForm.account" :placeholder="$t('user.username')" maxlength="20"></el-input>
      </el-form-item>

      <el-form-item prop="password" v-if="!pageId" :label="$t('user.password') + '：'" :class="{ 'is-required': !pageId }">
        <el-input v-model="dataForm.password" type="password" placeholder="6-12位,由数字,字母组成" maxlength="12"></el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword" v-if="!pageId" :label="$t('user.comfirmPassword') + '：'" :class="{ 'is-required': !pageId }">
        <el-input v-model="dataForm.confirmPassword" type="password" placeholder="6-12位,由数字,字母组成" maxlength="12"></el-input>
      </el-form-item>
      <el-form-item prop="nickname" label="姓名：">
        <el-input v-model="dataForm.nickname" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item prop="sex" :label="$t('user.gender') + '：'" size="mini">
        <el-radio-group v-model="dataForm.sex">
          <el-radio :label="0">保密</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="email" :label="$t('user.email') + '：'">
        <el-input v-model="dataForm.email" maxlength="30" :placeholder="$t('user.email')"></el-input>
      </el-form-item>
      <el-form-item prop="mobilePhone" :label="$t('user.mobile') + '：'">
        <el-input v-model="dataForm.mobilePhone" :placeholder="$t('user.mobile')"></el-input>
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
import { isEmail, isMobile } from '@/utils/validate'
import JsEncrypt from 'jsencrypt'
import security from '@/utils/security.js'
export default {
  data () {
    return {
      visible: false,
      deptList: [],
      deptListVisible: false,
      pageId:null,
      dataForm: {
        id: '',
        password:'',
        confirmPassword:'',
        account: '',
        mobilePhone: '',
        isEnable: 0,
        nickname:"",
        email:"",
        sex:0
      }
    }
  },
  computed: {
    dataRule () {
      var validatePassword = (rule, value, callback) => {
        if (!this.pageId && !/\S/.test(value)) {
          return callback(new Error(this.$t('validate.required')))
        }
        callback()
      }
      var validateComfirmPassword = (rule, value, callback) => {
        if (!this.pageId && !/\S/.test(value)) {
          return callback(new Error(this.$t('validate.required')))
        }
        if (this.dataForm.password !== value) {
          return callback(new Error(this.$t('user.validate.comfirmPassword')))
        }
        callback()
      }
      // var validateMobile = (rule, value, callback) => {
      //    if(value&&!isMobile(value)){
      //           return callback(new Error('电话格式不正确'))
      //     }else{
      //        callback()
      //     }
      // }
      var validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.email') })))
        }
        callback()
      }
      var validateMobile = (rule, value, callback) => {
        if (!isMobile(value)) {
          return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.mobile') })))
        }
        callback()
      }
      return {
        account: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        deptName: [
          { required: true, message: this.$t('validate.required'), trigger: 'change' }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validateComfirmPassword, trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        email: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ],
        mobilePhone: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateMobile, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.password=''
      console.log('666666',id)
      this.visible = true;
      if(id){
        this.pageId = id;
        this.getInfo(id);
      }else{
        this.pageId = '';
      }
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
     
      })
    },

    // 获取信息
    getInfo (id) {
      this.$http.get(`/admin-api/store/user/${id}`).then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
       // 增加用户名与密码 RSA加密
        let publicKey = security.publicKey;
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var obj = {};
        Object.assign(obj,this.dataForm)
        obj.storeId=null
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        if(this.pageId){
           this.dataForm.password=''
        }else{
          obj.account = encrypt.encrypt(this.dataForm.account);
          obj.password = encrypt.encrypt(this.dataForm.password);
          obj.confirmPassword = encrypt.encrypt(this.dataForm.confirmPassword);
        }

        this.$http[!this.pageId ? 'post' : 'put']('/admin-api/store/user', {
          ...obj
        }).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>

<style lang="scss">
.mod-sys__user {
  .dept-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }
  .role-list {
    .el-select {
      width: 100%;
    }
  }
}
</style>
