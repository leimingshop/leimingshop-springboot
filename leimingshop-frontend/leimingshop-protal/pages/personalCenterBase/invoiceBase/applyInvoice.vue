<template>
    <div class="invoice">
        <div class="invoiContent">
            <div
                class="invoiContent-top"
                v-show="
                    invoiceForm.changeFlag == 0 &&
                    invoiceForm.invoiceEvolve == 2 &&
                    invoiceForm.storeInvoiceType == 2
                "
            >
                <span class="word"
                    >确认换开后，请尽快将纸质发票寄回以下地址：</span
                >
                <div class="invoiContent-tx">
                    <img :src="'/img/06.personalCenter/signal@2x.png'" />
                    <span>收件人：{{ changForm.addressee }}</span>
                </div>
                <div class="invoiContent-tx">
                    <img :src="'/img/06.personalCenter/phone@2x.png'" />
                    <span>联系电话：{{ changForm.addresseePhone }}</span>
                </div>
                <div class="invoiContent-tx">
                    <img :src="'/img/06.personalCenter/shdz@2x.png'" />
                    <span
                        >地址：{{
                            changForm.mailingAddress +
                            "" +
                            changForm.detailedAddress
                        }}</span
                    >
                </div>
            </div>
        </div>
        <div class="personal">
            <div class="personalWord">
                <div
                    style="
                        width: 3px;
                        height: 14px;
                        margin-right: 3px;
                        border: 1px solid #dd2619;
                        background: #dd2619;
                    "
                ></div>
                <p>发票信息</p>
            </div>
            <div class="personal-one">
                <div class="personal-oneList">
                    <p>订单编号</p>
                    <p style="margin-left: 31px">{{ invoiceForm.orderSn }}</p>
                </div>
                <div class="receipt_mold">
                    <div class="commenTitle">
                        <p>发票类型</p>
                    </div>
                    <div
                        class="mold_item"
                        @click="receiptMoldFn(item.key, index)"
                        v-for="(item, index) in moldList"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.memberInvoiceType,
                        }"
                        :key="index"
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.memberInvoiceType"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
                <div class="receipt_type" style="margin: 0 0 22px 0">
                    <div class="commenTitle">发票抬头</div>
                    <div
                        class="commentDiv"
                        @click="receiptTypeFn(index)"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.companyType,
                        }"
                        v-for="(item, index) in typeList"
                        :key="index"
                        v-if="
                            receiptForm.memberInvoiceType === 3 && item.key == 1
                                ? false
                                : true
                        "
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.companyType"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
                <div class="receipt_type">
                    <div class="commenTitle">发票内容</div>
                    <div
                        class="commentDiv"
                        @click="changeMemberInvoiceContent(item)"
                        :class="{
                            mold_itemActive:
                                item.key == receiptForm.memberInvoiceContent,
                        }"
                        v-for="(item, index) in contentTypeList"
                        :key="index"
                        v-if="
                            receiptForm.memberInvoiceType === 3 && item.key == 2? false: true"
                    >
                        {{ item.name }}
                        <img
                            v-if="item.key == receiptForm.memberInvoiceContent"
                            :src="'/img/04.shoppingCar/coupon3.svg'"
                        />
                    </div>
                </div>
            </div>
        </div>
        <div>
            <div class="commenBox">
                <!-- 电子个人 -->
                <div
                    v-if="receiptForm.memberInvoiceType === 1 &&
                        receiptForm.companyType === 1">
                    <div
                        style="display: flex;
                            margin: 0 0 20px 0;
                            line-height: 16px;"
                    >
                        <div
                            style="
                                width: 3px;
                                height: 14px;
                                margin-right: 3px;
                                border: 1px solid #dd2619;
                                background: #dd2619;
                            "
                        ></div>
                        <div class="commenBox-title">收件信息</div>
                    </div>
                    <Form
                        class="receiptForm1"
                        ref="receiptForm"
                        :model="receiptForm"
                        :label-colon="false"
                        :rules="receiptRule"
                    >
                        <FormItem label="个人名称" prop="personalName">
                            <Input
                                v-model="receiptForm.personalName"
                                clearable
                                maxlength="12"
                                placeholder="请输入个人名称"
                            />
                        </FormItem>
                        <FormItem label="收票人手机" prop="addresseePhone">
                            <Input
                                v-model="receiptForm.addresseePhone"
                                clearable
                                maxlength="11"
                                placeholder="请填写收票人手机"
                            />
                        </FormItem>
                        <FormItem label="收票人邮箱" prop="addresseeMail">
                            <Input
                                v-model="receiptForm.addresseeMail"
                                clearable
                                maxlength="25"
                                placeholder="请填写收票人邮箱"
                            />
                        </FormItem>
                    </Form>
                </div>

                <!-- 普通个人 -->
                <div
                    v-if="
                        receiptForm.memberInvoiceType === 2 &&
                        receiptForm.companyType === 1"
                >
                    <div
                        style="
                            display: flex;
                            margin: 0 0 20px 0;
                            line-height: 16px;
                        "
                    >
                        <div
                            style="
                                width: 3px;
                                height: 14px;
                                margin-right: 3px;
                                border: 1px solid #dd2619;
                                background: #dd2619;
                            "
                        ></div>
                        <div class="commenBox-title">收件信息</div>
                    </div>
                    <Form
                        class="receiptForm1"
                        ref="receiptForm"
                        :model="receiptForm"
                        :label-colon="false"
                        :rules="receiptRule"
                    >
                        <FormItem label="个人名称" prop="personalName">
                            <Input
                                v-model="receiptForm.personalName"
                                clearable
                                maxlength="12"
                                placeholder="请输入个人名称"
                            />
                        </FormItem>
                        <FormItem label="收票人手机" prop="addresseePhone">
                            <Input
                                v-model="receiptForm.addresseePhone"
                                clearable
                                maxlength="11"
                                placeholder="请填写收票人手机"
                            />
                        </FormItem>
                        <FormItem label="所在地区" prop="regions">
                            <Cascader
                                :data="areaListOne"
                                :load-data="loadData"
                                @on-change="regionChangeOtner"
                                v-model="receiptForm.regions"
                                placeholder="请选择省/市/区"
                            ></Cascader>
                        </FormItem>
                        <FormItem label="详细地址" prop="detailedAddress">
                            <Input
                                v-model="receiptForm.detailedAddress"
                                placeholder="请输入详细地址"
                            />
                        </FormItem>
                    </Form>
                </div>

                <!-- 普通单位类型的发票 -->
                <div
                    v-if="
                        receiptForm.memberInvoiceType === 2 &&
                        receiptForm.companyType === 2 &&
                        receiptForm.memberInvoiceType !== 3
                    "
                >
                    <Form
                        ref="receiptForm"
                        :model="receiptForm"
                        :label-colon="false"
                        :rules="receiptRule"
                    >
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">资质信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="单位名称"
                            prop="company"
                        >
                            <Input
                                v-if="invoiceList.length == 0"
                                v-model="receiptForm.company"
                                clearable
                                maxlength="40"
                                placeholder="请输入单位名称，内容在40字以内"
                            />
                            <Select
                                v-else
                                v-model="receiptForm.id"
                                filterable
                                allow-create
                                @on-create="handleCreate1"
                                @on-change="changeInvoice($event)"
                                style="width: 328px"
                                placeholder="请输入单位名称，内容在40字以内"
                            >
                                <Option
                                    v-for="(invoiceitem,
                                    invoiceindex) in invoiceList"
                                    :value="invoiceitem.id"
                                    :key="invoiceindex"
                                    >{{ invoiceitem.company }}</Option
                                >
                            </Select>
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="税号"
                            prop="dutyParagraph"
                        >
                            <Input
                                v-model="receiptForm.dutyParagraph"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位税号"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="注册地址">
                            <Input
                                v-model="receiptForm.registeredAddress"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位注册地址"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="办公电话">
                            <Input
                                v-model="receiptForm.officePhone"
                                type="number"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位办公电话"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="开户银行">
                            <Input
                                v-model="receiptForm.bankName"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位开户银行"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="开户银行账号"
                            prop="bankNumber"
                        >
                            <Input
                                v-model="receiptForm.bankNumber"
                                clearable
                                maxlength="19"
                                placeholder="请填写单位开户银行账号"
                            />
                        </FormItem>
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">收件信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="个人名称"
                            prop="personalName"
                        >
                            <Input
                                v-model="receiptForm.personalName"
                                clearable
                                maxlength="12"
                                placeholder="请输入个人名称"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="收票人手机"
                            prop="addresseePhone"
                        >
                            <Input
                                v-model="receiptForm.addresseePhone"
                                clearable
                                maxlength="11"
                                placeholder="请填写收票人手机"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="所在地区"
                            prop="regions"
                        >
                            <Cascader
                                :data="areaListOne"
                                :load-data="loadData"
                                @on-change="regionChangeOtner"
                                v-model="receiptForm.regions"
                                placeholder="请选择省/市/区"
                            ></Cascader>
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="详细地址"
                            prop="detailedAddress"
                        >
                            <Input
                                v-model="receiptForm.detailedAddress"
                                placeholder="请输入详细地址"
                            />
                        </FormItem>
                    </Form>
                </div>

                <!-- 电子单位类型的发票 -->
                <div
                    v-if="
                        receiptForm.memberInvoiceType === 1 &&
                        receiptForm.companyType === 2 &&
                        receiptForm.memberInvoiceType !== 3
                    "
                >
                    <Form
                        ref="receiptForm"
                        :model="receiptForm"
                        :label-colon="false"
                        :rules="receiptRule"
                    >
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">资质信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="单位名称"
                            prop="company"
                        >
                            <Input
                                v-if="invoiceList.length == 0"
                                v-model="receiptForm.company"
                                clearable
                                maxlength="12"
                                placeholder="请输入单位名称，内容在40字以内"
                            />
                            <Select
                                v-else
                                v-model="receiptForm.id"
                                filterable
                                allow-create
                                @on-create="handleCreate1"
                                @on-change="changeInvoice($event)"
                                style="width: 328px"
                                placeholder="请输入单位名称，内容在40字以内"
                            >
                                <Option
                                    v-for="(invoiceitem,
                                    invoiceindex) in invoiceList"
                                    :value="invoiceitem.id"
                                    :key="invoiceindex"
                                    >{{ invoiceitem.company }}</Option
                                >
                            </Select>
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="税号"
                            prop="dutyParagraph"
                        >
                            <Input
                                v-model="receiptForm.dutyParagraph"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位税号"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="注册地址">
                            <Input
                                v-model="receiptForm.registeredAddress"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位注册地址"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="办公电话">
                            <Input
                                v-model="receiptForm.officePhone"
                                type="number"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位办公电话"
                            />
                        </FormItem>
                        <FormItem class="receiptForm1" label="开户银行">
                            <Input
                                v-model="receiptForm.bankName"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位开户银行"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="开户银行账号"
                            prop="bankNumber"
                        >
                            <Input
                                v-model="receiptForm.bankNumber"
                                clearable
                                maxlength="19"
                                placeholder="请填写单位开户银行账号"
                            />
                        </FormItem>
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">收件信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="个人名称"
                            prop="personalName"
                        >
                            <Input
                                v-model="receiptForm.personalName"
                                clearable
                                maxlength="12"
                                placeholder="请输入个人名称"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="收票人手机"
                            prop="addresseePhone"
                        >
                            <Input
                                v-model="receiptForm.addresseePhone"
                                clearable
                                maxlength="11"
                                placeholder="请填写收票人手机"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="收票人邮箱"
                            prop="addresseeMail"
                        >
                            <Input
                                v-model="receiptForm.addresseeMail"
                                clearable
                                maxlength="25"
                                placeholder="请填写收票人邮箱"
                            />
                        </FormItem>
                    </Form>
                </div>

                <!-- 增值税类型的发票 -->
                <div
                    v-if="
                        receiptForm.companyType === 2 &&
                        receiptForm.memberInvoiceType == 3
                    "
                >
                    <Form
                        ref="receiptForm"
                        :model="receiptForm"
                        :label-colon="false"
                        :rules="receiptRule"
                    >
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">资质信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="单位名称"
                            prop="company"
                        >
                            <Input
                                v-if="invoiceList.length == 0"
                                v-model="receiptForm.company"
                                clearable
                                maxlength="12"
                                placeholder="请输入单位名称，内容在40字以内"
                            />
                            <Select
                                v-else
                                v-model="receiptForm.id"
                                filterable
                                allow-create
                                @on-create="handleCreate1"
                                @on-change="changeInvoice($event)"
                                style="width: 328px"
                                placeholder="请输入单位名称，内容在40字以内"
                            >
                                <Option
                                    v-for="(invoiceitem,
                                    invoiceindex) in invoiceList"
                                    :value="invoiceitem.id"
                                    :key="invoiceindex"
                                    >{{ invoiceitem.company }}</Option
                                >
                            </Select>
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="税号"
                            prop="dutyParagraph"
                        >
                            <Input
                                v-model="receiptForm.dutyParagraph"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位税号"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="注册地址"
                            prop="registeredAddress"
                        >
                            <Input
                                v-model="receiptForm.registeredAddress"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位注册地址"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="办公电话"
                            prop="officePhone"
                        >
                            <Input
                                v-model="receiptForm.officePhone"
                                type="number"
                                clearable
                                maxlength="20"
                                placeholder="请填写单位办公电话"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="开户银行"
                            prop="bankName"
                        >
                            <Input
                                v-model="receiptForm.bankName"
                                clearable
                                maxlength="25"
                                placeholder="请填写单位开户银行"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="开户银行账号"
                            prop="bankNumber"
                        >
                            <Input
                                v-model="receiptForm.bankNumber"
                                clearable
                                maxlength="19"
                                placeholder="请填写单位开户银行账号"
                            />
                        </FormItem>
                        <div
                            style="
                                display: flex;
                                margin: 0 0 20px 0;
                                line-height: 16px;
                            "
                        >
                            <div
                                style="
                                    width: 3px;
                                    height: 14px;
                                    margin-right: 3px;
                                    border: 1px solid #dd2619;
                                    background: #dd2619;
                                "
                            ></div>
                            <div class="commenBox-title">收件信息</div>
                        </div>
                        <FormItem
                            class="receiptForm1"
                            label="个人名称"
                            prop="personalName"
                        >
                            <Input
                                v-model="receiptForm.personalName"
                                clearable
                                maxlength="12"
                                placeholder="请输入个人名称"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="收票人手机"
                            prop="addresseePhone"
                        >
                            <Input
                                v-model="receiptForm.addresseePhone"
                                clearable
                                maxlength="11"
                                placeholder="请填写收票人手机"
                            />
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="所在地区"
                            prop="regions"
                        >
                            <Cascader
                                :data="areaListOne"
                                :load-data="loadData"
                                @on-change="regionChangeOtner"
                                v-model="receiptForm.regions"
                                placeholder="请选择省/市/区"
                            ></Cascader>
                        </FormItem>
                        <FormItem
                            class="receiptForm1"
                            label="详细地址"
                            prop="detailedAddress"
                        >
                            <Input
                                v-model="receiptForm.detailedAddress"
                                placeholder="请输入详细地址"
                            />
                        </FormItem>
                    </Form>
                </div>
            </div>
            <div
                style="
                    width: 100%;
                    justify-content: space-between;
                    margin: 0 146px 42px;
                "
                v-show="receiptForm.companyType != 1"
            >
                <Checkbox v-model="setStatus">设为默认发票抬头</Checkbox>
                <p
                    style="
                        font-size: 12px;
                        font-family: PingFangSC-Regular, PingFang SC;
                        font-weight: 400;
                        color: rgba(221, 38, 25, 1);
                        margin: 9px 0;
                    "
                >
                    注：发票金额为实际支付金额，不包括优惠券、满减活动及虚拟资产等抵消金额
                </p>
            </div>
            <div slot="footer" class="footerBtn">
                <Button
                    size="default"
                    class="newBtn"
                    style="background: #f5f5f5"
                    @click="$router.push({ path: '/personalCenterBase/invoiceBase/useInvoice' })"
                    >取消</Button
                >
                <Button
                    class="newBtn"
                    style="
                        margin: 0 0 0 38px;
                        background: rgba(221, 38, 25, 1);
                        color: rgba(255, 255, 255, 1);
                    "
                    size="default"
                    :loading="modal_loading"
                    @click="receiptSubmit('receiptForm')"
                    >保存</Button
                >
            </div>
        </div>
        <!-- <electronicPersonal></electronicPersonal> -->
    </div>
