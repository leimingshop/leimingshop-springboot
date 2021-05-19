/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.setting;


import com.leimingtech.dto.setting.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统设置表
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */

public interface SettingService {

    /**
     * 根据name获取
     *
     * @param settingName
     * @return
     */

    SettingDTO queryByName(@RequestParam("settingName") String settingName);

    /**
     * 根据名称查询系统设置
     *
     * @param settingName
     * @return
     */

    String queryRedisByName(@RequestParam("settingName") String settingName);

    /**
     * 修改站点信息
     *
     * @param settingInfo 新的站点的信息
     * @param settingName 站点模块类型
     */

    void updateSite(@RequestParam("settingInfo") String settingInfo,
                    @RequestParam("settingName") String settingName);

    /**
     * @return com.leimingtech.dto.setting.SettingDTO
     * @Description 根据id获取设置信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:55 2019-05-27
     */

    SettingDTO get(Long id);

    /**
     * @return void
     * @Description 保存设置信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:08 2019-05-27
     */

    void save(@RequestBody SettingDTO dto);

    /**
     * 获取网站设置的系统设置的站点信息
     */

    SettingUpdateDTO getSite();

    /**
     * 新增/修改网站设置的系统设置的站点信息
     *
     * @param dto
     * @return
     */

    void putSite(@RequestBody SettingUpdateDTO dto);


    /**
     * 获取网站设置的系统设置的短信设置
     *
     * @param
     * @return
     */

    SettingMessageDTO getShortMessageSet();


    void saveMessageSetting(@RequestBody SettingMessageDTO dto);

    /**
     * @Author weixianchun
     * @Description 获取用户默认头像设置
     * @Date 2019/11/4 16:21
     * @Return com.leimingtech.dto.setting.SettingDefaultAvatarsDTO
     * @version 1.0
     */

    SettingDefaultAvatarsDTO getDefaultAvatarsSet();

    /**
     * @Author weixianchun
     * @Description 修改用户默认头像设置
     * @Date 2019/11/4 16:21
     * @version 1.0
     */

    void updateDefaultAvatarsSet(@RequestBody SettingDefaultAvatarsDTO defaultAvatarsDTO);

    /**
     * 获取网站设置的系统设置的商品审核设置
     *
     * @param
     * @return
     */

    SettingGoodsAuditDTO getGoodsAuditSet();


    void updateGoodsAuditSet(@RequestBody SettingGoodsAuditDTO dto);

    /**
     * 获取网站设置的系统设置的公告提示设置
     *
     * @param
     * @return
     */

    SettingAnnouncementTipsDTO getAnnouncementTipsSet();

    /**
     * @param dto
     * @return
     */

    void updateAnnouncementTips(@RequestBody SettingAnnouncementTipsDTO dto);

    /**
     * 获取网站设置的订单设置
     *
     * @param
     * @return
     */

    SettingSeniorDTO getSeniorSet();

    /**
     * 更新网站设置的订单设置
     *
     * @param
     * @return
     */

    void updateSeniorSet(@RequestBody SettingSeniorDTO dto);

    /**
     * 获取售后设置
     *
     * @param
     * @return
     */

    SettingAftersaleDTO getAftersaleDTO();

    /**
     * 更新发票设置
     *
     * @param
     * @return
     */

    void updateInvoice(@RequestBody SettingInvoiceDTO dto);

    /**
     * 获取发票设置
     *
     * @param
     * @return
     */

    SettingInvoiceDTO getInvoice();

    /**
     * 更新售后设置
     *
     * @param
     * @return
     */

    void updateAftersaleDTO(@RequestBody SettingAftersaleDTO dto);

    /**
     * 获取快递设置
     *
     * @param
     * @return
     */

    SettingSeniorExpressDTO getExpressSet();

    /**
     * 更新快递设置
     *
     * @param
     * @return
     */

    void updateExpressSet(@RequestBody SettingSeniorExpressDTO dto);

    /**
     * 获取结算设置
     *
     * @param
     * @return
     */

    SettingSeniorSettlementDTO getSettlementSet();

    /**
     * 更新结算设置
     *
     * @param
     * @return
     */

    void updateSettlementSet(@RequestBody SettingSeniorSettlementDTO dto);

    /**
     * 获取对账设置
     *
     * @return
     */

    OrderBillSettingDTO getOrderBill();

    /**
     * 修改对账设置
     *
     * @return
     */

    void updateOrderBill(@RequestBody OrderBillSettingDTO orderBillSettingDTO);

    /**
     * 功能描述：
     * <获取秒杀设置>
     *
     * @date 2020/3/6 10:46
     * @author 刘远杰
     **/

    SettingSeckillDTO getSeckillSetting();

    /**
     * 功能描述：
     * <修改秒杀设置>
     *
     * @param dto 秒杀设置信息
     * @date 2020/3/6 10:48
     * @author 刘远杰
     **/

    void updateSeckillSetting(@RequestBody SettingSeckillDTO dto);

    /**
     * 定时查询替换对账时间
     */

    void timeReplace();
}
