<template>
  <div class="adverClass">
    <Bread :breaddata="advId?breaddata:breaddatas" @changePage="changePage" :index="'2'"></Bread>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="130px">
        <el-form-item prop="relationId" label="展示分类名称：">
            <el-select v-model="dataForm.relationId" clearable :disabled="advId?true:false"  placeholder="请选择">
                <el-option
                    v-for="item in slectList"
                    :key="item.id"
                    :label="item.gcName"
                    :disabled="idList.indexOf(item.id)!=-1"
                    :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item  prop="advTitle" label="广告标题：" class="aa">
            <el-input style="width: 167%;"  v-model="dataForm.advTitle" placeholder="" maxlength="20" show-word-limit clearable></el-input>
        </el-form-item>
         <el-form-item prop="imageUrl" label="图片："  v-if="dataForm.showType==0 ">
             <div v-loading="uploading" style="width: 100px">
                <img-cropper
                    ref="imageUrl"
                    :index="'1'"
                    :imgWidth='"100px"'
                    :imgHeight='"100px"'
                    :cropImg="dataForm.imageUrl"
                    @GiftUrlHandle="GiftUrlHandle">
                </img-cropper>
             </div>
             <div class="operationTips">建议上传高于490x190像素的白底图片</div>
        </el-form-item>

         <el-form-item prop="imageUrl" label="图片：" v-if="dataForm.showType==1 ">
            <el-input v-model="dataForm.imageUrl" style="display:none" v-if="picList.length == 6" ></el-input>
            <div class="picMain">
                <div style="margin-bottom: 10px;">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(0)" v-if="!dataForm.advLinkConfigDTOList[0].imgUrl" alt="图标" style="margin-right:30px;" class="img1">
                    <img :src="dataForm.advLinkConfigDTOList[0].imgUrl | filterImgUrl" alt="img" @click="changeClass(0)" v-if="dataForm.advLinkConfigDTOList[0].imgUrl" style="margin-right:30px" class="img1">
                </div>

                <div>
                    <img src="~@/assets/img/nopic.png" @click="changeClass(1)" alt="图标" v-if="!dataForm.advLinkConfigDTOList[1].imgUrl" class="img2" style="margin-right:10px">
                    <img :src="dataForm.advLinkConfigDTOList[1].imgUrl | filterImgUrl" alt="img" @click="changeClass(1)" v-if="dataForm.advLinkConfigDTOList[1].imgUrl" class="img2" style="margin-right:10px">

                    <img src="~@/assets/img/nopic.png" @click="changeClass(2)" alt="图标" v-if="!dataForm.advLinkConfigDTOList[2].imgUrl" class="img2" style="margin-right:10px">
                    <img :src="dataForm.advLinkConfigDTOList[2].imgUrl | filterImgUrl" alt="img" @click="changeClass(2)" v-if="dataForm.advLinkConfigDTOList[2].imgUrl" class="img2" style="margin-right:10px">
                </div>

                <div style="margin-top: 10px;">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(3)" alt="图标" v-if="!dataForm.advLinkConfigDTOList[3].imgUrl" class="img2" style="margin-right:10px">
                    <img :src="dataForm.advLinkConfigDTOList[3].imgUrl | filterImgUrl" alt="img" @click="changeClass(3)" v-if="dataForm.advLinkConfigDTOList[3].imgUrl" class="img2" style="margin-right:10px">
                    <img src="~@/assets/img/nopic.png" @click="changeClass(4)" alt="图标" v-if="!dataForm.advLinkConfigDTOList[4].imgUrl" class="img2">
                    <img :src="dataForm.advLinkConfigDTOList[4].imgUrl | filterImgUrl" alt="img" @click="changeClass(4)" v-if="dataForm.advLinkConfigDTOList[4].imgUrl" class="img2">
                </div>

            </div>
            <div class="operationTips">此处需要依次点进去并需完成全部展示内容编辑</div>
        </el-form-item>
        <el-form-item label="广告链接（URL）：" v-if="dataForm.showType==0">
            <el-input v-model="dataForm.relationTarget" placeholder="Http://xxxxxx.com" clearable @blur="urlChange"></el-input>
            <div class="operationTips">仅支持输入Http://开头的链接格式</div>
        </el-form-item>
        <el-form-item prop="radio" label="启用时间：">
            <el-radio v-model="dataForm.radio" label="1">立即启用</el-radio>
            <el-radio v-model="dataForm.radio" label="2" style="margin-right:30px">定时启用</el-radio>
            <el-date-picker
                v-if="timeStatus"
                v-model="dataForm.startDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                clearable
                placeholder="选择启用时间">
            </el-date-picker>
        </el-form-item>
        <el-form-item label="停用时间：">
            <el-date-picker
                v-model="dataForm.endDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                clearable
                placeholder="选择停用时间">
            </el-date-picker>
            <div class="operationTips">默认勾选立即启用，勾选为定时启用则将弹出时间框，需继续完成设置时间</div>
        </el-form-item>
        <el-form-item prop="sort" label="排序：">
            <el-input v-model="dataForm.sort" controls-position="right"  :min="0" :max="255" type="number"></el-input>
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
        width="600px">
        <el-form :model="dataForm1" :rules="dataRuleOther" ref="dataRuleOther" label-width="120px" style="margin-top: -38px;">
            <el-form-item prop="classImg" label="上传图片：">
                <el-input v-model="dataForm1.classImg" style="display:none" v-if="photoVisible&&dataForm.advLinkConfigDTOList[dataNum].imgUrl"></el-input>
                <img-cropper
                    ref="classImg"
                    :index="dataNum"
                    :imgWidth='"100px"'
                    :imgHeight='"100px"'
                    :cropImg='dataForm.advLinkConfigDTOList[dataNum].imgUrl'
                    @GiftUrlHandle="GiftUrlHandle2">
                </img-cropper>
                <span style="color:#f56c6c" v-if="photoVisible&&!dataForm.advLinkConfigDTOList[dataNum].imgUrl&&imgStatus"></span>
                <div class="operationTips">建议上传高于250x150像素的白底图片</div>
            </el-form-item>
            <el-form-item prop="actionParams" label="链接地址：" v-if="photoVisible">
                <el-input v-model="dataForm.advLinkConfigDTOList[dataNum].typeKeyWord" placeholder="Http://xxxxxx.com" clearable></el-input>
                <div class="operationTips">仅支持输入Http://开头的链接格式</div>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center;">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="startCheck">确 定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import cloneDeep from 'lodash/cloneDeep'
