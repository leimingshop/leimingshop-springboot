<template>
    <div v-loading="loading">
      <Bread  :breaddata="breaddata" @changePage="getList()" :index="'2'"></Bread>
      <h2 style="margin-top:40px">{{$route.query.gcId?"编辑商品":'发布商品'}}</h2>
      <myProgress :showPageNumber="showPageNumber"></myProgress>

      <chooseKind v-show="showPageNumber==1" @showPageNumberFn="showPageNumberFn"></chooseKind>

      <edit  v-if="showPageNumber==2" ref="editCompon" @showPageNumberFn="showPageNumberFn"></edit>

      <finnal v-if="showPageNumber==3" ref="finnalCompon" @showPageNumberFn="showPageNumberFn"></finnal>
    </div>
</template>

<script>
import myProgress from "./myProgress.vue"
import chooseKind from "./chooseKind.vue"
import edit from "./edit.vue"
import finnal from "./finnal.vue"
import Bread from "@/components/bread";
import {backScanDetailGoods} from "@/api/api"
export default {
  data () {
    return {
      loading:false,
      breaddata: ["商品管理", "商品管理", "商品添加"],
      showPageNumber:1,
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
    }
  },
  components: {
    myProgress,
    chooseKind,
    edit,
    finnal,
    Bread
  },
  watch:{
     // 监听路由，如果是商品编辑，需要跳转到商品编辑页面
      "$route.fullPath":function (newVal,oldVal) {
        // console.log(newVal,oldVal);
        if(/mggoods-goods-list-add/.test(newVal)){
            if(this.$route.query.gcId){
              this.getEditData();
               this.breaddata=["商品", "商品管理","商品列表", "商品编辑"]
            }else{
                this.showPageNumberFn(1);
                this.breaddata=["商品管理", "商品管理", "商品添加"]
            }

        }

      },
  },
  created () {
        //  防止刷新
        if(this.$route.query.gcId ){
            this.getEditData();
           this.breaddata=["商品", "商品管理", "商品列表","商品编辑"]
        }else{
              this.breaddata=["商品管理", "商品管理", "商品添加"]
        }
  },
   mounted() {
     window.that = this;
        document.documentElement.scrollTop =0
  },
  methods: {
      // 点击列表返回列表页
      getList(){
          this.$router.push({
              name: "mggoods-goods-list"
          });
      },
    // 如果是编辑商品
    getEditData(){
        var obj  = {
            //  params:{
                id:this.$route.query.gcId
            //  }
        }
        console.log(obj);
        this.loading = true;
        backScanDetailGoods(obj).then((res)=>{
          this.loading = false;
            if(res.code == 200){
              console.log("商品编辑回显数据");
              console.log(res.data);
              Object.assign(this.dataForm,res.data)
              this.dataForm.gcId0 = res.data.firstGcId?res.data.firstGcId:'';
              this.dataForm.gcId1 = res.data.secondGcId?res.data.secondGcId:'';
              this.dataForm.gcId2 = res.data.thirdGcId?res.data.thirdGcId:'';

              this.dataForm.gcName0 = res.data.firstGcName?res.data.firstGcName:'';
              this.dataForm.gcName1 = res.data.secondGcName?res.data.secondGcName:'';
              this.dataForm.gcName2 = res.data.thirdGcName?res.data.thirdGcName:'';

              if(this.dataForm.gcId2){
                this.dataForm.gcId =  this.dataForm.gcId2
                this.dataForm.gcName = this.dataForm.gcName2
              }else if(this.dataForm.gcId1){
                this.dataForm.gcId =  this.dataForm.gcId1
                this.dataForm.gcName = this.dataForm.gcName1
              }else{
                this.dataForm.gcId =  this.dataForm.gcId0
                this.dataForm.gcName = this.dataForm.gcName0
              }

               this.showPageNumberFn(2,this.dataForm);
            }else{

            }
        })
    },
    // 跳转页面
    showPageNumberFn(num,dataForm){
      this.showPageNumber = num;
      // 如果是编辑，需要把分类传过去
      this.$nextTick(()=>{
        if(num==2 && dataForm){
            this.$refs.editCompon.init(dataForm);
        }
      })
    }
  }
}
</script>
