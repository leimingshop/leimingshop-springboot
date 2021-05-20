<template>
    <div>
        <div v-show="isChange">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>

                <!--导出按钮-->
                <importAndExport
                    style="right: 20px; top: 12px; position: absolute"
                    :importAndExportOptions="importAndExportOptions"
                    :dataForm="dataForm"
                    @getDataList="getDataList"
                    v-if="$hasPermission('sys:aftersale:audit:export')"
                ></importAndExport>

                <el-form
                    :inline="true"
                    ref="dataForm"
                    class="grayLine topGapPadding"
                    :model="dataForm"
                >
                    <el-form-item label="申请单号：">
                        <el-input
                            v-model="dataForm.aftersaleSn"
                            placeholder="申请单号"
                            clearable
                        ></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="商品货号：">
        <el-input v-model="dataForm.specSerial" placeholder="商品货号" rable></el-input>
      </el-form-item> -->
                    <!--<el-form-item label="商户名称：" prop="storeName">-->
                    <!--<el-input v-model="dataForm.storeName" placeholder="商户名称" clearable></el-input>-->
                    <!--</el-form-item>-->
                    <el-form-item label="会员名称：" prop="memberName">
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

                    <el-form-item label="审核时间：">
                        <el-date-picker
                            v-model="timeArr2"
                            type="datetimerange"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            align="left"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']"
                        ></el-date-picker>
                    </el-form-item>
                    <!-- <el-form-item label="售后状态：">
        <el-select v-model="dataForm.auditResult" placeholder="售后状态">
          <el-option label="全部" value=""></el-option>
          <el-option label="审核中" value="3">审核中</el-option>
          <el-option label="审核通过" value="1">审核通过</el-option>
          <el-option label="审核拒绝" value="2">审核拒绝</el-option>
          <el-option label="用户取消" value="4">用户取消</el-option>
        </el-select>
      </el-form-item> -->
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
                    <div>
                        <el-radio-group
                            v-model="dataForm.result"
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
                            1、该页面仅展示需要商家用户进行售后审核的数据
                        </div>
                        <div class="iconSize">
                            2、售后审核状态为待审核、审核通过、审核拒绝、用户取消四种状态
                        </div>
                        <div class="iconSize">
                            3、待审核状态的售后单可审核该申请并填写审核理由
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
                    width="60"
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
                    width="170"
                ></el-table-column>
                <!-- <el-table-column prop="specSerial" label="商品货号" align="center" width="160"></el-table-column> -->
                <el-table-column
                    prop="goodsName"
                    label="商品名称"
                    align="center"
                    min-width="220"
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
                    width="110"
                ></el-table-column>
                <!--<el-table-column prop="storeName" label="商户名称" align="center"></el-table-column>-->
                <el-table-column
                    prop="createDate"
                    label="申请时间"
                    align="center"
                    width="160"
                ></el-table-column>
                <el-table-column
                    prop="auditDate"
                    label="审核时间"
                    align="center"
                    width="160"
                ></el-table-column>
                <el-table-column
                    prop="refundAmount"
                    label="退款金额"
                    align="right"
                >
                    <template
                        slot-scope="scope"
                        v-if="scope.row.refundAmount != null"
                    >
                        {{
                            scope.row.aftersaleType == 1
                                ? "---"
                                : "￥" + scope.row.refundAmount
                        }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="auditStatus"
                    label="审核状态"
                    v-if="changeVal == ''"
                    align="center"
                    width="100"
                >
                    <template slot-scope="scope">
                        <el-tag type="danger" v-if="scope.row.result == 2"
                            >审核拒绝</el-tag
                        >
                        <el-tag type="warning" v-if="scope.row.result == 3"
                            >待审核</el-tag
                        >
                        <el-tag type="success" v-if="scope.row.result == 1"
                            >审核通过</el-tag
                        >
                        <el-tag type="info" v-if="scope.row.result == 4"
                            >用户取消</el-tag
                        >
                    </template>
                </el-table-column>

                <el-table-column
                    prop="aftersaleType"
                    width="80"
                    label="申请类型"
                    align="center"
                >
                    <template slot-scope="scope">
                        <span v-if="scope.row.aftersaleType == 0">退货</span>
                        <span v-else-if="scope.row.aftersaleType == 1"
                            >换货</span
                        >
                        <span v-if="scope.row.aftersaleType == 2">仅退款</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" min-width="150" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="text"
                            @click="detShowChange(scope.row, '1')"
                            v-if="$hasPermission('sys:aftersale:audit:view')"
                            >查看详情</el-button
                        >
                        <el-button
                            size="mini"
                            type="text"
                            v-if="
                                scope.row.result == 3 &&
                                $hasPermission('sys:aftersale:apply')
                            "
                            @click="detShowChange(scope.row, '2')"
                            style="margin-left: 10px"
                            >审核申请</el-button
                        >
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
        <!-- 预览h5 -->
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import afterDet from "./afterDet";
    import { aftersaleAuditPage } from "@/api/url";
    import { exchDetail, returnDetail, salesAuditDetail } from "@/api/api";
    import modelPreviewH5 from "../../mggoods/modules/model-preview-h5.vue";
    import mixinViewModule from "@/mixins/view-module";
    import importAndExport from "@/components/import-and-export";
    import { aftersaleAuditExport } from "@/api/io";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                previewH5Visible: false,
                /*导出*/
                importAndExportOptions: {
                    exportUrl: aftersaleAuditExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: aftersaleAuditPage,
                    getDataListIsPage: true,
                    activatedIsNeed: false,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                isChange: true, //true--列表 false--审核/退换货详情
                isExamine: true, //true--审核详情  false--退换货详情
                dataList: [],
                timeArr: "", //活动时间
                breaddata: ["售后管理", "售后审核"],
                status: [
                    { title: "所有售后审核", id: "" },
                    { title: "待审核", id: "3" },
                    { title: "审核通过", id: "1" },
                    { title: "审核拒绝", id: "2" },
                    { title: "用户取消", id: "4" },
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
                    // aftersaleType: "", //售后类型
                    // auditStatus: "", //平台审核状态
                    // auditResult: "", //筛选状态
                    // aftersaleSn: "", //售后单号
                    // specSerial: "", //商品货号
                    // storeName: "", //商户名称
                    // memberName: "", //用户名称
                    // goodsName: "", //商品名称
                    // startTime: "",
                    // endTime: "",
                    id: "", //申请单号
                    specSerial: "", //商品货号
                    goodsName: "", //	商品名称
                    memberName: "", //会员名称
                    storeName: "", //	商户名称
                    aftersaleType: "", //	售后方式（0:退货,1:换货,2:仅退款）
                    startTime: "", //申请开始时间
                    endTime: "", //申请结束时间
                    auditStartTime: "", //审核开始时间
                    auditEndTime: "", //审核结束时间
                    result: "", //	审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消
                    process: 1, //审核流程（1:商家审核,2:平台审核）
                },
                timeArr: [],
                timeArr2: [],
                totalPage: 0,
                dataListLoading: false,
                row: "",
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
                tableheight: document.body.offsetHeight - 470,
                tableheightBig: 300,
            };
        },
        components: { Bread, afterDet, modelPreviewH5, importAndExport },
        activated() {
            if (
                this.$route.query.aftersaleType == 0 ||
                this.$route.query.aftersaleType == 1 ||
                this.$route.query.aftersaleType == 2
            ) {
                // 0退货，1换货，1仅退款，
                this.dataForm.aftersaleType = this.$route.query.aftersaleType + "";
            }
            if (this.$route.query.result) {
                // 3待审核,1审核通过,2审核拒绝,4用户取消
                this.dataForm.result = this.$route.query.result + "";
            }
            this.getDataList();
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
                this.changeTime();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?specId=" +
                        row.specId
                );
            },
            // 根据数组判断时间
            changeTime() {
                if (this.timeArr && this.timeArr.length == 2) {
                    this.dataForm.startTime = this.timeArr && this.timeArr[0];
                    this.dataForm.endTime = this.timeArr && this.timeArr[1];
                } else {
                    this.dataForm.startTime = "";
                    this.dataForm.endTime = "";
                }
                if (this.timeArr2 && this.timeArr2.length == 2) {
                    this.dataForm.auditStartTime =
                        this.timeArr2 && this.timeArr2[0];
                    this.dataForm.auditEndTime = this.timeArr2 && this.timeArr2[1];
                } else {
                    this.dataForm.auditStartTime = "";
                    this.dataForm.auditEndTime = "";
                }
            },
            //详情页展示判断
            detShowChange(index, status) {
                //status 1-详情 2-申请   select
                //auditStatus =2,审核申请,有按钮有商家无平台无物流
                this.isSelect = index.aftersaleType; //0-退 1-换
                // 审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消,5:仲裁审核）
                if (index.auditStatus == 2) {
                    // 2:平台审核中
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = false; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["售后管理", "售后审核", "审核申请"];
                    this.isChange = false;
                    this.getSalesDet(index);
                } else if (index.auditStatus == 1) {
                    //auditStatus =1||4,商家审核中和用户已取消无按钮无商家无平台无物流
                    this.isBtn = status == 2 ? true : false; //操作按钮
                    this.isSellerFin = false; //商家审核
                    this.isAdminFin = false; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["售后管理", "售后审核", "售后详情"];
                    this.isChange = false;
                    this.getSalesDet(index);
                } else if (index.auditStatus == 4) {
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = false; //商家审核
                    this.isAdminFin = false; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["售后管理", "售后审核", "售后详情"];
                    this.isChange = false;
                    this.getSalesDet(index);
                } else if (index.auditStatus == 3) {
                    //auditStatus =3,审核申请,无按钮无商家无平台无物流
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = true; //平台审核
                    this.detdata = ["售后管理", "售后审核", "售后详情"];
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
                                        if (this.saleAuditLog.result == 2) {
                                            this.isAdminFin = false;
                                        }
                                    } else if (item.process == 2) {
                                        this.adminAuditLog = item; //平台审核数据
                                    }
                                }
                            );
                            this.saleLogs = res.data.aftersaleLogListDTOList;
                            this.piclist = this.aftersale.aftersalePics.split(",");
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
                            this.saleLogs = res.data.aftersaleLogListDTOList;
                            this.piclist = this.aftersale.aftersalePics.split(",");
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

                salesAuditDetail(obj).then((res) => {
                    if (res.code == 200) {
                        this.detdata = [
                            "订单管理",
                            "售后管理",
                            "售后审核",
                            "审核详情",
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
                        this.saleLogs = res.data.aftersaleLogListDTOList;
                        this.piclist =
                            this.aftersale.aftersalePics.indexOf(",") != -1
                                ? this.aftersale.aftersalePics.split(",")
                                : [];
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
                this.timeArr2 = [];
                this.$refs["dataForm"].resetFields();
                this.dataForm.id = "";
                this.dataForm.specSerial = "";
                this.dataForm.goodsName = "";
                this.dataForm.memberName = "";
                this.dataForm.storeName = "";
                this.dataForm.aftersaleType = "";
                this.dataForm.aftersaleSn = "";
                this.getData();
            },
            //筛选状态
            agreeChange(val) {
                this.page = 1;
                this.limit = 10;
                // this.params = {
                //   currentPage: 1, //当前页数
                //   currentPageSize: 10 //每页显示的条数
                // };
                // this.changeVal = val;
                // this.dataForm.auditStatus = val;
                // if (this.dataForm.auditStatus == 3) {
                //   this.dataForm.auditResult = "2";
                // }
                this.getDataList();
            },
            //页面跳转
            changeState() {
                this.isChange = !this.isChange;
                this.getDataList(); //刷新页面数据
            },
        },
        created() {},
    };
</script>

<style scoped>
    .el-breadcrumb {
        margin-bottom: 20px;
    }
    .el-table--border {
        margin-top: 20px;
    }
    .el-radio-group {
        margin-top: 20px;
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

    /deep/ .el-form-item {
        margin-top: 0px !important;
    }
</style>
