<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormTemp" class="addOrEdit" :rules="dataRule" ref="dataFormTemp" v-loading="couponLoading">
<!--            <el-form-item label="优惠券类型：">-->
<!--                <el-radio-group v-model="radio">-->
<!--                    <el-radio label="0">满减券</el-radio>-->
<!--                </el-radio-group>-->
<!--            </el-form-item>-->
            <el-form-item label="优惠券名称：" prop="activityName">
                <el-input v-model.trim="dataFormTemp.activityName" placeholder="请输入优惠券名称" maxlength="20" clearable></el-input>
            </el-form-item>
            <div class="hrTips">优惠券名称最长不超过20个汉字</div>
            <el-form-item label="优惠券数量：" prop="totalNum">
                <el-input v-model.trim="dataFormTemp.totalNum" placeholder="请输入优惠券数量" clearable></el-input>
            </el-form-item>
            <div class="hrTips">请输入1-99999的整数，0或不填为不限量</div>
            <el-form-item label="优惠券面额：" prop="faceValue">
                <el-input v-model.trim="dataFormTemp.faceValue" placeholder="请输入优惠券面额" clearable @blur="changeInput()"></el-input>
            </el-form-item>
            <div class="hrTips">优惠券面额范围为0.01-9999.99元</div>
            <el-form-item label="每人限领券数：" prop="personLimit">
                <el-input v-model.trim="dataFormTemp.personLimit" placeholder="请输入每人限领券数" clearable></el-input>
            </el-form-item>
            <div class="hrTips">请输入1-99999的整数，0或不填为不限量</div>
            <el-form-item  label="使用门槛：" prop="limitAmount" class="specSize">
                订单金额满
                <el-input v-model.trim="dataFormTemp.limitAmount" placeholder="请输入金额" @blur="changeInput()" clearable ></el-input>
                    元可用
            </el-form-item>
            <div class="hrTips">订单金额范围0.01-99999.99元。（最大值不超过十万）</div>
            <!--getStartDate getEndDate-->
            <el-form-item label="领券时间：" prop="valuetime">
                <el-date-picker
                        v-model="valuetime"
                        type="datetimerange"
                        :picker-options="pickerOptions"
                        align="right"
                        unlink-panels
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        @blur='acttime'>
                </el-date-picker>
            </el-form-item>
            <div class="hrTips">请选择优惠券领取的有效时间</div>
            <el-form-item label="优惠券有效期：" prop="validityType">
                <el-radio-group v-model="dataFormTemp.validityType" @change="chooseType">
                    <el-radio label="0">固定时间</el-radio>
                    <el-radio label="1">自领取后开始计算</el-radio>
                </el-radio-group>
            </el-form-item>
            <!--useStartDate  useEndDate -->
            <el-form-item label="起止时间：" v-show="fixedVisible" class="specSize" prop="useStartDate">
                <el-date-picker
                        v-model="dataFormTemp.useStartDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="开始日期"
                >
                </el-date-picker>
                <span style="margin: 0px 5px;">~</span>
                <el-date-picker
                        v-model="dataFormTemp.useEndDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="结束日期"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="有效期天数：" v-show="!fixedVisible" class="specSize"  prop="validityDays">
                <el-input v-model="dataFormTemp.validityDays">
                    <span slot="suffix">天</span>
                </el-input>
            </el-form-item>
            <!-- <div style="color: #999999;margin-left: 150px" >最多90天</div> -->
            <el-form-item label="可用范围：" prop="activityGoodsScope">
                <el-radio-group v-model="dataFormTemp.activityGoodsScope">
                    <span @click="changeType(0)"><el-radio label="0">全场通用</el-radio></span>
                    <span @click="changeType(1)" style="margin-left: 28px"><el-radio label="1">指定分类</el-radio></span>
                    <span @click="changeType(2)" style="margin-left: 28px"><el-radio label="2">指定商品</el-radio></span>
                    <span @click="changeType(3)" style="margin-left: 28px"><el-radio label="3">指定品牌</el-radio></span>
                </el-radio-group>
            </el-form-item>
