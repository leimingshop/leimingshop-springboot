/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.goods;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.modules.excel.goods.EasyGoodsCheckExcel;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.service.sys.SysExportManagementService;
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
 * 功能描述：
 * 商品审核导出优化
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/10
 **/

@Slf4j
@Component
public class GoodsCheckExportConsumer {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private UploadService uploadService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_CHECK_EXPORT_QUEUE)
    public void export(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        String assignmentName = ExcelEnum.GOODS_CHECK_EXPORT.getName();
        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(msgText);
        Map<String, Object> params = (Map<String, Object>) jsonObject;
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        // 查询导出管理信息
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.get(Long.parseLong(params.get("managementId").toString()));
        // 状态修改为失败
        sysExportManagementVO.setOperationStatus(0);

        log.info("收到商品审核导出mq，名称：{}，条件：{}", assignmentName, msgText);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            if (sysExportManagementVO.getId() != null) {
                log.info(assignmentName, sysExportManagementVO);
                //查询导出的数据
                PageData<EasyGoodsCheckExcel> pageData = goodsService.goodsCheckExport(params);
                if (pageData != null && CollectionUtils.isNotEmpty(pageData.getList())) {
                    EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, pageData.getList(), EasyGoodsCheckExcel.class, Integer.parseInt(params.get(Constant.PAGE).toString()));
                }
                int totalPage = pageData.getTotal() / limit;
                for (int i = 2; i <= totalPage; i++) {
                    params.put(Constant.PAGE, i);
                    pageData = goodsService.goodsCheckExport(params);
                    if (pageData != null) {
                        EasyExcelUtils.excelFileByteUpload(byteArrayOutputStream, pageData.getList(), EasyGoodsCheckExcel.class, Integer.parseInt(params.get(Constant.PAGE).toString()));
                    }
                }
                log.info("总条数：{}", pageData.getTotal());

                FileUploadDTO fileUploadDTO = new FileUploadDTO();
                fileUploadDTO.setLogByteArr(byteArrayOutputStream.toByteArray());
                fileUploadDTO.setFileName(System.currentTimeMillis() + ".xls");
                Map<String, Object> result = uploadService.fileUpload(fileUploadDTO);
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

            }
            Boolean update = sysExportManagementService.update(ConvertUtils.sourceToTarget(sysExportManagementVO, SysExportManagementDTO.class));
            log.info("{}导出结束：{}", assignmentName, update);
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
