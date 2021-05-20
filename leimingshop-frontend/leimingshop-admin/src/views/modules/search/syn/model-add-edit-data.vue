<template>
    <el-dialog
            class="model-add-edit-data"
            :title="title"
            :close-on-click-modal="false"
            :visible.sync="visible"
            :before-close="closeDialog"
            width="26%"
    >
        <el-form
                :model="dataForm"
                ref="addForm"
                :rules="rules" 
                @keyup.enter.native="dataFormSubmit('addForm')"
                label-width="120px"
        >
            <el-form-item label="同义词：" prop="name">
                <el-input type="textarea" v-model="dataForm.name" placeholder="请输入300字以内的内容" :rows="5"></el-input>
                <span style="color: #999999;">多词请用英文逗号“,”隔开</span>
            </el-form-item>
<!--            <el-form-item style="text-align: center;margin-left: -120px!important;">-->
<!--                <el-button type="primary" @click="dataFormSubmit('addForm')"-->
<!--                           :loading="loading">{{loading ? "提交中···" : "确定"}}</el-button>-->
<!--                <el-button  @click="dataFormCancel()">返回</el-button>-->
<!--            </el-form-item>-->
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dataFormCancel()">取消</el-button>
            <el-button type="primary" @click="dataFormSubmit('addForm')"
                       :loading="loading">{{loading ? "提交中···" : "确定"}}</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import { editShopsynonym,shopsynonymSave, backScanShopsynonym } from '@/api/api'
    export default {
        name: "model-add-edit-data",
        data () {
            return {
                visible : false,
                loading : false,
                dataForm: {
                    name: "",//同义词
                },
                title:'',
                row:"",
                formLabelWidth: '120px',
                rules: {
		          name: [
		            { required: true, message: '请输入同义词', trigger: 'blur' }
		          ],
		        },
            }
        },
        watch: {
            'dataForm.name': function (newV, oldV) {
                var chineseCount = 0, characterCount = 0;
                for (let i = 0; i < newV.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
                        chineseCount = chineseCount + 2;
                    } else { //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 600) { //输入字符大于600的时候过滤
                        this.dataForm.name = newV.substr(0, (chineseCount / 2 + characterCount) - 1)
                    }
                }
            }
        },
        methods: {
            init (row) {
                this.visible = true;
                this.row = row;
                if(row){
                    this.title="编辑同义词";
                    this.backScan();
                }else{
                    this.title="新建同义词"

                }
                this.$nextTick(() => {
                    this.$refs['addForm'].resetFields();
                    // this.getApplyPullList();
                })
            },
            // 编辑回显
            backScan(){
                var obj  = {
                    id:this.row.id,
                    name:this.row.name,
                }
                backScanShopsynonym(obj).then((res)=>{
                    if(res.code == 200){
                        Object.assign(this.dataForm,res.data);

                    }else{

                    }
                })
            },
            // 提交
            dataFormSubmit(formName){
                // alert([this.dataForm.name,this.dataForm.domainAddress]);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        var obj = {
                            "name":  this.dataForm.name,
                            "state":1,
                        }
                        if(this.row) obj.id = this.row.id
                        var fn = this.row?editShopsynonym:shopsynonymSave;
                        fn(obj).then((res) => {
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
                this.$parent.addDataVisible = false;
            },
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .el-form-item__label {
        width: 66px!important;
    }
    /deep/ .el-form-item__content {
        margin-left: 66px!important;
    }
</style>