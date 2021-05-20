<template>
  <div class="homeFloor">
    <Bread :breaddata="floorId?breaddata:breaddatas" @changePage="changePage" :index="'1'"></Bread>

    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="140px">
        <el-form-item prop="floorName" label="楼层名称：">
            <el-input v-model="dataForm.floorName" maxlength="4" clearable show-word-limit></el-input>
        </el-form-item>
        <el-form-item prop="jiaoyan" label="展示内容：">
            <el-input v-model="dataForm.jiaoyan" style="display:none" v-if="picList.length == 6" ></el-input>
            <div class="picMain">
                <div style="margin-bottom: 10px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(0)" v-if="!picList[0]" alt="图标" style="margin-right:30px;" class="img1">
                    <img :src="picList[0]" alt="img" @click="changeClass(0)" v-if="picList[0]" style="margin-right:30px" class="img1">

                </div>
                <div>
                    <div style="display: flex;flex-direction: column;">
                        <img src="~@/assets/img/nopic.png" @click="changeType(1)" alt="图标" v-if="!picList[1]" class="img2" style="margin-right:10px">
                        <img :src="picList[1]" alt="img" @click="changeType(1)" v-if="picList[1]" class="img2" style="margin-right:10px">
     
                    </div>

                    <div style="display: flex;flex-direction: column;">
                        <img src="~@/assets/img/nopic.png" @click="changeType(2)" alt="图标" v-if="!picList[2]" class="img2" style="margin-right:10px">
                        <img :src="picList[2]" @click="changeType(2)" v-if="picList[2]" class="img2" style="margin-right:10px">

                    </div>

                    <div style="display: flex;flex-direction: column;">
                        <img src="~@/assets/img/nopic.png" @click="changeType(3)" alt="图标" v-if="!picList[3]" class="img2" style="margin-right:10px">
                        <img :src="picList[3]" @click="changeType(3)" v-if="picList[3]" class="img2" style="margin-right:10px">

                    </div>

                </div>
                <div style="margin-top: 10px;">
                    <div style="display: flex;flex-direction: column;">
                          <img src="~@/assets/img/nopic.png" @click="changeType(4)" alt="图标" v-if="!picList[4]" class="img2" style="margin-right:10px">
                         <img :src="picList[4]" @click="changeType(4)" v-if="picList[4]" class="img2" style="margin-right:10px">

                    </div>

                    <div style="display: flex;flex-direction: column;">
                        <img src="~@/assets/img/nopic.png" @click="changeType(5)" alt="图标" v-if="!picList[5]" class="img2" style="margin-right:10px">
                        <img :src="picList[5]" @click="changeType(5)" v-if="picList[5]" class="img2" style="margin-right:10px">

                    </div>

                    <div style="display: flex;flex-direction: column;">
                        <img src="~@/assets/img/nopic.png" @click="changeType(6)" alt="图标" v-if="!picList[6]" class="img2" style="margin-right:10px">
                        <img :src="picList[6]" @click="changeType(6)" v-if="picList[6]" class="img2" style="margin-right:10px">

                    </div>

                </div>
            </div>
        </el-form-item>

        <el-form-item prop="actionType" label="查看更多跳转类型：">
            <el-radio-group v-model="dataForm.actionType" @change="toGetType">
                <el-radio-button label="link">链接</el-radio-button>
                <el-radio-button label="searchGoodsByClass">店铺分类</el-radio-button>
                <el-radio-button label="searchByKeyWord">关键字</el-radio-button>

            </el-radio-group>
        </el-form-item>
        <el-form-item prop="actionParams " label="链接地址：" v-if="dataForm.actionType == 'link'">
            <el-input v-model="dataForm.actionParams" placeholder="Http://xxxxxx.com" clearable></el-input>
            <div class="operationTips">仅支持输入Http://开头的链接格式</div>
        </el-form-item>
        <el-form-item prop="actionParams " label="所属分类：" v-if="dataForm.actionType == 'searchGoodsByClass'">
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
            <el-input v-model="dataForm.actionParams" maxlength="20" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item prop="isShow" label="是否显示：">
            <el-radio-group v-model="dataForm.isShow">
                <el-radio label="1">显示</el-radio>
                <el-radio label="0">隐藏</el-radio>
            </el-radio-group>
            <div class="operationTips">默认勾选显示该楼层，勾选为隐藏则不显示该楼层</div>
        </el-form-item>
        <el-form-item prop="sort" label="优先级排序：">
            <el-input-number v-model="dataForm.sort" onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number>
            <div class="operationTips">支持输入0-255内的整数，数值越小排序越靠前</div>
        </el-form-item>
        <div class="btnSub">
            <el-button @click="changePage">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitStore()" :loading="buttonStatus">{{ $t('confirm') }}</el-button>
        </div>
    </el-form>

    <!-- 图片弹框 -->
    <el-dialog
        title="广告类型配置"
        :visible.sync="photoVisible"
        :close-on-click-modal = "false"
        :show-close = "false"
        width="44%">
        <el-form :model="dataForm1" :rules="dataRuleOther" ref="dataRuleOther" label-width="120px">
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
                        <el-radio-button label="searchGoodsByClass">店铺分类</el-radio-button>
                        <el-radio-button label="searchByKeyWord">关键字</el-radio-button>
                        <el-radio-button label="goodsDetail">商品详情</el-radio-button>
                    </el-radio-group>
                </div>
                <span style="color:#f56c6c" v-if="!dataForm.webFloorLinkConfigDTOList[dataNum].linkType&&imgStatus">跳转类型不能为空</span>
            </el-form-item>
            <el-form-item prop="actionParams" label="链接地址：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'link'">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" placeholder="Http://xxxxxx.com" clearable></el-input>
                <div class="operationTips">仅支持输入Http://开头的链接格式</div>
            </el-form-item>
            <el-form-item prop="actionParams" label="所属分类：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'searchGoodsByClass'">
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
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" placeholder="" clearable></el-input>
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
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center;">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="startCheck">确 定</el-button>
        </div>
    </el-dialog>
        <el-dialog
                title="选择指定商品"
                :close-on-click-modal = "true"
                :show-close = "true"
                :before-close='clearChooseObj'
                :visible.sync="goodsVisible"
                width="65%"
                class="goodsDialog">
            <el-form :inline="true" :model="goodsDataForm" style="width:100%;">
                <el-form-item label="输入搜索：" style="margin-left: 28px; margin-top: -44px;"    >
                    <el-input  placeholder="商品名称\商品货号" clearable v-model="goodsDataForm.goodsName"></el-input>
                    <el-button class="btn" type="primary" @click="getgoodsList()" style="margin-left: 20px">查询</el-button>
                </el-form-item>
                <div class="flexLayout" v-loading="goodsLoading">
                    <div v-for="(item,index) in goodsOptions" :key="index" class="blueBlock">
                        <div :class="chooseFn(item) ? 'chooseContent' : 'content'" @click="chooseGoods(item)">
                            <div class="displayImg">
                                <div class="goodsImg">
                                    <img :src="item.pictureUrl | filterImgUrl" alt="" style="height:80px;width:80px" />
                                    <img src="@/assets/images/hook.png" v-show="chooseFn(item)"  style="height:auto;width:80px;position: absolute;left: 0" />
                                </div>
                                <div class="goodsName">
                                    <div class="goodsTitle" :title="item.goodsName">
                                        {{item.goodsName}}
                                    </div>
                                    <div class="goodsPrice">
                                        ￥ {{item.specSellPrice}}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 分页 -->
                <div style=" width: 90%;">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="page"
                            :page-sizes="[10, 20, 50, 100]"
                            :page-size="limit"
                            :total="total"
                            layout="total, prev, pager, next, jumper">
                    </el-pagination>
                </div>
            
            </el-form>
                <div style="text-align: center;">
                    <el-button class="btn"  type="primary" plain @click="clearChooseObj">取消</el-button>
                    <el-button class="btn" type="primary" @click="goodsVisible = false">确定</el-button>
                </div>
        </el-dialog>
  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import cloneDeep from 'lodash/cloneDeep'
