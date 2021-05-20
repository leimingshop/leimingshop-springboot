<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
            <div style="display: flex">
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
                    1、此功能为运维技术人员操作的功能，请谨慎操作
                </div>
            </template>
        </el-alert>

        <el-table
            width="100%"
            :data="tableData"
            border
            style="width: 100%"
            :height="tableheight"
        >
            <el-table-column
                prop="name"
                label="索引名称"
                align="center"
                min-width="200"
            >
            </el-table-column>

            <el-table-column label="操作" align="center" min-width="120">
                <template slot-scope="scope">
                    <el-button
                        v-if="
                            scope.row.type == 1 &&
                            $hasPermission('sys:goods:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 2 &&
                            $hasPermission('sys:spec:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 3 &&
                            $hasPermission('sys:coupons:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 4 &&
                            $hasPermission('sys:reduce:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 5 &&
                            $hasPermission('sys:seckill:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 6 &&
                            $hasPermission('sys:group:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>

                    <el-button
                        v-if="
                            scope.row.type == 7 &&
                            $hasPermission('sys:store:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>
                    <el-button
                        v-if="
                            scope.row.type == 8 &&
                            $hasPermission('sys:store:search')
                        "
                        @click.native.prevent="synchronizeIndex(scope.row)"
                        type="text"
                        size="mini"
                    >
                        同步索引
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import {
        goodsIndexInit,
        specIndexInit,
        couponsIndexInit,
        storeIndexInit,
        reduceIndexInit,
        seckillIndexInit,
        groupIndexInit,
        flashSaleIndexInit
    } from "@/api/api.js";

    export default {
        dataForm: {
            module: "",
        },
        data() {
            return {
                forbitLoading: false,
                breaddata: ["网站管理", "搜索管理", "同步索引"],
                tableData: [
                    {
                        type: 1,
                        name: "商品索引",
                    },
                    {
                        type: 2,
                        name: "规格索引",
                    },
                    {
                        type: 3,
                        name: "优惠券索引",
                    },
                    {
                        type: 4,
                        name: "满减活动索引",
                    },
                    {
                        type: 5,
                        name: "秒杀活动索引",
                    },
                    {
                        type: 6,
                        name: "拼团活动索引",
                    },
                    {
                        type: 7,
                        name: "店铺索引",
                    },
                    {
                        type: 8,
                        name: "限时购索引",
                    },
                ],
                tableheight: document.body.offsetHeight - 280,
            };
        },
        components: {
            Bread,
        },
        created() {},
        mounted() {},
        methods: {
            synchronizeIndex(row) {
                this.$confirm("是否同步 " + row.name + " ?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        this.forbitLoading = true;
                        // 同步商品索引
                        if (row.type == 1) {
                            goodsIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 2) {
                            // 同步规格索引
                            specIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 3) {
                            // 同步优惠券索引
                            couponsIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 4) {
                            // 同步满减活动索引
                            reduceIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 5) {
                            // 同步秒杀活动索引
                            seckillIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 6) {
                            // 同步拼团索引
                            groupIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        } else if (row.type == 7) {
                            // 同步店铺索引
                            storeIndexInit().then((res) => {
                                this.forbitLoading = false;
                                let status = "error";
                                if (res.code == 200) {
                                    status = "success";
                                }
                                this.$message({
                                    message: res.msg,
                                    type: status,
                                    duration: 1500,
                                });
                            });
                        }else if(row.type == 8){
                            // 同步限时购索引
                            flashSaleIndexInit().then((res)=>{
                                this.forbitLoading = false;
                                let status = "error"
                                if(res.code==200){
                                    status = "success"
                                }
                                this.$message({
                                    message:res.msg,
                                    type: status,
                                    duration: 1500,
                                })
                            })
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
        },
    };
</script>
