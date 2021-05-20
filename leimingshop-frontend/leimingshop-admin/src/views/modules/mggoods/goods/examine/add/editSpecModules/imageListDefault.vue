    <template>
    <div style="margin-left:-30px;margin-bottom:30px">
       <div v-for="(item,index) in imgList" class="imgWarpList" style="margin-bottom: 0px;">
            <div class="specName1" >{{item.specName1}}:</div>
            <!-- <transition-group> -->
            <!-- <img :src="item.imgListItem[0].image" alt="" class="mainPic"> -->
            <!-- {{item.imgListItem}} -->
            <div  v-if="item.imgListItem.length>0"
                class="pcCoverUrl imgUrl mainPic"
                :class="item.imgListItem[0].noImg && !item.imgListItem[0].image?'redWarp':''"
                @click="changeCurrentIndex(index,0)">
                  <!-- :aspectRatio="1 / 1" -->
                <img-cropper
                  ref="cropperImg"
                  :index="'0'"
                  :imgWidth='"190px"'
                  :imgHeight='"190px"'
                  :fontSize = "'90px'"
                  :lineHeight = "'160px'"
                  :cropImg='item.imgListItem[0].image'
                  @GiftUrlHandle="GiftUrlHandle"
                ></img-cropper>
                 <div  class="uploadCover" v-if="uploading" style="position:absolute;top:0;left:0;width: 190px;height: 190px;  z-index: 1000;"></div>
            </div>
            <div class="samllPic" v-if="item.imgListItem[0].image">
                <draggable v-model="item.imgListItem" draggable=".item" class="dragga">
                  <div v-for="(element,index2) in item.imgListItem" v-if="index2!=0" :key="Math.ceil(Math.random()*1000000)" class="item" style="margin-right:10px;">
                        <!-- <img  :src="element.image" alt=""> -->
                          <div class="pcCoverUrl imgUrl" >
                               <div @click="changeCurrentIndex(index,index2)">
                                 <!-- :aspectRatio="1 / 1" -->
                                    <img-cropper
                                    ref="cropperImg"
                                    :index="index2"
                                    :imgWidth='"88px"'
                                    :imgHeight='"88px"'
                                    :cropImg='element.image'
                                    @GiftUrlHandle="GiftUrlHandle"
                                  ></img-cropper>
                                  <div  class="uploadCover" v-if="uploading"></div>
                               </div>
                                 <!-- hover时显示 -->
                                <div class="hoverShow" v-if="false"   @click="deleteImg(index,index2)">
<!--                            <div class="hoverShow" v-if="element.image"   @click="deleteImg(index,index2)">-->
                                      <div class="pointer" >
                                        <!-- <img src="http://morefun.image.alimmdn.com/paimai/image/renweiicon2.png"> -->
                                        <span>删除</span>
                                      </div>
                                </div>
                            </div>
                  </div>
                </draggable>
            </div>
          <!-- </transition-group> -->
            <!-- <button slot="footer" @click="addPeople">Add</button> -->
       </div>
       <!-- <div style="margin-bottom: 30px; color: gray;margin-left: 90px">图片大小限制为10M内，图片格式为jpg\jpeg\png\bmp\PNG\JPEG\JPG</div> -->
    </div>
</template>

<script>

import cloneDeep from 'lodash/cloneDeep'
import draggable from 'vuedraggable'
import imgCropper from "./model-photo-cropper";
import { uploadPicBase64 } from '@/api/api'
import { setInterval } from 'timers';


