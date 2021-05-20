<template>
	<el-dialog
	 	    class="model-sku-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
				:before-close="closeDialog">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		label-width="130px">
				        <el-form-item label="商品类目：" prop="className">
				        	  <span>{{dataForm.className}}</span> 
				        </el-form-item>
							
				         <el-form-item label="商品规格：" prop="specAttrName">
				        	 	 <span v-if="dataForm.specAttrName">{{dataForm.specAttrName.replace(/,/g,' - ')}}</span>
								   <span v-else>--</span>
				        </el-form-item>

				        <el-form-item label="sku名称：" prop="specName" style="width:90%">
							<el-input v-model="dataForm.specName"    placeholder="请输入"></el-input>
                            <!-- <span>{{dataForm.goodsName}}</span> -->
				        </el-form-item>
                        
                         <el-form-item label="售价：" prop="specSellPrice">
                            <el-input-number v-model="dataForm.specSellPrice" :precision="2" :step="0.1" :min="0.01"  :max="999999.99" placeholder="请输入"></el-input-number>
                            <span>元</span>
				        </el-form-item>

                         <el-form-item label="成本价：" prop="specCostPrice">
                            <el-input-number v-model="dataForm.specCostPrice" :precision="2" :step="0.1" :min="0.01"  :max="999999.99" placeholder="请输入"></el-input-number>
                            <span>元</span>
				        </el-form-item>

                        <el-form-item label="SKU库存：" prop="specStorage">
                            <el-input-number v-model="dataForm.specStorage"  :min="0"  :max="999999" placeholder="请输入" precision="0"></el-input-number>
                            <span>件</span>
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
			 //数字
	        var validateNum = (rule, value, callback) => {
		        if (!isIntNum(value)) {
		          callback(new Error('请输入正确的天数'))
		        } else {
		          callback()
		        }
	        }
			return{
				visible : false,
				loading : false,
				dataForm : {
					className: "童鞋>男童鞋>运动鞋",
                    id: "1138287273546715137",
                    specAttrName: "",//颜色
                    specCostPrice: 999,
                    specName: "",//测试商品 黑色 xs
                    specSellPrice: 999,
                    specStorage: 0,
				},
				dataRule : {
					//specAttrName: [
			        //  { required: true, message: '必填项不能为空', trigger: 'blur' },
                   // ],
			        className : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
                    ],
                    goodsName : [
			          { regoodsNamequired: true, message: '必填项不能为空', trigger: 'blur' },
					],
					specName : [
			          { regoodsNamequired: true, message: '必填项不能为空', trigger: 'blur' },
                    ],
                    specSellPrice : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
                    ],
                    specCostPrice : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
                    ],
                    specStorage : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
                    ],
				},
				optionsApplication: [],
				optionsRight: [],
				title:'编辑SKU',
				row:"",
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
				Object.assign(this.dataForm,row);
				console.log(this.dataForm);
                this.backScan();
			},
			// 编辑回显
			backScan(){
                var obj  = {
                    id:this.row.id
                }
                backScangoodsSpec(obj).then((res)=>{
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
                        var obj = this.dataForm
                        if(this.row) obj.id = this.row.id
                        updateGoodsSpecSku(obj).then((res) => {
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
                this.$parent.skuDataVisible = false;
            },
		},
	}
</script>
<style scoped>

</style>
