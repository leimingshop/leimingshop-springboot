<template>
	<div class="crop-img">
		<div @click="showImgLib" :style="{width:imgWidth,height:imgHeight}" class="upload-box" >
			<img :src="cropImg | filterImgUrl"  :style="{width:'100%','object-fit': 'cover'}" v-if="cropImg" />
			<!-- <img class="pre-img" src="~@/assets/images/default.png"  />	 -->
			<!-- <i class="icon el-icon-plus" v-else-if="continueUpload=='appendImg'" :style="{width:imgWidth,height:imgHeight,border: '1px dashed #d9d9d9',fontSize:fontSize}" ></i>
			<i class="icon el-icon-upload" v-else :style="{width:imgWidth,height:imgHeight,border: '1px dashed #d9d9d9',fontSize:fontSize}" ></i>
			 -->
			<i class="icon el-icon-plus"  v-else :style="{width:imgWidth,height:imgHeight,border: '1px dashed #d9d9d9',fontSize:fontSize}" ></i>
		
			<!-- 实现删除图片和上传图片功能 -->
			<div  class="hiddenUloadingBox" v-if="needHover!='0' && cropImg">
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
							@click.stop="showImgLib"></el-button>

						<el-button icon="el-icon-delete" 
							v-if="canDelete!=0"
							class="artbtns"
							@click.stop="handleRemove"></el-button>
					</div>
			</div>
		
		</div>
	
		<!-- <modelImgLib 
			v-if="showImgLibVisible" 
			ref="modelImgLibCompon"
			@GiftUrlHandle="GiftUrlHandle"
			>
		</modelImgLib> -->
		
		<!-- 从图片库选择 -->
		<picturesLib v-if="picturesLibVisible" ref="picturesLibCompon" @GiftUrlHandle="GiftUrlHandle"></picturesLib>
	
		<!-- 图片预览 -->
		<el-dialog
			title="图片预览"
			:append-to-body="true"
			:visible.sync="showPreviewVisible"
			:before-close="closeDialog"
			width="30%"
		>
			<div style="width:100%;text-align: center;">
				<img :src="cropImg | filterImgUrl" alt="" style="max-width:100%; object-fit: cover"  >
			</div>
		</el-dialog>

	</div>
</template>
<script>
// import  modelImgLib from  "./model-img-lib.vue"
import  picturesLib from "./pictures/index.vue"
export default {
    data(){
        return {
			// showImgLibVisible:false,
			picturesLibVisible:false,
			showPreviewVisible:false,
        }
	},
	props:{
		index: {
				// type: String
		},
		imgWidth: {
			type: String,
		},
		imgHeight: {
			type: String,
		},
		fontSize:{
			type: String,
			default: '60px'
		},
		// lineHeight:{
		// 		type: String,
		// 		default: '50px'
		// },
		cropImg:{
			type:String,
			default:''
		},
		needHover:{
			type:String,
			default:'0'  // 0标识鼠标不能悬停，1标识鼠标能悬停
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
	components:{
		picturesLib,
		
    },
    created() {
	},
	mounted(){
    },
    methods:{
        init(){
        },
        submit(){
         
		},
		// 展示图库选择弹框
		showImgLib(){
			// this.showImgLibVisible = true;
			// this.$nextTick(()=>{
			// 	this.$refs.modelImgLibCompon.init();
			// })
			this.picturesLibVisible = true;
			this.$nextTick(()=>{
				this.$refs.picturesLibCompon.init();
			})
		},
		GiftUrlHandle(imagUrl){
			this.$emit("GiftUrlHandle",imagUrl,this.index);
		},
		// 预览图片
		previewImg(){
			this.showPreviewVisible = true;
		},
		// 关闭预览
		closeDialog(){
			this.showPreviewVisible = false;
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
    },
}
</script>
<style lang="scss" scoped>
	.crop-img{
		position: relative;
		.icon{
			display: flex;
			align-items: center;
			justify-content: center;
		}
		.el-icon-upload{
			// margin: 14px 0 16px;
			font-size: 60px;
			color:#c7c7c7
		}
		.el-icon-plus{
			color:#c7c7c7
		}

		.upload-box{
		//   border:1px solid rgb(204, 198, 198);
			position: relative;
			overflow: hidden;
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

	}
</style>