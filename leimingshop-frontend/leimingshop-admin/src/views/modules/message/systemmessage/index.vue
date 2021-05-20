<template>
  <div>
  	<Bread :breaddata="breaddata" ></Bread>
	  <el-table
		  	:data="dataForm"
		  	border
		  	style="width: 100%"
		  	v-loading="dataListLoading">

			<el-table-column
			    prop="tempTitle"
			    label="推送类型"
			    align="center">
			</el-table-column>
			<el-table-column
			    prop="isSendInner"
			    label="站内信"
			    align="center">
			    <template slot-scope="scope">
					<div style="position: relative;">
						<el-switch
						  v-model="scope.row.isSendInner" active-text="开" inactive-text="关"
						  @change="putState(scope.row.id,scope.row.isSendInner,1)"
						  >
						</el-switch>
						<span class="artblue" @click="show(0,scope.row.id,scope.row.innerCanOpen)">模板设置</span>
						<div v-if="changeSwitchVisible"  class="hiddenSwitch"></div>
					</div>
			    </template>
			</el-table-column>
			<el-table-column
			    prop="isSendUmeng"
			    label="APP推送"
			    align="center">
				<template slot-scope="scope">
					<div style="position: relative;">
						<el-switch
								v-model="scope.row.isSendUmeng" active-text="开" inactive-text="关"
								@change="putState(scope.row.id,scope.row.isSendUmeng,2)"
								>
						</el-switch>
						<span class="artblue" @click="show(1,scope.row.id,scope.row.umengCanOpen)">模板设置</span>
						<div v-if="changeSwitchVisible"  class="hiddenSwitch"></div>
					</div>
				</template>
			</el-table-column>
			<el-table-column
			    prop="isSendSms"
			    label="短信 "
			    align="center">
				<template slot-scope="scope">
					<div style="position: relative;">
						<el-switch v-model="scope.row.isSendSms" active-text="开" inactive-text="关"
								   @change="putState(scope.row.id,scope.row.isSendSms,3)"
								>
						</el-switch>
						<span class="artblue" @click="show(2,scope.row.id,scope.row.smsCanOpen)">模板设置</span>
						<div v-if="changeSwitchVisible"  class="hiddenSwitch"></div>
					</div>
				</template>
			</el-table-column>

		  	<el-table-column
				  prop="isSendWechat"
				  label="微信公众号推送"
				  align="center">
			  <template slot-scope="scope">
				  <div style="position: relative;">
					  <el-switch v-model="scope.row.isSendWechat" active-text="开" inactive-text="关"
								 @change="putState(scope.row.id,scope.row.isSendWechat,4)">
					  </el-switch>
						  <span class="artblue" @click="show(3,scope.row.id)">模板设置</span>
					  <div v-if="changeSwitchVisible"  class="hiddenSwitch"></div>
				  </div>
			  </template>
		  </el-table-column>
		</el-table>

	  <el-dialog title="消息模板设置" :visible.sync="dialogTableVisible" v-if="dialogTableVisible">
			  <el-form :rules="dataRule" :model="ShopmessagetemplateList" ref="addForm1">
				  <el-form-item label="消息类型：">
					  <span>{{ShopmessagetemplateList.messageTypeName}}</span>
				  </el-form-item>
				  <el-form-item label="推送方式：">
					  <span>{{ShopmessagetemplateList.templateType == 0?"站内信":"APP推送"}}</span>
				  </el-form-item>
				  <el-form-item label="标签说明：">
					  <span>
						   <el-tag
								   :key="index"
								   v-for="(tag,index) in ShopmessagetemplateList.labelList"
								   :disable-transitions="false"
								   style="margin-right:5px;"
								   @click="handleClose(tag)">
                    {{tag}}
                </el-tag>
					  </span>
				  </el-form-item>
				  <el-form-item label="消息标题：" style="height: 100%!important;" prop="messageTitle">
					  <el-input type="text" maxlength="100" show-word-limit v-model="ShopmessagetemplateList.messageTitle" placeholder="请输入标题名称"></el-input>
				  </el-form-item>
				  <el-form-item label="消息内容：" style="height: 100%!important;" prop="messageContent">
					  <el-input  type="textarea" maxlength="500" show-word-limit v-model="ShopmessagetemplateList.messageContent" :rows="5" placeholder="请输入内容"></el-input>
				  </el-form-item>
			  </el-form>
			  <div slot="footer" class="dialog-footer">
				  <el-button @click="rest(0)">取 消</el-button>
				  <el-button type="primary" @click="saveTemplate(0)">确 定</el-button>
			  </div>
		  </el-dialog>

	  <el-dialog title="短信模板设置" :visible.sync="dialogTableVisibleOne">
		  <el-form
			:model="ShopmessagetemplateList"
			:rules="dataRule"
			ref="addForm2">
			  <el-form-item label="消息类型：">
				  <span>{{ShopmessagetemplateList.messageTypeName}}</span>
			  </el-form-item>
			  <el-form-item label="模板编码：" style="height: 100%!important;">
				  <el-input type="text" v-model="ShopmessagetemplateList.messageTitle" placeholder="请输入短信模板编码"></el-input>
				  <p style="color: #bebebe;line-height: 14px;">请填写在阿里短信后台配置的短信模板编码</p>
			  </el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
			  <el-button @click="rest(1)">取 消</el-button>
			  <el-button type="primary" @click="saveTemplate(1)">确 定</el-button>
		  </div>
	  </el-dialog>

	  <el-dialog title="微信消息模板设置" :visible.sync="dialogTableVisibleWechat">
		  <el-form
				  :model="ShopmessagetemplateList"
				  :rules="dataRule"
				  ref="addForm3">
			  <el-form-item label="消息类型：">
				  <span>{{ShopmessagetemplateList.messageTypeName}}</span>
			  </el-form-item>
			  <el-form-item label="模板ID：" style="height: 100%!important;">
				  <el-input @change="changeId" type="text" v-model="ShopmessagetemplateList.wechatTemplateId" placeholder="请填写微信公众平台模板ID"></el-input>
				  <p style="color: #bebebe;line-height: 14px;">请填写微信公众平台-模板消息-模板ID</p>
			  </el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
			  <el-button @click="rest(1)">取 消</el-button>
			  <el-button type="primary" @click="saveTemplate(2)">确 定</el-button>
		  </div>
	  </el-dialog>
  </div>
