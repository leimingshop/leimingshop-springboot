<template>
  <el-dialog :visible.sync="visible" :title="$t('sms.config')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item :label="$t('sms.platform')+ '：'" size="mini">
        <el-radio-group v-model="dataForm.platform">
          <el-radio :label="1">{{ $t('sms.platform1') }}</el-radio>
          <el-radio :label="2">{{ $t('sms.platform2') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="dataForm.platform === 1">
        <el-form-item prop="aliyunAccessKeyId" :label="$t('sms.aliyunAccessKeyId')+ '：'">
          <el-input v-model="dataForm.aliyunAccessKeyId" :placeholder="$t('sms.aliyunAccessKeyIdTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunAccessKeySecret" :label="$t('sms.aliyunAccessKeySecret')+ '：'">
          <el-input v-model="dataForm.aliyunAccessKeySecret" :placeholder="$t('sms.aliyunAccessKeySecretTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunSignName" :label="$t('sms.aliyunSignName')+ '：'">
          <el-input v-model="dataForm.aliyunSignName" :placeholder="$t('sms.aliyunSignName')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunTemplateCode" :label="$t('sms.aliyunTemplateCode')+ '：'">
          <el-input v-model="dataForm.aliyunTemplateCode" :placeholder="$t('sms.aliyunTemplateCodeTips')"></el-input>
        </el-form-item>
      </template>
      <template v-else-if="dataForm.platform === 2">
        <el-form-item prop="qcloudAppId" :label="$t('sms.qcloudAppId')">
          <el-input v-model="dataForm.qcloudAppId" :placeholder="$t('sms.qcloudAppIdTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudAppKey" :label="$t('sms.qcloudAppKey')">
          <el-input v-model="dataForm.qcloudAppKey" :placeholder="$t('sms.qcloudAppKeyTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudSignName" :label="$t('sms.qcloudSignName')">
          <el-input v-model="dataForm.qcloudSignName" :placeholder="$t('sms.qcloudSignName')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudTemplateId" :label="$t('sms.qcloudTemplateId')">
          <el-input v-model="dataForm.qcloudTemplateId" :placeholder="$t('sms.qcloudTemplateIdTips')"></el-input>
        </el-form-item>
      </template>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        platform: 0,
        aliyunAccessKeyId: '',
        aliyunAccessKeySecret: '',
        aliyunSignName: '',
        aliyunTemplateCode: '',
        qcloudAppId: 0,
        qcloudAppKey: '',
        qcloudSignName: '',
        qcloudTemplateId: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        aliyunAccessKeyId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunAccessKeySecret: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunSignName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunTemplateCode: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudAppId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudAppKey: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudSignName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudTemplateId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    'dataForm.platform' (val) {
      this.$refs['dataForm'].clearValidate()
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.getInfo()
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get('/message/sms/config').then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.dataForm = res.data
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http.post('/message/sms/saveConfig', this.dataForm).then(({ data: res }) => {
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
