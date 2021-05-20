<template>
    <Poptip placement="bottom-start" v-model="datas.visible" width="170">
        <div
            style="cursor: pointer"
            class="content-good-select"
            @click="haha(datas, list)"
        >
            促销<img src="/img/04.shoppingCar/open.png" alt="" />
        </div>
        <div class="api" slot="content">
            <RadioGroup
                v-model="datas.activityId"
                vertical
                @on-change="changeRadio"
            >
                <Radio
                    :label="smallActivityItem.id"
                    v-for="(smallActivityItem,
                    smallActivityIndex) in activityList"
                    :key="smallActivityIndex"
                >
                    <span>{{ smallActivityItem.activityDescription }}</span>
                </Radio>
            </RadioGroup>
            <div
                style="
                    display: flex;
                    justify-content: space-around;
                    padding: 5px 10px;
                "
            >
                <Button size="small" class="cancel" @click.stop="cancel(datas)"
                    >取消</Button
                >
                <Button
                    type="error"
                    @click.stop="ok(datas)"
                    style=""
                    size="small"
                    >确定</Button
                >
            </div>
        </div>
    </Poptip>
</template>

<script>
    import { GoodsActivityList } from "@/api/api_04.shoppingCar.js";
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部

    export default {
        props: ["id", "list", "datas"],
        data() {
            return {
                radioId: "",
                activityList: [],
            };
        },
        methods: {
            ...mapActions("shoppingCarIndex", [
                "getcoupon", // 领取优惠券
                "addCar",
                "cartPage",
            ]),
            changeRadio(e) {
                this.radioId = e;
            },
            haha(item, list) {
                this.datas.visible = true;
                for (var i = 0; i < list.length; i++) {
                    list[i].visible = false;
                }
                this.getListData();
            },
            getListData() {
                GoodsActivityList({ goodsId: this.id })
                    .then((res) => {
                        if (res.code == 200) {
                            this.activityList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //更换活动
            ok(item) {
                let obj = {
                    cartId: item.id,
                    goodsNum: item.goodsNum,
                    specId: item.specId,
                    activityId: this.radioId,
                    activityType: item.activityType,
                };
                this.addCar(obj);
                this.radioId = "";
                item.visible = false;
            },
            cancel(item) {
                console.log("=======", item);
                item.visible = false;
                this.radioId = "";
                this.cartPage();
            },
        },
    };
</script>

<style lang="scss" scoped>
    .content-good-coupon {
        width: 38px;
        height: 16px;
        // line-height: 16px;
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        // text-align: center;
        font-size: 10px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 600;
        color: rgba(221, 38, 25, 1);
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .content-good-received {
        width: 29px;
        height: 11px;
        font-size: 8px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(102, 102, 102, 1);
        line-height: 11px;

        letter-spacing: 1px;
    }
    .content-good-receive {
        width: 26px;
        height: 14px;
        background: rgba(221, 38, 25, 1);
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        font-size: 8px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(255, 255, 255, 1);
        line-height: 14px;
        text-align: center;
        letter-spacing: 1px;
        cursor: pointer;
    }
    .content-good-select {
        width: 55px;
        height: 18px;
        // line-height: 18px;
        border-radius: 2px;
        border: 1px solid rgba(221, 38, 25, 1);
        // text-align: center;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(221, 38, 25, 1);
        display: flex;
        justify-content: center;
        align-items: center;
        // line-height: 12px;
        img {
            width: 9px;
            height: 6px;
            margin-left: 4px;
        }
    }
    /deep/ .ivu-poptip-body {
        padding: 8px 10px;
    }
    /deep/.ivu-select-small {
        color: red;
    }
    /deep/ .ivu-radio-default {
        font-size: 12px;
    }
    .ivu-select-small.ivu-select-single
        .ivu-select-selection
        .ivu-select-placeholder {
        color: red;
    }
    .api {
        // min-height: 100px;
        max-height: 150px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .ivu-btn-small {
            font-size: 12px;
        }
        .ivu-btn-error {
            background-color: rgba(221, 38, 25, 1);
        }
        .cancel {
            background: #f5f5f5;
        }
    }
</style>