<!--            <el-form-item label="与其他促销活动叠加：">-->
<!--                <el-radio-group v-model="radio">-->
<!--                    <el-radio label="0">否</el-radio>-->
<!--                </el-radio-group>-->
<!--            </el-form-item>-->
            <el-form-item style="text-align: center;">
                <el-button class="btn" type="primary" @click="dataFormSubmit('dataFormTemp')" :loading="saveLoading"> {{saveLoading?"提交中...":"确认"}}</el-button>
                <el-button class="btn" type="primary" plain @click="changePage()" plain>取消</el-button>
            </el-form-item>
        </el-form>
        <!--选择指定商品-->
        <el-dialog
                title="选择指定商品"
                :visible.sync="goodsVisible"
                width="70%"
        class="goodsDialog">
            <el-form :inline="true" :model="goodsDataForm">
                <el-form-item label="输入搜索：" style="margin-left: 28px;">
                    <el-input  placeholder="商品名称\商品货号" clearable v-model="goodsDataForm.goodsName"></el-input>
                    <el-button class="btn" type="primary" @click="goodsList()" style="margin-left: 20px">查询</el-button>
                </el-form-item>
               <div class="flexLayout" v-loading="goodsLoading">
                  <div v-for="(item,index) in goodsOptions" :key="index" class="blueBlock">
                      <div :class="chooseFn(item) ? 'chooseContent' : 'content'" @click="chooseGoods(item)">
                         <div class="displayImg">
                            <div class="goodsImg">
                             <img :src="item.pictureUrl | filterImgUrl" alt="" style="height:80px;width:80px" />
                                <img src="@/assets/images/hook.png" v-show="chooseFn(item)"  style="height:auto;width:80px;position: absolute;left: 0" />
                         </div>
                             <div class="goodsName">
                                 <div class="goodsTitle" :title="item.goodsName">
                                     {{item.goodsName}}
                                 </div>
                                <div class="goodsPrice">
                                    ￥ {{item.specSellPrice}}
                                </div>
                             </div>
                         </div>
                      </div>
                  </div>
               </div>
                <!-- 分页 -->
              <div style=" width: 90%;">
                  <el-pagination
                          @size-change="handleSizeChange"
                          @current-change="handleCurrentChange"
                          :current-page="page"
                          :page-sizes="[10, 20, 50, 100]"
                          :page-size="limit"
                          :total="total"
                          layout="total, prev, pager, next, jumper">
                  </el-pagination>
              </div>
            </el-form>
            <div style="text-align: center;">
                <el-button class="btn" type="primary" @click="goodsVisible = false">确定</el-button>
                <el-button class="btn"  type="primary" plain @click="clearChooseObj" plain>取消</el-button>
            </div>
        </el-dialog>
        <!--选择指定品牌-->
        <el-dialog
                title="添加指定品牌"
                :visible.sync="brandVisible"
                width="44%"
        class="brandDialog">
            <el-form :inline="true" :model="brandDataForm">
                <el-form-item label="品牌选择：">
                    <el-select v-model="brandDataForm.brandName"
                               filterable
                               placeholder="请选择"
                               @change="changeSelect"
                    >
                        <el-option
                                v-for="item in brandOptions"
                                :key="item.id"
                                :label="item.brandName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
<!--                <el-form-item label="tag：">-->
                    <div class="elTagWarp">
                        <el-tag closable
                                v-for="(item,index) in dataArray"
                                :key="item.index"
                                :label="item.brandName"
                                :value="item.id"
                                @close="handleClose(item)"
                                class="tag">
                            {{item.brandName}}
                        </el-tag>
                    </div>
               <div class="clear">
                   <el-button class="btn" type="primary" @click="cancelAll()">清空</el-button>
               </div>
            </el-form>
            <div style="text-align: center;">
                 <el-button class="btn" type="primary" @click="brandCommit()">确定</el-button>
                <el-button class="btn"  type="primary" @click="clearChooseObj2" plain>取消</el-button>
            </div>
        </el-dialog>
         <!--选择指定分类-->
        <el-dialog
                title="添加指定分类"
                :visible.sync="classVisible"
                width="44%"
        class="brandDialog">
            <el-form :inline="true">
                <el-form-item label="选择分类：" >
                    <el-cascader
                        :options="classDataList"
                        change-on-select
                        v-model="actionParams"
                        :clearable="true"
                        :props="props"
                        @change="changeSelectClass">
                    </el-cascader>
                </el-form-item>
