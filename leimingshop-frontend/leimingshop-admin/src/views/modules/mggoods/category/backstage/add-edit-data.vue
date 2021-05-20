<template>
  <div>
        <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
        <!-- :style="{height:formHeight+ 'px',overflow:'scroll'}"  -->
        <el-form  :model="dataForm" label-width="140px" 	:rules="dataRule" class="demo-ruleForm" ref="addForm">
        <el-form-item label="后台分类名称：" prop="gcName">
            <el-input v-model="dataForm.gcName"  maxlength="10" show-word-limit style="width:400px;"></el-input>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'最长不超过10个汉字'"></promptMessage>

        <el-form-item label="上级分类：">
            <!-- <el-popover placement="right" width="400" trigger="click"> -->
            <el-cascader v-model="value" :options="dataArray" @change="handleChange" clearable change-on-select :disabled="row.type=='addNext'"></el-cascader>
            <!-- <el-button slot="reference">上级</el-button> -->
            <!-- </el-popover> -->
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'为空则新增一级分类'"></promptMessage>


        <el-form-item label="排序：" prop="gcSort">
             <el-input-number v-model="dataForm.gcSort" :step="1" step-strictly  :min="0" :max="255"></el-input-number>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'支持输入0-255内的整数，数值越小排序越靠前'"></promptMessage>
        <el-form-item  v-if="(row&&row.level==2&&row.type=='addNext')||(row&&row.level==3 && row.type=='edit')" label="分类类型：" prop="classType">
             <!-- <el-input-number v-model="dataForm.classType" :step="1" step-strictly  :min="0" :max="255"></el-input-number> -->
             <!-- 分类类型（0:实体商品分类，1:虚拟商品分类） -->
             <!-- <template >
                <el-radio v-model="dataForm.classType" :label="0">实体商品分类</el-radio>
                <el-radio v-model="dataForm.classType" :label="1">虚拟商品分类</el-radio>
            </template> -->
            <!-- {{dataForm.classType}} -->
            <el-radio-group v-model="dataForm.classType">
                <el-radio :label=0>实体商品分类</el-radio>
                <el-radio :label=1>虚拟商品分类</el-radio>
            </el-radio-group>
        </el-form-item>
        <promptMessage :labelWidth="140" :message="'选择分类类型后，商户发布商品时只能发布对应类型的商品'"></promptMessage>

        <div>

            <el-form-item label="关联品牌：" prop="brandName">
                 <el-button type="primary" @click="brand()" size="mini">查看商品品牌</el-button><br>
                 <!-- <el-table   :data="dataForm.goodsClassBrandSaveDTOList" border style="width:720px" >
                    <el-table-column type="index" prop="$index" label="序号" width="70"></el-table-column>
                    <el-table-column property="brandId" align="center" label="品牌id" width="200"></el-table-column>
                    <el-table-column property="brandName" align="center" label="品牌名称"></el-table-column>
                    <el-table-column align="center" label="操作">
                        <template slot-scope="scope">
                        <el-button @click="deleteFn(scope.row,scope.$index,1)" type="text" size="mini">删除</el-button>
                       </template>
                    </el-table-column>
                </el-table> -->

               <div style="width:800px;" v-if="flushData" >
                    <el-tag
                        v-for="(tag,index) in dataForm.goodsClassBrandSaveDTOList"
                        style="margin-right: 10px;"
                        :key="index"
                        closable
                        @close="deleteFn(tag,index,1)"
                        type="success">
                        {{tag.brandName}}
                    </el-tag>
               </div>
            </el-form-item>
            <promptMessage :labelWidth="140" :message="'发布该后台分类的商品时，可选择已关联品牌'"></promptMessage>

            <el-form-item label="关联规格：" prop="desc">
                <el-button type="primary" @click="spec()" size="mini">查看商品规格</el-button>
                 <el-table   :data="dataForm.goodsClassSpecDTO" border style="width:800px" >
                    <el-table-column type="index" prop="$index" label="序号" width="70"></el-table-column>
                    <el-table-column property="specName" align="center" label="规格名称" width="200"></el-table-column>
                    <el-table-column property="specValue" align="center" label="规格值"></el-table-column>
                    <!-- <el-table-column property="specGroupValue" align="center" label="规格分组"></el-table-column> -->
                    <el-table-column align="center" label="操作">
                        <template slot-scope="scope">
                         <el-button @click="deleteFn(scope.row,scope.$index,2)" type="text" size="mini">删除</el-button>
                       </template>
                    </el-table-column>
                </el-table>
            </el-form-item>
            <promptMessage :labelWidth="140" :message="'发布该后台分类的商品时，可选择已关联规格'"></promptMessage>

            <el-form-item label="关联属性：" prop="desc">
                 <el-button type="primary" @click="attribute()" size="mini">查看商品属性</el-button>

                <el-table  :data="dataForm.goodsClassAttrDTO" border style="width:800px" >
                    <el-table-column type="index" prop="$index" label="序号" width="70"></el-table-column>
                    <el-table-column property="attrName" align="center" label="属性名称" width="200"></el-table-column>
                    <el-table-column property="attrValue" align="center" label="属性值"></el-table-column>
                    <!-- <el-table-column property="attrGroupValue" align="center" label="属性分组"></el-table-column> -->
                    <el-table-column align="center" label="操作">
                        <template slot-scope="scope">
                         <el-button @click="deleteFn(scope.row,scope.$index,3)" type="text" size="mini">删除</el-button>
                       </template>
                    </el-table-column>
                </el-table>
            </el-form-item>
             <promptMessage :labelWidth="140" :message="'发布该后台分类的商品时，可选择已关联规格'"></promptMessage>
        </div>

        <el-form-item>
            <el-button @click="goList()">返回</el-button>
            <el-button type="primary" @click="dataFormSubmit('addForm')" :loading="saveLoading">{{saveLoading?"提交中...":"确认"}}</el-button>
            <!-- <el-button @click="resetForm('addForm')">重置</el-button> -->
        </el-form-item>
        </el-form>

        <modelBrand  ref="brandCompon"  v-if="modelBrandVisible"></modelBrand>
        <modelSpec  ref="specCompon"  v-if="modelSpecVisible"></modelSpec>
        <modelAttribute  ref="attributeCompon" v-if="modelAttributeVisible"></modelAttribute>

  </div>
