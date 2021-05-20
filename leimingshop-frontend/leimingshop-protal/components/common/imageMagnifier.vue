<template>
    <div
        class="image-magnifier"
        :style="style"
    >
        <img
            ref="img"
            :width="width"
            :height="height"
            v-lazy="src"
            :key="src"
            class="image-magnifier__img anim"
            @mouseenter="handleOver"
            @wheel="handleMove"
            @mousemove="handleMove"
            @mouseleave="handleOut"
        />

        <div
            ref="mask"
            class="image-magnifier__mask"
            :class="maskClass"
            :style="maskStyle"
        ></div>

        <div
          class="image-magnifier__zoom"
          :class="zoomClass"
          :style="zoomStyle"
          v-show="zoomShow"
        >
            <img v-lazy="zoomSrc" :style="zoomImgStyle"/>
        </div>
    </div>
</template>

<script>
    import $ from 'jquery'
    import { debounce } from 'lodash'

    export default {
      name: "ImageMagnifier",
      props: {
        width: {
          default: 'auto'
        },
        height: {
          default: 'auto'
        },
        src: '',
        zoomSrc: '',
        zoomWidth: {
          default: 'auto'
        },
        zoomHeight: {
          default: 'auto'
        },
        zoomClass: {},
        maskWidth: {
          default: 100
        },
        maskHeight: {
          default: 100
        },
        maskBgColor: {
          default: '#409eff'
        },
        maskOpacity: {
          default: .5
        },
        maskClass: {},
        delayIn: {
          type: Number,
          default: 0
        },
        delayOut: {
          type: Number,
          default: 0
        }
      },
      data() {
        return {
          zoomShow: false,
          imgRect: '',
          maskRect: '',
          maskX: 0,
          maskY: 0,
          zoomImage: '',
          zoomLeft: '',
          zoomImgWidth: 0,
          zoomImgHeight: 0,
          zoomPosition: {
            x: 0,
            y: 0
          },
          zoomInTimeoutId: null,
          zoomOutTimeoutId: null,
          isInit: true, // 页面加载完 ，鼠标在图片的位置，假设 是
        }
      },
      computed: {
        style() {
          return {
            position: 'relative',
            cursor: 'move'
          }
        },
        maskStyle() {
          return {
            position: 'absolute',
            width: `${this.maskWidth}px`,
            height: `${this.maskHeight}px`,
            opacity: this.maskOpacity,
            backgroundColor: this.maskBgColor,
            left: 0,
            top: 0,
            transform: `translate(${this.maskX}px, ${this.maskY}px)`,
            willChange: 'transform',
            pointerEvents: 'none',
            zIndex: 1000,
            visibility: this.zoomShow ? 'visible' : 'hidden',
          }
        },
        zoomStyle() {
              return {
                  width: `${this.zoomWidth}px`,
                  height: `${this.zoomHeight}px`,
                  position: 'absolute',
                  left: `${this.zoomLeft}px`,
                  top: 0,
                  overflow: 'hidden',
                  zIndex: 1000
              }
        },
        zoomImgStyle() {
            return {
                width: `${this.zoomImgWidth}px`,
                height: `${this.zoomImgHeight}px`,
                willChange: 'transform',
                transform: `translate(-${this.zoomPosition.x}px, -${this.zoomPosition.y}px)`,
            }
        }

      },
      created() {

      },

      mounted(){
      },

      methods: {
            handleInit(e){
                if( this.isInit ){
                    this.isInit = false
                    this.imgRect = this.$refs.img && this.$refs.img.getBoundingClientRect();
                    this.maskRect = this.$refs.mask && this.$refs.mask.getBoundingClientRect();
                    this.maskX = this.outXCheck(e.clientX - this.imgRect.left );
                    this.maskY = this.outYCheck(e.clientY - this.imgRect.top);

                    this.zoomLeft = parseInt(this.width) + 10;
                    //计算大图偏移量
                    this.zoomPosition.x = this.maskX * (this.zoomImgWidth / this.width)
                    this.zoomPosition.y = this.maskY * (this.zoomImgHeight / this.height)
                }
            },

            async handleOver(e) {
                this.calcZoomSize();

                this.handleInit(e)

                clearTimeout(this.zoomOutTimeoutId);

                if (this.delayIn === 0) {
                  this.zoomShow = true;
                } else {
                  this.zoomInTimeoutId = setTimeout(() => {
                    this.zoomShow = true;
                  }, this.delayIn)
                }
            },

            calcZoomSize() {
                this.imgRect = this.$refs.img && this.$refs.img.getBoundingClientRect();
                this.maskRect = this.$refs.mask && this.$refs.mask.getBoundingClientRect();

                //计算大图宽高
                if (this.imgRect && this.maskRect) {
                  this.zoomImgWidth = (this.width / this.maskRect.width) * this.zoomWidth;
                  this.zoomImgHeight = (this.height / this.maskRect.height) * this.zoomHeight;
                }
            },

            handleMove(e) {
                if (!this.imgRect || !this.maskRect) {
                  return;
                }
                this.maskX = this.outXCheck(e.clientX - this.imgRect.left);
                this.maskY = this.outYCheck( $(window).scrollTop() - $('.image-magnifier__img').offset().top + e.clientY )

                this.zoomLeft = parseInt(this.width) + 10;

                //计算大图偏移量
                this.zoomPosition.x = this.maskX * (this.zoomImgWidth / this.width)
                this.zoomPosition.y = this.maskY * (this.zoomImgHeight / this.height)
            },

            handleOut() {
                clearTimeout(this.zoomInTimeoutId);
                if (this.delayOut === 0) {
                  this.zoomShow = false;
                } else {
                  this.zoomOutTimeoutId = setTimeout(() => {
                    this.zoomShow = false;
                  }, this.delayOut);
                }
            },

            outXCheck(x) {
                x = x - this.maskRect.width / 2;
                if (x < 0) {
                  return 0;
                }
                if (x + this.maskRect.width > this.width) {
                    return this.width - this.maskRect.width;
                }
                return x;
            },

            outYCheck(y) {
                y = y - this.maskRect.height / 2;
                if (y < 0) {
                  return 0;
                }
                if (y + this.maskRect.height > this.height) {
                  return this.height - this.maskRect.height;
                }
                return y;
            }
      }
    }
</script>

<style lang="scss" scoped>
    @keyframes mymove{
        from { transform: scale(1.25); opacity: .2; filter: blur(10px);}
        to { transform: scale(1); opacity: 1; filter: blur(0);}
    }
    .image-magnifier{
        img{
            &.anim{
                // animation:mymove .6s 1;
            }
        }
    }
    .image-magnifier__zoom{
        background-color:#ffffff;
    }
</style>
