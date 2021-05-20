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
				@keyup.enter.native="dataFormSubmit('addForm')"
				label-width="120px">
			<el-form-item label="分组名称：" prop="groupName">
				<el-input v-model="dataForm.groupName" maxlength="5" placeholder="请输入标签名称"></el-input>
			</el-form-item>

			<el-form-item label="分组排序：" prop="sort">
				<el-input v-model="dataForm.sort" maxlength="3" placeholder="请输入0-255,数字越小排序越靠前"></el-input>
			</el-form-item>

			<el-form-item label="选择标签：" prop="labelId">
				<el-select
						class="skuSelect"
						v-model="dataForm.labelId"
						multiple
						:multiple-limit='limitNumber'
						filterable
						placeholder="请输入关键词"
						:remote-method="getTagGroupAllList"
						:loading="tagLoading">
						 <!-- remote
							reserve-keyword -->
					<el-option
							v-for="(item,index) in groupOptions"
							:key="index"
							:label="item.labelName"
							:value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
		</el-form>

		<span slot="footer" class="dialog-footer">
		     		    <el-button @click="dataFormCancel()">返回</el-button>
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
								   :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>


	import { addTaggroup,backScanTaggroup,updateTaggroup } from '@/api/api'
	import {tagGroupAllList} from '@/api/api'


	export default{
		name: "model-add-edit-data",
		data(){
			return{
				limitNumber:3,
				visible : false,
				loading : false,
				uploading:false,
				tagLoading:false,
				currentIndex:-1,
				dialogImageUrl: '',
				dialogVisible: false,
				dataForm : {
					sort:"",
					groupName:"",
					labelName:"",
					labelValueDTOList:[],
					// 测试发现如果不在下面添加 input输入不进去（代码动态添加也不行）
					labelValue0:"",
					labelValue1:"",
					labelValue2:"",
					labelValue3:"",
					labelValue4:"",
					labelValue5:"",
					labelValue6:"",
					labelValue7:"",
					labelValue8:"",
					labelValue9:"",
					labelValue10:"",
					labelValue11:"",
					labelValue12:"",
					labelValue13:"",
					labelValue14:"",
					labelValue15:"",
					labelValue16:"",
					labelValue17:"",
					labelValue18:"",
					labelValue19:"",
					labelValue20:"",
				},
				labelValueDTOItem:	{
					labelValueName:'',
				},
				groupOptions:[],
				dataRule : {
					groupName : [
						{ required: true, message: '必填项不能为空', trigger: 'blur' },
					],
					sort : [
						{ required: true, message: '必填项不能为空', trigger: 'blur' },
					],
				},
				title:'',
				row:"",
			}
		},
		components:{
		},
		methods:{
			getTagGroupAllList(query){

				var obj = {
					params:{
						labelName:query
					}
				}
				this.tagLoading = true;
				tagGroupAllList(obj).then((res)=>{

					this.tagLoading = false;
					var groupOptions = [];

					if(res.code == 200 && res.data){
						groupOptions =res.data;

					}

					var arr = [...groupOptions]
					// js中数组对象去重的方法
					var obj = {};
					arr = arr.reduce(function(item, next) {
						obj[next.id] ? '' : obj[next.id] = true && item.push(next);
						return item;
					}, []);
					this.groupOptions = arr;
				})
			},

			init (row) {
				this.visible = true;
				this.row = row;
				if(row){
					this.title="编辑标签分组";
					this.backScan();
				}else{
					this.title="新建标签分组"
					this.dataForm.labelValueDTOList = new  Array();
					this.dataForm.labelValueDTOList.push(this.labelValueDTOItem);
				}
				this.$nextTick(() => {
					this.$refs['addForm'].resetFields();
				})
				this.getTagGroupAllList()
			},
			// 编辑回显
			backScan(){
				var obj  = {
					id:this.row.id
				}
				backScanTaggroup(obj).then((res)=>{
					if(res.code == 200){
						Object.assign(this.dataForm,res.data);
						
					}else{

					}
				})
			},

			// 提交
			dataFormSubmit(formName){
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.loading = true;
						var obj = {
							"groupName":  this.dataForm.groupName,//标签分组名称 ,
							"labelIds":  this.dataForm.labelId,//关联标签ids ,
							"sort":this.dataForm.sort
						}
						if(this.row) obj.id = this.row.id
						var fn = this.row?updateTaggroup:addTaggroup;
						fn(obj).then((res) => {
							this.loading = false;
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
						return false;
					}
				})
			},
			dataFormCancel(){
				this.visible = false;
				this.closeDialog();
			},

			closeDialog() {
				console.log("关闭窗口");
				this.$parent.addEditDataVisible = false;
			},
		},
	}
</script>
<style scoped>

	.skuSelect{
		width: 100%;
	}
</style>

