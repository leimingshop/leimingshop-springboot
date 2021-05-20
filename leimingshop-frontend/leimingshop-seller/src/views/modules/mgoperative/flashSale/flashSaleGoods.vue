<template>
    <!--优惠券领取明细-->
    <div class="seckill">
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form
                :inline="true"
                ref="dataForm"
                :model="dataForm"
                v-show="showTable == 0"
        >
            <el-form-item label="输入搜索：">
                <el-input v-model="dataForm.goodsName" placeholder="商品名称/商品货号" clearable></el-input>
            </el-form-item>
            <el-form-item label="商品状态：" prop="goodsShow">
                <el-select v-model="dataForm.goodsShow" placeholder="活动状态">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="下架" value="0">下架</el-option>
                    <el-option label="上架" value="1">上架</el-option>
                    <el-option label="未上架" value="2">未上架</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="商品分类：">
                <el-cascader
                        clearable=""
                        v-model="gcIds"
                        :options="goodscalssOption"
                        :props="props"
                        @change="finishCange()"
                ></el-cascader>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" type="primary" @click="getData()">查询</el-button>
                <el-button class="btn" type="primary" plain @click="reset('dataForm')">重置</el-button>
            </el-form-item>
        </el-form>
        <!--表格-->
        <el-radio-group v-model="activeName" @change="handleClick" style="margin-top: 5px;margin-bottom: 5px">
            <el-radio-button label="added">已添加</el-radio-button>
            <el-radio-button label="notadd">未添加</el-radio-button>
        </el-radio-group>
        <!--未添加商品表格-->
        <div class="table-box" v-show="showTable == 0">
            <flashSaleAddGoods ref="firstSkuList"></flashSaleAddGoods>
        </div>
        <!--已添加商品表格-->
        <div class="table-box" v-show="showTable == 1">
            <flashSaleAddedGoods ref="secSkuList"></flashSaleAddedGoods>
        </div>

        <!--sku编辑弹框-->
        <editSku v-if="editSkuVisible" ref="editSkuCompent" @flushData="flushData"></editSku>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import editSku from "./editSku";
    import flashSaleAddedGoods from "./flashSaleAddedGoods";
    import flashSaleAddGoods from "./flashSaleAddGoods";
    import {seckillGoodsPage, allTreeGoodsclass,deletSeckillGoods, seckillGoodsSkuDetail} from '@/api/api'

    export default {
        mixins: [mixinViewModule],
        name: "seckillGoods",
        data() {
            return {
                breaddata: ["营销系统", "限时抢购活动", "管理限时抢购商品"],
                editSkuVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: 'seller-api/activity/goods/flash/sale/already',
                    getDataListIsPage: true,
                    activatedIsNeed: false
                },
                dataList: [],
                dataListLoading: false,
                row: '',
                activeName:"notadd",
                showTable:0,

                // 未添加商品筛选
                dataForm: {
                    goodsName: '',   //商品名称
                    goodsShow: '',   //商品名称
                    gcId: '',   //商品分类
                    activityId: '',
                },
                gcIds: [],
                props: {
                    value: "id",
                    label: "gcName"
                },
                activityId: '',
                activityState: '',
                goodscalssOption: [],
                startControlTips: false,
            }
        },
        components: {
            Bread,
            editSku,
            flashSaleAddedGoods,
            flashSaleAddGoods
        },
        created() {
            this.getGoodsClassList();
        },
        methods: {
            init(row) {
                this.row = row;
                console.log(this.row,"wanl")
                this.dataForm.activityId = this.row.id;
                this.$nextTick(() => {
                    this.handleClick();
                })
            },
            // 分类下拉框
            handleItemChange(val) {
                console.log('active item:', val, typeof val);
                // console.log(val);
                var val1 = [];
                if (val && typeof (val) == "string") {
                    val1 = JSON.parse(val);
                } else {
                    val && val.forEach((item, index) => {
                        if (typeof (item) == "string") {
                            val1.push(JSON.parse(item))
                        } else {
                            val1.push(item)
                        }
                    })
                }
                if (val1)
                // console.log(val1)
                    var id = 0
                let takeBlack = true
                if (Object.prototype.toString.call(val1) === '[object Object]') {
                    id = val1.id ? val1.id : 0;
                } else if (Object.prototype.toString.call(val1) === '[object Array]' && val1.length > 0) {
                    id = val1[val1.length - 1].id;
                    if (val1.length == 2) takeBlack = false
                }
            },
            finishCange() {
                let len = this.gcIds.length;
                if (len > 0) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
            },
            // 商品分类列表
            getGoodsClassList() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOption = res.data
                    } else {
                        // this.$message.error(res.msg);
                    }
                })
            },
            getData() {
                this.$nextTick(()=>{
                    this.$refs.firstSkuList.init(this.dataForm, this.row);
                })
            },
            reset() {
                this.dataForm = {
                    goodsName: '',
                    goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
                    gcId: '',
                }
                this.gcIds = [];//分类ID
                this.$nextTick(()=>{
                    this.$refs.firstSkuList.init(this.dataForm, this.row);
                })
            },
            // 刷新列表数据
            flushData() {
                this.getDataList();
            },
            handleClick(tab) {

                var tab = this.activeName;
                if (tab == "added") {
                    this.showTable = 1;
                    this.$nextTick(()=>{
                        this.$refs.secSkuList.init(this.row);
                    })
                } else if (tab == "notadd") {
                    this.showTable = 0;
                    this.$nextTick(()=>{
                        this.$refs.firstSkuList.init(this.dataForm, this.row);
                    })

                }

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
