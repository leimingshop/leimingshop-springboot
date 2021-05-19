/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.logistic.kuaidi100.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryDTO;
import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryResponseDTO;
import com.leimingtech.modules.dto.logistic.kuaidi100.SubsrcibeRequestDTO;
import com.leimingtech.modules.service.logistic.kuaidi100.Kuaidi100Service;
import com.leimingtech.modules.util.MD5Util;
import com.leimingtech.util.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 快递一百接口
 * @Date: 2019/6/22 15:25
 * @Version: V1.0
 */
@Slf4j
@Service
@Transactional
public class Kuaidi100ServiveImpl implements Kuaidi100Service {

    /**
     * 实时查询请求地址
     */
    private static final String SYNQUERY_URL = "http://poll.kuaidi100.com/poll/query.do";

    /**
     * 提交订阅接口
     */
    private static final String SUBSCRIBE_URL = "http://poll.kuaidi100.com/poll";

    /**
     * @param realTimeQueryDTO: 参数实体
     * @Author: SWH ab4856812@163.com
     * @Description: 快递100实时查询快递信息接口
     * @Date: 2019/7/31 10:12
     * @Version: V1.0
     */

    @Override
    public RealTimeQueryResponseDTO realtime(@RequestBody RealTimeQueryDTO realTimeQueryDTO) {
        RealTimeQueryResponseDTO realTimeQueryResponseDTO = new RealTimeQueryResponseDTO();

        StringBuilder param = new StringBuilder("{");
        param.append("\"com\":\"").append(realTimeQueryDTO.getCom()).append("\"");
        param.append(",\"num\":\"").append(realTimeQueryDTO.getNum()).append("\"");
        param.append(",\"phone\":\"").append(realTimeQueryDTO.getPhone()).append("\"");
        param.append(",\"from\":\"").append(realTimeQueryDTO.getFrom()).append("\"");
        param.append(",\"to\":\"").append(realTimeQueryDTO.getTo()).append("\"");
        if (null != realTimeQueryDTO.getResultv2() && 1 == realTimeQueryDTO.getResultv2()) {
            param.append(",\"resultv2\":1");
        } else {
            param.append(",\"resultv2\":0");
        }
        param.append("}");
        String customer = realTimeQueryDTO.getCustomer();
        String key = realTimeQueryDTO.getKey();
        Map<String, String> params = new HashMap<>();
        params.put("customer", customer);
        String sign = MD5Util.encode(param + key + customer);
        params.put("sign", sign);
        params.put("param", param.toString());
        JSONObject jsonObject = HttpsUtil.post(SYNQUERY_URL, params);
        log.info("快递100参数:[{}],响应结果:[{}]", param, jsonObject);
        realTimeQueryResponseDTO = JSON.toJavaObject(jsonObject, RealTimeQueryResponseDTO.class);
        return realTimeQueryResponseDTO;
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 快递100实订阅快递信息
     * @Date: 2019/7/31 15:59
     * @Version: V1.0
     */

    @Override
    public Map<String, Object> subsrcibe(@RequestBody SubsrcibeRequestDTO subsrcibeRequestDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        StringBuilder param = new StringBuilder("{");
        param.append("\"company\":\"").append(subsrcibeRequestDTO.getCompany()).append("\"");
        param.append(",\"number\":\"").append(subsrcibeRequestDTO.getNumber()).append("\"");
        param.append(",\"from\":\"").append(subsrcibeRequestDTO.getFrom()).append("\"");
        param.append(",\"to\":\"").append(subsrcibeRequestDTO.getTo()).append("\"");
        param.append(",\"key\":\"").append(subsrcibeRequestDTO.getKey()).append("\"");
        param.append(",\"parameters\":{");
        param.append("\"callbackurl\":\"").append(subsrcibeRequestDTO.getCallbackurl()).append("\"");
        if (null != subsrcibeRequestDTO.getResultv2() && 1 == subsrcibeRequestDTO.getResultv2()) {
            param.append(",\"resultv2\":1");
        } else {
            param.append(",\"resultv2\":0");
        }
        param.append(",\"phone\":\"").append(subsrcibeRequestDTO.getPhone()).append("\"");
        param.append("}");
        param.append("}");
        Map<String, String> params = new HashMap<>();
        params.put("schema", "json");
        params.put("param", param.toString());
        JSONObject jsonObject = HttpsUtil.post(SUBSCRIBE_URL, params);
        resultMap.put("result", jsonObject.get("result"));
        resultMap.put("returnCode", jsonObject.get("returnCode"));
        resultMap.put("message", jsonObject.getString("message"));
        return resultMap;
    }
}
