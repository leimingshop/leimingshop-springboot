<template>
  <el-dialog title="会员编辑" :visible.sync="outerVisible" @click="goBack" :show-close="false">
    <el-dialog
      width="30%"
      title="密码重置确认"
      :visible.sync="innerVisible"
      class="restPass"
      append-to-body
    >
      <el-form label-width="100px" :rules="rules" ref="form" :model="form">
        <h3>您确定重置会员[{{memberInfo.nickName}}]的密码吗？</h3>
        <el-form-item label="请输入" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入接受短信的手机号"></el-input>
        </el-form-item>
        <p>提示：确认后，新密码将通过会员提供的手机号【{{form.phone}}】，以短信的形式通知会员</p>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button plain @click="innerVisible =false">取 消</el-button>
        <el-button type="primary" @click="sendMsg">保存</el-button>
      </div>
    </el-dialog>
    <el-form label-width="100px" :model="dataForm">
      <el-form-item label="昵称：">
        <el-input v-model="memberInfo.nickName"></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input v-model="memberInfo.memberPasswd">******</el-input>
        <span class="rest" @click="innerVisible = true">重置密码</span>
      </el-form-item>
      <el-form-item label="备注：">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="地区：">
        <el-select
        disabled
          v-model="dataForm.memberAreaid"
          placeholder="请选择"
          loading-text="加载中···"
          @visible-change="getGoodsClass(0)"
        >
          <!-- 市级 -->
          <el-option
            v-for="item in goodscalssOption0"
            :key="item.id"
            :label="item.areaName"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
        disabled
          v-model="dataForm.memberCityid"
          placeholder="请选择"
          @visible-change="getGoodsClass(1)"
        >
          <el-option
            v-for="item in goodscalssOption1"
            :key="item.id"
            :label="item.areaName"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
        disabled
          v-model="dataForm.memberProvinceid"
          placeholder="请选择"
          @visible-change="getGoodsClass(2)"
        >
          <el-option
            v-for="item in goodscalssOption2"
            :key="item.id"
            :label="item.areaName"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
        disabled
          v-if="dataForm.stressId"
          v-model="dataForm.stressId"
          placeholder="请选择"
        >
          <el-option
            v-for="item in goodscalssOption3"
            :key="item.id"
            :label="item.areaName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="restPass">保存</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { restPassword, setMemberPass, areaList, proList } from "@/api/api";
