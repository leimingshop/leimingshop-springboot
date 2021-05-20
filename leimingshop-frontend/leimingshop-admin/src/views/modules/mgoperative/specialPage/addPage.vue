<template>
    <div>
        <div v-show="!commoditiesList">
        <Bread :breaddata="menuId?breaddata:breaddatas" @changePage="changePage" :index="'2'"></Bread>
        <el-form :model="dataForm" ref="dataForm" :rules="dataRule" @keyup.enter.native="submitStore()" label-width="140px">
            <el-form-item prop="topicName" label="专题名称：">
                <el-input v-model="dataForm.topicName" placeholder="请输入专题名称" style="width:300px"></el-input>
            </el-form-item>
            <el-form-item   label="专区顶部横幅(PC)：" prop="topicPicturePc">
               <div class="img">
                    <div style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'0'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="picList[0]"
                        @GiftUrlHandle="GiftUrlHandle1">
                    </img-cropper>
                    <div v-if="picList[0]" @click="deleteImgPc(0)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="picList[0]"  style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'1'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="picList[1]"
                        @GiftUrlHandle="GiftUrlHandle1">

                    </img-cropper>
                    <div v-if="picList[1]" @click="deleteImgPc(1)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="picList[1]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'2'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="picList[2]"
                        @GiftUrlHandle="GiftUrlHandle1">

                    </img-cropper>
                    <div v-if="picList[2]" @click="deleteImgPc(2)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="picList[2]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'3'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="picList[3]"
                        @GiftUrlHandle="GiftUrlHandle1">

                    </img-cropper>
                   <div v-if="picList[3]" @click="deleteImgPc(3)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="picList[3]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'4'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="picList[4]"
                        @GiftUrlHandle="GiftUrlHandle1">
                    </img-cropper>
                    <div v-if="picList[4]" @click="deleteImgPc(4)" style="text-align:center">
                        删除
                    </div>
                </div>
               </div>
            </el-form-item>
            <el-form-item  label="专区顶部横幅(APP)：" label-width="145px" prop="topicPictureH5">
                <div class="img">
                    <div style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'0'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="appPicList[0]"
                        @GiftUrlHandle="GiftUrlHandle2">
                    </img-cropper>
                    <div v-if="appPicList[0]" @click="deleteImgH5(0)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="appPicList[0]"  style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'1'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="appPicList[1]"
                        @GiftUrlHandle="GiftUrlHandle2">
                    </img-cropper>
                    <div v-if="appPicList[1]" @click="deleteImgH5(1)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="appPicList[1]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'2'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="appPicList[2]"
                        @GiftUrlHandle="GiftUrlHandle2">
                    </img-cropper>
                    <div v-if="appPicList[2]" @click="deleteImgH5(2)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="appPicList[2]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'3'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="appPicList[3]"
                        @GiftUrlHandle="GiftUrlHandle2">
                    </img-cropper>
                    <div v-if="appPicList[3]" @click="deleteImgH5(3)" style="text-align:center">
                        删除
                    </div>
                </div>
                <div v-if="appPicList[3]" style="width: 100px;margin-right:5px">
                    <img-cropper
                        ref="imageUrl1"
                        :index="'4'"
                        :imgWidth='"100px"'
                        :imgHeight='"100px"'
                        :cropImg="appPicList[4]"
                        @GiftUrlHandle="GiftUrlHandle2">
                    </img-cropper>
                    <div v-if="appPicList[4]" @click="deleteImgH5(4)" style="text-align:center">
                        删除
                    </div>
                </div>
               </div>
            </el-form-item>
            <el-form-item style="margin-top:40px">
                <el-button @click="changePage">取消</el-button>
                <el-button type="primary" @click="addGoodsList(dataForm)">继续并管理商品</el-button>
            </el-form-item>
        </el-form>
        </div>
        <commodities v-if="commoditiesList" ref="commoditiesCompon" @closeGoods="closeGoods" @changePage="changePage"></commodities>
    </div>
