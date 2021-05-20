<template>
  <el-dialog
    :visible.sync="visible"
    :title='title'
    :before-close='close'
 >

    <el-form ref="add" :model="dataFrom" label-width="80px" :rules="dataRule">
            <el-form-item label="名称" prop='name'>
                <el-input v-model="dataFrom.name"></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop ='age'>
                <el-input v-model="dataFrom.age"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop='phone'>
                <el-input v-model="dataFrom.phone"></el-input>
            </el-form-item>
        <el-item>
            <el-button  @click="submit">提交</el-button>
            <el-button  @click="close">返回</el-button>
        </el-item>
    </el-form>
    
    <!-- 许志成哈哈哈<br> -->

    <!-- {{data}} -->
<!-- {{row}} -->
  </el-dialog>
</template>


<script>
import { isMobile } from '@/utils/validate'
export default {
    data(){
        var vilidPhone =(rule, value, callback) => {
            if(!isMobile(this.dataFrom.phone)){
                alert(this.dataFrom.phone)
                return callback(new Error("手机号不符合规则"));
            }
            collback();
        }
        return {
            title:'',
            data:12331,
            visible:false,
            row :{},
            dataFrom:{
                name:'',
                age:'',
                phone:'',
            },
            dataRule :{
                name: [
                    { required: true, message: "名字不能为空", trigger: "blur" }
                ],
                age: [
                    { required: true, message: "年龄不能小于18", trigger: "blur" }
                ],
                phone: [
                    { required: true, message: "商品名字不能为空", trigger: "blur" },
                    { validator : vilidPhone , trigger: "blur"}
                ],
            }
        }
    },
   
    methods:{
        
        init(row){
            this.row=row;
            if(this.row){
                this.title="编辑";
                this.dataFrom.name = this.row.name;
            }else{
                this.title="新增";
                
            };
        },
        submit(){
           this.$refs["add"].validate(valid=>{
                if(valid){
                    this.$message.success("验证通过");
                }else{
                    this.$message.console.error("验证失败");
                }
           });
        },
        close(){
            this.visible=false;
            this.$parent.addOrEditVisible=false;
        }
    },
  
    commponents:{

    },
    created() {
        this.visible=true;
    }
}
    // mounted(){

    // }
</script>
