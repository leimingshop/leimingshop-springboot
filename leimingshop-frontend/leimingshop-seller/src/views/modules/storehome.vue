<template>
 
    <div>

      <div shadow="never" class="aui-card--fill"  style="float:left;width:70%">
        <el-card shadow="never" class="aui-card--fill">
          <div class="storeNameClass">你好,{{this.dataForm.storeDTO.storeName}}!</div>
            <div style="display: flex; justify-content: space-between;padding-bottom:20px;border-bottom: 1px solid rgb(233, 229, 229);"></div>
            <div style="float:left;width:140px;margin-top: 20px;">
              <div style="width:100px">
              
                <img :src="dataForm.storeDTO.storeLogo | filterImgUrl" alt="" style="width:100px;height:100px;border-radius: 6px;">
              </div>
              <br/>
            </div>

              <div>
                <div style="display: flex;">
                  <div style="margin-top: 40px;width: 312px;display: flex;">
                      <span class="spacnClass ">店铺名称：</span>
                      <span class="storeNameClass2" style="width: 235px;">{{this.dataForm.storeDTO.storeName}}</span>
                  </div>
                  <div style="margin-top: 40px;width: 254px;">
                      <span  class="spacnClass">管理账号：</span>
                      <span class="storeNameClass2">{{this.dataForm.storeDTO.account}}</span>
                  </div>
                </div>

                <div :style="storeNameLength()?'margin-top: -4px;display: flex;':'margin-top: 17px;display: flex;'">
                  <div style="margin-top: 10px;width: 312px;">
                    <span  class="spacnClass">店铺等级：</span>
                    <span class="storeNameClass2">{{this.dataForm.storeDTO.gradeName}}</span>
                  </div>

                  <div style="margin-top: 10px;width: 254px;">
                      <span  class="spacnClass">管理权限：</span>
                      <span v-if="this.dataForm.storeDTO.roleMark==1" class="storeNameClass2">店长</span>
                      <span v-else class="storeNameClass2">{{this.dataForm.storeDTO.roleName}}</span>
                  </div>
                </div>
              </div>  
        </el-card>
        <el-card v-if="$hasPermission('sys:index:goods')"  shadow="never" class="aui-card--fill"  style="float:left;width:50%;">
            <div style="display: flex;flex-direction: column;">  
              <span style="font-size:18px;color:#333;font-weight: bold;"><span class="titleClassStyle" style="margin-left: 10px;">|&nbsp;</span>商品实况</span>
              <span>&#12288;</span>
              <div style="display: flex;">
                  <div style="margin-left: 24px;">
                  <span style="color:#999999"> 数据更新时间：{{this.goodsLiveDTO.createDate}}</span>
                </div>
                <div style="margin-left: auto;">
                  <i class="el-icon-refresh iconClass"   @click="storegoodsLive(1)"></i>
                </div>
              </div>
            </div>
            <div style="margin-top: 5px; display: flex;">
              <div class="goodsInfoClass" >
                <span>{{this.goodsLiveDTO.goodsCount}}</span>
                    
                <label>出售中商品</label>
            </div>

            <div  class="goodsInfoClass" >
                <span style="color:#DE7910">{{this.goodsLiveDTO.auditGoodsCount}}</span>
                      
                <label>待审核商品</label>
            </div>

            <div  class="goodsInfoClass">
                <span style=" color:#E52424">{{this.goodsLiveDTO.refusedAuditGoodsCount}}</span>
                      
                <label>被拒绝商品</label>
            </div>

              <div  class="goodsInfoClass" >
                <span style="color:#747474">{{this.goodsLiveDTO.sellOutGoods}}</span>
                      
                <label>售罄商品</label>
              
              </div>
            </div>
                  
        </el-card>
        <el-card v-if="$hasPermission('sys:index:order')" shadow="never" class="aui-card--fill"  style="float:left;width:50%;margin-top: 15px;">
            <div style="display: flex;flex-direction: column;">
                <span style="font-size:18px;color:#333;font-weight: bold;"><span class="titleClassStyle" style="margin-left: 10px;">|&nbsp;</span>订单实况</span>
                 <span>&#12288;</span>
              <div style="display: flex;">
                <div style="margin-left: 24px;">
                  <span style="color:#999999"> 数据更新时间：{{this.orderLiveDTO.createDate}}</span>
                </div>
                <div style="margin-left: auto;">
                  <i class="el-icon-refresh iconClass"   @click="storeorderLive(1)"></i>
                </div>
              </div>
            </div>
          <div style="margin-top: 5px; display: flex;">
            <div  class="goodsInfoClass"  >
                <span>{{this.orderLiveDTO.unpaidCount}}</span>
                <label >待支付订单</label>
            </div>

            <div class="goodsInfoClass" >
                <span style="color:#1BBA6A">{{this.orderLiveDTO.pendingCount}}</span>
                <label >待发货订单</label>
            </div>

            <div class="goodsInfoClass"  >
                <span style=" color:#333333" >{{this.orderLiveDTO.toAuditCount}}</span>
                <label>待审核申请单</label>
            </div>

              <div class="goodsInfoClass"  >
                <span style="#3D4FB7">{{this.orderLiveDTO.orderCount}}</span>
                <label >今日订单数</label>
              </div>
            </div>
        </el-card>
        <!-- <div style="display: flex;float: left; justify-content: space-between;border-bottom: 10px solid rgb(233, 229, 229);width: 100%;"></div> -->
        <el-card shadow="never" class="aui-card--fill" style="float:left;width:100%">
              <div style="background:#FFF;">
                <div class="baseMangerClass" style="margin-top: -23px;">
                   <h3 style="font-size:18px;"><span class="titleClassStyle" style="margin-left: 10px;">|&nbsp;</span>常用功能</h3>
                  <span style="color: blue;padding-right: 30px;cursor: pointer;font-size: 16px;" @click="editUserFunction">自定义</span>
                </div>
                

            <div style="display: flex;">
              <div class="frenFnclass" v-for="(item,index) in this.dataForm.indexDTO" style="color: blue;cursor: pointer;" @click="goRouter(item.menuUrl)">
                  <svg  class="icon-svg" aria-hidden="true"><use :xlink:href="`#${item.icon}`" ></use></svg>
                    <span style="font-size:14px;color:#333333">{{item.menuName}}</span>
              </div>
            </div>
          </div>
        
        </el-card>
        <el-card shadow="never" class="aui-card--fill" style="float:left;width:100%">
           <div style="background:#FFF;">
                <div class="baseMangerClass" style="margin-top: -23px;">
                   <h3 style="font-size:18px;"><span class="titleClassStyle" style="margin-left: 10px;">|&nbsp;</span>营销活动</h3>
                  <span class="btnClass"  @click="activityRouter('mgoperative-coupon-couponList')">更多>></span>
                </div>
            <div  style=" display: flex;margin-left: 20px;margin-top: -17px;">
              <div class="activeFnclass"  style="cursor: pointer;" @click="activityRouter('mgoperative-coupon-couponList')">
                  <img src="@/assets/images/youhuiquan.png" alt="" style="width:60px;height:60px;border-radius: 6px;">
                <div>
                  <span class="activityClass">优惠券</span>
       
                  <span class="spanFint">对不同商品进行提供优惠，提升部分商品销量</span>
                </div>
              </div>

              <div class="activeFnclass"  style="cursor: pointer;" @click="activityRouter()">
                  <img src="@/assets/images/jifen.png" alt="" style="width:60px;height:60px;border-radius: 6px;">
                <div>
                  <span class="activityClass">积分活动</span>
           
                  <span class="spanFint">为增加客户留存率，对客户某些操作进行奖励</span>
                </div>
              </div>

              <div class="activeFnclass"  style="cursor: pointer;" @click="activityRouter('mgoperative-fullReduction-index')">
                  <img src="@/assets/images/manjian.png" alt="" style="width:60px;height:60px;border-radius: 6px;">
                <div>
                  <span class="activityClass">满减活动</span>
               
                  <span class="spanFint">客户变员工，进行多渠道销售商品</span>
                </div>
              </div>
           
              <div>
                
              </div>
            </div>
          </div>
        </el-card>

        
      </div>
       <div shadow="never" class="aui-card--fill"  style="float:right;width:29%">
         
        <el-card shadow="never" class="aui-card--fill" style="float:right;width:100%;height: 201px;">
          <div style="background:#FFF;margin-top: -23px;">
              <h3></h3>
                <div class="baseMangerClass">
                   <h3 style="font-size:17px;"><span class="titleClassStyle">|&nbsp;</span>平台私信</h3>
                  <span class="btnClass" v-if="this.massageList.length>0" @click="indexskip('aa','content-message-index',queryObj1)" >更多>></span>
                </div>
            <div > 
              <div v-for="(item,index) in this.massageList" style="display: flex; justify-content: space-between;padding-bottom:11px;"  >
                  <span style="padding-right: 30px;cursor: pointer;font-size:14px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" @click="indexskip(item.id)">{{item.messageTitle}}</span>
                  
                  <span style="padding-right: 30px;cursor: pointer;font-size:14px;white-space: nowrap;" @click="indexskip(item.id)">{{item.sendTime.substring(5,10)}}</span>
              </div>
            </div>
          </div>
        </el-card>
        <el-card shadow="never" class="aui-card--fill" style="float:right;width:100%;height: 172px;">
          <div style="background:#FFF;margin-top: -23px;">
              <div class="baseMangerClass">
                   <h3 style="font-size:17px;"><span class="titleClassStyle">|&nbsp;</span>公告</h3>
                  <span class="btnClass" v-if="this.sysList.length>0"  @click="indexskip('aa','content-message-index',queryObj2)" >更多>></span>
                </div>
            <div > 
              <div v-for="(item,index) in this.sysList" style="display: flex; justify-content: space-between;padding-bottom:11px;"  >
                  <span style="padding-right: 10px;font-size:14px;cursor: pointer;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" @click="indexskip(item.id)">{{item.messageTitle}}</span>
                  
                  <span style="padding-right: 30px;cursor: pointer;font-size:14px;white-space: nowrap;"  @click="indexskip(item.id)">{{item.sendTime.substring(5,10)}}</span>
              </div>
            </div>
          </div>
        </el-card>

             <el-card shadow="never" class="aui-card--fill" style="float:right;width:100%;height: 161px;">
          <div style="background:#FFF;margin-top: -23px;">
                  <div class="baseMangerClass">
                   <h3 style="font-size:17px;"><span class="titleClassStyle">|&nbsp;</span>常见问题</h3>
                  <span  class="btnClass" v-if="this.massageList.length>0" @click="indexskip('aa','content-message-index',queryObj2)" >更多>></span>
                </div>
            <div > 
              <div v-for="(item,index) in this.massageList" style="display: flex; justify-content: space-between;padding-bottom:11px;"  >
                  <span style="padding-right: 30px;font-size:14px;cursor: pointer;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" @click="indexskip(item.id)">{{item.messageTitle}}</span>
                  
              </div>
            </div>
          </div>
        </el-card>

             <el-card shadow="never" class="aui-card--fill" style="float:right;width:100%;height: 167px;">
          <div style="background:#FFF;margin-top: -23px;">
                  <div class="baseMangerClass">
                   <h3 style="font-size:17px;"><span class="titleClassStyle" >|&nbsp;</span>使用手册</h3>
                  <span class="btnClass" v-if="this.massageList.length>0" @click="indexskip('aa','content-message-index',queryObj2)">更多>></span>
                </div>
            <div > 
              <div v-for="(item,index) in this.massageList" style="display: flex; justify-content: space-between;padding-bottom:11px;"  >
                  <span style="padding-right: 30px;cursor: pointer;font-size:14px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" @click="indexskip(item.id)">{{item.messageTitle}}</span>
                  
              </div>
            </div>
          </div>
        </el-card>
       </div>
         <modelBaseFn v-if="baseFnVisible" ref="baseFnCompon"></modelBaseFn>
    </div>
