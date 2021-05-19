/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.logistics;

import com.leimingtech.modules.dto.transport.TransportMessageDTO;
import com.leimingtech.modules.service.transport.TransportMessageService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 物流自动更新
 * @Date: 2019/8/13 22:02
 * @Version: V1.0
 */
@Slf4j
@Component
public class LogisticslHandler implements Job {

    @Autowired
    private TransportMessageService transportMessageService;

    @Override
    public void run(String params) {
        // 查询未完成的物流信息单
        List<TransportMessageDTO> transportMessageDTOList = transportMessageService.progressList();
        if (CollectionUtils.isNotEmpty(transportMessageDTOList)) {
            // 调用物流系统查询最新物流数据，存入mongodb中并修改物流表状态
            Boolean result = transportMessageService.updateData(transportMessageDTOList);
        }

    }
}
