<template>
	<div>
		<list v-show="!dtailVisible" @showDetail="showDetail"></list>
		<detail v-if="dtailVisible" ref="detailCompon" @showList="showList"></detail> 
	</div>
</template>

<script>
	import list from "./list"
	import detail from "./detail"
	export default {
		data () {
		    return {
		    	dtailVisible:false,
		    }
		},
		components:{
			list,
			detail
		},
	  	methods:{
			showDetail(row){
				this.dtailVisible = true;
				this.$nextTick(()=>{
					this.$refs.detailCompon.init(row);
				})
			},
			showList(){
				this.dtailVisible = false
			}
	
		}
	}
</script>

<style>
</style>