</template>

<script>
import {storeIndex,storegoodsLive,storeorderLive,messagePage} from "@/api/api.js";
import modelBaseFn from "./homeModules/model-base-fn.vue"
export default {
        data() {
            return {
              queryObj1:[{key:'messageType',value:'0'},{key:'activeName2',value:'0'}],
              queryObj2:[{key:'messageType',value:'1'},{key:'activeName2',value:'1'}],
              baseFnVisible:false,
              dataForm: {
                   storeDTO:"",
                   activityMenu:"",
                   indexDTO:"",
                  
                },
              goodsLiveDTO:{

              },
              orderLiveDTO:{

              },
              massageList:{

              },
              sysList:{

              },
            };
        },
        mounted() {
             // 数据回显
            this.storeIndex();
            this.storegoodsLive(0)
            this.storeorderLive(0)
            this.messagePage(0);
            this.messagePage(1);
        },
        components: {
           modelBaseFn
        },
    
        created() {
        
        },
        methods: {
           storeIndex(){
            storeIndex().then((res)=>{
                console.log(res);
                if(res.code==200){
                    this.dataForm=res.data
                }
            })
        },
        // 判断是否有站内信跳转权限
        indexskip(id,url,query){
          for(let i=0;i<=window.SITE_CONFIG['menuList'].length-1;i++){
           if(window.SITE_CONFIG['menuList'][i].name=="内容管理"){
            for(let j =0;j<=window.SITE_CONFIG['menuList'][i].children.length-1;j++){
              if(window.SITE_CONFIG['menuList'][i].children[j].url=="content/message/index"){
                if(id!="aa"){
                    this.goRouter('content-message-index',[{key:'index',value:'9'},{key:'id',value:id}])
                     return;
                }else{
                    this.goRouter('content-message-index',query)
                     return;
                }
              }
            }
           }
          }
          this.$message.error("暂无权限,请联系店长")
        },
        // 判断是否有活动权限
        

        editUserFunction(){
              this.baseFnVisible = true;
              this.$nextTick(()=>{
                  this.$refs.baseFnCompon.init(this.dataForm.indexDTO);
              })
          },
          storeNameLength(){
            if(this.dataForm.storeDTO.storeName&&this.dataForm.storeDTO.storeName.length>24){
             return true
            }else{
              return false
            }
          },
        storegoodsLive(type){
           var params={
              type:type
          }
       
          storegoodsLive(params).then((res)=>{
                if(res.code==200){
                    this.goodsLiveDTO=res.data
                }
              })
          },
          messagePage(type){
            var obj={
              params:{
                page:1,
                limit:4,
                messageType: type
              }
            }
              
            messagePage(obj).then((res)=>{
              if(res.code==200){
                if(type==0){
                  this.massageList=res.data.list
                }else{
                  this.sysList=res.data.list
                }
                
              }
           
            })
          
        },
         //活动路由跳转
         activityRouter(url){
           if(!url){
             this.$message.success("敬请期待")
           }
          let reg=new RegExp('-','g')//g代表全部
           var menuUrl=url.replace(reg,'/');
            for(let i=0;i<=window.SITE_CONFIG['menuList'].length-1;i++){
              if(window.SITE_CONFIG['menuList'][i].name=="营销管理"){
                 for(let j =0;j<=window.SITE_CONFIG['menuList'][i].children.length-1;j++){
                    if(window.SITE_CONFIG['menuList'][i].children[j].url==menuUrl){
                        this.goRouter(url);
                        return
                    }
                }
              }
            }
          this.$message.error("暂无权限,请联系店长")
         },
    // 跳转路由
    goRouter(argu,queryObj){
      console.log(queryObj);
      if(!queryObj){
        this.$router.push({
          path: argu
        })
      }else{
        var obj = {}
        queryObj.forEach((item,index)=>{
            obj[item.key] = item.value
        })
        console.log(obj);
        this.$router.push({
          path: argu,
          query:obj
        })
      }

    },
        storeorderLive(type){
           var params={
              type:type
          }
       
          storeorderLive(params).then((res)=>{
                if(res.code==200){
                    this.orderLiveDTO=res.data
                }
              })
      }
  }
}
</script>
<style lang="scss" scoped>
.storeNameClass{
  height:22px;
  font-size:20px;
  font-family:PingFangSC-Semibold,PingFang SC;
  font-weight:600;
  color:rgba(51,51,51,1);
  line-height:22px;
}
.goodsInfoClass{
  display: flex;
  flex-direction: column;
  font-size:13px;
  color:#333;
  font-weight: bold;
  text-align-last: center;
  width:33%;
  span{
      font-size:28px;
      color:#3D4FB7;
      font-weight: bold;
      margin-top: 20px;
  }
  label{
    font-size:13px;
    color:#333;
    font-weight: bold;
    margin-top: 3px;

  }
}
 .proofNews{
            /deep/ .el-form-item__label{
                width: 180px !important;
            }
}

