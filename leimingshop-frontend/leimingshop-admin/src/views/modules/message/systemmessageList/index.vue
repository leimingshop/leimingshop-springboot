<template>
    <div>
        <list v-if="mainVisible" ref="listCompon" @add="add"></list>
        <add v-if="addVisible" ref="addCompon" @addList="addList"></add>
    </div>
</template>

<script>
    import list from "./list"
    import add from "./add"
    export default {
        data () {
            return {
                mainVisible:true,
                dtailVisible:false,
                addoraditVisible:false,
                addVisible:false
            }
        },
        components:{
            list,
            add
        },
        methods:{
            add(){
                this.addVisible = true;
                this.mainVisible = false;
            },
            showList(){
                this.dtailVisible = false;
                this.mainVisible = true;
                this.$nextTick(()=>{
                    this.$refs.listCompon.getDataList();
                })
            },
            addList(){
                this.addVisible = false;
                this.mainVisible = true;
                this.$nextTick(()=>{
                    this.$refs.listCompon.getDataList();
                })
            }
        }
    }
</script>

<style>
</style>