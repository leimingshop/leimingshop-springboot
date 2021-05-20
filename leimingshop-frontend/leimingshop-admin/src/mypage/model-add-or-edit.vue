<template>
    <el-dialog
  		:visible.sync="visible"
  		:title="title"
        :before-close="close"
    >
            <el-form ref="addOrEdit" :model="dataForm" label-width="80px" :rules="dataRule">
                <el-form-item label="商品名字：" prop="goodsName">
                    <el-input v-model="dataFrom.goodsName"></el-input>
                </el-form-item>
                <el-form-item label="规格" prop="spec">
                    <el-input v-model="dataFrom.spec"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="dataFrom.phone" maxlength="11" show-word-limit></el-input>
                </el-form-item>
           
            <el-form-item>
                <el-button @click="submit">提交</el-button>
                 <el-button @click="close ">返回</el-button>
            </el-form-item>
             </el-form>
    </el-dialog>

</template>
<script>
import { isMobile } from '@/utils/validate'
export default {
    data(){//放数据
        var validPhone=(rule,value,callback)=>{
            if(!isMobile(this.dataFrom.phone)){
                return callback(new Error("手机号不符合规格"));
            }
            callback();
        }
        return{
            visible:false,
            title:'',
            data:123,
            row:{},
            dataFrom:{
                goodsName:'',
                spec:'',
                phone:'',
            },
            dataRule:{
               goodsName: [
                    { required: true, message: "商品名字不能为空", trigger: "blur" }
                ], 
                spec:[
                    {required:true,message:"规格不能为空", trigger:"blur"}
                ] ,
                 phone: [
                    { required: true, message: "手机号不能为空", trigger: "blur" },
                    {validator:validPhone, trigger:"blur"}
                ],
            }
        }
    },

    components:{//组件的注册

    },
    created(){
        this.visible=true;
    },
    mounted(){

    },
    methods:{
        init(row){
            this.row=row;
            if(this.row){
                this.title="编辑"
                this .dataFrom.goodsName=this.row.goodsName;
            }else{
                this.title="新增"
            }
        },
        //提交
        submit(){
            this.$refs["addOrEdit"].validate(valid=>{
                if(valid){
                    this.$message.success("验证通过")
                }else{
                    this.$message.error("验证不通过")
                }
            })
        },
        close(){
            this.visible=false;
            this.$parent.addOrEditVisible=false;
        }
    }
}
</script>