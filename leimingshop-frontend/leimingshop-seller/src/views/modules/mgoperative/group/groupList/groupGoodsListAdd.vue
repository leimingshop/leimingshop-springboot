<template>
    <!--优惠券列表-->
    <div class="couponList">
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>

        <el-form
                :inline="true"
                v-show="showTable == 0"
                ref="dataForm"
                :model="dataForm">
            <el-form-item label="输入搜索：">
                <el-input v-model="dataForm.goodsName" placeholder="商品名称/商品货号" clearable></el-input>
            </el-form-item>
            <el-form-item label="商品状态：" prop="activityState">
                <el-select v-model="dataForm.goodsShow" placeholder="商品状态">
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
        <div class="line"></div>
        <!--表格-->
        <el-radio-group v-model="activeName" @change="handleClick" style="margin-top: 5px;margin-bottom: 5px">
            <el-radio-button label="added">已添加</el-radio-button>
            <el-radio-button label="notadd">未添加</el-radio-button>
        </el-radio-group>
        <!--未添加商品表格-->
        <div class="table-box" v-show="showTable == 0">
            <add-table ref="firstSkuList" @showListFn="showListFn"></add-table>
        </div>
        <!--已添加商品表格-->
        <div class="table-box" v-show="showTable == 1">
            <added-table ref="secSkuList" @showListFn="showListFn"></added-table>
        </div>


        <!-- 图片弹框 -->
        <el-dialog
                title="编辑活动商品信息"
                :visible.sync="photoVisible"
                :close-on-click-modal="false"
                :show-close="false"
                width="80%"

        >
            <el-form class="model-box" :model="modelForm" :rules="dataRuleOther" ref="dataRuleOther"
                     label-width="120px">
                <div class="fullTitle ">
                    基础设置
                </div>
                <el-form-item label="商品名称：">
                    <div>{{goodsRow.goodsName}}</div>
                </el-form-item>
                <div>
                    <el-table
                            width="90%"
                            :data="modelForm.skuList"
                            v-loading="dataListLoading"
                            style="width: 100%;maigin-top:20px;">
                        <el-table-column prop="specAttrName" label="规格" align="center" min-width="120">
                            <template slot-scope="scope">
                                <span>{{scope.row.specAttrName == null ? '默认规格' :scope.row.specAttrName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="specStorage" label="商品库存" align="center"
                                         min-width="60"></el-table-column>
                        <el-table-column prop="activityStorage" label="活动库存" align="center" min-width="160">
                            <template slot-scope="scope">
                                <el-input-number v-model="scope.row.activityStorage" controls-position="right" :min="0"
                                                 :step="1"></el-input-number>
                            </template>
                        </el-table-column>
                        <el-table-column prop="specSellPrice" label="销售价" align="center"
                                         min-width="60"></el-table-column>
                        <el-table-column prop="activityPrice" label="拼团价（元）" align="center" min-width="160">
                            <template slot-scope="scope">
                                <el-input-number v-model="scope.row.activityPrice" controls-position="right" :min="0.01"
                                                 :max="99999999.99"
                                                 :step="0.01"></el-input-number>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div>

                    </div>
                </div>
                <div class="hrTips-t">若活动库存为商品总库存，会导致活动开始前无法原价购买。活动库存为0表示该商品不参加活动</div>
                <el-form-item label="参团次数限制：" prop="joinLimit">
                    每个用户可参团次数最多
                    <el-input v-model="modelForm.joinLimit">
                        <span slot="suffix">次</span>
                    </el-input>
                </el-form-item>
                <div class="hrTips">0表示不限</div>
                <el-form-item label="单次购买数量：" prop="onceBuyLimit">
                    每个用户单次开团或参团时，购买件数最多
                    <el-input v-model="modelForm.onceBuyLimit">
                        <span slot="suffix">件</span>
                    </el-input>
                </el-form-item>
                <div class="hrTips">0表示不限</div>

                <el-form-item label="排序：" prop="sort">
                    <el-input-number v-model="modelForm.sort" controls-position="right" :min="0"></el-input-number>
                </el-form-item>
                <div class="hrTips">0-255，前台活动，后台商品列表均以此排序，数字越小排序越靠前</div>

                <el-form-item label="成团人数：" prop="regimentNum">
                    <el-input v-model="modelForm.regimentNum">
                        <span slot="suffix">人</span>
                    </el-input>
                </el-form-item>
                <div class="hrTips">拼团成功需要参与购买的人数，需大于1人</div>

            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center;">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="startCheck" :loading="saveLoading">{{saveLoading?"提交中...":"确 认"}}
                </el-button>
            </div>
        </el-dialog>
        <!-- 图片弹框 -->


    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    import {getGroupGoodsPage, getGroupGoodsList} from "@/api/url";
    import {deletetGroupGoods, allTreeGoodsclass} from "@/api/api";
    import {getGoodscalss, getGroupListSku, getGroupGoodsAdd} from "@/api/api.js";
    import AddTable from "./addTable";
    import AddedTable from "./addedTable";

    export default {
        mixins: [mixinViewModule],
        data() {
            var geZero = (rule, value, callback) => {
                if (/[^\d]/.test(value) || parseInt(value) <= 1) {
                    callback(new Error('成团人数必须大于1'))
                } else if (/[^\d]/.test(value) || parseInt(value) > 999) {
                    callback(new Error('此选项不能大于999'))
                }
                callback()
            };
            var maxNum = (rule, value, callback) => {
                if (/[^\d]/.test(value) || parseInt(value) > 999) {
                    callback(new Error('此选项不能大于999'))
                }
                callback()
            };
            return {
                row: {},
                breaddata: ["营销系统", "拼团管理", "拼团活动商品"],
                // 查询表单
                dataForm: {
                    goodsName: '',
                    goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
                    gcId: '',
                },
                gcIds: [],
                dataList: [],
                dataListLoading: false,

                isShowButton: false,
                saveLoading: false,
                btnText: '已添加',
                goodscalssOption: [],
                props: {
                    value: "id",
                    label: "gcName"
                },
                goodsRow: {},

                activeName: "notadd",
                showTable: 0,
                modelForm: {
                    activityId: '',
                    goodsId: '',
                    sort: '',
                    skuList: [],
                    regimentNum: '',
                    joinLimit: '',
                    onceBuyLimit: '',
                },
                photoVisible: false,
                dataRuleOther: {
                    // activityId: [
                    // 	{required: true, message: '必填项不能为空', trigger: 'blur'},
                    // ],
                    // goodsId: [
                    // 	{required: true, message: '必填项不能为空', trigger: 'blur'},
                    // ],
                    sort: [
                        {required: false, message: '必填项不能为空', trigger: 'blur'},
                    ],
                    regimentNum: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        {validator: geZero, trigger: 'blur'}
                    ],
                    joinLimit: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        {validator: maxNum, trigger: 'blur'}
                    ],
                    onceBuyLimit: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        {validator: maxNum, trigger: 'blur'}
                    ],
                }
            }
        },
        components: {
            AddTable,
            AddedTable,
            Bread,
        },
        created() {
            this.getallTreeGoodsclassFn();
        },
        methods: {

            init(row) {
                this.row = row;  //活动item
                this.activityId = row.id;
                this.breaddata[2] = row.groupName
                // this.awaitgetData();
                this.$nextTick(() => {
                    this.handleClick();
                })
            },
            showListFn() {
                this.$emit("showListFn");
            },
            handleClick(tab) {
                this.page = 1;
                this.limit = 10;
                var tab = this.activeName;
                if (tab == "added") {
                    this.showTable = 1;
                    this.$nextTick(() => {
                        this.$refs.secSkuList.init(this.dataForm, this.row);
                    })
                } else if (tab == "notadd") {
                    this.showTable = 0;
                    this.$nextTick(() => {
                        this.$refs.firstSkuList.init(this.dataForm, this.row);
                    })

                }

            },
            isSelectable(row, index) {
                if (row.activityId == this.activityId) {
                    return true
                } else {
                    return false
                }
            },
            dataListSelectionChangeHandle(val) {
                let arr = []
                val.forEach((v, i) => {
                    if (v.activityId != null && v.activityId == this.activityId) {
                        arr.push(v)
                    }
                })
                this.dataListSelections = arr
            },
            handleClose() {
                this.photoVisible = false
                this.$refs['dataRuleOther'].resetFields() //校验隐藏
            },
            startCheck() {
                this.$refs['dataRuleOther'].validate(valid => {
                    if (valid) {
                        let isCheck = this.modelForm.skuList.some(v => {
                            return v.activityStorage != 0 && v.activityStorage != null
                        })
                        if (isCheck) {
                            this.saveLoading = true;
                            getGroupGoodsAdd(this.modelForm).then(res => {
                                this.saveLoading = false;
                                if (res.code == 200) {
                                    this.$message({
                                        message: res.msg,
                                        type: 'success'
                                    })
                                    this.photoVisible = false
                                    this.reset()
                                    this.$refs['dataRuleOther'].resetFields() //校验隐藏
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: 'warning'
                                    })
                                }
                            })
                        } else {
                            this.$message({
                                type: 'error',
                                message: '必须有一个规格的活动库存不为0！'
                            });
                        }

                    }
                })
            },
            getallTreeGoodsclassFn() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOption = res.data
                    }
                })
            },
            finishCange() {
                let len = this.gcIds.length;
                if (len != -1) {
                    this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
                }
            },

            // 查询表格数据
            getData() {
                this.page = 1;
                if (this.showTable == 0) {
                    this.$refs.firstSkuList.init(this.dataForm, this.row);
                } else {
                    this.$refs.secSkuList.init(this.dataForm, this.row);
                }
            },

            // 重置
            reset() {
                this.dataForm = {
                    goodsName: '',
                    goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
                    gcId: '',
                }
                this.gcIds = [];//分类ID
                this.page = 1;
                if (this.showTable == 0) {
                    this.$refs.firstSkuList.init(this.dataForm, this.row);
                } else {
                    this.$refs.secSkuList.init(this.dataForm, this.row);
                }
            },
            changeCompent(type, row) {
                this.$emit('changeCompent', type, row)
            },
            changePage() {
                this.$emit('changePage')
            },
        }
    }
