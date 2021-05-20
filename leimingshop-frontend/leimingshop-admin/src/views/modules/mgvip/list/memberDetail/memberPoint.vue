<template>
  <div>
    <el-table
      width="100%"
      :data="dataList"
      border=""
      v-loading="mixinViewModuleOptions.dataListLoading"
      style="width: 100%"
      @selection-change="dataListSelectionChangeHandle">
      <el-table-column prop="pointDesc" label="积分来源" align="center"></el-table-column>
      <el-table-column prop="pointValue" label="积分变化" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.pointValue >= 0">+{{scope.row.pointValue}}</span>
          <span v-if="scope.row.pointValue < 0">{{scope.row.pointValue}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="currentValue" label="剩余积分" align="center"></el-table-column>
      <el-table-column prop="createDate" align="center" label="时间"></el-table-column>
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
import { memberPointPage } from "@/api/url";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: memberPointPage+'?pointType=1',
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


