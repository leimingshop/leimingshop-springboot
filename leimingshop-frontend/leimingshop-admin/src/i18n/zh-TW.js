const t = {}

t.loading = '加載中...'

t.brand = {}
t.brand.lg = '雷铭微服務平台'
t.brand.mini = '雷铭'

t.add = '新增'
t.delete = '刪除'
t.deleteBatch = '刪除'
t.update = '修改'
t.query = '查詢'
t.export = '導出'
t.handle = '操作'
t.confirm = '確定'
t.cancel = '取消'
t.logout = '退出'

t.prompt = {}
t.prompt.title = '提示'
t.prompt.info = '確定進行[{handle}]操作?'
t.prompt.success = '操作成功'
t.prompt.failed = '操作失敗'
t.prompt.deleteBatch = '請選擇刪除項'

t.validate = {}
t.validate.required = '必填項不能為空'
t.validate.format = '{attr}格式錯誤'

t.upload = {}
t.upload.text = '將文件拖到此處，或<em>點擊上傳</em>'
t.upload.tip = '只支持{format}格式文件！ '
t.upload.button = '點擊上傳'

t.datePicker = {}
t.datePicker.range = '至'
t.datePicker.start = '開始日期'
t.datePicker.end = '結束日期'

t.fullscreen = {}
t.fullscreen.prompt = '您的瀏覽器不支持此操作'

t.updatePassword = {}
t.updatePassword.title = '修改密碼'
t.updatePassword.username = '賬號'
t.updatePassword.password = '原密碼'
t.updatePassword.newPassword = '新密碼'
t.updatePassword.comfirmPassword = '確認密碼'
t.updatePassword.validate = {}
t.updatePassword.validate.comfirmPassword = '確認密碼與新密碼輸入不一致'

t.contentTabs = {}
t.contentTabs.closeCurrent = '關閉當前標籤頁'
t.contentTabs.closeOther = '關閉其它標籤頁'
t.contentTabs.closeAll = '關閉全部標籤頁'

/* 頁面 */
t.notFound = {}
t.notFound.desc = '抱歉！您訪問的頁面<em>失聯</em>啦...'
t.notFound.back = '上一頁'
t.notFound.home = '首頁'

t.login = {}
t.login.title = '登錄'
t.login.username = '用戶名'
t.login.password = '密碼'
t.login.captcha = '驗證碼'
t.login.demo = '在線演示'
t.login.copyright = '雷铭開源'

t.home = {}
t.home.desc = {}
t.home.desc.title = '項目介紹'
t.home.desc.list = [
  '雷铭微服務平台(leimingshop-cloud)，是基於Spring Cloud打造的微服務平台，為企業提供一站式微服務解決方案',
  '基於最新的Spring Cloud Finchley.SR2、Spring Cloud Gateway、Spring Boot 2.0、Mybatis、Element 2.0+開發',
  '代碼風格優雅簡潔、通俗易懂，且符合《阿里巴巴Java開發手冊》規範要求，可作為企業代碼規範',
  '優秀的菜單功能權限，前端可靈活控制頁面及按鈕的展示，後端可對未授權的請求進行攔截',
  '優秀的數據權限管理，只需增加相應註解，無需其他任何代碼，即可實現數據過濾，達到數據權限目的',
  '靈活的角色權限管理，新增角色時，角色權限只能是創建者權限的子集，可有效防止權限越權',
  '靈活的日誌管理，可查看登錄日誌、操作日誌、異常日誌，方便審計及BUG定位',
  '靈活的國際化配置，目前已支持簡體中文、繁體中文、English，如需增加新語言，只需增加新語言[i18n]文件即可',
  '靈活的前端動態路由，新增頁面無需修改路由文件，也可在頁面動態新增tab標籤'
]

/* 模塊 */
t.model = {}
t.model.name = '名稱'
t.model.key = '標識'
t.model.version = '版本'
t.model.createTime = '創建時間'
t.model.lastUpdateTime = '更新時間'
t.model.design = '在線設計'
t.model.deploy = '部署'
t.model.description = '描述'

