<template>
    <Modal
        v-model="visible"
        :mask-closable="false"
        width="394"
        class="modal-confirm-receipt"
        :loading="confirmReceiptLoading"
        @on-ok="handleConfirm"
        @on-cancel="handleCancel"
    >
    	<div class="hint">请确认您是否已收到货物？</div>
	</Modal>
</template>

<script>
	import { mapState, mapActions } from 'vuex'
	export default{
		name: 'modelConfirmReceipt',
		data(){
			return{
				visible: false
			}
		},
		components:{

		},
		created(){

		},
		computed:{
			...mapState(
				'afterSalesExchange',
				[
					'confirmReceiptLoading'
				]
			),
		},
		watch:{

		},
		mounted(){

		},
		methods:{
			...mapActions(
				'afterSalesExchange',
				[
					'handleConfirmReceipt'
				]
			),

			handleConfirm(){
				let aftersaleId = this.$route.query.aftersaleId
				this.handleConfirmReceipt(aftersaleId)
			},

			handleCancel(){

			}
		}
	}
</script>

<style lang="scss" scoped>
    /deep/ .ivu-modal{
        border: 8px solid rgba(0, 0, 0, .15);
        border-radius: 8px;
        top: 0;
    }

    /deep/ .ivu-modal-mask{
    	background-color: rgba(0, 0, 0, 0.14);
    }

    /deep/ .ivu-modal-wrap{
	    display: flex;
	    justify-content: center;
	    align-items: center;
    }

	/deep/ .ivu-modal-body{
    	padding: 56px 0 0;
    	text-align: center;
	}

	.hint{
		font-size: 24px;
		color: #333333;
	}

    /deep/ .ivu-modal-footer{
        height: 116px;
        line-height: 90px;
        border-top: 0;
        text-align: center;
        button{
            width: 68px;
            height: 32px;
            font-size: 15px;
            border-radius: 0;
            margin-left: 0;
            &:nth-of-type(1){
                background: #F5F5F5;
                color: #333333;
                margin-right: 38px;
                transition: background .5s;
                &:hover{
                    background: #cccccc;
                }
            }
        }
    }
</style>