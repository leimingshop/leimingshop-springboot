import Vue from 'vue';
import Loading from '@/components/common/loading';

// Loading构造函数
const Mask = Vue.extend(Loading);

const loadingDirective = {};

loadingDirective.install = Vue => {
  // 切换组件状态函数
    const toggleLoading = (el, binding) => {
        if (binding.value) {
          Vue.nextTick(() => {
            insertDom(el, el, binding);
          });
        }
        else {
          el.instance.visible = false;
        }
    };

    // 插入Loading
    const insertDom = (parent, el) => {
        parent.appendChild(el.mask);
        el.instance.visible = true;
    };

    Vue.directive('loading', {
      bind: function(el, binding) {
        /**
         * el 添加定位
        */
        const  elPosi = el.style.position
        if(elPosi != 'relative' || elPosi != 'absolute' || elPosi != 'fixed' ){
            el.style.position = 'relative'
        }

        /**
         * 这里把Loading组件实例挂载到el上，然后再把el传参到toggleLoading中判断
         */
        const mask = new Mask({
          el: document.createElement('div'),
          data: {}
        });
        el.instance = mask;
        el.mask = mask.$el;
        el.maskStyle = {};

        binding.value && toggleLoading(el, binding);
    },

    update: function(el, binding) {
        if (binding.oldValue !== binding.value) {
          toggleLoading(el, binding);
        }
    },

    unbind: function() {
          // destory
      }
    });
};

export default loadingDirective;