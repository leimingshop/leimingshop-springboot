<template>
    <div>
         <span class="upload" style="float: left">
          <div class="upload_warp">
            <div class="upload_warp_left upload_warp_left1" @click="showChooseImgWarp()">
              <span>从图片库中选择</span>
              <img-cropper  id="img_repository"
                            v-loading="uploading"
                            ref="showImgLib"
                            :index="'1'"
                            :imgWidth='"64px"'
                            :imgHeight='"64px"'
                            @GiftUrlHandle="GiftUrlHandle"
              ></img-cropper>
            </div>
            <div class="upload_warp_left upload_warp_left2" @click="fileClick">
              <span>从本地上传</span>
              <img src="@/assets/images/upload.png">
            </div>
            <div class="upload_warp_right" @drop="drop($event)" @dragenter="dragenter($event)" @dragover="dragover($event)">
              将图片拖到此处
            </div>
          </div>
          <div class="upload_warp_text">
            选中{{imgList.length}}张文件
            <!-- ，共{{bytesToSize(this.size)}} -->
          </div>
          <input @change="fileChange($event)" type="file" id="upload_file" multiple style="display: none"/>
          <div class="upload_warp_img" v-show="imgList.length!=0" v-loading="uploading">
            <div class="upload_warp_img_div" v-for="(item,index) of imgList">
              <div class="upload_warp_img_div_top">
                <div class="upload_warp_img_div_text">
                  {{item.file.name}}
                </div>
                <span class="bt1"  @click="changeLocation(item,index,'up')">上移</span>
                <span  class="bt2" @click="changeLocation(item,index,'down')">下移</span>
                <img src="@/assets/images/del.png" class="upload_warp_img_div_del" @click="fileDel(index)">
              </div>
              <!-- <img :src="item.file.src" v-arr="filterImgUrl |  v-attr="src: sex | isM""> -->
              <img :src="item.file.src | filterImgUrl">
            </div>
          </div>

            <!-- 添加图片列表弹出框  style="position: absolute; top: -50px; left: -180px; right: -180px; bottom: -225px; background: white" -->
            <wheelImages
                    v-if="wheelImagesVisible" ref="wheelImages" >
            </wheelImages>

        </span>
        <span style="float: left; line-height: 25px; margin-left: 20px;">
            <div class="msg1">1.基本要求：</div>
            <div class="msg2">(1) 手机详情总体大小：图片不超过20张；</div>
            <div class="msg2">建议：所有图片都是本宝贝相关的图片。</div>
            <div class="msg1">2.图片大小及要求：</div>
            <div class="msg2">(1) 建议使用宽度480～620像素、高度为960像素的图片；</div>
            <div class="msg2">(2) 格式为：JPG\JEPG\GIF\PNG；</div>
            <div class="msg2">举例：可以上传一张宽度为480，高度为960像素，格式为JPG的图片。</div>
        </span>
    </div>
</template>

<script>
 import { uploadPicBase64 } from '@/api/api'
 import wheelImages from './module/model-wheel-images.vue'
 import cloneDeep from 'lodash/cloneDeep'
