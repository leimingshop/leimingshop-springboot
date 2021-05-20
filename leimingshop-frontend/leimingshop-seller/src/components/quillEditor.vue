<template>
  <div>
		  <el-row v-loading="quillUpdateImg">
	    		<div class="edit_container">
		        <quill-editor 
		            v-model="dataForm.messageContent" 
		            ref="myQuillEditor" 
		            :options="quillOption" 
		            @blur="onEditorBlur($event)" 
		            @focus="onEditorFocus($event)"
		            @change="onEditorChange($event)">
		        </quill-editor>
	    	</div>
	   </el-row>
  </div>
</template>
<script>
import mixinViewModule from '@/mixins/view-module'
import { quillEditor } from "vue-quill-editor"; //调用编辑器
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { uploadPicBase64 } from '@/api/api'
export default {
	data () {
	    return {
	    	quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
	      dataForm: {
					messageContent:'',  //消息内容
	      },
	    };
	},
	components: {
    quillEditor
  },
  created () {
  	var that = this;
  	/*富文本编辑图片上传配置*/
		const uploadConfig = {
		    action:  'http://192.168.1.164:9093/admin-api/picture/base64',  // 必填参数 图片上传地址
		    methods: 'POST',  // 必填参数 图片上传方式
		    name: 'img',  // 必填参数 文件的参数名
		    size: 1024*3,  // 可选参数   图片大小，单位为Kb, 1M = 1024Kb
		    accept: 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon'  // 可选 可上传的图片格式
		};
		 
		// toolbar工具栏的工具选项（默认展示全部）
		const toolOptions = [
		    ['bold', 'italic', 'underline', 'strike'],
		    ['blockquote', 'code-block'],
		    [{'header': 1}, {'header': 2}],
		    [{'list': 'ordered'}, {'list': 'bullet'}],
		    [{'script': 'sub'}, {'script': 'super'}],
		    [{'indent': '-1'}, {'indent': '+1'}],
		    [{'direction': 'rtl'}],
		    [{'size': ['small', false, 'large', 'huge']}],
		    [{'header': [1, 2, 3, 4, 5, 6, false]}],
		    [{'color': []}, {'background': []}],
		    [{'font': []}],
		    [{'align': []}],
		    ['clean'],
		    ['link', 'image', 'video']
		];
		const handlers = {
		    image: function image() {
		        var self = this;
		        var fileInput = this.container.querySelector('input.ql-image[type=file]');
		        if (fileInput === null) {
		            fileInput = document.createElement('input');
		            fileInput.setAttribute('type', 'file');
		            // 设置图片参数名
		            if (uploadConfig.name) {
		                fileInput.setAttribute('name', uploadConfig.name);
		            }
		            // 可设置上传图片的格式
		            fileInput.setAttribute('accept', uploadConfig.accept);
		            fileInput.classList.add('ql-image');
		            // 监听选择文件
		            fileInput.addEventListener('change', function () {
		            	that.quillUpdateImg = true;
		            	let reader = new FileReader();
					        let img_base64 = '';
							    reader.readAsDataURL(fileInput.files[0]);
							    reader.onload  = function(event){
						     		img_base64 = this.result
		//				     		console.log(img_base64);
									 const params = { "imgStr": img_base64 };
									 params.type=1
							     	uploadPicBase64(params).then(res =>{
							     		that.quillUpdateImg = false
							     		var lengthcontent = self.quill.getSelection().index;
					            console.log(lengthcontent);
						          console.log(res.data.url);
						          if(res && res.code == "200"){
						            var url = res.data.url
		                    //这里很重要，你图片上传成功后，img的src需要在这里添加，res.path就是你服务器返回的图片链接。        、                        
						            self.quill.insertEmbed(self.quill.getSelection().index, 'image', window.SITE_CONFIG['imgURL'] + res.data.url)
						            self.quill.setSelection(lengthcontent + 1)
						           
						          }else {
									    this.$message({
											message: this.$t(res.msg),
											type: 'error',
											duration: 2500,
											onClose: () => {
											}
										})
								  }
								  fileInput.value = null;
						        })
						     	}
		            });
		            this.container.appendChild(fileInput);
		        }
		        fileInput.click();
		    }
		};
		 
		this.quillOption =  {
		    placeholder: '',
		    theme: 'snow',  // 主题
		    modules: {
		        toolbar: {
		            container: toolOptions,  // 工具栏选项
		            handlers: handlers  // 事件重写
		        }
		    }
		};
  },
  methods: {
  	onEditorReady(editor) { },// 准备编辑器
    init(){
			this.dataForm.messageContent = '';
	  },
    onEditorBlur(){}, // 失去焦点事件
    onEditorFocus(){}, // 获得焦点事件
    onEditorChange(){
    	// let imgurl = window.SITE_CONFIG['imgURL'];
		// 	var re =new RegExp(imgurl ,"g"); 
			// this.$emit('artmessageContent',this.dataForm.messageContent.replace(re,''));
			this.$emit('artmessageContent',this.dataForm.messageContent);
    }, // 内容改变事件

  }
}
</script>
<style lang="scss" scoped>

</style>

