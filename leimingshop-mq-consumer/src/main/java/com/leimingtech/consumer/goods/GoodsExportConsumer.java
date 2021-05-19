/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.goods;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import com.leimingtech.modules.service.goods.GoodsService;
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
 * 功能描述：
 * 商品导出优化
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/10
 **/

@Slf4j
@Component
public class GoodsExportConsumer {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private UploadService uploadService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_EXPORT_QUEUE)
    public void exportToTemplate(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        int totalPage = 0;
        String assignmentName = ExcelEnum.GOODS_EXPORT.getName();
        log.info("收到商品导出mq，名称：{}，条件：{}", assignmentName, msgText);
        SysExportManagementVO sysExportManagementVO = new SysExportManagementVO();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            //解析消息

            JSONObject jsonObject = JSONObject.parseObject(msgText);
            Map<String, Object> params = (Map<String, Object>) jsonObject;
            // 查询导出管理信息
            sysExportManagementVO = sysExportManagementService.get(Long.parseLong(params.get("managementId").toString()));
            int limit = 1000;
            if (sysExportManagementVO.getId() != null) {
                log.info(assignmentName, sysExportManagementVO);
                // 查询总记数
                int total = goodsService.pageGoods(params).getTotal();
                // 为空 设置空数据
                if (total == 0) {
                    sysExportManagementVO.setFailureNumber(total);
                    sysExportManagementVO.setOperationStatus(0);
                } else {
                    //说明有数据
                    totalPage = (int) Math.ceil((double) total / limit);
                    List<GoodsTemplateExcel> list = new ArrayList<>();
                    params.put(Constant.LIMIT, limit);
                    for (int i = 1; i <= totalPage; i++) {
                        params.put(Constant.PAGE, i);
                        List<GoodsTemplateExcel> list1 = goodsService.exportGoodsToExcel(params);
                        list.addAll(list1);
                    }
                    byteArrayOutputStream = EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, list, GoodsTemplateExcel.class, limit);
                    byte[] tmp = byteArrayOutputStream.toByteArray();
                    FileUploadDTO fileUploadDTO = new FileUploadDTO();
                    fileUploadDTO.setLogByteArr(tmp);
                    fileUploadDTO.setFileName(System.currentTimeMillis() + ".xls");
                    Map<String, Object> result = uploadService.fileUpload(fileUploadDTO);
                    log.info("地址为：{}", result.get("data").toString());

                    Object code = result.get("code");
                    if (code.equals(ErrorCode.SUCCESS)) {
                        sysExportManagementVO.setDownloadLink(result.get("data").toString());
                        sysExportManagementVO.setFinishTime(new Date());
                        // 状态修改为成功
                        sysExportManagementVO.setOperationStatus(2);
                    } else {
                        // 状态修改为失败sysexportmanagement
                        sysExportManagementVO.setOperationStatus(0);
                    }
                    Boolean update = sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
                    log.info("{}导出结束：{}", assignmentName, update);
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
            log.info(assignmentName + "处理完毕，手动执行ACK!");
        }
    }
}
