<template>
    <div class="seckill">
        <!--表格-->
        <el-table
                width="100%"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%;maigin-top:20px;"
        >

            <el-table-column prop="activityId" label="商品信息" align="center" min-width="360">
                <template slot-scope="scope">
                    <div class="goodsPropsWrap" @click="previewH5Fn(scope.row)" >
                        <div class="goodsImg">
                            <img :src="scope.row.goodsMainPicture | filterImgUrl" alt=""/>
                        </div>
                        <div class="goodsProps">
                            <span v-if="scope.row.activityTypeList && scope.row.activityTypeList.length > 0"
                                  style="font-size:15px;color:red;font-weight: bold;white-space: nowrap;overflow: hidden;">
                            </span>
                            <span v-for="item in scope.row.activityTypeList" class="goodsname">
                                <span v-if="item == 1">券</span>
                                <span v-if="item == 2">减</span>
                                <span v-if="item == 3">秒</span>
                                <span v-if="item == 4">团</span>
                            </span>
                            <el-tooltip class="item" effect="dark" :content="scope.row.goodsName" placement="top-start">
                                <span style="color: #4e80db;margin-left:8px" class="goodsNameContent">
                                    {{scope.row.goodsName}}
                                </span>
                            </el-tooltip>
                        </div>
                    </div>
                </template>
                <!--<template slot-scope="scope">
                    <div>
                        <span v-if="scope.row.activityTypeList && scope.row.activityTypeList.length > 0"
                              style="font-size:15px;color:red;font-weight: bold;white-space: nowrap;overflow: hidden;">
                            <span v-for="item in scope.row.activityTypeList">
                                <span v-if="item == 1"> 券&nbsp; </span>
                                <span v-if="item == 2"> 减&nbsp; </span>
                                <span v-if="item == 3"> 秒&nbsp; </span>
                                <span v-if="item == 4"> 团&nbsp; </span>
                            </span>
                        </span>
                        <span style="font-size:15px;color:#333;font-weight: bold;white-space: nowrap;overflow: hidden;">
                            {{scope.row.goodsName}}
                        </span>
                    </div>
                </template>-->
            </el-table-column>
            <el-table-column prop="specSellPrice" label="销售价" align="center" min-width="80"></el-table-column>
            <el-table-column prop="activityState" label="商品状态" align="center" width="80">
                <template slot-scope="scope">
                    <div v-if="scope.row.goodsShow==0">下架</div>
                    <div v-else-if="scope.row.goodsShow==1">上架</div>
                    <div v-else-if="scope.row.goodsShow==2">未上架</div>
                </template>
            </el-table-column>
            <el-table-column prop="saleNum" label="实际销量" align="center" min-width="80"></el-table-column>
            <el-table-column prop="specStorage" label="商品库存" align="center" min-width="80"></el-table-column>
            <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="text"
                               v-if="scope.row.operationType == 0 && $hasPermission('sys:seckill:edit')"
                               @click="seckillGoodsSkuDetail(scope.row)">添加
                    </el-button>
                    <el-button size="mini" type="text"
                               v-if="scope.row.operationType == 1 && (scope.row.activityTypeList[0] != 3 || (scope.row.activityTypeList[0] == 3 && scope.row.activityId != activityId)) && $hasPermission('sys:seckill:edit')"
                               disabled="">添加
                    </el-button>
                    <el-button size="mini" type="text"
                               v-show="scope.row.operationType == 1 && scope.row.activityTypeList[0] == 3 && scope.row.activityId == activityId && $hasPermission('sys:seckill:edit')">
                        <span @click="deleteGoodsBatch(scope.row.id)"
                              @mouseenter="Onmouseenter(scope.row,scope.$index)"
                              @mouseleave="Onmouseleave(scope.row,scope.$index)">{{scope.row.btnText}}</span>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
                @size-change="pageSizeChange"
                @current-change="pageCurrentChange"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

