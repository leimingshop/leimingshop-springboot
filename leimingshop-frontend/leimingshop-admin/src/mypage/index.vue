<template>
  <div>
      <el-button type="primary" @click="addFn">新增</el-button>
   

      <el-table
        :data="dataList"
        border
        style="width: 100%">
            <!-- 第一列 -->
            <el-table-column
              prop="goodsName"
              label="商品名"
              width="150">
            </el-table-column>
            <!-- 第二列 -->
            <el-table-column
              prop="price"
              label="价格"
              width="150">
            </el-table-column>
            <!-- 第三列 -->
            <el-table-column
              prop="spec"
              label="颜色"
              width="150">
            </el-table-column>
           <!-- 第四列 -->
            <el-table-column
              label="上下架状态"
              width="150">
                <template  slot-scope="scope">
                  <span v-if="scope.row.status==1">上架</span>
                   <span v-if="scope.row.status==0" style="color:red">下架</span>
                </template>
            </el-table-column>
           <!-- 第五列 -->
          <el-table-column
            label="操作"
            width="150">
              <template  slot-scope="scope">
                  <el-button  type="primary" @click="editFn(scope.row)">编辑</el-button>
              </template>
          </el-table-column>
        </el-table>
      <modelAddOrEdit v-if="addOrEditVisible" ref="addOrEditCompon" ></modelAddOrEdit>
  </div>
</template>
<script>
import  modelAddOrEdit from  "./model-add-or-edit.vue"
export default {
  data(){
    return {
      addOrEditVisible:false,
      dataList:[
        {id:1,goodsName:"华为手机",price:"0.1",spec:'红色',status:1},
        {id:2,goodsName:"ios手机",price:"0.2",spec:'白色',status:0},
        {id:3,goodsName:"山寨手机",price:"1000",spec:'彩色',status:1},
      ]
      // row:{id:1,goodsName:"华为手机",price:"0.1",sepc:'红色'}
    }
  },
  components:{
    modelAddOrEdit
  },
  created () {},
  methods: {
    // 新增弹框
    addFn(){
        this.addOrEditVisible = true;
        this.$nextTick(()=>{
            this.$refs.addOrEditCompon.init();
        })
    },
    // 编辑弹框
    editFn(row){
       this.addOrEditVisible = true;
        this.$nextTick(()=>{
            this.$refs.addOrEditCompon.init(row);
        })
    },
  }
}
</script>
<style lang="scss">

</style>
