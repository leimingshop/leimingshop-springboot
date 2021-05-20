<template>
    <div 
        class="imageAndWords"
        v-if="imgAndText.length != 0"
    >
        <img
            v-for="(item, index) in imgAndText"
            :key="index"
            :src="item.file.src | filterImgUrl"
            alt=""
        />
    </div>

    <div
        v-else
        v-text="noData"
    ></div>
</template>

<script>
  export default{
    name: 'imageAndWords',

    props: {
        goodsBoby: {
            type: String,
            default: ''
        }
    },

    data(){
      return{
        noData: '暂无商品详情'
      }
    },

    components:{

    },
    created(){

    },
    computed:{
        imgAndText(){
            if( this.goodsBoby ){
                return JSON.parse( this.goodsBoby )
            } else {
                return []
            }
        }
    },
    watch:{

    },
    mounted(){

    },
    methods:{

    }
  }
</script>

<style lang="scss" scoped>
    .imageAndWords{
        text-align: center;
        font-size: 0;
        img{
            width: 100%;
        }
    }
</style>