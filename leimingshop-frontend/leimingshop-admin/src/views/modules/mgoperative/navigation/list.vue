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
                <el-form-item label="导航标题：">
                    <el-input
                        v-model="dataForm.title"
                        placeholder="导航标题"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button @click="reset()" type="primary" plain
                        >重置</el-button
                    >
                </el-form-item>
            </el-form>

            <el-form>
                <div class="formControlArea">
                    <el-form-item
                        style="disply: block; margin-bottom: 0px !important"
                    >
                        <el-button type="primary" @click="addOrAdit()"
                            >新增</el-button
                        >
                        <el-button type="danger" plain @click="deleteHandle()"
                            >批量删除</el-button
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
                    <div class="iconSize">1、首页导航为PC端顶部导航</div>
                    <div class="iconSize">
                        2、导航最多可添加8个，导航标题字数建议为二个字或四个字
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
                prop="title"
                width="140"
                align="center"
                label="标题"
            >
            </el-table-column>
            <el-table-column
                prop="relationType"
                align="center"
                label="导航类型"
            >
                <template slot-scope="scope">
                    {{
                        scope.row.relationType == 1 ? "自定义链接" : "商品分类"
                    }}
                </template>
            </el-table-column>
            <el-table-column prop="isOpen" align="center" label="新窗口打开">
                <template slot-scope="scope">
                    {{ scope.row.isOpen === 0 ? "否" : "是" }}
                </template>
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
                        >删除
                    </el-button>
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
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/navigation/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/navigation",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                breaddata: ["运营管理", "pc首页配置", "pc首页导航设置"],
                dataForm: {
                    title: "",
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
                        100;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        methods: {
            addOrAdit(id) {
                if (!id && this.total >= 8) {
                    this.$message({
                        message: "最多添加八个导航",
                        type: "error",
                        duration: 1500,
                    });
                    return;
                }
                this.$emit("addOrAditFn", id);
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.dataForm.title = "";
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