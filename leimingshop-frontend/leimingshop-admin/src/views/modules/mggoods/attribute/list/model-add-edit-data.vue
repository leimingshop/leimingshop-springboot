<template>
	<el-dialog
	 	    class="model-add-edit-data"
		    :title="title"
		    :close-on-click-modal="false"
		    :visible.sync="visible"
				:before-close="closeDialog">
		    	<el-form
		    		:model="dataForm"
		    		:rules="dataRule"
		    		ref="addForm"
		    		@keyup.enter.native="dataFormSubmit('addForm')"
		    		label-width="120px">
				        <el-form-item label="属性名称：" prop="attrName">
				        	  <el-input v-model="dataForm.attrName" maxlength="60" placeholder="请输入属性名称"></el-input>
				        </el-form-item>
							
				        
				        </el-form-item>
				         <!-- <el-form-item label="排序：" prop="spSort">
				        	 	 <el-input v-model="dataForm.spSort" placeholder="1-255"></el-input>
								</el-select>
				        </el-form-item>
				        <el-form-item label="展示方式：">
		        			    <el-radio v-model="dataForm.attrFormat" label="0">文字</el-radio>
							   		  <el-radio v-model="dataForm.attrFormat" label="1">图片</el-radio>
				        </el-form-item> -->

								<h3 class="attrList">添加属性值：</h3>
				        <!-- <el-form-item label="排序:" prop="attrValueSort">
				        		 <el-input v-model="dataForm.attrValueSort" placeholder="请输入0-255,数字越大排序越靠前"></el-input>
				        </el-form-item> -->
						 <el-form-item label="属性值："  v-for="(item,index) in dataForm.attributeValueDTOList" :prop="'arrtibute'+index" :key="index">
				        	  <el-input v-model="dataForm['arrtibute'+index]" placeholder="属性值"  class="sepcInput" @input="changeArrtibute(index)" @change="changeArrtibute(index)"></el-input>
				       		  <el-button type="danger" plain  @click="removeSpec(index)" v-if="index!=0">删除</el-button>
					    </el-form-item>
					    <br />
						<el-form-item>
							<el-button   @click="appendAttribute()" type="primary" >添加属性值</el-button>
						</el-form-item>
					</el-form>
			    <span slot="footer" class="dialog-footer">
		     		    <el-button @click="dataFormCancel()">返回</el-button>
		     		    <el-button type="primary" @click="dataFormSubmit('addForm')"
		     		    :loading="loading">{{loading ? "提交中···" : "提交"}}</el-button>
			    </span>
	</el-dialog>
