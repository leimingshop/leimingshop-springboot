<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form  v-show="shopsIsShow" :inline="true"  :model="dataForm" :rules="dataRule" ref="dataForm1" label-width="200px" style="width:90%;margin: auto;">
           <div>
               <div class="formMode">
                   <div style="margin-bottom:20px;font-weight: 650">店铺信息</div>
                   <div class="itemMode">
                       <el-form-item label="店铺名称：" prop="storeName">
                           <el-input type="textarea" v-model="dataForm.storeName" maxlength="30" placeholder="输入建议" clearable></el-input>
                       </el-form-item>
                       <el-form-item label="店铺联系人：" prop="storeLinkman">
                           <el-input v-model="dataForm.storeLinkman" placeholder="输入建议" maxlength="20" clearable></el-input>
                       </el-form-item>
                   </div>
                   <div class="itemMode">
                       <el-form-item label="店铺类型：" prop="storeType">
                           <el-select v-model="dataForm.storeType" placeholder="店铺类型">
                               <el-option label="自营店铺" value="1">自营店铺</el-option>
                               <el-option label="普通店铺" value="2">普通店铺</el-option>
                           </el-select>
                       </el-form-item>
                       <el-form-item label="店铺联系电话：" prop="linkmanPhone">
                           <el-input v-model="dataForm.linkmanPhone" placeholder="输入建议" maxlength="20" clearable></el-input>
                       </el-form-item>
                   </div>
                   <div class="itemMode">
                       <el-form-item label="店铺简介：" prop="storeIntro">
                           <el-input type="textarea" v-model="dataForm.storeIntro" placeholder="请输入备注" maxlength="200" clearable></el-input>
                       </el-form-item>
                       <!--storeLogo-->
                       <el-form-item label="店铺LOGO：" prop="storeLogo">
                           <div style="width:250px">
                               <div style="width:100px" v-loading="imgLoading[0]">
                                    <img-cropper v-if="dataForm.id==null | dataForm.id==''"
                                       ref="cropperImg1"
                                       :index="'0'"
                                       :imgWidth='"100px"'
                                       :imgHeight='"100px"'
                                        :cropImg="dataForm.storeLogo"
                                       @GiftUrlHandle="uploadPic">
                                  </img-cropper>

                                  <img-cropper2 v-else
                                       ref="cropperImg1"
                                       :index="'0'"
                                       :imgWidth='"100px"'
                                       :imgHeight='"100px"'
                                        :cropImg="dataForm.storeLogo"
                                       @GiftUrlHandle="GiftUrlHandle">
                                  </img-cropper2>
                               </div>
                           </div>
                       </el-form-item>
                       <!--分割线-->
                       <!-- <div style="width:392px"></div> -->
                   </div>
               </div>
               <div class="formMode" style="margin-top: 20px;">
                    <div style="margin-bottom:20px;font-weight: 650">可选经营范围</div>
                    <div  style="display: flex;justify-content: space-between;">
                         <!-- 左边区域 -->
                         <div class="businessScope" style="max-height:300px;overflow: auto;width:300px;">
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
                        <div>
                             <el-button style="margin-top: 136px;" class="btn"  @click="saveClassName()">添加></el-button>
                        </div>
                        <!-- 右边 区域 -->
                        <div   style="width:400px;margin-top: -36px;"> 
                             <!-- 店铺信息 -->
                            <div style="width:400px;">
                                <div style="margin-bottom:20px;font-weight: 650">已选择经营范围</div>
                                <div class="businessScope">
                                    <div  style="line-height: 25px;" v-for="(item, index) in className">
                                        <span>{{item}}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div style="margin-right: 148px;">
                              <div>
                                <el-form-item v-if="dataForm.registerAuditStatus!=40" label="审核结果：" >
                                    <span v-if="dataForm.registerAuditStatus==10">待审核</span>
                                    <span v-if="dataForm.registerAuditStatus==20">审核通过</span>
                                    <span v-if="dataForm.registerAuditStatus==30">审核拒绝</span>
                                </el-form-item>
                            </div>
                            <div>
                                <el-form-item v-if="dataForm.registerAuditStatus!=10 &&dataForm.registerAuditStatus!=40" label="审核时间：" >
                                    <span>{{dataForm.updateDate}}</span>
                                </el-form-item>
                            </div>
                            <div>
                                <el-form-item v-if="dataForm.registerAuditStatus!=10&&dataForm.registerAuditStatus!=40" label="备注：" >
                                    <span>{{dataForm.registerAuditCause}}</span>
                                </el-form-item>
                            </div>
                        </div>
                
                    </div>
               </div>
           </div>

            <div style="display: flex;justify-content: center;margin-top:20px">
                <el-button class="btn" type="primary" @click="nextStep()">下一步</el-button>
            </div>
        </el-form>

        <!--资质证明表单-->
        <el-form  v-show="!shopsIsShow" :inline="true"  :model="dataForm" :rules="dataRule" ref="dataForm2" label-width="200px">
            
            <div class="secondForm">
                <div class="companyNews" style="margin-bottom:20px;font-weight: 650">
                    公司信息
                    <el-form-item label="公司名称：" prop="companyName">
                        <el-input type="textarea" v-model="dataForm.companyName" placeholder="输入建议" maxlength="30" clearable></el-input>
                        <div style="color: red;position: absolute;min-width: 150px;margin-left: 205px;margin-top: -35px;">（须与营业执照一致）</div>
                    </el-form-item>
                    <el-form-item label="法人姓名：" prop="legalPersonName">
                        <el-input v-model="dataForm.legalPersonName" placeholder="输入建议" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="办公电话：" prop="companyPhone">
                        <el-input v-model="dataForm.companyPhone" placeholder="输入建议" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="办公地址：" prop="companyAddressDetail">
                        <el-input type="textarea" v-model="dataForm.companyAddressDetail" placeholder="输入建议" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="公司税号：" prop="taxpayerId">
                        <el-input v-model="dataForm.taxpayerId"  maxlength="20" placeholder="输入建议" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="银行开户账号：" prop="bankAccountNumber">
                        <el-input v-model="dataForm.bankAccountNumber" maxlength="20" placeholder="输入建议" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="开户银行：" prop="bankName">
                        <el-input type="textarea" v-model="dataForm.bankName"  maxlength="30"  placeholder="输入建议" clearable></el-input>
                    </el-form-item>
                </div>
                <div class="proofNews" style="margin-bottom:20px;font-weight: 650">
                    资质证明
                    <el-form-item label="法人身份证（正反两面）：" prop="idCardNatPicture">
                        <div style="float: left">
                            <div style="width:129px" v-loading="imgLoading[1]">
                                <img-cropper v-if="dataForm.id==null | dataForm.id==''"
                                        ref="idCardNatPicture"
                                        :index="'1'"
                                        :imgWidth='"129px"'
                                        :imgHeight='"86px"'
                                        :cropImg="dataForm.idCardNatPicture"
                                        @GiftUrlHandle="uploadPic"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('idCardNatPicture')">
                                </img-cropper>

                                <img-cropper2 v-else
                                        ref="idCardNatPicture"
                                        :index="'1'"
                                        :imgWidth='"129px"'
                                        :imgHeight='"86px"'
                                        :type='"0"'
                                        :cropImg="dataForm.idCardNatPicture"
                                        @GiftUrlHandle="GiftUrlHandle"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('idCardNatPicture')">
                                </img-cropper2>
                            </div>
                            <div style="text-align: center;">身份证（国徽页）</div>
                        </div>
                        <div style="float: left;margin-left:10px">
                            <div style="width:129px" v-loading="imgLoading[2]">
                                <img-cropper v-if="dataForm.id==null | dataForm.id==''"
                                        ref="idCardPeoPicture"
                                        :index="'2'"
                                        :imgWidth='"129px"'
                                        :imgHeight='"86px"'
                                        :cropImg="dataForm.idCardPeoPicture"
                                        @GiftUrlHandle="uploadPic"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('idCardPeoPicture')"
                                        >
                                </img-cropper>

                                 <img-cropper2 v-else
                                        ref="idCardPeoPicture"
                                        :index="'2'"
                                        :imgWidth='"129px"'
                                        :imgHeight='"86px"'
                                        :cropImg="dataForm.idCardPeoPicture"
                                        @GiftUrlHandle="GiftUrlHandle"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        :type='"0"'
                                        @delteteImg="delteteImg('idCardPeoPicture')"
                                        >
                                </img-cropper2>
                            </div>
                            <div style="text-align: center;">身份证（人像页）</div>
                        </div>
                    </el-form-item>
                    <el-form-item label="三证合一营业执照：" prop="electronicBusinessLicense">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[3]">
                                <img-cropper v-if="dataForm.id==null | dataForm.id==''"
                                        ref="cropperImg3"
                                        :index="'3'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.electronicBusinessLicense"
                                        @GiftUrlHandle="uploadPic"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('electronicBusinessLicense')"
                                        >
                                </img-cropper>

                                 <img-cropper2 v-else
                                        ref="cropperImg3"
                                        :index="'3'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.electronicBusinessLicense"
                                        @GiftUrlHandle="GiftUrlHandle"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        :type='"0"'
                                        @delteteImg="delteteImg('electronicBusinessLicense')"
                                        >
                                </img-cropper2>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="纳税人资格证：" prop="taxpayerPicture">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[4]">
                                <img-cropper  v-if="dataForm.id==null | dataForm.id==''"
                                        ref="cropperImg4"
                                        :index="'4'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.taxpayerPicture"
                                        @GiftUrlHandle="uploadPic" 
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('taxpayerPicture')"
                                        >
                                </img-cropper>

                                <img-cropper2 v-else
                                        ref="cropperImg4"
                                        :index="'4'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.taxpayerPicture"
                                        @GiftUrlHandle="GiftUrlHandle" 
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        :type='"0"'
                                        @delteteImg="delteteImg('taxpayerPicture')"
                                        >
                                </img-cropper2>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="银行开户许可证：" prop="bankAccountPicture">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[5]">
                                <img-cropper v-if="dataForm.id==null | dataForm.id==''"
                                        ref="cropperImg5"
                                        :index="'5'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.bankAccountPicture"
                                        @GiftUrlHandle="uploadPic"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        @delteteImg="delteteImg('bankAccountPicture')"
                                        >
                                </img-cropper>

                                    <img-cropper2 v-else
                                        ref="cropperImg5"
                                        :index="'5'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.bankAccountPicture"
                                        @GiftUrlHandle="GiftUrlHandle"
                                        :canDelete='"1"'
                                        :needHover='"1"'
                                        :type='"0"'
                                        @delteteImg="delteteImg('bankAccountPicture')"
                                        >
                                </img-cropper2>
                            </div>
                        </div>
                    </el-form-item>
                </div>
            </div>
            <div style="display: flex;justify-content: center;margin-top:20px">
                <el-button class="btn" type="primary" @click="submitStore" :saveLoading="saveLoading">{{saveLoading ? "提交中···" : "完成"}}</el-button>
                <el-button class="btn" type="primary" @click="preStep()">上一步</el-button>
                <el-button @click="cancleSubmit()">取消</el-button>
            </div>
        </el-form>
      
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import imgCropper2 from "@/components/upload/model-photo-cropper2";
    import imgCropper from "@/components/model-photo-cropper";
    import { uploadPicBase64,storeInfo,settingUpdateStore,addStore,allGoodsclass,verifyStoreName } from '@/api/api'
    import { isMobile,isPhone } from '@/utils/validate'
    import { isPrice } from '@/utils/validate'

    export default {
        data () {
            var validateGtZero = (rule, value, callback) => {
                if (value <0 || value==0) {
                    return callback(new Error('注册资金须大于0'))
                }
                callback()
            }
            return {
                row:"",
                breaddata: [],
                className:[],
                flag:true,
                dataForm: {
                    id:'',
                    bankAccountNumber:"",
                    bankAccountPicture: "",//银行开户许可证
                    bankName: "",
                    bankAccountName: "",
                    companyAddressDetail: "",
                    companyName: "",
                    companyPhone: "",
                    electronicBusinessLicense: "",//营业执照电子版
                    idCardNatPicture: "",//身份证，反面
                    idCardPeoPicture: "",//身份证，正面
                    legalPersonName: "",
                    linkmanPhone: "",
                    storeClassId: "",// 要么传null 要么数据 要么不传
                    storeIntro: "",
                    storeLinkman: "",
                    storeLogo: "",//店铺logo
                    storeName: "",
                    storeType: "",
                    taxpayerPicture: "",//纳税人资格证
                    userId: "",
                    taxpayerId:"",//公司税号
                    storeGoodsClassDTO:[]
                    
                },
                imgLoading:[false,false,false,false,false,false],
                shopsIsShow:true,
                saveLoading:false,
                defaultProps: {
                    children: 'children',
                    label: 'gcName'
                },
                getClassLoading:false,// 获取树形结构loading
                data:[],
                expandedKeys:[],
                checkedKeys:[],
                dataRule:{
                    storeName: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    storeLinkman: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    storeType: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    linkmanPhone: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    storeIntro: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    storeLogo: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    companyName: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    legalPersonName: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    companyPhone: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    companyAddressDetail: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    taxpayerId: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    bankAccountNumber: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    bankName: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    idCardNatPicture: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    electronicBusinessLicense: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    taxpayerPicture: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    bankAccountPicture: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                }
            }
        },
        components:{
            imgCropper,
            imgCropper2,
            Bread,
        },
        methods: {
           init(row){
                // 获取树形结构数据
                this.getTreeData();
                this.row = row;
                if(row){
                    this.breaddata=["店铺管理", "店铺设置", "修改店铺"]
                    // 回显数据
                    this.backScan();
                    this.flag=true
                }else{
                    this.breaddata=["店铺管理", "店铺设置", "创建店铺"]
                    this.flag=false
                }
                setTimeout(() => {
                    this.saveClassName()
                }, 500);
           },
           //  如果是编辑，需要回显数据
           backScan(){
                //  存放树形结构末级id 
                // this.expandedKeys = ['1191625918657863681']
                // this.checkedKeys  = ['1191625918657863681']
                storeInfo().then(res=>{
                    if(res.code == 200 && res.data){
                        Object.assign(this.dataForm,res.data);
                        this.dataForm.storeType = res.data.storeType+""
                        this.dataForm.id= res.data.storeId
                        this.expandedKeys = [];
                        this.checkedKeys = [];
                        if(res.data.storeGoodsClassDTO){
                            this.dataForm.storeGoodsClassDTO=res.data.storeGoodsClassDTO
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
                    }
                })
           },
        //    删除图片
           delteteImg(key){
               this.dataForm[key] = ""
           },
            // 创建店铺
            submitStore(){
                if(this.saveLoading){
                    return;
                }
                this.$refs['dataForm2'].validate((valid) => {
                    if (valid) {
                        var fn = this.row?settingUpdateStore:addStore
                        this.saveLoading = true
                        fn(this.dataForm).then(res=>{
                            this.saveLoading = false
                            if(res.code == 200){
                                this.$message({
                                    message:res.msg,
                                    type: 'success',
                                    duration: 1500,
                                })
                                this.changePage();
                            }else{
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                            }
                        })
                    }
                })
            },
            GiftUrlHandle(imgUrl,index){
                let that = this;
                 let url = imgUrl;
                // this.uploadPic(val,index);
                // var url = res.data.url
                if(index == '0'){
                    // 1.店铺logo
                    that.dataForm.storeLogo = url;
                }else if(index == '1'){
                    //2.法人身份证（正面）
                    that.dataForm.idCardNatPicture = url;
                } else if(index == '2'){
                    //2.法人身份证（反面）
                    that.dataForm.idCardPeoPicture = url;
                }else if(index == '3'){
                    //3.三证合一营业执照
                    that.dataForm.electronicBusinessLicense = url;
                }else if(index == '4'){
                    //4.纳税人资格证
                    that.dataForm.taxpayerPicture = url;
                }else{
                    // 5.银行开户许可证
                    that.dataForm.bankAccountPicture = url;
                }
            },
            saveClassName(){
                var treeDate=this.$refs.tree.getCheckedKeys();
                if(this.row && this.flag){
                    treeDate=[]
                    if(this.dataForm.storeGoodsClassDTO){
                        this.dataForm.storeGoodsClassDTO.forEach((item,index)=>{
                            treeDate.push(item.id);
                        })
                    }
                    this.flag=false
                }
                var copyName=[];
                this.data.forEach((item,index)=>{
                    item.children && item.children.forEach((item2,index2)=>{
                        item2.children && item2.children.forEach((item3,index3)=>{
                            treeDate.forEach((item4,index4)=>{
                                if(item4 && item4== item3.id){
                                    var bb =item.gcName+"-->"+item2.gcName+"-->"+item3.gcName
                                    copyName.push(bb)
                                }
                            })
                        })
                    })
                })
                this.className=copyName
            },
            uploadPic(base64,index){
                let that = this;
                const params = { "imgStr": base64 };
                if(index!=0){
                    params.type=0
                }else{
                    params.type=1
                }
                return new Promise(function(resolve){
                    that.imgLoading[index] = true;
                    that.$set(that.imgLoading,index,that.imgLoading[index])
                    uploadPicBase64(params).then(res =>{
                        that.imgLoading[index] = false;
                        that.$set(that.imgLoading,index,that.imgLoading[index])
                        if(res && res.code == "200"){
                            var url = res.data.url
                            if(index == '0'){
                                // 1.店铺logo
                                that.dataForm.storeLogo = url;
                            }else if(index == '1'){
                                //2.法人身份证（正面）
                                that.dataForm.idCardNatPicture = url;
                            } else if(index == '2'){
                                //2.法人身份证（反面）
                                that.dataForm.idCardPeoPicture = url;
                            }else if(index == '3'){
                                //3.三证合一营业执照
                                that.dataForm.electronicBusinessLicense = url;
                            }else if(index == '4'){
                                //4.纳税人资格证
                                that.dataForm.taxpayerPicture = url;
                            }else{
                                // 5.银行开户许可证
                                that.dataForm.bankAccountPicture = url;
                            }
                            resolve("true")
                        }else {
                            that.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                            resolve("false")
                        }
                    })
                });
            },
            // 返回上一页
            changePage(){
                this.$emit('changePage')
            },
            // 下一步
            async nextStep(){
                let obj={
                    storeName:this.dataForm.storeName,
                    storeId:this.dataForm.id
                    }
                        
                 //校验店铺名称是否重复
                let res = await verifyStoreName(obj)
                    if(res.code!=200){
                        this.$message({
                            message:res.msg,
                            type: 'error',
                            duration: 1500,
                        })
                    return
                    }
      
                // console.log(this.$refs.tree.getCheckedNodes());
                // console.log(this.$refs.tree.getCheckedKeys());
                var checkedIds1 =  this.$refs.tree.getCheckedKeys(); // 全选中
                var checkedIds2 =  this.$refs.tree.getHalfCheckedKeys(); //  半选中
                this.dataForm.storeClassId = checkedIds1.concat(checkedIds2); 
                this.$refs['dataForm1'].validate((valid) => {
                    if (valid) { 
                        this.shopsIsShow = false
                    }
                })
            },
            // 上一步
            preStep(){
                this.shopsIsShow = true;
            },
            // 取消
            cancleSubmit(){
               let that = this;
               this.$confirm("是否确认取消审核？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                   that.changePage();
                })
                .catch(() => {
                    // this.$message({type: "info",message: "已取消"});
                }); 
            }
        },
    };
