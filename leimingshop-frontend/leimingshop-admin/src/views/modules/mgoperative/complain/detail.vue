<template>
  <div>
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
	<div style="display:flex;flex-direction: row;">
		<div style="margin-left: 10px;display: flex;flex-direction: column;float:left;width:45%;">
			<div style="margin-left: 75px;float:left;">
			<h3>投诉内容</h3>
			<div>
				<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;">
					<!--前两行左右对齐-->
					<div style="display: flex;justify-content: space-between;align-items: center">
						<div>
							<div>会员账号：{{detail.account}}</div>
							<div style="margin-top: 20px;">联系人电话：{{detail.phone}}</div>
						</div>
						<div style=" font-size:13px;color:#333;">
							<div>投诉类型：
								<span v-if="detail.type==1">服务相关</span>
								<span v-if="detail.type==2">物流相关</span>
								<span v-if="detail.type==3">售后相关</span>
								<span v-if="detail.type==4">商品相关</span>
								<span v-if="detail.type==5">其他</span>

							</div>
							<div style="margin-top: 20px;">投诉来源：
								<span v-if="detail.source==0">PC</span>
								<span v-if="detail.source==1">H5</span>
								<span v-if="detail.source==2">android</span>
								<span v-if="detail.source==3">ios</span>
								<span v-if="detail.source==4">微信</span>
								<span v-if="detail.source==5">小程序</span>
							</div>
						</div>
					</div>

					<div style="margin-top: 20px;">投诉时间：{{detail.createDate}}</div>
					<div style="margin-top: 20px;display: flex;width:520px;">
						<label style="width:75px;	">投诉内容：</label>
						<el-input
								:disabled="true"
								type="textarea"
								:rows="4"
								v-model="detail.cause">
						</el-input>
					</div>
					<div style="margin-top: 20px;display: flex;width: 570px;">投诉图片：
						<div style="display: flex;">
							<div  v-for="(item,index) in this.picture" 	>
								<div style="width:96px">
									<img :src="item | filterImgUrl" alt="" style="width:70px;height:70px;border-radius: 6px;object-fit: scale-down;" @click="previewBigImg(item,index)">
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>
		</div>
		<div style="margin-left: 75px;float:left;margin-top: 30px;">
			<h3>投诉处理</h3>
			<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;float:left;width:40%;">
				<div v-if="detail.status==20">&nbsp;&nbsp;&nbsp;&nbsp;处理人：{{detail.disposePerson}}</div>
				<div v-if="detail.status==20" style="margin-top: 20px;">投诉判定：
					<span v-if="detail.verdict==1">有效问题</span>
					<span v-if="detail.verdict==2">重点问题</span>
					<span v-if="detail.verdict==3">无效投诉</span>
				</div>
				<div v-if="detail.status==10" style="margin-top: 20px;display: flex;flex-direction: row;width: 400px;">投诉判定：
					<el-radio-group v-model="status">
					<el-radio v-model="status" label="1">有效问题</el-radio>
					<el-radio v-model="status" label="2">重点问题</el-radio>
					<el-radio v-model="status" label="3">无效投诉</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-top: 20px;display: flex;width:520px;">
					<span  v-if="detail.status==10" style="color:red">*</span>
					<span  v-if="detail.status==20"></span>
					<label style="width:86px;	">客服回复：</label>
					<el-input
						:disabled="flag"
						type="textarea"
						:rows="4"
						maxlength="500"
						placeholder="请输入您对该投诉的处理内容，最多可输入500字"
						v-model="replyContent"
						show-word-limit>
						</el-input>
				</div>
				<div style="width: 320px;margin-left: 75px;margin-top: 7px;color: #aca9a8;">客服回复的内容将会显示在会员端的投诉处理结果中。</div>
				<div style="margin-top: 20px;display: flex;width:520px;">
					<label style="width:86px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注：</label>
					<el-input
						:disabled="flag"
						type="textarea"
						:rows="4"
						v-model="remark"
						maxlength="500"
						placeholder="请输入备注，最多可输入500字"
						show-word-limit>
						</el-input>
				</div>
				<div style="width: 390px;margin-left: 75px;margin-top: 7px;color: #aca9a8;">备注用于记录该投诉的处理方式、处理方法等，不会显示在会员端。</div>

			</div>
			<div v-if="detail.status==20" style=" font-size:13px;color:#333;">
				<div style="margin-top: 20px;">处理时间：{{detail.disposeDate}}</div>
			</div>
		</div>
		</div>
		<div style="clear:both"></div>
		<div style="margin-left: 100px;display: flex;flex-direction: column;float:left;width:40%;">
				<div style="margin-left: 75px;float:left;">
				<h3>订单信息</h3>
				<div class="goodsInfoClass" style="margin-left: 65px;display: flex;flex-direction: column;float:left;">
					<div>
						<label>订单编号：</label>
						<span >{{detail.orderSn}}</span>
						<label style="color: blue;padding-right: 30px;cursor: pointer;margin:0 20px; " @click="toOrder()">查看订单详情</label>
					</div>
					<div style="margin-top: 20px;">下单时间：{{detail.downOrderTime}}</div>
					<div style="margin-top: 20px;">订单金额：{{detail.orderAmount}}</div>
					<div style="margin-top: 20px;">订单状态：
						<span v-if="detail.orderStatus==10">待付款</span>
						<span v-if="detail.orderStatus==20">待发货</span>
						<span v-if="detail.orderStatus==30">待收货</span>
						<span v-if="detail.orderStatus==40">交易完成</span>
					</div>
				</div>
			</div>

			<div style="margin-left: 75px;float:left;margin-top: 30px;">
				<h3>商户信息</h3>
				<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;float:left;">
					<div> 
						<label>店铺名称：</label>
						<span>{{detail.storeName}}</span>
						<!-- <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> -->
						<label style="color: blue;padding-right: 30px;cursor: pointer;margin:0 60px;" @click="toStore()">
							查看店铺详情
						</label>
					</div>
					<div style="margin-top: 20px;">商户账号：{{detail.storeAccount}}</div>
					<div style="margin-top: 20px;">店铺等级：{{detail.gradeName}}</div>
					<div style="margin-top: 20px;">店铺状态：
						<span v-if="detail.storeStatus==0">启用</span>
						<span v-if="detail.storeStatus==1">禁用</span>
					</div>
				</div>
			</div>
				<div v-if="detail.status==20" style="margin-left: 75px;float:left;margin-top: 30px;">
				<h3>用户满意度</h3>
				<div class="goodsInfoClass" style="margin-left: 75px;display: flex;flex-direction: column;float:left;width:40%;">
					<div style="display: flex;width: 300px;">
							<label style="font-weight: 550;">用户满意度：</label>
							<el-rate
									v-model="detail.customerSatisfaction "
									disabled
									text-color="#ff9900" style="float: left;margin-left: 8px">
							</el-rate>
							<span>&nbsp;&nbsp;&nbsp;</span>
							<span v-if="detail.customerSatisfaction==1||detail.customerSatisfaction==2">差评</span>
							<span v-if="detail.customerSatisfaction==3||detail.customerSatisfaction==4">一般</span>
							<span v-if="detail.customerSatisfaction==5">满意</span>
					</div>
					<div style="margin-top: 20px;display: flex;width:320px;">
						<label style="width:50px;	">说明：</label>
						<el-input
							:disabled="true"
							type="textarea"
							:rows="4"
							v-model="detail.customerContent">
							</el-input>
					</div>
				</div>
			</div>
		</div>
		<div style="clear:both"></div>
	</div>
	<div style="display:flex;flex-direction: row;margin-top:30px;justify-content: center;">
		<el-button  type="primary"  @click="changePage()">
			<span v-if="detail.status==20">返回</span>
			<span v-if="detail.status==10">取消</span>
		</el-button>
		<el-button v-if="detail.status==10" type="primary"  @click="save()">处理</el-button>
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
			breaddata: ["运营管理", "客服管理", "投诉列表","投诉详情"],
			id:'',
			picture:[],
			flag:true,
			status:'1',
			replyContent:"",
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
		init(row){
		
			this.$nextTick(()=>{
				this.id = row.id;
				this.info();
			})
		},
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
		// 详情
		info(){
			this.$http.get(`/admin-api/ordercomplain/${this.id}`).then(({ data: res }) => {
				if (res.code !== 200) {
					return this.$message.error(res.msg)
				}
				this.detail=res.data
				if(this.detail.picture){
					this.picture=this.detail.picture.split(',')
				}
				if(this.detail.status==10){
					this.flag=false
				}
				
				this.replyContent=this.detail.replyContent
				this.remark=this.detail.remark
				if(this.detail.status==20 && !this.detail.remark){
					this.remark="暂无备注"
				}
				if(!this.detail.customerContent){
					this.detail.customerContent='用户未评价'
				}
				console.log(this.picture);
				// this.status=this.detail.verdict
			}).catch(() => {})
		},
		// 返回
		changePage(){
    		this.$emit("showList1");
		},
		//保存
		save(){

			if(!this.replyContent){
				this.$message.error("请输入回复内容")
				return
			}
			var obj={
				id:this.id,
				replyContent:this.replyContent,
				remark:this.remark,
				verdict:this.status
			}
			this.$http.put(`/admin-api/ordercomplain`,obj).then(({ data: res }) => {
				if (res.code == 200) {
					this.info();
					this.flag=true
				}
				
			}).catch(() => {})

		},
		// 跳转订单详情
		toOrder(){
			var row ={
				id:this.detail.orderId,
				orderSn:this.detail.orderSn
			}
			this.$emit("orderDetail",row)
		},
		//跳转店铺详情
		toStore(){
			var row ={
				id:this.detail.storeId
			}
			this.$emit("storeDetail",row)
		}
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

