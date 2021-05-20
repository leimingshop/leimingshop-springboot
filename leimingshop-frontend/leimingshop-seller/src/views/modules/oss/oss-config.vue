<template>
  <el-dialog :visible.sync="visible" :title="$t('oss.config')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item :label="$t('oss.type') + '：'" size="mini">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="1">{{ $t('oss.type1') }}</el-radio>
          <el-radio :label="2">{{ $t('oss.type2') }}</el-radio>
          <el-radio :label="3">{{ $t('oss.type3') }}</el-radio>
          <el-radio :label="4">{{ $t('oss.type4') }}</el-radio>
          <el-radio :label="5">{{ $t('oss.type5') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="dataForm.type === 1">
        <el-form-item prop="qiniuDomain" :label="$t('oss.qiniuDomain') + '：'">
          <el-input v-model="dataForm.qiniuDomain" :placeholder="$t('oss.qiniuDomainTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qiniuPrefix" :label="$t('oss.qiniuPrefix') + '：'">
          <el-input v-model="dataForm.qiniuPrefix" :placeholder="$t('oss.qiniuPrefixTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qiniuAccessKey" :label="$t('oss.qiniuAccessKey') + '：'">
          <el-input v-model="dataForm.qiniuAccessKey" :placeholder="$t('oss.qiniuAccessKeyTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qiniuSecretKey" :label="$t('oss.qiniuSecretKey') + '：'">
          <el-input v-model="dataForm.qiniuSecretKey" :placeholder="$t('oss.qiniuSecretKeyTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qiniuBucketName" :label="$t('oss.qiniuBucketName') + '：'">
          <el-input v-model="dataForm.qiniuBucketName" :placeholder="$t('oss.qiniuBucketNameTips')"></el-input>
        </el-form-item>
      </template>
      <template v-else-if="dataForm.type === 2">
        <el-form-item prop="aliyunDomain" :label="$t('oss.aliyunDomain') + '：'">
          <el-input v-model="dataForm.aliyunDomain" :placeholder="$t('oss.aliyunDomainTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunPrefix" :label="$t('oss.aliyunPrefix') + '：'">
          <el-input v-model="dataForm.aliyunPrefix" :placeholder="$t('oss.aliyunPrefixTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunEndPoint" :label="$t('oss.aliyunEndPoint') + '：'">
          <el-input v-model="dataForm.aliyunEndPoint" :placeholder="$t('oss.aliyunEndPointTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunAccessKeyId" :label="$t('oss.aliyunAccessKeyId') + '：'">
          <el-input v-model="dataForm.aliyunAccessKeyId" :placeholder="$t('oss.aliyunAccessKeyIdTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunAccessKeySecret" :label="$t('oss.aliyunAccessKeySecret') + '：'">
          <el-input v-model="dataForm.aliyunAccessKeySecret" :placeholder="$t('oss.aliyunAccessKeySecretTips')"></el-input>
        </el-form-item>
        <el-form-item prop="aliyunBucketName" :label="$t('oss.aliyunBucketName') + '：'">
          <el-input v-model="dataForm.aliyunBucketName" :placeholder="$t('oss.aliyunBucketNameTips')"></el-input>
        </el-form-item>
      </template>
      <template v-else-if="dataForm.type === 3">
        <el-form-item prop="qcloudDomain" :label="$t('oss.qcloudDomain') + '：'">
          <el-input v-model="dataForm.qcloudDomain" :placeholder="$t('oss.qcloudDomainTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudPrefix" :label="$t('oss.qcloudPrefix') + '：'">
          <el-input v-model="dataForm.qcloudPrefix" :placeholder="$t('oss.qcloudPrefixTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudAppId" :label="$t('oss.qcloudAppId') + '：'">
          <el-input v-model="dataForm.qcloudAppId" :placeholder="$t('oss.qcloudAppIdTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudSecretId" :label="$t('oss.qcloudSecretId') + '：'">
          <el-input v-model="dataForm.qcloudSecretId" :placeholder="$t('oss.qcloudSecretIdTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudSecretKey" :label="$t('oss.qcloudSecretKey') + '：'">
          <el-input v-model="dataForm.qcloudSecretKey" :placeholder="$t('oss.qcloudSecretKeyTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudBucketName" :label="$t('oss.qcloudBucketName') + '：'">
          <el-input v-model="dataForm.qcloudBucketName" :placeholder="$t('oss.qcloudBucketNameTips')"></el-input>
        </el-form-item>
        <el-form-item prop="qcloudRegion" :label="$t('oss.qcloudRegion') + '：'">
          <el-select v-model="dataForm.qcloudRegion" clearable :placeholder="$t('oss.qcloudRegionTips')" class="w-percent-100">
            <el-option value="ap-beijing-1" :label="$t('oss.qcloudRegionBeijing1')"></el-option>
            <el-option value="ap-beijing" :label="$t('oss.qcloudRegionBeijing')"></el-option>
            <el-option value="ap-shanghai" :label="$t('oss.qcloudRegionShanghai')"></el-option>
            <el-option value="ap-guangzhou" :label="$t('oss.qcloudRegionGuangzhou')"></el-option>
            <el-option value="ap-chengdu" :label="$t('oss.qcloudRegionChengdu')"></el-option>
            <el-option value="ap-chongqing" :label="$t('oss.qcloudRegionChongqing')"></el-option>
            <el-option value="ap-singapore" :label="$t('oss.qcloudRegionSingapore')"></el-option>
            <el-option value="ap-hongkong" :label="$t('oss.qcloudRegionHongkong')"></el-option>
            <el-option value="na-toronto" :label="$t('oss.qcloudRegionToronto')"></el-option>
            <el-option value="eu-frankfurt" :label="$t('oss.qcloudRegionFrankfurt')"></el-option>
          </el-select>
        </el-form-item>
      </template>
      <template v-else-if="dataForm.type === 4">
        <el-form-item prop="localDomain" :label="$t('oss.localDomain') + '：'">
          <el-input v-model="dataForm.localDomain" :placeholder="$t('oss.localDomainTips')"></el-input>
        </el-form-item>
      </template>
      <template v-else-if="dataForm.type === 5">
        <el-form-item prop="fastdfsDomain" :label="$t('oss.fastdfsDomain') + '：'">
          <el-input v-model="dataForm.fastdfsDomain" :placeholder="$t('oss.fastdfsDomainTips')"></el-input>
        </el-form-item>
        <el-form-item prop="localPrefix" :label="$t('oss.localPrefix') + '：'">
          <el-input v-model="dataForm.localPrefix" :placeholder="$t('oss.localPrefixTips')"></el-input>
        </el-form-item>
        <el-form-item prop="localPath" :label="$t('oss.localPath') + '：'">
          <el-input v-model="dataForm.localPath" :placeholder="$t('oss.localPathTips')"></el-input>
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
        type: 0,
        qiniuDomain: '',
        qiniuPrefix: '',
        qiniuAccessKey: '',
        qiniuSecretKey: '',
        qiniuBucketName: '',
        aliyunDomain: '',
        aliyunPrefix: '',
        aliyunEndPoint: '',
        aliyunAccessKeyId: '',
        aliyunAccessKeySecret: '',
        aliyunBucketName: '',
        qcloudDomain: '',
        qcloudPrefix: '',
        qcloudAppId: 0,
        qcloudSecretId: '',
        qcloudSecretKey: '',
        qcloudBucketName: '',
        qcloudRegion: '',
        localDomain: '',
        fastdfsDomain: '',
        localPrefix: '',
        localPath: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        qiniuDomain: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qiniuAccessKey: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qiniuSecretKey: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qiniuBucketName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunDomain: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunEndPoint: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunAccessKeyId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunAccessKeySecret: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        aliyunBucketName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudDomain: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudAppId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudSecretId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudSecretKey: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudBucketName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        qcloudRegion: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        localDomain: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        fastdfsDomain: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        localPath: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    'dataForm.type' (val) {
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
      this.$http.get('/oss/file/info').then(({ data: res }) => {
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
        this.$http.post('/oss/file', this.dataForm).then(({ data: res }) => {
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
