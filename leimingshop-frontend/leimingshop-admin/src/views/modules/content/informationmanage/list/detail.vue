<template>
  <div>
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
		<el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" ref="dataForm" @keyup.enter.native="getDataList()" >
				<el-form-item label="输入搜索:" >
					<el-input v-model="dataForm.receiveName" placeholder="发件人/收件人名称" clearable></el-input>
				</el-form-item>
				<el-form-item label="查看状态:">
						<el-select
							v-model="dataForm.status"
							placeholder="请选择"
							loading-text="加载中···"
							@visible-change="getGoodKind2">
							<el-option
									v-for="item in goodKindList1"
									:key="item.id"
									:label="item.name"
									:value="item.id">
							</el-option>
						</el-select>
				</el-form-item>
				<el-form-item label="读取时间:">
						<el-date-picker
							v-model="valuetime"
							type="daterange"
							range-separator="-"
							start-placeholder="开始日期"
							end-placeholder="结束日期"
							value-format="yyyy-MM-dd"
							@blur='acttime'>
					</el-date-picker>
				</el-form-item>
			<el-form-item>
					<el-button  class="btn" type="primary" @click="getDataList()">查询</el-button>
					<el-button  class="btn"  @click="reset('dataForm')" type="primary" plain>重置</el-button>
			</el-form-item>
			<br />
		</el-form>

		  <el-form>
      	<el-form-item>
           <el-button type="danger" plain @click="deleteHandle()" >批量删除</el-button>
        </el-form-item>
      </el-form>
		<el-table
			:data="dataList"
			ref='dataList'
			border=""
			v-loading="dataListLoading"
			style="width: 100%"
			@selection-change="dataListSelectionChangeHandle">
			<el-table-column
					type="selection"
					align="center"
					width="70">
			</el-table-column>
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
				<el-table-column
					prop="sendName"
					label="发送者"
					align="center">
				</el-table-column>
				<el-table-column
					prop="status"
					label="查看状态"
					align="center"
					width="120"
					:formatter="formatstatus">
				</el-table-column>
			<el-table-column
					prop="messageContent"
					align="center"
					label="信中内容">
				<template slot-scope="scope">
					<div>{{messageContentHandle(scope.row)}}</div>
				</template>
			</el-table-column>
				<el-table-column
					prop="updateDate"
					align="center"
					label="读取时间"
          width="180">
				</el-table-column>
				<el-table-column
					prop="receiveName"
					align="center"
					label="接收人">
				</el-table-column>
				<el-table-column
					prop="address"
					align="center"
					width="120"
					label="操作">
					<template slot-scope="scope">
						<el-button class="artdanger" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
		</el-table>

			<!-- 分页 -->
			<el-pagination
				@size-change="sizeChangeHandle"
				@current-change="currentChangeHandle"
				:current-page="params.currentPage"
				:page-sizes="[10, 20, 50, 100]"
				:page-size="params.currentPageSize"
				:total="total"
				layout="total, sizes, prev, pager, next, jumper">
			</el-pagination>

	  <div style="margin-top: 40px;margin-bottom: 40px">
		  <el-button type="primary" style="display:block;margin:0 auto" @click="changePage()">返回</el-button>
	  </div>

  </div>
</template>

<script>
//import list from "./list.vue"
import mixinViewModule from '@/mixins/view-module'
import { informationlist,informationdelete } from '@/api/url'
import Bread from "@/components/bread";
export default {
  mixins: [mixinViewModule],
	data () {
	    return {
	      mixinViewModuleOptions: {
	          getDataListURL: informationlist,
	          getDataListIsPage: true,
	          // exportURL: '/admin-api/log/login/export',
	          deleteURL: informationdelete,
	          dataListLoading: false,
	          deleteIsBatch: true,
	          deleteIsBatchKey: 'id'
				},
				breaddata: ["内容管理", "站内信管理", "站内信列表", "查看"],
	      goodKind2loading:false,
	      goodKind3loading:false,
	      dataForm: {
	      	receiveName:'',
	      	status:'',
	      	strTime: '',
					endTime:'',
	        messageId: "",
			  messageContent:""
	      },
	      params:{
	      "currentPage": 1, //当前页数
	      "currentPageSize" : 10, //每页显示的条数
	      },
	      tableData: [],
	      goodKindList1: [{ id: '', name: "全部" },{ id: 1, name: "已读" },{ id: 0, name: "未读" }],
	      activeName: "first",
	      tableData1: [],
	      tableData2: [],
	      tableData3: [],
	      tableData4: [],
	      valuetime: '',
	    };
	},
	created() {
		this.dataForm.status = this.goodKindList1[0].id;
	},
	components: {
	 Bread
	},
	methods: {
		init(row){
			this.$nextTick(()=>{
				this.dataForm.messageId = row.id;
				this.getDataList();
			})
		},
    reset() {
    	this.dataForm.receiveName = '';
    	this.dataForm.status = this.goodKindList1[0].id;
    	this.dataForm.strTime = '';
    	this.dataForm.endTime = '';
    	this.valuetime = '';
    	this.getDataList();
    },
    getGoodKind2() {},
    getGoodKind3() {},
    handleClick(tab, event) {
      console.log(tab.name);
    },
    changePage(){
    	this.$emit("showList");
    },
		messageContentHandle(item) {
			var reg = /[\u4e00-\u9fa5+\uff1f]/g;
			return item.messageContent.match(reg).join("");

		},
    //查看状态
    formatstatus(row,column){
    	return row.status  == 1 ?  <el-tag type="success">已读</el-tag>: <el-tag type="info">未读</el-tag>;
    },
		// 每页数
		sizeChangeHandle (val) {
			this.params.currentPageSize = val;
			this.params.currentPage = 1;
			this.limit = val;
			this.getDataList();
		},
		// 当前页
		currentChangeHandle (val) {
			this.params.currentPage = val;
			this.page = val;
			this.getDataList()
		},
		//开始结束时间
    acttime(){
    	this.dataForm.strTime = this.valuetime[0];
    	this.dataForm.endTime = this.valuetime[1];
    },
   }
};
</script>
<style lang="scss" scoped>
/* .el-scrollbar__wrap {
  overflow-x: hidden;
} */
@import "@/element-ui/theme-variables.scss";
// 表头背景和字体颜色
/deep/ .el-table__header  th{
  background:#f5f7fa;
}
.el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
  white-space: nowrap;
}
.el-form--inline .el-form-item {
  margin-right: 76px;
}
*::-webkit-scrollbar {
  width: 7px;
  height: 1px;
  background-color: transparent;
} /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
*::-webkit-scrollbar-track {
  background-color: #f0f6ff;
} /*定义滚动条轨道 内阴影+圆角*/
*::-webkit-scrollbar-thumb {
  background-color: #e3e4e4;
  border-radius: 6px;
} /*定义滑块 内阴影+圆角*/
.scrollbarHide::-webkit-scrollbar {
  display: none;
}
.scrollbarShow::-webkit-scrollbar {
  display: block;
}

.el-input {
  width: 170px;
  height: 40px;
}
/deep/ .el-tabs__nav-wrap {
  border-bottom: 2px #efefef dotted;
  &::after {
    content: unset;
  }
}
/deep/ .el-tabs__nav-scroll {
  height: 60px;
  line-height: 60px;
  /deep/ .el-tabs__item {
    width: 120px;
    // box-sizing: border-box;
    text-align: center;
  }
}
/deep/ .el-tabs__active-bar {
  // width: 120px !important;
}


</style>

