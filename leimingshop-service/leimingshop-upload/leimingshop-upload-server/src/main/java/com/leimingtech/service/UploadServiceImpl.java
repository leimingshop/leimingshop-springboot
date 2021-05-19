/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service;

import cn.hutool.core.img.ImgUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leimingtech.cloud.OssFactory;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.ZxingUtils;
import com.leimingtech.dto.setting.SettingUpdateDTO;
import com.leimingtech.dto.upload.UploadRecordDTO;
import com.leimingtech.enums.picture.ErrorCodeEnum;
import com.leimingtech.exception.ModuleErrorCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.SettingService;
import com.leimingtech.upload.dto.*;
import com.leimingtech.upload.service.UploadService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/5/25 2:21 PM
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "上传文件")
    @Override
    public Map<String, Object> upload(@RequestPart(value = "file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>(3);
        if (file.isEmpty()) {
            result.put("code", ModuleErrorCode.UPLOAD_FILE_EMPTY);
            result.put("message", "文件不能为空");
            return result;
        }

        //上传文件
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String url = null;
        try {
            url = OssFactory.build().uploadSuffix(file.getBytes(), extension);
        } catch (IOException e) {
            log.error("错误信息为" + e);
        }
        // 保存上传记录
        UploadRecordDTO uploadRecordDTO = new UploadRecordDTO();
        uploadRecordDTO.setFileUrl(url);
        uploadRecordDTO.setFileRealName(file.getOriginalFilename());
        uploadRecordDTO.setFileSuffix(extension);
        Integer size = Math.toIntExact(file.getSize());
        uploadRecordDTO.setFileSize(size);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_UPLOAD_RECORD_QUEUE, JSONObject.toJSONString(uploadRecordDTO));

        //文件信息
        UploadDTO dto = new UploadDTO();
        dto.setUrl(url);
        dto.setSize(file.getSize());
        result.put("code", ErrorCodeEnum.SUCCESS.value());
        result.put("message", "上传成功");
        result.put("url", dto.getUrl());
        result.put("size", dto.getSize());
        return result;
    }

    /**
     * 功能描述:
     * 〈base64文件上传〉
     *
     * @param base64FileDTO base64文件实体
     * @author : 刘远杰
     */
    @Override

    @ApiOperation(value = "Base64上传文件")
    public Map<String, Object> uploadPicBase64(@RequestBody Base64FileDTO base64FileDTO) {
        Map<String, Object> result = new HashMap<>(3);
        String url = null;

        if (base64FileDTO.getType() != null && base64FileDTO.getType() == 0) {
            // 添加图片水印
            url = pressText(base64FileDTO);
        } else {
            //上传文件
            url = OssFactory.build().uploadSuffix(base64FileDTO.getBase64ByteArr(), base64FileDTO.getFileSuffix());
        }


        // 保存上传记录
        UploadRecordDTO uploadRecordDTO = new UploadRecordDTO();
        uploadRecordDTO.setFileUrl(url);
        uploadRecordDTO.setFileRealName(base64FileDTO.getFileRealName());
        uploadRecordDTO.setFileSuffix(base64FileDTO.getFileSuffix());
        Integer size = Math.toIntExact(base64FileDTO.getFileSize());
        uploadRecordDTO.setFileSize(size);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_UPLOAD_RECORD_QUEUE, JSONObject.toJSONString(uploadRecordDTO));

        //文件信息
        UploadDTO dto = new UploadDTO();
        dto.setUrl(url);
        dto.setSize(base64FileDTO.getFileSize());

        result.put("code", ErrorCode.SUCCESS);
        result.put("msg", "上传成功");
        result.put("data", dto);
        return result;
    }

    /**
     * 添加图片文字水印
     *
     * @param base64ByteArr base64上传参数
     * @throws IOException
     */
    private String pressText(Base64FileDTO base64ByteArr) {
        ByteArrayOutputStream out = null;
        InputStream inputStream = null;
        String url = null;
        try {
            inputStream = new ByteArrayInputStream(base64ByteArr.getBase64ByteArr());
            out = new ByteArrayOutputStream();
            SettingUpdateDTO site = settingService.getSite();
            /**
             *  InputStream ,
             *  OutputStream ,
             *  String pressText   水印文字,
             *  Color color  字体颜色,
             *  Font font   字体类型,
             *  int x      x坐标修正值。 默认在中间，偏移量相对于中间偏移,
             *  int y      y坐标修正值。 默认在中间，偏移量相对于中间偏移,
             *  float alpha  透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
             */
            ImgUtil.pressText(inputStream, out, site.getName() + "审核专用图", Color.gray, new Font("黑体", Font.BOLD, 40), 0, 0, 0.8f);
            url = OssFactory.build().uploadSuffix(out.toByteArray(), base64ByteArr.getFileSuffix());
            log.info(url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return url;
    }

    /**
     * 根据URL下载图片
     *
     * @param urlString: 图片URL地址
     * @return 储存图片地址
     * @date 2019/12/26 14:44
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public String downloadToUrl(@RequestParam("urlString") String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //通过输入流获取图片数据
            InputStream inputStream = conn.getInputStream();
            //注！IOUtils.toByteArray(url).length的大小如果错误则会出现图片上传成功但查看图片时图片部分丢失的情况
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, IOUtils.toByteArray(url).length, "png", null);
            log.debug(storePath.getFullPath());
            inputStream.close();
            return "/" + storePath.getFullPath();
        } catch (Exception e) {
            log.error("上传图片失败", e);
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param dto
     * @return
     */
    @Override

    public Map<String, Object> fileUpload(@RequestBody FileUploadDTO dto) {
        Map<String, Object> result = new HashMap<>(3);
        String extension = FilenameUtils.getExtension(dto.getFileName());
        String url = null;
        try {
            url = OssFactory.build().uploadSuffix(dto.getLogByteArr(), extension);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "上传失败");
            result.put("data", url);
            return result;
        }


        result.put("code", 200);
        result.put("message", "上传成功");
        result.put("data", url);
        return result;
    }

    /**
     * 功能描述:
     * 〈视频上传〉
     *
     * @param fileDTO
     * @author : pixiaoyong
     */
    @Override

    public Map<String, Object> uploadVideoBase64(@RequestBody FileInputStreamDTO fileDTO) {
        Map<String, Object> result = new HashMap<>(3);

        //上传文件
        String url = OssFactory.build().uploadSuffix(fileDTO.getInputStream(), fileDTO.getFileSuffix());
        //文件信息
        UploadVideoDTO dto = new UploadVideoDTO();
        dto.setUrl(url);
        dto.setSize(fileDTO.getFileSize());
        result.put("code", ErrorCode.SUCCESS);
        result.put("msg", "视频上传成功");
        result.put("data", dto);
        return result;
    }


    /**
     * 文本生成二维码并上传到服务器返回图片在线地址
     *
     * @param content: 文本内容
     * @return 图片地址
     * @date 2020/5/7 18:38
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public String uploadQrCode(@RequestParam("content") String content) {
        ByteArrayOutputStream out = ZxingUtils.generateQRCode(content);
        return OssFactory.build().uploadSuffix(out.toByteArray(), "png");
    }
}
