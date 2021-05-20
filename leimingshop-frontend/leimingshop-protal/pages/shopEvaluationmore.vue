<template>
    <div style="width: 100%; background: #eee">
        <div class="personl-top" style="background: #ffffff">
            <personal-top></personal-top>
        </div>
        <div class="personal-center-base">
            <p class="personal-center-nav">
                <router-link to="/" tag="span" class="evaluationSheet"
                    >首页</router-link
                >
                <span style="color: #3a3a3a">></span>
                <router-link
                    to="/personalCenterBase/evaluationSheet"
                    tag="span"
                    class="evaluationSheet"
                    >评价晒单
                </router-link>
            </p>

            <!-- 多商品下单时间 -->
            <div
                class="person-center-header"
                v-show="orderGoodsList.length > 1"
            >
                <span>下单时间：{{ orderGoodsList[0].createDate }}</span>
                <span @click="gohandleDetail(orderGoodsList[0].orderId)"
                    >订单编号：<span class="orderno">{{
                        orderGoodsList[0].orderSn
                    }}</span></span
                >
            </div>

            <!-- 多商品 -->
            <div
                class="person-center-content"
                v-for="(item, index1) in orderGoodsList"
                :key="index1"
                v-show="orderGoodsList.length > 1"
            >
                <div class="person-center-left">
                    <div class="person-center-top">
                        <div
                            class="click"
                            @click="goshopDetailPage(item.goodsId, item.specId)"
                        >
                            <div
                                class="person-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    :data-src="$imgURL + item.goodsImage"
                                    alt
                                />
                            </div>
                            <div class="text-explain">{{ item.goodsName }}</div>
                            <div class="explain">{{ item.goodsSubTitle }}</div>
                            <div class="price">
                                <span>￥</span>
                                <span>{{
                                    item.specPrice.toString().split(".")[0]
                                }}</span>
                                <span
                                    >.{{
                                        item.specPrice.toString().split(".")
                                            .length > 1
                                            ? item.specPrice
                                                  .toString()
                                                  .split(".")[1]
                                            : "00"
                                    }}</span
                                >
                            </div>
                            <div class="person-center-bottom">
                                <div>评价人数：{{ item.num }}条</div>
                                <div v-show="orderGoodsList.length < 2">
                                    订单编号：{{ orderGoodsList[0].orderSn }}
                                </div>
                                <div v-show="orderGoodsList.length < 2">
                                    下单时间：{{ orderGoodsList[0].createDate }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div
                    class="center-line"
                    v-show="orderGoodsList.length > 1"
                ></div>
                <div class="person-center-right">
                    <div class="shopgrade">
                        <span class="shopgrade-left">商品评分：</span>
                        <Rate
                            custom-icon="rate-icon"
                            v-model="form[index1].evaluateScores"
                        />
                        <span class="shopgrade-right">(请选择评分)</span>
                    </div>
                    <div class="texteare">
                        <span class="texteare-span">填写评价：</span>
                        <Input
                            v-model="form[index1].evaluateContent"
                            :maxlength="maxlength"
                            type="textarea"
                            show-word-limit
                            :rows="8"
                            :autosize="{ minRows: 8, maxRows: 8 }"
                            @input="descInput(index1)"
                            placeholder="宝贝是否满足了你的期待？说说你的使用心得分享给其它想购买的朋友吧。"
                        />
                    </div>

                    <div class="question">
                        <span class="null"></span>
                        <div class="question-right">
                            <img
                                v-lazy="
                                    '/img/03.goodsClass/03-03.goodsDetail/question.png'
                                "
                                alt
                            />
                            <Tooltip
                                content="Bottom Center 文字提示"
                                placement="bottom"
                            >
                                <span class="quality"
                                    >如何写高质量的评价？</span
                                >
                                <div slot="content" class="bubble">
                                    <p>
                                        <i>感受如何：记录下开箱时的感受。</i>
                                    </p>
                                    <p>
                                        <i
                                            >产品如何：写点有理有据的产品分析。</i
                                        >
                                    </p>
                                    <p>
                                        <i
                                            >提点建议：说说你的改进建议和好点子。</i
                                        >
                                    </p>
                                </div>
                            </Tooltip>
                        </div>
                    </div>
                    <div class="sheet">
                        <span class="sheet-span">评价晒单：</span>
                        <!-- 外部定义 -->
                        <div class="personalContent-top">
                            <div
                                class="pictureShow"
                                style="display: flex"
                                v-viewer
                            >
                                <div :images="imgFiles" class="bigImg">
                                    <div
                                        class="moreImg"
                                        v-for="(imgsrc, index) in imgFiles[
                                            index1
                                        ]"
                                        :key="index"
                                        v-lazy-container="{
                                            selector: 'img',
                                            error:
                                                '/img/common/loading/200_200.png',
                                            loading:
                                                '/img/common/loading/200_200.png',
                                        }"
                                    >
                                        <a
                                            class="delete-A"
                                            @click.stop.prevent="
                                                imgFiles[index1].splice(
                                                    index,
                                                    1
                                                );
                                                imgSrcs[index1].splice(
                                                    index,
                                                    1
                                                );
                                            "
                                        ></a>
                                        <img
                                            class="indexImg"
                                            :data-src="imgsrc"
                                            title="点击图片预览"
                                        />
                                    </div>
                                </div>
                            </div>
                            <!-- 添加主图 -->
                            <div
                                class="onContent"
                                v-show="imgFiles[index1].length < 8"
                            >
                                <Upload
                                    ref="upload"
                                    :max-size="5 * 1024"
                                    :show-upload-list="false"
                                    :format="[
                                        'jpg',
                                        'jpeg',
                                        'png',
                                        'bmp',
                                        'gif',
                                    ]"
                                    accept="image/bmp, image/gif, image/jpg, image/jpeg, image/png"
                                    :on-exceeded-size="maxSize"
                                    :before-upload="handleUpload"
                                    type="drag"
                                    multiple
                                    action
                                    :disabled="disabled"
                                >
                                    <div
                                        class="add-image-btn"
                                        @click="rememberIndex(index1)"
                                    >
                                        <Spin fix v-if="uploadLoading"></Spin>
                                    </div>

                                    <input
                                        type="hidden"
                                        v-model="form.evaluateImage"
                                    />
                                </Upload>
                            </div>
                        </div>
                    </div>

                    <div class="limit">
                        <span class="null"></span>
                        <div class="limit-right">
                            <p class="surplus">
                                共
                                <span>{{ imgFiles[index1].length }}</span
                                >张，还能上传
                                <span>{{ 8 - imgFiles[index1].length }}</span
                                >张图片
                            </p>
                            <p class="more">
                                最多上传8张图片,每张图片大小不超过5M,支持jpg,png,jpeg,bmp,gif格式文件
                            </p>
                        </div>
                    </div>

                    <!-- 多商品提交按钮样式 -->
                    <div class="submit" v-show="orderGoodsList.length <= 1">
                        <span class="null"></span>
                        <button class="submit-button" @click="Submit()">
                            提交
                        </button>
                    </div>
                </div>
            </div>

            <!-- 单商品 -->
            <div
                class="person-center-content-one"
                v-for="(item, index1) in orderGoodsList"
                :key="index1 + '-value'"
                v-show="orderGoodsList.length == 1"
            >
                <div class="person-center-left-one">
                    <div class="person-center-top">
                        <div
                            class="click"
                            @click="
                                goshopDetailPage(
                                    orderGoodsList[0].goodsId,
                                    orderGoodsList[0].specId
                                )
                            "
                        >
                            <div
                                class="person-img"
                                v-lazy-container="{
                                    selector: 'img',
                                    error: '/img/common/loading/200_200.png',
                                    loading: '/img/common/loading/200_200.png',
                                }"
                            >
                                <img
                                    :data-src="$imgURL + item.goodsImage"
                                    alt
                                />
                            </div>
                            <div class="text-explain">{{ item.goodsName }}</div>
                            <div class="explain">{{ item.goodsSubTitle }}</div>
                            <div class="price">
                                <span>￥</span>
                                <span>{{
                                    item.specPrice.toString().split(".")[0]
                                }}</span>
                                <span
                                    >.{{
                                        item.specPrice.toString().split(".")
                                            .length > 1
                                            ? item.specPrice
                                                  .toString()
                                                  .split(".")[1]
                                            : "00"
                                    }}</span
                                >
                            </div>
                        </div>

                        <div
                            class="jump"
                            @click="gohandleDetail(orderGoodsList[0].orderId)"
                        >
                            <div class="price-line"></div>
                            <div class="person-center-bottom">
                                <div>评价人数：{{ item.num }}条</div>
                                <div v-show="orderGoodsList.length < 2">
                                    订单编号：{{ orderGoodsList[0].orderSn }}
                                </div>
                                <div v-show="orderGoodsList.length < 2">
                                    下单时间：{{ orderGoodsList[0].createDate }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="person-center-right-one">
                    <div class="shopgrade">
                        <span class="shopgrade-left">商品评分：</span>
                        <Rate
                            custom-icon="rate-icon"
                            v-model="form[index1].evaluateScores"
                        />
                        <span class="shopgrade-right">(请选择评分)</span>
                    </div>
                    <div class="texteare">
                        <span class="texteare-span">填写评价：</span>
                        <Input
                            v-model="form[index1].evaluateContent"
                            :maxlength="maxlength"
                            type="textarea"
                            show-word-limit
                            :rows="8"
                            :autosize="{ minRows: 8, maxRows: 8 }"
                            style="overflow-y: auto"
                            @input="descInput(index1)"
                            placeholder="宝贝是否满足了你的期待？说说你的使用心得分享给其它想购买的朋友吧。"
                        />
                    </div>

                    <div class="question">
                        <span class="null"></span>
                        <div class="question-right">
                            <img
                                v-lazy="
                                    '/img/03.goodsClass/03-03.goodsDetail/question.png'
                                "
                                alt
                            />
                            <Tooltip
                                content="Bottom Center 文字提示"
                                placement="bottom"
                            >
                                <span class="quality"
                                    >如何写高质量的评价？</span
                                >
                                <div slot="content" class="bubble">
                                    <p>
                                        <i>感受如何：记录下开箱时的感受。</i>
                                    </p>
                                    <p>
                                        <i
                                            >产品如何：写点有理有据的产品分析。</i
                                        >
                                    </p>
                                    <p>
                                        <i
                                            >提点建议：说说你的改进建议和好点子。</i
                                        >
                                    </p>
                                </div>
                            </Tooltip>
                            <!-- <div class="text-input">
                                <span>还可输入</span>
                                <span class="number">{{ remnant }}</span>
                                字
                            </div> -->
                        </div>
                    </div>
                    <div class="sheet">
                        <span class="sheet-span">评价晒单：</span>
                        <!-- 外部定义 -->
                        <div class="personalContent-top">
                            <div
                                class="pictureShow"
                                style="display: flex"
                                v-viewer
                            >
                                <div :images="imgFiles" class="bigImg">
                                    <div
                                        class="moreImg"
                                        v-for="(imgsrc, index) in imgFiles[
                                            index1
                                        ]"
                                        :key="index"
                                        v-lazy-container="{
                                            selector: 'img',
                                            error:
                                                '/img/common/loading/200_200.png',
                                            loading:
                                                '/img/common/loading/200_200.png',
                                        }"
                                    >
                                        <a
                                            class="delete-A"
                                            @click.stop.prevent="
                                                imgFiles[index1].splice(
                                                    index,
                                                    1
                                                );
                                                imgSrcs[index1].splice(
                                                    index,
                                                    1
                                                );
                                            "
                                        ></a>
                                        <img
                                            class="indexImg"
                                            :data-src="imgsrc"
                                            title="点击图片预览"
                                            alt=""
                                        />
                                    </div>
                                </div>
                            </div>

                            <!-- 添加主图 -->
                            <div
                                class="onContent"
                                v-show="imgFiles[index1].length < 8"
                            >
                                <Upload
                                    ref="upload"
                                    :max-size="1024 * 5"
                                    :show-upload-list="false"
                                    :format="[
                                        'jpg',
                                        'jpeg',
                                        'png',
                                        'bmp',
                                        'gif',
                                    ]"
                                    accept="image/bmp, image/gif, image/jpg, image/jpeg, image/png"
                                    :on-exceeded-size="maxSize"
                                    :before-upload="handleUpload"
                                    type="drag"
                                    multiple
                                    action
                                    :disabled="disabled"
                                >
                                    <!-- multiple -->
                                    <!-- <Button class="mainButton" style="width:78px;height:78px" @click="rememberIndex(index1)"></Button> -->

                                    <div
                                        class="add-image-btn"
                                        @click="rememberIndex(index1)"
                                    >
                                        <Spin fix v-if="uploadLoading"></Spin>
                                    </div>

                                    <input
                                        type="hidden"
                                        v-model="form.evaluateImage"
                                    />
                                </Upload>
                            </div>
                        </div>
                    </div>

                    <div class="limit">
                        <span class="null"></span>
                        <div class="limit-right">
                            <p class="surplus">
                                共
                                <span>{{ imgFiles[index1].length }}</span
                                >张，还能上传
                                <span>{{ 8 - imgFiles[index1].length }}</span
                                >张图片
                            </p>
                            <p class="more">
                                最多上传8张图片,每张图片大小不超过5M,支持jpg,png,jpeg,bmp,gif格式文件
                            </p>
                        </div>
                    </div>

                    <!-- 单商品提交按钮样式 -->
                    <div class="submit" v-show="orderGoodsList.length <= 1">
                        <span class="null"></span>
                        <Button
                            type="primary"
                            @click="Submit"
                            :disabled="disabled"
                            >提交</Button
                        >
                    </div>
                </div>
            </div>

            <!-- 底部提交按钮 -->
            <div
                class="submit-bottom submit"
                v-show="orderGoodsList.length > 1"
            >
                <Button type="primary" @click="Submit" :disabled="disabled"
                    >提交</Button
                >
            </div>
        </div>
    </div>
