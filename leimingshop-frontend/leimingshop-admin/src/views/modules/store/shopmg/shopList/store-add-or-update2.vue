<template>
  <div>
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
    
        <el-form  v-if="shopsIsShow" :inline="true"  :model="dataForm" :rules="dataRule" ref="dataForm" label-width="200px" style="width:80%;margin: auto;">
           <div>
               <div class="formMode" v-if="!id">
                   <div style="margin-bottom:20px;font-weight: 650">商户账号</div>
                   <div class="itemMode">
                      <el-form-item  label="商户账号："  prop="userId">
                         <!-- <el-select   v-model="dataForm.userId" placeholder="请选择商户账号">
                                    <el-option
                                    v-for="item in userList"
                                    :key="item.id"
                                    :label="item.account"
                                    :value="item.id">
                                    </el-option>
                                </el-select> -->
                        <!-- <el-input v-model="dataForm.userId" :value="dataForm.userId" :label="dataForm.account"  show-word-limit clearable @click="userPageList"></el-input> -->
                        <a v-if="!this.dataForm.userId"  @click="userPageList" style="cursor: pointer;">请选择账号</a>
                         <a v-if="this.dataForm.userId"  @click="userPageList" style="cursor: pointer;">{{this.dataForm.userName}}</a>

                      </el-form-item>
                      <!-- <el-form-item label="密码："  prop="password">
                          <el-input v-model="dataForm.password" clearable type="password" ></el-input>
                      </el-form-item> -->
                   </div>
                   <!-- <div class="itemMode">
                      <el-form-item label="邮箱："  prop="e_mail">
                          <el-input v-model="dataForm.e_mail"  show-word-limit clearable></el-input>
                      </el-form-item>
                      <el-form-item label="电话："  prop="mobilePhone">
                          <el-input v-model="dataForm.mobilePhone"  maxlength="11" show-word-limit clearable></el-input>
                      </el-form-item>
                   </div> -->
               </div>
               <div class="formMode">
                   <div style="margin-bottom:20px;font-weight: 650">店铺信息</div>
                   <div class="itemMode">
                       <el-form-item label="店铺名称：" prop="storeName">
                           <el-input v-model="dataForm.storeName" maxlength="20" placeholder="输入建议" clearable></el-input>
                       </el-form-item>
                       <el-form-item label="店铺联系人：" prop="storeLinkman">
                           <el-input v-model="dataForm.storeLinkman" placeholder="请输入店铺联系人" maxlength="20" clearable></el-input>
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
                           <el-input v-model="dataForm.linkmanPhone" placeholder="请输入店铺联系电话" maxlength="20" clearable></el-input>
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
                                    <img-cropper
                                       ref="cropperImg1"
                                       :index="'0'"
                                       :imgWidth='"100px"'
                                       :imgHeight='"100px"'
                                        :cropImg="dataForm.storeLogo"
                                       @GiftUrlHandle="GiftUrlHandle">
                                  </img-cropper>
                               </div>
                           </div>
                       </el-form-item>
                       <!--分割线-->
                       <!-- <div style="width:392px"></div> -->
                   </div>
               <div class="itemMode">
                   <!-- </el-form-item> -->
                        <el-form-item label="店铺等级：" prop="gradeId">
                            <el-select v-model="dataForm.gradeId" >
                                <el-option v-for="item in storeGradeList" :key="item.id" :label="item.sgName" :value="item.id">
                                </el-option>
                        </el-select>
                    </el-form-item>
               </div>
               </div>
               <div class="formMode" style="margin-top: 20px;">
                    <div style="margin-bottom:20px;font-weight: 650">经营范围</div>
                    <div style="max-height:300px;overflow: auto;width:300px;">
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

            <div style="display: flex;justify-content: center;margin-top:20px">
                <el-button class="btn" type="primary" @click="nextStep()">下一步</el-button>
            </div>
        </el-form>

        <!--资质证明表单-->
        <el-form  v-else :inline="true"  :model="dataForm" :rules="dataRule" ref="dataForm" label-width="200px"  style="width: 100%;margin: auto;">
            
            <div class="secondForm">
                <div class="companyNews" style="margin-bottom:20px;font-weight: 650;width:46%;">
                    公司信息
                    <el-form-item  label="公司名称：" prop="companyName">
                        <el-input type="textarea" v-model="dataForm.companyName" placeholder="请输入公司名称" maxlength="30" clearable></el-input>
                        <span style="color: red;position: absolute;min-width: 150px">（须与营业执照一致）</span>
                    </el-form-item>
                    <el-form-item label="法人姓名：" prop="legalPersonName">
                        <el-input v-model="dataForm.legalPersonName" placeholder="请输入法人姓名" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="办公电话：" prop="companyPhone">
                        <el-input v-model="dataForm.companyPhone" placeholder="请输入办公电话" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="办公地址：" prop="companyAddressDetail">
                        <el-input type="textarea" v-model="dataForm.companyAddressDetail" placeholder="请输入办公地址" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="公司税号：" prop="taxpayerId">
                        <el-input v-model="dataForm.taxpayerId" placeholder="请输入公司税号" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="银行开户账号：" prop="bankAccountNumber">
                        <el-input v-model="dataForm.bankAccountNumber" placeholder="请输入银行开户账号" maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="开户银行：" prop="bankName">
                        <el-input type="textarea" v-model="dataForm.bankName" placeholder="请输入开户银行" maxlength="30" clearable></el-input>
                    </el-form-item>
                </div>
                <div class="proofNews" style="margin-bottom:20px;font-weight: 650;width:41%;">
                    资质证明
                    <el-form-item label="法人身份证（正反两面）：" prop="idCardNatPicture">
                        <div style="float: left">
                            <div style="width:100px" v-loading="imgLoading[1]">
                                <img-cropper
                                        ref="idCardNatPicture"
                                        :index="'1'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                         :cropImg="dataForm.idCardNatPicture"
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper>
                            </div>
                            <div>身份证（国徽页）</div>
                        </div>
                        <div style="float: left">
                            <div style="width:100px" v-loading="imgLoading[2]">
                                <img-cropper
                                        ref="idCardPeoPicture"
                                        :index="'2'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.idCardPeoPicture"
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper>
                            </div>
                            <div>身份证（人像页）</div>
                        </div>
                    </el-form-item>
                    <el-form-item label="三证合一营业执照：" prop="electronicBusinessLicense">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[3]">
                                <img-cropper
                                        ref="cropperImg3"
                                        :index="'3'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.electronicBusinessLicense"
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="纳税人资格证：" prop="taxpayerPicture">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[4]">
                                <img-cropper
                                        ref="cropperImg4"
                                        :index="'4'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.taxpayerPicture"
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="银行开户足许可证：" prop="bankAccountPicture">
                        <div style="width:250px">
                            <div style="width:100px" v-loading="imgLoading[5]">
                                <img-cropper
                                        ref="cropperImg5"
                                        :index="'5'"
                                        :imgWidth='"100px"'
                                        :imgHeight='"100px"'
                                        :cropImg="dataForm.bankAccountPicture"
                                        @GiftUrlHandle="GiftUrlHandle">
                                </img-cropper>
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

 <el-dialog
        class="model-sku-data"
        title="店铺管理员"
        width="70%"
        :close-on-click-modal="false"
        :visible.sync="visible"
        :before-close="closeDialog">

    <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" >
        <el-form-item label="用户名：">
            <el-input v-model="dataForm.account" placeholder="用户名" clearable></el-input>
        </el-form-item>

        <el-form-item label="手机号：">
            <el-input v-model="dataForm.mobilePhone" placeholder="手机号" clearable></el-input>
        </el-form-item>

        <el-form-item label="邮箱：">
            <el-input v-model="dataForm.email" placeholder="邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="getUserList()">查询</el-button>
            <el-button @click="reset()" type="primary" plain>重置</el-button>
        </el-form-item>
    </el-form>
            <el-table 
                width="100%"
                border
                :data='userList'
                v-loading="dataListLoading"
                style="width: 100%;cursor: pointer;" >
            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    <!-- {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }} -->
                     {{scope.$index+1}}
                </template>
            </el-table-column>

			<el-table-column label="用户名" prop="account"  align="center">
                
            </el-table-column>

		    <el-table-column label="手机号"  prop="mobilePhone"  align="center">
         
            </el-table-column>

			<el-table-column label="邮箱" prop="email" align="center">
                
            </el-table-column>

			<el-table-column label="性别" prop="sex" align="center">
                <template slot-scope="scope">
                     <span  v-if="scope.row.sex==0">保密</span>
                      <span  v-if="scope.row.sex==1">男</span>
                       <span  v-if="scope.row.sex==2">女</span>
                </template>

            </el-table-column>
            
			<el-table-column label="操作" align="center">
                 <template slot-scope="scope">
                    <el-button v-if="!dataForm.userId || dataForm.userId!=scope.row.id" @click="getuser(scope.row)" type="primary" plain>选择</el-button>
                    <el-button v-if="dataForm.userId && dataForm.userId==scope.row.id" @click="getuser(scope.row)" type="success" plain>已选择</el-button>
                 </template>
            </el-table-column>

        </el-table>
        	<!-- 分页 -->
    <el-pagination
	    
	    :current-page="page1"
	    :page-sizes="[5, 20, 50, 100]"
	    :page-size="limit1"
	    :total="total1"
	    layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle1"
	    @current-change="pageCurrentChangeHandle1"
        >
    </el-pagination>
