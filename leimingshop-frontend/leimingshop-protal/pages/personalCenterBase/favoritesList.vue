<template>
    <div class="favorite-store-list">
        <div class="haveshop" v-show="!emptyOrNot">
            <div class="store-top">
                <div class="store-top-left">收藏的商品</div>
                <div class="store-top-right">
                    <div
                        class="status1"
                        v-show="bulkDelete"
                        @click="bulkDelete = false"
                        @mouseout="mouseout"
                        @mouseover="mouseover"
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
                        v-for="(item, index) in collectionList"
                        :key="index"
                        @click="deleteButton(index)"
                        name="li-array"
                        v-lazy-container="{
                            selector: 'img',
                            error: '/img/common/loading/200_200.png',
                            loading: '/img/common/loading/200_200.png',
                        }"
                    >
                        <div class="content">
                            <input
                                type="checkbox"
                                class="change"
                                @change="shuchu()"
                                name="selected"
                                :id="item.specId"
                            />
                            <div class="img-wrapper">
                                <div class="label" v-show="item.labelName">
                                    {{ item.labelName }}
                                </div>
                                <img
                                    :data-src="picturePrefix + item.pictureUrl"
                                    alt
                                />
                            </div>
                            <p
                                class="store-name"
                                @click.stop="
                                    goDetail(item.goodsId, item.specId)
                                "
                                :title="item.goodsName"
                            >
                                {{ item.goodsName }}
                            </p>
                            <p
                                class="concerned-people"
                                :title="item.goodsSubTitle"
                                @click.stop="
                                    goDetail(item.goodsId, item.specId)
                                "
                            >
                                {{ item.goodsSubTitle }}
                            </p>
                            <div class="store-price">
                                <p class="concerned-price">
                                    <span class="symbol">￥</span>
                                    <span class="number">{{
                                        item.favPrice
                                    }}</span>
                                </p>
                                <Button
                                    type="primary"
                                    @click.stop="addCat(item.specId)"
                                    style="
                                        height: 24px !iimportant;
                                        font-size: 12px;
                                        border: none !important;
                                    "
                                    >加购物车</Button
                                >
                            </div>
                            <Icon
                                type="ios-close"
                                size="19"
                                name="icon"
                                class="on"
                                @click.stop="cancelCollection(0, index)"
                                style="cursor: pointer"
                            />
                        </div>
                    </li>
                </ul>
                <div class="store-bottom">
                    <paging
                        class="paging"
                        :totalCount="pageTotal"
                        :pageSize="page.limit"
                        @handlePageChange="handlePageChange"
                        v-if="
                            collectionList.length > 0 && pageTotal > page.limit
                        "
                    />
                    <div v-else style="height: 60px"></div>
                </div>
            </div>
        </div>
        <div class="noshop" v-show="emptyOrNot">
            <div class="store-top">
                <div class="store-top-left">收藏的商品</div>
            </div>
            <div class="line1"></div>
            <div class="noshop-bottom">
                <!-- <img src="" alt=""> -->
                <img src="/img/06.personalCenter/noshop.png" alt />
                <p>对不起，对应商品分类或筛选组合下没有商品</p>
            </div>
        </div>
        <!-- 默认地址设置成功弹窗 -->
        <prompt-page
            :promptShow="task.promptShow"
            :successOrFail="task.successOrFail"
            :titpMessage="task.titpMessage"
        ></prompt-page>
        <!-- 提交之前进行询问-->
        <!-- <div class="popup-wrapper" v-show="titleShow">
      <div class="title">
        <div>
          <img src="@static/img/06.personalCenter/cha@2x.png" alt @click="titleShow=false" />
        </div>
        <p class="successfail-p" v-show="okPrompt==0">是否删除收藏的商品?</p>
        <p class="successfail-p" v-show="okPrompt==1">是否删除收藏的商品?</p>
        <div class="button-group">
          <div class="button-left" @click="titleShow=false">取消</div>
          <div class="button-right" @click="determine">确定</div>
        </div>
      </div>
    </div>-->

        <!-- 删除弹框 -->
        <Modal v-model="titleShow" width="400" class="delete">
            <div style="text-align: center">
                <p style="font-size: 24px; color: #333">
                    {{
                        okPrompt == 0
                            ? "确定删除商品吗？"
                            : "是否删除收藏的商品？"
                    }}
                </p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    class="newBtn newBtn1"
                    style="width: 68px; background: #f5f5f5; margin-right: 48px"
                    size="default"
                    @click.stop="titleShow = false"
                    >取消</Button
                >
                <Button
                    class="newBtn"
                    style="
                        width: 68px;
                        margin: 0 0 0 10px;
                        background: rgba(221, 38, 25, 1);
                        color: rgba(255, 255, 255, 1);
                    "
                    size="default"
                    @click="determine"
                    >确定</Button
                >
            </div>
        </Modal>
    </div>
