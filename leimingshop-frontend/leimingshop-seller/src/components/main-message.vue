<template>
    <div class="main-tips-message">
        <a @click="showListMessage"><img :src="noticeImgSrc" title="提醒" /></a>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                noticeImgSrc: require("../assets/img/notice.png"),
            };
        },
        methods: {
            showListMessage() {
                console.log("触发点击提示事件");
                this.$store.commit("showListMessage");
            },
        },
    };
</script>
<style lang="scss" scoped>
    .main-tips-message {
        margin-left: 10px;
        a {
            cursor: pointer;
        }
        img {
            width: 18px;
            height: 18px;
        }
    }
</style>