<template>
    <div v-if="listOrAdd">
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form>
                <el-form-item style="margin-bottom: 0px !important">
                    <div class="formControlArea">
                        <div>
                            <el-button
                                type="primary"
                                @click="addOrEditHandle()"
                                v-if="$hasPermission('sys:backstage:save')"
                            >
                                新增后台分类
                            </el-button>
                            <el-button
                                type="danger"
                                plain
                                @click="deletRowsFn()"
                                v-if="$hasPermission('sys:backstage:delete')"
                            >
                                批量删除
                            </el-button>
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
                                1、商品分类用于商家发布商品以及用户在商城导航索引商品时使用，请事先合理规划您的商品分类及层级关系后再进行操作。
                            </div>
                            <div class="iconSize">
                                2、分类建立后可通过点击一二级分类进入其下级分类并进行相关操作。再次点击一二级分类可收起子分类
                            </div>
                            <div class="iconSize">
                                3、新增时请按照提示信息逐步进行操作完成分类的添加，后台分类可关联品牌、规格、属性
                            </div>
                            <div class="iconSize">
                                4、如果您的商城正在运营中，因商铺、商品、分润结算等重要数据都与商品分类相关，因此尽量使用“编辑”而避免“删除”操作，这有可能引起商品、店铺以及结算等数据的严重错误！
                            </div>
                        </template>
                    </el-alert>
                </el-form-item>
                <el-button
                    style="right: 12px; top: 12px; position: absolute"
                    @click="getfilename()"
                    >下载模板</el-button
                >
                <el-upload
                    class="import-btn"
                    :action="goodsClassImport"
                    :before-upload="beforeUpload"
                    accept="xlsx,xls"
                    :show-file-list="false"
                    style="height: 0px"
                >
                    <el-button
                        style="right: 120px; top: 12px; position: absolute"
                        ><i class="icon iconfont icon-piliangdaoru"></i
                        >导入</el-button
                    >
                </el-upload>
            </el-form>
        </div>

        <!-- <el-table :data="table" empty-text style="width: 100%">
      <el-table-column prop="date" label="分类管理" width="550"></el-table-column>
      <el-table-column prop="name" label="排序" width="180"></el-table-column>
      <el-table-column prop="address" label="操作"></el-table-column>
    </el-table> -->

        <MyTableTree
            v-loading="dataListLoading"
            :children="'children'"
            :label="'gcName'"
            :mate="treeConfig"
            ref="MyTree"
            @check-change="checkChange"
            @node-click="nodeClick"
            @current-change="currentChange"
        >
        </MyTableTree>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import TableTreeColumn from "@/components/table-tree-column";

    // import { categoryUrl } from "@/api/url";
    // import { goodsclassPageUrl } from "@/api/url";
    import { allGoodsclassUrl } from "@/api/url";
    import { deleteGoodsclassUrl } from "@/api/url";
    import { goodsClassImport } from "@/api/io";
    import { goodsClassTemplateEx } from "@/api/io";
    // import { backstageList,goodsclassPage } from "@/api/api";
    // import { treeDataTranslate } from "@/utils";
    import Bread from "@/components/bread";
    import MyTableTree from "@/components/treeTable/MyTableTree.vue";
    import Cookies from "js-cookie";
    import qs from "qs";
    let id = 1000;
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                importAndExportOptions: {
                    importUrl: goodsClassImport, //导入接口
                    importWord: "导入",
                },
                table: [],
                value: [],
                addForm: {},
                listOrAdd: true,
                breaddata: ["商品", "类目管理", "后台分类"],

                mixinViewModuleOptions: {
                    activatedIsNeed: false,
                    getDataListURL: allGoodsclassUrl,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: deleteGoodsclassUrl,
                    deleteIsBatch: true,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                gridData: [], //品牌table
                specData: [], //规格table
                attrData: [], //属性table
                formLabelWidth: "120px",
                goodsClassImport: goodsClassImport,
                treeConfig: {
                    //等于 el-tree 的选项配置
                    options: {
                        "show-checkbox": true,
                    },
                    title: "后台分类",
                    //自定义表格列 目前只支持文本渲染
                    columns: [
                        // {
                        //       label: '序号',
                        //       prop: 'seq',
                        //       span: 8
                        //   },
                        {
                            label: "排序",
                            prop: function (a, data) {
                                if (data.sort) {
                                    return data.sort;
                                } else {
                                    return 0;
                                }
                            },
                            span: 3,
                        },
                        {
                            label: "分类类型",
                            prop: function (a, data) {
                                if (data.level == 3 && data.classType == 0) {
                                    return "实体商品分类";
                                } else if (data.level == 3 && data.classType == 1) {
                                    return "虚拟商品分类";
                                } else {
                                    return "--";
                                }
                            },
                            span: 4,
                        },
                    ],
                    //操作按钮列表
                    actions: [
                        {
                            type: "", //同el-button 的 type
                            prop: this.nextLevel, //支持函数返回html 和 文本字符串
                            action: this.addRowFn, //按钮点击触发的函数 回调函数是该行的row
                        },
                        {
                            type: "", //同el-button 的 type
                            prop: this.editBtFn, // "编辑",  //支持函数返回html 和 文本字符串
                            action: this.eidtRowFn, //按钮点击触发的函数 回调函数是该行的row
                        },
                        {
                            type: "", //同el-button 的 type
                            prop: this.deleteBtFn, //"删除",  //支持函数返回html 和 文本字符串
                            action: this.deleteRowFn, //按钮点击触发的函数 回调函数是该行的row
                        },
                    ],
                    //tree 的数据来源
                    rows: [],
                },
            };
        },
        components: {
            TableTreeColumn,
            Bread,
            MyTableTree,
        },
        created() {
            this.getTree();
        },
        mounted() {},
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },

        methods: {
            //导入
            beforeUpload(file) {
                const that = this;
                let fd = new FormData();
                let params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                    modelId: id,
                });
                fd.append("files", file); //传文件
                fd.append("params", params); //传其他参数
                this.$http.post(goodsClassImport, fd).then(({ data: res }) => {
                    console.log(res);
                    if (res.code == 200) {
                        that.$message.success("导入成功");
                        this.getTree();
                    } else {
                        that.$message.error("导入失败");
                    }
                });
                return false; //屏蔽了action的默认上传
            },
            handleChange(value) {
                console.log(value);
            },
            init() {
                this.getTree();
            },
            getTree() {
                this.getDataList().then((res) => {
                    //Promise后 对数据格式进行处理
                    if (res.code == 200) {
                        var data = res.data;
                        let seq = 0;
                        data.forEach((item1, index1) => {
                            item1.seq = ++seq;
                            item1.level = 1;
                            item1.children &&
                                item1.children.forEach((item2, index2) => {
                                    item2.level = 2;
                                    item2.seq = seq + "-" + (index2 + 1);
                                    item2.children &&
                                        item2.children.forEach((item3, index3) => {
                                            item3.level = 3;
                                            item3.seq =
                                                seq +
                                                "-" +
                                                (index2 + 1) +
                                                "-" +
                                                (index3 + 1);
                                        });
                                });
                        });
                        //处理树形数据
                        // this.treeConfig.rows =  data;
                        var dataStr = JSON.stringify(data);
                        dataStr = dataStr.replace(/gcName/g, "label");
                        dataStr = dataStr.replace(/children/g, "children");
                        this.treeConfig.rows = [].concat(JSON.parse(dataStr));
                        var dataStr = JSON.stringify(data);
                        dataStr = dataStr.replace(/gcName/g, "label");
                        dataStr = dataStr.replace(/children/g, "children");
                        this.treeConfig.rows = [].concat(JSON.parse(dataStr));

                        var dataArray = JSON.stringify(this.treeConfig.rows);
                        var dataArrayStr = dataArray.replace(/id/g, "value");
                        dataArrayStr = dataArrayStr.replace(/\[]/g, '""');
                        dataArray = JSON.parse(dataArrayStr);
                        // 只要一二级，不要三级，因为新建分类时，不能再三级下面挂
                        // console.log("treeTable数据:");
                        // console.log(dataArrayStr);
                        var dataArray2 = [];
                        dataArray.forEach((item, index) => {
                            if (item.children) {
                                item.children.forEach((item2, index2) => {
                                    item2.children = "";
                                });
                            }
                            dataArray2.push(item);
                        });

                        this.$parent.dataArray = dataArray2;
                    }
                });
            },
            // 下载分类的模板
            getfilename() {
                var params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                });
                window.location.href = `${goodsClassTemplateEx}?${params}`;
            },
            // 从模板当中导入
            importExcel() {},
            // 新建和编辑
            addOrEditHandle(row = "") {
                this.$emit("hiddenList", row);
            },
            nextLevel(row) {
                if (!this.$hasPermission("sys:backstage:save")) {
                    return "";
                }
                if (row.level == 3 || !row.lowerLevel) {
                    return "";
                } else {
                    return "新增下级";
                }
            },
            //编辑
            editBtFn() {
                if (!this.$hasPermission("sys:backstage:update")) {
                    return "";
                }
                return "编辑";
            },
            //删除
            deleteBtFn() {
                if (!this.$hasPermission("sys:backstage:delete")) {
                    return "";
                }
                return "删除";
            },
            // 新增下级
            addRowFn(row) {
                console.log(111111111111111111111111111111);
                console.log(row);
                row.type = "addNext";
                this.addOrEditHandle(row);
            },
            // 编辑回调
            eidtRowFn(row) {
                console.log(row);
                row.type = "edit";
                this.addOrEditHandle(row);
            },
            // 删除回调
            deleteRowFn(row) {
                console.log(row);
                this.dataListSelections = [row];
                this.deleteHandle("", false).then((res) => {
                    console.log(res);
                    if (res == "ok") {
                        this.getTree();
                    }
                });
            },

            deletRowsFn() {
                console.log(this.$refs.MyTree.$refs.myTreeGrid.getCheckedNodes());
                console.log(this.$refs.MyTree.$refs.myTreeGrid.getCheckedKeys());
                var arr = this.$refs.MyTree.$refs.myTreeGrid.getCheckedNodes();
                if (arr.length != 0) {
                    this.dataListSelections = arr;
                    this.deleteHandle("", false).then((res) => {
                        if (res == "ok") {
                            this.getTree();
                        }
                    });
                }
            },
            checkChange(list) {
                console.log("check-change");
                console.log(list);
            },
            nodeClick(list) {
                console.log("node-click");
                console.log(list);
            },
            currentChange(list) {
                console.log("current-change");
                console.log(list);
            },
        },
    };
</script>
<style>
    /* .el-table__empty-block {
              display: none;
            } */
    .el-tree-node__content {
        border: 1px solid #d1d1d1;
    }

    .nums {
        width: 100px;
        display: inline-block;
        margin-left: 80px;
    }

    .operation {
        width: 500px;
        display: inline-block;
        text-align: center;
        margin-left: 200px;
    }

    .nums_level {
        min-width: 450px;
        display: inline-block;
    }

    /* .el-tree-node{padding-left: 80px;} */
</style>