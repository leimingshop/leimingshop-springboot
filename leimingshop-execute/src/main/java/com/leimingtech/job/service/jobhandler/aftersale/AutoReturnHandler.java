/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.aftersale;

import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.enmus.AfterSaleEnum;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditLogDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditLogSaveDTO;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.modules.aftersale.service.AftersaleAuditLogService;
import com.leimingtech.modules.task.Job;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 自动退货操作
 * @Date: 2019/6/25 0:26
 * @Version: V1.0
 */
@Slf4j
@Component
public class AutoReturnHandler implements Job {

    @Autowired
    private SettingService settingService;

    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @Autowired
    private AftersaleAuditLogService aftersaleAuditLogService;

    @Override
    public void run(String params) {
        //查询售后设置
        SettingAftersaleDTO aftersaleDTO = settingService.getAftersaleDTO();
        if (null == aftersaleDTO || null == aftersaleDTO.getAgreeReturn() || null == aftersaleDTO.getAgreeBarter()) {
            log.info("查询售后设置异常");
            return;
        }
        List<AftersaleApplyDTO> aftersaleApplyReturnList = aftersaleApplyService.findAutoApplyList(AfterSaleEnum.TYPERETURN.value(), aftersaleDTO.getAgreeReturn(), AfterSaleEnum.SELLERAUDITING.value());
        log.info("查询出符合条件的售后退货数据" + aftersaleApplyReturnList);
        //买家发起退货申请xx天后，商家未处理，按商家同意退货处理
        saveAuto(aftersaleApplyReturnList, AfterSaleEnum.TYPERETURN.value());

        //买家发起换货申请xx天后，商家未处理，按商家同意换货处理
        List<AftersaleApplyDTO> aftersaleApplyBarterList = aftersaleApplyService.findAutoApplyList(AfterSaleEnum.TYPEBARTER.value(), aftersaleDTO.getAgreeBarter(), AfterSaleEnum.SELLERAUDITING.value());
        log.info("查询出符合条件的售后换货数据" + aftersaleApplyBarterList);

        saveAuto(aftersaleApplyBarterList, AfterSaleEnum.TYPEBARTER.value());
    }

    private void saveAuto(List<AftersaleApplyDTO> aftersaleApplyList, Integer type) {
        if (CollectionUtils.isNotEmpty(aftersaleApplyList)) {
            for (AftersaleApplyDTO aftersaleApplyDTO : aftersaleApplyList) {
                AftersaleAuditLogSaveDTO aftersaleAuditLogSaveDTO = new AftersaleAuditLogSaveDTO();
                aftersaleAuditLogSaveDTO.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
                if (type.equals(AfterSaleEnum.TYPERETURN.value())) {
                    aftersaleAuditLogSaveDTO.setAftersaleType(AfterSaleEnum.TYPERETURN.value());
                    aftersaleAuditLogSaveDTO.setReason("审核通过：系统自动同意商家退货");
                } else {
                    aftersaleAuditLogSaveDTO.setAftersaleType(AfterSaleEnum.TYPEBARTER.value());
                    aftersaleAuditLogSaveDTO.setReason("审核通过：系统自动同意商家换货");

                }
                Map<String, Object> map = new HashMap<>(16);
                map.put("id", aftersaleApplyDTO.getAftersaleSn());
                List<AftersaleAuditLogDTO> list = aftersaleAuditLogService.list(map);
                log.info("查出的数据为" + list);
                for (AftersaleAuditLogDTO aftersaleAuditLogDTO : list) {
                    if (aftersaleAuditLogDTO.getProcess().equals(1L)
                            && aftersaleAuditLogDTO.getResult().equals(AfterSaleEnum.RESULT_REVIEW.value())) {
                        aftersaleAuditLogSaveDTO.setId(aftersaleAuditLogDTO.getId());
                    }
                }
                aftersaleAuditLogSaveDTO.setProcess(1L);
                aftersaleAuditLogSaveDTO.setResult(AfterSaleEnum.RESULT_PROCESS.value());
                Map<String, Object> resultMap = aftersaleApplyService.saveApplyResult(aftersaleAuditLogSaveDTO);
                log.info("商家自动同意退货" + resultMap);
            }
        }

    }
}
