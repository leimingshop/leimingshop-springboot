<template>
    <div>
        <el-breadcrumb >
        <el-breadcrumb-item  v-for="(item,_index) of breaddata" :key="_index">
            <span @click="changePage(index,_index)" :class="index==_index?'canClick':'noclick'">{{item}}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
</template>
<script>
export default {
    name:'bread',
    props: {
      breaddata: Array,
      index: {
        type: String,
        default:"-1"
      },
    },
    methods: {
      changePage(index,_index){
        if(index == _index){
          this.$emit("changePage");
        }
      }

    }
}
</script>
<style>
.canClick{
  cursor: pointer;
  color: #2260D2;
}
</style>

