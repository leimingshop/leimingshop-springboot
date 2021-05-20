<template>
  <div>
    <list v-show="isShowTemplateIndex == 1"
			:status="status"
			:breaddata="breaddata"
          	@changeCompent="changeCompent"
			>
	</list>
    <!--    电子退货码详情-->
    <codeDet
        ref="orderDetailCompon"
        v-if="isShowTemplateIndex == 2"
        :index="'2'"
        @changeCompent="changeCompent"
        @changePage="changePage"
        :orderDetBreaddata="orderDetBreaddata"
    ></codeDet>

  </div>
</template>

<script>
	import list from "./list";
	import codeDet from "@/views/modules/mgorder/order/fetchCodeList/codeDet";
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
					return ["订单系统", "订单管理", "电子提货码"];
				}
			},
			// 订单详情面包屑
			orderDetBreaddata: {
				type: Array,
				default() {
					return ["订单管理", "订单管理", "电子提货码列表", "电子提货码详情"];
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


		},
		components: {
			list,
			codeDet,
		}
	};
</script>
<style scoped>

</style>