</template>

<script>

import modelAttribute from "./model-attribute";
import modelSpec from "./model-spec";
import modelBrand from "./model-brand";
import promptMessage from "@/components/prompt/promptMessage"

import {brandList} from "@/api/api.js";
import {addGoodsclass,updataGoodsclass} from "@/api/api.js";

// 根据分类id查询品牌，规格，
import { goodsclassById } from "@/api/api.js";
import Bread from "@/components/bread";
import { setTimeout } from 'timers';
export default {
   data() {
    return {
      breaddata: ["商品", "类目管理", "后台分类", ""],
      modelBrandVisible:false,
      modelSpecVisible:false,
      modelAttributeVisible:false,
      saveLoading:false,
      value:[],
      dataForm:{
        "brandName":'',//XX
        "gcName": "",//分类名称 ,
        "brandId": 0,//品牌ID ,
        "classType":0,//分类类型（0:实体商品分类，1:虚拟商品分类）
        // "gcIdpath": "",//层级path ,
        "gcParentId": 0,//父ID ,
        "gcSort": 0,// 排序 ,
        // "brandIds":[],
        "goodsClassBrandSaveDTOList":[
            // {
            //     "brandId": 0,
            //     "brandName": "string",
            //     // "gcClassId": 0
            // }
        ],
        goodsClassAttrDTO:[],
        goodsClassSpecDTO:[],
        "attrIds":[],//属性关联数组 ,
        "specIds":[],//规格关联数组 ,
        "storeId": 0//店铺ID
        // "id":''
    },
      flushData:true,
      row:'',
      dataRule : {
        gcName : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        gcParentId : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        gcSort : [
            { required: true, message: '必填项不能为空', trigger: 'blur' },
        ],
        // brandName : [
        //     { required: true, message: '必填项不能为空', trigger: 'blur' },
        // ],
      },
    //   dataArray:[{
    //       value: 'zhinan',
    //       label: '指南',
    //       children: [
    //           {
    //         value: 'shejiyuanze',
    //         label: '设计原则',
    //         children: [{
    //           value: 'yizhi',
    //           label: '一致'
    //         }, {
    //           value: 'fankui',
    //           label: '反馈'
    //         }, {
    //           value: 'xiaolv',
    //           label: '效率'
    //         }, {
    //           value: 'kekong',
    //           label: '可控'
    //         }]
    //        }
    //        ]
    //     }],
        formHeight:document.body.offsetHeight-210,
    }
  },
  props:['dataArray'],
  components:{
    modelAttribute,
    modelSpec,
    modelBrand,
    Bread,
    promptMessage
  },
  created () {
  },
  methods: {
        init(row){
            this.saveLoading = false;
            this.row = row;
             this.$nextTick(()=>{
                if(row){
                    if(row.type && row.type=="addNext"){
                        this.breaddata = ["商品", "类目管理", "后台分类", "新增下级"]
                        this.gcParentId = row.id;
                    }else if(row.type && row.type=="edit"){
                        // 編輯
                        this.breaddata = ["商品", "类目管理", "后台分类", "编辑"]
                        this.dataForm.id= row.id;
                        this.dataForm.gcName = row.label;
                    }
                    // this.dataForm.gcParentId = row.gcParentId;
                    this.backScan(row);
                }else {
                    this.breaddata = ["商品", "类目管理", "后台分类", "新增"]
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
                    gcName:row.label,
                }
            }
            goodsclassById(obj).then((res)=>{
                if(res.code == 200){
                    this.value = res.data.gcIdpath.split(',')
                    // 防止后端反回的字符串中最后一位是“，”
                    if(this.value.length>0 && this.value[this.value.length-1] ==""){
                        this.value.splice(this.value.length-1,1);
                    }
                    // 如果是编辑，要计算他的父级id
                    if(row.type=="edit"){
                        this.dataForm = res.data
                        var index = this.value.indexOf(this.row.id);
                        if(index!=-1) this.value.splice(index,1);
                        this.dataForm.gcParentId =  res.data.gcParentId;
                        this.dataForm.goodsClassBrandSaveDTOList = res.data.goodsClassBrandUpdateDTO
                        this.dataForm.attrIds = [];
                        this.dataForm.specIds = []
                        this.dataForm.goodsClassAttrDTO =  res.data.goodsClassAttrDTO;
                        res.data.goodsClassAttrDTO.forEach((item,index)=>{
                            this.dataForm.attrIds.push(item.attrId);
                        })
                        this.dataForm.goodsClassSpecDTO =  res.data.goodsClassSpecDTO;
                        res.data.goodsClassSpecDTO.forEach((item,index)=>{
                            this.dataForm.specIds.push(item.specId);
                        })
                    }

                    this.dataForm.gcSort =  res.data.gcSort;
                    // this.dataForm.brandId =  res.data.brandId;
                    // this.dataForm.brandName =  res.data.brandName;
                }
            })
        },
        handleSelect(item){
          this.dataForm.brandName= item.brandName
          this.dataForm.brandId= item.id
       },
        brand(){
            this.modelBrandVisible = true;
            this.$nextTick(()=>{
                this.$refs.brandCompon.init();
            })
        },
        spec(){
            this.modelSpecVisible = true;
            this.$nextTick(()=>{
                this.$refs.specCompon.init();
            })
        },
        attribute(){
            this.modelAttributeVisible = true;
            this.$nextTick(()=>{
                this.$refs.attributeCompon.init();
            })
        },
      dataFormSubmit(formName){
           this.handleChange();
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

                    let fn = this.row.type=="edit"?updataGoodsclass:addGoodsclass
                    this.dataForm.goodsClassBrandSaveDTO = this.dataForm.goodsClassBrandSaveDTOList
                   fn(this.dataForm).then((res)=>{
                        this.saveLoading = false;
                       let status = "warning"
                        if(res.code=="200"){
                            status = "success"
                            this.goList();
                        }else{
                            status = "errror"
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
        deleteFn(row,index,type){
            let that = this
            // this.$confirm('是否确认删除?', '提示', {
            //     confirmButtonText: '确定',
            //     cancelButtonText: '取消',
            //     type: 'warning'
            // }).then(() => {
               if(type==1){that.deleteBrandFn(row,index)}
                else if(type==2){that.deleteSpecFn(row,index)}
                else if(type==3){that.deleteAttrFn(row,index)}
            // }).catch(() => {});
        },
        deleteBrandFn(row,index){
            this.flushData=false
            this.dataForm.goodsClassBrandSaveDTOList.splice(index,1);
            this.flushData=true
        },
        deleteSpecFn(row,index){
            this.dataForm.goodsClassSpecDTO.splice(index,1);
            this.dataForm.specIds.splice(index,1);
        },
        deleteAttrFn(row,index){
            this.dataForm.goodsClassAttrDTO.splice(index,1);
            this.dataForm.attrIds.splice(index,1);
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
  }
}
</script>
<style lang="scss" scoped>

.el-form-item{
    margin-bottom: 45px;
}
.el-form {
    // width: 545px;
}
/deep/ .el-form-item__error{
    min-width: 200px;
    line-height: inherit;
    left: 100%;
    top:0;
}
</style>

