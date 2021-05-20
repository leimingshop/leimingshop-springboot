<template>
    <div>
        <div id="control-area">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>运营</el-breadcrumb-item>
                <el-breadcrumb-item>广告管理</el-breadcrumb-item>
                <el-breadcrumb-item>H5楼层广告列表</el-breadcrumb-item>
            </el-breadcrumb>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="楼层名称：">
                    <el-input
                        v-model="dataForm.floorName"
                        placeholder="楼层名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="广告标题：">
                    <el-input
                        v-model="dataForm.advTitle"
                        placeholder="广告标题"
                        clearable
                    ></el-input>
                </el-form-item>
                <!-- <el-form-item label="楼层类别：">
             <el-select  v-model="dataForm.type"  placeholder="请选择">
                <el-option
                    key="2"
                    label="pc楼层"
                    value="2">
                </el-option>

                <el-option
                    key="1"
                    label="h5楼层"
                    value="1">
                </el-option>
            </el-select>
        </el-form-item> -->
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
                    <el-button type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
                <br />
            </el-form>
            <el-form>
                <div class="formControlArea">
                    <el-form-item
                        style="disply: block; margin-bottom: 0px !important"
                    >
                        <el-button
                            type="primary"
                            @click="addOrAdit()"
                            v-if="$hasPermission('sys:floor:adv:add')"
                            >添加广告
                        </el-button>
                        <el-button
                            type="danger"
                            plain
                            @click="deleteHandle()"
                            v-if="$hasPermission('sys:batch:adv:delete')"
                        >
                            批量删除</el-button
                        >
                    </el-form-item>
                    <div style="display: flex">
                        <mainSwitch></mainSwitch>
                        <mainTipsMessage></mainTipsMessage>
                    </div>
                </div>
            </el-form>
            <el-alert
                title="操作提示"
                type="warning"
                @close="$store.commit('showListMessage')"
                v-show="$store.state.listMessageFlag"
            >
                <template slot="title">
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">
                        1、添加广告选择的楼层为移动端首页楼层创建的楼层，如不先创建楼层，则无法选择楼层广告的楼层
                    </div>
                    <div class="iconSize">
                        2、楼层广告默认为开启，如不想立即显示在前台可设置定时开启
                    </div>
                    <div class="iconSize">
                        3、如不设置停用时间则该广告会一直显示
                    </div>
                    <div class="iconSize">
                        4、同一楼层设置多个广告时会轮播显示
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
                align="center"
                label="序号"
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
            <el-table-column prop="floorName" align="center" label="楼层名称">
            </el-table-column>

            <el-table-column prop="type" align="center" label="楼层类别">
                <template slot-scope="scope">
                    <span v-if="scope.row.type == 1">H5楼层</span>
                    <span v-if="scope.row.type == 2">PC楼层</span>
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
                width="180"
                align="center"
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
                        v-if="$hasPermission('sys:floor:adv:edit')"
                        >编辑</el-button
                    >
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:floor:adv:delete')"
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
    // import { businessPageUrl } from '@/api/url'
    // import { storeGrade } from '@/api/api'

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/adv/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/adv",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                dataForm: {
                    advType: "1",
                    type: "1",
                },
                startList: [],
                endList: [],
                tableheight: document.body.offsetHeight - 480,
                tableheightBig: 300,
            };
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
                        110;
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
                this.dataForm.advType = "1";
                this.dataForm.type = "1";
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
</style>