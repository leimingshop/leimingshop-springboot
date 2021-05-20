import Vue from 'vue';
const isServer = Vue.prototype.$isServer;

/* istanbul ignore next */
export const on = (function() {
    if (!isServer && document.addEventListener) {
        return function(element, event, handler) {
            if (element && event && handler) {
                element.addEventListener(event, handler, false);
            }
        };
    } else {
        return function(element, event, handler) {
            if (element && event && handler) {
                element.attachEvent('on' + event, handler);
            }
        };
    }
})();

/* istanbul ignore next */
export const off = (function() {
    if (!isServer && document.removeEventListener) {
        return function(element, event, handler) {
            if (element && event) {
                element.removeEventListener(event, handler, false);
            }
        };
    } else {
        return function(element, event, handler) {
            if (element && event) {
                element.detachEvent('on' + event, handler);
            }
        };
    }
})();

export const getScroll = function(target, top) {
    const prop = top ? 'pageYOffset' : 'pageXOffset';
    const method = top ? 'scrollTop' : 'scrollLeft';
    let ret = target[prop];

    if (typeof ret !== 'number') {
        ret = window.document.documentElement[method];
    }

    return ret;
}


export const getOffset = function(element) {
    const rect = element.getBoundingClientRect();

    const scrollTop = getScroll(window, true);
    const scrollLeft = getScroll(window);

    const docEl = window.document.body;
    const clientTop = docEl.clientTop || 0;
    const clientLeft = docEl.clientLeft || 0;

    return {
        top: rect.top + scrollTop - clientTop,
        left: rect.left + scrollLeft - clientLeft
    };
}

export const handleScroll = function(el, offsetTop, style) {
    if(!el || el.length == 0) return

    const scrollTop = getScroll(window, true);

    // Fixed Top
    if (scrollTop > offsetTop) {
        el.style.cssText = style
    } else{
        el.style.cssText = ''
    }
}

// 防抖
export const debounce = function(fn,wait){
    var timer = null;
    return function(){
        if(timer !== null){
            clearTimeout(timer);
        }
        timer = setTimeout(fn,wait);
    }
}