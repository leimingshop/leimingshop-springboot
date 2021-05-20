<template>
  <div v-if="isList">
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
    <div class="memberInfo">
      <img
        class="memberAva"
        :src="'http://192.168.1.108:8888/group1/M00/00/04/wKgBbF0UX-SAYxbpAAKyM4Q0U_o788.png'"
        alt=""
      >
      <div class="memberDetail">
        <div class="detTop">
          <ul>
            <li>昵称：{{memberInfo.nickName}}</li>
            <li>邮箱：{{memberInfo.memberEmail}}</li>
            <li>姓名：{{memberInfo.memberName }}</li>
            <li>当前积分：{{memberInfo.memberInfoDTO.availablePoint}}</li>
            <li>用户来源：{{memberInfo.memberSource==0?'网站注册':'未知来源'}}</li>
            <li>上次登录IP:{{memberInfo.lastLoginIp }}</li>
          </ul>
          <ul>
            <li>性别：{{memberInfo.memberSex==0?'保密':memberInfo.memberSex==1?'女':'男'}}</li>
            <li>手机号：{{memberInfo.memberMobile}}</li>
            <li>生日：{{memberInfo.memberBirthday}}</li>
            <!-- <li>消费积分：{{memberInfo.memberInfoDTO.consumePoint}}</li> -->
            <li>用户状态：{{memberInfo.memberState==0?'正常':'锁定'}}</li>
            <li>上次登录时间:{{memberInfo.lastLoginDate}}</li>
          </ul>
        </div>
        <div class="detBot">
          <ul>
            <li>会员类别：{{memberInfo.gradeName}}</li>
            <li>地区：{{area==''?'':area}}{{city==''?'':'/'+city}}{{province==''?'':'/'+province}}{{stress==''?'':'/'+stress}}</li>
            <li style="margin-left:45%;color:blue;cursor:pointer;" @click="logMore">更多登录日志</li>
          </ul>
        </div>
      </div>
    </div>
    <el-tabs v-model="activeName" @tab-click="checkMember" class="botttomGapPadding">
      <el-tab-pane label="会员订单" name="first">
        <memberOrder ref="merberOrderCompon" :memberId="memberId" @goBack="goBack"></memberOrder>
      </el-tab-pane>
      <el-tab-pane label="会员积分" name="second">
        <span>抱歉，积分功能暂未开通，敬请期待！</span>
      </el-tab-pane>
      <el-tab-pane label="会员地址" name="third">
        <memberAddress ref="memberAddressCompon" :memberId="memberId"></memberAddress>
      </el-tab-pane>
    </el-tabs>
  </div>
  <memberOrderDet
    v-else
    :data="data"
    :addressInfo="addressInfo"
    :orderData="orderData"
    :orderLog="orderLog"
    :packageInfo="packageInfo"
    @goList="goList"
  ></memberOrderDet>
</template>

