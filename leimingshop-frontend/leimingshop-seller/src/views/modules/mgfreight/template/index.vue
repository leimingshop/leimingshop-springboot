<template>
<div>
    <!--列表-->
    <list ref="listCompent" v-if="freightTemplatePage==1" @changeCompent="changeCompent"></list>
    <!--新增或编辑-->
    <modelAddOrEdit ref="modelAddOrEditCompon" v-if="freightTemplatePage==2" @changePage="changePage"></modelAddOrEdit>
</div>
</template>

<script>
    import modelAddOrEdit from "./addOrEdit.vue";
    import list from "./list.vue";
    import updateGoodsTemplate from "./updateGoodsTemplate.vue";
    export default {
        name: "index",
        data(){
            return{
                freightTemplatePage: 1,
            }
        },
        components:{
            list,
            updateGoodsTemplate,
            modelAddOrEdit
        },
        activated () {
            if(this.$route.query.type){
                let  type= this.$route.query.type
                this.changeCompent(type, '')
            }



        },

        methods:{
            changeCompent(type,row){
                //type:1--新增，2--编辑，3-复制
                if(type==1 || type ==2 || type == 3){
                    this.freightTemplatePage = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditCompon.init(row, type);
                    })
                }

            },
            //返回列表页
            changePage(){
                this.freightTemplatePage = 1;
                this.$nextTick(()=>{
                    this.$refs.listCompent.getData()
                })
            }
        }
    }
</script>

<style scoped>

</style>
