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
                    :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="advTitle" label="广告标题：" class="aa">
            <el-input v-model="dataForm.advTitle" placeholder="" maxlength="20" show-word-limit clearable></el-input>
        </el-form-item>
         <el-form-item prop="imageUrl" label="图片：">
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
        <!-- <el-form-item label="广告链接（URL）：">
            <el-input v-model="dataForm.relationTarget" placeholder="Http://xxxxxx.com" clearable @blur="urlChange"></el-input>
            <div class="operationTips">仅支持输入Http://开头的链接格式</div>
        </el-form-item> -->
        <el-form-item prop="relationType" label="跳转链接：">
            <el-radio-group v-model="dataForm.relationType" @change="toGetType">
                <el-radio-button label="url">链接</el-radio-button>
                <el-radio-button label="searchGoodsByClass">展示分类</el-radio-button>
                <el-radio-button label="topic">专题页</el-radio-button>
            </el-radio-group>
        </el-form-item>
       <el-form-item prop="relationTarget " label="展示分类：" v-if="dataForm.relationType == 'searchGoodsByClass'">
            <el-cascader
                :options="classDataList"
                change-on-select
                :clearable="true"
                :props="props"
                v-model="classIds"
                @change="handleChange">
            </el-cascader>
        </el-form-item>
        <el-form-item prop="relationTarget " label="专题页：" v-if="dataForm.relationType == 'topic'">
            <el-select v-model="dataForm.relationTarget" filterable placeholder="请选择">
                    <el-option
                        v-for="item in topicList"
                        :key="item.id"
                        :label="item.topicName"
                        :value="item.id">
                    </el-option>
                </el-select>
        </el-form-item>
         <el-form-item prop="relationTarget" label="链接：" v-if="dataForm.relationType =='url'">
            <el-input v-model="dataForm.relationTarget" maxlength="256" placeholder="http://xxxxxx.com" clearable></el-input>
            <div class="operationTips">仅支持输入http://开头的链接格式</div>
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
  </div>
</template>

<script>
import imgCropper from "@/components/upload/model-photo-cropper2";
import cloneDeep from 'lodash/cloneDeep'
import { floorClassAdveAdd,plainAdveUpdate } from '@/api/api'
import { uploadPicBase64,adverDetail,floorclassList,getTopicList  } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";

export default {
  // mixins: [mixinViewModule],
    data () {
        return {
            timeStatus:false,
            props:{
                label:'gcName',
                value: 'id'
            },
            classIds:[],
            classDataList:[],
            topicList:[],
            dataForm: {
                classId:'',
                relationType:"url",
                showType:'0',
                advTitle: "",
                relationId: '',
                endDate: "",
                imageUrl: "",
                relationTarget: "",
                sort: 0,
                startDate: this.parseTime(new Date()),
                radio:'1',
                advType: 2
            },
            breaddata: ["运营管理", "广告管理", "分类广告列表",'分类广告编辑'],
            breaddatas: ["运营管理", "广告管理", "分类广告列表",'分类广告新增'],
            buttonStatus:false,
            subStatus:true,
            uploading:false,
            advId:null,
            slectList:[]
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
                    { required: true, message: '图片不能为空！', trigger: 'blur' }
                    // {
                    //     validator: function (rule, value, callback) {
                    //         console.log('9999',value)
                    //         console.log('9999','dataForm.imageUrl')
                    //     }, trigger: 'change'
                    // }
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
        this.advType()
        getTopicList().then((res)=>{
            this.topicList = res.data;
        });
    },
    methods: {
        handleChange(val){
            this.dataForm.relationTarget = val[val.length-1];
        },
        toGetType(val){
            console.log('变化值',val)
            this.dataForm.relationType = val;
            this.dataForm.relationTarget = ''
            this.classIds=[]
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
        init(id){
            this.$nextTick(()=>{
                this.advId = id;
                if(id){
                    var obj  = {
                        id:id
                    }
                    adverDetail(obj).then((res)=>{
                        console.log('回显',res.data)
                        if(res.code == 200){
                            if(res.data.classId){
                                this.classIds = res.data.classId.split(",");
                            }
                            this.startObj = cloneDeep(res.data);//深拷贝回显详情初始数据
                            Object.assign(this.dataForm,res.data);
                            if(res.data.startDate){
                                this.dataForm.radio = '2';
                                this.dataForm.startDate = res.data.startDate;
                            }
                        }
                    })
                }
            })
        },
        advType(){
            var params={
                'showType':this.dataForm.showType
            }
            floorclassList(params).then((res)=>{
                    if(res.code == 200&&res.data){
                        this.slectList = res.data
                        this.classDataList = res.data;

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
                    if(this.classIds){
                        this.dataForm.classId=this.classIds.join(",")
                    }
                    this.buttonStatus = true;
                    if(this.advId){//编辑
                        var aa = this.dataForm;
                        var bb = {
                            advTitle: aa.advTitle,
                            endDate: aa.endDate,
                            id: this.advId,
                            classId:aa.classId,
                            relationType:aa.relationType,
                            imageUrl: aa.imageUrl,
                            relationTarget: aa.relationTarget,
                            sort: aa.sort,
                            startDate: aa.startDate
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
                                // this.goBack();
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
                                // this.goBack();
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
.aa{

    /deep/.el-input__inner {
        padding-right: 64px;
    }
}
</style>
