<template>
    <div>
        <!-- <el-breadcrumb separator="/">
        <el-breadcrumb-item>运营</el-breadcrumb-item>
        <el-breadcrumb-item>广告管理</el-breadcrumb-item>
        <el-breadcrumb-item>广告列表</el-breadcrumb-item>
    </el-breadcrumb> -->
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="广告类别：">
                    <el-input
                        maxlength="20"
                        v-model="dataForm.categoryName"
                        placeholder="广告类别名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="广告标题：">
                    <el-input
                        maxlength="20"
                        v-model="dataForm.advTitle"
                        placeholder="广告标题"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="广告类型：">
                    <el-select v-model="dataForm.advKey" placeholder="请选择">
                        <el-option key="2" label="pc广告" value="pcStoreIndex">
                        </el-option>

                        <el-option key="1" label="h5广告" value="storeIndex">
                        </el-option>

                        <el-option
                            key="3"
                            label="pc店铺首页logo"
                            value="pcStoreLogo"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="启用时间：">
                    <el-date-picker
                        v-model="startList"
                        type="datetimerange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        end-placeholder="结束日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <br />
                <el-form-item label="停用时间：">
                    <el-date-picker
                        v-model="endList"
                        type="datetimerange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        end-placeholder="结束日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
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
                                >添加广告</el-button
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
                        1、添加广告可选择PC店铺首页logo，PC及H5首页轮播广告
                    </div>
                    <div class="iconSize">
                        2、广告默认为开启，如不想立即显示在前台可设置定时开启
                    </div>
                    <div class="iconSize">
                        3、如不设置停用时间则该广告会一直显示
                    </div>
                    <div class="iconSize">
                        4、广告图设置跳转链接时则可点击跳转到指定页面，不设置跳转链接的广告图点击无跳转效果
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
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">
                    {{
                        scope.$index +
                        1 +
                        (parseInt(page) - 1) * parseInt(limit)
                    }}
                </template>
            </el-table-column>
            <el-table-column
                prop="imageUrl"
                width="140"
                align="center"
                label="广告图"
            >
                <template slot-scope="scope" v-if="scope.row.imageUrl">
                    <img
                        :src="$imgDomain + scope.row.imageUrl"
                        alt="img"
                        style="object-fit: contain; width: 60px; height: 60px"
                    />
                </template>
            </el-table-column>
            <el-table-column
                prop="categoryName"
                align="center"
                label="广告类别名称"
            >
            </el-table-column>

            <el-table-column prop="advKey" align="center" label="广告类型">
                <template slot-scope="scope">
                    <span v-if="scope.row.advKey == 'storeIndex'">H5广告</span>
                    <span v-if="scope.row.advKey == 'pcStoreIndex'"
                        >PC广告</span
                    >
                    <span v-if="scope.row.advKey == 'pcStoreLogo'"
                        >PC店铺首页logo</span
                    >
                </template>
            </el-table-column>
            <el-table-column prop="advTitle" align="center" label="广告标题">
            </el-table-column>
            <el-table-column
                prop="startDate"
                align="center"
                width="180"
                label="启用时间"
            >
            </el-table-column>
            <el-table-column
                prop="endDate"
                align="center"
                width="180"
                label="停用时间"
            >
            </el-table-column>
            <el-table-column prop="sort" label="排序" align="center" width="60">
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作">
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
                    getDataListURL: "/seller-api/adv/page",
                    getDataListIsPage: true,
                    deleteURL: "/seller-api/adv",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["运营管理", "广告列表"],
                dataForm: {
                    advType: "0",
                },
                startList: [],
                endList: [],
                tableheight: document.body.offsetHeight - 480,
                tableheightBig: 300,
            };
        },
        components: {
            Bread,
        },
        watch: {
            startList(newval, oldval) {
                if (newval) {
                    this.dataForm.startDate1 = newval[0];
                    this.dataForm.startDate2 = newval[1];
                } else {
                    this.dataForm.startDate1 = "";
                    this.dataForm.startDate2 = "";
                }
            },
            endList(newval, oldval) {
                if (newval) {
                    this.dataForm.endDate1 = newval[0];
                    this.dataForm.endDate2 = newval[1];
                } else {
                    this.dataForm.endDate1 = "";
                    this.dataForm.endDate2 = "";
                }
            },
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        90;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            addOrAdit(id) {
                this.$emit("addOrAditFn", id);
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.startList = [];
                this.endList = [];
                this.dataForm = {};
                this.dataForm.advType = "0";
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
