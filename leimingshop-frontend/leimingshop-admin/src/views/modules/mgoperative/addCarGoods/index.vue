<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
                <el-form-item label="商品名称：">
                    <el-input
                        v-model.trim="dataForm.goodsName"
                        placeholder="商品名称"
                        clearable
                        maxlength="300"
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >搜索</el-button
                    >
                    <el-button class="btn" type="primary" plain @click="reset()"
                        >重置</el-button
                    >
                </el-form-item>
                <el-form-item style="float: right">
                    <el-button
                        type="primary"
                        @click="addActivity()"
                        plain
                        v-if="$hasPermission('sys:cart:goods:add')"
                    >
                        添加商品
                    </el-button>
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
                        1、商品推荐为首页推荐的商品，前台展示的排序按照后天排序展示
                    </div>
                    <div class="iconSize">
                        2、如排序一样时先添加的商品排序在前
                    </div>
                </template>
            </el-alert>
        </div>
        <el-form :inline="true" :model="sortDataList">
            <el-table
                :data="sortDataList.list"
                v-loading="dataListLoading"
                @selection-change="dataListSelectionChangeHandle"
                border=""
                style="width: 100%"
                ref="multipleTable"
                :height="
                    !$store.state.mainSwitch ? tableheight : tableheightBig
                "
            >
                <el-table-column
                    type="selection"
                    header-align="center"
                    align="center"
                    width="50"
                ></el-table-column>
                <el-table-column
                    type="index"
                    prop="$index"
                    label="序号"
                    align="center"
                    width="70"
                >
                    <template slot-scope="scope">{{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}</template>
                </el-table-column>
                <!-- <el-table-column
	    	type="index"
		    prop="$index"
				align="center"
		    label="序号"
		    width="70">
		    <template slot-scope="scope">
            {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
            </template>
                </el-table-column>-->

                <el-table-column prop="goodsName" label="商品名称">
                    <template slot-scope="scope">
                        <div
                            style="display: flex; cursor: pointer"
                            @click="previewH5Fn(scope.row)"
                        >
                            <img
                                :src="scope.row.specMainPicture | filterImgUrl"
                                width="40px"
                                height="40px"
                            />
                            <div class="towEllipsis" style="margin-left: 8px">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    :content="scope.row.goodsName"
                                    v-if="scope.row.goodsName.length > 20"
                                    placement="top-start"
                                >
                                    <span
                                        style="
                                            color: #4e80db;
                                            text-decoration: none;
                                            cursor: pointer;
                                        "
                                        >{{ scope.row.goodsName }}</span
                                    >
                                </el-tooltip>
                                <span
                                    v-else
                                    style="
                                        color: #4e80db;
                                        text-decoration: none;
                                        cursor: pointer;
                                    "
                                    >{{ scope.row.goodsName }}</span
                                >
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="category"
                    align="center"
                    label="所属分类"
                ></el-table-column>

                <el-table-column
                    prop="specSellPrice"
                    align="center"
                    label="销售价格"
                    width="200"
                ></el-table-column>
                <el-table-column
                    prop="storage"
                    align="center"
                    label="库存"
                    width="200"
                ></el-table-column>
                <el-table-column
                    prop="address"
                    align="center"
                    label="操作"
                    width="180"
                >
                    <template slot-scope="scope">
                        <el-button
                            class="artdanger"
                            type="text"
                            size="small"
                            @click="deleteHandle(scope.row.id)"
                            v-if="$hasPermission('sys:cart:goods:delete')"
                            >删除</el-button
                        >
                    </template>
                </el-table-column>
                <el-table-column prop="sortNum" label="排序" align="center">
                    <template slot-scope="scope">
                        <div align="center">
                            <el-input-number
                                v-model="scope.row.sortNum"
                                @change="changeSort(scope.row)"
                                :min="0"
                                :precision="0"
                                :step="1"
                                :max="10000000"
                                controls-position="right"
                            ></el-input-number>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
        <!-- 分页 -->
        <div class="footerBox">
            <div class="footerBtn">
                <!-- <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox> -->
                <el-button
                    @click="deleteHandle()"
                    style="margin-left: 20px"
                    type="primary"
                    v-if="$hasPermission('sys:cart:goods:delete')"
                    >批量删除</el-button
                >
                <el-button
                    @click="changeSortSave()"
                    :loading="clicking"
                    type="primary"
                    v-if="$hasPermission('sys:cart:goods:sort')"
                    >保存排序</el-button
                >
            </div>
            <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
            ></el-pagination>
        </div>
        <!-- 商品弹框 -->
        <el-dialog
            title="添加商品"
            :visible.sync="goodsVisible"
            :close-on-click-modal="false"
            :before-close="handleClose"
            width="50%"
        >
            <el-form :inline="true" :model="goodsdataForm">
                <el-form-item label="商品名称：" class="RequisitioNumber1">
                    <el-input
                        v-model="goodsdataForm.goodsName"
                        placeholder="商品名称/商品货号"
                        maxlength="300"
                    ></el-input>
                </el-form-item>
                <el-form-item label="选择分类：">
                    <div class="myCascader">
                        <el-cascader
                            clearable=""
                            v-model="selectedOptions"
                            :options="classList"
                            @active-item-change="handleItemChange"
                            @change="handleChangeOut"
                            :show-all-levels="false"
                        >
                        </el-cascader>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button
                        class="btn"
                        type="primary"
                        @click="getgoodsList(1)"
                        >搜索</el-button
                    >
                    <el-button
                        class="btn"
                        type="primary"
                        plain
                        @click="goodsreset()"
                        >重置</el-button
                    >
                    <el-button
                        class="btn"
                        type="primary"
                        plain
                        @click="handleCheckAllAdd(dataFormList)"
                        >选择全选
                    </el-button>
                </el-form-item>
            </el-form>
            <el-form :inline="true" :model="dataFormList">
                <el-form-item v-for="item in dataFormList.list" :key="item.id">
                    <div
                        :class="[
                            { goodsPropsWrap: item.checked == false },
                            { goodsPropsWrap2: item.checked == true },
                        ]"
                        @click="getCheck(item)"
                    >
                        <div class="goodsImg" @click="getCheck(item)">
                            <img
                                :src="$imgDomain + item.goodsMainPicture"
                                alt=""
                                @click="getCheck(item)"
                            />
                            <img
                                src="~@/assets/images/hook.png"
                                v-show="item.checked == true"
                                @click="getCheck(item)"
                                style="
                                    height: auto;
                                    width: 80px;
                                    position: absolute;
                                    left: 10px;
                                "
                            />
                        </div>
                        <div class="goodsProps">
                            <span class="goodsName">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    :content="item.goodsName"
                                    v-if="item.goodsName.length > 20"
                                    placement="top-start"
                                >
                                    <span class="goodsNameTitle">{{
                                        item.goodsName
                                    }}</span>
                                </el-tooltip>
                                <span v-else class="goodsNameTitle">{{
                                    item.goodsName
                                }}</span>
                                <span class="goodsSellPrice"
                                    >￥{{ item.goodsSellPrice }}</span
                                >
                            </span>
                        </div>
                    </div>
                </el-form-item>
                <div style="margin-right: 406px" class="abc">
                    <el-pagination
                        @size-change="pageSize"
                        @current-change="pageCurrent"
                        :current-page="pages"
                        :page-sizes="[10, 20, 50, 100]"
                        :page-size="limits"
                        :total="totals"
                        layout="total, sizes, prev, pager, next, jumper"
                    ></el-pagination>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center">
                <el-button
                    class="btn"
                    type="primary"
                    plain
                    @click="clearChooseObj"
                    >取消</el-button
                >
                <el-button class="btn" type="primary" @click="closeGoods()"
                    >确定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import { goodsCarlist, goodsCarlistDelete } from "@/api/url";
    import {
        goodsListVisible,
        getGoodscalss,
        addGoodscarList,
        cartConfigSort,
    } from "@/api/api";
    import Bread from "@/components/bread";

    export default {
        mixins: [mixinViewModule],
        components: {
            Bread,
        },
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: goodsCarlist,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/store/export',
                    deleteURL: goodsCarlistDelete,
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                arry1: [],
                dataFormList: {},
                pageDataList: {},
                goodsdataList: [],
                isIndeterminate: false,
                checkAll: false,
                buttonStatus: false,
                goodsVisible: false,
                goodsdataListLoading: false,
                dataForm: {
                    goodsName: "",
                    goodsId: "",
                },
                checkedCount: 0, //当前选中条数
                sortDataList: {},
                goodsdataForm: {
                    goodsName: "",
                    categoryId: "",
                },
                selectedOptions: [],
                props: {
                    label: "gcName",
                    value: "id",
                    children: "children",
                },
                sortList: [],
                dataListSelections: [], //批量选中
                classList: [],
                checkList: [],
                trueList: [],
                pages: 1,
                limits: 10,
                totals: 0,
                breaddata: ["运营管理", "商品推荐配置", "商品推荐配置"],
                clicking: false,
                tableheight: document.body.offsetHeight - 380,
                tableheightBig: 300,
            };
        },
        // ID类搜索框仅可输入数字、英文，最多可输入30个字符
        watch: {
            "dataForm.goodsId": function (newV, oldV) {
                for (let i = 0; i < newV.length; i++) {
                    if (!/[a-zA-Z0-9]/.test(newV[i])) {
                        this.dataForm.goodsId = newV.replace(newV[i], "");
                    }
                }
                if (newV.length > 30) {
                    this.dataForm.goodsId = newV.substr(0, 30);
                }
                s;
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
            dataList(val) {
                console.log(val, "tableData");
                this.sortDataList = {
                    list: val,
                };
            },
        },
        created() {
            // this.getClassList();
            this.demo();
            this.handleItemChange();
        },
        methods: {
            //批量选中数据
            dataListSelectionChangeHandle(val) {
                this.dataListSelections = val;
                if (this.dataListSelections.length == this.dataList.length)
                    this.checkAll = true;
                else this.checkAll = false;
            },
            //批量删除
            cotrolGoodsShow() {
                this.deleteHandle();
            },
            //保存排序
            changeSortSave() {
                if (this.sortList.length == 0) {
                    this.$message({
                        type: "warning",
                        message: "请选择修改排序的数据",
                    });
                    return false;
                }
                const obj = this.sortList;
                this.clicking = true;
                cartConfigSort(obj).then((res) => {
                    if (res.code == 200) {
                        this.clicking = false;
                        this.$message({
                            message: res.msg,
                            type: "success",
                            duration: 1500,
                            onClose: () => {
                                this.sortList = [];
                                this.getDataList();
                            },
                        });
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500,
                            onClose: () => {
                                this.sortList = [];
                                this.getDataList();
                            },
                        });
                    }
                });
            },
            //全选
            handleCheckAllChange(val) {
                if (val) this.$refs.multipleTable.toggleAllSelection();
                else this.$refs.multipleTable.clearSelection();
            },
            //添加商品全选
            handleCheckAllAdd(val) {
                console.log(val);
                // this.goodsVisible = false;
                this.arry1 = [];
                console.log(val);
                for (let i = 0; i < val.list.length; i++) {
                    val.list[i].checked = true;
                }
                val.list.map((item) => {
                    this.arry1.push({
                        goodsId: item.goodsId,

                        category: item.categoty,
                    });
                });
                /*addGoodscarList({ list: JSON.stringify(arry1) }).then(res => {
                            console.log("添加结果", res);
                            if (res.code == 200) {
                                this.$message.success("添加成功");
                                this.dataFormList = {};
                                this.getDataList();
                                done();
                            } else {
                                this.$message.error(res.msg);
                            }
                        });*/
            },
            //更改排序
            changeSort(row) {
                this.sortList.push(row);
            },
            //重置
            reset() {
                this.dataForm = {};
                this.getDataList();
            },
            getData() {
                this.page = 1;
                this.getDataList();
            },
            //打开新增编辑活动弹框
            addActivity(id) {
                this.goodsVisible = true;
                this.goodsreset();
                this.getgoodsList();
            },
            //商品弹框列表查询
            getgoodsList(int) {
                this.goodsdataListLoading = true;
                if(int){
                   this.pages=1
                }
                console.log(1111);
                console.log(this.pageDataList);
                console.log(this.pageDataList.list);
                if (this.pages != 1) {
                    for (let i = 0; i < this.pageDataList.list.length; i++) {
                        if (this.pageDataList.list[i].checked == true) {
                            this.arry1.push(this.pageDataList.list[i]);
                        }
                    }
                }
                this.pageDataList = {};
                this.checkList = [];
                this.trueList = [];
                goodsListVisible({
                    params: {
                        page: this.pages,
                        limit: this.limits,
                        goodsName: this.goodsdataForm.goodsName,
                        categoryId: this.goodsdataForm.categoryId,
                    },
                }).then((res) => {
                    // console.log('数据',res)
                    if (res.code == 200) {
                        this.goodsdataListLoading = false;
                        this.totals = Number(res.data.total);
                        console.log(res.data.list, "0000");
                        this.goodsdataList = res.data.list;

                        this.dataFormList = {
                            list: this.goodsdataList,
                        };
                        this.pageDataList = this.dataFormList;
                        console.log("数据", this.dataFormList.list);
                    }
                });
            },
            //商品弹框重置
            goodsreset() {
                this.pages = 1;
                this.limits = 10;
                this.goodsdataForm = {
                    goodsName: "",
                    categoryId: "",
                };
                this.selectedOptions = [];
                this.getgoodsList();
            },
            //分类联动
            // getClassList() {
            //   var obj={
            //     id:0
            //   };
            //   getGoodscalss(obj).then(res => {
            //     if (res.code == 200) {
            //       this.classList = res.data;
            //       let i, j;
            //       for (i = 0; i < this.classList.length; i++) {
            //         if (this.classList[i].list && this.classList[i].list.length == 0) {
            //           this.classList[i].list = null;
            //         }
            //         if (this.classList[i].list && this.classList[i].list.length > 0) {
            //           let aa = this.classList[i].list;
            //           for (j = 0; j < aa.length; j++) {
            //             if (aa[j].list && aa[j].list.length == 0) {
            //               aa[j].list = null;
            //             }
            //           }
            //         }
            //       }
            //     }
            //   });
            // },
            handleItemChange(val) {
                console.log("active item:", val, typeof val);
                // console.log(val);
                var val1 = [];
                if (val && typeof val == "string") {
                    val1 = JSON.parse(val);
                } else {
                    val &&
                        val.forEach((item, index) => {
                            if (typeof item == "string") {
                                val1.push(JSON.parse(item));
                            } else {
                                val1.push(item);
                            }
                        });
                }
                if (val1)
                    // console.log(val1)
                    var id = 0;
                let takeBlack = true;
                if (Object.prototype.toString.call(val1) === "[object Object]") {
                    id = val1.id ? val1.id : 0;
                } else if (
                    Object.prototype.toString.call(val1) === "[object Array]" &&
                    val1.length > 0
                ) {
                    id = val1[val1.length - 1].id;
                    if (val1.length == 2) takeBlack = false;
                }
                // alert(id);
                var obj = {
                    id: id,
                };
                // console.log(val1.length,Object.prototype.toString.call(val1),takeBlack);
                getGoodscalss(obj).then((res) => {
                    if (res.code == 200) {
                        res.data.forEach((item, index) => {
                            item.label = item.gcName;
                            // item.name = item.gcName
                            takeBlack ? (item.children = []) : "";
                            item.value = JSON.stringify(item);
                            // item.value = item.id
                        });
                        if (!val) {
                            this.classList = res.data;
                        } else {
                            this.classList.forEach((item, index) => {
                                if (item.id == id) {
                                    item.children =
                                        res.data.length > 0 ? res.data : "";
                                }
                                item.children &&
                                    item.children.forEach((item2, index2) => {
                                        if (item2.id == id) {
                                            item2.children =
                                                res.data.length > 0 ? res.data : "";
                                        }
                                    });
                            });
                        }
                    }
                    console.log("分类数据:");
                    console.log(this.classList);
                });
            },
            handleChangeOut(val) {
                console.log(val);
                if (val && val.length > 0) {
                    this.goodsdataForm.categoryId = JSON.parse(
                        val[val.length - 1]
                    ).id;
                } else {
                    this.goodsdataForm.categoryId = "";
                }
            },
            previewH5Fn(row) {
                window.open(
                    window.SITE_CONFIG["pcURL"] +
                        "/goodsDetails?goodsId=" +
                        row.id +
                        "&specId=" +
                        row.specId
                );
            },
            getCheck(item) {
                console.log(item);
                item.checked = !item.checked;
            },
            // 关闭弹框
            handleClose(done) {
                this.goodsVisible = false;
                // if (this.dataFormList.list) {
                //   console.log(this.dataFormList.list)
                //   let arry1 = [];
                //   console.log(arry1.length)
                //     this.dataFormList.list.map(item => {
                //         if(item.checked) arry1.push({
                //       goodsId: item.goodsId,
                //
                //       category: item.categoty
                //     });
                //   });
                //
                //   if (arry1.length == 0) {
                //     done();
                //     return
                //   }
                //   addGoodscarList({ list: JSON.stringify(arry1) }).then(res => {
                //     console.log("添加结果", res);
                //     if (res.code == 200) {
                //       this.$message.success("添加成功");
                //       this.dataFormList = {};
                //       this.getDataList();
                //       done();
                //     } else {
                //       this.$message.error(res.msg);
                //     }
                //   });
                // } else {
                //   done();
                // }
                // done();
            },
            gettem($event, item, index) {
                console.log($event, item, index);
                if ($event) {
                    this.dataFormList.list[index].checked = true;
                    this.checkedCount++;
                } else {
                    this.dataFormList.list[index].checked = false;
                    this.checkedCount--;
                }
                console.log(this.checkedCount, "tiaoshu");
                this.dataFormList.list = this.dataFormList.list;
            },
            //单选商品项
            // gettem($event, item) {
            //   console.log($event, item, index);
            //   this.goodsdataList[index].checked = $event;
            //   this.goodsdataList = [].concat(this.goodsdataList);
            //   // this.$set(this.goodsdataList[index],"checked", $event);
            //   this.checkList.push(item);
            //   let i, j, flag;
            //   if ($event == true) {
            //     for (i = 0; i < this.checkList.length; i++) {
            //       flag = true;
            //       for (j = 0; j < this.trueList.length; j++) {
            //         if (this.checkList[i].idJp == this.trueList[j].idJp) {
            //           flag = false;
            //         }
            //       }
            //       if (flag) {
            //         this.trueList.push(this.checkList[i]);
            //       }
            //     }
            //   } else {
            //     let aa = item.idJp;
            //     this.trueList.map((item, index) => {
            //       if (item.idJp == aa) {
            //         this.trueList.splice(index, 1);
            //       }
            //     });
            //   }
            //   console.log(this.trueList);
            // },
            // 弹框分页, 每页条数
            pageSize(val) {
                this.pages = 1;
                this.limits = val;
                this.getgoodsList();
            },
            handleCheckedCitiesChange(value) {
                let checkedCount = value.length;
                console.log(checkedCount, this.dataList.length);
                this.checkAll = checkedCount === this.dataList.length; //dataList  nodeslist
                this.isIndeterminate =
                    checkedCount > 0 && checkedCount < this.dataList.length; //dataList  nodeslist
                console.log(this.checkAll, this.isIndeterminate);
            },
            // 弹框分页, 当前页
            pageCurrent(val) {
                this.pages = val;
                console.log("分页");
                console.log(val);

                this.getgoodsList();
            },

            demo() {
                function placeholderPic() {
                    var w = document.documentElement.offsetWidth;
                    document.documentElement.style.fontSize = w / 20 + "px";
                }
                placeholderPic();
                window.onresize = function () {
                    placeholderPic();
                };
            },
            clearChooseObj() {
                this.goodsVisible = false;
            },
            closeGoods() {
                if (this.dataFormList.list) {
                    console.log(this.dataFormList.list);
                    console.log("看看arry1");
                    this.arry1 = [];
                    this.dataFormList.list.map((item) => {
                        if (item.checked == true)
                            this.arry1.push({
                                goodsId: item.goodsId,

                                category: item.categoty,
                            });
                    });
                    console.log("再看看arry1");
                    console.log(this.arry1);
                    console.log(JSON.stringify(this.arry1));
                    if (this.arry1.length == 0) {
                        this.goodsVisible = false;
                        return;
                    }
                    addGoodscarList({
                        list: JSON.stringify(this.arry1),
                    }).then((res) => {
                        console.log("添加结果", res);
                        if (res.code == 200) {
                            this.$message.success("添加成功");
                            this.dataFormList = {};
                            this.getDataList();
                            this.goodsVisible = false;
                        } else {
                            this.$message.error(res.msg);
                        }
                    });
                } else {
                    this.goodsVisible = false;
                }
                this.goodsVisible = false;
            },
        },
    };
