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
				        <el-form-item label="地区名称：" prop="areaName">
				        	  <el-input v-model="dataForm.areaName" maxlength="60" placeholder="请输入地区名称"></el-input>
				        </el-form-item>
						<el-form-item label="上级地区：" v-show="!flag" prop="gcIds" >
				        	  <el-cascader
							 	v-model="dataForm.gcIds"
								:options="options"
								:props="props" 
								@change ="finishCange()"
								change-on-select
								clearable></el-cascader>
				        </el-form-item>
						<el-form-item label="所属大区：" v-show="!flag" >
							<el-select v-model="dataForm.regionId" placeholder="请选择" clearable>
								<el-option
									v-for="item in regionList"
									:key="item.id"
									:label="item.regionName"
									:value="item.id">
								</el-option>
							</el-select>
				        </el-form-item>
						<el-form-item label="所在层级：" v-show="flag" >
				        	  <el-input v-model="dataForm.areaDeep" :disabled="flag"  maxlength="40" show-word-limit clearable ></el-input>
				        </el-form-item>
						<el-form-item label="父级ID：" v-show="flag" >
				        	  <el-input v-model="dataForm.areaParentId" :disabled="flag"  maxlength="40" show-word-limit clearable ></el-input>
				        </el-form-item>
					</el-form>
			    <span slot="footer" class="dialog-footer">
		     		    <el-button  @click="dataFormCancel()">返回</el-button>
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>
	import { treeList,info,updateArea,saveArea,regionList } from '@/api/api'
	import cloneDeep from 'lodash/cloneDeep'
    import imgCropper from "@/components/upload/model-photo-cropper2";

	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dialogImageUrl: '',
				type:'1', // 1 添加 2 修改
				dialogVisible: false,
				options:[],
				props:{
					checkStrictly: true ,
                    value:"id",
					label:"areaName",
                },
				flag:false, 
				regionList:[],
				dataForm : {
					options:[],
					areaName: "",
					areaDeep: "",
					areaParentId:'',
					gcIds:[],
					regionId:'',
				},
				dataRule : {
			        areaName : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
					],
				},
				title:'',
				row:"",
			}
		},
		components:{
			imgCropper,
		},
		computed:{},
		mounted(){
			// this.props.checkStrictly = true
		},
		methods:{
			init (row) {
				this.visible = true;
				this.row = row;
				if(row){
					this.title="编辑地区";
					this.backScan();
					this.flag=true
					this.dataRule = {
			         areaName : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
					]}
				}else{
					this.title="新增地区"
				
				}
				this.$nextTick(() => {
					this.$refs['addForm'].resetFields();
						
				})
				this.gettreeList();
				this.getRegion();
			},
			// 获取所有大区
			getRegion(){
				regionList().then((res)=>{
					if(res.code==200){
						this.regionList=res.data
					}
				})
			},
			finishCange(){
				let  len = this.dataForm.gcIds.length;
				this.dataForm.areaDeep=len;
                if(len!=-1){
                    this.dataForm.areaParentId = this.dataForm.gcIds[this.dataForm.gcIds.length-1];
                }
            },
			// 编辑回显
			backScan(){
				var obj  = {
					id:this.row.id
				}
				info(obj).then((res)=>{
					if(res.code == 200){
						Object.assign(this.dataForm,res.data);
					}
				})
			},
			gettreeList(){
				treeList().then((res)=>{
						if(res.code == 200){
						this.options=res.data
						this.options.forEach((item,index)=>{
							if(item.id){
								item.value = item.id
							}
							if(item.areaName){
								item.label=item.areaName
							}
						
						})
						console.log(this.options)
					}
				})
			},
			// 提交
			dataFormSubmit(formName){
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.loading = true;
							var obj={
								"id":this.row.id,
								"areaName": this.dataForm.areaName,
								
							}
							if(this.flag){// 编辑
								updateArea(obj).then((res)=>{
									if(res.code == 200){
										this.$message.success(res.msg)
										this.loading = false;
										this.closeDialog();
										this.$parent.getDataList();
									}
								})
							}else{//新增
								var obj2={
									"areaName": this.dataForm.areaName,
									"areaParentId":this.dataForm.areaParentId,
									"areaDeep":this.dataForm.areaDeep+1
								}
								if(this.dataForm.regionId){
									obj2.regionId=this.dataForm.regionId
								}

								saveArea(obj2).then((res)=>{
									if(res.code==200){
										this.$message.success(res.msg)
										this.loading = false;
										this.closeDialog();
										this.$parent.getDataList();
									}
								})
							}
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
 .attrList{
	 height: 60px;
	 line-height: 90px;
	 border-top: 2px dotted gainsboro;
	 padding-left: 16px;
 }
 
</style>
