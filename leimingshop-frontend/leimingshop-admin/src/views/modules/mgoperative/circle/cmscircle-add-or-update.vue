<template>
  <el-dialog @close="close()" :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '111px'">
      <el-form-item label="话题名称：" prop="topicName">
          <el-input v-model="dataForm.topicName" placeholder="话题名称" maxlength="40"></el-input>话题格式为#xxxx#
      </el-form-item>
      <el-form-item label="圈子分类：">
        <el-cascader
          v-model="acId"
          :options="acOption"
          @change="caeChange"
          :props="defaultParams"
          change-on-select
          :show-all-levels = "false"
          clearable></el-cascader>
      </el-form-item>
      <el-form-item label="话题描述：" prop="topicAbstract">
          <el-input v-model="dataForm.topicAbstract" placeholder="话题描述"></el-input>
      </el-form-item>
       <el-form-item label="话题logo：" prop="topicLogo">
           <el-upload
                   class="avatar-uploader"
                   style="margin-right:30px;"
                   action="123"
                   :show-file-list="false"
                   :http-request="upLoad1"
                   :on-success="handleAvatarSuccess"
                   :before-upload="beforeAvatarUpload">
               <img v-if="dataForm.topicLogo" :src="$imgDomain+dataForm.topicLogo" class="avatar">
               <i v-else class="el-icon-plus avatar-uploader-icon"></i>
           </el-upload>
      </el-form-item>
      <el-form-item label="话题底图：" prop="topicPicture">
        <el-upload
          class="avatar-uploader"
          style="margin-right:30px;"
          action="123"
          :show-file-list="false"
          :http-request="upLoad2"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="dataForm.topicPicture" :src="$imgDomain+dataForm.topicPicture" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="优先级排序：" prop="sort">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number> 0-255，数字越小优先级排序越靠前
      </el-form-item>
      <el-form-item label="是否启用：" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="话题类型：" prop="superTopicFlag">
        <el-radio-group v-model="dataForm.superTopicFlag">
          <el-radio :label="0">普通话题</el-radio>
          <el-radio :label="1">超级话题</el-radio>
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
import { circleTopicInfoUrl,
	saveCircleTopicUrl,
	updateCircleTopicUrl,
	circleClassListUrl} from "@/api/url";
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        topicName: '',
        acId: '',
        acName: '',
        topicAbstract: '',
        topicLogo: '',
        topicPicture: '',
				sort:'',
        status: 1,
				superTopicFlag: 0,
      },
			acId:[],
			acOption:[],
			defaultParams: {
				label: 'acName',
				value: 'id',
				children: 'children'
			},
    }
  },
  computed: {
    dataRule () {
      return {
        topicName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
					{ message: '请按正确格式输入话题', trigger: ['change','blur'],pattern:/^#([^#]{1,40})#/g },
        ],
				sort: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
				status: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
				superTopicFlag: [
					{ required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
        }
    }
  },
  methods: {
    init () {
			this.getClassListInfo();
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.getInfo();
        }
      })
    },
		close(){
      this.$parent.addOrUpdateVisible=false;
			this.acId = [],
			this.acOption = [];
			this.dataForm = {};
		},
		//改变acId参数格式
		caeChange(item){
			this.dataForm.acId = item[item.length-1];
			this.dataForm.acId = this.dataForm.acId.toString();
		},

    // 获取信息
    getInfo () {
      this.$http.get(circleTopicInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code == 200) {
          this.dataForm = {
          ...this.dataForm,
          ...res.data
          }
          //分类回显
					this.acId = [this.dataForm.acId]
        }else{
          return this.$message.error(res.msg)
        }
      }).catch(() => {})
    },

    //获取分类列表信息
    getClassListInfo () {
      this.$http.get(circleClassListUrl).then(({ data: res }) => {
        if (res.code == 200) {
					// 将获得到的数据赋值给options
					this.acOption=res.data;
          //增加“无”分类
					this.$set(this.acOption, this.acOption.length, {acName: "无", id: "0"});
        } else {
          this.$message({
            type: "warning",
            message: res.msg
          });
        }
      });
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
					this.$http[!this.dataForm.id ? 'post' : 'put'](!this.dataForm.id ? saveCircleTopicUrl : updateCircleTopicUrl, this.dataForm).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
      this.$parent.addOrUpdateVisible=false;

              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false }),

    //上传topicLogo图片
		upLoad1(file) {
			const that = this;
			that.getBease64(URL.createObjectURL(file.file),file.file.type,'1','1')
		},
		//上传topicPicture图片
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
				//       alert(this.width + " " + this.height);

				getUrlBase64(obj,type,function (base) {
					uploadPicBase64({"imgStr": base}).then(res =>{
						if(who==1){
							that.selectionloading = false;
						}else if(who==2){
							that.uncheckedloading = false;
						}
						if(res.code == 200){
							if(flag == 1){
								that.dataForm.topicLogo = res.data.url;
              }else if(flag == 2){
								that.dataForm.topicPicture = res.data.url;
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
