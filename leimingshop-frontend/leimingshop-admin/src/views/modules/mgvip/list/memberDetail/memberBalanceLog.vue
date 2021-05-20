<template>
  <div>
    <el-table
      width="100%"
      :data="dataList"
      border=""
      v-loading="mixinViewModuleOptions.dataListLoading"
      style="width: 100%"
      @selection-change="dataListSelectionChangeHandle">
      <el-table-column prop="beforeBalance" label="变动前" align="center"></el-table-column>
      <el-table-column prop="changeBalance" label="变动金额"  align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.changeBalance >= 0">+{{scope.row.changeBalance}}</span>
          <span v-if="scope.row.changeBalance < 0">{{scope.row.changeBalance}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="currentBalance" label="当前金额"  align="center"></el-table-column>
      <el-table-column prop="currentValue" label="类型说明"  align="center">
        <!-- 余额类型(1:运营端调整2:用户购买下单3:订单退款4:用户充值5:商家返利) -->
        <template slot-scope="scope">
          <span v-if="scope.row.balanceType == 1">{{"运营端调整"}}</span>
          <span v-if="scope.row.balanceType == 2">{{"用户购买下单"}}</span>
          <span v-if="scope.row.balanceType == 3">{{"订单退款"}}</span>
          <span v-if="scope.row.balanceType == 4">{{"用户充值"}}</span>
          <span v-if="scope.row.balanceType == 5">{{"商家返利"}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createDate" align="center"  label="创建时间"></el-table-column>
      <!-- <el-table-column prop="remark" align="center" label="备注"></el-table-column> -->
    </el-table>
    <el-pagination
      @size-change="pageSizeChangeHandle"
      @current-change="pageCurrentChangeHandle"
      :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="limit"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
  </div>
</template>
<script>
import mixinViewModule from "@/mixins/view-module";
import { memberBalancelogPage } from "@/api/url";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: memberBalancelogPage,
        getDataListIsPage: true,
        dataListLoading: false
      },
      dataForm: {
        memberId: ""
      },
      isChange: false
    };
  },
  props: ["memberId"],
  watch: {
    isChange: "getDataList" //修改成功后更新数据
  },
  methods: {
    init() {
      this.dataForm.memberId = this.memberId;
      console.log(this.dataForm, "条件查询");
      this.getDataList();
    }
  }
};
</script>
<style scoped>
.towEllipsis{
  text-align: left;
   text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>


