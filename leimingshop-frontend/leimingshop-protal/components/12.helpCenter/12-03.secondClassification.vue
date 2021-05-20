<template>
    <div class="product-categories">
        <Menu ref="menus" width="160px" :open-names="openNames" accordion @on-select="handleMenuSelect">
            <Submenu v-for="(ITEM, INDEX) in shopClassifyList" :key="ITEM.gcName" :name="`${INDEX}`">
                <template slot="title">
                    <span :title="ITEM.gcName">{{ITEM.gcName}}</span>
                </template>

                <MenuItem v-for="(item, index) in ITEM.children" :key="item.id" :name="`${INDEX}-${index}`"
                    :class="{active: selectVal == item.id}">
                <span :title="ITEM.gcName">{{item.gcName}}</span>
                </MenuItem>
            </Submenu>
        </Menu>
    </div>
</template>

<script>
export default {
    name: 'helpCenter',
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
                this.selectVal = newVal.query.storeGcIds

                let tempArr = this.shopClassifyList
                tempArr.forEach((ITEM, INDEX) => {
                    let childIndex = ITEM.children.findIndex((item, index) => {
                        return item.id == newVal.query.storeGcIds
                    })

                    if (childIndex != -1) {
                        this.$set(this.openNames, 0, `${INDEX}`)
                    }
                })
            }
        },

        openNames: {
            immediate: true,
            async handler(newVal, oldVal) {
                await this.$nextTick()
                this.$refs.menus.updateOpened();
                this.$refs.menus.updateActiveName();
            }
        }
    },

    methods: {
        handleMenuSelect(name) {
            let tempArr = name.split('-')
            let tempStr = this.shopClassifyList[tempArr[0]]['children'][tempArr[1]]['id']

            // 判断取消选中
            this.selectVal = this.selectVal == tempStr ? '' : tempStr

            this.$router.replace({
                name: 'shopProClassification',
                query: {
                    storeId: this.$route.query.storeId,
                    storeGcIds: this.selectVal,
                    posi: 'none'
                }
            })
        }
    }
}
</script>

<style>

</style>