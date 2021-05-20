import Vue from 'vue'
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'

import imgMagnifier from 'img-magnifier'
import 'img-magnifier/lib/img-magnifier.css'

Vue.use(imgMagnifier)

// 图片预览功能
Vue.use(Viewer, {
    defaultOptions: {
        zIndex: 9999
    }
})