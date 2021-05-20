<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <!-- <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList" v-if="$hasPermission('sys:goods:export')"></importAndExport> -->
            <!--单独的导出按钮-->
            <el-button
                style="
                    right: 20px;
                    top: 12px;
                    position: absolute;
                    padding: 9px 20px;
                "
                v-if="
                    importAndExportOptions && importAndExportOptions.exportUrl
                "
                class="btn"
                @click="exportExcel"
            >
                <img
                    :src="importAndExportOptions.exportSrc"
                    style="width: 20px; height: 20px"
                    alt=""
            /></el-button>
            <el-form
                :inline="true"
                class="grayLine noMargin topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <!-- <el-scrollbar style="height:90px;margin-right: 30px;"> -->
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.goodsName"
                        placeholder="商品名称/商品编号"
                    ></el-input>
                </el-form-item>
                <el-form-item label="所属店铺：">
                    <el-input
                        v-model="dataForm.storeName"
                        placeholder="店铺名称"
                    ></el-input>
                </el-form-item>

                <el-form-item label="商品品牌：">
                    <!-- <el-autocomplete
                        v-model="state4"
                        :fetch-suggestions="getBrandListPull"
                        placeholder="请选择品牌"
                        @blur="handleSelect"
                        @select="handleSelect"
                ></el-autocomplete> -->
                    <el-select
                        v-model="dataForm.brandName"
                        filterable
                        clearable
                        placeholder="请选择品牌"
                    >
                        <el-option
                            v-for="item in brandlist"
                            :key="item.value"
                            :label="item.value"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="商品分类：">
                    <el-cascader
                        clearable=""
                        v-model="gcIds"
                        :options="goodscalssOption"
                        :props="props"
                        @change="finishCange()"
                    ></el-cascader>
                    <!-- @active-item-change="handleItemChange" -->
                </el-form-item>
                <!-- </el-scrollbar> -->
                <el-form-item>
                    <el-button class="btn" type="primary" @click="handleClick()"
                        >查询</el-button
                    >
                    <el-button class="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <el-radio-group
                    v-model="activeName"
                    @change="handleClick"
                    style="margin-top: 5px; margin-bottom: 5px"
                >
                    <el-radio-button label="">全部</el-radio-button>
                    <el-radio-button label="upper">上架</el-radio-button>
                    <el-radio-button label="lower">下架</el-radio-button>
                    <el-radio-button label="not">未上架</el-radio-button>
                </el-radio-group>
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
                        1、商品状态分为：上架、下架、未上架三种状态
                    </div>
                    <div class="iconSize">
                        2、商品列表的商品数据均为审核通过的商品
                    </div>
                    <div class="iconSize">
                        3、未上架和下架商品不在前台显示，商品不能正常出售
                    </div>
                    <div class="iconSize">
                        4、普通商户的商品需审核，自营商户的商品默认审核通过（无需审核）
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column label="序号" width="60" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column label="商品信息" align="center" width="300">
                <template slot-scope="scope">
                    <div class="goodsPropsWrap">
                        <div class="goodsImg">
                            <img
                                :src="scope.row.pictureUrl | filterImgUrl"
                                alt=""
                            />
                        </div>
                        <div class="goodsProps">
                            <span class="goodsName">
                                <label class="goodsNameTitle">名称:</label>
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    :content="scope.row.goodsName"
                                    v-if="scope.row.goodsName.length > 20"
                                    placement="top-start"
                                >
                                    <span
                                        class="goodsNameContent"
                                        @click="previewH5Fn(scope.row)"
                                        >{{ scope.row.goodsName }}</span
                                    >
                                </el-tooltip>
                                <span
                                    v-else
                                    class="goodsNameContent"
                                    @click="previewH5Fn(scope.row)"
                                    >{{ scope.row.goodsName }}</span
                                >
                            </span>
                            <span class="goodsBrand">
                                <label class="goodsNameTitle">品牌:</label>
                                <span class="goodsBrandName">{{
                                    scope.row.brandName
                                }}</span>
                            </span>
                            <span class="goodsNum">
                                <label class="goodsNameTitle">货号:</label>
                                <span class="goodsBrandName">{{
                                    scope.row.goodsSerial
                                }}</span>
                            </span>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="storeName" label="店铺名称" align="center">
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.storeName }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="price"
                label="销售价/成本价"
                width="130"
                align="right"
            >
                <template slot-scope="scope">
                    <div class="priceWrap">
                        <div class="price1">
                            ￥{{ scope.row.specSellPrice }}
                        </div>
                        <div class="price2">
                            ￥{{ scope.row.specCostPrice }}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="price"
                label="商品库存"
                width="90"
                align="right"
            >
                <template slot-scope="scope">
                    <div class="stockWrap">
                        <div class="stock">{{ scope.row.goodsStock }}</div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                prop="gcName"
                label="后台分类"
                width="130"
                align="center"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.gcName }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="上下架状态" width="140" align="center">
                <template slot-scope="scope">
                    <el-tag type="waring" v-if="scope.row.goodsShow == 0"
                        >下架</el-tag
                    >
                    <el-tag type="success" v-if="scope.row.goodsShow == 1"
                        >上架</el-tag
                    >
                    <el-tag type="waring" v-if="scope.row.goodsShow == 2"
                        >未上架</el-tag
                    >
                </template>
            </el-table-column>
            <el-table-column
                prop="createDate"
                label="创建时间"
                width="95"
                align="center"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.createDate }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        @click="goEidt(scope.row)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:goods:edit')"
                        >编辑
                    </el-button>
                    <el-button
                        @click="cotrolGoodsShow('singe', scope.row)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:goods:status')"
                    >
                        <span
                            v-if="
                                scope.row.goodsShow == 0 ||
                                scope.row.goodsShow == 2
                            "
                            class="artstart"
                            >上架</span
                        >
                        <span v-if="scope.row.goodsShow == 1" class="artclose"
                            >下架</span
                        >
                        <!-- <span  v-if="scope.row.goodsShow==2"   style="color:#FF0000">未上架</span> -->
                    </el-button>
                    <el-button
                        @click="editSku(scope.row)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:goods:sku')"
                        >SKU
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="bottomFun">
            <div class="bottomFunLeft">
                <!-- <el-checkbox v-model="checked" @change="change">全选</el-checkbox> -->
                <div class="grayBtnWarp">
                    <el-button
                        type="primary"
                        plain
                        @click="cotrolGoodsShow('batch', 1)"
                        v-if="$hasPermission('sys:goods:status')"
                        >上架
                    </el-button>
                    <el-button
                        type="info"
                        plain
                        @click="cotrolGoodsShow('batch', 0)"
                        v-if="$hasPermission('sys:goods:status')"
                        >下架
                    </el-button>
                    <!--                    <el-button type="danger" plain @click="deleteHandle()">删除</el-button>-->
                </div>
            </div>

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

        <goodsOnOff
            v-if="goodsOnOffVisible"
            ref="goodsOnOffCompon"
            @searchDataList="getDataList"
        ></goodsOnOff>

        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>

