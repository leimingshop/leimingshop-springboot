<template>
    <!--优惠券领取明细-->
    <div>
        <Bread :breaddata="breaddata" :index="'1'" @changePage="changePage"></Bread>
        <el-form
                :inline="true"
                ref="dataForm"
                :model="dataForm"
        >
            <el-form-item label="领取会员名称：">
                <el-input v-model="dataForm.memberName" placeholder="请输入领取会员名称" clearable></el-input>
            </el-form-item>

            <el-form-item label="使用状态：">
                <el-select v-model="dataForm.couponsState">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="未使用" value="0">未使用</el-option>
                    <el-option label="已使用" value="2">已使用</el-option>
                    <el-option label="已过期" value="3">已过期</el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="订单编号：">
                <el-input v-model="dataForm.orderSn" placeholder="请输入订单编号" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" type="primary" @click="getData()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" type="primary" plain @click="reset('dataForm')">重置</el-button>
            </el-form-item>
        </el-form>
        <!--表格-->
        <el-table
                width="100%"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%;maigin-top:20px;"
        >
            <el-table-column prop="activityId" label="领卷ID" align="center" min-width="180"></el-table-column>
            <el-table-column prop="memberName" label="领取会员" align="center" min-width="160"></el-table-column>
            <el-table-column prop="createDate" label="领取时间" align="center" min-width="170">
                <template slot-scope="scope">
                    <div>
                        {{scope.row.createDate}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="useDate" label="使用时间" align="center" min-width="170">
                <template slot-scope="scope">
                    <div>
                        {{scope.row.useDate}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="orderAmount" label="支付金额\订单金额" align="center" min-width="180">
                <template slot-scope="scope">
                    <div v-if="scope.row.goodsAmount">{{scope.row.orderAmount}} \ {{scope.row.goodsAmount}}</div>
                </template>
            </el-table-column>
            <el-table-column prop="couponsState" label="状态" align="center" min-width="170">
                <template slot-scope="scope">
                    <div v-if="scope.row.couponsState==0 || scope.row.couponsState==1">未使用</div>
                    <div v-if="scope.row.couponsState==2">已使用</div>
                    <div v-if="scope.row.couponsState==3">已过期</div>
                </template>
            </el-table-column>
            <el-table-column prop="orderSn" label="订单编号" align="center" min-width="170"></el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
                @size-change="pageSizeChangeHandle"
                @current-change="pageCurrentChangeHandle"
                :current-page="page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="limit"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
    </div>
</template>

<script>
    import Bread from "@/components/bread";
    import mixinViewModule from "@/mixins/view-module";
    export default {
        mixins: [mixinViewModule],
        name: "receiveDetails",
    data(){
            return{
                breaddata: ["营销系统", "优惠券管理", "领取明细"],
                mixinViewModuleOptions: {
                    getDataListURL: '/seller-api/con/coupons/member/page',
                    getDataListIsPage: true,
                    activatedIsNeed:false
                },
                // 查询表单
                dataForm:{
                    memberNam:'',
                    couponsState:'',
                    orderSn:'',
                    activityId:''
                },
                dataList:[],
                dataListLoading: false,
                row:'',
            }
    },
        components:{
            Bread,
        },
        methods:{
            init(row){
                this.row = row ;
                // this.mixinViewModuleOptions.getDataListURL = `/seller-api/con/coupons/member/page?id=${row.id}&`
                this.dataForm.activityId = row.id
                this.getDataList();
            },
            // 查询表格数据
            getData() {
                this.dataForm.activityId = this.row.id ;
                this.page =1;
                this.getDataList();
            },
            // 重置
            reset() {
                this.dataForm.memberName =  '';
                this.dataForm.couponsState = '';
                this.dataForm.orderSn = '';
                this.getDataList();
            },
            changePage(){
                this.$emit('changePage')
            }
        }
    }
</script>

<style scoped>

</style>
