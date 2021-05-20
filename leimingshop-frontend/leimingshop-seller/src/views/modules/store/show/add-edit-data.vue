<template>
  <div>
       <Bread :breaddata="breaddata" @changePage="changePage" :index="'1'"></Bread>
        <el-form :model="dataForm" label-width="140px" 	:rules="dataRule" class="demo-ruleForm" ref="addForm">
        <el-form-item label="店铺商品分类名称：" prop="gcName">
            <el-input v-model="dataForm.gcName"  maxlength="10" show-word-limit style="width:488px;"></el-input>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'最长不超过10个汉字'"></promptMessage>
        
        <el-form-item label="上级分类：">
            <el-cascader v-model="value" :options="dataArray2" @change="handleChange" clearable change-on-select :disabled="row.type=='addNext'"></el-cascader>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'为空则新增一级分类'"></promptMessage>

        <promptMessage :labelWidth="140" :message="'关联后台分类控制商城展示分类关联的商品'"></promptMessage>

        
        <el-form-item label="排序优先级：" prop="sort">
             <el-input-number v-model="dataForm.sort" :step="1" step-strictly :min="0" :max="255"></el-input-number>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'支持输入0-255内的整数，数值越小排序越靠前'"></promptMessage>

        <el-form-item label="店铺商品分类图片：" prop="gcPic" >
             <div class="pcCoverUrl imgUrl" style="display: flex;width: 900px;flex-direction: column;">
                    <img-cropper
                            v-loading="uploading"
                            ref="cropperImg"
                            :index="'1'"
                            :cropImg="dataForm.gcPic"
                            :imgWidth='"100px"'
                            :imgHeight='"100px"'
                            @GiftUrlHandle="GiftUrlHandle"
                    ></img-cropper>
                <promptMessage style="left: -3px;line-height: 22px;width: 530px;margin-top: 14px;" :labelWidth="140" :message="'商品图片将展示在商城的分类筛选页，为了更好的展示效果，请尽量按照要求上传。<br> \
                1.建议上传高于640x640像素的白底图片，长宽比例为1:1 <br> \
                2.图片支持格式：jpg\\jpeg\\png\\bmp\\PNG\\JPEG\\JPG <br> \
                3.为保障图片加载流畅，请上传小于5M的图片'">
             </promptMessage>
            </div>
            <!-- <div style="color: #f56c6c;font-size: 12px;">必填项不能为空</div> -->
        
        </el-form-item>
       


        <el-form-item>
            <el-button @click="goList()"  type="primary" plain>返回</el-button>
            <el-button type="primary" @click="dataFormSubmit('addForm')" :loading="saveLoading">{{saveLoading?"提交中...":"确认"}}</el-button>
            <!-- <el-button type="primary" plain @click="resetForm('addForm')">重置</el-button> -->
        </el-form-item>
        </el-form>
        

  </div>
</template>

<script>


import {addstoreGoodsclasscustom,updatastoreGoodsclasscustom} from "@/api/api.js";

import { firstClass,storeGoodsClassInfo } from "@/api/api";
import imgCropper from "@/components/upload/model-photo-cropper2";
import Bread from "@/components/bread";
import promptMessage from "@/components/prompt/promptMessage"

