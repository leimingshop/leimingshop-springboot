<template>
    <div>
        <div>
            <div id="control-area">
                <Bread :breaddata="breaddata"></Bread>
                <!--导出按钮-->
                <importAndExport style="right: 20px;top: 12px;position: absolute"
                    :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
                    v-if="$hasPermission('sys:aftersale:barter:export')"></importAndExport>

                <el-form :inline="true" ref="dataForm" class="grayLine topGapPadding" :model="dataForm">
                    <el-form-item label="换货单号：" prop="aftersaleSn">
                        <el-input v-model="dataForm.aftersaleSn" placeholder="换货单号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商品货号：" prop="specSerial">
                        <el-input v-model="dataForm.specSerial" placeholder="商品货号" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商品名称：" prop="goodsName">
                        <el-input v-model="dataForm.goodsName" placeholder="商品名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="用户名称：" prop="memberName">
                        <el-input v-model="dataForm.memberName" placeholder="用户名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="商户名称：" prop="storeName">
                        <el-input v-model="dataForm.storeName" placeholder="商户名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="申请时间：">
                        <el-date-picker v-model="timeArr" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                            align="left" start-placeholder="开始日期" end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']"></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                        <el-button class="btn" type="primary" plain @click="reset()">重置</el-button>
                    </el-form-item>
                </el-form>
                <div class="formControlArea">
                    <el-radio-group v-model="radio1" @change="agreeChange" style="margin-bottom: 0px;margin-top:0px">
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
                        <div class="iconSize">1、仅售后申请审核通过后的换货申请才会出现在该列表数据中</div>
                    </template>
                </el-alert>
            </div>
            <el-table width="100%" :data="dataList" border="" v-loading="dataListLoading"
                style="width: 100%;" :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
                <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                    <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
                </el-table-column>
                <el-table-column prop="aftersaleSn" label="换货单号" align="center"></el-table-column>
                <el-table-column prop="storeName" width="150" label="商户名称" align="center"></el-table-column>
                <el-table-column prop="specSerial" label="商品货号" align="center"></el-table-column>
                <el-table-column prop="goodsName" label="商品名称" align="center">
                    <template slot-scope="scope">
                        <div style="display: flex;cursor: pointer;" @click="previewH5Fn(scope.row)">
                            <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                            <div class="towEllipsis" style="margin-left:8px">
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                    v-if="scope.row.goodsName.length>20" placement="top-start">
                                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;"
                                        @click="previewH5Fn(scope.row)">
                                        {{scope.row.goodsName}}
                                    </span>
                                </el-tooltip>
                                <span v-else style="color: #4e80db;text-decoration: none; cursor: pointer;"
                                    @click="previewH5Fn(scope.row)">
                                    {{scope.row.goodsName}}
                                </span>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="memberName" label="用户名" align="center"></el-table-column>
                <el-table-column prop="createDate" label="申请时间" align="center"></el-table-column>
                <el-table-column prop="aftersaleStatus" v-if="changeVal==''" label="换货状态" align="center"
                    :formatter="statusRules"></el-table-column>
                <!-- <el-table-column prop="aftersaleStatus" v-if="changeVal=='1'" label="商家审核状态" align="center"></el-table-column> -->
                <!-- <el-table-column prop="aftersaleStatus" v-if="changeVal=='1'" label="平台审核状态" align="center"></el-table-column> -->
                <el-table-column label="操作" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" @click="goChangeDetail(scope.row)"
                            v-if="$hasPermission('sys:aftersale:barter:view')">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="pageSizeChangeHandle" @current-change="pageCurrentChangeHandle"
                :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
                layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>
        <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import importAndExport from "@/components/import-and-export"
    import {
        aftersaleBarterExport
    } from '@/api/io'
    import {
        exchangegoods
    } from "@/api/url";
    // import { exchDetail } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5';
    import cloneDeep from 'lodash/cloneDeep'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: aftersaleBarterExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: exchangegoods,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id"
                },
                breaddata: ["订单管理", "售后管理", "换货管理"],
                dataForm: {
                    specSerial: "",
                    aftersaleSn: "",
                    storeName: "",
                    goodsName: "",
                    memberName: "",
                    endTime: "",
                    startTime: ""
                },
                status: [{
                        title: "所有换货订单",
                        id: ""
                    },
                    {
                        title: "待换货入库",
                        id: "8"
                    },
                    {
                        title: "换货出库中",
                        id: "9"
                    },
                    {
                        title: "换货成功",
                        id: "10"
                    },
                    {
                        title: "用户取消",
                        id: "1"
                    }
                ],
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
                detailOrList: true,
                dataListLoading: false,
                timeArr: [], //活动时间
                isExchange: false,
                radio1: "",
                changeVal: "",
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 500,
                tableheightBig:0
            }
        },
        components: {
            Bread,
            modelPreviewH5,
            importAndExport
        },
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
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
            //去退货详情页面
            goChangeDetail(row) {
                this.$emit("changePageNum", 2, row);
            },
            // //订单详情
            // handleEdit() {
            //     const obj = { id: "1195666731297792001" };
            //     orderDetail(obj).then(res => {
            //         if (res.code == 200) {
            //             this.data2 = res.data;
            //             this.addressInfo = res.data.orderAddressDTO; //收货人信息
            //             this.orderData = res.data.orderGoodsDTOList;
            //             if(res.data.orderLogisticsDTO){
            //                 this.orderLog = res.data.orderLogisticsDTO.data; //物流
            //             }else{
            //                 this.orderLog = [];
            //             }
            //             this.packageInfo = res.data.orderLogDTOList; //订单状态
            //             this.detailOrList = 2;
            //         } else {
            //             this.$message({
            //                 type: "warning",
            //                 message: res.msg
            //             });
            //         }
            //     });
            // },
            //筛选订单状态
            agreeChange(val) {
                this.page = 1;
                this.limit = 10;
                //  this.params= {
                //   currentPage: 1, //当前页数
                //   currentPageSize: 10 //每页显示的条数
                // };
                this.changeVal = val;
                this.dataForm.aftersaleStatus = val;
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
            // 预览h5
            previewH5Fn(row) {
                window.open(window.SITE_CONFIG['pcURL'] + '/goodsDetails?specId=' + row.specId);
            },
        }
    }
</script>
<style lang="scss">
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