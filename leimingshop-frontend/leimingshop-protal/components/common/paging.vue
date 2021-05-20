<template>
    <div class="paging-comp">
        <Page
            ref="page"
            :total="totalCount"
            :current="currentPage"
            :page-size="pageSize"
            :show-elevator="elevatorVisible"
            @on-change="handlePageChange"
            @wheel.native.prevent
            style="height: 30px; padding-top: 2px"
        />

        <Button
            v-if="elevatorVisible"
            class="confirm-btn"
            type="primary"
            @click="handlePageSelect"
            >确定</Button
        >
    </div>
</template>

<script>
    export default {
        data() {
            return {
                currentPage: 1, // 当前页
            };
        },

        model: {
            prop: "pageNo",
            event: "handlePageChange",
        },

        props: {
            pageNo: {
                type: Number,
                default: 1,
            }, // 当前页
            totalCount: {
                type: Number,
                default: 1,
            }, // 数据总数

            pageSize: {
                type: Number,
                default: 20,
            }, // 每页条数
        },

        computed: {
            // 总页数
            tatalPage() {
                return Math.ceil(this.totalCount / this.pageSize);
            },

            // 显示电梯，可以快速切换到某一页
            elevatorVisible() {
                return this.tatalPage > 1 ? true : false;
            },
        },

        watch: {
            tatalPage: {
                immediate: true,
                async handler(newVal, oldVal) {
                    await this.$nextTick();

                    if (document.querySelector("#totalPage")) {
                        document.querySelector(
                            "#totalPage"
                        ).innerHTML = `共${this.tatalPage}页`;
                        this.handleElevatorPage();
                    }
                },
            },

            pageNo: {
                immediate: true,
                handler(newVal, oldVal) {
                    this.currentPage = newVal;

                    let input = document.querySelector(
                        ".ivu-page-options-elevator input"
                    );
                    if (input) input.value = newVal;
                },
            },
        },

        mounted() {
            this.handleTotalPage();

            if (this.elevatorVisible) {
                this.handleElevatorPage();
            }
        },

        methods: {
            // dom样式
            handleTotalPage() {
                if (!document.querySelector("#totalPage")) {
                    let nextPageBtn = document.querySelector(".ivu-page-next");
                    let totalPage = document.createElement("div");

                    if (!nextPageBtn || !totalPage) return;

                    totalPage.id = "totalPage";
                    totalPage.innerHTML = `共${this.tatalPage}页`;
                    this.insertAfter(totalPage, nextPageBtn);
                }
            },

            // dom样式
            handleElevatorPage() {
                let elevatorPage = document.querySelector(
                    ".ivu-page-options-elevator"
                );

                if (elevatorPage) {
                    elevatorPage.innerHTML = elevatorPage.innerHTML.replace(
                        "跳至",
                        "到第"
                    );

                    let input = document.querySelector(
                        ".ivu-page-options-elevator input"
                    );
                    input.type = "number";
                    input.value = this.currentPage;
                    input.onblur = (event) => {
                        input.value =
                            event.currentTarget.value == 0
                                ? 1
                                : event.currentTarget.value;
                        input.value = event.currentTarget.value.replace(
                            /^(0+)|[^\d]+/g,
                            ""
                        );
                        input.value =
                            event.currentTarget.value > this.tatalPage
                                ? this.tatalPage
                                : event.currentTarget.value;
                    };
                }
            },

            // dom插入元素操作
            insertAfter(newElement, targetElement) {
                var parent = targetElement.parentNode;
                if (parent.lastChild == targetElement) {
                    // 如果最后的节点是目标元素，则直接添加。因为默认是最后
                    parent.appendChild(newElement);
                } else {
                    parent.insertBefore(newElement, targetElement.nextSibling);
                    //如果不是，则插入在目标元素的下一个兄弟节点 的前面。也就是目标元素的后面
                }
            },

            // 页码改变的回调
            handlePageChange(val) {
                let input = document.querySelector(
                    ".ivu-page-options-elevator input"
                );
                if (input) input.value = val;

                this.$emit("handlePageChange", val);
                window.scroll(0, 0);
            },

            // 快捷切换页码
            handlePageSelect() {
                let inputVal = document.querySelector(
                    ".ivu-page-options-elevator input"
                ).value;
                this.currentPage = parseInt(inputVal);
                this.handlePageChange(inputVal);
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/modules/goods-list-comp.scss";
    .paging-comp {
        text-align: center;
        margin: 30px 0 40px;
        box-sizing: border-box;
        overflow: hidden;
    }
    .ivu-page {
        display: inline-block;

        /deep/ .ivu-page-item {
            min-width: 24px;
            width: 24px;
            height: 24px;
            background: #f7f7f7;
            overflow: hidden;
            border-radius: 0px;
            &:hover {
                background: #dd2619;
                border-color: #dd2619;
            }
            a {
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #4c4c4c;
                line-height: 22px;
                display: block;
                text-align: center;
                margin: auto;
                &:hover {
                    color: white;
                }
            }
        }
        /deep/ .ivu-page-item-jump-next,
        /deep/ .ivu-page-item-jump-prev {
            height: 24px;
            width: 24px;
            line-height: 24px;
        }
        /deep/ .ivu-page-item-active {
            background-color: #dd2619;
            border: 0;
            a {
                color: #ffffff;
            }
        }

        /deep/ .ivu-page-options {
            input {
                background-color: transparent;
                position: relative;
                top: 1px;
                line-height: normal;
                width: 34px;
                height: 14px;
                border: none;
                padding: 0px;
                font-size: 12px;
                &:focus {
                    border: none;
                    box-shadow: none;
                }
                &:hover {
                    border-color: none;
                }
                outline: 1px solid rgb(220, 222, 226);
                outline-offset: 4px;
            }
        }

        /deep/ .ivu-page-prev,
        /deep/ .ivu-page-next {
            height: 24px;
            background: #f7f7f7;
            line-height: 24px;
            .ivu-icon {
                font-size: 12px;
                color: #3a3a3a;
                &::after {
                    display: inline-block;
                }
            }
        }

        /deep/ .ivu-page-prev {
            padding: 0 12px 0 8px;
            &:hover {
                border-color: #dd2619;
                background-color: #dd2619;
                .ivu-icon {
                    color: white;
                }
            }
            .ivu-icon {
                &::before {
                    font-size: 14px;
                }

                &::after {
                    content: "上一页";
                    vertical-align: 2px;
                }
            }
        }

        /deep/ .ivu-page-next {
            padding: 0 8px 0 12px;
            &:hover {
                border-color: #dd2619;
                background-color: #dd2619;
                .ivu-icon {
                    color: white;
                }
            }
            .ivu-icon {
                &::before {
                    content: "下一页";
                    vertical-align: 2px;
                }
                &::after {
                    content: "\F11F";
                    font-size: 16px;
                }
            }
        }

        /deep/ .ivu-page-disabled {
            &:hover {
                border-color: rgb(220, 222, 226);
            }
            .ivu-icon {
                color: #cccccc;
            }
        }

        /deep/ #totalPage {
            display: inline-block;
            margin: 0 -10px 0 20px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #4c4c4c;
            line-height: 24px;
        }

        /deep/ input {
            text-align: center;
        }
        /deep/ .ivu-page-options-elevator {
            height: 24px;
            margin-top: -1px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #4c4c4c;
            line-height: 24px;
            vertical-align: bottom;
            // input {
            //   border-radius: 0px;
            //   font-weight: 400;
            //   line-height: normal;
            //   padding: 10px 0;
            // }
        }
    }
    .confirm-btn {
        display: inline-block;
        width: 40px;
        height: 24px;
        background: #f7f7f7;
        padding: 0;
        border: 1px solid #dddddd;
        margin-left: 10px;
        margin-top: -19px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #4c4c4c;
        position: relative;
        top: 2px;
        border-radius: 0px;
        &:hover {
            border-color: #dd2619;
            background-color: #dd2619;
            color: white;
        }
    }
    .ivu-page-item {
        a {
            line-height: 15px;
            margin: 4px 0 !important;
        }
    }
    /deep/ .ivu-page-item-jump-next,
    /deep/ .ivu-page-item-jump-prev {
        a {
            color: #dd2619;
            &:hover {
                color: #dd2619;
            }
        }
    }
</style>
