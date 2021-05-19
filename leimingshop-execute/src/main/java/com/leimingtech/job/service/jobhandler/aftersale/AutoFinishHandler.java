/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.aftersale;

import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.enmus.AfterSaleStateEnum;
import com.leimingtech.modules.aftersale.service.AftersaleBarterService;
import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
import com.leimingtech.modules.task.Job;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 自动完成
 * @Date: 2019/6/25 0:26
 * @Version: V1.0
 */
@Slf4j
@Component
public class AutoFinishHandler implements Job {

    @Autowired
    private SettingService settingService;

    @Autowired
    private AftersaleReturnService aftersaleReturnService;

    @Autowired
    private AftersaleBarterService aftersaleBarterService;

    @Override
    public void run(String params) {
        //查询退货表换货表买家待发货状态
        //查询售后设置
        SettingAftersaleDTO aftersaleDTO = settingService.getAftersaleDTO();
        if (null == aftersaleDTO) {
            log.info("查询售后设置异常");
            return;
        }
        //买家已退货xx天后，商家未处理，自动进行退货处理
        List<String> aftersaleReturnList = aftersaleReturnService.findSellerInOutTimeList(aftersaleDTO.getAutoReturn(), String.valueOf(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value()));
        log.info("买家已退货{1}天后，商家未处理，自动进行退货处理的数据为：{2}", aftersaleDTO.getAutoReturn(), aftersaleReturnList);
        if (CollectionUtils.isNotEmpty(aftersaleReturnList)) {
            for (String aftersaleSn : aftersaleReturnList) {
                Map<String, Object> map = aftersaleReturnService.confirmGoods(Long.valueOf(aftersaleSn), AfterSaleStateEnum.AFTER_STATUS_PAYING.value());
                if (map.get("code").equals(ResultCodeEnum.SUCCESS.value())) {
                    //调用退款服务
                    log.info("开始调用退款服务");
                    aftersaleReturnService.wxRefund(Long.valueOf(map.get("refundId").toString()));
                }

            }
        }
        //买家已换货xx天后，商家未处理，自动进行换货处理
        List<String> aftersaleBarterList = aftersaleBarterService.findSellerInOutTimeList(aftersaleDTO.getAutoBarter(), String.valueOf(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value()));
        log.info("买家已换货{1}天后，商家未处理，自动进行换货处理的数据为：{2}", aftersaleDTO.getAutoBarter(), aftersaleBarterList);
        if (CollectionUtils.isNotEmpty(aftersaleBarterList)) {
            for (String aftersaleSn : aftersaleReturnList) {
                aftersaleBarterService.confirmGoods(Long.valueOf(aftersaleSn), AfterSaleStateEnum.AFTER_STATUS_PAYING.value());

            }
        }


    }
}
