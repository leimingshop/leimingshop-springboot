<template>
	<div>
		<list v-if="mainVisible" ref="listCompon" @showDetail="showDetail" @addOrAdit="addOrAdit"></list>
		<detail v-if="dtailVisible" ref="detailCompon" @showList="showList"></detail> 
    <addoradit v-if="addoraditVisible" ref="addoraditCompon" @addoraditList="addoraditList"></addoradit> 
	</div>
</template>

<script>
	import list from "./list"
  import detail from "./detail"
  import addoradit from "./store-add-or-update"
	export default {
		data () {
		    return {
          mainVisible:true,
          dtailVisible:false,
          addoraditVisible:false
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