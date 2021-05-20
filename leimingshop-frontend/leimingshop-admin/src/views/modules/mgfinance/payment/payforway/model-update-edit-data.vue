<template>
  <div>
    <el-dialog
      class="model-add-edit-data"
      :title="title"
      :close-on-click-modal="false"
      :visible.sync="visible"
      :before-close="closeDialog"
    >
      <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="updateEditData"
        @keyup.enter.native="dataFormSubmit('addForm')"
        label-width="120px"
      >
        <el-form-item label="支付方式：" prop="paymentName">
          <el-input v-model="dataForm.paymentName" placeholder="请输入属性组名" readonly></el-input>
        </el-form-item>
        <el-form-item label="商户公众号APPID：" prop="appId">
          <el-input v-model="dataForm.appId" placeholder="1-255"></el-input>
        </el-form-item>
        <el-form-item label="Appsecret：" prop="secretKey">
          <el-input v-model="dataForm.secretKey" placeholder="1-255"></el-input>
        </el-form-item>
        <el-form-item label="商户号MCHID：" prop="paymentAccount">
          <el-input v-model="dataForm.paymentAccount" placeholder="1-255"></el-input>
        </el-form-item>
        <el-form-item label="商户API密钥：" prop="secretKeyApi">
          <el-input v-model="dataForm.secretKeyApi" placeholder="1-255"></el-input>
        </el-form-item>
        <el-form-item label="状态：" prop="paymentState">
          <el-switch
            v-model="dataForm.paymentState"
            active-color="#13ce66"
            inactive-color="#ff4949"
          ></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="dataFormSubmit('updateEditData')"
          :loading="loading"
        >{{loading ? "保存中" : "保存"}}</el-button>
        <el-button @click="dataFormCancel()">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { paymentEdit, paymentSave } from "@/api/api";
export default {
  name: "model-add-edit-data",
  data() {
    return {
      visible: false,
      loading: false,
      readonly: true,
      dataForm: {
        id: "",
        paymentName: "",
        paymentConfig: "",
        appId: "",
        paymentAccount: "",
        secretKey: "",
        secretKeyApi: "",
        paymentState: false
      },
      dataRule: {
        paymentAccount: [
          { required: true, message: "必填项不能为空", trigger: "blur" }
        ],
        secretKey: [
          { required: true, message: "必填项不能为空", trigger: "blur" }
        ],
        商户号MCHID: [
          { required: true, message: "必填项不能为空", trigger: "blur" }
        ]
      },
      title: "支付方式设置"
    };
  },
  components: {},
  computed: {},
  mounted() {},
  methods: {
    init(id) {
      this.visible = true;
      this.getpaymentDetail();
      //   this.$nextTick(() => {
      //     this.$refs["addForm"].resetFields();
      //   });
    },
    // 获取支付方式详情
    getpaymentDetail() {
      var obj = {
        params: {
          id: this.dataForm.id
        }
      };
      paymentEdit(obj).then(res => {
        if (res.code == 200 && res.data) {
          this.dataForm.id=res.data.id;
          this.dataForm.paymentConfig = JSON.parse(res.data.paymentConfig);
          this.dataForm.appId = JSON.parse(res.data.paymentConfig).appId;
          this.dataForm.paymentAccount = JSON.parse(
            res.data.paymentConfig
          ).paymentAccount;
          this.dataForm.secretKey = "**********";
          this.dataForm.paymentName = res.data.remark;
          this.dataForm.secretKeyApi = "**********";
          this.dataForm.paymentState =
            res.data.paymentState == 0 ? false : true; //0 禁用 1启用
        }
      });
    },
    // 提交
    dataFormSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.loading = true;
         const paymentConfigUpdateDTO = {
            appId: this.dataForm.appId,
            id: this.dataForm.id,
            paymentAccount: this.dataForm.paymentAccount,
            paymentState: this.dataForm.paymentState ? 1 : 0,
            secretKey:
              this.dataForm.secretKey == "**********"
                ? ""
                : this.dataForm.secretKey,
            secretKeyApi:
              this.dataForm.secretKeyApi == "**********"
                ? ""
                : this.dataForm.secretKeyApi
          };
          paymentSave(paymentConfigUpdateDTO).then(res => {
            if (res.code == 200) {
              this.$message({
                message: res.msg,
                type: "success",
                duration: 1500
              });
              this.visible = false;
              this.$emit("searchDataList");
              this.closeDialog();
            } else {
              this.$message({
                message: res.msg,
                type: "warning",
                duration: 1500
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    dataFormCancel() {
      this.visible = false;
      this.closeDialog();
    },
    closeDialog() {
      this.$parent.addEditDataVisible = false;
    }
  }
};
</script>
<style lang="sass" scoped>

</style>
