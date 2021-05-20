<template>
    <div class="counter">
        <Button
            type="text"
            :class="{ limitValue: number == min }"
            @click="handleCount('down', $event)"
            style="line-height: 15px"
        >
            <img style="width: 30px; height: 30px" :src="reduceImg" alt="" />
        </Button>

        <InputNumber
            ref="inputNumber"
            v-model="number"
            :min="min"
            :max="max"
            @on-change="handleChange"
        ></InputNumber>

        <!-- 解决Chrome浏览器自动填充input的问题 -->
        <input
            v-model="number"
            autocomplete="off"
            style="width: 0; opacity: 0; border: 0"
        />

        <Button
            type="text"
            :class="{ limitValue: number == max }"
            @click="handleCount('up', $event)"
        >
            <img style="width: 30px; height: 30px" :src="increaseImg" alt="" />
        </Button>
    </div>
</template>

<script>
    export default {
        name: "counter",

        model: {
            prop: "currentNum",
            event: "handleChange",
        },

        props: {
            currentNum: {
                type: Number,
                default: 1,
            },

            min: {
                type: Number,
                default: 1,
            },

            max: {
                type: Number,
                default: 10000000,
            },
        },

        watch: {
            currentNum: {
                immediate: true,
                handler(val) {
                    this.number = val;
                },
            },
        },

        data() {
            return {
                reduceImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_reduce.png"), // '+'图片
                increaseImg: require("~/static/img/03.goodsClass/03-03.goodsDetail/num_increase.png"), // '-'图片
                number: 1,
            };
        },

        methods: {
            async handleCount(type, e) {
                await this.$nextTick();
                this.$refs.inputNumber[type](e);
            },

            handleChange(val) {
                // 清空时触发判断
                if (!val) {
                    this.number = this.$refs.inputNumber.currentValue = val = this
                        .min;
                }
                this.$emit("handleChange", val);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .counter {
        font-size: 0;
    }

    .ivu-input-number {
        height: 30px;
        width: 52px;
        border-radius: 0;
        border-width: 1px 0;
        border-style: solid;
        border-color: #f5f5f5;
        transition: none;
        line-height: 30px;

        &.ivu-input-number-focused {
            box-shadow: none;
        }
        /deep/ .ivu-input-number-handler-wrap {
            display: none;
        }
        /deep/ .ivu-input-number-input-wrap {
            input {
                font-size: 14px;
                text-align: center;
            }
        }
    }

    button {
        width: 30px;
        height: 30px;
        padding: 0;
        border-radius: 0;
        background: #f5f5f5;
        /deep/ span {
            width: 100%;
            height: 100%;
            display: block;
        }
        &.limitValue {
            opacity: 0.6;
            cursor: not-allowed;
        }
        // &:hover{
        //     background: #CECECE;
        //     &.limitValue{
        //         background: #F5F5F5;
        //     }
        // }
        &:nth-of-type(1) {
            border-radius: 2px 0 0 2px;
            img {
                vertical-align: sub;
            }
        }
        &:nth-of-type(2) {
            border-radius: 0 2px 2px 0;
            img {
                vertical-align: -9px;
            }
        }
    }
    /deep/ .ivu-input-number-input {
        height: 30px;
        line-height: 30px;
    }
</style>