</el-dialog>

  </div>
</template>

<script>
    import cloneDeep from 'lodash/cloneDeep'
    import Bread from "@/components/bread";
    import imgCropper from "@/components/upload/model-photo-cropper2";
    import { uploadPicBase64,storeInfo,storeUpdateStore,addStoreSave,allGoodsclass } from '@/api/api'
    import { storeVerifyAccount,storeVerifyMobile,notStoreUser } from '@/api/api'
    import { isMobile,isPhone,isEmail } from '@/utils/validate'
    import { isPrice } from '@/utils/validate'
    import JsEncrypt from 'jsencrypt'
    import security from '@/utils/security.js'
export default {
  data () {
            var validateGtZero = (rule, value, callback) => {
                if (value <0 || value==0) {
                    return callback(new Error('注册资金须大于0'))
                }
                callback()
            }
            // 校验账号是否可用
            var validateAccount = (rule, value, callback) => {
                var obj = {
                params:{
                  account:value
                }
              }
              storeVerifyAccount(obj).then((res)=>{
                if(res.code==200){
                  return callback()
                }else{
                   return callback(new Error(res.msg))
                }
              })
            }
            // 校验手机号
            var validateMobile = (rule, value, callback) => {
               if(!isMobile(value)){
                return callback(new Error("手机格式不正确"))
              }
                var obj = {
                  params:{
                    mobileEmail:value,
                    type:1 ,//1手机号，2是邮箱
                  }
                }
                storeVerifyMobile(obj).then((res)=>{
                  if(res.code==200){
                    return callback()
                  }else{
                    return callback(new Error(res.msg))
                  }
                })
            }
            //  校验邮箱
            var validateE_mail = (rule, value, callback) => {
              if(!isEmail(value)){
                return callback(new Error("邮箱格式不正确"))
              }
                var obj = {
                  params:{
                    mobileEmail:value,
                    type:2 ,//1手机号，2是邮箱
                  }
                }
                storeVerifyMobile(obj).then((res)=>{
                  if(res.code==200){
                    return callback()
                  }else{
                    return callback(new Error(res.msg))
                  }
                })
            }
            return {
                id:"",
                visible:false,
                page1:1,
                limit1:5,
                total1:0,
                dataListLoading:false,
                breaddata: [],
                userList:[],
                dataForm: {
                    account:'',
                    email:"",
                    mobilePhone:"",

                    id:'',
                    userId:'',
                    // account:'', //  账户
                    // password:'',  // 密码
                    // e_mail:'', // 邮箱
                    // mobilePhone:'', // 手机号
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
                    gradeId:"",
                    storeLinkman: "",
                    storeLogo: "",//店铺logo
                    storeName: "",
                    storeType: "",
                    taxpayerPicture: "",//纳税人资格证
                    userId: "",
                    taxpayerId:"",//公司税号
                    
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
                storeGradeList:[],
                dataRule:{
                    // account: [
                    //     { required: true, message: this.$t('validate.required'), trigger: 'blur' },
                    //     { validator: validateAccount, trigger: 'blur' }
                    // ],
                    // password: [
                    //     { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    // ],
                    // e_mail: [
                    //     { required: true, message: this.$t('validate.required'), trigger: 'blur' },
                    //     { validator: validateE_mail, trigger: 'blur' }
                    // ],
                    // mobilePhone: [
                    //     { required: true, message: this.$t('validate.required'), trigger: 'blur' },
                    //     { validator: validateMobile, trigger: 'blur' }
                    // ],
                    userId: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
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
                    gradeId: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                }
            }
    },
    components:{
        imgCropper,
        Bread,
    },
  created(){
    this.getGradeList()
    this.getUserList()
  },
  methods: {
           init(id){
               if(id){
                   this.id = id;
                   this.breaddata = ["商家系统", "商家管理", "商家列表",'商家编辑'];
                    // 回显数据
                    this.backScan();
               }else{
                   this.breaddata = ["商家系统", "商家管理", "商家列表",'新增商家'];
               }
               // 获取树形结构数据
               this.getTreeData();
           },
            getuser(row){
                this.dataForm.userId=row.id
                this.dataForm.userName=row.account
                this.visible=false
            },
            // 获取用户列表
            getUserList(page,limit){
                var obj = {
                    params:{
                        page:page==null? 1 : page,
                        limit:limit==null? 5 : limit,
                        account:this.dataForm.account,
                        mobilePhone:this.dataForm.mobilePhone,
                        email:this.dataForm.email
                        
                    }
                }

                notStoreUser(obj).then(res=>{
                    if(res.code==200){
                        this.userList=res.data.list
                        this.total1=res.data.total
                    }
                })
            },
            closeDialog() {
                this.visible = false;
            },
            userPageList(){
                this.visible=true
                this.dataForm.account=''
                this.dataForm.mobilePhone=''
                this.dataForm.email=''
                if(!this.dataForm.userId){
                    this.page1=1
                }
                this.getUserList(this.page1,this.limit1);
            },
           //  如果是编辑，需要回显数据
           backScan(){
                 //  存放树形结构末级id 
                // this.expandedKeys = ['1191625918657863681']
                // this.checkedKeys  = ['1191625918657863681']
                var obj = {
                    params:{
                      storeId:this.id
                    }
                }
                storeInfo(obj).then(res=>{
                    if(res.code == 200 && res.data){
                        Object.assign(this.dataForm,res.data);
                       this.dataForm.storeType = res.data.storeType+""
                       this.dataForm.id= res.data.storeId
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
                    }
                })
           },
           reset(){
               this.dataForm.account='',
               this.dataForm.mobilePhone='',
               this.dataForm.email=''
               this.getUserList();
           },
            // 创建店铺
            submitStore(){
                if(this.saveLoading){
                    return;
                }
                this.$refs['dataForm'].validate((valid) => {
                    if (valid) {
                        var fn = this.id?storeUpdateStore:addStoreSave
                        this.saveLoading = true
                        var dataForm = cloneDeep(this.dataForm);
                          // 增加用户名与密码 RSA加密
                        //   if(dataForm.password){
                        //     let publicKey = security.publicKey;
                        //     var encrypt = new JSEncrypt();
                        //     encrypt.setPublicKey(publicKey);
                        //     dataForm.password = encrypt.encrypt(this.dataForm.password)
                        //   }
                        
                        fn(dataForm).then(res=>{
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
                // this.uploadPic(val,index);
            },
            // uploadPic(base64,index){
            //     let that = this;
            //     const params = { "imgStr": base64 };
            //     return new Promise(function(resolve){
            //         that.imgLoading[index] = true;
            //         that.$set(that.imgLoading,index,that.imgLoading[index])
            //         uploadPicBase64(params).then(res =>{
            //             that.imgLoading[index] = false;
            //             that.$set(that.imgLoading,index,that.imgLoading[index])
            //             if(res && res.code == "200"){
            //                 var url = res.data.url
            //                 if(index == '0'){
            //                     // 1.店铺logo
            //                     that.dataForm.storeLogo = url;
            //                 }else if(index == '1'){
            //                     //2.法人身份证（正面）
            //                     that.dataForm.idCardNatPicture = url;
            //                 } else if(index == '2'){
            //                     //2.法人身份证（反面）
            //                     that.dataForm.idCardPeoPicture = url;
            //                 }else if(index == '3'){
            //                     //3.三证合一营业执照
            //                     that.dataForm.electronicBusinessLicense = url;
            //                 }else if(index == '4'){
            //                     //4.纳税人资格证
            //                     that.dataForm.taxpayerPicture = url;
            //                 }else{
            //                     // 5.银行开户许可证
            //                     that.dataForm.bankAccountPicture = url;
            //                 }
            //                 resolve("true")
            //             }else {
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
            // 返回上一页
            changePage(){
                this.$emit('addoraditList')
            },
            // 下一步
            nextStep(){
                // console.log(this.$refs.tree.getCheckedNodes());
                // console.log(this.$refs.tree.getCheckedKeys());
                var checkedIds1 =  this.$refs.tree.getCheckedKeys();  // 全选中
                this.dataForm.checkedIds1=checkedIds1
                var checkedIds2 =  this.$refs.tree.getHalfCheckedKeys(); //  半选中
                var checkedIds  = checkedIds1.concat(checkedIds2);
                // this.dataForm.storeClassId = this.$refs.tree.getCheckedKeys();
                this.dataForm.storeClassId = checkedIds;
                console.log(this.dataForm.storeClassId);
                this.$refs['dataForm'].validate((valid) => {
                    if (valid) { 
                        this.shopsIsShow = false
                    }
                })
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
            // 上一步
            preStep(){
                this.checkedKeys=this.dataForm.checkedIds1
                this.shopsIsShow = true;
            },
                 // 分页, 每页条数
      pageSizeChangeHandle1(val) {
          this.page1 = 1
          this.limit1 = val
          this.getUserList(this.page1,this.limit1)
      },
      // 分页, 当前页
      pageCurrentChangeHandle1(val) {
          this.page1 = val
           this.getUserList(this.page1,this.limit1)
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
            width: 150px !important;
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
        width: 100%;
        display: flex;
        justify-content: space-around;
        /deep/ .el-textarea__inner{
            width: 260px !important;
        }
        .el-form-item{
            display: block;
        }
        .el-input{
            width: 260px !important;
        }
        // .el-textarea{
        //     width: 108% !important;
        // }
        // .companyNews{
        //     /deep/ .el-form-item__label{
        //         width: 150px !important;
        //     }
        // }
        // .proofNews{
        //     /deep/ .el-form-item__label{
        //         width: 180px !important;
        //     }
        // }
    }
</style>