</template>

<script>
    import {
        getCollectionsList,
        deleteCollectionsList,
        postCart,
    } from "@/api/api_06personalCenter.js";
    import paging from "@/components/common/paging";
    import promptPage from "@/components/06.personalCenter/common/promptPage.vue";
    export default {
        name: "favoritesList",
        head() {
            return {
                title: "收藏的商品",
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
                emptyOrNot: false, //收藏列表是否为空
                bulkDelete: true,
                checkIndex: -1, //选中某个商品的index
                checklist: [],
                iconList: [],
                collectionList: [], //收藏商品列表
                picturePrefix: "", //图片前缀
                indexFlag: "",
                page: {
                    currentPage: 1, //当前所在的页数，默认是第一页
                    limit: 12, //每页显示多少条数据
                },
                pageTotal: 0,
                pageFlag: false, //总页数请求成功，翻页再出现
                // pageSize: 10, //每页多少条数据
                liArray: [],
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
            changeAll() {},
            mouseout() {
                this.trash = "/img/06.personalCenter/trash1.png";
            },
            mouseover() {
                this.trash = "/img/06.personalCenter/trashRed.png";
            },
            uncheckStatus() {
                //取消复选框和商品选中的状态
                for (var j = 0; j < this.checklist.length; j++) {
                    this.checklist[j].checked = 0;
                    this.iconList[j].style.visibility = "hidden";
                    this.liArray[j].style.border = "1px solid #ffffff";
                }
                document.getElementById("controlAll").checked = 0;
            },
            handlePageChange(val) {
                this.page.currentPage = val;
                this.uncheckStatus();
                getCollectionsList({
                    page: val,
                    limit: this.page.limit,
                })
                    .then((res) => {
                        if (res.data && res.data.code == 200) {
                            this.collectionList = res.data.data.list;
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
                    // this.liArray[index].style.border = "1px solid red";
                    this.iconList[index].style.visibility = "visible";
                    this.liArray[index].style.boxShadow =
                        "0px 0px 4px 0px rgba(0,0,0,0.1)";
                } else {
                    this.checklist[index].checked = false;
                    // this.liArray[index].style.border = "1px solid #ffffff";
                    this.iconList[index].style.visibility = "hidden";
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
                    idArr.push(this.collectionList[this.selectIndex].specId);
                } else {
                    for (var i = 0; i < this.checklist.length; i++) {
                        if (this.checklist[i].checked == true) {
                            idArr.push(this.checklist[i].id);
                        }
                    }
                }
                if (idArr.length > 0) {
                    deleteCollectionsList(idArr)
                        .then((res) => {
                            this.taskModel(true, 1, "所选收藏的商品删除成功", 2000);
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
                                this.collectionList.length == 1
                            ) {
                                this.page.currentPage = this.page.currentPage - 1;
                            }
                            this.uncheckStatus();
                            this.getCollectionsListAll(); //删除之后重新刷新一下数据
                        })
                        .catch((err) => {});
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
                    this.$Message.warning("请选择要删除的收藏商品");
                }
            },
            // 有关复选框的相关知识
            //定义一个全选的方法
            selectAll() {
                if (document.getElementById("controlAll").checked) {
                    for (var i = 0; i < this.checklist.length; i++) {
                        this.checklist[i].checked = 1;
                        this.iconList[i].style.visibility = "visible";
                        // this.liArray[i].style.border = "1px solid #ffffff";
                        this.liArray[i].style.boxShadow =
                            "0px 0px 4px 0px rgba(0,0,0,0.1)";
                    }
                } else {
                    for (var j = 0; j < this.checklist.length; j++) {
                        this.checklist[j].checked = 0;
                        this.iconList[j].style.visibility = "hidden";
                        this.liArray[j].style.border = "1px solid #ffffff";
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
            //获得商品收藏列表
            getCollectionsListAll() {
                getCollectionsList({
                    page: this.page.currentPage,
                    limit: this.page.limit,
                })
                    .then((res) => {
                        if (res.data && res.data.code == 200) {
                            this.collectionList = res.data.data.list;
                            this.pageTotal = res.data.data.total;
                            this.pageFlag = true;
                            if (this.pageTotal == 0) {
                                this.emptyOrNot = true;
                            }
                        }
                    })
                    .catch((err) => {});
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
            //加入到购物车
            addCat(specId) {
                //goodsNum 产品的数量
                postCart({ specId: specId, goodsNum: 1 })
                    .then((res) => {
                        if (res && res.code == 200) {
                            this.$Message.success(res.msg);
                            this.$store.dispatch("main/actCartList"); //刷新一下购物车中的数据
                        }
                    })
                    .catch((err) => {});
            },
        },
        created() {},
        mounted() {
            this.getCollectionsListAll();
            this.picturePrefix = this.$imgURL;
            this.checklist = document.getElementsByName("selected");
            this.liArray = document.getElementsByName("li-array");
            this.iconList = document.getElementsByName("icon");
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/assets/scss/modules/modal.scss";
    /deep/ .ivu-modal {
        border: 8px solid rgba(0, 0, 0, 0.15);
        border-radius: 10px;
    }
    .delete {
        /deep/ .ivu-modal-content {
            border-radius: 0;
        }
        /deep/ .ivu-modal-body {
            padding: 40px 16px 30px 16px;
        }
        /deep/ .ivu-modal-footer {
            border: none;
            height: 90px;
            .footerBtn {
                display: flex;
                justify-content: center;
                padding: 0;
            }
        }
        /deep/ .ivu-modal-close {
            right: 10px;
            top: 10px;
        }
    }
    .newBtn {
        width: 69px;
        height: 34px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 2px;
        &:hover {
            color: #333333;
        }
    }
    .newBtn1:hover {
        border-color: #dcdee2;
    }

    .favorite-store-list {
        height: 100%;
        background: #ffffff;
    }
    .ivu-btn-primary {
        font-size: 14px;
        text-align: center;
        display: block;
        width: 64px;
        height: 24px !important;
        line-height: 24px;
        // margin: 0 auto;
        background: linear-gradient(360deg, #f3513a 0%, #f83729 100%);
        color: #fff;
        // margin-bottom: 10px;
        border-radius: 20px;
        // margin-top: 3px;
    }
    .img-wrapper {
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
    .store-top {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        background: #ffffff;
        height: 54px;
        padding: 0 40px;
        line-height: 54px;
        .store-top-left {
            font-size: 14px;
            color: #333333;
            font-weight: 600;
        }
        .store-top-right {
            .status1 {
                font-size: 14px;
                color: #a3a3a3;
                font-weight: 400;
                img {
                    width: 15px;
                    vertical-align: middle;
                    margin-bottom: 3px;
                }
                &:hover {
                    color: #dd2619;
                }
            }
        }
    }
    .content {
        width: 217px;
        // margin: 0 auto;
        padding: 20px;
        position: relative;
    }
    .status1 {
        cursor: pointer;
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
    // .close {
    //   width: 25%;
    //   margin-top: 20px;
    //   position: relative;
    //   padding: 50px 28px 20px 28px;
    //   border:1px solid rgba(0,0,0,0)
    // }
    // .closeAnother {
    //   width: 25%;
    //   border: 1px solid #f84539;
    //   margin-top: 20px;
    //   position: relative;
    //   padding: 50px 28px 20px 28px;
    // }
    .store-main {
        width: 100%;
        // margin-top: 10px;
        background: #ffffff;
        padding: 20px 40px 40px 40px;
        ul {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;

            li {
                width: 25%;
                position: relative;
                // padding: 20px;
                // :hover{
                //   box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
                //   .store-name{
                //     color: #dd2619;
                //   }
                // }
                i {
                    display: none;
                }

                i.on {
                    display: block;
                    visibility: hidden;
                    position: absolute;
                    right: 1px;
                    top: -1px;
                    background: #dd2619;
                    color: #ffffff;
                }
            }
            li:last-child {
                margin-right: auto;
            }
        }
    }

    .store-name {
        height: 14px;
        line-height: 14px;
        font-size: 14px;
        color: #222222;
        font-weight: 600;
        margin-top: 20px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        cursor: pointer;
    }
    .store-name:hover {
        color: #dd2619;
    }
    .concerned-people {
        height: 12px;
        line-height: 12px;
        font-size: 12px;
        color: #999999;
        font-weight: 400px;
        margin-top: 8px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        &:hover {
            color: red;
            cursor: pointer;
        }
    }
    .store-price {
        height: 24px;
        line-height: 24px;
        display: flex;
        justify-content: space-between;
        margin-top: 7px;
    }
    .concerned-price {
        // padding: 6px 0;
        // margin-top: 10px;
        .symbol {
            font-size: 14px;
            color: #dd2619;
            margin-right: -5px;
        }
        .number {
            font-size: 18px;
            font-weight: 600;
            color: #dd2619;
        }
    }
    .store-bottom {
        text-align: center;
        margin-top: 30px;
    }
    .placeholder {
        width: 230px;
        height: 0;
    }
    .noshop {
        width: 100%;
        height: 100%;
    }
    .noshop-bottom {
        margin-top: 10px;
        background: #fff;
        text-align: center;
        height: auto;
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
                cursor: pointer;
                border-radius: 1px;
            }
        }
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
        width: 10px;
        height: 10px;
        border: 1px solid #dcdee2;
        border-radius: 2px;
        background-color: #ffffff;
        display: inline-block;
        position: relative;
        border-radius: 4px;
        cursor: pointer;
        overflow: hidden;
    }
    #controlAll:checked + label.label1:before {
        content: "\2713"; /*这里设置选中之后的显示内容*/
        font-size: 0px;
        width: 10px;
        height: 10px;
        display: block;
        color: #ffffff;
        text-align: center;
        // font-weight: bolder;
        line-height: 5px;
        margin-left: 0px;
        background-color: #dd2619;
    }
    .label2 {
        margin-right: 10px;
        font-size: 10px;
    }
    .cancel {
        margin-right: 10px;
        font-size: 12px;
        color: #999999;
        font-weight: 400;
        &:hover {
            background: #dd2619;
            color: #ffffff;
        }
    }
    .finish {
        font-size: 12px;
        color: #999999;
        font-weight: 400;
        &:hover {
            background: #dd2619;
            color: #ffffff;
        }
    }
    /deep/ .store-main ul li {
        &:hover {
            .content {
                // box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.1);
            }
            .store-name {
                color: #dd2619;
            }
        }
    }
    /deep/ .ivu-btn {
        height: 20px;
        padding: 0 7px !important;
    }

    /deep/ .ivu-page-item {
        border-radius: 0px !important;
    }
    /deep/ .ivu-page .ivu-page-prev {
        padding: 0 6px 0 8px !important;
        border-radius: 0px !important;
    }
    /deep/ .ivu-page .ivu-page-disabled .ivu-icon {
        color: #3a3a3a !important;
    }
    /deep/ .ivu-page .ivu-page-next {
        padding: 0 8px 0 6px !important;
        border-radius: 0px !important;
    }
    /deep/ .ivu-page-options-elevator input {
        border-radius: 0px !important;
    }
    /deep/ .confirm-btn {
        line-height: 22px !important;
        border-radius: 0px !important;
    }
    /deep/ .li-array {
        &:active {
            border: 1px solid #ebebeb;
        }
    }
</style>
