/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.aftersale;

import com.leimingtech.modules.aftersale.dto.AftersaleRefundRecordsDTO;
import com.leimingtech.modules.aftersale.service.AftersaleRefundRecordsService;
import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:退款失败重试接口
 * @Date: 2019/6/25 0:26
 * @Version: V1.0
 */
@Slf4j
@Component
public class RefundRetryHandler implements Job {

    @Autowired
    private AftersaleRefundRecordsService aftersaleRefundRecordsService;

    @Autowired
    private AftersaleReturnService aftersaleReturnService;

    @Override
    public void run(String params) {
        //查询创建时间为三天内的支付单
        List<AftersaleRefundRecordsDTO> list = aftersaleRefundRecordsService.findPayFail();
        log.info("查询出三天前的退款种订单为{1}", list);
        if (CollectionUtils.isNotEmpty(list)) {
            for (AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO : list) {
                //调用退款接口
                aftersaleReturnService.wxRefund(aftersaleRefundRecordsDTO.getId());
            }
        }

    }
}
