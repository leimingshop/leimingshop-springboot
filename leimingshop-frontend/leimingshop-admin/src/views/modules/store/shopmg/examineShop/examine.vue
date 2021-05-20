<template>
	<div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="showListFn"></Bread>
        <div v-loading="backScanLoading" style="margin-bottom:40px;">
            <el-form :inline="false"  label-width="150px">
                <div style="display:flex;justify-content: space-around" >
                      <!-- 店铺信息 -->
                    <div class="formMode" style="width:400px;">
                        <div style="margin-bottom:20px;font-weight: 650">店铺信息</div>
                            <el-form-item label="店铺名称：" prop="storeName">
                                <el-input type="textarea"  :disabled="disabledVisible" v-model="dataInfo.storeName" placeholder="" clearable></el-input>
                            </el-form-item>
                            <el-form-item label="店铺联系人：" prop="storeLinkman">
                                <el-input  :disabled="disabledVisible" v-model="dataInfo.storeLinkman" placeholder="" clearable></el-input>
                            </el-form-item>
                            <el-form-item label="店铺类型：" prop="storeType">
                                <el-select  :disabled="disabledVisible" v-model="dataInfo.storeType" placeholder="店铺类型">
                                    <el-option
                                    v-for="item in storeTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="店铺联系电话：" prop="linkmanPhone">
                                <el-input   :disabled="disabledVisible" v-model="dataInfo.linkmanPhone" placeholder="" clearable></el-input>
                            </el-form-item>
                            <el-form-item label="店铺简介：" prop="storeIntro">
                                <el-input  :disabled="disabledVisible" type="textarea" v-model="dataInfo.storeIntro" placeholder="请输入备注" clearable></el-input>
                            </el-form-item>
                            <!--storeLogo-->
                            <el-form-item label="店铺LOGO：" prop="storeLogo">
                                <div style="width:250px">
                                    <!-- <img-cropper
                                            ref="cropperImg1"
                                            :index="'cropperImg1'"
                                            :imgWidth='"100px"'
                                            :imgHeight='"100px"'
                                            @GiftUrlHandle="GiftUrlHandle">
                                    </img-cropper> -->
                                    <img :src="dataInfo.storeLogo | filterImgUrl" alt="" style="width:100px;height:100px" @click="previewBigImg(dataInfo.storeLogo,0)">
                                </div>
                            </el-form-item>
                            <!--分割线-->
                            <div style="width:392px"></div>
                    </div>
                    <!-- 经营范围 -->
                    <div class="formMode"  style="width:400px;">
                        <div style="margin-bottom:20px;font-weight: 650">经营范围</div>
                        <div class="businessScope" >
                            <!-- <div v-for="(item,index) in dataInfo.storeGoodsClassDTO">
                                {{item.gcName}}
                            </div> -->
                            <el-tree
                                    v-loading="getClassLoading"
                                    ref="tree"
                                    :data="data"
                                    show-checkbox
                                    node-key="id"
                                    :default-expanded-keys="expandedKeys"
                                    :default-checked-keys="checkedKeys"
                                    :props="defaultProps">
                            </el-tree>
                        </div>
                    </div>
                </div>

            </el-form>

            <div>
                <h4>入驻审核</h4>
            </div>
            <el-form :inline="false" :model="dataForm" :rules="dataRule" ref="dataForm"   label-width="200px">
                <el-form-item label="审核结果：" prop="infoAuditStatus">
                    <!-- <el-radio v-model="infoAuditStatus" label="20">审核通过</el-radio>
                    <el-radio v-model="infoAuditStatus" label="30">审核拒绝</el-radio> -->
                    <el-radio-group v-model="dataForm.infoAuditStatus" :disabled="row.operationType=='look'">
                        <el-radio :label="20">审核通过</el-radio>
                        <el-radio :label="30">审核拒绝</el-radio>
                    </el-radio-group>

                </el-form-item>
                 <el-form-item label="备注：" prop="infoAuditCause">
                   <el-input
                       :disabled="row.operationType=='look'"
                        type="textarea"
                        :rows="2"
                        maxlength="100"
                        placeholder="请输入备注"
                        v-model="dataForm.infoAuditCause">
                    </el-input>
                </el-form-item>

                <div style="display: flex;justify-content: center;margin-top:20px" v-if="row.operationType=='examine'">
                        <el-button type="primary" size="mini" @click="dataFormSubmit('dataForm')"  :disabled="saveLoading" :loading="saveLoading">{{saveLoading ? "审核中···" : "确定"}}</el-button>
                        <el-button @click="showListFn"  size="mini" >取消</el-button>
                </div>
            </el-form>
        </div>

        <!--返回按钮-->
        <div style="margin-top: 40px;margin-bottom: 40px">
            <el-button type="primary" style="display:block;margin:0 auto" @click="showListFn()">返回</el-button>
        </div>

	</div>
