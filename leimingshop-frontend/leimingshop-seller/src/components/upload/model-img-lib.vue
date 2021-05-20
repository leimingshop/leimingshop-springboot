<template>
		<el-dialog
			:visible.sync="visible"
			:title='title'
			:append-to-body="true"
			:before-close='close'
			:close-on-click-modal="false"
  			:close-on-press-escape="false"
		>
		   <!-- <el-button type="primary" size="mini"  @click="upload">上传图品</el-button> -->
			<div style="height:45px;">	
				<el-upload
					class="upload-demo"
					:action='url'
					multiple
					:limit="100"
					:show-file-list="false"
					:headers="myHeaders"
					:before-upload="beforeAvatarUpload"
					:on-exceed="handleExceed"
					:http-request="httpRequest">
					<!--
						:on-progress="handleProgress"
						:on-success="uploadSuccess"  
						:on-error="uploadError" 
					-->
					<el-button size="small" type="primary">点击上传</el-button>
					<!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
				</el-upload>
				<el-progress :percentage="percentage" v-if="startUpload"></el-progress>
			</div>

			 <div>
				<div class="imageWarp"  v-loading="dataListLoading">
					<div class="imageItem" v-for="(item,index) in dataList" @click="chooseImg(item)" v-if="item.picturePath">
						<img :src="item.picturePath | filterImgUrl" alt="">
					</div>
				</div>
			</div>
				<el-pagination
				style="display:flex;flex-wrap: wrap;"
				:current-page="page"
				:page-sizes="[10, 20, 50, 100]"
				:page-size="limit"
				:total="total"
				layout="total, sizes, prev, pager, next, jumper"
				@size-change="pageSizeChangeHandle"
				@current-change="pageCurrentChangeHandle">
			</el-pagination>

		</el-dialog>
</template>


