<template>
  <el-dialog @close="close()" :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" :label-width="$i18n.locale === 'en-US' ? '120px' : '111px'">
      <el-form-item label="文章标题：" prop="articleTitle">
        <el-input v-model="dataForm.articleTitle" placeholder="文章标题" maxlength="30" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="文章栏目：" prop="acId">
        <el-cascader
          v-model="acId"
          :options="acOption"
          @change="caeChange"
          :props="defaultParams"
          change-on-select
          :show-all-levels = "false"
          clearable></el-cascader>发布文章只能选择第三级
      </el-form-item>
      <el-form-item label="短标题：" prop="articleShortTitle">
          <el-input v-model="dataForm.articleShortTitle" placeholder="短标题" maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="作者：" prop="author">
          <el-input v-model="dataForm.author" placeholder="作者" maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="来源：" prop="articleSource">
          <el-input v-model="dataForm.articleSource" placeholder="来源" maxlength="30" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="URL路径：" prop="articleUrl" v-if="urlStatus">
        <el-input v-model="dataForm.articleUrl" placeholder="URL 路径"></el-input>文章将链接到此url地址。格式（http://www.leimingtech.com）
      </el-form-item>
       <el-form-item label="引导图：" prop="articlePicture">
          <el-input v-model="dataForm.articlePicture" placeholder="引导图"></el-input>
      </el-form-item>
      <el-form-item label="文章内容：" prop="articleContent" v-if="articleStatus">
        <quill-editor-img @artmessageContent='artmessageContent' ref="myTextEditor"></quill-editor-img>文章内容长度不超过3000个字符
      </el-form-item>
      <el-form-item label="文章摘要：" prop="articleAbstract">
          <el-input v-model="dataForm.articleAbstract" placeholder="文章摘要"></el-input>
      </el-form-item>
      <el-form-item label="优先级排序：" prop="sort">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :max="255" class="floorInput"></el-input-number> 0-255，数字越小优先级排序越靠前
      </el-form-item>
      <el-form-item label="是否显示：" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否允许评论：" prop="commentFlag">
      <el-radio-group v-model="dataForm.commentFlag">
        <el-radio :label="1">允许</el-radio>
        <el-radio :label="0">禁止</el-radio>
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
import quillEditorImg from "@/components/quillEditor"
import {informationInfoUrl,
	saveInformationUrl,
	updateInformationUrl,
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
      acLevel:'',
      dataForm: {
				id:'',
				acId: [],
        articleTitle: '',
        articleShortTitle: '',
        author: '',
        articleSource: '',
        articleUrl: '',
        articlePicture: '',
        articleContent: '',
        articleAbstract: '',
        sort: '',
        status: 1,
        commentFlag: 1,
				acCode: 2,
        articleBelong:'系统平台',

      },
      articleStatus:true,
      urlStatus:true,

    }
  },
	components:{
		quillEditorImg,
	},
  watch:{
    'dataForm.articleUrl'(newval,oldval){
      if(newval){
        this.articleStatus = false;
        this.dataForm.articleContent = '';
      }else{
        this.articleStatus = true;
      }
    },
    // 'dataForm.articleContent'(newval,oldval){
    //   if(newval){
    //     this.urlStatus = false;
    //   }else{
    //     this.urlStatus = true;
    //   }
    // },
  },
  computed: {
    dataRule () {
      return {
          acId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          articleTitle: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          author: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          articleSource: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          articleContent: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },{ validator: this.validatorValue, trigger: 'blur'}
        ],
          articleAbstract: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          status: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
          commentFlag: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        }
    }
  },
	created() {

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
		//改变acId参数格式
		caeChange(item){
			this.dataForm.acId = item[item.length-1];
      this.dataForm.acId = this.dataForm.acId.toString();
      let newId = this.dataForm.acId;
      let a,b,c;
      if(this.acOption&&this.acOption.length!=0){
        for(a=0;a<this.acOption.length;a++){
          if(newId == this.acOption[a].id){
            this.acLevel = this.acOption[a].acLevel;
          }else{
            if(this.acOption[a].children&&this.acOption[a].children.length!=0){
              for(b=0;b<this.acOption[a].children.length;b++){
                if(newId == this.acOption[a].children[b].id){
                  this.acLevel = this.acOption[a].children[b].acLevel;
                }else{
                  if(this.acOption[a].children[b].children&&this.acOption[a].children[b].children.length!=0){
                    for(c=0;c<this.acOption[a].children[b].children.length;c++){
                      if(newId == this.acOption[a].children[b].children[c].id){
                        this.acLevel = this.acOption[a].children[b].children[c].acLevel;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        console.log('=========',this.acLevel)
      }
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
			this.acId = [];
      this.$refs.myTextEditor.dataForm.messageContent = '';
      this.$parent.addOrUpdateVisible=false;
		},
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
		// 获取信息
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
        this.acId = res.data.acIdpaths;
        this.acLevel = 3;
			}).catch(() => {})
		},
		//富文本内容
		artmessageContent(messageContent){
      console.log('=================')
      this.dataForm.articleContent = messageContent;
      if(this.dataForm.articleContent){
        this.urlStatus = false;
        this.data.articleUrl = '';
      }else{
        this.urlStatus = true;
      }
		},
		// 表单提交
		dataFormSubmitHandle: debounce(function () {
			this.$refs['dataForm'].validate((valid) => {
				if (!valid) {
					return false
        }
        if(!this.acLevel||this.acLevel<3){
          this.$message.error('发布文章只能选择三级')
          return false;
        }
				this.$http[!this.dataForm.id ? 'post' : 'put'](!this.dataForm.id ? saveInformationUrl : updateInformationUrl, this.dataForm).then(({ data: res }) => {
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
