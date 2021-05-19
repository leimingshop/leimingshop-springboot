/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.setting.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.dao.SettingDao;
import com.leimingtech.dto.setting.*;
import com.leimingtech.entity.setting.SettingEntity;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.dto.seckill.SeckillSessionDTO;
import com.leimingtech.modules.service.seckill.SeckillSessionService;
import com.leimingtech.redis.SysSettingRedis;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统设置表
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */
@Service

@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SettingServiceImpl extends CrudServiceImpl<SettingDao, SettingEntity, SettingDTO> implements SettingService {

    @Resource
    private SettingDao settingDao;

    @Autowired
    private SysSettingRedis sysSettingRedis;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SeckillSessionService seckillSessionService;

    /**
     * leiming-cloud，代码生成器 自带的查询方法
     *
     * @param params
     * @return
     */
    @Override
    public QueryWrapper<SettingEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<SettingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 用于查询系统设置的不同的设置的内容
     *
     * @param settingName: 模块名称
     * @return
     */
    @Override

    public SettingDTO queryByName(@RequestParam("settingName") String settingName) {

        SettingEntity entity = settingDao.queryByName(settingName);

        return ConvertUtils.sourceToTarget(entity, SettingDTO.class);
    }

    /**
     * 根据名称查询系统设置
     *
     * @param settingName
     * @return
     */
    @Override

    public String queryRedisByName(@RequestParam("settingName") String settingName) {
        String message = (String) sysSettingRedis.get(settingName);
        if (StringUtils.isBlank(message)) {
            message = settingDao.queryRedisByName(settingName);
            sysSettingRedis.set(settingName, message);
        }
        return message;
    }

    /**
     * 更新系统设置中的设置内容
     *
     * @param settingInfo 新的站点的信息 settingName 站点模块类型
     * @param settingName
     */
    @Override

    public void updateSite(@RequestParam("settingInfo") String settingInfo,
                           @RequestParam("settingName") String settingName) {

        log.info("修改站点信息 --> {} ", settingInfo);

        settingDao.updateSite(settingInfo, settingName);


    }

    /**
     * @return com.leimingtech.dto.setting.SettingDTO
     * @Description 根据id获取设置信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:55 2019-05-27
     */

    @Override
    public SettingDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @return void
     * @Description 保存设置信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 14:55 2019-05-27
     */

    @Override
    public void save(@RequestBody SettingDTO dto) {
        super.save(dto);
    }

    /**
     * 获取网站设置的系统设置的站点信息
     */
    @Override

    public SettingUpdateDTO getSite() {

        SettingDTO data = this.queryByName(SettingsEnum.SITE.value());

        return JSON.parseObject(data.getValue(), SettingUpdateDTO.class);
    }

    /**
     * 新增/修改网站设置的系统设置的站点信息
     *
     * @param dto
     * @return
     */
    @Override

    public void putSite(@RequestBody SettingUpdateDTO dto) {

        String siteInfo = JSON.toJSONString(dto);

        sysSettingRedis.set(SettingsEnum.SITE.value(), siteInfo);

        this.updateSite(siteInfo, SettingsEnum.SITE.value());

    }


    /**
     * 获取网站设置的系统设置的短信设置
     *
     * @param
     * @return
     */
    @Override

    @ApiOperation("获取网站设置的系统设置的短信设置")
    public SettingMessageDTO getShortMessageSet() {

        SettingDTO data = this.queryByName(SettingsEnum.MESSAGE.value());

        return JSON.parseObject(data.getValue(), SettingMessageDTO.class);
    }

    @Override

    @ApiOperation("修改网站设置的系统设置的短信设置")
    public void saveMessageSetting(@RequestBody SettingMessageDTO dto) {

        String messageInfo = JSON.toJSONString(dto);

        this.updateSite(messageInfo, SettingsEnum.MESSAGE.value());

    }

    /**
     * @Author weixianchun
     * @Description 获取用户默认头像设置
     * @Date 2019/11/4 16:21
     * @Return com.leimingtech.dto.setting.SettingDefaultAvatarsDTO
     * @version 1.0
     */

    @Override
    public SettingDefaultAvatarsDTO getDefaultAvatarsSet() {
        SettingDTO data = this.queryByName(SettingsEnum.DEFAULT_AVATARS.value());
        return JSON.parseObject(data.getValue(), SettingDefaultAvatarsDTO.class);
    }

    /**
     * @Author weixianchun
     * @Description 修改用户默认头像设置
     * @Date 2019/11/4 16:21
     * @version 1.0
     */

    @Override
    public void updateDefaultAvatarsSet(@RequestBody SettingDefaultAvatarsDTO defaultAvatarsDTO) {

        String messageInfo = JSON.toJSONString(defaultAvatarsDTO);

        this.updateSite(messageInfo, SettingsEnum.DEFAULT_AVATARS.value());
    }

    /**
     * 获取网站设置的系统设置的商品审核设置
     *
     * @param
     * @return
     */
    @Override

    @ApiOperation("获取网站设置的系统设置的商品审核设置")
    public SettingGoodsAuditDTO getGoodsAuditSet() {

        SettingDTO data = this.queryByName(SettingsEnum.GOODS_AUDIT.value());

        return JSON.parseObject(data.getValue(), SettingGoodsAuditDTO.class);
    }


    @Override

    @ApiOperation("修改网站设置的系统设置的商品审核设置")
    public void updateGoodsAuditSet(@RequestBody SettingGoodsAuditDTO dto) {

        String messageInfo = JSON.toJSONString(dto);
        sysSettingRedis.set(SettingsEnum.GOODS_AUDIT.value(), messageInfo);
        this.updateSite(messageInfo, SettingsEnum.GOODS_AUDIT.value());

    }

    /**
     * 获取网站设置的系统设置的公告提示设置
     *
     * @param
     * @return
     */
    @Override

    @ApiOperation("获取网站设置的系统设置的公告提示设置")
    public SettingAnnouncementTipsDTO getAnnouncementTipsSet() {

        SettingDTO data = this.queryByName(SettingsEnum.ANNOUNCEMENT_TIPS.value());

        return JSON.parseObject(data.getValue(), SettingAnnouncementTipsDTO.class);
    }

    /**
     * @param dto
     * @return
     */
    @Override

    @ApiOperation("修改网站设置的系统设置的公告提示设置")
    public void updateAnnouncementTips(@RequestBody SettingAnnouncementTipsDTO dto) {
        //效验数据
        //todo

        String messageInfo = JSON.toJSONString(dto);

        this.updateSite(messageInfo, SettingsEnum.ANNOUNCEMENT_TIPS.value());
    }

    /**
     * 获取网站设置的高级设置
     *
     * @param
     * @return
     */
    @Override

    @ApiOperation("获取网站设置的订单设置")
    public SettingSeniorDTO getSeniorSet() {

        SettingDTO data = this.queryByName(SettingsEnum.SENIOR.value());

        return JSON.parseObject(data.getValue(), SettingSeniorDTO.class);
    }

    @Override

    @ApiOperation("修改网站设置的订单设置")
    public void updateSeniorSet(@RequestBody SettingSeniorDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        String messageInfo = JSON.toJSONString(dto);
        sysSettingRedis.set(SettingsEnum.SENIOR.value(), messageInfo);
        this.updateSite(messageInfo, SettingsEnum.SENIOR.value());

    }

    /**
     * 获取发票设置
     *
     * @param
     * @return
     */
    @Override

    @ApiOperation("获取发票设置")
    public SettingInvoiceDTO getInvoice() {

        SettingDTO data = this.queryByName(SettingsEnum.INVOICE.value());

        return JSON.parseObject(data.getValue(), SettingInvoiceDTO.class);
    }

    /**
     * 更新发票设置
     *
     * @param dto
     */
    @Override

    @ApiOperation("更新发票设置")
    public void updateInvoice(@RequestBody SettingInvoiceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        String messageInfo = JSON.toJSONString(dto);
        sysSettingRedis.set(SettingsEnum.INVOICE.value(), messageInfo);
        this.updateSite(messageInfo, SettingsEnum.INVOICE.value());

    }

    /**
     * 获取售后设置
     *
     * @param
     * @return
     */
    @Override

    public SettingAftersaleDTO getAftersaleDTO() {
        SettingDTO data = this.queryByName(SettingsEnum.AFTERSALE.value());

        return JSON.parseObject(data.getValue(), SettingAftersaleDTO.class);
    }

    /**
     * 更新售后设置
     *
     * @param
     * @return
     */
    @Override

    public void updateAftersaleDTO(@RequestBody SettingAftersaleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        String messageInfo = JSON.toJSONString(dto);

        this.updateSite(messageInfo, SettingsEnum.AFTERSALE.value());
        sysSettingRedis.set(SettingsEnum.AFTERSALE.value(), messageInfo);
    }

    /**
     * 获取快递设置
     *
     * @param
     * @return
     */
    @Override

    public SettingSeniorExpressDTO getExpressSet() {
        SettingDTO data = this.queryByName(SettingsEnum.EXPRESS.value());

        return JSON.parseObject(data.getValue(), SettingSeniorExpressDTO.class);
    }

    /**
     * 更新快递设置
     *
     * @param
     * @return
     */
    @Override

    public void updateExpressSet(@RequestBody SettingSeniorExpressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        String messageInfo = JSON.toJSONString(dto);
        sysSettingRedis.set(SettingsEnum.EXPRESS.value(), messageInfo);
        this.updateSite(messageInfo, SettingsEnum.EXPRESS.value());
    }

    /**
     * 获取结算设置
     *
     * @param
     * @return
     */
    @Override

    public SettingSeniorSettlementDTO getSettlementSet() {
        SettingDTO data = this.queryByName(SettingsEnum.SETTLEMENT.value());

        return JSON.parseObject(data.getValue(), SettingSeniorSettlementDTO.class);
    }

    /**
     * 更新结算设置
     *
     * @param
     * @return
     */
    @Override

    public void updateSettlementSet(@RequestBody SettingSeniorSettlementDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        String messageInfo = JSON.toJSONString(dto);
        sysSettingRedis.set(SettingsEnum.SETTLEMENT.value(), messageInfo);
        this.updateSite(messageInfo, SettingsEnum.SETTLEMENT.value());
    }

    /**
     * 获取对账设置
     *
     * @return
     */

    @Override
    public OrderBillSettingDTO getOrderBill() {
        SettingDTO data = this.queryByName(SettingsEnum.ORDER_BILL.value());
        String substringAfter = StringUtils.substringAfter(data.getValue(), "*");
        if (StringUtils.isNotBlank(substringAfter)) {
            return JSON.parseObject(substringAfter, OrderBillSettingDTO.class);
        }

        return JSON.parseObject(data.getValue(), OrderBillSettingDTO.class);
    }

    /**
     * 修改对账设置
     *
     * @return
     */

    @Override
    public void updateOrderBill(@RequestBody OrderBillSettingDTO orderBillSettingDTO) {
        if (orderBillSettingDTO.getType() == 1) {
            Date date = DateUtils.initDateByMonth();
            orderBillSettingDTO.setCreateDate(DateUtils.addDateMonths(date, 1));
        }
        String messageInfo = JSON.toJSONString(orderBillSettingDTO);
        String result = null;
        SettingDTO data = this.queryByName(SettingsEnum.ORDER_BILL.value());

        if (StringUtils.isBlank(data.getValue())) {
            result = messageInfo;
        } else {
            String substringBefore = StringUtils.substringBefore(data.getValue(), "*");
            result = substringBefore + "*" + messageInfo;
        }
        this.updateSite(result, SettingsEnum.ORDER_BILL.value());
    }

    /**
     * 功能描述：
     * <获取秒杀设置>
     *
     * @date 2020/3/6 10:46
     * @author 刘远杰
     **/
    @Override

    public SettingSeckillDTO getSeckillSetting() {
        // 查询秒杀设置
        SettingDTO data = this.queryByName(SettingsEnum.SECKILL.value());
        return JSON.parseObject(data.getValue(), SettingSeckillDTO.class);
    }

    /**
     * 功能描述：
     * <修改秒杀设置>
     *
     * @param dto 秒杀设置信息
     * @date 2020/3/6 10:48
     * @author 刘远杰
     **/
    @Override

    public void updateSeckillSetting(@RequestBody SettingSeckillDTO dto) {
        Date now = new Date();
        List<SeckillSessionDTO> sessionList = new ArrayList<>();

        // 1.对比已存在预设场次 新增场次保存
        List<Date> activityStartDate = dto.getActivityStartDate();
        Integer presetDays = dto.getPresetDays();
        // 封装新增场次集合
        for (int i = 0; i <= presetDays; i++) {
            Map<String, Object> params = new HashMap<>(2);
            String activityStartDateStart;
            String activityStartDateEnd;
            if (i == 0) {
                // 时间区间;  今天 - （明天）
                activityStartDateStart = DateUtils.format(now, DateUtils.DATE_PATTERN);
                activityStartDateEnd = DateUtils.format(DateUtils.addDateDays(now, 1), DateUtils.DATE_PATTERN);
            } else {
                // 时间区间;  当前 - （当前 + 1天）
                activityStartDateStart = DateUtils.format(DateUtils.addDateDays(now, i), DateUtils.DATE_PATTERN);
                activityStartDateEnd = DateUtils.format(DateUtils.addDateDays(now, i + 1), DateUtils.DATE_PATTERN);
            }
            params.put("activityStartDateStart", activityStartDateStart);
            params.put("activityStartDateEnd", activityStartDateEnd);
            // 查询已预设场次
            List<SeckillSessionDTO> preSessionList = seckillSessionService.list(params);

            if (CollectionUtils.isNotEmpty(preSessionList)) {
                for (Date startDate : activityStartDate) {
                    //场次开始时间
                    Date date = new Date(DateUtils.addDateDays(DateUtils.parse(DateUtils.format(now, DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN), i).getTime() + startDate.getTime() + 28800000);
                    boolean repeatFlag = false;
                    for (SeckillSessionDTO preSession : preSessionList) {
                        if (preSession.getActivityStartDate().compareTo(date) == 0) {
                            repeatFlag = true;
                            break;
                        }
                    }

                    if (!repeatFlag) {
                        // 不重复 添加场次
                        if (!date.before(now)) {
                            SeckillSessionDTO session = createSeckillSession(dto, date);
                            // 添加到新增秒杀场次集合
                            sessionList.add(session);
                        }
                    }
                }
            } else {
                for (Date startDate : activityStartDate) {
                    //场次开始时间
                    Date date = new Date(DateUtils.addDateDays(DateUtils.parse(DateUtils.format(now, DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN), i).getTime() + startDate.getTime() + 28800000);
                    if (!date.before(now)) {
                        SeckillSessionDTO session = createSeckillSession(dto, date);
                        // 添加到新增秒杀场次集合
                        sessionList.add(session);
                    }
                }
            }

        }

        // 3.保存秒杀场次
        Boolean sessionFlag = seckillSessionService.saveBatch(sessionList);
        if (!sessionFlag) {
            throw new RuntimeException("保存秒杀场次异常");
        }

        // 4.保存场次设置
        String seckillSettingJson = JSON.toJSONString(dto);
        this.updateSite(seckillSettingJson, SettingsEnum.SECKILL.value());

        // 5.查询当前时间及以后结束的场次维护到redis
        Map<String, Object> params = new HashMap<>(1);
        String activityStartDateStart = DateUtils.format(DateUtils.addDateDays(now, -3), DateUtils.DATE_TIME_PATTERN);
        params.put("activityStartDateStart", activityStartDateStart);
        List<SeckillSessionDTO> sessions = seckillSessionService.list(params);
        // redis维护未结束场次
        redisUtils.set(ActivityRedisConstants.SECKILL_SESSION, sessions, -1);

        // 6.redis保存秒杀设置信息
        sysSettingRedis.set(SettingsEnum.SECKILL.value(), seckillSettingJson);

    }

    /**
     * 功能描述：
     * <创建秒杀场次>
     *
     * @param dto  秒杀设置
     * @param date 场次开始时间
     * @return
     * @date 2020/3/6 19:46
     * @author 刘远杰
     **/
    private SeckillSessionDTO createSeckillSession(SettingSeckillDTO dto, Date date) {
        // 封装秒杀场次数据
        SeckillSessionDTO session = new SeckillSessionDTO();
        session.setAuditSwitch(dto.getAuditSwitch());
        session.setSellPriceSwitch(dto.getSellPriceSwitch());
        session.setActivityStartDate(date);
        session.setActivityEndDate(DateUtils.addDateHours(session.getActivityStartDate(), dto.getActivityEffectiveTime()));
        session.setActivityEffectiveTime(dto.getActivityEffectiveTime());
        session.setPayEffectiveTime(dto.getPayEffectiveTime());
        session.setReminderTime(dto.getReminderTime());
        return session;
    }

    /**
     * 定时查询替换对账时间
     */

    @Override
    public void timeReplace() {
        SettingDTO data = this.queryByName(SettingsEnum.ORDER_BILL.value());

        if (StringUtils.isBlank(data.getValue())) {
            return;
        }

        String substringAfter = StringUtils.substringAfter(data.getValue(), "*");
        if (StringUtils.isBlank(substringAfter)) {
            return;
        }

        OrderBillSettingDTO orderBillSettingDTO = JSON.parseObject(substringAfter, OrderBillSettingDTO.class);
        if (orderBillSettingDTO.getType() == 1) {
            if (orderBillSettingDTO.getCreateDate().compareTo(new Date()) >= 0) {
                this.updateSite(substringAfter, SettingsEnum.ORDER_BILL.value());
                return;
            }

        }

        if (orderBillSettingDTO.getStartTime().compareTo(new Date()) <= 0) {
            this.updateSite(substringAfter, SettingsEnum.ORDER_BILL.value());
        }

    }
}
