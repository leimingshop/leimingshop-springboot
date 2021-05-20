<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
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
                <div class="iconSize">1、该列表显示本平台支持的支付方式</div>
                <div class="iconSize">
                    2、禁用功能请谨慎操作，关闭后可能会对用户购买商品支付造成影响
                </div>
            </template>
        </el-alert>
        <el-table
            :data="dataList"
            style="width: 100%"
            border
            v-loading="dataListLoading"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column
                label="名称"
                align="center"
                prop="paymentName"
            ></el-table-column>
            <el-table-column label="Logo" align="center" prop="paymentLogo">
                <template slot-scope="scope">
                    <img
                        :src="$imgDomain + scope.row.paymentLogo"
                        alt
                        v-if="scope.row.picPath != ''"
                        style="width: 30px"
                    />
                </template>
            </el-table-column>
            <el-table-column
                label="说明"
                align="center"
                prop="remark"
                width="750px"
            ></el-table-column>
            <el-table-column
                label="状态"
                align="center"
                prop="paymentState"
                :formatter="statusRole"
            ></el-table-column>
            <!--      <el-table-column align="center" label="操作">-->
            <!--        <template slot-scope="scope">-->
            <!--          <el-button @click.native.prevent="edit(scope.row)" type="text" size="mini" v-if="$hasPermission('sys:payment:edit')">编辑</el-button>-->
            <!--          <el-button-->
            <!--            @click.native.prevent="changeStatus(scope.row)"-->
            <!--            type="text"-->
            <!--            size="mini"-->
            <!--            v-if="scope.row.paymentState==1 && $hasPermission('sys:payment:status')"-->
            <!--          > <span class="artdanger">禁用</span></el-button>-->
            <!--          <el-button  @click.native.prevent="changeStatus(scope.row)" type="text" size="mini" v-else> <span class="artstart">启用</span></el-button>-->
            <!--        </template>-->
            <!--      </el-table-column>-->
        </el-table>
        <updateEditData
            v-if="addEditDataVisible"
            ref="updateEditData"
            @searchDataList="getDataList"
        ></updateEditData>
    </div>
</template>
<script>
    import updateEditData from "./model-update-edit-data";
    import { paymentWayUrl } from "@/api/url";
    import { paymentStatus } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: paymentWayUrl,
                    getDataListIsPage: false,
                    // exportURL: '/admin-api/log/login/export',
                    deleteIsBatch: false,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                addEditDataVisible: false,
                tableData: [
                    {
                        name: "测试微信",
                        intro: "【微信APP支付】商城Androd、IOS客户端使用微信支付",
                        status: "1",
                    },
                    {
                        name: "测试支付宝",
                        intro: "【JSAPI支付】微信内/WAP商城内使用微信支付",
                        status: "0",
                    },
                ],
                breaddata: ["财务管理", "支付管理", "支付方式"],
                statusRole: function (row, column) {
                    //状态 0--禁用  1--开启
                    return row.paymentState == 0 ? (
                        <el-tag type="danger">关闭</el-tag>
                    ) : (
                        <el-tag type="success">开启</el-tag>
                    );
                },
            };
        },
        created() {
            this.getDataList();
        },
        components: {
            Bread,
            updateEditData,
        },
        methods: {
            changeStatus(index) {
                //修改支付状态 0--禁用  1--开启
                var prompt = index.paymentState == "1" ? "禁用" : "启用";
                this.$confirm("是否" + prompt + "该支付方式?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        var obj = {
                            id: index.id,
                            paymentState: index.paymentState == "1" ? "0" : "1",
                        };
                        paymentStatus(obj).then((res) => {
                            if (res && res.code == 200) {
                                this.$message({
                                    message: res.msg,
                                    type: "success",
                                    duration: 1000,
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "warning",
                                    duration: 1000,
                                });
                            }
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "取消操作",
                        });
                    });
            },
            edit(index) {
                this.setAddEditDataVisible(true);
                this.$nextTick(() => {
                    this.$refs.updateEditData.dataForm.id = index.id;
                    this.$refs.updateEditData.init();
                });
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
        },
    };
</script>
<style lang="sass" scoped>

</style>
