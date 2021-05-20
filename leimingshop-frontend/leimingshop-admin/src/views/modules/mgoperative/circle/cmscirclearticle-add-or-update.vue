<template>
  <el-dialog @close="close()" :visible.sync="visible" :title="$t('详情')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '111px'">

      <el-form-item label="发布类型：" prop="articleFlag">
        <el-input v-model="dataForm.articleFlag" placeholder="发布类型"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="文章标题：" prop="articleTitle" v-if="dataForm.articleTitle">
        <el-input v-model="dataForm.articleTitle" placeholder="文章标题" v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="圈子分类：" prop="acName">
        <el-input v-model="dataForm.acName" placeholder="圈子分类"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="圈子话题：" prop="topicName">
        <el-input v-model="dataForm.topicName" placeholder="圈子话题"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="创建人：" prop="creator">
        <el-input v-model="dataForm.creator" placeholder="创建人"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="图片：" prop="articlePicture" v-if="dataForm.articlePicture">
        <!-- <el-input v-model="dataForm.articlePicture" placeholder="图片"  v-bind:readonly="true"></el-input> -->
		<div class="imgs">
			<img v-for="(picitem,picindex) in dataForm.articlePicture" :key="picindex" :src="$imgDomain+picitem" alt="">
		</div>
		<!-- <img :src="$imgDomain" alt=""> -->
      </el-form-item>
      <el-form-item label="视频：" prop="videoUrl" v-if="dataForm.videoUrl">
        <!-- <el-input v-model="dataForm.videoUrl" placeholder="视频"  v-bind:readonly="true"></el-input> -->
		<video class="pics" :src="$imgDomain+dataForm.videoUrl" @click="tovideo($imgDomain+dataForm.videoUrl)"></video>
      </el-form-item>
      <el-form-item label="封面图：" prop="indexImage" v-if="dataForm.indexImage">
        <!-- <el-input v-model="dataForm.indexImage" placeholder="封面图"  v-bind:readonly="true"></el-input> -->
		<img class="pics" :src="$imgDomain+dataForm.indexImage" alt="">
      </el-form-item>
      <el-form-item label="文章内容：" prop="articleContent">
		<div v-if="dataForm.articleFlag == '文章'">
			<div v-for="(contentitem,contentIndex) in dataForm.articleContent" :key="contentIndex">
				<div v-if="contentitem.type == 'str'">{{contentitem.content}}</div>
				<div v-if="contentitem.type == 'img'">
					<img class="listpics" :src="$imgDomain+contentitem.content" alt="">
				</div>
				<div v-if="contentitem.type == 'goods'" class="goodsitem">
					<img class="goodspic" :src="$imgDomain+contentitem.goodsMainPicture">
					<div class="goodsRight">
						<div class="goodstitle">{{contentitem.goodsName}}</div>
						<div style="display:flex;align-items: center;">
							<div style="color:#DD2619;font-size:12px;">￥</div>
							<div class="price">{{contentitem.specSellPrice}}</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div v-if="dataForm.articleFlag != '文章'">
        	<!-- <el-input v-model="dataForm.articleContent"  v-bind:readonly="true"></el-input> -->
			<div>{{dataForm.articleContent}}</div>
		</div>


      </el-form-item>
    </el-form>

    <template slot="footer">
      <el-button @click="visible = false">{{ $t('返回') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
	import debounce from 'lodash/debounce'
	import quillEditorImg from "@/components/quillEditor"
	import {circleArticleInfoUrl} from '@/api/url'
	export default {
		data () {
			return {
				visible: false,
				dataForm: {
					id:'',
				},
			}
		},
		components:{
			quillEditorImg,
		},
		created() {
			this.visible=true;
			// this.init();
		},
		methods: {
			//文章内容长度限制
			validatorValue (rule, value, callback) {
				var value2 = value.replace(/<\/p>/g,"").replace(/<p>/g,"").replace(/&nbsp;/g,"")
				value2 = value2.replace(/ /g,"")
				// value2 = value2.replace("</p>","")
				// value2 .replace("<p>","")
				console.log(value2.length)
				console.log(value2)
				if (value2.length>3000) {
					callback(new Error('模板内容长度限制为3000个字符'))
				}else {
					callback()
				}
			},
			//富文本只读
			onEditorFocus(val,editor){ // 富文本获得焦点时的事件
				editor.enable(false); // 在获取焦点的时候禁用
			},

			close(){
				this.visible=false;
				this.$refs.myTextEditor.dataForm.messageContent = '';
			},
			tovideo(url){
				window.open(url)
			},
			init (id,val) {
				console.log('???????????',id,val)
				this.visible = true
				this.$nextTick(() => {
					this.$refs['dataForm'].resetFields()
					if (this.dataForm.id) {
						this.getInfo()
					}
				})
			},
			// 获取文章信息
			getInfo () {
				this.$http.get(circleArticleInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
					if (res.code !== 200) {
						return this.$message.error(res.msg)
					}
					this.dataForm = {
						...this.dataForm,
						...res.data
					}
					if(res.data.articleFlag == 1){
						this.dataForm.articleFlag = '文字'
					}
					if(res.data.articleFlag == 2){
						this.dataForm.articleFlag='图说'
					}
					if(res.data.articleFlag == 3){
						this.dataForm.articleFlag='视频'
					}
					if(res.data.articleFlag == 4){
						this.dataForm.articleFlag='文章'
					}
					if(res.data.articlePicture){
						this.dataForm.articlePicture = JSON.parse(res.data.articlePicture)
					}
					if(res.data.articleFlag == 4){
						this.dataForm.articleContent = JSON.parse(res.data.articleContent);
						console.log('===========',this.dataForm.articleContent)
					}
					// this.$refs.myTextEditor.dataForm.messageContent =this.dataForm.articleContent
				}).catch(() => {})
			},

			//富文本内容
			artmessageContent(messageContent){
				this.dataForm.articleContent = messageContent;
			},


		}
	}
</script>
<style lang="scss" scoped>
    .pics{
		width: 300px;
		height: 180px;
	}
	.imgs{
		width: 100%;
		display: flex;
		flex-wrap: wrap;
		img{
			width: 150px;
			height: 120px;
			margin-right: 10px;
			margin-bottom: 10px;
			&:nth-child(3n+3){
				margin-right: 0;
				
			}
		}
	}
	.listpics{
		width: 60%;
		height: 170px;
	}
	.goodsitem{
		width: 100%;
		display: flex;
		.goodspic{
			width: 80px;
			height: 80px;
			margin-right: 10px;
			margin-bottom: 10px;
		}
		.goodsRight{
			flex: 1;
			height: 100&;
			display: flex;
			flex-direction: column;
			padding: 12px 0;
			justify-content: space-between;
			
			.goodstitle{
				width: 100%;
				text-overflow: -o-ellipsis-lastline;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				line-clamp: 2;
				-webkit-box-orient: vertical;
				color: #1D1D1D;
				font-size: 14px;
			}
			.price{
				font-size: 11px;
				color: #999;
			}
		}
	}

</style>
