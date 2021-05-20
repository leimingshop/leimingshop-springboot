<template>
    <div class="myGrid">
        <el-row>
            <el-col :span="24">
                <div class="rltv">
                    <div class="tree-title tree-head-first">{{mate.title}}</div>
                    <!-- <div class="tree-title tree-head-first">新增下级</div> -->
                    <el-row class="last">
                        <el-col v-for="col in mate.columns" :key="col.label" :span="col.span" class="tree-title" :class="{'text-center': col.center}">{{col.label}}</el-col>
                        <el-col :span="actionSpan" class="tree-title text-center" v-if="this.mate.actions && this.mate.actions.length">操作</el-col>
                    </el-row>
                </div>
                <div class="myGridTree">
                    <el-tree 
                        :data="mate.rows" 
                        node-key="id" 
                        :props="defaultProps" 
                        v-bind="mate.options" 
                        @check-change="handleCheckChange" 
                        :render-content='renderContent'
                        @node-click="handleClick" 
                        @current-change="handleCurrentChange" ref="myTreeGrid"
                        @node-expand='handleIsExpand'
						@node-collapse='handleIsCollapse'
						:default-expanded-keys='openrowid'
                    ></el-tree>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
//删除数组一个元素
	function arrRemove(arr, value) {
	    var i = arr.length;
	    while (i--) {
	        if (arr[i] === value) {
	            return i;
	        }
	    }
	    return false;
    }	
    
    import MyButton from './MyButton.vue';

    export default {
        components: {
            MyButton
        },
        // props: {
        //     mate: Object,
        //     required: true
        // },
        props: {
            mate: {
                type: Object,
                required: true
            },
            children:{
                type: String,
                default: 'children'
            },
            label:{
                type: String,
                default: 'label'
            }
        },
        data () {
            return {
                defaultProps: {
                    children: "children",
                    label: "label"
                    // children: this.children,
                    // label: this.label
                },
                tree: null,
                btnDisable: true,
                actionSpan: 0,
                openrowid:[], //默认打开的节点id
            };
        },
        mounted () {
            this.tree = this.$refs.myTreeGrid;
        },
        created () {
            this.execSpan();
        },
        methods: {
            handleIsExpand(d,n,s){  //点击>和节点name时       节点展开时触发的事件
                this.openrowid.push(d.id);   //开启时   加到数组中
                console.log(this.openrowid)
			},
			handleIsCollapse(d,n,s){   //点击>和节点name时   节点关闭时触发的事件
				this.openrowid.splice(arrRemove(this.openrowid,d.id), 1);  //关闭时   从去掉默认打开的目录
                // console.log(this.openrowid)
            },
            // 计算按钮宽度
            execSpan () {
                let sum = 0;
                this.mate.columns.map(col => {
                    sum += Number.parseInt(col.span);
                });
                this.actionSpan = 24 - sum;
                this.mate.actions = this.mate.actions.filter(item => !item.isHide);
                if (!this.mate.actions || !this.mate.actions.length) {
                    let last = this.mate.columns.length;
                    this.mate.columns[last - 1].span += this.actionSpan;
                }
            },
            renderContent: function (h, node) {
                // 生成按钮
                let buttons = [];
                if (this.mate.actions) {
                    for (let btn of this.mate.actions) {
                        btn.text = typeof btn.prop === 'function' ? btn.prop(node.data) : btn.prop || '按钮';
                        buttons.push(h(MyButton, {
                            props: {
                                name: btn.text,
                                row: node.data,
                                type: btn.type,
                                className: btn.className || ''
                            },
                            on: {
                                action: btn.action
                            }
                        }));
                    }
                }
                // 单元格渲染
                let colSpan = 0;
                let cols = this.mate.columns.map((col) => {
                    let value = '--',
                        key =  col.prop;
                    if (typeof key === 'function') {
                        value = key.call(this.mate._self || null, h, node.data);
                        value = value instanceof Array ? value : [value];
                    } else if (typeof key === 'string' && node.data[key]) {
                        value = node.data[key];
                    }
                    colSpan = Number(col.span);
                    if (colSpan <= 0) colSpan = 3;
                    return h('el-col', {props: {span: colSpan}, 'class': {'text-center': col.center, 'textIndex5': true}}, value);
                });
                // 插入行按钮
                cols.push(h('el-col', {props: {span: this.actionSpan}, 'class': {'text-center': true}}, buttons));
                var seq = node.data.seq?node.data.seq:""
                return h('span', [h('span', seq+"   "+node.data.label), h('div', {'class': {'line-row': true}}, cols)]);
            },
            handleCheckChange (...list) {
                this.$emit('check-change', ...list);
            },
            handleClick (...list) {
                this.$emit('node-click', ...list);
            },
            handleCurrentChange (...list) {
                this.$emit('current-change', ...list);
            }
        }
    };
</script>

<style lang="scss">
    $textIn: 8px;
    $borderColor: #e6ebf5;
    $lineH: 53px;
    .myGrid {
        .tree-title {
            text-align: center;
            border-bottom: 1px solid $borderColor;
            border-right: 1px solid $borderColor;
            white-space: nowrap;
            overflow: hidden;
            background-color: #ecf1f6;
            height: $lineH;
            text-indent: $textIn;
            line-height: $lineH;
            min-width: 0;
            text-overflow: ellipsis;
            box-sizing: border-box;
            font-size: 14px;
            color: #333;
        }

        .tree-head-first {
            width: 30%;
            padding-left: 15px;
            position: absolute;
            left: 0;
            top: 0;
        }
        .el-tree-node__content {
            line-height: $lineH;
            height: $lineH;
            position: relative;
            border: 1px solid $borderColor;
            border-top: none;
            text-align: left;
            span:nth-child(3){
                width:25%;
                &>span{
                    display: block;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    overflow-wrap: initial;
                }
            }
            .line-row {
                text-align: center;
                position: absolute;
                left: 30%;
                top: 0;
                width: 70%;
                margin-left: -1px;
                .el-col {
                    overflow: hidden;
                    border-left: 1px solid $borderColor;
                }
            }
        }
        .myGridTree {

        }
        .line-btn {
            margin-left: 0;
            position: absolute;
            top: 10px
        }

        .textIndex5 {
            text-indent: $textIn;
        }

        .last {
            margin-left: 30%;
        }
        .rltv {
            position: relative;
            border: 1px solid #dfe6ec;
            border-bottom: 0;
        }
        .col-tree, .text-center {
            text-align: center;
        }
    }
</style>
