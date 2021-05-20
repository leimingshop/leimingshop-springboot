<template>
	<el-dialog
	 	    class="model-base-fn"
		    title="自定义功能"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
			width="65%"
			:before-close="closeDialog">
		    	



				<div>
					<h3>已选功能</h3>
					<div>
						<div v-for="(item,index) in choosedArr" class="baseFnLevel2" style="color:blue" @click="cancleChoosedItem(index)">
							<svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true">
								<use :xlink:href="`#${item.menuIcon}`" v-if="item.menuIcon"></use>
								<use xlink:href="#icon-home" v-else></use>
							</svg>
							<span>{{item.menuName}}</span>
						</div>
						<div  class="clearBoth"></div>
					</div>

				</div>
				<div style="height:300px;overflow-y: scroll" class="baseFnLevel1">
					<!-- <div v-for="(item,index) in menuList" :key="index" >
					
						<h3>{{item.name}}</h3>
						<div class="baseFnLevel2">
							<div v-for="(item2,index2) in item.children"  :key="index2" class="baseFnLevel1Item">
								<div v-if="!item2.children" class="baseFnLevel2Item" style="">
									<svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true">
										<use :xlink:href="`#${item2.icon}`" v-if="item2.icon"></use>
										<use xlink:href="#icon-home" v-else></use>
									</svg>
									<span>{{item2.name}}</span>
								</div>
								<div  class="baseFnLevel2Item" v-else v-for="(item3,index3) in item2.children" :key="index3">
									<svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true">
										<use :xlink:href="`#${item3.icon}`" v-if="item3.icon"></use>
										<use xlink:href="#icon-home" v-else></use>
									</svg>
									<span>{{item3.name}}</span>
								</div>
								<div class="clearBoth"></div>
							</div>
							<div  class="clearBoth"></div>
						</div>
					
					</div> -->
					<div v-for="(item,index) in myMenuList" class="baseFnLevel1">
						<h3>{{item.menuName}}</h3>
						<div v-for="(item2,index2) in item.children" class="baseFnLevel2" @click="choseItem(item2)" 
						:class="isShowClass(item2)?'choosedClass':''">
							<svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true">
								<use :xlink:href="`#${item2.menuIcon}`" v-if="item2.menuIcon"></use>
								<use xlink:href="#icon-home" v-else></use>
							</svg>
							<span>{{item2.menuName}}</span>
						</div>
						<div  class="clearBoth"></div>
					</div>
				</div>
			    <span slot="footer" class="dialog-footer">
			    	<el-button @click="dataFormCancel()">返回</el-button>
	     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
	     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>
	
	import { checkGoods,saveUserFn} from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'


	export default{
		name: "model-add-edit-data",
		data(){
			return{
				visible : false,
				loading : false,
				dataForm : {
					ids:[],
					goodsStatus:'',//商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布
					rejectReason:'',//拒绝原因
				},
				dataRule : {
			        // goodsStatus : [
			        //   { required: true, message: '必填项不能为空', trigger: 'blur' },
			        // ],
				},
				row:'',
				menuList:[],
				myMenuList:[],
				choosedArr:[],
				menuids:[]
			
			}
		},
		components:{
        },
		computed:{},
		mounted(){},
		methods:{
			init (frenFnOpction) {
			  this.visible = true;
			   this.menuList = window.SITE_CONFIG['menuList']
			   console.log(this.menuList)
			   this.choosedArr = cloneDeep(frenFnOpction);
			   this.handleMenuList();
			},
			handleMenuList(){
					
					this.myMenuList = [];
					this.menuList.forEach((item,index)=>{
						this.myMenuList.push({
							menuName:item.name,
							menuId:item.id,
							menuIcon:item.icon,
							menuUrl:item.url,
							children:[],
						})
						item.children.forEach((item2,index2)=>{
							if(item2.children){
								item2.children.forEach((item3,index3)=>{
									this.myMenuList[this.myMenuList.length-1].children.push(
										{
											menuName:item3.name,
											menuIcon:item3.icon,
											menuId:item3.id,
											menuUrl:item3.url
										}
									)
								})
							}else{
								this.myMenuList[this.myMenuList.length-1].children.push(
									{
										menuName:item2.name,
										menuIcon:item2.icon,
										menuUrl:item2.url,
										menuId:item2.id,
									}
								)
							}
						})
					})
			},
			cancleChoosedItem(index){
					this.choosedArr.splice(index,1);
			},
			isShowClass(itemArgu){
				let _index =  this.choosedArr.findIndex((item,index)=>{
					return item.menuId== itemArgu.menuId
				});
				if(_index==-1){
					return false;
				}else{
					return true;
				}
			},
			// 选择时间
			choseItem(itemArgu){
				if(this.choosedArr.length==8){
					this.$message.error("最多不能超过8个菜单")
					return
				}
				let _index =  this.choosedArr.findIndex((item,index)=>{
					return item.menuId== itemArgu.menuId
				});
				if(_index==-1){
					this.choosedArr.push(itemArgu);			
				}else{
					this.choosedArr.splice(_index,1);
				}
			},
			// 提交
			dataFormSubmit(formName){
				this.$parent.frenFnOpction = cloneDeep(this.choosedArr);
				this.choosedArr.id
				var obj = [];
				this.choosedArr.forEach((item,ibdex)=>{
					obj.push(item.menuId)
				})
				if(obj.length>8){
					this.$message.error("最多不能超过8个菜单")
					return
				}
				saveUserFn(obj).then((res)=>{
					if(res.code==200){
						this.dataFormCancel();
						this.$parent.storeIndex();
					}
				})

				
			},
			dataFormCancel(){
					this.visible = false;
					this.closeDialog();
			},
			closeDialog() {
				this.$parent.baseFnVisible = false;
			},
		},
	}
</script>
<style lang="scss" scoped>
.baseFnLevel2{
	width:70px;
	height:70px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-around;
	float: left;
	margin-left:20px;
	text-align: center;
	span{
		width:70px;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
}
 .choosedClass{
	 color:blue;
 }
 .clearBoth{
	clear:both;
 }
</style>
