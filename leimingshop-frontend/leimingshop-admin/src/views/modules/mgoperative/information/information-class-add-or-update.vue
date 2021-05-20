<template>
    <el-dialog @close="close()" :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '100px'">
            <el-form-item label="分类名：" prop="acName">
                <el-input v-model="dataForm.acName" placeholder="分类名" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item v-if="!dataForm.id" label="上级分类：">
                <el-cascader
                  v-model="acParentId"
                  :options="acOption"
                  @change="caeChange"
                  :props="defaultParams"
                  change-on-select
                  :show-all-levels = "false"
                  clearable></el-cascader>不选则为一级分类<span v-if="tips" style="color:red;">,该分类已禁用！</span>
            </el-form-item>
            <el-form-item label="优先级排序：" prop="sort">
                <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number> 0-255，数字越小排序越靠前
            </el-form-item>
            <el-form-item prop="status" label="是否启用：" size="mini">
                <el-radio-group v-model="dataForm.status">
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
        <template slot="footer">
            <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
        </template>
    </el-dialog>
</template>

<script>
    import debounce from 'lodash/debounce'
		import {informationClassInfoUrl,
			saveInformationClassUrl,
			updateInformationClassUrl,
			informationClassTreeSecondLevelUrl} from '@/api/url'
    export default {
        data () {
            return {
							tips:false,
							visible: false,
							acParentId:[],
							acOption:[],
							defaultParams: {
								label: 'acName',
								value: 'id',
								children: 'children'
							},
							dataForm: {
								id: '',
								acName: '',
								sort: '',
								acParentId: 0,
								acLevel: 1,
								acIdpaths: '0',
								acCode: 2,
                status:1,
							}
            }
        },
        computed: {
            dataRule () {
                return {
                    acName: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    sort: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                    status: [
                        { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                    ],
                }
            }
        },
        methods: {
					init () {
						this.classList();
						this.visible = true
						this.$nextTick(() => {
							this.$refs['dataForm'].resetFields()
							if (this.dataForm.id) {
								this.getInfo()
							}
						})
					},
					//改变acId参数格式
					caeChange(item){
					  console.log('============',item)
					  this.acOption.map((items)=>{
						  if(items.id == item&&items.status==0){
							  this.tips = true;
						  }
						  if(items.id == item&&items.status==1){
							  this.tips = false;
						  }
					  })

						this.dataForm.acParentId = item[item.length-1];
						this.dataForm.acParentId = this.dataForm.acParentId.toString();
					},
					close(){
						this.acParentId = [];
						this.dataForm.acParentId = 0;
					},
					// 获取信息
					getInfo () {
						this.$http.get(informationClassInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
							if (res.code !== 200) {
								return this.$message.error(res.msg)
							}
							this.dataForm = {
								...this.dataForm,
								...res.data
							}
						}).catch(() => {})
					},
					//文章分类集合
					classList() {
						this.$http.get(informationClassTreeSecondLevelUrl).then((res) => {
							if(res.data.code == 200) {
								// 将获得到的数据赋值给options
								this.acOption=res.data.data;
							}
						});
					},
					// 表单提交
					dataFormSubmitHandle: debounce(function () {
						this.$refs['dataForm'].validate((valid) => {
							if (!valid) {
								return false
							}
							this.$http[!this.dataForm.id ? 'post' : 'put'](!this.dataForm.id ? saveInformationClassUrl : updateInformationClassUrl, this.dataForm).then(({ data: res }) => {
								if (res.code !== 200) {
									return this.$message.error(res.msg)
								}
								this.$message({
									message: this.$t('prompt.success'),
									type: 'success',
									duration: 500,
									onClose: () => {
										this.visible = false
										this.$emit('refreshDataList')
									}
								})
							}).catch(() => {})
						})
					}, 1000, { 'leading': true, 'trailing': false })
        },
    }
</script>