export default {
  components: {},
  data() {
    return {
      value: [],
      outerVisible: true,
      innerVisible: false,
      dialogVisible: "",
      form: {},
      dataForm: {
        memberAreaid: "",
        memberCityid: "",
        memberProvinceid: "",
        stressId: ""
      },
      goodscalssOption0: [],
      goodscalssOption1: [],
      goodscalssOption2: [],
      goodscalssOption3: [],
      rules: {
        phone: [{ required: true, message: "请输入手机号码", trigger: "blur" }]
      },
      options: [
        {
          value: "zhinan",
          label: "指南",
          children: [
            {
              value: "shejiyuanze",
              label: "设计原则",
              children: [
                {
                  value: "yizhi",
                  label: "一致"
                },
                {
                  value: "fankui",
                  label: "反馈"
                },
                {
                  value: "xiaolv",
                  label: "效率"
                },
                {
                  value: "kekong",
                  label: "可控"
                }
              ]
            },
            {
              value: "daohang",
              label: "导航",
              children: [
                {
                  value: "cexiangdaohang",
                  label: "侧向导航"
                },
                {
                  value: "dingbudaohang",
                  label: "顶部导航"
                }
              ]
            }
          ]
        },
        {
          value: "zujian",
          label: "组件",
          children: [
            {
              value: "basic",
              label: "Basic",
              children: [
                {
                  value: "layout",
                  label: "Layout 布局"
                },
                {
                  value: "color",
                  label: "Color 色彩"
                },
                {
                  value: "typography",
                  label: "Typography 字体"
                },
                {
                  value: "icon",
                  label: "Icon 图标"
                },
                {
                  value: "button",
                  label: "Button 按钮"
                }
              ]
            },
            {
              value: "form",
              label: "Form",
              children: [
                {
                  value: "radio",
                  label: "Radio 单选框"
                },
                {
                  value: "checkbox",
                  label: "Checkbox 多选框"
                },
                {
                  value: "input",
                  label: "Input 输入框"
                },
                {
                  value: "input-number",
                  label: "InputNumber 计数器"
                },
                {
                  value: "select",
                  label: "Select 选择器"
                },
                {
                  value: "cascader",
                  label: "Cascader 级联选择器"
                },
                {
                  value: "switch",
                  label: "Switch 开关"
                },
                {
                  value: "slider",
                  label: "Slider 滑块"
                },
                {
                  value: "time-picker",
                  label: "TimePicker 时间选择器"
                },
                {
                  value: "date-picker",
                  label: "DatePicker 日期选择器"
                },
                {
                  value: "datetime-picker",
                  label: "DateTimePicker 日期时间选择器"
                },
                {
                  value: "upload",
                  label: "Upload 上传"
                },
                {
                  value: "rate",
                  label: "Rate 评分"
                },
                {
                  value: "form",
                  label: "Form 表单"
                }
              ]
            },
            {
              value: "data",
              label: "Data",
              children: [
                {
                  value: "table",
                  label: "Table 表格"
                },
                {
                  value: "tag",
                  label: "Tag 标签"
                },
                {
                  value: "progress",
                  label: "Progress 进度条"
                },
                {
                  value: "tree",
                  label: "Tree 树形控件"
                },
                {
                  value: "pagination",
                  label: "Pagination 分页"
                },
                {
                  value: "badge",
                  label: "Badge 标记"
                }
              ]
            },
            {
              value: "notice",
              label: "Notice",
              children: [
                {
                  value: "alert",
                  label: "Alert 警告"
                },
                {
                  value: "loading",
                  label: "Loading 加载"
                },
                {
                  value: "message",
                  label: "Message 消息提示"
                },
                {
                  value: "message-box",
                  label: "MessageBox 弹框"
                },
                {
                  value: "notification",
                  label: "Notification 通知"
                }
              ]
            },
            {
              value: "navigation",
              label: "Navigation",
              children: [
                {
                  value: "menu",
                  label: "NavMenu 导航菜单"
                },
                {
                  value: "tabs",
                  label: "Tabs 标签页"
                },
                {
                  value: "breadcrumb",
                  label: "Breadcrumb 面包屑"
                },
                {
                  value: "dropdown",
                  label: "Dropdown 下拉菜单"
                },
                {
                  value: "steps",
                  label: "Steps 步骤条"
                }
              ]
            },
            {
              value: "others",
              label: "Others",
              children: [
                {
                  value: "dialog",
                  label: "Dialog 对话框"
                },
                {
                  value: "tooltip",
                  label: "Tooltip 文字提示"
                },
                {
                  value: "popover",
                  label: "Popover 弹出框"
                },
                {
                  value: "card",
                  label: "Card 卡片"
                },
                {
                  value: "carousel",
                  label: "Carousel 走马灯"
                },
                {
                  value: "collapse",
                  label: "Collapse 折叠面板"
                }
              ]
            }
          ]
        },
        {
          value: "ziyuan",
          label: "资源",
          children: [
            {
              value: "axure",
              label: "Axure Components"
            },
            {
              value: "sketch",
              label: "Sketch Templates"
            },
            {
              value: "jiaohu",
              label: "组件交互文档"
            }
          ]
        }
      ]
    };
  },
  props: ["memberInfo", "memberInfoSourse"],
  methods: {
    init() {
      this.getCity("");
      this.dataForm.memberAreaid = this.memberInfo.memberProvinceid;
      this.dataForm.memberCityid = this.memberInfo.memberCityid;
      this.dataForm.memberProvinceid = this.memberInfo.memberAreaid;
      this.dataForm.stressId = this.memberInfo.stressId;
      this.getAreaList(this.dataForm.memberAreaid);
      this.getProList(this.dataForm.memberCityid);
      this.getStrList(this.dataForm.memberProvinceid);
    },
    getCity() {
      //所有一级区域
      areaList().then(res => {
        if (res.code == 200) {
          this.goodscalssOption0 = res.data;
        }
      });
    },
    getAreaList(id) {
      //二级
      proList({ id }).then(res => {
        if (res.code == 200) {
          this.goodscalssOption1 = res.data;
        }
      });
    },
    getProList(id) {
      //三级
      proList({ id }).then(res => {
        if (res.code == 200) {
          this.goodscalssOption2 = res.data;
        }
      });
    },
    getStrList(id) {
      //四级
      proList({ id }).then(res => {
        if (res.code == 200) {
          console.log(res.data, "333");
          this.goodscalssOption3 = res.data;
        }
      });
    },
    handleClose() {},
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
    //重置密码
    restPass() {
      console.log(this.memberInfo, "111", this.dataForm);
      const obj = {
        id: this.memberInfo.id,
        memberAreaid: this.dataForm.memberAreaid,
        memberCityid: this.dataForm.memberCityid,
        memberPasswd: "string",
        stressId: this.dataForm.stressId,
        memberProvinceid: this.dataForm.memberProvinceid
      };
      setMemberPass(obj).then(res => {
        if (res.code == 200) {
          console.log(res, "sss");
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
              console.log(res, "222");
              this.$message({
                message: res.msg,
                type: "success",
                duration: 1000
              });
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
  width: 20%;
}
.header {
  text-align: center;
  font-weight: bold;
}
.el-input {
  width: 50%;
}
.el-dialog {
  width: 40%;
}
.rest {
  margin-left: 15px;
  display: inline-block;
  padding: 8px 10px;
  color: dodgerblue;
}
/* .restPass{text-align: center;} */
</style>