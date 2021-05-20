<template>
	<div>
		<!-- 列表页 -->
		<list v-show="showListVisible" ref="listCompon"  @showExamineFn="showExamineFn"></list>

		<!-- 审核页 -->
		<examine v-if="!showListVisible" ref="examineCompon" @showListFn="showListFn"></examine>
	</div>
</template>
<script>
import examine from "./examine";
import list from "./list";
export default {
	data () {
		return {
			showListVisible:true,
		}
	},
	components:{
		examine,
		list
	},
	methods:{
		showListFn(){
			this.showListVisible = true;
			this.$nextTick(()=>{
				this.$refs.listCompon.getDataList();
			})
		},
		showExamineFn(row){
			this.showListVisible = false;
			this.$nextTick(()=>{
				this.$refs.examineCompon.init(row);
			})
		},

	}
}
</script>
<style>
</style>