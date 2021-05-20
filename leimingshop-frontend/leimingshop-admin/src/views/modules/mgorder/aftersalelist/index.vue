<template>
    <div>
        <div v-show="isChange">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>
                <el-form
                    :inline="true"
                    ref="dataForm"
                    class="grayLine topGapPadding"
                    :model="dataForm"
                >
                    <el-form-item label="申请单号：" prop="aftersaleSn">
                        <el-input
                            v-model="dataForm.aftersaleSn"
                            placeholder="申请单号"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="商品货号：" prop="specSerial">
                        <el-input
                            v-model="dataForm.specSerial"
                            placeholder="商品货号"
                            rable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="商户名称：" prop="storeName">
                        <el-input
                            v-model="dataForm.storeName"
                            placeholder="商户名称"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="会员名称：" prop="memberName" v-if="">
                        <el-input
                            v-model="dataForm.memberName"
                            placeholder="会员名称"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="商品名称：" prop="goodsName">
                        <el-input
                            v-model="dataForm.goodsName"
                            placeholder="商品名称"
                            clearable
                        ></el-input>
                    </el-form-item>
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
                    <el-form-item label="售后类型：" prop="aftersaleType">
                        <el-select
                            v-model="dataForm.aftersaleType"
                            placeholder="售后类型"
                        >
                            <el-option label="全部" value=""></el-option>
                            <el-option label="退货" value="0">退货</el-option>
                            <el-option label="换货" value="1">换货</el-option>
                            <!-- <el-option label="仅退款" value="2">仅退款</el-option> -->
                        </el-select>
                    </el-form-item>
                    <el-form-item label="售后状态：">
                        <el-select
                            v-model="dataForm.auditResult"
                            placeholder="售后状态"
                        >
                            <el-option label="全部" value=""></el-option>
                            <el-option label="审核中" value="3"
                                >审核中</el-option
                            >
                            <el-option label="审核通过" value="1"
                                >审核通过</el-option
                            >
                            <el-option label="审核拒绝" value="2"
                                >审核拒绝</el-option
                            >
                            <el-option label="用户取消" value="4"
                                >用户取消</el-option
                            >
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" @click="getData()"
                            >查询</el-button
                        >
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            class="btn"
                            type="primary"
                            plain
                            @click="reset('dataForm')"
                            >重置</el-button
                        >
                    </el-form-item>
                </el-form>
                <div class="formControlArea">
                    <div></div>
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
                            1、申请记录包括该系统存在的所有申请的记录数据
                        </div>
                        <div class="iconSize">
                            2、申请单所需的全部审核流程的必须人员全部审核通过，该申请单才为审核通过
                        </div>
                        <div class="iconSize">
                            3、申请单所需的全部审核流程的必须人员存在审核拒绝，该申请单为审核拒绝
                        </div>
                        <div class="iconSize">
                            4、申请单仍处于审核流程中，该申请单为审核中
                        </div>
                        <div class="iconSize">
                            5、该申请单在最后一个审核人员审核通过之前，用户进行了取消操作，该申请单为用户取消
                        </div>
                        <div class="iconSize">
                            6、申请单处于状态的最终状态时，申请单不在跟随后续操作进行状态变化
                        </div>
                    </template>
                </el-alert>
            </div>

            <el-table
                width="100%"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%,maigin-top:20px;"
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
                    label="申请单号"
                    align="center"
                    width="180"
                ></el-table-column>
                <el-table-column
                    prop="specSerial"
                    label="商品货号"
                    align="center"
                    width="160"
                ></el-table-column>
                <el-table-column
                    prop="goodsName"
                    label="商品名称"
                    width="180"
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
                                    v-if="scope.row.goodsName.length > 20"
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
                                <span
                                    v-else
                                    style="
                                        color: #4e80db;
                                        text-decoration: none;
                                        cursor: pointer;
                                    "
                                    @click="previewH5Fn(scope.row)"
                                >
                                    {{ scope.row.goodsName }}
                                </span>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="memberName"
                    label="会员名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="storeName"
                    label="商户名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    prop="createDate"
                    label="申请时间"
                    align="center"
                    width="170"
                ></el-table-column>
                <el-table-column
                    prop="refundAmount"
                    label="退款金额"
                    width="80"
                    align="right"
                >
                    <template
                        slot-scope="scope"
                        v-if="scope.row.refundAmount != null"
                        >{{
                            scope.row.aftersaleType == 1
                                ? "---"
                                : "￥" + scope.row.refundAmount
                        }}</template
                    >
                </el-table-column>
                <!-- <el-table-column
        prop="auditStatus"
        label="平台审核状态"
        v-if="changeVal==''"
        align="center"
        width="120"
        :formatter="sellerState"
      ></el-table-column> -->
                <el-table-column
                    prop="auditResult"
                    width="100"
                    label="售后状态"
                    align="center"
                    :formatter="buyerState"
                >
                </el-table-column>
                <el-table-column
                    prop="aftersaleType"
                    width="80"
                    label="售后类型"
                    align="center"
                    :formatter="saleState"
                >
                </el-table-column>
                <el-table-column label="操作" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="text"
                            @click="detShowChange(scope.row, '1')"
                            v-if="$hasPermission('sys:apply:log:view')"
                            >查看详情</el-button
                        >
                        <!-- <el-button
            size="mini"
            type="text"
            v-if="scope.row.auditStatus==2"
            @click="detShowChange(scope.row,'2')"
            style="margin-left:0;"
          >审核申请</el-button> -->
                        <!-- v-if="scope.row.auditStatus==2" -->
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
        </div>
        <afterDet
            @changeState="changeState"
            v-if="!isChange"
            :data="data"
            :detdata="detdata"
            :isBtn="isBtn"
            :isSelect="isSelect"
            :isSellerFin="isSellerFin"
            :isAdminFin="isAdminFin"
            :isLog="isLog"
            :piclist="piclist"
            :aftersale="aftersale"
            :saleGoods="saleGoods"
            :saleAuditLog="saleAuditLog"
            :saleLogs="saleLogs"
            :barinfor="barinfor"
            :adminAuditLog="adminAuditLog"
            :row="row"
        ></afterDet>
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import afterDet from "./afterDet";
    import { aftersales } from "@/api/url";
    import { exchDetail, returnDetail, salesDetail } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import modelPreviewH5 from "../../mggoods/goods/modules/model-preview-h5";
    import cloneDeep from "lodash/cloneDeep";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: aftersales,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                previewH5Visible: false,
                isChange: true, //true--列表 false--审核/退换货详情
                isExamine: true, //true--审核详情  false--退换货详情
                dataList: [],
                timeArr: "", //活动时间
                breaddata: ["订单管理", "售后管理", "申请记录"],
                status: [
                    {
                        title: "所有售后审核",
                        id: "",
                    },
                    {
                        title: "待审核",
                        id: "1",
                    },
                    {
                        title: "审核通过",
                        id: "2",
                    },
                    {
                        title: "审核拒绝",
                        id: "3",
                    },
                    {
                        title: "用户取消",
                        id: "4",
                    },
                ],
                radio1: "",
                changeVal: "",
                tableData: [],
                piclist: [], //凭证照片
                aftersale: [],
                goodsData: [], //售后商品table
                saleGoods: [], //售后申请数据
                barinfor: [], //物流
                saleAuditLog: [], //商家售后理由数据
                adminAuditLog: [], //平台售后理由数据
                saleLogs: [], //操作日志
                data: {}, //总数据
                isSelect: "", //退换货类型
                dataForm: {
                    aftersaleType: "", //售后类型
                    auditStatus: "", //平台审核状态
                    auditResult: "", //筛选状态
                    aftersaleSn: "", //售后单号
                    specSerial: "", //商品货号
                    storeName: "", //商户名称
                    memberName: "", //用户名称
                    goodsName: "", //商品名称
                    startTime: "",
                    endTime: "",
                },
                totalPage: 0,
                dataListLoading: false,
                row: "",
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                saleState(row, column) {
                    return row.aftersaleType == 0
                        ? "退货"
                        : row.aftersaleType == 1
                        ? "换货"
                        : "仅退款";
                },
                sellerState: function (row, column) {
                    return row.auditStatus == 1 ? (
                        <el-tag type="warning">未审核</el-tag>
                    ) : row.auditStatus == 2 ? (
                        <el-tag type="warning">待审核</el-tag>
                    ) : row.auditStatus == 3 ? (
                        <el-tag type="success">已完成</el-tag>
                    ) : (
                        <el-tag type="info">用户取消</el-tag>
                    );
                },
                buyerState: function (row, column) {
                    return row.auditResult == 1 ? (
                        <el-tag type="success">审核通过</el-tag>
                    ) : row.auditResult == 2 ? (
                        <el-tag type="danger">审核拒绝</el-tag>
                    ) : row.auditResult == 3 ? (
                        <el-tag type="warning">审核中</el-tag>
                    ) : row.auditResult == 4 ? (
                        <el-tag type="info">已取消</el-tag>
                    ) : (
                        <el-tag type="warning">未审核</el-tag>
                    );
                },
                tableheight: document.body.offsetHeight - 440,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
            afterDet,
            modelPreviewH5,
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
            //条件查询
            getData() {
                this.dataForm.startTime = this.timeArr && this.timeArr[0];
                this.dataForm.endTime = this.timeArr && this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //详情页展示判断
            detShowChange(index, status) {
                //status---  1-查看   2-审核
                //auditStatus =2,审核申请,有按钮有商家无平台无物流
                this.isSelect = index.aftersaleType; //0-退 1-换
                if (index.auditStatus == 2) {
                    this.isBtn = status == 2 ? true : false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = false; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["订单管理", "售后管理", "申请记录", "售后详情"];
                    this.isChange = false;
                    this.getSalesExamine(index);
                } else if (index.auditStatus == 1 || index.auditStatus == 4) {
                    //auditStatus =1||4,审核申请,无按钮无商家无平台无物流
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = false; //商家审核
                    this.isAdminFin = false; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["订单管理", "售后管理", "申请记录", "售后详情"];
                    this.isChange = false;
                    this.getSalesExamine(index);
                } else {
                    //auditStatus =3,审核申请,无按钮无商家无平台无物流
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = true; //平台审核
                    this.detdata = ["订单管理", "售后管理", "申请记录", "售后详情"];
                    this.isLog = index.auditResult == 1 ? true : false; //物流-审核通过
                    this.isChange = false;
                    this.getSalesDet(index);
                }
            },
            //查看详情
            getSalesDet(index, statue) {
                this.row = index;
                const obj = {
                    aftersaleSn: index.aftersaleSn,
                };
                //0:退货,1:换货,
                if (index.aftersaleType == 0) {
                    returnDetail(obj).then((res) => {
                        if (res.code == 200) {
                            this.data = res.data;
                            this.aftersale = res.data.aftersaleApplyDTO;
                            this.saleGoods = res.data.aftersaleGoodsDTOList;
                            this.barinfor = res.data.aftersaleReturnDTO;
                            res.data.aftersaleAuditLogDTOList.forEach(
                                (item, index) => {
                                    if (item.process == 1) {
                                        this.saleAuditLog = item; //商家审核数据
                                        if (item.result == 2) {
                                            this.isAdminFin = false;
                                        }
                                    } else if (item.process == 2) {
                                        this.adminAuditLog = item; //平台审核数据
                                    }
                                }
                            );
                            if (!this.adminAuditLog) {
                                this.adminAuditLog = "";
                            }
                            this.saleLogs = res.data.aftersaleLogListDTOList;
                            this.piclist =
                                this.aftersale.aftersalePics.indexOf(",") != -1
                                    ? this.aftersale.aftersalePics.split(",")
                                    : [this.aftersale.aftersalePics];
                            this.row.id = res.data.aftersaleApplyDTO.orderId;
                        } else {
                            this.$message({
                                type: "warning",
                                message: res.msg,
                            });
                        }
                    });
                } else {
                    exchDetail(obj).then((res) => {
                        if (res.code == 200) {
                            this.data = res.data;
                            this.aftersale = res.data.aftersaleApplyDTO;
                            this.barinfor = res.data.aftersaleBarterDTO;
                            this.saleGoods = res.data.aftersaleGoodsDTOList;
                            res.data.aftersaleAuditLogDTOList.forEach(
                                (item, index) => {
                                    if (item.process == 1) {
                                        this.saleAuditLog = item; //商家审核数据
                                    } else if (item.process == 2) {
                                        this.adminAuditLog = item; //平台审核数据
                                    }
                                }
                            );
                            if (!this.adminAuditLog) {
                                this.adminAuditLog = "";
                            }
                            this.saleLogs = res.data.aftersaleLogListDTOList;
                            this.piclist =
                                this.aftersale.aftersalePics.indexOf(",") != -1
                                    ? this.aftersale.aftersalePics.split(",")
                                    : [this.aftersale.aftersalePics];
                            this.row.id = res.data.aftersaleApplyDTO.orderId;
                        } else {
                            this.$message({
                                type: "warning",
                                message: res.msg,
                            });
                        }
                    });
                }
            },
            //审核申请
            getSalesExamine(index) {
                this.row = index;
                const obj = {
                    aftersaleSn: index.aftersaleSn,
                };
                //0:退货,1:换货,

                salesDetail(obj).then((res) => {
                    if (res.code == 200) {
                        this.detdata = [
                            "订单管理",
                            "售后管理",
                            "申请记录",
                            "售后详情",
                        ];
                        this.data = res.data;
                        this.isChange = false;
                        this.isExamine = true;
                        this.aftersale = res.data.aftersaleApplyDTO;
                        this.saleGoods = res.data.aftersaleGoodsDTOList;
                        res.data.aftersaleAuditLogDTOList.forEach((item, index) => {
                            if (item.process == 1) {
                                this.saleAuditLog = item; //商家审核数据
                            } else if (item.process == 2) {
                                this.adminAuditLog = item; //平台审核数据
                            }
                        });
                        if (!this.adminAuditLog) {
                            this.adminAuditLog = "";
                        }
                        this.saleLogs = res.data.aftersaleLogListDTOList;
                        this.piclist =
                            this.aftersale.aftersalePics.indexOf(",") != -1
                                ? this.aftersale.aftersalePics.split(",")
                                : [this.aftersale.aftersalePics];
                        this.row.id = res.data.aftersaleApplyDTO.orderId;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            },

            //重置筛选
            reset() {
                this.timeArr = [];
                this.dataForm.startTime = "";
                this.dataForm.endTime = "";
                this.dataForm.aftersaleSn = "";
                this.dataForm.specSerial = "";
                this.dataForm.storeName = "";
                this.dataForm.memberName = "";
                this.dataForm.goodsName = "";
                this.dataForm.auditResult = "";
                this.$refs["dataForm"].resetFields();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //筛选状态
            agreeChange(val) {
                this.page = 1;
                this.limit = 10;
                // this.params = {
                //   currentPage: 1, //当前页数
                //   currentPageSize: 10 //每页显示的条数
                // };
                this.changeVal = val;
                this.dataForm.auditStatus = val;
                if (this.dataForm.auditStatus == 3) {
                    this.dataForm.auditResult = "2";
                }
                this.getDataList();
            },
            //页面跳转
            changeState() {
                this.isChange = !this.isChange;
                this.getDataList(); //刷新页面数据
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcURL"] +
                        "/goodsDetails?specId=" +
                        row.specId
                );
            },
        },
        created() {},
    };
</script>

<style lang="scss" scoped>
    .el-breadcrumb {
        margin-bottom: 20px;
    }

    //.el-table--border {
    //margin-top: 20px;
    //}

    .el-radio-group {
        margin-top: 20px;
    }

    /deep/ .el-form-item {
        margin-top: 0px !important;
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
</style>