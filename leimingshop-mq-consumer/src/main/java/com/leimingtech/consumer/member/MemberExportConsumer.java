/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.member;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.modules.dto.member.EasyMemberExcelDTO;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.service.sys.SysExportManagementService;
import com.leimingtech.upload.dto.FileUploadDTO;
import com.leimingtech.upload.service.UploadService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员导出
 *
 * @author xuzhch
 * @date 2020年10月15日
 **/

@Slf4j
@Component
public class MemberExportConsumer {


    @Autowired
    private MemberService memberService;
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private UploadService uploadService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_MEMBER_EXPORT_QUEUE)
    public void export(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        String assignmentName = ExcelEnum.MEMBER_EXPORT.getName();
        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(msgText);
        Map<String, Object> params = (Map<String, Object>) jsonObject;
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        // 查询导出管理信息
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.get(Long.parseLong(params.get("managementId").toString()));
        sysExportManagementVO.setOperationStatus(0);
        log.info("收到会员导出mq，名称：{}，条件：{}", assignmentName, msgText);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (sysExportManagementVO.getId() != null) {
                log.info(assignmentName, sysExportManagementVO);
                //查询导出的数据
                PageData<EasyMemberExcelDTO> page = memberService.export(params);
                int total = page.getTotal();
                int totalPage = (int) Math.ceil((double) total / limit);
                params.put(Constant.LIMIT, 100);
                List<EasyMemberExcelDTO> excelDTOList = new ArrayList<>();
                for (int i = 1; i <= totalPage; i++) {
                    params.put(Constant.PAGE, i);
                    PageData<EasyMemberExcelDTO> pageData = memberService.export(params);
                    List<EasyMemberExcelDTO> list = pageData.getList();
                    excelDTOList.addAll(list);
                }

                EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, excelDTOList, EasyMemberExcelDTO.class, Integer.parseInt(params.get(Constant.PAGE).toString()));

                log.info("总条数：{}", excelDTOList.size());
                FileUploadDTO fileUploadDTO = new FileUploadDTO();
                fileUploadDTO.setLogByteArr(byteArrayOutputStream.toByteArray());
                fileUploadDTO.setFileName(System.currentTimeMillis() + ".xls");
                Map<String, Object> result = uploadService.fileUpload(fileUploadDTO);
                if (result.get("code").equals(ErrorCode.SUCCESS)) {
                    sysExportManagementVO.setDownloadLink(result.get("data").toString());
                    sysExportManagementVO.setFinishTime(new Date());
                    // 状态修改为成功
                    sysExportManagementVO.setOperationStatus(2);
                } else {
                    // 状态修改为失败
                    sysExportManagementVO.setOperationStatus(0);
                }
                Boolean update = sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
                log.info("{}导出结束：{}", assignmentName, update);
            }
        } catch (Exception e) {
            log.error("出现异常", e);
            sysExportManagementVO.setOperationStatus(0);
            sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
        } finally {
            byteArrayOutputStream.close();
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info(assignmentName + "处理完毕，手动执行ACK!");
        }
    }


}
