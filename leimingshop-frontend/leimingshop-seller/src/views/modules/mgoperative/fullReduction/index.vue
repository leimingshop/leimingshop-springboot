<template>
    <div>
        <!--列表-->
        <list v-if="fullIsShow == 1"  @changeCompent="changeCompent" ref="listCompent"></list>
        <!--新增或编辑-->
        <modelAddOrEdit v-if="fullIsShow == 2" ref="modelAddOrEditFullRed" @changePage="changePage"></modelAddOrEdit>
        <!--查看-->
        <detail v-if="fullIsShow==3" ref="detailCompon" @changePage="changePage"></detail>
    </div>
</template>

<script>
    import list from "./list.vue";
    import detail from "./details.vue";
    import modelAddOrEdit from "./model-add-or-edit.vue";
    export default {
        name: "index",
        data(){
            return{
                fullIsShow : 1,

            }
        },
        components:{
            list,
            modelAddOrEdit,
            detail
        },
        methods:{
            changeCompent(type,row){
                //type:1--新增，2--编辑，3--查看
               if(type==1 || type ==2){
                    this.fullIsShow = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditFullRed.init(row);
                    })
                }else if(type==3){
                   this.fullIsShow = 3
                   this.$nextTick(()=>{
                       this.$refs.detailCompon.init(row);
                   })
                }
            },
            changePage(){
                this.fullIsShow = 1
                this.$nextTick(()=>{
                    this.$refs.listCompent.getData()
                })
            }
        }
    }
</script>

<style  lang="scss" scoped>

</style>
