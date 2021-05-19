/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.message.dto.SysSmsDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 短信
 */

public interface SysSmsService {

    /**
     * 短信发送结果分页查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    PageData<SysSmsDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param params 短信参数
     */

    void send(@RequestParam("mobile") String mobile, @RequestParam("params") String params);

    /**
     * 根据模板选择发送短信
     *
     * @param mobile 手机号
     * @param params 短信参数
     * @param code   模板code
     */

    void send(@RequestParam("mobile") String mobile, @RequestParam("params") String params,
              @RequestParam("code") String code);

    /**
     * 根据模板批量发送短信
     *
     * @param mobileJson   手机号数组
     * @param paramsJson   数据数组
     * @param signNameJson 签名数组
     * @param code         模板code
     */

    void sendBatchSms(@RequestParam("mobileJson") String mobileJson,
                      @RequestParam("paramsJson") String paramsJson,
                      @RequestParam("signNameJson") String signNameJson,
                      @RequestParam("code") String code);

    /**
     * 根据发送记录id，删除发送记录
     *
     * @param ids 短信发送记录Id数组
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    boolean deleteBatchIds(@RequestBody Long[] ids);

}



