<template>
    <div class="favorite-store-list">
        <div class="haveshop" v-show="!emptyOrNot">
            <div class="store-top">
                <div class="store-top-left">收藏的店铺</div>
                <div class="store-top-right">
                    <div
                        class="status1"
                        v-show="bulkDelete"
                        @click="bulkDelete = false"
                        @mouseover="mouseover"
                        @mouseout="mouseout"
                    >
                        <img :src="trash" alt />
                        批量删除
                    </div>
                    <div class="status2" v-show="!bulkDelete">
                        <input
                            type="checkbox"
                            name="controlAll"
                            id="controlAll"
                            @click="selectAll"
                            style="vertical-align: middle"
                        />
                        <label for="controlAll" class="label1">全选</label>
                        <label for="controlAll" class="label2">全选</label>
                        <Button @click="cancelCollection(1)" class="cancel"
                            >取消收藏</Button
                        >
                        <Button
                            @click="
                                bulkDelete = true;
                                single = false;
                            "
                            class="finish"
                            >完成</Button
                        >
                    </div>
                </div>
            </div>
            <div class="line1"></div>
            <div class="store-main">
                <ul>
                    <li
                        v-for="(item, index) in storeTotalList"
                        :key="index"
                        @click="deleteButton(index)"
                        name="li-array"
                        v-lazy-container="{
                            selector: 'img',
                            error: '/img/common/loading/200_200.png',
                            loading: '/img/common/loading/200_200.png',
                        }"
                    >
                        <input
                            type="checkbox"
                            class="change"
                            @change="shuchu()"
                            name="selected"
                            :id="item.storeId"
                        />
                        <div class="img-wrapper">
                            <img
                                :data-src="picturePrefix + item.storeLogo"
                                alt
                            />
                        </div>
                        <div class="p-wrapper">
                            <p
                                class="store-name"
                                @click.stop="goDetail(item.storeId)"
                                :title="item.storeName"
                            >
                                {{ item.storeName }}
                            </p>
                            <p class="concerned-people">
                                {{ item.storeCollection }}人关注
                            </p>
                        </div>
                        <Icon
                            type="ios-close"
                            size="24"
                            name="icon"
                            class="on"
                            @click.stop="cancelCollection(0, index)"
                            style="cursor: pointer"
                        />
                    </li>
                    <div class="empty" v-for="(item, index) in 4"></div>
                </ul>

                <paging
                    class="paging"
                    :totalCount="pageTotal"
                    :pageSize="page.limit"
                    @handlePageChange="handlePageChange"
                    v-if="storeTotalList.length !== 0 && pageTotal > page.limit"
                />
                <div v-else style="height: 60px"></div>
            </div>

            <div class="store-bottom"></div>

            <div class="store-bottom"></div>
        </div>
        <div class="noshop" v-show="emptyOrNot">
            <div class="store-top">
                <div class="store-top-left">收藏的店铺</div>
            </div>
            <div class="line1"></div>
            <div class="noshop-bottom">
                <!-- <img src="" alt=""> -->
                <img src="/img/06.personalCenter/noshop.png" alt />
                <p>对不起，对应店铺分类或筛选组合下没有店铺</p>
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
                    />
                </div>
                <p class="successfail-p" v-show="okPrompt == 0">
                    是否删除收藏的店铺?
                </p>
                <p class="successfail-p" v-show="okPrompt == 1">
                    是否删除收藏的店铺?
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
    import {
        getCollectionShopList,
        deleteCollecStoreList,
    } from "@/api/api_06personalCenter";
    import paging from "@/components/common/paging";
    import promptPage from "@/components/06.personalCenter/common/promptPage.vue";
    export default {
        name: "favoriteStoreList",
        head() {
            return {
                title: "收藏的店铺",
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
            paging,
            promptPage,
        },
        props: {},
        data() {
            return {
                trash: "/img/06.personalCenter/trash1.png",
                liArray: [],
                emptyOrNot: false, //收藏列表是否为空
                bulkDelete: true,
                checkIndex: -1, //选中某个商品的index
                checklist: [],
                iconList: [],
                page: {
                    currentPage: 1,
                    limit: 12,
                },
                pageTotal: 20,
                pageFlag: false, //总页数请求成功，翻页再出现
                storeTotalList: [], //收藏的店铺列表
                picturePrefix: "",
                titleShow: false, //提示弹层
                task: {
                    promptShow: false,
                    successOrFail: 0,
                    titpMessage: "默认地址修改成功",
                },
                okPrompt: -1, //0删除某条数据 1删除某个时间段的数据 2删除全部数据
                selectIndex: -1,
            };
        },
        computed: {},
        methods: {
            mouseover() {
                this.trash = "/img/06.personalCenter/trashRed.png";
            },
            mouseout() {
                this.trash = "/img/06.personalCenter/trash1.png";
            },
            changeAll() {},
            uncheckStatus() {
                //取消复选框和商品选中的状态
                for (var j = 0; j < this.checklist.length; j++) {
                    this.checklist[j].checked = 0;
                    this.iconList[j].style.visibility = "hidden";
                    // this.liArray[j].style.border = "1px solid #ebebeb";
                }
                document.getElementById("controlAll").checked = 0; //让总的复选框处于未选中状态
            },
            handlePageChange(val) {
                this.page.currentPage = val;
                this.uncheckStatus();
                getCollectionShopList({
                    page: val,
                    limit: this.page.limit,
                })
                    .then((res) => {
                        if (res.data && res.data.code == 200) {
                            this.storeTotalList = res.data.data.list;
                            this.pageTotal = res.data.data.total;
                            if (this.pageTotal == 0) {
                                this.emptyOrNot = true;
                            }
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            deleteButton(index) {
                if (this.checklist[index].checked == false) {
                    this.checklist[index].checked = true;
                    this.iconList[index].style.visibility = "visible";
                    this.liArray[index].style.boxShadow =
                        "0px 0px 4px 0px rgba(0,0,0,0.1)";
                } else {
                    this.checklist[index].checked = false;
                    this.iconList[index].style.visibility = "hidden";
                    this.liArray[index].style.border = "1px solid #ebebeb";
                    this.liArray[index].style.boxShadow = "none";
                }
                this.shuchu();
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
            determine() {
                //取消收藏
                this.titleShow = false;
                var idArr = [];
                if (this.okPrompt == 0) {
                    idArr.push(this.storeTotalList[this.selectIndex].storeId);
                } else {
                    for (var i = 0; i < this.checklist.length; i++) {
                        if (this.checklist[i].checked == true) {
                            idArr.push(this.checklist[i].id);
                        }
                    }
                }
                console.log(idArr);
                if (idArr.length > 0) {
                    deleteCollecStoreList(idArr)
                        .then((res) => {
                            this.taskModel(true, 1, "所选收藏的店铺删除成功", 2000);
                            //判断当前页后面是否还有页数
                            let pageCount = Math.ceil(
                                this.pageTotal / this.page.limit
                            );
                            let subtract = true;
                            if (pageCount > this.page.currentPage) {
                                subtract = false;
                            }
                            if (
                                (document.getElementById("controlAll").checked ==
                                    1 &&
                                    this.bulkDelete == false &&
                                    subtract == true) ||
                                this.storeTotalList.length == 1
                            ) {
                                this.page.currentPage = this.page.currentPage - 1;
                            }
                            this.uncheckStatus();
                            this.getCollectionShopData(); //删除之后重新刷新一下数据
                        })
                        .catch((err) => {
                            console.log(err);
                        });
                } else {
                    document.getElementById("controlAll").checked = 0; //让总的复选框处于未选中状态
                }
            },
            cancelCollection(judge, indexOne) {
                let showFlag = false;
                this.checklist.forEach((item, index) => {
                    if (item.checked == 1) showFlag = true;
                });
                if (showFlag == true) {
                    this.titleShow = true;
                    this.okPrompt = judge;
                    if (indexOne >= 0) {
                        this.selectIndex = indexOne;
                    }
                } else {
                    this.$Message.warning("请选择要删除的收藏店铺");
                }
            },
            //定义一个全选的方法
            selectAll() {
                if (document.getElementById("controlAll").checked) {
                    for (var i = 0; i < this.checklist.length; i++) {
                        this.checklist[i].checked = 1;
                        this.iconList[i].style.visibility = "visible";
                        this.liArray[i].style.boxShadow =
                            "0px 0px 4px 0px rgba(0,0,0,0.1)";
                    }
                } else {
                    for (var j = 0; j < this.checklist.length; j++) {
                        this.checklist[j].checked = 0;
                        this.iconList[j].style.visibility = "hidden";
                        this.liArray[j].style.border = "1px solid #ebebeb";
                        this.liArray[j].style.boxShadow = "none";
                    }
                }
            },
            shuchu() {
                var checked = [];
                for (var i = 0; i < this.checklist.length; i++) {
                    if (this.checklist[i].checked == 1) {
                        checked.push(1);
                    }
                }
                if (checked.length == this.checklist.length) {
                    document.getElementById("controlAll").checked = 1;
                } else {
                    document.getElementById("controlAll").checked = 0;
                }
            },
            getCollectionShopData() {
                getCollectionShopList({
                    page: this.page.currentPage,
                    limit: this.page.limit,
                })
                    .then((res) => {
                        if (res.data && res.data.code == 200) {
                            this.storeTotalList = res.data.data.list;
                            this.pageTotal = res.data.data.total;
                            this.pageFlag = true;
                            if (this.pageTotal == 0) {
                                this.emptyOrNot = true;
                            }
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },
            //跳转到店铺详情页
            goDetail(storeId) {
                // this.$router.push({
                // name:"shopIndex",
                //   //name: "shopProClassification",
                //   query: {
                //     storeId
                //   }
                // });
                let routeUrl = this.$router.resolve({
                    path: "/shopIndex",
                    query: {
                        storeId,
                    },
                });
                window.open(routeUrl.href, "_blank");
            },
        },
        created() {},
        mounted() {
            this.picturePrefix = this.$imgURL;
            this.getCollectionShopData(); //请求收藏店铺列表数据
            this.checklist = document.getElementsByName("selected");
            this.iconList = document.getElementsByName("icon");
            this.liArray = document.getElementsByName("li-array");
        },
    };
</script>
<style lang="scss" scoped>
    .favorite-store-list {
        height: 100%;
        background: #ffffff;
    }
    .store-top {
        display: flex;
        justify-content: space-between;
        padding: 0 40px;
        background: #fff;
        height: 44px;
        line-height: 44px;
        .store-top-left {
            font-size: 14px;
            color: #333333;
            font-weight: 600;
        }
        .store-top-right {
            font-size: 14px;
            color: #a3a3a3;
            font-weight: 400;
        }
    }

    .status1 {
        cursor: pointer;
        img {
            width: 15px;
            vertical-align: middle;
            margin-bottom: 3px;
        }
        &:hover {
            color: #dd2618;
        }
    }

    .status2 {
        label {
            vertical-align: middle;
        }
    }

    .change {
        visibility: hidden;
        position: absolute;
        left: 0;
        top: 0;
    }

    .store-name {
        // padding: 20px 15px 0 20px;
        height: 14px;
        line-height: 14px;
        margin-top: 20px;
        font-size: 14px;
        color: #333333;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500px;
        cursor: pointer;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .store-name:hover {
        color: #dd2618;
    }
    .concerned-people {
        // padding: 10px 0 15px 20px;
        font-size: 12px;
        color: #999999;
        font-family: PingFangSC-Regular, PingFang SC;
        height: 12px;
        line-height: 12px;
        margin-top: 10px;
    }

    .store-main {
        background: #ffffff;
        padding: 20px 40px;
        ul {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            li {
                width: 206px;
                border: 1px solid #ebebeb;
                margin-top: 20px;
                position: relative;
                img {
                    width: 100%;
                    height: 120px;
                }
                i {
                    display: none;
                }

                i.on {
                    display: block;
                    visibility: hidden;
                    position: absolute;
                    right: 0;
                    top: 0;
                    background: #dd2619;
                    color: #ffffff;
                }
            }
        }
    }

    .store-bottom {
        text-align: center;
        margin-top: 30px;
    }
    .noshop {
        width: 100%;
    }
    .noshop-bottom {
        margin-top: 10px;
        background: #fff;
        text-align: center;
        height: 400px;
        padding-top: 100px;
        p {
            margin-top: 30px;
            font-size: 16px;
            color: #3a3a3a;
            font-weight: 400;
        }
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
            justify-content: space-between;
            margin-top: 53px;
            .button-left {
                width: 68px;
                height: 32px;
                line-height: 32px;
                font-size: 15px;
                color: #333333;
                background: #f5f5f5;
                text-align: center;
                border-radius: 1px;
                cursor: pointer;
            }
            .button-right {
                width: 68px;
                height: 32px;
                line-height: 32px;
                font-size: 15px;
                color: #ffffff;
                background: #dd2619;
                text-align: center;
                cursor: pointer;
                border-radius: 1px;
            }
        }
    }
    .empty {
        width: 206px;
        height: 0;
    }
    .img-wrapper {
        font-size: 0;
    }
    .line1 {
        width: 100%;
        height: 1px;
        background: #ebebeb;
    }
    //自定义复选框的样式
    #controlAll {
        visibility: hidden;
    }

    #controlAll + label.label1 {
        width: 18px;
        height: 18px;
        border: 1px solid #dcdee2;
        border-radius: 2px;
        background-color: #ffffff;
        display: inline-block;
        position: relative;
        border-radius: 4px;
        cursor: pointer;
        overflow: hidden;
        &:after {
            content: "";
            display: table;
            width: 4px;
            height: 8px;
            position: absolute;
            top: 3px;
            left: 6px;
            border: 1px solid #ffffff;
            border-top: 0;
            border-left: 0;
            -webkit-transform: rotate(45deg) scale(1);
            transform: rotate(45deg) scale(1);
            -webkit-transition: all 0.2s ease-in-out;
            transition: all 0.2s ease-in-out;
        }
    }
    #controlAll:checked + label.label1:before {
        content: ""; /*这里设置选中之后的显示内容*/
        width: 18px;
        height: 18px;
        display: block;
        color: #ffffff;
        text-align: center;
        // font-weight: bolder;
        line-height: 17px;
        background-color: #dd2619;
    }
    .label2 {
        margin-right: 17px;
        font-size: 10px;
        color: #333333;
    }
    .cancel {
        margin-right: 10px;
        font-size: 12px;
        color: #999999;
        font-weight: 400;
        &:hover {
            color: #ffffff;
            background: #dd2619;
        }
    }
    .finish {
        font-size: 10px;
        color: #999999;
        font-weight: 400;
        &:hover {
            color: #ffffff;
            background: #dd2619;
        }
    }
    /deep/ .ivu-btn {
        padding: 0 7px !important;
        height: 20px !important;
    }
    /deep/ .p-wrapper {
        padding: 0px 20px 20px;
    }
</style>
