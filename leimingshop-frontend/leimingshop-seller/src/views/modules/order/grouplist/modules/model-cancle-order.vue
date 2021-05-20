<template>
  <el-dialog
      class="model-add-edit-data"
      title="取消订单"
      :close-on-click-modal="false"
      :visible.sync="visible"
      width="35%">
    <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="addForm"
        v-loading="getDataloading"
        label-width="80px"
    >
      <el-form-item label="取消原因：" prop="reasonId">
        <el-select v-model="dataForm.reasonId">
          <el-option
              v-for="item in reasonIdOptionlist"
              :key="item.id"
              :label="item.content"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer" style="text-align: center;">
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
                           :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
		     		    <el-button @click="dataFormCancel()">返回</el-button>
	</div>
  </el-dialog>
</template>
<script>

	import {orderReasonList, groupOrderCancel} from "@/api/api";
	// import { isNum,isIntNum} from '@/utils/validate'
	import cloneDeep from 'lodash/cloneDeep'


	export default {
		name: "model-add-edit-data",
		data() {
			return {
				visible: false,
				loading: false,
				getDataloading: false,
				dataForm: {
					reasonId: "",//退货原因id
					remarks: '',
				},
				dataRule: {
					reasonId: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
					],
					remarks: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
					]
				},
				row: {},
				reasonIdOptionlist: [
					// {id:1,content:"原因1"},
					// {id:2,content:"原因2"},
					// {id:3,content:"原因3"},
				]
			}
		},
		components: {},
		computed: {},
		mounted() {
		},
		methods: {
			init(row,groupRecordid) {
				this.groupRecordid = groupRecordid
				console.log(row)
				this.visible = true;
				this.row = row;
				this.dataForm.reasonId = ""
				this.dataForm.remarks = ""
				this.getOrderReasonList();
			},
			// 获取原因下拉列表
			getOrderReasonList() {
				var obj = {
					params: {
						type: 3 //类型（0：退货，1：换货，2：申请退款 ，3：取消订单）
					}
				}
				this.getDataloading = true;
				orderReasonList(obj).then((res) => {
					this.getDataloading = false
					if (res.code == "200") {
						this.reasonIdOptionlist = res.data;
					} else {
						this.$message.error(res.msg);
					}
				})
			},
			// 提交
			dataFormSubmit(formName) {
				let that = this
				this.$refs[formName].validate((valid) => {
					if (valid) {
						var obj = {
							"groupRecordId": that.groupRecordid,// 主键 ,
							"reasonId": that.dataForm.reasonId, //订单取消原因Id
						}
						this.loading = true;
						groupOrderCancel(obj).then((res) => {
							this.loading = false;
							// alert(JSON.stringify(res));
							let status = null;
							if (res.code == "200") {
								status = "success";
								this.visible = false;
								this.$emit('searchDataList');
							} else {
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
			dataFormCancel() {
				this.visible = false;
			},
		},
	}
</script>
<style scoped>
  .attrList {
    height: 60px;
    line-height: 90px;
    border-top: 2px dotted gainsboro;
    padding-left: 16px;
  }

  .el-textarea {
    width: 100%;
  }
</style>
