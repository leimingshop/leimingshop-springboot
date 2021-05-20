const t = {}

t.loading = 'Loading...'

t.brand = {}
t.brand.lg = 'leimingshop Cloud'
t.brand.mini = 'RC'

t.add = 'Add'
t.delete = 'Delete'
t.deleteBatch = 'Delete'
t.update = 'Edit'
t.query = 'Query'
t.export = 'Export'
t.handle = 'Action'
t.confirm = 'Confirm'
t.cancel = 'Cancel'
t.logout = 'Sign Out'

t.prompt = {}
t.prompt.title = 'Prompt'
t.prompt.info = 'Confirm to carry out [{handle}] operation?'
t.prompt.success = 'Succeeded'
t.prompt.failed = 'Failed'
t.prompt.deleteBatch = 'Please select delete item'

t.validate = {}
t.validate.required = 'Required field cannot be empty'
t.validate.format = '{attr} format error'

t.upload = {}
t.upload.text = 'Drop files here, or <em>Click Upload</em>'
t.upload.tip = 'Only support {format} format files! '
t.upload.button = 'Click to upload'

t.datePicker = {}
t.datePicker.range = 'To'
t.datePicker.start = 'Start Date'
t.datePicker.end = 'End Date'

t.fullscreen = {}
t.fullscreen.prompt = 'Your browser does not support this operation'

t.updatePassword = {}
t.updatePassword.title = 'Change Password'
t.updatePassword.username = 'Account'
t.updatePassword.password = 'Original'
t.updatePassword.newPassword = 'New Password'
t.updatePassword.comfirmPassword = 'Confirm'
t.updatePassword.validate = {}
t.updatePassword.validate.comfirmPassword = 'Confirm password is not consistent with new password input'

t.contentTabs = {}
t.contentTabs.closeCurrent = 'Close Current Tab'
t.contentTabs.closeOther = 'Close Other Tabs'
t.contentTabs.closeAll = 'Close All Tabs'

/* pages */
t.notFound = {}
t.notFound.desc = 'Sorry! <em>missing</em> on the page you visited...'
t.notFound.back = 'Previous Page'
t.notFound.home = 'Home'

t.login = {}
t.login.title = 'Sign In'
t.login.username = 'Username'
t.login.password = 'Password'
t.login.captcha = 'Verification Code'
t.login.demo = 'Demo'
t.login.copyright = 'leimingshop'

t.home = {}
t.home.desc = {}
t.home.desc.title = 'Project introduction'
t.home.desc.list = [
  'leimingshop-cloud is a micro service platform based on Spring cloud, providing enterprises with one-stop micro service solutions',
  'Based on the latest development of Spring Cloud finchley.sr2, Spring Cloud Gateway, Spring Boot 2.0, Mybatis, Element 2.0+',
  'The code style is elegant and simple, easy to understand, and meets the specification requirements of "alibaba Java development manual", which can be used as enterprise code specification',
  'Excellent menu function permissions, the front end can flexibly control the display of pages and buttons, and the back end can intercept unauthorized requests',
  'Good data access management, just add corresponding annotations without any other code, can achieve data filtering, data access purposes',
  'Flexible role permission management. When adding a role, the role permission can only be a subset of the creator permission, which can effectively prevent the permission from exceeding the authority',
  'Flexible log management, can view log, operation log, exception log, easy to audit and BUG location',
  'The flexible internationalization configuration supports simplified Chinese, traditional Chinese and English. If you need to add a new language, you only need to add a new language [i18n] file',
  'Flexible front-end dynamic routing, new pages do not need to modify the routing file, can also dynamically add TAB tags in the page'
]

/* modules */
t.model = {}
t.model.name = 'Name'
t.model.key = 'Information'
t.model.version = 'Version'
t.model.createTime = 'Create Time'
t.model.lastUpdateTime = 'Update Time'
t.model.design = 'Online Design'
t.model.deploy = 'Deployment'
t.model.description = 'Description'

