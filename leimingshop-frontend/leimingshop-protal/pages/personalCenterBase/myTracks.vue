<template>
    <div class="mytracks" style>
        <!-- 有数据的情况 -->
        <div
            class="data-available"
            v-show="mytrackDataLength"
            id="data-available"
        >
            <div class="mytrack-top" style>
                <div class="mytrack-top-left">我的足迹</div>
                <div
                    class="mytrack-top-right"
                    style="cursor: pointer"
                    @click.stop="deleteBrowse(2)"
                    @mouseover="mouseover"
                    @mouseout="mouseout"
                >
                    <img :src="trash" alt />
                    全部删除
                </div>
            </div>
            <div class="line1"></div>
            <div class="mytrack-main">
                <load-more
                    :completed="iscompleted"
                    :loading="loading"
                    @changeData="changPages"
                >
                    <ul class="ul-one" name="ul-one">
                        <li
                            v-for="(item1, index1) in myTracksList"
                            :key="index1"
                            class="li-wrapper"
                            name="li-array1"
                        >
                            <div class="li1-top">
                                <div class="circle-wrapper">
                                    <img
                                        src="/img/06.personalCenter/redCircle.png"
                                        alt
                                        style="
                                            margin-right: 8px;
                                            margin-top: -3px;
                                        "
                                    />
                                    <span>{{ item1.browseTime }}</span>
                                </div>
                                <div
                                    @click.stop="deleteBrowse(1, index1)"
                                    class="word"
                                >
                                    删除记录
                                </div>
                            </div>
                            <div class="li1-main">
                                <ul class="ul-two" name="ul-two">
                                    <li
                                        class="li-two"
                                        v-for="(item2,
                                        index2) in item1.goodsBrowseDTOList"
                                        :key="index2"
                                        @mouseenter="mouseenter(index1, index2)"
                                        @mouseleave="mouseleave(index1, index2)"
                                        :id="item2.id"
                                        name="li-array2"
                                        v-lazy-container="{
                                            selector: 'img',
                                            error:
                                                '/img/common/loading/200_200.png',
                                            loading:
                                                '/img/common/loading/200_200.png',
                                        }"
                                        @click="
                                            goDetail(
                                                item2.goodsId,
                                                item2.goodsSpecId
                                            )
                                        "
                                    >
                                        <div class="content">
                                            <div
                                                @click.stop="
                                                    deleteBrowse(
                                                        0,
                                                        index1,
                                                        index2
                                                    )
                                                "
                                                :class="{
                                                    close:
                                                        index1 == indexFlag1 &&
                                                        index2 == indexFlag2,
                                                    closeAnother:
                                                        index1 !== indexFlag1 ||
                                                        index2 !== indexFlag2,
                                                }"
                                            >
                                                x
                                            </div>
                                            <div class="picture">
                                                <div
                                                    class="label"
                                                    v-show="item2.labelName"
                                                >
                                                    {{ item2.labelName }}
                                                </div>
                                                <img
                                                    :data-src="
                                                        myTracksImgUrl +
                                                        item2.goodsImage
                                                    "
                                                    alt
                                                />
                                            </div>
                                            <p class="p1">
                                                {{ item2.goodsName }}
                                            </p>
                                            <p class="p2">
                                                {{ item2.goodsSubTitle }}
                                            </p>
                                            <p class="price">
                                                <span class="symbol">￥</span>
                                                <span class="number">{{
                                                    item2.goodsPrice
                                                }}</span>
                                            </p>
                                            <p class="flag">
                                                <span
                                                    class="type"
                                                    v-show="
                                                        item2.storeType == 1
                                                    "
                                                    >自营</span
                                                >
                                                <!--<span class="store-name">{{font | wordCount}}</span>-->
                                                <span
                                                    class="store-name"
                                                    :title="item2.storeName"
                                                    >{{
                                                        item2.storeName
                                                            | wordCount
                                                    }}</span
                                                >
                                            </p>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </load-more>
            </div>
        </div>
        <!-- 无数据的情况 -->
        <div class="no-data" v-show="!mytrackDataLength">
            <div class="no-data-top">我的足迹</div>
            <div class="no-data-main">
                <div class="no-data-main-content">
                    <img src="/img/06.personalCenter/noshop.png" alt />
                    <p>当前无浏览足迹记录</p>
                </div>
            </div>
        </div>
        <!-- 默认地址设置成功弹窗 -->
        <prompt-page
            :promptShow="task.promptShow"
            :successOrFail="task.successOrFail"
            :titpMessage="task.titpMessage"
        ></prompt-page>
        <!-- 提交之前进行询问-->
        <div class="popup-wrapper" v-show="titleShow">
            <div class="title">
                <div>
                    <img
                        src="/img/06.personalCenter/cha@2x.png"
                        alt
                        @click="titleShow = false"
                        style="cursor: pointer"
                    />
                </div>
                <p class="successfail-p" v-show="okPrompt == 0">
                    确定删除这条浏览足迹吗？
                </p>
                <p class="successfail-p" v-show="okPrompt == 1">
                    确定删除今日浏览足迹吗？
                </p>
                <p class="successfail-p" v-show="okPrompt == 2">
                    确定删除全部浏览足迹吗？
                </p>
                <div class="button-group">
                    <div class="button-left" @click="titleShow = false">
                        取消
                    </div>
                    <div class="button-right" @click="determine">确定</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { getMytracksList, deleteBrowseList } from "@/api/api_06personalCenter";
    import promptPage from "@/components/06.personalCenter/common/promptPage.vue";
    export default {
        head() {
            return {
                title: "我的足迹",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        components: {
            promptPage,
        },
        props: {},
        data() {
            return {
                font: "去去去去去去去去去去去去去去去去去去去去去去去去去去",
                isShowMask: false,
                trash: "/img/06.personalCenter/trash1.png",
                mytrackDataLength: true, //判断是否有数据
                myTracksList: [],
                myTracksImgUrl: "",
                indexFlag1: "", //经过某个商品的index1
                indexFlag2: "", //经过某个商品的index2
                liArray1: [], //时间段的li
                liArray2: [], //单个时间的li
                iscompleted: false, //当为true的时候不再触发infinite-scroll事件
                loading: false, //  true 时将不会触发任何事件,
                temp: [],
                titleShow: false,
                task: {
                    promptShow: false,
                    successOrFail: 0,
                    titpMessage: "默认地址修改成功",
                },
                okPrompt: -1, //0删除某条数据 1删除某个时间段的数据 2删除全部数据
                selectIndex: {
                    index1: -1,
                    index2: -1,
                },
                currentPage: -1, //当前的page
            };
        },
        watch: {},
        computed: {},
        methods: {
            mouseover() {
                this.trash = "/img/06.personalCenter/trashRed.png";
            },
            mouseout() {
                this.trash = "/img/06.personalCenter/trash1.png";
            },
            //跳转到商品详情页
            goDetail(goodsId, specId) {
                let routeUrl = this.$router.resolve({
                    path: "/goodsDetails",
                    query: {
                        goodsId,
                        specId,
                    },
                });

                window.open(routeUrl.href, "_blank");
            },
            //下滑加载更多
            changPages(page) {
                this.loading = true;
                if (this.temp == null) {
                    this.iscompleted = true;
                    this.loading = false;
                    return;
                }
                this.getMytracksListAll(page);
                this.currentPage = page;
            },
            taskModel(promptShow, successOrFail, titpMessage, time) {
                this.task = {
                    promptShow,
                    successOrFail,
                    titpMessage,
                };
                setTimeout(() => {
                    this.task.promptShow = false;
                }, time);
            },
            mouseenter(index1, index2) {
                //鼠标经过事件
                this.liArray1 = document.getElementsByName("li-array1");
                this.liArray2 = this.liArray1[index1].getElementsByTagName("li");
                // this.liArray2[index2].style.boxShadow = "0px 0px 4px 0px rgba(0,0,0,0.1)"
                this.indexFlag1 = index1;
                this.indexFlag2 = index2;
            },
            mouseleave(index1, index2) {
                //鼠标离开事件
                this.liArray1 = document.getElementsByName("li-array1");
                this.liArray2 = this.liArray1[index1].getElementsByTagName("li");
                // this.liArray2[index2].style.border = "1px solid #ffffff";
                this.indexFlag1 = -1;
                this.indexFlag2 = -1;
            },
            //删除浏览记录数据 单个或者某个时间段或者全部数据
            determine() {
                let index1 = this.selectIndex.index1;
                let index2 = this.selectIndex.index2;
                var idArr = [];
                if (this.okPrompt == 0) {
                    idArr.push(
                        this.myTracksList[index1].goodsBrowseDTOList[index2].goodsId
                    );
                } else if (this.okPrompt == 1) {
                    for (
                        var i = 0;
                        i < this.myTracksList[index1].goodsBrowseDTOList.length;
                        i++
                    ) {
                        idArr.push(
                            this.myTracksList[index1].goodsBrowseDTOList[i].goodsId
                        );
                    }
                } else if (this.okPrompt == 2) {
                }
                this.titleShow = false;
                if (idArr.length > 0) {
                    deleteBrowseList(idArr)
                        .then((res) => {
                            this.taskModel(true, 1, "所选浏览足迹删除成功", 2000);
                            //删除成功之后刷新数据
                            (this.myTracksList = []), this.getMytracksListAll(1);
                        })
                        .catch((err) => {});
                } else {
                    deleteBrowseList([])
                        .then((res) => {
                            this.taskModel(true, 1, "全部浏览足迹删除成功", 2000);
                            this.mytrackDataLength = false;
                        })
                        .catch((err) => {});
                }
            },
            deleteBrowse(judge, indexOne, indexTwo) {
                this.titleShow = true;
                this.okPrompt = judge;
                if (indexTwo >= 0) {
                    this.selectIndex.index1 = indexOne;
                    this.selectIndex.index2 = indexTwo;
                } else {
                    this.selectIndex.index1 = indexOne;
                }
            },
            getMytracksListAll(page) {
                //请求我的足迹列表数据
                getMytracksList({ page: page, limit: 5 })
                    .then((res) => {
                        console.log(res);
                        if (res.data && res.data.code == 200) {
                            this.temp = res.data.data.list;
                            if (page != 1 && this.temp == null) {
                                this.loading = false;
                                return;
                            } else if (page == 1 && this.temp == null) {
                                this.mytrackDataLength = false;
                                return;
                            }
                            this.myTracksList = this.myTracksList.concat(this.temp);
                            this.loading = false;
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
        },
        created() {},
        filters: {
            //中英文混着限制长度，一个中文是两个字符
            wordCount: function (word) {
                var zfc = {};
                zfc.mixTextOverflow = function (text, length) {
                    if (text.replace(/[\u4e00-\u9fa5]/g, "aa").length <= length) {
                        return text;
                    } else {
                        var _length = 0;
                        var outputText = "";
                        for (var i = 0; i < text.length; i++) {
                            if (/[\u4e00-\u9fa5]/.test(text[i])) {
                                _length += 2;
                            } else {
                                _length += 1;
                            }
                            if (_length > length) {
                                break;
                            } else {
                                outputText += text[i];
                            }
                        }
                        return outputText + "...";
                    }
                };
                return zfc.mixTextOverflow(word, 34);
            },
        },
        mounted() {
            this.myTracksImgUrl = this.$imgURL;
            //this.getMytracksListAll(1);
            // this.liArray2=(document.getElementsByName("li-array1"))[0].getElementsByTagName("li")
        },
    };
</script>
<style lang="scss" scoped>
    .mytracks {
        width: 100%;
        font-size: 13px;
        float: right;
        height: 100%;
        background: #ffffff;
    }

    .data-available {
        .mytrack-top {
            display: flex;
            justify-content: space-between;
            height: 44px;
            background: #ffffff;
            padding: 0 40px;
            align-items: center;
            .mytrack-top-left {
                font-size: 14px;
                color: #333333;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
            }
            .mytrack-top-right {
                font-size: 14px;
                color: #999999;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                img {
                    width: 15px;
                    vertical-align: middle;
                }
                &:hover {
                    color: #dd2619;
                }
            }
        }
    }
    .line1 {
        width: 100%;
        height: 1px;
        background: #ebebeb;
    }
    .mytrack-main {
        width: 100%;
        background: #ffffff;
        padding: 0px 40px;
        .ul-one {
            .li1-top {
                height: 52px;
                line-height: 52px;
                display: flex;
                justify-content: space-between;
                .circle-wrapper {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                span {
                    font-size: 14px;
                    color: #333333;
                    font-family: PingFangSC-Medium, PingFang SC;
                    font-weight: 500;
                }
                div {
                    font-size: 14px;
                    color: #a3a3a3;
                }
                .circle {
                    width: 4px;
                    height: 4px;
                    background: #dd2619;
                    margin-right: 6px;
                    border-radius: 50%;
                }
            }
        }
    }
    .li1-main {
        // margin-top: 20px;
    }
    .li-wrapper {
        // margin-top: 30px;
    }
    .content {
        width: 177px;
        // margin: 0 auto;
    }
    .close {
        width: 20px;
        height: 20px;
        background: #dd2619;
        color: #ffffff;
        text-align: center;
        line-height: 20px;
        position: absolute;
        right: 0;
        top: 0;
        display: block;
        cursor: pointer;
    }
    .closeAnother {
        display: none;
        cursor: pointer;
    }
    .ul-two {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        .li-two {
            width: 25%;
            // margin-top: 30px;
            padding: 20px;
            position: relative;
            &:hover {
                box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
            }
            cursor: pointer;
            .picture {
                width: 177px;
                height: 177px;
                position: relative;
                display: flex;
                .label {
                    position: absolute;
                    width: 40px;
                    height: 20px;
                    background: #dd2619;
                    color: #ffffff;
                    text-align: center;
                    line-height: 20px;
                    border-radius: 0 3px 3px 0;
                    left: 0;
                    top: 0;
                }
                img {
                    width: 177px;
                }
            }
            .p1 {
                height: 14px;
                line-height: 14px;
                font-size: 14px;
                width: 161px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                color: #333333;
                margin-top: 20px;
                text-align: left;
                font-weight: 500;
                font-family: PingFangSC-Medium, PingFang SC;
            }
            .p2 {
                height: 12px;
                line-height: 12px;
                font-size: 12px;
                color: #999999;
                width: 161px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                text-align: left;
                font-weight: 400;
                font-family: PingFangSC-Regular, PingFang SC;
                margin-top: 8px;
            }
            .price {
                text-align: left;
                font-size: 18px;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-weight: 600;
                color: #dd2619;
                height: 18px;
                line-height: 18px;
                margin-top: 10px;
                .symbol {
                    color: #dd2619;
                    font-size: 14px;
                }
                .number {
                    font-size: 20px;
                    color: #dd2619;
                    margin-left: -6px;
                }
            }
            .flag {
                margin-top: 11px;
                text-align: left;
                // height: 12px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 16px;
                .type {
                    font-size: 12px;
                    color: #ffffff;
                    width: 26px;
                    height: 12px;
                    background: linear-gradient(
                        90deg,
                        rgba(221, 41, 28, 1) 0%,
                        rgba(255, 78, 2, 1) 100%
                    );
                    border-radius: 4px;
                    padding: 0 2px;
                }
                .store-name {
                    font-size: 14px;
                    color: #666666;
                    margin-left: 4px;
                }
            }
        }
    }
    .no-data-top {
        border-bottom: 1px solid #dddddd;
        padding: 20px 0 20px 40px;
        font-size: 15px;
        color: #3a3a3a;
        font-weight: 500;
    }

    .no-data-main {
        position: relative;
        height: 600px;
    }

    .no-data-main-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);

        p {
            text-align: center;
            margin-top: 10px;
            font-size: 16px;
            color: #3a3a3a;
        }
    }

    .taskAll {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.4);
    }

    .taskAllContainer {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: #fff;
        width: 340px;
        padding: 20px;

        i {
            float: right;
            cursor: pointer;
        }

        p {
            margin-top: 30px;
            text-align: center;
        }
    }

    .word {
        cursor: pointer;
        &:hover {
            color: #dd2619 !important;
        }
    }
    .button-group {
        margin-top: 40px;
        display: flex;
        justify-content: center;
    }

    // .button-left {
    //   width: 120px;
    //   background: #f5f5f5;
    //   text-align: center;
    //   padding: 10px 0;
    // }

    // .button-right {
    //   width: 120px;
    //   background: #f36d69;
    //   text-align: center;
    //   padding: 10px 0;
    //   margin-left: 20px;
    //   color: #fff;
    //   cursor: pointer;
    // }

    .mytrack-bottom {
        text-align: center;
    }
    // 弹出确定的提示框
    .popup-wrapper {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(1, 1, 1, 0.13);
    }
    .title {
        padding: 0 30px;
        background: #ffffff;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        border: 4px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
        padding: 0 80px 36px 80px;
        img {
            width: 14px;
            position: absolute;
            right: 13px;
            top: 13px;
        }
        .successfail-p {
            font-size: 24px;
            color: #333333;
            text-align: center;
            margin-top: 46px;
        }
        .button-group {
            display: flex;
            justify-content: center;
            margin-top: 53px;
            .button-left {
                width: 68px;
                height: 32px;
                line-height: 32px;
                font-size: 15px;
                color: #333333;
                background: #f5f5f5;
                text-align: center;
                cursor: pointer;
                border-radius: 1px;
            }
            .button-right {
                width: 68px;
                height: 32px;
                line-height: 32px;
                font-size: 15px;
                color: #ffffff;
                background: #dd2619;
                text-align: center;
                margin-left: 46px;
                cursor: pointer;
                border-radius: 1px;
            }
        }
    }
    .no-data {
        background: #ffffff;
        height: 100%;
    }
</style>
