<template>
  <el-dialog
    title="会员编辑"
    :before-close="ai_dialog_close"
    :visible.sync="outerVisible"
    @click="goBack"
    :show-close="false"
    width="30%"
  >
    <el-dialog
      width="30%"
      title="密码重置确认"
      :visible.sync="innerVisible"
      class="restPass"
      append-to-body
    >
      <el-form label-width="100px" :rules="rules" ref="form" :model="form">
        <h3>您确定重置会员[{{memberInfo.memberName}}]的密码吗？</h3>
        <el-form-item label="请输入" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入接受短信的手机号"></el-input>
        </el-form-item>
        <p style="width:60%;">提示：确认后，新密码将通过会员提供的手机号【{{form.phone}}】，以短信的形式通知会员</p>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button plain @click="closeInner">取 消</el-button>
        <el-button type="primary" @click="sendMsg">确认</el-button>
      </div>
    </el-dialog>
    <el-form label-width="100px" :model="memberInfo" ref="memberInfo">
      <el-form-item label="会员名称：">
<!--        <el-input v-model="memberInfo.memberName"></el-input>-->
        <span>{{memberInfo.memberName}}</span>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input v-model="memberInfo.memberPasswd" type="password" :disabled="true">******</el-input>
        <span class="rest" @click="innerVisible = true">重置密码</span>
      </el-form-item>
