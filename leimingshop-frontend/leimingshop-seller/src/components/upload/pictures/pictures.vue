<template>
    <div class="picturesClass">
        <!-- <Bread :breaddata="breaddata"></Bread> -->
        <el-form
            :inline="true"
            class="grayLine topGapPadding"
            :model="dataForm"
            ref="dataForm"
        >
            <el-form-item label="图片名称：">
                <el-input
                    v-model="dataForm.pictureName"
                    placeholder="请输入图片名称"
                    clearable
                ></el-input>
            </el-form-item>
            <el-form-item label="文件夹名称：">
                <el-input
                    v-model="dataForm.categoryName"
                    placeholder="请输入文件夹名称"
                    clearable
                ></el-input>
            </el-form-item>
            <el-form-item label="上传日期：">
                <el-date-picker
                    v-model="valuetime"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd"
                    @blur="acttime"
                    :picker-options="pickerOptions2"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" type="primary" @click="actgetDataList()"
                    >查询</el-button
                >
                <el-button
                    class="btn"
                    @click="reset('dataForm')"
                    type="primary"
                    plain
                    >重置</el-button
                >
            </el-form-item>
        </el-form>

        <el-container>
            <el-container class="artcontent">
                <el-aside width="250px">
                    <aside-tree
                        ref="reftree"
                        @parentImgBreadcrumb="ImgBreadcrumb"
                        @showChildNodes="showChildNodes"
                    ></aside-tree>
                </el-aside>
                <el-main>
                    <div class="arttop">
                        <el-button plain
                            ><img
                                class="artimggroup"
                                src="~@/assets/img/imggroup.jpg"
                                alt=""
                        /></el-button>
                        <el-dropdown @command="handleCommand">
                            <span class="el-dropdown-link">
                                <!--{{contentmenu}}<i class="el-icon-arrow-down el-icon--right"></i>-->
                                <i
                                    ><img
                                        class="artimggroup"
                                        src="~@/assets/img/asc.png"
                                        alt=""
                                /></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item
                                    command="上传时间降序"
                                    value="asc"
                                    ><span
                                        ><img
                                            v-if="ascdesc == 1"
                                            class="artimggroup"
                                            src="~@/assets/img/yes.png"
                                            alt="" /></span
                                    >上传时间降序</el-dropdown-item
                                >
                                <el-dropdown-item
                                    command="上传时间升序"
                                    value="asc"
                                    ><span
                                        ><img
                                            v-if="ascdesc == 2"
                                            class="artimggroup"
                                            src="~@/assets/img/yes.png"
                                            alt="" /></span
                                    >上传时间升序</el-dropdown-item
                                >
                                <el-dropdown-item
                                    command="文件名降序"
                                    value="asc"
                                    ><span
                                        ><img
                                            v-if="ascdesc == 3"
                                            class="artimggroup"
                                            src="~@/assets/img/yes.png"
                                            alt="" /></span
                                    >文件名降序</el-dropdown-item
                                >
                                <el-dropdown-item
                                    command="文件名升序"
                                    value="asc"
                                    ><span
                                        ><img
                                            v-if="ascdesc == 4"
                                            class="artimggroup"
                                            src="~@/assets/img/yes.png"
                                            alt="" /></span
                                    >文件名升序</el-dropdown-item
                                >
                            </el-dropdown-menu>
                        </el-dropdown>
                        <div class="artbtn">
                            <el-upload
                                class="upload-demo"
                                :action="url"
                                :show-file-list="false"
                                :http-request="httpRequest"
                                multiple
                                :before-upload="beforeAvatarUpload"
                            >
                                <el-button
                                    class="btn"
                                    type="primary"
                                    @click="actAddImgsBtn"
                                    >上传图片</el-button
                                >
                            </el-upload>
                        </div>
                    </div>

                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item
                            v-for="(item, index) in breadcrumbNodes"
                            :key="index"
                            >{{ item }}</el-breadcrumb-item
                        >
                    </el-breadcrumb>
                    <!-- <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox> -->
                    <div class="artImagegroup">
                        <el-checkbox-group
                            v-model="checkednodeslist"
                            @change="handleCheckedCitiesChange"
                        >
                            <div
                                style="display: inline-block"
                                v-for="(item, index) in dataList"
                                :key="index"
                            >
                                <el-checkbox class="artcheckbox" :label="item">
                                    <div
                                        class="artfileimg"
                                        @dblclick="storageImg(item.id)"
                                    >
                                        <!-- :preview-text="item.pictureName" -->
                                        <img
                                            :src="$imgDomain + item.picturePath"
                                            preview="2"
                                            alt=""
                                        />
                                    </div>
                                    <div class="artimagename">
                                        {{ item && item.pictureName }}
                                    </div>
                                </el-checkbox>
                            </div>
                        </el-checkbox-group>
                    </div>
                    <!-- 分页 -->
                    <el-pagination
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        :current-page="params.currentPage"
                        :page-sizes="[40, 80, 120]"
                        :page-size="params.currentPageSize"
                        :total="total"
                        layout="total, sizes, prev, pager, next, jumper"
                    >
                    </el-pagination>
                </el-main>
            </el-container>
        </el-container>
        <el-dialog
            title="已选择的图片"
            :visible.sync="isshow"
            :append-to-body="true"
        >
            <div class="popImgWarp">
                <img
                    class="imgItem"
                    v-for="item in imgBase64Array"
                    :src="item"
                    alt=""
                />
                <el-button
                    class="artuploadbtn"
                    size="small"
                    type="primary"
                    @click="uploadpics"
                    >确定上传</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    //import { getallimages } from '@/api/api'
    import { uploadPicBase64, businessPageUrl, getallimages } from "@/api/url";
    import imgCropper from "@/components/model-photo-cropper";
    import { storeScope, deletepicture, saveimagesurl } from "@/api/api";
    import Bread from "@/components/bread";
    import asideTree from "./asidetree"; //节点渲染组件
    export default {
        mixins: [mixinViewModule],
        name: "pictures",
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: getallimages,
                    activatedIsNeed: false,
                    getDataListIsPage: true,
                    // exportURL: '/admin-api/log/login/export',
                    deleteURL: "",
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: "id",
                },
                goodKind2loading: false,
                goodKind3loading: false,
                breaddata: ["内容管理", "内容管理", "图片"],
                dataForm: {
                    startTime: "",
                    endTime: "",
                    orderField: "", //排序字段
                    categoryId: "", //分组id
                    pictureName: "", //图片名称
                    categoryName: "", //分组名称  文件夹名称
                    order: "", //排序方式  asc  desc
                },
                valuetime: "",
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 40, //每页显示的条数
                },
                pickerOptions2: {
                    //时间
                    shortcuts: [
                        {
                            text: "最近一周",
                            onClick(picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(
                                    start.getTime() - 3600 * 1000 * 24 * 6
                                );
                                picker.$emit("pick", [start, end]);
                            },
                        },
                        {
                            text: "最近一个月",
                            onClick(picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(
                                    start.getTime() - 3600 * 1000 * 24 * 30
                                );
                                picker.$emit("pick", [start, end]);
                            },
                        },
                        {
                            text: "最近三个月",
                            onClick(picker) {
                                const end = new Date();
                                const start = new Date();
                                start.setTime(
                                    start.getTime() - 3600 * 1000 * 24 * 90
                                );
                                picker.$emit("pick", [start, end]);
                            },
                        },
                    ],
                },
                allImgChecked: false, //图片全选
                imgChecked: true,
                breadcrumbNodes: [], //图片的面包屑
                imgId: "", //鼠标进入时imgId
                checkAll: false,
                isIndeterminate: false,
                checkednodeslist: [],
                nodeslist: [], //当前目录的目录
                contentmenu: "上传时间降序",
                url: ``,
                imgBase64Array: [],
                imgurl: [],
                onlyone: true,
                loading: null,
                isshow: false,
                ascdesc: 1,
                dataAllChange: false,
            };
        },
        props: {
            type: "",
        },
        created() {
            this.limit = 40;
            this.getDataList();
        },
        mounted() {},
        components: {
            asideTree,
            imgCropper,
            Bread,
        },
        watch: {
            valuetime(val) {
                if (!val) {
                    this.dataForm.startTime = "";
                    this.dataForm.endTime = "";
                }
            },
        },
        methods: {
            init() {},
            actAddImgsBtn() {
                this.onlyone = true; //mark初始可以显示
            },

            //base64  图片的生成
            httpRequest(options) {
                if (options.file.size > 5 * 1024 * 1024) {
                    this.$message({
                        message: "图片最大不能超过5M",
                        type: "warning",
                        duration: 1500,
                    });
                    return;
                }
                if (this.onlyone) {
                    this.onlyone = false;
                    this.isshow = true;
                }
                console.log(options);
                const self = this;
                let reader = new FileReader();
                reader.readAsDataURL(options.file);
                self.imgBase64Array = [];
                self.imgurl = []; //参数file清空
                reader.onload = function (event) {
                    let img_base64 = this.result;
                    self.imgBase64Array.push(img_base64); //弹框img的src
                    self.imgurl.push({
                        imgStr: img_base64,
                        pictureName: options.file.name,
                    }); //post提交的base64
                };
                console.log(self.imgurl);
            },
            uploading() {
                this.loading = this.$loading({
                    lock: true,
                    text: "图片上传中",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)",
                });
            },
            uploadpics() {
                console.log(this.imgurl);
                this.uploading();
                var iddata = this.dataForm.categoryId
                    ? this.dataForm.categoryId
                    : "";
                const data = {
                    id: this.dataForm.categoryId ? this.dataForm.categoryId : "", //没选默认是所有图片
                    imgList: this.imgurl,
                };
                let that = this;
                if (!this.type) {
                    this.type = 1;
                }
                this.$http
                    .post(
                        uploadPicBase64 + `?id=${iddata}&type=${this.type}`,
                        this.imgurl
                    )
                    .then(({ data: data }) => {
                        console.log(data);
                        that.loading.close(); //关掉mark
                        that.isshow = false;
                        if (data && data.code == "200") {
                            this.getDataList(); //图片刷新
                            this.$refs.reftree.getpicturecategoryall(); //左侧的树结构更新
                            this.$message({
                                message: this.$t(data.msg),
                                type: "success",
                                duration: 500,
                                onClose: () => {},
                            });
                        } else {
                            this.$message({
                                message: this.$t(data.msg),
                                type: "error",
                                duration: 2500,
                                onClose: () => {},
                            });
                        }
                    })
                    .catch(() => {});
            },
            beforeAvatarUpload(file) {
                // 	       var allImageType = 'jpgjpegpngJPGJPEGPNGgifGIF' //'jpgjpegpngJPGJPEGPNGgifGIFbmpmp4flvMP4FLV';
                // //	      const isJPG = file.type === 'image/jpeg';
                // 			console.log(file);
                // 			var dessnamenum = file.name.indexOf('.') + 1;
                // 			console.log(file.name.substring(dessnamenum))
                // 			const isJPG = allImageType.indexOf(file.name.substring(dessnamenum)) != -1;
                const isJPG = /image/.test(file.type);

                const isLt5M = file.size / 1024 / 1024 < 10;
                if (!isJPG) {
                    this.$message.error("请上传正确的图片格式!");
                }
                if (!isLt5M) {
                    this.$message.error("图片大小不能超过 10MB!");
                }
                return isJPG && isLt5M;
            },
            handleCommand(command) {
                //图片排序
                if (command.indexOf("文件") != -1) {
                    this.dataForm.orderField = "picture_name";
                    if (command.indexOf("升") != -1) {
                        this.ascdesc = 4;
                        this.dataForm.order = "asc";
                    } else {
                        this.ascdesc = 3;
                        this.dataForm.order = "desc";
                    }
                } else {
                    this.dataForm.orderField = "create_date";
                    if (command.indexOf("升") != -1) {
                        this.ascdesc = 2;
                        this.dataForm.order = "asc";
                    } else {
                        this.ascdesc = 1;
                        this.dataForm.order = "desc";
                    }
                }
                this.contentmenu = command;
                console.log(this.dataForm);
                this.getDataList();
            },
            reset() {
                this.dataForm.startTime = "";
                this.dataForm.endTime = "";
                this.dataForm.orderField = "";
                this.dataForm.categoryId = "";
                this.dataForm.pictureName = "";
                this.dataForm.categoryName = "";
                this.dataForm.order = "";
                this.valuetime = "";
                this.ascdesc = 1;
                this.page = 1;
                this.limit = 30;
                this.getDataList();
            },
            actreset() {
                this.dataForm.startTime = "";
                this.dataForm.endTime = "";
                this.dataForm.orderField = "";
                this.dataForm.categoryId = "";
                this.dataForm.pictureName = "";
                this.dataForm.categoryName = "";
                this.dataForm.order = "";
                this.valuetime = "";
                this.ascdesc = 1;
            },
            // 每页数
            sizeChangeHandle(val) {
                this.page = 1;
                this.params.currentPageSize = val;
                this.params.currentPage = 1;
                this.limit = val;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle(val) {
                this.params.currentPage = val;
                this.page = val;
                this.getDataList();
            },
            //开始结束时间
            acttime() {
                this.dataForm.startTime = this.valuetime[0];
                this.dataForm.endTime = this.valuetime[1];
            },
            handleNodeClick(data, n, s) {
                //点击节点
                console.log(data);
            },
            ImgBreadcrumb(breadcrumbNodes) {
                //图片上部的面包屑
                this.breadcrumbNodes = breadcrumbNodes;
                console.log(this.breadcrumbNodes);
            },
            actgetDataList() {
                //点击查询查询图片
                this.dataForm.categoryId = "";
                this.page = 1;
                this.getDataList();
            },
            showChildNodes(idAndCategoryName) {
                //点击目录获取图片
                this.actreset();
                if (idAndCategoryName.id) {
                    this.dataForm.categoryId =
                        idAndCategoryName.id == "fjid" ? "" : idAndCategoryName.id;
                } else {
                    this.dataForm.categoryId = 0;
                }
                //	    	this.dataForm.categoryName = idAndCategoryName.categoryName;
                this.getDataList();
            },
            storageFile(id) {
                //双击文件夹
                console.log("双击文件夹");
            },
            storageImg(id) {
                console.log("双击图片");
            },
            handleCheckAllChange(val) {
                this.checkednodeslist = val ? this.dataList : []; //dataList  nodeslist
                this.isIndeterminate = false;
                this.dataAllChange = val;
                //	        console.log(this.checkednodeslist + '当前选中的复选框')
            },
            handleCheckedCitiesChange(value) {
                //  console.log(this.checkednodeslist );
                //  if(this.checkednodeslist.length>1){
                // 	 this.$message.warning("一次只能选择一张");
                // 	this.checkednodeslist.pop()
                //  }else
                if ((this.checkednodeslist.length = 1)) {
                    this.$emit(
                        "GiftUrlHandle",
                        this.checkednodeslist[0].picturePath
                    );
                }
                // let checkedCount = value.length;
                // console.log(checkedCount,this.dataList.length)
                // this.checkAll = checkedCount === this.dataList.length;  //dataList  nodeslist
                // this.isIndeterminate = checkedCount > 0 && checkedCount < this.dataList.length; //dataList  nodeslist
                // console.log(this.checkAll,this.isIndeterminate)
            },
            //删除图片
            artDeletePicture() {
                if (
                    this.dataAllChange ||
                    (this.checkAll && !this.isIndeterminate)
                ) {
                    //选择全选/一个一个点的去       删除一整页
                    //一共几页
                    var currentpage = this.total / this.params.currentPageSize;
                    if (this.page == Math.ceil(currentpage)) {
                        //删除的是最后一整页
                        console.log(currentpage);
                        this.page--;
                    }
                    this.dataAllChange = false;
                }
                var obj = {
                    //request.js
                    data: [],
                };
                this.checkednodeslist.forEach((item) => {
                    obj.data.push(item.id);
                    //    			console.log(item.id)
                });
                if (obj.data.length == 0) {
                    this.$message({
                        message: "请选择要删除的图片",
                        type: "warning",
                    });
                    return;
                }
                this.$confirm(
                    this.$t("prompt.info", { handle: this.$t("delete") }),
                    this.$t("prompt.title"),
                    {
                        confirmButtonText: this.$t("confirm"),
                        cancelButtonText: this.$t("cancel"),
                        type: "warning",
                    }
                )
                    .then(() => {
                        deletepicture(obj)
                            .then((data) => {
                                if (data.code !== 200) {
                                    return this.$message.error(data.msg);
                                } else {
                                    this.$message({
                                        message: this.$t(data.msg),
                                        type: "success",
                                        duration: 500,
                                        onClose: () => {
                                            this.visible = false;
                                        },
                                    });
                                }
                                this.checkAll = false; //全选按钮恢复初始状态
                                this.isIndeterminate = false; //全选按钮恢复初始状态
                                this.dataList = [];
                                this.checkednodeslist = []; //全选清空
                                this.getDataList(); //图片更新
                                this.$refs.reftree.getpicturecategoryall(); //左侧的树结构更新
                            })
                            .catch(() => {});
                    })
                    .catch(() => {});
            },
        },
    };