</template>
<script>
import Bread from "@/components/bread";
import commodities from './commodities.vue';
import {getTopicGoods,getTopicDetail,topicAdd,topicUpdate} from '@/api/api';
import imgCropper from "@/components/upload/model-photo-cropper2";

export default {
    components:{
        Bread,
        commodities,
        imgCropper
    },
    data(){
        return{
            picList:[

            ],
            appPicList:[

            ],
            commoditiesList:false,
            dataForm:{
                topicName:"",
                topicPicturePc:"",
                topicPictureH5:"",
            },
            id:"",
            action:"https://jsonplaceholder.typicode.com/posts/",
            breaddata: ["运营管理", "专题管理", "专题页管理",'编辑专题页'],
            breaddatas: ["运营管理", "专题管理", "专题页管理",'新增专题页'],
            menuId:null,
            dataRule:{
                topicName:[
                    {required:true,message:"请输入专题名称",trigger:'blur'}
                ],
                topicPicturePc:[
                    {required:true,message:"请选择专题图片",trigger:'blur'}
                ],
                topicPictureH5:[
                    {required:true,message:"请选择专题图片",trigger:'blur'}
                ]
            }
        }
    },
    methods:{
        init(id){
            this.id=id;
            if(id){
                if(this.id){
                getTopicDetail({id:id}).then((res)=>{
                    if(res.code=200){
                        this.dataForm=res.data
                        this.appPicList=this.dataForm.topicPictureH5.split(",")
                        this.picList=this.dataForm.topicPicturePc.split(",")
                    }
                })
            }
            }

        },

        //返回上级
        changePage(){
            this.$emit("addoraditList");
        },
        addGoodsList(dataForm,id){

            if(!this.dataForm.topicName){
                this.$message.error("请输入专题名称")
                return
            }
            // if(this.picList.length==0){
            //     this.$message.error("请选择PC专区顶部横幅")
            //     return
            // }
            // if(!this.appPicList.length==0){
            //     this.$message.error("请选择H5专区顶部横幅")
            //     return
            // }
            this.dataForm.topicPicturePc=this.picList.join(",");
            this.dataForm.topicPictureH5=this.appPicList.join(",");
            this.commoditiesList=true;
            this.$nextTick(()=>{
                this.$refs.commoditiesCompon.init(this.dataForm,this.id);
            })
        },
        closeGoods(){
             this.commoditiesList=false;
        },
        addorGoods(){
            this.commoditiesList=!this.commoditiesList;
        },
        GiftUrlHandle1(topicPicturePc,index){
            // this.picList.push(topicPicturePc)
            this.$set(this.picList,index,topicPicturePc)

        },
        GiftUrlHandle2(topicPictureH5,index){
            // this.appPicList.push(topicPictureH5)
            this.$set(this.appPicList,index,topicPictureH5)

        },
        deleteImgPc(index){
            this.picList.splice(index,1);
        },
        deleteImgH5(index){
            this.appPicList.splice(index,1);
        },
    }
}
</script>
<style lang="scss" scoped>
    .el-form{
        padding-top: 50px;
        width: 1100px;
        margin: 0 auto;
    }
    .avatar-upload{
        border: 1px solid #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .el-icon-plus{
        font-size: 28px;
        color: #8c939d;
        width: 78px;
        height: 78px;
        line-height: 78px;
        text-align: center;
    }
    .avatar-upload{
        width: 120px;
        height: 120px;
        line-height: 120px;
        text-align: center;
    }
    .el-form-item{
        margin-bottom: 40px !important;
    }
    .onNext{
        color: #ffffff;
        border-color: #2260d2;
        background-color: #2260d2;
        &:hover{
            border-color: #1354cd;
            background-color: #1354cd;
        }
    }
    .img{
        margin-left:5px;
        display:flex
        }
        .delete{
            position: absolute;
            bottom: 30px;
            left: 40px;
            color: #c7c7c7;
        }
    // /deep/ .el-form-item__label{
    //     width: 156px !important;
    // }
    // /deep/ .el-form-item__content{
    //     margin-left: 156px !important;
    // }
</style>
