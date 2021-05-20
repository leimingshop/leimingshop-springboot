<template>
  <div class="adverList">
    <Bread :breaddata="advId?breaddata:breaddatas" @changePage="changePage" :index="'1'"></Bread>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="130px">
        <el-form-item prop="relationType" label="导航类型：">
            <el-select v-model="dataForm.relationType" @change="typeChange()" placeholder="请选择">
                <el-option
                    :key="1"
                    label="自定义链接"
                    :value='1'>
                </el-option>
                  <el-option
                    :key='2'
                    label="商品分类"
                    :value='2'>
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="relationParams" label="链接地址："  v-show="dataForm.relationType==1" :rules="[
        {required: this.dataForm.relationType===1 && true,message:'链接不能不能为空',trigger:'blur'},]">
            <el-input v-model="dataForm.relationParams" placeholder="请输入链接" show-word-limit clearable ></el-input>
            <div class="operationTips">仅支持输入Http://开头的链接格式</div>
        </el-form-item>

        <el-form-item prop="showclassIds" label="所属分类：" v-show="dataForm.relationType!=1" :rules="[{
            required: this.dataForm.relationType!==1 && true,message:'分类不能为空',trigger:'blur'},]">
            <el-cascader
                :options="classDataList"
                change-on-select
                :clearable="true"
                :props="props"
                v-model="dataForm.showclassIds"
                placeholder="请选择"
                >
            </el-cascader>
        </el-form-item>
        <el-form-item prop="title" label="导航标题：" class="tital">
            <el-input v-model="dataForm.title" placeholder="" maxlength="20" show-word-limit clearable></el-input>
        </el-form-item>
        <el-form-item prop="sort" label="排序：">
            <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" maxlength="3" onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"></el-input-number>
            <div class="operationTips">支持输入0-255内的整数，数值越小排序越靠前</div>
        </el-form-item>
        <el-form-item prop="isOpen" label="新窗口打开：">
            <el-radio-group v-model="dataForm.isOpen">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
            </el-radio-group>
        </el-form-item>
        <div class="btnSub">
            <el-button @click="changePage">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitStore()" :loading="buttonStatus">{{ $t('confirm') }}</el-button>
        </div>
    </el-form>

  </div>
</template>

<script>
import cloneDeep from 'lodash/cloneDeep'
import { addNavigation,updateNavigation,storeGoodsclass } from '@/api/api'
import { navigationDetail,plainAdveSlect } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";


export default {
    data () {
        return {
            props:{
                label:'gcName',
                value: 'id'
            },
            classDataList:[],
     
            dataForm: {
              relationType:1,
              isOpen:1,
              showclassIds:[]
            },
            breaddata: ["运营管理", "pc首页导航设置",'编辑'],
            breaddatas: ["运营管理",  "pc首页导航设置",'新增'],
            buttonStatus:false,
            subStatus:true,
            uploading:false,
            advId:null,
        }
    },
    computed: {
        dataRule () {
            return {
                relationType: [
                    { required: true, message: '导航类型不能为空', trigger: 'blur' }
                ],
                sort: [
                    { required: true, message: '排序不能为空', trigger: 'blur' }
                ],
                title: [
                    { required: true, message: '导航标题不能为空', trigger: 'blur' }
                ]
                // relationParams: [
                //     { required: this.dataForm.relationType===1 && true, message: '链接不能不能为空', trigger: 'blur' }
                // ],
                // showclassIds: [
                //     { required: this.dataForm.relationType!==1 && true, message: '分类不能为空', trigger: 'change' }
                // ],
            }
        }
    },
 
    components:{
        Bread
    },
    created(){
        //展示分类
         var obj={
                params:{
                   'type':'goodsDeatil'
                }
             }
        storeGoodsclass(obj).then((res)=>{
            console.log('展示分类',res)
            this.classDataList = res.data;
        })
    },
    methods: {
        init(id){
            this.$nextTick(()=>{
                this.advId = id;
                if(id){
                    var obj  = {
                        id:id
                    }
                    navigationDetail(obj).then((res)=>{
                        if(res.code == 200){
                            this.dataForm=res.data
                            if(res.data.classIds){
                                this.dataForm.showclassIds = res.data.classIds.split(",");
                            }
                           
                        }
                    })
                }
            })
        },

        // 提交
        submitStore(){
            if(this.dataForm.sort>255){
                this.$message({
                    message:'排序最大值为255',
                    type: 'error',
                    duration: 1500,
                })
                return
            }
            if(this.dataForm.relationType==2){
                this.dataForm.relationParams = this.dataForm.showclassIds[this.dataForm.showclassIds.length-1];
            }else{
                this.dataForm.showclassIds=[];
            }
            if(this.dataForm.showclassIds){
                this.dataForm.classIds = this.dataForm.showclassIds.join(',');
            }
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.buttonStatus = true;
                    if(this.advId){//编辑
                        updateNavigation(this.dataForm).then((res)=>{
                            if(res.code==200){
                                this.changePage();
                                this.$message({
                                message:res.msg,
                                    type: 'success',
                                    duration: 1500,
                                })
                            }else{
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                                // this.changePage();
                            }
                            this.buttonStatus = false;
                        })
                    }else{
                        addNavigation(this.dataForm).then((res)=>{
                            if(res.code==200){
                                this.changePage();
                                this.$message({
                                message:res.msg,
                                    type: 'success',
                                    duration: 1500,
                                })
                            }else{
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                                // this.changePage();
                            }
                            this.buttonStatus = false;
                        })
                    }
                }
            })
        },
        //返回上级
        changePage(){
            this.$emit("addoraditList");
        },
        handleChange(val){
            // console.log(val)
            this.dataForm.relationParams = val[val.length-1];
        },
        typeChange(){
           if(this.dataForm.relationParams&&!isURL(this.dataForm.relationParams)){
                this.dataForm.relationParams=''
            }
        }
    }
};
</script>
<style lang="scss" scoped>
    .adverList{
        .el-input{
            width: 240px !important;
        }
        .el-form{
            padding-top: 50px;
            width: 1100px;
            margin: 0 auto;
        }
        .btnSub{
            margin-top: 30px;
            padding: 0 180px;
        }
       .el-form-item__content{
            display: inherit !important;
        }
        .operationTips{
            color: #999999;
            font-size: 12px;
        }
    }
  .tital{
    /deep/ .el-input__inner {
        padding-right: 67px;
    }
  }
</style>
