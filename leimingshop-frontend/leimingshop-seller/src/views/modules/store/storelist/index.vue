<template>
    <div>
        <!--列表-->
        <list v-show="storeIsshow==1 && !previewVisible " @changeCompent="changeCompent" @showPreview="showPreview" ref="listCompent"></list>
        <!--新增或编辑-->
        <modelAddOrEdit v-if="storeIsshow==2" ref="modelAddOrEditCompon" @changePage="changePage"></modelAddOrEdit>
        

        <!-- <shuttleNox></shuttleNox> -->
        <!-- 查看预览 -->
        <preview v-if="previewVisible" ref="previewCompon" @hiddenPreview="hiddenPreview"></preview>
    </div>
</template>

<script>
    import modelAddOrEdit from "./model-add-or-edit.vue";
    import list from "./list.vue";
    // import shuttleNox from "./modules/shuttleNox.vue"
    import preview from "./preview.vue";
    export default {
        name: "index",
        data(){
            return{
                storeIsshow: 1,
                previewVisible:false,//是否展示
            }
        },
        components:{
            list,
            modelAddOrEdit,
            // shuttleNox,
            preview
        },
        methods:{
            changeCompent(row){
                //type:1--新增，2--编辑
                    this.storeIsshow = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditCompon.init(row);
                    })

            },
            //返回列表页
            changePage(){
                this.storeIsshow = 1
                this.$nextTick(()=>{
                    this.$refs.listCompent.getStoreInfo()
                })
            },
            // 查看预览
            showPreview(row){
                this.previewVisible = true;
                this.$nextTick(()=>{
                    this.$refs.previewCompon.init(row);
                })
            },
            // 关闭预览
            hiddenPreview(){
                this.previewVisible = false;
            }, 
        }
    }
</script>

<style  lang="scss" scoped>

</style>
