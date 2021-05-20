<template>
    <el-dialog
            width="70%"
            :visible.sync="editSkuVisible"
            title="编辑活动商品信息"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :append-to-body="true">
        <el-form :inline="true" ref="dataFormData" class="addOrEdit">
            <el-form-item label="商品名称:" style="font-weight: bold">
                <span style="font-size:15px;font-weight: 500;overflow: hidden;">{{goodsName}}</span>
            </el-form-item>
            <el-form-item label="活动库存:" style="font-weight: bold">
                <el-table
                        width="100%"
                        :data="dataList"
                        border=""
                        v-loading="dataListLoading"
                        style="width: 700px;maigin-top:20px;"
                >
                    <el-table-column prop="specAttrName" label="规格名称" align="center" min-width="80"></el-table-column>
                    <el-table-column prop="specStorage" label="商品库存" align="center" min-width="60"></el-table-column>
                    <el-table-column prop="activityStorage" label="限时抢购库存" align="center" min-width="100">
                        <template slot-scope="scope">
                            <el-input-number v-model="scope.row.activityStorage" size="small" controls-position="right" placeholder="" ></el-input-number>
                        </template>
                    </el-table-column>
                    <el-table-column prop="specSellPrice" label="销售价" align="center" min-width="60"></el-table-column>
                    <el-table-column prop="activityPrice" label="限时抢购价" align="center" min-width="100">
                        <template slot-scope="scope">
                            <el-input-number v-model="scope.row.activityPrice" size="small" controls-position="right" placeholder="" :min="0.01" :max="999999.99" ></el-input-number>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form-item>
            
        </el-form>
        <div slot="footer" style="text-align: center;">
            <el-button @click="cancel()">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="saveData()">{{ $t('confirm') }}</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {clearLoginInfo} from '@/utils'
    import {seckillGoodsSkuDetail, saveFlashSaleGoods} from '@/api/api'

    export default {
        data() {
            return {
                editSkuVisible: false,
                row: '',
                goodsName: '',
                dataListLoading: true,
                saveLoading: false,
                dataList: [],
                saveGoodsData: {
                    activityId: '',
                    goodsId: '',
                    skuList: []
                },
            }
        },
        computed: {},
        methods: {
            init(obj) {
                this.editSkuVisible = true;
                this.row = obj;
                this.saveGoodsData.activityId = this.row.activityId;
                this.saveGoodsData.goodsId = this.row.goodsId;
                // 查询sku活动信息
                this.getSeckillGoodsSkuDetail();
            },
            // 查询sku活动信息
            getSeckillGoodsSkuDetail() {
                let obj = {
                    params: {
                        goodsId: this.row.goodsId,
                        activityId: this.row.activityId,
                    }
                };
                seckillGoodsSkuDetail(obj).then((res => {
                    this.goodsName = this.row.goodsName;
                    if (res.code == 200) {
                        this.dataListLoading = false;
                        this.dataList = res.data;
                        res.data.forEach((item,index)=>{
                            if(item.activityStorage == undefined){
                                item.activityStorage = item.specStorage
                            }
                            if(item.activityPrice == undefined){
                                item.activityPrice =item.specSellPrice
                            }
                        })
                    } else {
                        this.dataListLoading = false;
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                }));
            },
            // 表单提交
            saveData() {
                if (!this.saveGoodsData.activityId) {
                    this.$message({
                        type: "warning",
                        message: "限时抢购活动id为必填参数"
                    });
                    return;
                }
                if (!this.saveGoodsData.goodsId) {
                    this.$message({
                        type: "warning",
                        message: "商品id为必填参数"
                    });
                    return;
                }
                if (!this.dataList || this.dataList.length == 0) {
                    // 未选择限时抢购商品，返回上一页
                    this.$message({
                        type: "warning",
                        message: "至少选择一件限时抢购商品"
                    });
                    return;
                } else {
                    this.saveGoodsData.skuList = [];
                    for (let i = 0; i <= this.dataList.length - 1; i++) {
                        if (this.dataList[i].activityStorage != undefined && this.dataList[i].activityPrice){
                            this.saveGoodsData.skuList.push(this.dataList[i]);
                        }
                    }
                    if (this.saveGoodsData.skuList.length == 0) {
                        // 未选择限时抢购商品，返回上一页
                        this.$message({
                            type: "warning",
                            message: "至少选择一件限时抢购商品"
                        });
                        return;
                    }
                }
                var  checkFlag = true ;
                this.dataList.forEach((item,index)=>{
                    this.saveGoodsData.skuList.forEach((item1,index1)=>{
                        if(item.specId == item1.specId ){
                            if(new Number(item1.activityPrice) > new Number(item.specSellPrice)){
                                this.$message({
                                    type: "warning",
                                    message: "活动价格不能小于商品售价"
                                });
                                checkFlag = false
                            }
                        }
                    })
                })
                if(!checkFlag){
                    return
                }
                this.dataListLoading = true;
                saveFlashSaleGoods(this.saveGoodsData).then(res => {
                    if (res.code == 200) {
                        this.dataListLoading = false;
                        this.dataList = [];
                        this.goodsName = '';
                        // 刷新上一个页面
                        this.$emit("flushData")
                        this.editSkuVisible = false;
                    } else {
                        this.dataListLoading = false;
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                });
            },
            // 取消sku选择
            cancel() {
                // 刷新上一个页面
                this.dataList = [];
                this.goodsName = '';
                this.$emit("flushData")
                this.editSkuVisible = false;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit {
        .hrTips{
            color: #999999;
            margin-left: 150px;
            margin-bottom: 10px;
        }
        .fullTitle {
            font-weight: 700;
            font-size: 25px;
        }
        .title-1 {
            margin-left: -60px;
        }
        margin-left: 10%;
        .el-form-item {
            display: flex;
        }
        .el-input {
            width: 400px !important;
        }
        /deep/ .el-form-item__label {
            margin-right: 20px;
            min-width: 80px;
            text-align: right;
        }
        /deep/.el-input{
            width: 100% !important;
            /deep/.el-input-number__decrease{
                line-height: 16px;
            }
        }
        .displayName {
            display: inline-block;
        }
    }
</style>
