<template>
    <div class="comment-comp">
        <!-- 用户信息 -->
        <div class="user-info">
            <img
                class="head-portrait"
                :src="comment.memberAvatar | filterImgUrl"
                alt=""
            />

            <div class="u-content">
                <p class="nick-name" v-text="comment.memberName"></p>
                <p class="member-grade"><img :src="memberGrade" alt="" /></p>
            </div>
        </div>

        <!-- 评论相关 -->
        <div class="comment-about">
            <!-- 订单信息 -->
            <div class="c-other-info">
                <Rate
                    v-model="comment.evaluateScores"
                    disabled
                    custom-icon="rate-icon"
                />
                <span
                    class="p-info"
                    v-text="comment.specInfo"
                    :title="comment.specInfo"
                ></span>
                <span class="c-date" v-text="comment.createDate"></span>
            </div>

            <!-- 评论内容 -->
            <div class="c-content">
                <p class="c-text" v-text="comment.evaluateContent"></p>
                <div
                    class="c-img-wrap"
                    v-viewer
                    v-lazy-container="{
                        selector: 'img',
                        error: '/img/common/loading/200_200.png',
                        loading: '/img/common/loading/200_200.png',
                    }"
                >
                    <img
                        v-for="(item, index) in evaluateImageList"
                        :key="item"
                        :data-src="item"
                        alt=""
                    />
                </div>
            </div>

            <!-- 商家回复 -->
            <div class="business-reply" v-if="comment.replyContent">
                <img
                    class="b-head"
                    v-lazy="$options.filters.filterImgUrl(comment.storeLogo)"
                    alt=""
                />
                <div class="r-about">
                    <div class="p-other-info">
                        <span class="b-name" v-text="businessName"></span>
                        <span class="r-date" v-text="comment.replyDate"></span>
                    </div>
                    <div class="r-content">
                        <p class="r-text" v-html="comment.replyContent"></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "commentComp",

        data() {
            return {
                businessName: "商家回复：",
            };
        },

        props: {
            comment: {
                type: Object,
                default: () => {},
            },
        },

        computed: {
            evaluateImageList() {
                if (this.comment.evaluateImage) {
                    let tempImgList = this.comment.evaluateImage.split(",");
                    return tempImgList.map((item, index) => {
                        return `${this.$imgURL}${item}`;
                    });
                }
            },

            memberGrade() {
                let icon;

                switch (this.comment.memberGrade) {
                    case "普通会员":
                        icon = require("~/static/img/03.goodsClass/03-03.goodsDetail/normal.png");
                        break;
                    case "黄金会员":
                        icon = require("~/static/img/03.goodsClass/03-03.goodsDetail/huangjin.png");
                        break;
                    case "铂金会员":
                        icon = require("~/static/img/03.goodsClass/03-03.goodsDetail/bojin.png");
                        break;
                    case "钻石会员":
                        icon = require("~/static/img/03.goodsClass/03-03.goodsDetail/zuanshi.png");
                        break;
                }
                return icon;
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .comment-comp {
        padding: 30px 0;
        border-bottom: 2px solid #ebebeb;
        overflow: hidden;
        display: flex;
        justify-content: space-between;
        align-items: top;
    }

    .user-info {
        .head-portrait {
            width: 60px;
            height: 60px;
            border-radius: 60px;
            border: 1px solid #f5f5f5;
            display: inline-block;
        }

        .u-content {
            display: inline-block;
            vertical-align: top;
            margin-left: 10px;
        }

        .nick-name {
            font-size: 15px;
            color: #333333;
            margin-top: 5px;
        }

        .member-grade {
            margin-top: 5px;
            padding-left: 3px;
        }
    }

    .comment-about {
        width: 600px;
        padding-top: 8px;
    }

    .c-other-info {
        width: 100%;
        height: 16px;
        line-height: 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .ivu-rate {
            margin-top: -8px;
        }
        .p-info {
            width: 330px;
            height: 16px;
            line-height: 16px;
            color: #3a3a3a;
            white-space: nowrap;
            word-break: break-all;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .c-date {
            color: #999999;
        }
    }

    .c-content {
        margin-top: 10px;
        .c-text {
            color: #6b6b6b;
            font-size: 14px;
            word-break: break-all;
        }

        .c-img-wrap {
            margin-top: 10px;
            font-size: 0;
            img {
                width: 90px;
                height: 90px;
                margin: 0 10px 10px 0;
                border: 1px solid #f5f5f5; /* 待删 */
                object-fit: scale-down;
                cursor: pointer;
                transition: all 0.3s;
                &:hover {
                    border: 1px solid $primary-color;
                }
            }
        }
    }

    .business-reply {
        width: 100%;
        border-top: 1px solid #ebebeb;
        padding-top: 20px;
        margin-top: 10px;
        .b-head {
            width: 40px;
            height: 40px;
            display: inline-block;
            vertical-align: top;
        }
        .r-about {
            padding-left: 8px;
            width: 556px;
            display: inline-block;
        }
        .p-other-info {
            .b-name {
                font-size: 15px;
                color: #424242;
            }
            .r-date {
                float: right;
                font-size: 14px;
                color: #999999;
            }
        }
        .r-content {
            .r-text {
                font-size: 14px;
                color: #717171;
                word-break: break-all;
            }
        }
    }
</style>
