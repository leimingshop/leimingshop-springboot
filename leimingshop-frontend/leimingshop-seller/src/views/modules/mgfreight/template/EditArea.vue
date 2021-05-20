<template>
    <el-dialog
            width="89%"
            :visible.sync="editAreaVisible"
            title="编辑可配送区域"
            v-loading="dataLoading"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :append-to-body="true">

        <div v-if="regionAndAreaData" v-for="region in regionAndAreaData" style="display: flex;">
            <div style="display: flex;">
                <!--<span class="choice"><el-checkbox :indeterminate="region.isIndeterminate" v-model="region.checked"-->
                                                  <!--:disabled="region.disabled" @change="handleCheckAllChange(region)">{{region.areaName}}</el-checkbox></span>-->

                <span class="choice">{{region.areaName}}</span>
                <!--<span class="choice">{{region.areaName}}</span>-->
                <span class="choice" style="width: 200px;max-height: 400px;display:inline-block;overflow: auto;"
                      v-if="region" v-for="province in region.children">
                    <el-tree
                            :data="[province]"
                            show-checkbox
                            node-key="id"
                            :props="{ label: 'areaName', children: 'children' }"
                            :default-expanded-keys="[1]"
                            :default-checked-keys="areaIdsArr"
                            ref="areaDataRef"
                    >
                </el-tree>
                </span>
            </div>

        </div>

        <div slot="footer" style="text-align: center;">
            <el-button @click="cancel()">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="saveData()">{{ $t('confirm') }}</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {clearLoginInfo} from '@/utils'
    import {seckillGoodsSkuDetail, saveSeckillGoods} from '@/api/api'

    export default {
        data() {
            return {
                editAreaVisible: false,
                index: '',
                areaIdsArr: [], // 该规则地区
                regionAndAreaData: [],  // 所有地区
                regionData: [],  // 其他规则已选择大区
                provinceData: [],  // 其他规则已选择省
                cityData: [],  // 其他规则已选择市
                areaData: [],  // 其他规则已选择区县
                dataLoading: false,
                row: '',
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
            init(row, rowIndex, regionAndAreaData, regionData, provinceData, cityData, areaData) {
                this.editAreaVisible = true;
                this.dataLoading = true;
                if (row) {
                    this.areaIdsArr = row.areaIdsArr;
                }
                this.index = rowIndex;
                this.regionAndAreaData = regionAndAreaData;
                this.regionData = regionData;
                this.provinceData = provinceData;
                this.cityData = cityData;
                this.areaData = areaData;
                this.dataLoading = false;

                // 大区半选及失效状态
                this.$nextTick(() => {
                    this.regionAndAreaData.forEach((region, index) => {
                        let checkedKeys = [];
                        for (let i = 0; i <= this.$refs.areaDataRef.length - 1; i++) {
                            if (this.$refs.areaDataRef[i].data[0].areaParentId == region.id) {
                                checkedKeys = checkedKeys.concat(this.$refs.areaDataRef[i].getCheckedKeys());
                                if (this.$refs.areaDataRef[i].getCheckedKeys().indexOf(this.$refs.areaDataRef[i].data[0].id) == -1) {
                                    // 未勾选省，取消全选
                                    region.checked = "";
                                }
                            }
                        }
                        if (region.checked != true && checkedKeys.length > 0) {
                            region.isIndeterminate = true;
                        }
                        // 设置大区复选框失效状态
                        for (let i = 0; i <= region.children.length - 1; i++) {
                            if (region.children[i].disabled != true) {
                                region.disabled = false;
                                break;
                            }
                        }
                    })
                })
            },
            // 大区全选/取消去选
            handleCheckAllChange(region) {

            },
            // 表单提交
            saveData() {
                // 获取所有勾选地区 -- 匹配最上级全选地区 -- 保存所选地区
                let checkedNodes = [];
                let halfCheckedNodes = [];
                for (let i = 0; i <= this.$refs.areaDataRef.length - 1; i++) {
                    // 获取所有勾选地区
                    checkedNodes = checkedNodes.concat(this.$refs.areaDataRef[i].getCheckedNodes());
                    // 获取所有勾选地区
                    halfCheckedNodes = halfCheckedNodes.concat(this.$refs.areaDataRef[i].getCheckedNodes(false, true));
                }

                if (!checkedNodes || checkedNodes.length == 0) {
                    this.$message({
                        message: "所选地区不能为空",
                        type: "error",
                        duration: 1500
                    })
                    return;
                }

                // 按层级分组
                let groups = this.ngOnInit(checkedNodes);
                let list1 = groups[1] || [];
                let list2 = groups[2] || [];
                let list3 = groups[3] || [];

                let parentAreaList = [];
                halfCheckedNodes.forEach(item => {
                    if (item.areaDeep == 1) {
                        let obj = {
                            id: item.id,
                            areaName: item.areaName,
                            isAll: false,
                            children: []
                        };
                        parentAreaList.push(obj);
                    } else if (item.areaDeep == 2) {
                        let obj = {
                            id: item.id,
                            areaName: item.areaName,
                            isAll: false,
                            children: []
                        };
                        parentAreaList[parentAreaList.length - 1].children.push(obj);
                    }
                });

                this.areaIdsArr = [];
                let id1list = [];
                let id2list = [];
                let id3list = [];

                list1.forEach((item, index) => {
                    this.areaIdsArr.push(item.id);
                    id1list.push(item.id);

                    for (let i = 0; i <= parentAreaList.length - 1; i++) {
                        if (parentAreaList[i].id == item.id) {
                            parentAreaList[i].isAll = true;
                            break;
                        }
                    }
                });

                list2.forEach((item, index) => {
                    id2list.push(item.id);
                    if (id1list.indexOf(item.areaParentId) == -1) {
                        this.areaIdsArr.push(item.id);
                    }
                });

                list3.forEach((item, index) => {
                    id3list.push(item.id);
                    if (id2list.indexOf(item.areaParentId) == -1) {
                        this.areaIdsArr.push(item.id);
                    }
                });



                let areaDescription = "";
                parentAreaList.forEach((item, index) => {
                    if (areaDescription == "") {
                        areaDescription = areaDescription;
                    } else {
                        areaDescription = areaDescription + "、";
                    }

                    if (item.isAll) {
                        areaDescription = areaDescription + item.areaName + " 【全部】";
                    } else {
                        areaDescription = areaDescription + item.areaName + " 【";
                        item.children.forEach((item1, index1) => {
                            if (index1 == 0) {
                                areaDescription = areaDescription + item1.areaName;
                            } else {
                                areaDescription = areaDescription + "、" + item1.areaName;
                            }
                        })
                        areaDescription = areaDescription + "】";
                    }
                })


                // 父页面对应的列传值
                this.editRow(this.index, this.areaIdsArr, areaDescription);
                this.editAreaVisible = false;


            },
            ngOnInit(checkedNodes) {
                const sorted = this.groupBy(checkedNodes, function (item) {
                    return item.areaDeep;
                });
                return sorted;

            },
            groupBy(array, f) {
                const groups = {};
                array.forEach(function (o) { //注意这里必须是forEach 大写
                    const group = JSON.stringify(f(o));
                    groups[group] = groups[group] || [];
                    groups[group].push(o);
                });
                return groups;
            },
            // 取消sku选择
            cancel() {
                this.editAreaVisible = false;
            },
            // 修改对应列数据
            editRow(index, areaIds, areaDescription) {
                this.$emit('editRow', index, areaIds, areaDescription);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addOrEdit {
        .hrTips {
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
        .displayName {
            display: inline-block;
        }
    }

    .choice {
        display: inline-block;
        font-size: 14px;
        width: 120px;
        margin-top: 20px;
        vertical-align: top;
        /*padding-right: 50px;*/
    }
</style>
