<template>
  <div>
    <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
    <el-form :inline="true" :model="dataFormTemp" class="addOrEdit" :rules="dataRule" ref="dataFormTemp"
             v-loading="couponLoading">
      <div class="fullTitle title-1">
        基本信息
      </div>
      <el-form-item label="活动名称：" prop="groupName">
        <el-input
            maxlength="20"
            show-word-limit
            v-model.trim="dataFormTemp.groupName" placeholder="请输入活动名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="活动时间：" class="specSize" prop="startTime">
        <el-date-picker
            v-model="dataFormTemp.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm"
			:picker-options="pickerOptionsStart"
            placeholder="开始日期"
        >
        </el-date-picker>
        <span style="margin: 0px 5px;">~</span>
        <el-date-picker
            v-model="dataFormTemp.endTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm"
            placeholder="结束日期"
			:picker-options="pickerOptionsEnd"
        >
        </el-date-picker>
      </el-form-item>
      <div class="hrTips">拼团到期后将自动失效，失效后不可延长</div>

      <el-form-item label="参团用户条件：" prop="joinFlag">
        <el-radio-group :disabled="true" v-model="dataFormTemp.joinFlag">
          <el-radio label="0">无限制</el-radio>
          <el-radio label="1">新用户</el-radio>
        </el-radio-group>
      </el-form-item>
      <div class="hrTips">只限制参团用户，对开团用户不受影响</div>

      <el-form-item label="活动预热：" prop="groupPreheat">
        活动开始前
        <el-input v-model="dataFormTemp.groupPreheat">
          <span slot="suffix">小时</span>
        </el-input>
        商品详情页显示活动信息
      </el-form-item>
      <div class="hrTips">开启预热后，会在商品详情页，显示活动基本信息，仅支持正整数</div>

      <el-form-item label="推荐拼团：" prop="recommendFlag">
        <el-checkbox v-model="dataFormTemp.recommendFlag">开启</el-checkbox>
      </el-form-item>
      <div class="hrTips">开启推荐拼团后，开团页面会显示其他人发起的拼团，会员可直接参与</div>
      <el-form-item label="模拟成团：" prop="simulateFlag">
        <el-checkbox v-model="dataFormTemp.simulateFlag">开启</el-checkbox>
      </el-form-item>
      <div class="hrTips">开启模拟成团后，拼团有效期内人数未满的团，系统将会模拟“匿名买家”凑满人数，使该团成团</div>

      <div class="fullTitle title-1">
        优惠规则
      </div>
      <el-form-item label="成团有效时间：" prop="validTime">
        <el-input v-model="dataFormTemp.validTime">
          <span slot="suffix">小时</span>
        </el-input>
      </el-form-item>
      <div class="hrTips">不超过72小时</div>
      <el-form-item label="订单支付有效期：" prop="payEndTime">
        <el-input v-model="dataFormTemp.payEndTime">
          <span slot="suffix">分</span>
        </el-input>
      </el-form-item>
      <div class="hrTips">优先级高于平台规定的自动取消时间</div>
      <!--      三期暂时不做-->
      <!--      <el-form-item label="下单使用优惠：" prop="totalNum">-->
      <!--        <el-input v-model="dataFormTemp.validityDays" >-->
      <!--          <span slot="suffix">分</span>-->
      <!--        </el-input>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="下单可用抵扣：" prop="totalNum">-->
      <!--        <el-input v-model="dataFormTemp.validityDays" >-->
      <!--          <span slot="suffix">分</span>-->
      <!--        </el-input>-->
      <!--      </el-form-item>-->

    </el-form>
	      <div style="text-align: center;">
        <el-button class="btn" type="primary" @click="dataFormSubmit('dataFormTemp')" :loading="saveLoading">
          {{saveLoading?"提交中...":"确认"}}
        </el-button>
        <el-button class="btn" type="primary" @click="savaAndSub('dataFormTemp')" :loading="saveAddLoading">
          {{saveAddLoading?"提交中...":"保存并添加商品"}}
        </el-button>
        <el-button class="btn" type="primary" plain @click="changePage()" plain>取消</el-button>
      </div>
  </div>
