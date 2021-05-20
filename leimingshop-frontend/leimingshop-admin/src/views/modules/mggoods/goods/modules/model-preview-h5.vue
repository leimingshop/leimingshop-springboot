<template>
	<el-dialog
	 	    class="model-sku-data"
		    title="商品预览"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
			:before-close="closeDialog"
			width="420px"
			style="text-align: center;"
			>
				<iframe id="Newpage" :src="url" frameborder="0" style="width:375px;height:667px;margin:auto"></iframe>
				<!-- <iframe src="https://www.baidu.com" frameborder="0" style="width:375px;height:667px;margin:auto"></iframe> -->
				
	</el-dialog>
</template>
<script>
    // import { livemembertype_add } from '@/api/api2'
	
	import { addAttribute,backScangoodsSpec,updateGoodsSpecSku } from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'

	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
				
				},
				url:window.SITE_CONFIG['h5Url']+"/#/",//http://152.136.176.161
			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init (row) {
				this.visible = true;
				this.row = row;
				if(this.row && this.row.id){
					if(this.row.specId){
						this.url = window.SITE_CONFIG['h5Url']+"/#/pagesA/product/product?id="+this.row.id+"&specId="+this.row.specId;
					}else{
						this.url = window.SITE_CONFIG['h5Url']+"/#/pagesA/product/product?id="+this.row.id;
					}
				}else{
					this.url =  window.SITE_CONFIG['h5Url']+"/#/";
				}
				var _iframe = document.getElementById('Newpage').contentWindow.document.getElementsByClassName('page-bottom')[0];   //get iframe下的id
         		 _iframe.style.display= "none";  //修改样式、
			},
		
			dataFormCancel(){
                this.visible = false;
                this.closeDialog();
			},
			closeDialog() {
				this.visible = false
				this.$parent.previewH5Visible = false;
            },
		},
	}
</script>
<style scoped>

</style>
