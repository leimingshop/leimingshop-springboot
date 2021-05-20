<template>
    <div>
      <!-- 商品数据组合表格 -->
      <el-table
        :data="dataForm.goodsSpecSaveDTOList"
        border
        style="width: 100%"
         @selection-change="dataListSelectionChangeHandle">

        <el-table-column
            type="selection"
            width="70"
           >
        </el-table-column>

        <el-table-column
          prop="specAttrName"
          :label="(this.choosedSpecLevel_1.length>0 && this.choosedSpecLevel_1[0].name!=='')?this.choosedSpecLevel_1[0].name:'主规格'"
          align="center">
              <template slot-scope="scope">
                <!-- <span>{{scope.row.specName.replace(dataForm.goodsName,"")}}</span> -->
                  <span  v-if="scope.row.specAttrName.split(',').length>0">{{scope.row.specAttrName.split(',')[0]}}</span>
              </template>
        </el-table-column>

          <el-table-column
          v-if="choosedSpecLevel_2.length >1"
          prop="specAttrName"
          :label="(this.choosedSpecLevel_1.length>0 && this.choosedSpecLevel_1[1].name!=='')?this.choosedSpecLevel_1[1].name:'规格值'"
          align="center">
              <template slot-scope="scope">
                  <span v-if="scope.row.specAttrName.split(',').length>1">{{scope.row.specAttrName.split(',')[1]}}</span>
              </template>
        </el-table-column>

        <el-table-column
          v-if="choosedSpecLevel_2.length >2"
          prop="specAttrName"
          :label="(this.choosedSpecLevel_1.length>0 && this.choosedSpecLevel_1[2].name!=='')?this.choosedSpecLevel_1[2].name:'规格值'"
          align="center">
              <template slot-scope="scope">
                  <span v-if="scope.row.specAttrName.split(',').length>2">{{scope.row.specAttrName.split(',')[2]}}</span>
              </template>
        </el-table-column>

          <el-table-column
          prop="specSellPrice"
          label="销售价"
          min-width="155px"
          width="155px"
          align="center">
            <template slot-scope="scope">
			        <!-- <el-input v-model="scope.row.specSellPrice" maxlength="30" placeholder="请输入"  ></el-input> -->
               <el-input-number v-model="scope.row.specSellPrice" size="small" :precision="2" :step="1"  controls-position="right" :min="0.01" :max="999999.99"   @change="changeInput(1,scope.row,scope.$index)"  @input="changeInput(1,scope.row,scope.$index)" ></el-input-number>
            </template>
        </el-table-column>

        <el-table-column
          prop="specCostPrice"
          label="成本价"
            min-width="155px"
          width="155px"
          align="center">
            <template slot-scope="scope">
			        <!-- <el-input v-model="scope.row.specCostPrice" maxlength="30" placeholder="请输入" ></el-input> -->
              <el-input-number v-model="scope.row.specCostPrice" size="small" :precision="2" :step="1"  controls-position="right" :min="0.01" :max="999999.99" @change="changeInput(2,scope.row,scope.$index)"  @input="changeInput(2,scope.row,scope.$index)"></el-input-number>
            </template>
        </el-table-column>

        <el-table-column
          prop="specStorage"
          label="库存"
          min-width="155px"
          width="155px"
          align="center">
            <template slot-scope="scope">
			        <!-- <el-input v-model="scope.row.specStorage" maxlength="30" placeholder="请输入"  ></el-input> -->
               <el-input-number v-model="scope.row.specStorage" width="500px" size="small" step-strictly :precision="0" :step="1"  controls-position="right" :min="0"  :max="999999"  @change="changeInput(3,scope.row,scope.$index)"  @input="changeInput(3,scope.row,scope.$index)"></el-input-number>
            </template>
        </el-table-column>

        <el-table-column
          prop="specWeight"
          label="重量(kg)"
          min-width="155px"
          width="155px"
          align="center">
            <template slot-scope="scope">
			        <!-- <el-input v-model="scope.row.specStorage" maxlength="30" placeholder="请输入"  ></el-input> -->
               <el-input-number v-model="scope.row.specWeight" width="500px" size="small" :precision="2" :step="1"  controls-position="right" :min="0.01"  :max="999999.99"  @change="changeInput(5,scope.row,scope.$index)"  @input="changeInput(5,scope.row,scope.$index)"></el-input-number>
            </template>
        </el-table-column>

        <el-table-column
          prop="specSerial"
          label="商品编码"
          align="center">
            <template slot-scope="scope">
			        <el-input v-model="scope.row.specSerial" class="specSerialInputClass" size="small" width="200px" maxlength="20" placeholder="请输入" @change="changeInput(4,scope.row,scope.$index)"  @input="changeInput(4,scope.row,scope.$index)"></el-input>
                <!-- <el-input-number  v-model="scope.row.specSerial" :precision="0" :step="1"  controls-position="right" :min="0"  :max="99999999999"   maxlength="11"  @change="changeInput(4,scope.row,scope.$index)"  @input="changeInput(4,scope.row,scope.$index)"></el-input-number> -->
            </template>
        </el-table-column>

        <!-- <el-table-column  header-align="center" align="center">
            <template slot-scope="scope">
            </template>
        </el-table-column> -->
      </el-table>
       <div style="margin-top:10px;margin-bottom:10px;" v-if="!goodsSpecSaveDTOList==0">
					<el-button @click="batch()"   type="primary"  size="mini">批量设置</el-button>
       </div>

          <goodsBatch v-if="goodsBatchVisible" ref="goodsBatchCompon"  @batchCbFn="batchCbFn" ></goodsBatch>

    </div>
