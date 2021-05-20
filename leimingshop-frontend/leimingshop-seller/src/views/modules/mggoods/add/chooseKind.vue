<template>
    <div>
        <div class="chooseHistoryList">
            最近选择历史：
            <el-select
                v-model="chooseHistoryValue"
                @change="changeChooseValue"
                placeholder="请选择"
                style="width: 255px"
            >
                <el-option
                    v-for="item in chooseHistory"
                    :key="item.gcId"
                    :label="
                        item.gcName0 +
                        ' > ' +
                        item.gcName1 +
                        ' > ' +
                        item.gcName2
                    "
                    :value="item.gcId2"
                >
                </el-option>
            </el-select>
            <span>历史所选分类为近期所选过的分类 </span>
        </div>

        <el-row class="row-bg" :key="elRowKey">
            <!-- 第一级 -->
            <el-col :span="6" :offset="3">
                <el-card class="box-card">
                    <ul class="ulclass">
                        <div
                            v-for="(item, index) in goodscalssOption0"
                            @click="getGoodsClassFn(1, item)"
                            :key="index"
                            class="itemGcName"
                            :class="
                                item.id == dataForm.gcId0 ? 'currentItem' : ''
                            "
                        >
                            <li>
                                <span> {{ item.gcName }}</span>
                            </li>
                            <!-- v-if="goodscalssOption1 && goodscalssOption1.length>0" -->
                            <div class="iconWarp">
                                <i
                                    class="el-submenu__icon-arrow el-icon-arrow-right"
                                ></i>
                            </div>
                        </div>
                    </ul>
                </el-card>
            </el-col>
            <!-- 第二级 -->
            <el-col :span="6">
                <el-card class="box-card">
                    <ul class="ulclass">
                        <div
                            v-for="(item, index) in goodscalssOption1"
                            @click="getGoodsClassFn(2, item)"
                            :key="index"
                            class="itemGcName"
                            :class="
                                item.id == dataForm.gcId1 ? 'currentItem' : ''
                            "
                        >
                            <li>
                                <span> {{ item.gcName }}</span>
                            </li>
                            <!--  v-if="goodscalssOption1 && goodscalssOption2.length>0" -->
                            <div class="iconWarp">
                                <i
                                    class="el-submenu__icon-arrow el-icon-arrow-right"
                                ></i>
                            </div>
                        </div>
                    </ul>
                </el-card>
            </el-col>
            <!-- 第三级 -->
            <el-col :span="6">
                <el-card class="box-card">
                    <ul class="ulclass">
                        <div
                            v-for="(item, index) in goodscalssOption2"
                            @click="getGoodsClassFn(3, item)"
                            :key="index"
                            class="itemGcName"
                            :class="
                                item.id == dataForm.gcId2 ? 'currentItem' : ''
                            "
                        >
                            <li>
                                <span>
                                    {{ item.gcName }}
                                    <!-- 分类类型（0:实体商品分类，1:虚拟商品分类） 实体商品分类注掉 不显示 -->
                                    <!--                        <span v-if="item.classType==0"> (实体商品分类)</span>-->
                                    <span v-if="item.classType == 1">
                                        (虚拟商品分类)</span
                                    >
                                </span>
                            </li>
                            <!-- <div class="iconWarp">
                          <i class="el-submenu__icon-arrow el-icon-arrow-right"></i>
                      </div> -->
                        </div>
                    </ul>
                </el-card>
            </el-col>
        </el-row>
        <div class="bottomTip">
            <span>您当前的选择：</span>
            <span>{{ dataForm.gcName0 }} </span>
            <span v-if="dataForm.gcName1">> {{ dataForm.gcName1 }} </span>
            <span v-if="dataForm.gcName2">> {{ dataForm.gcName2 }} </span>
            <!-- <span v-for="item in goodscalssOption0">
               <span v-if="dataForm.gcId0==item.id">{{item.gcName}}</span>
            </span>
            <span v-if="dataForm.gcId1">></span>

            <span v-for="item in goodscalssOption1">
              <span v-if="dataForm.gcId1==item.id">{{item.gcName}}</span>
            </span>
            <span v-if="dataForm.gcId2">></span>

             <span v-for="item in goodscalssOption2">
                <span v-if="dataForm.gcId2==item.id">{{item.gcName}}</span>
            </span> -->
        </div>
        <div class="nextStepWrap">
            <el-button class="nextStep" type="primary" @click="nextStep()"
                >下一步</el-button
            >
        </div>
    </div>
