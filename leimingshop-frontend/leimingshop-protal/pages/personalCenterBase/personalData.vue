<template>
    <div class="person">
        <div class="personTop">
            <p class="on">基本信息</p>
            <!-- <p class="li" @click="$router.push('/interested')">兴趣分类</p> -->
        </div>
        <div class="personalContent">
            <div class="personalContent-head">
                <p type="primary" class="personalContent-head-p">修改头像</p>
                <div class="personalContent-top">
                    <img
                        :src="dataForm.memberAvatar | filterImgUrl"
                        @click="modal = true"
                        style="width: 80px; height: 80px"
                    />
                </div>
                <!-- <div class="shadow" v-show="modal"> -->
                <Modal v-model="modal" title="设置头像">
                    <div class="onContent" v-if="isFileImg">
                        <Upload
                            :max-size="50 * 1024"
                            :format="['jpg', 'png']"
                            :before-upload="handleUpload"
                            :on-exceeded-size="maxSize"
                            accept="image/*"
                            action=""
                        >
                            <div @on-ok="uploadeImg" class="onContent-picture">
                                <Icon
                                    type="ios-cloud-upload-outline"
                                    size="40"
                                    style="color: #0491ff"
                                ></Icon>
                                <p>选择上传图片</p>
                            </div>
                            <!-- <Button icon="ios-cloud-upload-outline" class="onContent-picture"
                                @on-ok="uploadeImg">选择上传图片</Button> -->
                            <!-- <div v-if="file !== null">Upload file: {{ file.name }}</div>  -->
                        </Upload>
                        <p class="onContent-picture-p">
                            只能上传jpg/png格式文件 文件不能超过50KB
                        </p>
                    </div>

                    <div class="onContent" v-if="!isFileImg">
                        <img
                            :src="baseFile | filterImgUrl"
                            alt=""
                            style="width: 200px; height: 200px"
                        />
                    </div>
                    <div v-if="!isFileImg" class="onContent-photo">
                        <p @click="uploadAgain">重新上传</p>
                    </div>

                    <p
                        slot="header"
                        style="
                            font-size: 16px;
                            color: #333333;
                            text-align: center;
                        "
                    >
                        <span>设置头像</span>
                    </p>
                    <div slot="footer">
                        <Button
                            type="error"
                            v-if="!isFileImg"
                            size="large"
                            long
                            :loading="loading"
                            @click="cancel"
                            class="footer-btn"
                            >取消</Button
                        >
                        <Button
                            type="error"
                            size="large"
                            long
                            :loading="loading"
                            @click="uploadeImg"
                            class="finsh"
                            :disabled="fileImgOver"
                            >确定</Button
                        >
                    </div>
                </Modal>
                <!-- </div> -->
            </div>
            <div>
                <div class="personalStyle">
                    <p>用户名：</p>
                    <i-Input
                        v-model="dataForm.memberName"
                        style="border: none !important"
                        class="change"
                        disabled
                        >{{ dataForm.memberName }}</i-Input
                    >
                </div>
                <div class="personalStyle">
                    <p>昵称：</p>
                    <i-Input
                        placeholder="输入昵称"
                        v-model="dataForm.nickName"
                        :maxlength="10"
                        minlength="2"
                        >{{ dataForm.nickName }}</i-Input
                    >
                </div>

                <div class="personalStyle">
                    <p>性别：</p>
                    <RadioGroup
                        on-change="bindPickerChange"
                        v-model="dataForm.memberSex"
                    >
                        <Radio
                            v-for="item in sexArr"
                            :label="item.value"
                            :key="item.value"
                        >
                            <span>{{ item.label }}</span>
                        </Radio>
                    </RadioGroup>
                </div>
                <div class="personalStyle">
                    <p>生日：</p>
                    <Row>
                        <i-Col span="12">
                            <!-- <DatePicker :value="value1" format="yyyy-MM-dd" type="date" placeholder="选择出生日期" v-model="dataForm.memberBirthday" @on-change="binkTime"></DatePicker> -->
                            <DatePicker
                                :value="value1"
                                format="yyyy-MM-dd"
                                type="date"
                                placeholder="选择出生日期"
                                :options="options1"
                                v-model="dataForm.memberBirthday"
                            ></DatePicker>
                        </i-Col>
                    </Row>
                </div>
                <div class="personalStyle">
                    <p>城市：</p>
                    <!-- <Cascader v-model="dataForm.memberAreainfo" :data="areaListOne" :load-data="loadData" @on-change="regionChange" placeholder="请选择省/市/区"></Cascader> -->
                    <!-- @on-change="onChangeAAA" -->

                    <Cascader
                        :data="addressData"
                        v-model="dataForm.areaList"
                        :load-data="loadData"
                        style="width: 348px; height: 34px"
                        placeholder="请选择省/市/区"
                    ></Cascader>
                </div>
            </div>
            <div class="personalBtn" @click="submit">保存</div>
        </div>
        <!-- {{dataForm.areaList}} -->
    </div>
