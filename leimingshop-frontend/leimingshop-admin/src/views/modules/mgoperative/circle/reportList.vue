<template>
    <div>
        <div id="control-area">
        <Bread :breaddata="breaddata"></Bread>
        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getData()">
            <el-form-item label="帖子标题/内容：">
                <el-input v-model="dataForm.reportFlagName" placeholder="帖子标题/内容" clearable></el-input>
            </el-form-item>
            <el-form-item label="举报信息：">
                <el-input v-model="dataForm.reportContent" placeholder="举报信息" clearable></el-input>
            </el-form-item>
            <el-form-item label="举报人：">
                <el-input v-model="dataForm.creator" placeholder="举报人" clearable></el-input>
            </el-form-item>
            <el-form-item label="审核状态：">
                <el-select v-model="dataForm.reportStatus" placeholder="请选择">
                    <el-option v-for="item in statusList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="举报时间：">
                <el-date-picker v-model="dataForm.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                    clearable placeholder="选择开始时间">
                </el-date-picker>
                -
                <el-date-picker v-model="dataForm.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable
                    placeholder="选择结束时间">
                </el-date-picker>
            </el-form-item>

            <el-form-item>
                <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
                <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
            </el-form-item>
        </el-form>

        <el-form>
            <div class="formControlArea">
            <el-form-item style="disply:block;margin-bottom:0px !important;">
                <el-button type="danger" plain @click="deleteHandle()">批量删除</el-button>
            </el-form-item>
                <div style="display:flex;">
                    <mainSwitch></mainSwitch>
                    <mainTipsMessage></mainTipsMessage>
                </div>
            </div>
        </el-form>
        <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                <template slot='title'>
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">1、用户举报的帖子在此列表审核</div>
                </template>
            </el-alert>
        </div>
        <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle"
            style="width: 100%;" :height="tableHeight">
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
            </el-table-column>
            <el-table-column prop="reportFlagName" label="帖子标题/内容" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="publishUserName" label="发布人" header-align="center" align="center" width="120">
            </el-table-column>
            <el-table-column prop="reportContent" label="举报信息" header-align="center" align="center" width="150">
            </el-table-column>
            <el-table-column prop="creator" label="举报人" header-align="center" align="center" width="120">
            </el-table-column>
            <el-table-column prop="createDate" label="举报时间" header-align="center" align="center" width="160">
            </el-table-column>
            <el-table-column prop="reportStatus" label="审核状态" header-align="center" align="center" width="100">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.reportStatus==1">待处理</el-tag>
                    <el-tag type="success" v-if="scope.row.reportStatus==2">举报通过</el-tag>
                    <el-tag type="danger" v-if="scope.row.reportStatus==3">举报驳回</el-tag>
                </template>
            </el-table-column>
            <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
                <template slot-scope="scope">
                    <el-button type="text" size="small"
                        @click="reportAudits(scope.row.id,scope.row.reportStatus == 1 ? 0 : 1)">
                        {{scope.row.reportStatus == 1 ? '审核' : '审核结果'}}</el-button>
                    <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"></el-pagination>
        <!-- 弹窗, 审核 / 审核结果 -->
        <reportAudit v-if="reportAuditVisible" ref="reportAudit" @refreshDataList="getDataList"></reportAudit>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    import reportAudit from "./reportAudit"
    import {
        circleReportPageUrl,
        deleteCircleReportUrl
    } from '@/api/url'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                reportAuditVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: circleReportPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteCircleReportUrl,
                    deleteIsBatch: true
                },
                dataForm: {
                    status: '',
                    commentParentId: '0',
                },
                breaddata: ["运营管理", "圈子管理", "举报管理"],
                statusList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 1,
                        sgName: '待处理'
                    },
                    {
                        id: 2,
                        sgName: '举报通过'
                    },
                    {
                        id: 3,
                        sgName: '举报驳回'
                    }
                ],
                // tableheight: 'auto',
                tableHeight:null,
                tableheightBig: 0
            };
        },
        components: {
            Bread,
            reportAudit
        },
        created() {},
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
        },
        mounted(){
            window.onresize = function(){
                this.tableHeight="";
            }.bind(this)
        },
        methods: {
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm.reportStatus = '';
                this.dataForm.creator = '';
                this.dataForm.reportContent = '';
                this.dataForm.reportFlagName = '';
                this.dataForm.beginTime = '';
                this.dataForm.endTime = '';
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //审核
            reportAudits(id, auditCode) {
                console.log(id);
                this.reportAuditVisible = true
                this.$nextTick(() => {
                    this.$refs.reportAudit.visible = true;
                    // this.$refs.init(id,auditCode);
                    this.$refs.reportAudit.dataForm.id = id
                    this.$refs.reportAudit.auditDataForm.auditCode = auditCode
                })
            },

        }
    };
</script>
<style lang="scss" scoped>
    /deep/ .el-date-editor--datetime {
        width: 200px !important;
    }
</style>