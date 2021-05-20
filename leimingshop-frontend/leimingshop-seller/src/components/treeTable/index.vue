
<template>
 <el-table ref="multipleTable" :data="formatData" :row-style="showRow" v-bind="$attrs">   <!--  @header-click="chooseall" -->
    <el-table-column :render-header="renderHeader" width="50" align="center">
      <template slot-scope="scope">
        <el-checkbox v-model="scope.row.checks" @change="toselect(scope.row)"></el-checkbox>
      </template>
    </el-table-column>
    <el-table-column v-if="columns.length===0" width="150">
      <template slot-scope="scope">
        <span v-for="space in scope.row._level" :key="space" class="ms-tree-space"/>
        <span v-if="iconShow(0,scope.row)" class="tree-ctrl" @click="toggleExpanded(scope.$index)">
          <i v-if="!scope.row._expanded" class="el-icon-plus"/>
          <i v-else class="el-icon-minus"/>
        </span>
        {{ scope.$index }}
      </template>
    </el-table-column>
    <el-table-column v-for="(column, index) in columns" v-else :key="column.value" :label="column.text" :width="column.width">
      <template slot-scope="scope">
        <!-- Todo -->
        <!-- eslint-disable-next-line vue/no-confusing-v-for-v-if -->
        <span v-for="space in scope.row._level" v-if="index === 0" :key="space" class="ms-tree-space"/>
        <span v-if="iconShow(index,scope.row)" class="tree-ctrl" @click="toggleExpanded(scope.$index)">
          <i v-if="!scope.row._expanded" class="el-icon-plus"/>
          <i v-else class="el-icon-minus"/>
        </span>
        {{ scope.row[column.value] }}
      </template>
    </el-table-column>
    <slot/>
  </el-table>
</template>
 
<script>
/**
  Auth: Lei.j1ang
  Created: 2018/1/19-13:59
*/
import treeToArray from "./eval";
export default {
  name: "TreeTable",
  data() {
    return {
      chooseson: true, //全选
      key: true //单个点击直到全部选中
    };
  },
  props: {
    /* eslint-disable */
    data: {
      type: [Array, Object],
      required: true
    },
    columns: {
      type: Array,
      default: () => []
    },
    evalFunc: Function,
    evalArgs: Array,
    expandAll: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    // 格式化数据源
    formatData: function() {
      let tmp;
      if (!Array.isArray(this.data)) {
        tmp = [this.data];
      } else {
        tmp = this.data;
      }
      const func = this.evalFunc || treeToArray;
      const args = this.evalArgs
        ? [tmp, this.expandAll].concat(this.evalArgs)
        : [tmp, this.expandAll];
      return func.apply(null, args);
    }
  },
  methods: {
    showRow: function(row) {
      const show = row.row.parent
        ? row.row.parent._expanded && row.row.parent._show
        : true;
      row.row._show = show;
      return show
        ? "animation:treeTableShow 1s;-webkit-animation:treeTableShow 1s;"
        : "display:none;";
    },
    // 切换下级是否展开
    toggleExpanded: function(trIndex) {
      const record = this.formatData[trIndex];
      record._expanded = !record._expanded;
    },
    // 图标显示
    iconShow(index, record) {
      return index === 0 && record.child && record.child.length > 0;
    },
 
    //设置表头全选
    renderHeader(h, data) {
      return h("span", [
        h("input", {
          attrs: {
            id: "chooseall",
            type: "checkbox",
            style:
              "border: 1px solid #dcdfe6;border-radius: 2px;box-sizing: border-box;width: 14px;height: 14px;background-color: #fff;"
          }
        })
      ]);
    },
    //功能函数:选中部分子集
    setchildtobeselect(arr, key) {
      arr.forEach((v, i) => {
        v.checks = key;
        // v._expanded = key;//选中后展开子项
        if (v.child) {
          this.setchildtobeselect(v.child, v.checks);
        }
      });
    },
    //是否所有的都被选中
    isallchecked(arr) {
      arr.forEach((v, i) => {
        if (!v.checks) {
          this.key = false;
        }
        if (v.child) {
          this.isallchecked(v.child);
        }
      });
    },
    //设置父级为 未选中状态（父级的父级没改变-有bug）
    setparentfalse(arr, id, level) {
      arr.forEach((v, i) => {
        if (v._level == level - 1 && v.child) {
          v.child.forEach((val, ind) => {
            if (val.id == id) {
              v.checks = false;
              return false; //终止此次循环，减少循环次数
            }
          });
        }
        if (v.child) {
          this.setparentfalse(v.child, id, level);
        }
      });
    },
    //设置父级为 选中状态
    setparenttrue(arr, id, level) {
      arr.forEach((v, i) => {
        if (v._level == level - 1 && v.child) {
          let key = true;
          let sameidkey = false;
          v.child.forEach((val, ind) => {
            if (val.id == id) {
              //确保当前点击的在该父级内
              sameidkey = true;
            }
            if (!val.checks) {
              key = false;
            }
          });
          if (key && sameidkey) {
            v.checks = true;
          }
        }
        if (v.child) {
          this.setparentfalse(v.child, id, level);
        }
      });
    },
    //某个复选框被点击时
    toselect(row) {
      console.log(row);
      // row._expanded = row.checks;//选中后是否展开
      //1、若有子集先让子选中
      if (row.child) {
        this.setchildtobeselect(row.child, row.checks);
      }
      //2、然后判断是否全选中
      this.key = true; //重置为true，防止上次已经是false的状态
      this.isallchecked(this.formatData);
      //3、设置多选框的状态
      if (!row.checks) {
        this.setparentfalse(this.formatData, row.id, row._level); //设置父级选中的状态为false
        document.getElementById("chooseall").checked = false; //设置全选框的状态
      } else {
        this.setparenttrue(this.formatData, row.id, row._level); //设置父级选中的状态为true
      }
      if (this.key) {
        document.getElementById("chooseall").checked = true; //设置全选框的状态
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      var that = this;
      const all = document.getElementById("chooseall");
      all.onchange = function(e) {
        console.log(all.checked);
        if (all.checked == true) {
          that.setchildtobeselect(that.formatData, true);
        } else {
          that.setchildtobeselect(that.formatData, false);
        }
      };
    });
  }
};
</script>
<style rel="stylesheet/css">
@keyframes treeTableShow {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
@-webkit-keyframes treeTableShow {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
 
<style scoped>
.ms-tree-space {
  position: relative;
  top: 1px;
  display: inline-block;
  font-style: normal;
  font-weight: 400;
  line-height: 1;
  width: 18px;
  height: 14px;
}
.ms-tree-space::before {
  content: "";
}
.processContainer {
  width: 100%;
  height: 100%;
}
table td {
  line-height: 26px;
}
.tree-ctrl {
  position: relative;
  cursor: pointer;
  color: #2196f3;
  margin-left: -18px;
}
</style>
