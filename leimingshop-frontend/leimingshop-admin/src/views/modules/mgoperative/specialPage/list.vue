<template>
    <div>
        <div id="control-area">
            <Bread :breaddata="breaddata"></Bread>
            <el-form
            :inline="true"
            :model="dataForm"
            ref="dataForm"
            @keyup.enter.native="onSearch()">
                <el-form-item label="专区名称：">
                    <el-input v-model="dataForm.topicName" placeholder="专区名称" clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="onSearch()" class="query">查询</el-button>
                    <el-button @click="onReset()" class="reset">重置</el-button>
                </el-form-item>
                <br/>
            </el-form>

            <el-form>
                <div class="formControlArea">
                    <el-button  class="addpage" @click="addOrAdit()">新建专题页</el-button>
                </div>
            </el-form>
           <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
            <!-- <el-table-column type="selection" width="70" label="全选"> </el-table-column> -->
            <el-table-column
                type="index"
                prop="$index"
                align="center"
                label="序号"
                width="90"
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
                prop="topicName"
                width="180"
                align="center"
                label="专题页名称"
            >
            </el-table-column>
            <el-table-column prop="goodsNum" align="center" label="商品数量">
            </el-table-column>
            <el-table-column
                prop="createDate"
                align="center"
                width="180"
                label="创建时间"
            >
            </el-table-column>
            <!-- <el-table-column
                prop="endDate"
                width="180"
                align="center"
                label="停用时间"
            >
            </el-table-column>
            <el-table-column prop="sort" label="排序" align="center" width="60">
            </el-table-column> -->
            <el-table-column
                label="操作"
                align="center"
                width="180">
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
                        @click="deleteSpecial(scope.row.id)"
                        >删除
                    </el-button>
                </template>
                </el-table-column>
        </el-table>
           <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="params.currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="params.currentPageSize"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper">
           </el-pagination>
           <el-dialog
            :visible.sync="dialogVisible"
            width="30%"
            >
            <span>是否删除此专题页</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="sure()">确 定</el-button>
            </span>
            </el-dialog>
        </div>
    </div>
</template>
<script>
import Bread from "@/components/bread";
import mixinViewModule from "@/mixins/view-module";
import {getTopic,deleteTopic} from "@/api/api"
    export default {
         mixins: [mixinViewModule],
        data(){
            return{
                mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/topic/page",
                    getDataListIsPage: true,
                    deleteURL: "/admin-api/topic/del",
                    deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },

                breaddata: ["运营管理", "专题管理", "专题页管理"],
                tableheight: document.body.offsetHeight - 420,
                tableheightBig: 300,
                dataForm:{
                    topicName:''
                },
                id:"",
                dataList:[],
                dialogVisible:false,
                dataListLoading:false,
                params: {
                    currentPage: 1, //当前页数
                    currentPageSize: 10, //每页显示的条数
                },
            }

        },
        components:{
            Bread,
        },
        watch:{
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
      created(){
          this.getDataList()
      },
        methods:{
            pageSizeChangeHandle(val) {
            this.page = 1
            this.limit = 10
            return this.getDataList()
            },
     // 分页, 当前页
    pageCurrentChangeHandle(val) {
      this.page = val
      return this.getDataList()
    },
            addOrAdit(id){
                this.$emit('addOrAdit',id);
            },
             deleteSpecial(id) {
                 this.dialogVisible=true;
                 this.id=id;
            },
            sure(){
                if (this.id) {
                    var params = {
                        id:this.id
                    }
                    deleteTopic(params).then((res) => {
                        console.log(res);
                        if (res.code == 200) {
                            this.$message.success("删除成功")
                            this.getDataList()
                        } else {
                             this.$message.error(res.msg)
                        }
                    });
                }
                this.dialogVisible=false;
                this.getDataList();
            },
            onSearch() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },

            onReset() {
                this.page = 1;
                this.dataList = [];
                this.dataForm.topicName=''
                this.onSearch();
            },
        }
    }
</script>
<style lang="scss" scoped>
   .addpage,.query{
        color: #fff;
        background-color: #2260D2;
        border-color: #2260D2;
   }
   .reset{
        color: #2260D2;
        background: #e6f4fc;
        border-color: #9cd3f2;
        &:hover{
            color: #fff;
            background-color: #2260D2;
            border-color: #2260D2;
        }
   }
</style>
