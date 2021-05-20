<template>
    <div>
        <!--列表-->
        <list v-if="seckillPage == 1"  @changeCompent="changeCompent" ref="listCompent"></list>
        <!--新增或编辑-->
        <modelAddOrEdit v-if="seckillPage == 2" ref="modelAddOrEditCompent" @changePage="changePage" @changeCompent="changeCompent"></modelAddOrEdit>
        <!--活动详情-->
        <detail v-if="seckillPage == 3" ref="detailCompent" @changePage="changePage" @changeCompent="changeCompent"></detail>
        <!--查看秒杀活动商品-->
        <viewSeckillGoods v-if="seckillPage == 6" ref="viewSeckillGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></viewSeckillGoods>
        <!--管理秒杀活动商品-->
        <seckillGoods v-if="seckillPage == 4" ref="seckillGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></seckillGoods>
        <!--添加秒杀活动商品-->
        <addSeckillGoods v-if="seckillPage == 5" ref="addSeckillGoodsCompent" @changePage="changePage" @changeCompent="changeCompent"></addSeckillGoods>
    </div>
</template>

<script>
    import list from "./list.vue";
    import modelAddOrEdit from "./model-add-or-edit.vue";
    import detail from "./details.vue";
    import seckillGoods from "./seckillGoods.vue";
    import viewSeckillGoods from "./viewSeckillGoods.vue";
    import addSeckillGoods from "./addSeckillGoods.vue";

    export default {
        name: "index",
        data(){
            return{
                seckillPage: 1 // 1:列表页 2:新增/修改 3:详情
            }
        },
        components:{
            list,
            modelAddOrEdit,
            detail,
            seckillGoods,
            addSeckillGoods,
            viewSeckillGoods
        },
        methods:{
            // 切换页面
            changeCompent(type,row){
                console.log('6666666666');
                //type:1--新增，2--编辑，3--查看 4--秒杀商品 5--添加秒杀商品分页 6--复制
                if(type==1 || type ==2){
                    this.seckillPage = 2;
                    this.$nextTick(()=>{
                        this.$refs.modelAddOrEditCompent.init(row, type);
                    })
                }else if(type==3){
                    this.seckillPage = 3
                    this.$nextTick(()=>{
                        this.$refs.detailCompent.init(row);
                    })
                }else if(type==4){
                    this.seckillPage = 4
                    this.$nextTick(()=>{
                        this.$refs.seckillGoodsCompent.init(row);
                    })
                }else if(type==5){
                    this.seckillPage = 5
                    this.$nextTick(()=>{
                        this.$refs.addSeckillGoodsCompent.init(row);
                    })
                }else if(type==6){
                    this.seckillPage = 6
                    this.$nextTick(()=>{
                        this.$refs.viewSeckillGoodsCompent.init(row);
                    })
                }

            },
            // 刷新秒杀活动列表
            changePage(){
                this.seckillPage = 1
                this.$nextTick(()=>{
                    this.$refs.listCompent.getData()
                })
            }
        }
    }
</script>

<style  lang="scss" scoped>

</style>
