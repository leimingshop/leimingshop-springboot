<template>
  <div class="homeFloor">
    <Bread :breaddata="floorId?breaddata:breaddatas" @changePage="changePage" :index="'2'"></Bread>

    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="140px">
        <el-form-item prop="floorName" label="楼层名称：">
            <el-input v-model="dataForm.floorName" maxlength="4" clearable show-word-limit></el-input>
        </el-form-item>
        <!-- <el-form-item prop="floorCode" label="楼层标识:">
            <el-input v-model="dataForm.floorCode" placeholder="" :disabled="floorId?true:false" clearable @blur="tocnki"></el-input>
        </el-form-item> -->
        <el-form-item prop="jiaoyan" label="展示内容：">
            <!-- <el-input v-model="dataForm.jiaoyan" style="display:none" v-if="picList.length == 6" ></el-input> -->
            <div @change="changeFloor()">
                <el-radio v-model="dataForm.floorStyle" :label=1>样式一</el-radio>
                <el-radio v-model="dataForm.floorStyle" :label=2>样式二</el-radio>
                <el-radio v-model="dataForm.floorStyle" :label=3>样式三</el-radio>
            </div>
            <div class="picMain" v-if="dataForm.floorStyle==1">
                <div style="margin-bottom: 5px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(0)" v-if="!picList[0]" alt="图标" style="margin-right:5px;" class="img1">
                    <img :src="picList[0]" alt="img" @click="changeClass(0)" v-if="picList[0]" style="margin-right:5px" class="img1">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(1)" alt="图标" v-if="!picList[1]" class="img1">
                    <img :src="picList[1]" alt="img" @click="changeClass(1)" v-if="picList[1]" class="img1">
                </div>
                <div>
                    <img src="~@/assets/img/nopic.png" @click="changeClass(2)" alt="图标" v-if="!picList[2]" class="img2" style="margin-right:5px">
                    <img :src="picList[2]" alt="img" @click="changeClass(2)" v-if="picList[2]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(3)" alt="图标" v-if="!picList[3]" class="img2" style="margin-right:5px">
                    <img :src="picList[3]" alt="img" @click="changeClass(3)" v-if="picList[3]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(4)" alt="图标" v-if="!picList[4]" class="img2" style="margin-right:5px">
                    <img :src="picList[4]" alt="img" @click="changeClass(4)" v-if="picList[4]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(5)" alt="图标" v-if="!picList[5]" class="img2">
                    <img :src="picList[5]" alt="img" @click="changeClass(5)" v-if="picList[5]" class="img2">

                </div>
            </div>
            <div class="picMain" v-if="dataForm.floorStyle==2">
                <div style="margin-bottom: 5px">
                   <img src="~@/assets/img/nopic.png" @click="changeClass(0)" alt="图标" v-if="!picList[0]" class="img2" style="margin-right:5px">
                    <img :src="picList[0]" alt="img" @click="changeClass(0)" v-if="picList[0]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(1)" alt="图标" v-if="!picList[1]" class="img2" style="margin-right:5px">
                    <img :src="picList[1]" alt="img" @click="changeClass(1)" v-if="picList[1]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(2)" alt="图标" v-if="!picList[2]" class="img2" style="margin-right:5px">
                    <img :src="picList[2]" alt="img" @click="changeClass(2)" v-if="picList[2]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(3)" alt="图标" v-if="!picList[3]" class="img2">
                    <img :src="picList[3]" alt="img" @click="changeClass(3)" v-if="picList[3]" class="img2">
                </div>
                <div style="margin-bottom: 5px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(4)" alt="图标" v-if="!picList[4]" class="img2" style="margin-right:5px">
                    <img :src="picList[4]" alt="img" @click="changeClass(4)" v-if="picList[4]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(5)" alt="图标" v-if="!picList[5]" class="img2" style="margin-right:5px">
                    <img :src="picList[5]" alt="img" @click="changeClass(5)" v-if="picList[5]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(6)" alt="图标" v-if="!picList[6]" class="img2" style="margin-right:5px">
                    <img :src="picList[6]" alt="img" @click="changeClass(6)" v-if="picList[6]" class="img2" style="margin-right:5px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(7)" alt="图标" v-if="!picList[7]" class="img2">
                    <img :src="picList[7]" alt="img" @click="changeClass(7)" v-if="picList[7]" class="img2">
                </div>
            </div>
            <div class="picMain" v-if="dataForm.floorStyle==3">
                <div style="margin-bottom: 5px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(0)" v-if="!picList[0]" alt="图标" style="margin-right:5px;" class="img3">
                    <img :src="picList[0]" alt="img" @click="changeClass(0)" v-if="picList[0]" style="margin-right:5px" class="img3">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(1)" alt="图标" v-if="!picList[1]" class="img3">
                    <img :src="picList[1]" alt="img" @click="changeClass(1)" v-if="picList[1]" class="img3">
                </div>
                <div style="margin-bottom: 5px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(2)" v-if="!picList[2]" alt="图标" style="margin-right:5px;" class="img3">
                    <img :src="picList[2]" alt="img" @click="changeClass(2)" v-if="picList[2]" style="margin-right:5px" class="img3">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(3)" alt="图标" v-if="!picList[3]" class="img3">
                    <img :src="picList[3]" alt="img" @click="changeClass(3)" v-if="picList[3]" class="img3">
                </div>
                 <div style="margin-bottom: 10px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(4)" v-if="!picList[4]" alt="图标" style="margin-right:5px;" class="img3">
                    <img :src="picList[4]" alt="img" @click="changeClass(4)" v-if="picList[4]" style="margin-right:5px" class="img3">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(5)" alt="图标" v-if="!picList[5]" class="img3">
                    <img :src="picList[5]" alt="img" @click="changeClass(5)" v-if="picList[5]" class="img3">
                </div>
            </div>
            <div class="operationTips">此处需要依次点进去并需完成全部展示内容编辑</div>
        </el-form-item>
        <el-form-item prop="nameIcon" label="楼层名称图片：">
            <div v-loading="uploading" style="width: 100px;display: flex;">
                <img-cropper style="box-shadow: 1px 1px 1px #efefef;"
                    ref="nameIcon"
                    :index="'nameIcon'"
                    :imgWidth='"300px"'
                    :imgHeight='"50px"'
                    :cropImg="dataForm.nameIcon"
                    @GiftUrlHandle="GiftUrlHandle">
                </img-cropper>
                <el-button type="text" @click="deleteNameIcon" v-if="dataForm.nameIcon" style="margin-left:10px;">删除</el-button>
            </div>
            <div class="operationTips">建议上传高于750x50像素的白底图片</div>
        </el-form-item>
        <el-form-item prop="actionType" label="查看更多跳转类型：">
            <el-radio-group v-model="dataForm.actionType" @change="toGetType">
                <el-radio-button label="link">链接</el-radio-button>
                <el-radio-button label="searchGoodsByClass">展示分类</el-radio-button>
                <el-radio-button label="searchByKeyWord">关键字</el-radio-button>
                <el-radio-button label="topic">专题页</el-radio-button>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="actionParams " label="链接地址：" v-if="dataForm.actionType == 'link'">
            <el-input v-model="dataForm.actionParams" placeholder="Http://xxxxxx.com" clearable></el-input>
            <div class="operationTips">仅支持输入Http://开头的链接格式</div>
        </el-form-item>
        <el-form-item prop="actionParams " label="所属分类：" v-if="dataForm.actionType == 'searchGoodsByClass'">
            <!-- <el-input v-model="dataForm.goodsClassName" placeholder="" clearable v-if="floorId&&!faClassStatus" @focus="toFaClassStatus"></el-input> -->

            <el-cascader
                :options="classDataList"
                change-on-select
                :clearable="true"
                :props="props"
                v-model="dataForm.classIds"
                @change="handleChange">
            </el-cascader>
        </el-form-item>
        <el-form-item prop="actionParams " label="关键字：" v-if="dataForm.actionType == 'searchByKeyWord'">
            <el-input v-model="dataForm.actionParams" placeholder="" maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item prop="actionParams " label="专题页：" v-if="dataForm.actionType == 'topic'">
        <el-select v-model="dataForm.actionParams" filterable placeholder="请选择">
                <el-option
                    v-for="item in topicList"
                    :key="item.id"
                    :label="item.topicName"
                    :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="isShow" label="是否显示：">
            <el-radio-group v-model="dataForm.isShow">
                <el-radio label="1">显示</el-radio>
                <el-radio label="0">隐藏</el-radio>
            </el-radio-group>
            <div class="operationTips">默认勾选显示该楼层，勾选为隐藏则不显示该楼层</div>
        </el-form-item>
        <el-form-item prop="sort" label="优先级排序：">
            <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number>
            <div class="operationTips">支持输入0-255内的整数，数值越小排序越靠前</div>
        </el-form-item>
        <div class="btnSub">
            <el-button @click="changePage">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitStore()" :loading="buttonStatus">{{ $t('confirm') }}</el-button>
        </div>
    </el-form>

    <!-- 图片弹框 -->
    <el-dialog
        title="类型配置"
        :visible.sync="photoVisible"
        :close-on-click-modal = "false"
        :show-close = "false"
        width="652px">
        <el-form :model="dataForm1" :rules="dataRuleOther" ref="dataRuleOther" style="margin-top: -46px;" label-width="120px">
            <el-form-item label="标题：" prop="title">
                <div>
                    <el-input v-model="dataForm1.title" style="display:none" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].title"></el-input>
                    <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].title" maxlength="4" show-word-limit></el-input>
                </div>
                <span style="color:#f56c6c" v-if="!dataForm.webFloorLinkConfigDTOList[dataNum].title&&imgStatus">标题不能为空</span>
            </el-form-item>
            <el-form-item label="副标题：">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].subTitle" maxlength="8" show-word-limit></el-input>
            </el-form-item>
            <el-form-item prop="classImg" label="上传图片：">
                <el-input v-model="dataForm1.classImg" style="display:none" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].imgUrl"></el-input>
                <img-cropper
                    ref="classImg"
                    :index="dataNum"
                    :imgWidth='"100px"'
                    :imgHeight='"100px"'
                    :cropImg='dataForm.webFloorLinkConfigDTOList[dataNum].imgUrl'
                    @GiftUrlHandle="GiftUrlHandle">
                </img-cropper>
                <span style="color:#f56c6c" v-if="!dataForm.webFloorLinkConfigDTOList[dataNum].imgUrl&&imgStatus">图片还没上传</span>
                <div class="operationTips">建议上传高于250x150像素的白底图片</div>
            </el-form-item>
            <el-form-item prop="linkType" label="跳转类型：">
                <div>
                    <el-input v-model="dataForm1.linkType" style="display:none" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType"></el-input>
                    <el-radio-group v-model="dataForm.webFloorLinkConfigDTOList[dataNum].linkType" @change="toGetTypeChild">
                        <el-radio-button label="link">链接</el-radio-button>
                        <el-radio-button label="searchGoodsByClass">展示分类</el-radio-button>
                        <el-radio-button label="searchByKeyWord">关键字</el-radio-button>
                        <el-radio-button label="goodsDetail">商品详情</el-radio-button>
                        <el-radio-button label="topic">专题页</el-radio-button>
                        <!-- <el-radio-button label="xianshiqiangList">限时抢购</el-radio-button> -->
                    </el-radio-group>
                </div>
                <span style="color:#f56c6c" v-if="!dataForm.webFloorLinkConfigDTOList[dataNum].linkType&&imgStatus">跳转类型不能为空</span>
            </el-form-item>
            <el-form-item prop="actionParams" label="链接地址：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'link'">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" placeholder="Http://xxxxxx.com" clearable></el-input>
                <div class="operationTips">仅支持输入Http://开头的链接格式</div>
            </el-form-item>
            <el-form-item prop="actionParams" label="所属分类：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'searchGoodsByClass'">
                <!-- <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].gcName" clearable v-if="floorId&&!chClassStatus" @focus="toChClassStatus"></el-input> -->
                <el-cascader
                    :options="classDataList"
                    change-on-select
                    :clearable="true"
                    :props="props"
                    v-model="dataForm.webFloorLinkConfigDTOList[dataNum].classIds"
                    @change="handleChangeOut">
                </el-cascader>
            </el-form-item>
            <el-form-item prop="actionParams" label="关键字：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'searchByKeyWord'">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" maxlength="20" placeholder="" clearable></el-input>
            </el-form-item>
            <el-form-item prop="actionParams" label="商品名称：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'goodsDetail'">
                <el-select v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" filterable placeholder="请选择" @change="getChangeChild">
                    <el-option
                        v-for="item in goodsList"
                        :key="item.specId"
                        :label="item.goodsName"
                        :value="item.specId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="actionParams " label="专题页：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'topic'">
            <el-select v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" filterable placeholder="请选择">
                    <el-option
                        v-for="item in topicList"
                        :key="item.id"
                        :label="item.topicName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div style="text-align: center;">
             <span slot="footer" class="dialog-footer" >
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="startCheck">确 定</el-button>
            </span>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import cloneDeep from 'lodash/cloneDeep'
