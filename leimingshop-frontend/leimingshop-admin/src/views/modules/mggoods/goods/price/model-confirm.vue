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
				        	  <el-input v-model="dataForm.groupName" maxlength="30" placeholder="请输入分组名称"></el-input>
				        </el-form-item>
				        
				         <el-form-item label="分组排序：" prop="sort">
				        	 	 <el-input v-model="dataForm.sort" placeholder="1-255"></el-input>
				        </el-form-item>

						<el-form-item label="选择标签：" prop="attrIds">
                                 <el-select
                                    class="skuSelect"
                                    v-model="dataForm.attrIds"
                                    multiple
                                    filterable
                                    remote
                                    reserve-keyword
                                    placeholder="请输入关键词"
                                    :remote-method="getAttributeList"
                                    :loading="skuLoading">
										<el-option
											v-for="(item,index) in skuOptions"
											:key="index"
											:label="item.attrName"
											:value="item.id">
										</el-option>
                                </el-select>
                        </el-form-item>
					</el-form>
			    <span slot="footer" class="dialog-footer">
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
		     		    <el-button @click="dataFormCancel()">返回</el-button>
			    </span>
	</el-dialog>
</template>
<script>
 
	import { addAttributegroup,backScanAttributegroup,updateAttributegroup} from '@/api/api'
	import {attributeList} from '@/api/api'

	export default{
		name: "model-add-edit-data",
		data(){
			
			return{
				visible : false,
                loading : false,
                skuLoading:false,
				dataForm : {
					groupName: "",
					sort:'',
                    attrIds:[],
				},
				dataRule : {
			        groupName : [
			            { required: true, message: '必填项不能为空', trigger: 'blur' },
			        ],
			        sort: [
			            { required: true, message: '必填项不能为空', trigger: 'blur' },
					],
					attrIds: [
			            { required: true, message: '必填项不能为空', trigger: 'blur' },
					],
				},
				 optionsApplication: [],
				 optionsRight: [],
				 InitSkuOptions:[],
                 skuOptions: [],
                //  list:[],
                //  states: [],
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
                this.visible = true;
				// 获取属性下拉列表
				this.getAttributeList();
                if(row){
                    this.title="编辑分组"
                    this.row = row;
                    // this.dataForm.groupName = row.groupName + "";
                    // this.dataForm.sort = row.sort;
					// 还需要获取标签列表
					this.backScan();
                }else{
                    this.title= "新建分组"
                }
				
				this.$nextTick(() => {
                    this.$refs['addForm'].resetFields();
				})
			},
			// 编辑回显
			backScan(){
				var obj  = {id:this.row.id}
				backScanAttributegroup(obj).then((res)=>{
					if(res.code == 200){
						Object.assign(this.dataForm,res.data);
						if(res.data.attrDTOList){
								this.InitSkuOptions = res.data.attrDTOList;
								this.InitSkuOptions.forEach((item,index)=>{
									this.dataForm.attrIds.push(item.id);
								})
								
						}
					}else{
					}
				})
			},
			// 获取属性下拉列表
			getAttributeList(query){
			  	var obj = {
					params:{
						page:1,
						limit:50,
						// groupName:this.row.groupName?this.row.groupName:'',
						attrName:query?query:'',
					}
				}
				 this.skuLoading = true;
				 this.skuOptions = [];
				attributeList(obj).then((res)=>{
					 this.skuLoading = false;
					 Object.assign(this.skuOptions,this.InitSkuOptions)
					 if(res.code == 200 && res.data.list){
						Object.assign(this.skuOptions,res.data.list)
						console.log("this.skuOptions");
						console.log(this.skuOptions);
					 }
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
                                    "sort":  this.dataForm.sort, // 属性分组排序
                                    "attrIds":  this.dataForm.attrIds,//关联属性id ,
								}
								if(this.row) obj.id = this.row.id
                                var fn = this.row?updateAttributegroup:addAttributegroup;
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
