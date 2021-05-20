<template>
  <el-dialog @close="close()" :visible.sync="visible" :title="auditDataForm.auditCode == 0 ? $t('审核') : $t('审核结果')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '111px'">
      <el-form-item label="文章栏目：" >
        <el-cascader
          v-model="acId"
          :options="acOption"
          @change="caeChange"
          :props="defaultParams"
          change-on-select
          :show-all-levels = "false"
          clearable
          :disabled="true"></el-cascader>
      </el-form-item>
      <el-form-item label="文章标题：" prop="articleTitle">
        <el-input v-model="dataForm.articleTitle" placeholder="文章标题" v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="短标题：" prop="articleShortTitle">
          <el-input v-model="dataForm.articleShortTitle" placeholder="短标题"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="作者：" prop="author">
          <el-input v-model="dataForm.author" placeholder="作者"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="来源：" prop="articleSource">
          <el-input v-model="dataForm.articleSource" placeholder="来源"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="URL 路径：" prop="articleUrl">
        <el-input v-model="dataForm.articleUrl" placeholder="URL 路径"  v-bind:readonly="true"></el-input>文章将链接到此url地址。格式（http://www.leimingtech.com）
      </el-form-item>
       <el-form-item label="引导图：" prop="articlePicture">
          <el-input v-model="dataForm.articlePicture" placeholder="引导图"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="文章内容：" prop="articleContent">
        <quill-editor-img @artmessageContent='artmessageContent'
                          ref="myTextEditor"
                          @focus="onEditorFocus($event)">
        </quill-editor-img>文章内容长度不超过3000个字符
      </el-form-item>
      <el-form-item label="文章摘要：" prop="articleAbstract">
          <el-input v-model="dataForm.articleAbstract" placeholder="文章摘要"  v-bind:readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="优先级排序：" prop="sort">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"  v-bind:disabled="true"></el-input-number> 0-255，数字越小优先级排序越靠前
      </el-form-item>
      <el-form-item label="是否显示：" prop="status">
        <el-radio-group v-model="dataForm.status"  v-bind:disabled="true">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否允许评论：" prop="commentFlag">
      <el-radio-group v-model="dataForm.commentFlag"  v-bind:disabled="true">
        <el-radio :label="1">允许</el-radio>
        <el-radio :label="0">禁止</el-radio>
      </el-radio-group>
      </el-form-item>
        </el-form>


    <el-form :model="auditDataForm" :rules="auditDataRule" ref="auditDataForm" :label-width="$i18n.locale === 'en-US' ? '120px' : '111px'">
      <el-form-item label="审核状态：" prop="audit">
        <el-radio-group v-model="auditDataForm.audit" v-bind:disabled="auditDataForm.auditCode == 0 ? false : true">
          <el-radio :label="1">通过</el-radio>
          <el-radio :label="2">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核结果：" prop="auditResult"
                    :rules="auditDataForm.audit == 1 ? auditDataRule.auditResult:[{ required: true, message: this.$t('validate.required'), trigger: 'blur' }]">
        <el-input v-model="auditDataForm.auditResult" placeholder="审核结果" v-bind:disabled="auditDataForm.auditCode == 0 ? false : true"></el-input>
      </el-form-item>
    </el-form>


    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" v-if="auditDataForm.auditCode == 0" @click="dataFormSubmitHandle()">{{'提交审核'}}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
import quillEditorImg from "@/components/quillEditor"
import {informationInfoUrl,
	saveInformationAuditUrl,
	informationAuditInfoUrl,
	informationClassTreeUrl} from '@/api/url'
export default {
  data () {
    return {
      visible: false,
			acId:[],
			acOption:[],
			defaultParams: {
				label: 'acName',
				value: 'id',
				children: 'children'
			},
      dataForm: {
				id:'',
      },
      auditDataForm: {
				audit: 1,
				auditResult: '',
        auditCode: 0,
      },
    }
  },
	components:{
		quillEditorImg,
	},
  computed: {
		auditDataRule () {
      return {
				  audit: [
				  { required: true, message: this.$t('validate.required'), trigger: 'blur' }
				],
				auditResult: [
          { required: false, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        }
    }
  },
	created() {
		this.visible=true;
		this.init();
	},
  methods: {
		//文章内容长度限制
		validatorValue (rule, value, callback) {
			var value2 = value.replace(/<\/p>/g,"").replace(/<p>/g,"").replace(/&nbsp;/g,"")
			value2 = value2.replace(/ /g,"")
			// value2 = value2.replace("</p>","")
			// value2 .replace("<p>","")
			console.log(value2.length)
			console.log(value2)
			if (value2.length>3000) {
				callback(new Error('模板内容长度限制为3000个字符'))
			}else {
				callback()
			}
		},
    //富文本只读
		onEditorFocus(val,editor){ // 富文本获得焦点时的事件
			editor.enable(false); // 在获取焦点的时候禁用
		},
		//改变acId参数格式
		caeChange(item){
			this.dataForm.acId = item[item.length-1];
			this.dataForm.acId = this.dataForm.acId.toString();
		},
		//文章分类集合
		classList() {
			this.$http.get(informationClassTreeUrl).then((res) => {
				if(res.data.code == 200) {
					// 将获得到的数据赋值给options
					this.acOption=res.data.data;
				}
			});
		},
		close(){
			this.auditDataForm = {};
			this.acId = [];
			this.$refs.myTextEditor.dataForm.messageContent = '';
      this.visible=false;
      this.$parent.informationAuditVisible=false;
      this.$parent.getData();
		},
		init () {
			this.classList();
			this.visible = true
			this.$nextTick(() => {
				this.$refs['dataForm'].resetFields()
				if (this.dataForm.id) {
					this.getInfo()
          if(this.auditDataForm.auditCode == 1){
						this.getAuditInfo()
          }
				}
			})
		},
		// 获取文章信息
		getInfo () {
			this.$http.get(informationInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
				if (res.code !== 200) {
					return this.$message.error(res.msg)
				}
				this.dataForm = {
					...this.dataForm,
					...res.data
				}
				this.$refs.myTextEditor.dataForm.messageContent =this.dataForm.articleContent
				this.acId = res.data.acIdpaths
			}).catch(() => {})
		},
		// 获取文章审核信息
		getAuditInfo () {
			this.$http.get(informationAuditInfoUrl+`/${this.dataForm.id}`).then(({ data: res }) => {
				if (res.code !== 200) {
					return this.$message.error(res.msg)
				}
				this.auditDataForm = {
					...this.auditDataForm,
					...res.data
				}
			}).catch(() => {})
		},
		//富文本内容
		artmessageContent(messageContent){
			this.dataForm.articleContent = messageContent;
		},
		// 提交审核
		dataFormSubmitHandle: debounce(function () {
			this.$refs['auditDataForm'].validate((valid) => {
				if (!valid) {
					return false
				}
				var obj = {};
				obj.articleId = this.dataForm.id;
				obj.audit = this.auditDataForm.audit;
				obj.auditResult = this.auditDataForm.auditResult;
				this.$http.post(saveInformationAuditUrl, obj).then(({ data: res }) => {
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

  }
}
</script>