</script>
<style lang="scss" scoped>
    .footerBox {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    /deep/.el-dialog__body {
        padding: 30px 38px;
        color: #606266;
        font-size: 14px;
    }

    .el-input {
        width: 170px;
        height: 40px;
    }

    .abc {
        /deep/ .el-input {
            width: 90px !important;
        }
    }

    .activiDialog {
        .el-input {
            width: 300px;
        }
    }

    .cell div {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        // overflow: hidden;
    }

    /deep/ .el-table td {
        padding: 5px 0;
    }

    /*/deep/ .cell {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }*/
    .checkbox-div {
        margin-top: 30px;
        margin-bottom: 30px;

        // 未选中样式
        /deep/ .el-checkbox__inner {
            border-radius: 0;
            width: 0px;
            height: 0px;
        }
    }

    /*checkboxClass{*/
    /*    /deep/ .el-checkbox-button__inner{*/
    /*        background-image: $;*/
    /*    }*/
    /*}*/
    /deep/.el-input-number {
        width: 215px;
    }

    /deep/ .el-checkbox__label {
        word-break: normal;
        width: auto;
        display: inline-grid;
        white-space: pre-line;
        word-wrap: break-word;
        overflow: hidden;
        line-height: 14px;
    }

    .el-input--default .el-form--inline .el-input .el-input__inner {
        height: 40px;
        line-height: 35px;
        width: 100%;
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

    .content-text {
        max-width: 100px;
    }

    .el-header,
    .el-footer {
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .goodsImg {
        width: 70px;
        height: 70px;
        margin: auto;

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
        line-height: 20px;
        color: #999999;

        .goodsBrandName {
            text-align: left;
        }
    }

    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 300px;
        display: flex;
        justify-content: space-around;
        align-items: center;
        border: 1px solid #e0e0e0;

        .goodsImg {
            width: 70px;
            height: 70px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsNameTitle {
            display: flex;
            margin-top: 20px;
            width: 200px;
            height: 40px;
            color: #666666ff;
            white-space: normal;
            word-break: break-all;
            word-wrap: break-word;
            overflow: hidden;
        }

        .goodsSellPrice {
            display: flex;
            width: 200px;
            height: 40px;
            margin-top: 20px;
            text-align: left;
        }
    }

    .goodsPropsWrap2 {
        margin: auto;
        height: 80px;
        width: 300px;
        display: flex;
        justify-content: space-around;
        align-items: center;
        border: 1px solid #2589ff;

        .goodsImg {
            width: 70px;
            height: 70px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsNameTitle {
            display: flex;
            margin-top: 20px;
            width: 200px;
            height: 40px;
            color: #666666ff;
            white-space: normal;
            word-break: break-all;
            word-wrap: break-word;
            overflow: hidden;
        }

        .goodsSellPrice {
            display: flex;
            width: 200px;
            height: 40px;
            margin-top: 20px;
            text-align: left;
        }
    }

    .active {
        -webkit-filter: grayscale(100%);
        /* Chrome, Safari, Opera */
        filter: grayscale(100%);
        width: 100%;
        height: 100%;
        background: #6f7180;
    }

    // .RequisitioNumber1 >>> .el-input__inner{
    //     width: 215px;
    //     text-align: left;
    //     margin-right: 60px;

    // }
    // .myCascader >>> .el-cascader__label{
    //     width: 215px;
    //     text-align: left;
    //     margin-right: 60px;
    // }
</style>