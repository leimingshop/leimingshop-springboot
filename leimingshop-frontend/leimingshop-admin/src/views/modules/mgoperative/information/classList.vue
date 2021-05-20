<template>
    <div class="mod-sys__dept">
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>

            <el-form :model="dataForm" @keyup.enter.native="getDataList()">
                <div class="formControlArea">
                    <el-form-item
                        style="disply: block; margin-bottom: 0px !important"
                    >
                        <el-button
                            type="primary"
                            @click="addOrUpdateHandle()"
                            >{{ $t("add") }}</el-button
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
                        1、资讯分类后可在资讯文章新增时进行选择
                    </div>
                </template>
            </el-alert>
        </div>
        <el-table
            :data="dataList"
            row-key="id"
            border
            style="width: 100%"
            :height="tableHeight"
        >
            <el-table-column
                type="index"
                prop="$index"
                label="序号"
                align="center"
                width="70"
            >
                <template slot-scope="scope">{{
                    scope.$index + 1 + (parseInt(page) - 1) * parseInt(limit)
                }}</template>
            </el-table-column>

            <el-table-column
                prop="acName"
                header-align="center"
                align="center"
                label="名称"
            ></el-table-column>
            <el-table-column
                prop="sort"
                header-align="center"
                align="center"
                width="70"
                label="排序号"
            ></el-table-column>
            <el-table-column
                prop="status"
                label="是否启用"
                header-align="center"
                align="center"
                width="120"
            >
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.status == 0"
                        >停用</el-tag
                    >
                    <el-tag type="success" v-else>启用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                :label="$t('handle')"
                fixed="right"
                header-align="center"
                align="center"
                width="150"
            >
                <template slot-scope="scope">
                    <el-button
                        type="text"
                        size="small"
                        @click="addOrUpdateHandle(scope.row.id)"
                        >{{ $t("update") }}
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        @click="deleteHandle(scope.row.id)"
                        >{{ $t("delete") }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update
            v-if="addOrUpdateVisible"
            ref="addOrUpdate"
            @refreshDataList="getDataList"
        ></add-or-update>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./information-class-add-or-update";
    import Bread from "@/components/bread";
    import { deleteInformationClassUrl, informationClassTreeUrl } from "@/api/url";
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: informationClassTreeUrl,
                    deleteURL: deleteInformationClassUrl,
                    deleteIsBatch: true,
                },
                dataForm: {},
                dataList: [],
                dataListLoading: false,
                addOrUpdateVisible: false,
                breaddata: ["运营管理", "资讯管理", "资讯分类"],
                // tableheight: "auto",
                tableHeight:null,
                tableheightBig: 300,
            };
        },
        components: {
            AddOrUpdate,
            Bread,
        },
        watch: {
            "$store.state.mainSwitch"() {
                //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(() => {
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                }, 100);
            },
        },
        mounted(){
            window.onresize = function(){
                this.tableHeight="";
            }.bind(this)
        },
        methods: {},
    };
</script>

<style lang="scss">
    .mod-sys__dept {
        /deep/.el-table__row {
            td:nth-child(2) {
                .cell {
                    display: flex;

                    .el-table__expand-icon {
                        margin-right: 10px;
                    }
                }
            }
        }
    }
</style>