<template>
	<div>
		<list v-show="!dtailVisible &&!orderdtailVisible&&!storedtailVisible" ref="listCompon" @showDetail="showDetail"></list>
		<detail v-if="dtailVisible &&!orderdtailVisible&&!storedtailVisible" ref="detailCompon"   @orderDetail="orderDetail" @storeDetail="storeDetail" @showList1="showList1"></detail> 
		<orderDet v-if="orderdtailVisible" ref="orderdetailCompon" @changePage="changePage"  :orderDetBreaddata="orderDetBreaddata" :index="index"></orderDet>
		<storeDet v-if="storedtailVisible" ref="storedetailCompon" @showListFn="changePage" @showList="showList"  :breaddata="breaddata" :index="index"></storeDet>
	</div>
</template>

<script>
	import list from "./list"
	import detail from "./detail"
	import orderDet from "@/views/modules/mgorder/order/list/orderDet";
	import storeDet from "@/views/modules/store/shopmg/shopList/preview";
	export default {
		data () {
		    return {
				index:"3",
				row:{},
				dtailVisible:false,
				orderdtailVisible:false,
				storedtailVisible:false,
				orderDetBreaddata:["运营管理","客服管理","投诉列表","投诉详情","订单详情"],
				breaddata:["运营管理","客服管理","投诉列表","投诉详情","店铺详情"],
		    }
		},
		components:{
			list,
			detail,
			orderDet,
			storeDet
		},
	  	methods:{
			showDetail(row){
				this.dtailVisible = true;
				this.orderdtailVisible=false;
				this.storedtailVisible=false;
				this.$nextTick(()=>{
					this.$refs.detailCompon.init(row);
				})
				this.row=row
			},
			showList1(){
				this.dtailVisible = false
					this.$nextTick(()=>{
					this.$refs.listCompon.init();
				})

			},
			showList(){
				this.showDetail(this.row)
			},
			orderDetail(row){
				this.dtailVisible=true;
				this.orderdtailVisible=true;
            	this.$nextTick(()=>{
                this.$refs.orderdetailCompon.init(row);
            	})
			},

			storeDetail(id){
				this.dtailVisible=true;
				this.orderdtailVisible=false;
				this.storedtailVisible=true;
				this.$nextTick(()=>{
					this.$refs.storedetailCompon.init(id);
				})
			},
		changePage(){
          this.showDetail(this.row)
        }
	
		}
	}
</script>

<style>
</style>