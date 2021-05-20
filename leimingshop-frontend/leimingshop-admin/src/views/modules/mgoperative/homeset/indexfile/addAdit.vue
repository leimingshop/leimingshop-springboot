<template>
  <div class="homeIndex">
    <Bread :breaddata="menuId?breaddata:breaddatas" @changePage="changePage" :index="'2'"></Bread>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="102px">
        <el-form-item prop="menuName" label="菜单名称：">
            <el-input v-model="dataForm.menuName" maxlength="4" show-word-limit placeholder="请输入" clearable></el-input>
        </el-form-item>
        <el-form-item prop="linkType" label="跳转类型：">
            <el-select v-model="dataForm.linkType " @change="linkChange()" clearable  placeholder="请选择">
                <el-option
                    v-for="item in linkTypeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
            <div class="operationTips">可对点击跳转类型做设置，如选择外部链接，下方链接处为必填</div>
        </el-form-item>
        <el-form-item prop="url" label="链接：" v-if="dataForm.linkType =='url'">
            <el-input v-model="dataForm.url" maxlength="256" placeholder="http://xxxxxx.com" clearable></el-input>
            <div class="operationTips">仅支持输入http://开头的链接格式</div>
        </el-form-item>
        <el-form-item prop="classId" label="所属分类：" v-if="dataForm.linkType == 'searchGoodsByClass'">
                <el-cascader
                    :options="classDataList"
                    change-on-select
                    :clearable="true"
                    :props="props"
                    v-model="classIds"
                    @change="handleChangeOut">
                </el-cascader>
        </el-form-item>
        <el-form-item prop="topic" label="专题页：" v-if="dataForm.linkType == 'topic'">
            <el-select v-model="dataForm.url" @change="topicChange()"  placeholder="请选择">
                    <el-option
                        v-for="item in topicList"
                        :key="item.id"
                        :label="item.topicName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        <el-form-item prop="menuIcon" label="菜单图标：">
           <div v-loading="uploading" style="width: 100px">
               <img-cropper
                   ref="menuIcon"
                   :index="'1'"
                   :imgWidth='"100px"'
                   :imgHeight='"100px"'
                   :cropImg='this.dataForm.menuIcon'
                   @GiftUrlHandle="GiftUrlHandle">
             </img-cropper>
           </div>
            <div class="operationTips">建议上传高于86x86像素的白底图片，长宽比例为1:1</div>
        </el-form-item>
        <el-form-item prop="sort" label="排序：">
            <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" type="number"></el-input-number>
            <div class="operationTips">支持输入0-255内的整数，数值越小排序越靠前</div>
        </el-form-item>
        <el-form-item prop="showFlag" label="是否显示：">
            <el-radio-group v-model="dataForm.showFlag">
                <el-radio label="1">显示</el-radio>
                <el-radio label="0">隐藏</el-radio>
            </el-radio-group>
            <div class="operationTips">默认勾选显示该菜单，勾选为隐藏则不显示该菜单</div>
        </el-form-item>
        <div class="btnSub">
            <el-button @click="changePage">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitStore()" :loading="buttonStatus">{{ $t('confirm') }}</el-button>
        </div>
    </el-form>

  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import { menuNews,addMobbileMenu,updateMobbileMenu,getTopicList,floorclassList  } from '@/api/api'
import { uploadPicBase64 } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";



