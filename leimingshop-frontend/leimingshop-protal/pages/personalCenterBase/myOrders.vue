<template>
    <div class="myOrders">
        <Tabs type="card" @on-click="actTabs" :animated="false" v-model="lala">
            <TabPane v-for="tab in tabs" :key="tab" :label="tab" name>
                <Affix>
                    <div class="o-table-head">
                        <p
                            v-for="item in table.head"
                            :key="item.title"
                            v-text="item.title"
                            :style="{ width: item.width }"
                        ></p>
                    </div>
                </Affix>

                <div class="order-list">
                    <template v-for="item in listData">
                        <!-- data循环里的appStatus-{{item.appStatus}} -->
                        <!-- 订单中商品在不同的库房或商家，故拆单  只有groupStatus!= 0/10 或者订单是在待发货状态下取消 并且 orderChildrenList有值   子组件展示的数据也取orderChildrenList里的-->
                        <!--下面的内容在每个TabPane的下面都是相同的-->
                        <separate
                            :thead="table.head"
                            v-if="
                                (item.orderStatus == 20 ||
                                    item.orderStatus == 30 ||
                                    item.orderStatus == 40 ||
                                    (item.orderStatus == 0 &&
                                        item.paymentName)) &&
                                item.orderChildrenList != null
                            "
                            :data="item"
                            @getDataList="actTabs"
                        ></separate>

                        <!-- 待付款 子组件展示的数据取orderGoodsDtoList里的-->
                        <wait-pay
                            :thead="table.head"
                            @getDataList="actTabs"
                            v-if="item.appStatus == 10"
                            :data="item"
                            :page="page"
                        ></wait-pay>

                        <!-- 待收货 子组件展示的数据取orderGoodsDtoList里的-->
                        <wait-receive
                            :thead="table.head"
                            @getDataList="actTabs"
                            v-if="
                                (item.appStatus == 20 ||
                                    item.appStatus == 30) &&
                                item.orderChildrenList == null
                            "
                            :data="item"
                            :page="page"
                        ></wait-receive>

                        <!-- 已完成 子组件展示的数据取orderGoodsDtoList里的-->
                        <completed
                            :thead="table.head"
                            @getDataList="actTabs"
                            v-if="
                                item.appStatus == 40 &&
                                item.orderChildrenList == null
                            "
                            :data="item"
                        ></completed>

                        <!-- 已取消 子组件展示的数据取orderGoodsDtoList里的-->
                        <canceled
                            :thead="table.head"
                            @getDataList="actTabs"
                            v-if="
                                item.appStatus == 0
                            "
                            :data="item"
                        ></canceled>
                    </template>
                </div>
                <!--无数据的情况-->
                <div class="no-data" v-show="noDataFlag">
                    <img src="/img/06.personalCenter/noshop.png" alt />
                    <p>对不起，暂无订单数据！</p>
                </div>
                <!--无搜索结果的情况-->
                <div class="no-search" v-show="noSearchFlag">
                    <img src="/img/06.personalCenter/noshop.png" alt />
                    <p>对不起，没有搜索到您想要的结果！</p>
                </div>
                <!--数据未出来的等待状态-->
                <ul class="spin" v-show="spinFlag">
                    <li>
                        <Spin size="large"></Spin>
                    </li>
                    <li>
                        <Spin></Spin>
                    </li>
                    <li>
                        <Spin size="small"></Spin>
                    </li>
                </ul>
            </TabPane>

            <div slot="extra">
                <Input
                    v-model="searchContent"
                    placeholder="商品名称/商品编号/订单编号"
                    @on-enter="handleSearch"
                />
                <img
                    src="/img/06.personalCenter/search.png"
                    alt
                    style="
                        position: absolute;
                        top: 14px;
                        right: 50px;
                        cursor: pointer;
                    "
                    @click="handleSearch"
                />
            </div>
        </Tabs>
        <!--不能写在上面 需要切换后单独渲染-->
        <!-- 分页 -->
        <paging
            class="paging"
            :totalCount="total"
            :pageSize="pageSize"
            v-model="page"
            @handlePageChange="handlePageChange"
            v-if="listData.length > 0 && total > pageSize"
        />
        <div v-else style="height: 60px"></div>
        <reason-popup
            v-if="$store.state.myOrders.reasonList.length > 0"
            @getDataList="actTabs"
        ></reason-popup>
        <logistics-popup
            v-if="$store.state.myOrders.logisticsFlag"
        ></logistics-popup>
    </div>
</template>

