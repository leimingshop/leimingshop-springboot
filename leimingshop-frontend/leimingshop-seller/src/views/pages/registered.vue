<template>
    <div class="registered">
        <!--头部-->
        <div class="registeredHead">
            <img src="@/assets/img/sellerlogo.png" />
            <div class="Rtitle">商户注册</div>
        </div>
        <div class="registeredBody">
            <div class="instructions">
                <div v-html="this.notice" style="width: 320px;" class="ql-editor"></div>
            </div>
            <div class="line"></div>
            <div class="steps">
                <el-steps :active="active">
                    <el-step title="手机验证" description="">
                    </el-step>
                    <el-step title="用户协议" description=""></el-step>
                    <el-step title="注册账号" description=""></el-step>
                </el-steps>
                <div v-if="active==1" style="text-align: center">
                    <el-form
                            :inline="true"
                            ref="phoneForm"
                            :model="phoneForm"
                            class="phoneForm"
                            :rules="phoneRule"
                    >
                        <el-form-item label="手机号：" prop="mobilePhone" class="phoneInput">
                            <el-input v-model="phoneForm.mobilePhone" placeholder="请输入" maxlength="11" clearable @blur="isRegisteredMobile()"></el-input>
                            <span v-show="isHref" class="isHref">该手机号已注册，请
                                 <a @click="jumpLogin" style="cursor: pointer;">直接登录</a>
                            </span>
                            <!-- <span v-show="phoneCanuse" class="phoneCanuse">当前账号可以使用</span> -->
                        </el-form-item>
                        <el-form-item label="图形验证码：" prop="captcha">
                            <el-input v-model="phoneForm.captcha" placeholder="请输入" clearable></el-input>
                            <div class="pirtureCode">
                                <img :src="captchaPath" @click="getCaptcha()" style="height:38px;width:100%;border: 1px solid #b4bccc;">
                            </div>
                        </el-form-item>
                        <el-form-item label="手机验证码：" prop="phoneCode">
                            <el-input v-model="phoneForm.phoneCode" placeholder="请输入" clearable></el-input>
                            <el-button class="btn getCode" type="primary" @click="getCode()" :disabled="msgBtn.waitingCode">{{msgBtn.text}}</el-button>
                        </el-form-item>
                    </el-form>
                    <el-button class="btn nextStep" type="primary" @click="nextStep()">下一步</el-button>
                </div>
                <div v-if="active==2" class="secondForm">
                    <div class="useProtocol">用户服务协议</div>
                    <div class="ql-editor" style="width: 500px; height: 155px;"
                            type="textarea"
                            v-html="textarea2">
                    </div>
                    <el-checkbox v-model="checked1" style="margin-top: 15px;margin-left: 50px">我已阅读并同意上述《用户服务协议》</el-checkbox>
                   <div style="text-align: center">
                       <el-button class="btn nextStep" type="primary" @click="nextStep()" :disabled="!checked1">下一步</el-button>
                   </div>
                </div>
                <div v-if="active==3">
                    <el-form
                            :inline="true"
                            ref="registeredForm"
                            :model="registeredForm"
                            class="ThirdForm"
                            :rules="registeredRule"
                    >
                        <el-form-item label="用户名称：" prop="account">
                            <el-input v-model="registeredForm.account" placeholder="请输入字母，数字，下划线" clearable></el-input>
                            <el-button class="verifyName" type="primary" @click="isRegisteredName()">检查是否可用</el-button>
                        </el-form-item>
                        <el-form-item label="邮箱：" prop="email">
                            <el-input v-model="registeredForm.email" placeholder="请输入" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="登录密码：" prop="password">
                            <el-input v-model="registeredForm.password" type="password" placeholder="请输入" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="再输一次：" prop="confirmPassword">
                            <el-input v-model="registeredForm.confirmPassword" type="password" placeholder="请输入" clearable></el-input>
                        </el-form-item>
                        <div class="foot3">
                            <div class="foot3-1">
                                <el-checkbox v-model="checked2">我已阅读并同意上述《用户服务协议》</el-checkbox>
                            </div>
                            <div class="foot3-2">
                                <el-button class="btn" type="primary" @click="registeredAccount">注册账号</el-button>
                            </div>
                        </div>
                    </el-form>
                </div>
            </div>
        </div>
        <div class="registeredFoot">
            雷铭科技2019 © leimingtech.com
        </div>
    </div>
