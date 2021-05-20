<template>
    <div v-loading="dataLoading">
        <Bread :breaddata="breaddata"></Bread>
        <div class="formControlArea">
            <div></div>
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
                    1、以一天为一期秒杀活动，每期秒杀活动由平台自行设置场次。以30分钟的间隔确定场次的开始时间，每期最多可设置48场。
                </div>
                <div class="incoSize">
                    2、平台开启秒杀活动后，会自动生成5期秒杀活动，以后每天新增一期秒杀活动，可修改预设秒杀期数范围（0-10）天，0为只可添加当天的秒杀活动，10为可设置10天内的秒杀活动。
                </div>
                <div class="incoSize">
                    3、每场秒杀活动默认持续24小时，到时自动结束。
                </div>
                <div class="incoSize">
                    4、如果修改每日场次设置，已生成场次不会发生变化，只会影响即将生成的场次。
                </div>
            </template>
        </el-alert>
        <el-form
            :model="ruleForm"
            :rules="rules"
            ref="ruleForm"
            label-width="130px"
            class="demo-ruleForm artinputcontent"
        >
            <el-form-item label="场次时间设置：" prop="activityStartDate">
                <div class="raceTime">
                    <div
                        class="raceTimeItem"
                        style="cursor: pointer"
                        v-for="(item, index) in allTimsOptions"
                        @click="chooseRaceTime(item)"
                    >
                        <i
                            class="el-icon-check raceTimeHook"
                            v-if="
                                ruleForm.activityStartDate.indexOf(item) != -1
                            "
                        ></i>
                        <span> {{ item }}</span>
                    </div>
                </div>
                <!-- ruleForm.activityStartDate -->
                <!-- <el-input v-model="ruleForm.activityStartDate" label-width="102px"></el-input> -->
                <div class="operationTips">
                    以一天为一期秒杀活动，每期秒杀活动由平台自行设置场次。以30分钟的间隔确定场次的开始时间，每期最多可设置48场，点击选中状态的场次为取消该场次
                </div>
            </el-form-item>
            <!--<el-form-item label="秒杀审核：" prop="activityStartDate ">-->
            <!--<el-input v-model="ruleForm.activityStartDate "></el-input>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="销售价格：" prop="activityStartDate ">-->
            <!--<el-input v-model="ruleForm.activityStartDate "></el-input>-->
            <!--</el-form-item>-->
            <el-form-item label="秒杀预设天数：" prop="presetDays">
                <el-input-number
                    v-model="ruleForm.presetDays"
                    controls-position="right"
                    :min="0"
                    :max="10"
                    :step="1"
                    step-strictly
                    onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                    type="number"
                ></el-input-number
                >&nbsp;天
                <div class="operationTips">
                    默认为5天，可修改范围为0-10天，设置后商家可添加规定预设时间内的秒杀活动
                </div>
            </el-form-item>
            <el-form-item label="秒杀活动有效期：" prop="activityEffectiveTime">
                <el-input-number
                    v-model="ruleForm.activityEffectiveTime"
                    controls-position="right"
                    :min="1"
                    :max="48"
                    onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                    type="number"
                ></el-input-number
                >&nbsp;小时
                <div class="operationTips">
                    默认为24小时，可修改范围为1-48小时，优先级低于商家单独设置的有效期时间
                </div>
            </el-form-item>
            <el-form-item label="秒杀支付有效期：" prop="payEffectiveTime">
                <el-input-number
                    v-model="ruleForm.payEffectiveTime"
                    controls-position="right"
                    :min="1"
                    :max="1440"
                    onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                    type="number"
                ></el-input-number
                >&nbsp;分钟
                <div class="operationTips">
                    默认为5分钟，可修改时间范围为优先级高于平台规定的自动取消时间，时间范围：1-1440分钟
                </div>
            </el-form-item>
            <el-form-item label="秒杀预约提醒：" prop="reminderTime">
                <el-input-number
                    v-model="ruleForm.reminderTime"
                    controls-position="right"
                    :min="1"
                    :max="60"
                    onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"
                    type="number"
                ></el-input-number
                >&nbsp;分钟
                <div class="operationTips">
                    默认为5分钟，可修改范围为1-60分钟，前台用户设置秒杀提醒后会提前5分钟提醒用户参与秒杀
                </div>
            </el-form-item>
            <el-form-item prop="advImageUrl" label="秒杀广告：">
                <div style="width: 500px; display: flex">
                    <img-cropper
                        ref="advImageUrl"
                        :index="'1'"
                        :imgWidth="'100px'"
                        :imgHeight="'100px'"
                        :cropImg="ruleForm.advImageUrl"
                        @GiftUrlHandle="GiftUrlHandle"
                    >
                    </img-cropper>
                    <el-button
                        type="text"
                        @click="deleteAdvImage"
                        v-if="ruleForm.advImageUrl"
                        style="margin-left: 10px"
                        >删除
                    </el-button>
                </div>
                <div class="operationTips">
                    添加后该广告图显示在秒杀默认列表页上方显示该图，填写广告链接点击图片可跳转到指定链接页面
                </div>
            </el-form-item>
            <el-form-item label="广告链接（URL）：" prop="advUrl">
                <el-input
                    v-model="ruleForm.advUrl"
                    @blur="urlChange"
                    style="width: 400px"
                    placeholder="Http://xxxxxx.com"
                ></el-input>
            </el-form-item>

            <el-form-item>
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                    v-if="$hasPermission('sys:setting:seckill')"
                >
                    保存秒杀设置
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import Bread from "@/components/bread";
    import {
        saveSeckillSetting,
        getSeckillSetting,
        uploadPicBase64,
    } from "@/api/api";
    import { isURL } from "@/utils/validate";
    import imgCropper from "@/components/upload/model-photo-cropper2";

    export default {
        data() {
            return {
                breaddata: ["营销管理", "秒杀管理", "秒杀设置"],
                dataLoading: false,
                subStatus: true,
                ruleForm: {
                    activityStartDate: [], // 场次时间设置
                    presetDays: 5, // 秒杀预设天数
                    activityEffectiveTime: 24, // 秒杀活动有效期
                    payEffectiveTime: 5, // 秒杀支付有效期
                    reminderTime: 5, // 秒杀预约提醒
                    advImageUrl: "", // 秒杀广告图片url
                    advUrl: "", // 秒杀广告url
                },
                allTimsOptions: [
                    "00:00",
                    "00:30",
                    "01:00",
                    "01:30",
                    "02:00",
                    "02:30",
                    "03:00",
                    "03:30",
                    "04:00",
                    "04:30",
                    "05:00",
                    "05:30",
                    "06:00",
                    "06:30",
                    "07:00",
                    "07:30",
                    "08:00",
                    "08:30",
                    "09:00",
                    "09:30",
                    "10:00",
                    "10:30",
                    "11:00",
                    "11:30",
                    "12:00",
                    "12:30",
                    "13:00",
                    "13:30",
                    "14:00",
                    "14:30",
                    "15:00",
                    "15:30",
                    "16:00",
                    "16:30",
                    "17:00",
                    "17:30",
                    "18:00",
                    "18:30",
                    "19:00",
                    "19:30",
                    "20:00",
                    "20:30",
                    "21:00",
                    "21:30",
                    "22:00",
                    "22:30",
                    "23:00",
                    "23:30",
                ],
                rules: {
                    activityStartDate: [
                        {
                            required: true,
                            message: "请选择场次时间",
                            trigger: "blur",
                        },
                    ],
                    presetDays: [
                        {
                            required: true,
                            message: "请输入秒杀预设天数",
                            trigger: "blur",
                        },
                    ],
                    activityEffectiveTime: [
                        {
                            required: true,
                            message: "请输入秒杀活动有效期",
                            trigger: "blur",
                        },
                    ],
                    payEffectiveTime: [
                        {
                            required: true,
                            message: "请输入秒杀支付有效期",
                            trigger: "blur",
                        },
                    ],
                    reminderTime: [
                        {
                            required: true,
                            message: "请输入秒杀预约提醒",
                            trigger: "blur",
                        },
                    ],
                },
            };
        },
        components: {
            Bread,
            imgCropper,
        },
        created() {
            // 获取秒杀设置
            this.getSeckillSetting();
        },
        methods: {
            // 图片上传
            GiftUrlHandle(imgUrl) {
                let that = this;
                let url = imgUrl;
                that.ruleForm.advImageUrl = url;
            },
            // 删除广告图片
            deleteAdvImage() {
                this.ruleForm.advImageUrl = "";
            },
            urlChange(e) {
                if (e.target.value && !isURL(e.target.value)) {
                    this.subStatus = false;
                    this.$message({
                        message: "链接格式不正确",
                        type: "error",
                        duration: 1500,
                    });
                } else {
                    this.subStatus = true;
                }
            },

            // 保存秒杀设置
            submitForm(formName) {
                this.$confirm("修改后的秒杀设置会在第二天(24:00:00)生效", "提示", {
                    cancelButtonText: "取消",
                    confirmButtonText: "确定",
                    type: "warning",
                })
                    .then(() => {
                        if (!this.subStatus) {
                            this.$message({
                                message: "链接地址格式有问题",
                                type: "error",
                                duration: 1500,
                            });
                            return;
                        }
                        this.$refs[formName].validate((valid) => {
                            if (valid) {
                                const obj = {
                                    activityStartDate: this.ruleForm
                                        .activityStartDate,
                                    presetDays: this.ruleForm.presetDays,
                                    activityEffectiveTime: this.ruleForm
                                        .activityEffectiveTime,
                                    payEffectiveTime: this.ruleForm
                                        .payEffectiveTime,
                                    reminderTime: this.ruleForm.reminderTime,
                                    advImageUrl: this.ruleForm.advImageUrl,
                                    advUrl: this.ruleForm.advUrl,
                                };
                                // 保存秒杀设置
                                this.dataLoading = true;
                                saveSeckillSetting(obj).then((res) => {
                                    this.dataLoading = false;
                                    if (res.code == 200) {
                                        this.$message({
                                            message: res.msg,
                                            type: "success",
                                            duration: 1500,
                                        });
                                        this.getSeckillSetting();
                                    }
                                });
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: "warning",
                                    duration: 1500,
                                });
                            }
                        });
                    })
                    .catch(() => {});
            },
            // 获取秒杀后台设置
            getSeckillSetting() {
                this.dataLoading = true;
                getSeckillSetting().then((res) => {
                    this.dataLoading = false;
                    if (res.data) {
                        this.ruleForm.activityStartDate =
                            res.data.activityStartDate;
                        this.ruleForm.advUrl = res.data.advUrl;
                        this.ruleForm.advImageUrl = res.data.advImageUrl;
                        if (res.data.presetDays) {
                            this.ruleForm.presetDays = res.data.presetDays;
                        }
                        if (res.data.activityEffectiveTime) {
                            this.ruleForm.activityEffectiveTime =
                                res.data.activityEffectiveTime;
                        }
                        if (res.data.payEffectiveTime) {
                            this.ruleForm.payEffectiveTime =
                                res.data.payEffectiveTime;
                        }
                        if (res.data.reminderTime) {
                            this.ruleForm.reminderTime = res.data.reminderTime;
                        }
                    }
                });
            },
            // 选择时间
            chooseRaceTime(itemArgu) {
                if (!this.ruleForm.activityStartDate) {
                    this.ruleForm.activityStartDate = [];
                }
                var _index = this.ruleForm.activityStartDate.indexOf(itemArgu);
                if (_index != -1) {
                    this.ruleForm.activityStartDate.splice(_index, 1);
                } else {
                    this.ruleForm.activityStartDate.push(itemArgu);
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    .operationTips {
        color: #999999;
        font-size: 12px;
    }

    .raceTime {
        display: flex;
        border-top: 1px solid grey;
        border-left: 1px solid grey;
        flex-wrap: wrap;
        width: 897px;
        text-align: center;
        .raceTimeItem {
            border-bottom: 1px solid grey;
            border-right: 1px solid grey;
            min-width: 56px;
            height: 50px;
            box-sizing: border-box;
            position: relative;

            span {
            }
            .raceTimeHook {
                color: #2260d2;
                font-size: 40px;
                position: absolute;
                left: 8px;
                opacity: 0.7;
            }
        }
    }
</style>
