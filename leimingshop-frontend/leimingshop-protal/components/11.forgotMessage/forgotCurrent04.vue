<template>
    <div style="text-align: center">
        <!-- <img :src="'/static/img/10.register/success@2x.png'"/> -->
        <div>
            <div class="content-text">
                <img :src="'/img/10.register/success@2x.png'" style="width: 30px; height: 30px" />
                <span>密码重置成功</span>
            </div>
            <div class="content-text1">
                <span>下次请使用新密码进行登录</span>
            </div>
        </div>
        <div>
            <Button type="primary" class="successBtn" style="margin: 0 50px;" @click="toSuccess">立即登录</Button>
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {};
        },
        methods: {
            toSuccess() {
                this.$router.push({
                    path: "/login",
                });
            },
        },
    };
</script>
<style lang="scss" scoped>
    .content-text {
        font-size: 29px;
        font-weight: 500;
        color: #222;
        line-height: 43px;

        img {
            display: inline-block;
            vertical-align: middle;
        }
    }

    .content-text1 {
        font-size: 18px;
        color: #666666;
        margin: 8px 0 46px 0;
    }

    .successBtn {
        width: 348px;
        height: 46px;
        font-size: 15px;
        color: white;
        border-color: #d40d2b;
        background: #dd2619;
    }
</style>
