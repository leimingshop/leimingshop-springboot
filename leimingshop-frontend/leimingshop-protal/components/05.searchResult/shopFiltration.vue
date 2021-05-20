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
                    :class="['filtration-btn', {active: index == tabIndex}]"
                    @click="handleFilter(item, index)"
                    
                >
                    {{ item.title }}
                    <i :class="[{'arrow-down-btn': index == 3}]"> </i>
                </Button>
            </div>

            <!-- 店铺数量 -->
            <div class="shop-num" v-html="shopNumText"></div>
        </div>

    </div>
</template>

<script>
    export default{
        name: 'productFiltration', //店铺分类列表页 - 商品筛选过滤工具栏

        data(){
            return{
                sortType: [
                    {
                        title: '综合',
                        sortField: '', // {'' : '综合'}, {'saleNum': '销量'}, { 'specSellPrice': '价格'}
                        sortType: 'DESC'  // {'DESC': '降序' }, {'ASC': '升序'}
                    },
                    {
                        title: '销量',
                        sortField: 'saleNum',
                        sortType: 'DESC'
                    },
                    // {
                    //     title: '新品',
                    //     sortField: 'goodsUpTime',
                    //     sortType: 'DESC'
                    // }, // 暂时没有
                ],

                filterData:{
                    sortField: '',
                    sortType: 'DESC'
                },

                tabIndex: 0, //过滤条件默认选中下标
            }
        },

        props: {
            totalCount:{
                type: Number,
                default: 0
            }
        },

        computed:{
            shopNumText(){
                return `找到相关店铺&nbsp;<span>${this.totalCount}</span>&nbsp;家`
            }
        },

        watch:{
            '$route':{
                immediate: true,
                handler(newVal, oldVaL){
                    this.$set(this.filterData, 'sortField', '')
                }
            }
        },

        mounted(){

        },

        methods:{
            handleSortTypeStyle(index){
                this.$refs.sortType.style.setProperty("--active-index", index);
            },

            // 店铺过滤处理
            handleFilter(param, index){
                this.tabIndex = index

                this.handleSortTypeStyle(index)

                let filterData = {
                    sortField: param.sortField,
                    sortType: param.sortType
                }
                this.filterData = filterData
                this.$emit('handleFilterData', filterData)
            },

        }
    }
</script>

<style lang="scss" scoped>
    $primary-color: #DD2619;

    .sort-wrap{
        .sort-type{
            float: left;
            display: flex;
            position: relative;
            &::after{
                position: absolute;
                z-index: 1;
                left: 0;
                top: 0;
                content: '';
                display: block;
                width: 70px;
                height: 34px;
                background: #F6F6F6;
                color: $primary-color;
                transform: translateX( calc(70px * var(--active-index) ) );
                transition: all .3s;
            }
        }
        .shop-num{
            float: right;
            height: 34px;
            line-height: 34px;
            color: #666666;
            /deep/ span{
                color: $primary-color;
            }
        }
    }

    .product-filtration{
        width: 100%;
        height: 46px;
        padding: 6px 30px 6px 20px;
        box-sizing: border-box;
        background: #FFFFFF;
    }
    .filtration-btn{
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 2;
        width: 70px;
        height: 34px;
        color: #3A3A3A;
        font-size: 13px;
        border-radius: 0;
        &.active{
            background: transparent;
            color: #DD2619;
        }
        &.active{
            font-weight: 600;
        }
    }
</style>