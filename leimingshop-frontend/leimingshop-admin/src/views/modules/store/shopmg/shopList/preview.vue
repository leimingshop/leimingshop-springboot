<template>
	<div>
        <Bread :breaddata="breaddata" :index="index" @changePage="showListFn"></Bread>
        <div v-loading="backScanLoading" style="margin-bottom:40px;">
            <el-form :inline="false"  label-width="150px">
                <div style="display:flex;justify-content: space-around" >
                      <!-- 店铺信息 -->
                    <div class="formMode" style="width:400px;">
                        <div style="margin-bottom:20px;font-weight: 650">店铺信息</div>
                            <el-form-item  label="店铺名称：" prop="storeName">
                                <el-input type="textarea" :disabled="disabledVisible" v-model="dataInfo.storeName" placeholder="" clearable></el-input>
                            </el-form-item>
                            <el-form-item label="店铺联系人：" prop="storeLinkman">
                                <el-input  :disabled="disabledVisible" v-model="dataInfo.storeLinkman" placeholder="" clearable></el-input>
                            </el-form-item>
                            <el-form-item label="店铺类型：" prop="storeType">
                                <el-select  :disabled="disabledVisible" v-model="dataInfo.storeType" placeholder="店铺类型">
                                    <el-option label="自营店铺" value="1">自营店铺</el-option>
                                    <el-option label="普通店铺" value="2">普通店铺</el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="店铺联系电话：" prop="linkmanPhone">
                                <el-input   :disabled="disabledVisible" v-model="dataInfo.linkmanPhone" placeholder="" clearable></el-input>
                            </el-form-item>
                            
                            <el-form-item  label="店铺等级：" prop="gradeId">
                                <el-select :disabled="disabledVisible" v-model="dataInfo.gradeId" >
                                    <el-option  v-for="item in storeGradeList" :key="item.id" :label="item.sgName" :value="item.id">
                                    </el-option>
                                </el-select>
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
                <!--资质证明表单-->
                <div class="secondForm">
                    <div class="companyNews" style="margin-bottom:20px;font-weight: 650;width:400px;">
                        公司信息
                        <el-form-item label="公司名称：" prop="companyName">
                            <el-input type="textarea"  :disabled="disabledVisible" v-model="dataInfo.companyName" placeholder="" clearable></el-input>
                            <span style="color: red;position: absolute;min-width: 150px">（须与营业执照一致）</span>
                        </el-form-item>
                        <el-form-item label="法人姓名：" prop="legalPersonName">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.legalPersonName" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="办公电话：" prop="companyPhone">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.companyPhone" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item  label="办公地址：" prop="companyAddressDetail">
                            <el-input  :disabled="disabledVisible" type="textarea" v-model="dataInfo.companyAddressDetail" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="公司税号：" prop="taxpayerId">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.taxpayerId" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="银行开户账号：" prop="bankAccountNumber">
                            <el-input  :disabled="disabledVisible" v-model="dataInfo.bankAccountNumber" placeholder="" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="开户银行：" prop="bankName">
                            <el-input type="textarea"   :disabled="disabledVisible" v-model="dataInfo.bankName" placeholder="" clearable></el-input>
                        </el-form-item>
                    </div>
                    <div class="proofNews" style="margin-bottom:20px;font-weight: 650;width:400px;">
                        资质证明
                        <el-form-item label="法人身份证(正反两面)：" prop="idCardNatPicture">
                            <div style="float: left">
                                <!-- <img-cropper
                                        ref="idCardNatPicture"
                                        :index="'idCardNatPicture'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.idCardNatPicture | filterImgUrl" alt="" style="width:100px;height:100px"  @click="previewBigImg(dataInfo.idCardNatPicture,0)">
                                <!-- <div>身份证（国徽页）</div> -->
                            </div>
                            <div style="float: left">
                                <!-- <img-cropper
                                        ref="idCardPeoPicture"
                                        :index="'idCardPeoPicture'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.idCardPeoPicture | filterImgUrl" alt="" style="width:100px;height:100px;margin-left:10px"  @click="previewBigImg(dataInfo.idCardPeoPicture,0)">
                                <!-- <div>身份证（人像页）</div> -->
                            </div>
                        </el-form-item>
                        <el-form-item label="三证合一营业执照：" prop="electronicBusinessLicense">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg3"
                                        :index="'cropperImg3'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.electronicBusinessLicense | filterImgUrl" alt="" style="width:100px;height:100px"  @click="previewBigImg(dataInfo.electronicBusinessLicense,0)">
                            </div>
                        </el-form-item>
                        <el-form-item label="纳税人资格证：" prop="taxpayerPicture">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg4"
                                        :index="'cropperImg4'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.taxpayerPicture | filterImgUrl" alt="" style="width:100px;height:100px"  @click="previewBigImg(dataInfo.taxpayerPicture,0)">
                            </div>
                        </el-form-item>
                        <el-form-item label="银行开户足许可证：" prop="bankAccountPicture">
                            <div style="width:250px">
                                <!-- <img-cropper
                                        ref="cropperImg5"
                                        :index="'cropperImg5'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper> -->
                                <img :src="dataInfo.bankAccountPicture | filterImgUrl" alt="" style="width:100px;height:100px"  @click="previewBigImg(dataInfo.bankAccountPicture,0)">
                            </div>
                        </el-form-item>
                    </div>
                </div>
            </el-form>
             <div style="display: flex;justify-content: center;margin-top:20px">
                <el-button @click="showListFn()" type="primary" >返回</el-button>
            </div>
        </div>
	</div>
</template>
<script>
import Bread from "@/components/bread";
import {storeInfo,allGoodsclass} from  "@/api/api.js"
export default {
	data () {
		return {
            disabledVisible:true,//是否disabled
            // breaddata: ["商家系统", "商家管理", "商家列表",'商家详情'],
            row:'',
            dataInfo:"",//回显的数据,
            backScanLoading:false,
            saveLoading:false,// 提交数据loading
            registerAuditStatus:20,
               storeGradeList:[],
            // dataForm:{
            //     registerAuditStatus:20,//10 待审核 20 审核通过 30 审核拒绝 40 待提交 ,
            //     registerAuditCause:'', // 审核原因 ,
            // },
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
    props: ['index', "breaddata"],
      created(){
    this.getGradeList()
  },
	methods:{
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
        init(row){
            this.row = row;
            this.backScan();
             // 获取树形结构数据
            this.getTreeData();
        },
             // 获取店铺等级
            getGradeList () {
            return this.$http.get('/admin-api/store/grade/find/grade').then(({ data: res }) => {
                if (res.code !== 200) {
                return this.$message.error(res.msg)
                }
                this.storeGradeList = res.data;
                if(!this.dataForm.gradeId){
                    this.dataForm.gradeId = res.data[0].id;
                }

            }).catch(() => {})
            },
        // 回显数据
        backScan(){
            var obj = {
               params:{
                   storeId:this.row.id,
               }
            }
            this.backScanLoading = true;
            storeInfo(obj).then((res)=>{
                 this.backScanLoading = false;
                console.log("店铺详情");
                console.log(res);
                if(res.code==200){
                    this.dataInfo = res.data;
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
            this.$emit("showList");
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