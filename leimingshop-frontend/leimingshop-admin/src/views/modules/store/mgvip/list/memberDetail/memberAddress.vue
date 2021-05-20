<template>
  <div>
    <el-table
      width="100%"
      :data="dataList"
      border=""
      v-loading="mixinViewModuleOptions.dataListLoading"
      style="width: 100%"
      @selection-change="dataListSelectionChangeHandle"
    >
      <el-table-column prop="trueName" label="姓名" align="center"></el-table-column>
      <el-table-column prop="mobPhone" label="手机号" align="center"></el-table-column>
      <el-table-column prop="provinceId" label="省份" align="center"></el-table-column>
      <el-table-column prop="cityId" align="center" label="市"></el-table-column>
      <el-table-column prop="areaId" align="center" label="区"></el-table-column>
      <el-table-column prop="address" align="center" label="街道"></el-table-column>
      <el-table-column prop="memberSource" align="center" label="邮编"></el-table-column>
      <el-table-column label="操作" align="center" min-width="150">
        <template slot-scope="scope">
          <!-- <el-button @click.native.prevent="goDetail(scope.row)" type="primary" size="mini">编辑</el-button> -->
          <el-button
            @click.native.prevent="goDefault(scope.row)"
            v-if="scope.row.defaultFlag!==1"
            type="primary"
            size="mini"
          >设为默认</el-button>
          <el-button
          	class="artdanger"
            @click.native.prevent="deleteHandle(scope.row.id)"
            type="danger"
            plain
            size="mini"
          >删除</el-button>
        </template>
      </el-table-column>
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
import { memberAddress, deleteAddress } from "@/api/url";
import { memberAdd, setDefault } from "@/api/api";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: memberAddress,
        getDataListIsPage: true,
        deleteURL: deleteAddress,
        deleteIsBatch: false,
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "ids"
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
    },
    aaa() {
      console.log(this.dataList);
      console.log(this.row.id);
    },
    //设置默认
    goDefault(index) {
      const obj = {
        id: index.id,
        memberId: index.memberId,
        defaultFlag: index.defaultFlag
      };
      setDefault(obj).then(res => {
        if (res.code == 200) {
          this.$message({
            message: res.msg,
            type: "success",
            duration: 1000
          });
          this.dataForm.memberId = "";
        } else {
          this.$message({
            message: res.msg,
            type: "error",
            duration: 1000
          });
        }
        this.isChange = true; //watch刷新地址列表数据
      });
    },
    //编辑
    goDetail(){
      console.log('Edit')
    }
  }
};
</script>
<style scoped>
</style>


