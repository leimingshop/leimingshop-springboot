s<template>
	<el-dialog
	 	    class="model-sku-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
            :before-close="closeDialog"
            width="30%">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		label-width="130px">
				        <el-form-item label="发布类型：" >
                             <el-radio v-model="dataForm.goodsType" label="0">{{this.goodsShow==1?"立即上架":"立即下架"}}</el-radio>
                            <el-radio v-model="dataForm.goodsType" label="1">{{this.goodsShow==1?"定时上架":"定时下架"}}</el-radio>
                        </el-form-item>

                        <el-form-item label="选择日期："  v-if="dataForm.goodsType!=0">
                              <el-date-picker
                               :disabled="dataForm.goodsType==0"
                                style="margin-left:8px;"
                                v-model="dataForm.shelfTime"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择日期时间"
                                :picker-options="pickerOptions"
                                @change="afterTime">
                            </el-date-picker>
                        
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
    import { showBatchGoods,goodsTime,timedShowBatchGoods} from '@/api/api'

	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
					goodsType:"0",//商品发布类型 0:立即发布; 1:定时发布 ,
                    shelfTime:new Date(), //发布时间 ,
				},
				dataRule : {
					// specAttrName: [
			        //   { required: true, message: '必填项不能为空', trigger: 'blur' },
                    // ],
				},
				title:'商品上下架',
                row:"",
              
                pickerOptions: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                    }
                },// 日期组件 设置项
                goodsShow:'',
                ids:[],
			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init (row,ids,goodsShow) {
                 this.row = row;
                this.visible = true;
                this.ids = ids;
                this.goodsShow = goodsShow;
                if(this.goodsShow==1){
                     this.title = "商品上架"
                }else{
                    this.title = "商品下架"
                }
                // if(row.createDate){
				// 	this.dataForm.shelfTime = new Date(row.createDate);
				// }else{
					this.dataForm.shelfTime = new Date();
				// }
               
                Object.assign(this.dataForm,row);
                this.getTimeById();
            },
              getTimeById(){
                var obj = {
                    id:this.row.id
                }
                goodsTime(obj).then((res)=>{
                    console.log(res);
                    if(res.code==200 && res.data){
                        this.dataForm.shelfTime = new Date(res.data.shelfTime);
                        this.dataForm.goodsType=res.data.operationalStatus.toString();;
                    }
                })
            },
             filterTime(value){
                function add0(m){return m<10?'0'+m:m }
                var time = new Date(value);
                var y = time.getFullYear();
                var m = time.getMonth()+1;
                var d = time.getDate();
                var h = time.getHours();
                var mm = time.getMinutes();
                var s = time.getSeconds();
                        return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s)
                    // return y+'-'+add0(m)+'-'+add0(d)
            },
            afterTime(){
                console.log(this.dataForm.shelfTime);
                if(new Date(this.dataForm.shelfTime).getTime() < new Date().getTime()){
                    this.dataForm.shelfTime = this.filterTime(new Date());
                }
            },
			// 提交
			dataFormSubmit(formName){
                  this.afterTime();
				  var obj = {
                    ids:this.ids,
                    goodsShow:this.goodsShow,
                    shelfTime:this.dataForm.goodsType==0?null:this.filterTime(this.dataForm.shelfTime),
                    showType:this.dataForm.goodsType
                }
                  if(obj.showType==0){
                    this.loading = true ;
                    showBatchGoods(obj).then(res=>{
                        let status="";
                        let msg = "";
                        if(res.code=="200"){
                            status= "success"
                            msg = this.goodsShow==1?"商品上架设置成功":"商品下架设置成功"
                            this.$parent.getDataList();
                            this.closeDialog();
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
                }else{
                    timedShowBatchGoods(obj).then(res=>{
                        let status="";
                        let msg = "";
                        if(res.code=="200"){
                            status= "success"
                            msg = this.goodsShow==1?"商品定时上架设置成功":"商品定时下架设置成功"
                            this.$parent.getDataList();
                            this.closeDialog();
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
                 this.visible = false;
                this.$parent.goodsOnOffVisible = false;
            },
		},
	}
</script>
<style scoped>
/deep/ .el-form-item__content{
	display: block;
}
</style>
