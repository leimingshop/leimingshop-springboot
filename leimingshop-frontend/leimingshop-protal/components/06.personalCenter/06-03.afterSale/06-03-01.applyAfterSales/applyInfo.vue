<template>
  <div class="blockWrap applyInfo">
    <h4 v-text="title"></h4>

    <Form
      ref="formValidate"
      :model="formValidate"
      :rules="ruleValidate"
      :label-width="100"
    >
      <FormItem label="服务类型：" prop="aftersaleType" class="aftersale-type">
        <RadioGroup
          v-model="formValidate.aftersaleType"
          type="button"
          size="large"
          @on-change="handleRadioChange"
        >
          <Radio
            v-for="(item, index) in aftersaleTypeList"
            :key="item.type"
            :label="item.type"
            :class="{ active: formValidate.aftersaleType == index }"
            >{{ item.title }}</Radio
          >
        </RadioGroup>
      </FormItem>

      <FormItem label="提交数量：" prop="applyNum">
        <div class="apply-number">
          <counter
            :min="1"
            :max="applyNumMax"
            v-model="formValidate.applyNum"
          ></counter>
          <!--           <img
            :class="{limitValue: formValidate.applyNum == 1}"
            src="/img/03.goodsClass/03-03.goodsDetail/num_reduce.png"
            alt
            @click="handleApplyNum('subject')"
          />

          <Input
            class="num-input"
            v-model="formValidate.applyNum"
            @on-keyup="handleInputKeyUp"
            @wheel.native.prevent
            placeholder="0"
            style="width: 52px"
          />

          <img
            :class="{limitValue: formValidate.applyNum == applyNumMax}"
            src="/img/03.goodsClass/03-03.goodsDetail/num_increase.png"
            alt
            @click="handleApplyNum('add')"
          />
 -->
          <p class="hint">
            您最多可提交数量为
            <span v-text="applyNumMax"></span>个
          </p>
        </div>
      </FormItem>

      <FormItem label="申请原因：" prop="aftersaleReasonId">
        <Select
          v-model="formValidate.aftersaleReasonId"
          placeholder="请选择原因"
          :loading="applyReasonLoading"
          :label-in-value="true"
          @on-change="handleSelectChange"
        >
          <Option
            v-for="(item, index) in applyReasonList"
            :key="index"
            :value="item.id"
            >{{ item.content }}</Option
          >
        </Select>
      </FormItem>

      <FormItem label="问题描述：" prop="aftersaleExplain">
        <Input
          class="text-content-box"
          v-model="formValidate.aftersaleExplain"
          type="textarea"
          :autosize="{ minRows: 8, maxRows: 15 }"
          maxlength="500"
          show-word-limit
          placeholder="请输入问题描述，不得少于10个字"
          style="width: 526px"
        />
      </FormItem>

      <FormItem label="上传凭证：" prop="aftersalePics" class="upload-wrap">
        <div class="upload-content">
          <upload-file
            :maxSize="maxSize"
            :limit="5"
            v-model="aftersalePicList"
          ></upload-file>

          <input type="hidden" v-model="formValidate.aftersalePics" />
        </div>

        <p class="hint-1" v-text="hint1"></p>
        <p class="hint-2" v-text="hint2"></p>
      </FormItem>
    </Form>
  </div>
</template>

<script>
import counter from "@/components/common/counter";
import uploadFile from "@/components/common/uploadFile";
import { afterApplySave } from "@/api/api_06-03.afterSale";
import { clearLoginInfo } from "@/utils";

