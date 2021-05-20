<template>
  <div>
      <!-- <div>
          您最近使用的类目：
        </div> -->
      	<el-form
		    		:model="dataForm"
		    		ref="addForm"
		    		@keyup.enter.native="dataFormSubmit('addForm')"
		    		label-width="120px">
            <el-form-item>
                <el-select
                  v-model="dataForm.gcId0"
                  placeholder="请选择"
                  loading-text="加载中···"
                  @visible-change="getGoodsClassFn(1)">
                  <el-option
                    v-for="item in goodscalssOption0"
                    :key="item.id"
                    :label="item.gcName"
                    :value="item.id">
                  </el-option>
                </el-select>
                <span style="color:#999999"> - </span>
                <el-select
                v-model="dataForm.gcId1"
                  placeholder="请选择"
                  :loading="goodKind1loading"
                  loading-text="加载中···"
                  @visible-change="getGoodsClassFn(2)">
                  <el-option
                    v-for="item in goodscalssOption1"
                    :key="item.id"
                    :label="item.gcName"
                    :value="item.id">
                  </el-option>
                </el-select>
                <span style="color:#999999"> - </span>
                <el-select
                v-model="dataForm.gcId2"
                  placeholder="请选择"
                  :loading="goodKind2loading"
                  loading-text="加载中···"
                   @visible-change="changeIschooseEnd()">
                  <el-option
                    v-for="item in goodscalssOption2"
                    :key="item.id"
                    :label="item.gcName"
                    :value="item.id">
                  </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div class="bottomTip">
            <span>您当前的选择：</span>
            <span>{{dataForm.gcName0}} </span>
            <span v-if="dataForm.gcName1">> {{dataForm.gcName1}} </span>
            <span v-if="dataForm.gcName2">> {{dataForm.gcName2}} </span>
            <!-- <span v-for="item in goodscalssOption0">
               <span v-if="dataForm.gcId0==item.id">{{item.gcName}}</span>
            </span>
            <span v-if="dataForm.gcId1">></span>

            <span v-for="item in goodscalssOption1">
              <span v-if="dataForm.gcId1==item.id">{{item.gcName}}</span>
            </span>
            <span v-if="dataForm.gcId2">></span>

             <span v-for="item in goodscalssOption2">
                <span v-if="dataForm.gcId2==item.id">{{item.gcName}}</span>
            </span> -->
        </div>
        <div class="nextStepWrap">
            <el-button class="nextStep" type="primary" @click="nextStep()" >下一步</el-button>
        </div>
  </div>
</template>

<script>
import {getGoodscalss} from "@/api/api.js";

export default {
   data () {
    return {
      goodKind1loading:false,
      goodKind2loading:false,
      ischooseEnd:false,
      dataForm: {
          gcId: "",//分类ID
          gcId0:'',
          gcId1:'',
          gcId2:'',
          gcName:'',
          gcName0:'',
          gcName1:'',
          gcName2:'',
      },
      goodscalssOption0: [],
      goodscalssOption1: [],
      goodscalssOption2: [],
    };
  },
   watch: {
      'dataForm.gcId0' (val) {
          this.goodscalssOption0.forEach((item,index)=>{
              if(item.id==this.dataForm.gcId0){
                this.dataForm.gcName0 =  item.gcName;
              }
          })
      },
       'dataForm.gcId1' (val) {
           this.goodscalssOption1.forEach((item,index)=>{
              if(item.id==this.dataForm.gcId1){
                this.dataForm.gcName1 =  item.gcName;
              }
          })
      },
      'dataForm.gcId2' (val) {
          this.goodscalssOption2.forEach((item,index)=>{
              if(item.id==this.dataForm.gcId2){
                this.dataForm.gcName2 =  item.gcName;
              }
          })
      }
    },
  components: {
  },
  created () {
    // 获取商品分类下拉
    this.getGoodsClassFn(0);
  },
  methods: {
      // 后去商品分类（三级联动）
      getGoodsClassFn(arguId){
          var id= 0;
            if(arguId==1){//二级关联
                if(!this.dataForm.gcId0){this["goodscalssOption"+arguId] = []; return;}
                id = this.dataForm.gcId0;

                this.dataForm.gcId1 = "";
                this.dataForm.gcId2 = "";
                this["goodscalssOption1"] = [];
                this["goodscalssOption2"] = [];
        
            }else if(arguId==2){//三级关联
                if(!this.dataForm.gcId1) {this["goodscalssOption"+arguId] = []; return;}
                id = this.dataForm.gcId1;

                this.dataForm.gcId2 = "";
                this["goodscalssOption2"] = [];
            }else{//一级关联
                id=0;
                this.dataForm.gcId1 = "";
                this.dataForm.gcId2 = "";
                this["goodscalssOption1"] = [];
                this["goodscalssOption2"] = [];
            }
            var obj = {
                id:id
            }
            getGoodscalss(obj).then((res)=>{
                console.log(res)
                if(res.code == 200){
                  if(arguId==0){
                      this["goodscalssOption"+arguId] = res.data;//[{id:"",gcName:"全部"}].concat(res.data);
                  }else{
                    this["goodscalssOption"+arguId] = res.data;
                  }
                  if( res.data.length==0){
                      this.ischooseEnd = true;
                  }else{
                      this.ischooseEnd = false;
                  }
                }
            })
      },
      changeIschooseEnd(){
        this.ischooseEnd = true;
      },
      nextStep(){
        // console.log(this.dataForm);
        if(!this.ischooseEnd){
           this.$message({
              message: '请选择分类',
              type: 'warning',
              duration: 1500,
          });
          return
        }
        var gcId = "";
        var gcName = '';
        var gcId0 = this.dataForm.gcId0;
        var gcId1 = this.dataForm.gcId1;
        var gcId2 = this.dataForm.gcId2;
        if(gcId2){
          gcId = gcId2;
          gcName = this.dataForm.gcName0;
        }else if(gcId1){
          gcId = gcId1;
           gcName = this.dataForm.gcName1;
        }else if(gcId0){
          gcId = gcId0;
           gcName = this.dataForm.gcName2;
        }
        if(!gcId){
          this.$message({
              message: '请选择分类',
              type: 'warning',
              duration: 1500,
          });
          return
        }
        this.dataForm.gcId = gcId;
        this.dataForm.gcName = gcName;
        this.$emit("showPageNumberFn",2,this.dataForm);
      },
  }
}
</script>
<style lang="scss" scoped>
.el-form{
  margin-top: 60px;
  .el-form-item{
    width: 90%;
    margin:auto;
    /deep/ .el-form-item__content{
      display: flex;
      justify-content: space-between;
      margin-left: 0 !important;
      .el-select{
          width: 23%;
      }
    }
  }
}
/deep/ .el-form-item__content::after, /deep/  .el-form-item__content::before{
        display: flex !important;
     content: none !important;
}
.bottomTip{
  margin:auto;
  width: 90%
  ;height: 40px;
  line-height:40px; 
  background: #ffbb40;
  border: 2px solid rgba(255, 153, 51, 1);
  padding-left:8px;
  margin-top:200px;
}
.nextStepWrap{
   margin-top:30px;
   text-align: center;
   margin-left: 0 !important;
}

</style>
