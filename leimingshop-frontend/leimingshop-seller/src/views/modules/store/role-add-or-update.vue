<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false" class="rolePage">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="roleName" label="角色名称：">
        <el-input v-model="dataForm.roleName" :placeholder="$t('role.name')" minlength="12" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item prop="roleRemark" label="角色说明：">
        <el-input v-model="dataForm.roleRemark" :placeholder="$t('role.remark')" maxlength="50"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item size="mini" label="功能权限：">
            <el-tree
              :data="menuList"
              :props="{ label: 'name', children: 'children' }"
              node-key="id"
              ref="menuListTree"
              accordion
              show-checkbox>
            </el-tree>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item size="mini" :label="$t('role.deptList') + '：'">
            <el-tree
              :data="deptList"
              :props="{ label: 'name', children: 'children' }"
              node-key="id"
              ref="deptListTree"
              accordion
              show-checkbox>
            </el-tree>
          </el-form-item>
        </el-col> -->
      </el-row>
    </el-form>
    <div slot="footer" style="text-align: center;">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  data () {
    return {
      visible: false,
      menuList: [],
      // deptList: [],
      dataForm: {
        id: '',
        roleName: '',
        menuIdList: [],
        // deptIdList: [],
        roleRemark: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        roleName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.$refs.menuListTree.setCheckedKeys([])
        // this.$refs.deptListTree.setCheckedKeys([])
        Promise.all([
          this.getMenuList(),
          // this.getDeptList()
        ]).then(() => {
          if (id) {
            this.getInfo()
          }
        })
      })
    },
    // 获取菜单列表
    getMenuList () {
      return this.$http.get('/seller-api/menu/find/all/menu').then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.menuList = res.data
      }).catch(() => {})
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/seller-api/role/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
        this.dataForm.menuIdList.forEach(item => this.$refs.menuListTree.setChecked(item, true))
        // this.$refs.deptListTree.setCheckedKeys(this.dataForm.deptIdList)
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.dataForm.menuIdList = [
          ...this.$refs.menuListTree.getCheckedKeys(),
          ...this.$refs.menuListTree.getHalfCheckedKeys()
        ]
        // this.dataForm.deptIdList = this.$refs.deptListTree.getCheckedKeys()
        this.$http[!this.dataForm.id ? 'post' : 'put']('/seller-api/role', this.dataForm).then(({ data: res }) => {
          if (res.code !== 200) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
<style lang="scss">
.rolePage {
  .el-tree-node__content{
    border: none !important;
  }
}
</style>
