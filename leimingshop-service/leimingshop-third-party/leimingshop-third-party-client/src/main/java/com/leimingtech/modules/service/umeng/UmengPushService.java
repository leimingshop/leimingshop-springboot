/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.umeng;

import com.leimingtech.modules.dto.sendDTO.AppUnicastDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface UmengPushService {


    /**
     * @return
     * @throws Exception
     * @Description IOS 单推
     * @author zhangsenhao
     * @Copyrights 2016年10月17日下午4:57:54 handongkeji All rights reserved.
     */

    Boolean sendIOSSingle(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception;

    /**
     * @return
     * @throws Exception
     * @Description IOS 全推
     */

    Boolean sendIOSBroad(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception;


    /**
     * 单推
     *
     * @param appUnicastDTO
     * @return
     * @throws Exception
     */

    Boolean sendAndroidSingle(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception;

    /**
     * @return
     * @throws Exception
     * @Description 安卓全推
     * @Site http://www.handongkeji.com
     * @Copyrights 2016年10月17日下午4:58:25 handongkeji All rights reserved.
     */

    Boolean sendAndroidBroad(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception;


    void sendSingle(@RequestBody List<AppUnicastDTO> appUnicastDTOS);
}
