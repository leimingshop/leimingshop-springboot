<template>
  <div class="adverFloor">
    <Bread :breaddata="advId?breaddata:breaddatas" @changePage="changePage" :index="'1'"></Bread>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"  @keyup.enter.native="submitStore()" label-width="130px">
        <el-form-item prop="relationId" label="楼层名称：">
            <!-- <el-select  v-model="dataForm.floorType" @change="floorType()"   v-show="advId?false:true" placeholder="请选择">
                     <el-option
                        key="2"
                        label="pc楼层"
                        value="2">
                    </el-option>

                    <el-option
                        key="1"
                        label="h5楼层"
                        value="1">
                    </el-option>
                </el-select> -->
            <el-select v-model="dataForm.relationId" clearable :disabled="advId?true:false" placeholder="请选择">
                <el-option
                    v-for="item in slectList"
                    :key="item.id"
                    :label="item.floorName"
                    :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="advTitle" label="广告标题：">
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
             <div class="operationTips">建议上传高于702x260像素的白底图片</div>
        </el-form-item>
        <el-form-item label="广告链接（URL）：">
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
        <el-form-item prop="sort" label="排序优先级：">
            <el-input-number v-model="dataForm.sort" controls-position="right" onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )" :min="0" :max="255" ></el-input-number>
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
import { adverDetail,floorAdveSlect } from '@/api/api'
import { isURL } from '@/utils/validate'
import Bread from "@/components/bread";


export default {
//   mixins: [mixinViewModule],
    data () {
        return {
            timeStatus:false,
            dataForm: {
                floorType:'1',
                advTitle: "",
                relationId: '',
                endDate: "",
                imageUrl: "",
                relationTarget: "",
                sort: 0,
                startDate: this.parseTime(new Date()),
                radio:'1',
                advType: 1
            },
            breaddata: ["运营管理", "H5楼层广告列表",'编辑'],
            breaddatas: ["运营管理",  "H5楼层广告列表",'新增'],
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
                    { required: true, message: '楼层类别不能为空', trigger: 'blur' }
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
        this.floorAdveSlect();
    },
    methods: {
     
        floorType(){
            this.floorAdveSlect();
        },

         floorAdveSlect(){
            var obj={
                params:{
                    'floorType':this.dataForm.floorType
                }
            }
            floorAdveSlect(obj).then((res)=>{
                if(res.code == 200&&res.data){
                    this.slectList = res.data
                }
            })
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
            console.log(this.dataForm.startDate)
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
                    this.buttonStatus=true;
                    if(this.advId){//编辑
                        let aa = this.dataForm;
                        let bb = {
                            advTitle: aa.advTitle,
                            endDate: aa.endDate,
                            id: this.advId,
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
                                // this.changePage();
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
                                // this.changePage();
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
    .adverFloor{
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
        .operationTips{
            color: #999999;
            font-size: 12px;
        }
    }

    /deep/ .el-input__inner {
        padding-right: 67px;
    }
</style>
