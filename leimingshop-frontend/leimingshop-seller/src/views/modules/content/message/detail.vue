<template>
  <div v-loading="loading">
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
	 <div style="text-align: center; font-size: 30px; color: rgb(51, 51, 51); font-weight: bold;">
      <span>{{this.dataForm.messageTitle}}</span>
    </div>

   <div style="margin-top: 20px;">
      <span style="font-size:15px;color:#333;font-weight: bold;margin-left: 25%;margin-top: 20px; }">发件人：</span>
      <span>{{ this.dataForm.sendName}}</span>
	  <span style="font-size:15px;color:#333;font-weight: bold;margin-left: 30%;">发送时间：</span>
      <span>{{ this.dataForm.sendTime}}</span>
	</div> 

    <div style="margin-left: 25%;margin-top: 20px;">
      <div v-html="dataForm.messageContent" style="width: 815px;" class="ql-editor"></div>

	</div> 
	<div style="margin-left: 50%;margin-top: 30px;">
         <el-button  type="primary" size="big" style="with:500px" @click="changePage()">
         返回
        </el-button>
	</div> 
  </div>
</template>

<script>
import list from "./list.vue"
import mixinViewModule from '@/mixins/view-module'
import { messageInfo } from '@/api/api'
import Bread from "@/components/bread";
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
export default {
  mixins: [mixinViewModule],
	data () {
	    return {
	      mixinViewModuleOptions: {
	          getDataListURL: '',
	          getDataListIsPage: true,
	          // exportURL: '/admin-api/log/login/export',
	          deleteURL: '',
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
	      },
	      params:{
	      "currentPage": 1, //当前页数
	      "currentPageSize" : 10, //每页显示的条数
	      },
	      tableData: [],
	      goodKindList1: [{ id: '', name: "全部" },{ id: 1, name: "已读" },{ id: 0, name: "未读" }],
	      loading:false
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
			 var obj = {
                id:row
			}
			this.loading = true;
			messageInfo(obj).then((res)=>{
				this.loading=false;
                if(res.code == 200){
                  this.dataForm=res.data
                }
            })
			})
		},
		goback(){

		},


    handleClick(tab, event) {
      console.log(tab.name);
    },
    changePage(){
    	this.$emit("showList");
    },
    //查看状态
    formatstatus(row,column){
    	return row.status  == 1 ?  <el-tag type="success">已读</el-tag>: <el-tag type="info">未读</el-tag>;
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
.artcon{
			/deep/p{
				font-size: 24upx;
				text-indent: 2em;
			}
		}

.ql-editor{
	    padding: 12px 0px;
}
</style>

