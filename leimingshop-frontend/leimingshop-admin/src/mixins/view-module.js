import Cookies from 'js-cookie'
import qs from 'qs'
// import { resolve, reject } from '_any-promise@1.3.0@any-promise';
export default {
  data() {
    /* eslint-disable */
    return {
      // 设置属性
      mixinViewModuleOptions: {
        activatedIsNeed: true,    // 此页面是否在激活（进入）时，调用查询数据列表接口？
        getDataListURL: '',       // 数据列表接口，API地址
        getDataListIsPage: false, // 数据列表接口，是否需要分页？
        deleteURL: '',            // 删除接口，API地址
        deleteIsBatch: false,     // 删除接口，是否需要批量？
        deleteIsBatchKey: 'id',   // 删除接口，批量状态下由那个key进行标记操作？比如：pid，uid...
        exportURL: ''            // 导出接口，API地址
      },
      // 默认属性
      dataForm: {},               // 查询条件
      dataList: [],               // 数据列表
      order: '',                  // 排序，asc／desc
      orderField: '',             // 排序，字段
      page: 1,                    // 当前页码
      limit: 10,                  // 每页数
      total: 0,                   // 总条数
      dataListLoading: false,     // 数据列表，loading状态
      dataListSelections: [],     // 数据列表，多选项
      addOrUpdateVisible: false,   // 新增／更新，弹窗visible状态
      retquestType: "get",
      deleteMsg:"",//删除时提示语
    }
    /* eslint-enable */
  },
  activated() {

    if (this.mixinViewModuleOptions.activatedIsNeed) {
      this.getDataList()
    }
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      return new Promise((resolve,reject)=>{
        this.$http[this.retquestType](
          this.mixinViewModuleOptions.getDataListURL,
          {
            params: {
              order: this.order,
              orderField: this.orderField,
              page: this.mixinViewModuleOptions.getDataListIsPage ? this.page : null,
              limit: this.mixinViewModuleOptions.getDataListIsPage ? this.limit : null,
              ...this.dataForm
            }
          }
        ).then(({ data: res }) => {
          this.dataListLoading = false
          if (res.code !== 200) {
            this.dataList = []
            this.total = 0
            return this.$message.error(res.msg)
          }
          if(this.mixinViewModuleOptions.getDataListIsPage){
              if(res.data && res.data.list){
                this.dataList = res.data.list
                this.total = res.data.total;
              }else{
                this.dataList = [];
                this.total = 0;
              }
          }else{
              this.dataList = res.data;
              this.total = res.data.total;
          }
          resolve(res);
        }).catch(() => {
          this.dataListLoading = false;
          resolve(this.dataList);
        })
      })
    },
    // 多选
    dataListSelectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 排序
    dataListSortChangeHandle(data) {
      if (!data.order || !data.prop) {
        this.order = ''
        this.orderField = ''
        return false
      }
      this.order = data.order.replace(/ending$/, '')
      this.orderField = data.prop.replace(/([A-Z])/g, '_$1').toLowerCase()
      this.getDataList()
    },
    // 分页, 每页条数
    pageSizeChangeHandle(val) {
    	// alert(val)
      this.page = 1
      this.limit = val
      return this.getDataList()
    },
    // 分页, 当前页
    pageCurrentChangeHandle(val) {
      this.page = val
      return this.getDataList()
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        if(id){
          this.$refs.addOrUpdate.dataForm.id = id
        }else{
          this.$refs.addOrUpdate.dataForm.id = null
        }
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id,isRefesh=true) {
      let that = this;
      if (this.mixinViewModuleOptions.deleteIsBatch && !id && this.dataListSelections.length <= 0) {
        return this.$message({
          message: this.$t('prompt.deleteBatch'),
          type: 'warning',
          duration: 500
        })
      }
      return new Promise((resolve,reject)=>{
          that.$confirm(that.deleteMsg?that.deleteMsg:that.$t('prompt.info', { 'handle': that.$t('delete') }), that.$t('prompt.title'), {
            confirmButtonText: that.$t('confirm'),
            cancelButtonText: that.$t('cancel'),
            type: 'warning'
          }).then(() => {
            that.$http.delete(
              `${that.mixinViewModuleOptions.deleteURL}${that.mixinViewModuleOptions.deleteIsBatch ? '' : '/' + id}`,
              that.mixinViewModuleOptions.deleteIsBatch ? {
                'data': id ? [id] : that.dataListSelections.map(item => item[that.mixinViewModuleOptions.deleteIsBatchKey])
              } : {}
            ).then(({ data: res }) => {
              if (res.code !== 200) {
                return that.$message.error(res.msg)
                resolve("error");
              }
              that.$message({
                // message: that.$t('prompt.success'),
                message: res.msg,
                type: 'success',
                duration: 1500,
                onClose: () => {
                  if(isRefesh) that.getDataList()
                }
              })
              resolve("ok");
            }).catch(() => { resolve("error");  })
          }).catch(() => { resolve("error"); })
      })
    },
    // 导出
    exportHandle() {
      var params = qs.stringify({
        'token': Cookies.get('ADMIN-TOKEN'),
        ...this.dataForm
      })
      window.location.href = `${window.SITE_CONFIG['apiURL']}${this.mixinViewModuleOptions.exportURL}?${params}`
    }
  }
}
