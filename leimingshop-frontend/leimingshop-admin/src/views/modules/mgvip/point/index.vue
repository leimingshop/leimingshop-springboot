<template>
  <div v-loading="dataLoding">
    <Bread :breaddata="breaddata"></Bread>
    <div class="formControlArea">
        <div></div>
        <div style="display:flex;">
            <!-- <mainSwitch></mainSwitch> -->
            <mainTipsMessage></mainTipsMessage>
        </div>
    </div>
    <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
        <template slot='title'>
            <div class="iconSize">操作提示：</div>
            <div class="iconSize">1、积分和成长值的设立可促进会员完善相关信息及登录商城购买商品</div>
            <div class="iconSize">2、积分可通过完善信息、购物、评价等操作获得</div>
            <div class="iconSize">3、成长值是用户近12个月在雷铭商城的账户信息、消费金额、活跃互动等方面行为，综合计算出的分值。分值每天更新，每月1日结算最近12个月分值</div>
        </template>
    </el-alert>
    <div class="topMoudel">
      <h3>新手任务<span>&nbsp;&nbsp;新手任务每个用户只奖励一次，相关值必须为大于0的整数</span></h3>
      <div class="topItem">
        <label class="topItemLeft" for="">新手欢迎奖励</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.welcome.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.welcome.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">设置头像</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.avatar.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.avatar.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">设置昵称</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.nikename.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.nikename.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">完善个人信息</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.information.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.information.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">首次关注店铺</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.attentionStore.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.attentionStore.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">首次分享商品</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.sharegoods.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.sharegoods.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">首次收藏商品</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.favoritesgoods.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.favoritesgoods.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">首次购物成功</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.firstOrder.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.firstOrder.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">首次完成评价</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.evaluateOrder.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.noviceTaskSetting.evaluateOrder.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>
    </div>

    <div class="topMoudel">
      <h3>日常购物奖励<span>&nbsp;&nbsp;日常任务奖励多次，相关值必须为大于0的整数</span> </h3>
      <div class="topItem">
        <label class="topItemLeft" for="">每日登录</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.login.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.login.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">每日签到</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.signin.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.signin.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">分享商品</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.shareGoods.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.shareGoods.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>最多奖励次数：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.shareGoods.frequency" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;次</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">邀请好友</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.inviteFriends.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.inviteFriends.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>最多奖励次数：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.inviteFriends.frequency" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;次</span>
          </div>
        </div>
      </div>

      <div class="topItem">
        <label class="topItemLeft" for="">好友首次下单成功</label>
        <div class="topItemRight">
          <div class="rightItem">
            <span>成长值：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.friendOrder.growthValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
          <div class="rightItem">
            <span>积分：</span>
            <el-input-number v-model="dataForm.dailyTaskSetting.friendOrder.pointValue" :step="1" :min="0" :max="1000000"  step-strictly></el-input-number><span>&nbsp;分</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bottomMoudel">
      <h3>奖励领取说明</h3>
    </div>
    <el-tabs type="border-card">
      <el-tab-pane label="积分获取说明">
        <quill-editor-img @artmessageContent='artmessageContent' ref="myTextEditor"></quill-editor-img>
      </el-tab-pane>
      <el-tab-pane label="成长值获取说明">
        <quill-editor-img @artmessageContent='growthContent' ref="growthTextEditor"></quill-editor-img>
      </el-tab-pane>
    </el-tabs>
    <div  class="bottombtWarp">
      <el-button type="primary" @click="dataFormSubmit()" v-if="$hasPermission('sys:reward:save')">{{saveLoading?"保存中":"保存"}}</el-button>
    </div>

  </div>
</template>

