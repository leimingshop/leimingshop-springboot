<template>
	<el-dialog
        class="model-sku-data"
        title="操作流水"
        :close-on-click-modal="false"
        :visible.sync="visible"
        :before-close="closeDialog">

            <el-table
                width="100%"
                :data="dataList"
                border
                v-loading="dataListLoading"
                style="width: 100%"
        >
            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    <!-- {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }} -->
                     {{scope.$index+1}}
                </template>
            </el-table-column>

			<el-table-column label="操作人"  prop="operator"  align="center">
            </el-table-column>

			<el-table-column label="操作类型" prop="submitType" align="center">
                <template slot-scope="scope">
					<span v-if="scope.row.submitType ==1">提交</span>
					<span v-if="scope.row.submitType ==2">审核</span>
                </template>
            </el-table-column>

			<el-table-column label="操作内容" prop="auditStatus" align="center">
                 <template slot-scope="scope">
					<span v-if="scope.row.auditStatus ==10">提交申请</span>
					<span v-if="scope.row.auditStatus ==20">审核通过</span>
                    <span v-if="scope.row.auditStatus ==30">审核拒绝</span>
                </template>
            </el-table-column>

			<el-table-column label="操作时间" prop="createDate" align="center">
            </el-table-column>

			<el-table-column label="备注" prop="remark" align="center" :show-overflow-tooltip="true">
            </el-table-column>


        </el-table>
        <div style="text-align:center;margin-top:20px">
            <span slot="footer" class="dialog-footer">
                <el-button @click="dataFormCancel()">返回</el-button>
            </span>
        </div>

	</el-dialog>
</template>
<script>
import { storeAuditTally } from "@/api/api.js"
export default {
	data () {
		return {
            dataList:[],
            dataListLoading:false,
            visible:false,
			row:'',
		}
	},
	components:{
	},
	methods:{
        init(row){
            this.visible = true;
            this.row = row;
            this.backScan();
        },
        backScan(){
            var obj = {
                params:{
                    contentId:this.row.id, // 操作内容ID
                    auditType:1, // 审核类型（1 店铺普通信息 2 店铺公司信息 3 店铺入住审核
                }
            }
            this.dataListLoading = true;
            storeAuditTally(obj).then((res)=>{
                this.dataListLoading = false
                if(res.code==200){
                    this.dataList = res.data;
                }
            })
        },
        dataFormCancel(){
            this.visible = false;
            this.closeDialog();
        },
        closeDialog() {
            this.$parent.operationFlowVisible = false;
        },
	}
}
</script>
<style>
</style>
