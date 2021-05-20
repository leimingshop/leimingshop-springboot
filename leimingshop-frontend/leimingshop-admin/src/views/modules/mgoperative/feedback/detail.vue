<template>
  <div>
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
	<div style="display:flex;flex-direction: row;">
		<div style="margin-left: 12%;display: flex;flex-direction: column;float:left;width:45%;">
			<div style="margin-left: 75px;float:left;">
			<h3>反馈内容</h3>
			<div>
				<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;">
					<!--前两行左右对齐-->
					<div style="display: flex;justify-content: space-between;align-items: center">
						<div>
							<div>会员账号：{{detail.memberName}}</div>
							<div style="margin-top: 20px;">联系人电话：{{detail.mobileNumber}}</div>
						</div>
						<div style=" font-size:13px;color:#333;">
							<div>反馈类型：
								<span v-if="detail.feedbackType==1">功能异常</span>
								<span v-if="detail.feedbackType==2">优化建议</span>
								<span v-if="detail.feedbackType==3">其他反馈</span>
							</div>
							<div style="margin-top: 20px;">反馈来源：
								<span v-if="detail.feedbackSource==0">PC</span>
								<span v-if="detail.feedbackSource==1">H5</span>
								<span v-if="detail.feedbackSource==2">android</span>
								<span v-if="detail.feedbackSource==3">ios</span>
								<span v-if="detail.feedbackSource==4">微信</span>
								<span v-if="detail.feedbackSource==5">小程序</span>
							</div>
						</div>
					</div>

					<div style="margin-top: 20px;">反馈时间：{{detail.createDate}}</div>
					<div style="margin-top: 20px;display: flex;width:520px;">
						<label style="width:75px;	">反馈内容：</label>
						<el-input
								:disabled="true"
								type="textarea"
								:rows="4"
								v-model="detail.detailedDescription">
						</el-input>
					</div>
					<div style="margin-top: 20px;display: flex;width: 570px;">反馈图片：
						<div style="display: flex;">
							<div  v-for="(item,index) in this.picture">
								<div style="width:96px">
									<img :src="item | filterImgUrl" alt="" style="width:70px;height:70px;border-radius: 6px;object-fit: scale-down;"  	@click="previewBigImg(item,index)">
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>
		</div>
		<div style="margin-left: 75px;float:left;margin-top: 30px;">
			<h3>反馈处理</h3>
			<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;float:left;width:40%;">
				<div v-if="detail.disposeStatus==1">&nbsp;&nbsp;&nbsp;&nbsp;处理人：{{detail.operator}}</div>
				<div v-if="detail.disposeStatus==1" style="margin-top: 20px;">反馈判定：
					<span v-if="detail.decideType==0">无效反馈</span>
					<span v-if="detail.decideType==1">有效反馈</span>
					<span v-if="detail.decideType==2">重点问题</span>
				</div>
				<div v-if="detail.disposeStatus==0" style="margin-top: 20px;display: flex;flex-direction: row;width: 400px;">反馈判定：
					<el-radio-group v-model="status">
					<el-radio v-model="status" label="0">无效反馈</el-radio>
					<el-radio v-model="status" label="1">有效反馈</el-radio>
					<el-radio v-model="status" label="2">重点问题</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-top: 20px;display: flex;width:520px;">
					<label style="width:86px;	">
						<span  v-if="detail.disposeStatus==0" style="color:red">*</span>
						<span  v-if="detail.disposeStatus==1" >&nbsp;</span>
						客服回复：</label>
					<el-input
						:disabled="flag"
						type="textarea"
						:rows="4"
						maxlength="500"
						placeholder="请输入您对该反馈的处理内容，最多可输入500字"
						v-model="servicesReply"
						show-word-limit>
						</el-input>
				</div>
				<div style="width: 321px;margin-left: 75px;margin-top: 7px;color: #aca9a8;">客服回复的内容将会显示在会员端的反馈处理结果中。</div>
				<div style="margin-top: 20px;display: flex;width:520px;">
					<label style="width:86px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注：</label>
					<el-input
						:disabled="flag"
						type="textarea"
						:rows="4"
						maxlength="500"
						v-model="remark"
						placeholder="请输入备注，最多可输入500字"
						show-word-limit
						>
						</el-input>
				</div>
				<div style="width: 390px;margin-left: 75px;margin-top: 7px;color: #aca9a8;">备注用于记录该反馈的处理方式、处理方法等，不会显示在会员端。</div>

			</div>
			<div v-if="detail.disposeStatus==1" style=" font-size:13px;color:#333;">
				<div style="margin-top: 20px;">处理时间：{{detail.disposeDate}}</div>
			</div>
		</div>
		</div>
		<div style="clear:both"></div>
	</div>
	<div style="display:flex;flex-direction: row;margin-top:30px;justify-content: center;">
		<el-button  type="primary"  @click="changePage()">
			<span v-if="detail.disposeStatus==1">返回</span>
			<span v-if="detail.disposeStatus==0">取消</span>
		</el-button>
		<el-button v-if="detail.disposeStatus==0" type="primary"  @click="save()">处理</el-button>
	</div>
	</div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import { informationlist,informationdelete } from '@/api/url'