export default {
  data () {
    return {
      // 默认规格的图片
        imgList:[
             {
              specName:'上传图片',
              specName1:'上传图片',
              specId:"规格属性id", //如颜色,尺码 的id
              id:"规格属性值id", //如红色，m码 的id
              imgListItem:[
                {
                  image:'',
                  specId:"规格属性id",//如颜色,尺码 的id
                  id:"规格属性值id", //如红色，m码 的id
                },
                {
                  image:''
                },
              ],
            },
        ],
        groupIndex:-1,
        currentIndex:-1,
        uploading:false,
        specAttributePictureSaveDTOListDefault:[],
    }
  },
  props:["goodsSpecSaveDTOList","dataForm","specLevel_1","specLevel_2"],
  components: {
    draggable,
    imgCropper
  },
 watch: {
    'goodsSpecSaveDTOList.length' (val) {
    //    this.calculateImgList();
    }
  },
  created () {
    //  setInterval(()=>{
        // this.imgbackScan();
    //  },500)
  },
  methods: {
       // 编辑点进来的图片回显
        editImgBackSacn(imgList){
            this.imgList =  cloneDeep(imgList);
            // console.log("cloneDeep(imgList)");
            // console.log(this.imgList );
            this.setSpecAttributePictureSaveDTOListDefault();
        },
        calculateImgList(){
            var lastItem = {};
            var imgList = [];
             console.log("goodsSpecSaveDTOList");
            console.log( this.goodsSpecSaveDTOList);
            this.goodsSpecSaveDTOList.forEach((item,index)=>{

                // 添加过
                if(lastItem.specName1 == item.specName1){
                    // imgList[imgList.length-1].imgListItem.push(
                    //   {image:"http://morefun.image.alimmdn.com/lancomeC2B/gift_3.png"}
                    // ) ;
                }else{
                  // 没添加过
                    imgList.push({});
                    imgList[imgList.length-1].specName = item.specName ;
                    imgList[imgList.length-1].specName1 = item.specName1 ;
                    imgList[imgList.length-1].id = item.img_use_id ;
                    imgList[imgList.length-1].specId = item.img_use_specId ;
                    imgList[imgList.length-1].imgListItem = [
                      // {image:"http://morefun.image.alimmdn.com/lancomeC2B/gift_3.png"},//一张大图
                      // {image:"http://morefun.image.alimmdn.com/lancomeC2B/gift_4.png"},//一张小图
                      // {image:"http://morefun.image.alimmdn.com/lancomeC2B/gift_3.png"},//一张小图
                      {image:"",id:item.img_use_id,specId:item.img_use_specId},//一张大图
                      {image:"",id:item.img_use_id,specId:item.img_use_specId},//一张小图
                      // {image:""},//一张小图
                    ] ;
                }
                lastItem = cloneDeep(item);
                console.log(item,index);
              // 追加specId（规格id，如颜色）和id（规格值id，如红色）让后端用
                imgList && imgList.forEach((item2,index2)=>{
                    item2.imgListItem && item2.imgListItem.forEach((item3,index3)=>{
                        if(!item3.id) item3.id = item.img_use_id
                        if(!item3.specId) item3.specId = item.img_use_specId
                    })
                })

            })
            console.log("imgList")
            console.log(imgList);
            // 保存新的数据 和以前老的数据，吧老的数据的image保存上
            imgList.forEach((item,index)=>{
                  this.imgList.forEach((item3,index3)=>{
                    console.log([item.specId,item3.specId,"##################",item.id,item3.id]);
                      if( item.specId==item3.specId && item.id == item3.id){
                            item.imgListItem =cloneDeep(item3.imgListItem)
                      }
                  })
            })
            this.imgList = cloneDeep(imgList);
            this.setSpecAttributePictureSaveDTOListDefault();
        },
        changeCurrentIndex(index1,index2){
          return
          // e.stopPropagation()
          if(this.uploading){
             this.$message({
                message: "请等待上次图片上传完毕!",
                type: 'warning',
                duration: 500,
              })
            return;
          }
          this.groupIndex = index1,
          this.currentIndex  = index2;
           console.log("两个下标");
          console.log([index1,index2]);
        },
        imgbackScan(){
          let that = this;
          var length = 0
          that.imgList.forEach((item,index)=>{
                item.imgListItem.forEach((item2,index2)=>{
                  this.$nextTick(()=>{
                      length++;
                      if(item2.image && this.$refs.cropperImg[length] && this.$refs.cropperImg[length].cropper){
                        this.$refs.cropperImg[length].cropper.imgShow = true;
                        this.$refs.cropperImg[length].cropper.cropImg = item2.image;
                      }else{
                          // item.imgListItem.splice(index2,1);
                      }
                  })
                })
          })
        },
        //上传图片
        uploadPic(base64){
          if(this.currentIndex == -1){
            alert("前端未捕捉到图片的下标,请重新点击上传!");
            return;
          }
          const params = { "imgStr": base64 };
          const that = this;
          this.uploading = true;
          return new Promise(function(resolve){
            uploadPicBase64(params).then(res =>{
              that.uploading = false
              if(res && res.code == "200"){
                var url = res.data.url
                if(that.currentIndex!=-1){
                  console.log(that.imgList);
                  if( that.currentIndex==0){
                      that.$parent.$parent.goodsSpecSaveDTOList.forEach((item,index)=>{
                        if(item.img_use_id == that.imgList[that.groupIndex].imgListItem[0].id){
                          //  if(!item.specMainPicture)
                           item.specMainPicture = url;
                        }
                          // alert(item.specMainPicture)
                      })
                    //  that.$emit("setGoodsSpecSaveDTOList");
                  }
                  that.imgList[that.groupIndex].imgListItem[that.currentIndex].image =  url;
                  var len = that.imgList[that.groupIndex].imgListItem.length;
                  if(that.imgList[that.groupIndex].imgListItem[len-1].image && len <15){
                       that.imgList[that.groupIndex].imgListItem.push({
                          image:'' ,
                          id:that.imgList[that.groupIndex].imgListItem[len-1].id,
                          specId:that.imgList[that.groupIndex].imgListItem[len-1].specId
                      });
                  };

                  // that.$set(that.imgList[that.groupIndex].imgListItem,that.imgList[that.groupIndex].imgListItem.length-1,that.imgList[that.groupIndex].imgListItem[that.imgList[that.groupIndex].imgListItem.length-1]);
                  that.imgList = [].concat(that.imgList);
                  // that.imgbackScan();
                  console.log("上传图片之后：");
                  console.log(that.imgList);
                  console.log(that.imgList[that.groupIndex]);

                }

                that.setSpecAttributePictureSaveDTOListDefault();
                that.$message({
                  message: "图片上传成功!",
                  type: 'success',
                  duration: 800,
                })
                // that.currentIndex = -1;//不能这样写，防止网络延迟
                resolve("true")
              }else {
                // that.currentIndex = -1;//不能这样写，防止网络延迟
                 that.imgList[that.groupIndex].imgListItem[that.currentIndex].image = ""
                 that.$message({
                  message: res.msg,
                  type: 'error',
                  duration: 800,
                })
                resolve("false")
              }
            })
          });
        },
        GiftUrlHandle(val){
          return
          console.log("base64上传图片接口");
          // console.log(val);
          this.uploadPic(val);
        },
        setSpecAttributePictureSaveDTOListDefault(){
          let that = this;
          var specAttributePictureSaveDTOListDefault = [
            // {
            //   "isMainPicture": 0,//是否主图（默认0否，1是） ,
            //   "pictureUrl": "string",//图片地址
            //   "sort": 0,//排序
            //   "specAttrId": 0,//商品规格属性ID ,
            //   "specAttrValueId": 0//商品规格属性值ID
            // }
          ];
          console.log("-----------------------------");
          console.log(that.imgList);
          let  sort = 1;
          that.imgList.forEach((item,index)=>{
             // 方式编辑回显时为空时数组
              if(item.imgListItem.length==0){
                console.log("图片空数组push");
                  item.imgListItem = [
                     {image:"",id:item.id,specId:item.specId},//一张大图
                     {image:"",id:item.id,specId:item.specId},//一张小图
                  ]
              }

              item.imgListItem.forEach((item2,index2)=>{
                    sort++;
                    var obj = new Object();
                    obj.isMainPicture = index2==0?1:0
                    obj.pictureUrl = item2.image;
                    obj.sort = sort;
                    obj.specAttrId = item2.specId;
                    obj.specAttrValueId = item2.id;
                    specAttributePictureSaveDTOListDefault.push(obj);
              })

          })
          this.specAttributePictureSaveDTOListDefault =  cloneDeep(specAttributePictureSaveDTOListDefault)
          console.log(specAttributePictureSaveDTOListDefault)
        //   this.$emit("setSpecAttributePictureSaveDTOList",specAttributePictureSaveDTOList);
        },
        addPeople(){

        },
        deleteImg(index,index2){
             this.imgList[index].imgListItem.splice(index2,1);
              var len = this.imgList[index].imgListItem.length;
             if(this.imgList[index].imgListItem[len-1].image){
                  this.imgList[index].imgListItem.push(
                     {image:"",id:this.imgList[index].id,specId:this.imgList[index].specId},//一张小图
                  )
             }
        }
  }
}
</script>


