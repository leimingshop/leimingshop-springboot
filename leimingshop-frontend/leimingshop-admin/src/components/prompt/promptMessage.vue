<template>
    <span class="promptMessage" :style="{left:labelWidth+'px'}" v-html="message"></span>
</template>
<script>
export default {
    name:'promptMessage',
    // props:["message"],
    props:{
       message:{
            type: String,
            default:""
        },
        labelWidth:{
            type: Number,
            default:0
        },
    },
    methods: {
    }
}
</script>
<style>
.promptMessage{
    color: #999999;
    font-size:12px;
    position: relative;
    top:-8px;
}
</style>