</template>
<script>
    // import electronicPersonal from "@/components/06.personalCenter/06-13.invoice/electronicPersonal.vue"
    import { AreaList, InvoiceList } from "@/api/api_04.shoppingCar.js";

    import {
        getInvoice,
        invoiceApply,
        changeInvoiceApply,
        orderInvoice,
        invoiceInfo,
        invoiceCheck,
    } from "@/api/api_06-13-06invoiceTitle.js";

    export default {
        head() {
            return {
                title: "申请发票",
                meta: [
                    {
                        hid: "description",
                        name: "description",
                        content: "My custom description",
                    },
                ],
            };
        },
        // components: {
        //     electronicPersonal,
        // },
        data() {
            return {
                moldList: [], //发票类型
                typeList: [
                    //发票抬头
                    // {name:'不开票',key:0},
                    {
                        name: "个人",
                        key: 1,
                    },
                    {
                        name: "单位",
                        key: 2,
                    },
                ],
                contentTypeList: [], //发票内容
                receiptForm: {
                    //发票表单内容
                    memberInvoiceType: 1, //发票分类 1：电子发票，2：普通发票（纸质），3：增值税专项发票
                    companyType: 1, //发票类型 1：个人，2：单位
                    memberInvoiceContent: '', //发票内容类型 1：商品明细，2：商品类别
                    orderId: this.$route.query.orderId,
                    addresseeAddress: "", //收票人地址（空格分隔） ,
                    addresseeMail: "", //收票人邮箱 ,
                    addresseeName: "", //收票人名称 ,
                    addresseePhone: "", //收票人手机 ,
                    bankName: "", //开户银行 ,
                    bankNumber: "", //银行账号 ,
                    cityId: "", //市ID ,
                    company: "", //单位名称（公司抬头）,
                    detailedAddress: "", //收票人详细地址 ,
                    districtId: "", //区ID ,
                    dutyParagraph: "", //企业税号 ,
                    officePhone: "", //办公电话 ,
                    personalName: "", //个人名称（个人发票使用） ,
                    provinceId: "", //省ID ,
                    registeredAddress: "", //注册地址 ,
                    storeId: "", //店铺ID ,
                    streetId: "", //街道ID
                    regions: [], //所在地区
                    id: "",
                    orderSn: "",
                },
                invoiceForm: {
                    //当前发票状态
                    changeFlag: 1, //是否换开（默认0：未换开、1：换开） ,
                    invoiceEvolve: 1, //开票进度（1：待开票、2：已开票、3：待换开、4：已换开） ,
                    storeInvoiceType: 1, //发票分类 1：电子发票，2：普通发票（纸质），3：增值税专项发票
                },
                changForm: {
                    //发票换开地址
                    addressee: "",
                    addresseePhone: "",
                    detailedAddress: "",
                    mailingAddress: "",
                },
                receiptRule: {
                    //表单规则验证
                    personalName: [
                        {
                            required: true,
                            message: "个人名称不能为空",
                            trigger: "blur",
                        },
                    ],
                    addresseeName: [
                        {
                            required: true,
                            message: "收票人名称不能为空",
                            trigger: "blur",
                        },
                    ],
                    addresseePhone: [
                        {
                            required: true,
                            message: "收票人手机号不能为空",
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                let reg = /^1[3456789]\d{9}$/;
                                if (!reg.test(value)) {
                                    callback(new Error("手机号格式错误"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    addresseeMail: [
                        {
                            required: true,
                            message: "收票人邮箱不能为空",
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                let reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
                                if (!reg.test(value)) {
                                    callback(new Error("邮箱格式错误"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    company: [
                        {
                            required: true,
                            message: "单位名称不能为空",
                            trigger: "blur",
                        },
                        // {
                        //     validator: (rule, value, callback) => {
                        //         let a = /^.{0,40}$/;
                        //         if (!a.test(value)) {
                        //             callback(new Error('最多不超过40个字'));
                        //         }else {
                        //             callback();
                        //         }
                        //     },
                        //     trigger: 'blur'
                        // }
                    ],
                    dutyParagraph: [
                        {
                            required: true,
                            message: "单位税号不能为空",
                            trigger: "blur",
                        },
                        {
                            validator: (rule, value, callback) => {
                                let reg = /^[A-Z0-9]{15}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$/;
                                if (!reg.test(value)) {
                                    callback(new Error("税号格式错误"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "blur",
                        },
                    ],
                    regions: [
                        {
                            required: true,
                            validator: (rule, value, callback) => {
                                if (value.length == 0 || !value[0]) {
                                    callback(new Error("所在地区不能为空"));
                                } else {
                                    callback();
                                }
                            },
                            trigger: "change",
                        },
                    ],
                    detailedAddress: [
                        {
                            required: true,
                            message: "详细地址不能为空",
                            trigger: "blur",
                        },
                    ],
                    // registeredAddress: [{
                    //     required: false,
                    //     message: '注册地址不能为空',
                    //     trigger: 'blur'
                    // }],
                    // officePhone: [{
                    //         required: true,
                    //         message: '办公电话不能为空',
                    //         trigger: 'blur'
                    //     },
                    // {
                    //     validator: (rule, value, callback) => {
                    //         let reg = /^1[3456789]\d{9}$/;
                    //         if (!(reg.test(value))) {
                    //             callback(new Error('手机号格式错误'));
                    //         } else {
                    //             callback();
                    //         }
                    //     },
                    //     trigger: 'blur'
                    // }
                    // ],
                    // bankName: [{
                    //     required: true,
                    //     message: '开户银行不能为空',
                    //     trigger: 'blur'
                    // }],
                    // bankNumber: [{
                    //     required: true,
                    //     validator: (rule, value, callback) => {
                    //         let reg = /[0-9]{16,19}/;
                    //         if (!value) {
                    //             callback();
                    //         } else if (!reg.test(value)) {
                    //             callback(new Error('银行卡号格式错误'));
                    //         } else {
                    //             callback();
                    //         }
                    //     },
                    //     trigger: 'blur'
                    // }],
                },
                areaListOne: [], //一级地址城市
                setStatus: false, //设置发票默认
                invoiceList: [], //发票抬头列表
                defaultNew: "", //默认发票信息
                modal_loading: false, //按钮缓加载
                addValInvoiceType: false,
                disabled: false,
                maxlength: 10,
            };
        },
        created() {
            this.getArea(0);
            this.getOrderInvoice();
            this.getInvoiceList();
            this.checkInvoice();
        },
        methods: {
            checkInvoice() {
                invoiceCheck(this.$route.query.orderId)
                    .then((res) => {
                            console.log("99999999999999999999",res)
                        for (var i = 0; i < res.data.invoiceTypes.length; i++) {
                            if (res.data.invoiceTypes[i] == 1) {
                                this.moldList.push({
                                    name: "电子发票",
                                    key: 1,
                                });
                            } else if (res.data.invoiceTypes[i] == 2) {
                                this.moldList.push({
                                    name: "普通发票（纸质）",
                                    key: 2,
                                });
                            } else if (res.data.invoiceTypes[i] == 3) {
                                this.moldList.push({
                                    name: "增值税专项发票",
                                    key: 3,
                                });
                            }
                        }
                        for (var i = 0; i < res.data.invoiceContents.length; i++) {
                            if (res.data.invoiceContents[i] == 1) {
                                this.contentTypeList.push({
                                    name: "商品明细",
                                    key: 1,
                                });
                            } else if (res.data.invoiceContents[i] == 2) {
                                this.contentTypeList.push({
                                    name: "商品类别",
                                    key: 2,
                                });
                            }
                        }
                        if(this.contentTypeList.length!=0){
                                this.receiptForm.memberInvoiceContent = this.contentTypeList[0].key
                        }


                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            getInvoiceList() {
                InvoiceList()
                    .then((res) => {
                        console.log("发票列表", res);
                        if (res.code == 200) {
                            this.invoiceList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            changeInvoice(e) {
                this.invoiceList.map((item, index) => {
                    if (e) {
                        if (item.id == e || item.company == e) {
                            this.defaultNew = item;
                            this.receiptForm = {
                                ...this.receiptForm,
                                ...item,
                            };
                        }
                    }
                });
            },
            handleCreate1(val) {
                // console.log(val, this.receiptForm.id)
                // this.invoiceList.map((item, index) => {
                //     if (item.id == this.receiptForm.id) {
                //         this.invoiceList[index].company = val;
                //         this.receiptForm.company = val;
                //     }
                //
                if (val.length > 40) {
                    this.$Message.info("单位名称最多为40个字,请重新输入");
                    val = val.substring(0, 40);
                    // alert(val)
                }
                this.invoiceList.push({
                    company: val.substring(0, 40),
                    id: 1,
                });
                this.receiptForm.company = val;
            },

            //获取用户默认发票
            getDefultInvoice() {
                DefaultInvoice()
                    .then((res) => {
                        console.log("获取默认发票", res);
                        if (res.code == 200 && res.data) {
                            this.defaultNew = res.data;
                            this.receiptForm = {
                                ...this.receiptForm,
                                ...this.defaultNew,
                            };
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //获取用户当前订单发票信息
            getOrderInvoice() {
                orderInvoice(this.$route.query.orderId)
                    .then((res) => {
                        if (res.code == 200) {
                            this.invoiceForm = res.data;
                            //查询换开地址
                            if (res.data.storeId !== null) {
                                invoiceInfo(res.data.storeId)
                                    .then((res) => {
                                        if (res.code == 200) {
                                            this.changForm = res.data;
                                        }
                                    })
                                    .catch((error) => {
                                        console.log(error);
                                    });
                            } else {
                                this.invoiceForm.orderSn = this.$route.query.orderSn;
                            }
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            getInvoiceList() {
                InvoiceList()
                    .then((res) => {
                        console.log("发票列表", res);
                        if (res.code == 200) {
                            this.invoiceList = res.data;
                        }
                    })
                    .catch((err) => console.log(err));
            },
            //重构发票的省市县街道id
            regionChangeOtner(e) {
                console.log("修改id", e);
                this.receiptForm.provinceId = e[0];
                this.receiptForm.cityId = e[1];
                this.receiptForm.districtId = e[2];
                if (e.length == 3) {
                    this.receiptForm.streetId = "";
                } else {
                    this.receiptForm.streetId = e[3];
                }
            },
            //通过省市县街道ID获取对应的省市县街道lable
            receiptDemoFn(arr, data, city = []) {
                if (typeof data === "object") {
                    for (let i = 0; arr[i] !== undefined; i++) {
                        for (let j = 0; data[j] !== undefined; j++) {
                            if (arr[i] === data[j].value) {
                                city.push(data[j].label);
                            }
                        }
                    }
                    for (let i = 0; data[i] !== undefined; i++) {
                        this.receiptDemoFn(arr, data[i].children, city);
                    }
                }
                this.receiptForm.addresseeAddress = city.join(" ");
            },
            // 第一级的调地区接口  以后可能有用
            getArea(id) {
                AreaList({
                    id: id,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            this.areaListOne = res.data.map((item) => {
                                return {
                                    value: item.id,
                                    label: item.areaName,
                                    children: [],
                                    loading: false,
                                    flag: item.count,
                                };
                            });
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 一级一级的调地区接口  以后可能有用
            loadData(item, callback) {
                console.log("返回数据", item);
                item.loading = true;
                AreaList({
                    id: item.value,
                })
                    .then((res) => {
                        if (res.code == 200) {
                            item.children = res.data.map((item) => {
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
                            item.loading = false;
                            callback();
                        }
                    })
                    .catch((err) => console.log(err));
            },
            // 发票表单储存
            receiptSubmit(name) {
                //将城市联动的名称重新组装
                if (this.receiptForm.regions.length != 0) {
                    this.receiptDemoFn(this.receiptForm.regions, this.areaListOne);
                }
                this.$refs[name].validate((valid) => {
                    console.log(2);
                    if (valid) {
                        var memberInvoiceDTO = {
                            bankName: this.receiptForm.bankName,
                            bankNumber: this.receiptForm.bankNumber,
                            company: this.receiptForm.company,
                            defaultFlag: this.receiptForm.setStatus == true ? 1 : 0,
                            dutyParagraph: this.receiptForm.dutyParagraph,
                            officePhone: this.receiptForm.officePhone,
                            registeredAddress: this.receiptForm.registeredAddress,
                        };
                        this.receiptForm.memberInvoiceDTO = memberInvoiceDTO;
                        if (this.invoiceForm.invoiceEvolve !== 2) {
                            invoiceApply(this.receiptForm)
                                .then((res) => {
                                    if (res.code == 200) {
                                        this.$router.push({
                                            path: "/personalCenterBase/invoiceBase/verificationInvoice",
                                            query: {
                                                orderId: this.$route.query.orderId,
                                            },
                                        });
                                    }
                                })
                                .catch((error) => {
                                    console.log(error);
                                });
                        } else {
                            changeInvoiceApply(this.receiptForm)
                                .then((res) => {
                                    if (res.code == 200) {
                                        this.$router.push({
                                            path: "/personalCenterBase/invoiceBase/verificationInvoice",
                                            query: {
                                                orderId: this.$route.query.orderId,
                                            },
                                        });
                                    }
                                })
                                .catch((error) => {
                                    console.log(error);
                                });
                        }
                    }
                });
            },
            //清空发票校验
            receiptResetData(name) {
                this.$refs[name].resetFields();
                if (this.receiptForm.companyType == 2) {
                    this.receiptForm = {
                        ...this.receiptForm,
                        ...this.defaultNew,
                    };
                }
                // this.$router.push({
                //     name:'useInvoice',
                //     query:{
                //          orderId:this.$route.query.orderId
                //     }
                // })
            },
            //改变发票类型
            receiptMoldFn(key, index) {
                console.log(key, index);
                let that = this;
                try {
                    that.receiptResetData("receiptForm");
                    if (key == 3) {
                        that.receiptForm.companyType = 1;
                        that.receiptForm.memberInvoiceType = 1;
                        setTimeout(() => {
                            that.receiptForm.companyType = 2;
                        }, 10);
                    } else {
                        that.receiptForm.companyType = 1;
                    }
                    that.receiptForm.memberInvoiceType = index + 1;
                    that.receiptForm.memberInvoiceContent = 1;
                    let obj = {
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //订单ID
                        regions: [],
                        orderId: this.$route.query.orderId,
                    };
                    that.receiptForm = {
                        ...that.receiptForm,
                        ...obj,
                    };
                    if (that.receiptForm.companyType == 2) {
                        that.receiptForm = {
                            ...that.receiptForm,
                            ...that.defaultNew,
                        };
                        this.setStatus = true;
                    }
                } catch (e) {
                    that.receiptForm.memberInvoiceType = index + 1;
                    that.receiptForm.memberInvoiceContent = 1;
                    key == 3
                        ? (that.receiptForm.companyType = 2)
                        : (that.receiptForm.companyType = 1);
                    let obj = {
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //街道ID
                        regions: [],
                        orderId: this.$route.query.orderId,
                    };
                    that.receiptForm = {
                        ...that.receiptForm,
                        ...obj,
                    };
                    if (that.receiptForm.companyType == 2) {
                        that.receiptForm = {
                            ...that.receiptForm,
                            ...that.defaultNew,
                        };
                        this.setStatus = true;
                    }
                }
            },
            //改变发票抬头为个人或单位
            receiptTypeFn(index) {
                let that = this;
                try {
                    that.receiptResetData("receiptForm");
                    that.receiptForm.companyType = index + 1;
                    that.receiptForm.memberInvoiceContent = 1;
                    let obj = {
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //街道ID
                        regions: [],
                    };
                    that.receiptForm = {
                        ...that.receiptForm,
                        ...obj,
                    };
                    if (that.receiptForm.companyType == 2) {
                        that.receiptForm = {
                            ...that.receiptForm,
                            ...that.defaultNew,
                        };
                        this.setStatus = true;
                    }
                } catch (e) {
                    that.receiptForm.companyType = index + 1;
                    that.receiptForm.memberInvoiceContent = 1;
                    let obj = {
                        addresseeAddress: "", //收票人地址（空格分隔） ,
                        addresseeMail: "", //收票人邮箱 ,
                        addresseeName: "", //收票人名称 ,
                        addresseePhone: "", //收票人手机 ,
                        bankName: "", //开户银行 ,
                        bankNumber: "", //银行账号 ,
                        cityId: "", //市ID ,
                        company: "", //单位名称（公司抬头） ,
                        detailedAddress: "", //收票人详细地址 ,
                        districtId: "", //区ID ,
                        dutyParagraph: "", //企业税号 ,
                        officePhone: "", //办公电话 ,
                        personalName: "", //个人名称（个人发票使用） ,
                        provinceId: "", //省ID ,
                        registeredAddress: "", //注册地址 ,
                        storeId: "", //店铺ID ,
                        streetId: "", //街道ID
                        regions: [],
                    };
                    that.receiptForm = {
                        ...that.receiptForm,
                        ...obj,
                    };
                    if (that.receiptForm.companyType == 2) {
                        that.receiptForm = {
                            ...that.receiptForm,
                            ...that.defaultNew,
                        };
                        this.setStatus = true;
                    }
                }
            },
            changeMemberInvoiceContent(item){
                    this.receiptForm.memberInvoiceContent = item.key
            }
        },
        watch: {
            receiptForm: {
                immediate: true,
                handler(newVal, oldVal) {
                    try {
                        if (newVal.memberInvoiceType == 3) {
                            this.receiptRule.registeredAddress[0].required = true;
                            this.receiptRule.bankNumber[0].required = true;
                        } else if (newVal.memberInvoiceType !== 3) {
                            // console.log(this.receiptRule, "this.receiptRule");
                            this.receiptRule.registeredAddress[0].required = false;
                            this.receiptRule.bankNumber[0].required = false;
                        }
                    } catch {}
                },
            },
        },
    };
</script>
<style lang="scss" scoped>
    .invoice {
        width: 948px;
        height: 100%;
        background: #ffffff;
        padding-bottom: 50px;

        .invoiContent-top {
            width: 742px;
            height: 132px;
            margin: 40px 103px 0;
            padding: 18px 30px;
            background: rgba(252, 184, 182, 0.06);
            .word {
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(51, 51, 51, 1);
            }

            .invoiContent-tx {
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                margin: 6px 0 0 0;

                img {
                    width: 12px;
                    height: 12px;
                }
            }
        }

        .personal {
            padding: 30px 103px;

            .personalWord {
                font-size: 16px;
                font-weight: 400;
                color: #333333;
                display: flex;
                line-height: 16px;
                margin: 0 0 20px 0;
            }

            .personal-one {
                margin: 0 37px;
            }
        }

        .receipt_mold {
            width: 100%;
            display: flex;

            .mold_item {
                width: 155px;
                height: 34px;
                border-radius: 1px;
                text-align: center;
                line-height: 34px;
                font-size: 16px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(102, 102, 102, 1);
                border: 1px solid rgba(204, 204, 204, 1);
                position: relative;
                border-radius: 1px;
                margin: 0 25px 22px 0;
                cursor: pointer;

                img {
                    width: 14px;
                    height: 14px;
                    position: absolute;
                    bottom: -1px;
                    right: -1px;
                }
            }

            .mold_itemActive {
                color: rgba(51, 51, 51, 1);
                border: 1px solid rgba(221, 38, 25, 1);
            }

            .mold_item:nth-child(4) {
                margin-right: 0;
            }
        }

        .receipt_type {
            width: 100%;
            display: flex;
        }

        .commenTitle {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(51, 51, 51, 1);
            line-height: 34px;
            margin-right: 31px;
        }

        .commentDiv {
            width: 98px;
            height: 34px;
            margin-right: 27px;
            border-radius: 1px;
            border: 1px solid rgba(204, 204, 204, 1);
            text-align: center;
            line-height: 34px;
            color: rgba(102, 102, 102, 1);
            position: relative;
            cursor: pointer;

            img {
                width: 14px;
                height: 14px;
                position: absolute;
                bottom: -1px;
                right: -1px;
            }
        }

        .mold_itemActive {
            color: rgba(51, 51, 51, 1);
            border: 1px solid rgba(221, 38, 25, 1);
        }

        .personal-oneList {
            display: flex;
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #333;
            line-height: 34px;
            margin: 0 18px 26px 0;
        }
    }

    .commenBox {
        width: 100%;
        padding: 10px 103px 3px;
        margin-top: 22px;

        .commenBox-title {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 600;
            color: rgba(51, 51, 51, 1);
        }

        .receiptForm1 {
            padding: 0 40px;
        }
    }

    .footerBtn {
        text-align: center;
    }

    /deep/ .ivu-form-item {
        display: flex;
    }

    /deep/ .ivu-form .ivu-form-item-label {
        width: 119px !important;
        font-size: 16px;
        font-weight: 400;
        text-align: left;
    }

    /deep/ .ivu-input {
        width: 327px;
        height: 38px;
        padding: 4px 14px;
    }

    /deep/ .ivu-btn {
        width: 96px;
        height: 34px;
    }

    /deep/ .ivu-select-single .ivu-select-selection {
        height: 38px;
    }

    /deep/ .ivu-select-input {
        height: 38px;
        padding: 0 24px 0 14px;
    }
</style>