<style lang="scss" scoped>
.imgWarpList{
    display:flex;
    // width:700px;
    height:190px;
    align-items: center;
    margin-bottom:30px;
    //左边 主图
    .mainPic{
        width: 190px;
        height: 190px;
        line-height: 190px;
        margin-right: 20px;
        position: relative;
    }
    // 右边小图
    .samllPic{
        display: flex;
        align-items: flex-start;
        height: 100%;
      .dragga{
        display: flex;
        flex-wrap: wrap;
        height: 100%;
        //  &>span{
        //    display: inherit;
            .item{
              display: inline;
          //   margin-right:10px;
          //   width: 88px;
          //   height: 88px;
          //   line-height: 88px;
          //   text-align: center;
            .pcCoverUrl,img{
              width: 88px;
              height: 88px;
              position: relative;
              overflow: hidden;
            }
          }
        //  }
      }
    }
  }
.uploadCover{
    position:absolute;
    top:0;
    left:0;
    width: 88px;
    height: 88px;
    z-index: 1000;
}

.pcCoverUrl:hover .hoverShow {
  transform: translateY(0);
}

.hoverShow {
  position: absolute;
  left: 0;
  bottom: 0;
  z-index: 1000;
  width: 100%;
  height: 22px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  transform: translateY(22px);
  transition: transform .5s;
  background: rgba(0, 0, 0, .6);
  color: white;
}

.hoverShow>div {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-direction: column;
}

.hoverShow>div>span {
  // margin-top: 5px;
}

.pointer {
  cursor: pointer;
}

.pointer img {
  width: 24px;
  height: 24px;
}

.specName1{
  height: 100%;
  width:70px;
  margin-right: 20px;
  text-align:right
}
.specName1:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
}
.redWarp{
  border:1px solid red;
  overflow: hidden;
}
</style>

