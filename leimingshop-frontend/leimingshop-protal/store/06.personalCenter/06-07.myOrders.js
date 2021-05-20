
const namespaced = true
// 数据
let state = () => ({
   reasonList: [],//取消订单理由列表
   id: "", //取消的订单id
   logisticsList: {},//物流的相关信息
   logisticsFlag: false,//用来判断物流是否有信息
   currentLabelnum: 0 //订单列表导航位置 
})

// 事件
let actions = {

}

// 改变
let mutations = {
   //改变取消订单原因的数据
   getReasonList(state, params) {
      console.log("进来了");
      state.reasonList = params.reasonList;
      state.id = params.id;
      console.log(state.reasonList.length);
   },
   getLogisticsList(state, params) {
      state.logisticsList = params.logisticsList;
      let logObject = state.logisticsList;
      //   if (Object.keys(logObject).length) {
      //    state.logisticsFlag=true
      //  } else {
      //     state.logisticsFlag=false
      //  }
      if (Object.keys(logObject).length != 0 && logObject.data) {
         state.logisticsFlag = true
      } else {
         params._that.$Message.warning('暂无物流信息，请稍后查询。');
         state.logisticsFlag = false
      }
   },
   setCurrentLabelnum(state, params) {
      state.currentLabelnum = params.currentLabelnum
   }
}
// 获取
let getters = {

}
export default {
   namespaced,
   actions,
   state,
   mutations,
   getters
}