</template>
<script>
import Bread from "@/components/bread";
import {storeAuditbaseInfo,shopAuditUpdateStatus,allGoodsclass} from  "@/api/api.js"
export default {
	data () {
        var validateAuditCaus= (rule, value, callback) => {
            if (this.dataForm.infoAuditStatus==30 && this.dataForm.infoAuditStatus=="") {
                return callback(new Error("审核拒绝必填!"))
            }
            return callback()
        }
		return {
            disabledVisible:true,//是否disabled
            breaddata: ["店铺管理", "店铺列表", "店铺信息"],
            row:'',
            dataInfo:"",//回显的数据,
            backScanLoading:false,
            saveLoading:false,// 提交数据loading
            infoAuditStatus:20,
            dataForm:{
                infoAuditStatus:20,//10 待审核 20 审核通过 30 审核拒绝 40 待提交 ,
                infoAuditCause:'', // 审核原因 ,
            },
            storeTypes:[
                    {value: 1,label: '自营商户'},
                    {value: 2,label: '普通商户'}
            ],
            dataRule:{
                infoAuditStatus: [
                    // { required: true, message:"必选项不能为空", trigger: 'blur' },
                ],
                infoAuditCause: [
                    // { required: true, message:"必选项不能为空", trigger: 'blur' },
                    { validator: validateAuditCaus, trigger: 'blur' }
                ]
            },
            defaultProps: {
                children: 'children',
                label: 'gcName'
            },
            getClassLoading:false,// 获取树形结构loading
            data:[],
            expandedKeys:[],
            checkedKeys:[],
		}
	},
	components:{
        Bread
	},
	methods:{
        init(row){
            this.row = row;
            this.backScan();
             // 获取树形结构数据
            this.getTreeData();
        },
            //大图预览带分页
            previewBigImg(images,index) {
                //string转数组
                var imagesArr = images?images.split(","):[];
                if(imagesArr.length==0){
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2,index2)=>{
                    if (/http/.test(item2) || /data:image/.test(item2)) {
                    } else {
                        imagesArr[index2]  = window.SITE_CONFIG['imgURL'] + "" + item2;
                    }
                })
                this.$imagePreview({
                    images: imagesArr,
                    index:index,

                })
            },
        // 回显数据
        backScan(){
            var obj = {
               params:{
                   storeId:this.row.storeId,
                   createDate :this.row.createDate
               }
            }
            this.backScanLoading = true;
            storeAuditbaseInfo(obj).then((res)=>{
                 this.backScanLoading = false;
                console.log("店铺详情");
                console.log(res);
                if(res.code==200){
                    this.dataInfo = res.data;
                    // 如果是查看的话，回显审核的情况
                    if(this.row.operationType=="look"){
                         this.dataForm.infoAuditStatus = res.data.infoAuditStatus
                          this.dataForm.infoAuditCause = res.data.infoAuditCause
                    }
                    this.expandedKeys = [];
                    this.checkedKeys = [];
                    if(res.data.storeGoodsClassDTO){
                        res.data.storeGoodsClassDTO.forEach((item,index)=>{
                            this.expandedKeys.push(item.id);
                            this.checkedKeys.push(item.id);
                        })
                    }
                }
            })
        },
          // 获取树形结构数据
        getTreeData(){
            this.getClassLoading = true;
            allGoodsclass().then((res)=>{
                this.getClassLoading = false
                if(res.code==200){
                    this.data = res.data;
                    this.changeTreeDisabled(true)
                }
            })
        },
         // 树形结构禁用和启用
        changeTreeDisabled(boolArgu){
            this.data.forEach((item,index)=>{
                item.disabled = boolArgu
                item.children && item.children.forEach((item2,index2)=>{
                    item2.disabled = boolArgu
                    item2.children && item2.children.forEach((item3,index3)=>{
                         item3.disabled = boolArgu
                    })
                })
            })
            this.data = [].concat(this.data);
            console.log(this.data);
        },
        // 显示列表页
        showListFn(){
            this.$emit("showListFn");
        },
        // 提交
        dataFormSubmit(formName) {
            if(this.saveLoading){
                return;
            }
            this.$refs[formName].validate(valid => {
                if (valid) {
                    var obj  = {
                        "auditType": 1,// 审核类型 1 店铺普通信息 2 店铺公司信息 3 店铺入住审核 ,
                        "id": this.row.id, //  审核资质或者公司信息ID ,
                        // "operator": "", //  操作人 ,
                        "registerAuditCause": this.dataForm.infoAuditCause, //  审核原因 ,
                        "registerAuditStatus": this.dataForm.infoAuditStatus, // 10 待审核 20 审核通过 30 审核拒绝 40 待提交 ,
                        "storeId": this.row.storeId?this.row.storeId:this.row.id // 店铺ID
                    }
                    this.saveLoading = true;
                    shopAuditUpdateStatus(obj).then((res)=>{
                        this.saveLoading = false;
                        console.log(res);
                        if(res.code==200){
                             this.$message.success(res.msg);
                             this.showListFn();
                        }else{
                            this.$message.error(res.msg);
                        }
                    })
                } else {
                    return false;
                }
            })
        },
	}
}
</script>
<style  lang="scss" scoped>
    // .formMode{
    //     border-bottom: 1px solid #ECEDF1;
    //     /deep/ .el-form-item__label{
    //         width: 150px !important;
    //     }
    //     .el-input{
    //         width: 250px !important;
    //     }
    //     .textarea{
    //         width: 250px !important;
    //     }
    //     &:first-child{
    //         padding-top: 0;
    //     }
    //     &:last-child{
    //         border: none;
    //     }
    //     .itemMode{
    //         display: flex;
    //         justify-content: space-between;
    //     }
    // }
    /*第二个表单 资质证明*/
    .secondForm{
        display: flex;
        justify-content: space-around;
        .el-textarea__inner{
            // width: 250px !important;
        }
        .el-form-item{
            // display: block;
        }
        .el-input{
            // width: 100% !important;
        }
        .el-textarea{
            // width: 108% !important;
        }
        .companyNews{
            /deep/ .el-form-item__label{
                // width: 150px !important;
            }
        }
        .proofNews{
            /deep/ .el-form-item__label{
                width: 180px !important;
            }
        }
    }
.businessScope{
     padding: 10px;
    max-height:350px;
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(217, 217, 217, 1);
    border-radius: 4px;
    box-shadow: 0px 0px 4px rgba(217, 217, 217, 0.988235294117647);
    // overflow-y: scroll;
    overflow: auto;
    margin-bottom: 20px;
}
</style>
