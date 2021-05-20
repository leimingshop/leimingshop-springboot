<template>
    <div class="returngoodsWarp">
        <div v-show="isOrderDet && !orderDetShow">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>

                <!--导出按钮-->
                <importAndExport style="right: 20px;top: 12px;position: absolute"
                    :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
                    v-if="$hasPermission('sys:aftersale:return:export')"></importAndExport>

                <el-form :inline="true" ref="dataForm" class="grayLine noMargin topGapPadding" :model="dataForm">
                    <el-form-item label="退货单号：" prop="aftersaleSn">
                        <el-input v-model="dataForm.aftersaleSn" placeholder="退货单号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商品货号：" prop="specSerial">
                        <el-input v-model="dataForm.specSerial" placeholder="商品货号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商户名称：" prop="storeName">
                        <el-input v-model="dataForm.storeName" placeholder="商户名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="会员名称：" prop="memberName">
                        <el-input v-model="dataForm.memberName" placeholder="会员名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商品名称：" prop="goodnum">
                        <el-input v-model="dataForm.goodsName" placeholder="商品名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="申请时间：">
                        <el-date-picker v-model="timeArr" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                            align="left" start-placeholder="开始日期" end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']"></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                        <el-button class="btn" type="primary" plain @click="resetForm('dataForm')">重置</el-button>
                    </el-form-item>
                    <br>

                </el-form>
            
                <div class="formControlArea">
                    <el-radio-group v-model="radio1" @change="agreeChange" style="margin-top: 0px;margin-bottom: 0px">
                        <el-radio-button :label="item.id" v-for="(item,index) in status" :key="item.id">{{item.title}}
                        </el-radio-button>
                    </el-radio-group>
                    <div style="display:flex;">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>
                <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                    <template slot='title'>
                        <div class="iconSize">操作提示：</div>
                        <div class="iconSize">1、仅售后申请审核通过后的退货申请才会出现在该列表数据中</div>
                    </template>
                </el-alert>
            </div>
            <el-table width="100%" :data="dataList" border="" v-loading="dataListLoading"
                style="width: 100%;" :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
                <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                    <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
                </el-table-column>
                <el-table-column prop="aftersaleSn" label="退货单号" align="center" width="200"></el-table-column>
                <el-table-column prop="specSerial" label="商品货号" align="center" width="120"></el-table-column>
                <el-table-column prop="goodsName" label="商品名称" width="300" align="center">
                    <template slot-scope="scope">
                        <div style="display: flex;cursor: pointer;" @click="previewH5Fn(scope.row)">
                            <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                            <div class="towEllipsis" style="margin-left:8px">
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                    placement="top-start">
                                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;"
                                        @click="previewH5Fn(scope.row)">
                                        {{scope.row.goodsName}}
                                    </span>
                                </el-tooltip>
                                <!-- <span v-else style="color: #4e80db;text-decoration: none; cursor: pointer;" @click="previewH5Fn(scope.row)" >
                        {{scope.row.goodsName}}
                    </span> -->
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="memberName" label="会员名称" align="center" width="120"></el-table-column>
                <el-table-column prop="storeName" label="商户名称" align="center"></el-table-column>
                <el-table-column prop="createDate" label="申请时间" align="center" width="180"></el-table-column>
                <el-table-column prop="refundAmount" label="退款金额" width="100" align="right">
                    <template slot-scope="scope">￥{{scope.row.refundAmount}}</template>
                </el-table-column>
                <el-table-column prop="aftersaleStatus" v-if="changeVal==''" label="退货状态" align="center"
                    :formatter="statusRules" width="100"></el-table-column>
                <el-table-column label="操作" min-width="150" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" @click="handleGoodDet(scope.row)"
                            v-if="$hasPermission('sys:aftersale:return:view')">查看详情</el-button>
                        <el-button v-if="scope.row.aftersaleStatus==2 && $hasPermission('sys:wx:refund')" size="mini"
                            type="text" @click="returngoodsAgain(scope.row)" style="margin-left:20px;">重新退款</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="pageSizeChangeHandle" @current-change="pageCurrentChangeHandle"
                :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>
        <!--退货详情-->
        <div v-if="!isOrderDet && !orderDetShow">
            <Bread :breaddata="detdata" @changePage="changePage" :index="'2'"></Bread>
            <div class="orderState">
                <div>
                    <label>当前退货状态：</label>
                    <span
                        style="color:#01BD25;">{{returnInfo.aftersaleStatus==1?'用户取消':returnInfo.aftersaleStatus==2?'退款失败':returnInfo.aftersaleStatus==3?'待退货入库':returnInfo.aftersaleStatus==4?'确认收货':returnInfo.aftersaleStatus==5?'退款中':returnInfo.aftersaleStatus==6?'退款成功':returnInfo.aftersaleStatus==7?'换货失败':returnInfo.aftersaleStatus==8?'待换货入库':returnInfo.aftersaleStatus==9?'换货出库中':'换货成功'}}</span>
                </div>
                <div>
                    <label>退货单号：</label>
                    <span>{{aftersale.aftersaleSn}}</span>
                </div>
                <div>
                    <label>订单编号：</label>
                    <span>{{aftersale.orderSn}}</span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->

            <div class="opctionWarp">
                <div class="opctionItem">
                    <h3>买家退货申请</h3>
                    <el-form class="saleOrderInfo" style="margin-bottom:40px;">
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

                            <el-form-item label="退货原因：">
                                <span>{{aftersale.aftersaleReason}}</span>
                            </el-form-item>

                            <el-form-item label="退货说明：">
                                <span>{{aftersale.aftersaleExplain}}</span>
                            </el-form-item>

                            <el-form-item label="退货凭证：" class="pingzheng"
                                style="display:flex;justify-content:flex-start;white-space: nowrap;">
                                <div v-for="(item,index) in piclist">
                                    <img id="oImg" :src="item | filterImgUrl" alt=""
                                        style="height:60px;width:60px;object-fit: scale-down;"
                                        @click="previewBigImg(item,index)">
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <!--<div class="buyerInfo">-->
                <!--<p>买家退货申请</p>-->
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
                <!--<span class="inforTit">退货原因</span>-->
                <!--<span class="inforRight">{{aftersale.aftersaleReason}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">退货说明</span>-->
                <!--<span class="inforRight">{{aftersale.aftersaleExplain}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit right">退货凭证</span>-->
                <!--<img class="imglist right" v-for="item in piclist" :src="$imgDomain + item" alt="">-->
                <!--</div>-->
                <!--</div>-->
                <!--<div class="sellerInfo">-->
                <!--<p>商家退货审核</p>-->
                <!--<div>-->
                <!--<span class="inforTit">商家名称</span>-->
                <!--<span class="inforRight">{{aftersale.storeName}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">审核时间</span>-->
                <!--<span class="inforRight">{{saleAuditLog.createDate}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">审核理由</span>-->
                <!--<span class="inforRight">{{saleAuditLog==""?'':saleAuditLog.reason}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">审核结果</span>-->
                <!--<span class="inforRight">{{saleAuditLog.result==1?'同意':'拒绝'}}</span>-->
                <!--</div>-->
                <!--</div>-->
                <div class="opctionItem">
                    <h3>商家退货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="商家名称：">
                                <span>{{aftersale.storeName}}</span>
                            </el-form-item>
                            <el-form-item label="审核时间：">
                                <span>{{saleAuditLog && saleAuditLog.updateDate}}</span>
                            </el-form-item>

                            <el-form-item label="审核理由：">
                                <span>{{saleAuditLog && saleAuditLog !=""?saleAuditLog.reason:''}}</span>
                            </el-form-item>

                            <el-form-item label="审核结果：">
                                <span>{{saleAuditLog && saleAuditLog.result==1?'同意':saleAuditLog && saleAuditLog.result==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <!--<div class="sellerInfo" v-if="adminAuditLog!=''">-->
                <!--<p>平台退货审核</p>-->
                <!--<div>-->
                <!--<span class="inforTit">审核时间</span>-->
                <!--<span class="inforRight">{{adminAuditLog.createDate}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">审核理由</span>-->
                <!--<span class="inforRight">{{adminAuditLog==""?'':saleAuditLog.reason}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">审核结果</span>-->
                <!--<span class="inforRight">{{adminAuditLog.result==1?'同意':'拒绝'}}</span>-->
                <!--</div>-->
                <!--</div>-->
                <div class="opctionItem" v-if="adminAuditLog && adminAuditLog!=''">
                    <h3>平台退货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="审核时间：">
                                <span>{{adminAuditLog.updateDate}}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{adminAuditLog==""?'':saleAuditLog.reason}}</span>
                            </el-form-item>
                            <el-form-item label="审核结果：">
                                <span>{{adminAuditLog.result==1?'同意':adminAuditLog.result==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <!--<div class="goodsLog">-->
                <!--<p>退换货物流</p>-->
                <!--<div>-->
                <!--<span class="inforTit">退货时间</span>-->
                <!--<span class="inforRight">{{returnInfo.createDate}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">退货物流</span>-->
                <!--<span class="inforRight">{{returnInfo.logisticsCompany}}</span>-->
                <!--</div>-->
                <!--<div>-->
                <!--<span class="inforTit">退货单号</span>-->
                <!--<span class="inforRight">{{returnInfo.aftersaleSn}}</span>-->
                <!--</div>-->
                <!--</div>-->
                <div class="opctionItem">
                    <h3 style="font-size: 20px;">退货物流</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="退货时间：">
                                <span v-if="returnInfo.logisticsCompany">{{returnInfo.createDate}}</span>
                            </el-form-item>
                            <el-form-item label="退货物流：">
                                <span>{{returnInfo.logisticsCompany}}</span>
                            </el-form-item>
                            <el-form-item label="退货单号：">
                                <span>{{returnInfo.logisticsNumber}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
            </div>
            <div class="orderState" v-if="arbitrationDTO">
                <div>
                    <label>当前仲裁审核状态：</label>
                    <span style="color:#01BD25;">{{arbitrationDTO.arbitrationStatus==3?'审核中':'已完成'}}</span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->

            <div class="opctionWarp" v-if="arbitrationDTO">
                <div class="opctionItem">
                    <h3>仲裁退货审核</h3>
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
                            <el-form-item label="申请图片：" class="pingzheng"
                                style="display: flex; justify-content: flex-start;">
                                <div v-for="(item,index) in imagesList " style="margin-left: 1px;">
                                    <img id="oImg" :src="item | filterImgUrl" alt=""
                                        style="height:60px;width:60px;object-fit: scale-down;"
                                        @click="previewBigImg(item,index)">
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <div class="opctionItem">
                    <h3>仲裁退货结果</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="仲裁结果：">
                                <span>{{arbitrationDTO && arbitrationDTO.arbitrationStatus==1?'同意':arbitrationDTO && arbitrationDTO.arbitrationStatus==2?'拒绝':'审核中'}}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{arbitrationDTO && arbitrationDTO.auditReason}}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
            </div>
            <!--<div class="goods">-->
            <!--<p>退货商品</p>-->
            <!--<el-table :data="saleGoods" border="" style="width: 100%">-->
            <!--<el-table-column prop="goodsName" label="商品名称" width="180" align="center"></el-table-column>-->
            <!--<el-table-column prop="specSerial" label="商品货号" width="180" align="center"></el-table-column>-->
            <!--<el-table-column prop="specAttrName" label="规格" align="center"></el-table-column>-->
            <!--<el-table-column prop="specSellPrice" label="单价" align="center">-->
            <!--<template-->
            <!--slot-scope="scope"-->
            <!--v-if="scope.row.specSellPrice!==''&&scope.row.specSellPrice!==null"-->
            <!--&gt;￥{{scope.row.specSellPrice}}</template>-->
            <!--</el-table-column>-->
            <!--<el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>-->
            <!--<el-table-column prop="specPayPrice" label="总价" align="center">-->
            <!--<template-->
            <!--slot-scope="scope"-->
            <!--v-if="scope.row.specPayPrice!==''&&scope.row.specPayPrice!==null"-->
            <!--&gt;￥{{scope.row.specPayPrice}}</template>-->
            <!--</el-table-column>-->
            <!--</el-table>-->
            <!--</div>-->
            <div class="returnGoods">
                <div class="returnGoodsTop">
                    <h3 style="font-size: 18px;">退货商品</h3>
                    <el-button class="goOrderDetail" @click="jumpOrderDetail">前往查看订单详情</el-button>
                </div>
                <el-table :data="saleGoods" border="" style="width: 100%">
                    <el-table-column prop="goodsName" label="商品名称" width="180" align="center"
                        style="text-align:center;cursor: pointer;">
                        <template slot-scope="scope">
                            <div style="display: flex;cursor: pointer;" @click="previewH5Fn(scope.row)">
                                <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                                <div class="towEllipsis" style="text-align:center;cursor: pointer;margin-left: 8px;">
                                    <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                        placement="top-start">
                                        <span style="color: #4e80db;text-decoration: none; cursor: pointer;">
                                            {{scope.row.goodsName}}
                                        </span>
                                    </el-tooltip>

                                </div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="specId" label="商品货号" width="180" align="center"></el-table-column>
                    <el-table-column prop="specAttrName" label="规格" align="center"></el-table-column>
                    <el-table-column prop="specSellPrice" label="单价" align="center">
                        <template slot-scope="scope"
                            v-if="scope.row.specSellPrice!==''&&scope.row.specSellPrice!==null">￥{{scope.row.specSellPrice}}</template>
                    </el-table-column>
                    <el-table-column prop="goodsNum" label="数量" align="center"></el-table-column>
                    <el-table-column prop="specPayPrice" label="总价" align="center">
                        <template slot-scope="scope"
                            v-if="scope.row.specPayPrice!==''&&scope.row.specPayPrice!==null">￥{{scope.row.specPayPrice}}</template>
                    </el-table-column>
                </el-table>
            </div>
            <!--<div class="operationList">-->
            <!--<p>操作日志</p>-->
            <!--<p v-for="item in logList">-->
            <!--{{item.updateDate}}-->
            <!--<span class="creater">{{item.updater}}</span>-->
            <!--{{item.message}}-->
            <!--</p>-->
            <!--</div>-->
            <div class="operationLog">
                <h3 style="font-size: 18px;">操作日志</h3>
                <el-table width="100%" :data="logList" border="" v-loading=""
                    style="width: 100%;margin-bottom:40px;">
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

        </div>
        <orderDet ref="orderDetailCompon" v-if="!((isOrderDet && !orderDetShow) || (!isOrderDet && !orderDetShow) )"
            @changePage="changePage" :index="'3'" :orderDetBreaddata="orderDetBreaddata"></orderDet>
        <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>



    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import importAndExport from "@/components/import-and-export"
    import {
        afterReturnExport
    } from '@/api/io'
    import {
        returngoods,
        exportsales
    } from "@/api/url";
    import {
        returnDetail,
        wxRefund
    } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import orderDet from "../order/list/orderDet";
    import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5';
    import cloneDeep from 'lodash/cloneDeep'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {

                /*导出*/
                importAndExportOptions: {
                    exportUrl: afterReturnExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: returngoods,
                    getDataListIsPage: true,
                    exportURL: exportsales,
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id"
                },
                goodsData: [],
                isOrderDet: true,
                breaddata: ["订单管理", "售后管理", "退货管理"],
                detdata: ["订单管理", "售后管理", "退货管理", "退货详情"],
                orderDetBreaddata: ["订单管理", "售后管理", "退货管理", "退货详情", "订单详情"],
                status: [{
                        title: "所有退货订单",
                        id: ""
                    },
                    {
                        title: "待退货入库",
                        id: "3"
                    },
                    {
                        title: "退款中",
                        id: "5"
                    },
                    {
                        title: "退款失败",
                        id: "2"
                    },
                    {
                        title: "退款成功",
                        id: "6"
                    },
                    {
                        title: "用户取消",
                        id: "1"
                    }
                ],
                tableData: [],
                timeArr: "", //下单时间
                dataForm: {
                    aftersaleSn: "",
                    specSerial: "",
                    storeName: "",
                    memberName: "",
                    goodnum: "",
                    startTime: "",
                    endTime: ""
                },
                arbitrationDTO: "",
                imagesList: [],
                piclist: [],
                changeVal: "",
                totalPage: 0,
                dataListLoading: false,
                detailOrList: true,
                radio1: "",
                goodsData: [], //售后商品table
                saleGoods: [], //售后申请数据
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10 //每页显示的条数
                },
                statusRules: function(row, column) {
                    return row.aftersaleStatus == 1 ? (
                    <el-tag type="danger">用户取消</el-tag>
                    ) : row.aftersaleStatus == 2 ? (
                    <el-tag type="info">退款失败</el-tag>
                    ) : row.aftersaleStatus == 3 ? (
                    <el-tag type="warning">待退货入库</el-tag>
                    ) : row.aftersaleStatus == 4 ? (
                    <el-tag type="info">确认收货</el-tag>
                    ) : row.aftersaleStatus == 5 ? (
                    <el-tag type="warning">退款中</el-tag>
                    ) : row.aftersaleStatus == 6 ? (
                    <el-tag type="success">退款成功</el-tag>
                    ) : row.aftersaleStatus == 7 ? (
                    <el-tag type="info">换货失败</el-tag>
                    ) : row.aftersaleStatus == 8 ? (
                    <el-tag type="warning">待换货入库</el-tag>
                    ) : row.aftersaleStatus == 9 ? (
                    <el-tag type="warning">换货出库中</el-tag>
                    ) : (
                    <el-tag type="success">换货成功</el-tag>
                    );
                },
                orderDetShow: false,
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 500,
                tableheightBig:0
            };
        },
        components: {
            Bread,
            orderDet,
            modelPreviewH5,
            importAndExport
        },
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
        },
        methods: {
            //调到订单详情页面
            jumpOrderDetail() {
                this.orderDetShow = true
                // this.row.id = this.row.aftersaleSn
                this.$nextTick(() => {
                    this.$refs.orderDetailCompon.init(this.row);
                })
            },
            //导出
            exForm() {
                this.exportHandle();
            },
            //大图预览带分页
            previewBigImg(images, index) {
                //string转数组
                var imagesArr = images ? images.split(",") : [];
                if (imagesArr.length == 0) {
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2, index2) => {
                    if (/http/.test(item2) || /data:image/.test(item2)) {} else {
                        imagesArr[index2] = window.SITE_CONFIG['imgURL'] + "" + item2;
                    }
                })
                this.$imagePreview({
                    images: imagesArr,
                    index: index,

                })
            },
            //订单状态筛选
            agreeChange(val) {
                //  this.params= {
                //   currentPage: 1, //当前页数
                //   currentPageSize: 10 //每页显示的条数
                // };
                this.page = 1;
                this.limit = 10;
                this.changeVal = val;
                this.dataForm.aftersaleStatus = val;
                this.getDataList();
            },
            //重置
            resetForm(formName) {
                this.timeArr = [];
                this.dataForm.startTime = "";
                this.dataForm.endTime = "";
                this.dataForm.goodsName = "";
                this.dataForm.aftersaleSn = "";
                this.dataForm.specSerial = "";
                this.dataForm.storeName = "";
                this.dataForm.memberName = "";
                this.$refs[formName].resetFields();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //条件查询
            getData() {
                this.dataForm.startTime = this.timeArr && this.timeArr[0];
                this.dataForm.endTime = this.timeArr && this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //查看详情
            handleGoodDet(row) {
                this.row = row;
                const obj = {
                    aftersaleSn: row.aftersaleSn
                };
                returnDetail(obj).then(res => {
                    if (res.code == 200) {
                        this.isOrderDet = false;
                        console.log(res.data);
                        this.aftersale = res.data.aftersaleApplyDTO;
                        this.saleGoods = res.data.aftersaleGoodsDTOList;
                        this.returnInfo = res.data.aftersaleReturnDTO;
                        this.arbitrationDTO = res.data.arbitrationDTO;
                        if (this.arbitrationDTO && this.arbitrationDTO.imagesArr) {
                            this.imagesList = this.arbitrationDTO.imagesArr.split(",");
                        }
                        this.saleAuditLog = "";
                        this.adminAuditLog = "";
                        res.data.aftersaleAuditLogDTOList.forEach((item, index) => {
                            if (item.process == 1) {
                                this.saleAuditLog = item; //商家审核数据
                            } else if (item.process == 2) {
                                this.adminAuditLog = item; //平台审核数据
                            }
                        })
                        this.logList = res.data.aftersaleLogListDTOList; //操作日志
                        this.piclist = this.aftersale.aftersalePics.split(",");
                        /************用的orderId替换成订单id查询订单详情，你说气人不****************/
                        this.row.id = res.data.aftersaleApplyDTO.orderId;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
            },
            //返回上一级
            changePage() {
                if (this.orderDetShow) {
                    this.orderDetShow = false
                    this.isOrderDet = this.isOrderDet;
                } else {
                    this.orderDetShow = false
                    this.isOrderDet = !this.isOrderDet;
                }
            },
            // 重新退款
            returngoodsAgain(row) {
                let that = this;
                that.$confirm("是否确认重新退货？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    var obj = {
                        id: row.aftersaleReturnRecordId
                    }
                    wxRefund(obj).then((res) => {
                        if (res.code == 200) {
                            that.getDataList();
                            that.$message.success(res.msg);

                        } else {
                            that.$message.error(res.msg);
                        }
                    })
                }).catch(() => {})
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(window.SITE_CONFIG['pcURL'] + '/goodsDetails?specId=' + row.specId);
            },
        }
    };
</script>
<style lang="scss" scoped>
    .returngoodsWarp {
        .creater {
            display: inline-block;
            width: 80px;
            margin: 0 15px;
        }

        //.el-table--border {
            //margin-top: 20px;
        //}

        .el-radio-group {
            margin-top: 20px;
        }

        .imglist {
            width: 100px;
            height: 100px;
        }

        .right {
            margin-right: 15px;
        }

        .orderState {
            /* width: 100%; */
            border: 1px solid #d1d1d1;
            height: 86px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #D5DFF7;
            font-size: 18px;
            font-weight: 600;
            color: rgba(0, 0, 0, 1);
            padding-left: 5%;
            padding-right: 5%;

            label {
                width: 269px;
                height: 23px;
            }
        }

        .opctionWarp {
            // padding-left:5%;
            // padding-right:5%;
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #eeeeee;
            background-color: #fcfbfe;
            // height: 295px;
            font-size: 16px;
            width: 100%;

            h3 {
                font-size: 18px;
            }

            .opctionItem {
                h3 {
                    padding-left: 25px;
                }

                width:34%;
                border-right: 1px solid #eeeeee;
                margin-top:16px;
                margin-bottom:16px;

                // padding-left: 25px;
                /deep/ .el-form-item {
                    margin-bottom: 0 !important;
                    display: flex;

                    .el-form-item__label {
                        width: 120px !important;
                        min-width: 120px !important;
                    }

                    .el-form-item__content {
                        display: flex;
                        word-wrap: break-word;
                        margin-left: 0 !important;
                        width: 70%;
                    }
                }
            }
        }

        .buyerInfo {
            flex-direction: column;
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

        .buyerInfo span,
        .sellerInfo span,
        .goodsLog span {
            margin-top: 20px;
        }

        /deep/ .el-form-item {
            margin-top: 0px !important;
        }

        // .el-form-item {
        //     margin-bottom:0 !important;
        // }
        .returnGoods {
            margin-top: 40px;

            .returnGoodsTop {
                font-size: 18px;
                font-weight: 600;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .goOrderDetail {
                    display: flex;
                    align-items: center;
                    padding: 0;
                    justify-content: center;
                    font-size: 14px;
                    background: #DDE7F4;
                    color: #395FB7;
                    width: 131px;
                    height: 24px;
                }
            }
        }

        .operationLog {
            margin-top: 50px;
            font-size: 18px;
            font-weight: 600;
        }

        //  /deep/ th {
        //       color:#6185CD !important;
        //       background-color:#D2E0F7 !important;
        //   }
        .pingzheng /deep/.el-form-item__content {
            margin-left: 10px !important;
            display: flex;
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