t.process = {}
t.process.name = '名稱'
t.process.key = '標識'
t.process.deployFile = '部署流程文件'
t.process.id = '流程ID'
t.process.deploymentId = '部署ID'
t.process.version = '版本'
t.process.resourceName = 'XML'
t.process.diagramResourceName = '圖片'
t.process.deploymentTime = '部署時間'
t.process.active = '激活'
t.process.suspend = '掛起'
t.process.convertToModel = '轉換為模型'

t.running = {}
t.running.id = '實例ID'
t.running.definitionKey = '定義Key'
t.running.processDefinitionId = '定義ID'
t.running.processDefinitionName = '定義名稱'
t.running.activityId = '當前環節'
t.running.suspended = '是否掛起'
t.running.suspended0 = '否'
t.running.suspended1 = '是'

t.news = {}
t.news.title = '標題'
t.news.pubDate = '發佈時間'
t.news.createDate = '創建時間'
t.news.content = '內容'

t.schedule = {}
t.schedule.beanName = 'bean名稱'
t.schedule.beanNameTips = 'spring bean名稱, 如: testTask'
t.schedule.pauseBatch = '暫停'
t.schedule.resumeBatch = '恢復'
t.schedule.runBatch = '執行'
t.schedule.log = '日誌列表'
t.schedule.params = '參數'
t.schedule.cronExpression = 'cron表達式'
t.schedule.cronExpressionTips = '如: 0 0 12 * * ?'
t.schedule.remark = '備註'
t.schedule.status = '狀態'
t.schedule.status0 = '暫停'
t.schedule.status1 = '正常'
t.schedule.statusLog0 = '失敗'
t.schedule.statusLog1 = '成功'
t.schedule.pause = '暫停'
t.schedule.resume = '恢復'
t.schedule.run = '執行'
t.schedule.jobId = '任務ID'
t.schedule.times = '耗時(單位: 毫秒)'
t.schedule.createDate = '執行時間'

t.mail = {}
t.mail.name = '名稱'
t.mail.config = '郵件配置'
t.mail.subject = '主題'
t.mail.createDate = '創建時間'
t.mail.send = '發送郵件'
t.mail.content = '內容'
t.mail.smtp = 'SMTP'
t.mail.port = '端口號'
t.mail.username = '郵箱賬號'
t.mail.password = '郵箱密碼'
t.mail.mailTo = '收件人'
t.mail.mailCc = '抄送'
t.mail.params = '模板參數'
t.mail.paramsTips = '如：{"code": "123456"}'
t.mail.templateId = '模版ID'
t.mail.status = '狀態'
t.mail.status0 = '發送失敗'
t.mail.status1 = '發送成功'
t.mail.mailFrom = '發送者'
t.mail.createDate = '發送時間'

t.sms = {}
t.sms.mobile = '手機號'
t.sms.status = '狀態'
t.sms.status0 = '發送失敗'
t.sms.status1 = '發送成功'
t.sms.config = '短信配置'
t.sms.send = '發送短信'
t.sms.platform = '平台類型'
t.sms.platform1 = '阿里雲'
t.sms.platform2 = '騰訊雲'
t.sms.params = '參數'
t.sms.paramsTips = '如：{"code": "123456"}'
t.sms.params1 = '參數1'
t.sms.params2 = '參數2'
t.sms.params3 = '參數3'
t.sms.params4 = '參數4'
t.sms.createDate = '發送時間'
t.sms.aliyunAccessKeyId = 'Key'
t.sms.aliyunAccessKeyIdTips = '阿里雲AccessKeyId'
t.sms.aliyunAccessKeySecret = 'Secret'
t.sms.aliyunAccessKeySecretTips = '阿里雲AccessKeySecret'
t.sms.aliyunSignName = '短信簽名'
t.sms.aliyunTemplateCode = '短信模板'
t.sms.aliyunTemplateCodeTips = '短信模板CODE'
t.sms.qcloudAppId = 'AppId'
t.sms.qcloudAppIdTips = '騰訊雲AppId'
t.sms.qcloudAppKey = 'AppKey'
t.sms.qcloudAppKeyTips = '騰訊雲AppKey'
t.sms.qcloudSignName = '短信簽名'
t.sms.qcloudTemplateId = '短信模板'
t.sms.qcloudTemplateIdTips = '短信模板ID'

