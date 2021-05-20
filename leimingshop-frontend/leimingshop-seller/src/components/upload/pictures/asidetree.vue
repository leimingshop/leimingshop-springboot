<template>
    <div v-loading="isLoading" class="comp-tree">
        <!--<el-button class="comp-tr-top" 
			style='margin-bottom:10px;'
			type="primary" 
			size="small" 
			@click="handleAddTop">添加顶级节点</el-button>-->
        <!-- tree -->
        <el-input
            placeholder="输入关键字进行过滤"
            v-model="filterText"
            class="filter-input"
        >
        </el-input>
        <el-button type="primary" size="small" @click="filterTree"
            >筛选</el-button
        >
        <el-tree
            ref="SlotTree"
            :data="setTree"
            :props="defaultProps"
            :expand-on-click-node="true"
            highlight-current
            :default-expanded-keys="openid"
            :node-key="NODE_KEY"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            @node-expand="handleIsExpand"
            @node-collapse="handleIsCollapse"
        >
            <div class="comp-tr-node" slot-scope="{ node, data }">
                <!-- 编辑状态 -->
                <template v-if="node.isEdit">
                    <el-input
                        v-model="data.categoryName"
                        autofocus
                        size="mini"
                        :ref="'slotTreeInput' + data[NODE_KEY]"
                        @blur.stop="handleInput(node, data)"
                        @keyup.enter.native="handleInputEnter(node, data)"
                    ></el-input>
                </template>

                <!-- 非编辑状态 -->
                <template v-else>
                    <!-- 名称： 新增节点增加class（is-new） -->
                    <span
                        :class="[
                            data[NODE_KEY] < NODE_ID_START ? 'is-new' : '',
                            'comp-tr-node--categoryName',
                        ]"
                    >
                        {{ node.label }}
                        {{
                            "(" +
                            (data.pictureCount ? data.pictureCount : 0) +
                            ")"
                        }}
                    </span>
                    <!-- 按钮 -->
                    <span
                        class="comp-tr-node--btns"
                        v-if="data.categoryName != '未分组图片'"
                    >
                        <!-- 新增 -->
                        <el-button
                            icon="el-icon-plus"
                            class="artbtns"
                            size="mini"
                            @click.stop="handleAdd(node, data)"
                        ></el-button>

                        <!-- 编辑 -->
                        <el-button
                            icon="el-icon-edit"
                            class="artbtns"
                            size="mini"
                            @click.stop="handleEdit(node, data)"
                            v-if="data.categoryName != '所有图片数量'"
                        ></el-button>

                        <!-- 删除 -->
                        <el-button
                            icon="el-icon-delete"
                            class="artbtns"
                            size="mini"
                            @click.stop="handleDelete(node, data)"
                            v-if="data.categoryName != '所有图片数量'"
                        ></el-button>
                    </span>
                </template>
            </div>
        </el-tree>
        <el-dialog title="提示！" :visible.sync="dialogVisible" width="30%">
            <p>删除该文件夹同时会删除其子文件夹是否继续？</p>
            <el-radio-group v-model="isDeleteImage">
                <el-radio :label="0">不删除图片</el-radio>
                <el-radio :label="1">删除图片</el-radio>
            </el-radio-group>
            <div slot="footer" class="dialog-footer" style="text-align: center">
                <el-button @click="dialogVisible = false" type="primary" plain
                    >取 消</el-button
                >
                <el-button type="primary" @click="actDeletefileImage"
                    >确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
    //删除数组一个元素
    function arrRemove(arr, value) {
        var i = arr.length;
        while (i--) {
            if (arr[i] === value) {
                return i;
            }
        }
        return false;
    }
    //import mixinViewModule from '@/mixins/view-module'
    //import { getallimages } from '@/api/url'
    import {
        getpicturecategoryallapi,
        addpicturecategory,
        deleteImage,
        updataPictureCategory,
    } from "@/api/api";
    export default {
        //	mixins: [mixinViewModule],
        name: "component-tree",
        data() {
            return {
                url: ``,
                //	      	mixinViewModuleOptions: {
                //	          	getDataListURL: getallimages,
                //	          	getDataListIsPage: true,
                //	          	// exportURL: '/admin-api/log/login/export',
                //	          	deleteURL: '',
                //	          	dataListLoading: false,
                //	          	deleteIsBatch: true,
                //	          	deleteIsBatchKey: 'id'
                //	      	},
                isLoading: false, // 是否加载
                setTree: [], // tree数据
                NODE_KEY: "id", // id对应字段
                MAX_LEVEL: 3, // 设定最大层级
                NODE_ID_START: 0, // 新增节点id，逐次递减
                startId: null,
                startcId: null,
                openid: [], //默认打开的节点id
                defaultProps: {
                    // 默认设置
                    children: "list",
                    label: "categoryName",
                },
                initParam: {
                    // 新增参数
                    categoryName: "未命名文件夹",
                    parentCategoryId: 0, //
                    parentCategoryName: "",
                    list: [],
                },
                parentNodes: [], //当前所在的目录
                isUpdataNode: false,
                isDeleteImage: 0, //是否删除文件   0不删   1删
                dialogVisible: false,
                deleteImageId: "",
                filterText: "",
            };
        },
        computed: {},
        created() {
            // 初始值
            this.startId = this.NODE_ID_START;
            this.startcId = this.NODE_ID_START;
            this.getpicturecategoryall();
        },
        methods: {
            filterTree() {
                this.$refs.SlotTree.filter(this.filterText);
            },
            filterNode(value, data) {
                if (!value) return true;
                console.log(data);
                return data.categoryName.indexOf(value) !== -1;
            },
            //请求
            getpicturecategoryall() {
                getpicturecategoryallapi()
                    .then((data) => {
                        if (data.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        console.log(data.data);
                        if (!data.data[0]) {
                            data.data[0] = {
                                categoryName: "所有图片数量",
                                list: [],
                                pictureCount: 0,
                            };
                        }
                        //添加成功是默认打开添加的所在的目录    把添加的那个id填一下
                        this.$set(data.data[0], "id", "fjid");
                        this.setTree = data.data;
                        this.openid.push("fjid"); //默认打开所有图片
                        //		        console.log(this.setTree);
                    })
                    .catch(() => {});
            },
            handleIsExpand(d, n, s) {
                //点击>和节点name时       节点展开时触发的事件
                this.openid.push(d.id); //开启时   加到数组中
            },
            handleIsCollapse(d, n, s) {
                //点击>和节点name时   节点关闭时触发的事件
                this.openid.splice(arrRemove(this.openid, d.id), 1); //关闭时   从去掉默认打开的目录
            },
            handleNodeClick(d, n, s) {
                //点击节点 参数：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。
                console.log(d, n);
                var idAndCategoryName = {
                    id: d.id,
                    categoryName: d.categoryName,
                };
                //	         console.log(n.expanded)
                this.$emit("showChildNodes", idAndCategoryName);
                this.parentNodes = [];
                this.parentNodes.push(d.categoryName);
                this.findlabel(n.parent); //向上查找父级节点
                d.isEdit = false; //放弃编辑状态
            },
            findlabel(parentNode) {
                if (parentNode) {
                    if (parentNode.label) {
                        //	         	console.log(parentNode.label)
                        this.parentNodes.push(parentNode.label);
                        this.findlabel(parentNode.parent);
                    } else {
                        this.parentNodes.reverse();
                        this.$emit("parentImgBreadcrumb", this.parentNodes); //图片顶部面包屑
                        //	         	console.log(this.parentNodes)
                    }
                }
            },
            handleDelete(_node, _data) {
                // 删除节点
                console.log(_node, _data);
                this.dialogVisible = true;
                this.deleteImageId = _data.id;
            },
            actDeletefileImage() {
                var obj = {
                    //request.js
                    data: {
                        delFlag: this.isDeleteImage, //是否删除目录下的图片 0 不删除，1 删除 ,
                        id: [this.deleteImageId],
                    },
                };
                deleteImage(obj)
                    .then((data) => {
                        if (data.code !== 200) {
                            return this.$message.error(data.msg);
                        } else {
                            this.dialogVisible = false;
                            this.$message({
                                message: this.$t(data.msg),
                                type: "success",
                                duration: 500,
                                onClose: () => {
                                    this.visible = false;
                                    this.$emit("refreshDataList");
                                },
                            });
                        }
                        this.getpicturecategoryall();
                    })
                    .catch(() => {});
            },
            handleInputEnter(_node, _data) {
                // 退出编辑状态
                if (_node.isEdit) {
                    this.$set(_node, "isEdit", false);
                }
            },
            handleInput(_node, _data) {
                // 修改节点
                //			console.log(_node, _data)
                // 退出编辑状态
                if (_node.isEdit) {
                    this.$set(_node, "isEdit", false);
                }
                if (this.isUpdataNode) {
                    //新增节点     形参是父级
                    //				console.log('添加的请求', apiData);
                    var apiData = {
                        categoryName: _data.categoryName,
                        parentCategoryId:
                            _node.parent.data.id == "fjid"
                                ? ""
                                : _node.parent.data.id,
                        parentCategoryName: _node.parent.data.categoryName,
                    };
                    this.$set(apiData, "list", []);
                    addpicturecategory(apiData)
                        .then((data) => {
                            //					console.log(data)
                            if (data.code !== 200) {
                                this.$message.error(data.msg);
                            } else if (data.data == 301) {
                                this.$message.error(data.msg + "，没有添加成功");
                            } else {
                                //添加成功
                                this.$message({
                                    message: this.$t(data.msg),
                                    type: "success",
                                    duration: 500,
                                    onClose: () => {
                                        this.visible = false;
                                        this.$emit("refreshDataList");
                                    },
                                });
                            }
                            this.getpicturecategoryall(); //重新调接口
                        })
                        .catch(() => {});

                    this.isUpdataNode = false; //false  走修改的接口
                } else {
                    //修改节点  形参是自己
                    console.log("修改的请求", _data);
                    var apiData = {
                        categoryName: _data.categoryName,
                        id: _data.id,
                        parentCategoryId:
                            _node.parent.data.id == "fjid"
                                ? ""
                                : _node.parent.data.id,
                        parentCategoryName: _node.parent.data.categoryName,
                    };
                    updataPictureCategory(apiData)
                        .then((data) => {
                            console.log(data);
                            if (data.code !== 200) {
                                this.$message.error(data.msg);
                            } else if (data.data == 201) {
                                this.$message.error(data.msg + "，没有修改成功");
                            } else {
                                //添加成功
                                this.$message({
                                    message: this.$t("修改成功"),
                                    type: "success",
                                    duration: 500,
                                    onClose: () => {
                                        this.visible = false;
                                        this.$emit("refreshDataList");
                                    },
                                });
                            }
                            this.getpicturecategoryall(); //重新调接口
                        })
                        .catch(() => {});
                }
            },
            handleEdit(_node, _data) {
                // 编辑节点
                console.log(_node, _data);
                // 设置编辑状态
                if (!_node.isEdit) {
                    this.$set(_node, "isEdit", true);
                }

                // 输入框聚焦
                this.$nextTick(() => {
                    if (this.$refs["slotTreeInput" + _data[this.NODE_KEY]]) {
                        this.$refs[
                            "slotTreeInput" + _data[this.NODE_KEY]
                        ].$refs.input.focus();
                    }
                });
            },
            handleAdd(_node, _data) {
                // 新增节点
                console.log(_node, _data); //被点击的节点
                // 判断层级
                if (_node.level >= this.MAX_LEVEL) {
                    this.$message.warning(
                        "当前已达到" + this.MAX_LEVEL + "级，无法新增！"
                    );
                    return false;
                }

                // 参数修改
                let obj = JSON.parse(JSON.stringify(this.initParam)); // copy参数
                obj.parentCategoryId = _node.key ? _node.key : "fjid"; // 父id
                obj.parentCategoryName = _node.label ? _node.label : ""; // 父name
                obj[this.NODE_KEY] = --this.startcId; // 节点id：逐次递减id
                //			console.log(obj.parentCategoryId,obj[this.NODE_KEY]);
                // 判断字段是否存在
                console.log(_data);
                if (!_data.list) {
                    this.$set(_data, "list", []);
                }
                // 新增数据
                _data.list.push(obj); //添加进去就会有编辑的状态
                setTimeout(() => {
                    console.log(_node);
                    if (!_node.childNodes[_node.childNodes.length - 1].isEdit) {
                        this.isUpdataNode = true; // 这是新增节点
                        this.handleEdit(
                            _node.childNodes[_node.childNodes.length - 1],
                            _node.childNodes[_node.childNodes.length - 1].data
                        );
                    }
                }, 100);
                // 展开节点
                if (!_node.expanded) {
                    _node.expanded = true;
                    this.openid.push(_data.id); //开启时   加到数组中
                }
            },
            handleAddTop() {
                // 添加顶部节点      重新打开 之前节点还存在   this.startId从0开始了    报错   没事
                let obj = JSON.parse(JSON.stringify(this.initParam)); // copy参数
                obj[this.NODE_KEY] = --this.startId; // 节点id：逐次递减id
                console.log("buyong添加的请求", obj); //--------------------------------------------------
                obj.isEdit = true;
                this.setTree[0].list.push(obj);
            },
        },
    };
</script>

<style lang="scss" scoped>
    /* common */

    // 显示按钮
    .show-btns {
        opacity: 1;
    }

    /* common end */

    .comp-tree {
        width: 100%;
        max-width: 500px;
        max-height: 80vh;
        overflow: auto;
        // 顶部按钮
        .comp-tr-top {
            margin-bottom: 2em;
        }
        // 自定义节点
        .comp-tr-node {
            // label
            .comp-tr-node--name {
                display: inline-block;
                line-height: 40px;
                min-height: 40px;
                // 新增
                &.is-new {
                    font-weight: bold;
                }
            }
            // button
            .comp-tr-node--btns {
                opacity: 0;
                transition: opacity 0.1s;
                .el-button {
                    transform: scale(0.8); // 缩小按钮
                    & + .el-button {
                        margin-left: -1px;
                    }
                }
            }
        }
        // 高亮显示按钮
        .is-current {
            & > .el-tree-node__content {
                .comp-tr-node--btns {
                    @extend .show-btns;
                }
            }
        }
        // 悬浮显示按钮
        .el-tree-node__content {
            &:hover {
                .comp-tr-node--btns {
                    @extend .show-btns;
                }
            }
        }
    }
    //删除的弹窗
    .el-dialog__header {
        background-color: #f2f2f2;
    }
    .el-dialog__body {
        padding-top: 0px;
        padding-bottom: 0px;
    }
    //增删改的按钮
    .el-tree-node__children .el-button {
        background: #f5f7fa;
        border: none;
    }
    //去掉目录border
    /deep/ .el-tree-node__content {
        border: none !important;
    }
    .el-tree-node__content {
        border: none !important;
    }
    //目录超长   可以滑动
    .el-tree-node {
        border: none;
        width: 100%;
    }
    .comp-tr-node {
        border: none;
        width: 100%;
    }
    //按钮间距
    .artbtns {
        padding: 6px 4px;
    }
    .comp-tr-node--categoryName {
        float: left;
        width: 130px;
        line-height: 24px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
    }
    .filter-input {
        width: 168px;
        padding-right: 7px;
        padding-bottom: 7px;
    }
</style>