<template>
  <div class="homeFloor">
    <Bread :breaddata="floorId?breaddata:breaddatas" @changePage="changePage" :index="'1'"></Bread>

    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="140px">
        <el-form-item prop="floorName" label="楼层名称：">
            <el-input v-model="dataForm.floorName" maxlength="4" clearable show-word-limit></el-input>
        </el-form-item>
        <el-form-item prop="jiaoyan" label="展示内容：">
            <el-input v-model="dataForm.jiaoyan" style="display:none" v-if="picList.length == 6" ></el-input>
            <div @change="changeFloor()">
                <el-radio v-model="dataForm.floorStyle" label="1" >样式一</el-radio>
                <el-radio v-model="dataForm.floorStyle" label="2" >样式二</el-radio>
                <el-radio v-model="dataForm.floorStyle" label="3" >样式三</el-radio>
            </div>
            <div class="picMain">
                <div style="margin-bottom: 10px">
                    <div v-if="dataForm.floorStyle!=1">
                        <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(0)" v-if="!this.dataForm.webFloorLinkConfigDTOList[0].imgUrl" alt="图标" style="margin-right:25px;" class="img1">
                        <img :src="this.dataForm.webFloorLinkConfigDTOList[0].imgUrl |filterImgUrl" alt="img" @click="changeGoodsOrPic(0)" v-if="this.dataForm.webFloorLinkConfigDTOList[0].imgUrl" style="margin-right:25px;" class="img1">
                    </div>
                     <div v-if="dataForm.floorStyle==1" style="display: flex;flex-direction: column;">
                        <div>
                            <img src="~@/assets/img/nopic-200.png" @click="changeGoodsOrPic(0)" v-if="!this.dataForm.webFloorLinkConfigDTOList[0].imgUrl" alt="图标" style="margin-right:25px;object-fit: fill;width: 406px;height: 130px;">
                            <img :src="this.dataForm.webFloorLinkConfigDTOList[0].imgUrl |filterImgUrl" alt="img" @click="changeGoodsOrPic(0)" v-if="this.dataForm.webFloorLinkConfigDTOList[0].imgUrl" style="margin-right:25px;object-fit: fill;width: 406px;height: 133px;">
                        </div>

                        <div style="margin-top:6px">
                             <img src="~@/assets/img/nopic-90.png" @click="changeGoodsOrPic(1)" v-if="!this.dataForm.webFloorLinkConfigDTOList[1].imgUrl" alt="图标" style="margin-right:25px;object-fit: fill;width: 406px;height: 62px;">
                            <img :src="this.dataForm.webFloorLinkConfigDTOList[1].imgUrl |filterImgUrl" alt="img" @click="changeGoodsOrPic(1)" v-if="this.dataForm.webFloorLinkConfigDTOList[1].imgUrl" style="margin-right:25px;object-fit: fill;width: 406px;height: 62px;">
                        </div>

                        <div style="margin-top:6px">
                            <img src="~@/assets/img/nopic-90.png" @click="changeGoodsOrPic(2)" v-if="!this.dataForm.webFloorLinkConfigDTOList[2].imgUrl" alt="图标" style="margin-right:25px;object-fit: fill;width: 406px;height: 62px;">
                            <img :src="this.dataForm.webFloorLinkConfigDTOList[2].imgUrl |filterImgUrl" alt="img" @click="changeGoodsOrPic(2)" v-if="this.dataForm.webFloorLinkConfigDTOList[2].imgUrl" style="margin-right:25px;object-fit: fill;width: 406px;height: 62px;">
                        </div>

                    </div>

                    <div  style="display: flex;flex-direction: column;margin-left: -21px;">
                        <div>
                            <div v-if="dataForm.floorStyle!=1">
                                <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(1)" v-if="!this.dataForm.webFloorLinkConfigDTOList[1].imgUrl" alt="图标" style="margin-right:25px;" class="img2">
                                <img :src="this.dataForm.webFloorLinkConfigDTOList[1].imgUrl |filterImgUrl" alt="img" @click="changeGoodsOrPic(1)" v-if="this.dataForm.webFloorLinkConfigDTOList[1].imgUrl" style="margin-right:25px;" class="img2">
                            </div>




                            <div :class="dataForm.floorStyle!=1?'floorClass':''">
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(3)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[3].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[3].imgUrl  | filterImgUrl" alt="img" @click="changeGoodsOrPic(3)" v-if="this.dataForm.webFloorLinkConfigDTOList[3].imgUrl" class="img2" style="margin-right:5px">

                                </div>
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(4)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[4].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[4].imgUrl | filterImgUrl" @click="changeGoodsOrPic(4)" v-if="this.dataForm.webFloorLinkConfigDTOList[4].imgUrl" class="img2" style="margin-right:5px">

                                </div>
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(5)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[5].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[5].imgUrl | filterImgUrl" @click="changeGoodsOrPic(5)" v-if="this.dataForm.webFloorLinkConfigDTOList[5].imgUrl" class="img2" style="margin-right:5px">
                                </div>
                            </div>
                        </div>

                        <div style="margin-top: 6px;">
                            <div v-if="dataForm.floorStyle!=1">
                                <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(2)" v-if="!this.dataForm.webFloorLinkConfigDTOList[2].imgUrl" alt="图标" style="margin-right:25px;" class="img2">
                                <img :src="this.dataForm.webFloorLinkConfigDTOList[2].imgUrl | filterImgUrl" alt="img" @click="changeGoodsOrPic(2)" v-if="this.dataForm.webFloorLinkConfigDTOList[2].imgUrl" style="margin-right:25px;" class="img2">
                            </div>



                            <div :class="dataForm.floorStyle!=1?'floorClass':''">
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(6)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[6].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[6].imgUrl | filterImgUrl" @click="changeGoodsOrPic(6)" v-if="this.dataForm.webFloorLinkConfigDTOList[6].imgUrl" class="img2" style="margin-right:5px">

                                </div>
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(7)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[7].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[7].imgUrl | filterImgUrl" @click="changeGoodsOrPic(7)" v-if="this.dataForm.webFloorLinkConfigDTOList[7].imgUrl" class="img2" style="margin-right:5px">

                                </div>
                                <div style="display: flex;flex-direction: column;">
                                    <img src="~@/assets/img/nopic.png" @click="changeGoodsOrPic(8)" alt="图标" v-if="!this.dataForm.webFloorLinkConfigDTOList[8].imgUrl" class="img2" style="margin-right:5px">
                                    <img :src="this.dataForm.webFloorLinkConfigDTOList[8].imgUrl | filterImgUrl" @click="changeGoodsOrPic(8)" v-if="this.dataForm.webFloorLinkConfigDTOList[8].imgUrl" class="img2" style="margin-right:5px">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                </div>
            </div>
        </el-form-item>

        <el-form-item prop="actionType" label="楼层名称跳转类型：">
            <el-radio-group v-model="dataForm.actionType" @change="toGetType">
                <el-radio-button label="link">链接</el-radio-button>
                <el-radio-button label="searchGoodsByClass">展示分类</el-radio-button>
                <el-radio-button label="searchByKeyWord">关键字</el-radio-button>
                <el-radio-button label="topic">专题页</el-radio-button>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="actionParams " label="链接地址：" v-if="dataForm.actionType == 'link'">
            <el-input v-model="dataForm.actionParams" placeholder="Http://xxxxxx.com" clearable></el-input>
            <div class="operationTips">仅支持输入http://开头的链接格式</div>
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
            <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput" ></el-input-number>
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
    :close-on-click-modal =  'true'
    :show-close = 'true'
    :before-close='before'
    :visible.sync="goodsOrPic"
     width="765px"
     class="goodsDialog"
    >
    <div :style="this.configStyle==1?'margin-left: -96px':'margin-left: -164px;'" v-show="showStyle" @change="stylechange">
        <span style="margin-left: 193px;">配置类型：</span>
        <el-radio v-model="configStyle" label="1" >图片</el-radio>
        <el-radio v-model="configStyle" label="2" >商品</el-radio>
    </div>
    <div v-if="this.configStyle==1" style="margin-left: 45px;">

        <el-form :model="dataForm1" :rules="dataRuleOther" ref="dataRuleOther" label-width="120px">

             <el-form-item prop="title" label="标题：" v-show="!showStyle" :class="!showStyle?'cc':''">
                 <div>
                     <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].title" placeholder="" maxlength="4" clearable></el-input>
                 </div>
            </el-form-item>

             <el-form-item prop="subTitle" label="副标题：" v-show="!showStyle">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].subTitle" placeholder="" maxlength="8" clearable></el-input>
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
                    </el-radio-group>
                </div>
                <span style="color:#f56c6c" v-if="!dataForm.webFloorLinkConfigDTOList[dataNum].linkType&&imgStatus">跳转类型不能为空</span>
            </el-form-item>
            <el-form-item prop="actionParams" label="链接地址：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'link'">
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" placeholder="Http://xxxxxx.com" clearable></el-input>
                <div class="operationTips">仅支持输入http://开头的链接格式</div>
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
                <el-input v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" maxlength="20" placeholder="" clearable></el-input>
            </el-form-item>
            <el-form-item prop="actionParams" label="商品名称：" v-if="dataForm.webFloorLinkConfigDTOList[dataNum].linkType == 'goodsDetail'">
                <el-select v-model="dataForm.webFloorLinkConfigDTOList[dataNum].typeKeyWord" filterable placeholder="请选择" @change="getChangeChild">
                    <el-option
                        v-for="item in goodsList"
                        :key="item.id"
                        :label="item.goodsName"
                        :value="item.id">
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
        <div style="text-align: center;margin-left: -45px;">
            <el-button @click="handleClose()">取 消</el-button>
            <el-button type="primary" @click="startCheck()">确 定</el-button>
        </div>
    </div>
    <div v-if="this.configStyle==2" style="margin-top: -26px;">
            <el-form :inline="true" :model="goodsDataForm">
                <el-form-item label="商品名称：" style="margin-left: 28px;">
                    <el-input  placeholder="商品名称" clearable v-model="goodsDataForm.goodsName" class="aaa" style="width: 103px important;"></el-input>
                </el-form-item>
                <el-form-item label="商品分类：">
                    <el-cascader
                            clearable=""
                            v-model="gcIds"
                            :options="goodscalssOption"
                            :props="props"
                            @change="finishCange()"
                    ></el-cascader>
                </el-form-item>
                 <el-button class="btn" type="primary" @click="getgoodsList(1)" style="margin-left: 20px">查询</el-button>
                <div class="flexLayout" v-loading="goodsLoading" style="margin-top: -13px;">
                    <div v-for="(item,index) in goodsOptions" :key="index" class="blueBlock">
                        <div :class="chooseFn(item) ? 'chooseContent' : 'content'" @click="chooseGoods(item)">
                            <div class="displayImg">
                                <div class="goodsImg">
                                    <img :src="item.pictureUrl | filterImgUrl" alt="" style="height:80px;width:80px" />
                                    <img src="~@/assets/images/hook.png" v-show="chooseFn(item)"  style="height:auto;width:80px;position: absolute;left: 0" />
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
                <div style="margin-right: 406px;">
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
                <el-form-item style="margin-left: 275px;margin-top: 26px;">
                    <el-button class="btn"  type="primary" plain @click="clearChooseObj">取消</el-button>
                    <el-button class="btn" type="primary" @click="closeGoods()">确定</el-button>
                </el-form-item>
            </el-form>
    </div>
  </el-dialog>
  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import cloneDeep from 'lodash/cloneDeep'
