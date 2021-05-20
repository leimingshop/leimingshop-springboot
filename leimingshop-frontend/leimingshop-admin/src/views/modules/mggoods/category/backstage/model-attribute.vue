<template>
    <!-- 属性 -->
    <el-dialog title="关联属性" :visible.sync="visible" :before-close="closeDialog">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item label="属性名称：">
          <el-input v-model="dataForm.attrName" placeholder="属性名称"></el-input>
        </el-form-item>
        <el-form-item label="属性分组：">
          <el-input v-model="dataForm.groupName" placeholder="属性分组"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search()">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table  height="220px" v-loading="dataListLoading" :data="dataList" border style=" width:96%;margin:auto"   ref="multipleTable" @selection-change="dataListSelectionChangeHandle">
        <el-table-column type="selection" width="100"></el-table-column>
        <el-table-column property="attrName" label="属性名称"></el-table-column>
        <el-table-column property="attrValue" label="属性值"></el-table-column>
        <el-table-column property="attrGroupValue" label="属性分组"></el-table-column>
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
import { attributePageUrl } from '@/api/url'

export default {
    mixins: [mixinViewModule],
   data() {
    return {
        mixinViewModuleOptions: {
            getDataListURL: attributePageUrl,
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
            attrName:'',
            groupName:'',
        },
        specData:[],
        tempGoodsClassAttrDTOS:[],//防止分页代码数据冲掉
        tableheight: document.body.offsetHeight-550,
    }
  },
  created () {
  },
    
  mounted(){
    window.that = this
  },
  methods: {
      init(){
        this.visible = true;
        this.dataListSelections = [];
        // var attrIds = this.$parent.dataForm.attrIds;
        this.tempGoodsClassAttrDTOS = this.$parent.dataForm.goodsClassAttrDTO;
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
          var attrIds = [];
          this.tempGoodsClassAttrDTOS.forEach((item,index)=>{
              if(item.attrId){item.id= item.attrId}
              attrIds.push(item.id);
          })
          this.dataList.forEach((item,index)=>{
               if(attrIds.indexOf(item.id)!=-1){
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
      changeTempGoodsClassAttrDTOS(){
           var dataListIds = [];
            this.dataList.forEach((item,index)=>{
              dataListIds.push(item.id);
            })
            // 防止当前数据取消对勾
             this.tempGoodsClassAttrDTOS =  this.tempGoodsClassAttrDTOS.filter((item,index)=>{
                  if(dataListIds.indexOf(item.id)==-1){
                      return item
                  }
             })

            this.tempGoodsClassAttrDTOS = this.tempGoodsClassAttrDTOS.concat(this.dataListSelections)
            // 去重处理
            var obj = {};
            var tempGoodsClassAttrDTOS = [];
            tempGoodsClassAttrDTOS =  this.tempGoodsClassAttrDTOS.reduce(function(item, next) {
            obj[next.id] ? '' : obj[next.id] = true && item.push(next);
            return item;
            }, []);
            this.tempGoodsClassAttrDTOS = tempGoodsClassAttrDTOS;
      },
        // 切换页大小
      pageSizeChangeHandleLocal(val){
        this.changeTempGoodsClassAttrDTOS();
        this.pageSizeChangeHandle(val).then(()=>{
            this.backScanHook();
        });
      },
      // 切换分页
      pageCurrentChangeHandleLocal(val){
         this.changeTempGoodsClassAttrDTOS();
        this.pageCurrentChangeHandle(val).then(()=>{
            this.backScanHook();
        });
      },
      dataFormSubmit(){
          this.changeTempGoodsClassAttrDTOS();
          // if(this.tempGoodsClassAttrDTOS.length==0){
          //      this.$message({
          //         message: "请勾选后再提交",
          //         type: "warning",
          //         duration: 1500
          //       })
          //     return
          // }
           console.log( this.tempGoodsClassAttrDTOS);
           this.$parent.dataForm.attrIds = []
           this.$parent.dataForm.goodsClassAttrDTO = [];
           this.tempGoodsClassAttrDTOS.forEach((item,index)=>{
              //  if(typeof(item) == "object"){
                    this.$parent.dataForm.attrIds.push(item.id);
                    this.$parent.dataForm.goodsClassAttrDTO.push(item);
              //  }else{
              //      this.$parent.dataForm.attrIds.push(item);
              //  }
           })
           this.closeDialog();
      },
      closeDialog() {
        console.log("关闭窗口");
        this.visible = false;
        this.$parent.modelAttributeVisible = false;
      },
  }
}
</script>
