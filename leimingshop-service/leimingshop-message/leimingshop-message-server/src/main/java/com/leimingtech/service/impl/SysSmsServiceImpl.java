/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dao.SysSmsDao;
import com.leimingtech.entity.SysSmsEntity;
import com.leimingtech.message.dto.SysSmsDTO;
import com.leimingtech.message.exception.ModuleErrorCode;
import com.leimingtech.message.service.SysSmsService;
import com.leimingtech.sms.AbstractSmsService;
import com.leimingtech.sms.SmsFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信发送方法实现类
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
@Service

public class SysSmsServiceImpl extends BaseServiceImpl<SysSmsDao, SysSmsEntity> implements SysSmsService {

    @Override

    public PageData<SysSmsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SysSmsEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, SysSmsDTO.class);
    }

    private QueryWrapper<SysSmsEntity> getWrapper(Map<String, Object> params) {
        String mobile = (String) params.get("mobile");
        String status = (String) params.get("status");

        QueryWrapper<SysSmsEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(mobile), "mobile", mobile);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);

        return wrapper;
    }

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param params 短信参数
     * @param code   短信模板
     */
    @Override

    public void send(@RequestParam("mobile") String mobile, @RequestParam("params") String params,
                     @RequestParam("code") String code) {
        LinkedHashMap<String, String> map;
        try {
            map = JSON.parseObject(params, LinkedHashMap.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_FORMAT_ERROR);
        }

        //短信服务
        AbstractSmsService service = SmsFactory.build();
        if (service == null) {
            throw new CustomException(ModuleErrorCode.SMS_CONFIG);
        }
        for (String s : map.keySet()) {
            String val = map.get(s);
            if (val.length() > 20) {
                String substring = val.substring(0, 17) + "...";
            }
            map.put(s, val);
        }
        //发送短信
        service.sendSms(mobile, map, code);
    }

    /**
     * 批量发送短信
     *
     * @param mobileJson   手机号数组
     * @param paramsJson   数据数组
     * @param signNameJson 签名数组
     * @param code         模板code
     */
    @Override

    public void sendBatchSms(@RequestParam("mobileJson") String mobileJson,
                             @RequestParam("paramsJson") String paramsJson,
                             @RequestParam("signNameJson") String signNameJson,
                             @RequestParam("code") String code) {
        /*LinkedHashMap<String, String> map
        try {
            map = JSON.parseObject(paramsJson, LinkedHashMap.class)
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_FORMAT_ERROR)
        }*/

        //短信服务
        AbstractSmsService service = SmsFactory.build();
        if (service == null) {
            throw new CustomException(ModuleErrorCode.SMS_CONFIG);
        }

        //发送短信
        service.sendBatchSms(mobileJson, paramsJson, signNameJson, code);
    }

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param params 短信参数
     */
    @Override

    public void send(@RequestParam("mobile") String mobile, @RequestParam("params") String params) {
        LinkedHashMap<String, String> map;
        try {
            map = JSON.parseObject(params, LinkedHashMap.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_FORMAT_ERROR);
        }

        //短信服务
        AbstractSmsService service = SmsFactory.build();
        if (service == null) {
            throw new CustomException(ModuleErrorCode.SMS_CONFIG);
        }

        //发送短信
        service.sendSms(mobile, map);
    }


    @Override

    public boolean deleteBatchIds(@RequestBody Long[] ids) {
        deleteBatchIds(Arrays.asList(ids));
        return true;
    }

    public void save(Integer platform, String mobile, LinkedHashMap<String, String> params, Integer status) {
        SysSmsEntity sms = new SysSmsEntity();
        sms.setPlatform(platform);
        sms.setMobile(mobile);

        //设置短信参数
        if (MapUtil.isNotEmpty(params)) {
            int index = 1;
            for (String value : params.values()) {
                if (index == 1) {
                    sms.setParams1(value);
                } else if (index == 2) {
                    sms.setParams2(value);
                } else if (index == 3) {
                    sms.setParams3(value);
                } else if (index == 4) {
                    sms.setParams4(value);
                }
                index++;
            }
        }

        sms.setStatus(status);

        this.insert(sms);
    }

}
