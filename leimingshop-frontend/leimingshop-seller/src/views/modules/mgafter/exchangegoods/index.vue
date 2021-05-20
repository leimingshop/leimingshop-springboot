<template>
    <div class="exchangeGoods">
        <div v-if="isExchange && !orderDetShow">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>

                <!--导出按钮-->
                <importAndExport
                    style="right: 20px; top: 12px; position: absolute"
                    :importAndExportOptions="importAndExportOptions"
                    :dataForm="dataForm"
                    @getDataList="getDataList"
                    v-if="$hasPermission('sys:aftersale:barter:export')"
                ></importAndExport>

                <el-form
                    :inline="true"
                    ref="dataForm"
                    class="grayLine topGapPadding"
                    :model="dataForm"
                    @keyup.enter.native="getDataList()"
                >
                    <el-form-item label="换货单号：" prop="aftersaleSn">
                        <el-input
                            v-model="dataForm.aftersaleSn"
                            placeholder="换货单号"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="商品货号：" prop="specSerial">
        <el-input v-model="dataForm.specSerial" placeholder="商品货号" clearable></el-input>
      </el-form-item> -->
                    <el-form-item label="商品名称：" prop="goodsName">
                        <el-input
                            v-model="dataForm.goodsName"
                            placeholder="商品名称"
                            clearable
                        ></el-input>
                    </el-form-item>

                    <!-- <el-form-item label="商户ID/名称：" prop="goodsName">
        <el-input v-model="dataForm.goodsName" placeholder="商户ID/名称" clearable></el-input>
      </el-form-item>-->
                    <el-form-item label="申请时间：">
                        <el-date-picker
                            v-model="timeArr"
                            type="datetimerange"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            align="left"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']"
                        ></el-date-picker>
                    </el-form-item>

                    <el-form-item label="会员名称：" prop="memberName">
                        <el-input
                            v-model="dataForm.memberName"
                            placeholder="会员名称"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" @click="getData()"
                            >查询</el-button
                        >
                        <el-button
                            class="btn"
                            type="primary"
                            plain
                            @click="reset()"
                            >重置</el-button
                        >
                    </el-form-item>
                </el-form>
                <div class="formControlArea">
                    <div>
                        <el-radio-group
                            v-model="radio1"
                            @change="agreeChange"
                            style="margin-top: 5px; margin-bottom: 5px"
                        >
                            <el-radio-button
                                :label="item.id"
                                v-for="item in status"
                                :key="item.id"
                                >{{ item.title }}</el-radio-button
                            >
                        </el-radio-group>
                    </div>
                    <div style="display: flex">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>

                <el-alert
                    title="操作提示"
                    type="warning"
                    @close="$store.commit('showListMessage')"
                    v-show="$store.state.listMessageFlag"
                >
                    <template slot="title">
                        <div class="iconSize">操作提示：</div>
                        <div class="iconSize">
                            1、仅售后申请审核通过后的换货申请才会出现在该列表数据中
                        </div>
                    </template>
                </el-alert>
            </div>
            <el-table
                width="100%"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%; margin-top: 8px"
                :height="
                    !$store.state.mainSwitch ? tableheight : tableheightBig
                "
            >
                <el-table-column
                    type="index"
                    prop="$index"
                    label="序号"
                    align="center"
                    width="70"
                >
                    <template slot-scope="scope">{{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}</template>
                </el-table-column>
                <el-table-column
                    prop="aftersaleSn"
                    label="换货单号"
                    align="center"
                    width="200"
                ></el-table-column>
                <!-- <el-table-column prop="specSerial" label="商品货号" align="center" width="200"></el-table-column> -->
                <el-table-column
                    prop="goodsName"
                    label="商品名称"
                    align="center"
                >
                    <template slot-scope="scope">
                        <div
                            style="display: flex; cursor: pointer"
                            @click="previewH5Fn(scope.row)"
                        >
                            <img
                                :src="scope.row.specMainPicture | filterImgUrl"
                                width="40px"
                                height="40px"
                            />
                            <div class="towEllipsis" style="margin-left: 8px">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    :content="scope.row.goodsName"
                                    placement="top-start"
                                >
                                    <span
                                        style="
                                            color: #4e80db;
                                            text-decoration: none;
                                            cursor: pointer;
                                        "
                                        @click="previewH5Fn(scope.row)"
                                    >
                                        {{ scope.row.goodsName }}
                                    </span>
                                </el-tooltip>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="memberName"
                    label="会员名称"
                    align="center"
                    width="120"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    label="申请时间"
                    align="center"
                    width="160"
                ></el-table-column>
                <el-table-column
                    prop="aftersaleStatus"
                    v-if="changeVal == ''"
                    label="换货状态"
                    width="120"
                    align="center"
                    :formatter="statusRules"
                ></el-table-column>
                <!-- <el-table-column prop="aftersaleStatus" v-if="changeVal=='1'" label="商家审核状态" align="center"></el-table-column> -->
                <!-- <el-table-column prop="aftersaleStatus" v-if="changeVal=='1'" label="平台审核状态" align="center"></el-table-column> -->
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="text"
                            v-if="
                                scope.row.aftersaleStatus == 9 &&
                                scope.row.logisticsStatus == 3
                            "
                            @click="delGoods(scope.row)"
                            >发货</el-button
                        >
                        <el-button
                            size="mini"
                            type="text"
                            @click="handleGoodDet(scope.row)"
                            v-if="$hasPermission('sys:aftersale:barter:view')"
                            >查看详情</el-button
                        >
                        <el-button
                            size="mini"
                            type="text"
                            v-if="
                                scope.row.aftersaleStatus == 8 &&
                                $hasPermission('sys:aftersale:barter:confirm')
                            "
                            @click="sureGood(scope.row)"
                            >确认入库</el-button
                        >
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
            ></el-pagination>
            <el-dialog
                title="标记发货"
                :visible.sync="dialogGoods"
                width="500px"
            >
                <el-form :model="goodsform" ref="goodsform" :rules="dataRule">
                    <el-form-item
                        label="物流方式："
                        :label-width="formLabelWidth"
                        prop="devlierType"
                    >
                        <el-radio-group
                            v-model="goodsform.devlierType"
                            style="margin-top: 0px; margin-left: 10px"
                        >
                            <el-radio :label="0">快递</el-radio>
                            <el-radio :label="1">自提</el-radio>
                            <el-radio :label="2">无需物流</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item
                        label="物流公司："
                        :label-width="formLabelWidth"
                        prop="name"
                        v-if="goodsform.devlierType == 0"
                    >
                        <el-select
                            v-model="goodsform.name"
                            placeholder="请选择物流公司"
                            style="width: 280px"
                        >
                            <el-option
                                :label="item.companyName"
                                :value="item.companyName"
                                v-for="(item, index) in logList"
                                :key="index"
                                >{{ item.companyName }}</el-option
                            >
                        </el-select>
                    </el-form-item>
                    <el-form-item
                        label="物流单号："
                        :label-width="formLabelWidth"
                        prop="transportCode"
                        v-if="goodsform.devlierType == 0"
                    >
                        <el-input
                            v-model="goodsform.transportCode"
                            @blur="checkTransportCodeValide()"
                            maxlength="30"
                            autocomplete="off"
                            style="width: 280px"
                        ></el-input>
                    </el-form-item>
                </el-form>
                <div
                    slot="footer"
                    class="dialog-footer"
                    style="text-align: center"
                >
                    <el-button @click="dialogGoods = false">取 消</el-button>
                    <el-button type="primary" @click="goodsLog"
                        >确 定</el-button
                    >
                </div>
            </el-dialog>
        </div>
        <div v-else-if="!isExchange && !orderDetShow" id="exchangeGoodsDetail">
            <Bread
                :breaddata="detdata"
                @changePage="changePage"
                :index="'1'"
            ></Bread>
            <div class="orderState">
                <div>
                    <label>当前换货状态：</label>
                    <span style="color: #01bd25">{{
                        barinfor.aftersaleStatus == 1
                            ? "用户取消"
                            : barinfor.aftersaleStatus == 2
                            ? "退款失败"
                            : barinfor.aftersaleStatus == 3
                            ? "待退货入库"
                            : barinfor.aftersaleStatus == 4
                            ? "确认收货"
                            : barinfor.aftersaleStatus == 5
                            ? "退款中"
                            : barinfor.aftersaleStatus == 6
                            ? "退款成功"
                            : barinfor.aftersaleStatus == 7
                            ? "换货失败"
                            : barinfor.aftersaleStatus == 8
                            ? "待换货入库"
                            : barinfor.aftersaleStatus == 9
                            ? "换货出库中"
                            : "换货成功"
                    }}</span>
                </div>
                <div>
                    <label>换货单号：</label>
                    <span>{{ aftersale.aftersaleSn }}</span>
                </div>
                <div>
                    <label>订单编号：</label>
                    <span>{{ aftersale.orderSn }}</span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->
            <div class="opctionWarp">
                <div class="opctionItem">
                    <h3>买家换货申请</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="买家：">
                                <span>{{ aftersale.contacts }}</span>
                            </el-form-item>
                            <el-form-item label="电话：">
                                <span>{{ aftersale.contactsPhone }}</span>
                            </el-form-item>

                            <el-form-item label="申请时间：">
                                <span>{{ aftersale.createDate }}</span>
                            </el-form-item>

                            <el-form-item label="换货原因：">
                                <span>{{ aftersale.aftersaleReason }}</span>
                            </el-form-item>

                            <el-form-item label="换货说明：" class="">
                                <span>{{ aftersale.aftersaleExplain }}</span>
                            </el-form-item>

                            <el-form-item
                                label="换货凭证："
                                class="pingzheng"
                                style="
                                    display: flex;
                                    justify-content: flex-start;
                                "
                            >
                                <div
                                    v-for="(item, index) in piclist"
                                    style="margin-left: 1px; display: flex"
                                >
                                    <img
                                        id="oImg"
                                        :src="item | filterImgUrl"
                                        alt=""
                                        style="
                                            height: 48px;
                                            width: 48px;
                                            object-fit: scale-down;
                                        "
                                        @click="previewBigImg(item, index)"
                                    />
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
                <!--<span class="inforRight">2019-05-09 15:25:56</span>-->
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
                    <h3>商家换货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="商家名称：">
                                <span>{{ aftersale.storeName }}</span>
                            </el-form-item>
                            <el-form-item label="审核时间：">
                                <span>{{ saleAuditLog.updateDate }}</span>
                            </el-form-item>

                            <el-form-item label="审核理由：">
                                <span>{{
                                    saleAuditLog == ""
                                        ? ""
                                        : saleAuditLog.reason
                                }}</span>
                            </el-form-item>

                            <el-form-item label="审核结果：">
                                <span>{{
                                    saleAuditLog.result == 1
                                        ? "同意"
                                        : saleAuditLog.result == 2
                                        ? "拒绝"
                                        : "审核中"
                                }}</span>
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
                <div
                    class="opctionItem"
                    v-if="null != adminAuditLog && adminAuditLog != ''"
                >
                    <h3>平台换货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="审核时间：">
                                <span>{{ adminAuditLog.updateDate }}</span>
                            </el-form-item>
                            <el-form-item label="审核理由：">
                                <span>{{ adminAuditLog.reason }}</span>
                            </el-form-item>
                            <el-form-item label="审核结果：">
                                <span>{{
                                    adminAuditLog.result == 1
                                        ? "同意"
                                        : adminAuditLog.result == 2
                                        ? "拒绝"
                                        : "审核中"
                                }}</span>
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
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="买家换货时间：">
                                <span>{{ barinfor.buyerDeliveryTime }}</span>
                            </el-form-item>
                            <el-form-item label="买家发货物流：">
                                <span>{{
                                    barinfor.buyerLogisticsCompany
                                }}</span>
                            </el-form-item>
                            <el-form-item label="买家发货单号：">
                                <span>{{ barinfor.buyerLogisticsNumber }}</span>
                            </el-form-item>
                            <el-form-item label="卖家换货时间：">
                                <span>{{ barinfor.sellerDeliveryTime }}</span>
                            </el-form-item>
                            <el-form-item label="卖家发货物流：">
                                <span>{{
                                    barinfor.sellerLogisticsCompany
                                }}</span>
                            </el-form-item>
                            <el-form-item label="卖家发货单号：">
                                <span>{{
                                    barinfor.sellerLogisticsNumber
                                }}</span>
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
                    <h3 style="font-size: 18px">换货商品</h3>
                    <el-button class="goOrderDetail" @click="jumpOrderDetail"
                        >前往查看订单详情</el-button
                    >
                </div>
                <el-table :data="saleGoods" border="" style="width: 100%">
                    <el-table-column
                        prop="goodsName"
                        label="商品名称"
                        width="180"
                    >
                        <template slot-scope="scope">
                            <div
                                style="display: flex; cursor: pointer"
                                @click="previewH5Fn(scope.row)"
                            >
                                <img
                                    :src="
                                        scope.row.specMainPicture | filterImgUrl
                                    "
                                    width="40px"
                                    height="40px"
                                />
                                <div
                                    class="towEllipsis"
                                    style="
                                        text-align: center;
                                        cursor: pointer;
                                        margin-left: 8px;
                                    "
                                >
                                    <el-tooltip
                                        class="item"
                                        effect="dark"
                                        :content="scope.row.goodsName"
                                        placement="top-start"
                                    >
                                        <span
                                            style="
                                                color: #4e80db;
                                                text-decoration: none;
                                                cursor: pointer;
                                            "
                                        >
                                            {{ scope.row.goodsName }}
                                        </span>
                                    </el-tooltip>
                                </div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="specSerial"
                        label="商品货号"
                        width="180"
                    ></el-table-column>
                    <el-table-column
                        prop="specAttrName"
                        label="规格"
                    ></el-table-column>
                    <el-table-column prop="specSellPrice" label="单价">
                        <template slot-scope="scope"
                            >￥{{ scope.row.specSellPrice }}</template
                        >
                    </el-table-column>
                    <el-table-column
                        prop="goodsNum"
                        label="数量"
                    ></el-table-column>
                    <el-table-column prop="specPayPrice" label="总价">
                        <template slot-scope="scope"
                            >￥{{ scope.row.specPayPrice }}</template
                        >
                    </el-table-column>
                </el-table>
            </div>

            <div class="operationLog">
                <h3 style="font-size: 18px">操作日志</h3>
                <div>
                    <el-table
                        width="100%"
                        :data="logList"
                        border=""
                        v-loading=""
                        style="
                            width: 100%;
                            maigin-top: 20px;
                            margin-bottom: 40px;
                        "
                    >
                        <el-table-column
                            prop="creator"
                            label="操作者："
                            align="center"
                        ></el-table-column>
                        <el-table-column
                            label="操作"
                            min-width="100"
                            align="center"
                        >
                            <template slot-scope="scope">
                                <span>{{ scope.row.message }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                            prop="createDate"
                            label="操作时间："
                            align="center"
                        ></el-table-column>
                    </el-table>

                    <!--返回按钮-->
                    <div style="margin-top: 40px; margin-bottom: 40px">
                        <el-button
                            type="primary"
                            style="display: block; margin: 0 auto"
                            @click="changePage()"
                            >返回</el-button
                        >
                    </div>
                </div>
            </div>
            <!--<div class="operationList">-->
            <!--<p>操作日志</p>-->
            <!--<p v-for="item in logList">-->
            <!--{{item.updateDate}}-->
            <!--<span class="creater">{{item.updater}}</span>-->
            <!--{{item.message}}-->
            <!--</p>-->
            <!--</div>-->
        </div>
        <orderDet
            v-else
            ref="orderDetailCompon"
            @changePage="changePage"
            :index="'2'"
            :orderDetBreaddata="orderDetBreaddata"
        ></orderDet>
        <!-- :data="orderDetData"
          :addressInfo="addressInfo"
          :orderLog="orderLog"
          :packageInfo="packageInfo"
          :orderData="orderData" -->
        <!-- 预览h5 -->
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>
<script>
    import importAndExport from "@/components/import-and-export";
    import { aftersaleBarterExport } from "@/api/io";

    import Bread from "@/components/bread";
    import { exchangegoods } from "@/api/url";
    import orderDet from "../../order/list/orderDet";
    import modelPreviewH5 from "../../mggoods/modules/model-preview-h5.vue";
    import {
        exchDetail,
        sureExchangeGoods,
        logCompany,
        exGoodsLog,
        orderDetail,
        orderDetailBybh,
    } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import cloneDeep from "lodash/cloneDeep";
    export default {
        mixins: [mixinViewModule],
        data() {
            //物流单号
            var transport = (rule, value, callback) => {
                if (value == "") {
                    callback(new Error("物流单号不得为空"));
                } else if (value.length > 32) {
                    callback(new Error("物流单号最长限制32位"));
                } else {
                    callback();
                }
            };
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: aftersaleBarterExport, //导出接口
                    exportWord: "导出",
                },
                previewH5Visible: false,
                mixinViewModuleOptions: {
                    getDataListURL: exchangegoods,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                goodsform: {
                    id: "",
                    devlierType: 0,
                    transportCode: "",
                    name: "",
                }, //发货表单
                dataRule: {
                    devlierType: [
                        {
                            required: true,
                            message: "配送方式不得为空",
                            trigger: "blur",
                        },
                    ],
                    name: [
                        {
                            required: true,
                            message: "请输入物流公司",
                            trigger: "blur",
                        },
                    ],
                    transportCode: [{ validator: transport, trigger: "blur" }],
                },
                formLabelWidth: "120px",
                dialogGoods: false, //发货
                isExchange: true,
                breaddata: ["售后管理", "换货管理"],
                detdata: ["售后管理", "换货管理", "换货详情"],
                orderDetBreaddata: ["售后管理", "换货管理", "换货详情", "订单详情"],
                status: [
                    { title: "所有换货订单", id: "" },
                    { title: "待换货入库", id: "8" },
                    { title: "换货出库中", id: "9" },
                    { title: "换货成功", id: "10" },
                    { title: "用户取消", id: "1" },
                ],
                logList: "", //物流
                tableData: [],
                timeArr: "", //申请时间
                dataForm: {
                    specSerial: "",
                    aftersaleSn: "",
                    storeName: "",
                    // goodsName: "",
                    memberName: "",
                    endTime: "",
                    startTime: "",
                },
                totalPage: 0,
                goodsData: [], //售后商品table
                saleGoods: [], //售后申请数据
                saleAuditLog: [], //售后理由数据-平台审核数据
                adminAuditLog: "",
                data: {}, //总数据
                piclist: [], //凭证图片
                total: 0,
                changeVal: "",
                dataListLoading: false,
                detailOrList: true,
                radio1: "",
                goodsData: "",
                data: [],
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                statusRules: function (row, column) {
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
                row: "",
                addressInfo: [], //地址数据
                packageInfo: [], //包裹数据
                orderLog: [], //操作日志
                orderData: [],
                orderDetData: "",
                tableheight: document.body.offsetHeight - 470,
                tableheightBig: 300,
            };
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            //跳到订单详情页面
            jumpOrderDetail() {
                this.orderDetShow = true;
                this.$nextTick(() => {
                    this.$refs.orderDetailCompon.init(this.row);
                });
                // var fn = this.row.id ? orderDetail : orderDetailBybh
                // if(this.row.id){
                //   var obj = { id: this.row.id };
                // }else{
                //   var obj = { orderSn: this.row.aftersaleSn };
                // }
                // fn(obj).then(res => {
                //   if (res.code == 200) {
                //     this.addressInfo = res.data.orderAddressDTO; //收货人信息
                //     this.orderData = res.data.orderGoodsDTOList; //订单商品信息
                //     this.orderLog = res.data.orderLogisticsDTO; //物流信息
                //     this.packageInfo = res.data.orderLogDTOList; //订单操作日志
                //     this.orderDetData = res.data;
                //   } else {
                //     this.$message({
                //       type: "warning",
                //       message: res.msg
                //     });
                //   }
                // });
            },
            checkTransportCodeValide() {
                // 只能输入数字，字母或者汉字!
                if (this.goodsform.transportCode.replace(/[a-zA-Z0-9]/g, "")) {
                    this.$message({
                        message: "物流单号只能输入英文和数字",
                        type: "warning",
                        duration: 1500,
                    });
                    return false;
                }
                return true;
            },
            //获取物流公司
            getLogCom() {
                logCompany().then((res) => {
                    if (res.code == 200) {
                        this.logList = res.data;
                    } else {
                        console.log("获取物流公司失败");
                    }
                });
            },
            //确定发货
            goodsLog() {
                this.$refs["goodsform"].validate((valid) => {
                    if (valid) {
                        const obj = {
                            sellerDeliveryType: this.goodsform.devlierType,
                            aftersaleSn: this.goodsform.id,
                            sellerLogisticsNumber: this.goodsform.transportCode,
                            sellerLogisticsCompany: this.goodsform.name,
                        };
                        if (!this.checkTransportCodeValide()) {
                            return;
                        }
                        exGoodsLog(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.dialogGoods = false;
                                this.goodsform = {
                                    id: "",
                                    devlierType: 1,
                                    transportCode: "",
                                    name: "",
                                };
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg,
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            //发货
            delGoods(item) {
                this.goodsform.id = item.aftersaleSn;
                this.getLogCom();
                this.dialogGoods = true;
            },
            //确认入库
            sureGood(index) {
                const obj = {
                    aftersaleSn: index.aftersaleSn,
                    aftersaleStatus: 9,
                };
                const h = this.$createElement;
                this.$msgbox({
                    title: "商品入库确认",
                    message: h("p", null, [
                        h("p", null, "您确定收到该换货商品了吗? "),
                        h("p", null, "确认收货后，需再次发货给用户 "),
                    ]),
                    showCancelButton: true,
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    beforeClose: (action, instance, done) => {
                        if (action === "confirm") {
                            sureExchangeGoods(obj).then((res) => {
                                if (res.code == 200) {
                                    this.$message({
                                        type: "success",
                                        message: "入库成功",
                                    });
                                    this.getDataList();
                                    done();
                                } else {
                                    this.$message({
                                        type: "error",
                                        message: res.msg,
                                    });
                                }
                            });
                        } else {
                            done();
                        }
                    },
                }).catch(() => {});
            },
            //筛选订单状态
            agreeChange(val) {
                this.page = 1;
                this.limit = 10;
                this.changeVal = val;
                this.dataForm.aftersaleStatus = val;
                this.getDataList();
            },
            //条件查询
            getData() {
                this.dataForm.startTime = this.timeArr[0];
                this.dataForm.endTime = this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.timeArr = [];
                this.dataForm = {};
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //返回上一级
            changePage() {
                if (this.orderDetShow) {
                    this.orderDetShow = false;
                    this.isExchange = this.isExchange;
                } else {
                    this.orderDetShow = false;
                    this.isExchange = !this.isExchange;
                }
            }, // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?specId=" +
                        row.specId
                );
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
                    if (/http/.test(item2) || /data:image/.test(item2)) {
                    } else {
                        imagesArr[index2] =
                            window.SITE_CONFIG["imgURL"] + "" + item2;
                    }
                });
                this.$imagePreview({
                    images: imagesArr,
                    index: index,
                });
            },
            //换货详情
            handleGoodDet(row) {
                this.row = row;
                const obj = {
                    aftersaleSn: row.aftersaleSn,
                };
                exchDetail(obj).then((res) => {
                    if (res.code == 200) {
                        this.data = res.data;
                        this.aftersale = res.data.aftersaleApplyDTO;
                        this.saleGoods = res.data.aftersaleGoodsDTOList;
                        this.barinfor = res.data.aftersaleBarterDTO;
                        res.data.aftersaleAuditLogDTOList.forEach((item, index) => {
                            if (item.process == 1) {
                                this.saleAuditLog = item; //商家审核数据
                            } else if (item.process == 2) {
                                this.adminAuditLog = item; //平台审核数据
                            }
                        });
                        this.logList = res.data.aftersaleLogListDTOList;
                        this.isExchange = !this.isExchange;
                        this.piclist = this.aftersale.aftersalePics.split(",");
                        /************用的orderId替换成订单id查询订单详情，你说气人不****************/
                        this.row.id = res.data.aftersaleApplyDTO.orderId;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            },
        },
        created() {
            //   this.getDataListURL
        },
        components: { Bread, orderDet, modelPreviewH5, importAndExport },
    };
</script>
<style  lang="scss" scoped>
    .exchangeGoods {
        .grayLine {
            margin-bottom: 8px;
            border-bottom: 2px solid #efefef !important;
        }
        .creater {
            display: inline-block;
            width: 80px;
            margin: 0 15px;
        }
        .el-table--border {
            margin-top: 20px;
        }
        .el-radio-group {
            margin-top: 20px;
        }
        .orderState {
            /* width: 100%; */
            border: 1px solid #d1d1d1;
            height: 86px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #d5dff7;
            font-size: 18px;
            font-weight: 600;
            color: rgba(0, 0, 0, 1);
            padding-left: 2%;
            padding-right: 8%;
            label {
                // width:269px;
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
            h3 {
                font-size: 18px;
            }
            .opctionItem {
                h3 {
                    padding-left: 40px;
                }
                margin-left: -8px;
                width: 25%;
                border-right: 1px solid #eeeeee;
                margin-top: 16px;
                margin-bottom: 16px;
                // padding-left: 5%;
            }
        }
        .buyerInfo {
            flex-direction: column;
        }
        .buyerInfo,
        .sellerInfo,
        .buyerInfo,
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
        .imglist {
            width: 100px;
            height: 100px;
        }
        .inforRight {
            margin-left: 40px;
            display: inline-block;
        }
        .right {
            margin-right: 15px;
        }
        .buyerInfo span,
        .sellerInfo span,
        .buyerInfo span,
        .goodsLog span {
            margin-top: 20px;
        }
        #exchangeGoodsDetail {
            /deep/ .el-form-item {
                // margin-top: 0px !important;
                margin-bottom: 0 !important;
                display: flex;
                .el-form-item__label {
                    width: 120px !important;
                    min-width: 120px !important;
                }
                .el-form-item__content div {
                    // margin-left: 10px !important;
                }
                .el-form-item__content {
                    word-wrap: break-word;
                    margin-left: 0 !important;
                    width: 70%;
                }
            }
        }
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
                    background: #dde7f4;
                    color: #395fb7;
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
        // /deep/ th {
        //       color:#6185CD !important;
        //       background-color:#D2E0F7 !important;
        //   }
        .pingzheng /deep/.el-form-item__content {
            margin-left: 10px !important;
            width: 1px;
            display: flex;
        }
        .saleOrderInfo .formWarp {
            /*border-bottom: 1px solid #eeeeee;*/
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
