s<template>
	<el-dialog
	 	    class="model-sku-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
            :before-close="closeDialog"
            width="35%">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		label-width="130px">
				        <el-form-item label="销售价格：" style="width:85%">
                            <el-input v-model="dataForm.firstSpecSellPrice" type="number" show-word-limit placeholder="请输入销售价格" ></el-input>
                        </el-form-item>

                        <el-form-item label="成本价格：" style="width:85%">
                            <el-input v-model="dataForm.firstSpecCostPrice" type="number" show-word-limit  placeholder="请输入成本价格" ></el-input>
                        </el-form-item>

                          <el-form-item label="库存：" style="width:85%">
                            <el-input v-model="dataForm.firstSpecStorage" type="number" show-word-limit  placeholder="请输入库存" ></el-input>
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
    // import { livemembertype_add } from '@/api/api2'
	
	import { addAttribute,backScangoodsSpec,updateGoodsSpecSku } from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'

	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
                    firstSpecSellPrice:'',
                    firstSpecCostPrice:'',
                    firstSpecStorage:'',
				},
				dataRule : {
					firstSpecSellPrice: [
			          { required: true, message: '销售价格不能为空', trigger: 'blur' },
                    ],
					firstSpecCostPrice: [
			          { required: true, message: '成本价格不能为空', trigger: 'blur' },
                    ],
					firstSpecStorage: [
			          { required: true, message: '库存不能为空', trigger: 'blur' },
                    ],
				},
				optionsApplication: [],
				optionsRight: [],
				title:'批量设置',
                row:"",
              
                pickerOptions: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                    }
                },// 日期组件 设置项
			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init () {
                this.visible = true;
                // Object.assign(this.dataForm,row);
			},
			// 提交
			dataFormSubmit(formName){
				console.log([this.dataForm.firstSpecSellPrice,this.dataForm.firstSpecCostPrice,this.dataForm.firstSpecStorage])
				console.log([this.dataForm.firstSpecSellPrice=="" ,this.dataForm.firstSpecCostPrice=="" ,this.dataForm.firstSpecStorage=="" ]);
				if(this.dataForm.firstSpecSellPrice=="" && this.dataForm.firstSpecCostPrice=="" && this.dataForm.firstSpecStorage==""){
					this.$message.info("至少填一项");
					return;	
				} 
				this.$refs[formName].validate((valid) => {
					if(valid){
						var m1 =  this.dataForm.firstSpecSellPrice? "销售价":'';
						var m2 = this.dataForm.firstSpecCostPrice? "成本价":'';
						var m1_2 =m2? "、":'';
						var m3 = this.dataForm.firstSpecStorage? "库存":'';
						var m2_3 =m3? "、":'';
								
						this.$confirm('是否确定需要批量设置'+m1+''+m1_2+''+m2+''+m2_3+''+m3+'?', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.$emit("batchCbFn", this.dataForm);
							this.closeDialog();
						}).catch(() => {
						});
					}
				});
			 },
		
			dataFormCancel(){
                this.visible = false;
                this.closeDialog();
			},
			closeDialog() {
                this.$parent.goodsBatchVisible = false;
            },
		},
	}
</script>
<style scoped>

</style>
