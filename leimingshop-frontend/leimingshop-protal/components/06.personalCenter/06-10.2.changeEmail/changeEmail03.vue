<template>
    <div class="content">
        <!-- <img :src="'/static/img/10.register/success@2x.png'"/> -->
        <div>
            <div class="content-text">
                <img
                    :src="'/img/10.register/success@2x.png'"
                    style="width: 60px; height: 60px; margin: 0 auto"
                />
                <span
                    style="
                        margin: 20px 0 10px;
                        font-size: 14px;
                        font-family: PingFangSC-Medium, PingFang SC;
                        color: #222222;
                        height: 14px;
                        line-height: 14px;
                    "
                    >邮箱更换成功</span
                >
            </div>
            <div class="content-text1">
                <span>您可以在下次使用新号码进行登录</span>
            </div>
        </div>
        <div>
            <Button
                class="content-btn"
                @click="$router.push('/securitySetting')"
                >返回</Button
            >
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {};
        },
        methods: {},
    };
</script>
<style lang="scss" scoped>
    .content {
        width: 250px;
        text-align: center;
        margin: 0 auto;
    }
    .content-text {
        font-size: 20px;
        font-weight: 600;
        color: #333333;
        display: flex;
        flex-flow: column;
    }

    .content-text1 {
        font-size: 12px;
        font-weight: 400;
        font-family: PingFangSC-Regular, PingFang SC;
        color: #999999;
        // margin: 8px 0 46px 0;
    }

    .content-btn1 {
        width: 80px;
        height: 30px;
        font-size: 20px;
        margin: 0 50px;
        color: #fff;
        background: #d40d28;
        border-radius: 2px;
    }

    .content-btn {
        width: 80px;
        height: 30px;
        border-radius: 22px;
        border: 1px solid #cccccc;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        margin: 20px auto;
    }
</style>
