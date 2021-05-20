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
				        	  <el-input v-model="dataForm.groupName" maxlength="30" show-word-limit placeholder="请输入分组名称"></el-input>
				        </el-form-item>
				        
						<el-form-item label="选择规格：" prop="specIds"> 
                                 <el-select
                                   class="skuSelect"
                                    v-model="dataForm.specIds"
                                    multiple
                                    filterable
                                    placeholder="请输入关键词"
                                    :remote-method="getSpecAllList"
                                    :loading="specLoading">
									 <!-- remote
                                    reserve-keyword -->
                                        <el-option
                                        v-for="(item,index) in specOptions"
                                        :key="index"
                                        :label="item.specName"
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
 
	import { addSpecgroup,backScanSpecgroup,updateSpecgroup} from '@/api/api'
	import {specAllList} from '@/api/api'
import cloneDeep from 'lodash/cloneDeep'
	export default{
		name: "model-add-edit-data",
		data(){
			
			return{
				visible : false,
                loading : false,
                specLoading:false,
				dataForm : {
					groupName: "",
                    specIds:[],
				},
				dataRule : {
			        groupName : [
			            { required: true, message: '必填项不能为空', trigger: 'blur' },
			        ],
					specIds: [
			            { required: true, message: '必填项不能为空', trigger: 'blur' },
					],
				},
				InitSpecOptions:[],
                specOptions: [],
				title:'',
				row:"",
			}
		},
		components:{
        },
		computed:{},
        mounted() {
            // this.list = this.states.map((item,index) => {
            //     return { value: index+100, label: item };
            // }); 
        },
		methods:{
			init (row) {
				// 获取属性下拉列表
			
                if(row){
                    this.title="编辑分组"
                    this.row = row;
                    this.dataForm.groupName = row.groupName + "";
					// 还需要获取标签列表
					this.backScan();
                }else{
					this.title= "新建分组"
					this.getSpecAllList();
                }
				
				this.$nextTick(() => {
                    this.$refs['addForm'].resetFields();
				})
			},
			// 编辑回显
			backScan(){
				var obj  = {id:this.row.id}
				backScanSpecgroup(obj).then((res)=>{
					if(res.code == 200){
						Object.assign(this.dataForm,res.data);
						if(res.data.specDTOList){
							this.InitSpecOptions = cloneDeep(res.data.specDTOList);
							this.InitSpecOptions.forEach((item,index)=>{
								this.dataForm.specIds.push(item.id);
							})
						}
					}
					this.getSpecAllList();
				})
			},
			getSpecAllList(query){
				
			  	var obj = {
					params:{
					specName:query
					}
					
				}
				this.specLoading = true;
				specAllList(obj).then((res)=>{
					
					this.specLoading = false;
					var specOptions = [];
					// console.log("this.InitSpecOptions");
					// console.log(this.InitSpecOptions);
					if(res.code == 200 && res.data){
						specOptions =res.data;
						// console.log("specOptions");
						// console.log(specOptions);
					}
					// var  arr = this.InitSpecOptions.concat(specOptions)
					// var arr = [...this.InitSpecOptions,...this.specOptions,...specOptions]
					var arr = [...this.InitSpecOptions,...specOptions]
				   // js中数组对象去重的方法
					var obj = {};
					arr = arr.reduce(function(item, next) {
					obj[next.id] ? '' : obj[next.id] = true && item.push(next);
						return item;
					}, []);
					this.specOptions = arr;
					console.log("this.specOptions");
					console.log(this.specOptions);
					this.visible = true;
				})
			},

// 提交
			dataFormSubmit(formName){
				// alert([this.dataForm.name,this.dataForm.domainAddress]);
				this.$refs[formName].validate((valid) => {
						if (valid) {
								this.loading = true;
                                var obj = {
                                    "groupName":  this.dataForm.groupName,//属性分组名称 ,
                                    "specIds":  this.dataForm.specIds,//关联属性id ,
								}
								if(this.row) obj.id = this.row.id
                                var fn = this.row?updateSpecgroup:addSpecgroup;
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
	 border-top: 2px solid gainsboro;
	 padding-left: 16px;
 }
 .skuSelect{
    width: 100%;
 }
</style>
