<template>
    <div class="blockWrap concatWay">
        <h4 v-text="title"></h4>

        <Form
            ref="formValidate"
            :model="formValidate"
            :rules="ruleValidate"
            :label-width="100"
        >

            <FormItem label="联系人：" prop="contacts">
                <Input v-model="formValidate.contacts" type="text" placeholder="请输入联系人" maxlength="20" />
            </FormItem>

            <FormItem label="联系电话：" prop="contactsPhone">
                <Input v-model="formValidate.contactsPhone" type="number" :rows="4" placeholder="请输入联系电话" />
            </FormItem>
        </Form>
    </div>
</template>

<script>
    import { isMobile } from '@/utils/validate'
    export default{
        name: 'concatWay',
        data(){
            return{
                title: '联系方式',
                formValidate: {
                    contacts: undefined,
                    contactsPhone: undefined
                },
                ruleValidate: {
                    contacts: [
                        { required: true, message: '请输入联系人', trigger: 'blur' }
                    ],
                    contactsPhone: [
                        { required: true, message: '请输入联系电话', trigger: 'blur' },
                        { validator: (rule, value, callback) => {
                            if ( !isMobile(value) ) callback( new Error('手机号格式错误') )
                            else callback();
                        }, trigger: 'blur'}
                    ]
                }
            }
        },

        props: {
            orderInfo: {
                type: Object,
                default: () => null
            }
        },



        components:{

        },
        created(){

        },
        computed:{
            'formValidate.contacts':{
                get(){

                },
                set(){

                }
            }
        },
        watch:{
            orderInfo: {
                immediate: false,
                handler(newVal, oldVal){
                    let temp = {
                        contacts: newVal.contacts,
                        contactsPhone: newVal.contactsPhone
                    }

                    this.formValidate = temp
                }
            }
        },
        mounted(){

        },
        methods:{
            async handleConcat(){
                const concat = await this.$refs["formValidate"].validate();
                if(!concat){
                    setTimeout(()=>{
                        var isError = document.getElementsByClassName("ivu-form-item-error");
                    if (isError[0].querySelector("input")){
                        isError[0].querySelector("input").focus();
                    }
                    },1);
                    this.$Message.warning("请完善申请内容");
                    return false;   
                }else{
                    return true;
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .concatWay{
        /deep/ .ivu-form-item-label{
            height: 42px;
            line-height: 24px;
        }

        /deep/ input{
            width: 248px;
            height: 42px;
        }
    }
    /deep/ .ivu-input{
        box-shadow: none !important;
        border-color: none !important;
        &:hover{
            box-shadow: none !important;
            border-color: #dd2619 !important;
        }
       
    }
</style>