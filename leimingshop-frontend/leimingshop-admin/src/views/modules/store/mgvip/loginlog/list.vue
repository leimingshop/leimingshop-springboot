<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <el-form :inline="true" :model="dataForm" class="demo-form-inline grayLine" ref="dataForm">
            <el-form-item label="用户ID/手机号/昵称：" prop="memberId" clearable>
                <el-input v-model="dataForm.memberId" placeholder="用户ID/手机号/昵称"></el-input>
            </el-form-item>
            <el-form-item label="发送时间：" clearable>
                <el-date-picker
                    v-model="valuetime"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd"
                    @blur="changeTime"
                ></el-date-picker>
            </el-form-item>
            <el-form-item label="登录端：" prop="source" clearable>
                <el-select v-model="dataForm.source" placeholder="活动区域">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="手机" value="1"></el-option>
                    <el-option label="PC" value="0"></el-option>
                    <el-option label="其他" value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="getDataList">查询</el-button>
              <el-button type="primary" plain @click="rest">重置</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="dataList" border="" style="width: 100%">
          <el-table-column
            type='index'
            prop="$index"
            label="序号"
            align="center"
            width="70">
            <template slot-scope="scope">
              {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
            </template>
          </el-table-column>
            <el-table-column prop="id" label="用户ID" width="280"></el-table-column>
            <el-table-column prop="phoneNumber" label="手机号" width="200"></el-table-column>
            <el-table-column prop="loginName" label="昵称" width="150"></el-table-column>
            <el-table-column prop="createDate" label="操作时间"></el-table-column>
            <el-table-column prop="source" label="登录端" width="80" :formatter="sourceRule"></el-table-column>
            <el-table-column prop="ip" label="操作人IP"></el-table-column>
            <el-table-column prop="operation" label="操作内容" :formatter="stateRule"></el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="params.currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="params.currentPageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
    </div>
</template>
<script>
import mixinViewModule from "@/mixins/view-module";
import Bread from "@/components/bread";
import { loginLog } from "@/api/url";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: loginLog,
        getDataListIsPage: true,
        // deleteURL: deleteMemberUrl,
        deleteIsBatch: false,
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "id"
      },
      dataForm: {
        source: "",
        memberId: this.$route.query.memberId || ""
      },
      params: {
        currentPage: 1, //当前页数
        currentPageSize: 10 //每页显示的条数
      },
      valuetime: "", //开始结束时间
      breaddata: ["会员管理", "会员管理", "登录日志"],
      stateRule(row, column) {
        return row.operation == 0 ? "用户登录" : "用户退出";
      },
      sourceRule(row, column) {
        return row.source == 0 ? "PC" : row.source == 1 ? "手机" : "其他";
      }
    };
  },
  methods: {
    //选择开始结束时间
    changeTime() {
      this.dataForm.starTime = this.valuetime[0];
      this.dataForm.endTime = this.valuetime[1];
    },
    //重置
    rest() {
      this.$refs["dataForm"].resetFields();
      this.valuetime = [];
      this.dataForm.starTime = "";
      this.dataForm.endTime = "";
      this.dataForm.memberId = "";
      console.log(this.dataForm, "000");
      this.getDataList();
    },
    // 每页数
    sizeChangeHandle(val) {
      this.params.currentPageSize = val;
      this.params.currentPage = 1;
      this.limit = val;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.params.currentPage = val;
      this.page = val;
      this.getDataList();
    }
  },
  components: { Bread }
};
</script>
