<template>
    <div class="message">
        <div class="messageTop">
            <p class="on">系统消息</p>
            <!-- <p class="li">活动消息</p> -->
            <!-- <p class="li">服务消息</p> -->
        </div>
        <div class="system">
            <div
                class="systemContent"
                v-for="(item, index) in dataList"
                :key="index"
            >
                <div class="systemContent-top" style="margin: 0 0 5px">
                    <div style="display: flex">
                        <Badge
                            color="red"
                            v-if="item.status == '0' ? true : false"
                        />
                        <p
                            class="systemContent-notice"
                            @click="modalDisplay(item.id)"
                            :style="
                                item.status == '0'
                                    ? 'color: #333333'
                                    : 'color:#999999'
                            "
                        >
                            {{ item.messageTitle }}
                        </p>
                    </div>
                    <p
                        class="systemContent-time"
                        style="color: #999999; line-height: 21px"
                    >
                        {{ item.createDate }}
                    </p>
                </div>
                <div class="systemContent-top">
                    <p
                        :class="
                            item.status == '0'
                                ? 'systemContent-time2'
                                : 'systemContent-time'
                        "
                        v-html="item.messageContent"
                        style="
                            max-width: 726px;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                        "
                    >
                        {{ item.messageContent }}
                    </p>
                    <p
                        class="systemContent-time1"
                        @click="modalDisplay(item.id)"
                    >
                        查看详情>
                    </p>
                    <!-- {{item.id}} -->
                </div>
            </div>
            <Modal v-model="modal" @on-cancel="cancel">
                <p
                    slot="header"
                    style="text-align: center; height: 87px; padding: 31px 0 0"
                >
                    <span
                        style="
                            font-size: 16px;
                            font-family: PingFangSC-Mefium, PingFang SC;
                            font-weight: 500;
                            color: #333333;
                            margin-bottom: 10px;
                        "
                        >{{ itemData.messageTitle }}</span
                    >
                    <span
                        style="
                            font-size: 12px;
                            color: #999999;
                            font-family: PingFangSC-Regular, PingFang SC;
                        "
                        >{{ itemData.createDate }}</span
                    >
                </p>
                <div>
                    <Input
                        v-html="itemData.messageContent"
                        type="textarea"
                        style="
                            font-size: 12px;
                            font-family: PingFangSC-Regular, PingFang SC;
                            font-weight: 400;
                            color: #666666;
                        "
                        >{{ itemData.messageContent }}</Input
                    >
                </div>
                <div slot="footer">
                    <Button
                        type="error"
                        size="large"
                        long
                        @click="sure"
                        class="systemContent-btn"
                        >返回</Button
                    >
                </div>
            </Modal>
        </div>
        <!-- 分页 -->
        <paging
            v-if="dataList && dataList.length != 0 && totalCount > limit"
            class="paging"
            :totalCount="totalCount"
            :current="page"
            :pageSize="limit"
            @handlePageChange="handlePageChange"
        />
        <div v-else style="height: 60px"></div>
    </div>
</template>
<script>
    import paging from "@/components/common/paging";
    import { mapState, mapActions, mapMutations } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    import {
        messagePage,
        messageDetail,
    } from "@/api/api_06-09messageNotification.js";
    export default {
        name: "messageNotification",
        components: {
            paging,
        },
        head() {
            return {
                title: "消息通知",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        data() {
            return {
                totalCount: 1,
                page: 1,
                limit: 10,
                modal: false,
                // loading:false,
                isReader: true,
                // status: '',
                id: "",
                dataList: [
                    {
                        MessageTitle: "",
                        createDate: "",
                        MessageContent: "",
                        id: "",
                    },
                ],
                itemData: {
                    MessageTitle: "",
                    createDate: "",
                    MessageContent: "",
                    id: "",
                },
            };
        },
        created() {
            this.getMessageData(1, 10);
        },
        mounted() {
            this.actGetMessageCount();
        },
        computed: {
            ...mapState("main", [
                //2.确定使用模块，引入对应功能,此方法在只使用一个vuex模块的情况下，代码量少
                "messageCountData",
            ]),
        },
        methods: {
            ...mapActions("main", ["actGetMessageCount"]),
            // 分页
            handlePageChange(val) {
                this.page = val;
                this.getMessageData(val, this.limit);
            },
            sure() {
                // this.loading = true;
                this.loading = false;
                this.modal = false;
                this.itemData = {
                    MessageTitle: "",
                    createDate: "",
                    MessageContent: "",
                };
                this.getMessageData(this.page, this.limit);
                this.actGetMessageCount();
            },
            cancel() {
                this.getMessageData(this.page, this.limit);
                this.actGetMessageCount();
            },
            modalDisplay(id) {
                (this.modal = true),
                    // this.itemData = item,
                    messageDetail({
                        messageId: id,
                    }).then((res) => {
                        if (res.code == 200) {
                            this.itemData = res.data;
                        }
                    });
            },
            //获取系统消息
            getMessageData(page, limit) {
                var obj = {
                    page: page,
                    limit: limit,
                };
                messagePage(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataList = res.data.list;
                            this.totalCount = res.data.total;
                        }
                    })
                    .catch((err) => console.log(err));
            },
        },
    };
