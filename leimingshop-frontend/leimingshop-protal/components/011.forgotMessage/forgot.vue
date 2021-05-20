<template>
    <div>
        <Steps :current="current">
            <Step content="填写账户号"></Step>
            <Step content="验证身份"></Step>
            <Step content="设置新密码"></Step>
            <Step content="完成"></Step>
        </Steps>
        <Button type="primary" @click="next">下一步</Button>
    </div>
</template>
<script>
export default {
    data(){
        return{

        }
    },
    methods:{
        next() {
                if (this.current == 3) {
                    this.$router.replace({
                        path: 'revise'
                    })
                } else {
                    this.current += 1;
                }
            }
    }
}
</script>
<style lang="scss" scoped>

</style>