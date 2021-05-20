<template>
    <!-- v-if="detailOrList==1" -->
    <div>
        <Bread :breaddata="breaddata? breaddata : breaddataHome"></Bread>

        <!--导出按钮-->
        <!--    <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList" v-if="$hasPermission('sys:order:export')"></importAndExport>-->

        <el-form
                :inline="true"
                ref="dataForm"
                class="grayLine topGapPadding"
                :model="dataForm"
                :rules="dataRule"
                @keyup.enter.native="getDataList()"
        >
            <el-form-item label="会员名称：" prop="buyerName">
                <el-input v-model="dataForm.buyerName" maxlength="20"  placeholder="会员名称" clearable></el-input>
            </el-form-item>
            <!-- <el-form-item label="商品编号：" prop="goodsSerial">
                <el-input v-model="dataForm.goodsSerial" placeholder="商品编号" clearable></el-input>
            </el-form-item> -->
            <el-form-item label="订单编号：" prop="orderSn">
                <el-input v-model="dataForm.orderSn" maxlength="20" placeholder="订单编号" clearable></el-input>
            </el-form-item>
            <el-form-item label="商品名称：" prop="goodsName">
                <el-input v-model="dataForm.goodsName" placeholder="商品名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="核销时间：">
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
            <el-form-item>
                <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                <el-button class="btn" type="primary" plain @click="reset('dataForm')">重置</el-button>
            </el-form-item>
            <br/>
        </el-form>
        <el-button type="primary" @click="verifyCode()">电子兑换码核销</el-button>
    <el-table
      width=" 100%"
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%;margin-top:8px;"
        :height="tableheight"
        >
        <el-table-column type="index" prop="$index" label="序号" align="center" width="50">
            <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
        </el-table-column>
        <el-table-column prop="creator" label="核销人员" align="center" width="120"></el-table-column>
        <el-table-column prop="createDate" label="核销时间" align="center" width="160"></el-table-column>
        <el-table-column prop="memberName" label="会员名称" align="center" width="120"></el-table-column>
        <el-table-column prop="memberMobile" label="会员手机" align="center" width="120"></el-table-column>
        <el-table-column prop="goodsName" label="商品名称" align="center" width="200">
             <template slot-scope="scope">
                 <div  style="display: flex;cursor: pointer;"  @click="previewH5Fn(scope.row)" >
                    <img :src="scope.row.goodsImage | filterImgUrl" width="40px" height="40px"/>
                    <div class="towEllipsis" style="margin-left: 8px;">
                        <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                          <span  style="color: #4e80db;text-decoration: none; cursor: pointer;" >{{scope.row.goodsName}}</span>
                        </el-tooltip>
                    </div>
                  </div>
           </template>
        </el-table-column>
        <el-table-column prop="orderSn" label="订单编号" align="center" width="170"></el-table-column>
        <el-table-column prop="exchangeNum" label="核销数量" align="center" width="80"></el-table-column>
        <el-table-column prop="goodsPrice" label="商品单价" align="center" width="130">
            <template slot-scope="scope">￥{{scope.row.goodsPrice}}</template>
        </el-table-column>
        <el-table-column label="操作" min-width="100" align="center">
            <template slot-scope="scope">
                <el-button size="mini" type="text" @click="changeCompent(scope.row,2)"
                           v-if="$hasPermission('sys:order:view')">查看详情
                </el-button>
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
</template>