import { uploadPicBase64,flooradveDetail,updateMobbileFloor,addMobbileFloor } from '@/api/api';
import { mobbileFloorCnki,mobbileGoodsList,floorclassList,getTopicList } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";


export default {
    data () {
        return {
            editStyle:'',
            topicList:[],
            editStyleList:[],
            picListCopy:[],
            otherObj:'',
            dataForm: {
                floorStyle:1,
                classIds:[],
                jiaoyan:'',
                actionType:'link',
                sort:0,
                isShow:'1',
                actionParams: "",
                floorCode: "",
                floorName: "",
                goodsClassName: "",
                goodsName: "",
                nameIcon: "",
                webFloorLinkConfigDTOList: [
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                ]
            },
            classDataList:[],
            props:{
                label:'gcName',
                value: 'id'
            },
            breaddata: ["运营", "移动端首页配置", "首页楼层",'编辑楼层'],
            breaddatas: ["运营", "移动端首页配置", "首页楼层",'新增楼层'],
            startObj:'',//回显的初始数据
            buttonStatus:false,
            imgStatus:false,
            photoVisible:false,
            picList:[],
            goodsList:[],
            uploading:false,
            floorId:null,
            dataNum:0,
            transientData:[
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                ],
            dataForm1:{// 伪验证
                title:'1212',
                linkType:'212',
                classImg:'2121'
            },
            dataRuleOther:{
                 title: [
                    { required: true, message: '标题不能为空', trigger: 'blur' }
                ],

                classImg :[
                    { required: true, message: '上传图片不能为空', trigger: 'blur' }
                ],
                linkType :[
                    { required: true, message: '跳转类型不能为空', trigger: 'blur' }
                ],
            }
        }
    },
    computed: {
        dataRule () {
            return {
                floorName: [
                    { required: true, message: '楼层名称不能为空', trigger: 'blur' }
                ],
                floorCode: [
                    { required: true, message: '楼层标识不能为空！', trigger: 'blur' }
                ],
                actionType: [
                    { required: true, message: '跳转类型不能为空！', trigger: 'blur' }
                ],
                isShow: [
                    { required: true, message: '是否显示不能为空！', trigger: 'blur' }
                ],
                sort: [
                    { required: true, message: '优先级排序不能为空！', trigger: 'blur' }
                ]
            }
        },
    },
    components:{
        imgCropper,
        Bread
    },
    created(){
        let obj = {
            params:{
                page:1,
                limit:9999,
                goodsShow:1,
            }
        }
        mobbileGoodsList(obj).then((res)=>{
            if(res.code == 200&&res.data.list){
                this.goodsList = res.data.list;
            }else{
                this.$message({
                    message:res.msg,
                    type: 'error',
                    duration: 1500,
                })
            }
        })
        // this.getClassList(0);
        this.jnn();
        getTopicList().then((res)=>{
            this.topicList = res.data;
        });
    },
    methods: {
        //回显
        init(id){
            this.$nextTick(()=>{
                if(id){
                    this.floorId = id;
                    flooradveDetail({id:id}).then((res)=>{
                        console.log('楼层详情',res)
                        if(res.code == 200&&res.data){
                            // this.$nextTick(()=>{
                            //     this.$refs.nameIcon.cropper.imgShow  = true;
                            //     this.$refs.nameIcon.cropper.cropImg  = this.$imgDomain + res.data.nameIcon;
                            // })
                            Object.assign(this.dataForm,res.data);
                            this.dataForm.webFloorLinkConfigDTOList = res.data.webFloorLinkConfigDTOList;
                            this.startObj = cloneDeep(res.data);//深拷贝回显详情初始数据
                            if(this.startObj.classIds){
                                this.dataForm.classIds = res.data.classIds.split(",");
                                this.startObj.classIds = res.data.classIds.split(",");
                            }
                            this.transientData = cloneDeep(res.data.webFloorLinkConfigDTOList);
                            res.data.webFloorLinkConfigDTOList.map((item,index)=>{
                                if(item.classIds.length>0){
                                    this.transientData[index].classIds = item.classIds.split(',');
                                    this.dataForm.webFloorLinkConfigDTOList[index].classIds = item.classIds.split(',');
                                    console.log('6666',item.classIds)
                                }else{
                                    this.transientData[index].classIds = [];
                                }
                            })
                            this.dataForm.isShow = JSON.stringify(res.data.isShow);
                            this.picList = res.data.webFloorLinkConfigDTOList.map(item=>{
                                if(item.imgUrl){
                                    return this.$imgDomain + item.imgUrl
                                }else{
                                     return ''
                                }
                            })
                            this.editStyle=this.dataForm.floorStyle
                            this.editStyleList=this.dataForm.webFloorLinkConfigDTOList
                            this.picListCopy=this.picList;
                        }
                    })
                }
            })
        },
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
        changeFloor(){
            console.log("ssss",this.editStyleList)
            if(this.editStyle!=this.dataForm.floorStyle){
                this.dataForm.webFloorLinkConfigDTOList=[
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                ]
                this.transientData=[
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: ""},
                ]
                this.picList=[]
            }else{
                this.dataForm.webFloorLinkConfigDTOList=this.editStyleList
                this.transientData=this.editStyleList
                this.picList=this.picListCopy;

            }
        },
        handleChange(val){
            // console.log(val)
            this.dataForm.actionParams = val[val.length-1];
        },
        handleChangeOut(val){
            console.log(val)
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord = val[val.length-1];
        },
        //外部跳转类型
        toGetType(val){
            console.log('变化值',val)
            if(this.startObj.actionType!=val){
                this.dataForm.actionParams = '';
                this.dataForm.goodsClassName = '';
                this.dataForm.goodsName = '';
                this.dataForm.classIds = [];
            }else{
                this.dataForm.actionParams = this.startObj.actionParams;
                this.dataForm.goodsClassName = this.startObj.goodsClassName;
                this.dataForm.goodsName = this.startObj.goodsName;
                this.dataForm.classIds = this.startObj.classIds;
            }
        },
        //内部跳转类型
        toGetTypeChild(val){
            console.log('变化值',this.transientData[this.dataNum])
            if(this.transientData[this.dataNum].linkType!=val){
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord = '';
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].gcName = '';
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName = '';
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = [];
            }else{
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord = this.transientData[this.dataNum].typeKeyWord;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].gcName = this.transientData[this.dataNum].gcName;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName = this.transientData[this.dataNum].goodsName;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = this.transientData[this.dataNum].classIds;
            }
        },
        //外部商品详情
        getChange(val){
            this.goodsList.map(item=>{
                if(item.id == val){
                    console.log(item.goodsName)
                    this.dataForm.goodsName = item.goodsName;
                }
            })
        },
        // 内部商品详情
        getChangeChild(val){
            this.goodsList.map(item=>{
                if(item.id == val){
                    console.log(item.goodsName)
                    this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName = item.goodsName;
                }
            })
        },
        //点击展示内容
        changeClass(num){
            this.imgStatus = false;
            this.photoVisible = true;
            this.dataNum = num;
            // if(this.floorId){
            //     if(this.dataForm.webFloorLinkConfigDTOList[num].imgUrl){
            //         this.$nextTick(()=>{
            //             this.$refs.classImg.cropper.imgShow  = true;
            //             this.$refs.classImg.cropper.cropImg  = this.picList[num];
            //         })
            //     }
            // }else if(!this.floorId&&!this.dataForm.webFloorLinkConfigDTOList[num].imgUrl){
            //     this.$nextTick(()=>{
            //         this.$refs.classImg.cropper.imgShow  = false;
            //     })
            // }
        },
        // 取消关闭弹框
        handleClose(){
            this.photoVisible = false;
            this.imgStatus = false;
            if(!this.floorId){
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl = this.transientData[this.dataNum].imgUrl;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title = this.transientData[this.dataNum].title;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle = this.transientData[this.dataNum].subTitle;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType = this.transientData[this.dataNum].linkType;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = this.transientData[this.dataNum].classIds;
                if(this.transientData[this.dataNum].imgUrl){
                    this.picList[this.dataNum] = this.$imgDomain + this.transientData[this.dataNum].imgUrl;
                }else{
                    // this.$nextTick(()=>{
                    //     this.$refs.classImg.cropper.imgShow  = false;
                    // })
                    this.picList[this.dataNum] = '';
                }
                this.$refs['dataRuleOther'].resetFields();//校验隐藏
            }else{
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl = this.transientData[this.dataNum].imgUrl;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title = this.transientData[this.dataNum].title;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle = this.transientData[this.dataNum].subTitle;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType = this.transientData[this.dataNum].linkType;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = this.transientData[this.dataNum].classIds;
                if(this.transientData[this.dataNum].imgUrl){
                    this.picList[this.dataNum] = this.$imgDomain + this.transientData[this.dataNum].imgUrl;
                }else{
                    this.picList[this.dataNum] = ''
                }
            }
        },
        //确认关闭弹框
        startCheck(){
            this.imgStatus = true;
            if(!this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord){
                if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType == 'link'){
                    this.$message.error('链接地址不能为空')
                }
                if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType == 'searchGoodsByClass'){
                    this.$message.error('所属分类不能为空')
                }
                if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType == 'searchByKeyWord'){
                    this.$message.error('关键字不能为空')
                }
                if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType == 'goodsDetail'){
                    this.$message.error('商品名称不能为空')
                }
                return
            }
        if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl
                &&this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title&&this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType){
                this.$refs['dataRuleOther'].validate((valid) => {
                    if (valid) {
                        if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType== 'link'&&!isURL(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord)){
                            this.$message({
                                message:'链接地址格式有问题',
                                type: 'error',
                                duration: 1500,
                            })
                            return
                        }


                        console.log(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle.length)
                        this.transientData = cloneDeep(this.dataForm.webFloorLinkConfigDTOList);
                        this.photoVisible = false;
                    }
                })
            }
        },
        // 提交
        submitStore(){
            console.log('????',this.dataForm)
            if(this.picList.length == 6){
                this.dataForm.jiaoyan = 'url';
            }



            if(!this.dataForm.actionParams){
                if(this.dataForm.actionType == 'link'){
                    this.$message.error('链接地址不能为空')
                }
                if(this.dataForm.actionType == 'searchGoodsByClass'){
                    this.$message.error('所属分类不能为空')
                }
                if(this.dataForm.actionType == 'searchByKeyWord'){
                    this.$message.error('关键字不能为空')
                }
                return
            }

            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    if(this.dataForm.actionType == 'link'&&!isURL(this.dataForm.actionParams)){
                        this.$message({
                            message:'链接格式不正确',
                            type: 'error',
                            duration: 1500,
                        })
                        return
                    }
                    this.otherObj = cloneDeep(this.dataForm);//深拷贝提交数据
                    if(this.otherObj.classIds){
                        this.otherObj.classIds = this.otherObj.classIds.join(',');
                    }
                    this.otherObj.webFloorLinkConfigDTOList.map((item,index)=>{
                        if(item.classIds){
                            this.otherObj.webFloorLinkConfigDTOList[index].classIds = item.classIds.join(',');
                        }
                    })

                    this.otherObj.floorType=1
                    var numList=[];
                    this.otherObj.webFloorLinkConfigDTOList.map((item,index)=>{
                        if(item.imgMarking){
                            numList.push(item.imgMarking)
                        }
                    })
                    if(this.otherObj.floorStyle!=2){
                        if(numList.length<6){
                            this.$message.error('请完善展示内容')
                            return
                        }
                    }else{
                        if(numList.length<8){
                            this.$message.error('请完善展示内容')
                            return
                        }
                    }
                    this.buttonStatus = true;
                    if(this.floorId){
                        updateMobbileFloor(this.otherObj).then((res)=>{
                            if(res.code == 200){
                                this.$message({
                                    message:res.msg,
                                        type: 'success',
                                        duration: 1500,
                                })
                                this.changePage()
                            }else{
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                                // this.changePage()
                            }
                            this.buttonStatus = false;
                        })
                    }else{
                        addMobbileFloor(this.otherObj).then((res)=>{
                            if(res.code == 200){
                                this.$message({
                                    message:res.msg,
                                        type: 'success',
                                        duration: 1500,
                                })
                                this.changePage()
                            }else{
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                                // this.changePage()
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
            if(index == 'nameIcon'){
                that.dataForm.nameIcon = url;
            }else{
                that.dataForm.webFloorLinkConfigDTOList[index].imgUrl = url;
                that.dataForm.webFloorLinkConfigDTOList[index].imgMarking = JSON.stringify(index)
                that.picList[index] = that.$imgDomain + url;
            }
            // this.uploadPic(val,index);
        },
        // uploadPic(base64,index){
        //     let that = this;
        //     const params = { "imgStr": base64 };
        //     return new Promise(function(resolve){
        //         that.uploading = true;
        //         uploadPicBase64(params).then(res =>{
        //             that.uploading = false;
        //             if(res && res.code == 200&&res.data){
        //                 var url = res.data.url
        //                 if(index == 'nameIcon'){
        //                     that.dataForm.nameIcon = url;
        //                 }else{
        //                     that.dataForm.webFloorLinkConfigDTOList[index].imgUrl = url;
        //                     that.dataForm.webFloorLinkConfigDTOList[index].imgMarking = JSON.stringify(index)
        //                     that.picList[index] = that.$imgDomain + url;
        //                 }
        //                 resolve("true")
        //             }else {
        //                 if(that.floorId){
        //                     if(that.startObj.nameIcon){
        //                         that.$nextTick(()=>{
        //                             that.$refs.nameIcon.cropper.imgShow  = true
        //                             that.$refs.nameIcon.cropper.cropImg  = that.startObj.nameIcon;
        //                         })
        //                     }else{
        //                         that.$refs.nameIcon.cropper.imgShow  = false
        //                     }
        //                     if(that.startObj.webFloorLinkConfigDTOList[that.dataNum].imgUrl){
        //                         that.$nextTick(()=>{
        //                             that.$refs.classImg.cropper.imgShow  = true
        //                             that.$refs.classImg.cropper.cropImg  = that.startObj.webFloorLinkConfigDTOList[that.dataNum].imgUrl;
        //                         })
        //                     }else{
        //                         that.$refs.classImg.cropper.imgShow  = false
        //                     }
        //                 }else{
        //                     if(index == 'nameIcon'){
        //                         that.$refs.nameIcon.cropper.imgShow  = false
        //                     }else{
        //                         that.$refs.classImg.cropper.imgShow  = false
        //                     }
        //                 }
        //                 that.$message({
        //                     message:res.msg,
        //                     type: 'error',
        //                     duration: 1500,
        //                 })
        //                 resolve("false")
        //             }
        //         })
        //     });
        // },
        // 删除楼层名称图片
        deleteNameIcon(){
           this.dataForm.nameIcon = ""
        //    this.$set( this.dataForm,"nameIcon",this.dataForm.nameIcon);
        },
        //返回上级
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
    .homeFloor{
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
        .picMain{
            display: flex;
            flex-direction: column;
            div{
                display: flex;
            }
            .img1{
                object-fit: fill;
                width: 205px;
                height: 205px;
            }
            .img2{
                object-fit: fill;
                width: 100px;
                height:130px;
            }
             .img3{
                object-fit: fill;
                width: 205px;
                height: 100px;
            }
        }
    }

</style>