</template>
<script>
    // import { livemembertype_add } from '@/api/api2'
	
		import { addAttribute,backScanAttribute,updateAttribute } from '@/api/api'
    // import { isNum,isIntNum} from '@/utils/validate'
    import cloneDeep from 'lodash/cloneDeep'


	export default{
		name: "model-add-edit-data",
		data(){
			 //数字
	        var validateNum = (rule, value, callback) => {
		        if (!isIntNum(value)) {
		          callback(new Error('请输入正确的天数'))
		        } else {
		          callback()
		        }
	        }
			return{
				visible : false,
				loading : false,
				dataForm : {
					attrFormat:"0",
					attrName: "",
					spSort:'',
					attributeValueDTOList:[
						
					],
					// 测试发现如果不在下面添加 input输入不进去（代码动态添加也不行）
					arrtibute0:"",
					arrtibute1:"",
					arrtibute2:"",
					arrtibute3:"",
					arrtibute4:"",
					arrtibute5:"",
					arrtibute6:"",
					arrtibute7:"",
					arrtibute8:"",
					arrtibute9:"",
					arrtibute10:"",
					arrtibute11:"",
					arrtibute12:"",
					arrtibute13:"",
					arrtibute14:"",
					arrtibute15:"",
					arrtibute16:"",
					arrtibute17:"",
					arrtibute18:"",
					arrtibute19:"",
					arrtibute20:"",
				},
				attributeValueDTOItem: {
							"attrValueImage": "",
							"attrValueName":"",
							"attrValueSort": 0
				},
				dataRule : {
			        attrName : [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
			        ],
			        spSort: [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
							],
							attrValueName: [
			          { required: true, message: '必填项不能为空', trigger: 'blur' },
							],
				},
				optionsApplication: [],
				optionsRight: [],
				title:'',
				row:"",
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
				if(row){
					this.title="编辑商品属性";
					this.backScan();
				}else{
					this.title="新建商品属性"
					this.dataForm.attributeValueDTOList = new  Array();
					var attributeValueDTOItem = cloneDeep(this.attributeValueDTOItem);
					this.dataForm.attributeValueDTOList.push(attributeValueDTOItem);
					this.addDataRule();
				}
				this.$nextTick(() => {
						this.$refs['addForm'].resetFields();
						// this.getApplyPullList();
				})
			},
			// 编辑回显
			backScan(){
				  var obj  = {
							id:this.row.id
					}
					backScanAttribute(obj).then((res)=>{
							if(res.code == 200){
									Object.assign(this.dataForm,res.data);
									this.dataForm.attrFormat = res.data.attrFormat+"";
									// 属性单独处理回显
									res.data.attributeValueDTOList.forEach((item,index)=>{
										this.dataForm["arrtibute"+index] = item.attrValueName;
										// this.dataForm.arrtibute0 =this.dataForm.arrtibute0+  123132;
									})
									this.addDataRule();
							}else{

							}
					})
			},
			removeSpec(index){
				 this.dataForm.attributeValueDTOList.splice(index,1);
				 this.dataForm.attributeValueDTOList.forEach((item,index)=>{
						 this.dataForm["arrtibute"+index] = item.attrValueName
				 })		
			},
			// 追加属性
			appendAttribute(){
					// var len = this.dataForm.attributeValueDTOList.length;
					this.dataForm.attributeValueDTOList.push(cloneDeep(this.attributeValueDTOItem));
					this.addDataRule();
			},
			// 改变属性值时
			changeArrtibute(index){
					console.log(this.dataForm);
					this.dataForm.attributeValueDTOList[index].attrValueName = this.dataForm['arrtibute'+index] ;
			},
			// 添加必选项星星
			addDataRule(){
				 	var len = this.dataForm.attributeValueDTOList.length;
					 this.dataForm.attributeValueDTOList.forEach((item,index)=>{
							// if(!this.dataForm["arrtibute"+index]){this.dataForm["arrtibute"+index] = ""}
							 this.dataRule["arrtibute"+index] =  [{ required: true, message: '必填项不能为空', trigger: 'blur' } ]
							 	this.$set(this.dataForm,'arrtibute'+index, this.dataForm['arrtibute'+index]);
					 })
					// this.dataRule[this.dataForm.attributeValueDTOList[len-1].key]=  [{ required: true, message: '必填项不能为空', trigger: 'blur' } ]
					console.log(this.dataRule);
			},
			// 提交
			dataFormSubmit(formName){
				// alert([this.dataForm.name,this.dataForm.domainAddress]);
				// console.log(this.dataForm);
				for(var i=0;i<this.dataForm.attributeValueDTOList.length;i++){
					for(var j=i+1;j<this.dataForm.attributeValueDTOList.length;j++){
							console.log([i,j,this.dataForm.attributeValueDTOList[i].attrValueName,this.dataForm.attributeValueDTOList[j].attrValueName])
							if(this.dataForm.attributeValueDTOList[i].attrValueName == this.dataForm.attributeValueDTOList[j].attrValueName ){
								this.$message({
									message: "属性名字不能有重复",
									type: 'warning',
									duration: 1500,
								})
								return
							}
					}
				}
				this.$refs[formName].validate((valid) => {
						if (valid) {
								this.loading = true;
								var obj={
									// "attrFormat": this.dataForm.attrFormat,//0:text; 1:image ,
									"attrName": this.dataForm.attrName,
									// "spSort": this.dataForm.spSort,
									"attributeValueDTOList": this.dataForm.attributeValueDTOList
								}
								if(this.row) obj.id = this.row.id
								var fn = this.row?updateAttribute:addAttribute;
								fn(obj).then((res) => {
									this.loading = false;
									// alert(JSON.stringify(res));
									let status = null;
									if(res.code == "200"){
										status = "success";
										this.visible = false;
										this.$emit('searchDataList');
					          this.closeDialog();
									}else{
										status = "error";
									}
									this.$message({
													message: res.msg,
													type: status,
													duration: 1500
												})
								})
						} else {
								//console.log('error 添加失败!!');
								return false;
						}
				})
			},
			dataFormCancel(){
					this.visible = false;
					this.closeDialog();
			},
			closeDialog() {
        this.$parent.addEditDataVisible = false;
      },
		},
	}
</script>
<style scoped>
 .attrList{
	 height: 60px;
	 line-height: 90px;
	 border-top: 2px dotted gainsboro;
	 padding-left: 16px;
 }
  .sepcInput{
	width: 60%;
    margin-right: 20px;
 }
 
</style>
