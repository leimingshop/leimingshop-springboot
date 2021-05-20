<template>
  <div>
    <Bread :breaddata="breaddata" :index="index" @changePage="changePage"></Bread>
    <div class="fullTitle title-1">
      活动信息
    </div>
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <div class="label-box">拼团记录ID：</div>
          <div class="text-box">{{dataFormData.id}}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <div class="grid-content bg-purple">
            <div class="label-box">拼团活动ID：</div>
            <div class="text-box">{{dataFormData.groupId}}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <div class="grid-content bg-purple">
            <div class="label-box">活动名称：</div>
            <div class="text-box">{{dataFormData.groupName}}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <div class="label-box">发起时间：</div>
          <div class="text-box">{{dataFormData.startTime}}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <div class="grid-content bg-purple">
            <div class="label-box">截止时间：</div>
            <div class="text-box">{{dataFormData.overTime}}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
        <el-col :span="6">
            <div class="grid-content bg-purple">
                <div class="grid-content bg-purple">
                    <div class="label-box">拼团状态：</div>
                    <div class="text-box" v-if="dataFormData.groupStatus == 0">拼团中</div>
                    <div class="text-box" v-if="dataFormData.groupStatus == 1">拼团成功</div>
                    <div class="text-box" v-if="dataFormData.groupStatus == 2">拼团失败</div>
                </div>
            </div>
        </el-col>
      <el-col :span="6" v-if="dataFormData.groupStatus == 0">
        <div class="grid-content bg-purple">
          <div class="label-box">成团人数：</div>
          <div class="text-box">{{dataFormData.needNum}}</div>
        </div>
      </el-col>
    </el-row>


    <div class="fullTitle title-1">
      拼团信息
    </div>
    <div class="table-box">
      <el-table
          width="100%"
          :data="dataFormData.groupOrderListDTOList"
          border
          v-loading="dataListLoading"
          style="width: 100%">
        <el-table-column label="商品信息" align="center" width="330">
          <template slot-scope="scope">
            <div class="goodsPropsWrap">
              <div class="goodsImg">
                <img :src="scope.row.goodsImage | filterImgUrl" alt=""/>
              </div>
              <div class="goodsProps">
                            <span class="goodsName">
<!--                                <label class="goodsNameTitle">名称:</label>-->
                                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                                            placement="top-start">
                                          <span class="goodsNameContent" @click="previewH5Fn(scope.row)">{{scope.row.goodsName}}</span>
                                </el-tooltip>
                            </span>
                <!--                            <span class="goodsBrand">-->
                <!--                                <label class="goodsNameTitle">品牌:</label>-->
                <!--                                <span class="goodsBrandName">{{scope.row.brandName}}</span>-->
                <!--                            </span>-->
                <span class="goodsNum">
