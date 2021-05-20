<template>
    <div>
        <div  style="margin-left: 2%;margin-right: 2%;" v-if="goodsFreightTemplatePage==false">
            <h3 style="margin-top: 30px;"   class="section">基本信息</h3>
            <el-form
                    class="form-box-class"
                    :model="dataForm"
                    :rules="dataRule"
                    ref="addForm"
                    label-width="120px">
                <el-form-item label="商品类目：">
                    <span>{{dataForm.gcName0}} </span>
                    <span v-if="dataForm.gcName1">> {{dataForm.gcName1}} </span>
                    <span v-if="dataForm.gcName2">> {{dataForm.gcName2}} </span>
                    <el-button v-if="!$route.query.gcId" @click="preStep()"  style="margin-left:20px;">编辑</el-button>
                    <span v-if="dataForm.virtualFlag == 1"  style="color: red;padding-left: 20px;">提示：你选择的分类为虚拟分类，配送方式与实体商品不同，请注意。</span>
                </el-form-item>
                <el-form-item label="商品名称：" prop="goodsName" style="width:45%">
                    <el-input v-model="dataForm.goodsName" maxlength="64"  show-word-limit placeholder="请输入商品名称"></el-input>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'商品名称最长不超过64个汉字，建议格式“品牌+商品名称+型号”'"></promptMessage>

                <el-form-item label="副标题：" prop="goodsSubTitle" style="width:45%;">
                    <el-input v-model="dataForm.goodsSubTitle" maxlength="64"  show-word-limit placeholder="请输入副标题"></el-input>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'副标题可填写商品的简介或特点，最长不能超过64个汉字'"></promptMessage>

                <el-form-item label="售价：" prop="specSellPrice">
                    <el-input-number v-model="dataForm.specSellPrice" :precision="2" :step="0.1" :min="0.01" :max="999999.99"></el-input-number>
                    <span> 元</span>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'该价格为实际售卖价格'"></promptMessage>

                <el-form-item label="成本价：" prop="specCostPrice">
                    <el-input-number v-model="dataForm.specCostPrice"  :precision="2" :step="0.1" :min="0.01" :max="999999.99"></el-input-number>
                    <span> 元</span>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'辅助成本核算，商城各页面不显示'"></promptMessage>

                <el-form-item label="商品重量：" prop="specWeight" v-if="dataForm.goodsSpecSaveDTOList.length ==0">
                  <el-input-number  v-if="dataForm.goodsSpecSaveDTOList.length!=0" v-model="dataForm.specWeight"
                                  :step="1" :min="0"></el-input-number>
                  <el-input-number v-else v-model="dataForm.specWeight" :step="1" :min="0"
                                  :max="999999"></el-input-number>
                  <span> kg</span>
                </el-form-item>

                <el-form-item label="总库存：" prop="specStorage">
                    <el-input-number v-if="dataForm.goodsSpecSaveDTOList.length!=0" v-model="dataForm.specStorage"   :step="1" :min="0"  :disabled="true"></el-input-number>
                    <el-input-number v-else v-model="dataForm.specStorage"   :step="1" :min="0" :max="999999" ></el-input-number>
                    <span> 件</span>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'商品所有规格型号的库存总和，编辑单个规格库存后，总库存自动更新为全部规格库存的总和'"></promptMessage>
                <el-form-item label="商品标签：" prop="labelIds">
                    <el-select
                    class="skuSelect"
                    v-model="dataForm.labelId"
                    remote
                    filterable
                    :remote-method="getTagList"
                    :loading="tagLoading"
                    placeholder="请选择">
                    <el-option
                        v-for="(item,index) in labelOptions"
                        :key="index"
                        :label="item.labelName"
                        :value="item.id">
                    </el-option>
                  </el-select>
                    <!-- <el-select
                            class="skuSelect"
                            v-model="dataForm.labelIds"
                            multiple
                            :multiple-limit='limitNumber'
                            filterable
                            remote
                            reserve-keyword
                            placeholder="请输入关键词"
                            :remote-method="getTagList"
                            :loading="tagLoading">
                        <el-option
                                v-for="(item,index) in labelOptions"
                                :key="index"
                                :label="item.labelName"
                                :value="item.id">
                        </el-option>
                    </el-select> -->
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'正确选择标签有助于提升搜索筛选的准确性，请根据实际情况认真选择'"></promptMessage>

                <el-form-item label="商品品牌：" prop="brandId">
                    <span style="padding:0 10px;">{{dataForm.brandName?dataForm.brandName:"未选择"}}</span>
                    <el-popover
                            placement="bottom"
                            trigger="click">
                        <el-autocomplete
                                v-model="brandName"
                                :fetch-suggestions="getBrandList"
                                placeholder="请输入内容"
                                @select="handleSelect"
                        ></el-autocomplete>
                        <el-button slot="reference" id="brandSelect" type="primary">选择商品品牌</el-button>
                    </el-popover>
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'正确选择品牌有助于提升搜索筛选的准确性，请根据实际情况认真选择'"></promptMessage>

                <el-form-item label="商品属性：" class="arrrtibuteWrap">
                    <!-- <el-input v-model="dataForm.name" maxlength="128" placeholder="请输入内容"></el-input> -->
                    <!-- 下拉形式显示属性 -->
                    <div class="arrrtibuteWrap1" v-if="dataForm.goodsAttributeSaveDTOList && dataForm.goodsAttributeSaveDTOList.length!=0">
                        <div class="attributeItem"  v-for="(item,index) in dataForm.goodsAttributeSaveDTOList" v-if="item.attributeId">
                            <span>{{item.attrName}}：</span>
                            <el-select v-model="item.attrValue"  placeholder="请选择区间">
                                <el-option
                                        v-for="(item2,index) in item.attrValueOpction"
                                        v-if="item2.attrValue"
                                        :key="index"
                                        :label="item2.attrValue"
                                        :value="item2.attrValue">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <!-- 列表形式显示属性 -->
                    <div class="arrrtibuteWrap2">
                        <div   v-for="(item,index) in dataForm.goodsAttributeSaveDTOList" v-if="!item.attributeId" :key="index" style="display: flex;margin-bottom:20px;">
                            <el-form-item label="属性名称：">
                                <el-input v-model="dataForm.goodsAttributeSaveDTOList[index].attrName"  maxlength="20" placeholder="请输入属性名称"></el-input>
                            </el-form-item>

                            <el-form-item label="属性值：" style="margin-left:20px;">
                                <el-input v-model="dataForm.goodsAttributeSaveDTOList[index].attrValue"  maxlength="20"  placeholder="请输入属性名称"></el-input>
                            </el-form-item>

                            <el-button type="primary"  plain @click="clearAttribut(index)" style="margin-left:10px;height: 40px;">清空</el-button>
                            <el-button type="danger"   plain @click="deleteAttributeGroup(index)" style="margin-left:10px;height: 40px;">删除</el-button>
                        </div>

                        <el-form-item>
                            <el-button  type="primary" @click="appendAttributeGroup()" >添加属性值</el-button>
                        </el-form-item>
                    </div>

                    <!-- <div class="attributeBtWrap">
                      <el-button type="primary" @click="saveTemplate()" size="mini">存为模板</el-button>
                      <el-button type="primary" @click="useTemplate()" size="mini">立即使用</el-button>
                      <el-button type="primary" @click="chooseTemplate()" size="mini">选择模板</el-button>
                    </div> -->
                </el-form-item>
                <promptMessage :labelWidth="120" :message="'商品属性会显示在商城商品详情的规格参数中'"></promptMessage>

                <!--  选择规格 和 图片上传 -->
                <h3 style="margin-top: 30px;"  class="section">商品规格</h3>
                <chooseSpec
                        ref="chooseSpecCompon"
                        @setGoodsSpecSaveDTOList="setGoodsSpecSaveDTOList"
                        @setSpecLevel_1="setSpecLevel_1"
                        @setSpecLevel_2="setSpecLevel_2"
                        @setSpecAttributePictureSaveDTOList="setSpecAttributePictureSaveDTOList"
                        @setImgList="setImgList"
                        @setSpecImgList="setSpecImgList"
                        :dataForm="dataForm"
                        >
                </chooseSpec>

                <h3 style="margin-top: 10px;"  class="section">商品详情</h3>
                <el-form-item label="商品详情：" >
                    <goodsDetailH5 ref="goodsDetailH5"></goodsDetailH5>
                </el-form-item>
                 <h3 style="margin-top: 30px;"  class="section">店铺分类</h3>
           <el-form-item  prop="actionParams"  label="店铺商品分类：">
                <el-cascader
                    :options="classDataList"
                    :clearable="true"
                    :props="props"
                    v-model="dataForm.storeGoodsClass"
                    @change="handleChangeOut">
                </el-cascader>
            </el-form-item>
          <!--物流信息-->
          <!-- 配送方式 0:无需物流 1:快递 2自提 3电子提货码 -->
          <h3 style="margin-top: 30px;"  class="section">物流信息</h3>
          <el-form-item v-if="dataForm.virtualFlag==0"  label="配送方式：" >
              <el-radio-group v-model="dataForm.expressFlag">
                  <el-radio :label="0" @change="changeExpressFlag()">无需物流</el-radio>
                  <el-radio :label="1" @change="changeExpressFlag()">快递</el-radio>
                  <!-- <el-radio :label="3" @change="changeExpressFlag(dataForm)">电子提货码</el-radio> -->
              </el-radio-group>
          </el-form-item>
            <el-form-item v-if="dataForm.virtualFlag==0" label="运费承担方式：">
              <el-radio-group   v-model="dataForm.freightBearType" label="运费承担方式" >

                  <el-radio :label="1" :disabled="dataForm.expressFlag!=1" @change="getDefaultFreightTemplate">买家承担</el-radio>
                  <el-radio :label="2" :disabled="dataForm.expressFlag!=1">卖家承担</el-radio><br>
              </el-radio-group><br>
            </el-form-item>
                <el-form-item v-show="dataForm.freightBearType==1&&dataForm.virtualFlag==0" label="运费模板：" >
              <template v-if="dataForm.expressFlag == 1" :loading="freightTemplateLoading">

                  <el-select
                             v-model="dataForm.freightTemplateId"
                             placeholder="请选择"
                             @change="chooseFreightTemplate">
                      <el-option
                              v-for="item in freightTemplateListoptionSet"
                              :key="item.id"
                              :label="item.templateName"
                              :value="item.id">
                      </el-option>
                  </el-select>

                        &nbsp;<el-button  style="margin-bottom: 15px" @click="storeFreightTemplateList()" type="text" size="mini" >刷新</el-button>
                        <el-button type="text" size="mini" @click="goFreightTemplateAdd()">新建</el-button>
                        <el-button @click="collapseFreightTemplateRule" type="text" size="mini">{{this.collapseRule?'收起':'展开'}}</el-button>

                  <el-table  v-show="dataForm.freightTemplateId && this.collapseRule"
                            :data="freightRuleList"
                            stripe
                            border
                            style="width: 90%"
                  >
                      <el-table-column
                              prop="areaDescription"
                              label="可配送区域"
                              width="420px">
                      </el-table-column>
                      <el-table-column
                              prop="firstFee"
                              :label='this.feiType==0?"首件（个）":"首重(kg)"'
                              align="center"
                              width="134px">
                      </el-table-column>
                      <el-table-column
                              prop="firstAmount"
                              label="运费（元）"
                              align="center"
                              width="140px">
                      </el-table-column>
                      <el-table-column
                              prop="additionalFee"
                              :label='this.feiType==0?"续件（个）":"续重(kg)"'
                              align="center"
                              width="140px">
                      </el-table-column>
                      <el-table-column
                              prop="additionalAmount"
                              label="续费（元）"
                              align="center"
                              width="140px">
                      </el-table-column>
                  </el-table>
              </template>
          </el-form-item>
          <!-- *************************虚拟商品 start ****************************** -->
          <!-- 配送方式 0:无需物流 1:快递 2自提 3电子提货码 -->
          <el-form-item v-if="dataForm.virtualFlag==1"  label="配送方式：">
              <el-radio-group v-model="dataForm.expressFlag" >
                  <el-radio :label="3">电子提货码</el-radio>
                  <el-radio :label="0">无需物流</el-radio>
                  <!-- <el-radio :label="1" disabled @change="getDefaultFreightTemplate">买家承担</el-radio>
                  <el-radio :label="2" disabled >卖家承担</el-radio><br> -->
              </el-radio-group><br>
          </el-form-item>
          <!-- <el-form-item v-if="dataForm.virtualFlag==1" label="虚拟配送方式：">
             <el-radio v-model="dataForm.expressFlag" :label="3" >无需配送</el-radio>
          </el-form-item> -->
          <el-form-item style="padding-left: 0px;" v-if="dataForm.virtualFlag==1" label="提货码有效期：" >
                 <el-radio-group v-model="expirationDay" @change="chageCodeRefundFlag">
                      <el-radio :label="0">领取后
                        <el-input-number size="mini"  v-model="dataForm.codeValidDay" :precision="0" :step="1" :min="1" :disabled="expirationDay==-1" placeholder="请输入天数"  style="width: 100px;"/>天
                      </el-radio>
                     <el-radio :label="-1" >永久有效</el-radio>
                 </el-radio-group>
            </el-form-item>
          <el-form-item v-if="dataForm.virtualFlag==1" label="提货码过期退款：">
             <el-radio v-model="dataForm.codeRefundFlag" :label="1" :disabled="expirationDay==-1">支持</el-radio>
             <el-radio v-model="dataForm.codeRefundFlag" :label="0" :disabled="expirationDay==-1">不支持</el-radio>
          </el-form-item>
          <!-- *************************虚拟商品 end ****************************** -->
            <h3 style="margin-top: 30px;"  class="section">售后说明</h3>
              <el-form-item label="售后说明模板：" prop="afterSale"> <!-- prop="afterSale" -->
                 <el-select
                    v-model="afterSale"
                    placeholder="请选择"
                    :loading="afterTemplateLoading"
                    loading-text="加载中···"
                    @change="chooseAfterTemplate(2)">
                    <el-option
                      v-for="item in aftertemplateListoption"
                      :key="item.id"
                      :label="item.name"
                      :value="item.value">
                    </el-option>
                </el-select>
               <!-- <quill-editor style=" margin-top: 12px;" ref="myTextEditor" v-model="dataForm.afterSale" :options="editorOption"></quill-editor> -->
                <quill-editor-img @artmessageContent='artmessageContent' ref="myTextEditor" style="margin-right: 10%"></quill-editor-img>
            </el-form-item>
             <promptMessage :labelWidth="120" :message="'售后说明会显示在商城商品详情的售后说明中'"></promptMessage>
      


      <h3 style="margin-top: 30px;" class="section">商品设置</h3>

      <div class="error-box">
        <el-form-item  label="是否开具发票：" prop="invoiceFlag">
          <el-radio-group v-model="dataForm.invoiceFlag"  @change="updateInvoice(dataForm.invoiceFlag)">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <div class="hrTips">控制用户购买商品时，是否可以申请发票。</div>
        <el-form-item  label="发票类型选择：" prop="invoiceType">
          <el-checkbox-group v-model="dataForm.invoiceType"  @change="updateContent(dataForm.invoiceType)">
            <el-checkbox label="1" :disabled=invoiceTypeDisabled>电子发票</el-checkbox>
            <el-checkbox label="2" :disabled=invoiceTypeDisabled>纸质普票</el-checkbox>
            <el-checkbox label="3" :disabled=invoiceTypeDisabled>增值税专项发票</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <div class="hrTips">控制用户购买商品时，可以选择的发票内容。</div>
        <el-form-item  label="发票内容：" prop="invoiceContent">
          <el-checkbox-group v-model="dataForm.invoiceContent">
            <el-checkbox label="1" :disabled='invoiceContentDisabled || invoiceTypeDisabled'>商品明细</el-checkbox>
            <el-checkbox label="2" :disabled='invoiceContentDisabled || invoiceTypeDisabled'>商品类别</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <div class="hrTips">控制用户购买商品时，可以申请的发票种类。</div>
        <!-- <el-form-item label="商品发布：">
        <el-radio-group v-model="dataForm.goodsType" class="radioGroup">
          <el-radio :label="0">立即发布</el-radio>
          <br>
          <el-radio :label="1">定时发布</el-radio>
          <el-date-picker
              :disabled="dataForm.goodsType==0"
              style="margin-left:8px;"
              v-model="dataForm.shelfTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期时间"
              :picker-options="pickerOptions"
              @change="afterTime">
          </el-date-picker>
        </el-radio-group>
      </el-form-item> -->
      </div>
            </el-form>
            <div class="nextStepWrap">
                <el-button class="nextStep" @click="preStep()" >取消</el-button>
                <el-button class="nextStep" type="primary"  @click="dataFormSubmit('addForm',0)"  v-loading="saveLoading"  v-if="$hasPermission('sys:goods:save')">{{saveLoading?"保存中...":"立即发布"}}</el-button>
                <!-- <el-button class="nextStep" type="primary"  @click="dataFormSubmit('addForm',1)"  v-loading="saveLoading"  v-if="$hasPermission('sys:goods:save')">{{saveLoading?"保存中...":"定时发布"}}</el-button> -->
                <el-button class="nextStep" type="primary"  @click="timedReleaseDialog = true"  v-loading="saveLoading"  v-if="$hasPermission('sys:goods:save')">{{saveLoading?"保存中...":"定时发布"}}</el-button>
                <el-button class="nextStep" type="primary"  @click="dataFormSubmit('addForm',2)"  v-loading="saveLoading"  v-if="$hasPermission('sys:goods:save')">{{saveLoading?"保存中...":"保存草稿"}}</el-button>
            </div>

            <nav style="position:fixed;right:30px;top:300px;">
                <div class="rightWarp">
                    <div  class="nav1" v-for="(item, index) in navList" :key="index" :class="index==0?'current':''">
                        <div class="bgCircle">
                            <div class="smallCircle"></div>
                        </div>
                        <a @click="jump(index)" >{{item}}</a>
                    </div>
                </div>
            </nav>

        </div>
        <el-dialog
          title="设置定时发布"
          :visible.sync="timedReleaseDialog"
          width="300px">
          <div style="text-align: center;">
              <span>定时发布：</span>
              <el-date-picker
                style="margin-left:8px;"
                v-model="dataForm.shelfTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间"
                :picker-options="pickerOptions"
                @change="afterTime">
            </el-date-picker>
          </div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="timedReleaseDialog = false">取 消</el-button>
            <el-button class="nextStep" type="primary"  @click="dataFormSubmit('addForm',1)"  v-loading="saveLoading"  v-if="$hasPermission('sys:goods:save')">{{saveLoading?"保存中...":"保存"}}</el-button>
            <!-- <el-button type="primary" @click="dataFormSubmit('addForm',1)">确 定</el-button> -->
          </span>
        </el-dialog>
        <addOrEditFreightTemplate :inline="true" ref="addOrEditFreightTemplateCompon" v-if="goodsFreightTemplatePage==true" @changePage="changePage"></addOrEditFreightTemplate>
    </div>

