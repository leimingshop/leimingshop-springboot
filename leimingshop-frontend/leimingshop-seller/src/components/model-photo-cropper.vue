<template>
	<div class="crop-img">
    <el-dialog
  		title="裁剪图片"
  		:show-close="false"
  		:append-to-body="true"
  		:close-on-click-modal="false"
  		:close-on-press-escape="false"
  		:visible.sync="cropper.dialogVisible"
  		width="30%"
    >
        <vue-cropper
	        ref='cropper'
	        preview="aa"
	        :src="cropper.imgSrc"
	        :aspectRatio="aspectRatio"
	        :autoCropArea="1"
	        :ready="cropImage"
	        :zoom="cropImage"
	        :cropmove="cropImage"
	        style="width:100%;height:300px;"
        >
        </vue-cropper>
		<div class="pre-content">
			<span class="pre-text">预览：</span>
			<img class="pre-img crop-pre-img" :style="{width:imgWidth,height:imgHeight}" :src="cropper.cropImg">
		</div>
	    <span slot="footer" class="dialog-footer">
	        <!-- <el-button type="primary" @click="cropper.dialogVisible = false">确 定</el-button> -->
			 <el-button type="primary" @click="submitImg()">确 定</el-button>
	    </span>
    </el-dialog>


		<div class="upload-box" :style="{width:imgWidth,height:imgHeight}" >
			<img class="pre-img" :src="cropper.cropImg | filterImgUrl" :style="{width:'100%',height:'100%'}" v-if="cropper.imgShow"/>

			<input class="crop-input" type="file" ref="cropInput" name="image" accept="image/*" :value="value" @change="setImage"/>
		    <el-upload
		    :style="{width:imgWidth,height:imgHeight}"
		      class="upload-demo"
		      drag
		      :action="action"
		      :auto-upload="false"
			   v-if="!cropper.imgShow"
		    >
					<i class="el-icon-plus" v-if="continueUpload=='appendImg'" :style="{fontSize:fontSize,lineHeight:lineHeight}"></i>
					<i class="el-icon-upload" v-else :style="{fontSize:fontSize,lineHeight:lineHeight}"></i>
					<!-- <div class="el-upload__text" :style="{width:imgWidth,height:imgHeight}">
						<p v-if="imgWidth == '282px'">
							<span >点击上传图片</span>
							<br/>
							<span>建议图片尺寸282*167</span>
						</p>
					 	 <span style="display:none">{{index}}</span>
					</div> -->
		    </el-upload>


			<!-- 实现删除图片和上传图片功能 -->
			<div  class="hiddenUloadingBox" v-if="cropper.imgShow && cropper.cropImg">
					<div class="hiddenMask">
						<!-- 遮罩层 -->
					</div>
					<div class="buttonFn">
						<!-- <img src="http://bug.leimingtech.com/zentao/file-read-33634.png" alt="上传图片" @click="updataImg">
						<img src="http://bug.leimingtech.com/zentao/file-read-33634.png" alt="删除图片" @click="handleRemove"> -->
						
						<el-button icon="el-icon-zoom-in" 
							class="artbtns"
							@click.stop="previewImg"></el-button>

						<el-button icon="el-icon-upload" 
							class="artbtns"
							@click.stop="updataImg"></el-button>

						<el-button icon="el-icon-delete" 
							v-if="canDelete!=0"
							class="artbtns"
							@click.stop="handleRemove"></el-button>
					</div>
			</div>
		</div>

		<el-dialog
			title="图片预览"
			:append-to-body="true"
			:visible.sync="showPreviewVisible"
			:before-close="closeDialog"
			width="30%"
		>
			<div style="width:100%;text-align: center;">
				<img :src="cropper.cropImg | filterImgUrl" alt="" style="max-width:100%"  >
			</div>
		</el-dialog>
		
	</div>
