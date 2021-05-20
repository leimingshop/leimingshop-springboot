<template>
	<el-dialog
	 	    class="model-whether-edit"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
            :before-close="closeDialog"
            width="30%">
			    <span slot="footer" class="dialog-footer">
                        <el-button @click="dataFormCancel()">返回</el-button>
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>

    import cloneDeep from 'lodash/cloneDeep'
import { goodsSpecShow,goodsSpecTime ,timedGoodsSpecShow} from '@/api/api'

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
					goodsType:"0",//商品发布类型 0:立即发布; 1:定时发布 ,
				},

				title:'编辑SKU',

				row:"",
				ids:[],
				specShow:'',
				goodsId:''

			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init (row,ids,goodsId,specShow) {
				this.visible = true;
				this.goodsId=goodsId;
                this.row = row;
                this.ids = ids;
				this.specShow = specShow;
				Object.assign(this.dataForm,row);
			},

			// 提交
			dataFormSubmit(formName){
				this.loading = true;
		 	 	this.afterTime();
				var obj = {
					ids:this.ids,
					goodsId:this.goodsId,
					specShow:this.specShow,
					shelfTime:this.dataForm.goodsType==0?null:this.filterTime(this.dataForm.shelfTime),
					showType:this.dataForm.goodsType
					
				}
				if(obj.showType==0){
					goodsSpecShow(obj).then(res=>{
						let status="";
						let msg = "";
						if(res.code=="200"){
							status= "success"
							msg = this.specShow ==1 ? "sku上架设置成功":"sku下架设置成功"
							this.$parent.getDataList();
							this.closeDialog();
                            this.$router.push({
                                path: 'mggoods-add',
                                query: {
                                    gcId: this.ids,
                                }
                            })
						}else{
							status= "error"
							msg = res.msg;
						}
						this.$message({
							message: msg,
							type:status,
							duration: 1500,
						})
					})
				}
			},
			dataFormCancel(){
                this.visible = false;
                this.closeDialog();
			},
			closeDialog() {
                this.$parent.skuOnOffVisible = false;
            },
		},
	}
</script>
<style scoped>
	/deep/ .el-form-item__content{
		display: block;
	}
</style>
