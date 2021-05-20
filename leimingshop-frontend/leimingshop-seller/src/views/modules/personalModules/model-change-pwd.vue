<template>
		<el-dialog
			:visible.sync="visible"
			title='修改密码'
			:append-to-body="true"
			:before-close='close'
			:close-on-click-modal="false"
  			:close-on-press-escape="false"
            width="30%"
		>
                 <el-form
                    :model="dataForm"
                    :rules="dataRule"
                    ref="dataForm"
                    label-width="130px">
                    <el-form-item label="原密码：" prop="password">
                        <el-input v-model="dataForm.password"  show-password   placeholder="请输入"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码：" prop="newPassword">
                        <el-input v-model="dataForm.newPassword"  show-password  placeholder="请输入"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码：" prop="confirmPassword">
                        <el-input v-model="dataForm.confirmPassword"  show-password  placeholder="请输入"></el-input>
                    </el-form-item>
                </el-form>
                <div class="bottomBts">
                    <el-button @click="close">取消</el-button>
                    <el-button type="primary" @click="dataFormSubmit">确认</el-button>
                </div>
		</el-dialog>
</template>
<script>
import { getUUID } from '@/utils'
import { editPwd } from "@/api/api.js"
import JsEncrypt from 'jsencrypt'
import security from '@/utils/security.js'

export default {
    data(){
        return {
			visible:false,
            dataForm:{
                password:'',
                newPassword:'',
                confirmPassword:'',
            },

        }
	},
	props:{

    },
    computed: {
        dataRule () {
            var sameNewPwdFn = (rule, value, callback) => {
                if (this.dataForm.confirmPassword!=this.dataForm.newPassword) {
                    return callback(new Error("新密码和确认密码不一致"))
                }
                callback()
            }
            return {
                password: [
                    { required: true, message: "必填项不能为空", trigger: 'blur' }
                ],
                newPassword: [
                    { required: true, message: "必填项不能为空", trigger: 'blur' },
                ],
                confirmPassword: [
                    { required: true, message: "必填项不能为空", trigger: 'blur' },
                    { validator: sameNewPwdFn, trigger: 'blur' }
                ]
            }
        }
    },
	components:{
    },
    created() {
	},
	mounted(){
    },
    methods:{
        init(obj){
			// console.log("弹框出来后会走这里");
			this.visible = true;

        },
        // 确认修改
		dataFormSubmit(){
             this.$refs['dataForm'].validate((valid) => {
                if (!valid) {
                    return false
                }
                // 增加用户名与密码 RSA加密
                let publicKey = security.publicKey;
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey(publicKey);
                var obj = {
                    "confirmPassword": encrypt.encrypt(this.dataForm.confirmPassword),
                    "newPassword":encrypt.encrypt(this.dataForm.newPassword),
                    "password": encrypt.encrypt(this.dataForm.password),
                   /* "preConfirmPassword":this.dataForm.confirmPassword,
                    "preNewPassword":this.dataForm.newPassword,
                    "prePassword":this.dataForm.password,*/
                }
                editPwd(obj).then((res)=>{
                    if(res.code==200){
                        this.$message.success(res.msg);
                        this.close();
                    }else{
                        this.$message.error(res.msg);
                    }
                })
             })
        },
		// 关闭前走的函数
        close(){
			this.visible=false;
			this.$parent.changePwdVisible = false;
        }
    },
}
</script>
<style lang="scss" scoped>

.bottomBts{
    text-align: center;
    margin-top:40px;
}
</style>
