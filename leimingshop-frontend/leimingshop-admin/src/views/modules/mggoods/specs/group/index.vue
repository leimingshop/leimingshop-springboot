<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form :inline="true" class="grayLine noMargin topGapPadding" :model="dataForm">
                <el-form-item label="输入搜索：">
                    <el-input v-model="dataForm.groupName" placeholder="请输入规格组名" clearable></el-input>
                </el-form-item>
                <el-form-item label="启用状态：" prop="applicationId">
                    <el-select v-model="dataForm.groupStatus">
                        <el-option v-for="item in statusOptions" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
                    <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
                </el-form-item>
                <br />

            </el-form>

            <el-form>
                <el-form-item style="margin-bottom:0px !important;">
                    <div class="formControlArea">
                        <div>
                            <el-button type="primary" @click="addOrEditHandle()"
                                v-if="$hasPermission('sys:specgroup:save')">新增分组</el-button>
                            <el-button type="danger" plain @click="deleteHandleLocal()"
                                v-if="$hasPermission('sys:batch:specgroup:delete')">批量删除</el-button>
                        </div>
                        <div style="display:flex;">
                            <mainSwitch></mainSwitch>
                            <mainTipsMessage></mainTipsMessage>
                        </div>
                    </div>

                </el-form-item>

                <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')"
                    v-show="$store.state.listMessageFlag">
                    <template slot='title'>
                        <div class="iconSize">操作提示：</div>
                        <div class="iconSize">1、分组用于对规格类型进行划分区别使用，例如规格分组名称：电脑规格分组、手机规格分组、服装规格分组等此类表述方式</div>
                        <div class="iconSize">2、在添加分组的时候可选择绑定规格，且可一个分组绑定多个规格，因此应先行添加规格后再做相关分组添加编辑设置</div>
                    </template>
                </el-alert>
            </el-form>

        </div>
        <el-table width="100%" :height="!$store.state.mainSwitch ? tableheight:tableheightBig" :data="dataList" border
            v-loading="dataListLoading" @selection-change="dataListSelectionChangeHandle" style="width: 100%">
            <el-table-column type="selection" width="70">
            </el-table-column>

            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    <span>{{scope.$index+1}}</span>
                    <!-- {{scope.$index+1+(parseInt(params.currentPage)-1)* parseInt(params.currentPageSize) }} -->
                </template>
            </el-table-column>


            <el-table-column prop="groupName" label="分组名称" align="center">
            </el-table-column>


            <el-table-column prop="groupStatus" label="启用状态" align="center" width="200">
                <template slot-scope="scope">
                    <!-- <span v-if="scope.row.groupStatus==1">启用</span> -->
                    <el-tag type="success" v-if="scope.row.groupStatus==1">启用</el-tag>
                    <el-tag type="warning" v-else>禁用</el-tag>
                    <!-- <span v-else>未启用</span> -->
                </template>
            </el-table-column>

            <el-table-column prop="createDate" label="创建时间" align="center" width="200">
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button @click.native.prevent="addOrEditHandle(scope.$index,scope.row)" type="text" size="mini"
                        v-if="$hasPermission('sys:specgroup:update')">编辑</el-button>
                    <el-button class="artdanger" @click.native.prevent="deleteHandleLocal(scope.row.id)" type="text"
                        size="mini" v-if="$hasPermission('sys:specgroup:delete')">删除</el-button>
                    <el-button @click.native.prevent="forbitHandle(scope.$index,scope.row)" type="text" size="mini"
                        v-if="$hasPermission('sys:specgroup:status')">
                        <span v-if="scope.row.groupStatus==1"
                            class="artdisable">{{scope.$index==currentIndex&&forbitLoading?"禁用中..":"禁用"}}</span>
                        <span class="artstart"
                            v-else>{{scope.$index==currentIndex && forbitLoading?"启用中..":"启用"}}</span>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle">
        </el-pagination>

        <!-- 弹窗, 新建 -->
        <addEditData v-if="addEditDataVisible" ref="addEditData" @searchDataList="getDataList"></addEditData>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import {
        statusSpecgroup
    } from '@/api/api'
    import {
        specGroup,
        deleteSpecGroupUrl
    } from '@/api/url'

    import addEditData from './model-add-edit-data'
    import Bread from "@/components/bread"
    import {
        specCheckClass
    } from '@/api/api'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["商品管理", "规格管理", "规格分组"],
                mixinViewModuleOptions: {
                    getDataListURL: specGroup,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteSpecGroupUrl,
                    deleteIsBatch: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: 'id'
                },
                forbitLoading: false,
                currentIndex: -1,
                addEditDataVisible: false,
                dataForm: {
                    groupName: "",
                    groupStatus: '',
                },
                params: {
                    "currentPage": 1, //当前页数
                    "currentPageSize": 10, //每页显示的条数
                    // "roleType" : 2 //角色(1-老师2-学生)
                },
                totalPage: 0,
                statusOptions: [{
                        id: '',
                        name: "全部"
                    },
                    {
                        id: '1',
                        name: "启用"
                    },
                    {
                        id: '2',
                        name: "禁用"
                    }
                ],
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 0
            }
        },
        components: {
            addEditData,
            Bread
        },
        created() {},
        mounted() {
            // this.getDataList();
        },
        watch: {
            '$store.state.mainSwitch'() { //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100)
            }
        },
        methods: {
            // 新建和编辑
            addOrEditHandle(index = -1, row = "") {
                this.setAddEditDataVisible(true);
                this.$nextTick(() => {
                    this.$refs.addEditData.init(row)
                })
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            deleteHandleLocal(id) {
                if (id) {
                    var obj = {
                        id: id
                    }
                    this.deleteHandle(id);

                    // specCheckClass(obj).then((res)=>{
                    //       console.log(res);
                    //       if(res.code ==200 && res.data){
                    //          this.deleteMsg = "该规格下关联的有后台分类，是否确定删除啊?"
                    //       }else{ 
                    //          this.deleteMsg =""
                    //       }
                    // //      // 单个删除
                    // })

                } else {
                    // 批量删除
                    this.deleteHandle();
                }
            },
            // 重置
            reset() {
                this.dataForm = {
                    status: '',
                    groupStatus: '',
                };
                this.getData();
            },
            forbitHandle(index, row) {
                this.currentIndex = index;
                var obj = {
                    "groupStatus": row.groupStatus == 1 ? 2 : 1,
                    "id": row.id,
                }
                var msg = ""
                row.groupStatus == 1 ? msg = "禁用" : msg = "启用"
                this.$confirm('是否' + msg + '该分组?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.forbitLoading = true;
                    statusSpecgroup(obj).then((res) => {
                        this.forbitLoading = false;
                        console.log(res);
                        let status = "error"
                        if (res.code == 200) {
                            this.getDataList();
                            status = "success"
                        }
                        this.$message({
                            message: res.msg,
                            type: status,
                            duration: 1500,
                        })
                    })
                }).catch(() => {});
            }
        }
    }
</script>
<style lang="scss" scoped>
    /deep/.el-table td {
        padding: 7px 0;
    }
</style>