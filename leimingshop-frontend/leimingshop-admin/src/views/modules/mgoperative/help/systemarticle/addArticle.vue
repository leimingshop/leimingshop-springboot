<template>
  <div class="article">
    <Bread :breaddata="isEdit?breaddata:breaddatas" @changePage="changePage" :index="'2'"></Bread>
    <el-form :model="dataForm" ref="dataForm" :rules="rules" :inline="true" label-width="140px">
      <el-form-item label="文章标题：" prop="docTitle">
        <el-input class='artcontent' v-model="dataForm.docTitle" placeholder="请输入系统文章标题" show-word-limit maxlength=20
                  clearable/>
      </el-form-item>
      <el-form-item label="识别码：" prop="docCode">
        <el-input class='artcontent' v-model="dataForm.docCode" placeholder="请输入识别码" :disabled="isEdit ? true : false"
                  onkeyUp="value=value.replace(/[\W]/g,'')"></el-input>
      </el-form-item>
      <el-form-item class="msg1" label="信息内容：" prop="docContent">
        <quill-editor-img class="msg2" @artmessageContent='artdocContent' ref="refdocContent"></quill-editor-img>
      </el-form-item>
      <br/>
      <el-form-item>
        <div class="btnSub">
          <el-button @click="changePage">{{$t('cancel')}}</el-button>
          <el-button class="btn" type="primary" @click="submitForm()" :loading="buttonStatus">{{$t('confirm')}}
          </el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import Bread from '@/components/bread'
  import { quillEditor } from 'vue-quill-editor' //调用编辑器
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  import quillEditorImg from '@/components/quillEditor'
  import { documentAdd, documentID } from '@/api/api'
  import { document, documentDelete, documentPage } from '@/api/api'
  import list from './list'
  import { isURL } from '@/utils/validate'

  export default {
    data () {
      return {
        isEdit: true,
        otherObj: '',
        breaddata: ['运营管理', '帮助中心', '系统文章', '编辑系统文章'],
        breaddatas: ['运营管理', '帮助中心', '系统文章', '新增系统文章'],
        buttonStatus: false,
        content: '', //富文本内容
        quillUpdateImg: false, //根据图片上传状态来确定是否显示loading动画，刚开始是false，不显示
        acId: null,
        dataForm: {
          docTitle: '', //文章标题
          docCode: '',//识别码
          docContent: '', //文章内容

        },
        rules: {
          docTitle: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
          docCode: [{ required: true, message: '请输入识别码', trigger: 'blur' }],
          docContent: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
        },
      }
    },
    components: {
      quillEditor,
      quillEditorImg,
      list,
      Bread,
    },
    created () {

    },
    methods: {
      init (id) {
        this.acId = id
        // 编辑
        if (id) {
          this.isEdit = true
          this.getDocument(id)

        } else { // 新增
          this.isEdit = false
        }
      },
      //编辑获取数据
      getDocument (id) {
        var params = {
          id: id
        }
        documentID(params).then(res => {
          if (res.code == 200) {
            this.dataForm = res.data
            this.$nextTick(() => {
              this.$refs.refdocContent.dataForm.messageContent = this.dataForm.docContent
            })

          } else {
            this.$message({
              message: res.msg,
              type: 'error',
              duration: 1500
            })
          }

        })
      },
      artdocContent (docContent) {
        this.dataForm.docContent = docContent
      },
      //提交保存
      submitForm () {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            this.buttonStatus = true
            if (this.isEdit) {
              var data = this.dataForm
              var datas = {
                docTitle: data.docTitle,
                id: data.id,
                // cCode:data.docCode,
                docContent: data.docContent
              }
              document(datas).then(res => {
                if (res.code == 200) {
                  this.changePage()
                  this.$message({
                    message: res.msg,
                    type: 'success',
                    duration: 1500
                  }),
                    this.getData()
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'error',
                    duration: 1500
                  })
                }
                this.buttonStatus = false
              })
            } else {
              documentAdd(this.dataForm).then((res) => {
                if (res.code == 200) {
                  this.changePage()
                  this.$message({
                    message: res.msg,
                    type: 'success',
                    duration: 1500,
                  }),
                    this.getData()
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'error',
                    duration: 1500,
                  })
                }
                this.buttonStatus = false
              })
            }
          }
        })

      },
      //删除系统文章
      documentDelete () {
        this.dataForm.docTitle = ''
        this.dataForm.docCode = ''
        this.dataForm.docContent = ''
      },
      //返回上级
      changePage () {
        this.$emit('addArticleList')
      },
    }
  }
</script>
<style lang="scss" scoped>
  .artcontent {
    width: 500px;
  }

  .article {
    .el-input {
      width: 240px !important;
    }

    .el-form {
      width: 1100px;
      margin: 0 auto;
    }

    .btnSub {
      margin-top: 30px;
      padding: 0 180px;
    }
    .msg1{
      margin-top: 35px;
    }
    .msg2{
      margin-left: 139px;
      margin-top: -30px;
    }
  }
</style>
