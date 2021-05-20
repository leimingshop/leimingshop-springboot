import Vue from 'vue'
import Clipboard from 'clipboard'

function clipboardSuccess() {
      console.log('success')
       Vue.prototype.$message({
         message: 'Copy successfully',
         type: 'success',
         duration: 1500
      })
    }
function clipboardError() {
        console.log('error')
        Vue.prototype.$message({
         message: 'Copy failed',
         type: 'error'
        })
    }


    export default function handleClipboard(text, event) {
           const clipboard = new Clipboard(event.target, {
             text: () => text
           })
           clipboard.on('success', () => {
             clipboardSuccess()
             clipboard.off('error')
             clipboard.off('success')
             clipboard.destroy()
           })
           clipboard.on('error', () => {
             clipboardError()
             clipboard.off('error')
             clipboard.off('success')
             clipboard.destroy()
           })
           clipboard.onClick(event)
         }