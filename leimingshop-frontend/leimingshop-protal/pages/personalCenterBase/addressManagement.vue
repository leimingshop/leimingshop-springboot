<template>
  <div>
    <div class="notice">地址管理</div>
    <div class="address-management">
      <div class="address-management1">
        <div class="management1-left" v-if="maxAddressCount - pageTotal > 0">
          收货地址（地址最多
          <span>{{ maxAddressCount }}</span>
          条，还能保存{{ maxAddressCount - pageTotal }}条）
        </div>
        <div class="management1-left" v-else="maxAddressCount - pageTotal < 0">
          收货地址数量达到上限，不能再保存
        </div>
        <div
          class="management1-right"
          @click="showAddress1"
          v-show="maxAddressCount - pageTotal > 0"
        >
          <Icon type="md-add" />
          <span>添加收货地址</span>
        </div>
      </div>
      <div class="has-data" v-show="pageTotal > 0">
        <div class="address-management2">
          <ul>
            <li v-for="(item, index) in addressListObject.list" :key="index">
              <div class="management2-left" style="color: #666666">
                <p>
                  <span class="management2Left">收货人：</span>
                  <span class="word">{{ item.trueName }}</span>
                </p>
                <p>
                  <span class="management2Left">联系方式：</span>
                  <span class="word">{{ item.mobPhone }}</span>
                </p>
                <p style="display: flex">
                  <span class="management2Left">收货地址：</span>
                  <span class="word"
                    >{{ item.areaInfo }} {{ item.address }}</span
                  >
                </p>
              </div>
              <div class="management2-right">
                <div class="set-default" v-if="item.defaultFlag == 1">
                  默认地址
                </div>
                <div class="set-default1" v-else @click="setDefault(index)">
                  设为默认
                </div>
                <div class="button1">
                  <span @click="showAddress2(index)">编辑</span>
                  <span @click="addressDeletion(index)">删除</span>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="address-management3">
          <!-- 分页 -->
          <paging
            class="paging"
            :totalCount="$store.state.addressManagement.pageTotal"
            :pageSize="page.limit"
            @handlePageChange="handlePageChange"
            v-if="$store.state.addressManagement.pageFlag && pageTotal > 5"
          />
          <div v-else style="height: 60px"></div>
        </div>
      </div>
      <div class="no-data" v-show="pageTotal <= 0">
        <!-- <div class="line"></div> -->
        <!-- 无收获地址 -->
        <div class="no-address">
          <img src="/img/06.personalCenter/noAddress.png" alt />
          <p>当前还未添加收货地址</p>
        </div>
      </div>
      <!-- 修改地址弹窗 -->
      <div class="modaddr-wrapper" v-show="modifyAddress">
        <div class="modify-address">
          <a class="ivu-modal-close" @click="hideAddress('formTop')">
            <i class="ivu-icon ivu-icon-ios-close"></i>
          </a>
          <p>
            <span v-show="receivingAddress == 1">添加收货地址</span>
            <span v-show="receivingAddress == 2">编辑收货地址</span>
          </p>
          <Form
            :model="formTop"
            label-position="left"
            :rules="addressVerification"
            :label-width="100"
            style="padding-left: 74px"
            ref="formTop"
          >
            <FormItem label="姓名" prop="trueName">
              <i-Input
                v-model="formTop.trueName"
                placeholder="请填写收货人姓名"
                style="width: 210px; height: 34px"
                :maxlength="20"
              ></i-Input>
            </FormItem>
            <FormItem label="手机号" prop="mobPhone">
              <i-Input
                v-model="formTop.mobPhone"
                placeholder="请填写手机号码"
                style="width: 210px; height: 34px"
                :maxlength="11"
              ></i-Input>
            </FormItem>
            <FormItem label="填写信息" prop="areaInfo">
              <Cascader
                @on-change="onChangeAAA"
                :data="addressData"
                v-model="formTop.areaInfo"
                :load-data="loadData"
                style="width: 405px; height: 34px"
                placeholder="请选择省/市/区"
              ></Cascader>
            </FormItem>
            <FormItem label="详细地址" prop="address">
              <i-Input
                v-model="formTop.address"
                placeholder="请输入详细地址"
                style="width: 405px; height: 34px"
                :maxlength="50"
              ></i-Input>
            </FormItem>
            <FormItem label="邮政编码" prop="zipCode">
              <i-Input
                v-model="formTop.zipCode"
                placeholder="请输入邮政编码"
                style="width: 210px; height: 34px"
                :maxlength="6"
              ></i-Input>
            </FormItem>
            <FormItem>
              <Checkbox v-model="formTop.setDefaultaddFlag">
                设为默认收货地址
                <span class="default-span">提示：每次下单会默认使用该地址</span>
              </Checkbox>
            </FormItem>
          </Form>
          <div
            class="anniu-group1"
            style="text-align: center; padding-bottom: 41px"
          >
            <Button
              class="cancel"
              style="width: 96px; height: 34px; background: f5f5f5"
              @click="hideAddress('formTop')"
              >取消</Button
            >
            <Button
              class="confirm"
              style="
                width: 96px;
                height: 34px;
                background: #dd2619;
                color: #ffffff;
                margin-left: 38px;
              "
              @click="confirm('formTop')"
              >确定</Button
            >
          </div>
        </div>
      </div>
      <!-- 默认地址设置成功弹窗 -->
      <prompt-page
        :promptShow="task.promptShow"
        :successOrFail="task.successOrFail"
        :titpMessage="task.titpMessage"
      ></prompt-page>
      <!-- 提交之前进行询问-->
      <div class="popup-wrapper" v-show="titleShow">
        <div class="title">
          <div>
            <img
              src="/img/06.personalCenter/cha@2x.png"
              alt
              @click="titleShow = false"
            />
          </div>
          <p class="successfail-p">是否删除地址记录?</p>
          <div class="button-group">
            <div class="button-left" @click="titleShow = false">取消</div>
            <div class="button-right" @click="determine">确定</div>
          </div>
        </div>
      </div>
      <div
        class="loading"
        style="
          position: fixed;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          width: 400px;
        "
        v-show="!addressListObject"
      >
        <i-Col class="demo-spin-col" span="8">
          <Spin fix>
            <Icon
              type="ios-loading"
              size="18"
              class="demo-spin-icon-load"
            ></Icon>
            <div>数据加载中</div>
          </Spin>
        </i-Col>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import paging from "@/components/common/paging";
