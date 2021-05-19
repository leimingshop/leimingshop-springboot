/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.aftersale;

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

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 自动取消退换货操作
 * @Date: 2019/6/25 0:26
 * @Version: V1.0
 */
@Slf4j
@Component
public class AutoCancelHandler implements Job {

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
        //审核同意退货申请xx天后，买家未处理，自动取消退货处理
        List<String> aftersaleReturnList = aftersaleReturnService.findSellerInOutTimeList(aftersaleDTO.getCancelReturn(), String.valueOf(AfterSaleStateEnum.LOGISTICS_STATUS_BUYEROUT.value()));
        log.info("查询出审核同意退货申请{1}天后，买家未处理，自动取消退货处理的数据为：{2}", aftersaleDTO.getCancelReturn(), aftersaleReturnList);
        if (CollectionUtils.isNotEmpty(aftersaleReturnList)) {
            //批量更新
            aftersaleReturnService.batchCancel(aftersaleReturnList);
        }
        List<String> aftersaleBarterList = aftersaleBarterService.findSellerInOutTimeList(aftersaleDTO.getCancelBarter(), String.valueOf(AfterSaleStateEnum.LOGISTICS_STATUS_BUYEROUT.value()));
        log.info("查询出审核同意换货申请{1}天后，买家未处理，自动取消换货处理的数据为：{2}", aftersaleDTO.getCancelBarter(), aftersaleBarterList);
        if (CollectionUtils.isNotEmpty(aftersaleBarterList)) {
            //批量更新
            aftersaleBarterService.batchCancel(aftersaleBarterList);
        }

    }
}
