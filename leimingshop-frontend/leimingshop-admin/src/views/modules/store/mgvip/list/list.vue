<template>
  <div v-if="isGradelist">
    <Bread :breaddata="breaddata"></Bread>
    <el-form
      :inline="true"
      class="grayLine topGapPadding"
      :model="dataForm"
      ref="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item label="会员ID/昵称：" prop="nickName">
        <el-input v-model="dataForm.nickName" placeholder="会员ID/昵称" clearable></el-input>
      </el-form-item>
      <el-form-item label="会员手机号：" prop="memberMobile">
        <el-input v-model="dataForm.memberMobile" placeholder="会员手机号" clearable></el-input>
      </el-form-item>
      <el-form-item label="会员等级：" prop="gradeId">
        <el-select v-model="dataForm.gradeId" placeholder="会员等级" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option
            v-for="(item,index) in gradeName"
            :key="index"
            :label="item.gradeName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList()">查询</el-button>
        <el-button @click="reset()" type="primary" plain >重置</el-button>
      </el-form-item>
      <br />
      <!-- <el-form-item>
        <el-button type="primary" @click="deal()">批量更换会员等级</el-button>
      </el-form-item>-->
    </el-form>
    <el-table
      width="100%"
      :data="dataList"
      border=""
      v-loading="mixinViewModuleOptions.dataListLoading"
      style="width: 100%"
      @selection-change="dataListSelectionChangeHandle"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
        <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
      </el-table-column>
      <el-table-column prop="id" label="会员ID" align="center" width="170"></el-table-column>
      <el-table-column prop="memberAvatar" label="头像" align="center">
        <template slot-scope="scope">
          <img
            :src="$imgDomain + scope.row.memberAvatar"
            alt=""
            style=" object-fit: contain;width: 70px;height:70px;border-radius:100px;"
          >
        </template>
      </el-table-column>
      <el-table-column prop="nickName" label="会员昵称" align="center"></el-table-column>
      <el-table-column prop="memberMobile" align="center" label="会员手机号" width="140"></el-table-column>
      <el-table-column prop="gradeName" align="center" label="会员等级" width="140"></el-table-column>
      <el-table-column prop="memberSource" align="center" label="会员来源">
        <template slot-scope="scope">
          <!--<sapn :memberSourceType=memberSourceType>{{scope.row.memberSource}}</sapn>-->
          <span v-if="scope.row.memberSource == 0">网站注册</span>
          <span v-if="scope.row.memberSource == 1">微信注册</span>
          <span v-if="scope.row.memberSource == 2">QQ注册</span>
          <span v-if="scope.row.memberSource == 3">微博注册</span>
        </template>
      </el-table-column>
      <el-table-column prop="createDate" align="center" label="注册时间" width="170"></el-table-column>
      <el-table-column
        prop="memberState"
        align="center"
        label="状态"
        width="80"
        :formatter="stateRules"
      >
        <!-- <template slot-scope="scope">
          <span v-if="scope.row.groupStatus == 0">启用</span>
          <span v-else>禁止</span>
        </template>-->
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template slot-scope="scope">
          <el-button @click.native.prevent="goDetail(scope.row.id)" type="text" size="mini">查看</el-button>
          <el-button @click.native.prevent="addHandle(scope.row.id)" type="text" size="mini">编辑</el-button>
          <el-button
            @click.native.prevent="forbitHandle(scope.row)"
            type="text"
            size="mini"
            class="artdisable"
            v-if="scope.row.memberState==0"
          >禁用</el-button>
          <el-button
            @click.native.prevent="forbitHandle(scope.row)"
            type="text"
            class="artstart"
            size="mini"
            v-if="scope.row.memberState==1"
          >启用</el-button>
          <el-button
            @click.native.prevent="loginLogHandle(scope.row.id)"
            type="text"
            size="mini"
          >登录日志</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="limit"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="pageSizeChangeHandle"
      @current-change="pageCurrentChangeHandle"
    ></el-pagination>
    <!-- 编辑 -->
    <editHandle
      ref="memberEdit"
      v-if="editMember"
      @changeWindow="changeWindow"
      :memberInfo="memberInfo"
      :memberInfoSourse="memberInfoSourse"
    ></editHandle>
    <!-- 批量更换等级 -->
    <!-- <el-dialog title="批量更换会员等级" :visible.sync="dialogVisible">
      <el-form :model="form">
        <p>将所选中会员批量转移到下面的会员等级下</p>
        <el-form-item label="会员类别：" :label-width="formLabelWidth">
          <el-select v-model="form.region" placeholder="请选择活动区域">
            <el-option label="普通会员" value="0"></el-option>
            <el-option label="黄金会员" value="1"></el-option>
            <el-option label="铂金会员" value="2"></el-option>
            <el-option label="白银会员" value="3"></el-option>
            <el-option label="钻石会员" value="4"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>-->
  </div>
  <gradeDet
    v-else
    ref="membershow"
    :memberInfo="memberInfo"
    :memberId="memberId"
    :memberOrders="memberOrders"
    @changeState="changeState"
  ></gradeDet>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import { memberPageUrl, deleteMemberUrl, memberGrade } from "@/api/url";
