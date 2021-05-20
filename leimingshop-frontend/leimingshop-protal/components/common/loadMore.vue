<template>
    <div class="loadMore">
        <client-only>
            <vue-data-loading
                :loading="loading"
                :completed="completed"
                :listens="['infinite-scroll', 'pull-down']"
                :init-scroll="true"
                @infinite-scroll="infiniteScroll"
            >
                <div slot="pull-down-ready">ready to refresh</div>
                <div>
                    <slot></slot>
                </div>
                <div slot="infinite-scroll-loading">
                    <div class="loading">
                        <Spin class="img">
                            <Icon
                                type="ios-time-outline"
                                class="demo-spin-icon-load"
                                size="16"
                            />
                            <div style="font-size: 16px; padding-bottom: 20px">
                                正在拼命加载中...
                            </div>
                        </Spin>
                    </div>
                </div>
                <div
                    slot="completed"
                    style="font-size: 16px; color: #333333; font-weight: bold"
                >
                    客官，数据已经全部加载出来了。
                </div>
            </vue-data-loading>
        </client-only>
    </div>
</template>

<script>
    import VueDataLoading from "vue-data-loading";
    export default {
        name: "loadMore",
        props: {
            completed: {
                type: Boolean,
                default: false,
            },
            loading: {
                type: Boolean,
                default: false,
            },
        },
        components: {
            VueDataLoading,
        },
        data() {
            return {
                page: 1,
            };
        },
        methods: {
            infiniteScroll() {
                this.$emit("changeData", this.page);
                this.page++;
            },
        },
    };
</script>

<style lang="scss" scoped>
    .loadMore {
        margin: 0 auto;
        text-align: center;
        ul {
            margin: 0;
            padding: 0;
            list-style: none;
        }
    }
    .loadMore /deep/ .loading-footer {
        background: #ffffff;
    }
    /*加载中icon样式*/
    .loadMore /deep/ .demo-spin-icon-load {
        animation: ani-demo-spin 1s linear infinite;
    }
    .loadMore /deep/ .loading-footer {
        height: 80px;
    }
    @keyframes ani-demo-spin {
        from {
            transform: rotate(0deg);
        }
        50% {
            transform: rotate(180deg);
        }
        to {
            transform: rotate(360deg);
        }
    }
</style>
