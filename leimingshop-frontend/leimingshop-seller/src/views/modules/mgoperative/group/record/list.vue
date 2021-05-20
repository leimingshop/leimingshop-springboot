<template>
    <!--优惠券列表-->
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                class="grayLine topGapPadding"
                :inline="true"
                ref="dataForm"
                :model="dataForm"
            >
                <!-- <el-form-item label="拼团记录id：">
        <el-input v-model="dataForm.activityRecordId" placeholder="拼团记录id" clearable></el-input>
      </el-form-item>
      <el-form-item label="拼团活动id：">
        <el-input v-model="dataForm.groupId" placeholder="拼团活动id" clearable></el-input>
      </el-form-item> -->
                <el-form-item label="活动名称：">
                    <el-input
                        v-model="dataForm.activityName"
                        placeholder="活动名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="活动状态：">
                    <el-select
                        v-model="dataForm.groupStatus"
                        placeholder="活动状态"
                    >
                        <el-option label="全部状态" value=""></el-option>
                        <el-option label="拼团中" value="0">拼团中</el-option>
                        <el-option label="拼团成功" value="1">拼团成功</el-option>
                        <el-option label="拼团失败" value="2">拼团失败</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
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
                        1、拼团记录显示该店铺所有状态的拼团活动记录
                    </div>
                    <div class="iconSize">
                        2、拼团中的可点击进入详情也进行快速成团操作
                    </div>
                </template>
            </el-alert>
        </div>

        <!--表格-->
        <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="dataListLoading"
            style="width: 100%; maigin-top: 20px"
        >
            <!-- <el-table-column prop="id" label="拼团记录id" align="center" min-width="200"></el-table-column> -->
            <!-- <el-table-column prop="groupId" label="拼团活动id" align="center" min-width="200"></el-table-column> -->
            <el-table-column
                prop="groupName"
                label="拼团活动名称"
                align="center"
                min-width="200"
            ></el-table-column>
            <el-table-column
                prop="goodsName"
                label="商品信息"
                align="center"
                min-width="200"
            >
                <template slot-scope="scope">
                    <div
                        style="display: flex; cursor: pointer"
                        @click="previewH5Fn(scope.row)"
                    >
                        <img
                            :src="scope.row.goodsMainPicture | filterImgUrl"
                            width="40px"
                            height="40px"
                        />
                        <div
                            class="towEllipsis"
                            style="
                                text-align: center;
                                cursor: pointer;
                                margin-left: 10px;
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
                prop="startTime"
                label="开团时间"
                align="center"
                min-width="160"
            ></el-table-column>
            <el-table-column
                prop="groupNum"
                label="成团所剩时间"
                align="center"
                min-width="160"
            >
                <template slot-scope="scope">
                    {{ scope.row.timeLeft }}
                </template>
            </el-table-column>
            <el-table-column
                prop="needNum"
                label="所需成团人数"
                align="center"
                min-width="120"
            ></el-table-column>
            <el-table-column
                prop="actualTime"
                label="成团时间"
                align="center"
                width="160"
            ></el-table-column>
            <el-table-column
                prop="activityState"
                label="状态"
                align="center"
                width="100"
            >
                <template slot-scope="scope">
                    <div style="color: red" v-if="scope.row.groupStatus == 0">
                        拼团中
                    </div>
                    <div
                        style="color: green"
                        v-else-if="scope.row.groupStatus == 1"
                    >
                        拼团成功
                    </div>
                    <div
                        style="color: gray"
                        v-else-if="scope.row.groupStatus == 2"
                    >
                        拼团失败
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="changeCompent(scope.row.id, 2)"
                        >查看拼团
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        >
        </el-pagination>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import { getGroupRecordList } from "@/api/url";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["营销系统", "拼团记录"],
                mixinViewModuleOptions: {
                    getDataListURL: getGroupRecordList,
                    getDataListIsPage: true,
                    // deleteURL: '/seller-api/group',
                    // deleteIsBatch: true
                },
                // 查询表单
                dateArr: [],
                dataForm: {
                    activityRecordId: "",
                    groupId: "",
                    groupName: "",
                    groupStatus: "",
                },
                // 优惠券列表表格
                dataList: [],
                dataListLoading: false,
                setTimeoutTime: null,
            };
        },
        components: {
            Bread,
        },
        beforeDestroy() {
            clearInterval(this.setTimeoutTime);
            this.timer = null;
        },
        methods: {
            // 查询表格数据
            async getData() {
                this.page = 1;
                this.dataForm.startTime = this.dateArr[0] ? this.dateArr[0] : null;
                this.dataForm.endTime = this.dateArr[1] ? this.dateArr[1] : null;
                this.getDataList();
                await this.dataList.forEach((v, i) => {
                    v.timeLeft = "0天00时00分00秒";
                });
                if (this.setTimeoutTime != "") {
                    clearTimeout(this.setTimeoutTime);
                }
                this.setIntervalTime();
            },
            forTime() {
                let obj = null;
                let isClose = true;
                this.dataList.forEach((v, i) => {
                    let newTime = new Date().getTime();
                    let endTime = new Date(v.overTime).getTime();
                    if (endTime - newTime > 0 && v.groupStatus == 0) {
                        let time = (endTime - newTime) / 1000;
                        let day = parseInt(time / (60 * 60 * 24));
                        let hou = parseInt((time % (60 * 60 * 24)) / 3600);
                        let min = parseInt(((time % (60 * 60 * 24)) % 3600) / 60);
                        let sec = parseInt(((time % (60 * 60 * 24)) % 3600) % 60);
                        obj = {
                            day: day,
                            hou: hou > 9 ? hou : "0" + hou,
                            min: min > 9 ? min : "0" + min,
                            sec: sec > 9 ? sec : "0" + sec,
                        };
                        let timeLeft =
                            obj.day +
                            "天" +
                            obj.hou +
                            "时" +
                            obj.min +
                            "分" +
                            obj.sec +
                            "秒";
                        console.log(i + "xxx" + timeLeft);
                        this.$set(this.dataList[i], "timeLeft", timeLeft);
                        isClose = false;
                    } else {
                        this.$set(this.dataList[i], "timeLeft", "--");
                        // this.$set(this.dataList[i], 'timeLeft', '0天00时00分00秒')
                    }
                });
                //如果所有活动都结束了就停止倒计时
                if (isClose) {
                    clearTimeout(this.setTimeoutTime);
                }
            },
            setIntervalTime() {
                this.setTimeoutTime = setInterval(() => {
                    this.forTime();
                }, 1000);
            },
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?specId=" +
                        row.specId
                );
            },
            // 重置
            reset() {
                this.dateArr = [];
                this.dataForm = {
                    activityRecordId: "",
                    groupId: "",
                    groupName: "",
                    groupStatus: "",
                };
                this.getDataList();
            },
            // 查看拼团记录详情
            changeCompent(id, type) {
                this.$emit("changeCompent", id, type);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .couponList {
        /deep/ .el-input__icon {
            height: unset !important;
        }

        .line {
            height: 10px;
            background: #ecedf1;
            margin: 0 -20px;
        }

        .addButton {
            margin: 10px 30px;
        }

        .el-table--border {
            margin-top: 20px;
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
