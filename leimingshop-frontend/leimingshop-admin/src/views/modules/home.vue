<template>
  <!-- style="background:#b4bbc5" -->
  <div class="homePageWarp">
    <div class="mod-home" style="background:#FFF;margin: 0px 10px 0px 10px;">

      <h3><span class="titleClassStyle" style="margin-left: 10px;">|</span> 管理端基础概况</h3>
      <div style="margin-left: 10px;margin-top: -10px;color:#999999">
        <el-select size="small" v-model="value" @change="getBaseData(0)" placeholder="请选择">
          <el-option label="今天" :value="0"></el-option>
          <el-option label="昨天" :value="1"></el-option>
          <el-option label="近7天" :value="2"></el-option>
          <el-option label="近15天" :value="3"></el-option>
          <el-option label="近30天" :value="4"></el-option>
        </el-select>
        <span style="margin-left: 20px;font-size: 15px;">数据更新时间：{{baseManagerData.updateDate}}
          <i class="el-icon-refresh"
            style="cursor:pointer;color:rgba(34,96,210,1);margin-left: 20px;font-size: 18px;vertical-align: text-bottom;"
            @click="getBaseData(1)"></i>
        </span>
        <!-- <el-button   class="btn" size="mini" style="margin-left: 20px;font-size: 15px;"   type="text"  >刷新</el-button> -->

      </div>
      <!-- <div class="baseMangerClass" v-for="(item,index) in  [0,1]">
          <div  class="baseMangerItem" v-for="(item2,index2) in  managerBaseOpction" v-if="(item==0&&index2<4) || (item==1&&index2>3)">
            <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>{{item2.name}}</span>
              <div >
                <span>{{ item2.subName }}</span>
                <el-button type="text" class="button">{{item2.price}}</el-button>
              </div>
            </div>
          </div>
        </div> -->
      <div style="display: flex; flex-wrap: wrap; margin-top: 20px;margin-bottom: 25px;">
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(255,178,91,1) 0%,rgba(239,128,0,1) 100%);">
          <div class="text item">
            <div class="card_content">￥{{baseManagerData.subOrderAmount}}</div>
            <div class="baseMangerClassText">{{'下单金额'}}</div>
            <div class="baseHintText">{{'平台所选时间内全部的订单金额'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(101,170,255,1) 0%,rgba(60,138,235,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.newMemberCount}}</div>
            <div class="baseMangerClassText">{{'新增会员'}}</div>
            <div class="baseHintText">{{'所选时间内的新增会员数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(65,208,204,1) 0%,rgba(13,170,166,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.newStoreCount}}</div>
            <div class="baseMangerClassText">{{'新增商户'}}</div>
            <div class="baseHintText">{{'所选时间内的新增申请商户数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(255,119,119,1) 0%,rgba(229,91,91,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.newGoodsCount}}</div>
            <div class="baseMangerClassText">{{'新增商品'}}</div>
            <div class="baseHintText">{{'所选时间内的新增商品数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(109,165,255,1) 0%,rgba(65,131,240,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.orderCount}}</div>
            <div class="baseMangerClassText">{{'订单数'}}</div>
            <div class="baseHintText">{{'平台所选时间内全部的订单数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(255,117,117,1) 0%,rgba(222,66,66,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.memberCount}}</div>
            <div class="baseMangerClassText">{{'会员数量'}}</div>
            <div class="baseHintText">{{'平台所有会员数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(217,120,252,1) 0%,rgba(221,47,176,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.storeCount}}</div>
            <div class="baseMangerClassText">{{'商户数量'}}</div>
            <div class="baseHintText">{{'平台所有商户数量'}}</div>
          </div>
        </el-card>
        <el-card class="box-card"
          style="background:linear-gradient(135deg,rgba(255,178,91,1) 0%,rgba(239,128,0,1) 100%);">
          <div class="text item">
            <div class="card_content">{{baseManagerData.goodsCount}}</div>
            <div class="baseMangerClassText">{{'商品数量'}}</div>
            <div class="baseHintText">{{'平台所有商品数量'}}</div>
          </div>
        </el-card>

      </div>
    </div>
    <!-- margin: 10px 10px 5px 10px -->
    <div style="background:#FFF;">
      <div class="baseMangerClass">
        <!-- style="padding-left: 20px;" -->
        <h3><span class="titleClassStyle" style="margin-left: 10px;">|</span> 常用功能</h3>
        <el-button class="btn" size="mini" style="margin-right: 110px; font-size: 15px;" @click="editUserFunction"
          type="text">自定义</el-button>

      </div>
      <div style=" display: flex; width:100%;">
        <div class="frenFnclass" v-for="(item,index) in frenFnOpction" :key="index" @click="getRoute(item.menuUrl)">
          <!-- <i :class="item.menuIcon"></i> -->
          <div
            style="width:80px; height:80px;background:rgba(93,150,254,1); display: flex;align-items: center;justify-content: center;border-radius:10px;">
            <svg class="icon-svg aui-sidebar__menu-icon"
              style="   border-radius: 10px;font-size: 40px ;fill: #FFF; width: 40px !important; height: 40px !important;"
              aria-hidden="true">
              <use :xlink:href="`#${item.menuIcon}`" v-if="item.menuIcon"></use>
              <use xlink:href="#icon-home" v-else></use>
            </svg>
          </div>
          <span>{{item.menuName}}</span>
        </div>
      </div>
    </div>


    <div style="display: flex; margin-top:20px">
      <div style="width:50%;background:#FFF; margin-left: 10px; margin-right: px;">
        <div class="baseMangerClass">
          <h3><span class="titleClassStyle">|</span> 商户销售排名</h3>
          <!-- <span style="color: blue;padding-right: 30px;" @click="goOrderStatistic">查看更多</span> -->
        </div>
        <div>
          <el-select size="small" v-model="storeOrderBy" @change="getStoreSellRanking(0)" placeholder="请选择">
            <el-option label="成交额" :value="1"></el-option>
            <el-option label="订单数" :value="2"></el-option>
          </el-select>

          <el-select size="small" v-model="storeDateType" @change="getStoreSellRanking(0)" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <span style="padding-left: 20px;font-size: 15px;">数据更新时间：{{storeSellData.date}}
            <i class="el-icon-refresh"
              style="cursor:pointer;color:rgba(34,96,210,1);margin-left: 20px;font-size: 18px;vertical-align: text-bottom;"
              @click="getStoreSellRanking(1)"></i>
          </span>
        </div>
        <div style="margin-top:30px;" v-loading="storeRakingLoading">
          <div v-for="(item,index) in  storeSellData.data" :key="index" class="greyBgcolor"
            style="margin-bottom: 10px;border-radius: 23px;width:708px;height: 32px;display: flex;align-items: center;position: relative;">

            <el-tooltip class="item" effect="dark" :content="item.storeName" placement="top-start">
              <span
                style="z-index: 2;margin-left: 10px; text-overflow: ellipsis;overflow: hidden;white-space: nowrap; ">
                {{item.storeName}}
              </span>
            </el-tooltip>
            <div style="display:flex;align-items: center; border-radius: 23px;position: absolute;">
              <!-- <el-progress v-if="storeOrderBy=='1'" :text-inside="true" :stroke-width="26" :percentage="item.sellOrderAmount*10+10">
                </el-progress>
                 <el-progress v-if="storeOrderBy=='2'" :text-inside="true" :stroke-width="26" :percentage="item.sellOrderCount*10+10">
                </el-progress> -->
              <div class="progress"
                :style="{width:computeStoreWidth(item.sellOrderAmount,'sellOrderAmount'),opacity:1-index*0.1}"
                style="background:linear-gradient(270deg,rgba(93,150,254,1) 0%,rgba(93,150,254,1) 100%);"
                v-if="storeOrderBy=='1'"></div>
              <div class="progress"
                :style="{width:computeStoreWidth(item.sellOrderCount,'sellOrderCount'),opacity:1-index*0.1}"
                style="background:linear-gradient(270deg,rgba(93,150,254,1) 0%,rgba(93,150,254,1) 100%);"
                v-if="storeOrderBy=='2'"></div>

            </div>
            <div style="position: absolute;right: 0;margin-right: 5px;">
              <span v-if="storeOrderBy=='1'" class="appendImg">{{item.sellOrderAmount}}元</span>
              <span v-if="storeOrderBy=='2'" class="appendImg">{{item.sellOrderCount}}件</span></div>
          </div>
        </div>
      </div>

      <div style="width:50%;background:#FFF;margin-right: 10px;margin-left: 10px;">
        <div class="baseMangerClass">
          <h3><span class="titleClassStyle">|</span> 商品销量排名</h3>
          <!-- <span style="color: blue;padding-right: 30px;" @click="goGoodsStatistic">查看更多</span> -->
          <el-button class="btn" size="mini" style="margin-right: 95px; font-size: 15px;" @click="goGoodsStatistic"
            type="text">查看更多</el-button>

        </div>
        <div>
          <el-select size="small" v-model="goodsOrderBy" @change="getGoodsSellRanking(0)" placeholder="请选择">
            <el-option label="销售额" :value="1"></el-option>
            <el-option label="销量" :value="2"></el-option>
          </el-select>
          <el-select size="small" v-model="goodsDateType" @change="getGoodsSellRanking(0)" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <span style="padding-left: 20px;font-size: 15px;">数据更新时间：{{goodsSellData.date}}
            <i class="el-icon-refresh"
              style="cursor:pointer;color:rgba(34,96,210,1);margin-left: 20px;font-size: 18px;vertical-align: text-bottom;"
              @click="getGoodsSellRanking(1)"></i>
          </span>
        </div>
        <div style="margin-top:30px;" v-loading="goodsRakingLoading">
          <div v-for="(item,index) in  goodsSellData.data" :key="index" class="greyBgcolor"
            style="position: relative;margin-bottom: 10px;border-radius: 20px;width:708px;height: 32px;display: flex;align-items: center;">
            <el-tooltip class="item" effect="dark" :content="item.goodsName" placement="top-start">
              <div
                style="z-index: 2;margin-left: 10px;width:203px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap; backgroud:red">
                {{item.goodsName}}
              </div>
            </el-tooltip>
            <div style="display:flex;align-items: center border-radius: 20px;position: absolute;">
              <!-- <el-progress v-if="goodsOrderBy=='1'" :text-inside="true" :stroke-width="26"  :percentage="item.sellGoodsAmount">
                  </el-progress>
                  <el-progress v-if="goodsOrderBy=='2'" :text-inside="true" :stroke-width="26" :percentage="item.sellGoodsCount">
                  </el-progress> -->
              <div class="progress"
                :style="{width:computeGoodsWidth(item.sellGoodsAmount,'sellGoodsAmount'),opacity:1-index*0.1}"
                style="background:rgba(254,175,86,0.8);" v-if="goodsOrderBy=='1'"></div>
              <div class="progress"
                :style="{width:computeGoodsWidth(item.sellGoodsCount,'sellGoodsCount'),opacity:1-index*0.1}"
                style="background:rgba(254,175,86,0.8);" v-if="goodsOrderBy=='2'"></div>

            </div>
            <div style="position: absolute;right: 0;margin-right: 5px;">
              <span v-if="goodsOrderBy=='1'" class="appendImg">{{item.sellGoodsAmount}}元</span>
              <span v-if="goodsOrderBy=='2'" class="appendImg">{{item.sellGoodsCount}}件</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <modelBaseFn v-if="baseFnVisible" ref="baseFnCompon"></modelBaseFn>
  </div>
</template>

<script>
  import {
    indexBaseManager,
    storeSellRaking,
    goodsSellRaking,
    userFunctionList,
    userFunctionAllList
  } from '@/api/api';
  import modelBaseFn from "./homeModules/model-base-fn.vue"

  export default {
    data() {
      return {
        storeRakingLoading: false,
        goodsRakingLoading: false,
        baseFnVisible: false,
        storeOrderBy: 1,
        storeDateType: 2,
        goodsOrderBy: 1,
        goodsDateType: 2,
        options: [{
          value: 2,
          label: '近7天'
        }, {
          value: 3,
          label: '近15天'
        }, {
          value: 4,
          label: '近30天'
        }],
        value: 0,
        // 基础信息
        managerBaseOpction: [{
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "101"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
          {
            "name": '下单金额',
            "subName": "平台所选时间内全部的订单金额",
            price: "100"
          },
        ],
        // 常用功能
        frenFnOpction: [
          // {"id":'1',"name":"发布平台",icon:"el-icon-edit"},
          // {"id":'2',"name":"发布平台",icon:"el-icon-share"},
          // {"id":'3',"name":"发布平台",icon:"el-icon-share"},
        ],
        baseManagerData: [],
        goodsSellData: [],
        storeSellData: [],

      }
    },
    components: {
      modelBaseFn
    },
    created() {},
    mounted() {
      this.getUserFunctionList();
      this.getBaseData(0);
      this.getStoreSellRanking(0);
      this.getGoodsSellRanking(0);
    },
    methods: {

      computeGoodsWidth(num, key) {
        var maxL = -1;
        this.goodsSellData.data.forEach((item, index) => {
          if (parseFloat(item[key]) > maxL) {
            maxL = parseFloat(item[key])
          }
        })
        return (parseFloat(num) / maxL) * 355 + "px";
      },
      computeStoreWidth(num, key) {
        var maxL = -1;
        this.storeSellData.data.forEach((item, index) => {
          if (parseFloat(item[key]) > maxL) {
            maxL = parseFloat(item[key])
          }
        })
        return (parseFloat(num) / maxL) * 355 + "px";
      },
      // computeGoodsWidth(num,key){
      //   var maxL = -1;
      //   this.goodsSellData.data.forEach((item,index)=>{
      //         if(item[key]>maxL){
      //            maxL = item[key]
      //         }
      //   })
      //   return (num/maxL) *355+"px";
      // },
      // computeStoreWidth(num,key){
      //   var maxL = -1;
      //   this.storeSellData.data.forEach((item,index)=>{
      //         if(item[key]>maxL){
      //            maxL = item[key]
      //         }
      //   })
      //   return (num/maxL) *355+"px";
      // },
      //基础管理数据
      getBaseData(refresh) {
        var obj = {
          params: {
            "dateType": this.value,
            "date": this.baseManagerData.updateDate,
            "refresh": refresh
          }
        }
        indexBaseManager(obj).then((res) => {
          if (res.code != 200) {
            return this.$message.error(res.msg)
          } else {
            this.baseManagerData = res.data;
          }
        })
      },
      //店铺销量排名
      getStoreSellRanking(refresh) {
        var obj = {
          params: {
            "orderBy": this.storeOrderBy,
            "dateType": this.storeDateType,
            "date": this.storeSellData.date,
            "refresh": refresh
          }
        }
        this.storeRakingLoading = true;
        storeSellRaking(obj).then((res) => {

          if (res.code != 200) {
            return this.$message.error(res.msg)
          } else {
            this.storeSellData = res.data;
          }
          setTimeout(() => {
            this.storeRakingLoading = false;
          }, 300)
        })
      },
      //商品销量排名
      getGoodsSellRanking(refresh) {
        var obj = {
          params: {
            "orderBy": this.goodsOrderBy,
            "dateType": this.goodsDateType,
            "date": this.goodsSellData.date,
            "refresh": refresh
          }
        }
        this.goodsRakingLoading = true;
        goodsSellRaking(obj).then((res) => {
          if (res.code != 200) {
            return this.$message.error(res.msg)
          } else {
            this.goodsSellData = res.data;
          }
          setTimeout(() => {
            this.goodsRakingLoading = false;
          }, 300)
        })
      },
      getUserFunctionList() {
        userFunctionList().then((res) => {
          if (res.code != 200) {
            return this.$message.error(res.msg)
          } else {
            this.frenFnOpction = res.data;
          }
        })
      },
      editUserFunction() {
        this.baseFnVisible = true;
        this.$nextTick(() => {
          this.$refs.baseFnCompon.init(this.frenFnOpction);
        })
        // var hehe = window.SITE_CONFIG['menuList']
        // console.log(hehe)
        // userFunctionAllList().then((res)=>{
        //    if(res.code!=200){
        //       return this.$message.error(res.msg)
        //   }else{
        //       this.goodsSellData = res.data;
        //   }
        // })
      },
      getRoute(menuUrl) {
        //mggoods/goods/price
        console.log(menuUrl.replace(/\//g, '-'))

        this.$router.push({
          name: menuUrl.replace(/\//g, '-')
        })
      },
      goGoodsStatistic() {
        this.$router.push({
          name: 'statistic-product-index'
        })
      },
      goOrderStatistic() {
        this.$router.push({
          name: 'statistic-transaction-index'
        })
      },

    }
  }
</script>
<style lang="scss" scoped>
  .homePageWarp {
    width: 100%;

    // .el-tabs__content{
    //   background-color: #ECEDF1 !important;;
    // }
    // background: #ECEDF1 !important;;
    .baseMangerClass {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .titleClassStyle {
      color: rgba(34, 96, 210, 1);
    }

    .frenFnclass {
      cursor: pointer;
      display: flex;
      flex-direction: column;
      align-items: center;
      // margin-right: 90px;
      justify-content: center;
      width: 12%;
      i {
        font-size: 40px;
        color: white;
        background: #5d66de;
        //  width: 66px;
        //  height: 66px;;
      }

      span {
        white-space:nowrap;
        margin-top: 10px;
        font-size: 18px;
      }
    }

    .box-card {
      width: 22%;
      height: auto;
      margin-right: 1%;
      margin-left: 1%;
      margin-bottom: 20px;
      padding-bottom: 20px;
      margin-top: 15px;
      border-radius: 10px;
      color: rgba(255, 255, 255, 1);

      .text {
        margin-left: 10%;
      }
    }

    .card_content {
      font-size: 32px;
      font-weight: 600;
      word-wrap: break-word;
      // height: 40px;
    }

    .baseMangerClassText {
      font-size: 16px;
      font-weight: bold;
    }

    .baseHintText {
      max-width: 226px;
      min-width: 100px;
      height: 12px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      line-height: 14px;
    }

    .greyBgcolor {
      background: #F4F6F8;
      color: #666666;
    }

    .whiteBgcolor {
      background: white;
      color: #666666;

    }

    .el-progress-bar__outer {
      background-color: transparent;
      border-radius: 0;
      overflow: initial;
    }

    .el-progress-bar__inner {
      border-radius: 0;
      background-color: rgba(255, 204, 102, 0.47843137254902);
      position: relative;
    }

    .el-progress-bar__innerText {
      position: absolute;
      right: -106px;
      // background-image: url('../../assets/images/progressAfterImg.png');
      width: 96px;
      height: 32px;
      display: flex;

      // align-items: center;
      justify-content: center;
      // padding-top: 6px;

    }

    .progress {
      height: 32px;
      display: inline-block;
      width: 200px;
      background: rgba(93, 150, 254, 0.4);
      border-radius: 20px;
    }

    .appendImg {
      // background-image: url('../../assets/images/progressAfterImg.png');
      min-width: 96px;
      height: 32px;
      display: flex;
      // align-items: center;
      justify-content: center;
      padding-top: 4px;
      margin-top: 8px;
    }
  }
</style>