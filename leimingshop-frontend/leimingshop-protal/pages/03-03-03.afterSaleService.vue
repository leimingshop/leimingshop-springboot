<template>
    <div 
        class="after-sale-service"
        v-if="afterSale"
    >
        <div v-html="afterSale"></div>
    </div>

    <div
        v-else
        v-text="noData"
    ></div>
</template>

<script>
    export default{
        name: 'afterSaleService',
        data(){
            return{
                noData: '暂无售后服务'
            }
        },
        props: {
            afterSale: {
                type: String,
                default: ''
            }
        },
        components:{

        },
        created(){

        },
        computed:{

        },
        watch:{

        },
        mounted(){

        },
        methods:{

        }
    }
</script>

<style lang="scss" scoped></style>