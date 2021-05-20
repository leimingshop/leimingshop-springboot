<template>
    <div class="aftersalelog_afterDetClass">
        <div id="afterDetGoodsDetail1" v-if="isSaleList">
            <Bread
                :breaddata="detdata"
                @changePage="changePage"
                :index="'1'"
            ></Bread>
            <!-- 分割线------------------------------------------------ -->
            <div class="orderState">
                <div>
                    <label
                        >当前{{
                            isSelect == 0 ? "退" : "换"
                        }}货审核状态：</label
                    >
                    <span
                        v-if="aftersale.auditStatus != 3"
                        style="color: #e6a23c"
                        >{{
                            aftersale.auditStatus == 1
                                ? "商家审核中"
                                : aftersale.auditStatus == 2
                                ? "平台审核中"
                                : aftersale.auditStatus == 4
                                ? "已取消"
                                : "未审核"
                        }}</span
                    >
                    <span v-if="aftersale.auditStatus == 3">
                        <span
                            v-if="aftersale.auditResult == 2"
                            style="color: #f56c6c"
                        >
                            {{ "审核拒绝" }}
                        </span>
                        <span
                            v-if="aftersale.auditResult == 1"
                            style="color: #01bd25"
                        >
                            {{ "审核通过" }}
                        </span>
                    </span>

                    <!--<span>{{barinfor.aftersaleStatus==1?'用户取消':barinfor.aftersaleStatus==2?'退款失败':barinfor.aftersaleStatus==3?'待退货入库':barinfor.aftersaleStatus==4?'退款中':barinfor.aftersaleStatus==5?'退款成功':barinfor.aftersaleStatus==6?'换货失败':barinfor.aftersaleStatus==7?'待换货入库':barinfor.aftersaleStatus==8?'换货出库中':'换货成功'}}</span>-->
                </div>
                <div>
                    <label>{{ isSelect == 0 ? "退" : "换" }}货单号：</label>
                    <span>{{ aftersale.aftersaleSn }}</span>
                </div>
                <div>
                    <label>订单编号：</label>
                    <span>{{ aftersale.orderSn }}</span>
                </div>
            </div>
            <!-- 分割线------------------------------------------------ -->
            <div class="opctionWarp">
                <div class="opctionItem2">
                    <h3>买家{{ isSelect == 0 ? "退" : "换" }}货申请</h3>
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

                            <el-form-item
                                :label="
                                    isSelect == 0 ? '退货原因：' : '换货原因：'
                                "
                            >
                                <span>{{ aftersale.aftersaleReason }}</span>
                            </el-form-item>

                            <el-form-item
                                :label="
                                    isSelect == 0 ? '退货说明：' : '换货说明：'
                                "
                            >
                                <span
                                    style="
                                        word-wrap: break-word;
                                        word-break: normal;
                                    "
                                    >{{ aftersale.aftersaleExplain }}</span
                                >
                            </el-form-item>
                            <el-form-item
                                :label="
                                    isSelect == 0 ? '退货凭证：' : '换货凭证：'
                                "
                                class="pingzheng"
                                style="
                                    display: flex;
                                    justify-content: flex-start;
                                    white-space: nowrap;
                                "
                            >
                                <div
                                    v-for="(item, index) in piclist"
                                    style="margin-left: 1px"
                                >
                                    <img
                                        id="oImg"
                                        :src="item | filterImgUrl"
                                        alt=""
                                        style="
                                            height: 60px;
                                            width: 60px;
                                            object-fit: scale-down;
                                        "
                                        @click="previewBigImg(item, index)"
                                    />
                                </div>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>
                <!-- 分割线------------------------------------------------ -->
                <div class="opctionItem2" v-if="isSellerFin">
                    <h3>商家{{ isSelect == 0 ? "退" : "换" }}货审核</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="商家名称：">
                                <span>{{ aftersale.storeName }}</span>
                            </el-form-item>
                            <el-form-item label="审核时间：">
                                <span>{{ saleAuditLog.updateDate }}</span>
                            </el-form-item>

                            <el-form-item label="审核理由：">
                                <span>{{ saleAuditLog.reason }}</span>
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
                <!-- 分割线------------------------------------------------ -->
                <div class="opctionItem2" v-if="isAdminFin">
                    <h3>平台{{ isSelect == 0 ? "退" : "换" }}货审核</h3>
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
                <div class="opctionItem2" v-if="isLog && isSelect == 0">
                    <h3>退货物流</h3>
                    <el-form class="saleOrderInfo">
                        <div class="formWarp formWarp1">
                            <el-form-item label="退货时间">
                                <span v-if="barinfor.logisticsCompany">{{
                                    barinfor.createDate
                                }}</span>
                            </el-form-item>
                            <el-form-item label="退货物流">
                                <span>{{ barinfor.logisticsCompany }}</span>
                            </el-form-item>
                            <el-form-item label="退货单号">
                                <span>{{ barinfor.logisticsNumber }}</span>
                            </el-form-item>
                        </div>
                    </el-form>
                </div>

                <div class="opctionItem2" v-if="isLog && isSelect == 1">
                    <h3 v-if="isLog && isSelect == 1">换货物流</h3>
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
            <!-- 分割线------------------------------------------------ -->
            <div class="returnGoods">
                <div class="returnGoodsTop">
                    <h3 style="font-size: 18px">
                        {{ isSelect == 0 ? "退" : "换" }}货商品
                    </h3>
                    <el-button class="goOrderDetail" @click="jumpOrderDetail"
                        >前往查看订单详情</el-button
                    >
                </div>
                <el-table :data="saleGoods" border="" style="width: 100%">
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
                                        ,margin-left: 8px;
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
                        align="center"
                    ></el-table-column>
                    <el-table-column
                        prop="specAttrName"
                        label="规格"
                        align="center"
                    ></el-table-column>
                    <el-table-column
                        prop="specSellPrice"
                        label="单价"
                        align="center"
                    >
                        <template
                            slot-scope="scope"
                            v-if="
                                scope.row.specSellPrice !== '' &&
                                scope.row.specSellPrice !== null
                            "
                            >￥{{ scope.row.specSellPrice }}</template
                        >
                    </el-table-column>
                    <el-table-column
                        prop="goodsNum"
                        label="数量"
                        align="center"
                    ></el-table-column>
                    <el-table-column
                        prop="specPayPrice"
                        label="总价"
                        align="center"
                    >
                        <template
                            slot-scope="scope"
                            v-if="
                                scope.row.specPayPrice !== '' &&
                                scope.row.specPayPrice !== null
                            "
                            >￥{{ scope.row.specPayPrice }}</template
                        >
                    </el-table-column>
                </el-table>
            </div>
            <!-- 分割线------------------------------------------------ -->
            <div class="operationLog">
                <h3 style="font-size: 18px">操作日志</h3>
                <el-table
                    width="100%"
                    :data="saleLogs"
                    border=""
                    v-loading=""
                    style="width: 100%; maigin-top: 20px; margin-bottom: 40px"
                >
                    <el-table-column
                        prop="creator"
                        label="操作者"
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
                        label="操作时间"
                        align="center"
                    ></el-table-column>
                </el-table>
            </div>
            <!--返回按钮-->
            <div v-if="!isBtn" style="margin-top: 40px; margin-bottom: 40px">
                <el-button
                    type="primary"
                    style="display: block; margin: 0 auto"
                    @click="changePage()"
                    >返回</el-button
                >
            </div>

            <!-- 分割线------------------------------------------------ -->
            <h3 style="font-size: 18px" v-if="isBtn">商家退换货处理</h3>
            <el-form ref="form" :model="form" :rules="reaRule" v-if="isBtn">
                <el-form-item label="审核理由：" prop="reason">
                    <el-input
                        type="textarea"
                        v-model="form.reason"
                        maxlength="500"
                        style="resize: none; margin-left: 6px"
                        :autosize="{ minRows: 4, maxRows: 6 }"
                        placeholder="请输入内容"
                    ></el-input>
                </el-form-item>
                <el-form-item label-width="80px">
                    <div
                        style="
                            margin-left: 6px;
                            margin-top: 20px;
                            display: flex;
                            justify-content: space-between;
                            width: 250px;
                        "
                    >
                        <el-button @click="agreeGoods(0)">取消</el-button>
                        <el-button
                            type="danger"
                            plain
                            v-loading="checkLoading"
                            @click="agreeGoods(2)"
                            >拒绝</el-button
                        >
                        <el-button
                            type="primary"
                            plain
                            v-loading="checkLoading"
                            @click="agreeGoods(1)"
                            >同意</el-button
                        >
                    </div>
                </el-form-item>
            </el-form>
        </div>
        <div v-else>
            <orderDet
                ref="orderDetailCompon"
                @changePage="changePage"
                :index="'2'"
                :orderDetBreaddata="orderDetBreaddata"
            ></orderDet>
        </div>
        <!-- 预览h5 -->
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import orderDet from "../../order/list/orderDet";
    import modelPreviewH5 from "../../mggoods/modules/model-preview-h5.vue";
    import { examineGoods } from "@/api/api";
    export default {
        data() {
            return {
                previewH5Visible: false,
                form: { reason: "" }, //审核理由
                isSaleList: true,
                checkLoading: false,
                orderDetBreaddata: ["售后管理", "售后审核", "售后详情", "订单详情"],
                reaRule: {
                    reason: [
                        {
                            required: true,
                            message: "请输入审核理由",
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        components: {
            Bread,
            orderDet,
            modelPreviewH5,
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
            "data",
            "barinfor",
            "saleGoods",
            "piclist", //凭证照片
            "row", //查看订单的那一行数据
        ],
        methods: {
            //跳到订单详情页面
            jumpOrderDetail() {
                this.isSaleList = false;
                this.$nextTick(() => {
                    this.$refs.orderDetailCompon.init(this.row);
                });
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?goodsId=" +
                        row.goodsId +
                        "&specId=" +
                        row.specId
                );
            },
            //返回上一级
            changePage() {
                if (this.isSaleList == false) {
                    this.isSaleList = true;
                } else {
                    this.$emit("changeState");
                }
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
            //审核退换货
            agreeGoods(type) {
                this.checkLoading = true;

                //0 取消 1 同意 2 拒绝
                if (type == 0) {
                    this.changePage();
                    return;
                }
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        const obj = {
                            id: this.saleAuditLog.id,
                            aftersaleSn: this.aftersale.aftersaleSn,
                            aftersaleType: this.aftersale.aftersaleType,
                            process: 1, //1:商家审核,2:平台审核
                            reason: this.form.reason,
                            result: type,
                        };
                        examineGoods(obj).then((res) => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg,
                                });
                                this.changePage();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg,
                                });
                                this.checkLoading = false;
                            }
                        });
                    } else {
                        this.checkLoading = false;
                        return false;
                    }
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .aftersalelog_afterDetClass {
        .creater {
            display: inline-block;
            width: 80px;
            margin: 0 15px;
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
            padding-right: 9%;
            label {
                width: 269px;
                height: 23px;
            }
        }
        .orderState div {
            // margin-left: 5%;
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
            .opctionItem2 {
                // h3{
                //   padding-left: 40px;
                // }
                width: 33%;
                border-right: 1px solid #eeeeee;
                margin-top: 16px;
                margin-bottom: 16px;
                padding-left: 2%;
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
        .bottomBtns {
            text-align: center;
            position: fixed;
            bottom: 20px;
            width: 216px;
            left: 50%;
            z-index: 22;
            background-color: white;
        }

        #afterDetGoodsDetail1 {
            /deep/ .el-form-item {
                margin-top: 0px !important;
                display: flex;
                .el-form-item__label {
                    width: 80px !important;
                    min-width: 80px !important;
                }
                .el-form-item__content div {
                    // margin-left: 10px !important;
                }
                .el-form-item__content {
                    // margin-left: 0 !important;
                    word-wrap: break-word;
                    width: 70%;
                }
            }
        }

        .el-form-item {
            margin-bottom: 0 !important;
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
        //  /deep/ th {
        //       color:#6185CD !important;
        //       background-color:#D2E0F7 !important;
        //   }
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
    .pingzheng {
        /deep/ .el-form-item__content {
            margin-left: 10px !important;
            width: 1px;
            display: flex;
        }
    }
</style>