import { uploadPicBase64,flooradveDetail,updateMobbileFloor,addMobbileFloor,allTreeGoodsclass } from '@/api/api';
import { mobbileFloorCnki,mobbileGoodsList,floorclassList,getTopicList} from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";


export default {
    data () {
        return {
            goodsIdList:[],
            topicList:[],
            editStyle:'',
            editStyleList:[],
            goodsVisible:false,
            goodsinfo:[

            ],
            gcIds:[],
            goodsDataForm:{
                goodsName:'',
                gcIds:""
            },
            dataFormTemp: {
                relationIds:[],
            },
            showStyle:true,
            aa: {},
            configStyle:"1",
            urlFlag:"",
            goodsOrPic:false,
            page:1,limit:9,total:0,
            goodsLoading:true,
            goodsOptions:[],
            // 存返回的relationIds数据
            tempRelationIds:[],
            otherObj:'',
             props: {
                    value: "id",
                    label: "gcName"
                },
            dataForm: {
                floorStyle:'1',
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
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},

                ]
            },
            classDataList:[],
            props:{
                label:'gcName',
                value: 'id'
            },
            breaddata: ["运营管理", "PC首页楼层", '编辑楼层'],
            breaddatas: ["运营管理", "PC首页楼层", '新增楼层'],
            startObj:'',//回显的初始数据
            buttonStatus:false,
            imgStatus:false,
            photoVisible:false,
            picList:[],
            goodsList:[],
            uploading:false,
            floorId:null,
            dataNum:0,
            goodscalssOption:[],
            transientData:[
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},

                ],
            dataForm1:{// 伪验证
                title:'1212',
                linkType:'link',
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
        this.getallTreeGoodsclassFn();
        this.getgoodsList()
        this.jnn();
          getTopicList().then((res)=>{
            this.topicList = res.data;
        })
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
                            res.data.isShow = JSON.stringify(res.data.isShow);
                            res.data.floorStyle = JSON.stringify(res.data.floorStyle);
                            this.startObj = cloneDeep(res.data);//深拷贝回显详情初始数据
                            if(res.data.classIds.length>0){
                                res.data.classIds = res.data.classIds.split(",");
                            }else{
                                this.dataForm.classIds=[]
                                this.startObj.classIds =[]
                            }
                            res.data.webFloorLinkConfigDTOList.map((item,index)=>{
                                res.data.webFloorLinkConfigDTOList[index].configStyle=JSON.stringify(res.data.webFloorLinkConfigDTOList[index].configStyle);
                                if(item.classIds.length>0){
                                    res.data.webFloorLinkConfigDTOList[index].classIds = item.classIds.split(',');
                                    console.log('6666',item.classIds)
                                }else{
                                    res.data.webFloorLinkConfigDTOList[index].classIds =[]
                                }
                                if( res.data.webFloorLinkConfigDTOList[index].configStyle==1 &&  res.data.webFloorLinkConfigDTOList[index].linkType=="goodsDetail"){
                                    this.goodsIdList.push({id:res.data.webFloorLinkConfigDTOList[index].typeKeyWord,goodsName:res.data.webFloorLinkConfigDTOList[index].goodsName});
                                }
                            })
                            this.dataForm=res.data
                            this.dataForm.webFloorLinkConfigDTOList = res.data.webFloorLinkConfigDTOList;
                            this.transientData = cloneDeep(res.data.webFloorLinkConfigDTOList);
                            this.picList = res.data.webFloorLinkConfigDTOList.map(item=>{
                                return this.$imgDomain + item.imgUrl
                            })
                            this.editStyle=this.dataForm.floorStyle
                            this.editStyleList=this.dataForm.webFloorLinkConfigDTOList

                        }
                    })
                }
            })
        },
        //展示分类
          finishCange() {
            let len = this.gcIds.length;
                if (len != -1) {
                    this.goodsDataForm.gcIds = this.gcIds[this.gcIds.length - 1];
                }
            },
        jnn(){
            //商品列表
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
                    var flag0=true
                    var flag1=true
                    var flag2=true
                    //接盘侠注意： 这里查询的是所有上架的商品列表,但是编辑回显的时候，有的商品已经下架了就查询不出来了，这里做个判断，如果没有查询出当前关联的商品就手动放进去
                    this.goodsList.map((item,index)=>{
                        if(this.goodsIdList[0] && item.id==this.goodsIdList[0].id){
                            flag0=false
                        }
                        if(this.goodsIdList[1] && item.id==this.goodsIdList[1].id){
                            flag1=false
                        }
                        if(this.goodsIdList[2] && item.id==this.goodsIdList[2].id){
                            flag2=false
                        }
                    })
                    if(flag0 && this.goodsIdList[0]){
                        this.goodsList.push(this.goodsIdList[0])
                    }
                    if(flag1 && this.goodsIdList[1]){
                        this.goodsList.push(this.goodsIdList[1])
                    }
                    if(flag2 && this.goodsIdList[2]){
                        this.goodsList.push(this.goodsIdList[2])
                    }

                }else{
                    this.$message({
                        message:res.msg,
                        type: 'error',
                        duration: 1500,
                    })
                }
            })
            // 分类列表
            var params= {
                'showType':1
            }
            floorclassList(params).then((res)=>{
                console.log('展示分类',res)
                this.classDataList = res.data;
            })
        },
          getallTreeGoodsclassFn() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOption = res.data
                    }
                })
            },
        /* 选择指定商品*/
        chooseGoods(itemargu){
            this.transientData[this.dataNum].subTitle=''
            this.transientData[this.dataNum].title=''
            this.transientData[this.dataNum].linkType=''
            this.transientData[this.dataNum].typeKeyWord=''
            this.transientData[this.dataNum].configStyle='2'
            this.transientData[this.dataNum].imgMarking=this.dataNum
            this.transientData[this.dataNum].imgUrl=itemargu.pictureUrl
            this.transientData[this.dataNum].goodsName=itemargu.goodsName
            this.transientData[this.dataNum].typeKeyWord=itemargu.id
        },
        before(){
            if(this.configStyle==1){
                this.handleClose()
            }else{
                this.clearChooseObj()
            }
        },
        //指定商品列表
        getgoodsList(page){
            if(page){
                this.page=1
            }

            var obj={
                params:{
                    goodsShow:1,
                    page:this.page,
                    limit:9,
                    goodsName:this.goodsDataForm.goodsName,
                    gcId:this.goodsDataForm.gcIds

                }
            }
            if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].configStyle==2 && this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord && !page){
                obj.params.goodsId=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord

            }else{
                obj.params.limit=9
            }

            mobbileGoodsList(obj).then(res=>{
                if(res.code == 200){
                    this.goodsLoading = false
                    this.goodsOptions = res.data.list
                    this.total = res.data.total
                }
            })
        },
        chooseFn(itemargu){
            if(this.transientData[this.dataNum].configStyle==2){
                if(this.transientData[this.dataNum].typeKeyWord&&this.transientData[this.dataNum].typeKeyWord==itemargu.id){
                    return  true;
                }
            }
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
            if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName){
                this.transientData[this.dataNum].subTitle=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle
                this.transientData[this.dataNum].title=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title
                this.transientData[this.dataNum].linkType=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType
                this.transientData[this.dataNum].typeKeyWord=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord
                this.transientData[this.dataNum].configStyle=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].configStyle
                this.transientData[this.dataNum].imgMarking=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgMarking
                this.transientData[this.dataNum].imgUrl=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl
                this.transientData[this.dataNum].goodsName=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName
            }else{
                this.transientData[this.dataNum].subTitle=''
                this.transientData[this.dataNum].title=''
                this.transientData[this.dataNum].linkType=''
                this.transientData[this.dataNum].typeKeyWord=''
                this.transientData[this.dataNum].configStyle='2'
                this.transientData[this.dataNum].imgMarking=''
                this.transientData[this.dataNum].imgUrl=''
                this.transientData[this.dataNum].goodsName=''
            }
            this.goodsOrPic = false
        },
        closeGoods(){
            if(!this.transientData[this.dataNum].goodsName){
                this.$message.error('请选择关联商品')
                return
            }
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum]=cloneDeep(this.transientData[this.dataNum])
            this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType="goodsDetail"

            this.goodsOrPic=false
        },
        stylechange(){
            this.page=1
            if(this.configStyle==1 && this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl && this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName && this.urlFlag==''){//商品切换图片
                this.urlFlag=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl=''
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum]=this.aa
            }
            if(this.configStyle==2&&this.urlFlag){
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl=this.urlFlag
            }
            if(this.configStyle==2 && !this.urlFlag){
                this.urlFlag=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl
            }
        },
        changeGoodsOrPic(int){
            this.dataNum = int;
            this.goodsDataForm.goodsName='',
            this.goodsDataForm.gcIds='',
            this.page=1
            this.limit=9
            this.getgoodsList();

            this.urlFlag=''

            this.goodsDataForm.goodsName='';
            this.goodsDataForm.gcIds='';
            this.gcIds=[];
            if(this.dataForm.webFloorLinkConfigDTOList[this.dataNum].configStyle){
                this.configStyle=this.dataForm.webFloorLinkConfigDTOList[this.dataNum].configStyle
            }else{
                this.configStyle='1'
            }

            if(this.dataForm.floorStyle!=3 &&( int ==0||int ==1 || int ==2 )){
                this.showStyle=false
                this.configStyle='1'
            }else if(this.dataForm.floorStyle==3 && int ==0){
                this.showStyle=false
                this.configStyle='1'
            }else{
                this.showStyle=true
            }
            this.goodsOrPic = true;
            this.aa=this.dataForm.webFloorLinkConfigDTOList[this.dataNum]
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
        changeFloor(){
            if(this.editStyle!=this.dataForm.floorStyle){
                this.dataForm.webFloorLinkConfigDTOList=[
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                ]
                this.transientData=[
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},
                    {classIds:[],gcName: "",imgMarking: "",imgUrl: "",linkType: "link",subTitle: "",title: "",typeKeyWord: "",configStyle:"",goodsName:""},

                ]
            }else{
                this.dataForm.webFloorLinkConfigDTOList=this.editStyleList
                this.transientData=this.editStyleList

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
        // 内部商品详情
        getChangeChild(val){
            this.goodsList.map(item=>{
                if(item.id == val){
                    console.log(item.goodsName)
                    this.dataForm.webFloorLinkConfigDTOList[this.dataNum].goodsName = item.goodsName;
                }
            })
        },

        // 取消关闭弹框
        handleClose(){
            this.goodsOrPic = false;
            if(!this.floorId){
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl = this.transientData[this.dataNum].imgUrl;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].title = this.transientData[this.dataNum].title;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].subTitle = this.transientData[this.dataNum].subTitle;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType = this.transientData[this.dataNum].linkType;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].classIds = this.transientData[this.dataNum].classIds;
                this.dataForm.webFloorLinkConfigDTOList[this.dataNum].typeKeyWord = this.transientData[this.dataNum].typeKeyWord;
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
            if(!this.dataForm.webFloorLinkConfigDTOList[this.dataNum].linkType){
                 this.$message.error('请选择跳转类型')
                 return
            }
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
            if(!this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgUrl){
                 this.$message.error('图片不能为空')
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
                        this.dataForm.webFloorLinkConfigDTOList[this.dataNum].imgMarking=this.dataNum
                        this.dataForm.webFloorLinkConfigDTOList[this.dataNum].configStyle='1'
                        this.transientData = cloneDeep(this.dataForm.webFloorLinkConfigDTOList);
                        this.goodsOrPic = false;
                    }
                })
            }
        },
        // 提交
        submitStore(){
            var numList=[]
            this.dataForm.webFloorLinkConfigDTOList.map((item,index)=>{
                if(item.imgMarking!=''){
                    numList.push(item.imgMarking)
                }
            })
            console.log("111",numList)
            if(numList.length<8){
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
                    this.otherObj.floorType=2
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
        /deep/ .el-input{
            width: 213px !important;
        }
        /deep/ .aaa{
            width: 143px !important;
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
           /deep/ .el-input{
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
                width: 260px;
                height: 266px;
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
            width: 80%;
            flex-wrap: wrap;
            margin: 0 26px;
            .blueBlock{
                height: 112px;
                width: 240px;
                margin-left: -13px;
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
.floorClass{
    margin-left: -21px;
}
.cc{
    margin-top: -62px;
}

</style>
