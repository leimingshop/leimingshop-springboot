<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <!--导出按钮-->
            <!-- <importAndExport style="right: 20px;top: 12px;position: absolute" :importAndExportOptions="importAndExportOptions"
                     :dataForm="dataForm" @getDataList="getDataList"
                     v-if="$hasPermission('sys:goods:check:export')"></importAndExport> -->
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
                class="grayLine topGapPadding"
                :model="dataForm"
            >
                <!-- <el-scrollbar style="height:90px;margin-right: 30px;"> -->
                <el-form-item label="输入搜索：">
                    <el-input
                        v-model="dataForm.goodsName"
                        placeholder="商品名称/商品编号"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="所属店铺：">
                    <el-input
                        v-model="dataForm.storeName"
                        placeholder="店铺名称"
                        clearable
                    ></el-input>
                </el-form-item>

                <el-form-item label="商品品牌：">
                    <!-- <el-select
        v-model="dataForm.brandId"
        placeholder="请选择">
            <el-option
                v-for="item in brandListOption"
                :key="item.id"
                :label="item.brandName"
                :value="item.id">
            </el-option>
        </el-select> -->
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
                    <!-- <el-select
          v-model="dataForm.gcId0"
          placeholder="请选择"
          loading-text="加载中···"
          @visible-change="getGoodsClassFn(1)">
          <el-option
            v-for="item in goodscalssOption0"
            :key="item.id"
            :label="item.gcName"
            :value="item.id">
          </el-option>
        </el-select>
        <span style="color:#999999"> - </span>
        <el-select
        v-model="dataForm.gcId1"
          placeholder="请选择"
          :loading="goodKind2loading"
          loading-text="加载中···"
          @visible-change="getGoodsClassFn(2)">
          <el-option
            v-for="item in goodscalssOption1"
            :key="item.id"
            :label="item.gcName"
            :value="item.id">
          </el-option>
        </el-select>
        <span style="color:#999999"> - </span>
        <el-select
        v-model="dataForm.gcId2"
          placeholder="请选择"
          :loading="goodKind3loading"
          loading-text="加载中···">
          <el-option
            v-for="item in goodscalssOption2"
            :key="item.id"
            :label="item.gcName"
            :value="item.id">
          </el-option>
        </el-select> -->
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
                    <el-button class="btn" type="primary" @click="getData()"
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
                    <el-radio-button label="upper">待审核</el-radio-button>
                    <el-radio-button label="lower">审核拒绝</el-radio-button>
                    <el-radio-button label="not">审核通过</el-radio-button>
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
                        1、在“网址设置-系统设置-审核设置”中，开启商品审核设置后，普通商家发布、编辑商品需要平台管理员审核通过才能正常销售
                    </div>
                    <div class="iconSize">
                        2、审核状态分为：待审核、审核拒绝、审核通过三种状态，审核拒绝需添加备注拒绝原因，商家可再次编辑提交商品审核
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
            <el-table-column
                type="selection"
                :selectable="selectable"
                width="70"
            >
            </el-table-column>

            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>

            <el-table-column label="商品信息" align="center" width="330">
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

            <el-table-column
                prop="price"
                label="销售价/成本价"
                align="center"
                width="120"
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
                prop="storeName"
                label="店铺名称"
                width="180"
                align="center"
            >
            </el-table-column>

            <el-table-column
                prop="gcName"
                label="后台分类"
                width="180"
                align="center"
            >
                <template slot-scope="scope">
                    <div>
                        {{ scope.row.gcName }}
                    </div>
                </template>
            </el-table-column>

            <el-table-column
                prop="goodsStatus"
                label="审核状态"
                align="center"
                width="130"
            >
                <template slot-scope="scope">
                    <el-tag type="waring" v-if="scope.row.goodsStatus == 10"
                        >待审核</el-tag
                    >
                    <el-tag type="success" v-if="scope.row.goodsStatus == 30"
                        >审核通过</el-tag
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
                        <el-tag type="info" slot="reference">审核拒绝</el-tag>
                    </el-popover>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <!--            <el-button @click.native.prevent="addOrEditHandle('sing',scope.row,1)" type="text" size="mini"-->
                    <!--                       v-if="$hasPermission('sys:goods:check:yes')">通过-->
                    <!--            </el-button>-->
                    <!--            <el-button @click.native.prevent="addOrEditHandle('sing',scope.row,0)" type="text" class="artdisable"-->
                    <!--                       size="mini" v-if="$hasPermission('sys:goods:check:no')">拒绝-->
                    <!--            </el-button>-->
                    <el-button
                        v-if="
                            scope.row.goodsStatus != 10 &&
                            $hasPermission('sys:goods:check:yes')
                        "
                        @click.native.prevent="
                            goodsConfirm('sing', scope.row, 1)
                        "
                        type="text"
                        size="mini"
                        >查看
                    </el-button>
                    <el-button
                        v-if="
                            scope.row.goodsStatus == 10 &&
                            $hasPermission('sys:goods:check:yes')
                        "
                        @click.native.prevent="
                            goodsConfirm('sing', scope.row, 1)
                        "
                        type="text"
                        size="mini"
                        >审核
                    </el-button>
                    <el-button
                        @click.native.prevent="showModel('sing', scope.row, 0)"
                        type="text"
                        size="mini"
                        v-if="$hasPermission('sys:goods:check:no')"
                        >操作流水
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="bottomFun">
            <div class="bottomFunLeft">
                <!-- <el-checkbox v-model="checked" @change="change">全选</el-checkbox> -->
                <div class="grayBtnWarp">
                    <el-button
                        @click="addOrEditHandle('batch', {}, 1)"
                        type="primary"
                        v-if="$hasPermission('sys:goods:check:yes')"
                    >
                        通过
                    </el-button>
                    <el-button
                        @click="addOrEditHandle('batch', {}, 0)"
                        type="primary"
                        plain
                        v-if="$hasPermission('sys:goods:check:no')"
                        >拒绝
                    </el-button>
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

        <!-- 操作流水-->
        <el-dialog
            title="操作流水"
            width="50%"
            :visible.sync="dialogVisible"
            :before-close="handleClose"
        >
            <div style="padding: 0 30px">
                <el-table
                    width="100%"
                    height="320"
                    :data="modelList"
                    border
                    v-loading="dataListLoading"
                    style="width: 100%"
                >
                    <el-table-column label="序号" width="70" align="center">
                        <template slot-scope="scope">
                            {{ scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="creator"
                        label="操作人"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column label="操作类型" width="90" align="center">
                        <template slot-scope="scope">
                            <div v-if="scope.row.operationType == 1">发布</div>
                            <div v-else-if="scope.row.operationType == 2">
                                更新
                            </div>
                            <div v-else-if="scope.row.operationType == 3">
                                审核
                            </div>
                            <div v-else-if="scope.row.operationType == 4">
                                审核
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                        label="操作内容"
                        width="140"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div v-if="scope.row.operationType == 1">
                                商品上架申请
                            </div>
                            <div v-else-if="scope.row.operationType == 2">
                                商品更新申请
                            </div>
                            <div v-else-if="scope.row.operationType == 3">
                                审核通过
                            </div>
                            <div v-else-if="scope.row.operationType == 4">
                                审核拒绝
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="createDate"
                        label="操作时间"
                        align="center"
                        width="180"
                    >
                    </el-table-column>
                    <el-table-column prop="price" label="备注" align="center">
                        <template slot-scope="scope">
                            <el-tooltip
                                class="item"
                                effect="dark"
                                placement="top-start"
                            >
                                <div slot="content" class="content-text">
                                    <div>
                                        {{ scope.row.remarks }}
                                    </div>
                                </div>
                                <div class="towEllipsis">
                                    {{ scope.row.remarks }}
                                </div>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false"
                    >返 回</el-button
                >
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import importAndExport from "@/components/import-and-export";
    import { goodsCheckExport } from "@/api/io";

    import { brandListPull, checkGoods } from "@/api/api.js";
    import { getGoodscalss, goodsCheckList } from "@/api/api.js";
    import { goodsVerifyPageUrl } from "@/api/url";
    import modelPreviewH5 from "../modules/model-preview-h5.vue";
    import addEditData from "./model-add-edit-data";
    import Bread from "@/components/bread";
    import { allTreeGoodsclass } from "@/api/api.js";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                brandlist: [],
                activeName: "",
                gcIds: [],
                state4: "",
                breaddata: ["商品", "商品管理", "商品审核"],
                dialogVisible: false,
                goodKind2loading: false,
                goodKind3loading: false,
                mixinViewModuleOptions: {
                    getDataListURL: goodsVerifyPageUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    // deleteURL: deleteAttributeUrl,
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                /*导出*/
                importAndExportOptions: {
                    exportUrl: goodsCheckExport, //导出接口
                    exportSrc: require("@/assets/img/export.png"),
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
                    {
                        id: "",
                        name: "全部",
                    },
                    {
                        id: "10",
                        name: "待审核",
                    },
                    {
                        id: "20",
                        name: "审核未通过",
                    },
                    {
                        id: "30",
                        name: "审核通过",
                    },
                    // {id:"40",name:"违规下架"},
                    // {id:"50",name:"未发布"},
                ],
                previewH5Visible: false,
                modelList: [],
                tableheight: document.body.offsetHeight - 430,
                tableheightBig: 300,
                props: {
                    value: "id",
                    label: "gcName",
                },
            };
        },
        components: {
            addEditData,
            Bread,
            modelPreviewH5,
            importAndExport,
        },
        created() {
            // 获取商品品牌下拉
            this.getBrandListPull();
            // 获取商品分类下拉
            // this.getGoodsClassFn(0);
            // this.handleItemChange();
            // 获取分类的所有树形结构
            this.getallTreeGoodsclassFn();
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
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        mounted() {
            // this.getDataList();
        },
        methods: {
            handleClose(done) {
                done();
            },
            goodsConfirm(type, row) {
                this.$router.push({
                    path: "mggoods-goods-examine-add",
                    query: {
                        gcId: row.id,
                    },
                });
            },
            async showModel(type, row) {
                let param = {
                    goodsId: row.id,
                };
                let result = await goodsCheckList(param);
                if (result.code == 200) {
                    this.modelList = result.data;
                }
                this.dialogVisible = !this.dialogVisible;
            },
            selectable(row, index) {
                if (row.goodsStatus == 20 || row.goodsStatus == 30) {
                    return false;
                } else {
                    return true;
                }
            },
            // 新建和编辑
            addOrEditHandle(type, row = "", inspectType) {
                var ids = [];

                if (type == "batch") {
                    //批量处理
                    // 没有勾选商品
                    if (this.multipleSelection.length == 0) {
                        this.$message({
                            message: "请先选择商品",
                            type: "warning",
                            duration: 1500,
                        });
                        return;
                    }
                    ids = this.getIds();
                } else {
                    //单个处理
                    ids = [row.id];
                }
                this.setAddEditDataVisible(true);
                row.inspectType = inspectType;
                row.ids = ids;
                this.$nextTick(() => {
                    this.$refs.addEditData.init(row);
                });
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
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

            // // 获取品牌下拉列表
            handleSelect(item) {
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
                            message: "请到系统管理-管理中",
                            type: "success",
                            duration: 1500,
                        });
                    });
            },
            finishCange() {
                // console.log("finishCange");
                console.log(this.gcIds);
                let len = this.gcIds.length;
                if (len != -1) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
                // if (this.gcIds.length > 0) {
                //   this.dataForm.gcId = JSON.parse(this.gcIds[this.gcIds.length - 1]).id
                // } else {
                //   this.dataForm.gcId = ''
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
                this.multipleSelection.forEach((item, index) => {
                    if ("object" == typeof item) {
                        ids.push(item.id);
                    } else {
                        ids.push(id);
                    }
                });
                return ids;
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

                this.getDataList();
            },
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";

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

    .content-text {
        max-width: 600px;
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

    /deep/ .el-input__inner {
        padding-right: 0 !important;
    }

    // 商品
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

    // 表格内部纵向分割线颜色
    .el-table--border td,
    .el-table--border th,
    .el-table__body-wrapper .el-table--border.is-scrolling-left ~ .el-table__fixed {
        border-right: 1px solid white;
    }

    // .el-table--border {
    //     margin-top: 8px;
    // }

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