</template>

<script>

import chooseSpec from "./editSpecModules/chooseSpec.vue"
//标签下拉
import {tagAllList} from '@/api/api'
import cloneDeep from 'lodash/cloneDeep'
// 属性下拉
import {attributeList} from '@/api/api'
// 品牌下拉
import {goodsclassBrandById,brandList} from "@/api/api.js";
// 规格下拉
import { specList } from "@/api/api.js";
// 根据分类id查询品牌，规格，
import { goodsclassById } from "@/api/api.js";
// 属性值回显
import { backScanAttribute } from "@/api/api.js";
// import { backScanAttributegroup } from "@/api/api.js";
// 二级规格回显
import {backScanSpec} from '@/api/api'
import {addGoods,updateGoods} from "@/api/api.js";
// 模板下拉列表
import {aftertemplateList} from "@/api/api.js";
//  校验规格货号
import {validGoodsSeria} from "@/api/api.js";
// 运费模板列表
import {storeFreightTemplateList,storeGoodsclass} from "@/api/api.js";
// 店铺发票设置信息
import {getStoreInvoiceSetting} from "@/api/api.js";

import { setTimeout } from 'timers';
import { type } from 'os';
import addOrEditFreightTemplate from '../../mgfreight/template/addOrEdit'

	// import { quillEditor } from "vue-quill-editor"; //调用编辑器
	// import 'quill/dist/quill.core.css';
	// import 'quill/dist/quill.snow.css';
	// import 'quill/dist/quill.bubble.css';
	import quillEditorImg from "@/components/quillEditor"
	import promptMessage from "@/components/prompt/promptMessage"


	import goodsDetailH5 from './goodsDetailModules/goodsDetailH5.vue'
	import {constants} from 'crypto';

