<template>
  <div>
    <list v-show="!showSkuListVisible" ref="listCompon" @showSkuListFn="showSkuListFn"></list>

    <skuList v-if="showSkuListVisible" ref="skuListCompon" @showListFn="showListFn"></skuList>
  </div>
</template>

<script>
import list from "./list.vue"
import skuList from "./skuList.vue"


  
export default {
  data () {
    return {
      showSkuListVisible:false,
    };
  },
  components: {
    list,
    skuList
  },
  created() {
  },
  methods: {
    showListFn(){
      this.showSkuListVisible = false;
      this.$nextTick(()=>{
          this.$refs.listCompon.getDataList();
      })
    },
    showSkuListFn(row){
      this.showSkuListVisible = true
      this.$nextTick(()=>{
          this.$refs.skuListCompon.init(row);
      })
    }
  }
};
</script>
<style lang="scss" scoped>

</style>
