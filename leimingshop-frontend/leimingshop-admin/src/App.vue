<template>
  <transition name="el-fade-in-linear">
    <router-view />
  </transition>
</template>

<script>
import Cookies from 'js-cookie'
import { messages } from '@/i18n'
export default {
  watch: {
    '$i18n.locale': 'i18nHandle'
  },
  created () {
    this.i18nHandle(this.$i18n.locale)
  },
  methods: {
    i18nHandle (val, oldVal) {
      Cookies.set('language', val)
      document.querySelector('html').setAttribute('lang', val)
      document.title = messages[val].brand.lg
      // 非登录页面，切换语言刷新页面
      if (this.$route.name !== 'login' && oldVal) {
        window.location.reload()
      }
    }
  }
}
</script>
<style lang="scss">
  /*图片库预览*/
  .pswp{
    z-index: 3000 !important;
  }
.aui-wrapper{
  height:100%
}
.aui-content__wrapper{
      // height: calc(100vh-30px);
}
/* bottom分割线 */
.grayLine{
      border-bottom: 2px solid #EFEFEF !important;
}
// .topGapPadding{
//   padding:40px 40px  2px 40px  !important;
// }
// .botttomGapPadding{
//    padding: 10px 40px;
// }
// .el-table .cell,
.el-table th div, .el-table--border td:first-child .cell, .el-table--border th:first-child .cell {
    text-align: center;
}
.el-form-item__error{
  padding-top: 1px !important;
}
.el-form-item{
	margin-bottom: 14px !important;
}
.el-form-item__label{
  color: #333 !important;
}
.el-dialog{
   min-width: 420px;
}
.el-dialog__title{
  font-weight: bold;
}
.inforTit{
  float: left;
  font-size: 15px;
  display: inline-block;
  min-width: 100px;
}
.orderConList p{
		display: flex;
	}
// .el-popover{
//     background: black !important;
// }
// .el-popover__title{
//     color:white  !important;
// }
// .el-popover .popper__arrow::after{
//     // background: black !important;
//     border-top-color: black !important;
// }

.el-step__head.is-process {
  color: #C0C4CC  !important;
  border-color: #C0C4CC  !important;
}

.timeClass{
    /deep/ .el-date-picker__header{
       display: none
     }
}
.el-step__title.is-process{
  font-weight: unset !important;
  font-size: 16px !important;
  color: #C0C4CC  !important;
}


</style>