</template>

<script>
import Bread from "@/components/bread";
import { getmessagepage, putMessageState,getShopmessagetemplate,saveShopmessagetemplate } from '@/api/api'

export default {
	data () {
	    return {
	      	breaddata: ["消息", "系统消息设置"],
            dataListLoading: false,
            dialogTableVisible: false,
            dialogTableVisibleOne: false,
			dialogTableVisibleWechat: false,
            ShopmessagetemplateList:{
                messageTypeName:"",
                labelList:"",
                templateType:"",
                messageTitle:"",
                messageContent:"",
                messageId:"",
                messageCode:"",
				wechatTemplateId:""
			},
            dataForm: [],
			changeSwitchVisible: false,
            selectVal:"",
			dataRule:{
				messageTitle: [
					{ required: true, message: '必填项不能为空', trigger: 'blur' }
				],
				wechatTemplateId: [
					{ required: true, message: '必填项不能为空', trigger: 'blur' }
				],
				messageContent: [
					{ required: true, message: '必填项不能为空', trigger: 'blur' }
				],
			},
	    }
	},
	watch: {
		'ShopmessagetemplateList.messageTitle': function (newV, oldV) {
			var chineseCount = 0, characterCount = 0;
			for (let i = 0; i < newV.length; i++) {
				if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
					chineseCount = chineseCount + 2;
				} else { //字符
					characterCount = characterCount + 1;
				}
				var count = chineseCount + characterCount;
				if (count > 200) { //输入字符大于200的时候过滤
					this.ShopmessagetemplateList.messageTitle = newV.substr(0, (chineseCount / 2 + characterCount) - 1)
				}
			}
		}
	},
	created() {
        getmessagepage().then((res)=>{
            if(res.code == 200){
                this.dataForm = res.data;
                this.dataForm.map((v,i)=>{
                    if(v.isSendInner) v.isSendInner = false;
                    else v.isSendInner = true;
                    if(v.isSendSms) v.isSendSms = false;
                    else v.isSendSms  = true;
                    if(v.isSendUmeng) v.isSendUmeng = false;
                    else v.isSendUmeng  = true;
                    if(v.isSendWechat) v.isSendWechat = false;
                    else v.isSendWechat  = true;
				})
            }
        })
	},
	components: {
		Bread
	},
	methods: {
		changeId(v){
			console.log(v)
		},
  	    rest(type){
            if(type == 0) {
            	this.dialogTableVisible = false;
            }else {
            	this.dialogTableVisibleOne = false;
            	this.dialogTableVisibleWechat = false;
            }
            this.ShopmessagetemplateList = {
                messageTypeName:"",
                    labelList:"",
                    templateType:"",
                    messageTitle:"",
                    messageContent:"",
                    messageId:"",
                    messageCode:"",
				wechatTemplateId:''
            }
		},
        saveTemplate(type){
					let that = this;
					let obj = {};
					var formName="addForm1"
					if(type == 0){
						formName="addForm1"
						obj = {
							templateType:this.ShopmessagetemplateList.templateType,
							messageTitle:this.ShopmessagetemplateList.messageTitle,
							messageContent:this.ShopmessagetemplateList.messageContent,
							messageId:this.ShopmessagetemplateList.messageId
						}
					}else if(type==2){
						// 微信弹框
						console.log("type2",this.ShopmessagetemplateList.wechatTemplateId)
						formName="addForm3"
						var res =  /^[0-9A-Za-z_-]+$/g
						if(!res.test(this.ShopmessagetemplateList.wechatTemplateId)){
							this.$message({
								message:"请输入正确格式的模板编码",
								type: 'error',
								duration: 1500,
							})
							return
						}
						obj = {
							templateType:this.ShopmessagetemplateList.templateType,
							messageId:this.ShopmessagetemplateList.messageId,
							wechatTemplateId:this.ShopmessagetemplateList.wechatTemplateId,
						}
					}else{
						formName="addForm2"
						console.log("addForm2")
						var res =  /^[0-9A-Z_]+$/g
						if(!res.test(this.ShopmessagetemplateList.messageTitle)){
							this.$message({
								message:"请输入正确格式的模板编码",
								type: 'error',
								duration: 1500,
							})
							return
						}
						obj = {
							templateType:this.ShopmessagetemplateList.templateType,
							messageId:this.ShopmessagetemplateList.messageId,
							messageCode:this.ShopmessagetemplateList.messageCode,
							messageTitle:this.ShopmessagetemplateList.messageTitle,
							wechatTemplateId:this.ShopmessagetemplateList.wechatTemplateId,
						}
					}
					this.$refs[formName].validate((valid) => {
						if (valid) {
							saveShopmessagetemplate(obj).then((res)=>{
								if(res.code==200){
									if(type == 0) {
										that.dialogTableVisible = false;
									}
									else {
										that.dialogTableVisibleOne = false;
										that.dialogTableVisibleWechat = false;
									}
									getmessagepage().then((res)=>{
										if(res.code == 200){
											that.dataForm = res.data;
											that.dataForm.map((v,i)=>{
												if(v.isSendInner) v.isSendInner = false;
												else v.isSendInner = true;
												if(v.isSendSms) v.isSendSms = false;
												else v.isSendSms  = true;
												if(v.isSendUmeng) v.isSendUmeng = false;
												else v.isSendUmeng  = true;
												if(v.isSendWechat) v.isSendWechat = false;
												else v.isSendWechat  = true;
											})
										}
									})
									this.$message({
										message:res.msg,
										type: 'success',
										duration: 1500,
									})
								}else{
									this.$message({
										message:res.msg,
										type: 'error',
										duration: 1500,
									})
								}
							})
						}
					})
		},
        handleClose(val){
            this.ShopmessagetemplateList.messageContent = this.ShopmessagetemplateList.messageContent+val;
		},
  	    show(name,id,open){
			this.ShopmessagetemplateList.messageTitle = ""
			this.ShopmessagetemplateList.messageCode = ""
  	        // if(open == 1){
  	        	// 短信
                if(name == 2) {
                    this.dialogTableVisibleOne = true;
                } else if(name == 3){
                	this.dialogTableVisibleWechat = true;
				}else{
                    this.dialogTableVisible = true;
                }
                getShopmessagetemplate({id:id}).then((res)=>{
                    if(res.code == 200){
                        this.ShopmessagetemplateList.labelList = res.data.labelList;
                        this.ShopmessagetemplateList.messageTypeName = res.data.messageTypeName;
                        this.ShopmessagetemplateList.templateType = name;
                        this.ShopmessagetemplateList.messageId = id;
                        if(name == 0){
                            this.ShopmessagetemplateList.messageTitle = res.data.messAgeTemplate.tempTitle;
                            this.ShopmessagetemplateList.messageContent = res.data.messAgeTemplate.tempInnerContent;
                        }else if(name == 1){
                            this.ShopmessagetemplateList.messageTitle = res.data.messAgeTemplate.umengTitle;
                            this.ShopmessagetemplateList.messageContent = res.data.messAgeTemplate.tempUmengContent;
                        }else if(name == 2){
							this.ShopmessagetemplateList.messageCode = res.data.messAgeTemplate.tempSmsCode;
							this.ShopmessagetemplateList.messageTitle = res.data.messAgeTemplate.tempSmsCode;
                        }else if(name == 3){
							this.ShopmessagetemplateList.wechatTemplateId = res.data.messAgeTemplate.wechatTemplateId;
							this.ShopmessagetemplateList.wechatTemplateId = res.data.messAgeTemplate.wechatTemplateId;
						}
                    }
                })
			// }
		},
  	    putState(id,isCheck,type){
            let that = this;
                putMessageState({
					id:id,
					isSendInner: type==1?isCheck?'0':'1':"",
                    isSendUmeng:type==2?isCheck?'0':'1':"",
                    isSendSms:type==3?isCheck?'0':1:"",
                    isSendWechat:type==4?isCheck?'0':'1':"",
				}).then((res)=>{
                    if(res.code == 200){
                        this.$message({
                            message: res.msg,
                            type: 'success',
                            onClose:function () {
								that.changeSwitchVisible = true;
                                getmessagepage().then((res)=>{
									that.changeSwitchVisible = false;
                                    if(res.code == 200){
                                        that.dataForm = res.data;
                                        that.dataForm.map((v,i)=>{
                                            if(v.isSendInner) v.isSendInner = false;
                                            else v.isSendInner = true;
                                            if(v.isSendSms) v.isSendSms = false;
                                            else v.isSendSms  = true;
                                            if(v.isSendUmeng) v.isSendUmeng = false;
                                            else v.isSendUmeng  = true;
                                            if(v.isSendWechat) v.isSendWechat = false;
                                            else v.isSendWechat  = true;
                                        })
                                    }
                                })
                            }
                        });
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error',
                        });
                    }
                })
		}
	}
};
</script>
<style lang="scss" scoped>

 .grayBtnWarp{
 	overflow: hidden;
 	.grayBtnWarp-right{
 		float:right;
 		
 	}
 	/*margin:20px 340px 0;
 	display:flex;
 	justify-content: space-around;*/
 	
 }
/deep/ table{
 	width: auto !important;
 }
/deep/ .el-table__header{
 	width: auto !important;
 }
.grayLine{
	border-bottom: 0!important;
}
.artblue{
	color: blue;
	margin-left: 10px;
}
 .hiddenSwitch {
	 position: absolute;
	 width: 100%;
	 height: 100%;
	 top: 0;
	 left: 0;
 }
/*文字显示在开关内部*/
/deep/ .el-switch__label--left{
	 position: relative;
	 left: 46px;
	 color: #fff;
	 z-index: -1111;
 }
 /deep/ .el-switch__label--right{
	 position: relative;
	 right: 46px;
	 color: #fff;
	 z-index: -1111;
 }
 /deep/ .el-switch__label.is-active {
	 z-index: 1111;
	 color: #fff;
 }
 /deep/ .el-textarea{
	position: relative;
	.el-input__count{
		position: absolute;
		bottom: -5px;
		right: 10px;
	}
}
</style>

