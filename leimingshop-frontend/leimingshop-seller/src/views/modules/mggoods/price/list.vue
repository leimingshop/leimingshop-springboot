<template>
    <div>
        <div id="control-area">
            <!-- <el-breadcrumb separator="/">
		  <el-breadcrumb-item :to="{ path: '/' }">商品</el-breadcrumb-item>
		  <el-breadcrumb-item><a href="/">商品管理</a></el-breadcrumb-item>
		  <el-breadcrumb-item>商品列表</el-breadcrumb-item>
		</el-breadcrumb> -->
            <div style="display: flex; justify-content: space-between">
                <Bread :breaddata="breaddata"></Bread>
                <el-button
                    type="primary"
                    @click="showLogListFn()"
                    v-if="$hasPermission('sys:price:log:view')"
                    >修改记录</el-button
                >
            </div>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getDataList()"
            >
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
          @select="handleSelect">
          </el-autocomplete> -->
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
                    >
                    </el-cascader>
                </el-form-item>
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
                <div>
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
                    <div class="iconSize">1、审核通过的商品修改规格库存</div>
                    <div class="iconSize">
                        2、可通过修改流水查看商品库存数量修改前后变化及修改人修改时间
                    </div>
                </template>
            </el-alert>
        </div>
        <listItem
            :dataForm="dataForm"
            style="margin-top: 8px"
            ref="fourList"
            @showLogListFn="showLogListFn"
            :tableheightBig="tableheightBig"
        ></listItem>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import listItem from "./listItem.vue";
    import { setTimeout } from "timers";
    import { brandListPull, getGoodscalss, allTreeGoodsclass } from "@/api/api.js";
    export default {
        data() {
            return {
                brandlist: [],
                breaddata: ["商品", "商品管理", "价格管理"],
                dataForm: {
                    goodsShow: "", //上下架状态:0下架;1上架
                    goodsName: "",
                },
                activeName: "",
                tab: "",
                gcIds: [],
                goodscalssOption: [],
                state4: "",
                props: {
                    value: "id",
                    label: "gcName",
                },
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
            listItem,
        },
        activated() {
            this.handleClick();
        },
        created() {
            this.getallTreeGoodsclassFn();
            this.getBrandListPull();
        },
        methods: {
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
            finishCange() {
                let len = this.gcIds.length;
                if (len != -1) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
            },
            // // 获取品牌下拉列表
            handleSelect(item) {
                console.log(item);
                console.log(this.state4);
                if (item.id) {
                    this.dataForm.brandName = item.brandName;
                } else {
                    this.dataForm.brandName = this.state4;
                }
            },
            getBrandListPull(brandName, cb) {
                var obj = {
                    params: {
                        brandName: brandName,
                    },
                };
                brandListPull(obj).then((res) => {
                    if (res.code == 200) {
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
                this.tab = tab;
                this.$nextTick(() => {
                    if (tab == "") {
                        this.dataForm.goodsShow = "";
                    } else if (tab == "upper") {
                        this.dataForm.goodsShow = "1";
                    } else if (tab == "lower") {
                        this.dataForm.goodsShow = "0";
                    } else if (tab == "not") {
                        this.dataForm.goodsShow = "2";
                    }
                    this.$refs.fourList.init(this.dataForm);
                });
            },
            // 重置
            reset() {
                this.dataForm.goodsName = "";
                // this.handleClick();
                this.dataForm.brandId = ""; //品牌id
                this.dataForm.brandName = "";
                this.dataForm.gcId = ""; //分类ID
                this.gcIds = []; //分类ID
                this.state4 = "";
                this.$refs.fourList.init(this.dataForm);
            },
            showLogListFn(id) {
                this.$emit("showLogListFn", id);
            },
        },
    };
</script>
<style lang="scss" scoped>
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

    .el-button {
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

    .el-radio-group {
        margin-top: 20px;
    }
    .el-form--inline {
        /deep/.el-input {
            height: 40px !important;
        }
    }
</style>
