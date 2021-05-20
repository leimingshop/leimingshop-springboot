<template>
    <el-dialog
            width="50%"
            :visible.sync="editVisible"
            title="确认删除"
            v-loaging="dataLoading"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            destroy-on-close="true"
            :append-to-body="true">

        <div>
            <div style="margin-left: 40px;margin-top: 20px;">
                该模板有{{row.goodsNum}}个商品在使用，如需删除请先更换这些商品的运费模板：
            </div>
            <el-select
                    style="margin-left: 80px;margin-top: 20px;"
                    v-model="newFreightTemplateId"
                    @change="chooseFreightTemplate"
                    placeholder="请选择">
                <el-option
                        v-for="item in freightTemplateListoption"
                        :key="item.id"
                        :label="item.templateName"
                        :value="item.id">
                </el-option>
            </el-select>
            <!-- &nbsp;<el-button type="text" size="small">新增运费模板</el-button> -->
            <div style="margin-left: 80px;margin-top: 20px;">
                <span style="color: #a7a2a2">商品重量未填写时，将默认按照首件、首重计算运费</span>
            </div>
        </div>

        <div slot="footer" style="    text-align: center;">
            <el-button @click="cancel()">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="deleteAndUpdate()">{{ $t('confirm') }}</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {clearLoginInfo} from '@/utils'
    import { storeFreightTemplateList, deleteAndUpdateFreightTemplate } from '@/api/api'

    export default {
        name : 'updateGoodsTemplate',
        data() {
            return {
                editVisible: false,
                row: '',
                newFreightTemplateId: '',
                freightTemplateListoption: [],
                dataLoading: false,
            }
        },
        created() {

        },
        computed: {

        },
        methods: {
            init(obj) {
                this.editVisible = true;
                this.row = obj;
                // 店铺运费模板列表
                this.storeFreightTemplateList();
            },
            // 店铺运费模板列表
            storeFreightTemplateList() {
                this.dataLoading = true;
                return storeFreightTemplateList().then((res)=>{
                    this.dataLoading = false;
                    if(res.code = '200'){
                        this.freightTemplateListoption = res.data;
                        this.freightTemplateListoption.forEach((item,index)=>{
                            if (item.id == this.row.id) {
                                this.freightTemplateListoption.splice(index, 1);
                                return;
                            }
                        })
                    }
                })
            },
            // 修改原运费模板并删除
            deleteAndUpdate() {
                if (!this.newFreightTemplateId) {
                    this.$message({
                        message: '请选择运费模板',
                        type: 'error',
                        duration: 1500,
                    })
                }
                let obj = {
                    oldFreightTemplateId: this.row.id,
                    newFreightTemplateId: this.newFreightTemplateId,
                }
                this.dataLoading = true;
                deleteAndUpdateFreightTemplate(obj).then(res => {
                    this.dataLoading = false;
                    if (res.code == 200) {
                        this.$emit("flushData")
                        this.editVisible = false;
                    } else {
                        this.$message({
                            type: "warning",
                            message: res.msg
                        });
                    }
                })
            },
            // 取消sku选择
            cancel() {
                // 刷新上一个页面
                this.newFreightTemplateId = '';
                // this.$emit("flushData")
                this.editVisible = false;
            },
            chooseFreightTemplate(val) {
                this.newFreightTemplateId = val;
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
        .displayName {
            display: inline-block;
        }
    }
</style>
