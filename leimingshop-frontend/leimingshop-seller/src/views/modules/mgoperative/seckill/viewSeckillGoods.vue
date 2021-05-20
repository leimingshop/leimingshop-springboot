<template>
    <!--优惠券领取明细-->
    <div class="seckill">
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <div class="addButton" v-if="row.activityState == 0">
            <el-button class="btn" type="primary"  @click="changeCompent(5)"
                       v-if="$hasPermission('sys:seckill:edit')">添加秒杀商品
            </el-button>
        </div>
        <div class="line" v-if="row.activityState != 2"></div>
        <!--表格-->
        <el-table
                width="100%"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%;maigin-top:20px;"
        >
            <el-table-column prop="activityId" label="商品信息" align="center" min-width="150">
                <template slot-scope="scope">
                    <div class="goodsPropsWrap">
                        <div class="goodsImg">
                            <img :src="scope.row.goodsMainPicture | filterImgUrl" alt=""/>
                        </div>
                        <div class="towEllipsis" @click="previewH5Fn(scope.row)">
                            <div style="text-align:center;cursor: pointer;margin-left:10px" >
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                                    <span  style="color: #4e80db;float:left;font-size:15px;cursor: pointer;font-weight: bold;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 100%">
                                        {{scope.row.goodsName}}
                                    </span>
                                </el-tooltip>
                            </div>
                            <span style="margin-left:10px" class="goodsBrand">
                                ￥{{scope.row.specSellPrice}}
                            </span>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="activityStorage" label="活动库存" align="center" min-width="80"></el-table-column>
            <el-table-column prop="activitySurplusStorage" label="剩余库存" align="center" min-width="80"></el-table-column>
            <el-table-column prop="activityPrice" label="秒杀价" align="center" min-width="80">
                <template slot-scope="scope">
                    <div v-if="scope.row.activityPrice">
                        {{scope.row.activityPrice}}
                    </div>
                    <div v-else>
                        {{scope.row.minActivityPrice}}-{{scope.row.maxActivityPrice}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="spuOrderNum" label="秒杀订单" align="center" min-width="80"></el-table-column>
            <el-table-column label="操作" min-width="150" align="center" v-if="row.activityState != 2">
                <template slot-scope="scope">
                    <el-button size="mini" type="text"
                               v-if="row.activityState == 0 &&$hasPermission('sys:seckill:edit')"
                               @click="seckillGoodsSkuDetail(scope.row)">编辑
                    </el-button>
                    <el-button size="mini" type="text"
                               v-if="$hasPermission('sys:seckill:edit')"
                               @click="deleteSeckillGoods(scope.row.goodsId)">移除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

        <!--sku编辑弹框-->
        <editSku v-if="editSkuVisible" ref="editSkuCompent" @flushData="flushData"></editSku>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import editSku from "./editSku";
    import {seckillGoodsPage, deletSeckillGoods, seckillGoodsSkuDetail} from '@/api/api'

    export default {
        mixins: [mixinViewModule],
        name: "seckillGoods",
        data() {
            return {
                breaddata: ["营销系统", "秒杀活动", "管理秒杀商品"],
                editSkuVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: 'seller-api/seckill/goods/list',
                    getDataListIsPage: true,
                    activatedIsNeed: false
                },
                dataForm: {
                    activityId: '',
                },
                dataList: [],
                dataListLoading: false,
                row: '',
            }
        },
        components: {
            Bread,
            editSku
        },
        methods: {
            init(row) {
                this.row = row;
                this.dataForm.activityId = row.id;
                this.getDataList();
            },
            // 刷新列表数据
            flushData() {
                this.getDataList();
            },
            // 删除秒杀商品
            deleteSeckillGoods(goodsId) {
                this.$confirm('您确认移除该秒杀商品吗?', '提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定',
                    type: 'warning'
                }).then(() => {
                    let obj = {
                        data: {
                            activityId: this.row.id,
                            goodsIds: [goodsId]
                        }
                    };
                    deletSeckillGoods(obj).then((res => {
                        if (res.code == 200) {
                            this.$message({
                                message: res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                        } else {
                            this.$message({
                                message: res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                        this.flushData() // 刷新列表数据
                    }))
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            previewH5Fn(row){
                window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?specId='+row.specId);
            },
            // 编辑秒杀商品弹框
            seckillGoodsSkuDetail(row) {
                this.editSkuVisible = true;
                this.$nextTick(() => {
                    let obj = {
                        activityId: this.row.id,
                        activityState: this.row.activityState,
                        goodsId: row.goodsId,
                        goodsName: row.goodsName
                    };
                    this.$refs.editSkuCompent.init(obj);
                });
            },
            changePage() {
                this.$emit('changePage')
            },
            changeCompent(type, row) {
                this.$emit('changeCompent', type, this.row);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .seckill {
        .line {
            height: 10px;
            background: #ECEDF1;
            margin: 0 -20px;
        }
        .addButton {
            margin: 10px 30px;
        }
        .el-table--border {
            margin-top: 20px;
        }
    }
    // 商品
    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 100%;
        display: flex;
        align-items: center;

        .goodsImg {
            width: 70px;
            height: 70px;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsProps {
            width: 500px;
            height: 70px;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            color: #999999;

            .goodsNameTitle {
                display: inline-block;
                width: 50px;
                color: #666666FF;
            }

            .goodsBrandName {
                text-align: left;
            }

            .goodsName {
                display: flex;

                .goodsNameContent {
                    width: 150px;
                    text-align: left;
                    text-overflow: -o-ellipsis-lastline;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    display: -webkit-box;
                    -webkit-line-clamp: 2;
                    line-clamp: 2;
                    -webkit-box-orient: vertical;
                    color: #4e80db;
                    text-decoration: none;
                    cursor: pointer;
                }

            }
        }
    }
    .towEllipsis{
        text-align: left;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
    }
</style>