</template>
<script>
	import VueCropper  from 'vue-cropperjs';
	export default{
		data(){
			return{
				action:`/admin-api/picture/base64`,
				cropper: {
					dialogVisible : false,
					imgSrc: '',
					cropImg: '',
					imgShow: false
				},
				//默认16：9的比例
				//imgWidth:'337.7778px',
				//imgHeight:'190px',
				value:'',
				showPreviewVisible:false,//预览图片
			}
		},
		// props:['index','imgWidth','imgHeight', 'aspectRatio','font-size'],
		props: {
			index: {
				// type: String
			},
			imgWidth: {
				type: String,
			},
			imgHeight: {
				type: String,
			},
			aspectRatio: {
				// type: String,
				// default: '1'
			},
			fontSize:{
				type: String,
				default: '60px'
			},
			lineHeight:{
				type: String,
				default: '50px'
			},
			cropImg:{
				type:String,
				default:''
			},
			canDelete:{
				type:String,
				default:'0'  // 0标识不可以删除，其他标识可以删除
 			},
			continueUpload:{
				type:String,
				default:'firsUpload'  // appendImg继续上传;firsUpload一组中第一个上传
			}
		},
		// 监听图片变化需要回显
		watch:{
			cropImg (newVal,oldVal){
				console.log("newValnewValnewVal");
				console.log(newVal);
				console.log(oldVal);
				this.$nextTick(()=>{
					this.backScanImage(newVal);
				})
			}
		},
		components:{
			VueCropper
		},
		created(){
			
		},
		mounted(){
			this.backScanImage(this.cropImg);	
		},
		methods:{
			init(){
				this.cropper.imgShow = false;
			},
			// 初始回显图片
			backScanImage(cropImg){
				console.log(cropImg);
				if(cropImg){
					this.cropper.imgShow = true;	
					this.cropper.cropImg = cropImg;	
				}else{
					this.cropper.imgShow = false;	
					this.cropper.cropImg = "";
				}
			},
			setImage(e){
				let that = this;
				const file = e.target.files[0];
				if (!file.type.includes('image/')) {
					this.$message.error('仅支持（jpg,jpeg,png,bmp,PNG,JEPG,JPG）为后缀的文件!');
					//   this.value = file
					var cropImg = this.cropper.cropImg;
					this.cropper.cropImg = ""
					this.cropper.cropImg =cropImg;
					this.value = "";
					return;
				}
				if(file.size/(1024*1024) > 6){
					this.$message.error('图片大小不能大于10M');
					//    this.value = file
					var cropImg = this.cropper.cropImg;
					this.cropper.cropImg = ""
					this.cropper.cropImg =cropImg;
					this.value = "";
					return
				}
				const reader = new FileReader();
				reader.onload = (event) => {
					//   this.cropper.dialogVisible = true;
				
						that.cropper.imgShow = true;
						that.cropper.cropImg =  event.target.result;
					//   this.$refs.cropper && this.$refs.cropper.replace(event.target.result);
						that.submitImg();
						that.value = "";
				};
				reader.readAsDataURL(file);
			},
			cropImage () {
				this.cropper.cropImg = this.$refs.cropper.getCroppedCanvas().toDataURL();
				//this.$emit("GiftUrlHandle", this.cropper.cropImg,this.index);
			},
			submitImg(){
				this.cropper.dialogVisible = false;
				this.$emit("GiftUrlHandle", this.cropper.cropImg,this.index);
			},
			// 预览图片
			previewImg(){
				this.showPreviewVisible = true;
			},
			// 关闭预览
			closeDialog(){
				this.showPreviewVisible = false;
			},

			// 上传图片
			updataImg(){
				this.$refs.cropInput.click();
			},
			// 删除图片
			handleRemove() {
				let that = this;
			   this.$confirm('是否确定删除该图片?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$emit("delteteImg",that.index);
				}).catch(() => { 

				})
				
			},
		}
	}
</script>
<style lang="scss" scoped>
	.crop-img{
		.el-dialog__body{
			height: auto;
		}
	}
</style>
<style lang="scss" scoped>
	.pre-img{
		object-fit: fill;
	}
	.crop-img{
		position: relative;
		/deep/ .el-upload{
			width: 100% !important;
			height: 100% !important;
			.el-upload-dragger{
				width: 100% !important;
				height: 100% !important;
				display: flex;
				align-items: center;
				justify-content: center;
				.el-icon-upload{
					margin: 14px 0 16px;
					font-size: 60px;
				}
				.el-icon-plus{
					color:#c7c7c7
				}
			}
		}
	}

	.el-upload-dragger{
		// display: flex;
		// align-items: center;
		// justify-content: center;
	}
	.upload-box{
	//   border:1px solid rgb(204, 198, 198);
		position: relative;
		height: 190px;
		.crop-input{
			width: 100%;
			height: 100%;
			position: absolute;
			z-index: 999;
			top: 0;
			left: 0;
			opacity: 0;
			cursor: pointer;
		}
		img{
			vertical-align: baseline
		}
	}
	.pre-content{
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 30px;
		.crop-pre-img{
			max-width: 100%;
			border: 1px solid #ccc;
			margin: 0 auto;
			height:190px;
		}
	}

  .el-upload,.el-upload-dragger{
	 	width: 100% !important;
	    height: 100% !important;
	}

	.uloadingBox{
		position: relative;
		width: 100%;
		height: 100%;
		// padding: 1px;
	}
	.upload-box:hover > .hiddenUloadingBox{
			display: inline-block;
	}
	.hiddenUloadingBox{
		display: none;
		z-index: 1000;
		position: absolute;
		top: 0;
		left:0;
		width: 100%;
		height: 100%;
		.hiddenMask{
			background: black;
			opacity: 0.3;
			position: absolute;
			top: 0;
			left:0;
			width: 100%;
			height: 100%;
		}
		.buttonFn{
			width: 100%;
			height: 100%;
			display: flex;
			justify-content: center;
			position: relative;
			align-items: center;
			.el-button{
				color:white;
				background: transparent;
    			border: 0;
				width: 30px;
				margin: 3px;
				padding: 0;
				i{
				  transform: scale(1.5);
				}
			}
		}

	}
</style>