export default {
  data () {
    return {
      topicList:[],
      classIds:[],
      classDataList:[],
      dataForm: {
        linkType: '',
        menuIcon: '',
        menuName: '',
        classId:"",
        topic:'',
        url: '',
        sort:0,
        showFlag:'1'
      },
      props:{
              label:'gcName',
              value: 'id'
      },
      breaddata: ["运营管理", "移动端首页配置", "首页菜单",'编辑首页菜单'],
      breaddatas: ["运营管理", "移动端首页配置", "首页菜单",'新增首页菜单'],
      buttonStatus:false,
      uploading:false,
      menuId:null,
      linkTypeList:[
            {value:'url',label:'外部链接'},
            {value:'coupon',label:'优惠券'},
            {value:'topic',label:'专题页'},
            {value:'searchGoodsByClass',label:'商品分类'},
            {value:'seckill',label:'秒杀'}
      ],
      dataRule:{
          linkType: [
            { required: true, message: '跳转类型不能为空！', trigger: 'blur' }
          ],
          menuName: [
            { required: true, message: '菜单名称不能为空！', trigger: 'blur' }
          ],
          classId: [
            { required: true, message: '所属分类不能为空！', trigger: 'blur' }
          ],
          topic: [
            { required: true, message: '专题页不能为空！', trigger: 'blur' }
          ],
          url: [
            { required: true, message: '链接地址不能为空！', trigger: 'blur' },
            {
                validator: function (rule, value, callback) {
                    if (!isURL(value) && this.dataForm.linkType=='url') {
                      return callback(new Error('链接地址格式不正确'))
                    }
                    callback()
                }, trigger: 'change'
            }
          ],
          sort: [
            { required: true, message: '排序不能为空！', trigger: 'blur' }
          ],
          menuIcon:[
            { required: true, message: '图标不能为空！', trigger: 'blur' }
          ]
      }
    }
  },
  components:{
        imgCropper,
        Bread
  },
  created(){
     this.jnn();
  },
  methods: {
    //展示分类
    jnn(){
        var params={
            'showType':0
        }
        floorclassList(params).then((res)=>{
            console.log('展示分类',res)
            this.classDataList = res.data;
        })
    },
    handleChangeOut(val){
      this.dataForm.url = val[val.length-1];
    },
    topicChange(){
      this.dataForm.topic=this.dataForm.url
    },
    linkChange(){
      this.dataForm.url=''
    },
    init(id){
      getTopicList().then((res)=>{
        this.topicList = res.data;
      });
      this.$nextTick(()=>{
              this.menuId = id;
              if(id){
                  var obj  = {
                      id:id
                  }
                  menuNews(obj).then((res)=>{
                   
                      console.log('回显',res.data)
                      if(res.code == 200){
                        if(res.data.linkType=="searchGoodsByClass"){
                          this.classIds = res.data.classId.split(",");
                        }
                        Object.assign(this.dataForm,res.data);
                        this.dataForm.showFlag = JSON.stringify(res.data.showFlag)
                        if(this.dataForm.linkType=='topic'){
                          this.dataForm.topic=this.dataForm.url
                        }
                      }
                  })
              }
      })
    },
    submitStore(){
      if(this.dataForm.linkType != 'searchGoodsByClass'){
          this.classIds = []
      }
      if(this.classIds){
        this.dataForm.classId = this.classIds.join(',');
      }
      if(this.dataForm.linkType=='seckill' || this.dataForm.linkType=='coupon'){
        this.dataForm.url=''
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.buttonStatus = true;
          if(this.menuId){//编辑
            var aa = this.dataForm;
            var bb = {
                id: this.menuId,
                linkType: aa.linkType,
                menuIcon: aa.menuIcon,
                menuName: aa.menuName,
                classId:aa.classId,
                showFlag: aa.showFlag,
                sort: aa.sort,
                url: aa.url
            }
            updateMobbileMenu(bb).then((res)=>{
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
            addMobbileMenu(this.dataForm).then((res)=>{
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

    GiftUrlHandle(imgUrl,index){
          let that = this;
          let url = imgUrl;
          that.dataForm.menuIcon = url;
    },
    changePage(){
      this.$emit("addoraditList");
    }
  }
};
</script>
<style lang="scss" scoped>
    .operationTips{
        color: #999999;
        font-size: 12px;

    }
     .homeIndex{
       .el-radio-group{
        margin-top: 0;
      }
        .el-input{
            width: 300px !important;
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
        .floorInput{
            width: 130px !important;
            .el-input{
                width: 130px !important;
            }
        }
    }
     .el-input-number .el-input {
         width: auto !important;
     }
</style>