import { uploadPicBase64,flooradveDetail,updateMobbileFloor,addMobbileFloor,getGoodsPage } from '@/api/api';
import { mobbileFloorCnki,mobbileGoodsList,storeGoodsclass } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";


export default {
    data () {
        return {
            cacheObj:{classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
            goodsVisible:false,
            goodsDataForm:{
                goodsName:''
            },
            dataFormTemp: {
                relationIds:[],
            },
            page:1,limit:12,total:0,
            goodsLoading:true,
            goodsOptions:[],
            // 存返回的relationIds数据
            tempRelationIds:[],
            otherObj:'',
            dataForm: {
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
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                ]
            },
            classDataList:[],
            props:{
                label:'gcName',
                value: 'id'
            },
            breaddata: ["运营管理", "h5楼层管理", '编辑楼层'],
            breaddatas: ["运营管理", "h5楼层管理", '新增楼层'],
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
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                ],
            dataForm1:{// 伪验证
                title:'1212',
                linkType:'212',
                classImg:'2121'
            },
            dataRuleOther:{
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
                ],

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
        this.jnn();
     
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
                            // {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
                     
                            Object.assign(this.dataForm,res.data);
                            this.dataForm.webFloorLinkConfigDTOList = res.data.webFloorLinkConfigDTOList;
                            this.startObj = cloneDeep(res.data);//深拷贝回显详情初始数据
                            if(this.startObj.classIds){
                                this.dataForm.classIds = res.data.classIds.split(",");
                                this.startObj.classIds = res.data.classIds.split(",");
                            }
                            this.transientData = cloneDeep(res.data.webFloorLinkConfigDTOList);
                            res.data.webFloorLinkConfigDTOList.map((item,index)=>{
                                if(item.classIds){
                                    this.transientData[index].classIds = item.classIds.split(',');
                                    this.dataForm.webFloorLinkConfigDTOList[index].classIds = item.classIds.split(',');
                                    console.log('6666',item.classIds)
                                }else{
                                    this.transientData[index].classIds = [];
                                }
                            })
                            this.dataForm.isShow = JSON.stringify(res.data.isShow);
                            this.picList = res.data.webFloorLinkConfigDTOList.map(item=>{
                                return this.$imgDomain + item.imgUrl
                            })
                        }
                    })
                }
            })
        },
        /* 选择指定商品*/
        chooseGoods(itemargu){
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName=itemargu.goodsName
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl=itemargu.pictureUrl
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord=itemargu.id
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgMarking=this.dataNum
            this.picList[this.dataNum]=this.$imgDomain + itemargu.pictureUrl
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType='goodsDetail'
        },
        chooseFn(itemargu){
            if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum]&&this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord==itemargu.id){
                return true
            }
        },
        //展示分类
        jnn(){
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
        //指定商品列表
        getgoodsList(){
            var obj={
                params:{
                    goodsShow:1,
                    page:this.page,
                    limit:this.limit,
                    goodsName:this.goodsDataForm.goodsName
                }
            }
            getGoodsPage(obj).then(res=>{
                if(res.code == 200){
                    this.goodsLoading = false
                    this.goodsOptions = res.data.list
                    this.total = res.data.total
                }
            })
        },
            /*指定商品分页*/
        handleSizeChange(val) {
            this.limit = val
            this.getgoodsList()
        },
        handleCurrentChange(val) {
            this.page = val
            this.getgoodsList()
        },
        clearChooseObj(){
            this.goodsVisible = false
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum]=this.cacheObj
            if(this.cacheObj.imgUrl){
                this.picList[this.dataNum]=this.$imgDomain + this.cacheObj.imgUrl
            }else{
                 this.picList[this.dataNum]=''
            }
            
        },
         changeType(val){
            this.dataNum=val
            this.goodsDataForm.goodsName=''
            this.page=1
            this.getgoodsList()
           
            this.goodsVisible = true;
            this.cacheObj=cloneDeep(this.dataForm.webFloorLinkConfigDTOList[this.dataNum])
          
        },
        // 清空所有的tag选项
        cancelAll(){
            this.brandOptions=this.brandOptions.concat(this.dataArray);
            this.dataArray = []
            this.brandDataForm.brandName = ''

        },
        handleChange(val){
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

                    this.picList[this.dataNum] = '';
                }
                this.$refs['dataRuleOther'].resetFields();//校验隐藏
            }else{
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl = this.transientData[this.dataNum].imgUrl;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title = this.transientData[this.dataNum].title;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle = this.transientData[this.dataNum].subTitle;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType = this.transientData[this.dataNum].linkType;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = this.transientData[this.dataNum].classIds;
                this.picList[this.dataNum] = this.$imgDomain + this.transientData[this.dataNum].imgUrl;
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
                &&this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType){
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
                        this.transientData = cloneDeep(this.dataForm.webFloorLinkConfigDTOList);
                        this.photoVisible = false;
                    }
                })
            }
        },
        // 提交
        submitStore(){
            console.log('????',this.dataForm)
            // {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "",subTitle: "",title: "",typeKeyWord: ""},
            var numList=[]
            this.dataForm.webFloorLinkConfigDTOList.map((item,index)=>{
                if(item.linkType){
                    numList.push(item.linkType)
                }
            })
            if(numList.length<7){
                 this.$message.error('请完善展示内容')
                return
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
        },


        // 删除楼层名称图片
        deleteNameIcon(){
           this.dataForm.nameIcon = ""
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
    .goodsnamespan{
        display: flex;
        width: 137px;
        height: 34px;
        line-height: 13pt;
        font-size: 17px;
        margin-top: 10px;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
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
                width: 430px;
                height: 430px;
            }
            .img2{
                object-fit: fill;
                width: 137px;
                height:130px;
            }
        }
    }
       // 指定商品
    .goodsDialog{
        .flexLayout{
            display: flex;
            width: 98%;
            flex-wrap: wrap;
            margin: 0 15px;
            .blueBlock{
                width: 33%;
                .content{
                    border: 1px solid #cccccc;
                    min-height: 100px;
                    margin: 12px;
                }
                .chooseContent{
                    border: 1px solid #3f51af;
                    min-height: 100px;
                    margin: 12px;
                }
                .displayImg{
                    display: flex;
                    margin: 10px;
                    .goodsImg{
                        position: relative;
                        width: 80px;
                        height: 80px;
                        display: inline-block;
                        margin-right: 10px;
                    }
                    .goodsName{
                        display: inline-block;
                        .goodsTitle{
                            width: 100%;
                            line-height: 22px;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            display: -webkit-box;
                            -webkit-line-clamp: 2;
                            line-clamp: 2;
                            -webkit-box-orient: vertical;
                        }
                        .goodsPrice{
                            margin-top: 5px;
                        }
                    }
                }
            }
        }
    }


</style>
