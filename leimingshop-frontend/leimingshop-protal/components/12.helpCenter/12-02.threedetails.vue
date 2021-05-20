<template>
    <div class="system">
        <div
            class="systemContent"
            v-for="(item, index) in myHelpList"
            :key="index"
        >
            <div class="systemContent-top" style="margin: 14px 0px 14px">
                <div style="display: flex">
                    <!-- <Badge color="red" /> -->
                    <p class="systemContent-notice">{{ item.articleTitle }}</p>
                </div>
            </div>
            <div class="systemContent-top">
                <div class="systemContent-time" style="width: 726px">
                    <span v-html="item.articleContent"></span>
                </div>
                <div class="systemContent-time1">
                    <span @click="modalDisplay(item.id)">查看详情></span>
                </div>
            </div>
        </div>

        <!-- 分页 -->
        <paging
            class="paging"
            :totalCount="totalCount"
            :current="page"
            :pageSize="limit"
            @handlePageChange="handlePageChange"
            v-if="myHelpList.length !== 0 && totalCount > limit"
        />
        <div v-else style="height: 60px"></div>
        <!-- 弹出模态框 -->
        <Modal v-model="modal" @on-cancel="cancel">
            <p slot="header">
                <span>{{ itemData.articleTitle }}</span>
            </p>
            <div class="header-top">
                <p v-html="itemData.articleContent"></p>
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
</template>

<script>
    import paging from "@/components/common/paging";

    // 分类下的问题列表和查看问题详情接口
    import {
        getMyHelpList,
        getquestionDetails,
    } from "@/api/api_06-14.helpCenter.js";

    export default {
        name: "helpCenter",
        props: ["acId"],
        components: {
            paging,
        },
        data() {
            return {
                modal: false,
                page: 1,
                limit: 10,
                totalCount: 1,
                id: "",
                // 右侧子分类下的问题列表数据
                myHelpList: [
                    {
                        // acCode: 3,
                        acId: "",
                        acName: "",
                        articleTitle: "",
                        articleUrl: "",
                        createDate: "",
                        id: "",
                        updateDate: "",
                    },
                ],

                // 查看详情接口数据
                itemData: {
                    acCode: 3,
                    acId: "",
                    articleContent: "",
                    articleTitle: "",
                },
            };
        },
        mounted() {
            // this.getMyHelpListDate(1, 10);
        },
        methods: {
            // 获取分类下的问题列表
            getMyHelpListDate(page, limit, acId) {
                var obj = {
                    page: page,
                    limit: limit,
                    acId: this.acId,
                };
                getMyHelpList(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            console.log(res);
                            this.myHelpList = res.data.list;
                            this.totalCount = res.data.total;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 每切换一次分页就调用一次接口
            handlePageChange(val) {
                this.page = val;
                this.getMyHelpListDate(val, this.limit);
            },

            // 模态框
            modalDisplay(id) {
                this.modal = true;
                // 根据接口数据只能传字符串id，不能使用对象({id})这种形式
                getquestionDetails(id)
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.itemData = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },

            // 返回
            sure() {
                this.loading = false;
                this.modal = false;
                this.itemData = {
                    acId: "",
                    articleContent: "",
                    articleTitle: "",
                };
                this.getMyHelpListDate(this.page, this.limit);
            },
            // 关闭
            cancel() {
                this.getMyHelpListDate(this.page, this.limit);
            },
        },
        watch: {
            acId: {
                deep: true, // 深度监听
                immediate: true, // 监听到后，立即执行 handler方法
                handler(val, oldval) {
                    this.getMyHelpListDate(1, 10);
                },
            },
        },
    };
</script>

<style lang="scss" scoped>
    .system {
        width: 948px;
        height: 1240px;
        padding: 20px 32px 0;

        .systemContent {
            min-height: 102px;
            display: flex;
            flex-flow: column;
            justify-content: center;
            border-bottom: 1px solid #ebebeb;

            .systemContent-top {
                display: flex;
                justify-content: space-between;

                .systemContent-notice {
                    font-size: 16px;
                    line-height: 16px;
                    font-weight: 500;
                    color: #333333;
                }

                .systemContent-time {
                    font-size: 14px;
                    line-height: 14px;
                    font-weight: 400;
                    color: #666666;
                    span {
                        display: inline-block;
                        white-space: nowrap;
                        width: 100%;
                        height: 60px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                    }
                }

                .systemContent-time1 {
                    font-size: 14px;
                    font-weight: 400;
                    color: #666666;
                    line-height: 60px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    cursor: pointer;

                    &:hover {
                        color: #dd2619;
                    }
                }
            }
            .systemContent-top:last-child {
                height: 60px;
                .systemContent-time {
                    /deep/ span {
                        display: flex;
                        align-items: center;
                        white-space: nowrap;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        /deep/ p {
                            display: inline-block;
                            white-space: nowrap;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }
                        /deep/ h1 {
                            line-height: 60px;
                            white-space: nowrap;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }
                    }
                }
                .systemContent-time1 {
                    span {
                        display: flex;
                        align-items: center;
                    }
                }
            }
        }
    }

    .systemContent-btn {
        width: 96px;
        height: 34px;
    }

    /deep/ .ivu-modal {
        width: 650px !important;
        height: 565px;
    }

    /deep/ .ivu-modal-content {
        height: 565px;
    }

    /deep/ .ivu-modal-body {
        width: 574px;
        height: 273px;
        margin: 0 auto;
        word-wrap: break-word;
        overflow-y: auto;
        .header-top {
            text-align: center;
            p:first-child {
                font-size: 14px;
                text-align: left;
            }
        }
    }

    /deep/ .ivu-modal-header {
        border-bottom: none;
        padding: 36px 16px 19px;
    }

    /deep/ .ivu-modal-header p:first-child {
        text-align: center;
        height: 75px;
        line-height: 55px;
        font-size: 16px;
        color: #333333 !important;
        display: flex;
        flex-flow: column;
        span {
            height: 25px;
            font-size: 18px;
        }
    }

    /deep/ .ivu-modal-footer {
        border-top: none;
        text-align: center;
        margin: 62px 0 0;
    }
</style>