</script>

<style lang="scss" scoped>
    .btn-box {
        margin-top: 30px;
        text-align: center;
    }

    .table-box {
        .el-button {
            margin-left: 0;
        }
    }


    .el-table__body-wrapper {
        max-height: 300px;
    }

    .model-box {
        padding: 0 30px;

        .el-input-number {
            width: 140px;
        }

        .el-input {
            width: 100px;
        }

        /deep/
        .el-form-item__content {
            display: flex;
        }

        /deep/
        .el-form-item__error {
            position: relative;
            line-height: 42px;
            padding-left: 12px;
        }
    }

    .fullTitle {
        font-weight: 700;
        height: 40px;
        line-height: 40px;
    }

    .hrTips-t {
        color: #999999;
        margin: 10px 0;
    }

    .hrTips {
        color: #999999;
        margin-left: 120px;
        margin-bottom: 10px;
    }


    .couponList {
        /deep/ .el-input__icon {
            height: unset !important;
        }

        .line {
            height: 10px;
            background: #ECEDF1;
            margin: 0 -20px;
        }

        .addButton {
            margin: 10px 0px 0px 0px;
        }

        .el-table--border {
            margin-top: 20px;
        }
    }


    .btn-span {
        display: inline-block;
        width: 60px;
    }

    .goodsPropsWrap {
        margin: auto;
        height: 80px;
        width: 410px;
        display: flex;
        justify-content: center;
        /*justify-content: space-around;*/
        align-items: center;

        .goodsImg {
            width: 70px;
            height: 70px;
            flex-shrink: 0;

            img {
                width: 100%;
                height: 100%;
            }
        }

        .goodsProps {
            /*width: 240px;*/
            height: 70px;
            display: flex;
            align-items: center;
            /*flex-direction: column;*/
            /*align-items: flex-start;*/

            .goodsNameTitle {
                display: inline-block;
                width: 30px;
            }

            .goodsBrandName {
                text-align: left;
            }

            .goodsName {
                display: flex;
                width: 340px;
                box-sizing: border-box;
                padding-left: 12px;

                .active-name {
                    width: 60px;
                    color: red;
                    margin-right: 6px;
                    border: 1px solid red;
                    padding: 1px 3px;
                    flex-shrink: 0;
                }

                .active-name-box {
                    width: 60px;
                    margin: 0 4px;
                    padding: 1px 3px;
                    flex-shrink: 0;
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

            }
        }
    }
</style>


