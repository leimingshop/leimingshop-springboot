<template>
    <div>
        <Bread  :breaddata="breaddata"></Bread>
        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getDataList()" >
            <!-- <el-form-item label="消息标题：">
                <el-input v-model.trim="dataForm.messageTitle" ></el-input>
            </el-form-item> -->
            <el-form-item label="推送方式：">
                <el-select v-model="dataForm.messageSendType" placeholder="请选择"  >
                    <el-option label="全部" value="4"></el-option>
                    <el-option label="站内信" value="0"></el-option>
                    <el-option label="短信" value="1"></el-option>
                    <el-option label="微信" value="2"></el-option>
                    <el-option label="邮件" value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="消息类型：">
                <el-select  v-model="dataForm.messageCode" placeholder="请选择"   :popper-append-to-body="true">
                    <el-option v-for="item in sendCodeoptions" :key="item.code" :label="item.label" :value="item.code">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button  class="btn" type="primary" @click="getDataList()">搜索</el-button>
                <el-button  class="btn" type="primary" plain @click="reset()" >重置</el-button>
            </el-form-item>
        </el-form>
        <!-- <el-button @click="add()" type="primary" style="float: left;margin-bottom: 10px;">推送消息</el-button> -->
        <el-table
                width="100%"
                ref="multipleTable"
                :data="dataList"
                border=""
                v-loading="dataListLoading"
                style="width: 100%;maigin-top:10px;"
                @selection-change="handleSelectionChange"
                @sort-change="sortChange"
                :height="tableheight" 
        >
            <el-table-column type="selection" width="70"></el-table-column>
            <!-- <el-table-column prop="messageTitle" label="消息标题" align="center"></el-table-column> -->
            <el-table-column prop="messageCode" label="消息类型" align="center">
                <template slot-scope="scope">
                     <div v-for="(item,index) in sendCodeoptions "   :key="index" v-if="scope.row.messageCode == item.code" >
                            {{item.label}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="messageSource" label="消息来源" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.messageSource == 0" >雷铭商城</div>
                </template>
            </el-table-column>
             <el-table-column prop="messageSendType" label="推送方式" align="center">
                <template slot-scope="scope">
                    <!-- <div v-if="scope.row.messageSendType == 1" >友盟</div> -->
                    <div v-if="scope.row.messageSendType == '0'" >站内信</div>
                    <div v-else-if="scope.row.messageSendType == '1'" >短信</div>
                    <div v-else-if="scope.row.messageSendType == '2'" >微信</div>
                    <div v-else-if="scope.row.messageSendType == '3'" >邮件</div>
                    <div v-else >全部</div>
                </template>
            </el-table-column>
            <el-table-column prop="sendName" label="发送人" align="center"></el-table-column>
            <el-table-column prop="sendFlag" label="发送状态" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.sendFlag == 0" >未发送</div>
                    <div v-else-if="scope.row.sendFlag == 1" >已发送</div>
                </template>
            </el-table-column>
            <el-table-column prop="sendTime" label="发送时间" align="center"></el-table-column>
            <el-table-column  prop="paramJson" label="消息参数" align="center">
                <template slot-scope="scope">
                    <div style="white-space: nowrap; overflow:hidden; text-overflow:ellipsis; " >{{scope.row.paramJson}}</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200">
                <template slot-scope="scope">
                    <el-button @click.native.prevent="showDetail(scope.row.id)" type="text"size="mini" v-if="$hasPermission('sys:msg:view')">查看</el-button>
                    <el-button @click.native.prevent="deleteList(scope.row.id)" type="text"size="mini" v-if="$hasPermission('sys:msg:delete')">删除</el-button>
                </template>
            </el-table-column>
  
        </el-table>
        <el-dialog title="查看消息" :visible.sync="dialogTableVisible">
            <el-form>
                <el-form-item label="推送方式：">
                    <!-- <span>{{messageDetail.messageSendType == 0?"站内信":messageDetail.messageSendType == 1?"短信":"私信"}}</span> -->
                    <div v-if="messageDetail.messageSendType == 0" >站内信</div>
                    <div v-else-if="messageDetail.messageSendType == 1" >短信</div>
                    <div v-else-if="messageDetail.messageSendType == 2" >微信</div>
                    <div v-else-if="messageDetail.messageSendType == 3" >邮件</div>
                    <div v-else-if="messageDetail.messageSendType == 4" >全部</div>
                </el-form-item>
                <el-form-item label="消息来源：">
                    <div v-if="messageDetail.messageSource == 0" >雷铭商城</div>
                    <!-- <div v-else-if="messageDetail.receiverType == 2" >商户</div> -->
                </el-form-item>
                <el-form-item label="消息类型：">
                    <div v-for="(item,index) in sendCodeoptions "   :key="index" v-if="messageDetail.messageCode == item.code" >{{item.label}}</div>
                </el-form-item>
                <el-form-item label="消息参数：">
                    <div style="overflow: auto;" v-html="messageDetail.paramJson"></div>
                </el-form-item>
            </el-form>

            <!--返回按钮-->
            <div style="margin-top: 40px;margin-bottom: 40px">
                <el-button type="primary" style="display:block;margin:0 auto"   @click="closeDialog">返回</el-button>
            </div>

        </el-dialog>
        <div class="bottomFun">
            <div class="bottomFunLeft">
                <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                <el-button @click="cotrolGoodsShow()" type="primary" style="margin-left: 10px;" v-if="$hasPermission('sys:msg:delete')">批量删除</el-button>
            </div>
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
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import Bread from "@/components/bread";
    import { getmessagepage } from '@/api/url';
    import { deleteMessage,getMessageDetail } from '@/api/api';
    export default {
        mixins: [mixinViewModule],
        data () {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: getmessagepage,
                    getDataListIsPage: true,
                    deleteURL: '',
                    dataListLoading: false,
                    deleteIsBatch: true,
                    deleteIsBatchKey: 'id'
                },
                dialogTableVisible:false,
                breaddata: [ "消息", "消息列表"],
                dataForm: {},
                messageDetail:{},
                value: '',
                multipleSelection:[],
                dataList: [],
                dataListLoading: false,
                checkAll: false,
                options: [],
                tableheight:document.body.offsetHeight-360,
                sendCodeoptions:[
                    {code:'',label:"全部"},
                    {code:0,label:"私信"},
                    {code:1,label:"系统消息"},
                    {code:2,label:"到货通知"},
                    {code:3,label:"退款通知"},
                    // {code:4,label:"私信"},
                    // {code:5,label:"私信"},
                    {code:6,label:"订单发货通知"},
                    // {code:7,label:"私信"},
                    {code:8,label:"秒杀活动即将开始通知"},
                    // {code:9,label:"私信"},
                    // {code:10,label:"私信"},
                    // {code:11,label:"私信"},
                    {code:12,label:"退货审核通过通知"},
                    // {code:13,label:"私信"},
                    {code:14,label:"重置密码验证码消息"},
                    {code:15,label:"退货审核不通过通知"},
                    {code:16,label:"登录注册验证码消息"},
                    // {code:17,label:"私信"},
                    // {code:18,label:"私信"},
                    {code:19,label:"购物车商品降价通知"},
                    {code:20,label:"库存预警通知"},
                    {code:21,label:"购物车商品库存不足通知"},
                    {code:22,label:"订单支付成功通知"},
                    {code:23,label:"虚拟订单支付成功通知"},
                    {code:24,label:"订单超时取消通知"},
                ],
            }
        },
        components: {
            Bread
        },
        created () {
            this.getDataList();
        },
        methods: {
            sortChange(val){
                if(val.order == "descending") this.dataForm.descOrAsc = 0;
                else if(val.order == "ascending") this.dataForm.descOrAsc = 1;
                this.getDataList();
            },
            add(){
                this.$emit("add");
            },
            showDetail(id){
                this.dialogTableVisible = true;
                let that = this;
                getMessageDetail({id:id}).then((res)=>{
                    if(res.code == 200){
                        that.messageDetail = res.data;
                    }
                })
            },
            reset() {
                this.dataForm.messageSendType = "";
                this.dataForm.messageTitle = "";
                this.dataForm.messageCode = "";
                this.getDataList();
            },
            cotrolGoodsShow(){
                var ids = this.getIds();
                var obj = {
                    data:ids,
                }
                this.$confirm('是否删除所选数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteMessage(obj).then((res)=>{
                        if(res.code==200){
                            this.getDataList();
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                    })

                }).catch(() => {});
            },
            deleteList(id){
                var ids= [];
                ids.push(id);
                var obj = {
                    data:ids,
                }
                this.$confirm('是否删除当前数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteMessage(obj).then((res)=>{
                        if(res.code==200){
                            this.getDataList();
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                    })

                }).catch(() => {});
            },
            getIds(){
                var ids= [];
                this.multipleSelection.forEach((item,index)=>{
                    if("object" == typeof(item)){
                        ids.push(item.id);
                    }else{
                        ids.push(id);
                    }
                })
                return ids;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                if(this.multipleSelection.length == this.dataList.length) this.checkAll = true;
                else this.checkAll = false;
            },
            handleCheckAllChange(val) {
                if(val) this.$refs.multipleTable.toggleAllSelection();
                else this.$refs.multipleTable.clearSelection();
            },
            /*关闭弹窗*/
            closeDialog() {
                this.dialogTableVisible = false;
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";
    .grayLine{
        border-bottom: 0!important;
    }
    .bottomFun {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .bottomFunLeft {
            width: 450px;
            display: flex;
            align-items: center;
        }
    }
</style>