<script>
  import quillEditorImg from "@/components/quillEditor"
  import {updatehoppointssettings,getShoppointssettings} from "@/api/api.js"
  import Bread from "@/components/bread";
  export default {
    data() {
      return {
        dataLoding:false,
        saveLoading:false,
        breaddata: ["会员管理", "积分及成长值","奖励设置"],
        dataForm:{
          "dailyTaskSetting": {
            "friendOrder": {
              "growthValue": 0,
              "pointValue": 0
            },
            "inviteFriends": {
              "frequency": 0,
              "growthValue": 0,
              "pointValue": 0
            },
            "login": {
              "growthValue": 0,
              "pointValue": 0
            },
            "shareGoods": {
              "frequency": 0,
              "growthValue": 0,
              "pointValue": 0
            },
            "signin": {
              "growthValue": 0,
              "pointValue": 0
            }
          },
          "noviceTaskSetting": {
            "attentionStore": {
              "growthValue": 0,
              "pointValue": 0
            },
            "welcome": {
              "growthValue": 0,
              "pointValue": 0
            },
            "avatar": {
              "growthValue": 0,
              "pointValue": 0
            },
            "evaluateOrder": {
              "growthValue": 0,
              "pointValue": 0
            },
            "favoritesgoods": {
              "growthValue": 0,
              "pointValue": 0
            },
            "firstOrder": {
              "growthValue": 0,
              "pointValue": 0
            },
            "information": {
              "growthValue": 0,
              "pointValue": 0
            },
            "nikename": {
              "growthValue": 0,
              "pointValue": 0
            },
            "sharegoods": {
              "growthValue": 0,
              "pointValue": 0
            }
          },
          "rewardDescription": {
            "growthDescription": "",
            "pointDescription": ""
          }
        }
      };
    },
    components: {
      quillEditorImg,
      Bread
    },
    created() {
      this.backScan();
    },
    methods: {
      // 积分获取说明
      artmessageContent(messageContent){
        this.dataForm.rewardDescription.pointDescription = messageContent;
      },

      // 成长值获取说明
      growthContent(meContent){
        this.dataForm.rewardDescription.growthDescription = meContent;
      },
      backScan(){
        this.dataLoding=true
        getShoppointssettings().then((res)=>{
          this.dataLoding=false
          if(res.code==200){
            this.dataForm = res.data;
            this.$refs.myTextEditor.dataForm.messageContent = this.dataForm.rewardDescription.pointDescription;
            this.$refs.growthTextEditor.dataForm.messageContent = this.dataForm.rewardDescription.growthDescription;
          }else{
            this.$message.error(res.msg);
          }
        })
      },
      dataFormSubmit(){
        var obj = this.dataForm;
        console.log(obj)
        this.saveLoading = true;
        updatehoppointssettings(obj).then((res)=>{
          this.saveLoading = false;
          if(res.code==200){
            this.$message.success(res.msg);
            this.backScan();
          }else{
            this.$message.error(res.msg);
          }
        })
      }
    }
  };
</script>
<style  lang="scss" scoped>

  .el-dialog {
    width: 40%;
  }
  h3{
    margin-top: 40px;
    span{
      font-weight: 400;
      font-size: 14px;
      color: #CCCCCC;
    }
  }
  // 顶部
  .topMoudel{
    .topItem{
      display: flex;
      .topItemLeft{
        display: inline-block;
        width: 246px;
        height: 50px;
        line-height: 50px;
        text-align: right;
        padding-right: 10px;
        background-color: #f0f2f5;
        border:1px solid #ebebeb;
      }
      .topItemRight{
        width: 994px;
        height: 50px;
        line-height: 50px;
        text-align: left;
        display: flex;
        align-items: center;
        padding-left: 10px;
        border:1px solid #ebebeb;
        justify-content: flex-start;
      }
    }
  }
  .rightItem{
    margin-left: 60px;
  }
  // 中部
  .midMoudel{
    .midItem{
      display: flex;
      .midItemLeft{
        display: inline-block;
        width: 246px;
        height: 50px;
        line-height: 50px;
        text-align: right;
        padding-right: 10px;
        background-color: #f0f2f5;
        border:1px solid #ebebeb;
      }
      .midItemRight{
        width: 794px;
        height: 50px;
        line-height: 50px;
        text-align: left;
        display: flex;
        align-items: center;
        padding-left: 10px;
        border:1px solid #ebebeb;
      }
    }
  }
  .bottombtWarp{
    margin: 40px auto 40px;
    text-align: center;

  }
</style>
