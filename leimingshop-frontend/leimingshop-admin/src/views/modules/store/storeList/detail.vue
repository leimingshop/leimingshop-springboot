<template>
  <div class="storeDetail">
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
    <el-form :inline="true" class="topGapPadding" :model="dataForm" ref="dataForm"  @keyup.enter.native="getDataList()" label-width="102px">
      <div class="formMode">
        <div style="margin-bottom:20px">管理员</div>
        <div class="itemMode">
          <el-form-item prop="account" label="登录账号：">
              <el-input v-model="dataForm.account" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码：">
              <el-input v-model="dataForm.password" type="password" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
         <el-form-item label="电话：">
              <el-input v-model="dataForm.mobilePhone " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <!-- <div class="itemMode">
          <el-form-item label="性别：">
              <el-input v-model="dataForm.sex" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="电话：">
              <el-input v-model="dataForm.mobilePhone " placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="地址：">
              <el-input v-model="dataForm.paramCode" placeholder="" clearable></el-input>
          </el-form-item>
        </div> -->
      </div>
      <div class="formMode">
        <div style="margin-bottom:20px">商家信息:</div>
        <div class="itemMode">
          <el-form-item label="商家ID：" v-if="storeId">
              <el-input v-model="dataForm.id" placeholder="" :disabled="true" clearable></el-input>
          </el-form-item>
          <el-form-item label="logo：">
              <div style="width: 250px;">
                 <img src="~@/assets/img/nopic.png" alt="" v-if="!logo" style=" object-fit: contain;width: 60px;height:60px;border: 1px solid #ccc6c6;">
                 <img v-else :src="$imgDomain + logo" alt="logo" style=" object-fit: contain;width: 60px;height:60px;border: 1px solid #ccc6c6;">
              </div>
          </el-form-item>
          <div style="width:392px"></div>
        </div>
        <div class="itemMode">
          <el-form-item label="商家名称：">
              <el-input v-model="dataForm.saveStoreDTO.storeName" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="店铺等级：">
              <!-- <el-input v-model="dataForm.saveStoreDTO.gradeId" :disabled="true" placeholder="" clearable></el-input> -->
              <el-select v-model="dataForm.saveStoreDTO.gradeId" :disabled="true" clearable  placeholder="请选择">
                <el-option
                    v-for="item in storeGradeList"
                    :key="item.id"
                    :label="item.sgName"
                    :value="item.id">
                </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商户类型：">
              <!-- <el-input v-model="dataForm.saveStoreDTO.storeType" placeholder="" clearable></el-input> -->
              <el-select v-model="dataForm.saveStoreDTO.storeType" :disabled="true" clearable  placeholder="请选择">
                <el-option
                    v-for="item in storeTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="经营范围：">
              <!-- <el-input v-model="dataForm.storeClassId" :disabled="true" placeholder="" clearable></el-input> -->
              <el-select v-model="dataForm.storeClassId" :disabled="true" multiple placeholder="请选择">
                <el-option
                  v-for="item in storeClassList"
                  :key="item.id"
                  :label="item.gcName"
                  :value="item.id">
                </el-option>
              </el-select>
          </el-form-item>
        </div>
      </div>
      <div class="formMode">
        <div style="margin-bottom:20px">公司信息</div>
        <div class="itemMode">
          <el-form-item label="公司名称：">
              <el-input v-model="dataForm.saveStoreAuthDTO.companyName" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="法人姓名：">
              <el-input v-model="dataForm.saveStoreAuthDTO.legalPersonName" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="办公电话：">
              <el-input v-model="dataForm.saveStoreAuthDTO.companyPhone" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="注册地址：">
              <el-input v-model="dataForm.saveStoreAuthDTO.companyAddress" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="办公地址：">
              <el-input v-model="dataForm.saveStoreAuthDTO.companyAddressDetail" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="注册资金：">
              <el-input v-model="dataForm.saveStoreAuthDTO.companyRegisteredCapital " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="公司注册证书：">
              <div style="width: 250px;">
                 <img src="~@/assets/img/nopic.png" alt="" v-if="!electronicBusinessLicense" style=" object-fit: contain;width: 60px;height:60px;border: 1px solid #ccc6c6;">
                 <img v-else :src="$imgDomain + electronicBusinessLicense" alt="img" style=" object-fit: contain;width: 60px;height:60px;border: 1px solid #ccc6c6;">
                 <!-- <a v-if="electronicBusinessLicense" :href="electronicBusinessLicense" download>
                    <el-button>下载附件</el-button>
                </a> -->
              </div>
              
          </el-form-item>
          <el-form-item label="税号：">
              <el-input v-model="dataForm.saveStoreAuthDTO.taxpayerId" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
         <div style="width:392px">
            <el-form-item label="公司简介：">
                <el-input type="textarea" v-model="dataForm.saveStoreDTO.storeIntro" :disabled="true"  placeholder="" clearable></el-input>
            </el-form-item>
          </div>
        </div>
      </div>
      <div class="formMode">
        <div style="margin-bottom:20px">结算信息</div>
        <div class="itemMode">
          <el-form-item label="银行开户名称：">
              <el-input v-model="dataForm.saveStoreAuthDTO.bankAccountName " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="开户行名称：">
              <el-input v-model="dataForm.saveStoreAuthDTO.bankName " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="开户行账号：">
              <el-input v-model="dataForm.saveStoreAuthDTO.bankAccountNumber" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
        </div>
        <div class="itemMode">
          <el-form-item label="支付宝姓名：">
              <el-input v-model="dataForm.saveStoreAuthDTO.alipayName " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="支付宝账号：">
              <el-input v-model="dataForm.saveStoreAuthDTO.alipayAccountNumber" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <div style="width:392px"></div>
        </div>
        <div class="itemMode">
          <el-form-item label="微信姓名：">
              <el-input v-model="dataForm.saveStoreAuthDTO.wechatName " :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <el-form-item label="微信账号：">
              <el-input v-model="dataForm.saveStoreAuthDTO.wechatAccountNumber" :disabled="true" placeholder="" clearable></el-input>
          </el-form-item>
          <div style="width:392px"></div>
        </div>
        <div class="itemMode">
          <el-form-item label="是否启用：">
              <el-radio-group v-model="dataForm.isEnable" :disabled="true">
                <el-radio label="0">启用</el-radio>
                <el-radio label="1">禁用</el-radio>
              </el-radio-group>
          </el-form-item>
        </div>
      </div>
    </el-form>
    <div style="display: flex;justify-content: center;">
        <el-button @click="changePage">返回</el-button>
    </div>
  </div>
