s<template>
	<el-dialog class="model-sku-data"
		       :title="textStr"
		       :close-on-click-modal="false"
		       :visible.sync="visible"
               :before-close="closeDialog"
               width="30%">
		<el-form ref="addForm"
		         label-width="130px">
            <el-form-item label="" >
                <el-radio v-model="auditType" label="4">{{textStr.indexOf("审核")!==-1?"审核通过":"发放通过"}}</el-radio>
                <el-radio v-model="auditType" label="3">{{textStr.indexOf("审核")!==-1?"审核驳回":"发放驳回"}}</el-radio>
            </el-form-item>
            <el-form-item label="拒绝理由：" v-if="auditType!=4">
                <el-input v-model="auditTextStr"
                          placeholder="请输入拒绝理由"
                          maxlength="15"
                          clearable>
                </el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dataFormCancel()">
                取消
            </el-button>
            <el-button type="primary" 
                       @click="dataFormSubmit()">
                {{"确定"}}
            </el-button>
        </span>
	</el-dialog>
</template>
<script>
	import { addAttribute,backScangoodsSpec,updateGoodsSpecSku } from '@/api/api'
    import cloneDeep from 'lodash/cloneDeep'
    import { showBatchGoods,goodsTime,timedShowBatchGoods} from '@/api/api'

	export default{
        props: {
			textStr :{
				type: String,
				default: "批量审核",
            }
		},
		data(){
			return{
				visible : false,
				loading : false,
                auditType: '4',
                auditTextStr:''
			}
        },
		methods:{
			init () {
                this.visible = true;
            },

			// 确定
			dataFormSubmit(){
                if (parseInt(this.auditType)!=3) {
                    this.auditTextStr='';
                } 
	            this.$emit('determineSubmit',this.auditType,this.auditTextStr);
            },

            //取消
			dataFormCancel(){
                this.visible = false;
                this.closeDialog();
            },
            
			closeDialog() {
                this.visible = false;
                this.$parent.showFlag = false;
            },
		},
	}
</script>
<style scoped>

</style>
