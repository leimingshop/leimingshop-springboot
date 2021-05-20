<template>
    <div class="aftersalelog_afterDetClass">
        <div v-if="isSaleList">
            <Bread :breaddata="detdata" @changePage="changePage" :index="'2'"></Bread>
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
                    <span>{{aftersale.aftersaleSn}}</span>
                </div>
                <div>
                    <label>订单编号：</label>
                    <span>{{aftersale.orderSn}}</span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->
            <div class="opctionWarp">
                <div class="opctionItem1">
                    <h3>买家{{isSelect==0?'退':'换'}}货申请</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="买家：">
                                <span>{{aftersale.contacts}}</span>
                            </el-form-item>
                            <el-form-item label="电话：">
                                <span>{{aftersale.contactsPhone}}</span>
                            </el-form-item>

                            <el-form-item label="申请时间：">
                                <span>{{aftersale.createDate}}</span>
                            </el-form-item>

                            <el-form-item :label="isSelect==0?'退货原因：':'换货原因：'">
                                <span>{{aftersale.aftersaleReason}}</span>
                            </el-form-item>

                            <el-form-item :label="isSelect==0?'退货说明：':'换货说明：'">
                                <span>{{aftersale.aftersaleExplain}}</span>
                            </el-form-item>

                            <el-form-item :label="isSelect==0?'退货凭证：':'换货凭证：'" class="pingzheng"
                                        style="display: flex; justify-content: flex-start;">
                                <div v-for="(item,index) in piclist " style="margin-left: 1px;">
                                    <img id="oImg" :src="item | filterImgUrl" alt="" style="height:70px;width:70px;object-fit: scale-down;" @click="previewBigImg(item,index)">
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>

                <!-- 分割线------------------------------------------------ -->
                <div class="opctionItem1" v-if="isSellerFin">
                    <h3 >商家{{isSelect==0?'退':'换'}}货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="商家名称：">
                                <span>{{aftersale.storeName}}</span>
                            </el-form-item>
                            <el-form-item label="审核时间：">
                                <span>{{saleAuditLog.updateDate}}</span>
                            </el-form-item>

                            <el-form-item label="审核理由：">
                                <span>{{saleAuditLog.reason}}</span>
                            </el-form-item>

                            <el-form-item label="审核结果：">
                                <span>{{saleAuditLog.result==1?'同意':saleAuditLog.result==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <!-- 分割线------------------------------------------------ -->
                <div class="opctionItem1" v-if="isAdminFin && saleAuditLog.result==1">
                    <h3 >平台{{isSelect==0?'退':'换'}}货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="审核时间：">
                                <span>{{adminAuditLog.updateDate}}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{adminAuditLog.reason}}</span>
                            </el-form-item>
                            <el-form-item label="审核结果：">
                                <span>{{adminAuditLog.result==1?'同意':adminAuditLog.result==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->

            <div class="orderState">
                <div>
                    <label>当前仲裁审核状态：</label>
                    <!-- <span style="color:#01BD25;">{{data.arbitrationStatus==3?'审核中':'已完成'}}</span> -->
                    <span v-if="data.arbitrationStatus == 3" style="color:#e6a23c;">
                        {{"审核中"}}
                    </span>
                    <span v-if="data.arbitrationStatus == 2" style="color:#f56c6c">
                        {{"审核拒绝"}}
                    </span>
                    <span v-if="data.arbitrationStatus == 1" style="color:#01BD25;">
                        {{"审核通过"}}
                    </span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->

            <div class="opctionWarp">
                <div class="opctionItem1" >
                    <h3 >仲裁{{isSelect==0?'退':'换'}}货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="会员账号：">
                                <span>{{data.memberName}}</span>
                            </el-form-item>
                            <el-form-item label="联系人：">
                                <span>{{data.contacts}}</span>
                            </el-form-item>
                            <el-form-item label="手机号码：">
                                <span>{{data.contactsWay}}</span>
                            </el-form-item>
                            <el-form-item label="申请仲裁时间：">
                                <span>{{data.arbitrationApplyDate}}</span>
                            </el-form-item>
                            <el-form-item label="申请内容：">
                                <span>{{data.detailedDescription}}</span>
                            </el-form-item>
                            <el-form-item label="申请图片：" class="pingzheng" style="display: flex; justify-content: flex-start;">
                                <div v-for="(item,index) in imagesList " style="margin-left: 1px;">
                                    <img id="oImg" :src="item | filterImgUrl" alt="" style="height:70px;width:70px;object-fit: scale-down;"  @click="previewBigImg(item,index)">
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <div class="opctionItem1" >
                    <h3 >仲裁{{isSelect==0?'退':'换'}}货结果</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="仲裁结果：">
                                <span>{{data.arbitrationStatus==1?'同意':data.arbitrationStatus==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{data.auditReason}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <div class="opctionItem1" v-if="isLog && isSelect==0" >
                    <h3>退货物流</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="退货时间：">
                                <span v-if="barinfor.logisticsCompany">{{barinfor.createDate}}</span>
                            </el-form-item>
                            <el-form-item label="退货物流：">
                                <span>{{barinfor.logisticsCompany}}</span>
                            </el-form-item>
                            <el-form-item label="退货单号：">
                                <span>{{barinfor.logisticsNumber}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <div class="opctionItem1" v-if="isLog && isSelect==1" >
                    <h3 >换货物流</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="买家换货时间：">
                                <span>{{barinfor.buyerDeliveryTime}}</span>
                            </el-form-item>
                            <el-form-item label="买家发货物流：">
                                <span>{{barinfor.buyerLogisticsCompany}}</span>
                            </el-form-item>
                            <el-form-item label="买家发货单号：">
                                <span>{{barinfor.buyerLogisticsNumber}}</span>
                            </el-form-item>
                            <el-form-item label="卖家换货时间：">
                                <span>{{barinfor.sellerDeliveryTime}}</span>
                            </el-form-item>
                            <el-form-item label="卖家发货物流：">
                                <span>{{barinfor.sellerLogisticsCompany}}</span>
                            </el-form-item>
                            <el-form-item label="卖家发货单号：">
                                <span>{{barinfor.sellerLogisticsNumber}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
            </div>
                
            <!-- 分割线------------------------------------------------ -->

            <div class="returnGoods">
                <div class="returnGoodsTop">
                    <h3>{{isSelect==0?'退':'换'}}货商品</h3>
                    <el-button class="goOrderDetail" @click="jumpOrderDetail">前往查看订单详情</el-button >
                </div>
                <el-table :data="saleGoods" border="" style="width: 100%">
                    <el-table-column prop="goodsName" label="商品名称" width="180" align="center">
                        <template slot-scope="scope">
                            <div  style="display: flex;cursor: pointer;"  @click="previewH5Fn(scope.row)" >
                                <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px"/>
                                <div class="towEllipsis"  style="text-align:center;cursor: pointer;margin-left: 8px;">
                                    <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"  v-if="scope.row.goodsName.length>20" placement="top-start">
                                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;" >
                                        {{scope.row.goodsName}}
                                    </span>
                                    </el-tooltip>
                                     <span v-else style="color: #4e80db;text-decoration: none; cursor: pointer;" >
                                        {{scope.row.goodsName}}
                                    </span>
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
                        >￥{{scope.row.specSellPrice}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
                    <el-table-column prop="specPayPrice" label="总价" align="center">
                        <template
                                slot-scope="scope"
                                v-if="scope.row.specPayPrice!==''&&scope.row.specPayPrice!==null"
                        >￥{{scope.row.specPayPrice}}
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分割线------------------------------------------------ -->
            <div class="operationLog">
                <h3>操作日志</h3>
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
            <div  v-if="!isBtn" style="margin-top: 40px;margin-bottom: 40px">
                <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>
            </div>

            <!-- 分割线------------------------------------------------ -->
            <h3 v-if="isBtn" >平台仲裁审核</h3>
            <el-form ref="form" :model="form" label-width="80px" :rules="reaRule" v-if="isBtn" >
                <el-form-item label="审核理由：" prop="reason">
                    <el-input
                            type="textarea"
                            v-model="form.reason"
                            maxlength=500
                            style="resize:none;margin-left:6px"
                            :autosize="{ minRows: 4, maxRows: 6}"
                            placeholder="请输入内容"
                    ></el-input>
                </el-form-item>
                <el-form-item label="">
                    <div style="margin-left:6px;margin-top:20px;display: flex;justify-content: space-between;width:250px">
                        <el-button @click="agreeGoods(0)">取消</el-button>
                        <el-button type="danger" plain @click="agreeGoods(2)">拒绝</el-button>
                        <el-button type="primary" plain @click="agreeGoods(1)"
                                   :loading="loading">{{loading ? "提交中···" : "同意"}}</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </div>
        <div v-else>
            <orderDet
                    ref="orderDetailCompon"
                    @changePage="changePage"
                    :index="'3'"
                    :orderDetBreaddata="orderDetBreaddata"
            ></orderDet>
        </div>
        <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>



    </div>
</template>
<script>
  import Bread from "@/components/bread";
  import orderDet from "../order/list/orderDet"
  import {arbitrationAudit} from "@/api/api";
  import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5';
  import cloneDeep from 'lodash/cloneDeep'

  export default {
        data() {
            return {
                loading:false,
                form: {reason: ""}, //审核理由
                isSaleList: true,
                orderDetBreaddata: ["售后系统", "售后管理", "售后审核", "售后详情", "订单详情"],
                reaRule: {
                    reason: [{required: true, message: "请输入审核理由", trigger: "blur"}]
                },
                previewH5Visible: false,
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
            "isSelect",
            "isBtn",
            "isSellerFin",
            "isAdminFin",
            "isLog",
            "saleLogs",
            "saleAuditLog",
            "adminAuditLog",
            "arbitrationLog",
            "data",
            "barinfor",
            "saleGoods",
            "piclist", //凭证照片
            "imagesList",
            "row",//查看订单的那一行数据
        ],
        methods: {
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
            //跳到订单详情页面
            jumpOrderDetail() {
                this.isSaleList = false
                this.$nextTick(() => {
                    this.$refs.orderDetailCompon.init(this.row);
                })
            },
            //返回上一级
            changePage() {
                if (this.isSaleList == false) {
                    this.isSaleList = true
                } else {
                    this.$emit("changeState");
                }
            },
            //审核退换货
            agreeGoods(type) {
                //1 通过 2 拒绝 3 待审核
                if (type == 0) {
                    this.changePage();
                    return;
                }
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        const obj = {
                            id: this.data.id,
                            auditLogId:this.arbitrationLog.id,
                            // arbitrationStatus: this.aftersale.aftersaleType,
                            process: 3, //1:商家审核,2:平台审核
                            auditReason: this.form.reason,//仲裁审核理由
                            arbitrationStatus: type
                        };
                        this.loading = true;
                        arbitrationAudit(obj).then(res => {
                            this.loading= false;
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
                        this.loading= false;
                        return false;
                    }
                });
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(window.SITE_CONFIG['pcURL']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
            }
        }
    };
</script>
<style lang="scss" >
.aftersalelog_afterDetClass{
    .creater {
        display: inline-block;
        width: 80px;
        margin: 0 15px;
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
        padding-left:5%;
        padding-right:5%;
        label{
            width:269px;
            height:23px;
        }
    }

    .orderState div {
        // margin-left: 5%;
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
        width:100%;
        h3{
            font-size:18px;
            padding-left: 40px;
        }
        .opctionItem1{
            h3{
                 font-size:18px;
            }
            width:33%;
            margin-top:16px;
            margin-bottom:16px;
            /deep/ .el-form-item {
                display: flex;
                margin-bottom: 0 !important;
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
    }
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

    .bottomBtns {
        text-align: center;
        position: fixed;
        bottom: 20px;
        width: 216px;
        left: 50%;
        z-index: 22;
        background-color: white;
    }

    .saleOrderInfo .formWarp {
        // margin: 10px auto;
    }

    .formWarp {
        // border-bottom: 1px solid #eeeeee;
    }
    .el-form-item {
        margin-bottom:0 !important;
    }
    .returnGoods{
        margin-top:40px;
        .returnGoodsTop{
            h3{
                 font-size:18px;
            }
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
        h3{
            font-size: 18px;
        }
        margin-top:50px;
        font-size:18px;
        font-weight:600;
    }
//    /deep/ th {
//         color:#6185CD !important;
//         background-color:#D2E0F7 !important;
//     }
    .pingzheng{
        /deep/ .el-form-item__content {
            margin-left: 10px !important;
            display: flex;
            width: 1px;
        }
    }
    .towEllipsis {
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        text-overflow: ellipsis;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }
}
</style>
