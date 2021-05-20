<template>
    <!-- 属性 -->
    <el-dialog title="关联品牌" :visible.sync="visible" :before-close="closeDialog">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item label="品牌名称：">
          <el-input v-model="dataForm.brandName" placeholder="品牌名称"></el-input>
        </el-form-item>
        <!-- <el-form-item label="品牌分组：">
          <el-input v-model="dataForm.groupName" placeholder="品牌分组"></el-input>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="search()">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table  height="220px"  v-loading="dataListLoading"  :data="dataList" border style=" width:96%;margin:auto;"   ref="multipleTable" @selection-change="dataListSelectionChangeHandle">
        <el-table-column type="selection" width="100"></el-table-column>
        <el-table-column property="brandName" label="品牌名称"></el-table-column>
        <el-table-column property="brandNameEn" label="品牌英文名"></el-table-column>

        <!-- <el-table-column property="attrValue" label="属性值"></el-table-column> -->
        <!-- <el-table-column property="attrGroupValue" label="属性分组"></el-table-column> -->
      </el-table>
      <div slot="footer" class="dialog-footer" style="text-align: center;">
         <el-button @click="cancle()">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确 定</el-button>
      </div>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandleLocal"
        @current-change="pageCurrentChangeHandleLocal">
      </el-pagination>
    </el-dialog>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import { brandPageUrl } from '@/api/url'
import cloneDeep from 'lodash/cloneDeep'
export default {
    mixins: [mixinViewModule],
   data() {
    return {
        mixinViewModuleOptions: {
            getDataListURL: brandPageUrl,
            activatedIsNeed:false,
            getDataListIsPage: true,
            // // exportURL: '/admin-api/log/login/export',
            // deleteURL: deleteSpecUrl,
            // deleteIsBatch: false,
            // deleteIsBatch: true,
            // deleteIsBatchKey: 'id'
        },
        visible:false,
        dataForm:{
            brandName:'',
            groupName:'',
        },
        specData:[],
        // brandIds:[],
        tempGoodsClassBrandSaveDTOList:[],//防止分页代码数据冲掉
        tableheight: document.body.offsetHeight-550,
    }
  },
  created () {
  },
  methods: {
      init(){
        window.that = this;
        this.visible = true;
        this.dataListSelections = [];
        // this.brandIds = []
        this.tempGoodsClassBrandSaveDTOList = cloneDeep(this.$parent.dataForm.goodsClassBrandSaveDTOList);
        this.$nextTick(()=>{
             this.search();
        })
      },
      search(){
          this.getDataList().then((res)=>{
              this.backBrandHook();
          });
      },
       // 处理对勾回显
       backBrandHook(){
         var brandIds = []
          this.tempGoodsClassBrandSaveDTOList.forEach((item,index)=>{
              if(item.brandId){item.id= item.brandId}
              brandIds.push(item.id);
          })
          this.dataList.forEach((item,index)=>{
               if(brandIds.indexOf(item.id)!=-1){
                   this.dataListSelections.push(item);
               }
           })
            this.toggleSelection(this.dataListSelections);
      },
      //  处理回显事件
      toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      cancle(){
          this.closeDialog();
      },
      // 切换分页时候保存不同页里面选中的数据
      changeTempGoodsClassBrandSaveDTOList(){
            var dataListIds = [];
            this.dataList.forEach((item,index)=>{
              dataListIds.push(item.id);
            })
            // 防止当前数据取消对勾
             this.tempGoodsClassBrandSaveDTOList =  this.tempGoodsClassBrandSaveDTOList.filter((item,index)=>{
                  if(dataListIds.indexOf(item.id)==-1){
                      return item
                  }
             })
            this.tempGoodsClassBrandSaveDTOList = this.tempGoodsClassBrandSaveDTOList.concat(this.dataListSelections)
            // 去重处理
            var obj = {};
            var tempGoodsClassBrandSaveDTOList = [];
            tempGoodsClassBrandSaveDTOList =  this.tempGoodsClassBrandSaveDTOList.reduce(function(item, next) {
            obj[next.id] ? '' : obj[next.id] = true && item.push(next);
            return item;
            }, []);
            this.tempGoodsClassBrandSaveDTOList = tempGoodsClassBrandSaveDTOList;
      },
      // 切换页大小
      pageSizeChangeHandleLocal(val){
        this.changeTempGoodsClassBrandSaveDTOList();
        this.pageSizeChangeHandle(val).then(()=>{
            this.backBrandHook();
        });
      },
      // 切换分页
      pageCurrentChangeHandleLocal(val){
         this.changeTempGoodsClassBrandSaveDTOList();
        this.pageCurrentChangeHandle(val).then(()=>{
            this.backBrandHook();
        });
      },
      dataFormSubmit(){
          this.changeTempGoodsClassBrandSaveDTOList();
          // if(this.tempGoodsClassBrandSaveDTOList.length==0){
          //      this.$message({
          //         message: "请勾选后再提交",
          //         type: "warning",
          //         duration: 1500
          //       })
          //     return
          // }
           console.log( this.dataListSelections);
          //  this.brandIds = []
           this.$parent.dataForm.goodsClassBrandSaveDTOList = [];
           this.tempGoodsClassBrandSaveDTOList.forEach((item,index)=>{
                  // this.brandIds.push(item.id);
                  this.$parent.dataForm.goodsClassBrandSaveDTOList.push({
                      "brandId":item.id,
                      "brandName":item.brandName,
                  });
           })


           this.closeDialog();
      },
      closeDialog() {
        console.log("关闭窗口");
        this.visible = false;
        this.$parent.modelBrandVisible = false;
      },
  }
}
</script>