</template>

<script>
  import cloneDeep from 'lodash/cloneDeep'
import goodsBatch from './modules/model-goods-batch.vue'
export default {
  data () {
    return {
        goodsSpecSaveDTOList:[],
        goodsBatchVisible:false,
        multipleSelection:[],
    }
  },
  props:["dataForm","choosedSpecLevel_1","choosedSpecLevel_2"],
  components: {
    goodsBatch,
  },
   watch: {
      'dataForm.goodsSpecSaveDTOList.length' (val) {
        this.updataGoodsSpecSaveDTOList();
      }
  },
  created () {
  },
  methods: {
      dataListSelectionChangeHandle(val){
          this.multipleSelection = val;
        },
        // savePreData(){
        //     console.log(this.dataForm.goodsSpecSaveDTOList);
        // },
        changeInput(type,row,_index){
          if(!row.specSellPrice){ this.$set(row,"specSellPrice",0)}
          if(!row.specCostPrice){this.$set(row,"specCostPrice",0)}
          if(!row.specStorage){ this.$set(row,"specStorage",0)}
          if(!row.specWeight){ this.$set(row,"specWeight",0)}
          if(type==4){
              console.log(row);
              row.specSerial = row.specSerial+''
              row.specSerial = row.specSerial.replace(/[^a-zA-Z0-9-]/g,"")
              let isHaveRepeatSpecSerial =false;
              this.dataForm.goodsSpecSaveDTOList.forEach((item,index)=>{
                     if(index!=_index && item.specSerial == row.specSerial){
                        isHaveRepeatSpecSerial = true
                     }
              })
              //
              if(isHaveRepeatSpecSerial){
                  this.$message({
                      message:"商品编码已存在!",
                      type: 'warning',
                      duration: 800,
                  })
                 let specSerial =  this.$parent.$parent.goodsSpecSaveDTOList[_index].specSerial
                  // specSerial = "1723341749132" ;
                  setTimeout(()=>{
                    this.dataForm.goodsSpecSaveDTOList[_index].specSerial = specSerial;
                   },0)
                  return;
              }
          }
          this.updataGoodsSpecSaveDTOList();
        },
         updataGoodsSpecSaveDTOList(){
            console.log(this.dataForm.goodsSpecSaveDTOList);
            let totalSpecStorage = 0 ;
            this.dataForm.goodsSpecSaveDTOList.forEach((item,index)=>{
                  totalSpecStorage  +=  parseInt(item.specStorage)
            })
            this.$parent.$parent.$parent.$parent.dataForm.specStorage = totalSpecStorage;
            this.$parent.$parent.goodsSpecSaveDTOList = cloneDeep(this.dataForm.goodsSpecSaveDTOList);
        },
        batch(){
           if(this.multipleSelection.length==0){
               this.$message({
                  message:"请先选择规格!",
                  type: 'warning',
                  duration: 800,
              })
              return;
            }
            this.goodsBatchVisible = true;
            this.$nextTick(()=>{
              this.$refs.goodsBatchCompon.init();
            })
         },
         batchCbFn(dataForm){
            let totalSpecStorage = 0 ;
            this.multipleSelection.forEach((item,index)=>{
                totalSpecStorage  +=  parseInt(item.specStorage)
                       if(dataForm.firstSpecSellPrice!=""){
                     item.specSellPrice = dataForm.firstSpecSellPrice
                  }
                  if(dataForm.firstSpecCostPrice!=""){
                     item.specCostPrice = dataForm.firstSpecCostPrice
                  }
                  if(dataForm.specWeight!=""){
                     item.specWeight = dataForm.specWeight
                  }
                  if(dataForm.firstSpecStorage!=""){
                     item.specStorage = dataForm.firstSpecStorage

                  }
            })
            if(totalSpecStorage) this.$parent.$parent.$parent.$parent.dataForm.specStorage = totalSpecStorage;
            this.$parent.$parent.goodsSpecSaveDTOList = cloneDeep(this.dataForm.goodsSpecSaveDTOList);
        }
  }
}
</script>
<style lang="scss" scoped>
  /deep/ .el-input__inner{
        padding: 0px 10px;
        padding-right: 0px;
  }
  /deep/.el-input-number.is-controls-right .el-input__inner{
    padding-left:0px;
    padding-right:35px;
  }
</style>