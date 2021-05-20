<template>
    <div>
        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" ref="dataForm"
            @keyup.enter.native="getDataList()">
            <el-form-item label="收件人类型：">
                <el-select v-model="dataForm.type" placeholder="选择收件人" loading-text="加载中···"
                    @visible-change="getGoodKind2" @change='actselectchange'>
                    <el-option v-for="item in goodKindList1" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="请输入会员名称：" v-if='dataForm.type != 1'>
                <el-input v-model="dataForm.memberName" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <el-form-item label="请输入店铺名称：" v-else>
                <el-input v-model="dataForm.storeName" placeholder="请输入" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" type="primary" @click="getDataList()">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table :height="tableheight" v-if='isshop' :data="dataList" ref='dataList' border style="width: 100%"
            v-loading="dataListLoading" @select="dataListSelectionChangeHandleshop"
            @select-all="dataListSelectionChangeHandleshop">
            <!-- @select = "onTableSelect" -->
            <el-table-column type="selection" width="70" align="center">
            </el-table-column>
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">
                    {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                </template>
            </el-table-column>
            <!--id-->
            <el-table-column prop="storeName" label="店铺名称" align="center">
            </el-table-column>
            <el-table-column prop="account" label="商户账号" align="center">
            </el-table-column>
            <el-table-column prop="createDate" label="注册时间" width="180" align="center">
            </el-table-column>
            <el-table-column prop="gradeName" label="商户等级 " align="center" width="110">
            </el-table-column>
            <el-table-column prop="storeType" label="店铺类型" align="center" width="80" :formatter="formatstoreType">
            </el-table-column>
        </el-table>
        <el-table :height="tableheight" v-else :data="dataList" ref='dataList' border style="width: 100%"
            v-loading="dataListLoading" @select="dataListSelectionChangeHandle"
            @select-all="dataListSelectionChangeHandle">
            <!-- @select = "onTableSelect" -->
            <el-table-column type="selection" width="70" align="center">
            </el-table-column>
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">
                    {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                </template>
            </el-table-column>
            <!--id-->
            <el-table-column prop="memberName" label="账号名称" align="center">
            </el-table-column>
            <el-table-column prop="memberMobile" label="手机号" align="center">
            </el-table-column>
            <!-- <el-table-column prop="nickName" label="昵称" align="center">
            </el-table-column> -->
            <el-table-column prop="createDate" label="注册时间" align="center" width="180">
            </el-table-column>
            <el-table-column prop="memberEmail" label="邮箱" align="center" width="220">
            </el-table-column>
            <!-- <el-table-column prop="memberState" label="登录" width="70" align="center" :formatter="formatmessageType">
            </el-table-column> -->
        </el-table>

        <!-- 分页 -->
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
            :current-page="params.currentPage" :page-sizes="[10, 20, 50, 100]" :page-size="params.currentPageSize"
            :total="total" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

        <div class="grayBtnWarp" style="text-align: center;">
            <div >
                <el-button class="btn" @click='actDoClose'>取消</el-button>
                <el-button class="btn" type="primary" @click='actDoAdd'>确定</el-button>
            </div>
        </div>
    </div>
    </div>
</template>

<script>
    //import list from "./list.vue"
    import mixinViewModule from '@/mixins/view-module'
    import {
        addmessagevip,
        addmessagevipshop
    } from '@/api/url'
    import cloneDeep from 'lodash/cloneDeep'

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: addmessagevip,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: '',
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: 'id'
                },
                goodKind2loading: false,
                goodKind3loading: false,
                dataForm: {
                    type: 0,
                    memberName: '',
                    storeName: '',
                },
                tableData: [],
                goodKindList1: [{
                    id: 0,
                    name: "会员"
                }, {
                    id: 1,
                    name: "店铺"
                }],
                activeName: "first",
                valuetime: '',
                params: {
                    "currentPage": 1, //当前页数
                    "currentPageSize": 10, //每页显示的条数
                },
                vipData: [],
                memberId: [],
                storeList: [],
                shopData: [],
                isshop: false,
                dataListSelections: [],
                tableheight: document.body.offsetHeight - 550,
            };
        },
        // props:['showdata'],
        created() {

        },
        components: {

        },
        mounted() {},
        methods: {
            // onTableSelect(rows, row) {
            // 	let selected = rows.length && rows.indexOf(row) !== -1
            // 		if(selected == false){
            // 			this.$emit("actdodelete",row.id);
            // 		}else{
            // 			if(this.dataForm.type == 1){        //店铺
            // 				this.$emit("actVipNameshop",row);
            // 			}else{    //会员
            // 				this.$emit("actVipNamevip",row);
            // 			}
            // 		}
            // },
            init(vipData, memberId, storeList) {
                this.$nextTick(() => {
                    this.memberId = cloneDeep(memberId);
                    this.storeList = cloneDeep(storeList);
                    this.vipData = cloneDeep(vipData);
                    this.search();
                })
            },
            search() {
                this.getDataList().then((res) => {
                    this.backScanHook();
                });
            },
            //   处理回显数据
            backScanHook() {

                this.dataListSelections = [];
                var specIds = [];
                this.vipData.forEach((item, index) => {
                    specIds[index] = item.id;
                })
                this.dataList.forEach((item, index) => {
                    if (specIds.indexOf(item.id) != -1) {
                        this.dataListSelections.push(item);
                    }
                })
                console.log("回显数据“");
                console.log(this.dataListSelections);
                console.log(this.vipData);
                this.toggleSelection(this.dataListSelections);
            },
            //  处理回显事件
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.dataList.toggleRowSelection(row);
                    });
                }
            },
            reset(dataForm) {
                this.$refs[dataForm].resetFields();
                this.dataForm.memberName = '';
                this.dataForm.strTime = '';
                this.dataForm.endTime = '';
                this.valuetime = '';
            },
            getGoodKind2() {},
            actselectchange(val) {
                this.dataForm.memberName = '';
                //id=1  是店铺  走店铺的接口
                if (val == 1) {
                    this.isshop = true;
                    this.mixinViewModuleOptions.getDataListURL = addmessagevipshop;
                    this.getDataList().then((res) => {
                        this.backScanHook();
                    });
                } else {
                    this.isshop = false;
                    this.mixinViewModuleOptions.getDataListURL = addmessagevip;
                    this.getDataList().then((res) => {
                        this.backScanHook();
                    });
                }
            },
            getGoodKind3() {},
            showDetail(row) {
                this.$emit("showDetail", row);
            },
            //登录
            formatmessageType(row, column) {
                return row.messageType == 1 ? '不允许' : '允许';
            },
            //店铺类型
            formatstoreType(row, column) {
                return row.storeType == 1 ? '自营商户' : '普通商户';
            },
            // 每页数
            sizeChangeHandle(val) {
                this.params.currentPageSize = val;
                this.params.currentPage = 1;
                this.limit = val;
                this.getDataList().then((res) => {
                    this.backScanHook();
                });
            },
            // 当前页
            currentChangeHandle(val) {
                this.params.currentPage = val;
                this.page = val;
                this.getDataList().then((res) => {
                    this.backScanHook();
                });
            },
            //点击取消
            actDoClose() {
                this.$emit("actclose");
            },
            //点击确定   把姓名和id传给父级
            actDoAdd() {
                var memberId = this.memberId;
                var storeList = this.storeList;
                var vipData = this.vipData;
                this.$emit("changeMemberId", memberId);
                this.$emit("changeStoreList", storeList);
                this.$emit("changeVipData", vipData);
                this.$emit("actclose");
            },
            // 非店铺的选择
            dataListSelectionChangeHandle(val) {
                //			this.vipData = val;
                this.changeSelect(val, "vip");
            },
            // 店铺的选择
            dataListSelectionChangeHandleshop(val) {
                //	this.shopData = val;
                this.changeSelect(val, "shop");

            },

            changeSelect(val, type) {
                if (type == "vip") {
                    this.dataList.forEach((item, index) => {
                        var _index = this.memberId.findIndex((item2, index2) => {
                            return item2.id == item.id
                        })
                        if (_index != -1) {
                            this.memberId.splice(_index, 1);
                        }
                    })
                    this.memberId = this.memberId.concat(val);
                } else if (type == "shop") {
                    this.dataList.forEach((item, index) => {
                        var _index = this.storeList.findIndex((item2, index2) => {
                            return item2.id == item.id
                        })
                        if (_index != -1) {
                            this.storeList.splice(_index, 1);
                        }
                    })
                    this.storeList = this.storeList.concat(val);
                }

                this.vipData = this.memberId.concat(this.storeList);
                console.log(val);
                console.log("我就看看this.vipData");
                console.log(this.vipData);

                // var vipData = this.vipData;
                // this.$emit("changeVipData",vipData);
            },
        }
    };
</script>
<style lang="scss" scoped>
    .el-dialog__wrapper {
        display: block !important;
    }

    .grayBtnWarp {
        overflow: hidden;

        .grayBtnWarp-right {
            float: right;

        }

        /*margin:20px 340px 0;
 	display:flex;
 	justify-content: space-around;*/

    }

    /deep/ table {
        width: auto !important;
    }

    /deep/ .el-table__header {
        width: auto !important;
    }

    .grayLine {
        margin-top: 0px;
    }
</style>