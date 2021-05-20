<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
    <el-form
      :inline="true"
      class="grayLine topGapPadding"
      :model="dataForm"
      ref="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item label="等级名称：" prop="gradeName">
        <el-input v-model="dataForm.gradeName" placeholder="等级名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList()">查询</el-button>
        <el-button @click="reset()" type="primary"  plain>重置</el-button>
      </el-form-item>
      <br />
    </el-form>
    <el-form>
    	<el-form-item>
        <el-button type="primary" @click="deal">新增等级</el-button>
      </el-form-item>
    </el-form>
    <el-table width="100%" :data="dataList" border="">
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
      <el-table-column prop="gradeName" align="center" label="等级名称"></el-table-column>
      <el-table-column prop="integration" align="center" label="等级分值">
        <template slot-scope="scope">{{scope.row.integration +'分值以上'}}</template>
      </el-table-column>
      <el-table-column prop="memeberNum" align="center" label="人数"></el-table-column>
      <el-table-column label="操作" width="400" align="center">
        <template slot-scope="scope">
          <el-button @click="goDetail(scope.row.id,false)" type="text" size="mini">查看</el-button>
          <el-button @click="goDetail(scope.row.id,true)" type="text" size="mini">编辑</el-button>
          <el-button class="artdanger" @click="deleteHandle(scope.row.id)" type="text" size="mini">删除</el-button>
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
    <!-- 查看 编辑  新增-->
    <el-dialog :title="changeTit" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="等级名称：" :label-width="formLabelWidth" v-if="isChange">
          <el-input v-model="form.gradeName" autocomplete="off" style="width:61%" readonly></el-input>
        </el-form-item>
        <el-form-item label="等级名称：" :label-width="formLabelWidth" v-else prop="gradeName">
          <el-input v-model="form.gradeName" autocomplete="off" style="width:61%"></el-input>
        </el-form-item>
        <el-form-item label="等级分值：" :label-width="formLabelWidth" v-if="isChange">
          <el-input v-model="form.integration" autocomplete="off" style="width:30%" readonly></el-input>--
          <el-input v-model="form.maxIntegration" autocomplete="off" style="width:30%" readonly></el-input>
        </el-form-item>
        <el-form-item label="等级分值：" :label-width="formLabelWidth" v-else prop="integration">
          <el-input-number v-model="form.integration" :min="0"></el-input-number>
          <span class="tip">不小于该等级分值</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="isChange">
        <el-button plain @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button plain @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeScore">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import { memberGrade, gradeDel } from "@/api/url";
import { greadDet, changeGrade, addSavaGrade } from "@/api/api";
import Bread from "@/components/bread";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: memberGrade,
        getDataListIsPage: true,
        deleteURL: gradeDel,
        deleteIsBatch: false,
        dataListLoading: false,
        deleteIsBatch: true,
        deleteIsBatchKey: "ids"
      },
      formLabelWidth: "120px",
      isChange: false,
      changeTit: "", //弹窗标题
      dialogFormVisible: false,
      breaddata: ["会员管理", "会员管理", "会员等级"],
      dataForm: {},
      form: {
        gradeName: "",
        integration: ""
      },
      rules: {
        gradeName: [
          { required: true, message: "请输入会员名称", trigger: "blur" }
        ],
        integration: [
          { required: true, message: "请选择等级分值", trigger: "change" }
        ]
      }
    };
  },
  created() {},
  methods: {
    goDetail(id, states) {
      this.form.id = id;
      console.log(id, states, "查看或编辑");
      if (states) {
        this.isChange = false;
        this.changeTit = "会员编辑";
      } else {
        this.isChange = true;
        this.changeTit = "会员详情";
      }
      const obj = { id: id };
      greadDet(obj).then(res => {
        if (res.code == 200) {
          this.dialogFormVisible = !this.dialogFormVisible;
          this.form = res.data;
        } else {
          this.$message({
            message: res.msg,
            type: "error",
            duration: 1000
          });
        }
      });
    },
    //重置
    reset() {
      this.dataForm.gradeName = "";
      this.getDataList();
    },
    //修改等级
    changeScore() {
      const obj = {
        id: this.form.id,
        gradeName: this.form.gradeName,
        integration: this.form.integration
      };
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id == "") {
            console.log("kong");
            addSavaGrade(obj).then(res => {
              if (res.code == 200) {
                this.$message({
                  message: res.msg,
                  type: "success",
                  duration: 1000
                });
                this.dialogFormVisible = false;
                this.getDataList();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                  duration: 1000
                });
                this.dialogFormVisible = false;
              }
            });
          } else {
            changeGrade(obj).then(res => {
              if (res.code == 200) {
                this.$message({
                  message: res.msg,
                  type: "success",
                  duration: 1000
                });
                this.dialogFormVisible = false;
                this.getDataList();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                  duration: 1000
                });
                this.dialogFormVisible = false;
              }
            });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //新增等级
    deal() {
      this.form = {};
      this.form.id = "";
      this.changeTit = "新增会员";
      this.dialogFormVisible = true;
    }
  },
  components: { Bread }
};
</script>
<style lang="scss" scoped>
.tip {
  margin-left: 15px;
  display: inline-block;
}
</style>
