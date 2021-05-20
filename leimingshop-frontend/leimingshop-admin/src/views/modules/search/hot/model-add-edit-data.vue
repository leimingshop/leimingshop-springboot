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
            <el-form-item label="搜索词：" prop="hotWord"
             :rules="[
                { required: true, message: '请输入搜索词'}
            ]"
            >
                <el-input v-model.trim="dataForm.hotWord" placeholder="请输入30字以内的内容"></el-input>
            </el-form-item>
            <el-form-item label="排序：">
                <el-input v-model.trim="dataForm.sort" placeholder="0"></el-input>
                <span style="color: #999999;">数字越大越靠前</span>
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
    import { editShophotkeyword,shophotkeywordSave, backScanShophotkeyword } from '@/api/api'
    export default {
        name: "model-add-edit-data",
        data () {
            return {
                visible : false,
                loading : false,
                dataForm: {
                    hotWord: "",
                    sort: "",
                },
                title:'',
                row:"",
                formLabelWidth: '120px',
                rules: {
		          hotWord: [
		            { required: true, message: '请输入搜索词', trigger: 'blur' },
		          ],
		        },
            }
        },
        watch: {
            'dataForm.hotWord': function (newV, oldV) {
                var chineseCount = 0, characterCount = 0;
                for (let i = 0; i < newV.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
                        chineseCount = chineseCount + 2;
                    } else { //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 60) { //输入字符大于60的时候过滤
                        this.dataForm.hotWord = newV.substr(0, (chineseCount / 2 + characterCount) - 1)
                    }
                }
            },
            'dataForm.sort': function (newV,oldV) {
                newV=~~newV;
                if(newV.toString().length>6){
                    this.dataForm.sort = newV.toString().substr(0,6)
                }else{
                    this.dataForm.sort = newV
                }
            }
        },
        methods: {
            init (row) {
                this.visible = true;
                this.row = row;
                if(row){
                    this.title="编辑搜索词";
                    this.backScan();
                }else{
                    this.title="新建搜索词"

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
                    keyword:this.row.keyword,
                    sort:this.row.sort,
                }
                backScanShophotkeyword(obj).then((res)=>{
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
                            "hotWord":  this.dataForm.hotWord,
                            "sort":  this.dataForm.sort,
                        }
                        if(this.row) obj.id = this.row.id
                        var fn = this.row?editShophotkeyword:shophotkeywordSave;
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

<style scoped>

</style>