<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataFormTemp" class="addOrEdit" :rules="dataRule" ref="dataFormTemp" v-loading="couponLoading">
            <div class="fullTitle title-1">
                基本信息
            </div>
            <el-form-item label="满减活动名称：" prop="activityName">
                <el-input v-model.trim="dataFormTemp.activityName" placeholder="请输入" maxlength="20" show-word-limit clearable></el-input>
            </el-form-item>
            <!--startDate  endDate -->
            <el-form-item label="生效时间：" class="specSize" prop="startDate">
                <el-date-picker
                        v-model="dataFormTemp.startDate"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm"
                        placeholder="开始日期"
                >
                </el-date-picker>
                <span style="margin: 0px 5px;">~</span>
                <el-date-picker
                        v-model="dataFormTemp.endDate"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm"
                        placeholder="结束日期"
                >
                </el-date-picker>
            </el-form-item>
            <div class="hrTips">活动到期后将自动失效，失效后不可延长</div>
            <el-form-item label="活动类型：" prop="ruleType">
                <el-radio-group v-model="dataFormTemp.ruleType" @change="changeRuleType">
                    <el-radio label="0">普通满减</el-radio>
                    <el-radio label="1">每满减</el-radio>
                    <el-radio label="2">阶梯满减</el-radio>
                </el-radio-group>
                <div style="display: flex;flex-flow: row;color: #999999;">
                    <div>说明：</div>
                    <div>
                         <div v-if="dataFormTemp.ruleType==0">普通满减：满200减50，金额满200可减免50</div>
                         <div v-if="dataFormTemp.ruleType==1">每满减：每满200减50，金额满200可减免50，满400可减免100，可设封顶金额</div>
                         <div v-if="dataFormTemp.ruleType==2">阶梯满减：满200减50，满300减80，满400减110，金额满200可减50，满300可减110，只减免一次</div>
                    </div>
                </div>
            </el-form-item>
            <div class="fullTitle title-2">{{dataFormTemp.ruleType ==0 ? '普通' : dataFormTemp.ruleType==1 ? '每':'阶梯'}}满减</div>
           <div>
              <div>
                  <el-form-item
                          v-for="(item,index) in dataFormTemp.reduceRuleSaveDTOList"
                          v-if="(dataFormTemp.ruleType!=2 && index==0) || dataFormTemp.ruleType==2"
                          :key="index"
                          label="条件-满："
                          class="specSize title-3"
                          prop="reduceRuleSaveDTOList.0.limitAmount"
                  >
                      <el-input v-model="item.limitAmount">
                          <template slot="append">元</template>
                      </el-input>
                      优惠-减
                      <el-input v-model="item.reduceAmount">
                          <template slot="append">元</template>
                      </el-input>
                      <el-button v-if="index>0" class="btn delBtn" type="primary" plain @click="delItem(item)">删除</el-button>

                  </el-form-item>
              </div>
               <span class="fullTis">
                    提示：输入内容为数字，其他内容输入无效，输入的数字保留小数点后两位，输入的满减条件范围应大于等于0；优惠条件范围应大于0；
                </span>
           </div>
<div>
    <el-button class="btn addBtn" type="primary" plain v-if="dataFormTemp.ruleType==2"  @click="addItem">添加</el-button>