import {
  updateMembergrade,
  memberDet,
  memberOrders,
  memberList,
  memberDetTable
} from "@/api/api";
import cloneDeep from "lodash/cloneDeep";
import Bread from "@/components/bread";
// import changeGrade from "./model-change-grade";
import editHandle from "./model-edit";
import gradeDet from "./gradeDet";

export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: memberPageUrl,
        getDataListIsPage: true,
        deleteURL: deleteMemberUrl,
        deleteIsBatch: false,
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "id"
      },
      //   addEditDataVisible: false,
      editMember: false,
      isGradelist: true,
      isEdit: false, //编辑
      memberInfo: {}, //会员信息
      forbitLoading: false, //当前row
      currentIndex: -1, //当前index
      dataForm: {
        nickName: "",
        memberMobile: "",
        gradeId: ""
      },
      memberInfoSourse: "", //保存源数据
      breaddata: ["会员管理", "会员管理", "会员列表"],
      gradeName: [],
      form: {
        region: "0"
      },
      stateRules:function(row, column) {
        return row.memberState == 0 ? <el-tag type="success">启用</el-tag>:<el-tag type="danger">禁用</el-tag>;
      },
      memberOrders: {},
      formLabelWidth: "120px"
    };
  },
  components: {
    editHandle,
    Bread,
    gradeDet
  },
  created() {
    this.memberGra();
  },
  methods: {
    //会员等级
    memberGra() {
      memberList().then(res => {
        if (res.code == 200) {
          this.gradeName = res.data;
        }
      });
    },
    // 更改权限
    addOrEditHandle() {
      this.addEditDataVisible = true;
    },
    // 编辑
    addHandle(id) {
      this.editMember = true;
      this.memberId = id;
      const obj = { id: id };
      memberDet(obj).then(res => {
        if (res.code == 200 && res.data.id != "") {
          this.memberInfo = res.data;
          this.memberInfoSourse = cloneDeep(res.data);
          this.memberInfo.memberPasswd = "******";
          this.isEdit = true;
          this.$nextTick(() => {
            this.$refs.memberEdit.init();
          });
        }
      });
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
       
    },
    // 重置
    reset() {
      this.$refs["dataForm"].resetFields();
      this.dataForm.gradeId = "";
      this.getDataList();
    },
    // 启用&禁用
    forbitHandle(index) {
      let obj = {
        id: index.id
      };
      updateMembergrade(obj).then(res => {
        console.log(res);
        if (res.code == 200) {
          this.$message({
            message: res.msg,
            type: "success",
            duration: 1000
          });
          this.getDataList();
        } else {
          this.$message({
            message: res.msg,
            type: "error",
            duration: 1000
          });
        }
      });
    },
    //编辑页面触发
    changeWindow() {
      this.editMember = false;
    },
    //页面跳转
    changeState() {
      this.isGradelist = true;
      // this.$emit("showListFn");
    },
    //批量处理
    // deal() {
    //   this.dialogVisible=true;
    //   console.log(this.dataListSelections, "选中项");
    // }
    //登录日志
    loginLogHandle(id) {
      this.$router.push({
        name: "mgvip-loginlog-list",
        query: { memberId: id }
      });
    }
  }
};
</script>
<style>
.el-dialog {
  width: 40%;
}
</style>