<!--        <div style="text-align: center;">-->
<!--            <el-button class="btn" type="primary" plain @click="changeCompent(4)"-->
<!--                       v-if="$hasPermission('sys:seckill:edit')">返回管理商品-->
<!--            </el-button>-->
<!--        </div>-->

        <!--sku编辑弹框-->
        <editSku v-if="editSkuVisible" ref="editSkuCompent" @flushData="flushData"></editSku>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import editSku from "./editSku";
    import {addSeckillGoodsPage, allTreeGoodsclass, seckillGoodsSkuDetail, deletSeckillGoods} from '@/api/api'

    export default {
        mixins: [mixinViewModule],
        name: "addSeckillGoods",
        data() {
            return {
                editSkuVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/seckill/goods/add/list",
                    getDataListIsPage: true,
                    activatedIsNeed: false
                },
                btnText: '已添加',
                dataForm: {
                    goodsName: '',   //商品名称
                    goodsShow: '',   //商品名称
                    gcId: '',   //商品分类
                },
                gcIds: [],
                props: {
                    value: "id",
                    label: "gcName"
                },
                activityId: '',
                activityState: '',
                dataList: [],
                goodscalssOption: [],
                dataListLoading: false,
                startControlTips: false,
            }
        },
        components: {
            Bread,
            editSku
        },
        created() {
            this.getGoodsClassList();
        },
        methods: {
            // 查询商品列表
            init(dataForm,row) {
                Object.assign(this.dataForm,dataForm)

                this.activityId = row.id;
                this.activityState = row.activityState;
                this.handleItemChange();
                // this.getData();
                this.awaitgetData();
            },
            async awaitgetData() {
                await this.getDataList();
                this.dataList.forEach(v => {
                    this.$set(v, 'btnText', '已添加')
                })
            },
            getData() {
                this.page = 1;
                this.awaitgetData();
            },
            // 分页, 每页条数
            pageSizeChange (val) {
                this.page = 1
                this.limit = val
                this.awaitgetData()
            },
            // 分页, 当前页
            pageCurrentChange (val) {
                this.page = val
                this.awaitgetData()
            },
            previewH5Fn(row){
               window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?specId='+row.specId);
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
            reset() {
                this.dataForm = {
                    goodsName: '',
                    goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
                    gcId: '',
                }
                this.gcIds = [];//分类ID
                this.awaitgetData();
            },
            // 刷新列表数据
            flushData() {
                this.awaitgetData();
            },
            // 编辑秒杀商品弹框
            seckillGoodsSkuDetail(row) {
                this.editSkuVisible = true;
                this.$nextTick(() => {
                    let obj = {
                        activityId: this.activityId,
                        goodsId: row.id,
                        goodsName: row.goodsName
                    };
                    this.$refs.editSkuCompent.init(obj);
                });
            },
            Onmouseenter(row, index) {
                let obj = row
                obj.btnText = '移除'
                this.$set(this.dataList, index, obj);
            },
            Onmouseleave(row, index) {
                let obj = row
                obj.btnText = '已添加'
                this.$set(this.dataList, index, obj);
            },
            deleteGoods(row, index) {
                this.$nextTick(() => {
                    if (row.btnText == '移除') {
                        this.$confirm('您确认移除该商品吗?', '提示', {
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            type: 'warning'
                        }).then((e) => {
                            var obj = {
                                goodsIds: [row.id],
                                activityId: row.activityId
                            }
                            deletSeckillGoods(obj).then((res => {
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: 'success',
                                        duration: 1500,
                                    })
                                    this.awaitgetData() // 刷新列表数据
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: 'error',
                                        duration: 1500,
                                    })
                                }
                            }))
                        }).catch((e) => {
                            this.$message({
                                type: 'info',
                                message: '已取消操作'
                            });
                        });
                    }
                })
            },
            //批量隐藏/隐藏

            changeCompent(type) {
                this.activityId
                let obj = {
                    id: this.activityId,
                    activityState: this.activityState,
                };
                this.$emit('changeCompent', type, obj);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .goodsname {
        span {
            color: red;
            border: 1px solid red;
            padding: 1px 3px;
        }
    }

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
            display: contents;
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
      .goodsNameContent {
          width: auto;
          padding: 1px 3px;
          text-align: left;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          cursor: pointer;
          font-weight: 600;
        }
</style>