</template>

<script>
	import Bread from "@/components/bread";
	import {addGroup, editGroup, groupDetails, storeBrandList,getOrderChangeDetailApi} from '@/api/api'

	export default {
		name: "model-add-or-edit",
		data() {
			let that = this
			// 活动时间必须事整点或者半点
			var integerTime = (rule, value, callback) => {
				if (that.dataFormTemp.startTime ) {
					let start = new Date(that.dataFormTemp.startTime).getMinutes()
          if(start !== 0 && start !== 30){
						return callback(new Error('活动时间必须是整点或者半点'))
          }
				}
				if(that.dataFormTemp.startTime){
					let d = new Date();
					let month = d.getMonth() > 8 ? d.getMonth() + 1 : '0' + (d.getMonth() + 1)
					let day = d.getDate()<10?'0'+d.getDate():d.getDate()
                	let date = d.getFullYear() + '-' + month + '-' + day
                	let hour = d.getHours() > 9 ? d.getHours() : '0' + d.getHours()
                	let minute = d.getMinutes() > 9 ? d.getMinutes() : '0' + d.getMinutes()
					let time = date + ' ' + hour + ':' + minute + ':00'
					// console.log(this.dataFormTemp.startTime,787777777)
					// console.log(time,22222)
					if(that.dataFormTemp.startTime<time){
						return callback(new Error('活动开始时间不能小于此刻的时间'))
					}
				}
				if(that.dataFormTemp.endTime){
					let end = new Date(that.dataFormTemp.endTime).getMinutes()
					if(end !== 0 && end !== 30){
						return callback(new Error('活动时间必须是整点或者半点'))
					}
        }
				callback()
			};
			//固定时间 --》 起止时间
			var startTimechange = (rule, value, callback) => {
				if (that.dataFormTemp.startTime && that.dataFormTemp.endTime) {
					let start = new Date(that.dataFormTemp.startTime).getTime()
					let end = new Date(that.dataFormTemp.endTime).getTime()
					let time = 0
					if (start > end) {
						return callback(new Error('开始时间需要小于结束时间'))
					}
				} else {
					callback(new Error('必选项不能为空'))
				}
				callback()
			};
			var validTimechange = (rule, value, callback) => {
				if (/[^\d]/.test(value) || parseInt(value) <= 0) {
					callback(new Error('有效时间只能为正整数'))
				} else if (parseInt(value) > 72) {
					callback(new Error('最多72小时'))
				}
				callback()
			};
			var payEndTimechange = (rule, value, callback) => {
				if (/[^\d]/.test(value) || parseInt(value) <= 0) {
					callback(new Error('有效时间只能为整数'))
				}
				callback()
			};
			var maxNum = (rule, value, callback) => {
                if(/[^\d]/.test(value)){
                    callback(new Error('预热时间只能为正整数'))
                } else if (parseInt(value) > 999) {
                  callback(new Error('此选项不能大于999'))
                }
				callback()
			};
			return {
				data:{},
				breaddata: [],
				//回显数据的时候loading
				couponLoading: true,
				// 新增优惠券表单
				dataFormTemp: {
					groupName: '',
					joinFlag: '0',  //参团条件（默认0无限制，1新用户）
					startTime: '',
					endTime: '',
					groupPreheat: 12,
					recommendFlag: false,
					simulateFlag: false,
					validTime: '',
					payEndTime: ''
				},
				row: '',
				//radio--可用范围
				// 指定商品
				page: 1, limit: 12, total: 0,
				goodsVisible: false,
				goodsLoading: true,
				goodsDataForm: {
					goodsName: ''
				},
				// 存返回的relationIds数据
				tempRelationIds: [],
				// 存返回的可用范围
				tempCanUse: '',
				goodsOptions: [],
				dataList: [],
				// 指定品牌数据
				brandVisible: false,
				brandOptions: [],
				dataArray: [],
				brandDataForm: {
					brandName: ''
				},
				// radio--优惠券有效期
				fixedVisible: false,// true 是起止时间 false是有效期天数
				// 提交
				saveLoading: false,
				saveAddLoading:false,
				//开始时间的限制
				pickerOptionsStart:{
					disabledDate:time=>{
						if(this.dataFormTemp.endTime){
							return(
								time.getTime() < Date.now()-8.64e7 ||
								time.getTime() < this.dataFormTemp.endTime
							);
						}
						return time.getTime()<Date.now()-8.64e7;
					}
				},
				pickerOptionsEnd:{
					disabledDate:time=>{
						if(this.dataFormTemp.startTime){
							return (
								time.getTime() > this.dataFormTemp.startTime || 
								time.getTime() < Date.now()
							);
						}
						return 	time.getTime()<Date.now()-8.64e7
					}
				},
				// 校验规则
				dataRule: {
					groupName: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
						// {validator: validActivityName, trigger: 'blur'},
					],
					joinFlag: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
					],
					groupPreheat: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
						{validator: maxNum, trigger: 'blur'},
					],
					startTime: [
						{required: false, message: '必填项不能为空', trigger: 'blur'},
						{validator: startTimechange, trigger: 'blur'},
						{validator: integerTime, trigger: 'blur'},
					],
					validTime: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
						{validator: validTimechange, trigger: 'blur'},
					],
					payEndTime: [
						{required: true, message: '必填项不能为空', trigger: 'blur'},
						{validator: payEndTimechange, trigger: 'blur'},
					],
				},
			}
		},
		components: {
			Bread,
		},
		watch: {},
		methods: {
			init(row) {
				// 商品列表数据
				if (row) {
					this.breaddata = ["营销系统", "拼团管理", "编辑"];
					this.row = row;
					//获取品牌数据 异步回调用promise，否则回显可能获取不到商品或品牌的数据
					this.backScan(row)
				} else {
					//新增关闭loading效果
					this.couponLoading = false
					this.breaddata = ["营销系统", "拼团管理", "新增"];
					this.getOrderChangeDetail();
				}
			},
			getOrderChangeDetail(){
				getOrderChangeDetailApi().then(res=>{
					this.dataFormTemp.payEndTime = res.data.cancelOrder
				})
			},
			// 回显
			backScan(row) {
				var obj = {
					id: row.id,
					type: "update"
				}
				groupDetails(obj).then((res => {
					if (res.code == 200) {
						this.couponLoading = false
						let data = {}
						for (let k in this.dataFormTemp) {
							this.data = res.data;
							this.dataFormTemp[k] = res.data[k];
							if (k == 'recommendFlag') {
								if (res.data[k] == 0) {
									this.dataFormTemp[k] = true
								} else {
									this.dataFormTemp[k] = false
								}
							}
							if (k == 'simulateFlag') {
								if (res.data[k] == 0) {
									this.dataFormTemp[k] = true
								} else {
									this.dataFormTemp[k] = false
								}
							}
						}
						this.dataFormTemp.joinFlag = this.dataFormTemp.joinFlag.toString();
					} else {
						this.$message({
							message: res.msg,
							type: "error",
							duration: 1500
						})
						// 跳回上一页 刷新列表
						this.$emit('changePage')
					}
				}))
			},
      async savaAndSub(dataFormTemp){
				var that = this
				that.$refs[dataFormTemp].validate((valid) => {
					if (valid) {
						let param = {}
						for (let k in that.dataFormTemp) {
							param[k] = that.dataFormTemp[k]
							if (k == 'recommendFlag') {
								if (that.dataFormTemp[k]) {
									param[k] = 0
								} else {
									param[k] = 1
								}
							}
							if (k == 'simulateFlag') {
								if (that.dataFormTemp[k]) {
									param[k] = 0
								} else {
									param[k] = 1
								}
							}
						}
						this.saveAddLoading = true;
						var fn = this.row ? editGroup : addGroup
						if(this.row){
							param.id = this.row.id
						}
						fn(param).then((res => {
							this.saveAddLoading = false;
							let status = null;
							if (res.code == "200") {
								status = "success";
								this.$emit('changeCompent', 6, res.data)
							} else {
								status = "error";
							}
							this.$message({
								message: res.msg,
								type: status,
								duration: 1500
							})
						}))
					} else {
						return false;
					}
				})

      },
			// 表单提交
			dataFormSubmit(dataFormTemp) {
				var that = this
				that.$refs[dataFormTemp].validate((valid) => {
					if (valid) {
						let param = {}
						for (let k in that.dataFormTemp) {
							param[k] = that.dataFormTemp[k]
							if (k == 'recommendFlag') {
								if (that.dataFormTemp[k]) {
									param[k] = 0
								} else {
									param[k] = 1
								}
							}
							if (k == 'simulateFlag') {
								if (that.dataFormTemp[k]) {
									param[k] = 0
								} else {
									param[k] = 1
								}
							}
						}
						this.saveLoading = true;
						var fn = this.row ? editGroup : addGroup
            if(this.row){
							param.id = this.row.id
            }
						fn(param).then((res => {
							this.saveLoading = false;
							let status = null;
							if (res.code == "200") {
								status = "success";
								this.changePage()
							} else {
								status = "error";
							}
							this.$message({
								message: res.msg,
								type: status,
								duration: 1500
							})
						}))
					} else {
						return false;
					}
				})
			},


			// 返回上一页
			changePage() {
				this.$emit('changePage')
			}
		}
	}
