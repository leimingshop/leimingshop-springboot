/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.message.dto.SysMailLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 邮件发送记录
 */

public interface SysMailLogService {

    /**
     * 邮件发送记录列表
     *
     * @param params 条件参数
     * @return PageData<SysMailLogDTO> 分页数据信息
     */

    PageData<SysMailLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据邮件记录id集合删除记录
     *
     * @param ids id集合
     * @return Boolean 操作结果：true成功；false失败
     */

    Boolean deleteBatchIds(@RequestBody Long[] ids);
}

