<template>
  <div class="person" style>
    <div class="personTop" style="display: flex">
      <p class="li" @click="$router.push('/personalData')">基本信息</p>
      <p class="on">兴趣分类</p>
    </div>
    <div>
      <p class="interestText">
        建议多选几个感兴趣的分类，我们会更精确的推荐相关商品哦！
      </p>
      <div>
        <CheckboxGroup v-model="social">
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
          <div class="interest">
            <img :src="'' | filterImgUrl" />
            <Checkbox label="一级分类">
              <span>一级分类</span>
            </Checkbox>
          </div>
        </CheckboxGroup>
        <div class="interestBtn">
          <Button class="Btn">保存</Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  head() {
    return {
      title: "兴趣分类",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
  data() {
    return {
      loading: true,
      modal: false,
      isSelect: true,
    };
  },
  methods: {
    ok() {
      this.$Message.info("修改成功");
    },
    cancel() {
      this.$Message.info("修改失败");
    },
  },
};
</script>
<style lang="scss" scoped>
.person {
  width: 948px;
  height: 1400px;
  background: #ffffff;
}
.personTop {
  display: flex;
  color: #999999;
  background: #cccccc;
  .on {
    width: 120px;
    height: 50px;
    font-size: 18px;
    line-height: 50px;
    background: #ffffff;
    color: #dd2619;
    border-top: 4px solid #dd2619;
    text-align: center;
  }
  .li {
    width: 120px;
    height: 50px;
    font-size: 18px;
    line-height: 50px;
    text-align: center;
    border: 1px solid #999999;
  }
}
.personalContent {
  width: 30%;
  margin: 50px auto 0;
  text-align: center;
  .personalContent-top {
    width: 100px;
    height: 100px;
    margin: 0 auto 15px;
    border-radius: 50%;
  }
}
.personalBtn {
  width: 200px;
  height: 45px;
  margin: 60px 0 0;
  color: #ffffff;
  background: #dd2619;
}
.interestText {
  font-size: 18px;
  color: #999999;
  margin: 100px 0 60px 120px;
}
.interest {
  width: 152px;
  height: 190px;
  margin: 0 24px 10px 0;
  text-align: center;
}
.interestBtn {
  text-align: center;
  .Btn {
    width: 200px;
    height: 44px;
    color: #ffffff;
    background: #dd2619;
  }
}
/deep/ .ivu-checkbox-group {
  margin: 0 95px 100px 146px;
  display: flex;
  flex-wrap: wrap;
}
/deep/ .ivu-form-item-content {
  display: flex;
}
</style>