import { floorClassAdveAdd,plainAdveUpdate } from '@/api/api'
import { uploadPicBase64,adverDetail,floorclassList,pcClassId } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";

export default {
    data () {
        return {
            timeStatus:false,
            idList:[],
            dataForm: {
                showType:'1',
                advTitle: "",
                relationId: '',
                endDate: "",
                imageUrl: "",
                relationTarget: "",
                sort: 0,
                startDate: this.parseTime(new Date()),
                radio:'1',
                advType: 2,
                advLinkConfigDTOList: [
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                ]
            },
            dataForm1:{
                 actionParams:'',
                classImg:""
            },
            photoVisible:false,
            imgStatus:false,
            dataNum:0,
            breaddata: ["运营管理", "广告管理", "PC分类广告列表",'编辑'],
            breaddatas: ["运营管理", "广告管理", "PC分类广告列表",'新增'],
            buttonStatus:false,
            subStatus:true,
            picList:[],
            uploading:false,
            advId:null,
            slectList:[],
            transientData:[
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
                    {imgMarking: "",imgUrl: "",linkType: "",typeKeyWord: ""},
            ],
            dataRuleOther:{
                classImg :[
                    { required: true, message: '上传图片不能为空', trigger: 'blur' }
                ],
            },

        }
    },
    computed: {
          dataRule () {
                return {
                    relationId: [
                        { required: true, message: '展示分类类别不能为空', trigger: 'blur' }
                    ],
                    advTitle: [
                        { required: true, message: '广告标题不能为空！', trigger: 'blur' }
                    ],
                    imageUrl: [
                        { required: true, message: '图片不能为空', trigger: 'blur' }
                    ],

                    sort: [
                        { required: true, message: '排序不能为空！', trigger: 'blur' }
                    ],
                    radio: [
                        { required: true, message: '启用时间不能为空！', trigger: 'blur' }
                    ]
                }
            }
    },
    watch:{
        'dataForm.radio'(val, oldVal){//普通的watch监听
            if(val=='2'){
                this.timeStatus = true;
            }else{
                this.timeStatus = false;
                this.dataForm.startDate = this.parseTime(new Date());
            }
        },
    },
    components:{
        imgCropper,
        Bread
    },
    created(){

    },
    methods: {
        GiftUrlHandle2(imgUrl,index){
            let that = this;
             let url = imgUrl;
            if(index == 'nameIcon'){
                that.dataForm.nameIcon = url;
            }else{
                that.dataForm.advLinkConfigDTOList[index].imgUrl = url;
                that.dataForm.advLinkConfigDTOList[index].imgMarking = JSON.stringify(index)
                that.picList[index] = that.$imgDomain + url;
            }
        },

          //确认关闭弹框
        startCheck(){

            this.imgStatus = true;
            this.$refs['dataRuleOther'].resetFields();//校验隐藏
            if(this.dataForm.advLinkConfigDTOList[this.dataNum].typeKeyWord&&!isURL(this.dataForm.advLinkConfigDTOList[this.dataNum].typeKeyWord)){
                this.$message({
                    message:'链接地址格式有问题',
                    type: 'error',
                    duration: 1500,
                })
                return
            }
            if(!this.dataForm.advLinkConfigDTOList[this.dataNum].imgUrl){
                this.$message({
                    message:'图片不能为空',
                    type: 'error',
                    duration: 1500,
                })
                return
            }else{
                 this.dataForm.imageUrl=this.dataForm.advLinkConfigDTOList[this.dataNum].imgUrl
            }


            this.transientData = cloneDeep(this.dataForm.advLinkConfigDTOList);
            this.photoVisible = false;

            if(this.dataForm.advLinkConfigDTOList[this.dataNum].typeKeyWord){
                // 目前只有连接，先写死
                 this.dataForm.advLinkConfigDTOList[this.dataNum].linkType='link'
            }

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

        },
        parseTime(d) {
            let year = d.getFullYear();
            let mon = d.getMonth()+1;
            let day = d.getDate();
            let hour = d.getHours();
            let minutes = d.getMinutes() ;
            let seconds = d.getSeconds();
            let s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day)+' '+(hour<10?('0'+hour):hour) + ":"+ (minutes<10?('0'+minutes):minutes) +":"+(seconds<10?('0'+seconds):seconds);
            return s
        },
        init(row){
            pcClassId().then((res)=>{
                if(res.code==200){
                    this.idList=res.data
                }
            })
            this.$nextTick(()=>{
                if(row){
                    this.dataForm.showType=row.type
                    this.advId = row.id;
                    var obj  = {
                        id:row.id
                    }
                    adverDetail(obj).then((res)=>{
                        console.log('回显',res.data)
                        if(res.code == 200){
                            this.startObj = cloneDeep(res.data);//深拷贝回显详情初始数据
                            Object.assign(this.dataForm,res.data);
                            if(res.data.startDate){
                                this.dataForm.radio = '2';
                                this.dataForm.startDate = res.data.startDate;
                            }
                        }
                    })
                }
                this.advType()
            })

        },
        advType(){
            var params={
                'showType':this.dataForm.showType
            }
            floorclassList(params).then((res)=>{
                    if(res.code == 200&&res.data){
                        this.slectList = res.data
                    }
            })

            this.dataForm.relationId=''
        },
        urlChange(e){
            if(e.target.value&&!isURL(e.target.value)){
                this.subStatus = false;
                this.$message({
                    message:'链接格式不正确',
                    type: 'error',
                    duration: 1500,
                })
            }else{
                this.subStatus = true;
            }
        },
        // 提交
        submitStore(){
            // this.dataForm.advLinkConfigDTOList = this.dataForm.advLinkConfigDTOList.filter(t => t.imgUrl)
            if(this.dataForm.showType==1&&this.dataForm.advLinkConfigDTOList[4].imgUrl==''){
                this.$message({
                    message:'图片不足5个',
                    type: 'error',
                    duration: 1500,
                })
                return
            }
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    if(!this.subStatus){
                        this.$message({
                            message:'链接地址格式有问题',
                            type: 'error',
                            duration: 1500,
                        })
                        return
                    }
                    if(!this.dataForm.startDate){
                        this.$message.error('定时启用时间不能为空！')
                        return
                    }
                    this.buttonStatus = true;
                    if(this.advId){//编辑
                        var aa = this.dataForm;
                        var bb = {
                            advTitle: aa.advTitle,
                            endDate: aa.endDate,
                            id: this.advId,
                            imageUrl: aa.imageUrl,
                            relationTarget: aa.relationTarget,
                            sort: aa.sort,
                            startDate: aa.startDate,
                            advLinkConfigDTOList:aa.advLinkConfigDTOList
                        }
                        plainAdveUpdate(bb).then((res)=>{
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
                            }
                            this.buttonStatus = false;
                        })
                    }else{
                        floorClassAdveAdd(this.dataForm).then((res)=>{
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
                            }
                            this.buttonStatus = false;
                        })
                    }
                }
            })
        },
         GiftUrlHandle(imgUrl){
            let that = this;
            var url = imgUrl;
              that.dataForm.imageUrl = url;
        },
        //返回上级
        changePage(){
            this.$emit("addoraditList");
        }
    }
};
</script>
<style lang="scss" scoped>
    .adverClass{
        .el-input{
            width: 240px !important;
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
        /deep/ .el-form-item__content{
            display: inherit !important;
        }
        .operationTips{
            color: #999999;
            font-size: 12px;
        }
    }
         .img1{
                object-fit: fill;
                width: 270px;
                height: 270px;
            }
            .img2{
                object-fit: fill;
                width: 130px;
                height:130px;
            }
.aa{

    /deep/.el-input__inner {
        padding-right: 64px;
    }
}
</style>