<script>
import mixinViewModule from '@/mixins/view-module'
import Cookies from 'js-cookie'
import { getallimages } from "@/api/url"
import { uploadPicBase64,uploadFile  } from "@/api/api"
export default {
	mixins: [mixinViewModule],
    data(){
        return {
			mixinViewModuleOptions: {
				getDataListURL: getallimages,
				activatedIsNeed:false,
				getDataListIsPage: true,
				//   // exportURL: '/admin-api/log/login/export',
				//   deleteURL: deleteAttributeUrl,
				//   deleteIsBatch: true,
				//   deleteIsBatchKey: 'id'
			},
			visible:false,
			title:"我的图库",
			url:"",
			dataListLoading:false,
			myHeaders: {},//Cookies.get(teacher_token)
			uploadLoading:false,// 上传loading
			progress:0,// 上传进度
			fileReader:new FileReader(),
			percentage:0,
			startUpload:false,
        }
	},
	props:{
		
	},
	commponents:{

    },
    created() {
	},
	mounted(){
		 this.myHeaders ={"ADMIN-TOKEN":Cookies.get('ADMIN-TOKEN')} ;
    },
    methods:{
        init(){
			// console.log("弹框出来后会走这里");
			this.visible = true;
			this.percentage = 0;
			this.startUpload = false;

			this.fileList = [];
			this.getDataList().then((res)=>{
				// console.log(this.dataList);
			})
		},
		handleExceed(files, fileList) {
			this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
		},
		// 参考https://www.jianshu.com/p/2600ac58fa4b
		httpRequest (options) {
			console.log("httpRequest");
			console.log(options);
			let file = options.file
			if(/video/.test(file.type) ){ // 上传视频
				// this.url = `${window.SITE_CONFIG['apiURL'] }/admin-api/upload/upload`;
				this.uploadVideo(options);
			}else if(/image/.test(file.type)){ // 上传图片
				// this.url = `${window.SITE_CONFIG['apiURL'] }/admin-api/picture/base64`
				this.uploadImg(options);

			}

			return;
			// let file = options.file
			// let filename = file.name
			// if (file) {
			// 	this.fileReader.readAsDataURL(file)
			// }
			// this.fileReader.onload = () => {
			// 	let base64Str = this.fileReader.result
			// 	let config = {
			// 	url: '/api/file/upload/base64',
			// 	method: 'post',
			// 	// file: file,
			// 	data: {
			// 		base64Str: base64Str.split(',')[1],
			// 		name: filename
			// 	},
			// 	timeout: 10000,
			// 	onUploadProgress: function (progressEvent) {
			// 		// console.log(progressEvent)
			// 		progressEvent.percent = progressEvent.loaded / progressEvent.total * 100
			// 		options.onProgress(progressEvent, file)
			// 	},
			// 	}
			// 	axios(config)
			// 	.then(res => {
			// 		options.onSuccess(res, file)
			// 	})
			// 	.catch(err => {
			// 		options.onError(err)
			// 	})
			// }
		},
		// 上传图片
		uploadImg(options){
			let file = options.file;
			if (file) {
				this.fileReader.readAsDataURL(file)
			}
			this.fileReader.onload = () => { 
				let base64 = this.fileReader.result
				const params = { "imgStr": base64 };
				this.percentage = 0;
				this.startUpload = true;
				 params.type=1
				uploadPicBase64(params,this.progressCb).then((res)=>{
					//  options.onSuccess(res, file)
					this.startUpload = false
					if(res.code==200){
					//   this.$message.success(res.msg);
					//   console.log("上传成功耨返回的url:"+res.data.url)
						this.getDataList();
					}else{
						this.$message.error(res.msg);
					}
				}).catch(err => {
					// options.onError(err)
					this.$message.error("上传失败，请重新上传");
				})
			}
			
		},
		// 上传视频
		uploadVideo(options){
			var form = new FormData();
			let file = options.file;
			form.append('file', file);
			this.percentage = 0;
			 this.startUpload = true;
			uploadFile(form,this.progressCb).then((res)=>{
				this.startUpload = false
				if(res.code==200){
					this.getDataList();
				}else{
					this.$message.error(res.msg);
				}
			}).catch(err => {
				// options.onError(err)
				this.$message.error("上传失败，请重新上传");
			})
		},
		// 上传进度
		progressCb(progressEvent){
			console.log("13213213");
			console.log(progressEvent)
			this.percentage  = parseInt(progressEvent.loaded / progressEvent.total)  * 100
		},
		// 上传之前
		beforeAvatarUpload(file) {
		//   console.log("beforeAvatarUpload");
		//   console.log(file);
          this.progress = 0;
          console.log(file);
          const isimageOrvideo =  /video/.test(file.type) || /image/.test(file.type);
          const isLt2M = file.size / 1024 / 1024 < 10;

			// if(/video/.test(file.type) ){ // 上传视频
			// 	this.url = `${window.SITE_CONFIG['apiURL'] }/admin-api/upload/upload`;
			// }else if(/image/.test(file.type)){ // 上传图片
			// 	this.url = `${window.SITE_CONFIG['apiURL'] }/admin-api/picture/base64`
			// }
          if (!isimageOrvideo) {
            this.$message.error('只能上传图片或者视频');
          }
          if (!isLt2M) {
            this.$message.error('视频大小不能超过 10MB!');
          }
          return isimageOrvideo && isLt2M;
		},
		// 选择图片
        chooseImg(item){
			console.log(item);
			this.$emit("GiftUrlHandle",item.picturePath);
			this.close();
		},
		// 关闭前走的函数
        close(){
			this.visible=false;
			this.$parent.showImgLibVisible = false;
        }
    },
}
</script>
<style lang="scss" scoped>
.imageWarp{
  min-height: 100px;
  display: flex;
  flex-wrap: wrap;
  .imageItem{
      width:100px;
      height:100px;
      line-height: 100px;
      display: inline-block;
      border: 1px solid #979797;
      margin: 10px 10px;
      overflow: hidden;
      img{
        width:98%;
        height:auto;
      }
  }
}
.upload-demo{
	width:80px;
}
</style>