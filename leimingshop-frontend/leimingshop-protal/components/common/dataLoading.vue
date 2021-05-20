<template>
    <div class="loadMore">
        <vue-data-loading
                :loading="loading"
                :completed="completed"
                :offset="100"
                :listens="['infinite-scroll', 'pull-down']"
                :init-scroll="false"
                @infinite-scroll="infiniteScroll"
                @pull-down="pullDown">
            <div>
                <slot></slot>
            </div>  
            <div v-show="!completed" style="text-align: center; height: 50px;">  
                <p style="height: 22px;"><Icon style="height: 20px;width: 20px;margin:0 auto;" type="ios-loading" size=18 class="demo-spin-icon-load"></Icon></p> 
                <p style="font-size: 14px">加载中...</p>  
            </div>
            <div slot="completed" style="text-align: center; font-size: 14px"> 
                没有更多了
            </div>
        </vue-data-loading> 
    </div>
</template>
 
<script>
    import VueDataLoading from 'vue-data-loading'
    export default {
        name: 'loadMore',
        props:{
            completed:{
                type:Boolean,
                default:false
            }
        },
        components: {
            VueDataLoading
        },
        data() {
            return {
                loading: false,
                page: 1,
                isBack:false
            }
        },
        methods: {
            pullDown() {
                this.page = 1
                this.$emit('changeData',1)
            },
            infiniteScroll() {
                //到底触发的事件
                this.page++;
                this.$emit('changeData',this.page)
            },
        }
    }
</script> 
<style lang="scss" scoped>
/deep/ .demo-spin-icon-load{
    transform-origin: center center; 
    animation: ani-demo-spin 1s linear infinite;
    display: block;
}
@keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
}
/deep/ .demo-spin-col{
    height: 100px;
    position: relative;
    border: 1px solid #eee;
}
</style>