</template>
<script>
    import {
        AddressUpdata,
        AddAddress,
        AreaList,
    } from "@/api/api_04.shoppingCar.js";
    import { homeMember, infoBase } from "@/api/api_06-08-01personalData.js";
    import { uploadPicBase64 } from "@/api/api_Common.js";
    import { getAddressThreelist } from "@/api/api_09login.js";
    import {
        getAddressSaving, //新地址增加
        getThreelistId, //获取四级地址通过id
        putAddressModification, //会员地址修改
        deleteAddressDeletion, //会员地址删除
        setDefaultAddress, //设置默认地址
    } from "@/api/api_06personalCenter.js";
    // import cities from '@/utils/area.js';
    export default {
        head() {
            return {
                title: "个人信息",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        data() {
            return {
                addressData: [],
                date: "",
                isFileImg: true,
                loading: false,
                modal_loading: false,
                modal: false,
                file: null,
                imgStr: "",
                options1: {
                    disabledDate(date) {
                        return date && date.valueOf() >= new Date();
                    },
                },
                sexArr: [
                    {
                        label: "保密",
                        value: 0,
                    },
                    {
                        label: "女",
                        value: 1,
                    },
                    {
                        label: "男",
                        value: 2,
                    },
                ],
                dataForm: {
                    memberName: "",
                    nickName: "",
                    memberBirthday: "",
                    memberSex: "",
                    memberAvatar: "",
                    memberAreainfo: "", //地区
                },
                //regionList:JSON.parse(localStorage.getItem("addressThree")),
                formTopSubmit: {
                    areaInfo: "河北省 邯郸市 涉县",
                    address: "ddd", //详细地址 string
                    zipCode: "056400", //邮政编码
                    provinceId: "130000", //省份ID
                    cityId: "130100", //城市ID
                    areaId: "130102", //县级ID
                    stressId: "130102001", //乡镇ID
                },
                formTopSubmit1: {
                    //编辑地址用的
                    areaInfo: "河北省 邯郸市 涉县",
                    address: "ddd", //详细地址 string
                    zipCode: "056400", //邮政编码
                    provinceId: "130000", //省份ID
                    cityId: "130100", //城市ID
                    areaId: "130102", //县级ID
                    stressId: "130102001", //乡镇ID
                    defaultFlag: 0, //新增不用加
                    id: "", //增加新的店铺就不能加
                    memberId: "", //增加新的店铺就不能加
                },
                // uploadImg:'',
                baseFile: "",
                picName: "",
                selDate: "",
                value1: "",
                // value:'',
                fileImgOver: true, //判断是否超出50kb
            };
        },
        mounted() {
            //一级一级加载
            getThreelistId({
                id: 0,
            })
                .then((res) => {
                    if (res.data && res.data.code == 200) {
                        res.data.data.forEach((item, index) => {
                            this.addressData.push({
                                value: item.id,
                                label: item.areaName,
                                children: [],
                                loading: false,
                            });
                        });
                    }
                })
                .catch((err) => {});
        },
        created() {
            this.homeMember();
        },

        methods: {
            loadData(item, callback) {
                item.loading = true;
                getThreelistId({
                    id: item.value,
                })
                    .then((res) => {
                        if (res.data && res.data.code == 200) {
                            item.children = [];
                            res.data.data.forEach((item2, index) => {
                                if (item2.count == 0) {
                                    item.children.push({
                                        value: item2.id,
                                        label: item2.areaName,
                                    });
                                } else {
                                    item.children.push({
                                        value: item2.id,
                                        label: item2.areaName,
                                        children: [],
                                        loading: false,
                                    });
                                }
                            });
                            item.loading = false;
                            callback();
                        }
                    })
                    .catch((err) => {});
            },

            // binkTime() {
            //     this.dataForm.memberBirthday = this.date
            //     console.log(this.dataForm.memberBirthday)
            // },
            maxSize(file) {
                // console.log(file)
                this.$Message.warning(
                    `图片【${file.name}】大小超出50kb，请重新上传`
                );
            },
            //处理图片转换成base64
            handleUpload(file) {
                //    this.dataForm.memberAvatar = file.name
                let reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = (e) => {
                    this.baseFile = e.target.result;
                    this.picName = file.name;
                    this.isFileImg = !this.isFileImg;
                    if (file.size > 51200) {
                        this.fileImgOver = true;
                        this.$Message.info("图片过大");
                    } else {
                        this.fileImgOver = false;
                    }
                };
            },
            //上传图片
            uploadeImg() {
                this.loading = true;
                uploadPicBase64({
                    imgStr: this.baseFile,
                    pictureName: this.picName,
                })
                    .then((res) => {
                        console.log(res);
                        if (res.code == 200) {
                            console.log(res.data.url);
                            // var rrr ='http://img.b2b2c.leimingtech.com'
                            this.dataForm.memberAvatar = res.data.url;
                            this.loading = false;
                            this.modal = false;
                        }
                    })
                    .catch((err) => console.log(err));
                return false;
            },
            uploadAgain() {
                this.isFileImg = true;
            },
            cancel() {
                // this.$Message.error('取消提交发票抬头');
                this.modal = false;
            },

            handelAddressName() {
                //处理areaInfo和城市id 提交信息
                var demo = document.getElementsByClassName("ivu-cascader-label");
                var string = demo[0].innerText;
                var arr = string.split(" / ");
                var str = "";
                arr.forEach((item, index) => {
                    str += `${item} `;
                });
                return str;
                // if (this.receivingAddress == 1) {
                //     this.formTopSubmit.memberProvinceid  = this.dataForm.memberAreainfo[0]; //省份ID
                //     this.formTopSubmit.memberCityid  = this.dataForm.memberAreainfo[1]; //城市ID
                //     this.formTopSubmit.memberAreaid  = this.dataForm.memberAreainfo[2]; //县级ID
                //     this.formTopSubmit.stressId = this.dataForm.memberAreainfo[3]; //乡级ID
                //     this.formTopSubmit.memberAreainfo = str; //省份、城市、镇
                //     this.formTopSubmit.address = this.dataForm.address; //省份、城市、镇
                //     this.formTopSubmit.zipCode = this.dataForm.zipCode; //邮政编码
                // } else if (this.receivingAddress == 2) {
                //     this.formTopSubmit1.memberProvinceid = this.dataForm.memberAreainfo[0]; //省份ID
                //     this.formTopSubmit1.memberCityid = this.dataForm.memberAreainfo[1]; //城市ID
                //     this.formTopSubmit1.memberAreaid = this.dataForm.memberAreainfo[2]; //县级ID
                //     this.formTopSubmit1.stressId = this.dataForm.memberAreainfo[3]; //乡级ID
                //     this.formTopSubmit1.memberAreainfo = str; //省份、城市、镇
                //     this.formTopSubmit1.address = this.dataForm.address; //省份、城市、镇
                // }
            },
            //修改地址
            regionChange(e) {
                this.adressForm.provinceId = e[0];
                this.adressForm.cityId = e[1];
                this.adressForm.areaId = e[2];
                if (e.length == 3) {
                    this.adressForm.stressId = "";
                } else {
                    this.adressForm.stressId = e[3];
                }
            },

            //一级一级的调地区接口
            getAreaChildren(id) {
                let a = [];
                AreaList({
                    id: id,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.childrenList = res.data.map((item) => {
                                if (item.count != 0) {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                        children: [],
                                        loading: false,
                                    };
                                } else {
                                    return {
                                        value: item.id,
                                        label: item.areaName,
                                    };
                                }
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //通过省市县街道ID获取对应的省市县街道lable
            demoFn(arr, data, city = []) {
                if (typeof data === "object") {
                    for (let i = 0; arr[i] !== undefined; i++) {
                        for (let j = 0; data[j] !== undefined; j++) {
                            if (arr[i] === data[j].value) {
                                city.push(data[j].label);
                            }
                        }
                    }
                    for (let i = 0; data[i] !== undefined; i++) {
                        this.jn(arr, data[i].children, city);
                    }
                }
                this.adressForm.memberAreainfo = city.join(" ");
            },
            //获取个人信息
            homeMember(id) {
                var params = {
                    id: id,
                };
                homeMember(params)
                    .then((res) => {
                        if (res.code == 200) {
                            this.dataForm = res.data;
                            this.dataForm.memberName = res.data.memberName;
                            this.dataForm.memberAvatar = res.data.memberAvatar;
                            this.dataForm.nickName = res.data.nickName;
                            this.dataForm.memberSex = res.data.memberSex;
                            this.dataForm.memberBirthday = res.data.memberBirthday;
                            this.dataForm.memberAreainfo = res.data.memberAreainfo;
                            this.dataForm.areaList = [
                                this.dataForm.memberProvinceid,
                                this.dataForm.memberCityid,
                                this.dataForm.memberAreaid,
                                this.dataForm.stressId,
                            ];
                            // setTimeout(() => {
                            //     this.regionList = cities
                            // }, 2000)
                        } else {
                            this.$api.msg(res.msg);
                        }
                    })
                    .catch((err) => console.log(err));
            },

            //提交
            submit() {
                let time = "";
                if (
                    this.dataForm.memberBirthday != null &&
                    this.dataForm.memberBirthday != ""
                ) {
                    var date = new Date(this.dataForm.memberBirthday);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    m = m < 10 ? "0" + m : m;
                    var d = date.getDate();
                    d = d < 10 ? "0" + d : d;
                    var h = date.getHours();
                    var minute = date.getMinutes();
                    minute = minute < 10 ? "0" + minute : minute;
                    time = y + "-" + m + "-" + d + " " + "00" + ":00" + ":00"; //这里如果不需要小时 分  后边的可以不需要拼接
                    // if (valid){
                    //     this.demoFn(this.memberAreainfo,this.areaListOne)
                    //     this.loading = true;
                    //     let newObj = JSON.parse(JSON.stringify(this.memberAreainfo));
                    //     newObj.memberAreainfo = JSON.stringify(this.memberAreainfo);

                    // }
                }
                console.log(this.dataForm.areaList);

                var str = this.handelAddressName();
                var obj = {
                    memberAvatar: this.dataForm.memberAvatar,
                    nickName: this.dataForm.nickName,
                    memberBirthday: time,
                    memberSex: this.dataForm.memberSex,
                    memberAreainfo: str, //城市内容
                    memberProvinceid: this.dataForm.areaList[0], //省份ID
                    memberCityid: this.dataForm.areaList[1], //城市ID
                    memberAreaid: this.dataForm.areaList[2], //地区ID
                    // stressId: this.dataForm.areaList[3],//街道Id
                };
                if (this.dataForm.areaList[3]) {
                    obj.stressId = this.dataForm.areaList[3];
                }
                infoBase(obj)
                    .then((res) => {
                        if (res.code == 200) {
                            this.$Message.success(res.msg);
                        }
                    })
                    .catch((err) => console.log(err));
                // this.homeMember()
            },
        },
    };
</script>
<style lang="scss" scoped>
    .person {
        width: 948px;
        height: 1200px;
        background: #ffffff;
    }

    .personTop {
        display: flex;
        background: #ffffff;
        border-bottom: 1px solid #e8e8e8;

        .on {
            // width: 120px;
            height: 44px;
            font-size: 14px;
            color: #333333;
            padding-left: 40px;
            line-height: 44px;
            font-weight: 600;
        }

        // .li{
        //     width: 120px;
        //     height: 50px;
        //     font-size: 18px;
        //     line-height: 50px;
        //     text-align: center;
        //     border: 1px solid #999999;
        // }
    }

    .personalContent {
        padding: 60px 0 0 267px;

        .personalContent-head {
            display: flex;
            margin: 0 0 30px;

            .personalContent-head-p {
                width: 74px;
                min-width: 74px;
                height: 80px;
                line-height: 80px;
                font-size: 14px;
                font-weight: 400;
                color: #222222;
                font-family: PingFangSC-Regular, PingFang SC;
            }

            .personalContent-top {
                width: 80px;
                height: 80px;
                margin: 0 0 0 19px;
                background: rgba(216, 216, 216, 1);
                border-radius: 50%;
                overflow: hidden;
            }
        }
    }

    .personalStyle {
        margin: 0 0 20px;
        display: flex;

        p {
            width: 56px;
            min-width: 56px;
            height: 34px;
            line-height: 34px;
            font-size: 14px;
            margin: 0 20px 0 0;
            color: #222222;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            text-align: right;
        }
    }

    .personalBtn {
        width: 120px;
        height: 44px;
        margin: 29px 100px 0;
        font-size: 15px;
        color: #ffffff;
        font-weight: 500;
        font-family: PingFangSC-Medium, PingFang SC;
        background: linear-gradient(270deg, #f04e36 0%, #dd2619 100%);
        text-align: center;
        padding: 10px;
        border-radius: 25px;
        cursor: pointer;
    }

    .onContent {
        width: 200px;
        height: 200px;
        margin: 0 auto;
        overflow: hidden;
        border-radius: 6px;
    }

    .onContent-picture {
        width: 200px;
        height: 200px;
        background: #f5f5f5;
        font-size: 12px;
        color: #0491ff;
        padding: 48px 0 0;
        border-radius: 6px;
    }

    .onContent-picture-p {
        width: 142px;
        font-size: 12px;
        color: #999999;
        bottom: 142px;
        left: 70px;
        z-index: 999;
        position: absolute;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
    }

    .footer-btn {
        width: 60px;
        height: 30px;
        font-size: 14px;
        font-weight: 400;
        color: #333333;
        font-family: PingFangSC-regular, PingFang SC;
        background: rgba(245, 245, 245, 1) !important;
        border-radius: 2px;
        border: none;
    }

    .onContent-photo {
        // width: 64px;
        // height: 14px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        // padding:16px 386px 0 110px;
        // margin: 16px 0 0 111px;
        color: #dd2619;
        position: relative;
        bottom: 30px;
    }
    .shadow {
        position: relative;
        // top: 40%;
        left: -10%;
        -webkit-transform: translate(-50%, -50%);
        transform: translate(-50%, -50%);
        width: 302px;
        height: 389px;
        background: rgba(0, 0, 0, 0.1401);
        border-radius: 12px;
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-pack: center;
        -ms-flex-pack: center;
        justify-content: center;
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
    }
    .finsh {
        width: 60px;
        height: 30px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        background: linear-gradient(90deg, #dd291c 0%, #ff4e02 100%);
        color: #ffffff;
    }

    /deep/ .person-center-right[data-v-4fc7d81c] {
        height: 1450px;
    }

    /deep/ .ivu-form-item-content {
        width: 348px;
        height: 48px;
        display: flex;
    }

    /deep/ .ivu-input {
        width: 338px;
        height: 34px;
        padding: 4px 12px;
        font-size: 12px !important;
        border-radius: 2px !important;
        color: #333333;
        // border: 1px solid #cccccc !important;
    }

    /deep/ .ivu-radio-group {
        line-height: 34px;
    }

    /deep/ .ivu-select-single .ivu-select-selection {
        width: 348px;
        height: 48px;
    }

    /deep/ .ivu-select-single .ivu-select-selection .ivu-select-placeholder,
    .ivu-select-single .ivu-select-selection .ivu-select-selected-value {
        height: 48px;
        line-height: 48px;
    }

    /deep/ .ivu-select .ivu-select-dropdown {
        width: 348px;
    }

    /deep/ .ivu-input-prefix i,
    .ivu-input-suffix i {
        line-height: 48px;
    }

    /deep/ .ivu-input-suffix {
        line-height: 34px;
    }

    /deep/ .ivu-modal-content {
        width: 280px;
        height: 367px;
    }

    /deep/ .ivu-btn ivu-btn-default {
        width: 171px;
        height: 51px;
        border-radius: 6px;
        background: rgba(245, 245, 245, 1);
    }

    /deep/ .ivu-modal-header {
        border-bottom: 1px solid #ffffff;
        padding: 31px 16px 20px !important;
    }

    /deep/ .ivu-modal-footer {
        border-top: 1px solid #ffffff;
        padding: 0px !important;
        margin: 30px 0 40px !important;
    }

    /deep/ .ivu-modal-wrap * {
        text-align: center;
    }
    /deep/ .ivu-radio {
        span.ivu-radio + * {
            margin-right: 30px;
        }
    }
    /deep/ .ivu-modal-body {
        padding: 0px !important;
        height: 200px;
    }
    /deep/ .ivu-modal {
        width: 280px !important;
        // margin: 25px 44% !important;
    }
    /deep/ .ivu-modal-footer button + button {
        margin-left: 26px !important;
    }
    // /deep/ .ivu-btn-error{
    //     background: linear-gradient(90deg, #DD291C 0%, #FF4E02 100%);
    // }
    /deep/ .ivu-cascader-arrow {
        right: 20px !important;
    }
</style>
