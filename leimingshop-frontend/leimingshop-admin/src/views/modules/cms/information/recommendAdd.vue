<template>
  <el-dialog @close="close()" :before-close="close" :visible.sync="visible" :title="'添加推荐'"
             :close-on-click-modal="false" :close-on-press-escape="false" width="80%">
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

      <el-form-item>
        <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
        <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
      </el-form-item>
    </el-form>

    <el-form>
      <el-form-item>
        <el-button type="primary" @click="addRecommends()">添加</el-button>
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
      <el-table-column prop="commentFlag" label="是否允许评论" header-align="center" align="center" width="110">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.commentFlag==0">禁止</el-tag>
          <el-tag type="success" v-else>允许</el-tag>
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
  </div>
  </el-dialog>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import Bread from "@/components/bread";
import { informationAddRecommendListUrl,
	saveInformationRecommendUrl,
	informationClassTreeUrl} from '@/api/url'
export default {
  mixins: [mixinViewModule],
  data() {
    return {
			visible:false,
      mixinViewModuleOptions: {
        getDataListURL: informationAddRecommendListUrl,
        getDataListIsPage: true,
      },
			recommendArticleIds:[],
			acId:[],
			acOption:[],
			checkAll: false,
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
      breaddata: ["运营管理", "资讯管理", "资讯文章", "资讯相关推荐", "添加推荐"],
			commentFlagList:[
				{id:'',sgName:'全部'},
				{id:0,sgName:'禁止'},
				{id:1,sgName:'允许'}
			],
    };
  },
  components: {
    Bread
  },
  created() {
		this.visible=true;
  	this.dataForm.id = this.$route.params.id;
      // 加载栏目分类数据
      this.classList();
      this.getData();
  },
  methods: {
		dataListSelectionChangeHandle(val){
			this.recommendArticleIds = [];
			val.forEach((item,index)=>{
				this.recommendArticleIds.push(item.articleId)
			})
		},
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
    close(){
			this.visible=false;
			this.$parent.addRecommendVisible=false;
			this.$parent.getData();
    },


		//确认添加相关推荐
		addRecommends() {
			if(this.recommendArticleIds.length == 0){
				this.$message({
					message: '请选择要推荐的文章',
					type: 'warning'
				});
				return;
      }
				this.$confirm(`确定添加推荐？`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning",
					center: true
				}).then(() => {
					var obj = {};
					obj.mainArticleId = this.dataForm.id;
					obj.recommendArticleIds = this.recommendArticleIds;
					this.$http.post(saveInformationRecommendUrl, obj).then(res => {
						if (res.data.code == 200) {
							this.$message.success({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.getData();
								}
							});
						} else {
							this.$message.info(res.data.msg);
						}
					});
				});
			},



  }
};
</script>
