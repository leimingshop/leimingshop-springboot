<template>
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form :inline="true" :model="dataForm" class="addOrEdit" :rules="dataRule" ref="dataForm"
                 v-loading="templateLoading">
            <el-form-item label="运费模板名称：" prop="templateName" class="name">
                <el-input v-model="dataForm.templateName" placeholder="请输入模板名称" maxlength="20"
                          show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="计价方式：" prop="templateType">
                <el-radio-group v-model="dataForm.templateType">
                    <el-radio class="label" :label="0">按件计件</el-radio>
                    <el-radio class="label" :label="1">按重计件</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item   prop="freightTemplateRuleShowList"  >
                <!--<el-input v-model="dataForm.freightTemplateRuleList" placeholder="请输入模板名称" maxlength="20" show-word-limit></el-input>-->
                <span class="star"  slot="label">*</span>
                <span slot="label">配送区域及运费：</span>
                <el-table
                        width="80%"
                        :data="freightTemplateRuleShowList"
                        border=""
                        style="width: 1050px"
                >
                    <el-table-column prop="areaDescription" label="可配送区域" align="left"
                                     min-width="500"  >
                        <span slot-scope="scope" >

                                <span v-if="scope.row.areaIdsArr && scope.row.areaIdsArr[0] == 0" >
                                    <span style="display: flex;justify-content: flex-start"><el-checkbox
                                            v-model="allArea">{{scope.row.areaDescription}}</el-checkbox><font
                                            style="color: #999999">（若未勾选，则仅下列所选区域可配送）</font></span>
                                </span>
                                <span v-else
                                      style="">
                                   <div style="overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;"> {{scope.row.areaDescription}}</div>
                                    <div style="min-width: 93px;display: flex;justify-content: flex-end;line-height: 15px">
                                        <el-button class="btn" type="text" style="line-height: 15px"
                                                   @click="choiceArea(scope.row, scope.$index)" v-loading="ruleLoading">编辑</el-button>
                                        <el-button class="btn" type="text" style="line-height: 15px"
                                                   @click="deleteTemplateRule(scope.$index)">删除</el-button>
                                    </div>
                                </span>
                        </span>
                    </el-table-column>
                    <el-table-column prop="firstFee" :label="dataForm.templateType==0?'首件（个）' : '首重（kg）'" align="left"
                                     width="120">
                        <template slot-scope="scope">
                            <el-input type="number"  onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )" class="input-number" v-model="scope.row.firstFee" min="1"
                                      max="999999" @blur="feeCheck(scope.row, scope.$index)" placeholder=""></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column prop="firstAmount" label="运费（元）" align="left"
                                     width="120">
                        <template slot-scope="scope">
                            <el-input type="number" class="input-number" v-model="scope.row.firstAmount" min="0.01"
                                      max="999999.99" @blur="amountCheck(scope.row, scope.$index)"
                                      placeholder=""></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column prop="additionalFee"  :label="dataForm.templateType==0?'续件（个）' : '续重（kg）'" align="left"
                                     width="120">
                        <template slot-scope="scope">
                                <el-input type="number"   onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )" class="input-number" v-model="scope.row.additionalFee" min="1"
                                      max="999999" @blur="feeCheck(scope.row, scope.$index)" placeholder=""></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column prop="additionalFee" label="续费（元）" align="left"
                                     width="120">
                        <template slot-scope="scope">
                            <el-input type="number" class="input-number" v-model="scope.row.additionalAmount" min="0.01"
                                          max="999999.99" @blur="amountCheck(scope.row, scope.$index)"
                                          placeholder=""></el-input>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button class="btn" type="primary" @click="choiceArea()" v-loading="ruleLoading">添加可配送区域</el-button>
            </el-form-item>

            <el-form-item label=" " prop="defaultFlag">
                <el-checkbox v-model="dataForm.defaultFlag == 1" @change="updateDefaultFlag()">设置为默认模板</el-checkbox>
            </el-form-item>

        </el-form>
        <div style="text-align: center;">
            <el-button class="btn" type="primary" plain @click="changePage()">取消</el-button>
            <el-button class="btn" type="primary" @click="dataFormSubmit('dataForm')" :loading="saveLoading">
                {{saveLoading?"提交中...":"保存"}}
            </el-button>
        </div>

        <!--地区选择弹框-->
        <editArea v-if="editAreaVisible" ref="editAreaCompent" @editRow="editRow"></editArea>

    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import editArea from "./EditArea.vue";
    import {
        freightTemplateDetail,
        redionAndAreaTree,
        saveFreightTemplate,
        updateFreightTemplate,
    } from '@/api/api'

    export default {
        name: "model-add-or-edit",
        data() {

            return {
                breaddata: [],

                // 新增秒杀表单
                dataForm: {
                    id: '',
                    templateName: '', // 模板名称
                    templateType: 0,   // 计价方式 默认按件
                    freightTemplateRuleList: [],  // 模板规则集合
                    defaultFlag: 0,    // 是否默认 默认不是默认运费模板
                },
                freightTemplateRuleShowList: [],
                editAreaVisible: false,
                regionAndAreaData: [], // 地区数据
                regionData: [], // 已选择大区
                provinceData: [], // 已选择省
                cityData: [], // 已选择市
                areaData: [], // 已选择区县
                allArea: false,
                row: '',
                page: 1,
                limit: 10,
                type: '', // 1新增 2编辑 3复制
                total: 0,
                // 数据加载loadng
                templateLoading: false,
                ruleLoading: true,
                // 提交
                saveLoading: false,

                // 校验规则

                dataRule: {
                    templateName: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'}
                    ],
                    templateType: [
                        {required: true, message: '计价方式不能为空', trigger: 'blur'}
                    ]
                },

            }
        },
        components: {
            Bread,
            editArea,
        },
        created() {
            redionAndAreaTree().then(res => {
                // this.ruleLoading = true;
                if (res.code == 200) {
                    this.ruleLoading = false;
                    this.regionAndAreaData = res.data;
                } else {
                    this.$message({
                        message: res.msg,
                        type: "error",
                        duration: 1500
                    })
                    // 跳回上一页 刷新列表
                    this.$emit('changePage')
                }
            });
        },
        methods: {

            init(row, type) {
                this.type = type;
                // 获取地区
                if (this.type == 2) {
                    this.breaddata = ["物流管理", "运费模板列表", "运费模板编辑"];
                    this.row = row;
                    // 回显活动
                    this.backScan(row);
                } else if (this.type == 1 || this.type == 4) {
                    if (type == 1) {
                        this.breaddata = ["物流管理", "运费模板列表", "运费模板新增"];
                    } else {
                        this.breaddata = ["商品", "商品编辑", "新增运费模板"];
                    }
                    this.freightTemplateRuleShowList = [
                        {
                            areaDescription: '所有地区',
                            areaIdsArr: [0],
                            firstFee: '',
                            firstAmount: '',
                            additionalFee: '',
                            additionalAmount: '',
                        }
                    ];
                    this.allArea = true;
                } else if (this.type == 3) {
                    this.breaddata = ["物流管理", "运费模板列表", "运费模板新增"];
                    this.row = row;
                    // 回显活动
                    this.backScan(row);
                }
            },
            // 编辑回显
            backScan(row) {
                var obj = {
                    id: row.id
                }
                this.templateLoading = true;
                freightTemplateDetail(obj).then((res => {
                    if (res.code == 200) {
                        this.templateLoading = false;
                        if (this.type == 3) {
                            this.dataForm = res.data;
                            this.dataForm.templateName = '';
                            this.freightTemplateRuleShowList = res.data.freightTemplateRuleList;
                            this.row = '';
                        } else if (this.type == 2) {
                            this.dataForm = res.data;
                            this.freightTemplateRuleShowList = res.data.freightTemplateRuleList;
                        }
                        if (this.freightTemplateRuleShowList) {
                            this.freightTemplateRuleShowList.forEach((item, index) => {
                                if (item.areaIdsArr[0] == 0) {
                                    this.allArea = true;
                                }
                            });
                        }
                        if (this.allArea == false) {
                            this.freightTemplateRuleShowList = [
                                {
                                    areaDescription: '所有地区',
                                    areaIdsArr: [0],
                                    firstFee: '',
                                    firstAmount: '',
                                    additionalFee: '',
                                    additionalAmount: '',
                                }
                            ].concat(this.freightTemplateRuleShowList);
                        }
                    } else {
                        this.$message({
                            message: res.msg,
                            type: "error",
                            duration: 1500
                        })
                        // 跳回上一页 刷新列表
                        this.$emit('changePage')
                    }
                }))
            },
            // 修改是否默认
            updateDefaultFlag() {
                if (this.dataForm.defaultFlag == 1) {
                    this.dataForm.defaultFlag = 0;
                } else {
                    this.dataForm.defaultFlag = 1;
                }
            },
            // 表单提交
            dataFormSubmit(dataForm) {
                var that = this
                this.$refs[dataForm].validate((valid) => {
                    if (valid) {
                        this.dataForm.freightTemplateRuleList = that.freightTemplateRuleShowList;

                        if (that.allArea == false) {
                            if (that.dataForm.freightTemplateRuleList.length == 1 &&that.dataForm.freightTemplateRuleList[0].areaDescription=='所有地区') {
                                 this.$message({
                                    message: "配送区域不能为空",
                                    type: "error",
                                    duration: 1500
                                })
                                return false
                            }
                            that.dataForm.freightTemplateRuleList.splice(0,1)
                        }
                        that.saveLoading = true;
                        var fn = this.type == 2 ? updateFreightTemplate : saveFreightTemplate;
                        fn(this.dataForm).then((res => {
                            that.saveLoading = false;
                            let status = null;
                            if (res.code == "200") {
                                status = "success";
                                that.changePage()
                            } else {
                                status = "error";
                            }
                            this.$message({
                                message: res.msg,
                                type: status,
                                duration: 1500
                            })
                        }))
                    } else {
                        return false;
                    }
                })
            },

            // 删除运费模板规则
            deleteTemplateRule(index) {
                this.freightTemplateRuleShowList.splice(index, 1);
            },
            feeCheck(row, index) {
                if (index != 0 || (index == 0 && this.allArea == true)) {
                    if (row.firstFee < 1) {
                        row.firstFee = 1;
                    }
                    if (row.firstFee > 999999) {
                        row.firstFee = 999999;
                    }
                    if (row.additionalFee < 1) {
                        row.additionalFee = 1;
                    }
                    if (row.additionalFee > 999999) {
                        row.additionalFee = 999999;
                    }

                }
            },
            amountCheck(row, index) {
                if (index != 0 || (index == 0 && this.allArea == true)) {
                    if (row.firstAmount < 0.01) {
                        row.firstAmount = 0.01;
                    }
                    if (row.firstAmount > 999999.99) {
                        row.firstAmount = 999999.99
                    }
                    if (row.additionalAmount < 0.01) {
                        row.additionalAmount = 0.01;
                    }
                    if (row.additionalAmount > 999999.99) {
                        row.additionalAmount = 999999.99;
                    }
                    if(row.firstAmount>0.01) {
                        row.firstAmount = row.firstAmount.toString().replace(/^\D*([0-9]\d*\.?\d{0,2})?.*$/, '$1')
                    }
                    if (row.additionalAmount>0.01) {
                        row.additionalAmount = row.additionalAmount.toString().replace(/^\D*([0-9]\d*\.?\d{0,2})?.*$/, '$1')
                    }


                }
            },
            // 选择地区
            choiceArea(row, rowIndex) {
                this.editAreaVisible = true;
                let displayArea = JSON.parse(JSON.stringify(this.regionAndAreaData));// copy参数

                // 编辑运费规则 遍历所有规则，确定已被选择的地区
                if (this.freightTemplateRuleShowList && this.freightTemplateRuleShowList.length > 0) {

                    // 获取所有已选择地区
                    var allChoicedArea = [];
                    this.freightTemplateRuleShowList.forEach((item, index) => {
                        if (index != 0) {
                            if (rowIndex) {
                                // 编辑
                                if (index != rowIndex) {
                                    allChoicedArea = allChoicedArea.concat(item.areaIdsArr);
                                }
                            } else {
                                // 添加
                                allChoicedArea = allChoicedArea.concat(item.areaIdsArr);
                            }
                        }
                    });

                    if (allChoicedArea.length > 0) {
                        // 遍历是所有地区，确定各级地区被选择数据
                        console.log(allChoicedArea)
                        displayArea.forEach((region, index) => {
                            region.checked = true;
                            region.disabled = true;
                            if (region.children && region.children.length > 0) {
                                region.children.forEach((province, index) => {
                                    if (allChoicedArea.indexOf(province.id) != -1) {
                                        this.provinceData.push(province.id);
                                        province.disabled = true;
                                        if (province.children && province.children.length > 0) {
                                            province.children.forEach((city, index) => {
                                                city.disabled = true;
                                                if (city.children && city.children.length > 0) {
                                                    city.children.forEach((area, index) => {
                                                        area.disabled = true;
                                                    });
                                                }
                                            });
                                        }
                                    } else {
                                        if (province.children && province.children.length > 0) {
                                            province.children.forEach((city, index) => {
                                                if (allChoicedArea.indexOf(city.id) != -1) {
                                                    this.cityData.push(city.id);
                                                    city.disabled = true;
                                                    if (city.children && city.children.length > 0) {
                                                        city.children.forEach((area, index) => {
                                                            area.disabled = true;
                                                        });
                                                    }
                                                } else {
                                                    if (city.children && city.children.length > 0) {
                                                        city.children.forEach((area, index) => {
                                                            if (allChoicedArea.indexOf(area.id) != -1) {
                                                                this.areaData.push(area.id);
                                                                area.disabled = true;
                                                            }
                                                        })
                                                    }
                                                }
                                            })
                                        }
                                    }
                                })
                            }
                        })
                        this.regionAndAreaData = [].concat(this.regionAndAreaData)
                    }
                }

                this.$nextTick(() => {
                    this.$refs.editAreaCompent.init(row, rowIndex, displayArea, this.regionData, this.provinceData, this.cityData, this.areaData);
                });
            },
            // 返回上一页
            changePage() {
                this.$emit('changePage')
            },
            // 修改对应列数据
            editRow(index, areaIds, areaDescription) {
                if (index) {
                    this.freightTemplateRuleShowList[index].areaIds = JSON.stringify(areaIds);
                    this.freightTemplateRuleShowList[index].areaIdsArr = areaIds;
                    this.freightTemplateRuleShowList[index].areaDescription = areaDescription;
                } else {
                    let obj = {
                        areaIds: JSON.stringify(areaIds),
                        areaIdsArr: areaIds,
                        areaDescription: areaDescription,
                    }
                    this.freightTemplateRuleShowList.push(obj)
                }

            }
        }
    }
