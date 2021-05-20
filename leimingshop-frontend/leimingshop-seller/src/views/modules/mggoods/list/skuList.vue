<template>
  <div>
    <Bread :breaddata="breaddata" @changePage="changePage" :index="'2'"></Bread>
    <el-tabs v-model="activeName" @tab-click="handleClick" class="botttomGapPadding">
      <el-tab-pane label="全部" name="skuFirst">
        <skuListItem ref="firstSkuList" @showListFn="showListFn"></skuListItem>
      </el-tab-pane>
      <el-tab-pane label="下架" name="skuSecond">
        <skuListItem ref="secondSkuList" @showListFn="showListFn"></skuListItem>
      </el-tab-pane>
      <el-tab-pane label="上架" name="skuThird">
        <skuListItem ref="thirdSkuList" @showListFn="showListFn"></skuListItem>
      </el-tab-pane>
      <el-tab-pane label="未上架" name="skuFour">
        <skuListItem ref="fourSkuList" @showListFn="showListFn"></skuListItem>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>

	import Bread from "@/components/bread";
	import skuListItem from "./skuListItem.vue"

	export default {
		data() {
			return {
				breaddata: ["商品", "商品管理", "商品列表", "sku列表"],
				activeName: 'skuFirst',
				dataForm: {
					specShow: '',//规格上下架状态:0下架;1上架;2未上架
					goodsId: '',//商品id
				}
			}
		},
		components: {
			Bread,
			skuListItem
		},
		created() {
		},
		methods: {
			init(row) {
				this.dataForm.goodsId = row.id
				this.dataForm.goodsName = row.goodsName
				this.$nextTick(() => {
					this.handleClick();
				})
			},
			changePage() {
				this.showListFn();
			},
			handleClick(tab, event) {
				if (tab) {
					this.tab = tab;
				} else {
					tab = this.tab ? this.tab : {name: "skuFirst"};
				}
				// console.log(tab.name);
				if (tab.name == "skuFirst") {
					this.dataForm.specShow = ""//全部
					this.$refs.firstSkuList.init(this.dataForm);
				} else if (tab.name == "skuSecond") {
					this.dataForm.specShow = "0"//下架
					this.$refs.secondSkuList.init(this.dataForm);
				} else if (tab.name == "skuThird") {
					this.dataForm.specShow = "1"//上架
					this.$refs.thirdSkuList.init(this.dataForm);
				} else if (tab.name == "skuFour") {
					this.dataForm.specShow = "2"//未上架
					this.$refs.fourSkuList.init(this.dataForm);
				}
			},
			showListFn() {
				this.$emit("showListFn");
			}
		}
	}
</script>
<style lang="scss" scoped>
  /* .el-scrollbar__wrap {
    overflow-x: hidden;
  } */
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
  }

  /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
  *::-webkit-scrollbar-track {
    background-color: #f0f6ff;
  }

  /*定义滚动条轨道 内阴影+圆角*/
  *::-webkit-scrollbar-thumb {
    background-color: #e3e4e4;
    border-radius: 6px;
  }

  /*定义滑块 内阴影+圆角*/
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

  .grayBtn {
  }
</style>
