<template>
    <div>
        <div id="control-area">
        <Bread :breaddata="breaddata"></Bread>
        <el-form :inline="true" class="grayLine topGapPadding" :model="dataForm" @keyup.enter.native="getData()">
            <el-form-item label="标题：">
                <el-input v-model="dataForm.articleTitle" placeholder="标题" clearable></el-input>
            </el-form-item>
            <el-form-item label="文章所属：">
                <el-input v-model="dataForm.articleBelong" placeholder="文章所属" clearable></el-input>
            </el-form-item>
            <el-form-item label="作者：">
                <el-input v-model="dataForm.author" placeholder="作者" clearable></el-input>
            </el-form-item>
            <el-form-item label="来源：">
                <el-input v-model="dataForm.articleSource" placeholder="来源" clearable></el-input>
            </el-form-item>
            <el-form-item label="文章分类：">
                <el-cascader v-model="acId" :options="acOption" @change="caeChange" :props="defaultParams"
                    change-on-select :show-all-levels="false" clearable></el-cascader>
            </el-form-item>
            <el-form-item label="是否置顶：">
                <el-select v-model="dataForm.topFlag" placeholder="请选择">
                    <el-option v-for="item in topFlagList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否头条：">
                <el-select v-model="dataForm.headFlag" placeholder="请选择">
                    <el-option v-for="item in topFlagList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否显示：">
                <el-select v-model="dataForm.status" placeholder="请选择">
                    <el-option v-for="item in statusList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否允许评论：">
                <el-select v-model="dataForm.commentFlag" placeholder="请选择">
                    <el-option v-for="item in commentFlagList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="审核状态：">
                <el-select v-model="dataForm.audit" placeholder="请选择">
                    <el-option v-for="item in auditList" :key="item.id" :label="item.sgName" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button calss="btn" type="primary" @click="getData()">查询</el-button>
                <el-button calss="btn" @click="reset()" type="primary" plain>重置</el-button>
            </el-form-item>
        </el-form>

        <el-form>
            <div class="formControlArea">
            <el-form-item style="disply:block;margin-bottom:0px !important;">
                <el-button type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
                <el-button type="danger" plain @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
            </el-form-item>
                <div style="display:flex;">
                    <mainSwitch></mainSwitch>
                    <mainTipsMessage></mainTipsMessage>
                </div>
            </div>
        </el-form>
        <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
                <template slot='title'>
                    <div class="iconSize">操作提示：</div>
                    <div class="iconSize">1、新增资讯文章选择的文章栏目需先创建资讯分类否则无法创建文章</div>
                    <div class="iconSize">2、新增的资讯文章默认为显示，如不想立即显示可选择隐藏</div>
                    <div class="iconSize">3、评论默认为允许，如新增的文章不想被评论可禁止评论</div>
                </template>
            </el-alert>
        </div>
        <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle"
            style="width: 100%;" 
            :height="tableHeight"
        >
            <!-- :height="!$store.state.mainSwitch ? tableheight:tableheightBig" -->
            <el-table-column type="index" prop="$index" label="序号" align="center" width="70">
                <template slot-scope="scope">{{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}</template>
            </el-table-column>
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column prop="sort" label="排序" header-align="center" align="center" width="70"></el-table-column>
            <el-table-column prop="articleTitle" label="标题" header-align="center" align="center"></el-table-column>
            <el-table-column prop="articleBelong" label="文章所属" header-align="center" align="center" width="120">
            </el-table-column>
            <el-table-column prop="author" label="作者" header-align="center" align="center" width="120">
            </el-table-column>
            <el-table-column prop="articleSource" label="来源" header-align="center" align="center" width="120">
            </el-table-column>
            <el-table-column prop="acName" label="文章分类" header-align="center" align="center" width="120">
            </el-table-column>
            <!--<el-table-column prop="articlePraiseNum" label="点赞数" header-align="center" align="center" width="80"></el-table-column>-->
            <!--<el-table-column prop="articleCommentNum" label="评论数" header-align="center" align="center" width="80"></el-table-column>-->
            <el-table-column prop="updateDate" label="更新时间" header-align="center" align="center" width="160">
            </el-table-column>
            <el-table-column prop="topFlag" label="是否置顶" header-align="center" align="center" width="80">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.topFlag==0">否</el-tag>
                    <el-tag type="success" v-else>是</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="headFlag" label="是否头条" header-align="center" align="center" width="80">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.headFlag==0">否</el-tag>
                    <el-tag type="success" v-else>是</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="是否显示" header-align="center" align="center" width="100">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.status==0">隐藏</el-tag>
                    <el-tag type="success" v-else>显示</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="commentFlag" label="是否允许评论" header-align="center" align="center" width="110">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.commentFlag==0">禁止</el-tag>
                    <el-tag type="success" v-else>允许</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="audit" label="审核状态" header-align="center" align="center" width="100">
                <template slot-scope="scope">
                    <el-tag type="info" v-if="scope.row.audit==0">未审核</el-tag>
                    <el-tag type="success" v-else-if="scope.row.audit==1">审核通过</el-tag>
                    <el-tag type="danger" v-else>审核驳回</el-tag>
                </template>
            </el-table-column>
            <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="250">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}
                    </el-button>
                    <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}
                    </el-button>
                    <el-button type="text" size="small" v-if="scope.row.audit==1"
                        @click="statusUpdate(scope.row.id,1,scope.row.topFlag == 0 ? 1 : 0)">
                        {{scope.row.topFlag == 0 ? '设为置顶' : '取消置顶'}}</el-button>
                    <el-button type="text" size="small" v-if="scope.row.audit==1"
                        @click="statusUpdate(scope.row.id,2,scope.row.headFlag == 0 ? 1 : 0)">
                        {{scope.row.headFlag == 0 ? '设为头条' : '取消头条'}}</el-button>
                    <el-button type="text" size="small" v-if="scope.row.audit==1"
                        @click="recommendArticle(scope.row.id)">编辑相关推荐</el-button>
                    <el-button type="text" size="small"
                        @click="articleAudit(scope.row.id,scope.row.audit == 0 ? 0 : 1)">
                        {{scope.row.audit == 0 ? '审核' : '审核结果'}}</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page="page" :page-sizes="[10, 20, 50, 100]" :page-size="limit" :total="total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="pageSizeChangeHandle"
            @current-change="pageCurrentChangeHandle"></el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
        <!-- 弹窗, 审核 / 审核结果 -->
        <InformationAudit v-if="informationAuditVisible" ref="informationAudit" @refreshDataList="getDataList">
        </InformationAudit>
    </div>
