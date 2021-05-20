 <template>
  <el-dialog title="填写备注" :visible.sync="visible">
      <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="addForm"
        >
        <el-form-item label="备注信息：" prop="sellerRemark">
          <el-input type="textarea" v-model="dataForm.sellerRemark" maxlength="50" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="等级："  prop="sellerRemarkGrade" class="remark">
          <el-radio-group v-model="dataForm.sellerRemarkGrade" style="margin-top: 3px">
            <el-radio label="1">一级</el-radio>
            <el-radio label="2">二级</el-radio>
            <el-radio label="3">三级</el-radio>
            <el-radio label="4">四级</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" plain @click="dataFormCancel">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmit('addForm')">确 定</el-button>
      </div>
    </el-dialog>

 </template>
<script>
    import cloneDeep from 'lodash/cloneDeep'
    import {sellerRemarkInfo} from "@/api/api"
	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
                    sellerRemark:'',
                    sellerRemarkGrade:'4',
				},
				dataRule : {
					sellerRemark: [
						{ required: true, message: '必填项不能为空', trigger: 'blur' },
					],
					sellerRemarkGrade: [
						{ required: true, message: '必填项不能为空', trigger: 'blur' },
					]
				},
				optionsApplication: [],
				optionsRight: [],
				title:'批量设置',
                row:"",
                data:""
			}
		},
		computed:{},
		mounted(){},
		methods:{
			init (data) {
                this.visible = true;
                this.data = data;
			},
			// 提交
			dataFormSubmit(formName){
				this.$refs[formName].validate((valid) => {
					if(valid){
                        var obj = {
                            id:this.data.id,
                            sellerRemark:this.dataForm.sellerRemark,
                            sellerRemarkGrade:this.dataForm.sellerRemarkGrade,
                        }
						sellerRemarkInfo(obj).then((res)=>{
                            if(res.code==200){
                                this.$message.success(res.msg);
                                this.$parent.data.disable=1;
                                this.$parent.data.sellerRemark = this.dataForm.sellerRemark;
                                this.$parent.data.sellerRemarkGrade = this.dataForm.sellerRemarkGrade,
                                this.closeDialog();
                            }else{
                                this.$message.error(res.msg);
                            }
                        })
					}
			});
			},
			dataFormCancel(){
                this.visible = false;
                this.closeDialog();
			},
			closeDialog() {
                this.$parent.remarkVisible = false;
            },
		},
	}
</script>
<style lang="scss" scoped>
.remark {
  /deep/ .el-form-item__content {
    display: inline-block!important;
  }
}
</style>
