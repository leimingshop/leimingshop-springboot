<template>
  <div>
    <list v-show="!showLogList" @showLogListFn="showLogListFn"></list>

    <loginList v-if="showLogList" ref="logListCompon" @showListFn="showListFn"></loginList>
  </div>
</template>

<script>
import list from "./list.vue"
import loginList from "./loglist.vue"


  
export default {
  data () {
    return {
        showLogList:false,
    };
  },
  components: {
    list,
    loginList
  },
  created() {
  },
  methods: {
    showListFn(){
      this.showLogList = false;
    },
    showLogListFn(id){
      this.showLogList = true
      this.$nextTick(()=>{
          this.$refs.logListCompon.init(id);
      })
    }
  }
};
</script>
<style lang="scss" scoped>

</style>
