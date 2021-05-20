<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-button
            class="btn createShops"
            type="primary"
            v-if="dataList.length == 0 && !loading"
            @click="changeCompent()"
            >创建店铺</el-button
        >
        <!--表格-->
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
                    1、商户注册完，初次登录，会自动显示店铺入驻菜单
                </div>
                <div class="iconSize">
                    2、店铺列表如果为空，则点击创建店铺可以弹出创建店铺页面
                </div>
                <div class="iconSize">
                    3、如果店铺列表页已存在一个店铺，则创建店铺按钮不显示
                </div>
                <div class="iconSize">
                    4、店铺状态指的是店铺的审核状态，商户入驻时需要运营人员审核
                </div>
            </template>
        </el-alert>

        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="loading"
            style="width: 100%; maigin-top: 20px"
        >
            <!-- <el-table-column prop="id" label="店铺ID" align="center" width="180"></el-table-column> -->
            <el-table-column
                prop="storeName"
                label="店铺名称"
                align="center"
                width="160"
            ></el-table-column>
            <el-table-column
                prop="storeLogo"
                label="店铺logo"
                align="center"
                min-width="220"
            >
                <template slot-scope="scope">
                    <img
                        :src="scope.row.storeLogo | filterImgUrl"
                        alt=""
                        style="
                            object-fit: contain;
                            width: 70px;
                            height: 70px;
                            border-radius: 100px;
                        "
                    />
                </template>
            </el-table-column>
            <el-table-column prop="storeType" label="店铺类型" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.storeType == 1">自营商户</div>
                    <div v-if="scope.row.storeType == 2">普通商户</div>
                </template>
            </el-table-column>
            <el-table-column
                prop="storeIntro"
                label="店铺简介"
                align="center"
                width="170"
            >
                <template slot-scope="scope">
                    <div :title="scope.row.storeIntro">
                        {{ scope.row.storeIntro }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="storeLinkman"
                label="店铺联系人"
                align="center"
                width="170"
            ></el-table-column>
            <el-table-column
                prop="registerAuditStatus"
                label="店铺状态"
                align="center"
                width="170"
            >
                <template slot-scope="scope">
                    <div v-if="scope.row.registerAuditStatus == 10">待审核</div>
                    <div v-if="scope.row.registerAuditStatus == 20">
                        审核通过
                    </div>
                    <div v-if="scope.row.registerAuditStatus == 30">
                        审核拒绝
                    </div>
                    <div v-if="scope.row.registerAuditStatus == 40">待提交</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                    <div
                        v-if="
                            scope.row.registerAuditStatus == 30 ||
                            scope.row.registerAuditStatus == 40
                        "
                    >
                        <el-button
                            size="mini"
                            type="text"
                            @click="changeCompent(scope.row)"
                            >修改</el-button
                        >
                        <el-button
                            size="mini"
                            type="text"
                            @click="submitExamine(scope.row)"
                            >提交</el-button
                        >
                    </div>
                    <div v-else>
                        <el-button
                            size="mini"
                            type="text"
                            @click="previewShop(scope.row)"
                            v-if="$hasPermission('sys:store:list')"
                            >查看</el-button
                        >
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="prompt">
            注：商户想要入驻商城，首先需要创建店铺，维护店铺相关信息，公司相关信息，提交资质证明。然后提交店铺至平台运营审核，审核通过则开店成功。
        </div>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import { storeInfo, storeUdateStatus, storeList } from "@/api/api";
    export default {
        name: "list",
        data() {
            return {
                breaddata: ["店铺管理", "店铺列表"],
                dataList: [],
                loading: true,
            };
        },
        components: {
            Bread,
        },
        mounted() {
            this.$nextTick(() => {
                this.getStoreInfo();
            });
        },
        methods: {
            // 点击新增、编辑
            changeCompent(row) {
                this.$emit("changeCompent", row);
            },
            // 获取店铺列表
            getStoreInfo() {
                var that = this;
                storeList().then((res) => {
                    that.loading = false;
                    if (res.code == 200 && res.data.list) {
                        that.dataList = res.data.list;
                    }
                });
            },
            // 提交审核
            submitExamine(row) {
                let that = this;
                this.$confirm("确认提交审核？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        var obj = {
                            auditType: 3, // 审核类型 1 店铺普通信息 2 店铺公司信息 3 店铺入住审核 ,
                            // "id": row.storeId,// 审核资质或者公司信息ID
                            // "operator": "string",
                            // "registerAuditCause": row.registerAuditCause ,// 审核原因
                            registerAuditStatus: 10, // 10 待审核 20 审核通过 30 审核拒绝 40 待提交
                            storeId: row.id, //  店铺ID
                        };
                        storeUdateStatus(obj).then((res) => {
                            if (res.code == 200) {
                                that.$message.success(res.msg);
                                that.getStoreInfo();
                            } else {
                                that.$message.error(res.msg);
                            }
                        });
                    })
                    .catch(() => {
                        // this.$message({type: "info",message: "已取消"});
                    });
            },
            // 查看预览
            previewShop(row) {
                this.$emit("showPreview", row);
            },
        },
    };
</script>

<style  lang="scss" scoped>
    .createShops {
        margin: 20px 0;
    }
    .prompt {
        margin-top: 65px;
    }
</style>