</template>

<script>
    import mixinViewModule from "@/mixins/view-module";
    import AddOrUpdate from "./information-add-or-update";
    import InformationAudit from "./informationAudit";
    import Bread from "@/components/bread";
    import {
        informationPageUrl,
        deleteInformationUrl,
        statusUpdateInformationUrl,
        informationClassTreeUrl
    } from '@/api/url'
    export default {
        mixins: [mixinViewModule],
        data() {
            return {
                informationAuditVisible: false,
                mixinViewModuleOptions: {
                    getDataListURL: informationPageUrl,
                    getDataListIsPage: true,
                    deleteURL: deleteInformationUrl,
                    deleteIsBatch: true
                },
                acId: [],
                acOption: [],
                defaultParams: {
                    label: 'acName',
                    value: 'id',
                    children: 'children'
                },
                dataForm: {
                    articleTitle: "", //文章标题
                    author: '',
                    acId: [], //栏目ID
                    topFlag: '',
                    headFlag: '',
                    commentFlag: '',
                    status: '',
                    audit: '',
                },
                breaddata: ["运营管理", "资讯管理", "资讯文章"],
                topFlagList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 0,
                        sgName: '否'
                    },
                    {
                        id: 1,
                        sgName: '是'
                    }
                ],
                statusList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 0,
                        sgName: '隐藏'
                    },
                    {
                        id: 1,
                        sgName: '显示'
                    }
                ],
                commentFlagList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 0,
                        sgName: '禁止'
                    },
                    {
                        id: 1,
                        sgName: '允许'
                    }
                ],
                auditList: [{
                        id: '',
                        sgName: '全部'
                    },
                    {
                        id: 0,
                        sgName: '未审核'
                    },
                    {
                        id: 1,
                        sgName: '审核通过'
                    },
                    {
                        id: 2,
                        sgName: '审核驳回'
                    }
                ],
                // tableheight: 'auto',
                tableHeight:null,
                tableheightBig: 0,
            };
        },
        components: {
            AddOrUpdate,
            InformationAudit,
            Bread,
        },
        created() {
            // 加载栏目分类数据
            this.classList();
        },
        watch:{
            '$store.state.mainSwitch'(){ //监听vuex中列表放大的Flag重新计算列表高度
                setTimeout(()=>{
                    const height =
                        document.body.clientHeight -
                        document.getElementById("control-area").clientHeight -
                        60;
                    this.tableheightBig = height > 300 ? height : 300;
                },100)
            }
        },
        mounted(){
            window.onresize = function(){
                this.tableHeight="";
            }.bind(this)
        },
        methods: {
            getData() {
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            reset() {
                this.dataForm = {};
                this.acId = []; //分类ID（显示）
                this.page = 1;
                this.limit = 10;
                this.getDataList();
            },
            //文章分类集合
            classList() {
                this.$http.get(informationClassTreeUrl).then((res) => {
                    if (res.data.code == 200) {
                        // 将获得到的数据赋值给options
                        this.acOption = res.data.data;
                    }
                });
            },
            //改变acId参数格式
            caeChange(item) {
                this.dataForm.acId = item[item.length - 1];
                this.dataForm.acId = this.dataForm.acId.toString();
            },
            //文章状态修改
            statusUpdate(id, type, code) {
                this.$confirm(`确定进行此次操作？`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    center: true
                }).then(() => {
                    var obj = {};
                    obj.id = id;
                    obj.type = type;
                    obj.code = code;
                    this.$http.put(statusUpdateInformationUrl, obj).then(res => {
                        if (res.data.code == 200) {
                            this.$message.success({
                                message: "操作成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => {
                                    this.getData();
                                }
                            });
                        } else {
                            this.$message.info(res.data.msg);
                        }
                    });
                });
            },
            //编辑相关推荐
            recommendArticle(id) {
                this.$router.push({
                    name: "cms-information-recommendList",
                    params: {
                        id: id
                    }
                })
            },


            //审核
            articleAudit(id, auditCode) {
                this.informationAuditVisible = true
                this.$nextTick(() => {
                    this.$refs.informationAudit.dataForm.id = id
                    this.$refs.informationAudit.auditDataForm.auditCode = auditCode
                })
            },




        }
    };
</script>