<!--      <el-form-item label="备注：">-->
<!--        <el-input v-model="form.name"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="地区：">-->
<!--        <el-select-->
<!--          disabled-->
<!--          v-model="dataForm.memberAreaid"-->
<!--          placeholder="请选择"-->
<!--          loading-text="加载中···"-->
<!--          @visible-change="getGoodsClass(0)"-->
<!--        >-->
<!--          &lt;!&ndash; 市级 &ndash;&gt;-->
<!--          <el-option-->
<!--            v-for="item in goodscalssOption0"-->
<!--            :key="item.id"-->
<!--            :label="item.areaName"-->
<!--            :value="item.id"-->
<!--          ></el-option>-->
<!--        </el-select>-->
<!--        <el-select-->
<!--          disabled-->
<!--          v-model="dataForm.memberCityid"-->
<!--          placeholder="请选择"-->
<!--          @visible-change="getGoodsClass(1)"-->
<!--        >-->
<!--          <el-option-->
<!--            v-for="item in goodscalssOption1"-->
<!--            :key="item.id"-->
<!--            :label="item.areaName"-->
<!--            :value="item.id"-->
<!--          ></el-option>-->
<!--        </el-select>-->
<!--        <el-select-->
<!--          disabled-->
<!--          v-model="dataForm.memberProvinceid"-->
<!--          placeholder="请选择"-->
<!--          @visible-change="getGoodsClass(2)"-->
<!--        >-->
<!--          <el-option-->
<!--            v-for="item in goodscalssOption2"-->
<!--            :key="item.id"-->
<!--            :label="item.areaName"-->
<!--            :value="item.id"-->
<!--          ></el-option>-->
<!--        </el-select>-->
<!--        <el-select disabled v-if="dataForm.stressId" v-model="dataForm.stressId" placeholder="请选择">-->
<!--          <el-option-->
<!--            v-for="item in goodscalssOption3"-->
<!--            :key="item.id"-->
<!--            :label="item.areaName"-->
<!--            :value="item.id"-->
<!--          ></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="restPass">保存</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { isMobile } from "@/utils/validate";
import { restPassword, setMemberPass, areaList, proList } from "@/api/api";
export default {
  components: {},
  data() {
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("手机号不能为空"));
      } else {
        const reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
        console.log(reg.test(value));
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error("请输入正确的手机号"));
        }
      }
    };
    return {
      value: [],
      outerVisible: true,
      innerVisible: false,
      dialogVisible: "",
      form: {},
      rules: {
        phone: [{ validator: checkPhone, trigger: "blur" }]
      },
      // dataForm: {
      //   memberAreaid: "",
      //   memberCityid: "",
      //   memberProvinceid: "",
      //   stressId: "",
      // },
      // goodscalssOption0: [],
      // goodscalssOption1: [],
      // goodscalssOption2: [],
      // goodscalssOption3: [],
    };
  },
  props: ["memberInfo", "memberInfoSourse"],
  // computed: {
  //   dataRule () {
  //     // 限制20个汉字,1个汉字相当于2个字符
  //     var validateMemberName = (rule, value, callback) => {
  //                 var chineseCount = 0, characterCount = 0;
  //                 for (let i = 0; i < value.length; i++) {
  //                   if (/^[\u4e00-\u9fa5]*$/.test(value[i])) { //汉字
  //                     chineseCount = chineseCount + 2;
  //                   } else { //字符
  //                     characterCount = characterCount + 1;
  //                   }
  //                   var count = chineseCount + characterCount;
  //             if(count>40){
  //               return callback(new Error("会员名称长度为20位汉字，1个汉字相当于2个字符"));
  //             }
  //                 }
  //                   callback()
  //     };
  //     var validateMemberPasswd = (rule, value, callback) => {
  //       if (!/^[a-zA-Z0-9_]{6,20}$/.test(value)) {
  //         return callback(new Error("用户名必须为6-20位字符，数字，下划线"));
  //       }
  //       callback()
  //     };
  //     return {
  //       // 会员名称
  //       memberName: [
  //         { required: true, message: this.$t('validate.required'), trigger: 'blur' },
  //         { validator: validateMemberName, trigger: "blur" }
  //       ],
  //       // 密码
  //       memberPasswd: [
  //         { required: true, message: this.$t('validate.required'), trigger: 'blur' },
  //         { validator: validateMemberPasswd, trigger: "blur" }
  //       ],
  //     }
  //   }
  // },
  methods: {
    //避免非正常关闭
    ai_dialog_close() {
      this.outerVisible = false;
      this.$emit("changeWindow");
    },
    // init() {
    //   this.getCity("");
    //   this.dataForm.memberAreaid = this.memberInfo.memberProvinceid;
    //   this.dataForm.memberCityid = this.memberInfo.memberCityid;
    //   this.dataForm.memberProvinceid = this.memberInfo.memberAreaid;
    //   this.dataForm.stressId = this.memberInfo.stressId;
    //   this.getAreaList(this.dataForm.memberAreaid);
    //   this.getProList(this.dataForm.memberCityid);
    //   this.getStrList(this.dataForm.memberProvinceid);
    // },
    // getCity() {
    //   //所有一级区域
    //   areaList().then(res => {
    //     if (res.code == 200) {
    //       this.goodscalssOption0 = res.data;
    //     }
    //   });
    // },
    // getAreaList(id) {
    //   //二级
    //   proList({ id }).then(res => {
    //     if (res.code == 200) {
    //       this.goodscalssOption1 = res.data;
    //     }
    //   });
    // },
    // getProList(id) {
    //   //三级
    //   proList({ id }).then(res => {
    //     if (res.code == 200) {
    //       this.goodscalssOption2 = res.data;
    //     }
    //   });
    // },
    // getStrList(id) {
    //   //四级
    //   proList({ id }).then(res => {
    //     if (res.code == 200) {
    //       console.log(res.data, "333");
    //       this.goodscalssOption3 = res.data;
    //     }
    //   });
    // },
    closeInner() {
      this.innerVisible = false;
      this.form.phone = "";
    },
    handleChange(value) {
      console.log(value);
    },
    //取消
    cancel() {
      this.outerVisible = false;
      this.$emit("changeWindow");
      //回传主页面。false
    },
    //返回列表页
    goBack() {
      console.log("关闭");
      this.$emit("changeWindow");
    },
    //重置密码 提交
    restPass() {
        const obj = {
          id: this.memberInfo.id,
          // memberAreaid: this.dataForm.memberAreaid,
          // memberCityid: this.dataForm.memberCityid,
          // memberPasswd: "string",
          // stressId: this.dataForm.stressId,
          // memberProvinceid: this.dataForm.memberProvinceid
        };
        setMemberPass(obj).then(res => {
          if (res.code == 200) {
            this.$message({
              message: res.msg,
              type: "success",
              duration: 1000
            });
            this.$emit("changeWindow");
          } else {
            this.$message({
              message: res.msg,
              type: "error",
              duration: 1000
            });
          }
        });
    },
    //获取密码
    sendMsg() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const obj = {
            id: this.memberInfo.id,
            mobile: this.form.phone
          };
          restPassword(obj).then(res => {
            if (res.code == 200) {
              this.$message({
                message: res.msg,
                type: "success",
                duration: 1000
              });
              this.innerVisible = !this.innerVisible;
            } else {
              this.$message({
                message: res.msg,
                type: "error",
                duration: 1000
              });
            }
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style scoped>
.el-select {
  width: 15%;
}
.header {
  text-align: center;
  font-weight: bold;
}
.el-input {
  width: 50%;
}
.el-dialog {
  width: 40% !important;
}
.rest {
  margin-left: 15px;
  display: inline-block;
  padding: 8px 10px;
  color: dodgerblue;
  cursor:pointer;
}
.dialog-footer {
  width: 91%;
}
/* /deep/.el-dialog {
  width: 30% !important;
} */
.el-form {
  width: 140%;
}
/* .restPass{text-align: center;} */
</style>
