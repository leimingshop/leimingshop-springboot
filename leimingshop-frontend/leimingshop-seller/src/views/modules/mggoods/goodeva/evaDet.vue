<template>
    <div>
        <Bread :breaddata="detdata" @changePage="changePage" :index="'2'"></Bread>
        <div class="evaBox">
            <div class="title">
                <span class="word">商品评价</span>
                <span>{{evaDetails.storeName }}</span>
                <span>商户id：{{evaDetails.storeId }}</span>
            </div>
            <div class="goods">
                <img class="picSize" :src="$imgDomain +evaDetails.specMainPicture" alt="">
                <!-- <span></span> -->
                <div class="goodsName">
                    <!-- <p>{{evaDetails.goodsName }}</p> -->
                    <span style="color: #4e80db;text-decoration: none; cursor: pointer;"  @click="previewH5Fn(evaDetails)">{{evaDetails.goodsName }}</span>
                    <p>规格:{{evaDetails.specInfo}}</p>
                </div>
            </div>
            <div class="evalist">
                <p class="word">评价</p>
                <div class="buyer">
                    <img :src="$imgDomain+evaDetails.memberAvatar " alt="" class="avator">
                    <div class="infor">
                        <p>
                            <span>名称：{{evaDetails.nickName}}</span>
                            <span>时间：{{evaDetails.createDate}}</span>
                        </p>
                        <p>用户名：{{evaDetails.memberName}}</p>
                    </div>
                </div>
                <div class="content">评价内容：{{evaDetails.evaluateContent }}</div>
                <div class="picList" v-if="evaDetails && evaDetails.evaluateImage">
                    <img
                        class="picEvaSize"
                        :src="$imgDomain+item"
                        alt=""
                        v-for="item in evaDetails.evaluateImage.split(',')"
                    >
                </div>
            </div>
            <div class="request">
                <p class="word">回复内容</p>
                <p class="reCon">{{evaDetails.replyContent }}</p>
            </div>
            <el-button @click="changePage">返回</el-button>
        </div>
         <!-- 预览h5 -->
         <modelPreviewH5 v-if="previewH5Visible" ref="previewH5Compon"></modelPreviewH5>
    </div>
</template>
<script>
import Bread from "@/components/bread";
import modelPreviewH5 from '../modules/model-preview-h5.vue'
import cloneDeep from 'lodash/cloneDeep'
export default {
  data() {
    return {
       previewH5Visible:false,
      detdata: ["订单系统", "售后管理", "商品评价", "评价详情"]
    };
  },
  props: ["evaDetails"],
  methods: {
    changePage() {
      this.$emit("changeState");
    },
     // 预览h5
    previewH5Fn(row){
        this.previewH5Visible = true;
        var  row2 = cloneDeep(row)
        this.$nextTick(()=>{
            row2.specId=row2.goodsSpecId
            this.$refs.previewH5Compon.init(row2)
        })
    },
  },
  components: { Bread,modelPreviewH5 }
};
</script>
<style lang="scss" scoped>
.el-button {
  margin-left: 20%;
}
.picEvaSize {
  width: 180px;
  display: inline-block;
  height: 140px;
}
.picSize {
  width: 100px;
  display: inline-block;
  height: 100px;
}
.evaBox {
  width: 100%;
  padding: 10px 10px 100px 10px;
  height: auto;
  border: 1px solid #e1e1e1;
}
.reCon {
  padding-left: 60px;
}
.word {
  font-weight: 700;
}
.title {
  span {
    display: inline-block;
    margin-right: 50px;
  }
}
.goods {
  margin-top: 10px;
  height: 120px;
  display: flex;
  align-items: center;
  padding-left: 50px;

  .goodsName {
    margin-left: 15px;
    display: flex;
    flex-direction: column;
    height: 100%;
    justify-content: space-between;
  }
}
.evalist {
  width: 100%;
  height: auto;
  .buyer {
    display: flex;
    padding-left: 50px;
    .avator {
      display: inline-block;
      width: 100px;
      height: 100px;
    }
    .infor {
      padding-left: 20px;
      display: flex;
      justify-content: space-between;
      flex-direction: column;
      span {
        margin-right: 50px;
      }
    }
  }
  .content {
    margin-top: 20px;
    padding-left: 50px;
    width: 50%;
    word-wrap: break-word;
  }
  .picList {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    height: auto;
    width: 50%;
    padding: 10px 10px 10px 40px;
    span {
      display: inline-block;
      width: 150px;
      height: 150px;
      margin-right: 10px;
    }
  }
}
</style>
