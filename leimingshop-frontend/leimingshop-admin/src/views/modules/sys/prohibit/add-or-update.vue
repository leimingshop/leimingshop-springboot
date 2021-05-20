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
                :rules="dataRule"
                ref="addForm"
                @keyup.enter.native="dataFormSubmit('dataForm')"
                label-width="120px"
        >
            <el-form-item label="禁用词名称：" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="dataForm && dataForm.name" auto-complete="off" placeholder="请输入10字以内的内容" maxlength="10"></el-input>
            </el-form-item>
<!--            <el-form-item style="text-align: center;margin-left: -120px!important;">-->
<!--                <el-button  @click="dataFormCancel()">取消</el-button>-->
<!--                <el-button type="primary" @click="dataFormSubmit('dataForm')"-->
<!--                           :loading="loading">{{loading ? "提交中···" : "确定"}}</el-button>-->
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
	import { addadvertisingban, updateadvertisingban } from '@/api/api'
    export default {
        data () {
            return {
                visible : false,
                loading : false,
                dataForm: {
                	name:'',
                },
                optionsApplication: [],
                optionsRight: [],
                title:'',
                adddata: true,
                formLabelWidth: '120px',
                dataRule:{
		          name: [
		            { required: true, message: '必填项不能为空', trigger: 'blur' }
		          ],
                },
            }
        },
        components:{
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
                    if (count > 20) { //输入字符大于20的时候过滤
                        this.dataForm.name = newV.substr(0, (chineseCount / 2 + characterCount) - 1)
                    }
                }
            }
        },

        computed:{
        	
        },
        mounted(){
        },
        methods: {
            init (row) {
            	console.log(row);
                this.visible = true;
                this.loading = false;
                if(row){
                    this.title="编辑禁用词语";
                    this.dataForm = row;
                    this.adddata = false;
                }else{
                    this.title="添加禁用词语"
                    this.dataForm.name = "";
                    this.adddata = true;
                }
            },
            // 提交
            dataFormSubmit(formName){
                // alert([this.dataForm.name,this.dataForm.domainAddress]);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        var fn = addadvertisingban;
                        if(!this.adddata){
                        	fn = updateadvertisingban;
                        }
                        fn(this.dataForm).then((res) => {
                            this.loading = false;
                            // alert(JSON.stringify(res));
                            let status = null;
                            if(res.code == "200"){
                                status = "success";
                                this.visible = false;
                                this.$emit('searchDataList');
                                this.closeDialog();
                                 this.$parent.getDataList();

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
            	this.visible = false;
                this.$parent.addOrUpdateVisible = false;
            },
        }
    }
</script>

<style scoped>
    /*/deep/.el-form-item__content:nth-child(1) {*/
    /*    margin-left: 50px!important;*/
    /*}*/
    .title {
        margin-left: -70px;
    }
</style>