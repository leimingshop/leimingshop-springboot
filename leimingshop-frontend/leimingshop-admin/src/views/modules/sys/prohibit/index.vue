<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div>
            <el-form :inline="true" :model="dataForm" class="grayLine" @keyup.enter.native="getData()">
                <el-form-item label="关键字搜索：">
                    <el-input v-model.trim="dataForm.name" placeholder="请输入关键字搜索" clearable style="width: 220px!important;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="getData()" type="primary">搜索</el-button>
                    <el-button  @click="reset()" plain type="primary">重置</el-button>
                </el-form-item>
            </el-form>
            <el-form>
                <el-button type="primary" @click="addOrUpdateHandle()" >添加禁用词</el-button>
                 <importAndExport :importAndExportOptions="importAndExportOptions" :dataForm="dataForm"  @getDataList="getDataList" style="margin-left: 30px;"></importAndExport>
            </el-form>
            <el-table
                    v-loading="dataListLoading"
                    :data="dataList"
                    border
                    @selection-change="dataListSelectionChangeHandle"
                    @sort-change="dataListSortChangeHandle"
                    style="width: 100%;margin-top: 10px;">
                <el-table-column
                        type="index"
                        prop="$index"
                        label="序号"
                        align="center"
                        width="70">
                    <template slot-scope="scope">
                        {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="禁用词名称" header-align="center" align="center"></el-table-column>
                <el-table-column prop="createDate" label="添加时间" header-align="center" align="center"></el-table-column>
                <el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
                    <template slot-scope="scope" >
                        <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row)">编辑</el-button>
                        <el-button  type="text" class="artdanger" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                    :current-page="page"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="limit"
                    :total="total"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="pageSizeChangeHandle"
                    @current-change="pageCurrentChangeHandle">
            </el-pagination>
            <!-- 弹窗, 新增 / 修改 -->
            <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" ></add-or-update>
        </div>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module';
    import Bread from "@/components/bread";
    import AddOrUpdate from './add-or-update';
    import importAndExport from "@/components/import-and-export";
    import { getadvertisingban} from '@/api/api.js';
    import { advertisingban } from "@/api/url";
    import { importAdvertisingbanUrl} from '@/api/io'
    export default {
        mixins: [mixinViewModule],
        data () {
            return {
                importAndExportOptions:{
                    importUrl:importAdvertisingbanUrl,//导入接口
                    importWord:"导入",
                },
                mixinViewModuleOptions: {
                    getDataListURL: '/admin-api/stopword/page',
                    getDataListIsPage: true,
                    deleteURL: advertisingban,
                    deleteIsBatch: false
                },
                dataListLoading:true,
                breaddata: ["系统管理", "禁用词管理"],
                dataForm: {
                    name:'',
                },
                addOrUpdateVisible: true,
            }
        },
        components: {
            AddOrUpdate,
            Bread,
            importAndExport
        },
        watch:{
            // 'dataForm.name':function(newV,oldV) {
            //     var chineseCount = 0,characterCount = 0;
            //     for (let i = 0; i < newV.length; i++) {
            //         if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
            //             chineseCount = chineseCount + 2;
            //         } else { //字符
            //             characterCount = characterCount + 1;
            //         }
            //         var count = chineseCount + characterCount;
            //         if (count > 600) { //输入字符大于600的时候过滤
            //             this.dataForm.name = newV.substr(0,(chineseCount/2+characterCount)-1)
            //         }
            //     }
            // },
        },
        methods:{
            getData(){
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset(){
                this.dataForm = {};
                this.getData();
            },
            addOrUpdateHandle(row){
            	this.addOrUpdateVisible = true;
            	this.$nextTick(()=>{
            		this.$refs.addOrUpdate.init(row);
            	})
            	
            }
        }
    }

</script>

<style lang="scss" scoped>
</style>