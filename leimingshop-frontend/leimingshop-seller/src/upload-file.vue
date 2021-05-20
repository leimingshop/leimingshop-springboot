<template>
	<div class="upload-file">
		<el-upload
		  ref="uploadFile"
		  class="upload-demo"
		  :class="{hide:hideUpload}"
		  action="*"
		  :accept="accept"
		  :list-type="listType"
		  :multiple="multiple"
		  :limit="limitCount"
		  :file-list="fileList"
		  :auto-upload="autoUpload"
		  :http-request="handleBeforeUpload"
		  :on-exceed="handleExceed"
		  :on-change="handleChange"
		>

			<slot name="uploadBtn">
				<i class="el-icon-plus"></i>
			</slot>

			<div class="el-upload__tip" slot="tip">
				<span>{{uploadTip}}</span>
			</div>

			<div
				slot="file"
				slot-scope="{file}"
				style="width: 100%; height: 100%;"
			>
				<!-- {{ file.percentage }} -->
				<!-- 进度条 -->
        <el-progress
          v-if="file.percentage && file.percentage !== 100"
          :type="listType === 'picture-card' ? 'circle' : 'line'"
          :stroke-width="listType === 'picture-card' ? 6 : 2"
          :percentage="parsePercentage(file.percentage)">
        </el-progress>

        <!-- 文件上传成功 等待状态 -->
        <div
        	class="uploadLoading"
					v-if="file.percentage === 100 && uploadLoading"
        	v-loading="uploadLoading"
        	element-loading-spinner="el-icon-loading"
        ></div>

				<!-- 上传成功样式 -->
				<div class="uploadSuccess" v-if="file.filePath">
					<!-- 缩略图片 -->
		      <el-image
		        class="el-upload-list__item-thumbnail"
		        :src="file.url" alt=""
		        :fit="'contain'"
		        v-if="type == 'img'"
		      ></el-image>

					<!-- 视频 -->
		      <i class="el-icon-video-play"	v-if="type == 'video'"></i>

					<!-- 文档 -->
		      <i class="el-icon-document" v-if="type == 'file'"></i>

					<!-- zip -->
		      <i class="el-icon-folder-opened" v-if="type == 'zip'"></i>

					<!-- 右上角 对勾-->
	        <label class="el-upload-list__item-status-label">
	          <i :class="{
	            'el-icon-upload-success': true,
	            'el-icon-circle-check': listType === 'text',
	            'el-icon-check': ['picture-card', 'picture'].indexOf(listType) > -1
	          }"></i>
	        </label>

	        <!-- 预览，删除 -->
		      <span class="el-upload-list__item-actions">
		        <span
		          class="el-upload-list__item-preview"
		          @click="handlePictureCardPreview(file)"
		        >
		          <i class="el-icon-zoom-in"></i>
		        </span>

		        <span
		          class="el-upload-list__item-delete"
		          @click="handleRemove(file)"
		        >
		          <i class="el-icon-delete"></i>
		        </span>
		      </span>
				</div>

				<!-- 文件名 -->
	      <span class="flieName" v-if="flieName">{{file.name}}</span>
	    </div>
		</el-upload>

		<!-- 预览弹窗 -->
		<el-dialog
			title="预览文件"
			width="48%"
			class="upload-preview-dialog"
			:close-on-click-modal="false"
			:append-to-body="true"
			:visible.sync="dialogVisible"
			@close="closeDialgoHandle"
		>
		  <el-image
		  	width="100%"
		  	:src="dialogFileUrl"
		  	v-if="type == 'img' || type == 'file'"
		  >
	   		<div slot="error" class="image-slot">
	        <i class="el-icon-picture-outline"></i>
	      </div>
		  </el-image>

      <video
      	class="video-js vjs-big-play-centered"
        :id="myPlayerId"
      	v-if="type == 'img'"
      >
        <source :src="dialogFileUrl" type="video/mp4"/>
      </video>

			<iframe
				style="width:100%; height: 750px;"
				:src="dialogFileUrl"
				frameborder="0"
				v-if="type == 'video' || type == 'zip'"
			></iframe>

			<div v-if="type == 100">
				<a href="dialogFileUrl">下载附件</a>
			</div>

  	 	<div slot="footer" class="dialog-footer">
  	    <el-button
	  	    type="primary"
	  	    @click="dialogVisible = false, closeDialgoHandle()"
  	    >关闭</el-button>
  	  </div>

    </el-dialog>
	</div>

