<template>
  <div>
    <list v-show="isShowTemplateIndex == 1"
			:status="status"
			:breaddata="breaddata"
          	@changeCompent="changeCompent"
			@showGroupDetail="showGroupDetail">
	</list>
    <!--    订单详情-->
    <orderDet
        ref="orderDetailCompon"
        v-if="isShowTemplateIndex == 2"
        :index="'2'"
        @changeCompent="changeCompent"
        @changePage="changePage"
        :orderDetBreaddata="orderDetBreaddata"
    ></orderDet>


	  <!--查看拼团信息-->
    <detail
		v-if="isShowTemplateIndex == 3"
		ref="groupDetailCompon"
		:index="'2'"
		:breaddata= '["订单系统","订单管理","拼团列表", "拼团记录详情"]'
		@changePage="hiddenGrupDetail"></detail>

  </div>
</template>

<script>
	import list from "./list";
	import orderDet from "@/views/modules/order/list/orderDet";
	import detail from "@/views/modules/mgoperative/group/record/details.vue";
	export default {
		data() {
			return {
				isShowTemplateIndex: 1,
				// orderDetBreaddata:["订单管理", "订单管理", "订单列表", "订单详情"],
				couponIsshow:1,
			};
		},
		props: {
			status: {
				type: Number,
			},
			// 列表面包屑
			breaddata: {
				type: Array,
				default() {
					return ["订单系统", "订单管理", "拼团列表"];
				}
			},
			// 订单详情面包屑
			orderDetBreaddata: {
				type: Array,
				default() {
					return ["订单管理", "订单管理", "拼团列表", "订单详情"];
				}
			},
		},
		watch: {
			'$route.fullPath'(newVal, oldVal) {
				if (this.$route.query.tabIndex == 2 && this.$route.path == '/order-grouplist') {
					this.changeCompent(this.$route.query.orderItem, 2)
				}
			}
		},
		created() {
			if (this.$route.query.tabIndex == 2) {
				this.changeCompent(this.$route.query.orderItem, 2)
			}
		},
		methods: {
			changePage() {
				this.isShowTemplateIndex = 1;
			},
			changeCompent(row, index) {
				if (index == 2) {
					this.isShowTemplateIndex = 2;
					this.$nextTick(() => {
						this.$refs.orderDetailCompon.init(row);
					})
				} else {
					this.changePage();
				}
			},
			// 展示拼团详情页面
			showGroupDetail(row){
				this.isShowTemplateIndex = 3;
				window.that = this;
				this.$nextTick(() => {
					this.$refs.groupDetailCompon.init(row.groupRecordId);
				})

			},
			// 隐藏拼团详情页面
			hiddenGrupDetail(bool){
				this.isShowTemplateIndex = 1;
			}

		},
		components: {
			list,
			orderDet,
			detail
		}
	};
</script>
<style scoped>

</style>