<!--                                <label class="goodsNameTitle">SPU:</label>-->
                                <span class="goodsBrandName">{{scope.row.specInfo}}</span>
                            </span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="单价/数量" width="140" align="right">
          <template slot-scope="scope">
            <div class="priceWrap">
              <div class="price1">￥{{scope.row.specPayPrice}}</div>
              <div class="price2">{{scope.row.goodsNum}}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="买家" align="center" width="120">
          <template slot-scope="scope">
            <div>
              {{scope.row.buyerName}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="paymentName" label="支付方式" align="center" width="100"></el-table-column>
        <el-table-column prop="gcName" label="参团时间" width="180" align="center">
          <template slot-scope="scope">
            <div>
              {{scope.row.paymentTime}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="orderAmount" label="订单金额" align="center" width="120">
          <template slot-scope="scope">
            <div>
              ￥{{scope.row.orderAmount}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="gcName" label="订单状态" align="center" width="130">
          <template slot-scope="scope">
            <el-tag type="info" v-if="scope.row.orderStatus==0">已取消</el-tag>
            <el-tag type="info" v-if="scope.row.orderStatus==10">待付款</el-tag>
            <el-tag type="info" v-if="scope.row.orderStatus==20">待发货</el-tag>
            <el-tag type="info" v-if="scope.row.orderStatus==30">待收货</el-tag>
            <el-tag type="success" v-if="scope.row.orderStatus==40">交易完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="viewDetails(scope.row)" type="text" size="mini" >查看详情</el-button>
<!--            <el-button @click="remarks(scope.row)" type="text" size="mini" v-if="$hasPermission('sys:goods:sku:view')">备注</el-button>-->
          </template>
        </el-table-column>
      </el-table>

    </div>
    <div class="btn-box">
      <el-button class="btn" plain @click="changePage">返回</el-button>
      <el-button v-if="dataFormData.groupStatus ==0" class="btn" type="primary" plain @click="fastGroup">快速成团</el-button>
      <el-button v-if="dataFormData.groupStatus ==0" class="btn" type="danger" plain @click="cancelOrder">取消订单</el-button>
    </div>


    <modelCancleOrder
        v-show="cancleOrderVisible"
        ref="cancleOrderCompon"
        @searchDataList="changePage"
    ></modelCancleOrder>
  </div>
</template>

<script>
	import Bread from "@/components/bread";
	import {getGroupRecordDetails,groupComposition,groupOrderCancel} from '@/api/api'
  import modelCancleOrder from "./model-cancle-order";

	export default {
		name: "details",
		data() {
			return {
				dataFormData: {
					groupOrderListDTOList: []
				},
				cancleOrderVisible:false,
				loading: true,
				dataListLoading: false,
				listItem: {},
				groupRecordId:'',
			}
    },
    props:['breaddata','index'],
		components: {
			Bread,
			modelCancleOrder
		},
		methods: {
			init(groupRecordId) {
				this.groupRecordId = groupRecordId;
				var obj = {
					id: groupRecordId
				}
				getGroupRecordDetails(obj).then((res => {
					if (res.code == 200) {
						this.dataFormData = res.data
						this.loading = false
					}
				}))
			},
			viewDetails(row) {
				this.$router.push({
          path:'/order-grouplist',
          query: {
          	tabIndex:2,
						orderItem:row,
            orderId:row.id,
          }
				})
			},
			remarks(row) {

      },
      previewH5Fn(row){
        window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?specId='+row.specId);
      },
			fastGroup() {
				this.$confirm('您确认快速成团吗?', '提示', {
					cancelButtonText: '取消',
					confirmButtonText: '确定',
					type: 'warning'
				}).then((e) => {
					var obj = {
						groupRecordId: this.groupRecordId
					}
					groupComposition(obj).then((res => {
						if (res.code == 200) {
							this.$message({
								message: res.msg,
								type: 'success',
								duration: 1500,
							})
							this.changePage() // 刷新列表数据
						} else {
							this.$message({
								message: res.msg,
								type: 'error',
								duration: 1500,
							})
						}
					}))
				}).catch((e) => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});
			},
			cancelOrder() {
				this.cancleOrderVisible = true;
				this.$nextTick(() => {
					this.$refs.cancleOrderCompon.init(this.groupRecordId);
				});
			},
			changePage() {
				this.$emit('changePage')
			}
		}
	}
</script>

<style lang="scss" scoped>
  .bg-purple {
    display: flex;
    line-height: 40px;
    height: 40px;

    .label-box {
      width: 120px;
      text-align: right;
    }
  }

  .fullTitle {
    font-weight: 700;
    height: 40px;
    line-height: 40px;
  }

  .btn-box {
    margin-top: 30px;
    text-align: center;
  }

  // 商品
  .goodsPropsWrap {
    margin: auto;
    height: 80px;
    width: 330px;
    display: flex;
    justify-content: space-around;
    align-items: center;

    .goodsImg {
      width: 70px;
      height: 70px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .goodsProps {
      width: 210px;
      height: 70px;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: center;
      color: #999999;

      .goodsNameTitle {
        display: inline-block;
        width: 50px;
        color: #666666FF;
      }

      .goodsBrandName {
        text-align: left;
      }

      .goodsName {
        display: flex;
        align-items: center;
        justify-content: center;

        .goodsNameContent {
          width: 150px;
          text-align: left;
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          -webkit-box-orient: vertical;
          color: #4e80db;
          text-decoration: none;
          cursor: pointer;
        }

      }
    }
  }

  // 价格
  .priceWrap {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 100%;

    .price1 {

    }

    div {
      width: 100%;
      text-align: center;
    }

  }


  // 操作
  .btnWrap {
    display: flex;
    justify-content: space-around;
    align-items: center;

    .editWrap {
      cursor: pointer;
    }

    .skuWrap {
      cursor: pointer;
    }

    .btsvg {
      margin-right: 3px;
    }
  }
</style>
