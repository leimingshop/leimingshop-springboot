/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.sys;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.dto.SysLogOperationDTO;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.excel.SysLogOperationExcel;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.service.sys.SysExportManagementService;
import com.leimingtech.service.sys.SysLogOperationService;
import com.leimingtech.upload.dto.FileUploadDTO;
import com.leimingtech.upload.service.UploadService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author: lishuo
 * @Date: 2020/8/27 14:03
 * @email: lishuo@leimingtech.com
 * @Description: 操作日志导出 消费者
 */
@Component
@Slf4j
public class OperationLogExportConsumer {
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private SysLogOperationService sysLogOperationService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_LOG_EXPORT_QUEUE)
    public void export(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("收到操作日志导出mq，名称：{}，条件：{}", ExcelEnum.OPERATION_LOG_EXPORT.getName(), msgText);

        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(msgText);
        Map<String, Object> params = (Map<String, Object>) jsonObject;
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        // 查询导出管理信息
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.get(Long.parseLong(params.get("managementId").toString()));
        //设置默认导入失败
        sysExportManagementVO.setOperationStatus(0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (sysExportManagementVO.getId() != null) {
                //查询导出的数据
                PageData<SysLogOperationDTO> pageData = sysLogOperationService.page(params);
                log.info("总条数：{}", pageData.getTotal());
                if (CollectionUtils.isNotEmpty(pageData.getList())) {
                    EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, pageData.getList(), SysLogOperationExcel.class, Integer.parseInt(params.get(Constant.PAGE).toString()));
                    int totalPage = pageData.getTotal() / limit;
                    for (int i = 2; i <= totalPage; i++) {
                        params.put(Constant.PAGE, i);
                        pageData = sysLogOperationService.page(params);
                        EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, pageData.getList(), SysLogOperationExcel.class, Integer.parseInt(params.get(Constant.PAGE).toString()));
                    }
                    FileUploadDTO fileUploadDTO = new FileUploadDTO();
                    fileUploadDTO.setLogByteArr(byteArrayOutputStream.toByteArray());
                    fileUploadDTO.setFileName(System.currentTimeMillis() + ".xls");
                    Map<String, Object> result = uploadService.fileUpload(fileUploadDTO);
                    byteArrayOutputStream.close();
                    if (result.get("code").equals(200)) {
                        sysExportManagementVO.setDownloadLink(result.get("data").toString());
                        sysExportManagementVO.setFinishTime(new Date());
                        // 状态修改为成功
                        sysExportManagementVO.setOperationStatus(2);
                    } else {
                        // 状态修改为失败sysexportmanagement
                        sysExportManagementVO.setOperationStatus(0);
                    }
                    Boolean update = sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
                    log.info("{}导出结束：{}", ExcelEnum.OPERATION_LOG_EXPORT.getName(), update);
                } else {
                    sysExportManagementVO.setFailureNumber(pageData.getTotal());

                }
            }
        } catch (Exception e) {
            log.error("出现异常", e);
            sysExportManagementVO.setOperationStatus(0);
            sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
        } finally {
            byteArrayOutputStream.close();
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info(ExcelEnum.OPERATION_LOG_EXPORT.getName() + "导出失败，手动执行ACK!");
        }

    }
}
