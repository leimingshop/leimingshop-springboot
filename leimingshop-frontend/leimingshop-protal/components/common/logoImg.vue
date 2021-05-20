<template>
    <img @click="$router.push('/')" class="imgage" :src="$imgURL + IndexSiteData.logoNow" alt />
</template>

<script>
    import {
        mapState,
    } from 'vuex'; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "IndexSiteData"
            ]),
            
        },
        mounted() {
        },
        methods:{
        }

    }

</script>

<style lang="scss" scoped>
    img{
        height:60px;
        width:auto;
        cursor: pointer;
    }
</style>
