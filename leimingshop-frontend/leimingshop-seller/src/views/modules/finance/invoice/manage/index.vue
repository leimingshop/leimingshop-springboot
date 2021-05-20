<template>
<div>
    <!--列表-->
    <list v-if="componentShowIndex==1" @changeCompent="changeCompent" ref="listComponent"></list>

    <!--查看-->
    <detail v-if="componentShowIndex==2" ref="detailComponent"  @changeCompent="changeCompent" @changePage="changePage"></detail>

</div>
</template>

<script>
    import list from "./list.vue";
    import detail from "./details.vue";
    export default {
        name: "index",
        data(){
            return{
                componentShowIndex: 1,
            }
        },
        components:{
            list,
            detail,
        },
        watch:{
            '$route.path'(newVal, oldVal) {
                if (this.$route.query.tabIndex == 2 && newVal == '/finance-invoice-manage') {
                    this.changeCompent(2,this.$route.query.row, )
                }
            },
        },
        created() {
            if (this.$route.query.tabIndex == 2) {
                this.changeCompent(2,this.$route.query.orderItem, )
            }
        },
        methods:{
            changeCompent(type,row){
                // if(type==2){
                    this.componentShowIndex = 2
                    this.$nextTick(()=>{
                        this.$refs.detailComponent.init(row,type);
                    })
                // }

            },
            //返回列表页
            changePage(){
                this.componentShowIndex = 1
                this.$nextTick(()=>{
                    this.$refs.listComponent.getData()
                })
            }
        }
    }
</script>

<style scoped>

</style>