import Bread from "@/components/bread";
export default {
  mixins: [mixinViewModule],
	data () {
	    return {
			mixinViewModuleOptions: {
				getDataListURL: informationlist,
				getDataListIsPage: true,

			},
			breaddata: ["运营管理", "客服管理", "反馈列表","反馈详情"],
			id:'',
			picture:[],
			flag:true,
			status:'0',
			servicesReply:"",
			remark:"",
			detail:{

			},
	    };
	},
	created() {

	},
	components: {
	 Bread
	},
	methods: {
		   //大图预览带分页
            previewBigImg(images,index) {
                //string转数组
                var imagesArr = images?images.split(","):[];
                if(imagesArr.length==0){
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2,index2)=>{
                    if (/http/.test(item2) || /data:image/.test(item2)) {
                    } else {
                        imagesArr[index2]  = window.SITE_CONFIG['imgURL'] + "" + item2;
                    }
                })
                this.$imagePreview({
                    images: imagesArr,
                    index:index,

                })
            },
		init(row){
		
			this.$nextTick(()=>{
				this.id = row.id;
				this.info();
			})
		},

		// 详情
		info(){
			this.$http.get(`/admin-api/feedback/${this.id}`).then(({ data: res }) => {
				if (res.code !== 200) {
					return this.$message.error(res.msg)
				}
				this.detail=res.data
				if(this.detail.imagesArr){
					this.picture=this.detail.imagesArr.split(',')
				}
				if(this.detail.disposeStatus==0){
					this.flag=false
				}
				
				this.servicesReply=this.detail.servicesReply
				this.remark=this.detail.remark
				if(this.detail.disposeStatus==1 && !this.detail.remark){
					this.remark="暂无备注"
				}
				console.log(this.picture);
				// this.status=this.detail.decideType
			}).catch(() => {})
		},
		// 返回
		changePage(){
    		this.$emit("showList1");
		},
		//保存
		save(){

			if(!this.servicesReply){
				this.$message.error("请输入回复内容")
				return
			}
			var obj={
				id:this.id,
				servicesReply:this.servicesReply,
				remark:this.remark,
				decideType:this.status
			}
			this.$http.put(`/admin-api/feedback`,obj).then(({ data: res }) => {
				if (res.code == 200) {
					this.info();
					this.flag=true
				}
				
			}).catch(() => {})

		},
   }
};
</script>
<style lang="scss" scoped>

.goodsInfoClass{
  display: flex;
  flex-direction: column;
  font-size:13px;
  color:#333;
}
/deep/ .el-textarea{
	position: relative;
	.el-input__count{
		position: absolute;
		bottom: 5px;
		right: 10px;
	}
}

</style>

