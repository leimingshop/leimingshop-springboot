<template>
    <div class="after-sale">
        <!-- <Tabs type="card" v-model="tabCardVal" @on-click="handleTabChange">
      <TabPane
        v-for="(tab, index) in tabs"
        :key="tab.title"
        :name="index.toString()"
        :label="tab.title"
        :class="{ height_0: tabCardVal != index }"
        v-loading="tab.loading"
      > -->
        <div class="afterSaleTop">
            <div style="display: flex">
                <div
                    :class="tabCardVal == 0 ? 'btn-on' : 'btn-off'"
                    @click="handleTabChange(0)"
                >
                    售后申请
                </div>
                <div
                    :class="tabCardVal == 0 ? 'btn-off' : 'btn-on'"
                    @click="handleTabChange(1)"
                >
                    申请记录
                </div>
            </div>
            <div slot="extra" class="afterLeft">
                <Input
                    v-model="searchText"
                    placeholder="订单编号"
                    @on-enter="handleSearch"
                    @on-click="handleSearch"
                />
                <img
                    src="/img/06.personalCenter/search.png"
                    @on-enter="handleSearch"
                    @on-click="handleSearch"
                    slot="suffix"
                    alt
                    style="position: absolute; top: 15px; right: 50px"
                />
            </div>
        </div>
        <div style="padding: 40px">
            <div v-if="applyList.length != 0 || recordList.length != 0">
                <!-- 售后申请 -->
                <div class="listWrap" v-if="tabCardVal == 0">
                    <after-sale-comp
                        v-for="item in applyList"
                        :key="item.id"
                        :applyData="item"
                    ></after-sale-comp>
                </div>

                <!-- 申请记录 -->
                <div class="listWrap" v-if="tabCardVal == 1">
                    <processing-record-comp
                        v-for="(item, index) in recordList"
                        :key="index"
                        :recordData="item"
                        @handleModalVisible="handleModalVisible"
                    ></processing-record-comp>
                </div>

                <!-- 分页 -->
                <paging
                    v-if="totalCount > 6"
                    class="paging"
                    :totalCount="totalCount"
                    :pageSize="limit"
                    v-model="page"
                    @handlePageChange="handlePageChange"
                />
            </div>

            <!-- 无数据 -->
            <search-no-data v-else></search-no-data>
            <!-- </TabPane> -->

            <!-- <div slot="extra">
        <Input
          v-model="searchText"
          placeholder="订单编号"
          @on-enter="handleSearch"
          @on-click="handleSearch"
        />
        <img
          src="/img/06.personalCenter/search.png"
          @on-enter="handleSearch"
          @on-click="handleSearch"
          slot="suffix"
          alt
          style="position: absolute; top: 15px; right: 50px"
        />
      </div> -->
            <!-- </Tabs> -->
        </div>
        <!-- 进度跟踪 -->
        <modal-progress-tracking ref="modalProgressTracking">
        </modal-progress-tracking>
    </div>
</template>

