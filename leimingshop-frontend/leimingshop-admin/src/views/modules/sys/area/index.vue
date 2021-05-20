<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>

        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()">
         
            <el-form-item label="地区名称：">
                <el-input v-model="dataForm.areaName" placeholder="请输入地区名称" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
                <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
            </el-form-item>
            <br/>
        </el-form>
        <el-form>
            <el-form-item>
                <el-button type="primary" @click="addOrEditHandle()">新增数据</el-button>
                <el-button type="primary" @click="goback()" v-if="(this.dataList.length==0||this.dataList[0].areaDeep!=1) && this.flag" >返回上级</el-button>
            </el-form-item>
        </el-form>

        <el-table
                width="100%"
                :data="dataList"
                border
                v-loading="dataListLoading"
                style="width: 100%"
                
                @selection-change="dataListSelectionChangeHandle">



            <el-table-column
                    prop="areaName"
                    label="地区名称"
                    align="center"
                    min-width="200">
            </el-table-column>
            <el-table-column
                    prop="regionName"
                    label="所属大区"
                    align="center"
                    min-width="200">
                    <template slot-scope="scope">
                        <span  v-if="!scope.row.regionName">-</span>
                          <span  v-else>{{scope.row.regionName}}</span>
                     </template>
            </el-table-column>

            <el-table-column
                    prop="areaDeep"
                    label="所在层级"
                    align="center"
                    min-width="200">
            </el-table-column>


            <el-table-column
                    prop="superName"
                    label="上级"
                    align="center"
                    min-width="200">{{this.superName}}
            </el-table-column>

            <el-table-column
                    label="操作"
                    align="center"
                    min-width="220"
            >
                <template slot-scope="scope">
                    <el-button @click.native.prevent="addOrEditHandle(scope.$index, scope.row)" v-show="scope.row.areaDeep!=1" type="text" size="mini" > 编辑 </el-button>
                    <el-button class="artdanger" @click.native.prevent="deleteHandle(scope.row.id)" type="text" size="mini" v-show="scope.row.areaDeep!=1">删除</el-button>
                     <el-button class="artdanger"   type="text" size="mini" @click="getData(scope.row.id,scope.row.areaName,scope.row.areaParentId,true)">下级地区</el-button>
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

        <!-- 弹窗, 新建 -->
        <addEditData v-if="addEditDataVisible" ref="addEditData" @searchDataList="getDataList"></addEditData>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import { areaDel} from '@/api/url'
     import { parentInfo} from '@/api/api'
    import addEditData from './model-add-edit-data'
    import Bread from "@/components/bread"

    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                breaddata: ["系统设置", "地区管理"],
                mixinViewModuleOptions: {
                    getDataListURL: '/admin-api/area/page',
                    getDataListIsPage: true,
                    deleteURL: areaDel,
                    deleteIsBatch: false,
                    deleteIsBatch: false,
                    deleteIsBatchKey: 'id'
                },
                flag:false,
                addEditDataVisible: false,
                superName:'-',
                superName2:'',
                parentId2:"",
                parentInfos:{},
                dataForm: {
                    parentId: '',
                    areaName: '',
                },
                value:'',
                areaDeep:"",
                params: {
                    "currentPage": 1, //当前页数
                    "currentPageSize": 10, //每页显示的条数
                },
            }
        },
        components: {
            addEditData,
            Bread,
        },
        created() {
        },
        mounted() {
        },
        methods: {
            // 新建和编辑
            addOrEditHandle(index = -1, row = "") {
                this.setAddEditDataVisible(true);
                this.$nextTick(() => {
                    this.$refs.addEditData.init(row)
                })
            },
            setAddEditDataVisible(boolargu) {
                this.addEditDataVisible = boolargu;
            },
            getData(id,name,parentId2,status) {
                if(status){
                    this.dataForm.areaName=''
                }
                if(this.superName){
                    this.superName2=this.superName
                }
                if(name){
                    this.superName=name
                }else{
                     this.superName='-'
                }
                if(id){
                    this.dataForm.parentId=id
                }
                if(parentId2){
                    this.parentId2=parentId2
                }
                if(this.dataForm.parentId){
                    this.dataForm.areaDeep=''
                    // this.dataForm.areaName=''
                }
                if(this.dataForm.areaName){
                    this.dataForm.parentId=''
                    this.flag=false
                }else{
                    this.flag=true
                }
                this.page = 1;
                this.limit = 10;
                this.getDataList();
                if(this.dataList.length>0){
                    this.value=this.dataList[0].areaDeep
                }
              
            },
            goback(){
                
                    this.infos(this.parentId2);
              
                this.goback2();
            },
            goback2(){
                this.dataForm.areaName = "";
                this.dataForm.parentId =this.parentId2;
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.parentId = "";
                this.dataForm.areaName = "";
                this.dataForm.areaDeep ="";
                this.flag=false
                this.dataList.length==0
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            infos(id){
                var obj={
                    "id":id
                }
                parentInfo(obj).then((res)=>{
                    if(res.code==200){
                        if(res.data==null){
                          this.superName='-';
                        }else{
                            this.parentInfos=res.data
                            this.parentId2=this.parentInfos.areaParentId;
                            this.superName=this.parentInfos.areaName;
                        }
                       
                    }
                })
            },
        }
    }
</script>
