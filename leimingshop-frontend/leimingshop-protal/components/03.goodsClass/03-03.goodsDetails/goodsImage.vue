<template>
    <div class="goods-image">
        <image-magnifier
            :src="(mainpic + '_400x400.png') | filterImgUrl"
            :zoomSrc="(mainpic + '_800x800.png') | filterImgUrl"
            width="480"
            height="480"
            zoom-width="480"
            zoom-height="480"
            maskWidth="200"
            maskHeight="200"
        ></image-magnifier>

        <div class="carouser-wrap">
            <span
                :class="[
                    'custom-carousel-arrow',
                    'left',
                    { forbidden: hoverIndex == 1 },
                ]"
                @click="handleTabLeft"
            ></span>

            <Carousel v-model="carouselItemIndex" dots="none" arrow="never">
                <CarouselItem v-for="(ITEM, INDEX) in picList" :key="INDEX">
                    <img
                        v-for="(item, index) in ITEM"
                        :key="item.id"
                        v-lazy="hanldeGoodsPic(item.pictureUrl)"
                        :class="{ active: carouselItemImgIndex == index }"
                        @mouseover="handleMouseover(item.pictureUrl, index)"
                    />
                </CarouselItem>
            </Carousel>

            <span
                :class="[
                    'custom-carousel-arrow',
                    'right',
                    { forbidden: hoverIndex == maxIndex },
                ]"
                @click="handleTabRight"
            ></span>
        </div>
    </div>
</template>

<script>
    import imageMagnifier from "@/components/common/imageMagnifier";
    import { handleGroup } from "@/utils/array-handle";
    export default {
        name: "goodsImage",

        components: {
            imageMagnifier,
        },

        props: {
            goodsPicList: {
                type: Array,
                default: () => [],
            }, // 没有规格的时候默认展示图片

            specAttrValuePicList: {
                type: Array,
                default: () => [],
            },
        },

        data() {
            return {
                carouselItemIndex: 0, // carouselItem的选中下标
                hoverIndex: 1, // 图片总数中的 当前选中下标，（从1开始）
                carouselItemImgIndex: 0, // 每个carouselItem中image的选中下标
                maxIndex: 1, // carousel中的图片总数
                mainpic: "", // 显示主图
            };
        },

        created() {},

        computed: {
            picList() {
                if (this.specAttrValuePicList.length != 0) {
                    this.handleInit(this.specAttrValuePicList);
                    return handleGroup(this.specAttrValuePicList, 5)["newArray"];
                } else {
                    this.handleInit(this.goodsPicList);
                    return handleGroup(this.goodsPicList, 5)["newArray"];
                }
            },
        },

        watch: {},

        mounted() {},

        methods: {
            // 初始化
            handleInit(list) {
                let hoverIndex;

                this.maxIndex = list.length;

                this.mainpic = list.filter((item, index) => {
                    if (item.isMainPicture == 1) {
                        hoverIndex = index + 1;
                        return item.isMainPicture == 1;
                    }
                })[0].pictureUrl;

                this.carouselItemImgIndex = 0;
                this.carouselItemIndex = 0;
                this.hoverIndex = hoverIndex;
            },

            hanldeGoodsPic(img) {
                return `${this.$imgURL}${img}`;
            },

            // left tab btn
            handleTabLeft() {
                let hoverIndex = this.hoverIndex; // 图片总数中的 当前选中下标 （从1开始）
                let rule = -1; // 上个carouselItem的第一张图片

                if (hoverIndex == 1) return; // 是否跳到图片总数的第一张

                this.hoverIndex = hoverIndex - 1; // 图片总数中的当前选中下标 - 1（从1开始）
                this.carouselItemImgIndex -= 1; // 当前carouselItem选中图片的 上一张图片下标

                /* 是否跳到上个carouselItem */
                if (this.carouselItemImgIndex == rule) {
                    this.carouselItemImgIndex = 4; // 恢复到carouselItem的最后一张
                    this.carouselItemIndex -= 1; // 上一个carouselItem
                    this.carouselItemImgIndex = 4; // 上一个carouselItem的最后一张图片 (从4开始）
                }

                this.mainpic = this.picList[this.carouselItemIndex][
                    this.carouselItemImgIndex
                ].pictureUrl; // 当前主图
            },

            // right tab btn
            handleTabRight() {
                let maxIndex = this.maxIndex; // carousel中的图片总数
                let cIndex = this.carouselItemIndex; // carouselItem的选中下标
                let hoverIndex = this.hoverIndex; // 图片总数中的 当前选中下标  （从1开始）
                let rule = 5; // 当前carouselItem的最后一张图片

                if (hoverIndex == maxIndex) return; // 是否跳到图片总数的最后一张

                this.hoverIndex = hoverIndex + 1; // 图片总数中的当前选中下标 + 1（从1开始）
                this.carouselItemImgIndex += 1; //  当前carouselItem选中图片的 下一张图片下标

                /* 是否跳到下个carouselItem */
                if (this.carouselItemImgIndex % rule == 0) {
                    this.carouselItemImgIndex = 0; // 恢复到carouselItem的第一张
                    this.carouselItemIndex += 1; // 下一个carouselItem（从0开始）
                    this.carouselItemImgIndex = 0; // 下一个carouselItem的第-张图片
                }

                this.mainpic = this.picList[this.carouselItemIndex][
                    this.carouselItemImgIndex
                ].pictureUrl; // 当前主图
            },

            // carouselItem中图片的滑入事件
            handleMouseover(pic, index) {
                let cIndex = this.carouselItemIndex;

                this.mainpic = pic;
                this.carouselItemImgIndex = index;
                this.hoverIndex = 5 * cIndex + (index + 1);
            },
        },
    };
