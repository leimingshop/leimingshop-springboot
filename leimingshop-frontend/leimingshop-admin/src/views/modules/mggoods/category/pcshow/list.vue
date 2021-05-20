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
                                v-if="$hasPermission('sys:show:class:save')"
                                >新增展示分类</el-button
                            >
                            <el-button
                                type="danger"
                                plain
                                @click="deletRowsFn()"
                                v-if="$hasPermission('sys:show:class:delete')"
                                >批量删除</el-button
                            >
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
                                1、PC展示分类用于PC端用户筛选商品时使用，请事先合理规划您的展示分类及层级关系后再进行操作
                            </div>
                            <div class="iconSize">
                                2、展示分类和后台分类的关联关系为多对多，展示分类的图片会显示在移动端分类列表
                            </div>
                            <div class="iconSize">
                                3、分类建立后可通过点击一二级分类进入其下级分类并进行相关操作。再次点击一二级分类可收起子分类
                            </div>
                            <div class="iconSize">
                                4、展示分类只有创建到三级才会在前台显示，仅创建到二级的前台不会显示
                            </div>
                            <div class="iconSize">
                                5、状态分为启用和禁用，只有启用状态的展示分类才会显示
                            </div>
                        </template>
                    </el-alert>
                </el-form-item>
                <el-button
                    style="right: 12px; top: 12px; position: absolute"
                    @click="exportTemplate()"
                    >下载模板</el-button
                >
                <el-upload
                    class="import-btn"
                    :action="goodsClassCustomImport"
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
            :children="'goodsClassListDTO'"
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
    import cloneDeep from "lodash/cloneDeep";
    import mixinViewModule from "@/mixins/view-module";
    import TableTreeColumn from "@/components/table-tree-column";
    import qs from "qs";
    import { goodsClassCustomExport } from "@/api/io";
    import { goodsClassCustomImport } from "@/api/io";

    import { categoryUrl } from "@/api/url";
    import { deleteGoodsclasscustomUrl } from "@/api/url";

    import { goodsclasscustomUpdateShow } from "@/api/api";

    import { treeDataTranslate } from "@/utils";
    import Bread from "@/components/bread";
    import MyTableTree from "@/components/treeTable/MyTableTree.vue";
    import Cookies from "js-cookie";
    let id = 1000;
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                fileList: [],
                table: [],
                value: [],
                addForm: {},
                listOrAdd: true,
                breaddata: ["商品", "类目管理", "展示分类"],
                mixinViewModuleOptions: {
                    activatedIsNeed: false,
                    getDataListURL: "/admin-api/goods/classcustom/all?showType=1",
                    getDataListIsPage: true,
                    deleteURL: deleteGoodsclasscustomUrl,
                    deleteIsBatch: true,
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                goodsClassCustomImport: goodsClassCustomImport,
                gridData: [], //品牌table
                specData: [], //规格table
                attrData: [], //属性table
                formLabelWidth: "120px",
                treeConfig: {
                    //等于 el-tree 的选项配置
                    options: {
                        "show-checkbox": true,
                    },
                    title: "展示分类",
                    //自定义表格列 目前只支持文本渲染
                    columns: [
                        {
                            label: "后台分类",
                            prop: "className",
                            span: 4,
                        },
                        {
                            label: "商品数量",
                            prop: function (a, data) {
                                if (data.goodsNum) {
                                    return data.goodsNum;
                                } else {
                                    return 0;
                                }
                            },
                            span: 3,
                        },

                        // {
                        //     label: '类目管理',
                        //     prop: 'label',
                        //     span: 4
                        // },

                        {
                            label: "状态",
                            prop: function (a, data) {
                                console.log(data);
                                if (data.showFlag == 1) {
                                    return <el-tag type="success"> 启用 </el-tag>;
                                } else {
                                    return <el-tag type="info">禁用</el-tag>;
                                }
                            },
                            span: 3,
                        },
                        {
                            label: "创建时间",
                            prop: "createDate",
                            span: 4,
                        },
                        {
                            label: "排序",
                            prop: function (a, data) {
                                if (data.sort) {
                                    return data.sort;
                                } else {
                                    return 0;
                                }
                            },
                            span: 2,
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
                            prop: this.editBtFn, //支持函数返回html 和 文本字符串
                            action: this.eidtRowFn, //按钮点击触发的函数 回调函数是该行的row
                        },
                        {
                            type: "", //同el-button 的 type
                            prop: this.deleteBtFn, //支持函数返回html 和 文本字符串
                            action: this.deleteRowFn, //按钮点击触发的函数 回调函数是该行的row
                        },
                        {
                            type: "", //同el-button 的 type
                            prop: this.judgeStatusFn, //支持函数返回html 和 文本字符串,
                            action: this.forbitFn, //按钮点击触发的函数 回调函数是该行的row
                            className: "forbitClass",
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

                        console.log("treeTable数据:");
                        console.log(this.treeConfig.rows);

                        var dataArray = JSON.stringify(this.treeConfig.rows);
                        var dataArrayStr = dataArray.replace(/id/g, "value");
                        dataArrayStr = dataArrayStr.replace(/\[]/g, '""');
                        dataArray = JSON.parse(dataArrayStr);
                        // 只要一二级，不要三级，因为新建分类时，不能再三级下面挂
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
                        console.log(dataArray2);
                    }
                });
            },
            // 模板下载
            exportTemplate() {
                var params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                    modelId: id,
                });
                window.location.href = `${goodsClassCustomExport}?${params}`;
            },
            // 新建和编辑
            addOrEditHandle(row = "") {
                this.$emit("hiddenList", row);
            },
            nextLevel(row) {
                if (!this.$hasPermission("sys:category:show:save")) {
                    return "";
                }
                if (row.level == 3) {
                    return "";
                } else {
                    return "新增下级";
                }
            },
            //编辑
            editBtFn() {
                if (!this.$hasPermission("sys:category:show:update")) {
                    return "";
                }
                return "编辑";
            },
            //删除
            deleteBtFn() {
                if (!this.$hasPermission("sys:category:show:delete")) {
                    return "";
                }
                return "删除";
            },
            // 新增下级
            addRowFn(row) {
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
            judgeStatusFn(data) {
                if (!this.$hasPermission("sys:category:show:disable")) {
                    return "";
                }
                //  前台展示（0不展示，默认为1展示） ,
                if (data.showFlag == 1) {
                    return "<span class='artdisable'>禁用</span>";
                } else {
                    return "<span class=' artstart'>启用</span>";
                }
            },
            showJudgeStatusFn(data) {
                if (data.showFlag == 1) {
                    return "<span class='artstart'>启用</span>";
                } else {
                    return "<span class='artdisable'>禁用</span>";
                }
            },
            // 启用禁用
            forbitFn(arguRow) {
                console.log(arguRow);
                var row = cloneDeep(arguRow);
                if (row.showFlag) {
                    row.showFlag = 0;
                } else {
                    row.showFlag = 1;
                }
                var obj = {
                    params: {
                        id: row.id,
                        showFlag: row.showFlag,
                    },
                };
                var msg = "";
                row.showFlag == 0 ? (msg = "禁用") : (msg = "启用");
                this.$confirm("是否" + msg + "该分类?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        goodsclasscustomUpdateShow(obj).then((res) => {
                            let status = "error";
                            if (res.code == 200) {
                                status = "success";
                                this.getTree();
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1000,
                            });
                        });
                    })
                    .catch(() => {});
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
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },
            handleExceed(files, fileList) {
                this.$message.warning(
                    `当前限制选择 1 个文件，本次选择了 ${
                        files.length
                    } 个文件，共选择了 ${files.length + fileList.length} 个文件`
                );
            },
            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${file.name}？`);
            },
            ////
            beforeUpload(file) {
                const that = this;
                let fd = new FormData();
                let params = qs.stringify({
                    "ADMIN-TOKEN": Cookies.get("ADMIN-TOKEN"),
                    modelId: id,
                });
                fd.append("files", file); //传文件
                fd.append("params", params); //传其他参数
                this.$http
                    .post(goodsClassCustomImport, fd)
                    .then(({ data: res }) => {
                        console.log(res);
                        if (res.code == 200) {
                            that.$message.success("导入成功");
                            this.getTree();
                        } else {
                            that.$message.success(res.msg);
                        }
                    });
                return false; //屏蔽了action的默认上传
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