export default {
   data() {
    return {
    //   breaddata: [ "商品管理", "展示分类", ""],
      breaddata:[ "商品管理", "店铺商品分类", "新增"],
      uploading:false,
      saveLoading:false,
      value:[],
      value2:[],
      dataArray2:[],
      dataForm:{
        "gcName": "",//分类名称 ,
        "brandId": 0,//品牌ID ,
        "storeId": 0,//店铺ID
        "sort":0,
         "showFlag":1,
         "gcParentId": 0,//父ID ,
        "gcPic":"",
       
        
        // "gcUrl"
    },
      row:'',
      dataRule : {
        gcName : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        gcParentId : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        sort : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        gcPic : [
            { required: true, message: '必填项不能为空', trigger: 'blur'},
        ]
      },
    }
  },
//   props:['dataArray'],
  components:{
    imgCropper,
    Bread,
    promptMessage
  },
  created () {
  },
  methods: {
        init(row){
           
            this.getTree2();
            this.saveLoading = false;
            // this.row = row;
            this.row=row;
           this.$nextTick(()=>{
                if(row){
                    //新增下一级
                    if(row.type && row.type=="addNext"){
                     this.breaddata = [ "商品管理", "店铺商品分类", "新增下级"]
                    this.gcParentId = row.id;   
                    }else if(row.type && row.type=="edit"){
                        // 編輯
                         this.breaddata = [ "商品管理", "店铺商品分类", "编辑"]
                        this.dataForm.id= row.id;
                        this.dataForm.gcName = row.label;
                    }
                    // this.dataForm.gcParentId = row.gcParentId;
                    this.backScan(row);
                }else {
                     this.breaddata = ["商品管理", "店铺商品分类", "新增"]
                }
           })
        },
        changePage(){
            this.goList();
        },
        backScan(row){
            var obj = {
                params:{
                    gcId:row.id,
                }
            }
                storeGoodsClassInfo(obj).then((res)=>{
                if(res.code == 200){
                   
                    // this.value = res.data.idPath.split(',')
                    // // 防止后端反回的字符串中最后一位是“，”
                    // if(this.value.length>0 && this.value[this.value.length-1] ==""){
                    //     this.value.splice(this.value.length-1,1);
                    // }
                    
                    // if(res.data.gcIdPath) this.value2 = res.data.gcIdPath.split(',')
                    // // 防止后端反回的字符串中最后一位是“，”
                    // if(this.value2.length>0 && this.value2[this.value2.length-1] ==""){
                    //     this.value2.splice(this.value2.length-1,1);
                    // }
   
                    // 如果是编辑，要计算他的父级id
                    if(row.type=="edit"){
                        var index = this.value.indexOf(this.row.id);
                        if(index!=-1) this.value.splice(index,1);
                        this.value = [res.data.gcParentId,res.data.gcParentId];
                        this.dataForm.gcParentId =  res.data.gcParentId;
                        this.dataForm.gcPic =  res.data.gcPic;
                        this.dataForm.sort =  res.data.sort;
                    }else if(row.type=="addNext"){
                        this.value = [row.id,row.id];
                    }
                }
            })
        }, 

        getTree2() {
            var params={
                type:0
            }
            firstClass(params).then(res => {
                //Promise后 对数据格式进行处理
                if (res.code == 200) {
                    var data = res.data
                    //处理树形数据
                    // this.treeConfig.rows =  data; 
                    var dataStr = JSON.stringify(data);
                    dataStr = dataStr.replace(/gcName/g,"label")
                    dataStr = dataStr.replace(/children/g,"children")
                    var rows = [].concat(JSON.parse(dataStr));
                    this.dataArray=[]
                    var dataArray = JSON.stringify(rows);
                    var dataArrayStr = dataArray.replace(/id/g,"value");
                    dataArrayStr = dataArrayStr.replace(/\[]/g,'""');
                    this.dataArray2 = JSON.parse(dataArrayStr);
                    console.log(this.dataArray2 );

                    
                }
            });
        },
        handleSelect(item){
          this.dataForm.brandId= item.id
       },
      dataFormSubmit(formName){ 
          this.handleChange();
          this.handleChange2();
          if(this.row.type=="edit" &&  this.dataForm.gcParentId == this.row.id){
              this.$message({
                  message: "上级分类不能选自己",
                  type: 'warning',
                  duration: 1000
              })
             return
          }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.saveLoading = true;
                    let fn = this.row.type=="edit"?updatastoreGoodsclasscustom:addstoreGoodsclasscustom
                   fn(this.dataForm).then((res)=>{
                        this.saveLoading = false;
                       let status = "warning"
                        if(res.code=="200"){
                            status = "success"
                            this.goList();
                        }else{
                            status = "error"
                        }
                        this.$message({
                            message: res.msg,
                            type: status,
                            duration: 1500
                            })
                        return
                   })
                } else {
                    return false;
                }
            })
        },
        resetForm(){

        },
        goList(){
            this.$emit("showList");
        },
        handleChange(obj) {
            var value = this.value;
            if(value.length>=3){
                this.value = [obj[0],obj[1]];
            }
            if(value.length >0){
                this.dataForm.gcParentId =value[value.length-1]
            }else{
                this.dataForm.gcParentId = 0;
            }
        },
        handleChange2() {
            var value = this.value2;
        },
        GiftUrlHandle(imgUrl){
          let that = this;
          var url = imgUrl;
          this.dataForm.gcPic = url
       
        },
  }
}
</script>
<style  lang="scss" scoped>
.el-form {
    width: 545px;
}
/deep/ .el-form-item__error{
    min-width: 200px;
    line-height: inherit;
    left: 100%;
    top:0;
}
</style>

