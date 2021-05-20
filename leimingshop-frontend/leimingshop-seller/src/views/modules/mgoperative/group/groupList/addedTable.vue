<template>
  <div class="couponList">
    <!--表格-->
    <el-table
            width="100%"
            :data="dataList"
            border=""
            v-loading="dataListLoading"
            @selection-change="dataListSelectionChangeHandle"
            style="width: 100%;maigin-top:20px;">
      <el-table-column type="selection" header-align="center" align="center" width="50"
      ></el-table-column>
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
                <el-tooltip class="item" effect="dark" :content="scope.row.goodsName"
                            placement="top-start">
                  <span style="color: #4e80db;" class="goodsNameContent">{{scope.row.goodsName}}</span>
                </el-tooltip>
              </div>
              <div class="goodsNum">
                <span class="goodsBrandName">￥{{scope.row.specSellPrice}}</span>
              </div>
            </div>
          </div>
        </template>


      </el-table-column>
      <el-table-column prop="activityPrice" label="拼团价" align="center" min-width="120">
        <template slot-scope="scope">
          <div v-if="scope.row.activityPrice">
            ￥{{scope.row.activityPrice}}
          </div>
          <div v-else>
            ￥{{scope.row.minActivityPrice}}-￥{{scope.row.maxActivityPrice}}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="activitySurplusStorage" label="活动库存" align="center" min-width="120"></el-table-column>
      <el-table-column prop="sort" label="排序" align="center" min-width="120"></el-table-column>
      <el-table-column prop="regimentNum" label="成团人数" align="center" min-width="120"></el-table-column>

      <el-table-column label="操作" min-width="250" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text"
                     @click="addGoods(scope.row)">编辑
          </el-button>
          <el-button size="mini" type="text"
                     @click="deleteGoods(scope.row)">移除
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    <!--批量删除按钮-->
    <div class="addButton">
      <el-button class="btn" type="primary" plain @click="deleteAll">批量删除</el-button>