</template>

<script>
    import { getUUID } from '@/utils'
    import { isEmail, isMobile } from '@/utils/validate'
    import JsEncrypt from 'jsencrypt'
    import security from '@/utils/security.js'
    import 'quill/dist/quill.core.css';
    import 'quill/dist/quill.snow.css';
    import 'quill/dist/quill.bubble.css';
    import { verifyMobile,getMobileCode,verifyUserName,registerShops,isSameCode,noticeProtocol } from "@/api/api";
    //verifyMobile
    export default {
        name: "registered.vue",
        data(){
            // 验证手机号
            var validateMobile = (rule, value, callback) => {
                if(this.mobileStatus){
                    callback()
                }else{
                    return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.mobile') })))
                }
            }
            // 验证邮箱
            var validateEmail = (rule, value, callback) => {
                if (!isEmail(value)) {
                    return callback(new Error(this.$t('validate.format', { 'attr': this.$t('user.email') })))
                }
                callback()
            }
            //  图形验证码校验
            var validateCaptcha = (rule, value, callback) => {
                if(this.captchaStatus){
                    callback()
                }else{
                    return callback(new Error('图形验证码错误'))
                }
            }
            //   手机验证码校验
            var validatePhoneCode = (rule, value, callback) => {
                if(this.phoneCodeStatus){
                    callback()
                }else{
                    return callback(new Error('手机验证码错误'))
                }
            }
            /*用户名 只能输入字符 数字 下划线和减号，输入别的会提示 输入非法字符，请重新输入*/
            var validateUserName = (rule, value, callback) => {
                if(value.length>20){
                    return callback(new Error('用户名称不能超过20个字符'))
                }
                for(let i=0;i<value.length;i++){
                    if(!/[a-z|A-Z|0-9|_|-]/.test(value[i])){
                       return callback(new Error('用户名称不能包括特殊字符（下划线与减号除外）'))
                    }
                }
                if(!/[a-zA-Z0-9|_|-]{6,20}/.test(value)){
                    return callback(new Error('账号必须为6-20位字母，数字，下划线'))
                }
                callback()
            }
            var validateComfirmPassword = (rule, value, callback) => {
                if (!/\S/.test(value)) {
                    return callback(new Error(this.$t('validate.required')))
                }
                if (this.registeredForm.password !== value) {
                    return callback(new Error(this.$t('user.validate.comfirmPassword')))
                }
                callback()
            }
            return{
                active:1,// 默认显示第一步
                checked1: false,
                checked2: true,
                captchaPath: '',
                // phoneCanuse:false,// 该账号可以适应
                isHref:false,// 该账号已经注册标记
                // 手机号输入状态
                mobileStatus:true,
                captchaStatus:true,
                phoneCodeStatus:true,
                notice:"",
                msgBtn: {
                    text: "发送验证码",
                    waitingCode: false,
                    count: 60
                },
                /*第一步 手机验证 */
                phoneForm:{
                    mobilePhone:'',
                    phoneCode:'',
                    uuid: '',
                    captcha:''
                },
                /*注册账号*/
                registeredForm:{
                    account:'',
                    email:'',
                    password:'',
                    confirmPassword:'',
                },
                // 校验规则
                phoneRule: {
                    mobilePhone: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validateMobile, trigger: 'blur' }
                    ],
                    captcha: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validateCaptcha, trigger: 'blur' }
                    ],
                    phoneCode: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validatePhoneCode, trigger: 'blur' }
                    ],
                },
                registeredRule:{
                    account: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validateUserName, trigger: 'blur' }
                    ],
                    email: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                        { validator: validateEmail, trigger: 'blur' }
                    ],
                    password: [
                        {required: true, message: '必填项不能为空', trigger: 'blur'},
                    ],
                    confirmPassword: [
                        { validator: validateComfirmPassword, trigger: 'blur' }
                    ],
                },
                textarea2: '特别提醒用户认真阅读本《用户服务协议》(下称《协议》) 中各条款。除非您接受本《协议》条款，否则您无权使用本网站提供的相关服务。本协议是您（本文又称“用户”）与雷铭科技（简称"本网站"，网址：www.leimingtech.com）所有者（以下简称为"雷铭科技"）之间就雷铭科技网站服务等相关事宜所订立的协议，请您仔细阅读本协议，您同意注册后，本协议即构成对双方有约束力的法律文件。\n' +
                    '\n' +
                    '一、定义\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '1.  "用户"指符合本协议所规定的条件，同意遵守本网站各种规则、条款（包括但不限于本协议），并使用本网站的个人或机构。\n' +
                    '\n' +
                    '2.  "卖家"是指在本网站上出售物品的用户。"买家"是指在本网站购买物品的用户。\n' +
                    '\n' +
                    '3.  "成交"指买家根据卖家所展示的交易要求，在特定时间内提出最优的交易条件，因而取得依其提出的条件购买该交易物品的权利。\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '二、用户资格\n' +
                    '\n' +
                    '只有符合下列条件之一的人员或实体才能申请成为本网站用户，可以使用本网站的服务。\n' +
                    '\n' +
                    '1.  年满十八岁，并具有民事权利能力和民事行为能力的自然人；\n' +
                    '\n' +
                    '2.  未满十八岁，但监护人（包括但不仅限于父母）予以书面同意的自然人；\n' +
                    '\n' +
                    '3.  根据中国法律或设立地法律、法规和/或规章成立并合法存在的公司、企事业单位、社团组织和其他组织。\n' +
                    '\n' +
                    '\n' +
                    '无民事行为能力人、限制民事行为能力人以及无经营或特定经营资格的组织不当注册为本网站用户或超过其民事权利或行为能力范围从事交易的，其与本网站之间的协议自始无效，本网站一经发现，有权立即注销该用户，并追究其使用本网站"服务"的一切法律责任。\n' +
                    '\n' +
                    '三.用户的权利和义务\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '1.  用户有权根据本协议的规定及本网站发布的相关规则，利用本网站网上交易平台登录物品、发布交易信息、查询物品信息、购买物品、与其他用户订立物品买卖合同、在本网站社区发帖、参加本网站的有关活动及有权享受本网站提供的其他的有关资讯及信息服务。\n' +
                    '\n' +
                    '2.  用户有权根据需要更改密码和交易密码。用户应对以该用户名进行的所有活动和事件负全部责任。\n' +
                    '\n' +
                    '3.  用户有义务确保向本网站提供的任何资料、注册信息真实准确，包括但不限于真实姓名、身份证号、联系电话、地址、邮政编码等。保证本网站及其他用户可以通过上述联系方式与自己进行联系。同时，用户也有义务在相关资料实际变更时及时更新有关注册资料。\n' +
                    '\n' +
                    '4.  用户不得以任何形式擅自转让或授权他人使用自己在本网站的用户帐号。\n' +
                    '\n' +
                    '5.  用户有义务确保在本网站网上交易平台上登录物品、发布的交易信息真实、准确，无误导性。\n' +
                    '\n' +
                    '6.  用户不得在本网站网上交易平台买卖国家禁止销售的或限制销售的物品、不得买卖侵犯他人知识产权或其他合法权益的物品，也不得买卖违背社会公共利益或公共道德的物品。\n' +
                    '\n' +
                    '7.  用户不得在本网站发布各类违法或违规信息。包括但不限于物品信息、交易信息、社区帖子、物品留言，店铺留言，评价内容等。\n' +
                    '\n' +
                    '8.  用户在本网站交易中应当遵守诚实信用原则，不得以干预或操纵物品价格等不正当竞争方式扰乱网上交易秩序，不得从事与网上交易无关的不当行为，不得在交易平台上发布任何违法信息。\n' +
                    '\n' +
                    '9.  用户不应采取不正当手段（包括但不限于虚假交易、互换好评等方式）提高自身或他人信用度，或采用不正当手段恶意评价其他用户，降低其他用户信用度。\n' +
                    '\n' +
                    '10.用户承诺自己在使用本网站网上交易平台实施的所有行为遵守国家法律、法规和本网站的相关规定以及各种社会公共利益或公共道德。对于任何法律后果的发生，用户将以自己的名义独立承担所有相应的法律责任。\n' +
                    '\n' +
                    '11.用户在本网站网上交易过程中如与其他用户因交易产生纠纷，可以请求本网站从中予以协调。用户如发现其他用户有违法或违反本协议的行为，可以向本网站举报。如用户因网上交易与其他用户产生诉讼的，用户有权通过司法部门要求本网站提供相关资料。\n' +
                    '\n' +
                    '12.用户应自行承担因交易产生的相关费用，并依法纳税。\n' +
                    '\n' +
                    '13.未经本网站书面允许，用户不得将本网站资料以及在交易平台上所展示的任何信息以复制、修改、翻译等形式制作衍生作品、分发或公开展示。\n' +
                    '\n' +
                    '14.用户同意接收来自本网站的信息，包括但不限于活动信息、交易信息、促销信息等。\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '四、 本网站的权利和义务\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '1.  本网站仅为用户提供一个信息交流、进行物品买卖的平台，充当买卖双方之间的交流媒介，而非买主或卖主的代理商、合伙人、雇员或雇主等经营关系人。公布在本网站上的交易物品是用户自行上传进行交易的物品，并非本网站所有。对于用户发布的物品或提供的信息，本网站不介入商品的交易过程，包括运送、付款、退款、瑕疵担保及其它交易事项，且不承担因交易物品存在品质、权利上的瑕疵以及交易方履行交易协议的能力而产生的任何责任。\n' +
                    '\n' +
                    '2.  本网站有义务在现有技术水平的基础上努力确保整个网上交易平台的正常运行，尽力避免服务中断或将中断时间限制在最短时间内，保证用户网上交易活动的顺利进行。\n' +
                    '\n' +
                    '3.  本网站有义务对用户在注册使用本网站网上交易平台中所遇到的问题及反映的情况及时作出回复。\n' +
                    '\n' +
                    '4.  本网站有权对用户的注册资料进行查阅，对存在任何问题或怀疑的注册资料，本网站有权发出通知询问用户并要求用户做出解释、改正，或直接做出处罚、删除等处理。\n' +
                    '\n' +
                    '5.  用户因在本网站网上交易与其他用户产生纠纷的，用户通过司法部门或行政部门依照法定程序要求本网站提供相关资料，本网站将积极配合并提供有关资料；用户将纠纷投诉致本网站，或本网站知悉纠纷情况的，经审核后，本网站有权通过电子邮件及电话联系向纠纷双方了解纠纷情况，并将所了解的情况通过电子邮件互相通知对方。\n' +
                    '\n' +
                    '6.  因网上交易平台的特殊性，本网站没有义务对所有用户的注册资料、所有的交易行为以及与交易有关的其他事项进行事先审查，但如发生以下情形，本网站有权限制用户的活动、向用户核实有关资料、发出警告通知、暂时中止、无限期地中止及拒绝向该用户提供服务：\n' +
                    '\n' +
                    'o    用户违反本协议或因被提及而纳入本协议的文件；\n' +
                    '\n' +
                    'o    存在用户或其他第三方通知本网站，认为某个用户或具体交易事项存在违法或不当行为，并提供相关证据，而本网站无法联系到该用户核证或验证该用户向本网站提供的任何资料；\n' +
                    '\n' +
                    'o    存在用户或其他第三方通知本网站，认为某个用户或具体交易事项存在违法或不当行为，并提供相关证据。本网站以普通非专业交易者的知识水平标准对相关内容进行判别，可以明显认为这些内容或行为可能对本网站用户或本网站造成财务损失或法律责任。\n' +
                    '\n' +
                    '7.  在反网络欺诈行动中，本着保护广大用户利益的原则，当用户举报自己交易可能存在欺诈而产生交易争议时，本网站有权通过表面判断暂时冻结相关用户账号，并有权核对当事人身份资料及要求提供交易相关证明材料。\n' +
                    '\n' +
                    '8.  根据国家法律法规、本协议的内容和本网站所掌握的事实依据，可以认定用户存在违法或违反本协议行为以及在本网站交易平台上的其他不当行为，本网站有权在本网站交易平台及所在网站上以网络发布形式公布用户的违法行为，并有权随时作出删除相关信息，而无须征得用户的同意。\n' +
                    '\n' +
                    '9.  本网站有权在不通知用户的前提下删除或采取其他限制性措施处理下列信息：包括但不限于以规避费用为目的；以炒作信用为目的；存在欺诈等恶意或虚假内容；与网上交易无关或不是以交易为目的；存在恶意竞价或其他试图扰乱正常交易秩序因素；该信息违反公共利益或可能严重损害本网站和其他用户合法利益的。\n' +
                    '\n' +
                    '10.用户授予本网站独家的、全球通用的、永久的、免费的信息许可使用权利，本网站有权对该权利进行再授权，依此授权本网站有权(全部或部份地)使用、复制、修订、改写、发布、翻译、分发、执行和展示用户公示于网站的各类信息或制作其派生作品，以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其他作品内。除法律另有强制性规定外，未经51育农网明确的特别书面许可,任何单位或个人不得以任何方式非法地全部或部分复制、转载、引用、链接、抓取或以其他方式使用本站的信息内容，否则，51育农网有权追究其法律责任\n' +
                    '\n' +
                    '11.本网站所刊登的资料信息（诸如文字、图表、标识、按钮图标、图像、声音文件片段、数字下载、数据编辑和软件），均是51育农网或其内容提供者的财产，受中国和国际版权法的保护。本站上所有内容的汇编是51育农网的排他财产，受中国和国际版权法的保护。本站上所有软件都是51育农网或其关联公司或其软件供应商的财产，受中国和国际版权法的保护。\n' +
                    '\n' +
                    '\n' +
                    '五、服务的中断和终止\n' +
                    '\n' +
                    '\n' +
                    '\n' +
                    '1.  在本网站未向用户收取相关服务费用的情况下，本网站可自行全权决定以任何理由 (包括但不限于本网站认为用户已违反本协议的字面意义和精神，或用户在超过180天内未登录本网站等)终止对用户的服务，并不再保存用户在本网站的全部资料（包括但不限于用户信息、商品信息、交易信息等）。同时本网站可自行全权决定，在发出通知或不发出通知的情况下，随时停止提供全部或部分服务。服务终止后，本网站没有义务为用户保留原用户资料或与之相关的任何信息，或转发任何未曾阅读或发送的信息给用户或第三方。此外，本网站不就终止对用户的服务而对用户或任何第三方承担任何责任。\n' +
                    '\n' +
                    '2.  如用户向本网站提出注销本网站注册用户身份，需经本网站审核同意，由本网站注销该注册用户，用户即解除与本网站的协议关系，但本网站仍保留下列权利：\n' +
                    '\n' +
                    'o    用户注销后，本网站有权保留该用户的资料,包括但不限于以前的用户资料、店铺资料、商品资料和交易记录等。\n' +
                    '\n' +
                    'o    用户注销后，如用户在注销前在本网站交易平台上存在违法行为或违反本协议的行为，本网站仍可行使本协议所规定的权利。\n' +
                    '\n' +
                    '3.  如存在下列情况，本网站可以通过注销用户的方式终止服务：\n' +
                    '\n' +
                    'o    在用户违反本协议相关规定时，本网站有权终止向该用户提供服务。本网站将在中断服务时通知用户。但如该用户在被本网站终止提供服务后，再一次直接或间接或以他人名义注册为本网站用户的，本网站有权再次单方面终止为该用户提供服务；\n' +
                    '\n' +
                    'o    一旦本网站发现用户注册资料中主要内容是虚假的，本网站有权随时终止为该用户提供服务；\n' +
                    '\n' +
                    'o    本协议终止或更新时，用户未确认新的协议的。\n' +
                    '\n' +
                    'o    其它本网站认为需终止服务的情况。\n' +
                    '\n' +
                    '4.  因用户违反相关法律法规或者违反本协议规定等原因而致使本网站中断、终止对用户服务的，对于服务中断、终止之前用户交易行为依下列原则处理：\n' +
                    '\n' +
                    'o    本网站有权决定是否在中断、终止对用户服务前将用户被中断或终止服务的情况和原因通知用户交易关系方，包括但不限于对该交易有意向但尚未达成交易的用户,参与该交易竞价的用户，已达成交易要约用户。\n' +
                    '\n' +
                    'o    服务中断、终止之前，用户已经上传至本网站的物品尚未交易或交易尚未完成的，本网站有权在中断、终止服务的同时删除此项物品的相关信息。\n' +
                    '\n' +
                    'o    服务中断、终止之前，用户已经就其他用户出售的具体物品作出要约，但交易尚未结束，本网站有权在中断或终止服务的同时删除该用户的相关要约和信息。\n' +
                    '\n' +
                    '5.  本网站若因用户的行为（包括但不限于发布的商品、在本网站社区发帖等）侵害了第三方的权利或违反了相关规定，而受到第三方的追偿或受到主管机关的处分时，用户应赔偿本网站因此所产生的一切损失及费用。\n' +
                    '\n' +
                    '6.  对违反相关法律法规或者违反本协议规定，且情节严重的用户，本网站有权终止该用户的其它服务。\n' +
                    '\n' +
                    '\n' +
                    '六、协议的修订\n' +
                    '\n' +
                    '本协议可由本网站随时修订，并将修订后的协议公告于本网站之上，修订后的条款内容自公告时起生效，并成为本协议的一部分。用户若在本协议修改之后，仍继续使用本网站，则视为用户接受和自愿遵守修订后的协议。本网站行使修改或中断服务时，不需对任何第三方负责。\n' +
                    '\n' +
                    '七、 本网站的责任范围\n' +
                    '\n' +
                    '当用户接受该协议时，用户应明确了解并同意∶\n' +
                    '\n' +
                    '1.  是否经由本网站下载或取得任何资料，由用户自行考虑、衡量并且自负风险，因下载任何资料而导致用户电脑系统的任何损坏或资料流失，用户应负完全责任。\n' +
                    '\n' +
                    '2.  用户经由本网站取得的建议和资讯，无论其形式或表现，绝不构成本协议未明示规定的任何保证。\n' +
                    '\n' +
                    '3.  基于以下原因而造成的利润、商誉、使用、资料损失或其它无形损失，本网站不承担任何直接、间接、附带、特别、衍生性或惩罚性赔偿（即使本网站已被告知前款赔偿的可能性）：\n' +
                    '\n' +
                    'o    本网站的使用或无法使用。\n' +
                    '\n' +
                    'o    经由或通过本网站购买或取得的任何物品，或接收之信息，或进行交易所随之产生的替代物品及服务的购买成本。\n' +
                    '\n' +
                    'o    用户的传输或资料遭到未获授权的存取或变更。\n' +
                    '\n' +
                    'o    本网站中任何第三方之声明或行为。\n' +
                    '\n' +
                    'o    本网站其它相关事宜。\n' +
                    '\n' +
                    '4.  本网站只是为用户提供一个交易的平台，对于用户所发布的交易物品的合法性、真实性及其品质，以及用户履行交易的能力等，本网站一律不负任何担保责任。用户如果因使用本网站，或因购买刊登于本网站的任何物品，而受有损害时，本网站不负任何补偿或赔偿责任。\n' +
                    '\n' +
                    '5.  本网站提供与其它互联网上的网站或资源的链接，用户可能会因此连结至其它运营商经营的网站，但不表示本网站与这些运营商有任何关系。其它运营商经营的网站均由各经营者自行负责，不属于本网站控制及负责范围之内。对于存在或来源于此类网站或资源的任何内容、广告、产品或其它资料，本网站亦不予保证或负责。因使  用或依赖任何此类网站或资源发布的或经由此类网站或资源获得的任何内容、物品或服务所产生的任何损害或损失，本网站不负任何直接或间接的责任。\n' +
                    '\n' +
                    '\n' +
                    '八.、不可抗力\n' +
                    '\n' +
                    '因不可抗力或者其他意外事件，使得本协议的履行不可能、不必要或者无意义的，双方均不承担责任。本合同所称之不可抗力意指不能预见、不能避免并不能克服的客观情况，包括但不限于战争、台风、水灾、火灾、雷击或地震、罢工、暴动、法定疾病、黑客攻击、网络病毒、电信部门技术管制、政府行为或任何其它自然或人为造成的灾难等客观情况。\n'
            }
        },
        created () {
            this.getCaptcha()
            this.noticeProtocol(1);
            this.noticeProtocol(2);
        },
        methods:{
            // 获取验证码
            getCaptcha () {
                this.phoneForm.uuid = getUUID()
                this.captchaPath = `${window.SITE_CONFIG['apiURL']}/seller-api/generate/image?uuid=${this.phoneForm.uuid}`
            },
            //获取用户协议
            noticeProtocol(type){
                let obj = {
                        params:{
                            type:type,
                        }
                }
                noticeProtocol(obj).then(res=>{
                    if(res.code ==200) {
                        if(type==1){
                            this.notice=res.data.docContent;
                        }else{
                            this.textarea2=res.data.docContent;
                        }
                       
                    } 
                })

            },
            // 下一步
            nextStep(){
                if(this.active == 1){
                    this.$refs['phoneForm'].validate((valid) => {
                        if (!valid) {
                            return false
                        }
                        let obj = {
                           params:{
                               mobile:this.phoneForm.mobilePhone,
                               code:this.phoneForm.phoneCode
                           }
                        }
                        isSameCode(obj).then(res=>{
                            if(res.code !==200) {
                                this.getCaptcha()
                                    this.phoneCodeStatus = false
                                    this.$refs['phoneForm'].validate((valid) => {
                                        this.phoneCodeStatus = true;
                                    })
                                this.$message({
                                    message:res.msg,
                                    type: 'error',
                                    duration: 1500,
                                })
                                } else{
                                if (this.active++ > 2) this.active = 1
                            }
                        })
                    })
                }else{
                    if (this.active++ > 2) this.active = 1
                }
            },
            // 手机号是否有注册
            isRegisteredMobile(){
              if(this.phoneForm.mobilePhone){
                let obj={
                    mobile:this.phoneForm.mobilePhone
                }
                // this.phoneCanuse = false;
                this.isHref = false;
                verifyMobile(obj).then((res=>{
                        // 手机号可以使用
                        if(res.code==200){
                            // this.$message({
                            //     message:res.msg,
                            //     type: 'success',
                            //     duration: 1500,
                            // })
                            // this.phoneCanuse = true;
                            // 手机号已经注册
                        }else if(res.code==500){
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                            this.isHref = true
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                }))
                }
            },
            // 名称是否有注册
            isRegisteredName(){
              if(this.registeredForm.account){
                let obj={
                    userName:this.registeredForm.account
                }
                verifyUserName(obj).then((res=>{
                        // 名称可以使用
                        if(res.code==200){
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                            // 名称重复和异常
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                }))
                }
            },
            // 注册过 可以跳到登录页
            jumpLogin(){
                this.$router.replace({ path: 'login' })
            },
            // 获取验证码
            getCode(){
                // 获取手机验证码时候，得先验证手机号和图像验证填了没
                if(!this.phoneForm.mobilePhone ){
                    this.$message.warning("请填写手机号!");
                }
                if( !this.phoneForm.captcha){
                     this.$message.warning("请填写图形验证码!");
                    return;
                }

                this.msgBtn.waitingCode = true;
                let obj={
                  params:{
                      mobile:this.phoneForm.mobilePhone,
                      uuid:this.phoneForm.uuid,
                      captcha:this.phoneForm.captcha
                  }
                }
                getMobileCode(obj).then((res=>{
                    if(res.code !==200) {
                        this.msgBtn.waitingCode = false;
                        this.getCaptcha()
                        // 手机号格式错误
                        if (res.code == 1003) {
                            this.mobileStatus = false
                            this.$refs['phoneForm'].validate((valid) => {
                                this.mobileStatus = true;
                            })
                        } else if (res.code == 10007) {
                            this.captchaStatus = false
                            this.$refs['phoneForm'].validate((valid) => {
                                this.captchaStatus = true;
                            })
                        }
                        return this.$message.error(res.msg)
                    }else{
                        this.$message({
                            message:res.msg,
                            type: 'success',
                            duration: 1500,
                        })
                        let countdown = setInterval(() => {
                            this.msgBtn.count--;
                            this.msgBtn.text = this.msgBtn.count + "s";
                            if (this.msgBtn.count < 0) {
                                clearInterval(countdown);
                                this.msgBtn.text = "重新发送";
                                this.msgBtn.waitingCode = false;
                            }
                        }, 1000);
                    }
                }))
            },
            // 注册账号
            registeredAccount(){
                if(!this.checked2){
                    this.$message({
                        message:'请先勾选协议',
                        type: 'error',
                        duration: 1500,
                    })
                    return
                }
                this.$refs['registeredForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    // 增加用户名与密码 RSA加密
                    let publicKey = security.publicKey;
                    var encrypt = new JSEncrypt();
                    encrypt.setPublicKey(publicKey);

                    let obj = {
                       account:encrypt.encrypt(this.registeredForm.account),
                       password:encrypt.encrypt(this.registeredForm.password),
                       confirmPassword:encrypt.encrypt(this.registeredForm.confirmPassword),
                       email:this.registeredForm.email,
                       mobilePhone:this.phoneForm.mobilePhone
                    }
                    registerShops(obj).then(res=>{
                        if(res.code==200){
                            this.$message({
                                message:res.msg,
                                type: 'success',
                                duration: 1500,
                            })
                            this.$router.replace({ path: 'login' })
                        }else{
                            this.$message({
                                message:res.msg,
                                type: 'error',
                                duration: 1500,
                            })
                        }
                    })
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
.registered{
    .registeredHead{
        height: 120px;
        background-color: rgba(51, 102, 204, 1);
        img{
            border-width: 0px;
            position: absolute;
            left: 49px;
            top: 26px;
            width: 257px;
            height: 60px;
        }
        .Rtitle{
            border-width: 0px;
            position: absolute;
            left: 368px;
            top: 59px;
            width: 113px;
            height: 33px;
            font-size: 28px;
            color: #FFFFFF;
        }
    }
    .registeredBody{
        width: 1024px;
        margin: auto;
        margin-top: 137px;
        // margin-left: 10%;
        overflow: hidden;
        .line{
            width: 1px;
            // position: absolute;
            // left: 446px;
            height: 300px;
            background: #333333;
            float: left;
            margin-left:30px;
        }
        .instructions{
            width: 300px;
            float: left;
            .uText{
                text-align: center;
            }
            .uFont{
                margin-top: 48px;
            }
            .content{
                line-height: 24px;
            }
        }
        /*下一步公共样式*/
        .nextStep{
            margin-top: 20px;
        }
        /*steps样式*/
        .steps{
            float: left;
            margin-left: 60px;
            /*第一个表单*/
                .phoneForm{
                    margin-top: 60px;
                    text-align: start;
                    .isHref{
                        color:red;
                        margin-left: 20px;
                        min-width: 200px;
                        position: absolute;
                    }
                    // .phoneCanuse{
                    //     color:red;
                    //     min-width: 200px;
                    //     position: absolute;
                    // }
                    /deep/ .el-form-item__label{
                        width: 120px !important;
                    }
                    .el-form-item{
                        display: block;
                    }
                    .phoneInput{
                        /deep/ .el-input{
                            width: 300px !important;
                        }
                    }
                    .getCode{
                        margin-left: 28px;
                        width: 111.8px;
                    }
                    .pirtureCode{
                        display: inline-block;
                        margin-left: 28px;
                        text-align: center;
                        width: 112px;
                        height: 40px;
                    }
                }
            /*第二个表单*/
                .secondForm{
                    margin-top: 60px;
                    .el-textarea{
                        width: 100%;
                    }
                    .useProtocol{
                        text-align: center;
                        margin-bottom: 24px;
                    }
                }
            /*第三个表单*/
            .ThirdForm{
                margin-top: 60px;
                text-align: center;
                .verifyName{
                    margin-left: 20px;
                    position: absolute;
                }
                .el-input{
                    width: 300px !important;
                }
                /deep/ .el-form-item__label{
                    width: 120px !important;
                }
                .el-form-item{
                    display: block;
                }
                .foot3{
                    text-align: center;
                    .foot3-2{
                        margin-top: 15px;
                    }
                }
            }
        }
    }
    .registeredFoot{
        margin-top: 100px;
        text-align: center;
        color: rgba(102, 102, 102, 0.847058823529412);
    }
}
</style>