</script>
<style lang="scss" scoped>
    /* .el-scrollbar__wrap {
                                                                                                                                                                  overflow-x: hidden;
                                                                                                                                                                } */
    @import "@/element-ui/theme-variables.scss";

    .picturesClass {
        // 表头背景和字体颜色
        /deep/ .el-table__header th {
            background: #f5f7fa;
        }
        .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
            white-space: nowrap;
        }
        *::-webkit-scrollbar {
            width: 7px;
            height: 1px;
            background-color: transparent;
        } /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
        *::-webkit-scrollbar-track {
            background-color: #f0f6ff;
        } /*定义滚动条轨道 内阴影+圆角*/
        *::-webkit-scrollbar-thumb {
            background-color: #e3e4e4;
            border-radius: 6px;
        } /*定义滑块 内阴影+圆角*/
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

        //左侧菜单
        .artbtn {
            float: right;
            & > div {
                display: inline-block;
                margin-right: 20px;
            }
        }
        .artbtn .el-button {
            border: none;
            border-radius: 0;
        }
        .el-header {
            margin-top: 5px;
            line-height: 30px;
            .el-aside {
                border: none;
                .el-button {
                    border: none;
                    border-radius: 0;
                }
            }
            .el-main {
                padding: 0;
                /*overflow: visible;*/
                .artbtn {
                    float: right;
                    .el-button {
                        width: auto;
                        border-radius: 5px;
                    }
                }

                //上传图片的按钮
                .upload-demo {
                    float: left;
                    margin-right: 10px;
                }
            }
        }
        //左侧
        .arttop {
            /*margin-bottom: 10px;*/
            width: 95%;
            .el-button {
                border: 0;
            }
        }
        //左侧导航加右侧图片列表
        .artcontent {
            .el-aside {
                margin-top: 10px;
                border-right: 1px solid #ccc;
            }
            .el-main {
                height: 700px;
                padding-top: 10px;
            }
            .el-row {
                margin-bottom: 20px;
                &:last-child {
                    margin-bottom: 0;
                }
            }
            .el-col {
                border-radius: 4px;
                margin-bottom: 20px;
            }
            .bg-purple-dark {
                background: #99a9bf;
            }
            .bg-purple {
                background: #d3dce6;
            }
            .bg-purple-light {
                background: #e5e9f2;
            }
            .grid-content {
                border-radius: 4px;
                min-height: 36px;
            }
            .row-bg {
                padding: 10px 0;
                background-color: #f9fafc;
            }
            .el-checkbox__label {
                padding-left: 0px;
                width: 100%;
                height: 100%;
            }
            .artfileimg {
                //图片
                width: 100%;
                height: 100%;
                text-align: center;
                width: 120px;
                img {
                    width: 120px;
                    max-height: 120px;
                    margin-bottom: 5px;
                    /*height:120px;*/
                    /*object-fit: contain;*/
                }
            }
        }

        // 右边图片样式
        /deep/ .el-checkbox-group {
            margin-top: 20px;
            display: flex;
            flex-wrap: wrap;
            .el-checkbox.is-checked {
                .el-checkbox__input {
                    display: block;
                }
            }
            .el-checkbox {
                margin-left: 15px;
                margin-right: 15px;
                margin-bottom: 10px;
                position: relative;
                width: 122px;
                height: 150px;
                .el-checkbox__label {
                    border: 1px solid #eee;
                    padding-left: 0px;
                    height: 124px;
                }
                .artimagename {
                    position: absolute;
                    bottom: 0px;
                    /*margin: 8px 0;*/
                    /*border-top: 1px solid #eee;*/
                    font-size: 12px;
                    width: 110px;
                    text-align: center;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                }
                &:hover .el-checkbox__input {
                    display: block;
                }
                .el-checkbox__input {
                    display: none;
                    position: absolute;
                    top: 5px;
                    left: 5px;
                    text-align: center;
                    /deep/ .el-checkbox__inner {
                        width: 25px;
                        height: 25px;
                        border-radius: 3px;
                        &:after {
                            height: 18px;
                            left: 13px;
                        }
                    }
                }
            }
        }
        //去除目录的border
        .el-tree-node__content {
            border: none;
        }
    }

    //弹框中的img
    .popImgWarp {
        .imgItem {
            width: 100px;
            height: 100px;
            margin: 10px;
            border: 1px solid #ccc;
        }

        .artuploadbtn {
            display: block;
            margin: 20px auto;
            width: 100px;
            height: 40px;
        }
    }

    /deep/ .el-dialog .el-dialog__body {
        overflow-y: scroll;
        max-height: 700px;
        height: auto;
        /*padding-top: 20px;*/
        padding-bottom: 10px;
        padding-left: 56px;
    }
    .el-dropdown-link {
        display: inline-block;
        padding: 0 60px 0 50px;
        cursor: pointer;
    }
    .el-icon-arrow-down {
        font-size: 12px;
    }
    //左侧按钮图片的大小
    .artimggroup {
        width: 20px;
    }
    //排序的li
    .el-dropdown-menu__item span {
        width: 170px;
    }
    .el-dropdown-menu__item span {
        display: inline-block;
        width: 20px;
    }
    .artImagegroup {
        width: 1240px;
    }
    .el-main::-webkit-scrollbar {
        height: 10px !important;
    }
</style>

