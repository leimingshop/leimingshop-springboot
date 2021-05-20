<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-form>
            <div style="display:flex;justify-content:space-between">
                <el-button type="danger" @click="delGoods()">批量删除</el-button>
                <el-button type="primary" @click="addGoods()">新增商品</el-button>
            </div>
            <br/>
        </el-form>
        <el-table
            :data="dataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
        <el-table-column type="selection" width="70" label="全选"> </el-table-column>
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
                        (parseInt(params.currentPage) - 1) * parseInt(params.currentPageSize)
                    }}
                </template>
            </el-table-column>
            <el-table-column prop="goodsName" label="商品名称" align="center">
            </el-table-column>
            <el-table-column prop="goodsId" label="SPU" align="center">
            </el-table-column>
            <el-table-column prop="gcName" label="分类" align="center">
            </el-table-column>
            <el-table-column prop="specSellPrice" label="价格" align="center">
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作">
                <template slot-scope="scope">
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        @click="delGoods(scope.row.id)"
                        >删除</el-button
                    >
                </template>
            </el-table-column>
             </el-table>
                <!-- <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="params.currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="params.currentPageSize"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper">
           </el-pagination> -->
        <div style="text-align:center">
            <el-button @click="goback()">取消</el-button>
            <el-button @click="submitStore()" type="primary">确定</el-button>
        </div>

        <!--添加商品信息列表-->
        <el-dialog :visible.sync="dialogTableVisible">
            <el-form :inline="true" :model="openDataForm"  @keyup.enter.native="onSearch1()">
            <el-form-item prop="goodsName" label="商品名称：">
                <el-input v-model="openDataForm.goodsName" placeholder="请输入商品名称"></el-input>
            </el-form-item>
        <el-form-item label="后台类目：">
            <el-cascader
                clearable
                filterable
                v-model="gcIds"
                :options="goodscalssOption"
                :props="props"
                @change="finishCange()"
            ></el-cascader>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSearch1()" class="query">查询</el-button>
                <el-button type="primary" plain @click="onReset1()" class="reset">重置</el-button>
            </el-form-item>
            </el-form>
            <el-form>
                <el-form-item>
                    <el-button @click="selectionAdd()" type="primary">批量选择</el-button>
                </el-form-item>
                <br/>
            </el-form>
            <el-table
            :row-key="getRowKeys"
            ref="multipleTable"
            :data="openDataList"
            v-loading="dataListLoading"
            border
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%"
            :height="!$store.state.mainSwitch ? tableheight : tableheightBig"
        >
        <el-table-column
        :reserve-selection="true"
            type="selection"
            width="70"
            :selectable="checkboxT"
            label="全选"></el-table-column>
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
                        (parseInt(pages) - 1) * parseInt(limits)
                    }}
                </template>
            </el-table-column>
            <el-table-column prop="goodsName" label="商品名称" align="center">
            </el-table-column>
            <el-table-column prop="id" label="SPU" align="center">
            </el-table-column>
            <el-table-column prop="gcName" label="分类" align="center">
            </el-table-column>
            <el-table-column prop="specSellPrice" label="价格" align="center">
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作">
                <template slot-scope="scope">
                    <el-button
                        class="artdanger"
                        type="text"
                        size="small"
                        v-if="scope.row.aa==1"
                        @click="selectionAdd(scope.row,0)"
                        >选择</el-button>
                    <el-button class="danger" v-if="scope.row.aa==2" type="text" size="small">已选择</el-button>
                </template>
            </el-table-column>
            </el-table>
                <el-pagination
                @size-change="pageSizeChangeHandle1"
                @current-change="pageCurrentChangeHandle1"
                :current-page="pages"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limits"
                :total="totals"
                layout="total, sizes, prev, pager, next, jumper">
           </el-pagination>
        </el-dialog>
    </div>
