<template>
  <div>
      <list v-show="showListVisible" @hiddenList="hiddenList" ref="backstaageList"></list>
      <addEditData ref="addEditData" v-if="!showListVisible" @showList="showList" :dataArray="dataArray"></addEditData>
   
  </div>
</template>
<script>
import list  from "./list";
import addEditData from "./add-edit-data";


export default {
  data() {
    return {
      showListVisible:true,
      dataArray:[],
    };
  },
  components: {
    addEditData,
    list,
  },
  created () {
  },
  methods: {
    showList(){
         this.showListVisible = true;
         this.$nextTick(()=>{
           this.$refs.backstaageList.init();
         })
         
    },
    hiddenList(row){
        this.showListVisible = false;
        this.$nextTick(()=>{
          this.$refs.addEditData.init(row);
        })

    },
  }
}
</script>
