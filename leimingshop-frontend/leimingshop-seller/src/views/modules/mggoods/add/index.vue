<template>
    <div v-loading="loading">
        <Bread
            :breaddata="breaddata"
            v-show="goodsFreightTemplatePage != true"
        ></Bread>
        <div class="formControlArea">
            <div>
                <h2
                    style="margin-top: 40px"
                    v-show="goodsFreightTemplatePage != true"
                >
                    {{ $route.query.gcId ? "编辑商品" : "发布商品" }}
                </h2>
            </div>
            <div style="display: flex">
                <mainSwitch></mainSwitch>
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert
            title="操作提示"
            type="warning"
            @close="$store.commit('showListMessage')"
            v-show="$store.state.listMessageFlag"
        >
            <template slot="title">
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">
                    1、商品类目仅显示该店铺支持销售的商品类目
                </div>
                <div class="iconSize">2、商品类目需选择到末级（第三级）</div>
                <div class="iconSize">
                    3、商品类目区分实体商品和虚拟商品，虚拟商品类目会在三级类目进行说明
                </div>
            </template>
        </el-alert>

        <myProgress
            :showPageNumber="showPageNumber"
            v-show="goodsFreightTemplatePage != true"
        ></myProgress>

        <chooseKind
            v-show="showPageNumber == 1"
            @showPageNumberFn="showPageNumberFn"
        ></chooseKind>

        <edit
            v-if="showPageNumber == 2"
            ref="editCompon"
            @showPageNumberFn="showPageNumberFn"
        ></edit>

        <finnal
            v-if="showPageNumber == 3"
            ref="finnalCompon"
            @showPageNumberFn="showPageNumberFn"
        ></finnal>
    </div>
</template>

<script>
    import myProgress from "./myProgress.vue";
    import chooseKind from "./chooseKind.vue";
    import edit from "./edit.vue";
    import finnal from "./finnal.vue";
    import Bread from "@/components/bread";
    import { backScanDetailGoods } from "@/api/api";
    export default {
        data() {
            return {
                loading: false,
                breaddata: ["商品", "商品管理", "商品添加"],
                showPageNumber: 1,
                goodsFreightTemplatePage: false,
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
                    storeGoodsClass: [],
                },
            };
        },
        components: {
            myProgress,
            chooseKind,
            edit,
            finnal,
            Bread,
        },
        watch: {
            // 监听路由，如果是商品编辑，需要跳转到商品编辑页面
            "$route.fullPath": function (newVal, oldVal) {
                console.log(newVal, oldVal);
                if (/mggoods-add/.test(newVal)) {
                    if (this.$route.query.gcId) {
                        this.getEditData();
                        this.breaddata = ["商品", "商品管理", "商品编辑"];
                    } else {
                        this.showPageNumberFn(1);
                        this.breaddata = ["商品", "商品管理", "商品添加"];
                    }
                }
            },
        },
        created() {
            //  防止刷新
            if (this.$route.query.gcId) {
                this.getEditData();
                this.breaddata = ["商品", "商品管理", "商品编辑"];
            } else {
                this.breaddata = ["商品", "商品管理", "商品添加"];
            }
        },
        mounted() {
            window.that = this;
            document.documentElement.scrollTop = 0;
        },
        methods: {
            getList() {
                this.$router.push({
                    name: "mggoods-goods-list",
                });
            },
            // 如果是编辑商品
            getEditData() {
                var obj = {
                    //  params:{
                    id: this.$route.query.gcId,
                    //  }
                };
                console.log(obj);
                this.loading = true;
                backScanDetailGoods(obj).then((res) => {
                    this.loading = false;
                    if (res.code == 200 && res.data.id) {
                        console.log("商品编辑回显数据");

                        Object.assign(this.dataForm, res.data);
                        this.dataForm.invoiceFlag = res.data.invoiceFlag
                            ? res.data.invoiceFlag + ""
                            : "0";
                        this.dataForm.invoiceType = res.data.invoiceType
                            ? res.data.invoiceType.split(",")
                            : [];
                        this.dataForm.invoiceContent = res.data.invoiceContent
                            ? res.data.invoiceContent.split(",")
                            : [];
                        console.log(this.dataForm.invoiceType);
                        console.log(this.dataForm.invoiceContent);
                        this.dataForm.gcId0 = res.data.firstGcId
                            ? res.data.firstGcId
                            : "";
                        this.dataForm.gcId1 = res.data.secondGcId
                            ? res.data.secondGcId
                            : "";
                        this.dataForm.gcId2 = res.data.thirdGcId
                            ? res.data.thirdGcId
                            : "";

                        this.dataForm.gcName0 = res.data.firstGcName
                            ? res.data.firstGcName
                            : "";
                        this.dataForm.gcName1 = res.data.secondGcName
                            ? res.data.secondGcName
                            : "";
                        this.dataForm.gcName2 = res.data.thirdGcName
                            ? res.data.thirdGcName
                            : "";
                        this.dataForm.storeGoodsClass[0] = this.dataForm.firstStoreGoodsGcId;
                        this.dataForm.storeGoodsClass[1] = this.dataForm.secondStoreGoodsGcId;

                        if (this.dataForm.gcId2) {
                            this.dataForm.gcId = this.dataForm.gcId2;
                            this.dataForm.gcName = this.dataForm.gcName2;
                        } else if (this.dataForm.gcId1) {
                            this.dataForm.gcId = this.dataForm.gcId1;
                            this.dataForm.gcName = this.dataForm.gcName1;
                        } else {
                            this.dataForm.gcId = this.dataForm.gcId0;
                            this.dataForm.gcName = this.dataForm.gcName0;
                        }

                        this.showPageNumberFn(2, this.dataForm);
                    } else if (!res.data.id) {
                        this.$message.error("后端根据id查询商品详情接口有问题");
                    }
                });
            },
            // 跳转页面
            showPageNumberFn(num, dataForm, goodsFreightTemplatePage) {
                this.goodsFreightTemplatePage = goodsFreightTemplatePage;
                // 如果是编辑，需要把分类传过去
                if (this.goodsFreightTemplatePage != true) {
                    this.showPageNumber = num;
                    this.$nextTick(() => {
                        if (num == 2 && dataForm) {
                            this.$refs.editCompon.init(dataForm);
                        }
                    });
                }
            },
        },
    };
</script>
