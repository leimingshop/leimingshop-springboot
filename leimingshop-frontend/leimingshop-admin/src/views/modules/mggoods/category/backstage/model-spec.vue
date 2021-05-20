<template>
 <!-- 规格 -->
    <el-dialog title="关联规格" :visible.sync="visible" :before-close="closeDialog">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item label="规格名称：">
          <el-input v-model="dataForm.specName" placeholder="规格名称"></el-input>
        </el-form-item>
        <el-form-item label="规格分组：">
          <el-input v-model="dataForm.groupName" placeholder="规格分组"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search()">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table height="220px" v-loading="dataListLoading"  :data="dataList" border style=" width:96%;margin:auto"   ref="multipleTable" @selection-change="dataListSelectionChangeHandle">
        <el-table-column type="selection" width="70"></el-table-column>
        <el-table-column property="specName" label="规格名称"></el-table-column>
        <el-table-column property="specValue" label="规格值"></el-table-column>
        <el-table-column property="specGroupValue" label="规格分组"></el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button @click="cancle()">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确 定</el-button>
      </div>

      <el-pagination
        :current-page="page"
        :page-sizes="[5,10, 20, 50, 100]"
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
import { specePageUrl } from '@/api/url'
import { setTimeout } from 'timers';

export default {
   mixins: [mixinViewModule],
   data() {
    return {
         mixinViewModuleOptions: {
            getDataListURL: specePageUrl,
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
            specName:'',
            groupName:'',
        },
        tempGoodsClassSpecDTOS:[],//防止分页代码数据冲掉
        tableheight: document.body.offsetHeight-550,
    }
  },
  created () {
  },
  methods: {
      init(){
        this.visible = true;
        this.dataListSelections = [];
        this.tempGoodsClassSpecDTOS = this.$parent.dataForm.goodsClassSpecDTO;
        this.$nextTick(()=>{
             this.search();
        })
      },
      search(){
          this.getDataList().then((res)=>{
              this.backScanHook();
          });
      },

        // 处理对勾回显
       backScanHook(){
          var specIds = [];
          this.tempGoodsClassSpecDTOS.forEach((item,index)=>{
              if(item.specId){item.id= item.specId}
              specIds.push(item.id);
          })
          console.log(specIds)
          this.dataList.forEach((item,index)=>{
               if(specIds.indexOf(item.id)!=-1){
                   this.dataListSelections.push(item);
               }
          })
          this.toggleSelection(this.dataListSelections);
      },
      //  处理回显事件
      toggleSelection(rows) {
      	console.log(rows)
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
      changeTempGoodsClassSpecDTOS(){
           var dataListIds = [];
            this.dataList.forEach((item,index)=>{
              dataListIds.push(item.id);
            })
            // 防止当前数据取消对勾
             this.tempGoodsClassSpecDTOS =  this.tempGoodsClassSpecDTOS.filter((item,index)=>{
                  if(dataListIds.indexOf(item.id)==-1){
                      return item
                  }
             })

            this.tempGoodsClassSpecDTOS = this.tempGoodsClassSpecDTOS.concat(this.dataListSelections)
            // 去重处理
            var obj = {};
            var tempGoodsClassSpecDTOS = [];
            tempGoodsClassSpecDTOS =  this.tempGoodsClassSpecDTOS.reduce(function(item, next) {
            obj[next.id] ? '' : obj[next.id] = true && item.push(next);
            return item;
            }, []);
            this.tempGoodsClassSpecDTOS = tempGoodsClassSpecDTOS;
      },
        // 切换页大小
      pageSizeChangeHandleLocal(val){
        this.changeTempGoodsClassSpecDTOS();
        this.pageSizeChangeHandle(val).then(()=>{
            this.backScanHook();
        });
      },
      // 切换分页
      pageCurrentChangeHandleLocal(val){
         this.changeTempGoodsClassSpecDTOS();
        this.pageCurrentChangeHandle(val).then(()=>{
            this.backScanHook();
        });
      },
      dataFormSubmit(){
          this.changeTempGoodsClassSpecDTOS();
          // if(this.tempGoodsClassSpecDTOS.length==0){
          //      this.$message({
          //         message: "请勾选后再提交",
          //         type: "warning",
          //         duration: 1500
          //       })
          //     return
          // }
           console.log( this.tempGoodsClassSpecDTOS);
           this.$parent.dataForm.specIds = []
            this.$parent.dataForm.goodsClassSpecDTO = [];
           this.tempGoodsClassSpecDTOS.forEach((item,index)=>{
              //  if(typeof(item) == "object"){
                    this.$parent.dataForm.specIds.push(item.id);
                    this.$parent.dataForm.goodsClassSpecDTO.push(item);
              //  }else{
              //      this.$parent.dataForm.specIds.push(item);
              //  }
           })
           this.closeDialog();
      },
      closeDialog() {
        console.log("关闭窗口");
        this.visible = false;
        this.$parent.modelSpecVisible = false;
      },
  }
}
</script>
