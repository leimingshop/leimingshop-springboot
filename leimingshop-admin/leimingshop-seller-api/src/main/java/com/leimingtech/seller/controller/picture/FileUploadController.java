/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.picture;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.upload.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("file")
@Api(tags = "文件上传")
public class FileUploadController {
    @Autowired
//    @Qualifier("uploadService")
    private UploadService uploadService;

    @PostMapping("invoice/upload")
    @ResponseBody
    public Result upload(@RequestPart("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>(16);
        if (file.isEmpty()) {
            return new Result().error("文件不能为空");
        }
        Map<String, Object> upload = uploadService.upload(file);
        return new Result().ok(upload, "上传成功");
    }
}
