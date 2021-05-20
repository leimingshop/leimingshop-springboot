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
				        <el-form-item label="标签名称：" prop="labelName">
				        	  <el-input v-model="dataForm.labelName" maxlength="5" placeholder="请输入标签名称"></el-input>
				        </el-form-item>

						<el-form-item label="已选分组：" prop="groupIds">
                                 <el-select
                                   class="skuSelect"
                                    v-model="dataForm.groupIds"
                                   multiple
								   :multiple-limit="3"
                                    filterable
                                    placeholder="请输入关键词"
                                    :remote-method="getTagAllList"
                                    :loading="tagLoading">
									  <!-- remote
                                    reserve-keyword -->
                                        <el-option
                                        v-for="(item,index) in groupOptions"
                                        :key="index"
                                        :label="item.groupName"
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


import { addTag,backScanTag,updateTag } from '@/api/api'
import {tagAllList} from '@/api/api'


	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				uploading:false,
                tagLoading:false,
				currentIndex:-1,
				dialogImageUrl: '',
				dialogVisible: false,
				dataForm : {
					groupName:"",
					labelName: "",
					groupValueDTOList:[],
					// 测试发现如果不在下面添加 input输入不进去（代码动态添加也不行）
					groupValue0:"",
					groupValue1:"",
					groupValue2:"",
					groupValue3:"",
					groupValue4:"",
					groupValue5:"",
					groupValue6:"",
					groupValue7:"",
					groupValue8:"",
					groupValue9:"",
					groupValue10:"",
					groupValue11:"",
					groupValue12:"",
					groupValue13:"",
					groupValue14:"",
					groupValue15:"",
					groupValue16:"",
					groupValue17:"",
					groupValue18:"",
					groupValue19:"",
					groupValue20:"",
				},
				groupOptions:[],
				dataRule : {
			        labelName : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
			        ],
				},
				title:'',
				row:"",
			}
		},
		components:{
		},
		computed:{},
		mounted(){},
		methods:{
		getTagAllList(query){

			  	var obj = {
					params:{
						groupName:query
					}
				}
				this.tagLoading = true;
				tagAllList(obj).then((res)=>{

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
					console.log(this.groupOptions);
				})
			},

			init (row) {
				this.visible = true;
				this.row = row;
				if(row){
					this.title="编辑商品标签";
					this.backScan();
				}else{
					this.title="新建商品标签"
				}
				this.$nextTick(() => {
						this.$refs['addForm'].resetFields();
				})
				this.getTagAllList()
			},
			// 编辑回显
			backScan(){
				  var obj  = {
							id:this.row.id
					}
					backScanTag(obj).then((res)=>{
							if(res.code == 200){
									Object.assign(this.dataForm,res.data);
									// 属性单独处理回显
									res.data.groupValueDTOList.forEach((item,index)=>{
										this.dataForm["groupValue"+index] = item.labelValueName;
										this.$nextTick(()=>{
											if(this.$refs.cropperImg[index]){
												this.$refs.cropperImg[index].cropper.imgShow = true;
												this.$refs.cropperImg[index].cropper.cropImg = item.groupValueImage;
											}
										})
									})
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
                                    "labelName":  this.dataForm.labelName,//标签名称 ,
                                    "groupIds":  this.dataForm.groupIds,//关联标签分组id ,
								}
								if(this.row) obj.id = this.row.id
                                var fn = this.row?updateTag:addTag;
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

