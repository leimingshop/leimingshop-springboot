/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.VerifyImageUtil;
import com.leimingtech.front.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 滑动验证码
 * @Date: 2019/7/15 18:39
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("auth")
@Api(tags = "滑动验证码管理")
public class AuthSlideController {

    private static final String KEY_VALIDATE_IMG = "key_validate_img";

    private static final String KEY_VALIDATE_TPL = "key_validate_tpl";

    private static final String KEY_VALIDATE_TOKEN = "key_validate_token";

    private static final String SUFFIX = "!/";

    /**
     * 验证码5分钟过期
     */
    private static final long EXPIRE = 60 * 5L;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CaptchaService captchaService;

    /**
     * 生成验证码
     *
     * @return
     */
    @GetMapping("init")
    @ResponseBody
    @ApiOperation(value = "生成滑动验证码")
    public JSONObject init() throws IOException {
        JSONObject object = new JSONObject();

//        /*redis实现:使用base64编码转化成字符串处理*/
        List<String> imgListString = (List<String>) redisUtils.get(KEY_VALIDATE_IMG);
        List<String> tpllistString = (List<String>) redisUtils.get(KEY_VALIDATE_TPL);
        List<byte[]> imgList = new ArrayList();
        List<byte[]> tpllist = new ArrayList();
        imgListString = new ArrayList<>();
        tpllistString = new ArrayList<>();
        for (String string : imgListString) {
            byte[] bytes = string.getBytes();
            imgList.add(bytes);
        }
        for (String string : tpllistString) {
            byte[] bytes = string.getBytes();
            tpllist.add(bytes);
        }
        if (CollectionUtils.isEmpty(imgListString) || CollectionUtils.isEmpty(tpllistString)) {
            initValidateResources(imgList, tpllist);
            redisUtils.set(KEY_VALIDATE_IMG, imgList, EXPIRE);
            redisUtils.set(KEY_VALIDATE_TPL, tpllist, EXPIRE);
        }

        log.info("redis数据初始化完毕");
        byte[] targetIs = null;
        byte[] templateIs = null;
        Random ra = new Random();
        if (!CollectionUtils.isEmpty(imgList)) {
            int rd = ra.nextInt(imgList.size());
            targetIs = imgList.get(rd);
        }
        if (!CollectionUtils.isEmpty(tpllist)) {
            int rd = ra.nextInt(tpllist.size());
            templateIs = tpllist.get(rd);
        }
        log.info("设置值完毕");

        Map<String, Object> pictureMap = null;
        try {
            pictureMap = VerifyImageUtil.pictureTemplatesCut(templateIs, targetIs, "png", "jpg");
            String newImage = Base64Utils.encodeToString((byte[]) pictureMap.get("newImage"));
            String sourceImage = Base64Utils.encodeToString((byte[]) pictureMap.get("oriCopyImage"));
            String baseImage = Base64Utils.encodeToString((byte[]) pictureMap.get("baseImage"));
            int x = (int) pictureMap.get("X");
            int y = (int) pictureMap.get("Y");
            object.put("baseImage", baseImage);
            object.put("newImage", newImage);
            object.put("sourceImage", sourceImage);
            // 前端赶进度。后期待删除X
            object.put("X", x);
            object.put("Y", y);

            String token = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String, Object> tokenObj = new HashMap<>(10);
            tokenObj.put("token", token);
            tokenObj.put("X", x);
            tokenObj.put("Y", y);
            redisUtils.set(KEY_VALIDATE_TOKEN + ":" + token, tokenObj, EXPIRE);
            object.put("token", token);
        } catch (Exception e) {
            log.error("", e);
        }
        return object;
    }


    /**
     * 初始化验证图形生成资源
     *
     * @param imgList
     * @param tpllist
     */
    private void initValidateResources(List<byte[]> imgList, List<byte[]> tpllist) throws IOException {
        /*加载验证原图*/
        String target = URLDecoder.decode(AuthSlideController.class.getClassLoader().getResource("image").getPath(), "UTF-8");
        byte[] targetIs = null;
        byte[] templateIs = null;
        //jar包
        if (target.indexOf(SUFFIX) != -1) {
            String jarPath = "jar:" + target;
            log.info(jarPath);
            URL jarUrl = new URL(jarPath);
            JarURLConnection jarCon = (JarURLConnection) jarUrl.openConnection();
            JarFile jarFile = jarCon.getJarFile();
            Enumeration<JarEntry> jarEntrys = jarFile.entries();
            while (jarEntrys.hasMoreElements()) {
                JarEntry entry = jarEntrys.nextElement();
                String name = entry.getName();
                Boolean flag = name.startsWith("image") && !("image").equals(name);
                Boolean picFlag = name.endsWith(".jpg") || name.endsWith(".png");
                Boolean templeFlag = name.startsWith("temple") && !("temple").equals(name);

                if (flag && picFlag) {
                    log.info("targets=" + name);
                    jarFile.getInputStream(entry);
                    targetIs = IOUtils.toByteArray(jarFile.getInputStream(entry));
                    imgList.add(targetIs);

                } else if (picFlag && templeFlag) {
                    log.info("templates=" + name);
                    jarFile.getInputStream(entry);
                    templateIs = IOUtils.toByteArray(jarFile.getInputStream(entry));
                    tpllist.add(templateIs);
                }
            }
        } else {
            File targetBaseFile = new File(target);
            if (null != targetBaseFile) {
                File[] fs = targetBaseFile.listFiles();
                for (File f : fs) {
                    targetIs = IOUtils.toByteArray(new FileInputStream(f));
                    imgList.add(targetIs);
                }
            }
            /*加载切图模板*/
            String template = URLDecoder.decode(AuthSlideController.class.getClassLoader().getResource("temple").getFile(), "UTF-8");
            File templateBaseFile = new File(template);
            if (null != templateBaseFile) {
                File[] fs = templateBaseFile.listFiles();
                for (File f : fs) {
                    templateIs = IOUtils.toByteArray(new FileInputStream(f));
                    tpllist.add(templateIs);
                }
            }
        }
        log.info("initValidateResources:template size:" + tpllist.size() + "target size:" + imgList.size());
    }


    /**
     * 验证方法 (有验证码的方法提交，有时候也可以带上验证参数，做后端二次验证)
     *
     * @return
     */
    @PostMapping("check")
    @ApiOperation(value = "验证滑动验证码")
    public Map<String, Object> check(@RequestParam("token") String token, @RequestParam("X") int X, @RequestParam("Y") int Y) {
        return captchaService.validateSlideCode(token, X, Y);
    }


}