.goodsImg {
    width: 100px;
    height: 100px;

    img {
        width: 90%;
        height: 90%;
    }
}
.frenFnclass{
    display: flex;
    flex-direction: column;
    align-items: center;    margin: auto;
    justify-content: center;
    float: left;
    text-align: center;
      svg{
        align-items: center;
          font-size: 60px;
          color:white;
          background:#666666;
          border-radius: 6px;
      }
      span{
        margin-top:10px;
        text-overflow:ellipsis;
        white-space:nowrap;
      }
}
.activeFnclass{
        height: 110px;
        width: 330px;
        display: flex;
        align-items: center;
        
    svg{
      font-size: 48px;
      color:white;
      background:#666666;;
      border-radius: 6px;
    //  width: 66px;
    //  height: 66px;;
    }
    span{
        margin-top: 10px;
        width: 20px;
      margin: 10px;
        word-break:normal; 
        width:150px;display:block;
        white-space:pre-wrap;
        word-wrap : break-word ;
        overflow: hidden ;
    }
}
  .baseMangerClass{
      display:flex;
      justify-content: space-between;
      align-items: center;
      .baseMangerItem{

      }
    }
    .titleClassStyle{
      color: rgba(34,96,210,1); 
      -webkit-text-stroke: medium;
    }
.towEllipsis {
    text-align: left;
    text-overflow: -o-ellipsis-lastline;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .spanFint{
    width:182px;
    height:32px;
    font-size:11px;
    font-family:PingFangSC-Regular,PingFang SC;
    font-weight:400;
    color:rgba(102,102,102,1);
    line-height:14px;
  }
  .iconClass{
    cursor:pointer;
    color: rgb(174, 169, 169);
    margin-top: -10px;
    background: rgba(236,237,241,1);
    margin-left: 103px;
    font-size: 21px;
    line-height: 28px;
    text-align: center;
    vertical-align: text-bottom;
    width: 28px;height: 28px;
    border-radius: 50px;
  }
  .btnClass{
    cursor: pointer;
    width:70px;
    height:20px;
    font-size:15px;
    font-family:PingFangSC-Regular,PingFang SC;
    font-weight:400;
    color:rgba(51,51,51,1);
    line-height:20px;
  }
  .spacnClass{
    width: 88px;
    font-size:17px;
    color:rgb(145, 137, 137);
  }
  .storeNameClass2{
    color:#333333;
    // height:17px;
    font-size:18px;
    font-family:PingFangSC-Regular,PingFang SC;
    font-weight:400;
    line-height:18px;
  }
  .activityClass{
    font-size:15px;
    line-height: 30px;
    margin: 10px;
    font-weight: bold;
    margin-bottom: -6px;
  }
</style>