export default {
  name: "applyInfo",

  components: {
    uploadFile,
    counter,
  },

  props: {
    applyNumMax: {
      type: Number,
      default: 0,
    },

    applyReasonLoading: {
      type: Boolean,
      default: false,
    },

    applyReasonList: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      title: "申请信息",

      aftersaleTypeList: [
        { title: "退货", type: "0" },
        { title: "换货", type: "1" },
      ],

      formValidate: {
        aftersaleType: "0",
        applyNum: 1,
        aftersaleReasonId: "",
        aftersaleReason: "",
        aftersaleExplain: "",
        aftersalePics: "",
      },

      ruleValidate: {
        aftersaleType: [
          { required: true, message: "请选择一个类型", trigger: "change" },
        ],

        applyNum: [
          {
            type: "number",
            required: true,
            message: "请输入数量",
            trigger: "blur",
          },
        ],

        aftersaleReasonId: [
          { required: true, message: "请选择", trigger: "change" },
        ],

        aftersaleExplain: [
          { required: true, message: "请输入问题描述", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value.length < 10)
                callback(new Error("问题描述不得少于10个字"));
              else callback();
            },
            trigger: "blur",
          },
        ],

        aftersalePics: [
          { required: true, message: "请上传凭证", trigger: "change" },
          {
            validator: (rule, value, callback) => {
              if (value == "") callback(new Error("请上传凭证"));
              else callback();
            },
            trigger: "blur",
          },
        ],
      },

      hint1: "为了帮助您更好的解决问题，请上传图片",
      aftersalePicList: [],
      maxSize: 5,
    };
  },

  computed: {
    hint2() {
      return `最多可上传${this.maxSize}张图片，每张图片大小不超过5M，支持bmp,gif,jpg,png,jpeg格式文件`;
    },

    aftersalePics() {
      if (this.aftersalePicList.length == 0) return "";

      return this.aftersalePicList
        .map((item, index) => item.url.replace(this.$imgUrl, ""))
        .join();
    },
  },

  watch: {
    $route: {
      immediate: true,
      handler(newVal, oldVal) {
        this.$emit("HANDLE_RESET_PICLIST");
      },
    },

    "formValidate.aftersaleType": {
      immediate: true,
      handler(newVal, oldVal) {
        // 获取售后原因下拉选项
        this.$emit("handleOrderApplyReason", { type: newVal });
      },
    },

    aftersalePicList: {
      immediate: false,
      handler(newVal, oldVal) {
        // 处理售后图片列表
        let temp = newVal
          .map((item, index) => item.url.replace(this.$imgUrl, ""))
          .join();
        this.$set(this.formValidate, "aftersalePics", temp);
      },
    },

    aftersalePics: {
      immediate: false,
      handler(newVal, oldVal) {
        // 校验表单字段
        this.$refs["formValidate"].validateField("aftersalePics");
      },
    },
  },

  mounted() {},

  methods: {
    // 退货 or 换货
    handleRadioChange(val) {
      this.$emit("handleRadioChange", val);
    },

    // 售后申请原因 选择
    handleSelectChange(obj) {
      if (!obj) return; // 切换退换货 obj = undefined
      this.$set(this.formValidate, "aftersaleReason", obj.label);
    },

    // 校验表单
    async handleValidate() {
      const res = await this.$refs["formValidate"].validate();

      if (!res) {
        setTimeout(() => {
          var isError = document.getElementsByClassName("ivu-form-item-error");
          if (isError[0].querySelector("input")) {
            isError[0].querySelector("input").focus();
          }
        }, 1);
        this.$Message.warning("请完善申请内容");
        return false;
      } else {
        return true;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
$primary-color: #dd2619;

/deep/ .ivu-form-item-label {
  height: 42px;
  line-height: 22px;
}

.ivu-form-item {
  margin-bottom: 30px;
  /deep/ .text-content-box textarea {
    padding-bottom: 15px;
  }
}

.aftersale-type {
  .ivu-radio-group-item {
    width: 90px;
    height: 34px;
    line-height: 30px;
    margin-right: 10px;
    text-align: center;
    border-radius: 4px;
    border: 1px solid #cccccc;
    font-size: 14px;
    color: #666666;
    transition: all 0.3s;
    &:hover,
    &.active {
      background: linear-gradient(
        270deg,
        rgba(253, 76, 5, 1) 0%,
        rgba(222, 42, 27, 1) 100%
      );
      color: #ffffff;
      border-color: $primary-color;
    }

    &:after,
    &:before {
      display: none !important;
    }
  }
}
.apply-number {
  color: #333;
  display: flex;
  align-items: center;

  /deep/ .counter {
    transform: scale(0.9);
    margin-left: -6px;
  }

  .hint {
    font-size: 12px;
    color: #666666;
    height: 36px;
    line-height: 54px;
    padding-left: 15px;
  }
}

/deep/ .ivu-select {
  width: 248px;
  height: 42px;
  /deep/ .ivu-select-selection {
    height: 100%;
  }

  /deep/ .ivu-select-placeholder {
    height: 42px;
    line-height: 40px;
    font-size: 12px !important;
    &:hover{
      border-color: #dd2619 !important;
      box-shadow: none !important;
    }
  }

  /deep/ .ivu-select-selected-value {
    height: 100%;
    line-height: 34px;
    font-size: 12px !important;
    color: #333333 !important;
    &:hover{
      border-color: #dd2619 !important;
    }
  }
}
/deep/ .ivu-select-visible .ivu-select-selection{
  border-color: #dd2619 !important;
  box-shadow: none !important;
}

/deep/ .ivu-form-item .text-content-box textarea {
  border-radius: 0px !important;
  box-shadow: none !important;
  &:hover{
    box-shadow: none !important;
    border-color: #dd2619 !important;
  }
}
/deep/ .ivu-input {
  padding: 22px 20px !important;
  &:hover{
    border-color: #dd2619 !important;
    box-shadow: none !important;
  }
}
/deep/ .ivu-input-word-count {
  bottom: 10px !important;
  right: 10px !important;
}
/deep/ textarea.ivu-input {
  font-size: 12px !important;
}
/deep/ .hint-1 {
  height: 14px !important;
  line-height: 14px !important;
  font-size: 14px !important;
  font-family: PingFangSC-Regular, PingFang SC !important;
  font-weight: 400 !important;
  color: #666666 !important;
  margin-top: 20px !important;
}
/deep/ .hint-2 {
  height: 15px !important;
  line-height: 15px !important;
  font-size: 12px !important;
  font-family: PingFangSC-Regular, PingFang SC !important;
  font-weight: 400 !important;
  color: #999999 !important;
  margin-top: 4px !important;
}


/deep/ .ivu-upload-drag{
  &:hover{
    border-color: #dd2619 !important;
  }
}
/deep/  .ivu-select-selection{
    &:hover{
      border-color: #dd2619 !important;
    }
}
.confirm-submit-wrap button{
  &:hover{
    border-color: none !important;
    box-shadow: none !important;
  }
}
</style>