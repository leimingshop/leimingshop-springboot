<template>
    <div  class="exchangegoods_afterDetClass">
        <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
        <div class="orderState">
            <div>
                 <label>当前换货状态：</label>
                <span style="color:#01BD25;">{{barinfor.aftersaleStatus==1?'用户取消':barinfor.aftersaleStatus==2?'退款失败':barinfor.aftersaleStatus==3?'待退货入库':barinfor.aftersaleStatus==4?'确认收货':barinfor.aftersaleStatus==5?'退款中':barinfor.aftersaleStatus==6?'退款成功':barinfor.aftersaleStatus==7?'换货失败':barinfor.aftersaleStatus==8?'待换货入库':barinfor.aftersaleStatus==9?'换货出库中':'换货成功'}}</span>
            </div>
            <div>
                 <label>换货单号：</label>
                <span>{{aftersale.aftersaleSn}}</span>
            </div>
            <div>
                 <label>订单编号：</label>
                <span>{{aftersale.orderSn}}</span>
            </div>
        </div>

        <div class="opctionWarp">
            <div class="opctionItem">
                <h3>买家换货申请</h3>
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

                        <el-form-item label="换货原因：" >
                            <span>{{aftersale.aftersaleReason}}</span>
                        </el-form-item>

                        <el-form-item label="换货说明：" >
                            <span>{{aftersale.aftersaleExplain}}</span>
                        </el-form-item>

                        <el-form-item label="换货凭证："  class="pingzheng" style="display: flex; justify-content: flex-start;white-space: nowrap;">
                            <div v-for="(item,index) in piclist " style="margin-left: 1px;">
                                <img id="oImg" :src="item | filterImgUrl" alt=""  style="height:60px;width:60px;object-fit: scale-down;" @click="previewBigImg(item,index)" >
                            </div>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <!--<div class="buyerInfo">-->
            <!--<p>买家换货申请</p>-->
            <!--<div>-->
            <!--<span class="inforTit">买家</span>-->
            <!--<span class="inforRight">{{aftersale.contacts}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">电话</span>-->
            <!--<span class="inforRight">{{aftersale.contactsPhone}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">申请时间</span>-->
            <!--<span class="inforRight">{{aftersale.createDate}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">换货原因</span>-->
            <!--<span class="inforRight">{{aftersale.aftersaleReason}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">换货说明</span>-->
            <!--<span class="inforRight">{{aftersale.aftersaleExplain}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit right">换货凭证</span>-->
            <!--<img class="imglist right" v-for="item in piclist" :src="$imgDomain + item" alt="">-->
            <!--&lt;!&ndash; <span class="inforRight">待照片</span> &ndash;&gt;-->
            <!--</div>-->
            <!--</div>-->
            <div class="opctionItem">
                <h3 >商家换货审核</h3>
                <el-form
                        class="saleOrderInfo"
                >
                    <div class="formWarp formWarp1">
                        <el-form-item label="商家名称：" >
                            <span>{{aftersale.storeName}}</span>
                        </el-form-item>
                        <el-form-item label="审核时间：">
                            <span>{{saleAuditLog.updateDate}}</span>
                        </el-form-item>

                        <el-form-item label="审核理由：" >
                            <span>{{saleAuditLog==""?"":saleAuditLog.reason}}</span>
                        </el-form-item>

                        <el-form-item label="审核结果：" >
                            <span>{{saleAuditLog.result==1?'同意':saleAuditLog.result==2?'拒绝':'审核中'}}</span>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <!--<div class="sellerInfo">-->
            <!--<p>商家换货审核</p>-->
            <!--<div>-->
            <!--<span class="inforTit">商家名称</span>-->
            <!--<span class="inforRight">{{aftersale.storeName}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">审核时间</span>-->
            <!--<span class="inforRight">{{aftersale.createDate}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">审核理由</span>-->
            <!--<span class="inforRight">{{saleAuditLog==""?"":saleAuditLog.reason}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">审核结果</span>-->
            <!--<span class="inforRight">{{saleAuditLog.result==1?'同意':'拒绝'}}</span>-->
            <!--</div>-->
            <!--</div>-->
            <div class="opctionItem" v-if="adminAuditLog!=''">
                <h3 >平台换货审核</h3>
                <el-form
                        class="saleOrderInfo"
                >
                    <div class="formWarp formWarp1">
                        <el-form-item label="审核时间：">
                            <span>{{adminAuditLog.updateDate}}</span>
                        </el-form-item>
                        <el-form-item label="审核理由：">
                            <span>{{adminAuditLog==''?'':adminAuditLog.reason}}</span>
                        </el-form-item>
                        <el-form-item label="审核结果：" >
                            <span>{{adminAuditLog.result==1?'同意':adminAuditLog.result==2?'拒绝':'审核中'}}</span>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <!--<div class="buyerInfo" v-if="adminAuditLog!=''">-->
            <!--<p>平台换货处理</p>-->
            <!--<div>-->
            <!--<span class="inforTit">审核时间</span>-->
            <!--<span class="inforRight">{{adminAuditLog.createDate}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">审核理由</span>-->
            <!--<span class="inforRight">{{adminAuditLog==''?'':adminAuditLog.reason}}</span>-->
            <!--</div>-->
            <!--<div>-->
            <!--<span class="inforTit">审核结果</span>-->
            <!--<span class="inforRight">{{adminAuditLog.result==1?'同意':'拒绝'}}</span>-->
            <!--</div>-->
            <!--</div>-->
            <div class="opctionItem">
                <h3>换货物流</h3>
                <el-form
                        class="saleOrderInfo"
                >
                    <div class="formWarp formWarp1">
                        <el-form-item label="买家换货时间：" >
                            <span>{{barinfor.buyerDeliveryTime}}</span>
                        </el-form-item>
                        <el-form-item label="买家发货物流：">
                            <span>{{barinfor.buyerLogisticsCompany}}</span>
                        </el-form-item>
                        <el-form-item label="买家发货单号：" >
                            <span>{{barinfor.buyerLogisticsNumber}}</span>
                        </el-form-item>
                        <el-form-item label="卖家换货时间：" >
                            <span>{{barinfor.sellerDeliveryTime}}</span>
                        </el-form-item>
                        <el-form-item label="卖家发货物流：">
                            <span>{{barinfor.sellerLogisticsCompany}}</span>
                        </el-form-item>
                        <el-form-item label="卖家发货单号：" >
                            <span>{{barinfor.sellerLogisticsNumber}}</span>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
        </div>
         <div class="orderState" v-if="arbitrationDTO">
                <div>
                    <label>当前仲裁审核状态：</label>
                    <span style="color:#01BD25;">{{data.arbitrationStatus==3?'审核中':'已完成'}}</span>
                </div>
        </div>
            <!-- 分割线------------------------------------------------ -->

            <div class="opctionWarp" v-if="arbitrationDTO" >
                <div class="opctionItem" >
                    <h3 >仲裁换货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="会员账号：">
                                <span>{{arbitrationDTO.memberName}}</span>
                            </el-form-item>
                            <el-form-item label="联系人：">
                                <span>{{arbitrationDTO.contacts}}</span>
                            </el-form-item>
                            <el-form-item label="手机号码：">
                                <span>{{arbitrationDTO.contactsWay}}</span>
                            </el-form-item>
                            <el-form-item label="申请仲裁时间：">
                                <span>{{arbitrationDTO.arbitrationApplyDate}}</span>
                            </el-form-item>
                            <el-form-item label="申请内容：">
                                <span>{{arbitrationDTO.detailedDescription}}</span>
                            </el-form-item>
                            <el-form-item label="申请图片：" class="pingzheng" style="display: flex; justify-content: flex-start;">
                                <div v-for="(item,index) in imagesList " style="margin-left: 1px;">
                                    <img id="oImg" :src="item | filterImgUrl" alt="" style="height:60px;width:60px;object-fit: scale-down;" @click="previewBigImg(item,index)">
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <div class="opctionItem" >
                    <h3 >仲裁换货结果</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="仲裁结果：">
                                <span>{{arbitrationDTO.arbitrationStatus==1?'同意':arbitrationDTO.arbitrationStatus==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{arbitrationDTO.auditReason}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
            </div>

        <!--<div class="goodsLog">-->
        <!--<p>退换货物流</p>-->
        <!--<div>-->
        <!--<span class="inforTit">换货时间</span>-->
        <!--<span class="inforRight">{{barinfor.createDate}}</span>-->
        <!--</div>-->
        <!--<div>-->
        <!--<span class="inforTit">换货物流</span>-->
        <!--<span class="inforRight">{{barinfor.sellerLogisticsCompany}}</span>-->
        <!--</div>-->
        <!--<div>-->
        <!--<span class="inforTit">换货单号</span>-->
        <!--<span class="inforRight">{{barinfor.aftersaleSn}}</span>-->
        <!--</div>-->
        <!--</div>-->
        <div class="returnGoods">
            <div class="returnGoodsTop">
                <h3 style="font-size:18px">{{isSelect==0?'退':'换'}}货商品</h3>
                <el-button class="goOrderDetail" @click="jumpOrderDetail">前往查看订单详情</el-button >
            </div>
            <el-table :data="saleGoods" border="" style="width: 100%">
                <el-table-column prop="goodsName" label="商品名称" width="180" align="center">
                    <template slot-scope="scope">
                    <div  style="display: flex;cursor: pointer;"  @click="previewH5Fn(scope.row)" >
                        <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px"/>
                        <div class="towEllipsis"   style="text-align:center;cursor: pointer;" >
                            <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" v-if="scope.row.goodsName.length>20" placement="top-start">
                                <span style="color: #4e80db;text-decoration: none; cursor: pointer;margin-left: 8px;">
                                    {{scope.row.goodsName}}
                                </span>
                            </el-tooltip>
                            <span v-else style="color: #4e80db;text-decoration: none; cursor: pointer;margin-left: 8px;">
                                    {{scope.row.goodsName}}
                                </span>
                        </div>
                    </div>
                    </template>
                </el-table-column>
                <el-table-column prop="specId" label="商品货号" width="180" align="center"></el-table-column>
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
        <!--<div class="goods">-->
        <!--<p>换货商品</p>-->
        <!--<el-table :data="saleGoods" border="" style="width: 100%">-->
        <!--<el-table-column prop="goodsName" label="商品名称" width="180" align="center"></el-table-column>-->
        <!--<el-table-column prop="specSerial" label="商品货号" width="180" align="center" ></el-table-column>-->
        <!--<el-table-column prop="specName" label="规格" align="center"></el-table-column>-->
        <!--<el-table-column prop="specSellPrice" label="单价" align="center">-->
        <!--<template slot-scope="scope">￥{{scope.row.specSellPrice}}</template>-->
        <!--</el-table-column>-->
        <!--<el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>-->
        <!--<el-table-column prop="specPayPrice" label="总价" align="center">-->
        <!--<template slot-scope="scope">￥{{scope.row.specPayPrice}}</template>-->
        <!--</el-table-column>-->
        <!--</el-table>-->
        <!--</div>-->
         <div class="operationLog">
            <h3 style="font-size:18px">操作日志</h3>
            <div>
                <el-table
                        width="100%"
                        :data="logList"
                        border=""
                        v-loading=""
                        style="width: 100%;maigin-top:18px;margin-bottom:40px;"
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
        </div>
        <!--返回按钮-->
        <div style="margin-top: 40px;margin-bottom: 40px">
            <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>
        </div>

        <!--<div class="operationList">-->
        <!--<p>操作日志</p>-->
        <!--<p v-for="item in logList">-->
        <!--{{item.updateDate}}-->
        <!--<span class="creater">{{item.updater}}</span>-->
        <!--{{item.message}}-->
        <!--</p>-->
        <!--</div>-->
<modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import { exchDetail } from "@/api/api";
    import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5'
    import cloneDeep from 'lodash/cloneDeep'
    export default {
        data() {
            return {
                breaddata:["订单管理", "售后管理", "换货管理", "换货详情"],
                detailStatus:false,
                row:"",
                isSelect:'0',
                barinfor:{},
                aftersale:'',
                saleGoods:[],
                arbitrationDTO:'',
                saleAuditLog:'',
                adminAuditLog:'',
                logList:[],
                isExchange:'',
                piclist:[],
                imagesList:[],
                previewH5Visible:false,
                data:'',
            }
        },
        components: {
            Bread,modelPreviewH5
        },
        methods:{
            init(row){
                this.row = row;
                if(row){
                    this.backScan();
                }
            },
            changePage(){
                this.$emit("changePageNum",1,);
            },
            jumpOrderDetail(){
                this.$emit("changePageNum",3,this.row);
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
            //换货详情
            backScan(id) {
                const obj = {
                    aftersaleSn: this.row.aftersaleSn
                };
                exchDetail(obj).then(res => {
                    if (res.code == 200) {
                        this.data = res.data;
                        this.aftersale = res.data.aftersaleApplyDTO;
                        this.saleGoods = res.data.aftersaleGoodsDTOList;
                        this.barinfor = res.data.aftersaleBarterDTO;
                        this.arbitrationDTO = res.data.arbitrationDTO;
                        if (this.arbitrationDTO && this.arbitrationDTO.imagesArr){
                            this.imagesList =this.arbitrationDTO.imagesArr.split(",");
                        }
                        // this.saleAuditLog = res.data.aftersaleAuditLogDTOList[0]; //商家审核数据
                        // this.adminAuditLog = res.data.aftersaleAuditLogDTOList[1] ||''; //平台审核数据
                        res.data.aftersaleAuditLogDTOList.forEach((item,index)=>{
                            if(item.process==1){
                            this.saleAuditLog = item; //商家审核数据
                            }else if(item.process==2){
                            this.adminAuditLog = item; //平台审核数据
                            }
                        })
                        this.logList = res.data.aftersaleLogListDTOList;
                        this.isExchange = !this.isExchange;
                        this.piclist = this.aftersale.aftersalePics.split(",");
                        this.row.orderId =  res.data.aftersaleApplyDTO.orderId
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
            },
            // 预览h5
            previewH5Fn(row){
                window.open(window.SITE_CONFIG['pcURL']+'/goodsDetails?goodsId='+row.goodsId+'&specId='+row.specId);
            }
        }
    }
</script>
<style lang="scss" scoped>
.exchangegoods_afterDetClass{
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
    .opctionWarp{
        // padding-left:5%;
        // padding-right:5%;
        display: flex;
        width: 100%;
        justify-content: space-between;
        border-bottom: 1px solid #eeeeee;
        background-color: #fcfbfe;
        // height: 295px;
        font-size:16px;
        h3{
            font-size:18px;
        }
        .opctionItem{
            width:25%;
            border-right: 1px solid #eeeeee;
            margin-top:16px;
            margin-bottom:16px;
            margin-left: -8px;
            // display: flex;
            h3{
              padding-left: 40px;
            }
            /deep/ .el-form-item {
                margin-bottom: 0 !important;
                display: flex;
                .el-form-item__label{
                    width: 120px!important;
                    min-width: 120px !important;
                }
                .el-form-item__content{
                    // display: flex;
                    word-wrap: break-word;
                    margin-left: 0 !important;
                    width: 70%;
                }
            }
        }
    }
    .el-form-item {
        margin-bottom:0 !important;
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
//    /deep/ th {
//         color:#6185CD !important;
//         background-color:#D2E0F7 !important;
//     }
    .pingzheng /deep/.el-form-item__content{
        margin-left: 0 !important;
        display: flex;
    }
    .towEllipsis{
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }
}
</style>
