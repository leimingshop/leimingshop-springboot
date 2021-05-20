<template>
  <div>
    <list v-if="showListVisible" ref="listCompon" @addArticleFn="addArticleFn"></list>
    <addoradit v-if="addArticleVisible" ref="addoraditCompon" @addArticleList="addArticleList"></addoradit>
  </div>
</template>
<script>
  import list from './list'
  import addoradit from './addArticle'

  export default {
    data () {
      return {
        showListVisible: true,
        addArticleVisible: false
      }
    },
    components: {
      list,
      addoradit,
    },
    methods: {
      addArticleFn (id) {
        this.addArticleVisible = true
        this.showListVisible = false
        this.$nextTick(() => {
          this.$refs.addoraditCompon.init(id)
        })

      },
      addArticleList () {
        this.addArticleVisible = false
        this.showListVisible = true
        this.$nextTick(() => {
          this.$refs.listCompon.getDataList()
        })

      }
    }
  }
</script>
