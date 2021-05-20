<template>
  <div>
    <el-dialog
      class="model-add-edit-data"
      title="请选择图片"
      :close-on-click-modal="false"
      :visible.sync="visible"
      :before-close="closeDialog"
      width="1300px"
    >
        <div>
          <div class="imageWarp"  v-loading="dataListLoading">
            <div class="imageItem" v-for="(item,index) in dataList" @click="chooseImg(item)" v-if="item.picturePath">
                <img :src="item.picturePath | filterImgUrl" alt="">
            </div>
          </div>
        </div>
            <el-pagination
             style="display:flex;flex-wrap: wrap;"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle">
          </el-pagination>
    </el-dialog>
  </div>
</template>
<script>

import mixinViewModule from '@/mixins/view-module'

import { getallimages } from "@/api/url"

export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: getallimages,
        activatedIsNeed:false,
        getDataListIsPage: true,
        //   // exportURL: '/admin-api/log/login/export',
        //   deleteURL: deleteAttributeUrl,
        //   deleteIsBatch: true,
        //   deleteIsBatchKey: 'id'
      },
      visible: false,
      loading: false,
      dataForm: {
       
      },
      dataRule: {
        // paymentAccount: [
        //   { required: true, message: "必填项不能为空", trigger: "blur" }
        // ],
       
      },
    };
  },
  components: {},
  computed: {},
  mounted() {},
  methods: {
    init() {
      this.visible = true;
      this.getDataList().then((res)=>{
          console.log(this.dataList);
      })

    },
    chooseImg(item){
      var file ={
        name: item.pictureName,
        src: item.picturePath,
      }
      this.$parent.imgList.push({file})
      this.closeDialog();
    },
    // 提交
    dataFormSubmit(formName) {
        item
    },
    dataFormCancel() {
      this.visible = false;
      this.closeDialog();
    },
    closeDialog() {
      this.$parent.wheelImagesVisible = false;
    }
  }
};
</script>
<style lang="scss" scoped>
.imageWarp{
  min-height: 300px;
  display: flex;
  flex-wrap: wrap;
  .imageItem{
      width:200px;
      height:200px;
      line-height: 198px;
      display: inline-block;
      border: 1px solid #979797;
      margin: 10px 25px;
      overflow: hidden;
      img{
        width:98%;
        height:auto;
      }
  }
}
</style>
