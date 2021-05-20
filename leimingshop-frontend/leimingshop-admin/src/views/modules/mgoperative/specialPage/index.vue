<template>
    <div>
       <list v-if="mainSpecial" ref="listCompon" @addOrAdit="addOrAdit"></list>
       <addpage v-if="addSpecial" ref="addPageCompon" @addoraditList="addoraditList"></addpage>
    </div>
</template>
<script>
import list from './list.vue'
import addpage from './addPage.vue'

    export default {
        data(){
            return{
                mainSpecial:true,
                addSpecial:false,

            }
        },
        components: {
            list,
            addpage,
         },
        methods:{
            addOrAdit(id){
                this.addSpecial=true;
                this.mainSpecial=false;
                this.$nextTick(()=>{
					this.$refs.addPageCompon.init(id);
				})
            },
            addoraditList(){
                this.addSpecial = false;
                this.mainSpecial = true;
        	    this.$nextTick(()=>{
                    this.$refs.listCompon.getDataList();
        	    })
            },
           addorGoods(){

           },
        }
    }
</script>
<style scoped>


</style>
