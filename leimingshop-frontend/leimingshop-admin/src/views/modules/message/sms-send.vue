<template>
  <el-dialog :visible.sync="visible" :title="$t('sms.send')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="mobile" :label="$t('sms.mobile') + '：'">
        <el-input v-model="dataForm.mobile" :placeholder="$t('sms.mobile')"></el-input>
      </el-form-item>
      <el-form-item prop="params" :label="$t('sms.params') + '：'">
        <el-input v-model="dataForm.params" :placeholder="$t('sms.paramsTips')"></el-input>
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
import { isMobile } from '@/utils/validate'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        mobile: '',
        params: ''
      }
    }
  },
  computed: {
    dataRule () {
      var validateMobile = (rule, value, callback) => {
        if (!isMobile(value)) {
          return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.mobile') })))
        }
        callback()
      }
      return {
        mobile: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validateMobile, trigger: 'blur' }
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
        this.$http.post(
          '/admin-api/sms/send',
          this.dataForm,
          { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
        ).then(({ data: res }) => {
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
