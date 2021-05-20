<template>
    <div>
        <div id="control-area">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>运营</el-breadcrumb-item>
                <el-breadcrumb-item>移动端首页配置</el-breadcrumb-item>
                <el-breadcrumb-item>首页菜单</el-breadcrumb-item>
            </el-breadcrumb>
            <el-form
                :inline="true"
                class="grayLine topGapPadding"
                :model="dataForm"
                @keyup.enter.native="getData()"
            >
                <el-form-item label="菜单名称：">
                    <el-input
                        v-model="dataForm.menuName"
                        placeholder="菜单名称"
                        clearable
                    ></el-input>
                </el-form-item>
                <el-form-item label="是否显示：">
                    <el-select v-model="dataForm.showFlag" placeholder="请选择">
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
                    <el-button class="btn" type="primary" @click="getData()"
                        >查询</el-button
                    >
                    <el-button type="primary" plain @click="reset()"
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
                            v-if="$hasPermission('sys:menu:save')"
                            >新增菜单
                        </el-button>
                        <el-button
                            type="danger"
                            plain
                            @click="deleteHandle()"
                            v-if="$hasPermission('sys:menu:delete')"
                        >
                            批量删除
                        </el-button>
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
                        1、首页菜单为移动端banner轮播图下方的菜单
                    </div>
                    <div class="iconSize">
                        2、菜单个数一行显示5个，超过10个可左滑，菜单标题字数建议为二个字或四个字
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
            <el-table-column prop="menuName" label="菜单名称">
                <template slot-scope="scope">
                    <div
                        style="
                            width: 100%;
                            display: flex;
                            justify-content: center;
                        "
                    >
                        <div style="width: 120px">
                            <span
                                style="width: 60px; margin-right: 20px"
                                v-if="scope.row.menuIcon"
                            >
                                <img
                                    :src="$imgDomain + scope.row.menuIcon"
                                    alt="img"
                                    style="
                                        object-fit: contain;
                                        width: 40px;
                                        height: 40px;
                                    "
                                />
                            </span>
                            <span>{{ scope.row.menuName }}</span>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="showFlag" align="center" label="是否显示">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.showFlag == 0"
                        >隐藏</el-tag
                    >
                    <el-tag type="success" v-else>显示</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createDate" align="center" label="创建时间">
            </el-table-column>

            <el-table-column prop="sort" align="center" label="排序">
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作">
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrAdit(scope.row.id)"
                        v-if="$hasPermission('sys:menu:update')"
                        >编辑</el-button
                    >
                    <el-button
                        type="text"
                        size="small"
                        @click="isShow(scope.row)"
                    >
                        <span v-if="scope.row.showFlag == 1" class="artdanger">
                            禁用
                        </span>
                        <span v-if="scope.row.showFlag == 0"> 启用 </span>
                    </el-button>
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        v-if="$hasPermission('sys:menu:delete')"
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
    import { isShow } from "@/api/api";

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/mobile/indexmenu/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/mobile/indexmenu/batch",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
                dataForm: {
                    showFlag: "",
                },
                showFlagList: [
                    {
                        id: "",
                        sgName: "全部",
                    },
                    {
                        id: 0,
                        sgName: "隐藏",
                    },
                    {
                        id: 1,
                        sgName: "显示",
                    },
                ],
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
            };
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
            isShow(row) {
                console.log(555555555);
                console.log(row.showFlag);
                var msg = row.showFlag === 0 ? "启用" : "禁用";
                this.$confirm("此操作将" + msg + "该菜单, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {
                    var bb = {
                        id: row.id,
                    };
                    if (row.showFlag == 1) {
                        bb.showFlag = 0;
                    } else {
                        bb.showFlag = 1;
                    }
                    isShow(bb).then((res) => {
                        if (res.code == 200) {
                            this.$message({
                                message: res.msg,
                                type: "success",
                                duration: 1500,
                            });
                            this.getData();
                        }
                    });
                });
            },
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.page = 1;
                this.dataForm = {
                    showFlag: "",
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
</style>