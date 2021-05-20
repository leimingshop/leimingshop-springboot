<template>
    <div class="product-filtration">
        <div class="sort-wrap">
            <!-- 按条件排序  -->
            <div
                class="sort-type"
                ref="sortType"
                @mouseleave="handleSortTypeStyle(tabIndex)"
            >
                <Button
                    type="text"
                    v-for="(item, index) in sortType"
                    :key="index"
                    :class="['filtration-btn', { active: index == tabIndex }]"
                    @click="handleFilter(item, index)"
                    @mouseenter.native="handleSortTypeStyle(index)"
                >
                    {{ item.title }}
                    <i
                        :class="[
                            { 'arrow-down-btn': index == 3 },
                            { desc: item.sortType == 'DESC' },
                        ]"
                    ></i>
                </Button>
            </div>

            <!-- 价格区间  -->
            <div class="price-range">
                <InputNumber
                    size="small"
                    :max="100000"
                    :min="0"
                    placeholder="￥"
                    v-model="priceData.minPrice"
                    :parser="(value) => value.replace(/\￥\s?|(,*)/g, '')"
                    style="width: 70px; height: 26px"
                ></InputNumber>

                <Divider />

                <InputNumber
                    size="small"
                    :max="1000000"
                    :min="0"
                    placeholder="￥"
                    v-model="priceData.maxPrice"
                    :parser="(value) => value.replace(/\￥\s?|(,*)/g, '')"
                    style="width: 70px; height: 26px"
                ></InputNumber>
            </div>

            <Button class="confirm-btn" type="primary" @click="handlePrice"
                >确定</Button
            >
        </div>

        <div class="show-rule">
            <Checkbox v-model="hasGoods.value" style="display: none">{{
                hasGoods.title
            }}</Checkbox>
            <Checkbox v-model="preferentialGoods.value" style="display: none">{{
                preferentialGoods.title
            }}</Checkbox>
            <div
                v-if="sortBtnVisible"
                class="show-mode"
                @click="handleToggeleStyle"
            >
                <img v-lazy="showMode[showMode.current]" class="mall-logo" />
            </div>
        </div>
    </div>
</template>