</template>
<script>
import mixinViewModule from '@/mixins/view-module'
import {topicAdd,topicUpdate,mobbileGoodsList,topicSave,deleteTopic,getTopicGoods,allTreeGoodsclass} from '@/api/api';
import Bread from "@/components/bread";
export default {
    mixins: [mixinViewModule],
    data(){
        return{
            mixinViewModuleOptions: {
                    getDataListURL: "/admin-api/topic/goods/page",
                    getDataListIsPage: true,
                    // deleteURL: "/admin-api/webfloor/del",
                    // deleteIsBatch: true,
                    // deleteIsBatchKey: 'id'
                },
            breaddata: ["运营管理", "专题管理", "专题页管理"],
            dataListLoading:false,
            btnFlag:false,
            dialogTableVisible:false,
            tableheight: document.body.offsetHeight - 480,
            tableheightBig: 300,
            limits:10,
            limit:10,
            pages:1,
            goodscalssOption:[],
            params: {
                currentPage: 1, //当前页数
                currentPageSize: 10, //每页显示的条数
            },
            dataForm:{
                topicId:'',
            },
            menuId:null,
            showSize:0,
            row:[],
            total:0,
            num:0,
            totals:0,
            gcIds:[],
            props: {
                value: "id",
                label: "gcName",
            },
            dataList:[],
            openDataForm:{
                goodsName:'',
            },
            openDataList:[],
            dataListSelections: [],
        }
    },
     components:{
            Bread,
        },
    methods:{
        // 分页, 每页条数
    pageSizeChangeHandle(val) {


    },
        // 获取分类的所有树形结构
    getallTreeGoodsclassFn() {
      allTreeGoodsclass().then((res) => {
        if (res.code == 200) {
          this.goodscalssOption = res.data;
        }
      });
    },
    pageSizeChangeHandle1(val) {
      this.pages = 1
      this.limits = val
    },
    getRowKeys(row) {
      return row.id;
    },
    goback(){
        this.$emit("closeGoods");
    },
    // 分页, 当前页
    pageCurrentChangeHandle(val) {
    },

    finishCange() {
      let len = this.gcIds.length;
      if (len != -1) {
        this.openDataForm.gcId = this.gcIds[this.gcIds.length - 1];
      }
    },
    pageCurrentChangeHandle1(val) {
      this.pages = val
      return this.addGoods()
    },
       init(dataForm,id){
           this.getallTreeGoodsclassFn();
           this.id=id;
           this.topicName=dataForm.topicName;
           this.topicPicturePc=dataForm.topicPicturePc;
           this.topicPictureH5=dataForm.topicPictureH5;
            if(this.id){
                this.dataForm.topicId=id
                this.limit=dataForm.goodsIdList.length
                this.getDataList();
            }
       },
       addGoods(){
           this.dialogTableVisible=true
             let obj = {
                params:{
                    page:this.pages,
                    limit:10,
                    sellFlag:0,
                    goodsShow:1,
                }
            }
            if(this.openDataForm.goodsName){
                obj.params.goodsName=this.openDataForm.goodsName
            }
            if(this.openDataForm.gcId){
                obj.params.gcId=this.openDataForm.gcId
            }
           mobbileGoodsList(obj).then((res)=>{
               if(res.code=200){
                    this.openDataList=res.data.list
                    this.totals=res.data.total
                    this.openDataList.forEach(a=>{
                        a.aa=1
                        this.dataList.forEach(b=>{
                            if(a.id==b.goodsId){
                                a.aa=2
                            }
                        })
                    })
               }
           })
       },
       //添加商品
       selectionAdd(rows,num){
           if(rows){
                rows.goodsId=rows.id
                this.dataList.push(rows);
                this.totals=this.totals+1
           }else{
               this.dataListSelections.forEach((item,index)=>{
                   item.goodsId=item.id
                    this.$refs.multipleTable.toggleRowSelection(item,false)
                   this.dataList.push(item)
                   this.totals=this.totals+1
               })
           }
            this.total=this.dataList.length
           this.dialogTableVisible=false
       },
            //弹窗内容
            onSearch1(){
                this.pages=1;
                this.limits=10;
                this.addGoods();
            },
            onReset1(){
                this.pages = 1;
                this.openDataForm = {
                };
                this.gcIds=[]
                this.onSearch1()
            },
            checkboxT(rows){
                if(rows.aa==2){
                   return false;
                }else{
                    return  true;
                }
            },
             delGoods(rows) {
                 if(rows){
                    this.dataList.forEach((item,index)=>{
                        if(this.dataList[index].id==rows){
                            this.dataList.splice(index,1)
                            this.total=this.dataList.length
                        }
                    })
                 }else{
                    for(var i=0;this.dataListSelections.length>i;i++){
                        this.dataList.forEach((v,k)=>{
                            if(this.dataListSelections[i].id==v.id){
                                this.dataList.splice(k,1)
                                this.total=this.dataList
                            }
                        })
                    }
                    console.log(this.total)
                 }
            },
        submitStore(){
            var aa =[]
            this.dataList.forEach(a=>{
                aa.push(a.goodsId);

            })
            if(aa.length==0){
                 this.$message.error("请选择商品")
                 return
            }
            var obj ={
                goodsIdList:aa,
                topicName:this.topicName,
                topicPicturePc:this.topicPicturePc,
                topicPictureH5:this.topicPictureH5,
            }
            var method =  this.id ? topicUpdate :topicSave;
            if(this.id){
                obj.id=this.id
            }
           method(obj).then(res=>{
                if(res.code=200){
                    this.$message.success("保存成功")
                    this.$emit("changePage")
                }else{
                    this.$message.error("保存失败")
                }
            })
        }

    },
}
</script>
<style lang="scss" scoped>
.danger{

}
/deep/ .el-form-item__content{
    margin-left: 0px !important;
}
/deep/ .el-form-item__label{
    width: auto !important;
}
/deep/ .el-form--inline .el-form-item{
    margin-right: 20px !important;
}
</style>
