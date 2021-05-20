<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'"></Bread>
        <el-form :inline="true" :model="dataFormData" class="viewInfo" ref="dataFormData" v-loading="loading">

            <el-form-item label="登录名:">
                {{dataFormData.account}}
            </el-form-item>
            <el-form-item label="头像:">
                <div>
                    <!-- <img :src="dataFormData.logo | filterImgUrl" style=" object-fit: contain;" alt=""/>
                    <div style="float: right">
                        <span class="labelTitle" @click="changeAvatar">更换头像</span>
                        <br/>
                        <span class="tip">仅支持JPG、PNG、JPEG格式，文件小于5M</span>
                    </div> -->
                    <div style="width:88px"  v-loading="uploading"  >
                       <img-cropper
                          v-if="dataFormData.storeId==null"
                          ref="cropperImg"
                          :imgWidth='"88px"'
                          :imgHeight='"88px"'
                          :cropImg='dataFormData.logo'
                          @GiftUrlHandle="GiftUrlHandle1"
                      ></img-cropper>
                       <img-cropper2  v-if="dataFormData.storeId!=null"
                          ref="cropperImg"
                          :imgWidth='"88px"'
                          :imgHeight='"88px"'
                          :cropImg='dataFormData.logo'
                          @GiftUrlHandle="GiftUrlHandle"
                      ></img-cropper2>
                    </div>
                      <span class="tip">仅支持JPG、PNG、JPEG格式，文件小于5M</span>
                </div>
            </el-form-item>
            <el-form-item label="手机号:">
                {{dataFormData.encryptionPhone}}
                <span class="labelTitle"  @click="changePhone">换绑手机</span>
            </el-form-item>
            <el-form-item label="邮箱:">
                {{dataFormData.encryptionEmail}}
                <span class="labelTitle" @click="changeEmail">换绑邮箱</span>
            </el-form-item>
            <el-form-item label="登录密码:">
                <span style="border-bottom: 1px solid deepskyblue;color:deepskyblue;cursor: pointer;" @click="changePwdFn">修改密码</span>
            </el-form-item>
        </el-form>

         <!-- 更换绑定手机号或者邮箱 -->
        <modelChangeBind v-if="showChangeBindVisible" ref="changeBindCompon" @refreshData="getData"></modelChangeBind>

         <!-- 修改密码弹框 -->
        <changePwd v-if="changePwdVisible" ref="changePwdCompon"></changePwd>


    </div>
</template>

<script>
  import Bread from '@/components/bread'
  import modelChangeBind from './personalModules/model-change-bind.vue'
  import changePwd from './personalModules/model-change-pwd.vue'
  import imgCropper from "@/components/model-photo-cropper";
  import imgCropper2 from "@/components/upload/model-photo-cropper2";
  import { personnal,editLogo,uploadPicBase64 } from '@/api/api'

  export default {
    name: 'personalInfo',
    data () {
      return {
        breaddata: ['首页', '个人中心'],
        dataFormData: {},
        loading: true,
        showChangeBindVisible:false,// 是否展示绑定手机号或者邮箱的弹框
        changePwdVisible:false,// 修改密码弹框
        uploading:false,
      }
    },
    components: {
      Bread,
      modelChangeBind,
      changePwd,
      imgCropper,
      imgCropper2
    },
    created () {
      this.getData()
    },
    methods: {
      getData () {
        personnal().then((res => {
          if (res.code == 200) {
            this.dataFormData = res.data
            this.loading = false
          } else {

          }
        }))
      },
      // 更换头像
      GiftUrlHandle(imgUrl){
         this.dataFormData.logo = imgUrl;
         var obj = {
             logo:imgUrl
         }
         editLogo(obj).then((res)=>{
             if(res.code==200){
               this.$message.success(res.msg);
             }else{
               this.$message.error(res.msg);
             }
         })
      },
            // 更换头像
      GiftUrlHandle1(base64){
        this.uploadPic(base64);
      },
       uploadPic(base64,index){
          let that = this;
          const params = { "imgStr": base64 };
          this.uploading = true;
           params.type=1
          uploadPicBase64(params).then(res =>{
              if(res && res.code == "200"){
                  var url = res.data.url
                  that.dataFormData.logo = url
                  that.editLoGoFn();
              }else {
                 this.uploading = false
                  that.$message({
                      message:res.msg,
                      type: 'error',
                      duration: 1500,
                  })
              }
          });
      },
      editLoGoFn(){
         var obj = {
             logo:this.dataFormData.logo
         }
         editLogo(obj).then((res)=>{
           this.uploading = false
             if(res.code==200){
               this.$message.success(res.msg);
             }else{
               this.$message.error(res.msg);
             }
         })
      },



      // 更换手机号
      changePhone(){
        var obj  = {
          title:"换绑手机",
          changeWay:1,
          ...this.dataFormData
        }
        this.changePhoneOrEmial(obj);
      },
      // 更换邮箱
      changeEmail(){
         var obj  = {
          title:"换绑邮箱",
          changeWay:2,
          ...this.dataFormData
        }
        this.changePhoneOrEmial(obj);
      },
      changePhoneOrEmial(obj){
        this.showChangeBindVisible = true;
        this.$nextTick(()=>{
            this.$refs.changeBindCompon.init(obj);
        })
      },
      // 修改密码
      changePwdFn(){
        this.changePwdVisible = true;
         var obj  = {
          ...this.dataFormData
        }
        this.$nextTick(()=>{
            this.$refs.changePwdCompon.init(obj);
        })
      },
    }
  }
</script>

<style lang="scss" scoped>

    .viewInfo {
        margin-left: 10%;
    }

    .el-form-item {
        display: flex;
    }

    /deep/ .el-form-item__label {
        font-weight: bold;
        width: 200px;
    }

    /deep/ .el-form-item__content {
        margin-left: 15px;
    }


    img {
        width: 70px;
        height: 70px;
    }

    .tip {
        color: #999999;
        font-size: 12px;
        // margin-left: 8px;
    }

    .labelTitle{
        border-bottom: 1px solid deepskyblue;
        color:deepskyblue;
        margin-left:8px;
        cursor: pointer;
    }

</style>

