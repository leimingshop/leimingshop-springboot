/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.setting;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.dto.setting.*;
import com.leimingtech.enums.activity.seckill.SeckillSettingEnum;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 系统设置controller
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */
@RestController
@RequestMapping("setting")
@Api(tags = "网站设置-系统设置")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SettingDTO> get(@PathVariable("id") Long id) {
        SettingDTO data = settingService.get(id);

        return new Result<SettingDTO>().ok(data, "查询成功");
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public Result save(@RequestBody SettingDTO dto) {

        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        settingService.save(dto);

        return new Result();
    }


    /**
     * 获取网站设置的系统设置的站点信息
     */
    @GetMapping("site")
    @ApiOperation("获取网站设置的系统设置的站点信息")
    public Result<SettingUpdateDTO> getSite() {

        SettingUpdateDTO settingUpdateDTO = settingService.getSite();

        return new Result<SettingUpdateDTO>().ok(settingUpdateDTO);
    }

    /**
     * 新增/修改网站设置的系统设置的站点信息
     *
     * @param dto
     * @return
     */
    @PostMapping("site")
    @ApiOperation("修改网站设置的系统设置的站点信息")
    public Result putSite(@RequestBody SettingUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.putSite(dto);

        return new Result().ok("", "修改成功");
    }


    /**
     * 获取网站设置的系统设置的短信设置
     *
     * @param
     * @return
     */
    @GetMapping("message")
    @ApiOperation("获取网站设置的系统设置的短信设置")
    public Result<SettingMessageDTO> getShortMessageSet() {

        SettingMessageDTO settingMessageDTO = settingService.getShortMessageSet();

        return new Result<SettingMessageDTO>().ok(settingMessageDTO);
    }

    @PostMapping("message")
    @ApiOperation("修改网站设置的系统设置的短信设置")
    public Result saveMessageSetting(@RequestBody SettingMessageDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.saveMessageSetting(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的系统设置的用户默认头像设置
     *
     * @param
     * @return
     */
    @GetMapping("default/avatars")
    @ApiOperation("获取网站设置的系统设置的用户默认头像设置")
    public Result<SettingDefaultAvatarsDTO> getDefaultAvatarsSet() {

        SettingDefaultAvatarsDTO defaultAvatarsSet = settingService.getDefaultAvatarsSet();

        return new Result<SettingDefaultAvatarsDTO>().ok(defaultAvatarsSet);
    }

    @PostMapping("default/avatars")
    @ApiOperation("修改网站设置的系统设置的用户默认头像设置")
    public Result updateDefaultAvatarsSet(@RequestBody SettingDefaultAvatarsDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateDefaultAvatarsSet(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的系统设置的商品审核设置
     *
     * @param
     * @return
     */
    @GetMapping("audit")
    @ApiOperation("获取网站设置的系统设置的商品审核设置")
    public Result<SettingGoodsAuditDTO> getGoodsAuditSet() {

        SettingGoodsAuditDTO settingGoodsAuditDTO = settingService.getGoodsAuditSet();

        return new Result<SettingGoodsAuditDTO>().ok(settingGoodsAuditDTO);
    }

    @PostMapping("audit")
    @ApiOperation("修改网站设置的系统设置的商品审核设置")
    public Result updateGoodsAuditSet(@RequestBody SettingGoodsAuditDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateGoodsAuditSet(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的系统设置的公告提示设置
     *
     * @param
     * @return
     */
    @GetMapping("tips")
    @ApiOperation("获取网站设置的系统设置的公告提示设置")
    public Result<SettingAnnouncementTipsDTO> getAnnouncementTipsSet() {

        SettingAnnouncementTipsDTO dto = settingService.getAnnouncementTipsSet();

        return new Result<SettingAnnouncementTipsDTO>().ok(dto);
    }

    /**
     * @param dto
     * @return
     */
    @PostMapping("tips")
    @ApiOperation("修改网站设置的系统设置的公告提示设置")
    public Result updateAnnouncementTips(@RequestBody SettingAnnouncementTipsDTO dto) {
        //效验数据
        //todo
        ValidatorUtils.validateEntity(dto);

        JSON.toJSONString(dto);

        settingService.updateAnnouncementTips(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的高级设置
     *
     * @param
     * @return
     */
    @GetMapping("order")
    @ApiOperation("获取网站设置的高级设置订单设置")
    public Result<SettingSeniorDTO> getSeniorSet() {

        SettingSeniorDTO dto = settingService.getSeniorSet();

        return new Result<SettingSeniorDTO>().ok(dto);
    }

    @PostMapping("order")
    @ApiOperation("修改网站设置的高级设置订单设置")
    public Result updateSeniorSet(@RequestBody SettingSeniorDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateSeniorSet(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取开票设置
     *
     * @param
     * @return
     */
    @GetMapping("invoice")
    @ApiOperation("获取开票设置")
    public Result<SettingInvoiceDTO> getInvoice() {

        SettingInvoiceDTO dto = settingService.getInvoice();

        return new Result<SettingInvoiceDTO>().ok(dto);
    }

    /**
     * 修改开票设置
     *
     * @param dto
     * @return
     */
    @PostMapping("invoice")
    @ApiOperation("修改开票设置")
    public Result updateInvoice(@RequestBody SettingInvoiceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateInvoice(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 获取网站设置的快递设置
     *
     * @param
     * @return
     */
    @GetMapping("express")
    @ApiOperation("获取网站设置的高级设置快递设置")
    public Result<SettingSeniorExpressDTO> getExpressSet() {

        SettingSeniorExpressDTO dto = settingService.getExpressSet();

        return new Result<SettingSeniorExpressDTO>().ok(dto);
    }

    @PostMapping("express")
    @ApiOperation("修改网站设置的高级设置快递设置")
    public Result updateExpressSet(@RequestBody SettingSeniorExpressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateExpressSet(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的高级设置
     *
     * @param
     * @return
     */
    @GetMapping("settlement")
    @ApiOperation("获取网站设置的高级设置结算设置")
    public Result<SettingSeniorSettlementDTO> getSettlementSet() {

        SettingSeniorSettlementDTO dto = settingService.getSettlementSet();

        return new Result<SettingSeniorSettlementDTO>().ok(dto);
    }

    @PostMapping("settlement")
    @ApiOperation("修改网站设置的高级设置结算设置")
    public Result updateSettlementSet(@RequestBody SettingSeniorSettlementDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateSettlementSet(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取网站设置的高级设置
     *
     * @param
     * @return
     */
    @GetMapping("aftersale")
    @ApiOperation("获取售后设置")
    public Result<SettingAftersaleDTO> getAftersaleDTO() {

        SettingAftersaleDTO dto = settingService.getAftersaleDTO();

        return new Result<SettingAftersaleDTO>().ok(dto);
    }

    @PostMapping("aftersale")
    @ApiOperation("更新售后设置")
    public Result updateAftersaleDTO(@RequestBody SettingAftersaleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        settingService.updateAftersaleDTO(dto);

        return new Result().ok("", "修改成功");
    }

    /**
     * 获取对账设置
     */
    @GetMapping("order/bill")
    @ApiOperation("获取对账设置")
    public Result<OrderBillSettingDTO> getOrderBill() {
        OrderBillSettingDTO orderSettlement = settingService.getOrderBill();
        return new Result<OrderBillSettingDTO>().ok(orderSettlement);
    }

    /**
     * 修改对账设置
     */
    @PutMapping("order/bill")
    @ApiOperation("修改对账设置")
    public Result updateOrderBill(@RequestBody OrderBillSettingDTO orderBillSettingDTO) {
        settingService.updateOrderBill(orderBillSettingDTO);
        return new Result().ok(null, "修改成功");
    }

    @GetMapping("seckill")
    @ApiOperation("查询秒杀设置")
    public Result<SettingSeckillDTO> getSeckillSetting() {
        // 查询秒杀设置
        SettingSeckillDTO dto = settingService.getSeckillSetting();
        return new Result<SettingSeckillDTO>().ok(dto, "查询秒杀设置成功");
    }

    @PutMapping("seckill")
    @ApiOperation("修改秒杀设置")
    public Result updateSeckillSetting(@RequestBody SettingSeckillDTO dto) {
        // TODO: 2020/3/6 不做审核开关和销售价显示开关 填充默认值 
        dto.setAuditSwitch(SeckillSettingEnum.AUDIT_SWITCH_OFF.value());
        dto.setSellPriceSwitch(SeckillSettingEnum.SELLPRICE_SWITCH_ON.value());
        // 数据校验
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);
        // 修改秒杀设置
        settingService.updateSeckillSetting(dto);
        return new Result<>().ok(null, "修改成功");
    }

}
