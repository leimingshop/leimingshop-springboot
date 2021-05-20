<template>
    <div>
        <div v-if="isChange">
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>

                <!--导出按钮-->
                <importAndExport style="right: 20px;top: 12px;position: absolute"
                    :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
                    v-if="$hasPermission('sys:aftersale:audit:export')"></importAndExport>

                <el-form :inline="true" ref="dataForm" class="grayLine topGapPadding" :model="dataForm" label-width="100px">
                    <el-form-item label="会员账号：" prop="memberName">
                        <el-input v-model="dataForm.memberName" placeholder="会员账号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="原申请单号：" class="RequisitioNumber">
                        <el-input v-model="dataForm.afterSn" placeholder="申请单号"></el-input>

                    </el-form-item>
                    <el-form-item label="仲裁类型：" prop="arbitrationType">
                        <el-select v-model="dataForm.arbitrationType" placeholder="申请仲裁类型">
                            <el-option label="全部" value=""></el-option>
                            <el-option label="仲裁-退货" value="0">仲裁-退货</el-option>
                            <el-option label="仲裁-换货" value="1">仲裁-换货</el-option>
                            <!-- <el-option label="仅退款" value="2">仅退款</el-option> -->
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="商品货号：">
            <el-input v-model="dataForm.specSerial" placeholder="商品货号" rable></el-input>
        </el-form-item> -->

                    <el-form-item label="商品名称：" prop="goodsName">
                        <el-input v-model="dataForm.goodsName" placeholder="商品名称" clearable></el-input>
                    </el-form-item>

                    <!-- <el-form-item label="商户名称：" prop="goodsName">
            <el-input v-model="dataForm.storeName" placeholder="商户名称" clearable></el-input>
        </el-form-item> -->

                    <el-form-item label="申请时间：">
                        <el-date-picker v-model="timeArr" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                            align="left" start-placeholder="开始日期" end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']">
                        </el-date-picker>
                    </el-form-item>

                    <el-form-item label="仲裁审核时间：">
                        <el-date-picker v-model="timeArr2" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                            align="left" start-placeholder="开始日期" end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="审核人：" prop="auditor">
                        <el-input v-model="dataForm.auditor" placeholder="审核人" clearable></el-input>
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
                        <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" plain @click="reset('dataForm')">重置</el-button>
                    </el-form-item>
                </el-form>
                <div class="formControlArea">
                    <el-radio-group v-model="dataForm.arbitrationStatus" @change="agreeChange"
                        style="margin-top: 0px;margin-bottom: 0px">
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
                        <div class="iconSize">1、仲裁记录状态为：待审核、审核通过、审核拒绝</div>
                        <div class="iconSize">2、查看仲裁详情可查看各端操作日志</div>
                        <div class="iconSize">3、待审核的仲裁记录审核理由为必填项</div>
                    </template>
                </el-alert>
            </div>

            <el-table width="100%" :data="dataList" border="" v-loading="dataListLoading" :height="!$store.state.mainSwitch ? tableheight:tableheightBig" style="width: 100%,maigin-top:20px;">               
                <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                    <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
                </el-table-column>
                <el-table-column prop="aftersaleSn" label="原申请单号" align="center" width="180"></el-table-column>
                <!-- <el-table-column prop="specSerial" label="商品货号" align="center" width="160"></el-table-column> -->
                <el-table-column prop="goodsName" label="商品名称" align="center" min-width="220">
                    <!-- <template slot-scope="scope" style="text-align: left;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;" :title="scope.row.goodsName"> -->
                    <!-- <span>{{scope.row.goodsName.replace(/ /g,"&nbsp;")}}</span> -->
                    <!-- <span style="color: #4e80db;text-decoration: none; cursor: pointer;" >{{scope.row.goodsName.replace(/ /g,"&nbsp;")}}</span>
        </template> -->
                    <template slot-scope="scope">
                        <div style="display: flex;cursor: pointer;" @click="previewH5Fn(scope.row)">
                            <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                            <div class="towEllipsis" style="margin-left:8px">
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                    v-if="scope.row.goodsName.length>20" placement="top-start">
                                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;">
                                        {{scope.row.goodsName}}
                                    </span>
                                </el-tooltip>
                                <span v-else style="color: #4e80db;text-decoration: none; cursor: pointer;">
                                    {{scope.row.goodsName}}
                                </span>
                            </div>
                        </div>
                    </template>

                </el-table-column>
                <el-table-column prop="memberName" label="会员账号" align="center"></el-table-column>
                <el-table-column prop="arbitrationType" width="100" label="仲裁类型" align="center">
                    <template slot-scope="scope">
                        <span v-if="scope.row.arbitrationType==0">仲裁-退货</span>
                        <span v-else-if="scope.row.arbitrationType==1">仲裁-换货</span>
                        <!-- <span v-if="scope.row.arbitrationType==2">仅退款</span> -->
                    </template>
                </el-table-column>
                <el-table-column prop="arbitrationApplyDate" label="申请仲裁时间" align="center" width="170">
                </el-table-column>

                <el-table-column prop="auditor" label="审核人" align="center"></el-table-column>
                <el-table-column prop="arbitrationAuditDate" label="审核仲裁时间" align="center" width="170">
                </el-table-column>
                <!-- <el-table-column prop="refundAmount" label="退款金额" align="right">
          <template slot-scope="scope" v-if=" scope.row.refundAmount!=null">
              {{scope.row.aftersaleType==1?"---":'￥'+scope.row.refundAmount}}
          </template>
      </el-table-column> -->
                <!-- <el-table-column prop="storeName" label="商户名称" align="center"></el-table-column> -->

                <el-table-column prop="arbitrationStatus" label="仲裁审核状态" v-if="changeVal==''" align="center"
                    width="120">
                    <!-- 仲裁状态（默认：1已通过、2：已拒绝、3：待审核） -->
                    <template slot-scope="scope">
                        <el-tag type="danger" v-if="scope.row.arbitrationStatus==2">审核拒绝</el-tag>
                        <el-tag type="success" v-if="scope.row.arbitrationStatus==1">审核通过</el-tag>
                        <el-tag type="warning" v-if="scope.row.arbitrationStatus==3">待审核</el-tag>
                        <!-- <el-tag type="info"  v-if="scope.row.result==4">用户取消</el-tag> -->
                    </template>
                </el-table-column>


                <el-table-column label="操作" min-width="150" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" @click="detShowChange(scope.row,'1')"
                            v-if="(scope.row.arbitrationStatus==2||scope.row.arbitrationStatus==1) && $hasPermission('sys:aftersaleaudit:view')">
                            查看详情
                        </el-button>

                        <el-button size="mini" type="text"
                            v-if="scope.row.arbitrationStatus==3 && $hasPermission('sys:aftersaleaudit:apply')"
                            @click="detShowChange(scope.row,'2')" style="margin-left:10px;">
                            审核
                        </el-button>
                        <!-- v-if="scope.row.auditStatus==2" -->
                    </template>
                </el-table-column>

            </el-table>
            <el-pagination @size-change="pageSizeChangeHandle" @current-change="pageCurrentChangeHandle"
                :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>

            <!-- <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5> -->
        </div>
        <arbitrationDet @changeState="changeState" v-else :data="data" :detdata="detdata" :isBtn="isBtn"
            :isSelect="isSelect" :isSellerFin="isSellerFin" :isAdminFin="isAdminFin" :isLog="isLog" :piclist="piclist"
            :imagesList="imagesList" :aftersale="aftersale" :saleGoods="saleGoods" :saleAuditLog="saleAuditLog"
            :saleLogs="saleLogs" :barinfor="barinfor" :adminAuditLog="adminAuditLog" :arbitrationLog="arbitrationLog"
            :row="row"></arbitrationDet>
    </div>