</script>
<style lang="scss" scoped>
    .message {
        width: 948px;
        // height: 1450px;
        background: #ffffff;
        overflow: hidden;

        .messageTop {
            width: 948px;
            height: 44px;
            border-bottom: 1px solid #dd2619;

            .on {
                width: 120px;
                height: 44px;
                line-height: 44px;
                font-size: 14px;
                font-weight: 500;
                float: left;
                text-align: center;
                color: #ffffff;
                background: #dd2619;
            }

            .li {
                width: 120px;
                height: 55px;
                line-height: 55px;
                font-size: 16px;
                float: left;
                text-align: center;
                color: #666666;
            }
        }
    }

    .system {
        width: 948px;
        padding: 20px 50px 0;

        .systemContent {
            height: 76px;
            display: flex;
            flex-flow: column;
            justify-content: center;
            border-bottom: 1px solid #f6f6f6;
            &:hover {
                .systemContent-notice {
                    color: #dd2619 !important;
                    cursor: pointer;
                }
            }

            .systemContent-top {
                display: flex;
                justify-content: space-between;

                .systemContent-notice {
                    font-size: 14px;
                    line-height: 21px;
                    font-weight: 500;
                    font-family: PingFangSC-Medium, PingFang SC;
                }

                .systemContent-time {
                    font-size: 12px;
                    // line-height: 14px;
                    font-weight: 400;
                    color: #999999;
                    font-family: PingFangSC-Regular, PingFang SC;
                    overflow: hidden !important;
                    text-overflow: ellipsis !important;
                    white-space: nowrap !important;
                    // cursor:pointer;
                    display: flex;
                    cursor: pointer;
                }

                .systemContent-time1 {
                    font-size: 12px;
                    line-height: 21px;
                    font-weight: 400;
                    color: #666666;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    cursor: pointer;

                    &:hover {
                        color: #dd2619;
                    }
                }
                .systemContent-time2 {
                    font-size: 12px;
                    line-height: 21px;
                    font-weight: 400;
                    color: #666666;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    cursor: pointer;
                }
            }
            &:hover {
                .systemContent-notice {
                    color: #dd2619;
                }
            }
        }
    }

    .systemContent-btn {
        width: 60px;
        height: 30px;
        line-height: 30px;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
    }

    /deep/ .ivu-modal {
        width: 500px !important;
        height: 367px;
    }

    /deep/ .ivu-modal-content {
        height: 367px;
    }

    /deep/ .ivu-modal-body {
        width: 420px;
        height: 192px;
        margin: 0 auto;
        padding: 18px 0 !important;
    }

    /deep/ .ivu-modal-header {
        border-bottom: none;
        padding: 0px !important;
    }

    /deep/ .ivu-modal-header p {
        // height: 5px;
        // line-height: 55px;
        font-size: 16px;
        color: #333333 !important;
        display: flex;
        flex-flow: column;
    }

    /deep/ .ivu-modal-footer {
        border-top: none;
        text-align: center;
        // margin: 62px 0 0;
        padding: 0px !important;
    }
    /deep/ textarea.ivu-input {
        height: 275px;
    }
    /deep/ .ivu-badge-status {
        margin-left: -13px;
    }
    /deep/ .ivu-badge-status-text {
        margin-left: 3px;
    }
</style>
