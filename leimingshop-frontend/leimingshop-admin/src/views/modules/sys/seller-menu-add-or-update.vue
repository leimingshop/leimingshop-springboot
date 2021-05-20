<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="type" :label="$t('menu.type') + '：'" size="mini">
        <el-radio-group v-model="dataForm.type" :disabled="!!dataForm.id">
          <el-radio :label="0">{{ $t('menu.type0') }}</el-radio>
          <el-radio :label="1">{{ $t('menu.type1') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="name" :label="$t('menu.name') + '：'">
        <el-input v-model="dataForm.name" :placeholder="$t('menu.name')"></el-input>
      </el-form-item>
      <el-form-item prop="parentName" :label="$t('menu.parentName') + '：'" class="menu-list">
        <el-popover v-model="menuListVisible" ref="menuListPopover" placement="bottom-start" trigger="click">
          <el-tree
            :data="menuList"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            ref="menuListTree"
            :highlight-current="true"
            :expand-on-click-node="false"
            accordion
            @current-change="menuListTreeCurrentChangeHandle">
          </el-tree>
        </el-popover>
        <el-input v-model="dataForm.parentName" v-popover:menuListPopover :placeholder="$t('menu.parentName')">
         <i v-if="dataForm.pid !== '0'" slot="suffix" @click.stop="deptListTreeSetDefaultHandle()" class="el-icon-circle-close el-input__icon"></i>
        </el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 0" prop="url" :label="$t('menu.url') + '：'">
        <el-input v-model="dataForm.url" :placeholder="$t('menu.url')"></el-input>
      </el-form-item>
      <el-form-item prop="sort" :label="$t('menu.sort') + '：'">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" :label="$t('menu.sort')"></el-input-number>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 0" prop="icon" :label="$t('menu.icon') + '：'" class="icon-list">
        <el-popover v-model="iconListVisible" ref="iconListPopover" placement="bottom-start" trigger="click" popper-class="mod-sys__menu-icon-popover">
          <div class="mod-sys__menu-icon-inner">
            <div class="mod-sys__menu-icon-list">
              <el-button
                v-for="(item, index) in iconList"
                :key="index"
                @click="iconListCurrentChangeHandle(item)"
                :class="{ 'is-active': dataForm.icon === item }">
                <svg class="icon-svg" aria-hidden="true"><use :xlink:href="`#${item}`"></use></svg>
              </el-button>
            </div>
          </div>
        </el-popover>
        <el-input v-model="dataForm.icon" v-popover:iconListPopover :placeholder="$t('menu.icon')"></el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 1" prop="permissions" :label="$t('menu.permissions') + '：'">
        <el-input v-model="dataForm.permissions" :placeholder="$t('menu.permissionsTips')"></el-input>
      </el-form-item>
      <el-form-item
        v-for="(item, index) in dataForm.resourceList"
        :key="item.key"
        :prop="`resourceList.${index}.resourceUrl`"
        :rules="{ required: true, message: $t('validate.required'), trigger: 'blur' }"
        :label="(index === 0 ? $t('menu.resource') : '' ) + '：'"
        class="resource-list">
        <el-row>
          <el-col :span="22">
            <el-input v-model="item.resourceUrl" :placeholder="$t('menu.resourceUrl')">
              <el-select v-model="item.resourceMethod" slot="prepend" :placeholder="$t('menu.resourceMethod')">
                <el-option label="GET" value="GET"></el-option>
                <el-option label="POST" value="POST"></el-option>
                <el-option label="PUT" value="PUT"></el-option>
                <el-option label="DELETE" value="DELETE"></el-option>
              </el-select>
            </el-input>
          </el-col>
          <el-col :span="2" class="text-center">
            <el-button @click="resourceDeleteHandle(item)" size="small" type="text" class="artdanger">{{ $t('delete') }}</el-button>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item :label="dataForm.resourceList.length <= 0 ? $t('menu.resource') : ''">
        <el-button type="primary" @click="resourceAddHandle()" class=" w-percent-100">{{ $t('menu.resourceAddItem') }}</el-button>
      </el-form-item>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
import { getIconList } from '@/utils'
export default {
  data () {
    return {
      visible: false,
      menuList: [],
      menuListVisible: false,
      iconList: [],
      iconListVisible: false,
      dataForm: {
        id: '',
        type: 0,
        name: '',
        pid: '0',
        parentName: '',
        url: '',
        resourceList: [],
        permissions: '',
        sort: 0,
        icon: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        name: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        parentName: [
          { required: true, message: this.$t('validate.required'), trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    'dataForm.type' (val) {
      this.$refs['dataForm'].clearValidate()
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.iconList = getIconList()
        this.dataForm.parentName = this.$t('menu.parentNameDefault')
        this.dataForm.resourceList = []
        this.getMenuList().then(() => {
          if (this.dataForm.id) {
            this.getInfo()
          }
        })
      })
    },
    // 获取菜单列表
    getMenuList () {
      return this.$http.get('/admin-api/store/menu/find/all/menu').then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.menuList = res.data
      }).catch(() => {})
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/admin-api/store/menu/${this.dataForm.id}`).then(({ data: res }) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.dataForm.id=res.data.id;
        this.dataForm.name=res.data.menuName;
        this.dataForm.permissions=res.data.menuPermission;
        this.dataForm.type = res.data.menuType;
        this.dataForm.parentName=res.data.parentName;
        this.dataForm.url=res.data.menuUrl;
        this.dataForm.sort=res.data.sort;
        this.dataForm.icon=res.data.menuIcon;
        this.dataForm.pid=res.data.parentId;
        this.dataForm.resourceList = res.data.resourceList;
        for(let i = 0;i<this.menuList.length;i++){
          if(this.menuList[i].id==this.dataForm.pid){
            this.dataForm.parentName=this.menuList[i].name
          }
        }
        if (this.dataForm.pid === '0') {
          return this.deptListTreeSetDefaultHandle()
        }
        this.$refs.menuListTree.setCurrentKey(this.dataForm.pid)
      }).catch(() => {})
    },
    // 上级菜单树, 设置默认值
    deptListTreeSetDefaultHandle () {
      this.dataForm.pid = '0'
      this.dataForm.parentName = this.$t('menu.parentNameDefault')
    },
    // 上级菜单树, 选中
    menuListTreeCurrentChangeHandle (data) {
      this.dataForm.pid = data.id
      this.dataForm.parentName = data.name
      this.menuListVisible = false
    },
    // 图标, 选中
    iconListCurrentChangeHandle (icon) {
      this.dataForm.icon = icon
      this.iconListVisible = false
    },
    // 菜单资源, 添加
    resourceAddHandle () {
      this.dataForm.resourceList.push({
        key: new Date().getTime(),
        resourceMethod: 'GET',
        resourceUrl: ''
      })
    },
    // 菜单资源, 删除
    resourceDeleteHandle (resource) {
      this.dataForm.resourceList = this.dataForm.resourceList.filter(item => item.key !== resource.key)
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        var obj={
         
        }
          obj.id=this.dataForm.id,
          obj.menuName=this.dataForm.name,
          obj.menuUrl=this.dataForm.url,
          obj.parentId=this.dataForm.pid,
          obj.sort=this.dataForm.sort,
          obj.menuPermission=this.dataForm.permissions,
          obj.menuIcon=this.dataForm.icon,
          obj.menuType=this.dataForm.type,
          obj.resourceList=this.dataForm.resourceList,
        this.$http[!this.dataForm.id ? 'post' : 'put']('/admin-api/store/menu', obj).then(({ data: res }) => {
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
.mod-sys__menu {
  .resource-list {
    .el-select .el-input__inner {
      min-width: 110px;
      text-align: center;
    }
  }
  .menu-list,
  .icon-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }
  &-icon-popover {
    width: 458px;
    overflow: hidden;
  }
  &-icon-inner {
    width: 478px;
    max-height: 258px;
    overflow-x: hidden;
    overflow-y: auto;
  }
  &-icon-list {
    width: 458px;
    padding: 0;
    margin: -8px 0 0 -8px;
    > .el-button {
      padding: 8px;
      margin: 8px 0 0 8px;
      > span {
        display: inline-block;
        vertical-align: middle;
        width: 18px;
        height: 18px;
        font-size: 18px;
      }
    }
  }
}
</style>