<script>
    /*导出商品*/
    import importAndExport from "@/components/import-and-export";
    import { goodsExport } from "@/api/io";
    import goodsOnOff from "./modules/model-goods-on-off.vue";
    import mixinViewModule from "@/mixins/view-module";
    import { goodsPageUrl, deleteGoodsUrl } from "@/api/url";
    import Bread from "@/components/bread";
    // import listItem from "./listItem.vue"
    import { brandListPull } from "@/api/api.js";
    import { getGoodscalss } from "@/api/api.js";
    import { allTreeGoodsclass } from "@/api/api.js";
    import { setTimeout } from "timers";
    import { constants } from "crypto";
    import { goodsUpdateCheckActivity } from "@/api/api.js";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";

    let id = 0;
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                /*导出*/
                importAndExportOptions: {
                    exportUrl: goodsExport, //导出接口
                    exportSrc: require("@/assets/img/export.png"),
                },
                mixinViewModuleOptions: {
                    getDataListURL: goodsPageUrl,
                    activatedIsNeed: false,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteGoodsUrl,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                brandlist: [],
                checked: false,
                multipleSelection: [],
                gcIds: [],
                state4: "",
                breaddata: ["商品", "商品管理", "商品列表"],
                goodKind1loading: false,
                goodKind2loading: false,
                dataForm: {
                    goodsName: "", //商品名称/商品货号
                    storeName: "", //所属店铺
                    brandId: "", //品牌id
                    brandName: "",
                    gcId: "", //分类ID
                    goodsShow: "", //上下架状态:0下架;1上架
                    // gcId0:'',
                    // gcId1:'',
                    // gcId2:'',
                },
                activeName: "",
                brandListOption: [], //品牌列表
                // goodscalssOption0: [],
                // goodscalssOption1: [],
                // goodscalssOption2: [],
                goodscalssOption: [
                    // {
                    //     value: '123',
                    //     label: '江苏2',
                    //     children: []
                    // },
                ],
                dataListLoading: false,
                goodsOnOffVisible: false,
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 430,
                tableheightBig: 300,
                props: {
                    value: "id",
                    label: "gcName",
                },
            };
        },
        components: {
            Bread,
            // listItem,
            goodsOnOff,
            modelPreviewH5,
            importAndExport,
        },
        watch: {
            state4(val) {
                if (!val) {
                    this.dataForm.brandId = "";
                }
            },
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        100;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        created() {
            // 获取商品品牌下拉
            // this.getBrandList();
            // 获取商品分类下拉
            // this.getGoodsClassFn(0);
            // this.handleItemChange();
            // 获取分类的所有树形结构
            this.getallTreeGoodsclassFn();
            this.getBrandListPull();
            // 第一次请求数据
            this.handleClick();
            this.activeName = this.status == undefined ? "" : this.status;
            this.dataForm.goodsShow = this.status == undefined ? "" : this.status;
        },
        mounted() {
            // this.$nextTick(()=>{
            //     this.companTableHeight()
            // })
        },
        methods: {
            // 单独的导出方法
            exportExcel() {
                let that = this;
                var obj = {
                    params: {
                        ...this.dataForm,
                    },
                };
                this.$http
                    .get(`${this.importAndExportOptions.exportUrl}`, obj)
                    .then((res) => {
                        that.$message({
                            message: "请到系统管理-导出管理中下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },

            // // 计算表格高度
            // companTableHeight(){
            //     let that = this;
            //     this.tableheight = document.body.offsetHeight-480;
            //      window.onresize = () => {
            //         return (() => {
            //             this.tableheight = document.body.offsetHeight-480;

            //         })()
            //     }
            // },
            // 获取品牌下拉列表
            getBrandListPull(brandName, cb) {
                var obj = {
                    params: {
                        // page:1,
                        // limit:50,
                        brandName: brandName,
                    },
                };
                brandListPull(obj).then((res) => {
                    if (res.code == 200) {
                        // this.brandListOption =  [{"id":"",brandName:"全部"}].concat(res.data);
                        var showData = [];
                        res.data.forEach((item, index) => {
                            item.value = item.brandName;
                            showData.push(item);
                        });
                        // cb(showData)
                        this.brandlist = showData;
                    }
                });
            },
            // // 获取品牌下拉列表
            handleSelect(item) {
                console.log(item);
                console.log(this.state4);
                if (item.id) {
                    // this.dataForm.brandId = item.id
                    this.dataForm.brandName = item.brandName;
                } else {
                    this.dataForm.brandName = this.state4;
                }
                // this.dataForm.brandName= item.brandName
                // this.dataForm.brandId= item.id
            },
            // 获取分类的所有树形结构
            getallTreeGoodsclassFn() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOption = res.data;
                    } else {
                        // this.$message.error(res.msg);
                    }
                });
            },
            // handleItemChange(val){
            //     console.log('active item:', val, typeof val);
            //     // console.log(val);
            //     var val1 = [];
            //     if(val && typeof(val) == "string"){
            //         val1 =  JSON.parse(val);
            //     }
            //     else{
            //         val &&  val.forEach((item,index)=>{
            //             if(typeof(item) == "string"){
            //                 val1.push(JSON.parse(item))
            //             }else{
            //                 val1.push(item)
            //             }
            //         })
            //     }
            //     if(val1)
            //     // console.log(val1)
            //         var id =0
            //     let takeBlack = true
            //     if(Object.prototype.toString.call(val1) === '[object Object]'){
            //         id = val1.id ?val1.id:0;
            //     }else if(Object.prototype.toString.call(val1) === '[object Array]' && val1.length>0){
            //         id =  val1[val1.length-1].id;
            //         if(val1.length==2) takeBlack = false
            //     }
            //     var obj = {
            //         id:id
            //     }
            //     // console.log(val1.length,Object.prototype.toString.call(val1),takeBlack);
            //     getGoodscalss(obj).then((res)=>{
            //         if(res.code == 200){
            //             res.data.forEach((item,index)=>{
            //                 item.label = item.gcName
            //                 takeBlack?item.children = []:""
            //                 item.value = JSON.stringify(item);
            //                 // item.value = item.id
            //             })
            //             if(!val){
            //                 this.goodscalssOption = res.data;
            //             }else{
            //                 this.goodscalssOption.forEach((item,index)=>{
            //                     if(item.id == id){
            //                         item.children= res.data.length>0?res.data:"";
            //                     }
            //                      item.children &&  item.children.forEach((item2,index2)=>{
            //                         if(item2.id == id){
            //                             item2.children =  res.data.length>0?res.data:"";
            //                         }
            //                     })
            //                 })
            //             }
            //         }
            //         // console.log(this.goodscalssOption);
            //     })
            // },
            finishCange() {
                // console.log("finishCange");
                console.log(this.gcIds);
                let len = this.gcIds.length;
                if (len != -1) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
                // console.log(this.gcIds);
                // if(this.gcIds.length>0){
                //     this.dataForm.gcId = JSON.parse(this.gcIds[this.gcIds.length-1]).id
                //     console.log(JSON.parse(this.gcIds[this.gcIds.length-1]).id);
                // }else{
                //     this.dataForm.gcId="";
                // }
            },
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                if (tab == "") {
                    this.dataForm.goodsShow = "";
                } else if (tab == "upper") {
                    this.dataForm.goodsShow = "1";
                } else if (tab == "lower") {
                    this.dataForm.goodsShow = "0";
                } else if (tab == "not") {
                    this.dataForm.goodsShow = "2";
                }
                this.getDataList();
            },
            agreeChange(val) {
                this.dataForm.goodsShow = val;
                this.getDataList();
            },
            reset() {
                this.gcIds = "";
                this.dataForm.goodsName = ""; //商品名称/商品货号
                this.dataForm.storeName = ""; //所属店铺
                this.dataForm.brandId = ""; //品牌id
                this.dataForm.brandName = "";
                this.dataForm.gcId = ""; //分类ID
                this.gcIds = []; //分类ID
                this.state4 = "";
                // this.dataForm.goodsShow = "";//上下架状态:0下架;1上架
                // this.dataForm.gcId0 = "";//分类ID
                // this.dataForm.gcId1 = "";//分类ID
                // this.dataForm.gcId2 = "";//分类ID
                this.handleClick();
            },
            showSkuListFn(row) {
                this.$emit("showSkuListFn", row);
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.dataListSelectionChangeHandle(val);
            },
            getIds() {
                var ids = [];
                console.log(this.multipleSelection);
                this.multipleSelection.forEach((item, index) => {
                    if ("object" == typeof item) {
                        ids.push(item.id);
                    } else {
                        ids.push(id);
                    }
                });
                return ids;
            },
            // 编辑
            goEidt(row) {
                console.log(row);
                // 查询商品是否参加活动
                let obj = [row.id];
                goodsUpdateCheckActivity(obj).then((res) => {
                    if (res.code == 200) {
                        if (res.data.activityFlag == 0) {
                            // 未参加促销
                            this.$router.push({
                                path: "mggoods-goods-list-add",
                                query: {
                                    gcId: row.id,
                                },
                            });
                        } else if (res.data.activityFlag == 1) {
                            // 促销未开始
                            this.$confirm(res.data.operationMsg, "提示", {
                                cancelButtonText: "取消",
                                confirmButtonText: "确定",
                                type: "warning",
                            })
                                .then(() => {
                                    // 未参加促销
                                    this.$router.push({
                                        path: "mggoods-goods-list-add",
                                        query: {
                                            gcId: row.id,
                                        },
                                    });
                                })
                                .catch(() => {
                                    this.$message({
                                        type: "info",
                                        message: "已取消操作",
                                    });
                                });
                        } else if (res.data.activityFlag == 2) {
                            // 促销进行中
                            this.$message({
                                type: "warning",
                                message: res.data.operationMsg,
                            });
                        } else {
                            this.$message({
                                message: "服务器异常",
                                type: "error",
                                duration: 1500,
                            });
                        }
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                        });
                    }
                });
            },
            // 控制上下架
            cotrolGoodsShow(type, rowOrstatus) {
                var ids = [];
                var goodsShow = 0;
                console.log(this.multipleSelection);
                if (type == "batch") {
                    //批量
                    if (this.multipleSelection.length == 0) {
                        this.$message({
                            message: "请选择商品",
                            type: "warning",
                            duration: 1500,
                        });
                        return;
                    }
                    ids = this.getIds();
                    goodsShow = rowOrstatus;
                } else {
                    //单个
                    ids = [rowOrstatus.id];
                    goodsShow = rowOrstatus.goodsShow == 1 ? 0 : 1;
                }

                this.goodsOnOffVisible = true;
                this.$nextTick(() => {
                    this.$refs.goodsOnOffCompon.init(rowOrstatus, ids, goodsShow);
                });
            },
            editSku(row) {
                this.$emit("showSkuListFn", row);
            },
            // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcURL"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";

    .el-popper {
        left: 292px !important;
    }

    // .el-table--border {
    //     margin-top: 8px;
    // }

    // .el-radio-group {
    //     margin-top: 20px;
    // }
    .orderUerInfo {
        width: 100%;
        height: auto;
        margin-top: 20px;
        border: 1px solid #d1d1d1;
    }

    /* .el-scrollbar__wrap {
                                  overflow-x: hidden;
                                } */
    .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
        white-space: nowrap;
    }

    .el-form--inline .el-form-item {
        margin-right: 76px;
    }

    *::-webkit-scrollbar {
        width: 7px;
        height: 1px;
        background-color: transparent;
    }

    /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    *::-webkit-scrollbar-track {
        background-color: #f0f6ff;
    }

    /*定义滚动条轨道 内阴影+圆角*/
    *::-webkit-scrollbar-thumb {
        background-color: #e3e4e4;
        border-radius: 6px;
    }

    /*定义滑块 内阴影+圆角*/
    .scrollbarHide::-webkit-scrollbar {
        display: none;
    }

    .scrollbarShow::-webkit-scrollbar {
        display: block;
    }

    .el-input {
        width: 170px;
        height: 40px;
    }

    /deep/ .el-tabs__nav-wrap {
        border-bottom: 2px #efefef dotted;

        &::after {
            content: unset;
        }
    }

    /deep/ .el-tabs__nav-scroll {
        height: 60px;
        line-height: 60px;

        /deep/ .el-tabs__item {
            width: 120px;
            // box-sizing: border-box;
            text-align: center;
        }
    }

    /deep/ .el-tabs__active-bar {
        // width: 120px !important;
    }

    .grayBtn {
    }

    // 表头背景和字体颜色
    /deep/ .el-table__header th {
        background: #f5f7fa;
    }

    // 勾选表格复选框居中
    /deep/ tr .cell {
        display: flex;
        justify-content: center;
    }

    // 表内字体默认颜色
    /deep/ .el-table__row {
        color: #999999ff;
        font-size: 14px;
    }

    // 复选框颜色改正
    /deep/ .el-checkbox__input.is-checked .el-checkbox__inner,
    .el-checkbox__input.is-indeterminate .el-checkbox__inner {
        // background-color: #666666 !important;
        // border-color: #666666 !important;
    }

    // 表格内部纵向分割线颜色
    .el-table--border td,
    .el-table--border th,
    .el-table__body-wrapper .el-table--border.is-scrolling-left ~ .el-table__fixed {
        border-right: 1px solid white;
    }

    // 商品
    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 300px;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .goodsImg {
            width: 70px;
            height: 70px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsProps {
            width: 210px;
            height: auto;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            color: #999999;

            .goodsNameTitle {
                display: inline-block;
                width: 50px;
                color: #666666ff;
            }

            .goodsBrandName {
                text-align: left;
            }

            .goodsName {
                display: flex;
                height: 41px;

                .goodsNameContent {
                    width: 150px;
                    text-align: left;
                    text-overflow: -o-ellipsis-lastline;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    line-clamp: 2;
                    -webkit-box-orient: vertical;
                    color: #4e80db;
                    text-decoration: none;
                    cursor: pointer;
                }
            }
        }
    }

    // 价格
    .priceWrap {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 100%;

        .price1 {
            color: $--color-primary;
        }

        div {
            width: 100%;
            text-align: right;
        }
    }

    // 分类

    // 店铺名称

    // 操作
    .btnWrap {
        display: flex;
        justify-content: space-around;
        align-items: center;

        .editWrap {
            cursor: pointer;
        }

        .skuWrap {
            cursor: pointer;
        }

        .btsvg {
            margin-right: 3px;
        }
    }

    .el-table__row:hover {
        .editWrap,
        .skuWrap {
            color: #2260d2;
        }
    }

    .bottomFun {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .bottomFunLeft {
            width: 450px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    }

    .el-radio-group {
        margin-top: 20px;
    }

    /deep/ .el-cascader {
        width: 260px;

        /deep/.el-input {
            width: 100% !important;
        }
    }
</style>