</template>

<script>
import {storeNews} from '@/api/api'
import { storeGrade,storeScope } from '@/api/api'
import Bread from "@/components/bread";

export default {
  data () {
    return {
      dataForm: {
        isEnable:'', 
        account:'',
        password:'',
        saveStoreDTO:{},
        saveStoreAuthDTO:{}
      },
      breaddata: ["商家系统", "商户管理", "商户列表",'商家详情'],
      logo:'',
      electronicBusinessLicense:'',
      storeTypes:[
          {value: 1,label: '自营商户'},
          {value: 2,label: '普通商户'}
      ],
      storeId:null,
      storeGradeList:[],
      storeClassList:[]
    }
  },
  created(){
    let obj1 = {
          params:{
              page:1,
              limit:100,
              gcParentId:0
          }
      }
    storeScope(obj1).then((res)=>{
        console.log(res)
        if(res.code == 200 && res.data.list){
          this.storeClassList = res.data.list;//商家经验范围
        }
    })

    let obj2 = {
            params:{
                page:1,
                limit:100,
            }
        }
      storeGrade(obj2).then((res)=>{
          console.log('商家等级',res)
            if(res.code == 200 && res.data.list){
                this.storeGradeList = res.data.list;//商家等级
            }
      })
  },
  components:{
      Bread
	},
  methods: {
    init(id){
		this.$nextTick(()=>{
            if(id){
                var obj  = {
                    id:id
                }
                storeNews(obj).then((res)=>{
                    console.log('详情',res.data)
                    if(res.code == 200){
                        this.logo = res.data.storeDTO.storeLogo;
                        this.electronicBusinessLicense = res.data.storeAuthDTO.electronicBusinessLicense;
                        this.dataForm.storeClassId = res.data.storeClassDTOList.map(item=>{
                            return item.classId
                        })
                        this.storeId = res.data.storeDTO.id;
                        this.dataForm.id = res.data.storeUserDTO.id;
                        Object.assign(this.dataForm, res.data.storeUserDTO);
                        Object.assign(this.dataForm.saveStoreDTO, res.data.storeDTO);
                        if(res.data.storeAuthDTO){
                            Object.assign(this.dataForm.saveStoreAuthDTO,res.data.storeAuthDTO)
                        }
                        this.dataForm.isEnable = JSON.stringify(res.data.isEnable)

                    }
                })
            }
		})
    },
    changePage(){
      this.$emit("showList");
    }
  }
};
</script>
<style lang="scss">
    .storeDetail{
        .formMode{
            padding-top: 30px;
            border-bottom: 20px solid #ECEDF1;
            .el-input{
                width: 250px  !important;
                height: auto !important;
            }
            &:first-child{
                padding-top: 0;
            }
            &:last-child{
                border: none;
            }
            .itemMode{
                padding-left: 20px;
                display: flex;
                justify-content: space-between;
            }
        }
    }
        
</style>
