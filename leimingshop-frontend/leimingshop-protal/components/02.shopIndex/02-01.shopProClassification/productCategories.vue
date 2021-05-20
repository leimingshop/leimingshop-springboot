<template>
    <div class="product-categories" v-show="shopClassifyList.length != 0">
        <h2>{{ classifyTitle }}</h2>

        <Menu ref="menus" width="160px" :open-names="['0']" @on-select="handleMenuSelect" accordion>
            <Submenu v-for="(ITEM, INDEX) in shopClassifyList" :key="ITEM.gcName" :name="`${INDEX}`">
                <template slot="title">
                    <span :title="ITEM.gcName">{{ITEM.gcName}}</span>
                </template>

                <MenuItem v-for="(item, index) in ITEM.children" :key="item.id" :name="`${INDEX}-${index}`"
                    :class="{active: selectVal == item.id}">
                    <span :title="item.gcName">{{item.gcName}}</span>
                </MenuItem>
            </Submenu>
        </Menu>
        <!-- accordion -->
    </div>
</template>

<script>
    import {
        mapState
    } from 'vuex'

    export default {
        name: 'productCategories', //店铺分类列表页 - 宝贝分类

        data() {
            return {
                classifyTitle: '宝贝分类',
                selectVal: '', // 菜单选中的id
                openNames: [] // 激活菜单的 name 值
            }
        },

        props: {
            shopClassifyList: {
                type: Array,
                default: () => []
            }
        },

        computed: {
            ...mapState({
                product: state => state['shop'].proClassification['product'] // 宝贝分类
            })
        },

        watch: {
            '$route': {
                immediate: true,
                handler(newVal, oldVal) {
                    // 菜单默认选中值
                    this.handleDefaultSelect(this.shopClassifyList)
                }
            },

            'shopClassifyList':{
                immediate: true,
                handler(newVal, oldVal){
                    this.handleDefaultSelect(newVal)
                }
            },

            'openNames': {
                immediate: true,
                async handler(newVal, oldVal) {
                    await this.$nextTick()
                    this.$refs.menus.updateOpened();
                    this.$refs.menus.updateActiveName();
                }
            }
        },

        created() {},

        methods: {
            // 展开菜单项，默认选中值
            handleDefaultSelect(list){
                if( list.length == 0 ) return
                let routeQuery = this.$route.query

                this.selectVal = routeQuery.storeGcIds

                list.forEach((ITEM, INDEX) => {
                    if( ITEM.children ){
                        let childIndex = ITEM.children.findIndex((item, index) => {
                            return item.id == routeQuery.storeGcIds
                        })

                        this.$set(this.openNames, INDEX, `${INDEX}`)

                        if (childIndex != -1) {
                            this.$emit('handleSelectFirstClass', ITEM.gcName)
                        }
                    }
                })
            },

            // 点击菜单回调
            handleMenuSelect(name) {
                let tempArr = name.split('-')
                let tempStr = this.shopClassifyList[tempArr[0]]['children'][tempArr[1]]['id']

                // 判断取消选中，暂时关闭该功能
                // this.selectVal = this.selectVal == tempStr ? '' : tempStr
                this.selectVal = tempStr

                this.$router.replace({
                    name: 'shopProClassification',
                    query: {
                        storeId: this.$route.query.storeId,
                        storeGcIds: this.selectVal,
                        autoFixed: 'true',
                        t: Date.now() // 此参数作用为重置页面
                    }
                })
            }
        }
    }

</script>


<style lang="scss" scoped>
    .product-categories {
        width: 160px;
        min-height: 586px;
        box-sizing: border-box;
        background: #FFFFFF;

        /* 待删 */
        h2 {
            width: 160px;
            height: 46px;
            line-height: 46px;
            background: #DD2619;
            padding-left: 30px;
            font-size: 15px;
            color: #FFFFFF;
            font-family: PingFangSC-Regular, PingFang SC;
        }

        .ivu-menu {
            width: 160px;
            padding: 0 0 180px;
            z-index: 1;

            /deep/ .ivu-menu-submenu {
                border-bottom: 1px solid #EBEBEB;
            }

            /deep/ .ivu-menu-submenu-title {
                padding: 15px 45px 15px 30px;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                font-size: 15px;
                color: #303030;

                .ivu-icon {
                    font-size: 18px;
                    color: #666666;
                    top: 48%;
                }
            }

            /deep/ .ivu-menu-item {
                font-size: 13px;
                color: #939393;
                background: none;
                padding: 0 10px 15px 30px !important;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;

                &:after {
                    display: none;
                }

                &:hover,
                &.active {
                    color: #DD2619;
                }

                &.active {
                    font-weight: 600;
                }
            }

            &.ivu-menu-vertical.ivu-menu-light:after {
                display: none;
            }
        }
    }

</style>