import loading from "@/components/common/loading";
import promptPage from "@/components/06.personalCenter/common/promptPage";
import {
  getAddressSaving, //新地址增加
  getThreelistId, //获取四级地址通过id
  putAddressModification, //会员地址修改
  deleteAddressDeletion, //会员地址删除
  setDefaultAddress, //设置默认地址
  getEditInfo, //编辑地址（根据id查询）
} from "@/api/api_06personalCenter.js";
export default {
  head() {
    return {
      title: "地址管理",
      meta: [
        {
          hid: "description",
          name: "description",
          content: "My custom description",
        },
      ],
    };
  },
  components: {
    paging,
    loading,
    promptPage,
  },
  props: {},
  data() {
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("手机号不能为空"));
      } else if (!/^1[34578]\d{9}$/.test(value)) {
        callback("手机号格式不正确");
      } else {
        callback();
      }
    };
    //验证邮政编码
    const validatePostoffice = (rule, value, callback) => {
      if (!value) {
        return callback();
      } else if (!/^[0-9]{6}$/.test(value)) {
        callback("邮政编码格式不正确");
      } else {
        callback();
      }
    };
    return {
      titleShow: false,
      task: {
        promptShow: false,
        successOrFail: 0,
        titpMessage: "默认地址修改成功",
      },
      currentIndex: -1,
      setDefaultIndex: 0,
      addressData: [],
      addressVerification: {
        //地址填入信息验证 规则
        trueName: [
          {
            required: true,
            message: "请输入正确的名字",
            trigger: "blur",
          },
        ],
        mobPhone: [
          {
            required: true,
            validator: validatePhone,
            trigger: "blur",
          },
        ],
        areaInfo: [
          {
            required: true,
            trigger: "blur",
            message: "请输入省、市、县、镇。",
            type: "array",
          },
        ],
        address: [
          {
            required: true,
            trigger: "change blur",
            message: "请输入正确的详细地址",
          },
        ],
        zipCode: [
          {
            required: false,
            trigger: "change blur",
            validator: validatePostoffice,
          },
        ],
      }, //修改地址相关信息验证格式
      modifyAddress: false, //修改地址弹窗显示、隐藏
      mask1Show: false, //修改地址成功提示
      receivingAddress: 1, //1 新建地址  2编辑地址
      maxAddressCount: 20, //最多地址数量
      addressList: [],
      //编辑地址信息参数 start
      formTop: {
        //地址填入信息
        trueName: "", //姓名
        mobPhone: "", //电话
        areaInfo: [], //省、市、区
        address: "", //详细地址 string
        zipCode: "", //邮政编码
        setDefaultaddFlag: false, //是否设置为默认地址
      },
      formTopSubmit: {
        trueName: "w", //姓名
        mobPhone: "15810040865", //电话
        areaInfo: "河北省 邯郸市 涉县",
        address: "ddd", //详细地址 string
        zipCode: "056400", //邮政编码
        provinceId: "130000", //省份ID
        cityId: "130100", //城市ID
        areaId: "130102", //县级ID
        stressId: "130102001", //乡镇ID
      },
      formTopSubmit1: {
        //编辑地址用的
        trueName: "w", //姓名
        mobPhone: "15810040865", //电话
        areaInfo: "河北省 邯郸市 涉县",
        address: "ddd", //详细地址 string
        zipCode: "056400", //邮政编码
        provinceId: "130000", //省份ID
        cityId: "130100", //城市ID
        areaId: "130102", //县级ID
        stressId: "130102001", //乡镇ID
        defaultFlag: 0, //新增不用加
        id: "", //增加新的店铺就不能加
        memberId: "", //增加新的店铺就不能加
      },
    };
  },
  watch: {},
  computed: {
    countPages() {
      return Math.ceil(this.pageTotal / this.page.limit);
    },
    ...mapState("addressManagement", [
      "addressListObject",
      "pageTotal",
      "page",
    ]),
  },
  methods: {
    loadData(item, callback) {
      item.loading = true;
      getThreelistId({ id: item.value })
        .then((res) => {
          if (res.data && res.data.code == 200) {
            item.children = [];
            res.data.data.forEach((item2, index) => {
              if (item2.count == 0) {
                item.children.push({
                  value: item2.id,
                  label: item2.areaName,
                });
              } else {
                item.children.push({
                  value: item2.id,
                  label: item2.areaName,
                  children: [],
                  loading: false,
                });
              }
            });
            item.loading = false;
            callback();
          }
        })
        .catch((err) => console.log(err));
    },
    onChangeAAA(value, selectedData) {},
    ...mapActions("addressManagement", [
      "getAddressList",
      "getCurrentPage",
      "getAnswer",
    ]),
    onChange(value, selectedData) {},
    handlePageChange(val) {
      this.getCurrentPage(val);
      this.getAddressList({
        page: val,
        limit: this.page.limit,
      });
    },
    //新增地址
    showAddress1() {
      this.receivingAddress = 1;
      this.modifyAddress = true;
    },
    //编辑地址
    showAddress2(index) {
      this.receivingAddress = 2;
      let targetAddress = {};
      let targetAddressId = this.addressListObject.list[index].id;
      //回显地址信息
      getEditInfo({ id: targetAddressId })
        .then((res) => {
          if (res && res.code == 200) {
            targetAddress = res.data;
            this.formTop.trueName = targetAddress.trueName;
            this.formTop.mobPhone = targetAddress.mobPhone;
            this.formTop.areaInfo = [
              targetAddress.provinceId,
              targetAddress.cityId,
              targetAddress.areaId,
            ];
            if (targetAddress.stressId) {
              this.formTop.areaInfo.push(targetAddress.stressId);
            }
            this.formTop.address = targetAddress.address;
            this.formTop.zipCode = targetAddress.zipCode;
            //回显判断是否设置为默认地址
            if (targetAddress.defaultFlag == 0) {
              this.formTop.setDefaultaddFlag = false;
            } else {
              this.formTop.setDefaultaddFlag = true;
            }
            this.formTopSubmit1.id = targetAddress.id; //新增地址不能加
            this.formTopSubmit1.memberId = targetAddress.memberId; //新增地址不能加
            this.modifyAddress = true;
          }
        })
        .catch((err) => {console.log(err)});
    },
    taskModel(promptShow, successOrFail, titpMessage, time) {
      this.task = {
        promptShow,
        successOrFail,
        titpMessage,
      };
      setTimeout(() => {
        this.task.promptShow = false;
      }, time);
    },
    hideAddress(name) {
      this.modifyAddress = false;
      this.$refs[name].resetFields(); //置空表单
      this.formTop.setDefaultaddFlag = false;
    },
    confirm(name) {
      var _this = this;
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.modifyAddress = false;
          this.handelAddressName();
          if (this.receivingAddress == 1) {
            getAddressSaving(this.formTopSubmit) //新增地址
              .then((res) => {
                if (res.data && res.data.code == 200) {
                  this.taskModel(true, 1, "添加收货地址设置成功", 2000);
                  this.getAddressList({
                    page: this.page.currentPage,
                    limit: this.page.limit,
                  });
                }
              })
              .catch((err) => {
                this.$Message.error(err);
              });
            this.$refs[name].resetFields(); //置空表单
            this.formTop.setDefaultaddFlag = false;
          } else if (this.receivingAddress == 2) {
            // 将编辑的地址信息提交
            putAddressModification(this.formTopSubmit1)
              .then((res) => {
                if (res.data && res.data.code == 200) {
                  this.taskModel(true, 1, "编辑地址设置成功", 2000);
                  this.getAddressList({
                    page: this.page.currentPage,
                    limit: this.page.limit,
                  });
                }
              })
              .catch((err) => {console.log(err)});
            this.modifyAddress = false;
            this.$refs[name].resetFields(); //置空表单
            this.formTop.setDefaultaddFlag = false;
          }
        } else {
          this.$Message.error("请您将地址信息添加完整后再提交！");
        }
      });
    },
    addressDeletion(index) {
      this.titleShow = true;
      this.currentIndex = index;
    },
    //经过确认之后进行删除
    determine() {
      this.titleShow = false;
      //删除地址
      deleteAddressDeletion({
        id: this.addressListObject.list[this.currentIndex].id,
      })
        .then((res) => {
          if (res.data && res.data.code == 200) {
            this.taskModel(true, 1, "删除地址设置成功", 2000);
            this.getAnswer();
            this.getAddressList({
              page: this.page.currentPage,
              limit: this.page.limit,
            });
          }
        })
        .catch((err) => {console.log(err)});
    },
    handelAddressName() {
      //处理areaInfo和城市id 提交信息
      var demo = document.getElementsByClassName("ivu-cascader-label");
      var string = demo[0].innerText;
      var arr = string.split(" / ");
      var str = "";
      arr.forEach((item, index) => {
        str += `${item} `;
      });
      if (this.receivingAddress == 1) {
        this.formTopSubmit.provinceId = this.formTop.areaInfo[0]; //省份ID
        this.formTopSubmit.cityId = this.formTop.areaInfo[1]; //城市ID
        this.formTopSubmit.areaId = this.formTop.areaInfo[2]; //县级ID
        this.formTopSubmit.stressId = this.formTop.areaInfo[3]; //乡级ID
        this.formTopSubmit.trueName = this.formTop.trueName; //姓名
        this.formTopSubmit.mobPhone = this.formTop.mobPhone; //电话
        this.formTopSubmit.areaInfo = str; //省份、城市、镇
        this.formTopSubmit.address = this.formTop.address; //省份、城市、镇
        this.formTopSubmit.zipCode = this.formTop.zipCode; //邮政编码
        if (this.formTop.setDefaultaddFlag == true) {
          //是否设置为默认地址
          this.formTopSubmit.defaultFlag = 1;
        } else if (this.formTop.setDefaultaddFlag == false) {
          this.formTopSubmit.defaultFlag = 0;
        }
      } else if (this.receivingAddress == 2) {
        this.formTopSubmit1.provinceId = this.formTop.areaInfo[0]; //省份ID
        this.formTopSubmit1.cityId = this.formTop.areaInfo[1]; //城市ID
        this.formTopSubmit1.areaId = this.formTop.areaInfo[2]; //县级ID
        this.formTopSubmit1.stressId = this.formTop.areaInfo[3]; //乡级ID
        this.formTopSubmit1.trueName = this.formTop.trueName; //姓名
        this.formTopSubmit1.mobPhone = this.formTop.mobPhone; //电话
        this.formTopSubmit1.areaInfo = str; //省份、城市、镇
        this.formTopSubmit1.address = this.formTop.address; //省份、城市、镇
        this.formTopSubmit1.zipCode = this.formTop.zipCode; //邮政编码
        if (this.formTop.setDefaultaddFlag == true) {
          //是否设置为默认地址
          this.formTopSubmit1.defaultFlag = 1;
        } else if (this.formTop.setDefaultaddFlag == false) {
          this.formTopSubmit1.defaultFlag = 0;
        }
      }
    },
    //设置默认地址
    setDefault(index) {
      setDefaultAddress({ id: this.addressListObject.list[index].id })
        .then((res) => {
          if (res.data && res.data.code == 200) {
            this.taskModel(true, 1, "默认地址设置成功", 2000);
            this.getAddressList({
              page: this.page.currentPage,
              limit: this.page.limit,
            });
          } else if (res.data && res.data.code == 500) {
            this.taskModel(true, 0, res.data.msg, 2000);
          }
        })
        .catch((err) => {console.log(err)});
    },
  },
  created() {},
  mounted() {
    this.$store.state.addressManagement.page.limit = 5;
    this.getAddressList({
      page: 1,
      limit: this.page.limit,
    });
    //一级一级加载
    getThreelistId({ id: 0 })
      .then((res) => {
        if (res.data && res.data.code == 200) {
          res.data.data.forEach((item, index) => {
            this.addressData.push({
              value: item.id,
              label: item.areaName,
              children: [],
              loading: false,
            });
          });
        }
      })
      .catch((err) => {console.log(err)});
  },
};
</script>
<style scoped lang="scss">
.notice {
  width: 100%;
  height: 54px;
  background: #ffffff;
  padding: 20px 40px 0;
  border-bottom: 1px solid #ebebeb;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: bold;
  color: #333333;
}
.address-management {
  padding: 20px 37px 20px 37px;
  min-height: 800px;
  position: relative;
  font-size: 14px;
  color: #666666;
  background: #ffffff;
}
.address-management /deep/ {
  .ivu-form-item-label {
    text-align: right;
  }
}
.address-management1 {
  display: flex;
  justify-content: space-between;
  // padding-bottom: 10px;
  .management1-left {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    span {
      color: red;
    }
  }
  .management1-right {
    cursor: pointer;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #0491ff;
  }
}
.address-management2 {
  margin-top: 20px;
  li {
    border: 1px solid #f6f6f6;
    padding: 15px 30px 20px 30px;
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    &:hover {
      border: 1px solid #ebebeb;
    }
    .management2-right {
      cursor: pointer;
      display: flex;
      .set-default {
        width: 71px;
        height: 30px;
        background: linear-gradient(270deg, #f7460a 0%, #e02d19 100%);
        border-radius: 4px;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        text-align: center;
        line-height: 30px;
        color: #ffffff;
        border-radius: 15px;
        margin-right: 30px;
        &:hover {
          opacity: 0.8;
        }
      }
      .set-default1 {
        width: 71px;
        height: 30px;
        background: #ffffff;
        font-size: 12px;
        border: 1px solid #cccccc;
        text-align: center;
        line-height: 30px;
        color: #666666;
        font-weight: 500;
        font-family: PingFangSC-Medium, PingFang SC;
        border-radius: 15px;
        margin-right: 30px;
        &:hover {
          opacity: 0.8;
        }
      }
      .button1 {
        // width: 100%;
        display: flex;
        justify-content: space-between;
        margin-top: 5px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang Sc;
        font-weight: 400;
        // padding: 0 6px;
        span {
          color: #dd2619;
          margin-right: 10px;
        }
      }
    }
    .management2-left {
      p {
        margin-top: 10px;
        display: flex;
      }
      .management2Left {
        width: 78px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        float: left;
        text-align: right;
      }
      .word {
        width: 400px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #222222;
        font-weight: 400;
      }
    }
  }
}
.address-management3 {
  text-align: center;
  margin-top: 20px;
  .ivu-page {
    position: relative;
  }
}
.modaddr-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 1000;
  .modify-address {
    width: 690px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: #ffffff;
    // padding: 24px 0 0 73px;
    padding: 24px 0 0 0;
    border-radius: 10px;
    p {
      display: flex;
      justify-content: space-between;
      justify-content: center;
      padding-bottom: 29px;
      span {
        font-size: 24px;
        color: #333333;
      }
    }
  }
}
.mask1 {
  position: fixed;
  background: rgba(0, 0, 0, 0.4);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #ffffff;
  padding: 10px 20px;
  text-align: center;
  border-radius: 8px;
}
.no-data {
  width: 200px;
  position: absolute;
  top: 194px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  .line {
    width: 950px;
    border-bottom: 1px solid #ebebeb;
    position: absolute;
    left: -275px;
    top: -114px;
  }
}
.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
.demo-spin-col {
  height: 100px;
  position: relative;
  border: 1px solid #eee;
}
.popup-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(1, 1, 1, 0.13);
}
.title {
  padding: 0 30px;
  background: #ffffff;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 4px solid rgba(0, 0, 0, 0.2);
  border-radius: 6px;
  padding: 0 80px 36px 80px;
  img {
    width: 14px;
    position: absolute;
    right: 13px;
    top: 13px;
  }
  .successfail-p {
    font-size: 24px;
    color: #333333;
    text-align: center;
    margin-top: 46px;
  }
  .button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 53px;
    .button-left {
      width: 68px;
      height: 32px;
      line-height: 32px;
      font-size: 15px;
      color: #333333;
      background: #f5f5f5;
      text-align: center;
      cursor: pointer;
      border-radius: 1px;
    }
    .button-right {
      width: 68px;
      height: 32px;
      line-height: 32px;
      font-size: 15px;
      color: #ffffff;
      background: #dd2619;
      text-align: center;
      cursor: pointer;
      border-radius: 1px;
    }
  }
}
.no-address {
  p {
    font-size: 14px;
    color: #666666;
    margin-top: 31px;
  }
}
.default-span {
  font-size: 14px;
  font-family: PingFangSC-Regular, "PingFang SC";
  font-weight: 400;
  color: rgb(235, 72, 68);
}
/deep/ .ivu-page .ivu-page-disabled .ivu-icon {
  color: #333333 !important;
}
</style>
