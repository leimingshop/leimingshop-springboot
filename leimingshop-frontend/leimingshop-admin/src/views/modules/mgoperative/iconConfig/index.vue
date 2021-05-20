<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <!-- <el-button type="text" size="small" @click="editConfig()">编辑</el-button> -->
        <div class="formControlArea">
            <div></div>
            <div style="display:flex;">
                <!-- <mainSwitch></mainSwitch> -->
                <mainTipsMessage></mainTipsMessage>
            </div>
        </div>
        <el-alert title="操作提示" type="warning" @close="$store.commit('showListMessage')" v-show="$store.state.listMessageFlag">
            <template slot='title'>
                <div class="iconSize">操作提示：</div>
                <div class="iconSize">1、移动端底部icon可通过此功能进行选择和更换</div>
            </template>
        </el-alert>
        <el-table :data="dataList" v-loading="dataListLoading" border style="width: 100%">
            <el-table-column type="index" prop="$index" align="center" label="序号" width="70">
                <template slot-scope="scope">
                    {{scope.$index+1+(parseInt(page)-1)* parseInt(limit) }}
                </template>
            </el-table-column>
            <el-table-column prop="menuName" label="模块名称">
            </el-table-column>
            <el-table-column prop="selectedIcon" label="选中图标" align="center" width="320">
                <template slot-scope="scope">
                    <img style="width:80px;height:50px;object-fit: contain"
                        :src="scope.row.selectedIcon | filterImgUrl " alt="">
                </template>
            </el-table-column>
            <el-table-column prop="unselectedIcon" align="center" label="未选中图标" width="320">
                <template slot-scope="scope">
                    <img style="width:80px;height:50px;object-fit: contain"
                        :src="scope.row.unselectedIcon | filterImgUrl" alt="">
                </template>
            </el-table-column>
            <el-table-column prop="address" align="center" label="操作" width="140">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editConfig(scope.row)"
                        v-if="$hasPermission('sys:icon:edit')">编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <!-- <el-pagination
	    @size-change="pageSizeChangeHandle"
	    @current-change="pageCurrentChangeHandle"
	    :current-page="page"
	    :page-sizes="[10, 20, 50, 100]"
	    :page-size="limit"
	    :total="total"
	    layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->


        <el-dialog title="编辑图标" :visible.sync="editVisible" :close-on-click-modal="false" class="activiDialog"
            width="40%">
            <el-form :model="editDataForm" :rules="dataRule" ref="editDataForm" label-width="120px">
                <el-form-item label="名称：" prop="menuName">
                    <el-input v-model.trim="editDataForm.menuName" placeholder="请输入5字以内的名称"></el-input>
                </el-form-item>
                <div class="imgConfig">
                    <el-form-item label="上传图片：" :prop="editDataForm.selectedIcon?'unselectedIcon':'selectedIcon'">
                        <div class="imgItem">
                            <el-upload v-loading="selectionloading" style="margin-right:30px;" class="avatar-uploader"
                                action="123" :http-request="upLoad1" :show-file-list="false"
                                :before-upload="beforeAvatarUpload1">
                                <!-- :on-success="handleAvatarSuccess1" -->
                                <img v-if="editDataForm.selectedIcon" :src="editDataForm.selectedIcon | filterImgUrl"
                                    class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                            <span style="width:80px;text-align:center;">选中状态</span>
                        </div>

                        <div class="imgItem">
                            <el-upload v-loading="uncheckedloading" class="avatar-uploader" action="123"
                                :http-request="upLoad2" :show-file-list="false" :before-upload="beforeAvatarUpload2">
                                <img v-if="editDataForm.unselectedIcon"
                                    :src="editDataForm.unselectedIcon | filterImgUrl" class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                            <span style="width:80px;text-align:center;">未选中状态</span>
                        </div>

                    </el-form-item>
                </div>
                <div style="margin-left:120px;width:280px;color:#999;">
                    只能上传jpg/png格式文件，文件不能超过200kb,上传尺寸：80*80px；建议大小：200kb</div>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="noCheck('editDataForm')">取 消</el-button>
                <el-button type="primary" @click="subActivity('editDataForm')" :loading="buttonStatus">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import mixinViewModule from '@/mixins/view-module'
    import {
        iconCinfigList
    } from '@/api/url'
    import {
        uploadPicBase64,
        iconEdit
    } from '@/api/api'
    import Bread from "@/components/bread";
    import {
        getUrlBase64
    } from '@/utils'


    export default {
        mixins: [mixinViewModule],
        components: {
            Bread
        },
        data() {
            return {
                mixinViewModuleOptions: {
                    getDataListURL: iconCinfigList,
                },
                dataListLoading: false,
                dataList: [],
                buttonStatus: false,
                editVisible: false,
                editDataForm: {
                    id: "",
                    menuName: "",
                    selectedIcon: "",
                    unselectedIcon: ""
                },
                breaddata: ["配置管理", "底部icon配置"],
                selectionloading: false,
                uncheckedloading: false,
                tableheight: document.body.offsetHeight - 280,
            }
        },
        computed: {
            dataRule() {
                return {
                    menuName: [{
                        required: true,
                        message: '名称不能为空',
                        trigger: 'blur'
                    }, ],
                    selectedIcon: [{
                        required: true,
                        message: '选中状态不能为空',
                        trigger: 'blur'
                    }, ],
                    unselectedIcon: [{
                        required: true,
                        message: '未选中状态不能为空',
                        trigger: 'blur'
                    }, ]
                }
            }
        },
        watch: {
            'editDataForm.menuName': function (newV, oldV) {
                var chineseCount = 0,
                    characterCount = 0;
                for (let i = 0; i < newV.length; i++) {
                    if (/^[\u4e00-\u9fa5]*$/.test(newV[i])) { //汉字
                        chineseCount = chineseCount + 2;
                    } else { //字符
                        characterCount = characterCount + 1;
                    }
                    var count = chineseCount + characterCount;
                    if (count > 10) { //输入字符大于10的时候过滤
                        this.editDataForm.menuName = newV.substr(0, (chineseCount / 2 + characterCount) - 1)
                    }
                }
            }
        },

        created() {
            this.demo();
        },
        methods: {
            upLoad1(file) {
                const that = this;
                that.getBease64(URL.createObjectURL(file.file), file.file.type, '1')

            },
            upLoad2(file) {
                const that = this;
                console.log('上传')
                that.getBease64(URL.createObjectURL(file.file), file.file.type, '2')
            },
            editConfig(item) {
                this.editVisible = true;
                Object.assign(this.editDataForm, item)
            },
            getBease64(obj, type, who) {
                const that = this;
                var img;
                img = new Image();
                if (who == 1) {
                    this.selectionloading = true;
                } else if (who == 2) {
                    this.uncheckedloading = true;
                }
                img.onload = function () {
                    //       alert(this.width + " " + this.height);
                    let WH = false;
                    if (this.width == 80 && this.height == 80) {
                        WH = true
                    }
                    if (!WH) {
                        that.$message.error('请上传80*80px的图片');
                    } else {
                        getUrlBase64(obj, type, function (base) {
                            uploadPicBase64({
                                "imgStr": base
                            }).then(res => {
                                if (who == 1) {
                                    that.selectionloading = false;
                                } else if (who == 2) {
                                    that.uncheckedloading = false;
                                }
                                console.log(res)
                                if (res.code == 200) {
                                    if (who == '1') {
                                        that.editDataForm.selectedIcon = res.data.url;
                                    } else {
                                        console.log('type==2')
                                        that.editDataForm.unselectedIcon = res.data.url;
                                    }
                                } else {
                                    that.$message.error('上传失败');
                                }
                            })
                        })
                    }
                };
                img.src = obj;
            },
            // handleAvatarSuccess1(res, file) {
            //     const that = this;
            //     that.getBease64(URL.createObjectURL(file.raw),file.raw.type,'1')
            // },
            beforeAvatarUpload1(file) {
                let that = this
                let isJPG = false;
                if (file.type == 'image/jpeg' || file.type == 'image/png') {
                    isJPG = true;
                }


                // const isSize = new Promise(function (resolve, reject) {
                //     let width = 80; // 限制图片尺寸为80X80
                //     let height = 80;
                //     let img = new Image();
                //     img.onload = function () {
                //         let valid = img.width === width && img.height === height;
                //         valid ? resolve() : reject();
                //     }
                //     }).then(() => {
                //     return file;
                //     }, () => {
                //     _this.$message.error('请上传80*80px的图片')
                //     return Promise.reject();
                // });
                console.log(file)
                let isLt2M = file.size / 1024 < 200;
                console.log(file.type, isJPG)
                if (!isJPG) {
                    that.$message.error('上传图标只能是jpg/png格式!');
                }
                if (!isLt2M) {
                    that.$message.error('上传图标大小不能超过 200k!');
                }

                return isJPG && isLt2M

            },
            handleAvatarSuccess2(res, file) {
                const that = this;
                that.getBease64(URL.createObjectURL(file.raw), file.raw.type, '2')
            },
            beforeAvatarUpload2(file) {
                let isJPG = false;
                if (file.type == 'image/jpeg' || file.type == 'image/png') {
                    isJPG = true;
                }
                let isLt2M = file.size / 1024 < 200;
                console.log(file.type, isJPG)
                if (!isJPG) {
                    this.$message.error('上传头像图片只能是jpg/png格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 200k!');
                }
                return isJPG && isLt2M;
            },
            noCheck(formName) {
                this.$refs[formName].resetFields();
                this.editVisible = false;
                this.editDataForm.selectedIcon = '';
                this.editDataForm.unselectedIcon = '';
            },
            subActivity(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        iconEdit(this.editDataForm).then((res) => {
                            console.log('编辑结果', res)
                            if (res.code == 200) {
                                this.$refs[formName].resetFields();
                                this.editVisible = false;
                                this.editDataForm.selectedIcon = '';
                                this.editDataForm.unselectedIcon = '';
                                this.getDataList();
                            } else {
                                this.$message.error(res.msg);
                            }
                        })
                    }
                });
            },



            demo() {
                function placeholderPic() {
                    var w = document.documentElement.offsetWidth;
                    document.documentElement.style.fontSize = w / 20 + 'px';
                }
                placeholderPic();
                window.onresize = function () {
                    placeholderPic();
                }
            },
        }
    };
</script>
<style lang="scss" scoped>
    img {
        object-fit: contain;
    }

    .el-input {
        width: 170px;
        height: 40px;
    }

    .activiDialog {
        .el-input {
            width: 300px;
        }
    }

    .imgConfig {
        width: 100%;

        /deep/.el-form-item__content {
            display: flex;

            .avatar-uploader {
                width: 80px;
                height: 80px;

                .el-upload {
                    border: 1px dashed #d9d9d9;
                    border-radius: 6px;
                    cursor: pointer;
                    position: relative;
                    overflow: hidden;
                }

                .el-upload:hover {
                    border-color: #409EFF;
                }

                .avatar-uploader-icon {
                    font-size: 28px;
                    color: #8c939d;
                    width: 100px;
                    height: 100px;
                    line-height: 100px;
                    text-align: center;
                }

                .avatar {
                    width: 80px;
                    height: 80px;
                    display: block;
                }
            }
        }

        .imgItem {
            display: flex;
            flex-direction: column;
        }
    }
</style>