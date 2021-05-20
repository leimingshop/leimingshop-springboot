<template>
<div>
    <!--列表-->
    <list v-if="couponIsshow==1" @changeCompent="changeCompent" ref="listCompent"></list>
    <!--新增或编辑-->
    <modelAddOrEdit v-if="couponIsshow==2" ref="modelAddOrEditCompon" @changePage="changePage"></modelAddOrEdit>
    <!--查看-->
    <detail v-if="couponIsshow==3" ref="detailCompon" @changePage="changePage"></detail>
    <!--领取明细-->
    <getReceive v-if="couponIsshow==4" ref="receiveCompon" @changePage="changePage"></getReceive>
</div>
</template>

<script>
    import modelAddOrEdit from "./model-add-or-edit.vue";
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
            getReceive
        },
        methods:{
            changeCompent(type,row){
                //type:1--新增，2--编辑，3--查看,4--明细
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
                }else{
                    this.couponIsshow = 4
                    this.$nextTick(()=>{
                        this.$refs.receiveCompon.init(row);
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