</script>

<style lang="scss" scoped>
  // 新增或编辑表单
  .addOrEdit {
    margin-left: 10%;

    .fullTitle {
      font-weight: 700;
      height: 40px;
      line-height: 40px;
    }

    .title-1 {
      margin-left: -60px;
    }

    .hrTips {
      color: #999999;
      margin-left: 150px;
      margin-bottom: 10px;
    }

    .el-form-item {
      display: block;
    }

    /deep/ .el-form-item__label {
      width: 150px !important;
      text-align: right;
    }

    /deep/ .el-form-item__error {
      position: relative !important;
      display: inline-block;
      margin-left: 10px;
    }

    .el-input {
      width: 400px !important;
    }

    .specSize {
      .el-input {
        width: 200px !important;
      }
    }
  }

  // 添加指定品牌
  .brandDialog {
    .elTagWarp {
      border: 1px solid #cccccc;
      padding: 12px 12px 12px 0px;
      margin-bottom: 12px;
      min-height: 120px;

      .el-tag {
        margin-left: 12px;
        margin-bottom: 12px;
      }
    }

    /deep/ .el-dialog__header {
      border-bottom: 1px solid #cccccc;
    }

    .clear {
      height: 60px;
      display: flex;
      align-items: flex-end;
      justify-content: flex-end;
    }
  }

  // 指定商品
  .goodsDialog {
    .flexLayout {
      display: flex;
      flex-wrap: wrap;
      margin: 0 15px;

      .blueBlock {
        width: 33%;

        .content {
          border: 1px solid #cccccc;
          min-height: 100px;
          margin: 12px;
        }

        .chooseContent {
          border: 1px solid #3f51af;
          min-height: 100px;
          margin: 12px;
        }

        .displayImg {
          display: flex;
          margin: 10px;

          .goodsImg {
            position: relative;
            width: 80px;
            height: 80px;
            display: inline-block;
            margin-right: 10px;
          }

          .goodsName {
            display: inline-block;

            .goodsTitle {
              width: 100%;
              line-height: 22px;
              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .goodsPrice {
              margin-top: 5px;
            }
          }
        }
      }
    }
  }
</style>
