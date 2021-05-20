<template>
	<el-dialog
	 	    class="model-add-edit-data"
		    :title="title"
			style="margin-bottom: 10px"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
			:before-close="closeDialog"
			width="57%">
		    	<!-- <el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		@keyup.enter.native="dataFormSubmit('addForm')"
		    		label-width="120px">
				        <el-form-item label="属性名称：" prop="attrName">
				        	  <el-input v-model="dataForm.attrName" maxlength="60" placeholder="请输入属性名称"></el-input>
				        </el-form-item>
						 <el-form-item label="排序：" prop="spSort">
				        	 	 <el-input v-model="dataForm.spSort" placeholder="1-255"></el-input>
								</el-select>
				        </el-form-item>
				</el-form> -->
				<span style="color:red;margin-bottom: 10px;margin-top:20px">多规格商品修改第一个库存，点击【批量】则会批量向下同步所有规格商品库存。</span>
				<el-table
					width="100%"
					:data="goodsSpecPriceAndStorageDTOList"
					border
					v-loading="backScanLoading"
					style="width: 100%;position: relative; top: 0px; margin-top: 10px"
					class="tableBox"
					>
						<div v-for="(item,index) in specAttrNameDTOList" >
							<el-table-column v-if="index==0" width="1" label="" max-width="0" :key="index">
								<template slot-scope="scope">
									<!-- <span>不展示</span> -->
								</template>
							</el-table-column>
							<el-table-column
								 v-if="index!=0"
							    :key="item.index"
								prop="goodsName"
								:label="item.specAttrName"
								align="center">
								<template slot-scope="scope">
									<el-input-number  v-if="index==specAttrNameDTOList.length-2"
													  v-model="scope.row.specStorage" :precision="0" :step="1" :min="0"
													  :max="999999"
													  controls-position="right"
									>
									</el-input-number>
									<span  v-if="index==specAttrNameDTOList.length-1">
										{{scope.row.specActivityStorage}}
									</span>

									<span  v-if="index < specAttrNameDTOList.length-2">
											{{scope.row.specAttrValueDTOList[index-1]?scope.row.specAttrValueDTOList[index-1].specAttrValue:'--'}}
									</span>
								</template>
							</el-table-column>
						</div>
				</el-table>
				<div style="margin-top:10px;margin-bottom:10px;" v-if="!backScanLoading">
					<el-button @click="batch()"  type="primary" size="mini">批量</el-button>
				</div>
				<!-- {{specAttrNameDTOList}}<br>
				{{goodsSpecPriceAndStorageDTOList}} -->
			    <span slot="footer" class="dialog-footer" >
		     		    <el-button @click="dataFormCancel()">返回</el-button>
						 <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>
    // import { livemembertype_add } from '@/api/api2'

	import { updataStorage,backScanPriceAndStorage } from '@/api/api'
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
				backScanLoading:false,
				loading : false,
				dataForm : {},
				dataList:[],
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
							{id: "", specId: null,specAttrName:'库存'},
                            {id: "", specId: null,specAttrName:'活动库存'},
						]
						this.specAttrNameDTOList =  [{}].concat(res.data.specAttrNameDTOList)//表头
						this.specAttrNameDTOList=this.specAttrNameDTOList.concat(array);
						console.log(this.specAttrNameDTOList);

						// if(res.data.goodsSpecPriceAndStorageDTOList.length>0) res.data.goodsSpecPriceAndStorageDTOList[0].specAttrValueDTOList.unshift({});
						this.goodsSpecPriceAndStorageDTOList =  [].concat(res.data.goodsSpecPriceAndStorageDTOList);//表内容
					    this.goodsSpecPriceAndStorageDTOList.forEach((item,index)=>{
							item.beforeSpecStorage = item.specStorage;
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
				// 		if (valid) {
							this.loading = true;
							console.log(this.goodsSpecPriceAndStorageDTOList);
							var obj= this.goodsSpecPriceAndStorageDTOList
							if(this.row) obj.id = this.row.id
							// if(obj.length>0) obj[0].specAttrValueDTOList.shift();
							updataStorage(obj).then((res) => {
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
						let specStorage = 0;
						this.goodsSpecPriceAndStorageDTOList.forEach((item,index)=>{
							if(index==0){
								specStorage = item.specStorage

							}else{
								item.specStorage =specStorage
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
<style lang="scss">
	.tableBox{
		th{
			padding: 0!important;
			height: 40px;
			line-height: 30px;
		}
		td{
			padding: 0!important;
			height: 50px;
			line-height: 30px;
		}
	}

</style>
