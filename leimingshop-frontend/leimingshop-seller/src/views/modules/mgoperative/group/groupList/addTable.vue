<template>
  <div class="couponList">
    <!--表格-->
    <div class="table-box">
      <el-table
              width="100%"
              :data="dataList"
              ref="multipleTable"
              border=""
              v-loading="dataListLoading"
              style="width: 100%;maigin-top:20px;"
      >
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品信息" align="center" min-width="450">
          <template slot-scope="scope">
            <div class="goodsPropsWrap" @click="previewH5Fn(scope.row)">
              <div class="goodsImg">
                <img :src="scope.row.goodsMainPicture | filterImgUrl" alt=""/>
              </div>
              <div class="goodsProps">
                <div class="goodsName">
                  <template v-if="scope.row.activityTypeList.length > 0">
                    <span class="active-name" v-if="scope.row.activityTypeList[0] == 1">优惠券</span>
                    <span class="active-name" v-else-if=" scope.row.activityTypeList[0] == 2">满减</span>
                    <span class="active-name" v-else-if="scope.row.activityTypeList[0] == 3">秒杀</span>
                    <span class="active-name" v-else-if=" scope.row.activityTypeList[0] == 4">拼团</span>
                    <span class="active-name" v-else-if=" scope.row.activityTypeList[0] == 5">限时抢</span>
                  </template>
                  <!--                  <span class="active-name-box" v-else></span>-->
                  <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                              placement="top-start">
                    <span style="color: #4e80db;" class="goodsNameContent">{{scope.row.goodsName}}</span>
                  </el-tooltip>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="specSellPrice" label="销售价" align="center" min-width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.specSellPrice">
              ￥{{scope.row.specSellPrice}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="activityState" label="商品状态" align="center" min-width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.goodsShow==0">下架</div>
            <div v-else-if="scope.row.goodsShow==1">上架</div>
            <div v-else-if="scope.row.goodsShow==2">未上架</div>
          </template>
        </el-table-column>
        <el-table-column prop="saleNum" label="实际销量" align="center" min-width="120"></el-table-column>
        <el-table-column prop="specStorage" label="商品库存" align="center" min-width="120"></el-table-column>

        <el-table-column label="操作" min-width="100" align="center">
          <template slot-scope="scope">
            <div>
              <el-button
                      v-if="scope.row.operationType == 0 "
                      size="mini" type="text" @click="addGoods(scope.row,scope.$index)">添加
              </el-button>
              <el-button
                      v-else-if="scope.row.activityTypeList.length > 0 && scope.row.activityId != activityId"
                      :disabled="true"
                      size="mini" type="text">添加`
              </el-button>
              <el-button
                      v-else-if="scope.row.operationType == 1 && scope.row.activityTypeList[0] && scope.row.activityTypeList[0] == 4"
                      size="mini" type="text">
                 <span @click="deleteGoods(scope.row,scope.$index)"  class="btn-span"
                       :style="{color:(scope.row.btnText=='已添加'? 'gray':'#3F51AF')}"
                       @mouseenter="Onmouseenter(scope.row,scope.$index)"
                       @mouseleave="Onmouseleave(scope.row,scope.$index)">{{scope.row.btnText}}</span>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <el-pagination
            @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"
            :current-page="page"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="limit"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

    <!-- 图片弹框 -->
    <el-dialog
            title="编辑活动商品信息"
            :visible.sync="photoVisible"
            :close-on-click-modal="false"
            :show-close="false"
            width="80%"

    >
      <el-form class="model-box" :model="modelForm" :rules="dataRuleOther" ref="dataRuleOther" label-width="120px">
        <div class="fullTitle ">
          基础设置
        </div>
        <el-form-item label="商品名称：">
          <div>{{goodsRow.goodsName}}</div>
        </el-form-item>
        <div>
          <el-table
                  width="90%"
                  :data="modelForm.skuList"
                  v-loading="dataListLoading"
                  style="width: 100%;maigin-top:20px;">
            <el-table-column prop="specAttrName" label="规格" align="center" min-width="120">
              <template slot-scope="scope">
                <span>{{scope.row.specAttrName == null ? '默认规格' :scope.row.specAttrName}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="specStorage" label="商品库存" align="center" min-width="60"></el-table-column>
            <el-table-column prop="activityStorage" label="活动库存" align="center" min-width="160">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.activityStorage" controls-position="right" :min="0"
                                 :step="1"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column prop="specSellPrice" label="销售价" align="center" min-width="60"></el-table-column>
            <el-table-column prop="activityPrice" label="拼团价（元）" align="center" min-width="160">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.activityPrice" controls-position="right" :min="0.01" :max="99999999.99"
                                 :step="0.01"></el-input-number>
              </template>
            </el-table-column>
          </el-table>
          <div>

          </div>
        </div>
        <div class="hrTips-t">若活动库存为商品总库存，会导致活动开始前无法原价购买。活动库存为0表示该商品不参加活动</div>
        <el-form-item label="参团次数限制：" prop="joinLimit">
          每个用户可参团次数最多
          <el-input v-model="modelForm.joinLimit">
            <span slot="suffix">次</span>
          </el-input>
        </el-form-item>
        <div class="hrTips">0表示不限</div>
        <el-form-item label="单次购买数量：" prop="onceBuyLimit">
          每个用户单次开团或参团时，购买件数最多
          <el-input v-model="modelForm.onceBuyLimit">
            <span slot="suffix">件</span>
          </el-input>
        </el-form-item>
        <div class="hrTips">0表示不限</div>

        <el-form-item label="排序：" prop="sort">
          <el-input-number v-model="modelForm.sort" controls-position="right" :min="0"></el-input-number>
        </el-form-item>
        <div class="hrTips">0-255，前台活动，后台商品列表均以此排序，数字越小排序越靠前</div>

        <el-form-item label="成团人数：" prop="regimentNum">
          <el-input v-model="modelForm.regimentNum">
            <span slot="suffix">人</span>
          </el-input>
        </el-form-item>
        <div class="hrTips">拼团成功需要参与购买的人数，需大于1人</div>

      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="startCheck" :loading="saveLoading">{{saveLoading?"提交中...":"确 认"}}</el-button>
      </div>
    </el-dialog>
    <!-- 图片弹框 -->
  </div>
</template>

<script>

  import mixinViewModule from "@/mixins/view-module";
  import {getGroupGoodsPage} from "@/api/url";
  import {deletetGroupGoods, allTreeGoodsclass} from "@/api/api";
  import {getGroupListSku, getGroupGoodsAdd} from "@/api/api.js";

  export default {
    mixins: [mixinViewModule],
    data() {
      var geZero = (rule, value, callback) => {
        if (/[^\d]/.test(value) || parseInt(value) <= 1) {
          callback(new Error('成团人数必须大于1'))
        } else if (/[^\d]/.test(value) || parseInt(value) > 999) {
          callback(new Error('此选项不能大于999'))
        }
        callback()
      };
      var maxNum = (rule, value, callback) => {
        if (/[^\d]/.test(value) || parseInt(value) > 999) {
          callback(new Error('此选项不能大于999'))
        }
        callback()
      };
      return {
        row: {},
        mixinViewModuleOptions: {
          getDataListURL: getGroupGoodsPage,
          getDataListIsPage: true,
          activatedIsNeed: false,
          // deleteURL: '/seller-api/group',
          // deleteIsBatch: true
        },
        // 查询表单
        dataForm: {
          goodsName: '',
          goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
          gcId: '',
        },
        gcIds: [],
        dataList: [],
        dataListLoading: false,

        isShowButton: false,
        saveLoading: false,
        btnText: '已添加',
        goodscalssOption: [],
        props: {
          value: "id",
          label: "gcName"
        },
        goodsRow: {},


        modelForm: {
          activityId: '',
          goodsId: '',
          sort: '',
          skuList: [],
          regimentNum: '',
          joinLimit: '',
          onceBuyLimit: '',
        },
        photoVisible: false,
        dataRuleOther: {
          // activityId: [
          // 	{required: true, message: '必填项不能为空', trigger: 'blur'},
          // ],
          // goodsId: [
          // 	{required: true, message: '必填项不能为空', trigger: 'blur'},
          // ],
          sort: [
            {required: false, message: '必填项不能为空', trigger: 'blur'},
          ],
          regimentNum: [
            {required: true, message: '必填项不能为空', trigger: 'blur'},
            {validator: geZero, trigger: 'blur'}
          ],
          joinLimit: [
            {required: true, message: '必填项不能为空', trigger: 'blur'},
            {validator: maxNum, trigger: 'blur'}
          ],
          onceBuyLimit: [
            {required: true, message: '必填项不能为空', trigger: 'blur'},
            {validator: maxNum, trigger: 'blur'}
          ],
        }
      }
    },
    components: {
    },
    created() {
      // this.getallTreeGoodsclassFn();
    },
    methods: {
      // init(row) {
      //   // this.row = row;  //活动item
      //   // this.activityId = row.id;
      //   // this.awaitgetData();
      //
      //   this.page=1;
      //   this.limit=10;
      //   this.getDataList();
      //
      // },
      init(dataForm,row){
        Object.assign(this.dataForm,dataForm)
        this.page=1;
        this.limit=10;
        this.row = row;  //活动item
        this.activityId = row.id;
        this.awaitgetData();
        // this.getDataList();
      },
      isSelectable(row, index) {
        if (row.activityId == this.activityId) {
          return true
        } else {
          return false
        }
      },
      dataListSelectionChangeHandle(val) {
        let arr = []
        val.forEach((v, i) => {
          if (v.activityId != null && v.activityId == this.activityId) {
            arr.push(v)
          }
        })
        this.dataListSelections = arr
      },
      async addGoods(row, index) {
        this.modelForm.activityId = this.row.id
        this.modelForm.goodsId = row.id
        this.goodsRow = row;
        let param = {
          activityId: this.modelForm.activityId,
          goodsId: this.goodsRow.id
        }
        let res = await getGroupListSku(param);
        if (res.code == 200) {
          this.modelForm.skuList = res.data;
          this.photoVisible = true;
        }
      },
      handleClose() {
        this.photoVisible = false
        this.$refs['dataRuleOther'].resetFields() //校验隐藏
      },
      startCheck() {
        this.$refs['dataRuleOther'].validate(valid => {
          if (valid) {
            let isCheck = this.modelForm.skuList.some(v => {
              return v.activityStorage != 0 && v.activityStorage != null
            })
            if (isCheck) {
              this.saveLoading = true;
              getGroupGoodsAdd(this.modelForm).then(res => {
                this.saveLoading = false;
                if (res.code == 200) {
                  this.$message({
                    message: res.msg,
                    type: 'success'
                  })
                  this.photoVisible = false
                  this.reset()
                  this.$refs['dataRuleOther'].resetFields() //校验隐藏
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                }
              })
            } else {
              this.$message({
                type: 'error',
                message: '必须有一个规格的活动库存不为0！'
              });
            }

          }
        })
      },
      finishCange() {
        let len = this.gcIds.length;
        if (len != -1) {
          this.dataForm.gcId = this.gcIds[this.gcIds.length - 1];
        }
      },

      async awaitgetData() {
        await this.getDataList();
        this.dataList.forEach(v => {
          this.$set(v, 'btnText', '已添加')
        })
      },
      // 查询表格数据
      getData() {
        this.page = 1;
        this.awaitgetData();
      },
      // 分页, 每页条数
      pageSizeChangeHandle(val) {
        this.page = 1
        this.limit = val
        this.awaitgetData()
      },
      // 分页, 当前页
      pageCurrentChangeHandle(val) {
        this.page = val
        this.awaitgetData()
      },
      // 重置
      reset() {
        this.dataForm = {
          goodsName: '',
          goodsShow: '',  //默认:2未上架,0下架状态，1上架状态
          gcId: '',
        }
        this.gcIds = [];//分类ID
        this.awaitgetData();
      },
      changeCompent(type, row) {
        this.$emit('changeCompent', type, row)
      },
      changePage() {
        this.$emit('changePage')
      },
      Onmouseenter(row, index) {
        let obj = row
        obj.btnText = '移除'
        this.$set(this.dataList, index, obj);
      },
      Onmouseleave(row, index) {
        let obj = row
        obj.btnText = '已添加'
        this.$set(this.dataList, index, obj);
      },

      previewH5Fn(row){
        window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?specId='+row.specId);
      },
      deleteGoods(row, index) {
        this.$nextTick(() => {
          if (row.btnText == '移除') {
            this.$confirm('您确认移除该商品吗?', '提示', {
              cancelButtonText: '取消',
              confirmButtonText: '确定',
              type: 'warning'
            }).then((e) => {
              var obj = {
                goodsIds: [row.id],
                activityId: this.activityId
              }
              deletetGroupGoods(obj).then((res => {
                if (res.code == 200) {
                  this.$message({
                    message: res.msg,
                    type: 'success',
                    duration: 1500,
                  })
                  this.awaitgetData() // 刷新列表数据
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
          }
        })


      }
    }
  }
</script>

<style lang="scss" scoped>
  .btn-box {
    margin-top: 30px;
    text-align: center;
  }

  .table-box {
    .el-button {
      margin-left: 0;
    }
  }


  .el-table__body-wrapper {
    max-height: 300px;
  }

  .model-box {
    padding: 0 30px;

    .el-input-number {
      width: 140px;
    }

    .el-input {
      width: 100px;
    }

    /deep/
    .el-form-item__content {
      display: flex;
    }

    /deep/
    .el-form-item__error {
      position: relative;
      line-height: 42px;
      padding-left: 12px;
    }
  }

  .fullTitle {
    font-weight: 700;
    height: 40px;
    line-height: 40px;
  }

  .hrTips-t {
    color: #999999;
    margin: 10px 0;
  }

  .hrTips {
    color: #999999;
    margin-left: 120px;
    margin-bottom: 10px;
  }


  .couponList {
    /deep/ .el-input__icon {
      height: unset !important;
    }

    .line {
      height: 10px;
      background: #ECEDF1;
      margin: 0 -20px;
    }

    .addButton {
      margin: 10px 30px;
    }

    .el-table--border {
      margin-top: 20px;
    }
  }


  .btn-span{
    display: inline-block;
    width: 60px;
  }

  .goodsPropsWrap {
    margin: auto;
    height: 80px;
    width: 410px;
    display: flex;
    justify-content: center;
    /*justify-content: space-around;*/
    align-items: center;

    .goodsImg {
      width: 70px;
      height: 70px;
      flex-shrink: 0;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .goodsProps {
      /*width: 240px;*/
      height: 70px;
      display: flex;
      align-items: center;
      /*flex-direction: column;*/
      /*align-items: flex-start;*/

      .goodsNameTitle {
        display: inline-block;
        width: 30px;
      }

      .goodsBrandName {
        text-align: left;
      }

      .goodsName {
        display: flex;
        width: 340px;
        box-sizing: border-box;
        padding-left: 12px;
        .active-name {
          width: 60px;
          color: red;
          margin-right: 6px;
          border: 1px solid red;
          padding: 1px 3px;
          flex-shrink: 0;
        }

        .active-name-box {
          width: 60px;
          margin: 0 4px;
          padding: 1px 3px;
          flex-shrink: 0;
        }

        .goodsNameContent {
          width: auto;
          padding: 1px 3px;
          text-align: left;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          cursor: pointer;
          font-weight: 600;
        }

      }
    }
  }
</style>


