<template>
    <div class="all-categories">
        <!-- 面包屑 -->
        <div class="breadcrumb" v-if="breadcrumbVisible">
            <breadcrumb1 :list="breadcrumb.list">
                <!-- <span v-show="handleArrowsVisible()" class="custom-separator">&nbsp;>&nbsp;</span> -->

                <Tag v-for="(ITEM, INDEX) in category" :key="ITEM.id" v-if="handleTagVisible(ITEM.selectTags)" type="border" closable color="error" @on-close="handleClassifyCloseTag(ITEM, INDEX)">
                    <em v-text="ITEM.title"></em>
                    <span class="v-title" v-for="(item, index) in ITEM.selectTags" :key="item" v-text="item"></span>
                </Tag>

                <Tag v-if="handleTagVisible(filtrate.changeVal)" type="border" closable color="error" @on-close="handleFltrateCloseTag">
                    <em v-text="filtrate.hoverAttrName"></em>
                    <span class="v-title" v-text="filtrate.changeVal"></span>
                </Tag>
            </breadcrumb1>
        </div>

        <div class="category-list" v-loading="categoryLoading" v-if="categoriesVisible" v-show="categoryListVisible">
            <div v-for="(ITEM, INDEX) in category" :key="ITEM.type" v-if="handleClassifyVisible(ITEM, INDEX)" :class="['basic-style', ITEM.type]" style="padding: 5px 0 15px">
                <!-- 需求: 【当前分类有选中值 ==> 显示】 （ITEM.selectTags.length == 0） -->
                <h6 v-text="ITEM.title"></h6>
                <ul class="c-first" v-if="!ITEM.multipleChoice">
                    <li v-for="(item, index) in ITEM.list" :key="item.id" :class="[{ active: item.title == ITEM.selectTags[0] }]" v-text="item.title" @click="handleClassifySelect(ITEM, INDEX, item)"></li>
                </ul>

                <CheckboxGroup v-model="ITEM.changeDataList" class="c-first" v-if="ITEM.multipleChoice">
                    <Checkbox v-for="(item, index) in ITEM.list" :key="item.id" :label="`${JSON.stringify(item)}`">
                        <span>{{ item.title }}</span>
                    </Checkbox>
                </CheckboxGroup>

                <div class="handle-btn" v-if="isMultipleChoice && INDEX != 0">
                    <Button v-show="!ITEM.multipleChoice" icon="md-add" class="multiple-choice-btn" @click="handleChoice(ITEM, INDEX, true)">多选</Button>

                    <ButtonGroup v-show="ITEM.multipleChoice">
                        <Button type="default" @click.native="handleCancel(ITEM, INDEX)">取消</Button>
                        <Button type="primary" @click.native="handleConfirm(ITEM, INDEX)">确定</Button>
                    </ButtonGroup>
                </div>
            </div>

            <!-- 高级筛选 -->
            <div v-if="handleFiltrateVisible()" class="filtrate basic-style" style="padding: 5px 0 15px">
                <!-- 需求: 【当前分类有选中值 ==> 显示】 （!filtrate.changeVal） -->
                <h6 v-text="filtrate.title" style="
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
          "></h6>
                <ul class="c-first">
                    <li v-for="(item, index) in goodsAttrVOList" :key="item.attrId" :class="[
              {
                active: filtrate.activeIndex == item.attrId,
                hoverActive: filtrate.hoverIndex == item.attrId,
              },
            ]" style="margin-top: 1px">
                        <Dropdown trigger="hover" :visible="filtrate.hoverIndex == item.attrId" :transfer="true" transfer-class-name="filtrate-dropdown" placement="bottom" @on-visible-change="handleDropDownChange($event, item.attrId)">
                            <Button type="default">
                                {{ item.attrName }}
                                <Icon type="ios-arrow-down" size="16" />
                            </Button>

                            <DropdownMenu slot="list">
                    <li v-for="(Item, Index) in item.goodsAttrValueVOList" :key="Item.attrValueName" :class="[
                        {
                        active: filtrate.changeVal == Item.attrValueName,
                        },
                    ]" v-text="Item.attrValueName" @click="
                        handleAttrSelect(
                        item.attrName,
                        item.attrId,
                        Item.attrValueName
                        )
                    "></li>
                    </DropdownMenu>
                    </Dropdown>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
    import breadcrumb1 from "@/components/common/breadcrumb1.vue";
    export default {
        name: "allCategories", //店铺分类列表页 - 全部分类
        components: {
            breadcrumb1,
        },
        data() {
            return {
                // 面包屑
                breadcrumb: {
                    separator: ">",
                    list: [],
                },

                // 大类
                category: [
                    {
                        type: "classification",
                        title: "分类：",
                        list: [],
                        multipleChoice: false, // 多选
                        changeDataList: [], // 数据格式 [{}], 多选用
                        selectTags: [], // 选中的 tags
                    },

                    {
                        type: "brand",
                        title: "品牌：",
                        list: [],
                        multipleChoice: false, // 多选
                        changeDataList: [],
                        selectTags: [],
                    },

                    {
                        type: "goodsLabel",
                        title: "标签：",
                        list: [],
                        multipleChoice: false, // 多选
                        changeDataList: [],
                        selectTags: [],
                    },
                ],

                // 高级筛选
                filtrate: {
                    title: "高级筛选：",
                    changeVal: null,
                    hoverAttrName: null,
                    hoverIndex: null,
                    activeIndex: null,
                    // multipleChoice: false, // 暂无多选
                    // changeDataList: [] // 暂无多选
                },
            };
        },

        props: {
            // 面包屑
            breadcrumbList: {
                type: Array,
                default: () => [],
            },

            // 商品分类列表
            goodsClassVOList: {
                type: Array,
                default: () => [],
            },

            // 品牌列表
            brandVOList: {
                type: Array,
                default: () => [],
            },

            // 标签列表
            goodsLabelVOList: {
                type: Array,
                default: () => [],
            },

            // 商品属性列表
            goodsAttrVOList: {
                type: Array,
                default: () => [],
            },

            // 是否显示面包屑
            breadcrumbVisible: {
                type: Boolean,
                default: true,
            },

            // 大类加载状态
            categoryLoading: {
                type: Boolean,
                default: true,
            },

            // 是否显示大类
            categoriesVisible: {
                type: Boolean,
                default: true,
            },

            // 是否一直显示大类
            categoryAlwaysVisible: {
                type: Boolean,
                default: false,
            },

            // 是否显示多选操作按钮
            isMultipleChoice: {
                type: Boolean,
                default: true,
            },

            // 店铺商品分类
            shopClassifyList: {
                type: Array,
                required: false,
                default: () => [],
            },
        },

        computed: {
            categoryListVisible() {
                // 大类加载状态
                if (this.categoryLoading) return true;

                // 是否显示 大类
                let res1 = this.category.every((item, index) => {
                    return this.handleClassifyVisible(item, index) == false;
                });

                // 是否显示 高级筛选
                let res2 = this.handleFiltrateVisible() == false;

                if (res1 && res2) return false;
                else return true;
            },
        },

        created() {},

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    // resetStore 店铺列表页所用
                    if (
                        newVal.name == "searchGoodsResult" ||
                        newVal.query.querySearchType == "resetStore"
                    ) {
                        Object.assign(this.$data, this.$options.data());
                    }
                },
            },

            // 面包屑
            breadcrumbList: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.$set(this.breadcrumb, "list", newVal);
                },
            },

            // 商品分类列表
            goodsClassVOList: {
                immediate: true,
                handler(newVal, oldVal) {
                    // 如果是 [店铺 - 商品列表页 - 分类]（第一级）需要调该页面的方法处理分类数据, 其他的走该方法
                    if (this.$route.name != "shopProClassification") {
                        this.$set(
                            this.category[0],
                            "list",
                            this.handleParam(newVal, "id", "gcName")
                        );
                    }
                },
            },

            // 品牌列表
            brandVOList: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.$set(
                        this.category[1],
                        "list",
                        this.handleParam(this.brandVOList, "brandId", "brandName")
                    );
                },
            },

            // 标签列表
            goodsLabelVOList: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.$set(
                        this.category[2],
                        "list",
                        this.handleParam(this.goodsLabelVOList, "labelId", "labelName")
                    );
                },
            },

            // 店铺商品分类
            shopClassifyList: {
                immediate: false,
                handler(newVal, oldVal) {
                    this.$set(
                        this.category[0],
                        "list",
                        this.handleParam(newVal, "id", "gcName")
                    );
                },
            },
        },

        mounted() {},

        methods: {
            // tag 是否显示
            handleTagVisible(tags) {
                if (this.$route.name == "shopProClassification") return false;
                return tags && tags.length != 0;
            },

            // 大类是否一直显示状态
            handleClassifyVisible(param, index) {
                // index == 0 有可能当前选中二级分类，所以要判断是否有三级分类列表
                if (index == 0 || this.categoryAlwaysVisible)
                    return param.list.length != 0;
                else return param.selectTags.length == 0 && param.list.length != 0;
            },

            // 高级筛选是否一直显示状态
            handleFiltrateVisible() {
                if (this.categoryAlwaysVisible) return this.goodsAttrVOList.length != 0;
                else return this.goodsAttrVOList.length != 0 && !this.filtrate.changeVal;
            },

            // 面包屑 tag 左侧箭头
            handleArrowsVisible() {
                return (
                    this.category.some((item) => item.selectTags.length != 0) ||
                    this.filtrate.changeVal
                ); // 四个类别其一 tag 有数据
            },

            // 高级筛选下拉菜单回调
            handleDropDownChange(visible, id) {
                this.$set(this.filtrate, "hoverIndex", visible ? id : null);
            },

            // 点击面包屑
            handleBreadcrumbChange(param) {
                if (!param.toPath) return;
                // 清空tag
                let temp = this.category[0];
                temp.selectTags = [];
                this.$set(this.category, 0, temp);
            },

            // 关闭面包屑标签(分类)
            handleClassifyCloseTag(param, INDEX) {
                // 置空该类别
                param.changeDataList = [];
                param.selectTags = [];
                this.$set(this.category, INDEX, param);

                if (INDEX == 0) {
                    if (this.$route.name == "proClassification") {
                        let temp = this.breadcrumbList[this.breadcrumbList.length - 1];

                        //this.$router.replace({
                            //path: "/proClassification",
                            //params: {
                               // id: temp.id,
                         //   },
                          //  query: {
                          //      classId: temp.classId,
                          //      dataSource: "currentPage",
                          //  },
                      //  });
                        this.$emit("handleClassifyList,temp.id")
                    }
                }

                // 处理查询列表所需数据
                let idsParam = this.handleChangeData(param, []);
                this.$emit("handleCategoryIds", idsParam);
            },

            // 关闭高级筛选
            handleFltrateCloseTag() {
                // 置空该类别
                this.$set(this.filtrate, "changeVal", null);
                this.$set(this.filtrate, "activeIndex", null);
                // 处理查询列表所需数据
                this.$emit("handleAttrData", []);
            },

            // 是否多选
            handleChoice(param, INDEX, bool) {
                param.multipleChoice = bool;
                this.$set(this.category, INDEX, param);
            },

            // 取消多选
            handleCancel(param, INDEX) {
                param.changeDataList = [];
                this.handleChoice(param, INDEX, false);
            },

            // 处理多选分类回调
            handleConfirm(param, INDEX) {
                if (param.changeDataList.length == 0)
                    return this.$Message.warning("请选择筛选条件");

                // 处理选中id列表, title列表
                let idList = [],
                    titleList = [];
                param.changeDataList.forEach((item) => {
                    let tempData = JSON.parse(item);
                    idList.push(tempData.id);
                    titleList.push(tempData.title);
                });

                // 处理查询列表所需数据
                let idsParam = this.handleChangeData(param, idList);
                this.$emit("handleCategoryIds", idsParam);

                // 关闭多选
                this.handleChoice(param, INDEX, false);

                // 处理面包屑所需数据
                param.selectTags = titleList;
                this.$set(this.category, INDEX, param);
            },

            //  单选分类回调
            handleClassifySelect(parent, INDEX, current) {
                // 取消选中( 用在店铺商品分类列表页 ), 暂时关闭该功能
                // if( parent.selectTags[0] == current.title  ){
                //     parent.selectTags = []
                //     var current = {
                //         title: undefined,
                //         id: undefined
                //     }
                // }

                // 处理面包屑所需数据
                parent.selectTags = [current.title];
                this.$set(this.category, INDEX, parent);

                // 点击分类
                if (INDEX == 0) {
                    // 商品分类列表页
                    if (this.$route.name == "proClassification") {
                        // 重置路由
                        //this.$router.replace({
                           // path: "/proClassification",
                           // query: {
                                //classId: current.id,
                                //dataSource: "currentPage",
                            //},
                        //});
                        this.$emit("handleClassifyList,current.id")
                    } else if (this.$route.name == "shopProClassification") {
                        // 店铺分类列表页, 重置数据
                        this.$emit("handleResetSecondClassify", current.id);
                    }
                }

                // 处理查询列表所需数据
                let id = current.id ? [current.id] : [];

                let idsParam = this.handleChangeData(parent, id);
                this.$emit("handleCategoryIds", idsParam);
            },

            // 处理查询列表所需数据
            handleChangeData(param, ids) {
                let idList;

                switch (param.type) {
                    case "classification":
                        idList = {
                            gcIds: ids,
                        };
                        break;

                    case "brand":
                        idList = {
                            brandIds: ids,
                        };
                        break;

                    case "goodsLabel":
                        idList = {
                            labelIds: ids,
                        };
                        break;
                }
                return idList;
            },

            // 选中属性回调
            handleAttrSelect(hoverAttrName, parentId, tabVal) {
                // 取消选中( 用在店铺商品分类列表页 ), 暂时关闭该功能
                // if( this.filtrate.changeVal == tabVal ) {
                //     var hoverAttrName = '',
                //         parentId = '',
                //         tabVal = null,
                //         parentId = null
                // }

                // 处理查询列表所需数据
                var params;

                if (parentId && tabVal) {
                    params = [
                    {
                        attrId: parentId,
                        attrValueList: [tabVal],
                    }, ];
                } else {
                    params = undefined;
                }

                this.$emit("handleAttrData", params);

                this.filtrate = {
                    title: "高级筛选：",
                    hoverAttrName: `${hoverAttrName}：`,
                    changeVal: tabVal,
                    hoverIndex: null,
                    activeIndex: parentId,
                };
            },

            // 处理参数
            handleParam(params, id, name) {
                params.map((item, index) => {
                    let tempArr = Object.keys(item);
                    tempArr.map((ele, i) => {
                        item.id = item[id];
                        item.title = item[name];
                    });
                });
                return params;
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .custom-separator {
        color: #333333;
    }

    .ivu-tag {
        margin: 0 4px 0 0;
        min-width: 90px;
        height: 24px;
        line-height: 24px;
        display: inline-block;
        background: transparent !important;
        border-radius: 0;
        border-color: $primary-color;

        &::after {
            width: 0;
        }

        /deep/ em {
            font-style: normal;
            font-size: 12px;
            color: #333333;
        }

        .v-title {
            display: inline-block;
            margin-right: 10px;
            color: $primary-color;

            &:nth-of-type(1) {
                margin-left: -5px;
            }
        }

        /deep/ .ivu-tag-text {
            display: inline-block;
            /*                overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;*/
        }

        /deep/ .ivu-icon-ios-close {
            opacity: 1;
            background: #dd2619 !important;
            color: #ffffff !important;
            border-radius: 0;
            border: 1px solid $primary-color;
            padding: 0;
            margin: 0 !important;
            display: inline-block;
            top: 0;
            left: 5px;
            transition: opacity 0.2s;

            &:hover {
                opacity: 0.66;
            }

            &::before {
                width: 0;
            }
        }
    }

    .breadcrumb {
        background-color: #f6f6f6;
    }
</style>


<style lang="scss" scoped>
    $primary-color: #dd2619;

    .all-categories {
        width: 100%;
        background: #ffffff;
        // margin-bottom: 10px;
    }

    .category-list {
        min-height: 56px;
        padding: 5px 25px;
        // margin-bottom: 30px;
        margin-bottom: 10px;

        &.padding0 {
            padding: 0;
        }
    }

    .basic-style {
        width: 1200px;
        // min-height: 50px;
        // line-height: 50px;
        margin: 0 auto;
        border-bottom: 1px dotted #e3e3e3;
        box-sizing: border-box;
        padding: 11px 0 13px;
        position: relative;

        &:last-of-type {
            border-bottom: 0;
        }

        h6 {
            // width: 39px;
            height: 13px;
            line-height: 13px;
            display: inline-block;
            vertical-align: top;
            white-space: nowrap;
            text-align: right;
            font-weight: 400;
            font-family: PingFangSC-Regular, PingFang SC;
            color: rgba(102, 102, 102, 1);
            font-size: 13px;
            margin-top: 10px;

            &:last-child {
                width: 74px;
                height: 15px;
                font-size: 15px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(86, 86, 86, 1);
                line-height: 15px;
            }
        }

        .c-first {
            width: 90%;
            line-height: 13px;
            // padding: 12px 0;
            display: inline-block;
            vertical-align: top;

            // margin-top: 10px;
            &>li {
                cursor: pointer;
                display: inline-block;
                margin: 10px 20px 0;
                text-align: center;
                font-size: 13px;
                height: 30px;
                font-size: 13px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                line-height: 13px;

                &:hover,
                &.active {
                    color: $primary-color;
                }

                &.active {
                    font-weight: 600;
                }
            }

            &.ivu-checkbox-group {
                margin: 0px 0 0 20px;

                .ivu-checkbox-wrapper {
                    margin-right: 30px;
                    margin-top: 10px;
                }
            }
        }

        .handle-btn {
            height: 22px;
            position: absolute;
            top: 14px;
            right: 0;

            .multiple-choice-btn {
                width: 42px;
                height: 100%;
                display: block;
                border-radius: 4px;
                border: 1px solid rgba(204, 204, 204, 1);
                padding: 0;
                font-size: 10px;
                color: #999999;

                &:hover {
                    color: #e45147;
                    background-color: #ffffff;
                    border-color: #e45147;
                }

                /deep/ .ivu-icon {
                    font-size: 13px;
                }

                /deep/ span {
                    margin-left: -4px;
                }
            }

            .ivu-btn-group {
                height: 100%;
                display: block;
                margin-top: 4px;

                button {
                    width: 36px;
                    height: 22px;
                    padding: 0;
                    font-size: 10px;
                    border-radius: 4px !important;

                    &:nth-of-type(1) {
                        margin-right: 10px;
                    }

                    &:nth-of-type(2) {}
                }
            }
        }
    }

    .brand {
        padding: 11px 0 13px;
    }

    .filtrate {
        // min-height: 56px;
        border-bottom: none;

        ul.c-first {
            &>li {
                position: relative;
                margin: 0 10px;

                &::before {
                    content: "";
                    display: block;
                    width: 100%;
                    height: 80%;
                    box-shadow: 0 0 2px 1px rgba(0, 0, 0, 0.1);
                    border-bottom: none;
                    border-top-left-radius: 4px;
                    border-top-right-radius: 4px;
                    position: absolute;
                    left: 0;
                    top: 0;
                    opacity: 0;
                    transition: opacity 0.8s;
                    background-color: blue;
                }

                &::after {
                    content: "";
                    display: block;
                    width: 100%;
                    height: 5px;
                    background: #ffffff;
                    position: absolute;
                    z-index: -3;
                    left: 0;
                    bottom: 3px;
                    box-shadow: 0 0 4px rgba(255, 255, 255, 1);
                    opacity: 0;
                    transition: opacity 1s;
                }

                button .ivu-icon {
                    transition: transform 0.3s;
                }

                &.active {

                    &::before,
                    &::after {
                        opacity: 0;
                    }

                    button {
                        font-weight: 600;
                    }
                }

                &:hover,
                &.hoverActive {

                    &::before,
                    &::after {
                        opacity: 1 !important;
                    }
                }

                &:hover,
                &.active,
                &.hoverActive {
                    button {
                        color: $primary-color;

                        .ivu-icon {
                            transform: rotate(-180deg);
                            color: $primary-color;
                        }
                    }
                }
            }
        }

        button {
            border: none;
            box-sizing: border-box;
            padding: 0 10px;
            vertical-align: 1px;

            /deep/ &>span {
                font-size: 13px;
                vertical-align: top;
            }

            &:hover {
                color: $primary-color !important;

                /deep/ .ivu-icon {
                    color: $primary-color !important;
                }
            }

            .ivu-icon {
                color: #979797;
            }
        }
    }
</style>

<style lang="scss">
    $primary-color: #dd2619;

    .activity {
        /deep/ .product-filtration {
            margin-top: 0 !important;
        }
    }

    .filtrate-dropdown.ivu-select-dropdown {
        width: 1140px;
        min-height: 60px;
        padding: 0px;
        left: 0;
        right: 0;
        margin: -5px auto 0;
        z-index: 2 !important;

        .ivu-dropdown-menu {
            text-align: left;
            width: 100%;
            padding: 15px;
            display: inline-block;

            li {
                color: #565656;
                display: inline-block;
                padding: 5px 20px;
                cursor: pointer;

                &:hover {
                    color: $primary-color;
                }

                &.active {
                    color: $primary-color;
                    font-weight: 600;
                }
            }
        }
    }

    /deep/ .ivu-checkbox-focus {
        box-shadow: none !important;
    }

    /deep/ .ivu-checkbox-checked .ivu-checkbox-inner {
        background-color: #ffffff !important;

        &::after {
            border: 1px solid #dd2619 !important;
        }
    }

    /deep/ .ivu-btn {
        color: #666666 !important;
    }
</style>
