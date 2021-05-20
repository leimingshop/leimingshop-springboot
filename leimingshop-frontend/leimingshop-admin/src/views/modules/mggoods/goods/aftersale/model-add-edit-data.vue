<template>
	<el-dialog
	 	    class="model-add-edit-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
				:before-close="closeDialog">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		label-width="120px">
				        <el-form-item label="模板售后名称：" prop="name">
				        	  <el-input v-model="dataForm.name" maxlength="64"   placeholder="请输入属性名称"></el-input>
				        </el-form-item>
						<el-form-item label="模板内容：" prop="value">
				        	  <!-- <el-input v-model="dataForm.value" maxlength="60" placeholder="请输入属性名称"></el-input> -->
							    <!-- <quill-editor ref="myTextEditor" v-model="dataForm.value" :options="editorOption"></quill-editor> -->
								<quill-editor-img @artmessageContent='artmessageContent' ref="myTextEditor"></quill-editor-img>
				        </el-form-item>
					</el-form>
			    <div slot="footer" class="dialog-footer" style="text-align: center;">
		     		    <el-button @click="dataFormCancel()">返回</el-button>
						 <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </div>
	</el-dialog>
</template>
<script>
    // import { livemembertype_add } from '@/api/api2'
	
	import { addAftertemplate,backScanAftertemplate,updateAftertemplate } from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'

 	// import { quillEditor } from "vue-quill-editor"; //调用编辑器
	// import 'quill/dist/quill.core.css';
	// import 'quill/dist/quill.snow.css';
	// import 'quill/dist/quill.bubble.css'; 
	import quillEditorImg from "@/components/quillEditor"
	export default{
		name: "model-add-edit-data",
		data(){
			 //数字
	        var validateNum = (rule, value, callback) => {
		        if (!isIntNum(value)) {
		          callback(new Error('请输入正确的天数'))
		        } else {
		          callback()
		        }
			}
			var  validatorNameFn =  (rule, value, callback) => {
				if (value.length>=65) {
					callback(new Error('模板售后名称长度限制为64个汉字'))
				}else {
					callback()
				}
			}
			var  validatorValue = (rule, value, callback) => {
				var value2 = value.replace(/<\/p>/g,"").replace(/<p>/g,"").replace(/&nbsp;/g,"")
				value2 = value2.replace(/ /g,"")
				// value2 = value2.replace("</p>","")
				// value2 .replace("<p>","")
				console.log(value2.length)
				console.log(value2)
				if (value2.length>800) {
					callback(new Error('模板内容长度限制为800个汉字'))
				}else {
					callback()
				}
			}
			return{
				visible : false,
				loading : false,
				dataForm : {
					name:'',
					value:'',
				},
				editorOption: {
					placeholder: '请输入您要编辑的内容'
				},
				dataRule : {
			        name : [
					  { required: true, message: '必填项不能为空', trigger: 'blur' },
					  {validator: validatorNameFn, trigger: 'blur'}
			        ],
			        value: [
					  { required: true, message: '必填项不能为空', trigger: 'blur' },
					  { validator: validatorValue, trigger: 'blur'}
					]
				},
				optionsApplication: [],
				optionsRight: [],
				title:'',
				row:"",
			}
		},
		components:{
			quillEditorImg,
        },
		computed:{},
		mounted(){},
		methods:{
			init (row) {
				this.visible = true;
				this.row = row;
				if(row){
					this.title="编辑模板";
					this.backScan();
				}else{
					this.title="新建模板"
				}
				this.$nextTick(() => {
					this.$refs['addForm'].resetFields();
				})
			},
			artmessageContent(messageContent){
				this.dataForm.value = messageContent;
			},
			// 编辑回显
			backScan(){
				var obj  = {
					id:this.row.id
				}
				backScanAftertemplate(obj).then((res)=>{
						if(res.code == 200){
								Object.assign(this.dataForm,res.data);
							  this.$refs.myTextEditor.dataForm.messageContent =this.dataForm.value
						}else{

						}
				})
			},
		
			// 提交
			dataFormSubmit(formName){
				// alert([this.dataForm.name,this.dataForm.domainAddress]);
				// console.log(this.dataForm);
				this.$refs[formName].validate((valid) => {
						if (valid) {
								this.loading = true;
								var obj={
									"name": this.dataForm.name,
									"storeId": 0,
									"value": this.dataForm.value
								}
								if(this.row) obj.id = this.row.id
								var fn = this.row?updateAftertemplate:addAftertemplate;
								fn(obj).then((res) => {
									this.loading = false;
									// alert(JSON.stringify(res));
									let status = null;
									if(res.code == "200"){
										status = "success";
										this.visible = false;
										this.$emit('searchDataList');
					          this.closeDialog();
									}else{
										status = "error";
									}
									this.$message({
													message: res.msg,
													type: status,
													duration: 1500
												})
								})
						} else {
								//console.log('error 添加失败!!');
								return false;
						}
				})
			},
			dataFormCancel(){
					this.visible = false;
					this.closeDialog();
			},
			closeDialog() {
        this.$parent.addEditDataVisible = false;
      },
		},
	}
</script>
<style scoped>
 .attrList{
	 height: 60px;
	 line-height: 90px;
	 border-top: 2px dotted gainsboro;
	 padding-left: 16px;
 }
 
</style>
