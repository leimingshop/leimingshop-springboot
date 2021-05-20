<template>
  <div>
    <!--列表-->
    <list v-if="couponIsshow==1" ref="listCompent"
          @changeCompent="changeCompent"
          @changePage="changePage"></list>

    <!--查看-->
    <detail 
		v-if="couponIsshow== 2" 
		:index="'1'"
		:breaddata= '["营销系统", "拼团记录", "拼团记录详情"]'
		ref="detailCompon"
		@changePage="changePage"></detail>



  </div>
</template>

<script>
	import list from "./list.vue";
	import detail from "./details.vue";
	// import orderDetail from "./orderDetail/orderDet"

	export default {
		name: "index",
		data() {
			return {
				couponIsshow: 1,
				orderDetBreaddata: ["拼团记录","拼团记录" ,"拼团记录详情", "订单详情"],
			}
		},
		components: {
			list,
			detail,
			// orderDetail
		},
    watch:{
			"$route.fullPath"(newPath,oldPath){
				if(this.$route.query.tabIndex == 2 && this.$route.path == '/mgoperative-group-record'){
					let groupRecordId = this.$route.query.groupRecordId;
          this.changeCompent(groupRecordId,2)
        }
      },
    },
		created() {
			if(this.$route.query.tabIndex == 2){
				let groupRecordId = this.$route.query.groupRecordId;
				this.changeCompent(groupRecordId,2)
			}else {
				this.changePage();
      }
		},
		methods: {
			changeCompent(groupRecordId,index) {
				// 1.列表  2拼团记录详情
				if (index == 1) {
					this.couponIsshow = index
					this.$nextTick(() => {
						this.$refs.listCompent.getData(row);
					})
				} else if(index == 2){
					this.couponIsshow = index;
					this.$nextTick(() => {
						this.$refs.detailCompon.init(groupRecordId);
					})
				}
			},
			//返回列表页
			changePage() {
				this.couponIsshow = 1
				this.$nextTick(() => {
					this.$refs.listCompent.getData()
				})
			}
		}
	}
</script>

<style scoped>

</style>