<script>
    import $ from "jquery";
    import afterSaleComp from "@/components/06.personalCenter/06-03.afterSale/afterSaleComp";
    import processingRecordComp from "@/components/06.personalCenter/06-03.afterSale/processingRecordComp";

    import paging from "@/components/common/paging";
    import searchNoData from "@/components/06.personalCenter/06-03.afterSale/searchNoData";
    import modalProgressTracking from "@/components/06.personalCenter/06-03.afterSale/modalProgressTracking";

    import { mapState, mapMutations, mapActions } from "vuex";

    export default {
        name: "afterSale",
        head() {
            return {
                title: "售后服务",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },

        data() {
            return {
                tabCardVal: "0",
                searchText: "",
            };
        },

        components: {
            afterSaleComp,
            processingRecordComp,
            paging,
            searchNoData,
            modalProgressTracking,
        },

        computed: {
            ...mapState("afterSale", [
                "applyList",
                "recordList",
                "totalCount",
                "limit",
                "applyDataLoading",
                "recordDataLoading",
            ]),

            page: {
                get() {
                    return this.$store.state.afterSale.page;
                },
                set(val) {
                    this.HANDLE_CHANGE_PAGENO(val);
                },
            },

            tabs() {
                return [
                    {
                        title: "售后申请",
                        loading: this.applyDataLoading,
                        list: this.applyList,
                    },
                    {
                        title: "申请记录",
                        loading: this.recordDataLoading,
                        list: this.recordList,
                    },
                ];
            },
        },

        watch: {
            $route: {
                immediate: true,
                handler(newVal, oldVal) {
                    // 默认选中的tab项
                    this.tabCardVal = this.$route.query.tab
                        ? this.$route.query.tab
                        : "0";
                    this.handleSearch();
                },
            },
        },

        created() {
            // 其他页带参进入
            this.searchText = this.$route.query.orderSn;

            // 路由参数重构;
            this.$router.replace({
                path: "/personalCenterBase/afterSale",
                query: { tab: this.tabCardVal },
            });
            console.log(this.tabCardVal, "created");
            // 加载数据
            this.handleSearch();
        },

        mounted() {
            $(window).on("scroll", () => this.handleAffix());

            this.$once("hook:destroyed", () => {
                $(window).off("scroll");
                this.HANDLE_CHANGE_PAGENO(1);
            });
            console.log(this.$route.query.tab, "mounted");
            this.handleDataList(this.$route.query.tab);
        },

        methods: {
            ...mapMutations("afterSale", [
                "HANDLE_CHANGE_PAGENO",
                "HANDLE_APPLY_SEARCH_DATA",
                "HANDLE_RECORD_SEARCH_DATA",
            ]),

            ...mapActions("afterSale", [
                "handleAftersaleAvailable",
                "handleAftersaleApplyRecord",
            ]),

            // 切换tab
            handleTabChange(val) {
                this.searchText = "";

                this.$router.replace({
                    path: "/personalCenterBase/afterSale",
                    query: { tab: val },
                });
            },

            // 搜索
            handleSearch() {
                let searchText = this.searchText;

                switch (this.tabCardVal) {
                    case "0":
                        // 申请售后服务列表 查询字段
                        this.HANDLE_APPLY_SEARCH_DATA(searchText);
                        break;
                    case "1":
                        // 售后记录列表 查询字段
                        this.HANDLE_RECORD_SEARCH_DATA(searchText);
                        break;
                }

                // 重置分页
                this.handlePageChange(1);
            },

            // 切换分页
            handlePageChange(val) {
                this.HANDLE_CHANGE_PAGENO(val);
                this.handleDataList(this.tabCardVal);
            },

            // 获取列表
            handleDataList(val) {
                console.log(val, "获取列表");
                switch (val) {
                    case "0":
                        // 订单可申请售后服务列表
                        this.handleAftersaleAvailable();
                        break;
                    case "1":
                        console.log("售后服务申请纪录列表");
                        // 售后服务申请记录列表
                        this.handleAftersaleApplyRecord();
                        break;
                }
            },

            // 固定card header
            handleAffix() {
                if ($(window).scrollTop() >= $(".after-sale").offset().top) {
                    $(".after-sale .ivu-tabs-bar").css({
                        position: "fixed",
                        top: "155px",
                        "z-index": 2,
                        transform: "translateZ(0)",
                    });
                } else {
                    $(".after-sale .ivu-tabs-bar").css("position", "static");
                }
            },

            // 显示进度跟踪弹窗
            handleModalVisible(aftersaleSn) {
                this.$refs["modalProgressTracking"].visible = true;
                this.$refs["modalProgressTracking"].aftersaleSn = aftersaleSn;
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .after-sale {
        min-height: 100%;
        background: #ffffff;
        .height_0 {
            height: 0;
        }
    }
    .afterSaleTop {
        width: 100%;
        height: 44px;
        display: flex;
        justify-content: space-between;
        padding: 0 30px 0 0;
        line-height: 44px;
        border-bottom: 1px solid #dd2619;
    }
    .btn-on {
        width: 120px;
        background: $primary-color;
        color: #ffffff;
        font-size: 14px;
        text-align: center;
    }
    .btn-off {
        width: 120px;
        font-size: 14px;
        text-align: center;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
    }
    .afterLeft {
        width: 252px;
        background: #ffffff;
        border-radius: 4px;
    }
    .ivu-tabs.ivu-tabs-card {
        padding-top: 55px;
        /deep/ & > .ivu-tabs-bar {
            margin: -55px 0 0;
            width: 948px;
            height: 44px;
            background: #ffffff;
            border-color: $primary-color;
            box-sizing: content-box;
            .ivu-tabs-nav-container,
            .ivu-tabs-nav-scroll,
            .ivu-tabs-nav,
            .ivu-tabs-nav-wrap {
                height: 44px;
            }
            .ivu-icon {
                cursor: pointer;
                transition: color 0.5s;
                &:hover {
                    color: $primary-color;
                }
            }
            .ivu-tabs-tab {
                width: 120px;
                height: 44px;
                line-height: 44px;
                padding: 0;
                margin-right: 0;
                border: 0;
                border-radius: 0;
                background: #ffffff;
                color: #666666;
                text-align: center;
                transition: all 0.1s;
                &.ivu-tabs-tab-active {
                    background: $primary-color;
                    color: #ffffff;
                }
            }
            /deep/ .ivu-input-wrapper {
                width: 252px;
                height: 30px;
                margin: 8px 30px 0 0;
                /deep/ .ivu-input {
                    height: 100%;
                }
                /deep/ .ivu-icon {
                    line-height: 36px;
                }
            }
        }

        /deep/ .ivu-tabs-content {
            margin: 40px 40px 0px;
        }
    }
    /deep/ .ivu-card {
        border-radius: 0px !important;
    }
    /deep/ .ivu-btn {
        border: none !important;
    }
    /deep/ .ivu-card-bordered {
        border: 1px solid #f6f6f6 !important;
        &:hover {
            box-shadow: none !important;
            border: 1px solid #e8e8e8 !important;
            .a-head {
                background: #e8e8e8 !important;
            }
            .p-head {
                background: #e8e8e8 !important;
            }
        }
    }
    /deep/ .p-head {
        border: none !important;
    }
    /deep/ .ivu-page-prev {
        border-radius: 0px !important;
    }
    /deep/ .ivu-page-item {
        border-radius: 0px !important;
        a {
            // margin: 5px 0px !important;
        }
    }
    /deep/ .ivu-page .ivu-page-next {
        border-radius: 0px !important;
    }
    /deep/ .ivu-page .ivu-page-options-elevator input {
        border-radius: 0px !important;
    }
    /deep/ .confirm-btn {
        // margin-top: -25px !important;
        border: 1px solid #dddddd !important;
        border-radius: 0px !important;
    }
    /deep/ .paging-comp {
        // margin: 10px 0 0 !important;
        padding: 0px !important;
    }
    /deep/ .p-provide-time {
        margin-top: 10px;
    }
    /deep/ .p-content .p-goods-wrap .p-info {
        color: #666666 !important;
    }
    /deep/ .ivu-btn-text {
        color: #666666 !important;
    }
    /deep/ .ivu-input {
        font-size: 12px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #222222 !important;
        input::-webkit-input-placeholder {
            color: #999999;
        }
        input::-moz-placeholder {
            color: #999999;
        }
        input::-moz-placeholder {
            color: #999999;
        }
        &:hover {
            border-color: #b7b7b7 !important;
        }
        &:focus {
            border-color: #b7b7b7 !important;
            box-shadow: none !important;
        }
    }
    /deep/ .a-content .a-goods-wrap .a-goods-desc .g-name {
        font-size: 14px !important;
        color: #222222 !important;
        font-family: PingFangSC-Regular, PingFang SC;
    }
    /deep/ .p-content .p-goods-wrap .p-goods-desc .p-name {
        font-size: 14px !important;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
    }
    /deep/ .ivu-btn-text {
        font-size: 12px;
        font-family: PingFangSC-regular, PingFang SC;
        font-weight: 400;
        color: #666666 !important;
    }
    /deep/ .p-head > p {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
    }
    /deep/ .a-content .a-goods-wrap .a-price {
        span:nth-of-type(2) {
            margin: 0 -4px !important;
            font-weight: 500 !important;
        }
    }
    /deep/ .a-head {
        // padding: 0px !important;
        // justify-content: center;
        p {
            &:last-child {
                margin-right: 0px;
            }
        }
    }
    /deep/ .p-content .p-goods-wrap .p-operate .ivu-btn {
        font-size: 12px !important;
        height: 12px;
        line-height: 12px;
        &:nth-of-type(2) {
            margin-top: 11px;
        }
    }
</style>