</template>

<script>
    import { orderDetails } from "@/api/api_06-07-01personalMyOrders.js";
    import { uploadPicBase64 } from "@/api/api_Common.js";
    import {
        getEvaluatedId,
        postNewEvaluated,
    } from "@/api/api_06-02.evaluationSheet.js";
    import { goodsDetails } from "@/api/api_03.goodsClass.js";

    import { mapState, mapMutations, mapActions } from "vuex";

    import personalTop from "@/components/06.personalCenter/common/personalTop.vue";

    export default {
        name: "shopEvaluationmore",
        components: {
            personalTop,
        },
        head() {
            return {
                title: "商品评价",
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
                uploadLoading: false,
                disabled: false,
                isFileImg: true,
                addIndex: 0,
                orderId: this.$route.query.orderID ? this.$route.query.orderID : "",
                orderGoodsList: [
                    {
                        id: "",
                        createDate: "",
                        goodsId: "",
                        goodsImage: "",
                        goodsName: "",
                        num: 0,
                        orderId: "",
                        orderSn: "",
                        specId: "",
                        specPayPrice: "",
                        specPrice: "",
                    },
                ],
                form: [
                    {
                        evaluateContent: "",
                        evaluateImage: "",
                        evaluateScores: 5,
                        orderGoodsId: "",
                    },
                ],
                maxlength: 500,
                remnant: 500,
                imgName: "",
                modal: false,
                imgFiles: [[]],
                imgSrcs: [[]],
            };
        },
        computed: {
            ...mapState("evaluationSheet", [
                "evaluateListObject",
                "evaluateListObj",
            ]),
        },

        mounted() {
            this.order();
        },
        methods: {
            ...mapActions("evaluationSheet", [
                "getNoevaluatedList",
                "getEvaluatedList",
            ]),

            // 跳转商品详情页
            goshopDetailPage(goodsId, specId) {
                goodsDetails({
                    goodsId: goodsId,
                    specId: specId,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$router.push({
                                path: "/goodsDetails",
                                query: {
                                    goodsId: res.data.goodsDetailsVO.goodsId,
                                    specId: res.data.goodsDetailsVO.goodsVO.specId,
                                },
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },

            // 查看详情跳转到订单详情页
            gohandleDetail(orderId) {
                orderDetails({
                    id: orderId,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.$router.push({
                                path: "/personalCenterBase/myOrdersDetail",
                                query: {
                                    id: orderId,
                                    appStatus: res.data.appStatus,
                                },
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },

            order() {
                var params = {
                    orderId: this.orderId,
                    orderGoodsId: "",
                };

                if (this.$route.query.orderGoodsId != null) {
                    params.orderGoodsId = this.$route.query.orderGoodsId;
                }

                getEvaluatedId(params)
                    .then((res) => {
                        if (res.code !== 200) {
                            this.$Message.info(res.msg);
                        } else {
                            for (var i = 0; i < res.data.length - 1; i++) {
                                this.form.push({
                                    evaluateContent: "",
                                    evaluateImage: "",
                                    evaluateScores: 5,
                                    orderGoodsId: "",
                                });
                                this.imgFiles.push([]);
                                this.imgSrcs.push([]);
                            }
                            this.orderGoodsList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            rememberIndex(index) {
                this.addIndex = index;
            },
            // 限制输入框输入字数方法
            descInput(index1) {
                let txtVal = this.form[index1].evaluateContent.length;
                this.remnant = this.maxlength - txtVal;
            },

            maxSize(file) {
                this.$Message.warning(`图片【${file.name}】大小超出5M，请重新上传`);
            },
            //处理图片转换成base64
            handleUpload(file) {
                let that = this;
                let reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = (e) => {
                    this.baseFile = e.target.result;
                    this.picName = file.name;
                    this.isFileImg = !this.isFileImg;
                    // 如果大于8就取前8张图片
                    if (file.size > 1024 * 1024 * 5) {
                        return this.maxSize;
                    } else if (this.imgFiles[this.addIndex].length < 8) {
                        this.imgFiles[this.addIndex].push(this.baseFile);
                        this.uploadeImg();
                    }
                };
            },
            // 上传图片前的回调
            uploadeImg() {
                uploadPicBase64({
                    imgStr: this.baseFile,
                    pictureName: this.picName,
                }).then((res) => {
                    if (res.code == 200) {
                        this.imgSrcs[this.addIndex].push(res.data.url);
                    }
                });

                return false;
            },

            Submit() {
                this.disabled = true;

                for (var i = 0; i < this.form.length; i++) {
                    this.form[i].evaluateImage = this.imgSrcs[i].toString();
                    this.form[i].orderGoodsId = this.orderGoodsList[i].id;
                }

                postNewEvaluated(this.form)
                    .then((res) => {
                        console.log(res);
                        if (res && res.code == 200) {
                            this.$router.push({
                                path: "/personalCenterBase/evaluationSheet",
                            });

                            this.$nextTick(() => {
                                this.noevaluatedLoading = true;
                                this.pagingFlag1 = false;
                                this.getNoevaluatedList({
                                    page: 1,
                                    limit: this.pageSize,
                                })
                                    .then((res) => {
                                        this.noevaluatedLoading = false;
                                        if (
                                            this.evaluateListObject.list.length > 0
                                        ) {
                                            this.total = this.evaluateListObject.total;
                                            this.pagingFlag1 = true;
                                        } else if (
                                            this.evaluateListObject.list.length == 0
                                        ) {
                                            this.pagingFlag1 = true;
                                        } else {
                                            this.pagingFlag1 = false;
                                        }
                                    })
                                    .catch((err) => console.log(err));
                            });
                            this.$Message.success("提交成功");
                        }
                    })
                    .catch((err) => (this.disabled = false));
            },
        },
    };
</script>

<style lang="scss" scoped>
    .personal-center-base {
        width: 1200px;
        margin: 0 auto;
        padding-bottom: 48px;

        .personal-center-nav {
            font-size: 13px;
            padding: 15px 0;
            color: #333333;
            cursor: pointer;
        }

        .person-center-header {
            width: 100%;
            height: 44px;
            line-height: 44px;
            background: rgba(255, 255, 255, 1);
            margin-bottom: 10px;
            padding: 0 30px;
            // cursor: pointer;

            span {
                margin-right: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
            }

            span:last-child {
                cursor: pointer;
            }

            span:last-child .orderno:hover {
                color: #e72900;
            }
        }

        .person-center-content {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            background: rgba(255, 255, 255, 1);
        }

        .person-center-content-one {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
    }

    // 面包屑导航
    .evaluationSheet:hover {
        cursor: pointer;
        color: #e2270b;
    }

    // 单商品左侧
    .person-center-left-one {
        width: 300px;
        height: 615px;
        background: rgba(255, 255, 255, 1);
        display: flex;
        justify-content: center;
        margin-right: 20px;
        box-sizing: border-box;
        padding: 0 30px;
        .person-center-top {
            text-align: left;
            // padding: 32px;
            margin-top: 32px;
            width: 240px;
            .person-img {
                width: 180px;
                height: 180px;
                margin: auto;

                span {
                    position: absolute;
                    left: 0;
                    top: 0;
                    width: 56px;
                    height: 24px;
                }

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .click {
                cursor: pointer;

                .text-explain {
                    width: 193px;
                    margin-top: 15px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(58, 58, 58, 1);
                    margin-left: 30px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                .text-explain:hover {
                    color: #e72900;
                }

                .explain {
                    height: 12px;
                    line-height: 12px;
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    margin-left: 30px;
                    width: 140px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    margin-top: 10px;
                }

                .price {
                    margin-left: 30px;
                    color: rgba(221, 38, 25, 1);
                    margin-top: 10px;
                    span {
                        font-size: 14px;
                        color: rgba(221, 38, 25, 1);
                    }
                    span:nth-child(2) {
                        font-weight: bold;
                        font-size: 20px;
                        display: inline-block;
                        margin: 0 -4px;
                    }
                    span:last-child {
                        font-weight: 600;
                    }
                }
            }

            .jump {
                cursor: pointer;
            }

            .price-line {
                width: 240px;
                background: rgba(235, 235, 235, 1);
                height: 1px;
                margin: 29px 0 30px;
                // margin-left: 22px;
            }

            .person-center-bottom {
                width: 264px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                // padding-left: 17px;

                div {
                    margin-left: 20px;
                    line-height: 15px;
                    height: 15px;
                    margin-bottom: 15px;
                }
            }
        }
    }

    .person-center-right-one {
        width: 948px;
        background: rgba(255, 255, 255, 1);
        padding: 40px;
        padding-bottom: 0;
        box-sizing: border-box;

        .shopgrade {
            height: 45px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);

            .shopgrade-left {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                vertical-align: bottom;
            }

            .shopgrade-right {
                font-size: 13px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(163, 163, 163, 1);
                vertical-align: bottom;
            }
        }

        .texteare {
            display: flex;
            position: relative;

            .texteare-span {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(66, 66, 66, 1);
                width: 75px;
            }

            /deep/ textarea {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #333333;
                text-align: justify;
            }

            /deep/ .ivu-input {
                padding: 10px 10px 0 10px;
                border-color: none;
                /deep/ input::-webkit-input-placeholder {
                    color: #999999;
                }
                /deep/ input::-moz-placeholder {
                    color: #999999;
                }
                /deep/ input::-moz-placeholder {
                    color: #999999;
                }
                &:focus {
                    border-color: #dd2619 !important;
                    box-shadow: none !important;
                }
                &:hover {
                    border-color: #dd2619;
                }
            }

            /deep/ .ivu-input-word-count {
                margin-bottom: 6px;
                margin-right: 10px;
                color: rgba(102, 102, 102, 1);
                font-size: 12px;
            }

            .ivu-input-wrapper {
                width: 526px;
            }

            .input {
                position: absolute;
                right: 258px;
                bottom: 12px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
            }
        }

        .text-input {
            width: 365px;
            display: inline-block;
            text-align: right;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
        }

        .question {
            margin-bottom: 20px;

            .null {
                display: inline-block;
                width: 70px;
            }

            .question-right {
                display: inline-block;
                width: 527px;
                text-align: left;
                margin-top: 20px;

                img {
                    vertical-align: text-top;
                    margin-top: 1px;
                }

                .quality {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(76, 76, 76, 1);
                }

                .bubble {
                    text-align: left;
                    padding: 10px 10px;

                    p {
                        font-size: 14px;
                        color: #bdbdbd;
                    }
                }

                /deep/ .ivu-tooltip-popper .ivu-tooltip-content .ivu-tooltip-inner {
                    max-width: 500px !important;
                }
            }
        }

        .sheet {
            display: flex;

            .personalContent-top {
                display: flex;

                /deep/ .onContent {
                    width: 78px;
                    height: 78px;
                    /deep/ .ivu-upload {
                        display: inline-block;
                        width: 78px;
                        height: 78px;
                        &:hover {
                            border-color: #dd2619;
                        }
                    }
                    .add-image-btn {
                        width: 100%;
                        height: 100%;
                        background: url("/img/06.personalCenter/06-03.afterSale/add-image-btn.png")
                            center;
                        &:hover {
                            background: url("/img/06.personalCenter/06-03.afterSale/add-image-redbtn.png")
                                center;
                        }
                    }
                }

                .moreImg {
                    position: relative;
                }

                .moreImg .delete-A {
                    width: 18px;
                    height: 18px;
                    display: block;
                    position: absolute;
                    top: -8px;
                    right: -8px;
                    background: url("/img/06.personalCenter/delete.png") no-repeat
                        center/cover;
                }

                .moreImg .delete-A:hover {
                    width: 18px;
                    height: 18px;
                    display: block;
                    position: absolute;
                    top: -8px;
                    right: -8px;
                    background: url("/img/06.personalCenter/delete_hover.png")
                        no-repeat center/cover;
                }
            }

            .sheet-span {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(66, 66, 66, 1);
                width: 73px;
            }

            .sheet-tupian {
                display: inline-block;
                width: 78px;
                height: 78px;
                border-radius: 2px;
                border: 1px solid rgba(245, 245, 245, 1);
                margin-right: 14px;

                img {
                    height: 78px;
                    padding: 5px 7px;
                }
            }

            .upload {
                display: inline-block;
                width: 77px;
                height: 77px;
                border: 1px solid rgba(204, 204, 204, 1);

                .check {
                    display: inline-block;
                    width: 78px;
                    height: 78px;
                    background: url("/img/04.shoppingCar/编组 7.png") center;
                }
            }
        }

        .limit {
            .null {
                display: inline-block;
                width: 70px;
            }

            .limit-right {
                display: inline-block;

                .surplus {
                    margin-top: 15px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(125, 125, 125, 1);
                    letter-spacing: 1px;
                    span {
                        color: #e72900;
                        margin-left: -4px;
                    }
                }

                .more {
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    height: 15px;
                    line-height: 15px;
                    margin-top: 4px;
                }
            }
        }
    }

    // 多商品左侧
    .person-center-left {
        width: 300px;
        height: 534px;
        background: rgba(255, 255, 255, 1);
        display: flex;
        justify-content: center;
        align-items: center;

        .person-center-top {
            text-align: left;
            padding: 32px;

            .person-img {
                position: relative;
                width: 180px;
                height: 180px;
                margin: auto;

                span {
                    position: absolute;
                    left: 0;
                    top: 0;
                    width: 56px;
                    height: 24px;
                }

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .click {
                cursor: pointer;

                .text-explain {
                    width: 193px;
                    margin-top: 15px;
                    font-size: 16px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 600;
                    color: rgba(58, 58, 58, 1);
                    margin-left: 14px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                .text-explain:hover {
                    color: #e72900;
                }

                .explain {
                    font-size: 13px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    margin-left: 14px;
                    display: inline-block;
                    width: 140px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    margin-top: 2px;
                }

                .price {
                    margin-left: 14px;
                    margin-top: 3px;
                    span {
                        font-size: 14px;
                        color: rgba(221, 38, 25, 1);
                    }
                    span:nth-child(2) {
                        font-weight: bold;
                        font-size: 20px;
                        display: inline-block;
                        margin: 0 -4px;
                    }
                    span:last-child {
                        font-weight: 600;
                    }
                }
            }

            .person-center-bottom {
                margin-top: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);

                div {
                    margin-left: 15px;
                    height: 25px;
                    line-height: 25px;
                    height: 25px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }
            }
        }
    }

    .person-center-right {
        width: 948px;
        background: rgba(255, 255, 255, 1);
        padding: 40px;
        box-sizing: border-box;

        .shopgrade {
            height: 45px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);

            .shopgrade-left {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
                vertical-align: bottom;
            }

            .shopgrade-right {
                font-size: 13px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(163, 163, 163, 1);
                vertical-align: bottom;
            }
        }

        .texteare {
            display: flex;
            position: relative;

            .texteare-span {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(66, 66, 66, 1);
                width: 75px;
            }

            /deep/ textarea {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(163, 163, 163, 1);
                text-align: justify;
            }

            /deep/ .ivu-input {
                padding: 20px 10px 0 20px;
                ::placeholder {
                    color: #999999;
                }
            }

            /deep/ .ivu-input-word-count {
                margin-bottom: 6px;
                margin-right: 13px;
                color: rgba(102, 102, 102, 1);
                font-size: 12px;
            }

            .ivu-input-wrapper {
                width: 526px;
            }

            .input {
                position: absolute;
                right: 258px;
                bottom: 12px;
                font-size: 12px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
            }
        }

        .text-input {
            width: 365px;
            display: inline-block;
            text-align: right;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            .number {
                color: #dd2619;
            }
        }
        .question {
            margin-bottom: 20px;
            .null {
                display: inline-block;
                width: 70px;
            }
            .question-right {
                display: inline-block;
                width: 527px;
                text-align: left;
                margin-top: 20px;
                img {
                    vertical-align: text-top;
                }

                .quality {
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(76, 76, 76, 1);
                }

                .bubble {
                    text-align: left;
                    padding: 10px 10px;

                    p {
                        font-size: 14px;
                        color: #bdbdbd;
                    }
                }

                /deep/ .ivu-tooltip-popper .ivu-tooltip-content .ivu-tooltip-inner {
                    max-width: 500px !important;
                }
            }
        }

        .sheet {
            display: flex;
            .personalContent-top {
                display: flex;

                /deep/ .onContent {
                    width: 78px;
                    height: 78px;

                    /deep/ .ivu-upload {
                        display: inline-block;
                        width: 78px;
                        height: 78px;
                        &:hover {
                            border-color: #dd2619;
                        }
                    }
                    .add-image-btn {
                        width: 100%;
                        height: 100%;
                        background: url("/img/06.personalCenter/06-03.afterSale/add-image-btn.png")
                            center;
                        &:hover {
                            background: url("/img/06.personalCenter/06-03.afterSale/add-image-redbtn.png")
                                center;
                        }
                    }
                }

                .moreImg {
                    position: relative;
                }

                .moreImg .delete-A {
                    width: 18px;
                    height: 18px;
                    display: block;
                    position: absolute;
                    top: -8px;
                    right: -8px;
                    background: url("/img/06.personalCenter/delete.png") no-repeat
                        center/cover;
                }

                .moreImg .delete-A:hover {
                    width: 18px;
                    height: 18px;
                    display: block;
                    position: absolute;
                    top: -8px;
                    right: -8px;
                    background: url("/img/06.personalCenter/delete_hover.png")
                        no-repeat center/cover;
                }
            }

            .sheet-span {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(66, 66, 66, 1);
                width: 73px;
            }

            .sheet-tupian {
                display: inline-block;
                width: 78px;
                height: 78px;
                border-radius: 2px;
                border: 1px solid rgba(245, 245, 245, 1);
                margin-right: 12px;

                img {
                    height: 78px;
                    padding: 5px 7px;
                }
            }

            .upload {
                display: inline-block;
                width: 77px;
                height: 77px;
                border: 1px solid rgba(204, 204, 204, 1);

                .check {
                    display: inline-block;
                    width: 78px;
                    height: 78px;
                    background: url("/img/04.shoppingCar/编组 7.png") center;
                }
            }
        }

        .limit {
            .null {
                display: inline-block;
                width: 70px;
            }

            .limit-right {
                display: inline-block;

                .surplus {
                    margin-top: 15px;
                    font-size: 14px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(125, 125, 125, 1);
                    letter-spacing: 1px;
                    span {
                        color: #e72900;
                        margin-left: -4px;
                    }
                }

                .more {
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: rgba(153, 153, 153, 1);
                    height: 15px;
                    line-height: 15px;
                }
            }
        }
    }

    .center-line {
        width: 1px;
        height: 432px;
        margin-top: 40px;
        margin-bottom: 40px;
        background: rgba(235, 235, 235, 1);
    }

    // 单商品提交
    .submit {
        .null {
            display: inline-block;
            width: 70px;
        }

        .ivu-btn {
            width: 120px;
            height: 44px;
            line-height: 44px;
            border-radius: 22px;
            text-align: center;
            outline: none;
            border: none;
            margin-top: 38px;
            margin-bottom: 40px;
            font-size: 15px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 600;
            color: rgba(255, 255, 255, 1);
            cursor: pointer;
        }
    }

    // 多商品提交
    .submit-bottom {
        margin-top: 10px;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 105px;
        background: rgba(255, 255, 255, 1);

        .ivu-btn {
            margin-top: 0;
        }
    }

    .moreImg {
        display: inline-block;
        width: 78px;
        height: 78px;
        margin-right: 14px;
        border: 1px solid rgba(245, 245, 245, 1);

        img {
            width: 78px;
            height: 78px;
            // padding: 5px 7px;
        }
    }
    /deep/ .ivu-btn-primary {
        background: linear-gradient(90deg, #de2a1c 0%, #ff4e03 100%) !important;
    }
    /deep/ .person-center-right-one .texteare .ivu-input {
        padding: 20px !important;
    }
    /deep/ .ivu-rate-star-chart {
        margin-right: 4px;
    }
</style>