</template>

<script>
	import { 
		uploadPicBase64, 
		uploadPicBase64Url, 
		uploadFile,
		uploadFileUrl
	} from '@/api/api'

	export default{
		name: 'uploadFile',

		props: {

			// 文件列表的类型 text/picture/picture-card
			'listType' : {
				type: String,
				default: 'picture-card'
			},

			// 是否支持多选文件
			'multiple' : {
				type: Boolean,
				default: true
			},

			// 最大上传文件数
			'limitCount' : {
				type: Number,
				default: 1
			},

			// 是否自动上传
			'autoUpload' : {
				type: Boolean,
				default: true
			},

			// 上传文件大小限制（最大）
			'maxSize':{
				type: Number,
				default: 1000000
			},

			// 慕课:mooc_course-cover_img,公开课:open_course-cover_img,type为0或3是必填
			'module' : {
				type: String,
				default: ''
			},

			/*
				* 上传类型
				* img - 图片
				* video - 视频
				* file - pdf或word或xml或ppt
				× zip - zip文件夹
			*/

			'type' : {
				type: String,
				default: 'img'
			},

			// 上传提示
			'uploadTip': {
				type: String,
				default: ''
			},

			// 编辑时的文件列表
			'fileList' : {
				type: Array,
				default: () => {return []}
			},

			// 文件名是否显示
			'flieName' : {
				type: Boolean,
				default: false
			},

			// 校验文件名称是否由某种规则命名
			'checkFileNameObj' : {
				type: Object,
				default: () => {return {}}
			}
		},

		data(){
			return{
				dialogVisible: false,
				myPlayer: null,
				myPlayerId: `basic_item_video_${Date.now()}`,
				hideUpload: false,
				uploadLoading: false,
				dialogFileUrl: '', // 预览地址
				accept: '' //文件限制类型
			}
		},

		computed:{
			// 上传文件地址 （必填参数）
			uploadFileUrl(){
				if( this.type == 'img' ){
					return uploadPicBase64Url
				}
				else {
					return uploadFileUrl
				}
			}
		},

		watch:{
			fileList: {
				immediate: true,
				handler(newVal, oldVal){
					// 是否隐藏上传按钮
					this.hideUpload = newVal.length >= this.limitCount

					// 上传提示
					let uploadTip
					// 文件类型
					switch (this.type) {
						case 'img':
							this.accept = 'image/jpeg,image/jpg,image/png'
							uploadTip = '*支持JPG,PNG,JPEG格式的图片'
							break;

						case 'video':
							this.accept = 'audio/mp4,video/mp4'
							uploadTip = '*支持MP4格式的视频'
							break;

						case 'file':
							// .pdf, .doc（文档第一种类型）, .doc（文档第二种类型）, .ppt,.pptx,.xls,.xlsx,.docx --对应
							this.accept=` application/pdf,
														application/msword,
														application/wps-writer,
														application/vnd.ms-powerpoint,
														application/vnd.openxmlformats-officedocument.presentationml.presentation,
														application/vnd.ms-excel,
														application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
														application/vnd.openxmlformats-officedocument.wordprocessingml.document `
							uploadTip = '*说明：支持Word,Excel,PPT。'
							break;

						case 'zip':
							this.accept=` application/zip,
														application/x-zip,
														application/x-zip-compressed `
							break;
					}
					this.$emit('update:uploadTip',  this.uploadTip || uploadTip)
				}
			},

			$route(){
				this.myPlayerId = `basic_item_video_${Date.now()}`
			}

		},

		mounted(){
			console.log(this.$refs)
		},

		methods:{
	    // file对象转base64
	    handleFileToBase64(file) {
		    let that = this,
		        reader = new FileReader();
		    reader.readAsDataURL(file);
      	return new Promise((resolve, reject) => {
	        reader.onload = function(e) {
		        //这里是一个异步，所以获取数据不好获取在实际项目中，就用new Promise解决
		        if (this.result) {
		            resolve(this.result);
		        } else {
		            reject("err");
		        }
	        };
      	});
	    },

			// 上传文件之前的钩子
			async handleBeforeUpload(params){
				const file = params.file
    		const isLtSize = file.size / 1024 / 1024  < this.maxSize
    		
		    if (this.accept && this.accept.indexOf(file.type) == -1) {
	        this.$message.error('请上传正确的格式文件')
	        this.hideUpload = false
	        return false;
		    }

		    // 校验文件名称是否由某种规则命名
		    if( this.checkFileNameObj.rule ){
		    	if ( !this.checkFileNameObj.rule.test(file.name) ){
	          this.$message.warning(`${file.name}${this.checkFileNameObj.warningTip}`);
	          this.hideUpload = false
	          return false;
		    	}
		    }

		    if (!isLtSize) {
	        this.$message.error(`上传视频大小不能超过${this.maxSize}MB哦!`)
	        this.hideUpload = false
	        return false
		    }

		    this.uploadLoading = true

		    const _self = this

 				let config = {
            onUploadProgress: ProgressEvent => {
              let progressPercent = _self.parsePercentage(ProgressEvent.loaded / ProgressEvent.total * 100)
              let fileList = _self.$refs.uploadFile.$refs['upload-inner'].fileList
              let curIndex = fileList.findIndex( (item, index, arr) => { return item.uid == file.uid } )
              _self.$refs.uploadFile.$refs['upload-inner'].fileList[curIndex].percentage = progressPercent
            }
        }

		    if( this.type == 'img' ){
		    	this.handleUploadImg(file, config)
		    } else {
		    	this.handleUploadFile(file, config)
		    }
			},

			// 上传图片文件时的钩子
			async handleUploadImg(file, config){
				let base64File =  await this.handleFileToBase64(file)

				let params = {
					type: 1,
					imgStr: base64File
				}

				const res = await uploadPicBase64(params, config)

				this.handleSuccess(res, file)
			},

			// 上传其他文件的钩子
			async handleUploadFile(file, config){
				var form = new FormData();
				form.append('file', file);
				const res = await uploadFile(form, config)
				this.handleSuccess(res, file)
			},

			// 上传文件成功后的钩子
			handleSuccess(res, file){

				this.uploadLoading = false

				if(res.code == 200){
					let tempFileList = this.fileList;
					tempFileList.push({
						url: this.$imgDomain + res.data.url,
						name: file.name,
						filePath: res.data.url
					})

					this.dialogFileUrl = this.$imgDomain + res.data.url
					// 同步表单值方法
					this.$emit('update:fileList', tempFileList)
					this.$emit('handleUploadCallback', tempFileList)
				} else {
					this.$message.warning(res.message)
					this.$refs.uploadFile.clearFiles()
				}
			},

			// 预览上传成功的文件时的钩子
      async handlePictureCardPreview(file) {

      	this.dialogVisible = true
      	this.dialogFileUrl = file.url
      	await this.$nextTick()

      	if( this.type == 1 ){
					if (!this.myPlayer) {
	      		this.initVideo(this.myPlayerId)
					} else {
						this.myPlayer.src(this.dialogFileUrl)
					}
      	}

      },

      // 删除上传成功的文件时的钩子
   		handleRemove(file) {
   			const fileName =  file.name ? `"${file.name}"` : '该文件'

				this.$confirm(`确认要移除${fileName}吗？`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then( () => {

					let param = this.fileList.filter( item => {return item.url != file.url})
					// 更新文件列表
					this.$emit('update:fileList', param)
					this.$emit('handleUploadCallback', tempFileList)
					// 更新弹窗播放路径 为空
        	this.dialogFileUrl = ''
				}).catch( () => {

				})

      },

      //文件状态改变时的钩子 添加文件、上传成功和上传失败时都会被调用
      handleChange(file, fileList){
      	this.hideUpload = fileList.length >= this.limitCount
      },

      // 文件超出个数限制时的钩子
      handleExceed(files, fileList){
				this.$message.warning(`当前限制选择 ${this.limitCount} 个文件，本次选择了 ${files.length} 个文件，请重新上传`);
      },

      // 初始化播放器
	    initVideo(myVideo) {
	      //初始化视频方法
	      this.myPlayer = this.$video(myVideo, {
	        //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
	        controls: true,
	        //自动播放属性,muted:静音播放
	        autoplay: "muted",
	        //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
	        //preload: "auto",
	        //设置视频播放器的显示宽度（以像素为单位）
	        width: "634px",
	        //设置视频播放器的显示高度（以像素为单位）
	        //height: "auto"
	      })

	      // 解决 视频全屏脱离文档时 排版错乱问题
				$(document).on('click', '.vjs-fullscreen-control', function(){
					$('.upload-preview-dialog').css('opacity', 0)
					setTimeout( function(){
						$('.upload-preview-dialog').css('opacity', 1)
					}, 500)
				})
	    },

      // 格式化视频上传进度
      parsePercentage(val) {
        return parseInt(val, 10)
      },

			// 关闭弹窗
			closeDialgoHandle(){
				if( this.myPlayer ){
					this.myPlayer.pause() // 暂停
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.hide{
		/deep/ .el-upload--picture-card {
		  display: none;
		}
	}

	/deep/ .el-upload__tip{
    color: #606266;
    line-height: 30px;
    font-size: 14px;
    font-weight: bold;
    color: #afafaf;
    text-align: left;
    width: 800px;
    z-index: 0;
	}

	/deep/ .el-upload-list--picture-card{
		.el-upload-list__item{
			overflow: visible;
			transition: none !important;
			.flieName{
				width: 800px;
				display: block;
				position: absolute;
				bottom: -28px;
			}

			.uploadLoading,
			.uploadSuccess{
				width: 100%;
				height: 100%;
				background: #f5f7fa;
				position: absolute;
				top: 0;
				left: 0;
		    border-radius: 5px;
		    overflow: hidden;
			}

			.uploadLoading{
				font-size: 20px;
			}
		}

		.el-upload-list__item-thumbnail{
			.el-image__inner{
				width: 100%;
				height: 100%;
			  margin-top: 50%;
			  vertical-align: middle;
		    transform: translateY(-50%);
			}

		}
		.el-icon-video-play,
		.el-icon-document,
		.el-icon-folder-opened{
    	color: #909399;
	    font-size: 30px;
	    width: 30px;
	    display: block;
	    margin: 50%;
	    transform: translate(-50%, -50%);
		}
	}
	/deep/ .el-upload--picture-card {
		position: relative;
		.el-icon-plus{
			width: 28px;
			height: 28px;
			display: block;
			position: absolute;
			top: -2px;
			bottom: 0;
			left: 0;
			right: 0;
			margin: auto;
		}
	}

</style>

<style lang="scss">
	.upload-preview-dialog{
		.el-dialog{
			min-width: 480px;
			max-width: 600px;
		}
		.el-dialog__body{
			padding: 30px;
			border-bottom: 1px solid #EEEEEE;
			text-align: center;
			.video-js{
				max-width: 100%;
				overflow: hidden;
			}
		}
		.el-dialog__footer{
			padding: 20px 30px;
		}
	}
</style>