</script>
<style  lang="scss" scoped>
    .formMode{
        border-bottom: 1px solid #ECEDF1;
        /deep/ .el-form-item__label{
            width: 120px !important;
        }
        .el-input{
            width: 250px !important;
        }
        .textarea{
            width: 250px !important;
        }
        &:first-child{
            padding-top: 0;
        }
        &:last-child{
            border: none;
        }
        .itemMode{
            display: flex;
            justify-content: space-between;
        }
    }
    /*第二个表单 资质证明*/
    .secondForm{
        display: flex;
        justify-content: space-around;
        .el-textarea__inner{
            width: 250px !important;
        }
        .el-form-item{
            display: block;
        }
        .el-input{
            width: 100% !important;
        }
        .el-textarea{
            width: 108% !important;
        }
        .companyNews{
            /deep/ .el-form-item__label{
                width: 110px !important;
            }
        }
        .proofNews{
            /deep/ .el-form-item__label{
                width: 180px !important;
            }
        }
        /deep/ .el-input__inner, /deep/ .el-textarea__inner, /deep/ .el-form--inline .el-input{
            width: 330px;
        }
        /deep/ .el-form-item {
            margin-right: 20px !important;
        }
    }
    /deep/ .el-input__inner, /deep/ .el-textarea__inner{
        width: 210px;
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
        height: 310px;
}
</style>
