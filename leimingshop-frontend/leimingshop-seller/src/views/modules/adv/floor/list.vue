<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="楼层名称：">
                    <el-input
                        maxlength="10"
                        v-model="dataForm.floorName"
                        placeholder="楼层名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="是否显示：">
                    <el-select v-model="dataForm.isShow" placeholder="请选择">
                        <el-option
                            v-for="item in showFlagList"
                            :key="item.id"
                            :label="item.sgName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <div class="formControlArea">
                <div>
                    <el-form>
                        <el-form-item>
                            <el-button type="primary" @click="addOrAdit()"
                                >新增楼层</el-button
                            >
                            <el-button
                                type="danger"
                                plain
                                @click="deleteHandle()"
                                >批量删除</el-button
                            >
                        </el-form-item>
                    </el-form>
                </div>
                <div style="display: flex">
                    <mainSwitch></mainSwitch>
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
                        1、新增的楼层默认为显示如不想立即显示可点击隐藏该楼层
                    </div>
                    <div class="iconSize">
                        2、排序为商户首页移动端楼层的排列顺序
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <el-table-column type="selection" width="70"> </el-table-column>

            <el-table-column prop="floorName" align="center" label="楼层名称">
            </el-table-column>
            <el-table-column prop="isShow" align="center" label="是否显示">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.isShow == 0"
                        >隐藏</el-tag
                    >
                    <el-tag type="success" v-else>显示</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="sort" align="center" label="排序" width="60">
            </el-table-column>
            <el-table-column
                align="center"
                width="180"
                prop="createDate"
                label="创建时间"
            >
            </el-table-column>

            <el-table-column
                prop="address"
                align="center"
                width="140"
                label="操作"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrAdit(scope.row.id)"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        >删除</el-button
                    >
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
            layout="total, sizes, prev, pager, next, jumper"
        >
        </el-pagination>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import Bread from "@/components/bread";
    // import { businessPageUrl } from '@/api/url'
    // import { storeGrade } from '@/api/api'

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/seller-api/webfloor/page",
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/webfloor",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["运营管理", "h5楼层管理"],
                dataForm: {
                    isShow: "",
                    floorType: "1",
                },
                showFlagList: [
                    { id: "", sgName: "全部" },
                    { id: 0, sgName: "隐藏" },
                    { id: 1, sgName: "显示" },
                ],
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        110;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            addOrAdit(id) {
                this.$emit("addOrAdit", id);
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.dataForm = {
                    isShow: "",
                    floorType: "1",
                };
                this.getData();
            },
        },
    };
</script>
<style lang="scss" scoped>
    .el-input {
        width: 170px;
        height: 40px;
    }
    .el-form--inline {
        /deep/.el-input {
            height: 40px !important;
        }
    }
    .formControlArea {
        height: 70px;
    }
</style>
