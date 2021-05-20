<template>
    <div>
        <!--列表-->
        <list v-if="flashSalePage == 1"  @changeCompent="changeCompent" ref="listCompent"></list>
        <!--新增或编辑-->
        <modelAddOrEdit v-if="flashSalePage == 2" ref="modelAddOrEditCompent" @changeCompent="changeCompent" @changePage="changePage"></modelAddOrEdit>
        <!--活动详情-->
        <detail v-if="flashSalePage == 3" ref="detailCompent" @changePage="changePage" @changeCompent="changeCompent"></detail>
        <!-- 查看限时购活动商品 -->
        <viewFlashSaleGoods v-if="flashSalePage == 6" ref="viewFlashSaleGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></viewFlashSaleGoods>
        <!--管理限时购活动商品-->
        <flashSaleGoods v-if="flashSalePage == 4" ref="flashSaleGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></flashSaleGoods>
        <!--添加限时购活动商品-->
        <addFlashSaleGoods v-if="flashSalePage == 5" ref="addFlashSaleGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></addFlashSaleGoods>
    </div>
</template>

<script>
    import list from "./list.vue";
    import modelAddOrEdit from "./model-add-or-edit.vue";
    import detail from "./details.vue";
    import flashSaleGoods from "./flashSaleGoods.vue";
    import viewFlashSaleGoods from "./viewFlashSaleGoods.vue";
    import addFlashSaleGoods from "./addFlashSaleGoods.vue";

    export default {
        name: "index",
        data(){
            return{
                flashSalePage: 1 // 1:列表页 2:新增/修改 3:详情
            }
        },
        components:{
            list,
            modelAddOrEdit,
            detail,
            flashSaleGoods,
            addFlashSaleGoods,
            viewFlashSaleGoods
        },
        methods:{
            // 切换页面
            changeCompent(type,row){
                //type:1--新增，2--编辑，3--查看 4--限时购商品 5--添加限时购商品分页 6--复制
                if(type==1 || type ==2){
                    this.flashSalePage = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditCompent.init(type,row);
                    })
                }else if(type==3){
                    this.flashSalePage = 3
                    this.$nextTick(()=>{
                        this.$refs.detailCompent.init(row);
                    })
                }else if(type==4){
                    this.flashSalePage = 4
                    this.$nextTick(()=>{
                        this.$refs.flashSaleGoodsCompent.init(row);
                    })
                }else if(type==5){

                    this.flashSalePage = 5
                    this.$nextTick(()=>{
                        this.$refs.addFlashSaleGoodsCompent.init(row);
                    })
                }else if(type==6){
                    this.flashSalePage = 6
                    this.$nextTick(()=>{
                        this.$refs.viewFlashSaleGoodsCompent.init(row);
                    })
                }

            },
            // 刷新限时购活动列表
            changePage(){
                this.flashSalePage = 1
                this.$nextTick(()=>{
                    this.$refs.listCompent.getData()
                })
            }
        }
    }
</script>

<style  lang="scss" scoped>

</style>
