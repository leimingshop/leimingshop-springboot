<template>
    <div class="blockWrap shoppingAddress">
        <div class="s-head">
            <h4 v-text="title"></h4>
            <ButtonGroup>
                <Button type="text" @click="$emit('handleVisibleModalAddressList')">切换地址</Button>
                <!-- <Button type="text" @click="$emit('handleVisibleModalEditAddress', 'add')">
                    <Icon type="md-add" size="18"></Icon>
                    <span>添加收货地址</span>
                </Button> -->
            </ButtonGroup>
        </div>


        <Card :bordered="true" class="address-wrap">
            <div class="user-info">
                <p>
                    <span v-text="adressInfo.consignee"></span>
                    <span v-text="afterSaleAddress.receiver"></span>
                </p>

                <p>
                    <span v-text="adressInfo.phoneNumber"></span>
                    <span v-text="afterSaleAddress.receiverPhone"></span>
                </p>

                <p>
                    <span v-text="adressInfo.shoppingAddress"></span>
                    <span v-text="afterSaleAddress.receiverAddress"></span>
                </p>
            </div>

            <Button
                class="e-btn"
                type="default"
                shape="circle"
                @click="$emit('handleVisibleModalEditAddress')"
            >{{ editAddress.text }}</Button>
        </Card>
    </div>
</template>

<script>
    export default{
        name: 'shoppingAddress',
        data(){
            return{
                title: '收货地址',
                adressInfo: {
                    consignee: '收货人：',
                    phoneNumber: '联系方式：',
                    shoppingAddress: '收货地址：'
                },

                editAddress:{
                    text:  '修改地址'
                }
            }
        },

        props: {
            afterSaleAddress:{
                type: Object,
                default: () => {}
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

<style lang="scss" scoped>
    .s-head{
        display: flex;
        justify-content: space-between;
        h4{
            display: inline-block;
        }
        button{
            margin: 13px 0;
            padding: 0;
            &:last-of-type{
                margin-left: 26px;
            }
            .ivu-icon{
                vertical-align: -3px;
            }
        }
    }

    .address-wrap{
        /deep/ .ivu-card-body{
            height: 100%;
            padding: 40px 82px 40px 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .user-info{
            width: 82%;
            p{
                height: 30px;
                line-height: 30px;
                color: #333333;
                &:last-of-type{
                    height: auto;
                }
            }
        }

        .e-btn{
            width: 120px;
            height: 44px;
        }
    }
</style>