<template>
	<div>
		<list v-if="mainVisible" ref="listCompon" @showDetail="showDetail" @addOrAdit="addOrAdit"></list>
		<detail v-if="dtailVisible" ref="detailCompon" @showList="showList" :breaddata="breaddata" :index="index"></detail> 
		<addoradit v-if="addoraditVisible" ref="addoraditCompon" @addoraditList="addoraditList"></addoradit> 
	</div>
</template>

<script>
  import list from "./list"
//   import detail from "./detail"
//   import addoradit from "./store-add-or-update"
  import detail from "./preview"
  import addoradit from "./store-add-or-update2"
	export default {
		data () {
		    return {
				index:'2',
          mainVisible:true,
          dtailVisible:false,
		  addoraditVisible:false,
		  breaddata: ["商家系统", "商家管理", "商家列表",'商家详情'],
		    }
		},
		components:{
			list,
      detail,
      addoradit
		},
	  methods:{
			showDetail(row){
        this.dtailVisible = true;
        this.mainVisible = false;
				this.$nextTick(()=>{
					this.$refs.detailCompon.init(row);
				})
      },
      addOrAdit(id){
        this.addoraditVisible = true;
        this.mainVisible = false;
				this.$nextTick(()=>{
					this.$refs.addoraditCompon.init(id);
				})
      },
			showList(){
        this.dtailVisible = false;
        this.mainVisible = true;
        this.$nextTick(()=>{
            this.$refs.listCompon.getDataList();
        		})
      },
      addoraditList(){
        this.addoraditVisible = false;
        this.mainVisible = true;
        	this.$nextTick(()=>{
            this.$refs.listCompon.getDataList();
        		})
      }
	
		}
	}
</script>

<style>
</style>