export default {
  data () {
    var  validatorValue = (rule, value, callback) => {
      var value2 = value.replace(/<\/p>/g,"").replace(/<p>/g,"").replace(/&nbsp;/g,"")
				value2 = value2.replace(/ /g,"")
        console.log(value2.length)
        if (value2.length>800) {
          callback(new Error('模板内容长度限制为800个汉字'))
        }else {
          callback()
        }
    }
      return {
          timedReleaseDialog:false,
          invoiceContentDisabled: false,
          invoiceTypeDisabled: false,
          props: {
              label: 'gcName',
              value: 'id'
          },
          defaultFreightTemplateId: '',
          storeGcId1: '',
          feiType:0,
          storeGcId2: '',
          storeGcName1: '',
          storeGcName2: '',
          limitNumber: 3,
          tagLoading: false,
          scroll: '',
          navList: ["基本信息", "商品规格", "上传图片", "商品详情", "物流信息", "售后说明"],
          saveLoading: false,
          breaddata: ["商品", "商品管理", "商品添加"],
          showPageNumber: 1,
          afterSale: '',//用户模板select用
          brandName: '',//用于搜索用
          labelOptions: [],//标签
          expirationDay: 0,
          classDataList: [],
          dataForm: {
              gcId: "",//1135755925667115009
              goodsSerial: new Date().getTime(),
              gcName: '',
              gcId0: "",
              gcId1: "",
              gcId2: "",
              gcName0: '',
              gcName1: '',
              gcName2: '',
              goodsName: '',//商品名称
              goodsSubTitle: '',
              specSellPrice: 0,
              specCostPrice: 0,
              specWeight:0.00,
              specStorage: 0,
              brandId: "",
              brandName: '',
              goodsClassSpecDTO:[],
              expressFlag: '',
              freightBearType: '',
              codeValidDay: '',
              codeRefundFlag: '',
              freightTemplateId: '',
              invoiceFlag: '',
              invoiceType: [],
              invoiceContent: [],
              labelId: '',

              goodsAttributeSaveDTOList: [ //属性组
                  //  {
                  //     "attrName":"",
                  //     "attrValue":"",
                  //     "attributeId":0,
                  //     "goodsId":0
                  //   },
              ],
              specAttributeAndValueSaveDTOList: [],
              goodsSpecSaveDTOList: [],
              specLevel_1: [],//规格table
              specLevel_2:[],
              imgList:[],
              specImgList:[],
              specImgList1:[],
              specAttributePictureSaveDTOList: [],//规格对应的图片
              mobileBody: "",//商品详细内容(手机版) ,
              afterSale: '',//售后说明 ，提交后端用
              goodsType: 0,//商品发布类型 0:立即发布; 1:定时发布 ,
              shelfTime: new Date(), //发布时间 ,
              labelIds: [],
              // 编辑时需要多穿
              specAttributeModifyIds: [], //移除规格属性数组 ,
              specAttributeAndValueModifyIds: [],//移除规格属性值数组 ,

          },

          // {
          //   "id": 0,//主键 ,
          //   "goodsSerial": 0,//商品货号 ,
          //   "storeId": 0,//店铺ID ,
          //   "storeName": "string"//店铺名称
          //   "goodsStorePrice": 0,//商品店铺价格 ,

          //  "gcId": 0,商品分类id ,
//   "gcName": "string",商品分类名称 ,
//   "goodsName": "string",商品名称 ,
//   "goodsSubTitle": "string",//商品副标题 ,
//   "brandId": 0,商品品牌id
//   "brandName": "string",品牌名称 ,
//    //商品属性集合 ,
//   "goodsAttributeSaveDTOList": [
//     {
//       "attrName": "string",//属性名称
//       "attrValue": "string",//属性值（多个属性值之间用逗号隔开） ,
//       "attributeId": 0,//属性ID
//       "goodsId": 0//商品ID
//     }
//   ],
//   // 商品规格属性和规格值保存对象
//   "specAttributeAndValueSaveDTOList": [
//     {
//       "addType": 0,//修改操作填写 是否新增 0:原有; 1:新增属性值 2:新增属性和属性值 ,
//       "goodsId": 0,//商品ID ,
//       "isMain": 0,//是否为主规格（默认0否，1是） ,
//       "isSelect": 0,// 是否选中（0未选中，默认1选中） ,
//       "specAttrId": 0,//规格属性ID ,
//       "specAttrName": "string",//规格属性名称 ,
//       "specAttrValue": "string",//规格属性值名称 ,
//       "specAttrValueId": 0,//规格属性值ID ,
//       "tempSpecAttrValueId": 0 //后台使用
//     }
//   ],
//   //商品规格集合 ,表格
//   "goodsSpecSaveDTOList": [
//     {
//       "goodsId": 0,//商品ID ,
//       "id": 0,//主键id ,
//       "specAttrName": "string",//商品规格属性值名称（中间用逗号隔开） ,
//       "specAttributeRelationSaveDTOList": [
//         {
//           "isMain": 0,
//           "specAttrId": 0,
//           "specAttrValueId": 0
//         }
//       ],
//       "specCostPrice": 0,//成本价 ,
//       "specMainPicture": "string",//商品规格主图 ,
//       "specName": "string",//商品规格名称 ,
//       "specSellPrice": 0,//销售价 ,
//       "specSerial": 0,//商品规格编号 ,
//       "specStorage": 0//规格库存
//     }
//   ],
//    //商品规格属性和规格值集合 ,//图片
//   "specAttributePictureSaveDTOList": [//商品图片集合 ,
//     {
//       "isMainPicture": 0,//是否主图（默认0否，1是） ,
//       "pictureUrl": "string",//图片地址
//       "sort": 0,//排序
//       "specAttrId": 0,//商品规格属性ID ,
//       "specAttrValueId": 0//商品规格属性值ID
//     }
//   ],

//   "goodsBoby": "string",//商品详细内容(pc) ,
//   "mobileBody": "string",//商品详细内容(手机版) ,
//   "afterSale": "string",售后服务 ,
//   "goodsType": //0,商品发布类型 0:立即发布; 1:定时发布 ,
//   "shelfTime": "string",//发布时间
// }
          attributeList: [
              {label: "价格区间", option: 'priceOption'},
              {label: "口碑", option: 'mouthWordOption'},
              {label: "配送", option: 'DeliveryOption'},
              {label: "产地", option: 'placeOriginOption'},
          ],
          priceOption: [],//价格
          mouthWordOption: [],//口碑
          DeliveryOption: [],//配送
          placeOriginOption: [],//产地
          attributeValueDTOItem: {
              "attrName": "",
              "attrValue": "",
              "attributeId": "",
              "goodsId": null
          },
          skuLoading: false,//属性loading
          skuOptions: [],//属性下拉列表,
          editorOption: {
              placeholder: '请输入您要编辑的内容'
          },
          afterTemplateLoading: false,
          freightTemplateLoading: false,
          collapseRule: true,
          aftertemplateListoption: [],//售后模板下拉
          freightTemplateListoption: [], //物流模板下拉
          freightTemplateListoptionSet:[],
          goodsFreightTemplatePage: false,
          freightRuleList: [],
          pickerOptions: {
              disabledDate(time) {
                  return time.getTime() < Date.now() - 8.64e7;
              }
          },// 日期组件 设置项
          dataRule: {
              goodsName: [
                  {required: true, message: '商品名称不能为空', trigger: 'blur'},
              ],
              goodsSubTitle: [
                  {required: true, message: '副标题不能为空', trigger: 'blur'},
              ],
              specSellPrice: [
                  {required: true, message: '出售价不能为空', trigger: 'blur'},
                  // {
                  //       validator: function (rule, value, callback) {
                  //           if (Number(value)<0.01) {
                  //               callback(new Error('售价不能为0'))
                  //           }else {
                  //               callback()
                  //           }
                  //       }, trigger: 'change'
                  //  }
              ],
              specCostPrice: [
                  {required: true, message: '成本价不能为空', trigger: 'blur'},
                  // {
                  //       validator: function (rule, value, callback) {
                  //           if (Number(value)<0.01) {
                  //               callback(new Error('成本价不能为0'))
                  //           }else {
                  //               callback()
                  //           }
                  //       }, trigger: 'change'
                  //  }
              ],
              specStorage: [
                  {required: true, message: '库存不能为空', trigger: 'blur'},
              ],
              afterSale: [//售后说明
                  // { required: true, message: '售后模板不能为空', trigger: 'blur' },
                  {validator: validatorValue, trigger: 'blur'}
              ],
              invoiceFlag: [
                  {required: true, message: '必选项不能为空', trigger: 'change'},
              ],
              // invoiceType: [
              //     {required: true, message: '必选项不能为空', trigger: 'change'},
              // ],
              // invoiceContent: [
              //     {required: true, message: '必选项不能为空', trigger: 'change'},
              // ],
          },
      };
  },
  watch: {
    // 'dataForm.goodsName' (val) {
    //   this.composeTableDataDelay();
    // },
    // scroll: function () {
    //   this.loadSroll()
    // }
  },
    //组件激活后
    activated(){
        this.$refs.addForm.clearValidate()
    },
  components: {
      // quillEditor,
      chooseSpec,
      goodsDetailH5,
      quillEditorImg,
      promptMessage,
      addOrEditFreightTemplate
  },
  created () {

       // 获取属性下拉列表
        // this.getAttributeList();
        // this.getGoodsclassById();
        this.getTagList();
      // 获取售后下拉列表
        this.aftertemplateListPull();
        this.jnn();

        // window.addEventListener('scroll', this.dataScroll);
  },
  mounted(){
      this.$nextTick(()=>{
          document.getElementsByClassName("aui-content__wrapper")[0].onscroll = this.dataScroll
          setTimeout(()=>{
             document.getElementsByClassName("aui-content__wrapper")[0].scrollTop = 0
          },301)
      })
  },
  methods: {
      setTimedRelease(){
         this.timedReleaseDialog = true
      },
      updateInvoice(flag){
				if(flag==1){
					this.invoiceTypeDisabled=false;
				}else{
					this.invoiceTypeDisabled=true;
				}
      },
      updateContent(flag){
        this.invoiceContentDisabled=false
        for(var i=0;i< flag.length;i++){
					if(flag[i]=="3"){
            // this.dataForm.invoiceContent="2"
            this.dataForm.invoiceContent=[];
						this.dataForm.invoiceContent.push("1");
						this.invoiceContentDisabled=true
					}
        }
			},
      // getTagAllList(query){
      //     var obj = {
      //       params: {
      //         labelName: query
      //       }
      //     }
      //     this.tagLoading = true;
      //     tagAllList(obj).then((res) => {

      //       this.tagLoading = false;
      //       var labelOptions = [];

      //       if (res.code == 200 && res.data) {
      //         labelOptions = res.data;
      //       }
      //     var arr = [...labelOptions]
      //     // js中数组对象去重的方法
      //     var obj = {};
      //     arr = arr.reduce(function(item, next) {
      //       obj[next.id] ? '' : obj[next.id] = true && item.push(next);
      //       return item;
      //     }, []);
      //     this.labelOptions = arr;
      //   })
      // },
      getTagList(){
        this.tagLoading  = true
         tagAllList().then((res) => {
            this.tagLoading = false;
            this.labelOptions = res.data
         })
      },
      chageCodeRefundFlag(){
        if(this.expirationDay==-1){
          this.dataForm.codeRefundFlag = 1

        }
      },
      init(dataForm){
          console.log(dataForm);
          console.log(this.dataForm);
          Object.assign(this.dataForm,dataForm);
          if(dataForm.goodsSpecDetailDTOList && dataForm.goodsSpecDetailDTOList.length==1){
            this.dataForm.specWeight=dataForm.goodsSpecDetailDTOList[0].specWeight
          }
          if (this.dataForm.virtualFlag ==1 && this.dataForm.codeValidDay==-1){
              this.expirationDay = this.dataForm.codeValidDay
          }
          console.log("dffsdfsdf ",this.dataForm.expressFlag)
          if(this.dataForm.expressFlag === '') {
              console.log("zhecijinlaile ")
              if (this.dataForm.virtualFlag == 0) {
                  this.dataForm.expressFlag = 1
                  this.dataForm.freightBearType = 2
              } else if (this.dataForm.virtualFlag == 1) {
                  this.dataForm.expressFlag = 3
                  this.dataForm.codeValidDay = 1
                  this.dataForm.codeRefundFlag = 1
              } else {
                  this.dataForm.expressFlag = 0
              }
              if (this.dataForm.codeValidDay == -1) {
                  this.expirationDay = -1;
                  this.dataForm.codeValidDay = '';
              } else {
                  this.expirationDay = 0;
              }
          }
          // 获取属性下拉列表
          // this.getAttributeList();
          this.$nextTick(()=>{
              this.storeFreightTemplateList().then(() => {
                  if(!dataForm.id){//如果是新增商品，需要根据分类id查数据
                      this.getGoodsclassById();
                      this.getInvoiceSetting();
                  }else{//商品编辑
                      //  回显属性
                      this.backScanEditAttribute();
                      // 回显规格
                      this.backScanEditSpec();
                      // 回显商品详情
                      this.backScanGoodDetail();
                      // 回显售后模板
                      this.backScantAfterSale();
                      //回显标签数据
                      this.backLabelName();
                      this.updateInvoice(this.dataForm.invoiceFlag);
                      this.updateContent(this.dataForm.invoiceType);
                  }
              });

          })
        // this.getTagAllList()
      },
       // 页面滚动右侧锚点代码-----------------------------------------------------------------------------------------
      dataScroll: function () {
          // this.scroll = document.documentElement.scrollTop || document.body.scrollTop;
        this.scroll = document.getElementsByClassName("aui-content__wrapper")[0].scrollTop
        this.loadSroll();
      },
      jump(index) {
          let jump = document.getElementsByClassName('section');
          // 获取需要滚动的距离
          let total = jump[index].offsetTop-30;
           document.getElementsByClassName("aui-content__wrapper")[0].scrollTop = total
          // // Chrome
          // document.body.scrollTop = total;
          // // // Firefox
          // document.documentElement.scrollTop = total;
          // // Safari
          // window.pageYOffset = total;
          // $('html, body').animate({
          // 'scrollTop': total
          // }, 400);
      },
      // 新建运费模板
      goFreightTemplateAdd(){
          this.goodsFreightTemplatePage = true;
          this.$nextTick(()=>{
               this.$refs.addOrEditFreightTemplateCompon.init("", 1);
          })
          this.$emit("showPageNumberFn",2, "", true);
      },
      getDefaultFreightTemplate() {
          if (this.dataForm.freightTemplateId=='') {
              this.freightTemplateListoption.forEach((item, index) => {
                  if (item.defaultFlag == 1) {
                      this.dataForm.freightTemplateId = item.id
                      this.chooseFreightTemplate(this.dataForm.freightTemplateId)
                  }

              })
          }
      },
      changePage() {
        if (this.goodsFreightTemplatePage == true) {
            this.goodsFreightTemplatePage = false;
            this.$emit("showPageNumberFn", 2, "", false);
            console.log("跳转回来了")

            console.log("跳转回来了")
            this.dataForm.specImgList1=cloneDeep(this.dataForm.specImgList)
            // 新建跳转回来执行
            //  this.backScanSpecFn()
            this.$nextTick(() => {
                // 跳转回来图片回显
                this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList = this.dataForm.specImgList1;
                if (this.dataForm.specLevel_1 != '') {
                    this.$refs.chooseSpecCompon.specLevel_1 = this.dataForm.specLevel_1;
                }if (this.dataForm.specLevel_2!='') {
                    this.$refs.chooseSpecCompon.specLevel_2 = this.dataForm.specLevel_2;
                }
                if (this.dataForm.specLevel_1==''){
                    this.backScanSpecFn(this.dataForm.goodsClassSpecDTO)
                }
                if (this.dataForm.imgList!=''){
                    this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList = this.dataForm.imgList;
                }
                if (this.dataForm.mobileBody) {
                    this.$refs.goodsDetailH5.imgList = JSON.parse(this.dataForm.mobileBody);
                }
                this.$refs.myTextEditor.dataForm.messageContent = this.afterSale;
            })
        } else {
            this.goodsFreightTemplatePage = true;
            this.$emit("showPageNumberFn",2, "", true);
        }
      },
      // 路由跳转
      goRouter(argu,queryObj){
          if(!queryObj){
              this.$router.push({
                  path: argu
              })
          }else{
              var obj = {}
              queryObj.forEach((item,index)=>{
                  obj[item.key] = item.value
              })
              this.$router.push({
                  path: argu,
                  query:obj
              })
          }
      },
      loadSroll: function () {
          var self = this;
          var $navs = $(".nav1");
          var sections = document.getElementsByClassName('section');
          for (var i = sections.length - 1; i >= 0; i--) {
            if (self.scroll >= sections[i].offsetTop - 200) {
              $navs.eq(i).addClass("current").siblings().removeClass("current")
              break;
            }
          }
      },
      // 新建回显----------------------------------------------------------------------------------------------------
      //新建商品，回显商家设置的发票信息
      getInvoiceSetting(){
          getStoreInvoiceSetting().then((res)=>{
              this.dataForm.invoiceFlag = res.data.invoiceFlag?res.data.invoiceFlag+'':'0';
              this.dataForm.invoiceType = res.data.invoiceType?res.data.invoiceType.split(","):[];
              if(this.dataForm.invoiceFlag=="0"){
                  this.invoiceContentDisabled = true
                  this.invoiceTypeDisabled = true
              }
              this.dataForm.invoiceType.indexOf("3")>=0?this.invoiceContentDisabled = true:this.invoiceContentDisabled=false
              this.dataForm.invoiceContent = res.data.invoiceContent?res.data.invoiceContent.split(","):[];
              // this.invoiceSetting = res.data;
          })
      },
      // // 根据分类id查询品牌，规格，
      getGoodsclassById(){
        if(!this.dataForm.gcId){
          this.preStep();
        }
        var obj = {
          params:{
            gcId:this.dataForm.gcId,
            gcName:this.dataForm.gcName,
          }
        }
        goodsclassById(obj).then((res)=>{
					 if(res.code == 200){
              this.dataForm.brandId =  res.data.brandId;
              this.dataForm.brandName =  res.data.brandName;
              this.dataForm.gcIdpath =  res.data.gcIdpath;
              this.dataForm.gcParentId =  res.data.gcParentId;
              this.dataForm.gcSort =  res.data.gcSort;
              this.dataForm.goodsClassSpecDTO=res.data.goodsClassSpecDTO;
              this.backScanAttributeFn(res.data.goodsClassAttrDTO);
              this.backScanSpecFn(res.data.goodsClassSpecDTO);
					 }
				})
			},
			// 属性回显
			backScanAttributeFn(goodsClassAttrDTO) {
				let that = this;
				if (goodsClassAttrDTO) {
					this.dataForm.goodsAttributeSaveDTOList = [];
					goodsClassAttrDTO.forEach((item, index) => {
						that.dataForm.goodsAttributeSaveDTOList.push({
							"attrName": item.attrName,
							// "attrValue":item.attrName,
							"attributeId": item.attrId,
							"goodsId": 0,//tudo
							// "attrValueOpction":attrValueOpction
						});
						this.backScan(item, index);
						// this.dataForm.goodsAttributeSaveDTOList.push({
						//   "attrName":item.attrId,
						//   "attrValue":item.attrName,
						//   "attributeId":item.id,
						//   "goodsId":0,//tudo
						//   // "attrValueOpction":
						// });
					})
				}
			},
			// 编辑回显
			backScan(item, index) {
				let that = this;
				var obj = {
					id: item.attrId,
				}
				backScanAttribute(obj).then((res) => {
					if (res.code == 200) {
						// 属性单独处理回显
						var attrValueOpction = [];
						res.data.attributeValueDTOList.forEach((item2, index2) => {
							attrValueOpction.push(
								{attrValue: item2.attrValueName}
							);
						})
						that.dataForm.goodsAttributeSaveDTOList[index].attrValueOpction = attrValueOpction
						that.dataForm.goodsAttributeSaveDTOList = [].concat(that.dataForm.goodsAttributeSaveDTOList)
					}
				})
      },
      backLabelName(){
          if(this.dataForm.labelIds&&this.dataForm.labelIds.length>0){
              this.dataForm.labelId = this.dataForm.labelIds[0]
          }
      },
			// 规格回显
			backScanSpecFn(goodsClassSpecDTO) {

				this.$refs.chooseSpecCompon.specLevel_1 = [
					{specId: "10086", name: '颜色', checked: false},
				];
				this.$refs.chooseSpecCompon.specLevel_2 = [
					[
						{id: "1008601", specId: "10086", name: '红色', checked: false},
						{id: "1008602", specId: "10086", name: '橙色', checked: false},
						{id: "1008603", specId: "10086", name: '黄色', checked: false},
						{id: "1008604", specId: "10086", name: '绿色', checked: false},
						{id: "1008605", specId: "10086", name: '青色', checked: false},
						{id: "1008606", specId: "10086", name: '粉色', checked: false},
						{id: "1008607", specId: "10086", name: '蓝色', checked: false},
						{id: "1008608", specId: "10086", name: '紫色', checked: false},
						// { id:"1008609",specId:"10086",name:'粉色',checked:false},
						{id: "1008609", specId: "10086", name: '黑色', checked: false},
						{id: "1008610", specId: "10086", name: '白色', checked: false},
						{id: "1008611", specId: "10086", name: '棕色', checked: false},
					]
				];
				goodsClassSpecDTO.forEach((item, index) => {
					// 回显规格一级
					this.$refs.chooseSpecCompon.specLevel_1.push(
						{
							// id:item.id,
							specId: item.specId,
							name: item.specName,
							checked: false
						}
					)
					this.$refs.chooseSpecCompon.specLevel_2.push([]);
					var obj = {id: item.specId}
					// 回显规格二级
					backScanSpec(obj).then((res) => {
						if (res.code == 200) {
							if (res.data.specValueDTOList) {
								res.data.specValueDTOList.forEach((item2, index2) => {
									this.$refs.chooseSpecCompon.specLevel_2[index + 1].push(
										{
											id: item2.id,//红色id
											specId: item2.specId,//颜色id
											name: item2.specValueName,
											checked: false
										},
									);
								})
							}
						}
					})
					// console.log("000000000000000000000000000000000000");
					// console.log(this.$refs.chooseSpecCompon.specLevel_2);
				})
			},
			// 编辑回显----------------------------------------------------------------------------------------------------
			// 回显编辑属性
			backScanEditAttribute() {
				this.dataForm.goodsAttributeSaveDTOList = this.dataForm.goodsAttributeDTOList;
				this.dataForm.goodsAttributeSaveDTOList.forEach((item, index) => {
					if (item.attributeId) {
						backScanAttribute({id: item.attributeId}).then((res) => {
							// 属性单独处理回显
              var attrValueOpction = [];
              if(res.data){
                res.data.attributeValueDTOList.forEach((item2, index2) => {
                  attrValueOpction.push(
                    {attrValue: item2.attrValueName}
                  );
                })
              }
							item.attrValueOpction = cloneDeep(attrValueOpction);
							this.$set(this.dataForm.goodsAttributeSaveDTOList, index, this.dataForm.goodsAttributeSaveDTOList[index]);
						})

					}

				})

				// delete this.dataForm.goodsAttributeDTOList;
			},
			// 回显规格
			backScanEditSpec() {
				//规格数据 ************************************
				// specAttributeDetailDTOList
				var specAttributeDetailDTOList = cloneDeep(this.dataForm.specAttributeDetailDTOList);
				this.$refs.chooseSpecCompon.specLevel_1 = [];
				this.$refs.chooseSpecCompon.specLevel_2 = [];
				specAttributeDetailDTOList.forEach((item, index) => {
					// 回显规格一级
					this.$refs.chooseSpecCompon.specLevel_1.push(
						{
							// id:item.id,
							specId: item.id,
							name: item.specAttrName,
							// isMain:item.isMain,
							checked: false
						}
					);
					// 回显规格二级
					this.$refs.chooseSpecCompon.specLevel_2.push([]);
					item.specAttributeValueDetailDTOList.forEach((item2, index2) => {
						// 如果有规格值选中，那么规格也要选中
						if (item2.isSelect) {
							this.$refs.chooseSpecCompon.specLevel_1[index].checked = true
						}
						console.log("编辑规格回显数据", [index2, item2.specAttrValueId, item.id]);
						this.$refs.chooseSpecCompon.specLevel_2[index].push(
							{
								id: item2.specAttrValueId,//红色id
								specId: item.id,//颜色id
								name: item2.specAttrValue,
								checked: item2.isSelect ? true : false,
								// isSelect:item2.isSelect
							},
						);
					})
				})
				delete this.dataForm.specAttributeDetailDTOList;

				// 表格数据************************************
				// goodsSpecDetailDTOList
				var goodsSpecDetailDTOList = cloneDeep(this.dataForm.goodsSpecDetailDTOList);
				this.$refs.chooseSpecCompon.goodsSpecSaveDTOList = [];
				goodsSpecDetailDTOList.forEach((item, index) => {
					if (item.specAttributeValueDetailDTOList) {
						var obj = item.specAttributeValueDetailDTOList.find((item2) => {
							return item2.isMainValue == 1
						})
						console.log(obj);
						if (obj) {
							item.img_use_id = obj.specAttrValueId;
						}

					}
					if (specAttributePictureDetailDTOList) {
						var obj2 = specAttributePictureDetailDTOList.find((item2) => {
							return item2.specAttrValueId;
						})
						if (obj2) {
							var obj3 = obj2.specAttributePictureDTOLists.find((item2) => {
								return item2.isMainPicture == 1
							})
						}

						console.log(obj2);
						if (obj3) {
							item.specMainPicture = obj3.pictureUrl;
						}
					}
					// console.log(["12============",item]);
					//  this.$refs.chooseSpecCompon.goodsSpecSaveDTOList.push(item)
				})
				this.$refs.chooseSpecCompon.goodsSpecSaveDTOList = goodsSpecDetailDTOList
				this.$refs.chooseSpecCompon.composeTableDataDelay();

				delete this.dataForm.goodsSpecDetailDTOList;

				// 图片主规格属性图回显************************************
				var specAttributePictureDetailDTOList = cloneDeep(this.dataForm.specAttributePictureDetailDTOList);
				let imgListDefalt = [];
				specAttributePictureDetailDTOList.forEach((item, index) => {
					console.log([item, index]);
					imgListDefalt.push({
						// specName:item.specAttrValue,
						specName1: item.specAttrValue,
						id: item.specAttrValueId,
						specId: item.specAttrId,
						imgListItem: []
					})
					item.specAttributePictureDTOList.forEach((item2, index2) => {
						console.log(item2);
						imgListDefalt[index].imgListItem.push({
							image: item2.pictureUrl,
							id: item.specAttrValueId,//规格属性值id//"1144611004190105603"
							specId: item.specAttrId,//规格属性id //"1144611004190105604"
							sort: item2.sort,
						});
					})
				})
				// 因为图片的数据监听了表格数据的变化，而表格数据监听复选框的变化，所以这里需要加哥延迟
				setTimeout(() => {
					this.$refs.chooseSpecCompon.$refs.imageListCompon.editImgBackSacn(imgListDefalt);
					console.log("编辑回显imgList值：");
					console.log(imgListDefalt);
					console.log(this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList);
				}, 100)

				// 图片商品图图回显************************************
				var specAttributePictureDetailDTOList = cloneDeep(this.dataForm.specPictureListDefaultDTO);
				let imgList = [];
				imgList.push({
					// specName:item.specAttrValue,
					specName1: "上传图片",
					id: "1008601",
					specId: "10086",
					imgListItem: []
				})
				specAttributePictureDetailDTOList.forEach((item, index) => {
					console.log(item);
					imgList[0].imgListItem.push({
						image: item.pictureUrl,
						isMainPicture: item.isMainPicture,
						id: "1008601",
						specId: "10086",
						sort: item.sort,
					});
				})
				// 因为图片的数据监听了表格数据的变化，而表格数据监听复选框的变化，所以这里需要加哥延迟
				setTimeout(() => {
					this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.editImgBackSacn(imgList);
					console.log("编辑回显imgList值：");
					console.log(imgList);
					console.log(this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList);
				}, 100)


			},
			backScanGoodDetail() {
				if (this.dataForm.mobileBody) {
					this.$refs.goodsDetailH5.imgList = JSON.parse(this.dataForm.mobileBody);
				}
			},
			backScantAfterSale() {
				//  this.afterSale =  this.dataForm.afterSale;
				this.$refs.myTextEditor.dataForm.messageContent = this.dataForm.afterSale;
			},
			// 获取品牌下拉列表
			getBrandList(queryString, cb) {
				queryString ? queryString : ''
				var obj = {
					params: {
						gcClassId: this.dataForm.gcId,
						brandName: queryString,
					}
				}
				// brandList
				goodsclassBrandById(obj).then((res) => {
					if (res.code == 200) {
						var showData = []
						res.data.forEach((item, index) => {
							item.value = item.brandName;
							item.id = item.brandId
							showData.push(item);
						});
						cb(showData)
					}
				})
			},
			handleSelect(item) {
				console.log(item);
				this.dataForm.brandName = item.brandName
        this.dataForm.brandId = item.id
        document.getElementById("brandSelect").click()
			},
			// 追加属性条
			appendAttributeGroup() {
				var attributeValueDTOItem = cloneDeep(this.attributeValueDTOItem);
				this.dataForm.goodsAttributeSaveDTOList.push(attributeValueDTOItem);
			},
			//删除属性条
			deleteAttributeGroup(index) {
				this.dataForm.goodsAttributeSaveDTOList.splice(index, 1)
			},
			// 清除某条属性的属性
			clearAttribut(index) {
				this.dataForm.goodsAttributeSaveDTOList[index].attrValue = "";
				this.dataForm.goodsAttributeSaveDTOList[index].attrName = "";
			},
			// changeArrtibute(index){
			//      console.log(this.dataForm.goodsAttributeSaveDTOList[index]);
			// },
			// 保存模板
			saveTemplate() {
			},
			// 使用模板
			useTemplate() {
			},
			// 选择模板
			chooseTemplate() {
			},

			// 商品规格模块 ------------------------------------------------------------------------------------------------------------------
			// 模块化了
			// 列举有的的规格(不包括一级未选中的);
			setSpecAttributeAndValueSaveDTOList() {

				console.log(this.$refs.chooseSpecCompon.specLevel_2);
				// this.$refs.chooseSpecCompon.specLevel_1
				let isChooseMiain = false;
                this.dataForm.specAttributeAndValueSaveDTOList = [];
				this.$refs.chooseSpecCompon.specLevel_1.forEach((item, index) => {
					// 第一层的主规格的第一个必须选中
					if (index == 0 && item.checked) {
						isChooseMiain = true;
					}
					// if(item.checked){
					this.$refs.chooseSpecCompon.specLevel_2[index].forEach((item2, index2) => {
						var addType = 2;
						if (this.dataForm.id) {
							addType = item2.addType ? item2.addType : 0;
						}
						this.dataForm.specAttributeAndValueSaveDTOList.push({
							"addType": addType,// 新增传2或者不传//修改操作填写 是否新增 0:原有; 1:新增规格属性值 2:新增规格属性和规格属性值 ,//todu
							"goodsId": null,//商品ID ,//todu
							"isMain": index == 0 ? 1 : 0,//是否为主规格（默认0否，1是） ,
							"isSelect": item2.checked ? 1 : 0,// 是否选中（0未选中，默认1选中） ,
							"specAttrId": item2.specId ? item2.specId : new Date().getTime(),//规格属性ID , //如 颜色，尺寸
							"specAttrName": item.name,//规格属性名称 ,
							"specAttrValue": item2.name,//规格属性值名称 ,
							"specAttrValueId": item2.id ? item2.id : new Date().getTime(),//规格属性值ID ,//如红色，s码
							// "tempSpecAttrValueId": 0 //后台使用
						})
					})
					// }
				})
				return isChooseMiain;

      },
      // 表格 -------------------------
      setGoodsSpecSaveDTOList(goodsSpecSaveDTOList) {
          this.dataForm.goodsSpecSaveDTOList = cloneDeep(goodsSpecSaveDTOList);
      },
      setSpecLevel_1(specLevel_1){
          this.dataForm.specLevel_1 = cloneDeep(specLevel_1);
      },
      setSpecLevel_2(specLevel_2){
          this.dataForm.specLevel_2 = cloneDeep(specLevel_2);
      },
      setImgList(imgList){
          console.log("我看看默认的图片");
          this.dataForm.imgList = cloneDeep(imgList);
          console.log(this.dataForm.imgList)
      },
      setSpecImgList(specImgList){
          this.dataForm.specImgList = cloneDeep(specImgList);
      },
      // 图片 -------------------------
      setSpecAttributePictureSaveDTOList(specAttributePictureSaveDTOList){
          console.log("我看看图片")
          this.dataForm.specAttributePictureSaveDTOList = cloneDeep(specAttributePictureSaveDTOList);
          console.log(this.dataForm.specAttributePictureSaveDTOList)
      },

      // 物流信息--------------------------------------------------------------------------------------------------------------------
      storeFreightTemplateList() {
        this.freightTemplateLoading = true;
          return storeFreightTemplateList().then((res)=>{
              this.freightTemplateLoading = false;
              if(res.code = '200'){
                  this.freightTemplateListoption = res.data;
                  this.freightTemplateListoptionSet=this.unique(this.freightTemplateListoption)
              }
              // 回显物流信息
              this.backScantFreightTemplate();
          })
      },
      unique(arr) {
        const res = new Map();
        return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1));
      },
      collapseFreightTemplateRule() {
        if (this.collapseRule == false) {
            this.collapseRule = true;
        } else {
            this.collapseRule = false;
        }
      },
      // 快递开关
      expressFlagSwitch(){
        if (this.dataForm.expressFlag == 1) {
            this.dataForm.expressFlag = 0;
        } else {
            this.dataForm.expressFlag = 1;
        }
      },
      // 选择运费模板
      chooseFreightTemplate(val){
          this.freightRuleList = [];
          this.freightTemplateListoption.forEach((item,index)=>{
              if (item.id == val) {
                this.feiType=item.templateType
                this.freightRuleList = item.freightTemplateRuleDTOList;
              }
          })
      },
      backScantFreightTemplate(){
        if (this.dataForm.id){
            this.chooseFreightTemplate(this.dataForm.freightTemplateId);
        }
      },
      // 售后模板--------------------------------------------------------------------------------------------------------------------
      aftertemplateListPull(){
        var obj  = {
          params:{
            // page:1,
            // limit:50
          }
        }
        this.afterTemplateLoading = true;
        aftertemplateList(obj).then((res)=>{
           this.afterTemplateLoading = false;
           if(res.code = '200'){
              this.aftertemplateListoption = res.data;
           }
        })
      },
      chooseAfterTemplate(){
         this.$refs.myTextEditor.dataForm.messageContent = this.afterSale;
      },
       artmessageContent(messageContent){
        this.dataForm.afterSale = messageContent;
      },
      // 底部按钮------------------------------------------------------------------------------------------------------------
      filterTime(value){
       	  function add0(m){return m<10?'0'+m:m }
          var time = new Date(value);
          var y = time.getFullYear();
          var m = time.getMonth()+1;
          var d = time.getDate();
          var h = time.getHours();
          var mm = time.getMinutes();
          var s = time.getSeconds();
				  return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s)
            // return y+'-'+add0(m)+'-'+add0(d)
      },
      afterTime(){
           console.log(this.dataForm.shelfTime);
           if(new Date(this.dataForm.shelfTime).getTime() < new Date().getTime()){
                this.dataForm.shelfTime = this.filterTime(new Date());
           }
      },
      // 提交商品时各种验证----------------------------------------------------------------------------------------------------
      // 统计取消勾选的规格的id
      updateSpecIds(){
          let specAttributeModifyIds = [];//勾选的的规格属性id
          let specAttributeAndValueModifyIds= [];//移除的规格属性值id
          this.$refs.chooseSpecCompon.specLevel_1.forEach((item,index)=>{
              if(item.checked && item.specId) specAttributeModifyIds.push(item.specId);
              this.$refs.chooseSpecCompon.specLevel_2[index].forEach((item2,index2)=>{
                  if(!item2.checked  && item2.id) specAttributeAndValueModifyIds.push(item2.id);
              })
          })
          this.dataForm.specAttributeModifyIds = specAttributeModifyIds;
          this.dataForm.specAttributeAndValueModifyIds = specAttributeAndValueModifyIds;
          console.log("specAttributeModifyIds");
          console.log(this.dataForm.specAttributeModifyIds);
          console.log("specAttributeAndValueModifyIds");
          console.log(this.dataForm.specAttributeAndValueModifyIds);
      },
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
         handleChangeOut(val){
           if(val.length==0){
             this.dataForm.firstStoreGoodsGcId='';
             this.dataForm.firstStoreGoodsGcName='';
             this.dataForm.secondStoreGoodsGcId='';
             this.dataForm.secondStoreGoodsGcName='';
             return
           }
          this.dataForm.firstStoreGoodsGcId=val[0]
          this.dataForm.secondStoreGoodsGcId=val[1]
          for(let i=0;i<=this.classDataList.length-1;i++){
             if(this.classDataList[i].id==this.dataForm.firstStoreGoodsGcId){
                this.dataForm.firstStoreGoodsGcName=this.classDataList[i].gcName
             }
             if(this.classDataList[i] && this.classDataList[i].children){
                for(let j=0;j<=this.classDataList[i].children.length-1;j++){
                  if(this.dataForm.secondStoreGoodsGcId&&this.dataForm.secondStoreGoodsGcId==this.classDataList[i].children[j].id){
                      this.dataForm.secondStoreGoodsGcName=this.classDataList[i].children[j].gcName
                      break
                }
             }
             }
           }


        },
      //  商品属性不能有空值
      judgeGoodsAttribute(){
        let isHaveNullValue = false;
        console.log(this.dataForm.goodsAttributeSaveDTOList);
        var attrNameArr = []
        this.dataForm.goodsAttributeSaveDTOList.forEach((item,index)=>{
           attrNameArr.push(item.attrName);
          // if(!item.attrValueOpction && (!item.attrName || !item.attrValue)){
          //   isHaveNullValue = true
          // }
        })
         if(attrNameArr.length !=new Set(attrNameArr).size){
           this.$message({
            message: "属性名称不能有重复!",
            type: "warning",
            duration: 800
          });
          return true;
        }
        if(isHaveNullValue){
             this.$message({
              message: "属性名或属性值不能为空",
              type: "warning",
              duration: 800
            });
        }
        return  isHaveNullValue;
      },
       // 验证规格值不能重复
      judgeGoodsSepc(){
        let isHaveReptSpec = false;
        var specName = []
        this.$refs.chooseSpecCompon.specLevel_1.forEach((item,index)=>{
            specName.push(item.name);
        })
        if(specName.length != new Set(specName).size){
            isHaveReptSpec = true; //有重复的规格名
        }
        if(isHaveReptSpec){
             this.$message({
              message: "商品规格名不能有重复",
              type: "warning",
              duration: 800
            });
        }
        return isHaveReptSpec;
      },
        // 判断是否每个主图都上传了
      judgeMainPictureFn(){
           let isHaveNullPicture = false;
           this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList.forEach((item,index)=>{
             item.imgListItem.forEach((item2,index2)=>{
                  item2.noImg = false
                  if(index2==0 && !item2.image){
                      item2.noImg = true;
                      isHaveNullPicture = true;
                  }
             })
           })
           this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList = [].concat(this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList)

				this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList.forEach((item, index) => {
					item.imgListItem.forEach((item2, index2) => {
						item2.noImg = false
						if (index2 == 0 && !item2.image) {
							item2.noImg = true;
							isHaveNullPicture = true;
						}
					})
				})
				this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList = [].concat(this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList)

				if (isHaveNullPicture) {
					this.$message({
						message: "请上传图片",
						type: "warning",
						duration: 800
					});
				}
				return isHaveNullPicture;
			},
			// 校验规格货号 (掉后端接口验证)
			validGoodsSeriaFn() {
				let that = this
				var serials = [];
				let haveEmptySerial = false;//是否有空的商品编码
				this.dataForm.goodsSpecSaveDTOList.forEach((item, index) => {
					serials.push(item.specSerial);
					if (!item.specSerial) {
						haveEmptySerial = true;
					}
				})
				return new Promise(function (resolve) {
					// 没添加 表格数据，就不验证了
					if (serials.length == 0) {
						resolve("success")
						return;
					}
					// 如果有有空的商品编码,不让提交
					if (haveEmptySerial) {
						that.$message.warning("商品编码不能为空");
						resolve("error")
						return;
					}
					var obj = {
						"goodsId": that.dataForm.id ? that.dataForm.id : '',//商品ID ,
						"serials": serials,// 规格货号
					}
					validGoodsSeria(obj).then((res) => {
						if (res.code == 200) {
							if (res.data && res.data.length != 0) {
								that.$message.warning(res.data.join(",") + "货号值和系统中其他商品有重复");
								resolve("error")
							} else {
								resolve("success")
							}
						} else {
							that.$message.error();
							resolve("error")
						}
					})
				})
			},
      dataFormSubmit(formName,type) {
        if(type===0){
          this.dataForm.goodsType = 0
        }else if (type===1){
          this.timedReleaseDialog = false
          this.dataForm.goodsType = 1
        }else{
          this.dataForm.draftFlag = 0
          this.dataForm.goodsType = 2
          console.log("这里是保存为草稿")
        }
				this.afterTime();
				// 如果正在提交商品，这里
				if (this.saveLoading) {
					return;
				}
				//  this.dataForm.specAttributePictureSaveDTOListDefault = [{
				//         "goodsId":0,
				//         "isMainPicture":1,
				//         "pictureUrl":"https://mockplus-prototype-host.mockplus.cn/525794/EK3GDFF6HGADXAGA/7/images/%E9%A6%96%E9%A1%B5/u6.png",
				//         "sort":0
				//  }]
				this.dataForm.specAttributePictureSaveListDefaultDTO = this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.specAttributePictureSaveDTOListDefault

				this.dataForm.firstGcId = this.dataForm.gcId0
				this.dataForm.firstGcName = this.dataForm.gcName0
				this.dataForm.secondGcId = this.dataForm.gcId1
				this.dataForm.secondGcName = this.dataForm.gcName1
				this.dataForm.thirdGcId = this.dataForm.gcId2
				this.dataForm.thirdGcName = this.dataForm.gcName2
                if (this.dataForm.freightBearType != 1) {
                    this.dataForm.freightTemplateId = '';
                }
				if (this.dataForm.thirdGcId) {
					this.dataForm.gcId = this.dataForm.thirdGcId;
					this.dataForm.gcName = this.dataForm.thirdGcName;
				} else if (this.dataForm.secondGcId) {
					this.dataForm.gcId = this.dataForm.secondGcId;
					this.dataForm.gcName = this.dataForm.secondGcName;
				} else {
					this.dataForm.gcId = this.dataForm.firstGcId;
					this.dataForm.gcName = this.dataForm.firstGcName;
				}
				//  提交之前先拼数据
				this.setSpecAttributeAndValueSaveDTOList()

        this.updateSpecIds();
        if(this.dataForm.labelId){
          this.dataForm.labelIds=[],
          this.dataForm.labelIds.push(this.dataForm.labelId);
        }
				let obj = {
          ...this.dataForm,
					shelfTime: this.dataForm.goodsType == 0 ? null : this.filterTime(this.dataForm.shelfTime),
                    codeValidDay:this.expirationDay==0?this.dataForm.codeValidDay:-1,

                }
        	if(obj.invoiceFlag=="1"&&obj.invoiceContent==""){
						this.$message({
							message: "发票内容不能为空",
							type: "error",
							duration: 1500
						})
						return;
					};
					if(obj.invoiceFlag=="1"&&obj.invoiceType==""){
						this.$message({
							message: "发票类型不能为空",
							type: "error",
							duration: 1500
						})
						return;
					};
					if(obj.invoiceContent&&obj.invoiceContent!=""&&obj.invoiceContent.length>0){
						var newInvoiceContent = []
						for(var i=0;i<obj.invoiceContent.length;i++){
							if(obj.invoiceContent[i] !==''){
								newInvoiceContent.push(obj.invoiceContent[i])
							}
						}
						obj.invoiceContent =  newInvoiceContent.join()
					}else{
						obj.invoiceContent=""
					}
					if( obj.invoiceType&&obj.invoiceType!=""&&obj.invoiceType.length>0){
						var newinvoiceType = []
						for(var i=0;i<obj.invoiceType.length;i++){
							if(obj.invoiceType[i] !==''){
								newinvoiceType.push(obj.invoiceType[i])
							}
						}
						obj.invoiceType = newinvoiceType.join()
					}else{
						obj.invoiceType=""
					}
				obj.invoiceType = this.dataForm.invoiceType.join();
        obj.invoiceContent = this.dataForm.invoiceContent.join();
        // obj.firstStoreGoodsGcId=this.storeGcId1
        // obj.secondStoreGoodsGcId=this.storeGcId2
        // obj.firstStoreGoodsGcName=this.storeGcName1
        // obj.secondStoreGoodsGcName=this.storeGcName2

				console.log(JSON.stringify(obj));
				this.$refs[formName].validate((valid) => {
					if (valid) {
						// 商品属性不能有空值
						// if(this.judgeGoodsAttribute()){ return;}
						// 验证规格值不能重复
						if (this.judgeGoodsSepc()) {
							return
						}

                    if( this.$refs.chooseSpecCompon.$refs.imageListCompon.uploading){
                        this.$message({
                            message: "正在上传商品主图,请稍后提交",
                            type: "warning",
                            duration: 500
                          });
                          return;
                    }
                    if(this.dataForm.expressFlag == 1&&this.dataForm.freightBearType==1 && !this.dataForm.freightTemplateId){
                        this.$message({
                            message: "请选择运费模板",
                            type: "warning",
                            duration: 500
                        });
                        return;
                    }
                    //如果是虚拟商品走这里校验数据
                    if(this.dataForm.virtualFlag==1){
                      if(this.dataForm.expressFlag=''){
                          this.$message({
                              message: "配送方式不能为空",
                              type: "warning",
                              duration: 500
                          });
                          return;
                      }
                      if(this.dataForm.codeValidDay=''){
                          this.$message({
                              message: "提货码有效期不能为空",
                              type: "warning",
                              duration: 500
                          });
                          return;
                      }
                      if(this.dataForm.codeRefundFlag=''){
                          this.$message({
                              message: "提货码过期退款不能为空",
                              type: "warning",
                              duration: 500
                          });
                          return;
                      }
                    }else{
                      this.dataForm.codeValidDay='';
                      this.dataForm.codeRefundFlag='';
                    }
                  // 提交前判断商品主图是否全部提交
                  if(this.judgeMainPictureFn()){
                    return;
                  }
                  let fn = addGoods;
                  // 如果是编辑
                  if(this.dataForm.id){
                    fn = updateGoods;
                    delete obj.specAttributePictureDetailDTOList;
                    delete obj.goodsAttributeSaveDTOList;
                  }
                   // 验证规格货号有没有重复
                    this.saveLoading = true;
                  this.validGoodsSeriaFn().then((res)=>{
                    if(res=='success'){
                          this.saveLoading = true;
                          fn(obj).then((res)=>{
                               this.saveLoading = false;
                               let status = "warning"
                                if(res.code=="200"){
                                    status = "success"
                                    this.$emit("showPageNumberFn",3);
                                }else{
                                    status = "errror"
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500
                                    })
                                return
                          })
                      }else{
                        this.saveLoading = false;
                      }
                  })
                } else {
                    //console.log('error 添加失败!!');
                    return false;
                }
            })
      },
      changeExpressFlag(){
        // 配送方式 0:无需物流 1:快递 2自提 3电子提货码
          if (this.dataForm.expressFlag==0){
              this.dataForm.freightBearType=0
          }
          if (this.dataForm.expressFlag == 1) {
              this.dataForm.freightBearType=2
          }
      },
      preStep(){
        if(this.dataForm.id){
            this.$router.go(-1);//当详情页
        }else{
           this.$emit("showPageNumberFn",1);
        }

			},
		}
	}