t.process = {}
t.process.name = 'name'
t.process.key = 'Identification'
t.process.deployFile = 'Deploy process file'
t.process.id = 'Process ID'
t.process.deploymentId = 'Deployment ID'
t.process.version = 'Version'
t.process.resourceName = 'XML'
t.process.diagramResourceName = 'Image'
t.process.deploymentTime = 'Deployment Time'
t.process.active = 'Activate'
t.process.suspend = 'Hang'
t.process.convertToModel = 'Convert to model'

t.running = {}
t.running.id = 'Instance ID'
t.running.definitionKey = 'Define Key'
t.running.processDefinitionId = 'Define ID'
t.running.processDefinitionName = 'Define the name'
t.running.activityId = 'Current Link'
t.running.suspended = 'Whether Hang'
t.running.suspended0 = 'No'
t.running.suspended1 = 'Yes'

t.news = {}
t.news.title = 'Title'
t.news.pubDate = 'Publish Time'
t.news.createDate = 'Create Time'
t.news.content = 'Content'

t.schedule = {}
t.schedule.beanName = 'Bean Name'
t.schedule.beanNameTips = 'Spring bean name, eg: testTask'
t.schedule.pauseBatch = 'Pause'
t.schedule.resumeBatch = 'Recovery'
t.schedule.runBatch = 'Execution'
t.schedule.log = 'Log List'
t.schedule.params = 'Parameters'
t.schedule.cronExpression = 'Cron Expression'
t.schedule.cronExpressionTips = 'Example: 0 0 12 * * ?'
t.schedule.remark = 'Remarks'
t.schedule.status = 'Status'
t.schedule.status0 = 'Pause'
t.schedule.status1 = 'Normal'
t.schedule.statusLog0 = 'Failed'
t.schedule.statusLog1 = 'Success'
t.schedule.pause = 'Pause'
t.schedule.resume = 'Restore'
t.schedule.run = 'Execute'
t.schedule.jobId = 'Task ID'
t.schedule.times = 'Time-consuming (unit: milliseconds)'
t.schedule.createDate = 'Execution Time'

t.mail = {}
t.mail.name = 'Name'
t.mail.config = 'Mail Configuration'
t.mail.subject = 'Theme'
t.mail.createDate = 'Create Time'
t.mail.send = 'Send Mail'
t.mail.content = 'Content'
t.mail.smtp = 'SMTP'
t.mail.port = 'Port Number'
t.mail.username = 'Email Account'
t.mail.password = 'Mailbox Password'
t.mail.mailTo = 'Recipient'
t.mail.mailCc = 'Cc'
t.mail.params = 'Template Parameter'
t.mail.paramsTips = 'Example: {"code": "123456"}'
t.mail.templateId = 'Template ID'
t.mail.status = 'Status'
t.mail.status0 = 'Send Failed'
t.mail.status1 = 'Successfully Sent'
t.mail.mailFrom = 'Sender'
t.mail.createDate = 'Send Time'

t.sms = {}
t.sms.mobile = 'Mobile Number'
t.sms.status = 'Status'
t.sms.status0 = 'Send Failed'
t.sms.status1 = 'Successfully Sent'
t.sms.config = 'SMS Configuration'
t.sms.send = 'Send SMS'
t.sms.platform = 'platform Type'
t.sms.platform1 = 'Alibaba Cloud'
t.sms.platform2 = 'Tencent Cloud'
t.sms.params = 'Parameters'
t.sms.paramsTips = 'eg: {"code": "123456"}'
t.sms.params1 = 'Parameter 1'
t.sms.params2 = 'Parameter 2'
t.sms.params3 = 'Parameter 3'
t.sms.params4 = 'Parameter 4'
t.sms.createDate = 'Send Time'
t.sms.aliyunAccessKeyId = 'Key'
t.sms.aliyunAccessKeyIdTips = 'Alibaba Cloud AccessKeyId'
t.sms.aliyunAccessKeySecret = 'Secret'
t.sms.aliyunAccessKeySecretTips = 'Alibaba Cloud AccessKeySecret'
t.sms.aliyunSignName = 'SMS Signature'
t.sms.aliyunTemplateCode = 'SMS Template'
t.sms.aliyunTemplateCodeTips = 'SMS Template CODE'
t.sms.qcloudAppId = 'AppId'
t.sms.qcloudAppIdTips = 'Tencent Cloud AppId'
t.sms.qcloudAppKey = 'AppKey'
t.sms.qcloudAppKeyTips = 'Tencent Cloud AppKey'
t.sms.qcloudSignName = 'SMS Signature'
t.sms.qcloudTemplateId = 'SMS Template'
t.sms.qcloudTemplateIdTips = 'SMS template ID'

