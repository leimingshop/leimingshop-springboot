<template>
    <div>
        <div id="control-area">
        <Bread :breaddata="breaddata"></Bread>
        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()">
            <el-form-item label="分类名称：">
                <el-input v-model="dataForm.acName" placeholder="分类名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="是否启用：">
                <el-select v-model="dataForm.status" placeholder="请选择">
                    <el-option v-for="item in statusList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
                <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
            </el-form-item>
        </el-form>

        <el-form>
            <div class="formControlArea">
                <el-form-item style="disply:block;margin-bottom:0px !important;">
                    <el-button type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
                    <el-button type="danger" plain @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
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
                    <div class="iconSize">1、合理创建圈子分类有利于用户的使用</div>
                </template>
            </el-alert>
        </div>
        <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle"
            style="width: 100%;" :height="!$store.state.mainSwitch ? tableheight:tableheightBig">
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
            </el-table-column>
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column prop="sort" label="排序" header-align="center" align="center" width="70"></el-table-column>
            <el-table-column prop="acName" label="分类名称" header-align="center" align="center"></el-table-column>
            <el-table-column prop="imageUrl" label="分类图标" header-align="center" align="center" width="150">
                <template slot-scope="scope">
                    <img style="width: 100px; height: 100px" :src="$imgDomain + scope.row.imageUrl " />
                </template>
            </el-table-column>
            <el-table-column prop="acIcon" label="分类icon" header-align="center" align="center" width="150">
                <template slot-scope="scope">
                    <img style="width: 100px; height: 100px" :src="$imgDomain + scope.row.acIcon " />
                </template>
            </el-table-column>
            <el-table-column prop="attentionNum" label="关注数" header-align="center" align="center" width="90">
            </el-table-column>
            <el-table-column prop="articleNum" label="帖子数" header-align="center" align="center" width="90">
            </el-table-column>
            <el-table-column prop="pvNum" label="阅读量" header-align="center" align="center" width="90"></el-table-column>
            <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center" width="160">
            </el-table-column>
            <el-table-column prop="status" label="是否启用" header-align="center" align="center" width="100">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.status==0">停用</el-tag>
                    <el-tag type="success" v-else>启用</el-tag>
                </template>
            </el-table-column>
            <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}
                    </el-button>
                    <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import AddOrUpdate from './cmsclass-add-or-update'
    import Bread from "@/components/bread"
    import {
        circleClassUrl,
        deleteCircleClassUrl
    } from "@/api/url";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: circleClassUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteCircleClassUrl,
                    deleteIsBatch: true,
                    dataListLoading: false
                },
                imageUrl: '',
                acIcon: '',
                dataForm: {
                    acName: '',
                    status: '',
                },
                breaddata: ["运营", "圈子管理", "圈子分类"],
                statusList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 0,
                        sgName: '停用'
                    },
                    {
                        id: 1,
                        sgName: '启用'
                    }
                ],
                tableheight: 'auto',
                tableheightBig: 0
            }
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
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.limit = 10;
                this.dataForm = {
                    acName: '',
                    status: '',
                };
                // this.dataForm.status = '';
                this.getDataList()
            }
        },
        components: {
            AddOrUpdate,
            Bread
        }
    }
</script>