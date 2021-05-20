<template>
  <Modal
    v-model="visible"
    :mask-closable="false"
    width="342"
    class="modal-cancel-apply"
    :loading="applyCancelLoading"
    @on-ok="handleConfirm"
    @on-cancel="handleCancel"
  >
    <div class="add">
      <img
        src="/img/06.personalCenter/warn.png"
        style="height: 20px; width: 22px; margin-right: 7px"
        alt
      />
      <div class="hint">确定取消该售后申请吗？</div>
    </div>
  </Modal>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "modalCancelApply",
  data() {
    return {
      visible: false,
    };
  },
  components: {},
  created() {},
  computed: {
    ...mapState("afterSalesExchange", ["applyCancelLoading"]),
  },
  watch: {},
  mounted() {},
  methods: {
    ...mapActions("afterSalesExchange", ["handleAftersaleApplyCancel"]),

    handleConfirm() {
      let aftersaleId = this.$route.query.aftersaleId;
      this.handleAftersaleApplyCancel(aftersaleId);
    },

    handleCancel() {},
  },
};
</script>

<style lang="scss" scoped>
/deep/ .ivu-modal {
  border: 8px solid rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  top: 0;
}

/deep/ .ivu-modal-mask {
  background-color: rgba(0, 0, 0, 0.14);
}

/deep/ .ivu-modal-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
}

/deep/ .ivu-modal-body {
  padding: 56px 0 0;
  text-align: center;
}

.hint {
  font-size: 16px;
  color: #333333;
}

/deep/ .ivu-modal-footer {
  // height: 116px;
  // line-height: 90px;
  border-top: 0;
  text-align: center;
  button {
    width: 68px;
    height: 32px;
    font-size: 15px;
    border-radius: 0;
    margin-left: 0;
    &:nth-of-type(1) {
      background: #f5f5f5;
      color: #333333;
      border: 1px solid #dddddd;
      margin-right: 28px;
      transition: background 0.5s;
      &:hover {
        background: #cccccc;
      }
    }
  }
}
/deep/ .ivu-modal-close {
  right: 4px;
  top: 4px;
}
/deep/ .ivu-modal-footer button {
  width: 54px !important;
  height: 24px !important;
  font-size: 14px !important;
  border-radius: 2px !important;
  padding: 0 10px !important;
  background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
}

.add {
  display: flex;
  margin: 0px 60px;
}
/deep/ .ivu-modal-footer {
  padding: 47px 18px 31px 18px !important;
}
</style>