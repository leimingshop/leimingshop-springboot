<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <!--导出按钮-->
            <!-- <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions" :dataForm="dataForm" @getDataList="getDataList"  v-if="$hasPermission('sys:goods:check:export')"></importAndExport> -->
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
                ><img
                    :src="importAndExportOptions.exportSrc"
                    style="width: 20px; height: 20px"
                    alt=""
            /></el-button>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
            >
                <!-- <el-scrollbar style="height:90px;margin-right: 30px;"> -->
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.goodsName"
                        placeholder="商品名称/商品编号"
                    ></el-input>
                </el-form-item>
                <el-form-item label="商品品牌：">
                    <!-- <el-autocomplete
                   v-model="state4"
                  :fetch-suggestions="getBrandListPull"
                  placeholder="请输入品牌"
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
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button class="btn" @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>
            <div class="formControlArea">
                <div>
                    <el-radio-group
                        v-model="activeName"
                        @change="handleClick"
                        style="margin-top: 5px; margin-bottom: 5px"
                    >
                        <el-radio-button label="">全部</el-radio-button>
                        <el-radio-button label="upper">待审核</el-radio-button>
                        <el-radio-button label="lower"
                            >审核拒绝</el-radio-button
                        >
                        <el-radio-button label="not">审核通过</el-radio-button>
                    </el-radio-group>
                </div>
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
                        1、审核状态分为：待审核、审核拒绝、审核通过三种状态
                    </div>
                    <div class="iconSize">
                        2、审核拒绝的商品可查看备注拒绝原因，审核未通过的商品可再次编辑提交商品审核
                    </div>
                    <div class="iconSize">
                        3、审核通过及待审核的商品无任何操作
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            width="100%"
            :data="dataList"
            border
            v-loading="dataListLoading"
            style="width: 100%; margin-top: 8px"
            @selection-change="handleSelectionChange"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <!-- https://blog.csdn.net/princek123/article/details/84377639 -->
            <el-table-column
                type="selection"
                :selectable="selectable"
                width="70"
            >
            </el-table-column>

            <el-table-column label="序号" width="100" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>

            <el-table-column label="商品信息" align="center" width="350">
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
                                    placement="top-start"
                                >
                                    <span
                                        class="goodsNameContent"
                                        @click="previewH5Fn(scope.row)"
                                        >{{ scope.row.goodsName }}</span
                                    >
                                </el-tooltip>
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

            <el-table-column prop="gcName" label="后台分类" align="center">
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.gcName }}
                    </div>
                </template>
            </el-table-column>

            <el-table-column prop="price" label="销售价/成本价" align="center">
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

            <el-table-column prop="goodsStatus" label="审核状态" align="center">
                <template slot-scope="scope">
                    <el-tag type="waring" v-if="scope.row.goodsStatus == 10"
                        >待审核</el-tag
                    >
                    <!-- <el-tag type="info"    v-if="scope.row.goodsStatus==20">审核未通过</el-tag> -->
                    <el-tag type="success" v-if="scope.row.goodsStatus == 30"
                        >审核已通过</el-tag
                    >
                    <el-tag type="waring" v-if="scope.row.goodsStatus == 40"
                        >违规下架</el-tag
                    >
                    <el-tag type="waring" v-if="scope.row.goodsStatus == 50"
                        >未发布</el-tag
                    >

                    <el-popover
                        v-if="scope.row.goodsStatus == 20"
                        placement="top-start"
                        title="拒绝理由"
                        width="200"
                        trigger="hover"
                        :content="scope.row.remarks"
                    >
                        <el-tag type="info" slot="reference">审核未通过</el-tag>
                    </el-popover>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <!-- <div v-if="scope.row.goodsStatus==10">
                 <el-button @click.native.prevent="addOrEditHandle('sing',scope.row,1)" type="primary"size="mini">通过</el-button>
                 <el-button @click.native.prevent="addOrEditHandle('sing',scope.row,0)" type="primary" plain size="mini">拒绝</el-button>
              </div> -->
                    <el-button
                        v-if="
                            scope.row.goodsStatus == 20 ||
                            (scope.row.goodsStatus == 50 &&
                                $hasPermission('sys:goods:check:edit'))
                        "
                        @click="editGoods(scope.row)"
                        type="text"
                        size="mini"
                        >编辑</el-button
                    >
                </template>
            </el-table-column>
        </el-table>
        <!--<div  class="bottomFun">-->
        <!-- <div class="bottomFunLeft">
           <div class="grayBtnWarp">
              <el-button @click="addOrEditHandle('batch',{},1)" type="primary">通过</el-button>
              <el-button @click="addOrEditHandle('batch',{},0)" type="primary" plain>拒绝</el-button>
           </div>
        </div> -->

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

        <!--</div>-->

        <!-- 弹窗, 新建 -->
        <addEditData
            v-if="addEditDataVisible"
            ref="addEditData"
            @searchDataList="getDataList"
        ></addEditData>
        <!-- 预览h5 -->
        <modelPreviewH5
            v-if="previewH5Visible"
            ref="previewH5Compon"
        ></modelPreviewH5>
    </div>
