<template>
	<div>
		<list v-show="mainVisible" ref="listCompon" @showDetail="showDetail"></list>
	</div>
</template>

<script>
	import list from "./list"
    // import detail from "./detail"
    import orderDet from "@/views/modules/mgorder/order/list/orderDet";
    import afterDet from "@/views/modules/mgorder/aftersalelist/afterDet";
    import { returnDetail } from "@/api/api";

	export default {
		data () {
		    return {
                index:"3",
                index2:"3",
                orderDetBreaddata:["财务管理","对账管理","对账列表","对账详情","订单详情"],
                afterDetBreaddata:["财务管理","对账管理","对账列表","对账详情","售后详情"],
                mainVisible:true,
                dtailVisible:false,
                orderdtailVisible:false,
                returndtailVisible:false,
                row:"",
                         piclist: [], //凭证照片
              aftersale: [],
              goodsData: [], //售后商品table
              saleGoods: [], //售后申请数据
              barinfor:[],//物流
              infor:[],
              saleAuditLog: [], //商家售后理由数据
              adminAuditLog: [], //平台售后理由数据
              saleLogs: [], //操作日志
              data: {}, //总数据
              isSelect:'',//退换货类型
              isBtn : false,//操作按钮
              isSellerFin: true, //商家审核
              isAdminFin: false,
              isLog :true
		    }
		},
		components:{
			list,
            // detail,
            afterDet,
            orderDet
  
		},
	  methods:{
        showDetail(row){
            this.dtailVisible = true;
            this.mainVisible = false;
            this.row = row;
            this.$nextTick(()=>{
                this.$refs.detailCompon.init(row);
            })
        },
        orderDetail(row){
            this.dtailVisible = false;
            this.mainVisible = false;
            this.orderdtailVisible=true;
            this.returndtailVisible=false;
            this.$nextTick(()=>{
                this.$refs.orderdetailCompon.init(row);
            })
        },
        //跳转售后  
         afterDetail(row){
            this.dtailVisible = false;
            this.mainVisible = false;
            this.orderdtailVisible=false;
            this.returndtailVisible=true;
            this.$nextTick(()=>{
            this.getSalesDet(row);
            })
        },
            //查看详情
    getSalesDet(index) {
      const obj = {
        aftersaleSn: index.returnSn
      }; 
        returnDetail(obj).then(res => {
          if (res.code == 200) {
            this.data = res.data;
            this.aftersale = res.data.aftersaleApplyDTO;
            this.saleGoods = res.data.aftersaleGoodsDTOList;
            this.infor = res.data.aftersaleReturnDTO;
            this.saleAuditLog = res.data.aftersaleAuditLogDTOList[0]; //商家审核数据
            this.adminAuditLog = res.data.aftersaleAuditLogDTOList[1] || ""; //平台审核数据
            this.saleLogs = res.data.aftersaleLogListDTOList;
            this.piclist = this.aftersale.aftersalePics.indexOf(",")!=-1?this.aftersale.aftersalePics.split(","):[this.aftersale.aftersalePics];
            this.row.id = res.data.aftersaleApplyDTO.orderId;
          } else {
            this.$message({
              type: "warning",
              message: res.msg
            });
          }
        });
    },
        changePage(){
            if(this.orderdtailVisible){
               this.dtailVisible=true;
                this.mainVisible=false;
                this.orderdtailVisible=false;
                this.$nextTick(()=>{
                     this.$refs.detailCompon.init(this.row);
                })
            }else if(this.returndtailVisible){
              this.dtailVisible=true;
              this.mainVisible=false;
              this.orderdtailVisible=false;
              this.returndtailVisible=false;
              this.$nextTick(()=>{
                     this.$refs.detailCompon.init(this.row);
                })
            }else{
                this.dtailVisible=false;
                this.mainVisible=true
            }
           
        }
	
		}
	}
</script>

<style>
</style>