<template>
<div>
    <!--列表-->
    <list v-if="couponIsshow==1" @changeCompent="changeCompent" ref="listCompent"></list>
    <!--新增或编辑-->
    <modelAddOrEdit v-if="couponIsshow==2" ref="modelAddOrEditCompon"
                    @changeCompent="changeCompent"
                    @changePage="changePage"></modelAddOrEdit>
    <!--查看-->
    <detail v-if="couponIsshow==3" ref="detailCompon"  @changeCompent="changeCompent" @changePage="changePage"></detail>
    <!--领取明细-->
    <getReceive v-if="couponIsshow==4" ref="receiveCompon" @changePage="changePage"></getReceive>
    <!--添加拼团商品-->
    <groupGoodsListAdd v-if="couponIsshow==5" ref="groupGoodsListRef"
                       @changeCompent="changeCompent"
                       @changePage="changePage"></groupGoodsListAdd>
    <!--管理拼团商品-->
    <groupGoodsListAdd v-if="couponIsshow==6" ref="groupGoodsListRef"
                       @changeCompent="changeCompent"
                       @changePage="changePage"></groupGoodsListAdd>
    <!--查看拼团商品-->
    <groupGoodsListSee v-if="couponIsshow==7" ref="groupGoodsListSeeRef"
                        @changeCompent="changeCompent"
                        @changePage="changePage"></groupGoodsListSee>
</div>
</template>

<script>
    import modelAddOrEdit from "./model-add-or-edit.vue";
    import groupGoodsListAdd from "./groupGoodsListAdd";
    import groupGoodsListEdit from "./groupGoodsListEdit";
    import groupGoodsListSee from "./groupGoodsListSee";
    import list from "./list.vue";
    import detail from "./details.vue";
    import getReceive from "./get-receive.vue";
    export default {
        name: "index",
        data(){
            return{
                couponIsshow: 1,
            }
        },
        components:{
            list,
            modelAddOrEdit,
            detail,
            getReceive,
            groupGoodsListAdd,
            groupGoodsListEdit,
            groupGoodsListSee
        },
        methods:{
            changeCompent(type,row){
                console.log(type)
                console.log(row)
                //type:1--新增，2--编辑，3--查看,4--明细 ,5 查看活动商品
                if(type==3){
                    this.couponIsshow = 3
                    this.$nextTick(()=>{
                        this.$refs.detailCompon.init(row);
                    })
                }else if(type==1 || type ==2){
                    this.couponIsshow = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditCompon.init(row);
                    })
                }else if(type==4){
                    this.couponIsshow = 4
                    this.$nextTick(()=>{
                        this.$refs.receiveCompon.init(row);
                    })
                }else if(type==5){
                    this.couponIsshow = 5
                    this.$nextTick(()=>{
                        this.$refs.groupGoodsListRef.init(row);
                    })
                }else if(type==6){
                    this.couponIsshow = 6
                    this.$nextTick(()=>{
                        this.$refs.groupGoodsListRef.init(row);
                    })
                }else if(type==7){
                    this.couponIsshow = 7
                    this.$nextTick(()=>{
                        this.$refs.groupGoodsListSeeRef.init(row);
                    })
                }


            },
            //返回列表页
            changePage(){
                this.couponIsshow = 1
                this.$nextTick(()=>{
                    this.$refs.listCompent.getData()
                })
            }
        }
    }
</script>

<style scoped>

</style>
