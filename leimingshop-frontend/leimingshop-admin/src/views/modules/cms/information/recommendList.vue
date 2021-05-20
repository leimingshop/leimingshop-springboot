<template>
  <div>
    <Bread :breaddata="breaddata"></Bread>
    <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getData()">
      <el-form-item label="标题：">
        <el-input v-model="dataForm.articleTitle" placeholder="标题" clearable></el-input>
      </el-form-item>
      <el-form-item label="文章所属：">
        <el-input v-model="dataForm.articleBelong" placeholder="文章所属" clearable></el-input>
      </el-form-item>
      <el-form-item label="作者：">
        <el-input v-model="dataForm.author" placeholder="作者" clearable></el-input>
      </el-form-item>
      <el-form-item label="文章分类：">
        <el-cascader
                v-model="acId"
                :options="acOption"
                @change="caeChange"
                :props="defaultParams"
                change-on-select
                :show-all-levels = "false"
                clearable></el-cascader>
      </el-form-item>
      <el-form-item  label="是否显示：">
        <el-select v-model="dataForm.status" placeholder="请选择">
          <el-option
            v-for="item in statusList"
            :key="item.id"
            :label="item.sgName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item  label="是否允许评论：">
        <el-select v-model="dataForm.commentFlag" placeholder="请选择">
          <el-option
            v-for="item in commentFlagList"
            :key="item.id"
            :label="item.sgName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item  label="审核状态：">
        <el-select v-model="dataForm.audit" placeholder="请选择">
          <el-option
            v-for="item in auditList"
            :key="item.id"
            :label="item.sgName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
        <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
      </el-form-item>
    </el-form>

    <el-form>
    <el-form-item>
      <el-button type="primary" @click="recommendList()">添加推荐</el-button>
      <el-button type="danger" plain @click="deleteHandle()">移除推荐</el-button>
    </el-form-item>
  </el-form>

    <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
      <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
        <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
      </el-table-column>
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="articleTitle" label="标题" header-align="center" align="center"></el-table-column>
      <el-table-column prop="articleBelong" label="文章所属" header-align="center" align="center" width="150"></el-table-column>
      <el-table-column prop="author" label="作者" header-align="center" align="center" width="150"></el-table-column>
      <el-table-column prop="acName" label="文章分类" header-align="center" align="center" width="150"></el-table-column>
      <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column prop="status" label="是否显示" header-align="center" align="center" width="100">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.status==0">隐藏</el-tag>
          <el-tag type="success" v-else>显示</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="commentFlag" label="是否允许评论" header-align="center" align="center" width="110">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.commentFlag==0">禁止</el-tag>
          <el-tag type="success" v-else>允许</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="audit" label="审核状态" header-align="center" align="center" width="120">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.audit==0">未审核</el-tag>
          <el-tag type="success" v-else-if="scope.row.audit==1">审核通过</el-tag>
          <el-tag type="danger" v-else>审核驳回</el-tag>
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
    <!-- 弹窗, 新增 / 修改 -->
    <recommendAdd v-if="addRecommendVisible" ref="addRecommend" @refreshDataList="getDataList"></recommendAdd>
  </div>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import recommendAdd from "./recommendAdd";
import Bread from "@/components/bread";
import { informationRecommendPageUrl,
	deleteInformationRecommendUrl,
	informationClassTreeUrl} from '@/api/url'
export default {
  mixins: [mixinViewModule],
  data() {
    return {
			addRecommendVisible:false,
      mixinViewModuleOptions: {
        getDataListURL: informationRecommendPageUrl,
        getDataListIsPage: true,
        deleteURL: deleteInformationRecommendUrl,
        deleteIsBatch: true
      },
			acId:[],
			acOption:[],
      defaultParams: {
        label: 'acName',
        value: 'id',
        children: 'children'
      },
      dataForm: {
      	id:'',//文章ID
        articleTitle: "",//文章标题
				author:'',
        acId: [],//栏目ID
				commentFlag:'',
				status:'',
				audit:'',
      },
      breaddata: ["运营管理", "资讯管理", "资讯文章", "资讯相关推荐"],
			statusList:[
				{id:'',sgName:'全部'},
				{id:0,sgName:'隐藏'},
				{id:1,sgName:'显示'}
			],
			commentFlagList:[
				{id:'',sgName:'全部'},
				{id:0,sgName:'禁止'},
				{id:1,sgName:'允许'}
			],
			auditList:[
				{id:'',sgName:'全部'},
				{id:0,sgName:'未审核'},
				{id:1,sgName:'审核通过'},
				{id:2,sgName:'审核驳回'}
			],
    };
  },
  components: {
		recommendAdd,
    Bread
  },
  created() {
  	this.dataForm.id = this.$route.params.id;
      // 加载栏目分类数据
      this.classList();
  },
  methods: {
    getData() {
      this.page = 1;
      this.limit = 10;
      this.getDataList();
    },
    reset() {
      this.dataForm = {};
			this.dataForm.id = this.$route.params.id;
			this.acId = [];//分类ID（显示）
			this.page = 1;
			this.limit = 10;
			this.getDataList();
    },
		//文章分类集合
		classList() {
			this.$http.get(informationClassTreeUrl).then((res) => {
				if(res.data.code == 200) {
					// 将获得到的数据赋值给options
					this.acOption=res.data.data;
				}
			});
		},
    //改变acId参数格式
    caeChange(item) {
			this.dataForm.acId = item[item.length-1];
			this.dataForm.acId = this.dataForm.acId.toString();
    },

    //添加相关推荐
		recommendList(){
				this.addRecommendVisible = true
				this.$nextTick(() => {
					this.$refs.addRecommend.dataForm.id = this.dataForm.id
				})
    },

  }
};
</script>
