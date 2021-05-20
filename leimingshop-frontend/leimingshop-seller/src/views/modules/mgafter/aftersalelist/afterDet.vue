<template>
  <div class="aftersalelist_afterDetClass">
    <div  id="afterDetGoodsDetail" v-if="isSaleList">
      <Bread :breaddata="detdata" @changePage="changePage" :index="index?index:'1'"></Bread>
      <!-- 分割线------------------------------------------------ -->
      <div class="orderState">
        <div>
          <label>当前{{isSelect==0?'退':'换'}}货审核状态：</label>
          <!-- <span style="color:#01BD25;">{{aftersale.auditStatus==1?'商家审核中':aftersale.auditStatus==2?'平台审核中':aftersale.auditStatus==3?'已完成':aftersale.auditStatus==4?'已取消':'未审核'}}</span> -->
            <span v-if="aftersale.auditStatus != 3" style="color:#e6a23c;">{{aftersale.auditStatus==1?'商家审核中':aftersale.auditStatus==2?'平台审核中':aftersale.auditStatus==4?'已取消':'未审核'}}</span>
            <span v-if="aftersale.auditStatus == 3">
              <span v-if="aftersale.auditResult ==2" style="color:#f56c6c">
                {{"审核拒绝"}}
              </span>
              <span v-if="aftersale.auditResult ==1" style="color:#01BD25;">
                {{"审核通过"}}
              </span>
            </span>
        </div>
        <div>
          <label>{{isSelect==0?'退':'换'}}货单号：</label>
          <span >{{aftersale.aftersaleSn}}</span>
        </div>
        <div>
          <label>订单编号：</label>
          <span>{{aftersale.orderSn}}</span>
        </div>
      </div>
      <!-- 分割线------------------------------------------------ -->
      <div class="opctionWarp">
          <div class="opctionItem3">
            <h3>买家{{isSelect==0?'退':'换'}}货申请</h3>
            <el-form
                    class="saleOrderInfo"
            >
              <div class="formWarp formWarp1">
                <el-form-item label="买家：" >
                  <span>{{aftersale.contacts}}</span>
                </el-form-item>
                <el-form-item label="电话：">
                  <span>{{aftersale.contactsPhone}}</span>
                </el-form-item>

                <el-form-item label="申请时间：" >
                  <span>{{aftersale.createDate}}</span>
                </el-form-item>

                <el-form-item :label="isSelect==0?'退货原因：':'换货原因：'" >
                  <span>{{aftersale.aftersaleReason}}</span>
                </el-form-item>

                <el-form-item :label="isSelect==0?'退货说明：':'换货说明：'" >
                  <span>{{aftersale.aftersaleExplain}}</span>
                </el-form-item>

                <el-form-item :label="isSelect==0?'退货凭证：':'换货凭证：'"  class="pingzheng" style="display: flex; justify-content: flex-start;white-space: nowrap;">
                  <div v-for="(item,index) in piclist " style="margin-left:1px">
                    <img id="oImg" :src="item | filterImgUrl" alt="" style="height:60px;width:60px;object-fit: scale-down;" @click="previewBigImg(item,index)" >
                  </div>
                </el-form-item>
              </div>
            </el-form>
          </div>
          <!-- 分割线------------------------------------------------ -->
          <div class="opctionItem3" v-if="isSellerFin">
              <h3>商家{{isSelect==0?'退':'换'}}货审核</h3>
              <el-form
                      class="saleOrderInfo"
                      style="margin-bottom:40px;"

              >
                <div class="formWarp formWarp1">
                  <el-form-item label="商家名称：" >
                    <span>{{aftersale.storeName}}</span>
                  </el-form-item>
                  <el-form-item label="审核时间：">
                    <span>{{saleAuditLog.updateDate}}</span>
                  </el-form-item>

                  <el-form-item label="审核理由：" >
                    <span>{{saleAuditLog.reason}}</span>
                  </el-form-item>

                  <el-form-item label="审核结果：" >
                    <span>{{saleAuditLog.result==1?'同意':saleAuditLog.result==2?'拒绝':'审核中'}}</span>
                  </el-form-item>
                </div>
              </el-form>
           </div>
          <!-- 分割线------------------------------------------------ -->
          <div class="opctionItem3"  v-if="adminAuditLog!=''">
            <h3>平台{{isSelect==0?'退':'换'}}货审核</h3>
            <el-form
                    class="saleOrderInfo"
            >
              <div class="formWarp formWarp1">
                <el-form-item label="审核时间：">
                  <span>{{adminAuditLog.updateDate}}</span>
                </el-form-item>
                <el-form-item label="审核理由：">
                  <span>{{adminAuditLog.reason}}</span>
                </el-form-item>
                <el-form-item label="审核结果：" >
                  <span>{{adminAuditLog.result==1?'同意':adminAuditLog.result==2?'拒绝':'审核中'}}</span>
                </el-form-item>
              </div>
            </el-form>
          </div>
          <div class="opctionItem3" v-if="isLog && isSelect==0">
              <h3 >退货物流</h3>
              <el-form
                      class="saleOrderInfo"
              >
                <div class="formWarp formWarp1">
                  <el-form-item label="退货时间：" >
                    <span>{{infor.createDate}}</span>
                  </el-form-item>
                  <el-form-item label="退货物流：">
                    <span>{{infor.logisticsCompany}}</span>
                  </el-form-item>
                  <el-form-item label="退货单号：" >
                    <span>{{infor.logisticsNumber}}</span>
                  </el-form-item>
                </div>
              </el-form>
          </div>

          <div class="opctionItem3"  v-if="isLog && isSelect==1">
            <h3>换货物流</h3>
            <el-form
                    class="saleOrderInfo"
            >
              <div class="formWarp formWarp1">
                <el-form-item label="买家换货时间：" >
                  <span>{{infor.buyerDeliveryTime}}</span>
                </el-form-item>
                <el-form-item label="买家发货物流：">
                  <span>{{infor.buyerLogisticsCompany}}</span>
                </el-form-item>
                <el-form-item label="买家发货单号：" >
                  <span>{{infor.buyerLogisticsNumber}}</span>
                </el-form-item>
                <el-form-item label="卖家换货时间：" >
                  <span>{{infor.sellerDeliveryTime}}</span>
                </el-form-item>
                <el-form-item label="卖家发货物流：">
                  <span>{{infor.sellerLogisticsCompany}}</span>
                </el-form-item>
                <el-form-item label="卖家发货单号：" >
                  <span>{{infor.sellerLogisticsNumber}}</span>
                </el-form-item>
              </div>
            </el-form>
          </div>
      </div>
      <!-- 分割线------------------------------------------------ -->
      <div class="returnGoods">
          <div class="returnGoodsTop">
              <h3 style="font-size:18px;">{{isSelect==0?'退':'换'}}货商品</h3>
              <el-button class="goOrderDetail" @click="jumpOrderDetail">前往查看订单详情</el-button >
          </div>
          <el-table :data="saleGoods" border="" style="width: 100%">
            <el-table-column prop="goodsName" label="商品名称" width="180" align="center">
              <template slot-scope="scope">
              <div  style="display: flex;cursor: pointer;"  @click="previewH5Fn(scope.row)" >
                <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px"/>
                <div class="towEllipsis"  style="text-align:center;cursor: pointer;">
                      <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                        <span style="color: #4e80db;text-decoration: none; cursor: pointer;" @click="previewH5Fn(scope.row)" >
                            {{scope.row.goodsName}}
                        </span>
                      </el-tooltip>
                </div>
                 </div>
              </template>
            </el-table-column>
            <el-table-column prop="specSerial" label="商品货号" width="180" align="center"></el-table-column>
            <el-table-column prop="specAttrName" label="规格" align="center"></el-table-column>
            <el-table-column prop="specSellPrice" label="单价" align="center">
              <template
                      slot-scope="scope"
                      v-if="scope.row.specSellPrice!==''&&scope.row.specSellPrice!==null"
              >￥{{scope.row.specSellPrice}}</template>
            </el-table-column>
            <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
            <el-table-column prop="specPayPrice" label="总价" align="center">
              <template
                      slot-scope="scope"
                      v-if="scope.row.specPayPrice!==''&&scope.row.specPayPrice!==null"
              >￥{{scope.row.specPayPrice}}</template>
            </el-table-column>
          </el-table>
      </div>
      <!-- 分割线------------------------------------------------ -->
      <div class="operationLog">
          <h3 style="font-size:18px;">操作日志</h3>
          <el-table
                  width="100%"
                  :data="saleLogs"
                  border=""
                  v-loading=""
                  style="width: 100%;maigin-top:20px;margin-bottom:40px;"
          >
            <el-table-column prop="creator" label="操作者" align="center"></el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
              <template slot-scope="scope">
                <span>{{scope.row.message}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="操作时间" align="center"></el-table-column>
          </el-table>
      </div>
      <!--返回按钮-->
      <div style="margin-top: 40px;margin-bottom: 40px">
        <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>
      </div>

      <!-- 分割线------------------------------------------------ -->
      <!-- <h3 style="font-size:18px;" v-if="isBtn">商家退换货处理</h3>
      <el-form ref="form" :model="form" label-width="80px" :rules="reaRule" v-if="isBtn">
        <el-form-item label="审核理由：" prop="reason">
          <el-input
                  type="textarea"
                  v-model="form.reason"
                  style="resize:none;"
                  :autosize="{ minRows: 4, maxRows: 6}"
                  placeholder="请输入内容"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain @click="agreeGoods(0)">取消</el-button>
          <el-button type="danger" plain @click="agreeGoods(2)">拒绝</el-button>
          <el-button type="primary" @click="agreeGoods(1)">同意</el-button>
        </el-form-item>
      </el-form> -->
      <!-- 分割线------------------------------------------------ -->
    </div>
    <div  v-else>
      <orderDet
              ref="orderDetailCompon"
              @changePage="changePage"
              :index="'2'"
              :orderDetBreaddata="orderDetBreaddata"
      ></orderDet>
    </div>
        <!-- 预览h5 -->
  <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
  </div>
</template>
<script>
import Bread from "@/components/bread";
import orderDet from "../../order/list/orderDet"
import modelPreviewH5 from '../../mggoods/modules/model-preview-h5.vue'
import { examineGoods } from "@/api/api";
export default {
  data() {
    return {
      previewH5Visible:false,
      form: { reason: "" }, //审核理由
      isSaleList : true,
      orderDetBreaddata: [ "售后管理", "申请记录", "售后详情","订单详情"],
      reaRule: {
        reason: [{ required: true, message: "请输入审核理由", trigger: "blur" }]
      }
    };
  },
  components: {
    Bread,
    orderDet,
    modelPreviewH5
  },
  props: [
    "detdata",
    "aftersale",
    "isBtn",
    "isSellerFin",
    "isAdminFin",
    "isLog",
    "index",
    "saleLogs",
    "saleAuditLog",//商家审核
    "adminAuditLog",//平台审核
    "infor",//退换货物流
    "data",
    "isSelect",//退换货类型  0-退1-换
    "saleGoods",
    "piclist", //凭证照片
    "row",//查看订单的那一行数据
  ],
  methods: {
    //跳到订单详情页面
    jumpOrderDetail(){
   /*   alert(this.row.id)*/
      this.isSaleList = false
      this.$nextTick(()=>{
        this.$refs.orderDetailCompon.init(this.row);
      })
    },
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
           // 预览h5
    previewH5Fn(row){
         window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
    },
    //返回上一级
    changePage() {
      if(this.index){
         this.$emit("changePage");
         return
      }
      if(this.isSaleList == false){
        this.isSaleList = true
      }else{
        this.$emit("changeState");
      }
    },
    //审核退换货
    agreeGoods(type) {
      //0 取消 1 同意 2 拒绝
      if (type == 0) {
        this.changePage();
        return;
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          const obj = {
            id: this.saleAuditLog.id,
            aftersaleSn: this.aftersale.aftersaleSn,
            aftersaleType: this.aftersale.aftersaleType,
            process: 1, //1:商家审核,2:平台审核
            reason: this.form.reason,
            result: type
          };
          examineGoods(obj).then(res => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: res.msg
              });
              this.changePage();
            } else {
              this.$message({
                type: "warning",
                message: res.msg
              });
            }
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.aftersalelist_afterDetClass{
    .creater {
      display: inline-block;
      width: 80px;
      /* margin: 0 15px; */
    }
    .orderState {
        /* width: 100%; */
        border: 1px solid #d1d1d1;
        height:86px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background:#D5DFF7;
        font-size:18px;
        font-weight:600;
        color:rgba(0,0,0,1);
        padding-left:2%;
        padding-right:9%;
        label{
            width:269px;
            height:23px;
        }
    }
    .opctionWarp{
        // padding-left:5%;
        // padding-right:5%;
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid #eeeeee;
        background-color: #fcfbfe;
        // height: 295px;
        font-size:16px;
        h3{
            font-size:18px;
        }
        .opctionItem3{
            h3{
              padding-left: 30px;
            }
            width:25%;
            border-right: 1px solid #eeeeee;
            margin-top:16px;
            margin-bottom:16px;
            // padding-left: 25px;
        }
    }
    .buyerInfo,
    .sellerInfo,
    .goodsLog,
    .goods,
    .operationList {
      border: 1px solid #e1e1e1;
      margin-top: 10px;
      padding: 10px 10px 20px 10px;
    }
    .inforTit {
      width: 100px;
      font-weight: 600;
      text-align: right;
      display: inline-block;
    }
    .inforRight {
      margin-left: 40px;
      display: inline-block;
    }
    .imglist {
      width: 100px;
      height: 100px;
    }
    .right {
      margin-right: 15px;
    }
    .buyerInfo span,
    .sellerInfo span,
    .goodsLog span {
      margin-top: 20px;
    }
    .el-textarea {
      width: 30%;
    }
    .bottomBtns{
      text-align: center;
      position: fixed;
      bottom: 20px;
      width: 216px;
      left: 50%;
      z-index: 22;
      background-color: white;
    }


    #afterDetGoodsDetail{
      /deep/ .el-form-item {
        margin-top: 0px !important;
        display: flex;
        .el-form-item__label{
          width: 120px!important;
          min-width: 120px !important;
        }
        .el-form-item__content{
              word-wrap: break-word;
              margin-left: 0 !important;
              width: 70%;
        }
      }
    }
    .returnGoods{
        margin-top:40px;
        .returnGoodsTop{
            font-size:18px;
            font-weight:600;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .goOrderDetail{
                display: flex;
                align-items: center;
                padding: 0;
                justify-content: center;
                font-size:14px;
                background:#DDE7F4;
                color:#395FB7;
                width:131px;
                height:24px;
            }
        }
    }
    .operationLog{
        margin-top:50px;
        font-size:18px;
        font-weight:600;
    }
  //  /deep/ th {
  //       color:#6185CD !important;
  //       background-color:#D2E0F7 !important;
  //   }
    .pingzheng /deep/.el-form-item__content{
      margin-left: 10px !important;
      width: 1px;
      display: flex;
    }
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

</style>