t.oss = {}
t.oss.config = '雲存儲配置'
t.oss.upload = '上傳文件'
t.oss.url = 'URL地址'
t.oss.createDate = '創建時間'
t.oss.type = '類型'
t.oss.type1 = '七牛'
t.oss.type2 = '阿里雲'
t.oss.type3 = '騰訊雲'
t.oss.type4 = 'FastDFS'
t.oss.type5 = '本地上傳'
t.oss.qiniuDomain = '域名'
t.oss.qiniuDomainTips = '七牛綁定的域名'
t.oss.qiniuPrefix = '路徑前綴'
t.oss.qiniuPrefixTips = '不設置默認為空'
t.oss.qiniuAccessKey = 'AccessKey'
t.oss.qiniuAccessKeyTips = '七牛AccessKey'
t.oss.qiniuSecretKey = 'SecretKey'
t.oss.qiniuSecretKeyTips = '七牛SecretKey'
t.oss.qiniuBucketName = '空間名'
t.oss.qiniuBucketNameTips = '七牛存儲空間名'
t.oss.aliyunDomain = '域名'
t.oss.aliyunDomainTips = '阿里雲綁定的域名，如：http://cdn.leimingshop.io'
t.oss.aliyunPrefix = '路徑前綴'
t.oss.aliyunPrefixTips = '不設置默認為空'
t.oss.aliyunEndPoint = 'EndPoint'
t.oss.aliyunEndPointTips = '阿里雲EndPoint'
t.oss.aliyunAccessKeyId = 'AccessKeyId'
t.oss.aliyunAccessKeyIdTips = '阿里雲AccessKeyId'
t.oss.aliyunAccessKeySecret = 'AccessKeySecret'
t.oss.aliyunAccessKeySecretTips = '阿里雲AccessKeySecret'
t.oss.aliyunBucketName = 'BucketName'
t.oss.aliyunBucketNameTips = '阿里雲BucketName'
t.oss.qcloudDomain = '域名'
t.oss.qcloudDomainTips = '騰訊雲綁定的域名'
t.oss.qcloudPrefix = '路徑前綴'
t.oss.qcloudPrefixTips = '不設置默認為空'
t.oss.qcloudAppId = 'AppId'
t.oss.qcloudAppIdTips = '騰訊雲AppId'
t.oss.qcloudSecretId = 'SecretId'
t.oss.qcloudSecretIdTips = '騰訊雲SecretId'
t.oss.qcloudSecretKey = 'SecretKey'
t.oss.qcloudSecretKeyTips = '騰訊雲SecretKey'
t.oss.qcloudBucketName = 'BucketName'
t.oss.qcloudBucketNameTips = '騰訊雲BucketName'
t.oss.qcloudRegion = '所屬地區'
t.oss.qcloudRegionTips = '請選擇'
t.oss.qcloudRegionBeijing1 = '北京一區（華北）'
t.oss.qcloudRegionBeijing = '北京'
t.oss.qcloudRegionShanghai = '上海（華東）'
t.oss.qcloudRegionGuangzhou = '廣州（華南）'
t.oss.qcloudRegionChengdu = '成都（西南）'
t.oss.qcloudRegionChongqing = '重慶'
t.oss.qcloudRegionSingapore = '新加坡'
t.oss.qcloudRegionHongkong = '香港'
t.oss.qcloudRegionToronto = '多倫多'
t.oss.qcloudRegionFrankfurt = '法蘭克福'
t.oss.localDomain = '域名'
t.oss.localDomainTips = '綁定的域名，如：http://cdn.leimingshop.io'
t.oss.fastdfsDomain = '域名'
t.oss.fastdfsDomainTips = '綁定的域名，如：http://cdn.leimingshop.io'
t.oss.localPrefix = '路徑前綴'
t.oss.localPrefixTips = '不設置默認為空'
t.oss.localPath = '存儲目錄'
t.oss.localPathTips = '如：D:/upload'

