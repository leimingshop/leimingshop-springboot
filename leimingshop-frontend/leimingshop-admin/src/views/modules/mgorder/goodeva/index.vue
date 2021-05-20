<template>
    <div v-if="isList">
        <div id="control-area">

            <Bread :breaddata="breaddata"></Bread>

            <!--导出按钮-->
            <importAndExport style="right: 20px;top: 12px;position: absolute"
                :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"
                v-if="$hasPermission('sys:evaluate:export')"></importAndExport>

            <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm">
                <el-form-item label="评价时间：">
                    <el-date-picker v-model="timeArr" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                        align="left" start-placeholder="开始日期" end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="评价状态：">
                    <el-select v-model="dataForm.evaluateState" placeholder="请选择活动区域">
                        <el-option label="全部" value=""></el-option>
                        <el-option label="显示" value="0"></el-option>
                        <el-option label="隐藏" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="读取状态：" prop="readState">
                    <el-select v-model="dataForm.readState" placeholder="请选择活动区域">
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未读" value="0"></el-option>
                        <el-option label="已读" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                    <el-button class="btn" type="primary" plain @click="reset()">重置</el-button>
                </el-form-item>
                <br />

            </el-form>


            <div class="formControlArea">
                <el-form>
                    <el-form-item style="display:block;margin-bottom:0px !important;">
                        <el-button class="btn" @click="changeRead(null,null,0)" type="primary" plain
                            v-if="$hasPermission('sys:evaluate:batch')">批量撤销
                        </el-button>
                        <!-- <el-button class="btn" @click="changeHide()" type="info" plain
                            v-if="$hasPermission('sys:evaluate:batch')">批量隐藏
                    </el-button> -->
                        <el-button class="btn" @click="changeRead(null,null,1)" type="danger" plain
                            v-if="$hasPermission('sys:evaluate:delete')">批量删除
                        </el-button>
                        <el-button class="btn" type="primary" plain @click="changeRead(null,1,null)"
                            v-if="$hasPermission('sys:evaluate:read')">批量已读
                        </el-button>
                    </el-form-item>
                </el-form>
                <div style="display:flex;">
                    <mainSwitch></mainSwitch>
                    <mainTipsMessage></mainTipsMessage>
                </div>
            </div>

                <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                    <template slot='title'>
                        <div class="iconSize">操作提示：</div>
                        <div class="iconSize">1、违规的评价可通过违规删除隐藏该评价在前台的展示</div>
                        <div class="iconSize">2、误操作的评价可通过撤销操作回复显示</div>
                        <div class="iconSize">3、勾选复选框可批进行批量删除或已读操作</div>
                    </template>
                </el-alert>
        </div>

        <el-table width="100%" :data="dataList" border="" @selection-change="dataListSelectionChangeHandle"
            v-loading="dataListLoading" style="width: 100%,maigin-top:20px;"
            :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
            <el-table-column type="selection" width="50" align="center"></el-table-column>
            <el-table-column type='index' prop="$index" label="序号" align="center" width="50">
                <template slot-scope="scope">
                    {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                </template>
            </el-table-column>
            <el-table-column prop="goodsName" label="商品信息" align="center" min-width="120">

                <template slot-scope="scope">
                    <div class="towEllipsis"
                        style="text-align: left;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;">
                        <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                            v-if="scope.row.goodsName.length>20" placement="top-start">
                            <span style="color: #4e80db;text-decoration: none; cursor: pointer;"
                                @click="previewH5Fn(scope.row)">
                                <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                                {{scope.row.goodsName}}
                            </span>
                        </el-tooltip>
                        <span style="color: #4e80db;text-decoration: none; cursor: pointer;" v-else
                            @click="previewH5Fn(scope.row)">
                            <img :src="scope.row.specMainPicture | filterImgUrl" width="40px" height="40px" />
                            {{scope.row.goodsName}}
                        </span>
                    </div>

                </template>

            </el-table-column>
            <el-table-column label="评价" align="left" width="330">
                <template slot-scope="scope">
                    <div class="evaluatePropsWrap">
                        <div class="evaluateProps" style="width: 93%">
                            <span>
                                <label style="font-weight: 550;float: left">评价星级:</label>
                                <el-rate v-model="scope.row.evaluateScores " disabled text-color="#ff9900"
                                    style="float: left;margin-left: 8px">
                                </el-rate>
                            </span>
                            <br />
                            <span>
                                <label style="font-weight: 550">评价时间:</label>
                                <span style="margin-left: 8px">{{scope.row.createDate}}</span>
                            </span>
                            <br />
                            <span v-if="scope.row.evaluateContent" style="display:-webkit-box;
 -webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;">

                                <label style="font-weight: 550">评价内容:</label>
                                <el-tooltip class="item" effect="dark" :content="scope.row.evaluateContent"
                                    placement="top-start" popper-class="evaluationContent">
                                    <span style="margin-left: 8px">{{scope.row.evaluateContent}}</span>
                                </el-tooltip>
                            </span>

                            <span v-if="scope.row.replyContent" style="display:-webkit-box;
 -webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;">

                                <label style="font-weight: 550">回复内容:</label>
                                <el-tooltip class="item" effect="dark" :content="scope.row.replyContent"
                                    placement="bottom" popper-class="evaluationContent">
                                    <span style="margin-left: 8px">{{scope.row.replyContent}}</span>
                                </el-tooltip>
                            </span>

                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="晒单图片" align="center" min-width="130px">
                <template slot-scope="scope">
                    <div align="center">
                        <span v-if="!scope.row.evaluateImage || scope.row.evaluateImage==null"></span>

                        <img v-else v-for="(itemimg,index) in scope.row.evaluateImage.split(',')"
                            :src="itemimg | filterImgUrl" width="40px" height="40px"
                            @click="previewBigImg(scope.row.evaluateImage,index)" />
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="readState " label="读取状态" align="center" min-width="60">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.readState==0" type="success" plain>未读</el-tag>
                    <el-tag v-if="scope.row.readState==1" type="info">已读</el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="memberName" label="用户信息" align="center"></el-table-column>
            <el-table-column prop="createDate" label="评价时间" align="center" min-width="120"></el-table-column>
            <el-table-column prop="evaluateState " label="显示状态" align="center" min-width="60">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.evaluateState==0" type="success" plain>显示</el-tag>
                    <el-tag v-if="scope.row.evaluateState==1" type="info" plain>隐藏</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
                <template slot-scope="scope">
                    <!-- <el-button
                            size="mini"
                            @click="changeHide( scope.row.id)"
                            v-if="scope.row.evaluateState==0 && $hasPermission('sys:evaluate:status')"
                            type="text"
                    ><span class="artdisable">隐藏</span>
                    </el-button>-->

                    <!-- <el-button size="mini" type="text" @click="reply( scope.row)"
                               v-if="$hasPermission('sys:evaluate:reply')">回复
                    </el-button> -->
                    <el-button v-if="scope.row.illegalDel==0" class="artdanger" size="mini" type="text"
                        @click="changeRead(scope.row.id,null,1)">违规删除
                    </el-button>
                    <el-button size="mini" @click="changeRead(scope.row.id,null,0)" type="text" class="artstart"
                        v-if="scope.row.illegalDel==1">
                        撤销
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination @size-change="pageSizeChangeHandle" @current-change="pageCurrentChangeHandle"
            :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
    </div>
    <evaDet v-else @changeState="changeModel" :evaDetails="evaDetails"></evaDet>
</template>
<script>
    import importAndExport from "@/components/import-and-export"
    import {
        goodsEvaluateExport
    } from '@/api/io'
    import Bread from "@/components/bread";
    import evaDet from "./evaDet.vue";
    import {
        deleva,
        goodseva
    } from "@/api/url";
    import {
        changeReadStatus,
        changeStatus,
        evaDets,
        msgReply
    } from "@/api/api";
    import mixinViewModule from "@/mixins/view-module";
    import modelPreviewH5 from '../../mggoods/goods/modules/model-preview-h5';
    import cloneDeep from 'lodash/cloneDeep'

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: goodsEvaluateExport, //导出接口
                    exportWord: "导出",
                },
                mixinViewModuleOptions: {
                    getDataListURL: goodseva,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleva,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id"
                },
                breaddata: ["订单管理", "售后管理", "商品评价"],
                dataForm: {
                    evaluateState: "",
                    readState: '',
                },
                evaDetails: "", //评价详情
                dataListLoading: false,
                totalPage: 0,
                isList: true, //列表页 false-详情页
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10 //每页显示的条数
                },
                timeArr: "", //下单时间
                dataList: [],
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 430,
                tableheightBig: 0

            };
        },
        created() {},
        components: {
            Bread,
            evaDet,
            modelPreviewH5,
            importAndExport
        },
        watch: {
            '$store.state.mainSwitch'() { //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100)
            }
        },

        methods: {
            //大图预览带分页
            previewBigImg(images, index) {
                //string转数组
                var imagesArr = images ? images.split(",") : [];
                if (imagesArr.length == 0) {
                    return;
                }
                //  如果是绝对地址，不用加前缀(拼接地址)
                imagesArr.forEach((item2, index2) => {
                    if (/http/.test(item2) || /data:image/.test(item2)) {} else {
                        imagesArr[index2] = window.SITE_CONFIG['imgURL'] + "" + item2;
                    }
                })
                this.$imagePreview({
                    images: imagesArr,
                    index: index,

                })
            },
            //详情
            handleEdit(index) {
                const obj = {
                    id: index.id
                };
                evaDets(obj).then(res => {
                    if (res.code == 200) {
                        this.evaDetails = res.data;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
                this.isList = false;
            },
            //返回上一级
            changeModel() {
                this.isList = !this.isList;
            },
            //条件查询
            getData() {
                this.dataForm.startDate = this.timeArr && this.timeArr[0];
                this.dataForm.endDate = this.timeArr && this.timeArr[1];
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //重置
            reset() {
                this.timeArr = [];
                // this.dataForm = [];
                this.dataForm.evaluateState = "";
                this.dataForm.readState = "";
                this.dataForm.endDate = ''
                this.dataForm.startDate = ''
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //评价回复
            reply(index) {
                this.$prompt("回复内容", "回复", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消"
                    })
                    .then(({
                        value
                    }) => {
                        const obj = {
                            id: index.id,
                            content: value
                        };
                        msgReply(obj).then(res => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: res.msg
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: res.msg
                                });
                            }
                        });
                    })
                    .catch(() => {
                        // this.$message({
                        //   type: "info",
                        //   message: "取消回复"
                        // });
                    });
            },
            //批量已读
            changeRead(id, readstate, illegalDel) {
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: 'warning',
                        message: '请选择批量处理的数据'
                    })
                    return false
                }
                const arrId = []
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id)
                })
                var message = '';
                if (illegalDel != null && illegalDel == 1) {
                    message = '您确认违规删除所有选中评价吗？'
                } else if (illegalDel != null && illegalDel == 0) {
                    message = '您确认撤销所有选中评价吗？'
                } else {
                    message = '您确认读取所有选中评价吗？'
                }
                this.$confirm(message, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    .then(() => {
                        const obj = {
                            ids: id == null ? arrId : [id],
                            readState: readstate,
                            illegalDel: illegalDel
                        }
                        changeReadStatus(obj).then(res => {
                            if (res.code == 200) {
                                this.$message({
                                    type: 'success',
                                    message: '修改成功'
                                })
                                this.getDataList()
                            } else {
                                this.$message({
                                    type: 'warning',
                                    message: '修改失败'
                                })
                                this.getDataList()
                            }
                        })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消'
                        })
                    })
            },
            //批量显示/显示
            changeShow(id) {
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择批量处理的数据"
                    });
                    return false;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });
                this.$confirm("您确认显示所有选中评价吗？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                    .then(() => {
                        const obj = {
                            ids: id == null ? arrId : [id],
                            state: 0
                        };
                        changeStatus(obj).then(res => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: "修改成功!"
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: "修改失败!"
                                });
                                this.getDataList();
                            }
                        });
                    })
                    .catch(() => {
                        // this.$message({
                        //   type: "info",
                        //   message: "已取消"
                        // });
                    });
            },
            //批量隐藏/隐藏
            changeHide(id) {
                if (id == undefined && this.dataListSelections.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择批量处理的数据"
                    });
                    return false;
                }
                const arrId = [];
                this.dataListSelections.forEach(function (val, index, arr) {
                    arrId.push(val.id);
                });
                this.$confirm("您确认隐藏所有选中评价吗？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                    .then(() => {
                        const obj = {
                            ids: id == null ? arrId : [id],
                            state: 1
                        };
                        changeStatus(obj).then(res => {
                            if (res.code == 200) {
                                this.$message({
                                    type: "success",
                                    message: "修改成功!"
                                });
                                this.getDataList();
                            } else {
                                this.$message({
                                    type: "warning",
                                    message: "修改失败!"
                                });
                                this.getDataList();
                            }
                        });
                    })
                    .catch(() => {
                        // this.$message({
                        //   type: "info",
                        //   message: "已取消"
                        // });
                    });
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(window.SITE_CONFIG['pcURL'] + '/goodsDetails?goodsId=' + row.goodsId + '&specId=' + row
                    .goodsSpecId);
            }
        }
    };
</script>
<style lang="css">
    /*评价*/
    .evaluatePropsWrap {
        margin: auto;
        width: 330px;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .evaluatePropsWrap.evaluateProps {
        width: 210px;
        height: 70px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        color: #999999;
    }

    /*评价内容回复内容框大小限制*/
    .evaluationContent {
        max-width: 40%;
    }
</style>