<script>
    /**
                                                                                                                                                                                     1
                                                                                                                                                                                     */
    import waitPay from "@/components/06.personalCenter/06-07.myOrders/waitPay";
    import waitReceive from "@/components/06.personalCenter/06-07.myOrders/waitReceive";
    import completed from "@/components/06.personalCenter/06-07.myOrders/completed";
    import canceled from "@/components/06.personalCenter/06-07.myOrders/canceled";
    import separate from "@/components/06.personalCenter/06-07.myOrders/separate";
    import paging from "@/components/common/paging";
    import reasonPopup from "@/components/06.personalCenter/06-07.myOrders/reasonPopup.vue";
    import logisticsPopup from "@/components/06.personalCenter/06-07.myOrders/logisticsPopup.vue";
    import { OrderPage } from "@/api/api_06-07-01personalMyOrders.js";

    import { on, off, handleScroll, debounce } from "@/utils/dom";

    export default {
        name: "myOrders",
        head() {
            return {
                title: "我的订单",
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
                searchContent: "",
                table: {
                    head: [
                        {
                            title: "订单详情",
                            width: "336px",
                        },
                        {
                            title: "收货人",
                            width: "154px",
                        },
                        {
                            title: "金额",
                            width: "180px",
                        },
                        {
                            title: "订单状态",
                            width: "154px",
                        },
                        {
                            title: "操作",
                            width: "130px",
                        },
                    ],
                },

                tabs: ["全部订单", "待付款", "待收货", "已完成", "已取消"],
                listData: [],
                currentTabIndex: 0, //当前的tabs下标
                page: 1, //当前页
                pageSize: 5, //每页显示多少条数据
                total: 1, //总共条数
                noDataFlag: false, //是否有订单数据
                noSearchFlag: false, //是否搜索到结果
                spinFlag: true, //是否加载中
                lala: 2,
            };
        },

        components: {
            waitPay,
            waitReceive,
            completed,
            canceled,
            separate,
            paging,
            reasonPopup,
            logisticsPopup,
        },

        created() {
            // this.handlePageChange(1);
        },
        mounted() {
            this.lala = this.$store.state.myOrders.currentLabelnum;
            this.actTabs(this.$store.state.myOrders.currentLabelnum);
            //将state中的理由列表进行置空
            this.$store.state.myOrders.reasonList = [];
            //将state中的物流状态变为无
            this.$store.state.myOrders.logisticsFlag = false;
            on(window, "scroll", debounce(this.handleAffix, 10));
        },
        methods: {
            async actTabs(index) {
                this.page = 1;
                //保存当前导航位置
                this.$store.commit("myOrders/setCurrentLabelnum", {
                    currentLabelnum: this.lala,
                });
                window.scroll(0, 0);
                this.spinFlag = true;
                this.noDataFlag = false;
                this.searchContent = "";
                this.currentTabIndex =
                    index === 0 ? 0 : index ? index : this.currentTabIndex;
                let obj = {
                    page: 1,
                    limit: this.pageSize,
                };
                try {
                    if (this.currentTabIndex)
                        obj.appStatus =
                            this.currentTabIndex == 1
                                ? 10
                                : this.currentTabIndex == 2
                                ? 30
                                : this.currentTabIndex == 3
                                ? 40
                                : 0;
                    let res = await OrderPage(obj);
                    this.listData = res.data.data.list;
                    console.log(this.listData);
                    if (this.listData.length > 0) {
                        this.spinFlag = false;
                    } else {
                        this.spinFlag = false;
                        this.noDataFlag = true;
                    }
                    this.total = res.data.data.total;
                } catch (e) {}
            },
            //分页  获取订单详情列表
            async handlePageChange(val) {
                this.page = parseInt(val);
                this.spinFlag = true;
                let obj = {};
                if (this.currentTabIndex == 0) {
                    obj = { page: val, limit: this.pageSize };
                    let searchContent = this.searchContent.replace(
                        /(^\s*)|(\s*$)/g,
                        ""
                    );
                    obj.keyWord = searchContent;
                } else {
                    obj = { page: val, limit: this.pageSize };
                    let searchContent = this.searchContent.replace(
                        /(^\s*)|(\s*$)/g,
                        ""
                    );
                    obj.keyWord = searchContent;
                    if (this.currentTabIndex)
                        obj.appStatus =
                            this.currentTabIndex == 1
                                ? 10
                                : this.currentTabIndex == 2
                                ? 30
                                : this.currentTabIndex == 3
                                ? 40
                                : 0;
                }
                try {
                    let res = await OrderPage(obj);
                    this.spinFlag = false;
                    this.listData = res.data.data.list;
                    this.total = res.data.data.total;
                } catch (e) {}
            },
            handleAffix() {
                let el = document.querySelector(".myOrders .ivu-tabs-bar");
                let style =
                    "position: absolute; top: 155px; z-index: 2; transform: translateZ(0);";
                handleScroll(el, 230, style);
            },
            //根据条件搜索订单
            async handleSearch() {
                console.log("搜索订单");
                this.spinFlag = true;
                this.noDataFlag = false;
                let obj = {
                    page: 1,
                    limit: this.pageSize,
                };
                try {
                    if (this.currentTabIndex)
                        obj.appStatus =
                            this.currentTabIndex == 1
                                ? 10
                                : this.currentTabIndex == 2
                                ? 30
                                : this.currentTabIndex == 3
                                ? 40
                                : 0;
                    let searchContent = this.searchContent.replace(
                        /(^\s*)|(\s*$)/g,
                        ""
                    );
                    obj.keyWord = searchContent;
                    let res = await OrderPage(obj);
                    this.listData = res.data.data.list;
                    if (this.listData.length > 0) {
                        this.spinFlag = false;
                    } else {
                        this.spinFlag = false;
                        this.noDataFlag = true;
                    }
                    this.total = res.data.data.total;
                    this.page = 1;
                } catch (e) {}
            },
        },
        beforeDestroy() {
            off(window, "scroll", this.handleAffix);
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .myOrders {
        width: 948px;
        background: #ffffff;
        height: 100%;
    }

    .ivu-tabs.ivu-tabs-card {
        padding-top: 55px;
        /deep/ & > .ivu-tabs-bar {
            margin: -56px 0 0;
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
            .ivu-tabs-tab {
                width: 120px;
                height: 44px;
                line-height: 44px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
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
                margin: 8px 40px 0 0;
                /deep/ .ivu-input {
                    height: 30px;
                    box-sizing: border-box;
                }
                /deep/ .ivu-icon {
                    line-height: 30px;
                }
            }
        }

        /deep/ .ivu-tabs-content {
            margin: 40px 40px 0px;
        }
    }

    .o-table-head {
        display: none;
        width: 100%;
        height: 42px;
        line-height: 42px;
        background: #f2f2f2;
        margin-bottom: 15px;
        & > p {
            display: inline-block;
            text-align: center;
        }
    }

    /deep/ .ivu-tabs-content {
        margin: 20px;
    }

    //取消订单理由复选框的样式
    .myOrders /deep/ {
        // .ivu-radio {
        //   // display: none;
        // }
        .ivu-radio-group {
            margin-top: 12px;
        }
        .ivu-radio-border {
            border: 1px solid #dcdee2;
            border-radius: 4px;
            height: 32px;
            line-height: 42px;
            transition: border 0.2s ease-in-out;
            width: 228px;
            height: 42px;
            text-align: center;
            margin-top: 18px;
        }
    }
    .no-data,
    .no-search {
        margin-top: 10px;
        background: #fff;
        text-align: center;
        height: auto;
        padding-top: 100px;
        p {
            margin-top: 30px;
            font-size: 16px;
            color: #3a3a3a;
            font-weight: 400;
        }
    }
    .spin {
        display: flex;
        justify-content: center;
        margin-top: 130px;
        align-items: center;
        li {
            margin-left: 20px;
        }
    }
    /deep/ .ivu-card {
        border-radius: 0px !important;
    }

    /deep/ .ivu-input {
        font-size: 12px;
        color: #222222;
        /deep/ input::-webkit-input-placeholder {
            color: #999999;
        }
        /deep/ input::-moz-placeholder {
            color: #999999;
        }
        /deep/ input::-moz-placeholder {
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

    // /deep/ .order-info .o-content > div.o-freight span {
    //    font-size: 18px !important;
    // }
    // /deep/ .ivu-btn-text{
    //   font-size: 12px !important;
    //   color:#666666 !important
    // }
    /deep/ .order-info .o-content > div.o-freight i {
        margin-right: -4px !important;
    }
    /deep/ .order-info .o-content > div.o-freight span {
        height: 18px;
        line-height: 18px;
        margin-bottom: 10px;
    }
    /deep/ order-info .o-operate .ivu-btn {
        height: 12px !important;
        line-height: 12px !important;
    }
    /deep/.order-info .o-operate .ivu-btn {
        margin-top: 10px !important;
        &:hover {
            color: #dd2619;
        }
    }
    /deep/ .order-info .o-content > div.o-freight em {
        height: 12px;
        line-height: 12px;
        margin: 0px 0 10px !important;
        &:nth-of-type(2) {
            margin-bottom: 10px !important;
        }
    }
    /deep/ .order-info .o-head {
        // padding: 0px !important;
        // margin: 0 auto !important;
        justify-content: space-between !important;
    }
    /deep/ .emem {
        line-height: 20px !important;
    }
    /deep/ .order-info .o-content > div.o-status button {
        height: 13px;
        line-height: 13px;
    }
    /deep/ .order-info .o-content > div.o-status {
        span {
            height: 13px;
            line-height: 13px;
            margin-bottom: 10px;
        }
    }
    /deep/ .delete {
        margin-left: 4px;
    }
    /deep/ .ivu-btn-text {
        color: #666666;
    }

    /deep/ .order-list {
        div:last-child {
            .order-info {
                margin-bottom: 0px;
            }
        }
    }
</style>