<script>
    import Bread from "@/components/bread";
    import {getVerifyRecordPage} from "@/api/url";

    import mixinViewModule from "@/mixins/view-module";

    export default {
        mixins: [mixinViewModule],
        data() {
            //订单编号校验
            var validateOrderSn = (rule, value, callback) => {
                if (value) {
                    var myReg = /[\u4E00-\u9FA5]/;
                    if (myReg.test(value)) {
                        return callback(new Error("订单编号不能含有中文"));
                    }
                }
                callback();
            };
            //物流单号
            var transport = (rule, value, callback) => {
                if (value == "") {
                    callback(new Error("物流单号不得为空"));
                } else if (value.length > 15) {
                    callback(new Error("物流单号最长限制15位"));
                } else {
                    callback();
                }
            };
            return {
                mixinViewModuleOptions: {
                    getDataListURL: getVerifyRecordPage,
                    getDataListIsPage: true,
                    exportURL: "",
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id"
                },

                breaddataHome: ["订单系统", "订单管理", "核销记录列表"],
                dataListLoading: false,
                radio1: "",
                tableData: [],
                orderData: [],
                dataForm: {
                    orderSn: "",
                    // storeIdAndName: "",
                    buyerName: "",
                    paymentId: "",
                    paymentStatus: "",
                    startTime: "",
                    endTime: "",
                },
                tableData: [],
                timeArr: "", //核销时间数据
                addressInfo: [], //地址数据
                packageInfo: [], //包裹数据
                orderLog: [], //操作日志
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10 //每页显示的条数
                },
                dataRule: {
                    orderSn: [{validator: validateOrderSn, trigger: "blur"}]
                },
                tableheight: document.body.offsetHeight - 470,
            };
        },
        computed: {},
        components: {
            Bread,
        },
        props: ["status", "breaddata"],
        created() {
            //处理不同状态
            this.isShow = this.status !== undefined ? false : true;
            this.radio1 = this.status == undefined ? "" : this.status;
            this.dataForm.orderStatus = this.status == undefined ? "" : this.status;
        },
        methods: {
            getData() {

                if (this.timeArr && this.timeArr.length == 2) {
                    this.dataForm.startTime = this.timeArr[0];
                    this.dataForm.endTime = this.timeArr[1];
                } else {
                    this.dataForm.startTime = ""
                    this.dataForm.endTime = ""
                }
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset(dataForm) {
                this.dataForm.orderSn = "";
                this.dataForm.buyerName = "";
                this.dataForm.paymentId = "";
                this.dataForm.paymentStatus = "";
                this.dataForm.storeIdOrName = "";
                this.timeArr = [];
                this.dataForm.startTime = "";
                this.dataForm.endtime = "";
                this.$refs['dataForm'].resetFields();
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            previewH5Fn(row){
                window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?goodsId='+row.id+'&specId='+row.specId);
            },
            verifyCode(row) {
                this.$emit("showGroupDetail");
            },
            changeCompent(row, index) {
                this.$emit("changeCompent", row, index);
            },

        }
    };
</script>
<style lang="scss" scoped>
    .sendModal {
        /deep/ .el-form-item__content {
            display: block !important;
        }
    }

    .el-popper {
        left: 292px !important;
    }

    .el-table--border {
        margin-top: 20px;
    }

    .el-radio-group {
        margin-top: 20px;
    }

    .orderUerInfo {
        width: 100%;
        height: auto;
        margin-top: 20px;
        border: 1px solid #d1d1d1;
    }

    .orderDe {
        /* border: 1px solid #333; */
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .orderDelf,
    .orderDerg {
        /* display: flex; */
        display: flex;
        align-items: center;
    }

    .orderDelf div {
        margin-left: 20px;
    }

    .orderDerg div {
        margin-right: 20px;
    }

    .buyerInfo {
        box-sizing: border-box;
        border-radius: 5px;
        padding: 0px 8px;
        height: 70px;
        margin: 5px 10px;
        border: 1px solid #d1d1d1;
        display: flex;
    }

    .buyerInfo ul {
        width: 50%;
        padding: inherit;
    }

    .buyerInfo ul li {
        list-style-type: none;
        padding: 3px;
    }

    .orderRecord {
        widows: 100%;
        height: auto;
        height: 200px;
        display: flex;
        margin-top: 10px;
        border: 1px solid #d1d1d1;
    }

    .orderText {
        width: 80%;
        height: 100%;
        display: flex;
        padding: 10px;
        justify-content: center;
    }

    .orderInfo {
        width: 20%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: 100%;
    }

    .el-textarea {
        width: 100%;
    }

    .el-textarea__inner {
        height: 100%;
        resize: none;
    }

    .orderConfig {
        width: 100%;
        border: 1px solid #d1d1d1;
        height: auto;
        display: flex;
        justify-content: flex-start;
        margin-top: 10px;
    }

    .orderConList {
        width: 25%;
        padding: 20px 0 0 30px;
        border-right: 1px solid #d1d1d1;
        text-align: left;
        height: auto;
    }

    .summary {
        width: 100%;
        display: flex;
        margin-top: 20px;
        justify-content: flex-end;
    }

    .summary ul li {
        list-style: none;
        width: 200px;
        line-height: 30px;
    }

    .operationRecord {
        width: 100%;
        padding-left: 20px;
        margin: 20px 0;
        height: auto;
        border: 1px solid #d1d1d1;
    }

    .operationRecord p {
        line-height: 30px;
    }

    .order {
        display: flex;
    }

    .order p {
        margin-left: 20px;
    }

    .ssss {
        display: flex;
        height: auto;
        align-items: center;
    }

    /deep/ .el-form-item {
        margin-top: 0px !important;
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