</script>

<style lang="scss" scoped>
    $primary-color: #dd2619;

    .image-magnifier {
        width: 480px;
        height: 480px;
        border: 1px solid #efefef;
        box-sizing: content-box;
        margin-bottom: 10px;
        /deep/ .image-magnifier__img {
            width: 100%;
            height: 100%;
        }

        /deep/ .image-magnifier__zoom {
            border: 1px solid #efefef;
        }
    }

    .carouser-wrap {
        width: 480px !important;
        position: relative;
    }

    .ivu-carousel {
        height: 70px;
    }

    /deep/ .ivu-carousel-item {
        width: 480px !important;
        height: 70px !important;
        padding: 0 40px;
        display: flex;

        img {
            width: 70px;
            height: 70px;
            border: 2px solid #ffffff;
            margin-left: 13px;
            object-fit: scale-down;
            transition: border 0.3s;
            /*&:hover,*/
            &.active {
                border: 2px solid $primary-color;
            }
        }
        img:nth-of-type(1) {
            width: 70px;
            height: 70px;
            border: 2px solid #ffffff;
            margin-left: 0px;
            object-fit: scale-down;
            transition: border 0.3s;
            /*&:hover,*/
            &.active {
                border: 2px solid $primary-color;
            }
        }
    }

    /deep/ .custom-carousel-arrow {
        display: block;
        width: 26px !important;
        height: 26px !important;
        position: absolute;
        z-index: 3;
        top: 0;
        bottom: 0;
        margin: auto;
        opacity: 1;
        transition: opacity 0.3s;
        cursor: pointer;
        &:hover {
            opacity: 0.4;
        }
        &:active {
            opacity: 1;
        }
        &.left {
            left: 0;
            background: url("/img/03.goodsClass/03-03.goodsDetail/arrow-left.png")
                no-repeat center;
        }
        &.right {
            right: 0;
            background: url("/img/03.goodsClass/03-03.goodsDetail/arrow-right.png")
                no-repeat center;
        }
        &.forbidden {
            opacity: 0.4;
            cursor: not-allowed;
        }
    }
</style>

<style lang="scss">
</style>