<script>
import {
  memberDet,
  memberAdd,
  memberOrders,
  areaList,
  proList,
  memberDetTable
} from "@/api/api";
import mixinViewModule from "@/mixins/view-module";
import memberAddress from "./memberDetail/memberAddress";
import memberOrder from "./memberDetail/memberOrder";
import memberOrderDet from "./memberDetail/memberOrderDet";
import Bread from "@/components/bread";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: "",
        getDataListIsPage: true,
        // exportURL: '/admin-api/log/login/export',
        deleteURL: "/admin-api/member",
        deleteIsBatch: false,
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "id"
      },
      isList: true, //列表页
      addEditDataVisible: false,
      breaddata: ["会员管理", "会员管理", "会员列表", "会员详情"],
      activeName: "first", //tab标签默认选项
      dataForm: {
        // groupName:''
      },
      row: "",
      value: [],
      area: "",
      city: "",
      province: "",
      stress: "",
      dataForm: {},
      labelPosition: "top",
      form: {
        name: "",
        region: ""
      }
    };
  },
  props: ["memberInfo", "memberId", "memberOrders"],
  components: {
    memberAddress,
    memberOrder,
    Bread,
    memberOrderDet
  },
  methods: {
    init() {
      this.memberInfo.gradeName = this.memberInfo.memberInfoDTO.gradeName;
      this.getCity();
      this.dataForm.memberAreaid = this.memberInfo.memberInfoDTO.memberProvinceid;
      this.dataForm.memberCityid = this.memberInfo.memberInfoDTO.memberCityid;
      this.dataForm.memberProvinceid = this.memberInfo.memberInfoDTO.memberAreaid;
      this.dataForm.stressId = this.memberInfo.memberInfoDTO.stressId;
      this.dataForm.memberAreaid == "" || this.dataForm.memberAreaid == null
        ? ""
        : this.getAreaList(this.dataForm.memberAreaid);
      this.dataForm.memberCityid == "" || this.dataForm.memberCityid == null
        ? ""
        : this.getProList(this.dataForm.memberCityid);
      this.dataForm.memberProvinceid == "" ||
      this.dataForm.memberProvinceid == null
        ? ""
        : this.getStrList(this.dataForm.memberProvinceid);
    },
    //tab切换
    checkMember(tab, event) {
      console.log(tab.name);
      if (tab.name == "first") {
      } else if (tab.name == "second") {
      } else if (tab.name == "third") {
        this.$refs.memberAddressCompon.init();
      }
    },
    goList(){
      this.isList=!this.isList;
    },
    getCity() {
      //所有一级区域
      areaList().then(res => {
        if (res.code == 200) {
          this.goodscalssOption0 = res.data;
          this.goodscalssOption0.forEach((item, index) => {
            if (item.id == this.dataForm.memberAreaid) {
              this.area = item.areaName;
            }
          });
        }
      });
    },
    getAreaList(id) {
      //二级
      proList({ id }).then(res => {
        if (res.code == 200) {
          this.goodscalssOption1 = res.data;
          this.goodscalssOption1.forEach((item, index) => {
            if (item.id == this.dataForm.memberCityid) {
              this.city = item.areaName;
            }
          });
        }
      });
    },
    getProList(id) {
      //三级
      proList({ id }).then(res => {
        if (res.code == 200) {
          this.goodscalssOption2 = res.data;
          this.goodscalssOption2.forEach((item, index) => {
            if (item.id == this.dataForm.memberProvinceid) {
              this.province = item.areaName;
            }
          });
        }
      });
    },
    getStrList(id) {
      //四级
      proList({ id }).then(res => {
        if (res.code == 200) {
          this.goodscalssOption3 = res.data;
          this.goodscalssOption3.forEach((item, index) => {
            if (item.id == this.dataForm.stressId) {
              this.stress = item.areaName;
            }
          });
        }
      });
    },
    //返回上一级
    changePage() {
      this.$emit("changeState");
    },
    //切换至订单详情
    goBack(val) {
      this.data=val;
      this.addressInfo = val.orderAddressDTO; //收货人信息
      this.orderData = val.orderGoodsDTOList;
      this.orderLog = val.orderLogisticsLogDTOList; //物流
      this.packageInfo = val.orderLogDTOList; //订单状态
      this.isList = false;
    },
    //更多日志
    logMore() {
      this.$router.push({ name: "mgvip-loginlog-list" });
    },
    //查看会员详情
    goDetail(id) {
      const obj = { id: id };
      this.memberId = id;
      memberDetTable(obj).then(res => {
        if (res.code == 200) {
          this.memberInfo = res.data;
          this.isGradelist = false;
          this.$nextTick(() => {
            this.$refs.membershow.init();
          });
        }
      });
      memberOrders(obj).then(res => {
        if (res.code == 200) {
          this.memberOrders = res.data;
          this.isGradelist = false;
        }
      });
    }
  }
};
</script>
<style scoped>
.memberInfo {
  width: 100%;
  /* height: 190px; */
  padding: 15px;
  display: flex;
  justify-content: space-around;
  /* border: 1px solid red; */
}
.memberInfo .memberAva {
  width: 170px;
  height: 170px;
}
.memberInfo .memberDetail {
  height: 150px;
  width: 90%;
  /* border: 1px solid blue; */
}
.detTop {
  width: 100%;
  border-bottom: 1px solid #e1e1e1;
}
.detTop ul,
.detBot ul {
  display: flex;
  padding-left: 10px;
  /* justify-content: space-around; */
}
.detTop ul li,
.detBot ul li {
  list-style: none;
  width: 230px;
  text-align: left;
  margin-right: 20px;
}
.el-form {
  margin-left: 10px;
}

/* .memberAvatar {
  width: 100px;
  height: 100px;
  border: 1px solid gray;
  float: left;
}
.detailInfo {
  margin-left: 125px;
  line-height: 25px;
}
.detailInfo:nth-child(3) {
  padding-bottom: 5px;
  border-bottom: 1px solid gray;
}
.detailInfo:nth-child(4) {
  padding-top: 5px;
}
.detailInfo span {
  width: 15%;
  display: inline-block;
}
.detailInfo select {
  width: 85px;
  display: inline-block;
}
.detailInfo .province {
  width: 500px;
}
.detailInfo .province select {
  margin-right: 10px;
} */
</style>