<!--      <el-button class="btn" style="margin-left: 10px" type="primary" @click="changePage()">返回上一页</el-button>-->
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
            width="80%">
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
        </div>
        <div class="hrTips-t">若活动库存为商品总库存，会导致活动开始前无法原价购买</div>

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
  import Bread from "@/components/bread";
  import mixinViewModule from "@/mixins/view-module";
  import {getGroupGoodsList} from "@/api/url";
  import {deletetGroupGoods} from "@/api/api";
  import {getGoodscalss, getGroupListSku, getGroupGoodsAdd} from "@/api/api.js";

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
        breaddata: ["营销系统", "拼团管理", "活动名称", "编辑拼团商品"],
        mixinViewModuleOptions: {
          getDataListURL: getGroupGoodsList,
          getDataListIsPage: true,
          activatedIsNeed: false,
          // deleteURL: '/seller-api/group/goods',
          // deleteIsBatch: true
        },
        // 查询表单
        dataForm: {
          activityId: '',
        },
        dataList: [],
        dataListLoading: false,
        saveLoading: false,

        goodscalssOption: [],
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
            {validator: geZero, trigger: 'blur'},
          ],
          joinLimit: [
            {required: true, message: '必填项不能为空', trigger: 'blur'},
          ],
          onceBuyLimit: [
            {required: true, message: '必填项不能为空', trigger: 'blur'},
            {validator: maxNum, trigger: 'blur'}
          ],
        }
      }
    },
    components: {
      Bread,
    },
    created() {
      // this.handleItemChange();
    },
    methods: {
      init(dataForm,row){
        Object.assign(this.dataForm,dataForm);
        this.row = row;  //活动itemw
        this.dataForm.activityId = row.id;
        this.getDataList();
      },
      previewH5Fn(row){
        window.open(window.SITE_CONFIG['pcUrl']+'/goodsDetails?specId='+row.specId);
      },
      dataListSelectionChangeHandle(val) {
        let arr = []
        console.log('33333333',val)
        val.forEach((v, i) => {
            arr.push(v)
        })
        this.dataListSelections = arr
      },
      // 全选删除按钮触发事件
      deleteAll() {
        if (this.dataListSelections.length == 0) {
          this.$message({
            type: 'warning',
            message: '请选择删除的商品'
          })
          return false
        }
        let that = this;
        const arrId = []
        this.dataListSelections.forEach(function (val, index, arr) {
            arrId.push(val.goodsId);
        })
        console.log(that.dataListSelections)
        this.$confirm('您确认删除选中商品吗？', '提示', {
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let obj = {
            goodsIds: arrId,
            activityId: that.dataForm.activityId,
          }
          deletetGroupGoods(obj).then(res => {
            if (res.code == 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.getDataList() // 刷新列表数据
            } else {
              this.$message({
                type: 'warning',
                message: '删除失败!'
              })
              this.awaitgetData()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })
      },
      async addGoods(row) {
        this.modelForm.activityId = this.row.id;
        this.modelForm.goodsId = row.goodsId;
        this.modelForm.sort = row.sort;
        this.modelForm.regimentNum = row.regimentNum;  //成团人数
        this.modelForm.joinLimit = row.joinLimit;
        this.modelForm.onceBuyLimit = row.onceBuyLimit;
        this.goodsRow = row;
        let param = {
          activityId: this.modelForm.activityId,
          goodsId: this.goodsRow.goodsId
        }
        let res = await getGroupListSku(param);
        if (res) {
          this.modelForm.skuList = res.data;
          this.photoVisible = true;
        }
      },
      handleClose() {
        this.photoVisible = false
        this.$refs['dataRuleOther'].resetFields() //校验隐藏
      },
      startCheck() {
        console.log(this.skuList)
        this.$refs['dataRuleOther'].validate(valid => {
          if (valid) {
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
          }
        })
      },
      handleItemChange(val) {
        console.log('active item:', val, typeof val);
        var val1 = [];
        if (val && typeof (val) == "string") {
          val1 = JSON.parse(val);
        } else {
          val && val.forEach((item, index) => {
            if (typeof (item) == "string") {
              val1.push(JSON.parse(item))
            } else {
              val1.push(item)
            }
          })
        }
        if (val1)
          var id = 0
        let takeBlack = true
        if (Object.prototype.toString.call(val1) === '[object Object]') {
          id = val1.id ? val1.id : 0;
        } else if (Object.prototype.toString.call(val1) === '[object Array]' && val1.length > 0) {
          id = val1[val1.length - 1].id;
          if (val1.length == 2) takeBlack = false
        }
        var obj = {
          id: id
        }
        // console.log(val1.length,Object.prototype.toString.call(val1),takeBlack);
        getGoodscalss(obj).then((res) => {
          if (res.code == 200) {
            res.data.forEach((item, index) => {
              item.label = item.gcName
              takeBlack ? item.children = [] : ""
              item.value = JSON.stringify(item);
              // item.value = item.id
            })
            if (!val) {
              this.goodscalssOption = res.data;
            } else {
              this.goodscalssOption.forEach((item, index) => {
                if (item.id == id) {
                  item.children = res.data.length > 0 ? res.data : "";
                }
                item.children.forEach((item2, index2) => {
                  if (item2.id == id) {
                    item2.children = res.data.length > 0 ? res.data : "";
                  }
                })
              })
            }
          }
          // console.log(this.goodscalssOption);
        })
      },
      finishCange() {
        console.log(this.gcIds);
        if (this.gcIds.length > 0) {
          this.dataForm.gcId = JSON.parse(this.gcIds[this.gcIds.length - 1]).id
          console.log(JSON.parse(this.gcIds[this.gcIds.length - 1]).id);
        } else {
          this.dataForm.gcId = "";
        }

      },

      // 重置
      reset() {
        this.modelForm = {
          activityId: '',
          goodsId: '',
          sort: '',
          skuList: [],
          regimentNum: '',
          joinLimit: '',
          onceBuyLimit: '',
        }
        this.getDataList();
      },
      // 点击新增、编辑、查看
      changeCompent(type, row) {
        this.$emit('changeCompent', type, this.row)
      },
      changePage() {
        this.$emit("showPageNumberFn",1);
      },
      deleteGoods(row) {
        this.$confirm('您确认移除该商品吗?', '提示', {
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          var obj = {
            goodsIds: [row.goodsId],
            activityId: this.row.id
          }
          console.log(obj)
          deletetGroupGoods(obj).then((res => {
            if (res.code == 200) {
              this.$message({
                message: res.msg,
                type: 'success',
                duration: 1500,
              })
              this.getDataList() // 刷新列表数据
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
                duration: 1500,
              })
            }
          }))
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      }
    }
  }
</script>

<style lang="scss" scoped>

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
      margin: 10px 0px 0px 0px;
    }

    .el-table--border {
      margin-top: 20px;
    }
  }

  // 商品
  .goodsPropsWrap {
    margin: auto;
    height: 70px;
    display: flex;
    justify-content: center;
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
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: flex-start;
      margin-left: 20px;

      .goodsNameTitle {
        display: inline-block;
        width: 50px;
      }

      .goodsBrandName {
        text-align: left;
      }

      .goodsName {
        display: flex;
        width: 340px;

        .goodsNameContent {
          width: auto;
          text-align: left;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          cursor: pointer;
          font-weight: 600;
        }

      }
    }
  }
</style>

