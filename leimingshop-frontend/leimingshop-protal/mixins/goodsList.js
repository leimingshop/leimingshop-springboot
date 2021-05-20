// 搜索头部
import searchPart from '@/components/common/searchPartWhite.vue'

// 页面组件
import allCategories from '@/components/02.shopIndex/02-01.shopProClassification/allCategories'
import productFiltration from '@/components/02.shopIndex/02-01.shopProClassification/productFiltration'
import goodsComp from '@/components/02.shopIndex/02-01.shopProClassification/goodsComp'
import paging from '@/components/common/paging'
import filtrationNoData from '@/components/03.goodsClass/03-01.goodsClassification/filtrationNoData'
import searchNoData from '@/components/05.searchResult/05-02.searchNoData'

import { mapState, mapMutations, mapActions } from 'vuex'

export default {
    name: 'goodsList', // 搜索商品结果

    data() {
        return {
            filterNoDataText: '对不起，对应商品分类或筛选组合下没有商品',

            categoryData: {
                gcIds: this.$route.query.storeId ? void (0) : [], // 商品分类
                brandIds: [], // 商品品牌
                labelIds: [] // 商品标签
            }, // 分类字段

            filterData: {
                sortField: '', // {'' : '综合'}, {'goodsSaleNum': '销量'}, { 'specSellPrice': '价格'}
                sortType: 'DESC', // {'DESC': '降序' }, {'ASC': '升序'}
            }, // 过滤字段

            priceData: {
                minPrice: '',
                maxPrice: ''
            }, // 价格字段

            attrData: [], // 属性列表

            resetData: false // 是否重置数据
        }
    },
    async fetch({ store }) {
        console.log("fetch");
        await store.dispatch("main/actIndexCustomClass");
        await store.dispatch("main/actIndexNavList");
    },
    components: {
        searchPart,
        allCategories,
        productFiltration,
        goodsComp,
        paging,
        filtrationNoData,
        searchNoData
    },

    computed: {
        ...mapState(
            'goodsClassification',
            [
                'goodsClassVOList'
            ]
        ),

        ...mapState(
            'searchGoodsResult',
            [
                'hasSearchList',
                'brandVOList',
                'goodsVOList',
                'goodsAttrVOList',
                'goodsLabelVOList',
                'dataLoading',
                'categoryLoading',
                'totalCount',
                'pageSize'
            ],
        ),

        pageNo: {
            get() {
                return this.$store.state.searchGoodsResult.pageNo
            },
            set(val) {
                this.HANDLE_CHANGE_PAGENO(val)
            }
        },

        storeData() {
            let routeQuery = this.$route.query

            return {
                storeFirstGcIds: routeQuery.storeFirstGcIds ? [routeQuery.storeFirstGcIds] : void (0), // 店铺一级商品分类Id
                storeGcIds: routeQuery.storeGcIds ? [routeQuery.storeGcIds] : void (0), // 店铺二级商品分类Id
                storeId: routeQuery.storeId // 店铺id
            }
        },// 店铺字段

        searchDataForm() {
            let routeQuery = this.$route.query

            return {
                keyword: routeQuery.keyWord ? decodeURIComponent(routeQuery.keyWord) : '', // 搜索关键字
                pageSize: this.pageSize, // 每页显示条数
                pageNo: this.pageNo, // 第几页
                ...this.storeData // 店铺字段
            }
        },

        filterDataForm() {
            return {
                ...this.searchDataForm, // 搜索字段
                ...this.categoryData, // 分类字段
                attrListList: this.attrData,    // 属性字段
                ...this.filterData, // 过滤字段
                ...this.priceData, // 价格字段
            }
        },
    },

    watch: {
        '$route': {
            immediate: true,
            handler(newVal, oldVal) {
                this.resetData = true
            }
        },

        'filterDataForm': {
            immediate: true,
            deep: false,
            async handler(newVal, oldVal) {
                // currentPage没变，但是查询数据有变动，currentPage 恢复默认
                if (oldVal && newVal.pageNo != 1 && newVal.pageNo == oldVal.pageNo) {
                    return this.HANDLE_CHANGE_PAGENO(1)
                }
                console.log(1)
                let routeQuery = this.$route.query
                // 如果是活动查询商品列表，走主页面逻辑
                if (routeQuery.activityType) return
                console.log(2, this.resetData)
                // 路由变化 需要重置数据
                if (this.resetData) { return this.handleResetData(routeQuery) }
                console.log('商品筛选列表')
                // 商品筛选列表
                await this.handleFilterGoods(newVal)

            }
        }
    },

    created() {
    },

    mounted() {
        console.log('执行mounted')
    },

    methods: {
        ...mapMutations(
            'searchGoodsResult',
            [
                'HANDLE_CHANGE_PAGENO',
                'HANDLE_BREADCRUMB_LIST'
            ]
        ),

        ...mapActions(
            'searchGoodsResult',
            [
                'handleSearchGoods',
                'handleFilterGoods'
            ]
        ),

        // 分类数据处理
        handleCategoryIds(categoryData) {
            let key = Object.keys(categoryData)[0]
            let val = categoryData[key]
            this.$set(this.categoryData, key, val)
        },

        // 属性数据处理
        handleAttrData(attrData) {
            this.attrData = attrData
        },

        // 筛选数据处理
        handleFilterData(filterData) {
            this.filterData = filterData
        },

        // 价格数据处理
        handlePriceData(priceData) {
            // Object.assign({}, priceData) 防止价格输入后 双向绑定实时刷新
            this.priceData = Object.assign({}, priceData)
        },

        // 切换商品列表样式
        hanldeChangeStyle(mode) {
            this.$set(this.goodsList, 'sortMethod', mode)
        },

        // 切换商品列表样式
        hanldeChangeStyle(mode) {
            this.$set(this.goodsCard, 'sortMethod', mode)
        },

        // 重置data数据
        handleResetData(routeQuery) {

            if (this.$route.name == 'proClassification') {
                // 商品分类页，只重置分类id
                this.$set(this.categoryData, 'gcIds', [routeQuery.classId])
            } else {
                this.categoryData = {
                    gcIds: routeQuery.storeId ? void (0) : [],
                    brandIds: [],
                    labelIds: []
                }

                this.filterData = {
                    sortField: '',
                    sortType: 'DESC',
                }

                this.priceData = {
                    minPrice: '',
                    maxPrice: ''
                }

                this.attrData = []
            }

            this.HANDLE_CHANGE_PAGENO(1)
            this.resetData = false
        }
    },

    destroyed() {
        this.HANDLE_CHANGE_PAGENO(1);
    }
}
