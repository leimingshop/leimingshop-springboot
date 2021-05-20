<template>
  <el-table-column :prop="prop" v-bind="$attrs">
    <template slot-scope="scope">
      <span @click.prevent="toggleHandle(scope.$index, scope.row)"  :key="scope.$index" :style="{ 'padding-left': ((scope.row._level || 0) * 10) + 'px' }">
        <i :class="[ scope.row._expanded ? 'el-icon-caret-bottom' : 'el-icon-caret-right' ]" :style="{ 'visibility': hasChild(scope.row) ? 'visible' : 'hidden' }"></i>
        {{ scope.row[prop] }}
      </span>
    </template>
  </el-table-column>
</template>

<script>
import isArray from 'lodash/isArray'
export default {
  name: 'table-tree-column',
  props: {
    prop: {
      type: String
    },
    treeKey: {
      type: String,
      default: 'id'
    },
    parentKey: {
      type: String,
      default: 'pid'
    },
    childKey: {
      type: String,
      default: 'children'
    }
  },
  methods: {
    hasChild (row) {
      return (isArray(row[this.childKey]) && row[this.childKey].length >= 1) || false
    },
    // 切换处理
    toggleHandle (index, row) {
      if (!this.hasChild(row)) {
        return false
      }
      var data = this.$parent.store.states.data.slice(0)
      // data[index]._expanded = !data[index]._expanded
      if (data[index]._expanded) {
        data[index]._expanded = false
      } else {
        data[index]._expanded = true
      }
      if (data[index]._expanded) {
        row[this.childKey].forEach(item => {
          item._level = (row._level || 0) + 1
          item._expanded = false
        })
        console.log(data);
        data = data.splice(0, index + 1).concat(row[this.childKey]).concat(data)
        console.log(data);
      } else {
        console.log(data);
        data = this.removeChildNode(data, row[this.treeKey])
         console.log(data);
      }
      this.$parent.store.commit('setData', data)
      // this.$nextTick(() => {
      //   this.$parent.doLayout()
      // })
    },
    // 移除子节点
    removeChildNode (data, pid) {
      console.log( pid,'ooo')
      var pids = isArray(pid) ? pid : [pid]
      if (pid.length <= 0) {
        return data
      }
      var ids = []
      for (var i = 0; i < data.length; i++) {
        if (pids.indexOf(data[i][this.parentKey]) !== -1 && pids.indexOf(data[i][this.treeKey]) === -1) {
          ids.push(data.splice(i, 1)[0][this.treeKey])
          i--
        }
      }
      return this.removeChildNode(data, ids)
    }
  }
}
</script>