t.dept = {}
t.dept.name = '名稱'
t.dept.parentName = '上級部門'
t.dept.sort = '排序'
t.dept.parentNameDefault = '一級部門'

t.dict = {}
t.dict.dictName = '名稱'
t.dict.dictType = '類型'
t.dict.dictValue = '值'
t.dict.sort = '排序'
t.dict.remark = '備註'
t.dict.createDate = '創建時間'

t.logError = {}
t.logError.module = '模塊名稱'
t.logError.requestUri = '請求URI'
t.logError.requestMethod = '請求方式'
t.logError.requestParams = '請求參數'
t.logError.ip = '操作IP'
t.logError.userAgent = '用戶代理'
t.logError.createDate = '創建時間'
t.logError.errorInfo = '異常信息'

t.logLogin = {}
t.logLogin.creatorName = '用戶名'
t.logLogin.status = '狀態'
t.logLogin.status0 = '失敗'
t.logLogin.status1 = '成功'
t.logLogin.status2 = '賬號已鎖定'
t.logLogin.operation = '操作類型'
t.logLogin.operation0 = '登錄'
t.logLogin.operation1 = '退出'
t.logLogin.ip = '操作IP'
t.logLogin.userAgent = 'User-Agent'
t.logLogin.createDate = '創建時間'

t.logOperation = {}
t.logOperation.module = '模塊名稱'
t.logOperation.status = '狀態'
t.logOperation.status0 = '失敗'
t.logOperation.status1 = '成功'
t.logOperation.creatorName = '用戶名'
t.logOperation.operation = '用戶操作'
t.logOperation.requestUri = '請求URI'
t.logOperation.requestMethod = '請求方式'
t.logOperation.requestParams = '請求參數'
t.logOperation.requestTime = '請求時長'
t.logOperation.ip = '操作IP'
t.logOperation.userAgent = 'User-Agent'
t.logOperation.createDate = '創建時間'

t.menu = {}
t.menu.name = '名稱'
t.menu.icon = '圖標'
t.menu.type = '類型'
t.menu.type0 = '菜單'
t.menu.type1 = '按鈕'
t.menu.sort = '排序'
t.menu.url = '路由'
t.menu.permissions = '授權標識'
t.menu.permissionsTips = '如: sys:menu:save'
t.menu.parentName = '上級菜單'
t.menu.parentNameDefault = '一級菜單'
t.menu.resource = '授權資源'
t.menu.resourceUrl = '資源URL'
t.menu.resourceMethod = '請求方式'
t.menu.resourceAddItem = '添加一項'

t.params = {}
t.params.paramCode = '編碼'
t.params.paramValue = '值'
t.params.remark = '備註'

t.role = {}
t.role.name = '名稱'
t.role.remark = '備註'
t.role.createDate = '創建時間'
t.role.menuList = '菜單授權'
t.role.deptList = '數據授權'

t.user = {}
t.user.username = '用戶名'
t.user.deptName = '所屬部門'
t.user.email = '郵箱'
t.user.mobile = '手機號'
t.user.status = '狀態'
t.user.status0 = '停用'
t.user.status1 = '正常'
t.user.createDate = '創建時間'
t.user.password = '密碼'
t.user.comfirmPassword = '確認密碼'
t.user.realName = '真實姓名'
t.user.gender = '性別'
t.user.gender0 = '男'
t.user.gender1 = '女'
t.user.gender2 = '保密'
t.user.roleIdList = '角色配置'
t.user.validate = {}
t.user.validate.comfirmPassword = '確認密碼與密碼輸入不一致'

export default t