import imgCropper from "@/components/upload/model-photo-cropper2";
export default {
  data () {
    return {
        imgList: [],
        size: 0,
        uploading:false,
        wheelImagesVisible:false,
        picturesLibVisible:false
    }
  },
  components: {
    wheelImages,
    imgCropper
  },
  watch: {
      imgList (val) {
         this.$parent.$parent.$parent.dataForm.mobileBody = JSON.stringify(this.imgList);
      }
   },
  created () {
  },
  methods: {
          fileClick() {
              document.getElementById('upload_file').click()
          },
          fileChange(el) {
              if (!el.target.files[0].size) return;
              this.fileList(el.target);
              el.target.value = ''
          },
          fileList(fileList) {
              let files = fileList.files;
               for (let i = 0; i < files.length; i++) {
                 if (!/image/.test(files[i].type)){
                   if (!files[i].type.includes('image/'))
                     this.$message.error('仅支持（jpg,jpeg,png,bmp,PNG,JEPG,JPG）为后缀的文件!');
                    return
                 }
               }
              for (let i = 0; i < files.length; i++) {
                  //判断是否为文件夹
                  if (files[i].type != '') {
                      this.fileAdd(files[i]);
                  } else {
                      //文件夹处理
                      this.folders(fileList.items[i]);
                  }
              }
          },
          showChooseImgWarp(){
              this.$refs.showImgLib.showImgLib();
          },
          //文件夹处理
          folders(files) {
              let _this = this;
              //判断是否为原生file
              if (files.kind) {
                  files = files.webkitGetAsEntry();
              }
              files.createReader().readEntries(function (file) {
                  for (let i = 0; i < file.length; i++) {
                      if (file[i].isFile) {
                          _this.foldersAdd(file[i]);
                      } else {
                          _this.folders(file[i]);
                      }
                  }
              })
          },
          foldersAdd(entry) {
              let _this = this;
              entry.file(function (file) {
                  _this.fileAdd(file)
              })
          },
          fileAdd(file) {
               let  that  = this;
               if(this.uploading){
                 return;
               }
              //  console.log(file);
              //   console.log(this.imgList);
              //总大小
              this.size = this.size + file.size;
              //判断是否为图片文件
              if (file.type.indexOf('image') == -1) {
                  file.src = '@/assets/images/wenjian.png';
                  this.imgList.push({
                      file
                  });
              } else {
                  let reader = new FileReader();
                  reader.vue = this;
                  reader.readAsDataURL(file);
                  reader.onload = function () {
                      // file.src = this.result;
                      that.uploadPic(this.result).then(res=>{
                          file.src = res;
                          this.vue.imgList.push({
                              file
                          });
                      })

                  }
              }
              // console.log(this.imgList);
          },
          fileDel(index) {
              this.size = this.size - this.imgList[index].file.size;//总大小
              this.imgList.splice(index, 1);
          },
          bytesToSize(bytes) {
              if (bytes === 0) return '0 B';
              let k = 1000, // or 1024
                  sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
                  i = Math.floor(Math.log(bytes) / Math.log(k));
              return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
          },
          dragenter(el) {
              el.stopPropagation();
              el.preventDefault();
          },
          dragover(el) {
              el.stopPropagation();
              el.preventDefault();
          },
          drop(el) {
              el.stopPropagation();
              el.preventDefault();
              this.fileList(el.dataTransfer);
          },
          GiftUrlHandle(imgUrl){
            var file ={
              src: imgUrl,
            }
            this.imgList.push({file})
          },
          // 上传图片
          uploadPic(base64){
            const params = { "imgStr": base64 };
            const that = this;
            this.uploading = true;
            params.type=1
            return new Promise(function(resolve){
              uploadPicBase64(params).then(res =>{
                that.uploading = false
                if(res && res.code == "200"){
                  var url = res.data.url
                  // that.currentIndex = -1;//不能这样写，防止网络延迟
                  resolve(url)
                }else {
                  // that.currentIndex = -1;//不能这样写，防止网络延迟
                 that.$message({
                      message: that.$t(res.msg),
                      type: 'error',
                      duration: 2500,
                      onClose: () => {
                      }
                })
                }
              })
            });
          },
          showChooseImgWarp(){
              this.$refs.showImgLib.showImgLib();
          },
          changeLocation(item,index2,type){
            if(type=="up"){
                if(index2==0) return;
                 var file =  cloneDeep(this.imgList[index2])
                this.imgList[index2] =  cloneDeep(this.imgList[index2-1])
                this.imgList[index2-1] =  cloneDeep(file)

            }else if(type=="down") {
                 if(index2<this.imgList.length-1){
                    var file =  cloneDeep(this.imgList[index2])
                    this.imgList[index2] =  cloneDeep(this.imgList[index2+1])
                    this.imgList[index2+1] =  cloneDeep(file)
                 }

            }
            this.imgList = [].concat(this.imgList);
          }


  }
}
</script>
 <style  lang="scss" scoped>
    .upload_warp_img_div_del {
      position: absolute;
      top: 6px;
      width: 16px;
      right: 4px;
    }

    .upload_warp_img_div_top {
      position: absolute;
      top: 0;
      width: 100%;
      height: 30px;
      background-color: rgba(0, 0, 0, 0.4);
      line-height: 30px;
      text-align: left;
      color: #fff;
      font-size: 12px;
      text-indent: 4px;
      .bt1{
        position: absolute;
        top: 0px;
        width: 30px;
        right: 70px;
      }
      .bt2{
          position: absolute;
          top: 0px;
          width: 30px;
          right: 36px;
      }
    }

    .upload_warp_img_div_text {
      white-space: nowrap;
      width: 80%;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .upload_warp_img_div img {
      max-width: 100%;
      max-height: 100%;
      vertical-align: middle;
    }

    .upload_warp_img_div {
      position: relative;
      height: auto;
      width: 96%;
      border: 1px solid #ccc;
      margin: 0px 0px 20px 2%;
      float: left;
      line-height: 100px;
      display: table-cell;
      text-align: center;
      background-color: #eee;
      cursor: pointer;
    }

    .upload_warp_img {
      border-top: 1px solid #D2D2D2;
      // padding: 14px 0 0 14px;
      overflow: hidden
    }

    .upload_warp_text {
      text-align: left;
      margin-bottom: 10px;
      padding-top: 10px;
      text-indent: 14px;
      border-top: 1px solid #ccc;
      font-size: 14px;
    }

    .upload_warp_right {
      float: left;
      width: 33.3%;
      margin-left: 2%;
      height: 100%;
      border: 1px dashed #999;
      border-radius: 4px;
      line-height: 130px;
      color: #999;
      text-align: center;
    }

    .upload_warp_left img {
      // margin-top: 32px;
    }

    .upload_warp_left {
      float: left;
      width: 33.3%;
      height: 100%;
      border: 1px dashed #999;
      border-radius: 4px;
      cursor: pointer;
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      color: #999;
    }
    .upload_warp_left2 {
        margin-left: 2%;
    }

    .upload_warp {
      margin: 14px;
      height: 130px;
      display: flex
    }

    .upload {
      border: 1px solid #ccc;
      background-color: #fff;
      width: 480px;
      box-shadow: 0px 1px 0px #ccc;
      border-radius: 4px;
    }

    .hello {
      width: 650px;
      margin-left: 34%;
      text-align: center;
    }

     /deep/ .el-loading-mask{
          z-index: 99;
    }
     .msg1 {
         font-weight: 700
     }
     .msg2 {
         color: gray;
         line-height: 20px;
     }
  </style>