</template>

<script>
    import importAndExport from "@/components/import-and-export";
    import { goodsCheckExport } from "@/api/io";
    import mixinViewModule from "@/mixins/view-module";

    import { brandListPull } from "@/api/api.js";
    import { getGoodscalss } from "@/api/api.js";
    import { goodsVerifyPageUrl } from "@/api/url";

    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";
    import { constants } from "crypto";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";
    import cloneDeep from "lodash/cloneDeep";
    import { allTreeGoodsclass } from "@/api/api.js";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                activeName: "",
                brandlist: [],
                gcIds: [],
                state4: "",
                breaddata: ["商品", "商品管理", "商品审核"],
                goodKind2loading: false,
                goodKind3loading: false,
                /*导出*/
                importAndExportOptions: {
                    exportUrl: goodsCheckExport, //导出接口
                    exportSrc: require("@/assets/img/export.png"),
                },
                mixinViewModuleOptions: {
                    getDataListURL: goodsVerifyPageUrl,
                    getDataListIsPage: true,
                    activatedIsNeed: false,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                addEditDataVisible: false,
                dataForm: {
                    goodsName: "", //商品名称/商品货号
                    storeName: "", //所属店铺
                    brandId: "", //品牌id
                    brandName: "",
                    gcId: "", //分类ID
                    goodsShow: "", //上下架状态:0下架;1上架
                    gcId0: "",
                    gcId1: "",
                    gcId2: "",
                    goodsStatus: "", //审核状态:10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布
                },
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
                multipleSelection: [],
                // 商品审核状态
                goodsStatusOption: [
                    { id: "", name: "全部" },
                    { id: "10", name: "待审核" },
                    { id: "20", name: "审核未通过" },
                    { id: "30", name: "审核通过" },
                    // {id:"40",name:"违规下架"},
                    { id: "50", name: "未发布" },
                ],
                previewH5Visible: false,
                tableheight: document.body.offsetHeight - 380,
                props: {
                    value: "id",
                    label: "gcName",
                },
                tableheightBig: 300,
            };
        },
        components: {
            addEditData,
            Bread,
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
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        activated() {
            if (this.$route.query.goodsStatus) {
                this.dataForm.goodsStatus = this.$route.query.goodsStatus + "";
            }
            this.getDataList();
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
        },
        mounted() {
            // this.getDataList();
        },
        methods: {
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                if (tab == "") {
                    this.dataForm.goodsStatus = "";
                } else if (tab == "upper") {
                    this.dataForm.goodsStatus = "10";
                } else if (tab == "lower") {
                    this.dataForm.goodsStatus = "20";
                } else if (tab == "not") {
                    this.dataForm.goodsStatus = "30";
                }
                this.getDataList();
            },

            // 单独的导出按钮
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
                            message: "请到店铺管理-导出管理中下载",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
            selectable(row, index) {
                console.log(row);
                if (row.goodsStatus == 20 || row.goodsStatus == 30) {
                    return false;
                } else {
                    return true;
                }
            },
            editGoods(row) {
                console.log(row);
                this.$router.push({
                    path: "mggoods-add",
                    query: {
                        gcId: row.id,
                    },
                });
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
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
            //  handleItemChange(val){
            //     console.log('active item:', val, typeof val);
            //     // console.log(val);
            //      var val1 = [];
            //     if(val && typeof(val) == "string"){
            //         val1 =  JSON.parse(val);
            //     }
            //     else{
            //       val &&  val.forEach((item,index)=>{
            //             if(typeof(item) == "string"){
            //                val1.push(JSON.parse(item))
            //             }else{
            //                  val1.push(item)
            //             }
            //        })
            //     }
            //     if(val1)
            //     // console.log(val1)
            //     var id =0
            //     let takeBlack = true
            //     if(Object.prototype.toString.call(val1) === '[object Object]'){
            //          id = val1.id ?val1.id:0;
            //     }else if(Object.prototype.toString.call(val1) === '[object Array]' && val1.length>0){
            //          id =  val1[val1.length-1].id;
            //          if(val1.length==2) takeBlack = false
            //     }
            //     var obj = {
            //          id:id
            //     }
            //     // console.log(val1.length,Object.prototype.toString.call(val1),takeBlack);
            //     getGoodscalss(obj).then((res)=>{
            //           if(res.code == 200){
            //              res.data.forEach((item,index)=>{
            //                  item.label = item.gcName
            //                  takeBlack?item.children = []:""
            //                  item.value = JSON.stringify(item);
            //                 // item.value = item.id
            //              })
            //              if(!val){
            //                 this.goodscalssOption = res.data;
            //              }else{
            //                  this.goodscalssOption.forEach((item,index)=>{
            //                     if(item.id == id){
            //                          item.children= res.data.length>0?res.data:"";
            //                     }
            //                     item.children.forEach((item2,index2)=>{
            //                         if(item2.id == id){
            //                             item2.children =  res.data.length>0?res.data:"";
            //                         }
            //                     })
            //                  })
            //              }
            //           }
            //           // console.log(this.goodscalssOption);
            //       })
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
                //      this.dataForm.gcId = JSON.parse(this.gcIds[this.gcIds.length-1]).id
                //      console.log(JSON.parse(this.gcIds[this.gcIds.length-1]).id);
                // }else{
                //     this.dataForm.gcId="";
                // }
            },
            getData() {
                // var gcId0 = this.dataForm.gcId0
                // var gcId1 = this.dataForm.gcId1
                // var gcId2 = this.dataForm.gcId2
                // this.dataForm.gcId =gcId2?gcId2:(gcId1?gcId1:gcId0)
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.dataListSelectionChangeHandle(val);
            },

            getIds() {
                var ids = [];
                // console.log(this.multipleSelection);
                this.multipleSelection.forEach((item, index) => {
                    if ("object" == typeof item) {
                        ids.push(item.id);
                    } else {
                        ids.push(id);
                    }
                });
                return ids;
            }, // 预览h5
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcUrl"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
            // 重置
            reset() {
                this.dataForm.goodsName = ""; //商品名称/商品货号
                this.dataForm.storeName = ""; //所属店铺
                this.dataForm.brandId = ""; //品牌id
                this.dataForm.brandName = "";
                this.dataForm.gcId = ""; //分类ID
                this.gcIds = []; //分类ID
                this.state4 = "";
                // this.dataForm.gcId0 = "";//分类ID
                // this.dataForm.gcId1 = "";//分类ID
                // this.dataForm.gcId2 = "";//分类ID
                // this.dataForm.goodsStatus = "";//商品审核状态
                this.getData();
            },
        },
    };
</script>
<style lang="scss"  scoped>
    @import "@/element-ui/theme-variables.scss";
    .goodsImg {
        width: 70px;
        height: 70px;
        margin: auto;
        img {
            width: 100%;
            height: 100%;
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
    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 330px;
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

    .el-radio-group {
        margin-top: 20px;
    }
    .el-form--inline {
        /deep/.el-input {
            height: 40px !important;
        }
    }
</style>
