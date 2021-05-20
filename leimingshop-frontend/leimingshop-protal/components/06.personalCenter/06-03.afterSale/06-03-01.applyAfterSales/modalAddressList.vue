<template>
    <Modal
        v-model="visible"
        :mask-closable="false"
        width="690"
        class="address-wrap"
        v-loading="addressListLoading"
        @on-ok="handleConfirm"
        @on-cancel="handleCancel"
    >
        <div slot="header">
            <span class="h-title" v-text="title"></span>
        </div>

        <vue-scroll :ops="ops">
            <div class="content address-list">
                <Card
                    :bordered="true"
                    v-for="(item, index) in addressList"
                    :key="item.id"
                    :class="{active: handleActiveBool(item, index) }"
                    @click.native="handleSelect(index)"
                >
                    <div class="user-info">
                        <p>
                            <span v-text="addressInfo.consignee"></span>
                            <span v-text="item.trueName"></span>
                        </p>

                        <p>
                            <span v-text="addressInfo.phoneNumber"></span>
                            <span v-text="item.mobPhone"></span>
                        </p>

                        <p>
                            <span v-text="addressInfo.shoppingAddress"></span>
                            <span v-text="item.areaInfo"></span>
                        </p>

                        <p class="defaultAddress" v-if="item.defaultFlag == 1">默认地址</p>
                    </div>
                </Card>
            </div>
        </vue-scroll>
    </Modal>
</template>

<script>
    export default{
        name: 'addressList',
        data(){
            return{
                title: '选择收货地址',
                visible: false,
                addressInfo: {
                    consignee: '收货人：',
                    phoneNumber: '联系方式：',
                    shoppingAddress: '收货地址：'
                },
                tabVal: undefined,
                ops:{
                    bar:{
                        hoverStyle: true,
                        onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
                        background: '#DD2619',   //颜色
                    },

                    scrollPanel:{
                        scrollingX: false
                    }
                }
            }
        },

        props: {
            memberAddressId: {
                type: String,
                default: ''
            },

            addressList: {
                type: Array,
                default: () => []
            },

            addressListLoading: {
                type: Boolean,
                default: false
            },
        },


        components:{

        },
        created(){

        },
        computed:{
        },

        watch:{
            defaultAddressId:{
                immediate: false,
                handler(newVal, oldVal){
                    this.$set(this.cardSelect, 'id', newVal)
                }
            }
        },
        mounted(){

        },
        methods:{

            // 是否选中
            handleActiveBool(item, index){
                // 选择
                if( this.tabVal !== undefined ) return this.tabVal == index
                // 默认打开弹窗(第一次无选中状态)
                else return this.memberAddressId == item.id
            },

            handleSelect(index){
                this.tabVal = index
            },

            handleCancel(){
                this.tabVal = undefined
            },

            handleConfirm(){
                let afterAddress = this.addressList[ this.tabVal ]
                this.$emit('HANDEL_AFTER_ADDRESS', afterAddress)
                this.$emit('HANDEL_MEMBER_ADDRESS', afterAddress)
                this.tabVal = undefined
            },

        }
    }
</script>

<style lang="scss" scoped>
    @import '@/assets/scss/modules/modal.scss';
    $primary-color: #DD2619;

    /deep/ .ivu-modal-content{
        border-radius: 12px;
    }

    .ivu-card{
        cursor: pointer;
        &:hover{
            border: 1px solid #cccccc;
        }
        &.active{
            border: 1px solid $primary-color;
        }
    }

    .address-wrap{
        .address-list{
            margin: 0 60px;
        }

        .ivu-card{
            height: 140px;
            margin-bottom: 20px;
        }

        /deep/ .ivu-card-body{
            height: 100%;
            padding: 30px 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .user-info{
            p{
                height: 30px;
                line-height: 30px;
                color: #333333;
                &.defaultAddress{
                    font-size: 14px;
                    color: $primary-color;
                    position: absolute;
                    right: 40px;
                    bottom: 30px;
                }
            }
        }
    }
</style>