t.oss = {}
t.oss.config = 'Cloud Storage Configuration'
t.oss.upload = 'Upload File'
t.oss.url = 'URL Address'
t.oss.createDate = 'Create Time'
t.oss.type = 'Type'
t.oss.type1 = 'Seven Cows'
t.oss.type2 = 'Alibaba Cloud'
t.oss.type3 = 'Tencent Cloud'
t.oss.type4 = 'FastDFS'
t.oss.type5 = 'Local Upload'
t.oss.qiniuDomain = 'Domain Name'
t.oss.qiniuDomainTips = 'Seven cattle bound domain name'
t.oss.qiniuPrefix = 'Path Prefix'
t.oss.qiniuPrefixTips = 'Do not set default to empty'
t.oss.qiniuAccessKey = 'AccessKey'
t.oss.qiniuAccessKeyTips = 'Seven cattle AccessKey'
t.oss.qiniuSecretKey = 'SecretKey'
t.oss.qiniuSecretKeyTips = 'Seven Cow SecretKey'
t.oss.qiniuBucketName = 'Space Name'
t.oss.qiniuBucketNameTips = 'Seven cattle storage space name'
t.oss.aliyunDomain = 'Domain Name'
t.oss.aliyunDomainTips = 'Alibaba Cloud bound domain name, such as: http://cdn.leimingshop.io'
t.oss.aliyunPrefix = 'Path Prefix'
t.oss.aliyunPrefixTips = 'Do not set default to empty'
t.oss.aliyunEndPoint = 'EndPoint'
t.oss.aliyunEndPointTips = 'Ali Cloud EndPoint'
t.oss.aliyunAccessKeyId = 'AccessKeyId'
t.oss.aliyunAccessKeyIdTips = 'Alibaba Cloud AccessKeyId'
t.oss.aliyunAccessKeySecret = 'AccessKeySecret'
t.oss.aliyunAccessKeySecretTips = 'Alibaba Cloud AccessKeySecret'
t.oss.aliyunBucketName = 'BucketName'
t.oss.aliyunBucketNameTips = 'Alibaba Cloud BucketName'
t.oss.qcloudDomain = 'Domain Name'
t.oss.qcloudDomainTips = 'Tencent cloud bound domain name'
t.oss.qcloudPrefix = 'Path Prefix'
t.oss.qcloudPrefixTips = 'Do not set default to empty'
t.oss.qcloudAppId = 'AppId'
t.oss.qcloudAppIdTips = 'Tencent Cloud AppId'
t.oss.qcloudSecretId = 'SecretId'
t.oss.qcloudSecretIdTips = 'Tencent Cloud SecretD'
t.oss.qcloudSecretKey = 'SecretKey'
t.oss.qcloudSecretKeyTips = 'Tencent Cloud SecretKey'
t.oss.qcloudBucketName = 'BucketName'
t.oss.qcloudBucketNameTips = 'Tencent Cloud BucketName'
t.oss.qcloudRegion = 'Affiliate'
t.oss.qcloudRegionTips = 'Please Select'
t.oss.qcloudRegionBeijing1 = 'Beijing District 1 (North China)'
t.oss.qcloudRegionBeijing = 'Beijing'
t.oss.qcloudRegionShanghai = 'Shanghai (East China)'
t.oss.qcloudRegionGuangzhou = 'Guangzhou (South China)'
t.oss.qcloudRegionChengdu = 'Chengdu (Southwest)'
t.oss.qcloudRegionChongqing = 'Chongqing'
t.oss.qcloudRegionSingapore = 'Singapore'
t.oss.qcloudRegionHongkong = 'HongKong'
t.oss.qcloudRegionToronto = 'Toronto'
t.oss.qcloudRegionFrankfurt = 'Frankfurt'
t.oss.localDomain = 'Domain Name'
t.oss.localDomainTips = 'Binded domain name, eg http://cdn.leimingshop.io'
t.oss.fastdfsDomain = 'Domain Name'
t.oss.fastdfsDomainTips = 'Binded domain name, eg http://cdn.leimingshop.io'
t.oss.localPrefix = 'Path Prefix'
t.oss.localPrefixTips = 'Do not set default to empty'
t.oss.localPath = 'Storage Directory'
t.oss.localPathTips = 'eg: D:/upload'

