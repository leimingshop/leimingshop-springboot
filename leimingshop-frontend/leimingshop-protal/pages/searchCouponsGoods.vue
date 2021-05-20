<template>
    <div class="search-goods-result">
        <!-- 顶部搜索部分 -->
        <searchPart></searchPart>

        <div class="inner">
            <!-- 内容 -->
            <div class="p-content">
                <!-- 全部分类 -->

                <div class="product-wrap">
                    <!-- 商品列表 -->
                    <div class="goods-wrap">
                        <goods-comp
                            v-for="(item, index) in goodsVOList"
                            :key="index"
                            :thumbnailVisible="goodsCard.thumbnailVisible"
                            :otherInfoVisible="goodsCard.otherInfoVisible"
                            :goodsId="item.goodsId"
                            :specId="item.id"
                            :specSellPrice="item.specSellPrice"
                            :goodsLabels="item.goodsLabels"
                            :goodsMainPicture="item.specMainPicture"
                            :goodsName="item.specName"
                            :goodsSubTitle="item.goodsVO.goodsSubTitle"
                            :storeName="item.goodsVO.storeName"
                            :storeId="item.goodsVO.storeId"
                            :storeType="item.goodsVO.storeType"
                            :evaluateCount="item.goodsVO.evaluateCount"
                            :goodsSaleNum="item.specSaleNum"
                        ></goods-comp>
                    </div>

                    <!-- 分页 -->
                    <paging
                        v-if="goodsVOList.length != 0 && totalCount > pageSize"
                        class="paging"
                        :totalCount="totalCount"
                        :pageSize="pageSize"
                        @handlePageChange="handlePageChange"
                    />
                    <div v-else style="height: 60px"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import searchPart from "@/components/common/searchPartWhite.vue";
    import paging from "@/components/common/paging";
    import goodsComp from "@/components/02.shopIndex/02-01.shopProClassification/goodsComp.vue";
    import { mapState, mapMutations, mapActions } from "vuex";
    import { collectBills } from "@/api/api_06-13.coupon.js";

    export default {
        name: "searchCouponsGoods", // 搜索商品结果
        head() {
            return {
                title: "搜索优惠券可用商品",
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
                goodsVOList: [],
                goodsCard: {
                    thumbnailVisible: false,
                    otherInfoVisible: true,
                },
                page: 1, //当前页
                pageSize: 10, //每页显示多少条数据
                totalCount: 1, //总共页数，默认为1
            };
        },
        components: {
            goodsComp,
            paging,
            searchPart,
        },
        mounted() {
            this.getList(1);
        },
        methods: {
            getList(data) {
                collectBills({
                    activityId: this.$route.query.activityId,
                    collectBillType: 3,
                    pageNo: data,
                    pageSize: this.pageSize,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.totalCount = res.data.totalCount * 1;
                            this.goodsVOList = res.data.frontCouponsGoodsVOList;
                            for (var i = 0; i < this.goodsVOList.length; i++) {
                                if (
                                    this.goodsVOList[i].goodsVO.evaluateCount ===
                                    null
                                ) {
                                    this.goodsVOList[i].goodsVO.evaluateCount = 0;
                                }
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            handlePageChange(val) {
                this.getList(val);
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/goods-list-comp.scss";
</style>

<style lang="scss" scoped>
    .inner {
        margin-top: 20px;
    }
    .search-goods-result {
        background: #f6f6f6;
        width: 100%;
        padding: 0 0 80px 0;
    }

    /deep/ .product-filtration {
        margin-bottom: 10px;

        .ivu-checkbox-wrapper {
            vertical-align: -8px !important;
        }
    }
</style>
