<template>
  <div class="eidtGoodsClass" style="margin-left: 2%;margin-right: 2%;">
    <h3 style="margin-top: 30px;" class="section">基本信息</h3>
    <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="addForm"
        label-width="120px">
      <el-form-item label="商品类目：">
        <span>{{dataForm.gcName0}} </span>
        <span v-if="dataForm.gcName1">> {{dataForm.gcName1}} </span>
        <span v-if="dataForm.gcName2">> {{dataForm.gcName2}} </span>
        <el-button v-if="!$route.query.gcId" @click="preStep()" style="margin-left:20px;">编辑</el-button>
      </el-form-item>
      <el-form-item label="商品名称：" prop="goodsName" style="width:45%">
        <el-input :disabled="true" v-model="dataForm.goodsName" maxlength="64" show-word-limit
                  placeholder="请输入商品名称"></el-input>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'商品名称最长不超过64个汉字，建议格式“品牌+商品名称+型号”'"></promptMessage>

      <el-form-item label="副标题：" prop="goodsSubTitle" style="width:45%;">
        <el-input :disabled="true" v-model="dataForm.goodsSubTitle" maxlength="64" show-word-limit
                  placeholder="请输入副标题"></el-input>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'副标题可填写商品的简介或特点，最长不能超过64个汉字'"></promptMessage>

      <el-form-item label="售价：" prop="specSellPrice">
        <el-input-number :disabled="true" v-model="dataForm.specSellPrice" :precision="2" :step="0.1" :min="0.01"
                         :max="999999.99"></el-input-number>
        <span> 元</span>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'该价格为实际售卖价格'"></promptMessage>


      <el-form-item label="成本价：" prop="specCostPrice">
        <el-input-number :disabled="true" v-model="dataForm.specCostPrice" :precision="2" :step="0.1" :min="0.01"
                         :max="999999.99"></el-input-number>
        <span> 元</span>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'辅助成本核算，商城各页面不显示'"></promptMessage>


      <el-form-item label="总库存：" prop="specStorage">
        <el-input-number :disabled="true" v-if="dataForm.goodsSpecSaveDTOList.length!=0" v-model="dataForm.specStorage"
                         :step="1" :min="0"></el-input-number>
        <el-input-number :disabled="true" v-else v-model="dataForm.specStorage" :step="1" :min="0"
                         :max="999999"></el-input-number>
        <span> 件</span>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'商品所有规格型号的库存总和，编辑单个规格库存后，总库存自动更新为全部规格库存的总和'"></promptMessage>


      <el-form-item label="商品标签：" prop="labelIds">
        <el-select
            :disabled="true"
            class="skuSelect"
            v-model="dataForm.labelIds"
            multiple
            :multiple-limit='limitNumber'
            filterable
            remote
            reserve-keyword
            placeholder="请输入关键词"
            :remote-method="getTagGroupAllList"
            :loading="tagLoading">
          <el-option
              v-for="(item,index) in labelOptions"
              :key="index"
              :label="item.labelName"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'正确选择标签有助于提升搜索筛选的准确性，请根据实际情况认真选择'"></promptMessage>


      <el-form-item label="商品品牌：" prop="brandId">
        <span style="padding:0 10px;">{{dataForm.brandName?dataForm.brandName:'未选择'}}</span>
        <el-popover
            placement="bottom"
            trigger="click">
          <el-autocomplete
              v-model="brandName"
              :fetch-suggestions="getBrandList"
              placeholder="请输入内容"
              @select="handleSelect"
          ></el-autocomplete>
          <el-button :disabled="true" slot="reference" type="primary">选择商品品牌</el-button>
        </el-popover>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'正确选择品牌有助于提升搜索筛选的准确性，请根据实际情况认真选择'"></promptMessage>


      <el-form-item label="商品属性：" class="arrrtibuteWrap">
        <!-- <el-input v-model="dataForm.name" maxlength="128" placeholder="请输入内容"></el-input> -->
        <!-- 下拉形式显示属性 -->
        <div class="arrrtibuteWrap1"
             v-if="dataForm.goodsAttributeSaveDTOList && dataForm.goodsAttributeSaveDTOList.length!=0">
          <div class="attributeItem" v-for="(item,index) in dataForm.goodsAttributeSaveDTOList" v-if="item.attributeId">
            <span>{{item.attrName}}：</span>
            <el-select v-model="item.attrValue" placeholder="请选择区间">
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
          <div v-for="(item,index) in dataForm.goodsAttributeSaveDTOList" v-if="!item.attributeId" :key="index"
               style="display: flex;margin-bottom:20px;">
            <el-form-item label="属性名称：">
              <el-input :disabled="true" v-model="dataForm.goodsAttributeSaveDTOList[index].attrName" maxlength="20"
                        placeholder="请输入属性名称"></el-input>
            </el-form-item>

            <el-form-item label="属性值：" style="margin-left:20px;">
              <el-input :disabled="true" v-model="dataForm.goodsAttributeSaveDTOList[index].attrValue" maxlength="20"
                        placeholder="请输入属性名称"></el-input>
            </el-form-item>

            <el-button :disabled="true" type="primary" plain @click="clearAttribut(index)"
                       style="margin-left:10px;height: 40px;">清空
            </el-button>
            <el-button :disabled="true" type="danger" plain @click="deleteAttributeGroup(index)"
                       style="margin-left:10px;height: 40px;">
              删除
            </el-button>
          </div>
          <el-form-item>
            <el-button :disabled="true" type="primary" @click="appendAttributeGroup()">添加属性值</el-button>
          </el-form-item>
        </div>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'商品属性会显示在商城商品详情的规格参数中'"></promptMessage>


      <!--  选择规格 和 图片上传 -->
      <h3 style="margin-top: 30px;" class="section">商品规格</h3>
      <chooseSpec
          ref="chooseSpecCompon"
          @setGoodsSpecSaveDTOList="setGoodsSpecSaveDTOList"
          @setSpecAttributePictureSaveDTOList="setSpecAttributePictureSaveDTOList"
          :dataForm="dataForm">
      </chooseSpec>


      <h3 style="margin-top: 30px;" class="section">商品详情</h3>
      <el-form-item label="商品详情：">
        <goodsDetailH5 ref="goodsDetailH5"></goodsDetailH5>
      </el-form-item>


      <h3 style="margin-top: 30px;" class="section">售后说明</h3>
      <el-form-item label="售后说明模板：" prop="afterSale" style="margin-right:120px;"> <!-- prop="afterSale" -->
        <el-select
            :disabled="true"
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
        <!-- <quill-editor  style=" margin-top: 12px;" ref="myTextEditor" v-model="dataForm.afterSale" :options="editorOption"></quill-editor> -->
        <quill-editor-img :isDisabled="isDisabled" @artmessageContent='artmessageContent'
                          ref="myTextEditor"></quill-editor-img>
      </el-form-item>
      <promptMessage :labelWidth="120" :message="'售后说明会显示在商城商品详情的售后说明中'"></promptMessage>


      <div v-show="dataForm.goodsStatus == 10">
        <h3 style="margin-top: 30px;">上架审核</h3>


        <el-form-item label="提交人：" style="width:45%">
          <div>{{dataForm.updater}}</div>
        </el-form-item>
        <el-form-item label="审核结果：" prop="goodsName" style="width:45%">
          <el-radio v-model="checkObj.isChecke" label="30">审核通过</el-radio>
          <el-radio v-model="checkObj.isChecke" label="20">审核拒绝</el-radio>
        </el-form-item>
        <el-form-item label="备注：" :prop="checkObj.isChecke == 20 ? 'goodsName' : null" style="width:45%">
          <el-input v-model="checkObj.remark"
                    :maxlength="maxlength"
                    type="textarea"
                    autosize
                    placeholder="请输入内容"></el-input>
        </el-form-item>
      </div>
    </el-form>
    <div class="nextStepWrap" v-if=" dataForm.goodsStatus == 10">
      <el-button class="nextStep" @click="preStep()">取消</el-button>
      <el-button class="nextStep" type="primary" @click="dataFormSubmit('addForm')" v-loading="saveLoading">
        {{saveLoading?'保存中...':'保存'}}
      </el-button>
    </div>
    <div class="nextStepWrap" v-if=" dataForm.goodsStatus != 10 ">
      <el-button class="nextStep" @click="preStep()">返回</el-button>
    </div>
    <nav style="position:fixed;right:30px;top:300px;">
      <div class="rightWarp">
        <div class="nav1" v-for="(item, index) in navList" :key="index" :class="index==0?'current':''">
          <div class="bgCircle">
            <div class="smallCircle"></div>
          </div>
          <a @click="jump(index)">{{item}}</a>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>

  import chooseSpec from './editSpecModules/chooseSpec.vue'
  // 标签下拉
  import { tagGroupAllList } from '@/api/api'
  import cloneDeep from 'lodash/cloneDeep'
  // 属性下拉
  import { attributeList } from '@/api/api'
  // 品牌下拉
  import { goodsclassBrandById, brandList } from '@/api/api.js'
  // 规格下拉
  import { specList } from '@/api/api.js'
  // 根据分类id查询品牌，规格，
  import { goodsclassById } from '@/api/api.js'
  // 属性值回显
  import { backScanAttribute } from '@/api/api.js'
  // import { backScanAttributegroup } from "@/api/api.js";
  // 二级规格回显
  import { backScanSpec, checkGoods } from '@/api/api'
  import { addGoods, updateGoods } from '@/api/api.js'
  // 模板下拉列表
  import { aftertemplateList } from '@/api/api.js'
  // 校验规格货号
  import { validGoodsSeria } from '@/api/api.js'

  import { setTimeout } from 'timers'
  import { type } from 'os'

  // import { quillEditor } from "vue-quill-editor"; //调用编辑器
  // import 'quill/dist/quill.core.css';
  // import 'quill/dist/quill.snow.css';
  // import 'quill/dist/quill.bubble.css';
  import quillEditorImg from '@/components/quillEditor'
  import promptMessage from '@/components/prompt/promptMessage'

  import goodsDetailH5 from './goodsDetailModules/goodsDetailH5.vue'
  import { constants } from 'crypto'

  export default {
    data () {
      var validatorValue = (rule, value, callback) => {
        var value2 = value.replace(/<\/p>/g, '').replace(/<p>/g, '').replace(/&nbsp;/g, '')
        value2 = value2.replace(/ /g, '')
        // console.log(value2.length)
        if (value2.length > 800) {
          callback(new Error('模板内容长度限制为800个汉字'))
        } else {
          callback()
        }
      }
      return {
        maxlength: 200,
        checkObj: {
          isChecke: null,
          remark: ''
        },
        isDisabled: true,
        limitNumber: 3,
        tagLoading: false,
        scroll: '',
        navList: ['基本信息', '商品规格', '上传图片', '商品详情', '售后说明'],
        saveLoading: false,
        breaddata: ['商品管理', '商品管理', '商品添加'],
        showPageNumber: 1,
        afterSale: '',//用户模板select用
        brandName: '',//用于搜索用
        labelOptions: [],//标签
        dataForm: {
          gcId: '',//1135755925667115009
          goodsSerial: new Date().getTime(),
          gcName: '',
          gcId0: '',
          gcId1: '',
          gcId2: '',
          gcName0: '',
          gcName1: '',
          gcName2: '',
          goodsName: '',//商品名称
          goodsSubTitle: '',
          specSellPrice: 0,
          specCostPrice: 0,
          specStorage: 0,
          brandId: '',
          brandName: '',

          goodsAttributeSaveDTOList: [ //属性组
            //  {
            //     "attrName":"",
            //     "attrValue":"",
            //     "attributeId":0,
            //     "goodsId":0
            //   },
          ],
          specAttributeAndValueSaveDTOList: [],
          goodsSpecSaveDTOList: [],//规格table
          specAttributePictureSaveDTOList: [],//规格对应的图片
          mobileBody: '',//商品详细内容(手机版) ,
          afterSale: '',//售后说明，提交后端用
          goodsType: '0',//商品发布类型 0:立即发布; 1:定时发布 ,
          shelfTime: new Date(), //发布时间 ,

          // 编辑时需要多穿
          specAttributeModifyIds: [], //移除规格属性数组 ,
          specAttributeAndValueModifyIds: [],//移除规格属性值数组 ,

        },

        attributeList: [
          { label: '价格区间', option: 'priceOption' },
          { label: '口碑', option: 'mouthWordOption' },
          { label: '配送', option: 'DeliveryOption' },
          { label: '产地', option: 'placeOriginOption' },
        ],
        priceOption: [],//价格
        mouthWordOption: [],//口碑
        DeliveryOption: [],//配送
        placeOriginOption: [],//产地
        attributeValueDTOItem: {
          'attrName': '',
          'attrValue': '',
          'attributeId': '',
          'goodsId': null
        },
        skuLoading: false,//属性loading
        skuOptions: [],//属性下拉列表,
        editorOption: {
          placeholder: '请输入您要编辑的内容'
        },
        afterTemplateLoading: false,
        aftertemplateListoption: [],//售后模板下拉
        pickerOptions: {
          disabledDate (time) {
            return time.getTime() < Date.now() - 8.64e7
          }
        },// 日期组件 设置项
        dataRule: {
          goodsName: [
            { required: true, message: '商品名称不能为空', trigger: 'blur' },
          ],
          goodsSubTitle: [
            { required: true, message: '副标题不能为空', trigger: 'blur' },
          ],
          specSellPrice: [
            { required: true, message: '出售价不能为空', trigger: 'blur' },
          ],
          specCostPrice: [
            { required: true, message: '成本价不能为空', trigger: 'blur' },
          ],
          specStorage: [
            { required: true, message: '库存不能为空', trigger: 'blur' },
          ],
          afterSale: [//售后说明
            // { required: true, message: '售后模板不能为空', trigger: 'blur' },
            { validator: validatorValue, trigger: 'blur' }
          ],
        },
      }
    },
    watch: {
      // 'dataForm.goodsName' (val) {
      //   this.composeTableDataDelay();
      // },
      'query.gcId' (val) {
        console.log(val)
      },
      // scroll: function () {
      //   this.loadSroll()
      // }
    },
    components: {
      // quillEditor,
      chooseSpec,
      goodsDetailH5,
      quillEditorImg,
      promptMessage

    },
    created () {
      // 获取属性下拉列表
      // this.getAttributeList();
      // this.getGoodsclassById();
      // 获取售后下拉列表
      this.aftertemplateListPull()
      // window.addEventListener('scroll', this.dataScroll)
    },
    //组件激活后
    activated () {
      this.$refs.addForm.clearValidate()
     
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

      async dataFormSubmit (formName) {
        //       this.title = "审核提示";
        //       this.dataForm.goodsStatus = 30;
        //     }else{//拒绝
        //       this.title = "确定拒绝审核通过？";
        // this.dataForm.goodsStatus = 20;
        if (this.checkObj.isChecke == null) {
          this.$message({
            message: '请选择审核结果',
            type: 'warning',
            duration: 1500
          })

        } else if (this.checkObj.isChecke == 20 && this.checkObj.remark.trim() == '') {
          this.$message({
            message: '请填写拒绝原因',
            type: 'warning',
            duration: 1500
          })
        } else {
          let obj = {
            goodState: this.checkObj.isChecke,
            goodsIds: [this.$route.query.gcId],
            remarks: this.checkObj.remark,
          }
          let result = await checkGoods(obj)
          console.log(result)
          if (result.code == 200) {
            this.$message({
              message: result.msg,
              type: 'success',
              duration: 1000
            })
            this.preStep()

          }
        }

      },
      getTagGroupAllList (query) {

        var obj = {
          params: {
            labelName: query
          }
        }
        this.tagLoading = true
        tagGroupAllList(obj).then((res) => {

          this.tagLoading = false
          var labelOptions = []

          if (res.code == 200 && res.data) {
            labelOptions = res.data

          }

          var arr = [...labelOptions]
          // js中数组对象去重的方法
          var obj = {}
          arr = arr.reduce(function (item, next) {
            obj[next.id] ? '' : obj[next.id] = true && item.push(next)
            return item
          }, [])
          this.labelOptions = arr
        })
      },
      init (dataForm) {
        Object.assign(this.dataForm, dataForm)
        // 获取属性下拉列表
        // this.getAttributeList();
        this.$nextTick(() => {
          if (!dataForm.id) {//如果是新增商品，需要根据分类id查数据
            this.getGoodsclassById()
          } else {//商品编辑
            //  回显属性
            this.backScanEditAttribute()
            // 回显规格
            this.backScanEditSpec()
            // 回显商品详情
            this.backScanGoodDetail()
            // 回显售后模板
            this.backScantAfterSale()
          }
        })
        this.getTagGroupAllList()
      },
      // 页面滚动右侧锚点代码-----------------------------------------------------------------------------------------
      dataScroll: function () {
         this.scroll = document.getElementsByClassName("aui-content__wrapper")[0].scrollTop
         this.loadSroll();
      },
      jump (index) {
        let jump = document.getElementsByClassName('section')
        
        // 获取需要滚动的距离
        let total = jump[index].offsetTop - 30
        document.getElementsByClassName("aui-content__wrapper")[0].scrollTop = total
     
        // // Chrome
        // document.body.scrollTop = total
        // // Firefox
        // document.documentElement.scrollTop = total
        // // Safari
        // window.pageYOffset = total
        // // $('html, body').animate({
        // // 'scrollTop': total
        // // }, 400);
      },
       loadSroll: function () {
        console.log("loadSroll");
        var self = this
        var $navs = $('.nav1')
        var sections = document.getElementsByClassName('section')
        for (var i = sections.length - 1; i >= 0; i--) {
          console.log(79879879809,[self.scroll,sections[i].offsetTop,sections[0].offsetTop,i]);
          if (self.scroll >= sections[i].offsetTop-200) {
            $navs.eq(i).addClass('current').siblings().removeClass('current')
            break
          }else{

          }
        }
      },
      // 新建回显----------------------------------------------------------------------------------------------------
      // // 根据分类id查询品牌，规格，
      getGoodsclassById () {
        if (!this.dataForm.gcId) {
          this.preStep()
        }
        var obj = {
          params: {
            gcId: this.dataForm.gcId,
            gcName: this.dataForm.gcName,
          }
        }
        goodsclassById(obj).then((res) => {
          if (res.code == 200) {
            this.dataForm.brandId = res.data.brandId
            this.dataForm.brandName = res.data.brandName
            this.dataForm.gcIdpath = res.data.gcIdpath
            this.dataForm.gcParentId = res.data.gcParentId
            this.dataForm.gcSort = res.data.gcSort
            this.backScanAttributeFn(res.data.goodsClassAttrDTO)
            this.backScanSpecFn(res.data.goodsClassSpecDTO)
          }
        })
      },
      // 属性回显
      backScanAttributeFn (goodsClassAttrDTO) {
        if (goodsClassAttrDTO) {
          this.dataForm.goodsAttributeSaveDTOList = []
          goodsClassAttrDTO.forEach((item, index) => {
            that.dataForm.goodsAttributeSaveDTOList.push({
              'attrName': item.attrName,
              // "attrValue":item.attrName,
              'attributeId': item.attrId,
              'goodsId': 0,//tudo
              // "attrValueOpction":attrValueOpction
            })
            this.backScan(item)
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
      backScan (item) {
        let that = this
        var obj = {
          id: item.attrId,
        }
        backScanAttribute(obj).then((res) => {
          if (res.code == 200) {
            // 属性单独处理回显
            var attrValueOpction = []
            res.data.attributeValueDTOList.forEach((item2, index2) => {
              attrValueOpction.push(
                { attrValue: item2.attrValueName }
              )
            })
            that.dataForm.goodsAttributeSaveDTOList[index].attrValueOpction = attrValueOpction
            that.dataForm.goodsAttributeSaveDTOList = [].concat(that.dataForm.goodsAttributeSaveDTOList)
            // that.dataForm.goodsAttributeSaveDTOList.push({
            //   "attrName":item.attrName,
            //   // "attrValue":item.attrName,
            //   "attributeId":item.attrId,
            //   "goodsId":0,//tudo
            //   "attrValueOpction":attrValueOpction
            // });
          }
        })
      },
      // 规格回显
      backScanSpecFn (goodsClassSpecDTO) {
        this.$refs.chooseSpecCompon.specLevel_1 = [
          { specId: '10086', name: '颜色', checked: false },
        ]
        this.$refs.chooseSpecCompon.specLevel_2 = [
          [
            { id: '1008601', specId: '10086', name: '红色', checked: false },
            { id: '1008602', specId: '10086', name: '橙色', checked: false },
            { id: '1008603', specId: '10086', name: '黄色', checked: false },
            { id: '1008604', specId: '10086', name: '绿色', checked: false },
            { id: '1008605', specId: '10086', name: '青色', checked: false },
            { id: '1008606', specId: '10086', name: '粉色', checked: false },
            { id: '1008607', specId: '10086', name: '蓝色', checked: false },
            { id: '1008608', specId: '10086', name: '紫色', checked: false },
            { id: '1008609', specId: '10086', name: '黑色', checked: false },
            { id: '1008610', specId: '10086', name: '白色', checked: false },
            { id: '1008611', specId: '10086', name: '棕色', checked: false },
          ]
        ]
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
          this.$refs.chooseSpecCompon.specLevel_2.push([])
          var obj = { id: item.specId }
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
                  )
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
      backScanEditAttribute () {
        this.dataForm.goodsAttributeSaveDTOList = this.dataForm.goodsAttributeDTOList
        this.dataForm.goodsAttributeSaveDTOList.forEach((item, index) => {
          if (item.attributeId) {
            backScanAttribute({ id: item.attributeId }).then((res) => {
              if(res.code == 200 && res.data){
                  // 属性单独处理回显
                var attrValueOpction = []
                res.data.attributeValueDTOList.forEach((item2, index2) => {
                  attrValueOpction.push(
                    { attrValue: item2.attrValueName }
                  )
                })
                item.attrValueOpction = cloneDeep(attrValueOpction)
                this.$set(this.dataForm.goodsAttributeSaveDTOList, index, this.dataForm.goodsAttributeSaveDTOList[index])
              }
            })
          }

        })

        // delete this.dataForm.goodsAttributeDTOList;
      },
      // 回显规格
      backScanEditSpec () {
        //规格数据 ************************************
        // specAttributeDetailDTOList
        var specAttributeDetailDTOList = cloneDeep(this.dataForm.specAttributeDetailDTOList)
        this.$refs.chooseSpecCompon.specLevel_1 = []
        this.$refs.chooseSpecCompon.specLevel_2 = []
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
          )
          // 回显规格二级
          this.$refs.chooseSpecCompon.specLevel_2.push([])
          item.specAttributeValueDetailDTOList.forEach((item2, index2) => {
            // 如果有规格值选中，那么规格也要选中
            if (item2.isSelect) {
              this.$refs.chooseSpecCompon.specLevel_1[index].checked = true
            }
            console.log('编辑规格回显数据', [index2, item2.specAttrValueId, item.id])
            this.$refs.chooseSpecCompon.specLevel_2[index].push(
              {
                id: item2.specAttrValueId,//红色id
                specId: item.id,//颜色id
                name: item2.specAttrValue,
                checked: item2.isSelect ? true : false,
                // isSelect:item2.isSelect
              },
            )
          })
        })
        delete this.dataForm.specAttributeDetailDTOList

        // 表格数据************************************
        var specAttributePictureDetailDTOList = cloneDeep(this.dataForm.specAttributePictureDetailDTOList)
        // goodsSpecDetailDTOList
        var goodsSpecDetailDTOList = cloneDeep(this.dataForm.goodsSpecDetailDTOList)
        this.$refs.chooseSpecCompon.goodsSpecSaveDTOList = []
        goodsSpecDetailDTOList.forEach((item, index) => {
          if (item.specAttributeValueDetailDTOList) {
            var obj = item.specAttributeValueDetailDTOList.find((item2) => {
              return item2.isMainValue == 1
            })
            // console.log(obj)
            if (obj) {
              item.img_use_id = obj.specAttrValueId
            }

          }
          if (specAttributePictureDetailDTOList) {
            var obj2 = specAttributePictureDetailDTOList.find((item2) => {
              return item2.specAttrValueId
            })
            if (obj2) {
              var obj3 = obj2.specAttributePictureDTOList.find((item2) => {
                return item2.isMainPicture == 1
              })
            }

            console.log(obj2)
            if (obj3) {
              item.specMainPicture = obj3.pictureUrl
            }
          }
          // console.log(["12============",item,item.specSellPrice]);
          // this.$refs.chooseSpecCompon.goodsSpecSaveDTOList.push(item)
        })
        this.$refs.chooseSpecCompon.goodsSpecSaveDTOList = goodsSpecDetailDTOList
        this.$refs.chooseSpecCompon.composeTableDataDelay()

        delete this.dataForm.goodsSpecDetailDTOList

        // 图片主规格属性图回显************************************
        // var specAttributePictureDetailDTOList = cloneDeep(this.dataForm.specAttributePictureDetailDTOList);
        let imgListDefalt = []
        specAttributePictureDetailDTOList.forEach((item, index) => {
          // console.log([item, index])
          imgListDefalt.push({
            // specName:item.specAttrValue,
            specName1: item.specAttrValue,
            id: item.specAttrValueId,
            specId: item.specAttrId,
            imgListItem: []
          })
          item.specAttributePictureDTOList.forEach((item2, index2) => {
            // console.log(item2)
            imgListDefalt[index].imgListItem.push({
              image: item2.pictureUrl,
              id: item.specAttrValueId,//规格属性值id//"1144611004190105603"
              specId: item.specAttrId,//规格属性id //"1144611004190105604"
              sort: item2.sort,
            })
          })
        })
        // 因为图片的数据监听了表格数据的变化，而表格数据监听复选框的变化，所以这里需要加哥延迟
        setTimeout(() => {
          this.$refs.chooseSpecCompon.$refs.imageListCompon.editImgBackSacn(imgListDefalt)
        }, 100)

        // 图片商品图图回显************************************
        var specAttributePictureDetailDTOList = cloneDeep(this.dataForm.specPictureListDefaultDTO)
        let imgList = []
        imgList.push({
          // specName:item.specAttrValue,
          specName1: '上传图片',
          id: '1008601',
          specId: '10086',
          imgListItem: []
        })
        specAttributePictureDetailDTOList.forEach((item, index) => {
          imgList[0].imgListItem.push({
            image: item.pictureUrl,
            isMainPicture: item.isMainPicture,
            id: '1008601',
            specId: '10086',
            sort: item.sort,
          })
        })
        // 因为图片的数据监听了表格数据的变化，而表格数据监听复选框的变化，所以这里需要加哥延迟
        setTimeout(() => {
          this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.editImgBackSacn(imgList)

        }, 100)

      },
      backScanGoodDetail () {
        if (this.dataForm.mobileBody) {
          this.$refs.goodsDetailH5.imgList = JSON.parse(this.dataForm.mobileBody)
        }
      },
      backScantAfterSale () {
        //  this.afterSale =  this.dataForm.afterSale;
        this.$refs.myTextEditor.dataForm.messageContent = this.dataForm.afterSale
      },
      // 获取品牌下拉列表
      getBrandList (queryString, cb) {
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
              item.value = item.brandName
              item.id = item.brandId
              showData.push(item)
            })
            cb(showData)
          }
        })
      },
      handleSelect (item) {
        this.dataForm.brandName = item.brandName
        this.dataForm.brandId = item.id
      },

      // 追加属性条
      appendAttributeGroup () {
        var attributeValueDTOItem = cloneDeep(this.attributeValueDTOItem)
        this.dataForm.goodsAttributeSaveDTOList.push(attributeValueDTOItem)
      },
      //删除属性条
      deleteAttributeGroup (index) {
        this.dataForm.goodsAttributeSaveDTOList.splice(index, 1)
      },
      // 清除某条属性的属性
      clearAttribut (index) {
        this.dataForm.goodsAttributeSaveDTOList[index].attrValue = ''
        this.dataForm.goodsAttributeSaveDTOList[index].attrName = ''
      },
      // changeArrtibute(index){
      //      console.log(this.dataForm.goodsAttributeSaveDTOList[index]);
      // },
      // 保存模板
      saveTemplate () {
      },
      // 使用模板
      useTemplate () {
      },
      // 选择模板
      chooseTemplate () {
      },

      // 商品规格模块 ------------------------------------------------------------------------------------------------------------------
      // 模块化了
      // 列举有的的规格(不包括一级未选中的);
      setSpecAttributeAndValueSaveDTOList () {

        let isChooseMiain = false
        this.dataForm.specAttributeAndValueSaveDTOList = []
        this.$refs.chooseSpecCompon.specLevel_1.forEach((item, index) => {
          // 第一层的主规格的第一个必须选中
          if (index == 0 && item.checked) {
            isChooseMiain = true
          }
          // if(item.checked){
          this.$refs.chooseSpecCompon.specLevel_2[index].forEach((item2, index2) => {
            var addType = 2
            if (this.dataForm.id) {
              addType = item2.addType ? item2.addType : 0
            }
            this.dataForm.specAttributeAndValueSaveDTOList.push({
              'addType': addType,// 新增传2或者不传//修改操作填写 是否新增 0:原有; 1:新增规格属性值 2:新增规格属性和规格属性值 ,//todu
              'goodsId': null,//商品ID ,//todu
              'isMain': index == 0 ? 1 : 0,//是否为主规格（默认0否，1是） ,
              'isSelect': item2.checked ? 1 : 0,// 是否选中（0未选中，默认1选中） ,
              'specAttrId': item2.specId ? item2.specId : new Date().getTime(),//规格属性ID , //如 颜色，尺寸
              'specAttrName': item.name,//规格属性名称 ,
              'specAttrValue': item2.name,//规格属性值名称 ,
              'specAttrValueId': item2.id ? item2.id : new Date().getTime(),//规格属性值ID ,//如红色，s码
              // "tempSpecAttrValueId": 0 //后台使用
            })
          })
          // }
        })
        return isChooseMiain

      },
      // 表格 -------------------------
      setGoodsSpecSaveDTOList (goodsSpecSaveDTOList) {
        this.dataForm.goodsSpecSaveDTOList = cloneDeep(goodsSpecSaveDTOList)
      },
      // 图片 -------------------------
      setSpecAttributePictureSaveDTOList (specAttributePictureSaveDTOList) {
        this.dataForm.specAttributePictureSaveDTOList = cloneDeep(specAttributePictureSaveDTOList)
      },

      // 售后模板--------------------------------------------------------------------------------------------------------------------
      aftertemplateListPull () {
        var obj = {
          params: {
            // page:1,
            // limit:50
          }
        }
        this.afterTemplateLoading = true
        aftertemplateList(obj).then((res) => {
          this.afterTemplateLoading = false
          if (res.code = '200') {
            this.aftertemplateListoption = res.data
          }

        })
      },
      chooseAfterTemplate () {
        this.$refs.myTextEditor.dataForm.messageContent = this.afterSale
      },
      artmessageContent (messageContent) {
        this.dataForm.afterSale = messageContent
      },
      // 底部按钮------------------------------------------------------------------------------------------------------------
      filterTime (value) {
        function add0 (m) {
          return m < 10 ? '0' + m : m
        }

        var time = new Date(value)
        var y = time.getFullYear()
        var m = time.getMonth() + 1
        var d = time.getDate()
        var h = time.getHours()
        var mm = time.getMinutes()
        var s = time.getSeconds()
        return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s)
        // return y+'-'+add0(m)+'-'+add0(d)
      },
      afterTime () {
        console.log(this.dataForm.shelfTime)
        if (new Date(this.dataForm.shelfTime).getTime() < new Date().getTime()) {
          this.dataForm.shelfTime = this.filterTime(new Date())
          console.log('纠正时间')
        }
      },
      // 提交商品时各种验证----------------------------------------------------------------------------------------------------
      // 统计取消勾选的规格的id
      updateSpecIds () {
        let specAttributeModifyIds = []//勾选的的规格属性id
        let specAttributeAndValueModifyIds = []//移除的规格属性值id
        this.$refs.chooseSpecCompon.specLevel_1.forEach((item, index) => {
          if (item.checked && item.specId) specAttributeModifyIds.push(item.specId)
          this.$refs.chooseSpecCompon.specLevel_2[index].forEach((item2, index2) => {
            if (!item2.checked && item2.id) specAttributeAndValueModifyIds.push(item2.id)
          })
        })
        this.dataForm.specAttributeModifyIds = specAttributeModifyIds
        this.dataForm.specAttributeAndValueModifyIds = specAttributeAndValueModifyIds
        console.log('specAttributeModifyIds')
        console.log(this.dataForm.specAttributeModifyIds)
        console.log('specAttributeAndValueModifyIds')
        console.log(this.dataForm.specAttributeAndValueModifyIds)
      },
      //  商品属性不能有空值
      judgeGoodsAttribute () {
        let isHaveNullValue = false
        console.log(this.dataForm.goodsAttributeSaveDTOList)
        var attrNameArr = []
        this.dataForm.goodsAttributeSaveDTOList.forEach((item, index) => {
          attrNameArr.push(item.attrName)
          if (!item.attrValueOpction && (!item.attrName || !item.attrValue)) {
            isHaveNullValue = true
          }
        })
        if (attrNameArr.length != new Set(attrNameArr).size) {
          this.$message({
            message: '属性名称不能有重复!',
            type: 'warning',
            duration: 800
          })
          return true
        }
        if (isHaveNullValue) {
          this.$message({
            message: '属性名或属性值不能为空',
            type: 'warning',
            duration: 800
          })
        }
        return isHaveNullValue
      },
      // 验证规格值不能重复
      judgeGoodsSepc () {
        let isHaveReptSpec = false
        var specName = []
        this.$refs.chooseSpecCompon.specLevel_1.forEach((item, index) => {
          specName.push(item.name)
        })
        if (specName.length != new Set(specName).size) {
          isHaveReptSpec = true //有重复的规格名
        }
        if (isHaveReptSpec) {
          this.$message({
            message: '商品规格名不能有重复',
            type: 'warning',
            duration: 800
          })
        }
        return isHaveReptSpec
      },
      // 判断是否每个主图都上传了
      judgeMainPictureFn () {
        let isHaveNullPicture = false
        this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList.forEach((item, index) => {
          item.imgListItem.forEach((item2, index2) => {
            item2.noImg = false
            if (index2 == 0 && !item2.image) {
              isHaveNullPicture = true
              item2.noImg = true
            }
          })
        })
        this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList = [].concat(this.$refs.chooseSpecCompon.$refs.imageListDefaultCompon.imgList)

        this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList.forEach((item, index) => {
          item.imgListItem.forEach((item2, index2) => {
            item2.noImg = false
            if (index2 == 0 && !item2.image) {
              isHaveNullPicture = true
              item2.noImg = true
              // this.$set(item2,"noImg",true)
            }
          })
        })
        this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList = [].concat(this.$refs.chooseSpecCompon.$refs.imageListCompon.imgList)
        if (isHaveNullPicture) {
          this.$message({
            message: '请上传图片',
            type: 'warning',
            duration: 800
          })
        }
        return isHaveNullPicture
      },
      // 校验规格货号 (掉后端接口验证)
      validGoodsSeriaFn () {
        let that = this
        var serials = []
        let haveEmptySerial = false//是否有空的商品编码
        this.dataForm.goodsSpecSaveDTOList.forEach((item, index) => {
          serials.push(item.specSerial)
          if (!item.specSerial) {
            haveEmptySerial = true
          }
        })
        return new Promise(function (resolve) {
          // 没添加 表格数据，就不验证了
          if (serials.length == 0) {
            resolve('success')
            return
          }
          // 如果有有空的商品编码,不让提交
          if (haveEmptySerial) {
            that.$message.warning('商品编码不能为空')
            resolve('error')
            return
          }
          var obj = {
            'goodsId': that.dataForm.id,//商品ID ,
            'serials': serials,// 规格货号
          }
          validGoodsSeria(obj).then((res) => {
            if (res.code == 200) {
              if (res.data && res.data.length != 0) {
                that.$message.warning(res.data.join(',') + '货号值和系统中其他商品有重复')
                resolve('error')
              } else {
                resolve('success')
              }
            } else {
              that.$message.error(res.mg)
              resolve('error')
            }
          })
        })
      },

      preStep () {
        //关闭当前页面
        let arr = this.$store.state.contentTabs.filter(v => {
          return v.name != this.$route.name
        })
        this.$store.state.contentTabs = arr

        if (this.dataForm.id) {
          this.$router.go(-1)//当详情页
        } else {
          this.$emit('showPageNumberFn', 1)
        }

      },
    }
  }
