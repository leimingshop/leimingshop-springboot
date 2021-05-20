<template>
    <div
        class="specifiactions-body"
        v-if="goodsAttrVOList.length != 0"
    >
        <div
            class="specifiactions-box"
            v-for="(item, index) in goodsAttrVOList"
            :key="item.attrName"
        >
            <span class="specifiactions-name" v-text="item.attrName"></span>
            <span class="specifiactions-content" v-text="item.attrValueName"></span>
        </div>
    </div>

    <div
        v-else
        v-text="noData"
    ></div>
</template>

<script>
export default {
    name: 'specifiactions',
    data(){
        return{
            noData: '暂无规格参数'
        }
    },
    props: {
        goodsAttrVOList: {
            type: Array,
            default: () => []
        }
    }

}
</script>

<style lang="scss" scoped>
    .specifiactions-body{
        display: flex;
        flex-wrap: wrap;
        .specifiactions-box{
            width: 250px;
            min-height: 54px;
            border-bottom: 1px solid #ebebeb;
            margin: 0 0 0 30px;
            font-size: 14px;
            line-height: 54px;
            display: flex;
            &:nth-of-type(3n-2){
                margin-left: 0;
            }
            .specifiactions-name{
                margin: 0 18px 0 0;
                color: #717171;
                display: inline-block;
            }
            .specifiactions-content{
                color: #333;
                letter-spacing: 1px;
                word-break: break-word;
                display: inline-block;
                padding: 16px 0;
                line-height: 20px;
                flex: 1;
            }
        }
    }
</style>