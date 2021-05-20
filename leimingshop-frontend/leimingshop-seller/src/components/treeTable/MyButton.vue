<template>
    <el-button :size="size" :disabled="btnDisable"
               type="text"
               @click.stop="handleAction"
               :plain="plain"
               :class="name=='删除'? 'artdanger' : ''">
        <span class="myBtn" :class="className" v-html="name"></span>
    </el-button>
</template>

<script>
    export default {
        props: {
            row: {
                type: Object
            },
            /**
             * 按钮外观样式
             */
            plain: {
                type: Boolean,
                'default': false
            },
            type: {
                type: String,
                'default': 'text'
            },
            name: {
                type: String
            },
            className: {
                type: [Object, String]
            },
            /** 
             * 按钮大小
             */
            size: {
                type: String,
                'default': 'small'
            }
        },
        computed: {
            btnDisable: function () {
                return this.row.disabled;
            }
        },
        // 需要添加刷新消息
        methods: {
            handleAction () {
                this.$emit('action', this.row);
            },
            getSwitchValue (key) {
                if (this.act.switch) return this.act[key][Number(this.row[this.act.switch])];
                return this.act[key];
            }
        }
    };
</script>

<style>
</style>