</script>
<style lang="scss" scoped>

  @import "@/element-ui/theme-variables.scss";
  .eidtGoodsClass{
    .arrrtibuteWrap{
      .arrrtibuteWrap1{
        width: 85%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        .attributeItem{
          margin-right:10px;
          display: flex;
          margin-bottom: 30px;
        }
      }
      .arrrtibuteWrap2{
        .el-form-item{
          display: flex;
          /deep/ .el-form-item__label{
            width:auto !important;
          }
          /deep/ .el-form-item__content{
            margin-left:0 !important;
          }
        }
      }
    }

    /deep/ .el-form-item__content{
      display:block;
    }
    /deep/ .el-form-item__content::before{
      content: none !important;
    }

    .skuSelect{
      min-width: 400px;
    }
    .attributeBtWrap{
      margin-top:20px;
    }
    .nextStepWrap{
      margin-top:40px;
      margin-bottom:40px;
      text-align: center;
    }
    .clickSpan{
      cursor: pointer;
    }
    /deep/ .el-form-item{
      margin-top: 0;
    }

    .rightWarp{
      width: 100px;
      border-left: 2px solid #cac9c9;
      z-index:10;
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
      font-size:14px;
      a{
        color:rgba(0, 0, 0, 0.647058823529412);
        text-decoration:none;
        cursor: pointer;
      }
      .bgCircle{
        visibility: hidden;
        display: flex;
        position: relative;
        left: -7.5px;
        border-radius: 50%;
        width: 14px;
        height: 14px;
        background-color:  $--color-primary;
        align-items: center;
        justify-content: center;
        .smallCircle{
          border-radius: 50%;
          width: 5px;
          height: 5px;
          background-color: white;
        }
      }
    }
    .current {
      color:  $--color-primary;
      .bgCircle{
        visibility: visible;
      }
    }
    .radioGroup{
      margin-top: 13px;
    }
    /deep/ .tabFixed[data-v-769d46ff] > .el-tabs__content{
      margin-bottom: 250px;
    }
    /deep/ .el-form-item__error{
      min-width: 200px;
      line-height: inherit;
      left: 100%;
      top:0;
    }
  }
  </style>
