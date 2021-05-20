<template>
  <div>
    <tree-table :data="data" :columns="columns" border=""/>
  </div>
</template>
 
<script>
import treeTable from "@/components/treeTable";
export default {
  name: "home",
  components: { treeTable },
  data() {
    return {
      columns: [
        {
          text: "分类管理",
          value: "event",
          width: 200
        },
        {
          text: "序号",
          value: "num",
          width: 70
        },
        {
          text: "ID",
          value: "id"
        }
      ],
      data: [
        {
          id: 0,
          event: "事件000",
          timeLine: 50,
          comment: "无"
        },
        {
          id: 1,
          event: "事件111",
          timeLine: 100,
          comment: "无",
          child: [
            {
              id: 2,
              event: "事件2",
              timeLine: 10,
              comment: "无"
            },
            {
              id: 3,
              event: "事件3",
              timeLine: 90,
              comment: "无",
              child: [
                {
                  id: 4,
                  event: "事件4",
                  timeLine: 5,
                  comment: "无"
                },
                {
                  id: 5,
                  event: "事件5",
                  timeLine: 10,
                  comment: "无"
                },
                {
                  id: 6,
                  event: "事件6",
                  timeLine: 75,
                  comment: "无"
                }
              ]
            }
          ]
        }
      ]
    };
  },
  created() {
    // 注意：初始化defaultCheckedKeys时，在created里面
    // 父组件先执行自己的created，然后子组件开始执行自己的created和mounted，最后父组件再执行自己的mounted。
    // 因为此页面加载，先执行该组件自己的created，然后执行TreeSelect组件的created和mounted，最后执行该组件的mounted
  },
  mounted() {
    // 此处初始化defaultCheckedKeys，没效果
    this.defaultCheckedKeys = [1001];
  },
  methods: {}
};
</script>
 
<style scoped>
</style>
