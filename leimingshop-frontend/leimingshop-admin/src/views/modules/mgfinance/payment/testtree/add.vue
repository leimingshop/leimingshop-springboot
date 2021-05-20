<template>
  <div>
    <div class="mask" v-show="isShowSelect" @click="isShowSelect = !isShowSelect"></div>
    <el-popover placement="bottom-start" :width="width" trigger="manual"
                v-model="isShowSelect" @hide="popoverHide">
      <el-tree class="common-tree" :style="style" ref="tree" :data="data" :props="defaultProps"
               :show-checkbox="multiple"
               :node-key="nodeKey"
               :check-strictly="checkStrictly"
               default-expand-all
               :expand-on-click-node="false"
               :default-checked-keys="defaultCheckedKeys"
               :highlight-current="true"
               @node-click="handleNodeClick"
               @check-change="handleCheckChange"></el-tree>
      <el-select :style="selectStyle" slot="reference" ref="select"
                 v-model="selectedData"
                 :multiple="multiple"
                 @click.native="isShowSelect = !isShowSelect"
                 class="tree-select">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </el-popover>
  </div>
</template>
 
<script>
export default {
  name: 'tree-select',
  props: {
    // 树结构数据
    data: {
      type: Array,
      default () {
        return [];
      }
    },
    defaultProps: {
      type: Object,
      default () {
        return {};
      }
    },
    // 配置是否可多选
    multiple: {
      type: Boolean,
      default () {
        return false;
      }
    },
    nodeKey: {
      type: String,
      default () {
        return 'id';
      }
    },
    // 显示复选框情况下，是否严格遵循父子不互相关联
    checkStrictly: {
      type: Boolean,
      default () {
        return false;
      }
    },
    // 默认选中的节点key数组
    checkedKeys: {
      type: Array,
      default () {
        return [];
      }
    },
    width: {
      type: Number,
      default () {
        return 250;
      }
    },
    height: {
      type: Number,
      default () {
        return 300;
      }
    }
  },
  data () {
    return {
      defaultCheckedKeys: [],
      isShowSelect: false, // 是否显示树状选择器
      options: [],
      selectedData: [], // 选中的节点
      style: 'width:' + this.width + 'px;' + 'height:' + this.height + 'px;',
      selectStyle: 'width:' + (this.width + 24) + 'px;',
      checkedIds: [],
      checkedData: []
    };
  },
  mounted () {
    if (this.checkedKeys.length > 0) {
      if (this.multiple) {
        this.defaultCheckedKeys = this.checkedKeys;
        this.selectedData = this.checkedKeys.map((item) => {
          var node = this.$refs.tree.getNode(item);
          return node.label;
        });
      } else {
        var item = this.checkedKeys[0];
        this.$refs.tree.setCurrentKey(item);
        var node = this.$refs.tree.getNode(item);
        this.selectedData = node.label;
      }
    }
  },
  methods: {
    popoverHide () {
      if (this.multiple) {
        this.checkedIds = this.$refs.tree.getCheckedKeys(); // 所有被选中的节点的 key 所组成的数组数据
        this.checkedData = this.$refs.tree.getCheckedNodes(); // 所有被选中的节点所组成的数组数据
      } else {
        this.checkedIds = this.$refs.tree.getCurrentKey();
        this.checkedData = this.$refs.tree.getCurrentNode();
      }
      this.$emit('popoverHide', this.checkedIds, this.checkedData);
    },
    // 节点被点击时的回调,返回被点击的节点数据
    handleNodeClick (data, node) {
      if (!this.multiple) {
        let tmpMap = {};
        tmpMap.value = node.key;
        tmpMap.label = node.label;
        this.options = [];
        this.options.push(tmpMap);
        this.selectedData = node.label;
        this.isShowSelect = !this.isShowSelect;
      }
    },
    // 节点选中状态发生变化时的回调
    handleCheckChange () {
      var checkedKeys = this.$refs.tree.getCheckedKeys(); // 所有被选中的节点的 key 所组成的数组数据
      this.options = checkedKeys.map((item) => {
        var node = this.$refs.tree.getNode(item); // 所有被选中的节点对应的node
        let tmpMap = {};
        tmpMap.value = node.key;
        tmpMap.label = node.label;
        return tmpMap;
      });
      this.selectedData = this.options.map((item) => {
        return item.label;
      });
    }
  },
  watch: {
    isShowSelect (val) {
      // 隐藏select自带的下拉框
      this.$refs.select.blur();
    }
  }
};
</script>
 
<style scoped>
  .mask{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    opacity: 0;
  }
  .common-tree{
    overflow: auto;
  }
</style>
 
<style>
  .tree-select .el-select__tags .el-tag .el-tag__close{
    display: none;
  }
  .tree-select .el-select__tags .el-tag .el-icon-close{
    display: none;
  }
</style>

