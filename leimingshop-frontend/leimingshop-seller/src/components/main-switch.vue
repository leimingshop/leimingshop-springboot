<template>
    <div>
        <a @click="switchHide"
            ><img
                :src="$store.state.mainSwitch ? windowImgSrc : fullScreenSrc"
                :title="$store.state.mainSwitch ? '窗口化' : '全屏'"
            />
        </a>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                windowImgSrc: require("../assets/img/window.png"),
                fullScreenSrc: require("../assets/img/full-screen.png"),
            };
        },
        methods: {
            switchHide() {
                this.$store.commit("hideOrDisplay");
            },
        },
    };
</script>
<style lang="scss" scoped>
    a {
        cursor: pointer;
    }
    img {
        width: 18px;
        height: 18px;
    }
</style>