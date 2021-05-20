<template>
		<el-dialog
			:visible.sync="visible"
			:title='title'
			:append-to-body="true"
			:before-close='close'
			:close-on-click-modal="false"
  			:close-on-press-escape="false"
			width="83%"
		>
			<div>
				<pictures @GiftUrlHandle="GiftUrlHandle"></pictures>
			</div>
		</el-dialog>
</template>
<script>
import  pictures from "./pictures.vue"
export default {
	name: 'picturesIndex',
    data(){
        return {
			visible:false,
			title:"图片库"
        }
	},
	props:{},
	components:{
		pictures
    },
    created() {
	},
	mounted(){
    },
    methods:{
		init(){
			this.visible = true;
		},
		GiftUrlHandle(url){
			this.$emit("GiftUrlHandle",url);
			this.close();
		},
		close(){
			this.visible = false;
			this.$parent.picturesLibVisible = false;
		}
      
    },
}
</script>
<style lang="scss" scoped>

</style>