</template>

<script>
    import importAndExport from "@/components/import-and-export"
    import {
        arbitrationRecordExport
    } from '@/api/io'
    import Bread from "@/components/bread";
    import arbitrationDet from "./arbitrationDet";
    import {
        arbitrationPage
    } from "@/api/url";
    import {
        exchDetail,
        returnDetail,
        arbitrationDetail
    } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5'
    import cloneDeep from 'lodash/cloneDeep'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: arbitrationRecordExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: arbitrationPage,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id"
                },
                isChange: true, //true--列表 false--审核/退换货详情
                isExamine: true, //true--审核详情  false--退换货详情

                timeArr: "", //活动时间
                breaddata: ["订单管理", "售后管理", "仲裁记录"],
                status: [{
                        title: "所有售后审核",
                        id: ""
                    },
                    {
                        title: "待审核",
                        id: "3"
                    },
                    {
                        title: "审核通过",
                        id: "1"
                    },
                    {
                        title: "审核拒绝",
                        id: "2"
                    },
                    // { title: "用户取消", id: "4" }
                ],
                radio1: "",
                changeVal: "",
                tableData: [],
                piclist: [], //凭证照片
                imagesList: [], //仲裁图片
                aftersale: [],
                goodsData: [], //售后商品table
                saleGoods: [], //售后申请数据
                barinfor: [], //物流
                saleAuditLog: [], //商家售后理由数据
                adminAuditLog: [], //平台售后理由数据
                arbitrationLog: [],
                saleLogs: [], //操作日志
                data: {}, //总数据
                isSelect: '', //退换货类型
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
                    id: '', //申请单号
                    specSerial: '', //商品货号
                    goodsName: '', //	商品名称
                    memberName: '', //会员名称
                    storeName: '', //	商户名称
                    aftersaleType: '', //	售后方式（0:退货,1:换货,2:仅退款）
                    startTime: "", //申请开始时间
                    endTime: '', //申请结束时间
                    auditStartTime: "", //审核开始时间
                    auditEndTime: '', //审核结束时间
                    result: "", //	审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消
                    process: 2, //审核流程（1:商家审核,2:平台审核）
                    arbitrationStatus: ""
                },
                timeArr: [],
                timeArr2: [],
                totalPage: 0,
                dataListLoading: false,
                row: '',
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10 //每页显示的条数
                },
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 480,
                tableheightBig:0
            };
        },
        components: {
            Bread,
            arbitrationDet,
            modelPreviewH5,
            importAndExport
        },
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        115;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
        },
        methods: {
            //条件查询
            getData() {
                this.changeTime();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 根据数组判断时间
            changeTime() {
                if (this.timeArr && this.timeArr.length == 2) {
                    this.dataForm.applyStartDate = this.timeArr && this.timeArr[0];
                    this.dataForm.applyEndDate = this.timeArr && this.timeArr[1];
                } else {
                    this.dataForm.applyStartDate = ""
                    this.dataForm.applyEndDate = ""
                }
                if (this.timeArr2 && this.timeArr2.length == 2) {
                    this.dataForm.auditStartDate = this.timeArr2 && this.timeArr2[0];
                    this.dataForm.auditEndDate = this.timeArr2 && this.timeArr2[1];
                } else {
                    this.dataForm.auditStartDate = ""
                    this.dataForm.auditEndDate = ""
                }
            },
            //详情页展示判断
            detShowChange(index, status) {
                //status---  1-审核   2-查看
                //auditStatus =2,审核申请,有按钮有商家无平台无物流
                this.isSelect = index.arbitrationType; //0-退 1-换
                if (index.arbitrationStatus == 3) {
                    this.isBtn = status == 2 ? true : false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = true; //平台审核
                    this.isLog = false; //物流
                    this.detdata = ["订单管理", "售后管理", "仲裁记录", "审核申请"];
                    this.isChange = false;
                    this.getSalesExamine(index);
                } else {
                    //auditStatus =3,审核申请,无按钮无商家无平台无物流
                    this.isBtn = false; //操作按钮
                    this.isSellerFin = true; //商家审核
                    this.isAdminFin = true; //平台审核
                    this.detdata = ["订单管理", "售后管理", "仲裁记录", "售后详情"];
                    this.isLog = index.arbitrationStatus == 1 ? true : false; //物流-审核通过
                    this.isChange = false;
                    this.getSalesExamine(index);
                }
            },
            //审核申请
            getSalesExamine(index) {
                this.row = index
                const obj = {
                    id: index.id
                };
                //0:退货,1:换货,

                arbitrationDetail(obj).then(res => {
                    if (res.code == 200) {
                        this.detdata = ["订单管理", "售后管理", "仲裁记录", "审核详情"];
                        this.data = res.data;
                        this.isChange = false;
                        this.isExamine = true;
                        //退货
                        if (res.data.arbitrationType == 0) {
                            this.aftersale = res.data.returnDetailDTO.aftersaleApplyDTO;
                            this.saleGoods = res.data.returnDetailDTO.aftersaleGoodsDTOList;
                            this.barinfor = res.data.returnDetailDTO.aftersaleReturnDTO;
                            res.data.returnDetailDTO.aftersaleAuditLogDTOList.forEach((item, index) => {
                                if (item.process == 1) {
                                    this.saleAuditLog = item; //商家审核数据
                                } else if (item.process == 2) {
                                    this.adminAuditLog = item; //平台审核数据
                                } else if (item.process == 3) {
                                    this.arbitrationLog = item;
                                }
                            })
                            this.saleLogs = res.data.returnDetailDTO.aftersaleLogListDTOList;
                            this.piclist = this.aftersale.aftersalePics.indexOf(",") != -1 ? this.aftersale
                                .aftersalePics.split(",") : [this.aftersale.aftersalePics];
                            this.imagesList = res.data.imagesArr && res.data.imagesArr.indexOf(",") != -1 ? res
                                .data.imagesArr.split(",") : [res.data.imagesArr];
                            this.row.id = res.data.returnDetailDTO.aftersaleApplyDTO.orderId;
                        } else {
                            this.aftersale = res.data.barterDetailDTO.aftersaleApplyDTO;
                            this.saleGoods = res.data.barterDetailDTO.aftersaleGoodsDTOList;
                            this.barinfor = res.data.barterDetailDTO.aftersaleBarterDTO;
                            res.data.barterDetailDTO.aftersaleAuditLogDTOList.forEach((item, index) => {
                                if (item.process == 1) {
                                    this.saleAuditLog = item; //商家审核数据
                                } else if (item.process == 2) {
                                    this.adminAuditLog = item; //平台审核数据
                                } else if (item.process == 3) {
                                    this.arbitrationLog = item;
                                }
                            })
                            this.saleLogs = res.data.barterDetailDTO.aftersaleLogListDTOList;
                            this.piclist = this.aftersale.aftersalePics.indexOf(",") != -1 ? this.aftersale
                                .aftersalePics.split(",") : [this.aftersale.aftersalePics];
                            this.imagesList = res.data.imagesArr && res.data.imagesArr.indexOf(",") != -1 ? res
                                .data.imagesArr.split(",") : [res.data.imagesArr];
                            this.row.id = res.data.barterDetailDTO.aftersaleApplyDTO.orderId;
                        }

                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
            },

            //重置筛选
            reset() {
                this.timeArr = [];
                this.timeArr2 = [];
                this.$refs["dataForm"].resetFields();
                this.dataForm.id = ""
                this.dataForm.specSerial = ""
                this.dataForm.goodsName = ""
                this.dataForm.memberName = ""
                this.dataForm.arbitrationType = ""
                this.dataForm.afterSn = ""
                this.dataForm.auditor = ""
                this.getData();
            },
            //筛选状态
            agreeChange(val) {
                this.page = 1;
                this.limit = 10;

                this.getDataList();
            },
            //页面跳转
            changeState() {
                this.isChange = !this.isChange;
                this.getDataList(); //刷新页面数据
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(window.SITE_CONFIG['pcURL'] + '/goodsDetails?specId=' + row.specId);
            }
        },
        created() {}
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

    /deep/ .el-form-item {
        margin-top: 0px !important;
    }

    .RequisitioNumber>>>.el-input__inner {
        width: 205px;
        text-align: left;
        margin-right: 60px;

    }
</style>