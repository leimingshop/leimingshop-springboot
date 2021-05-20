<template>
	<el-dialog
	 	    class="model-add-edit-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
			width="35%"
				:before-close="closeDialog">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		@keyup.enter.native="dataFormSubmit('addForm')"
		    		>

						<h3 v-if="row.inspectType==1" style="text-align:center">确定审核通过？</h3>

						<el-input v-else
							type="textarea"
							:rows="2"
							placeholder="请填写拒绝原因"
							v-model="dataForm.rejectReason">
						</el-input>
							
				</el-form>
			    <span slot="footer" class="dialog-footer">
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
		     		    <el-button @click="dataFormCancel()">返回</el-button>
			    </span>
	</el-dialog>
</template>
<script>
	
	import { checkGoods} from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'


	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
					ids:[],
					goodsStatus:'',//商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布
					rejectReason:'',//拒绝原因
				},
			
				dataRule : {
			        // goodsStatus : [
			        //   { required: true, message: '必填项不能为空', trigger: 'blur' },
			        // ],
				},
				row:'',
				title:'',
			
			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init (row) {
				this.visible = true;
				this.row = row;
				this.dataForm.ids = row.ids
				if(row.inspectType==1){//通过
					this.title = "审核提示";
					this.dataForm.goodsStatus = 30;
				}else{//拒绝
					this.title = "确定拒绝审核通过？";
					this.dataForm.goodsStatus = 20;
				}
				
			},
			// 提交
			dataFormSubmit(formName){
				if(this.row.inspectType != 1  && this.dataForm.rejectReason.replace(/ /g,"")=="" ){
					   this.$message({
							message:"请填写拒绝原因",
							type: "warning",
							duration: 1500
						})
						return;
				}
				this.$refs[formName].validate((valid) => {
						if (valid) {
								this.loading = true;
								var obj=  {
									goodState:this.dataForm.goodsStatus,
									goodsIds:this.dataForm.ids,
									remarks:this.dataForm.rejectReason, 
								}
								checkGoods(obj).then((res) => {
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
 .el-textarea{
	 width: 100%;
 }
</style>
