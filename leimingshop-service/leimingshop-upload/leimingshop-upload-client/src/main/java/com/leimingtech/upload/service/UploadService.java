/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.service;

import com.leimingtech.upload.dto.Base64FileDTO;
import com.leimingtech.upload.dto.FileInputStreamDTO;
import com.leimingtech.upload.dto.FileUploadDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/5/25 2:39 PM
 */

public interface UploadService {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回路径
     */
    Map<String, Object> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 功能描述:
     * 〈base64文件上传〉
     *
     * @param base64FileDTO base64文件实体
     * @return 图片上传结果
     * @author : 刘远杰
     */

    Map<String, Object> uploadPicBase64(@RequestBody Base64FileDTO base64FileDTO);

    /**
     * base64视频文件上传
     *
     * @param fileDTO 文件流对象
     * @return 上传结果
     * @author : 皮小勇
     */

    Map<String, Object> uploadVideoBase64(@RequestBody FileInputStreamDTO fileDTO);

    /**
     * 根据URL下载图片
     *
     * @param urlString: 图片URL地址
     * @return 储存图片地址
     * @date 2019/12/26 14:44
     * @author lixiangx@leimingtech.com
     **/

    String downloadToUrl(@RequestParam("urlString") String urlString);

    /**
     * 文件上传
     *
     * @param dto
     * @return
     */

    Map<String, Object> fileUpload(@RequestBody FileUploadDTO dto);

    /**
     * 文本生成二维码并上传到服务器返回图片在线地址
     *
     * @param content: 文本内容
     * @return 图片地址
     * @date 2020/5/7 18:38
     * @author lixiangx@leimingtech.com
     **/

    String uploadQrCode(@RequestParam("content") String content);
}