</script>

<style lang="scss" scoped>
    // 新增或编辑表单

    .addOrEdit {
        margin-left: 1%;
        .input-number {
            width: 100px !important;
        }
        .hrTips {
            color: #999999;
        }
        .fullTitle {
            font-weight: 700;
            font-size: 25px;
        }
        .title-1 {
            margin-left: -60px;
        }
        .title-2 {
            margin-left: 150px;
        }
        .title-3 {
            margin-left: 60px;
        }
        .el-form-item {
            display: flex;
        }
        /deep/ .el-form-item__label {
            width: 150px !important;
            text-align: right;
        }
        /deep/ .el-form-item__error {
            position: relative !important;
            display: inline-block;
            margin-left: 10px;
        }
        /deep/ .el-input__icon {
            height: unset !important;
        }
        /deep/ .el-input-number {
            line-height: 34px !important;
            width: unset !important;
        }
        .name .el-input {
            width: 400px !important;
        }
        .specSize {
            .el-input {
                width: 200px !important;
            }
        }
        .fullTis {
            color: #999999;
            margin-left: 5px;
            margin-left: 170px
        }
        .addBtn {
            margin-top: 15px;
            margin-left: 170px;
        }
        .delBtn {
            margin-left: 20px;
            margin-top: 15px;
        }
        .star{
            color: #f56c6c;
            font-size: 14px;
            margin-right: 4px;
        }
    }
</style>
