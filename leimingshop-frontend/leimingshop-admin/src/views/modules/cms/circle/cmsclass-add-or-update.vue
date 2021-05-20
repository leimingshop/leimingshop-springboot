<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" label-position="left" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '100px'">
      <el-form-item label="圈子分类名称:" label-width="120px"  prop="acName" >
        <el-col :span="12">
          <el-input  v-model="dataForm.acName"  placeholder="圈子分类名称" maxlength="20"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="分类图标：" prop="imageUrl">
        <el-upload
          class="avatar-uploader"
          style="margin-right:30px;"
          action="123"
          :show-file-list="false"
          :http-request="upLoad1"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="dataForm.imageUrl" :src="$imgDomain+dataForm.imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="分类icon：" prop="acIcon">
        <el-upload
          class="avatar-uploader"
          style="margin-right:30px;"
          action="123"
          :show-file-list="false"
          :http-request="upLoad2"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="dataForm.acIcon" :src="$imgDomain+dataForm.acIcon" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>

      <el-form-item label="优先级排序：" prop="sort">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number> 0-255，数字越小排序越靠前
      </el-form-item>
      <el-form-item prop="status" label="是否启用:" size="mini">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
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
import { getUrlBase64 } from '@/utils'
import { uploadPicBase64 } from '@/api/api'
import { circleClassInfoUrl,
	saveCircleClassUrl,
	updateCircleClassUrl} from "@/api/url";
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        acName: '',
        sort: '',
        status: 1,
        imageUrl:'',
				acIcon:'',
      },
			fileList: [],
    }
  },
  computed: {
    dataRule () {
      return {
        acName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
				imageUrl: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
				acIcon: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
				sort: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
				status: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
        }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(circleClassInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code == 200) {
					this.dataForm = {
						...this.dataForm,
						...res.data
					}
        }else{
          return this.$message.error(res.msg)
        }
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
				this.$http[!this.dataForm.id ? 'post' : 'put'](!this.dataForm.id ? saveCircleClassUrl : updateCircleClassUrl, this.dataForm).then(({ data: res }) => {
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
    }, 1000, { 'leading': true, 'trailing': false }),

    //上传分类图标
		upLoad1(file) {
			const that = this;
			that.getBease64(URL.createObjectURL(file.file),file.file.type,'1','1')
		},
		//上传分类icon
		upLoad2(file) {
			const that = this;
			that.getBease64(URL.createObjectURL(file.file),file.file.type,'1','2')
		},
		getBease64(obj,type,who,flag){
			const that = this;
			var  img;
			img = new Image();
			if(who==1){
				this.selectionloading = true;
			}else if(who==2){
				this.uncheckedloading = true;
			}
			img.onload = function () {

				getUrlBase64(obj,type,function (base) {
					uploadPicBase64({"imgStr": base}).then(res =>{
						if(who==1){
							that.selectionloading = false;
						}else if(who==2){
							that.uncheckedloading = false;
						}
						console.log(res)
						if(res.code == 200){
							if(flag == 1){
								that.dataForm.imageUrl = res.data.url;
							}else if(flag == 2){
								that.dataForm.acIcon = res.data.url;
							}
						}else{
							that.$message.error('上传失败');
						}
					})
				})

			};
			img.src = obj;
		},

		handleAvatarSuccess(res, file) {
			this.imageUrl = URL.createObjectURL(file.raw);
		},
		beforeAvatarUpload(file) {
			var allImageType = 'jpgjpegpngJPGJPEGPNG';
			// const isJPG = file.type === 'image/jpeg';
			var dessnamenum = file.name.indexOf('.') + 1;
			const isJPG = allImageType.indexOf(file.name.substring(dessnamenum)) != -1;
			const isLt2M = file.size / 1024 / 1024 < 10;

			if (!isJPG) {
				this.$message.error('仅支持（jpg,jpeg,png,JPG,JPEG,PNG）为后缀的文件!!');
			}
			if (!isLt2M) {
				this.$message.error('上传头像图片大小不能超过 10MB!');
			}
			return isJPG && isLt2M;
		},


  }
}
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