</script>
<style lang="scss" scoped>
  @import "@/element-ui/theme-variables.scss";


  .hrTips {
    color: #999999;
    margin-left: 150px;
    margin-bottom: 10px;
  }

  .arrrtibuteWrap {
    .arrrtibuteWrap1 {
      width: 85%;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;

      .attributeItem {
        margin-right: 10px;
        display: flex;
        margin-bottom: 30px;
      }
    }

    .arrrtibuteWrap2 {
      .el-form-item {
        display: flex;

        /deep/ .el-form-item__label {
          width: auto !important;
        }

        /deep/ .el-form-item__content {
          margin-left: 0 !important;
        }
      }
    }
  }

  /deep/ .el-form-item__content {
    display: block;
  }

  /deep/ .el-form-item__content::before {
    content: none !important;
  }

  .skuSelect {
    min-width: 400px;
  }

  .attributeBtWrap {
    margin-top: 20px;
  }

  .nextStepWrap {
    margin-top: 40px;
    margin-bottom: 40px;
    margin-left: 100px;
  }

  .clickSpan {
    cursor: pointer;
  }

  /deep/ .el-form-item {
    margin-top: 0;
  }

  .rightWarp {
    width: 100px;
    border-left: 2px solid #cac9c9;
    z-index: 10;
  }

  .nav1 {
    display: flex;
    // width: 100px;
    // height: 40px;
    text-align: center;
    line-height: 40px;
    background: white;
    margin: 10px 0;
    align-items: center;
    font-size: 14px;

    a {
      color: rgba(0, 0, 0, 0.647058823529412);
      text-decoration: none;
      cursor: pointer;
    }

    .bgCircle {
      visibility: hidden;
      display: flex;
      position: relative;
      left: -7.5px;
      border-radius: 50%;
      width: 14px;
      height: 14px;
      background-color: $--color-primary;
      align-items: center;
      justify-content: center;

      .smallCircle {
        border-radius: 50%;
        width: 5px;
        height: 5px;
        background-color: white;
      }
    }
  }

  .current {
    color: $--color-primary;

    .bgCircle {
      visibility: visible;
    }
  }

  .radioGroup {
    margin-top: 13px;
  }

  /deep/ .el-form-item__error {
    min-width: 200px;
    line-height: inherit;
    left: 100%;
    top: 0;
  }

  .error-box{
    .el-form-item {
      display: block;
    }

    /deep/
    .el-checkbox-group{
      display: inline-block;
    }
    /deep/ .el-form-item__error {
      position: relative ;
      display: inline-block;
      margin-left: 10px;
      left: 0px;
    }
  }
</style>