<!--                <el-form-item label="tag：">-->
                    <div class="elTagWarp">
                        <el-tag closable
                                v-for="(item,index) in dataArray"
                                :key="item.index"
                                :label="item.gcName"
                                :value="item.id"
                                @close="handleCloseClass(item)"
                                class="tag">
                            {{item.gcName}}
                        </el-tag>
                    </div>
               <div class="clear">
                   <el-button class="btn" type="primary" @click="cancelAllClass()">清空</el-button>
               </div>
            </el-form>
            <div style="text-align: center;">
                 <el-button class="btn" type="primary" @click="brandCommit()">确定</el-button>
                <el-button class="btn"  type="primary" @click="clearChooseClass" plain>取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import { addCoupons,editCommit,editCoupons,storeBrandList,getGoodsPage,storeGoodsclass } from '@/api/api'
import { Alert } from 'element-ui';
    export default {
        name: "model-add-or-edit",
        data(){
            /*如输入框内容超过20个汉字时输入框变红，显示提示信息（超出字数限制）*/
            var validActivityName = (rule, value, callback) => {
                if(this.limitCountStatus){
                    callback()
                }else{
                    callback(new Error('超出字数限制'))
                }
            };
            /*输入非数字或不在此范围的数字时提示（输入错误，输入范围为1-99999）*/
            var validTotalNum = (rule, value, callback) => {
                if(parseInt(value)>99999){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }else if(parseInt(value)<0){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }
                if(/[^\d]/.test(value)){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }
                callback()
            };
            // 面额
            var validFaceValue = (rule, value, callback) => {
                if (value / 1 > 10000) {
                    callback(new Error('输入错误，输入范围为0.01-9999.99'))
                }
                if (value < 0.01) {
                    callback(new Error('输入错误，输入范围为0.01-9999.99'))
                }
                if(/[^\d|\.]/.test(value)){
                    callback(new Error('输入错误，输入范围为0.01-9999.99'))
                }
                callback()
            };
            // 订单金额满额最大99999.99
            var validLimitAmount = (rule, value, callback) => {
                if(parseFloat(value)>parseFloat(99999.99)){
                    callback(new Error('订单金额范围0.01-99999.99元。（最大值不超过十万）'))
                }
                if(/[^\d|\.]/.test(value)){
                    callback(new Error('订单金额范围0.01-99999.99元。（最大值不超过十万）'))
                }
                callback()
            };
            //每人限领券数
            var validPersonLimit = (rule, value, callback) => {
                if(parseInt(value)>99999){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }else if(parseInt(value)<0){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }
                if(/[^\d]/.test(value)){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }
                callback()
            };
             // 固定时间 --》 起止时间
            var validUseStartDate = (rule, value, callback) => {
                let that = this
                let start = new Date(that.dataFormTemp.useStartDate).getTime()
                let end = new Date(that.dataFormTemp.useEndDate).getTime()
                let time = 0
                if(start > end){
                    return callback(new Error('开始时间需要小于结束时间'))
                }else{
                    time = end - start
                    let date = Math.floor(time/86400000)
                    if(date > 90){
                        callback(new Error('最多90天'))
                    }
                }
                callback()
            };
//领券时间
            var valueTimeProp = (rule, value, callback) => {
                let that = this
                let start = new Date(that.valuetime[0]).getTime()
                let end = new Date(that.valuetime[1]).getTime()
                let time = 0
                if(start > end){
                    return callback(new Error('开始时间需要小于结束时间'))
                }else{
                    time = end - start
                    let date = Math.floor(time/86400000)
                    if(date > 90){
                        callback(new Error('最多90天'))
                    }
                }
                callback()
            };
            // 自领取后开始计算 --》 有效期天数
            var validalidityDays = (rule, value, callback) => {
                if(!this.fixedVisible){
                    if(/[^\d]/.test(value)){
                        callback(new Error('有效天数只能为整数'))
                    }else if(parseInt(value)>90){
                        callback(new Error('最多90天'))
                    }
                }
                callback()
            };
            return{
                //只能选择今天和今天之后的时间且不超过90天
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
                    }
                },
                breaddata:[],
                limitCountStatus:true,
                //默认显示 本期不做
                // radio:'0',
                //领券时间
                valuetime:[],
                //回显数据的时候loading
                couponLoading:true,
                classDataList:[],
                // 新增优惠券表单
                dataFormTemp: {
                    valuetime:'',
                    activityGoodsScope: '0',
                    activityName:'',
                    faceValue:'',
                    getEndDate:'',
                    getStartDate:'',
                    limitAmount:'',
                    personLimit:'',
                    relationIds:[],
                    totalNum:'',
                    useEndDate:'',
                    useStartDate:'',
                    validityDays:'',
                    validityType:'0',
                },
                row:'',
                //radio--可用范围
                // 指定商品
                page:1,limit:12,total:0,
                goodsVisible:false,
                goodsLoading:true,
                goodsDataForm:{
                    goodsName:''
                },
                // 存返回的relationIds数据
                tempRelationIds:[],
                // 存返回的可用范围
                tempCanUse:'',
                goodsOptions:[],
                dataList:[],
                copybrandOptions:"",
                actionParams:[],
                props: {
                    value: "id",
                    label: "gcName",
                },
                // 指定品牌数据
                brandVisible:false,
                classVisible:false,
                brandOptions:[],
                dataArray:[],
                brandDataForm:{
                    brandName:''
                },
                // radio--优惠券有效期
                fixedVisible:true,// true 是起止时间 false是有效期天数
                // 提交
                saveLoading:false,
                // 校验规则
                dataRule:{
                    activityName: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validActivityName, trigger: 'blur' },
                    ],
                    totalNum: [
                        {required: false, message: '输入错误，输入范围为1-99999', trigger: 'blur'},
                        { validator: validTotalNum, trigger: 'blur' },
                    ],

                    faceValue: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        {validator: validFaceValue, trigger: 'blur'},
                    ],
                    personLimit: [
                        {required: false, message: '输入错误，输入范围为1-99999', trigger: 'blur'},
                        {validator: validPersonLimit, trigger: 'blur'},
                    ],
                    limitAmount: [
                        {required: true, message: '订单金额范围0.01-99999.99元。(最大值不超过十万)', trigger: 'blur'},
                        {validator: validLimitAmount, trigger: 'blur'},
                    ],
                    validityType: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'}
                    ],
                    // 起止时间
                    useStartDate: [
                        {required: false, message: '最多90天', trigger: 'blur'},
                        {validator: validUseStartDate, trigger: 'blur'},
                    ],
                    // 有效期天数
                    validityDays: [
                        {required: true, message: '最多90天', trigger: 'blur'},
                        {validator: validalidityDays, trigger: 'blur'},
                    ],
                    activityGoodsScope: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                    ],
                    valuetime: [
                        {required: true, message: '必填项不能为空', trigger: 'change'},
                        {validator: valueTimeProp, trigger: 'change'},
                    ],
                },
            }
        },
        components:{
            Bread,
        },
        watch:{
            'dataFormTemp.activityName':function(newV,oldV){
                var chineseCount = 0, characterCount = 0;
                for (let i = 0; i < newV.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
                        chineseCount = chineseCount + 2;
                    } else { //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 40) { //输入字符大于40的时候
                        this.limitCountStatus = false
                    }else{
                        this.limitCountStatus = true
                    }
                }
            }
        },
        methods:{
           init(row){
               this.getClass();
               // 商品列表数据
               this.goodsList()
                if(row){
                    this.breaddata =  ["营销系统", "优惠券管理", "编辑"];
                    this.row = row ;
                    //获取品牌数据 异步回调用promise，否则回显可能获取不到商品或品牌的数据
                        this.brandList().then(()=>{
                            this.backScan(row)
                        })
                }else{
                    // 获取品牌数据
                    this.brandList();
                    //新增关闭loading效果
                    this.couponLoading = false
                    this.breaddata =  ["营销系统", "优惠券管理", "新增"];
                }
           },
            // 回显
            backScan(row){
                var obj  = {
                    id:row.id,
                    type:"update"
                }
                editCoupons(obj).then((res=>{
                    if(res.code == 200){
                        this.couponLoading = false
                        // 防止返回数据覆盖this.dataFormTemp
                        Object.assign(this.dataFormTemp,res.data)
                        if(res.data.validityType == 0){
                            this.fixedVisible = true
                        }else{
                            this.fixedVisible = false
                        }
                    // 数据回显格式转换一下
                        this.valuetime = [res.data.getStartDate,res.data.getEndDate]
                        this.dataFormTemp.valuetime = this.valuetime
                        this.dataFormTemp.validityType = res.data.validityType.toString()
                        if(res.data.activityGoodsScope){
                            this.tempCanUse = res.data.activityGoodsScope
                        }
                        this.dataFormTemp.activityGoodsScope = res.data.activityGoodsScope.toString()

                        // 回显数据中拿到dataFormTemp.relationIds
                        for(let i=0;i<res.data.couponsGoodsDTOList.length;i++){
                            //将relationId给id relationName给brandName
                            res.data.couponsGoodsDTOList[i].id = res.data.couponsGoodsDTOList[i].relationId
                            res.data.couponsGoodsDTOList[i].brandName = res.data.couponsGoodsDTOList[i].relationName
                            res.data.couponsGoodsDTOList[i].gcName = res.data.couponsGoodsDTOList[i].relationName
                            this.dataFormTemp.relationIds.push(res.data.couponsGoodsDTOList[i].id)
                            this.classDataList.find((item,index)=>{
                                item.children.find((item2,index2)=>{
                                    if(item2 && item2.id == res.data.couponsGoodsDTOList[i].relationId){
                                        item2.disabled = true
                                        return;
                                    }
                                })
                            });
                        }

                    
                        this.tempRelationIds.push(res.data.couponsGoodsDTOList)
                        //
                        this.dataArray = res.data.couponsGoodsDTOList
                        var brandOptions =  this.brandOptions.filter((item,index)=>{
                            var _index = this.dataArray.findIndex((item2,index2)=>{
                                return item.id == item2.id
                            })
                            if(_index==-1){
                                return true
                            }else{
                                return false
                            }
                        })
                        this.brandOptions = brandOptions;
                    }else{
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500
                        })
                        // 跳回上一页 刷新列表
                        this.$emit('changePage')
                    }
                }))
            },
            //领券时间
            acttime() {
                if(this.valuetime && this.valuetime.length!=0){
                    this.dataFormTemp.getStartDate = this.valuetime[0];
                    this.dataFormTemp.getEndDate = this.valuetime[1];
                    this.dataFormTemp.valuetime = this.valuetime
                }else{
                    this.dataFormTemp.getStartDate = "";
                    this.dataFormTemp.getEndDate = ""
                    this.dataFormTemp.valuetime = "";
                }
            },
            //点击可用范围切换 出弹窗
            changeType(val){
                // 切换关联类型，清空上个关联类型选择的数据
                // this.dataFormTemp.relationIds = [];
                //选择全部时
                if(val==0){

                }else if(val==2){ // 指定商品分类
                    this.goodsList();
                    this.goodsVisible = true;
                    this.cancelAll()
                    this.cancelAllClass()
                } else if(val==1){
                     //指定分类
                    this.classVisible = true
                    if(this.tempCanUse !==1){
                        this.cancelAllClass()
                         this.cancelAll()
                    }
                }else{
                    //指定品牌分类
                    this.brandVisible = true
                    if(this.tempCanUse ==1){
                        this.cancelAllClass()
                    }
                    // 如果回显的数据不是品牌数据 清空品牌的选择
                    if(this.tempCanUse !==3){
                        this.cancelAll()
                    }
                }
            },
            changeInput(){
                if (this.dataFormTemp.limitAmount) {
                    this.dataFormTemp.limitAmount = Number(this.dataFormTemp.limitAmount.match(/^\d+(?:\.\d{0,2})?/))
                }
                if (this.dataFormTemp.faceValue) {
                    this.dataFormTemp.faceValue = Number(this.dataFormTemp.faceValue.match(/^\d+(?:\.\d{0,2})?/))
                }
            },
            // 指定商品分类
            brandList(){
                let obj = {
                    brandName:this.brandDataForm.brandName,
                }
               return storeBrandList().then((res)=>{
                    if(res.code == 200){
                        this.brandOptions = res.data
                        this.copybrandOptions = res.data
                    }
                })
            },
            //指定商品列表
            goodsList(){
                var obj={
                    params:{
                        goodsShow:1,
                        page:this.page,
                        limit:this.limit,
                        goodsName:this.goodsDataForm.goodsName
                    }
                }
                getGoodsPage(obj).then(res=>{
                    if(res.code == 200){
                        this.goodsLoading = false
                        this.goodsOptions = res.data.list
                        this.total = res.data.total
                    }
                })
            },
            chooseType(val){
               if(val==0){
                   this.fixedVisible = true
               }else{
                   this.fixedVisible = false
               }
            },
            // 表单提交
            dataFormSubmit(dataFormTemp){
               // 选择全部 必须传[]
               if(this.dataFormTemp.activityGoodsScope == 0){
                   this.dataFormTemp.relationIds = []
               }
               //优惠券有效期-自领取后计算清空
               if(this.dataFormTemp.validityType==0){
                   this.dataFormTemp.validityDays = 0
               }else{
                   this.dataFormTemp.useStartDate = ''
                   this.dataFormTemp.useEndDate = ''
               }
                this.acttime();
               var that = this
                that.$refs[dataFormTemp].validate((valid) => {
                    if (valid) {
                        // 面额faceValue
                        if(parseFloat(that.dataFormTemp.faceValue) > parseFloat(that.dataFormTemp.limitAmount) || parseFloat(that.dataFormTemp.faceValue) == parseFloat(that.dataFormTemp.limitAmount)){
                            that.$message({
                                message: "提交失败，面额必须小于使用门槛",
                                type: "error",
                                duration: 1500
                            })
                            return false
                        }
                        this.saveLoading = true;
                        var fn = this.row ? editCommit : addCoupons
                        fn(this.dataFormTemp).then((res=>{
                            this.saveLoading = false;
                            let status = null;
                            if(res.code == "200"){
                                status = "success";
                               this.changePage()
                            }else{
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500
                            })
                        }))
                    }else{
                        return false;
                    }
                })
            },
            /*弹窗-----指定品牌*/
            changeSelect(val){
                // 找到下拉选中的obj
                var obj = this.brandOptions.find((item,index)=>{
                    return item.id == val;
                });
                    // 从下拉中去掉已选的
                    var brandOptions = this.brandOptions.filter((item,index)=>{
                        return item.id != val;
                    });
                    this.brandOptions = brandOptions;
                    //  已关联的新增一条
                    this.dataArray.unshift(obj);
                this.tempCanUse = 3;
            },
            // 弹窗选择指定分类
            changeSelectClass(val){
                if(val.length==2){
                    this.classDataList.find((item,index)=>{
                        item.children.find((item2,index2)=>{
                            if(item2 && item2.id == val[val.length-1]){
                                this.dataArray.unshift(item2);
                                // item.children.splice(item.children.indexOf(item2),1);
                                item2.disabled = true
                                return;
                            }
                        })
                    });
                }
                this.tempCanUse = 1;
            },
            // 取消选择风格标签
            handleClose(item) {
                this.dataArray.splice(this.dataArray.indexOf(item), 1);
                this.brandOptions.push(item);
                this.brandDataForm.brandName = ''
            },
            /**取消选中分类 */
            handleCloseClass(value){
                this.dataArray.splice(this.dataArray.indexOf(value), 1);
                this.classDataList.find((item,index)=>{
                        item.children.find((item2,index2)=>{
                            if(item2 && item2.id == value.id){
                                item2.disabled = false
                                return;
                            }
                        })
                    });
            },
            // 清空所有的tag选项
            cancelAll(){
                // this.brandOptions=this.brandOptions.concat(this.dataArray);
                this.dataArray = []
                this.brandOptions=this.copybrandOptions
                this.brandDataForm.brandName = ''
            },
            //清空所有分类
            cancelAllClass(){
                this.dataArray = []
                this.actionParams=[],
                    this.classDataList.find((item,index)=>{
                    item.children.find((item2,index2)=>{
                        if(item2){
                            item2.disabled = false
                            return;
                        }
                    })
                });

            },
            //分类类表
            getClass(){
                var obj={
                    params:{
                    'type':'goodsDeatil'
                    }
                }
                
                storeGoodsclass(obj).then((res)=>{
                    console.log('店铺分类',res)
                    this.classDataList = res.data;
                })
            },
            // 点击确定按钮 将tag数据存入dataFormTemp
            brandCommit(){
               var dataArrayId = []
                    for(let i=0;i<this.dataArray.length;i++){
                        dataArrayId.push(this.dataArray[i].id)
                    }
                this.dataFormTemp.relationIds = dataArrayId
                this.brandVisible = false
                this.classVisible = false
                
            },
            /*弹窗-----指定商品*/
            //指定商品的样式
            chooseFn(itemargu){
               if(this.dataFormTemp.relationIds){
                   let _index = this.dataFormTemp.relationIds.findIndex((item,index)=>{
                       return item == itemargu.id
                   })
                   if(_index==-1){
                       return false
                   }else{
                       return  true;
                   }
               }
            },
             /* 选择指定商品*/
            chooseGoods(itemargu){
                //有回显的dataFormTemp.relationIds数据 清空回显的数据
                if(this.tempRelationIds.length > 0){
                    this.dataFormTemp.relationIds = []
                    this.tempRelationIds = []
                }
                //点击选择的模块在relationIds中，则取消选择，同时删除relationIds中的数据
                    let _index = this.dataFormTemp.relationIds.findIndex((item,index)=>{
                        return item == itemargu.id
                    })
                    if(_index==-1){
                        this.dataFormTemp.relationIds.push(itemargu.id)
                    }else{
                        this.dataFormTemp.relationIds.splice(this.dataFormTemp.relationIds.indexOf(itemargu.id), 1);
                    }
            },
            clearChooseObj(){
                this.goodsDataForm.goodsName = '';
                this.dataFormTemp.relationIds = []
                this.goodsVisible = false
            },
            clearChooseObj2(){
                this.brandVisible = false
                this.cancelAll()
            },

            clearChooseClass(){
                this.classVisible = false
                this.cancelAllClass()
            },
            /*指定商品分页*/
            handleSizeChange(val) {
                this.limit = val
                this.goodsList()
            },
            handleCurrentChange(val) {
                this.page = val
                this.goodsList()
            },
            // 返回上一页
            changePage(){
                this.$emit('changePage')
            }
        }
    }