<script>
    import { mapState } from "vuex";

    export default {
        name: "productFiltration", //店铺分类列表页 - 商品筛选过滤工具栏

        data() {
            return {
                sortType: [
                    {
                        title: "综合",
                        sortField: "", // {'' : '综合'}, {'goodsSaleNum': '销量'}, { 'specSellPrice': '价格'}
                        sortType: "DESC", // {'DESC': '降序' }, {'ASC': '升序'}
                    },
                    {
                        title: "销量",
                        sortField: "goodsSaleNum",
                        sortType: "DESC",
                    },
                    {
                        title: "新品",
                        sortField: "goodsUpTime",
                        sortType: "DESC",
                    },
                    {
                        title: "价格",
                        sortField: "specSellPrice",
                        sortType: "DESC",
                    },
                ],
                hasGoods: {
                    value: false,
                    title: "仅显示有货商品",
                },
                preferentialGoods: {
                    value: false,
                    title: "仅显示特惠商品",
                },
                showMode: {
                    column: require("~/static/img/02.shopIndex/02-01.shopProClassification/show-mode-column.png"),
                    row: require("~/static/img/02.shopIndex/02-01.shopProClassification/show-mode-row.png"),
                    current: "column",
                },

                filterData: {
                    sortField: "",
                    sortType: "DESC",
                },

                priceData: {
                    minPrice: null,
                    maxPrice: null,
                },

                tabIndex: 0, //过滤条件默认选中下标
            };
        },

        props: {
            // 商品排序按钮是否显示
            sortBtnVisible: {
                type: Boolean,
                default: true,
            },
        },

        computed: {
            ...mapState({
                product: (state) => state["shop"].proClassification["product"], // 宝贝分类
            }),
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    Object.assign(this.$data, this.$options.data());
                },
            },
        },

        created() {},

        mounted() {},

        methods: {
            handleSortTypeStyle(index) {
                this.$refs.sortType.style.setProperty("--active-index", index);
            },

            // 商品过滤处理
            handleFilter(param, index) {
                this.tabIndex = index;

                this.handleSortTypeStyle(index);

                if (index == 3) {
                    param.sortType = param.sortType == "DESC" ? "ASC" : "DESC";
                }
                this.$set(this.sortType, index, param);

                let filterData = {
                    sortField: param.sortField,
                    sortType: param.sortType,
                };
                this.filterData = filterData;
                this.$emit("handleFilterData", filterData);
            },

            // 商品价格
            handlePrice() {
                this.$emit("handlePriceData", this.priceData);
            },

            // 切换商品列表样式
            handleToggeleStyle() {
                let currentMode = this.showMode.current;
                currentMode = currentMode == "column" ? "row" : "column";
                this.$set(this.showMode, "current", currentMode);
                this.$emit("hanldeChangeStyle", currentMode);
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .sort-wrap {
        float: left;
    }

    .sort-type {
        float: left;
        display: flex;
        position: relative;
        &::after {
            position: absolute;
            z-index: 1;
            left: 0;
            top: 0;
            content: "";
            display: block;
            width: 70px;
            height: 34px;
            background: #f6f6f6;
            color: $primary-color;
            transform: translateX(calc(70px * var(--active-index)));
            transition: all 0.3s;
        }
    }

    .product-filtration {
        width: 100%;
        height: 46px;
        padding: 6px 30px 6px 20px;
        box-sizing: border-box;
        background: #ffffff;
        margin: 0 0 10px;
    }

    .filtration-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 2;
        width: 70px;
        height: 34px;
        color: #3a3a3a;
        font-size: 13px;
        border-radius: 0;
        // &:hover,
        //   &.active {
        //   }
        &.active {
            font-weight: 600;
            color: $primary-color;
            background: #f6f6f6;
        }
    }

    .arrow-down-btn {
        width: 5px;
        height: 12px;
        display: inline-block;
        vertical-align: -1px;
        margin-left: 3px;
        background: url("/img/02.shopIndex/02-01.shopProClassification/arrow-high.png")
            no-repeat center/cover;
        transform: rotateX(0);
        &.desc {
            transform: rotateX(180deg);
        }
    }

    .price-range {
        height: 26px;
        margin: 4px 0 0 20px;
        float: left;
        white-space: nowrap;
        /deep/ .ivu-divider {
            display: inline-block;
            min-width: auto;
            width: 6px;
            height: 2px;
            background: #333333;
            vertical-align: 1px;
            margin: 0;
        }
        /deep/ .ivu-input-number-input {
            color: #222222;
            font-size: 12px;
            line-height: 12px;
            padding-top: 6px;
            padding-bottom: 5px;
            margin-top: 0px;
            input {
                font-size: 12px;
            }
        }
    }

    .confirm-btn {
        width: 50px;
        height: 26px;
        line-height: 24px;
        padding: 0;
        border-radius: 2px;
        border: 0;
        margin: 5px 0 0 10px;
        background: #cccccc;
        color: #ffffff;
        font-size: 13px;
        border-radius: 4px;
        &:hover {
            background: $primary-color;
            color: #ffffff;
        }
    }

    .show-rule {
        float: right;
        /deep/ .ivu-checkbox-wrapper {
            vertical-align: 8px;
            &.ivu-checkbox-wrapper-checked {
                color: #e2270b;
            }
        }
    }

    .show-mode {
        width: 30px;
        height: 30px;
        display: inline-block;
        margin-left: 10px;
        cursor: pointer;
        .mall-logo {
            width: 100%;
            height: 100%;
        }
    }
    /deep/ .ivu-btn-text {
        background-color: #ffffff;
    }
    /deep/ .ivu-input-number {
        &:hover {
            border-color: #a7a7a7;
        }
    }
    /deep/ .ivu-input-number-focused {
        box-shadow: none;
        border-color: #b7b7b7;
    }
    /deep/ .p-text {
        margin-top: 10px !important;
    }
</style>
