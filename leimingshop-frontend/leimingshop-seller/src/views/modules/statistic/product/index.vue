<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
            <div style="display: flex">
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert
            title="操作提示"
            type="warning"
            @close="$store.commit('showListMessage')"
            v-show="$store.state.listMessageFlag"
        >
            <template slot="title">
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">
                    1、商品分类销售分析数据统计图展示了指定时间段内一级分类商品的销售占比
                </div>
                <div class="iconSize">
                    2、单个商品销售情况数据统计图展示了指定时间段内销量前十的商品销售数量
                </div>
                <div class="iconSize">
                    3、单品销售明细数据统计图展示了指定时间段内前十的商品销售情况明细
                </div>
            </template>
        </el-alert>

        <!-- 分类商品统计 -->
        <!-- <goodsClassStatic></goodsClassStatic> -->
        <!-- 单个商品销售情况 -->
        <singGoodsSale></singGoodsSale>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    // import goodsClassStatic from "./goodsClassStatic"
    import singGoodsSale from "./singGoodsSale";
    export default {
        data() {
            return {
                breaddata: ["统计", "商品分析", "商品统计"],

                chartData: {
                    columns: ["分类名称", "销售数量"],
                    rows: [
                        { 分类名称: "彩妆", 销售数量: "100" },
                        { 分类名称: "食品", 销售数量: "100" },
                        { 分类名称: "家电", 销售数量: "100" },
                        { 分类名称: "厨具", 销售数量: "100" },
                        { 分类名称: "男装", 销售数量: "100" },
                    ],
                },

                //   starttime:{
                //     disabledDate:time=>{
                //       if (this.temp.endTime){
                //         return (
                //           time.getTime()>new Date(this.temp.endTime).getTime()
                //         )
                //       }else{
                //         return time.getTime()>Date.now()
                //       }
                //     }
                //   },
                //   endTime:{
                //     disabledDate:time=>{
                //       if (this.temp.starttime){
                //       return(
                //         time.getTime()>Date.now()||
                //         time.getTime()<new Date(this.temp.starttime).getTime()-8.64e7
                //       )
                //     }else{
                //       return time.getTime()<Date.now()
                //     }
                //   }
                // },
            };
        },
        components: {
            Bread,
            //  goodsClassStatic,
            singGoodsSale,
        },
        mounted() {},
        methods: {},
    };
</script>
<style lang="scss" scoped>
    // /deep/ canvas{
    // width: 1700px !important;
    // }
</style>