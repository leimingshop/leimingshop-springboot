<template>
    <el-dialog
        title="目标选择"
        :close-on-click-modal="false"
        :visible.sync="visible"
        width="50%"
        :before-close="closeDialog"
        >
        
        <div>
            <div class="opctionItem">
                <span>曝光</span>
                <div class="checkWarp">
                    <el-checkbox-group v-model="checkList" @change="handleCheckMarketChange">
                        <el-checkbox label="浏览量"></el-checkbox>
                        <el-checkbox label="访客量"></el-checkbox>
                        <el-checkbox label="领取量"></el-checkbox>
                    </el-checkbox-group>
                </div>
            </div>
            <div class="opctionItem">
                <span>下单</span>
                <div  class="checkWarp">
                    <el-checkbox-group v-model="checkList" @change="handleCheckMarketChange">
                        <el-checkbox label="下单订单数"></el-checkbox>
                        <el-checkbox label="下单人数"></el-checkbox>
                        <el-checkbox label="下单使用量"></el-checkbox>
                        <el-checkbox label="下单金额"></el-checkbox>
                    </el-checkbox-group>
                </div>
            </div>
            <div class="opctionItem">
                <span>支付</span>
                <div  class="checkWarp">
                    <el-checkbox-group v-model="checkList" @change="handleCheckMarketChange">
                        <el-checkbox label="支付订单数"></el-checkbox>
                        <el-checkbox label="支付人数"></el-checkbox>
                        <el-checkbox label="支付使用量"></el-checkbox>
                        <el-checkbox label="应收金额"></el-checkbox>
                        <el-checkbox label="实收金额"></el-checkbox>
                        <el-checkbox label="减免金额"></el-checkbox>
                    </el-checkbox-group>
                </div>
            </div>
            <div class="opctionItem">
                <span>曝光</span>
                <div class="checkWarp">
                    <el-checkbox-group v-model="checkList" @change="handleCheckMarketChange">
                        <el-checkbox label="转化率"></el-checkbox>
                    </el-checkbox-group>
                </div>
            </div>
        </div>
        <div slot="footer" class="dialog-footer" style="text-align: center;"> 
	     	<el-button type="primary" @click="submitForm()" :loading="submitLoading">确 定</el-button>
            <el-button @click="dataFormCancel()">取消</el-button>
        </div>
    </el-dialog>

</template>
<script>
export default {
    data(){
       return{
           visible:false,
           loading:false,
           submitLoading:false,
           checkList:[],
        
       } 
    },
    methods:{
        init(){
            this.visible=true;
        },
        handleCheckMarketChange(){
            // let checkedCount = value.length;
        },
        submitForm(){
            if(this.checkList.length==0){
                this.$message.warning("至少选择一项");
                
                return
            }
            this.$emit('choosedAfter',this.checkList);
            this.closeDialog();
        },
        dataFormCancel(){
            this.visible=false;
            this.closeDialog();
        },
        closeDialog(){
            this.visible=false;
        },
    }
}
</script>
<style lang="scss" scoped>
.opctionItem{
     margin-left:20px;
    margin-bottom:30px;
    font-size: 24px;
    &>span{
        font-weight: bold;
    }
    .checkWarp{
        margin-top:10px;
        margin-left: 20px;
    }
}
</style>
