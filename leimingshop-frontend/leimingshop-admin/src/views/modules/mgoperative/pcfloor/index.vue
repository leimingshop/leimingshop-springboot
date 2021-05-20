<template>
	<div>
		<list v-if="mainVisible" ref="listCompon" @addOrAdit="addOrAdit"></list>
    <addoradit v-if="addoraditVisible" ref="addoraditCompon" @addoraditList="addoraditList"></addoradit> 
	</div>
</template>

<script>
	import list from "./list"
  import addoradit from "./addAdit"
	export default {
		data () {
		    return {
          mainVisible:true,
          addoraditVisible:false
		    }
		},
		components:{
			list,
      addoradit
		},
	  methods:{
      addOrAdit(id){
        this.addoraditVisible = true;
        this.mainVisible = false;
				this.$nextTick(()=>{
					this.$refs.addoraditCompon.init(id);
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