</script>

<style lang="scss" scoped>
    // 新增或编辑表单
    .addOrEdit{
        margin-left: 10%;
        .hrTips{
            color: #999999;
            margin-left: 150px;
            margin-bottom: 10px;
        }
        .el-form-item{
            display: block;
        }
        /deep/ .el-form-item__label{
            width: 150px !important;
            text-align: right;
        }
        /deep/ .el-form-item__error{
            position: relative !important;
            display: inline-block;
            margin-left: 10px;
        }
        .el-input{
            width: 400px !important;
        }
        .specSize{
            .el-input{
                width: 200px !important;
            }
        }
    }
    // 添加指定品牌
    .brandDialog{
        .elTagWarp{
            border: 1px solid #cccccc;
            padding: 12px 12px 12px 0px;
            margin-bottom: 12px;
            min-height: 120px;
            .el-tag{
                margin-left: 12px;
                margin-bottom: 12px;
            }
        }
        /deep/ .el-dialog__header{
            border-bottom: 1px solid #cccccc;
        }
        .clear{
            height: 60px;
            display: flex;
            align-items: flex-end;
            justify-content: flex-end;
        }
    }
    // 指定商品
    .goodsDialog{
        .flexLayout{
            display: flex;
            width: 90%;
            flex-wrap: wrap;
            margin: 0 15px;
           .blueBlock{
               width: 33%;
              .content{
                  border: 1px solid #cccccc;
                  min-height: 100px;
                  margin: 12px;
              }
               .chooseContent{
                   border: 1px solid #3f51af;
                   min-height: 100px;
                   margin: 12px;
               }
               .displayImg{
                   display: flex;
                   margin: 10px;
                   .goodsImg{
                       position: relative;
                       width: 80px;
                       height: 80px;
                       display: inline-block;
                       margin-right: 10px;
                   }
                   .goodsName{
                       display: inline-block;
                       .goodsTitle{
                           width: 100%;
                           line-height: 22px;
                           overflow: hidden;
                           text-overflow: ellipsis;
                           display: -webkit-box;
                           -webkit-line-clamp: 2;
                           line-clamp: 2;
                           -webkit-box-orient: vertical;
                       }
                       .goodsPrice{
                           margin-top: 5px;
                       }
                   }
               }
           }
        }
    }
    /deep/ .el-form-item{
        margin-bottom: 3px !important;
    }
</style>