</template>

<script>
    import { getGoodscalss } from "@/api/api.js";
    import { allTreeGoodsclass } from "@/api/api.js";

    export default {
        data() {
            return {
                goodKind1loading: false,
                goodKind2loading: false,
                ischooseEnd: false,
                dataForm: {
                    gcId: "", //分类ID
                    gcId0: "",
                    gcId1: "",
                    gcId2: "",
                    gcName: "",
                    gcName0: "",
                    gcName1: "",
                    gcName2: "",
                    virtualFlag: "",
                },
                goodscalssOption0: [],
                goodscalssOption1: [],
                goodscalssOption2: [],
                goodscalssOptions: [], // 存放所有分类的树形结构
                chooseHistory: [],
                chooseHistoryValue: "",
                elRowKey: 1,
            };
        },
        watch: {
            "dataForm.gcId0"(val) {
                this.goodscalssOption0.forEach((item, index) => {
                    if (item.id == this.dataForm.gcId0) {
                        this.dataForm.gcName0 = item.gcName;
                        item.children
                            ? (this.goodscalssOption1 = item.children)
                            : "";
                        this.judgeChooseHistoryValue();
                    }
                });
            },
            "dataForm.gcId1"(val) {
                this.goodscalssOption1.forEach((item, index) => {
                    if (item.id == this.dataForm.gcId1) {
                        this.dataForm.gcName1 = item.gcName;
                        item.children
                            ? (this.goodscalssOption2 = item.children)
                            : "";
                        this.judgeChooseHistoryValue();
                    }
                });
            },
            "dataForm.gcId2"(val) {
                this.goodscalssOption2.forEach((item, index) => {
                    if (item.id == this.dataForm.gcId2) {
                        this.dataForm.gcName2 = item.gcName;
                        item.children
                            ? (this.goodscalssOption3 = item.children)
                            : "";
                        this.judgeChooseHistoryValue();
                    }
                });
            },
        },
        components: {},
        created() {
            // 获取商品分类下拉
            // this.getGoodsClassFn(0);
            // 获取分类的所有树形结构
            this.getallTreeGoodsclassFn();
            this.getChooseHistory();
        },
        methods: {
            judgeChooseHistoryValue() {
                if (!this.dataForm.gcId2) {
                    this.chooseHistoryValue = "";
                    return;
                }
                if (
                    this.chooseHistory.find(
                        (item) => item.gcId2 === this.dataForm.gcId2
                    )
                ) {
                    this.chooseHistoryValue = this.dataForm.gcId2;
                } else {
                    this.chooseHistoryValue = "";
                }
            },
            // 通过select设置dataform
            changeChooseValue() {
                const index = this.chooseHistory.findIndex(
                    (item) => item.gcId2 === this.chooseHistoryValue
                );
                const {
                    gcId0,
                    gcId1,
                    gcId2,
                    gcName,
                    gcName0,
                    gcName1,
                    gcName2,
                    virtualFlag,
                } = this.chooseHistory[index];
                this.dataForm = {
                    gcId0,
                    gcId1,
                    gcId2,
                    gcName,
                    gcName0,
                    gcName1,
                    gcName2,
                    virtualFlag,
                };
                this.elRowKey++;
                this.ischooseEnd = true;
            },
            // 获取分类的所有树形结构
            getallTreeGoodsclassFn() {
                allTreeGoodsclass().then((res) => {
                    if (res.code == 200) {
                        this.goodscalssOptions = res.data;
                        this.getGoodsClassFn(0);
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            // 后去商品分类（三级联动）
            getGoodsClassFn(arguId, item) {
                var id = 0;
                if (arguId == 1) {
                    //二级关联
                    this.dataForm.gcId0 = item.id;
                    if (!this.dataForm.gcId0) {
                        this["goodscalssOption" + arguId] = [];
                        return;
                    }
                    id = this.dataForm.gcId0;

                    this.dataForm.gcId1 = "";
                    this.dataForm.gcId2 = "";
                    this.dataForm.gcName1 = "";
                    this.dataForm.gcName2 = "";
                    this["goodscalssOption1"] = [];
                    this["goodscalssOption2"] = [];
                } else if (arguId == 2) {
                    //三级关联
                    this.dataForm.gcId1 = item.id;
                    if (!this.dataForm.gcId1) {
                        this["goodscalssOption" + arguId] = [];
                        return;
                    }
                    id = this.dataForm.gcId1;

                    this.dataForm.gcId2 = "";
                    this.dataForm.gcName2 = "";
                    this["goodscalssOption2"] = [];
                } else if (arguId == 3) {
                    this.dataForm.gcId2 = item.id;
                    this.dataForm.virtualFlag = item.classType;
                } else {
                    //一级关联
                    id = 0;
                    this.dataForm.gcId1 = "";
                    this.dataForm.gcId2 = "";
                    this.dataForm.gcName1 = "";
                    this.dataForm.gcName2 = "";
                    this["goodscalssOption1"] = [];
                    this["goodscalssOption2"] = [];
                }

                // 之前是网络去请求的方式，现在全部获取数据后，前端来处理
                if (arguId == 0) {
                    this.goodscalssOption0 = this.goodscalssOptions;
                } else {
                    this["goodscalssOption" + arguId] = item.children;
                    if (item.children) {
                        // 有子集，说明没有到末级
                        this.ischooseEnd = false;
                    } else {
                        // 有子集，说明到末级了
                        this.ischooseEnd = true;
                    }
                }

                // var obj = {
                //     id:id
                // }
                // getGoodscalss(obj).then((res)=>{
                //     console.log(res)
                //     if(res.code == 200){
                //       var data = res.data;
                //       if(arguId==0){
                //           this["goodscalssOption"+arguId] = data;//[{id:"",gcName:"全部"}].concat(data);
                //       }else{
                //         this["goodscalssOption"+arguId] = data;
                //       }
                //       if( res.data.length==0  || arguId== 3){
                //           this.ischooseEnd = true;
                //       }else{
                //           this.ischooseEnd = false;
                //       }
                //     }
                // })
            },
            changeIschooseEnd() {
                this.ischooseEnd = true;
            },
            nextStep() {
                // console.log(this.dataForm);
                if (!this.ischooseEnd) {
                    this.$message({
                        message: "请选择分类！",
                        type: "warning",
                        duration: 1500,
                    });
                    return;
                }
                var gcId = "";
                var gcName = "";
                var gcId0 = this.dataForm.gcId0;
                var gcId1 = this.dataForm.gcId1;
                var gcId2 = this.dataForm.gcId2;
                if (gcId2) {
                    gcId = gcId2;
                    gcName = this.dataForm.gcName0;
                } else if (gcId1) {
                    gcId = gcId1;
                    gcName = this.dataForm.gcName1;
                } else if (gcId0) {
                    gcId = gcId0;
                    gcName = this.dataForm.gcName2;
                }
                console.log([gcId0, gcId1, gcId2, gcId]);
                if (!gcId2) {
                    this.$message({
                        message: "请选择三级分类",
                        type: "warning",
                        duration: 1500,
                    });
                    return;
                }
                this.dataForm.gcId = gcId;
                this.dataForm.gcName = gcName;
                this.addToChooseHistory();
                this.setChooseHistory();
                this.$emit("showPageNumberFn", 2, this.dataForm);
            },
            // 判断筛选历史中是否存在，不存在添加到数组中
            addToChooseHistory() {
                const index = this.chooseHistory.findIndex(
                    (item) => item.gcId2 === this.dataForm.gcId2
                );
                if (index !== -1) {
                    this.chooseHistory.splice(index, 1);
                    this.chooseHistory.unshift(
                        JSON.parse(JSON.stringify(this.dataForm))
                    );
                } else {
                    this.chooseHistory.unshift(
                        JSON.parse(JSON.stringify(this.dataForm))
                    );
                    this.chooseHistory.length > 20
                        ? (this.chooseHistory.length = 20)
                        : "";
                }
                this.judgeChooseHistoryValue();
            },
            // 将localStorage中的数据取出
            getChooseHistory() {
                this.chooseHistory =
                    JSON.parse(localStorage.getItem("chooseHistory")) || [];
            },
            // 将历史存储于localStorage中
            setChooseHistory() {
                localStorage.setItem(
                    "chooseHistory",
                    JSON.stringify(this.chooseHistory)
                );
            },
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";
    .el-form {
        margin-top: 60px;
        .el-form-item {
            width: 90%;
            margin: auto;
            /deep/ .el-form-item__content {
                display: flex;
                justify-content: space-between;
                margin-left: 0 !important;
                .el-select {
                    width: 23%;
                }
            }
        }
    }
    /deep/ .el-form-item__content::after,
    /deep/ .el-form-item__content::before {
        display: flex !important;
        content: none !important;
    }
    .bottomTip {
        background: url("../../../../assets/images/tipbg.png");
        background-size: 100% 100%;
        background-repeat: no-repeat;
        margin: auto;
        width: 76%;
        height: 70px;
        line-height: 72px;
        padding-left: 20px;
        margin-top: 10px;
    }
    .nextStepWrap {
        margin-top: 30px;
        text-align: center;
        margin-left: 0 !important;
    }
    .box-card {
        margin-top: 30px;
        height: 360px;
        overflow-y: auto;
        .itemGcName {
            cursor: pointer;
            line-height: 30px;
        }
    }
    .el-card__body {
        .ulclass {
            list-style: none;
            padding-left: 0;
            .itemGcName {
                position: relative;
                padding-left: 30px;
                display: flex;
                justify-content: space-between;
                // li {
                //     // display:flex;
                // }
                .iconWarp {
                    position: relative;
                }
            }
        }
    }
    .currentItem {
        color: $--color-primary;
        // background-color: #fffaea;
        // border: 1px solid #ffe696;
        .iconWarp {
            .el-icon-arrow-right {
                color: $--color-primary;
            }
        }
    }

    /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    // ::-webkit-scrollbar
    // {
    //     // width: 6px;  /*滚动条宽度*/
    //     // height: auto;  /*滚动条高度*/
    // }

    /*定义滚动条轨道 内阴影+圆角*/
    // ::-webkit-scrollbar-track
    // {
    //     -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    //     border-radius: 10px;  /*滚动条的背景区域的圆角*/
    //     background-color: #eff0f7;/*滚动条的背景颜色*/
    // }

    // /*定义滑块 内阴影+圆角*/
    // ::-webkit-scrollbar-thumb
    // {
    //     border-radius: 10px;  /*滚动条的圆角*/
    //     -webkit-box-shadow: inset 0 0 6px #ECEDF1;
    //     background-color: #eff0f7;  /*滚动条的背景颜色*/
    // }
    .chooseHistoryList {
        margin-left: 12.5%;
        padding: 20px 0 0 30px;
        span {
            display: block;
            padding: 5px 0 0 102px;
            font-size: 12px;
            color: #808080;
        }
    }
</style>