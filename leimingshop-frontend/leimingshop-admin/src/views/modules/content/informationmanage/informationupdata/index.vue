<template>
  <div>
		<Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
		<el-form  :style="{height:formHeight+ 'px','overflow-y':'scroll',width:'100%','margin-left':'10%'}" :inline="true" :model="dataForm" ref="dataForm" :rules="rules">
	    <el-form-item label="站内信标题：" prop="messageTitle">
	      	<el-input  class='artcontent' v-model="dataForm.messageTitle" placeholder="请输入站内信标题" show-word-limit maxlength=30  showclearable></el-input>
	    </el-form-item>
	    <el-form-item label="信息类型：">
		    <el-radio-group style="margin-left: -20px;" v-model="dataForm.messageType">
		    	<em @click='actreceiverTypeshow'><el-radio :label="1" >系统信息</el-radio></em>
		      <em @click='actchangeredioshow'><el-radio  :label="0" >私信</el-radio></em>
		    </el-radio-group>
		  </el-form-item>
		  <el-form-item label="收件人：" v-if='receiverTypeshow'>
		    <el-radio-group v-model="dataForm.receiverType" v-if='receiverTypeshow'>
		      <el-radio :label="0">全部</el-radio>
		      <el-radio :label="1">会员</el-radio>
		      <el-radio :label="2">商户</el-radio>
		    </el-radio-group>
		  </el-form-item>
		  <el-form-item label="收件人/店铺名：" v-else>
			<el-button class="btn" @click="addReceive" type="primary" >添加收件人</el-button>
			<el-button class="btn"  @click="removeAll">移除全部</el-button>
			<div v-if="vipData.length!=0" style="padding:0px;max-height:250px;overflow-y: auto;border:1px solid #dedbdb;margin-top:8px;padding: 8px;">
			    <em  v-for='item in vipData' style="background: #efefef;padding: 3px;margin-right: 20px;margin-left:0px;">
					{{item.nickName || item.storeName}}
					<i class="el-icon-error" @click='actdelete(item.id)'></i>
				</em>
			</div>
		  </el-form-item>
	    <el-form-item label="信息内容：">
	    	<quill-editor-img @artmessageContent='artmessageContent' ref="refmessageContent"></quill-editor-img>
	    </el-form-item>
	    <el-form-item >
	    	<div class='artbtn'>
	    		<el-button class="btn" @click="changePage" >取消</el-button>
			    <el-button class="btn" type="primary" @click="actSubmite('dataForm',false)" :loading="nextloading"  v-if="$hasPermission('sys:send:message:save')">发送并添加下一条</el-button>
			    <el-button class="btn" type="primary" @click="actSubmite('dataForm',true)" :loading="sendloading" v-if="$hasPermission('sys:send:message:save')">发送</el-button>
	    	</div>
			</el-form-item>
	 </el-form>
	  <div v-if='dialogTableVisible'>
	  	<el-dialog title="请选择" :visible.sync="dialogTableVisible" width="60%">
		  	<list
			  	v-if='dialogTableVisible'
				@actdodelete='actdelete'
				ref="detailCompon"
				@actclose='actclose'
				@changeMemberId="changeMemberId"
				@changeStoreList="changeStoreList"
				@changeVipData="changeVipData"></list>
				<!-- :showdata='vipData' -->
				<!-- @actVipNameshop='actVipNameshop'  -->
				<!-- @actVipNamevip='actVipNamevip' -->
			</el-dialog>
	  </div>

  </div>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import { quillEditor } from "vue-quill-editor"; //调用编辑器
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { addmessage } from '@/api/url'
import list from "./list"
import quillEditorImg from "@/components/quillEditor"
import Bread from "@/components/bread";
import cloneDeep from 'lodash/cloneDeep'
export default {
	data () {
	    return {
	    	content:'',   //富文本内容
	    	quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
			dataForm: {
						messageTitle:'',   //短消息标题
						messageType:1,    //信息类型(0:私信;1:系统信息)
						receiverType:0,    //收件人类型  全部0 会员1 商户2
						messageContent:'',  //消息内容
						memberId:[],  //接收人信息          必为私信
						storeList:[], //店铺
			},
				breaddata: ["内容管理", "站内信管理", "站内信列表","新增"],
	      rules:{
        	messageTitle :[{required: true, message: '请输入站内信标题', trigger: 'blur'}],
        },
        dialogTableVisible: false,
        vipData: [],   //显示用的
        receiverTypeshow: true,
        dataVipNameshop:[],
        dataNamevip:[],
        dataVipNameemail:[],
        dataVipName:[],
        nextloading:false,
		sendloading:false,
        formHeight:document.body.offsetHeight-230,
	    };
	},
	components: {
    quillEditor,
    list,
		Bread,
		quillEditorImg
  },
  created () {
  },
  methods: {
  	artmessageContent(messageContent){
  		this.dataForm.messageContent = messageContent;
  	},
    //发送
		actSubmite(dataForm,flag){
			this.$refs[dataForm].validate((valid) => {
				 if (valid) {
		 				if(flag){
							this.sendloading = true;
						}else{
							this.nextloading = true;
						}
						let  dataForm = cloneDeep(this.dataForm)
						let  memberList  = []
						dataForm.memberId.forEach((k,v)=>{
							memberList.push({
								id:k.id,
								nickName:k.nickName
							})
						})
						delete  dataForm.memberId
						dataForm.memberList = memberList
						
						let  storeList = []
						dataForm.storeList.forEach((k,v)=>{
							storeList.push({
								storeId:k.id,
								storeName:k.storeName
							})
						})
						delete  dataForm.storeList
						dataForm.storeList = storeList

            this.$http.post(addmessage,dataForm).then(({ data: res }) => {
            	console.log(res)
		          if (res.code !== 200) {
		            return this.$message.error(res.msg)
		          }
		          this.$message({
		            message: this.$t('prompt.success'),
		            type: 'success',
		            duration: 1500,
		            onClose: () => {
		            }
		          })
		          this.dataForm.messageTitle = '';
		          this.dataForm.messageType = 1;
		          this.dataForm.receiverType = 0;
				  this.$refs.refmessageContent.init();
		          this.dataForm.messageContent = '';
		          this.vipData = [];
		          this.dataForm.memberId = [];
		          this.dataForm.storeList = [];
		          this.receiverTypeshow = true;
		          //跳转list页面
		          if(flag){
		          	this.sendloading = false;
		          	this.$router.push({	name: "content-informationmanage-list"})
		          }else{
		          	this.nextloading = false;
		          }
		        }).catch(() => {})
          } else {
            // this.$message('请输入发件人/收件人');
            return false;
          }

	 		});
		},
		changePage(){
			this.$router.push({name:"content-informationmanage-list"})
		},
		actclose(){
			this.dialogTableVisible = false;
		},
		// 点击系统信息
		actreceiverTypeshow(){
			this.vipData = [];
			this.dataForm.receiverType = 0;
			this.dataForm.memberId = [];
			this.dataForm.storeList = [];
			this.receiverTypeshow = true;
		},
		// 点击私信
		actchangeredioshow(){
			this.dataForm.receiverType = "";
			this.receiverTypeshow = false;
		},
		// 添加收件人
		addReceive(){
			this.dialogTableVisible = true;
			this.$nextTick(()=>{
				this.$refs.detailCompon.init(this.vipData,this.dataForm.memberId,this.dataForm.storeList);
			})
		},
		// 移除全部
		removeAll(){
			this.vipData = [];
			this.dataForm.memberId = [];
		},
		// actVipNameshop(vipData){  // 1 店铺
		// 	this.vipData = [];
		// 	if(this.dataForm.storeList.length == 0){
		// 		this.dataForm.storeList.push(vipData)
		// 	}
		// 	var flag = true;
		// 	this.dataForm.storeList.forEach((item,index)=>{
		// 		if(item.id == vipData.id){
		// 		 	flag = false;
		// 		}
		// 	})
		// 	if(flag){
		// 		this.dataForm.storeList.push(vipData)
		// 	}
		// 	this.vipData = this.vipData.concat(this.dataForm.storeList).concat(this.dataForm.memberId);
		// },
		// actVipNamevip(vipData){  //0 会员   2 邮箱  3  名称、
		// 	this.vipData = [];
		// 	if(this.dataForm.memberId.length == 0){
		// 		this.dataForm.memberId.push(vipData)
		// 	}
		// 	var flag = true;
		// 	this.dataForm.memberId.forEach((item,index)=>{
		// 		if(item.id == vipData.id){
		// 		 	flag = false;
		// 		}
		// 	})
		// 	if(flag){
		// 		this.dataForm.memberId.push(vipData)
		// 	}
		//   this.vipData = this.vipData.concat(this.dataForm.storeList).concat(this.dataForm.memberId);
		// },
		// 改变选中的会员你数据
		changeMemberId(memberId){
			this.dataForm.memberId = cloneDeep(memberId);
		},
		// 改变选中店铺的数据
		changeStoreList(storeList){
			this.dataForm.storeList = cloneDeep(storeList);
		},
		// 改变选中的数据
		changeVipData(vipData){
			this.vipData = cloneDeep(vipData);
		},
		actdelete(id){
			this.vipData.forEach((item,index)=>{
				 if(item.id == id){
				 	 this.vipData.splice(index, 1);
				 }
			})
			this.dataForm.storeList.forEach((item,index)=>{
				 if(item.id == id){
				 	 this.dataForm.storeList.splice(index, 1);
				 }
			})
			this.dataForm.memberId.forEach((item,index)=>{
				 if(item.id == id){
				 	 this.dataForm.memberId.splice(index, 1);
				 }
			})
		}
  },
}
</script>
<style lang="scss" scoped>
.el-form{
	// padding-top:50px;
	width: 1050px;
	margin: 0 auto;
	.el-form-item {
		display: block;
	}
/deep/	.el-form-item__label{
		margin-right: 20px;
		width:110px;
		text-align: right;
	}
/deep/	input{
		width: 500px;
	}
	.artcontent{
		width: 500px  !important;
	}
/deep/ .el-form-item__content{
		width: 870px;
/deep/ .edit_container{
			height: 500px;
		}
		.ql-container.ql-snow{
			height: 450px;
		}
	}
	.artbtn{
		margin-top: 30px;
		margin-bottom: 30px;
		padding: 0 200px;
		// display: flex;
		// justify-content: space-evenly;
		.btn{
			margin-right: 10px;
		}
	}
}
em{
	margin-left: 20px;
	font-style: normal;
}
.el-icon-error{
	cursor: pointer;
	font-size: 12px;
}

</style>