t.dept = {}
t.dept.name = 'Name'
t.dept.parentName = 'Superior'
t.dept.sort = 'Sort'
t.dept.parentNameDefault = 'Top Department'

t.dict = {}
t.dict.dictName = 'Name'
t.dict.dictType = 'Type'
t.dict.dictValue = 'Value'
t.dict.sort = 'Sort'
t.dict.remark = 'Remarks'
t.dict.createDate = 'Create Date'

t.logError = {}
t.logError.module = 'Module Name'
t.logError.requestUri = 'Request URI'
t.logError.requestMethod = 'Request Method'
t.logError.requestParams = 'Request Parameters'
t.logError.ip = 'IP'
t.logError.userAgent = 'User Agent'
t.logError.createDate = 'Create Date'
t.logError.errorInfo = 'Exception'

t.logLogin = {}
t.logLogin.creatorName = 'Username'
t.logLogin.status = 'Status'
t.logLogin.status0 = 'Failed'
t.logLogin.status1 = 'Success'
t.logLogin.status2 = 'Locked'
t.logLogin.operation = 'User Action'
t.logLogin.operation0 = 'Login'
t.logLogin.operation1 = 'Exit'
t.logLogin.ip = 'IP'
t.logLogin.userAgent = 'User-Agent'
t.logLogin.createDate = 'Create Date'

t.logOperation = {}
t.logOperation.module = 'Module Name'
t.logOperation.status = 'Status'
t.logOperation.status0 = 'Failed'
t.logOperation.status1 = 'Success'
t.logOperation.creatorName = 'Username'
t.logOperation.operation = 'User Action'
t.logOperation.requestUri = 'Request URI'
t.logOperation.requestMethod = 'Request Mode'
t.logOperation.requestParams = 'Request Parameters'
t.logOperation.requestTime = 'Request Duration'
t.logOperation.ip = 'IP'
t.logOperation.userAgent = 'User-Agent'
t.logOperation.createDate = 'Create Date'

t.menu = {}
t.menu.name = 'Name'
t.menu.icon = 'Icon'
t.menu.type = 'Type'
t.menu.type0 = 'Menu'
t.menu.type1 = 'Button'
t.menu.sort = 'Sort'
t.menu.url = 'Route'
t.menu.permissions = 'Auth ID'
t.menu.permissionsTips = 'eg: sys:menu:save'
t.menu.parentName = 'Superior'
t.menu.parentNameDefault = 'Top Menu'
t.menu.resource = 'Auth Resources'
t.menu.resourceUrl = 'Resource URL'
t.menu.resourceMethod = 'Request Method'
t.menu.resourceAddItem = 'Add an Item'

t.params = {}
t.params.paramCode = 'Code'
t.params.paramValue = 'Value'
t.params.remark = 'Remarks'

t.role = {}
t.role.name = 'Name'
t.role.remark = 'Remarks'
t.role.createDate = 'Create Date'
t.role.menuList = 'Menu Scope'
t.role.deptList = 'Data Scope'

t.user = {}
t.user.username = 'Username'
t.user.deptName = 'Department'
t.user.email = 'Email'
t.user.mobile = 'Mobile'
t.user.status = 'Status'
t.user.status0 = 'Disable'
t.user.status1 = 'Enable'
t.user.createDate = 'Create Date'
t.user.password = 'Password'
t.user.comfirmPassword = 'Confirm'
t.user.realName = 'Real Name'
t.user.gender = 'Gender'
t.user.gender0 = 'Male'
t.user.gender1 = 'Female'
t.user.gender2 = 'Secure'
t.user.roleIdList = 'Role Config'
t.user.validate = {}
t.user.validate.comfirmPassword = 'Confirm password is not consistent with password input'

export default t