</div>
            <div class="fullTitle title-1">优惠规则</div>
            <el-form-item label="适用商品：" prop="activityGoodsScope">
                <el-radio-group v-model="dataFormTemp.activityGoodsScope">
                    <span @click="changeType(0)"><el-radio label="0">全场通用</el-radio></span>
                    <span @click="changeType(1)" style="margin-left: 28px"><el-radio label="1">指定分类</el-radio></span>
                    <span @click="changeType(2)" style="margin-left: 28px"><el-radio label="2">指定商品</el-radio></span>
                    <span @click="changeType(3)" style="margin-left: 28px"><el-radio label="3">指定品牌</el-radio></span>
                </el-radio-group>
                <div style="color: #999999;" v-if="dataFormTemp.activityGoodsScope==0">
                    全部通用：店铺下全部上架商品通用
                </div>
                <div style="color: #999999;" v-if="dataFormTemp.activityGoodsScope==2">
                    指定商品：选择的指定商品参与
                </div>
                <div style="color: #999999;" v-if="dataFormTemp.activityGoodsScope==3">
                    指定品牌：选择的指定品牌参与
                </div>
            </el-form-item>

        </el-form>
          <div class="title-2" style="text-align: center;">
                <el-button class="btn" type="primary" plain @click="changePage()">取消</el-button>
                <el-button class="btn" type="primary" @click="dataFormSubmit('dataFormTemp')" :loading="saveLoading"> {{saveLoading?"提交中...":"保存"}}</el-button>
            </div>
        <!--后台还没有这个接口-->
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
                <div style="text-align: center;">
                    <el-button class="btn" type="primary" @click="goodsVisible = false">确定</el-button>
                    <el-button class="btn"  type="primary" plain @click="clearChooseObj">取消</el-button>
                </div>
            </el-form>
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
                <div style="text-align: center;">
                    <el-button class="btn" type="primary" @click="brandCommit()">确定</el-button>
                    <el-button class="btn"  type="primary" plain @click="clearChooseObj2">取消</el-button>
                </div>
            </el-form>
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
    import { addFullReduction,editCommitFullReduction,editFullReduction,storeBrandList,getGoodsPage,storeGoodsclass } from '@/api/api'
    export default {
        name: "model-add-or-edit",
        data(){
            var that = this
            /*如输入框内容超过20个汉字时输入框变红，显示提示信息（超出字数限制）*/
            var validActivityName = (rule, value, callback) => {
                var chineseCount = 0, characterCount = 0;
                for (let i = 0; i < value.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(value[i])) { //汉字
                        chineseCount = chineseCount + 2;
                    } else { //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 40) { //输入字符大于100的时候过滤
                     return    callback(new Error('超出字数限制'))
                    }else{
                       return  callback()
                    }
                }
            };
            // 订单金额满额需输入1-9999的整数
            var validLimitAmount = (rule, value, callback) => {
                if (value / 1 > 9999) {
                    callback(new Error('输入错误，输入范围为1-9999'))
                }
                if (value < 1) {
                    callback(new Error('输入错误，输入范围为1-9999'))
                }
                if(/[^\d|\.]/.test(value)){
                    callback(new Error('输入错误，输入范围为1-99999'))
                }
                callback()
            };
            var validTimes = (rule, value, callback) => {
               if(that.dataFormTemp.startDate && that.dataFormTemp.endDate){
                   let that = this
                   let start = new Date(that.dataFormTemp.startDate).getTime()
                   let end = new Date(that.dataFormTemp.endDate).getTime()
                   let time = 0
                   if(start > end){
                       return callback(new Error('开始时间需要小于结束时间'))
                   }else{
                       time = end - start
                       let date = Math.floor(time/86400000)
                       if(date > 1095){
                           callback(new Error('生效时间最长时间跨度为3年'))
                       }
                   }
               }else{
                   callback(new Error('必填项不能为空'))
               }
                callback()
            };
            var validReduceRuleSaveDTOList = (rule, value, callback) => {
                if(/[^\d|\.]/.test(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount)){
                    callback(new Error('输入错误，只能输入数字'))
                }
                if(/[^\d|\.]/.test(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount)){
                    callback(new Error('输入错误，只能输入数字'))
                }

                if(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount == null || that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount == ''){
                    callback(new Error('必填项不能为空'))
                }

                if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount) > 9999999 ){
                    callback(new Error('满减门槛需小于9999999'))
                }

                if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount) > 9999999 ){
                    callback(new Error('优惠门槛需小于9999999'))
                }

                if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount) < 0.01 ){
                    callback(new Error('满减门槛需大于0.01'))
                }

                if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount) < 0.01  ){
                    callback(new Error('优惠门槛需大于0.01'))
                }

                if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount)<parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount) || parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount)==parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount)){
                    callback(new Error('满减门槛需大于优惠门槛'))
                }
                // 阶梯满减 下一条金额大于上一条金额
                if(that.dataFormTemp.reduceRuleSaveDTOList.length >1){
                    let pass = 0;
                    for(let i=1;i<that.dataFormTemp.reduceRuleSaveDTOList.length;i++){
                        //下一条满减金额大于上一条满减金额
                        if(+(that.dataFormTemp.reduceRuleSaveDTOList[i].limitAmount) > +(that.dataFormTemp.reduceRuleSaveDTOList[i-1].limitAmount)
                            && +(that.dataFormTemp.reduceRuleSaveDTOList[i].reduceAmount) > +(that.dataFormTemp.reduceRuleSaveDTOList[i-1].reduceAmount)
                        ){
                            pass++
                            if(parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[i].limitAmount)<parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[i].reduceAmount) || parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].limitAmount)==parseFloat(that.dataFormTemp.reduceRuleSaveDTOList[0].reduceAmount)){
                                callback(new Error('满减门槛需大于优惠门槛'))
                            }
                        }else{

                        }
                    }
                    if( that.dataFormTemp.reduceRuleSaveDTOList.length==0 || pass==that.dataFormTemp.reduceRuleSaveDTOList.length-1){
                        return  callback()
                    }else{
                        return callback(new Error('下一条满减金额需要大于上一条满减金额'))
                    }
                }
                callback()
            };
            return{
                breaddata:[],
                //默认显示 本期不做
                radio:'0',
                //回显数据的时候loading
                couponLoading:true,
                reduceRuleSaveDTOListTemp:[{
                    reduceAmount:'',//减
                    limitAmount:'',//满
                }],
                // 新增优惠券表单
                dataFormTemp: {
                    ruleType: '0',//活动规则
                    activityState : '0',//活动规则
                    activityGoodsScope: '0',//活动商品范围
                    activityName:'',
                    relationIds:[],
                    endDate:'',
                    startDate:'',
                    reduceRuleSaveDTOList:[{
                        reduceAmount:'',//减
                        limitAmount:'',//满
                    }],
                },
                row:'',
                //radio--可用范围
                // 指定商品
                page:1,limit:12,total:0,
                goodsVisible:false,
                classVisible:false,
                tempCanUse:'',
                actionParams:[],
                classDataList:[],
                copybrandOptions:"",
                props: {
                    value: "id",
                    label: "gcName",
                },
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
                // 指定品牌数据
                brandVisible:false,
                brandOptions:[],
                dataArray:[],
                brandDataForm:{
                    brandName:''
                },

                // 提交
                saveLoading:false,
                // 校验规则
                dataRule:{
                    activityName: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validActivityName, trigger: 'blur' },
                    ],
                    limitAmount: [
                        {required: true, message: '订单金额满额需输入1-9999的整数', trigger: 'blur'},
                        {validator: validLimitAmount, trigger: 'blur'},
                    ],
                    activityGoodsScope: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                    ],
                    ruleType: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                    ],
                    startDate: [
                        {required: true, message: '必填项不能为空', trigger: 'change'},
                        { validator: validTimes, trigger: 'change' },
                    ],
                    'reduceRuleSaveDTOList.0.limitAmount': [
                        {required: false, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validReduceRuleSaveDTOList, trigger: 'blur' },
                    ],
                },
            }
        },
        components:{
            Bread,
        },
        methods:{
            init(row){
                this.getClass();
                // 商品列表数据
                this.goodsList()
                if(row){
                    this.breaddata =  ["营销系统", "满减活动列表", "满减活动编辑"];
                    this.row = row ;
                    // 商品列表数据 获取品牌数据 异步回调用promise，否则回显可能获取不到商品或品牌的数据
                        this.brandList().then(()=>{
                            this.backScan(row)
                        })
                }else{
                    // 获取品牌数据
                    this.brandList();
                    //新增关闭loading效果
                    this.couponLoading = false
                    this.breaddata =  ["营销系统", "满减活动列表", "满减活动新增"];
                }
            },
            // 回显
            backScan(row){
                var obj  = {
                    id:row.id,
                    type:"update"
                }
                editFullReduction(obj).then((res=>{
                    if(res.code == 200){
                        this.couponLoading = false
                        // 防止返回数据覆盖this.dataFormTemp
                        Object.assign(this.dataFormTemp,res.data)
                        // 数据回显格式转换一下
                        if(res.data.activityGoodsScope){
                            this.tempCanUse = res.data.activityGoodsScope
                        }
                    this.dataFormTemp.ruleType = res.data.reduceRuleSaveDTOList[0].ruleType.toString()
                        this.dataFormTemp.activityGoodsScope = res.data.activityGoodsScope.toString()
                        // 回显数据中拿到dataFormTemp.relationIds
                        for(let i=0;i<res.data.reduceGoodsDTOList.length;i++){
                            //将relationId给id relationName给brandName
                            res.data.reduceGoodsDTOList[i].id = res.data.reduceGoodsDTOList[i].relationId
                            res.data.reduceGoodsDTOList[i].brandName = res.data.reduceGoodsDTOList[i].relationName
                            res.data.reduceGoodsDTOList[i].gcName = res.data.reduceGoodsDTOList[i].relationName
                            this.dataFormTemp.relationIds.push(res.data.reduceGoodsDTOList[i].id)
                            this.classDataList.find((item,index)=>{
                            item.children.find((item2,index2)=>{
                                if(item2 && item2.id == res.data.reduceGoodsDTOList[i].relationId){
                                    item2.disabled = true
                                    return;
                                }
                            })
                        });
                        }
                        this.tempRelationIds.push(res.data.reduceGoodsDTOList)
                        //
                        this.dataArray = res.data.reduceGoodsDTOList
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
            // 添加阶梯优惠
            addItem() {
                if (this.dataFormTemp.reduceRuleSaveDTOList.length >= 6) {
                    // 阶梯满减规则不能超过6条
                    this.$message({
                        message: '阶梯满减规则不能超过6条',
                        type: 'info',
                        duration: 1500
                    })
                } else if (this.dataFormTemp.reduceRuleSaveDTOList[this.dataFormTemp.reduceRuleSaveDTOList.length - 1].limitAmount == ""
                    || this.dataFormTemp.reduceRuleSaveDTOList[this.dataFormTemp.reduceRuleSaveDTOList.length - 1].reduceAmount == "") {
                    this.$message({
                        message: '请先填写满减规则',
                        type: 'info',
                        duration: 1500
                    })
                } else {
                    this.dataFormTemp.reduceRuleSaveDTOList.push({
                        reduceAmount: '',//减
                        limitAmount: '',//满
                    })
                }
            },
            delItem(item){
                var index = this.dataFormTemp.reduceRuleSaveDTOList.indexOf(item)
                if (index !== -1) {
                    this.dataFormTemp.reduceRuleSaveDTOList.splice(index, 1)
                }
            },
            //点击可用范围切换 出弹窗
            changeType(val){
                // 切换关联类型，清空上个关联类型选择的数据
                // this.dataFormTemp.relationIds = [];
                //全部
                if(val==0){

                }else if(val==1){
                     //指定分类
                    this.classVisible = true
                    if(this.tempCanUse !==1){
                        this.cancelAllClass()
                        this.cancelAll()
                    }
                }else if(val==2){ // 指定商品分类
                    this.goodsList();
                    this.goodsVisible = true;
                    this.cancelAll()
                     this.cancelAllClass()
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
            // 切换活动规则类型
            changeRuleType(){
                this.dataFormTemp.reduceRuleSaveDTOList = [{reduceAmount: '', limitAmount:''}];
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
            // 表单提交
            dataFormSubmit(dataFormTemp){

                this.dataFormTemp.reduceRuleSaveDTOList.forEach((item,index)=>{
                    item['ruleType'] = this.dataFormTemp.ruleType
                })
                // 选择全部 必须传[]
                if(this.dataFormTemp.activityGoodsScope == 0){
                    this.dataFormTemp.relationIds = []
                }
                this.$refs[dataFormTemp].validate((valid) => {
                    if (valid) {
                        this.saveLoading = true;
                        var fn = this.row ? editCommitFullReduction : addFullReduction
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
                this.dataArray.unshift(obj)
                this.tempCanUse = 3;
            },
            // 取消选择风格标签
            handleClose(item) {
                this.dataArray.splice(this.dataArray.indexOf(item), 1);
                this.brandOptions.push(item);
                this.brandDataForm.brandName = ''
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
             clearChooseClass(){
                this.classVisible = false
                this.cancelAllClass()
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

            // 清空所有的tag选项
            cancelAll(){
                // this.brandOptions=this.brandOptions.concat(this.dataArray);
                this.brandOptions=this.copybrandOptions
                this.dataArray = []
                this.brandDataForm.brandName = ''
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
                //有回显的dataFormTemp.relationIds数据 清空回显的数据和指定品牌的选择数据
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
        .fullTitle{
            font-weight: 700;
        }
        .title-1{
            margin-left: -60px;
        }
        .title-2{
            margin-left: 150px;
        }
        .title-3{
            margin-left: 60px;
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
        .fullTis{
            color: #999999;
            margin-left: 5px;
            margin-left: 170px
        }
        .addBtn{
            margin-top: 15px;
            margin-left: 170px;
        }
        .delBtn{
            margin-left: 20px;
            margin-top: 15px;
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
</style>
