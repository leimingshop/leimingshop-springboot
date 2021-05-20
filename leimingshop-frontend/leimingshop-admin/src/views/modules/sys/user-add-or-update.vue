<template>
  <el-dialog
    :visible.sync="visible"
    :title="!pageId ? $t('add') : $t('update')"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form
      v-loading="loading"
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmitHandle()"
      label-width="120px"
    >
      <el-form-item prop="username" :label="$t('user.username') + '：'">
        <el-input v-model="dataForm.username" :placeholder="$t('user.username')" maxlength="20"></el-input>
      </el-form-item>
      <!-- <el-form-item prop="deptName" :label="$t('user.deptName') + '：'" class="dept-list">
        <el-popover v-model="deptListVisible" ref="deptListPopover" placement="bottom-start" trigger="click">
          <el-tree
            :data="deptList"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            ref="deptListTree"
            :highlight-current="true"
            :expand-on-click-node="false"
            accordion
            @current-change="deptListTreeCurrentChangeHandle">
          </el-tree>
        </el-popover>
        <el-input v-model="dataForm.deptName" v-popover:deptListPopover :readonly="true" :placeholder="$t('user.deptName')"></el-input>
      </el-form-item>-->
      <el-form-item
        v-if="!pageId"
        prop="password"
        :label="$t('user.password') + '：'"
        :class="{ 'is-required': !pageId }"
      >
        <el-input
          v-model="dataForm.password"
          type="password"
          placeholder="6-12位,由数字,字母组成"
          maxlength="20"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-if="!pageId"
        prop="comfirmPassword"
        :label="$t('user.comfirmPassword') + '：'"
        :class="{ 'is-required': !pageId }"
      >
        <el-input
          v-model="dataForm.comfirmPassword"
          type="password"
          placeholder="6-12位,由数字,字母组成"
          maxlength="20"
        ></el-input>
      </el-form-item>
      <el-form-item prop="realName" :label="$t('user.realName') + '：'">
        <el-input v-model="dataForm.realName" :placeholder="$t('user.realName')" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item prop="gender" :label="$t('user.gender') + '：'" size="mini">
        <el-radio-group v-model="dataForm.gender">
          <el-radio :label="0">{{ $t('user.gender0') }}</el-radio>
          <el-radio :label="1">{{ $t('user.gender1') }}</el-radio>
          <el-radio :label="2">{{ $t('user.gender2') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="email" :label="$t('user.email') + '：'">
        <el-input v-model="dataForm.email" :placeholder="$t('user.email')"></el-input>
      </el-form-item>
      <el-form-item prop="mobile" :label="$t('user.mobile') + '：'">
        <el-input v-model="dataForm.mobile" :placeholder="$t('user.mobile')"></el-input>
      </el-form-item>
      <el-form-item prop="roleId" :label="$t('user.roleIdList') + '：'" class="role-list">
        <el-select v-model="dataForm.roleId" :placeholder="$t('user.roleIdList')">
          <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="status" :label="$t('user.status') + '：'" size="mini">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">{{ $t('user.status1') }}</el-radio>
          <el-radio :label="0">{{ $t('user.status0') }}</el-radio>
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
import debounce from "lodash/debounce";
import { isEmail, isMobile } from "@/utils/validate";
import JsEncrypt from "jsencrypt";
import security from "@/utils/security.js";
export default {
  data() {
    return {
      loading: false,
      visible: false,
      deptList: [],
      deptListVisible: false,
      pageId: null,
      roleList: [],
      // roleIdListDefault: [],
      dataForm: {
        id: "",
        username: "",
        deptId: "0",
        deptName: "",
        password: "",
        comfirmPassword: "",
        realName: "",
        gender: 0,
        email: "",
        mobile: "",
        roleId: "",
        status: 1
      }
    };
  },
  computed: {
    dataRule() {
      var validateUserName = (rule, value, callback) => {
        if (!/^[a-zA-Z0-9_]{6,20}$/.test(value)) {
          return callback(new Error("用户名必须为6-20位字符，数字，下划线"));
        }
        callback();
      };
      var validatePassword = (rule, value, callback) => {
        if (!this.pageId && !/\S/.test(value)) {
          return callback(new Error(this.$t("validate.required")));
        }
        callback();
      };
      var validateComfirmPassword = (rule, value, callback) => {
        if (!this.pageId && !/\S/.test(value)) {
          return callback(new Error(this.$t("validate.required")));
        }
        if (this.dataForm.password !== value) {
          return callback(new Error(this.$t("user.validate.comfirmPassword")));
        }
        callback();
      };
      var validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          return callback(
            new Error(
              this.$t("validate.format", { attr: this.$t("user.email") })
            )
          );
        }
        callback();
      };
      var validateMobile = (rule, value, callback) => {
        if (!isMobile(value)) {
          return callback(
            new Error(
              this.$t("validate.format", { attr: this.$t("user.mobile") })
            )
          );
        }
        callback();
      };
      return {
        username: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur"
          },
          { validator: validateUserName, trigger: "blur" }
        ],
        deptName: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "change"
          }
        ],
        password: [{ validator: validatePassword, trigger: "blur" }],
        comfirmPassword: [
          { validator: validateComfirmPassword, trigger: "blur" }
        ],
        realName: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur"
          }
        ],
        roleId: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur"
          }
        ],
        email: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur"
          },
          { validator: validateEmail, trigger: "blur" }
        ],
        mobile: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur"
          },
          { validator: validateMobile, trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    init(id) {
      // this.dataForm.id= id;
      // console.log(id)
      this.visible = true;
      if (id) {
        this.pageId = id;
        this.getInfo(id);
      } else {
        this.dataForm.password = "";
        this.pageId = "";
      }
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        // this.roleIdListDefault = []
        this.getRoleList();
      });
    },
    // 获取部门列表
    getDeptList() {
      return this.$http
        .get("/admin-api/dept/list")
        .then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg);
          }
          this.deptList = res.data;
        })
        .catch(() => {});
    },
    // 获取角色列表
    getRoleList() {
      return this.$http
        .get("/admin-api/role/list")
        .then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg);
          }
          this.roleList = res.data;
        })
        .catch(() => {});
    },
    // 获取信息
    getInfo(id) {
      this.loading = true;
      this.$http
        .get(`/admin-api/user/${id}`)
        .then(({ data: res }) => {
          this.loading = false;
          if (res.code !== 200) {
            return this.$message.error(res.msg);
          }
          this.dataForm = {
            ...this.dataForm,
            ...res.data
          };
          // this.$refs.deptListTree.setCurrentKey(this.dataForm.deptId)
          // 角色配置, 区分是否为默认角色
          // for (var i = 0; i < res.data.roleIdList.length; i++) {
          //   if (this.roleList.filter(item => item.id === res.data.roleIdList[i])[0]) {
          //     this.dataForm.roleIdList.push(res.data.roleIdList[i])
          //     continue
          //   }
          //   this.roleIdListDefault.push(res.data.roleIdList[i])
          // }
        })
        .catch(() => {});
    },
    // 所属部门树, 选中
    deptListTreeCurrentChangeHandle(data, node) {
      this.dataForm.deptId = data.id;
      this.dataForm.deptName = data.name;
      this.deptListVisible = false;
    },
    // 表单提交
    dataFormSubmitHandle: debounce(
      function() {
        this.$refs["dataForm"].validate(valid => {
          if (!valid) {
            return false;
          }
          // 增加用户名与密码 RSA加密
          let publicKey = security.publicKey;
          var encrypt = new JSEncrypt();
          encrypt.setPublicKey(publicKey);
          var obj = {};
          Object.assign(obj, this.dataForm);

          this.dataForm.password = encrypt.encrypt(this.dataForm.password);
          this.dataForm.comfirmPassword = encrypt.encrypt(
            this.dataForm.comfirmPassword
          );

          this.$http[!this.pageId ? "post" : "put"]("/admin-api/user", {
            ...this.dataForm
          })
            .then(({ data: res }) => {
              if (res.code !== 200) {
                return this.$message.error(res.msg);
              }
              this.$message({
                message: this.$t("prompt.success"),
                type: "success",
                duration: 500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                }
              });
            })
            .catch(() => {});
        });
      },
      1000,
      { leading: true, trailing: false }
    )
  }
};
</script>

<style lang="scss">
.mod-sys__user {
  .dept-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }
  .role-list {
    .el-select {
      width: 100%;
    }
  }
}
</style>
