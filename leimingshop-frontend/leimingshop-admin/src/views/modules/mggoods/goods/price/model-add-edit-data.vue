<template>
	<el-dialog
	 	    class="model-add-edit-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
			:before-close="closeDialog"
			width="57%">
				
				<span style="color:red">多规格商品修改第一个价格，点击【批量】则会批量向下同步所有规格商品价格。</span>
				<el-table
					width="100%"
					:data="goodsSpecPriceAndStorageDTOList"
					border
					v-loading="backScanLoading"
					style="width: 100%;position: relative;"
					>  
							
						<!-- <el-table-column
							v-if="goodsSpecPriceAndStorageDTOList.length>0"
							prop="goodsName"
							:label="goodsSpecPriceAndStorageDTOList[0].specAttrNameDTOList[0].specAttrName"
							align="center">
							<template slot-scope="scope">
									<span>
										123
									</span>
							</template>
						</el-table-column>	 -->

				
						<div  v-for="(item,index) in specAttrNameDTOList">
								
							<el-table-column v-if="index==0" width="1" label="" max-width="0" :key="index">
								<template slot-scope="scope">
									<!-- <span>不展示</span> -->
								</template>
							</el-table-column>

							<el-table-column
							   v-if="index!=0"
							    :key="index"
								prop="goodsName"
								:label="item.specAttrName"
								align="center">
								<template slot-scope="scope">
									<el-input-number  v-if="index==specAttrNameDTOList.length-2"
											 v-model="scope.row.specSellPrice" :precision="2" :step="0.1" :min="0.01"
											 :max="999999.99"
											 controls-position="right" 
											>
									</el-input-number>

									<el-input-number  v-else-if="index==specAttrNameDTOList.length-1"
											 v-model="scope.row.specCostPrice" :precision="2" :step="0.1" :min="0.01"
											 :max="999999.99"
											 controls-position="right" 
											>
									</el-input-number>

									<span v-else>
										<!-- {{index}}{{scope.row.specAttrValueDTOList}} -->
										{{scope.row.specAttrValueDTOList[index-1]?scope.row.specAttrValueDTOList[index-1].specAttrValue:'--'}}
									</span>
								</template>
							</el-table-column>
						</div>
						
				</el-table>
				<div style="margin-top:10px;margin-bottom:10px;" v-if="!backScanLoading">
					<el-button @click="batch()"   type="primary"  size="mini">批量</el-button>
				</div>
					<!-- {{specAttrNameDTOList}}<br>
					{{goodsSpecPriceAndStorageDTOList}} -->
			    <span slot="footer" class="dialog-footer">
		     		  <el-button @click="dataFormCancel()">返回</el-button>
						 <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>
    // import { livemembertype_add } from '@/api/api2'
	
	import { uptateprice,backScanPriceAndStorage } from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'
import { setTimeout } from 'timers';


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
				backScanLoading:false,
				loading : false,
				dataForm : {},
				specAttrNameDTOList:[],//表头
				goodsSpecPriceAndStorageDTOList:[],//表内容
				title:'',
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
				this.title=row.goodsName;
				this.backScan();
			},
			// 编辑回显
			backScan(){
				var obj  = {
					id:this.row.id
				}
				this.backScanLoading = true;
				backScanPriceAndStorage(obj).then((res)=>{
					this.backScanLoading = false;
					if(res.code == 200){
						var array = [
							{id: "", specId: null,specAttrName:'售价'},
							{id: "", specId: null,specAttrName:'成本价'},
						]
						this.specAttrNameDTOList = [{}].concat(res.data.specAttrNameDTOList)//表头
						this.specAttrNameDTOList=this.specAttrNameDTOList.concat(array);
						console.log(this.specAttrNameDTOList);
						// var obj= {}
						// if(res.data.goodsSpecPriceAndStorageDTOList.length>0) {obj = cloneDeep(res.data.goodsSpecPriceAndStorageDTOList[0].specAttrValueDTOList[0])}
						// if(res.data.goodsSpecPriceAndStorageDTOList.length>0) res.data.goodsSpecPriceAndStorageDTOList[0].specAttrValueDTOList.unshift({});
						this.goodsSpecPriceAndStorageDTOList = [].concat(res.data.goodsSpecPriceAndStorageDTOList);//表内容
						this.goodsSpecPriceAndStorageDTOList.forEach((item,index)=>{
							item.beforeSpecCostPrice = item.specCostPrice
							item.beforeSpecSellPrice = item.specSellPrice
						})
					}else{

					}
				})
			},
			
			// 提交
			dataFormSubmit(formName){
				// alert([this.dataForm.name,this.dataForm.domainAddress]);
				// console.log(this.dataForm);
				// this.$refs[formName].validate((valid) => {
						// if (valid) {
							this.loading = true;
							console.log(this.goodsSpecPriceAndStorageDTOList);
							
							var obj= cloneDeep(this.goodsSpecPriceAndStorageDTOList);
							// if(obj.length>0) obj[0].specAttrValueDTOList.shift();
							if(this.row) obj.id = this.row.id
							uptateprice(obj).then((res) => {
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
				// 		} else {
				// 				//console.log('error 添加失败!!');
				// 				return false;
				// 		}
				// })
			},
			dataFormCancel(){
					this.visible = false;
					this.closeDialog();
			},
			// 批量
			batch(){
				if(this.goodsSpecPriceAndStorageDTOList==0){
						return;
				}
				 this.$confirm('是否确定批量修改?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
						let specSellPrice = 0;
						let specCostPrice = 0;
						// let specStorage = 0;
						this.goodsSpecPriceAndStorageDTOList.forEach((item,index)=>{
							if(index==0){
								specSellPrice = item.specSellPrice
								specCostPrice = item.specCostPrice
								// specStorage = item.specStorage

							}else{
								item.specSellPrice =specSellPrice
								item.specCostPrice =specCostPrice
								// item.sspecStorage =specStorage
							}
						})
					
				}).catch(() => {});
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
 .el-input-number{
	 width: auto !important;
 }
</style>
